package com.huabo.audit.oracle.mapper;

import com.hbfk.util.DateUtil;
import com.huabo.audit.oracle.entity.TblWgzzShbg;
import com.huabo.audit.oracle.entity.TblWgzzWghs;
import com.huabo.audit.util.PageInfo;

/**
 * @Created with IDEA 2019
 * @package: com.huabo.audit.oracle.mapper
 * @project_name: SVN4
 * @author:wjh
 * @Date:2023/5/4
 * @Time:11:43
 */
public class TblWgzzShbgMapperSqlConfig {

    public String getByContSHBGList(PageInfo<TblWgzzShbg> pageInfo, String clueNaber,String verifycontent){
        StringBuffer sb = new StringBuffer("SELECT COUNT(0) FROM TBL_WGZZ_SHBG TNA WHERE 1=1 ");

        if(clueNaber != null){
            sb.append("AND CLUENABER Like '%"+clueNaber+"%'");
        }
        if(verifycontent != null){
            sb.append("AND verifycontent Like '%"+verifycontent+"%'");
        }

        return sb.toString();
    }

    /*public String updateSelective(TblWgzzShbg plan) {
        StringBuffer sqlSb = new StringBuffer("UPDATE TBL_WGZZ_SHBG SET CLUENABER = '"+plan.getClueNaber()+"' ");

        if(plan.getVerifyconHsjg() != null && !"".equals(plan.getVerifyconHsjg())) {
            sqlSb.append(" ,VERIFYCONHSJG = '"+plan.getVerifyconHsjg()+"'");
        }
        if(plan.getClueGzjy() != null && !"".equals(plan.getClueGzjy())) {
            sqlSb.append(" ,CLUEGZJY = '"+plan.getClueGzjy()+"'");
        }
        sqlSb.append(" WHERE ID= "+plan.getId());
        return sqlSb.toString();
    }*/


    public String getByWghsSHBGList(PageInfo<TblWgzzShbg> pageInfo, String clueNaber,String verifycontent) {
        StringBuffer sb = new StringBuffer("SELECT * FROM "
                + "(SELECT T1.*,ROWNUM RN  FROM "
                + "(SELECT TNA.* "
                + "FROM TBL_WGZZ_SHBG TNA "
                + "WHERE 1=1 ");

        if(clueNaber !=null && clueNaber.length()>0) {
            sb.append("AND CLUENABER like "+ "'%"+clueNaber+"%' ");
        }
        if(verifycontent !=null && verifycontent.length()>0) {
            sb.append("AND verifycontent like "+ "'%"+verifycontent+"%' ");
        }

        sb.append(" ORDER BY TNA.ID DESC) T1 WHERE ROWNUM <= "+(pageInfo.getCurrentRecord()+pageInfo.getPageSize())+" ) T2 WHERE T2.RN > "+pageInfo.getCurrentRecord());
        return sb.toString();
    }

}
