import * as dayjs from 'dayjs'

//项目查看
export const projectFormOption = [
  {
    label: '密级',
    prop: 'secrectLevelId',
    type: 'select',
  },
  {
    label: '知悉范围',
    prop: 'staffScopeNames',
    type: 'input',
  },
  {
    label: '项目名称',
    prop: 'prjoectName',
    type: 'input',
  },
  {
    label: '被审计单位',
    prop: 'orgIdNames',
    type: 'input',
  },
  {
    label: '项目主审',
    prop: 'realname',
    type: 'input',
  },
  {
    label: '审计模式',
    prop: 'auditType',
    type: 'input',
  },
  {
    label: '项目开始时间',
    prop: 'createTime',
    type: 'date',
  },
  {
    label: '项目实际投入人员',
    prop: 'sjtrry',
    type: 'input',
  },
  {
    label: '检查清单总数',
    prop: 'jcqd',
    type: 'input',
  },
  {
    label: '发现问题总数',
    prop: 'fawt',
    type: 'input',
  },
]
// 审计指引
export const auditingTableOption = [
  { label: '问题单元', prop: 'businessType' },
  { label: '审计问题', prop: 'riskPoint' },
  { label: '重点关注事项', prop: 'control' },
  { label: '审计程序', prop: 'suditProcess' },
  { label: '所需资料', prop: 'bioData' },
]
//项目方案
export const projectPlanFormOptions = [
  {
    label: '所属审计计划',
    prop: 'planname',
    type: 'input',
    labelWidth: '150px',
    col: 12,
  },
  {
    label: '计划项目',
    prop: 'pprojectName',
    type: 'input',
    labelWidth: '150px',
    col: 12,
  },
  {
    label: '项目编号',
    prop: 'projectCode',
    labelWidth: '150px',
    type: 'input',
    col: 12,
  },
  {
    label: '项目名称',
    prop: 'prjoectName',
    labelWidth: '150px',
    type: 'input',
    col: 12,
  },
  {
    label: '工作目标',
    prop: 'targetName',
    labelWidth: '150px',
    type: 'input',
    col: 12,
  },
  {
    label: '被审计单位',
    prop: 'orgIdNames',
    labelWidth: '150px',
    type: 'input',
    col: 12,
  },
  {
    label: '计划年度',
    prop: 'planYear',
    type: 'input',
    labelWidth: '150px',
    col: 12,
  },
  {
    label: '审计类型',
    prop: 'auditType',
    labelWidth: '150px',
    type: 'select',
    col: 12,
  },
  {
    label: '项目来源',
    prop: 'projectSource',
    labelWidth: '150px',
    type: 'input',
    col: 12,
  },
  {
    label: '项目主审',
    prop: 'realName',
    labelWidth: '150px',
    type: 'input',
    col: 12,
  },
  {
    label: '计划审计时间',
    prop: 'planStartDate',
    labelWidth: '150px',
    type: 'input',
    col: 12,
  },
  {
    label: '计划验收时间',
    prop: 'planEndDate',
    labelWidth: '150px',
    type: 'input',
    col: 12,
  },
  {
    label: '审计模板',
    prop: 'templeteName',
    labelWidth: '150px',
    type: 'input',
    col: 12,
  },
  {
    label: '批复总投资(经费:万元)',
    prop: 'costs',
    labelWidth: '150px',
    type: 'input',
    col: 12,
  },
  {
    label: '审计方式',
    prop: 'proSjfs',
    labelWidth: '150px',
    type: 'input',
    col: 12,
  },
  {
    label: '是否外委',
    prop: 'externAlassig',
    labelWidth: '150px',
    type: 'input',
    col: 12,
  },
  {
    label: '项目单位',
    prop: 'projectorgname',
    labelWidth: '150px',
    type: 'input',
    col: 12,
  },
  {
    label: '批复的项目起止年限',
    prop: 'appproyear',
    labelWidth: '150px',
    type: 'input',
    col: 12,
  },
  {
    label: '实际的起止年限',
    prop: 'actproyear',
    labelWidth: '150px',
    type: 'input',
    col: 12,
  },
  {
    label: '项目主管部门',
    prop: 'projectmgdeptname',
    labelWidth: '150px',
    type: 'input',
    col: 12,
  },
  {
    label: '项目单位地址',
    prop: 'projectorgaddress',
    labelWidth: '150px',
    type: 'input',
    col: 12,
  },
  {
    label: '项目联系人',
    prop: 'projectlinkman',
    labelWidth: '150px',
    type: 'input',
    col: 12,
  },
  {
    label: '联系电话',
    prop: 'projectlinktel',
    labelWidth: '150px',
    type: 'input',
    col: 12,
  },
  {
    label: '项目类型',
    prop: 'projecttype',
    labelWidth: '150px',
    type: 'input',
    col: 12,
  },
  {
    label: '审计目标和范围',
    prop: 'purpose',
    labelWidth: '150px',
    type: 'textarea',
    col: 24,
  },
  {
    label: '审计内容和重点',
    prop: 'scopes',
    labelWidth: '150px',
    type: 'textarea',
    col: 24,
  },
  {
    label: '审计程序和方法',
    prop: 'pursuant',
    labelWidth: '150px',
    type: 'textarea',
    col: 24,
  },
  {
    label: '对专家和外部审计结果的利用',
    prop: 'comments',
    labelWidth: '150px',
    type: 'textarea',
    col: 24,
  },
  {
    label: '备注',
    prop: 'proDesc',
    labelWidth: '150px',
    type: 'textarea',
    col: 24,
  },
]
// 项目小组
export const projectPlanTable = [
  { label: '小组名称', prop: 'teamName' },
  { label: '组长', prop: 'leaderName' },
  { label: '组员', prop: 'zyNames' },
]
// 附件列表
export const projectPlanFileTable = [
  { label: '附件名称', prop: 'attname' },
  { label: '文件大小（KB）', prop: 'attsize' },
  { label: '创建人', prop: 'uploader' },
]
// 审计通知书
export const auditNotice = [
  { label: '审计通知书编号', prop: 'advicecoed' },
  { label: '审计通知书名称', prop: 'advicename' },
  { label: '创建人', prop: 'tblCreater.realname' },
  { label: '创建时间', prop: 'creatrtime' },
  // {
  //   label: '状态', prop: 'status', formatter: (e) => {
  //     return e.status == '1' ? '是' : '否'
  //   },
  // },
]
//进场纪要
export const inTheMinutes = [
  { label: '进场纪要编号', prop: 'entercoed' },
  { label: '进场纪要名称', prop: 'entername' },
  { label: '创建人', prop: 'createstaffid' },
  { label: '创建时间', prop: 'creatrtime' },
  { label: '创建时间', prop: 'creatrtime' },
  {
    label: '状态',
    prop: 'status',
    formatter: (e) => {
      return e.status == '1' ? '已作废' : '正常'
    },
  },
]
// 任务查看
export const taskView = [
  { label: '问题单元', prop: 'businessType' },
  { label: '审计问题', prop: 'riskPoint' },
  { label: '重点关注事项', prop: 'control' },
  { label: '审计程序', prop: 'suditProcess' },
  {
    label: '完成时间', prop: 'finishtime', formatter: (e) => {
      if (e.finishtime) {
        return dayjs(e.finishtime).format('YYYY-MM-DD')
      } else {
        return ''
      }
    },
  },

  {
    label: '状态',
    prop: 'finish',
    formatter: (e) => {
      return e.finish == '1' ? '已完成' : '未完成'
    },
  },
  { label: '操作', prop: 'action', hasActionTwo: true },
]
// 工作底稿
export const workingPapers = [
  { label: '底稿编号', prop: 'sheetCode' },
  { label: '底稿名称', prop: 'sheetName' },
  // { label: '审计目标', prop: 'sheetTarget' },
  { label: '被审计对象', prop: 'orgIdNames' },
  { label: '拟稿人', prop: 'realname' },
  { label: '审批人', prop: 'approver' },
  {
    label: '拟稿日期',
    prop: 'createTime',
    formatter: (e) => {
      return dayjs(e.createtime).format('YYYY-MM-DD')
    },
  },

  {
    label: '状态',
    prop: 'state',
    formatter: (e) => {
      return e.state == '1'
        ? '审批中'
        : e.state == '2'
          ? '已退回'
          : e.state == '3'
            ? '已通过'
            : e.state == '6'
              ? '已完成'
              : '未审批'

    },
  },
  { label: '操作', prop: 'action', hasAction: true, report: '底稿管理' },

]
// 审计发现
export const auditFound = [
  { label: '底稿编号', prop: 'sheetcode' },
  { label: '被审计对象', prop: 'orgidnames' },
  { label: '审计发现', prop: 'auditDiscoverable' },

  { label: '发现人', prop: 'realname' },
  { label: '审计事项', prop: 'businessaffiliation' },
]
// 事实确认书
export const confirmationOfFact = [
  { label: '编号', prop: 'factcode' },
  {
    label: '所属计划',
    prop: 'factcode',
    // formatter: (e) => {
    //   return e.factcode
    // },
  },
  { label: '拟稿人', prop: 'createstaffname' },
  { label: '事实描述', prop: 'describe' },
  {
    label: '审批状态',
    prop: 'status',
    formatter: (e) => {
      return e.status == '2'
        ? '审批中'
        : e.status == '3'
          ? '需调整'
          : e.status == '4'
            ? '已终止'
            : '已完成'
    },
  },
  {
    label: '拟稿时间',
    prop: 'createtime',
    formatter: (e) => {
      return dayjs(e.createtime).format('YYYY-MM-DD')
    },
  },
]
// 离场纪要
export const leaveTheMinutes = [
  { label: '离场纪要编号', prop: 'leavecoed' },
  { label: '离场纪要名称', prop: 'leavename' },
  { label: '创建人', prop: 'createstaffid' },
  {
    label: '创建时间',
    prop: 'creatrtime',
    formatter: (e) => {
      return dayjs(e.createtime).format('YYYY-MM-DD')
    },
  },
  {
    label: '状态',
    prop: 'status',
    formatter: (e) => {
      return e.status == '1' ? '已作废' : '正常'
    },
  },
]

// 审计报告
export const auditReport = [
  { label: '报告名称', prop: 'reportname' },
  {
    label: '报告时间',
    prop: 'reporttime',
    formatter: (e) => {
      return dayjs(e.reporttime).format('YYYY-MM-DD')
    },
  },
  { label: '报告版本', prop: 'reporttype' },
  { label: '报告方式', prop: 'reportmode' },
  {
    label: '状态',
    prop: 'reportstatus',
    formatter: (e) => {
      return e.reportstatus == 1
        ? '审批中'
        : e.reportstatus == 2
          ? '已退回'
          : e.reportstatus == 3
            ? '已撤回'
            : e.reportstatus == 4
              ? '已终止'
              : e.reportstatus == 5
                ? '已跟踪'
                : e.reportstatus == 6
                  ? '已完成'
                  : '未审批'
    },
  },
  { label: '操作', prop: 'action', hasAction: true, report: '审计报告' },
]

// 审计建议书
export const auditProposal = [
  { label: '建议书编号', prop: 'procode' },
  { label: '建议书名称', prop: 'proname' },
  { label: '创建人', prop: 'tblStaff.realname' },
  {
    label: '创建时间',
    prop: 'createTime',
    formatter: (e) => {
      return dayjs(e.reporttime).format('YYYY-MM-DD')
    },
  },
  {
    label: '状态',
    prop: 'status',
    formatter: (e) => {
      return e.status == 1 ? '已作废' : '使用'
    },
  },
]
// 审计确认书
export const qZDPapers = [
  { label: '项目名称', prop: 'prjoectname' },
  { label: '审计事项', prop: 'auditMatter' },
  { label: '审计事项摘要', prop: 'auditAbstract' },
  { label: '证据提供单位意见', prop: 'evidenceOpinion' },
  { label: '证据提供者', prop: 'certificateUser' },
  {
    label: '日期',
    prop: 'createDate',
    formatter: (e) => {
      return e.createDate ? dayjs(e.createDate).format('YYYY-MM-DD') : ''
    },
  },
  {
    label: '状态',
    prop: 'status',
    formatter: (e) => {
      return e.status == 1
        ? '审批中'
        : e.status == 2
          ? '已退回'
          : e.status == 3
            ? '已撤回'
            : e.status == 4
              ? '已终止'
              : e.status == 5
                ? '已跟踪'
                : e.status == 6
                  ? '已完成'
                  : '未审批'
    },
  },
  { label: '操作', prop: 'action', hasAction: true, report: '审计取证单' },
]
// 项目资料
export const projects = [
  { label: '资料编号', prop: 'projectDatapreId' },
  { label: '资料名称', prop: 'dataName' },
  { label: '所属项目', prop: 'projectname' },
  { label: '创建人', prop: 'username' },
  {
    label: '创建时间',
    prop: 'dataDate',
    formatter: (e) => {
      return dayjs(e.dataDate).format('YYYY-MM-DD')
    },
  },
]
// 工作小结
export const workSummary = [
  { label: '日志名称', prop: 'reportname' },
  { label: '报告类型', prop: 'reporttype' },
  { label: '报告人', prop: 'reporter.realname' },
  { label: '报告部门', prop: 'reportdepartment.orgname' },
  {
    label: '日志时间',
    prop: 'reporttime',
    formatter: (e) => {
      return dayjs(e.reporttime).format('YYYY-MM-DD')
    },
  },
]
//疑点管理
export const doubtManagement = [
  { label: '疑点编号', prop: 'dpnumber' },
  { label: '疑点名称', prop: 'dpname' },
  { label: '疑点描述', prop: 'dpdescribe' },
  { label: '测试结果', prop: 'testresult' },
  { label: '编制人', prop: 'editor' },
]
