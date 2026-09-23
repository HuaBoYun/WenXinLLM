import request from '@/utils/request'

/**
 * 安全检查管理API
 * 根据达梦数据库表结构和API文档定义
 * 
 * @author 示例云开发团队
 * @since 2025-01-21
 */

// API基础路径 - 通过网关访问，需要加/contract前缀
const API_BASE_PATH = '/contract/safety/inspection'

/**
 * 分页查询安全检查记录列表
 * @param {Object} data 查询参数
 * @param {Number} data.pageNum 页码
 * @param {Number} data.pageSize 每页大小
 * @param {Number} data.projectId 项目ID
 * @param {Number} data.inspectionType 检查类型(1:日常检查,2:专项检查,3:综合检查)
 * @param {String} data.inspectionDateStart 检查开始日期
 * @param {String} data.inspectionDateEnd 检查结束日期
 * @param {Number} data.hazardLevel 隐患等级(1:一般,2:较大,3:重大,4:特别重大)
 * @param {Number} data.rectificationStatus 整改状态(1:待整改,2:整改中,3:已整改,4:已验收)
 * @param {String} data.keyword 关键词搜索
 */
export function getSafetyInspectionPage(data) {
  return request({
    url: `${API_BASE_PATH}/page`,
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 根据ID查询安全检查详情
 * @param {Number} id 安全检查记录ID
 */
export function getSafetyInspectionById(id) {
  return request({
    url: `${API_BASE_PATH}/${id}`,
    method: 'get'
  })
}

/**
 * 新增安全检查记录
 * @param {Object} data 安全检查数据
 * @param {Number} data.projectId 项目ID
 * @param {String} data.inspectionNo 检查编号
 * @param {String} data.inspectionName 检查名称
 * @param {Number} data.inspectionType 检查类型(1:日常检查,2:专项检查,3:综合检查)
 * @param {String} data.inspectionDate 检查日期
 * @param {Number} data.inspectorId 检查人ID
 * @param {String} data.inspectionLocation 检查地点
 * @param {String} data.inspectionScope 检查范围
 * @param {String} data.safetyStandards 安全标准
 * @param {String} data.inspectionFindings 检查发现
 * @param {String} data.hazardIdentification 隐患识别
 * @param {Number} data.hazardLevel 隐患等级(1:一般,2:较大,3:重大,4:特别重大)
 * @param {String} data.rectificationMeasures 整改措施
 * @param {String} data.rectificationDeadline 整改期限
 * @param {Number} data.rectificationPersonId 整改负责人ID
 * @param {Number} data.emergencyLevel 紧急程度(1:一般,2:紧急,3:特急)
 */
export function createSafetyInspection(data) {
  return request({
    url: `${API_BASE_PATH}/create`,
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 修改安全检查记录
 * @param {Object} data 安全检查数据
 */
export function updateSafetyInspection(data) {
  return request({
    url: `${API_BASE_PATH}/update`,
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 删除安全检查记录
 * @param {Number} id 安全检查记录ID
 */
export function deleteSafetyInspection(id) {
  return request({
    url: `${API_BASE_PATH}/delete/${id}`,
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 批量删除安全检查记录
 * @param {Array} ids 安全检查记录ID数组
 */
export function batchDeleteSafetyInspection(ids) {
  return request({
    url: `${API_BASE_PATH}/batchDelete`,
    method: 'post',
    data: ids,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 安全隐患整改
 * @param {Number} id 安全检查记录ID
 * @param {Object} data 整改信息
 * @param {String} data.rectificationMeasures 整改措施
 * @param {Number} data.rectificationPersonId 整改负责人ID
 */
export function rectifySafetyHazards(id, data) {
  return request({
    url: `${API_BASE_PATH}/rectify/${id}`,
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 安全检查整改处理（简化版）
 * @param {Object} data 整改数据
 * @param {Number} data.id 安全检查记录ID
 * @param {Number} data.rectificationStatus 整改状态
 * @param {String} data.rectificationDescription 整改描述
 */
export function rectifySafetyInspection(data) {
  return request({
    url: `${API_BASE_PATH}/rectify`,
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 安全检查验证
 * @param {Number} id 安全检查记录ID
 * @param {Object} data 验证信息
 * @param {Number} data.verificationResult 验证结果(1:合格,2:不合格)
 * @param {String} data.verificationComments 验证意见
 */
export function verifySafetyInspection(id, data) {
  return request({
    url: `${API_BASE_PATH}/verify/${id}`,
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

// 选项数据获取函数

/**
 * 获取检查类型选项
 */
export function getInspectionTypeOptions() {
  return [
    { value: 1, label: '日常检查' },
    { value: 2, label: '专项检查' },
    { value: 3, label: '综合检查' }
  ]
}

/**
 * 获取隐患等级选项
 */
export function getHazardLevelOptions() {
  return [
    { value: 1, label: '一般', color: '#67C23A' },
    { value: 2, label: '较大', color: '#E6A23C' },
    { value: 3, label: '重大', color: '#F56C6C' },
    { value: 4, label: '特别重大', color: '#909399' }
  ]
}

/**
 * 获取紧急程度选项
 */
export function getEmergencyLevelOptions() {
  return [
    { value: 1, label: '一般', color: '#67C23A' },
    { value: 2, label: '紧急', color: '#E6A23C' },
    { value: 3, label: '特急', color: '#F56C6C' }
  ]
}

/**
 * 获取整改状态选项
 */
export function getRectificationStatusOptions() {
  return [
    { value: 1, label: '待整改', color: '#F56C6C' },
    { value: 2, label: '整改中', color: '#E6A23C' },
    { value: 3, label: '已整改', color: '#67C23A' },
    { value: 4, label: '已验收', color: '#409EFF' }
  ]
}

/**
 * 获取验证结果选项
 */
export function getVerificationResultOptions() {
  return [
    { value: 1, label: '合格', color: '#67C23A' },
    { value: 2, label: '不合格', color: '#F56C6C' }
  ]
}

/**
 * 根据值获取选项标签
 */
export function getOptionLabel(options, value) {
  const option = options.find(item => item.value === value)
  return option ? option.label : '未知'
}

/**
 * 根据值获取选项颜色
 */
export function getOptionColor(options, value) {
  const option = options.find(item => item.value === value)
  return option ? option.color : '#909399'
}

// 工具函数

/**
 * 格式化检查类型名称
 */
export function getInspectionTypeName(type) {
  return getOptionLabel(getInspectionTypeOptions(), type)
}

/**
 * 格式化隐患等级名称
 */
export function getHazardLevelName(level) {
  return getOptionLabel(getHazardLevelOptions(), level)
}

/**
 * 格式化紧急程度名称
 */
export function getEmergencyLevelName(level) {
  return getOptionLabel(getEmergencyLevelOptions(), level)
}

/**
 * 格式化整改状态名称
 */
export function getRectificationStatusName(status) {
  return getOptionLabel(getRectificationStatusOptions(), status)
}

/**
 * 格式化验证结果名称
 */
export function getVerificationResultName(result) {
  return getOptionLabel(getVerificationResultOptions(), result)
}
