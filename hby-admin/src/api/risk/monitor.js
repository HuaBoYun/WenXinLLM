/*
 * @Author: 康某 dev@example.com
 * @Date: 2022-12-08 21:08:35
 * @LastEditors: 康某 dev@example.com
 * @LastEditTime: 2022-12-26 00:06:20
 * @FilePath: \hb-admin\src\api\risk\monitor.js
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
 */
import request from '@/utils/request'
import { transData } from '@/utils/requestData'

// 供应商监控主列表

// 风险创建-风险类型判断
export function getTableList(params) {
  return request({
    url: '/riskcontrol/fxxt/fxyj/fxyj_list',
    method: 'get',
    params: transData(params),
  })
}
// 供应商监控走马灯
export function getTabListData(params) {
  return request({
    url: '/riskcontrol/fxxt/riskwarning/main',
    method: 'get',
    params: transData(params),
  })
}
// 下属公司监控走马灯
export function getGroupListData(params) {
  return request({
    url: '/riskcontrol/subsidiary/group_list',
    method: 'get',
    params: transData(params),
  })
}

// 新增修改供应商监控走马灯
export function addOrganization(params) {
  return request({
    url: '/riskcontrol/fxyj/saveteam',
    method: 'post',
    data: transData(params),
  })
}
// 添加公司
export function saveCompanyInfo(params) {
  return request({
    url: '/riskcontrol/fxyj/savecompany',
    method: 'post',
    data: transData(params),
  })
}
// 修改公司
export const updateCompanyInfo = (params) => {
  return request({
    url: '/riskcontrol/fxyj/updatecompany',
    method: 'post',
    data: transData(params),
  })
}

//
export const getGyDetail = (params) => {
  return request({
    url: '/riskcontrol/fxxt/gysjkDetail',
    method: 'get',
    params: transData(params),
  })
}
//删除分组

export const deleteGroupItem = (params) => {
  return request({
    url: '/riskcontrol/fxyj/delteam',
    method: 'get',
    params: transData(params),
  })
}

// 删除分组下的公司3

export const deleteGroupCompany = (params) => {
  return request({
    url: '/riskcontrol/fxyj/delcompany',
    method: 'get',
    params: transData(params),
  })
}
