package com.huabo.finance.unit;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class BaseDao {
	
	public static Connection getConnection(String financedbtype, String financeconn, String financeport,
			String financeuser, String financepwd, String financedbexpm) throws Exception {
		Connection conn = null;
		if("Oracle".equals(financedbtype)) {
			conn = getOracleConnection(financeconn, financeport, financeuser, financepwd, financedbexpm);
		}
		if("Mysql".equals(financedbtype)) {
			conn = MySqlBaseDao.getInstance().getConnection("jdbc:mysql://"+financeconn+":"+financeport+"/"+financedbexpm+"?characterEncoding=utf8&useSSL=true&useOldAliasMetadataBehavior=true&autoReconnect=true&failOverReadOnly=false&serverTimezone=Asia/Shanghai", financeuser, financepwd);
		}
		if("DM".equals(financedbtype)) {
			conn = DmBaseDao.getInstance().getConnection("jdbc:dm://"+financeconn+":"+financeport+"/"+financedbexpm+"?zeroDateTimeBehavior=convertToNull&useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai", financeuser, financepwd);
		}
		if("SqlServer".equals(financedbtype)) {
			conn = SqlServerBaseDao.getInstance().getConnection("jdbc:sqlserver://"+financeconn+":"+financeport+";databaseName="+financedbexpm, financeuser, financepwd);
		}
		if("inceptor".equals(financedbtype)) {
			conn = InceptorBaseDao.getInstance().getConnection("jdbc:hive2://"+financeconn+":"+financeport+"/"+financedbexpm, financeuser, financepwd);
		}
		if("OceanBase".equals(financedbtype)) {
			conn = OceanBaseDao.getInstance().getConnection("jdbc:oceanbase://"+financeconn+":"+financeport+"/"+financedbexpm+"?useUnicode=true&characterEncoding=utf8&useSSL=false", financeuser, financepwd);
		}

		return conn;
	}

	/**
	 * Oracle 连接：兼容服务名与 SID 两种监听注册方式。
	 * 服务名格式 jdbc:oracle:thin:@//host:port/service_name，
	 * SID 格式   jdbc:oracle:thin:@host:port:SID。
	 * 数据库名以 / 开头按服务名直连不回退（与采集任务的实例名约定一致）；
	 * 否则先按服务名尝试（Navicat「服务名」方式连的库均属此类），
	 * 监听器不认识该服务名（ORA-12514）时自动换 SID 格式重试，兼容历史按 SID 注册的库；
	 * 账号密码等其他错误不重试直接抛出。
	 */
	private static Connection getOracleConnection(String financeconn, String financeport,
			String financeuser, String financepwd, String financedbexpm) throws Exception {
		String serviceName = financedbexpm.startsWith("/") ? financedbexpm.substring(1) : financedbexpm;
		String serviceUrl = "jdbc:oracle:thin:@//" + financeconn + ":" + financeport + "/" + serviceName;
		if (financedbexpm.startsWith("/")) {
			return OracleBaseDao.getInstance().getConnection(serviceUrl, financeuser, financepwd);
		}
		String sidUrl = "jdbc:oracle:thin:@" + financeconn + ":" + financeport + ":" + financedbexpm;
		try {
			return OracleBaseDao.getInstance().getConnection(serviceUrl, financeuser, financepwd);
		} catch (SQLException e) {
			String msg = String.valueOf(e.getMessage());
			if (msg.contains("ORA-12514") || msg.contains("ORA-12505")) {
				return OracleBaseDao.getInstance().getConnection(sidUrl, financeuser, financepwd);
			}
			throw e;
		}
	}
	
	public static void close(String dbType,Connection con,ResultSet rs , Statement s){
		try {
			if("Oracle".equals(dbType)) {
				OracleBaseDao.getInstance().close(con, rs, s);
			}
			if("Mysql".equals(dbType)) {
				MySqlBaseDao.getInstance().close(con, rs, s);
			}
			if("DM".equals(dbType)) {
				DmBaseDao.getInstance().close(con, rs, s);
			}
			if("SqlServer".equals(dbType)) {
				SqlServerBaseDao.getInstance().close(con, rs, s);
			}
			if("inceptor".equals(dbType)) {
				InceptorBaseDao.getInstance().close(con, rs, s);
			}
			if("OceanBase".equals(dbType)) {
				OceanBaseDao.getInstance().close(con, rs, s);
			}
		} catch (Exception e) {
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
