package com.huabo.system.mapper;

import com.hbfk.util.DateUtil;
import com.huabo.system.entity.TblMyTask;

import cn.hutool.core.util.StrUtil;

public class TblMyTaskMapperSqlConfig {
	
	public String findByObj(String fromid,String status,String staffid,String type) {
		String sql="SELECT * FROM ( SELECT TBL_MY_TASK.*, ROW_NUMBER () OVER ( PARTITION BY FROMID ORDER BY ID DESC ) cn FROM TBL_MY_TASK "
				+ " WHERE 1=1 "//KZJZEXAM='"+type+"'
						+ ") WHERE cn = 1 AND result LIKE '%"+status+"%' and fxexam ="+staffid;
		if(StrUtil.isNotEmpty(fromid)) {
			sql+=" and fromid="+fromid;
		}
		return sql;
	}

	public String insertMyTaskSetting(TblMyTask task) {
		StringBuffer column = new StringBuffer("INSERT INTO TBL_MY_TASK (ID");
		StringBuffer value = new StringBuffer(" VALUES (MYTASK_SEQUENCE.nextval");
		
		if(task.getTaskId() != null && !"".equals(task.getTaskId())){
			column.append(",TASKID");
			value.append(",'"+task.getTaskId()+"'");
		}
		if(task.getProcessDefinitionId() != null && !"".equals(task.getProcessDefinitionId())){
			column.append(",PROCESSDEFINITIONID");
			value.append(",'"+task.getProcessDefinitionId()+"'");
		}
		if(task.getProcessInstanceId() != null && !"".equals(task.getProcessInstanceId())){
			column.append(",PROCESSINSTANCEID");
			value.append(",'"+task.getProcessInstanceId()+"'");
		}
		if(task.getUsrid() != null && !"".equals(task.getUsrid())){
			column.append(",USRID");
			value.append(",'"+task.getUsrid()+"'");
		}
		if(task.getFromid() != null && !"".equals(task.getFromid())){
			column.append(",FROMID");
			value.append(",'"+task.getFromid()+"'");
		}
		if(task.getFromname() != null && !"".equals(task.getFromname())){
			column.append(",FROMNAME");
			value.append(",'"+task.getFromname()+"'");
		}
		if(task.getApprover() != null && !"".equals(task.getApprover())){
			column.append(",APPROVER");
			value.append(",'"+task.getApprover()+"'");
		}
		if(task.getExamination() != null && !"".equals(task.getExamination())){
			column.append(",EXAMINATION");
			value.append(",'"+task.getExamination()+"'");
		}
		if(task.getProcessName() != null && !"".equals(task.getProcessName())){
			column.append(",PROCESSNAME");
			value.append(",'"+task.getProcessName()+"'");
		}
		if(task.getResult() != null && !"".equals(task.getResult())){
			column.append(",RESULT");
			value.append(",'"+task.getResult()+"'");
		}
		if(task.getApprovalrole() != null && !"".equals(task.getApprovalrole())){
			column.append(",APPROVALROLE");
			value.append(",'"+task.getApprovalrole()+"'");
		} 
		if(task.getApprovaldate() != null && !"".equals(task.getApprovaldate())){
			column.append(",APPROVALDATE");
			value.append(",TO_DATE('"+DateUtil.parseDate(task.getApprovaldate(),"yyyy-MM-dd HH:mm:ss") +"', 'YYYY-MM-DD HH24:MI:SS')");
		}
		if(task.getCirid() != null && !"".equals(task.getCirid())){
			column.append(",CIRID");
			value.append(",'"+task.getCirid()+"'");
		}
		if(task.getHandle() != null && !"".equals(task.getHandle())){
			column.append(",HANDLE");
			value.append(",'"+task.getHandle()+"'");
		}
		if(task.getAnalid() != null && !"".equals(task.getAnalid())){
			column.append(",ANALID");
			value.append(",'"+task.getAnalid()+"'");
		}
		if(task.getFxexam() != null && !"".equals(task.getFxexam())){
			column.append(",FXEXAM");
			value.append(",'"+task.getFxexam()+"'");
		}
		if(task.getKzjzexam() != null && !"".equals(task.getKzjzexam())){
			column.append(",KZJZEXAM");
			value.append(",'"+task.getKzjzexam()+"'");
		}
		if(task.getImgbasestr() != null && !"".equals(task.getImgbasestr())){
			column.append(",IMGBASESTR");
			value.append(",'"+task.getImgbasestr()+"'");
		}
		column.append(")");
		value.append(")");
		String sql = column.toString()+value.toString();
		return sql;
	}



	public String updateSetting(TblMyTask task) {
		StringBuffer sql = new StringBuffer("UPDATE TBL_MY_TASK SET APPROVER = "+task.getApprover()+"");

		if(task.getTaskId() != null && !"".equals(task.getTaskId())) {
			sql.append("  TASKID = '"+task.getTaskId()+"'");
		}
		if(task.getProcessDefinitionId() != null && !"".equals(task.getProcessDefinitionId())) {
			sql.append(" , PROCESSDEFINITIONID = '"+task.getProcessDefinitionId()+"'");
		}
		if(task.getProcessInstanceId() != null && !"".equals(task.getProcessInstanceId())) {
			sql.append(" , PROCESSINSTANCEID = '"+task.getProcessInstanceId()+"'");
		}
		if(task.getUsrid() != null && !"".equals(task.getUsrid())) {
			sql.append(" , USRID = '"+task.getUsrid()+"'");
		}
		if(task.getFromid() != null && !"".equals(task.getFromid())) {
			sql.append(" , FROMID = '"+task.getFromid()+"'");
		}
		if(task.getFromname() != null && !"".equals(task.getFromname())) {
			sql.append(" , FROMNAME = '"+task.getFromname()+"'");
		}
		if(task.getApprover() != null && !"".equals(task.getApprover())) {
			sql.append(" , APPROVER = '"+task.getApprover()+"'");
		}
		if(task.getExamination() != null && !"".equals(task.getExamination())) {
			sql.append(" , EXAMINATION = '"+task.getExamination()+"'");
		}
		if(task.getProcessName() != null && !"".equals(task.getProcessName())) {
			sql.append(" , PROCESSNAME = '"+task.getProcessName()+"'");
		}
		if(task.getResult() != null && !"".equals(task.getResult())) {
			sql.append(" , RESULT = '"+task.getResult()+"'");
		}
		if(task.getApprovalrole() != null && !"".equals(task.getApprovalrole())) {
			sql.append(" , APPROVALROLE = '"+task.getApprovalrole()+"'");
		}
		if(task.getApprovaldate() != null && !"".equals(task.getApprovaldate())) {
			sql.append(" , APPROVALDATE = '"+task.getApprovaldate()+"'");
		}
		if(task.getCirid() != null && !"".equals(task.getCirid())) {
			sql.append(" , CIRID = '"+task.getCirid()+"'");
		}
		if(task.getHandle() != null && !"".equals(task.getHandle())) {
			sql.append(" , HANDLE = '"+task.getHandle()+"'");
		}
		if(task.getAnalid() != null && !"".equals(task.getAnalid())) {
			sql.append(" , ANALID = '"+task.getAnalid()+"'");
		}
		if(task.getFxexam() != null && !"".equals(task.getFxexam())) {
			sql.append(" , FXEXAM = '"+task.getFxexam()+"'");
		}
		if(task.getKzjzexam() != null && !"".equals(task.getKzjzexam())) {
			sql.append(" , KZJZEXAM = '"+task.getKzjzexam()+"'");
		}
		if(task.getImgbasestr() != null && !"".equals(task.getImgbasestr())) {
			sql.append(" , IMGBASESTR = '"+task.getImgbasestr()+"'");
		}

		sql.append(" WHERE ID = '"+task.getId()+"'");
		return sql.toString();
	}
}
