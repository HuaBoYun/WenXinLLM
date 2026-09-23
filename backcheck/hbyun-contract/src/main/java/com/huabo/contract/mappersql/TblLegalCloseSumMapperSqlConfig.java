package com.huabo.contract.mappersql;

import com.hbfk.util.DateUtil;
import com.hbfk.util.database.DataBaseSqlConfig;
import com.huabo.contract.entity.TblLegalCloseSum;

public class TblLegalCloseSumMapperSqlConfig {
	 public String addEntity(TblLegalCloseSum tla) throws Exception {
	        StringBuffer column = new StringBuffer("INSERT INTO TBL_LEGAL_CLOSESUM (ID");
	        StringBuffer value = new StringBuffer(" VALUES ("+tla.getId());

	        if(tla.getCasememo() != null && !"".equals(tla.getCasememo())) {
	            column.append(",CASEMEMO");
	            value.append(",'"+tla.getCasememo()+"'");
	        }
	        if(tla.getSentenres() != null && !"".equals(tla.getSentenres())) {
	            column.append(",SENTENRES");
	            value.append(",'"+tla.getSentenres()+"'");
	        }
	        if(tla.getExpersum() != null && !"".equals(tla.getExpersum())) {
	            column.append(",EXPERSUM");
	            value.append(",'"+tla.getExpersum()+"'");
	        }
	        if(tla.getCreatetime() != null) {
	            column.append(",CREATETIME");
	            value.append(","+ DataBaseSqlConfig.getDateStrFormat(tla.getCreatetime()));
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


	    public String updateEntity(TblLegalCloseSum tla) throws Exception {
	        StringBuffer sql = new StringBuffer("UPDATE TBL_LEGAL_CLOSESUM SET CASEMEMO = '"+tla.getCasememo()+"'");

	        if(tla.getSentenres() != null && !"".equals(tla.getSentenres())) {
	            sql.append(" , SENTENRES = '"+tla.getSentenres()+"'");
	        }
	        if(tla.getExpersum() != null && !"".equals(tla.getExpersum())) {
	            sql.append(" , EXPERSUM = '"+tla.getExpersum()+"'");
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
	            sql.append(" ,CREATETIME = "+ DataBaseSqlConfig.getDateStrFormat(tla.getCreatetime()));
	        }
	        
	        sql.append(" WHERE ID = '"+tla.getId()+"'");
	        return sql.toString();
	    }
}
