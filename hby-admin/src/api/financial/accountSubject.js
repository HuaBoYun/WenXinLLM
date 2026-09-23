import { transData } from '@/utils/requestData'
import request from '@/utils/request'

// 会计科目管理-分页查询
export function getList(params) {
  return request({
    url: '/cwgxAi/common/subject/getList',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: JSON.stringify(transData(params)),
  })
}

// 会计科目管理-根据ID查询详情
export function getAccountSubjectById(subjectId) {
  return request({
    url: `/cwgxAi/common/subject/${subjectId}`,
    method: 'get',
  })
}

// 会计科目管理-新增或更新
export function saveOrUpdate(params) {
  return request({
    url: '/cwgxAi/common/subject/saveOrUpdate',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: JSON.stringify(transData(params)),
  })
}

// 会计科目管理-删除
export function deleteAccountSubject(subjectId) {
  return request({
    url: `/cwgxAi/common/subject/${subjectId}`,
    method: 'delete',
  })
}

// 会计科目管理-批量删除
export function batchDelete(subjectIds) {
  return request({
    url: '/cwgxAi/common/subject/batch',
    method: 'delete',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: JSON.stringify(transData({ subjectIds })),
  })
}

// 会计科目管理-更新状态
export function updateStatus(subjectId, isEnabled) {
  return request({
    url: `/cwgxAi/common/subject/${subjectId}/status?isEnabled=${isEnabled}`,
    method: 'put',
  })
}

// 会计科目管理-批量更新状态
export function batchUpdateStatus(subjectIds, isEnabled) {
  return request({
    url: `/cwgxAi/common/subject/batch/status?isEnabled=${isEnabled}`,
    method: 'put',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: JSON.stringify(transData({ subjectIds })),
  })
}

// 会计科目管理-检查科目编码
export function checkSubjectCode(params) {
  return request({
    url: '/cwgxAi/common/subject/check-code',
    method: 'get',
    params: transData(params),
  })
}

// 会计科目管理-获取科目树形结构
export function getAccountSubjectTree(params) {
  return request({
    url: '/cwgxAi/common/subject/tree',
    method: 'get',
    params: transData(params),
  })
}

// 会计科目管理-根据科目类型查询
export function getAccountSubjectsByType(subjectType, params) {
  return request({
    url: `/cwgxAi/common/subject/type/${subjectType}`,
    method: 'get',
    params: transData(params),
  })
}

// 会计科目管理-根据上级科目查询子科目
export function getAccountSubjectsByParentId(params) {
  return request({
    url: '/cwgxAi/common/subject/children',
    method: 'get',
    params: transData(params),
  })
}

// 会计科目管理-刷新缓存
export function refreshCache(bookId) {
  return request({
    url: `/cwgxAi/common/subject/refresh-cache?bookId=${bookId}`,
    method: 'post',
  })
}