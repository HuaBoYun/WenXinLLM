import request from '@/utils/request'

// 财资公共模块API接口

// ==================== 参数配置管理 ====================

/**
 * 分页查询参数配置
 */
export function getTreasuryParameterList(params) {
  return request({
    url: '/qqsk/financial/basicConfig/parameters/page',
    method: 'get',
    params
  })
}

/**
 * 根据ID查询参数配置
 */
export function getTreasuryParameter(id) {
  return request({
    url: `/qqsk/financial/basicConfig/parameters/${id}`,
    method: 'get'
  })
}

/**
 * 创建参数配置
 */
export function createTreasuryParameter(data) {
  return request({
    url: '/qqsk/financial/basicConfig/parameters',
    method: 'post',
    data
  })
}

/**
 * 更新参数配置
 */
export function updateTreasuryParameter(data) {
  return request({
    url: '/qqsk/financial/basicConfig/parameters',
    method: 'put',
    data
  })
}

/**
 * 删除参数配置
 */
export function deleteTreasuryParameter(id) {
  return request({
    url: `/qqsk/financial/basicConfig/parameters/${id}`,
    method: 'delete'
  })
}

/**
 * 更新参数状态
 */
export function updateParameterStatus(data) {
  return request({
    url: '/qqsk/financial/basicConfig/parameters/status',
    method: 'put',
    data
  })
}

/**
 * 保存或更新参数配置
 */
export function saveOrUpdateParameter(data) {
  return request({
    url: '/qqsk/financial/basicConfig/parameters/save-or-update',
    method: 'post',
    data
  })
}

// ==================== 业务系统注册管理 ====================

/**
 * 分页查询业务系统
 */
export function getBusinessSystemList(params) {
  return request({
    url: '/qqsk/financial/basicConfig/system/list',
    method: 'post',
    data: params
  })
}

/**
 * 根据ID查询业务系统
 */
export function getBusinessSystem(id) {
  return request({
    url: '/qqsk/financial/basicConfig/system/detail',
    method: 'get',
    params: { id }
  })
}

/**
 * 创建业务系统
 */
export function createBusinessSystem(data) {
  return request({
    url: '/qqsk/financial/basicConfig/system/add',
    method: 'post',
    data
  })
}

/**
 * 更新业务系统
 */
export function updateBusinessSystem(data) {
  return request({
    url: '/qqsk/financial/basicConfig/system/update',
    method: 'post',
    data
  })
}

/**
 * 删除业务系统
 */
export function deleteBusinessSystem(params) {
  return request({
    url: '/qqsk/financial/basicConfig/system/delete',
    method: 'post',
    data: params
  })
}

/**
 * 测试系统连接
 */
export function testSystemConnection(params) {
  return request({
    url: '/qqsk/financial/basicConfig/system/testConnection',
    method: 'post',
    data: params
  })
}

/**
 * 同步系统状态
 */
export function syncSystemStatus() {
  return request({
    url: '/qqsk/financial/basicConfig/system/syncStatus',
    method: 'post'
  })
}

/**
 * 获取系统类型选项
 */
export function getSystemTypes() {
  return request({
    url: '/qqsk/financial/basicConfig/system/systemTypes',
    method: 'get'
  })
}

/**
 * 获取认证方式选项
 */
export function getAuthTypes() {
  return request({
    url: '/qqsk/financial/basicConfig/system/authTypes',
    method: 'get'
  })
}

/**
 * 导出业务系统数据
 */
export function exportBusinessSystem(params) {
  return request({
    url: '/qqsk/financial/basicConfig/system/export',
    method: 'post',
    data: params,
    responseType: 'blob'
  })
}

// ==================== Ukey厂商管理 ====================

/**
 * 分页查询Ukey厂商
 */
export function getUkeyVendorList(params) {
  // 转换参数名：前端使用 pageNumber/pageSize，后端使用 page/limit
  const convertedParams = {
    ...params,
    page: params.pageNumber || params.page || 1,
    limit: params.pageSize || params.limit || 20
  }
  // 删除前端参数名
  delete convertedParams.pageNumber
  delete convertedParams.pageSize

  return request({
    url: '/qqsk/financial/basicConfig/ukeyVendor/list',
    method: 'get',
    params: convertedParams
  })
}

/**
 * 根据ID查询Ukey厂商
 */
export function getUkeyVendor(id) {
  return request({
    url: `/qqsk/financial/basicConfig/ukeyVendor/${id}`,
    method: 'get'
  })
}

/**
 * 创建Ukey厂商
 */
export function createUkeyVendor(data) {
  return request({
    url: '/qqsk/financial/basicConfig/ukeyVendor/add',
    method: 'post',
    data
  })
}

/**
 * 更新Ukey厂商
 */
export function updateUkeyVendor(data) {
  return request({
    url: '/qqsk/financial/basicConfig/ukeyVendor/update',
    method: 'post',
    data
  })
}

/**
 * 删除Ukey厂商
 */
export function deleteUkeyVendor(id) {
  return request({
    url: '/qqsk/financial/basicConfig/ukeyVendor/delete',
    method: 'post',
    params: { id }
  })
}

/**
 * 获取厂商类型选项
 */
export function getVendorTypes() {
  return request({
    url: '/qqsk/financial/basicConfig/ukeyVendor/vendorTypes',
    method: 'get'
  })
}

/**
 * 获取厂商等级选项
 */
export function getVendorLevels() {
  return request({
    url: '/qqsk/financial/basicConfig/ukeyVendor/vendorLevels',
    method: 'get'
  })
}

/**
 * 获取合作状态选项
 */
export function getCooperationStatuses() {
  return request({
    url: '/qqsk/financial/basicConfig/ukeyVendor/cooperationStatuses',
    method: 'get'
  })
}

/**
 * 获取评价等级选项
 */
export function getEvaluationLevels() {
  return request({
    url: '/qqsk/financial/basicConfig/ukeyVendor/evaluationLevels',
    method: 'get'
  })
}

/**
 * 获取厂商统计信息
 */
export function getUkeyVendorStatistics() {
  return request({
    url: '/qqsk/financial/basicConfig/ukeyVendor/statistics',
    method: 'get'
  })
}

/**
 * 导出Ukey厂商数据
 */
export function exportUkeyVendor(params) {
  return request({
    url: '/qqsk/financial/basicConfig/ukeyVendor/export',
    method: 'get',
    params,
    responseType: 'blob'
  })
}

// ==================== 数据映射配置管理 ====================

/**
 * 分页查询数据映射配置
 */
export function getDataMappingList(params) {
  return request({
    url: '/qqsk/financial/basicConfig/dataMapping/list',
    method: 'post',
    data: params
  })
}

/**
 * 创建数据映射配置
 */
export function createDataMapping(data) {
  return request({
    url: '/qqsk/financial/basicConfig/dataMapping/add',
    method: 'post',
    data
  })
}

/**
 * 更新数据映射配置
 */
export function updateDataMapping(data) {
  return request({
    url: '/qqsk/financial/basicConfig/dataMapping/update',
    method: 'post',
    data
  })
}

/**
 * 删除数据映射配置
 */
export function deleteDataMapping(params) {
  return request({
    url: '/qqsk/financial/basicConfig/dataMapping/delete',
    method: 'post',
    data: params
  })
}

/**
 * 测试数据映射
 */
export function testDataMapping(params) {
  return request({
    url: '/qqsk/financial/basicConfig/dataMapping/testMapping',
    method: 'post',
    data: params
  })
}

/**
 * 获取字段映射
 */
export function getFieldMappings(params) {
  return request({
    url: '/qqsk/treasury/data-mappings/fields',
    method: 'get',
    params
  })
}

/**
 * 保存字段映射
 */
export function saveFieldMappings(data) {
  return request({
    url: '/qqsk/treasury/data-mappings/fields',
    method: 'post',
    data
  })
}

// ==================== 印鉴类型管理 ====================

/**
 * 分页查询印鉴类型
 */
export function getSealTypeList(params) {
  return request({
    url: '/qqsk/financial/xjgl/basicConfig/seal/type/list',
    method: 'post',
    data: params
  })
}

/**
 * 创建印鉴类型
 */
export function createSealType(data) {
  return request({
    url: '/qqsk/financial/xjgl/basicConfig/seal/type/create',
    method: 'post',
    data
  })
}

/**
 * 更新印鉴类型
 */
export function updateSealType(data) {
  return request({
    url: '/qqsk/financial/xjgl/basicConfig/seal/type/update',
    method: 'post',
    data
  })
}

/**
 * 删除印鉴类型
 */
export function deleteSealType(data) {
  return request({
    url: '/qqsk/financial/xjgl/basicConfig/seal/type/delete',
    method: 'post',
    data
  })
}

/**
 * 更新印鉴类型状态
 */
export function updateSealTypeStatus(data) {
  return request({
    url: '/qqsk/financial/xjgl/basicConfig/seal/type/updateStatus',
    method: 'post',
    data
  })
}

/**
 * 导出印鉴类型数据
 */
export function exportSealType(params) {
  return request({
    url: '/qqsk/financial/xjgl/basicConfig/seal/type/export',
    method: 'get',
    params,
    responseType: 'blob'
  })
}

// ==================== 市场数据管理 ====================

/**
 * 获取市场数据统计信息
 */
export function getMarketDataStatistics() {
  return request({
    url: '/qqsk/treasury/market-data/statistics',
    method: 'get'
  })
}

/**
 * 分页查询市场数据
 */
export function getMarketDataPage(params) {
  return request({
    url: '/qqsk/treasury/market-data/page',
    method: 'get',
    params
  })
}

/**
 * 查询市场数据列表（不分页）
 */
export function getMarketDataList(params) {
  return request({
    url: '/qqsk/treasury/market-data/list',
    method: 'get',
    params
  })
}

/**
 * 根据ID查询市场数据
 */
export function getMarketData(dataId) {
  return request({
    url: `/qqsk/treasury/market-data/${dataId}`,
    method: 'get'
  })
}

/**
 * 创建市场数据
 */
export function createMarketData(data) {
  return request({
    url: '/qqsk/treasury/market-data',
    method: 'post',
    data
  })
}

/**
 * 更新市场数据
 */
export function updateMarketData(data) {
  return request({
    url: '/qqsk/treasury/market-data',
    method: 'put',
    data
  })
}

/**
 * 删除市场数据
 */
export function deleteMarketData(dataId, updateUser) {
  return request({
    url: `/qqsk/treasury/market-data/${dataId}`,
    method: 'delete',
    params: { updateUser }
  })
}

/**
 * 批量删除市场数据
 */
export function batchDeleteMarketData(dataIds, updateUser) {
  return request({
    url: '/qqsk/treasury/market-data/batch',
    method: 'delete',
    data: dataIds,
    params: { updateUser }
  })
}

/**
 * 导入市场数据
 */
export function importMarketData(file, updateUser) {
  const formData = new FormData()
  formData.append('file', file)
  return request({
    url: '/qqsk/treasury/market-data/import',
    method: 'post',
    data: formData,
    params: { updateUser },
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

/**
 * 导出市场数据
 */
export function exportMarketData(params) {
  return request({
    url: '/qqsk/treasury/market-data/export',
    method: 'get',
    params,
    responseType: 'blob'
  })
}

/**
 * 同步市场数据
 */
export function syncMarketData(dataType, updateUser) {
  return request({
    url: '/qqsk/treasury/market-data/sync',
    method: 'post',
    params: { dataType, updateUser }
  })
}

// ==================== 银企直连日志管理 ====================

/**
 * 分页查询银企直连日志
 */
export function getBankConnectLogPage(params) {
  return request({
    url: '/qqsk/treasury/bank-connect-log/page',
    method: 'get',
    params
  })
}

/**
 * 查询银企直连日志列表（不分页）
 */
export function getBankConnectLogList(params) {
  return request({
    url: '/qqsk/treasury/bank-connect-log/list',
    method: 'get',
    params
  })
}

/**
 * 根据ID查询银企直连日志
 */
export function getBankConnectLog(logId) {
  return request({
    url: `/qqsk/treasury/bank-connect-log/${logId}`,
    method: 'get'
  })
}

/**
 * 根据请求ID查询银企直连日志
 */
export function getBankConnectLogByRequestId(requestId) {
  return request({
    url: `/qqsk/treasury/bank-connect-log/request/${requestId}`,
    method: 'get'
  })
}

/**
 * 删除银企直连日志
 */
export function deleteBankConnectLog(logId, updateUser) {
  return request({
    url: `/qqsk/treasury/bank-connect-log/${logId}`,
    method: 'delete',
    params: { updateUser }
  })
}

/**
 * 批量删除银企直连日志
 */
export function batchDeleteBankConnectLog(logIds, updateUser) {
  return request({
    url: '/qqsk/treasury/bank-connect-log/batch',
    method: 'delete',
    data: logIds,
    params: { updateUser }
  })
}

/**
 * 清理历史日志
 */
export function cleanHistoryLogs(days, updateUser) {
  return request({
    url: '/qqsk/treasury/bank-connect-log/clean',
    method: 'delete',
    params: { days, updateUser }
  })
}

/**
 * 导出银企直连日志
 */
export function exportBankConnectLog(params) {
  return request({
    url: '/qqsk/treasury/bank-connect-log/export',
    method: 'get',
    params,
    responseType: 'blob'
  })
}

/**
 * 重试银企直连请求
 */
export function retryBankConnectRequest(logId, updateUser) {
  return request({
    url: `/qqsk/treasury/bank-connect-log/${logId}/retry`,
    method: 'post',
    params: { updateUser }
  })
}

/**
 * 获取银企直连日志统计信息
 */
export function getBankConnectLogStatistics(params) {
  return request({
    url: '/qqsk/treasury/bank-connect-log/statistics',
    method: 'get',
    params
  })
}

// ==================== 通用接口 ====================

/**
 * 获取数据字典
 */
export function getDictData(dictType) {
  return request({
    url: `/qqsk/treasury/dict/${dictType}`,
    method: 'get'
  })
}

/**
 * 获取组织机构列表
 */
export function getOrgList(params) {
  return request({
    url: '/qqsk/treasury/org/list',
    method: 'get',
    params
  })
}

/**
 * 获取用户信息
 */
export function getUserInfo(userId) {
  return request({
    url: `/qqsk/treasury/user/${userId}`,
    method: 'get'
  })
}

/**
 * 获取当前用户信息
 */
export function getCurrentUserInfo() {
  return request({
    url: '/qqsk/treasury/user/current',
    method: 'get'
  })
}

/**
 * 文件上传
 */
export function uploadFile(file, fileType) {
  const formData = new FormData()
  formData.append('file', file)
  return request({
    url: '/qqsk/treasury/file/upload',
    method: 'post',
    data: formData,
    params: { fileType },
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

/**
 * 文件下载
 */
export function downloadFile(fileId) {
  return request({
    url: `/qqsk/treasury/file/download/${fileId}`,
    method: 'get',
    responseType: 'blob'
  })
}

// ==================== 第三方账户管理 ====================

/**
 * 分页查询第三方账户
 * 前端字段 -> 后端字段映射:
 *   pageNumber -> page, pageSize -> limit,
 *   thirdPartySystem -> platformType, connectionStatus -> status
 */
export function getThirdPartyAccountList(params) {
  const query = {
    page: params.pageNumber || params.page || 1,
    limit: params.pageSize || params.limit || 20,
    accountName: params.accountName || undefined,
    accountCode: params.accountCode || undefined,
    accountType: params.accountType || undefined,
    platformType: params.thirdPartySystem || undefined,
    connectionStatus: params.connectionStatus || undefined
  }
  // 清除值为 undefined 的字段
  Object.keys(query).forEach(key => {
    if (query[key] === undefined || query[key] === null || query[key] === '') {
      delete query[key]
    }
  })
  return request({
    url: '/qqsk/financial/basicConfig/thirdPartyAccount/list',
    method: 'get',
    params: query
  })
}

/**
 * 根据ID查询第三方账户
 */
export function getThirdPartyAccount(id) {
  return request({
    url: '/qqsk/financial/basicConfig/thirdPartyAccount/detail',
    method: 'get',
    params: { id }
  })
}

/**
 * 创建第三方账户
 */
export function createThirdPartyAccount(data) {
  return request({
    url: '/qqsk/financial/basicConfig/thirdPartyAccount/add',
    method: 'post',
    data
  })
}

/**
 * 更新第三方账户
 */
export function updateThirdPartyAccount(data) {
  return request({
    url: '/qqsk/financial/basicConfig/thirdPartyAccount/update',
    method: 'post',
    data
  })
}

/**
 * 删除第三方账户
 */
export function deleteThirdPartyAccount(params) {
  return request({
    url: '/qqsk/financial/basicConfig/thirdPartyAccount/delete',
    method: 'post',
    params
  })
}

/**
 * 同步第三方账户状态
 */
export function syncThirdPartyAccountStatus(params) {
  return request({
    url: '/qqsk/financial/basicConfig/thirdPartyAccount/sync',
    method: 'post',
    data: params
  })
}

/**
 * 批量同步第三方账户状态
 */
export function batchSyncThirdPartyAccountStatus(params) {
  return request({
    url: '/qqsk/financial/basicConfig/thirdPartyAccount/batchSync',
    method: 'post',
    params
  })
}

/**
 * 测试第三方账户连接
 */
export function testThirdPartyAccountConnection(id) {
  return request({
    url: '/qqsk/financial/basicConfig/thirdPartyAccount/testConnection',
    method: 'post',
    params: { id }
  })
}

/**
 * 导出第三方账户数据
 */
export function exportThirdPartyAccount(params) {
  return request({
    url: '/qqsk/financial/basicConfig/thirdPartyAccount/export',
    method: 'post',
    params,
    responseType: 'blob'
  })
}

/**
 * 获取第三方系统选项
 */
export function getThirdPartySystems() {
  return request({
    url: '/qqsk/financial/basicConfig/thirdPartyAccount/systems',
    method: 'get'
  })
}

/**
 * 获取账户类型选项
 */
export function getAccountTypes() {
  return request({
    url: '/qqsk/financial/basicConfig/thirdPartyAccount/accountTypes',
    method: 'get'
  })
}

// ==================== 电票账户配置管理 ====================

/**
 * 分页查询电票账户配置
 */
export function getETicketAccountList(params) {
  return request({
    url: '/qqsk/financial/basicConfig/eTicketAccount/list',
    method: 'post',
    data: params
  })
}

/**
 * 创建电票账户配置
 */
export function createETicketAccount(data) {
  return request({
    url: '/qqsk/financial/basicConfig/eTicketAccount/add',
    method: 'post',
    data
  })
}

/**
 * 更新电票账户配置
 */
export function updateETicketAccount(data) {
  return request({
    url: '/qqsk/financial/basicConfig/eTicketAccount/update',
    method: 'post',
    data
  })
}

/**
 * 删除电票账户配置
 */
export function deleteETicketAccount(params) {
  return request({
    url: '/qqsk/financial/basicConfig/eTicketAccount/delete',
    method: 'post',
    data: params
  })
}

/**
 * 保存或更新电票账户配置
 */
export function saveOrUpdateETicketAccount(data) {
  return request({
    url: '/qqsk/financial/basicConfig/eTicketAccount/saveOrUpdate',
    method: 'post',
    data
  })
}

/**
 * 获取票据类型选项
 */
export function getETicketTypes() {
  return request({
    url: '/qqsk/financial/basicConfig/eTicketAccount/eTicketTypes',
    method: 'get'
  })
}

/**
 * 同步账户状态
 */
export function syncETicketAccountStatus() {
  return request({
    url: '/qqsk/financial/basicConfig/eTicketAccount/syncStatus',
    method: 'post'
  })
}

/**
 * 测试账户连接
 */
export function testETicketAccountConnection(id) {
  return request({
    url: '/qqsk/financial/basicConfig/eTicketAccount/testConnection',
    method: 'post',
    data: { id }
  })
}

// ==================== 印鉴档案管理 ====================

/**
 * 分页查询印鉴档案
 */
export function getSealArchiveList(params) {
  return request({
    url: '/qqsk/financial/basicConfig/sealArchive/list',
    method: 'get',
    params
  })
}

/**
 * 获取印鉴档案详情
 */
export function getSealArchiveDetail(id) {
  return request({
    url: '/qqsk/financial/basicConfig/sealArchive/detail',
    method: 'get',
    params: { id }
  })
}

/**
 * 创建印鉴档案
 */
export function createSealArchive(data) {
  return request({
    url: '/qqsk/financial/basicConfig/sealArchive/add',
    method: 'post',
    data
  })
}

/**
 * 更新印鉴档案
 */
export function updateSealArchive(data) {
  return request({
    url: '/qqsk/financial/basicConfig/sealArchive/update',
    method: 'post',
    data
  })
}

/**
 * 删除印鉴档案（POST + id 参数，与后端 @PostMapping("/delete") 对齐）
 */
export function deleteSealArchive(id) {
  return request({
    url: '/qqsk/financial/basicConfig/sealArchive/delete',
    method: 'post',
    params: { id }
  })
}

/**
 * 获取印鉴统计数据
 */
export function getSealStatistics() {
  return request({
    url: '/qqsk/financial/basicConfig/sealArchive/statistics',
    method: 'get'
  })
}

/**
 * 获取印鉴类型选项（下拉框）
 */
export function getSealTypeOptions() {
  return request({
    url: '/qqsk/financial/basicConfig/sealArchive/types',
    method: 'get'
  })
}

/**
 * 上传印鉴图片
 */
export function uploadSealImage(file, sealId) {
  const formData = new FormData()
  formData.append('file', file)
  formData.append('sealId', sealId)
  return request({
    url: '/qqsk/financial/basicConfig/seal/uploadImage',
    method: 'post',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

/**
 * 更新印鉴状态
 */
export function updateSealStatus(data) {
  return request({
    url: '/qqsk/financial/basicConfig/seal/updateStatus',
    method: 'post',
    data
  })
}

/**
 * 检查印鉴编码唯一性
 */
export function checkSealCodeUnique(data) {
  return request({
    url: '/qqsk/financial/basicConfig/seal/checkCode',
    method: 'post',
    data
  })
}

// ==================== 印鉴组合配置管理 ====================

/**
 * 分页查询印鉴组合
 */
export function getSealCombinationList(params) {
  return request({
    url: '/qqsk/financial/basicConfig/sealCombination/list',
    method: 'get',
    params
  })
}

/**
 * 获取印鉴组合详情
 */
export function getSealCombinationDetail(id) {
  return request({
    url: `/qqsk/financial/basicConfig/sealCombination/detail?id=${id}`,
    method: 'get'
  })
}

/**
 * 创建印鉴组合
 */
export function createSealCombination(data) {
  return request({
    url: '/qqsk/financial/basicConfig/sealCombination/create',
    method: 'post',
    data
  })
}

/**
 * 更新印鉴组合
 */
export function updateSealCombination(data) {
  return request({
    url: '/qqsk/financial/basicConfig/sealCombination/update',
    method: 'post',
    data
  })
}

/**
 * 删除印鉴组合
 */
export function deleteSealCombination(id) {
  return request({
    url: `/qqsk/financial/basicConfig/sealCombination/delete/${id}`,
    method: 'delete'
  })
}

/**
 * 批量删除印鉴组合
 */
export function batchDeleteSealCombination(ids) {
  return request({
    url: '/qqsk/financial/basicConfig/sealCombination/batchDelete',
    method: 'delete',
    data: ids
  })
}

/**
 * 获取组合类型选项
 */
export function getCombinationTypes() {
  return request({
    url: '/qqsk/financial/basicConfig/sealCombination/combinationTypes',
    method: 'get'
  })
}

/**
 * 获取业务类型选项
 */
export function getBusinessTypes() {
  return request({
    url: '/qqsk/financial/basicConfig/sealCombination/businessTypes',
    method: 'get'
  })
}

/**
 * 获取权限级别选项
 */
export function getAuthorityLevels() {
  return request({
    url: '/qqsk/financial/basicConfig/sealCombination/authorityLevels',
    method: 'get'
  })
}

/**
 * 获取印鉴组合统计数据
 */
export function getSealCombinationStatistics() {
  return request({
    url: '/qqsk/financial/basicConfig/sealCombination/statistics',
    method: 'get'
  })
}

/**
 * 获取可选印鉴列表
 */
export function getAvailableSeals() {
  return request({
    url: '/qqsk/financial/basicConfig/sealCombination/availableSeals',
    method: 'get'
  })
}

/**
 * 更新印鉴组合状态
 */
export function updateSealCombinationStatus(data) {
  return request({
    url: '/qqsk/financial/basicConfig/sealCombination/updateStatus',
    method: 'post',
    data
  })
}

/**
 * 检查组合编码唯一性
 */
export function checkCombinationCodeUnique(data) {
  return request({
    url: '/qqsk/financial/basicConfig/sealCombination/checkCode',
    method: 'post',
    data
  })
}

// ==================== 印鉴使用记录管理 ====================

/**
 * 分页查询印鉴使用记录
 */
export function getSealUsageRecordList(params) {
  return request({
    url: '/qqsk/financial/basicConfig/sealUsageRecord/list',
    method: 'post',
    data: params
  })
}

/**
 * 获取印鉴使用记录详情
 */
export function getSealUsageRecordDetail(id) {
  return request({
    url: `/qqsk/financial/basicConfig/sealUsageRecord/detail?id=${id}`,
    method: 'get'
  })
}

/**
 * 创建印鉴使用记录
 */
export function createSealUsageRecord(data) {
  return request({
    url: '/qqsk/financial/basicConfig/sealUsageRecord/save',
    method: 'post',
    data
  })
}

/**
 * 更新印鉴使用记录
 */
export function updateSealUsageRecord(data) {
  return request({
    url: '/qqsk/financial/basicConfig/sealUsageRecord/update',
    method: 'post',
    data
  })
}

/**
 * 删除印鉴使用记录
 */
export function deleteSealUsageRecord(id) {
  return request({
    url: '/qqsk/financial/basicConfig/sealUsageRecord/delete',
    method: 'post',
    data: { id }
  })
}

/**
 * 批量删除印鉴使用记录
 */
export function batchDeleteSealUsageRecord(ids) {
  return request({
    url: '/qqsk/financial/basicConfig/sealUsageRecord/deleteBatch',
    method: 'post',
    data: { ids }
  })
}

/**
 * 获取印鉴使用统计数据
 */
export function getSealUsageStatistics() {
  return request({
    url: '/qqsk/financial/basicConfig/sealUsageRecord/statistics',
    method: 'get'
  })
}

/**
 * 导出印鉴使用记录
 */
export function exportSealUsageRecord(params) {
  return request({
    url: '/qqsk/financial/basicConfig/sealUsageRecord/export',
    method: 'get',
    params,
    responseType: 'blob'
  })
}

// ==================== 安全参数配置管理 ====================

/**
 * 分页查询安全参数配置
 */
export function getSecurityParamList(params) {
  return request({
    url: '/qqsk/financial/basicConfig/securityParam/list',
    method: 'get',
    params
  })
}

/**
 * 获取安全参数配置详情
 */
export function getSecurityParamDetail(id) {
  return request({
    url: `/qqsk/financial/basicConfig/securityParam/detail?id=${id}`,
    method: 'get'
  })
}

/**
 * 创建安全参数配置
 */
export function createSecurityParam(data) {
  return request({
    url: '/qqsk/financial/basicConfig/securityParam/create',
    method: 'post',
    data
  })
}

/**
 * 更新安全参数配置
 */
export function updateSecurityParam(data) {
  return request({
    url: '/qqsk/financial/basicConfig/securityParam/update',
    method: 'post',
    data
  })
}

/**
 * 删除安全参数配置
 */
export function deleteSecurityParam(id) {
  return request({
    url: `/qqsk/financial/basicConfig/securityParam/delete/${id}`,
    method: 'delete'
  })
}

/**
 * 批量删除安全参数配置
 */
export function batchDeleteSecurityParam(ids) {
  return request({
    url: '/qqsk/financial/basicConfig/securityParam/batchDelete',
    method: 'delete',
    data: ids
  })
}

/**
 * 获取参数类型选项
 */
export function getParamTypes() {
  return request({
    url: '/qqsk/financial/basicConfig/securityParam/paramTypes',
    method: 'get'
  })
}

/**
 * 获取安全级别选项
 */
export function getSecurityLevels() {
  return request({
    url: '/qqsk/financial/basicConfig/securityParam/securityLevels',
    method: 'get'
  })
}

/**
 * 获取加密方式选项
 */
export function getEncryptionMethods() {
  return request({
    url: '/qqsk/financial/basicConfig/securityParam/encryptionMethods',
    method: 'get'
  })
}

/**
 * 测试参数配置
 */
export function testSecurityParam(id) {
  return request({
    url: `/qqsk/financial/basicConfig/securityParam/test/${id}`,
    method: 'post'
  })
}

/**
 * 导出安全参数配置
 */
export function exportSecurityParam(params) {
  return request({
    url: '/qqsk/financial/basicConfig/securityParam/export',
    method: 'get',
    params,
    responseType: 'blob'
  })
}
