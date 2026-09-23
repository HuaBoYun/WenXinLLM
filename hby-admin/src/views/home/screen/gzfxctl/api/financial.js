/**
 * 国资财经运行总览数据接口
 */

// 模拟数据 - 实际项目中应该调用真实API
export function getFinancialData() {
  return new Promise((resolve) => {
    setTimeout(() => {
      resolve({
        code: 200,
        message: '成功',
        data: {
          // 概览数据
          overview: {
            totalIncome: 789.24,
            incomeTrend: -17.23,
            receivableTotal: 2048,
            receivableRate: 1.23,
            payableTotal: 2048,
            payableRate: -1.23,
            costTotal: 789.24,
            costTrend: -17.23
          },
          // 应收数据
          receivableData: {
            total: 2048,
            rate: 1.23,
            trendData: [
              { month: '1月', value: 20 },
              { month: '2月', value: 50 },
              { month: '3月', value: 75 },
              { month: '4月', value: 100 },
              { month: '5月', value: 75 },
              { month: '6月', value: 150 },
              { month: '7月', value: 100 },
              { month: '8月', value: 50 },
              { month: '9月', value: 25 },
              { month: '10月', value: 0 }
            ],
            industries: [
              { name: '钢铁', value: 123654 },
              { name: '能源', value: 123525 },
              { name: '交通', value: 42523 },
              { name: '建筑', value: 145624 },
              { name: '金融', value: 123525 },
              { name: '其他', value: 123525 }
            ]
          },
          // 应付数据
          payableData: {
            total: 2048,
            rate: -1.23,
            trendData: [
              { month: '1月', value: 0 },
              { month: '2月', value: 25 },
              { month: '3月', value: 50 },
              { month: '4月', value: 100 },
              { month: '5月', value: 75 },
              { month: '6月', value: 150 },
              { month: '7月', value: 100 },
              { month: '8月', value: 75 },
              { month: '9月', value: 50 },
              { month: '10月', value: 25 }
            ],
            industries: [
              { name: '钢铁', value: 123654 },
              { name: '能源', value: 123525 },
              { name: '交通', value: 42523 },
              { name: '建筑', value: 145624 },
              { name: '金融', value: 123525 },
              { name: '其他', value: 123525 }
            ]
          },
          // 收入数据
          incomeData: {
            total: 789.24,
            trend: -17.23
          },
          // 地球仪数据
          globeData: {
            amount: 1617.46,
            rate: -3.2
          },
          // 企业列表
          companies: {
            top: ['河钢集团', '开滦集团', '冀中能源', '河北交投', '河北国控'],
            bottom: ['河北建投', '河北港工', '唐山三友', '河北水发', '河北建投']
          },
          // 成本费用数据
          costData: {
            total: 789.24,
            trend: -17.23,
            trendData: [
              { month: '1月', cost: 100, expense: 75 },
              { month: '2月', cost: 75, expense: 50 },
              { month: '3月', cost: 105, expense: 75 },
              { month: '4月', cost: 100, expense: 100 },
              { month: '5月', cost: 75, expense: 50 },
              { month: '6月', cost: 100, expense: 75 },
              { month: '7月', cost: 75, expense: 50 },
              { month: '8月', cost: 50, expense: 25 },
              { month: '9月', cost: 25, expense: 0 },
              { month: '10月', cost: 0, expense: 25 }
            ],
            industries: [
              { name: '钢铁', value: 123654 },
              { name: '能源', value: 123525 },
              { name: '交通', value: 42523 },
              { name: '建筑', value: 145624 },
              { name: '金融', value: 123525 },
              { name: '其他', value: 123525 }
            ]
          },
          // 重点企业列表
          keyCompanies: [
            { id: 1, name: '河钢集团', status: 'normal', statusText: '正常' },
            { id: 2, name: '开滦集团', status: 'normal', statusText: '正常' },
            { id: 3, name: '冀中能源', status: 'warning', statusText: '预警' },
            { id: 4, name: '河北交投', status: 'normal', statusText: '正常' },
            { id: 5, name: '河北国控', status: 'normal', statusText: '正常' },
            { id: 6, name: '河北建投', status: 'normal', statusText: '正常' },
            { id: 7, name: '河北港工', status: 'normal', statusText: '正常' },
            { id: 8, name: '唐山三友', status: 'normal', statusText: '正常' },
            { id: 9, name: '河北水发', status: 'normal', statusText: '正常' },
            { id: 10, name: '河北建投', status: 'normal', statusText: '正常' },
            { id: 11, name: '河北地电', status: 'normal', statusText: '正常' },
            { id: 12, name: '河北国资', status: 'normal', statusText: '正常' },
            { id: 13, name: '河北物资', status: 'normal', statusText: '正常' },
            { id: 14, name: '河北矿业', status: 'normal', statusText: '正常' },
            { id: 15, name: '河北租赁', status: 'normal', statusText: '正常' }
          ]
        }
      })
    }, 500)
  })
}

/**
 * 获取应收账款详情
 */
export function getReceivableDetail(params) {
  return new Promise((resolve) => {
    setTimeout(() => {
      resolve({
        code: 200,
        message: '成功',
        data: {
          total: 2048,
          rate: 1.23,
          list: [
            {
              companyName: '河钢集团',
              amount: 350.5,
              overdueDays: 0,
              status: 'normal',
              updateTime: '2024-01-15 10:30:00'
            },
            {
              companyName: '开滦集团',
              amount: 280.3,
              overdueDays: 15,
              status: 'warning',
              updateTime: '2024-01-15 09:45:00'
            },
            {
              companyName: '冀中能源',
              amount: 420.8,
              overdueDays: 45,
              status: 'danger',
              updateTime: '2024-01-15 11:20:00'
            }
          ]
        }
      })
    }, 300)
  })
}

/**
 * 获取应付账款详情
 */
export function getPayableDetail(params) {
  return new Promise((resolve) => {
    setTimeout(() => {
      resolve({
        code: 200,
        message: '成功',
        data: {
          total: 2048,
          rate: -1.23,
          list: [
            {
              companyName: '河钢集团',
              amount: 320.5,
              overdueDays: 0,
              status: 'normal',
              updateTime: '2024-01-15 10:30:00'
            },
            {
              companyName: '开滦集团',
              amount: 260.3,
              overdueDays: 10,
              status: 'warning',
              updateTime: '2024-01-15 09:45:00'
            },
            {
              companyName: '冀中能源',
              amount: 380.8,
              overdueDays: 30,
              status: 'danger',
              updateTime: '2024-01-15 11:20:00'
            }
          ]
        }
      })
    }, 300)
  })
}

/**
 * 获取成本费用详情
 */
export function getCostDetail(params) {
  return new Promise((resolve) => {
    setTimeout(() => {
      resolve({
        code: 200,
        message: '成功',
        data: {
          total: 789.24,
          trend: -17.23,
          breakdown: {
            materialCost: 350.5,
            laborCost: 180.3,
            manufacturingCost: 120.8,
            managementExpense: 80.5,
            salesExpense: 40.2,
            financialExpense: 16.94
          },
          list: [
            {
              industry: '钢铁',
              cost: 123654,
              expense: 45678,
              total: 169332,
              rate: -5.2
            },
            {
              industry: '能源',
              cost: 123525,
              expense: 42356,
              total: 165881,
              rate: -3.8
            },
            {
              industry: '交通',
              cost: 42523,
              expense: 15678,
              total: 58201,
              rate: 2.5
            }
          ]
        }
      })
    }, 300)
  })
}

/**
 * 获取企业财务状况
 */
export function getCompanyFinancial(companyId) {
  return new Promise((resolve) => {
    setTimeout(() => {
      resolve({
        code: 200,
        message: '成功',
        data: {
          companyId: companyId,
          companyName: '河钢集团',
          income: 350.5,
          receivable: 120.3,
          payable: 98.7,
          cost: 280.5,
          profit: 70.0,
          assetLiabilityRatio: 65.5,
          currentRatio: 1.35,
          quickRatio: 0.98,
          riskLevel: 'low',
          riskWarnings: [
            {
              type: '逾期应收',
              level: 'medium',
              amount: 15.5,
              description: '存在部分逾期应收账款'
            }
          ]
        }
      })
    }, 300)
  })
}

