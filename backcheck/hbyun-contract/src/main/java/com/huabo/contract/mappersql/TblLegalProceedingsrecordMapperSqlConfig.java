package com.huabo.contract.mappersql;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hbfk.util.database.DataBaseSqlConfig;
import com.huabo.contract.entity.TblLegalProceedingsrecord;

public class TblLegalProceedingsrecordMapperSqlConfig {

	public String getproceedja(String litigationId) throws Exception{
		String sql = "SELECT "+DataBaseSqlConfig.getYearAllStrColumn("CREATETIME", "YYYY-MM-DD")+" AS year,'结案时间' AS condation FROM TBL_LEGAL_CLOSESUM WHERE LITIGATIONID = "+litigationId;
		return sql;
	}
	
	public String getproceedTim(String litigationId) throws Exception{
		String sql = "SELECT "+DataBaseSqlConfig.getYearAllStrColumn("filingtime", "YYYY-MM-DD")+" AS year,PORCEEDSTAGE AS condation   from TBL_LEGAL_PROCEEDINGSRECORD where litigationinfo = "+litigationId+" order by createtime ";
		return sql;
	}

	public String getproceedex(String litigationId) throws Exception{
		String sql = "SELECT "+DataBaseSqlConfig.getYearAllStrColumn("EXECUTIONTIME", "YYYY-MM-DD")+" AS year,'执行时间'  AS condation   from TBL_LEGAL_EXECUMGR where LITIGATIONID = "+litigationId+" order by createtime ";
		return sql;
	}
	
	public String getproceeddis(String litigationId) throws Exception{
		String sql = "SELECT "+DataBaseSqlConfig.getYearAllStrColumn("BUSINESSDATE", "YYYY-MM-DD")+" AS year,'业务发生时间'  AS condation   from TBL_LEGAL_LITIGATIONSETTLEMENT lm LEFT JOIN TBL_LEGAL_DISPUTREGISTRATION dis on LM.DISPUTEID=DIS.DISPUTEID WHERE LM.LITIGATIONID= = "+litigationId;
		return sql;
	}
	
    public String saveProceedingRecord(TblLegalProceedingsrecord proceed) throws Exception {
        StringBuffer column = new StringBuffer("INSERT INTO TBL_LEGAL_PROCEEDINGSRECORD (PROCEEDID");
        StringBuffer value = new StringBuffer(" VALUES ("+proceed.getProceedid());

        if(proceed.getProceedno() != null && !"".equals(proceed.getProceedno())){
            column.append(",PROCEEDNO");
            value.append(",'"+proceed.getProceedno()+"'");
        }
        if(proceed.getPorceedstage() != null && !"".equals(proceed.getPorceedstage())){
            column.append(",PORCEEDSTAGE");
            value.append(",'"+proceed.getPorceedstage()+"'");
        }
        if(proceed.getCourt() != null && !"".equals(proceed.getCourt())){
            column.append(",COURT");
            value.append(",'"+proceed.getCourt()+"'");
        }
        if(proceed.getCourtlink() != null && !"".equals(proceed.getCourtlink())){
            column.append(",COURTLINK");
            value.append(",'"+proceed.getCourtlink()+"'");
        }
        if(proceed.getCourtcontact() != null && !"".equals(proceed.getCourtcontact())){
            column.append(",COURTCONTACT");
            value.append(",'"+proceed.getCourtcontact()+"'");
        }
        if(proceed.getFilingtime() != null && !"".equals(proceed.getFilingtime())){
            column.append(",FILINGTIME");
            value.append(","+ DataBaseSqlConfig.getDateStrFormat(proceed.getFilingtime()));
        }
        if(proceed.getPaymentremindtime() != null && !"".equals(proceed.getPaymentremindtime())){
            column.append(",PAYMENTREMINDTIME");
            value.append(","+ DataBaseSqlConfig.getDateStrFormat(proceed.getPaymentremindtime()));
        }
        if(proceed.getOpeningtime() != null && !"".equals(proceed.getOpeningtime())){
            column.append(",OPENINGTIME");
            value.append(","+ DataBaseSqlConfig.getDateStrFormat(proceed.getOpeningtime()));
        }
        if(proceed.getJudgetiem() != null && !"".equals(proceed.getJudgetiem())){
            column.append(",JUDGETIEM");
            value.append(","+ DataBaseSqlConfig.getDateStrFormat(proceed.getJudgetiem()));
        }
        if(proceed.getIsexternallawyer() != null && !"".equals(proceed.getIsexternallawyer())){
            column.append(",ISEXTERNALLAWYER");
            value.append(",'"+proceed.getIsexternallawyer()+"'");
        }
        if(proceed.getLawyearword() != null && !"".equals(proceed.getLawyearword())){
            column.append(",LAWYEARWORD");
            value.append(",'"+proceed.getLawyearword()+"'");
        }
        if(proceed.getLawyearname() != null && !"".equals(proceed.getLawyearname())){
            column.append(",LAWYEARNAME");
            value.append(",'"+proceed.getLawyearname()+"'");
        }
        if(proceed.getLawyearlink() != null && !"".equals(proceed.getLawyearlink())){
            column.append(",LAWYEARLINK");
            value.append(",'"+proceed.getLawyearlink()+"'");
        }
        if(proceed.getLitigationinfo() != null && !"".equals(proceed.getLitigationinfo())){
            column.append(",LITIGATIONINFO");
            value.append(",'"+proceed.getLitigationinfo()+"'");
        }
        if(proceed.getCreatestaff() != null && !"".equals(proceed.getCreatestaff())){
            column.append(",CREATESTAFF");
            value.append(",'"+proceed.getCreatestaff()+"'");
        }
        if(proceed.getCreatetime() != null && !"".equals(proceed.getCreatetime())){
            column.append(",CREATETIME");
            value.append(","+ DataBaseSqlConfig.getDateStrFormat(proceed.getCreatetime()));
        }
        if(proceed.getLinkorg() != null && !"".equals(proceed.getLinkorg())){
            column.append(",LINKORG");
            value.append(",'"+proceed.getLinkorg()+"'");
        }
        if(proceed.getNegotiator() != null && !"".equals(proceed.getNegotiator())){
            column.append(",NEGOTIATOR");
            value.append(",'"+proceed.getNegotiator()+"'");
        }
        if(proceed.getNegotiatorlink() != null && !"".equals(proceed.getNegotiatorlink())){
            column.append(",NEGOTIATORLINK");
            value.append(",'"+proceed.getNegotiatorlink()+"'");
        }
        if(proceed.getPresedingjudge() != null && !"".equals(proceed.getPresedingjudge())){
            column.append(",PRESEDINGJUDGE");
            value.append(",'"+proceed.getPresedingjudge()+"'");
        }
        if(proceed.getCasepromotion() != null && !"".equals(proceed.getCasepromotion())){
            column.append(",CASEPROMOTION");
            value.append(",'"+proceed.getCasepromotion()+"'");
        }
        if(proceed.getExistingdifficulties() != null && !"".equals(proceed.getNegotiator())){
            column.append(",EXISTINGDIFFICULTIES");
            value.append(",'"+proceed.getNegotiator()+"'");
        }
        if(proceed.getMeasurespromote() != null && !"".equals(proceed.getMeasurespromote())){
            column.append(",MEASURESPROMOTE");
            value.append(",'"+proceed.getMeasurespromote()+"'");
        }
        if(proceed.getState() != null ){
            column.append(",STATE");
            value.append(",'"+proceed.getState()+"'");
        }

        column.append(")");
        value.append(")");
        String sql = column.toString()+value.toString();
        return sql;
    }


    public String updateModifyNegotiateRecord(TblLegalProceedingsrecord oldproceed) throws Exception {
        StringBuffer sql = new StringBuffer("UPDATE TBL_LEGAL_PROCEEDINGSRECORD SET PROCEEDNO = '"+oldproceed.getProceedno()+"'");

        if(oldproceed.getPorceedstage() != null && !"".equals(oldproceed.getPorceedstage())) {
            sql.append("  ,PORCEEDSTAGE = '"+oldproceed.getPorceedstage()+"'");
        }
        if(oldproceed.getCourt() != null && !"".equals(oldproceed.getCourt())) {
            sql.append(" , COURT = '"+oldproceed.getCourt()+"'");
        }
        if(oldproceed.getCourtlink() != null && !"".equals(oldproceed.getCourtlink())) {
            sql.append(" , COURTLINK = '"+oldproceed.getCourtlink()+"'");
        }
        if(oldproceed.getCourtcontact() != null && !"".equals(oldproceed.getCourtcontact())) {
            sql.append(" , COURTCONTACT = '"+oldproceed.getCourtcontact()+"'");
        }
        if(oldproceed.getFilingtime() != null && !"".equals(oldproceed.getFilingtime())) {
            //sql.append(" , FILINGTIME = '"+oldproceed.getFilingtime()+"'");
            sql.append(" ,FILINGTIME = "+DataBaseSqlConfig.getDateStrFormat(oldproceed.getFilingtime()));
        }
        if(oldproceed.getPaymentremindtime() != null && !"".equals(oldproceed.getPaymentremindtime())) {
           // sql.append(" , PAYMENTREMINDTIME = '"+oldproceed.getPaymentremindtime()+"'");
            sql.append(" ,PAYMENTREMINDTIME = "+DataBaseSqlConfig.getDateStrFormat(oldproceed.getPaymentremindtime()));
        }
        if(oldproceed.getOpeningtime() != null && !"".equals(oldproceed.getOpeningtime())) {
            //sql.append(" , OPENINGTIME = '"+oldproceed.getOpeningtime()+"'");
            sql.append(" ,OPENINGTIME = "+DataBaseSqlConfig.getDateStrFormat(oldproceed.getOpeningtime()));

        }
        if(oldproceed.getJudgetiem() != null && !"".equals(oldproceed.getJudgetiem())) {
            //sql.append(" , JUDGETIEM = '"+oldproceed.getJudgetiem()+"'");
            sql.append(" ,JUDGETIEM = "+DataBaseSqlConfig.getDateStrFormat(oldproceed.getJudgetiem()));

        }
        if(oldproceed.getIsexternallawyer() != null ) {
            sql.append(" , ISEXTERNALLAWYER = '"+oldproceed.getIsexternallawyer()+"'");
        }
        if(oldproceed.getLawyearword() != null && !"".equals(oldproceed.getLawyearword())) {
            sql.append(" , LAWYEARWORD = '"+oldproceed.getLawyearword()+"'");
        }
        if(oldproceed.getLawyearname() != null && !"".equals(oldproceed.getLawyearname())) {
            sql.append(" , LAWYEARNAME = '"+oldproceed.getLawyearname()+"'");
        }
        if(oldproceed.getLawyearlink() != null && !"".equals(oldproceed.getLawyearlink())) {
            sql.append(" , LAWYEARLINK = '"+oldproceed.getLawyearlink()+"'");
        }
        if(oldproceed.getLitigationinfo() != null && !"".equals(oldproceed.getLitigationinfo())) {
            sql.append(" , LITIGATIONINFO = '"+oldproceed.getLitigationinfo()+"'");
        }
        if(oldproceed.getCreatestaff() != null && !"".equals(oldproceed.getCreatestaff())) {
            sql.append(" , CREATESTAFF = '"+oldproceed.getCreatestaff()+"'");
        }
        if(oldproceed.getCreatetime() != null && !"".equals(oldproceed.getCreatetime())) {
            sql.append(" ,CREATETIME = "+DataBaseSqlConfig.getDateStrFormat(oldproceed.getCreatetime()));
        }
        if(oldproceed.getLinkorg() != null && !"".equals(oldproceed.getLinkorg())) {
            sql.append(" , LINKORG = '"+oldproceed.getLinkorg()+"'");
        }
        if(oldproceed.getNegotiator() != null && !"".equals(oldproceed.getNegotiator())) {
            sql.append(" , NEGOTIATOR = '"+oldproceed.getNegotiator()+"'");
        }
        if(oldproceed.getNegotiatorlink() != null && !"".equals(oldproceed.getNegotiatorlink())) {
            sql.append(" , NEGOTIATORLINK = '"+oldproceed.getNegotiatorlink()+"'");
        }
        if(oldproceed.getPresedingjudge() != null && !"".equals(oldproceed.getPresedingjudge())) {
            sql.append(" , PRESEDINGJUDGE = '"+oldproceed.getPresedingjudge()+"'");
        }
        if(oldproceed.getCasepromotion() != null && !"".equals(oldproceed.getCasepromotion())) {
            sql.append(" , CASEPROMOTION = '"+oldproceed.getCasepromotion()+"'");
        }
        if(oldproceed.getExistingdifficulties() != null && !"".equals(oldproceed.getExistingdifficulties())) {
            sql.append(" , EXISTINGDIFFICULTIES = '"+oldproceed.getExistingdifficulties()+"'");
        }
        if(oldproceed.getMeasurespromote() != null && !"".equals(oldproceed.getMeasurespromote())) {
            sql.append(" , MEASURESPROMOTE = '"+oldproceed.getMeasurespromote()+"'");
        }

        sql.append(" WHERE PROCEEDID = '"+oldproceed.getProceedid()+"'");
        return sql.toString();
    }


    public String findListByPageInfo(IPage<TblLegalProceedingsrecord> page, TblLegalProceedingsrecord record) throws Exception {
        StringBuffer sqlSb = new StringBuffer("SELECT " +
                "TLP.*,\n" +
                "TOR.ORGID ,\n" +
                "TOR.ORGNAME,\n" +
                "TL.ACTIONOBJECT,\n" +
                "TLD.PLAINTIFF,\n" +
                "TLD.DEFENDANT,\n" +
                "TLD.DISPUTEITEM" +
                " FROM TBL_LEGAL_PROCEEDINGSRECORD TLP " +
                "LEFT JOIN TBL_ORGANIZATION TOR ON TLP.LINKORG = TOR.ORGID " +
                "LEFT JOIN TBL_LEGAL_LITIGATIONSETTLEMENT TL ON TLP.LITIGATIONINFO = TL.LITIGATIONID " +
                "LEFT JOIN TBL_LEGAL_DISPUTREGISTRATION TLD ON TL.DISPUTEINFO = TLD.DISPUTEID " +
                "WHERE TLP.LINKORG = "+ record.getLinkorg());

        sqlSb.append(" ORDER BY proceedId DESC ");
        String sql = sqlSb.toString();
        return sql;
    }

    public String findByNegotiaId(IPage<TblLegalProceedingsrecord> page, BigDecimal litigationId) {
        StringBuffer sqlSb = new StringBuffer("SELECT " +
                "TLP.*,\n" +
                "TLL.FIRSTCOURT,\n" +
                "TLL.PRESIDINGJUDGE,\n" +
                "TLL.COLLEGIALPANEL,\n" +
                "TLL.LITIGATIONAMOUNT,\n" +
                "TLL.DEALDATE,\n" +
                "TLL.FIRSTHEARINGDATE,\n" +
                "TLL.LITIGATIONENDDATE,\n" +
                "TLL.LITIGATIONRESULT,\n" +
                "TLL.LITISTATUS,\n" +
                "TLL.ACTIONOBJECT,\n" +
                "TLL.ISEFFECT,\n" +
                "TLL.JUDGEMONEY,\n" +
                "TLL.DISPUTEINFO,\n" +
                "TLL.DISPUTEID,\n" +
                "TS.REALNAME,\n" +
                "TS.USERNAME,\n" +
                "TS.STATUS" +
                " FROM TBL_LEGAL_PROCEEDINGSRECORD TLP " +
                "LEFT JOIN TBL_LEGAL_LITIGATIONSETTLEMENT TLL ON TLP.LITIGATIONINFO = TLL.LITIGATIONID " +
                "LEFT JOIN TBL_STAFF TS ON TLP.CREATESTAFF = TS.STAFFID " +
                "WHERE TLL.LITIGATIONID =  "+ litigationId );

        sqlSb.append(" ORDER BY proceedId DESC ");
        String sql = sqlSb.toString();
        return sql;
    }
}
