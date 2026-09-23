/**
 * 信用风险数据接口
 */

/**
 * 获取完整的信用风险数据
 */
export function getCreditRiskData() {
  return new Promise((resolve) => {
    setTimeout(() => {
      resolve({
        // 集团公司信用排名
        creditRankingList: [
          { id: 1, company: 'xxxx建设控股有限公司', level: '一级', score: 635, date: '2018/11/15 17:50:00' },
          { id: 2, company: 'xxxx汽车有限公司', level: '一级', score: 621, date: '2018/11/15 17:50:00' },
          { id: 3, company: 'xxxx投资发展有限公司', level: '一级', score: 614, date: '2018/11/15 17:50:00' },
          { id: 4, company: 'xxxx汽车集团有限公司', level: '一级', score: 598, date: '2018/11/15 17:50:00' },
          { id: 5, company: 'xxxx集团有限公司', level: '一级', score: 576, date: '2018/11/15 17:50:00' },
          { id: 6, company: 'xxxx建设开发有限公司', level: '一级', score: 555, date: '2018/11/15 17:50:00' },
          { id: 7, company: 'xxxx集团有限公司', level: '一级', score: 554, date: '2018/11/15 17:50:00' },
          { id: 8, company: 'xxxx汽车集团有限公司', level: '一级', score: 550, date: '2018/11/15 17:50:00' },
          { id: 9, company: 'xxxx建设集团有限公司', level: '一级', score: 545, date: '2018/11/15 17:50:00' },
          { id: 10, company: 'xxxx汽车集团有限公司', level: '一级', score: 525, date: '2018/11/15 17:50:00' }
        ],
        // 集团单位信用画像（关系图）
        creditPortraitGraph: {
          nodes: [
            { id: 'center', name: 'xxxxxxx有限公司', value: 100, category: 0 },
            { id: 'node1', name: 'xxxx建设控股有限公司', value: 80, category: 1 },
            { id: 'node2', name: 'xxxxxx工程有限公司', value: 70, category: 1 },
            { id: 'node3', name: 'xxxx投资发展有限公司', value: 60, category: 1 },
            { id: 'node4', name: 'xxxx汽车有限公司', value: 50, category: 2 },
            { id: 'node5', name: 'xxxx建设开发有限公司', value: 40, category: 2 },
            { id: 'node6', name: 'xxxx集团有限公司', value: 30, category: 2 },
            { id: 'node7', name: '北方重工集团1000万元', value: 20, category: 3 },
            { id: 'node8', name: 'xxxxxxx有限公司', value: 20, category: 3 },
            { id: 'node9', name: 'xxxxxxx有限公司', value: 20, category: 3 }
          ],
          links: [
            { source: 'center', target: 'node1' },
            { source: 'center', target: 'node2' },
            { source: 'center', target: 'node3' },
            { source: 'node1', target: 'node4' },
            { source: 'node1', target: 'node5' },
            { source: 'node2', target: 'node6' },
            { source: 'node3', target: 'node7' },
            { source: 'node4', target: 'node8' },
            { source: 'node5', target: 'node9' }
          ],
          categories: [
            { name: '核心企业' },
            { name: '高风险' },
            { name: '中风险' },
            { name: '低风险' }
          ]
        },
        // 风险事件预警
        riskEventWarningList: [
          { id: 1, company: 'xxxx建设控股有限公司' },
          { id: 2, company: 'xxxxxx工程有限公司' },
          { id: 3, company: 'xxxx投资发展有限公司' },
          { id: 4, company: 'xxxx汽车有限公司' },
          { id: 5, company: 'xxxx建设开发有限公司' },
          { id: 6, company: 'xxxx集团有限公司' }
        ],
        // 二级单位信用评分（柱状图）
        unitCreditScoreChart: {
          categories: ['集团总部', '建设公司', '投资公司', '汽车公司', '工程公司', '开发公司', '科技公司', '金融公司', '贸易公司', '物流公司', '能源公司', '环保公司', '文化公司', '教育公司', '医疗公司'],
          data: [340, 320, 310, 280, 260, 240, 220, 200, 180, 160, 140, 120, 100, 80, 60],
          highlight: 330
        },
        // 风险事件类型（环形图）
        riskEventTypeStats: [
          { name: '1级', value: 32, percent: 32 },
          { name: '2级', value: 18, percent: 18 },
          { name: '3级', value: 5, percent: 5 },
          { name: '4级', value: 30, percent: 30 },
          { name: '5级', value: 15, percent: 15 }
        ],
        // 信用风险等级（环形图）
        creditRiskLevelStats: [
          { name: '零级', value: 32, percent: 32 },
          { name: '1级', value: 18, percent: 18 },
          { name: '2级', value: 5, percent: 5 },
          { name: '3级', value: 30, percent: 30 },
          { name: '4级', value: 15, percent: 15 }
        ],
        // 负面信息分布（环形图）
        negativeInfoStats: [
          { name: '诉讼', value: 40, percent: 40 },
          { name: '欠款', value: 30, percent: 30 },
          { name: '违约', value: 30, percent: 30 }
        ],
        // 风险事件数量及金额查询
        riskEventQuery: {
          eventCount: 470,
          eventAmount: 300
        },
        // 整体统计
        overallStats: {
          companyCount: 89,
          riskCount: 340,
          aaaCount: 470,
          relationCount: 230
        }
      })
    }, 300)
  })
}

/**
 * 获取集团公司信用排名
 */
export function getCreditRankingList() {
  return getCreditRiskData().then(data => data.creditRankingList)
}

/**
 * 获取集团单位信用画像
 */
export function getCreditPortraitGraph() {
  return getCreditRiskData().then(data => data.creditPortraitGraph)
}

/**
 * 获取风险事件预警列表
 */
export function getRiskEventWarningList() {
  return getCreditRiskData().then(data => data.riskEventWarningList)
}

/**
 * 获取二级单位信用评分
 */
export function getUnitCreditScoreChart() {
  return getCreditRiskData().then(data => data.unitCreditScoreChart)
}

/**
 * 获取风险事件类型统计
 */
export function getRiskEventTypeStats() {
  return getCreditRiskData().then(data => data.riskEventTypeStats)
}

/**
 * 获取信用风险等级统计
 */
export function getCreditRiskLevelStats() {
  return getCreditRiskData().then(data => data.creditRiskLevelStats)
}

/**
 * 获取负面信息分布统计
 */
export function getNegativeInfoStats() {
  return getCreditRiskData().then(data => data.negativeInfoStats)
}

/**
 * 获取风险事件数量及金额查询
 */
export function getRiskEventQuery() {
  return getCreditRiskData().then(data => data.riskEventQuery)
}

/**
 * 获取整体统计数据
 */
export function getOverallStats() {
  return getCreditRiskData().then(data => data.overallStats)
}

