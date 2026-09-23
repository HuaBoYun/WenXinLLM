package com.huabo.system.mapper;

import com.hbfk.util.PageInfo;
import com.huabo.system.entity.TblJob;
import com.huabo.system.entity.TblJobGrade;
import com.huabo.system.entity.TblRole;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;


public class TblJobGradeDaoSqlConfig {
 

    public String saveJobGrade(TblJobGrade job) {
        StringBuffer column = new StringBuffer("INSERT INTO TBL_JOB_GRADE (GRADEID");
        StringBuffer value = new StringBuffer(" VALUES (").append(job.getGradeid());

        if(job.getGradeCode() != null) {
            column.append(",GRADECODE");
            value.append(",'"+job.getGradeCode()+"'");
        }
        if(job.getAliasName() != null) {
            column.append(",AliasName");
            value.append(",'"+job.getAliasName()+"'");
        }
        if(job.getCategory() != null) {
            column.append(",Category");
            value.append(",'"+job.getCategory()+"'");
        }
        if(job.getCategoryName() != null) {
            column.append(",CategoryName");
            value.append(",'"+job.getCategoryName()+"'");
        }
        if(job.getDescription() != null) {
            column.append(",Description");
            value.append(",'"+job.getDescription()+"'");
        }
        if(job.getHistoryCode() != null) {
            column.append(",HistoryCode");
            value.append(",'"+job.getHistoryCode()+"'");
        }
        column.append(")");
        value.append(")");
        String sql = column.toString()+value.toString();
        return sql;
    }

    public String updateJobGrade(TblJobGrade job) {
        StringBuffer sql = new StringBuffer("UPDATE TBL_JOB_GRADE SET ");
        
        if(job.getGradeCode() != null) {
            sql.append(" GRADECODE = '"+job.getGradeCode()+"'");

        }
        if(job.getAliasName() != null) {
            sql.append(",AliasName = '"+job.getAliasName()+"'");
        }
        if(job.getCategory() != null) {
            sql.append(",Category = '"+job.getCategory()+"'");
        }
        if(job.getCategoryName() != null) {
            sql.append(",CategoryName = '"+job.getCategoryName()+"'");
        }
        if(job.getDescription() != null) {
            sql.append(" ,Description = '"+job.getDescription()+"'");
        }
        if(job.getHistoryCode() != null) {
            sql.append(",HistoryCode = '"+job.getHistoryCode()+"'");
        }
        sql.append(" WHERE Gradeid = '"+job.getGradeid()+"'");
        return sql.toString();
    }


}