package com.huabo.monitor.mysql.mapper;

import com.hbfk.util.PageInfo;
import com.huabo.monitor.entity.Find;
import com.huabo.monitor.mysql.entity.TblyypriceMySql;

import java.math.BigDecimal;

public class TblyypriceMapperSqlMySqlConifg {


    public String selectListByPageInfo(PageInfo<TblyypriceMySql> pageInfo, Find find, BigDecimal orgid) {
        StringBuffer sqlSb = new StringBuffer("SELECT * FROM (SELECT BUDGET.* FROM (SELECT tpy.* from TBL_YY_PRICE typ where COMPANYID = " + orgid);
        if (find != null && find.getInterfacename() != null && find.getInterfacename().length() > 0) {
            sqlSb.append(" AND INTERFACENAME = '" + find.getInterfacename() + "' ");
        }
        sqlSb.append(" ORDER BY PRICEID ) BUDGET LIMIT " + pageInfo.getCurrentRecord() + " , " + pageInfo.getPageSize() + " ) as a ");
        String sql = sqlSb.toString();
        return sql;
    }

    public String selectCountByPageInfo(PageInfo<TblyypriceMySql> pageInfo, Find find, BigDecimal orgid) {
        String sqlCount = "select count(*) from TBL_YY_PRICE where COMPANYID=" + orgid;
        if (find != null && find.getInterfacename() != null && find.getInterfacename().length() > 0) {
            sqlCount += " and  INTERFACENAME ='" + find.getInterfacename() + "' ";
        }
        sqlCount += " order by PRICEID ";
        return sqlCount;
    }

    public String selectRepearBudgetName(String budgetname, BigDecimal orgid, String budgetId) {
        String sql = "SELECT COUNT(0) FROM TBL_CYHW_PROJECTBUDGET WHERE BUDGETNAME ='" + budgetname + "' AND ORGID = '" + orgid + "'";
        if (budgetId != null) {
            sql += " AND BUDGETID != " + budgetId;
        }
        return sql;
    }


}
