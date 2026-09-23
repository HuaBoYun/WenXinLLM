package com.huabo.audit.oracle.mapper;

import com.hbfk.util.PageInfo;
import com.huabo.audit.oracle.entity.TblYqnsSjxmb;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Administrator
 * @description 针对表【TBL_YQNS_SJXMB(审计项目表)】的数据库操作Mapper
 * @Entity TblYqnsSjxmb
 */
public class TblYqnsSjxmbMapperSqlConfig {

    public String selectCountByPageInfo(PageInfo<TblYqnsSjxmb> pageInfo, TblYqnsSjxmb vo) {
        StringBuffer sb = new StringBuffer("SELECT COUNT(0) "
                + " FROM TBL_YQNS_SJXMB TBL1 "
                + " WHERE 1=1");

        sqlQuery(vo, sb);

        return sb.toString();
    }


    public String selectListByPageInfo(PageInfo<TblYqnsSjxmb> pageInfo, TblYqnsSjxmb vo) {
        StringBuffer sb = new StringBuffer("SELECT * FROM "
                + "(SELECT T1.*,ROWNUM RN  FROM "
                + "(SELECT TBL1.* "
                + "FROM TBL_YQNS_SJXMB TBL1 "
                + "WHERE 1=1");

        sqlQuery(vo, sb);

        sb.append(" ORDER BY TBL1.sjxmbid DESC) T1 WHERE ROWNUM <= " + (pageInfo.getCurrentRecord() + pageInfo.getPageSize()) + " ) T2 WHERE T2.RN > " + pageInfo.getCurrentRecord());
        return sb.toString();
    }

    private void sqlQuery(TblYqnsSjxmb vo, StringBuffer sb) {
        if (vo.getOrgid() != null) {
            sb.append(" and orgid = ").append(vo.getOrgid());
        }

        if (StringUtils.isNotEmpty(vo.getGcmc())) {
            sb.append(" AND TBL1.Gcmc LIKE '%" + vo.getGcmc() + "%'");
        }

        if (StringUtils.isNotEmpty(vo.getHtbh())) {
            sb.append(" AND TBL1.Htbh LIKE '%" + vo.getHtbh() + "%'");
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




