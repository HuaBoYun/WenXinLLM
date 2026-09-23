/**
 * 国资风险穿透监控 API 服务
 * 对接34个风险模型数据
 */

import request from '@/utils/request'

/**
 * 获取风险监控总览数据
 */
export function getRiskOverview() {
  return request({
    url: '/api/risk/overview',
    method: 'get'
  })
}

/**
 * 获取投资风险数据
 * 包含：对外投资预测、无关多元化、境外投资、资产闲置
 */
export function getInvestmentRisk() {
  return request({
    url: '/api/risk/investment',
    method: 'get'
  })
}

/**
 * 获取集团管控风险数据
 * 包含：控股不控权、多层架构、超股比担保、违规挂靠
 */
export function getGroupControlRisk() {
  return request({
    url: '/api/risk/group-control',
    method: 'get'
  })
}

/**
 * 获取财务风险数据
 * 包含：过度负债、财务金融风险、应付账款敞口、应收账款风险
 */
export function getFinancialRisk() {
  return request({
    url: '/api/risk/financial',
    method: 'get'
  })
}

/**
 * 获取信用风险数据
 * 包含：信用风控监测、债务风险预测、贷款违约风险、对外借款预测、担保预测
 */
export function getCreditRisk() {
  return request({
    url: '/api/risk/credit',
    method: 'get'
  })
}

/**
 * 获取经营风险数据
 * 包含：经营潜亏风险、合同经营风险、资金头寸预警、薪酬乱象
 */
export function getBusinessRisk() {
  return request({
    url: '/api/risk/business',
    method: 'get'
  })
}

/**
 * 获取采购风险数据
 * 包含：招标采购风险、违规招投标、靠企吃企、超合同支付、违规公款消费
 */
export function getProcurementRisk() {
  return request({
    url: '/api/risk/procurement',
    method: 'get'
  })
}

/**
 * 获取法律风险数据
 * 包含：合规内控风险、捞偏门、法律诉讼
 */
export function getLegalRisk() {
  return request({
    url: '/api/risk/legal',
    method: 'get'
  })
}

/**
 * 获取贸易风险数据
 * 包含：虚假贸易（融资性）、虚假贸易（空转）
 */
export function getTradeRisk() {
  return request({
    url: '/api/risk/trade',
    method: 'get'
  })
}

/**
 * 获取所有风险监控数据（一次性获取）
 */
export function getRiskMonitorData() {
  return request({
    url: '/api/risk/monitor/all',
    method: 'get'
  }).then(response => {
    // 如果后端未实现，返回模拟数据
    if (!response || response.code === 404) {
      return getMockRiskData()
    }
    return response.data
  }).catch(() => {
    // 接口失败时返回模拟数据
    return getMockRiskData()
  })
}

/**
 * 模拟数据生成函数
 * 用于开发和演示
 */
function getMockRiskData() {
  return {
    // 风险总览
    overview: {
      criticalCount: 5,
      highCount: 12,
      mediumCount: 23,
      lowCount: 8,
      categoryData: [
        { name: '投资风险', value: 8 },
        { name: '集团管控', value: 6 },
        { name: '财务风险', value: 10 },
        { name: '信用风险', value: 7 },
        { name: '经营风险', value: 9 },
        { name: '采购风险', value: 5 },
        { name: '法律风险', value: 3 },
        { name: '贸易风险', value: 4 }
      ],
      trendData: {
        months: ['7月', '8月', '9月', '10月', '11月', '12月'],
        critical: [3, 4, 5, 4, 6, 5],
        high: [8, 10, 12, 11, 13, 12],
        medium: [15, 18, 20, 22, 24, 23],
        low: [5, 6, 7, 8, 9, 8]
      }
    },
    
    // 投资风险
    investment: {
      externalInvestmentRisk: 5,  // 对外投资预测风险
      diversificationRisk: 3,      // 无关多元化风险
      overseasRisk: 2,             // 境外投资风险
      assetIdleRisk: 4,            // 资产闲置风险
      details: [
        {
          projectName: '某新能源项目投资',
          riskType: '对外投资',
          riskLevel: '高',
          amount: 5000,
          status: '待处理'
        },
        {
          projectName: '某房地产项目',
          riskType: '无关多元',
          riskLevel: '中',
          amount: 3200,
          status: '处理中'
        },
        {
          projectName: '某境外矿产投资',
          riskType: '境外投资',
          riskLevel: '严重',
          amount: 8000,
          status: '待处理'
        },
        {
          projectName: '某闲置厂房',
          riskType: '资产闲置',
          riskLevel: '中',
          amount: 1500,
          status: '已处理'
        }
      ]
    },
    
    // 集团管控风险
    groupControl: {
      controlRisk: 3,        // 控股不控权
      hierarchyRisk: 5,      // 多层架构
      guaranteeRisk: 4,      // 超股比担保
      affiliationRisk: 2     // 违规挂靠
    },
    
    // 财务风险
    financial: {
      debtRisk: 6,           // 过度负债
      debtRatio: '78%',      // 资产负债率
      financialRisk: 4,      // 财务金融风险
      financingCost: '6.5%', // 融资成本
      payableRisk: 8,        // 应付账款敞口
      overdueAmount: '2.3亿', // 逾期金额
      receivableRisk: 5,     // 应收账款风险
      ageingDays: '180天'    // 账龄超期
    },
    
    // 信用风险
    credit: {
      creditMonitoring: 7,   // 信用风控监测
      debtPrediction: 5,     // 债务风险预测
      loanDefault: 6,        // 贷款违约风险
      externalLoan: 4,       // 对外借款预测
      guarantee: 3           // 担保预测
    },
    
    // 经营风险
    business: {
      operatingLoss: 8,      // 经营潜亏风险
      contractRisk: 6,       // 合同经营风险
      cashPosition: 5,       // 资金头寸预警
      salaryIssues: 3        // 薪酬乱象
    },
    
    // 采购风险
    procurement: {
      biddingRisk: 7,        // 招标采购风险
      irregularBidding: 5,   // 违规招投标
      relatedParty: 4,       // 靠企吃企
      overPayment: 3,        // 超合同支付
      publicConsumption: 2   // 违规公款消费
    },
    
    // 法律风险
    legal: {
      complianceRisk: 6,     // 合规内控风险
      illegalBusiness: 4,    // 捞偏门
      litigation: 3          // 法律诉讼
    },
    
    // 贸易风险
    trade: {
      fakeTradeFin: 8,       // 虚假贸易（融资性）
      fakeTradeEmpty: 6      // 虚假贸易（空转）
    }
  }
}

/**
 * 获取风险详情
 * @param {String} riskType - 风险类型
 * @param {String} riskId - 风险ID
 */
export function getRiskDetail(riskType, riskId) {
  return request({
    url: `/api/risk/${riskType}/${riskId}`,
    method: 'get'
  })
}

/**
 * 更新风险状态
 * @param {String} riskId - 风险ID
 * @param {String} status - 新状态
 */
export function updateRiskStatus(riskId, status) {
  return request({
    url: `/api/risk/${riskId}/status`,
    method: 'put',
    data: { status }
  })
}

/**
 * 导出风险报告
 * @param {Object} params - 导出参数
 */
export function exportRiskReport(params) {
  return request({
    url: '/api/risk/export',
    method: 'post',
    data: params,
    responseType: 'blob'
  })
}

