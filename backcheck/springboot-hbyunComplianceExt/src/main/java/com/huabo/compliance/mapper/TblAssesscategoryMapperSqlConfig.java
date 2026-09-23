package com.huabo.compliance.mapper;



import com.huabo.compliance.entity.TblAssesscategory;

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
