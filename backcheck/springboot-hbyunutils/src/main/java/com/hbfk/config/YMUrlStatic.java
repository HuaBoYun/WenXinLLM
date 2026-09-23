package com.hbfk.config;

import java.util.HashMap;
import java.util.Map;

public class YMUrlStatic {

	public static String dbType = "MySql"; // 数据库类型 DM MySql Oracle

	// 流程平台版本信息3.4.2 ，5.0+
	public static String YMVERSION = "5.0+";
	// public static String YMVERSION = "3.4.2";

	// 是否调用流程平台 0调用 1 弃用
	public static Integer status = 0;

	public static final Integer STATE_WSP = 0;// 未审批

	public static final Integer STATE_SPZ = 1;// 审批中

	public static final Integer STATE_XTZ = 2;// 已退回 需调整

	public static final Integer STATE_YCX = 3;// 已撤销

	public static final Integer STATE_YWC = 6;// 已完成

	public static final Integer ORGINSERT = 1;// 登录后操作组织新增
	public static final Integer ORGUPDATE = 2;// 登录后操作组织修改
	public static final Integer ORGDELETE = 3;// 登录后操作组织删除

	public static final Integer DEPTINSERT = 4;// 登录后操作组织新增
	public static final Integer DEPTUPDATE = 5;// 登录后操作组织修改
	public static final Integer DEPTDELETE = 6;// 登录后操作组织删除

	public static final String SUPERACCOUNT = "admin";// 流程平台超级管理员账号

	public static final String YMSPACKEY = "YMSPACCKEY";// 流程平台超级管理账号在redis中的key

	public static String premissionGroupId = "585484516261364613";// 默认权限组id

	public static final Map<String, String> headerMap = new HashMap<String, String>(0); // 访问流程平台静态hear 信息MAp

	public static String dbMySqlDriver = "com.mysql.cj.jdbc.Driver"; // 数据库驱动
	public static String dmDriver = "dm.jdbc.driver.DmDriver"; // 数据库驱动

//	//中核正式环境
	// public static String interfaceUrl = "http://127.0.0.1:30000";

	// 正式环境
	public static String dmUser = "JNPF_BOOT_INIT"; // 数据库用户名
	public static String dmPassword = "REDACTED"; // 数据库密码
	public static String dmUrl = "jdbc:dm://192.0.2.200:5236/JNPF_BOOT_INIT?zeroDateTimeBehavior=convertToNull&useUnicode=true&characterEncoding=utf-8";

	// 测试环境
	/*
	 * public static String dmUser="JNPF_INIT"; //数据库用户名 public static String
	 * dmPassword="REDACTED"; //数据库密码 public static String dmUrl =
	 * "jdbc:dm://127.0.0.1:5236/JNPF_INIT?zeroDateTimeBehavior=convertToNull&useUnicode=true&characterEncoding=utf-8";
	 */

	//	数据库连接字符串
	public static String dbOracleDriver = "oracle.jdbc.driver.OracleDriver"; // 数据库驱动

	public static String oracleUser = "REDACTED";// 数据库用户名

	public static String oraclePassword = "REDACTED"; // 数据库密码

	// 数据库连接字符串
	public static String oracleUrl = "jdbc:oracle:thin:@192.0.2.200:1521:orcl";

	// 流程平台访问地址

	public static String mysqlUser = "root"; // 数据库用户名

//	星光正式环境数据库连接字符串
	//    public static String mysqlUrl = "jdbc:mysql://192.0.2.200:3306/jnpf_init?characterEncoding=utf8&useSSL=true&useOldAliasMetadataBehavior=true&autoReconnect=true&failOverReadOnly=false&serverTimezone=Asia/Shanghai";
	//    public static String interfaceUrl = "http://192.0.2.200:30000";
	//    public static String mysqlPassword = "REDACTED"; //数据库密码


	// 星光测试环境
	/*
	 * public static String mysqlUrl =
	 * "jdbc:mysql://192.0.2.200:3306/jnpf_init?characterEncoding=utf8&useSSL=true&useOldAliasMetadataBehavior=true&autoReconnect=true&failOverReadOnly=false&serverTimezone=Asia/Shanghai";
	 * public static String interfaceUrl = "http://192.0.2.200:30000"; public
	 * static String mysqlPassword = "REDACTED"; //数据库密码
	 */

	public static String mysqlUrl = "jdbc:mysql://192.0.2.200:3306/jnpf_init?characterEncoding=utf8&useSSL=true&useOldAliasMetadataBehavior=true&autoReconnect=true&failOverReadOnly=false&serverTimezone=Asia/Shanghai";
	public static String interfaceUrl = "http://192.0.2.200:30000";
	public static String mysqlPassword="REDACTED"; //数据库密码


	// public static String
	// mysqlUrl="jdbc:mysql://192.0.2.200:3306/jnpf_init?characterEncoding=utf8&useSSL=true&useOldAliasMetadataBehavior=true&autoReconnect=true&failOverReadOnly=false&serverTimezone=Asia/Shanghai";

//	//大庆
	/*
	 * public static String interfaceUrl = "http://192.0.2.200:30000"; public
	 * static String mysqlUrl =
	 * "jdbc:mysql://192.0.2.200:3306/dayt_init?characterEncoding=utf8&useSSL=true&useOldAliasMetadataBehavior=true&autoReconnect=true&failOverReadOnly=false&serverTimezone=Asia/Shanghai";
	 * public static String mysqlPassword="REDACTED"; //数据库密码
	 */
	// 大庆新服务器
	/*
	 * public static String interfaceUrl = "http://192.0.2.200:30000"; public
	 * static String mysqlUrl =
	 * "jdbc:mysql://192.0.2.200:3306/dayt_init?characterEncoding=utf8&useSSL=true&useOldAliasMetadataBehavior=true&autoReconnect=true&failOverReadOnly=false&serverTimezone=Asia/Shanghai";
	 * public static String mysqlPassword="REDACTED"; //数据库密码
	 */

	// 普通用户初始化模块功能
	public static String rightNames = "工作流程,";

	// 流程平台用户通用密码
	public static String ymPassword = "REDACTED";

	// public static String interfaceUrl = "http://192.0.2.200:30000";
	// 流程平台访问地址
	// public static String interfaceUrl = "http://127.0.0.1:30000";

	// 流程平台登录获取token地址
	public static String singelSignUrl = "/api/oauth/Login";

	// 权限组获取操作成员地址
	public static String premissionGroupMember = "/api/permission/PermissionGroup/PermissionMember/#{id}";

	// 公司集合地址
	public static String orgListUrl = "/api/permission/Organize";

	// 保存公司地址
	public static String saveOrgUrl = "/api/permission/Organize";

	// 保存部门地址
	public static String saveDeptUrl = "/api/permission/Organize/Department";

	// 保存角色地址
	public static String saveRoleUrl = "/api/permission/Role";

	// 修改角色地址
	public static String updateRoleUrl = "/api/permission/Role";

	// 保存岗位地址
	public static String saveJobUrl = "/api/permission/Position";

	// 修改岗位地址
	public static String updateJobUrl = "/api/permission/Position/#{id}";

	// 保存用户地址
	public static String saveStaffUrl = "/api/permission/Users";

	// 修改用户地址
	public static String updateStaffUrl = "/api/permission/Users";

	// 修改用户密码地址
	public static String updatePasswordUrl = "/api/permission/Users/#{id}/Actions/ResetPassword";

	// 获取权限接口地址
	public static String authorityAllUrl = "/api/permission/Authority/Data/#{id}/Values";

	// 单个角色授权地址
	public static String grantRoleRight = "/api/permission/Authority/Data/#{id}";

	// 批量角色授权地址
	public static String grantBatchRoleRight = "/api/permission/Authority/Data/Batch";

	// 工作流列表页面地址
	public static String getWorkFlowInfo = "/api/workflow/Engine/FlowEngine";

	// 获取下一步审批节点
	public static String getApprovalUser = "/api/workflow/Engine/FlowBefore/Candidates/#{id}";

	// 获取候选人信息
	public static String getCandidateUser = "/api/workflow/Engine/FlowBefore/CandidateUser/#{id}";

	// 提交审批
	public static String submitFlow = "/api/workflow/Engine/FlowTask/#{id}";

	// 工作流程我发起的查询路径
	public static String flowLaunch = "/api/workflow/Engine/FlowLaunch";

	// 工作流程待办事宜查询路径
	public static String workList = "/api/workflow/Engine/FlowBefore/List/";

	// 工作流路径信息
	public static String workInfo = "/api/workflow/Engine/FlowBefore/";

	// 我发起的 撤回接口
	public static String actionsWithdraw = "/api/workflow/Engine/FlowLaunch/#{id}/Actions/Withdraw";

	// 我的待办 退回接口
	public static String rejectUrl = "/api/workflow/Engine/FlowBefore/Reject/";

	// 我的待办 通过接口
	public static String auditUrl = "/api/workflow/Engine/FlowBefore/Audit/";

	// 我的待办 转审接口
	public static String transferUrl = "/api/workflow/Engine/FlowBefore/Transfer/";

	// 已办事宜 撤回接口
	public static String recallUrl = "/api/workflow/Engine/FlowBefore/Recall/";

	// 拒绝获取拒绝的审批节点
	public static String rejectList = "/api/workflow/Engine/FlowBefore/RejectList/";

	// 切换公司
	public static String changeOrgUrl = "/api/permission/Users/Current/major";

	// 角色分配员工
	public static String saveRoleUserUrl = "/api/permission/UserRelation/#{id}";

	// 流程复制路径
	public static String copyFlow = "/api/workflow/Engine/FlowEngine/#{id}/Actions/Copy";

	// 批量审批获取下一步办理信息（3.x）
	public static String batchCandidateUrl = "/api/workflow/Engine/FlowBefore/BatchCandidate";

	// 批量审批操作（3.x）
	public static String batchOperationUrl = "/api/workflow/Engine/FlowBefore/BatchOperation";

	// 批量审批获取下一步办理信息（5.0+）
	public static String batchCandidateUrlNew = "/api/workflow/operator/BatchCandidate";

	// 批量审批操作（5.0+）
	public static String batchOperationUrlNew = "/api/workflow/operator/BatchOperation";

	// 5.0版本接口
	public static String submitFlowNew = "/api/workflow/task/#{id}";

	// 处理基本流程
	public static String dealFlowBaseInfo = "/api/workflow/template";

	// 获取业务类型数据
	public static String getDictionaryData = "/api/system/DictionaryData/306427078100678661";

	// 获取审批下一步节点
	public static String getCandidateNodeUrl = "/api/workflow/operator/CandidateNode/#{id}";

	// 获取审批下一步节点的候选人
	public static String getCandidateUserUrl = "/api/workflow/operator/CandidateUser/#{id}";

	// 我发起的流程撤回接口
	public static String flowEngineRecall = "/api/workflow/task/Recall/#{id}";

	// 流程操作接口
	public static String flowTransactUrl = "/api/workflow/operator/Transact";

	// 流程审批接口
	public static String flowAuditUrl = "/api/workflow/operator/Audit/#{id}";

	// 退回获取退回节点集合信息
	public static String sendBackNodeUrl = "/api/workflow/operator/SendBackNodeList/";

	// 退回
	public static String sendBackUrl = "/api/workflow/operator/SendBack/#{id}";

	// 协办
	public static String assistUrl = "/api/workflow/operator/Assist/#{id}";

	// 加签
	public static String addSignUrl = "/api/workflow/operator/AddSign/#{id}";

	// 转审
	public static String transferFiveUrl = "/api/workflow/operator/Transfer/#{id}";

	// 工作流程 我发起的 流程列表
	public static String taskList = "/api/workflow/task";

	//审批办理撤回接口
	public static String flowSendBackUrl = "/api/workflow/operator/Recall/";

	// 工作流程 待办等 流程列表
	public static String operatorListUrl = "/api/workflow/operator/List/";

	// 流程复制路径
	public static String copyFiveVersionFlow = "/api/workflow/template/#{id}/Actions/Copy";

	//管理组路径
	public static String administratorUrl="/api/permission/organizeAdminIsTrator";

	//授权路径
	public static String adminSelectorUrl= "/api/permission/organizeAdminIsTrator/organizeSelector";

	//用户列表信息
	public static String userInfoUrl = "/api/permission/Users/getUserList";

	public static String selectAsyncListUrl = "/api/permission/organizeAdminIsTrator/SelectAsyncList/";

	static {
		headerMap.put("content-type", "application/json;charset=utf-8");
	}

}
