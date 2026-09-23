/**
 * 预算对比分析 Mock 接口
 */
let idCounter = 200

const organizationOptions = [
  {
    id: 'ORG001',
    name: '集团总部',
    value: 'ORG001',
    label: '集团总部',
    children: [
      { id: 'ORG002', name: '财务部', value: 'ORG002', label: '财务部' },
      { id: 'ORG003', name: '销售部', value: 'ORG003', label: '销售部' },
      {
        id: 'ORG004',
        name: '人力资源部',
        value: 'ORG004',
        label: '人力资源部',
      },
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
      {
        id: 'ORG012',
        name: '技术支持部',
        value: 'ORG012',
        label: '技术支持部',
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
      {
        id: 'ORG022',
        name: '渠道运营部',
        value: 'ORG022',
        label: '渠道运营部',
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

const mockComparisons = [
  {
    id: 'CMP001',
    comparisonItem: '2026年Q1差旅费对比',
    organizationId: 'ORG003',
    organizationName: '销售部',
    budgetAccountId: 'ACC001',
    budgetAccountName: '差旅费',
    baseValue: 120000,
    compareValue: 105000,
    difference: -15000,
    changeRate: -12.5,
    significance: 'MEDIUM',
    comparisonResult: 'BETTER',
    valueType: 'AMOUNT',
    analysisDate: '2026-04-01',
    comparisonType: 'PERIOD',
    confidence: 92,
    influenceFactors: '业务量减少',
    recommendations: '优化差旅路线',
    riskAssessment: '低风险',
    delFlag: 0,
  },
  {
    id: 'CMP002',
    comparisonItem: '2026年Q1营销费用对比',
    organizationId: 'ORG021',
    organizationName: '品牌推广部',
    budgetAccountId: 'ACC002',
    budgetAccountName: '营销费用',
    baseValue: 200000,
    compareValue: 235000,
    difference: 35000,
    changeRate: 17.5,
    significance: 'HIGH',
    comparisonResult: 'WORSE',
    valueType: 'AMOUNT',
    analysisDate: '2026-04-01',
    comparisonType: 'PERIOD',
    confidence: 88,
    influenceFactors: '新产品推广增加',
    recommendations: '加强预算控制',
    riskAssessment: '中风险',
    delFlag: 0,
  },
  {
    id: 'CMP003',
    comparisonItem: '2026年Q1研发费用对比',
    organizationId: 'ORG011',
    organizationName: '产品研发部',
    budgetAccountId: 'ACC003',
    budgetAccountName: '研发费用',
    baseValue: 350000,
    compareValue: 342000,
    difference: -8000,
    changeRate: -2.29,
    significance: 'LOW',
    comparisonResult: 'SIMILAR',
    valueType: 'AMOUNT',
    analysisDate: '2026-04-01',
    comparisonType: 'BUDGET_ACTUAL',
    confidence: 95,
    influenceFactors: '项目进度正常',
    recommendations: '维持现有管控',
    riskAssessment: '低风险',
    delFlag: 0,
  },
  {
    id: 'CMP004',
    comparisonItem: '2026年Q1人力成本对比',
    organizationId: 'ORG001',
    organizationName: '集团总部',
    budgetAccountId: 'ACC004',
    budgetAccountName: '人力成本',
    baseValue: 580000,
    compareValue: 612000,
    difference: 32000,
    changeRate: 5.52,
    significance: 'MEDIUM',
    comparisonResult: 'WORSE',
    valueType: 'AMOUNT',
    analysisDate: '2026-04-01',
    comparisonType: 'PERIOD',
    confidence: 90,
    influenceFactors: '新增招聘',
    recommendations: '控制招聘节奏',
    riskAssessment: '中风险',
    delFlag: 0,
  },
  {
    id: 'CMP005',
    comparisonItem: '2026年Q1办公经费对比',
    organizationId: 'ORG002',
    organizationName: '财务部',
    budgetAccountId: 'ACC005',
    budgetAccountName: '办公经费',
    baseValue: 85000,
    compareValue: 83000,
    difference: -2000,
    changeRate: -2.35,
    significance: 'NONE',
    comparisonResult: 'SIMILAR',
    valueType: 'AMOUNT',
    analysisDate: '2026-03-28',
    comparisonType: 'BUDGET_ACTUAL',
    confidence: 97,
    influenceFactors: '预算执行良好',
    recommendations: '维持现有措施',
    riskAssessment: '低风险',
    delFlag: 0,
  },
  {
    id: 'CMP006',
    comparisonItem: '2026年Q1采购费用对比',
    organizationId: 'ORG020',
    organizationName: '市场中心',
    budgetAccountId: 'ACC006',
    budgetAccountName: '采购费用',
    baseValue: 260000,
    compareValue: 310000,
    difference: 50000,
    changeRate: 19.23,
    significance: 'HIGH',
    comparisonResult: 'WORSE',
    valueType: 'AMOUNT',
    analysisDate: '2026-03-28',
    comparisonType: 'PERIOD',
    confidence: 85,
    influenceFactors: '原材料涨价',
    recommendations: '寻找替代供应商',
    riskAssessment: '高风险',
    delFlag: 0,
  },
  {
    id: 'CMP007',
    comparisonItem: '2026年Q1运营费用对比',
    organizationId: 'ORG010',
    organizationName: '研发中心',
    budgetAccountId: 'ACC007',
    budgetAccountName: '运营费用',
    baseValue: 150000,
    compareValue: 128000,
    difference: -22000,
    changeRate: -14.67,
    significance: 'MEDIUM',
    comparisonResult: 'BETTER',
    valueType: 'AMOUNT',
    analysisDate: '2026-03-28',
    comparisonType: 'DEPARTMENT',
    confidence: 91,
    influenceFactors: '系统优化降本',
    recommendations: '继续推进优化',
    riskAssessment: '低风险',
    delFlag: 0,
  },
  {
    id: 'CMP008',
    comparisonItem: '2026年Q1财务管理费对比',
    organizationId: 'ORG002',
    organizationName: '财务部',
    budgetAccountId: 'ACC008',
    budgetAccountName: '财务管理费',
    baseValue: 95000,
    compareValue: 98000,
    difference: 3000,
    changeRate: 3.16,
    significance: 'LOW',
    comparisonResult: 'SIMILAR',
    valueType: 'AMOUNT',
    analysisDate: '2026-03-25',
    comparisonType: 'BUDGET_ACTUAL',
    confidence: 94,
    influenceFactors: '审计费用微增',
    recommendations: '关注费用趋势',
    riskAssessment: '低风险',
    delFlag: 0,
  },
  {
    id: 'CMP009',
    comparisonItem: '销售部门环比对比',
    organizationId: 'ORG003',
    organizationName: '销售部',
    budgetAccountId: 'ACC002',
    budgetAccountName: '营销费用',
    baseValue: 180000,
    compareValue: 195000,
    difference: 15000,
    changeRate: 8.33,
    significance: 'MEDIUM',
    comparisonResult: 'WORSE',
    valueType: 'AMOUNT',
    analysisDate: '2026-03-25',
    comparisonType: 'DEPARTMENT',
    confidence: 89,
    influenceFactors: '季度促销活动',
    recommendations: '评估促销效果',
    riskAssessment: '中风险',
    delFlag: 0,
  },
  {
    id: 'CMP010',
    comparisonItem: '技术支持部运营对比',
    organizationId: 'ORG012',
    organizationName: '技术支持部',
    budgetAccountId: 'ACC007',
    budgetAccountName: '运营费用',
    baseValue: 110000,
    compareValue: 102000,
    difference: -8000,
    changeRate: -7.27,
    significance: 'LOW',
    comparisonResult: 'BETTER',
    valueType: 'AMOUNT',
    analysisDate: '2026-03-20',
    comparisonType: 'PERIOD',
    confidence: 93,
    influenceFactors: '自动化工具引入',
    recommendations: '扩大自动化范围',
    riskAssessment: '低风险',
    delFlag: 0,
  },
  {
    id: 'CMP011',
    comparisonItem: '人力资源部差旅对比',
    organizationId: 'ORG004',
    organizationName: '人力资源部',
    budgetAccountId: 'ACC001',
    budgetAccountName: '差旅费',
    baseValue: 65000,
    compareValue: 72000,
    difference: 7000,
    changeRate: 10.77,
    significance: 'MEDIUM',
    comparisonResult: 'WORSE',
    valueType: 'AMOUNT',
    analysisDate: '2026-03-20',
    comparisonType: 'BUDGET_ACTUAL',
    confidence: 87,
    influenceFactors: '校招出差增加',
    recommendations: '优化招聘渠道',
    riskAssessment: '低风险',
    delFlag: 0,
  },
  {
    id: 'CMP012',
    comparisonItem: '渠道运营部营销对比',
    organizationId: 'ORG022',
    organizationName: '渠道运营部',
    budgetAccountId: 'ACC002',
    budgetAccountName: '营销费用',
    baseValue: 175000,
    compareValue: 168000,
    difference: -7000,
    changeRate: -4.0,
    significance: 'LOW',
    comparisonResult: 'BETTER',
    valueType: 'AMOUNT',
    analysisDate: '2026-03-15',
    comparisonType: 'PERIOD',
    confidence: 92,
    influenceFactors: '渠道整合效果',
    recommendations: '继续渠道优化',
    riskAssessment: '低风险',
    delFlag: 0,
  },
  {
    id: 'CMP013',
    comparisonItem: '集团总部人力同比',
    organizationId: 'ORG001',
    organizationName: '集团总部',
    budgetAccountId: 'ACC004',
    budgetAccountName: '人力成本',
    baseValue: 520000,
    compareValue: 580000,
    difference: 60000,
    changeRate: 11.54,
    significance: 'HIGH',
    comparisonResult: 'WORSE',
    valueType: 'AMOUNT',
    analysisDate: '2026-03-15',
    comparisonType: 'PERIOD',
    confidence: 86,
    influenceFactors: '薪资调整',
    recommendations: '优化人员结构',
    riskAssessment: '高风险',
    delFlag: 0,
  },
  {
    id: 'CMP014',
    comparisonItem: '产品研发部采购对比',
    organizationId: 'ORG011',
    organizationName: '产品研发部',
    budgetAccountId: 'ACC006',
    budgetAccountName: '采购费用',
    baseValue: 220000,
    compareValue: 215000,
    difference: -5000,
    changeRate: -2.27,
    significance: 'NONE',
    comparisonResult: 'SIMILAR',
    valueType: 'AMOUNT',
    analysisDate: '2026-03-10',
    comparisonType: 'BUDGET_ACTUAL',
    confidence: 96,
    influenceFactors: '采购计划执行良好',
    recommendations: '维持现有策略',
    riskAssessment: '低风险',
    delFlag: 0,
  },
  {
    id: 'CMP015',
    comparisonItem: '品牌推广部办公对比',
    organizationId: 'ORG021',
    organizationName: '品牌推广部',
    budgetAccountId: 'ACC005',
    budgetAccountName: '办公经费',
    baseValue: 55000,
    compareValue: 58000,
    difference: 3000,
    changeRate: 5.45,
    significance: 'LOW',
    comparisonResult: 'SIMILAR',
    valueType: 'AMOUNT',
    analysisDate: '2026-03-10',
    comparisonType: 'DEPARTMENT',
    confidence: 93,
    influenceFactors: '办公设备更新',
    recommendations: '制定设备更新计划',
    riskAssessment: '低风险',
    delFlag: 0,
  },
  {
    id: 'CMP016',
    comparisonItem: '财务部运营费用环比',
    organizationId: 'ORG002',
    organizationName: '财务部',
    budgetAccountId: 'ACC007',
    budgetAccountName: '运营费用',
    baseValue: 78000,
    compareValue: 75000,
    difference: -3000,
    changeRate: -3.85,
    significance: 'NONE',
    comparisonResult: 'BETTER',
    valueType: 'AMOUNT',
    analysisDate: '2026-03-05',
    comparisonType: 'PERIOD',
    confidence: 95,
    influenceFactors: '流程优化',
    recommendations: '持续改进',
    riskAssessment: '低风险',
    delFlag: 0,
  },
  {
    id: 'CMP017',
    comparisonItem: '市场中心差旅费同比',
    organizationId: 'ORG020',
    organizationName: '市场中心',
    budgetAccountId: 'ACC001',
    budgetAccountName: '差旅费',
    baseValue: 135000,
    compareValue: 158000,
    difference: 23000,
    changeRate: 17.04,
    significance: 'HIGH',
    comparisonResult: 'WORSE',
    valueType: 'AMOUNT',
    analysisDate: '2026-03-05',
    comparisonType: 'PERIOD',
    confidence: 84,
    influenceFactors: '市场拓展出差增加',
    recommendations: '视频会议替代',
    riskAssessment: '中风险',
    delFlag: 0,
  },
  {
    id: 'CMP018',
    comparisonItem: '研发中心研发费预实',
    organizationId: 'ORG010',
    organizationName: '研发中心',
    budgetAccountId: 'ACC003',
    budgetAccountName: '研发费用',
    baseValue: 400000,
    compareValue: 385000,
    difference: -15000,
    changeRate: -3.75,
    significance: 'LOW',
    comparisonResult: 'BETTER',
    valueType: 'AMOUNT',
    analysisDate: '2026-03-01',
    comparisonType: 'BUDGET_ACTUAL',
    confidence: 94,
    influenceFactors: '项目延期节省',
    recommendations: '加快项目进度',
    riskAssessment: '低风险',
    delFlag: 0,
  },
  {
    id: 'CMP019',
    comparisonItem: '销售部财务管理费对比',
    organizationId: 'ORG003',
    organizationName: '销售部',
    budgetAccountId: 'ACC008',
    budgetAccountName: '财务管理费',
    baseValue: 45000,
    compareValue: 47000,
    difference: 2000,
    changeRate: 4.44,
    significance: 'NONE',
    comparisonResult: 'SIMILAR',
    valueType: 'AMOUNT',
    analysisDate: '2026-03-01',
    comparisonType: 'DEPARTMENT',
    confidence: 96,
    influenceFactors: '正常波动',
    recommendations: '无需调整',
    riskAssessment: '低风险',
    delFlag: 0,
  },
  {
    id: 'CMP020',
    comparisonItem: '集团总部采购费环比',
    organizationId: 'ORG001',
    organizationName: '集团总部',
    budgetAccountId: 'ACC006',
    budgetAccountName: '采购费用',
    baseValue: 300000,
    compareValue: 345000,
    difference: 45000,
    changeRate: 15.0,
    significance: 'HIGH',
    comparisonResult: 'WORSE',
    valueType: 'AMOUNT',
    analysisDate: '2026-02-28',
    comparisonType: 'PERIOD',
    confidence: 83,
    influenceFactors: '大宗采购集中',
    recommendations: '分散采购时间',
    riskAssessment: '高风险',
    delFlag: 0,
  },
]

const getActiveList = () => mockComparisons.filter((v) => v.delFlag === 0)

const calcStats = () => {
  const list = getActiveList()
  const highSig = list.filter((v) => v.significance === 'HIGH').length
  const totalBase = list.reduce((s, v) => s + (v.baseValue || 0), 0)
  const totalDiff = list.reduce((s, v) => s + Math.abs(v.difference || 0), 0)
  const accuracy =
    totalBase > 0 ? (((totalBase - totalDiff) / totalBase) * 100).toFixed(1) : 0
  const allAccounts = budgetAccountOptions.length
  const coveredAccounts = [...new Set(list.map((v) => v.budgetAccountId))]
    .length
  const coverage =
    allAccounts > 0 ? ((coveredAccounts / allAccounts) * 100).toFixed(1) : 0
  return {
    totalComparisons: list.length,
    significantDifferences: highSig,
    accuracy: parseFloat(accuracy),
    coverage: parseFloat(coverage),
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
  // ===== 分页查询对比分析列表 =====
  {
    url: '/glkj/accountant/budget/analysis/comparison/execute',
    type: 'post',
    response(req) {
      const {
        pageNum = 1,
        pageSize = 20,
        comparisonType,
        organizationPath,
        budgetAccount,
        basePeriod,
        comparePeriod,
        action,
        id,
      } = req.body || {}

      // 删除操作
      if (action === 'delete' && id) {
        const item = mockComparisons.find((v) => v.id === id)
        if (item) {
          item.delFlag = 1
          return { code: 1, msg: '删除成功' }
        }
        return { code: 0, msg: '记录不存在' }
      }

      // 创建操作（含 comparisonName）
      if (req.body && req.body.comparisonName) {
        const allOrgs = organizationOptions.flatMap((o) => [
          o,
          ...(o.children || []),
        ])
        const matchedOrg = allOrgs.find(
          (o) =>
            o.id === req.body.organizationId ||
            o.value === req.body.organizationId
        )
        const matchedAcc = budgetAccountOptions.find(
          (a) =>
            a.id === req.body.budgetAccountId ||
            a.value === req.body.budgetAccountId
        )
        const newItem = {
          id: 'CMP' + String(idCounter++).padStart(3, '0'),
          comparisonItem: req.body.comparisonName,
          organizationId: req.body.organizationId || '',
          organizationName: matchedOrg
            ? matchedOrg.name || matchedOrg.label
            : '',
          budgetAccountId: req.body.budgetAccountId || '',
          budgetAccountName: matchedAcc
            ? matchedAcc.name || matchedAcc.label
            : '',
          baseValue: 0,
          compareValue: 0,
          difference: 0,
          changeRate: 0,
          significance: '',
          comparisonResult: '',
          valueType: 'AMOUNT',
          analysisDate: new Date().toISOString().slice(0, 10),
          comparisonType: req.body.comparisonType || 'PERIOD',
          confidence: 90,
          influenceFactors: '',
          recommendations: '',
          riskAssessment: '低风险',
          delFlag: 0,
        }
        mockComparisons.unshift(newItem)
        return { code: 1, msg: '创建成功', data: newItem }
      }

      // 查询操作
      let list = getActiveList()
      if (comparisonType && comparisonType !== 'ALL')
        list = list.filter((v) => v.comparisonType === comparisonType)
      if (organizationPath)
        list = list.filter((v) => v.organizationId === organizationPath)
      if (budgetAccount)
        list = list.filter((v) => v.budgetAccountId === budgetAccount)
      const start = (pageNum - 1) * pageSize
      const tlist = list.slice(start, start + pageSize)
      return {
        code: 1,
        msg: '查询成功',
        data: { tlist, totalRecord: list.length },
      }
    },
  },

  // ===== 统计数据 =====
  {
    url: '/glkj/accountant/budget/analysis/comparison/stats',
    type: 'get',
    response() {
      return { code: 1, msg: '查询成功', data: calcStats() }
    },
  },

  // ===== 组织单元选项 =====
  {
    url: '/glkj/accountant/budget/analysis/comparison/organizations',
    type: 'get',
    response() {
      return { code: 1, msg: '查询成功', data: organizationOptions }
    },
  },

  // ===== 预算科目选项 =====
  {
    url: '/glkj/accountant/budget/analysis/comparison/budget-accounts',
    type: 'get',
    response() {
      return { code: 1, msg: '查询成功', data: budgetAccountOptions }
    },
  },

  // ===== 图表数据 =====
  {
    url: '/glkj/accountant/budget/analysis/comparison/chart-data',
    type: 'post',
    response() {
      const list = getActiveList()
      const accountMap = {}
      list.forEach((v) => {
        if (!accountMap[v.budgetAccountName]) {
          accountMap[v.budgetAccountName] = { base: 0, compare: 0, count: 0 }
        }
        accountMap[v.budgetAccountName].base += v.baseValue
        accountMap[v.budgetAccountName].compare += v.compareValue
        accountMap[v.budgetAccountName].count++
      })
      const xAxis = Object.keys(accountMap)
      const baseData = xAxis.map((k) =>
        Math.round(accountMap[k].base / accountMap[k].count)
      )
      const compareData = xAxis.map((k) =>
        Math.round(accountMap[k].compare / accountMap[k].count)
      )
      const diffData = xAxis.map((k, i) => compareData[i] - baseData[i])
      return {
        code: 1,
        msg: '查询成功',
        data: {
          comparisonChart: {
            xAxis,
            series: [
              { name: '基准值', data: baseData },
              { name: '对比值', data: compareData },
            ],
          },
          differenceChart: {
            xAxis,
            series: [{ name: '差异值', data: diffData }],
          },
        },
      }
    },
  },

  // ===== 更新 =====
  {
    url: '/glkj/accountant/budget/analysis/comparison/update',
    type: 'post',
    response(req) {
      const {
        id,
        comparisonName,
        comparisonType,
        organizationId,
        organizationName,
        budgetAccountId,
        budgetAccountName,
        baseValue,
        compareValue,
      } = req.body || {}
      const item = mockComparisons.find((v) => v.id === id)
      if (!item) return { code: 0, msg: '记录不存在' }
      if (comparisonName !== undefined) item.comparisonItem = comparisonName
      if (comparisonType !== undefined) item.comparisonType = comparisonType
      if (organizationId !== undefined) item.organizationId = organizationId
      if (organizationName !== undefined)
        item.organizationName = organizationName
      if (budgetAccountId !== undefined) item.budgetAccountId = budgetAccountId
      if (budgetAccountName !== undefined)
        item.budgetAccountName = budgetAccountName
      if (baseValue !== undefined) item.baseValue = Number(baseValue)
      if (compareValue !== undefined) item.compareValue = Number(compareValue)
      // 重新计算差异
      item.difference = item.compareValue - item.baseValue
      item.changeRate =
        item.baseValue > 0
          ? parseFloat(((item.difference / item.baseValue) * 100).toFixed(2))
          : 0
      item.significance =
        Math.abs(item.changeRate) > 15
          ? 'HIGH'
          : Math.abs(item.changeRate) > 5
          ? 'MEDIUM'
          : 'LOW'
      item.comparisonResult =
        item.changeRate > 5
          ? 'WORSE'
          : item.changeRate < -5
          ? 'BETTER'
          : 'SIMILAR'
      return { code: 1, msg: '更新成功', data: item }
    },
  },

  // ===== 批量对比 =====
  {
    url: '/glkj/accountant/budget/analysis/comparison/batch',
    type: 'post',
    response(req) {
      const { ids } = req.body || {}
      if (!ids || ids.length < 2)
        return { code: 0, msg: '请至少选择两条数据进行批量对比' }
      const items = mockComparisons.filter(
        (v) => ids.includes(v.id) && v.delFlag !== 1
      )
      if (items.length < 2) return { code: 0, msg: '有效记录不足两条' }
      const totalBase = items.reduce((s, v) => s + v.baseValue, 0)
      const totalCompare = items.reduce((s, v) => s + v.compareValue, 0)
      const totalDiff = totalCompare - totalBase
      const totalRate =
        totalBase > 0
          ? parseFloat(((totalDiff / totalBase) * 100).toFixed(2))
          : 0
      return {
        code: 1,
        msg: '批量对比成功',
        data: {
          totalBase,
          totalCompare,
          totalDifference: totalDiff,
          totalChangeRate: totalRate,
          count: items.length,
          details: items.map((v) => ({
            id: v.id,
            comparisonItem: v.comparisonItem,
            baseValue: v.baseValue,
            compareValue: v.compareValue,
            difference: v.difference,
            changeRate: v.changeRate,
          })),
        },
      }
    },
  },

  // ===== 导出 =====
  {
    url: '/glkj/accountant/budget/analysis/comparison/export',
    type: 'post',
    response() {
      return {
        code: 1,
        msg: '导出成功',
        data: { fileName: '对比分析报告_' + Date.now() + '.xlsx' },
      }
    },
  },

  // ===== 对比图表(钻取详情) =====
  {
    url: '/glkj/accountant/budget/analysis/comparison/chart',
    type: 'post',
    response(req) {
      const { id } = req.body || {}
      const item = mockComparisons.find((v) => v.id === id)
      if (!item) return { code: 0, msg: '记录不存在' }
      return {
        code: 1,
        msg: '查询成功',
        data: {
          ...item,
          details: [],
        },
      }
    },
  },

  // ===== 删除 =====
  {
    url: '/glkj/accountant/budget/analysis/comparison/delete',
    type: 'post',
    response(req) {
      const { id, ids } = req.body || {}
      const deleteIds = ids || (id ? [id] : [])
      let count = 0
      deleteIds.forEach((did) => {
        const item = mockComparisons.find((v) => v.id === did)
        if (item) {
          item.delFlag = 1
          count++
        }
      })
      return { code: 1, msg: `删除成功，共删除${count}条` }
    },
  },
]
