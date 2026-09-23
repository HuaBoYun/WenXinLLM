package com.huabo.audit.oracle.mapper;

import java.math.BigDecimal;

import com.hbfk.util.PageInfo;
import com.huabo.audit.oracle.entity.TblNbsjQuestionType;
import com.huabo.audit.oracle.entity.TblNbsjStatType;

public class TblNbsjQuestionTypeMapperSqlConfig {
	public String selectNbsjQuestionTypeListByPageInfo(PageInfo<TblNbsjQuestionType> pageInfo,BigDecimal orgid) throws Exception{
		StringBuffer sqlSb = new StringBuffer("SELECT * FROM (SELECT T1.*,ROWNUM RN  FROM (");
		sqlSb.append("SELECT * from TBL_NBSJ_QUESTION_TYPE  WHERE ORGID="+orgid);
		sqlSb.append(") T1 WHERE ROWNUM <= "+(pageInfo.getCurrentRecord()+pageInfo.getPageSize())+" ) T2 WHERE RN > "+pageInfo.getCurrentRecord()+" ORDER BY typeid DESC");
		return sqlSb.toString();
	}
	public String selectNbsjQuestionTypeListCountByPageInfo(PageInfo<TblNbsjQuestionType> pageInfo, BigDecimal orgid){
		StringBuffer sqlSb = new StringBuffer("SELECT COUNT(0) from TBL_NBSJ_QUESTION_TYPE  WHERE ORGID="+orgid);
		return sqlSb.toString();
	}
	
	
	//==
	public String selectNbsjStatTypeListByPageInfo(PageInfo<TblNbsjStatType> pageInfo,BigDecimal orgid) throws Exception{
		StringBuffer sqlSb = new StringBuffer("SELECT * FROM (SELECT T1.*,ROWNUM RN  FROM (");
		sqlSb.append("SELECT * from TBL_NBSJ_STAT_TYPE  WHERE ORGID="+orgid);
		sqlSb.append(") T1 WHERE ROWNUM <= "+(pageInfo.getCurrentRecord()+pageInfo.getPageSize())+" ) T2 WHERE RN > "+pageInfo.getCurrentRecord()+" ORDER BY typeid DESC");
		return sqlSb.toString();
	}
	public String selectNbsjStatTypeListCountByPageInfo(PageInfo<TblNbsjStatType> pageInfo, BigDecimal orgid){
		StringBuffer sqlSb = new StringBuffer("SELECT COUNT(0) from TBL_NBSJ_STAT_TYPE  WHERE ORGID="+orgid);
		return sqlSb.toString();
	}
}
