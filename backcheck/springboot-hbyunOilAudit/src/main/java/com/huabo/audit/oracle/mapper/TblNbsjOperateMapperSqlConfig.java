package com.huabo.audit.oracle.mapper;

import java.math.BigDecimal;

import com.hbfk.util.DateUtil;
import com.huabo.audit.oracle.entity.TblAduitProGramEntity;
import com.huabo.audit.oracle.entity.TblNbsjOperateEntity;
import com.huabo.audit.oracle.vo.TblNbsjOperateVo;
import com.huabo.audit.util.PageInfo;

public class TblNbsjOperateMapperSqlConfig {
	
	public String insertEntity(TblNbsjOperateEntity nbsjOperate) {
		String colSql = "INSERT INTO TBL_NBSJ_OPERATE(OPERATEID";
		String valSql = " VALUES (HIBERNATE_SEQUENCE.nextval";
	
		if(nbsjOperate.getAuthId() != null) {
			colSql += ",AUTHID";
			valSql += ", '"+nbsjOperate.getAuthId()+"'";
		}
		if(nbsjOperate.getFinish() != null) {
			colSql += ",FINISH";
			valSql += ", '"+nbsjOperate.getFinish()+"'";
		}
		if(nbsjOperate.getSheetId() != null) {
			colSql += ",SHEETID";
			valSql += ", '"+nbsjOperate.getSheetId()+"'";
		}
		if(nbsjOperate.getFinishtime() != null) {
			colSql += ",FINISHTIME";
			valSql += ",TO_DATE('"+DateUtil.parseDate(nbsjOperate.getFinishtime(), "yyyy-MM-dd HH:mm:ss") +"', 'YYYY-MM-DD HH24:MI:SS')";
		}
		String sql = colSql+") "+valSql+")";
		return sql;
	}
	
	
	
	public String selectPlanCodeByOrgid(TblNbsjOperateEntity plan) {
		StringBuffer sb = new StringBuffer("SELECT COUNT(*) FROM TBL_NBSJ_OPERATE WHERE 1=1 ");
//		if(plan.getPlanid() != null) {
//			sb.append(" AND PLANID != "+plan.getPlanid());
//		}
		return sb.toString();
	}
	
	public String selectListByPageInfo(PageInfo<TblNbsjOperateEntity> pageInfo,TblNbsjOperateVo tblNbsjOperateVo) {
		
		StringBuffer sb = new StringBuffer("SELECT * FROM "
				+ "(SELECT T1.*,ROWNUM RN  FROM "
				+ "(SELECT * "
				+ "FROM TBL_NBSJ_OPERATE TNA "
				+ "LEFT JOIN TBL_NBSJ_AUTHORIZATION auth ON TNA. AUTHID = auth. AUTHID "
				+ "LEFT JOIN TBL_NBSJ_AUDITPROGRAM audi ON audi.programid = auth.programid "
				+ "LEFT JOIN TBL_NBSJ_TARGETTYPE target ON target.targetId = audi.targetId "
//				+ "LEFT JOIN TBL_NBSJ_TEAMSTAFF team on team.id = auth.TEAMSTAFFID "
//				+ "where  target.targetId  = "+targetId+"  and  auth.projectid = "+projectId+" and  team.staffid = "+staffId
				+ "WHERE 1=1 ");
		
		if(tblNbsjOperateVo.getBusinessType()!=null && tblNbsjOperateVo.getBusinessType().length()>0) {
			sb.append(" AND audi.businessType LIKE '%"+tblNbsjOperateVo.getBusinessType()+"%'");
		}
		if(tblNbsjOperateVo.getProjectId()!=null) {
			sb.append(" AND auth.projectid = "+tblNbsjOperateVo.getProjectId());
		}
		if(tblNbsjOperateVo.getStaffid()!=null && tblNbsjOperateVo.getStatus()!=null && tblNbsjOperateVo.getStatus()==1) {
			sb.append(" AND auth.TEAMSTAFFID = "+tblNbsjOperateVo.getStaffid());
		}
		if(tblNbsjOperateVo.getTargetId()!=null) {
			sb.append(" AND target.targetId = "+tblNbsjOperateVo.getTargetId());
		}
		
		sb.append(" ORDER BY TNA.OPERATEID DESC) T1 WHERE ROWNUM <= "+(pageInfo.getCurrentRecord()+pageInfo.getPageSize())+" ) T2 WHERE T2.RN > "+pageInfo.getCurrentRecord());
		System.out.println(sb.toString());
		return sb.toString();
	}
	
	public String selectCountByPageInfo(PageInfo<TblNbsjOperateEntity> pageInfo,TblNbsjOperateVo tblNbsjOperateVo) {
//		TblNbsjEntermeetingVo plan = pageInfo.getCondition();
		
		StringBuffer sb = new StringBuffer("SELECT COUNT(0) "
				+ "FROM TBL_NBSJ_OPERATE TNA "
				+ "LEFT JOIN TBL_NBSJ_AUTHORIZATION auth ON TNA. AUTHID = auth. AUTHID "
				+ "LEFT JOIN TBL_NBSJ_AUDITPROGRAM audi ON audi.programid = auth.programid "
				+ "LEFT JOIN TBL_NBSJ_TARGETTYPE target ON target.targetId = audi.targetId "
//				+ "LEFT JOIN TBL_NBSJ_TEAMSTAFF team on team.id = auth.TEAMSTAFFID "
				+ "WHERE 1=1 ");
		
		if(tblNbsjOperateVo.getBusinessType()!=null && tblNbsjOperateVo.getBusinessType().length()>0) {
			sb.append(" AND audi.businessType LIKE '%"+tblNbsjOperateVo.getBusinessType()+"%'");
		}
		if(tblNbsjOperateVo.getProjectId()!=null) {
			sb.append(" AND auth.projectid = "+tblNbsjOperateVo.getProjectId());
		}
		if(tblNbsjOperateVo.getStaffid()!=null && tblNbsjOperateVo.getStatus()!=null && tblNbsjOperateVo.getStatus()==1) {
			sb.append(" AND auth.TEAMSTAFFID = "+tblNbsjOperateVo.getStaffid());
		}
		if(tblNbsjOperateVo.getTargetId()!=null) {
			sb.append(" AND target.targetId = "+tblNbsjOperateVo.getTargetId());
		}
		return sb.toString();
	}
	
	
	//==
	public String selectAllListByPageInfo(PageInfo<TblNbsjOperateEntity> pageInfo,Integer projectid,String businessType,Integer targetId) {
		
		StringBuffer sb = new StringBuffer("SELECT * "
				+ "FROM TBL_NBSJ_OPERATE TNA "
				+ " LEFT JOIN TBL_NBSJ_AUTHORIZATION auth ON TNA. AUTHID = auth. AUTHID "
				+ " LEFT JOIN TBL_NBSJ_AUDITPROGRAM audi ON audi.programid = auth.programid "
				+ " LEFT JOIN TBL_NBSJ_TARGETTYPE target ON target.targetId = audi.targetId "
				+ " LEFT JOIN TBL_NBSJ_TEAMSTAFF team on team.id = auth.TEAMSTAFFID "
				+ " WHERE auth.projectid = "+projectid);
		
		if(null != targetId) {
			sb.append(" AND target.targetId = "+targetId);
		}
		
		if(businessType != null) {
			sb.append(" AND audi.BUSINESSTYPE LIKE '%"+businessType+"%'");
		}
		
		sb.append(" ORDER BY TNA.OPERATEID DESC");
		return sb.toString();
	}
	public String selectAllCountByPageInfo(PageInfo<TblNbsjOperateEntity> pageInfo,Integer projectid,String businessType,Integer targetId) {
//		TblNbsjEntermeetingVo plan = pageInfo.getCondition();
		
		StringBuffer sb = new StringBuffer("SELECT COUNT(0) "
				+ "FROM TBL_NBSJ_OPERATE TNA "
				+ " LEFT JOIN TBL_NBSJ_AUTHORIZATION auth ON TNA. AUTHID = auth. AUTHID "
				+ " LEFT JOIN TBL_NBSJ_AUDITPROGRAM audi ON audi.programid = auth.programid "
				+ " LEFT JOIN TBL_NBSJ_TARGETTYPE target ON target.targetId = audi.targetId "
				+ " LEFT JOIN TBL_NBSJ_TEAMSTAFF team on team.id = auth.TEAMSTAFFID "
				+ " WHERE auth.projectid = "+projectid);
		
		if(null != targetId) {
			sb.append(" AND target.targetId = "+targetId);
		}
		
		if(businessType != null) {
			sb.append(" AND audi.BUSINESSTYPE LIKE '%"+businessType+"%'");
		}
		
		return sb.toString();
	}
	
	
	
	
	
	
	
	//==
	public String selectRWFPListByPageInfo(PageInfo<TblNbsjOperateEntity> pageInfo,BigDecimal tempId,BigDecimal targetId) {
//		
//		StringBuffer sb = new StringBuffer("SELECT * FROM "
//				+ "(SELECT T1.*,ROWNUM RN  FROM "
//				+ "(SELECT * "
////				+ " FROM TBL_NBSJ_OPERATE TNA "
////				+ " LEFT JOIN TBL_NBSJ_AUTHORIZATION auth ON TNA. AUTHID = auth. AUTHID "
////				+ " LEFT JOIN TBL_NBSJ_AUDITPROGRAM audi ON audi.programid = auth.programid "
////				+ " LEFT JOIN TBL_NBSJ_TARGETTYPE target ON target.targetId = audi.targetId "
////				+ " LEFT JOIN TBL_NBSJ_TEAMSTAFF team on team.id = auth.TEAMSTAFFID "
////				+ " LEFT JOIN TBL_STAFF staff on staff.staffid = team.staffid "
//				+ "FROM TBL_NBSJ_AUDITPROGRAM TNA "
//				+ "LEFT JOIN TBL_NBSJ_TEMPLETE ntemp ON ntemp.templeteId = TNA.TEMPID "
//				
//				+ " LEFT JOIN TBL_NBSJ_AUTHORIZATION auth ON TNA. programid = auth.programid "
//				+ " LEFT JOIN TBL_NBSJ_OPERATE op ON op. AUTHID = auth. AUTHID "
////				+ " LEFT JOIN TBL_NBSJ_TEAMSTAFF team on team.id = auth.TEAMSTAFFID "
//				+ " LEFT JOIN TBL_STAFF staff on staff.staffid = auth.TEAMSTAFFID "
//				
//				+ " WHERE 1=1 ");
//		if(null != targetId) {
//			sb.append(" AND TNA.TARGETID =  "+targetId);
//		}else {
//			sb.append(" AND ntemp.templeteId =  "+tempId);
//		}
//		
//		sb.append(" ORDER BY TNA.programid ) T1 WHERE ROWNUM <= "+(pageInfo.getCurrentRecord()+pageInfo.getPageSize())+" ) T2 WHERE T2.RN > "+pageInfo.getCurrentRecord());
//		
//		
		StringBuffer sb = new StringBuffer("SELECT *  "
//				+ "(SELECT T1.*,ROWNUM RN  FROM "
//				+ "(SELECT * "
//				+ " FROM TBL_NBSJ_OPERATE TNA "
//				+ " LEFT JOIN TBL_NBSJ_AUTHORIZATION auth ON TNA. AUTHID = auth. AUTHID "
//				+ " LEFT JOIN TBL_NBSJ_AUDITPROGRAM audi ON audi.programid = auth.programid "
//				+ " LEFT JOIN TBL_NBSJ_TARGETTYPE target ON target.targetId = audi.targetId "
//				+ " LEFT JOIN TBL_NBSJ_TEAMSTAFF team on team.id = auth.TEAMSTAFFID "
//				+ " LEFT JOIN TBL_STAFF staff on staff.staffid = team.staffid "
				+ "FROM TBL_NBSJ_AUDITPROGRAM TNA "
				+ "LEFT JOIN TBL_NBSJ_TEMPLETE ntemp ON ntemp.templeteId = TNA.TEMPID "
				
				+ " LEFT JOIN TBL_NBSJ_AUTHORIZATION auth ON TNA. programid = auth.programid "
				+ " LEFT JOIN TBL_NBSJ_OPERATE op ON op. AUTHID = auth. AUTHID "
//				+ " LEFT JOIN TBL_NBSJ_TEAMSTAFF team on team.id = auth.TEAMSTAFFID "
				+ " LEFT JOIN TBL_STAFF staff on staff.staffid = auth.TEAMSTAFFID "
				
				+ " WHERE 1=1 ");
		if(null != targetId) {
			sb.append(" AND TNA.TARGETID =  "+targetId);
		}else {
			sb.append(" AND ntemp.templeteId =  "+tempId);
		}
		
//		sb.append(" ORDER BY TNA.programid ) T1 WHERE ROWNUM <= "+(pageInfo.getCurrentRecord()+pageInfo.getPageSize())+" ) T2 WHERE T2.RN > "+pageInfo.getCurrentRecord());
		
		
		sb.append(" ORDER BY TNA.programid ");
		return sb.toString();
	}
	public String selectRWFPCountByPageInfo(PageInfo<TblNbsjOperateEntity> pageInfo,Integer tempId,Integer targetId) {
		StringBuffer sb = new StringBuffer("SELECT COUNT(0) "
//				+ "FROM TBL_NBSJ_OPERATE TNA "
//				+ " LEFT JOIN TBL_NBSJ_AUTHORIZATION auth ON TNA. AUTHID = auth. AUTHID "
//				+ " LEFT JOIN TBL_NBSJ_AUDITPROGRAM audi ON audi.programid = auth.programid "
//				+ " LEFT JOIN TBL_NBSJ_TARGETTYPE target ON target.targetId = audi.targetId "
//				+ " LEFT JOIN TBL_NBSJ_TEAMSTAFF team on team.id = auth.TEAMSTAFFID "
				+ "FROM TBL_NBSJ_AUDITPROGRAM TNA "
				+ "LEFT JOIN TBL_NBSJ_TEMPLETE ntemp ON ntemp.templeteId = TNA.TEMPID "
				+ " WHERE 1=1 ");
		
		if(null != targetId) {
			sb.append(" AND TNA.TARGETID =  "+targetId);
		}else {
			sb.append(" AND ntemp.templeteId =  "+tempId);
		}
				
		return sb.toString();
	}
	
	
	
	public String selectRWFPListCon(PageInfo<TblAduitProGramEntity> pageInfo,Integer projectid,Integer targetId) {
		StringBuffer sb = new StringBuffer("SELECT * FROM "
				+ "(SELECT T1.*,ROWNUM RN  FROM "
				+ "(SELECT * "
				+ " FROM TBL_NBSJ_AUDITPROGRAM TNA "
				+ " LEFT JOIN TBL_NBSJ_TARGETTYPE target ON target.targetId = TNA.targetId "
				+ " WHERE 1=1 ");
		
		if(null != targetId) {
			sb.append(" AND target.targetId = "+targetId);
		}
		
		sb.append(" ORDER BY TNA.programId DESC) T1 WHERE ROWNUM <= "+(pageInfo.getCurrentRecord()+pageInfo.getPageSize())+" ) T2 WHERE T2.RN > "+pageInfo.getCurrentRecord());
		return sb.toString();
	}
	
	 
    public String findPorgramByUser(PageInfo<TblNbsjOperateEntity> pageInfo,Integer targetId,Integer projectId, BigDecimal staffid,
			String bsunitname) {
		StringBuffer sb = new StringBuffer("SELECT	op.*,audi.businessType,audi.riskSource,audi.suditProcess FROM	TBL_NBSJ_OPERATE op LEFT JOIN TBL_NBSJ_AUTHORIZATION auth ON op. AUTHID = auth. AUTHID LEFT JOIN TBL_NBSJ_AUDITPROGRAM audi ON audi.programid = auth.programid LEFT JOIN TBL_NBSJ_TARGETTYPE target ON target.targetId = audi.targetId LEFT JOIN TBL_NBSJ_TEAMSTAFF team on team.id = auth.TEAMSTAFFID where 1=1 ");
		if(targetId != null && !"".equals(targetId)){
			sb.append(" and target.targetId  = "+targetId);
		}else{
			sb.append("and finish !=1 "); //底稿管理业务单元关联
		}
		if(bsunitname != null && !"".equals(bsunitname)){
			sb.append(" and  audi.businessType LIKE '%"+bsunitname+"%'");
		}
		sb.append(" and  auth.projectid = "+projectId+" and  team.staffid = "+staffid);
		return sb.toString();
	}
	
    public String findCountPorgramByUser(PageInfo<TblNbsjOperateEntity> pageInfo,Integer targetId,Integer projectId, BigDecimal staffid,
			String bsunitname) {
		String tar="";
		StringBuffer sb = new StringBuffer("SELECT count(op.operateid) FROM	TBL_NBSJ_OPERATE op LEFT JOIN TBL_NBSJ_AUTHORIZATION auth ON op. AUTHID = auth. AUTHID LEFT JOIN TBL_NBSJ_AUDITPROGRAM audi ON audi.programid = auth.programid LEFT JOIN TBL_NBSJ_TARGETTYPE target ON target.targetId = audi.targetId LEFT JOIN TBL_NBSJ_TEAMSTAFF team on team.id = auth.TEAMSTAFFID where 1=1 ");
		if(targetId != null && !"".equals(targetId)){
			sb.append(" and target.targetId  = "+targetId);
		}else{
			sb.append(" and finish !=1 "); //底稿管理业务单元关联
		}
		if(bsunitname != null && !"".equals(bsunitname)){
			sb.append(" and  audi.businessType LIKE '%"+bsunitname+"%'");
		}
		sb.append(" and  auth.projectid = "+projectId+" and  team.staffid = "+staffid);
	   return sb.toString();
	}
}
