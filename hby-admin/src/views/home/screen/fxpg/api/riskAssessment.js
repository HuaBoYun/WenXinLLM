/**
 * 风险评估数据接口
 */

/**
 * 获取完整的风险评估数据
 */
export function getRiskAssessmentData() {
  return new Promise((resolve) => {
    setTimeout(() => {
      resolve({
        // 风险评估性质统计
        assessmentNature: [
          { id: 1, company: 'xxxx建设控股有限公司', item: '重大投资决策' },
          { id: 2, company: 'xxxx汽车有限公司', item: '对外担保事项' },
          { id: 3, company: 'xxxx投资发展有限公司', item: '资产处置' },
          { id: 4, company: 'xxxx汽车集团有限公司', item: '重大合同签订' },
          { id: 5, company: 'xxxx集团有限公司', item: '关联交易' },
          { id: 6, company: 'xxxx建设开发有限公司', item: '融资决策' },
          { id: 7, company: 'xxxx集团有限公司', item: '股权变动' },
          { id: 8, company: 'xxxx汽车集团有限公司', item: '重大投资' },
          { id: 9, company: 'xxxx建设集团有限公司', item: '资产重组' },
          { id: 10, company: 'xxxx汽车集团有限公司', item: '对外投资' }
        ],
        // 高风险评估事项
        highRiskItems: [
          { id: 1, company: 'xxxx建设控股有限公司', item: '重大投资决策', level: '高' },
          { id: 2, company: 'xxxx汽车有限公司', item: '对外担保事项', level: '高' },
          { id: 3, company: 'xxxx投资发展有限公司', item: '资产处置', level: '高' },
          { id: 4, company: 'xxxx汽车集团有限公司', item: '重大合同签订', level: '中' },
          { id: 5, company: 'xxxx集团有限公司', item: '关联交易', level: '高' },
          { id: 6, company: 'xxxx建设开发有限公司', item: '融资决策', level: '中' },
          { id: 7, company: 'xxxx集团有限公司', item: '股权变动', level: '高' },
          { id: 8, company: 'xxxx汽车集团有限公司', item: '重大投资', level: '中' },
          { id: 9, company: 'xxxx建设集团有限公司', item: '资产重组', level: '高' },
          { id: 10, company: 'xxxx汽车集团有限公司', item: '对外投资', level: '高' },
          { id: 11, company: 'xxxx科技有限公司', item: '技术转让', level: '中' },
          { id: 12, company: 'xxxx能源集团', item: '并购重组', level: '高' },
          { id: 13, company: 'xxxx地产公司', item: '土地收购', level: '高' },
          { id: 14, company: 'xxxx金融公司', item: '贷款担保', level: '高' },
          { id: 15, company: 'xxxx制造公司', item: '设备采购', level: '中' }
        ],
        // 风险评估数量（环形图）
        assessmentCount: [
          { name: '已评估', value: 470, itemStyle: { color: '#00d4ff' } },
          { name: '未评估', value: 300, itemStyle: { color: '#1a2744' } }
        ],
        // 事项类型（环形图）
        itemType: [
          { name: '委托1', value: 32, itemStyle: { color: '#00d4ff' } },
          { name: '委托2', value: 12, itemStyle: { color: '#ffd700' } },
          { name: '委托3', value: 5, itemStyle: { color: '#ff6b6b' } },
          { name: '其他', value: 15, itemStyle: { color: '#a78bfa' } }
        ],
        // 事项风险程度（环形图）
        riskLevel: [
          { name: '高', value: 42, itemStyle: { color: '#ff6b6b' } },
          { name: '中', value: 230, itemStyle: { color: '#ffd700' } },
          { name: '低', value: 489, itemStyle: { color: '#00d4ff' } }
        ],
        // 风险评估事项统计（柱状图）
        assessmentStats: {
          categories: ['2018/1', '2018/2', '2018/3', '2018/4', '2018/5', '2018/6'],
          series: [
            { name: '低', values: [500, 600, 550, 650, 600, 700] },
            { name: '中', values: [300, 350, 320, 380, 340, 400] },
            { name: '高', values: [200, 250, 230, 270, 260, 300] }
          ]
        },
        // 最终审议单位级次（环形图）
        unitLevel: [
          { name: '委托1', value: 40, itemStyle: { color: '#00d4ff' } },
          { name: '委托2', value: 30, itemStyle: { color: '#ffd700' } },
          { name: '委托3', value: 15, itemStyle: { color: '#ff6b6b' } },
          { name: '其他', value: 15, itemStyle: { color: '#a78bfa' } }
        ]
      })
    }, 300)
  })
}

/**
 * 获取风险评估性质统计
 */
export function getAssessmentNature() {
  return getRiskAssessmentData().then(data => data.assessmentNature)
}

/**
 * 获取高风险评估事项
 */
export function getHighRiskItems() {
  return getRiskAssessmentData().then(data => data.highRiskItems)
}

/**
 * 获取风险评估数量数据
 */
export function getAssessmentCount() {
  return getRiskAssessmentData().then(data => data.assessmentCount)
}

/**
 * 获取事项类型数据
 */
export function getItemType() {
  return getRiskAssessmentData().then(data => data.itemType)
}

/**
 * 获取事项风险程度数据
 */
export function getRiskLevel() {
  return getRiskAssessmentData().then(data => data.riskLevel)
}

/**
 * 获取风险评估事项统计数据
 */
export function getAssessmentStats() {
  return getRiskAssessmentData().then(data => data.assessmentStats)
}

/**
 * 获取最终审议单位级次数据
 */
export function getUnitLevel() {
  return getRiskAssessmentData().then(data => data.unitLevel)
}

