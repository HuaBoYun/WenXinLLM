package com.huabo.system.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hbfk.util.PageInfo;
import com.hbfk.util.redis.Random.RandomUtil;
import com.huabo.system.entity.Find;
import com.huabo.system.entity.TblInteriorExpert;
import com.huabo.system.entity.TblJob;

import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;


public class TblInteriorExpertDaoSqlConfig {

    public String selectListByPageInfoo(IPage<TblInteriorExpert> page, BigDecimal orgid, Find find) {
        StringBuffer sqlSb = new StringBuffer("SELECT ie.*,s.REALNAME,o.ORGNAME FROM TBL_INTERIOR_EXPERT ie " +
                " LEFT JOIN TBL_STAFF s ON ie.STAFFID = S.STAFFID" +
                " LEFT JOIN TBL_ORGANIZATION o ON s.ORGID = o.ORGID WHERE 1=1 AND ie.ORGID = "+orgid);
        if(find.getUserName()!=null&&!find.getUserName().isEmpty()){
            sqlSb.append(" AND s.REALNAME LIKE '%"+find.getUserName()+"%'");
        }
        sqlSb.append(" ORDER BY ie.INTERIORID DESC");
        String sql = sqlSb.toString();
        return sql;
    }

    public String selectListByPageInfoCount(PageInfo<TblInteriorExpert> pageInfo, BigDecimal orgid, Find find) {
        String sqlCount = "SELECT COUNT(*) FROM TBL_INTERIOR_EXPERT ie LEFT JOIN TBL_STAFF s ON ie.STAFFID = S.STAFFID WHERE 1=1 AND ie.ORGID = "+orgid;

        if(find.getUserName()!=null&&!find.getUserName().isEmpty()) {
            sqlCount += " AND s.REALNAME LIKE '%"+find.getUserName()+"%'";
        }
        return sqlCount;
    }


    public String insertInteriorExpert(TblInteriorExpert tie) {
        StringBuffer column = new StringBuffer("INSERT INTO TBL_INTERIOR_EXPERT (INTERIORID");
        StringBuffer value = new StringBuffer(" VALUES ("+RandomUtil.uuBigDecimalId());

        if(tie.getQualification() != null) {
            column.append(",QUALIFICATION");
            value.append(",'"+tie.getQualification()+"'");
        }
        if(tie.getPosition() != null) {
            column.append(",POSITION");
            value.append(",'"+tie.getPosition()+"'");
        }
        if(tie.getProfessional() != null) {
            column.append(",PROFESSIONAL");
            value.append(",'"+tie.getProfessional()+"'");
        }
        if(tie.getOrgId() != null) {
            column.append(",ORGID");
            value.append(",'"+tie.getOrgId()+"'");
        }
        if(tie.getStaffid() != null) {
            column.append(",STAFFID");
            value.append(",'"+tie.getStaffid()+"'");
        }
        column.append(")");
        value.append(")");
        String sql = column.toString()+value.toString();
        return sql;
    }

    public String updateInteriorExpert(TblInteriorExpert tblinter) {
        StringBuffer sql = new StringBuffer("UPDATE TBL_INTERIOR_EXPERT SET ");


        if(tblinter.getPosition() != null && !"".equals(tblinter.getPosition())) {
            sql.append(" POSITION = '"+tblinter.getPosition()+"'");
        }
        if(tblinter.getQualification() != null && !"".equals(tblinter.getQualification())) {
            sql.append(",  QUALIFICATION = '"+tblinter.getQualification()+"'");
        }
        if(tblinter.getProfessional() != null && !"".equals(tblinter.getProfessional())) {
            sql.append(" , PROFESSIONAL = '"+tblinter.getProfessional()+"'");
        }
        if(tblinter.getOrgId() != null ) {
            sql.append(" , ORGID = '"+tblinter.getOrgId()+"'");
        }
        if(tblinter.getStaffid() != null) {
            sql.append(" , STAFFID = '"+tblinter.getStaffid()+"'");
        }

        sql.append(" WHERE INTERIORID = '"+tblinter.getInteriorid()+"'");
        return sql.toString();
    }
}
