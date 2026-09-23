package com.huabo.compliance.mysql.mapper;

import com.hbfk.util.PageInfo;
import com.huabo.compliance.mysql.entity.TblHomePageModelMySql;

import java.math.BigDecimal;

public class TblHomePageModelMapperSqlConfig {

    public String homePageModels(PageInfo<TblHomePageModelMySql> pageInfo, BigDecimal staffid) {
        StringBuffer sbSql = new StringBuffer("SELECT * FROM ( SELECT BUDGET.* FROM (select PA.name,pa.url,pa.type,j.tblWidth,pa.id from TBL_HOME_PAGE_MODEL pa " +
                "inner join TBL_HOME_PAGE_JURISDICTION j on pa.id=j.MODELID and j.staffid= " + staffid);


        sbSql.append(" ) BUDGET LIMIT " +  pageInfo.getCurrentRecord() + " , " + pageInfo.getPageSize() + " )  as a");
        String sql = sbSql.toString();
        return sql;
    }

    public String homePageModelsCount(BigDecimal staffid) {
        StringBuffer sbSql = new StringBuffer("select COUNT(*) from TBL_HOME_PAGE_MODEL pa " +
                "inner join TBL_HOME_PAGE_JURISDICTION j on pa.id=j.MODELID and j.staffid= " + staffid);


        //sbSql.append(" ) BUDGET WHERE rownum <= "+(pageInfo.getCurrentPage()*pageInfo.getPageSize())+" ) WHERE RNUM > "+pageInfo.getCurrentRecord());
        String sql = sbSql.toString();
        return sql;
    }
}
