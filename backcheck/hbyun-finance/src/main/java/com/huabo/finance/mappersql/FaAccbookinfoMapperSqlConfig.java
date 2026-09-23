package com.huabo.finance.mappersql;

import java.math.BigDecimal;

import org.apache.commons.lang.StringUtils;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huabo.finance.vo.FaAccbookinfoVo;
import com.huabo.finance.vr.FaAccbookinfoVr;

public class FaAccbookinfoMapperSqlConfig {
	
	
	public String selectPageInfoByUserRole(Page<FaAccbookinfoVr> page, FaAccbookinfoVo vo, String roleIdStrs, BigDecimal staffId) throws Exception{
		StringBuffer sqlSb = new StringBuffer("SELECT ABI.*,OSB.NAME AS SETOFNAME,BFP.FNAME AS PLANNAME,(SELECT COUNT(0) FROM FA_ACCBOOK_USER WHERE ACCBOOKID = ABI.PK_ACCBOOKINFO AND STAFFID = "+staffId+") AS ISCHECKED FROM FA_ACCBOOKINFO ABI LEFT JOIN ORG_SETOFBOOK OSB ON ABI.PK_SETOFBOOK = OSB.PK_SETOFBOOK LEFT JOIN BD_FINANCEPLAN BFP ON ABI.PK_FINANPLANID = BFP.FID");
		
		sqlSb.append("  WHERE ABI.PK_ACCBOOKINFO IN (SELECT ACCBOOKID FROM FA_ACCBOOK_ROLE WHERE ROLEID IN (").append(roleIdStrs).append("))");
		
		if(vo.getConvertDate() != null) {
			sqlSb.append(" AND ABI.CONVERT_DATE = ").append(vo.getConvertDate());
		}
		
		if(StringUtils.isNotBlank(vo.getBookName())) {
			sqlSb.append(" AND ABI.BOOKNAME LIKE '%").append(vo.getBookName()).append("%' ");
		}
		
		if(StringUtils.isNotBlank(vo.getAccbooktypecode())) {
			sqlSb.append(" AND ABI.ACCBOOKTYPECODE LIKE '%").append(vo.getAccbooktypecode()).append("%'");
		}
		
		if(StringUtils.isNotBlank(vo.getAccbooktypename())) {
			sqlSb.append(" AND ABI.ACCBOOKTYPENAME LIKE '%").append(vo.getAccbooktypename()).append("%'");
		}
		
		if(StringUtils.isNotBlank(vo.getLocaloriginvalue())) {
			sqlSb.append(" AND ABI.LOCALORIGINVALUE LIKE '%").append(vo.getLocaloriginvalue()).append("%'");
		}
		
		sqlSb.append(" ORDER BY ABI.CREATIONTIME DESC ");
		String sql = sqlSb.toString();
		return sql;
	}
	

	public String selectPageInfo(Page<FaAccbookinfoVr> page, FaAccbookinfoVo vo) throws Exception{
		StringBuffer sqlSb = new StringBuffer("SELECT ABI.*,OSB.NAME AS SETOFNAME,BFP.FNAME AS PLANNAME FROM FA_ACCBOOKINFO ABI LEFT JOIN ORG_SETOFBOOK OSB ON ABI.PK_SETOFBOOK = OSB.PK_SETOFBOOK LEFT JOIN BD_FINANCEPLAN BFP ON ABI.PK_FINANPLANID = BFP.FID");
		
		sqlSb.append(" WHERE 1 = 1 ");
		
		if(vo.getConvertDate() != null) {
			sqlSb.append(" AND ABI.CONVERT_DATE = ").append(vo.getConvertDate());
		}
		
		if(StringUtils.isNotBlank(vo.getBookName())) {
			sqlSb.append(" AND ABI.BOOKNAME LIKE '%").append(vo.getBookName()).append("%' ");
		}
		
		if(StringUtils.isNotBlank(vo.getAccbooktypecode())) {
			sqlSb.append(" ABI ABI.ACCBOOKTYPECODE LIKE '%").append(vo.getAccbooktypecode()).append("%'");
		}
		
		if(StringUtils.isNotBlank(vo.getAccbooktypename())) {
			sqlSb.append(" ABI ABI.ACCBOOKTYPENAME LIKE '%").append(vo.getAccbooktypename()).append("%'");
		}
		
		if(StringUtils.isNotBlank(vo.getLocaloriginvalue())) {
			sqlSb.append(" ABI ABI.LOCALORIGINVALUE LIKE '%").append(vo.getLocaloriginvalue()).append("%'");
		}
		
		sqlSb.append(" ORDER BY ABI.CREATIONTIME DESC ");
		String sql = sqlSb.toString();
		return sql;
	}
}
