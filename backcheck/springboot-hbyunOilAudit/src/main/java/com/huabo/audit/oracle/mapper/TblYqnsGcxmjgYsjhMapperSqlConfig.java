package com.huabo.audit.oracle.mapper;

import com.hbfk.util.PageInfo;
import com.hbfk.util.database.DataBaseSqlConfig;
import com.huabo.audit.oracle.entity.TblYqnsGcxmjgYsjh;
import org.apache.commons.lang.StringUtils;

/**
 * @author wangys
 * @description 针对表【TBL_YQNS_GCXMJG_YSJH(工程项目竣工验收计划)】的数据库操作Mapper
 * @createDate 2023-09-07 16:46:40
 * @Entity TblYqnsGcxmzj
 */
public class TblYqnsGcxmjgYsjhMapperSqlConfig {

    public String selectCountByPageInfo(PageInfo<TblYqnsGcxmjgYsjh> pageInfo, TblYqnsGcxmjgYsjh vo) throws Exception {
        StringBuffer sb = new StringBuffer("SELECT COUNT(0) "
                + " FROM TBL_YQNS_GCXMJG_YSJH TBL1 "
                + " WHERE 1=1");

        sqlQuery(vo, sb);

        return sb.toString();
    }


    public String selectListByPageInfo(PageInfo<TblYqnsGcxmjgYsjh> pageInfo, TblYqnsGcxmjgYsjh vo) throws Exception {
        StringBuffer sb = new StringBuffer("SELECT * FROM "
                + "(SELECT T1.*,ROWNUM RN  FROM "
                + "(SELECT TBL1.* "
                + "FROM TBL_YQNS_GCXMJG_YSJH TBL1 "
                + "WHERE 1=1");

        sqlQuery(vo, sb);

        sb.append(" ORDER BY TBL1.gcxmjgysjhid DESC) T1 WHERE ROWNUM <= " + (pageInfo.getCurrentRecord() + pageInfo.getPageSize()) + " ) T2 WHERE T2.RN > " + pageInfo.getCurrentRecord());
        return sb.toString();
    }

    public String selectCountByPageInfoDraftPlan(PageInfo<TblYqnsGcxmjgYsjh> pageInfo, TblYqnsGcxmjgYsjh vo) throws Exception {
        StringBuffer sb = new StringBuffer("SELECT COUNT(0) "
                + " FROM TBL_YQNS_GCXMJG_YSJH TBL1 "
                + " WHERE TBL1.GCXMJGYSJHID NOT IN (SELECT GLID FROM TBL_YQNS_JHGL_JHCG_GL WHERE GLTYPE = '32') ");

        sqlQuery(vo, sb);

        return sb.toString();
    }

    public String selectListByPageInfoDraftPlan(PageInfo<TblYqnsGcxmjgYsjh> pageInfo, TblYqnsGcxmjgYsjh vo) throws Exception {
        StringBuffer sb = new StringBuffer("SELECT * FROM "
                + "(SELECT T1.*,ROWNUM RN  FROM "
                + "(SELECT TBL1.* "
                + "FROM TBL_YQNS_GCXMJG_YSJH TBL1 "
                + "WHERE TBL1.GCXMJGYSJHID NOT IN (SELECT GLID FROM TBL_YQNS_JHGL_JHCG_GL WHERE GLTYPE = '32') ");

        sqlQuery(vo, sb);

        sb.append(" ORDER BY TBL1.gcxmjgysjhid DESC) T1 WHERE ROWNUM <= " + (pageInfo.getCurrentRecord() + pageInfo.getPageSize()) + " ) T2 WHERE T2.RN > " + pageInfo.getCurrentRecord());
        return sb.toString();
    }


    public String selectListByExport(TblYqnsGcxmjgYsjh vo) throws Exception {
        StringBuffer sb = new StringBuffer("SELECT TBL1.* FROM TBL_YQNS_GCXMJG_YSJH TBL1 WHERE 1=1");
        sqlQuery(vo, sb);
        sb.append(" ORDER BY TBL1.gcxmjgysjhid DESC");
        return sb.toString();
    }

    private void sqlQuery(TblYqnsGcxmjgYsjh vo, StringBuffer sb) throws Exception {
        if (vo.getCurrentStaffId() != null) {
            sb.append(" AND ( TBL1.EXT1 = '").append(vo.getCurrentStaffId()).append("'");
            if (StringUtils.isNotBlank(vo.getQueryDeptIds())) {
                sb.append(" OR TBL1.EXT1 IN ( SELECT " + DataBaseSqlConfig.toCharColumn("STAFFID") + " FROM TBL_USER_ORGRELATION WHERE DEPTID IN (").append(vo.getQueryDeptIds()).append("))");
            }
            sb.append(") ");
        }

        if (vo.getIds() != null && !vo.getIds().isEmpty()) {
            sb.append(" AND TBL1.GCXMJGYSJHID IN (");
            // 使用循环拼接 ids
            for (int i = 0; i < vo.getIds().size(); i++) {
                sb.append(vo.getIds().get(i));
                if (i < vo.getIds().size() - 1) {
                    sb.append(", ");
                }
            }
            sb.append(")");
        }


        if (vo.getGcxmjgysjhNo() != null) {
            sb.append(" AND TBL1.GCXMJGYSJHNO LIKE '%").append(vo.getGcxmjgysjhNo()).append("%'");
        }


        if (vo.getQueryYear() != null) {
            sb.append(" AND TBL1.CJSJ >= ").append(DataBaseSqlConfig.getDateStrFormat(vo.getQueryYear() + "-01-01")).append(" AND TBL1.CJSJ <= ").append(DataBaseSqlConfig.getDateStrFormat((vo.getQueryYear() + 1) + "-01-01"));
        }
        if (vo.getOrgid() != null) {
            sb.append(" and orgid = ").append(vo.getOrgid());
        }

        if (StringUtils.isNotEmpty(vo.getXmmc())) {
            sb.append(" AND TBL1.xmmc LIKE '%" + vo.getXmmc() + "%'");
        }

        if (StringUtils.isNotEmpty(vo.getJsdw())) {
            sb.append(" AND TBL1.jsdw LIKE '%" + vo.getJsdw() + "%'");
        }
        if (StringUtils.isNotEmpty(vo.getBz())) {
            sb.append(" AND TBL1.sgdw LIKE '%" + vo.getBz() + "%'");
        }

        if (vo.getStartDate() != null && vo.getStartDate().length() > 0) {
            sb.append(" AND TBL1.xmtcsj >= TO_DATE('" + vo.getStartDate() + " 00:00:00','yyyy-mm-dd HH24:MI:SS')");
        }

        if (vo.getEndDate() != null && vo.getEndDate().length() > 0) {
            sb.append(" AND TBL1.xmtcsj <= TO_DATE('" + vo.getEndDate() + " 23:59:59','yyyy-mm-dd HH24:MI:SS')");
        }
    }

}




