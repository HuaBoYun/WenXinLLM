import request from '@/utils/request'

/**
 * 查询报表任务列表
 */
export function getReportTaskList(data) {
  return request({
    url: '/cwgxAi/enterpriseReport/reportTask/getList',
    method: 'post',
    data
  })
}

/**
 * 查询报表任务详情
 */
export function getReportTaskDetail(taskId) {
  return request({
    url: '/cwgxAi/enterpriseReport/reportTask/detail',
    method: 'post',
    data: { taskId }
  })
}

/**
 * 保存报表任务
 */
export function saveReportTask(data) {
  return request({
    url: '/cwgxAi/enterpriseReport/reportTask/save',
    method: 'post',
    data
  })
}

/**
 * 删除报表任务
 */
export function deleteReportTask(taskId) {
  return request({
    url: '/cwgxAi/enterpriseReport/reportTask/delete',
    method: 'post',
    data: { taskId }
  })
}

/**
 * 发布报表任务
 */
export function publishReportTask(taskId) {
  return request({
    url: '/cwgxAi/enterpriseReport/reportTask/publish',
    method: 'post',
    data: { taskId }
  })
}

/**
 * 撤回报表任务
 */
export function withdrawReportTask(taskId) {
  return request({
    url: '/cwgxAi/enterpriseReport/reportTask/withdraw',
    method: 'post',
    data: { taskId }
  })
}

/**
 * 根据表单组ID查询报表任务列表
 */
export function getReportTaskListByGroupId(groupId) {
  return request({
    url: '/cwgxAi/enterpriseReport/reportTask/getListByGroupId',
    method: 'post',
    data: { groupId }
  })
}

