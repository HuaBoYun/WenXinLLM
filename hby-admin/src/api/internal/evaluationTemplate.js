import request from '@/utils/request'
import { transData } from '@/utils/requestData'

//评价模板-新增-添加树节点
export function catSave(data, params) {
  return request({
    url: '/nkhg/nbkz/gzdg/cat_save',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: transData(data),
    params,
  })
}

//评价模板-新增-模板第二步 头信息
export function defHead(params) {
  return request({
    url: '/nkhg/nbkz/gzdg/def_head',
    method: 'get',
    params: transData(params),
  })
}

//评价模板-新增-模板第二步左侧树信息
export function defLeftTree(params) {
  return request({
    url: '/nkhg/nbkz/gzdg/def_left_tree',
    method: 'get',
    params: transData(params),
  })
}

//评价模板-修改或新建模板第一步
export function defTmplAdd(params) {
  return request({
    url: '/nkhg/nbkz/gzdg/def_tmpl_add',
    method: 'get',
    params: transData(params),
  })
}

//评价模板-删除
export function defTmplDel(params) {
  return request({
    url: '/nkhg/nbkz/gzdg/def_tmpl_del',
    method: 'delete',
    params: transData(params),
  })
}

//评价模板-新增-新建评价模板下一步
export function defTmplIndex(params) {
  return request({
    url: '/nkhg/nbkz/gzdg/def_tmpl_index',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: JSON.stringify(transData(params)),
  })
}

//评价模板-列表
export function defTmplList(params) {
  return request({
    url: '/nkhg/nbkz/gzdg/def_tmpl_list',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    params: transData(params),
  })
}

//评价模板-新增-根据模板id查看cat
export function detTempList(params) {
  return request({
    url: '/nkhg/nbkz/gzdg/det_temp_list',
    method: 'get',
    params: transData(params),
  })
}

//评价模板-新增-内控评价树
export function getTree(params) {
  return request({
    url: '/nkhg/nbkz/gzdg/getTree',
    method: 'get',
    params: transData(params),
  })
}

//评价模板-新增-要素导入列表
export function importYaosu(data, params) {
  return request({
    url: '/nkhg/nbkz/gzdg/importYaosu?',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: transData(data),
    params,
  })
}

//评价模板-修改
export function upadteTmpl(params) {
  return request({
    url: '/nkhg/nbkz/gzdg/upadteTmpl',
    method: 'get',
    params: transData(params),
  })
}

//评价模板-新增-模板要素删除
export function yaosudelete(params) {
  return request({
    url: '/nkhg/nbkz/gzdg/yaosudelete',
    method: 'delete',
    params: transData(params),
  })
}

//评价模板-新增-模板要素标准分保存
export function yaosusave(params) {
  return request({
    url: '/nkhg/nbkz/gzdg/yaosusave',
    method: 'get',
    params: transData(params),
  })
}

//评价模板-新增-添加要素
export function yaosuAdd(params) {
  return request({
    url: '/nkhg/nbkz/gzdg/importYaosuSave',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    params,
  })
}

//评价模板-复制
export function copy(data, params) {
  return request({
    url: `/nkhg/nbkz/gzdg/def_tmpl_copy?beforeTempId=${params}`,
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: transData(data),
  })
}

//
export function deleteYaosu(params) {
  return request({
    url: `/nkhg/nbkz/gzdg/yaosudeletes?elementGategoryIds=${params}`,
    method: 'delete',
  })
}
