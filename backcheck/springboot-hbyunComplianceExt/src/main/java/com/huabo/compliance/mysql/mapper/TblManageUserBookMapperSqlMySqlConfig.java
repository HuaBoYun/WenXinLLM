package com.huabo.compliance.mysql.mapper;


import com.huabo.compliance.mysql.entity.TblManageUserBookMySql;

public class TblManageUserBookMapperSqlMySqlConfig {


    public String saveEntity(TblManageUserBookMySql manage) {
        StringBuffer column = new StringBuffer("INSERT INTO TBL_MANAGE_USER_BOOK (STAFFID");
        StringBuffer value = new StringBuffer(" VALUES ('" + manage.getStaffid() + "'");

        if (manage.getBookid() != null) {
            column.append(",BOOKID");
            value.append(",'" + manage.getBookid() + "'");
        }
        if (manage.getStatus() != null) {
            column.append(",STATUS");
            value.append(",'" + manage.getStatus() + "'");
        }


        column.append(")");
        value.append(")");
        String sql = column.toString() + value.toString();
        return sql;
    }


//    public String updateBySql(TblManageUserBook staffid) {
//        StringBuffer sql = new StringBuffer("UPDATE TBL_MANAGE_USER_BOOK SET ");
//
//        if(staffid.getBookid() != null && !"".equals(staffid.getBookid())) {
//            sql.append("  BOOKID = '"+staffid.getBookid()+"'");
//        }
//        if(staffid.getStatus() != null && !"".equals(staffid.getStatus())) {
//            sql.append(" , STATUS = '"+staffid.getStatus()+"'");
//        }
//
//        sql.append(" WHERE STAFFID = '"+staffid.getStaffid()+"'");
//        return sql.toString();
//    }
}
