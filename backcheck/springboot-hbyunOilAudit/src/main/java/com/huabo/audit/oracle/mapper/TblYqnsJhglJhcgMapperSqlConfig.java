package com.huabo.audit.oracle.mapper;

import cn.hutool.core.date.DateUtil;
import com.hbfk.util.PageInfo;
import com.huabo.audit.oracle.entity.TblYqnsJhglJhcg;
import org.apache.commons.lang3.StringUtils;

/**
 * @author wangys
 * @description 针对表【TBL_YQNS_JHGL_JHCG(计划管理计划草稿)】的数据库操作Mapper
 * @Entity TblYqnsJhglJhcg
 */
public class TblYqnsJhglJhcgMapperSqlConfig {

    public String selectCountByPageInfo(PageInfo<TblYqnsJhglJhcg> pageInfo, TblYqnsJhglJhcg vo) {
        StringBuffer sb = new StringBuffer("SELECT COUNT(0) "
                + " FROM TBL_YQNS_JHGL_JHCG TBL1 "
                + " WHERE 1=1");

        sqlQuery(vo, sb);

        return sb.toString();
    }


    public String selectListByPageInfo(PageInfo<TblYqnsJhglJhcg> pageInfo, TblYqnsJhglJhcg vo) {
        StringBuffer sb = new StringBuffer("SELECT * FROM "
                + "(SELECT T1.*,ROWNUM RN  FROM "
                + "(SELECT TBL1.* "
                + "FROM TBL_YQNS_JHGL_JHCG TBL1 "
                + "WHERE 1=1");

        sqlQuery(vo, sb);

        sb.append(" ORDER BY TBL1.JHCGID DESC) T1 WHERE ROWNUM <= " + (pageInfo.getCurrentRecord() + pageInfo.getPageSize()) + " ) T2 WHERE T2.RN > " + pageInfo.getCurrentRecord());
        return sb.toString();
    }

    private void sqlQuery(TblYqnsJhglJhcg vo, StringBuffer sb) {
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

        if(vo.getCjr() != null && vo.getCjr().length() > 0){
            sb.append(" AND TBL1.Cjr like ").append("'%").append(vo.getCjr()).append("%'");
        }

        if(vo.getCjsj()!= null){
            String sj = DateUtil.formatDate(vo.getCjsj());
            sb.append(" AND TBL1.Cjsj = to_date(").append("'").append(sj).append("',").append("'yyyy-mm-dd')");
        }

        if(vo.getSj() != null){
            String sj = DateUtil.formatDate(vo.getSj());
            sb.append(" AND TBL1.sj = to_date(").append("'").append(sj).append("',").append("'yyyy-mm-dd')");
        }

    }

}




