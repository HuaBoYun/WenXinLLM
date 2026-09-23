package com.huabo.audit.oracle.mapper;

import com.huabo.audit.oracle.entity.TblYqnsWgzzYstz;
import com.huabo.audit.util.PageInfo;

public class TblYqnsWgzzYstzMapperSqlConfig {

    public String getByYstzCount(PageInfo<TblYqnsWgzzYstz> pageInfo, TblYqnsWgzzYstz tblYqnsWgzzYstz){
        StringBuffer sb = new StringBuffer("SELECT COUNT(0) FROM TBL_YQNS_WGZZ_YSTZ TNA WHERE TNA.CREATOR="+tblYqnsWgzzYstz.getCreator()+" ");

        if(tblYqnsWgzzYstz.getProblemclue() != null){
            sb.append("AND TNA.PROBLEMCLUE Like '%"+tblYqnsWgzzYstz.getProblemclue()+"%'");
        }
        
        if(tblYqnsWgzzYstz.getCheckresult() != null){
            sb.append("AND TNA.CHECKRESULT Like '%"+tblYqnsWgzzYstz.getCheckresult()+"%'");
        }

        return sb.toString();
    }

    public String getByYstzList(PageInfo<TblYqnsWgzzYstz> pageInfo, TblYqnsWgzzYstz tblYqnsWgzzYstz) {
        StringBuffer sb = new StringBuffer("SELECT * FROM "
                + "(SELECT T1.*,ROWNUM RN  FROM "
                + "(SELECT TNA.*,ORG.ORGNAME imporgname,IMP.PROJECT_NAME projectname "
                + "FROM TBL_YQNS_WGZZ_YSTZ TNA "
                + "LEFT JOIN TBL_ORGANIZATION ORG ON ORG.ORGID = TNA.IMPORGID   "
                + "LEFT JOIN TBL_YQNS_IMPLEMENT_PLAN IMP ON IMP.ID = TNA.PROJECTID   "
                + "WHERE TNA.CREATOR="+tblYqnsWgzzYstz.getCreator()+" ");

        if(tblYqnsWgzzYstz.getProblemclue() != null){
            sb.append("AND TNA.PROBLEMCLUE Like '%"+tblYqnsWgzzYstz.getProblemclue()+"%'");
        }
        
        if(tblYqnsWgzzYstz.getCheckresult() != null){
            sb.append("AND TNA.CHECKRESULT Like '%"+tblYqnsWgzzYstz.getCheckresult()+"%'");
        }

        sb.append(" ORDER BY TNA.ID DESC) T1 WHERE ROWNUM <= "+(pageInfo.getCurrentRecord()+pageInfo.getPageSize())+" ) T2 WHERE T2.RN > "+pageInfo.getCurrentRecord());
        return sb.toString();
    }

}
