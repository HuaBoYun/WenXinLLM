package com.huabo.audit.oracle.mapper;

import com.huabo.audit.oracle.entity.TblNbsjTargettypeEntity;

import cn.hutool.core.util.StrUtil;

public class TblNbsjTargettypeMapperSqlConfig {
	public String insertEntity(TblNbsjTargettypeEntity target){
		StringBuffer colSb = new StringBuffer("INSERT INTO TBL_NBSJ_TARGETTYPE(TARGETID");
		StringBuffer valSb = new StringBuffer("  VALUES (HIBERNATE_SEQUENCE.nextval");
		if(StrUtil.isNotBlank(target.getTargetName())) {
			colSb.append(",TARGETNAME");
			valSb.append(",'"+target.getTargetName()+"'");
		}
		if(StrUtil.isNotBlank(target.getTargetDesc())) {
			colSb.append(",TARGETDESC");
			valSb.append(",'"+target.getTargetDesc()+"'");
		}
		if(target.getParentId()!=null) {
			colSb.append(",PARENTID");
			valSb.append(","+target.getParentId()+"");
		}
		if(target.getCreateTime()!=null) {
			colSb.append(",CREATETIME");
			valSb.append(",sysdate");
		}
		if(target.getUpdateTime()!=null) {
			colSb.append(",UPDATETIME");
			valSb.append(",sysdate");
		}
		if(target.getTempId()!=null) {
			colSb.append(",TEMPID");
			valSb.append(","+target.getTempId()+"");
		}
		if(target.getStatus()!=null) {
			colSb.append(",STATUS");
			valSb.append(",'"+target.getStatus()+"'");
		}
		String sql = colSb.toString()+")"+valSb.toString()+")";
		System.out.println(sql);
		return sql;
	}
	public String updateEntity(TblNbsjTargettypeEntity target){
		StringBuffer colSb = new StringBuffer("UPDATE TBL_NBSJ_TARGETTYPE set");
		if(StrUtil.isNotBlank(target.getTargetName())) {
			colSb.append(" TARGETNAME='"+target.getTargetName()+"',");
		}
		if(StrUtil.isNotBlank(target.getTargetDesc())) {
			colSb.append(" TARGETDESC='"+target.getTargetDesc()+"',");
		}
		if(target.getStatus()!=null) {
			colSb.append(" STATUS='"+target.getStatus()+"',");

		}
		colSb.append(" UPDATETIME=sysdate WHERE TARGETID="+target.getTargetId());
		String sql = colSb.toString();
		System.out.println(sql);
		return sql;
	}
}
