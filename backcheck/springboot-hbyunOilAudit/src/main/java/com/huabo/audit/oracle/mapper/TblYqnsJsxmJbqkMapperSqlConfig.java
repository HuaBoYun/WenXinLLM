package com.huabo.audit.oracle.mapper;

import com.hbfk.util.PageInfo;
import com.huabo.audit.oracle.entity.TblYqnsJsxmJbqk;
import org.apache.commons.lang3.StringUtils;

/**
 * @author wangys
 * @description 针对表【TBL_YQNS_JSXM_JBQK(建设项目基本情况表)】的数据库操作Mapper
 * @Entity TblYqnsJsxmJbqk
 */
public class TblYqnsJsxmJbqkMapperSqlConfig {

    public String selectCountByPageInfo(PageInfo<TblYqnsJsxmJbqk> pageInfo, TblYqnsJsxmJbqk vo) {
        StringBuffer sb = new StringBuffer("SELECT COUNT(0) "
                + " FROM TBL_YQNS_JSXM_JBQK TBL1 "
                + " WHERE 1=1");

        sqlQuery(vo, sb);

        return sb.toString();
    }


    public String selectListByPageInfo(PageInfo<TblYqnsJsxmJbqk> pageInfo, TblYqnsJsxmJbqk vo) {
        StringBuffer sb = new StringBuffer("SELECT * FROM "
                + "(SELECT T1.*,ROWNUM RN  FROM "
                + "(SELECT TBL1.* "
                + "FROM TBL_YQNS_JSXM_JBQK TBL1 "
                + "WHERE 1=1");

        sqlQuery(vo, sb);

        sb.append(" ORDER BY TBL1.jsxmjbqkid DESC) T1 WHERE ROWNUM <= " + (pageInfo.getCurrentRecord() + pageInfo.getPageSize()) + " ) T2 WHERE T2.RN > " + pageInfo.getCurrentRecord());
        return sb.toString();
    }

    private void sqlQuery(TblYqnsJsxmJbqk vo, StringBuffer sb) {
        if (vo.getOrgid() != null) {
            sb.append(" and orgid = ").append(vo.getOrgid());
        }

        if (StringUtils.isNotEmpty(vo.getHtbh())) {
            sb.append(" AND TBL1.htbh LIKE '%" + vo.getHtbh() + "%'");
        }

        if (StringUtils.isNotEmpty(vo.getYsxmmc())) {
            sb.append(" AND TBL1.ysxmmc LIKE '%" + vo.getYsxmmc() + "%'");
        }

        if (StringUtils.isNotEmpty(vo.getJhwh())) {
            sb.append(" AND TBL1.jhwh LIKE '%" + vo.getJhwh() + "%'");
        }
        if (StringUtils.isNotEmpty(vo.getXmfzr())) {
            sb.append(" AND TBL1.xmfzr LIKE '%" + vo.getXmfzr() + "%'");
        }

        if (StringUtils.isNotEmpty(vo.getLxdh())) {
            sb.append(" AND TBL1.lxdh LIKE '%" + vo.getLxdh() + "%'");
        }

        if (vo.getStartDate() != null && vo.getStartDate().length() > 0) {
            sb.append(" AND TBL1.cjsj >= TO_DATE('" + vo.getStartDate() + " 00:00:00','yyyy-mm-dd HH24:MI:SS')");
        }

        if (vo.getEndDate() != null && vo.getEndDate().length() > 0) {
            sb.append(" AND TBL1.cjsj <= TO_DATE('" + vo.getEndDate() + " 23:59:59','yyyy-mm-dd HH24:MI:SS')");
        }
    }

}




