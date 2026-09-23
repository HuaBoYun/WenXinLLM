package com.huabo.system.mappersql;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hbfk.util.PageInfo;
import com.huabo.system.entity.TblAcquisitionRecord;
import com.huabo.system.entity.TblImplog;

import java.math.BigDecimal;

import org.apache.ibatis.annotations.Param;

public class TblImplogMapperSqlConfig {

	public String findByTblImplogList(IPage<TblImplog> page, String username, String type) {
		StringBuffer sqlSb = new StringBuffer("SELECT * FROM TBL_IMP_LOG  WHERE IMPTYPE = '"+type+ "' and IMPCREATEUSERNAME = '"+username+"' ");
		sqlSb.append(" order by IMPCREATETIME desc ");
		String sql = sqlSb.toString();
		return sql;
	}
}
