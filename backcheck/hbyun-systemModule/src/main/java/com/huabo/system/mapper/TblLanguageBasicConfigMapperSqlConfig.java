package com.huabo.system.mapper;

import org.apache.commons.lang.StringUtils;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huabo.system.entity.TblLanguageBasicConfig;

public class TblLanguageBasicConfigMapperSqlConfig {
 
	public String selectPageList(Page<TblLanguageBasicConfig> page, TblLanguageBasicConfig config) throws Exception{
		StringBuffer sqlSb = new StringBuffer("SELECT TLBC.*,TLT.TRANTEXT,TLT.INFOID FROM TBL_LANGUAGE_BASIC_CONFIG TLBC LEFT JOIN TBL_LANGUAGE_TRANSLATE TLT ON TLBC.CONFIGID = TLT.CONFIGID AND TLT.INFOID = '")
				.append(config.getInfoid()).append("' WHERE 1 = 1 ");
		
		if(StringUtils.isNotBlank(config.getMenuname())) {
			sqlSb.append(" AND TLBC.MENUNAME LIKE '%").append(config.getMenuname()).append("%'");
		}
		
		if(StringUtils.isNotBlank(config.getTrantext())) {
			sqlSb.append(" AND TLT.TRANTEXT LIKE '%").append(config.getTrantext()).append("%'");
		}
		
		sqlSb.append(" ORDER BY TLBC.CREATETIME ASC ");
		String sql = sqlSb.toString();
		return sql;
	}

	
	public String selectConfigList(String infoid) throws Exception{
		StringBuffer sqlSb = new StringBuffer("SELECT TLBC.*,TLT.TRANTEXT,TLT.INFOID FROM TBL_LANGUAGE_TRANSLATE TLT LEFT JOIN TBL_LANGUAGE_BASIC_CONFIG TLBC ON TLBC.CONFIGID = TLT.CONFIGID")
				.append(" WHERE TLT.INFOID = '").append(infoid).append("'");
		
		sqlSb.append(" ORDER BY TLBC.CREATETIME ASC ");
		String sql = sqlSb.toString();
		return sql;
	}
}