package com.huabo.contract.mappersql;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hbfk.util.database.DataBaseSqlConfig;
import com.huabo.contract.entity.TblLegalArbitratsettlement;

public class TblLegalArbitratsettlementMapperSqlConfig {
	
	public String modifyNegotiatedSettlement(TblLegalArbitratsettlement arbitrat) throws Exception {
        StringBuffer sql = new StringBuffer("UPDATE TBL_LEGAL_ARBITRATSETTLEMENT SET COURTFIRST = '"+arbitrat.getCourtfirst()+"'");
        if(arbitrat.getAsdealdate() != null && !"".equals(arbitrat.getAsdealdate())) {
            sql.append(" ,ASDEALDATE = "+ DataBaseSqlConfig.getDateStrFormat(arbitrat.getAsdealdate()));
        }
        if(arbitrat.getAsfirsthearingdate() != null && !"".equals(arbitrat.getAsfirsthearingdate())) {
            sql.append(" ,ASFIRSTHEARINGDATE = "+ DataBaseSqlConfig.getDateStrFormat(arbitrat.getAsfirsthearingdate()));
        }
        if(arbitrat.getArbitrationamount() != null && !"".equals(arbitrat.getArbitrationamount())) {
            sql.append(" , ARBITRATIONAMOUNT = '"+arbitrat.getArbitrationamount()+"'");
        }
        if(arbitrat.getArbitrationenddate() != null ) {
            sql.append(" ,ARBITRATIONENDDATE = "+ DataBaseSqlConfig.getDateStrFormat(arbitrat.getArbitrationenddate()));
        }
        if(arbitrat.getArbitrationresult() != null && !"".equals(arbitrat.getArbitrationresult())) {
            sql.append(" , ARBITRATIONRESULT = '"+arbitrat.getArbitrationresult()+"'");
        }
        if(arbitrat.getNegotiate() != null && !"".equals(arbitrat.getNegotiate())) {
            sql.append(" , NEGOTIATEINFO = '"+arbitrat.getNegotiate()+"'");
        }
        if(arbitrat.getCreatestaff() != null && !"".equals(arbitrat.getCreatestaff())) {
            sql.append(" , CREATESTAFF = '"+arbitrat.getCreatestaff()+"'");
        }
        if(arbitrat.getCreatetime() != null && !"".equals(arbitrat.getCreatetime())) {
            sql.append(" ,CREATETIME = "+ DataBaseSqlConfig.getDateStrFormat(arbitrat.getCreatetime()));
        }
        if(arbitrat.getLinkorg() != null && !"".equals(arbitrat.getLinkorg())) {
            sql.append(" , LINKORG = '"+arbitrat.getLinkorg()+"'");
        }
        if(arbitrat.getArbitrastatus() != null && !"".equals(arbitrat.getArbitrastatus())) {
            sql.append(" , ARBITRASTATUS = '"+arbitrat.getArbitrastatus()+"'");
        }
        if(arbitrat.getCurrency() != null && !"".equals(arbitrat.getCurrency())) {
            sql.append(" , CURRENCY = '"+arbitrat.getCurrency()+"'");
        }
        if(arbitrat.getDisputeid() != null && !"".equals(arbitrat.getDisputeid())) {
            sql.append(" , DISPUTEID = '"+arbitrat.getDisputeid()+"'");
        }
        if(arbitrat.getDisputeitem() != null && !"".equals(arbitrat.getDisputeitem())) {
            sql.append(" , DISPUTEITEM = '"+arbitrat.getDisputeitem()+"'");
        }
        
        if(arbitrat.getArbitraorg() != null && !"".equals(arbitrat.getArbitraorg())) {
            sql.append(" , ARBITRAORG = '"+arbitrat.getArbitraorg()+"'");
        }
        if(arbitrat.getArbitralinkman() != null && !"".equals(arbitrat.getArbitralinkman())) {
            sql.append(" , ARBITRALINKMAN = '"+arbitrat.getArbitralinkman()+"'");
        }
        if(arbitrat.getEnteringperson() != null && !"".equals(arbitrat.getEnteringperson())) {
            sql.append(" , ENTERINGPERSON = '"+arbitrat.getEnteringperson()+"'");
        }
        if(arbitrat.getArbitratime() != null && !"".equals(arbitrat.getArbitratime())){
        	sql.append(" , ARBITRATIME = "+ DataBaseSqlConfig.getDateStrFormat(arbitrat.getArbitratime()));
        }

        sql.append(" WHERE ARBITRAID = '"+arbitrat.getArbitraid()+"'");
        return sql.toString();
    }
	
	public String findListByPageInfo(IPage<TblLegalArbitratsettlement> page,TblLegalArbitratsettlement negotia, BigDecimal disputeid) throws Exception {
        StringBuffer sqlSb = new StringBuffer("SELECT " +
                "TLA.*,\n" +
                "TLN.NEGOTIAID,\n" +
                "TLN.NEGOTIASTATUS,\n" +
                "TLN.COUNTERPART,\n" +
                "TLN.ISAGGREE,\n" +
                "TLN.ISPRESETCASE,\n" +
                "TLN.NEGETIARESULT,\n" +
                "TLN.JUDICIALSETTLEMENT,\n" +
                "TLN.SOLUTIONMODE,\n" +
                "TLN.COURTNAME,\n" +
                "TLN.SCHEMESTATUS,\n" +
                "TLN.MEDIATIONSCHEME,\n" +
                "TLD.DISPUTENO,\n" +
                "TLD.DISPUTESTATUS,\n" +
                "TLD.DISPUTETYPE,\n" +
                "TLD.CONTRACTINFO,\n" +
                "TLD.DISPUTECOURS,\n" +
                "TLD.ISUEGENT,\n" +
                "TLD.WHETHERSUED,\n" +
                "TLD.LITIGATIONPOS,\n" +
                "TLD.DISPUTEUNDERTAKER,\n" +
                "TLD.LASTDEALDATE,\n" +
                "TLD.SOLUTIONSUGGESTIONS,\n" +
                "TLD.URGENTMEMO,\n" +
                "TLD.LEGALEXAM,\n" +
                "TLD.COUNSELEXAM,\n" +
                "TLD.CHAIRMANEXAM,\n" +
                "TLD.GMANEXAM,\n" +
                "TLD.PLAINTIFF,\n" +
                "TLD.DEFENDANT,\n" +
                "TLD.ATTORNEY,\n" +
                "TLD.ATTORNEYPHONT,\n" +
                "TLD.ISATTORNEY" +
                " FROM TBL_LEGAL_ARBITRATSETTLEMENT TLA " +
                "LEFT JOIN TBL_LEGAL_NEGOTIATEDSETTLEMEN TLN ON TLA.NEGOTIATEINFO = TLN.NEGOTIAID " +
                "LEFT JOIN TBL_LEGAL_DISPUTREGISTRATION TLD ON TLN.DISPUINFO = TLD.DISPUTEID " +
                "WHERE TLA.LINKORG = " + negotia.getLinkorg());
        if ( disputeid != null){
            sqlSb.append(" AND TLD.DISPUTEID = " + disputeid );
        }
        if (negotia.getCourtfirst() != null) {
            sqlSb.append(" AND TLA.COURTFIRST like '%" + negotia.getCourtfirst() + "%' ");
        }
        if (negotia.getStartdate() != null && !"".equals(negotia.getStartdate())) {
            sqlSb.append(" AND TLA.ASDEALDATE >= " + DataBaseSqlConfig.getDateStrFormat(negotia.getStartdate()));
        }
        if (negotia.getEnddate() != null  && !"".equals(negotia.getEnddate())) {
            sqlSb.append(" AND TLA.ASDEALDATE <= "+ DataBaseSqlConfig.getDateStrFormat(negotia.getEnddate()));
        }
        sqlSb.append(" ORDER BY TLA.ARBITRAID DESC");
        String sql = sqlSb.toString();
        return sql;
    }
	
	public String saveDiputergistration(TblLegalArbitratsettlement arbitrat) throws Exception {
        StringBuffer column = new StringBuffer("INSERT INTO TBL_LEGAL_ARBITRATSETTLEMENT (ARBITRAID");
        StringBuffer value = new StringBuffer(" VALUES ("+arbitrat.getArbitraid());

        if(arbitrat.getCourtfirst() != null && !"".equals(arbitrat.getCourtfirst())){
            column.append(",COURTFIRST");
            value.append(",'"+arbitrat.getCourtfirst()+"'");
        }
        if(arbitrat.getAsdealdate() != null){
            column.append(",ASDEALDATE");
            value.append(","+ DataBaseSqlConfig.getDateStrFormat(arbitrat.getAsdealdate()));
        }
        if(arbitrat.getAsfirsthearingdate() != null){
            column.append(",ASFIRSTHEARINGDATE");
            value.append(","+ DataBaseSqlConfig.getDateStrFormat(arbitrat.getAsfirsthearingdate()));
        }
        if(arbitrat.getArbitrationamount() != null){
            column.append(",ARBITRATIONAMOUNT");
            value.append(",'"+arbitrat.getArbitrationamount()+"'");
        }
        if(arbitrat.getArbitrationenddate() != null){
            column.append(",ARBITRATIONENDDATE");
            //value.append(",'"+arbitrat.getArbitrationenddate()+"'");
            value.append(","+ DataBaseSqlConfig.getDateStrFormat(arbitrat.getArbitrationenddate()));
        }
        if(arbitrat.getArbitrationresult() != null && !"".equals(arbitrat.getArbitrationresult())){
            column.append(",ARBITRATIONRESULT");
            value.append(",'"+arbitrat.getArbitrationresult()+"'");
        }
        if(arbitrat.getNegotiateinfo() != null){
            column.append(",NEGOTIATEINFO");
            value.append(",'"+arbitrat.getNegotiateinfo()+"'");
        }
        if(arbitrat.getCreatestaff() != null){
            column.append(",CREATESTAFF");
            value.append(",'"+arbitrat.getCreatestaff()+"'");
        }
//        if(arbitrat.getCreatetime() != null && !"".equals(arbitrat.getCreatetime())){
//            column.append(",CREATETIME");
//          //  value.append(",'"+arbitrat.getCreatetime()+"'");
//            value.append(",CREATETIME('"+ DateUtil.parseDate(arbitrat.getCreatetime(),"yyyy-MM-dd HH:mm:ss") +"', 'YYYY-MM-DD HH24:MI:SS')");
//        }
        if(arbitrat.getLinkorg() != null && !"".equals(arbitrat.getLinkorg())){
            column.append(",LINKORG");
            value.append(",'"+arbitrat.getLinkorg()+"'");
        }
        if(arbitrat.getArbitrastatus() != null && !"".equals(arbitrat.getArbitrastatus())){
            column.append(",ARBITRASTATUS");
            value.append(",'"+arbitrat.getArbitrastatus()+"'");
        }
        if(arbitrat.getCurrency() != null && !"".equals(arbitrat.getCurrency())){
            column.append(",CURRENCY");
            value.append(",'"+arbitrat.getCurrency()+"'");
        }
        if(arbitrat.getDisputeid() != null && !"".equals(arbitrat.getDisputeid())){
            column.append(",DISPUTEID");
            value.append(",'"+arbitrat.getDisputeid()+"'");
        }
        if(arbitrat.getDisputeitem() != null && !"".equals(arbitrat.getDisputeitem())){
            column.append(",DISPUTEITEM");
            value.append(",'"+arbitrat.getDisputeitem()+"'");
        }
        if(arbitrat.getArbitraorg() != null && !"".equals(arbitrat.getArbitraorg())){
            column.append(",ARBITRAORG");//仲裁机构
            value.append(",'"+arbitrat.getArbitraorg()+"'");
        }
        if(arbitrat.getArbitralinkman() != null && !"".equals(arbitrat.getArbitralinkman())){
            column.append(",ARBITRALINKMAN");//仲裁机构联系人
            value.append(",'"+arbitrat.getArbitralinkman()+"'");
        }
        if(arbitrat.getEnteringperson() != null && !"".equals(arbitrat.getEnteringperson())){
            column.append(",ENTERINGPERSON");//录入人
            value.append(",'"+arbitrat.getEnteringperson()+"'");
        }
        if(arbitrat.getArbitratime() != null){
            column.append(",ARBITRATIME");
            value.append(","+ DataBaseSqlConfig.getDateStrFormat(arbitrat.getArbitratime()));
        }
        if(arbitrat.getIsexternal() != null && !"".equals(arbitrat.getIsexternal())){
            column.append(",ISEXTERNAL");//是否外聘
            value.append(",'"+arbitrat.getIsexternal()+"'");
        }
        if(arbitrat.getAgencyperson() != null && !"".equals(arbitrat.getAgencyperson())){
            column.append(",AGENCYPERSON");//代理人
            value.append(",'"+arbitrat.getAgencyperson()+"'");
        }
        if(arbitrat.getContactphone() != null && !"".equals(arbitrat.getContactphone())){
            column.append(",CONTACTPHONE"); //联系方式
            value.append(",'"+arbitrat.getContactphone()+"'");
        }

        column.append(")");
        value.append(")");
        String sql = column.toString()+value.toString();
        return sql;
    }

}
