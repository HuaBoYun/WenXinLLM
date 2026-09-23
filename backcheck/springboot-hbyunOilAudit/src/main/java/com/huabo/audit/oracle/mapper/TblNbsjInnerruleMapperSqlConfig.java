package com.huabo.audit.oracle.mapper;

import com.hbfk.util.DateUtil;
import com.huabo.audit.oracle.entity.TblNbsjInnerrule;
import com.huabo.audit.oracle.vo.TblNbsjInnerRuleVo;
import com.huabo.audit.util.PageInfo;

public class TblNbsjInnerruleMapperSqlConfig {
	
	public String selectInnerruleListView(PageInfo<TblNbsjInnerrule> pageInfo,TblNbsjInnerRuleVo tblNbsjInnerRuleVo) throws Exception {
		StringBuffer sqlSb = new StringBuffer();
		sqlSb.append("select * from TBL_NBSJ_INNERRULE t where 1=1 and COMPANYID = "+tblNbsjInnerRuleVo.getCompanyid());
		if (tblNbsjInnerRuleVo.getRulename()!= null && tblNbsjInnerRuleVo.getRulename().length() > 0) {
			sqlSb.append(" AND rulename LIKE '%"+tblNbsjInnerRuleVo.getRulename()+"%'");
		}
		if (tblNbsjInnerRuleVo.getPublishorg()!= null && tblNbsjInnerRuleVo.getPublishorg().length() > 0) {
			sqlSb.append(" AND PUBLISHORG= '"+tblNbsjInnerRuleVo.getPublishorg()+"'");
		}
		if (tblNbsjInnerRuleVo.getRulecode()!= null && tblNbsjInnerRuleVo.getRulecode().length() > 0) {
			sqlSb.append(" AND RULECODE LIKE '%"+tblNbsjInnerRuleVo.getRulecode()+"%'");
		}
		if (tblNbsjInnerRuleVo.getStatus()!= null && tblNbsjInnerRuleVo.getStatus().length() > 0) {
			sqlSb.append(" AND STATUS LIKE '%"+tblNbsjInnerRuleVo.getStatus()+"%'");
		}
		if (tblNbsjInnerRuleVo.getInnruletype()!= null && tblNbsjInnerRuleVo.getInnruletype().length() > 0) {
			sqlSb.append(" AND INNRULETYPE LIKE '%"+tblNbsjInnerRuleVo.getInnruletype()+"%'");
		}
		if (tblNbsjInnerRuleVo.getStarttime()!=null) {
			sqlSb.append(" AND PUBLISHDATE >= '"+DateUtil.parseDate(tblNbsjInnerRuleVo.getStarttime(), "yyyy-MM-dd HH:mm:ss")+"'");
		}
		if (tblNbsjInnerRuleVo.getEndtime()!=null) {
			sqlSb.append(" AND PUBLISHDATE <= '"+DateUtil.parseDate(tblNbsjInnerRuleVo.getEndtime(), "yyyy-MM-dd HH:mm:ss")+"'");
		}
		sqlSb.append(" ORDER BY INNRULID DESC ");
		System.out.println(sqlSb.toString());
		return sqlSb.toString();
	}
	
	public String selectInnerruleCountView(PageInfo<TblNbsjInnerrule> pageInfo,TblNbsjInnerRuleVo tblNbsjInnerRuleVo) throws Exception {
		StringBuffer sqlSb = new StringBuffer("select count(0) from TBL_NBSJ_INNERRULE   where 1=1 and COMPANYID = "+tblNbsjInnerRuleVo.getCompanyid());
		if (tblNbsjInnerRuleVo.getRulename()!= null && tblNbsjInnerRuleVo.getRulename().length() > 0) {
			sqlSb.append(" AND rulename LIKE '%"+tblNbsjInnerRuleVo.getRulename()+"%'");
		}
		if (tblNbsjInnerRuleVo.getPublishorg()!= null && tblNbsjInnerRuleVo.getPublishorg().length() > 0) {
			sqlSb.append(" AND PUBLISHORG='"+tblNbsjInnerRuleVo.getPublishorg()+"'");
		}
		if (tblNbsjInnerRuleVo.getRulecode()!= null && tblNbsjInnerRuleVo.getRulecode().length() > 0) {
			sqlSb.append(" AND RULECODE LIKE '%"+tblNbsjInnerRuleVo.getRulecode()+"%'");
		}
		if (tblNbsjInnerRuleVo.getStatus()!= null && tblNbsjInnerRuleVo.getStatus().length() > 0) {
			sqlSb.append(" AND STATUS LIKE '%"+tblNbsjInnerRuleVo.getStatus()+"%'");
		}
		if (tblNbsjInnerRuleVo.getInnruletype()!= null && tblNbsjInnerRuleVo.getInnruletype().length() > 0) {
			sqlSb.append(" AND INNRULETYPE LIKE '%"+tblNbsjInnerRuleVo.getInnruletype()+"%'");
		}
		if (tblNbsjInnerRuleVo.getStarttime()!=null) {
			sqlSb.append(" AND PUBLISHDATE >=TO_DATE('"+DateUtil.parseDate(tblNbsjInnerRuleVo.getStarttime(), "yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:mi:ss')");
		}
		if (tblNbsjInnerRuleVo.getEndtime()!=null) {
			sqlSb.append(" AND PUBLISHDATE <= TO_DATE('"+DateUtil.parseDate(tblNbsjInnerRuleVo.getEndtime(), "yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:mi:ss')");
		}
		return sqlSb.toString();
	}
	
	public String insertEntity(TblNbsjInnerrule inner){
		StringBuffer colSb = new StringBuffer("INSERT INTO TBL_NBSJ_INNERRULE(INNRULID");
		StringBuffer valSb = new StringBuffer("  VALUES (HIBERNATE_SEQUENCE.nextval");
		if(inner.getRulecode() != null && !"".equals(inner.getRulecode())) {
			colSb.append(",rulecode");
			valSb.append(",'"+inner.getRulecode()+"'");
		}
		if(inner.getRulename() != null && !"".equals(inner.getRulename())) {
			colSb.append(",rulename");
			valSb.append(",'"+inner.getRulename()+"'");
		}
		if(inner.getRulenumber() != null && !"".equals(inner.getRulenumber())) {
			colSb.append(",rulenumber");
			valSb.append(",'"+inner.getRulenumber()+"'");
		}
		if(inner.getInnruletype() != null && !"".equals(inner.getInnruletype())) {
			colSb.append(",INNRULETYPE");
			valSb.append(",'"+inner.getInnruletype()+"'");
		}
		if(inner.getPublishorg() != null && !"".equals(inner.getPublishorg())) {
			colSb.append(",Publishorg");
			valSb.append(",'"+inner.getPublishorg()+"'");
		}
		if(inner.getCompanyid() != null && !"".equals(inner.getCompanyid())) {
			colSb.append(",COMPANYID");
			valSb.append(",'"+inner.getCompanyid()+"'");
		}
		if(inner.getStatus() != null && !"".equals(inner.getStatus())) {
			colSb.append(",status");
			valSb.append(",'"+inner.getStatus()+"'");
		}
		if(inner.getPublishdate() != null) {
			colSb.append(",publishdate");
			valSb.append(",TO_DATE('"+DateUtil.parseDate(inner.getPublishdate(), "yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:mi:ss')");
		}
		if(inner.getBodyinfo() != null && !"".equals(inner.getBodyinfo())) {
			colSb.append(",bodyinfo");
			valSb.append(",'"+inner.getBodyinfo()+"'");
		}
		if(inner.getOrgname() != null && !"".equals(inner.getOrgname())) {
			colSb.append(",ORGNAME");
			valSb.append(",'"+inner.getOrgname()+"'");
		}
		String sql = colSb.toString()+")"+valSb.toString()+")";
		System.out.println(sql);
		return sql;
	}
	
	
	public String updateEntity(TblNbsjInnerrule inner) {
		StringBuffer sqlSb = new StringBuffer("UPDATE TBL_NBSJ_INNERRULE SET ");
		sqlSb.append(" rulecode = '"+inner.getRulecode()+"'");
		if(inner.getRulename() != null && !"".equals(inner.getRulename())) {
			sqlSb.append(" ,rulename = '"+inner.getRulename()+"'");
		}
		if(inner.getStatus() != null && !"".equals(inner.getStatus())) {
			sqlSb.append(" ,status = '"+inner.getStatus()+"'");
		}
		if(inner.getRulenumber() != null && !"".equals(inner.getRulenumber())) {
			sqlSb.append(" ,rulenumber = '"+inner.getRulenumber()+"'");
		}
		if(inner.getPublishorg() != null && !"".equals(inner.getPublishorg())) {
			sqlSb.append(" ,Publishorg = '"+inner.getPublishorg()+"'");
		}
		if(inner.getOrgname() != null && !"".equals(inner.getOrgname())) {
			sqlSb.append(" ,ORGNAME = '"+inner.getOrgname()+"'");
		}
		if(inner.getInnruletype() != null && !"".equals(inner.getInnruletype())) {
			sqlSb.append(" ,INNRULETYPE = '"+inner.getInnruletype()+"'");
		}
		if(inner.getPublishdate() != null) {
			sqlSb.append(" ,publishdate = TO_DATE('"+DateUtil.parseDate(inner.getPublishdate(), "yyyy-MM-dd")+"','YYYY-MM-DD')");
		}
		if(inner.getBodyinfo() != null && !"".equals(inner.getBodyinfo())) {
			sqlSb.append(" ,bodyinfo = '"+inner.getBodyinfo()+"'");
		}
		sqlSb.append(" WHERE INNRULID = "+inner.getInnrulid());
		System.out.println(sqlSb.toString());
		return sqlSb.toString();
	}
	

}
