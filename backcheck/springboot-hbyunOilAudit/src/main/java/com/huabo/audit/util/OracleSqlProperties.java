package com.huabo.audit.util;

import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.vip.vjtools.vjkit.mapper.JsonMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class OracleSqlProperties {

	@Value("${spring.datasource.oracle.username:}")
	private String username;
	@Value("${spring.datasource.oracle.password:}")
	private String password;
	@Value("${spring.datasource.oracle.url:}")
	private String url;

	/**
	 * sql执行 默认当前用户
	 * @param sql 执行sql
	 * @return
	 * @throws Exception
	 */
	public JsonBean execSql(String sql) throws Exception {
		Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
		log.info("Oracle 请求地址：{}  ...用户：{} ...密码：{} ...sql:{}", url, username, password, sql);
		Connection connGRC = DriverManager.getConnection(url, username, password);
		Statement stmtGRC = connGRC.createStatement();
		stmtGRC.setQueryTimeout(0);
		ResultSet rsOracle = stmtGRC.executeQuery(sql);
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

	/**
	 * sql执行 传默认账户
	 * @param username 账号
	 * @param password 密码
	 * @param sql 执行sql
	 * @return
	 * @throws Exception
	 */
	public JsonBean execSql(String username, String password, String sql) throws Exception {
		Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
		log.info("Oracle 请求地址：{}  ...用户：{} ...密码：{} ...sql:{}", url, username, password, JsonMapper.INSTANCE.toJson(sql));
		Connection connGRC = DriverManager.getConnection(url, username, password);
		Statement stmtGRC = connGRC.createStatement();
		stmtGRC.setQueryTimeout(0);
		ResultSet rsOracle = stmtGRC.executeQuery(sql);
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

	/**
	 * sql执行 批量插入
	 * @param username 账号
	 * @param password 密码
	 * @param sql 执行sql
	 * @return
	 * @throws Exception
	 */
	public void insertSql(String username, String password, String sql) throws Exception {
		Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
		log.info("Oracle 请求地址：{}  ...用户：{} ...密码：{} ...sql:{}", url, username, password, JsonMapper.INSTANCE.toJson(sql));
		Connection connGRC = DriverManager.getConnection(url, username, password);
		try {
			connGRC.setAutoCommit(false);
			Statement stmtGRC = connGRC.createStatement();
			stmtGRC.setQueryTimeout(0);
			ResultSet rsOracle = stmtGRC.executeQuery(sql);
			rsOracle.close();
			stmtGRC.close();
		} catch (Exception e) {
			log.error("sql执行 批量插入 Exception异常：", e);
			try {
				connGRC.rollback();
			} catch (SQLException e1) {
				log.error("sql执行 批量插入 connGRC.rollback异常：", e1);
			}
			throw e;
		} finally {
			try {
				connGRC.close();
			} catch (SQLException e) {
				log.error("sql执行 批量插入 SQLException异常：", e);
			}
		}
	}
}
