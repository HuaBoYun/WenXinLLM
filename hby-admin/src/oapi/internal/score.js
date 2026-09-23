import request from '@/utils/request'
import { transData } from '@/utils/requestData'

//评价评分-主页查询
export function getScoreList(params) {
  return request({
    url: '/nkhg/nbkz/pjgl/proj_task_gradelist',
    method: 'get',
    params: transData(params),
  })
}

//评价评分-主页查询-发起
export function getProjPingfenList(params) {
  return request({
    url: '/nkhg/nbkz/pjgl/t08_proj_pingfen',
    method: 'get',
    headers: { selectedPlans: params.selectedPlans },
    params: transData(params),
  })
}

//评价评分-左侧评分树形
export function getLeftTreeByPingfen(params) {
  return request({
    url: '/nkhg/nbkz/gzdg/leftTreeByPingfen',
    method: 'get',
    params: transData(params),
  })
}

//评价评分-点击左侧树显示右侧评分要素列表
export function getProjCatList(params) {
  return request({
    url: '/nkhg/nbkz/pjgl/t08_proj_cat_list1',
    method: 'get',
    params: transData(params),
  })
}

//评价评分-要素列表-要素详情
export function getDefBasicModifyt(params) {
  return request({
    url: '/nkhg/nbkz/gzdg/def_basic_modify',
    method: 'get',
    params: transData(params),
  })
}

//评价评分-评价-全部提交
export function sbumitProjCat(params) {
  return request({
    url: '/nkhg/nbkz/pjgl/sbumitProjCat',
    method: 'post',
    params: transData(params),
  })
}

//附件下载接口
export function download(params) {
  return request({
    url: '/nkhg/filec/download',
    method: 'get',
    responseType: 'blob',
    params: transData(params),
  })
}

//评价评分-删除附件
export function deleteFile(params) {
  return request({
    url: '/nkhg/nbkz/pjgl/pjpf_fj_del',
    method: 'post',
    params: transData(params),
  })
}

//评价评分-评价-导出excel
export function pfjgExport(params) {
  return request({
    url: '/nkhg/nbkz/pjgl/pfjg_export',
    method: 'get',
    params: transData(params),
    responseType: 'blob',
  })
}

//评价评分-评价-保存评价
export function projCatList1Save(params) {
  return request({
    url: '/nkhg/nbkz/pjgl/t08_proj_cat_list1_save',
    method: 'post',
    params: transData(params),
  })
}
