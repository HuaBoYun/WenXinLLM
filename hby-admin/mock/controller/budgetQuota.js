/**
 * 预算配额 Mock 接口
 */

let idCounter = 200

const mockQuotas = [
  {
    quotaId: 'QT001',
    quotaCode: 'QC-2026-001',
    quotaName: '研发部年度配额',
    budgetId: 'BUD001',
    quotaType: 'ANNUAL',
    quotaAmount: 1200000,
    usedAmount: 450000,
    availableAmount: 750000,
    quotaPeriod: 'YEARLY',
    effectiveDate: '2026-01-01',
    expiryDate: '2026-12-31',
    quotaStatus: 'ACTIVE',
    isEnabled: 1,
    quotaDescription: '研发部2026年度预算配额',
    allocatedAmount: 900000,
    remark: '年度研发预算',
    createBy: 'admin',
    createTime: '2026-01-01 09:00:00',
    updateBy: 'admin',
    updateTime: '2026-03-15 10:00:00',
    delFlag: 0,
  },
  {
    quotaId: 'QT002',
    quotaCode: 'QC-2026-002',
    quotaName: '市场部季度配额',
    budgetId: 'BUD002',
    quotaType: 'QUARTERLY',
    quotaAmount: 300000,
    usedAmount: 180000,
    availableAmount: 120000,
    quotaPeriod: 'QUARTERLY',
    effectiveDate: '2026-01-01',
    expiryDate: '2026-03-31',
    quotaStatus: 'ACTIVE',
    isEnabled: 1,
    quotaDescription: '市场部Q1季度配额',
    allocatedAmount: 280000,
    remark: '季度市场预算',
    createBy: 'admin',
    createTime: '2026-01-01 09:00:00',
    updateBy: 'admin',
    updateTime: '2026-02-20 11:00:00',
    delFlag: 0,
  },
  {
    quotaId: 'QT003',
    quotaCode: 'QC-2026-003',
    quotaName: '销售部月度配额',
    budgetId: 'BUD003',
    quotaType: 'MONTHLY',
    quotaAmount: 80000,
    usedAmount: 80000,
    availableAmount: 0,
    quotaPeriod: 'MONTHLY',
    effectiveDate: '2026-03-01',
    expiryDate: '2026-03-31',
    quotaStatus: 'EXPIRED',
    isEnabled: 0,
    quotaDescription: '销售部3月配额（已过期）',
    allocatedAmount: 80000,
    remark: '月度销售预算',
    createBy: 'admin',
    createTime: '2026-03-01 09:00:00',
    updateBy: 'admin',
    updateTime: '2026-04-01 09:00:00',
    delFlag: 0,
  },
  {
    quotaId: 'QT004',
    quotaCode: 'QC-2026-004',
    quotaName: '人力资源部年度配额',
    budgetId: 'BUD004',
    quotaType: 'ANNUAL',
    quotaAmount: 500000,
    usedAmount: 120000,
    availableAmount: 380000,
    quotaPeriod: 'YEARLY',
    effectiveDate: '2026-01-01',
    expiryDate: '2026-12-31',
    quotaStatus: 'ACTIVE',
    isEnabled: 1,
    quotaDescription: '人力资源部2026年度配额',
    allocatedAmount: 400000,
    remark: '年度HR预算',
    createBy: 'admin',
    createTime: '2026-01-01 09:00:00',
    updateBy: 'admin',
    updateTime: '2026-03-10 14:00:00',
    delFlag: 0,
  },
  {
    quotaId: 'QT005',
    quotaCode: 'QC-2026-005',
    quotaName: '财务部年度配额',
    budgetId: 'BUD005',
    quotaType: 'ANNUAL',
    quotaAmount: 200000,
    usedAmount: 60000,
    availableAmount: 140000,
    quotaPeriod: 'YEARLY',
    effectiveDate: '2026-01-01',
    expiryDate: '2026-12-31',
    quotaStatus: 'ACTIVE',
    isEnabled: 1,
    quotaDescription: '财务部2026年度配额',
    allocatedAmount: 180000,
    remark: '年度财务预算',
    createBy: 'admin',
    createTime: '2026-01-01 09:00:00',
    updateBy: 'admin',
    updateTime: '2026-02-15 09:00:00',
    delFlag: 0,
  },
  {
    quotaId: 'QT006',
    quotaCode: 'QC-2026-006',
    quotaName: '信息技术部项目配额',
    budgetId: 'BUD006',
    quotaType: 'PROJECT',
    quotaAmount: 800000,
    usedAmount: 320000,
    availableAmount: 480000,
    quotaPeriod: 'PROJECT',
    effectiveDate: '2026-02-01',
    expiryDate: '2026-11-30',
    quotaStatus: 'ACTIVE',
    isEnabled: 1,
    quotaDescription: 'IT系统升级项目配额',
    allocatedAmount: 700000,
    remark: '项目IT预算',
    createBy: 'admin',
    createTime: '2026-02-01 09:00:00',
    updateBy: 'admin',
    updateTime: '2026-03-20 10:00:00',
    delFlag: 0,
  },
  {
    quotaId: 'QT007',
    quotaCode: 'QC-2026-007',
    quotaName: '运营部部门配额',
    budgetId: 'BUD007',
    quotaType: 'DEPARTMENT',
    quotaAmount: 350000,
    usedAmount: 0,
    availableAmount: 350000,
    quotaPeriod: 'YEARLY',
    effectiveDate: '2026-01-01',
    expiryDate: '2026-12-31',
    quotaStatus: 'INACTIVE',
    isEnabled: 0,
    quotaDescription: '运营部2026年度配额（暂停）',
    allocatedAmount: 0,
    remark: '部门运营预算',
    createBy: 'admin',
    createTime: '2026-01-05 09:00:00',
    updateBy: 'admin',
    updateTime: '2026-01-20 09:00:00',
    delFlag: 0,
  },
  {
    quotaId: 'QT008',
    quotaCode: 'QC-2026-008',
    quotaName: '采购部年度配额',
    budgetId: 'BUD008',
    quotaType: 'ANNUAL',
    quotaAmount: 600000,
    usedAmount: 200000,
    availableAmount: 400000,
    quotaPeriod: 'YEARLY',
    effectiveDate: '2026-01-01',
    expiryDate: '2026-12-31',
    quotaStatus: 'ACTIVE',
    isEnabled: 1,
    quotaDescription: '采购部2026年度配额',
    allocatedAmount: 500000,
    remark: '年度采购预算',
    createBy: 'admin',
    createTime: '2026-01-01 09:00:00',
    updateBy: 'admin',
    updateTime: '2026-03-01 09:00:00',
    delFlag: 0,
  },
  {
    quotaId: 'QT009',
    quotaCode: 'QC-2025-009',
    quotaName: '行政部年度配额',
    budgetId: 'BUD009',
    quotaType: 'ANNUAL',
    quotaAmount: 120000,
    usedAmount: 120000,
    availableAmount: 0,
    quotaPeriod: 'YEARLY',
    effectiveDate: '2025-01-01',
    expiryDate: '2025-12-31',
    quotaStatus: 'EXPIRED',
    isEnabled: 0,
    quotaDescription: '行政部2025年度配额（已过期）',
    allocatedAmount: 120000,
    remark: '年度行政预算',
    createBy: 'admin',
    createTime: '2025-01-01 09:00:00',
    updateBy: 'admin',
    updateTime: '2026-01-01 09:00:00',
    delFlag: 0,
  },
  {
    quotaId: 'QT010',
    quotaCode: 'QC-2026-010',
    quotaName: '法务部年度配额',
    budgetId: 'BUD010',
    quotaType: 'ANNUAL',
    quotaAmount: 150000,
    usedAmount: 30000,
    availableAmount: 120000,
    quotaPeriod: 'YEARLY',
    effectiveDate: '2026-01-01',
    expiryDate: '2026-12-31',
    quotaStatus: 'ACTIVE',
    isEnabled: 1,
    quotaDescription: '法务部2026年度配额',
    allocatedAmount: 130000,
    remark: '年度法务预算',
    createBy: 'admin',
    createTime: '2026-01-01 09:00:00',
    updateBy: 'admin',
    updateTime: '2026-02-10 09:00:00',
    delFlag: 0,
  },
  {
    quotaId: 'QT011',
    quotaCode: 'QC-2026-011',
    quotaName: '品质部季度配额',
    budgetId: 'BUD011',
    quotaType: 'QUARTERLY',
    quotaAmount: 90000,
    usedAmount: 45000,
    availableAmount: 45000,
    quotaPeriod: 'QUARTERLY',
    effectiveDate: '2026-04-01',
    expiryDate: '2026-06-30',
    quotaStatus: 'ACTIVE',
    isEnabled: 1,
    quotaDescription: '品质部Q2季度配额',
    allocatedAmount: 80000,
    remark: '季度品质预算',
    createBy: 'admin',
    createTime: '2026-04-01 09:00:00',
    updateBy: 'admin',
    updateTime: '2026-04-01 09:00:00',
    delFlag: 0,
  },
  {
    quotaId: 'QT012',
    quotaCode: 'QC-2026-012',
    quotaName: '客服部月度配额',
    budgetId: 'BUD012',
    quotaType: 'MONTHLY',
    quotaAmount: 50000,
    usedAmount: 22000,
    availableAmount: 28000,
    quotaPeriod: 'MONTHLY',
    effectiveDate: '2026-04-01',
    expiryDate: '2026-04-30',
    quotaStatus: 'ACTIVE',
    isEnabled: 1,
    quotaDescription: '客服部4月配额',
    allocatedAmount: 45000,
    remark: '月度客服预算',
    createBy: 'admin',
    createTime: '2026-04-01 09:00:00',
    updateBy: 'admin',
    updateTime: '2026-04-01 09:00:00',
    delFlag: 0,
  },
  {
    quotaId: 'QT013',
    quotaCode: 'QC-2026-013',
    quotaName: '战略发展部项目配额',
    budgetId: 'BUD013',
    quotaType: 'PROJECT',
    quotaAmount: 2000000,
    usedAmount: 500000,
    availableAmount: 1500000,
    quotaPeriod: 'PROJECT',
    effectiveDate: '2026-01-01',
    expiryDate: '2027-12-31',
    quotaStatus: 'ACTIVE',
    isEnabled: 1,
    quotaDescription: '战略转型项目配额',
    allocatedAmount: 1800000,
    remark: '战略项目预算',
    createBy: 'admin',
    createTime: '2026-01-01 09:00:00',
    updateBy: 'admin',
    updateTime: '2026-03-15 09:00:00',
    delFlag: 0,
  },
  {
    quotaId: 'QT014',
    quotaCode: 'QC-2026-014',
    quotaName: '生产部自定义配额',
    budgetId: 'BUD014',
    quotaType: 'CUSTOM',
    quotaAmount: 450000,
    usedAmount: 380000,
    availableAmount: 70000,
    quotaPeriod: 'CUSTOM',
    effectiveDate: '2026-01-01',
    expiryDate: '2026-06-30',
    quotaStatus: 'ACTIVE',
    isEnabled: 1,
    quotaDescription: '生产部上半年自定义配额',
    allocatedAmount: 420000,
    remark: '自定义生产预算',
    createBy: 'admin',
    createTime: '2026-01-01 09:00:00',
    updateBy: 'admin',
    updateTime: '2026-04-01 09:00:00',
    delFlag: 0,
  },
  {
    quotaId: 'QT015',
    quotaCode: 'QC-2026-015',
    quotaName: '物流部年度配额',
    budgetId: 'BUD015',
    quotaType: 'ANNUAL',
    quotaAmount: 280000,
    usedAmount: 95000,
    availableAmount: 185000,
    quotaPeriod: 'YEARLY',
    effectiveDate: '2026-01-01',
    expiryDate: '2026-12-31',
    quotaStatus: 'ACTIVE',
    isEnabled: 1,
    quotaDescription: '物流部2026年度配额',
    allocatedAmount: 250000,
    remark: '年度物流预算',
    createBy: 'admin',
    createTime: '2026-01-01 09:00:00',
    updateBy: 'admin',
    updateTime: '2026-03-05 09:00:00',
    delFlag: 0,
  },
  {
    quotaId: 'QT016',
    quotaCode: 'QC-2026-016',
    quotaName: '安全部年度配额',
    budgetId: 'BUD016',
    quotaType: 'ANNUAL',
    quotaAmount: 100000,
    usedAmount: 15000,
    availableAmount: 85000,
    quotaPeriod: 'YEARLY',
    effectiveDate: '2026-01-01',
    expiryDate: '2026-12-31',
    quotaStatus: 'ACTIVE',
    isEnabled: 1,
    quotaDescription: '安全部2026年度配额',
    allocatedAmount: 90000,
    remark: '年度安全预算',
    createBy: 'admin',
    createTime: '2026-01-01 09:00:00',
    updateBy: 'admin',
    updateTime: '2026-02-01 09:00:00',
    delFlag: 0,
  },
  {
    quotaId: 'QT017',
    quotaCode: 'QC-2026-017',
    quotaName: '培训部季度配额',
    budgetId: 'BUD017',
    quotaType: 'QUARTERLY',
    quotaAmount: 60000,
    usedAmount: 60000,
    availableAmount: 0,
    quotaPeriod: 'QUARTERLY',
    effectiveDate: '2026-01-01',
    expiryDate: '2026-03-31',
    quotaStatus: 'EXPIRED',
    isEnabled: 0,
    quotaDescription: '培训部Q1季度配额（已过期）',
    allocatedAmount: 60000,
    remark: '季度培训预算',
    createBy: 'admin',
    createTime: '2026-01-01 09:00:00',
    updateBy: 'admin',
    updateTime: '2026-04-01 09:00:00',
    delFlag: 0,
  },
  {
    quotaId: 'QT018',
    quotaCode: 'QC-2026-018',
    quotaName: '公关部年度配额',
    budgetId: 'BUD018',
    quotaType: 'ANNUAL',
    quotaAmount: 180000,
    usedAmount: 40000,
    availableAmount: 140000,
    quotaPeriod: 'YEARLY',
    effectiveDate: '2026-01-01',
    expiryDate: '2026-12-31',
    quotaStatus: 'ACTIVE',
    isEnabled: 1,
    quotaDescription: '公关部2026年度配额',
    allocatedAmount: 160000,
    remark: '年度公关预算',
    createBy: 'admin',
    createTime: '2026-01-01 09:00:00',
    updateBy: 'admin',
    updateTime: '2026-03-01 09:00:00',
    delFlag: 0,
  },
  {
    quotaId: 'QT019',
    quotaCode: 'QC-2026-019',
    quotaName: '数据中心项目配额',
    budgetId: 'BUD019',
    quotaType: 'PROJECT',
    quotaAmount: 3000000,
    usedAmount: 1200000,
    availableAmount: 1800000,
    quotaPeriod: 'PROJECT',
    effectiveDate: '2026-03-01',
    expiryDate: '2027-06-30',
    quotaStatus: 'ACTIVE',
    isEnabled: 1,
    quotaDescription: '数据中心建设项目配额',
    allocatedAmount: 2800000,
    remark: '数据中心项目预算',
    createBy: 'admin',
    createTime: '2026-03-01 09:00:00',
    updateBy: 'admin',
    updateTime: '2026-04-01 09:00:00',
    delFlag: 0,
  },
  {
    quotaId: 'QT020',
    quotaCode: 'QC-2026-020',
    quotaName: '综合管理部部门配额',
    budgetId: 'BUD020',
    quotaType: 'DEPARTMENT',
    quotaAmount: 220000,
    usedAmount: 75000,
    availableAmount: 145000,
    quotaPeriod: 'YEARLY',
    effectiveDate: '2026-01-01',
    expiryDate: '2026-12-31',
    quotaStatus: 'ACTIVE',
    isEnabled: 1,
    quotaDescription: '综合管理部2026年度配额',
    allocatedAmount: 200000,
    remark: '部门综合预算',
    createBy: 'admin',
    createTime: '2026-01-01 09:00:00',
    updateBy: 'admin',
    updateTime: '2026-03-10 09:00:00',
    delFlag: 0,
  },
]

function getActiveList() {
  return mockQuotas.filter((q) => q.delFlag === 0)
}

function calcStats() {
  const list = getActiveList()
  const active = list.filter((q) => q.quotaStatus === 'ACTIVE')
  const inactive = list.filter((q) => q.quotaStatus === 'INACTIVE')
  const expired = list.filter((q) => q.quotaStatus === 'EXPIRED')
  const sum = (arr, key) => arr.reduce((s, q) => s + (q[key] || 0), 0)
  const totalQuota = sum(list, 'quotaAmount')
  const usedAmount = sum(list, 'usedAmount')
  const availableAmount = sum(list, 'availableAmount')
  const allocatedAmount = sum(list, 'allocatedAmount')
  return {
    totalCount: list.length,
    activeCount: active.length,
    inactiveCount: inactive.length,
    expiredCount: expired.length,
    totalQuota,
    usedAmount,
    remainingAmount: availableAmount,
    allocatedAmount,
    usageRate:
      totalQuota > 0 ? Math.round((usedAmount / totalQuota) * 1000) / 10 : 0,
    allocationRate:
      totalQuota > 0
        ? Math.round((allocatedAmount / totalQuota) * 1000) / 10
        : 0,
    remainingRate:
      totalQuota > 0
        ? Math.round((availableAmount / totalQuota) * 1000) / 10
        : 0,
  }
}

module.exports = [
  // 分页查询
  {
    url: '/glkj/accountant/budget/quota/page',
    type: 'post',
    response(req) {
      const {
        pageNum = 1,
        pageSize = 20,
        quotaName,
        quotaType,
        quotaStatus,
        budgetAccount,
        budgetId,
      } = req.body || {}
      let list = getActiveList()
      if (quotaName) list = list.filter((q) => q.quotaName.includes(quotaName))
      if (quotaType) list = list.filter((q) => q.quotaType === quotaType)
      if (quotaStatus) list = list.filter((q) => q.quotaStatus === quotaStatus)
      if (budgetAccount) list = list.filter((q) => q.budgetId === budgetAccount)
      if (budgetId) list = list.filter((q) => q.budgetId === budgetId)
      const start = (pageNum - 1) * pageSize
      const tlist = list.slice(start, start + pageSize)
      return {
        code: 1,
        msg: '查询成功',
        data: {
          tlist,
          list: tlist,
          totalRecord: list.length,
          total: list.length,
          pageNo: pageNum,
          pageNum,
          pageSize,
          totalPage: Math.ceil(list.length / pageSize),
        },
      }
    },
  },
  // 统计
  {
    url: '/glkj/accountant/budget/quota/statistics',
    type: 'get',
    response() {
      return { code: 1, msg: '查询成功', data: calcStats() }
    },
  },
  // 创建
  {
    url: '/glkj/accountant/budget/quota/create',
    type: 'post',
    response(req) {
      const data = req.body || {}
      const newItem = {
        ...data,
        quotaId: 'QT' + String(++idCounter),
        quotaStatus: data.quotaStatus || 'ACTIVE',
        usedAmount: 0,
        availableAmount: data.quotaAmount || 0,
        createBy: 'admin',
        createTime: new Date().toISOString(),
        updateBy: 'admin',
        updateTime: new Date().toISOString(),
        delFlag: 0,
      }
      mockQuotas.push(newItem)
      return { code: 1, msg: '创建成功', data: newItem }
    },
  },
  // 更新
  {
    url: '/glkj/accountant/budget/quota/update/:id',
    type: 'put',
    response(req) {
      const id = req.params.id
      const idx = mockQuotas.findIndex((q) => q.quotaId === id)
      if (idx === -1) return { code: 0, msg: '配额不存在' }
      Object.assign(mockQuotas[idx], req.body, {
        updateTime: new Date().toISOString(),
      })
      return { code: 1, msg: '更新成功', data: mockQuotas[idx] }
    },
  },
  // 删除
  {
    url: '/glkj/accountant/budget/quota/delete/:id',
    type: 'delete',
    response(req) {
      const id = req.params.id
      const item = mockQuotas.find((q) => q.quotaId === id)
      if (!item) return { code: 0, msg: '配额不存在' }
      item.delFlag = 1
      return { code: 1, msg: '删除成功' }
    },
  },
  // 详情
  {
    url: '/glkj/accountant/budget/quota/detail/:id',
    type: 'get',
    response(req) {
      const id = req.params.id
      const item = mockQuotas.find((q) => q.quotaId === id && q.delFlag === 0)
      if (!item) return { code: 0, msg: '配额不存在' }
      return { code: 1, msg: '查询成功', data: item }
    },
  },
  // 启用
  {
    url: '/glkj/accountant/budget/quota/enable/:id',
    type: 'put',
    response(req) {
      const item = mockQuotas.find((q) => q.quotaId === req.params.id)
      if (item) {
        item.isEnabled = 1
        item.quotaStatus = 'ACTIVE'
      }
      return { code: 1, msg: '启用成功' }
    },
  },
  // 禁用
  {
    url: '/glkj/accountant/budget/quota/disable/:id',
    type: 'put',
    response(req) {
      const item = mockQuotas.find((q) => q.quotaId === req.params.id)
      if (item) {
        item.isEnabled = 0
        item.quotaStatus = 'INACTIVE'
      }
      return { code: 1, msg: '禁用成功' }
    },
  },
  // 组织列表
  {
    url: '/glkj/accountant/budget/quota/organizations',
    type: 'get',
    response() {
      return {
        code: 1,
        msg: '查询成功',
        data: [
          {
            id: 'ORG001',
            name: '总公司',
            value: 'ORG001',
            label: '总公司',
            children: [
              {
                id: 'ORG001-1',
                name: '研发中心',
                value: 'ORG001-1',
                label: '研发中心',
              },
              {
                id: 'ORG001-2',
                name: '市场部',
                value: 'ORG001-2',
                label: '市场部',
              },
              {
                id: 'ORG001-3',
                name: '财务部',
                value: 'ORG001-3',
                label: '财务部',
              },
            ],
          },
          {
            id: 'ORG002',
            name: '华东分公司',
            value: 'ORG002',
            label: '华东分公司',
          },
          {
            id: 'ORG003',
            name: '华南分公司',
            value: 'ORG003',
            label: '华南分公司',
          },
        ],
      }
    },
  },
  // 预算科目列表
  {
    url: '/glkj/accountant/budget/quota/accounts',
    type: 'get',
    response() {
      return {
        code: 1,
        msg: '查询成功',
        data: [
          {
            id: 'BUD001',
            name: '研发费用',
            value: 'BUD001',
            label: '研发费用',
          },
          {
            id: 'BUD002',
            name: '市场推广费',
            value: 'BUD002',
            label: '市场推广费',
          },
          {
            id: 'BUD003',
            name: '销售费用',
            value: 'BUD003',
            label: '销售费用',
          },
          {
            id: 'BUD004',
            name: '人力资源费',
            value: 'BUD004',
            label: '人力资源费',
          },
          {
            id: 'BUD005',
            name: '财务管理费',
            value: 'BUD005',
            label: '财务管理费',
          },
          {
            id: 'BUD006',
            name: 'IT建设费',
            value: 'BUD006',
            label: 'IT建设费',
          },
          {
            id: 'BUD007',
            name: '运营费用',
            value: 'BUD007',
            label: '运营费用',
          },
          {
            id: 'BUD008',
            name: '采购费用',
            value: 'BUD008',
            label: '采购费用',
          },
        ],
      }
    },
  },
  // 分配配额
  {
    url: '/glkj/accountant/budget/quota/allocate',
    type: 'post',
    response() {
      return { code: 1, msg: '分配成功', data: { success: true } }
    },
  },
  // 调整配额
  {
    url: '/glkj/accountant/budget/quota/adjust/:id',
    type: 'post',
    response() {
      return { code: 1, msg: '调整成功' }
    },
  },
  // 批量调整
  {
    url: '/glkj/accountant/budget/quota/batch/adjust',
    type: 'post',
    response(req) {
      const ids = req.body || []
      return {
        code: 1,
        msg: '批量调整成功',
        data: {
          totalCount: ids.length,
          successCount: ids.length,
          failCount: 0,
        },
      }
    },
  },
  // 计算分配
  {
    url: '/glkj/accountant/budget/quota/calculate-allocation',
    type: 'post',
    response(req) {
      const { quotaAmount = 0, allocationMethod = 'EQUAL' } = req.body || {}
      return {
        code: 1,
        msg: '计算成功',
        data: {
          quotaAmount,
          allocationMethod,
          unitCount: 4,
          unitQuota: quotaAmount / 4,
        },
      }
    },
  },
  // 导出（批量）
  {
    url: '/glkj/accountant/budget/quota/export',
    type: 'post',
    response() {
      return { code: 1, msg: '导出成功' }
    },
  },
  // 导出（单条）
  {
    url: '/glkj/accountant/budget/quota/export/:id',
    type: 'get',
    response() {
      return { code: 1, msg: '导出成功' }
    },
  },
  // 导入
  {
    url: '/glkj/accountant/budget/quota/import',
    type: 'post',
    response() {
      return { code: 1, msg: '导入成功', data: 5 }
    },
  },
  // 转移
  {
    url: '/glkj/accountant/budget/quota/transfer',
    type: 'post',
    response() {
      return { code: 1, msg: '转移成功' }
    },
  },
]
