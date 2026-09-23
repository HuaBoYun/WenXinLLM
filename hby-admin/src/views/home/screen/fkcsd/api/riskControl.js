/**
 * 企业大数据智能风控平台 - 数据接口
 */

/**
 * 获取风控数据
 */
export function getRiskControlData() {
  return new Promise((resolve) => {
    setTimeout(() => {
      resolve({
        overview: {
          maturityScore: 85,
          maturityTrend: 3.5,
          riskCount: 156,
          riskTrend: -5.2,
          defectTypes: 12,
          defectTrend: -2.1,
          unitRiskCount: 398,
          unitRiskTrend: -1.8
        },
        topRiskList: [
          { companyName: 'XXXXXX建设集团有限公司', riskCount: 156, status: '正常' },
          { companyName: 'XXXXXX有限公司', riskCount: 143, status: '正常' },
          { companyName: 'XXXXXXXXXXXXXX控股有限公司', riskCount: 143, status: '预警' },
          { companyName: 'XXXXXX汽车集团有限公司', riskCount: 123, status: '正常' },
          { companyName: 'XXXXXX集团有限公司', riskCount: 106, status: '正常' },
          { companyName: 'XXXXXXXXXXXXXX开发学院', riskCount: 157, status: '预警' },
          { companyName: 'XXXXXX集团有限公司', riskCount: 107, status: '正常' },
          { companyName: 'XXXXXX汽车集团有限公司', riskCount: 123, status: '正常' },
          { companyName: 'XXXXXX集团有限公司', riskCount: 156, status: '预警' },
          { companyName: 'XXXXXX江苏集团有限公司', riskCount: 157, status: '正常' }
        ],
        maturityTop10: [
          { companyName: 'XXXXXX建设集团有限公司', score: 156 },
          { companyName: 'XXXXXX有限公司', score: 143 },
          { companyName: 'XXXXXXXXXXXXXX控股有限公司', score: 143 },
          { companyName: 'XXXXXX汽车集团有限公司', score: 123 },
          { companyName: 'XXXXXX集团有限公司', score: 106 },
          { companyName: 'XXXXXXXXXXXXXX开发学院', score: 157 },
          { companyName: 'XXXXXX集团有限公司', score: 107 },
          { companyName: 'XXXXXX汽车集团有限公司', score: 123 },
          { companyName: 'XXXXXX集团有限公司', score: 156 },
          { companyName: 'XXXXXX江苏集团有限公司', score: 157 }
        ],
        riskTop10: [
          { companyName: 'XXXXXX建设集团有限公司', riskCount: 156 },
          { companyName: 'XXXXXX有限公司', riskCount: 143 },
          { companyName: 'XXXXXXXXXXXXXX控股有限公司', riskCount: 143 },
          { companyName: 'XXXXXX汽车集团有限公司', riskCount: 123 },
          { companyName: 'XXXXXX集团有限公司', riskCount: 106 },
          { companyName: 'XXXXXXXXXXXXXX开发学院', riskCount: 157 },
          { companyName: 'XXXXXX集团有限公司', riskCount: 107 },
          { companyName: 'XXXXXX汽车集团有限公司', riskCount: 123 },
          { companyName: 'XXXXXX集团有限公司', riskCount: 156 },
          { companyName: 'XXXXXX江苏集团有限公司', riskCount: 157 }
        ],
        maturityDetails: [
          { name: '制度完善度', value: 40, color: '#00d4ff' },
          { name: '执行有效性', value: 40, color: '#ffd700' },
          { name: '技术应用度', value: 10, color: '#00ff88' },
          { name: '人员能力', value: 5, color: '#ff6b6b' },
          { name: '文化建设', value: 5, color: '#a78bfa' }
        ],
        defectData: {
          majorDefect: [
            { name: '已提交', value: 400 },
            { name: '未提交', value: 600 }
          ],
          defectType: [
            { name: '名词1', value: 32 },
            { name: '名词2', value: 18 },
            { name: '名词3', value: 5 },
            { name: '名词4', value: 10 },
            { name: '名词5', value: 15 }
          ],
          defectDistribution: [
            { name: '已提交', value: 470 },
            { name: '未提交', value: 400 }
          ],
          defectSubmit: [
            { name: '基础缺陷', value: 300, percent: 30 },
            { name: '新大缺陷', value: 230, percent: 23 }
          ]
        },
        unitRiskData: generateUnitData(24),
        unitDefectData: generateUnitData(30)
      })
    }, 500)
  })
}

/**
 * 生成单位数据
 */
function generateUnitData(count) {
  const data = []
  for (let i = 1; i <= count; i++) {
    data.push({
      unitName: `单位${i}`,
      value: Math.floor(Math.random() * 300) + 100
    })
  }
  return data
}

/**
 * 获取成熟度评估数据
 */
export function getMaturityData() {
  return new Promise((resolve) => {
    setTimeout(() => {
      resolve({
        score: 85,
        trend: 3.5,
        details: [
          { name: '制度完善度', value: 40, color: '#00d4ff' },
          { name: '执行有效性', value: 40, color: '#ffd700' },
          { name: '技术应用度', value: 10, color: '#00ff88' },
          { name: '人员能力', value: 5, color: '#ff6b6b' },
          { name: '文化建设', value: 5, color: '#a78bfa' }
        ],
        top10: [
          { companyName: 'XXXXXX建设集团有限公司', score: 156 },
          { companyName: 'XXXXXX有限公司', score: 143 },
          { companyName: 'XXXXXXXXXXXXXX控股有限公司', score: 143 },
          { companyName: 'XXXXXX汽车集团有限公司', score: 123 },
          { companyName: 'XXXXXX集团有限公司', score: 106 },
          { companyName: 'XXXXXXXXXXXXXX开发学院', score: 157 },
          { companyName: 'XXXXXX集团有限公司', score: 107 },
          { companyName: 'XXXXXX汽车集团有限公司', score: 123 },
          { companyName: 'XXXXXX集团有限公司', score: 156 },
          { companyName: 'XXXXXX江苏集团有限公司', score: 157 }
        ]
      })
    }, 300)
  })
}

/**
 * 获取风险监测数据
 */
export function getRiskMonitorData() {
  return new Promise((resolve) => {
    setTimeout(() => {
      resolve({
        count: 156,
        trend: -5.2,
        top10: [
          { companyName: 'XXXXXX建设集团有限公司', riskCount: 156 },
          { companyName: 'XXXXXX有限公司', riskCount: 143 },
          { companyName: 'XXXXXXXXXXXXXX控股有限公司', riskCount: 143 },
          { companyName: 'XXXXXX汽车集团有限公司', riskCount: 123 },
          { companyName: 'XXXXXX集团有限公司', riskCount: 106 },
          { companyName: 'XXXXXXXXXXXXXX开发学院', riskCount: 157 },
          { companyName: 'XXXXXX集团有限公司', riskCount: 107 },
          { companyName: 'XXXXXX汽车集团有限公司', riskCount: 123 },
          { companyName: 'XXXXXX集团有限公司', riskCount: 156 },
          { companyName: 'XXXXXX江苏集团有限公司', riskCount: 157 }
        ],
        unitDistribution: generateUnitData(24)
      })
    }, 300)
  })
}

/**
 * 获取缺陷数据
 */
export function getDefectData() {
  return new Promise((resolve) => {
    setTimeout(() => {
      resolve({
        types: 12,
        trend: -2.1,
        majorDefect: [
          { name: '已提交', value: 400 },
          { name: '未提交', value: 600 }
        ],
        typeDistribution: [
          { name: '名词1', value: 32 },
          { name: '名词2', value: 18 },
          { name: '名词3', value: 5 },
          { name: '名词4', value: 10 },
          { name: '名词5', value: 15 }
        ],
        distribution: [
          { name: '已提交', value: 470 },
          { name: '未提交', value: 400 }
        ],
        submit: [
          { name: '基础缺陷', value: 300, percent: 30 },
          { name: '新大缺陷', value: 230, percent: 23 }
        ],
        unitDefects: generateUnitData(30)
      })
    }, 300)
  })
}

/**
 * 获取各单位风险数据
 */
export function getUnitRiskData() {
  return new Promise((resolve) => {
    setTimeout(() => {
      resolve({
        count: 398,
        trend: -1.8,
        distribution: generateUnitData(24)
      })
    }, 300)
  })
}

