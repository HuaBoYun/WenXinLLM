import request from '@/utils/request'
import { contractRequest } from './riskAssessment'

/**
 * 项目策划API接口
 * @author 示例云开发团队
 * @since 2025-01-06
 */

// ==================== 项目策划管理 ====================

/**
 * 创建项目策划
 * @param {Object} data 策划数据
 * @returns {Promise} 创建结果
 */
export function createProjectPlanning(data) {
  return request({
    url: '/contract/planning/save',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 查询项目策划列表
 * @param {Object} params 查询参数
 * @returns {Promise} 分页结果
 */
export function getProjectPlanningList(params) {
  return request({
    url: '/contract/planning/list',
    method: 'post',
    data: params,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 获取项目策划详情
 * @param {String} id 策划ID
 * @returns {Promise} 策划详情
 */
export function getProjectPlanningById(id) {
  return request({
    url: `/contract/planning/${id}`,
    method: 'get'
  })
}

/**
 * 更新项目策划
 * @param {Object} data 更新数据（包含id）
 * @returns {Promise} 更新结果
 */
export function updateProjectPlanning(data) {
  return request({
    url: '/contract/planning/save',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 项目策划审核
 * @param {Object} data 审核数据（包含id）
 * @returns {Promise} 审核结果
 */
export function reviewProjectPlanning(data) {
  return request({
    url: '/contract/planning/review',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 删除项目策划
 * @param {Number} id 策划ID
 * @returns {Promise} 删除结果
 */
export function deleteProjectPlanning(id) {
  return request({
    url: `/contract/planning/${id}`,
    method: 'delete'
  })
}

/**
 * 批量删除项目策划
 * @param {Array} ids 策划ID列表
 * @returns {Promise} 删除结果
 */
export function batchDeleteProjectPlanning(ids) {
  return request({
    url: '/contract/planning/batchDelete',
    method: 'post',
    data: ids,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

// ==================== 人员配置管理 ====================

/**
 * 创建人员配置
 * @param {Object} data 人员配置数据
 * @returns {Promise} 创建结果
 */
export function createPersonnelAllocation(data) {
  return request({
    url: '/contract/planning/personnel',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 查询人员配置列表
 * @param {Object} params 查询参数
 * @returns {Promise} 人员配置列表
 */
export function getPersonnelAllocationList(params) {
  return request({
    url: '/contract/planning/personnel/page',
    method: 'post',
    data: params,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

// ==================== 设备资源管理 ====================

/**
 * 创建设备资源
 * @param {Object} data 设备资源数据
 * @returns {Promise} 创建结果
 */
export function createEquipmentResource(data) {
  return request({
    url: '/contract/planning/equipment/create',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 查询设备资源列表
 * @param {Object} params 查询参数
 * @returns {Promise} 设备资源列表
 */
export function getEquipmentResourceList(params) {
  return request({
    url: '/contract/planning/equipment/page',
    method: 'post',
    data: params,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

// ==================== 材料需求管理 ====================

/**
 * 创建材料需求
 * @param {Object} data 材料需求数据
 * @returns {Promise} 创建结果
 */
export function createMaterialRequirement(data) {
  return contractRequest({
    url: '/planning/material/create',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 查询材料需求列表
 * @param {Object} params 查询参数
 * @returns {Promise} 材料需求列表
 */
export function getMaterialRequirementList(params) {
  return contractRequest({
    url: '/planning/material/list',
    method: 'post',
    data: params,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

// ==================== 时间进度管理 ====================

/**
 * 创建进度节点
 * @param {Object} data 进度节点数据
 * @returns {Promise} 创建结果
 */
export function createScheduleTask(data) {
  return request({
    url: '/contract/planning/schedule/create',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 查询进度节点列表
 * @param {Object} params 查询参数
 * @returns {Promise} 进度节点列表
 */
export function getScheduleTaskList(params) {
  return request({
    url: '/contract/planning/schedule/page',
    method: 'post',
    data: params,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

// ==================== 对外采购计划管理 ====================

/**
 * 创建采购计划
 * @param {Object} data 采购计划数据
 * @returns {Promise} 创建结果
 */
export function createProcurementPlan(data) {
  return request({
    url: '/contract/planning/procurement/create',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 查询采购计划列表
 * @param {Object} params 查询参数
 * @returns {Promise} 采购计划列表
 */
export function getProcurementPlanList(params) {
  return request({
    url: '/contract/planning/procurement/page',
    method: 'post',
    data: params,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

// ==================== 协同部门协调管理 ====================

/**
 * 创建协调事项
 * @param {Object} data 协调事项数据
 * @returns {Promise} 创建结果
 */
export function createCoordinationItem(data) {
  return request({
    url: '/contract/planning/coordination/create',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 查询协调事项列表
 * @param {Object} params 查询参数
 * @returns {Promise} 协调事项列表
 */
export function getCoordinationItemList(params) {
  return request({
    url: '/contract/planning/coordination/page',
    method: 'post',
    data: params,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 发送协调通知
 * @param {Object} data 通知数据
 * @returns {Promise} 发送结果
 */
export function sendCoordinationNotification(data) {
  return request({
    url: '/contract/planning/coordination/notify',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 安排协调会议
 * @param {Object} data 会议数据
 * @returns {Promise} 安排结果
 */
export function scheduleMeeting(data) {
  return request({
    url: '/contract/planning/coordination/meeting',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}
