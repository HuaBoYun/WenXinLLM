package com.huabo.audit.oracle.mapper;
import com.huabo.audit.oracle.entity.TblWgzzEntity;
import com.huabo.audit.oracle.vo.TblWgzzVo;
import com.huabo.audit.util.PageInfo;

/**
 * @Created with IDEA 2019
 * @package: com.huabo.audit.oracle.mapper
 * @project_name: SVN4
 * @author:wjh
 * @Date:2023/4/26
 * @Time:13:56
 */
public class TblwgzzMapperSqlConfig {

    public String getwgzzContList(PageInfo<TblWgzzEntity> pageInfo, String clueNaber) {
        StringBuffer sb = new StringBuffer("SELECT COUNT(0) FROM TBL_WGZZ_WGZZ  WHERE 1=1 ");

        if(clueNaber != null){
            sb.append("AND CLUENABER LIKE '%"+clueNaber+"%'");
        }

        return sb.toString();
    }
    public String getwgzzList(PageInfo<TblWgzzEntity> pageInfo, TblWgzzEntity param) {
        StringBuffer sb = new StringBuffer("SELECT * FROM "
                + "(SELECT T1.*,ROWNUM RN  FROM "
                + "(SELECT TNA.*,TWS.VERIFYCONTENT as VERIFYCONTENTNEW "
                + "FROM TBL_WGZZ_WGZZ TNA "
				+ "LEFT JOIN TBL_WGZZ_SHBG TWS ON TNA.CLUEID=TWS.CLUEID "
                + "WHERE 1=1 ");
                 /*SELECT * FROM "
                + "(SELECT T1.*,ROWNUM RN  FROM "
                + "(SELECT TNA.*,TS.REALNAME CREATORNAME"
                + "FROM TBL_WGZZ_WGZZ TNA "
                + "LEFT JOIN TBL_STAFF TS ON TNA.CREATOR = TS.STAFFID"
                + "WHERE 1=1*/

        if(param.getCluenaber()!=null && param.getCluenaber().length()>0) {
            sb.append("AND TNA.CLUENABER LIKE '%"+param.getCluenaber()+"%'");
        }
		if(param.getIsaccepted()!=null) {
			sb.append("AND ISACCEPTED ="+param.getIsaccepted()+"");
		}

        sb.append(" ORDER BY TNA.CLUEID DESC) T1 WHERE ROWNUM <= "+(pageInfo.getCurrentRecord()+pageInfo.getPageSize())+" ) T2 WHERE T2.RN > "+pageInfo.getCurrentRecord());
        return sb.toString();
    }
    
    public String getwgzzYsList(PageInfo<TblWgzzEntity> pageInfo, TblWgzzEntity param,Integer type) {
        StringBuffer sb = new StringBuffer("SELECT * FROM "
                + "(SELECT T1.*,ROWNUM RN  FROM "
                + "(SELECT TNA.*,TWS.VERIFYCONTENT as VERIFYCONTENTNEW "
                + "FROM TBL_WGZZ_WGZZ TNA "
				+ "LEFT JOIN TBL_WGZZ_SHBG TWS ON TNA.CLUEID=TWS.CLUEID "
				+ "LEFT JOIN TBL_WGZZ_WGHS TWW ON TNA.CLUEID=TWW.CLUEID "
                + "WHERE 1=1   ");

        
        if(type!=null && type==3) {
        	sb.append(" AND TWW.WGHSSCOPE='是' ");
        }
        
        if(param.getCluenaber()!=null && param.getCluenaber().length()>0) {
            sb.append("AND TNA.CLUENABER LIKE '%"+param.getCluenaber()+"%'");
        }
		if(param.getIsaccepted()!=null) {
			sb.append("AND ISACCEPTED ="+param.getIsaccepted()+"");
		}

        sb.append(" ORDER BY TNA.CLUEID DESC) T1 WHERE ROWNUM <= "+(pageInfo.getCurrentRecord()+pageInfo.getPageSize())+" ) T2 WHERE T2.RN > "+pageInfo.getCurrentRecord());
        return sb.toString();
    }
}
