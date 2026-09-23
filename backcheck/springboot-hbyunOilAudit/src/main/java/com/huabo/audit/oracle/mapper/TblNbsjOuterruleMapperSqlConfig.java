package com.huabo.audit.oracle.mapper;

import com.hbfk.util.DateUtil;
import com.hbfk.util.PageInfo;
import com.huabo.audit.oracle.entity.TblNbsjOuterruleEntity;

public class TblNbsjOuterruleMapperSqlConfig {
	
	public String selectOuterruleListView(PageInfo<TblNbsjOuterruleEntity> pageInfo) throws Exception {
		TblNbsjOuterruleEntity outer = pageInfo.getCondition();
		StringBuffer sqlSb = new StringBuffer("SELECT * FROM (SELECT T1.*,ROWNUM RN  FROM (");
		sqlSb.append("select * from TBL_NBSJ_OUTERRULE t where 1=1 and createorgid = "+outer.getTblOrganization().getOrgid());
		if (outer.getBodyinfo()!= null && outer.getBodyinfo().length() > 0) {
			sqlSb.append(" AND bodyinfo LIKE '%"+outer.getBodyinfo()+"%'");
		}
		if (outer.getRulename()!= null && outer.getRulename().length() > 0) {
			sqlSb.append(" AND rulename LIKE '%"+outer.getRulename()+"%'");
		}
		if (outer.getRulenumber()!= null && outer.getRulenumber().length() > 0) {
			sqlSb.append(" AND Rulenumber LIKE '%"+outer.getRulenumber()+"%'");
		}
		
		if(outer.getEffectivelevel()!=null){
			sqlSb.append(" AND effectivelevel LIKE '%"+outer.getEffectivelevel()+"%'");
		}
		if(outer.getTimeliness()!=null){
			sqlSb.append(" AND timeliness LIKE '%"+outer.getTimeliness()+"%'");
		}
		sqlSb.append(" ORDER BY outrulid DESC ) T1 WHERE ROWNUM <= "+(pageInfo.getCurrentRecord()+pageInfo.getPageSize())+" ) T2 WHERE RN > "+pageInfo.getCurrentRecord());
		return sqlSb.toString();
	}
	
	public String selectOuterruleCountView(PageInfo<TblNbsjOuterruleEntity> pageInfo) throws Exception {
		TblNbsjOuterruleEntity outer = pageInfo.getCondition();
		StringBuffer sqlSb = new StringBuffer("select count(0) from TBL_NBSJ_OUTERRULE   where 1=1 and createorgid = "+outer.getTblOrganization().getOrgid());
		if (outer.getBodyinfo()!= null && outer.getBodyinfo().length() > 0) {
			sqlSb.append(" AND bodyinfo LIKE '%"+outer.getBodyinfo()+"%'");
		}
		if (outer.getRulename()!= null && outer.getRulename().length() > 0) {
			sqlSb.append(" AND rulename LIKE '%"+outer.getRulename()+"%'");
		}
		if (outer.getRulenumber()!= null && outer.getRulenumber().length() > 0) {
			sqlSb.append(" AND Rulenumber LIKE '%"+outer.getRulenumber()+"%'");
		}
		
		if(outer.getEffectivelevel()!=null){
			sqlSb.append(" AND effectivelevel LIKE '%"+outer.getEffectivelevel()+"%'");
		}
		if(outer.getTimeliness()!=null){
			sqlSb.append(" AND timeliness LIKE '%"+outer.getTimeliness()+"%'");
		}
		sqlSb.append("ORDER BY outrulid DESC");
		return sqlSb.toString();
	}
	
	public String insertEntity(TblNbsjOuterruleEntity outer){
		StringBuffer colSb = new StringBuffer("INSERT INTO TBL_NBSJ_OUTERRULE(outrulid");
		StringBuffer valSb = new StringBuffer("  VALUES (HIBERNATE_SEQUENCE.nextval");
		if(outer.getRulecode() != null && !"".equals(outer.getRulecode())) {
			colSb.append(",rulecode");
			valSb.append(",'"+outer.getRulecode()+"'");
		}
		
		if(outer.getRulename() != null && !"".equals(outer.getRulename())) {
			colSb.append(",rulename");
			valSb.append(",'"+outer.getRulename()+"'");
		}
		
		if(outer.getRulenumber() != null && !"".equals(outer.getRulenumber())) {
			colSb.append(",rulenumber");
			valSb.append(",'"+outer.getRulenumber()+"'");
		}
		if(outer.getTblOrganization() != null ) {
			colSb.append(",CREATEORGID");
			valSb.append(","+outer.getTblOrganization().getOrgid());
		}
		if(outer.getPublishorg() != null && !"".equals(outer.getPublishorg())) {
			colSb.append(",Publishorg");
			valSb.append(",'"+outer.getPublishorg()+"'");
		}
		if(outer.getEffectivelevel() != null) {
			colSb.append(",effectivelevel");
			valSb.append(",'"+outer.getEffectivelevel()+"'");
		}
		if(outer.getTimeliness() != null) {
			colSb.append(",timeliness");
			valSb.append(",'"+outer.getTimeliness()+"'");
		}
		
		if(outer.getPublishdate() != null) {
			colSb.append(",publishdate");
			valSb.append(",TO_DATE('"+DateUtil.parseDate(outer.getPublishdate(), "yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:mi:ss')");
		}
		
		if(outer.getTakeeffecttime() != null) {
			colSb.append(",takeeffecttime");
			valSb.append(",TO_DATE('"+DateUtil.parseDate(outer.getTakeeffecttime(), "yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:mi:ss')");
		}
		
		if(outer.getEnteringperson() != null) {
			colSb.append(",enteringperson");
			valSb.append(",'"+outer.getEnteringperson()+"'");
		}
		
		if(outer.getEnteringtime() != null) {
			colSb.append(",enteringtime");
			valSb.append(",TO_DATE('"+DateUtil.parseDate(outer.getEnteringtime(), "yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:mi:ss')");
		}
		
		if(outer.getSummaryinfo() != null && !"".equals(outer.getSummaryinfo())) {
			colSb.append(",summaryinfo");
			valSb.append(",'"+outer.getSummaryinfo()+"'");
		}
		if(outer.getBodyinfo() != null && !"".equals(outer.getBodyinfo())) {
			colSb.append(",bodyinfo");
			valSb.append(",'"+outer.getBodyinfo()+"'");
		}
		String sql = colSb.toString()+")"+valSb.toString()+")";
		System.out.println(sql);
		return sql;
	}
	
	
	public String updateEntity(TblNbsjOuterruleEntity outer) {
		StringBuffer sqlSb = new StringBuffer("UPDATE TBL_NBSJ_OUTERRULE SET ");
		if(outer.getRulecode() != null && !"".equals(outer.getRulecode())) {
			sqlSb.append(" rulecode = '"+outer.getRulecode()+"'");
		}
		
		if(outer.getRulename() != null && !"".equals(outer.getRulename())) {
			sqlSb.append(" ,rulename = '"+outer.getRulename()+"'");
		}
		
		if(outer.getRulenumber() != null && !"".equals(outer.getRulenumber())) {
			sqlSb.append(" ,rulenumber = '"+outer.getRulenumber()+"'");
		}
		if(outer.getPublishorg() != null && !"".equals(outer.getPublishorg())) {
			sqlSb.append(" ,Publishorg = '"+outer.getPublishorg()+"'");
		}
		if(outer.getEffectivelevel() != null) {
			sqlSb.append(" ,effectivelevel = '"+outer.getEffectivelevel()+"'");
		}
		if(outer.getTimeliness() != null) {
			sqlSb.append(" ,timeliness = '"+outer.getTimeliness()+"'");
		}
		
		if(outer.getPublishdate() != null) {
			sqlSb.append(" ,publishdate = TO_DATE('"+DateUtil.parseDate(outer.getPublishdate(), "yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:mi:ss')");
		}
		
		if(outer.getTakeeffecttime() != null) {
			sqlSb.append(" ,takeeffecttime = TO_DATE('"+DateUtil.parseDate(outer.getTakeeffecttime(), "yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:mi:ss')");
		}
		
		if(outer.getEnteringperson() != null) {
			sqlSb.append(" ,enteringperson = '"+outer.getEnteringperson()+"'");
		}
		if(outer.getEnteringtime() != null) {
			sqlSb.append(" ,enteringtime = TO_DATE('"+DateUtil.parseDate(outer.getEnteringtime(), "yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:mi:ss')");
		}
		
		if(outer.getSummaryinfo() != null && !"".equals(outer.getSummaryinfo())) {
			sqlSb.append(" ,summaryinfo = '"+outer.getSummaryinfo()+"'");
		}
		if(outer.getBodyinfo() != null && !"".equals(outer.getBodyinfo())) {
			sqlSb.append(" ,bodyinfo = '"+outer.getBodyinfo()+"'");
		}
		sqlSb.append(" WHERE outrulid = "+outer.getOutrulid());
		System.out.println(sqlSb.toString());
		return sqlSb.toString();
	}
	

}
