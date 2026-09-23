// tableColumnsConfig.js

import * as dayjs from 'dayjs'

// 定义不同类型表格的固定表头信息
const tableColumnsConfig = {
	'实施方案': [
		{
			title: '项目编号',
			key: 'qdcode',
			clickable: true,
		},
		{
			title: '项目名称',
			key: 'projectName',
		},
		{
			title: '项目负责人',
			key: 'projectOrderName',
		},
		{
			title: '项目类别',
			key: 'projectType',
			formatter: (row) => {
				return row.projectType == 1 ? '计划内' : '归档'
			}
		},
		{
			title: '计划开始时间',
			key: 'planStarttime',
		},
		{
			title: '计划结束时间',
			key: 'planEndtime',
		},
		{
			title: '计划年度',
			key: 'planYear',
		},
		{
			title: '项目状态',
			key: 'status',
			formatter: (row) => {
				return row.status == 1 ? '启动' : row.status == 2 ? '实施' : '未启动'

			},
		},
		{
			title: '状态',
			key: 'spzt',
			formatter: (row) => {
				let statusMap = {
					1: '审批中',
					2: '已退回',
					3: '已撤回',
					4: '已终止',
					5: '已跟踪',
					6: '已完成',
					// Map other status codes to your actual status names...
				};
				return statusMap[row.status] || '未审批';
			},
		},
	],
	'项目资料': [
		{
			title: '资料编号',
			key: 'projectDatapreId',
			clickable: true,
		},
		{
			title: '资料名称',
			key: 'dataName'
		},
		{
			title: '所属项目',
			key: 'projectname'
		},
		{
			title: '创建人',
			key: 'username'
		},
		{
			title: '创建时间',
			key: 'dataDate',
			formatter: (row) => {
				return dayjs(row.dataDate).format('YYYY-MM-DD');
			}
		},
	],
	'系统配置': [
		{
			title: '项目名称',
			key: 'prjoectName',
			clickable: true,
		},
		{
			title: '项目经理',
			key: 'realname'
		},
		{
			title: '被审计单位',
			key: 'auditOrgName'
		},
		{
			title: '项目来源',
			key: 'projectSource'
		},
		{
			title: '开始时间',
			key: 'startDate',
			formatter: (row, column) => {
				return row.startDate ? dayjs(row.startDate).format('YYYY-MM-DD') : '';
			}
		},
		{
			title: '实际开始时间',
			key: 'assigbedpmTime',
			formatter: (row, column) => {
				return row.assigbedpmTime ? dayjs(row.assigbedpmTime).format('YYYY-MM-DD') : '';
			}
		},
		{
			title: '实际结束时间',
			key: 'endDate',
			formatter: (row, column) => {
				return row.endDate ? dayjs(row.endDate).format('YYYY-MM-DD') : '';
			}
		},
	],
	'审计通知': [
		{
			title: '审计通知书编号',
			key: 'advicecoed',
			clickable: true,
		},
		{
			title: '审计通知书名称',
			key: 'advicename'
		},
		{
			title: '审计实施时间',
			key: 'sjsstime'
		},
		{
			title: '组长',
			key: 'teamleader'
		},
		{
			title: '主审',
			key: 'mainreviewer'
		},
		{
			title: '助审',
			key: 'helpreviewer'
		},
		{
			title: '经办人',
			key: 'operator',
		},
		{
			title: '审计部（审计中心）主任审批意见',
			key: 'proposal',
		},
	],
	'通知变更': [
		{
			title: '单位（科室）',
			key: 'orgName',
			clickable: true,
		},
		{
			title: '审计项目名称',
			key: 'orgName',

		},
		{
			title: '变更事项',
			key: 'changething',
		},
		{
			title: '变更前内容',
			key: 'changebefore',
		},
		{
			title: '变更后内容',
			key: 'changeafter',
		},
		{
			title: '变更原因',
			key: 'changereason',
		},
		{
			title: '经办人',
			key: 'jbr',
		},
	],
	'审计承诺书': [
		{
			title: '编号',
			key: 'lettercode',
			clickable: true,
		},
		{
			title: '名称',
			key: 'lettername',

		},
		{
			title: '创建人',
			key: 'realname',
		},
		{
			title: '创建时间',
			key: 'createtime',
		},
	],
	'我的任务': [
		{
			title: '编号',
			key: 'lettercode',
			clickable: true,
		},
		{
			title: '名称',
			key: 'lettername',

		},
		{
			title: '创建人',
			key: 'realname',
		},
		{
			title: '创建时间',
			key: 'createtime',
		},
	],
	// '我的任务': [
	// 	{
	// 		title: '合同编号',
	// 		key: 'businessType',
	// 		clickable: true,
	// 	},
	// 	{
	// 		title: '工程名称',
	// 		key: 'riskPoint',

	// 	},
	// 	{
	// 		title: '施工单位',
	// 		key: 'suditProcess',
	// 	},
	// 	{
	// 		title: '二审审查金额（元）',
	// 		key: 'finishtime',
	// 	},
	// 	{
	// 		title: '本次审计人员',
	// 		key: 'finishtime1',
	// 	},
	// 	{
	// 		title: '审计专业科室人员',
	// 		key: 'finishtime2',
	// 	},
	// ],
	'我的底稿': [
		{
			title: '底稿编号',
			key: 'draftNumber',
			clickable: true,
		},
		{
			title: '底稿名称',
			key: 'draftName',

		},
		{
			title: '审计项目名称',
			key: 'projectName',
		},
		{
			title: '审计事项',
			key: 'auditMatters',
		},
		{
			title: '被审计单位名称',
			key: 'auditeeName',
		},
		{
			title: '创建时间',
			key: 'createTime',
		},
		{
			title: '状态',
			key: 'status',
			formatter: (row) => {
				let statusMap = {
					1: '审批中',
					2: '已退回',
					3: '已撤回',
					4: '已终止',
					5: '已跟踪',
					6: '已完成',
					// Map other status codes to your actual status names...
				};
				return statusMap[row.status] || '未审批';
			},
		},
	],
	'底稿管理': [
		{
			title: '底稿编号',
			key: 'draftNumber',
			clickable: true,
		},
		{
			title: '底稿名称',
			key: 'draftName',

		},
		{
			title: '审计项目名称',
			key: 'projectName',
		},
		{
			title: '审计事项',
			key: 'auditMatters',
		},
		{
			title: '被审计单位名称',
			key: 'auditeeName',
		},
		{
			title: '创建时间',
			key: 'createTime',
		},
		{
			title: '状态',
			key: 'status',
			formatter: (row) => {
				let statusMap = {
					1: '审批中',
					2: '已退回',
					3: '已撤回',
					4: '已终止',
					5: '已跟踪',
					6: '已完成',
					// Map other status codes to your actual status names...
				};
				return statusMap[row.status] || '未审批';
			},
		},
	],
	'审计结果确认单': [
		{
			title: '编号',
			key: 'resultcode',
			clickable: true,
		},
		{
			title: '审计项目名称',
			key: 'projectname',

		},
		{
			title: '被审计单位',
			key: 'orgidnames',
		},
		{
			title: '合同编号',
			key: 'contractcode',
		},
		{
			title: '合同名称',
			key: 'contractname',
		},
		{
			title: '施工单位',
			key: 'sgorgname',
		},
		{
			title: '合同金额(元)',
			key: 'contractmoney',
		},
		{
			title: '核增金额(元)',
			key: 'hzmoney',
		},
		{
			title: '核减金额(元)',
			key: 'hjmoney',
		},
		{
			title: '审计认定金额(元)',
			key: 'sdmoney',
		},
		{
			title: '状态',
			key: 'status',
			formatter: (row) => {
				let statusMap = {
					1: '审批中',
					2: '已退回',
					3: '已撤回',
					4: '已终止',
					5: '已跟踪',
					6: '已完成',
					// Map other status codes to your actual status names...
				};
				return statusMap[row.status] || '未审批';
			},
		},
	],
	'审计项目追款': [
		{
			title: '审计项目名称',
			key: 'projectName',
			clickable: true,
		},
		{
			title: '追款金额',
			key: 'money',

		},
		{
			title: '追款相对方',
			key: 'orgname',
			formatter: (row) => {
				return row.zkOrg.orgname
			},
		},
		{
			title: '备注',
			key: 'remark',
		},
		{
			title: '状态',
			key: 'status',
			formatter: (row) => {
				let statusMap = {
					1: '审批中',
					2: '已退回',
					3: '已撤回',
					4: '已终止',
					5: '已跟踪',
					6: '已完成',
					// Map other status codes to your actual status names...
				};
				return statusMap[row.status] || '未审批';
			},
		},
	],
	'审计工作记录': [
		{
			title: '索引号',
			key: 'indexno',
			clickable: true,
		},
		{
			title: '被审计单位名称',
			key: 'auditeeName',

		},
		{
			title: '实施审计时间',
			key: 'implementationTime',
		},
		{
			title: '审计项目名称',
			key: 'projectName',
		},
		{
			title: '审计内容和目标',
			key: 'contentObjectives',
		},
		{
			title: '执行的审计程序和工作过程',
			key: 'executedProceduresProcesses',
		},
		{
			title: '发现的疑点、线索及查证情况',
			key: 'verificationSituation',
		},
		{
			title: '审计线索及数据来源',
			key: 'cluesSources',
		},
		{
			title: '状态',
			key: 'status',
			formatter: (row) => {
				let statusMap = {
					1: '审批中',
					2: '已退回',
					3: '已撤回',
					4: '已终止',
					5: '已跟踪',
					6: '已完成',
					// Map other status codes to your actual status names...
				};
				return statusMap[row.status] || '未审批';
			},
		},
	],
	'审计发现': [
		{
			title: '底稿编号',
			key: 'draftNumber',
			clickable: true,
		},
		{
			title: '底稿名称',
			key: 'draftName',

		},
		{
			title: '审计项目名称',
			key: 'projectName',
		},
		{
			title: '审计事项',
			key: 'auditMatters',
		},
		{
			title: '被审计单位名称',
			key: 'auditeeName',
		},
		{
			title: '创建时间',
			key: 'createTime',
		},
		{
			title: '状态',
			key: 'status',
			formatter: (row) => {
				let statusMap = {
					1: '审批中',
					2: '已退回',
					3: '已撤回',
					4: '已终止',
					5: '已跟踪',
					6: '已完成',
					// Map other status codes to your actual status names...
				};
				return statusMap[row.status] || '未审批';
			},
		},
	],
	'审计督导任务': [
		{
			title: '项目名称',
			key: 'projectName',
			clickable: true,
		},
		{
			title: '主审',
			key: 'chiefAuditor',

		},
		{
			title: '参与督导人员',
			key: 'supervisionParticipants',
		},
		{
			title: '督导日期',
			key: 'supervisionDate',
		},
		{
			title: '现场情况',
			key: 'onsiteCondition',
		},
		{
			title: '存在问题及需协调解决的问题',
			key: 'issuesAndCoordination',
		},
		{
			title: '督导意见',
			key: 'supervisionOpinions',
		},
		{
			title: '备注',
			key: 'remarks',
		},
		{
			title: '状态',
			key: 'status',
			formatter: (row) => {
				let statusMap = {
					1: '审批中',
					2: '已退回',
					3: '已撤回',
					4: '已终止',
					5: '已跟踪',
					6: '已完成',
					// Map other status codes to your actual status names...
				};
				return statusMap[row.status] || '未审批';
			},
		},
	],
	'现场审查主要内容': [
		{
			title: '结算项目编号',
			key: 'settleProjectNum',
			clickable: true,
		},
		{
			title: '项目名称',
			key: 'projectName',

		},
		{
			title: '结算金额（元）',
			key: 'settleAmount',
		},
		{
			title: '建设单位',
			key: 'buildOrgName',
		},
		{
			title: '施工单位',
			key: 'constructionOrgName',
		},
		{
			title: '核实主要内容',
			key: 'reviewContent',
		},
		{
			title: '审计人员',
			key: 'reviewStaffName',
		},
		{
			title: '建设单位项目经理',
			key: 'buildUnitManageName',
		},
		{
			title: '现场审查时间',
			key: 'sceneReviewTime',
		},
		{
			title: '复审人员',
			key: 'recheckStaffName',
		},
		{
			title: '状态',
			key: 'status',
			formatter: (row) => {
				let statusMap = {
					1: '审批中',
					2: '已退回',
					3: '已撤回',
					4: '已终止',
					5: '已跟踪',
					6: '已完成',
					// Map other status codes to your actual status names...
				};
				return statusMap[row.status] || '未审批';
			},
		},
	],
	'审计项目运行情况表': [
		{
			title: '项目名称',
			key: 'name',
			clickable: true,
		},
		{
			title: '审计组',
			key: 'auditGroup',

		},
		{
			title: '实施单位',
			key: 'exePhraseUnit',
		},
		{
			title: '被审计单位',
			key: 'auditUnit',
		},
		{
			title: '组长',
			key: 'groupLeader',
		},
		{
			title: '主审',
			key: 'approver',
		},
		{
			title: '助审',
			key: 'assistApprover',
		},
		{
			title: '现场审计开始时间',
			key: 'sceneApproveStaerTime',
		},
		{
			title: '计划现场工作时间',
			key: 'planSceneApproveStaerTime',
		},
		{
			title: '计划现场结束日期',
			key: 'planSceneApproveEndTime',
		},
		{
			title: '实际现场结束日期',
			key: 'sceneApproveEndTime',
		},
		{
			title: '状态',
			key: 'status',
			formatter: (row) => {
				let statusMap = {
					1: '审批中',
					2: '已退回',
					3: '已撤回',
					4: '已终止',
					5: '已跟踪',
					6: '已完成',
					// Map other status codes to your actual status names...
				};
				return statusMap[row.status] || '未审批';
			},
		},
	],
	'质量分析报告': [
		{
			title: '文号',
			key: 'documentNumber',
			clickable: true,
		},
		{
			title: '标题',
			key: 'title',

		},
		{
			title: '状态',
			key: 'status',
			formatter: (row) => {
				let statusMap = {
					1: '审批中',
					2: '已退回',
					3: '已撤回',
					4: '已终止',
					5: '已跟踪',
					6: '已完成',
					// Map other status codes to your actual status names...
				};
				return statusMap[row.status] || '未审批';
			},
		},
	],
};

export default tableColumnsConfig;
