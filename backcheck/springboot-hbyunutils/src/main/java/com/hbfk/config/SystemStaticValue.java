package com.hbfk.config;

public class SystemStaticValue {
	
	/**
	 * 数据库类型
	 * DM - 达梦数据库
	 * Oracle - oracle数据库
	 * MySql - mysql数据库
	 */
	public static final String DATABASETYPE = "DM";
	
	public static final String SYSTEMVERSION = "5.0";
	
	//业务中台系统管理员角色主键
	public static final String YWZTROLEADMINID = "94e3a9bb0fce4547886972998fddba1c";
	
	
	public static final String SJLBQX = "审计责任人,审计负责人";
	public static final String FXYD = "风险管理员";
	public static final String STRUCTURE = "集团";
	
	
	public static final boolean REQUIREMENTVALIDATE = false;//是否启用系统字段维护确认功能，true-启用 false-不启用
	
	
	
	//派可路径配置
	//1.派可登录地址：
	public static final String PKLOGINURL="http://192.0.2.200:9001/sso/integrationConfig/ssoLogin";
	public static final String PKMD5KEY="hbyfk147852";
	public static final String PKUSER="fengkong1";
	public static final String PKPWD="REDACTED";
	
	//流程任务节点状态静态变量
	public static final Integer OperatorStateDqs = 0; //代签收
	public static final Integer OperatorStateLzz = 1; //流转中
	public static final Integer OperatorStateJq = 2; //加签
	public static final Integer OperatorStateZb = 3; //转办
	public static final Integer OperatorStateZp = 4; //指派
	public static final Integer OperatorStateTh = 5; //退回
	public static final Integer OperatorStateCh = 6; //撤回
	public static final Integer OperatorStateXb = 7; //协办
	public static final Integer OperatorStateCx = 8; //撤销
	public static final Integer OperatorStateNo = -1; //无用节点
	public static final Integer OperatorStateWjh = -2; //未激活
	
	
	
	
}
