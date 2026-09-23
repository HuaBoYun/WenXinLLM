package com.huabo.audit.oracle.mapper;


import com.hbfk.util.PageInfo;
import com.huabo.audit.oracle.entity.AuditSuggestion2LEntity;
import com.hbfk.util.StringUtil;
import com.hbfk.util.database.DataBaseSqlConfig;

import java.util.Date;

import org.apache.commons.lang.StringUtils;

import cn.hutool.core.date.DateUtil;
/**
 * @author Rui
 * @ClassName AuditSuggestion2LMapperSqlConfig
 * @Description
 * @DATE 2023/9/23
 */
public class AuditSuggestion2LMapperSqlConfig {

    public String selectByEntity( AuditSuggestion2LEntity auditSuggestion2LEntity){
        StringBuffer sb = new StringBuffer();
        //	sb.append("SELECT * FROM (SELECT T1.* , ROWNUM RN FROM (");
        sb.append("SELECT * FROM TBL_YQNS_2L_AUDIT_SUGGESTION RS LEFT JOIN TBL_STAFF TS ON RS.CREATE_USER = TS.STAFFID WHERE 1=1 ");

        if(auditSuggestion2LEntity.getOrg() != null && StringUtil.isNotEmpty(auditSuggestion2LEntity.getOrg().getOrgname())){
            sb.append("AND RS.ORG IN ( SELECT ORGID FROM TBL_ORGANIZATION WHERE ORGNAME LIKE '%"+auditSuggestion2LEntity.getOrg().getOrgname()+"%') ");
        }

        if(StringUtil.isNotEmpty(auditSuggestion2LEntity.getName())){
            sb.append("AND RS.NAME LIKE '%"+auditSuggestion2LEntity.getName()+"%'");
        }

        //	sb.append(") T1 WHERE ROWNUM <= "+(pageInfo.getCurrentRecord() + pageInfo.getPageSize() )+" ) T2 WHERE T2.RN > "+ pageInfo.getCurrentRecord());
        return sb.toString();
    }
    
    public String selectListDraftPlan(AuditSuggestion2LEntity auditSuggestion2LEntity) throws Exception{
        StringBuffer sb = new StringBuffer();
        //	sb.append("SELECT * FROM (SELECT T1.* , ROWNUM RN FROM (");
        sb.append("SELECT * FROM TBL_YQNS_2L_AUDIT_SUGGESTION RS LEFT JOIN TBL_STAFF TS ON RS.CREATE_USER = TS.STAFFID WHERE 1=1 ");
        sb.append(" AND RS.ID NOT IN (SELECT GLID FROM TBL_YQNS_JHGL_JHCG_GL WHERE GLTYPE = '22')");
        if(auditSuggestion2LEntity.getOrg() != null && StringUtil.isNotEmpty(auditSuggestion2LEntity.getOrg().getOrgname())){
            sb.append(" AND RS.ORG IN ( SELECT ORGID FROM TBL_ORGANIZATION WHERE ORGNAME LIKE '%"+auditSuggestion2LEntity.getOrg().getOrgname()+"%') ");
        }

        if(StringUtil.isNotEmpty(auditSuggestion2LEntity.getName())){
            sb.append(" AND RS.NAME LIKE '%"+auditSuggestion2LEntity.getName()+"%'");
        }

        //	sb.append(") T1 WHERE ROWNUM <= "+(pageInfo.getCurrentRecord() + pageInfo.getPageSize() )+" ) T2 WHERE T2.RN > "+ pageInfo.getCurrentRecord());
        return sb.toString();
    }
    
    
    public String selectCountByEntity(AuditSuggestion2LEntity auditSuggestion2LEntity){
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT COUNT(0) FROM (");
        sb.append("SELECT * FROM TBL_YQNS_2L_AUDIT_SUGGESTION RS LEFT JOIN TBL_STAFF TS ON RS.CREATE_USER = TS.STAFFID WHERE 1=1 ");


        if(auditSuggestion2LEntity.getOrg() != null && StringUtil.isNotEmpty(auditSuggestion2LEntity.getOrg().getOrgname())){
            sb.append("AND RS.ORG IN ( SELECT ORGID FROM TBL_ORGANIZATION WHERE ORGNAME LIKE '%"+auditSuggestion2LEntity.getOrg().getOrgname()+"%') ");
        }

        if(StringUtil.isNotEmpty(auditSuggestion2LEntity.getName())){
            sb.append("AND RS.NAME LIKE '%"+auditSuggestion2LEntity.getName()+"%'");
        }

        sb.append(")");
        return sb.toString();
    }

    public String updateEntity(AuditSuggestion2LEntity auditSuggestion2LEntity) throws Exception{
        StringBuffer sb = new StringBuffer();
        sb.append("UPDATE TBL_YQNS_2L_AUDIT_SUGGESTION SET ");
        sb.append("ORG = '"+auditSuggestion2LEntity.getOrgId()+"'");

        if(StringUtil.isNotEmpty(auditSuggestion2LEntity.getName())){
            sb.append(", NAME = '"+auditSuggestion2LEntity.getName()+"'");
        }

        if(StringUtil.isNotEmpty(auditSuggestion2LEntity.getJob())){
            sb.append(", JOB = '"+auditSuggestion2LEntity.getJob()+"'");
        }

        if(StringUtil.isNotEmpty(auditSuggestion2LEntity.getLevel())){
            sb.append(", LV = '"+auditSuggestion2LEntity.getLevel()+"'");
        }

        if(auditSuggestion2LEntity.getWorkStartTime() != null){
            sb.append(", WORK_START_TIME = '"+DateUtil.format(auditSuggestion2LEntity.getWorkStartTime() ,"yyyy-MM-dd")+"'");
        }

        if(auditSuggestion2LEntity.getWorkEndTime() != null){
            sb.append(", WORK_END_TIME = '"+DateUtil.format(auditSuggestion2LEntity.getWorkEndTime() ,"yyyy-MM-dd")+"'");
        }

        if(auditSuggestion2LEntity.getWorkDuration() != null){
            sb.append(", WORK_DURATION = '"+auditSuggestion2LEntity.getWorkDuration()+"'");
        }

        if(auditSuggestion2LEntity.getHasFinanceProblem() != null){
            sb.append(", HAS_FINANCE_PROBLEM = '"+auditSuggestion2LEntity.getHasFinanceProblem()+"'");
        }

        if(auditSuggestion2LEntity.getHasEconomicProblem() != null){
            sb.append(", HAS_ECONOMIC_PROBLEM = '"+auditSuggestion2LEntity.getHasEconomicProblem()+"'");
        }

        if(auditSuggestion2LEntity.getHasBeenComplain() != null){
            sb.append(", HAS_BEEN_COMPLAIN = '"+auditSuggestion2LEntity.getHasBeenComplain()+"'");
        }

        if(auditSuggestion2LEntity.getIsLeaveNextYear() != null){
            sb.append(", IS_LEAVE_NEXT_YEAR = '"+auditSuggestion2LEntity.getIsLeaveNextYear()+"'");
        }

        if(auditSuggestion2LEntity.getNeedAudit() != null){
            sb.append(", NEED_AUDIT = '"+auditSuggestion2LEntity.getNeedAudit()+"'");
        }
        
        if(StringUtils.isNotBlank(auditSuggestion2LEntity.getProjectType())) {
        	sb.append(", PROJECTTYPE = '"+auditSuggestion2LEntity.getProjectType()+"'");
        }
        
        if(StringUtils.isNotBlank(auditSuggestion2LEntity.getAuditScope())) {
        	sb.append(", AUDITSCOPE = '"+auditSuggestion2LEntity.getAuditScope()+"'");
        }
        
        if(StringUtils.isNotBlank(auditSuggestion2LEntity.getRemarks())) {
        	sb.append(", REMARKS = '"+auditSuggestion2LEntity.getRemarks()+"'");
        }
        
        if(auditSuggestion2LEntity.getEntrustTime() != null) {
        	sb.append(", ENTRUSTTIME = ").append(DataBaseSqlConfig.getDateStrFormat(auditSuggestion2LEntity.getEntrustTime()));
        }

        sb.append(" WHERE ID = '"+auditSuggestion2LEntity.getId()+"'");

        return sb.toString();
    }

    public String insertEntity(AuditSuggestion2LEntity auditSuggestion2LEntity) throws Exception{
        StringBuffer colSb = new StringBuffer();
        colSb.append("INSERT INTO TBL_YQNS_2L_AUDIT_SUGGESTION (ID");

        StringBuffer valSb = new StringBuffer();
        valSb.append(" VALUES (HIBERNATE_SEQUENCE.nextval");

        if(StringUtil.isNotEmpty(  auditSuggestion2LEntity.getOrgId() )){
            colSb.append(", ORG");
            valSb.append(", '" + auditSuggestion2LEntity.getOrgId() + "'");
        }

        if(StringUtils.isNotBlank(auditSuggestion2LEntity.getProjectType())) {
        	colSb.append(", PROJECTTYPE");
            valSb.append(", '" + auditSuggestion2LEntity.getProjectType() + "'");
        }
        
        if(StringUtils.isNotBlank(auditSuggestion2LEntity.getAuditScope())) {
        	colSb.append(", AUDITSCOPE");
            valSb.append(", '" + auditSuggestion2LEntity.getAuditScope() + "'");
        }
        
        if(StringUtils.isNotBlank(auditSuggestion2LEntity.getRemarks())) {
        	colSb.append(", REMARKS");
            valSb.append(", '" + auditSuggestion2LEntity.getRemarks() + "'");
        }
        
        if(auditSuggestion2LEntity.getEntrustTime() != null) {
        	colSb.append(", ENTRUSTTIME");
            valSb.append(", "+DataBaseSqlConfig.getDateStrFormat(auditSuggestion2LEntity.getEntrustTime()));
        }

        if(StringUtil.isNotEmpty(auditSuggestion2LEntity.getName())){
            colSb.append(", NAME");
            valSb.append(", '" + auditSuggestion2LEntity.getName() + "'");
        }

        if(StringUtil.isNotEmpty(auditSuggestion2LEntity.getJob())){
            colSb.append(", JOB");
            valSb.append(", '" + auditSuggestion2LEntity.getJob() + "'");
        }

        if(StringUtil.isNotEmpty(auditSuggestion2LEntity.getLevel())){
            colSb.append(", LV");
            valSb.append(", '" + auditSuggestion2LEntity.getLevel() + "'");
        }


        if(auditSuggestion2LEntity.getWorkStartTime() != null){
            colSb.append(", WORK_START_TIME");
            valSb.append(", '"+DateUtil.format(auditSuggestion2LEntity.getWorkStartTime() ,"yyyy-MM-dd")+"'");
        }

        if(auditSuggestion2LEntity.getWorkEndTime() != null){
            colSb.append(", WORK_END_TIME");
            valSb.append(", '"+DateUtil.format(auditSuggestion2LEntity.getWorkEndTime() ,"yyyy-MM-dd")+"'");
        }

        if(auditSuggestion2LEntity.getWorkDuration() != null){
            colSb.append(", WORK_DURATION");
            valSb.append(", '" + auditSuggestion2LEntity.getWorkDuration() + "'");
        }

        if(auditSuggestion2LEntity.getHasFinanceProblem() != null){
            colSb.append(", HAS_FINANCE_PROBLEM");
            valSb.append(", '" + auditSuggestion2LEntity.getHasFinanceProblem() + "'");
        }

        if(auditSuggestion2LEntity.getHasEconomicProblem() != null){
            colSb.append(", HAS_ECONOMIC_PROBLEM");
            valSb.append(", '" + auditSuggestion2LEntity.getHasEconomicProblem() + "'");
        }

        if(auditSuggestion2LEntity.getHasBeenComplain() != null){
            colSb.append(", HAS_BEEN_COMPLAIN");
            valSb.append(", '" + auditSuggestion2LEntity.getHasBeenComplain() + "'");
        }

        if(auditSuggestion2LEntity.getIsLeaveNextYear() != null){
            colSb.append(", IS_LEAVE_NEXT_YEAR");
            valSb.append(", '" + auditSuggestion2LEntity.getIsLeaveNextYear() + "'");
        }

        if(auditSuggestion2LEntity.getNeedAudit() != null){
            colSb.append(", NEED_AUDIT");
            valSb.append(", '" + auditSuggestion2LEntity.getNeedAudit() + "'");
        }


        if(auditSuggestion2LEntity.getCreateUser() != null){
            colSb.append(", CREATE_USER");
            valSb.append(", '" + auditSuggestion2LEntity.getCreateUser().getStaffid() + "'");
        }

        colSb.append(", CREATE_TIME)");

        valSb.append(", '"+ DateUtil.format(new Date(),"yyyy-MM-dd")+"')");

        colSb.append(valSb);
        return colSb.toString();
    }

    public String deleteByIds(String ids){
        StringBuffer sb = new StringBuffer();
        sb.append("DELETE FROM TBL_YQNS_2L_AUDIT_SUGGESTION WHERE ID IN (" + ids+")");
        return sb.toString();
    }


    public String findByIds(String ids){
        StringBuffer sb = new StringBuffer();
        sb.append("select * from   TBL_YQNS_2L_AUDIT_SUGGESTION WHERE ID IN (" + ids+")");
        return sb.toString();
    }
}
