package com.huabo.audit.oracle.mapper;

import com.huabo.audit.oracle.entity.TblNbsjBugCriterion;
import com.huabo.audit.util.PageInfo;

public class TblNbsjBugCriterionMapperSqlConfig {
	
	 
	
	public String insertEntity(TblNbsjBugCriterion bug){
		StringBuffer colSb = new StringBuffer("INSERT INTO TBL_NBSJ_BUGCRITERION(BUGCRIID");
		StringBuffer valSb = new StringBuffer("  VALUES (HIBERNATE_SEQUENCE.nextval");
		if(bug.getBugcrilevel() != null && !"".equals(bug.getBugcrilevel())) {
			colSb.append(",BUGCRILEVEL");
			valSb.append(",'"+bug.getBugcrilevel()+"'");
		}
		if(bug.getBugcridefine() != null && !"".equals(bug.getBugcridefine())) {
			colSb.append(",BUGCRIDEFINE");
			valSb.append(",'"+bug.getBugcridefine()+"'");
		}
		if(bug.getStatus() != null && !"".equals(bug.getStatus())) {
			colSb.append(",STATUS");
			valSb.append(",'"+bug.getStatus()+"'");
		}
		if(bug.getBugcriration() != null && !"".equals(bug.getBugcriration())) {
			colSb.append(",BUGCRIRATION");
			valSb.append(",'"+bug.getBugcriration()+"'");
		}
		if(bug.getBugcristability() != null && !"".equals(bug.getBugcristability())) {
			colSb.append(",BUGCRISTABILITY");
			valSb.append(",'"+bug.getBugcristability()+"'");
		}
		if(bug.getVersion() != null && !"".equals(bug.getVersion())) {
			colSb.append(",version");
			valSb.append(",'"+bug.getVersion()+"'");
		}
		if(bug.getOrgid()!= null && !"".equals(bug.getOrgid())) {
			colSb.append(",orgid");
			valSb.append(",'"+bug.getOrgid()+"'");
		}
		 
		String sql = colSb.toString()+")"+valSb.toString()+")";
		System.out.println(sql);
		return sql;
	}
	
	public String updateEntity(TblNbsjBugCriterion bug) {
		StringBuffer sqlSb = new StringBuffer("UPDATE TBL_NBSJ_BUGCRITERION SET version=");
		sqlSb.append("'"+bug.getVersion()+"'");
		if(bug.getBugcrilevel() != null && !"".equals(bug.getBugcrilevel())) {
			sqlSb.append(" ,BUGCRILEVEL = '"+bug.getBugcrilevel()+"'");
		}
		if(bug.getBugcridefine() != null && !"".equals(bug.getBugcridefine())) {
			sqlSb.append(" ,BUGCRIDEFINE = '"+bug.getBugcridefine()+"'");
		}
		if(bug.getStatus()!=null) {
			sqlSb.append(" ,STATUS = '"+bug.getStatus()+"'");
		}
		if(bug.getBugcriration() != null && !"".equals(bug.getBugcriration())) {
			sqlSb.append(" ,BUGCRIRATION = '"+bug.getBugcriration()+"'");
		}
		if(bug.getBugcristability() != null && !"".equals(bug.getBugcristability())) {
			sqlSb.append(" ,BUGCRISTABILITY = '"+bug.getBugcristability()+"'");
		}
		if(bug.getOrgid()!= null && !"".equals(bug.getOrgid())) {
			sqlSb.append(" ,orgid = '"+bug.getOrgid()+"'");
		}
		sqlSb.append(" WHERE BUGCRIID= "+bug.getBugcriid());
		return sqlSb.toString();
	}
	public String selectNbsjBugCriterionListByPageInfo(PageInfo<TblNbsjBugCriterion> pageInfo, Integer orgid) throws Exception {
		StringBuffer sqlSb = new StringBuffer();
		sqlSb.append(" SELECT * from TBL_NBSJ_BUGCRITERION where 1=1 and ORGID="+orgid);
		sqlSb.append(" ORDER BY BUGCRIID DESC");
		System.out.println(sqlSb.toString());
		return sqlSb.toString();
	}
	public String selectNbsjBugCriterionListCountByPageInfo(PageInfo<TblNbsjBugCriterion> pageInfo, Integer orgid) throws Exception {
		StringBuffer sqlSb = new StringBuffer("SELECT COUNT(0) from TBL_NBSJ_BUGCRITERION  WHERE ORGID="+orgid);
		System.out.println(sqlSb.toString());
		return sqlSb.toString();
	}
}
