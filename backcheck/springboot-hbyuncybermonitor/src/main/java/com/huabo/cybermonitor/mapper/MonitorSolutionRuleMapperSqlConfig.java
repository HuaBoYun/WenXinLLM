package com.huabo.cybermonitor.mapper;

import java.util.Map;

import com.hbfk.util.PageInfo;

public class MonitorSolutionRuleMapperSqlConfig {

	
	public String selectListPageInfo(PageInfo<Map<String,Object>> pageInfo,String sql) throws Exception {
		
		StringBuffer sb = new StringBuffer("SELECT * FROM (SELECT T1.*,ROWNUM RN  FROM ("+sql+") T1 WHERE ROWNUM <= "+(pageInfo.getCurrentRecord()+pageInfo.getPageSize())+" ) T2 WHERE T2.RN > "+pageInfo.getCurrentRecord());
		return sb.toString();
	}
	
	
	public String selectListPageInfocount(String sql) throws Exception {
		
		StringBuffer sb = new StringBuffer("SELECT count(*) FROM ("+sql+")   ");
		return sb.toString();
	}
}
