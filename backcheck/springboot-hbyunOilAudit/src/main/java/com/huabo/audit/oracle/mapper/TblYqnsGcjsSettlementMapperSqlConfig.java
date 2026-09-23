package com.huabo.audit.oracle.mapper;


import com.hbfk.util.PageInfo;
import com.huabo.audit.oracle.entity.TblYqnsGcjsSettlement;
import com.huabo.audit.oracle.entity.TblYqnsJhglJh;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Administrator
 * @description 针对表【TBL_YQNS_GCJS_SETTLEMENT(工程结算审计项目汇总)】的数据库操作Mapper
 * @Entity TblYqnsGcjsSettlement
 */
public class TblYqnsGcjsSettlementMapperSqlConfig {


    public String selectListByPageInfo(PageInfo<TblYqnsGcjsSettlement> pageInfo, TblYqnsGcjsSettlement vo) {
        StringBuffer sb = new StringBuffer("SELECT * FROM "
                + "(SELECT T1.*,ROWNUM RN  FROM "
                + "(SELECT TBL1.* "
                + "FROM TBL_YQNS_GCJS_SETTLEMENT TBL1 "
                + "WHERE 1=1");

        sqlQuery(vo, sb);

        sb.append(" ORDER BY TBL1.SETTLEMENTID DESC) T1 WHERE ROWNUM <= " + (pageInfo.getCurrentRecord() + pageInfo.getPageSize()) + " ) T2 WHERE T2.RN > " + pageInfo.getCurrentRecord());
        return sb.toString();
    }


    private void sqlQuery(TblYqnsGcjsSettlement vo, StringBuffer sb) {
        if (StringUtils.isNotEmpty(vo.getSettlementcode())) {
            sb.append(" and settlementcode LIKE '%").append(vo.getSettlementcode()).append("%'");
        }
        if (StringUtils.isNotEmpty(vo.getIds())) {
            sb.append(" and TBL1.SETTLEMENTID  in ( " + vo.getIds() + ")");
        }
        if (StringUtils.isNotEmpty(vo.getSettlementname())) {
            sb.append(" AND TBL1.settlementname LIKE '%" + vo.getSettlementname() + "%'");
        }


    }

}
