package com.huabo.compliance.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JedisUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.RedisFinalUtis;
import com.hbfk.util.ResponseFormat;

import com.huabo.compliance.entity.*;
import com.huabo.compliance.mapper.*;
import com.huabo.compliance.mysql.mapper.TblOrganizationMySqlMapper;
import com.huabo.compliance.service.TblAssessService;
import com.huabo.compliance.util.DateUtils;

import redis.clients.jedis.Jedis;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * @Tag(name="评价管理-评价立项",description="评价管理-评价立项")
 */
@Service
@Transactional
public class TblAssessServiceImpl extends ServiceImpl<TblAssessMapper, TblAssess> implements TblAssessService {

    @Resource
    private TblAssessMapper tblAssessMapper;
    @Resource
    OrganizationServiceMapper organizationServiceMapper;
    @Resource
    YhrPageMapper yhrPageMapper;

    @Resource
    TblAssEleCategoryMapper tblAssEleCategoryMapper;

    @Resource
    TblAssessTargetMapper tblAssessTargetMapper;

    @Resource
    TblAssessMarkMapper tblAssessMarkMapper;
    @Resource
    TblAssessMarkVoMapper markVoMapper;
    @Resource
    TblAssessStaffMapper assessStaffMapper;
    @Resource
    TblAssesstempleMapper tblAssesstempleMapper;

    @Resource
    private TblOrganizationMySqlMapper tblOrganizationMySqlMapper;
    
    @Resource
    private TblOrganizationMapper tblOrganizationMapper;
    
    public JsonBean findByPage(Integer pageNumber, Integer pageSize, String assNumnber, String assName, String startDate, String startDates, String endDate, String endDates) {
        return null;
    }

    /**
     *  评价列表
     */
    @Override
    public IPage<TblAssessVo> initiatePjgl (boolean isAudit,BigDecimal orgid,BigDecimal staffid,String realName,Integer pageNumber, String assNumnber, String assName, String startDate, String startDates, String endDate, String endDates,Integer pageSize){

        IPage<TblAssessVo> iPage = new Page<>(pageNumber,pageSize); //ConstClass.DEFAULT_SIZE


        return   this.tblAssessMapper.findPageBeanPJLX(iPage,isAudit,orgid,staffid,realName,assNumnber,assName,startDate,startDates,endDate,endDates);
    }

    @Override
    public IPage<TblAssessVo> initiatePjjg(Integer pageNumber, String assNumnber, String assName, String startDate, String startDates, String endDate, String endDates, TblStaffUtil staff,Integer pageSize) {
        IPage<TblAssessVo> iPage = new Page<>(pageNumber, pageSize);//ConstClass.DEFAULT_SIZE
        return this.tblAssessMapper.findPageBeanPJJG(iPage,assNumnber,assName,startDate,startDates,endDate,endDates,staff);
    }


    /**
     * 传入参数说明
     * tabName ----  插入编号所在的表 列入 tbl_flow
     * column --- 编号的列名 例如 tbl_flow 表中的 FLOWNUMBER
     * orgCol --- 编号所在的组织的列名  列入 tbl_flow 表中 COMPANY
     * orgid -----  组织ID
     * noId  ------ TBL_AUTONO_INFO的主键标识 列入 流程编号就是3
     * chChoiceCol  -------    插入编号所在的表添加额外条件的列名，例如风险分类里的 MODULETYPE
     * choiceVal   ---------   插入编号所在的表添加额外条件的值，例如风险分类里的 MODULETYPE 的值FXSJK
     */
    @Override
    public String findFlowNextId(String tblName, String column, String orgCol, BigDecimal orgid, Integer NoId, String chChoiceCol, String choiceVal, String bjf) throws Exception {

        Integer isUse = organizationServiceMapper.selectUniqueColumn(orgid);
        if (isUse == 0) {
            return "-1";//该组织没有使用自定义编码
        } else {
            String codeRuleSql = "SELECT CASE WHEN TON.ISUSEDEFAULT = 0 THEN TAI.NODEFAULTCODE	ELSE TON.NOCODE	END AS \"CODE\" ," +
                    "CASE WHEN TON.ISUSEDEFAULT = 0 THEN TAI.NODEFAULTSEPARTOR ELSE TON.NOSEPARTOR END AS  SEP,	CASE WHEN TON.ISUSEDEFAULT = 0 THEN TAI.NODEFAULTNUMBER ELSE TON.NONUMBER END AS \"NUMBER\"" +
                    " FROM TBL_AUTONO_INFO TAI LEFT JOIN TBL_ORG_NO TON ON TAI.NOID = TON.NOID WHERE TON.ORGID = " + orgid + " AND TON.NOID = " + NoId;
            Map<String, Object> orgNo = yhrPageMapper.queryBySql(codeRuleSql);


            if (orgNo == null) {
                return "-1";
            } else {
                String noSql = null;
                String result = null;
                String jgf = String.valueOf(orgNo.get("CODE"));
                String noCode = String.valueOf(orgNo.get("CODE"));
                String sep = String.valueOf(orgNo.get("SEP"));

                TblOrgNoId ton = new TblOrgNoId();
                ton.setNocode(noCode);
                ton.setNoNumber(Integer.valueOf(String.valueOf(orgNo.get("NUMBER"))));
                if (sep != null) {
                    ton.setNoSepartor(sep);
                }

                if (jgf.indexOf("_") != -1) {
                    jgf = jgf.replace("_", "/_");
                }

                //判断插入的是一级编号和同级编号是
                noSql = "SELECT MAX(" + column + ") FROM " + tblName + " WHERE ";

                noSql += " TO_CHAR(" + orgCol + ") IN ( SELECT TO_CHAR(ORGID) FROM TBL_ORGANIZATION WHERE ORGTYPE = 0 START WITH FATHERORGID = " + orgid + " CONNECT BY PRIOR FATHERORGID = ORGID UNION ALL SELECT TO_CHAR(ORGID) FROM TBL_ORGANIZATION WHERE ORGID = " + orgid + " UNION ALL SELECT TO_CHAR(ORGID) FROM TBL_ORGANIZATION WHERE ORGTYPE >=100 START WITH FATHERORGID = -1 CONNECT BY PRIOR ORGID = FATHERORGID ) ";

                //判断用户有没有使用分隔符
                if (sep != null) {

                    if (jgf.indexOf("_") != -1) {
                        if ("_".equals(sep)) {
                            jgf += "/" + sep;
                        } else {
                            jgf += sep;
                        }
                        jgf += "%' escape '/";
                    } else {
                        if ("_".equals(sep)) {
                            jgf += "/" + sep + "%' escape '/";
                        } else {
                            jgf += sep + "%";
                        }
                    }

                    noSql += " AND " + column + " LIKE '" + jgf + "' AND (LENGTH(REPLACE(" + column + ",'" + noCode + "','')) - LENGTH(REPLACE(REPLACE(" + column + ",'" + noCode + "',''),'" + sep + "',''))) = " + 1;
                } else {

                    if (jgf.indexOf("_") != -1) {
                        jgf += "%' escape '/";
                    } else {
                        jgf += "%";
                    }

                    noSql += " AND " + column + " LIKE '" + jgf + "' AND (LENGTH(REPLACE(" + column + ",'" + noCode + "','')) - LENGTH(REPLACE(REPLACE(" + column + ",'" + noCode + "',''),'-',''))) = 0 AND (LENGTH(REPLACE(" + column + ",'" + noCode + "','')) - LENGTH(REPLACE(REPLACE(" + column + ",'" + noCode + "',''),'_','')))  = 0"
                            + " AND (LENGTH(REPLACE(" + column + ",'" + noCode + "','')) - LENGTH(REPLACE(REPLACE(" + column + ",'" + noCode + "',''),',','')))  = 0 AND (LENGTH(REPLACE(" + column + ",'" + noCode + "','')) - LENGTH(REPLACE(REPLACE(" + column + ",'" + noCode + "',''),'.','')))  = 0"
                            + " AND (LENGTH(REPLACE(" + column + ",'" + noCode + "','')) - LENGTH(REPLACE(REPLACE(" + column + ",'" + noCode + "',''),'`','')))  = 0 AND (LENGTH(REPLACE(" + column + ",'" + noCode + "','')) - LENGTH(REPLACE(REPLACE(" + column + ",'" + noCode + "',''),'~','')))  = 0";
                }
                if (chChoiceCol != null) {
                    if ("包含".equals(bjf)) {
                        bjf = "LIKE";
                        choiceVal = "'%" + choiceVal + "%'";
                    } else if ("等于".equals(bjf)) {
                        bjf = "=";
                        choiceVal = "'" + choiceVal + "'";
                    }
                    noSql += " AND " + chChoiceCol + " " + bjf + " " + choiceVal;
                }
                //查询是否存在规则编号 ， 没有就新增 有就在此基础上加1
                System.out.println(noSql);
                result = yhrPageMapper.querySqlRetnString(noSql);
                return getNewCode(result, ton);
            }
        }
    }

    /**
     * @param orgid 组织id
     * @return
     */
    @Override
    public TblOrganization queryOrganizationById(BigDecimal orgid) {
        return this.organizationServiceMapper.selectById(orgid);
    }

    /**
     * @param page
     * @param orgid   组织id
     * @param orgtype 组织类型
     */
    @Override
    public void queryAllPageBeanPid(IPage<Map<String, Object>> page, BigDecimal orgid, Integer orgtype, String realname) {
        String sql = "select STAFFID,REALNAME,STA.ORGID,ORG.ORGNAME,STA.ADDRESS,STA.EMAIL,username from tbl_staff sta INNER JOIN TBL_ORGANIZATION org ON STA.ORGID=ORG.ORGID "
                + " where STA.ORGID in (select ORGID from TBL_ORGANIZATION org where FATHERORGID=" + orgid + " AND ORGTYPE=0 ) AND (STA.STATUS is NULL or STA.STATUS != 0)";
        if (orgtype != null && orgtype.toString().equals("0")) {
            sql = "select STAFFID,REALNAME,STA.ORGID,ORG.ORGNAME,STA.ADDRESS,STA.EMAIL,username from tbl_staff sta INNER JOIN TBL_ORGANIZATION org ON STA.ORGID=ORG.ORGID "
                    + " where STA.ORGID =" + orgid + " AND (STA.STATUS is NULL or STA.STATUS != 0)";
        }

        if (StringUtils.isNotBlank(realname)) {
            sql += "and REALNAME like '%" + realname + "%'";
        }

        sql = sql + " order by STAFFID desc";
        String sqlCount = "select count(*) from (" + sql + ")";

        long start = (page.getCurrent() - 1) * page.getSize();
        long end = start + page.getSize();

        page.setTotal(this.yhrPageMapper.queryCount(sqlCount));
        page.setRecords(this.yhrPageMapper.queryList(start, end, sql));

    }

    /**
     * @param page
     * @param tblComany    组织id
     * @param templeNumber 模板编号
     * @param templename   模板名
     * @param orgids       机构ids
     */
    @Override
    public void findAll(IPage<Map<String, Object>> page, String tblComany, String templeNumber, String templename, String orgids) {

        String hql = " select distinct t.* from TBL_ASSESSTEMPLE t inner join TBL_TEMPLE_ORGANIZATION o\n" +
                "on t.ASSTEMID=o.ASSTEMID where o.orgid in(" + orgids + ") and t.ORGID = " + tblComany;
        if (StringUtils.isNotBlank(templename)) {
            hql += " and t.templename like '%" + templename + "%'";
        }
        if (StringUtils.isNotBlank(templeNumber)) {
            hql += " and t.templeNumber like '%" + templeNumber + "%'";
        }
        hql += " order by t.asstemid desc";
        String sqlCount = "select count(*) from (" + hql + ")";
        long start = (page.getCurrent() - 1) * page.getSize();
        long end = start + page.getSize();

        page.setTotal(this.yhrPageMapper.queryCount(sqlCount));
        page.setRecords(this.yhrPageMapper.queryList(start, end, hql));

    }

    /**
     * @param assessid 查询条数
     * @param orgid    所在机构orgid
     * @return
     */
    @Override
    public Long selectTblassessNumber(String assessid, BigDecimal orgid) {
        String sqlcount = "SELECT COUNT(*) from Tbl_ASSESS t where t.assessid = '" + assessid + "'"
                + "AND TBLCOMANY IN (SELECT ORGID FROM TBL_ORGANIZATION WHERE  1=1 START WITH FATHERORGID= " + orgid + " AND ORGTYPE=0  CONNECT BY PRIOR ORGID = FATHERORGID UNION ALL SELECT " + orgid + "  FROM DUAL)";
        return yhrPageMapper.queryCount(sqlcount);
    }

    /**
     * 评价立项-新建页面-保存
     *
     * @param staff
     * @param tblAssess
     * @param orgid
     * @return
     */
    @Override
    public JsonBean saveAssEss(TblStaffUtil staff, TblAssess tblAssess, String orgid) {

        List<TblAssEleCategory> assEleCategories = this.tblAssEleCategoryMapper
                .getAssesscategoryByMuBanId(tblAssess.getAsstemid());
//
//        TblStaff tblStaff = (TblStaff) request.getSession().getAttribute("longUser");
        if (assEleCategories.size() > 0) {

            // 保存TblAssess
            //Serializable assessId = this.save(tblAssess);
            this.tblAssessMapper.insert(tblAssess);
            //TblAssess assess = this.tblAssessService.get(assessId);
            String[] oids = orgid.split(",");
            for (String oid : oids) {
                TblOrganization organization = this.organizationServiceMapper.loadTblOrganization(new BigDecimal(oid));
                // --------新表----------//
                // 新表
                TblAssessTarget assessTarget = new TblAssessTarget();

                assessTarget.setAssid(tblAssess.getAssid());
                if (organization != null) {
                    assessTarget.setOrgid(organization.getOrgid());
                }

                this.tblAssessTargetMapper.insert(assessTarget);
                // ---------------------//
                for (TblAssEleCategory tblAssEleCategory : assEleCategories) {
                    TblAssessMark assessMark = new TblAssessMark();
                    assessMark.setAssid(tblAssess.getAssid().intValue());
                    assessMark.setStaffid(staff.getStaffid());
                    if (organization != null) {
                        assessMark.setAssorgid(organization.getOrgid());
                    }
                    assessMark.setSuitable("1");// 适用
                    assessMark.setAsseleid(tblAssEleCategory.getAsseleid());
                    assessMark.setAssesstargetid(assessTarget.getAssesstargetid());

                    this.tblAssessMarkMapper.insert(assessMark);
                }
            }
            return new JsonBean(1, "成功", null);
        } else {
            return new JsonBean(0, "评价模板不完整,请选择其他模板", null);
        }

    }

    /**
     * 产生新的编号不保持上下级关系的编号
     *
     * @param result
     * @param orgNo
     * @return
     */
    private String getNewCode(String result, TblOrgNoId orgNo) {
        String code;
        if (result == null || "".equals(result)) {
            code = orgNo.getNocode();
            if (orgNo.getNoSepartor() != null) {
                code += orgNo.getNoSepartor();
            }
            if (orgNo.getNoNumber() == -1) {
                code += "01";
            } else if (orgNo.getNoNumber() == 1) {
                code += "00001";
            } else if (orgNo.getNoNumber() == 2) {
                code += "001";
            }
        } else {
            if (orgNo.getNoSepartor() == null) {
                String num = result.replace(orgNo.getNocode(), "");
                Integer no = Integer.parseInt(num);
                no++;
                int nolength = num.length() - no.toString().length();
                code = orgNo.getNocode();
                for (int i = 0; i < nolength; i++) {
                    code += "0";
                }
                code += no;
            } else {
                String num = result.replace(orgNo.getNocode() + orgNo.getNoSepartor(), "");
                Integer no = Integer.parseInt(num);
                no++;
                int nolength = num.length() - no.toString().length();
                code = orgNo.getNocode() + orgNo.getNoSepartor();
                for (int i = 0; i < nolength; i++) {
                    code += "0";
                }
                code += no;
            }
        }
        return code;
    }


    /**
     * 产生新的编号保持上下级关系的编号
     *
     * @param result
     * @param orgNo
     * @return
     */
    private String getNewCodeByParentNumber(String result, TblOrgNoId orgNo, String parentNumber) {
        String code;
        if (result == null || "".equals(result)) {
            code = parentNumber;
            if (orgNo.getNoSepartor() != null) {
                code += orgNo.getNoSepartor();
            }
            if (orgNo.getNoNumber() == -1) {
                code += "01";
            } else if (orgNo.getNoNumber() == 1) {
                code += "00001";
            } else if (orgNo.getNoNumber() == 2) {
                code += "001";
            }
        } else {
            String num = result.replace(parentNumber, "");
            if (orgNo.getNoSepartor() != null) {
                parentNumber += orgNo.getNoSepartor();
                num = num.substring(1, num.length());
            }
            Integer no = Integer.parseInt(num);
            no++;
            int nolength = num.length() - no.toString().length();
            for (int i = 0; i < nolength; i++) {
                parentNumber += "0";
            }
            code = parentNumber + no;
        }

        return code;
    }


    @Override
    public JsonBean findByPage(Integer pageNumber, Integer pageSize, String assNumnber, String assName, String startDate, String endDate) {
        if (pageNumber == null || pageNumber <= 0) {
            pageNumber = 1;
        }
        if (pageSize <= 0 || pageSize > 15) {
            pageSize = 15;
        }
        Page<TblAssess> page = new Page<>();
        page.setSize(pageSize);
        page.setCurrent(pageNumber);

        LambdaQueryWrapper<TblAssess> wrapper = Wrappers.<TblAssess>lambdaQuery();
        if (StringUtils.isNotBlank(assNumnber)) {
            wrapper.eq(TblAssess::getAssessid, assNumnber);
        }
        if (StringUtils.isNotBlank(assName)) {
            wrapper.eq(TblAssess::getAssessname, assName);
        }
        if (StringUtils.isNotBlank(startDate)) {
            wrapper.ge(TblAssess::getStartdate, startDate);
        }
        if (StringUtils.isNotBlank(endDate)) {
            wrapper.le(TblAssess::getEnddate, endDate);
        }


        Page<TblAssess> tblAssessPage = tblAssessMapper.selectPage(page, wrapper);
        List<TblAssess> assessList = tblAssessPage.getRecords();

        return ResponseFormat.retParam(1, 200, assessList);
    }


    /**
     * 查询模板要素 双百测算
     *
     * @param tmplId 模板id
     * @return
     */
    @Override
    public JsonBean queryPjmb(BigDecimal tmplId) {
        //  体系权重得分
        String sql = "SELECT\n" +
                "    E.asscatid,\n" +
                "    nvl2(E.FATHERASSCATID,CONNECT_BY_ROOT CATNAME||'-'||E.catname,E.catname) catname1, to_char(E.CATWEIGHT*nvl2(E.FATHERASSCATID,CONNECT_BY_ROOT CATWEIGHT,100)/100,'fm9999999990.00')||'%' catw\n" +
                "FROM Tbl_Assesscategory e\n" +
                "CONNECT BY PRIOR E.asscatid = E.fatherasscatid\n" +
                "START WITH E.fatherasscatid is null and asstemid=" + tmplId + "\n" +
                "ORDER SIBLINGS BY  E.asscatid";

        List<Map<String, Object>> list = this.yhrPageMapper.queryBySqlToList(sql);
        // 要素得分明细
        sql = "select lin.*,t.standardscore,ta.elementname,ta.auditpoint from\n" +
                " (\n" +
                "SELECT\n" +
                "    E.asscatid,\n" +
                "    nvl2(E.FATHERASSCATID,CONNECT_BY_ROOT CATNAME||'-'||E.catname,E.catname) catname1, to_char(E.CATWEIGHT*nvl2(E.FATHERASSCATID,CONNECT_BY_ROOT CATWEIGHT,100)/100,'fm9999999990.00')||'%' catw\n" +
                "FROM Tbl_Assesscategory e\n" +
                "CONNECT BY PRIOR E.asscatid = E.fatherasscatid\n" +
                "START WITH E.fatherasscatid is null and asstemid=" + tmplId + "\n" +
                "ORDER SIBLINGS BY  E.asscatid\n" +
                ") lin inner join TBL_ASSELE_CATEGORY t on lin.asscatid=t.asscatid inner join TBL_ASSESSELEMENT ta on ta.ASSELEID=t.ASSELEID\n" +
                "order by  lin.asscatid\n";
        List<Map<String, Object>> list2 = this.yhrPageMapper.queryBySqlToList(sql);

        Map map = new LinkedHashMap();
        map.put("tixiquanzhongs", list);
        map.put("yaosudefens", list2);
        return new JsonBean(200, "成功", map);

    }

    /**
     * 评价立项-授权-分页查询
     *
     * @param page
     * @param assid
     */
    @Override
    public void getOrgByassId(IPage<Map<String, Object>> page, BigDecimal assid) {
        String sql = "select * from tbl_organization o where o.orgid in(select max(assorgid)  from TBL_ASSESS_MARK t where t.ASSID =" + assid + " group by assorgid) and o.status = 0 ";
        String countSql = "select count(*) from tbl_organization o where o.orgid in(select max(assorgid) from TBL_ASSESS_MARK t where t.ASSID =" + assid + " group by assorgid) and o.status = 0 ";
        long start = (page.getCurrent() - 1) * page.getSize();
        long end = start + page.getSize();

        page.setTotal(this.yhrPageMapper.queryCount(countSql));
        page.setRecords(this.yhrPageMapper.queryList(start, end, sql));
    }

    /**
     * 项目立项-授权列表-授权
     *
     * @param page
     * @param assid
     * @param orgid
     */
    @Override
    public void findAssessMarkVoByPageBean(IPage<TblAssessMarkVo> page, BigDecimal assid, BigDecimal orgid) {
        markVoMapper.queryTblAssessMarkVoPage(page, assid, orgid);
    }

    /**
     * 参评人列表
     *
     * @param assmarkid
     * @return
     */
    @Override
    public void getTblAssessStaffByMarkId(IPage<TblAssessStaffVo> page, BigDecimal assmarkid) {
        markVoMapper.getStaffsBymarkidToList(page, assmarkid);
    }

    // 得到TblAssess 参数 assmarkid

    public TblAssess getTblAssessByassmarkid(BigDecimal assmarkid) {
        return markVoMapper.getTblAssessByassmarkid(assmarkid);
    }

    /**
     * 保存参评人
     */

    public TblAssessMark updateTblAssessStaff(BigDecimal assMarkId, String[] idArray, String[] varleArray) {

        for (int i = 0; i < idArray.length; i++) {
            TblAssessStaff assessStaff = assessStaffMapper.selectById(idArray[i]);

            assessStaff.setAssweight(new BigDecimal(varleArray[i]));
            this.assessStaffMapper.updateById(assessStaff);
        }
        TblAssessMark tblAssessMark = this.tblAssessMarkMapper.selectById(assMarkId);
        tblAssessMark.setState(TblAssessMark.START);
        tblAssessMarkMapper.updateById(tblAssessMark);
        return tblAssessMark;
    }

    @Override
    public JsonBean updateAssessMarkStaffid(String userid, String[] assMarkids) {
        Integer proId = null;
        BigDecimal orgId = null;
        Map<String, Object> map = new HashMap<String, Object>();
        QueryWrapper<TblAssessMark> qw = new QueryWrapper();
        for (String string : assMarkids) {
            TblAssessMark assessMark = this.tblAssessMarkMapper.selectById(new BigDecimal(string));
            orgId = assessMark.getAssorgid();
            assessMark.setStaffid(new BigDecimal(userid));
            this.tblAssessMarkMapper.updateById(assessMark);
            proId = assessMark.getAssid();
        }
        map.put("assId", proId);
        map.put("orgId", orgId);

        return new JsonBean(200, "成功", map);

    }

    @Override
    public JsonBean updateCanPingRen(String users, String assMarkids) {
        String[] str = assMarkids.split(",");
        Integer proId = null;
        BigDecimal orgId = null;
        BigDecimal markid = null;
        Map<String, Object> map = new HashMap<String, Object>();
        for (String string : str) {
            markid = new BigDecimal(string);

            TblAssessMark assessMark = this.tblAssessMarkMapper.selectById(markid);
            this.assessStaffMapper.deleteBySmarkid(markid);


            orgId = assessMark.getAssorgid();
            String[] user = users.split(",");
            for (String s : user) {

                TblAssessStaff assessStaff = new TblAssessStaff();
                assessStaff.setAssmarkid(markid);

                assessStaff.setStaffid(new BigDecimal(s));
                assessStaff.setOrgid(orgId);
                this.assessStaffMapper.insert(assessStaff);
            }
            assessMark.setState(TblAssessMark.UNSTART);
            this.tblAssessMarkMapper.updateById(assessMark);
            proId = assessMark.getAssid();
        }
        map.put("assId", proId);
        map.put("orgId", orgId);

        return new JsonBean(200, "成功", map);
    }

    /**
     * 项目立项 -- 新建
     *
     * @return
     */
    @Override
    public JsonBean add(TblAssess tblAssess) {
        tblAssess.setAssstatus("1");//状态 1 创建、2 启动

        tblAssessMapper.insert(tblAssess);

        return ResponseFormat.retParam(1, 200, "success");
    }

    /**
     * 评价项目 -- 修改
     *
     * @param tblAssess
     * @return
     */
    @Override
    public JsonBean update(TblAssess tblAssess) {
        if (null == tblAssess) {
            return ResponseFormat.retParam(0, 10002, "缺少参数，请确认后重试");
        }
        TblAssess dbTblAssess = tblAssessMapper.selectById(tblAssess.getAssid());
        if (null == dbTblAssess) {
            return ResponseFormat.retParam(0, 50002, "修改的数据不存在，请确认后重试");
        }
        tblAssessMapper.updateById(tblAssess);

        return ResponseFormat.retParam(1, 200, "success");
    }

    /**
     * 项目立项 -- 删除
     *
     * @param assId
     * @return
     */
    @Override
    public JsonBean delete(BigDecimal assId) {

        TblAssess tblAssess = tblAssessMapper.selectById(assId);
        if (null == tblAssess) {
            return ResponseFormat.retParam(0, 50001, "删除数据已不存在，请确认后重试");
        }
        if (!"1".equals(tblAssess.getAssstatus())) {
            return ResponseFormat.retParam(0, 1000, "项目状态不能删除");
        }

        tblAssessMapper.deleteById(assId);

        return ResponseFormat.retParam(1, 200, "success");
    }

    @Override
    public String deleteAssess(BigDecimal assid) {
        TblAssess assess = this.tblAssessMapper.selectById(assid);
        if (assess.getAssstatus().equals(TblAssess.CREATE)) {
            List<TblAssessMark> assessMarks = this.markVoMapper.getAssessMarkByAssId(assess.getAssid());
            this.tblAssessTargetMapper.deleteAssessTargetByAssId(assess.getAssid());

            for (TblAssessMark tblAssessMark : assessMarks) {
                this.assessStaffMapper.deleteBySmarkid(tblAssessMark.getAssmarkid());

            }
            this.markVoMapper.deleteAssessMarkByAssId(assess.getAssid());
            this.tblAssessMapper.deleteById(assid);
            return JsonBean.success();
        } else if (assess.getAssstatus().equals(TblAssess.START)) {
            return JsonBean.error("项目已启动不能删除");
        } else if (assess.getAssstatus().equals(TblAssess.END) || assess.getAssstatus().equals(TblAssess.JISUAN)) {
            return JsonBean.error("项目已结束不能删除");
        }
        return JsonBean.error();
    }

    @Override
    public TblAssessVo queryTblAssessVoByAssidAndOrgid(BigDecimal assId, BigDecimal orgid) {
        return this.tblAssessMapper.queryTblAssessVoByAssidAndOrgid(assId,orgid);
    }

    /**
     *
     * @param assorgid
     * @return
     *  主评人/参评人 左侧部门树
     */
    @Override
    public List<Map<String,Object>> getOrgTree(BigDecimal assorgid) {

         String sql="select decode(CONNECT_BY_ISLEAF,0,'true','false') AS \"isParent\",\n" +
                 "          t.ORGID as \"id\",\n" +
                 "          t.orgname as \"name\",\n" +
                 "          t.fatherorgid as \"pId\"\n" +
                 "from TBL_ORGANIZATION t\n" +
                 "CONNECT BY PRIOR t.ORGID = t.FATHERORGID and status = 0 and orgtype=0\n" +
                 "START WITH t.ORGID ="+assorgid;
         return yhrPageMapper.queryBySqlToList(sql);
    }

    /**
     * // 项目立项-修改-查询所在部门ids
     *
     * @param assId
     * @return
     */
    public List<TblOrganization> getOrgByassId(BigDecimal assId) {
        return this.tblAssessMapper.getOrgByassId(assId);
    }

    @Override
    public JsonBean updateAssEss(TblStaffUtil staff, String assid, String assessname, String start, String end, String staffid, BigDecimal templatekey, String orgid) {
        if (DateUtils.compare_date(end, start) == 1) {

            TblAssess assess = this.tblAssessMapper.selectById(assid);
            if (assess.getAssstatus().equals(TblAssess.CREATE)) {
                // 现有的组织架构

                List<TblOrganization> list = this.tblAssessMapper.getOrgByassId(assess.getAssid());

                List<TblAssEleCategory> assEleCategories = this.tblAssEleCategoryMapper
                        .getAssesscategoryByMuBanId(templatekey);
                // 未更换评价模板
                if (assess.getAsstemid().equals(templatekey)) {
                    assess.setStartdate(DateUtils.StringToLocalDateTime(start, "yyyy-MM-dd"));
                    assess.setEnddate(DateUtils.StringToLocalDateTime(end, "yyyy-MM-dd"));

                    // assess.setBaseday(DateUtils.parse(base));//基准日
                    assess.setAssstatus(TblAssess.CREATE);
                    assess.setAssessname(assessname);
                    assess.setAsssponsor(staff.getRealname());

                    //---------------------------------
                    if (staffid != null && staffid.trim().length() > 0) {
                        assess.setLeaderid(new BigDecimal(staffid)); //负责人id

                    }
                    this.tblAssessMapper.updateById(assess);


                    List<TblOrganization> newList = new ArrayList<TblOrganization>();
                    List<TblOrganization> haveList = new ArrayList<TblOrganization>();
                    List<TblOrganization> delList = new ArrayList<TblOrganization>();
                    // 新提交的组织架构
                    String[] oids = orgid.split(",");
                    for (String id : oids) {
                        for (TblOrganization lid : list) {
                            if (lid.getOrgid().toString().equals(id)) {
                                haveList.add(lid);
                            }
                        }
                    }
                    for (String id : oids) {
                        boolean isNew = true;
                        for (TblOrganization lid : haveList) {
                            if (lid.getOrgid().toString().equals(id)) {
                                isNew = false;
                            }
                        }
                        if (isNew) {
                            newList.add(this.queryOrganizationById(new BigDecimal(id)));

                        }
                    }
                    for (TblOrganization lid : list) {
                        boolean isDel = true;
                        for (String id : oids) {
                            if (lid.getOrgid().toString().equals(id)) {
                                isDel = false;
                            }
                        }
                        if (isDel) {
                            delList.add(lid);
                        }
                    }
                    if (newList.size() > 0) {
                        for (TblOrganization o : newList) {
                            // -----------------//
                            // 新表
                            TblAssessTarget assessTarget = new TblAssessTarget();
                            assessTarget.setAssid(assess.getAssid());

                            assessTarget.setOrgid(o.getOrgid());
                            this.tblAssessTargetMapper.insert(assessTarget);
                            // ----------------//

                            for (TblAssEleCategory tblAssEleCategory : assEleCategories) {
                                TblAssessMark assessMark = new TblAssessMark();
                                assessMark.setAssid(assess.getAssid().intValue());
                                assessMark.setStaffid(staff.getStaffid());
                                assessMark.setAssorgid(o.getOrgid());

                                assessMark.setSuitable("1");// 适用
                                assessMark.setAsseleid(tblAssEleCategory.getAsseleid());
                                assessMark.setAssesstargetid(assessTarget.getAssesstargetid());
                                this.tblAssessMarkMapper.insert(assessMark);
                            }
                        }
                    }
                    for (TblOrganization o : delList) {

                        this.markVoMapper.deleteAssessMarkByAssIdAndOrgId(assess.getAssid(), o.getOrgid());

                        // ---------------//
                        // 新表
                        this.tblAssessTargetMapper.deleteAssessTargetByAssIdAndOrgId(assess.getAssid(), o.getOrgid());

                        // ---------------//
                    }
                    return new JsonBean(200, "修改成功", null);
                } else {
                    if (assEleCategories.size() > 0) {
                        // 更换评价模板
                        assess.setAsstemid(templatekey);
                        assess.setStartdate(DateUtils.StringToLocalDateTime(start, "yyyy-MM-dd"));
                        assess.setEnddate(DateUtils.StringToLocalDateTime(end, "yyyy-MM-dd"));
                        // assess.setBaseday(DateUtils.parse(base));//基准日
                        assess.setAssstatus(1 + "");
                        assess.setAssessname(assessname);
                        assess.setAsssponsor(staff.getRealname());
                        this.tblAssessMapper.updateById(assess);

                        for (TblOrganization o : list) {

                            this.markVoMapper.deleteAssessMarkByAssIdAndOrgId(assess.getAssid(), o.getOrgid());

                            // ---------------//
                            // 新表
                            this.tblAssessTargetMapper.deleteAssessTargetByAssIdAndOrgId(assess.getAssid(), o.getOrgid());

                        }
                        String[] oids = orgid.split(",");
                        for (String string : oids) {
                            TblOrganization organization = this.organizationServiceMapper.loadTblOrganization(new BigDecimal(string));
                            // --------新表----------//
                            // 新表
                            TblAssessTarget assessTarget = new TblAssessTarget();

                            assessTarget.setAssid(assess.getAssid());
                            if (organization != null) {
                                assessTarget.setOrgid(organization.getOrgid());
                            }

                            this.tblAssessTargetMapper.insert(assessTarget);
                            // ---------------------//
                            for (TblAssEleCategory tblAssEleCategory : assEleCategories) {
                                TblAssessMark assessMark = new TblAssessMark();
                                assessMark.setAssid(assess.getAssid().intValue());
                                assessMark.setStaffid(staff.getStaffid());
                                if (organization != null) {
                                    assessMark.setAssorgid(organization.getOrgid());
                                }
                                assessMark.setSuitable("1");// 适用
                                assessMark.setAsseleid(tblAssEleCategory.getAsseleid());
                                assessMark.setAssesstargetid(assessTarget.getAssesstargetid());

                                this.tblAssessMarkMapper.insert(assessMark);
                            }

                        }
                        return new JsonBean(200, "修改成功", null);
                    }
                    return new JsonBean(0, "模板不完整，请更换模板", null);

                }
            }
            return new JsonBean(0, "项目已启动,不能修改", null);

        } else {
            new JsonBean(0, "结束时间不能大于开始时间", null);

        }
        return null;

    }

	@Override
	public Integer findTblAssessByTempid(BigDecimal assId) throws Exception {
		// TODO Auto-generated method stub
		return tblAssessMapper.findTblAssessByTempid(assId);
	}

	 @Override
	    public JsonBean deleteCanPingRen(String assMarkids) {
	        String[] str = assMarkids.split(",");
	        BigDecimal markid = null;
	        for (String string : str) {
	        	if(StringUtils.isNotBlank(string)){
	            markid = new BigDecimal(string);
	            TblAssessMark assessMark = this.tblAssessMarkMapper.selectById(markid);
	            this.assessStaffMapper.deleteBySmarkid(markid);
	        }
	        }
	        return new JsonBean(200, "成功", null);
	    }
	 
	  
	    //组织架构标记
	    @Override
	    public List<Tree> getNodeAllbm(BigDecimal nodeId) {
	    	  List<Tree> trees=null;
	        if (com.huabo.compliance.config.DateBaseConfig.DATABASETYPE.equals("Oracle")) {
	            //return (List<Tree>) tblOrganizationMapper.selectByPrimaryKey(nodeId);
	            Jedis jedis = JedisUtil.getJedis();
	            try {
	                if (jedis.exists(RedisFinalUtis.ORGTREEDEPTLIST + nodeId)) {
	                    String str = jedis.get(RedisFinalUtis.ORGTREEDEPTLIST + nodeId);
	                    trees = (List<Tree>) com.alibaba.fastjson.JSONArray.parseArray(str, Tree.class);
	                } else {
	                	trees = new ArrayList<Tree>();
	                    List<Tree> children = new ArrayList<Tree>();
	                    List<TblOrganization> list = tblOrganizationMapper.findByNodeId(nodeId);
	                    for (TblOrganization tblOrganization : list) {
	                        // Set<TblOrganization> chil = tblOrganization.getChildren();
	                        Set<TblOrganization> chil = tblOrganizationMapper.findByfatherorgId(tblOrganization.getOrgid());
	                        children = getNoteTreesbm(chil);
	                        Tree tree = new Tree();
	                        tree.setChildren(children);
	                        tree.setName(tblOrganization.getOrgname());
	                        tree.setId(tblOrganization.getOrgid());
	                        tree.setpId(tblOrganization.getFatherorgid());
	                        tree.setOpen(true);
	                        tree.setIsParent(chil.size() > 0 ? true : false);
	                        trees.add(tree);
	                    }
	                }
	            } finally {
	                //JedisUtil.returnResource(jedis);
	            }
	        } 
	        return trees;
	    }
	    
	    private List<Tree> getNoteTreesbm(Set<TblOrganization> chil) {
	        List<Tree> children = new ArrayList<Tree>();
	        for (TblOrganization tblOrganization2 : chil) {
	            if (tblOrganization2.getOrgtype() != null && tblOrganization2.getOrgtype() != 0) {
	                continue;
	            }
	            if (tblOrganization2.getStatus() != null && tblOrganization2.getStatus() == 0) {
	                Tree tree = new Tree();
	                Set<TblOrganization> chil2 = tblOrganizationMapper.findByfatherorgId(tblOrganization2.getOrgid());
	                if (chil2.size() > 0) {
	                    List<Tree> children1 = new ArrayList<Tree>();
	                    children1 = getNoteTrees(chil2);
	                    tree.setChildren(children1);
	                }
	                tree.setName(tblOrganization2.getOrgname());
	                tree.setId(tblOrganization2.getOrgid());
	                tree.setpId(tblOrganization2.getFatherorgid());
	                tree.setOpen(true);
	                tree.setIsParent(chil2.size() > 0 ? true : false);
	                children.add(tree);
	            }
	        }
	        return children;
	    }
	    private List<Tree> getNoteTrees(Set<TblOrganization> chil) {
	        List<Tree> children = new ArrayList();
	        Iterator var3 = chil.iterator();

	        while (true) {
	            TblOrganization tblOrganization2;
	            do {
	                do {
	                    if (!var3.hasNext()) {
	                        return children;
	                    }

	                    tblOrganization2 = (TblOrganization) var3.next();
	                } while (tblOrganization2.getStatus() != null && tblOrganization2.getStatus() == 1);
	            } while (tblOrganization2.getOrgtype() != null && tblOrganization2.getOrgtype() != 0);

	            if (tblOrganization2.getStatus() != null && tblOrganization2.getStatus() == 0) {
	                Tree tree = new Tree();
	                Set<TblOrganization> chil2 = tblOrganizationMapper.findByfatherorgId(tblOrganization2.getOrgid());
	                if (chil2.size() > 0) {
	                    new ArrayList();
	                    List<Tree> children1 = this.getNoteTrees(chil2);
	                    tree.setChildren(children1);
	                }

	                tree.setName(tblOrganization2.getOrgname());
	                tree.setId(tblOrganization2.getOrgid());
	                tree.setpId(tblOrganization2.getFatherorgid());
	                tree.setOpen(true);
	                tree.setIsParent(chil2.size() > 0);
	                children.add(tree);
	            }
	        }

	    }
}
