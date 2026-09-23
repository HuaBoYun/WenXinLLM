package com.huabo.monitor.mysql.mapper;

import com.hbfk.util.PageInfo;
import com.huabo.monitor.entity.TblAcquisitionRecord;

import java.math.BigDecimal;

public class TblAcquisitionRecordMapperSqlMySqlConfig {

    public String selectListByPageInfo(PageInfo<TblAcquisitionRecord> pageInfo, BigDecimal orgid) {
        StringBuffer sqlSb = new StringBuffer("SELECT * FROM (SELECT BUDGET.*,CAST(@ROW := @ROW + 1 AS SIGNED) AS RN FROM (SELECT RECORDID,RECORDYEAR,RECORDSTART,RECORDEND,RECORDTIME,F.REALNAME,O.ORGNAME,R.RETYPE FROM TBL_ACQUISITION_RECORD R LEFT JOIN TBL_STAFF F ON R.STAFFID = F.STAFFID LEFT JOIN TBL_ORGANIZATION O ON R.ORGID = O.ORGID WHERE R.ORGID =" +orgid);
        sqlSb.append(" ORDER BY R.RECORDSTART DESC ) BUDGET LIMIT " + pageInfo.getCurrentRecord() + " , " + pageInfo.getPageSize() + " ) AS A ");
        String sql = sqlSb.toString();
        return sql;

    }
}
