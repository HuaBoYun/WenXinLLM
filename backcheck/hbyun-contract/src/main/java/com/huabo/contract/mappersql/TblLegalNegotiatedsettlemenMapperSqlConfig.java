package com.huabo.contract.mappersql;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hbfk.util.DateUtil;
import com.hbfk.util.database.DataBaseSqlConfig;
import com.huabo.contract.entity.TblLegalNegotiatedsettlemen;

public class TblLegalNegotiatedsettlemenMapperSqlConfig {
	
	public String findListForLitiationList(IPage<TblLegalNegotiatedsettlemen> page,TblLegalNegotiatedsettlemen negotiate) throws Exception{
		 StringBuffer sqlSb = new StringBuffer("SELECT " +
                 "tln.*,\n" +
                 "tld.DISPUTENO,\n" +
                 "tld.DISPUTETYPE,\n" +
                 "tld.DISPUTEITEM,\n" +
                 "tld.CONTRACTINFO,\n" +
                 "tld.DISPUTECOURS,\n" +
                 "tld.WHETHERSUED,\n" +
                 "tld.LITIGATIONPOS,\n" +
                 "tld.DISPUTEID DISPUTEIDONE,\n" +
                 "tcu.CONTRACTNO,\n" +
                 "tcu.CONTRACTNAME" +
                 " FROM TBL_LEGAL_NEGOTIATEDSETTLEMEN tln " +
			        "LEFT JOIN TBL_LEGAL_DISPUTREGISTRATION tld ON tln.DISPUINFO = tld.DISPUTEID " +
			        "LEFT JOIN TBL_CYHW_UNIT tcu ON tld.CONTRACTINFO = tcu.CONTRACTID " +
			        "WHERE tln.LINKORG = "+ negotiate.getLinkorg());
			
			if (negotiate.getJudicialsettlement() != null) {
			    sqlSb.append(" AND JUDICIALSETTLEMENT = " + negotiate.getJudicialsettlement());
			}
			sqlSb.append(" AND tld.DISPUTEID NOT IN (SELECT tld.DISPUTEID FROM TBL_LEGAL_CLOSEINFORMATION tlc " +
			        " LEFT JOIN TBL_LEGAL_DISPUTREGISTRATION tld on tlc.DISPUTINFO = tld.DISPUTEID " +
			        " WHERE tld.LINKORG = "+ negotiate.getLinkorg());
			
			sqlSb.append(" )ORDER BY negotiaId DESC ");
			String sql = sqlSb.toString();
			return sql;
	}
	
	public String findListByPage(IPage<TblLegalNegotiatedsettlemen> page,TblLegalNegotiatedsettlemen negotia, BigDecimal pid, BigDecimal disputeid) throws Exception {
		StringBuffer sqlSb = new StringBuffer("SELECT " +
                "tln.*,\n" +
                "tld.DISPUTENO,\n" +
                "tld.DISPUTETYPE,\n" +
                "tld.DISPUTEITEM,\n" +
                "tld.CONTRACTINFO,\n" +
                "tld.DISPUTECOURS,\n" +
                "tld.WHETHERSUED,\n" +
                "tld.LITIGATIONPOS,\n" +
                "tld.DISPUTEID DISPUTEIDONE,\n" +
                "tcu.CONTRACTNO,\n" +
                "tcu.CONTRACTNAME" +
                " FROM TBL_LEGAL_NEGOTIATEDSETTLEMEN tln " +
                "LEFT JOIN TBL_LEGAL_DISPUTREGISTRATION tld on tln.DISPUINFO = tld.DISPUTEID " +
                "LEFT JOIN TBL_CYHW_UNIT tcu on tld.CONTRACTINFO = tcu.CONTRACTID " +
                "WHERE tln.LINKORG ="+pid);

        if (disputeid != null){
            sqlSb.append("AND tld.DISPUTEID = "+disputeid);
        }
        if (negotia.getDisputeitem() != null && !"".equals(negotia.getDisputeitem())) {
            sqlSb.append(" AND tld.DISPUTEITEM LIKE '%" + negotia.getDisputeitem() + "%'");
        }
        if (negotia.getDisputetype() != null && !"".equals(negotia.getDisputetype())) {
            sqlSb.append(" AND tld.DISPUTETYPE LIKE '%" + negotia.getDisputetype() + "%'");
        }
        if (negotia.getContractname() != null && !"".equals(negotia.getContractname())) {
            sqlSb.append(" AND tcu.CONTRACTNAME LIKE '%" + negotia.getContractname() + "%'");
        }
        if (negotia.getCounterpart() != null) {
            sqlSb.append(" AND COUNTERPART like '%" + negotia.getCounterpart() + "%' ");
        }

        sqlSb.append(" ORDER BY negotiaId DESC");
        String sql = sqlSb.toString();
        return sql;
	}
	
	public String saveDiputregistration(TblLegalNegotiatedsettlemen negotiated) throws Exception {
        StringBuffer column = new StringBuffer("INSERT INTO TBL_LEGAL_NEGOTIATEDSETTLEMEN (NEGOTIAID");
        StringBuffer value = new StringBuffer(" VALUES ("+negotiated.getNegotiaid());

        if(negotiated.getNegotiastatus() != null && !"".equals(negotiated.getNegotiastatus())) {
            column.append(",NEGOTIASTATUS");
            value.append(",'"+negotiated.getNegotiastatus()+"'");
        }
        if(negotiated.getCounterpart() != null && !"".equals(negotiated.getCounterpart())) {
            column.append(",COUNTERPART");
            value.append(",'"+negotiated.getCounterpart()+"'");
        }
        if(negotiated.getCounterpartphone() != null && !"".equals(negotiated.getCounterpartphone())) {
            column.append(",COUNTERPARTPHONE");
            value.append(",'"+negotiated.getCounterpartphone()+"'");
        }
        if(negotiated.getIsaggree() != null && !"".equals(negotiated.getIsaggree())) {
            column.append(",ISAGGREE");
            value.append(",'"+negotiated.getIsaggree()+"'");
        }
        if(negotiated.getNegetiaresult() != null && !"".equals(negotiated.getNegetiaresult())) {
            column.append(",NEGETIARESULT");
            value.append(",'"+negotiated.getNegetiaresult()+"'");
        }
        if(negotiated.getJudicialsettlement() != null && !"".equals(negotiated.getJudicialsettlement())) {
            column.append(",JUDICIALSETTLEMENT");
            value.append(",'"+negotiated.getJudicialsettlement()+"'");
        }
        if(negotiated.getLinkorg() != null && !"".equals(negotiated.getLinkorg())) {
            column.append(",LINKORG");
            value.append(",'"+negotiated.getLinkorg()+"'");
        }
        if(negotiated.getDispuinfo() != null && !"".equals(negotiated.getDispuinfo())) {
            column.append(",DISPUINFO");
            value.append(",'"+negotiated.getDispuinfo()+"'");
        }
        if(negotiated.getCreatestaff() != null && !"".equals(negotiated.getCreatestaff())) {
            column.append(",CREATESTAFF");
            value.append(",'"+negotiated.getCreatestaff()+"'");
        }
        if(negotiated.getCreatetime() != null && !"".equals(negotiated.getCreatetime())) {
            column.append(",CREATETIME");
            value.append(","+DataBaseSqlConfig.getDateStrFormat(negotiated.getCreatetime()));
            //value.append(",'"+negotiated.getCreatetime()+"'");
        }
        if(negotiated.getSolutionmode() != null && !"".equals(negotiated.getSolutionmode())) {
            column.append(",SOLUTIONMODE");
            value.append(",'"+negotiated.getSolutionmode()+"'");
        }
        if(negotiated.getCourtname() != null && !"".equals(negotiated.getCourtname())) {
            column.append(",COURTNAME");
            value.append(",'"+negotiated.getCourtname()+"'");
        }
        if(negotiated.getIspresetcase() != null && !"".equals(negotiated.getIspresetcase())) {
            column.append(",ISPRESETCASE");
            value.append(",'"+negotiated.getIspresetcase()+"'");
        }
		if(negotiated.getSchemeStatus() != null && !"".equals(negotiated.getSchemeStatus())) {
			column.append(",SCHEMESTATUS");
			value.append(",'"+negotiated.getSchemeStatus()+"'");
		}
		if(negotiated.getMediationScheme() != null && !"".equals(negotiated.getMediationScheme())) {
			column.append(",MEDIATIONSCHEME");
			value.append(",'"+negotiated.getMediationScheme()+"'");
		}

        column.append(")");
        value.append(")");
        String sql = column.toString()+value.toString();
        return sql;
    }
	
	public String updateOldNegotiated(TblLegalNegotiatedsettlemen negotiated) throws Exception{
		StringBuffer sql = new StringBuffer("UPDATE TBL_LEGAL_NEGOTIATEDSETTLEMEN SET COUNTERPART = '"+negotiated.getCounterpart()+"' ");

        if(negotiated.getNegotiastatus() != null && !"".equals(negotiated.getNegotiastatus())) {
            sql.append(" , NEGOTIASTATUS = '"+negotiated.getNegotiastatus()+"'");
        }
        if(negotiated.getCounterpartphone() != null && !"".equals(negotiated.getCounterpartphone())) {
            sql.append(" , COUNTERPARTPHONE = '"+negotiated.getCounterpartphone()+"'");
        }
        if(negotiated.getIsaggree() != null && !"".equals(negotiated.getIsaggree())) {
            sql.append(" , ISAGGREE = '"+negotiated.getIsaggree()+"'");
        }
        if(negotiated.getNegetiaresult() != null && !"".equals(negotiated.getNegetiaresult())) {
            sql.append(" , NEGETIARESULT = '"+negotiated.getNegetiaresult()+"'");
        }
        if(negotiated.getJudicialsettlement() != null && !"".equals(negotiated.getJudicialsettlement())) {
            sql.append(" , JUDICIALSETTLEMENT = '"+negotiated.getJudicialsettlement()+"'");
        }
        if(negotiated.getLinkorg() != null && !"".equals(negotiated.getLinkorg())) {
            sql.append(" , LINKORG = '"+negotiated.getLinkorg()+"'");
        }
        if(negotiated.getDispuinfo() != null && !"".equals(negotiated.getDispuinfo())) {
            sql.append(" , DISPUINFO = '"+negotiated.getDispuinfo()+"'");
        }
        if(negotiated.getCreatestaff() != null && !"".equals(negotiated.getCreatestaff())) {
            sql.append(" , CREATESTAFF = '"+negotiated.getCreatestaff()+"'");
        }
        if(negotiated.getCreatetime() != null && !"".equals(negotiated.getCreatetime())) {
            sql.append(" ,CREATETIME = "+ DataBaseSqlConfig.getDateStrFormat(negotiated.getCreatetime()));
            //sql.append(" , CREATETIME = '"+negotiated.getCreatetime()+"'");
        }
        if(negotiated.getSolutionmode() != null && !"".equals(negotiated.getSolutionmode())) {
            sql.append(" , SOLUTIONMODE = '"+negotiated.getSolutionmode()+"'");
        }
        if(negotiated.getCourtname() != null && !"".equals(negotiated.getCourtname())) {
            sql.append(" , COURTNAME = '"+negotiated.getCourtname()+"'");
        }
        
        if(negotiated.getIspresetcase() != null && !"".equals(negotiated.getIspresetcase())) {
            sql.append(" , ISPRESETCASE = '"+negotiated.getIspresetcase()+"'");
        }

        sql.append(" WHERE NEGOTIAID = '"+negotiated.getNegotiaid()+"'");
        return sql.toString();
	}
}
