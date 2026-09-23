package com.huabo.monitor.mapper;



import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.hbfk.util.PageInfo;
import com.huabo.monitor.entity.TblTestTemplate;

public class TblTestTemplateMapperSqlConfig {

	public String insertEntity(TblTestTemplate tblTestTemplate) {
		String colsql = "INSERT INTO TBL_TESTTEMPLE(TESTTEMID, TEMPLENUMBER, TEMPLENAME,CREATETIME,STAFFID, TBLCOMANY ,SOURCE ";
		String valueSql = " VALUES (HIBERNATE_SEQUENCE.nextval, '"+tblTestTemplate.getTempleNumber()+"', '"+tblTestTemplate.getTemplename()+"', SYSDATE, '"+tblTestTemplate.getStaffId()+"','"+tblTestTemplate.getTblComany()+"','"+tblTestTemplate.getSource()+"'";
		
		if (StringUtils.isNotBlank(tblTestTemplate.getMemo())) {
			colsql += ",MEMO ";
			valueSql += ",'"+tblTestTemplate.getMemo()+"'";
        }
		
		if (StringUtils.isNotBlank(tblTestTemplate.getTempleDesc())) {
			colsql += ",TEMPLEDESC ";
			valueSql += ",'"+tblTestTemplate.getTempleDesc()+"'";
        }
		
		if (tblTestTemplate.getTempleStatus() != null) {
			colsql += ",TEMPLESTATUS ";
			valueSql += ","+tblTestTemplate.getTempleStatus();
        }
		
		String sql = colsql + ")" +valueSql + ")";
		return sql ;
	}
	
	public String updateEntity(TblTestTemplate tblTestTemplate) {
		String sql = "UPDATE TBL_TESTTEMPLE SET TEMPLENUMBER = '"+tblTestTemplate.getTempleNumber()+"', TEMPLENAME = '"+tblTestTemplate.getTemplename()+"'";
		if (StringUtils.isNotBlank(tblTestTemplate.getMemo())) {
			sql += ", MEMO = '"+tblTestTemplate.getMemo()+"' ";
        }
		
		if (StringUtils.isNotBlank(tblTestTemplate.getTempleDesc())) {
			sql += ", TEMPLEDESC = '"+tblTestTemplate.getTempleDesc()+"'  ";
        }
		
		if (StringUtils.isNotBlank(tblTestTemplate.getIssued())) {
			sql += ", ISSUED = '"+tblTestTemplate.getIssued()+"'  ";
        }
		sql += " WHERE TESTTEMID = "+tblTestTemplate.getTesttemid();
		return sql ;
	}
	
	public String selectPageInfo(PageInfo<TblTestTemplate> pageInfo) {
		TblTestTemplate temp = pageInfo.getCondition();
		
		String sql = "SELECT * FROM ( SELECT TMP.*, ROWNUM ROW_ID FROM ( SELECT  TT.TESTTEMID,TT.TEMPLENUMBER,TT.TEMPLENAME,TT.MEMO,TT.STAFFID,TT.CREATETIME,TT.TEMPLESTATUS,TT.TEMPLEDESC,TT.SOURCE,TT.TBLCOMANY,TS.REALNAME,TT.ISSUED FROM TBL_TESTTEMPLE TT LEFT JOIN TBL_STAFF TS ON TT.STAFFID = TS.STAFFID WHERE TT.TBLCOMANY = "+temp.getTblComany();
		
		if (StringUtils.isNotBlank(temp.getTempleNumber())) {
			sql += " AND TT.TEMPLENUMBER LIKE '%"+temp.getTempleNumber()+"%'";
        }
		if (StringUtils.isNotBlank(temp.getTemplename())) {
			sql += " AND TT.TEMPLENAME LIKE '%"+temp.getTemplename()+"%'";
        }
		
		sql += " ORDER BY TT.TESTTEMID DESC ) TMP WHERE ROWNUM <= "+(pageInfo.getCurrentPage()*pageInfo.getPageSize())+") WHERE ROW_ID > "+pageInfo.getCurrentRecord();
		return sql;
	}
	
	public String selectPageCount(PageInfo<TblTestTemplate> pageInfo) {
		TblTestTemplate temp = pageInfo.getCondition();
		
		String sql = "SELECT COUNT(0) FROM TBL_TESTTEMPLE TT LEFT JOIN TBL_STAFF TS ON TT.STAFFID = TS.STAFFID WHERE TT.TBLCOMANY = "+temp.getTblComany();
		
		if (StringUtils.isNotBlank(temp.getTempleNumber())) {
			sql += " AND TT.TEMPLENUMBER LIKE '%"+temp.getTempleNumber()+"%'";
        }
		if (StringUtils.isNotBlank(temp.getTemplename())) {
			sql += " AND TT.TEMPLENAME LIKE '%"+temp.getTemplename()+"%'";
        }
		
		return sql;
	}

}
