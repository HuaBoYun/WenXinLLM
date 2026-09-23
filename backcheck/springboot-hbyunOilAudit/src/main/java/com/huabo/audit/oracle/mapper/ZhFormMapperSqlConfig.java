package com.huabo.audit.oracle.mapper;

import com.hbfk.util.DateUtil;
import com.huabo.audit.oracle.entity.ZhFormEntity;
import com.huabo.audit.oracle.vo.ZhFormVo;
import com.huabo.audit.util.PageInfo;

public class ZhFormMapperSqlConfig {
	public String selectPlanCodeByOrgid(ZhFormEntity plan) {
		StringBuffer sb = new StringBuffer("SELECT COUNT(*) FROM TBL_ZH_FORM WHERE 1=1 AND FORMNAME='"+plan.getFormName()+"' ");
		if(plan.getFormid() != null) {
			sb.append(" AND FORMID != "+plan.getFormid());
		}
		return sb.toString();
	}
	
	public String selectListByPageInfo(PageInfo<ZhFormEntity> pageInfo,ZhFormVo zhFormVo) {
		
		StringBuffer sb = new StringBuffer("SELECT * FROM "
				+ "(SELECT T1.*,ROWNUM RN  FROM "
				+ "(SELECT TNA.* "
				+ "FROM TBL_ZH_FORM TNA "
//				+ "LEFT JOIN TBL_STAFF PRINCIPAL ON PRINCIPAL.STAFFID = TNA.PRINCIPALID "
//				+ "LEFT JOIN TBL_STAFF LEADER ON LEADER.STAFFID = TNA.LEADERID "
//				+ "LEFT JOIN TBL_ORGANIZATION AUDITORG ON AUDITORG.ORGID = TNA.AUDITORGID "
//				+ "LEFT JOIN TBL_STAFF CRESTAFF ON CRESTAFF.STAFFID = TNA.CREATESTAFFID "
				+ "WHERE 1=1 ");
		
//		if(plan.getPrincipalid() != null) {
//			sb.append(" AND TNA.PRINCIPALID =  "+plan.getPrincipalid());
//		}
		if(zhFormVo.getFormname()!=null && zhFormVo.getFormname().length()>0) {
			sb.append(" AND TNA.FORMNAME LIKE '%"+zhFormVo.getFormname()+"%'");
		}
		
		if(zhFormVo.getStartDate() !=null){
			sb.append(" AND TNA.SELFDATE >= TO_DATE('"+zhFormVo.getStartDate()+"','yyyy-MM-dd')");
		}
		
		if(zhFormVo.getEndDate() !=null){
			sb.append(" AND TNA.SELFDATE <= TO_DATE('"+zhFormVo.getEndDate()+" 23:59:59','yyyy-mm-dd HH24:MI:SS')");
		}
		
		sb.append(" ORDER BY TNA.FORMID DESC) T1 WHERE ROWNUM <= "+(pageInfo.getCurrentRecord()+pageInfo.getPageSize())+" ) T2 WHERE T2.RN > "+pageInfo.getCurrentRecord());
		return sb.toString();
	}
	
	
	public String selectCountByPageInfo(PageInfo<ZhFormEntity> pageInfo,ZhFormVo zhFormVo) {
		ZhFormEntity plan = pageInfo.getCondition();
		StringBuffer sb = new StringBuffer("SELECT COUNT(0) "
				+ "FROM TBL_ZH_FORM TNA "
//				+ "LEFT JOIN TBL_STAFF PRINCIPAL ON PRINCIPAL.STAFFID = TNA.PRINCIPALID "
//				+ "LEFT JOIN TBL_STAFF LEADER ON LEADER.STAFFID = TNA.LEADERID "
//				+ "LEFT JOIN TBL_ORGANIZATION AUDITORG ON AUDITORG.ORGID = TNA.AUDITORGID "
//				+ "LEFT JOIN TBL_STAFF CRESTAFF ON CRESTAFF.STAFFID = TNA.CREATESTAFFID "
				+ "WHERE 1=1 ");
		
		if(zhFormVo.getFormname()!=null && zhFormVo.getFormname().length()>0) {
			sb.append(" AND TNA.FORMNAME LIKE '%"+zhFormVo.getFormname()+"%'");
		}
		
		if(zhFormVo.getStartDate() !=null){
			sb.append(" AND TNA.SELFDATE >= TO_DATE('"+zhFormVo.getStartDate()+"','yyyy-MM-dd')");
		}
		
		if(zhFormVo.getEndDate() !=null){
			sb.append(" AND TNA.SELFDATE <= TO_DATE('"+zhFormVo.getEndDate()+" 23:59:59','yyyy-mm-dd HH24:MI:SS')");
		}
		
		return sb.toString();
	}
	
	public String updateEntity(ZhFormEntity plan) {
		StringBuffer sqlSb = new StringBuffer("UPDATE TBL_ZH_FORM SET FORMNAME = '"+plan.getFormName()+"'");
		if(plan.getFormType() != null && !"".equals(plan.getFormType())) {
			sqlSb.append(" ,FORMTYPE = '"+plan.getFormType()+"'");
		}
		sqlSb.append(" WHERE FORMID= "+plan.getFormid());
		return sqlSb.toString();
	}
	
	public String insertEntity(ZhFormEntity plan){
		StringBuffer colSb = new StringBuffer("INSERT INTO TBL_ZH_FORM(FORMID,SELFDATE,SELFSTAFF,PROJECTID,STATUS");
		StringBuffer valSb = new StringBuffer("  VALUES (HIBERNATE_SEQUENCE.nextval,TO_DATE('"+DateUtil.parseDate(plan.getSelfDate(), "yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:mi:ss'),"+plan.getSelfStaff().getStaffid()+","+plan.getProject().getProjectId()+",0");
		
		if(plan.getFormName() != null && !"".equals(plan.getFormName())) {
			colSb.append(",FORMNAME");
			valSb.append(",'"+plan.getFormName()+"'");
		}
		if(plan.getFormType() != null && !"".equals(plan.getFormType())) {
			colSb.append(",FORMTYPE");
			valSb.append(",'"+plan.getFormType()+"'");
		}
		
		String sql = colSb.toString()+")"+valSb.toString()+")";
		return sql;
	}
}
