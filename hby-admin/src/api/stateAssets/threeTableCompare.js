import request from '@/utils/request'
import { transData } from '@/utils/requestData'

const BASE = '/monitor/v1/supervision/property/three-table-compare'

/**
 * 分页查询三表比对列表（支持筛选条件）
 * @param {Object} data - { companyName, diffStatus, shareholderType, pageNumber, pageSize }
 */
export function getThreeTableCompareList(data) {
  return request({ url: `${BASE}/list`, method: 'post', data: transData(data) })
}

/**
 * 获取三表比对详情（含差异分析明细）
 * @param {String} id - 记录ID
 */
export function getThreeTableCompareDetail(id) {
  return request({ url: `${BASE}/detail/${id}`, method: 'get' })
}

/**
 * 导出差异报告（Excel）
 * @param {Object} params - { companyName, diffStatus, shareholderType }
 */
export function exportThreeTableCompare(params) {
  return request({
    url: `${BASE}/export`,
    method: 'get',
    params,
    responseType: 'blob'
  })
}

/**
 * 提交差异核查派单
 * @param {Object} data - { compareId, companyName, owner, deadline, requirement }
 */
export function submitThreeTableDispatch(data) {
  return request({ url: `${BASE}/dispatch`, method: 'post', data })
}
