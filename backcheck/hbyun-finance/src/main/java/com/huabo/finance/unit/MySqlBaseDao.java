package com.huabo.finance.unit;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class MySqlBaseDao {
	private static MySqlBaseDao configManager;
	
	static{
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			throw new ExceptionInInitializerError(e);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
		}
	}

	
	
	private MySqlBaseDao(){
		
	}
	
	
	public static MySqlBaseDao getInstance() throws Exception{
		if(configManager==null){
			configManager = new MySqlBaseDao();
		}
		if(configManager == null) {
			//加入有一个线程走到这里,然后又然给另一个线程执行完
			synchronized(MySqlBaseDao.class) {
				if(configManager == null) {
					configManager = new MySqlBaseDao();
				}
			}
		}
		return configManager;
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
    
}
