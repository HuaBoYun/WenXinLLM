package com.huabo.system.mapper;

import com.huabo.system.entity.TblJob;
import com.huabo.system.entity.TblManageUserBook;
import com.huabo.system.entity.TblOrganization;

public class TblManageUserBookMapperSqlConfig {


    public String saveEntity(TblManageUserBook manage) {
        StringBuffer column = new StringBuffer("INSERT INTO TBL_MANAGE_USER_BOOK (STAFFID");
        StringBuffer value = new StringBuffer(" VALUES ('"+manage.getStaffid()+"'");

        if(manage.getBookid() != null) {
            column.append(",BOOKID");
            value.append(",'"+manage.getBookid()+"'");
        }
        if(manage.getStatus() != null) {
            column.append(",STATUS");
            value.append(",'"+manage.getStatus()+"'");
        }

        column.append(")");
        value.append(")");
        String sql = column.toString()+value.toString();
        return sql;
    }


}
