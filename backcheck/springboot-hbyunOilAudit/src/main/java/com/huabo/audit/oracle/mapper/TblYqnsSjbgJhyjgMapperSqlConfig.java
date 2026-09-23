package com.huabo.audit.oracle.mapper;

import com.hbfk.util.PageInfo;
import com.huabo.audit.oracle.entity.TblYqnsSjbgJhyjg;
import org.apache.commons.lang3.StringUtils;

/**
 * @author wangxilu
 * @description 针对表【TTBL_YQNS_SJBG_JHYJG(交换意见稿表)】的数据库操作Mapper
 * @createDate 2023-09-27 16:46:40
 * @Entity TblYqnsSjbgJhyjg
 */
public class TblYqnsSjbgJhyjgMapperSqlConfig {

    public String selectCountByPageInfo(PageInfo<TblYqnsSjbgJhyjg> pageInfo, TblYqnsSjbgJhyjg vo) {
        StringBuffer sb = new StringBuffer("SELECT COUNT(0) "
                + " FROM TBL_YQNS_SJBG_JHYJG TBL1 "
                + " WHERE 1=1");

        sqlQuery(vo, sb);

        return sb.toString();
    }


    public String selectListByPageInfo(PageInfo<TblYqnsSjbgJhyjg> pageInfo, TblYqnsSjbgJhyjg vo) {
        StringBuffer sb = new StringBuffer("SELECT * FROM "
                + "(SELECT T1.*,ROWNUM RN  FROM "
                + "(SELECT TBL1.* "
                + "FROM TBL_YQNS_SJBG_JHYJG TBL1 "
                + "WHERE 1=1");

        sqlQuery(vo, sb);

        sb.append(" ORDER BY TBL1.JHYJGID DESC) T1 WHERE ROWNUM <= " + (pageInfo.getCurrentRecord() + pageInfo.getPageSize()) + " ) T2 WHERE T2.RN > " + pageInfo.getCurrentRecord());
        return sb.toString();
    }

    private void sqlQuery(TblYqnsSjbgJhyjg vo, StringBuffer sb) {

        if (StringUtils.isNotEmpty(vo.getTitle())) {
            sb.append(" AND TBL1.TITLE LIKE '%" + vo.getTitle() + "%'");
        }

        if (vo.getStartDate() != null && vo.getStartDate().length() > 0) {
            sb.append(" AND TBL1.cjsj >= TO_DATE('" + vo.getStartDate() + " 00:00:00','yyyy-mm-dd HH24:MI:SS')");
        }

        if (vo.getEndDate() != null && vo.getEndDate().length() > 0) {
            sb.append(" AND TBL1.cjsj <= TO_DATE('" + vo.getEndDate() + " 23:59:59','yyyy-mm-dd HH24:MI:SS')");
        }
    }

}




