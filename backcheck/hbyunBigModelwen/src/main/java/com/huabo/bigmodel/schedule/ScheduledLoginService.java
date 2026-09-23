package com.huabo.bigmodel.schedule;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.PreDestroy;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * AI定时任务代登录服务（移植自 hbyunSystemSetting /login/loginCheck 完整流程，去除密码校验）
 *
 * 与人工登录同体系、同存储，业务网关无感知：
 * - 用户/组织/权限/账套/兼职查询：与 hbyunSystemSetting 同一达梦库（REDACTED），SQL 逐条对齐原 mapper
 * - token 生成：AES(staffid, token-key) + URL 编码，算法对齐主平台 EncryptUtil.AESencode
 *   （SHA1PRNG 种子派生 128 位密钥，Cipher "AES"，标准 Base64）
 * - 会话写入：Redis SETEX <staffid>account = 用户上下文 JSON，TTL 3600 秒，
 *   对齐主平台 UserProvider.add —— 业务侧按 staffid 查 Redis 校验 token，本服务写入的
 *   会话与用户手工登录产生的完全等价，因此任务的全部操作都记录在任务创建人名下
 * - 登录日志：TBL_USER_LOGIN_LOG 落一条 "用户登录成功"，LOGIN_DEVICE 标记为 ai-scheduled-task
 *
 * token-key 为与主平台对称的兼容密钥，默认值即主平台 EncryptUtil.DESKEY 常量
 * （application.yml 内置，与主平台自身的硬编码约定一致），特殊环境可用环境变量覆盖。
 */
@Slf4j
@Service
public class ScheduledLoginService {

    /** 用户主查询：对齐 TblStaffMapper.selectUniqueStaffInfoByname（去除密码条件即免密登录） */
    private static final String SQL_STAFF =
            "SELECT TS.STAFFID, TS.REALNAME, TS.USERNAME, TS.ADDRESS, TS.PKYMSTAFFID, TS.MIBLEPHONE,"
                    + " TS.MEMO, TS.STATUS, TS.CREATETIME, TS.JOBID, TS.OUTSIDEID, TS.OUTSIDEOPENDID,"
                    + " TS.EMAIL, TS.FIXEDPHONE, TS.ROLEIDSTRS, TS.HISTORYCODE, TS.SECRECTLEVELID,"
                    + " TSL.LEVELNAME AS SECRECTLEVELNAME, ORG.ORGID AS DEPT_ORGID, ORG.ORGNAME AS DEPT_ORGNAME"
                    + " FROM TBL_STAFF TS"
                    + " LEFT JOIN TBL_SECRECT_LEVEL TSL ON TS.SECRECTLEVELID = TSL.LEVELID"
                    + " LEFT JOIN TBL_ORGANIZATION ORG ON TS.ORGID = ORG.ORGID"
                    + " WHERE TS.USERNAME = ?";

    /** 组织查询：对齐 TblOrganizationMapper.selectByOrgid 的关键列 */
    private static final String SQL_ORG =
            "SELECT ORGID, ORGNAME, FATHERORGID, ORGNUMBER, ORGMENO, MEMO, ICODE, ORGTYPE, STATUS,"
                    + " ISZY, USESECRECT, JTORGID, JTORGNAME, BGIMAGE, BGNAME, LOGOIMAGE, LOGONAME,"
                    + " JDZTIMAGE, JDZTNAME, CTZTIMAGE, CTZTNAME, BANAME"
                    + " FROM TBL_ORGANIZATION WHERE ORGID = ?";

    /** 数据权限部门：对齐 TblStaffMapper.selectDateDeptRelation */
    private static final String SQL_DATA_RIGHT_DEPT =
            "SELECT DEPTIDSTRS FROM TBL_SYSTEM_DATA_RIGHT WHERE ORGID = ? AND ROLEID IN (%s)";

    /** 密级可见范围：对齐 TblSecrectLevelMapper.selectMenuScopeById */
    private static final String SQL_SECRET_SCOPE =
            "SELECT SECRECYMENUSCOPE FROM TBL_SECRECT_LEVEL WHERE LEVELID = ?";

    /** 账套：对齐 TblStaffMapper.selectUserBookInfo */
    private static final String SQL_ACCBOOK =
            "SELECT PK_ACCBOOKINFO, PK_SETOFBOOK, CONVERT_DATE, LOCALORIGINVALUE, BODYVOS, BOOKNAME,"
                    + " PK_FINANPLANID, ACCBOOKTYPECODE, ACCBOOKTYPENAME FROM FA_ACCBOOKINFO"
                    + " WHERE PK_ACCBOOKINFO = (SELECT ACCBOOKID FROM FA_ACCBOOK_USER WHERE STAFFID = ?)";

    /** 兼职部门：对齐 TblUserOrgRelationMapperSqlConfig.selectUserOrgRelationInfoListByStaffId（达梦 || 拼接 LONGNAME） */
    private static final String SQL_ORG_RELATION =
            "SELECT UOR.STAFFID, UOR.ORGSTRS, UOR.DEPTID, UOR.ORGID, UOR.NUMNO, UOR.ORGYMSTRIDS,"
                    + " ORG.ORGNAME AS ORGNAME, DEPT.ORGNAME AS DETPNAME,"
                    + " (ORG.ORGNAME || '/' || DEPT.ORGNAME) AS LONGNAME"
                    + " FROM TBL_USER_ORGRELATION UOR"
                    + " LEFT JOIN TBL_ORGANIZATION ORG ON UOR.ORGID = ORG.ORGID"
                    + " LEFT JOIN TBL_ORGANIZATION DEPT ON UOR.DEPTID = DEPT.ORGID"
                    + " WHERE UOR.STAFFID = ?";

    /** 登录日志：对齐 UserLoginLog 实体的 TBL_USER_LOGIN_LOG 列映射 */
    private static final String SQL_LOGIN_LOG =
            "INSERT INTO TBL_USER_LOGIN_LOG (ID, LOGIN_TIME, USER_ID, USER_NAME, LOGIN_IP, LOGIN_DEVICE,"
                    + " LOGIN_MSG, LOGIN_RES, CREATED_AT, REAL_NAME) VALUES (?,?,?,?,?,?,?,?,?,?)";

    private static final String LOGIN_DEVICE_TAG = "ai-scheduled-task";

    private final JdbcTemplate dm;

    @Value("${scheduled-login.redis.host:127.0.0.1}")
    private String redisHost;

    @Value("${scheduled-login.redis.port:6379}")
    private int redisPort;

    @Value("${scheduled-login.redis.password:}")
    private String redisPassword;

    /** 与主平台 EncryptUtil.DESKEY 一致；默认值内置于 application.yml（平台级兼容常量），可经环境变量覆盖 */
    @Value("${scheduled-login.token-key:}")
    private String tokenKey;

    /** 会话有效期，对齐主平台 UserProvider.add 的 3600 秒 */
    @Value("${scheduled-login.session-ttl-seconds:3600}")
    private int sessionTtlSeconds;

    /** 对齐主平台 SystemStaticValue.REQUIREMENTVALIDATE */
    @Value("${scheduled-login.require-valuedata:false}")
    private boolean requireValuedata;

    private volatile JedisPool jedisPool;

    public ScheduledLoginService(@Qualifier("dmJdbcTemplate") JdbcTemplate dm) {
        this.dm = dm;
    }

    /**
     * 按登录账号代登录换取 token（完整登录流程、无密码校验）。
     * 成功返回与人工登录同体系的 token 并已写 Redis 会话；任何失败返回 null（调用方回退旧凭证），
     * 失败细节记入服务日志与登录日志，不抛异常以避免阻断任务发送。
     *
     * @param userName 登录账号（TBL_STAFF.USERNAME）
     */
    public String loginForToken(String userName) {
        if (userName == null || userName.trim().isEmpty()) return null;
        String account = userName.trim();
        Map<String, Object> staff = null;
        try {
            // 1. 按用户名取用户（原流程的密码校验即"用户名+密码"联合查询，此处仅按用户名，等价于免密分支）
            List<Map<String, Object>> staffRows = dm.queryForList(SQL_STAFF, account);
            if (staffRows.isEmpty()) {
                log.warn("[代登录] 用户不存在 userName={}", account);
                insertLoginLog(account, null, null, "用户不存在！", "1");
                return null;
            }
            staff = staffRows.get(0);
            // 2. 状态校验（status=0 禁用）
            if (intValue(staff.get("STATUS")) == 0) {
                log.warn("[代登录] 用户已被禁用 userName={}", account);
                insertLoginLog(account, staff, null, "该用户已被禁用！", "1");
                return null;
            }
            // 3. 角色校验
            String roleIdStrs = str(staff.get("ROLEIDSTRS"));
            if (roleIdStrs == null || roleIdStrs.trim().isEmpty()) {
                log.warn("[代登录] 用户未授权角色 userName={}", account);
                insertLoginLog(account, staff, null, "用户未授权角色！", "1");
                return null;
            }
            // 4. 组装与人工登录完全一致的用户上下文并写 Redis 会话
            String roleNames = queryRoleNames(roleIdStrs);
            JSONObject context = buildStaffContext(staff, roleIdStrs, roleNames);
            String staffid = str(staff.get("STAFFID"));
            String token = UriUtils.encode(aesEncrypt(staffid), "utf-8");
            writeSession(staffid, context.toJSONString());
            // 5. 登录日志记在创建人名下（LOGIN_DEVICE 标记来源于定时任务，便于审计区分）
            insertLoginLog(account, staff, str(staff.get("REALNAME")), "用户登录成功", "0");
            log.info("[代登录] 成功 userName={} staffid={}", account, staffid);
            return token;
        } catch (Exception e) {
            log.error("[代登录] 失败 userName={}", account, e);
            insertLoginLog(account, staff, null, "登录异常：" + e.getMessage(), "1");
            return null;
        }
    }

    /**
     * 组装用户上下文 JSON（字段名对齐主平台 TblStaffUtil，
     * 业务侧 fastjson 反序列化要求字段一致，缺失字段等价于"查无数据"）
     */
    private JSONObject buildStaffContext(Map<String, Object> staff, String roleIdStrs, String roleNames) {
        JSONObject ctx = new JSONObject(true);
        ctx.put("staffid", staff.get("STAFFID"));
        ctx.put("realname", str(staff.get("REALNAME")));
        ctx.put("username", str(staff.get("USERNAME")));
        ctx.put("address", str(staff.get("ADDRESS")));
        ctx.put("email", str(staff.get("EMAIL")));
        ctx.put("miblephone", str(staff.get("MIBLEPHONE")));
        ctx.put("memo", str(staff.get("MEMO")));
        ctx.put("fixedphone", str(staff.get("FIXEDPHONE")));
        ctx.put("jobid", staff.get("JOBID"));
        Object createTime = staff.get("CREATETIME");
        if (createTime instanceof Date) {
            // fastjson 对 Date 的默认序列化为毫秒时间戳，与主平台保持一致
            ctx.put("createDate", ((Date) createTime).getTime());
        }
        ctx.put("status", intValue(staff.get("STATUS")));
        ctx.put("outSideId", staff.get("OUTSIDEID"));
        ctx.put("outSideOpenId", str(staff.get("OUTSIDEOPENDID")));
        ctx.put("historycode", str(staff.get("HISTORYCODE")));
        ctx.put("roleIdStrs", roleIdStrs);
        ctx.put("roleNames", roleNames);
        ctx.put("pkYmStaffId", str(staff.get("PKYMSTAFFID")));
        ctx.put("secrectLevelId", staff.get("SECRECTLEVELID"));
        ctx.put("secrectLevelName", str(staff.get("SECRECTLEVELNAME")));
        ctx.put("requireValuedata", requireValuedata);
        ctx.put("workList", new JSONArray());

        // 所属部门（登录主查询仅携带 orgid/orgname，与原流程一致）
        String deptOrgid = str(staff.get("DEPT_ORGID"));
        if (deptOrgid != null && !deptOrgid.isEmpty()) {
            JSONObject dept = new JSONObject(true);
            dept.put("orgid", staff.get("DEPT_ORGID"));
            dept.put("orgname", str(staff.get("DEPT_ORGNAME")));
            ctx.put("linkDetp", dept);

            // 部门向上找所属公司（对齐 findCompanyInfoByDeptId：orgtype=0 为部门，递归到非 0）
            Map<String, Object> linkOrg = findCompanyByDept(new java.math.BigDecimal(deptOrgid));
            if (linkOrg != null) {
                JSONObject orgJson = toOrgJson(linkOrg);
                ctx.put("linkOrg", orgJson);
                ctx.put("currentOrg", orgJson);
                // 数据权限部门集合（对齐 selectDateDeptRelation + 原始拼装分支）
                ctx.put("deptIds", queryDeptIds(roleIdStrs, str(linkOrg.get("ORGID"))));
                // 集团根组织（对齐 getRootCompanyByOrgId：沿父链递归至 FATHERORGID=-1）
                Map<String, Object> rootOrg = findRootCompany(str(linkOrg.get("ORGID")));
                if (rootOrg != null) {
                    ctx.put("groupOrg", toOrgJson(rootOrg));
                }
                // 密级可见范围（公司启用密级时）
                if (intValue(linkOrg.get("USESECRECT")) == 1 && staff.get("SECRECTLEVELID") != null) {
                    List<Map<String, Object>> rows = dm.queryForList(SQL_SECRET_SCOPE, staff.get("SECRECTLEVELID"));
                    if (!rows.isEmpty()) {
                        ctx.put("secrectScopeIds", str(rows.get(0).get("SECRECYMENUSCOPE")));
                    }
                }
            }
        }

        // 账套
        List<Map<String, Object>> books = dm.queryForList(SQL_ACCBOOK, staff.get("STAFFID"));
        if (!books.isEmpty()) {
            Map<String, Object> b = books.get(0);
            JSONObject book = new JSONObject(true);
            book.put("pkAccbookinfo", str(b.get("PK_ACCBOOKINFO")));
            book.put("pkSetofbook", str(b.get("PK_SETOFBOOK")));
            book.put("convertDate", b.get("CONVERT_DATE"));
            book.put("localoriginvalue", str(b.get("LOCALORIGINVALUE")));
            book.put("bodyvos", str(b.get("BODYVOS")));
            book.put("bookName", str(b.get("BOOKNAME")));
            book.put("pkFinanplanid", str(b.get("PK_FINANPLANID")));
            book.put("accbooktypecode", str(b.get("ACCBOOKTYPECODE")));
            book.put("accbooktypename", str(b.get("ACCBOOKTYPENAME")));
            ctx.put("accbook", book);
        }

        // 兼职部门列表
        List<Map<String, Object>> relations = dm.queryForList(SQL_ORG_RELATION, staff.get("STAFFID"));
        JSONArray relaList = new JSONArray();
        for (Map<String, Object> r : relations) {
            JSONObject rela = new JSONObject(true);
            rela.put("staffid", r.get("STAFFID"));
            rela.put("orgstrs", str(r.get("ORGSTRS")));
            rela.put("deptId", r.get("DEPTID"));
            rela.put("orgId", r.get("ORGID"));
            rela.put("numno", r.get("NUMNO"));
            rela.put("orgYmStrIds", str(r.get("ORGYMSTRIDS")));
            rela.put("orgName", str(r.get("ORGNAME")));
            rela.put("detpName", str(r.get("DETPNAME")));
            rela.put("longName", str(r.get("LONGNAME")));
            relaList.add(rela);
        }
        ctx.put("relaList", relaList);
        return ctx;
    }

    /** 角色名（对齐 TblRoleMapper.findByRidList） */
    private String queryRoleNames(String roleIdStrs) {
        List<String> rids = Arrays.asList(roleIdStrs.split(","));
        String placeholders = String.join(",", java.util.Collections.nCopies(rids.size(), "?"));
        List<Map<String, Object>> roles = dm.queryForList(
                "SELECT RNAME FROM TBL_ROLE WHERE RID IN (" + placeholders + ")", rids.toArray());
        StringBuilder names = new StringBuilder();
        for (Map<String, Object> r : roles) {
            if (names.length() > 0) names.append(',');
            names.append(str(r.get("RNAME")));
        }
        return names.toString();
    }

    /** 数据权限部门集合（对齐 setOracleStaffRelationInfo 的 deptIds 拼装逻辑） */
    private String queryDeptIds(String roleIdStrs, String companyOrgid) {
        String placeholders = String.join(",", java.util.Collections.nCopies(roleIdStrs.split(",").length, "?"));
        Object[] args = new Object[1 + roleIdStrs.split(",").length];
        args[0] = new java.math.BigDecimal(companyOrgid);
        System.arraycopy(roleIdStrs.split(","), 0, args, 1, roleIdStrs.split(",").length);
        List<Map<String, Object>> rows = dm.queryForList(
                String.format(SQL_DATA_RIGHT_DEPT, placeholders), args);
        if (rows.isEmpty()) return null;
        // 多角色取 DEPTIDSTRS 逐行拼接（原流程 selectList 后 join）
        StringBuilder joined = new StringBuilder();
        for (Map<String, Object> row : rows) {
            String s = str(row.get("DEPTIDSTRS"));
            if (s == null || s.isEmpty()) continue;
            if (joined.length() > 0) joined.append(',');
            joined.append(s);
        }
        String deptStrIds = joined.toString();
        if (deptStrIds.isEmpty()) return null;
        Set<String> deptSet = new HashSet<>();
        if (("," + deptStrIds + ",").indexOf(companyOrgid) == -1) {
            deptSet.addAll(Arrays.asList(deptStrIds.split(",")));
        } else {
            collectChildDepts(new java.math.BigDecimal(companyOrgid), deptSet);
        }
        return String.join(",", deptSet);
    }

    /** 公司下全部子部门（对齐 selectSetDeptIdsByOrgId 递归，SQL=一层子部门查询） */
    private void collectChildDepts(java.math.BigDecimal orgid, Set<String> deptSet) {
        List<Map<String, Object>> children = dm.queryForList(
                "SELECT ORGID FROM TBL_ORGANIZATION WHERE ORGTYPE = 0 AND FATHERORGID = ?", orgid);
        for (Map<String, Object> child : children) {
            String id = str(child.get("ORGID"));
            deptSet.add(id);
            collectChildDepts(new java.math.BigDecimal(id), deptSet);
        }
    }

    /** 部门向上找公司：orgtype=0 为部门（对齐 findCompanyInfoByDeptId） */
    private Map<String, Object> findCompanyByDept(java.math.BigDecimal orgid) {
        List<Map<String, Object>> rows = dm.queryForList(SQL_ORG, orgid);
        if (rows.isEmpty()) return null;
        Map<String, Object> org = rows.get(0);
        if (intValue(org.get("ORGTYPE")) == 0) {
            Object father = org.get("FATHERORGID");
            if (father != null) {
                return findCompanyByDept(new java.math.BigDecimal(str(father)));
            }
        }
        return org;
    }

    /** 沿父链找集团根组织，FATHERORGID=-1 为根（对齐 getRootCompanyByOrgId） */
    private Map<String, Object> findRootCompany(String orgid) {
        String current = orgid;
        while (current != null) {
            List<Map<String, Object>> rows = dm.queryForList(SQL_ORG, new java.math.BigDecimal(current));
            if (rows.isEmpty()) return null;
            Map<String, Object> org = rows.get(0);
            Object father = org.get("FATHERORGID");
            if (father == null || "-1".equals(str(father)) || new java.math.BigDecimal(-1).compareTo(new java.math.BigDecimal(str(father))) == 0) {
                return org;
            }
            current = str(father);
        }
        return null;
    }

    /** 组织行 → TblOrganizationUtil 字段名 JSON */
    private JSONObject toOrgJson(Map<String, Object> org) {
        JSONObject o = new JSONObject(true);
        o.put("orgid", org.get("ORGID"));
        o.put("orgname", str(org.get("ORGNAME")));
        o.put("fatherorgid", org.get("FATHERORGID"));
        o.put("orgnumber", str(org.get("ORGNUMBER")));
        o.put("orgmeno", str(org.get("ORGMENO")));
        o.put("memo", str(org.get("MEMO")));
        o.put("icode", str(org.get("ICODE")));
        o.put("orgtype", intValue(org.get("ORGTYPE")));
        o.put("status", intValue(org.get("STATUS")));
        o.put("iszy", str(org.get("ISZY")));
        o.put("useSecrect", intValue(org.get("USESECRECT")));
        o.put("jtorgid", org.get("JTORGID"));
        o.put("jtorgname", str(org.get("JTORGNAME")));
        o.put("bgimage", str(org.get("BGIMAGE")));
        o.put("bgname", str(org.get("BGNAME")));
        o.put("logoimage", str(org.get("LOGOIMAGE")));
        o.put("logoname", str(org.get("LOGONAME")));
        o.put("jdztimage", str(org.get("JDZTIMAGE")));
        o.put("jdztname", str(org.get("JDZTNAME")));
        o.put("ctztimage", str(org.get("CTZTIMAGE")));
        o.put("ctztname", str(org.get("CTZTNAME")));
        o.put("baname", str(org.get("BANAME")));
        return o;
    }

    /**
     * AES 加密（对齐主平台 EncryptUtil.AESencode）：
     * SHA1PRNG 以 key 字节为种子派生 128 位密钥，Cipher "AES"，标准 Base64 输出。
     * 种子 getBytes() 保持与原实现相同的平台默认字符集行为（服务端均为 Linux/UTF-8，字节一致）。
     */
    private String aesEncrypt(String data) throws Exception {
        if (tokenKey == null || tokenKey.isEmpty()) {
            throw new IllegalStateException(
                    "scheduled-login.token-key 未配置（application.yml 内置默认值，勿覆盖为空；如需覆盖须与主平台 EncryptUtil.DESKEY 一致）");
        }
        KeyGenerator kg = KeyGenerator.getInstance("AES");
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        random.setSeed(tokenKey.getBytes());
        kg.init(128, random);
        SecretKeySpec key = new SecretKeySpec(kg.generateKey().getEncoded(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return Base64.getEncoder().encodeToString(cipher.doFinal(data.getBytes(StandardCharsets.UTF_8)));
    }

    /** 写主平台同款 Redis 会话：SETEX <staffid>account（对齐 UserProvider.add，TTL 3600s） */
    private void writeSession(String staffid, String json) {
        try (Jedis jedis = pool().getResource()) {
            jedis.setex(staffid + "account", sessionTtlSeconds, json);
        }
    }

    private JedisPool pool() {
        if (jedisPool == null) {
            synchronized (this) {
                if (jedisPool == null) {
                    if (redisPassword != null && !redisPassword.isEmpty()) {
                        jedisPool = new JedisPool(
                                new org.apache.commons.pool2.impl.GenericObjectPoolConfig(),
                                redisHost, redisPort, 5000, redisPassword);
                    } else {
                        jedisPool = new JedisPool(redisHost, redisPort);
                    }
                    log.info("[代登录] Redis 连接池已创建 {}:{}", redisHost, redisPort);
                }
            }
        }
        return jedisPool;
    }

    /** 登录日志（对齐 UserLoginLog 实体列；ID 用雪花，对齐原 addLoginLog） */
    private void insertLoginLog(String userName, Map<String, Object> staff, String realname, String msg, String res) {
        try {
            Long staffid = staff != null && staff.get("STAFFID") != null
                    ? Long.valueOf(str(staff.get("STAFFID"))) : 0L;
            dm.update(SQL_LOGIN_LOG,
                    cn.hutool.core.util.IdUtil.getSnowflakeNextId(),
                    new Date(), staffid, userName, localIp(), LOGIN_DEVICE_TAG,
                    msg, res, new Date(), realname != null ? realname : userName);
        } catch (Exception e) {
            // 日志失败不影响换票主流程
            log.warn("[代登录] 登录日志写入失败 userName={}: {}", userName, e.getMessage());
        }
    }

    private String localIp() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (Exception e) {
            return "0.0.0.0";
        }
    }

    private static String str(Object v) {
        return v == null ? null : String.valueOf(v).trim();
    }

    private static int intValue(Object v) {
        if (v == null) return -1;
        if (v instanceof Number) return ((Number) v).intValue();
        try {
            return new java.math.BigDecimal(String.valueOf(v)).intValue();
        } catch (Exception e) {
            return -1;
        }
    }

    @PreDestroy
    public void shutdown() {
        if (jedisPool != null && !jedisPool.isClosed()) {
            jedisPool.close();
        }
    }
}
