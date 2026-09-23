package com.huabo.system.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hbfk.util.PageInfo;
import com.hbfk.util.database.DataBaseSqlConfig;
import com.huabo.system.entity.TblJob;
import com.huabo.system.entity.TblRole;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;


public class TblJobDaoSqlConfig {

	public String selectListByPageInfo(IPage<TblJob> page, BigDecimal companyId, String orgIds, String jobName, String orgName) throws Exception {
        StringBuffer sbSql = new StringBuffer("SELECT JOB.*,"+DataBaseSqlConfig.getConcatColumn(DataBaseSqlConfig.getConcatColumn("JOB.JOBNAME", "'-'"),"ORG.ORGNAME")+" AS JLONGNAME,ORG.ORGNAME AS ORGNAME FROM TBL_JOB JOB LEFT JOIN TBL_ORGANIZATION ORG ON JOB.COMPANYID = ORG.ORGID where ");
       
        if(orgIds == null) {
        	sbSql.append(" JOB.COMPANYID = "+companyId);
        }else {
        	sbSql.append(" JOB.COMPANYID IN ("+orgIds+")");
        }
        
        if(jobName != null && !"".equals(jobName)) {
        	sbSql.append(" AND JOB.JOBNAME LIKE '%"+jobName+"%'");
        }
        if(orgName != null && !"".equals(orgName)) {
        	sbSql.append(" AND ORG.ORGNAME LIKE '%"+orgName+"%'");
        }
        sbSql.append(" ORDER BY JOB.JOBID DESC ");
        return sbSql.toString();
    }


    public String saveJob(TblJob job) throws Exception {
        StringBuffer column = new StringBuffer("INSERT INTO TBL_JOB (JOBID");
        StringBuffer value = new StringBuffer(" VALUES ("+job.getJobid());

        if(job.getJobname() != null) {
            column.append(",JOBNAME");
            value.append(",'"+job.getJobname()+"'");
        }
        if(job.getCompanyId() != null) {
            column.append(",COMPANYID");
            value.append(",'"+job.getCompanyId()+"'");
        }
        if(job.getHistorycode() != null) {
            column.append(",historycode");
            value.append(",'"+job.getHistorycode()+"'");
        }
        if(job.getDatasource() != null) {
            column.append(",datasource");
            value.append(",'"+job.getDatasource()+"'");
        }
        if(job.getStatus() != null) {
            column.append(",status");
            value.append(",'"+job.getStatus()+"'");
        }
        if(job.getCode() != null) {
            column.append(",code");
            value.append(",'"+job.getCode()+"'");
        }
        if(job.getCategoryId() != null) {
            column.append(",categoryId");
            value.append(",'"+job.getCategoryId()+"'");
        }
        if(job.getCategoryName() != null) {
            column.append(",categoryName");
            value.append(",'"+job.getCategoryName()+"'");
        }
        if(job.getCreateTime() != null) {
            column.append(",CreateTime");
            value.append(",").append(DataBaseSqlConfig.getDateStrFormat(job.getCreateTime()));
        }
        if(job.getDescription() != null) {
            column.append(",description");
            value.append(",'"+job.getDescription()+"'");
        }
        column.append(")");
        value.append(")");
        String sql = column.toString()+value.toString();
        return sql;
    }

    public String updateJob(TblJob newJob) throws Exception {
        StringBuffer sql = new StringBuffer("UPDATE TBL_JOB SET ");
        
        if(newJob.getJobname() != null && !"".equals(newJob.getJobname())) {
            sql.append("  JOBNAME = '"+newJob.getJobname()+"'");
        }
        if(newJob.getCompanyId() != null && !"".equals(newJob.getCompanyId())) {
            sql.append(" , COMPANYID = '"+newJob.getCompanyId()+"'");
        }
        
        if(newJob.getHistorycode() != null && !"".equals(newJob.getHistorycode())) {
            sql.append(" , historycode = '"+newJob.getHistorycode()+"'");
        }
        if(newJob.getDatasource() != null && !"".equals(newJob.getDatasource())) {
            sql.append(" , datasource = '"+newJob.getDatasource()+"'");
        }
        
        if(newJob.getStatus() != null && !"".equals(newJob.getStatus())) {
            sql.append(" , status = '"+newJob.getStatus()+"'");
        }
        if(newJob.getCode() != null && !"".equals(newJob.getCode())) {
            sql.append(" , code = '"+newJob.getCode()+"'");
        }
        if(newJob.getCategoryId() != null && !"".equals(newJob.getCategoryId())) {
            sql.append(" , categoryId = '"+newJob.getCategoryId()+"'");
        }
        if(newJob.getCategoryName() != null && !"".equals(newJob.getCategoryName())) {
            sql.append(" , categoryName = '"+newJob.getCategoryName()+"'");
        }
        if(newJob.getCreateTime() != null) {
            sql.append(" , CreateTime = ").append(DataBaseSqlConfig.getDateStrFormat(newJob.getCreateTime()));
        }
        
        if(newJob.getDescription() != null && !"".equals(newJob.getDescription())) {
            sql.append(" , description = '"+newJob.getDescription().trim()+"'");
        }
        sql.append(" WHERE JOBID = '"+newJob.getJobid().toString().trim()+"'");
        return sql.toString();
    }


}