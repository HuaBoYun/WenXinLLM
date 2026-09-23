package com.huabo.contract.mappersql;

import com.hbfk.util.DateUtil;
import com.hbfk.util.database.DataBaseSqlConfig;
import com.huabo.contract.entity.TbllegalAttorney;

public class TbllegalAttorneyMapperSqlConfig {
	
	public String addEntity(TbllegalAttorney tla) throws Exception {
        StringBuffer column = new StringBuffer("INSERT INTO TBL_LEGAL_ATTORNEY (ID");
        StringBuffer value = new StringBuffer(" VALUES ("+tla.getId());

        if(tla.getIsattorney() != null) {
            column.append(",ISATTORNEY");
            value.append(",'"+tla.getIsattorney()+"'");
        }
        if(tla.getAttorneystaffid() != null) {
            column.append(",ATTORNEYSTAFFID");
            value.append(",'"+tla.getAttorneystaffid()+"'");
        }
        if(tla.getAttorney() != null && !"".equals(tla.getAttorney())) {
            column.append(",ATTORNEY");
            value.append(",'"+tla.getAttorney()+"'");
        }
        if(tla.getCreatetime() != null) {
            column.append(",CREATETIME");
            value.append(","+ DataBaseSqlConfig.getDateStrFormat(tla.getCreatetime()));
        }
        if(tla.getAttorneyphont() != null && !"".equals(tla.getAttorneyphont())) {
            column.append(",ATTORNEYPHONT");
            value.append(",'"+tla.getAttorneyphont()+"'");
        }
        if(tla.getDisputeid() != null) {
            column.append(",DISPUTEID");
            value.append(",'"+tla.getDisputeid()+"'");
        }
		if(tla.getLawsuitid() != null) {
			column.append(",LAWSUITID");
			value.append(",'"+tla.getLawsuitid()+"'");
		}
		if(tla.getArbitrationid() != null) {
			column.append(",ARBITRATIONID");
			value.append(",'"+tla.getArbitrationid()+"'");
		}
		if(tla.getNegotiationid() != null) {
			column.append(",NEGOTIATIONID");
			value.append(",'"+tla.getNegotiationid()+"'");
		}
		if(tla.getLawFirm() != null && !"".equals(tla.getLawFirm())) {
			column.append(",LAWFIRM");
			value.append(",'"+tla.getLawFirm()+"'");
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


    public String updateEntity(TbllegalAttorney tla) throws Exception {
        StringBuffer sql = new StringBuffer("UPDATE TBL_LEGAL_ATTORNEY SET ISATTORNEY = '"+tla.getIsattorney()+"'");

        if(tla.getAttorneystaffid() != null) {
            sql.append(" , ATTORNEYSTAFFID = '"+tla.getAttorneystaffid()+"'");
        }
        if(tla.getAttorney() != null && !"".equals(tla.getAttorney())) {
            sql.append(" , ATTORNEY = '"+tla.getAttorney()+"'");
        }
        
        if(tla.getAttorneyphont() != null && !"".equals(tla.getAttorneyphont())) {
            sql.append(" , ATTORNEYPHONT = '"+tla.getAttorneyphont()+"'");
        }
        if(tla.getDisputeid() != null) {
            sql.append(" , DISPUTEID = '"+tla.getDisputeid()+"'");
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
