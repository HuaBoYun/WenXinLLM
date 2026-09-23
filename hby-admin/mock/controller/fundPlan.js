/**
 * @description 资金计划管理mock接口
 */

// 资金计划明细数据
let allPlanDetails = [
  {
    detailId: 1,
    planId: 1,
    businessType: 'INCOME',
    businessItem: '产品销售收入',
    plannedDate: '2025-01-15',
    plannedAmount: 5000000,
    actualDate: '2025-01-16',
    actualAmount: 4800000,
    varianceAmount: -200000,
    varianceRate: -4,
    executionStatus: 'EXECUTED',
    executionRate: 96,
    currencyCode: 'CNY',
    remark: '主营业务收入',
    createTime: '2025-01-01 09:00:00',
  },
  {
    detailId: 2,
    planId: 1,
    businessType: 'EXPENSE',
    businessItem: '原材料采购',
    plannedDate: '2025-01-20',
    plannedAmount: 2000000,
    actualDate: null,
    actualAmount: null,
    varianceAmount: null,
    varianceRate: null,
    executionStatus: 'PENDING',
    executionRate: null,
    currencyCode: 'CNY',
    remark: '生产原材料',
    createTime: '2025-01-01 09:00:00',
  },
  {
    detailId: 3,
    planId: 1,
    businessType: 'EXPENSE',
    businessItem: '员工薪酬',
    plannedDate: '2025-01-25',
    plannedAmount: 1500000,
    actualDate: null,
    actualAmount: null,
    varianceAmount: null,
    varianceRate: null,
    executionStatus: 'PROCESSING',
    executionRate: null,
    currencyCode: 'CNY',
    remark: '月度薪酬发放',
    createTime: '2025-01-01 09:00:00',
  },
  {
    detailId: 4,
    planId: 1,
    businessType: 'INVESTMENT',
    businessItem: '设备投资',
    plannedDate: '2025-02-10',
    plannedAmount: 3000000,
    actualDate: '2025-02-12',
    actualAmount: 2950000,
    varianceAmount: -50000,
    varianceRate: -1.67,
    executionStatus: 'EXECUTED',
    executionRate: 98.33,
    currencyCode: 'CNY',
    remark: '生产线设备采购',
    createTime: '2025-01-05 10:00:00',
  },
  {
    detailId: 5,
    planId: 1,
    businessType: 'FINANCING',
    businessItem: '银行贷款',
    plannedDate: '2025-02-20',
    plannedAmount: 10000000,
    actualDate: null,
    actualAmount: null,
    varianceAmount: null,
    varianceRate: null,
    executionStatus: 'PENDING',
    executionRate: null,
    currencyCode: 'CNY',
    remark: '流动资金贷款',
    createTime: '2025-01-05 10:00:00',
  },
]

let detailIdCounter = 6

const allExecutions = [
  {
    executionId: 1,
    planId: 1,
    executionNo: 'EX202501001',
    executionType: 'EXPENSE',
    executionStatus: 'COMPLETED',
    plannedAmount: 500000,
    executedAmount: 480000,
    executionRate: 96,
    plannedDate: '2025-01-15',
    actualDate: '2025-01-15',
    delayDays: 0,
    executionEfficiency: 96,
    riskLevel: 'LOW',
    executionNotes: '按时完成',
    createdTime: '2025-01-10 09:00:00',
  },
  {
    executionId: 2,
    planId: 1,
    executionNo: 'EX202501002',
    executionType: 'INCOME',
    executionStatus: 'IN_PROGRESS',
    plannedAmount: 1000000,
    executedAmount: 600000,
    executionRate: 60,
    plannedDate: '2025-01-20',
    actualDate: null,
    delayDays: 5,
    executionEfficiency: 72,
    riskLevel: 'MEDIUM',
    executionNotes: '进行中',
    createdTime: '2025-01-12 10:30:00',
  },
  {
    executionId: 3,
    planId: 2,
    executionNo: 'EX202502001',
    executionType: 'INVESTMENT',
    executionStatus: 'PENDING',
    plannedAmount: 2000000,
    executedAmount: 0,
    executionRate: 0,
    plannedDate: '2025-02-01',
    actualDate: null,
    delayDays: 12,
    executionEfficiency: 0,
    riskLevel: 'HIGH',
    executionNotes: '待执行',
    createdTime: '2025-01-20 14:00:00',
  },
]

module.exports = [
  {
    url: '/qqsk/fund/plan/page',
    type: 'get',
    response() {
      return {
        code: 1,
        msg: '操作成功',
        data: {
          records: [
            {
              planId: 1,
              planNo: 'ZJ202501240001',
              planName: '2025年第一季度资金计划',
              planType: 'QUARTERLY',
              planStatus: 'APPROVED',
              startDate: '2025-01-01',
              endDate: '2025-03-31',
              createdTime: '2024-12-20 14:00:00',
            },
            {
              planId: 2,
              planNo: 'ZJ202501240002',
              planName: '设备采购专项资金计划',
              planType: 'PROJECT',
              planStatus: 'EXECUTING',
              startDate: '2025-01-15',
              endDate: '2025-06-30',
              createdTime: '2025-01-10 09:15:00',
            },
            {
              planId: 3,
              planNo: 'ZJ202502010001',
              planName: '2025年第二季度资金计划',
              planType: 'QUARTERLY',
              planStatus: 'DRAFT',
              startDate: '2025-04-01',
              endDate: '2025-06-30',
              createdTime: '2025-03-01 10:00:00',
            },
          ],
          total: 3,
          current: 1,
          size: 200,
        },
        result: null,
      }
    },
  },
  {
    url: '/qqsk/fund/plan-execution/page',
    type: 'get',
    response(req) {
      const minDelay = parseInt(req.query.minDelayDays) || 0
      const status = req.query.executionStatus
      const type = req.query.executionType
      const riskLevel = req.query.riskLevel
      let list = allExecutions.filter((item) => {
        if (minDelay > 0 && item.delayDays < minDelay) return false
        if (status && item.executionStatus !== status) return false
        if (type && item.executionType !== type) return false
        if (riskLevel && item.riskLevel !== riskLevel) return false
        return true
      })
      return {
        code: 1,
        msg: '操作成功',
        data: {
          records: list,
          total: list.length,
          current: 1,
          size: 20,
        },
        result: null,
      }
    },
  },
  {
    url: '/qqsk/fund/plan-execution/summary',
    type: 'get',
    response() {
      return {
        code: 1,
        msg: '操作成功',
        data: {
          TOTALCOUNT: '3',
          TOTALPLANNEDAMOUNT: 3500000,
          TOTALEXECUTEDAMOUNT: 1080000,
          AVGEXECUTIONRATE: 52,
          AVGDELAYDAYS: 5.67,
        },
        result: null,
      }
    },
  },

  // ==================== 资金计划明细 mock 接口 ====================

  // 获取计划基本信息
  {
    url: '/qqsk/fund/plan/:planId',
    type: 'get',
    response(req) {
      const planId = parseInt(req.params.planId) || 1
      const plans = {
        1: {
          planId: 1,
          planNo: 'ZJ202501240001',
          planName: '2025年第一季度资金计划',
          planType: 'QUARTERLY',
          planStatus: 'APPROVED',
          startDate: '2025-01-01',
          endDate: '2025-03-31',
          totalIncome: 15000000,
          totalExpense: 12000000,
          netCashFlow: 3000000,
        },
        2: {
          planId: 2,
          planNo: 'ZJ202501240002',
          planName: '设备采购专项资金计划',
          planType: 'PROJECT',
          planStatus: 'EXECUTING',
          startDate: '2025-01-15',
          endDate: '2025-06-30',
          totalIncome: 0,
          totalExpense: 8000000,
          netCashFlow: -8000000,
        },
      }
      return {
        code: 1,
        msg: '操作成功',
        data: plans[planId] || plans[1],
        result: null,
      }
    },
  },

  // 分页查询资金计划明细
  {
    url: '/qqsk/fund/plan-detail/page',
    type: 'get',
    response(req) {
      const planId = parseInt(req.query.planId)
      const businessType = req.query.businessType
      const executionStatus = req.query.executionStatus
      const startDate = req.query.startDate
      const endDate = req.query.endDate
      const page = parseInt(req.query.page) || 1
      const limit = parseInt(req.query.limit) || 20

      let list = allPlanDetails.filter((item) => {
        if (planId && item.planId !== planId) return false
        if (businessType && item.businessType !== businessType) return false
        if (executionStatus && item.executionStatus !== executionStatus)
          return false
        if (startDate && item.plannedDate < startDate) return false
        if (endDate && item.plannedDate > endDate) return false
        return true
      })

      const total = list.length
      const start = (page - 1) * limit
      const records = list.slice(start, start + limit)

      return {
        code: 1,
        msg: '操作成功',
        data: { records, total, current: page, size: limit },
        result: null,
      }
    },
  },

  // 获取明细汇总
  {
    url: '/qqsk/fund/plan-detail/summary',
    type: 'get',
    response(req) {
      const planId = parseInt(req.query.planId)
      const list = planId
        ? allPlanDetails.filter((d) => d.planId === planId)
        : allPlanDetails
      const totalCount = list.length
      const totalPlannedAmount = list.reduce(
        (s, d) => s + (d.plannedAmount || 0),
        0
      )
      const totalActualAmount = list.reduce(
        (s, d) => s + (d.actualAmount || 0),
        0
      )
      const executedList = list.filter((d) => d.executionRate !== null)
      const avgExecutionRate = executedList.length
        ? Math.round(
            (executedList.reduce((s, d) => s + d.executionRate, 0) /
              executedList.length) *
              100
          ) / 100
        : 0
      return {
        code: 1,
        msg: '操作成功',
        data: {
          totalCount,
          totalPlannedAmount,
          totalActualAmount,
          avgExecutionRate,
        },
        result: null,
      }
    },
  },

  // 创建资金计划明细
  {
    url: '/qqsk/fund/plan-detail',
    type: 'post',
    response(req) {
      const body = req.body || {}
      const newDetail = {
        detailId: detailIdCounter++,
        planId: parseInt(body.planId) || 1,
        businessType: body.businessType || 'EXPENSE',
        businessItem: body.businessItem || '新明细',
        plannedDate: body.plannedDate || new Date().toISOString().split('T')[0],
        plannedAmount: parseFloat(body.plannedAmount) || 0,
        actualDate: null,
        actualAmount: null,
        varianceAmount: null,
        varianceRate: null,
        executionStatus: 'PENDING',
        executionRate: null,
        currencyCode: body.currencyCode || 'CNY',
        remark: body.remark || '',
        createTime: new Date().toLocaleString('zh-CN'),
      }
      allPlanDetails.push(newDetail)
      return { code: 1, msg: '创建成功', data: newDetail, result: null }
    },
  },

  // 更新资金计划明细
  {
    url: '/qqsk/fund/plan-detail',
    type: 'put',
    response(req) {
      const body = req.body || {}
      const detailId = parseInt(body.detailId)
      const idx = allPlanDetails.findIndex((d) => d.detailId === detailId)
      if (idx === -1)
        return { code: 0, msg: '明细不存在', data: null, result: null }
      allPlanDetails[idx] = { ...allPlanDetails[idx], ...body, detailId }
      return {
        code: 1,
        msg: '更新成功',
        data: allPlanDetails[idx],
        result: null,
      }
    },
  },

  // 删除资金计划明细
  {
    url: '/qqsk/fund/plan-detail/:detailId',
    type: 'delete',
    response(req) {
      const detailId = parseInt(req.params.detailId)
      const idx = allPlanDetails.findIndex((d) => d.detailId === detailId)
      if (idx === -1)
        return { code: 0, msg: '明细不存在', data: null, result: null }
      allPlanDetails.splice(idx, 1)
      return { code: 1, msg: '删除成功', data: null, result: null }
    },
  },

  // 执行资金计划明细
  {
    url: '/qqsk/fund/plan-detail/:detailId/execute',
    type: 'put',
    response(req) {
      const detailId = parseInt(req.params.detailId)
      const body = req.body || {}
      const idx = allPlanDetails.findIndex((d) => d.detailId === detailId)
      if (idx === -1)
        return { code: 0, msg: '明细不存在', data: null, result: null }
      const actualAmount =
        parseFloat(body.actualAmount) || allPlanDetails[idx].plannedAmount
      const plannedAmount = allPlanDetails[idx].plannedAmount
      const varianceAmount = actualAmount - plannedAmount
      const varianceRate = plannedAmount
        ? Math.round((varianceAmount / plannedAmount) * 10000) / 100
        : 0
      const executionRate = plannedAmount
        ? Math.round((actualAmount / plannedAmount) * 10000) / 100
        : 0
      allPlanDetails[idx] = {
        ...allPlanDetails[idx],
        executionStatus: 'EXECUTED',
        actualDate: body.actualDate || new Date().toISOString().split('T')[0],
        actualAmount,
        varianceAmount,
        varianceRate,
        executionRate,
      }
      return {
        code: 1,
        msg: '执行成功',
        data: allPlanDetails[idx],
        result: null,
      }
    },
  },

  // 调整资金计划明细
  {
    url: '/qqsk/fund/plan-detail/:detailId/adjust',
    type: 'put',
    response(req) {
      const detailId = parseInt(req.params.detailId)
      const body = req.body || {}
      const idx = allPlanDetails.findIndex((d) => d.detailId === detailId)
      if (idx === -1)
        return { code: 0, msg: '明细不存在', data: null, result: null }
      allPlanDetails[idx] = {
        ...allPlanDetails[idx],
        plannedAmount:
          parseFloat(body.plannedAmount) || allPlanDetails[idx].plannedAmount,
        plannedDate: body.plannedDate || allPlanDetails[idx].plannedDate,
        remark: body.remark || allPlanDetails[idx].remark,
      }
      return {
        code: 1,
        msg: '调整成功',
        data: allPlanDetails[idx],
        result: null,
      }
    },
  },

  // 差异分析
  {
    url: '/qqsk/fund/plan-detail/variance-analysis',
    type: 'get',
    response(req) {
      const planId = parseInt(req.query.planId)
      const list = planId
        ? allPlanDetails.filter(
            (d) => d.planId === planId && d.executionStatus === 'EXECUTED'
          )
        : []
      const analysisData = list.map((d) => ({
        businessItem: d.businessItem,
        businessType: d.businessType,
        plannedAmount: d.plannedAmount,
        actualAmount: d.actualAmount,
        varianceAmount: d.varianceAmount,
        varianceRate: d.varianceRate,
      }))
      return { code: 1, msg: '操作成功', data: analysisData, result: null }
    },
  },

  // 执行分析
  {
    url: '/qqsk/fund/plan-detail/execution-analysis',
    type: 'get',
    response(req) {
      const planId = parseInt(req.query.planId)
      const list = planId
        ? allPlanDetails.filter((d) => d.planId === planId)
        : allPlanDetails
      const statusStats = {
        PENDING: list.filter((d) => d.executionStatus === 'PENDING').length,
        PROCESSING: list.filter((d) => d.executionStatus === 'PROCESSING')
          .length,
        EXECUTED: list.filter((d) => d.executionStatus === 'EXECUTED').length,
        CANCELLED: list.filter((d) => d.executionStatus === 'CANCELLED').length,
      }
      const typeStats = {}
      list.forEach((d) => {
        if (!typeStats[d.businessType])
          typeStats[d.businessType] = {
            count: 0,
            plannedAmount: 0,
            actualAmount: 0,
          }
        typeStats[d.businessType].count++
        typeStats[d.businessType].plannedAmount += d.plannedAmount || 0
        typeStats[d.businessType].actualAmount += d.actualAmount || 0
      })
      return {
        code: 1,
        msg: '操作成功',
        data: { statusStats, typeStats, totalCount: list.length },
        result: null,
      }
    },
  },
]
