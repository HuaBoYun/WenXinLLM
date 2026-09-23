package com.huabo.audit.oracle.mapper;

import com.hbfk.util.DateUtil;
import com.hbfk.util.PageInfo;
import com.huabo.audit.oracle.entity.TblNbsjFactbookEntity;
import com.huabo.audit.oracle.vo.TblNbsjFactbookVo;

import cn.hutool.core.util.StrUtil;

public class TblNbsjFactbookMapperSqlConfig {
	public String selectPlanCodeByOrgid(TblNbsjFactbookEntity plan) {
		StringBuffer sb = new StringBuffer("SELECT COUNT(*) FROM TBL_NBSJ_FACTBOOK WHERE 1=1 AND FACTCODE='"+plan.getFactcode()+"' ");
		if(plan.getFactid() != null) {
			sb.append(" AND FACTID != "+plan.getFactid());
		}
		return sb.toString();
	}
	
	public String selectListByPageInfo(PageInfo<TblNbsjFactbookEntity> pageInfo,TblNbsjFactbookVo tblNbsjFactbookVo) {
		StringBuffer sb = new StringBuffer("SELECT * FROM "
				+ "(SELECT T1.*,ROWNUM RN  FROM "
				+ "(SELECT TNA.*,STAFF.REALNAME,PJ.PRJOECTNAME "
				+ "FROM TBL_NBSJ_FACTBOOK TNA "
				+ "LEFT JOIN TBL_STAFF STAFF ON STAFF.STAFFID = TNA.FACTSTAFFID "
				+ "LEFT JOIN TBL_NBSJ_PROJECT PJ ON PJ.PROJECTID = TNA.PROJECTID "
//				+ "LEFT JOIN TBL_ORGANIZATION AUDITORG ON AUDITORG.ORGID = TNA.AUDITORGID "
				+ "WHERE 1=1 ");
		
		if(tblNbsjFactbookVo.getProjectid() != null) {
			sb.append(" AND TNA.PROJECTID = "+tblNbsjFactbookVo.getProjectid());
		}
		
		if(tblNbsjFactbookVo.getFactcode() != null && !"".equals(tblNbsjFactbookVo.getFactcode())) {
			sb.append(" AND FACTCODE LIKE '%"+tblNbsjFactbookVo.getFactcode()+"%'");
		}
		
		if(tblNbsjFactbookVo.getFactname() != null && !"".equals(tblNbsjFactbookVo.getFactname())) {
			sb.append(" AND PJ.PRJOECTNAME LIKE '%"+tblNbsjFactbookVo.getFactname()+"%'");
		}
		
		sb.append(" ORDER BY TNA.FACTID DESC) T1 WHERE ROWNUM <= "+(pageInfo.getCurrentRecord()+pageInfo.getPageSize())+" ) T2 WHERE T2.RN > "+pageInfo.getCurrentRecord());
		return sb.toString();
	}
	
	
	public String selectCountByPageInfo(PageInfo<TblNbsjFactbookEntity> pageInfo,TblNbsjFactbookVo tblNbsjFactbookVo) {
//		TblNbsjFactbookEntity plan = pageInfo.getCondition();
		StringBuffer sb = new StringBuffer("SELECT COUNT(0) "
				+ "FROM TBL_NBSJ_FACTBOOK TNA "
				+ "LEFT JOIN TBL_STAFF STAFF ON STAFF.STAFFID = TNA.FACTSTAFFID "
				+ "LEFT JOIN TBL_NBSJ_PROJECT PJ ON PJ.PROJECTID = TNA.PROJECTID "
				+ "WHERE 1=1 ");
		
		if(tblNbsjFactbookVo.getProjectid() != null) {
			sb.append(" AND TNA.PROJECTID = "+tblNbsjFactbookVo.getProjectid());
		}
		
		if(tblNbsjFactbookVo.getFactcode() != null && !"".equals(tblNbsjFactbookVo.getFactcode())) {
			sb.append(" AND FACTCODE LIKE '%"+tblNbsjFactbookVo.getFactcode()+"%'");
		}
		
		if(tblNbsjFactbookVo.getFactname() != null && !"".equals(tblNbsjFactbookVo.getFactname())) {
			sb.append(" AND PJ.PRJOECTNAME LIKE '%"+tblNbsjFactbookVo.getFactname()+"%'");
		}
		
		return sb.toString();
	}
	
	public String updateEntity(TblNbsjFactbookEntity plan) {
		StringBuffer sqlSb = new StringBuffer("UPDATE TBL_NBSJ_FACTBOOK SET DESCRIBE = '"+plan.getDescribe()+"' ");
		if(plan.getStatus()!=null) {
			sqlSb.append(" ,STATUS = "+plan.getStatus());
		}
		if(plan.getFhstaffid() != null) {
			sqlSb.append(" ,FHSTAFFID = "+plan.getFhstaffid());
		}
		if(StrUtil.isNotBlank(plan.getFhstaffname())) {
			sqlSb.append(" ,FHSTAFFNAME = '"+plan.getFhstaffname()+"'");
		}
		if(plan.getFactstaffid() != null) {
			sqlSb.append(" ,FACTSTAFFID = "+plan.getFactstaffid());
		}
		if(StrUtil.isNotBlank(plan.getFactstaffname())) {
			sqlSb.append(" ,FACTSTAFFNAME = '"+plan.getFactstaffname()+"'");
		}
		if(StrUtil.isNotBlank(plan.getCreatestaffname())) {
			sqlSb.append(" ,CREATESTAFFNAME = '"+plan.getCreatestaffname()+"'");

		}
//		if(plan.getSheetName() != null && !"".equals(plan.getSheetName())) {
//			sqlSb.append(" ,SHEETNAME = '"+plan.getSheetName()+"'");
//		}
		
		sqlSb.append(" WHERE FACTID= "+plan.getFactid());
		System.out.println(sqlSb.toString());
		return sqlSb.toString();
	}
	
	public String insertEntity(TblNbsjFactbookEntity plan){
		StringBuffer colSb = new StringBuffer("INSERT INTO TBL_NBSJ_FACTBOOK(FACTID,CREATETIME,PROJECTID,CREATESTAFFID");
		StringBuffer valSb = new StringBuffer("  VALUES (HIBERNATE_SEQUENCE.nextval,TO_DATE('"+DateUtil.parseDate(plan.getCreatetime(), "yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:mi:ss'),"+plan.getProjectid()+","+plan.getCreatestaffid());
		
		if(StrUtil.isNotBlank(plan.getFactcode())) {
			colSb.append(",FACTCODE");
			valSb.append(",'"+plan.getFactcode()+"'");
		}
		if(StrUtil.isNotBlank(plan.getFactname())) {
			colSb.append(",FACTNAME");
			valSb.append(",'"+plan.getFactname()+"'");
		}
		if(plan.getDescribe() != null && !"".equals(plan.getDescribe())) {
			colSb.append(",DESCRIBE");
			valSb.append(",'"+plan.getDescribe()+"'");
		}
		if(plan.getFhstaffid() != null) {
			colSb.append(",FHSTAFFID");
			valSb.append(","+plan.getFhstaffid());
		}
		if(plan.getStatus() != null) {
			colSb.append(",STATUS");
			valSb.append(","+plan.getStatus());
		}
		if(StrUtil.isNotBlank(plan.getFhstaffname())) {
			colSb.append(",FHSTAFFNAME");
			valSb.append(",'"+plan.getFhstaffname()+"'");
		}
		if(plan.getFactstaffid() != null) {
			colSb.append(",FACTSTAFFID");
			valSb.append(","+plan.getFactstaffid());
		}
		if(StrUtil.isNotBlank(plan.getFactstaffname())) {
			colSb.append(",FACTSTAFFNAME");
			valSb.append(",'"+plan.getFactstaffname()+"'");
		}
		if(StrUtil.isNotBlank(plan.getCreatestaffname())) {
			colSb.append(",CREATESTAFFNAME");
			valSb.append(",'"+plan.getCreatestaffname()+"'");
		}
//		if(plan.getSheetName() != null && !"".equals(plan.getSheetName())) {
//			colSb.append(",SHEETNAME");
//			valSb.append(",'"+plan.getSheetName()+"'");
//		}
		
		String sql = colSb.toString()+")"+valSb.toString()+")";
		return sql;
	}
}
