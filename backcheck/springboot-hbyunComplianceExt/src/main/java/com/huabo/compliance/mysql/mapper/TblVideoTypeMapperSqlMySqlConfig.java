package com.huabo.compliance.mysql.mapper;

import com.hbfk.util.PageInfo;
import com.huabo.compliance.mysql.entity.TblVideoTypeMySql;
public class TblVideoTypeMapperSqlMySqlConfig {


    public String selectListByPageInfo(PageInfo<TblVideoTypeMySql> pageInfo, Integer orgid) {

        StringBuffer sqlSb = new StringBuffer("SELECT * FROM (SELECT BUDGET.* FROM (select TVT.* from TBL_VIDEO_TYPE TVT WHERE 1=1 AND ORGID = " + orgid);
        sqlSb.append(" ) BUDGET LIMIT " + pageInfo.getCurrentRecord() + " , " + pageInfo.getPageSize() + " ) AS A ");
        String sql = sqlSb.toString();
        return sql;
    }

    public String insertTblVideoType(TblVideoTypeMySql tnt) {
        StringBuffer column = new StringBuffer("INSERT INTO TBL_VIDEO_TYPE (TYPEID");
        StringBuffer value = new StringBuffer(" VALUES (" +tnt.getTypeId()+ " ");

        if (tnt.getTypename() != null) {
            column.append(",TYPENAME");
            value.append(",'" + tnt.getTypename() + "'");
        }
        if (tnt.getVersion() != null) {
            column.append(",VERSION");
            value.append(",'" + tnt.getVersion() + "'");
        }
        if (tnt.getOrgid() != null) {
            column.append(",ORGID");
            value.append(",'" + tnt.getOrgid() + "'");
        }
        if (tnt.getType() != null) {
            column.append(",TYPE");
            value.append(",'" + tnt.getType() + "'");
        }

        column.append(")");
        value.append(")");
        String sql = column.toString() + value.toString();
        return sql;
    }

    public String updateTblVideoType(TblVideoTypeMySql tnt) {
        StringBuffer sql = new StringBuffer("UPDATE TBL_VIDEO_TYPE SET TYPENAME = '" + tnt.getTypename() + "'");

        if (tnt.getVersion() != null && !"".equals(tnt.getVersion())) {
            sql.append(" , VERSION = '" + tnt.getVersion() + "'");
        }
        if (tnt.getOrgid() != null && !"".equals(tnt.getOrgid())) {
            sql.append(" , ORGID = '" + tnt.getOrgid() + "'");
        }
        if (tnt.getType() != null && !"".equals(tnt.getType())) {
            sql.append(" , TYPE = '" + tnt.getType() + "'");
        }
        sql.append(" WHERE TYPEID = '" + tnt.getTypeId() + "'");
        return sql.toString();
    }
}
