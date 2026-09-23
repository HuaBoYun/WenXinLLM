package com.huabo.contract.mappersql;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hbfk.util.database.DataBaseSqlConfig;
import com.huabo.contract.entity.TblLegalQualification;

public class TblLegalQualificationMapperSqlConfig {


    public String findListByPageInfo(IPage<TblLegalQualification> page,TblLegalQualification qualification, BigDecimal disputeid) throws Exception {
        StringBuffer sqlSb = new StringBuffer("select * from TBL_LEGAL_QUALIFICATION tlq " +
                "LEFT JOIN TBL_LEGAL_DISPUTREGISTRATION tld on tlq.DISPUTEINFO = tld.DISPUTEID " +
                "WHERE tlq.LINKORG = "+ qualification.getLinkorg() );


        if (disputeid != null){
            sqlSb.append(" AND tld.DISPUTEID = " + disputeid );
        }
        if (qualification.getDisputeitem() != null && !"".equals(qualification.getDisputeitem())) {
            sqlSb.append(" AND tld.DISPUTEITEM LIKE '%" + qualification.getDisputeitem() + "%'");
        }
        if (qualification.getExceteamount() != null) {
            sqlSb.append(" AND EXCETEAMOUNT  <= " + qualification.getExceteamount());
        }
        if (qualification.getMinExceteAmount() != null) {
            sqlSb.append(" AND EXCETEAMOUNT  >= " + qualification.getMinExceteAmount());
        }
        if (qualification.getPreservedamount() != null) {
            sqlSb.append(" AND PRESERVEDAMOUNT  <= " + qualification.getPreservedamount());
        }
        if (qualification.getMinPreservedAmount() != null) {
            sqlSb.append(" AND PRESERVEDAMOUNT  >= " + qualification.getMinPreservedAmount());
        }
        if (qualification.getPreservednature() != null && !"".equals(qualification.getPreservednature())) {
            sqlSb.append(" AND PRESERVEDNATURE LIKE '%" + qualification.getPreservednature() + "%'");
        }

        sqlSb.append(" ORDER BY tlq.QUALID DESC");
        String sql = sqlSb.toString();
        return sql;
    }

    public String saveQualification(TblLegalQualification qualification) throws Exception {
        StringBuffer column = new StringBuffer("INSERT INTO TBL_LEGAL_QUALIFICATION (QUALID");
        StringBuffer value = new StringBuffer(" VALUES ("+qualification.getQualid());

        if (qualification.getLinkorg() != null) {
            column.append(",LINKORG");
            value.append(",'" + qualification.getLinkorg() + "'");
        }
        if (qualification.getCreatestaff() != null) {
            column.append(",CREATESTAFF");
            value.append(",'" + qualification.getCreatestaff() + "'");
        }
        if (qualification.getCreatetime() != null) {
            column.append(",CREATETIME");
            value.append("," + DataBaseSqlConfig.getDateStrFormat(qualification.getCreatetime()));
        }
        if (qualification.getDisputeinfo() != null) {
            column.append(",DISPUTEINFO");
            value.append(",'" + qualification.getDisputeinfo() + "'");
        }
        if (qualification.getApplypreservation() != null && !"".equals(qualification.getApplypreservation())) {
            column.append(",APPLYPRESERVATION");
            value.append(",'" + qualification.getApplypreservation() + "'");
        }
        if (qualification.getPreservedamount() != null && !"".equals(qualification.getPreservedamount())) {
            column.append(",PRESERVEDAMOUNT");
            value.append(",'" + qualification.getPreservedamount() + "'");
        }
        if (qualification.getPreservednature() != null && !"".equals(qualification.getPreservednature())) {
            column.append(",PRESERVEDNATURE");
            value.append(",'" + qualification.getPreservednature() + "'");
        }
        if (qualification.getIsperformed() != null && !"".equals(qualification.getIsperformed())) {
            column.append(",ISPERFORMED");
            value.append(",'" + qualification.getIsperformed() + "'");
        }
        if (qualification.getExceteamount() != null && !"".equals(qualification.getExceteamount())) {
            column.append(",EXCETEAMOUNT");
            value.append(",'" + qualification.getExceteamount() + "'");
        }
        if (qualification.getIscancel() != null && !"".equals(qualification.getIscancel())) {
            column.append(",ISCANCEL");
            value.append(",'" + qualification.getIscancel() + "'");
        }
        if (qualification.getStillfrozen() != null && !"".equals(qualification.getStillfrozen())) {
            column.append(",STILLFROZEN");
            value.append(",'" + qualification.getStillfrozen() + "'");
        }
        if (qualification.getPreservation() != null && !"".equals(qualification.getPreservation())) {
            column.append(",PRESERVATION");
            value.append(",'" + qualification.getPreservation() + "'");
        }
        if (qualification.getPreservedname() != null && !"".equals(qualification.getPreservedname())) {
            column.append(",PRESERVEDNAME");
            value.append(",'" + qualification.getPreservedname() + "'");
        }
        if (qualification.getExecutionphase() != null && !"".equals(qualification.getExecutionphase())) {
            column.append(",EXECUTIONPHASE");
            value.append(",'" + qualification.getExecutionphase() + "'");
        }
        if (qualification.getPreservedcode() != null && !"".equals(qualification.getPreservedcode())) {
            column.append(",PRESERVEDCODE");
            value.append(",'" + qualification.getPreservedcode() + "'");
        }
        if (qualification.getCourt() != null && !"".equals(qualification.getCourt())) {
            column.append(",COURT");
            value.append(",'" + qualification.getCourt() + "'");
        }
        if (qualification.getSubjectmatter() != null && !"".equals(qualification.getSubjectmatter())) {
            column.append(",SUBJECTMATTER");
            value.append(",'" + qualification.getSubjectmatter() + "'");
        }
        if (qualification.getApplicant() != null && !"".equals(qualification.getApplicant())) {
            column.append(",APPLICANT");
            value.append(",'" + qualification.getApplicant() + "'");
        }
        if (qualification.getRespondent() != null && !"".equals(qualification.getRespondent())) {
            column.append(",RESPONDENT");
            value.append(",'" + qualification.getRespondent() + "'");
        }
        if (qualification.getApplyamount() != null && !"".equals(qualification.getApplyamount())) {
            column.append(",APPLYAMOUNT");
            value.append(",'" + qualification.getApplyamount() + "'");
        }
        if (qualification.getFrozenamount() != null && !"".equals(qualification.getFrozenamount())) {
            column.append(",FROZENAMOUNT");
            value.append(",'" + qualification.getFrozenamount() + "'");
        }
        if (qualification.getKouhuaamount() != null && !"".equals(qualification.getKouhuaamount())) {
            column.append(",KOUHUAAMOUNT");
            value.append(",'" + qualification.getKouhuaamount() + "'");
        }
        if (qualification.getFrozenmemo() != null && !"".equals(qualification.getFrozenmemo())) {
            column.append(",FROZENMEMO");
            value.append(",'" + qualification.getFrozenmemo() + "'");
        }
        if (qualification.getActualpreserved() != null && !"".equals(qualification.getActualpreserved())) {
            column.append(",ACTUALPRESERVED");
            value.append(",'" + qualification.getActualpreserved() + "'");
        }


        column.append(")");
        value.append(")");
        String sql = column.toString() + value.toString();
        return sql;
    }


    public String updateModifyQualification(TblLegalQualification old) throws Exception {
        StringBuffer sql = new StringBuffer("UPDATE TBL_LEGAL_QUALIFICATION SET PRESERVEDNATURE = '"+old.getPreservednature()+"'");

        if(old.getLinkorg() != null && !"".equals(old.getLinkorg())) {
            sql.append(" , LINKORG = '"+old.getLinkorg()+"'");
        }
        if(old.getCreatestaff() != null && !"".equals(old.getCreatestaff())) {
            sql.append(" , CREATESTAFF = '"+old.getCreatestaff()+"'");
        }
        if(old.getCreatetime() != null) {
            sql.append(" , CREATETIME = "+DataBaseSqlConfig.getDateStrFormat(old.getCreatetime()));
        }
        if(old.getDisputeinfo() != null && !"".equals(old.getDisputeinfo())) {
            sql.append(" , DISPUTEINFO = '"+old.getDisputeinfo()+"'");
        }
        if(old.getApplypreservation() != null && !"".equals(old.getApplypreservation())) {
            sql.append(" , APPLYPRESERVATION = '"+old.getApplypreservation()+"'");
        }
        if(old.getPreservedamount() != null && !"".equals(old.getPreservedamount())) {
            sql.append(" , PRESERVEDAMOUNT = '"+old.getPreservedamount()+"'");
        }
        if(old.getIsperformed() != null && !"".equals(old.getIsperformed())) {
            sql.append(" , ISPERFORMED = '"+old.getIsperformed()+"'");
        }
        if(old.getExceteamount() != null && !"".equals(old.getExceteamount())) {
            sql.append(" , EXCETEAMOUNT = '"+old.getExceteamount()+"'");
        }
        if(old.getIscancel() != null && !"".equals(old.getIscancel())) {
            sql.append(" , ISCANCEL = '"+old.getIscancel()+"'");
        }
        if(old.getStillfrozen() != null && !"".equals(old.getStillfrozen())) {
            sql.append(" , STILLFROZEN = '"+old.getStillfrozen()+"'");
        }
        if(old.getPreservation() != null && !"".equals(old.getPreservation())) {
            sql.append(" , PRESERVATION = '"+old.getPreservation()+"'");
        }
        if(old.getPreservedname() != null && !"".equals(old.getPreservedname())) {
            sql.append(" , PRESERVEDNAME = '"+old.getPreservedname()+"'");
        }
        if(old.getExecutionphase() != null && !"".equals(old.getExecutionphase())) {
            sql.append(" , EXECUTIONPHASE = '"+old.getExecutionphase()+"'");
        }
        if(old.getPreservedcode() != null && !"".equals(old.getPreservedcode())) {
            sql.append(" , PRESERVEDCODE = '"+old.getPreservedcode()+"'");
        }
        if(old.getCourt() != null && !"".equals(old.getCourt())) {
            sql.append(" , COURT = '"+old.getCourt()+"'");
        }
        if(old.getSubjectmatter() != null && !"".equals(old.getSubjectmatter())) {
            sql.append(" , SUBJECTMATTER = '"+old.getSubjectmatter()+"'");
        }
        if(old.getApplicant() != null && !"".equals(old.getApplicant())) {
            sql.append(" , APPLICANT = '"+old.getApplicant()+"'");
        }
        if(old.getApplyamount() != null && !"".equals(old.getApplyamount())) {
            sql.append(" , APPLYAMOUNT = '"+old.getApplyamount()+"'");
        }
        if(old.getFrozenamount() != null && !"".equals(old.getFrozenamount())) {
            sql.append(" , FROZENAMOUNT = '"+old.getFrozenamount()+"'");
        }
        if(old.getKouhuaamount() != null && !"".equals(old.getKouhuaamount())) {
            sql.append(" , KOUHUAAMOUNT = '"+old.getKouhuaamount()+"'");
        }
        if(old.getFrozenmemo() != null && !"".equals(old.getFrozenmemo())) {
            sql.append(" , FROZENMEMO = '"+old.getFrozenmemo()+"'");
        }
        if(old.getActualpreserved() != null && !"".equals(old.getActualpreserved())) {
            sql.append(" , ACTUALPRESERVED = '"+old.getActualpreserved()+"'");
        }

        sql.append(" WHERE QUALID = '"+old.getQualid()+"'");
        return sql.toString();
    }
}
