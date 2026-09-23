package com.huabo.system.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hbfk.util.PageInfo;
import com.huabo.system.entity.ProcessSetting;
import com.huabo.system.entity.TblOuterrule;
import com.huabo.system.entity.TblSystemModule;

import java.math.BigDecimal;

public class TblOuterruleMapperSqlConfig {



    public String findListByPageInfoFlow(IPage<TblOuterrule> outpage, String flowid) {
        StringBuffer sqlSb = new StringBuffer("SELECT * from TBL_OUTERRULE  o where o.OUTRULID in (SELECT fo.OUTRULID from TBL_FLOW_OUTERRULE fo where fo.flowid='" + flowid + "') ");
        sqlSb.append(" ORDER BY OUTRULID DESC");
        return sqlSb.toString();
    }

    public String findCountByPageInfoFlow(String flowid) {
        StringBuffer sqlSb = new StringBuffer("SELECT COUNT(*) FROM (SELECT * from TBL_OUTERRULE  o where o.OUTRULID in (SELECT fo.OUTRULID from TBL_FLOW_OUTERRULE fo where fo.flowid='" + flowid + "') )");
        return sqlSb.toString();
    }


    public String findInnerruleByFolwid(IPage<TblOuterrule> page, String orgid, String name, String status, String flowid) throws Exception {
        StringBuffer sqlSb = new StringBuffer("SELECT * FROM TBL_OUTERRULE where CREATEORGID=" + orgid + " and outrulid not in (select outrulid from TBL_flow_OUTERRULE where flowid = '" + flowid + "')");

        if(name != null && !"".equals(name)) {
            sqlSb.append(" and RULENAME='" + name + "' ");
        }
        if(status != null && !"".equals(status) ) {
            sqlSb.append("and STATUS='" + status + "'");
        }

        sqlSb.append(" ORDER BY OUTRULID DESC ");

        return sqlSb.toString();
    }

}
