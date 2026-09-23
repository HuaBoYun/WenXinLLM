package com.huabo.compliance.mapper;

import com.hbfk.util.PageInfo;
import com.huabo.compliance.entity.TblManageRight;

import java.math.BigDecimal;

public class TblManageRightDAOSqlConfig {


//    public String selectListSql(PageInfo<TblManageRight> pageInfo, BigDecimal orgid) {
//        StringBuffer sbSql = new StringBuffer("SELECT RIGHTID,RIGHTNAME,RIGHTURL,LEAF,FATHERRIGHTID,FUNCORDER,SUBSYSTEM,RIGHTCODE,MEMO,CSSCLASS,INDICATORSTATUS,CUSTOMPAGE,RIGHTDESC,RIGHTISBZ,RIGHTCONTENT FROM ( ");
//        sbSql.append("SELECT T1.RIGHTID,T1.RIGHTNAME,T1.RIGHTURL,T1.LEAF,T1.FATHERRIGHTID,T1.FUNCORDER,T1.SUBSYSTEM,T1.RIGHTCODE,T1.MEMO,T1.CSSCLASS,T1.INDICATORSTATUS,T1.CUSTOMPAGE,T1.RIGHTDESC,T1.RIGHTISBZ,T1.RIGHTCONTENT,ROWNUM RNUM FROM ( ");
//        sbSql.append("SELECT T1.RIGHTID,T1.RIGHTNAME,T1.RIGHTURL,T1.LEAF,T1.FATHERRIGHTID,T1.FUNCORDER,T1.SUBSYSTEM,T1.RIGHTCODE,T1.MEMO,T1.CSSCLASS,T1.INDICATORSTATUS,T1.CUSTOMPAGE,T1.RIGHTDESC,T1.RIGHTISBZ,T1.RIGHTCONTENT,ROWNUM RNUM FROM ( ");
//        sbSql.append("select TMR.RIGHTID,TMR.RIGHTNAME FROM TBL_MANAGE_RIGHT TMR WHERE TMR.FATHERRIGHTID = 1 AND TMR.RIGHTID IN (SELECT RIGHTID FROM TBL_ORG_RIGHT_NEW WHERE ORGID = "+orgid+" AND INDICATORSTATUS = 1) AND RIGHTID NOT IN (570,101,252)AND RIGHTNAME like '%RIGHTNAME%' ORDER BY TMR.FUNCORDER ASC , TMR.RIGHTID ASC");
//        sbSql.append(") T1 WHERE ROWNUM <= "+(pageInfo.getCurrentPage()*pageInfo.getPageSize())+" ) T2 WHERE T2.RNUM > "+pageInfo.getCurrentRecord());
//        return sbSql.toString();
//    }

	public String selectListSql(PageInfo<TblManageRight> pageInfo, BigDecimal orgid) {
		StringBuffer sbSql = new StringBuffer("SELECT * FROM ( SELECT BUDGET.*,ROWNUM RNUM FROM ( SELECT RIGHTID,RIGHTNAME FROM TBL_MANAGE_RIGHT TMR WHERE TMR.FATHERRIGHTID = 1 AND TMR.RIGHTID IN (SELECT RIGHTID FROM TBL_ORG_RIGHT_NEW WHERE ORGID = '"+orgid+"' AND INDICATORSTATUS = 1) AND RIGHTID NOT IN (570,101,252) ");


		sbSql.append(" ORDER BY TMR.FUNCORDER ASC , TMR.RIGHTID ASC) BUDGET WHERE rownum <= "+(pageInfo.getCurrentPage()*pageInfo.getPageSize())+" ) WHERE RNUM > "+pageInfo.getCurrentRecord());
		String sql = sbSql.toString();
		return sql;
	}

    public String countRightid( BigDecimal rightid) {
		StringBuffer sbSql = new StringBuffer("SELECT COUNT(*) FROM TBL_MANAGE_RIGHT TMR WHERE TMR.FATHERRIGHTID = 1 AND TMR.RIGHTID IN (SELECT RIGHTID FROM TBL_ORG_RIGHT_NEW WHERE ORGID = '"+rightid+"' AND INDICATORSTATUS = 1) AND RIGHTID NOT IN (570,101,252) ");


		sbSql.append(" ORDER BY TMR.FUNCORDER ASC , TMR.RIGHTID ASC ");
		String sql = sbSql.toString();
		return sql;
    }
    
    
    public String insertTblManageRight(TblManageRight right) {
    	StringBuffer column = new StringBuffer("INSERT INTO TBL_MANAGE_RIGHT (RIGHTID");
		StringBuffer value = new StringBuffer(" VALUES (MANAGERIGHT_SEQUENCE.nextval");
				
		if(right.getRightname() != null && !"".equals(right.getRightname())) {
			column.append(",RIGHTNAME");
			value.append(",'"+right.getRightname()+"'");
		}
		if(right.getRighturl() != null && !"".equals(right.getRighturl())) {
			column.append(",RIGHTURL");
			value.append(",'"+right.getRighturl()+"'");
		}
		if(right.getLeaf() != null) {
			column.append(",LEAF");
			value.append(",'"+right.getLeaf()+"'");
		}
		if(right.getFatherrightid() != null) {
			column.append(",FATHERRIGHTID");
			value.append(",'"+right.getFatherrightid()+"'");
		}
		if(right.getFuncorder() != null) {
			column.append(",FUNCORDER");
			value.append(",'"+right.getFuncorder()+"'");
		}
		if(right.getRightcode() != null && !"".equals(right.getRightcode())) {
			column.append(",RIGHTCODE");
			value.append(",'"+right.getRightcode()+"'");
		}
		if(right.getMemo() != null && !"".equals(right.getMemo())) {
			column.append(",MEMO");
			value.append(",'"+right.getMemo()+"'");
		}
		if(right.getCssClass() != null  && !"".equals(right.getCssClass())) {
			column.append(",CSSCLASS");
			value.append(",'"+right.getCssClass()+"'");
		}
		if(right.getRightcontent() != null  && !"".equals(right.getRightcontent())) {
			column.append(",RIGHTCONTENT");
			value.append(",'"+right.getRightcontent()+"'");
		}
		if(right.getIndicatorstatus() != null) {
			column.append(",INDICATORSTATUS");
			value.append(",'"+right.getIndicatorstatus()+"'");
		}
		if(right.getCustompage() != null) {
			column.append(",CUSTOMPAGE");
			value.append(",'"+right.getCustompage()+"'");
		}
		if(right.getRightdesc() != null && !"".equals(right.getRightdesc())) {
			column.append(",RIGHTDESC");
			value.append(",'"+right.getRightdesc()+"'");
		}
		if(right.getRightisbz() != null) {
			column.append(",RIGHTISBZ");
			value.append(",'"+right.getRightisbz()+"'");
		}
		if(right.getRightImgUrl() != null && !"".equals(right.getRightImgUrl())) {
			column.append(",RIGHTIMGURL");
			value.append(",'"+right.getRightImgUrl()+"'");
		}
		if(right.getRightModuleType() != null && !"".equals(right.getRightModuleType())) {
			column.append(",RIGHTMODULETYPE");
			value.append(",'"+right.getRightModuleType()+"'");
		}
		column.append(")");
		value.append(")");
		String sql = column.toString()+value.toString();
		return sql;
    }
    
    
    public String updateTblManageRight(TblManageRight right) {
    	StringBuffer sql = new StringBuffer("UPDATE TBL_MANAGE_RIGHT SET ");
		
		if(right.getRightname() != null && !"".equals(right.getRightname())) {
			sql.append("RIGHTNAME = '"+right.getRightname()+"' ,");
		}
		if(right.getRighturl() != null && !"".equals(right.getRighturl())) {
			sql.append("RIGHTURL = '"+right.getRighturl()+"',");
		}
		if(right.getLeaf() != null) {
			sql.append("LEAF = '"+right.getLeaf()+"',");
		}
		if(right.getFatherrightid() != null) {
			sql.append("FATHERRIGHTID = '"+right.getFatherrightid()+"',");
		}
		if(right.getFuncorder() != null) {
			sql.append("FUNCORDER = '"+right.getFuncorder()+"',");
		}
		if(right.getRightcode() != null && !"".equals(right.getRightcode())) {
			sql.append("RIGHTCODE = '"+right.getRightcode()+"',");
		}
		if(right.getMemo() != null && !"".equals(right.getMemo())) {
			sql.append("MEMO = '"+right.getMemo()+"',");
		}
		if(right.getCssClass() != null  && !"".equals(right.getCssClass())) {
			sql.append("CSSCLASS = '"+right.getCssClass()+"',");
		}
		if(right.getIndicatorstatus() != null) {
			sql.append("INDICATORSTATUS = '"+right.getIndicatorstatus()+"',");
		}
		if(right.getCustompage() != null) {
			sql.append("CUSTOMPAGE = '"+right.getCustompage()+"',");
		}
		if(right.getRightdesc() != null && !"".equals(right.getRightdesc())) {
			sql.append("RIGHTDESC = '"+right.getRightdesc()+"',");
		}
		if(right.getRightisbz() != null) {
			sql.append("RIGHTISBZ = '"+right.getRightisbz()+"',");
		}
		if(right.getRightImgUrl() != null && !"".equals(right.getRightImgUrl())) {
			sql.append(",RIGHTIMGURL = '"+right.getRightImgUrl()+"',");
		}
		if(right.getRightcontent() != null && !"".equals(right.getRightcontent())) {
			sql.append(",RIGHTCONTENT = '"+right.getRightcontent()+"',");
		}
		if(right.getRightModuleType() != null && !"".equals(right.getRightModuleType())) {
			sql.append(",RIGHTMODULETYPE = '"+right.getRightModuleType()+"',");
		}
		sql.deleteCharAt(sql.length()-1);
		sql.append(" WHERE RIGHTID = "+right.getRightid());
		return sql.toString();
    }
}
