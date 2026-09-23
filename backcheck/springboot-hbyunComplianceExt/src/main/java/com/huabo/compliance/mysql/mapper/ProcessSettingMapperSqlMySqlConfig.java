package com.huabo.compliance.mysql.mapper;

import com.hbfk.util.PageInfo;
import com.huabo.compliance.mysql.entity.ProcessSettingMySql;

import java.math.BigDecimal;

public class ProcessSettingMapperSqlMySqlConfig {

    public String selectListByPageInfo(PageInfo<ProcessSettingMySql> pageInfo, BigDecimal orgid, BigDecimal companyid) {
        StringBuffer sqlSb = new StringBuffer("select * from  TBL_PROCESS_SETTING t where COMPANYID= #{companyid} or ORGID=#{orgid}  order by settingId desc  LIMIT " + pageInfo.getCurrentRecord() + "," + pageInfo.getPageSize());

        return sqlSb.toString();
    }

    public String selctOrgid(PageInfo<ProcessSettingMySql> pageInfo, BigDecimal orgid) {
        StringBuffer sbSql = new StringBuffer("SELECT S.*,O.ORGNAME from TBL_PROCESS_SETTING S LEFT JOIN TBL_ORGANIZATION O ON S.ORGID = O.ORGID WHERE S.COMPANYID = #{orgid}  order by S.SETTINGID desc LIMIT " + pageInfo.getCurrentRecord() + "," + pageInfo.getPageSize());
        return sbSql.toString();
    }


    public String updateByProcessSetting(ProcessSettingMySql processSetting) {
        StringBuffer sql = new StringBuffer("UPDATE TBL_PROCESS_SETTING SET MODULE = '" + processSetting.getModule() + "'");

        if (processSetting.getStatus() != null && !"".equals(processSetting.getStatus())) {
            sql.append(" , STATUS = '" + processSetting.getStatus() + "'");
        }
        if (processSetting.getRemark() != null && !"".equals(processSetting.getRemark())) {
            sql.append(" , REMARK = '" + processSetting.getRemark() + "'");
        }
        if (processSetting.getOrgid() != null && !"".equals(processSetting.getOrgid())) {
            sql.append(" , ORGID = '" + processSetting.getOrgid() + "'");
        }
        if (processSetting.getCompanyid() != null && !"".equals(processSetting.getCompanyid())) {
            sql.append(" , COMPANYID = '" + processSetting.getCompanyid() + "'");
        }
        sql.append(" WHERE SETTINGID = '" + processSetting.getSettingId() + "'");
        return sql.toString();
    }

    public String savemerge(ProcessSettingMySql setting) {
        StringBuffer column = new StringBuffer("INSERT INTO TBL_PROCESS_SETTING (SETTINGID");
        StringBuffer value = new StringBuffer(" VALUES ( " + setting.getSettingId() + " ");

        if (setting.getModule() != null) {
            column.append(",MODULE");
            value.append(",'" + setting.getModule() + "'");
        }
        if (setting.getStatus() != null) {
            column.append(",STATUS");
            value.append(",'" + setting.getStatus() + "'");
        }
        if (setting.getRemark() != null) {
            column.append(",REMARK");
            value.append(",'" + setting.getRemark() + "'");
        }
        if (setting.getOrgid() != null) {
            column.append(",ORGID");
            value.append(",'" + setting.getOrgid() + "'");
        }
        if (setting.getCompanyid() != null) {
            column.append(",COMPANYID");
            value.append(",'" + setting.getCompanyid() + "'");
        }
        column.append(")");
        value.append(")");
        String sql = column.toString() + value.toString();
        return sql;
    }
}
