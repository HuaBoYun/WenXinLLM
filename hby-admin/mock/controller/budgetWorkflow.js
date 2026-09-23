/**
 * 预算工作流 Mock 接口
 */

let idCounter = 100

const mockWorkflows = [
  {
    workflowId: 'WF001',
    workflowCode: 'WF-DEPT-APPROVAL',
    workflowName: '部门预算审批流',
    workflowType: 'APPROVAL',
    workflowStatus: 'ACTIVE',
    version: 2,
    categoryId: 'APPROVAL',
    workflowDescription: '部门预算标准审批流程',
    nodeConfig:
      '[{"nodeName":"提交","nodeType":"START","assignee":"applicant","timeoutHours":24,"required":true},{"nodeName":"部门审核","nodeType":"USER_TASK","assignee":"manager","timeoutHours":48,"required":true},{"nodeName":"财务审批","nodeType":"USER_TASK","assignee":"finance","timeoutHours":72,"required":true}]',
    workflowDefinition: '{}',
    isEnabled: 1,
    allowParallel: 0,
    allowSkip: 0,
    autoStart: 0,
    notifyOnStart: 1,
    notifyOnComplete: 1,
    instanceCount: 45,
    avgDuration: 120,
    successRate: 95.5,
    createBy: '张三',
    createTime: '2026-01-01 09:00:00',
    updateBy: '张三',
    updateTime: '2026-03-15 10:00:00',
    delFlag: 0,
    tenantId: 'T001',
  },
  {
    workflowId: 'WF002',
    workflowCode: 'WF-GROUP-APPROVAL',
    workflowName: '集团预算审批流',
    workflowType: 'APPROVAL',
    workflowStatus: 'ACTIVE',
    version: 3,
    categoryId: 'APPROVAL',
    workflowDescription: '集团级预算审批流程',
    nodeConfig:
      '[{"nodeName":"提交","nodeType":"START","assignee":"applicant","timeoutHours":24,"required":true},{"nodeName":"部门审核","nodeType":"USER_TASK","assignee":"manager","timeoutHours":48,"required":true},{"nodeName":"CEO审批","nodeType":"USER_TASK","assignee":"ceo","timeoutHours":72,"required":true},{"nodeName":"董事会审批","nodeType":"USER_TASK","assignee":"board","timeoutHours":168,"required":true}]',
    workflowDefinition: '{}',
    isEnabled: 1,
    allowParallel: 0,
    allowSkip: 0,
    autoStart: 0,
    notifyOnStart: 1,
    notifyOnComplete: 1,
    instanceCount: 12,
    avgDuration: 480,
    successRate: 88.0,
    createBy: '张三',
    createTime: '2026-01-01 09:00:00',
    updateBy: '张三',
    updateTime: '2026-03-10 14:00:00',
    delFlag: 0,
    tenantId: 'T001',
  },
  {
    workflowId: 'WF003',
    workflowCode: 'WF-ADJ-APPROVAL',
    workflowName: '预算调整审批流',
    workflowType: 'APPROVAL',
    workflowStatus: 'ACTIVE',
    version: 1,
    categoryId: 'APPROVAL',
    workflowDescription: '预算调整审批流程',
    nodeConfig:
      '[{"nodeName":"申请","nodeType":"START","assignee":"applicant","timeoutHours":24,"required":true},{"nodeName":"财务审核","nodeType":"USER_TASK","assignee":"finance","timeoutHours":48,"required":true}]',
    workflowDefinition: '{}',
    isEnabled: 1,
    allowParallel: 1,
    allowSkip: 0,
    autoStart: 0,
    notifyOnStart: 0,
    notifyOnComplete: 1,
    instanceCount: 28,
    avgDuration: 60,
    successRate: 92.3,
    createBy: '李四',
    createTime: '2026-01-05 09:00:00',
    updateBy: '李四',
    updateTime: '2026-02-20 11:00:00',
    delFlag: 0,
    tenantId: 'T001',
  },
  {
    workflowId: 'WF004',
    workflowCode: 'WF-SUBMIT-NOTIFY',
    workflowName: '预算提交通知',
    workflowType: 'NOTIFICATION',
    workflowStatus: 'ACTIVE',
    version: 1,
    categoryId: 'NOTIFICATION',
    workflowDescription: '预算提交后通知相关人员',
    nodeConfig:
      '[{"nodeName":"通知","nodeType":"NOTIFICATION","assignee":"all","timeoutHours":1,"required":false}]',
    workflowDefinition: '{}',
    isEnabled: 1,
    allowParallel: 1,
    allowSkip: 1,
    autoStart: 1,
    notifyOnStart: 0,
    notifyOnComplete: 0,
    instanceCount: 156,
    avgDuration: 1,
    successRate: 99.8,
    createBy: '王五',
    createTime: '2026-01-03 09:00:00',
    updateBy: '王五',
    updateTime: '2026-01-03 09:00:00',
    delFlag: 0,
    tenantId: 'T001',
  },
  {
    workflowId: 'WF005',
    workflowCode: 'WF-DATA-SYNC',
    workflowName: '数据同步自动化',
    workflowType: 'AUTOMATION',
    workflowStatus: 'ACTIVE',
    version: 2,
    categoryId: 'AUTOMATION',
    workflowDescription: '每日凌晨2点同步ERP数据',
    nodeConfig:
      '[{"nodeName":"数据同步","nodeType":"SERVICE_TASK","assignee":"system","timeoutHours":2,"required":true}]',
    workflowDefinition: '{}',
    isEnabled: 1,
    allowParallel: 0,
    allowSkip: 0,
    autoStart: 1,
    notifyOnStart: 0,
    notifyOnComplete: 1,
    instanceCount: 365,
    avgDuration: 15,
    successRate: 97.2,
    createBy: '郑十',
    createTime: '2026-01-05 09:00:00',
    updateBy: '郑十',
    updateTime: '2026-03-01 09:00:00',
    delFlag: 0,
    tenantId: 'T001',
  },
  {
    workflowId: 'WF006',
    workflowCode: 'WF-QUICK-APPROVAL',
    workflowName: '快速审批流',
    workflowType: 'APPROVAL',
    workflowStatus: 'DRAFT',
    version: 1,
    categoryId: 'APPROVAL',
    workflowDescription: '金额10万以下快速审批',
    nodeConfig:
      '[{"nodeName":"提交","nodeType":"START","assignee":"applicant","timeoutHours":12,"required":true},{"nodeName":"经理审批","nodeType":"USER_TASK","assignee":"manager","timeoutHours":24,"required":true}]',
    workflowDefinition: '{}',
    isEnabled: 0,
    allowParallel: 0,
    allowSkip: 1,
    autoStart: 0,
    notifyOnStart: 0,
    notifyOnComplete: 1,
    instanceCount: 0,
    avgDuration: 0,
    successRate: 0,
    createBy: '张三',
    createTime: '2026-03-20 09:00:00',
    updateBy: '张三',
    updateTime: '2026-03-20 09:00:00',
    delFlag: 0,
    tenantId: 'T001',
  },
  {
    workflowId: 'WF007',
    workflowCode: 'WF-REPORT-GEN',
    workflowName: '报表自动生成',
    workflowType: 'AUTOMATION',
    workflowStatus: 'PUBLISHED',
    version: 1,
    categoryId: 'AUTOMATION',
    workflowDescription: '每月1日自动生成报表',
    nodeConfig:
      '[{"nodeName":"生成报表","nodeType":"SERVICE_TASK","assignee":"system","timeoutHours":4,"required":true}]',
    workflowDefinition: '{}',
    isEnabled: 1,
    allowParallel: 0,
    allowSkip: 0,
    autoStart: 1,
    notifyOnStart: 1,
    notifyOnComplete: 1,
    instanceCount: 3,
    avgDuration: 30,
    successRate: 100,
    createBy: '李四',
    createTime: '2026-02-01 09:00:00',
    updateBy: '李四',
    updateTime: '2026-03-01 09:00:00',
    delFlag: 0,
    tenantId: 'T001',
  },
  {
    workflowId: 'WF008',
    workflowCode: 'WF-DISABLED-FLOW',
    workflowName: '已停用流程',
    workflowType: 'APPROVAL',
    workflowStatus: 'SUSPENDED',
    version: 1,
    categoryId: 'APPROVAL',
    workflowDescription: '已停用的旧版审批流',
    nodeConfig: '[]',
    workflowDefinition: '{}',
    isEnabled: 0,
    allowParallel: 0,
    allowSkip: 0,
    autoStart: 0,
    notifyOnStart: 0,
    notifyOnComplete: 0,
    instanceCount: 200,
    avgDuration: 240,
    successRate: 78.5,
    createBy: '张三',
    createTime: '2025-01-01 09:00:00',
    updateBy: '张三',
    updateTime: '2025-12-01 09:00:00',
    delFlag: 0,
    tenantId: 'T001',
  },
]

function getActiveList() {
  return mockWorkflows.filter((w) => w.delFlag === 0)
}

module.exports = [
  // 分页查询
  {
    url: '/glkj/accountant/budget/workflow/page',
    type: 'post',
    response(req) {
      const {
        pageNum = 1,
        pageSize = 20,
        workflowName,
        workflowType,
        workflowStatus,
      } = req.body || {}
      let list = getActiveList()
      if (workflowName)
        list = list.filter((w) => w.workflowName.includes(workflowName))
      if (workflowType)
        list = list.filter((w) => w.workflowType === workflowType)
      if (workflowStatus)
        list = list.filter((w) => w.workflowStatus === workflowStatus)
      const start = (pageNum - 1) * pageSize
      return {
        code: 1,
        msg: '查询成功',
        data: {
          records: list.slice(start, start + pageSize),
          total: list.length,
          pageNum,
          pageSize,
        },
      }
    },
  },
  // 创建
  {
    url: '/glkj/accountant/budget/workflow/create',
    type: 'post',
    response(req) {
      const data = req.body || {}
      const newItem = {
        ...data,
        workflowId: 'WF' + String(++idCounter),
        version: 1,
        workflowStatus: data.workflowStatus || 'DRAFT',
        instanceCount: 0,
        avgDuration: 0,
        successRate: 0,
        createBy: 'admin',
        createTime: new Date().toISOString(),
        updateBy: 'admin',
        updateTime: new Date().toISOString(),
        delFlag: 0,
        tenantId: 'T001',
      }
      mockWorkflows.push(newItem)
      return { code: 1, msg: '创建成功', data: newItem }
    },
  },
  // 更新
  {
    url: '/glkj/accountant/budget/workflow/update/:id',
    type: 'put',
    response(req) {
      const id = req.params.id
      const idx = mockWorkflows.findIndex((w) => w.workflowId === id)
      if (idx === -1) return { code: 0, msg: '工作流不存在' }
      Object.assign(mockWorkflows[idx], req.body, {
        updateTime: new Date().toISOString(),
      })
      return { code: 1, msg: '更新成功', data: mockWorkflows[idx] }
    },
  },
  // 删除
  {
    url: '/glkj/accountant/budget/workflow/delete/:id',
    type: 'delete',
    response(req) {
      const id = req.params.id
      const item = mockWorkflows.find((w) => w.workflowId === id)
      if (!item) return { code: 0, msg: '工作流不存在' }
      item.delFlag = 1
      return { code: 1, msg: '删除成功' }
    },
  },
  // 统计
  {
    url: '/glkj/accountant/budget/workflow/stats',
    type: 'get',
    response() {
      const list = getActiveList()
      const active = list.filter((w) => w.workflowStatus === 'ACTIVE').length
      const draft = list.filter((w) => w.workflowStatus === 'DRAFT').length
      const total = list.length
      return {
        code: 1,
        msg: '查询成功',
        data: {
          totalWorkflows: total,
          activeWorkflows: active,
          draftWorkflows: draft,
          runningInstances: 0,
          activeRate: total > 0 ? Math.round((active * 1000) / total) / 10 : 0,
          draftRate: total > 0 ? Math.round((draft * 1000) / total) / 10 : 0,
          instanceRate: 0,
        },
      }
    },
  },
  // 分类树
  {
    url: '/glkj/accountant/budget/workflow/category/tree',
    type: 'get',
    response() {
      return {
        code: 1,
        msg: '查询成功',
        data: [
          { id: 'APPROVAL', name: '审批流程', children: [] },
          { id: 'NOTIFICATION', name: '通知流程', children: [] },
          { id: 'AUTOMATION', name: '自动化流程', children: [] },
        ],
      }
    },
  },
  // 分类列表
  {
    url: '/glkj/accountant/budget/workflow/categories',
    type: 'get',
    response() {
      return {
        code: 1,
        msg: '查询成功',
        data: [
          { id: 'APPROVAL', name: '审批流程' },
          { id: 'NOTIFICATION', name: '通知流程' },
          { id: 'AUTOMATION', name: '自动化流程' },
        ],
      }
    },
  },
  // 部署
  {
    url: '/glkj/accountant/budget/workflow/:id/deploy',
    type: 'post',
    response(req) {
      const id = req.params.id
      const item = mockWorkflows.find((w) => w.workflowId === id)
      if (!item) return { code: 0, msg: '工作流不存在' }
      item.workflowStatus = 'ACTIVE'
      item.isEnabled = 1
      return { code: 1, msg: '部署成功' }
    },
  },
  // 批量部署
  {
    url: '/glkj/accountant/budget/workflow/batch-deploy',
    type: 'post',
    response(req) {
      const ids = req.body || []
      ids.forEach((id) => {
        const item = mockWorkflows.find((w) => w.workflowId === id)
        if (item) {
          item.workflowStatus = 'ACTIVE'
          item.isEnabled = 1
        }
      })
      return { code: 1, msg: '批量部署成功' }
    },
  },
]
