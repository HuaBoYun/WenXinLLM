import request from '@/utils/request'
import { transData } from '@/utils/requestData'

//测试任务-主页
export function controlTestImplList(params) {
  return request({
    url: '/hgglext/nbkz/nkcs/impl/control_test_impl_list',
    method: 'get',
    params: transData(params),
  })
}

//测试任务-左侧树
export function getLeftTree(params) {
  return request({
    url: '/hgglext/nbkz/csrw/gettree',
    method: 'get',
    params: transData(params),
  })
}

//测试任务-右侧列表
export function getRightList(params) {
  return request({
    url: '/hgglext/nbkz/csrw/def_list',
    method: 'get',
    params: transData(params),
  })
}

//测试任务-全部提交
export function saveall(params) {
  return request({
    url: '/hgglext/nbkz/csrw/saveall',
    method: 'post',
    params: transData(params),
  })
}

//测试任务-右侧列表-点击编号查看详情
export function elemendatail(params) {
  return request({
    url: '/hgglext/nbkz/csmb/elemendatail',
    method: 'get',
    params: transData(params),
  })
}

//测试任务-右侧列表-点击修改/测试查询
export function addtask(params) {
  return request({
    url: '/hgglext/nbkz/csrw/addtask',
    method: 'get',
    params: transData(params),
  })
}

//测试任务-修改/测试页面-附件删除
export function delFile(params) {
  return request({
    url: '/hgglext/nbkz/csrw/control_test_impl_upload_del',
    method: 'post',
    params: transData(params),
  })
}

//测试任务-修改/测试页面-保存测试
export function controlTestImplSave(params) {
  return request({
    url: '/hgglext/nbkz/csrw/control_test_impl_save',
    method: 'post',
    params: transData(params),
  })
}
