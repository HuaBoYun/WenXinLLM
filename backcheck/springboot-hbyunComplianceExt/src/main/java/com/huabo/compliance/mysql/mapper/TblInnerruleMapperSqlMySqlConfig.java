package com.huabo.compliance.mysql.mapper;

import com.hbfk.util.PageInfo;
import com.huabo.compliance.mysql.entity.TblInnerruleMySql;

public class TblInnerruleMapperSqlMySqlConfig {


    public String findListByPageInfoFlow(PageInfo<TblInnerruleMySql> pageInfo, String flowid) {
        StringBuffer sqlSb = new StringBuffer("SELECT * FROM (SELECT BUDGET.* FROM (SELECT INNRULID,RULENAME,RULENUMBER,ORG.ORGNAME,PUBLISHDATE,BODYINFO  from TBL_INNERRULE inn LEFT JOIN TBL_ORGANIZATION org on INN.PUBLISHORG=ORG.ORGID WHERE inn.INNRULID in( SELECT tb.INNRULID from TBL_FLOW_INNERRULE tb where tb.flowid='" + flowid + "') ");
        sqlSb.append(" ) BUDGET LIMIT " + pageInfo.getCurrentRecord() + " , " + pageInfo.getPageSize());
        return sqlSb.toString();
    }

    public String findCountByPageInfoFlow(String flowid) {
        StringBuffer sqlSb = new StringBuffer(" SELECT COUNT(*) FROM (SELECT INNRULID,RULENAME,RULENUMBER,ORG.ORGNAME,PUBLISHDATE  from TBL_INNERRULE inn LEFT JOIN TBL_ORGANIZATION org on INN.PUBLISHORG=ORG.ORGID WHERE inn.INNRULID in( SELECT tb.INNRULID from TBL_FLOW_INNERRULE tb where tb.flowid='" + flowid + "') )");
        return sqlSb.toString();
    }


    public String findInnerruleByFolwid(PageInfo<TblInnerruleMySql> pageInfo, String orgid, String flowid) {
        StringBuffer sqlSb = new StringBuffer("SELECT * FROM (SELECT BUDGET.* FROM (SELECT INNRULID,RULECODE,RULENAME,RULENUMBER,PUBLISHDATE,ORGNAME  FROM TBL_INNERRULE ru LEFT JOIN TBL_ORGANIZATION org on RU.PUBLISHORG=ORG.ORGID WHERE PUBLISHORG in (select cast(ORGID as char) from TBL_ORGANIZATION where (1 = 1  AND orgtype=0 and (STATUS != 1 or STATUS IS NULL) ) or  ORGID = " + orgid + " and getOrgIdList(ORGID=" + orgid + ") ) and innrulid not in (select innrulid from TBL_flow_INNERRULE where flowid = " + flowid + ") ");
        sqlSb.append(" ) BUDGET LIMIT " + pageInfo.getCurrentRecord() + " , " + pageInfo.getPageSize() + " ) as a ");
        return sqlSb.toString();
    }

    public String findCountInnerruleByFolwid(String orgid, String flowid) {
        StringBuffer sqlSb = new StringBuffer(" SELECT COUNT(*) FROM (SELECT INNRULID,RULECODE,RULENAME,RULENUMBER,PUBLISHDATE,ORGNAME  FROM TBL_INNERRULE ru LEFT JOIN TBL_ORGANIZATION org on RU.PUBLISHORG=ORG.ORGID WHERE PUBLISHORG in (select cast(ORGID as char) from TBL_ORGANIZATION where (1 = 1  AND orgtype=0 and (STATUS != 1 or STATUS IS NULL) ) or  ORGID = " + orgid + " and getOrgIdList(ORGID=" + orgid + ") and innrulid not in (select innrulid from TBL_flow_INNERRULE where flowid = " + flowid + ") ) ) as a");
        return sqlSb.toString();
    }


}
