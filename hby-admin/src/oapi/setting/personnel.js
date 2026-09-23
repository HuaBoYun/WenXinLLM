import request from '@/utils/request'
import { transData } from '@/utils/requestData'

//审计人员管理列表分页功能
export function getStaffPageList(params) {
  return request({
    url: '/setting/getyrkStaffPageList',
    method: 'post',
    data: transData(params),
  })
}
//  获取人员信息
export function GetusrDetail(data) {
  return request({
    url: '/audit/auditProject/htdl/usr_detail',
    method: 'get',
    params: transData(data),
  })
}

// 查看详细信息
export function GetStaffInfo(params) {
  return request({
    url: '/setting/getStaffInfo',
    method: 'post',
    data: transData(params),
  })
}

// 保存和修改
export function SaveOrUpdateStaff(params) {
  return request({
    url: '/setting/saveOrUpdateStaff',
    method: 'post',
    data: transData(params),
  })
}

// 培训信息删除
export function RemoveTrain(params) {
  return request({
    url: '/setting/removeTrain',
    method: 'post',
    data: transData(params),
  })
}

// 培训信息删除
export function MergePlanProjectManageInfo(params) {
  return request({
    url: '/setting/mergePlanProjectManageInfo',
    method: 'post',
    data: transData(params),
  })
}
// 人员审批
export function personShenPi(params) {
  return request({
    url: '/audit/nbsjapproval/submitAuditUserApproval',
    method: 'post',
    data: transData(params),
  })
}
// 人员主列表删除
export function personDelete(params) {
  return request({
    url: '/setting/updateStaff',
    method: 'post',
    data: transData(params),
  })
}
// 审计对象库列表
export function getObjectPageList(params) {
  return request({
    url: '/setting/findAllLeader',
    method: 'post',
    data: transData(params),
  })
}
