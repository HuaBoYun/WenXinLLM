package com.huabo.finance.vr;

import java.sql.Types;
import java.util.Date;

import com.hbfk.util.DateUtil;
import com.hbfk.util.database.DataBaseSqlConfig;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Schema(name="ColumnType对象", description="数据库列名对象")
public class ColumnType {
	
	@Schema(name = "列名")
	private String name;
	
	@Schema(name = "列类型")
	private int ctype;

	@Schema(name = "值")
	private Object cdata;

	public static String getInsertDataStr(ColumnType cti) throws Exception{
		String istr = "''";
		
		if(cti.getCdata() == null) {
			return istr;
		}
		Date date = null;
		if (cti.getCtype() == Types.TIMESTAMP) {
			date = DateUtil.formatDate(cti.getCdata().toString(), DateUtil.DATE_FULL_STR);
			istr = DataBaseSqlConfig.getDateHmsStrFormat(date);
		} else if (cti.getCtype() == Types.TIME) {
			date = DateUtil.formatDate(cti.getCdata().toString(), DateUtil.DATE_SMALL_STR);
			istr = DataBaseSqlConfig.getDateHmsStrFormat(date);
		} else if (cti.getCtype() == Types.DATE) {
			date = DateUtil.formatDate(cti.getCdata().toString(), DateUtil.DATE_SMALL_STR);
			istr = DataBaseSqlConfig.getDateHmsStrFormat(date);
		}else {
			istr = "'"+cti.getCdata()+"'";
		}
		return istr;
	}
}
