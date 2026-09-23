package com.huabo.audit.oracle.mapper;

import com.huabo.audit.oracle.entity.TblYqnsWgzzCljg;
import com.huabo.audit.util.PageInfo;

public class TblYqnsWgzzCljgMapperSqlConfig {

    public String getByCljgCount(PageInfo<TblYqnsWgzzCljg> pageInfo, TblYqnsWgzzCljg tblYqnsWgzzCljg){
        StringBuffer sb = new StringBuffer("SELECT COUNT(0) FROM TBL_YQNS_WGZZ_CLJG TNA WHERE TNA.CREATOR="+tblYqnsWgzzCljg.getCreator()+" ");

        if(tblYqnsWgzzCljg.getClnumber() != null){
            sb.append("AND TNA.CLNUMBER Like '%"+tblYqnsWgzzCljg.getClnumber()+"%'");
        }
        if(tblYqnsWgzzCljg.getClname() != null){
            sb.append("AND TNA.CLNAME Like '%"+tblYqnsWgzzCljg.getClname()+"%'");
        }
        if(tblYqnsWgzzCljg.getStatus() != null){
            sb.append("AND TNA.STATUS = "+tblYqnsWgzzCljg.getStatus());
        }

        return sb.toString();
    }

    public String getByCljgList(PageInfo<TblYqnsWgzzCljg> pageInfo, TblYqnsWgzzCljg tblYqnsWgzzCljg) {
        StringBuffer sb = new StringBuffer("SELECT * FROM "
                + "(SELECT T1.*,ROWNUM RN  FROM "
                + "(SELECT TNA.* "
                + "FROM TBL_YQNS_WGZZ_CLJG TNA "
                + "WHERE TNA.CREATOR="+tblYqnsWgzzCljg.getCreator()+" ");

        if(tblYqnsWgzzCljg.getClnumber() != null){
            sb.append("AND TNA.CLNUMBER Like '%"+tblYqnsWgzzCljg.getClnumber()+"%'");
        }
        if(tblYqnsWgzzCljg.getClname() != null){
            sb.append("AND TNA.CLNAME Like '%"+tblYqnsWgzzCljg.getClname()+"%'");
        }
        if(tblYqnsWgzzCljg.getStatus() != null){
            sb.append("AND TNA.STATUS = "+tblYqnsWgzzCljg.getStatus());
        }

        sb.append(" ORDER BY TNA.ID DESC) T1 WHERE ROWNUM <= "+(pageInfo.getCurrentRecord()+pageInfo.getPageSize())+" ) T2 WHERE T2.RN > "+pageInfo.getCurrentRecord());
        return sb.toString();
    }

}
