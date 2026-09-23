package com.huabo.monitor.mysql.mapper;

public class TblAuditOptionMapperSqlMySqlConfig {


    public String OBJlistBySql(String sheetid, Integer cyid) throws Exception {
        StringBuffer sqlSb = new StringBuffer(" select s.REALNAME,AO.OPT_DESC,OPT_STATE,ao.OPT_STAFFID,AO.CREATE_DATE from TBL_AUDIT_OPTION ao LEFT JOIN TBL_STAFF s on ao.opt_staffid = s.staffid where 1=1 ");
        if (null != sheetid && !"".equals(sheetid)) {
            sqlSb.append("AND ao.RELATION_ID = '" + sheetid + "'");
        }

        if (null != cyid) {
            sqlSb.append("and CYID = " + cyid + "");
        }
        sqlSb.append(" order by ao.OPT_ID  ");
        String sql = sqlSb.toString();
        return sql;
    }
}
