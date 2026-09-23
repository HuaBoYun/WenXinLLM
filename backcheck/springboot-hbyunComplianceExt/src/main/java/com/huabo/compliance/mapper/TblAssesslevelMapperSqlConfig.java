package com.huabo.compliance.mapper;



import com.hbfk.util.PageInfo;
import com.huabo.compliance.entity.TblAssesslevel;

public class TblAssesslevelMapperSqlConfig {

	 
	public String selectPageInfo(PageInfo<TblAssesslevel> pageInfo) {
		TblAssesslevel temp = pageInfo.getCondition();
		String sql = "SELECT * FROM ( SELECT TMP.*, ROWNUM ROW_ID FROM ( SELECT  * FROM TBL_ASSESSLEVEL TT   WHERE TT.TBLCOMANY = "+temp.getTblcomany();
		sql += " ORDER BY TT.ASSLEVID DESC ) TMP WHERE ROWNUM <= "+(pageInfo.getCurrentPage()*pageInfo.getPageSize())+") WHERE ROW_ID > "+pageInfo.getCurrentRecord();
		return sql;
	}
	
	public String selectPageCount(PageInfo<TblAssesslevel> pageInfo) {
		TblAssesslevel temp = pageInfo.getCondition();
		String sql = "SELECT COUNT(0) FROM TBL_ASSESSLEVEL TT  WHERE TT.TBLCOMANY = "+temp.getTblcomany();
		return sql;
	}

}
