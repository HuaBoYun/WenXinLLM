package com.huabo.monitor.mysql.mapper;

import com.hbfk.util.PageInfo;
import com.huabo.monitor.entity.TblRole;
import com.huabo.monitor.mysql.entity.TblRoleMySql;

import java.math.BigDecimal;


public class TblRoleDaoSqlMySqlConfig {

    public String selectListByPageInfo(PageInfo<TblRoleMySql> pageInfo, BigDecimal companyid, TblRole role) {

        StringBuffer sbSql = new StringBuffer("SELECT * from (SELECT BUDGET.* FROM (SELECT TR.* FROM TBL_ROLE TR WHERE 1=1 and companyid = " + companyid);
        if (role != null) {
            if (role.getRid() != null) {
                sbSql.append(" and rid like '%" + role.getRid() + "%'");
            }

            if (role.getRname() != null && !role.getRname().equals("")) {
                sbSql.append(" and RNAME like '%" + role.getRname() + "%'");
            }
        }

        sbSql.append(" ORDER BY RID DESC) BUDGET LIMIT " + pageInfo.getCurrentRecord() + " , " + pageInfo.getPageSize() + " ) AS A ");
        String sql = sbSql.toString();
        return sql;
    }

//    public String selectListByPageInfoCount(PageInfo<TblRole> pageInfo, TblRole role) {
//        StringBuffer sbSql = new StringBuffer(" SELECT COUNT(*) from (SELECT BUDGET.*,@rownum := @rownum +1 RNUM FROM (SELECT * FROM TBL_ROLE WHERE 1=1 and companyid = " + role.getCompanyid() + " and rid like '%" + role.getRid() + "%' and RNAME like '%" + role.getRname() + "%'");
//        sbSql.append(") BUDGET WHERE @rownum := @rownum +1 <= "+(pageInfo.getCurrentPage()*pageInfo.getPageSize())+" ) WHERE RNUM > "+pageInfo.getCurrentRecord());
//        return sbSql.toString();
//    }

    public String saveTblRole(TblRole tr) {
        StringBuffer column = new StringBuffer("INSERT INTO TBL_ROLE (RID");
        StringBuffer value = new StringBuffer(" VALUES ( " + tr.getRid() + "");

        if (tr.getRname() != null) {
            column.append(",RNAME");
            value.append(",'" + tr.getRname() + "'");
        }
        if (tr.getRdesc() != null) {
            column.append(",RDESC");
            value.append(",'" + tr.getRdesc() + "'");
        }
        if (tr.getRstatus() != null) {
            column.append(",RSTATUS");
            value.append(",'" + tr.getRstatus() + "'");
        }
        if (tr.getCompanyid() != null) {
            column.append(",COMPANYID");
            value.append(",'" + tr.getCompanyid() + "'");
        }

        column.append(")");
        value.append(")");
        String sql = column.toString() + value.toString();
        return sql;
    }


    public String updateTblRole(TblRole role) {
        StringBuffer sql = new StringBuffer("UPDATE TBL_ROLE SET RNAME = '" + role.getRname() + "' ");

//        if(role.getRname() != null && !"".equals(role.getRname())) {
//            sql.append(" , RNAME = '"+role.getRname()+"'");
//        }
        if (role.getRdesc() != null && !"".equals(role.getRdesc())) {
            sql.append(" , RDESC = '" + role.getRdesc() + "'");
        }
        if (role.getRstatus() != null && !"".equals(role.getRstatus())) {
            sql.append(" , RSTATUS = '" + role.getRstatus() + "'");
        }
        if (role.getCompanyid() != null && !"".equals(role.getCompanyid())) {
            sql.append(" , COMPANYID = '" + role.getCompanyid() + "'");
        }
        sql.append(" WHERE RID = '" + role.getRid() + "'");
        return sql.toString();
    }
}