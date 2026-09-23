/**
 * 预算差异分析 Mock 接口
 */

let idCounter = 100

const organizationOptions = [
  {
    id: 'ORG001',
    name: '集团总部',
    value: 'ORG001',
    label: '集团总部',
    children: [
      { id: 'ORG002', name: '财务部', value: 'ORG002', label: '财务部' },
      { id: 'ORG003', name: '销售部', value: 'ORG003', label: '销售部' },
    ],
  },
  {
    id: 'ORG010',
    name: '研发中心',
    value: 'ORG010',
    label: '研发中心',
    children: [
      {
        id: 'ORG011',
        name: '产品研发部',
        value: 'ORG011',
        label: '产品研发部',
      },
    ],
  },
  {
    id: 'ORG020',
    name: '市场中心',
    value: 'ORG020',
    label: '市场中心',
    children: [
      {
        id: 'ORG021',
        name: '品牌推广部',
        value: 'ORG021',
        label: '品牌推广部',
      },
    ],
  },
]

const budgetAccountOptions = [
  { id: 'ACC001', name: '差旅费', value: 'ACC001', label: '差旅费' },
  { id: 'ACC002', name: '营销费用', value: 'ACC002', label: '营销费用' },
  { id: 'ACC003', name: '研发费用', value: 'ACC003', label: '研发费用' },
  { id: 'ACC004', name: '人力成本', value: 'ACC004', label: '人力成本' },
  { id: 'ACC005', name: '办公经费', value: 'ACC005', label: '办公经费' },
  { id: 'ACC006', name: '采购费用', value: 'ACC006', label: '采购费用' },
  { id: 'ACC007', name: '运营费用', value: 'ACC007', label: '运营费用' },
  { id: 'ACC008', name: '财务管理费', value: 'ACC008', label: '财务管理费' },
]

const userOptions = [
  { id: 1001, name: '张三' },
  { id: 1002, name: '李四' },
  { id: 1003, name: '王五' },
  { id: 1004, name: '赵六' },
]

const mockVariances = [
  {
    id: 'VA001',
    organizationId: 'ORG003',
    organizationName: '销售部',
    budgetAccountId: 'ACC001',
    budgetAccountName: '差旅费',
    budgetAmount: 100000,
    actualAmount: 85000,
    varianceAmount: -15000,
    varianceRate: -15,
    varianceType: 'NEGATIVE',
    varianceReason: '业务量减少导致差旅费用降低',
    improvementMeasures: '优化差旅路线，提高出差效率',
    responsiblePerson: 1001,
    responsiblePersonName: '张三',
    analysisDate: '2026-03-10',
    delFlag: 0,
  },
  {
    id: 'VA002',
    organizationId: 'ORG021',
    organizationName: '品牌推广部',
    budgetAccountId: 'ACC002',
    budgetAccountName: '营销费用',
    budgetAmount: 150000,
    actualAmount: 165000,
    varianceAmount: 15000,
    varianceRate: 10,
    varianceType: 'POSITIVE',
    varianceReason: '新产品推广活动增加',
    improvementMeasures: '加强预算控制，优化营销策略',
    responsiblePerson: 1002,
    responsiblePersonName: '李四',
    analysisDate: '2026-03-10',
    delFlag: 0,
  },
  {
    id: 'VA003',
    organizationId: 'ORG011',
    organizationName: '产品研发部',
    budgetAccountId: 'ACC003',
    budgetAccountName: '研发费用',
    budgetAmount: 300000,
    actualAmount: 278000,
    varianceAmount: -22000,
    varianceRate: -7.33,
    varianceType: 'NEGATIVE',
    varianceReason: '部分研发项目延期导致当期费用减少',
    improvementMeasures: '加强项目进度管理',
    responsiblePerson: 1003,
    responsiblePersonName: '王五',
    analysisDate: '2026-03-10',
    delFlag: 0,
  },
  {
    id: 'VA004',
    organizationId: 'ORG001',
    organizationName: '集团总部',
    budgetAccountId: 'ACC004',
    budgetAccountName: '人力成本',
    budgetAmount: 500000,
    actualAmount: 523000,
    varianceAmount: 23000,
    varianceRate: 4.6,
    varianceType: 'POSITIVE',
    varianceReason: '新增人员招聘费用超出预算',
    improvementMeasures: '严格控制招聘规模与节奏',
    responsiblePerson: 1001,
    responsiblePersonName: '张三',
    analysisDate: '2026-03-10',
    delFlag: 0,
  },
  {
    id: 'VA005',
    organizationId: 'ORG002',
    organizationName: '财务部',
    budgetAccountId: 'ACC005',
    budgetAccountName: '办公经费',
    budgetAmount: 80000,
    actualAmount: 80000,
    varianceAmount: 0,
    varianceRate: 0,
    varianceType: 'ZERO',
    varianceReason: '预算执行良好',
    improvementMeasures: '维持现有管控措施',
    responsiblePerson: 1004,
    responsiblePersonName: '赵六',
    analysisDate: '2026-03-10',
    delFlag: 0,
  },
  {
    id: 'VA006',
    organizationId: 'ORG020',
    organizationName: '市场中心',
    budgetAccountId: 'ACC006',
    budgetAccountName: '采购费用',
    budgetAmount: 200000,
    actualAmount: 245000,
    varianceAmount: 45000,
    varianceRate: 22.5,
    varianceType: 'POSITIVE',
    varianceReason: '原材料价格上涨导致采购成本增加',
    improvementMeasures: '寻求替代供应商，签订长期合约',
    responsiblePerson: 1002,
    responsiblePersonName: '李四',
    analysisDate: '2026-02-28',
    delFlag: 0,
  },
  {
    id: 'VA007',
    organizationId: 'ORG010',
    organizationName: '研发中心',
    budgetAccountId: 'ACC007',
    budgetAccountName: '运营费用',
    budgetAmount: 120000,
    actualAmount: 98000,
    varianceAmount: -22000,
    varianceRate: -18.33,
    varianceType: 'NEGATIVE',
    varianceReason: '系统优化降低了运营成本',
    improvementMeasures: '继续推进系统效率提升项目',
    responsiblePerson: 1003,
    responsiblePersonName: '王五',
    analysisDate: '2026-02-28',
    delFlag: 0,
  },
]

const getActiveList = () => mockVariances.filter((v) => v.delFlag === 0)

const calcStats = () => {
  const list = getActiveList()
  const positiveList = list.filter((v) => v.varianceAmount > 0)
  const negativeList = list.filter((v) => v.varianceAmount < 0)
  const totalVariance = list.reduce(
    (sum, v) => sum + Math.abs(v.varianceAmount || 0),
    0
  )
  const positiveVariance = positiveList.reduce(
    (sum, v) => sum + (v.varianceAmount || 0),
    0
  )
  const negativeVariance = Math.abs(
    negativeList.reduce((sum, v) => sum + (v.varianceAmount || 0), 0)
  )
  const totalBudget = list.reduce((sum, v) => sum + (v.budgetAmount || 0), 0)
  const varianceRate =
    totalBudget > 0 ? ((totalVariance / totalBudget) * 100).toFixed(2) : 0
  return {
    totalVariance,
    positiveVariance,
    negativeVariance,
    varianceRate: parseFloat(varianceRate),
  }
}

const formatDate = (value) => {
  if (!value) return new Date().toISOString().slice(0, 10)
  const d = new Date(value)
  return isNaN(d.getTime())
    ? String(value).slice(0, 10)
    : d.toISOString().slice(0, 10)
}

module.exports = [
  // 分页查询
  {
    url: '/glkj/accountant/variance/page',
    type: 'post',
    response(req) {
      const {
        pageNum = 1,
        pageSize = 20,
        organizationPath,
        budgetAccount,
        varianceType,
        analysisDateRange,
      } = req.body || {}
      let list = getActiveList()
      if (organizationPath)
        list = list.filter((v) => v.organizationId === organizationPath)
      if (budgetAccount)
        list = list.filter((v) => v.budgetAccountId === budgetAccount)
      if (varianceType && varianceType !== 'ALL')
        list = list.filter((v) => v.varianceType === varianceType)
      if (analysisDateRange && analysisDateRange.length === 2) {
        const [start, end] = analysisDateRange
        list = list.filter(
          (v) =>
            v.analysisDate >= start.slice(0, 10) &&
            v.analysisDate <= end.slice(0, 10)
        )
      }
      const s = (pageNum - 1) * pageSize
      return {
        code: 1,
        msg: '查询成功',
        data: {
          tlist: list.slice(s, s + pageSize),
          totalRecord: list.length,
          pageNum,
          pageSize,
        },
      }
    },
  },
  // 统计数据
  {
    url: '/glkj/accountant/variance/stats',
    type: 'get',
    response() {
      return { code: 1, msg: '查询成功', data: calcStats() }
    },
  },
  // 组织单元选项
  {
    url: '/glkj/accountant/variance/organizations',
    type: 'get',
    response() {
      return { code: 1, msg: '查询成功', data: organizationOptions }
    },
  },
  // 预算科目选项
  {
    url: '/glkj/accountant/variance/accounts',
    type: 'get',
    response() {
      return { code: 1, msg: '查询成功', data: budgetAccountOptions }
    },
  },
  // 用户选项
  {
    url: '/glkj/accountant/variance/users',
    type: 'get',
    response() {
      return { code: 1, msg: '查询成功', data: userOptions }
    },
  },
  // 预算科目选项（兼容 budget-accounts 路径）
  {
    url: '/glkj/accountant/variance/budget-accounts',
    type: 'get',
    response() {
      return { code: 1, msg: '查询成功', data: budgetAccountOptions }
    },
  },
  // 图表数据
  {
    url: '/glkj/accountant/variance/chart-data',
    type: 'post',
    response(req) {
      const { type, trendChartType, distributionChartType } = req.body || {}
      if (type === 'compare') {
        return {
          code: 1,
          msg: '查询成功',
          data: {
            items: [
              {
                periodName: '2025年Q4',
                budgetAmount: 800000,
                actualAmount: 780000,
                varianceAmount: -20000,
              },
              {
                periodName: '2026年Q1',
                budgetAmount: 1150000,
                actualAmount: 1194000,
                varianceAmount: 44000,
              },
            ],
          },
        }
      }

      // 趋势图：按月度/季度/年度返回不同粒度数据
      let trendXAxis, trendSeries
      if (trendChartType === 'quarterly') {
        trendXAxis = [
          '2025Q1',
          '2025Q2',
          '2025Q3',
          '2025Q4',
          '2026Q1',
          '2026Q2',
        ]
        trendSeries = [
          { name: '正差异', data: [45000, 68000, 52000, 83000, 38000, 61000] },
          { name: '负差异', data: [32000, 41000, 59000, 27000, 44000, 35000] },
          { name: '差异率', data: [4.1, 6.3, 5.8, 7.2, 3.9, 5.5] },
        ]
      } else if (trendChartType === 'yearly') {
        trendXAxis = ['2022年', '2023年', '2024年', '2025年', '2026年']
        trendSeries = [
          { name: '正差异', data: [120000, 185000, 210000, 248000, 83000] },
          { name: '负差异', data: [95000, 130000, 160000, 175000, 59000] },
          { name: '差异率', data: [3.5, 4.8, 5.2, 6.1, 4.3] },
        ]
      } else {
        // 默认月度
        trendXAxis = ['1月', '2月', '3月', '4月', '5月', '6月']
        trendSeries = [
          { name: '正差异', data: [15000, 45000, 38000, 23000, 18000, 15000] },
          { name: '负差异', data: [22000, 15000, 37000, 12000, 28000, 19000] },
          { name: '差异率', data: [3.2, 7.8, 5.4, 4.1, 3.8, 2.9] },
        ]
      }

      // 分布图：按部门/科目/项目返回不同维度数据
      let distributionData
      if (distributionChartType === 'account') {
        distributionData = [
          { value: 45000, name: '营销费用' },
          { value: 23000, name: '人力成本' },
          { value: 22000, name: '研发费用' },
          { value: 22000, name: '运营费用' },
          { value: 15000, name: '差旅费' },
          { value: 0, name: '办公经费' },
        ]
      } else if (distributionChartType === 'project') {
        distributionData = [
          { value: 38000, name: '新产品推广项目' },
          { value: 29000, name: '系统升级项目' },
          { value: 25000, name: '人才引进项目' },
          { value: 22000, name: '降本增效项目' },
          { value: 13000, name: '其他项目' },
        ]
      } else {
        // 默认按部门
        const list = getActiveList()
        const deptMap = {}
        list.forEach((v) => {
          const key = v.organizationName
          if (!deptMap[key]) deptMap[key] = 0
          deptMap[key] += Math.abs(v.varianceAmount || 0)
        })
        distributionData = Object.entries(deptMap)
          .filter(([, val]) => val > 0)
          .map(([name, value]) => ({ name, value }))
          .sort((a, b) => b.value - a.value)
      }

      return {
        code: 1,
        msg: '查询成功',
        data: {
          trendChart: { xAxis: trendXAxis, series: trendSeries },
          distributionChart: distributionData,
        },
      }
    },
  },
  // 创建分析
  {
    url: '/glkj/accountant/variance/create',
    type: 'post',
    response(req) {
      const data = req.body || {}
      if (!data.analysisName) return { code: 0, msg: '分析名称不能为空' }
      const orgOption = organizationOptions
        .flatMap((o) => [o, ...(o.children || [])])
        .find((o) => o.id === data.organizationId)
      // 兼容前端传 accountId 或 budgetAccountId
      const accId = data.accountId || data.budgetAccountId
      const accOption = budgetAccountOptions.find((a) => a.id === accId)
      const budgetAmt = data.budgetAmount || 0
      const actualAmt = data.actualAmount || 0
      const variance = actualAmt - budgetAmt
      const newItem = {
        id: `VA${String(++idCounter).padStart(3, '0')}`,
        analysisName: data.analysisName,
        organizationId: data.organizationId || 'ORG001',
        organizationName: orgOption ? orgOption.name : '集团总部',
        budgetAccountId: accId || 'ACC001',
        accountId: accId || 'ACC001',
        budgetAccountName: accOption ? accOption.name : '综合费用',
        budgetAmount: budgetAmt,
        actualAmount: actualAmt,
        varianceAmount: variance,
        varianceRate:
          budgetAmt > 0
            ? parseFloat(((variance / budgetAmt) * 100).toFixed(2))
            : 0,
        varianceType:
          variance > 0 ? 'POSITIVE' : variance < 0 ? 'NEGATIVE' : 'ZERO',
        varianceReason: data.varianceReason || '',
        improvementMeasures: data.improvementMeasures || '',
        responsiblePerson: data.reviewedBy || null,
        responsiblePersonName: (() => {
          const u = userOptions.find((u) => u.id === data.reviewedBy)
          return u ? u.name : ''
        })(),
        analysisDate: formatDate(
          data.analysisPeriod ? data.analysisPeriod[0] : null
        ),
        delFlag: 0,
      }
      mockVariances.unshift(newItem)
      return { code: 1, msg: '创建成功', data: newItem }
    },
  },
  // 更新差异原因
  {
    url: '/glkj/accountant/variance/reason',
    type: 'put',
    response(req) {
      const { id, varianceReason, improvementMeasures, responsiblePerson } =
        req.body || {}
      const item = mockVariances.find((v) => v.id === id)
      if (!item) return { code: 0, msg: '记录不存在' }
      if (varianceReason !== undefined) item.varianceReason = varianceReason
      if (improvementMeasures !== undefined)
        item.improvementMeasures = improvementMeasures
      if (responsiblePerson !== undefined) {
        item.responsiblePerson = responsiblePerson
        const user = userOptions.find((u) => u.id === responsiblePerson)
        item.responsiblePersonName = user ? user.name : ''
      }
      return { code: 1, msg: '保存成功', data: item }
    },
  },
  // 详情（钻取）
  {
    url: '/glkj/accountant/variance/detail/:id',
    type: 'get',
    response(req) {
      const item = mockVariances.find((v) => v.id === req.params.id)
      if (!item) return { code: 0, msg: '记录不存在' }
      return {
        code: 1,
        msg: '查询成功',
        data: {
          ...item,
          details: [
            {
              itemName: '一季度',
              budgetAmount: item.budgetAmount * 0.25,
              actualAmount: item.actualAmount * 0.23,
              varianceAmount:
                item.actualAmount * 0.23 - item.budgetAmount * 0.25,
              varianceRate: (
                ((item.actualAmount * 0.23 - item.budgetAmount * 0.25) /
                  (item.budgetAmount * 0.25)) *
                100
              ).toFixed(2),
            },
            {
              itemName: '二季度',
              budgetAmount: item.budgetAmount * 0.25,
              actualAmount: item.actualAmount * 0.27,
              varianceAmount:
                item.actualAmount * 0.27 - item.budgetAmount * 0.25,
              varianceRate: (
                ((item.actualAmount * 0.27 - item.budgetAmount * 0.25) /
                  (item.budgetAmount * 0.25)) *
                100
              ).toFixed(2),
            },
            {
              itemName: '三季度',
              budgetAmount: item.budgetAmount * 0.25,
              actualAmount: item.actualAmount * 0.25,
              varianceAmount: 0,
              varianceRate: '0.00',
            },
            {
              itemName: '四季度',
              budgetAmount: item.budgetAmount * 0.25,
              actualAmount: item.actualAmount * 0.25,
              varianceAmount: 0,
              varianceRate: '0.00',
            },
          ],
        },
      }
    },
  },
  // 更新分析
  {
    url: '/glkj/accountant/variance/update',
    type: 'put',
    response(req) {
      const data = req.body || {}
      const item = mockVariances.find((v) => v.id === data.id)
      if (!item) return { code: 0, msg: '记录不存在' }
      const orgOption = organizationOptions
        .flatMap((o) => [o, ...(o.children || [])])
        .find((o) => o.id === data.organizationId)
      const accId = data.accountId || data.budgetAccountId
      const accOption = budgetAccountOptions.find((a) => a.id === accId)
      const budgetAmt =
        data.budgetAmount !== undefined ? data.budgetAmount : item.budgetAmount
      const actualAmt =
        data.actualAmount !== undefined ? data.actualAmount : item.actualAmount
      const variance = actualAmt - budgetAmt
      Object.assign(item, {
        analysisName: data.analysisName || item.analysisName,
        organizationId: data.organizationId || item.organizationId,
        organizationName: orgOption ? orgOption.name : item.organizationName,
        budgetAccountId: accId || item.budgetAccountId,
        accountId: accId || item.accountId,
        budgetAccountName: accOption ? accOption.name : item.budgetAccountName,
        budgetAmount: budgetAmt,
        actualAmount: actualAmt,
        varianceAmount: variance,
        varianceRate:
          budgetAmt > 0
            ? parseFloat(((variance / budgetAmt) * 100).toFixed(2))
            : 0,
        varianceType:
          variance > 0 ? 'POSITIVE' : variance < 0 ? 'NEGATIVE' : 'ZERO',
        varianceReason:
          data.varianceReason !== undefined
            ? data.varianceReason
            : item.varianceReason,
        improvementMeasures:
          data.improvementMeasures !== undefined
            ? data.improvementMeasures
            : item.improvementMeasures,
        responsiblePerson:
          data.reviewedBy !== undefined
            ? data.reviewedBy
            : item.responsiblePerson,
        responsiblePersonName: (() => {
          const u = userOptions.find(
            (u) =>
              u.id ===
              (data.reviewedBy !== undefined
                ? data.reviewedBy
                : item.responsiblePerson)
          )
          return u ? u.name : item.responsiblePersonName
        })(),
      })
      return { code: 1, msg: '更新成功', data: item }
    },
  },
  // 删除分析
  {
    url: '/glkj/accountant/variance/delete/:id',
    type: 'delete',
    response(req) {
      const item = mockVariances.find((v) => v.id === req.params.id)
      if (!item) return { code: 0, msg: '记录不存在' }
      item.delFlag = 1
      return { code: 1, msg: '删除成功' }
    },
  },
  // 导出（整体/选中）
  {
    url: '/glkj/accountant/variance/export',
    type: 'post',
    response() {
      return { code: 1, msg: '导出成功' }
    },
  },
  // 导出单个
  {
    url: '/glkj/accountant/variance/export/:id',
    type: 'get',
    response() {
      return { code: 1, msg: '导出成功' }
    },
  },
  // 执行分析
  {
    url: '/glkj/accountant/variance/analyze',
    type: 'post',
    response() {
      return { code: 1, msg: '分析完成' }
    },
  },
  // 报告
  {
    url: '/glkj/accountant/variance/report',
    type: 'post',
    response() {
      return { code: 1, msg: '查询成功', data: {} }
    },
  },
]
