package com.huabo.compliance.mapper;

import com.huabo.compliance.entity.TblAssEleCategory;

public class TblAssEleCategoryMapperSqlConfig {

	public String insertEle(TblAssEleCategory assEleCategory) {
		String colsql = "INSERT INTO TBL_ASSELE_CATEGORY(ELEMENTCATEGORYID,STANDARDSCORE";
		String valueSql = " VALUES (HIBERNATE_SEQUENCE.nextval,5";
				if(assEleCategory.getAssesselement().getAsseleid()!=null){
					colsql+=",ASSELEID";
					valueSql+=","+assEleCategory.getAssesselement().getAsseleid()+"";
				}
				if(assEleCategory.getAssesscategory().getAsscatid()!=null){
					colsql+=",ASSCATID";
					valueSql+=","+assEleCategory.getAssesscategory().getAsscatid();
				}
		String sql = colsql + ")" +valueSql + ")";
		return sql ;
	}
	
	public String updateEle(TblAssEleCategory assEleCategory) {
		String sql = "UPDATE Tbl_assesstemple SET STANDARDSCORE = 5";
		if(assEleCategory.getAssesselement().getAsseleid()!=null){
			sql+=",ASSELEID="+assEleCategory.getAssesselement().getAsseleid();
		}
		if(assEleCategory.getAssesscategory().getAsscatid()!=null){
			sql+=",ASSCATID="+assEleCategory.getAssesscategory().getAsscatid();
		}
		sql += " WHERE ELEMENTCATEGORYID = "+assEleCategory.getElementcategoryid();
		return sql ;
	}
}
