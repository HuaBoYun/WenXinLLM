package com.huabo.audit.oracle.mapper;

import com.hbfk.util.PageInfo;
import com.huabo.audit.oracle.entity.TblYqnsJsxmTzwcqk;

import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;

/**
 * @author wangys
 * @description 针对表【TBL_YQNS_JSXM_TZWCQK(建设项目基本情况表)】的数据库操作Mapper
 * @Entity TblYqnsJsxmTzwcqk
 */
public class TblYqnsJsxmTzwcqkMapperSqlConfig {
	
	public String selectListByjhzgGlId(BigDecimal id, BigDecimal relaId) throws Exception{
		String sql = "SELECT TBL1.* FROM TBL_YQNS_JSXM_TZWCQK TBL1 WHERE ";
		
		if(id != null) {
			sql += " TBL1.JSXMTZWCQKID IN (SELECT FORMID FROM TBL_YQNS_JHCGGL_RELA WHERE GLID = "+id+")";
		}else {
			sql += " TBL1.JSXMTZWCQKID IN (SELECT FORMID FROM TBL_YQNS_JHCHUGGL_RELA WHERE GLID = "+relaId+")";
		}
		
		sql +=" ORDER BY TBL1.JSXMTZWCQKID DESC";
		return sql;
	}
	
	public String selectListByjhchugGlId(BigDecimal id, BigDecimal relaId) throws Exception{
		String sql = "SELECT TBL1.* FROM TBL_YQNS_JSXM_TZWCQK TBL1 WHERE ";
		
		if(id != null) {
			sql += " TBL1.JSXMTZWCQKID IN (SELECT FORMID FROM TBL_YQNS_JHCGGL_RELA WHERE GLID = "+id+")";
		}else {
			sql += " TBL1.JSXMTZWCQKID IN (SELECT FORMID FROM TBL_YQNS_JHCGGL_RELA WHERE GLID = "+relaId+")";
		}
		
		sql +=" ORDER BY TBL1.JSXMTZWCQKID DESC";
		return sql;
	}
	
	public String selectListByjhcgGlId(BigDecimal id, String tbdwName) throws Exception{
		String sql = "SELECT TBL1.* FROM TBL_YQNS_JSXM_TZWCQK TBL1 WHERE ";
		
		if(org.apache.commons.lang.StringUtils.isNotBlank(tbdwName)) {
			sql += " XMSTATUS=0 and TBL1.JSXMTZWCQKID NOT IN (SELECT FORMID FROM TBL_YQNS_JHCGGL_RELA WHERE GLID IN (SELECT ID FROM TBL_YQNS_JHGL_JHCG_GL WHERE GLTYPE = '32')) AND JSXMTZWCQKID IN (SELECT JSXMTZWCQKID FROM TBL_YQNS_JSXM_TZWCQKZJB WHERE HZID IN  (SELECT HZID FROM TBL_YQNS_JSXM_TZWCQKHZ WHERE status = 6 ) ) AND TBL1.TBDW_NAME = '"+tbdwName+"'";
		}else {
			sql += " TBL1.JSXMTZWCQKID IN (SELECT FORMID FROM TBL_YQNS_JHCGGL_RELA WHERE GLID = "+id+")";
		}
		
		sql +=" ORDER BY TBL1.JSXMTZWCQKID DESC";
		return sql;
	}
	

    public String selectCountByPageInfo(PageInfo<TblYqnsJsxmTzwcqk> pageInfo, TblYqnsJsxmTzwcqk vo) {
        StringBuffer sb = new StringBuffer("SELECT COUNT(0) "
                + " FROM TBL_YQNS_JSXM_TZWCQK TBL1 "
                + " WHERE 1=1");

        sqlQuery(vo, sb);

        return sb.toString();
    }


    public String selectListByPageInfo(PageInfo<TblYqnsJsxmTzwcqk> pageInfo, TblYqnsJsxmTzwcqk vo) {
        StringBuffer sb = new StringBuffer("SELECT * FROM "
                + "(SELECT T1.*,ROWNUM RN  FROM "
                + "(SELECT TBL1.* "
                + "FROM TBL_YQNS_JSXM_TZWCQK TBL1 "
                + "WHERE 1=1");

        sqlQuery(vo, sb);

        sb.append(" ORDER BY TBL1.jsxmtzwcqkid DESC) T1 WHERE ROWNUM <= " + (pageInfo.getCurrentRecord() + pageInfo.getPageSize()) + " ) T2 WHERE T2.RN > " + pageInfo.getCurrentRecord());
        return sb.toString();
    }

    private void sqlQuery(TblYqnsJsxmTzwcqk vo, StringBuffer sb) {
        if (vo.getOrgid() != null) {
            sb.append(" and orgid = ").append(vo.getOrgid());
        }

        if (StringUtils.isNotEmpty(vo.getHtbh())) {
            sb.append(" AND TBL1.htbh LIKE '%" + vo.getHtbh() + "%'");
        }

        if (StringUtils.isNotEmpty(vo.getGchfymc())) {
            sb.append(" AND TBL1.gchfymc LIKE '%" + vo.getGchfymc() + "%'");
        }

        if (StringUtils.isNotEmpty(vo.getJhwh())) {
            sb.append(" AND TBL1.jhwh LIKE '%" + vo.getJhwh() + "%'");
        }
        if (StringUtils.isNotEmpty(vo.getSsdw())) {
            sb.append(" AND TBL1.Ssdw LIKE '%" + vo.getSsdw() + "%'");
        }

        if (vo.getStartDate() != null && vo.getStartDate().length() > 0) {
            sb.append(" AND TBL1.cjsj >= TO_DATE('" + vo.getStartDate() + " 00:00:00','yyyy-mm-dd HH24:MI:SS')");
        }

        if (vo.getEndDate() != null && vo.getEndDate().length() > 0) {
            sb.append(" AND TBL1.cjsj <= TO_DATE('" + vo.getEndDate() + " 23:59:59','yyyy-mm-dd HH24:MI:SS')");
        }
    }

    
    public String selectListByIdall(BigDecimal id) throws Exception{
		String sql = "SELECT TBL1.* FROM TBL_YQNS_JSXM_TZWCQK TBL1 WHERE ";
		sql += " TBL1.JSXMTZWCQKID IN (SELECT FORMID FROM TBL_YQNS_JHCGGL_RELA  WHERE GLID = "+id+")";// TBL_YQNS_JHZGGL_RELA
		sql +=" ORDER BY TBL1.JSXMTZWCQKID DESC";
		return sql;
	}
    
    
    
    public String selectListByIdmyrw(BigDecimal id,BigDecimal staffid) throws Exception{
  		String sql = "SELECT TBL1.* FROM TBL_YQNS_JSXM_TZWCQK TBL1 WHERE ";
  		sql += " TBL1.JSXMTZWCQKID IN (SELECT FORMID FROM TBL_YQNS_JHCGGL_RELA WHERE GLID = "+id+") and (TBL1.FZSTATUS!=1 or TBL1.FZSTATUS is NULL) and RWIDS like '%"+staffid+"%'";
  		sql += " or ( TBL1.PARENTID IN (SELECT FORMID FROM TBL_YQNS_JHCGGL_RELA WHERE GLID = "+id+")and (TBL1.FZSTATUS!=1 or TBL1.FZSTATUS is NULL) and RWIDS like '%"+staffid+"%' )";
  		sql +=" ORDER BY TBL1.JSXMTZWCQKID DESC";
  		return sql;
  	}
      
    
    public String selectListByIdallcf(BigDecimal id) throws Exception{
		String sql = "SELECT TBL1.* FROM TBL_YQNS_JSXM_TZWCQK TBL1 WHERE ";
		sql += " TBL1.PARENTID  = "+id ;
		sql +=" ORDER BY TBL1.JSXMTZWCQKID DESC";
		return sql;
	}
}




