package com.huabo.audit.oracle.mapper;

import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.PageInfo;
import com.huabo.audit.oracle.entity.LeaveAudit3LEntity;
import com.hbfk.util.StringUtil;

import java.math.BigDecimal;
import java.util.Date;
import cn.hutool.core.date.DateUtil;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Rui
 * @ClassName LeaveAudit3LMapperSqlConfig
 * @Description
 * @DATE 2023/9/14
 */
public class LeaveAudit3LMapperSqlConfig {
	
	
	
	public String getcwanbList(BigDecimal id, BigDecimal relaId) throws Exception {
		String sql = "SELECT * FROM TBL_YQNS_LEAVE_AUDIT_3L RS LEFT JOIN TBL_STAFF TS ON RS.CREATE_USER = TS.STAFFID LEFT JOIN TBL_ORGANIZATION ORG ON RS.OLD_ORG_ID = ORG.ORGID WHERE ";
		if(relaId != null) {
			sql += " RS.ID IN (SELECT GLID FROM TBL_YQNS_FUND_AUDIT_PROJECT_GL WHERE CWID = "+relaId+")  ";
		}else {
			sql += " RS.ID IN (SELECT FORMID FROM TBL_YQNS_JHCGGL_RELA WHERE GLID = "+id+")  ";
//			sql += " RS.ID IN (SELECT FORMID FROM TBL_YQNS_JHCHUGGL_RELA WHERE GLID = "+relaId+") ";
		}
		sql += " order by RS.CREATE_TIME desc ";
		return sql;
	}
	
	
	
	public String selectListByjhzgGlId(BigDecimal id, BigDecimal relaId,String type) throws Exception {
		String sql = "SELECT * FROM TBL_YQNS_LEAVE_AUDIT_3L RS LEFT JOIN TBL_STAFF TS ON RS.CREATE_USER = TS.STAFFID LEFT JOIN TBL_ORGANIZATION ORG ON RS.OLD_ORG_ID = ORG.ORGID WHERE ";
		if(id != null) {
			sql += " RS.ID IN (SELECT FORMID FROM TBL_YQNS_JHCGGL_RELA WHERE GLID = "+id+")  ";
			if(type!=null && type.equals("1")) {
				sql += " and RS.ID not IN (SELECT GLID FROM TBL_YQNS_FUND_AUDIT_PROJECT_GL WHERE CWID = "+id+")  ";
			}
		}else {
			sql += " RS.ID IN (SELECT FORMID FROM TBL_YQNS_JHCGGL_RELA WHERE GLID = "+relaId+") ";
			if(type!=null && type.equals("1")) {
				sql += " and RS.ID not IN (SELECT GLID FROM TBL_YQNS_FUND_AUDIT_PROJECT_GL WHERE CWID = "+relaId+")  ";
			}
		}
		sql += " order by RS.CREATE_TIME desc";
		return sql;
	}
	
	public String selectListByjhchugGlId(BigDecimal id, BigDecimal relaId)  throws Exception{
		String sql = "SELECT * FROM TBL_YQNS_LEAVE_AUDIT_3L RS LEFT JOIN TBL_STAFF TS ON RS.CREATE_USER = TS.STAFFID LEFT JOIN TBL_ORGANIZATION ORG ON RS.OLD_ORG_ID = ORG.ORGID WHERE ";
		if(id != null) {
			sql += " RS.ID IN (SELECT FORMID FROM TBL_YQNS_JHCGGL_RELA WHERE GLID = "+id+")  ";
		}else {
			sql += " RS.ID IN (SELECT FORMID FROM TBL_YQNS_JHCGGL_RELA WHERE GLID = "+relaId+") ";
		}
		sql += " order by RS.CREATE_TIME desc"; 
		return sql;
	}
	
	public String selectListByjhcgGlId(BigDecimal id, BigDecimal orgId) throws Exception{
		String sql = "SELECT * FROM TBL_YQNS_LEAVE_AUDIT_3L RS LEFT JOIN TBL_STAFF TS ON RS.CREATE_USER = TS.STAFFID LEFT JOIN TBL_ORGANIZATION ORG ON RS.OLD_ORG_ID = ORG.ORGID WHERE ";
		
		if(orgId != null) {
			sql += " RS.ID NOT IN (SELECT FORMID FROM TBL_YQNS_JHCGGL_RELA WHERE GLID IN (SELECT ID FROM TBL_YQNS_JHGL_JHCG_GL WHERE GLTYPE = '23')) AND RS.ID IN (select NRID from TBL_YQNS_LEAVE_AUDIT_JD3L_GL where JDID IN ( SELECT JDID FROM TBL_YQNS_LEAVE_AUDIT_JD3L WHERE STATUS = 6 )) AND ORG.FATHERORGID = "+orgId;
		}else {
			sql += " RS.ID IN (SELECT FORMID FROM TBL_YQNS_JHCGGL_RELA WHERE GLID = "+id+") ";
		}
		
		sql += " order by RS.CREATE_TIME desc";
		return sql;
	}
	

    public String selectByEntity( LeaveAudit3LEntity leaveAudit3LEntity, TblStaffUtil user,BigDecimal jdid){
        StringBuffer sb = new StringBuffer();
        //	sb.append("SELECT * FROM (SELECT T1.* , ROWNUM RN FROM (");
        sb.append("SELECT * FROM TBL_YQNS_LEAVE_AUDIT_3L RS LEFT JOIN TBL_STAFF TS ON RS.CREATE_USER = TS.STAFFID WHERE 1=1 ");

        
        
         if(jdid!=null) { 
        	 sb.append(" and RS.ID in (select NRID from TBL_YQNS_LEAVE_AUDIT_JD3L_GL where JDID="+jdid+" )");
         }

        if (StringUtil.isNotEmpty(leaveAudit3LEntity.getIds())) {
            sb.append(" and RS.ID in (select NRID from TBL_YQNS_LEAVE_AUDIT_JD3L_GL where JDID in (" +leaveAudit3LEntity.getIds()).append("))");
        }

//        // 部门权限
//        if (StringUtils.isNotBlank(user.getDeptIds())) {
//            sb.append(" and RS.CREATE_USER IN ( SELECT STAFFID FROM TBL_USER_ORGRELATION WHERE DEPTID IN ("+user.getDeptIds()+"))");
//        }
//        sb.append(" OR RS.CREATE_USER = '"+user.getStaffid()+"'");
//        // 下发权限
//        sb.append(" OR RS.ID  IN (SELECT AUDITID  FROM TBL_YQNS_LEAVE_AUDIT_3L_XF WHERE  USERID  ='"+user.getStaffid()+"')");

        sb.append(" order by CREATE_TIME desc  ");
        addQuery(leaveAudit3LEntity, sb);
        //	sb.append(") T1 WHERE ROWNUM <= "+(pageInfo.getCurrentRecord() + pageInfo.getPageSize() )+" ) T2 WHERE T2.RN > "+ pageInfo.getCurrentRecord());
        return sb.toString();
    }
    
    public String selectListDraftPlan(LeaveAudit3LEntity leaveAudit3LEntity, TblStaffUtil user, BigDecimal jdid) throws Exception{
    	 StringBuffer sb = new StringBuffer();
         //	sb.append("SELECT * FROM (SELECT T1.* , ROWNUM RN FROM (");
         sb.append("SELECT * FROM TBL_YQNS_LEAVE_AUDIT_3L RS LEFT JOIN TBL_STAFF TS ON RS.CREATE_USER = TS.STAFFID WHERE 1=1 ");
         
         sb.append(" AND RS.ID NOT IN (SELECT GLID FROM TBL_YQNS_JHGL_JHCG_GL WHERE GLTYPE = '23')");
         
          if(jdid!=null) { 
         	 sb.append(" and RS.ID in (select NRID from TBL_YQNS_LEAVE_AUDIT_JD3L_GL where JDID="+jdid+" )");
          }
//         // 部门权限
//         if (StringUtils.isNotBlank(user.getDeptIds())) {
//             sb.append(" and RS.CREATE_USER IN ( SELECT STAFFID FROM TBL_USER_ORGRELATION WHERE DEPTID IN ("+user.getDeptIds()+"))");
//         }
//         sb.append(" OR RS.CREATE_USER = '"+user.getStaffid()+"'");
//         // 下发权限
//         sb.append(" OR RS.ID  IN (SELECT AUDITID  FROM TBL_YQNS_LEAVE_AUDIT_3L_XF WHERE  USERID  ='"+user.getStaffid()+"')");
          addQuery(leaveAudit3LEntity, sb);
         sb.append(" order by CREATE_TIME desc  ");
         
         //	sb.append(") T1 WHERE ROWNUM <= "+(pageInfo.getCurrentRecord() + pageInfo.getPageSize() )+" ) T2 WHERE T2.RN > "+ pageInfo.getCurrentRecord());
         return sb.toString();
    }

    public String selectDetailDistributeList(LeaveAudit3LEntity leaveAudit3LEntity, TblStaffUtil user) throws Exception{
    	StringBuffer sb = new StringBuffer();
        sb.append("SELECT * FROM TBL_YQNS_LEAVE_AUDIT_3L RS LEFT JOIN TBL_STAFF TS ON RS.CREATE_USER = TS.STAFFID WHERE 1 = 1 ")
        .append(" AND  (RS.DISFIRSTPERSON = ").append(user.getStaffid())
        .append("  or  RS.DISSECONDPERSON = ").append(user.getStaffid());
        if (StringUtils.isNotBlank(user.getDeptIds())) {
			 sb.append(" or  RS.CREATE_USER="+user.getStaffid()+"  or RS.CREATE_USER in (SELECT DISTINCT STAFFID FROM TBL_USER_ORGRELATION WHERE DEPTID IN ("+user.getDeptIds()+")  )");
	     }else {
	        sb.append(" or RS.CREATE_USER="+user.getStaffid() );
	     }
        sb.append(" )" );
        //RS.ID IN (SELECT NRID FROM TBL_YQNS_LEAVE_AUDIT_JD3L_GL WHERE JDID IN (SELECT JDID FROM TBL_YQNS_LEAVE_AUDIT_JD3L WHERE STATUS = 6) ) 
         addQuery(leaveAudit3LEntity, sb);
        sb.append(" order by RS.CREATE_TIME desc  ");
        return sb.toString();
    }
    
    public String selectDistributeReceiveList(LeaveAudit3LEntity vo, TblStaffUtil user) throws Exception{
    	StringBuffer sb = new StringBuffer();
        sb.append("SELECT * FROM TBL_YQNS_LEAVE_AUDIT_3L RS LEFT JOIN TBL_STAFF TS ON RS.CREATE_USER = TS.STAFFID WHERE ( RS.DISFIRSTPERSON =  ").append(user.getStaffid())
        .append(" OR RS.DISSECONDPERSON = ").append(user.getStaffid()).append(") ");
         addQuery(vo, sb);
        sb.append(" order by RS.CREATE_TIME desc  ");
        return sb.toString();
    }
    
    
    private void addQuery(LeaveAudit3LEntity leaveAudit3LEntity, StringBuffer sb) {
        if(StringUtil.isNotEmpty(leaveAudit3LEntity.getName())){
            sb.append(" AND RS.NAME LIKE '%"+ leaveAudit3LEntity.getName()+"%'");
        }

        if(StringUtil.isNotEmpty(leaveAudit3LEntity.getOldJob())){
            sb.append(" AND RS.OLD_JOB LIKE '%"+ leaveAudit3LEntity.getOldJob()+"%'");
        }

        if(leaveAudit3LEntity.getQuarterType() != null){
            sb.append(" AND RS.QUARTER_ID IN (SELECT ID FROM TBL_YQNS_QUARTER WHERE TYPE = "+leaveAudit3LEntity.getQuarterType()+")");
        }

        if(leaveAudit3LEntity.getQuarterNum() != null){
            sb.append(" AND RS.QUARTER_ID IN (SELECT ID FROM TBL_YQNS_QUARTER WHERE QUARTER = "+leaveAudit3LEntity.getQuarterNum()+")");
        }

        if(leaveAudit3LEntity.getQuarterId() != null){
            sb.append(" AND RS.QUARTER_ID = "+ leaveAudit3LEntity.getQuarterId());
        }
    }

    public String selectCountByEntity(LeaveAudit3LEntity leaveAudit3LEntity){
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT COUNT(0) FROM (");
        sb.append("SELECT * FROM TBL_YQNS_LEAVE_AUDIT_3L RS LEFT JOIN TBL_STAFF TS ON RS.CREATE_USER = TS.STAFFID WHERE 1=1 ");
        addQuery(leaveAudit3LEntity, sb);
        sb.append(")");
        return sb.toString();
    }

    public String updateEntity(LeaveAudit3LEntity leaveAudit3LEntity){
        StringBuffer sb = new StringBuffer();
        sb.append("UPDATE TBL_YQNS_LEAVE_AUDIT_3L SET ");
        sb.append("NAME = '"+leaveAudit3LEntity.getName()+"'");

        if(StringUtil.isNotEmpty(leaveAudit3LEntity.getOldJob())){
            sb.append(", OLD_JOB = '"+leaveAudit3LEntity.getOldJob()+"'");
        }

        if(StringUtil.isNotEmpty(leaveAudit3LEntity.getJob())){
            sb.append(", JOB = '"+leaveAudit3LEntity.getJob()+"'");
        }

        if(StringUtil.isNotEmpty(leaveAudit3LEntity.getOldLevel())){
            sb.append(", OLD_LEVEL = '"+leaveAudit3LEntity.getOldLevel()+"'");
        }

        if(StringUtil.isNotEmpty(leaveAudit3LEntity.getRemarks())){
            sb.append(", REMARKS = '"+leaveAudit3LEntity.getRemarks()+"'");
        }
        
        if(leaveAudit3LEntity.getUnitCount() != null){
            sb.append(", UNITCOUNT = "+leaveAudit3LEntity.getUnitCount());
        }

        if(StringUtil.isNotEmpty(  leaveAudit3LEntity.getOldOrgId() )){
            sb.append(", OLD_ORG_ID = "+leaveAudit3LEntity.getOldOrgId());
        }

        if(leaveAudit3LEntity.getOldJobStartTime() != null){
        	sb.append(", OLD_JOB_START_TIME = TO_DATE('"+ DateUtil.format(leaveAudit3LEntity.getOldJobStartTime(),"yyyy-MM-dd")+"', 'YYYY-MM-DD')");
        }

        if(leaveAudit3LEntity.getOldJobEndTime() != null){
            sb.append(", OLD_JOB_END_TIME = TO_DATE('"+ DateUtil.format(leaveAudit3LEntity.getOldJobEndTime(),"yyyy-MM-dd")+"', 'YYYY-MM-DD')");
        }

        if(StringUtil.isNotEmpty(  leaveAudit3LEntity.getLevels() )){
            sb.append(", LEVELS = '"+leaveAudit3LEntity.getLevels()+"'");
        }

        if(StringUtil.isNotEmpty(  leaveAudit3LEntity.getOrgId() )){
            sb.append(", ORG_ID = "+leaveAudit3LEntity.getOrgId());
        }
        if(leaveAudit3LEntity.getJobStartTime() != null){
            sb.append(", JOB_START_TIME = TO_DATE('"+ DateUtil.format(leaveAudit3LEntity.getJobStartTime(),"yyyy-MM-dd")+"', 'YYYY-MM-DD')");
        }

        if(leaveAudit3LEntity.getJobEndTime() != null){
            sb.append(", JOB_END_TIME = TO_DATE('"+ DateUtil.format(leaveAudit3LEntity.getJobEndTime(),"yyyy-MM-dd")+"', 'YYYY-MM-DD')");
        }


        if(StringUtil.isNotEmpty(leaveAudit3LEntity.getDeptType())){
            sb.append(", DEPT_TYPE = '"+leaveAudit3LEntity.getDeptType()+"'");
        }

        if(StringUtil.isNotEmpty(leaveAudit3LEntity.getDeptHistory())){
            sb.append(", DEPT_HISTORY = '"+leaveAudit3LEntity.getDeptHistory()+"'");
        }
 
        if(StringUtil.isNotEmpty(leaveAudit3LEntity.getMainDuty())){
            sb.append(", MAIN_DUTY = '"+leaveAudit3LEntity.getMainDuty()+"'");
        }

        if(StringUtil.isNotEmpty(leaveAudit3LEntity.getMainPower())){
            sb.append(", MAIN_POWER = '"+leaveAudit3LEntity.getMainPower()+"'");
        }

        if(StringUtil.isNotEmpty(leaveAudit3LEntity.getBusiness())){
            sb.append(", BUSINESS = '"+leaveAudit3LEntity.getBusiness()+"'");
        }

        if(StringUtil.isNotEmpty(leaveAudit3LEntity.getPerson())){
            sb.append(", PERSON = '"+leaveAudit3LEntity.getPerson()+"'");
        }

        if(leaveAudit3LEntity.getIsSeparateAccount() != null){
            sb.append(", IS_SEPARATE_ACCOUNT = "+leaveAudit3LEntity.getIsSeparateAccount());
        }

        if(StringUtil.isNotEmpty(leaveAudit3LEntity.getAssetInfo())){
            sb.append(", ASSET_INFO = '"+leaveAudit3LEntity.getAssetInfo()+"'");
        }

        if(leaveAudit3LEntity.getMainCost() != null ){
            sb.append(", MAIN_COST = '"+leaveAudit3LEntity.getMainCost()+"'");
        }

        if(leaveAudit3LEntity.getIncome() != null){
            sb.append(", INCOME = "+leaveAudit3LEntity.getIncome());
        }

//        if(leaveAudit3LEntity.getQuarterId() != null){
//            sb.append(", QUARTER_ID = "+leaveAudit3LEntity.getQuarterId());
//        }

        if(leaveAudit3LEntity.getCost() != null){
            sb.append(", COST = "+leaveAudit3LEntity.getCost());
        }

        if(leaveAudit3LEntity.getControllableCost() != null){
            sb.append(", CONTROLLABLE_COST = "+leaveAudit3LEntity.getControllableCost());
        }

        if(leaveAudit3LEntity.getHasExternalPerson() != null){
            sb.append(", HAS_EXTERNAL_PERSON = "+leaveAudit3LEntity.getHasExternalPerson());
        }

        if(StringUtil.isNotEmpty(leaveAudit3LEntity.getUnconventJob())){
            sb.append(", UNCONVENT_JOB = '"+leaveAudit3LEntity.getUnconventJob()+"'");
        }

        
        if(leaveAudit3LEntity.getIsAudit() != null){
            sb.append(", IS_AUDIT = "+leaveAudit3LEntity.getIsAudit());
        }
        
        if(leaveAudit3LEntity.getZsstaffid() != null){
            sb.append(", ZSSTAFFID = "+leaveAudit3LEntity.getZsstaffid());
        }
        
        if(StringUtil.isNotEmpty(leaveAudit3LEntity.getZsname())){
            sb.append(", ZSNAME = '"+leaveAudit3LEntity.getZsname()+"'");
        }
        
        if(leaveAudit3LEntity.getZzstaffids() != null){
            sb.append(", ZZSTAFFIDS = "+leaveAudit3LEntity.getZzstaffids());
        }
        
        if(StringUtil.isNotEmpty(leaveAudit3LEntity.getZznames())){
            sb.append(", ZZNAMES = '"+leaveAudit3LEntity.getZznames()+"'");
        }
        
        if(leaveAudit3LEntity.getFzstaffids() != null){
            sb.append(", FZSTAFFIDS = "+leaveAudit3LEntity.getFzstaffids());
        }
        
        if(StringUtil.isNotEmpty(leaveAudit3LEntity.getFznames())){
            sb.append(", FZNAMES = '"+leaveAudit3LEntity.getFznames()+"'");
        }

        if(StringUtil.isNotEmpty(leaveAudit3LEntity.getFzstaffid())){
            sb.append(", FZSTAFFID = '"+leaveAudit3LEntity.getFzstaffid()+"'");
        }

        
        if(StringUtil.isNotEmpty(leaveAudit3LEntity.getFzname())){
            sb.append(", FZNAME = '"+leaveAudit3LEntity.getFzname()+"'");
        }

        if(leaveAudit3LEntity.getRsyq() != null){
            sb.append(", RSYQ = "+leaveAudit3LEntity.getRsyq());
        }
        
        if(leaveAudit3LEntity.getXcsrarttime() != null){
            sb.append(", XCSRARTTIME = TO_DATE('"+ DateUtil.format(leaveAudit3LEntity.getXcsrarttime(),"yyyy-MM-dd")+"', 'YYYY-MM-DD')");
        }

        if(leaveAudit3LEntity.getXcendtime() != null){
            sb.append(", XCENDTIME = TO_DATE('"+ DateUtil.format(leaveAudit3LEntity.getXcendtime(),"yyyy-MM-dd")+"', 'YYYY-MM-DD')");
        }
        
        if(StringUtil.isNotEmpty(leaveAudit3LEntity.getProjectname())){
            sb.append(", PROJECTNAME = '"+leaveAudit3LEntity.getProjectname()+"'");
        }

        sb.append(" WHERE ID = '"+leaveAudit3LEntity.getId()+"'");

        return sb.toString();
    }

    public String insertEntity(LeaveAudit3LEntity leaveAudit3LEntity){
        StringBuffer colSb = new StringBuffer();
        colSb.append("INSERT INTO TBL_YQNS_LEAVE_AUDIT_3L (ID");

        StringBuffer valSb = new StringBuffer();
        valSb.append(" VALUES (HIBERNATE_SEQUENCE.nextval");

        if(StringUtil.isNotEmpty(leaveAudit3LEntity.getName())){
            colSb.append(", NAME");
            valSb.append(", '" + leaveAudit3LEntity.getName() + "'");
        }

        if(StringUtil.isNotEmpty(leaveAudit3LEntity.getOldJob())){
            colSb.append(", OLD_JOB");
            valSb.append(", '" + leaveAudit3LEntity.getOldJob() + "'");
        }

        if(StringUtil.isNotEmpty(leaveAudit3LEntity.getOldLevel())){
            colSb.append(", OLD_LEVEL");
            valSb.append(", '" + leaveAudit3LEntity.getOldLevel() + "'");
        }




        if(StringUtil.isNotEmpty(  leaveAudit3LEntity.getOldOrgId() )){
            colSb.append(", OLD_ORG_ID");
            valSb.append(", " + leaveAudit3LEntity.getOldOrgId());
        }

        if(leaveAudit3LEntity.getOldJobStartTime() != null){
            colSb.append(", OLD_JOB_START_TIME");
            valSb.append(", TO_DATE('"+ DateUtil.format(leaveAudit3LEntity.getOldJobStartTime(),"yyyy-MM-dd")+"', 'YYYY-MM-DD')");
        }

        if(leaveAudit3LEntity.getOldJobEndTime() != null){
            colSb.append(", OLD_JOB_END_TIME");
            valSb.append(", TO_DATE('"+ DateUtil.format(leaveAudit3LEntity.getOldJobEndTime(),"yyyy-MM-dd")+"', 'YYYY-MM-DD')");
        }


        if(StringUtil.isNotEmpty(leaveAudit3LEntity.getJob())){
            colSb.append(", JOB");
            valSb.append(", '" + leaveAudit3LEntity.getJob() + "'");
        }

        if(StringUtil.isNotEmpty(leaveAudit3LEntity.getRemarks())){
        	colSb.append(", REMARKS");
            valSb.append(", '" + leaveAudit3LEntity.getRemarks() + "'");
        }
        
        if(leaveAudit3LEntity.getUnitCount() != null){
        	colSb.append(", UNITCOUNT");
            valSb.append(", " + leaveAudit3LEntity.getUnitCount() );
        }

        if(StringUtil.isNotEmpty(leaveAudit3LEntity.getLevels())){
            colSb.append(", LEVELS");
            valSb.append(", '" + leaveAudit3LEntity.getLevels() + "'");
        }


        if(StringUtil.isNotEmpty(  leaveAudit3LEntity.getOrgId() )){
            colSb.append(", ORG_ID");
            valSb.append(", " + leaveAudit3LEntity.getOrgId());
        }

        if(leaveAudit3LEntity.getJobStartTime() != null){
            colSb.append(", JOB_START_TIME");
            valSb.append(", TO_DATE('"+ DateUtil.format(leaveAudit3LEntity.getJobStartTime(),"yyyy-MM-dd")+"', 'YYYY-MM-DD')");
        }

        if(leaveAudit3LEntity.getJobEndTime() != null){
            colSb.append(", JOB_END_TIME");
            valSb.append(", TO_DATE('"+ DateUtil.format(leaveAudit3LEntity.getJobEndTime(),"yyyy-MM-dd")+"', 'YYYY-MM-DD')");
        }



        if(StringUtil.isNotEmpty(leaveAudit3LEntity.getDeptType())){
            colSb.append(", DEPT_TYPE");
            valSb.append(", '" + leaveAudit3LEntity.getDeptType() + "'");
        }

        if(StringUtil.isNotEmpty(leaveAudit3LEntity.getDeptHistory())){
            colSb.append(", DEPT_HISTORY");
            valSb.append(", '" + leaveAudit3LEntity.getDeptHistory() + "'");
        }

        if(StringUtil.isNotEmpty(leaveAudit3LEntity.getMainDuty())){
            colSb.append(", MAIN_DUTY");
            valSb.append(", '" + leaveAudit3LEntity.getMainDuty() + "'");
        }

        if(StringUtil.isNotEmpty(leaveAudit3LEntity.getMainPower())){
            colSb.append(", MAIN_POWER");
            valSb.append(", '" + leaveAudit3LEntity.getMainPower() + "'");
        }

        if(StringUtil.isNotEmpty(leaveAudit3LEntity.getBusiness())){
            colSb.append(", BUSINESS");
            valSb.append(", '" + leaveAudit3LEntity.getBusiness() + "'");
        }

        if(StringUtil.isNotEmpty(leaveAudit3LEntity.getPerson())){
            colSb.append(", PERSON");
            valSb.append(", '" + leaveAudit3LEntity.getPerson() + "'");
        }

        if(leaveAudit3LEntity.getIsSeparateAccount() != null){
            colSb.append(", IS_SEPARATE_ACCOUNT");
            valSb.append(", " + leaveAudit3LEntity.getIsSeparateAccount() );
        }

        if(StringUtil.isNotEmpty(leaveAudit3LEntity.getAssetInfo())){
            colSb.append(", ASSET_INFO");
            valSb.append(", '" + leaveAudit3LEntity.getAssetInfo() + "'");
        }

        if(StringUtil.isNotEmpty(leaveAudit3LEntity.getMainCost() )){
            colSb.append(", MAIN_COST");
            valSb.append(", '" + leaveAudit3LEntity.getMainCost()+"'");
        }

        if(leaveAudit3LEntity.getIncome() != null){
            colSb.append(", INCOME");
            valSb.append(", " + leaveAudit3LEntity.getIncome());
        }

//        if(leaveAudit3LEntity.getQuarterId() != null){
//            colSb.append(", QUARTER_ID");
//            valSb.append(", " + leaveAudit3LEntity.getQuarterId());
//        }

        if(leaveAudit3LEntity.getCost() != null){
            colSb.append(", COST");
            valSb.append(", " + leaveAudit3LEntity.getCost());
        }

        if(leaveAudit3LEntity.getControllableCost() != null){
            colSb.append(", CONTROLLABLE_COST");
            valSb.append(", " + leaveAudit3LEntity.getControllableCost());
        }

        if(leaveAudit3LEntity.getHasExternalPerson() != null){
            colSb.append(", HAS_EXTERNAL_PERSON");
            valSb.append(", " + leaveAudit3LEntity.getHasExternalPerson());
        }

        if(StringUtil.isNotEmpty(leaveAudit3LEntity.getUnconventJob())){
            colSb.append(", UNCONVENT_JOB");
            valSb.append(", '" + leaveAudit3LEntity.getUnconventJob() + "'");
        }

        if(leaveAudit3LEntity.getIsAudit() != null){
            colSb.append(", IS_AUDIT");
            valSb.append(", " + leaveAudit3LEntity.getIsAudit());
        }

        if(StringUtil.isNotEmpty(leaveAudit3LEntity.getProjectname())){
            colSb.append(", PROJECTNAME");
            valSb.append(", '" + leaveAudit3LEntity.getProjectname() + "'");
        }

        if(leaveAudit3LEntity.getCreateUser() != null){
            colSb.append(", CREATE_USER");
            valSb.append(", '" + leaveAudit3LEntity.getCreateUser().getStaffid() + "'");
        }
        colSb.append(", CREATE_TIME)");
        valSb.append(", TO_DATE('"+ DateUtil.format(new Date(),"yyyy-MM-dd")+"', 'YYYY-MM-DD'))");

        colSb.append(valSb);
        return colSb.toString();
    }

    public String deleteByIds(String ids){
        StringBuffer sb = new StringBuffer();
        sb.append("DELETE FROM TBL_YQNS_LEAVE_AUDIT_3L WHERE ID IN (" + ids+")");
        return sb.toString();
    }

    public String findByIds(String ids){
        StringBuffer sb = new StringBuffer();
        sb.append("select * from   TBL_YQNS_LEAVE_AUDIT_3L WHERE ID IN (" + ids+")");
        return sb.toString();
    }
}
