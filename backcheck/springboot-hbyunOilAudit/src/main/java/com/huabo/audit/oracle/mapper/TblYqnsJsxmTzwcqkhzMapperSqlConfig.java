package com.huabo.audit.oracle.mapper;

import com.hbfk.util.database.DataBaseSqlConfig;

public class TblYqnsJsxmTzwcqkhzMapperSqlConfig {

	public String selectJsxmtzPage() throws Exception{
		String sql = "SELECT "+DataBaseSqlConfig.getConcatColumn("TBDW_NAME", "'竣工决算审计'")+" AS PROJECTNAME,TBDW_NAME AS RELAORGNAME,COUNT(0) AS PROJECTCOUNT,SUM(JSJE) AS PROJECTAMOUNT FROM TBL_YQNS_JSXM_TZWCQK " + 
				"WHERE JSXMTZWCQKID NOT IN (SELECT FORMID FROM TBL_YQNS_JHCGGL_RELA WHERE GLID IN (SELECT ID FROM TBL_YQNS_JHGL_JHCG_GL WHERE GLTYPE = '32')) AND JSXMTZWCQKID IN  (SELECT JSXMTZWCQKID FROM TBL_YQNS_JSXM_TZWCQKZJB WHERE HZID IN  (SELECT HZID FROM TBL_YQNS_JSXM_TZWCQKHZ WHERE status = 6 ) ) " + 
				"GROUP BY TBDW_NAME";
		return sql;
	}
}




