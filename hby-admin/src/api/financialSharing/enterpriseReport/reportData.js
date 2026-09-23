import request from '@/utils/request'

/**
 * 查询报表数据列表
 */
export function getReportDataList(data) {
  return request({
    url: '/cwgxAi/enterpriseReport/reportData/getList',
    method: 'post',
    data
  })
}

/**
 * 查询报表数据详情
 */
export function getReportDataDetail(dataId) {
  return request({
    url: '/cwgxAi/enterpriseReport/reportData/detail',
    method: 'post',
    data: { dataId }
  })
}

/**
 * 保存报表数据
 */
export function saveReportData(data) {
  return request({
    url: '/cwgxAi/enterpriseReport/reportData/save',
    method: 'post',
    data
  })
}

/**
 * 批量保存报表数据
 */
export function batchSaveReportData(dataList) {
  return request({
    url: '/cwgxAi/enterpriseReport/reportData/batchSave',
    method: 'post',
    data: dataList
  })
}

/**
 * 删除报表数据
 */
export function deleteReportData(dataId) {
  return request({
    url: '/cwgxAi/enterpriseReport/reportData/delete',
    method: 'post',
    data: { dataId }
  })
}

/**
 * 根据条件删除报表数据
 */
export function deleteReportDataByCondition(data) {
  return request({
    url: '/cwgxAi/enterpriseReport/reportData/deleteByCondition',
    method: 'post',
    data
  })
}

/**
 * 根据条件查询报表数据
 */
export function getReportDataByCondition(data) {
  return request({
    url: '/cwgxAi/enterpriseReport/reportData/getDataByCondition',
    method: 'post',
    data
  })
}

