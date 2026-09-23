package com.huabo.monitor.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JedisUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.RedisFinalUtis;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.database.GeneralSQLConcatConfig;
import com.hbfk.util.redis.Random.RandomUtil;
import com.huabo.monitor.entity.*;
import com.huabo.monitor.mapper.*;
import com.huabo.monitor.mysql.mapper.TblOrganizationMySqlMapper;
import com.huabo.monitor.service.ITblStaffService;
import com.huabo.monitor.service.TblAssessService;
import com.huabo.monitor.service.TblOrganizaService;
import com.huabo.monitor.util.ConstClass;
import com.huabo.monitor.util.DateUtils;
import com.huabo.monitor.util.FiexibleNameAssignment;
import com.huabo.monitor.vo.param.MaxNumberParam;
import com.huabo.monitor.vo.param.fieldActivationVo;
import com.huabo.monitor.vo.param.fieldOrgStaffId;
import com.huabo.monitor.vo.param.fieldOrgStaffName;
import com.vip.vjtools.vjkit.collection.CollectionUtil;

import net.sf.json.JSONArray;
import redis.clients.jedis.Jedis;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.PrePersist;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

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
    TblAttachmentMapper attachmentMapper;
    
    @Resource
    TblAssessAttMapper tblAssessAttMapper;
    
    @Resource
    private TblAssessPlanMapper tblAssessPlanMapper;
    
    @Resource
    private TblOrganizaService tblOrganizaService;
    
    @Resource
    private TblAssessTeammemberMapper tblAssessTeammemberMapper;
    
    @Resource
	private ITblStaffService staffService;
	

    
    @Resource
    private TblOrganizationMapper tblOrganizationMapper;
    
    public JsonBean findByPage(Integer pageNumber, Integer pageSize, String assNumnber, String assName, String startDate, String startDates, String endDate, String endDates) {
        return null;
    }

    /**
     *  评价列表
     */
    @Override
    public IPage<TblAssessVo> initiatePjgl (boolean isAudit,BigDecimal orgid,BigDecimal staffid,String realName,Integer pageNumber, String assNumnber, String assName, String startDate, String startDates, String endDate, String endDates,Integer authorityType){

        IPage<TblAssessVo> iPage = new Page<>(pageNumber, ConstClass.DEFAULT_SIZE);


        return   this.tblAssessMapper.findPageBeanPJLX(iPage,isAudit,orgid,staffid,realName,assNumnber,assName,startDate,startDates,endDate,endDates,authorityType);
    }

    @Override
    public IPage<TblAssessVo> initiatePjjg(Integer pageNumber, String assNumnber, String assName, String startDate, String startDates, String endDate, String endDates, TblStaffUtil staff,Integer authorityType) {
        IPage<TblAssessVo> iPage = new Page<>(pageNumber, ConstClass.DEFAULT_SIZE);
        return this.tblAssessMapper.findPageBeanPJJG(iPage,assNumnber,assName,startDate,startDates,endDate,endDates,staff,authorityType);
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
    @Override
    public String findAutoNextId(String tblName, String column, String orgCol, BigDecimal orgid, Integer NoId, String chChoiceCol, String choiceVal, String bjf) throws Exception {

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
                Integer number = null;
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
                if(noCode.equals("ELE") || noCode.equals("TRI")){
                    number = 10;
                }else {
                    number = 9;
                }


                //判断插入的是一级编号和同级编号是
                noSql = " SELECT " + column + " FROM ( ";
                noSql += "SELECT MAX(TO_NUMBER(SUBSTR(" + column + "," + number + "))) as result ," + column + "  FROM " + tblName + " WHERE ";
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

                    noSql += " AND " + column + " LIKE '" + jgf +"' "+"GROUP BY " + column + " ORDER BY result desc";//AND (LENGTH(REPLACE(" + column + ",'" + noCode + "','')) - LENGTH(REPLACE(REPLACE(" + column + ",'" + noCode + "',''),'" + sep + "',''))) = " + 1;
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
                    noSql += " AND " + chChoiceCol + " " + bjf + " " + choiceVal+" "+"GROUP BY " + column +" ORDER BY result desc";
                }
                noSql += " ) WHERE ROWNUM=1";
                //查询是否存在规则编号 ， 没有就新增 有就在此基础上加1
                System.out.println(noSql);
                result = yhrPageMapper.querySqlRetnString(noSql);
                return getNewCode_year_number(result, ton);
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
    public void findAll(IPage<Map<String, Object>> page, String tblComany, String templeNumber, String templename, String orgids,BigDecimal secrectLevelId,TblStaffUtil user) {
try {
	//密级
	String sql = GeneralSQLConcatConfig.concatSecrectSql(user.getCurrentOrg().getUseSecrect(), false, "t.orgid", "LINKDEPTID", "STAFFID", "SECRECTLEVELID", "STAFFSCOPEIDS", user.getStaffid(), user.getDeptIds(), user.getSecrectScopeIds());
    StringBuffer buf=new StringBuffer();
    buf.append(sql);
	if( secrectLevelId!=null){
  	List<String> levels=tblAssessMapper.getSecrectLevel(secrectLevelId.toString());
  	final String levelsStr=sql+String.join(",", levels);
   	 buf.append(" and (t.SECRECTLEVELID IN (").append(String.join(",", levels)).append(") ").append(" or ").append("t.SECRECTLEVELID").append(" IS NULL OR ").append("t.SECRECTLEVELID").append(" = ''  )");;
  }
        String hql = " select distinct t.* from TBL_ASSESSTEMPLE t inner join TBL_TEMPLE_ORGANIZATION o\n" +
                "on t.ASSTEMID=o.ASSTEMID where o.orgid in(" + orgids + ") and t.ORGID = " + tblComany;
        if (StringUtils.isNotBlank(templename)) {
            hql += " and t.templename like '%" + templename + "%'";
        }
        if (StringUtils.isNotBlank(templeNumber)) {
            hql += " and t.templeNumber like '%" + templeNumber + "%'";
        }
        hql+=buf.toString();
        hql += " order by t.asstemid desc";
        String sqlCount = "select count(*) from (" + hql + ")";
        long start = (page.getCurrent() - 1) * page.getSize();
        long end = start + page.getSize();
        page.setTotal(this.yhrPageMapper.queryCount(sqlCount));
        page.setRecords(this.yhrPageMapper.queryList(start, end, hql));
} catch (Exception e) {
	// TODO: handle exception
	e.printStackTrace();
}
    }

    /**
     * @param assessid 查询条数
     * @param orgid    所在机构orgid
     * @return
     */
    @Override
    public Long selectTblassessNumber(String assessid, BigDecimal orgid) throws Exception{
//        String sqlcount = "SELECT COUNT(*) from Tbl_ASSESS t where t.assessid = '" + assessid + "'"
//                + "AND TBLCOMANY IN (SELECT ORGID FROM TBL_ORGANIZATION WHERE  1=1 START WITH FATHERORGID= " + orgid + " AND ORGTYPE=0  CONNECT BY PRIOR ORGID = FATHERORGID UNION ALL SELECT " + orgid + "  FROM DUAL)";
//        return yhrPageMapper.queryCount(sqlcount);
        List<BigDecimal> orgList= tblOrganizaService.getTblOrganizationAll(orgid);
      TblAssessVo vo=new TblAssessVo();
      vo.setOrgList(orgList);
      vo.setAssessid(assessid);
     return tblAssessMapper.findAssessCount(vo);
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
    public JsonBean saveAssEss(TblStaffUtil staff, TblAssess tblAssess, String orgid,String attids) {
    	
    	System.out.println("tblAssess==================="+tblAssess);

        List<TblAssEleCategory> assEleCategories = this.tblAssEleCategoryMapper
                .getAssesscategoryByMuBanId(tblAssess.getAsstemid());
//
//        TblStaff tblStaff = (TblStaff) request.getSession().getAttribute("longUser");
        
        System.out.println("assEleCategories==================="+assEleCategories);
        
        if (assEleCategories.size() > 0) {

            // 保存TblAssess
            //Serializable assessId = this.save(tblAssess);
        	tblAssess.setAssid(RandomUtil.uuBigDecimalId());
            this.tblAssessMapper.insert(tblAssess);
            if (StringUtils.isNotBlank(attids)) {
                String[] ids = attids.split(",");
                TblAssessAtt  ass=new TblAssessAtt();
                for (int i = 0; i < ids.length; i++) {
                    ass.setAssid(tblAssess.getAssid());
        	        ass.setAttid(new BigDecimal(ids[i]));
                    this.tblAssessAttMapper.insert(ass);
                }
             }
            
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
                assessTarget.setAssesstargetid(RandomUtil.uuBigDecimalId());
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
                    assessMark.setAssmarkid(RandomUtil.uuBigDecimalId());
                    this.tblAssessMarkMapper.insert(assessMark);
                }
            }
            return new JsonBean(1, "成功", null);
        } else {
            return new JsonBean(0, "评价模板不完整,请选择其他模板", null);
        }

    }
    
    @Override
    public JsonBean saveAssPlan(TblStaffUtil staff, TblAssessPlan tblAssess, String orgid,String attids) {
    	tblAssess.setAssid(RandomUtil.uuBigDecimalId());
    	this.tblAssessPlanMapper.insert(tblAssess);
            if (StringUtils.isNotBlank(attids)) {
                String[] ids = attids.split(",");
                TblAssessAtt  ass=new TblAssessAtt();
                for (int i = 0; i < ids.length; i++) {
                    ass.setAssid(tblAssess.getAssid());
        	        ass.setAttid(new BigDecimal(ids[i]));
                    this.tblAssessAttMapper.insert(ass);
                }
             }
            return new JsonBean(1, "成功", null);

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


    public static void main(String[] args) {
        String result="ELE-2023-01";
        TblOrgNoId orgNoId = new TblOrgNoId();
        orgNoId.setNocode("ELE");
        orgNoId.setNoSepartor("-");

        /*String result="PJ-2023-02";
        TblOrgNoId orgNoId = new TblOrgNoId();
        orgNoId.setNocode("PJ");
        orgNoId.setNoSepartor("-");*/
        getNewCode_year_number(result,orgNoId);
    }

    private static String getNewCode_year_number(String result, TblOrgNoId orgNo) {
    	String code;
    	SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy"); // 年月
		String yearmonth = sdf1.format(new Date());
		if(result == null||"".equals(result)){
			code = orgNo.getNocode()+orgNo.getNoSepartor()+yearmonth+orgNo.getNoSepartor();
			if(orgNo.getNoSepartor() != null){
				//code = orgNo.getNoSepartor()+code;
			}
			if(orgNo.getNoNumber()==-1){
				code = code+"01";
			}else if(orgNo.getNoNumber()==1){
				code=code+"00001";
			}else if(orgNo.getNoNumber()==2){
				code=code+"001";
			}

		}else{
			if(orgNo.getNoSepartor() == null){
				String num = result.replace(orgNo.getNocode(),"");
				Integer no = Integer.parseInt(num);
				no++;
				int nolength = num.length()-no.toString().length();
				code = orgNo.getNocode();
				for (int i = 0; i < nolength; i++) {
					code += "0";
				}
				code += no;
			}else if(result.indexOf("-")>0&&result.indexOf(yearmonth)==-1){
				code =orgNo.getNocode()+orgNo.getNoSepartor()+yearmonth+orgNo.getNoSepartor()+"01";
			}else if(result.indexOf("-")>0&&(result.indexOf("ELE")!=-1 || result.indexOf("TRI")!=-1)){
                String num = result.substring(9,result.length());
                Integer no = Integer.parseInt(num);
                no++;
                code = orgNo.getNocode()+orgNo.getNoSepartor()+yearmonth+orgNo.getNoSepartor();
                if(no<10){
                    code+="0";
                }
                code += no;
            }else{
				String num = result.substring(8,result.length());
				Integer no = Integer.parseInt(num);
				no++;
				code = orgNo.getNocode()+orgNo.getNoSepartor()+yearmonth+orgNo.getNoSepartor();
                if(no<10){
                    code+="0";
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
      /*  //  体系权重得分
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
        List<Map<String, Object>> list2 = this.yhrPageMapper.queryBySqlToList(sql);*/
//------------------------新替换方法
        //  体系权重得分  老系统中评价类别是父子级关系  新系统中评价类别只有同级
        String sql = "SELECT asscatid,catname catname1,CATWEIGHT catw  FROM Tbl_Assesscategory  where asstemid=" + tmplId + " ORDER  BY   asscatid";
        List<Map<String, Object>> list = this.yhrPageMapper.queryBySqlToList(sql);
        list.stream().map(x->{ x.put("CATW", returnDoublePer(x.get("CATW")));return x;}).collect(Collectors.toList());
        
        // 要素得分明细
        sql = "select lin.*,t.standardscore,ta.elementname,ta.auditpoint from (SELECT\n" +
                "    E.asscatid,E.catname  catname1, CATWEIGHT catw FROM Tbl_Assesscategory e where asstemid=" + tmplId + "\n" +
                "ORDER   BY  E.asscatid " +
                ") lin inner join TBL_ASSELE_CATEGORY t on lin.asscatid=t.asscatid inner join TBL_ASSESSELEMENT ta on ta.ASSELEID=t.ASSELEID\n" +
                "order by  lin.asscatid\n";
        List<Map<String, Object>> list2 = this.yhrPageMapper.queryBySqlToList(sql);
        list2.stream().map(x->{ x.put("CATW", returnDoublePer(x.get("CATW")));return x;}).collect(Collectors.toList());
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
                assessStaff.setAssstaffid(RandomUtil.uuBigDecimalId());
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
        String sql="delete from Tbl_Assess_att where assid="+assid;
        this.yhrPageMapper.delete(sql);
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
    public JsonBean updateAssEss(TblStaffUtil staff, String assid, String assessname, String start, String end, String staffid, BigDecimal templatekey, String orgid
    		,String assteamname,String assteamlead,String assteammemberids,BigDecimal planid,String planname,BigDecimal secrectlevelid,String staffScopeNames,String staffscopeids,fieldActivationVo content,String attids) {
        if (DateUtils.compare_date(end, start) == 1) {

            TblAssess assess = this.tblAssessMapper.selectById(assid);
            if (assess.getAssstatus().equals(TblAssess.CREATE)) {
                // 现有的组织架构

                List<TblOrganization> list = this.tblAssessMapper.getOrgByassId(assess.getAssid());

                List<TblAssEleCategory> assEleCategories = this.tblAssEleCategoryMapper
                        .getAssesscategoryByMuBanId(templatekey);
                // 未更换评价模板
                if (assess.getAsstemid().equals(templatekey)) {
                    //assess.setStartdate(DateUtils.StringToLocalDateTime(start, "yyyy-MM-dd"));
                   // assess.setEnddate(DateUtils.StringToLocalDateTime(end, "yyyy-MM-dd"));
                	 try {
						assess.setStartdate(new SimpleDateFormat("yyyy-MM-dd").parse(start));
						  assess.setEnddate(new SimpleDateFormat("yyyy-MM-dd").parse(end));
                	 } catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                    // assess.setBaseday(DateUtils.parse(base));//基准日
                    assess.setAssstatus(TblAssess.CREATE);
                    assess.setAssessname(assessname);
                    assess.setAsssponsor(staff.getRealname());
                    assess.setSecrectLevelId(secrectlevelid);
                    assess.setStaffScopeNames(staffScopeNames);
                    assess.setStaffScopeIds(staffscopeids);
                    assess.setPlanid(planid);
                    assess.setPlanname(planname);

                    //---------------------------------
                    if (staffid != null && staffid.trim().length() > 0) {
                        assess.setLeaderid(new BigDecimal(staffid)); //负责人id
                    }
                    
                    assess.setAssteamname(assteamname);
                    assess.setAssteamlead(assteamlead);
                   if(content!=null){
                	   assess.setFieldActivationCopy(content);
                   }
                    this.tblAssessMapper.updateById(assess);
                    
                    
                    if (StringUtils.isNotBlank(attids)) {
                        String[] ids = attids.split(",");
                        TblAssessAtt  ass=new TblAssessAtt();
                        for (int i = 0; i < ids.length; i++) {
                            ass.setAssid(assess.getAssid());
                	        ass.setAttid(new BigDecimal(ids[i]));
                            this.tblAssessAttMapper.insert(ass);
                        }
                     }
                    
                    
                 /*
                    //评价小组组员  --先删除
                    this.tblAssessMapper.delAssessTeamMember(assess.getAssid());
		            if(null != assteammemberids) {
		            	String[] assteammemberidArr = assteammemberids.split(",");
		            	for (int i = 0; i < assteammemberidArr.length; i++) {
		            		String assteammemberid = assteammemberidArr[i];
		            		
		            		insAssessTeamMember(assteammemberid,assess.getAssid());
						}
		            }
*/
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
//                        assess.setStartdate(DateUtils.StringToLocalDateTime(start, "yyyy-MM-dd"));
//                        assess.setEnddate(DateUtils.StringToLocalDateTime(end, "yyyy-MM-dd"));
                        try {
                        	assess.setEnddate(new SimpleDateFormat("yyyy-MM-dd").parse(end));
							assess.setStartdate(new SimpleDateFormat("yyyy-MM-dd").parse(start));
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
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
    public JsonBean updateAssPlan(TblStaffUtil staff,String start, String end,String attids, TblAssessPlan assess) {
        if (DateUtils.compare_date(end, start) == 1) {
            try {
            	assess.setStartdate(new SimpleDateFormat("yyyy-MM-dd").parse(start));
				assess.setEnddate(new SimpleDateFormat("yyyy-MM-dd").parse(end));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            this.tblAssessPlanMapper.updateById(assess);
            if (StringUtils.isNotBlank(attids)) {
                String[] ids = attids.split(",");
                TblAssessAtt  ass=new TblAssessAtt();
                for (int i = 0; i < ids.length; i++) {
                    ass.setAssid(assess.getAssid());
        	        ass.setAttid(new BigDecimal(ids[i]));
                    this.tblAssessAttMapper.insert(ass);
                }
             }
            return new JsonBean(200, "修改成功", null);
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
	        if (com.huabo.monitor.config.DateBaseConfig.DATABASETYPE.equals("Oracle")) {
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

		@Override
		public void insAssessTeamMember(String assteammemberid, BigDecimal assid) {
			//tblAssessMapper.insAssessTeamMember(assteammemberid,assid);
			TblAssessTeammember men=new TblAssessTeammember();
			men.setAssid(assid);
			men.setStaffid(assteammemberid);
			men.setId(RandomUtil.uuBigDecimalId());
			tblAssessTeammemberMapper.insert(men);
		}

		@Override
		public List<TblStaff> gettAssteamMembers(BigDecimal assId) {
			return tblAssessMapper.gettAssteamMembers(assId);
		}
	    
	    @Override
	    @Transactional(rollbackFor = Exception.class)
	    public void saveRepAtt(TblAttachment a, String assid) {
	        attachmentMapper.insert(a);
	        TblAssessAtt  ass=new TblAssessAtt();
	        ass.setAssid(new BigDecimal(assid));
	        ass.setAttid(a.getAttid());
	        this.tblAssessAttMapper.insert(ass);
	    }

	    @Override
	    @Transactional(rollbackFor = Exception.class)
	    public void delAssessAtt(BigDecimal attid, String assid) {
	        String sql="delete from Tbl_Assess_att where attid="+attid;
	        this.yhrPageMapper.delete(sql);
	        attachmentMapper.deleteEntity(attid);

	    }
	    
	    @Override
	    public List<TblAttachment> getAssessAttListByAssid(BigDecimal assid) {
	        String sql="select * from TBL_ATTACHMENT where attid in (select attid from Tbl_Assess_att where assid="+assid+"  )";
	        return this.attachmentMapper.getListBySql(sql);
	    }
	    
	    
	//=================================
    @Override
    public IPage<TblAssessPlanVo> initiatePjjh(boolean isAudit,BigDecimal orgid,BigDecimal staffid,String realName,Integer pageNumber, String assNumnber, String assName, String startDate, String startDates, String endDate, String endDates,Integer status,Integer authorityType){
        IPage<TblAssessPlanVo> iPage = new Page<>(pageNumber, ConstClass.DEFAULT_SIZE);
        return   this.tblAssessMapper.findPageBeanPJJH(iPage,isAudit,orgid,staffid,realName,assNumnber,assName,startDate,startDates,endDate,endDates, status,authorityType);
    }

	@Override
	public int getMajorByAssid(BigDecimal assId, String staffid) throws Exception {
		// TODO Auto-generated method stub
		int count=tblAssessMapper.getMajorByAssid(assId,staffid);
		return count;
	}

	@Override
	public PageInfo<TblAssessPlan> initiatePjjhPage(boolean isAudit, BigDecimal orgid, BigDecimal staffid,
			String realName, Integer pageNumber, String assNumnber, String assName, String startDate, String startDates,
			String endDate, String endDates, Integer status, Integer authorityType,Integer pageSize,TblStaffUtil staff,BigDecimal secrectLevelld) throws Exception{
		// TODO Auto-generated method stub
		 TblAssessPlanVo vo=new TblAssessPlanVo();
		 vo.setStatus(status);
		 if(Objects.nonNull(staffid)){
			 vo.setCreatestaffid(staffid);
		 }
		 if(StringUtils.isNotBlank(assNumnber)){
			 vo.setAssessid(assNumnber);
		 }
		vo.setRealname(realName);
		vo.setAssessname(assName);
		if (StringUtils.isNotBlank(startDate)) {
			vo.setStartdate(DateUtils.StringToLocalDateTime(startDate, "yyyy-MM-dd"));
		}
		if (StringUtils.isNotBlank(startDates)) {
			vo.setStartdates(DateUtils.StringToLocalDateTime(startDates, "yyyy-MM-dd"));
		}
		if (StringUtils.isNotBlank(endDate)) {
			vo.setEnddate(DateUtils.StringToLocalDateTime(endDate, "yyyy-MM-dd"));
		}
		if (StringUtils.isNotBlank(endDates)) {
			vo.setEnddates(DateUtils.StringToLocalDateTime(endDates, "yyyy-MM-dd"));
		}
		vo.setAuthorityType(authorityType);
        String sql = GeneralSQLConcatConfig.concatSecrectSql(staff.getCurrentOrg().getUseSecrect(), false, "p.LINKORGID", "p.linkdeptid", "p.createstaffid", "p.SECRECTLEVELID", "p.STAFFSCOPEIDS", staff.getStaffid(), staff.getDeptIds(), staff.getSecrectScopeIds()); 
        StringBuffer buf=new StringBuffer();
        buf.append(sql);
        if( secrectLevelld!=null){
         	List<String> levels=tblAssessMapper.getSecrectLevel(secrectLevelld.toString());
         	final String levelsStr=sql+String.join(",", levels);
          	 buf.append(" and (p.SECRECTLEVELID IN (").append(String.join(",", levels)).append(") ").append(" or ").append("p.SECRECTLEVELID").append(" IS NULL OR ").append("p.SECRECTLEVELID").append(" = ''  )");;
         }
		// String sql=TblAssessPlanMapperSqlConfig.setSql(staff);
		 PageInfo<TblAssessPlan> pageInfo=PageMethod.startPage(pageNumber,pageSize).doSelectPageInfo(()->tblAssessPlanMapper.findList(vo,buf.toString()));
		 FiexibleNameAssignment ment=new FiexibleNameAssignment();
			if(CollectionUtil.isNotEmpty(pageInfo.getList())){
				pageInfo.getList().forEach(entity->{
					try {
						//对灵活字段中的姓名名称及机构名称赋值
						fieldOrgStaffId item=new fieldOrgStaffId();
						BeanUtils.copyProperties(entity,item); 
						fieldOrgStaffName nameEntity=ment.setOpenName(item);
						BeanUtils.copyProperties(nameEntity,entity ); 
						item=null; // 处理并解除引用
						nameEntity=null; // 处理并解除引用
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				} );
				
			}
		 return pageInfo;
	}

	@Override
	public PageInfo<TblAssess> initiatePjglPage(boolean isAudit, BigDecimal orgid, BigDecimal staffid, String realName,
			Integer pageNumber,Integer pageSize, String assNumnber, String assName, String startDate, String startDates, String endDate,
			String endDates, Integer authorityType,TblStaffUtil staff) {
		// TODO Auto-generated method stub
		 PageInfo<TblAssess> pageInfo=null;
		try {
		TblAssessVo vo=new TblAssessVo();
		 if(Objects.nonNull(orgid)){
				vo.setTblcomany(orgid.toString());
		 }
		 vo.setAuthorityType(authorityType);
		 vo.setIsAudit(isAudit);
		 if(Objects.nonNull(staffid)){
			 vo.setCreatestaffid(staffid);
		 }
		 if(StringUtils.isNotBlank(realName)){
			 vo.setAsssponsor(realName);
		 }
		 if(StringUtils.isNotBlank(assNumnber)){
			 vo.setAssessid(assNumnber);
		 }
		 if(StringUtils.isNotBlank(realName)){
				vo.setRealname(realName);
		 }
	    if (StringUtils.isNotBlank(assName)) {
			vo.setAssessname(assName);
        }
	    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
	    try {
		if (StringUtils.isNotBlank(startDate)) {
			 vo.setStartdate(sdf.parse(startDate));
		}
		if (StringUtils.isNotBlank(startDates)) {
			vo.setStartdates(sdf.parse(startDates));
		}
		if (StringUtils.isNotBlank(endDate)) {
			vo.setEnddate(sdf.parse(endDate));
		}
		if (StringUtils.isNotBlank(endDates)) {
			vo.setEnddates(sdf.parse(endDates));
		}
	    } catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	   // String sql=TblAssessMapperSqlConfig.setSql("t.", staff,0);
	    vo.setAuthorityType(authorityType);
	    StringBuffer querySql=new StringBuffer();
	    if(!isAudit){
	    	querySql.append(" or (t.asssponsor ='").append(vo.getAsssponsor()).append("' or t.leaderid=").append(vo.getCreatestaffid()).append(")");
	    }
	    if(authorityType!=null && authorityType==0){
	    	querySql.append(" or (CREATESTAFFID=").append(vo.getCreatestaffid()).append(" or LEADERID =").append(vo.getCreatestaffid()).append(")");
	    }
        String sql = GeneralSQLConcatConfig.concatSecrectSqlCase2(staff.getCurrentOrg().getUseSecrect(), authorityType==0, "t.LINKORGID", "t.linkdeptid", "t.createstaffid", "t.SECRECTLEVELID", "t.STAFFSCOPEIDS", staff.getStaffid(), staff.getDeptIds(), staff.getSecrectScopeIds(),querySql.toString()); 
		 pageInfo=PageMethod.startPage(pageNumber,pageSize).doSelectPageInfo(()->tblAssessMapper.findList(vo,sql));
		 FiexibleNameAssignment ment=new FiexibleNameAssignment();
			if(CollectionUtil.isNotEmpty(pageInfo.getList())){
				pageInfo.getList().forEach(entity->{
					try {
						//对灵活字段中的姓名名称及机构名称赋值
						fieldOrgStaffId item=new fieldOrgStaffId();
						BeanUtils.copyProperties(entity,item); 
						fieldOrgStaffName nameEntity=ment.setOpenName(item);
						BeanUtils.copyProperties(nameEntity,entity ); 
						item=null; // 处理并解除引用
						nameEntity=null; // 处理并解除引用
						StringBuffer buffer=new StringBuffer();
						buffer.append("[");
						List<String> list=tblAssessMapper.findXfList(entity.getAssid().toString());
                        for(String str:list){
                        	buffer.append("{\"formId\":"+entity.getAssid()+",\"distributionTitle\":\""+entity.getAssessname()+"\",\"reciver\":"+str+",\"isread\":0,\"moduleType\":\"nkhg\"}");
                        }
                        entity.setJsonString(buffer.append("]").toString());
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				} );
				
			}
		
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		 return pageInfo;
	}

	@Override
	public PageInfo<Map<String,Object>> getOrgByassIdNew(BigDecimal assid,Integer pageNumber,Integer pageSize) throws Exception {
		// TODO Auto-generated method stub
		List<BigDecimal> groupList = tblAssessMapper.getMaxAssorgid(assid);
         TblOrganization org=new TblOrganization();
         org.setGetMaxAssorgidList(groupList);
         PageInfo<Map<String,Object>> pageInfo=PageMethod.startPage(pageNumber,pageSize).doSelectPageInfo(()->tblOrganizationMapper.getOrgByassIdNew(org));
		return pageInfo;
	}

	@Override
	public PageInfo<TblAssessMarkVo> findAssessMarkIpage(Integer pagenumber, Integer pagesize, BigDecimal assid,
			BigDecimal orgid) {
		// TODO Auto-generated method stub
		TblAssessMarkVo mark=new TblAssessMarkVo();
		mark.setAssid(assid);
		mark.setAssorgid(orgid);
		PageInfo<TblAssessMarkVo> pageInfo=PageMethod.startPage(pagenumber,pagesize).doSelectPageInfo(()->tblAssessMarkMapper.findAssessMarkIpage(mark));
		
		//  markVoMapper.queryTblAssessMarkVoPage(page, assid, orgid);
		return pageInfo;
	}

	@Override
	public PageInfo<TblAssessVo> initiatePjglNew(boolean isAudit, BigDecimal orgid, BigDecimal staffid, String realName,
			Integer pageNumber,Integer pageSize, String assNumnber, String assName, String startDate, String startDates, String endDate,
			String endDates, Integer authorityType,TblStaffUtil staff ) {
		// TODO Auto-generated method stub
		 PageInfo<TblAssessVo> pageInfo=null;
		 try {
		TblAssessVo vo=new TblAssessVo();
		 if(Objects.nonNull(orgid)){
				vo.setTblcomany(orgid.toString());
		 }
		 vo.setAuthorityType(authorityType);
		 vo.setIsAudit(isAudit);
		 if(Objects.nonNull(staffid)){
			 vo.setCreatestaffid(staffid);
		 }
		 if(StringUtils.isNotBlank(realName)){
			 vo.setAsssponsor(realName);
		 }
		 if(StringUtils.isNotBlank(assNumnber)){
			 vo.setAssessid(assNumnber);
		 }
		 if(StringUtils.isNotBlank(realName)){
				vo.setRealname(realName);
		 }
	    if (StringUtils.isNotBlank(assName)) {
			vo.setAssessname(assName);
       }
	    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
	    try {
		if (StringUtils.isNotBlank(startDate)) {
			 vo.setStartdate(sdf.parse(startDate));
		}
		if (StringUtils.isNotBlank(startDates)) {
			vo.setStartdates(sdf.parse(startDates));
		}
		if (StringUtils.isNotBlank(endDate)) {
			vo.setEnddate(sdf.parse(endDate));
		}
		if (StringUtils.isNotBlank(endDates)) {
			vo.setEnddates(sdf.parse(endDates));
		}
	    } catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	   // String sql=TblAssessMapperSqlConfig.setSql("t.", staff,0);
	    StringBuffer querySql=new StringBuffer();
	    if(!isAudit){
	    	querySql.append(" or (t.asssponsor ='").append(vo.getAsssponsor()).append("' or t.leaderid=").append(vo.getCreatestaffid()).append(")");
	    }
        String sql = GeneralSQLConcatConfig.concatSecrectSqlCase2(staff.getCurrentOrg().getUseSecrect(), true, "t.LINKORGID", "t.linkdeptid", "t.createstaffid", "t.SECRECTLEVELID", "t.STAFFSCOPEIDS", staff.getStaffid(), staff.getDeptIds(), staff.getSecrectScopeIds(),querySql.toString()); 
		  pageInfo=PageMethod.startPage(pageNumber,pageSize).doSelectPageInfo(()->tblAssessMapper.findPageBeanPJLXNew(vo,sql));
		
		  FiexibleNameAssignment ment=new FiexibleNameAssignment();
			if(CollectionUtil.isNotEmpty(pageInfo.getList())){
				pageInfo.getList().forEach(entity->{
					try {
						//对灵活字段中的姓名名称及机构名称赋值
						fieldOrgStaffId item=new fieldOrgStaffId();
						BeanUtils.copyProperties(entity,item); 
						fieldOrgStaffName nameEntity=ment.setOpenName(item);
						BeanUtils.copyProperties(nameEntity,entity ); 
						item=null; // 处理并解除引用
						nameEntity=null; // 处理并解除引用
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				} );
				
			}
		 } catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		  return pageInfo;
	}

	@Override
	public PageInfo<TblAssessVo> initiatePjjgNew(Integer pageNumber, String assNumnber, String assName,
			String startDate, String startDates, String endDate, String endDates, TblStaffUtil staff,
			Integer authorityType,BigDecimal orgid) {
		// TODO Auto-generated method stub
		PageInfo<TblAssessVo> pageInfo=null;
		try {
      //  return this.tblAssessMapper.findPageBeanPJJG(iPage,assNumnber,assName,startDate,startDates,endDate,endDates,staff,authorityType);
		TblAssessVo vo=new TblAssessVo();
		 if(staff!=null&&Objects.nonNull(staff.getCurrentOrg().getOrgid())){
				vo.setTblcomany(staff.getCurrentOrg().getOrgid().toString());
		 }
		 vo.setAuthorityType(authorityType);
		 if(staff!=null&&Objects.nonNull(staff.getStaffid())){
			 vo.setCreatestaffid(staff.getStaffid());
		 }
		 if(StringUtils.isNotBlank(assNumnber)){
			 vo.setAssessid(assNumnber);
		 }
	    if (StringUtils.isNotBlank(assName)) {
			vo.setAssessname(assName);
	    }
	    if (orgid != null) {
			vo.setOrgid(orgid);
	    }
	    
	    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
	    try {
		if (StringUtils.isNotBlank(startDate)) {
			 vo.setStartdate(sdf.parse(startDate));
		}
		if (StringUtils.isNotBlank(startDates)) {
			vo.setStartdates(sdf.parse(startDates));
		}
		if (StringUtils.isNotBlank(endDate)) {
			vo.setEnddate(sdf.parse(endDate));
		}
		if (StringUtils.isNotBlank(endDates)) {
			vo.setEnddates(sdf.parse(endDates));
		}
	    } catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    StringBuffer querySql=new StringBuffer();
	    if(authorityType!=null && authorityType==0){
	    	querySql.append(" or (CREATESTAFFID=").append(vo.getCreatestaffid()).append(" or LEADERID =").append(vo.getCreatestaffid()).append(")");
	    }
        String sql = GeneralSQLConcatConfig.concatSecrectSqlCase2(staff.getCurrentOrg().getUseSecrect(), authorityType==0, "t.LINKORGID", "t.linkdeptid", "t.createstaffid", "t.SECRECTLEVELID", "t.STAFFSCOPEIDS", staff.getStaffid(), staff.getDeptIds(), staff.getSecrectScopeIds(),querySql.toString()); 
		  pageInfo=PageMethod.startPage(pageNumber,ConstClass.DEFAULT_SIZE).doSelectPageInfo(()->tblAssessMapper.findPageBeanPJJGNew(vo,sql));
		  FiexibleNameAssignment ment=new FiexibleNameAssignment();
			if(CollectionUtil.isNotEmpty(pageInfo.getList())){
				pageInfo.getList().forEach(entity->{
					try {
						//对灵活字段中的姓名名称及机构名称赋值
						fieldOrgStaffId item=new fieldOrgStaffId();
						BeanUtils.copyProperties(entity,item); 
						fieldOrgStaffName nameEntity=ment.setOpenName(item);
						BeanUtils.copyProperties(nameEntity,entity ); 
						item=null; // 处理并解除引用
						nameEntity=null; // 处理并解除引用
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				} );
				
			}
		
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		 return pageInfo;
	}
    
	//给数字加百分比
     public  String returnDoublePer(Object obj){
	  if(Objects.isNull(obj)){
		return new DecimalFormat("0.00%").format(new BigDecimal("100"));
    	}
	 return new DecimalFormat("0.00%").format(new Double(obj.toString())/100);
     }
 
     
     @Override
     public String findFlowNextIdNk(String tblName, String column, String orgCol, BigDecimal orgid, Integer NoId, String chChoiceCol, String choiceVal, String bjf) throws Exception {
         System.out.println("检查检点1");
         Integer isUse = organizationServiceMapper.selectUniqueColumn(orgid);
         if (isUse == 0) {
             return "-1";//该组织没有使用自定义编码
         } else {
             String orgNumber = organizationServiceMapper.selectOrgNumber(orgid);
             Map<String, Object> orgNo = tblAssessMapper.getInfo(orgid,NoId);
             if (orgNo == null) {
                 return "-1";
             } else {
                 String noSql = null;
                 String result = null;
                 String jgf = String.valueOf(orgNo.get("CODE"));
//                 jgf = jgf + "-"+orgNumber;
                 System.out.println("-------------------------------------------------------------");
                 System.out.println(jgf);
                 String noCode = String.valueOf(orgNo.get("CODE"));
                 String sep = String.valueOf(orgNo.get("SEP"));
                 TblOrgNoId ton = new TblOrgNoId();
                 ton.setNocode(noCode);
                 ton.setNoNumber(Integer.valueOf(String.valueOf(orgNo.get("NUMBER1"))));
                 if (sep != null) {
                     ton.setNoSepartor(sep);
                 }
                 if (jgf.indexOf("_") != -1) {
                     jgf = jgf.replace("_", "/_");
                 }
                 MaxNumberParam param=new MaxNumberParam();
                 param.setColumn(column);
                 param.setTblName(tblName);
                 param.setOrgCol(orgCol);
                 param.setOrgid(orgid);
                 param.setNoCode(noCode);
                 param.setSep(sep);
                 List<BigDecimal> addList = new ArrayList<>();
                 List<BigDecimal> orglist=tblOrganizaService.getOrgIdListAutoNumber();
                 List<BigDecimal> orglist2=tblOrganizaService.getOrgIdLis(orgid);
                 addList.addAll(orglist);
                 addList.addAll(orglist2);
                List<String> orgList = addList.stream()
                         .map(BigDecimal::toString)
                         .collect(Collectors.toList());
                 param.setOrgList(orgList);
                 //判断用户有没有使用分隔符
                 System.out.println("--------------222222-----------------------------------------------");
                 jgf = jgf + "-"+orgid;
                 System.out.println(jgf);

                 if (sep != null) {
                     if (jgf.indexOf("_") != -1) {
                         if ("_".equals(sep)) {
                             jgf += "/" +sep;
                         } else {
                             jgf +=sep;
                         }
                         jgf += "%' escape '/";
                     } else {
                         if ("_".equals(sep)) {
                             jgf += "/" + sep + "%' escape '/";
                         } else {
                             jgf +=sep + "%";
                         }
                     }
                     param.setJgf(jgf);
                 } else {
                     if (jgf.indexOf("_") != -1) {
                         jgf +="%' escape '/";
                     } else {
                         jgf +="%";
                     }
                   param.setJgf(jgf);
                 }
                 if (chChoiceCol != null) {
                     if ("包含".equals(bjf)) {
                         bjf = "LIKE";
                         choiceVal = "'%" + choiceVal + "%'";
                     } else if ("等于".equals(bjf)) {
                         bjf = "=";
                         choiceVal = "'" + choiceVal + "'";
                     }
                     param.setBjf(bjf) ;
                 }
                 result=  tblAssessMapper.getMaxNumberForNkpj(param);
                 if  (result == null) {
                     result = "0";
                 }
                 Integer num = Integer.parseInt(result)+1;
                 if(num<10){
                     jgf =  jgf.substring(0,jgf.length()-1)+"0"+num;
                 }else {
                     jgf =  jgf.substring(0,jgf.length()-1)+num;
                 }
                 System.out.println("jgf:"+jgf);
//                 return getNewCode(result, ton);
                 return jgf;
             }
         }
     }

	@Override
	public TblAssessPlan selectOne(BigDecimal planid) throws Exception {
		// TODO Auto-generated method stub
		return tblAssessPlanMapper.selectById(planid);
	}
	 
     }
