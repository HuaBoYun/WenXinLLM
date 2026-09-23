package com.huabo.audit.oracle.mapper;

import com.hbfk.util.DateUtil;
import com.hbfk.util.PageInfo;
import com.huabo.audit.oracle.entity.TblAduitProGramEntity;
import com.huabo.audit.vo.result.QualityParam;

public class TblAduitProGramMapperSqlConfig {
	
	
	
	public String findBytjsj(QualityParam param) {
		StringBuffer sb = new StringBuffer("SELECT bb.pc,bb.ssdw,bb.auditorgname,bb.projectname,bb.ksfzr,bb.xmzcy,bb.xcsrarttime,bb.xcendtime,bb.sj, (REGEXP_COUNT(bb.xmzcy,',')+1) * bb.sj trzy,"
	    		+ " CASE WHEN bb.dgzs=0 then 0 else (REGEXP_COUNT(bb.xmzcy,',')+1) * bb.sj/bb.dgzs END  gxb,bb.fhdg,bb.wtdg from (  "
	    		+ " SELECT aa.pc,aa.ssdw,aa.auditorgname,aa.projectname,aa.ksfzr,aa.xmzcy,aa.xcsrarttime,aa.xcendtime,CASE WHEN aa.xcsrarttime is null then 0 ELSE  aa.sj end sj,"
	    		+ " aa.dgzs,aa.fhdg,aa.wtdg from (select DISTINCT CASE WHEN PL.ZYKSTYPE='基建' THEN tb.BATC ELSE tbf.BATC END pc,"
	    		+ " CASE WHEN PL.ZYKSTYPE='基建' THEN EP.exePhraseUnit ELSE fp.exePhraseUnit END ssdw,PL.AUDIT_ORG_NAME auditorgname,PL.PROJECT_NAME projectname,FP.APPROVER,FP.ASSISTAPPROVER,FP.XCSRARTTIME,FP.XCENDTIME, "
	    		+ " CASE WHEN PL.ZYKSTYPE='基建' THEN EP.RSYQ ELSE fp.RSYQ END RSYQ, CASE WHEN PL.ZYKSTYPE='基建' THEN round(EP.XCENDTIME -  EP.XCSRARTTIME) ELSE round(fp.XCENDTIME -  fp.XCSRARTTIME) END  sj,"
	    		+ " CASE WHEN PL.ZYKSTYPE='基建' THEN EP.XFKSRYNAMES ELSE fp.XFKSRYNAMES END ksfzr, CASE WHEN PL.ZYKSTYPE='基建'   THEN ep.approver||','||ep.assistApprover ELSE fp.approver||','||fp.assistApprover END xmzcy,"
	    		+ " (SELECT count(*) from TBL_YQNS_AUDIT_MY_MANUSCRIPT dg where DG.PROJECTID=PL.ID) dgzs, "
	    		+ " (SELECT count(*) from TBL_YQNS_AUDIT_MY_MANUSCRIPT dg where DG.PROJECTID=PL.ID and DG.PROBLEMDRAFT=0) fhdg, "
	    		+ " (SELECT count(*) from TBL_YQNS_AUDIT_MY_MANUSCRIPT dg where DG.PROJECTID=PL.ID and DG.PROBLEMDRAFT=1) wtdg"
	    		+ " from TBL_YQNS_IMPLEMENT_PLAN pl  LEFT JOIN TBL_YQNS_FUND_AUDIT_PROJECT fp ON PL.XMAPBID=fp.ID  "
	    		+ " LEFT JOIN TBL_YQNS_FUNDTB_GL fgl on fp.ID =fgl.FUNID LEFT JOIN TBL_YQNS_ENGINTB tbf on fgl.TBID=tbf.TBID "
	    		+ " LEFT JOIN TBL_YQNS_ENGIN_AUDIT_PROJECT ep on PL.XMAPBID=EP.ID  LEFT JOIN TBL_YQNS_ENGINTB_GL gl on EP.ID =gl.ENGID "
	    		+ " LEFT JOIN TBL_YQNS_ENGINTB tb on gl.TBID=tb.TBID )  aa ) bb where 1=1 ");
		if(param.getPc()!=null && param.getPc().length()>0) {
			sb.append("  AND bb.pc LIKE '%"+param.getPc()+"%'");
		}
		
		
		if(param.getProjectname()!=null && param.getProjectname().length()>0) {
			sb.append(" AND bb.projectname LIKE '%"+param.getProjectname()+"%'");
		}
		
		if(param.getAuditorgname()!=null && param.getAuditorgname().length()>0) {
			sb.append(" AND bb.auditorgname LIKE '%"+param.getAuditorgname()+"%'");
		}
		
		return sb.toString();
	}
	
	
	public String selectPlanCodeByOrgid(TblAduitProGramEntity plan) {
		StringBuffer sb = new StringBuffer("SELECT COUNT(*) FROM TBL_NBSJ_AUDITPROGRAM WHERE 1=1 ");
//		if(plan.getPlanid() != null) {
//			sb.append(" AND PLANID != "+plan.getPlanid());
//		}
		return sb.toString();
	}
	
	public String selectListByPageInfo(PageInfo<TblAduitProGramEntity> pageInfo,Integer projectId,Integer tempId,Integer targetId) {
		StringBuffer sb = new StringBuffer("SELECT * FROM "
				+ "(SELECT T1.*,ROWNUM RN  FROM "
				+ "(SELECT TNA.* "
				+ "FROM TBL_NBSJ_AUDITPROGRAM TNA "
				+ "LEFT JOIN TBL_NBSJ_TEMPLETE ntemp ON ntemp.templeteId = TNA.TEMPID "
				+ "WHERE 1=1 ");
		
		if(null != targetId) {
			sb.append(" AND TNA.TARGETID =  "+targetId);
		}else {
			sb.append(" AND ntemp.templeteId =  "+tempId);
		}
		
		sb.append(" ORDER BY TNA.TARGETID,TNA.TEMPID ) T1 WHERE ROWNUM <= "+(pageInfo.getCurrentRecord()+pageInfo.getPageSize())+" ) T2 WHERE T2.RN > "+pageInfo.getCurrentRecord());
		return sb.toString();
	}
	
	
	public String selectCountByPageInfo(PageInfo<TblAduitProGramEntity> pageInfo,Integer projectId,Integer tempId,Integer targetId) {
		StringBuffer sb = new StringBuffer("SELECT COUNT(0) "
				+ "FROM TBL_NBSJ_AUDITPROGRAM TNA "
//				+ "LEFT JOIN TBL_STAFF PRINCIPAL ON PRINCIPAL.STAFFID = TNA.PRINCIPALID "
//				+ "LEFT JOIN TBL_STAFF LEADER ON LEADER.STAFFID = TNA.LEADERID "
//				+ "LEFT JOIN TBL_ORGANIZATION AUDITORG ON AUDITORG.ORGID = TNA.AUDITORGID "
//				+ "LEFT JOIN TBL_STAFF CRESTAFF ON CRESTAFF.STAFFID = TNA.CREATESTAFFID "
				+ "WHERE 1=1 ");
		
		if(null != targetId) {
			sb.append(" AND TNA.TARGETID =  "+targetId);
		}else {
			sb.append(" AND TNA.TEMPID =  "+tempId);
		}
		
		return sb.toString();
	}
	
	public String updateEntity(TblAduitProGramEntity plan) {
		StringBuffer sqlSb = new StringBuffer("UPDATE TBL_NBSJ_AUDITPROGRAM SET UPDATETIME=TO_DATE('"+DateUtil.parseDate(plan.getUpdateTime(), "yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:mi:ss') ");
		if(plan.getBusinessType() != null && !"".equals(plan.getBusinessType())) {
			sqlSb.append(" ,BUSINESSTYPE = '"+plan.getBusinessType()+"'");
		}
		if(plan.getRiskSource() != null && !"".equals(plan.getRiskSource())) {
			sqlSb.append(" ,RISKSOURCE = '"+plan.getRiskSource()+"'");
		}
		if(plan.getRiskPoint() != null && !"".equals(plan.getRiskPoint())) {
			sqlSb.append(" ,RISKPOINT = '"+plan.getRiskPoint()+"'");
		}
		if(plan.getControl() != null && !"".equals(plan.getControl())) {
			sqlSb.append(" ,CONTROL = '"+plan.getControl()+"'");
		}
		if(plan.getSuditProcess() != null && !"".equals(plan.getSuditProcess())) {
			sqlSb.append(" ,SUDITPROCESS = '"+plan.getSuditProcess()+"'");
		}
		if(plan.getBioData() != null && !"".equals(plan.getBioData())) {
			sqlSb.append(" ,BIODATA = '"+plan.getBioData()+"'");
		}
		
		
//		if(plan.getTargetId() != null && !"".equals(plan.getTargetId())) {
//			sqlSb.append(" ,TEMPID = '"+plan.getTargetId()+"'");
//		}
//		if(plan.getTempId() != null && !"".equals(plan.getTempId())) {
//			sqlSb.append(" ,TARGETID = '"+plan.getTempId()+"'");
//		}
		
		sqlSb.append(" WHERE PROGRAMID= "+plan.getProgramId());
		return sqlSb.toString();
	}
	
	public String insertEntity(TblAduitProGramEntity plan){
		StringBuffer colSb = new StringBuffer("INSERT INTO TBL_NBSJ_AUDITPROGRAM(PROGRAMID,CREATETIME,STATUS");
		StringBuffer valSb = new StringBuffer("  VALUES (HIBERNATE_SEQUENCE.nextval,TO_DATE('"+DateUtil.parseDate(plan.getCreateTime(), "yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:mi:ss'),0");
		if(plan!=null && plan.getProgramId()!=null) {
			valSb = new StringBuffer("  VALUES ("+plan.getProgramId()+",TO_DATE('"+DateUtil.parseDate(plan.getCreateTime(), "yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:mi:ss'),0");
		}
		
		if(plan.getBusinessType() != null && !"".equals(plan.getBusinessType())) {
			colSb.append(",BUSINESSTYPE");
			valSb.append(",'"+plan.getBusinessType()+"'");
		}
		if(plan.getRiskSource() != null && !"".equals(plan.getRiskSource())) {
			colSb.append(",RISKSOURCE");
			valSb.append(",'"+plan.getRiskSource()+"'");
		}
		if(plan.getRiskPoint() != null && !"".equals(plan.getRiskPoint())) {
			colSb.append(",RISKPOINT");
			valSb.append(",'"+plan.getRiskPoint()+"'");
		}
		if(plan.getControl() != null && !"".equals(plan.getControl())) {
			colSb.append(",CONTROL");
			valSb.append(",'"+plan.getControl()+"'");
		}
		if(plan.getSuditProcess() != null && !"".equals(plan.getSuditProcess())) {
			colSb.append(",SUDITPROCESS");
			valSb.append(",'"+plan.getSuditProcess()+"'");
		}
		if(plan.getBioData() != null && !"".equals(plan.getBioData())) {
			colSb.append(",BIODATA");
			valSb.append(",'"+plan.getBioData()+"'");
		}
		
		if(plan.getTargetId() != null && !"".equals(plan.getTargetId())) {
			colSb.append(",TARGETID");
			valSb.append(",'"+plan.getTargetId()+"'");
		}
		if(plan.getTempId() != null && !"".equals(plan.getTempId())) {
			colSb.append(",TEMPID");
			valSb.append(",'"+plan.getTempId()+"'");
		}
		
		String sql = colSb.toString()+")"+valSb.toString()+")";
		return sql;
	}
}
