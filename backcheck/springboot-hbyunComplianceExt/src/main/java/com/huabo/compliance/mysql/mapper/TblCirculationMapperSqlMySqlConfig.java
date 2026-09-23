package com.huabo.compliance.mysql.mapper;

import com.hbfk.util.PageInfo;
import com.huabo.compliance.entity.TblCirculation;

import java.math.BigDecimal;

public class TblCirculationMapperSqlMySqlConfig {

    public String findAll(PageInfo<TblCirculation> pageInfo, BigDecimal staffid, TblCirculation tca) {
        StringBuffer sbSql = new StringBuffer("SELECT * FROM ( SELECT BUDGET.*,@rownum:=@rownum+1 rnum FROM ( select TCC.*,TCU.RECORDTYPE recordtype,TCU.CONTRACTSTATUS contractstatus from TBL_CIRCULATION TCC LEFT JOIN TBL_CYHW_UNIT TCU ON TCC.TASKID= cast(TCU.CONTRACTID as char) where 1=1 AND (CYSTAFFID = '" + staffid + "' OR CYID IN (SELECT CYID FROM TBL_AUDIT_OPTION WHERE OPT_STAFFID = '" + staffid + "')) and  (  DEFINITIONID like '%SJ_JHTZD%' or DEFINITIONID like '%SJ_SSQRS%' or DEFINITIONID like '%SJ_XMGL%' OR DEFINITIONID LIKE '%SJ_ZQYJ%' or DEFINITIONID like '%SJ_DGFH%' or DEFINITIONID like '%SJ_JHGL%' or DEFINITIONID like '%SJ_SJBG%'  or DEFINITIONID like '%cyhw_xmzc%' or DEFINITIONID like '%cyhw_jjhtjc%' )  AND CYID = (SELECT MAX(CYID) FROM TBL_CIRCULATION WHERE CYURL = TCC.CYURL)");

        if (tca.getCycode() != null && !"".equals(tca.getCycode())) {
            sbSql.append(" AND CYCODE LIKE '%" + tca.getCycode() + "%'");
        }
        if (tca.getCytype() != null && !"".equals(tca.getCytype())) {
            sbSql.append(" AND CYTYPE LIKE '%" + tca.getCytype() + "%'");
        }

        sbSql.append("  ORDER BY CYDATE DESC  ) BUDGET limit " + pageInfo.getCurrentRecord() + " , " + pageInfo.getPageSize() + " ) as a ");
        String sql = sbSql.toString();
        return sql;
    }

    public String findAllCount(BigDecimal staffid, TblCirculation tca) {
        StringBuffer sbSql = new StringBuffer("select COUNT(*) from TBL_CIRCULATION TCC where 1=1 AND (CYSTAFFID = '" + staffid + "' OR CYID IN (SELECT CYID FROM TBL_AUDIT_OPTION WHERE OPT_STAFFID = '" + staffid + "')) and  (  DEFINITIONID like '%SJ_JHTZD%' or DEFINITIONID like '%SJ_SSQRS%' or DEFINITIONID like '%SJ_XMGL%' OR DEFINITIONID LIKE '%SJ_ZQYJ%' or DEFINITIONID like '%SJ_DGFH%' or DEFINITIONID like '%SJ_JHGL%' or DEFINITIONID like '%SJ_SJBG%'  or DEFINITIONID like '%cyhw_xmzc%' or DEFINITIONID like '%cyhw_jjhtjc%' )  AND CYID = (SELECT MAX(CYID) FROM TBL_CIRCULATION WHERE CYURL = TCC.CYURL)");

        if (tca.getCycode() != null && !"".equals(tca.getCycode())) {
            sbSql.append(" AND CYCODE LIKE '%" + tca.getCycode() + "%'");
        }
        if (tca.getCytype() != null && !"".equals(tca.getCytype())) {
            sbSql.append(" AND CYTYPE LIKE '%" + tca.getCytype() + "%'");
        }

        // sbSql.append(" ORDER BY CYDATE DESC  ) BUDGET limit "+(pageInfo.getCurrentPage()*pageInfo.getPageSize())+" ) WHERE RNUM > "+pageInfo.getCurrentRecord());
        String sql = sbSql.toString();
        return sql;
    }
}
