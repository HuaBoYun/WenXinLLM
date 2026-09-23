import request from '@/utils/request'

/**
 * 查询合并模型列表
 */
export function getModelList(data) {
  return request({
    url: '/cwgxAi/consolidationReport/consolidationModel/getModelList',
    method: 'post',
    data
  })
}

/**
 * 根据ID查询合并模型
 */
export function getModelById(data) {
  return request({
    url: '/cwgxAi/consolidationReport/consolidationModel/getModelById',
    method: 'post',
    data
  })
}

/**
 * 新增合并模型
 */
export function saveModel(data) {
  return request({
    url: '/cwgxAi/consolidationReport/consolidationModel/saveModel',
    method: 'post',
    data
  })
}

/**
 * 修改合并模型
 */
export function updateModel(data) {
  return request({
    url: '/cwgxAi/consolidationReport/consolidationModel/updateModel',
    method: 'post',
    data
  })
}

/**
 * 删除合并模型
 */
export function deleteModel(data) {
  return request({
    url: '/cwgxAi/consolidationReport/consolidationModel/deleteModel',
    method: 'post',
    data
  })
}

/**
 * 更新模型状态
 */
export function updateModelStatus(data) {
  return request({
    url: '/cwgxAi/consolidationReport/consolidationModel/updateModelStatus',
    method: 'post',
    data
  })
}

