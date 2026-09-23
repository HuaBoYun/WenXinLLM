package com.huabo.compliance.mapper;

import com.hbfk.util.DateUtil;
import com.hbfk.util.PageInfo;
import com.huabo.compliance.entity.TblAttachment;
import com.huabo.compliance.entity.TblNbsjOperateEntity;

import java.util.Date;

public class TblAttachmentMapperSqlConfig {

	public String insertEntity(TblAttachment att) {
		StringBuffer colSb = new StringBuffer("INSERT INTO TBL_ATTACHMENT(ATTID,UPLOADER,UPLOADTIME");
		StringBuffer valSb = new StringBuffer(" VALUES ( HIBERNATE_SEQUENCE.nextval,'"+att.getUploader()+"',TO_DATE('"+DateUtil.parseDate(new Date(), "yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:mi:ss')");
		
		if(att.getAttname() != null && !"".equals(att.getAttname())) {
			colSb.append(",ATTNAME");
			valSb.append(",'"+att.getAttname()+"'");
		}
		
		if(att.getAttpath() != null && !"".equals(att.getAttpath())) {
			colSb.append(",ATTPATH");
			valSb.append(",'"+att.getAttpath()+"'");
		}
		
		if(!"".equals(att.getAttsize())) {
			colSb.append(",ATTSIZE");
			valSb.append(",'"+att.getAttsize()+"'");
		}
		
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
	
	
	
	
	
	
	//==
	public String selectListByPageInfo(PageInfo<TblNbsjOperateEntity> pageInfo, String attname, Integer orgid) {
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

	/**
	 * 仅修改TBL_ATTACHMENT表中tblBugs字段
	 */
	public String updateTblBugsInTblAttachment(TblAttachment attachment) {
		StringBuffer sql = new StringBuffer("UPDATE TBL_ATTACHMENT SET tblBugs = '" + attachment.getTblBugs() + "'");

		sql.append(" WHERE ATTID = " + attachment.getAttid());
		return sql.toString();
	}
}
