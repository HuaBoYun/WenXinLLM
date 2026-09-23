package com.huabo.audit.oracle.mapper;

import java.util.Date;

import com.hbfk.util.DateUtil;
import com.huabo.audit.oracle.entity.TblCirculation;

public class TblCirculationMapperSqlConfig {

	public String updateCirculationInfoById(TblCirculation circulation) {
		StringBuffer sql = new StringBuffer("UPDATE TBL_CIRCULATION SET CYNAME = '"+circulation.getCyname()+"'");
		if(circulation.getCytype() != null && !"".equals(circulation.getCytype())) {
			sql.append(" , CYTYPE = '"+circulation.getCytype()+"'");
		}
		if(circulation.getCycode() != null && !"".equals(circulation.getCycode())) {
			sql.append(" , CYCODE = '"+circulation.getCycode()+"'");
		}
		if(circulation.getCyurl() != null && !"".equals(circulation.getCyurl())) {
			sql.append(" , CYURL = '"+circulation.getCyurl()+"'");
		}
		if(circulation.getCyStaffid() != null && !"".equals(circulation.getCyStaffid())) {
			sql.append(" , CYSTAFFID = '"+circulation.getCyStaffid()+"'");
		}
		if(circulation.getBusinesskey() != null && !"".equals(circulation.getBusinesskey())) {
			sql.append(" , BUSINESSKEY = '"+circulation.getBusinesskey()+"'");
		}
		if(circulation.getDefinitionid() != null && !"".equals(circulation.getDefinitionid())) {
			sql.append(" , DEFINITIONID = '"+circulation.getDefinitionid()+"'");
		}
		if(circulation.getTaskid() != null && !"".equals(circulation.getTaskid())) {
			sql.append(" , TASKID = '"+circulation.getTaskid()+"'");
		}
		if(circulation.getCystate() != null && !"".equals(circulation.getCystate())) {
			sql.append(" , CYSTATE = '"+circulation.getCystate()+"'");
		}
		if(circulation.getCydate() != null && !"".equals(circulation.getCydate())) {
			sql.append(" , CYDATE = TO_DATE('"+DateUtil.parseDate(new Date(),"yyyy-MM-dd HH:mm:ss")+"', 'YYYY-MM-DD HH24:MI:SS')");
		}
		sql.append(" WHERE CYID = '"+circulation.getCyid()+"'");
		System.out.println(sql.toString());
		return sql.toString();
	}
	
	
	public String saveTblCirculationnew(String type, String number, String name, String url, String loginUser,String buskey,String definitionId,String taskid) {
		StringBuffer column = new StringBuffer("INSERT INTO TBL_CIRCULATION (CYID,CYDATE,CYSTATE");
		StringBuffer value = new StringBuffer(" VALUES (CIRCULATION_SEQUENCE.nextval,TO_DATE('"+DateUtil.parseDate(new Date(),"yyyy-MM-dd HH:mm:ss") +"', 'YYYY-MM-DD HH24:MI:SS'),'"+TblCirculation.STATE_FQ+"'");
		
		if(type != null && !"".equals(type)) {
			column.append(",CYTYPE");
			value.append(",'"+type+"'");
		}
		if(number != null && !"".equals(number)) {
			column.append(",CYCODE");
			value.append(",'"+number+"'");
		}
		if(name != null && !"".equals(name)) {
			column.append(",CYNAME");
			value.append(",'"+name+"'");
		}
		if(url != null && !"".equals(url)) {
			column.append(",CYURL");
			value.append(",'"+url+"'");
		}
		if(loginUser != null && !"".equals(loginUser)) {
			column.append(",CYSTAFFID");
			value.append(",'"+loginUser+"'");
		}
		if(buskey != null && !"".equals(buskey)) {
			column.append(",BUSINESSKEY");
			value.append(",'"+buskey+"'");
		}
		if(definitionId != null && !"".equals(definitionId)) {
			column.append(",DEFINITIONID");
			value.append(",'"+definitionId+"'");
		}
		if(taskid != null && !"".equals(taskid)) {
			column.append(",TASKID");
			value.append(",'"+taskid+"'");
		}
		column.append(")");
		value.append(")");
		String sql = column.toString()+value.toString();
		return sql;
	}

	public String saveTblCirculation(TblCirculation c) {
		StringBuffer column = new StringBuffer("INSERT INTO TBL_CIRCULATION (CYID,CYDATE,CYSTATE");
		StringBuffer value = new StringBuffer(" VALUES (CIRCULATION_SEQUENCE.nextval,TO_DATE('"+DateUtil.parseDate(new Date(),"yyyy-MM-dd HH:mm:ss") +"', 'YYYY-MM-DD HH24:MI:SS'),'"+TblCirculation.STATE_FQ+"'");

		if(c.getCytype() != null && !"".equals(c.getCytype())) {
			column.append(",CYTYPE");
			value.append(",'"+c.getCytype()+"'");
		}
		if(c.getCycode() != null && !"".equals(c.getCycode())) {
			column.append(",CYCODE");
			value.append(",'"+c.getCycode()+"'");
		}
		if(c.getCyname() != null && !"".equals(c.getCyname())) {
			column.append(",CYNAME");
			value.append(",'"+c.getCyname()+"'");
		}
		if(c.getCyurl() != null && !"".equals(c.getCyurl())) {
			column.append(",CYURL");
			value.append(",'"+c.getCyurl()+"'");
		}
		if(c.getCyStaffid() != null && !"".equals(c.getCyStaffid())) {
			column.append(",CYSTAFFID");
			value.append(",'"+c.getCyStaffid()+"'");
		}
		if(c.getBusinesskey() != null && !"".equals(c.getBusinesskey())) {
			column.append(",BUSINESSKEY");
			value.append(",'"+c.getBusinesskey()+"'");
		}
		if(c.getDefinitionid() != null && !"".equals(c.getDefinitionid())) {
			column.append(",DEFINITIONID");
			value.append(",'"+c.getDefinitionid()+"'");
		}
		if(c.getTaskid() != null && !"".equals(c.getTaskid())) {
			column.append(",TASKID");
			value.append(",'"+c.getTaskid()+"'");
		}
		column.append(")");
		value.append(")");
		String sql = column.toString()+value.toString();
		return sql;
	}
}
