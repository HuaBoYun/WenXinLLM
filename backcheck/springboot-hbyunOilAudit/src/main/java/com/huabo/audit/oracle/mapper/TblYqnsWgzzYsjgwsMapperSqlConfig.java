package com.huabo.audit.oracle.mapper;

import com.huabo.audit.oracle.entity.TblYqnsWgzzYsjgws;
import com.huabo.audit.util.PageInfo;

public class TblYqnsWgzzYsjgwsMapperSqlConfig {

    public String getByYsjgwsCount(PageInfo<TblYqnsWgzzYsjgws> pageInfo, TblYqnsWgzzYsjgws tblYqnsWgzzYsjgws){
        StringBuffer sb = new StringBuffer("SELECT COUNT(0) FROM TBL_YQNS_WGZZ_YSJGWS TNA WHERE TNA.CREATOR="+tblYqnsWgzzYsjgws.getCreator()+" ");

        if(tblYqnsWgzzYsjgws.getWsnumber() != null){
            sb.append("AND TNA.WSNUMBER Like '%"+tblYqnsWgzzYsjgws.getWsnumber()+"%'");
        }
        
        if(tblYqnsWgzzYsjgws.getWstitle() != null){
            sb.append("AND TNA.WSTITLE Like '%"+tblYqnsWgzzYsjgws.getWstitle()+"%'");
        }
        
        if(tblYqnsWgzzYsjgws.getStatus() != null){
            sb.append("AND TNA.STATUS = "+tblYqnsWgzzYsjgws.getStatus());
        }

        return sb.toString();
    }

    public String getByYsjgwsList(PageInfo<TblYqnsWgzzYsjgws> pageInfo, TblYqnsWgzzYsjgws tblYqnsWgzzYsjgws) {
        StringBuffer sb = new StringBuffer("SELECT * FROM "
                + "(SELECT T1.*,ROWNUM RN  FROM "
                + "(SELECT TNA.*,STAFF.REALNAME handstaffname "
                + "FROM TBL_YQNS_WGZZ_YSJGWS TNA "
                + "LEFT JOIN TBL_STAFF STAFF ON STAFF.STAFFID = TNA.HANDSTAFFID   "
                + "WHERE TNA.CREATOR="+tblYqnsWgzzYsjgws.getCreator()+" ");

        if(tblYqnsWgzzYsjgws.getWsnumber() != null){
            sb.append("AND TNA.WSNUMBER Like '%"+tblYqnsWgzzYsjgws.getWsnumber()+"%'");
        }
        
        if(tblYqnsWgzzYsjgws.getWstitle() != null){
            sb.append("AND TNA.WSTITLE Like '%"+tblYqnsWgzzYsjgws.getWstitle()+"%'");
        }
        
        if(tblYqnsWgzzYsjgws.getStatus() != null){
            sb.append("AND TNA.STATUS = "+tblYqnsWgzzYsjgws.getStatus());
        }

        sb.append(" ORDER BY TNA.ID DESC) T1 WHERE ROWNUM <= "+(pageInfo.getCurrentRecord()+pageInfo.getPageSize())+" ) T2 WHERE T2.RN > "+pageInfo.getCurrentRecord());
        return sb.toString();
    }

}
