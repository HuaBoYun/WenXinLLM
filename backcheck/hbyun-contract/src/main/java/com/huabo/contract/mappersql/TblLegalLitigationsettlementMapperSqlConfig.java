package com.huabo.contract.mappersql;

import java.math.BigDecimal;

import org.apache.commons.lang.StringUtils;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hbfk.util.database.DataBaseSqlConfig;
import com.huabo.contract.entity.TblLegalLitigationsettlement;

public class TblLegalLitigationsettlementMapperSqlConfig {


    public String findListByPageInfo(IPage<TblLegalLitigationsettlement> page, TblLegalLitigationsettlement litigation,BigDecimal disputeid) throws Exception {
        StringBuffer sqlSb = new StringBuffer("SELECT " +
                "TLL.*,\n" +
                "TLD.DISPUTENO,\n" +
                "TLD.CONTRACTINFO,\n" +
                "TLD.DISPUTECOURS,\n" +
                "TLD.WHETHERSUED,\n" +
                "TLD.LITIGATIONPOS,\n" +
                "TLD.DISPUTEUNDERTAKER,\n" +
                "TLD.LASTDEALDATE,\n" +
                "TLD.SOLUTIONSUGGESTIONS,\n" +
                "TLD.LEGALEXAM,\n" +
                "TLD.COUNSELEXAM,\n" +
                "TLD.CHAIRMANEXAM,\n" +
                "TLD.GMANEXAM,\n" +
                "TLD.PLAINTIFF,\n" +
                "TLD.DEFENDANT,\n" +
                "TLD.ATTORNEY,\n" +
                "TLD.ATTORNEYPHONT" +
                " FROM TBL_LEGAL_LITIGATIONSETTLEMENT TLL " +
                "LEFT JOIN TBL_LEGAL_DISPUTREGISTRATION TLD ON TLL.DISPUTEID = TLD.DISPUTEID " +
                "WHERE TLL.LINKORG = "+ litigation.getLinkorg() );

        if (disputeid != null){
            sqlSb.append(" AND TLD.DISPUTEID = " + disputeid );
        }
        if (litigation.getFirstcourt() != null) {
            sqlSb.append(" AND TLL.FIRSTCOURT like '%" + litigation.getFirstcourt() + "%' ");
        }
        if (litigation.getPresidingjudge() != null) {
            sqlSb.append(" AND TLL.PRESIDINGJUDGE like '%" + litigation.getPresidingjudge() + "%' ");
        }
        if (!StringUtils.isBlank(litigation.getStartdate())) {
            sqlSb.append(" and TLL.DEALDATE >= " + DataBaseSqlConfig.getDateStrFormat(litigation.getStartdate()));
        }

        if (!StringUtils.isBlank(litigation.getEnddate())) {
            sqlSb.append(" and TLL.DEALDATE <= " + DataBaseSqlConfig.getDateStrFormat(litigation.getEnddate()));
        }

        sqlSb.append(" ORDER BY TLL.LITIGATIONID DESC");
        String sql = sqlSb.toString();
        return sql;
    }

    public String saveLitigationSettlement(TblLegalLitigationsettlement litigation) throws Exception {
        StringBuffer column = new StringBuffer("INSERT INTO TBL_LEGAL_LITIGATIONSETTLEMENT (LITIGATIONID");
        StringBuffer value = new StringBuffer(" VALUES ("+litigation.getLitigationid());

        if(litigation.getFirstcourt() != null && !"".equals(litigation.getFirstcourt())) {
            column.append(",FIRSTCOURT");
            value.append(",'"+litigation.getFirstcourt()+"'");
        }
        if(litigation.getPresidingjudge() != null && !"".equals(litigation.getPresidingjudge())) {
            column.append(",PRESIDINGJUDGE");
            value.append(",'"+litigation.getPresidingjudge()+"'");
        }
        if(litigation.getCollegialpanel() != null && !"".equals(litigation.getCollegialpanel())) {
            column.append(",COLLEGIALPANEL");
            value.append(",'"+litigation.getCollegialpanel()+"'");
        }
        if(litigation.getLitigationamount() != null) {
            column.append(",LITIGATIONAMOUNT");
            value.append(",'"+litigation.getLitigationamount()+"'");
        }
        if(litigation.getDealdate() != null && !"".equals(litigation.getDealdate())) {
            column.append(",DEALDATE");
            value.append("," + DataBaseSqlConfig.getDateStrFormat(litigation.getDealdate()));
            //value.append(",'"+litigation.getDealdate()+"'");
        }
        if(litigation.getFirsthearingdate() != null) {
            column.append(",FIRSTHEARINGDATE");
            value.append("," + DataBaseSqlConfig.getDateStrFormat(litigation.getFirsthearingdate()));
           // value.append(",'"+litigation.getFirsthearingdate()+"'");
        }
        if(litigation.getLitigationenddate() != null) {
            column.append(",LITIGATIONENDDATE");
            value.append(","+ DataBaseSqlConfig.getDateStrFormat(litigation.getLitigationenddate()));
           // value.append(",'"+litigation.getLitigationenddate()+"'");
        }
        if(litigation.getLitigationresult() != null) {
            column.append(",LITIGATIONRESULT");
            value.append(",'"+litigation.getLitigationresult()+"'");
        }
        if(litigation.getCreatestaff() != null) {
            column.append(",CREATESTAFF");
            value.append(",'"+litigation.getCreatestaff()+"'");
        }
//        if(litigation.getCreatetime() != null && !"".equals(litigation.getCreatetime())) {
//            column.append(",CREATETIME");
//            value.append(",'"+litigation.getCreatetime()+"'");
//        }
        if(litigation.getLinkorg() != null && !"".equals(litigation.getLinkorg())) {
            column.append(",LINKORG");
            value.append(",'"+litigation.getLinkorg()+"'");
        }
        if(litigation.getLitistatus() != null && !"".equals(litigation.getLitistatus())) {
            column.append(",LITISTATUS");
            value.append(",'"+litigation.getLitistatus()+"'");
        }
        if(litigation.getActionobject() != null && !"".equals(litigation.getActionobject())) {
            column.append(",ACTIONOBJECT");
            value.append(",'"+litigation.getActionobject()+"'");
        }
        if(litigation.getIseffect() != null && !"".equals(litigation.getIseffect())) {
            column.append(",ISEFFECT");
            value.append(",'"+litigation.getIseffect()+"'");
        }
        if(litigation.getJudgemoney() != null && !"".equals(litigation.getJudgemoney())) {
            column.append(",JUDGEMONEY");
            value.append(",'"+litigation.getJudgemoney()+"'");
        }
        if(litigation.getDisputeinfo() != null && !"".equals(litigation.getDisputeinfo())) {
            column.append(",DISPUTEINFO");
            value.append(",'"+litigation.getDisputeinfo()+"'");
        }
        
        //==
        if(litigation.getDisputeid() != null && !"".equals(litigation.getDisputeid())) {
            column.append(",DISPUTEID");
            value.append(",'"+litigation.getDisputeid()+"'");
        }
        if(litigation.getArbitraid() != null && !"".equals(litigation.getArbitraid())) {
            column.append(",ARBITRAID");
            value.append(",'"+litigation.getArbitraid()+"'");
        }
        if(litigation.getFillunit() != null && !"".equals(litigation.getFillunit())) {
            column.append(",FILLUNIT");
            value.append(",'"+litigation.getFillunit()+"'");
        }
        if(litigation.getBelonggroup() != null && !"".equals(litigation.getBelonggroup())) {
            column.append(",BELONGGROUP");
            value.append(",'"+litigation.getBelonggroup()+"'");
        }
        if(litigation.getCausecase() != null && !"".equals(litigation.getCausecase())) {
            column.append(",CAUSECASE");
            value.append(",'"+litigation.getCausecase()+"'");
        }
        if(litigation.getDisputehand() != null && !"".equals(litigation.getDisputehand())) {
            column.append(",DISPUTEHAND");
            value.append(",'"+litigation.getDisputehand()+"'");
        }
        if(litigation.getDisputename() != null && !"".equals(litigation.getDisputename())) {
            column.append(",DISPUTENAME");
            value.append(",'"+litigation.getDisputename()+"'");
        }
        
        if(litigation.getDisputetype() != null && !"".equals(litigation.getDisputetype())) {
            column.append(",DISPUTETYPE");
            value.append(",'"+litigation.getDisputetype()+"'");
        }
        if(litigation.getLawfirm() != null && !"".equals(litigation.getLawfirm())) {
            column.append(",LAWFIRM");
            value.append(",'"+litigation.getLawfirm()+"'");
        }
        if(litigation.getIsattorney() != null && !"".equals(litigation.getIsattorney())) {
            column.append(",ISATTORNEY");
            value.append(",'"+litigation.getIsattorney()+"'");
        }
        if(litigation.getLawname() != null && !"".equals(litigation.getLawname())) {
            column.append(",LAWNAME");
            value.append(",'"+litigation.getLawname()+"'");
        }
        if(litigation.getLawtype() != null && !"".equals(litigation.getLawtype())) {
            column.append(",LAWTYPE");
            value.append(",'"+litigation.getLawtype()+"'");
        }
        if(litigation.getIslinkcontract() != null && !"".equals(litigation.getIslinkcontract())) {
            column.append(",ISLINKCONTRACT");
            value.append(",'"+litigation.getIslinkcontract()+"'");
        }
        if(litigation.getDisputeunder() != null && !"".equals(litigation.getDisputeunder())) {
            column.append(",DISPUTEUNDER");
            value.append(",'"+litigation.getDisputeunder()+"'");
        }
        if(litigation.getLitigiousstatus() != null && !"".equals(litigation.getLitigiousstatus())) {
            column.append(",LITIGIOUSSTATUS");
            value.append(",'"+litigation.getLitigiousstatus()+"'");
        }
        if(litigation.getOurdisputeorg() != null && !"".equals(litigation.getOurdisputeorg())) {
            column.append(",OURDISPUTEORG");
            value.append(",'"+litigation.getOurdisputeorg()+"'");
        }
        if(litigation.getOppositeparty() != null && !"".equals(litigation.getOppositeparty())) {
            column.append(",OPPOSITEPARTY");
            value.append(",'"+litigation.getOppositeparty()+"'");
        }
        
        if(litigation.getSubjectamount() != null && !"".equals(litigation.getSubjectamount())) {
            column.append(",SUBJECTAMOUNT");
            value.append(",'"+litigation.getSubjectamount()+"'");
        }
        if(litigation.getJurisdiction() != null && !"".equals(litigation.getJurisdiction())) {
            column.append(",JURISDICTION");
            value.append(",'"+litigation.getJurisdiction()+"'");
        }
        if(litigation.getDisputestatus() != null && !"".equals(litigation.getDisputestatus())) {
            column.append(",DISPUTESTATUS");
            value.append(",'"+litigation.getDisputestatus()+"'");
        }
        if(litigation.getBiztime() != null && !"".equals(litigation.getBiztime())) {
            column.append(",BIZTIME");
            value.append(","+ DataBaseSqlConfig.getDateStrFormat(litigation.getBiztime()));
        }
        if(litigation.getCasetime() != null && !"".equals(litigation.getCasetime())) {
            column.append(",CASETIME");
            value.append("," + DataBaseSqlConfig.getDateStrFormat(litigation.getCasetime()));
        }
        if(litigation.getIscriminalcases() != null && !"".equals(litigation.getIscriminalcases())) {
            column.append(",ISCRIMINALCASES");
            value.append(",'"+litigation.getIscriminalcases()+"'");
        }
        if(litigation.getIsrepayment() != null && !"".equals(litigation.getIsrepayment())) {
            column.append(",ISREPAYMENT");
            value.append(",'"+litigation.getIsrepayment()+"'");
        }
        
        if(litigation.getIspreservation() != null && !"".equals(litigation.getIspreservation())) {
            column.append(",ISPRESERVATION");
            value.append(",'"+litigation.getIspreservation()+"'");
        }
        if(litigation.getExecstatus() != null && !"".equals(litigation.getExecstatus())) {
            column.append(",EXECSTATUS");
            value.append(",'"+litigation.getExecstatus()+"'");
        }
        if(litigation.getIsurgent() != null && !"".equals(litigation.getIsurgent())) {
            column.append(",ISURGENT");
            value.append(",'"+litigation.getIsurgent()+"'");
        }
        if(litigation.getUrgentmemo() != null && !"".equals(litigation.getUrgentmemo())) {
            column.append(",URGENTMEMO");
            value.append(",'"+litigation.getUrgentmemo()+"'");
        }
        if(litigation.getCasebasicinfo() != null && !"".equals(litigation.getCasebasicinfo())) {
            column.append(",CASEBASICINFO");
            value.append(",'"+litigation.getCasebasicinfo()+"'");
        }
        if(litigation.getTakemeasures() != null && !"".equals(litigation.getTakemeasures())) {
            column.append(",TAKEMEASURES");
            value.append(",'"+litigation.getTakemeasures()+"'");
        }
        if(litigation.getJudgmentresult() != null && !"".equals(litigation.getJudgmentresult())) {
            column.append(",JUDGMENTRESULT");
            value.append(",'"+litigation.getJudgmentresult()+"'");
        }
        column.append(")");
        value.append(")");
        String sql = column.toString()+value.toString();
        return sql;
    }



    public String updateModifyLitigationSettlement(TblLegalLitigationsettlement oldLitigation) throws Exception {
        StringBuffer sql = new StringBuffer("UPDATE TBL_LEGAL_LITIGATIONSETTLEMENT SET FIRSTCOURT = '"+oldLitigation.getFirstcourt()+"'");

        if(oldLitigation.getPresidingjudge() != null && !"".equals(oldLitigation.getPresidingjudge())) {
            sql.append(" , PRESIDINGJUDGE = '"+oldLitigation.getPresidingjudge()+"'");
        }
        if(oldLitigation.getCollegialpanel() != null && !"".equals(oldLitigation.getCollegialpanel())) {
            sql.append(" , COLLEGIALPANEL = '"+oldLitigation.getCollegialpanel()+"'");
        }
        if(oldLitigation.getLitigationamount() != null && !"".equals(oldLitigation.getLitigationamount())) {
            sql.append(" , LITIGATIONAMOUNT = '"+oldLitigation.getLitigationamount()+"'");
        }
        if(oldLitigation.getDealdate() != null && !"".equals(oldLitigation.getDealdate())) {
            sql.append(" ,DEALDATE = "+ DataBaseSqlConfig.getDateStrFormat(oldLitigation.getDealdate()));
        }
        if(oldLitigation.getFirsthearingdate() != null && !"".equals(oldLitigation.getFirsthearingdate())) {
            sql.append(" ,FIRSTHEARINGDATE = "+ DataBaseSqlConfig.getDateStrFormat(oldLitigation.getFirsthearingdate()));
        }
        if(oldLitigation.getLitigationenddate() != null && !"".equals(oldLitigation.getLitigationenddate())) {
            sql.append(" ,LITIGATIONENDDATE = "+ DataBaseSqlConfig.getDateStrFormat(oldLitigation.getLitigationenddate()));
        }
        if(oldLitigation.getLitigationresult() != null && !"".equals(oldLitigation.getLitigationresult())) {
            sql.append(" , LITIGATIONRESULT = '"+oldLitigation.getLitigationresult()+"'");
        }
        if(oldLitigation.getCreatestaff() != null && !"".equals(oldLitigation.getCreatestaff())) {
            sql.append(" , CREATESTAFF = '"+oldLitigation.getCreatestaff()+"'");
        }
        if(oldLitigation.getCreatetime() != null && !"".equals(oldLitigation.getCreatetime())) {
            sql.append(" ,CREATETIME = "+ DataBaseSqlConfig.getDateStrFormat(oldLitigation.getCreatetime()));
        }
        if(oldLitigation.getLinkorg() != null && !"".equals(oldLitigation.getLinkorg())) {
            sql.append(" , LINKORG = '"+oldLitigation.getLinkorg()+"'");
        }
        if(oldLitigation.getLitistatus() != null && !"".equals(oldLitigation.getLitistatus())) {
            sql.append(" , LITISTATUS = '"+oldLitigation.getLitistatus()+"'");
        }
        if(oldLitigation.getActionobject() != null && !"".equals(oldLitigation.getActionobject())) {
            sql.append(" , ACTIONOBJECT = '"+oldLitigation.getActionobject()+"'");
        }
        if(oldLitigation.getIseffect() != null && !"".equals(oldLitigation.getIseffect())) {
            sql.append(" , ISEFFECT = '"+oldLitigation.getIseffect()+"'");
        }
        if(oldLitigation.getJudgemoney() != null && !"".equals(oldLitigation.getJudgemoney())) {
            sql.append(" , JUDGEMONEY = '"+oldLitigation.getJudgemoney()+"'");
        }
        if(oldLitigation.getDisputeinfo() != null && !"".equals(oldLitigation.getDisputeinfo())) {
            sql.append(" , DISPUTEINFO = '"+oldLitigation.getDisputeinfo()+"'");
        }
        
        //==
        if(oldLitigation.getDisputeid() != null && !"".equals(oldLitigation.getDisputeid())) {
            sql.append(" , DISPUTEID = '"+oldLitigation.getDisputeid()+"'");
        }
        if(oldLitigation.getArbitraid() != null && !"".equals(oldLitigation.getArbitraid())) {
            sql.append(" , ARBITRAID = '"+oldLitigation.getArbitraid()+"'");
        }
        if(oldLitigation.getFillunit() != null && !"".equals(oldLitigation.getFillunit())) {
            sql.append(" , FILLUNIT = '"+oldLitigation.getFillunit()+"'");
        }
        if(oldLitigation.getBelonggroup() != null && !"".equals(oldLitigation.getBelonggroup())) {
            sql.append(" , BELONGGROUP = '"+oldLitigation.getBelonggroup()+"'");
        }
        if(oldLitigation.getCausecase() != null && !"".equals(oldLitigation.getCausecase())) {
            sql.append(" , CAUSECASE = '"+oldLitigation.getCausecase()+"'");
        }
        if(oldLitigation.getDisputehand() != null && !"".equals(oldLitigation.getDisputehand())) {
            sql.append(" , DISPUTEHAND = '"+oldLitigation.getDisputehand()+"'");
        }
        if(oldLitigation.getDisputename() != null && !"".equals(oldLitigation.getDisputename())) {
            sql.append(" , DISPUTENAME = '"+oldLitigation.getDisputename()+"'");
        }
        if(oldLitigation.getDisputetype() != null && !"".equals(oldLitigation.getDisputetype())) {
            sql.append(" , DISPUTETYPE = '"+oldLitigation.getDisputetype()+"'");
        }
        if(oldLitigation.getLawfirm() != null && !"".equals(oldLitigation.getLawfirm())) {
            sql.append(" , LAWFIRM = '"+oldLitigation.getLawfirm()+"'");
        }
        
        if(oldLitigation.getIsattorney() != null && !"".equals(oldLitigation.getIsattorney())) {
            sql.append(" , ISATTORNEY = '"+oldLitigation.getIsattorney()+"'");
        }
        if(oldLitigation.getLawname() != null && !"".equals(oldLitigation.getLawname())) {
            sql.append(" , LAWNAME = '"+oldLitigation.getLawname()+"'");
        }
        if(oldLitigation.getLawtype() != null && !"".equals(oldLitigation.getLawtype())) {
            sql.append(" , LAWTYPE = '"+oldLitigation.getLawtype()+"'");
        }
        if(oldLitigation.getIslinkcontract() != null && !"".equals(oldLitigation.getIslinkcontract())) {
            sql.append(" , ISLINKCONTRACT = '"+oldLitigation.getIslinkcontract()+"'");
        }
        if(oldLitigation.getDisputeunder() != null && !"".equals(oldLitigation.getDisputeunder())) {
            sql.append(" , DISPUTEUNDER = '"+oldLitigation.getDisputeunder()+"'");
        }
        if(oldLitigation.getLitigiousstatus() != null && !"".equals(oldLitigation.getLitigiousstatus())) {
            sql.append(" , LITIGIOUSSTATUS = '"+oldLitigation.getLitigiousstatus()+"'");
        }
        if(oldLitigation.getOurdisputeorg() != null && !"".equals(oldLitigation.getOurdisputeorg())) {
            sql.append(" , OURDISPUTEORG = '"+oldLitigation.getOurdisputeorg()+"'");
        }
        if(oldLitigation.getOppositeparty() != null && !"".equals(oldLitigation.getOppositeparty())) {
            sql.append(" , OPPOSITEPARTY = '"+oldLitigation.getOppositeparty()+"'");
        }
        if(oldLitigation.getSubjectamount() != null && !"".equals(oldLitigation.getSubjectamount())) {
            sql.append(" , SUBJECTAMOUNT = '"+oldLitigation.getSubjectamount()+"'");
        }
        if(oldLitigation.getJurisdiction() != null && !"".equals(oldLitigation.getJurisdiction())) {
            sql.append(" , JURISDICTION = '"+oldLitigation.getJurisdiction()+"'");
        }
        if(oldLitigation.getDisputestatus() != null && !"".equals(oldLitigation.getDisputestatus())) {
            sql.append(" , DISPUTESTATUS = '"+oldLitigation.getDisputestatus()+"'");
        }
        if(oldLitigation.getBiztime() != null && !"".equals(oldLitigation.getBiztime())) {
            sql.append(" ,BIZTIME = "+ DataBaseSqlConfig.getDateStrFormat(oldLitigation.getBiztime()));
        }
        if(oldLitigation.getCasetime() != null && !"".equals(oldLitigation.getCasetime())) {
            sql.append(" ,CASETIME = "+ DataBaseSqlConfig.getDateStrFormat(oldLitigation.getCasetime()));
        }
        
        if(oldLitigation.getIscriminalcases() != null && !"".equals(oldLitigation.getIscriminalcases())) {
            sql.append(" , ISCRIMINALCASES = '"+oldLitigation.getIscriminalcases()+"'");
        }
        if(oldLitigation.getIsrepayment() != null && !"".equals(oldLitigation.getIsrepayment())) {
            sql.append(" , ISREPAYMENT = '"+oldLitigation.getIsrepayment()+"'");
        }
        if(oldLitigation.getIspreservation() != null && !"".equals(oldLitigation.getIspreservation())) {
            sql.append(" , ISPRESERVATION = '"+oldLitigation.getIspreservation()+"'");
        }
        if(oldLitigation.getExecstatus() != null && !"".equals(oldLitigation.getExecstatus())) {
            sql.append(" , EXECSTATUS = '"+oldLitigation.getExecstatus()+"'");
        }
        if(oldLitigation.getIsurgent() != null && !"".equals(oldLitigation.getIsurgent())) {
            sql.append(" , ISURGENT = '"+oldLitigation.getIsurgent()+"'");
        }
        if(oldLitigation.getUrgentmemo() != null && !"".equals(oldLitigation.getUrgentmemo())) {
            sql.append(" , URGENTMEMO = '"+oldLitigation.getUrgentmemo()+"'");
        }
        if(oldLitigation.getCasebasicinfo() != null && !"".equals(oldLitigation.getCasebasicinfo())) {
            sql.append(" , CASEBASICINFO = '"+oldLitigation.getCasebasicinfo()+"'");
        }
        if(oldLitigation.getTakemeasures() != null && !"".equals(oldLitigation.getTakemeasures())) {
            sql.append(" , TAKEMEASURES = '"+oldLitigation.getTakemeasures()+"'");
        }
        if(oldLitigation.getJudgmentresult() != null && !"".equals(oldLitigation.getJudgmentresult())) {
            sql.append(" , JUDGMENTRESULT = '"+oldLitigation.getJudgmentresult()+"'");
        }
        
        sql.append(" WHERE LITIGATIONID = '"+oldLitigation.getLitigationid()+"'");
        return sql.toString();
    }
}
