package com.huabo.bigmodel.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.huabo.bigmodel.common.Result;
import com.huabo.bigmodel.dto.AiChatRequest;
import com.huabo.bigmodel.service.AiChatService;
import com.alibaba.druid.pool.DruidDataSource;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;
import java.util.Base64;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * AI 数据治理 - 元数据采集 / 分页查询 / 治理上下文固化
 * <p>
 * 对接数据中台已配置的全部数据库（当前支持 MySQL / 达梦 DM8）：
 * 1) 数据源配置入库（GOV_DATA_SOURCE，密码 Base64 存储，生产环境建议替换为 AES）；
 * 2) 异步采集任务：分页读取 information_schema / 达梦数据字典，表元数据（含字段 JSON）固化到 GOV_TABLE_META，
 *    万表规模不撑爆前端内存与 localStorage，接口秒回，进度轮询；
 * 3) 表清单分页查询（关键字 / 主题域过滤），支撑资产目录页签；
 * 4) 治理上下文（govCtx）整体固化到 GOV_CONTEXT，替代前端 localStorage。
 * 表结构见 resources/gov_meta_ddl.sql（启动时自动建表，失败可手工执行）。
 *
 * @author AI 数据治理
 */
@Slf4j
@RestController
@RequestMapping("/v1/ai/governance/meta")
@Tag(name = "AI数据治理-元数据", description = "数据治理元数据采集/查询/上下文固化")
public class GovernanceMetaController {

    private final JdbcTemplate dmJdbcTemplate;
    private final AiChatService aiChatService;

    /** 采集任务状态：taskId -> 进度 JSON */
    private static final Map<String, JSONObject> SCAN_TASKS = new ConcurrentHashMap<>();
    private static final ExecutorService SCAN_EXECUTOR = Executors.newFixedThreadPool(2, r -> {
        Thread t = new Thread(r, "gov-meta-scan");
        t.setDaemon(true);
        return t;
    });

    /** AI 主题域归类：固定候选域（与 inferDomain 关键词规则同一套体系） */
    private static final String[] GOV_DOMAINS = {
        "财务金融", "采购供应链", "主数据与组织", "销售市场", "生产制造", "风险合规", "系统支撑"
    };

    public GovernanceMetaController(@Qualifier("dmJdbcTemplate") JdbcTemplate dmJdbcTemplate,
                                    AiChatService aiChatService) {
        this.dmJdbcTemplate = dmJdbcTemplate;
        this.aiChatService = aiChatService;
    }

    /** 启动时幂等建表（达梦 DM8 语法；失败时提示手工执行 gov_meta_ddl.sql） */
    @PostConstruct
    public void ensureTables() {
        String[] ddls = {
            "CREATE TABLE IF NOT EXISTS GOV_DATA_SOURCE (" +
                "ID VARCHAR(64) PRIMARY KEY, USER_ID VARCHAR(64), NAME VARCHAR(200), DB_TYPE VARCHAR(20), " +
                "HOST VARCHAR(200), PORT INT, DB_NAME VARCHAR(200), USERNAME VARCHAR(200), PASSWORD_B64 VARCHAR(800), " +
                "DOMAIN_NAME VARCHAR(100), TABLE_COUNT INT DEFAULT 0, DOMAIN_STATS CLOB, " +
                "LAST_SCAN_TIME VARCHAR(40), CREATE_TIME VARCHAR(40), UPDATE_TIME VARCHAR(40))",
            "CREATE TABLE IF NOT EXISTS GOV_TABLE_META (" +
                "SOURCE_ID VARCHAR(64) NOT NULL, TABLE_NAME VARCHAR(200) NOT NULL, TABLE_COMMENT VARCHAR(1000), " +
                "DOMAIN_NAME VARCHAR(100), SUB_DOMAIN VARCHAR(100), BIZ_OBJECT VARCHAR(100), " +
                "TABLE_ROWS BIGINT DEFAULT 0, COLUMN_COUNT INT DEFAULT 0, " +
                "COLUMNS_JSON CLOB, UPDATE_TIME VARCHAR(40), PRIMARY KEY (SOURCE_ID, TABLE_NAME))",
            "CREATE TABLE IF NOT EXISTS GOV_CONTEXT (" +
                "USER_ID VARCHAR(64) PRIMARY KEY, CTX_JSON CLOB, UPDATE_TIME VARCHAR(40))"
        };
        for (String sql : ddls) {
            try {
                dmJdbcTemplate.execute(sql);
            } catch (Exception e) {
                // 单表失败不中断其余建表（列名避免达梦保留字，见 DOMAIN_NAME）；
                // 表结构以 resources/db/gov_meta_ddl.sql 为准，可手工执行
                log.warn("[数据治理] 自动建表失败，请手工执行 resources/db/gov_meta_ddl.sql：{}", e.getMessage());
            }
        }
        // 存量表增量迁移：补 L2 子主题域 / L3 业务对象 列（已存在时忽略报错）
        String[] migrations = {
            "ALTER TABLE GOV_TABLE_META ADD SUB_DOMAIN VARCHAR(100)",
            "ALTER TABLE GOV_TABLE_META ADD BIZ_OBJECT VARCHAR(100)"
        };
        for (String sql : migrations) {
            try {
                dmJdbcTemplate.execute(sql);
            } catch (Exception ignore) {
                // 列已存在
            }
        }
        log.info("[数据治理] 元数据表初始化完成（GOV_DATA_SOURCE / GOV_TABLE_META / GOV_CONTEXT）");
    }

    /* ==================== 数据源配置 ==================== */

    @Operation(summary = "保存数据源配置", description = "新增或更新数据中台连接配置（密码 Base64 存储），返回数据源 id")
    @PostMapping("/sources")
    public Result<JSONObject> saveSource(@RequestBody JSONObject body) {
        try {
            String id = body.getString("id");
            boolean isNew = id == null || id.isEmpty();
            if (isNew) {
                id = "ds_" + UUID.randomUUID().toString().replace("-", "").substring(0, 20);
            }
            String now = now();
            String passwordB64 = body.getString("password") == null ? "" :
                Base64.getEncoder().encodeToString(body.getString("password").getBytes(StandardCharsets.UTF_8));
            if (isNew) {
                Integer exists = queryInt("SELECT COUNT(*) FROM GOV_DATA_SOURCE WHERE ID=?", id);
                if (exists != null && exists > 0) {
                    dmJdbcTemplate.update(
                        "UPDATE GOV_DATA_SOURCE SET USER_ID=?, NAME=?, DB_TYPE=?, HOST=?, PORT=?, DB_NAME=?, " +
                            "USERNAME=?, PASSWORD_B64=?, DOMAIN_NAME=?, UPDATE_TIME=? WHERE ID=?",
                        body.getString("userId"), body.getString("name"), body.getString("dbType"),
                        body.getString("host"), body.getInteger("port"), body.getString("dbName"),
                        body.getString("username"), passwordB64.isEmpty() ? null : passwordB64,
                        body.getString("domain"), now, id);
                } else {
                    dmJdbcTemplate.update(
                        "INSERT INTO GOV_DATA_SOURCE (ID, USER_ID, NAME, DB_TYPE, HOST, PORT, DB_NAME, USERNAME, " +
                            "PASSWORD_B64, DOMAIN_NAME, TABLE_COUNT, CREATE_TIME, UPDATE_TIME) VALUES (?,?,?,?,?,?,?,?,?,?,0,?,?)",
                        id, body.getString("userId"), body.getString("name"), body.getString("dbType"),
                        body.getString("host"), body.getInteger("port"), body.getString("dbName"),
                        body.getString("username"), passwordB64, body.getString("domain"), now, now);
                }
            } else {
                // 更新：密码为空表示不修改密码
                String oldPwd = queryString("SELECT PASSWORD_B64 FROM GOV_DATA_SOURCE WHERE ID=?", id);
                String pwd = passwordB64.isEmpty() ? oldPwd : passwordB64;
                dmJdbcTemplate.update(
                    "UPDATE GOV_DATA_SOURCE SET USER_ID=?, NAME=?, DB_TYPE=?, HOST=?, PORT=?, DB_NAME=?, " +
                        "USERNAME=?, PASSWORD_B64=?, DOMAIN_NAME=?, UPDATE_TIME=? WHERE ID=?",
                    body.getString("userId"), body.getString("name"), body.getString("dbType"),
                    body.getString("host"), body.getInteger("port"), body.getString("dbName"),
                    body.getString("username"), pwd, body.getString("domain"), now, id);
            }
            JSONObject data = new JSONObject();
            data.put("id", id);
            return Result.success("数据源已保存", data);
        } catch (Exception e) {
            log.error("[数据治理] 保存数据源失败", e);
            return Result.error("保存数据源失败：" + e.getMessage());
        }
    }

    @Operation(summary = "数据源列表", description = "按用户查询已保存的数据源（不含密码）")
    @GetMapping("/sources")
    public Result<List<JSONObject>> listSources(@RequestParam String userId) {
        try {
            List<JSONObject> list = new ArrayList<>();
            dmJdbcTemplate.query(
                "SELECT ID, USER_ID, NAME, DB_TYPE, HOST, PORT, DB_NAME, USERNAME, DOMAIN_NAME, TABLE_COUNT, " +
                    "DOMAIN_STATS, LAST_SCAN_TIME, CREATE_TIME, UPDATE_TIME FROM GOV_DATA_SOURCE WHERE USER_ID=? " +
                    " ORDER BY CREATE_TIME",
                new RowCallbackHandler() {
                    @Override
                    public void processRow(ResultSet rs) throws java.sql.SQLException {
                    JSONObject o = new JSONObject();
                    o.put("id", rs.getString("ID"));
                    o.put("userId", rs.getString("USER_ID"));
                    o.put("name", rs.getString("NAME"));
                    o.put("dbType", rs.getString("DB_TYPE"));
                    o.put("host", rs.getString("HOST"));
                    o.put("port", rs.getInt("PORT"));
                    o.put("dbName", rs.getString("DB_NAME"));
                    o.put("username", rs.getString("USERNAME"));
                    o.put("domain", rs.getString("DOMAIN_NAME"));
                    o.put("tableCount", rs.getInt("TABLE_COUNT"));
                    String stats = readClob(rs, "DOMAIN_STATS");
                    if (stats != null && !stats.isEmpty()) {
                        try { o.put("domainStats", JSON.parseObject(stats)); } catch (Exception ignore) { }
                    }
                    o.put("lastScanTime", rs.getString("LAST_SCAN_TIME"));
                    o.put("createTime", rs.getString("CREATE_TIME"));
                    list.add(o);
                }}, userId);
            return Result.success(list);
        } catch (Exception e) {
            log.error("[数据治理] 查询数据源列表失败", e);
            return Result.error("查询数据源列表失败：" + e.getMessage());
        }
    }

    @Operation(summary = "删除数据源", description = "删除连接配置并清理已采集的表元数据")
    @DeleteMapping("/sources/{id}")
    public Result<Void> deleteSource(@PathVariable String id) {
        try {
            dmJdbcTemplate.update("DELETE FROM GOV_TABLE_META WHERE SOURCE_ID=?", id);
            dmJdbcTemplate.update("DELETE FROM GOV_DATA_SOURCE WHERE ID=?", id);
            return Result.success();
        } catch (Exception e) {
            log.error("[数据治理] 删除数据源失败", e);
            return Result.error("删除数据源失败：" + e.getMessage());
        }
    }

    @Operation(summary = "清除扫描结果", description = "清空该数据源已采集的表元数据并重置统计（保留连接配置，可重新扫描）")
    @DeleteMapping("/tables/{id}")
    public Result<Void> clearTables(@PathVariable String id) {
        try {
            dmJdbcTemplate.update("DELETE FROM GOV_TABLE_META WHERE SOURCE_ID=?", id);
            dmJdbcTemplate.update(
                "UPDATE GOV_DATA_SOURCE SET TABLE_COUNT=0, DOMAIN_STATS=NULL, LAST_SCAN_TIME=NULL, UPDATE_TIME=? WHERE ID=?",
                now(), id);
            log.info("[数据治理] 数据源[{}]扫描结果已清除", id);
            return Result.success();
        } catch (Exception e) {
            log.error("[数据治理] 清除扫描结果失败：{}", id, e);
            return Result.error("清除扫描结果失败：" + e.getMessage());
        }
    }

    @Operation(summary = "测试连接", description = "使用表单参数尝试建立 JDBC 连接并执行 SELECT 1")
    @PostMapping("/test")
    public Result<JSONObject> testConnection(@RequestBody JSONObject body) {
        DruidDataSource dds = null;
        try {
            dds = buildDataSource(body);
            try (Connection conn = dds.getConnection()) {
                boolean ok = conn.isValid(5);
                JSONObject data = new JSONObject();
                data.put("success", ok);
                data.put("message", ok ? "连接成功" : "连接不可用");
                return Result.success(data);
            }
        } catch (Exception e) {
            JSONObject data = new JSONObject();
            data.put("success", false);
            data.put("message", "连接失败：" + e.getMessage());
            return Result.success(data);
        } finally {
            if (dds != null) dds.close();
        }
    }

    /* ==================== 元数据采集（异步任务 + 进度轮询） ==================== */

    @Operation(summary = "启动采集", description = "对指定数据源启动异步元数据采集任务，返回 taskId 供轮询")
    @PostMapping("/scan")
    public Result<JSONObject> startScan(@RequestBody JSONObject body) {
        String sourceId = body.getString("id");
        if (sourceId == null || sourceId.isEmpty()) {
            return Result.error("缺少数据源 id");
        }
        JSONObject ds = loadSource(sourceId);
        if (ds == null) {
            return Result.error("数据源不存在：" + sourceId);
        }
        String taskId = "scan_" + UUID.randomUUID().toString().replace("-", "").substring(0, 16);
        JSONObject task = new JSONObject();
        task.put("status", "PENDING");
        task.put("total", 0);
        task.put("scanned", 0);
        task.put("percent", 0);
        task.put("message", "排队中…");
        SCAN_TASKS.put(taskId, task);
        SCAN_EXECUTOR.submit(() -> runScan(taskId, ds));
        JSONObject data = new JSONObject();
        data.put("taskId", taskId);
        return Result.success("采集任务已启动", data);
    }

    @Operation(summary = "采集进度", description = "按 taskId 轮询采集进度（status/total/scanned/percent/message）")
    @GetMapping("/scan/status/{taskId}")
    public Result<JSONObject> scanStatus(@PathVariable String taskId) {
        JSONObject task = SCAN_TASKS.get(taskId);
        if (task == null) {
            return Result.error("任务不存在：" + taskId);
        }
        return Result.success(new JSONObject(task));
    }

    /** 采集主流程：分页读表 → 批量读字段 → 分批入库 → 更新统计 */
    private void runScan(String taskId, JSONObject ds) {
        String sourceId = ds.getString("id");
        DruidDataSource dds = null;
        try {
            updateTask(taskId, "RUNNING", 0, 0, "正在建立连接…");
            dds = buildDataSource(ds);

            String dbType = ds.getString("dbType") == null ? "mysql" : ds.getString("dbType");
            String schema = schemaOf(dbType, ds);
            long total = countTables(dds, dbType, schema);
            updateTask(taskId, "RUNNING", total, 0, "共 " + total + " 张表，开始采集…");

            // 全量重采：先清旧数据
            dmJdbcTemplate.update("DELETE FROM GOV_TABLE_META WHERE SOURCE_ID=?", sourceId);

            final int PAGE = 400;
            long scanned = 0;
            int page = 0;
            String now = now();
            while (true) {
                List<Map<String, Object>> tables = queryTablesPage(dds, dbType, schema, PAGE, page * PAGE);
                if (tables.isEmpty()) break;

                // 本页表名集合 → 批量查询字段
                List<String> names = new ArrayList<>(tables.size());
                for (Map<String, Object> t : tables) names.add(String.valueOf(t.get("TABLE_NAME")));
                Map<String, List<JSONObject>> colMap = queryColumnsBatch(dds, dbType, schema, names);

                // 分批写库（每批 100 表）
                int batchSize = 100;
                for (int i = 0; i < tables.size(); i += batchSize) {
                    insertMetaBatch(sourceId, tables.subList(i, Math.min(i + batchSize, tables.size())), colMap, now);
                }
                scanned += tables.size();
                page++;
                updateTask(taskId, "RUNNING", total, scanned, "已采集 " + scanned + " / " + total + " 张表");
            }

            // 关键词规则归类后，对「未分类」的表再调 AI 大模型批量归类（依据表名/注释/字段）
            try {
                aiClassifyDomains(sourceId, taskId);
            } catch (Exception e) {
                log.error("[数据治理] AI 主题域归类失败（保留规则分类结果）", e);
            }

            // 更新数据源统计：表数 + 域分布（AI 归类后再汇总）
            JSONObject domainStats = computeDomainStats(sourceId);
            dmJdbcTemplate.update(
                "UPDATE GOV_DATA_SOURCE SET TABLE_COUNT=?, DOMAIN_STATS=?, LAST_SCAN_TIME=?, UPDATE_TIME=? WHERE ID=?",
                (int) scanned, domainStats.toJSONString(), now, now, sourceId);
            updateTask(taskId, "DONE", total, scanned, "采集完成，共 " + scanned + " 张表（含 AI 主题域归类）");
            log.info("[数据治理] 数据源[{}]元数据采集完成：{} 张表", ds.getString("name"), scanned);
        } catch (Exception e) {
            log.error("[数据治理] 元数据采集失败：sourceId={}", sourceId, e);
            updateTaskFailed(taskId, e.getMessage());
        } finally {
            if (dds != null) dds.close();
        }
    }

    private void insertMetaBatch(String sourceId, List<Map<String, Object>> tables,
                                 Map<String, List<JSONObject>> colMap, String now) throws Exception {
        String sql = "INSERT INTO GOV_TABLE_META (SOURCE_ID, TABLE_NAME, TABLE_COMMENT, DOMAIN_NAME, TABLE_ROWS, " +
            "COLUMN_COUNT, COLUMNS_JSON, UPDATE_TIME) VALUES (?,?,?,?,?,?,?,?)";
        try (Connection conn = dmJdbcTemplate.getDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            conn.setAutoCommit(false);
            for (Map<String, Object> t : tables) {
                String tableName = String.valueOf(t.get("TABLE_NAME"));
                String comment = t.get("TABLE_COMMENT") == null ? "" : String.valueOf(t.get("TABLE_COMMENT"));
                long rows = t.get("TABLE_ROWS") instanceof Number ? ((Number) t.get("TABLE_ROWS")).longValue() : 0L;
                List<JSONObject> cols = colMap.getOrDefault(tableName, Collections.emptyList());
                JSONArray colArr = new JSONArray();
                colArr.addAll(cols);
                ps.setString(1, sourceId);
                ps.setString(2, tableName);
                ps.setString(3, comment.length() > 900 ? comment.substring(0, 900) : comment);
                ps.setString(4, inferDomain(t.get("DB_NAME"), tableName, comment));
                ps.setLong(5, rows);
                ps.setInt(6, cols.size());
                ps.setString(7, colArr.toJSONString());
                ps.setString(8, now);
                ps.addBatch();
            }
            ps.executeBatch();
            conn.commit();
        }
    }

    private long countTables(DruidDataSource dds, String dbType, String schema) throws Exception {
        String sql = isOracleDict(dbType)
            ? "SELECT COUNT(*) FROM ALL_TABLES WHERE OWNER=?"
            : "SELECT COUNT(*) FROM information_schema.TABLES WHERE TABLE_SCHEMA=? AND TABLE_TYPE='BASE TABLE'";
        try (Connection conn = dds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, schema);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getLong(1) : 0;
            }
        }
    }

    private List<Map<String, Object>> queryTablesPage(DruidDataSource dds, String dbType, String schema,
                                                      int limit, int offset) throws Exception {
        boolean oracle = isOracle(dbType);
        String sql;
        if (oracle) {
            // Oracle 无 LIMIT/OFFSET 语法，用 ROWNUM 分页（兼容全部版本）
            sql = "SELECT TABLE_NAME, TABLE_COMMENT, TABLE_ROWS FROM (" +
                  "SELECT ROWNUM RN, A.* FROM (" +
                  "SELECT t.TABLE_NAME, c.COMMENTS AS TABLE_COMMENT, 0 AS TABLE_ROWS FROM ALL_TABLES t " +
                  "LEFT JOIN ALL_TAB_COMMENTS c ON c.OWNER=t.OWNER AND c.TABLE_NAME=t.TABLE_NAME " +
                  "WHERE t.OWNER=? ORDER BY t.TABLE_NAME" +
                  ") A WHERE ROWNUM<=?" +
                  ") WHERE RN>?";
        } else if (isDm(dbType)) {
            sql = "SELECT t.TABLE_NAME, c.COMMENTS AS TABLE_COMMENT, 0 AS TABLE_ROWS FROM ALL_TABLES t " +
                  "LEFT JOIN ALL_TAB_COMMENTS c ON c.OWNER=t.OWNER AND c.TABLE_NAME=t.TABLE_NAME " +
                  "WHERE t.OWNER=? ORDER BY t.TABLE_NAME LIMIT " + limit + " OFFSET " + offset;
        } else {
            sql = "SELECT TABLE_NAME, TABLE_COMMENT, TABLE_ROWS FROM information_schema.TABLES " +
                  "WHERE TABLE_SCHEMA=? AND TABLE_TYPE='BASE TABLE' ORDER BY TABLE_NAME LIMIT " + limit + " OFFSET " + offset;
        }
        List<Map<String, Object>> list = new ArrayList<>(limit);
        try (Connection conn = dds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            if (oracle) {
                ps.setString(1, schema);
                ps.setInt(2, offset + limit);
                ps.setInt(3, offset);
            } else {
                ps.setString(1, schema);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> m = new HashMap<>();
                    m.put("TABLE_NAME", rs.getString("TABLE_NAME"));
                    m.put("TABLE_COMMENT", rs.getString("TABLE_COMMENT"));
                    m.put("TABLE_ROWS", rs.getLong("TABLE_ROWS"));
                    list.add(m);
                }
            }
        }
        return list;
    }

    private Map<String, List<JSONObject>> queryColumnsBatch(DruidDataSource dds, String dbType, String schema,
                                                            List<String> tableNames) throws Exception {
        if (tableNames.isEmpty()) return Collections.emptyMap();
        StringBuilder in = new StringBuilder();
        for (int i = 0; i < tableNames.size(); i++) in.append(i == 0 ? "?" : ",?");
        String sql = isOracleDict(dbType)
            // DISTINCT：ALL_COL_COMMENTS 关联可能在部分达梦版本出现一列多行，去重避免字段重复
            ? "SELECT DISTINCT c.TABLE_NAME, c.COLUMN_NAME, c.COLUMN_ID, c.DATA_TYPE AS COLUMN_TYPE, " +
              "c.NULLABLE AS IS_NULLABLE, cc.COMMENTS AS COLUMN_COMMENT FROM ALL_TAB_COLUMNS c " +
              "LEFT JOIN ALL_COL_COMMENTS cc ON cc.OWNER=c.OWNER AND cc.TABLE_NAME=c.TABLE_NAME " +
              "AND cc.COLUMN_NAME=c.COLUMN_NAME WHERE c.OWNER=? AND c.TABLE_NAME IN (" + in + ") " +
              "ORDER BY c.TABLE_NAME, c.COLUMN_ID"
            : "SELECT TABLE_NAME, COLUMN_NAME, COLUMN_TYPE, IS_NULLABLE, COLUMN_COMMENT " +
              "FROM information_schema.COLUMNS WHERE TABLE_SCHEMA=? AND TABLE_NAME IN (" + in + ") " +
              "ORDER BY TABLE_NAME, ORDINAL_POSITION";
        Map<String, List<JSONObject>> map = new HashMap<>(tableNames.size() * 2);
        try (Connection conn = dds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, schema);
            for (int i = 0; i < tableNames.size(); i++) ps.setString(2 + i, tableNames.get(i));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    JSONObject col = new JSONObject(true);
                    col.put("name", rs.getString("COLUMN_NAME"));
                    col.put("type", rs.getString("COLUMN_TYPE"));
                    col.put("nullable", rs.getString("IS_NULLABLE"));
                    col.put("comment", rs.getString("COLUMN_COMMENT"));
                    String tn = rs.getString("TABLE_NAME");
                    map.computeIfAbsent(tn, k -> new ArrayList<>()).add(col);
                }
            }
        }
        return map;
    }

    /* ==================== 元数据分页查询 ==================== */

    @Operation(summary = "表清单分页", description = "按数据源分页查询表元数据，支持表名/注释关键字与主题域过滤")
    @GetMapping("/tables")
    public Result<JSONObject> listTables(@RequestParam String id,
                                         @RequestParam(defaultValue = "1") int pageNum,
                                         @RequestParam(defaultValue = "50") int pageSize,
                                         @RequestParam(required = false) String keyword,
                                         @RequestParam(required = false) String domain) {
        try {
            int size = Math.min(Math.max(pageSize, 1), 500);
            int offset = (Math.max(pageNum, 1) - 1) * size;
            StringBuilder where = new StringBuilder(" WHERE SOURCE_ID=?");
            List<Object> args = new ArrayList<>();
            args.add(id);
            if (keyword != null && !keyword.trim().isEmpty()) {
                where.append(" AND (TABLE_NAME LIKE ? OR TABLE_COMMENT LIKE ?)");
                String kw = "%" + keyword.trim() + "%";
                args.add(kw); args.add(kw);
            }
            if (domain != null && !domain.trim().isEmpty()) {
                where.append(" AND DOMAIN_NAME=?");
                args.add(domain.trim());
            }
            Long total = dmJdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM GOV_TABLE_META" + where, Long.class, args.toArray());
            List<Object> args2 = new ArrayList<>(args);
            args2.add(size); args2.add(offset);
            List<JSONObject> list = new ArrayList<>();
            dmJdbcTemplate.query(
                "SELECT TABLE_NAME, TABLE_COMMENT, DOMAIN_NAME, SUB_DOMAIN, BIZ_OBJECT, TABLE_ROWS, COLUMN_COUNT FROM GOV_TABLE_META" + where +
                    " ORDER BY TABLE_NAME LIMIT ? OFFSET ?",
                new RowCallbackHandler() {
                    @Override
                    public void processRow(ResultSet rs) throws java.sql.SQLException {
                    JSONObject o = new JSONObject(true);
                    o.put("tableName", rs.getString("TABLE_NAME"));
                    o.put("tableComment", rs.getString("TABLE_COMMENT"));
                    o.put("domain", rs.getString("DOMAIN_NAME"));
                    o.put("subDomain", rs.getString("SUB_DOMAIN"));
                    o.put("bizObject", rs.getString("BIZ_OBJECT"));
                    o.put("tableRows", rs.getLong("TABLE_ROWS"));
                    o.put("columnCount", rs.getInt("COLUMN_COUNT"));
                    list.add(o);
                }}, args2.toArray());
            JSONObject data = new JSONObject();
            data.put("total", total == null ? 0 : total);
            data.put("list", list);
            return Result.success(data);
        } catch (Exception e) {
            log.error("[数据治理] 查询表清单失败", e);
            return Result.error("查询表清单失败：" + e.getMessage());
        }
    }

    @Operation(summary = "人工修正表资产归属", description = "手动修正 AI 分层结果：业务域/子主题域/业务对象/表注释，修正后自动重算主题域统计")
    @PostMapping("/tables/update")
    public Result<JSONObject> updateTable(@RequestBody JSONObject body) {
        String id = body.getString("id");
        String tableName = body.getString("tableName");
        if (id == null || id.isEmpty() || tableName == null || tableName.isEmpty()) {
            return Result.error("缺少 id 或 tableName");
        }
        try {
            String domain = trimDomain(body.getString("domain"));
            String subDomain = trimDomain(body.getString("subDomain"));
            String bizObject = trimDomain(body.getString("bizObject"));
            String comment = body.getString("tableComment");
            if (domain == null && subDomain == null && bizObject == null && comment == null) {
                return Result.error("没有需要修改的字段");
            }
            StringBuilder sql = new StringBuilder("UPDATE GOV_TABLE_META SET UPDATE_TIME=?");
            List<Object> args = new ArrayList<>();
            args.add(now());
            if (domain != null) { sql.append(", DOMAIN_NAME=?"); args.add(domain); }
            if (subDomain != null) { sql.append(", SUB_DOMAIN=?"); args.add(subDomain); }
            if (bizObject != null) { sql.append(", BIZ_OBJECT=?"); args.add(bizObject); }
            if (comment != null) { sql.append(", TABLE_COMMENT=?"); args.add(comment); }
            sql.append(" WHERE SOURCE_ID=? AND TABLE_NAME=?");
            args.add(id);
            args.add(tableName);
            dmJdbcTemplate.update(sql.toString(), args.toArray());
            // 主题域变化后重算该数据源的域分布统计
            JSONObject domainStats = computeDomainStats(id);
            dmJdbcTemplate.update(
                "UPDATE GOV_DATA_SOURCE SET DOMAIN_STATS=?, UPDATE_TIME=? WHERE ID=?",
                domainStats.toJSONString(), now(), id);
            JSONObject data = new JSONObject();
            data.put("domain", domain);
            data.put("subDomain", subDomain);
            data.put("bizObject", bizObject);
            data.put("domainStats", domainStats);
            return Result.success("已保存", data);
        } catch (Exception e) {
            log.error("[数据治理] 人工修正表资产失败", e);
            return Result.error("人工修正失败：" + e.getMessage());
        }
    }

    @Operation(summary = "依据表质量检查", description = "连接源库对权威依据表执行真实质量检查：总行数/空值/前后空格/编码唯一（仅统计，不修改任何数据）")
    @PostMapping("/quality/check")
    public Result<JSONObject> qualityCheck(@RequestBody JSONObject body) {
        String id = body.getString("id");
        String table = body.getString("table");
        // 表名仅允许字母/数字/下划线（防注入），且必须存在于已采集元数据中
        if (id == null || id.isEmpty() || table == null || !table.matches("[A-Za-z0-9_]+")) {
            return Result.error("缺少数据源 id 或表名非法");
        }
        JSONObject src = loadSource(id);
        if (src == null) return Result.error("数据源不存在：" + id);
        String colJson = queryString(
            "SELECT COLUMNS_JSON FROM GOV_TABLE_META WHERE SOURCE_ID=? AND TABLE_NAME=?", id, table);
        if (colJson == null || colJson.isEmpty()) {
            return Result.error("该表不在已采集元数据中，请先扫描或从数据资产树带入");
        }
        DruidDataSource dds = null;
        try {
            JSONArray cols = JSON.parseArray(colJson);
            List<String> allCols = new ArrayList<>();
            Map<String, String> colType = new HashMap<>();
            for (int i = 0; i < cols.size(); i++) {
                JSONObject c = cols.getJSONObject(i);
                String n = c == null ? null : c.getString("name");
                String t = c == null || c.getString("type") == null ? "" : c.getString("type").toUpperCase();
                if (n != null && n.matches("[A-Za-z0-9_]+")) {
                    allCols.add(n);
                    colType.put(n, t);
                }
            }
            if (allCols.isEmpty()) return Result.error("该表无可用列信息");
            // 空值检查最多覆盖 20 列；前后空格仅针对字符型列，最多 15 列
            List<String> nullCols = allCols.subList(0, Math.min(allCols.size(), 20));
            List<String> trimCols = new ArrayList<>();
            for (String n : allCols) {
                String t = colType.get(n) == null ? "" : colType.get(n);
                if ((t.contains("CHAR") || t.contains("TEXT")) && trimCols.size() < 15) trimCols.add(n);
            }
            // 候选编码列：名称以 CODE/_CODE/NO/_NO 结尾或为 ID/_ID
            String keyCol = null;
            for (String n : allCols) {
                String u = n.toUpperCase();
                if (u.endsWith("CODE") || u.endsWith("_CODE") || u.endsWith("NO")
                    || u.endsWith("_NO") || u.equals("ID") || u.endsWith("_ID")) {
                    keyCol = n;
                    break;
                }
            }
            dds = buildDataSource(src);
            StringBuilder sql = new StringBuilder("SELECT COUNT(*) AS TOTAL");
            for (int k = 0; k < nullCols.size(); k++) {
                sql.append(", SUM(CASE WHEN ").append(nullCols.get(k))
                   .append(" IS NULL THEN 1 ELSE 0 END) AS N").append(k);
            }
            for (int k = 0; k < trimCols.size(); k++) {
                sql.append(", SUM(CASE WHEN ").append(trimCols.get(k)).append(" IS NOT NULL AND ")
                   .append(trimCols.get(k)).append(" <> TRIM(").append(trimCols.get(k))
                   .append(") THEN 1 ELSE 0 END) AS F").append(k);
            }
            if (keyCol != null) sql.append(", COUNT(DISTINCT ").append(keyCol).append(") AS DK");
            sql.append(" FROM ").append(table);
            long total = 0;
            long emptyTotal = 0;
            long fmtTotal = 0;
            long dup = 0;
            boolean uniqueOk = true;
            JSONArray colStats = new JSONArray();
            try (Connection conn = dds.getConnection();
                 Statement stmt = conn.createStatement()) {
                stmt.setQueryTimeout(120);
                try (ResultSet rs = stmt.executeQuery(sql.toString())) {
                    if (rs.next()) {
                        total = rs.getLong("TOTAL");
                        for (int k = 0; k < nullCols.size(); k++) {
                            long empty = rs.getLong("N" + k);
                            emptyTotal += empty;
                            String col = nullCols.get(k);
                            long fmt = 0;
                            int ti = trimCols.indexOf(col);
                            if (ti >= 0) {
                                fmt = rs.getLong("F" + ti);
                                fmtTotal += fmt;
                            }
                            JSONObject cs = new JSONObject(true);
                            cs.put("name", col);
                            cs.put("empty", empty);
                            cs.put("fmt", fmt);
                            colStats.add(cs);
                        }
                        if (keyCol != null) {
                            long distinct = rs.getLong("DK");
                            dup = Math.max(0, total - distinct);
                            uniqueOk = dup == 0;
                        }
                    }
                }
            }
            // 空值最多的前 10 列
            List<JSONObject> sorted = new ArrayList<>();
            for (int i = 0; i < colStats.size(); i++) sorted.add(colStats.getJSONObject(i));
            sorted.sort((a, b) -> Long.compare(b.getLongValue("empty"), a.getLongValue("empty")));
            JSONArray top = new JSONArray();
            for (int i = 0; i < sorted.size() && i < 10; i++) top.add(sorted.get(i));
            JSONObject data = new JSONObject(true);
            data.put("table", table);
            data.put("total", total);
            data.put("emptyTotal", emptyTotal);
            data.put("fmtTotal", fmtTotal);
            data.put("keyColumn", keyCol == null ? "" : keyCol);
            data.put("dup", dup);
            data.put("uniqueOk", uniqueOk);
            data.put("columns", top);
            return Result.success("检查完成", data);
        } catch (Exception e) {
            log.error("[数据治理] 依据表质量检查失败：{}：{}", id, table, e);
            return Result.error("质量检查失败：" + e.getMessage());
        } finally {
            if (dds != null) dds.close();
        }
    }

    @Operation(summary = "黄金记录预览", description = "真实读取权威数据源中依据表的前 N 行（仅查询，不修改数据）")
    @PostMapping("/quality/golden-preview")
    public Result<JSONObject> goldenPreview(@RequestBody JSONObject body) {
        String id = body.getString("id");
        String table = body.getString("table");
        if (id == null || id.isEmpty() || table == null || !table.matches("[A-Za-z0-9_]+")) {
            return Result.error("缺少数据源 id 或表名非法");
        }
        int limit = body.getInteger("limit") == null ? 10 : Math.min(Math.max(body.getInteger("limit"), 1), 50);
        JSONObject src = loadSource(id);
        if (src == null) return Result.error("数据源不存在：" + id);
        String colJson = queryString(
            "SELECT COLUMNS_JSON FROM GOV_TABLE_META WHERE SOURCE_ID=? AND TABLE_NAME=?", id, table);
        if (colJson == null || colJson.isEmpty()) {
            return Result.error("该表不在已采集元数据中，请先扫描或从数据资产树带入");
        }
        DruidDataSource dds = null;
        try {
            JSONArray cols = JSON.parseArray(colJson);
            // 展示前 12 个合法列
            List<String> showCols = new ArrayList<>();
            for (int i = 0; i < cols.size() && showCols.size() < 12; i++) {
                JSONObject c = cols.getJSONObject(i);
                String n = c == null ? null : c.getString("name");
                if (n != null && n.matches("[A-Za-z0-9_]+")) showCols.add(n);
            }
            if (showCols.isEmpty()) return Result.error("该表无可用列信息");
            dds = buildDataSource(src);
            String dbType = src.getString("dbType");
            long total;
            List<String[]> rows = new ArrayList<>();
            try (Connection conn = dds.getConnection();
                 Statement stmt = conn.createStatement()) {
                stmt.setQueryTimeout(60);
                try (ResultSet rs = stmt.executeQuery(
                        "SELECT COUNT(*) FROM " + table)) {
                    total = rs.next() ? rs.getLong(1) : 0;
                }
                try (ResultSet rs = stmt.executeQuery(
                        rowsLimitSql(dbType, String.join(", ", showCols) + " FROM " + table, limit))) {
                    while (rs.next()) {
                        String[] vals = new String[showCols.size()];
                        for (int i = 0; i < showCols.size(); i++) {
                            String v = rs.getString(i + 1);
                            if (v == null) v = "";
                            v = v.replace("\r", " ").replace("\n", " ").trim();
                            if (v.length() > 60) v = v.substring(0, 60) + "…";
                            vals[i] = v;
                        }
                        rows.add(vals);
                    }
                }
            }
            JSONObject data = new JSONObject(true);
            data.put("table", table);
            data.put("total", total);
            data.put("columns", showCols);
            JSONArray rowsJson = new JSONArray();
            for (String[] r : rows) {
                JSONArray jr = new JSONArray();
                for (String v : r) jr.add(v);
                rowsJson.add(jr);
            }
            data.put("rows", rowsJson);
            data.put("limit", limit);
            return Result.success("预览完成", data);
        } catch (Exception e) {
            log.error("[数据治理] 黄金记录预览失败：{}：{}", id, table, e);
            return Result.error("黄金记录预览失败：" + e.getMessage());
        } finally {
            if (dds != null) dds.close();
        }
    }

    /* ==================== AI 维度扫描（维度/字典表发现 + 真实值采样） ==================== */

    private static final Map<String, JSONObject> DIM_TASKS = new ConcurrentHashMap<>();

    /** 启动 AI 维度扫描：候选维度/字典表 → AI 确认（维度名/编码列/名称列）→ 回源库采样真实维度值 */
    @Operation(summary = "启动 AI 维度扫描", description = "异步发现数据源中的维度/字典表并采样真实维度值，返回 taskId 供轮询")
    @PostMapping("/dimensions/scan")
    public Result<JSONObject> startDimScan(@RequestBody JSONObject body) {
        String id = body.getString("id");
        if (id == null || id.isEmpty()) return Result.error("缺少数据源 id");
        JSONObject src = loadSource(id);
        if (src == null) return Result.error("数据源不存在：" + id);
        String taskId = "dim_" + UUID.randomUUID().toString().replace("-", "").substring(0, 16);
        JSONObject task = new JSONObject(true);
        task.put("status", "PENDING");
        task.put("total", 0);
        task.put("scanned", 0);
        task.put("dims", new JSONArray());
        task.put("message", "排队中…");
        DIM_TASKS.put(taskId, task);
        SCAN_EXECUTOR.submit(() -> runDimScan(taskId, id, src));
        JSONObject data = new JSONObject(true);
        data.put("taskId", taskId);
        return Result.success("AI 维度扫描已启动", data);
    }

    @Operation(summary = "AI 维度扫描进度", description = "按 taskId 轮询：status/total/scanned/dims（扫描完成后 dims 为全部发现的维度）")
    @GetMapping("/dimensions/scan/status/{taskId}")
    public Result<JSONObject> dimScanStatus(@PathVariable String taskId) {
        JSONObject task = DIM_TASKS.get(taskId);
        if (task == null) return Result.error("任务不存在：" + taskId);
        return Result.success(new JSONObject(task));
    }

    /** 维度扫描主流程：候选过滤 → AI 批量确认 → 回源库采样 */
    private void runDimScan(String taskId, String sourceId, JSONObject src) {
        DruidDataSource dds = null;
        try {
            // 1. 全量表元数据
            List<JSONObject> tables = new ArrayList<>();
            dmJdbcTemplate.query(
                "SELECT TABLE_NAME, TABLE_COMMENT, COLUMNS_JSON FROM GOV_TABLE_META WHERE SOURCE_ID=? ORDER BY TABLE_NAME",
                new RowCallbackHandler() {
                    @Override
                    public void processRow(ResultSet rs) throws java.sql.SQLException {
                        JSONObject o = new JSONObject(true);
                        o.put("tableName", rs.getString("TABLE_NAME"));
                        o.put("comment", readClob(rs, "TABLE_COMMENT"));
                        JSONArray cs = new JSONArray();
                        try {
                            JSONArray arr = JSON.parseArray(readClob(rs, "COLUMNS_JSON"));
                            if (arr != null) {
                                for (int i = 0; i < arr.size(); i++) {
                                    JSONObject c = arr.getJSONObject(i);
                                    if (c != null && c.getString("name") != null) cs.add(c.getString("name"));
                                }
                            }
                        } catch (Exception ignore) { }
                        o.put("columns", cs);
                        tables.add(o);
                    }
                }, sourceId);
            // 2. 候选过滤：表名/注释命中维度、字典类关键词（AI 只需判断候选，控制调用量）
            List<JSONObject> candidates = new ArrayList<>();
            for (JSONObject t : tables) {
                if (isDimCandidate(t.getString("tableName"), t.getString("comment"))) candidates.add(t);
            }
            JSONObject task = DIM_TASKS.get(taskId);
            task.put("total", candidates.size());
            task.put("status", "RUNNING");
            task.put("message", "候选维度表 " + candidates.size() + " 张，AI 确认中…");
            if (candidates.isEmpty()) {
                task.put("status", "DONE");
                task.put("message", "未发现候选维度/字典表");
                return;
            }
            dds = buildDataSource(src);
            // 3. AI 批量确认 + 回源采样
            final int BATCH = 25;
            JSONArray dims = new JSONArray();
            int scanned = 0;
            for (int i = 0; i < candidates.size(); i += BATCH) {
                List<JSONObject> batch = candidates.subList(i, Math.min(i + BATCH, candidates.size()));
                try {
                    JSONArray found = classifyDimBatch(batch);
                    for (int k = 0; k < found.size(); k++) {
                        JSONObject d = found.getJSONObject(k);
                        String table = d.getString("tableName");
                        String codeCol = d.getString("codeColumn");
                        String nameCol = d.getString("nameColumn");
                        if (table == null || codeCol == null || nameCol == null) continue;
                        JSONObject meta = findTable(tables, table);
                        if (meta == null) continue;
                        // 列名必须真实存在于该表元数据（大小写不敏感对齐到实际列名）
                        String pc = matchColumn(meta.getJSONArray("columns"), d.getString("parentColumn"));
                        String cc = matchColumn(meta.getJSONArray("columns"), codeCol);
                        String nc = matchColumn(meta.getJSONArray("columns"), nameCol);
                        if (cc == null || nc == null) continue;
                        JSONObject dim = new JSONObject(true);
                        dim.put("name", d.getString("dimName") == null ? table : d.getString("dimName"));
                        dim.put("table", table);
                        dim.put("codeColumn", cc);
                        dim.put("nameColumn", nc);
                        dim.put("parentColumn", pc == null ? "" : pc);
                        dim.put("values", sampleDimValues(dds, src.getString("dbType"), table, cc, nc, pc, 20));
                        dims.add(dim);
                    }
                } catch (Exception e) {
                    log.warn("[数据治理] AI 维度确认批次失败（跳过 {} 张）: {}", batch.size(), e.getMessage());
                }
                scanned = Math.min(i + BATCH, candidates.size());
                task.put("scanned", scanned);
                task.put("dims", dims);
                task.put("message", "AI 确认中 " + scanned + " / " + candidates.size());
            }
            task.put("status", "DONE");
            task.put("message", "扫描完成，发现 " + dims.size() + " 个维度");
            log.info("[数据治理] AI 维度扫描完成：候选 {} 张，确认维度 {} 个", candidates.size(), dims.size());
        } catch (Exception e) {
            log.error("[数据治理] AI 维度扫描失败", e);
            JSONObject task = DIM_TASKS.get(taskId);
            if (task != null) {
                task.put("status", "FAILED");
                task.put("message", e.getMessage());
            }
        } finally {
            if (dds != null) dds.close();
        }
    }

    /** 维度/字典表候选：表名或注释命中维度、代码、分类类关键词 */
    private boolean isDimCandidate(String name, String comment) {
        String n = name == null ? "" : name.toUpperCase();
        String c = comment == null ? "" : comment;
        if (n.matches(".*(DIM|DICT|CODE|TYPE|CATEGORY|KIND|LEVEL|REGION|AREA|CURRENCY|UNIT|INDUSTRY|RANK|GRADE).*"))
            return true;
        return c.contains("维度") || c.contains("字典") || c.contains("码表") || c.contains("代码")
            || c.contains("分类") || c.contains("类型") || c.contains("区划") || c.contains("币种")
            || c.contains("行业") || c.contains("职级") || c.contains("学历") || c.contains("民族");
    }

    private JSONObject findTable(List<JSONObject> tables, String name) {
        for (JSONObject t : tables) {
            if (name.equalsIgnoreCase(t.getString("tableName"))) return t;
        }
        return null;
    }

    /** 大小写不敏感对齐列名到元数据中的真实列名 */
    private String matchColumn(JSONArray columns, String name) {
        if (name == null || name.trim().isEmpty()) return null;
        String want = name.trim().toLowerCase();
        for (int i = 0; i < columns.size(); i++) {
            String c = columns.getString(i);
            if (c != null && c.toLowerCase().equals(want)) return c;
        }
        return null;
    }

    /** AI 批量确认维度/字典表 */
    private JSONArray classifyDimBatch(List<JSONObject> batch) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("以下为数据源中的候选维度/字典表（表名 | 表注释 | 字段示例）。\n")
          .append("请判断哪些表是维度表或字典/码表（提供分析口径的代码类数据，通常行数少、含编码列+名称列）。\n")
          .append("对确认的表输出：dimName=维度业务名称（2~8 个汉字，如：行政区划、币种、性别、学历）、")
          .append("codeColumn=编码列、nameColumn=名称列、parentColumn=上级/父级列（无则空字符串）。\n\n");
        for (JSONObject t : batch) {
            JSONArray colArr = t.getJSONArray("columns");
            StringBuilder colSample = new StringBuilder();
            for (int i = 0; colArr != null && i < colArr.size() && i < 10; i++) {
                if (colSample.length() > 0) colSample.append(", ");
                colSample.append(colArr.getString(i));
            }
            sb.append(t.getString("tableName")).append(" | ")
              .append(t.getString("comment") == null ? "" : t.getString("comment"))
              .append(" | ").append(colSample).append("\n");
        }
        sb.append("\n只输出 JSON 数组，仅包含确认是维度/字典表的记录，不要输出其他文字：")
          .append("[{\"tableName\":\"表名\",\"dimName\":\"维度名\",\"codeColumn\":\"编码列\",\"nameColumn\":\"名称列\",\"parentColumn\":\"上级列或空\"}]");
        AiChatRequest req = new AiChatRequest();
        req.setSessionId("gov_dims_" + UUID.randomUUID().toString().replace("-", "").substring(0, 12));
        req.setMessage(sb.toString());
        req.setSystemPrompt("你是资深数据治理与数据仓库架构专家，擅长从物理表中识别维度表/字典码表。严格按用户要求的 JSON 格式输出，不要输出任何解释性文字。");
        req.setEnableThinking(false);
        req.setEnableWebSearch(false);
        req.setEnableTools(false);
        String content = aiChatService.chat(req);
        int start = content == null ? -1 : content.indexOf('[');
        int end = content == null ? -1 : content.lastIndexOf(']');
        if (start < 0 || end <= start) return new JSONArray();
        return JSON.parseArray(content.substring(start, end + 1));
    }

    /** 回源库采样真实维度值（SELECT 编码列, 名称列[, 上级列] 取前 n 行，方言自适应） */
    private JSONArray sampleDimValues(DruidDataSource dds, String dbType, String table, String codeCol,
                                      String nameCol, String parentCol, int limit) {
        JSONArray out = new JSONArray();
        String sql = rowsLimitSql(dbType, codeCol + ", " + nameCol
            + (parentCol == null ? "" : ", " + parentCol)
            + " FROM " + table, limit);
        try (Connection conn = dds.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.setQueryTimeout(30);
            try (ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    JSONObject v = new JSONObject(true);
                    v.put("code", rs.getString(1) == null ? "" : rs.getString(1));
                    v.put("name", rs.getString(2) == null ? "" : rs.getString(2));
                    v.put("parent", parentCol == null ? "" : String.valueOf(rs.getObject(3) == null ? "" : rs.getObject(3)));
                    v.put("level", "");
                    v.put("note", "");
                    out.add(v);
                }
            }
        } catch (Exception e) {
            log.warn("[数据治理] 维度值采样失败：{}：{}", table, e.getMessage());
        }
        return out;
    }

    /** 手动采样维度值（供前端对单个维度重新采样） */
    @Operation(summary = "采样维度值", description = "回源库读取指定维度表的编码/名称/上级列真实数据（最多 20 行）")
    @PostMapping("/dimensions/sample")
    public Result<JSONObject> sampleDims(@RequestBody JSONObject body) {
        String id = body.getString("id");
        String table = body.getString("table");
        String codeCol = body.getString("codeColumn");
        String nameCol = body.getString("nameColumn");
        String parentCol = body.getString("parentColumn");
        if (id == null || id.isEmpty() || table == null || !table.matches("[A-Za-z0-9_]+")
            || codeCol == null || !codeCol.matches("[A-Za-z0-9_]+")
            || nameCol == null || !nameCol.matches("[A-Za-z0-9_]+")
            || (parentCol != null && !parentCol.matches("[A-Za-z0-9_]+"))) {
            return Result.error("参数缺失或表/列名非法");
        }
        String colJson = queryString(
            "SELECT COLUMNS_JSON FROM GOV_TABLE_META WHERE SOURCE_ID=? AND TABLE_NAME=?", id, table);
        if (colJson == null || colJson.isEmpty()) {
            return Result.error("该表不在已采集元数据中");
        }
        DruidDataSource dds = null;
        try {
            JSONObject src = loadSource(id);
            if (src == null) return Result.error("数据源不存在：" + id);
            dds = buildDataSource(src);
            JSONArray values = sampleDimValues(dds, src.getString("dbType"), table, codeCol, nameCol,
                parentCol == null || parentCol.isEmpty() ? null : parentCol, 20);
            JSONObject data = new JSONObject(true);
            data.put("values", values);
            return Result.success("采样完成", data);
        } catch (Exception e) {
            log.error("[数据治理] 维度值采样失败", e);
            return Result.error("维度值采样失败：" + e.getMessage());
        } finally {
            if (dds != null) dds.close();
        }
    }

    @Operation(summary = "单表字段", description = "查询单表的字段明细（名称/类型/可空/注释），按字段名去重")
    @GetMapping("/columns")
    public Result<JSONObject> listColumns(@RequestParam String id, @RequestParam String tableName) {
        try {
            String json = queryString(
                "SELECT COLUMNS_JSON FROM GOV_TABLE_META WHERE SOURCE_ID=? AND TABLE_NAME=?", id, tableName);
            JSONArray cols = new JSONArray();
            if (json != null && !json.isEmpty()) {
                try { cols = JSON.parseArray(json); } catch (Exception ignore) { }
            }
            // 按字段名去重（大小写不敏感）：兼容早期扫描入库的重复列数据
            java.util.Set<String> seen = new java.util.HashSet<>();
            JSONArray unique = new JSONArray();
            for (int i = 0; i < cols.size(); i++) {
                JSONObject c = cols.getJSONObject(i);
                if (c == null) continue;
                String n = c.getString("name") == null ? "" : c.getString("name").toLowerCase();
                if (n.isEmpty() || !seen.add(n)) continue;
                unique.add(c);
            }
            JSONObject data = new JSONObject();
            data.put("columns", unique);
            return Result.success(data);
        } catch (Exception e) {
            log.error("[数据治理] 查询字段明细失败", e);
            return Result.error("查询字段明细失败：" + e.getMessage());
        }
    }

    @Operation(summary = "资产汇总", description = "按用户汇总各数据源的表数与主题域分布（供 AI 上下文摘要）")
    @GetMapping("/summary")
    public Result<JSONObject> summary(@RequestParam String userId) {
        try {
            List<JSONObject> sources = new ArrayList<>();
            dmJdbcTemplate.query(
                "SELECT ID, NAME, DB_TYPE, TABLE_COUNT, DOMAIN_STATS, LAST_SCAN_TIME FROM GOV_DATA_SOURCE " +
                    "WHERE USER_ID=? ORDER BY CREATE_TIME",
                new RowCallbackHandler() {
                    @Override
                    public void processRow(ResultSet rs) throws java.sql.SQLException {
                    JSONObject o = new JSONObject(true);
                    o.put("id", rs.getString("ID"));
                    o.put("name", rs.getString("NAME"));
                    o.put("dbType", rs.getString("DB_TYPE"));
                    o.put("tableCount", rs.getInt("TABLE_COUNT"));
                    o.put("lastScanTime", rs.getString("LAST_SCAN_TIME"));
                    String stats = readClob(rs, "DOMAIN_STATS");
                    if (stats != null && !stats.isEmpty()) {
                        try { o.put("domainStats", JSON.parseObject(stats)); } catch (Exception ignore) { }
                    }
                    sources.add(o);
                }}, userId);
            JSONObject data = new JSONObject();
            data.put("sources", sources);
            return Result.success(data);
        } catch (Exception e) {
            log.error("[数据治理] 查询资产汇总失败", e);
            return Result.error("查询资产汇总失败：" + e.getMessage());
        }
    }

    /* ==================== 治理上下文固化（替代前端 localStorage） ==================== */

    @Operation(summary = "加载治理上下文", description = "按用户加载固化的治理上下文（步骤进度/工件/数据源等）")
    @GetMapping("/context")
    public Result<JSONObject> loadContext(@RequestParam String userId) {
        try {
            String json = queryString("SELECT CTX_JSON FROM GOV_CONTEXT WHERE USER_ID=?", userId);
            JSONObject data = new JSONObject();
            if (json == null || json.isEmpty()) {
                data.put("found", false);
            } else {
                data.put("found", true);
                try {
                    data.put("ctx", JSON.parseObject(json));
                } catch (Exception parseErr) {
                    data.put("found", false);
                }
            }
            return Result.success(data);
        } catch (Exception e) {
            log.error("[数据治理] 加载治理上下文失败", e);
            return Result.error("加载治理上下文失败：" + e.getMessage());
        }
    }

    @Operation(summary = "保存治理上下文", description = "整体 upsert 治理上下文（大 JSON 走 CLOB，不影响页面加载）")
    @PostMapping("/context")
    public Result<Void> saveContext(@RequestBody JSONObject body) {
        try {
            String userId = body.getString("userId");
            if (userId == null || userId.isEmpty()) {
                return Result.error("缺少 userId");
            }
            String json = body.getJSONObject("ctx") == null ? "{}" : body.getJSONObject("ctx").toJSONString();
            String now = now();
            Integer exists = queryInt("SELECT COUNT(*) FROM GOV_CONTEXT WHERE USER_ID=?", userId);
            if (exists != null && exists > 0) {
                dmJdbcTemplate.update("UPDATE GOV_CONTEXT SET CTX_JSON=?, UPDATE_TIME=? WHERE USER_ID=?",
                    json, now, userId);
            } else {
                dmJdbcTemplate.update("INSERT INTO GOV_CONTEXT (USER_ID, CTX_JSON, UPDATE_TIME) VALUES (?,?,?)",
                    userId, json, now);
            }
            return Result.success();
        } catch (Exception e) {
            log.error("[数据治理] 保存治理上下文失败", e);
            return Result.error("保存治理上下文失败：" + e.getMessage());
        }
    }

    /* ==================== 内部工具 ==================== */

    private DruidDataSource buildDataSource(JSONObject conf) throws Exception {
        String dbType = conf.getString("dbType") == null ? "mysql" : conf.getString("dbType");
        String host = conf.getString("host");
        int defaultPort = isOracle(dbType) ? 1521 : (isDm(dbType) ? 5236 : 3306);
        int port = conf.getInteger("port") == null ? defaultPort : conf.getInteger("port");
        DruidDataSource dds = new DruidDataSource();
        if (isDm(dbType)) {
            dds.setDriverClassName("dm.jdbc.driver.DmDriver");
            dds.setUrl("jdbc:dm://" + host + ":" + port);
        } else if (isOracle(dbType)) {
            dds.setDriverClassName("oracle.jdbc.driver.OracleDriver");
            // 默认按服务名连接（与 Navicat「服务名」方式一致，格式 @//host:port/service_name）；
            // dbName 以 ":" 开头（如 ":ORCL"）表示按 SID 连接，兼容仅注册 SID 的库
            String dbName = conf.getString("dbName");
            if (dbName != null && dbName.startsWith(":")) {
                dds.setUrl("jdbc:oracle:thin:@" + host + ":" + port + ":" + dbName.substring(1));
            } else {
                dds.setUrl("jdbc:oracle:thin:@//" + host + ":" + port + "/" + dbName);
            }
        } else {
            dds.setDriverClassName("com.mysql.cj.jdbc.Driver");
            dds.setUrl("jdbc:mysql://" + host + ":" + port + "/" + conf.getString("dbName")
                + "?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai"
                + "&allowPublicKeyRetrieval=true&connectTimeout=10000&socketTimeout=60000");
        }
        dds.setUsername(conf.getString("username"));
        String pwd = conf.getString("password");
        if (conf.containsKey("passwordB64")) {
            pwd = new String(Base64.getDecoder().decode(conf.getString("passwordB64")), StandardCharsets.UTF_8);
        }
        dds.setPassword(pwd == null ? "" : pwd);
        dds.setInitialSize(1);
        dds.setMaxActive(2);
        dds.setMinIdle(0);
        // Oracle 的校验语句必须带 FROM DUAL，否则连接校验直接报错
        dds.setValidationQuery(isOracle(dbType) ? "SELECT 1 FROM DUAL" : "SELECT 1");
        dds.setValidationQueryTimeout(5);
        dds.setConnectionErrorRetryAttempts(1);
        dds.setBreakAfterAcquireFailure(true);
        return dds;
    }

    private static boolean isDm(String dbType) {
        return "dm".equalsIgnoreCase(dbType) || "达梦".equals(dbType);
    }

    private static boolean isOracle(String dbType) {
        return "oracle".equalsIgnoreCase(dbType);
    }

    /** Oracle 与达梦同为 Oracle 系数据字典（ALL_TABLES / ALL_TAB_COLUMNS / ALL_COL_COMMENTS 通用） */
    private static boolean isOracleDict(String dbType) {
        return isDm(dbType) || isOracle(dbType);
    }

    /** 按源库方言拼接「取前 N 行」SQL：Oracle 无 LIMIT，用 ROWNUM 子查询（兼容全部版本）；MySQL/达梦用 LIMIT */
    private static String rowsLimitSql(String dbType, String selectBody, int limit) {
        if (isOracle(dbType)) {
            return "SELECT * FROM (SELECT " + selectBody + ") WHERE ROWNUM<=" + limit;
        }
        return "SELECT " + selectBody + " LIMIT " + limit;
    }

    /** MySQL 的 schema 即库名；达梦/Oracle 使用大写 OWNER（用户名） */
    private static String schemaOf(String dbType, JSONObject ds) {
        if (isOracleDict(dbType)) {
            String u = ds.getString("username");
            return u == null ? "" : u.toUpperCase();
        }
        return ds.getString("dbName");
    }

    /** 主题域推断：库名/表名/注释关键词打分（与前端治理向导域体系一致） */
    private static String inferDomain(Object dbNameObj, String tableName, String comment) {
        String text = (dbNameObj == null ? "" : String.valueOf(dbNameObj)) + " " + tableName + " " + comment;
        String[][] rules = {
            {"财务金融", "财务,资金,会计,凭证,报销,预算,发票,税务,应收,应付,成本,付款,收款,账务,核算,资产,总账,明细账"},
            {"采购供应链", "采购,招标,投标,合同,订单,库存,仓储,物流,运输,供应链,到货,验收,询价,竞价,供应商"},
            {"主数据与组织", "客户,供应商档案,合作伙伴,员工,人员,组织,部门,岗位,用户,账户,公司,法人,主数据"},
            {"销售市场", "销售,营销,渠道,商机,客户拜访,线索,回款,业绩"},
            {"生产制造", "生产,工单,设备,工艺,物料,bom,质检,检验,产线,排产"},
            {"风险合规", "风险,合规,审计,预警,监控,告警,漏洞,隐患,整改"},
            {"系统支撑", "日志,权限,角色,菜单,配置,字典,序列,token,session,qrtz,scheduler,job"},
        };
        String lower = text.toLowerCase();
        String best = "未分类";
        int bestScore = 0;
        for (String[] rule : rules) {
            int score = 0;
            for (String kw : rule[1].split(",")) {
                if (!kw.isEmpty() && lower.contains(kw.toLowerCase())) score++;
            }
            if (score > bestScore) {
                bestScore = score;
                best = rule[0];
            }
        }
        return best;
    }

        /* ==================== AI 主题域归类（扫描完成后自动执行） ==================== */

    /** 扫描后 AI 资产分层：对全部表一次性产出 L1 业务域 / L2 业务子主题域 / L3 业务对象 */
    private void aiClassifyDomains(String sourceId, String taskId) {
        List<JSONObject> pending = new ArrayList<>();
        dmJdbcTemplate.query(
            "SELECT TABLE_NAME, TABLE_COMMENT, COLUMNS_JSON FROM GOV_TABLE_META WHERE SOURCE_ID=? ORDER BY TABLE_NAME",
            new RowCallbackHandler() {
                @Override
                public void processRow(ResultSet rs) throws java.sql.SQLException {
                    JSONObject o = new JSONObject(true);
                    o.put("tableName", rs.getString("TABLE_NAME"));
                    o.put("comment", readClob(rs, "TABLE_COMMENT"));
                    // 字段名(注释)示例，最多 10 个：无注释的表靠字段辅助判断
                    StringBuilder cols = new StringBuilder();
                    try {
                        JSONArray arr = JSON.parseArray(readClob(rs, "COLUMNS_JSON"));
                        if (arr != null) {
                            for (int i = 0; i < arr.size() && i < 10; i++) {
                                JSONObject c = arr.getJSONObject(i);
                                if (c == null || c.getString("name") == null) continue;
                                if (cols.length() > 0) cols.append(", ");
                                cols.append(c.getString("name"));
                                String cc = c.getString("comment");
                                if (cc != null && !cc.isEmpty()) cols.append("(").append(cc).append(")");
                            }
                        }
                    } catch (Exception ignore) { }
                    o.put("fields", cols.toString());
                    pending.add(o);
                }
            }, sourceId);
        if (pending.isEmpty()) return;

        log.info("[数据治理] AI 资产分层开始：{} 张表（L1 业务域 / L2 子主题域 / L3 业务对象）", pending.size());
        updateTask(taskId, "RUNNING", pending.size(), 0, "AI 资产分层中，共 " + pending.size() + " 张表…");
        final int BATCH = 40;
        int done = 0;
        int classified = 0;
        for (int i = 0; i < pending.size(); i += BATCH) {
            List<JSONObject> batch = pending.subList(i, Math.min(i + BATCH, pending.size()));
            try {
                classified += classifyBatch(sourceId, batch);
            } catch (Exception e) {
                log.warn("[数据治理] AI 分层批次失败（跳过 {} 张）: {}", batch.size(), e.getMessage());
            }
            done = Math.min(i + BATCH, pending.size());
            updateTask(taskId, "RUNNING", pending.size(), done, "AI 资产分层中 " + done + " / " + pending.size());
        }
        log.info("[数据治理] AI 资产分层完成：{}/{} 张成功分层", classified, pending.size());
    }

    /** 单批次分层：调 AI（与 /v1/ai/governance/stream 同源 AiChatService），一次产出 L1 业务域 / L2 子主题域 / L3 业务对象 */
    private int classifyBatch(String sourceId, List<JSONObject> batch) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("请从数据资产视角，为下列每张物理表梳理业务分层归属。\n")
          .append("三个层级定义：\n")
          .append("1) domain 业务域：企业级业务领域（优先从以下通用域选择：").append(String.join("、", GOV_DOMAINS))
          .append("；明显不属于时允许自拟 2~6 个汉字的新业务域，如：人力资源、文档档案）\n")
          .append("2) subDomain 业务子主题域：业务域下的细分主题（2~8 个汉字，如：财务域下的「总账核算」「资金管理」；同批相近的表必须复用同一名称）\n")
          .append("3) bizObject 业务对象：表所描述的业务事物概念（2~8 个汉字，如：凭证、客户、银行账户；一张业务对象可对应多张主表/明细表）\n")
          .append("判断依据优先级：表注释 > 表名含义 > 字段名与字段注释；确实无法判断时 domain 选「未分类」，subDomain/bizObject 给出最接近的概括即可。\n\n")
          .append("清单格式：表名 | 表注释 | 字段示例\n\n");
        for (JSONObject o : batch) {
            sb.append(o.getString("tableName")).append(" | ")
              .append(o.getString("comment") == null ? "" : o.getString("comment"))
              .append(" | ").append(o.getString("fields")).append("\n");
        }
        sb.append("\n只输出 JSON 数组，不要输出任何其他文字：")
          .append("[{\"tableName\":\"表名\",\"domain\":\"业务域\",\"subDomain\":\"业务子主题域\",\"bizObject\":\"业务对象\"}]");
        AiChatRequest req = new AiChatRequest();
        req.setSessionId("gov_classify_" + UUID.randomUUID().toString().replace("-", "").substring(0, 12));
        req.setMessage(sb.toString());
        req.setSystemPrompt("你是资深数据治理与数据架构专家，负责把物理表反向梳理为「业务域-业务子主题域-业务对象」的资产分层模型。严格按用户要求的 JSON 格式输出，不要输出任何解释性文字。");
        req.setEnableThinking(false);
        req.setEnableWebSearch(false);
        req.setEnableTools(false);
        String content = aiChatService.chat(req);
        // 提取 JSON 数组（容忍模型输出前后的说明文字与代码块标记）
        int start = content == null ? -1 : content.indexOf('[');
        int end = content == null ? -1 : content.lastIndexOf(']');
        if (start < 0 || end <= start) return 0;
        JSONArray arr = JSON.parseArray(content.substring(start, end + 1));
        int count = 0;
        for (int i = 0; i < arr.size(); i++) {
            JSONObject item = arr.getJSONObject(i);
            if (item == null) continue;
            String tn = item.getString("tableName");
            if (tn == null || tn.trim().isEmpty()) continue;
            String domain = trimDomain(item.getString("domain"));
            String subDomain = trimDomain(item.getString("subDomain"));
            String bizObject = trimDomain(item.getString("bizObject"));
            if (domain == null) continue;
            dmJdbcTemplate.update(
                "UPDATE GOV_TABLE_META SET DOMAIN_NAME=?, SUB_DOMAIN=?, BIZ_OBJECT=? WHERE SOURCE_ID=? AND TABLE_NAME=?",
                domain, subDomain, bizObject, sourceId, tn.trim());
            count++;
        }
        return count;
    }

    /** 域/子域/业务对象名称合法性：2~12 位中文/字母/数字（允许 AI 自拟，防脏数据） */
    private String trimDomain(String raw) {
        if (raw == null) return null;
        String v = raw.trim();
        if (v.length() < 2 || v.length() > 12 || !v.matches("[\\u4e00-\\u9fa5A-Za-z0-9]+")) return null;
        return v;
    }

    private JSONObject computeDomainStats(String sourceId) {
        JSONObject stats = new JSONObject(true);
        List<Map<String, Object>> rows = dmJdbcTemplate.queryForList(
            "SELECT DOMAIN_NAME, COUNT(*) AS CNT FROM GOV_TABLE_META WHERE SOURCE_ID=? GROUP BY DOMAIN_NAME ORDER BY CNT DESC",
            sourceId);
        for (Map<String, Object> r : rows) {
            Object d = r.get("DOMAIN_NAME");
            Object c = r.get("CNT");
            stats.put(d == null ? "未分类" : String.valueOf(d), c instanceof Number ? ((Number) c).intValue() : 0);
        }
        return stats;
    }

    private JSONObject loadSource(String id) {
        List<JSONObject> list = new ArrayList<>();
        dmJdbcTemplate.query(
            "SELECT ID, NAME, DB_TYPE, HOST, PORT, DB_NAME, USERNAME, PASSWORD_B64 FROM GOV_DATA_SOURCE WHERE ID=?",
                new RowCallbackHandler() {
                    @Override
                    public void processRow(ResultSet rs) throws java.sql.SQLException {
                JSONObject o = new JSONObject();
                o.put("id", rs.getString("ID"));
                o.put("name", rs.getString("NAME"));
                o.put("dbType", rs.getString("DB_TYPE"));
                o.put("host", rs.getString("HOST"));
                o.put("port", rs.getInt("PORT"));
                o.put("dbName", rs.getString("DB_NAME"));
                o.put("username", rs.getString("USERNAME"));
                o.put("passwordB64", rs.getString("PASSWORD_B64"));
                list.add(o);
            }}, id);
        return list.isEmpty() ? null : list.get(0);
    }

    private void updateTask(String taskId, String status, long total, long scanned, String message) {
        JSONObject task = SCAN_TASKS.get(taskId);
        if (task == null) return;
        task.put("status", status);
        task.put("total", total);
        task.put("scanned", scanned);
        task.put("percent", total <= 0 ? (scanned > 0 ? 50 : 0) : (int) (scanned * 100 / total));
        task.put("message", message);
    }

    private void updateTaskFailed(String taskId, String message) {
        JSONObject task = SCAN_TASKS.get(taskId);
        if (task == null) return;
        task.put("status", "FAILED");
        task.put("message", message == null ? "采集失败" : message);
    }

    private Integer queryInt(String sql, Object... args) {
        try {
            return dmJdbcTemplate.queryForObject(sql, Integer.class, args);
        } catch (Exception e) {
            return null;
        }
    }

    private String queryString(String sql, Object... args) {
        try {
            return dmJdbcTemplate.query(sql, new ResultSetExtractor<String>() {
                @Override
                public String extractData(ResultSet rs) throws java.sql.SQLException {
                    return rs.next() ? readClob(rs, 1) : null;
                }
            }, args);
        } catch (Exception e) {
            return null;
        }
    }

    /** 读取 CLOB/字符串列：优先按字符流读，避免驱动返回 CLOB 对象 */
    private static String readClob(ResultSet rs, String column) throws java.sql.SQLException {
        try {
            Reader r = rs.getCharacterStream(column);
            if (r == null) return rs.getString(column);
            StringBuilder sb = new StringBuilder();
            char[] buf = new char[4096];
            int n;
            while ((n = r.read(buf)) > 0) sb.append(buf, 0, n);
            r.close();
            String s = sb.toString();
            return s.isEmpty() ? rs.getString(column) : s;
        } catch (Exception e) {
            return rs.getString(column);
        }
    }

    private static String readClob(ResultSet rs, int index) throws java.sql.SQLException {
        try {
            Reader r = rs.getCharacterStream(index);
            if (r == null) return rs.getString(index);
            StringBuilder sb = new StringBuilder();
            char[] buf = new char[4096];
            int n;
            while ((n = r.read(buf)) > 0) sb.append(buf, 0, n);
            r.close();
            String s = sb.toString();
            return s.isEmpty() ? rs.getString(index) : s;
        } catch (Exception e) {
            return rs.getString(index);
        }
    }

    private static String now() {
        return new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }
}
