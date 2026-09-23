package com.huabo.audit.oracle.mapper;

import com.hbfk.util.PageInfo;
import com.huabo.audit.oracle.entity.TblYqnsJsxmTzwcqkFymx;
import org.apache.commons.lang3.StringUtils;

/**
 * @author wangys
 * @description 针对表【TBL_YQNS_JSXM_TZWCQK_FYMX(建设项目投资完成情况费用明细表】的数据库操作Mapper
 * @Entity TblYqnsJsxmTzwcqkFymx
 */
public class TblYqnsJsxmTzwcqkFymxMapperSqlConfig {

    public String selectCountByPageInfo(PageInfo<TblYqnsJsxmTzwcqkFymx> pageInfo, TblYqnsJsxmTzwcqkFymx vo) {
        StringBuffer sb = new StringBuffer("SELECT COUNT(0) "
                + " FROM TBL_YQNS_JSXM_TZWCQK_FYMX TBL1 "
                + " WHERE 1=1");

        sqlQuery(vo, sb);

        return sb.toString();
    }


    public String selectListByPageInfo(PageInfo<TblYqnsJsxmTzwcqkFymx> pageInfo, TblYqnsJsxmTzwcqkFymx vo) {
        StringBuffer sb = new StringBuffer("SELECT * FROM "
                + "(SELECT T1.*,ROWNUM RN  FROM "
                + "(SELECT TBL1.* "
                + "FROM TBL_YQNS_JSXM_TZWCQK_FYMX TBL1 "
                + "WHERE 1=1");

        sqlQuery(vo, sb);

        sb.append(" ORDER BY TBL1.jsxmtzwcqkid DESC) T1 WHERE ROWNUM <= " + (pageInfo.getCurrentRecord() + pageInfo.getPageSize()) + " ) T2 WHERE T2.RN > " + pageInfo.getCurrentRecord());
        return sb.toString();
    }

    private void sqlQuery(TblYqnsJsxmTzwcqkFymx vo, StringBuffer sb) {
        if (vo.getOrgid() != null) {
            sb.append(" and orgid = ").append(vo.getOrgid());
        }
        if (vo.getJsxmtzwcqkid() != null) {
            sb.append(" AND TBL1.Jsxmtzwcqkid = " + vo.getJsxmtzwcqkid());
        }
        if (StringUtils.isNotEmpty(vo.getFylx())) {
            sb.append(" AND TBL1.Fylx LIKE '%" + vo.getFylx() + "%'");
        }
        if (StringUtils.isNotEmpty(vo.getHtbh())) {
            sb.append(" AND TBL1.Htbh LIKE '%" + vo.getHtbh() + "%'");
        }

        if (StringUtils.isNotEmpty(vo.getGchfymc())) {
            sb.append(" AND TBL1.gchfymc LIKE '%" + vo.getGchfymc() + "%'");
        }

        if (StringUtils.isNotEmpty(vo.getJhwh())) {
            sb.append(" AND TBL1.jhwh LIKE '%" + vo.getJhwh() + "%'");
        }
        if (StringUtils.isNotEmpty(vo.getSsdw())) {
            sb.append(" AND TBL1.Ssdw LIKE '%" + vo.getSsdw() + "%'");
        }

        if (vo.getStartDate() != null && vo.getStartDate().length() > 0) {
            sb.append(" AND TBL1.cjsj >= TO_DATE('" + vo.getStartDate() + " 00:00:00','yyyy-mm-dd HH24:MI:SS')");
        }

        if (vo.getEndDate() != null && vo.getEndDate().length() > 0) {
            sb.append(" AND TBL1.cjsj <= TO_DATE('" + vo.getEndDate() + " 23:59:59','yyyy-mm-dd HH24:MI:SS')");
        }

        if(vo.getJsxmtzwcqkid() != null){
            sb.append(" AND TBL1.Jsxmtzwcqkid = ").append(vo.getJsxmtzwcqkid());
        }

        if(StringUtils.isNotEmpty(vo.getFylx())){
            sb.append(" AND TBL1.Fylx = ").append(vo.getFylx());
        }
    }

}




