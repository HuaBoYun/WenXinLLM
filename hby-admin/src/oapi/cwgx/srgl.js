/**
 * 财务共享 - 收入管理 API
 * 调用后端 springboothbyunFinancialSharing 模块的收入合同接口
 */
import request from '@/utils/request'
import { transData } from '@/utils/requestData'

/**
 * 获取收入合同执行情况列表
 * @param {Object} params 查询参数
 * @param {number} params.pageNumber 页码（默认1）
 * @param {number} params.pageSize 每页数量（默认10）
 * @param {string} params.contractNo 合同编号
 * @param {string} params.contractName 合同名称
 * @param {string} params.customerName 客户名称
 * @param {string} params.contractStatus 合同状态
 * @param {string} params.contractType 合同类型
 * @param {string} params.signDateStart 签订日期开始
 * @param {string} params.signDateEnd 签订日期结束
 * @returns {Promise} 返回合同列表数据
 */
export function getRevenueContractList(params) {
  // 转换分页参数
  const queryParams = {
    pageNum: params.pageNumber || 1,
    pageSize: params.pageSize || 10,
    contractNo: params.contractNo,
    contractName: params.contractName,
    customerName: params.customerName,
    contractStatus: params.contractStatus,
    contractType: params.contractType,
    signDateStart: params.signDateStart,
    signDateEnd: params.signDateEnd,
  }
  
  return request({
    url: '/financial/transaction/contract/getList',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: JSON.stringify(transData(queryParams)),
  })
}

/**
 * 获取收入合同详情
 * @param {string|number} contractId 合同ID
 * @returns {Promise} 返回合同详情
 */
export function getRevenueContractDetail(contractId) {
  return request({
    url: `/financial/transaction/contract/detail/${contractId}`,
    method: 'get',
  })
}

/**
 * 删除收入合同
 * @param {string|number} contractId 合同ID
 * @returns {Promise} 返回删除结果
 */
export function deleteRevenueContract(contractId) {
  return request({
    url: `/financial/transaction/contract/delete/${contractId}`,
    method: 'delete',
  })
}

/**
 * 导出收入合同数据
 * @param {Object} params 导出参数
 * @returns {Promise} 返回导出结果
 */
export function exportRevenueContract(params) {
  return request({
    url: '/financial/transaction/contract/export',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: JSON.stringify(transData(params)),
    responseType: 'blob',
  })
}

/**
 * 获取合同收入确认情况
 * @param {string|number} contractId 合同ID
 * @returns {Promise} 返回收入确认情况
 */
export function getContractRevenueRecognition(contractId) {
  return request({
    url: `/financial/transaction/contract/revenue-recognition/${contractId}`,
    method: 'get',
  })
}

