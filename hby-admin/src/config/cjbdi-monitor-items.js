/**
 * CJBDI 客户A外部数据监控项配置
 * categoryId 使用后端内部编号（与 cjbdiTelescope 页面保持一致），非数据库 API_CODE
 * group 与数据库 TBL_CJBDI_DATA_CATEGORY.CATEGORY_GROUP 保持一致
 */

export const CJBDI_MONITOR_ITEMS = [
  // cjbdi_02 企业名录通用版：保存监控时由 MonitoringEdit 自动注入，不参与用户勾选
  // 但反查（如 monitoring 页风险标签列）需要中文名映射，所以在配置中保留一项
  { priceid: 'cjbdi_02', interfacename: '企业名录通用版', categoryId: '02', group: '基础信息' },
  { priceid: 'cjbdi_03', interfacename: '工商基础信息',   categoryId: '03', group: '基础信息' },
  { priceid: 'cjbdi_08', interfacename: '一般纳税人信息', categoryId: '2',  group: '经营信息' },
  { priceid: 'cjbdi_09', interfacename: '投融资事件',     categoryId: '3',  group: '经营信息' },
  { priceid: 'cjbdi_10', interfacename: '招投标信息',     categoryId: '4',  group: '经营信息' },
  { priceid: 'cjbdi_11', interfacename: '舆情信息',       categoryId: '5',  group: '经营信息' },
  { priceid: 'cjbdi_12', interfacename: '行政处罚',       categoryId: '6',  group: '经营风险' },
  { priceid: 'cjbdi_13', interfacename: '环保处罚',       categoryId: '7',  group: '经营风险' },
  { priceid: 'cjbdi_14', interfacename: '欠税公告',       categoryId: '8',  group: '经营风险' },
  { priceid: 'cjbdi_15', interfacename: '动产抵押',       categoryId: '9',  group: '经营风险' },
  { priceid: 'cjbdi_16', interfacename: '股权出质',       categoryId: '10', group: '经营风险' },
  { priceid: 'cjbdi_17', interfacename: '股权冻结',       categoryId: '11', group: '经营风险' },
  { priceid: 'cjbdi_18', interfacename: '军采黑名单',     categoryId: '12', group: '经营风险' },
  { priceid: 'cjbdi_19', interfacename: '政采黑名单',     categoryId: '13', group: '经营风险' },
  { priceid: 'cjbdi_20', interfacename: '涉诉信息',       categoryId: '14', group: '法律风险' },
  { priceid: 'cjbdi_21', interfacename: '失信记录',       categoryId: '15', group: '法律风险' },
  { priceid: 'cjbdi_22', interfacename: '限制高消费',     categoryId: '16', group: '法律风险' },
  { priceid: 'cjbdi_23', interfacename: '不良记录',       categoryId: '17', group: '法律风险' },
  { priceid: 'cjbdi_24', interfacename: '企业纠纷',       categoryId: '18', group: '法律风险' },
  { priceid: 'cjbdi_26', interfacename: '税收违法',       categoryId: '26', group: '经营风险' },
  { priceid: 'cjbdi_27', interfacename: '司法拍卖',       categoryId: '27', group: '法律风险' },
  { priceid: 'cjbdi_28', interfacename: '诉前调解',       categoryId: '28', group: '法律风险' },
  { priceid: 'cjbdi_29', interfacename: '仲裁案件',       categoryId: '29', group: '法律风险' },
  { priceid: 'cjbdi_30', interfacename: '军采失信',       categoryId: '30', group: '经营风险' },
]

/**
 * 根据categoryId获取监控项配置
 * @param {string} categoryId - 分类ID
 * @returns {Object|null} 监控项配置
 */
export function getCjbdiMonitorItemByCategoryId(categoryId) {
  return CJBDI_MONITOR_ITEMS.find(item => item.categoryId === categoryId) || null
}

/**
 * 根据priceid获取监控项配置
 * @param {string} priceid - 价格ID
 * @returns {Object|null} 监控项配置
 */
export function getCjbdiMonitorItemByPriceid(priceid) {
  return CJBDI_MONITOR_ITEMS.find(item => item.priceid === priceid) || null
}

/**
 * 获取所有CJBDI监控项的categoryId列表
 * @returns {string[]} categoryId列表
 */
export function getCjbdiCategoryIds() {
  return CJBDI_MONITOR_ITEMS.map(item => item.categoryId)
}
