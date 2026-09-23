package com.huabo.audit.oracle.mapper;

import com.hbfk.util.PageInfo;
import com.huabo.audit.oracle.entity.TblYqnsFgldhz;
import com.huabo.audit.oracle.entity.TblYqnsGzfa;
import org.apache.commons.lang3.StringUtils;

/**
 * @author wystan
 * @description 针对表【TBL_YQNS_FGLDHZ(分管领导汇总表)】的数据库操作Mapper
 * @Entity TblYqnsFgldhz
 */
public class TblYqnsFgldhzMapperSqlConfig {

    public String selectCountByPageInfo(PageInfo<TblYqnsFgldhz> pageInfo, TblYqnsFgldhz vo) {
        StringBuffer sb = new StringBuffer("SELECT COUNT(0) "
                + " FROM TBL_YQNS_FGLDHZ TBL1 "
                + " WHERE 1=1");

        sqlQuery(vo, sb);

        return sb.toString();
    }


    public String selectListByPageInfo(PageInfo<TblYqnsFgldhz> pageInfo, TblYqnsFgldhz vo) {
        StringBuffer sb = new StringBuffer("SELECT * FROM "
                + "(SELECT T1.*,ROWNUM RN  FROM "
                + "(SELECT TBL1.* "
                + "FROM TBL_YQNS_FGLDHZ TBL1 "
                + "WHERE 1=1");

        sqlQuery(vo, sb);

        sb.append(" ORDER BY TBL1.fgldhzid DESC) T1 WHERE ROWNUM <= " + (pageInfo.getCurrentRecord() + pageInfo.getPageSize()) + " ) T2 WHERE T2.RN > " + pageInfo.getCurrentRecord());
        return sb.toString();
    }

    private void sqlQuery(TblYqnsFgldhz vo, StringBuffer sb) {
        if (vo.getOrgid() != null) {
            sb.append(" and orgid = ").append(vo.getOrgid());
        }

        if (StringUtils.isNotEmpty(vo.getGsldxm())) {
            sb.append(" AND TBL1.Gsldxm LIKE '%" + vo.getGsldxm() + "%'");
        }

        if (StringUtils.isNotEmpty(vo.getLxyq())) {
            sb.append(" AND TBL1.Lxyq LIKE '%" + vo.getLxyq() + "%'");
        }

        if (StringUtils.isNotEmpty(vo.getLsjy())) {
            sb.append(" AND TBL1.Lsjy LIKE '%" + vo.getLsjy() + "%'");
        }

        if (vo.getStartDate() != null && vo.getStartDate().length() > 0) {
            sb.append(" AND TBL1.cjsj >= TO_DATE('" + vo.getStartDate() + " 00:00:00','yyyy-mm-dd HH24:MI:SS')");
        }

        if (vo.getEndDate() != null && vo.getEndDate().length() > 0) {
            sb.append(" AND TBL1.cjsj <= TO_DATE('" + vo.getEndDate() + " 23:59:59','yyyy-mm-dd HH24:MI:SS')");
        }
        if (!"5555".equals(vo.getCjr())) {
            sb.append(" AND ( TBL1.FGLDHZID  IN (SELECT FGLDHZID FROM TBL_YQNS_FGLDHZ_FF WHERE USERID='" + vo.getCjr()+"') or TBL1.CJR = '" +vo.getCjr()+"') ");
        }


    }

}




