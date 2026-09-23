package com.huabo.audit.oracle.mapper;

import java.math.BigDecimal;
import java.util.Date;

import com.hbfk.util.DateUtil;
import com.hbfk.util.database.DataBaseSqlConfig;
import com.huabo.audit.oracle.entity.TblAttachment;
import com.huabo.audit.oracle.entity.TblNbsjOperateEntity;
import com.huabo.audit.util.PageInfo;

public class TblAttachmentMapperSqlConfig {

	public String insertEntity(TblAttachment att) throws Exception {
		StringBuffer colSb = new StringBuffer("INSERT INTO TBL_ATTACHMENT(ATTID,UPLOADER,UPLOADTIME,ATTSIZE");
		StringBuffer valSb = new StringBuffer(" VALUES ( '"+att.getAttid()+"','"+att.getUploader()+"',"+DataBaseSqlConfig.getDateHmsStrFormat(new Date())+","+att.getAttsize());
		
		if(att.getAttname() != null && !"".equals(att.getAttname())) {
			colSb.append(",ATTNAME");
			valSb.append(",'"+att.getAttname()+"'");
		}
		
		if(att.getAttpath() != null && !"".equals(att.getAttpath())) {
			colSb.append(",ATTPATH");
			valSb.append(",'"+att.getAttpath()+"'");
		}
		
		//if(att.getAttsize()!=null) {
			//colSb.append(",ATTSIZE");
			//valSb.append(",'"+att.getAttsize()+"'");
		//}
		
		if(att.getMemo() != null && !"".equals(att.getMemo())) {
			colSb.append(",MEMO");
			valSb.append(",'"+att.getMemo()+"'");
		}
		
		if(att.getMemo() != null && !"".equals(att.getMemo())) {
			colSb.append(",UPLOADTIME");
			valSb.append(",'"+att.getMemo()+"'");
		}
		
		String sql = colSb.toString()+")"+valSb.toString()+")";
		return sql;
	}
	
	public String findAttachmentListByattids(String attids) {
		StringBuffer sql = new StringBuffer("SELECT * FROM TBL_ATTACHMENT WHERE ATTID IN ("+attids+")");
		
		return sql.toString();
	}
	
	public String updatexfry(String staffids, BigDecimal attid) {
		StringBuffer sql = new StringBuffer("UPDATE TBL_ATTACHMENT SET  STAFFIDS='"+staffids+"' WHERE ATTID="+attid+"");
		
		return sql.toString();
	}
	
	public String findAttachmentListByData(BigDecimal dataId,String staffid) {
		StringBuffer sql = new StringBuffer("SELECT * FROM TBL_ATTACHMENT WHERE ATTID IN (SELECT ATTID FROM TBL_LEGAL_DATAPROJECT_ATT WHERE DATAID = '"+dataId+"') and STAFFIDS like '%"+staffid+"%'");
		
		return sql.toString();
	}
	
	
	
	 
	
	
	//==
	public String selectListByPageInfo(PageInfo<TblNbsjOperateEntity> pageInfo,String attname,Integer orgid) {
		StringBuffer sb = new StringBuffer("SELECT * FROM "
				+ "(SELECT T1.*,ROWNUM RN  FROM "
				+ "(SELECT ATTID,ATTNAME,ATTPATH,ATTSIZE,MEMO,UPLOADTIME,UPLOADER "
				+ "FROM TBL_ATTACHMENT TNA "
				+ "WHERE ATTID in "
				+ "(SELECT ATTID FROM TBL_NBSJ_SHEETATT WHERE SHEETID IN "
				+ "(SELECT SHEETID FROM TBL_NBSJ_SHEET WHERE PROJECTID IN "
				+ "(SELECT PROJECTID FROM TBL_NBSJ_PROJECT WHERE ORGID = "+orgid+")))");
		
		if(attname!=null && attname.length()>0) {
			sb.append(" AND TNA.ATTNAME LIKE '%"+attname+"%'");
		}
		
		sb.append(" ORDER BY TNA.ATTID DESC) T1 WHERE ROWNUM <= "+(pageInfo.getCurrentRecord()+pageInfo.getPageSize())+" ) T2 WHERE T2.RN > "+pageInfo.getCurrentRecord());
		return sb.toString();
	}
	public String selectCountByPageInfo(PageInfo<TblNbsjOperateEntity> pageInfo,String attname,Integer orgid) {
		StringBuffer sb = new StringBuffer("SELECT COUNT(0) "
				+ "FROM TBL_ATTACHMENT TNA "
				+ "WHERE ATTID in "
				+ "(SELECT ATTID FROM TBL_NBSJ_SHEETATT WHERE SHEETID IN "
				+ "(SELECT SHEETID FROM TBL_NBSJ_SHEET WHERE PROJECTID IN "
				+ "(SELECT PROJECTID FROM TBL_NBSJ_PROJECT WHERE ORGID = "+orgid+")))");
		
		if(attname!=null && attname.length()>0) {
			sb.append(" AND TNA.ATTNAME LIKE '%"+attname+"%'");
		}
		
		return sb.toString();
	}
	
	
	  public String findAttachmentListInIds(String attids) {
	    	StringBuffer sb = new StringBuffer("SELECT ATTID,ATTNAME,ATTPATH,ATTSIZE,MEMO,UPLOADTIME,UPLOADER "
	    			+ "FROM TBL_ATTACHMENT "
	    			+ "WHERE ATTID IN ("+attids+") ");
			
			return sb.toString();
	    }
	    
}
