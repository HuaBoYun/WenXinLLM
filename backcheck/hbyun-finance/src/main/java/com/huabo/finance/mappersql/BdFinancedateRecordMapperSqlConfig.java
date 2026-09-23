package com.huabo.finance.mappersql;

import java.util.Date;

import org.apache.commons.lang.StringUtils;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hbfk.util.database.DataBaseSqlConfig;
import com.huabo.finance.vo.BdFinancedateRecordVo;
import com.huabo.finance.vr.BdFinanceplanVr;

public class BdFinancedateRecordMapperSqlConfig {

	public String updatePlanRecordStatus(String planId, String pid) throws Exception{
		StringBuffer sqlSb = new StringBuffer("UPDATE BD_FINANCEDATE_RECORD SET ISCOMPLETED = 2 , ENDDATE = ").append(DataBaseSqlConfig.getDateHmsStrFormat(new Date()));
		
		sqlSb.append("WHERE RECORDID = '").append(pid).append("'");
		
		String sql = sqlSb.toString();
		return sql;
	}
	
	
	public String selectGoonInfoPageInfo(IPage<BdFinanceplanVr> page, BdFinancedateRecordVo vo) throws Exception{
		StringBuffer sqlSb = new StringBuffer("SELECT FR.*,FP.FNAME FROM BD_FINANCEDATE_RECORD FR LEFT JOIN BD_FINANCEPLAN FP ON FR.PLANID = FP.FID ");
		
		sqlSb.append(" WHERE 1 = 1 ");
		
		if(vo.getIscompleted() != null) {
			sqlSb.append(" AND FR.ISCOMPLETED = ").append(vo.getIscompleted());
		}
		
		if(StringUtils.isNotBlank(vo.getFname())) {
			sqlSb.append(" AND FP.FNAME LIKE '%").append(vo.getFname()).append("%' ");
		}
		
		if(StringUtils.isNotBlank(vo.getRecordname())) {
			sqlSb.append(" AND FR.RECORDNAME LIKE '%").append(vo.getRecordname()).append("%'");
		}
		
		if(vo.getIsresult() != null) {
			sqlSb.append(" AND FR.ISRESULT = ").append(vo.getIsresult());
		}
		
		if(vo.getRecordtype() != null) {
			sqlSb.append(" AND FR.RECORDTYPE = ").append(vo.getRecordtype());
		}
		
		if(vo.getMinenddate() != null) {
			sqlSb.append(" AND FR.ENDDATE >= ").append(DataBaseSqlConfig.getDateStrFormat(vo.getMinenddate()));
		}
		
		if(vo.getMaxenddate() != null) {
			sqlSb.append(" AND FR.ENDDATE <= ").append(DataBaseSqlConfig.getDateStrFormat(vo.getMaxenddate()));
		}
		
		if(vo.getMinstartdate() != null) {
			sqlSb.append(" AND FR.STARTDATE >= ").append(DataBaseSqlConfig.getDateStrFormat(vo.getMinstartdate()));
		}
		
		if(vo.getMaxstartdate() != null) {
			sqlSb.append(" AND FR.STARTDATE <= ").append(DataBaseSqlConfig.getDateStrFormat(vo.getMaxstartdate()));
		}
		
		sqlSb.append(" ORDER BY FR.CREATETIME DESC ");
		String sql = sqlSb.toString();
		return sql;
	}
}
