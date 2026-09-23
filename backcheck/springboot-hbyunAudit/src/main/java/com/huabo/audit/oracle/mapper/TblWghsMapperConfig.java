package com.huabo.audit.oracle.mapper;
import com.huabo.audit.oracle.entity.TblWgzzWghs;
import com.huabo.audit.util.PageInfo;

/**
 * @Created with IDEA 2019
 * @package: com.huabo.audit.oracle.mapper
 * @project_name: SVN4
 * @author:wjh
 * @Date:2023/4/27
 * @Time:10:47
 */
public class TblWghsMapperConfig {

    public String getByContWghsList(PageInfo<TblWgzzWghs> pageInfo,String clueNaber,String verifycontent) {
        StringBuffer sb = new StringBuffer("SELECT COUNT(0) FROM TBL_WGZZ_WGHS TNA WHERE 1=1 ");

        if(clueNaber != null){
            sb.append("AND CLUENABER LIKE '%"+clueNaber+"%'");
        }
        if(verifycontent !=null) {
			sb.append("AND verifycontent like "+ "'%"+verifycontent+"%' ");
        }

        return sb.toString();
    }

    public String getByContWghsBHList(PageInfo<TblWgzzWghs> pageInfo,String clueNaber) {
        StringBuffer sb = new StringBuffer("SELECT COUNT(0) FROM TBL_WGZZ_WGHS TNA WHERE 1=1 AND STATUS = 6 and  WGHSSCOPE='是' ");

        if(clueNaber != null){
            sb.append("AND CLUENABER LIKE '%"+clueNaber+"%'");
        }

        return sb.toString();
    }
 /*   public String updateEntity(TblWgzzWghs plan) {
        StringBuffer sqlSb = new StringBuffer("UPDATE TBL_WGZZ_WGHS SET CLUENABER = '"+plan.getClueNaber()+"' ");

        if(plan.getVerifyconTent() != null && !"".equals(plan.getVerifyconTent())) {
            sqlSb.append(" ,VERIFYCONTENT = '"+plan.getVerifyconTent()+"'");
        }
        if(plan.getClueHsfw() != null && !"".equals(plan.getClueHsfw())) {
            sqlSb.append(" ,CLUEHSFW = '"+plan.getClueHsfw()+"'");
        }
        if(plan.getClueGzzz() != null && !"".equals(plan.getClueGzzz())) {
            sqlSb.append(" ,CLUEGZZZ = '"+plan.getClueGzzz()+"'");
        }
        if(plan.getRepdesc() != null && !"".equals(plan.getRepdesc())) {
            sqlSb.append(" ,REPDESC = '"+plan.getRepdesc()+"'");
        }

        sqlSb.append(" WHERE ID= "+plan.getId());
        return sqlSb.toString();
    }*/

   /* public String insertEntity(TblWgzzWghs plan){
        StringBuffer colSb = new StringBuffer("INSERT INTO TBL_WGZZ_WGHS(CLUENABER,VERIFYCONTENT,CLUENAME,CLUEHSFW,CLUEGZZZ,REPDESC");
        StringBuffer valSb = new StringBuffer("  VALUES (HIBERNATE_SEQUENCE.nextval,TO_DATE('"+ DateUtil.parseDate(plan.getAptime(), "yyyy-MM-dd HH:mm:ss"));

        if(plan.getClueNaber() != null && !"".equals(plan.getClueNaber())) {
            colSb.append(",CLUENABER");
            valSb.append(",'"+plan.getClueNaber()+"'");
        }

        if(plan.getVerifyconTent() != null && !"".equals(plan.getVerifyconTent())) {
            colSb.append(",VERIFYCONTENT");
            valSb.append(",'"+plan.getVerifyconTent()+"'");
        }

        if(plan.getClueName() != null && !"".equals(plan.getClueName())) {
            colSb.append(",CLUENAME");
            valSb.append(",'"+plan.getClueName()+"'");
        }

        if(plan.getClueHsfw() != null && !"".equals(plan.getClueHsfw())) {
            colSb.append(",CLUEHSFW");
            valSb.append(",'"+plan.getClueHsfw()+"'");
        }

        if(plan.getClueGzzz() != null && !"".equals(plan.getClueGzzz())) {
            colSb.append(",CLUEGZZZ");
            valSb.append(",'"+plan.getClueGzzz()+"'");
        }
        if(plan.getRepdesc() != null && !"".equals(plan.getRepdesc())) {
            colSb.append(",REPDESC");
            valSb.append(",'"+plan.getRepdesc()+"'");
        }


        String sql = colSb.toString()+")"+valSb.toString()+")";
        return sql;
    }
*/

      public String getByWghsList(PageInfo<TblWgzzWghs> pageInfo, String clueNaber,String verifycontent) {
        StringBuffer sb = new StringBuffer("SELECT * FROM "
                + "(SELECT T1.*,ROWNUM RN  FROM "
                + "(SELECT TNA.* "
                + "FROM TBL_WGZZ_WGHS TNA "
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

    public String getByWghsBHList(PageInfo<TblWgzzWghs> pageInfo, String clueNaber) {
        StringBuffer sb = new StringBuffer("SELECT * FROM "
                + "(SELECT T1.*,ROWNUM RN  FROM "
                + "(SELECT TNA.*"
                + "FROM TBL_WGZZ_WGHS TNA "
                + "WHERE 1=1 "
                +"AND STATUS = 6 and  WGHSSCOPE='是' ");

        if(clueNaber !=null && clueNaber.length()>0) {
            sb.append("AND CLUENABER LIKE '%"+clueNaber+"%'");
        }

        sb.append(" ORDER BY TNA.ID DESC) T1 WHERE ROWNUM <= "+(pageInfo.getCurrentRecord()+pageInfo.getPageSize())+" ) T2 WHERE T2.RN > "+pageInfo.getCurrentRecord());
        return sb.toString();
    }



}
