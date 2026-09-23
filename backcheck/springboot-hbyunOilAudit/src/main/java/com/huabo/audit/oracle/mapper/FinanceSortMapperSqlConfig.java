package com.huabo.audit.oracle.mapper;

import com.hbfk.util.PageInfo;
import com.huabo.audit.oracle.entity.FinanceSortEntity;
import com.hbfk.util.StringUtil;
import java.util.Date;
import cn.hutool.core.date.DateUtil;
/**
 * @author Rui
 * @ClassName FinanceSortMapperSqlConfig
 * @Description
 * @DATE 2023/9/23
 */
public class FinanceSortMapperSqlConfig {

    public String selectByEntity( FinanceSortEntity financeSortEntity){
        StringBuffer sb = new StringBuffer();
        //	sb.append("SELECT * FROM (SELECT T1.* , ROWNUM RN FROM (");
        sb.append("SELECT * FROM TBL_YQNS_FINANCE_SORT RS LEFT JOIN TBL_STAFF TS ON RS.CREATE_USER = TS.STAFFID WHERE 1=1 ");

        if(StringUtil.isNotEmpty(financeSortEntity.getProjectName())){
            sb.append("AND RS.PROJECT_NAME LIKE '%"+financeSortEntity.getProjectName()+"%'");
        }

        //	sb.append(") T1 WHERE ROWNUM <= "+(pageInfo.getCurrentRecord() + pageInfo.getPageSize() )+" ) T2 WHERE T2.RN > "+ pageInfo.getCurrentRecord());
        return sb.toString();
    }

    public String selectCountByEntity(FinanceSortEntity financeSortEntity){
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT COUNT(0) FROM (");
        sb.append("SELECT * FROM TBL_YQNS_FINANCE_SORT RS LEFT JOIN TBL_STAFF TS ON RS.CREATE_USER = TS.STAFFID WHERE 1=1 ");

        if(StringUtil.isNotEmpty(financeSortEntity.getProjectName())){
            sb.append("AND RS.PROJECT_NAME LIKE '%"+financeSortEntity.getProjectName()+"%'");
        }

        sb.append(")");
        return sb.toString();
    }

    public String updateEntity(FinanceSortEntity financeSortEntity){
        StringBuffer sb = new StringBuffer();
        sb.append("UPDATE TBL_YQNS_FINANCE_SORT SET ");

        sb.append("SUGGEST_DEPT_ID = "+financeSortEntity.getSuggestDeptId());

        if(financeSortEntity.getSort() != null ){
            sb.append(", SORT = "+financeSortEntity.getSort());
        }

        if(StringUtil.isNotEmpty(financeSortEntity.getProjectName())){
            sb.append(", PROJECT_NAME = '"+financeSortEntity.getProjectName()+"'");
        }

        if(StringUtil.isNotEmpty(financeSortEntity.getProjectPurpose())){
            sb.append(", PROJECT_PURPOSE = '"+financeSortEntity.getProjectPurpose()+"'");
        }

        if(StringUtil.isNotEmpty(financeSortEntity.getConcernsContent())){
            sb.append(", CONCERNS_CONTENT = '"+financeSortEntity.getConcernsContent()+"'");
        }

        if(StringUtil.isNotEmpty(financeSortEntity.getTimeRange())){
            sb.append(", TIME_RANGE = '"+financeSortEntity.getTimeRange()+"'");
        }

        if(StringUtil.isNotEmpty(financeSortEntity.getUnitRange())){
            sb.append(", UNIT_RANGE = '"+financeSortEntity.getUnitRange()+"'");
        }


        sb.append(" WHERE ID = '"+financeSortEntity.getId()+"'");

        return sb.toString();
    }

    public String insertEntity(FinanceSortEntity financeSortEntity){
        StringBuffer colSb = new StringBuffer();
        colSb.append("INSERT INTO TBL_YQNS_FINANCE_SORT (ID");

        StringBuffer valSb = new StringBuffer();
        valSb.append(" VALUES (HIBERNATE_SEQUENCE.nextval");

        if(financeSortEntity.getSuggestDeptId() != null){
            colSb.append(", SUGGEST_DEPT_ID");
            valSb.append(", '" + financeSortEntity.getSuggestDeptId() + "'");
        }


        if(financeSortEntity.getSort() != null ){
            colSb.append(", SORT");
            valSb.append(", '" + financeSortEntity.getSort() + "'");
        }

        if(StringUtil.isNotEmpty(financeSortEntity.getProjectName())){
            colSb.append(", PROJECT_NAME");
            valSb.append(", '" + financeSortEntity.getProjectName() + "'");
        }

        if(StringUtil.isNotEmpty(financeSortEntity.getProjectPurpose())){
            colSb.append(", PROJECT_PURPOSE");
            valSb.append(", '" + financeSortEntity.getProjectPurpose() + "'");
        }

        if(StringUtil.isNotEmpty(financeSortEntity.getConcernsContent())){
            colSb.append(", CONCERNS_CONTENT");
            valSb.append(", '" + financeSortEntity.getConcernsContent() + "'");
        }

        if(StringUtil.isNotEmpty(financeSortEntity.getTimeRange())){
            colSb.append(", TIME_RANGE");
            valSb.append(", '" + financeSortEntity.getTimeRange() + "'");
        }

        if(StringUtil.isNotEmpty(financeSortEntity.getUnitRange())){
            colSb.append(", UNIT_RANGE");
            valSb.append(", '" + financeSortEntity.getUnitRange() + "'");
        }


        if(financeSortEntity.getCreateUser() != null){
            colSb.append(", CREATE_USER");
            valSb.append(", '" + financeSortEntity.getCreateUser().getStaffid() + "'");
        }

        colSb.append(", CREATE_TIME)");
        valSb.append(", '"+ DateUtil.format(new Date(),"yyyy-MM-dd")+"')");

        colSb.append(valSb);
        return colSb.toString();
    }

    public String deleteByIds(String ids){
        StringBuffer sb = new StringBuffer();
        sb.append("DELETE FROM TBL_YQNS_FINANCE_SORT WHERE ID IN (" + ids+")");
        return sb.toString();
    }
}
