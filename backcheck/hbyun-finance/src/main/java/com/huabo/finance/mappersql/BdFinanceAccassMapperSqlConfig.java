package com.huabo.finance.mappersql;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.util.UriUtils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hbfk.entity.FaAccbookinfoUtil;
import com.hbfk.util.database.DataBaseSqlConfig;
import com.huabo.finance.entity.caiji.BdFinanceAccass;
import com.huabo.finance.vo.BdFinanceAccassVo;
import com.huabo.finance.vo.ExportRequestVo;

public class BdFinanceAccassMapperSqlConfig {
	
	public String exportAccAssList(ExportRequestVo exportRequestVo) throws Exception{
		StringBuffer sqlSb = new StringBuffer("SELECT * FROM BD_FINANCE_ACCASS WHERE 1 = 1 ");
		if(exportRequestVo.getPkAccasss() != null){
				sqlSb.append(" AND PK_ACCASS IN (");
				for (String pka : exportRequestVo.getPkAccasss()) {
					sqlSb.append("'").append(UriUtils.decode(pka, "utf-8")).append("',");
				}
				sqlSb.deleteCharAt(sqlSb.length()-1).append(")");
		}
		String sql = sqlSb.toString();
		return sql;
	}
	
	
	public String selectPageList(Page<BdFinanceAccass> page, BdFinanceAccassVo vo, FaAccbookinfoUtil bookInfo) throws Exception{
		StringBuffer sqlSb = new StringBuffer("SELECT * FROM BD_FINANCE_ACCASS WHERE FPLANID = '").append(bookInfo.getPkFinanplanid()).append("' AND ")
				.append(DataBaseSqlConfig.getWhereColumnInStr("PK_ACCASSITEM", UriUtils.decode(vo.getPkAccassitem(), "utf-8"), ",")).append(" AND DATAORIGINFLAG = -2 ");
		
		if(StringUtils.isNotBlank(vo.getAssname())) {
			sqlSb.append(" AND ASSNAME LIKE '%").append(vo.getAssname()).append("%' ");
		}
		
		if(StringUtils.isNotBlank(vo.getAssdd())) {
			sqlSb.append(" AND ASSDD LIKE '%").append(vo.getAssdd()).append("%' ");
		}
		
		if(StringUtils.isNotBlank(vo.getAsstype())) {
			sqlSb.append(" AND ASSTYPE LIKE '%").append(vo.getAsstype()).append("%' ");
		}
		
		sqlSb.append(" ORDER BY PK_ACCASS ASC");
		String sql = sqlSb.toString();
		System.out.println(sql);
		return sql;
		
	}
	
}
