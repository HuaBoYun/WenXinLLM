import request from '@/utils/request'

export function getBusinessRuleList(query) {
  return request({
    url: '/qqsk/financial/xjgl/dataRulesManage/businessRule/getList',
    method: 'post',
    params: query
  })
}

export function createBusinessRule(data) {
  return request({
    url: '/qqsk/financial/xjgl/dataRulesManage/businessRule/create',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    data
  })
}

export function updateBusinessRule(data) {
  return request({
    url: '/qqsk/financial/xjgl/dataRulesManage/businessRule/update',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    data
  })
}

export function deleteBusinessRule(data) {
  return request({
    url: '/qqsk/financial/xjgl/dataRulesManage/businessRule/delete',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    data
  })
}

export function getBusinessRuleDetail(ruleId) {
  return request({
    url: '/qqsk/financial/xjgl/dataRulesManage/businessRule/getDetail',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    data: { ruleId }
  })
}

export function getBusinessRuleStatistics() {
  return request({
    url: '/qqsk/financial/xjgl/dataRulesManage/businessRule/getStatistics',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function updateBusinessRuleStatus(data) {
  return request({
    url: '/qqsk/financial/xjgl/dataRulesManage/businessRule/updateStatus',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    data
  })
}
