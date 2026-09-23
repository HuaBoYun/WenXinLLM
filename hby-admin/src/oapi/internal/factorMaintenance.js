import request from '@/utils/request'
import { transData } from '@/utils/requestData'

//要素维护-新增-编号获取
export function findAutoNum(params) {
  return request({
    url: '/nkhg/nbkz/code/findAutoNum',
    method: 'get',
    params: transData(params),
  })
}

//要素维护-列表
export function defQuexianList(params) {
  // console.log(params)
  return request({
    url: `/nkhg/nbkz/gzdg/def_basic_list`,
    method: 'get',
    params: transData(params),
  })
}

//要素维护-保存
export function defBasicSave(params) {
  console.log(params)
  return request({
    url: '/nkhg/nbkz/gzdg/def_basic_save',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: JSON.stringify(transData(params)),
  })
}

//要素维护-删除
export function defBasicDel(params) {
  return request({
    url: '/nkhg/nbkz/gzdg/def_basic_del',
    method: 'delete',
    params: transData(params),
  })
}

//要素维护-查看及修改时获取数据
export function defModifyDetail(params) {
  return request({
    url: '/nkhg/nbkz/gzdg/def_modify',
    method: 'get',
    params: transData(params),
  })
}

//要素维护-导出(有选择项则导出选择的，没有则导出全部)
export function yswhexport(params) {
  return request({
    url: '/nkhg/nbkz/gzdg/yswhexport?eleid=',
    method: 'get',
    params: transData(params),
    responseType: 'blob',
  })
}

//要素维护-点击修改按钮之前调用-判断是否已经被引用
export function defBasicCheck(params) {
  return request({
    url: '/nkhg/nbkz/gzdg/def_basic_check',
    method: 'get',
    params: transData(params),
  })
}
