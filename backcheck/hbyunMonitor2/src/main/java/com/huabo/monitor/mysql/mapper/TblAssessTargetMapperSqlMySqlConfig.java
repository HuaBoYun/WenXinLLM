package com.huabo.monitor.mysql.mapper;

import com.hbfk.util.PageInfo;
import com.huabo.monitor.mysql.entity.TblAssessTargetMySql;
import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;

public class TblAssessTargetMapperSqlMySqlConfig {

    public String MyMark(BigDecimal staffid, String assid, String assName, PageInfo<TblAssessTargetMySql> pageInfo) {
        StringBuffer sbSql = new StringBuffer("SELECT BUDGET.* FROM" +
                "( SELECT  * FROM TBL_ASSESS_TARGET tat LEFT JOIN TBL_ASSESS_MARK am ON tat.ASSESSTARGETID " +
                "= am.assesstargetid left join TBL_ASSESS_STAFF tas on " +
                "am.assmarkid = tas.assmarkid left join TBL_ASSESS ta on tat.assid = ta.assid   " +
                "left join TBL_ORGANIZATION TOR ON tat.ORGID = TOR.ORGID  " +
                "WHERE (am.staffid = '" + staffid + "' or tas.staffid = '" + staffid + "' )  and ta.assstatus >1 ");
        if (StringUtils.isNotBlank(assid)) {
            sbSql.append(" and ta.assessid like '%" + assid + "%' ");
        }

        if (StringUtils.isNotBlank(assName)) {
            sbSql.append(" and ta.assessname like '%" + assName + "%' ");
        }

        sbSql.append(" order by tat.ASSESSTARGETID desc) BUDGET LIMIT " + pageInfo.getCurrentRecord() + " , " + pageInfo.getPageSize());
        return sbSql.toString();
    }

    public String MyMarkCount(BigDecimal staffid, String assid, String assName) {
        StringBuffer sbSql = new StringBuffer(" SELECT COUNT(*) FROM TBL_ASSESS_TARGET tat LEFT JOIN TBL_ASSESS_MARK am ON tat.ASSESSTARGETID = am.assesstargetid left join TBL_ASSESS_STAFF tas on am.assmarkid = tas.assmarkid left join TBL_ASSESS ta on tat.assid = ta.assid    left join TBL_ORGANIZATION TOR ON tat.ORGID = TOR.ORGID WHERE (am.staffid = '" + staffid + "' or tas.staffid = '" + staffid + "' )  and ta.assstatus >1 ");
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