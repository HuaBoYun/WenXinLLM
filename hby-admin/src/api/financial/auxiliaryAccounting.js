import { transData } from '@/utils/requestData'
import request from '@/utils/request'

// 辅助核算-分页查询
export function getList(params) {
  return request({
    url: '/cwgxAi/common/auxiliary/getList',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: JSON.stringify(transData(params)),
  })
}

// 辅助核算-根据ID查询详情
export function getAuxiliaryById(auxiliaryId) {
  return request({
    url: `/cwgxAi/common/auxiliary/${auxiliaryId}`,
    method: 'get',
  })
}

// 辅助核算-新增或更新
export function saveOrUpdate(params) {
  return request({
    url: '/cwgxAi/common/auxiliary/saveOrUpdate',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: JSON.stringify(transData(params)),
  })
}

// 辅助核算-删除
export function deleteAuxiliary(auxiliaryId) {
  return request({
    url: `/cwgxAi/common/auxiliary/${auxiliaryId}`,
    method: 'delete',
  })
}

// 辅助核算-批量删除
export function batchDelete(auxiliaryIds) {
  return request({
    url: '/cwgxAi/common/auxiliary/batch',
    method: 'delete',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: JSON.stringify(transData({ auxiliaryIds })),
  })
}

// 辅助核算-更新状态
export function updateStatus(auxiliaryId, isEnabled) {
  return request({
    url: `/cwgxAi/common/auxiliary/${auxiliaryId}/status?isEnabled=${isEnabled}`,
    method: 'put',
  })
}

// 辅助核算-批量更新状态
export function batchUpdateStatus(auxiliaryIds, isEnabled) {
  return request({
    url: `/cwgxAi/common/auxiliary/batch/status?isEnabled=${isEnabled}`,
    method: 'put',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: JSON.stringify(transData({ auxiliaryIds })),
  })
}

// 辅助核算-检查核算编码
export function checkAuxiliaryCode(params) {
  return request({
    url: '/cwgxAi/common/auxiliary/check-code',
    method: 'get',
    params: transData(params),
  })
}

// 辅助核算-获取核算项树形结构
export function getAuxiliaryTree(params) {
  return request({
    url: '/cwgxAi/common/auxiliary/tree',
    method: 'get',
    params: transData(params),
  })
}

// 辅助核算-根据核算类型查询
export function getAuxiliariesByType(auxiliaryType, params) {
  return request({
    url: `/cwgxAi/common/auxiliary/type/${auxiliaryType}`,
    method: 'get',
    params: transData(params),
  })
}

// 辅助核算-根据上级核算项查询子项
export function getAuxiliariesByParentId(params) {
  return request({
    url: '/cwgxAi/common/auxiliary/children',
    method: 'get',
    params: transData(params),
  })
}

// 辅助核算-刷新缓存
export function refreshCache(bookId) {
  return request({
    url: `/cwgxAi/common/auxiliary/refresh-cache?bookId=${bookId}`,
    method: 'post',
  })
}