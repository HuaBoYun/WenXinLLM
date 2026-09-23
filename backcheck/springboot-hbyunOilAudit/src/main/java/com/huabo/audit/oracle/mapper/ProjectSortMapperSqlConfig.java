package com.huabo.audit.oracle.mapper;

import com.hbfk.util.PageInfo;
import com.huabo.audit.oracle.entity.ProjectSortEntity;
import com.hbfk.util.StringUtil;
import java.util.Date;
import cn.hutool.core.date.DateUtil;
/**
 * @author Rui
 * @ClassName ProjectSortMapperSqlConfig
 * @Description
 * @DATE 2023/9/23
 */
public class ProjectSortMapperSqlConfig {

    public String selectByEntity( ProjectSortEntity projectSortEntity){
        StringBuffer sb = new StringBuffer();
        //	sb.append("SELECT * FROM (SELECT T1.* , ROWNUM RN FROM (");
        sb.append("SELECT * FROM TBL_YQNS_PROJECT_SORT RS LEFT JOIN TBL_STAFF TS ON RS.CREATE_USER = TS.STAFFID WHERE 1=1 ");

        if(StringUtil.isNotEmpty(projectSortEntity.getProjectName())){
            sb.append("AND RS.PROJECT_NAME LIKE '%"+projectSortEntity.getProjectName()+"%'");
        }

        //	sb.append(") T1 WHERE ROWNUM <= "+(pageInfo.getCurrentRecord() + pageInfo.getPageSize() )+" ) T2 WHERE T2.RN > "+ pageInfo.getCurrentRecord());
        return sb.toString();
    }

    public String selectCountByEntity(ProjectSortEntity projectSortEntity){
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT COUNT(0) FROM (");
        sb.append("SELECT * FROM TBL_YQNS_PROJECT_SORT RS LEFT JOIN TBL_STAFF TS ON RS.CREATE_USER = TS.STAFFID WHERE 1=1 ");

        if(StringUtil.isNotEmpty(projectSortEntity.getProjectName())){
            sb.append("AND RS.PROJECT_NAME LIKE '%"+projectSortEntity.getProjectName()+"%'");
        }

        sb.append(")");
        return sb.toString();
    }

    public String updateEntity(ProjectSortEntity projectSortEntity){
        StringBuffer sb = new StringBuffer();
        sb.append("UPDATE TBL_YQNS_PROJECT_SORT SET ");
        sb.append("SUGGEST_DEPT_ID = '"+projectSortEntity.getSuggestDeptId()+"'");

        if(projectSortEntity.getSort() != null ){
            sb.append(", SORT = "+projectSortEntity.getSort());
        }

        if(StringUtil.isNotEmpty(projectSortEntity.getProjectName())){
            sb.append(", PROJECT_NAME = '"+projectSortEntity.getProjectName()+"'");
        }

        if(StringUtil.isNotEmpty(projectSortEntity.getProjectPurpose())){
            sb.append(", PROJECT_PURPOSE = '"+projectSortEntity.getProjectPurpose()+"'");
        }

        if(StringUtil.isNotEmpty(projectSortEntity.getConcernsContent())){
            sb.append(", CONCERNS_CONTENT = '"+projectSortEntity.getConcernsContent()+"'");
        }

        if(StringUtil.isNotEmpty(projectSortEntity.getTimeRange())){
            sb.append(", TIME_RANGE = '"+projectSortEntity.getTimeRange()+"'");
        }

        if(StringUtil.isNotEmpty(projectSortEntity.getUnitRange())){
            sb.append(", UNIT_RANGE = '"+projectSortEntity.getUnitRange()+"'");
        }


        sb.append(" WHERE ID = '"+projectSortEntity.getId()+"'");

        return sb.toString();
    }

    public String insertEntity(ProjectSortEntity projectSortEntity){
        StringBuffer colSb = new StringBuffer();
        colSb.append("INSERT INTO TBL_YQNS_PROJECT_SORT (ID");

        StringBuffer valSb = new StringBuffer();
        valSb.append(" VALUES (HIBERNATE_SEQUENCE.nextval");

        if(StringUtil.isNotEmpty(  projectSortEntity.getSuggestDeptId() )){
            colSb.append(", SUGGEST_DEPT_ID");
            valSb.append(", '" + projectSortEntity.getSuggestDeptId() + "'");
        }


        if(projectSortEntity.getSort() != null ){
            colSb.append(", SORT");
            valSb.append(", '" + projectSortEntity.getSort() + "'");
        }

        if(StringUtil.isNotEmpty(projectSortEntity.getProjectName())){
            colSb.append(", PROJECT_NAME");
            valSb.append(", '" + projectSortEntity.getProjectName() + "'");
        }

        if(StringUtil.isNotEmpty(projectSortEntity.getProjectPurpose())){
            colSb.append(", PROJECT_PURPOSE");
            valSb.append(", '" + projectSortEntity.getProjectPurpose() + "'");
        }

        if(StringUtil.isNotEmpty(projectSortEntity.getConcernsContent())){
            colSb.append(", CONCERNS_CONTENT");
            valSb.append(", '" + projectSortEntity.getConcernsContent() + "'");
        }

        if(StringUtil.isNotEmpty(projectSortEntity.getTimeRange())){
            colSb.append(", TIME_RANGE");
            valSb.append(", '" + projectSortEntity.getTimeRange() + "'");
        }

        if(StringUtil.isNotEmpty(projectSortEntity.getUnitRange())){
            colSb.append(", UNIT_RANGE");
            valSb.append(", '" + projectSortEntity.getUnitRange() + "'");
        }


        if(projectSortEntity.getCreateUser() != null){
            colSb.append(", CREATE_USER");
            valSb.append(", '" + projectSortEntity.getCreateUser().getStaffid() + "'");
        }
        colSb.append(", CREATE_TIME)");
        valSb.append(", '"+ DateUtil.format(new Date(),"yyyy-MM-dd")+"')");

        colSb.append(valSb);
        return colSb.toString();
    }

    public String deleteByIds(String ids){
        StringBuffer sb = new StringBuffer();
        sb.append("DELETE FROM TBL_YQNS_PROJECT_SORT WHERE ID IN (" + ids+")");
        return sb.toString();
    }
}
