package com.huabo.monitor.mysql.mapper;

import com.hbfk.util.PageInfo;
import com.huabo.monitor.entity.Find;
import com.huabo.monitor.mysql.entity.TblExternalExpertMySql;
import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;

public class TblExternalExpertDaoSqlMySqlConfig {

    //    public String selectListByPageInfoo(PageInfo<TblExternalExpert> pageInfo,String userName) {
//        StringBuffer sbSql = new StringBuffer("SELECT EXTERID,COMPANY,EXPERTISE,QUALIFICATION,STAFFID FROM ( ");
//        sbSql.append("SELECT T1.EXTERID,T1.COMPANY,T1.EXPERTISE,T1.QUALIFICATION,T1.STAFFID,@rownum := @rownum +1 RNUM FROM ( ");
//        sbSql.append("SELECT T1.EXTERID,T1.COMPANY,T1.EXPERTISE,T1.QUALIFICATION,T1.STAFFID,@rownum := @rownum +1 RNUM FROM ( ");
//        sbSql.append("SELECT * FROM TBL_EXTERNAL_EXPERT ee,TblStaff s where ee.staffid = s.staffid and s.tblOrganization.orgid = ?AND s.REALNAME like '% "+ userName +" %' ");
//        sbSql.append(") T1 WHERE @rownum := @rownum +1 <= "+(pageInfo.getCurrentPage()*pageInfo.getPageSize())+" ) T2 WHERE T2.RNUM > "+pageInfo.getCurrentRecord());
//        return sbSql.toString();
//    }
    public String selectTblExternal(PageInfo<TblExternalExpertMySql> pageInfo, BigDecimal orgid, Find find, String company) {
        StringBuffer sbSql = new StringBuffer("SELECT ee.*,ts.REALNAME FROM TBL_EXTERNAL_EXPERT ee " +
                "LEFT JOIN TBL_STAFF ts on ee.STAFFID =ts.STAFFID " +
                "LEFT JOIN TBL_ORGANIZATION tor ON ts.ORGID = tor.ORGID " +
                "WHERE tor.FATHERORGID = " + orgid);
        if (StringUtils.isNotBlank(find.getUserName())) {
            sbSql.append(" AND  ts.REALNAME LIKE '%" + find.getUserName() + "%' ");
        }
        if (StringUtils.isNotBlank(company)) {
            sbSql.append(" AND  ee.COMPANY LIKE '%" + company + "%' ");
        }
        sbSql.append(" LIMIT " + pageInfo.getCurrentRecord() + " , " + pageInfo.getPageSize());
        return sbSql.toString();
    }

    public String selectExternal(BigDecimal orgid, Find find, String company) {
        StringBuffer sbSql = new StringBuffer("SELECT COUNT(*) FROM TBL_EXTERNAL_EXPERT ee " +
                "LEFT JOIN TBL_STAFF ts on ee.STAFFID =ts.STAFFID " +
                "LEFT JOIN TBL_ORGANIZATION tor ON ts.ORGID = tor.ORGID " +
                "WHERE tor.FATHERORGID = " + orgid);
        if (StringUtils.isNotBlank(find.getUserName())) {
            sbSql.append(" AND  ts.REALNAME LIKE '%" + find.getUserName() + "%' ");
        }
        if (StringUtils.isNotBlank(company)) {
            sbSql.append(" AND  ee.COMPANY LIKE '%" + company + "%' ");
        }
        // sbSql.append(") BUDGET WHERE rownum <= "+(pageInfo.getCurrentPage()*pageInfo.getPageSize())+" ) WHERE RNUM > "+pageInfo.getCurrentRecord());
        return sbSql.toString();
    }


    public String updateTblExternalExpert(TblExternalExpertMySql tee) {
        StringBuffer sql = new StringBuffer("UPDATE TBL_EXTERNAL_EXPERT SET ");

        if (tee.getCompany() != null && !"".equals(tee.getCompany())) {
            sql.append("  COMPANY = '" + tee.getCompany() + "'");
        }
        if (tee.getExpertise() != null && !"".equals(tee.getExpertise())) {
            sql.append(" , EXPERTISE = '" + tee.getExpertise() + "'");
        }
        if (tee.getQualification() != null && !"".equals(tee.getQualification())) {
            sql.append(" , QUALIFICATION = '" + tee.getQualification() + "'");
        }
        if (tee.getStaffid() != null && !"".equals(tee.getStaffid())) {
            sql.append(" , STAFFID = '" + tee.getStaffid() + "'");
        }

        sql.append(" WHERE EXTERID = '" + tee.getExterid() + "'");
        return sql.toString();
    }


    public String saveTblExternalExpert(TblExternalExpertMySql tee) {
        StringBuffer column = new StringBuffer("INSERT INTO TBL_EXTERNAL_EXPERT (EXTERID");
        StringBuffer value = new StringBuffer(" VALUES ( " +tee.getExterid() +" ");

        if (tee.getCompany() != null) {
            column.append(",COMPANY");
            value.append(",'" + tee.getCompany() + "'");
        }
        if (tee.getExpertise() != null) {
            column.append(",EXPERTISE");
            value.append(",'" + tee.getExpertise() + "'");
        }
        if (tee.getQualification() != null) {
            column.append(",QUALIFICATION");
            value.append(",'" + tee.getQualification() + "'");
        }
        if (tee.getStaffid() != null) {
            column.append(",STAFFID");
            value.append(",'" + tee.getStaffid() + "'");
        }
        column.append(")");
        value.append(")");
        String sql = column.toString() + value.toString();
        return sql;
    }
}
