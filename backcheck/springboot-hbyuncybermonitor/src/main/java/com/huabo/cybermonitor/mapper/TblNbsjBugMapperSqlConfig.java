package com.huabo.cybermonitor.mapper;

import cn.hutool.core.util.StrUtil;
import com.hbfk.util.DateUtil;
import com.hbfk.util.PageInfo;
import com.huabo.cybermonitor.entity.TblNbsjBugEntity;
import com.huabo.cybermonitor.entity.TblNbsjInnerrule;
import com.huabo.cybermonitor.entity.TblNbsjOuterruleEntity;
import com.huabo.cybermonitor.vo.TblNbsjBugVo;

public class TblNbsjBugMapperSqlConfig {
	public String selectPlanCodeByOrgid(TblNbsjBugEntity plan) {
		StringBuffer sb = new StringBuffer("SELECT COUNT(*) FROM TBL_YJPT_BUG WHERE 1=1 AND BUGNUMBER='"+plan.getBugnumber()+"' ");
		if(plan.getBugid() != null) {
			sb.append(" AND BUGID != "+plan.getBugid());
		}
		return sb.toString();
	}
	
	public String selectListByPageInfo(PageInfo<TblNbsjBugEntity> pageInfo, TblNbsjBugVo tblNbsjBugVo, Integer orgid, Integer companyid) {
		StringBuffer sb = new StringBuffer("SELECT * FROM "
				+ "(SELECT T1.*,ROWNUM RN  FROM "
				+ "(SELECT TNA.*,cc.*,ORG.ORGNAME "
				+ "FROM TBL_YJPT_BUG TNA "
				+ "LEFT JOIN TBL_YJPT_BUG_CRITERION cir on TNA.BUGID=CIR.BUGID "
				+ "LEFT JOIN TBL_YJPT_BUGCRITERION cc on cc.BUGCRIID=CIR.BUGCRIID "
				+ "LEFT JOIN TBL_ORGANIZATION ORG on ORG.ORGID=TNA.bugdepartment "
				+ "WHERE 1=1 ");
//				+ " AND TNA.BUGBYSYSTEM LIKE '%nbsj%' ");
//		if(plan.getPrincipalid() != null) {
//			sb.append(" AND TNA.PRINCIPALID =  "+plan.getPrincipalid());
//		}
		if(tblNbsjBugVo.getBugid()!=null) {
			sb.append(" AND TNA.BUGID !="+tblNbsjBugVo.getBugid()+" AND TNA.FATHERBUGID IS NULL");
		}
		if(null != orgid) {
			sb.append(" AND BUGDEPARTMENT='"+orgid+"'");
		}
		
		if(tblNbsjBugVo.getBugnumber()!=null && tblNbsjBugVo.getBugnumber().length()>0) {
			sb.append(" AND TNA.BUGNUMBER LIKE '%"+tblNbsjBugVo.getBugnumber()+"%'");
		}
		if(tblNbsjBugVo.getBugcriid()!=null && tblNbsjBugVo.getBugcriid().length()>0) {
			sb.append(" AND cir.BUGCRIID LIKE '%"+tblNbsjBugVo.getBugcriid()+"%'");
		}
		
		if(StrUtil.isNotBlank(tblNbsjBugVo.getStartDate() )){
			sb.append(" AND TNA.DISCOVERTIME >= TO_DATE('"+tblNbsjBugVo.getStartDate()+"','yyyy-MM-dd')");
		}
		
		if(StrUtil.isNotBlank(tblNbsjBugVo.getEndDate() )){
			sb.append(" AND TNA.DISCOVERTIME <= TO_DATE('"+tblNbsjBugVo.getEndDate()+" 23:59:59','yyyy-mm-dd HH24:MI:SS')");
		}
		
		sb.append(" ORDER BY TNA.BUGID DESC) T1 WHERE ROWNUM <= "+(pageInfo.getCurrentRecord()+pageInfo.getPageSize())+" ) T2 WHERE T2.RN > "+pageInfo.getCurrentRecord());
		System.out.println(sb.toString());
		return sb.toString();
	}
	
	
	public String selectCountByPageInfo(PageInfo<TblNbsjBugEntity> pageInfo,TblNbsjBugVo tblNbsjBugVo,Integer orgid,Integer companyid) {
		TblNbsjBugEntity plan = pageInfo.getCondition();
		StringBuffer sb = new StringBuffer("SELECT COUNT(0) "
				+ "FROM TBL_YJPT_BUG TNA "
				+ "LEFT JOIN TBL_YJPT_BUG_CRITERION cir on TNA.BUGID=CIR.BUGID "
				+ "WHERE 1=1 ");
//				+ " AND BUGDEPARTMENT='"+orgid+"'");
//				+ " AND TNA.BUGBYSYSTEM LIKE '%nbsj%' ");
		if(null != orgid) {
			sb.append(" AND TNA.BUGID !="+tblNbsjBugVo.getBugid()+" AND TNA.FATHERBUGID IS NULL");
		}
		if(tblNbsjBugVo.getBugid()!=null) {
			sb.append(" AND TNA.BUGID !="+tblNbsjBugVo.getBugid());
		}
		if(tblNbsjBugVo.getBugnumber()!=null && tblNbsjBugVo.getBugnumber().length()>0) {
			sb.append(" AND TNA.BUGNUMBER LIKE '%"+tblNbsjBugVo.getBugnumber()+"%'");
		}
		if(tblNbsjBugVo.getBugcriid()!=null && tblNbsjBugVo.getBugcriid().length()>0) {
			sb.append(" AND cir.BUGCRIID LIKE '%"+tblNbsjBugVo.getBugcriid()+"%'");
		}
		if(StrUtil.isNotBlank(tblNbsjBugVo.getStartDate() )){
			sb.append(" AND TNA.DISCOVERTIME >= TO_DATE('"+tblNbsjBugVo.getStartDate()+"','yyyy-MM-dd')");
		}
		
		if(StrUtil.isNotBlank(tblNbsjBugVo.getEndDate() )){
			sb.append(" AND TNA.DISCOVERTIME <= TO_DATE('"+tblNbsjBugVo.getEndDate()+" 23:59:59','yyyy-mm-dd HH24:MI:SS')");
		}
		System.out.println(sb.toString());
		return sb.toString();
	}
	
	public String updateEntity(TblNbsjBugEntity plan) {
		StringBuffer sqlSb = new StringBuffer("UPDATE TBL_YJPT_BUG SET BUGNUMBER = '"+plan.getBugnumber()+"'");
		if(plan.getDiscovertime() != null && !"".equals(plan.getDiscovertime())) {
			sqlSb.append(" ,DISCOVERTIME = TO_DATE('"+DateUtil.parseDate(plan.getDiscovertime(), "yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:mi:ss')");
		}
		if(plan.getDiscoverperson() != null && !"".equals(plan.getDiscoverperson())) {
			sqlSb.append(" ,DISCOVERPERSON = '"+plan.getDiscoverperson()+"'");
		}
		if(plan.getBugdescripte() != null && !"".equals(plan.getBugdescripte())) {
			sqlSb.append(" ,BUGDESCRIPTE = '"+plan.getBugdescripte()+"'");
		}
		if(plan.getBugproperty() != null && !"".equals(plan.getBugproperty())) {
			sqlSb.append(" ,BUGPROPERTY = '"+plan.getBugproperty()+"'");
		}
		if(plan.getBugdepartment() != null && !"".equals(plan.getBugdepartment())) {
			sqlSb.append(" ,BUGDEPARTMENT = '"+plan.getBugdepartment()+"'");
		}
		if(plan.getBusinessDescription() != null && !"".equals(plan.getBusinessDescription())) {
			sqlSb.append(" ,BUSINESSDESCRIPTION = '"+plan.getBusinessDescription()+"'");
		}
		if(plan.getNeedreform() != null && !"".equals(plan.getNeedreform())) {
			sqlSb.append(" ,NEEDREFORM = '"+plan.getNeedreform()+"'");
		}
		if(plan.getBugsource() != null && !"".equals(plan.getBugsource())) {
			sqlSb.append(" ,BUGSOURCE = '"+plan.getBugsource()+"'");
		}
		if(plan.getResonfornoreform() != null && !"".equals(plan.getResonfornoreform())) {
			sqlSb.append(" ,RESONFORNOREFORM = '"+plan.getResonfornoreform()+"'");
		}
		
		sqlSb.append(" WHERE BUGID= "+plan.getBugid());
		return sqlSb.toString();
	}
	
	public String insertEntity(TblNbsjBugEntity plan){
		StringBuffer colSb = new StringBuffer("INSERT INTO TBL_YJPT_BUG(BUGID,BUGREFORMSTATUS");
		StringBuffer valSb = new StringBuffer("  VALUES (HIBERNATE_SEQUENCE.nextval,0");
		
		if(plan.getBugnumber() != null && !"".equals(plan.getBugnumber())) {
			colSb.append(",BUGNUMBER");
			valSb.append(",'"+plan.getBugnumber()+"'");
		}
		if(plan.getDiscovertime() != null && !"".equals(plan.getDiscovertime())) {
			colSb.append(",DISCOVERTIME");
			valSb.append(",TO_DATE('"+DateUtil.parseDate(plan.getDiscovertime(), "yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:mi:ss')");
		}
		if(plan.getDiscoverperson() != null && !"".equals(plan.getDiscoverperson())) {
			colSb.append(",DISCOVERPERSON");
			valSb.append(",'"+plan.getDiscoverperson()+"'");
		}
		if(plan.getBugdescripte() != null && !"".equals(plan.getBugdescripte())) {
			colSb.append(",BUGDESCRIPTE");
			valSb.append(",'"+plan.getBugdescripte()+"'");
		}
		if(plan.getBugproperty() != null && !"".equals(plan.getBugproperty())) {
			colSb.append(",BUGPROPERTY");
			valSb.append(",'"+plan.getBugproperty()+"'");
		}
		if(plan.getBugdepartment() != null && !"".equals(plan.getBugdepartment())) {
			colSb.append(",BUGDEPARTMENT");
			valSb.append(",'"+plan.getBugdepartment()+"'");
		}
		if(plan.getBusinessDescription() != null && !"".equals(plan.getBusinessDescription())) {
			colSb.append(",BUSINESSDESCRIPTION");
			valSb.append(",'"+plan.getBusinessDescription()+"'");
		}
		if(plan.getNeedreform() != null && !"".equals(plan.getNeedreform())) {
			colSb.append(",NEEDREFORM");
			valSb.append(",'"+plan.getNeedreform()+"'");
		}
		if(plan.getBugsource() != null && !"".equals(plan.getBugsource())) {
			colSb.append(",BUGSOURCE");
			valSb.append(",'"+plan.getBugsource()+"'");
		}
		if(plan.getResonfornoreform() != null && !"".equals(plan.getResonfornoreform())) {
			colSb.append(",RESONFORNOREFORM");
			valSb.append(",'"+plan.getResonfornoreform()+"'");
		}
		
		String sql = colSb.toString()+")"+valSb.toString()+")";
		return sql;
	}
	
	//==
	public String selectInnerCommonListByPageInfo(PageInfo<TblNbsjInnerrule> pageInfo, Integer bugid, Integer orgid) {
		StringBuffer sb = new StringBuffer("SELECT * FROM "
				+ "(SELECT T1.*,ROWNUM RN  FROM "
				+ "(SELECT TNA.innrulid,TNA.rulecode,TNA.rulename,TNA.publishorg,TNA.publishdate,TNA.rulenumber,ORG.ORGNAME "
				+ "FROM TBL_YJPT_INNERRULE TNA "
				+ "LEFT JOIN TBL_ORGANIZATION ORG ON ORG.ORGID = TNA.publishorg "
				+ "WHERE INNRULID not in "
				+ "(SELECT DISTINCT INNRULID from TBL_YJPT_BUG_INNERRULE "
				+ "where bugid="+bugid+") "
				+ "and PUBLISHORG in "
				+ "( select to_char(ORGID) from TBL_ORGANIZATION where (1 = 1  AND orgtype=0 and (STATUS != 1 or STATUS IS NULL) ) "
				+ "or  ORGID = "+orgid+" start with  ORGID="+orgid+" connect by prior ORGID= fatherorgid) ");
				
		sb.append(" ORDER BY TNA.INNRULID DESC) T1 WHERE ROWNUM <= "+(pageInfo.getCurrentRecord()+pageInfo.getPageSize())+" ) T2 WHERE T2.RN > "+pageInfo.getCurrentRecord());
		return sb.toString();
	}
	public String selectInnerCommonCountByPageInfo(PageInfo<TblNbsjInnerrule> pageInfo,Integer bugid,Integer orgid) {
		StringBuffer sb = new StringBuffer("SELECT COUNT(0) "
				+ "FROM TBL_YJPT_INNERRULE TNA "
				+ "WHERE INNRULID not in "
				+ "(SELECT DISTINCT INNRULID from TBL_YJPT_BUG_INNERRULE "
				+ "where bugid="+bugid+") "
				+ "and PUBLISHORG in "
				+ "( select to_char(ORGID) from TBL_ORGANIZATION where (1 = 1  AND orgtype=0 and (STATUS != 1 or STATUS IS NULL) ) "
				+ "or  ORGID = "+orgid+" start with  ORGID="+orgid+" connect by prior ORGID= fatherorgid) ");
		
		return sb.toString();
	}
	public String selectInnerCommonListByLink(PageInfo<TblNbsjInnerrule> pageInfo,Integer bugid,Integer orgid) {
		StringBuffer sb = new StringBuffer("SELECT * FROM "
				+ "(SELECT T1.*,ROWNUM RN  FROM "
				+ "(SELECT TNA.innrulid,TNA.rulecode,TNA.rulename,TNA.publishorg,TNA.publishdate,TNA.rulenumber,ORG.ORGNAME "
				+ "FROM TBL_YJPT_INNERRULE TNA "
				+ "LEFT JOIN TBL_ORGANIZATION ORG ON ORG.ORGID = TNA.publishorg "
				+ " LEFT JOIN TBL_YJPT_BUG_INNERRULE TBI on TNA.INNRULID=TBI.INNRULID "
				+ " LEFT JOIN TBL_YJPT_BUG TBUG on TBUG.BUGID=TBI.BUGID "
				+ " WHERE TBUG.BUGID = "+bugid);
		sb.append(" ORDER BY TNA.INNRULID DESC) T1 WHERE ROWNUM <= "+(pageInfo.getCurrentRecord()+pageInfo.getPageSize())+" ) T2 WHERE T2.RN > "+pageInfo.getCurrentRecord());
		return sb.toString();
	}
	
	
	//===============
	public String selectOuterCommonListByPageInfo(PageInfo<TblNbsjOuterruleEntity> pageInfo, Integer bugid, Integer orgid) {
		StringBuffer sb = new StringBuffer("SELECT * FROM "
				+ "(SELECT T1.*,ROWNUM RN  FROM "
				+ "(SELECT TNA.outrulid,TNA.rulecode,TNA.rulename,TNA.publishorg,TNA.publishdate,TNA.rulenumber,ORG.ORGNAME "
				+ "FROM TBL_YJPT_OUTERRULE TNA "
				+ "LEFT JOIN TBL_ORGANIZATION ORG ON ORG.ORGID = TNA.CREATEORGID "
				+ "WHERE OUTRULID not in "
				+ "(SELECT DISTINCT OUTRULID from TBL_YJPT_BUG_OUTERRULE "
				+ "where bugid="+bugid+") "
				+ "and CREATEORGID in "
				+ " ( select to_char(two_org.ORGID) from TBL_ORGANIZATION two_org "
				+ "where (1 = 1  AND two_org.orgtype=0 and (two_org.STATUS != 1 or two_org.STATUS IS NULL) ) or  two_org.ORGID = "+orgid+" "
				+ "start with  two_org.ORGID="+orgid+" connect by prior two_org.ORGID= fatherorgid)");
				
		sb.append(" ORDER BY TNA.OUTRULID DESC) T1 WHERE ROWNUM <= "+(pageInfo.getCurrentRecord()+pageInfo.getPageSize())+" ) T2 WHERE T2.RN > "+pageInfo.getCurrentRecord());
		return sb.toString();
	}
	public String selectOuterCommonCountByPageInfo(PageInfo<TblNbsjOuterruleEntity> pageInfo,Integer bugid,Integer orgid) {
		StringBuffer sb = new StringBuffer("SELECT COUNT(0) "
				+ "FROM TBL_YJPT_OUTERRULE TNA "
				+ "WHERE OUTRULID not in "
				+ "(SELECT DISTINCT OUTRULID from TBL_YJPT_BUG_OUTERRULE "
				+ "where bugid="+bugid+") "
				+ "and CREATEORGID in "
				+ " ( select to_char(ORGID) from TBL_ORGANIZATION "
				+ "where (1 = 1  AND orgtype=0 and (STATUS != 1 or STATUS IS NULL) ) or  ORGID = "+orgid+" "
				+ "start with  ORGID="+orgid+" connect by prior ORGID= fatherorgid)");
		
		return sb.toString();
	}
	public String selectOuterCommonListByLink(PageInfo<TblNbsjOuterruleEntity> pageInfo,Integer bugid,Integer orgid) {
		StringBuffer sb = new StringBuffer("SELECT * FROM "
				+ "(SELECT T1.*,ROWNUM RN  FROM "
				+ "(SELECT TNA.outrulid,TNA.rulecode,TNA.rulename,TNA.publishorg,TNA.publishdate,TNA.rulenumber,ORG.ORGNAME "
				+ "FROM TBL_YJPT_OUTERRULE TNA "
				+ "LEFT JOIN TBL_ORGANIZATION ORG ON ORG.ORGID = TNA.CREATEORGID "
				+ " LEFT JOIN TBL_YJPT_BUG_OUTERRULE TBI on TNA.outrulid=TBI.outrulid "
				+ " LEFT JOIN TBL_YJPT_BUG TBUG on TBUG.BUGID=TBI.BUGID "
				+ " WHERE TBUG.BUGID = "+bugid);
				
		sb.append(" ORDER BY TNA.OUTRULID DESC) T1 WHERE ROWNUM <= "+(pageInfo.getCurrentRecord()+pageInfo.getPageSize())+" ) T2 WHERE T2.RN > "+pageInfo.getCurrentRecord());
		return sb.toString();
	}
	//============================================
	
	public String selectDefectLinkList(PageInfo<TblNbsjBugEntity> pageInfo,Integer bugid) {
		StringBuffer sb = new StringBuffer("SELECT * FROM "
				+ "(SELECT T1.*,ROWNUM RN  FROM "
				+ "(SELECT * "
				+ "FROM TBL_YJPT_BUG TNA "
				+ "LEFT JOIN TBL_YJPT_BUG_CRITERION cir on TNA.BUGID=CIR.BUGID "
				+ "WHERE 1=1 "
				+ " AND TNA.FATHERBUGID='"+bugid+"'");
		
		sb.append(" ORDER BY TNA.BUGID DESC) T1 WHERE ROWNUM <= "+(pageInfo.getCurrentRecord()+pageInfo.getPageSize())+" ) T2 WHERE T2.RN > "+pageInfo.getCurrentRecord());
		return sb.toString();
	}
	
	//==
	public String insertBugOutrulids(String outrulid, Integer bugid){
		StringBuffer colSb = new StringBuffer("INSERT INTO TBL_YJPT_BUG_OUTERRULE(BUGID,OUTRULID) VALUES("+bugid+","+outrulid+")");
		return colSb.toString();
	}
	
}
