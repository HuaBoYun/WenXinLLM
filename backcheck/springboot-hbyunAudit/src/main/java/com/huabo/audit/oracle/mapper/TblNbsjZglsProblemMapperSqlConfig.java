package com.huabo.audit.oracle.mapper;

import com.hbfk.util.DateUtil;
import com.huabo.audit.oracle.entity.TblNbsjZglsProblem;

import cn.hutool.core.util.StrUtil;

public class TblNbsjZglsProblemMapperSqlConfig {
	
	
	public String insertEntity(TblNbsjZglsProblem plan){
		StringBuffer colSb = new StringBuffer("INSERT INTO TBL_NBSJ_ZGLS_PROBLEM(PROBLEMID");
		StringBuffer valSb = new StringBuffer("  VALUES (HIBERNATE_SEQUENCE.nextval");
		
		if(plan.getProblemtype() != null && !"".equals(plan.getProblemtype())) {
			colSb.append(",PROBLEMTYPE");
			valSb.append(",'"+plan.getProblemtype()+"'");
		}
		if(plan.getCreatetiem() != null && !"".equals(plan.getCreatetiem())) {
			colSb.append(",CREATETIEM");
			valSb.append(",TO_DATE('"+DateUtil.parseDate(plan.getCreatetiem(), "yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:mi:ss')");
		}
		if(plan.getZgnum() != null && !"".equals(plan.getZgnum())) {
			colSb.append(",ZGNUM");
			valSb.append(","+plan.getZgnum());
		}
		if(plan.getSystemname() != null && !"".equals(plan.getSystemname())) {
			colSb.append(",SYSTEMNAME");
			valSb.append(",'"+plan.getSystemname()+"'");
		}
		if(plan.getDatatype() != null && !"".equals(plan.getDatatype())) {
			colSb.append(",DATATYPE");
			valSb.append(",'"+plan.getDatatype()+"'");
		}
		if(plan.getManuscript() != null && !"".equals(plan.getManuscript())) {
			colSb.append(",MANUSCRIPT");
			valSb.append(",'"+plan.getManuscript()+"'");
		}
		if(plan.getRectification() != null ) {
			colSb.append(",RECTIFICATION");
			valSb.append(","+plan.getRectification());
		}
		if(plan.getReformid() != null ) {
			colSb.append(",REFORMID");
			valSb.append(","+plan.getReformid());
		}
		if(plan.getCreateorg() != null ) {
			colSb.append(",CREATEORG");
			valSb.append(","+plan.getCreateorg());
		}
		if(plan.getCreatestaff() != null ) {
			colSb.append(",CREATESTAFF");
			valSb.append(","+plan.getCreatestaff());
		}
		if(plan.getZgmonty() != null ) {
			colSb.append(",ZGMONTY");
			valSb.append(","+plan.getZgmonty());
		}
		String sql = colSb.toString()+")"+valSb.toString()+")";
		return sql;
	}
	
	
	
	
	
}
