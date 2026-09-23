package com.huabo.contract.mappersql;

import com.hbfk.util.DateUtil;
import com.hbfk.util.database.DataBaseSqlConfig;
import com.huabo.contract.entity.TblCyhwBasicuninspection;

public class TblCyhwBasicuninspectionMapperSqlConfig {


    public String insertBybudget(TblCyhwBasicuninspection tcp) throws Exception {
        StringBuffer column = new StringBuffer("INSERT INTO TBL_CYHW_BASICUNINSPECTION (INSPECTIONID");
        StringBuffer value = new StringBuffer(" VALUES ("+tcp.getInspectionid());

        if(tcp.getInspectionname() != null && !"".equals(tcp.getInspectionname())) {
            column.append(",INSPECTIONNAME");
            value.append(",'"+tcp.getInspectionname()+"'");
        }
        if(tcp.getInspectionfunc() != null && !"".equals(tcp.getInspectionfunc())) {
            column.append(",INSPECTIONFUNC");
            value.append(",'"+tcp.getInspectionfunc()+"'");
        }
        if(tcp.getFilename() != null && !"".equals(tcp.getFilename())) {
            column.append(",FILENAME");
            value.append(",'"+tcp.getFilename()+"'");
        }
        if(tcp.getFileno() != null && !"".equals(tcp.getFileno())) {
            column.append(",FILENO");
            value.append(",'"+tcp.getFileno()+"'");
        }
        if(tcp.getInspectionmoney() != null) {
            column.append(",INSPECTIONMONEY");
            value.append(",'"+tcp.getInspectionmoney()+"'");
        }
        if(tcp.getInspectiondate() != null) {
            column.append(",INSPECTIONDATE");
            value.append(","+ DataBaseSqlConfig.getDateStrFormat(tcp.getInspectiondate()));
        }
        if(tcp.getOrgid() != null) {
            column.append(",ORGID");
            value.append(",'"+tcp.getOrgid()+"'");
        }
        if(tcp.getCreateuser() != null ) {
            column.append(",CREATEUSER");
            value.append(",'"+tcp.getCreateuser()+"'");
        }
        if(tcp.getCreatetime() != null ) {
            column.append(",CREATETIME");
            value.append(","+ DataBaseSqlConfig.getDateStrFormat(tcp.getCreatetime()));
        }
        if(tcp.getFlowid() != null) {
            column.append(",FLOWID");
            value.append(",'"+tcp.getFlowid()+"'");
        }
        if(tcp.getLinkdepr() != null) {
            column.append(",LINKDEPR");
            value.append(",'"+tcp.getLinkdepr()+"'");
        }
        if(tcp.getInspectionstatus() != null) {
            column.append(",INSPECTIONSTATUS");
            value.append(",'"+tcp.getInspectionstatus()+"'");
        }
        if(tcp.getInspectiondept() != null) {
            column.append(",INSPECTIONDEPT");
            value.append(",'"+tcp.getInspectiondept()+"'");
        }

        column.append(")");
        value.append(")");
        String sql = column.toString()+value.toString();
        return sql;
    }
}
