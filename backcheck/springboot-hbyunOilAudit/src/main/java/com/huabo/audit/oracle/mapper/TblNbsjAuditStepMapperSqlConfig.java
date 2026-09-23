package com.huabo.audit.oracle.mapper;


import java.util.Map;

import com.hbfk.util.DateUtil;
import com.hbfk.util.PageInfo;
import com.huabo.audit.oracle.entity.StepResult;

public class TblNbsjAuditStepMapperSqlConfig {
	
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

	
	
	
}
