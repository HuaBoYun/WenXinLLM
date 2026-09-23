package com.huabo.system.mappersql;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hbfk.util.DateUtil;
import com.hbfk.util.PageInfo;
import com.hbfk.util.database.DataBaseSqlConfig;
import com.huabo.system.entity.TblContractTypeof;
import com.huabo.system.entity.TblCourse;
import com.huabo.system.entity.TblVideoType;

import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;

public class TblCourseMapperSqlConifg {
	
	public String selectListByPageInfo(IPage<TblCourse> page, BigDecimal orgid, String coursename1, String coursetype1) {
		StringBuffer sqlSb = new StringBuffer("SELECT * from TBL_COURSE where PARENTID IS NULL AND ORGID="+orgid);
		if(!StringUtils.isBlank(coursename1)){
			sqlSb.append(" AND COURSENAME LIKE '%"+coursename1.trim()+"%'");
		}
		if(!StringUtils.isBlank(coursetype1)){
			sqlSb.append(" AND COURSETYPE LIKE '%"+coursetype1.trim()+"%'");
		}
		sqlSb.append(" ORDER BY COURSEID DESC ");
		String sql = sqlSb.toString();
		return sql;
	}
	
	public String updatetblCourse(TblCourse tblCourse) throws Exception {
		StringBuffer sql = new StringBuffer("UPDATE TBL_COURSE SET COURSENAME = '"+tblCourse.getCoursename()+"'");

		if(tblCourse.getMemo() != null && !"".equals(tblCourse.getMemo())) {
			sql.append(" , MEMO = '"+tblCourse.getMemo()+"'");
		}
		if(tblCourse.getParentid() != null && !"".equals(tblCourse.getParentid())) {
			sql.append(" , PARENTID = '"+tblCourse.getParentid()+"'");
		}
		if(tblCourse.getPicurl() != null && !"".equals(tblCourse.getPicurl())) {
			sql.append(" , PICURL = '"+tblCourse.getPicurl()+"'");
		}
		if(tblCourse.getVideourl() != null && !"".equals(tblCourse.getVideourl())) {
			sql.append(" , VIDEORUL = '"+tblCourse.getVideourl()+"'");
		}
		if(tblCourse.getUserid() != null && !"".equals(tblCourse.getUserid())) {
			sql.append(" , USERID = '"+tblCourse.getUserid()+"'");
		}
		if(tblCourse.getOrgid() != null && !"".equals(tblCourse.getOrgid())) {
			sql.append(" , ORGID = '"+tblCourse.getOrgid()+"'");
		}
		if(tblCourse.getCreateDate() != null) {
			sql.append(" ,CREATEDATE = ").append(DataBaseSqlConfig.getDateStrFormat(tblCourse.getCreateDate()));
		}
		if(tblCourse.getCoursetype() != null && !"".equals(tblCourse.getCoursetype())) {
			sql.append(" , COURSETYPE = '"+tblCourse.getCoursetype()+"'");
		}
		if(tblCourse.getCoursenumber() != null) {
			sql.append(" , COURSENUMBER = '"+tblCourse.getCoursenumber()+"'");
		}
		if(tblCourse.getType() != null && !"".equals(tblCourse.getType())) {
			sql.append(" , TYPE = '"+tblCourse.getType()+"'");
		}
		if(tblCourse.getCourseware() != null && !"".equals(tblCourse.getCourseware())) {
			sql.append(" , COURSEWARE = '"+tblCourse.getCourseware()+"'");
		}
		if(tblCourse.getCoursewareurl() != null && !"".equals(tblCourse.getCoursewareurl())) {
			sql.append(" , COURSEWAREURL = '"+tblCourse.getCoursewareurl()+"'");
		}
		sql.append(" WHERE COURSEID = '"+tblCourse.getCourseid()+"'");
		return sql.toString();
	}

	public String savetblCourse(TblCourse tblCourse) throws Exception {
		StringBuffer column = new StringBuffer("INSERT INTO TBL_COURSE (COURSEID");
		StringBuffer value = new StringBuffer(" VALUES (").append(tblCourse.getCourseid());

		if(tblCourse.getCoursename() != null) {
			column.append(",COURSENAME");
			value.append(",'"+tblCourse.getCoursename()+"'");
		}
		if(tblCourse.getMemo() != null) {
			column.append(",MEMO");
			value.append(",'"+tblCourse.getMemo()+"'");
		}
		if(tblCourse.getParentid() != null) {
			column.append(",PARENTID");
			value.append(",'"+tblCourse.getParentid()+"'");
		}
		if(tblCourse.getPicurl() != null) {
			column.append(",PICURL");
			value.append(",'"+tblCourse.getPicurl()+"'");
		}
		if(tblCourse.getVideourl() != null) {
			column.append(",VIDEORUL");
			value.append(",'"+tblCourse.getVideourl()+"'");
		}
		if(tblCourse.getUserid() != null) {
			column.append(",USERID");
			value.append(",'"+tblCourse.getUserid()+"'");
		}
		if(tblCourse.getOrgid() != null) {
			column.append(",ORGID");
			value.append(",'"+tblCourse.getOrgid()+"'");
		}
		if(tblCourse.getCreateDate() != null) {
			column.append(",CREATEDATE");
			value.append(",").append(DataBaseSqlConfig.getDateStrFormat(tblCourse.getCreateDate()));
		}
		if(tblCourse.getCoursetype() != null) {
			column.append(",COURSETYPE");
			value.append(",'"+tblCourse.getCoursetype()+"'");
		}
		if(tblCourse.getCoursenumber() != null ) {
			column.append(",COURSENUMBER");
			value.append(",'"+tblCourse.getCoursenumber()+"'");
		}
		if(tblCourse.getType() != null) {
			column.append(",TYPE");
			value.append(",'"+tblCourse.getType()+"'");
		}
		if(tblCourse.getCourseware() != null) {
			column.append(",COURSEWARE");
			value.append(",'"+tblCourse.getCourseware()+"'");
		}
		if(tblCourse.getCoursewareurl() != null) {
			column.append(",COURSEWAREURL");
			value.append(",'"+tblCourse.getCoursewareurl()+"'");
		}
		column.append(")");
		value.append(")");
		String sql = column.toString()+value.toString();
		return sql;
	}





	

	
}
