package com.huabo.cybermonitor.task;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Service
public class ThreadTask  {

	private String sql;
	private String connectionString;
	//private HibernateExecuteService hibernateExecuteService;
	private String tableName;
	private BigDecimal userId;
//	@Autowired
//	private TblacctbookService tblacctbookService;
	private Long dateTime;
//	public void run() {
//		System.out.println("------ Preparing to execute the thread. -------");
//		//	String reg = rule.getRegexp();
//			String userName = ContextHolder.getContext();
//			if (userName == null) {
//				userName = "25982FA6";
//				ContextHolder.setContext(userName);
//			}
//
//			//String password = FxglUtil.getPwdFromName(userName);
//			String password =FxglConfig.FXGL_ORACEL_BOOK_PWD;
//
//			if (sql == null
//					|| sql.isEmpty() || connectionString == null
//					|| connectionString.isEmpty())
//				return;
//			HibernatePartner instance = new HibernatePartner();
////			String resultfile = (new SimpleDateFormat("yyyyMMdd_hhmmss")
////					.format(new Date()) + "_" + rule.getRuleid() + ".xls");
//
//			try {
//				Class.forName("oracle.jdbc.driver.OracleDriver");
//				instance.setConnection(FxglConfig.FXGL_ORACLE_CONN_STR, userName, password);
//				List<Map<String, Object>>  list = instance.getData(sql);
//				//TODO
//				//hibernateExecuteService.insertData(tableName, list, userId,dateTime);
//				//if (instance.execRule(resultfile, sql, reg)) {
//
////					TblMonitorPrewarning prewarning = new TblMonitorPrewarning();
////					prewarning.setIntermediatetablename(path
////							+ File.separator
////							+ resultfile);
////					prewarning.setTblMonitorRule(rule);
////					tblMonitorPrewarningService.add(prewarning);
//				//}
//				instance.free();
//			} catch (ClassNotFoundException e) {
//				e.printStackTrace();
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//			System.out.println("------ The thread terminated. -------");
//	}
	public String getSql() {
		return sql;
	}
	public void setSql(String sql) {
		this.sql = sql;
	}
	public String getConnectionString() {
		return connectionString;
	}
	public void setConnectionString(String connectionString) {
		this.connectionString = connectionString;
	}
//	public HibernateExecuteService getHibernateExecuteService() {
//		return hibernateExecuteService;
//	}
//	public void setHibernateExecuteService(HibernateExecuteService hibernateExecuteService) {
//		this.hibernateExecuteService = hibernateExecuteService;
//	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public BigDecimal getUserId() {
		return userId;
	}
	public void setUserId(BigDecimal userId) {
		this.userId = userId;
	}
	public Long getDateTime() {
		return dateTime;
	}
	public void setDateTime(Long dateTime) {
		this.dateTime = dateTime;
	}

}
