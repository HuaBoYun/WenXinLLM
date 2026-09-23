package com.huabo.system.mappersql;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hbfk.util.PageInfo;
import com.huabo.system.entity.TblAssessTarget;

import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;

public class TblAssessTargetMapperSqlConfig {


    public String MyMark(BigDecimal staffid, String assid, String assName, IPage<TblAssessTarget> page) {
        StringBuffer sbSql = new StringBuffer("SELECT  * FROM TBL_ASSESS_TARGET tat LEFT JOIN TBL_ASSESS_MARK am ON tat.ASSESSTARGETID = am.assesstargetid left join TBL_ASSESS_STAFF tas on am.assmarkid = tas.assmarkid left join TBL_ASSESS ta on tat.assid = ta.assid   left join TBL_ORGANIZATION TOR ON tat.ORGID = TOR.ORGID  WHERE (am.staffid = '"+staffid+"' or tas.staffid = '"+staffid+"' )  and ta.assstatus >1 ");
        if (StringUtils.isNotBlank(assid)) {
            sbSql.append(" and ta.assessid like '%" + assid + "%' ");
        }

        if (StringUtils.isNotBlank(assName)) {
            sbSql.append(" and ta.assessname like '%" + assName + "%' ");
        }

        sbSql.append(" order by tat.ASSESSTARGETID desc ");
        return sbSql.toString();
    }

}
