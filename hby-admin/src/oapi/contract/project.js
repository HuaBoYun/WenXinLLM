import request from '@/utils/request'
import { transData } from '@/utils/requestData'

//计划管理列表
export function contractProList(params) {
  return request({
    url: '/contract/contractPro/htlx/contract_pro_list',
    method: 'get',
    params: transData(params),
  })
}

//立项管理-新增与修改
export function contractProSave(params) {
  return request({
    url: '/contract/contractPro/htlx/contract_pro_save',
    method: 'post',
    data: transData(params),
  })
}

//立项管理- 编号
export function contractProGetNo(params) {
  return request({
    url: '/contract/contractPro/htlx/contract_pro_getNo',
    method: 'get',
    params: transData(params),
  })
}

//立项管理-删除
export function contractProDel(params) {
  return request({
    url: '/contract/contractPro/htlx/contract_pro_del',
    method: 'get',
    params: transData(params),
  })
}

//立项管理-明细
export function contractProDetail(params) {
  return request({
    url: '/contract/contractPro/htlx/contract_pro_detail',
    method: 'get',
    params: transData(params),
  })
}

//立项管理-上传附件接口
// export function uploadFileAttInfo(params) {
//   return request({
//     url: '/contract/contractPro/uploadFileAttInfo',
//     method: 'get',
//     params: transData(params),
//   })
// }

//删除文件
export function deleteFileRelation(params) {
  return request({
    url: '/contract/contractPro/deleteFileRelation',
    method: 'post',
    data: transData(params),
  })
}
//文件下载
export function download(params) {
  return request({
    url: '/contract/contractPro/download',
    method: 'get',
    params: transData(params),
  })
}

//选择人员列表
export function userList(params) {
  return request({
    url: '/contract/contractPro/user/user_list',
    method: 'get',
    params: transData(params),
  })
}

//选择部门列表
export function orgList(params) {
  return request({
    url: '/contract/contractPro/user/org_list',
    method: 'get',
    params: transData(params),
  })
}

//项目立项-附件列表
export function fileList(params) {
  return request({
    url: '/contract/contractPro/htlx/file_list',
    method: 'get',
    params: transData(params),
  })
}
