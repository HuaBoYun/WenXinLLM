package com.huabo.audit.oracle.mapper;

import cn.hutool.core.date.DateUtil;
import com.hbfk.util.StringUtil;
import com.huabo.audit.oracle.entity.InterimAuditDetailEntity;
import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Rui
 * @ClassName InterimAuditDetailMapperSqlConfig
 * @Description
 * @DATE 2024/04/15
 */
public class InterimAuditDetailMapperSqlConfig {
	
	public String findListByAnalysis(Integer xmnd, String projectName) throws Exception {
		StringBuffer sb = new StringBuffer();
        sb.append("SELECT * FROM TBL_YQNS_INTERIM_AUDIT_DETAILS RS LEFT JOIN TBL_STAFF TS ON RS.CREATE_USER = TS.STAFFID WHERE ");
        
        sb.append(" RS.ID IN (SELECT GLID FROM TBL_YQNS_JHGL_JH_GL WHERE JHID IN (SELECT JHID FROM TBL_YQNS_JHGL_JH WHERE XMND = "+xmnd+") AND GLTYPE = '22')");
        sb.append(" AND RS.ID NOT IN (SELECT GLJHXMID FROM TBL_YQNS_XMQD WHERE PLANID IN (SELECT JHID FROM TBL_YQNS_JHGL_JH WHERE XMND = "+xmnd+") AND GLJHXMLX = '22')");
        
        if(StringUtil.isNotEmpty(projectName)){
            sb.append(" AND RS.PROJECT_NAME LIKE '%"+projectName+"%'");
        }
        
        sb.append(" ORDER BY RS.ID DESC ");
        return sb.toString();
	}
	
	public String getListDraftPlan(InterimAuditDetailEntity interimAuditDetailEntity, Integer sourceType, BigDecimal jhid) throws Exception {
		StringBuffer sb = new StringBuffer();
        sb.append("SELECT * FROM TBL_YQNS_INTERIM_AUDIT_DETAILS RS LEFT JOIN TBL_STAFF TS ON RS.CREATE_USER = TS.STAFFID WHERE 1 = 1 ");
        if(sourceType == 2) {
        	sb.append(" AND RS.ID NOT IN (SELECT GLID FROM TBL_YQNS_JHGL_JHCHUG_GL WHERE GLTYPE = '22') AND RS.ID IN (SELECT GLID FROM TBL_YQNS_JHGL_JHCG_GL WHERE GLTYPE = '22' AND JHCGID = "+jhid+" )");
        }else if(sourceType == 3) {
        	sb.append(" AND RS.ID NOT IN (SELECT GLID FROM TBL_YQNS_JHGL_JH_GL WHERE GLTYPE = '22') AND RS.ID IN (SELECT GLID FROM TBL_YQNS_JHGL_JHCHUG_GL WHERE GLTYPE  = '22' AND JHCHUGID = "+jhid+" )");
        }else {
        	sb.append(" AND RS.ID NOT IN (SELECT GLID FROM TBL_YQNS_JHGL_JHCG_GL WHERE GLTYPE = '22')");	
        }
        
        if(interimAuditDetailEntity.getOrg() != null && StringUtil.isNotEmpty(interimAuditDetailEntity.getOrg().getOrgname())){
            sb.append(" AND RS.ORGID IN (SELECT ORGID FROM TBL_ORGANIZATION WHERE ORGNAME LIKE '%"+interimAuditDetailEntity.getOrg().getOrgname()+"%')");
        }

        if(StringUtil.isNotEmpty(interimAuditDetailEntity.getProjectName())){
            sb.append(" AND RS.PROJECT_NAME LIKE '%"+interimAuditDetailEntity.getProjectName()+"%'");
        }

        if(StringUtil.isNotEmpty(interimAuditDetailEntity.getCreateyear())){
            sb.append(" and RS.CREATE_TIME>'"+interimAuditDetailEntity.getCreateyear()+"-01-01' and RS.CREATE_TIME<'"+interimAuditDetailEntity.getCreateyear()+"-12-31'");
        }

        if(interimAuditDetailEntity.getTeamLeaderId() != null && StringUtil.isNotEmpty(interimAuditDetailEntity.getTeamLeaderId())){
            sb.append(" AND RS.TEAM_LEADER_ID LIKE '%"+interimAuditDetailEntity.getTeamLeaderId()+"%'");
        }

        if(StringUtils.isNotBlank(interimAuditDetailEntity.getProjectType())) {
        	sb.append(" AND RS.PROJECTTYPE = '"+interimAuditDetailEntity.getProjectType()+"'");
        }
        
        if(interimAuditDetailEntity.getTbid()!=null){
            sb.append(" and RS.ID in (select DEID from TBL_YQNS_RZTB_GL where TBID="+interimAuditDetailEntity.getTbid()+") ");
        }
        
        if(interimAuditDetailEntity.getStatus() != null) {
        	sb.append(" and RS.ID in (SELECT DEID FROM TBL_YQNS_RZTB_GL WHERE TBID IN (SELECT TBID FROM TBL_YQNS_RZTB WHERE STATUS = "+interimAuditDetailEntity.getStatus()+" ))");
        }
        
        
        sb.append(" ORDER BY RS.ID DESC ");
        return sb.toString();
	}
	

    public String selectByEntity( InterimAuditDetailEntity interimAuditDetailEntity){
        StringBuffer sb = new StringBuffer();
        //	sb.append("SELECT * FROM (SELECT T1.* , ROWNUM RN FROM (");
        sb.append("SELECT * FROM TBL_YQNS_INTERIM_AUDIT_DETAILS RS LEFT JOIN TBL_STAFF TS ON RS.CREATE_USER = TS.STAFFID WHERE 1=1 ");
        if(interimAuditDetailEntity.getOrg() != null && StringUtil.isNotEmpty(interimAuditDetailEntity.getOrg().getOrgname())){
            sb.append(" AND RS.ORGID IN (SELECT ORGID FROM TBL_ORGANIZATION WHERE ORGNAME LIKE '%"+interimAuditDetailEntity.getOrg().getOrgname()+"%')");
        }


        if(StringUtil.isNotEmpty(interimAuditDetailEntity.getProjectName())){
            sb.append(" AND RS.PROJECT_NAME LIKE '%"+interimAuditDetailEntity.getProjectName()+"%'");
        }

        if(StringUtil.isNotEmpty(interimAuditDetailEntity.getCreateyear())){
            sb.append(" and RS.CREATE_TIME>'"+interimAuditDetailEntity.getCreateyear()+"-01-01' and RS.CREATE_TIME<'"+interimAuditDetailEntity.getCreateyear()+"-12-31'");
        }

        if(interimAuditDetailEntity.getTeamLeaderId() != null && StringUtil.isNotEmpty(interimAuditDetailEntity.getTeamLeaderId())){
            sb.append(" AND RS.TEAM_LEADER_ID LIKE '%"+interimAuditDetailEntity.getTeamLeaderId()+"%'");
        }

        //用户创建只能看见自己的。部门负责人要查看全部内容；下发人员可以查询下发给自己的
//        sb.append(" AND (RS.CREATE_USER = "+ interimAuditDetailEntity.getCreateUserId() );
//        sb.append(" OR INSTR (',' || PERSON_IDS || ',',',"+interimAuditDetailEntity.getCreateUserId()+",') > 0" );
//        if(StringUtils.isNotBlank(interimAuditDetailEntity.getQueryDeptIds())) {
//            sb.append(" OR RS.CREATE_USER IN (SELECT STAFFID FROM TBL_USER_ORGRELATION WHERE DEPTID IN (").append(interimAuditDetailEntity.getQueryDeptIds()).append("))");
//        }
//        sb.append(") ");
        
        
        if(interimAuditDetailEntity.getTbid()!=null){
            sb.append(" and RS.ID in (select DEID from TBL_YQNS_RZTB_GL where TBID="+interimAuditDetailEntity.getTbid()+") ");
        }

        if(StringUtil.isNotEmpty(interimAuditDetailEntity.getIds())){
            sb.append(" and RS.ID in (select DEID from TBL_YQNS_RZTB_GL where TBID  IN ("+interimAuditDetailEntity.getIds()+") ) ");
        }

        sb.append(" ORDER BY RS.ID DESC ");
        System.out.println(sb.toString());
        //	sb.append(") T1 WHERE ROWNUM <= "+(pageInfo.getCurrentRecord() + pageInfo.getPageSize() )+" ) T2 WHERE T2.RN > "+ pageInfo.getCurrentRecord());
        return sb.toString();
    }


    public String updateEntity(InterimAuditDetailEntity interimAuditDetailEntity){
        StringBuffer sb = new StringBuffer();
        sb.append("UPDATE TBL_YQNS_INTERIM_AUDIT_DETAILS SET ");
        sb.append("ORGID = '"+interimAuditDetailEntity.getOrgId()+"'");

        if(StringUtil.isNotEmpty(interimAuditDetailEntity.getAuditInfo())){
            sb.append(", AUDIT_INFO = '"+interimAuditDetailEntity.getAuditInfo()+"'");
        }

        if(interimAuditDetailEntity.getAuditTime() != null){
//            sb.append(", AUDIT_TIME = '"+DateUtil.format(interimAuditDetailEntity.getAuditTime(),"yyyy-MM-dd")+"'");
        	sb.append(", AUDIT_TIME = '"+interimAuditDetailEntity.getAuditTime()+"'");
        }

        if(interimAuditDetailEntity.getUnauditMonth() != null){
            sb.append(", UNAUDIT_MONTH = '"+interimAuditDetailEntity.getUnauditMonth()+"'");
        }

        if(interimAuditDetailEntity.getUnauditYear() != null){
            sb.append(", UNAUDIT_YEAR = '"+interimAuditDetailEntity.getUnauditYear()+"'");
        }

        if(StringUtil.isNotEmpty(interimAuditDetailEntity.getProjectName())){
            sb.append(", PROJECT_NAME = '"+interimAuditDetailEntity.getProjectName()+"'");
        }

        if(interimAuditDetailEntity.getWorkStartTime() != null){
//            sb.append(", WORK_START_TIME = '"+DateUtil.format(interimAuditDetailEntity.getWorkStartTime(),"yyyy-MM-dd")+"'");
        	sb.append(", WORK_START_TIME = '"+interimAuditDetailEntity.getWorkStartTime()+"'");
        }

        if(interimAuditDetailEntity.getWorkEndTime() != null){
//            sb.append(", WORK_END_TIME = '"+DateUtil.format(interimAuditDetailEntity.getWorkEndTime(),"yyyy-MM-dd")+"'");
        	sb.append(", WORK_END_TIME = '"+interimAuditDetailEntity.getWorkEndTime()+"'");
        }

        if(interimAuditDetailEntity.getDoAuditTime() != null){
//            sb.append(", DO_AUDIT_TIME = '"+DateUtil.format(interimAuditDetailEntity.getDoAuditTime(),"yyyy-MM-dd")+"'");
        	sb.append(", DO_AUDIT_TIME = '"+interimAuditDetailEntity.getDoAuditTime()+"'");
        }

        if(StringUtil.isNotEmpty(  interimAuditDetailEntity.getSubTeamLeaderId() )){
            sb.append(", SUB_TEAM_LEADER_ID = '"+interimAuditDetailEntity.getSubTeamLeaderId()+"'");
        }

        if(StringUtil.isNotEmpty(  interimAuditDetailEntity.getTeamLeaderId() )){
            sb.append(", TEAM_LEADER_ID = '"+interimAuditDetailEntity.getTeamLeaderId()+"'");
        }

        if(StringUtil.isNotEmpty(interimAuditDetailEntity.getPersonIds())){
            sb.append(", PERSON_IDS = '"+interimAuditDetailEntity.getPersonIds()+"'");
        }

        if(StringUtil.isNotEmpty(  interimAuditDetailEntity.getLeaderId() )){
            sb.append(", LEADER_ID = '"+interimAuditDetailEntity.getLeaderId()+"'");
        }

        if(StringUtil.isNotEmpty(  interimAuditDetailEntity.getChiefReviewerId() )){
            sb.append(", CHIEF_REVIEWER_ID = '"+interimAuditDetailEntity.getChiefReviewerId()+"'");
        }
        if(StringUtil.isNotEmpty(  interimAuditDetailEntity.getProjectType() )){
            sb.append(", PROJECTTYPE = '"+interimAuditDetailEntity.getProjectType()+"'");
        }

        if(StringUtil.isNotEmpty(  interimAuditDetailEntity.getTextarea() )){
            sb.append(", TEXTAREA = '"+interimAuditDetailEntity.getTextarea()+"'");
        }

        if(StringUtil.isNotEmpty(  interimAuditDetailEntity.getDeputyReviewerId() )){
            sb.append(", DEPUTY_REVIEWER_ID = '"+interimAuditDetailEntity.getDeputyReviewerId()+"'");
        }
        if(StringUtil.isNotEmpty(  interimAuditDetailEntity.getCreateyear() )){
            sb.append(", CREATEYEAR = '"+interimAuditDetailEntity.getCreateyear()+"'");
        }
        if(StringUtil.isNotEmpty(  interimAuditDetailEntity.getProjectlx() )){
            sb.append(", PROJECTLX = '"+interimAuditDetailEntity.getProjectlx()+"'");
        }
        
        if(StringUtil.isNotEmpty(  interimAuditDetailEntity.getRemarks() )){
            sb.append(", REMARKS = '"+interimAuditDetailEntity.getRemarks()+"'");
        }


        if(StringUtil.isNotEmpty(interimAuditDetailEntity.getLdname())){
            sb.append(", LDNAME = '"+interimAuditDetailEntity.getLdname()+"'");
        }

        if(StringUtil.isNotEmpty(interimAuditDetailEntity.getLdzw())){
            sb.append(", LDZW = '"+interimAuditDetailEntity.getLdzw()+"'");
        }

        if(StringUtil.isNotEmpty(interimAuditDetailEntity.getCsym())){
            sb.append(", CSYM = '"+interimAuditDetailEntity.getCsym()+"'");
        }

        if(StringUtil.isNotEmpty(interimAuditDetailEntity.getYjexsj())){
            sb.append(", YJEXSJ = '"+interimAuditDetailEntity.getYjexsj()+"'");
        }

        if(StringUtil.isNotEmpty(interimAuditDetailEntity.getZjprojectname())){
            sb.append(", ZJPROJECTNAME = '"+interimAuditDetailEntity.getZjprojectname()+"'");
        }
        
        if(StringUtil.isNotEmpty(interimAuditDetailEntity.getRzsj())){
            sb.append(", RZSJ = '"+interimAuditDetailEntity.getRzsj()+"'");
        }
        
        if(StringUtil.isNotEmpty(interimAuditDetailEntity.getSjfwstarttime())){
            sb.append(", SJFWSTARTTIME = '"+interimAuditDetailEntity.getSjfwstarttime()+"'");
        }
        
        if(StringUtil.isNotEmpty(interimAuditDetailEntity.getSjfwendtime())){
            sb.append(", SJFWENDTIME = '"+interimAuditDetailEntity.getSjfwendtime()+"'");
        }

        sb.append(" WHERE ID = '"+interimAuditDetailEntity.getId()+"'");

        return sb.toString();
    }

    public String insertEntity(InterimAuditDetailEntity interimAuditDetailEntity){
        StringBuffer colSb = new StringBuffer();
        colSb.append("INSERT INTO TBL_YQNS_INTERIM_AUDIT_DETAILS (ID");

        StringBuffer valSb = new StringBuffer();
        valSb.append(" VALUES (HIBERNATE_SEQUENCE.nextval");

        if(interimAuditDetailEntity.getOrgId() != null){
            colSb.append(", ORGID");
            valSb.append(", '"+ interimAuditDetailEntity.getOrgId()+"'");
        }

        if(interimAuditDetailEntity.getSerialNumber() != null){
            colSb.append(", SERIALNUMBER");
            valSb.append(", '"+ interimAuditDetailEntity.getSerialNumber()+"'");
        }

        if(StringUtil.isNotEmpty(interimAuditDetailEntity.getAuditInfo())){
            colSb.append(", AUDIT_INFO");
            valSb.append(", '"+ interimAuditDetailEntity.getAuditInfo()+"'");
        }

        if(interimAuditDetailEntity.getUnauditMonth() != null){
            colSb.append(", UNAUDIT_MONTH");
            valSb.append(", "+ interimAuditDetailEntity.getUnauditMonth());
        }

        if(interimAuditDetailEntity.getUnauditYear() != null){
            colSb.append(", UNAUDIT_YEAR");
            valSb.append(", '"+ interimAuditDetailEntity.getUnauditYear()+"'");
        }

        if(interimAuditDetailEntity.getAuditTime() != null){
            colSb.append(", AUDIT_TIME");
//            valSb.append(", '" + DateUtil.format(interimAuditDetailEntity.getAuditTime(),"yyyy-MM-dd") + "'" );
            valSb.append(", '" + interimAuditDetailEntity.getAuditTime()+ "'" );
        }

        if(StringUtil.isNotEmpty(interimAuditDetailEntity.getProjectName())){
            colSb.append(", PROJECT_NAME");
            valSb.append(", '"+ interimAuditDetailEntity.getProjectName()+"'");
        }

        if(interimAuditDetailEntity.getWorkStartTime() != null){
            colSb.append(", WORK_START_TIME");
//            valSb.append(", '" + DateUtil.format(interimAuditDetailEntity.getWorkStartTime(),"yyyy-MM-dd") + "'" );
            valSb.append(", '" + interimAuditDetailEntity.getWorkStartTime() + "'" );
        }

        if(interimAuditDetailEntity.getWorkEndTime() != null){
            colSb.append(", WORK_END_TIME");
//            valSb.append(", '" + DateUtil.format(interimAuditDetailEntity.getWorkEndTime(),"yyyy-MM-dd") + "'" );
            valSb.append(", '" + interimAuditDetailEntity.getWorkEndTime() + "'" );
        }

        if(interimAuditDetailEntity.getDoAuditTime() != null){
            colSb.append(", DO_AUDIT_TIME");
//            valSb.append(", '" + DateUtil.format(interimAuditDetailEntity.getDoAuditTime(),"yyyy-MM-dd") + "'" );
            valSb.append(", '" + interimAuditDetailEntity.getDoAuditTime() + "'" );
        }

        if(StringUtil.isNotEmpty(  interimAuditDetailEntity.getTeamLeaderId() )){
            colSb.append(", TEAM_LEADER_ID");
            valSb.append(", '"+ interimAuditDetailEntity.getTeamLeaderId()+"'");
        }

        if(StringUtil.isNotEmpty(  interimAuditDetailEntity.getSubTeamLeaderId() )){
            colSb.append(", SUB_TEAM_LEADER_ID");
            valSb.append(", '"+ interimAuditDetailEntity.getSubTeamLeaderId()+"'");
        }

        if(StringUtil.isNotEmpty(  interimAuditDetailEntity.getLeaderId() )){
            colSb.append(", LEADER_ID");
            valSb.append(", '"+ interimAuditDetailEntity.getLeaderId()+"'");
        }

        if(StringUtil.isNotEmpty(  interimAuditDetailEntity.getChiefReviewerId() )){
            colSb.append(", CHIEF_REVIEWER_ID");
            valSb.append(", '"+ interimAuditDetailEntity.getChiefReviewerId()+"'");
        }

        if(StringUtil.isNotEmpty(  interimAuditDetailEntity.getDeputyReviewerId() )){
            colSb.append(", DEPUTY_REVIEWER_ID");
            valSb.append(", '"+ interimAuditDetailEntity.getDeputyReviewerId()+"'");
        }

        if(StringUtil.isNotEmpty(  interimAuditDetailEntity.getProjectType() )){
            colSb.append(", PROJECTTYPE");
            valSb.append(", '"+ interimAuditDetailEntity.getProjectType()+"'");
        }

        if(StringUtil.isNotEmpty(  interimAuditDetailEntity.getTextarea() )){
            colSb.append(", TEXTAREA");
            valSb.append(", '"+ interimAuditDetailEntity.getTextarea()+"'");
        }

        if(StringUtil.isNotEmpty(  interimAuditDetailEntity.getCreateyear() )){
            colSb.append(", CREATEYEAR");
            valSb.append(", '"+ interimAuditDetailEntity.getCreateyear()+"'");
        }


        if(interimAuditDetailEntity.getCreateUser() != null){
            colSb.append(", CREATE_USER");
            valSb.append(", '" + interimAuditDetailEntity.getCreateUser().getStaffid() + "'");
        }
        if(StringUtil.isNotEmpty(  interimAuditDetailEntity.getProjectlx() )){
            colSb.append(", PROJECTLX");
            valSb.append(", '"+ interimAuditDetailEntity.getProjectlx()+"'");
        }

        if(StringUtil.isNotEmpty(  interimAuditDetailEntity.getRemarks() )){
            colSb.append(", REMARKS");
            valSb.append(", '"+ interimAuditDetailEntity.getRemarks()+"'");
        }
        
        
        if(StringUtil.isNotEmpty(  interimAuditDetailEntity.getLdname() )){
            colSb.append(", LDNAME");
            valSb.append(", '"+ interimAuditDetailEntity.getLdname()+"'");
        }

        if(StringUtil.isNotEmpty(  interimAuditDetailEntity.getLdzw() )){
            colSb.append(", LDZW");
            valSb.append(", '"+ interimAuditDetailEntity.getLdzw()+"'");
        }

        
        if(StringUtil.isNotEmpty(  interimAuditDetailEntity.getCsym() )){
            colSb.append(", CSYM");
            valSb.append(", '"+ interimAuditDetailEntity.getCsym()+"'");
        }

        if(StringUtil.isNotEmpty(  interimAuditDetailEntity.getYjexsj() )){
            colSb.append(", YJEXSJ");
            valSb.append(", '"+ interimAuditDetailEntity.getYjexsj()+"'");
        }

        if(StringUtil.isNotEmpty(  interimAuditDetailEntity.getZjprojectname() )){
            colSb.append(", ZJPROJECTNAME");
            valSb.append(", '"+ interimAuditDetailEntity.getZjprojectname()+"'");
        }
        
        if(StringUtil.isNotEmpty(  interimAuditDetailEntity.getRzsj() )){
            colSb.append(", RZSJ");
            valSb.append(", '"+ interimAuditDetailEntity.getRzsj()+"'");
        }
        
        if(StringUtil.isNotEmpty(  interimAuditDetailEntity.getSjfwstarttime() )){
            colSb.append(", SJFWSTARTTIME");
            valSb.append(", '"+ interimAuditDetailEntity.getSjfwstarttime()+"'");
        }
        
        if(StringUtil.isNotEmpty(  interimAuditDetailEntity.getSjfwendtime() )){
            colSb.append(", SJFWENDTIME");
            valSb.append(", '"+ interimAuditDetailEntity.getSjfwendtime()+"'");
        }

        
        colSb.append(", CREATE_TIME)");
        valSb.append(", '"+ DateUtil.format(new Date(),"yyyy-MM-dd")+"')");

        colSb.append(valSb);
        return colSb.toString();
    }

    public String deleteByIds(String ids){
        StringBuffer sb = new StringBuffer();
        sb.append("DELETE FROM TBL_YQNS_INTERIM_AUDIT_DETAILS WHERE ID IN (" + ids+")");
        return sb.toString();
    }
}
