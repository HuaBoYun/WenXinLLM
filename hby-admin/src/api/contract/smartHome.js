import request from '@/utils/request'
import { transData } from '@/utils/requestData'

/**
 * 智慧合同首页API接口
 * @author 示例云开发团队
 * @since 2025-06-01
 */

/**
 * 获取首页聚合数据（KPI、统计、合同类型分布等）
 * @param {Object} params { year, month }
 * @returns {Promise} 首页统计数据
 */
export function getSmartHomeData(params) {
  return request({
    url: '/contract/smartHomeData',
    method: 'get',
    params: transData(params),
  })
}

/**
 * 获取待审批合同列表（复用待办接口）
 * @param {Object} params { pageNumber, pageSize, category }
 * @returns {Promise} 待审批列表
 */
export function getPendingApprovalList(params) {
  return request({
    url: '/setting/ymWrok/getList',
    method: 'get',
    params: transData(params),
  })
}

/**
 * 获取风险预警列表
 * @param {Object} params { pageNumber, pageSize }
 * @returns {Promise} 风险预警列表
 */
export function getRiskWarningList(params) {
  return request({
    url: '/contract/smartHome/riskWarning',
    method: 'get',
    params: transData(params),
  })
}

/**
 * 获取即将到期合同列表
 * @param {Object} params { days: 30 }
 * @returns {Promise} 即将到期合同列表
 */
export function getExpiringContractList(params) {
  return request({
    url: '/contract/smartHome/expiring',
    method: 'get',
    params: transData(params),
  })
}

/**
 * 获取相对方风险监控数据
 * @param {Object} params { pageNumber, pageSize }
 * @returns {Promise} 相对方风险列表
 */
export function getCounterpartRiskList(params) {
  return request({
    url: '/contract/smartHome/counterpartRisk',
    method: 'get',
    params: transData(params),
  })
}

/**
 * 获取重点合同履行进度
 * @param {Object} params { pageNumber, pageSize }
 * @returns {Promise} 重点合同进度列表
 */
export function getKeyContractProgress(params) {
  return request({
    url: '/contract/smartHome/keyContracts',
    method: 'get',
    params: transData(params),
  })
}
