/**
 * 大数据监测数据接口
 */

/**
 * 获取完整的大数据监测数据
 */
export function getDataMonitorData() {
  return new Promise((resolve) => {
    setTimeout(() => {
      resolve({
        // 成熟度评价等级
        maturityLevels: [
          { name: '重要级别10%', value: 10, percent: 10, label: '10件' },
          { name: '普通级别15%', value: 15, percent: 15, label: '15件' },
          { name: '重要级别20%', value: 20, percent: 20, label: '20件' },
          { name: '重要级别24%', value: 24, percent: 24, label: '24件' },
          { name: '重要级别31%', value: 31, percent: 31, label: '31件' }
        ],
        // 重大经营风险事件总体情况
        majorRiskStats: {
          totalEvents: 26581,
          warningEvents: 15489,
          riskAmount: 165836,
          updateTime: '2018-6-12 12:00:00'
        },
        // 知识图谱数据
        knowledgeGraph: {
          nodes: [
            { id: 'center', name: '核心节点', value: 100, category: 0, symbolSize: 80, x: 0, y: 0 },
            { id: 'node1', name: '节点1', value: 80, category: 1, symbolSize: 60 },
            { id: 'node2', name: '节点2', value: 70, category: 1, symbolSize: 55 },
            { id: 'node3', name: '节点3', value: 60, category: 1, symbolSize: 50 },
            { id: 'node4', name: '节点4', value: 50, category: 2, symbolSize: 45 },
            { id: 'node5', name: '节点5', value: 40, category: 2, symbolSize: 40 },
            { id: 'node6', name: '节点6', value: 30, category: 2, symbolSize: 35 },
            { id: 'node7', name: '节点7', value: 20, category: 3, symbolSize: 30 },
            { id: 'node8', name: '节点8', value: 20, category: 3, symbolSize: 30 }
          ],
          links: [
            { source: 'center', target: 'node1' },
            { source: 'center', target: 'node2' },
            { source: 'center', target: 'node3' },
            { source: 'node1', target: 'node4' },
            { source: 'node2', target: 'node5' },
            { source: 'node3', target: 'node6' },
            { source: 'node4', target: 'node7' },
            { source: 'node5', target: 'node8' }
          ],
          categories: [
            { name: '台风' },
            { name: '外部因' },
            { name: '内部因' },
            { name: '其他' }
          ]
        },
        // 法律案件统计
        legalCases: [
          { name: '未整改', value: 761, percent: 30 },
          { name: '整改中', value: 2571, percent: 40 },
          { name: '已整改', value: 567, percent: 30 }
        ],
        // 各单位划分图（企业列表）
        unitDivision: [
          { id: 1, company: 'XXXXXX企业有限公司' },
          { id: 2, company: 'XXXXXX工程有限公司' },
          { id: 3, company: 'XXXXXXXXX建设有限公司' },
          { id: 4, company: 'XXXXXXX汽车有限公司' },
          { id: 5, company: 'XXXXXXX控股有限公司' }
        ],
        // 重大风险事件列表
        majorRiskEvents: [
          { id: 1, event: 'XXXXXXXXXXXXXXX风险事件', company: 'XXXXXX企业有限公司', time: '2018-6-12 12:00:00' },
          { id: 2, event: 'XXXXXXXXX风险预警', company: 'XXXXXX工程有限公司', time: '2018-6-12 12:00:00' },
          { id: 3, event: 'XXXXXXXXXXXXXX异常', company: 'XXXXXXXXX建设有限公司', time: '2018-6-12 12:00:00' },
          { id: 4, event: 'XXXXXXXXX风险提示', company: 'XXXXXXX汽车有限公司', time: '2018-6-12 12:00:00' },
          { id: 5, event: 'XXXXXXX风险监测', company: 'XXXXXXX控股有限公司', time: '2018-6-12 12:00:00' }
        ],
        // 信用等级占比
        creditLevelRatio: [
          { name: '40%', value: 40, percent: 40 },
          { name: '27%', value: 27, percent: 27 },
          { name: '31%', value: 31, percent: 31 }
        ],
        // 审计问题整改情况
        auditRectification: [
          { type: '整改进行中', count: 39, percent: 39 },
          { type: '整改到期时间', count: 24, percent: 24 },
          { type: '整改已逾期', count: 4, percent: 4 },
          { type: '整改已完成', count: 8, percent: 8 }
        ],
        // 中国地图数据（省份风险分布）
        chinaMapData: [
          { name: '北京', value: 177 },
          { name: '天津', value: 42 },
          { name: '河北', value: 102 },
          { name: '山西', value: 81 },
          { name: '内蒙古', value: 47 },
          { name: '辽宁', value: 67 },
          { name: '吉林', value: 82 },
          { name: '黑龙江', value: 66 },
          { name: '上海', value: 123 },
          { name: '江苏', value: 137 },
          { name: '浙江', value: 115 },
          { name: '安徽', value: 68 },
          { name: '福建', value: 78 },
          { name: '江西', value: 60 },
          { name: '山东', value: 147 },
          { name: '河南', value: 88 },
          { name: '湖北', value: 90 },
          { name: '湖南', value: 81 },
          { name: '广东', value: 201 },
          { name: '广西', value: 60 },
          { name: '海南', value: 35 },
          { name: '重庆', value: 55 },
          { name: '四川', value: 83 },
          { name: '贵州', value: 45 },
          { name: '云南', value: 49 },
          { name: '西藏', value: 15 },
          { name: '陕西', value: 63 },
          { name: '甘肃', value: 39 },
          { name: '青海', value: 21 },
          { name: '宁夏', value: 28 },
          { name: '新疆', value: 41 },
          { name: '台湾', value: 60 },
          { name: '香港', value: 95 },
          { name: '澳门', value: 42 }
        ],
        // 全球地图数据（国家风险分布）
        worldMapData: [
          { name: 'China', value: 2500, coords: [104.195397, 35.86166] },
          { name: 'United States', value: 1800, coords: [-95.712891, 37.09024] },
          { name: 'Japan', value: 1200, coords: [138.252924, 36.204824] },
          { name: 'Germany', value: 900, coords: [10.451526, 51.165691] },
          { name: 'United Kingdom', value: 800, coords: [-3.435973, 55.378051] },
          { name: 'France', value: 750, coords: [2.213749, 46.227638] },
          { name: 'India', value: 700, coords: [78.96288, 20.593684] },
          { name: 'Brazil', value: 650, coords: [-51.92528, -14.235004] },
          { name: 'Russia', value: 600, coords: [105.318756, 61.52401] },
          { name: 'Australia', value: 550, coords: [133.775136, -25.274398] }
        ],
        // 整体统计
        overallStats: {
          totalEvents: 26581,
          warningEvents: 15489,
          regulations: 761,
          autoWarningRatio: 42
        }
      })
    }, 300)
  })
}

/**
 * 获取成熟度评价等级
 */
export function getMaturityLevels() {
  return getDataMonitorData().then(data => data.maturityLevels)
}

/**
 * 获取五大支撑风险事件总体情况
 */
export function getSupportEventStats() {
  return getDataMonitorData().then(data => data.supportEventStats)
}

/**
 * 获取知识图谱数据
 */
export function getKnowledgeGraph() {
  return getDataMonitorData().then(data => data.knowledgeGraph)
}

/**
 * 获取法规数量统计
 */
export function getRegulationStats() {
  return getDataMonitorData().then(data => data.regulationStats)
}

/**
 * 获取事件中应急收储情况
 */
export function getEmergencyStorage() {
  return getDataMonitorData().then(data => data.emergencyStorage)
}

/**
 * 获取生命周期分组
 */
export function getLifecycleGroups() {
  return getDataMonitorData().then(data => data.lifecycleGroups)
}

/**
 * 获取重大风险事件
 */
export function getMajorRiskEvents() {
  return getDataMonitorData().then(data => data.majorRiskEvents)
}

/**
 * 获取自动预警占比
 */
export function getAutoWarningRatio() {
  return getDataMonitorData().then(data => data.autoWarningRatio)
}

/**
 * 获取中国地图数据
 */
export function getChinaMapData() {
  return getDataMonitorData().then(data => data.chinaMapData)
}

/**
 * 获取全球地图数据
 */
export function getWorldMapData() {
  return getDataMonitorData().then(data => data.worldMapData)
}

/**
 * 获取整体统计数据
 */
export function getOverallStats() {
  return getDataMonitorData().then(data => data.overallStats)
}

