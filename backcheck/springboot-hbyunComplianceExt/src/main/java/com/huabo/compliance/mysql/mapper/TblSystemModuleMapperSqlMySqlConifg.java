package com.huabo.compliance.mysql.mapper;

import com.hbfk.util.DateUtil;
import com.hbfk.util.PageInfo;
import com.huabo.compliance.mysql.entity.TblSystemModuleMySql;

import java.math.BigDecimal;

public class TblSystemModuleMapperSqlMySqlConifg {

    public String selectPageInfoByOrgId(PageInfo<TblSystemModuleMySql> pageInfo, BigDecimal orgid, TblSystemModuleMySql module) throws Exception {
        StringBuffer sqlSb = new StringBuffer("SELECT * FROM (SELECT BUDGET.* FROM (SELECT MODELID,MODELNAME,MODELSTATUS,MODELTYPE,MODELNO,TS.REALNAME createPersonName,T1.CREATETIME FROM " +
                "(SELECT MODELID,MODELNAME,MODELSTATUS,MODELTYPE,MODELNO,CREATEPERSON,CREATETIME " +
                "FROM TBL_SYSTEM_MODULE WHERE MODELORG = " + orgid + " AND MODELTYPE IN ('nk','fxgl','nbsj','znjk','fcobc') union ALL" +
                " SELECT MODELID,MODELNAME,MODELSTATUS,TMR.RIGHTNAME,MODELNO,TSM.CREATEPERSON,TSM.CREATETIME " +
                "FROM TBL_SYSTEM_MODULE TSM " +
                "LEFT JOIN TBL_MANAGE_RIGHT TMR ON TSM.MODELTYPE = TMR.RIGHTID " +
                "WHERE MODELORG = " + orgid + " AND MODELTYPE NOT IN ('nk','fxgl','nbsj','znjk','fcobc')) T1 " +
                "LEFT JOIN TBL_STAFF TS ON CREATEPERSON = TS.STAFFID WHERE 1 = 1");

        if (module.getModelNo() != null && !"".equals(module.getModelNo())) {
            sqlSb.append(" AND MODELNO LIKE '%" + module.getModelNo() + "%'");
        }
        if (module.getModelName() != null && !"".equals(module.getModelName())) {
            sqlSb.append(" AND MODELNAME LIKE '%" + module.getModelName() + "%'");
        }
        if (module.getModelStatus() != null && module.getModelStatus() != -1) {
            sqlSb.append(" AND MODELSTATUS = " + module.getModelStatus() + "");
        }
        sqlSb.append(" ORDER BY MODELSTATUS DESC,MODELID ASC ) BUDGET LIMIT " + pageInfo.getCurrentRecord() + " , " + pageInfo.getPageSize() + " ) as a ");

        return sqlSb.toString();
    }

    public String selectCountByPageInfo(BigDecimal orgid, TblSystemModuleMySql module) throws Exception {

        String sqlCount = "SELECT COUNT(*) FROM TBL_SYSTEM_MODULE WHERE MODELORG = " + orgid;

        if (module.getModelNo() != null && !"".equals(module.getModelNo())) {
            sqlCount += " AND MODELNO LIKE '%" + module.getModelNo() + "%'";
        }

        if (module.getModelName() != null && !"".equals(module.getModelName())) {
            sqlCount += " AND MODELNAME LIKE '%" + module.getModelName() + "%'";
        }

        if (module.getModelStatus() != null && module.getModelStatus() != -1) {
            sqlCount += " AND MODELSTATUS = " + module.getModelStatus();
        }
        return sqlCount;
    }

    public String insertSystemModule(TblSystemModuleMySql module) {
        StringBuffer column = new StringBuffer("INSERT INTO TBL_SYSTEM_MODULE (MODELID");
        StringBuffer value = new StringBuffer(" VALUES (" +module.getModelId() + " ");

        if (module.getModelName() != null) {
            column.append(",MODELNAME");
            value.append(",'" + module.getModelName() + "'");
        }
        if (module.getModelUrl() != null) {
            column.append(",MODELURL");
            value.append(",'" + module.getModelUrl() + "'");
        }
        if (module.getModelStatus() != null) {
            column.append(",MODELSTATUS");
            value.append(",'" + module.getModelStatus() + "'");
        }
        if (module.getModelType() != null) {
            column.append(",MODELTYPE");
            value.append(",'" + module.getModelType() + "'");
        }
        if (module.getModelOrg() != null) {
            column.append(",MODELORG");
            value.append(",'" + module.getModelOrg() + "'");
        }
        if (module.getModelNo() != null) {
            column.append(",MODELNO");
            value.append(",'" + module.getModelNo() + "'");
        }
        if (module.getCreatePerson() != null) {
            column.append(",CREATEPERSON");
            value.append(",'" + module.getCreatePerson() + "'");
        }
        if (module.getCreateTime() != null) {
            column.append(",CREATETIME");
            value.append(",'" + DateUtil.parseDate(module.getCreateTime(), "yyyy-MM-dd HH:mm:ss") + "'");
        }
        if (module.getModifyTime() != null) {
            column.append(",MODIFYTIME");
            value.append(",'" + DateUtil.parseDate(module.getModifyTime(), "yyyy-MM-dd HH:mm:ss") + "'");
        }
        if (module.getModifyPerson() != null) {
            column.append(",MODIFYPERSON");
            value.append(",'" + module.getModifyPerson() + "'");
        }
        if (module.getModelorder() != null) {
            column.append(",MODELORDER");
            value.append(",'" + module.getModelorder() + "'");
        }
        column.append(")");
        value.append(")");
        String sql = column.toString() + value.toString();
        return sql;
    }

    public String updateByModule(TblSystemModuleMySql oldModule) {
        StringBuffer sql = new StringBuffer("UPDATE TBL_SYSTEM_MODULE SET MODELNAME = '" + oldModule.getModelName() + "'");

        if (oldModule.getModelUrl() != null && !"".equals(oldModule.getModelUrl())) {
            sql.append(" , MODELURL = '" + oldModule.getModelUrl() + "'");
        }
        if (oldModule.getModelStatus() != null && !"".equals(oldModule.getModelStatus())) {
            sql.append(" , MODELSTATUS = '" + oldModule.getModelStatus() + "'");
        }
        if (oldModule.getModelType() != null && !"".equals(oldModule.getModelType())) {
            sql.append(" , MODELTYPE = '" + oldModule.getModelType() + "'");
        }
        if (oldModule.getModelOrg() != null && !"".equals(oldModule.getModelOrg())) {
            sql.append(" , MODELORG = '" + oldModule.getModelOrg() + "'");
        }
        if (oldModule.getModelNo() != null && !"".equals(oldModule.getModelNo())) {
            sql.append(" , MODELNO = '" + oldModule.getModelNo() + "'");
        }
        if (oldModule.getCreateTime() != null) {
            sql.append(" ,CREATETIME = '" + DateUtil.parseDate(oldModule.getCreateTime(), "yyyy-MM-dd HH:mm:ss") + "'");
        }
        if (oldModule.getCreatePerson() != null && !"".equals(oldModule.getCreatePerson())) {
            sql.append(" , CREATEPERSON = '" + oldModule.getCreatePerson() + "'");
        }
        if (oldModule.getModifyTime() != null) {
            sql.append(" ,MODIFYTIME = '" + DateUtil.parseDate(oldModule.getModifyTime(), "yyyy-MM-dd HH:mm:ss") + "'");
        }
        if (oldModule.getModifyPerson() != null && !"".equals(oldModule.getModifyPerson())) {
            sql.append(" , MODIFYPERSON = '" + oldModule.getModifyPerson() + "'");
        }
        if (oldModule.getModelorder() != null && !"".equals(oldModule.getModelorder())) {
            sql.append(" , MODELORDER = '" + oldModule.getModelorder() + "'");
        }
        sql.append(" WHERE MODELID = '" + oldModule.getModelId() + "'");
        return sql.toString();
    }


}
