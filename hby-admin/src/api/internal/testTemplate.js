import request from '@/utils/request'
import { transData } from '@/utils/requestData'

//测试模板-列表
export function defTmplList(params) {
  return request({
    url: '/nkhg/nbkz/csmb/def_tmpl_list',
    method: 'get',
    params: transData(params),
  })
}

//测试模板-新建-编号生成
export function getAutoNumber(params) {
  return request({
    url: '/nkhg/nbkz/csmb/getAutoNumber',
    method: 'get',
    params: transData(params),
  })
}


//测试模板-删除
export function tempdelete(params) {
  return request({
    url: '/nkhg/nbkz/csmb/tempdelete',
    method: 'post',
    data: transData(params),
  })
}

//测试模板-修改保存信息
export function defTmplModify(params) {
  return request({
    url: '/nkhg/nbkz/csmb/def_tmpl_modify',
    method: 'post',
    data: transData(params),
  })
}

//测试模板-新增保存信息
export function defTmplSave(params) {
  return request({
    url: '/nkhg/nbkz/csmb/def_tmpl_save',
    method: 'post',
    data: transData(params),
  })
}

//测试模板下发功能
export function saveissued(params) {
  return request({
    url: '/nkhg/nbkz/csmb/saveissued',
    method: 'post',
    data: transData(params),
  })
}

//测试模板-测试类型新增保存
export function typeSave(params) {
  return request({
    url: '/nkhg/nbkz/csmb/type_save',
    method: 'post',
    data: transData(params),
  })
}

///测试模板-测试类型修改保存
export function typeModify(params) {
  return request({
    url: '/nkhg/nbkz/csmb/type_modify',
    method: 'post',
    data: transData(params),
  })
}

//测试模板-测试类型通过tree数据
export function typeList(params) {
  return request({
    url: '/nkhg/nbkz/csmb/type_list',
    method: 'get',
    params: transData(params),
  })
}

//测试模板-测试类型删除
export function typeRemove(params) {
  return request({
    url: '/nkhg/nbkz/csmb/type_remove',
    method: 'get',
    params: transData(params),
  })
}

//测试模板-业务信息-列表分页数据
export function elementList(params) {
  return request({
    url: '/nkhg/nbkz/csmb/elementList',
    method: 'get',
    params: transData(params),
  })
}

//测试模板-业务信息-新增保存信息
export function elementssave(params) {
  return request({
    url: '/nkhg/nbkz/csmb/elementssave',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: transData(params),
  })
}

//测试模板-业务信息-修改保存信息
export function elementmodify(params) {
  return request({
    url: '/nkhg/nbkz/csmb/elementmodify',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: transData(params),
  })
}

//测试模板-业务信息-删除
export function elementremove(params) {
  return request({
    url: '/nkhg/nbkz/csmb/elementremove',
    method: 'get',
    params: transData(params),
  })
}

//测试模板-新建-分类编号生成
export function findAutoNextNumberForLevel(params) {
  return request({
    url: '/nkhg/nbkz/code/findAutoNextNumberForLevel',
    method: 'get',
    params: transData(params),
  })
}

//测试模板-业务内容-业务编号生成
export function findRootNumberByParentIdLevel(params) {
  return request({
    url: '/nkhg/nbkz/code/findRootNumberByParentIdLevel',
    method: 'get',
    params: transData(params),
  })
}
//测试模板-复制
export function copyIssued(params) {
  return request({
    url: `/nkhg/nbkz/csmb/copyIssued?tempIds=${params.tempId}`,
    method: 'post',
    data: transData(params),
  })
}
