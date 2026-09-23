package com.huabo.audit.oracle.mapper;

import com.hbfk.util.PageInfo;
import com.hbfk.util.StringUtil;
import com.hbfk.util.database.DataBaseSqlConfig;
import com.hbfk.util.redis.Random.RandomUtil;
import com.huabo.audit.oracle.entity.ServiceRequirementEntity;
import java.util.Date;
import cn.hutool.core.date.DateUtil;
/**
 * @author Rui
 * @ClassName ServiceSuggestionMapperSqlConfig
 * @Description
 * @DATE 2023/9/7
 */
public class ServiceRequirementMapperSqlConfig {

    public String selectByEntity( ServiceRequirementEntity serviceRequirementEntity) throws Exception{
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT RS.*,TS.STAFFID,TS.REALNAME,ORG.ORGID,ORG.ORGNAME FROM TBL_YQNS_SERVICE_REQUIREMENT RS LEFT JOIN TBL_STAFF TS ON RS.CREATE_USER = TS.STAFFID LEFT JOIN TBL_ORGANIZATION ORG ON RS.UNIT_ID = ORG.ORGID WHERE 1=1 ");
        
        sb.append(" AND ( RS.CREATE_USER = ").append(serviceRequirementEntity.getCurrentStaffId()).append(" OR ").append(DataBaseSqlConfig.getWhereColumnInStr("RS.PERSON_IDS", serviceRequirementEntity.getCurrentStaffId().toString(), ","));
        
        if(StringUtil.isNotEmpty(serviceRequirementEntity.getQueryDeptIds())) {
        	sb.append(" OR RS.CREATE_USER IN (SELECT STAFFID FROM TBL_USER_ORGRELATION WHERE DEPTID IN (").append(serviceRequirementEntity.getQueryDeptIds()).append(")) ");
        }
        
        sb.append(" ) ");
        
        if(serviceRequirementEntity.getId() != null){
            sb.append(" AND RS.ID = "+ serviceRequirementEntity.getId());
        }
        if(StringUtil.isNotEmpty(serviceRequirementEntity.getIds())){
            sb.append(" AND RS.ID in ( "+ serviceRequirementEntity.getIds()).append(") ");
        }

        if(serviceRequirementEntity.getRequirementNo()!= null){
            sb.append(" AND RS.RequirementNo like'%"+ serviceRequirementEntity.getRequirementNo()+"%' ");
        }
        if(StringUtil.isNotEmpty(serviceRequirementEntity.getAuditItem())){
            sb.append(" AND RS.AUDIT_ITEM LIKE '%"+serviceRequirementEntity.getAuditItem()+"%'");
        }
        if(StringUtil.isNotEmpty(serviceRequirementEntity.getProjectType())){
            sb.append(" AND RS.PROJECT_TYPE LIKE '%"+serviceRequirementEntity.getProjectType()+"%'");
        }
        if(serviceRequirementEntity.getQueryYear() != null) {
        	sb.append(" AND RS.CREATE_TIME LIKE '").append(serviceRequirementEntity.getQueryYear()).append("%'");
        }
        sb.append(" ORDER BY RS.CREATE_TIME DESC ");
        return sb.toString();
    }

    public String selectCountByEntity(ServiceRequirementEntity serviceRequirementEntity){
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT COUNT(0) FROM (");
        sb.append("SELECT * FROM TBL_YQNS_SERVICE_REQUIREMENT RS LEFT JOIN TBL_STAFF TS ON RS.CREATE_USER = TS.STAFFID WHERE 1=1 ");
        if(serviceRequirementEntity.getId() != null){
            sb.append(" AND RS.ID = "+ serviceRequirementEntity.getId());
        }
        if(StringUtil.isNotEmpty(serviceRequirementEntity.getAuditItem())){
            sb.append(" AND RS.AUDIT_ITEM LIKE '%"+serviceRequirementEntity.getAuditItem()+"%'");
        }
        sb.append(")");
        return sb.toString();
    }

    public String updateEntity(ServiceRequirementEntity serviceRequirementEntity){
        StringBuffer sb = new StringBuffer();
        sb.append("UPDATE TBL_YQNS_SERVICE_REQUIREMENT SET ");
        sb.append("AUDIT_ITEM = '"+serviceRequirementEntity.getAuditItem()+"'");

        if(serviceRequirementEntity.getRequirementNo() != null){
            sb.append(", REQUIREMENTNO = '"+serviceRequirementEntity.getRequirementNo()+"'");
        }
        
        if(StringUtil.isNotEmpty(serviceRequirementEntity.getAuditPurpose())){
            sb.append(", AUDIT_PURPOSE = '"+serviceRequirementEntity.getAuditPurpose()+"'");
        }

        if(StringUtil.isNotEmpty(serviceRequirementEntity.getConcernsContent())){
            sb.append(", CONCERNS_CONTENT = '"+serviceRequirementEntity.getConcernsContent()+"'");
        }

        if(serviceRequirementEntity.getOrganizationId()!=null){
            sb.append(", UNIT_ID = '"+serviceRequirementEntity.getOrganizationId()+"'");
        }

        if(serviceRequirementEntity.getProjectType() != null && !"".equals(serviceRequirementEntity.getProjectType())){
            sb.append(", PROJECT_TYPE = '"+serviceRequirementEntity.getProjectType()+"'");
        }

        if(StringUtil.isNotEmpty(serviceRequirementEntity.getPersonIds())){
            sb.append(", PERSON_IDS = '"+serviceRequirementEntity.getPersonIds()+"'");
        }

        if(StringUtil.isNotEmpty(serviceRequirementEntity.getTimeRange())){
            sb.append(", TIME_RANGE = '"+serviceRequirementEntity.getTimeRange()+"'");
        }

        if(StringUtil.isNotEmpty(serviceRequirementEntity.getUnitRange())){
            sb.append(", UNIT_RANGE = '"+serviceRequirementEntity.getUnitRange()+"'");
        }

        if(StringUtil.isNotEmpty(serviceRequirementEntity.getRemark())){
            sb.append(", REMARK = '"+serviceRequirementEntity.getRemark()+"'");
        }

        sb.append(" WHERE ID = '"+serviceRequirementEntity.getId()+"'");

        return sb.toString();
    }

    public String insertEntity(ServiceRequirementEntity serviceRequirementEntity){
        StringBuffer colSb = new StringBuffer();
        colSb.append("INSERT INTO TBL_YQNS_SERVICE_REQUIREMENT (ID");

        StringBuffer valSb = new StringBuffer();
        valSb.append(" VALUES (").append(RandomUtil.uuBigDecimalId());

        if(StringUtil.isNotEmpty(serviceRequirementEntity.getAuditItem())){
            colSb.append(", AUDIT_ITEM");
            valSb.append(", '"+ serviceRequirementEntity.getAuditItem()+"'");
        }

        if(serviceRequirementEntity.getRequirementNo() != null){
        	colSb.append(", REQUIREMENTNO");
            valSb.append(", "+ serviceRequirementEntity.getRequirementNo()+"");
        }
        
        if(StringUtil.isNotEmpty(serviceRequirementEntity.getAuditPurpose())){
            colSb.append(", AUDIT_PURPOSE");
            valSb.append(", '"+ serviceRequirementEntity.getAuditPurpose()+"'");
        }

        if(StringUtil.isNotEmpty(serviceRequirementEntity.getConcernsContent())){
            colSb.append(", CONCERNS_CONTENT");
            valSb.append(", '"+ serviceRequirementEntity.getConcernsContent()+"'");
        }

        if(serviceRequirementEntity.getOrganizationId()!=null){
            colSb.append(", UNIT_ID");
            valSb.append(", '"+ serviceRequirementEntity.getOrganizationId()+"'");
        }


        if(serviceRequirementEntity.getProjectType() != null && !"".equals(serviceRequirementEntity.getProjectType())){
            colSb.append(", PROJECT_TYPE");
            valSb.append(", '"+ serviceRequirementEntity.getProjectType()+"'");
        }

        if(StringUtil.isNotEmpty(serviceRequirementEntity.getUnitRange())){
            colSb.append(", UNIT_RANGE");
            valSb.append(", '"+ serviceRequirementEntity.getUnitRange()+"'");
        }

        if(StringUtil.isNotEmpty(serviceRequirementEntity.getTimeRange())){
            colSb.append(", TIME_RANGE");
            valSb.append(", '"+ serviceRequirementEntity.getTimeRange()+"'");
        }


        if(StringUtil.isNotEmpty(serviceRequirementEntity.getRemark())) {
            colSb.append(", REMARK");
            valSb.append(", '" + serviceRequirementEntity.getRemark() + "'");
        }

        if(serviceRequirementEntity.getCreateUser() != null){
            colSb.append(", CREATE_USER");
            valSb.append(", '" + serviceRequirementEntity.getCreateUser().getStaffid() + "'");
        }

        colSb.append(", CREATE_TIME)");
        valSb.append(", '"+ DateUtil.format(new Date(),"yyyy-MM-dd")+"')");

        colSb.append(valSb);
        return colSb.toString();
    }

    public String deleteByIds(String ids){
        StringBuffer sb = new StringBuffer();
        sb.append("DELETE FROM TBL_YQNS_SERVICE_REQUIREMENT WHERE ID IN (" + ids+")");
        return sb.toString();
    }
}
