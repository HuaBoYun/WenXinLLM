package com.huabo.audit.oracle.mapper;

import com.hbfk.util.PageInfo;
import com.huabo.audit.oracle.entity.TblYqnsJhxq;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Administrator
 * @description 针对表【TBL_YQNS_JHXQ(计划需求)】的数据库操作Mapper
 * @Entity TblYqnsJhxq
 */
public class TblYqnsJhxqMapperSqlConfig {

    public String selectCountByPageInfo(PageInfo<TblYqnsJhxq> pageInfo, TblYqnsJhxq vo) {
        StringBuffer sb = new StringBuffer("SELECT COUNT(0) "
                + " FROM TBL_YQNS_JHXQ TBL1 "
                + " WHERE 1=1");

        sqlQuery(vo, sb);

        return sb.toString();
    }


    public String selectListByPageInfo(PageInfo<TblYqnsJhxq> pageInfo, TblYqnsJhxq vo) {
        StringBuffer sb = new StringBuffer("SELECT * FROM "
                + "(SELECT T1.*,ROWNUM RN  FROM "
                + "(SELECT TBL1.* "
                + "FROM TBL_YQNS_JHXQ TBL1 "
                + "WHERE 1=1");

        sqlQuery(vo, sb);

        sb.append(" ORDER BY TBL1.JHXQID DESC) T1 WHERE ROWNUM <= " + (pageInfo.getCurrentRecord() + pageInfo.getPageSize()) + " ) T2 WHERE T2.RN > " + pageInfo.getCurrentRecord());
        return sb.toString();
    }

    private void sqlQuery(TblYqnsJhxq vo, StringBuffer sb) {
        if (vo.getOrgid() != null) {
            sb.append(" and orgid = ").append(vo.getOrgid());
        }

        if (StringUtils.isNotEmpty(vo.getJhxqmc())) {
            sb.append(" AND TBL1.Jhxqmc LIKE '%" + vo.getJhxqmc() + "%'");
        }

        if (StringUtils.isNotEmpty(vo.getSjxmlx())) {
            sb.append(" AND TBL1.Sjxmlx ='").append(vo.getSjxmlx()).append("'");
        }

        if (vo.getStartDate() != null && vo.getStartDate().length() > 0) {
            sb.append(" AND TBL1.cjsj >= TO_DATE('" + vo.getStartDate() + " 00:00:00','yyyy-mm-dd HH24:MI:SS')");
        }

        if (vo.getEndDate() != null && vo.getEndDate().length() > 0) {
            sb.append(" AND TBL1.cjsj <= TO_DATE('" + vo.getEndDate() + " 23:59:59','yyyy-mm-dd HH24:MI:SS')");
        }
    }

}




