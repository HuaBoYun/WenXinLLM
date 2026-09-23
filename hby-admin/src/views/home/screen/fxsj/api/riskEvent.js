/**
 * 风险评估数据接口
 */

/**
 * 获取完整的风险评估数据
 */
export function getRiskEventData() {
  return new Promise((resolve) => {
    setTimeout(() => {
      resolve({
        // 风险事件金额排名
        eventAmountRanking: [
          { id: 1, company: 'xxxx建设控股有限公司', eventType: '合同纠纷', amount: 1000 },
          { id: 2, company: 'xxxx汽车有限公司', eventType: '债务违约', amount: 900 },
          { id: 3, company: 'xxxx投资发展有限公司', eventType: '担保诉讼', amount: 800 },
          { id: 4, company: 'xxxx汽车集团有限公司', eventType: '劳动纠纷', amount: 700 },
          { id: 5, company: 'xxxx集团有限公司', eventType: '知识产权', amount: 600 },
          { id: 6, company: 'xxxx建设开发有限公司', eventType: '合同纠纷', amount: 500 },
          { id: 7, company: 'xxxx集团有限公司', eventType: '债务违约', amount: 400 },
          { id: 8, company: 'xxxx汽车集团有限公司', eventType: '担保诉讼', amount: 300 },
          { id: 9, company: 'xxxx建设集团有限公司', eventType: '劳动纠纷', amount: 200 },
          { id: 10, company: 'xxxx科技有限公司', eventType: '知识产权', amount: 100 }
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
        // 风险事件类型（环形图）
        eventType: [
          { name: '合同纠纷', value: 230, itemStyle: { color: '#00d4ff' } },
          { name: '债务违约', value: 180, itemStyle: { color: '#ffd700' } },
          { name: '担保诉讼', value: 150, itemStyle: { color: '#ff6b6b' } },
          { name: '劳动纠纷', value: 120, itemStyle: { color: '#a78bfa' } },
          { name: '知识产权', value: 81, itemStyle: { color: '#00ff88' } }
        ],
        // 风险事件数量及金额趋势
        eventTrend: {
          categories: ['2018/1', '2018/2', '2018/3', '2018/4', '2018/5', '2018/6', '2018/7', '2018/8', '2018/9', '2018/10', '2018/11', '2018/12'],
          eventCount: [120, 150, 130, 160, 140, 180, 170, 190, 160, 200, 180, 220],
          amount: [3000, 3500, 3200, 4000, 3800, 4500, 4200, 4800, 4300, 5000, 4600, 5500]
        },
        // 风险事件化解情况（环形图）
        eventResolution: [
          { name: '已化解', value: 320, itemStyle: { color: '#00ff88' } },
          { name: '化解中', value: 230, itemStyle: { color: '#ffd700' } },
          { name: '未化解', value: 211, itemStyle: { color: '#ff6b6b' } }
        ],
        // 事件诉讼情况占比（环形图）
        litigation: [
          { name: '进入诉讼', value: 42, itemStyle: { color: '#ff6b6b' } },
          { name: '未进入诉讼', value: 400, itemStyle: { color: '#00d4ff' } }
        ]
      })
    }, 300)
  })
}

/**
 * 获取风险事件金额排名
 */
export function getEventAmountRanking() {
  return getRiskEventData().then(data => data.eventAmountRanking)
}

/**
 * 获取高风险评估事项
 */
export function getHighRiskItems() {
  return getRiskEventData().then(data => data.highRiskItems)
}

/**
 * 获取风险评估数量数据
 */
export function getAssessmentCount() {
  return getRiskEventData().then(data => data.assessmentCount)
}

/**
 * 获取风险事件类型数据
 */
export function getEventType() {
  return getRiskEventData().then(data => data.eventType)
}

/**
 * 获取风险事件数量及金额趋势数据
 */
export function getEventTrend() {
  return getRiskEventData().then(data => data.eventTrend)
}

/**
 * 获取风险事件化解情况数据
 */
export function getEventResolution() {
  return getRiskEventData().then(data => data.eventResolution)
}

/**
 * 获取事件诉讼情况占比数据
 */
export function getLitigation() {
  return getRiskEventData().then(data => data.litigation)
}

