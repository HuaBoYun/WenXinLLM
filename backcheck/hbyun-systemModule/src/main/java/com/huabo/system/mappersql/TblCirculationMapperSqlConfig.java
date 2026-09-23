package com.huabo.system.mappersql;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.huabo.system.entity.TblCirculation;

public class TblCirculationMapperSqlConfig {

    public String findAll(IPage<TblCirculation> page, BigDecimal staffid, TblCirculation tca) {
        StringBuffer sbSql = new StringBuffer("select TCC.*,TCU.RECORDTYPE recordtype,TCU.CONTRACTSTATUS contractstatus from TBL_CIRCULATION TCC LEFT JOIN TBL_CYHW_UNIT TCU ON TCC.TASKID=TO_CHAR(TCU.CONTRACTID) where 1=1 AND (CYSTAFFID = '"+staffid+"' OR CYID IN (SELECT CYID FROM TBL_AUDIT_OPTION WHERE OPT_STAFFID = '"+staffid+"') OR CYID IN (SELECT CIRID FROM TBL_MY_TASK WHERE USRID='"+staffid+"'))  AND CYID = (SELECT MAX(CYID) FROM TBL_CIRCULATION WHERE CYURL = TCC.CYURL)");
        if (tca.getCycode() != null && !"".equals(tca.getCycode())) {
            sbSql.append(" AND CYCODE LIKE '%" + tca.getCycode() + "%'");
        }
        if (tca.getCytype() != null && !"".equals(tca.getCytype())) {
            sbSql.append(" AND CYTYPE LIKE '%" + tca.getCytype() + "%'");
        }

        sbSql.append("  ORDER BY CYDATE DESC ");
        String sql = sbSql.toString();
        return sql;
    }

}
