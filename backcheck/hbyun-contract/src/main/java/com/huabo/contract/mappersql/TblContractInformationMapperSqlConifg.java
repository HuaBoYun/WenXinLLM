package com.huabo.contract.mappersql;

import com.hbfk.util.DateUtil;
import com.hbfk.util.database.DataBaseSqlConfig;
import com.huabo.contract.entity.TblContractInformation;

public class TblContractInformationMapperSqlConifg {

    public String saveContractInfoMation(TblContractInformation information) throws Exception {
        StringBuffer column = new StringBuffer("INSERT INTO TBL_CONTRACT_INFORMATION (INFOID");
        StringBuffer value = new StringBuffer(" VALUES ("+information.getInfoid());

        if(information.getInfono() != null && !"".equals(information.getInfono())) {
            column.append(",INFONO");
            value.append(",'"+information.getInfono()+"'");
        }
        if(information.getInfoname() != null && !"".equals(information.getInfoname())) {
            column.append(",INFONAME");
            value.append(",'"+information.getInfoname()+"'");
        }
        if(information.getInfotype() != null && !"".equals(information.getInfotype())) {
            column.append(",INFOTYPE");
            value.append(",'"+information.getInfotype()+"'");
        }
        if(information.getInfoxh() != null && !"".equals(information.getInfoxh())) {
            column.append(",INFOXH");
            value.append(",'"+information.getInfoxh()+"'");
        }
        if(information.getInfoorg() != null && !"".equals(information.getInfoorg())) {
            column.append(",INFOORG");
            value.append(",'"+information.getInfoorg()+"'");
        }
        if(information.getInfoprice() != null && !"".equals(information.getInfoprice())) {
            column.append(",INFOPRICE");
            value.append(",'"+information.getInfoprice()+"'");
        }
        if(information.getInfonum() != null && !"".equals(information.getInfonum())) {
            column.append(",INFONUM");
            value.append(",'"+information.getInfonum()+"'");
        }
        if(information.getInfodesc() != null && !"".equals(information.getInfodesc())) {
            column.append(",INFODESC");
            value.append(",'"+information.getInfodesc()+"'");
        }
        if(information.getInfomomo() != null && !"".equals(information.getInfomomo())) {
            column.append(",INFOMOMO");
            value.append(",'"+information.getInfomomo()+"'");
        }
        if(information.getInfopinpai() != null && !"".equals(information.getInfopinpai())) {
            column.append(",INFOPINPAI");
            value.append(",'"+information.getInfopinpai()+"'");
        }
        if(information.getInfostartdate() != null) {
            column.append(",INFOSTARTDATE");
            value.append("," + DataBaseSqlConfig.getDateStrFormat(information.getInfostartdate()));
        }
        if(information.getInfoenddate() != null) {
            column.append(",INFOENDDATE");
            value.append("," + DataBaseSqlConfig.getDateStrFormat(information.getInfoenddate()));
        }
        if(information.getProjectid() != null && !"".equals(information.getProjectid())) {
            column.append(",PROJECTID");
            value.append(",'"+information.getProjectid()+"'");
        }

        column.append(")");
        value.append(")");
        String sql = column.toString()+value.toString();
        return sql;
    }

    public String updateContractInfoMation(TblContractInformation information) throws Exception {
        StringBuffer sql = new StringBuffer("UPDATE TBL_CONTRACT_INFORMATION SET INFONAME = '"+information.getInfoname()+"'");

        if(information.getInfono() != null) {
            sql.append(" ,INFONO = '"+information.getInfono()+"'");
        }
        if(information.getInfotype() != null) {
            sql.append(" ,INFOTYPE = '"+information.getInfotype()+"'");
        }
        if(information.getInfoxh() != null) {
            sql.append(" ,INFOXH = '"+information.getInfoxh()+"'");
        }
        if(information.getInfoorg() != null) {
            sql.append(" ,INFOORG = '"+information.getInfoorg()+"'");
        }
        if(information.getInfoprice() != null) {
            sql.append(" ,INFOPRICE = '"+information.getInfoprice()+"'");
        }
        if(information.getInfonum() != null) {
            sql.append(" ,INFONUM = '"+information.getInfonum()+"'");
        }
        if(information.getInfodesc() != null) {
            sql.append(" ,INFODESC = '"+information.getInfodesc()+"'");
        }
        if(information.getInfomomo() != null) {
            sql.append(" ,INFOMOMO = '"+information.getInfomomo()+"'");
        }
        if(information.getInfopinpai() != null) {
            sql.append(" ,INFOPINPAI = '"+information.getInfopinpai()+"'");
        }

        if(information.getInfostartdate() != null) {
            sql.append(" ,INFOSTARTDATE = " + DataBaseSqlConfig.getDateStrFormat(information.getInfostartdate()));
        }
        if(information.getInfoenddate() != null) {
            sql.append(" ,INFOENDDATE = " + DataBaseSqlConfig.getDateStrFormat(information.getInfoenddate()));
        }

        if(information.getProjectid() != null) {
            sql.append(" ,PROJECTID = '"+information.getProjectid()+"'");
        }

        sql.append(" WHERE INFOID = '"+information.getInfoid()+"'");
        return sql.toString();
    }


}
