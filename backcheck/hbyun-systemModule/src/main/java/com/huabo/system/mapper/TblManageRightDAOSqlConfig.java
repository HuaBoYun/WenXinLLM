package com.huabo.system.mapper;

import com.hbfk.util.DateUtil;
import com.hbfk.util.PageInfo;
import com.hbfk.util.database.DataBaseSqlConfig;
import com.huabo.system.entity.TblAcquisitionRecord;
import com.huabo.system.entity.TblManageRight;

import java.math.BigDecimal;

public class TblManageRightDAOSqlConfig {
	
	public String selectWorkRightInfo(String rightname) throws Exception{
		String sql = "SELECT * from TblManageRight WHERE rightname = #{rightname} and fatherrightid=1  AND indicatorstatus = 1 "+DataBaseSqlConfig.getRowLimitSql(0, 1);
		return sql;
	}
	
	public String selectChildrenRightListByUser(BigDecimal rightId, BigDecimal staffid, BigDecimal orgid) throws Exception{
		String sql = "SELECT TMR.RIGHTID,"+DataBaseSqlConfig.getNullColumn("TORN.RIGHTNAME", "TMR.RIGHTNAME")+" AS RIGHTNAME,TMR.RIGHTURL,TMR.FATHERRIGHTID,TMR.FUNCORDER,TMR.INDICATORSTATUS FROM TBL_MANAGE_RIGHT TMR LEFT JOIN TBL_MANAGE_USER_RIGHT TMUR ON TMR.RIGHTID = TMUR.RIGHTID LEFT JOIN TBL_ORG_RIGHT_NEW TORN ON TMR.RIGHTID = TORN.RIGHTID AND TORN.ORGID = "+orgid
				+ " WHERE TMR.INDICATORSTATUS = 1 AND TMR.FATHERRIGHTID = "+rightId+" AND TORN.INDICATORSTATUS = 1 AND TMUR.STAFFID = "+staffid+" ORDER BY FUNCORDER ASC";
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
