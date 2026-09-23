package com.huabo.system.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hbfk.util.PageInfo;
import com.huabo.system.entity.ProcessSetting;
import com.huabo.system.entity.TblInnerrule;

import java.math.BigDecimal;

public class TblInnerruleMapperSqlConfig {


    public String findListByPageInfoFlow(IPage<TblInnerrule> innpage, String flowid) {
        StringBuffer sqlSb = new StringBuffer("SELECT INNRULID,RULENAME,RULENUMBER,ORG.ORGNAME,PUBLISHDATE,BODYINFO  from TBL_INNERRULE inn LEFT JOIN TBL_ORGANIZATION org on INN.PUBLISHORG=ORG.ORGID WHERE inn.INNRULID in( SELECT tb.INNRULID from TBL_FLOW_INNERRULE tb where tb.flowid='" + flowid + "') ");
        sqlSb.append(" ORDER BY INNRULID DESC ");
        return sqlSb.toString();
    }

    public String findInnerruleByFolwid(IPage<TblInnerrule> page, String orgIdStrs, String flowid) {
        StringBuffer sqlSb = new StringBuffer("SELECT INNRULID,RULECODE,RULENAME,RULENUMBER,PUBLISHDATE,ORGNAME  FROM TBL_INNERRULE ru LEFT JOIN TBL_ORGANIZATION org on RU.PUBLISHORG=ORG.ORGID WHERE PUBLISHORG in (select ORGID from TBL_ORGANIZATION where (1 = 1  AND orgtype=0 and (STATUS != 1 or STATUS IS NULL) ) AND ORGID IN ("+orgIdStrs+") ) and innrulid not in (select innrulid from TBL_flow_INNERRULE where flowid = " + flowid + ") ");
        sqlSb.append(" ");
        return sqlSb.toString();
    }

}
