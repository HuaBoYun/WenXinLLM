package com.huabo.audit.oracle.mapper;

import org.apache.commons.lang.StringUtils;

import com.hbfk.util.database.DataBaseSqlConfig;

public class TblYqnsJhglJhcgGLMapperSqlConfig {

    public String selectGcxmjshzPage() throws Exception {
        String sql = "SELECT " + DataBaseSqlConfig.getConcatColumn("GC.JSDW", "'工程结算审计'") + " AS PROJECTNAME,GC.JSDW AS RELAORGNAME,COUNT(GC.HTBH) AS PROJECTCOUNT,SUM(GC.ESSCJE) AS PROJECTAMOUNT FROM TBL_YQNS_GCXMZJ_ZJB ZJB LEFT JOIN TBL_YQNS_GCXMZJ GC ON ZJB.GCXMZJID = GC.GCXMZJID " +
                " WHERE (ZJB.NWB != '工程建设公司'    and  ZJB.NWB != '工程建设' )  and GC.XMSTATUS=0    AND ZJB.GCXMZJZJBID NOT IN (SELECT FORMID FROM TBL_YQNS_JHCGGL_RELA WHERE GLID IN (SELECT ID FROM TBL_YQNS_JHGL_JHCG_GL WHERE GLTYPE = '31')) " +
                " GROUP BY GC.JSDW";
        return sql;
    }

    public String selectJsxmtzPage() throws Exception {
        String sql = "SELECT " + DataBaseSqlConfig.getConcatColumn("TBDW_NAME", "'竣工决算审计'") + " AS PROJECTNAME,TBDW_NAME AS RELAORGNAME,COUNT(0) AS PROJECTCOUNT,SUM(JSJE) AS PROJECTAMOUNT FROM TBL_YQNS_JSXM_TZWCQK " +
                "WHERE  XMSTATUS=0 and JSXMTZWCQKID NOT IN (SELECT FORMID FROM TBL_YQNS_JHCGGL_RELA WHERE GLID IN (SELECT ID FROM TBL_YQNS_JHGL_JHCG_GL WHERE GLTYPE = '32')) AND JSXMTZWCQKID IN  (SELECT JSXMTZWCQKID FROM TBL_YQNS_JSXM_TZWCQKZJB WHERE HZID IN  (SELECT HZID FROM TBL_YQNS_JSXM_TZWCQKHZ WHERE status = 6 ) ) " +
                "GROUP BY TBDW_NAME";
        return sql;
    }

    public String getJsxmjgjsPage(String year) throws Exception {
        String sql = "SELECT " + DataBaseSqlConfig.getConcatColumn("TBDW_NAME", "'竣工决算审计'") + " AS PROJECTNAME,TBDW_NAME AS RELAORGNAME,COUNT(0) AS PROJECTCOUNT,SUM(JSJE) AS PROJECTAMOUNT FROM TBL_YQNS_JSXM_TZWCQK ";
        if (StringUtils.isNotBlank(year)) {
            sql += "WHERE TO_CHAR( CJSJ , 'YYYY') = '" + year + "'";
        }
//			sql+=	"WHERE JSXMTZWCQKID NOT IN (SELECT FORMID FROM TBL_YQNS_JHCGGL_RELA WHERE GLID IN (SELECT ID FROM TBL_YQNS_JHGL_JHCG_GL WHERE GLTYPE = '32')) AND JSXMTZWCQKID IN  (SELECT JSXMTZWCQKID FROM TBL_YQNS_JSXM_TZWCQKZJB WHERE HZID IN  (SELECT HZID FROM TBL_YQNS_JSXM_TZWCQKHZ WHERE status = 6 ) ) " +
        sql += "GROUP BY TBDW_NAME";
        return sql;
    }


    public String selectSjdwlrsjSbList() throws Exception {
        String sql = "SELECT " + DataBaseSqlConfig.getConcatColumn("FAO.ORGNAME", "'所属三级单位离任经济责任审计'") + " AS PROJECTNAME, FAO.ORGID AS RELAORGID ,FAO.ORGNAME AS RELAORGNAME,COUNT(TYLA.NAME)  AS PROJECTCOUNT FROM TBL_YQNS_LEAVE_AUDIT_3L TYLA LEFT JOIN TBL_ORGANIZATION ORG ON TYLA.OLD_ORG_ID = ORG.ORGID " +
                " LEFT JOIN TBL_ORGANIZATION FAO ON ORG.FATHERORGID = FAO.ORGID " +
                " WHERE TYLA.ID NOT IN (SELECT FORMID FROM TBL_YQNS_JHCGGL_RELA WHERE GLID IN (SELECT ID FROM TBL_YQNS_JHGL_JH_GL WHERE GLTYPE = '23')) AND TYLA.ID IN (select NRID from TBL_YQNS_LEAVE_AUDIT_JD3L_GL where JDID IN ( SELECT JDID FROM TBL_YQNS_LEAVE_AUDIT_JD3L WHERE STATUS = 6 )) " +
                " GROUP BY FAO.ORGID,FAO.ORGNAME";
        return sql;
    }

    public String selectSjdwlrsjSbListByJhcgId(String jhcgid, String relaid, String glType) throws Exception {
        String sql = "SELECT * FROM TBL_YQNS_JHGL_JHCG_GL WHERE GLTYPE = '" + glType + "' AND JHCGID = " + jhcgid + " AND ID NOT IN (SELECT RELAID FROM TBL_YQNS_JHGL_JHCHUG_GL WHERE RELAID IS NOT NULL) ";
        if (StringUtils.isNotBlank(relaid)) {
            sql += "AND ID NOT IN (" + relaid + ")";
        }
        return sql;
    }
}




