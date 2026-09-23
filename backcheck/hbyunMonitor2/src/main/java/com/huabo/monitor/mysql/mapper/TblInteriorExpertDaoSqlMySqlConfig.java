package com.huabo.monitor.mysql.mapper;

import com.hbfk.util.PageInfo;
import com.huabo.monitor.entity.Find;
import com.huabo.monitor.entity.TblInteriorExpert;
import com.huabo.monitor.mysql.entity.TblInteriorExpertMySql;

import java.math.BigDecimal;


public class TblInteriorExpertDaoSqlMySqlConfig {

    public String selectListByPageInfoo(PageInfo<TblInteriorExpertMySql> pageInfo, BigDecimal orgid, Find find) {
        StringBuffer sqlSb = new StringBuffer("SELECT * FROM (SELECT BUDGET.* FROM (SELECT ie.*,s.REALNAME,o.ORGNAME FROM TBL_INTERIOR_EXPERT ie " +
                " LEFT JOIN TBL_STAFF s ON ie.STAFFID = S.STAFFID" +
                " LEFT JOIN TBL_ORGANIZATION o ON s.ORGID = o.ORGID WHERE 1=1 AND ie.ORGID = " + orgid);
        if (find.getUserName() != null && !find.getUserName().isEmpty()) {
            sqlSb.append(" AND s.REALNAME LIKE '%" + find.getUserName() + "%'");
        }
        sqlSb.append(" ) BUDGET LIMIT " + pageInfo.getCurrentRecord() + " , " + pageInfo.getPageSize());
        String sql = sqlSb.toString();
        return sql;
    }

    public String selectListByPageInfoCount(PageInfo<TblInteriorExpertMySql> pageInfo, BigDecimal orgid, Find find) {
        String sqlCount = "SELECT COUNT(*) FROM TBL_INTERIOR_EXPERT ie LEFT JOIN TBL_STAFF s ON ie.STAFFID = S.STAFFID WHERE 1=1 AND ie.ORGID = " + orgid;

        if (find.getUserName() != null && !find.getUserName().isEmpty()) {
            sqlCount += " AND s.REALNAME LIKE '%" + find.getUserName() + "%'";
        }
        return sqlCount;
    }


    public String insertInteriorExpert(TblInteriorExpert tie) {
        StringBuffer column = new StringBuffer("INSERT INTO TBL_INTERIOR_EXPERT (INTERIORID");
        StringBuffer value = new StringBuffer(" VALUES ("+tie.getInteriorid()+"");

        if (tie.getQualification() != null) {
            column.append(",QUALIFICATION");
            value.append(",'" + tie.getQualification() + "'");
        }
        if (tie.getPosition() != null) {
            column.append(",POSITION");
            value.append(",'" + tie.getPosition() + "'");
        }
        if (tie.getProfessional() != null) {
            column.append(",PROFESSIONAL");
            value.append(",'" + tie.getProfessional() + "'");
        }
        if (tie.getOrgId() != null) {
            column.append(",ORGID");
            value.append(",'" + tie.getOrgId() + "'");
        }
        if (tie.getStaffid() != null) {
            column.append(",STAFFID");
            value.append(",'" + tie.getStaffid() + "'");
        }
        column.append(")");
        value.append(")");
        String sql = column.toString() + value.toString();
        return sql;
    }

    public String updateInteriorExpert(TblInteriorExpert tblinter) {
        StringBuffer sql = new StringBuffer("UPDATE TBL_INTERIOR_EXPERT SET ");


        if (tblinter.getPosition() != null && !"".equals(tblinter.getPosition())) {
            sql.append(" POSITION = '" + tblinter.getPosition() + "'");
        }
        if (tblinter.getQualification() != null && !"".equals(tblinter.getQualification())) {
            sql.append(",  QUALIFICATION = '" + tblinter.getQualification() + "'");
        }
        if (tblinter.getProfessional() != null && !"".equals(tblinter.getProfessional())) {
            sql.append(" , PROFESSIONAL = '" + tblinter.getProfessional() + "'");
        }
        if (tblinter.getOrgId() != null && !"".equals(tblinter.getOrgId())) {
            sql.append(" , ORGID = '" + tblinter.getOrgId() + "'");
        }
        if (tblinter.getStaffid() != null && !"".equals(tblinter.getStaffid())) {
            sql.append(" , STAFFID = '" + tblinter.getStaffid() + "'");
        }

        sql.append(" WHERE INTERIORID = '" + tblinter.getInteriorid() + "'");
        return sql.toString();
    }
}
