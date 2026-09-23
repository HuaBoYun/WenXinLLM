package com.huabo.monitor.mysql.mapper;

import com.hbfk.util.PageInfo;
import com.huabo.monitor.mysql.entity.TblOuterruleMySql;

public class TblOuterruleMapperSqlMySqlConfig {


    public String findListByPageInfoFlow(PageInfo<TblOuterruleMySql> outPageInfo, String flowid) {
        StringBuffer sqlSb = new StringBuffer("SELECT * FROM (SELECT BUDGET.* FROM (SELECT * from TBL_OUTERRULE  o where o.OUTRULID in (SELECT fo.OUTRULID from TBL_FLOW_OUTERRULE fo where fo.flowid='" + flowid + "') ");
        sqlSb.append(" ) BUDGET LIMIT " + outPageInfo.getCurrentPage() +","+ outPageInfo.getPageSize());
        return sqlSb.toString();
    }

    public String findCountByPageInfoFlow(String flowid) {
        StringBuffer sqlSb = new StringBuffer("SELECT COUNT(*) FROM (SELECT * from TBL_OUTERRULE  o where o.OUTRULID in (SELECT fo.OUTRULID from TBL_FLOW_OUTERRULE fo where fo.flowid='" + flowid + "') )");
        return sqlSb.toString();
    }


    public String findInnerruleByFolwid(PageInfo<TblOuterruleMySql> pageInfo, String orgid, String name, String status, String flowid) throws Exception {
        StringBuffer sqlSb = new StringBuffer("SELECT * FROM (SELECT BUDGET.* FROM (SELECT * FROM TBL_OUTERRULE where 1=1 and CREATEORGID=" + orgid + " and outrulid not in (select outrulid from TBL_flow_OUTERRULE where flowid = '" + flowid + "')");

        if (name != null && !"".equals(name)) {
            sqlSb.append(" and RULENAME='" + name + "' ");
        }
        if (status != null && !"".equals(status)) {
            sqlSb.append("and STATUS='" + status + "'");
        }

        sqlSb.append(" ) BUDGET LIMIT " + pageInfo.getCurrentRecord() + " , " + pageInfo.getPageSize());

        return sqlSb.toString();
    }

    public String findCountInnerruleByFolwid(String orgid, String name, String status, String flowid) throws Exception {
        StringBuffer sqlSb = new StringBuffer("SELECT COUNT(*) FROM (SELECT * FROM TBL_OUTERRULE where 1=1 and CREATEORGID=" + orgid + " and outrulid not in (select outrulid from TBL_flow_OUTERRULE where flowid = '" + flowid + "') ");

        if (name != null && !"".equals(name)) {
            sqlSb.append(" and RULENAME='" + name + "' ");
        }
        if (status != null && !"".equals(status)) {
            sqlSb.append("and STATUS='" + status + "' ");
        }
        sqlSb.append(" ) ");
        return sqlSb.toString();
    }

}
