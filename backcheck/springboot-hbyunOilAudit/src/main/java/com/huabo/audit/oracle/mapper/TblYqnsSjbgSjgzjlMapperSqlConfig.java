package com.huabo.audit.oracle.mapper;

import org.apache.commons.lang3.StringUtils;

import com.hbfk.util.PageInfo;
import com.huabo.audit.oracle.entity.TblYqnsSjbgSjgzjl;

/**
 * @description 审计工作记录Mapper
 */
public class TblYqnsSjbgSjgzjlMapperSqlConfig {

    public String selectCountByPageInfo(PageInfo<TblYqnsSjbgSjgzjl> pageInfo, TblYqnsSjbgSjgzjl vo) {
        StringBuffer sb = new StringBuffer("SELECT COUNT(0) "
                + " FROM TBL_YQNS_SJBG_SJGZJL TBL1 "
                + " WHERE 1=1");

        sqlQuery(vo, sb);

        return sb.toString();
    }


    public String selectListByPageInfo(PageInfo<TblYqnsSjbgSjgzjl> pageInfo, TblYqnsSjbgSjgzjl vo) {
        StringBuffer sb = new StringBuffer("SELECT * FROM "
                + "(SELECT T1.*,ROWNUM RN  FROM "
                + "(SELECT TBL1.* "
                + "FROM TBL_YQNS_SJBG_SJGZJL TBL1 "
                + "WHERE 1=1");

        sqlQuery(vo, sb);

        sb.append(" ORDER BY TBL1.ID DESC) T1 WHERE ROWNUM <= " + (pageInfo.getCurrentRecord() + pageInfo.getPageSize()) + " ) T2 WHERE T2.RN > " + pageInfo.getCurrentRecord());
        return sb.toString();
    }

    private void sqlQuery(TblYqnsSjbgSjgzjl vo, StringBuffer sb) {

        if (StringUtils.isNotEmpty(vo.getTitle())) {
            sb.append(" AND TBL1.TITLE LIKE '%" + vo.getTitle() + "%'");
        }
        
        if (StringUtils.isNotEmpty(vo.getSjzz())) {
            sb.append(" AND TBL1.SJZZ LIKE '%" + vo.getSjzz() + "%'");
        }
        
        if (StringUtils.isNotEmpty(vo.getZs())) {
            sb.append(" AND TBL1.ZS LIKE '%" + vo.getZs() + "%'");
        }

    }

}




