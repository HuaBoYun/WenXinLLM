import request from '@/utils/request'
import { transData } from '@/utils/requestData'

// 风险监测指标创建列表
export function getList(data) {
  return request({
    url: '/riskcontrol/MonitoringFill/getList',
    method: 'post',
    data: transData(data),
  })
}
// 风险监测指标创建新增/修改
export function saveOrUpdate(data) {
  return request({
    url: '/riskcontrol/MonitoringFill/saveOrUpdate',
    method: 'post',
    data: transData(data),
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
  })
}
// 风险监测指标填报删除
export function deleteItem(data) {
  return request({
    url: '/riskcontrol/MonitoringFill/delete',
    method: 'post',
    data: transData(data),
  })
}
// 风险监测指标填报详情
export function detail(data) {
  return request({
    url: '/riskcontrol/MonitoringFill/details',
    method: 'post',
    data: transData(data),
  })
}
// 风险监测指标填报上报
export function reportToLeader(data) {
  return request({
    url: '/riskcontrol/MonitoringFill/reportToLeader',
    method: 'post',
    data: transData(data),
  })
}
// 风险监测指标填报作废
export function monitoringCancel(data) {
  return request({
    url: '/riskcontrol/MonitoringSummary/monitoringCancel',
    method: 'post',
    data: transData(data),
  })
}


//风险监测指标汇总-导出
export function exportExcel(params) {
  return request({
    url: '/riskcontrol/MonitoringSummary/export',
    method: 'post',
    data: transData(params),
    responseType: 'blob',
  })
}
//风险监测指标汇总-导出
export function getHzList(params) {
  return request({
    url: '/riskcontrol/MonitoringSummary/getHzList',
    method: 'post',
    data: transData(params),
  })
}
export function getRiskMonDeptList(params) {
  return request({
    url: '/riskcontrol/MonitoringFill/getRiskMonDeptList',
    method: 'post',
    params: transData(params),
  })
}

//风险监测指标汇总-导出
export function getZdHzListNew(params) {
  return request({
    url: '/riskcontrol/MonitoringSummary/getZdHzListNew',
    method: 'post',
    data: transData(params),
  })
}

//风险监测指标汇总-导出
export function zdExportNew(params) {
  return request({
    url: '/riskcontrol/MonitoringSummary/zdExportNew',
    method: 'post',
    data: transData(params),
    responseType: 'blob',
  })
}
