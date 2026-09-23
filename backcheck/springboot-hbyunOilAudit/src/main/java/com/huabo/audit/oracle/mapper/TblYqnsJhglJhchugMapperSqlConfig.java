package com.huabo.audit.oracle.mapper;

import com.hbfk.util.PageInfo;
import com.huabo.audit.oracle.entity.TblYqnsJhglJhcg;
import com.huabo.audit.oracle.entity.TblYqnsJhglJhchug;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Administrator
 * @description 针对表【TBL_YQNS_JHGL_JHCHUG(计划管理计划初稿)】的数据库操作Mapper
 * @Entity TblYqnsJhglJhchug
 */
public class TblYqnsJhglJhchugMapperSqlConfig {

    public String selectCountByPageInfo(PageInfo<TblYqnsJhglJhchug> pageInfo, TblYqnsJhglJhchug vo) {
        StringBuffer sb = new StringBuffer("SELECT COUNT(0) "
                + " FROM TBL_YQNS_JHGL_JHCHUG TBL1 "
                + " WHERE 1=1");

        sqlQuery(vo, sb);

        return sb.toString();
    }


    public String selectListByPageInfo(PageInfo<TblYqnsJhglJhchug> pageInfo, TblYqnsJhglJhchug vo) {
        StringBuffer sb = new StringBuffer("SELECT * FROM "
                + "(SELECT T1.*,ROWNUM RN  FROM "
                + "(SELECT TBL1.* "
                + "FROM TBL_YQNS_JHGL_JHCHUG TBL1 "
                + "WHERE 1=1");

        sqlQuery(vo, sb);

        sb.append(" ORDER BY TBL1.JHCHUGID DESC) T1 WHERE ROWNUM <= " + (pageInfo.getCurrentRecord() + pageInfo.getPageSize()) + " ) T2 WHERE T2.RN > " + pageInfo.getCurrentRecord());
        return sb.toString();
    }

    private void sqlQuery(TblYqnsJhglJhchug vo, StringBuffer sb) {
        if (vo.getOrgid() != null) {
            sb.append(" and orgid = ").append(vo.getOrgid());
        }

        if (StringUtils.isNotEmpty(vo.getJhmc())) {
            sb.append(" AND TBL1.Jhmc LIKE '%" + vo.getJhmc() + "%'");
        }

        if (vo.getStartDate() != null && vo.getStartDate().length() > 0) {
            sb.append(" AND TBL1.cjsj >= TO_DATE('" + vo.getStartDate() + " 00:00:00','yyyy-mm-dd HH24:MI:SS')");
        }

        if (vo.getEndDate() != null && vo.getEndDate().length() > 0) {
            sb.append(" AND TBL1.cjsj <= TO_DATE('" + vo.getEndDate() + " 23:59:59','yyyy-mm-dd HH24:MI:SS')");
        }
    }

}




