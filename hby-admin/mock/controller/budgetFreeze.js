/**
 * 预算冻结 Mock 接口
 */

let idCounter = 20
let historyIdCounter = 100
const mockHistories = {}

const organizationOptions = [
  { id: 'ORG001', name: '集团总部', code: 'HQ' },
  { id: 'ORG015', name: '财务共享中心', code: 'FIN015' },
  { id: 'ORG020', name: '研发中心', code: 'RD020' },
]

const budgetAccountOptions = [
  { id: 'A001', name: '办公经费', code: 'OFFICE001' },
  { id: 'A004', name: '研发费用', code: 'RD004' },
  { id: 'A008', name: '市场推广费', code: 'MKT008' },
]

const userOptions = [
  { id: 1001, name: '张主任' },
  { id: 1655, name: '李经理' },
  { id: 2008, name: '王总监' },
]

const mockFreezes = [
  {
    freezeId: 'FRZ020',
    freezeCode: 'FRZ-2026-020',
    freezeTitle: '研发专项预算冻结',
    freezeType: 'PARTIAL',
    freezeAmount: 120000,
    organizationId: 'ORG020',
    organizationName: '研发中心',
    budgetAccountId: 'A004',
    budgetAccountName: '研发费用',
    freezeReason: '项目资源重新分配',
    freezeDescription: '阶段性冻结部分研发预算',
    freezeStatus: 'FROZEN',
    approvalStatus: 'APPROVED',
    applicant: '张三',
    approver: 1655,
    applyDate: '2026-04-06',
    freezeDate: '2026-04-07',
    plannedUnfreezeDate: '2026-04-30',
    urgencyLevel: 'MEDIUM',
    notificationMethod: 'SYSTEM',
    autoApprove: false,
    allowPartialUnfreeze: true,
    sendNotification: true,
    delFlag: 0,
  },
  {
    freezeId: 'FRZ019',
    freezeCode: 'FRZ-2026-019',
    freezeTitle: '财务审核期间预算冻结',
    freezeType: 'FULL',
    freezeAmount: 88000,
    organizationId: 'ORG015',
    organizationName: '财务共享中心',
    budgetAccountId: 'A001',
    budgetAccountName: '办公经费',
    freezeReason: '审计临时管控',
    freezeDescription: '审计期间暂停预算使用',
    freezeStatus: 'FROZEN',
    approvalStatus: 'PENDING',
    applicant: '李四',
    approver: 1655,
    applyDate: '2026-04-05',
    freezeDate: '2026-04-06',
    plannedUnfreezeDate: '2026-04-29',
    urgencyLevel: 'HIGH',
    notificationMethod: 'SYSTEM',
    autoApprove: false,
    allowPartialUnfreeze: false,
    sendNotification: true,
    delFlag: 0,
  },
  {
    freezeId: 'FRZ018',
    freezeCode: 'FRZ-2026-018',
    freezeTitle: '市场活动预算部分冻结',
    freezeType: 'PARTIAL',
    freezeAmount: 56000,
    organizationId: 'ORG001',
    organizationName: '集团总部',
    budgetAccountId: 'A008',
    budgetAccountName: '市场推广费',
    freezeReason: '活动延期',
    freezeDescription: '市场活动顺延，先冻结预算',
    freezeStatus: 'RELEASED',
    approvalStatus: 'APPROVED',
    applicant: '王五',
    approver: 2008,
    applyDate: '2026-03-28',
    freezeDate: '2026-03-29',
    plannedUnfreezeDate: '2026-04-10',
    urgencyLevel: 'LOW',
    notificationMethod: 'EMAIL',
    autoApprove: true,
    allowPartialUnfreeze: true,
    sendNotification: false,
    delFlag: 0,
  },
]

const getActiveList = () => mockFreezes.filter((item) => item.delFlag === 0)
const formatDate = (value) => {
  if (!value) return ''
  const date = new Date(value)
  if (Number.isNaN(date.getTime())) return String(value).slice(0, 10)
  return date.toISOString().slice(0, 10)
}
const calcStats = () => {
  const list = getActiveList()
  const frozenList = list.filter((item) => item.freezeStatus === 'FROZEN')
  const pendingList = list.filter((item) => item.approvalStatus === 'PENDING')
  const frozenAmount = frozenList.reduce(
    (sum, item) => sum + (item.freezeAmount || 0),
    0
  )
  return {
    totalCount: list.length,
    activeCount: frozenList.length,
    pendingCount: pendingList.length,
    frozenAmount,
    frozenRate: list.length
      ? Math.round((frozenList.length / list.length) * 1000) / 10
      : 0,
  }
}

module.exports = [
  {
    url: '/glkj/accountant/budget/freeze/page',
    type: 'post',
    response(req) {
      const {
        pageNum = 1,
        pageSize = 20,
        freezeTitle,
        freezeType,
        freezeStatus,
        budgetAccount,
        organizationPath,
      } = req.body || {}
      let list = getActiveList()
      if (freezeTitle)
        list = list.filter((item) => item.freezeTitle.includes(freezeTitle))
      if (freezeType)
        list = list.filter((item) => item.freezeType === freezeType)
      if (freezeStatus)
        list = list.filter((item) => item.freezeStatus === freezeStatus)
      if (budgetAccount)
        list = list.filter((item) => item.budgetAccountId === budgetAccount)
      if (organizationPath)
        list = list.filter((item) => item.organizationId === organizationPath)
      const start = (pageNum - 1) * pageSize
      return {
        code: 1,
        msg: '查询成功',
        data: {
          tlist: list.slice(start, start + pageSize),
          totalRecord: list.length,
          pageNum,
          pageSize,
        },
      }
    },
  },
  {
    url: '/glkj/accountant/budget/freeze/statistics',
    type: 'get',
    response() {
      return { code: 1, msg: '查询成功', data: calcStats() }
    },
  },
  {
    url: '/glkj/accountant/budget/freeze/organizations',
    type: 'get',
    response() {
      return { code: 1, msg: '查询成功', data: organizationOptions }
    },
  },
  {
    url: '/glkj/accountant/budget/freeze/accounts',
    type: 'get',
    response() {
      return { code: 1, msg: '查询成功', data: budgetAccountOptions }
    },
  },
  {
    url: '/glkj/accountant/budget/freeze/users',
    type: 'get',
    response() {
      return { code: 1, msg: '查询成功', data: userOptions }
    },
  },
  {
    url: '/glkj/accountant/budget/freeze/detail/:id',
    type: 'get',
    response(req) {
      const item = mockFreezes.find(
        (row) => row.freezeId === req.params.id && row.delFlag === 0
      )
      return item
        ? { code: 1, msg: '查询成功', data: item }
        : { code: 0, msg: '冻结记录不存在', data: null }
    },
  },
  {
    url: '/glkj/accountant/budget/freeze/create',
    type: 'post',
    response(req) {
      const data = req.body || {}
      const newItem = {
        ...data,
        freezeId: `FRZ${String(++idCounter).padStart(3, '0')}`,
        freezeCode: data.freezeCode || `FRZ-${Date.now()}`,
        organizationName:
          organizationOptions.find((item) => item.id === data.organizationId)
            ?.name || '',
        budgetAccountName:
          budgetAccountOptions.find((item) => item.id === data.budgetAccountId)
            ?.name || '',
        applicant: '当前用户',
        applyDate: formatDate(data.applyDate),
        freezeDate: formatDate(data.applyDate),
        plannedUnfreezeDate: formatDate(data.plannedUnfreezeDate),
        freezeStatus: 'FROZEN',
        approvalStatus: data.autoApprove ? 'APPROVED' : 'PENDING',
        delFlag: 0,
      }
      mockFreezes.unshift(newItem)
      mockHistories[newItem.freezeId] = [
        {
          historyId: `H${++historyIdCounter}`,
          freezeId: newItem.freezeId,
          operationType: 'CREATE',
          operationDesc: '创建预算冻结：' + (newItem.freezeTitle || ''),
          amount: newItem.freezeAmount,
          beforeValue: null,
          afterValue: 'FROZEN',
          operator: '当前用户',
          operateTime: new Date().toISOString().replace('T', ' ').slice(0, 19),
          createTime: new Date().toISOString().replace('T', ' ').slice(0, 19),
        },
      ]
      return { code: 1, msg: '创建成功', data: newItem }
    },
  },
  {
    url: '/glkj/accountant/budget/freeze/update/:id',
    type: 'put',
    response(req) {
      const item = mockFreezes.find(
        (row) => row.freezeId === req.params.id && row.delFlag === 0
      )
      if (!item) return { code: 0, msg: '冻结记录不存在' }
      Object.assign(item, req.body || {}, {
        applyDate: formatDate(req.body?.applyDate || item.applyDate),
        plannedUnfreezeDate: formatDate(
          req.body?.plannedUnfreezeDate || item.plannedUnfreezeDate
        ),
      })
      return { code: 1, msg: '更新成功', data: item }
    },
  },
  {
    url: '/glkj/accountant/budget/freeze/delete/:id',
    type: 'delete',
    response(req) {
      const item = mockFreezes.find(
        (row) => row.freezeId === req.params.id && row.delFlag === 0
      )
      if (!item) return { code: 0, msg: '冻结记录不存在' }
      item.delFlag = 1
      return { code: 1, msg: '删除成功' }
    },
  },
  {
    url: '/glkj/accountant/budget/freeze/unfreeze/:id',
    type: 'post',
    response(req) {
      const item = mockFreezes.find(
        (row) => row.freezeId === req.params.id && row.delFlag === 0
      )
      if (!item) return { code: 0, msg: '冻结记录不存在' }
      const oldStatus = item.freezeStatus
      item.freezeStatus = 'RELEASED'
      if (!mockHistories[req.params.id]) mockHistories[req.params.id] = []
      mockHistories[req.params.id].unshift({
        historyId: `H${++historyIdCounter}`,
        freezeId: req.params.id,
        operationType: 'UNFREEZE',
        operationDesc: '解冻操作',
        amount: item.freezeAmount,
        beforeValue: oldStatus,
        afterValue: 'RELEASED',
        operator: '当前用户',
        operateTime: new Date().toISOString().replace('T', ' ').slice(0, 19),
        createTime: new Date().toISOString().replace('T', ' ').slice(0, 19),
      })
      return { code: 1, msg: '解冻成功' }
    },
  },
  {
    url: '/glkj/accountant/budget/freeze/execute/:id',
    type: 'post',
    response(req) {
      const item = mockFreezes.find(
        (row) => row.freezeId === req.params.id && row.delFlag === 0
      )
      if (!item) return { code: 0, msg: '冻结记录不存在' }
      item.freezeStatus = 'FROZEN'
      return { code: 1, msg: '冻结成功' }
    },
  },
  {
    url: '/glkj/accountant/budget/freeze/batch/freeze',
    type: 'post',
    response(req) {
      const ids = req.body || []
      ids.forEach((id) => {
        const item = mockFreezes.find((row) => row.freezeId === id)
        if (item) item.freezeStatus = 'FROZEN'
      })
      return {
        code: 1,
        msg: '批量冻结成功',
        data: {
          totalCount: ids.length,
          successCount: ids.length,
          failCount: 0,
        },
      }
    },
  },
  {
    url: '/glkj/accountant/budget/freeze/batch/unfreeze',
    type: 'post',
    response(req) {
      const body = req.body || {}
      const ids = body.freezeIds || (Array.isArray(body) ? body : [])
      ids.forEach((id) => {
        const item = mockFreezes.find((row) => row.freezeId === id)
        if (item) item.freezeStatus = 'RELEASED'
      })
      return {
        code: 1,
        msg: '批量解冻成功',
        data: {
          totalCount: ids.length,
          successCount: ids.length,
          failCount: 0,
        },
      }
    },
  },
  {
    url: '/glkj/accountant/budget/freeze/validate',
    type: 'post',
    response(req) {
      const { freezeAmount = 0 } = req.body || {}
      return {
        code: 1,
        msg: '验证成功',
        data: {
          availableAmount: 500000,
          requestAmount: freezeAmount,
          valid: freezeAmount <= 500000,
          message:
            freezeAmount <= 500000 ? '可冻结金额充足' : '冻结金额超出可用额度',
        },
      }
    },
  },
  {
    url: '/glkj/accountant/budget/freeze/import',
    type: 'post',
    response() {
      return {
        code: 1,
        msg: '导入成功',
        data: { successCount: 0, failCount: 0 },
      }
    },
  },
  {
    url: '/glkj/accountant/budget/freeze/export',
    type: 'post',
    response() {
      return { code: 1, msg: '导出成功' }
    },
  },
  {
    url: '/glkj/accountant/budget/freeze/export/:id',
    type: 'get',
    response() {
      return { code: 1, msg: '导出成功' }
    },
  },
  {
    url: '/glkj/accountant/budget/freeze/history/:id',
    type: 'get',
    response(req) {
      const freezeId = req.params.id
      const list = mockHistories[freezeId] || []
      return { code: 1, msg: '查询成功', data: list }
    },
  },
  {
    url: '/glkj/accountant/budget/freeze/extend/:id',
    type: 'post',
    response(req) {
      const freezeId = req.params.id
      const item = mockFreezes.find(
        (row) => row.freezeId === freezeId && row.delFlag === 0
      )
      if (!item) return { code: 0, msg: '冻结记录不存在' }
      if (item.freezeStatus !== 'FROZEN')
        return { code: 0, msg: '只有冻结中的记录才能延期' }
      const { newPlannedUnfreezeDate, reason } = req.body || {}
      if (!newPlannedUnfreezeDate)
        return { code: 0, msg: '新的计划解冻日期不能为空' }
      const oldDate = item.plannedUnfreezeDate || '无'
      item.plannedUnfreezeDate = newPlannedUnfreezeDate
      if (!mockHistories[freezeId]) mockHistories[freezeId] = []
      mockHistories[freezeId].unshift({
        historyId: `H${++historyIdCounter}`,
        freezeId,
        operationType: 'EXTEND',
        operationDesc: '延期冻结，原因：' + (reason || '未填写'),
        amount: item.freezeAmount,
        beforeValue: oldDate,
        afterValue: newPlannedUnfreezeDate,
        operator: '当前用户',
        operateTime: new Date().toISOString().replace('T', ' ').slice(0, 19),
        createTime: new Date().toISOString().replace('T', ' ').slice(0, 19),
      })
      return { code: 1, msg: '延期成功' }
    },
  },
]
