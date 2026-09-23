package com.huabo.system.mappersql;

import com.huabo.system.entity.TblFlowBussiness;

public class TblFlowBussinessMapperSqlConfig {


    public String findByBussinessnumber(String flowid, TblFlowBussiness bussiness) {
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
