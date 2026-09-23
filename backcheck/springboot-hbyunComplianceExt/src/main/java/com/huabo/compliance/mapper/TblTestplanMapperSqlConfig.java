package com.huabo.compliance.mapper;



import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.huabo.compliance.entity.TblTestplan;

public class TblTestplanMapperSqlConfig {

	public String insertEntity(TblTestplan tblTestplan) {
		String colsql = "INSERT INTO TBL_COM_EXT_TESTPLAN(TESTPLANID";
		String valueSql = " VALUES (HIBERNATE_SEQUENCE.nextval";
				if(StringUtils.isNotBlank(tblTestplan.getPlannumber())){
					colsql+=",PLANNUMBER";
					valueSql+=",'"+tblTestplan.getPlannumber()+"'";
				} 
				
				if(StringUtils.isNotBlank(tblTestplan.getPlanname())){
					colsql+=",PLANNAME";
					valueSql+=",'"+tblTestplan.getPlanname()+"'";
				} 
				
				if(StringUtils.isNotBlank(tblTestplan.getPlanyear())){
					colsql+=",PLANYEAR";
					valueSql+=",'"+tblTestplan.getPlanyear()+"'";
				}
				if(StringUtils.isNotBlank(tblTestplan.getTesttype())){
					colsql+=",TESTTYPE";
					valueSql+=",'"+tblTestplan.getTesttype()+"'";
				}
				if(StringUtils.isNotBlank(tblTestplan.getPlanmadeorg())){
					colsql+=",PLANMADEORG";
					valueSql+=",'"+tblTestplan.getPlanmadeorg()+"'";
				}
				if(StringUtils.isNotBlank(tblTestplan.getPlanmadedep())){
					colsql+=",PLANMADEDEP";
					valueSql+=",'"+tblTestplan.getPlanmadedep()+"'";
				}
				  if(tblTestplan.getStarttime() != null) {
			            colsql+=",STARTTIME";
						valueSql+=",TO_DATE('"+ tblTestplan.getStarttime().toString().replace("T", " ") +"', 'YYYY-MM-DD HH24:MI:SS')";
				  }
				  
				  if(tblTestplan.getEndtime() != null) {
			            colsql+=",ENDTIME";
						valueSql+=",TO_DATE('"+ tblTestplan.getEndtime().toString().replace("T", " ")+"', 'YYYY-MM-DD HH24:MI:SS')";
				  }
				if(StringUtils.isNotBlank(tblTestplan.getPlanleader())){
					colsql+=",PLANLEADER";
					valueSql+=",'"+tblTestplan.getPlanleader()+"'";
				}
				if(tblTestplan.getPlanfee()!=null){
					colsql+=",PLANFEE";
					valueSql+=","+tblTestplan.getPlanfee()+"";
				}
				
				if(StringUtils.isNotBlank(tblTestplan.getNumberofpeople())){
					colsql+=",NUMBEROFPEOPLE";
					valueSql+=",'"+tblTestplan.getNumberofpeople()+"'";
				}
				if(StringUtils.isNotBlank(tblTestplan.getTestedorgs())){
					colsql+=",TESTEDORGS";
					valueSql+=",'"+tblTestplan.getTestedorgs()+"'";
				}
				if(StringUtils.isNotBlank(tblTestplan.getMemo())){
					colsql+=",MEMO";
					valueSql+=",'"+tblTestplan.getMemo()+"'";
				}
				if(StringUtils.isNotBlank(tblTestplan.getPlanstatus())){
					colsql+=",PLANSTATUS";
					valueSql+=",'"+tblTestplan.getPlanstatus()+"'";
				}
				if(tblTestplan.getOrgid()!=null){
					colsql+=",ORGID";
					valueSql+=","+tblTestplan.getOrgid()+"";
				}
				if(tblTestplan.getCreatid()!=null){
					colsql+=",CREATID";
					valueSql+=","+tblTestplan.getCreatid()+"";
				}
				if(tblTestplan.getStaffid()!=null){
					colsql+=",STAFFID";
					valueSql+=","+tblTestplan.getStaffid()+"";
				}
				if(tblTestplan.getTesttemid()!=null){
					colsql+=",TESTTEMID";
					valueSql+=","+tblTestplan.getTesttemid()+"";
				}
				if(tblTestplan.getReturnstatus()!=null){
					colsql+=",RETURNSTATUS";
					valueSql+=","+tblTestplan.getReturnstatus()+"";
				}
		String sql = colsql + ")" +valueSql + ")";
		return sql ;
	}
	
	
	public String updateEntity(TblTestplan tblTestplan) {
		String sql = "UPDATE TBL_COM_EXT_TESTPLAN SET PLANNUMBER="+"'"+tblTestplan.getPlannumber()+"'";
		if(StringUtils.isNotBlank(tblTestplan.getPlanyear())){
			sql += ",PLANYEAR='"+tblTestplan.getPlanyear()+"'";
		}
		
		if(StringUtils.isNotBlank(tblTestplan.getPlanname())){
			 sql+=",PLANNAME='"+tblTestplan.getPlanname()+"'";
		} 
		
		if(StringUtils.isNotBlank(tblTestplan.getTesttype())){
			sql+=",TESTTYPE='"+tblTestplan.getTesttype()+"'";
		}
		if(StringUtils.isNotBlank(tblTestplan.getPlanmadeorg())){
			sql+=",PLANMADEORG='"+tblTestplan.getPlanmadeorg()+"'";
		}
		if(StringUtils.isNotBlank(tblTestplan.getPlanmadedep())){
			sql+=",PLANMADEDEP='"+tblTestplan.getPlanmadedep()+"'";
		}
		  if(tblTestplan.getStarttime() != null) {
	            sql+=",STARTTIME=TO_DATE('"+ tblTestplan.getStarttime().toString().replace("T", " ") +"', 'YYYY-MM-DD HH24:MI:SS')";
		  }
		  
		  if(tblTestplan.getEndtime() != null) {
	            sql+=",ENDTIME=TO_DATE('"+ tblTestplan.getEndtime().toString().replace("T", " ")+"', 'YYYY-MM-DD HH24:MI:SS')";
		  }
		if(StringUtils.isNotBlank(tblTestplan.getPlanleader())){
			sql+=",PLANLEADER='"+tblTestplan.getPlanleader()+"'";
		}
		if(tblTestplan.getPlanfee()!=null){
			sql+=",PLANFEE="+tblTestplan.getPlanfee()+"";
		}
		
		if(StringUtils.isNotBlank(tblTestplan.getNumberofpeople())){
			sql+=",NUMBEROFPEOPLE='"+tblTestplan.getNumberofpeople()+"'";
		}
		if(StringUtils.isNotBlank(tblTestplan.getTestedorgs())){
			sql+=",TESTEDORGS='"+tblTestplan.getTestedorgs()+"'";
		}
		if(StringUtils.isNotBlank(tblTestplan.getMemo())){
			sql+=",MEMO='"+tblTestplan.getMemo()+"'";
		}
		if(StringUtils.isNotBlank(tblTestplan.getPlanstatus())){
			sql+=",PLANSTATUS='"+tblTestplan.getPlanstatus()+"'";
		}
		if(tblTestplan.getOrgid()!=null){
			sql+=",ORGID="+tblTestplan.getOrgid()+"";
		}
		if(tblTestplan.getCreatid()!=null){
			sql+=",CREATID="+tblTestplan.getCreatid()+"";
		}
		if(tblTestplan.getStaffid()!=null){
			sql+=",STAFFID="+tblTestplan.getStaffid()+"";
		}
		if(tblTestplan.getTesttemid()!=null){
			sql+=",TESTTEMID="+tblTestplan.getTesttemid()+"";
		}
		if(tblTestplan.getReturnstatus()!=null){
			sql+=",RETURNSTATUS="+tblTestplan.getReturnstatus()+"";
		}
		sql += " WHERE TESTPLANID = "+tblTestplan.getTestplanid();
		return sql ;
	}
	
}
