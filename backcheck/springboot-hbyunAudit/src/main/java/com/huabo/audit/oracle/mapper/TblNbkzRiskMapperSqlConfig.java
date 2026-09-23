package com.huabo.audit.oracle.mapper;

import java.math.BigDecimal;

import org.apache.ibatis.annotations.Select;

import com.hbfk.util.DateUtil;
import com.huabo.audit.oracle.entity.TblNbkzRiskEntity;
import com.huabo.audit.oracle.entity.TblNbsjRisktolerability;
import com.huabo.audit.oracle.vo.TblNbkzRiskVo;
import com.huabo.audit.util.PageInfo;

public class TblNbkzRiskMapperSqlConfig {
	public String selectPlanCodeByOrgid(TblNbkzRiskEntity plan) {
		StringBuffer sb = new StringBuffer("SELECT COUNT(*) FROM TBL_NBSJ_RISK WHERE 1=1 AND RISKNUMBER='"+plan.getRisknumber()+"' ");
		if(plan.getRiskid() != null) {
			sb.append(" AND RISKID != "+plan.getRiskid());
		}
		return sb.toString();
	}
	
	public String selectListByPageInfo(PageInfo<TblNbkzRiskEntity> pageInfo,TblNbkzRiskVo tblNbkzRiskVo,Integer orgid) {
		StringBuffer sb = new StringBuffer("SELECT * FROM "
				+ "(SELECT T1.*,ROWNUM RN  FROM "
				+ "(SELECT TNA.*,STAFF.REALNAME,ORG.ORGNAME "
				+ "FROM TBL_NBSJ_RISK TNA "
				+ "LEFT JOIN TBL_STAFF STAFF ON STAFF.STAFFID = TNA.CREATEORID "
				+ "LEFT JOIN TBL_ORGANIZATION ORG ON ORG.ORGID = TNA.ORGID "
				+ "WHERE 1=1 ");
//				+ " AND STYPE LIKE 'nbsj%'");
		
		if(orgid != null) {
			sb.append(" AND ORG.ORGID =  "+orgid);
		}
		if(tblNbkzRiskVo.getRiskname()!=null && tblNbkzRiskVo.getRiskname().length()>0) {
			sb.append(" AND TNA.RISKNAME LIKE '%"+tblNbkzRiskVo.getRiskname()+"%'");
		}
		if(tblNbkzRiskVo.getRisknumber()!=null && tblNbkzRiskVo.getRisknumber().length()>0) {
			sb.append(" AND TNA.RISKNUMBER LIKE '%"+tblNbkzRiskVo.getRisknumber()+"%'");
		}
		
		if(tblNbkzRiskVo.getStartDate() !=null){
			sb.append(" AND TNA.DISCOVEREDDATE >= TO_DATE('"+tblNbkzRiskVo.getStartDate()+"','yyyy-MM-dd')");
		}
		
		if(tblNbkzRiskVo.getEndDate() !=null){
			sb.append(" AND TNA.DISCOVEREDDATE <= TO_DATE('"+tblNbkzRiskVo.getEndDate()+" 23:59:59','yyyy-mm-dd HH24:MI:SS')");
		}
		
		sb.append(" ORDER BY TNA.RISKID DESC) T1 WHERE ROWNUM <= "+(pageInfo.getCurrentRecord()+pageInfo.getPageSize())+" ) T2 WHERE T2.RN > "+pageInfo.getCurrentRecord());
		return sb.toString();
	}
	
	
	public String selectCountByPageInfo(PageInfo<TblNbkzRiskEntity> pageInfo,TblNbkzRiskVo tblNbkzRiskVo,Integer orgid) {
		TblNbkzRiskEntity plan = pageInfo.getCondition();
		StringBuffer sb = new StringBuffer("SELECT COUNT(0) "
				+ "FROM TBL_NBSJ_RISK TNA "
				+ "LEFT JOIN TBL_STAFF STAFF ON STAFF.STAFFID = TNA.CREATEORID "
				+ "LEFT JOIN TBL_ORGANIZATION ORG ON ORG.ORGID = TNA.ORGID "
				+ "WHERE 1=1 ");
//				+ " AND STYPE LIKE 'nbsj%'");
		if(orgid != null) {
			sb.append(" AND ORG.ORGID =  "+orgid);
		}
		if(tblNbkzRiskVo.getRiskname()!=null && tblNbkzRiskVo.getRiskname().length()>0) {
			sb.append(" AND TNA.RISKNAME LIKE '%"+tblNbkzRiskVo.getRiskname()+"%'");
		}
		if(tblNbkzRiskVo.getRisknumber()!=null && tblNbkzRiskVo.getRisknumber().length()>0) {
			sb.append(" AND TNA.RISKNUMBER LIKE '%"+tblNbkzRiskVo.getRisknumber()+"%'");
		}
		
		if(tblNbkzRiskVo.getStartDate() !=null){
			sb.append(" AND TNA.DISCOVEREDDATE >= TO_DATE('"+tblNbkzRiskVo.getStartDate()+"','yyyy-MM-dd')");
		}
		
		if(tblNbkzRiskVo.getEndDate() !=null){
			sb.append(" AND TNA.DISCOVEREDDATE <= TO_DATE('"+tblNbkzRiskVo.getEndDate()+" 23:59:59','yyyy-mm-dd HH24:MI:SS')");
		}
		
		return sb.toString();
	}
	
	public String updateEntity(TblNbkzRiskEntity plan) {
		StringBuffer sqlSb = new StringBuffer("UPDATE TBL_NBSJ_RISK SET RISKNUMBER = '"+plan.getRisknumber()+"'");
		if(plan.getRiskname() != null && !"".equals(plan.getRiskname())) {
			sqlSb.append(" ,RISKNAME = '"+plan.getRiskname()+"'");
		}
		if(plan.getOccureddate() != null && !"".equals(plan.getOccureddate())) {
			sqlSb.append(" ,OCCUREDDATE = TO_DATE('"+DateUtil.parseDate(plan.getOccureddate(), "yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:mi:ss')");
		}
		if(plan.getDiscovereddate() != null && !"".equals(plan.getDiscovereddate())) {
			sqlSb.append(" ,DISCOVEREDDATE = TO_DATE('"+DateUtil.parseDate(plan.getDiscovereddate(), "yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:mi:ss')");
		}
		if(plan.getOrgid() != null && !"".equals(plan.getOrgid())) {
			sqlSb.append(" ,ORGID = '"+plan.getOrgid()+"'");
		}
		if(plan.getSysOrgid() != null && !"".equals(plan.getSysOrgid())) {
			sqlSb.append(" ,SYSORGID = '"+plan.getSysOrgid()+"'");
		}
		if(plan.getCreateorid() != null && !"".equals(plan.getCreateorid())) {
			sqlSb.append(" ,CREATEORID = '"+plan.getCreateorid()+"'");
		}
		if(plan.getRiskdes() != null && !"".equals(plan.getRiskdes())) {
			sqlSb.append(" ,RISKDES = '"+plan.getRiskdes()+"'");
		}
		sqlSb.append(" WHERE RISKID= "+plan.getRiskid());
		return sqlSb.toString();
	}
	
	public String insertEntity(TblNbkzRiskEntity plan){
		StringBuffer colSb = new StringBuffer("INSERT INTO TBL_NBSJ_RISK(RISKID,CREATEDATE");
		StringBuffer valSb = new StringBuffer("  VALUES (HIBERNATE_SEQUENCE.nextval,TO_DATE('"+DateUtil.parseDate(plan.getCreateDate(), "yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:mi:ss')");
		
		if(plan.getRisknumber() != null && !"".equals(plan.getRisknumber())) {
			colSb.append(",RISKNUMBER");
			valSb.append(",'"+plan.getRisknumber()+"'");
		}
		if(plan.getRiskname() != null && !"".equals(plan.getRiskname())) {
			colSb.append(",RISKNAME");
			valSb.append(",'"+plan.getRiskname()+"'");
		}
		if(plan.getOccureddate() != null && !"".equals(plan.getOccureddate())) {
			colSb.append(",OCCUREDDATE");
			valSb.append(",TO_DATE('"+DateUtil.parseDate(plan.getOccureddate(), "yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:mi:ss')");
		}
		if(plan.getDiscovereddate() != null && !"".equals(plan.getDiscovereddate())) {
			colSb.append(",DISCOVEREDDATE");
			valSb.append(",TO_DATE('"+DateUtil.parseDate(plan.getDiscovereddate(), "yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:mi:ss')");
		}
		if(plan.getOrgid() != null && !"".equals(plan.getOrgid())) {
			colSb.append(",ORGID");
			valSb.append(",'"+plan.getOrgid()+"'");
		}
		if(plan.getSysOrgid() != null && !"".equals(plan.getSysOrgid())) {
			colSb.append(",SYSORGID");
			valSb.append(",'"+plan.getSysOrgid()+"'");
		}
		if(plan.getCreateorid() != null && !"".equals(plan.getCreateorid())) {
			colSb.append(",CREATEORID");
			valSb.append(",'"+plan.getCreateorid()+"'");
		}
		if(plan.getRiskdes() != null && !"".equals(plan.getRiskdes())) {
			colSb.append(",RISKDES");
			valSb.append(",'"+plan.getRiskdes()+"'");
		}
		
		String sql = colSb.toString()+")"+valSb.toString()+")";
		return sql;
	}
	
	
	
	
	
	
	
	
	
	
	//==
	public String updateToleEntity(TblNbsjRisktolerability plan) {
		StringBuffer sqlSb = new StringBuffer("UPDATE TBL_NBSJ_RISKTOLERABILITY SET RTCODE = '"+plan.getRtcode()+"'");
		if(plan.getRiskid() != null && !"".equals(plan.getRiskid())) {
			sqlSb.append(" ,RISKID = '"+plan.getRiskid()+"'");
		}
		if(plan.getLowerborder() != null && !"".equals(plan.getLowerborder())) {
			sqlSb.append(" ,LOWERBORDER = '"+plan.getLowerborder()+"'");
		}
		if(plan.getUpperborder() != null && !"".equals(plan.getUpperborder())) {
			sqlSb.append(" ,UPPERBORDER = '"+plan.getUpperborder()+"'");
		}
		if(plan.getColorstring() != null && !"".equals(plan.getColorstring())) {
			sqlSb.append(" ,COLORSTRING = '"+plan.getColorstring()+"'");
		}
		if(plan.getDescription() != null && !"".equals(plan.getDescription())) {
			sqlSb.append(" ,DESCRIPTION = '"+plan.getDescription()+"'");
		}
		if(plan.getMemo() != null && !"".equals(plan.getMemo())) {
			sqlSb.append(" ,MEMO = '"+plan.getMemo()+"'");
		}
		sqlSb.append(" WHERE TOLEID= "+plan.getToleid());
		return sqlSb.toString();
	}
	
	public String insertToleEntity(TblNbsjRisktolerability plan){
		StringBuffer colSb = new StringBuffer("INSERT INTO TBL_NBSJ_RISKTOLERABILITY(TOLEID");
		StringBuffer valSb = new StringBuffer("  VALUES (HIBERNATE_SEQUENCE.nextval");
		
		if(plan.getRtcode() != null && !"".equals(plan.getRtcode())) {
			colSb.append(",RTCODE");
			valSb.append(",'"+plan.getRtcode()+"'");
		}
		if(plan.getRiskid() != null && !"".equals(plan.getRiskid())) {
			colSb.append(",RISKID");
			valSb.append(",'"+plan.getRiskid()+"'");
		}
		if(plan.getLowerborder() != null && !"".equals(plan.getLowerborder())) {
			colSb.append(",LOWERBORDER");
			valSb.append(",'"+plan.getLowerborder()+"'");
		}
		if(plan.getUpperborder() != null && !"".equals(plan.getUpperborder())) {
			colSb.append(",UPPERBORDER");
			valSb.append(",'"+plan.getUpperborder()+"'");
		}
		if(plan.getColorstring() != null && !"".equals(plan.getColorstring())) {
			colSb.append(",COLORSTRING");
			valSb.append(",'"+plan.getColorstring()+"'");
		}
		if(plan.getDescription() != null && !"".equals(plan.getDescription())) {
			colSb.append(",DESCRIPTION");
			valSb.append(",'"+plan.getDescription()+"'");
		}
		if(plan.getMemo() != null && !"".equals(plan.getMemo())) {
			colSb.append(",MEMO");
			valSb.append(",'"+plan.getMemo()+"'");
		}
		
		String sql = colSb.toString()+")"+valSb.toString()+")";
		return sql;
	}
	
	public String selectFxrrdListByLink(PageInfo<TblNbsjRisktolerability> pageInfo,BigDecimal riskid) {
		StringBuffer sb = new StringBuffer("SELECT * FROM "
				+ "(SELECT T1.*,ROWNUM RN  FROM "
				+ "(SELECT * "
				+ "FROM TBL_NBSJ_RISKTOLERABILITY TNA "
				+ "WHERE 1=1 "
				+ " AND RISKID="+riskid);
		
//		if(plan.getPrincipalid() != null) {
//			sb.append(" AND TNA.PRINCIPALID =  "+plan.getPrincipalid());
//		}
		
		sb.append(" ORDER BY TNA.RTCODE ) T1 WHERE ROWNUM <= "+(pageInfo.getCurrentRecord()+pageInfo.getPageSize())+" ) T2 WHERE T2.RN > "+pageInfo.getCurrentRecord());
		return sb.toString();
	}
	
	
	
	
	public String selectListOrgInId(String sysorgid) {
		String lasts = sysorgid.substring(sysorgid.length() - 1);
		if(",".equals(lasts)) {
			sysorgid = sysorgid.substring(0,sysorgid.length() - 1);
		}
		
		StringBuffer sb = new StringBuffer("SELECT ORGNAME "
				+ "FROM TBL_ORGANIZATION TNA "
				+ "WHERE TNA.ORGID IN ("+sysorgid+") ");
		
		return sb.toString();
	}
	
	
	
	
	
	public String selectListByPageInfolist(TblNbkzRiskVo tblNbkzRiskVo,BigDecimal orgid) {
		StringBuffer sb = new StringBuffer("SELECT TNA.*,STAFF.REALNAME,ORG.ORGNAME "
				+ "FROM TBL_NBSJ_RISK TNA "
				+ "LEFT JOIN TBL_STAFF STAFF ON STAFF.STAFFID = TNA.CREATEORID "
				+ "LEFT JOIN TBL_ORGANIZATION ORG ON ORG.ORGID = TNA.ORGID "
				+ "WHERE 1=1 ");
//				+ " AND STYPE LIKE 'nbsj%'");
		
		if(orgid != null) {
			sb.append(" AND ORG.ORGID =  "+orgid);
		}
		if(tblNbkzRiskVo.getRiskname()!=null && tblNbkzRiskVo.getRiskname().length()>0) {
			sb.append(" AND TNA.RISKNAME LIKE '%"+tblNbkzRiskVo.getRiskname()+"%'");
		}
		if(tblNbkzRiskVo.getRisknumber()!=null && tblNbkzRiskVo.getRisknumber().length()>0) {
			sb.append(" AND TNA.RISKNUMBER LIKE '%"+tblNbkzRiskVo.getRisknumber()+"%'");
		}
		
		if(tblNbkzRiskVo.getStartDate() !=null){
			sb.append(" AND TNA.DISCOVEREDDATE >= TO_DATE('"+tblNbkzRiskVo.getStartDate()+"','yyyy-MM-dd')");
		}
		
		if(tblNbkzRiskVo.getEndDate() !=null){
			sb.append(" AND TNA.DISCOVEREDDATE <= TO_DATE('"+tblNbkzRiskVo.getEndDate()+" 23:59:59','yyyy-mm-dd HH24:MI:SS')");
		}
		
		sb.append(" ORDER BY TNA.RISKID DESC ");
		return sb.toString();
	}
	
}
