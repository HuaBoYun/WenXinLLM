package com.huabo.legal.oracle.mapper;

import java.math.BigDecimal;

import com.hbfk.util.DateUtil;
import com.hbfk.util.PageInfo;
import com.huabo.legal.oracle.entity.TblFwglLearrningOracle;

public class TblFwglLearrningOracleMapperSqlConfig {
	 
	
	public String saveEnity(TblFwglLearrningOracle opt) {
		StringBuffer colSb = new StringBuffer("INSERT INTO TBL_FWGL_LEARRNING(LINGID");
		StringBuffer valSb = new StringBuffer(" VALUES(HIBERNATE_SEQUENCE.nextval");
		
		if(opt.getLingcode() != null && !"".equals(opt.getLingcode())) {
			colSb.append(",LINGCODE");
			valSb.append(",'"+opt.getLingcode()+"'");
		}
		
		if(opt.getOrgid() != null) {
			colSb.append(",ORGID");
			valSb.append(",'"+opt.getOrgid()+"'");
		}
		
		if(opt.getCreatestaff() != null) {
			colSb.append(",CREATESTAFF");
			valSb.append(",'"+opt.getCreatestaff()+"'");
		}
		
		if(opt.getLingdname() != null && !"".equals(opt.getLingdname())) {
			colSb.append(",LINGDNAME");
			valSb.append(",'"+opt.getLingdname()+"'");
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
	
	
	public String updateEnity(TblFwglLearrningOracle opt) {
		 StringBuffer sql = new StringBuffer("UPDATE TBL_FWGL_LEARRNING SET LINGCODE = '"+opt.getLingcode()+"'");
	 
		
		if(opt.getLingdname() != null && !"".equals(opt.getLingdname())) {
			sql.append(" ,LINGDNAME = '"+opt.getLingdname()+"'");
		}
		
		return sql.toString();
	}
	
	
	
	 public String findbyorgidall(PageInfo<TblFwglLearrningOracle> pageInfo,  BigDecimal pid,String code,String name) {
	        StringBuffer sbSql = new StringBuffer("SELECT * FROM ( SELECT BUDGET.*,ROWNUM RNUM FROM (SELECT * " +
	                " FROM TBL_FWGL_LEARRNING  " +
	                "WHERE  1=1 " );//ORGID = " + pid
	        if (code != null && !"".equals(code)) {
	            sbSql.append(" AND LINGCODE LIKE '%" + code + "%'");
	        }
	        if (name != null && !"".contentEquals(name)) {
	            sbSql.append(" AND LINGDNAME LIKE '%" + name + "%'");
	        }

	        sbSql.append(" ORDER BY  LINGID DESC) BUDGET WHERE rownum <= "+(pageInfo.getCurrentPage()*pageInfo.getPageSize())+" ) WHERE RNUM > "+pageInfo.getCurrentRecord());
	        String sql = sbSql.toString();
	        return sql;
	    }
	
	  public String findbyorgidallCpount(BigDecimal pid, String code, String name) {
	        StringBuffer sbSql = new StringBuffer("SELECT count(*) FROM TBL_FWGL_LEARRNING TCU WHERE 1=1 " );//  ORGID = " + pid
	        if (code != null && !"".equals(code)) {
	            sbSql.append(" AND LINGCODE LIKE '%" + code + "%'");
	        }
	        if (name != null && !"".contentEquals(name)) {
	            sbSql.append(" AND LINGDNAME LIKE '%" + name + "%'");
	        }

	        sbSql.append(" ORDER BY LINGID DESC");
	        String sql = sbSql.toString();
	        return sql;
	    }
}
