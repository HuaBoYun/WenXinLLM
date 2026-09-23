package com.huabo.audit.oracle.mapper;

import com.huabo.audit.oracle.entity.TblYqnsWgzzWtdz;
import com.huabo.audit.util.PageInfo;

public class TblYqnsWgzzWtdzMapperSqlConfig {

    public String getByWtdzCount(PageInfo<TblYqnsWgzzWtdz> pageInfo, TblYqnsWgzzWtdz tblYqnsWgzzWtdz){
        StringBuffer sb = new StringBuffer("SELECT COUNT(0) FROM TBL_YQNS_WGZZ_WTDZ TNA "
        		+ " WHERE 1=1 AND (TNA.CREATOR="+tblYqnsWgzzWtdz.getCreator()+" "
                + " OR TNA.ID IN (SELECT ID FROM TBL_YQNS_WGZZ_WTDZ_ISSUE WHERE STAFFID = "+tblYqnsWgzzWtdz.getCreator()+"))  ");

        if(tblYqnsWgzzWtdz.getEditorgid() != null){
            sb.append("AND TNA.EDITORGID = "+tblYqnsWgzzWtdz.getEditorgid());
        }
        if(tblYqnsWgzzWtdz.getDzname() != null){
            sb.append("AND TNA.DZNAME Like '%"+tblYqnsWgzzWtdz.getDzname()+"%'");
        }
        if(tblYqnsWgzzWtdz.getStatus() != null){
            sb.append("AND TNA.STATUS = "+tblYqnsWgzzWtdz.getStatus());
        }
        if(tblYqnsWgzzWtdz.getIsuse() != null){
            sb.append("AND TNA.ISUSE = "+tblYqnsWgzzWtdz.getIsuse());
        }

        return sb.toString();
    }

    public String getByWtdzList(PageInfo<TblYqnsWgzzWtdz> pageInfo, TblYqnsWgzzWtdz tblYqnsWgzzWtdz) {
        StringBuffer sb = new StringBuffer("SELECT * FROM "
                + "(SELECT T1.*,ROWNUM RN  FROM "
                + "(SELECT TNA.*,ORG.ORGNAME editorgname,wthc.hcname,wthc.hcnumber "
                + "FROM TBL_YQNS_WGZZ_WTDZ TNA "
                + "LEFT JOIN TBL_ORGANIZATION ORG ON ORG.ORGID = TNA.EDITORGID   "
                + "LEFT JOIN TBL_YQNS_WGZZ_WTHC wthc ON wthc.ID = TNA.HCID   "
                + "WHERE 1=1 AND (TNA.CREATOR="+tblYqnsWgzzWtdz.getCreator()+" "
                + " OR TNA.ID IN (SELECT ID FROM TBL_YQNS_WGZZ_WTDZ_ISSUE WHERE STAFFID = "+tblYqnsWgzzWtdz.getCreator()+"))  ");

        if(tblYqnsWgzzWtdz.getEditorgid() != null){
            sb.append("AND TNA.EDITORGID = "+tblYqnsWgzzWtdz.getEditorgid());
        }
        if(tblYqnsWgzzWtdz.getDzname() != null){
            sb.append("AND TNA.DZNAME Like '%"+tblYqnsWgzzWtdz.getDzname()+"%'");
        }
        if(tblYqnsWgzzWtdz.getStatus() != null){
            sb.append("AND TNA.STATUS = "+tblYqnsWgzzWtdz.getStatus());
        }
        if(tblYqnsWgzzWtdz.getIsuse() != null){
            sb.append("AND TNA.ISUSE = "+tblYqnsWgzzWtdz.getIsuse());
        }

        sb.append(" ORDER BY TNA.ID DESC) T1 WHERE ROWNUM <= "+(pageInfo.getCurrentRecord()+pageInfo.getPageSize())+" ) T2 WHERE T2.RN > "+pageInfo.getCurrentRecord());
        return sb.toString();
    }

}
