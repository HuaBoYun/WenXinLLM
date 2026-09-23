package com.hbfk.util;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class BaseDaoSqlServer {
	private static BaseDaoSqlServer configManager;
	
	//集团公司sqlserver数据库配置
	/*private final static String GROUPDBURL = "jdbc:sqlserver://192.0.2.200:1433;Databasename=a8v60sp1";
	private final static String GROUPDBUSER = "oa_szfk";
	private final static String GROUPDBPWD = "REDACTED";*/
	
	private final static String GROUPDBURL = "jdbc:sqlserver://127.0.0.1:1433;Databasename=UFDATA_218_2018";
	private final static String GROUPDBUSER = "sa";
	private final static String GROUPDBPWD = "REDACTED";
	
	
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

	
	
	private BaseDaoSqlServer(){
		
	}
	
	
	public static BaseDaoSqlServer getInstance() throws Exception{
		if(configManager==null){
			configManager = new BaseDaoSqlServer();
		}
		if(configManager == null) {
			//加入有一个线程走到这里,然后又然给另一个线程执行完
			synchronized(BaseDaoSqlServer.class) {
				if(configManager == null) {
					configManager = new BaseDaoSqlServer();
				}
			}
		}
		return configManager;
	}
	
	//获取集团公司 sqlserver数据库链接
	public Connection getGroupConnection() throws SQLException {
		return DriverManager.getConnection(GROUPDBURL, GROUPDBUSER, GROUPDBPWD);
	}
	
	
	//获取 sqlserver数据库链接
	public Connection getGroupConnectionsj(String url,String user,String password) throws SQLException {
		return DriverManager.getConnection(url, user, password);
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
