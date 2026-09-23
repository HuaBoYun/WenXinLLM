package com.huabo.audit.oracle.mapper;


import com.hbfk.util.PageInfo;
import com.huabo.audit.oracle.entity.TblYqnsCompletionSet;
import com.huabo.audit.oracle.entity.TblYqnsGcjsSettlement;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Administrator
 * @description 针对表【TBL_YQNS_COMPLETION_SET(竣工结算审计项目汇总)】的数据库操作Mapper
 * @Entity TBL_YQNS_COMPLETION_SET
 */
public class TblYqnsCompletionSetMapperSqlConfig {


    public String selectListByPageInfo(PageInfo<TblYqnsCompletionSet> pageInfo, TblYqnsCompletionSet vo) {
        StringBuffer sb = new StringBuffer("SELECT * FROM "
                + "(SELECT T1.*,ROWNUM RN  FROM "
                + "(SELECT TBL1.* "
                + "FROM TBL_YQNS_COMPLETION_SET TBL1 "
                + "WHERE 1=1");

        sqlQuery(vo, sb);

        sb.append(" ORDER BY TBL1.COMPLETIONID DESC) T1 WHERE ROWNUM <= " + (pageInfo.getCurrentRecord() + pageInfo.getPageSize()) + " ) T2 WHERE T2.RN > " + pageInfo.getCurrentRecord());
        return sb.toString();
    }


    private void sqlQuery(TblYqnsCompletionSet vo, StringBuffer sb) {
        if (vo.getCompletioncode()!="" && StringUtils.isNotEmpty(vo.getCompletioncode())) {
            sb.append(" and completioncode LIKE '%").append(vo.getCompletioncode()).append("%'");
        }
        if (vo.getCompletionname()!="" && StringUtils.isNotEmpty(vo.getCompletionname())) {
            sb.append(" and completionname LIKE '%").append(vo.getCompletionname()).append("%'");
        }
        if (StringUtils.isNotEmpty(vo.getIds())) {
            sb.append(" and TBL1.COMPLETIONID   IN ( "+ vo.getIds() + ")");
        }



    }

}
