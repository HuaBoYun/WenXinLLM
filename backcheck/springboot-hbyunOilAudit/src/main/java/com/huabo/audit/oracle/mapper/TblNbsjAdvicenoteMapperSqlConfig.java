package com.huabo.audit.oracle.mapper;

import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;

import com.hbfk.util.DateUtil;
import com.huabo.audit.oracle.entity.TblNbsjAdvicenoteEntity;
import com.huabo.audit.oracle.entity.TblYqnsAdviceAprEntity;
import com.huabo.audit.oracle.entity.TblYqnsAdvicenoteEntity;
import com.huabo.audit.oracle.vo.TblNbsjAdvicenoteVo;
import com.huabo.audit.util.PageInfo;

public class TblNbsjAdvicenoteMapperSqlConfig {
	public String selectPlanCodeByOrgid(TblYqnsAdvicenoteEntity plan) {
		StringBuffer sb = new StringBuffer("SELECT COUNT(*) FROM TBL_YQNS_ADVICENOTE WHERE 1=1 AND ADVICECOED='"+plan.getAdvicecoed()+"' ");
		if(plan.getAdviceid() != null) {
			sb.append(" AND ADVICEID != "+plan.getAdviceid());
		}
		return sb.toString();
	}
	
	public String selectListByPageInfo(PageInfo<TblYqnsAdvicenoteEntity> pageInfo, TblNbsjAdvicenoteVo tblNbsjAdvicenoteVo,BigDecimal staffid,String ids) {
		StringBuffer sb = new StringBuffer("SELECT TNA.*,CRESTAFF.REALNAME "
				+ "FROM TBL_YQNS_ADVICENOTE TNA "
				+ "LEFT JOIN TBL_STAFF CRESTAFF ON CRESTAFF.STAFFID = TNA.CREATESTAFFID "
				+ "WHERE 1=1 ");
		
		if(tblNbsjAdvicenoteVo.getProjectId()!=null) { 
			sb.append(" AND TNA.PROGECTID = '"+tblNbsjAdvicenoteVo.getProjectId()+"'");
		}
		
		if(tblNbsjAdvicenoteVo.getAdvicecoed()!=null && tblNbsjAdvicenoteVo.getAdvicecoed().length()>0) {
			sb.append(" AND TNA.ADVICECOED LIKE '%"+tblNbsjAdvicenoteVo.getAdvicecoed()+"%'");
		}
		
		if(tblNbsjAdvicenoteVo.getAdvicename()!=null && tblNbsjAdvicenoteVo.getAdvicename().length()>0) {
			sb.append(" AND TNA.ADVICENAME LIKE '%"+tblNbsjAdvicenoteVo.getAdvicename()+"%'");
		}
		
		if(tblNbsjAdvicenoteVo.getStartDate() !=null&&"".equals(tblNbsjAdvicenoteVo.getStartDate())==false){
			sb.append(" AND TNA.CREATRTIME >= '" + tblNbsjAdvicenoteVo.getStartDate()+"'");
		}
		
		if(tblNbsjAdvicenoteVo.getEndDate() !=null&&"".equals(tblNbsjAdvicenoteVo.getEndDate())==false){
			sb.append(" AND TNA.CREATRTIME <= '" + tblNbsjAdvicenoteVo.getEndDate() + " 23:59:59'");
		}
		if (StringUtils.isNotBlank(ids)) {
			 sb.append(" and ( TNA.CREATESTAFFID="+staffid+"  or TNA.CREATESTAFFID in (SELECT DISTINCT STAFFID FROM TBL_USER_ORGRELATION WHERE DEPTID IN ("+ids+")  ))");
	     }else {
	        sb.append(" and (TNA.CREATESTAFFID="+staffid +" or ZSSTAFFID="+staffid+" )");
	     }
		
		sb.append(" ORDER BY TNA.ADVICEID DESC");
		return sb.toString();
	}
	
	
	public String selectCountByPageInfo(PageInfo<TblNbsjAdvicenoteEntity> pageInfo,TblNbsjAdvicenoteVo tblNbsjAdvicenoteVo) {
		TblNbsjAdvicenoteEntity plan = pageInfo.getCondition();
		StringBuffer sb = new StringBuffer("SELECT COUNT(0) "
				+ "FROM TBL_NBSJ_ADVICENOTE TNA "
//				+ "LEFT JOIN TBL_STAFF PRINCIPAL ON PRINCIPAL.STAFFID = TNA.PRINCIPALID "
//				+ "LEFT JOIN TBL_STAFF LEADER ON LEADER.STAFFID = TNA.LEADERID "
//				+ "LEFT JOIN TBL_ORGANIZATION AUDITORG ON AUDITORG.ORGID = TNA.AUDITORGID "
//				+ "LEFT JOIN TBL_STAFF CRESTAFF ON CRESTAFF.STAFFID = TNA.CREATESTAFFID "
				+ "WHERE 1=1 ");
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
	
	
	public String selectAprListByPageInfo(PageInfo<TblYqnsAdviceAprEntity> pageInfo, String advicename,BigDecimal staffid,String ids,String xctype ) {
		StringBuffer sb = new StringBuffer("SELECT TNA.*,CRESTAFF.REALNAME,PL.PROJECT_NAME projectname  "
				+ "FROM TBL_YQNS_ADVICEAPR TNA "
				+ "LEFT JOIN TBL_STAFF CRESTAFF ON CRESTAFF.STAFFID = TNA.CREATESTAFFID "
				+ "LEFT JOIN TBL_YQNS_IMPLEMENT_PLAN pl on TNA.PROGECTID=pl.ID "
				+ "WHERE 1=1 ");
		
//		if(advicename!=null && advicename.length()>0) {
//			sb.append(" AND TNA.advicename LIKE '%"+advicename+"%'");
//		}
		
		if(advicename!=null && advicename.length()>0) {
			sb.append(" AND pl.PROJECT_NAME LIKE '%"+advicename+"%'");
		}
		
		if (StringUtils.isNotBlank(xctype) && xctype.equals("sjtzs") ) {
			sb.append(" and   TNA.adviceid  NOT IN (select ADVICEAPRID from TBL_YQNS_ADVICENOTE where ADVICEAPRID is not NULL)  ");
         }
		
		 if (StringUtils.isNotBlank(ids)) {
			 sb.append(" and ( TNA.CREATESTAFFID="+staffid+"  or TNA.CREATESTAFFID in (SELECT DISTINCT STAFFID FROM TBL_USER_ORGRELATION WHERE DEPTID IN ("+ids+") ) )");
	     }else {
	        sb.append(" and TNA.CREATESTAFFID="+staffid );
	     }
		
		sb.append(" ORDER BY TNA.ADVICEID DESC");
		return sb.toString();
	}
	
	
	

	public String selectAprListByspPageInfo(PageInfo<TblYqnsAdviceAprEntity> pageInfo, String advicename,BigDecimal staffid,String ids ) {
		StringBuffer sb = new StringBuffer("SELECT TNA.*,CRESTAFF.REALNAME,PL.PROJECT_NAME projectname,ORG.ORGNAME "
				+ "FROM TBL_YQNS_ADVICEAPR TNA "
				+ "LEFT JOIN TBL_STAFF CRESTAFF ON CRESTAFF.STAFFID = TNA.CREATESTAFFID "
				+ "LEFT JOIN TBL_YQNS_IMPLEMENT_PLAN pl on TNA.PROGECTID=pl.ID "
				+ "LEFT JOIN TBL_ORGANIZATION org on TNA.ORGIDS=ORG.ORGID "
				+ "WHERE 1=1 and TNA.status=6 and  TNA.adviceid not in (select adviceaprid from TBL_YQNS_ADVICENOTE)  ");
		
		if(advicename!=null && advicename.length()>0) {
			sb.append(" AND TNA.advicename LIKE '%"+advicename+"%'");
		} 
		 if (StringUtils.isNotBlank(ids)) {
			 sb.append(" and ( TNA.CREATESTAFFID="+staffid+"  or TNA.CREATESTAFFID in (SELECT DISTINCT STAFFID FROM TBL_USER_ORGRELATION WHERE DEPTID IN ("+ids+") ) )");
	     }else {
	        sb.append(" and TNA.CREATESTAFFID="+staffid );
	     }
		
		sb.append(" ORDER BY TNA.ADVICEID DESC");
		return sb.toString();
	}
	
	
}
