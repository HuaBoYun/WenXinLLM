package com.huabo.audit.oracle.mapper;

import com.hbfk.util.PageInfo;
import com.hbfk.util.StringUtil;
import com.huabo.audit.oracle.entity.AuditProjectZkEntity;

import java.math.BigDecimal;

/**
 * @author Rui
 * @ClassName AuditProjectZkMapperSqlConfig
 * @Description
 * @DATE 2023/10/17
 */
public class AuditProjectZkMapperSqlConfig {

    public String selectByEntity(AuditProjectZkEntity auditProjectZkEntity){
        StringBuffer sb = new StringBuffer();
//        sb.append("SELECT * FROM (SELECT T1.* , ROWNUM RN FROM (");
        sb.append("SELECT * FROM TBL_YQNS_PROJECT_ZK RS WHERE 1=1 ");

        if(auditProjectZkEntity.getProjectId() != null ){
            sb.append("AND RS.PROJECTID  = '"+auditProjectZkEntity.getProjectId()+"'");
        }

        if(StringUtil.isNotEmpty(auditProjectZkEntity.getProjectName())){
            sb.append("AND RS.PROJECT_NAME  LIKE '%"+auditProjectZkEntity.getProjectName()+"%'");
        }

        if(auditProjectZkEntity.getMoney() != null){
            sb.append("AND RS.MONEY  = '"+auditProjectZkEntity.getMoney()+"'");
        }

//        sb.append(" ) T1 WHERE ROWNUM <= "+(pageInfo.getCurrentRecord() + pageInfo.getPageSize() )+" ) T2 WHERE T2.RN > "+ pageInfo.getCurrentRecord());
        return sb.toString();
    }

    public String selectCountByEntity(AuditProjectZkEntity auditProjectZkEntity){
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT COUNT(0) FROM (");
        sb.append("SELECT * FROM TBL_YQNS_PROJECT_ZK RS  WHERE 1=1");

        if(StringUtil.isNotEmpty(auditProjectZkEntity.getProjectName())){
            sb.append("AND RS.PROJECT_NAME  LIKE '%"+auditProjectZkEntity.getProjectName()+"%')");
        }

        if(auditProjectZkEntity.getMoney() != null){
            sb.append("AND RS.MONEY  = '"+auditProjectZkEntity.getMoney()+"'");
        }
        sb.append(")");
        return sb.toString();
    }


    public String updateEntity(AuditProjectZkEntity auditProjectZkEntity){
        StringBuffer sb = new StringBuffer();
        sb.append("UPDATE TBL_YQNS_PROJECT_ZK SET ");
        sb.append("RESULT_ID = '"+auditProjectZkEntity.getResultId()+"'");

        if(StringUtil.isNotEmpty(auditProjectZkEntity.getProjectName())){
            sb.append(", PROJECT_NAME = '"+auditProjectZkEntity.getProjectName()+"'");
        }
        
        if(StringUtil.isNotEmpty(auditProjectZkEntity.getNo())){
            sb.append(", NO = '"+auditProjectZkEntity.getNo()+"'");
        }

        if(StringUtil.isNotEmpty(  auditProjectZkEntity.getAuditOrgId() )){
            sb.append(", AUDIT_ORG_ID = '" + auditProjectZkEntity.getAuditOrgId()+"'");
        }

        if(StringUtil.isNotEmpty(  auditProjectZkEntity.getZkOrgId() )){
            sb.append(", ZK_ORG_ID = '" + auditProjectZkEntity.getZkOrgId()+"'");
        }

        if(StringUtil.isNotEmpty(auditProjectZkEntity.getReason())){
            sb.append(", REASON = '"+auditProjectZkEntity.getReason()+"'");
        }

        if(StringUtil.isNotEmpty(auditProjectZkEntity.getRemark())){
            sb.append(", REMARK = '"+auditProjectZkEntity.getRemark()+"'");
        }

        if(auditProjectZkEntity.getMoney() != null){
            sb.append(", MONEY = '"+auditProjectZkEntity.getMoney()+"'");
        }

        if(StringUtil.isNotEmpty(auditProjectZkEntity.getRESERVED1())){
            sb.append(", RESERVED1 = '"+auditProjectZkEntity.getRESERVED1()+"'");
        }

        sb.append(" WHERE ID = '"+auditProjectZkEntity.getId()+"'");

        return sb.toString();
    }


    public String insertEntity(AuditProjectZkEntity auditProjectZkEntity){
        StringBuffer colSb = new StringBuffer();
        colSb.append("INSERT INTO TBL_YQNS_PROJECT_ZK (ID");

        StringBuffer valSb = new StringBuffer();
        valSb.append(" VALUES (HIBERNATE_SEQUENCE.nextval");


        if(StringUtil.isNotEmpty(auditProjectZkEntity.getResultId())){
            colSb.append(", RESULT_ID");
            valSb.append(", '" + auditProjectZkEntity.getResultId() + "'");
        }

        if(StringUtil.isNotEmpty(auditProjectZkEntity.getProjectName())){
            colSb.append(", PROJECT_NAME");
            valSb.append(", '" + auditProjectZkEntity.getProjectName() + "'");
        }
        
        if(StringUtil.isNotEmpty(auditProjectZkEntity.getNo())){
            colSb.append(", NO");
            valSb.append(", '" + auditProjectZkEntity.getNo() + "'");
        }

        if(StringUtil.isNotEmpty(  auditProjectZkEntity.getAuditOrgId() )){
            colSb.append(", AUDIT_ORG_ID");
            valSb.append(", '" + auditProjectZkEntity.getAuditOrgId() + "'");
        }

        if(StringUtil.isNotEmpty(  auditProjectZkEntity.getZkOrgId() )){
            colSb.append(", ZK_ORG_ID");
            valSb.append(", '" + auditProjectZkEntity.getZkOrgId() + "'");
        }

        if(StringUtil.isNotEmpty(auditProjectZkEntity.getReason())){
            colSb.append(", REASON");
            valSb.append(", '" + auditProjectZkEntity.getReason() + "'");
        }

        if(StringUtil.isNotEmpty(auditProjectZkEntity.getRemark())){
            colSb.append(", REMARK");
            valSb.append(", '" + auditProjectZkEntity.getRemark() + "'");
        }

        if(auditProjectZkEntity.getMoney() != null){
            colSb.append(", MONEY");
            valSb.append(", '" + auditProjectZkEntity.getMoney() + "'");
        }
        if(auditProjectZkEntity.getProjectId() != null){
            colSb.append(", PROJECTID");
            valSb.append(", '" + auditProjectZkEntity.getProjectId() + "'");
        }

        if(auditProjectZkEntity.getCreatorStaffId() != null){
            colSb.append(", CREATORSTAFFID");
            valSb.append(", '" + auditProjectZkEntity.getCreatorStaffId() + "'");
        }

        if(StringUtil.isNotEmpty(auditProjectZkEntity.getRESERVED1())){
            colSb.append(", RESERVED1");
            valSb.append(", '" + auditProjectZkEntity.getRESERVED1() + "'");
        }


        colSb.append(")");
        valSb.append(")");

        colSb.append(valSb);
        return colSb.toString();
    }

    public String deleteByIds(String ids){
        StringBuffer sb = new StringBuffer();
        sb.append("DELETE FROM TBL_YQNS_PROJECT_ZK WHERE ID IN (" + ids+")");
        return sb.toString();
    }


}
