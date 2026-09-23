package com.huabo.audit.oracle.mapper;

import com.hbfk.util.database.DataBaseSqlConfig;
import com.huabo.audit.oracle.entity.TblYqnsWgzzFlczyj;
import com.huabo.audit.util.PageInfo;

public class TblYqnsWgzzFlczyjMapperSqlConfig {

    public String getByFlczyjCount(PageInfo<TblYqnsWgzzFlczyj> pageInfo, TblYqnsWgzzFlczyj tblYqnsWgzzFlczyj)throws Exception {
        StringBuffer sb = new StringBuffer("SELECT COUNT(0) FROM TBL_YQNS_WGZZ_FLCZYJ TNA WHERE TNA.CREATOR="+tblYqnsWgzzFlczyj.getCreator()+" ");

        if(tblYqnsWgzzFlczyj.getClueid() != null){
            sb.append("AND TNA.CLUEID = "+tblYqnsWgzzFlczyj.getClueid());
        }
        if(tblYqnsWgzzFlczyj.getVerifycontent() != null){
            sb.append("AND TNA.VERIFYCONTENT Like '%"+tblYqnsWgzzFlczyj.getVerifycontent()+"%'");
        }
        if(tblYqnsWgzzFlczyj.getImpcreateusername() != null) {
        	sb.append(" AND TNA.IMPCREATEUSERNAME = "+ DataBaseSqlConfig.getDateStrFormat(tblYqnsWgzzFlczyj.getImpcreateusername()));
        }
        if(tblYqnsWgzzFlczyj.getStatus() != null){
            sb.append("AND TNA.STATUS = "+tblYqnsWgzzFlczyj.getStatus());
        }

        return sb.toString();
    }

    public String getByFlczyjList(PageInfo<TblYqnsWgzzFlczyj> pageInfo, TblYqnsWgzzFlczyj tblYqnsWgzzFlczyj)throws Exception {
        StringBuffer sb = new StringBuffer("SELECT * FROM "
                + "(SELECT T1.*,ROWNUM RN  FROM "
                + "(SELECT TNA.*,ORG.ORGNAME cluename "
                + "FROM TBL_YQNS_WGZZ_FLCZYJ TNA "
                + "LEFT JOIN TBL_ORGANIZATION ORG ON ORG.ORGID = TNA.CLUEID   "
                + "WHERE TNA.CREATOR="+tblYqnsWgzzFlczyj.getCreator()+" ");

        if(tblYqnsWgzzFlczyj.getClueid() != null){
            sb.append("AND TNA.CLUEID = "+tblYqnsWgzzFlczyj.getClueid());
        }
        if(tblYqnsWgzzFlczyj.getVerifycontent() != null){
            sb.append("AND TNA.VERIFYCONTENT Like '%"+tblYqnsWgzzFlczyj.getVerifycontent()+"%'");
        }
        if(tblYqnsWgzzFlczyj.getImpcreateusername() != null) {
        	sb.append(" AND TNA.IMPCREATEUSERNAME = "+ DataBaseSqlConfig.getDateStrFormat(tblYqnsWgzzFlczyj.getImpcreateusername()));
        }
        if(tblYqnsWgzzFlczyj.getStatus() != null){
            sb.append("AND TNA.STATUS = "+tblYqnsWgzzFlczyj.getStatus());
        }

        sb.append(" ORDER BY TNA.ID DESC) T1 WHERE ROWNUM <= "+(pageInfo.getCurrentRecord()+pageInfo.getPageSize())+" ) T2 WHERE T2.RN > "+pageInfo.getCurrentRecord());
        return sb.toString();
    }

}
