package com.huabo.audit.oracle.mapper;

import com.hbfk.util.PageInfo;
import com.huabo.audit.oracle.entity.TblYqnsGzfa;
import org.apache.commons.lang3.StringUtils;

/**
 * @author wangys
 * @description 针对表【TBL_YQNS_GZFA(工作方案表)】的数据库操作Mapper
 * @Entity TblYqnsGzfa
 */
public class TblYqnsGzfaMapperSqlConfig {

    public String selectCountByPageInfo(PageInfo<TblYqnsGzfa> pageInfo, TblYqnsGzfa vo) {
        StringBuffer sb = new StringBuffer("SELECT COUNT(0) "
                + " FROM TBL_YQNS_GZFA TBL1 "
                + " WHERE 1=1");

        sqlQuery(vo, sb);

        return sb.toString();
    }


    public String selectListByPageInfo(PageInfo<TblYqnsGzfa> pageInfo, TblYqnsGzfa vo) {
        StringBuffer sb = new StringBuffer("SELECT * FROM "
                + "(SELECT T1.*,ROWNUM RN  FROM "
                + "(SELECT TBL1.* "
                + "FROM TBL_YQNS_GZFA TBL1 "
                + "WHERE 1=1");

        sqlQuery(vo, sb);

        sb.append(" ORDER BY TBL1.gzfaid DESC) T1 WHERE ROWNUM <= " + (pageInfo.getCurrentRecord() + pageInfo.getPageSize()) + " ) T2 WHERE T2.RN > " + pageInfo.getCurrentRecord());
        return sb.toString();
    }

    private void sqlQuery(TblYqnsGzfa vo, StringBuffer sb) {
        if (vo.getOrgid() != null) {
            sb.append(" and orgid = ").append(vo.getOrgid());
        }

        if (StringUtils.isNotEmpty(vo.getXmmc())) {
            sb.append(" AND TBL1.Xmmc LIKE '%" + vo.getXmmc() + "%'");
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

        if (vo.getStaffLoginId() != null){
            sb.append(" and ").append(vo.getStaffLoginId()).append(" = ext1 ").append(" or ")
                    .append(" ryids like ").append("'%").append(vo.getStaffLoginId()).append("%'");
        }
    }

}




