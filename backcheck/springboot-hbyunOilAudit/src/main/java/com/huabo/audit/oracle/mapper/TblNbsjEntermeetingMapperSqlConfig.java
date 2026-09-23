package com.huabo.audit.oracle.mapper;

import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.DateUtil;
import com.huabo.audit.oracle.dto.EntermeetingListSearchDto;
import com.huabo.audit.oracle.entity.TblNbsjEntermeetingEntity;
import com.huabo.audit.oracle.vo.EntermeetingListSearchVO;
import com.huabo.audit.oracle.vo.PlanManageVO;
import com.huabo.audit.oracle.vo.TblNbsjEntermeetingVo;
import com.huabo.audit.util.PageInfo;

public class TblNbsjEntermeetingMapperSqlConfig {
	public String selectPlanCodeByOrgid(TblNbsjEntermeetingEntity plan) {
		StringBuffer sb = new StringBuffer("SELECT COUNT(*) FROM TBL_NBSJ_ENTERMEETING WHERE ENTERCOED = '"+plan.getEntercoed()+"'");
		if(plan.getEnterid() != null) {
			sb.append(" AND ENTERID != "+plan.getEnterid());
		}
		return sb.toString();
	}
	
	public String selectNbsjEntermeetingListByPageInfo(PageInfo<TblNbsjEntermeetingVo> pageInfo,TblNbsjEntermeetingVo tblNbsjEntermeetingVo) {
//		TblNbsjEntermeetingVo plan = pageInfo.getCondition();
		
		StringBuffer sb = new StringBuffer("SELECT * FROM "
				+ "(SELECT T1.*,ROWNUM RN  FROM "
				+ "(SELECT TNA.ENTERID,TNA.ENTERCOED,TNA.ENTERNAME,TNA.CREATRTIME,TNA.CREATESTAFFID,TNA.STATUS "
				+ "FROM TBL_NBSJ_ENTERMEETING TNA "
//				+ "LEFT JOIN TBL_STAFF PRINCIPAL ON PRINCIPAL.STAFFID = TNA.PRINCIPALID "
//				+ "LEFT JOIN TBL_STAFF LEADER ON LEADER.STAFFID = TNA.LEADERID "
//				+ "LEFT JOIN TBL_ORGANIZATION AUDITORG ON AUDITORG.ORGID = TNA.AUDITORGID "
//				+ "LEFT JOIN TBL_STAFF CRESTAFF ON CRESTAFF.STAFFID = TNA.CREATESTAFFID "
				+ "WHERE 1=1 ");
		
		if(tblNbsjEntermeetingVo.getProgectid() != null) {
			sb.append(" AND TNA.PROGECTID =  "+tblNbsjEntermeetingVo.getProgectid());
		}
		if(tblNbsjEntermeetingVo.getEntercoed()!=null && tblNbsjEntermeetingVo.getEntercoed().length()>0) {
			sb.append(" AND TNA.ENTERCOED LIKE '%"+tblNbsjEntermeetingVo.getEntercoed()+"%'");
		}
		if(tblNbsjEntermeetingVo.getEntername()!=null && tblNbsjEntermeetingVo.getEntername().length()>0) {
			sb.append(" AND TNA.ENTERNAME LIKE '%"+tblNbsjEntermeetingVo.getEntername()+"%'");
		}
		
		if(tblNbsjEntermeetingVo.getStartDate() !=null){
			sb.append(" AND TNA.CREATRTIME >= TO_DATE('"+tblNbsjEntermeetingVo.getStartDate()+"','yyyy-MM-dd')");
		}
		
		if(tblNbsjEntermeetingVo.getEndDate() !=null){
			sb.append(" AND TNA.CREATRTIME <= TO_DATE('"+tblNbsjEntermeetingVo.getEndDate()+" 23:59:59','yyyy-mm-dd HH24:MI:SS')");
		}
		
		sb.append(" ORDER BY TNA.ENTERID DESC) T1 WHERE ROWNUM <= "+(pageInfo.getCurrentRecord()+pageInfo.getPageSize())+" ) T2 WHERE T2.RN > "+pageInfo.getCurrentRecord());
		return sb.toString();
	}
	
	
	public String selectNbsjEntermeetingCountByPageInfo(PageInfo<TblNbsjEntermeetingVo> pageInfo,TblNbsjEntermeetingVo tblNbsjEntermeetingVo) {
//		TblNbsjEntermeetingVo plan = pageInfo.getCondition();
		
		StringBuffer sb = new StringBuffer("SELECT COUNT(0) "
				+ "FROM TBL_NBSJ_ENTERMEETING TNA "
//				+ "LEFT JOIN TBL_STAFF PRINCIPAL ON PRINCIPAL.STAFFID = TNA.PRINCIPALID "
//				+ "LEFT JOIN TBL_STAFF LEADER ON LEADER.STAFFID = TNA.LEADERID "
//				+ "LEFT JOIN TBL_ORGANIZATION AUDITORG ON AUDITORG.ORGID = TNA.AUDITORGID "
//				+ "LEFT JOIN TBL_STAFF CRESTAFF ON CRESTAFF.STAFFID = TNA.CREATESTAFFID "
				+ "WHERE 1=1 ");
		
		if(tblNbsjEntermeetingVo.getProgectid() != null) {
			sb.append(" AND TNA.PROGECTID =  "+tblNbsjEntermeetingVo.getProgectid());
		}
		
		if(tblNbsjEntermeetingVo.getEntercoed()!=null && tblNbsjEntermeetingVo.getEntercoed().length()>0) {
			sb.append(" AND TNA.ENTERCOED LIKE '%"+tblNbsjEntermeetingVo.getEntercoed()+"%'");
		}
		if(tblNbsjEntermeetingVo.getEntername()!=null && tblNbsjEntermeetingVo.getEntername().length()>0) {
			sb.append(" AND TNA.ENTERNAME LIKE '%"+tblNbsjEntermeetingVo.getEntername()+"%'");
		}
		
		if(tblNbsjEntermeetingVo.getStartDate() !=null){
			sb.append(" AND TNA.CREATRTIME >= TO_DATE('"+tblNbsjEntermeetingVo.getStartDate()+"','yyyy-MM-dd')");
		}
		
		if(tblNbsjEntermeetingVo.getEndDate() !=null){
			sb.append(" AND TNA.CREATRTIME <= TO_DATE('"+tblNbsjEntermeetingVo.getEndDate()+" 23:59:59','yyyy-mm-dd HH24:MI:SS')");
		}
		
		return sb.toString();
	}
	
	public String planManagePageList(PageInfo<TblNbsjEntermeetingEntity> pageInfo, PlanManageVO planManageVO) {
		return null;
	}
	
	public String auditPlanListSearch(PageInfo<EntermeetingListSearchVO> pageInfo, TblStaffUtil tblStaffUtil, EntermeetingListSearchDto auditPlanListSearchDTO) {
		return null;
	}
	
	public String updateEntity(TblNbsjEntermeetingEntity plan) {
		StringBuffer sqlSb = new StringBuffer("UPDATE TBL_NBSJ_ENTERMEETING SET ENTERNAME = '"+plan.getEntername()+"'");
		if(plan.getEntercoed() != null && !"".equals(plan.getEntercoed())) {
			sqlSb.append(" ,ENTERCOED = '"+plan.getEntercoed()+"'");
		}
		if(plan.getStatus() != null && !"".equals(plan.getStatus())) {
			sqlSb.append(" ,STATUS = '"+plan.getStatus()+"'");
		}
		if(plan.getContent() != null && !"".equals(plan.getContent())) {
			sqlSb.append(" ,CONTENT = '"+plan.getContent()+"'");
		}
		sqlSb.append(" WHERE ENTERID = "+plan.getEnterid());
		return sqlSb.toString();
	}
	
	public String insertEntity(TblNbsjEntermeetingEntity plan){
		StringBuffer colSb = new StringBuffer("INSERT INTO TBL_NBSJ_ENTERMEETING(ENTERID,CREATRTIME,CREATESTAFFID,STATUS");
		StringBuffer valSb = new StringBuffer("  VALUES (HIBERNATE_SEQUENCE.nextval,TO_DATE('"+DateUtil.parseDate(plan.getCreatrtime(), "yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:mi:ss'), "+plan.getCreatestaffid()+",0");
		
		if(plan.getEntercoed() != null && !"".equals(plan.getEntercoed())) {
			colSb.append(",ENTERCOED");
			valSb.append(",'"+plan.getEntercoed()+"'");
		}
		
		if(plan.getEntername() != null && !"".equals(plan.getEntername())) {
			colSb.append(",ENTERNAME");
			valSb.append(",'"+plan.getEntername()+"'");
		}
		
		if(plan.getContent() != null && !"".equals(plan.getContent())) {
			colSb.append(",CONTENT");
			valSb.append(",'"+plan.getContent()+"'");
		}
		
		if(plan.getProject().getProjectId() != null && !"".equals(plan.getProject().getProjectId())) {
			colSb.append(",PROGECTID");
			valSb.append(",'"+plan.getProject().getProjectId()+"'");
		}
		
		String sql = colSb.toString()+")"+valSb.toString()+")";
		return sql;
	}
}
