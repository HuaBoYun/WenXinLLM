package com.huabo.audit.oracle.mapper;


import java.math.BigDecimal;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.hbfk.util.DateUtil;
import com.hbfk.util.PageInfo;
import com.huabo.audit.oracle.entity.StepResult;
import com.huabo.audit.oracle.entity.TblNbsjAuditStepEntity;

public class TblNbsjAuditStepMapperSqlConfig {
	
	public String selectListBySummary(TblNbsjAuditStepEntity step, String limitStr) throws Exception {
		StringBuffer sqlSb = new StringBuffer("SELECT STEPID,STEPNO,STEPTITLE,BOOKID FROM TBL_NBSJ_AUDITSTEP WHERE QYSTATUS = ").append(step.getQystatus()).append(" AND MPDELTYPE = '").append(step.getMpdeltype()).append("'");
		
		if(StringUtils.isNotBlank(limitStr)) {
			sqlSb.append(limitStr);
		}
		
		String sql = sqlSb.toString();
		return sql;
	}
	
	
	public String selectListPageInfo(PageInfo<Map<String,Object>> pageInfo,String sql) throws Exception {
		
		StringBuffer sb = new StringBuffer("SELECT * FROM (SELECT T1.*,ROWNUM RN  FROM ("+sql+") T1 WHERE ROWNUM <= "+(pageInfo.getCurrentRecord()+pageInfo.getPageSize())+" ) T2 WHERE T2.RN > "+pageInfo.getCurrentRecord());
		return sb.toString();
	}
	
	
	public String selectListPageInfocount(String sql) throws Exception {
		
		StringBuffer sb = new StringBuffer("SELECT count(*) FROM ("+sql+")   ");
		return sb.toString();
	}


	
	public String insertEntity(StepResult result){
		StringBuffer colSb = new StringBuffer("INSERT INTO TBL_NBSJ_SJMXRESULT (RESULTID, SAVETIME, MEMO, STAFFID, STEPID)");
		StringBuffer valSb = new StringBuffer("  VALUES (HIBERNATE_SEQUENCE.nextval,TO_DATE('"+DateUtil.parseDate(result.getSavetime(), "yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:mi:ss'),'"+result.getMemo()+"',"+result.getStaffid()+","+result.getStepid());
		if(result.getSource()!=null) {
			colSb.append(",SOURCE");
			valSb.append(","+result.getSource());
		}
		
		String sql = colSb.toString()+")"+valSb.toString()+")";
		return sql;
	}

	
	
	
	public String selectListxfPageInfo(PageInfo<TblNbsjAuditStepEntity> pageInfo,TblNbsjAuditStepEntity step,BigDecimal staffid) throws Exception {
		String sql="SELECT * from TBL_NBSJ_AUDITSTEP WHERE STEPID in (SELECT STEPID from TBL_NBSJ_AUDITSTEP_XFRY where STAFFID="+staffid+" ) and QYSTATUS=0";
		if(step.getStepno()!=null && step.getStepno().length()>0) {
			sql+=" and stepno like '%"+step.getStepno()+"%'";
		}
		
		
		if(step.getMpdeltype()!=null && step.getMpdeltype().length()>0) {
			sql+=" and MPDELTYPE = '"+step.getMpdeltype()+"'";
		}
		if(step.getSteptitle()!=null && step.getSteptitle().length()>0) {
			sql+=" and steptitle like '%"+step.getSteptitle()+"%'";
		}
		 
		
//		StringBuffer sb = new StringBuffer("SELECT * FROM (SELECT T1.*,ROWNUM RN  FROM ("+sql+") T1 WHERE ROWNUM <= "+(pageInfo.getCurrentRecord()+pageInfo.getPageSize())+" ) T2 WHERE T2.RN > "+pageInfo.getCurrentRecord());
		return sql;
	}
	
	
	public String selectListxfPageInfocount(TblNbsjAuditStepEntity step,BigDecimal staffid) throws Exception {
		String sql="SELECT * from TBL_NBSJ_AUDITSTEP WHERE STEPID in (SELECT STEPID from TBL_NBSJ_AUDITSTEP_XFRY where STAFFID="+staffid+" ) and QYSTATUS=0";
		if(step.getStepno()!=null && step.getStepno().length()>0) {
			sql+=" and stepno like '%"+step.getStepno()+"%'";
		}
		
		if(step.getSteptitle()!=null && step.getSteptitle().length()>0) {
			sql+=" and steptitle like '%"+step.getSteptitle()+"%'";
		}
		
		if(step.getMpdeltype()!=null && step.getMpdeltype().length()>0) {
			sql+=" and MPDELTYPE = '"+step.getMpdeltype()+"'";
		}
		StringBuffer sb = new StringBuffer("SELECT count(*) FROM ("+sql+")   ");
		return sb.toString();
	}

	
	
	public String selectListallPageInfo(BigDecimal typeid,TblNbsjAuditStepEntity step) throws Exception {
		String sql="SELECT * from TBL_NBSJ_AUDITSTEP WHERE 1=1 ";
		if(typeid!=null ) {
			sql+=" and TYPEID= "+typeid;
		}
		if(step.getStepno()!=null && step.getStepno().length()>0) {
			sql+=" and stepno like '%"+step.getStepno()+"%'";
		}
		
		if(step.getMpdeltype()!=null && step.getMpdeltype().length()>0) {
			sql+=" and MPDELTYPE = '"+step.getMpdeltype()+"'";
		}
		
		if(step.getSteptitle()!=null && step.getSteptitle().length()>0) {
			sql+=" and steptitle like '%"+step.getSteptitle()+"%'";
		}
		
		
		return sql;
	}
	
	
	
	
}
