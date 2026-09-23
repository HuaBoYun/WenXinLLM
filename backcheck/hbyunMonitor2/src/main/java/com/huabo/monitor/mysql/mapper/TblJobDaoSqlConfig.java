package com.huabo.monitor.mysql.mapper;

import com.hbfk.util.PageInfo;
import com.huabo.monitor.mysql.entity.TblJobMySql;

import java.math.BigDecimal;


public class TblJobDaoSqlConfig {

    public String selectListByPageInfo(PageInfo<TblJobMySql> pageInfo, BigDecimal companyId) {
        StringBuffer sbSql = new StringBuffer("SELECT * FROM (SELECT BUDGET.* FROM(SELECT TJ.* FROM TBL_JOB TJ where COMPANYID= " + companyId + "");
        sbSql.append(" ) BUDGET LIMIT " + pageInfo.getCurrentRecord() + " , " + pageInfo.getPageSize() + " ) as a ");
        return sbSql.toString();
    }

    public String saveJob(TblJobMySql job) {
        StringBuffer column = new StringBuffer("INSERT INTO TBL_JOB (JOBID");
        StringBuffer value = new StringBuffer(" VALUES ( " + job.getJobid() +"");

        if (job.getJobname() != null) {
            column.append(",JOBNAME");
            value.append(",'" + job.getJobname() + "'");
        }
        if (job.getCompanyId() != null) {
            column.append(",COMPANYID");
            value.append(",'" + job.getCompanyId() + "'");
        }
        column.append(")");
        value.append(")");
        String sql = column.toString() + value.toString();
        return sql;
    }

    public String updateJob(TblJobMySql newJob) {
        StringBuffer sql = new StringBuffer("UPDATE TBL_JOB SET ");

        if (newJob.getJobname() != null && !"".equals(newJob.getJobname())) {
            sql.append("  JOBNAME = '" + newJob.getJobname() + "'");
        }
        if (newJob.getCompanyId() != null && !"".equals(newJob.getCompanyId())) {
            sql.append(" , COMPANYID = '" + newJob.getCompanyId() + "'");
        }

        sql.append(" WHERE JOBID = '" + newJob.getJobid() + "'");
        return sql.toString();
    }


}