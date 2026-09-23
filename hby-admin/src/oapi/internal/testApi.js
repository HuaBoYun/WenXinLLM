import request from '@/utils/request'
import { transData } from '@/utils/requestData'

//评价立项-主页查询
export function getProjectList(params) {
  console.log('JSADAS')
  return request({
    url: '/nkhg/nbkz/nkcs/plan/ctrltest_plan_list',
    method: 'get',
    params: transData(params),
  })
}
//评价立项删除
export function deleteProject(params) {
  return request({
    url: '/nkhg/nbkz/pjgl/proj_delete',
    method: 'post',
    data: transData(params),
  })
}
