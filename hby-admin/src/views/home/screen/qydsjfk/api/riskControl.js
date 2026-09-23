/**
 * 企业大数据智能风控平台数据接口
 */

// 模拟数据 - 实际项目中应该调用真实API
export function getRiskControlData() {
  return new Promise((resolve) => {
    setTimeout(() => {
      resolve({
        code: 200,
        message: '成功',
        data: {
          // 概览数据
          overview: {
            unpaidCount: 6000,
            unpaidTrend: -5.2,
            completionRate: 47,
            completionTrend: 3.5,
            auditWarningCount: 330,
            auditWarningTrend: -2.1,
            foreignFunds: 400,
            foreignFundsTrend: 1.8
          },
          // 未缴纳件数趋势
          unpaidTrend: {
            months: ['2016/1', '2016/7', '2017/1', '2017/7', '2018/1', '2018/7', '2019/1', '2019/7', '2019/10', '2019/11', '2019/12'],
            data: [1000, 2000, 3000, 3500, 4000, 4500, 3000, 2000, 1500, 1000, 500]
          },
          // 新增/存量件数对比
          compareData: {
            months: ['2016/1', '2016/7', '2017/1', '2017/7', '2018/1', '2018/7', '2019/1', '2019/7', '2019/10', '2019/11', '2019/12'],
            newCases: [500, 800, 1200, 1500, 1800, 2000, 1500, 1000, 800, 600, 400],
            stockCases: [500, 1200, 1800, 2000, 2200, 2500, 1500, 1000, 700, 400, 100]
          },
          // 办结率对比
          completionData: {
            months: ['2016/1', '2016/7', '2017/1', '2017/7', '2018/1', '2018/7', '2019/1', '2019/7', '2019/10', '2019/11', '2019/12'],
            rate1: [50, 75, 100, 125, 150, 175, 200, 225, 250, 225, 200],
            rate2: [100, 125, 150, 175, 200, 225, 250, 275, 300, 275, 250],
            rate3: [75, 100, 125, 150, 175, 200, 225, 250, 275, 250, 225]
          },
          // 未缴纳件数及金额
          unpaidAmountData: {
            categories: ['名词1', '名词2', '名词3', '名词4', '名词5', '名词6', '名词7', '名词8', '名词9'],
            counts: [100, 150, 200, 180, 220, 190, 210, 170, 160],
            amounts: [200, 250, 300, 280, 320, 290, 310, 270, 260]
          },
          // 审计预警件数
          auditWarningData: {
            categories: ['名词1', '名词2', '名词3', '名词4', '名词5', '名词6', '名词7', '名词8', '名词9'],
            data: [100, 200, 300, 400, 500, 400, 300, 200, 100]
          },
          // 审计预警分布
          auditDistribution: [
            { value: 1000, name: '1000-2000万' },
            { value: 800, name: '500-1000万' },
            { value: 600, name: '300-500万' },
            { value: 400, name: '100-300万' }
          ],
          // 游外资件分布
          foreignFundsDistribution: [
            { value: 560, name: '有游外资' },
            { value: 40, name: '无游外资' }
          ],
          // 申报地分布
          regionDistribution: {
            categories: ['0-3个月', '3-6个月', '6-12个月', '12-24个月', '24-36个月', '36个月以上'],
            region1: [25, 50, 75, 50, 25, 0],
            region2: [50, 75, 100, 75, 50, 25],
            region3: [75, 100, 125, 100, 75, 50]
          }
        }
      })
    }, 500)
  })
}

/**
 * 获取未缴纳件数详情
 */
export function getUnpaidDetail(params) {
  return new Promise((resolve) => {
    setTimeout(() => {
      resolve({
        code: 200,
        message: '成功',
        data: {
          total: 6000,
          list: [
            {
              id: 1,
              companyName: '企业A',
              unpaidCount: 150,
              unpaidAmount: 350.5,
              overdueDays: 45,
              status: 'danger',
              updateTime: '2024-01-15 10:30:00'
            },
            {
              id: 2,
              companyName: '企业B',
              unpaidCount: 120,
              unpaidAmount: 280.3,
              overdueDays: 30,
              status: 'warning',
              updateTime: '2024-01-15 09:45:00'
            },
            {
              id: 3,
              companyName: '企业C',
              unpaidCount: 80,
              unpaidAmount: 180.8,
              overdueDays: 15,
              status: 'normal',
              updateTime: '2024-01-15 11:20:00'
            }
          ]
        }
      })
    }, 300)
  })
}

/**
 * 获取审计预警详情
 */
export function getAuditWarningDetail(params) {
  return new Promise((resolve) => {
    setTimeout(() => {
      resolve({
        code: 200,
        message: '成功',
        data: {
          total: 330,
          list: [
            {
              id: 1,
              warningType: '财务风险',
              companyName: '企业A',
              warningLevel: 'high',
              warningContent: '资产负债率超过80%',
              createTime: '2024-01-15 10:30:00'
            },
            {
              id: 2,
              warningType: '合规风险',
              companyName: '企业B',
              warningLevel: 'medium',
              warningContent: '未按时提交审计报告',
              createTime: '2024-01-15 09:45:00'
            },
            {
              id: 3,
              warningType: '经营风险',
              companyName: '企业C',
              warningLevel: 'low',
              warningContent: '营业收入同比下降15%',
              createTime: '2024-01-15 11:20:00'
            }
          ]
        }
      })
    }, 300)
  })
}

/**
 * 获取办结率统计
 */
export function getCompletionRateStats(params) {
  return new Promise((resolve) => {
    setTimeout(() => {
      resolve({
        code: 200,
        message: '成功',
        data: {
          currentRate: 47,
          targetRate: 80,
          completedCount: 2820,
          totalCount: 6000,
          trend: [
            { month: '2019/1', rate: 35 },
            { month: '2019/2', rate: 38 },
            { month: '2019/3', rate: 40 },
            { month: '2019/4', rate: 42 },
            { month: '2019/5', rate: 44 },
            { month: '2019/6', rate: 45 },
            { month: '2019/7', rate: 46 },
            { month: '2019/8', rate: 46.5 },
            { month: '2019/9', rate: 47 }
          ]
        }
      })
    }, 300)
  })
}

/**
 * 获取游外资件统计
 */
export function getForeignFundsStats(params) {
  return new Promise((resolve) => {
    setTimeout(() => {
      resolve({
        code: 200,
        message: '成功',
        data: {
          totalFunds: 400,
          hasForeignFunds: 560,
          noForeignFunds: 40,
          distribution: [
            { region: '北京', amount: 120, count: 180 },
            { region: '上海', amount: 100, count: 150 },
            { region: '广州', amount: 80, count: 120 },
            { region: '深圳', amount: 60, count: 80 },
            { region: '其他', amount: 40, count: 30 }
          ]
        }
      })
    }, 300)
  })
}

/**
 * 获取申报地分布统计
 */
export function getRegionDistributionStats(params) {
  return new Promise((resolve) => {
    setTimeout(() => {
      resolve({
        code: 200,
        message: '成功',
        data: {
          total: 6000,
          distribution: [
            { region: '河北', count: 1500, percentage: 25 },
            { region: '北京', count: 1200, percentage: 20 },
            { region: '天津', count: 900, percentage: 15 },
            { region: '山东', count: 800, percentage: 13.3 },
            { region: '河南', count: 700, percentage: 11.7 },
            { region: '其他', count: 900, percentage: 15 }
          ],
          ageDistribution: [
            { age: '0-3个月', count: 1500 },
            { age: '3-6个月', count: 1200 },
            { age: '6-12个月', count: 1000 },
            { age: '12-24个月', count: 800 },
            { age: '24-36个月', count: 600 },
            { age: '36个月以上', count: 900 }
          ]
        }
      })
    }, 300)
  })
}

/**
 * 获取风险预警统计
 */
export function getRiskWarningStats(params) {
  return new Promise((resolve) => {
    setTimeout(() => {
      resolve({
        code: 200,
        message: '成功',
        data: {
          total: 330,
          highRisk: 80,
          mediumRisk: 150,
          lowRisk: 100,
          typeDistribution: [
            { type: '财务风险', count: 120, percentage: 36.4 },
            { type: '合规风险', count: 90, percentage: 27.3 },
            { type: '经营风险', count: 70, percentage: 21.2 },
            { type: '法律风险', count: 50, percentage: 15.1 }
          ]
        }
      })
    }, 300)
  })
}

