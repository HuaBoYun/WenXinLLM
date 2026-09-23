package com.huabo.audit.oracle.mapper;

import com.hbfk.util.DateUtil;
import com.huabo.audit.oracle.entity.TblTargetTypeEntity;

import cn.hutool.core.util.StrUtil;

public class TblTargetTypeMapperSqlConfig {
	public String updateEntity(TblTargetTypeEntity plan) {
		StringBuffer sqlSb = new StringBuffer("UPDATE TBL_NBSJ_TARGETTYPE SET TARGETNAME = '"+plan.getTargetName()+"' ");
		if(plan.getStatus() != null && !"".equals(plan.getStatus())) {
			sqlSb.append(" ,STATUS = '"+plan.getStatus()+"'");
		}
		if(plan.getTargetDesc() != null && !"".equals(plan.getTargetDesc())) {
			sqlSb.append(" ,TARGETDESC = '"+plan.getTargetDesc()+"'");
		}
		if(plan.getParentId() != null && !"".equals(plan.getParentId())) {
			sqlSb.append(" ,PARENTID = '"+plan.getParentId()+"'");
		}
		if(plan.getNbsjTemplete().getTempleteId() != null && !"".equals(plan.getNbsjTemplete().getTempleteId())) {
			sqlSb.append(" ,TEMPID = '"+plan.getNbsjTemplete().getTempleteId()+"'");
		}
		
		sqlSb.append(" WHERE TARGETID= "+plan.getTargetId());
		return sqlSb.toString();
	}
	
	public String insertEntity(TblTargetTypeEntity plan){
		StringBuffer colSb = new StringBuffer("INSERT INTO TBL_NBSJ_TARGETTYPE(TARGETID,CREATETIME");
		StringBuffer valSb = new StringBuffer("  VALUES (HIBERNATE_SEQUENCE.nextval,TO_DATE('"+DateUtil.parseDate(plan.getCreateTime(), "yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:mi:ss')");
		if(plan!=null && plan.getTargetId()!=null) {
			valSb = new StringBuffer("  VALUES ("+plan.getTargetId()+",TO_DATE('"+DateUtil.parseDate(plan.getCreateTime(), "yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:mi:ss')");
		}
		
		if(plan.getTargetName() != null && !"".equals(plan.getTargetName())) {
			colSb.append(",TARGETNAME");
			valSb.append(",'"+plan.getTargetName()+"'");
		}
		if(plan.getTargetDesc() != null && !"".equals(plan.getTargetDesc())) {
			colSb.append(",TARGETDESC");
			valSb.append(",'"+plan.getTargetDesc()+"'");
		}
		if(plan.getParentId() != null && !"".equals(plan.getParentId())) {
			colSb.append(",PARENTID");
			valSb.append(",'"+plan.getParentId()+"'");
		}
		if(plan.getNbsjTemplete().getTempleteId() != null && !"".equals(plan.getNbsjTemplete().getTempleteId())) {
			colSb.append(",TEMPID");
			valSb.append(",'"+plan.getNbsjTemplete().getTempleteId()+"'");
		}
		if(plan.getStatus() != null && !"".equals(plan.getStatus())) {
			colSb.append(",STATUS");
			valSb.append(",'"+plan.getStatus()+"'");
		}
		
		String sql = colSb.toString()+")"+valSb.toString()+")";
		return sql;
	}
}
