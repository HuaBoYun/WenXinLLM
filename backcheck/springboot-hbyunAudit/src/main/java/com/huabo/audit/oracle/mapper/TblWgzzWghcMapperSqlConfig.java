package com.huabo.audit.oracle.mapper;

import com.huabo.audit.oracle.entity.TblWgzzShbg;
import com.huabo.audit.oracle.entity.TblWgzzWghc;
import com.huabo.audit.oracle.entity.TblWgzzWghs;
import com.huabo.audit.util.PageInfo;

/**
 * @Created with IDEA 2019
 * @package: com.huabo.audit.oracle.mapper
 * @project_name: SVN4
 * @author:wjh
 * @Date:2023/5/4
 * @Time:13:09
 */
public class TblWgzzWghcMapperSqlConfig {
    public String getByContWghcList(PageInfo<TblWgzzWghc> pageInfo, String creator){
        StringBuffer sb = new StringBuffer("SELECT COUNT(0) FROM TBL_WGZZ_WGHC TNA WHERE 1=1 ");

        if(creator != null){
            sb.append("AND CREATOR LIKE '%"+creator+"%'");
        }

        return sb.toString();
    }
    public String selectBywghcBy(PageInfo<TblWgzzWghc> pageInfo, String creator) {
        StringBuffer sb = new StringBuffer("SELECT * FROM "
                + "(SELECT T1.*,ROWNUM RN  FROM "
                + "(SELECT TNA.* "
                + "FROM TBL_WGZZ_WGHC TNA "
                + "WHERE 1=1 ");

        if(creator !=null && creator.length()>0) {
            sb.append("AND CREATOR LIKE '%"+creator+"%'");
        }
        sb.append(" ORDER BY TNA.ID DESC) T1 WHERE ROWNUM <= "+(pageInfo.getCurrentRecord()+pageInfo.getPageSize())+" ) T2 WHERE T2.RN > "+pageInfo.getCurrentRecord());
        return sb.toString();
    }

}
