import request from '@/utils/request'

// 参数配置管理相关接口
export function getParameterList(params) {
  // 参数映射：前端 page/limit -> 后端 pageNo/pageSize
  const mappedParams = {
    pageNo: params.page || params.pageNumber || 1,
    pageSize: params.limit || params.pageSize || 20,
    paramName: params.paramName,
    paramType: params.paramType,
    securityLevel: params.securityLevel,
    status: params.status
  }
  return request({
    url: '/zbgl/centralaudit/treasury/parameters/page',
    method: 'get',
    params: mappedParams
  })
}

// 财资参数配置管理相关接口
export function getTreasuryParameterList(params) {
  // 参数映射：前端 page/limit -> 后端 pageNo/pageSize
  const mappedParams = {
    pageNo: params.page || params.pageNumber || 1,
    pageSize: params.limit || params.pageSize || 20,
    paramName: params.paramName,
    paramType: params.paramType,
    securityLevel: params.securityLevel,
    status: params.status
  }
  return request({
    url: '/zbgl/centralaudit/treasury/parameters/page',
    method: 'get',
    params: mappedParams
  })
}

export function deleteTreasuryParameter(data) {
  return request({
    url: `/zbgl/centralaudit/treasury/parameters/delete?id=${data.id}`,
    method: 'post'
  })
}

export function saveOrUpdateParameter(data) {
  return request({
    url: '/zbgl/centralaudit/treasury/parameters/save-or-update',
    method: 'post',
    data
  })
}

export function deleteParameter(id) {
  return request({
    url: `/zbgl/centralaudit/treasury/parameters/delete?id=${id}`,
    method: 'post'
  })
}

export function getParameterById(id) {
  return request({
    url: `/zbgl/centralaudit/treasury/parameters/${id}`,
    method: 'get'
  })
}

export function getParameterByCode(params) {
  return request({
    url: '/zbgl/centralaudit/treasury/parameters/code/' + params.paramCode,
    method: 'get'
  })
}

export function getParameterByType(params) {
  return request({
    url: '/zbgl/centralaudit/treasury/parameters/type/' + params.paramType,
    method: 'get'
  })
}

export function getAllParameterTypes(params) {
  return request({
    url: '/zbgl/centralaudit/treasury/parameters/types',
    method: 'get',
    params
  })
}

export function batchUpdateParameterStatus(data) {
  return request({
    url: '/zbgl/centralaudit/treasury/parameters/batchUpdateStatus',
    method: 'post',
    data
  })
}

export function updateParameterStatus(data) {
  return request({
    url: '/zbgl/centralaudit/treasury/parameters/batchUpdateStatus',
    method: 'post',
    data: {
      paramIds: [data.id],
      isEnabled: data.isEnabled,
      updateUser: JSON.parse(localStorage.getItem('userInfo') || '{}').staffid || 1
    }
  })
}

export function getParameterValue(params) {
  return request({
    url: '/zbgl/centralaudit/treasury/parameters/value/' + params.paramCode,
    method: 'get'
  })
}

export function setParameterValue(data) {
  return request({
    url: '/zbgl/centralaudit/treasury/parameters/setValue',
    method: 'post',
    data
  })
}

// 印鉴类型管理相关接口
export function getSealTypeList(params) {
  // 参数映射：前端 page/limit -> 后端 pageNum/pageSize
  const mappedParams = {
    pageNum: params.page || 1,
    pageSize: params.limit || 20,
    typeCode: params.typeCode,
    typeName: params.typeName,
    isEnabled: params.isEnabled,
    orgId: params.orgId
  }
  return request({
    url: '/cwgxAi/basicConfig/sealTypeManage/list',
    method: 'get',
    params: mappedParams
  })
}

export function saveOrUpdateSealType(data) {
  return request({
    url: '/cwgxAi/basicConfig/sealTypeManage/saveOrUpdate',
    method: 'post',
    data
  })
}

export function deleteSealType(id) {
  return request({
    url: `/cwgxAi/basicConfig/sealTypeManage/delete/${id}`,
    method: 'delete'
  })
}

export function getSealTypeById(id) {
  return request({
    url: `/cwgxAi/basicConfig/sealTypeManage/detail/${id}`,
    method: 'get'
  })
}

export function getSealTypeByCode(params) {
  return request({
    url: '/cwgxAi/basicConfig/sealTypeManage/getByCode',
    method: 'get',
    params
  })
}

export function getEnabledSealTypes(params) {
  return request({
    url: '/cwgxAi/basicConfig/sealTypeManage/enabled',
    method: 'get',
    params
  })
}

export function batchDeleteSealType(data) {
  return request({
    url: '/cwgxAi/basicConfig/sealTypeManage/batchDelete',
    method: 'post',
    data
  })
}

export function updateSealTypeStatus(data) {
  return request({
    url: '/cwgxAi/basicConfig/sealTypeManage/updateStatus',
    method: 'post',
    data: {
      id: data.id,
      isEnabled: data.isEnabled,
      updateUser: JSON.parse(localStorage.getItem('userInfo') || '{}').staffid || 1
    }
  })
}

// 印鉴登记管理相关接口
export function getSealList(params) {
  return request({
    url: '/centralaudit/treasury-common/seal/list',
    method: 'get',
    params
  })
}

export function saveOrUpdateSeal(data) {
  return request({
    url: '/centralaudit/treasury-common/seal/saveOrUpdate',
    method: 'post',
    data
  })
}

export function deleteSeal(id) {
  return request({
    url: `/centralaudit/treasury-common/seal/${id}`,
    method: 'delete'
  })
}

export function getSealById(id) {
  return request({
    url: `/centralaudit/treasury-common/seal/${id}`,
    method: 'get'
  })
}

export function getSealByCode(params) {
  return request({
    url: '/centralaudit/treasury-common/seal/getByCode',
    method: 'get',
    params
  })
}

export function getSealByType(params) {
  return request({
    url: '/centralaudit/treasury-common/seal/getByType',
    method: 'get',
    params
  })
}

export function getActiveSeals(params) {
  return request({
    url: '/centralaudit/treasury-common/seal/active',
    method: 'get',
    params
  })
}

export function getSealByOwner(params) {
  return request({
    url: '/centralaudit/treasury-common/seal/getByOwner',
    method: 'get',
    params
  })
}

// 业务伙伴类型管理相关接口
export function getPartnerTypeList(params) {
  return request({
    url: '/centralaudit/treasury-common/partner-type/list',
    method: 'get',
    params
  })
}

export function saveOrUpdatePartnerType(data) {
  return request({
    url: '/centralaudit/treasury-common/partner-type/saveOrUpdate',
    method: 'post',
    data
  })
}

export function deletePartnerType(id) {
  return request({
    url: `/centralaudit/treasury-common/partner-type/${id}`,
    method: 'delete'
  })
}

export function getEnabledPartnerTypes(params) {
  return request({
    url: '/centralaudit/treasury-common/partner-type/enabled',
    method: 'get',
    params
  })
}

export function updatePartnerTypeStatus(data) {
  return request({
    url: '/centralaudit/treasury-common/partner-type/updateStatus',
    method: 'post',
    data
  })
}

// 业务伙伴管理相关接口
export function getPartnerList(params) {
  return request({
    url: '/centralaudit/treasury-common/partner/list',
    method: 'get',
    params
  })
}

export function saveOrUpdatePartner(data) {
  return request({
    url: '/centralaudit/treasury-common/partner/saveOrUpdate',
    method: 'post',
    data
  })
}

export function deletePartner(id) {
  return request({
    url: `/centralaudit/treasury-common/partner/${id}`,
    method: 'delete'
  })
}

export function getPartnerById(id) {
  return request({
    url: `/centralaudit/treasury-common/partner/${id}`,
    method: 'get'
  })
}

export function getPartnerByCode(params) {
  return request({
    url: '/centralaudit/treasury-common/partner/getByCode',
    method: 'get',
    params
  })
}

export function getPartnerByType(params) {
  return request({
    url: '/centralaudit/treasury-common/partner/getByType',
    method: 'get',
    params
  })
}

export function updatePartnerStatus(data) {
  return request({
    url: '/centralaudit/treasury-common/partner/updateStatus',
    method: 'post',
    data
  })
}

export function getEnabledPartners(orgId) {
  return request({
    url: '/centralaudit/treasury-common/partner/enabled',
    method: 'get',
    params: { orgId }
  })
}

// 业务品种类型管理相关接口
export function getBusinessTypeList(params) {
  return request({
    url: '/centralaudit/treasury-common/business-type/list',
    method: 'get',
    params
  })
}

export function saveOrUpdateBusinessType(data) {
  return request({
    url: '/centralaudit/treasury-common/business-type/saveOrUpdate',
    method: 'post',
    data
  })
}

export function deleteBusinessType(id) {
  return request({
    url: `/centralaudit/treasury-common/business-type/${id}`,
    method: 'delete'
  })
}

export function batchDeleteBusinessType(data) {
  return request({
    url: '/centralaudit/treasury-common/business-type/batchDelete',
    method: 'post',
    data
  })
}

export function updateBusinessTypeStatus(data) {
  return request({
    url: '/centralaudit/treasury-common/business-type/updateStatus',
    method: 'post',
    data
  })
}

export function getBusinessTypeTree(orgId) {
  return request({
    url: '/centralaudit/treasury-common/business-type/tree',
    method: 'get',
    params: { orgId }
  })
}

export function getEnabledBusinessTypes(orgId) {
  return request({
    url: '/centralaudit/treasury-common/business-type/enabled',
    method: 'get',
    params: { orgId }
  })
}

// 业务品种定义管理相关接口
export function getBusinessDefinitionList(params) {
  return request({
    url: '/centralaudit/treasury-common/business-definition/list',
    method: 'get',
    params
  })
}

export function saveOrUpdateBusinessDefinition(data) {
  return request({
    url: '/centralaudit/treasury-common/business-definition/saveOrUpdate',
    method: 'post',
    data
  })
}

export function deleteBusinessDefinition(id) {
  return request({
    url: `/centralaudit/treasury-common/business-definition/${id}`,
    method: 'delete'
  })
}

export function batchDeleteBusinessDefinition(data) {
  return request({
    url: '/centralaudit/treasury-common/business-definition/batchDelete',
    method: 'post',
    data
  })
}

export function updateBusinessDefinitionStatus(data) {
  return request({
    url: '/centralaudit/treasury-common/business-definition/updateStatus',
    method: 'post',
    data
  })
}

export function getEnabledBusinessDefinitions(orgId) {
  return request({
    url: '/centralaudit/treasury-common/business-definition/enabled',
    method: 'get',
    params: { orgId }
  })
}

// 银企直连配置管理
export function getBankConnectConfigList(query) {
  return request({
    url: '/centralaudit/treasury-common/bank-connect-config/list',
    method: 'get',
    params: query
  })
}

export function saveOrUpdateBankConnectConfig(data) {
  return request({
    url: '/centralaudit/treasury-common/bank-connect-config/saveOrUpdate',
    method: 'post',
    data
  })
}

export function deleteBankConnectConfig(id) {
  return request({
    url: `/centralaudit/treasury-common/bank-connect-config/delete/${id}`,
    method: 'delete'
  })
}

export function batchDeleteBankConnectConfig(ids) {
  return request({
    url: '/centralaudit/treasury-common/bank-connect-config/batchDelete',
    method: 'post',
    data: ids
  })
}

export function updateBankConnectConfigStatus(data) {
  return request({
    url: '/centralaudit/treasury-common/bank-connect-config/updateStatus',
    method: 'post',
    data
  })
}

export function testBankConnection(data) {
  return request({
    url: '/centralaudit/treasury-common/bank-connect-config/testConnection',
    method: 'post',
    data
  })
}

// 汇率管理
export function getExchangeRateList(query) {
  return request({
    url: '/centralaudit/treasury-common/exchange-rate/list',
    method: 'get',
    params: query
  })
}

export function saveOrUpdateExchangeRate(data) {
  return request({
    url: '/centralaudit/treasury-common/exchange-rate/saveOrUpdate',
    method: 'post',
    data
  })
}

export function deleteExchangeRate(id) {
  return request({
    url: `/centralaudit/treasury-common/exchange-rate/delete/${id}`,
    method: 'delete'
  })
}

export function batchDeleteExchangeRate(ids) {
  return request({
    url: '/centralaudit/treasury-common/exchange-rate/batchDelete',
    method: 'post',
    data: ids
  })
}

export function updateExchangeRateStatus(data) {
  return request({
    url: '/centralaudit/treasury-common/exchange-rate/updateStatus',
    method: 'post',
    data
  })
}

export function getLatestExchangeRate(query) {
  return request({
    url: '/centralaudit/treasury-common/exchange-rate/latest',
    method: 'get',
    params: query
  })
}

export function convertCurrency(data) {
  return request({
    url: '/centralaudit/treasury-common/exchange-rate/convert',
    method: 'post',
    data
  })
}

export function batchImportExchangeRate(data) {
  return request({
    url: '/centralaudit/treasury-common/exchange-rate/batchImport',
    method: 'post',
    data
  })
}

// 利率管理
export function getInterestRateList(query) {
  return request({
    url: '/centralaudit/treasury-common/interest-rate/list',
    method: 'get',
    params: query
  })
}

export function saveOrUpdateInterestRate(data) {
  return request({
    url: '/centralaudit/treasury-common/interest-rate/saveOrUpdate',
    method: 'post',
    data
  })
}

export function deleteInterestRate(id) {
  return request({
    url: `/centralaudit/treasury-common/interest-rate/delete/${id}`,
    method: 'delete'
  })
}

export function batchDeleteInterestRate(ids) {
  return request({
    url: '/centralaudit/treasury-common/interest-rate/batchDelete',
    method: 'post',
    data: ids
  })
}

export function updateInterestRateStatus(data) {
  return request({
    url: '/centralaudit/treasury-common/interest-rate/updateStatus',
    method: 'post',
    data
  })
}

export function getLatestInterestRate(query) {
  return request({
    url: '/centralaudit/treasury-common/interest-rate/latest',
    method: 'get',
    params: query
  })
}

export function calculateInterest(data) {
  return request({
    url: '/centralaudit/treasury-common/interest-rate/calculate',
    method: 'post',
    data
  })
}

export function batchImportInterestRate(data) {
  return request({
    url: '/centralaudit/treasury-common/interest-rate/batchImport',
    method: 'post',
    data
  })
}

// 业务活动规则管理
export function getBusinessRuleList(query) {
  return request({
    url: '/centralaudit/treasury-common/business-rule/list',
    method: 'get',
    params: query
  })
}

export function saveOrUpdateBusinessRule(data) {
  return request({
    url: '/centralaudit/treasury-common/business-rule/saveOrUpdate',
    method: 'post',
    data
  })
}

export function deleteBusinessRule(id) {
  return request({
    url: `/centralaudit/treasury-common/business-rule/delete/${id}`,
    method: 'delete'
  })
}

export function batchDeleteBusinessRule(ids) {
  return request({
    url: '/centralaudit/treasury-common/business-rule/batchDelete',
    method: 'post',
    data: ids
  })
}

export function updateBusinessRuleStatus(data) {
  return request({
    url: '/centralaudit/treasury-common/business-rule/updateStatus',
    method: 'post',
    data
  })
}

export function executeBusinessRule(data) {
  return request({
    url: '/centralaudit/treasury-common/business-rule/execute',
    method: 'post',
    data
  })
}

export function validateRuleScript(data) {
  return request({
    url: '/centralaudit/treasury-common/business-rule/validateScript',
    method: 'post',
    data
  })
}

export function testBusinessRule(data) {
  return request({
    url: '/centralaudit/treasury-common/business-rule/test',
    method: 'post',
    data
  })
}

export function batchImportBusinessRule(data) {
  return request({
    url: '/centralaudit/treasury-common/business-rule/batchImport',
    method: 'post',
    data
  })
}

// ==================== 缺失的API函数 ====================

/**
 * 更新印章状态
 * @param {Object} data 包含id、status、updateUser的对象
 */
export function updateSealStatus(data) {
  return request({
    url: `/centralaudit/treasury-common/seal/${data.id}/status`,
    method: 'put',
    data: {
      status: data.status,
      updateUser: data.updateUser
    }
  })
}

// ==================== 汇率管理相关接口 ====================
// getExchangeRateList 函数已在第496行定义，此处删除重复定义

// saveOrUpdateExchangeRate 函数已在第504行定义，此处删除重复定义

// deleteExchangeRate 函数已在第512行定义，此处删除重复定义

export function getExchangeRateById(id) {
  return request({
    url: `/centralaudit/treasury-common/exchange-rate/${id}`,
    method: 'get'
  })
}

// 电票账户配置管理相关接口
export function getETicketAccountList(params) {
  // 转换参数名以匹配后端Controller
  const backendParams = {
    pageNum: params.page || params.pageNum || 1,
    pageSize: params.limit || params.pageSize || 20,
    accountNumber: params.accountNumber,
    accountName: params.accountName,
    eTicketSystem: params.eTicketSystem,
    accountType: params.accountType,
    bankCode: params.bankCode
  }
  return request({
    url: '/cwgxAi/basicConfig/eTicketAccount/list',
    method: 'get',
    params: backendParams
  })
}

export function saveOrUpdateETicketAccount(data) {
  return request({
    url: '/cwgxAi/basicConfig/eTicketAccount/saveOrUpdate',
    method: 'post',
    data
  })
}

export function deleteETicketAccount(id) {
  return request({
    url: `/cwgxAi/basicConfig/eTicketAccount/${id}`,
    method: 'delete'
  })
}

export function getETicketAccountById(id) {
  return request({
    url: `/cwgxAi/basicConfig/eTicketAccount/${id}`,
    method: 'get'
  })
}

export function checkAccountNo(params) {
  return request({
    url: '/cwgxAi/basicConfig/eTicketAccount/checkAccountNo',
    method: 'post',
    data: params
  })
}

export function validateAccount(params) {
  return request({
    url: '/cwgxAi/basicConfig/eTicketAccount/validateAccount',
    method: 'post',
    data: params
  })
}

export function getETicketTypes() {
  return request({
    url: '/cwgxAi/basicConfig/eTicketAccount/eTicketTypes',
    method: 'get'
  })
}

// 印鉴组合配置管理相关接口
export function getSealCombinationList(params) {
  // 参数映射：前端 page/limit -> 后端 pageNo/pageSize
  const mappedParams = {
    pageNo: params.page || 1,
    pageSize: params.limit || 20,
    combinationCode: params.combinationCode,
    combinationName: params.combinationName,
    combinationType: params.combinationType,
    businessType: params.businessType,
    authorityLevel: params.authorityLevel
  }
  return request({
    url: '/cwgxAi/basicConfig/sealCombination/list',
    method: 'get',
    params: mappedParams
  })
}

export function saveOrUpdateSealCombination(data) {
  return request({
    url: '/cwgxAi/basicConfig/sealCombination/saveOrUpdate',
    method: 'post',
    data
  })
}

export function deleteSealCombination(id) {
  return request({
    url: `/cwgxAi/basicConfig/sealCombination/delete?id=${id}`,
    method: 'post'
  })
}

// 印鉴使用记录管理相关接口
export function getSealUsageRecordList(params) {
  return request({
    url: '/centralaudit/treasury-common/seal-usage-record/list',
    method: 'get',
    params
  })
}

export function saveOrUpdateSealUsageRecord(data) {
  return request({
    url: '/centralaudit/treasury-common/seal-usage-record/saveOrUpdate',
    method: 'post',
    data
  })
}

export function deleteSealUsageRecord(id) {
  return request({
    url: `/centralaudit/treasury-common/seal-usage-record/${id}`,
    method: 'delete'
  })
}

// 第三方账户管理相关接口
export function getThirdPartyAccountList(params) {
  return request({
    url: '/centralaudit/treasury-common/third-party-account/list',
    method: 'get',
    params
  })
}

export function saveOrUpdateThirdPartyAccount(data) {
  return request({
    url: '/centralaudit/treasury-common/third-party-account/saveOrUpdate',
    method: 'post',
    data
  })
}

export function deleteThirdPartyAccount(id) {
  return request({
    url: `/centralaudit/treasury-common/third-party-account/${id}`,
    method: 'delete'
  })
}

// 交易类型管理相关接口
export function getTransactionTypeList(params) {
  return request({
    url: '/centralaudit/treasury-common/transaction-type/list',
    method: 'get',
    params
  })
}

export function saveOrUpdateTransactionType(data) {
  return request({
    url: '/centralaudit/treasury-common/transaction-type/saveOrUpdate',
    method: 'post',
    data
  })
}

export function deleteTransactionType(id) {
  return request({
    url: `/centralaudit/treasury-common/transaction-type/${id}`,
    method: 'delete'
  })
}

// 银企直连管理相关接口
export function getBankDirectConnectionList(params) {
  return request({
    url: '/centralaudit/treasury-common/bank-direct-connection/list',
    method: 'get',
    params
  })
}

export function saveOrUpdateBankDirectConnection(data) {
  return request({
    url: '/centralaudit/treasury-common/bank-direct-connection/saveOrUpdate',
    method: 'post',
    data
  })
}

export function deleteBankDirectConnection(id) {
  return request({
    url: `/centralaudit/treasury-common/bank-direct-connection/${id}`,
    method: 'delete'
  })
}

// 产品利息规则管理相关接口
export function getProductInterestRuleList(params) {
  return request({
    url: '/centralaudit/treasury-common/product-interest-rule/list',
    method: 'get',
    params
  })
}

export function saveOrUpdateProductInterestRule(data) {
  return request({
    url: '/centralaudit/treasury-common/product-interest-rule/saveOrUpdate',
    method: 'post',
    data
  })
}

export function deleteProductInterestRule(id) {
  return request({
    url: `/centralaudit/treasury-common/product-interest-rule/${id}`,
    method: 'delete'
  })
}

// 业务系统注册配置管理相关接口
export function getBusinessSystemRegisterList(params) {
  return request({
    url: '/cwgxAi/basicConfig/system/list',
    method: 'get',
    params
  })
}

export function saveOrUpdateBusinessSystemRegister(data) {
  return request({
    url: '/cwgxAi/basicConfig/system/saveOrUpdate',
    method: 'post',
    data
  })
}

export function deleteBusinessSystemRegister(id) {
  return request({
    url: `/cwgxAi/basicConfig/system/${id}`,
    method: 'delete'
  })
}

export function getBusinessSystemRegisterById(id) {
  return request({
    url: `/cwgxAi/basicConfig/system/${id}`,
    method: 'get'
  })
}

export function getSystemTypes() {
  return request({
    url: '/cwgxAi/basicConfig/system/systemTypes',
    method: 'get'
  })
}

export function getSystemStatuses() {
  return request({
    url: '/cwgxAi/basicConfig/system/systemStatuses',
    method: 'get'
  })
}

// 数据映射配置管理相关接口
export function getDataMappingConfigList(params) {
  return request({
    url: '/cwgxAi/basicConfig/mapping/list',
    method: 'get',
    params
  })
}

export function saveOrUpdateDataMappingConfig(data) {
  return request({
    url: '/cwgxAi/basicConfig/mapping/saveOrUpdate',
    method: 'post',
    data
  })
}

export function deleteDataMappingConfig(id) {
  return request({
    url: `/cwgxAi/basicConfig/mapping/${id}`,
    method: 'delete'
  })
}

export function getDataMappingConfigById(id) {
  return request({
    url: `/cwgxAi/basicConfig/mapping/${id}`,
    method: 'get'
  })
}

export function getMappingTypes() {
  return request({
    url: '/cwgxAi/basicConfig/mapping/mappingTypes',
    method: 'get'
  })
}

export function getSourceSystems() {
  return request({
    url: '/cwgxAi/basicConfig/mapping/sourceSystems',
    method: 'get'
  })
}

export function getTargetSystems() {
  return request({
    url: '/cwgxAi/basicConfig/mapping/targetSystems',
    method: 'get'
  })
}

// 印鉴档案管理相关接口
export function getSealArchiveList(params) {
  return request({
    url: '/centralaudit/treasury-common/seal-archive/list',
    method: 'get',
    params
  })
}

export function saveOrUpdateSealArchive(data) {
  return request({
    url: '/centralaudit/treasury-common/seal-archive/saveOrUpdate',
    method: 'post',
    data
  })
}

export function deleteSealArchive(id) {
  return request({
    url: `/centralaudit/treasury-common/seal-archive/${id}`,
    method: 'delete'
  })
}

export function getSealArchiveById(id) {
  return request({
    url: `/centralaudit/treasury-common/seal-archive/${id}`,
    method: 'get'
  })
}

export function getSealArchiveStatistics() {
  return request({
    url: '/centralaudit/treasury-common/seal-archive/statistics',
    method: 'get'
  })
}

// Ukey厂商管理相关接口
export function getUkeyVendorList(params) {
  // 参数映射：前端 page/limit -> 后端 pageNo/pageSize
  const mappedParams = {
    pageNo: params.page || 1,
    pageSize: params.limit || 20,
    vendorName: params.vendorName,
    vendorType: params.vendorType,
    vendorLevel: params.vendorLevel,
    cooperationStatus: params.cooperationStatus
  }
  return request({
    url: '/zbgl/centralaudit/treasury/common/ukeyVendor/list',
    method: 'get',
    params: mappedParams
  })
}

export function saveOrUpdateUkeyVendor(data) {
  return request({
    url: '/zbgl/centralaudit/treasury/common/ukeyVendor/saveOrUpdate',
    method: 'post',
    data
  })
}

export function deleteUkeyVendor(id) {
  return request({
    url: `/zbgl/centralaudit/treasury/common/ukeyVendor/delete?id=${id}`,
    method: 'post'
  })
}

export function getUkeyVendorById(id) {
  return request({
    url: `/zbgl/centralaudit/treasury/common/ukeyVendor/detail?id=${id}`,
    method: 'get'
  })
}
