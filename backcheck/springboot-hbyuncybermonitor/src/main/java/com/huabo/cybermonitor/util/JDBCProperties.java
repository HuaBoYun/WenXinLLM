package com.huabo.cybermonitor.util;

import com.hbfk.util.PageInfo;
import com.hbfk.util.*;
import com.huabo.cybermonitor.entity.TblAuditModelDataSourceOracle;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.*;

@Slf4j
public class JDBCProperties {
	
	public static Properties properties = JedisUtil.loadPropertyFile("application.yml");

	public static JsonBean GetGather(TblAuditModelDataSourceOracle dataSource, String sql, PageInfo<Map<String, Object>> pageInfo) throws Exception {
		Connection connGRC = DriverManager.getConnection(dataSource.getDataBaseConnectionAddress(), dataSource.getDataBaseUsers(), dataSource.getDataBasePassWord());
		Statement stmtGRC = connGRC.createStatement();
		stmtGRC.setQueryTimeout(0);

		String countSql = "SELECT count(*) as count FROM (" + sql + ")";
		ResultSet countResultSet = stmtGRC.executeQuery(countSql);
		int count = 0;
		if (countResultSet.next()) {
			count = countResultSet.getInt(1);
		}
		log.info("记录总数: " + count);


		StringBuffer sb = new StringBuffer("SELECT * FROM (SELECT T1.*,ROWNUM RN  FROM (" + sql + ") T1 WHERE ROWNUM <= " + (pageInfo.getCurrentRecord() + pageInfo.getPageSize()) + " ) T2 WHERE T2.RN > " + pageInfo.getCurrentRecord());
		ResultSet rsOracle = stmtGRC.executeQuery(sb.toString());
		ResultSetMetaData meta = rsOracle.getMetaData();
		int columncount = meta.getColumnCount();
		log.info("=====" + columncount);
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		while (rsOracle.next()) {
			Map<String, Object> map = new HashMap<String, Object>();
			for (int i = 1; i <= columncount; i++) {
				map.put(meta.getColumnName(i), rsOracle.getObject(i));
			}
			if (!map.isEmpty()) {
				list.add(map);
			}
		}

		rsOracle.close();
		stmtGRC.close();
		connGRC.close();

		Map<String, Object> resultMap = new HashMap<String, Object>(0);
		pageInfo.setTlist(list);
		pageInfo.setTotalRecord(count);
		resultMap.put("pageInfo", pageInfo);
		return ResponseFormat.retParam(1, 200, resultMap);
	}
	
	
	
	public static JsonBean GetGatheroracle(String url,String username,String password,String sql,PageInfo<Map<String,Object>> pageInfo) throws Exception {
		  Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
	      // String url = "jdbc:oracle:thin:@192.0.2.200:1521:orcl";
		   //String url = properties.getProperty("cwurl");
		   System.out.println(url);

	      //String password = "1";
	      Connection connGRC = DriverManager.getConnection(url,  username, password);
	      Statement stmtGRC = connGRC.createStatement();
	      stmtGRC.setQueryTimeout(0);
	      StringBuffer sb = new StringBuffer("SELECT * FROM (SELECT T1.*,ROWNUM RN  FROM ("+sql+") T1 WHERE ROWNUM <= "+(pageInfo.getCurrentRecord()+pageInfo.getPageSize())+" ) T2 WHERE T2.RN > "+pageInfo.getCurrentRecord());
	     System.out.println(sb.toString());
	      ResultSet rsOracle = stmtGRC.executeQuery(sb.toString());
	      ResultSetMetaData meta = rsOracle.getMetaData();
		  int columncount = meta.getColumnCount();
		  System.out.println("====="+columncount);
	      List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			while (rsOracle.next()) {
				Map<String, Object> map = new HashMap<String, Object>();
				for (int i = 1; i <= columncount; i++) {
					map.put(meta.getColumnName(i), rsOracle.getObject(i));
				}
				list.add(map);
			}
	      
	      rsOracle.close();
	      
	      
	      StringBuffer count = new StringBuffer("SELECT count(*) as count FROM ("+sql+")  a ");
	      ResultSet cOracle = stmtGRC.executeQuery(count.toString());
	      ResultSetMetaData cmeta = cOracle.getMetaData();
		  int columncountc = cmeta.getColumnCount();
		  int columncountcc=0;
		  while (cOracle.next()) {
				Map<String, Object> map = new HashMap<String, Object>();
				for (int i = 1; i <= columncountc; i++) {
					columncountcc=Integer.parseInt(cOracle.getObject(i).toString());
				}
				list.add(map);
			}
	      cOracle.close();
	      
	      
	    stmtGRC.close();
	    connGRC.close();
	      
	    Map<String,Object> resultMap = new HashMap<String,Object>(0);
	    	
		System.out.println(list);
		System.out.println(columncountcc);
		pageInfo.setTlist(list);
		pageInfo.setTotalRecord(columncountcc);
		pageInfo.getTotalPage();
	    resultMap.put("pageInfo", pageInfo);
		return ResponseFormat.retParam(1,200,resultMap);
	}
	
	
	
	public static List<Object[]> GetGatheroracleall(String url,String username,String password,String sql,Integer length) throws Exception {
		  Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
	      // String url = "jdbc:oracle:thin:@192.0.2.200:1521:orcl";
		   //String url = properties.getProperty("cwurl");
		   System.out.println(url);

	      //String password = "1";
	      Connection connGRC = DriverManager.getConnection(url,  username, password);
	      Statement stmtGRC = connGRC.createStatement();
	      stmtGRC.setQueryTimeout(0);
	      StringBuffer sb = new StringBuffer(sql);
	     System.out.println(sb.toString());
	      ResultSet rsOracle = stmtGRC.executeQuery(sb.toString());
	      ResultSetMetaData meta = rsOracle.getMetaData();
		  int columncount = meta.getColumnCount();
		  System.out.println("====="+columncount);
	      List<Object[]> contractlist = new ArrayList<Object[]>(0);
			while (rsOracle.next()) {
				Object[] objs = new Object[length];
				for (int i = 1; i <= columncount; i++) {
					objs[i-1]=rsOracle.getObject(i);
				}
				contractlist.add(objs);
			}
	      
	      rsOracle.close();
	      
	    stmtGRC.close();
	    connGRC.close();
		return contractlist;
	}
	
	
	public static List<Object[]> GetGathermysqlall(String url,String username,String password,String sql,Integer length) throws Exception {
		Class.forName("com.mysql.jdbc.Driver").newInstance();
	      // String url = "jdbc:oracle:thin:@192.0.2.200:1521:orcl";
		   //String url = properties.getProperty("cwurl");
		   System.out.println(url);

	      //String password = "1";
	      Connection connGRC = DriverManager.getConnection(url,  username, password);
	      Statement stmtGRC = connGRC.createStatement();
	      stmtGRC.setQueryTimeout(0);
	      StringBuffer sb = new StringBuffer(sql);
	     System.out.println(sb.toString());
	      ResultSet rsOracle = stmtGRC.executeQuery(sb.toString());
	      ResultSetMetaData meta = rsOracle.getMetaData();
		  int columncount = meta.getColumnCount();
		  System.out.println("====="+columncount);
	      List<Object[]> contractlist = new ArrayList<Object[]>(0);
			while (rsOracle.next()) {
				Object[] objs = new Object[length];
				for (int i = 1; i <= columncount; i++) {
					objs[i-1]=rsOracle.getObject(i);
				}
				contractlist.add(objs);
			}
	      
	      rsOracle.close();
	      
	    stmtGRC.close();
	    connGRC.close();
		return contractlist;
	}
	
	public static List<Object[]> GetGatherSqlserverall(String url,String username,String password,String sql,Integer length) throws Exception {
		Connection connGRC = BaseDaoSqlServer.getInstance().getGroupConnectionsj(url, username, password);
		System.out.println(url);
	    Statement stmtGRC = connGRC.createStatement();
	    stmtGRC.setQueryTimeout(0);
	    StringBuffer sb = new StringBuffer(sql);
	     System.out.println(sb.toString());
	      ResultSet rsOracle = stmtGRC.executeQuery(sb.toString());
	      ResultSetMetaData meta = rsOracle.getMetaData();
		  int columncount = meta.getColumnCount();
		  System.out.println("====="+columncount);
	      List<Object[]> contractlist = new ArrayList<Object[]>(0);
			while (rsOracle.next()) {
				Object[] objs = new Object[length];
				for (int i = 1; i <= columncount; i++) {
					objs[i-1]=rsOracle.getObject(i);
				}
				contractlist.add(objs);
			}
	      
	      rsOracle.close();
	      
	    stmtGRC.close();
	    connGRC.close();
		return contractlist;
	}
	
	
	public static JsonBean GetGathermysql(String url,String username,String password,String sql,PageInfo<Map<String,Object>> pageInfo) throws Exception {
		  Class.forName("com.mysql.jdbc.Driver").newInstance();
	      // String url = "jdbc:oracle:thin:@192.0.2.200:1521:orcl";
		   //String url = properties.getProperty("cwurl");
		   System.out.println(url);

	      //String password = "1";
	      Connection connGRC = DriverManager.getConnection(url,  username, password);
	      Statement stmtGRC = connGRC.createStatement();
	      stmtGRC.setQueryTimeout(0);
	      StringBuffer sb = new StringBuffer("SELECT *,1 as RN FROM ("+sql+") a limit "+(pageInfo.getCurrentRecord()+pageInfo.getPageSize())+","+pageInfo.getPageSize());
	      //StringBuffer sb = new StringBuffer("SELECT * FROM (SELECT T1.*,ROWNUM RN  FROM ("+sql+") T1 WHERE ROWNUM <= "+(pageInfo.getCurrentRecord()+pageInfo.getPageSize())+" ) T2 WHERE T2.RN > "+pageInfo.getCurrentRecord());
	      System.out.println(sb.toString());
	      ResultSet rsOracle = stmtGRC.executeQuery(sb.toString());
	      ResultSetMetaData meta = rsOracle.getMetaData();
		  int columncount = meta.getColumnCount();
		  System.out.println("====="+columncount);
	      List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			while (rsOracle.next()) {
				Map<String, Object> map = new HashMap<String, Object>();
				for (int i = 1; i <= columncount; i++) {
					map.put(meta.getColumnName(i), rsOracle.getObject(i));
				}
				list.add(map);
			}
	      
	      rsOracle.close();
	      
	      
	      StringBuffer count = new StringBuffer("SELECT count(*) as count FROM ("+sql+") a  ");
	      ResultSet cOracle = stmtGRC.executeQuery(count.toString());
	      ResultSetMetaData cmeta = cOracle.getMetaData();
		  int columncountc = cmeta.getColumnCount();
		  int columncountcc=0;
		  while (cOracle.next()) {
				Map<String, Object> map = new HashMap<String, Object>();
				for (int i = 1; i <= columncountc; i++) {
					columncountcc=Integer.parseInt(cOracle.getObject(i).toString());
				}
				list.add(map);
			}
	      cOracle.close();
	      
	      
	    stmtGRC.close();
	    connGRC.close();
	      
	    Map<String,Object> resultMap = new HashMap<String,Object>(0);
	    	
		System.out.println(list);
		System.out.println(columncountcc);
		pageInfo.setTlist(list);
		pageInfo.setTotalRecord(columncountcc);
		pageInfo.getTotalPage();
	    resultMap.put("pageInfo", pageInfo);
		return ResponseFormat.retParam(1,200,resultMap);
	}
	
	
	
	public static JsonBean GetGatherSqlServer(String url,String username,String password,String sql,PageInfo<Map<String,Object>> pageInfo) throws Exception {
		  Connection connGRC = BaseDaoSqlServer.getInstance().getGroupConnectionsj(url, username, password);
	      // String url = "jdbc:oracle:thin:@192.0.2.200:1521:orcl";
		   //String url = properties.getProperty("cwurl");
		   System.out.println(url);

	      //String password = "1";
	      Statement stmtGRC = connGRC.createStatement();
	      stmtGRC.setQueryTimeout(0);
	     // StringBuffer sb = new StringBuffer("SELECT * FROM (SELECT T1.*,ROW_NUMBER() OVER(ORDER BY Id DESC)  RN  FROM ("+sql+") T1 ) T2 WHERE T2.RN BETWEEN "+(pageInfo.getCurrentRecord()+1) +" and "+(pageInfo.getCurrentRecord()+pageInfo.getPageSize()));
//	      StringBuffer sb = new StringBuffer("SELECT *,1 as RN FROM ("+sql+" order by id offset ("+(pageInfo.getCurrentRecord()+pageInfo.getPageSize())+") rows fetch next "+pageInfo.getPageSize()+" rows only ) a ");
	      StringBuffer sb = new StringBuffer("SELECT *,1 as RN FROM ("+sql+" fetch next "+pageInfo.getPageSize()+" rows only ) a ");
	      log.info("GetGatherSqlServer:{}",sb);
	      ResultSet rsOracle = stmtGRC.executeQuery(sb.toString());
	      ResultSetMetaData meta = rsOracle.getMetaData();
		  int columncount = meta.getColumnCount();
		  log.info("GetGatherSqlServer:{}",columncount);
	      List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			while (rsOracle.next()) {
				Map<String, Object> map = new HashMap<String, Object>();
				for (int i = 1; i <= columncount; i++) {
					map.put(meta.getColumnName(i), rsOracle.getObject(i));
				}
				list.add(map);
			}
	      
	      rsOracle.close();
	      
	      
	      StringBuffer count = new StringBuffer("SELECT count(*) as count FROM ("+sql+") a  ");
	      ResultSet cOracle = stmtGRC.executeQuery(count.toString());
	      ResultSetMetaData cmeta = cOracle.getMetaData();
		  int columncountc = cmeta.getColumnCount();
		  int columncountcc=0;
		  while (cOracle.next()) {
				Map<String, Object> map = new HashMap<String, Object>();
				for (int i = 1; i <= columncountc; i++) {
					columncountcc=Integer.parseInt(cOracle.getObject(i).toString());
				}
				list.add(map);
			}
	      cOracle.close();
	      
	      
	    stmtGRC.close();
	    connGRC.close();
	      
	    Map<String,Object> resultMap = new HashMap<String,Object>(0);

		log.info("GetGatherSqlServer:{}",list);
		log.info("GetGatherSqlServer:{}",columncountcc);
		pageInfo.setTlist(list);
		pageInfo.setTotalRecord(columncountcc);
		pageInfo.getTotalPage();
	    resultMap.put("pageInfo", pageInfo);
		return ResponseFormat.retParam(1,200,resultMap);
	}
	
	
	
	public static List<String> getTbable(String sql, TblAuditModelDataSourceOracle dataSource) throws SQLException {
		Statement stmt = null;
		List<String> typelist = new ArrayList<String>();
		log.info("获取表信息SQL：{}",sql);
		try {
			Connection connGRC = DriverManager.getConnection(dataSource.getDataBaseConnectionAddress(),  dataSource.getDataBaseUsers(), dataSource.getDataBasePassWord());
			stmt = connGRC.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			ResultSetMetaData meta = rs.getMetaData();
			int columncount = meta.getColumnCount();

			for (int i = 1; i <= columncount; i++) {
				if (typelist.indexOf(meta.getColumnName(i)) == -1) {
					typelist.add((meta.getColumnName(i)));
				}
			}
			rs.close();
			stmt.close();
			connGRC.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
				}
			}
		}
		return typelist;
	}
	
	
	public static void executeSql(String tbaleName, TblAuditModelDataSourceOracle dataSource, List<String> params) {
		List<String> sqlList = new ArrayList<String>();
		StringBuffer buffer = new StringBuffer();
		buffer.append("create table " + tbaleName + " (");
		
		buffer.append("ID NUMBER NOT NULL,");
		// buffer.append("STAFFID NUMBER NOT NULL,");
		buffer.append("EXECTIME  VARCHAR2(500),");
		for (int i = 0; i < params.size(); i++) {
			if (i == params.size() - 1) {
				buffer.append("" + params.get(i) + " VARCHAR2(4000) )");
			} else {
				buffer.append("" + params.get(i) + " VARCHAR2(4000) ,");
			}
		}
		sqlList.add(buffer.toString());

		Connection connGRC =null;
		Statement stmt = null;
		try {
			connGRC = DriverManager.getConnection(dataSource.getDataBaseConnectionAddress(),  dataSource.getDataBaseUsers(), dataSource.getDataBasePassWord());
			stmt = connGRC.createStatement();
			for (String string : sqlList) {
				 stmt.executeUpdate(string);
			}
			stmt.close();
			connGRC.close();
			log.info("创建表：{},{}",tbaleName,dataSource.getDataBaseConnectionAddress());
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			
			try {
				stmt.close();
				connGRC.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	public static List<Map<String, Object>> getData(String sql,TblAuditModelDataSourceOracle dataSource) throws SQLException {
	    Connection connGRC = DriverManager.getConnection(dataSource.getDataBaseConnectionAddress(),  dataSource.getDataBaseUsers(), dataSource.getDataBasePassWord());
		Statement stmt = null;
		stmt = connGRC.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		ResultSetMetaData meta = rs.getMetaData();
		int columncount = meta.getColumnCount();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		while (rs.next()) {
			Map<String, Object> map = new HashMap<String, Object>();
			for (int i = 1; i <= columncount; i++) {
				map.put(meta.getColumnName(i), rs.getObject(i));
			}
			list.add(map);
		}
		rs.close();
		stmt.close();
		connGRC.close();
		return list;
	}
	
	public static void insertData(String tableName, List<Map<String, Object>> list,TblAuditModelDataSourceOracle dataSource, String dateTime) {
		List<String> sqlList = new ArrayList<String>();
		long l = 0l;
		Long time = System.currentTimeMillis();
		for (Map<String, Object> map : list) {
			StringBuffer buffer = new StringBuffer();
			l++;
			String installSql = "INSERT INTO "  + tableName + " (ID,EXECTIME,";
			buffer.append(installSql);
			int i = 0;
			for (Map.Entry<String, Object> entry : map.entrySet()) {
				buffer.append(entry.getKey());
				i++;
				if (i < map.size()) {
					buffer.append(",");
				}
			}
			buffer.append(") ");
			buffer.append(" VALUES(" + (time +""+ l) + ", ");
			buffer.append("'" + dateTime + "' ,");
			System.out.println(l);
			int ii = 0;
			for (Map.Entry<String, Object> entry : map.entrySet()) {
				buffer.append("'" + entry.getValue() + "'");
				ii++;
				if (ii < map.size()) {
					buffer.append(",");
				}
			}
			buffer.append(") ");
			sqlList.add(buffer.toString());
		}

		Connection connGRC =null;
		Statement stmt = null;
		try {
			connGRC = DriverManager.getConnection(dataSource.getDataBaseConnectionAddress(),  dataSource.getDataBaseUsers(), dataSource.getDataBasePassWord());
			stmt = connGRC.createStatement();
			for (String sql : sqlList) {
				 stmt.executeUpdate(sql);
			}
			log.info("保存执行结果：{},{}",tableName,dataSource.getDataBaseConnectionAddress());
			stmt.close();
			connGRC.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			
			try {
				stmt.close();
				connGRC.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	
	
	public static boolean isNotExistsTable(String table, String bookName,TblAuditModelDataSourceOracle dataSource) {
		Connection connGRC =null;
			
		boolean is = false;
		try {
			connGRC = DriverManager.getConnection(dataSource.getDataBaseConnectionAddress(),  dataSource.getDataBaseUsers(), dataSource.getDataBasePassWord());
			DatabaseMetaData metaData = connGRC.getMetaData();
			// 修改数据库用户名
			ResultSet rs = metaData.getTables(null, bookName.toUpperCase(), table.toUpperCase(), new String[] { "TABLE" });
			if (rs.next()) {
				System.out.println("表存在...");
				rs.close();
				connGRC.close();
				is = true;
			} else {
				System.out.println("表不存在...");
				rs.close();
				connGRC.close();
				is = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return is;
	}

	public static JsonBean GetGatherList(String username, String sql) throws Exception {
		Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
		// String url = "jdbc:oracle:thin:@192.0.2.200:1521:orcl";
		String url = properties.getProperty("cwurl");
		System.out.println(url);

		String password = "1";
		Connection connGRC = DriverManager.getConnection(url, username, password);
		Statement stmtGRC = connGRC.createStatement();
		stmtGRC.setQueryTimeout(0);
		StringBuffer sb = new StringBuffer(sql);
		ResultSet rsOracle = stmtGRC.executeQuery(sb.toString());
		ResultSetMetaData meta = rsOracle.getMetaData();
		int columncount = meta.getColumnCount();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		while (rsOracle.next()) {
			Map<String, Object> map = new HashMap<String, Object>();
			for (int i = 1; i <= columncount; i++) {
				map.put(meta.getColumnName(i), rsOracle.getObject(i));
			}
			list.add(map);
		}

		rsOracle.close();
		StringBuffer count = new StringBuffer("SELECT count(*) as count FROM (" + sql + ")   ");
		ResultSet cOracle = stmtGRC.executeQuery(count.toString());
		ResultSetMetaData cmeta = cOracle.getMetaData();
		int columncountc = cmeta.getColumnCount();
		int columncountcc = 0;
		while (cOracle.next()) {
			Map<String, Object> map = new HashMap<String, Object>();
			for (int i = 1; i <= columncountc; i++) {
				columncountcc = Integer.parseInt(cOracle.getObject(i).toString());
			}
			list.add(map);
		}
		cOracle.close();
		stmtGRC.close();
		connGRC.close();
		return ResponseFormat.retParam(1, 200, list);
	}
	
	
	
	public static JsonBean GetGatherListoracle(String url,String username, String password,String sql) throws Exception {
		Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
		// String url = "jdbc:oracle:thin:@192.0.2.200:1521:orcl";
		//String url = properties.getProperty("cwurl");
		System.out.println(url);

		Connection connGRC = DriverManager.getConnection(url, username, password);
		Statement stmtGRC = connGRC.createStatement();
		stmtGRC.setQueryTimeout(0);
		StringBuffer sb = new StringBuffer(sql);
		ResultSet rsOracle = stmtGRC.executeQuery(sb.toString());
		ResultSetMetaData meta = rsOracle.getMetaData();
		int columncount = meta.getColumnCount();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		while (rsOracle.next()) {
			Map<String, Object> map = new HashMap<String, Object>();
			for (int i = 1; i <= columncount; i++) {
				map.put(meta.getColumnName(i), rsOracle.getObject(i));
			}
			list.add(map);
		}

		rsOracle.close();
		StringBuffer count = new StringBuffer("SELECT count(*) as count FROM (" + sql + ")   ");
		ResultSet cOracle = stmtGRC.executeQuery(count.toString());
		ResultSetMetaData cmeta = cOracle.getMetaData();
		int columncountc = cmeta.getColumnCount();
		int columncountcc = 0;
		while (cOracle.next()) {
			Map<String, Object> map = new HashMap<String, Object>();
			for (int i = 1; i <= columncountc; i++) {
				columncountcc = Integer.parseInt(cOracle.getObject(i).toString());
			}
			list.add(map);
		}
		cOracle.close();
		stmtGRC.close();
		connGRC.close();
		return ResponseFormat.retParam(1, 200, list);
	}
	
	public static JsonBean GetGatherListmysql(String url,String username,String password,String sql) throws Exception {
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		// String url = "jdbc:oracle:thin:@192.0.2.200:1521:orcl";
		//String url = properties.getProperty("cwurl");
		System.out.println(url);

		//String password = "1";
		Connection connGRC = DriverManager.getConnection(url, username, password);
		Statement stmtGRC = connGRC.createStatement();
		stmtGRC.setQueryTimeout(0);
		StringBuffer sb = new StringBuffer(sql);
		ResultSet rsOracle = stmtGRC.executeQuery(sb.toString());
		ResultSetMetaData meta = rsOracle.getMetaData();
		int columncount = meta.getColumnCount();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		while (rsOracle.next()) {
			Map<String, Object> map = new HashMap<String, Object>();
			for (int i = 1; i <= columncount; i++) {
				map.put(meta.getColumnName(i), rsOracle.getObject(i));
			}
			list.add(map);
		}

		
		rsOracle.close();
		stmtGRC.close();
		connGRC.close();
		return ResponseFormat.retParam(1, 200, list);
	}
	
	public static JsonBean GetGatherListSqlServer(String url,String username,String password,String sql) throws Exception {
		Connection connGRC = BaseDaoSqlServer.getInstance().getGroupConnectionsj(url, username, password);
		// String url = "jdbc:oracle:thin:@192.0.2.200:1521:orcl";
		//String url = properties.getProperty("cwurl");
		System.out.println(url);

		//String password = "1";
		//Connection connGRC = DriverManager.getConnection(url, username, password);
		Statement stmtGRC = connGRC.createStatement();
		stmtGRC.setQueryTimeout(0);
		StringBuffer sb = new StringBuffer(sql);
		ResultSet rsOracle = stmtGRC.executeQuery(sb.toString());
		ResultSetMetaData meta = rsOracle.getMetaData();
		int columncount = meta.getColumnCount();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		while (rsOracle.next()) {
			Map<String, Object> map = new HashMap<String, Object>();
			for (int i = 1; i <= columncount; i++) {
				map.put(meta.getColumnName(i), rsOracle.getObject(i));
			}
			list.add(map);
		}

		rsOracle.close();
		stmtGRC.close();
		connGRC.close();
		return ResponseFormat.retParam(1, 200, list);
	}
	
	public static List<String> getTbableall(String sql,String type,String url,String username,String password) throws Exception {
		 Connection connGRC =null;
		if(type!=null && type.equals("1")) {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			connGRC = DriverManager.getConnection(url,  username, password);
		}else if(type!=null && type.equals("2")) {
			connGRC = BaseDaoSqlServer.getInstance().getGroupConnectionsj(url, username, password);
		}else if(type!=null && type.equals("3")) {
			Class.forName("dm.jdbc.driver.DmDriver").newInstance();
			connGRC = DriverManager.getConnection(url,  username, password);
		}else {
			Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
			connGRC = DriverManager.getConnection(url, username, password);
		}
		
		Statement stmt = null;
		List<String> typelist = new ArrayList<String>();
		try {
			System.out.println(url);

			
			stmt = connGRC.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			ResultSetMetaData meta = rs.getMetaData();
			int columncount = meta.getColumnCount();

			for (int i = 1; i <= columncount; i++) {
				if (typelist.indexOf(meta.getColumnName(i)) == -1) {
					typelist.add((meta.getColumnName(i)));
				}
			}
			rs.close();
			stmt.close();
			connGRC.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
				}
			}
		}
		return typelist;
	}
	
	
	public static void main(String[] args) {
        PageInfo<Map<String,Object>> pageInfo = new PageInfo<Map<String,Object>>();
    	
    	pageInfo.setPageSize(15);
    	pageInfo.setCurrentPage(1);
    	String sql="SELECT * FROM  TBL_ACCOUNT";
		try {
//			List<Object[]> list = JDBCProperties.GetGatheroracleall("jdbc:oracle:thin:@192.0.2.200:1521:orcl", "HBFKCWZT2018", "1", sql,null);
//			List<String> cnmes = JDBCProperties.getTbableall(sql,null,"jdbc:oracle:thin:@192.0.2.200:1521:orcl", "HBFKCWZT2018", "1");
//			System.out.println(cnmes);
//			System.out.println(list);
			//JsonBean jsonBean = JDBCProperties.GetGather("HBFKCWZT2018", sql, pageInfo);
			String code="FL01-01-DL-20230809-001";
			code=code.substring(0,10);
			System.out.println("编号："+code);
			
			SimpleDateFormat sdFormat = new SimpleDateFormat("yyyyMMdd");
			Calendar cal = Calendar.getInstance();
			 int year = cal.get(Calendar.YEAR);
			Date date = cal.getTime();
			String myTime = sdFormat.format(date);
			System.out.println(myTime);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 校验数据库地址与账号密码是否存在
	 * @param username
	 * @param password
	 * @return
	 * @throws Exception
	 */
	public static Boolean checkConnection(String url, String username, String password) {
		boolean flags = true;
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
			log.info("Oracle 请求地址：{}  ...用户：{} ...密码：{}", url, username, password);
			Connection connGRC = DriverManager.getConnection(url, username, password);
			if (connGRC != null) {
				connGRC.close();
			}
		} catch (Exception e) {
			log.error("校验数据库地址与账号密码是否存在 接口异常:", e);
			flags = false;
		}
		return flags;
	}

}
