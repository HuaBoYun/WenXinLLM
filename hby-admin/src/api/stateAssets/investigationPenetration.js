import request from '@/utils/request'
import { transData } from '@/utils/requestData'

// ========== 核查任务 ==========
export function getInvestigationTaskList(data) {
  return request({
    url: '/monitor/v1/supervision/investigation/task/list',
    method: 'post',
    data: transData(data),
  })
}

export function getInvestigationTaskDetail(id) {
  return request({
    url: '/monitor/v1/supervision/investigation/task/' + id,
    method: 'get',
  })
}

export function addInvestigationTask(data) {
  return request({
    url: '/monitor/v1/supervision/investigation/task/add',
    method: 'post',
    data: transData(data),
  })
}

export function updateInvestigationTask(data) {
  return request({
    url: '/monitor/v1/supervision/investigation/task/update',
    method: 'post',
    data: transData(data),
  })
}

export function deleteInvestigationTask(id) {
  return request({
    url: '/monitor/v1/supervision/investigation/task/' + id,
    method: 'delete',
  })
}

export function batchDeleteInvestigationTask(data) {
  return request({
    url: '/monitor/v1/supervision/investigation/task/batch/delete',
    method: 'post',
    data: transData(data),
  })
}

export function getInvestigationStatistics(companyId) {
  return request({
    url: '/monitor/v1/supervision/investigation/task/statistics',
    method: 'get',
    params: { companyId },
  })
}

// ========== 整改记录 ==========
export function getRectificationRecordList(data) {
  return request({
    url: '/monitor/v1/supervision/investigation/task/rectification/list',
    method: 'post',
    data: transData(data),
  })
}

export function getRectificationRecordDetail(id) {
  return request({
    url: '/monitor/v1/supervision/investigation/task/rectification/' + id,
    method: 'get',
  })
}

export function addRectificationRecord(data) {
  return request({
    url: '/monitor/v1/supervision/investigation/task/rectification/add',
    method: 'post',
    data: transData(data),
  })
}

export function updateRectificationRecord(data) {
  return request({
    url: '/monitor/v1/supervision/investigation/task/rectification/update',
    method: 'post',
    data: transData(data),
  })
}

