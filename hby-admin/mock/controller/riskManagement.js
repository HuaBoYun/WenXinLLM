let mockDataList = [
  {
    riskTypeId: 1,
    riskTypeCode: 'RISK-MKT-001',
    riskTypeName: '汇率波动风险',
    riskCategory: 'MARKET',
    riskDescription: '因汇率波动导致的外币资产或负债价值变化的风险',
    impactLevel: 'HIGH',
    probabilityLevel: 'MEDIUM',
    riskLevel: 'HIGH',
    controlMeasures: '建立汇率风险对冲机制，使用远期合约和期权工具',
    monitoringIndicators: '汇率波动幅度、外汇敞口金额',
    responseStrategy: '触发预警时立即启动对冲操作',
    responsibleDepartment: '资金管理部',
    responsiblePerson: '张伟',
    reviewFrequency: 'MONTHLY',
    nextReviewDate: '2026-04-15',
    isEnabled: 1,
    remark: '',
    createTime: '2025-06-10 09:30:00',
    updateTime: '2026-03-01 14:20:00',
  },
  {
    riskTypeId: 2,
    riskTypeCode: 'RISK-CRD-001',
    riskTypeName: '交易对手信用风险',
    riskCategory: 'CREDIT',
    riskDescription: '交易对手违约导致的信用损失风险',
    impactLevel: 'CRITICAL',
    probabilityLevel: 'LOW',
    riskLevel: 'HIGH',
    controlMeasures: '定期评估交易对手信用等级，设置授信额度',
    monitoringIndicators: '交易对手信用评级变化、应收账款逾期率',
    responseStrategy: '降低授信额度或暂停交易',
    responsibleDepartment: '风控部',
    responsiblePerson: '李明',
    reviewFrequency: 'QUARTERLY',
    nextReviewDate: '2026-06-30',
    isEnabled: 1,
    remark: '重点关注境外交易对手',
    createTime: '2025-03-20 10:00:00',
    updateTime: '2026-02-15 11:45:00',
  },
  {
    riskTypeId: 3,
    riskTypeCode: 'RISK-LIQ-001',
    riskTypeName: '资金流动性风险',
    riskCategory: 'LIQUIDITY',
    riskDescription: '因资金流动性不足导致无法满足支付义务的风险',
    impactLevel: 'HIGH',
    probabilityLevel: 'MEDIUM',
    riskLevel: 'HIGH',
    controlMeasures: '建立流动性储备，制定应急融资计划',
    monitoringIndicators: '流动性覆盖率(LCR)、净稳定资金比率(NSFR)',
    responseStrategy: '启动应急融资预案，调整资产配置',
    responsibleDepartment: '资金管理部',
    responsiblePerson: '王芳',
    reviewFrequency: 'MONTHLY',
    nextReviewDate: '2026-04-01',
    isEnabled: 1,
    remark: '',
    createTime: '2025-05-15 08:30:00',
    updateTime: '2026-03-10 09:15:00',
  },
  {
    riskTypeId: 4,
    riskTypeCode: 'RISK-OPR-001',
    riskTypeName: '系统操作风险',
    riskCategory: 'OPERATIONAL',
    riskDescription: '因信息系统故障或人为操作失误导致的损失风险',
    impactLevel: 'MEDIUM',
    probabilityLevel: 'HIGH',
    riskLevel: 'MEDIUM',
    controlMeasures: '建立双机热备系统，定期进行灾备演练',
    monitoringIndicators: '系统可用率、操作失误率',
    responseStrategy: '启动备份系统，排查故障原因',
    responsibleDepartment: '信息技术部',
    responsiblePerson: '赵刚',
    reviewFrequency: 'QUARTERLY',
    nextReviewDate: '2026-06-15',
    isEnabled: 1,
    remark: '',
    createTime: '2025-07-01 14:00:00',
    updateTime: '2026-01-20 16:30:00',
  },
  {
    riskTypeId: 5,
    riskTypeCode: 'RISK-CMP-001',
    riskTypeName: '反洗钱合规风险',
    riskCategory: 'COMPLIANCE',
    riskDescription: '因违反反洗钱法规导致的处罚和声誉损失风险',
    impactLevel: 'CRITICAL',
    probabilityLevel: 'LOW',
    riskLevel: 'HIGH',
    controlMeasures: '建立完善的客户尽职调查和可疑交易监测机制',
    monitoringIndicators: '可疑交易报告数、合规检查通过率',
    responseStrategy: '立即上报监管部门，启动内部调查',
    responsibleDepartment: '合规部',
    responsiblePerson: '孙丽',
    reviewFrequency: 'MONTHLY',
    nextReviewDate: '2026-04-10',
    isEnabled: 1,
    remark: '需持续关注监管政策变化',
    createTime: '2025-04-10 11:00:00',
    updateTime: '2026-03-05 10:20:00',
  },
  {
    riskTypeId: 6,
    riskTypeCode: 'RISK-REP-001',
    riskTypeName: '品牌声誉风险',
    riskCategory: 'REPUTATION',
    riskDescription: '因负面事件导致的企业品牌和声誉受损风险',
    impactLevel: 'MEDIUM',
    probabilityLevel: 'LOW',
    riskLevel: 'LOW',
    controlMeasures: '建立舆情监控系统，制定危机公关预案',
    monitoringIndicators: '舆情负面指数、客户投诉率',
    responseStrategy: '启动危机公关预案，积极沟通回应',
    responsibleDepartment: '品牌部',
    responsiblePerson: '周婷',
    reviewFrequency: 'SEMI_ANNUALLY',
    nextReviewDate: '2026-07-01',
    isEnabled: 0,
    remark: '当前风险等级较低',
    createTime: '2025-08-20 15:30:00',
    updateTime: '2026-02-28 09:00:00',
  },
]

let nextId = 7

module.exports = [
  {
    url: '/qqsk/risk-management/risk-types/list',
    type: 'get',
    response(req) {
      const {
        pageNum = 1,
        pageSize = 10,
        riskTypeCode,
        riskTypeName,
        riskCategory,
        impactLevel,
        probabilityLevel,
        isEnabled,
      } = req.query || {}
      let filteredList = [...mockDataList]
      if (riskTypeCode) {
        filteredList = filteredList.filter((item) =>
          item.riskTypeCode.toLowerCase().includes(riskTypeCode.toLowerCase())
        )
      }
      if (riskTypeName) {
        filteredList = filteredList.filter((item) =>
          item.riskTypeName.includes(riskTypeName)
        )
      }
      if (riskCategory) {
        filteredList = filteredList.filter(
          (item) => item.riskCategory === riskCategory
        )
      }
      if (impactLevel) {
        filteredList = filteredList.filter(
          (item) => item.impactLevel === impactLevel
        )
      }
      if (probabilityLevel) {
        filteredList = filteredList.filter(
          (item) => item.probabilityLevel === probabilityLevel
        )
      }
      if (isEnabled !== null && isEnabled !== undefined && isEnabled !== '') {
        filteredList = filteredList.filter(
          (item) => String(item.isEnabled) === String(isEnabled)
        )
      }
      const start = (pageNum - 1) * pageSize
      const end = start + parseInt(pageSize)
      const pagedList = filteredList.slice(start, end)
      return {
        code: 1,
        msg: 'success',
        data: pagedList,
        result: {
          total: filteredList.length,
          pageNum: parseInt(pageNum),
          pageSize: parseInt(pageSize),
        },
      }
    },
  },
  {
    url: '/qqsk/risk-management/risk-types/statistics',
    type: 'get',
    response() {
      const enabled = mockDataList.filter((i) => i.isEnabled === 1)
      const highRisk = mockDataList.filter(
        (i) => i.riskLevel === 'HIGH' || i.riskLevel === 'CRITICAL'
      )
      const critical = mockDataList.filter(
        (i) => i.riskLevel === 'CRITICAL' || i.impactLevel === 'CRITICAL'
      )
      const categoryMap = {}
      mockDataList.forEach((item) => {
        categoryMap[item.riskCategory] =
          (categoryMap[item.riskCategory] || 0) + 1
      })
      return {
        code: 1,
        msg: 'success',
        data: {
          totalRiskTypes: mockDataList.length,
          enabledRiskTypes: enabled.length,
          highRiskTypes: highRisk.length,
          criticalRiskTypes: critical.length,
          categoryDistribution: categoryMap,
          trendData: [
            { month: '2025-10', count: 3 },
            { month: '2025-11', count: 4 },
            { month: '2025-12', count: 4 },
            { month: '2026-01', count: 5 },
            { month: '2026-02', count: 6 },
            { month: '2026-03', count: mockDataList.length },
          ],
        },
      }
    },
  },
  {
    url: '/qqsk/risk-management/risk-types',
    type: 'post',
    response(req) {
      const newItem = {
        ...req.body,
        riskTypeId: nextId++,
        riskLevel: calcRiskLevel(
          req.body.impactLevel,
          req.body.probabilityLevel
        ),
        createTime: new Date().toLocaleString(),
        updateTime: new Date().toLocaleString(),
      }
      mockDataList.unshift(newItem)
      return { code: 1, msg: '新增成功', data: newItem }
    },
  },
  {
    url: '/qqsk/risk-management/risk-types',
    type: 'put',
    response(req) {
      const updateData = req.body
      const index = mockDataList.findIndex(
        (item) =>
          item.riskTypeId === updateData.riskTypeId ||
          item.riskTypeId === Number(updateData.riskTypeId)
      )
      if (index === -1) {
        return { code: 0, msg: '记录不存在' }
      }
      mockDataList[index] = {
        ...mockDataList[index],
        ...updateData,
        riskLevel: calcRiskLevel(
          updateData.impactLevel || mockDataList[index].impactLevel,
          updateData.probabilityLevel || mockDataList[index].probabilityLevel
        ),
        updateTime: new Date().toLocaleString(),
      }
      return { code: 1, msg: '修改成功', data: mockDataList[index] }
    },
  },
  {
    url: '/qqsk/risk-management/risk-types',
    type: 'delete',
    response(req) {
      const urlParts = req.path.split('/')
      const idsStr = urlParts[urlParts.length - 1]
      const ids = idsStr.split(',').map((id) => parseInt(id))
      const beforeLen = mockDataList.length
      mockDataList = mockDataList.filter(
        (item) => !ids.includes(item.riskTypeId)
      )
      const deletedCount = beforeLen - mockDataList.length
      return { code: 1, msg: '删除成功，共删除' + deletedCount + '条' }
    },
  },
  {
    url: '/qqsk/risk-management/risk-types/export',
    type: 'get',
    response() {
      return {
        code: 1,
        msg: '导出成功',
        data: mockDataList,
      }
    },
  },
]

function calcRiskLevel(impact, probability) {
  const levels = { LOW: 1, MEDIUM: 2, HIGH: 3, CRITICAL: 4 }
  const score = (levels[impact] || 1) * (levels[probability] || 1)
  if (score >= 9) return 'CRITICAL'
  if (score >= 6) return 'HIGH'
  if (score >= 3) return 'MEDIUM'
  return 'LOW'
}
