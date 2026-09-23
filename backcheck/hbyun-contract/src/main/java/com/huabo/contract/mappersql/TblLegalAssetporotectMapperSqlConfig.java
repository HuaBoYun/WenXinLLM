package com.huabo.contract.mappersql;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hbfk.util.database.DataBaseSqlConfig;
import com.huabo.contract.entity.TblLegalAssetporotect;

public class TblLegalAssetporotectMapperSqlConfig {
	public String findListByPage(IPage<TblLegalAssetporotect> page, BigDecimal litigationid, BigDecimal arbitraid) throws Exception {
        StringBuffer sqlSb = new StringBuffer("SELECT TLA.*,tld.DISPUTEITEM FROM TBL_LEGAL_ASSETPOROTECT TLA " +
                "LEFT JOIN TBL_LEGAL_DISPUTREGISTRATION tld on TLA.DISPUTEID = tld.DISPUTEID " +
                "WHERE 1=1 ");

        if (litigationid != null){
            sqlSb.append("AND TLA.litigationid = "+litigationid);
        }
        if(arbitraid != null){
        	sqlSb.append("AND TLA.arbitraid = "+arbitraid);
        }

        sqlSb.append(" ORDER BY ID DESC");
        String sql = sqlSb.toString();
        return sql;
    }


    public String addEntity(TblLegalAssetporotect tla) throws Exception{
        StringBuffer column = new StringBuffer("INSERT INTO TBL_LEGAL_ASSETPOROTECT (ID");
        StringBuffer value = new StringBuffer(" VALUES ("+tla.getId());

        if(tla.getApper() != null && !"".equals(tla.getApper())) {
            column.append(",APPER");
            value.append(",'"+tla.getApper()+"'");
        }
        if(tla.getResponder() != null && !"".equals(tla.getResponder())) {
            column.append(",RESPONDER");
            value.append(",'"+tla.getResponder()+"'");
        }
        if(tla.getDisputeid() != null) {
            column.append(",DISPUTEID");
            value.append(",'"+tla.getDisputeid()+"'");
        }
        if(tla.getActionstage() != null && !"".equals(tla.getActionstage())) {
            column.append(",ACTIONSTAGE");
            value.append(",'"+tla.getActionstage()+"'");
        }
        if(tla.getCreatetime() != null) {
            column.append(",CREATETIME");
            value.append(","+DataBaseSqlConfig.getDateStrFormat(tla.getCreatetime()));
        }
        if(tla.getIsapppreserva() != null) {
            column.append(",ISAPPPRESERVA");
            value.append(",'"+tla.getIsapppreserva()+"'");
        }
        if(tla.getPreservaamount() != null) {
            column.append(",PRESERVAAMOUNT");
            value.append(",'"+tla.getPreservaamount()+"'");
        }
        if(tla.getPreservanature() != null) {
            column.append(",PRESERVANATURE");
            value.append(",'"+tla.getPreservanature()+"'");
        }
        if(tla.getIsexecutdeduction() != null) {
            column.append(",ISEXECUTDEDUCTION");
            value.append(",'"+tla.getIsexecutdeduction()+"'");
        }
        if(tla.getExecutamount() != null) {
            column.append(",EXECUTAMOUNT");
            value.append(",'"+tla.getExecutamount()+"'");
        }
        if(tla.getIsreleasepreserva() != null) {
            column.append(",ISRELEASEPRESERVA");
            value.append(",'"+tla.getIsreleasepreserva()+"'");
        }
        if(tla.getLitigationid() != null) {
            column.append(",LITIGATIONID");
            value.append(",'"+tla.getLitigationid()+"'");
        }
        if(tla.getArbitraid() != null) {
            column.append(",ARBITRAID");
            value.append(",'"+tla.getArbitraid()+"'");
        }
        if(tla.getCreatestaffid() != null) {
            column.append(",CREATESTAFFID");
            value.append(",'"+tla.getCreatestaffid()+"'");
        }

        column.append(")");
        value.append(")");
        String sql = column.toString()+value.toString();
        return sql;
    }


    public String updateEntity(TblLegalAssetporotect tla) throws Exception {
        StringBuffer sql = new StringBuffer("UPDATE TBL_LEGAL_ASSETPOROTECT SET APPER = '"+tla.getApper()+"'");

        if(tla.getResponder() != null && !"".equals(tla.getResponder())) {
            sql.append(" , RESPONDER = '"+tla.getResponder()+"'");
        }
        if(tla.getDisputeid() != null) {
            sql.append(" , DISPUTEID = '"+tla.getDisputeid()+"'");
        }
        if(tla.getActionstage() != null && !"".equals(tla.getActionstage())) {
            sql.append(" , ACTIONSTAGE = '"+tla.getActionstage()+"'");
        }
        
        if(tla.getIsapppreserva() != null) {
            sql.append(" , ISAPPPRESERVA = '"+tla.getIsapppreserva()+"'");
        }
        if(tla.getPreservaamount() != null) {
            sql.append(" , PRESERVAAMOUNT = '"+tla.getPreservaamount()+"'");
        }
        if(tla.getPreservanature() != null && !"".equals(tla.getPreservanature())) {
            sql.append(" , PRESERVANATURE = '"+tla.getPreservanature()+"'");
        }
        if(tla.getIsexecutdeduction() != null) {
            sql.append(" , ISEXECUTDEDUCTION = '"+tla.getIsexecutdeduction()+"'");
        }
        if(tla.getExecutamount() != null) {
            sql.append(" , EXECUTAMOUNT = '"+tla.getExecutamount()+"'");
        }
        if(tla.getIsreleasepreserva() != null) {
            sql.append(" , ISRELEASEPRESERVA = '"+tla.getIsreleasepreserva()+"'");
        }
        if(tla.getLitigationid() != null) {
            sql.append(" , LITIGATIONID = '"+tla.getLitigationid()+"'");
        }
        if(tla.getArbitraid() != null) {
            sql.append(" , ARBITRAID = '"+tla.getArbitraid()+"'");
        }
        if(tla.getCreatestaffid() != null) {
            sql.append(" , CREATESTAFFID = '"+tla.getCreatestaffid()+"'");
        }
        if(tla.getCreatetime() != null) {
            sql.append(" ,CREATETIME = " + DataBaseSqlConfig.getDateStrFormat(tla.getCreatetime()));
        }
        
        sql.append(" WHERE ID = '"+tla.getId()+"'");
        return sql.toString();
    }
}
