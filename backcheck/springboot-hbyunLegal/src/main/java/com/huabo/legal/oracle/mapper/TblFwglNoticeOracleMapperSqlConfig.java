package com.huabo.legal.oracle.mapper;

import java.math.BigDecimal;

import com.hbfk.util.DateUtil;
import com.hbfk.util.PageInfo;
import com.huabo.legal.oracle.entity.TblFwglNoticeOracle;

public class TblFwglNoticeOracleMapperSqlConfig {
	 
	
	public String saveEnity(TblFwglNoticeOracle opt) {
		StringBuffer colSb = new StringBuffer("INSERT INTO TBL_FWGL_NOTICE(NOTICEID");
		StringBuffer valSb = new StringBuffer(" VALUES(HIBERNATE_SEQUENCE.nextval");
		
		if(opt.getNoticecode() != null && !"".equals(opt.getNoticecode())) {
			colSb.append(",NOTICECODE");
			valSb.append(",'"+opt.getNoticecode()+"'");
		}
		
		if(opt.getOrgid() != null) {
			colSb.append(",ORGID");
			valSb.append(",'"+opt.getOrgid()+"'");
		}
		
		if(opt.getCreatestaff() != null) {
			colSb.append(",CREATESTAFF");
			valSb.append(",'"+opt.getCreatestaff()+"'");
		}
		
		if(opt.getNoticename() != null && !"".equals(opt.getNoticename())) {
			colSb.append(",NOTICENAME");
			valSb.append(",'"+opt.getNoticename()+"'");
		}
		if(opt.getCreatename() != null && !"".equals(opt.getCreatename())) {
			colSb.append(",CREATENAME");
			valSb.append(",'"+opt.getCreatename()+"'");
		}
		 
		
		if(opt.getCreatetime() != null){
			colSb.append(",CREATETIME");
			valSb.append(",TO_DATE('"+DateUtil.parseDate(opt.getCreatetime(), "yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:MI:SS')");
		}
		
		String sql = colSb.toString()+")"+valSb.toString()+")";
		return sql;
	}
	
	
	public String updateEnity(TblFwglNoticeOracle opt) {
		 StringBuffer sql = new StringBuffer("UPDATE TBL_FWGL_NOTICE SET NOTICECODE = '"+opt.getNoticecode()+"'");
	 
		
		if(opt.getNoticename() != null && !"".equals(opt.getNoticename())) {
			sql.append(" ,NOTICENAME = '"+opt.getNoticename()+"'");
		}
		
		return sql.toString();
	}
	
	
	
	 public String findbyorgidall(PageInfo<TblFwglNoticeOracle> pageInfo,  BigDecimal pid,String code,String name) {
	        StringBuffer sbSql = new StringBuffer("SELECT * FROM ( SELECT BUDGET.*,ROWNUM RNUM FROM (SELECT * " +
	                " FROM TBL_FWGL_NOTICE  " +
	                "WHERE  1=1 " );//ORGID = " + pid
	        if (code != null && !"".equals(code)) {
	            sbSql.append(" AND NOTICECODE LIKE '%" + code + "%'");
	        }
	        if (name != null && !"".contentEquals(name)) {
	            sbSql.append(" AND NOTICENAME LIKE '%" + name + "%'");
	        }

	        sbSql.append(" ORDER BY  NOTICEID DESC) BUDGET WHERE rownum <= "+(pageInfo.getCurrentPage()*pageInfo.getPageSize())+" ) WHERE RNUM > "+pageInfo.getCurrentRecord());
	        String sql = sbSql.toString();
	        return sql;
	    }
	
	  public String findbyorgidallCpount(BigDecimal pid, String code, String name) {
	        StringBuffer sbSql = new StringBuffer("SELECT count(*) FROM TBL_FWGL_NOTICE TCU WHERE 1=1 " );//  ORGID = " + pid
	        if (code != null && !"".equals(code)) {
	            sbSql.append(" AND NOTICECODE LIKE '%" + code + "%'");
	        }
	        if (name != null && !"".contentEquals(name)) {
	            sbSql.append(" AND NOTICENAME LIKE '%" + name + "%'");
	        }

	        sbSql.append(" ORDER BY NOTICEID DESC");
	        String sql = sbSql.toString();
	        return sql;
	    }
}
