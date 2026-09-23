/**
 * 国资穿透总览数据接口
 */

// 模拟数据 - 实际项目中应该调用真实API
export function getPenetrationData() {
  return new Promise((resolve) => {
    setTimeout(() => {
      resolve({
        code: 200,
        message: '成功',
        data: {
          // 概览数据
          overview: {
            totalCompanies: 1256,
            riskWarnings: 87,
            dataSources: 10,
            modelCount: 34
          },
          // 风险模型数据
          riskModels: [
            { id: 1, name: '虚假贸易', count: 12, level: 'high' },
            { id: 2, name: '逾期应付', count: 8, level: 'medium' },
            { id: 3, name: '违规挂靠', count: 5, level: 'low' },
            { id: 4, name: '违规公款消费', count: 3, level: 'low' },
            { id: 5, name: '靠企吃企', count: 7, level: 'medium' },
            { id: 6, name: '超股比担保', count: 4, level: 'medium' },
            { id: 7, name: '超合同支付', count: 6, level: 'medium' },
            { id: 8, name: '逾期应收', count: 15, level: 'high' },
            { id: 9, name: '财务金融风险', count: 9, level: 'high' },
            { id: 10, name: '控股不控权', count: 2, level: 'low' },
            { id: 11, name: '捞偏门', count: 1, level: 'low' },
            { id: 12, name: '资产闲置', count: 11, level: 'high' },
            { id: 13, name: '境外风险', count: 3, level: 'medium' },
            { id: 14, name: '过度负债', count: 8, level: 'high' },
            { id: 15, name: '无关多元', count: 5, level: 'medium' },
            { id: 16, name: '多层架构', count: 6, level: 'medium' },
            { id: 17, name: '薪酬乱象', count: 4, level: 'low' },
            { id: 18, name: '对外投资', count: 7, level: 'medium' },
            { id: 19, name: '对外借款', count: 5, level: 'medium' },
            { id: 20, name: '债务风险', count: 9, level: 'high' },
            { id: 21, name: '合同经营', count: 6, level: 'medium' },
            { id: 22, name: '应付账款', count: 8, level: 'medium' },
            { id: 23, name: '应收账龄', count: 10, level: 'high' },
            { id: 24, name: '担保预测', count: 4, level: 'medium' },
            { id: 25, name: '招标采购', count: 7, level: 'medium' },
            { id: 26, name: '经营潜亏', count: 5, level: 'medium' },
            { id: 27, name: '贷款违约', count: 3, level: 'low' },
            { id: 28, name: '资金头寸', count: 6, level: 'medium' },
            { id: 29, name: '违规招投标', count: 4, level: 'medium' },
            { id: 30, name: '信用风控', count: 8, level: 'medium' },
            { id: 31, name: '法律诉讼', count: 5, level: 'medium' },
            { id: 32, name: '税务风险', count: 3, level: 'low' },
            { id: 33, name: '环保风险', count: 2, level: 'low' },
            { id: 34, name: '安全生产', count: 4, level: 'medium' }
          ],
          // 社会侧数据资源
          socialDataResources: [
            {
              name: '工商数据',
              desc: '网络、内容、营业、审计',
              status: 'active',
              count: 1256,
              updateTime: '2024-01-15 10:30:00'
            },
            {
              name: '经营数据',
              desc: '收入、成本、利润、审计',
              status: 'active',
              count: 2341,
              updateTime: '2024-01-15 09:45:00'
            },
            {
              name: '财务数据',
              desc: '资产、负债、现金流',
              status: 'active',
              count: 1876,
              updateTime: '2024-01-15 11:20:00'
            },
            {
              name: '征信数据',
              desc: '信用评级、违约记录',
              status: 'active',
              count: 987,
              updateTime: '2024-01-15 08:15:00'
            }
          ],
          // 企业侧数据资源
          enterpriseDataResources: [
            {
              name: '自动网络',
              desc: '自动化数据采集',
              status: 'active',
              count: 3456,
              updateTime: '2024-01-15 10:00:00'
            },
            {
              name: '发票网络',
              desc: '发票数据管理',
              status: 'active',
              count: 5678,
              updateTime: '2024-01-15 09:30:00'
            },
            {
              name: '财务网络',
              desc: '财务数据分析',
              status: 'active',
              count: 2345,
              updateTime: '2024-01-15 11:00:00'
            },
            {
              name: '综合网络',
              desc: '综合数据整合',
              status: 'active',
              count: 4567,
              updateTime: '2024-01-15 10:45:00'
            },
            {
              name: '人员网络',
              desc: '人员信息管理',
              status: 'active',
              count: 1234,
              updateTime: '2024-01-15 09:00:00'
            },
            {
              name: '内部网络',
              desc: '内部数据流转',
              status: 'active',
              count: 3210,
              updateTime: '2024-01-15 08:30:00'
            }
          ],
          // 穿透指标数据
          penetrationIndicators: {
            income: {
              name: '收入',
              value: 125600000,
              trend: 'up',
              rate: 12.5
            },
            cost: {
              name: '成本',
              value: 98700000,
              trend: 'down',
              rate: -3.2
            },
            investment: {
              name: '投入',
              value: 45600000,
              trend: 'up',
              rate: 8.7
            },
            profit: {
              name: '收益',
              value: 26900000,
              trend: 'up',
              rate: 15.3
            }
          },
          // 风险预警统计
          riskWarningStats: {
            critical: 12,
            high: 25,
            medium: 35,
            low: 15
          },
          // 数据源接入状态
          dataSourceStatus: {
            total: 10,
            active: 10,
            inactive: 0,
            error: 0
          }
        }
      })
    }, 500)
  })
}

/**
 * 获取风险模型详情
 */
export function getRiskModelDetail(modelId) {
  return new Promise((resolve) => {
    setTimeout(() => {
      resolve({
        code: 200,
        message: '成功',
        data: {
          id: modelId,
          name: '虚假贸易',
          description: '识别以贸易为名、实为出借资金、无商业实质的虚假贸易风险',
          riskLevel: 'high',
          warningCount: 12,
          rules: [
            '购销合同标的物相同',
            '上下游关系异常',
            '资金流向异常',
            '销售业务结算方式选择汇票、信用证',
            '合同存在垫资、融资等条款',
            '应收、预付款逾期超60天'
          ],
          dataSources: ['资金结算', '客商信息', '合同数据', '发票', '物流信息'],
          cases: [
            {
              companyName: '某贸易公司A',
              riskType: '融资性贸易',
              amount: 5000000,
              status: '待处理',
              createTime: '2024-01-10 14:30:00'
            },
            {
              companyName: '某贸易公司B',
              riskType: '空转走单',
              amount: 3200000,
              status: '处理中',
              createTime: '2024-01-12 09:15:00'
            }
          ]
        }
      })
    }, 300)
  })
}

/**
 * 获取数据资源详情
 */
export function getDataResourceDetail(resourceName) {
  return new Promise((resolve) => {
    setTimeout(() => {
      resolve({
        code: 200,
        message: '成功',
        data: {
          name: resourceName,
          status: 'active',
          totalRecords: 12560,
          updateFrequency: '实时',
          lastUpdateTime: '2024-01-15 11:30:00',
          dataQuality: 98.5,
          fields: [
            { name: '企业名称', type: 'string', required: true },
            { name: '统一社会信用代码', type: 'string', required: true },
            { name: '注册资本', type: 'number', required: true },
            { name: '成立日期', type: 'date', required: true },
            { name: '经营状态', type: 'string', required: true }
          ]
        }
      })
    }, 300)
  })
}

