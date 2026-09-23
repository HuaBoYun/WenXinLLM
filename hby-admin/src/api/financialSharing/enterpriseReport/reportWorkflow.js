import request from '@/utils/request'

/**
 * 查询报表工作流列表
 */
export function getReportWorkflowList(data) {
  return request({
    url: '/cwgxAi/enterpriseReport/reportWorkflow/getList',
    method: 'post',
    data
  })
}

/**
 * 查询报表工作流详情
 */
export function getReportWorkflowDetail(workflowId) {
  return request({
    url: '/cwgxAi/enterpriseReport/reportWorkflow/detail',
    method: 'post',
    data: { workflowId }
  })
}

/**
 * 保存报表工作流
 */
export function saveReportWorkflow(data) {
  return request({
    url: '/cwgxAi/enterpriseReport/reportWorkflow/save',
    method: 'post',
    data
  })
}

/**
 * 删除报表工作流
 */
export function deleteReportWorkflow(workflowId) {
  return request({
    url: '/cwgxAi/enterpriseReport/reportWorkflow/delete',
    method: 'post',
    data: { workflowId }
  })
}

/**
 * 根据任务ID查询报表工作流列表
 */
export function getReportWorkflowListByTaskId(taskId) {
  return request({
    url: '/cwgxAi/enterpriseReport/reportWorkflow/getListByTaskId',
    method: 'post',
    data: { taskId }
  })
}

/**
 * 更新工作流状态
 */
export function updateReportWorkflowStatus(workflowId, status) {
  return request({
    url: '/cwgxAi/enterpriseReport/reportWorkflow/updateStatus',
    method: 'post',
    data: { workflowId, status }
  })
}

