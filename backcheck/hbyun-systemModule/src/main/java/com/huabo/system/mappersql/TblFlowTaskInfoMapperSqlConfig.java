package com.huabo.system.mappersql;

import java.util.Date;

import com.hbfk.util.database.DataBaseSqlConfig;
import com.hbfk.util.redis.Random.RandomUtil;
import com.huabo.system.entity.TblFlowTaskInfo;

public class TblFlowTaskInfoMapperSqlConfig {
	
	public String selectApprovalMemo(String processId, String condition) {
		String sql = "SELECT DISTINCT TS.REALNAME,TFT.COMMONT FROM TBL_FLOW_TASKINFO TFT LEFT JOIN TBL_STAFF TS ON TFT.CURRENTSTAFFID = TS.STAFFID WHERE PROCESSID = '"+processId+"' AND TFT.COMMONT IS NOT NULL AND CURRENTROLE IN "+condition+" AND TASKGROUPID IN (SELECT MAX(TASKGROUPID) FROM TBL_FLOW_TASKINFO WHERE PROCESSID = '"+processId+"' AND CURRENTROLE IN "+condition+" GROUP BY CURRENTROLE,CURRENTSTAFFID )";
		
		return sql;
	}
	
	
	public String saveEntity(TblFlowTaskInfo taskInfo) throws Exception {
		 StringBuffer column = new StringBuffer("INSERT INTO TBL_FLOW_TASKINFO (TAKSID,CREATETIME");
	     StringBuffer value = new StringBuffer(" VALUES ("+RandomUtil.uuBigDecimalId()+",").append(DataBaseSqlConfig.getDateStrFormat(new Date()));

	     if(taskInfo.getTaskNo() != null) {
	    	 column.append(",TASKNO");
	         value.append(",'"+taskInfo.getTaskNo()+"'");
	     }
	     if(taskInfo.getTaskTitle() != null) {
	    	 column.append(",TASKTITLE");
	         value.append(",'"+taskInfo.getTaskTitle()+"'");
	     }
	     if(taskInfo.getFromId() != null) {
	    	 column.append(",FROMID");
	         value.append(",'"+taskInfo.getFromId()+"'");
	     }
	     if(taskInfo.getFlowId() != null) {
	    	 column.append(",FLOWID");
	         value.append(",'"+taskInfo.getFlowId()+"'");
	     }
	     if(taskInfo.getProcessId() != null) {
	    	 column.append(",PROCESSID");
	         value.append(",'"+taskInfo.getProcessId()+"'");
	     }
	     if(taskInfo.getFlowTaskId() != null) {
	    	 column.append(",FLOWTASKID");
	         value.append(",'"+taskInfo.getFlowTaskId()+"'");
	     }
	     if(taskInfo.getThisStepId() != null) {
	    	 column.append(",THISSTEPID");
	         value.append(",'"+taskInfo.getThisStepId()+"'");
	     }
	     if(taskInfo.getOperatorId() != null) {
	    	 column.append(",OPERATORID");
	         value.append(",'"+taskInfo.getOperatorId()+"'");
	     }
	     if(taskInfo.getCurrentStaffId() != null) {
	    	 column.append(",CURRENTSTAFFID");
	         value.append(","+taskInfo.getCurrentStaffId()+"");
	     }
	     if(taskInfo.getCurrenRole() != null) {
	    	 column.append(",CURRENTROLE");
	         value.append(",'"+taskInfo.getCurrenRole()+"'");
	     }
	     if(taskInfo.getOperation() != null) {
	    	 column.append(",OPERATION");
	         value.append(",'"+taskInfo.getOperation()+"'");
	     }
	     if(taskInfo.getCommont() != null) {
	    	 column.append(",COMMONT");
	         value.append(",'"+taskInfo.getCommont()+"'");
	     }
	     if(taskInfo.getNextStaffId() != null) {
	    	 column.append(",NEXTSTAFFID");
	         value.append(","+taskInfo.getNextStaffId()+"");
	     }
	     if(taskInfo.getNextRole() != null) {
	    	 column.append(",NEXTROLE");
	         value.append(",'"+taskInfo.getNextRole()+"'");
	     }
	     if(taskInfo.getModuleType() != null) {
	    	 column.append(",MODULETYPE");
	         value.append(",'"+taskInfo.getModuleType()+"'");
	     }
	     if(taskInfo.getModuleType() != null) {
	    	 column.append(",TASKNODEID");
	         value.append(",'"+taskInfo.getTaskNodeId()+"'");
	     }
	     if(taskInfo.getModuleType() != null) {
	    	 column.append(",TASKSTATUS");
	         value.append(","+taskInfo.getTaskStatus()+"");
	     }
	     if(taskInfo.getModuleType() != null) {
	    	 column.append(",TASKGROUPID");
	         value.append(","+taskInfo.getTaskGroupId()+"");
	     }
	     column.append(")");
	     value.append(")");
	     String sql = column.toString()+value.toString();
	     return sql;
	}
}
