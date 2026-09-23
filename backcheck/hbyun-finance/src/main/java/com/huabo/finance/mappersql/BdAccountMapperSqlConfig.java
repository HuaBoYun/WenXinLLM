package com.huabo.finance.mappersql;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.util.UriUtils;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hbfk.entity.FaAccbookinfoUtil;
import com.huabo.finance.vo.BdAccountVo;
import com.huabo.finance.vo.BdFinanceplanVo;
import com.huabo.finance.vo.ExportRequestVo;
import com.huabo.finance.vr.BdFinanceplanVr;

public class BdAccountMapperSqlConfig {
	
	public String selectListByExport(ExportRequestVo exportRequestVo) throws Exception{
		StringBuffer sqlSb = new StringBuffer("SELECT DISTINCT BAC.CODE,BAC.NAME,BAC.BALANORIENT,PAC.CODE AS PARCODE,PAC.NAME AS PARNAME,BAC.SUMPRINT_LEVEL,BAC.ENDFLAG FROM BD_ACCOUNT BAC LEFT JOIN BD_ACCOUNT PAC ON BAC.PID = PAC.PK_ACCOUNT WHERE 1 = 1");
		
		if(StringUtils.isNotBlank(exportRequestVo.getPkOrg())) {
			sqlSb.append(" AND BAC.PK_ORG = '").append(exportRequestVo.getPkOrg()).append("'");
		}
		
		if(exportRequestVo.getPkAccounts() != null) {
			sqlSb.append(" AND BAC.PK_ACCOUNT IN (");
			for (String pka : exportRequestVo.getPkAccounts()) {
				sqlSb.append("'").append(UriUtils.decode(pka, "utf-8")).append("',");
			}
			sqlSb.deleteCharAt(sqlSb.length()-1).append(")");
		}
		sqlSb.append(" ORDER BY BAC.CODE ASC ");
		
		String sql = sqlSb.toString();
		return sql;
	}
	
	public String selectFatherPkAccount(String pid, FaAccbookinfoUtil bookInfo) throws Exception{
		StringBuffer sqlSb = new StringBuffer("SELECT PK_ACCOUNT FROM BD_ACCOUNT ");
		
		StringBuffer whereSql = new StringBuffer(" WHERE CODE = '").append(pid).append("'");
		
		if(StringUtils.isNotBlank(bookInfo.getPkFinanplanid())) {
			whereSql.append(" AND FPLANID = ").append(bookInfo.getPkFinanplanid()).append(" AND DATAORIGINFLAG = -2");
		}
		
		String sql = sqlSb.toString()+whereSql.toString();
		return sql;
	}
	
	
	public String selectAllList(BdAccountVo vo, FaAccbookinfoUtil bookInfo)throws Exception{
		StringBuffer sqlSb = new StringBuffer("SELECT BAC.*,BAT.NAME AS ACCTYPENAME FROM BD_ACCOUNT BAC " + 
				"LEFT JOIN BD_ACCTYPE BAT ON BAC.PK_ACCTYPE = BAT.PK_ACCTYPE ");
		
		StringBuffer whereSql = new StringBuffer(" WHERE 1 = 1");
	
		
		if(StringUtils.isNotBlank(bookInfo.getPkFinanplanid())) {
			sqlSb.append(" AND BAT.FPLANID = ").append(bookInfo.getPkFinanplanid()).append(" AND BAT.DATAORIGINFLAG = -2");
			whereSql.append(" AND BAC.FPLANID = ").append(bookInfo.getPkFinanplanid()).append(" AND BAC.DATAORIGINFLAG = -2");
		}
		
		if(StringUtils.isNotBlank(vo.getPkorg())) {
			whereSql.append(" AND BAC.PK_ORG = '").append(vo.getPkorg()).append("'");
		}
		
		if(StringUtils.isNotBlank(vo.getCode())) {
			whereSql.append(" AND BAC.CODE LIKE '%").append(vo.getCode()).append("%'");
		}
		
		if(StringUtils.isNotBlank(vo.getName())) {
			whereSql.append(" AND BAC.NAME LIKE '%").append(vo.getName()).append("%'");
		}
		
		if(vo.getBalanorient() != null) {
			whereSql.append(" AND BAC.BALANORIENT = ").append(vo.getBalanorient());
		}
		
		if(StringUtils.isNotBlank(vo.getInnercode())) {
			whereSql.append(" AND BAC.INNERCODE LIKE '%").append(vo.getInnercode()).append("%'");
		}
		
//		if(StringUtils.isNotBlank(vo.getFpkaccount())) {
//			whereSql.append(" AND (BAC.PID = '").append(vo.getFpkaccount()).append("' OR BAC.PK_ACCOUNT = '").append(vo.getFpkaccount()).append("')");
//		}
		
		if(StringUtils.isNotBlank(vo.getPid())) {
			whereSql.append(" AND BAC.CODE LIKE '").append(vo.getPid()).append("%'");
		}
		
		
		if(vo.getMinaccLev() != null) {
			whereSql.append(" AND BAC.ACCLEV >= ").append(vo.getMinaccLev());
		}
		
		if(vo.getMaxaccLev() != null) {
			whereSql.append(" AND BAC.ACCLEV <= ").append(vo.getMaxaccLev());
		}
		
		whereSql.append(" ORDER BY BAC.CODE ASC ");
		
		String sql = sqlSb.toString()+whereSql.toString();
		System.out.println(sql);
		return sql;
	}
}
