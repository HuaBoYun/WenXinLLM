package com.huabo.finance.unit;

import com.hbfk.util.DateUtil;
import com.huabo.finance.config.SystemStaticConfig;
import com.huabo.finance.entity.TblConfigColumnInfo;

public class BussinessDataSqlFunc {
	
	public static String formatQueryDate(TblConfigColumnInfo col) throws Exception {
		if(col.getQueryData() == null){
			return "''";
		}
		String dataSql;
		if("DM".equals(SystemStaticConfig.dbtype)) {
			switch (col.getColType()) {
				case "NUMBER":
				case "DECIMAL":
				case "FLOAT":
					dataSql = col.getQueryData().toString();
					break;
				case "DATE":
					dataSql = "TO_DATE('"+DateUtil.formatDate(col.getQueryData().toString(), DateUtil.DATE_SMALL_STR)+"','YYYY-MM-DD')";
					break;
				case "TIME":
				case "TIMESTAMP":
					dataSql = "TO_DATE('"+DateUtil.formatDate(col.getQueryData().toString(), DateUtil.DATE_FULL_STR)+"','YYYY-MM-DD HH24:MI:SS')";
					break;
				default:
					dataSql = "'"+col.getQueryData()+"'";
					break;
			}
		}else if("Oracle".equals(SystemStaticConfig.dbtype)) {
			switch (col.getColType()) {
				case "NUMBER":
				case "DECIMAL":
				case "FLOAT":
					dataSql = col.getQueryData().toString();
					break;
				case "DATE":
					dataSql = "TO_DATE('"+DateUtil.formatDate(col.getQueryData().toString(), DateUtil.DATE_SMALL_STR)+"','YYYY-MM-DD')";
					break;
				case "TIME":
				case "TIMESTAMP":
					dataSql = "TO_DATE('"+DateUtil.formatDate(col.getQueryData().toString(), DateUtil.DATE_FULL_STR)+"','YYYY-MM-DD HH24:MI:SS')";
					break;
				default:
					dataSql = " '"+col.getQueryData()+"'";
					break;
			}
		}else {
			switch (col.getColType()) {
				case "INT":
				case "BIGINT":
				case "DECIMAL":
				case "FLOAT":
					dataSql = col.getQueryData().toString();
					break;
				case "DATE":
					dataSql = "'"+DateUtil.formatDate(col.getQueryData().toString(), DateUtil.DATE_SMALL_STR)+"'";
					break;
				case "TIME":
				case "TIMESTAMP":
					dataSql = "'"+DateUtil.formatDate(col.getQueryData().toString(), DateUtil.DATE_FULL_STR)+"'";
					break;
				default:
					dataSql = "'"+col.getQueryData()+"'";
					break;
			}
		}
		return dataSql;
	}
}
