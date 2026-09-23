import request from '@/utils/request'
import { transData } from '@/utils/requestData'

//测试任务-主页
export function controlTestImplList(params) {
  return request({
    url: '/nkhg/nbkz/nkcs/impl/control_test_impl_list',
    method: 'get',
    params: transData(params),
  })
}

//测试任务-左侧树
export function getLeftTree(params) {
  return request({
    url: '/nkhg/nbkz/csrw/gettree',
    method: 'get',
    params: transData(params),
  })
}
//测试任务-左侧树(新 2025-5-14)
export function getDefgzTree(params) {
  return request({
    url: '/nkhg/nbkz/csrw/defgz_list',
    method: 'get',
    params: transData(params),
  })
}

//测试任务-右侧列表
export function getRightList(params) {
  return request({
    url: '/nkhg/nbkz/csrw/def_list',
    method: 'get',
    params: transData(params),
  })
}

//测试任务-全部提交
export function saveall(params) {
  return request({
    url: '/nkhg/nbkz/csrw/saveall',
    method: 'post',
    params: transData(params),
  })
}

//测试任务-右侧列表-点击编号查看详情
export function elemendatail(params) {
  return request({
    url: '/nkhg/nbkz/csmb/elemendatail',
    method: 'get',
    params: transData(params),
  })
}

//测试任务-右侧列表-点击修改/测试查询
export function addtask(params) {
  return request({
    url: '/nkhg/nbkz/csrw/addtask',
    method: 'get',
    params: transData(params),
  })
}

//测试任务-修改/测试页面-附件删除
export function delFile(params) {
  return request({
    url: '/nkhg/nbkz/csrw/control_test_impl_upload_del',
    method: 'post',
    params: transData(params),
  })
}

//测试任务-修改/测试页面-保存测试
export function controlTestImplSave(params) {
  return request({
    url: '/nkhg/nbkz/csrw/control_test_impl_save',
    method: 'post',
    data: transData(params),
    headers: {
      'Content-Type': 'application/json',
    },
  })
}

//测试任务-问题发现附件删除
export function delFjByTypeAndId(params) {
  return request({
    url: '/nkhg/nbkz/delFjByTypeAndId',
    method: 'post',
    data: transData(params),
  })
}

// 问题台账导出
export function exportProblemLedger(params) {
  return request({
    url: '/nkhg//nbkz/csrw/exportProblemLedger',
    method: 'post',
    responseType: 'blob',
    data: transData(params || {}),
  })
}

//测试任务-问题发现附件列表
export function getFjListByFind(params) {
  return request({
    url: '/nkhg/nbkz/getFjListByFind',
    method: 'post',
    data: transData(params),
  })
}

//测试任务-问题台账列表
export function getProblemLedgerList(params) {
  return request({
    url: '/nkhg/nbkz/csrw/getProblemLedgerList',
    method: 'get',
    params: transData(params),
  })
}

//问题台账-发起整改
export function sendreform(params) {
  return request({
    url: '/nkhg/nbkz/csrw/sendreform',
    method: 'post',
    data: transData(params),
  })
}

//测试任务-问题发现-删除
export function testtaskProfindDel(params) {
  return request({
    url: '/nkhg/nbkz/csrw/testtask_profind_del',
    method: 'get',
    params: transData(params),
  })
}

//测试任务-问题发现-查看明细
export function testtaskProfindDetail(params) {
  return request({
    url: '/nkhg/nbkz/csrw/testtask_profind_detail',
    method: 'get',
    params: transData(params),
  })
}

//测试任务-问题发现-查询
export function testtaskProfindList(params) {
  return request({
    url: '/nkhg/nbkz/csrw/testtask_profind_list',
    method: 'get',
    params: transData(params),
  })
}

//测试任务-问题发现-新增修改
export function testtaskProfindSave(params) {
  const { attids, ...restParams } = params

  return request({
    url: '/nkhg/nbkz/csrw/testtask_profind_save',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    params: attids ? { attids } : undefined,
    data: transData(restParams),
  })
}
//测试任务-问题发现-维护
export function testtaskProfindMaintenance(params) {

  return request({
    url: '/nkhg/nbkz/csrw/wttz_update',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: transData(params),
  })
}

//测试任务-问题发现-查询
export function testtaskProfindTesttaskid(params) {
  return request({
    url: '/nkhg/nbkz/csrw/testtask_profind_testtaskid',
    method: 'get',
    params: transData(params),
  })
}

//测试任务-问题台账列表 wgzz汇总
export function getProblemLedgerListwgzz(params) {
  return request({
    url: '/nkhg/nbkz/csrw/getProblemLedgerList',
    method: 'get',
    params: transData(params),
  })
}
//测试任务-详情接口
export function getWTFXDetail(params) {
  return request({
    url: '/nkhg/nbkz/csrw/testtask_profind_detail',
    method: 'get',
    params: transData(params),
  })
}


//测试任务-现行标准新增
export function addXXBZ(params) {
  return request({
    url: '/nkhg/nbkz/addXXBZ',
    method: 'post',
    data: transData(params),
    headers: {
      'Content-Type': 'application/json',
    },
  })
}
//测试任务-现行标准删除
export function removeXXBZ(params) {
  return request({
    url: '/nkhg/nbkz/removeXXBZ',
    method: 'post',
    data: transData(params),
    headers: {
      'Content-Type': 'application/json',
    },
  })
}
//测试任务-现行标准列表
export function getXXBZ(params) {
  return request({
    url: '/nkhg/nbkz/getXXBZ',
    method: 'get',
    params: transData(params),
  })
}
//问题台账-转发
export function toIssueLedger(params) {
  return request({
    url: '/nkhg/nbkz/csrw/toIssueLedger',
    method: 'post',
    data: transData(params),
  })
}
//问题台账-转发记录
export function getProblemTransferList(params) {
  return request({
    url: '/nkhg/nbkz/getProblemTransferList',
    method: 'get',
    params: transData(params),
  })
}
//问题台账-详情
export function getProfindDetail(params) {
  return request({
    url: '/nkhg/nbkz/csrw/testtask_profind_detail',
    method: 'get',
    params: transData(params),
  })
}
