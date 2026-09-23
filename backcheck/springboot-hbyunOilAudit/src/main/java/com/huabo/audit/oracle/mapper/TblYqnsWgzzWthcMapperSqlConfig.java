package com.huabo.audit.oracle.mapper;

import com.huabo.audit.oracle.entity.TblYqnsWgzzWthc;
import com.huabo.audit.util.PageInfo;

public class TblYqnsWgzzWthcMapperSqlConfig {

    public String getByWthcCount(PageInfo<TblYqnsWgzzWthc> pageInfo, TblYqnsWgzzWthc tblYqnsWgzzWthc){
        StringBuffer sb = new StringBuffer("SELECT COUNT(0) FROM TBL_YQNS_WGZZ_WTHC TNA WHERE TNA.CREATOR="+tblYqnsWgzzWthc.getCreator()+" ");

        if(tblYqnsWgzzWthc.getEditorgid() != null){
            sb.append("AND TNA.EDITORGID = "+tblYqnsWgzzWthc.getEditorgid());
        }
        if(tblYqnsWgzzWthc.getHcname() != null){
            sb.append("AND TNA.HCNAME Like '%"+tblYqnsWgzzWthc.getHcname()+"%'");
        }
        if(tblYqnsWgzzWthc.getStatus() != null){
            sb.append("AND TNA.STATUS = "+tblYqnsWgzzWthc.getStatus());
        }
        if(tblYqnsWgzzWthc.getIsuse() != null){
            sb.append("AND TNA.ISUSE = "+tblYqnsWgzzWthc.getIsuse());
        }

        return sb.toString();
    }

    public String getByWthcList(PageInfo<TblYqnsWgzzWthc> pageInfo, TblYqnsWgzzWthc tblYqnsWgzzWthc) {
        StringBuffer sb = new StringBuffer("SELECT * FROM "
                + "(SELECT T1.*,ROWNUM RN  FROM "
                + "(SELECT TNA.*,ORG.ORGNAME editorgname "
                + "FROM TBL_YQNS_WGZZ_WTHC TNA "
                + "LEFT JOIN TBL_ORGANIZATION ORG ON ORG.ORGID = TNA.EDITORGID   "
                + "WHERE TNA.CREATOR="+tblYqnsWgzzWthc.getCreator()+" ");

        if(tblYqnsWgzzWthc.getEditorgid() != null){
            sb.append("AND TNA.EDITORGID = "+tblYqnsWgzzWthc.getEditorgid());
        }
        if(tblYqnsWgzzWthc.getHcname() != null){
            sb.append("AND TNA.HCNAME Like '%"+tblYqnsWgzzWthc.getHcname()+"%'");
        }
        if(tblYqnsWgzzWthc.getStatus() != null){
            sb.append("AND TNA.STATUS = "+tblYqnsWgzzWthc.getStatus());
        }
        if(tblYqnsWgzzWthc.getIsuse() != null){
            sb.append("AND TNA.ISUSE = "+tblYqnsWgzzWthc.getIsuse());
        }

        sb.append(" ORDER BY TNA.ID DESC) T1 WHERE ROWNUM <= "+(pageInfo.getCurrentRecord()+pageInfo.getPageSize())+" ) T2 WHERE T2.RN > "+pageInfo.getCurrentRecord());
        return sb.toString();
    }

}
