package com.huabo.audit.oracle.mapper;

import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.DateUtil;
import com.hbfk.util.database.GeneralSQLConcatConfig;
import com.huabo.audit.oracle.entity.TblNbsjInnerrule;
import com.huabo.audit.oracle.vo.TblNbsjInnerRuleVo;
import com.huabo.audit.util.PageInfo;

public class TblNbsjInnerruleMapperSqlConfig {
	
	public String selectInnerruleListView(PageInfo<TblNbsjInnerrule> pageInfo, TblNbsjInnerRuleVo tblNbsjInnerRuleVo, TblStaffUtil loginStaff)throws Exception {
		StringBuffer sb = new StringBuffer("SELECT * FROM "
				+ "(SELECT T1.*,ROWNUM RN  FROM "
				+ "(select * from TBL_NBSJ_INNERRULE t "
				+ "where 1=1");
		
		//安全保密SQL
		sb.append(GeneralSQLConcatConfig.concatSecrectSql(loginStaff.getCurrentOrg().getUseSecrect(), false, "COMPANYID", "COMPANYID", "CREATESTAFFID",
				"SECRECTLEVELID", "STAFFSCOPEIDS", loginStaff.getStaffid(), loginStaff.getDeptIds(), loginStaff.getSecrectScopeIds()));
		
		if (tblNbsjInnerRuleVo.getRulename()!= null && tblNbsjInnerRuleVo.getRulename().length() > 0) {
			sb.append(" AND rulename LIKE '%"+tblNbsjInnerRuleVo.getRulename()+"%'");
		}
		if (tblNbsjInnerRuleVo.getPublishorg()!= null && tblNbsjInnerRuleVo.getPublishorg().length() > 0) {
			sb.append(" AND PUBLISHORG= '"+tblNbsjInnerRuleVo.getPublishorg()+"'");
		}
		if (tblNbsjInnerRuleVo.getRulecode()!= null && tblNbsjInnerRuleVo.getRulecode().length() > 0) {
			sb.append(" AND RULECODE LIKE '%"+tblNbsjInnerRuleVo.getRulecode()+"%'");
		}
		if (tblNbsjInnerRuleVo.getStatus()!= null && tblNbsjInnerRuleVo.getStatus().length() > 0) {
			sb.append(" AND STATUS LIKE '%"+tblNbsjInnerRuleVo.getStatus()+"%'");
		}
		if (tblNbsjInnerRuleVo.getInnruletype()!= null && tblNbsjInnerRuleVo.getInnruletype().length() > 0) {
			sb.append(" AND INNRULETYPE LIKE '%"+tblNbsjInnerRuleVo.getInnruletype()+"%'");
		}
		if (tblNbsjInnerRuleVo.getZdtype()!= null && tblNbsjInnerRuleVo.getZdtype().length() > 0) {
			sb.append(" AND zdtype LIKE '%"+tblNbsjInnerRuleVo.getZdtype()+"%'");
		}
		if (tblNbsjInnerRuleVo.getStarttime()!=null) {
			sb.append(" AND PUBLISHDATE >=TO_DATE('"+DateUtil.parseDate(tblNbsjInnerRuleVo.getStarttime(), "yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:mi:ss')");
		}
		if (tblNbsjInnerRuleVo.getEndtime()!=null) {
			sb.append(" AND PUBLISHDATE <= TO_DATE('"+DateUtil.parseDate(tblNbsjInnerRuleVo.getEndtime(), "yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:mi:ss')");
		}
		//add by ping_gu start
		if (tblNbsjInnerRuleVo.getContent()!= null && tblNbsjInnerRuleVo.getContent().length() > 0) {
			sb.append(" AND (BODYINFO LIKE '%"+tblNbsjInnerRuleVo.getContent()+"%' OR rulename LIKE '%"+tblNbsjInnerRuleVo.getContent()+"%') ");
		}
		
		sb.append(" ORDER BY INNRULID DESC) T1 WHERE ROWNUM <= "+(pageInfo.getCurrentRecord()+pageInfo.getPageSize())+" ) T2 WHERE T2.RN > "+pageInfo.getCurrentRecord());
		return sb.toString();
	}
	
	public String selectInnerruleCountView(PageInfo<TblNbsjInnerrule> pageInfo, TblNbsjInnerRuleVo tblNbsjInnerRuleVo, TblStaffUtil loginStaff) throws Exception {
		StringBuffer sb = new StringBuffer("select count(*) from TBL_NBSJ_INNERRULE t "
				+ "where 1=1");
		
		//安全保密SQL
		sb.append(GeneralSQLConcatConfig.concatSecrectSql(loginStaff.getCurrentOrg().getUseSecrect(), false, "COMPANYID", "COMPANYID", "CREATESTAFFID",
				"SECRECTLEVELID", "STAFFSCOPEIDS", loginStaff.getStaffid(), loginStaff.getDeptIds(), loginStaff.getSecrectScopeIds()));
		
		if (tblNbsjInnerRuleVo.getRulename()!= null && tblNbsjInnerRuleVo.getRulename().length() > 0) {
			sb.append(" AND rulename LIKE '%"+tblNbsjInnerRuleVo.getRulename()+"%'");
		}
		if (tblNbsjInnerRuleVo.getPublishorg()!= null && tblNbsjInnerRuleVo.getPublishorg().length() > 0) {
			sb.append(" AND PUBLISHORG= '"+tblNbsjInnerRuleVo.getPublishorg()+"'");
		}
		if (tblNbsjInnerRuleVo.getRulecode()!= null && tblNbsjInnerRuleVo.getRulecode().length() > 0) {
			sb.append(" AND RULECODE LIKE '%"+tblNbsjInnerRuleVo.getRulecode()+"%'");
		}
		if (tblNbsjInnerRuleVo.getStatus()!= null && tblNbsjInnerRuleVo.getStatus().length() > 0) {
			sb.append(" AND STATUS LIKE '%"+tblNbsjInnerRuleVo.getStatus()+"%'");
		}
		if (tblNbsjInnerRuleVo.getInnruletype()!= null && tblNbsjInnerRuleVo.getInnruletype().length() > 0) {
			sb.append(" AND INNRULETYPE LIKE '%"+tblNbsjInnerRuleVo.getInnruletype()+"%'");
		}
		if (tblNbsjInnerRuleVo.getZdtype()!= null && tblNbsjInnerRuleVo.getZdtype().length() > 0) {
			sb.append(" AND zdtype LIKE '%"+tblNbsjInnerRuleVo.getZdtype()+"%'");
		}
		if (tblNbsjInnerRuleVo.getStarttime()!=null) {
			sb.append(" AND PUBLISHDATE >=TO_DATE('"+DateUtil.parseDate(tblNbsjInnerRuleVo.getStarttime(), "yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:mi:ss')");
		}
		if (tblNbsjInnerRuleVo.getEndtime()!=null) {
			sb.append(" AND PUBLISHDATE <= TO_DATE('"+DateUtil.parseDate(tblNbsjInnerRuleVo.getEndtime(), "yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:mi:ss')");
		}
		//add by ping_gu start
		if (tblNbsjInnerRuleVo.getContent()!= null && tblNbsjInnerRuleVo.getContent().length() > 0) {
			sb.append(" AND (BODYINFO LIKE '%"+tblNbsjInnerRuleVo.getContent()+"%' OR rulename LIKE '%"+tblNbsjInnerRuleVo.getContent()+"%') ");
		}
		return sb.toString();
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
