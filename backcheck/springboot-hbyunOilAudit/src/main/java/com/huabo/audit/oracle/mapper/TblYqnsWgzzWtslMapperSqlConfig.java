package com.huabo.audit.oracle.mapper;

import com.huabo.audit.oracle.entity.TblYqnsWgzzWtsl;
import com.huabo.audit.util.PageInfo;

public class TblYqnsWgzzWtslMapperSqlConfig {

    public String getByWtslCount(PageInfo<TblYqnsWgzzWtsl> pageInfo, TblYqnsWgzzWtsl tblYqnsWgzzWtsl){
        StringBuffer sb = new StringBuffer("SELECT COUNT(0) FROM TBL_YQNS_WGZZ_WTSL TNA WHERE TNA.CREATOR="+tblYqnsWgzzWtsl.getCreator()+" ");

        if(tblYqnsWgzzWtsl.getSlnumber() != null){
            sb.append("AND TNA.SLNUMBER Like '%"+tblYqnsWgzzWtsl.getSlnumber()+"%'");
        }
        if(tblYqnsWgzzWtsl.getSlname() != null){
            sb.append("AND TNA.SLNAME Like '%"+tblYqnsWgzzWtsl.getSlname()+"%'");
        }
        if(tblYqnsWgzzWtsl.getStatus() != null){
            sb.append("AND TNA.STATUS = "+tblYqnsWgzzWtsl.getStatus());
        }
        if(tblYqnsWgzzWtsl.getIsuse() != null){
            sb.append("AND TNA.ISUSE = "+tblYqnsWgzzWtsl.getIsuse());
        }

        return sb.toString();
    }

    public String getByWtslList(PageInfo<TblYqnsWgzzWtsl> pageInfo, TblYqnsWgzzWtsl tblYqnsWgzzWtsl) {
        StringBuffer sb = new StringBuffer("SELECT * FROM "
                + "(SELECT T1.*,ROWNUM RN  FROM "
                + "(SELECT TNA.* "
                + "FROM TBL_YQNS_WGZZ_WTSL TNA "
                + "WHERE TNA.CREATOR="+tblYqnsWgzzWtsl.getCreator()+" ");

        if(tblYqnsWgzzWtsl.getSlnumber() != null){
            sb.append("AND TNA.SLNUMBER Like '%"+tblYqnsWgzzWtsl.getSlnumber()+"%'");
        }
        if(tblYqnsWgzzWtsl.getSlname() != null){
            sb.append("AND TNA.SLNAME Like '%"+tblYqnsWgzzWtsl.getSlname()+"%'");
        }
        if(tblYqnsWgzzWtsl.getStatus() != null){
            sb.append("AND TNA.STATUS = "+tblYqnsWgzzWtsl.getStatus());
        }
        if(tblYqnsWgzzWtsl.getIsuse() != null){
            sb.append("AND TNA.ISUSE = "+tblYqnsWgzzWtsl.getIsuse());
        }

        sb.append(" ORDER BY TNA.ID DESC) T1 WHERE ROWNUM <= "+(pageInfo.getCurrentRecord()+pageInfo.getPageSize())+" ) T2 WHERE T2.RN > "+pageInfo.getCurrentRecord());
        return sb.toString();
    }

}
