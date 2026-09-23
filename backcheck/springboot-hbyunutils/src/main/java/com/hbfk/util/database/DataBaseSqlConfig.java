package com.hbfk.util.database;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.hbfk.config.SystemStaticValue;
import com.hbfk.util.DateUtil;

public class DataBaseSqlConfig {
	
	public static final String TIMETYPEMONTH = "M";
	public static final String TIMETYPEYEAR = "Y";
	public static final String TIMETYPEDAY = "D";

	/**
	 * 根据数据库类型获取装换后的数据类型参数
	 * @param date -Date类型日期参数
	 * @return 默认返回当前时间
	 */
	public static String getDateStrFormat(Date date) throws Exception {
		if(date == null){
			date = new Date();
		}
		String dataStr = null;
		if(SystemStaticValue.DATABASETYPE.equals("DM") || SystemStaticValue.DATABASETYPE.equals("Oracle")) {
			dataStr = "TO_DATE('"+DateUtil.parseDate(date, DateUtil.DATE_SMALL_STR)+"','YYYY-MM-DD')";
		}else {
			dataStr = "'"+DateUtil.parseDate(date, DateUtil.DATE_SMALL_STR)+"'";
		}
		return dataStr;
	}
	
	public static String getDateHmsStrFormat(Date date) throws Exception {
		if(date == null){
			date = new Date();
		}
		String dataStr = null;
		if(SystemStaticValue.DATABASETYPE.equals("DM") || SystemStaticValue.DATABASETYPE.equals("Oracle")) {
			dataStr = "TO_DATE('"+DateUtil.parseDate(date, DateUtil.DATE_FULL_STR)+"','YYYY-MM-DD HH24:MI:SS')";
		}else {
			dataStr = "'"+DateUtil.parseDate(date, DateUtil.DATE_FULL_STR)+"'";
		}
		return dataStr;
	}
	
	
	/**
	 * 根据数据库类型获取装换后的数据类型参数
	 * @param date -Date类型字符串参数
	 * @return 默认返回当前时间
	 */
	public static String getDateStrFormat(String date) throws Exception {
		if(StringUtils.isBlank(date)){
			date = DateUtil.parseDate(new Date(), DateUtil.DATE_SMALL_STR);
		}
		String dataStr = null;
		if(SystemStaticValue.DATABASETYPE.equals("DM") || SystemStaticValue.DATABASETYPE.equals("Oracle")) {
			dataStr = "TO_DATE('"+date+"','YYYY-MM-DD')";
		}else {
			dataStr = "'"+date+"'";
		}
		return dataStr;
	}
	
	
	/**
	 * 根据数据库类型获取装换后的数据类型参数 
	 * @param date -Date类型字符串参数
	 * @param dbtype -数据库类型
	 * @return 返回年月日时分秒
	 */
	public static String getDateHmsStrFormat(String date,String dbtype) throws Exception {
		if(StringUtils.isBlank(date)){
			date = DateUtil.parseDate(new Date(), DateUtil.DATE_SMALL_STR);
		}
		String dataStr = null;
		if(dbtype.equals("DM") || dbtype.equals("Oracle")) {
			dataStr = "TO_DATE('"+date+"','YYYY-MM-DD HH24:MI:SS')";
		}else {
			dataStr = "'"+date+"'";
		}
		return dataStr;
	}
	
	/**
	 * 根据数据库类型 返回转换列的 年、月、日
	 * @param column --列名
	 * @param type 转换类型分批传入
	 * 				YYYY-年，
	 * 				MM-月、
	 * 				DD - 日
	 * @return
	 * @throws Exception
	 */
	public static String getDateColumn(String column,String type) throws Exception{
		String sql = null;
		if(SystemStaticValue.DATABASETYPE.equals("DM") || SystemStaticValue.DATABASETYPE.equals("Oracle")) {
			sql = "TO_NUMBER(TO_CHAR("+column+", '"+type+"'))";
		}else {
			switch (type) {
			case "YYYY":
				sql = "YEAR("+column+")";
				break;
			case "DD":
				sql = "DAY("+column+")";
				break;
			default:
				sql = "MONTH("+column+")";
				break;
			}
		}
		return sql;
	}
	
	public static String getYearAllStrColumn(String column,String type) throws Exception{
		String sql = null;
		if(SystemStaticValue.DATABASETYPE.equals("DM") || SystemStaticValue.DATABASETYPE.equals("Oracle")) {
			sql = "TO_CHAR("+column+", '"+type+"')";
		}else {
			sql = "DATE_FORMAT("+column+", '%Y-%m-%d')";
		}
		return sql;
	}
	
	/**
	 * 根据数据类型 返回 系统最大的编号 ，示例 XX-XX-01 
	 * @param column -- 编号列名
	 * @param split  -- 分割符
	 * @param no     -- 编号规则示例 用来获取长度进行获取 最后几位的编号
	 * @return
	 * @throws Exception
	 */
	public static String getMaxNoDeal(String column,String split,String no) throws Exception{
		String sql = null;
		if(SystemStaticValue.DATABASETYPE.equals("DM") || SystemStaticValue.DATABASETYPE.equals("Oracle")) {
			sql = "MAX(TO_NUMBER(SUBSTR("+column+",INSTR("+column+",'"+split+"',-1)+1,LENGTH("+column+")-INSTR("+column+",'"+split+"',-1))))";
		}else {
			sql = "MAX(CONVERT(SUBSTRING("+column+", LOCATE('"+split+"' , "+column+" , LENGTH('"+no+"'))+1), UNSIGNED))";
		}
		return sql;
	}
	
	/**
	 * 非空判断返回
	 * @param column --列名
	 * @param value  --为空时的取值 字符串加''
	 * @return
	 * @throws Exception
	 */
	public static String getNullColumn(String column,String value) throws Exception {
		String sql = null;
		if(SystemStaticValue.DATABASETYPE.equals("DM") || SystemStaticValue.DATABASETYPE.equals("Oracle")) {
			sql = "NVL("+column+","+value+")";
		}else {
			sql = "IFNULL("+column+","+value+")";
		}
		return sql;
	}
	
	/**
	 * where条件 筛选 某一列中是否包含 传入的字符串
	 * @param column		--列名
	 * @param valueStr		--筛选参数
	 * @param splitCode		--拼接字符 ,or.or~or-
	 * @return
	 * @throws Exception
	 */
	public static String getWhereColumnInStr(String column,String valueStr,String splitCode)throws Exception{
		String sql = null;
		if(SystemStaticValue.DATABASETYPE.equals("DM")) {
			sql = "POSITION('"+splitCode+valueStr+splitCode+"' IN '"+splitCode+"'||"+column+"||'"+splitCode+"') > 0";
		}else if(SystemStaticValue.DATABASETYPE.equals("Oracle")){
			sql = "INSTR('"+splitCode+"'||"+column+"||'"+splitCode+"', '"+splitCode+valueStr+splitCode+"') > 0";
		}else {
			sql = "FIND_IN_SET(CONCAT('"+splitCode+"',"+column+",'"+splitCode+"'),'"+splitCode+valueStr+splitCode+"')";
		}
		return sql;
	}
	
	/**
	 * where条件 筛选 某一列中是否包含 传入的列值
	 * @param column		--列名
	 * @param choiceCol		--筛选条件列
	 * @param splitCode		--拼接字符 ,or.or~or-
	 * @return
	 * @throws Exception
	 */
	public static String getWhereColumnInCol(String column,String choiceCol,String splitCode)throws Exception{
		String sql = null;
		if(SystemStaticValue.DATABASETYPE.equals("DM") || SystemStaticValue.DATABASETYPE.equals("Oracle")) {
			sql = "INSTR('"+splitCode+"'||"+column+"||'"+splitCode+"', '"+splitCode+"'||"+choiceCol+"||'"+splitCode+"') > 0";
		}else {
			sql = "FIND_IN_SET(CONCAT('"+splitCode+"',"+column+",'"+splitCode+"'),CONCAT('"+splitCode+"',"+choiceCol+",'"+splitCode+"'))";
		}
		return sql;
	}
	
	/**
	 * 获取 行数限制语句
	 * @param startIndex -- 起始位置  非空
	 * @param num		 -- 行数数量 非空
	 * @return
	 * @throws Exception
	 */
	public static String getRowLimitSql(Integer startIndex,Integer num) throws Exception{
		String sql = null;
		if(SystemStaticValue.DATABASETYPE.equals("DM") || SystemStaticValue.DATABASETYPE.equals("Oracle")) {
			sql = " ROWNUM <= "+(startIndex+num) + " AND ROWNUM > " + startIndex;
		}else {
			sql = " LIMIT "+startIndex+","+num+" ";
		}
		return sql ;
	}

	public static String getRowLimitSql(Integer startIndex,Integer num,String databasetype) throws Exception{
		String sql = null;
		if("DM".equals(databasetype) || "Oracle".equals(databasetype) ) {
			sql = " ROWNUM <= "+(startIndex+num) + " AND ROWNUM > " + startIndex;
		}else {
			sql = " LIMIT "+startIndex+","+num+" ";
		}
		return sql ;
	}
	
	/**
	 * 连接列或字符串
	 * @param col1 拼接的第一位  列 或者加单引号的字符串
	 * @param str1 拼接的第二位  列 或者加单引号的字符串
	 * @return sql连接字符完成语句
	 * @throws Exception
	 */
	public static String getConcatColumn(String col1,String str1) throws Exception{
		String sql = null;
		if(SystemStaticValue.DATABASETYPE.equals("DM") || SystemStaticValue.DATABASETYPE.equals("Oracle")) {
			sql = col1+"||"+str1;
		}else {
			sql = "CONCAT("+col1+","+str1+")";
		}
		return sql ;
	}
	
	/**
	 * 设置表别名
	 * @param tableName --表别名
	 * @return
	 * @throws Exception
	 */
	public static String setTableAliasName(String tableName) throws Exception {
		String sql = null;
		if(SystemStaticValue.DATABASETYPE.equals("DM") || SystemStaticValue.DATABASETYPE.equals("Oracle")) {
			sql = tableName;
		}else {
			sql = " AS "+tableName;
		}
		return sql ;
	}
	
	/**
	 * 
	 * @param type   --比较年月日类型，Y-年，M-月，D-日
	 * @param columnName --列
	 * @return
	 * @throws Exception
	 */
	public static String getWhereNowTimeCompareToTime(String type,String columnName) throws Exception{
		String sql = "";
		switch (type) {
		case "M":
			if(SystemStaticValue.DATABASETYPE.equals("DM") || SystemStaticValue.DATABASETYPE.equals("Oracle")) {
				sql = "months_between(sysdate,"+columnName+")";
			}else {
				sql = "TIMESTAMPDIFF(MONTH, "+columnName+", now())";
			}
			break;

		default:
			break;
		}
		return sql;
	}

	public static String toCharColumn(String column) throws Exception {
		String sql = null;
		if(SystemStaticValue.DATABASETYPE.equals("DM") || SystemStaticValue.DATABASETYPE.equals("Oracle")) {
			sql = "TO_CHAR("+column+")";
		}else {
			sql = "CONVERT("+column+", VARCHAR(255))";
		}
		return sql ;
	}
	
	
	public static String toNumberColumn(String column) throws Exception {
		String sql = null;
		if(SystemStaticValue.DATABASETYPE.equals("DM") || SystemStaticValue.DATABASETYPE.equals("Oracle")) {
			sql = "TO_NUMBER("+column+")";
		}else {
			sql = "CAST("+column+" AS INTEGER)";
		}
		return sql ;
	}
	
	/**
	 * 生成查询组织架构用的in \not in 的sql 拼接语句
	 * @param column		列名 orgid
	 * @param operator		运算符  in \ not in
	 * @param andor			and  or
	 * @param orgIdList		集合，orgid集合
	 * @return
	 * @throws Exception
	 */
	public static String getInOrNotSql(String column,String operator,String andor,List<String> orgIdList) throws Exception {
		List<List<String>> orgChoiceList = splitList(orgIdList);
		StringBuffer sqlSb = new StringBuffer("( ");
		for (List<String> list :orgChoiceList) {
			sqlSb.append(" "+column+" "+operator+" (" + String.join(",", list)+") "+andor);
		}
		String sql = sqlSb.substring(0, sqlSb.length()-andor.length())+" )";
		return sql;
	}
	
	/**
	 * 将string list 已每900个 进行分割，生成用于 in / not in sql 中
	 * @param list
	 * @return
	 */
	public static List<List<String>> splitList(List<String> list) {
        List<List<String>> splittedLists = new ArrayList<List<String>>();
        final int SIZE_PER_SUB_LIST = 900;
 
        for (int i = 0; i < list.size(); i += SIZE_PER_SUB_LIST) {
            splittedLists.add(list.subList(i, Math.min(i + SIZE_PER_SUB_LIST, list.size())));
        }
 
        return splittedLists;
    }
	
}
