package com.huabo.monitor.mysql.mapper;

import com.hbfk.util.PageInfo;
import com.huabo.monitor.mysql.entity.TblContractTypeofMySql;

public class TblContractTypeofMapperSqlMySqlConfig {

    public String selectPageInfoList(PageInfo<TblContractTypeofMySql> pageInfo) {
        TblContractTypeofMySql condition = pageInfo.getCondition();
        StringBuffer sqlSb = new StringBuffer("SELECT * FROM (SELECT BUDGET.* FROM (SELECT TCT.* from TBL_CONTRACT_TYPEOF TCT WHERE ORGID = " + condition.getOrgId());

        if (condition.getTypeName() != null && !"".equals(condition.getTypeName())) {
            sqlSb.append(" AND TYPENAME LIKE '%" + condition.getTypeName() + "%'");
        }
        if (condition.getTypeId() != null) {
            sqlSb.append(" AND PARENTID = " + condition.getTypeId() + "");
        } else {
            sqlSb.append(" AND PARENTID IS NULL");
        }
        sqlSb.append(" ORDER BY TYPEID DESC ) BUDGET LIMIT " + pageInfo.getCurrentRecord() + " , " + pageInfo.getPageSize() + " ) AS A " );
        String sql = sqlSb.toString();
        return sql;
    }

    public String selectPageInfoCount(PageInfo<TblContractTypeofMySql> pageInfo) {
        TblContractTypeofMySql condition = pageInfo.getCondition();
        String sqlCount = "SELECT COUNT(*) FROM TBL_CONTRACT_TYPEOF WHERE ORGID = '" + condition.getOrgId() + "'";

        if (condition.getTypeName() != null && !"".equals(condition.getTypeName())) {
            sqlCount += " AND TYPENAME LIKE '%" + condition.getTypeName() + "%'";
        }
        if (condition.getTypeId() != null) {
            sqlCount += " AND PARENTID = " + condition.getTypeId();
        } else {
            sqlCount += " AND PARENTID IS NULL ";
        }
        return sqlCount;
    }


}
