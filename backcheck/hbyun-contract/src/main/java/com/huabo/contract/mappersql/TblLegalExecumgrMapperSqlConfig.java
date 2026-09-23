package com.huabo.contract.mappersql;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hbfk.util.database.DataBaseSqlConfig;
import com.huabo.contract.entity.TblLegalExecumgr;

public class TblLegalExecumgrMapperSqlConfig {
	public String findListByPage(IPage<TblLegalExecumgr> page, BigDecimal litigationid, BigDecimal arbitraid,TblLegalExecumgr tla, String companyIds) throws Exception {
        StringBuffer sqlSb = new StringBuffer("SELECT * FROM TBL_LEGAL_EXECUMGR TLA " +
               "WHERE 1=1 and TLA.orgid in ("+companyIds+ ")");

        if (tla.getExecuno() != null && tla.getExecuno().length()>0){
            sqlSb.append(" AND TLA.EXECUNO like '%"+tla.getExecuno()+"%'");
        }
        if (tla.getExecucourt() != null && tla.getExecucourt().length()>0){
            sqlSb.append(" AND TLA.EXECUCOURT like '%"+tla.getExecucourt()+"%'");
        }
        if (tla.getExecutype()!= null && tla.getExecutype().length()>0){
            sqlSb.append(" AND TLA.EXECUTYPE = '"+tla.getExecutype()+"'");
        }
        if (tla.getSslx()!= null && tla.getSslx().length()>0){
            sqlSb.append(" AND TLA.SSLX = '"+tla.getSslx()+"'");
        }
        if (litigationid != null){
            sqlSb.append(" AND TLA.litigationid = "+litigationid);
        }
        if(arbitraid != null){
        	sqlSb.append(" AND TLA.arbitraid = "+arbitraid);
        }

        sqlSb.append(" ORDER BY ID DESC");
        String sql = sqlSb.toString();
        return sql;
    }

    public String addEntity(TblLegalExecumgr tla) throws Exception {
        StringBuffer column = new StringBuffer("INSERT INTO TBL_LEGAL_EXECUMGR (ID");
        StringBuffer value = new StringBuffer(" VALUES ("+tla.getId());
        column.append(",STATUS");
        value.append(",'0'");
        if(tla.getDisputename() != null && !"".equals(tla.getDisputename())) {
            column.append(",DISPUTENAME");
            value.append(",'"+tla.getDisputename()+"'");
        }
        if(tla.getExecuno() != null && !"".equals(tla.getExecuno())) {
            column.append(",EXECUNO");
            value.append(",'"+tla.getExecuno()+"'");
        }
        if(tla.getExecucourt() != null && !"".equals(tla.getExecucourt())) {
            column.append(",EXECUCOURT");
            value.append(",'"+tla.getExecucourt()+"'");
        }
        if(tla.getExecutype() != null && !"".equals(tla.getExecutype())) {
            column.append(",EXECUTYPE");
            value.append(",'"+tla.getExecutype()+"'");
        }
        
        if(tla.getCreatetime() != null) {
            column.append(",CREATETIME");
            value.append(","+ DataBaseSqlConfig.getDateStrFormat(tla.getCreatetime()));
        }
		if(tla.getExecutiontime() != null) {
			column.append(",EXECUTIONTIME");
			value.append(","+ DataBaseSqlConfig.getDateStrFormat(tla.getExecutiontime()));
		}
        if(tla.getExecuamount() != null) {
            column.append(",EXECUAMOUNT");
            value.append(",'"+tla.getExecuamount()+"'");
        }
        if(tla.getExecuedamount() != null) {
            column.append(",EXECUEDAMOUNT");
            value.append(",'"+tla.getExecuedamount()+"'");
        }
        if(tla.getNoexecuamount() != null) {
            column.append(",NOEXECUAMOUNT");
            value.append(",'"+tla.getNoexecuamount()+"'");
        }
        if(tla.getLitigationid() != null) {
            column.append(",LITIGATIONID");
            value.append(",'"+tla.getLitigationid()+"'");
        }
        if(tla.getArbitraid() != null) {
            column.append(",ARBITRAID");
            value.append(",'"+tla.getArbitraid()+"'");
        }
        if(tla.getDisputeid() != null) {
            column.append(",DISPUTEID");
            value.append(",'"+tla.getDisputeid()+"'");
        }
        if(tla.getCreatestaffid() != null) {
            column.append(",CREATESTAFFID");
            value.append(",'"+tla.getCreatestaffid()+"'");
        }
        if(tla.getOrgid() != null) {
            column.append(",ORGID");
            value.append(",'"+tla.getOrgid()+"'");
        }

        if(tla.getSslx() != null && !"".equals(tla.getSslx())) {
            column.append(",SSLX");
            value.append(",'"+tla.getSslx()+"'");
        }
        
        if(tla.getArbitraname() != null && !"".equals(tla.getArbitraname())) {
            column.append(",ARBITRANAME");
            value.append(",'"+tla.getArbitraname()+"'");
        }
        
        if(tla.getLitigationname() != null && !"".equals(tla.getLitigationname())) {
            column.append(",LITIGATIONNAME");
            value.append(",'"+tla.getLitigationname()+"'");
        }
        column.append(")");
        value.append(")");
        String sql = column.toString()+value.toString();
        return sql;
    }


    public String updateEntity(TblLegalExecumgr tla) throws Exception {
        StringBuffer sql = new StringBuffer("UPDATE TBL_LEGAL_EXECUMGR SET DISPUTENAME = '"+tla.getDisputename()+"'");

        if(tla.getExecuno() != null && !"".equals(tla.getExecuno())) {
            sql.append(" , EXECUNO = '"+tla.getExecuno()+"'");
        }
        if(tla.getExecucourt() != null && !"".equals(tla.getExecucourt())) {
            sql.append(" , EXECUCOURT = '"+tla.getExecucourt()+"'");
        }
        
        if(tla.getExecutype() != null && !"".equals(tla.getExecutype())) {
            sql.append(" , EXECUTYPE = '"+tla.getExecutype()+"'");
        }
        if(tla.getExecuamount() != null) {
            sql.append(" , EXECUAMOUNT = '"+tla.getExecuamount()+"'");
        }
        if(tla.getExecuedamount() != null) {
            sql.append(" , EXECUEDAMOUNT = '"+tla.getExecuedamount()+"'");
        }
        if(tla.getNoexecuamount() != null) {
            sql.append(" , NOEXECUAMOUNT = '"+tla.getNoexecuamount()+"'");
        }
        if(tla.getLitigationid() != null) {
            sql.append(" , LITIGATIONID = '"+tla.getLitigationid()+"'");
            sql.append(" , ARBITRAID = ''");
        }
        if(tla.getArbitraid() != null) {
        	sql.append(" , LITIGATIONID = ''");
            sql.append(" , ARBITRAID = '"+tla.getArbitraid()+"'");
        }
        if(tla.getCreatestaffid() != null) {
            sql.append(" , CREATESTAFFID = '"+tla.getCreatestaffid()+"'");
        }
        if(tla.getOrgid() != null) {
        	sql.append(" , ORGID = '"+tla.getOrgid()+"'");
        }

        if(tla.getSslx() != null && !"".equals(tla.getSslx())) {
        	sql.append(" , SSLX = '"+tla.getSslx()+"'");
        }
        if(tla.getCreatetime() != null) {
            sql.append(" ,CREATETIME = "+ DataBaseSqlConfig.getDateStrFormat(tla.getCreatetime()));
        }
		if(tla.getExecutiontime() != null) {
			sql.append(" ,EXECUTIONTIME = "+ DataBaseSqlConfig.getDateStrFormat(tla.getExecutiontime()));
		}
        
        if(tla.getArbitraname() != null && !"".equals(tla.getArbitraname())) {
        	sql.append(" , ARBITRANAME = '"+tla.getArbitraname()+"'");
        	sql.append(" , LITIGATIONNAME = ''");
        }
        
        if(tla.getLitigationname() != null && !"".equals(tla.getLitigationname())) {
        	sql.append(" , LITIGATIONNAME = '"+tla.getLitigationname()+"'");
        	sql.append(" , ARBITRANAME = ''");
        }
        sql.append(" WHERE ID = '"+tla.getId()+"'");
        return sql.toString();
    }
}
