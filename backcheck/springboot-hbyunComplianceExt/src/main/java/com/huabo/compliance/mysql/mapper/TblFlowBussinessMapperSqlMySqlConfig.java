package com.huabo.compliance.mysql.mapper;


import com.huabo.compliance.mysql.entity.TblFlowBussinessMySql;

public class TblFlowBussinessMapperSqlMySqlConfig {

    public String findByBussinessnumber(String flowid, TblFlowBussinessMySql bussiness) {
        String sql = "select * from TBL_FLOW_BUSSINESS where FLOWID in (" + flowid + ") ";
        if (bussiness != null && bussiness.getBussinessnumber() != null) {
            sql += " and bussinessnumber = " + bussiness.getBussinessnumber();
        }
        if (bussiness != null && bussiness.getBussinessname() != null) {
            sql += " and bussinessname like '%" + bussiness.getBussinessname() + "%'";
        }
        return sql.toString();
    }

}
