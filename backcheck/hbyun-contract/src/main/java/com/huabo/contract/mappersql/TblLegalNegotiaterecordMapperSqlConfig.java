package com.huabo.contract.mappersql;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hbfk.util.database.DataBaseSqlConfig;
import com.huabo.contract.entity.TblLegalNegotiaterecord;

public class TblLegalNegotiaterecordMapperSqlConfig {

	public String saveNegotiateRecord(TblLegalNegotiaterecord record) throws Exception {
        StringBuffer column = new StringBuffer("INSERT INTO TBL_LEGAL_NEGOTIATERECORD (RECORDID");
        StringBuffer value = new StringBuffer(" VALUES ("+record.getRecordid());

        if(record.getNegotiationtime() != null) {
            column.append(",NEGOTIATIONTIME");
            value.append(","+ DataBaseSqlConfig.getDateStrFormat(record.getNegotiationtime()));
            //value.append(",'"+record.getNegotiationtime()+"'");
        }
        if(record.getRecordcounterpart() != null && !"".equals(record.getRecordcounterpart())) {
            column.append(",RECORDCOUNTERPART");
            value.append(",'"+record.getRecordcounterpart()+"'");
        }
        if(record.getOurnegotiator() != null && !"".equals(record.getOurnegotiator())) {
            column.append(",OURNEGOTIATOR");
            value.append(",'"+record.getOurnegotiator()+"'");
        }
        if(record.getNegotiationmode() != null && !"".equals(record.getNegotiationmode())) {
            column.append(",NEGOTIATIONMODE");
            value.append(",'"+record.getNegotiationmode()+"'");
        }
        if(record.getNegotiationrecord() != null && !"".equals(record.getNegotiationrecord())) {
            column.append(",NEGOTIATIONRECORD");
            value.append(",'"+record.getNegotiationrecord()+"'");
        }
        if(record.getNegetiationmemoe() != null && !"".equals(record.getNegetiationmemoe())) {
            column.append(",NEGETIATIONMEMOE");
            value.append(",'"+record.getNegetiationmemoe()+"'");
        }
        if(record.getNegotiateinfo() != null && !"".equals(record.getNegotiateinfo())) {
            column.append(",NEGOTIATEINFO");
            value.append(",'"+record.getNegotiateinfo()+"'");
        }
        if(record.getLinkorg() != null && !"".equals(record.getLinkorg())) {
            column.append(",LINKORG");
            value.append(",'"+record.getLinkorg()+"'");
        }
        if(record.getCreatestaff() != null && !"".equals(record.getCreatestaff())) {
            column.append(",CREATESTAFF");
            value.append(",'"+record.getCreatestaff()+"'");
        }
        if(record.getCreatetime() != null && !"".equals(record.getCreatetime())) {
            column.append(",CREATETIME");
            value.append(","+ DataBaseSqlConfig.getDateHmsStrFormat(record.getCreatetime()));
            // value.append(",'"+record.getCreatetime()+"'");
        }
        if(record.getCourtname() != null && !"".equals(record.getCourtname())) {
            column.append(",COURTNAME");
            value.append(",'"+record.getCourtname()+"'");
        }
        if(record.getCourtparter() != null && !"".equals(record.getCourtparter())) {
            column.append(",COURTPARTER");
            value.append(",'"+record.getCourtparter()+"'");
        }
        if(record.getCourtlink() != null && !"".equals(record.getCourtlink())) {
            column.append(",COURTLINK");
            value.append(",'"+record.getCourtlink()+"'");
        }
        if(record.getStaffids() != null && !"".equals(record.getStaffids())) {
            column.append(",STAFFIDS");
            value.append(",'"+record.getStaffids()+"'");
        }
        if(record.getStaffnames() != null && !"".equals(record.getStaffnames())) {
            column.append(",STAFFNAMES");
            value.append(",'"+record.getStaffnames()+"'");
        }

        column.append(")");
        value.append(")");
        String sql = column.toString()+value.toString();
        return sql;
    }

    public String updateModifyNegotiateRecord(TblLegalNegotiaterecord oldRecord) throws Exception {
        StringBuffer sql = new StringBuffer("UPDATE TBL_LEGAL_NEGOTIATERECORD SET NEGOTIATIONMODE = '"+oldRecord.getNegotiationmode()+"' ");

        if(oldRecord.getNegotiationtime() != null && !"".equals(oldRecord.getNegotiationtime())) {
            sql.append(" ,NEGOTIATIONTIME = "+ DataBaseSqlConfig.getDateStrFormat(oldRecord.getNegotiationtime()));
        }
        if(oldRecord.getRecordcounterpart() != null && !"".equals(oldRecord.getRecordcounterpart())) {
            sql.append(" , RECORDCOUNTERPART = '"+oldRecord.getRecordcounterpart()+"'");
        }
        if(oldRecord.getOurnegotiator() != null && !"".equals(oldRecord.getOurnegotiator())) {
            sql.append(" , OURNEGOTIATOR = '"+oldRecord.getOurnegotiator()+"'");
        }
        if(oldRecord.getNegotiationrecord() != null && !"".equals(oldRecord.getNegotiationrecord())) {
            sql.append(" , NEGOTIATIONRECORD = '"+oldRecord.getNegotiationrecord()+"'");
        }
        if(oldRecord.getNegetiationmemoe() != null && !"".equals(oldRecord.getNegetiationmemoe())) {
            sql.append(" , NEGETIATIONMEMOE = '"+oldRecord.getNegetiationmemoe()+"'");
        }
        if(oldRecord.getNegotiateinfo() != null && !"".equals(oldRecord.getNegotiateinfo())) {
            sql.append(" , NEGOTIATEINFO = '"+oldRecord.getNegotiateinfo()+"'");
        }
        if(oldRecord.getLinkorg() != null && !"".equals(oldRecord.getLinkorg())) {
            sql.append(" , LINKORG = '"+oldRecord.getLinkorg()+"'");
        }
        if(oldRecord.getCreatestaff() != null && !"".equals(oldRecord.getCreatestaff())) {
            sql.append(" , CREATESTAFF = '"+oldRecord.getCreatestaff()+"'");
        }
        if(oldRecord.getCreatetime() != null && !"".equals(oldRecord.getCreatetime())) {
            sql.append(" ,CREATETIME = "+ DataBaseSqlConfig.getDateHmsStrFormat(oldRecord.getCreatetime()));
        }
        if(oldRecord.getCourtname() != null && !"".equals(oldRecord.getCourtname())) {
            sql.append(" , COURTNAME = '"+oldRecord.getCourtname()+"'");
        }
        if(oldRecord.getCourtparter() != null && !"".equals(oldRecord.getCourtparter())) {
            sql.append(" , COURTPARTER = '"+oldRecord.getCourtparter()+"'");
        }
        if(oldRecord.getCourtlink() != null && !"".equals(oldRecord.getCourtlink())) {
            sql.append(" , COURTLINK = '"+oldRecord.getCourtlink()+"'");
        }
        if(oldRecord.getStaffids() != null && !"".equals(oldRecord.getStaffids())) {
            sql.append(" , STAFFIDS = '"+oldRecord.getStaffids()+"'");
        }
        if(oldRecord.getStaffnames() != null && !"".equals(oldRecord.getStaffnames())) {
            sql.append(" , STAFFNAMES = '"+oldRecord.getStaffnames()+"'");
        }

        sql.append(" WHERE RECORDID = '"+oldRecord.getRecordid()+"'");
        return sql.toString();
    }

    public String findByNegotiaid(IPage<TblLegalNegotiaterecord> page, BigDecimal negotiaId){
        StringBuffer sqlSb = new StringBuffer("SELECT TS.REALNAME AS LRR,TST.REALNAME AS WFTPR,TLN.* FROM " +
                "TBL_LEGAL_NEGOTIATERECORD TLN " +
                "LEFT JOIN TBL_STAFF TS ON TLN.CREATESTAFF = TS.STAFFID " +
                "LEFT JOIN TBL_STAFF TST ON TLN.OURNEGOTIATOR = TST.STAFFID " +
                "WHERE TLN.NEGOTIATEINFO = "+negotiaId);

        sqlSb.append(" ORDER BY TLN.RECORDID DESC ");
        String sql = sqlSb.toString();
        return sql;
    }
}
