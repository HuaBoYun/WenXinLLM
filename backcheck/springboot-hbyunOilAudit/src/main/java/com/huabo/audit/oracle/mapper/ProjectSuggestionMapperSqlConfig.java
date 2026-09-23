package com.huabo.audit.oracle.mapper;

import java.util.Date;

import org.apache.commons.lang.StringUtils;

import com.hbfk.util.StringUtil;
import com.huabo.audit.oracle.entity.ProjectSuggestionEntity;
import com.huabo.audit.oracle.entity.TblStaff;

import cn.hutool.core.date.DateUtil;
/**
 * @author Rui
 * @ClassName RequireSuggestionMapperSqlConfig
 * @Description
 * @DATE 2023/9/7
 */
public class ProjectSuggestionMapperSqlConfig {

    public String selectByEntity( ProjectSuggestionEntity projectSuggestionEntity, String createYear){
        StringBuffer sb = new StringBuffer();
        //	sb.append("SELECT * FROM (SELECT T1.* , ROWNUM RN FROM (");
        sb.append("SELECT * FROM TBL_YQNS_PROJECT_SUGGESTION RS LEFT JOIN TBL_STAFF TS ON RS.CREATE_USER = TS.STAFFID WHERE 1=1 ");
        
        //用户创建只能看见自己的。部门负责人要查看全部内容；下发人员可以查询下发给自己的
        TblStaff loginuser = projectSuggestionEntity.getCreateUser();
        sb.append("AND (RS.CREATE_USER = "+ loginuser.getStaffid());
        sb.append("OR INSTR (',' || PERSON_IDS || ',',',"+loginuser.getStaffid()+",') > 0");
        if(StringUtils.isNotBlank(projectSuggestionEntity.getQueryDeptIds())) {
        	sb.append(" OR RS.CREATE_USER IN (SELECT STAFFID FROM TBL_USER_ORGRELATION WHERE DEPTID IN (").append(projectSuggestionEntity.getQueryDeptIds()).append("))");
        }
        sb.append(")");
        
        if(projectSuggestionEntity.getId() != null){
            sb.append("AND RS.ID = "+ projectSuggestionEntity.getId());
        }
        if(StringUtil.isNotEmpty(projectSuggestionEntity.getProjectName())){
            sb.append("AND RS.PROJECT_NAME LIKE '%"+projectSuggestionEntity.getProjectName()+"%'");
        }
        if(StringUtil.isNotEmpty(projectSuggestionEntity.getProjectType())){
            sb.append("AND RS.PROJECT_TYPE LIKE '%"+projectSuggestionEntity.getProjectType()+"%'");
        }
        if(StringUtil.isNotEmpty(createYear)){
            sb.append("and RS.CREATE_TIME>'"+createYear+"-01-01' and RS.CREATE_TIME<'"+createYear+"-12-31'");
        }
        if(projectSuggestionEntity.getStatus() != null) {
        	sb.append(" AND RS.STATUS = ").append(projectSuggestionEntity.getStatus());
        }
        if(StringUtil.isNotEmpty(projectSuggestionEntity.getIds())){
            sb.append("and  RS.ID in (").append(projectSuggestionEntity.getIds()).append(")");
        }
        
        sb.append(" ORDER BY RS.ID DESC ");
        //	sb.append(") T1 WHERE ROWNUM <= "+(pageInfo.getCurrentRecord() + pageInfo.getPageSize() )+" ) T2 WHERE T2.RN > "+ pageInfo.getCurrentRecord());
        return sb.toString();
    }

    public String selectCountByEntity(ProjectSuggestionEntity projectSuggestionEntity){
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT COUNT(0) FROM (");
        sb.append("SELECT * FROM TBL_YQNS_PROJECT_SUGGESTION RS LEFT JOIN TBL_STAFF TS ON RS.CREATE_USER = TS.STAFFID WHERE 1=1 ");
        if(projectSuggestionEntity.getId() != null){
            sb.append("AND RS.ID = "+ projectSuggestionEntity.getId());
        }
        if(StringUtil.isNotEmpty(projectSuggestionEntity.getProjectName())){
            sb.append("AND RS.PROJECT_NAME LIKE '%"+projectSuggestionEntity.getProjectName()+"%'");
        }
        sb.append(")");
        return sb.toString();
    }

    public String updateEntity(ProjectSuggestionEntity projectSuggestionEntity){
        StringBuffer sb = new StringBuffer();
        sb.append("UPDATE TBL_YQNS_PROJECT_SUGGESTION SET ");
        sb.append("PROJECT_NAME = '"+projectSuggestionEntity.getProjectName()+"'");

        if(StringUtil.isNotEmpty(projectSuggestionEntity.getProjectPurpose())){
            sb.append(", PROJECT_PURPOSE = '"+projectSuggestionEntity.getProjectPurpose()+"'");
        }
        if(StringUtil.isNotEmpty(projectSuggestionEntity.getPurposeNo())){
            sb.append(", PURPOSENO = '"+projectSuggestionEntity.getPurposeNo()+"'");
        }

        if(StringUtil.isNotEmpty(projectSuggestionEntity.getConcernsContent())){
            sb.append(", CONCERNS_CONTENT = '"+projectSuggestionEntity.getConcernsContent()+"'");
        }


        if(projectSuggestionEntity.getProjectType() != null && !"".equals(projectSuggestionEntity.getProjectType())){
            sb.append(", PROJECT_TYPE = '"+projectSuggestionEntity.getProjectType()+"'");
        }

        if(StringUtil.isNotEmpty(projectSuggestionEntity.getTimeRange())){
            sb.append(", TIME_RANGE = '"+projectSuggestionEntity.getTimeRange()+"'");
        }

        if(StringUtil.isNotEmpty(projectSuggestionEntity.getPersonIds())){
            sb.append(", PERSON_IDS = '"+projectSuggestionEntity.getPersonIds()+"'");
        }

        if(StringUtil.isNotEmpty(projectSuggestionEntity.getUnitRange())){
            sb.append(", UNIT_RANGE = '"+projectSuggestionEntity.getUnitRange()+"'");
        }

        if(StringUtil.isNotEmpty(projectSuggestionEntity.getRemark())){
            sb.append(", REMARK = '"+projectSuggestionEntity.getRemark()+"'");
        }

        
        if(StringUtil.isNotEmpty(projectSuggestionEntity.getTborgname())) {
        	  sb.append(", TBORGNAME = '"+projectSuggestionEntity.getTborgname()+"'");
        }
        if(projectSuggestionEntity.getTborgid() != null){
        	sb.append(", TBORGID = "+projectSuggestionEntity.getTborgid());
        }
        
        sb.append(" WHERE ID = '"+projectSuggestionEntity.getId()+"'");

        return sb.toString();
    }

    public String insertEntity(ProjectSuggestionEntity projectSuggestionEntity){
        StringBuffer colSb = new StringBuffer();
        colSb.append("INSERT INTO TBL_YQNS_PROJECT_SUGGESTION (ID");

        StringBuffer valSb = new StringBuffer();
        valSb.append(" VALUES (HIBERNATE_SEQUENCE.nextval");

        if(StringUtil.isNotEmpty(projectSuggestionEntity.getProjectName())){
            colSb.append(", PROJECT_NAME");
            valSb.append(", '"+ projectSuggestionEntity.getProjectName()+"'");
        }
        
        if(StringUtil.isNotEmpty(projectSuggestionEntity.getPurposeNo())){
            colSb.append(", PURPOSENO");
            valSb.append(", '"+ projectSuggestionEntity.getPurposeNo()+"'");
        }

        if(StringUtil.isNotEmpty(projectSuggestionEntity.getProjectPurpose())){
            colSb.append(", PROJECT_PURPOSE");
            valSb.append(", '"+ projectSuggestionEntity.getProjectPurpose()+"'");
        }

        if(StringUtil.isNotEmpty(projectSuggestionEntity.getConcernsContent())){
            colSb.append(", CONCERNS_CONTENT");
            valSb.append(", '"+ projectSuggestionEntity.getConcernsContent()+"'");
        }


        if(projectSuggestionEntity.getProjectType() != null && !"".equals(projectSuggestionEntity.getProjectType())){
            colSb.append(", PROJECT_TYPE");
            valSb.append(", '"+ projectSuggestionEntity.getProjectType()+"'");
        }

        if(StringUtil.isNotEmpty(projectSuggestionEntity.getUnitRange())){
            colSb.append(", UNIT_RANGE");
            valSb.append(", '"+ projectSuggestionEntity.getUnitRange()+"'");
        }

        if(StringUtil.isNotEmpty(projectSuggestionEntity.getTimeRange())){
            colSb.append(", TIME_RANGE");
            valSb.append(", '"+ projectSuggestionEntity.getTimeRange()+"'");
        }


        if(StringUtil.isNotEmpty(projectSuggestionEntity.getRemark())) {
            colSb.append(", REMARK");
            valSb.append(", '" + projectSuggestionEntity.getRemark() + "'");
        }
        if(StringUtil.isNotEmpty(projectSuggestionEntity.getTborgname())) {
            colSb.append(", TBORGNAME");
            valSb.append(", '" + projectSuggestionEntity.getTborgname() + "'");
        }
        if(projectSuggestionEntity.getTborgid() != null){
            colSb.append(", TBORGID");
            valSb.append(", '" + projectSuggestionEntity.getTborgid() + "'");
        }

        if(projectSuggestionEntity.getCreateUser() != null){
            colSb.append(", CREATE_USER");
            valSb.append(", '" + projectSuggestionEntity.getCreateUser().getStaffid() + "'");
        }

        colSb.append(", CREATE_TIME)");
        valSb.append(", '"+ DateUtil.format(new Date(),"yyyy-MM-dd")+"')");

        colSb.append(valSb);
        return colSb.toString();
    }

    public String deleteByIds(String ids){
        StringBuffer sb = new StringBuffer();
        sb.append("DELETE FROM TBL_YQNS_PROJECT_SUGGESTION WHERE ID IN (" + ids+")");
        return sb.toString();
    }

    public String selectByIds(String ids){
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT* FROM TBL_YQNS_PROJECT_SUGGESTION WHERE ID IN (" + ids+")");
        return sb.toString();
    }
}
