package com.huabo.monitor.mapper;



import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.hbfk.util.PageInfo;
import com.huabo.monitor.entity.TblAssesscategory;
import com.huabo.monitor.entity.TblTestTemplate;

public class TblAssesscategoryMapperSqlConfig {

	public String insertEntity(TblAssesscategory tblAssesscategory) {
		String colsql = "INSERT INTO TBL_ASSESSCATEGORY(ASSCATID, CATNAME, CATWEIGHT,ASSTEMID,CATDES";
		String valueSql = " VALUES (HIBERNATE_SEQUENCE.nextval, '"+tblAssesscategory.getCatname()+"', "+tblAssesscategory.getCatweight()+",'"+tblAssesscategory.getTblassesstemple().getAsstemid()+"','"+tblAssesscategory.getCatdes()+"'";
		String sql = colsql + ")" +valueSql + ")";
		return sql ;
	}
	
	public String updateEntity(TblAssesscategory tblAssesscategory) {
		String sql = "UPDATE TBL_ASSESSCATEGORY SET CATNAME = '"+tblAssesscategory.getCatname()+"'";
		if (tblAssesscategory.getCatweight()>0) {
			sql += ", CATWEIGHT ="+tblAssesscategory.getCatweight();
        }
		if (tblAssesscategory.getAsstemid()!=null) {
			sql += ", ASSTEMID ="+tblAssesscategory.getTblassesstemple().getAsstemid();
        }
		if (tblAssesscategory.getCatdes()!=null) {
			sql += ", CATDES ='"+tblAssesscategory.getCatdes()+"'";
        }
		sql += " WHERE ASSCATID = "+tblAssesscategory.getAsscatid();
		return sql ;
	}
	
}
