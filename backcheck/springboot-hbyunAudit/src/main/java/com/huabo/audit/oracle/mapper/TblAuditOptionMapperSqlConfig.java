package com.huabo.audit.oracle.mapper;

import com.hbfk.util.DateUtil;
import com.huabo.audit.oracle.entity.TblAuditOption;

public class TblAuditOptionMapperSqlConfig {
	
	public String selectListByCyId(String id, Integer cyId) throws Exception{
		String sql = "SELECT ao.OPT_ID,s.REALNAME,ao.OPT_DESC,OPT_STATE,ao.OPT_STAFFID,ao.CREATE_DATE from TBL_AUDIT_OPTION ao LEFT JOIN TBL_STAFF s on ao.opt_staffid = s.staffid where 1=1 ";
		if(null!=id&&!"".equals(id)) {
			sql+=" AND ao.RELATION_ID = '"+id+"'";
		}
		if(null!=cyId) {
			sql+=" and CYID = "+cyId+"";
		}
		sql+=" order by ao.opt_id";
		
		return sql;
		
	}
	
	public String saveEnity(TblAuditOption opt) {
		StringBuffer colSb = new StringBuffer("INSERT INTO TBL_AUDIT_OPTION(OPT_ID");
		StringBuffer valSb = new StringBuffer(" VALUES(HIBERNATE_SEQUENCE.nextval");
		
		if(opt.getOptDesc() != null && !"".equals(opt.getOptDesc())) {
			colSb.append(",OPT_DESC");
			valSb.append(",'"+opt.getOptDesc()+"'");
		}
		
		if(opt.getOptStaffid() != null) {
			colSb.append(",OPT_STAFFID");
			valSb.append(",'"+opt.getOptStaffid()+"'");
		}
		
		if(opt.getRelationId() != null) {
			colSb.append(",RELATION_ID");
			valSb.append(",'"+opt.getRelationId()+"'");
		}
		
		if(opt.getOptState() != null && !"".equals(opt.getOptState())) {
			colSb.append(",OPT_STATE");
			valSb.append(",'"+opt.getOptState()+"'");
		}
		
		if(opt.getCyid() != null) {
			colSb.append(",CYID");
			valSb.append(",'"+opt.getCyid()+"'");
		}
		
		if(opt.getCreateDate() != null){
			colSb.append(",CREATE_DATE");
			valSb.append(",TO_DATE('"+DateUtil.parseDate(opt.getCreateDate(), "yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:MI:SS')");
		}
		
		String sql = colSb.toString()+")"+valSb.toString()+")";
		return sql;
	}
	
	
	
	public String getOptionByRelationId(String id) throws Exception{
		String sql = "select s.REALNAME,AO.OPT_DESC,OPT_STATE,ao.OPT_STAFFID,AO.CREATE_DATE "
				+ "from TBL_AUDIT_OPTION ao "
				+ "LEFT JOIN TBL_STAFF s on ao.opt_staffid = s.staffid "
				+ "where ao.RELATION_ID = '"+id+"' "
				+ "order by ao.opt_id";
		
		return sql;
		
	}
	
}
