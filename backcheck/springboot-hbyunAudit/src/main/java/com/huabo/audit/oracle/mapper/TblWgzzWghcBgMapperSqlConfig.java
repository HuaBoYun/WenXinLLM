package com.huabo.audit.oracle.mapper;

import com.huabo.audit.oracle.entity.TblWgzzEntity;
import com.huabo.audit.oracle.entity.TblWgzzWghcBg;
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
public class TblWgzzWghcBgMapperSqlConfig {

    public String getByContWghcBgList(PageInfo<TblWgzzWghcBg> pageInfo, String clueNaber) {
        StringBuffer sb = new StringBuffer("SELECT COUNT(0) FROM TBL_WGZZ_WGHC_BG TNA WHERE 1=1 ");

        if(clueNaber != null){
            sb.append("AND CLUENABER LIKE '%"+clueNaber+"%'");
        }

        return sb.toString();
    }

    public String getwgzzList(PageInfo<TblWgzzWghcBg> pageInfo, String clueNaber) {
        StringBuffer sb = new StringBuffer("SELECT * FROM "
                + "(SELECT T1.*,ROWNUM RN  FROM "
                + "(SELECT TNA.* "
                + "FROM TBL_WGZZ_WGHC_BG TNA "
                + "WHERE 1=1 ");

        if(clueNaber!=null && clueNaber.length()>0) {
            sb.append("AND CLUENABER LIKE '%"+clueNaber+"%'");
        }

        sb.append(" ORDER BY TNA.ID DESC) T1 WHERE ROWNUM <= "+(pageInfo.getCurrentRecord()+pageInfo.getPageSize())+" ) T2 WHERE T2.RN > "+pageInfo.getCurrentRecord());
        return sb.toString();
    }

}
