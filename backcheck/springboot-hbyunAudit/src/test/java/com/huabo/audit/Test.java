package com.huabo.audit;

import com.hbfk.util.JsonBean;
import com.huabo.audit.util.OracleSqlProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@SpringBootTest
public class Test {

	@Resource
	private OracleSqlProperties oracleSqlProperties;

	@org.junit.jupiter.api.Test
	public void test() throws Exception {
		List<String> objects = new ArrayList<>();
		objects.add("insert into TBL_AUDIT_MODELEXT(ID,b,a) values (108 ,2,5)");
		objects.add("insert into TBL_AUDIT_MODELEXT(ID,b,a) values (109 ,3,5)");
		objects.add("insert into TBL_AUDIT_MODELEXT(ID,b,a) values (110 ,4,5)");
		for (String object : objects) {
			oracleSqlProperties.insertSql("AN", "qwe123", object);
		}
	}

	@org.junit.jupiter.api.Test
	public void test3() throws Exception {
		List<String> tableValueLists = new ArrayList<>();
		//		tableValueLists.add("5");
		tableValueLists.add("1.22");
		tableValueLists.add("2023-05-14");
		tableValueLists.add("1");
		tableValueLists
				.add("大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大大");
		tableValueLists.add("小小小小小小小小小小小小小小小小小小小小小小小小小小小小小小小小小小小小小小小小小小小小小小小小小小小小小小小小小小小小小小小小小小小小小小小小小小小小小小");

		HashMap<String, String> columnMap = new HashMap<>();
		//		columnMap.put("ID", "NUMBER");
		columnMap.put("NUM", "NUMBER(10, 2)");
		columnMap.put("TIME", "DATE");
		columnMap.put("NUM1", "NUMBER");
		columnMap.put("COM", "CLOB");
		columnMap.put("COM1", "VARCHAR2(500)");

		List<String> tableLists = new ArrayList<>();
		//		tableLists.add("ID");
		tableLists.add("NUM");
		tableLists.add("TIME");
		tableLists.add("NUM1");
		tableLists.add("COM");
		tableLists.add("COM1");
		//循环只勾选的字段进行数据库表新增 值
		int j = 0;
		List<String> values = new ArrayList<>();
		for (String str : tableValueLists) {
			//类型替换
			String columnValues = doColumnReplace(tableLists.get(j), str, columnMap);
			values.add(columnValues);
			j++;
		}
		log.info("values:{}", values.toString());
		/**
		 * insert into 表名（列名1,列名2,列名3.....）values(值1,值2,值3.....);
		 */
		JsonBean oracleSqlProperties = this.oracleSqlProperties.execSql("select HIBERNATE_SEQUENCE.nextval from dual");
		List<Map<String, Integer>> data = (List<Map<String, Integer>>) oracleSqlProperties.getData();
		StringBuffer insert = new StringBuffer();
		insert.append("insert into " + "TEST" + "(ID," + StringUtils.join(tableLists, ",") + ")");
		insert.append(" values");
		insert.append(" (" + data.get(0).get("NEXTVAL") + " ," + StringUtils.join(values, ",") + ")");
		this.oracleSqlProperties.insertSql("HBGRCTEST", "HBGRCTEST", insert.toString());
	}

	/**
	 * 字段类型替换
	 * @param str
	 * @param columnMap
	 */
	private String doColumnReplace(String column, String str, Map<String, String> columnMap) throws Exception {
		String varchar2 = "VARCHAR2(500)";
		String number = "NUMBER";
		String date = "DATE";
		String numberMin = "NUMBER(10,2)";
		String clob = "CLOB";
		if (columnMap.containsKey(column)) {
			String columnType = columnMap.get(column);
			//date类型特殊处理
			if (Objects.equals(date, columnType)) {
				str = doDateVail(str);
			}
			if (Objects.equals(varchar2, columnType) || Objects.equals(clob, columnType)) {
				str = "'" + str + "'";
			}
		}
		return str;
	}


	/**
	 * 时间格式处理
	 * @param date
	 * @return
	 */
	private String doDateVail(String date) throws ParseException {
		//用于指定 日期/时间 模式
		/**
		 * TO_DATE('2023-05-14 13:43:52', 'YYYY-MM-DD HH24:MI:SS')
		 */
		try {
			Date date1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date);
			String date2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date1);
			date = "TO_DATE('" + date2 + "','YYYY-MM-DD HH24:MI:SS')";
		} catch (Exception e) {
			// yyyy-MM-dd 转换成 yyyy-MM-dd HH:mm:ss
			Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(date);
			String date2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date1);
			date = "TO_DATE('" + date2 + "','YYYY-MM-DD HH24:MI:SS')";
		}
		return date;
	}


}
