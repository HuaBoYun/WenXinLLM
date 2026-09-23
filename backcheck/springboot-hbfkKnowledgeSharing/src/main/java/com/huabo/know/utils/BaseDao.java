package com.huabo.know.utils;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.validation.annotation.Validated;

import com.hbfk.config.YMUrlStatic;

import lombok.Data;


public class BaseDao {
	private static BaseDao configManager;
	
	static{
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		} catch (ClassNotFoundException e) {
			throw new ExceptionInInitializerError(e);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
		}
	}

	
	
	private BaseDao(){
		
	}
	
	
	public static BaseDao getInstance() throws Exception{
		if(configManager==null){
			configManager = new BaseDao();
		}
		if(configManager == null) {
			//加入有一个线程走到这里,然后又然给另一个线程执行完
			synchronized(BaseDao.class) {
				if(configManager == null) {
					configManager = new BaseDao();
				}
			}
		}
		return configManager;
	}
	
	public Connection getConnection() throws SQLException {
		if(YMUrlStatic.dbType.equals("MySql")) {
			return DriverManager.getConnection(YMUrlStatic.mysqlUrl, YMUrlStatic.mysqlUser, YMUrlStatic.mysqlPassword);
		}else {
			return DriverManager.getConnection(YMUrlStatic.oracleUrl, YMUrlStatic.oracleUser, YMUrlStatic.oraclePassword);
		}
	}
	
	public Connection getConnection(String url,String userName,String password) throws SQLException {
		return DriverManager.getConnection(url, userName, password);
	}
	
	public void close(Connection con,ResultSet rs , Statement s){
		try {
			if(rs != null){
				rs.close();
			}
			if(s!=null){
				s.close();
			}
			if(con!=null){
				con.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/** 判断数据库是否支持批处理 */
    public boolean supportBatch(Connection con) {
        try {
            // 得到数据库的元数据
            DatabaseMetaData md = con.getMetaData();
            return md.supportsBatchUpdates();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
