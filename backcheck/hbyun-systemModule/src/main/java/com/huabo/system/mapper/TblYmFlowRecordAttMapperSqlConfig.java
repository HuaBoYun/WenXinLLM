package com.huabo.system.mapper;

import java.util.Date;

import com.hbfk.util.DateUtil;
import com.hbfk.util.database.DataBaseSqlConfig;
import com.huabo.system.entity.TblYmFlowRecordAtt;

public class TblYmFlowRecordAttMapperSqlConfig {
	public String insertEntity(TblYmFlowRecordAtt att) throws Exception {
		StringBuffer colSb = new StringBuffer("INSERT INTO TBL_YMFLOWRECORD_ATT(ATTID,UPLOADER,UPLOADTIME,ATTSIZE");
		StringBuffer valSb = new StringBuffer(" VALUES ( "+att.getAttid()+",'"+att.getUploader()+"',"+DataBaseSqlConfig.getDateStrFormat(new Date())+","+att.getAttsize());
		
		if(att.getAttname() != null && !"".equals(att.getAttname())) {
			colSb.append(",ATTNAME");
			valSb.append(",'"+att.getAttname()+"'");
		}
		
		if(att.getAttpath() != null && !"".equals(att.getAttpath())) {
			colSb.append(",ATTPATH");
			valSb.append(",'"+att.getAttpath()+"'");
		}
		
		if(att.getFlowtaskid() != null && !"".equals(att.getFlowtaskid())) {
			colSb.append(",FLOWTASKID");
			valSb.append(",'"+att.getFlowtaskid()+"'");
		}
		
		if(att.getOperatorid() != null && !"".equals(att.getOperatorid())) {
			colSb.append(",OPERATORID");
			valSb.append(",'"+att.getOperatorid()+"'");
		}
		
		if(att.getJmurl() != null && !"".equals(att.getJmurl())) {
			colSb.append(",JMURL");
			valSb.append(",'"+att.getJmurl()+"'");
		}
		
		String sql = colSb.toString()+")"+valSb.toString()+")";
		return sql;
	}
	
	public String selectAllList(String flowTaskOperatorId, String flowTaskId) {
		StringBuffer sqlSb = new StringBuffer("SELECT TYA.ATTID,TYA.ATTNAME,TYA.ATTSIZE,TYA.UPLOADTIME,TYA.ATTPATH,TYA.FLOWTASKID,TYA.OPERATORID,TS.REALNAME AS UPLOADERNAME FROM TBL_YMFLOWRECORD_ATT TYA LEFT JOIN TBL_STAFF TS ON TYA.UPLOADER = TS.STAFFID WHERE 1 = 1 ");
		
		if(flowTaskOperatorId != null && !"".equals(flowTaskOperatorId)){
			sqlSb.append(" AND TYA.OPERATORID = '"+flowTaskOperatorId+"'");
		}
		
		if(flowTaskId != null && !"".equals(flowTaskId)){
			sqlSb.append(" AND TYA.FLOWTASKID = '"+flowTaskId+"'");
		}
		return sqlSb.toString();
	}
}
