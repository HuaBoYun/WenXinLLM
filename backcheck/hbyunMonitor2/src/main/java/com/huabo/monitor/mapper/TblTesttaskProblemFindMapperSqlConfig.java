package com.huabo.monitor.mapper;

import java.util.Date;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.hbfk.util.DateUtil;
import com.huabo.monitor.entity.TblTesttaskProblemFind;

public class TblTesttaskProblemFindMapperSqlConfig {

	public String insertEntity(TblTesttaskProblemFind tblTesttask) {
		String colsql = "INSERT INTO TBL_TESTTASK_PROBLEMFIND(FINDID";
		String valueSql = " VALUES (HIBERNATE_SEQUENCE.nextval";
				if(tblTesttask.getTesttaskid()!=null){
					colsql+=",TESTTASKID";
					valueSql+=",'"+tblTesttask.getTesttaskid()+"'";
				}
				if(tblTesttask.getCreatestaffid()!=null){
					colsql+=",CREATESTAFFID";
					valueSql+=",'"+tblTesttask.getCreatestaffid()+"'";
				}
				if(null != tblTesttask.getCreatetime()){
					colsql+=",CREATETIME";
					valueSql+=", TO_DATE('"+DateUtil.parseDate(new Date(),"yyyy-MM-dd HH:mm:ss")+"', 'YYYY-MM-DD HH24:MI:SS')";
				}
				if(tblTesttask.getOneprocess()!=null){
					colsql+=",ONEPROCESS";
					valueSql+=",'"+tblTesttask.getOneprocess()+"'";
				}
				if(StringUtils.isNotBlank(tblTesttask.getProblemmemo())){
					colsql+=",PROBLEMMEMO";
					valueSql+=",'"+tblTesttask.getProblemmemo()+"'";
				}
				if(StringUtils.isNotBlank(tblTesttask.getRisknumberid())){
					colsql+=",RISKNUMBERID";
					valueSql+=",'"+tblTesttask.getRisknumberid()+"'";
				}
				if(tblTesttask.getDefectmemo()!=null){
					colsql+=",DEFECTMEMO";
					valueSql+=",'"+tblTesttask.getDefectmemo()+"'";
				}
				if(tblTesttask.getProblemtype()!=null){
					colsql+=",PROBLEMTYPE";
					valueSql+=",'"+tblTesttask.getProblemtype()+"'";
				}
				if(tblTesttask.getDefectlevel()!=null){
					colsql+=",DEFECTLEVEL";
					valueSql+=",'"+tblTesttask.getDefectlevel()+"'";
				}
				
				if(StringUtils.isNotBlank(tblTesttask.getQuabasis())){
					colsql+=",QUABASIS";
					valueSql+=",'"+tblTesttask.getQuabasis()+"'";
				}
				
				if(tblTesttask.getMainorg()!=null){
					colsql+=",MAINORG";
					valueSql+=",'"+tblTesttask.getMainorg()+"'";
				}

				if(tblTesttask.getFeedback()!=null){
					colsql+=",FEEDBACK";
					valueSql+=",'"+tblTesttask.getFeedback()+"'";
				}
				if(tblTesttask.getReformplan()!=null){
					colsql+=",REFORMPLAN";
					valueSql+=",'"+tblTesttask.getReformplan()+"'";
				}
				  
				if(null != tblTesttask.getEstfinishdate()){
					colsql+=",ESTFINISHDATE";
					valueSql+=",TO_DATE('"+DateUtil.parseDate(tblTesttask.getEstfinishdate(),"yyyy-MM-dd HH:mm:ss")+"', 'YYYY-MM-DD HH24:MI:SS')";
				}
				
				if(null != tblTesttask.getReformstaffid()){
					colsql+=",REFORMSTAFFID";
					valueSql+=",'"+tblTesttask.getReformstaffid()+"'";
				}
				
				if(null != tblTesttask.getContent()){
					colsql+=",content";
					valueSql+=",'"+tblTesttask.getContent()+"'";
				}
				
				if(null != tblTesttask.getLinkOrg()){
					colsql+=",LINKORG";
					valueSql+=",'"+tblTesttask.getLinkOrg()+"'";
				}
				if(null != tblTesttask.getDefecttype()){
					colsql+=",DEFECTTYPE";
					valueSql+=",'"+tblTesttask.getDefecttype()+"'";
				}
				if( tblTesttask.getTestYear()>0){
					colsql+=",TESTYEAR";
					valueSql+=",'"+tblTesttask.getTestYear()+"'";
				}
				
				
		String sql = colsql + ")" +valueSql + ")";
		return sql ;
	}
	
	
	public String updateEntity(TblTesttaskProblemFind tblTesttask) {
		String sql = "UPDATE TBL_TESTTASK_PROBLEMFIND set FINDID = "+tblTesttask.getFindid();
		if(StringUtils.isNotBlank(tblTesttask.getOneprocess())){
			sql+=",ONEPROCESS='"+tblTesttask.getOneprocess()+"'";
		} 
		if(StringUtils.isNotBlank(tblTesttask.getProblemmemo())){
			sql+=",PROBLEMMEMO='"+tblTesttask.getProblemmemo()+"'";
		}
		if(StringUtils.isNotBlank(tblTesttask.getDefectmemo())){
			sql+=",DEFECTMEMO='"+tblTesttask.getDefectmemo()+"'";
		}
		if(StringUtils.isNotBlank(tblTesttask.getProblemtype())){
			sql+=",PROBLEMTYPE='"+tblTesttask.getProblemtype()+"'";
		}
		if(StringUtils.isNotBlank(tblTesttask.getDefectlevel())){
			sql+=",DEFECTLEVEL='"+tblTesttask.getDefectlevel()+"'";
		}
		if(StringUtils.isNotBlank(tblTesttask.getQuabasis())){
			sql+=",QUABASIS='"+tblTesttask.getQuabasis()+"'";
		}
		if(tblTesttask.getMainorg() != null){
			sql+=",MAINORG='"+tblTesttask.getMainorg()+"'";
		}
		if(StringUtils.isNotBlank(tblTesttask.getFeedback())){
			sql+=",FEEDBACK='"+tblTesttask.getFeedback()+"'";
		}
		if(StringUtils.isNotBlank(tblTesttask.getRisknumberid())){
			sql+=",RISKNUMBERID='"+tblTesttask.getRisknumberid()+"'";
		}
		if(StringUtils.isNotBlank(tblTesttask.getReformplan())){
			sql+=",REFORMPLAN='"+tblTesttask.getReformplan()+"'";
		}
		if(null != tblTesttask.getEstfinishdate()){
			sql+=",ESTFINISHDATE=TO_DATE('"+DateUtil.parseDate(tblTesttask.getEstfinishdate(),"yyyy-MM-dd HH:mm:ss")+"', 'YYYY-MM-DD HH24:MI:SS')";
		}
		if(null != tblTesttask.getReformstaffid()){
			sql+=",REFORMSTAFFID='"+tblTesttask.getReformstaffid()+"'";
		}
		if(null != tblTesttask.getStatus()){
			sql+=",STATUS='"+tblTesttask.getStatus()+"'";
		}
		
		if(null != tblTesttask.getLinkOrg()){
			sql+=",LinkOrg='"+tblTesttask.getLinkOrg()+"'";
		}
	 
		if(null != tblTesttask.getDefecttype()){
			sql+=",defecttype='"+tblTesttask.getDefecttype()+"'";
		}
		if(null != tblTesttask.getContent()){
			sql+=",CONTENT='"+tblTesttask.getContent()+"'";
		}
		if( tblTesttask.getTestYear()>0){
			sql+=",TESTYEAR='"+tblTesttask.getTestYear()+"'";
		}
		
		sql += " WHERE FINDID = "+tblTesttask.getFindid();
		return sql ;
	}
	
}
