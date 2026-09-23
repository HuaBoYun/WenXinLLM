package com.huabo.monitor.mapper;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.hbfk.util.DateUtil;
import com.huabo.monitor.entity.TblAssesstemple;

public class TblAssesstempleMapperSqlConfig {

	public String insertTemples(TblAssesstemple tblAssesstemple) {
		String colsql = "INSERT INTO Tbl_assesstemple(ASSTEMID";
		String valueSql = " VALUES (HIBERNATE_SEQUENCE.nextval";
				if(StringUtils.isNotBlank(tblAssesstemple.getTemplename())){
					colsql+=",TEMPLENAME";
					valueSql+=",'"+tblAssesstemple.getTemplename()+"'";
				}
				if(tblAssesstemple.getOrgid()!=null){
					colsql+=",ORGID";
					valueSql+=","+tblAssesstemple.getOrgid();
				}
				if(StringUtils.isNotBlank(tblAssesstemple.getTemplenumber())){
					colsql+=",TEMPLENUMBER";
					valueSql+=",'"+tblAssesstemple.getTemplenumber()+"'";
				}
				if(tblAssesstemple.getStaffid()!=null){
					colsql+=",STAFFID";
					valueSql+=","+tblAssesstemple.getStaffid();
				}
				if(tblAssesstemple.getModifydatetime()!=null){
					colsql+=",MODIFYDATETIME";
					valueSql+=",TO_DATE('"+ DateUtil.parseDate(tblAssesstemple.getModifydatetime(),"yyyy-MM-dd HH:mm:ss") +"', 'YYYY-MM-DD HH24:MI:SS')";
				}
				if(StringUtils.isNotBlank(tblAssesstemple.getTempledes())){
					colsql+=",TEMPLEDES";
					valueSql+=",'"+tblAssesstemple.getTempledes()+"'";
				}
		String sql = colsql + ")" +valueSql + ")";
		return sql ;
	}
	
	public String updateTemples(TblAssesstemple tblAssesstemple) {
		String sql = "UPDATE Tbl_assesstemple SET TEMPLENAME = '"+tblAssesstemple.getTemplename()+"'";
		if(tblAssesstemple.getOrgid()!=null){
			sql+=",ORGID="+tblAssesstemple.getOrgid();
		}
		if(StringUtils.isNotBlank(tblAssesstemple.getTemplenumber())){
			sql+=",TEMPLENUMBER='"+tblAssesstemple.getTemplenumber()+"'";
		}
		if(tblAssesstemple.getStaffid()!=null){
			sql+=",STAFFID="+tblAssesstemple.getStaffid();
		}
		if(tblAssesstemple.getModifydatetime()!=null){
			sql+=",MODIFYDATETIME=TO_DATE('"+ DateUtil.parseDate(tblAssesstemple.getModifydatetime(),"yyyy-MM-dd HH:mm:ss") +"', 'YYYY-MM-DD HH24:MI:SS')";
		}
		if(StringUtils.isNotBlank(tblAssesstemple.getTempledes())){
			sql+=",TEMPLEDES='"+tblAssesstemple.getTempledes()+"'";
		}
		sql += " WHERE ASSTEMID = "+tblAssesstemple.getAsstemid();
		return sql ;
	}
}
