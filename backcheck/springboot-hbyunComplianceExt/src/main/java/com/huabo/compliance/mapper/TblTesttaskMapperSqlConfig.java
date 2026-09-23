package com.huabo.compliance.mapper;



import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.huabo.compliance.entity.TblTesttask;

public class TblTesttaskMapperSqlConfig {

	public String insertEntity(TblTesttask tblTesttask) {
		String colsql = "INSERT INTO TBL_COM_EXT_TESTTASK(TESTTASKID";
		String valueSql = " VALUES (HIBERNATE_SEQUENCE.nextval";
				if(StringUtils.isNotBlank(tblTesttask.getTestresult())){
					colsql+=",TESTRESULT";
					valueSql+=",'"+tblTesttask.getTestresult()+"'";
				} 
				if(StringUtils.isNotBlank(tblTesttask.getTestpointvalidity())){
					colsql+=",TESTPOINTVALIDITY";
					valueSql+=",'"+tblTesttask.getTestpointvalidity()+"'";
				}
				if(StringUtils.isNotBlank(tblTesttask.getTeststatus())){
					colsql+=",TESTSTATUS";
					valueSql+=",'"+tblTesttask.getTeststatus()+"'";
				}
				if(tblTesttask.getElementid()!=null){
					colsql+=",ELEMENTID";
					valueSql+=","+tblTesttask.getElementid()+"";
				}
				if(StringUtils.isNotBlank(tblTesttask.getMemo())){
					colsql+=",MEMO";
					valueSql+=",'"+tblTesttask.getMemo()+"'";
				}
				if(tblTesttask.getPlanid()!=null){
					colsql+=",PLANID";
					valueSql+=","+tblTesttask.getPlanid()+"";
				}
				if(tblTesttask.getCompletestaus()!=null){
					colsql+=",COMPLETESTAUS";
					valueSql+=","+tblTesttask.getCompletestaus()+"";
				}
				if(tblTesttask.getAttid()!=null){
					colsql+=",ATTID";
					valueSql+=","+tblTesttask.getAttid()+"";
				}
				
				if(StringUtils.isNotBlank(tblTesttask.getAttname())){
					colsql+=",ATTNAME";
					valueSql+=",'"+tblTesttask.getAttname()+"'";
				}
				
				if(StringUtils.isNotBlank(tblTesttask.getProcedures())){
					colsql+=",PROCEDURES";
					valueSql+=",'"+tblTesttask.getProcedures()+"'";
				}

				if(tblTesttask.getCpuserid()!=null){
					colsql+=",CPUSERID";
					valueSql+=","+tblTesttask.getCpuserid()+"";
				}
				if(tblTesttask.getReturnstatus()!=null){
					colsql+=",RETURNSTATUS";
					valueSql+=","+tblTesttask.getReturnstatus()+"";
				}
				  
				if(StringUtils.isNotBlank(tblTesttask.getProposal())){
					colsql+=",PROPOSAL";
					valueSql+=",'"+tblTesttask.getProposal()+"'";
				}
		String sql = colsql + ")" +valueSql + ")";
		return sql ;
	}
	
	
	public String updateEntity(TblTesttask tblTesttask) {
		String sql = "UPDATE TBL_COM_EXT_TESTTASK set TESTTASKID = "+tblTesttask.getTesttaskid();
		if(StringUtils.isNotBlank(tblTesttask.getTestresult())){
			sql+=",TESTRESULT='"+tblTesttask.getTestresult()+"'";
		} 
		if(StringUtils.isNotBlank(tblTesttask.getTestpointvalidity())){
			sql+=",TESTPOINTVALIDITY='"+tblTesttask.getTestpointvalidity()+"'";
		}
		if(StringUtils.isNotBlank(tblTesttask.getTeststatus())){
			sql+=",TESTSTATUS='"+tblTesttask.getTeststatus()+"'";
		}
		if(tblTesttask.getElementid()!=null){
			sql+=",ELEMENTID="+tblTesttask.getElementid()+"";
		}
		if(StringUtils.isNotBlank(tblTesttask.getMemo())){
			sql+=",MEMO='"+tblTesttask.getMemo()+"'";
		}
		if(tblTesttask.getPlanid()!=null){
			sql+=",PLANID="+tblTesttask.getPlanid()+"";
		}
		if(tblTesttask.getCompletestaus()!=null){
			sql+=",COMPLETESTAUS="+tblTesttask.getCompletestaus()+"";
		}
		if(tblTesttask.getAttid()!=null){
			sql+=",ATTID="+tblTesttask.getAttid()+"";
		}
		
		if(StringUtils.isNotBlank(tblTesttask.getAttname())){
			sql+=",ATTNAME='"+tblTesttask.getAttname()+"'";
		}
		
		if(StringUtils.isNotBlank(tblTesttask.getProcedures())){
			sql+=",PROCEDURES='"+tblTesttask.getProcedures()+"'";
		}

		if(tblTesttask.getCpuserid()!=null){
			sql+=",CPUSERID="+tblTesttask.getCpuserid()+"";
		}
		if(tblTesttask.getReturnstatus()!=null){
			sql+=",RETURNSTATUS="+tblTesttask.getReturnstatus()+"";
		}
		  
		if(StringUtils.isNotBlank(tblTesttask.getProposal())){
			sql+=",PROPOSAL='"+tblTesttask.getProposal()+"'";
		}
		sql += " WHERE TESTTASKID = "+tblTesttask.getTesttaskid();
		return sql ;
	}
	
}
