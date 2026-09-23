package com.huabo.audit.oracle.mapper;

import org.apache.commons.lang.StringUtils;

import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.DateUtil;
import com.hbfk.util.database.DataBaseSqlConfig;
import com.hbfk.util.database.GeneralSQLConcatConfig;
import com.huabo.audit.oracle.entity.TblNbsjAdvicenoteEntity;
import com.huabo.audit.oracle.vo.TblNbsjAdvicenoteVo;
import com.huabo.audit.util.PageInfo;

public class TblNbsjAdvicenoteMapperSqlConfig {
	public String selectPlanCodeByOrgid(TblNbsjAdvicenoteEntity plan) {
		StringBuffer sb = new StringBuffer("SELECT COUNT(*) FROM TBL_NBSJ_ADVICENOTE WHERE 1=1 AND ADVICECOED='"+plan.getAdvicecoed()+"' ");
		if(plan.getAdviceid() != null) {
			sb.append(" AND ADVICEID != "+plan.getAdviceid());
		}
		return sb.toString();
	}
	
	public String selectListByPageInfo(PageInfo<TblNbsjAdvicenoteEntity> pageInfo,TblNbsjAdvicenoteVo tblNbsjAdvicenoteVo,TblStaffUtil loginStaff)throws Exception {
		StringBuffer sb = new StringBuffer("SELECT * FROM "
				+ "(SELECT T1.*,ROWNUM RN  FROM "
				+ "(SELECT TNA.*,CRESTAFF.REALNAME "
				+ "FROM TBL_NBSJ_ADVICENOTE TNA "
				+ "LEFT JOIN TBL_STAFF CRESTAFF ON CRESTAFF.STAFFID = TNA.CREATESTAFFID "
				+ "WHERE 1=1 ");
		
		//安全保密SQL
		sb.append(GeneralSQLConcatConfig.concatSecrectSql(loginStaff.getCurrentOrg().getUseSecrect(), true, "TNA.ORGID", "TNA.ORGID",
				"TNA.CREATESTAFFID", "TNA.SECRECTLEVELID", "TNA.STAFFSCOPEIDS", loginStaff.getStaffid(), loginStaff.getDeptIds(), loginStaff.getSecrectScopeIds()));
		
		if(tblNbsjAdvicenoteVo.getProjectId()!=null) {
			sb.append(" AND TNA.PROGECTID = '"+tblNbsjAdvicenoteVo.getProjectId()+"'");
		}
		
		if(tblNbsjAdvicenoteVo.getAdvicecoed()!=null && tblNbsjAdvicenoteVo.getAdvicecoed().length()>0) {
			sb.append(" AND TNA.ADVICECOED LIKE '%"+tblNbsjAdvicenoteVo.getAdvicecoed()+"%'");
		}
		
		if(tblNbsjAdvicenoteVo.getAdvicename()!=null && tblNbsjAdvicenoteVo.getAdvicename().length()>0) {
			sb.append(" AND TNA.ADVICENAME LIKE '%"+tblNbsjAdvicenoteVo.getAdvicename()+"%'");
		}
		
		if(tblNbsjAdvicenoteVo.getStartDate() !=null){
			sb.append(" AND TNA.CREATRTIME >= TO_DATE('"+tblNbsjAdvicenoteVo.getStartDate()+"','yyyy-MM-dd')");
		}
		
		if(tblNbsjAdvicenoteVo.getEndDate() !=null){
			sb.append(" AND TNA.CREATRTIME <= TO_DATE('"+tblNbsjAdvicenoteVo.getEndDate()+" 23:59:59','yyyy-mm-dd HH24:MI:SS')");
		}
		
		sb.append(" ORDER BY TNA.ADVICEID DESC) T1 WHERE ROWNUM <= "+(pageInfo.getCurrentRecord()+pageInfo.getPageSize())+" ) T2 WHERE T2.RN > "+pageInfo.getCurrentRecord());
		return sb.toString();
	}
	
	
	public String selectCountByPageInfo(PageInfo<TblNbsjAdvicenoteEntity> pageInfo,TblNbsjAdvicenoteVo tblNbsjAdvicenoteVo,TblStaffUtil loginStaff)throws Exception {
		TblNbsjAdvicenoteEntity plan = pageInfo.getCondition();
		StringBuffer sb = new StringBuffer("SELECT COUNT(0) "
				+ "FROM TBL_NBSJ_ADVICENOTE TNA "
//				+ "LEFT JOIN TBL_STAFF PRINCIPAL ON PRINCIPAL.STAFFID = TNA.PRINCIPALID "
//				+ "LEFT JOIN TBL_STAFF LEADER ON LEADER.STAFFID = TNA.LEADERID "
//				+ "LEFT JOIN TBL_ORGANIZATION AUDITORG ON AUDITORG.ORGID = TNA.AUDITORGID "
//				+ "LEFT JOIN TBL_STAFF CRESTAFF ON CRESTAFF.STAFFID = TNA.CREATESTAFFID "
				+ "WHERE 1=1 ");
		
		//安全保密SQL
		sb.append(GeneralSQLConcatConfig.concatSecrectSql(loginStaff.getCurrentOrg().getUseSecrect(), true, "TNA.ORGID", "TNA.ORGID",
				"TNA.CREATESTAFFID", "TNA.SECRECTLEVELID", "TNA.STAFFSCOPEIDS", loginStaff.getStaffid(), loginStaff.getDeptIds(), loginStaff.getSecrectScopeIds()));
		
		if(tblNbsjAdvicenoteVo.getProjectId()!=null) {
			sb.append(" AND TNA.PROGECTID = '"+tblNbsjAdvicenoteVo.getProjectId()+"'");
		}
		if(tblNbsjAdvicenoteVo.getAdvicecoed()!=null && tblNbsjAdvicenoteVo.getAdvicecoed().length()>0) {
			sb.append(" AND TNA.ADVICECOED LIKE '%"+tblNbsjAdvicenoteVo.getAdvicecoed()+"%'");
		}
		
		if(tblNbsjAdvicenoteVo.getAdvicename()!=null && tblNbsjAdvicenoteVo.getAdvicename().length()>0) {
			sb.append(" AND TNA.ADVICENAME LIKE '%"+tblNbsjAdvicenoteVo.getAdvicename()+"%'");
		}
		
		if(tblNbsjAdvicenoteVo.getStartDate() !=null){
			sb.append(" AND TNA.CREATRTIME >= TO_DATE('"+tblNbsjAdvicenoteVo.getStartDate()+"','yyyy-MM-dd')");
		}
		
		if(tblNbsjAdvicenoteVo.getEndDate() !=null){
			sb.append(" AND TNA.CREATRTIME <= TO_DATE('"+tblNbsjAdvicenoteVo.getEndDate()+" 23:59:59','yyyy-mm-dd HH24:MI:SS')");
		}
		
		return sb.toString();
	}
	
	public String updateEntity(TblNbsjAdvicenoteEntity plan) {
		StringBuffer sqlSb = new StringBuffer("UPDATE TBL_NBSJ_ADVICENOTE SET ADVICECOED='"+plan.getAdvicecoed()+"' ");
		if(plan.getAdvicename() != null && !"".equals(plan.getAdvicename())) {
			sqlSb.append(" ,ADVICENAME = '"+plan.getAdvicename()+"'");
		}
		if(plan.getContent() != null && !"".equals(plan.getContent())) {
			sqlSb.append(" ,CONTENT = '"+plan.getContent()+"'");
		}
		if(plan.getStatus()!=null) {
			sqlSb.append(" ,STATUS = "+plan.getStatus()+"");
		}
		sqlSb.append(" WHERE ADVICEID= "+plan.getAdviceid());
		return sqlSb.toString();
	}
	
	public String insertEntity(TblNbsjAdvicenoteEntity plan){
		StringBuffer colSb = new StringBuffer("INSERT INTO TBL_NBSJ_ADVICENOTE(ADVICEID,CREATRTIME,CREATESTAFFID,STATUS");
		StringBuffer valSb = new StringBuffer("  VALUES (HIBERNATE_SEQUENCE.nextval,TO_DATE('"+DateUtil.parseDate(plan.getCreatrtime(), "yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:mi:ss'),"+plan.getCreatestaffid()+",0");
		
		if(plan.getAdvicecoed() != null && !"".equals(plan.getAdvicecoed())) {
			colSb.append(",ADVICECOED");
			valSb.append(",'"+plan.getAdvicecoed()+"'");
		}
		
		if(plan.getAdvicename() != null && !"".equals(plan.getAdvicename())) {
			colSb.append(",ADVICENAME");
			valSb.append(",'"+plan.getAdvicename()+"'");
		}
		
		if(plan.getContent() != null && !"".equals(plan.getContent())) {
			colSb.append(",CONTENT");
			valSb.append(",'"+plan.getContent()+"'");
		}
		
		if(null != plan.getProject()) {
			if(plan.getProject().getProjectId() != null && !"".equals(plan.getProject().getProjectId())) {
				colSb.append(",PROGECTID");
				valSb.append(",'"+plan.getProject().getProjectId()+"'");
			}
		}
		
		
		String sql = colSb.toString()+")"+valSb.toString()+")";
		return sql;
	}
}
