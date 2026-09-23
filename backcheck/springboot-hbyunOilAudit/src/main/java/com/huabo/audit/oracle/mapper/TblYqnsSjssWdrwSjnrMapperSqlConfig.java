package com.huabo.audit.oracle.mapper;

import com.hbfk.util.PageInfo;
import com.huabo.audit.oracle.entity.TblYqnsSjssWdrwSjnr;

/**
 * @author wangxilu
 * @description 针对表【TBL_YQNS_SJSS_WDRW_SJNR(审减内容表)】的数据库操作Mapper
 * @createDate 2023-09-27 16:46:40
 * @Entity TblYqnsSjssWdrwSjnr
 */
public class TblYqnsSjssWdrwSjnrMapperSqlConfig {

    public String selectCountByPageInfo(PageInfo<TblYqnsSjssWdrwSjnr> pageInfo, TblYqnsSjssWdrwSjnr vo) {
        StringBuffer sb = new StringBuffer("SELECT COUNT(0) "
                + " FROM TBL_YQNS_SJSS_WDRW_SJNR TBL1 "
                + " WHERE 1=1");

        sqlQuery(vo, sb);

        return sb.toString();
    }


    public String selectListByPageInfo(PageInfo<TblYqnsSjssWdrwSjnr> pageInfo, TblYqnsSjssWdrwSjnr vo) {
        StringBuffer sb = new StringBuffer("SELECT * FROM "
                + "(SELECT T1.*,ROWNUM RN  FROM "
                + "(SELECT TBL1.* "
                + "FROM TBL_YQNS_SJSS_WDRW_SJNR TBL1 "
                + "WHERE 1=1");

        sqlQuery(vo, sb);

        sb.append(" ORDER BY TBL1.SJNRID DESC) T1 WHERE ROWNUM <= " + (pageInfo.getCurrentRecord() + pageInfo.getPageSize()) + " ) T2 WHERE T2.RN > " + pageInfo.getCurrentRecord());
        return sb.toString();
    }

    private void sqlQuery(TblYqnsSjssWdrwSjnr vo, StringBuffer sb) {

        if (vo.getStartDate() != null && vo.getStartDate().length() > 0) {
            sb.append(" AND TBL1.cjsj >= TO_DATE('" + vo.getStartDate() + " 00:00:00','yyyy-mm-dd HH24:MI:SS')");
        }

        if (vo.getEndDate() != null && vo.getEndDate().length() > 0) {
            sb.append(" AND TBL1.cjsj <= TO_DATE('" + vo.getEndDate() + " 23:59:59','yyyy-mm-dd HH24:MI:SS')");
        }
    }

}




