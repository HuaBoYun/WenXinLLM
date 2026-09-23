package com.huabo.audit.oracle.mapper;

import com.hbfk.util.DateUtil;
import com.huabo.audit.oracle.entity.TblNbsjDoubtfulpointEntity;
import com.huabo.audit.oracle.vo.TblNbsjDoubtfulpointVo;
import com.huabo.audit.util.PageInfo;

public class TblNbsjDoubtfulpointMapperSqlConfig {
	public String selectPlanCodeByOrgid(TblNbsjDoubtfulpointEntity plan) {
		StringBuffer sb = new StringBuffer("SELECT COUNT(*) FROM TBL_NBSJ_DOUBTFULPOINT WHERE DPNUMBER='"+plan.getDpnumber()+"' ");
		if(plan.getDpointid() != null) {
			sb.append(" AND DPOINTID != "+plan.getDpointid());
		}
		return sb.toString();
	}
	
	public String selectListByPageInfo(PageInfo<TblNbsjDoubtfulpointEntity> pageInfo,TblNbsjDoubtfulpointVo tblNbsjDoubtfulpointVo) {
//		TblNbsjDoubtfulpointEntity plan = pageInfo.getCondition();
		
		StringBuffer sb = new StringBuffer("SELECT * FROM "
				+ "(SELECT T1.*,ROWNUM RN  FROM "
				+ "(SELECT TNA.* "
				+ "FROM TBL_NBSJ_DOUBTFULPOINT TNA "
//				+ "LEFT JOIN TBL_STAFF PRINCIPAL ON PRINCIPAL.STAFFID = TNA.PRINCIPALID "
//				+ "LEFT JOIN TBL_STAFF LEADER ON LEADER.STAFFID = TNA.LEADERID "
//				+ "LEFT JOIN TBL_ORGANIZATION AUDITORG ON AUDITORG.ORGID = TNA.AUDITORGID "
//				+ "LEFT JOIN TBL_STAFF CRESTAFF ON CRESTAFF.STAFFID = TNA.CREATESTAFFID "
				+ "WHERE 1=1 ");
		
		if(tblNbsjDoubtfulpointVo.getOrgid()!=null) {
			sb.append(" AND TNA.ORGID = '"+tblNbsjDoubtfulpointVo.getOrgid()+"'");
		}
		if(tblNbsjDoubtfulpointVo.getProjectid()!=null) {
			sb.append(" AND TNA.PROJECTID = '"+tblNbsjDoubtfulpointVo.getProjectid()+"'");
		}
		
		if(tblNbsjDoubtfulpointVo.getDpname()!=null && tblNbsjDoubtfulpointVo.getDpname().length()>0) {
			sb.append(" AND TNA.DPNAME LIKE '%"+tblNbsjDoubtfulpointVo.getDpname()+"%'");
		}
		
		if(tblNbsjDoubtfulpointVo.getDpnumber()!=null && tblNbsjDoubtfulpointVo.getDpnumber().length()>0) {
			sb.append(" AND TNA.DPNUMBER LIKE '%"+tblNbsjDoubtfulpointVo.getDpnumber()+"%'");
		}
		
		sb.append(" ORDER BY TNA.DPOINTID DESC) T1 WHERE ROWNUM <= "+(pageInfo.getCurrentRecord()+pageInfo.getPageSize())+" ) T2 WHERE T2.RN > "+pageInfo.getCurrentRecord());
		return sb.toString();
	}
	
	
	public String selectCountByPageInfo(PageInfo<TblNbsjDoubtfulpointEntity> pageInfo,TblNbsjDoubtfulpointVo tblNbsjDoubtfulpointVo) {
//		TblNbsjDoubtfulpointEntity plan = pageInfo.getCondition();
		StringBuffer sb = new StringBuffer("SELECT COUNT(0) "
				+ "FROM TBL_NBSJ_DOUBTFULPOINT TNA "
//				+ "LEFT JOIN TBL_STAFF PRINCIPAL ON PRINCIPAL.STAFFID = TNA.PRINCIPALID "
//				+ "LEFT JOIN TBL_STAFF LEADER ON LEADER.STAFFID = TNA.LEADERID "
//				+ "LEFT JOIN TBL_ORGANIZATION AUDITORG ON AUDITORG.ORGID = TNA.AUDITORGID "
//				+ "LEFT JOIN TBL_STAFF CRESTAFF ON CRESTAFF.STAFFID = TNA.CREATESTAFFID "
				+ "WHERE 1=1 ");
		
		if(tblNbsjDoubtfulpointVo.getOrgid()!=null) {
			sb.append(" AND TNA.ORGID = '"+tblNbsjDoubtfulpointVo.getOrgid()+"'");
		}
		if(tblNbsjDoubtfulpointVo.getProjectid()!=null) {
			sb.append(" AND TNA.PROJECTID = '"+tblNbsjDoubtfulpointVo.getProjectid()+"'");
		}
		
		if(tblNbsjDoubtfulpointVo.getDpname()!=null && tblNbsjDoubtfulpointVo.getDpname().length()>0) {
			sb.append(" AND TNA.DPNAME LIKE '%"+tblNbsjDoubtfulpointVo.getDpname()+"%'");
		}
		
		if(tblNbsjDoubtfulpointVo.getDpnumber()!=null && tblNbsjDoubtfulpointVo.getDpnumber().length()>0) {
			sb.append(" AND TNA.DPNUMBER LIKE '%"+tblNbsjDoubtfulpointVo.getDpnumber()+"%'");
		}
		
		return sb.toString();
	}
	
	public String updateEntity(TblNbsjDoubtfulpointEntity plan) {
		StringBuffer sqlSb = new StringBuffer("UPDATE TBL_NBSJ_DOUBTFULPOINT SET DPNAME = '"+plan.getDpname()+"' ");
		if(plan.getEdittime() != null && !"".equals(plan.getEdittime())) {
			sqlSb.append(" ,EDITTIME = TO_DATE('"+DateUtil.parseDate(plan.getEdittime(), "yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:mi:ss')");
		}
		if(plan.getDpdescribe() != null && !"".equals(plan.getDpdescribe())) {
			sqlSb.append(" ,DPDESCRIBE = '"+plan.getDpdescribe()+"'");
		}
		if(plan.getMemo() != null && !"".equals(plan.getMemo())) {
			sqlSb.append(" ,MEMO = '"+plan.getMemo()+"'");
		}
		if(plan.getTestresult() != null && !"".equals(plan.getTestresult())) {
			sqlSb.append(" ,TESTRESULT = '"+plan.getTestresult()+"'");
		}
		if(plan.getDpnumber() != null && !"".equals(plan.getDpnumber())) {
			sqlSb.append(" ,DPNUMBER = '"+plan.getDpnumber()+"'");
		}
		if(plan.getProjectid() != null) {
			sqlSb.append(" ,PROJECTID = '"+plan.getProjectid()+"'");
		}
		
		sqlSb.append(" WHERE DPOINTID= "+plan.getDpointid());
		return sqlSb.toString();
	}
	
	public String insertEntity(TblNbsjDoubtfulpointEntity plan){
		StringBuffer colSb = new StringBuffer("INSERT INTO TBL_NBSJ_DOUBTFULPOINT(DPOINTID,EDITOR,DPSTATUS");
		StringBuffer valSb = new StringBuffer("  VALUES (HIBERNATE_SEQUENCE.nextval,'"+plan.getEditor()+"','0'");
		
		if(plan.getDpname() != null && !"".equals(plan.getDpname())) {
			colSb.append(",DPNAME");
			valSb.append(",'"+plan.getDpname()+"'");
		}
		
		
		if(plan.getDpnumber() != null && !"".equals(plan.getDpnumber())) {
			colSb.append(",DPNUMBER");
			valSb.append(",'"+plan.getDpnumber()+"'");
		}
		
		if(plan.getEdittime() != null && !"".equals(plan.getEdittime())) {
			colSb.append(",EDITTIME");
			valSb.append(",TO_DATE('"+DateUtil.parseDate(plan.getEdittime(), "yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:mi:ss')");
		}
		
		if(plan.getDpdescribe() != null && !"".equals(plan.getDpdescribe())) {
			colSb.append(",DPDESCRIBE");
			valSb.append(",'"+plan.getDpdescribe()+"'");
		}
		
		if(plan.getMemo() != null && !"".equals(plan.getMemo())) {
			colSb.append(",MEMO");
			valSb.append(",'"+plan.getMemo()+"'");
		}
		
		if(plan.getTestresult() != null && !"".equals(plan.getTestresult())) {
			colSb.append(",TESTRESULT");
			valSb.append(",'"+plan.getTestresult()+"'");
		}
		
		if(plan.getOrgid() != null) {
			colSb.append(",ORGID");
			valSb.append(",'"+plan.getOrgid()+"'");
		}
		
		if(plan.getProjectid() != null) {
			colSb.append(",PROJECTID");
			valSb.append(",'"+plan.getProjectid()+"'");
		}
		
		String sql = colSb.toString()+")"+valSb.toString()+")";
		return sql;
	}
}
