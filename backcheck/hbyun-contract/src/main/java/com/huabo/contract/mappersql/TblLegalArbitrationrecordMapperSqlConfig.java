package com.huabo.contract.mappersql;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hbfk.util.database.DataBaseSqlConfig;
import com.huabo.contract.entity.TblLegalArbitrationrecord;

public class TblLegalArbitrationrecordMapperSqlConfig {

    public String saveNegotiateRecord(TblLegalArbitrationrecord record) throws Exception {
        StringBuffer column = new StringBuffer("INSERT INTO TBL_LEGAL_ARBITRATIONRECORD (ARRECORDID");
        StringBuffer value = new StringBuffer(" VALUES ("+record.getArrecordid());

        if(record.getArstage() != null && !"".equals(record.getArstage())){
            column.append(",ARSTAGE");
            value.append(",'"+record.getArstage()+"'");
        }
        if(record.getArcontactperson() != null && !"".equals(record.getArcontactperson())){
            column.append(",ARCONTACTPERSON");
            value.append(",'"+record.getArcontactperson()+"'");
        }
        if(record.getOurcontractperson() != null && !"".equals(record.getOurcontractperson())){
            column.append(",OURCONTRACTPERSON");
            value.append(",'"+record.getOurcontractperson()+"'");
        }
        if(record.getCommunicationmode() != null && !"".equals(record.getCommunicationmode())){
            column.append(",COMMUNICATIONMODE");
            value.append(",'"+record.getCommunicationmode()+"'");
        }
        if(record.getArrecordmode() != null && !"".equals(record.getArrecordmode())){
            column.append(",ARRECORDMODE");
            value.append(",'"+record.getArrecordmode()+"'");
        }
        if(record.getArrecordmemo() != null){
            column.append(",ARRECORDMEMO");
            value.append(",'"+record.getArrecordmemo()+"'");
        }
        if(record.getArbitrationinfo() != null){
            column.append(",ARBITRATIONINFO");
            value.append(",'"+record.getArbitrationinfo()+"'");
        }
        if(record.getCreatestaff() != null){
            column.append(",CREATESTAFF");
            value.append(",'"+record.getCreatestaff()+"'");
        }
        if(record.getCreatetime() != null){
            column.append(",CREATETIME");
            value.append(","+ DataBaseSqlConfig.getDateHmsStrFormat(record.getCreatetime()));
            //value.append(" ,CREATETIME = TO_DATE('"+DateUtil.parseDate(record.getCreatetime(),"yyyy-MM-dd HH:mm:ss") +"', 'YYYY-MM-DD HH24:MI:SS')");
            //value.append(",'"+record.getCreatetime()+"'");
        }
        if(record.getLinkorg() != null){
            column.append(",LINKORG");
            value.append(",'"+record.getLinkorg()+"'");
        }
        if(record.getDealdate() != null){
            column.append(",DEALDATE");
            value.append(","+ DataBaseSqlConfig.getDateStrFormat(record.getDealdate()));
            //value.append(",'"+record.getDealdate()+"'");
        }
        if(record.getAttorney() != null && !"".equals(record.getAttorney())){
            column.append(",ATTORNEY");
            value.append(",'"+record.getAttorney()+"'");
        }
        if(record.getCompany() != null && !"".equals(record.getCompany())){
            column.append(",COMPANY");
            value.append(",'"+record.getCompany()+"'");
        }
        if(record.getPhone() != null && !"".equals(record.getPhone())){
            column.append(",PHONE");
            value.append(",'"+record.getPhone()+"'");
        }

        column.append(")");
        value.append(")");
        String sql = column.toString()+value.toString();
        return sql;
    }


    public String modifyNegotiateRecord(TblLegalArbitrationrecord oldRecord) throws Exception {
        StringBuffer sql = new StringBuffer("UPDATE TBL_LEGAL_ARBITRATIONRECORD SET ARCONTACTPERSON = '"+oldRecord.getArcontactperson()+"'");

        if(oldRecord.getArstage() != null && !"".equals(oldRecord.getArstage())) {
            sql.append(" , ARSTAGE = '"+oldRecord.getArstage()+"'");
        }
        if(oldRecord.getOurcontractperson() != null && !"".equals(oldRecord.getOurcontractperson())) {
            sql.append(" , OURCONTRACTPERSON = '"+oldRecord.getOurcontractperson()+"'");
        }
        if(oldRecord.getCommunicationmode() != null && !"".equals(oldRecord.getCommunicationmode())) {
            sql.append(" , COMMUNICATIONMODE = '"+oldRecord.getCommunicationmode()+"'");
        }

        if(oldRecord.getArrecordmode() != null && !"".equals(oldRecord.getArrecordmode())) {
            sql.append(" , ARRECORDMODE = '"+oldRecord.getArrecordmode()+"'");
        }
        if(oldRecord.getArrecordmemo() != null && !"".equals(oldRecord.getArrecordmemo())) {
            sql.append(" , ARRECORDMEMO = '"+oldRecord.getArrecordmemo()+"'");
        }
        if(oldRecord.getArbitrationinfo() != null) {
            sql.append(" , ARBITRATIONINFO = '"+oldRecord.getArbitrationinfo()+"'");
        }
        if(oldRecord.getCreatestaff() != null) {
            sql.append(" , CREATESTAFF = '"+oldRecord.getCreatestaff()+"'");
        }
//        if(oldRecord.getCreatetime() != null) {
//            sql.append(" ,CREATETIME = "+ DataBaseSqlConfig.getDateStrFormat((oldRecord.getCreatetime())));
//            // sql.append(" , CREATETIME = '"+oldRecord.getCreatetime()+"'");
//        }
        if(oldRecord.getLinkorg() != null) {
            sql.append(" , LINKORG = '"+oldRecord.getLinkorg()+"'");
        }
        if(oldRecord.getDealdate() != null) {
            sql.append(" ,DEALDATE = "+ DataBaseSqlConfig.getDateStrFormat((oldRecord.getDealdate())));
            //sql.append(" , DEALDATE = '"+oldRecord.getDealdate()+"'");
        }
        if(oldRecord.getAttorney() != null) {
            sql.append(" , ATTORNEY = '"+oldRecord.getAttorney()+"'");
        }
        if(oldRecord.getCompany() != null && !"".equals(oldRecord.getCompany())) {
            sql.append(" , COMPANY = '"+oldRecord.getCompany()+"'");
        }
        if(oldRecord.getPhone() != null) {
            sql.append(" , PHONE = '"+oldRecord.getPhone()+"'");
        }

        sql.append(" WHERE ARRECORDID = '"+oldRecord.getArrecordid()+"'");
        return sql.toString();
    }


    public String findListByPageInfo(IPage<TblLegalArbitrationrecord> page, BigDecimal arrecordid) throws Exception{
        StringBuffer sqlSb = new StringBuffer("SELECT " +
                "TLA.*,\n" +
                "TS.STAFFID,\n" +
                "TS.REALNAME " +
                " FROM TBL_LEGAL_ARBITRATIONRECORD TLA " +
                "LEFT JOIN TBL_STAFF TS ON TLA.CREATESTAFF = TS.STAFFID " +
                "WHERE TLA.ARBITRATIONINFO = "+ arrecordid);

        sqlSb.append(" ORDER BY TLA.CREATETIME DESC ");
        String sql = sqlSb.toString();
        return sql;
    }
}
