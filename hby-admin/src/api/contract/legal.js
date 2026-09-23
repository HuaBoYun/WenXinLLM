import request from '@/utils/request'
import { transData } from '@/utils/requestData'

//法务管理-纠纷登记--列表
export function getcaseInformationList(data) {
  return request({
    url: '/contract/legal/caseInformationList',
    method: 'post',
    data: transData(data),
  })
}
//法务管理-纠纷登记-删除
export function Removecase(data) {
  return request({
    url: '/contract/legal/caseInformationRemove',
    method: 'post',
    data: transData(data),
  })
}
//法务管理-纠纷登记-新增-合同名称
export function findContractInfo(data) {
  return request({
    url: '/contract/legal/findContractInfo',
    method: 'post',
    data: transData(data),
  })
}
//法务管理-纠纷登记-新增
export function caseInformationSave(data) {
  return request({
    url: '/contract/legal/caseInformationSave',
    method: 'post',
    data: transData(data),
  })
}
//法务管理-附件列表
export function findAttacheMent(data) {
  return request({
    url: '/contract/contract/findAttacheMentListByBid',
    method: 'post',
    data: transData(data),
  })
}
//法务管理-附件列表删除
export function deleAttacheMent(data) {
  return request({
    url: '/contract/contract/deleAttacheMentByBid',
    method: 'post',
    data: transData(data),
  })
}
//法务管理-纠纷登记-修改
export function caseInformationModify(data) {
  return request({
    url: '/contract/legal/caseInformationModify',
    method: 'post',
    data: transData(data),
  })
}
//法务管理-协商过程-列表
export function getnegotiatedSettlementInfoList(data) {
  return request({
    url: '/contract/legal/negotiatedSettlementInfoList',
    method: 'post',
    data: transData(data),
  })
}
//法务管理-协商过程-新增
export function negotiatedSettlemenSave(data) {
  return request({
    url: '/contract/legal/negotiatedSettlemenSave',
    method: 'post',
    data: transData(data),
  })
}
//法务管理-协商过程-修改
export function negotiatedSettlemenModify(data) {
  return request({
    url: '/contract/legal/negotiatedSettlemenModify',
    method: 'post',
    data: transData(data),
  })
}
//法务管理-协商过程-删除
export function Removenegotiated(data) {
  return request({
    url: '/contract/legal/legalNegotiatedSettlemenRemove',
    method: 'post',
    data: transData(data),
  })
}
//法务管理-协商过程-新建-协商过程信息-新建按钮
export function LegalnegotiatedRecordSave(data) {
  return request({
    url: '/contract/legal/LegalnegotiatedRecordSave',
    method: 'post',
    data: transData(data),
  })
}
//协商过程-新建-协商阶段-列表
export function negotiateRecord(data) {
  return request({
    url: '/contract/legal/negotiateRecord',
    method: 'post',
    data: transData(data),
  })
}
//法务管理-协商过程-新建-协商过程信息-删除
export function removeNegotiatedRecord(data) {
  return request({
    url: '/contract/legal/removeNegotiatedRecord',
    method: 'post',
    data: transData(data),
  })
}
//法务管理-协商过程-新建-协商过程信息-修改保存按钮
export function negotiatedRecordModify(data) {
  return request({
    url: '/contract/legal/negotiatedRecordModify',
    method: 'post',
    data: transData(data),
  })
}
//法务管理-诉讼过程-列表
export function getlitigationSettlement(data) {
  return request({
    url: '/contract/legal/litigationSettlement',
    method: 'post',
    data: transData(data),
  })
}
//法务管理-诉讼过程-删除
export function Removelitigation(data) {
  return request({
    url: '/contract/legal/litigationSettlementRemove',
    method: 'post',
    data: transData(data),
  })
}
//法务管理-诉讼过程-修改接口
export function litigationSettlementModify(data) {
  return request({
    url: '/contract/legal/litigationSettlementModify',
    method: 'post',
    data: transData(data),
  })
}
//法务管理-诉讼过程-新建接口
export function litigationSettlementSave(data) {
  return request({
    url: '/contract/legal/litigationSettlementSave',
    method: 'post',
    data: transData(data),
  })
}
//法务管理-诉讼过程-新建-诉讼过程记录-新建按钮
export function proceedingsRecordSave(data) {
  return request({
    url: '/contract/legal/proceedingsRecordSave',
    method: 'post',
    data: transData(data),
  })
}
//法务管理-诉讼过程-新建-诉讼过程记录-列表
export function proceedingsRecord(data) {
  return request({
    url: '/contract/legal/proceedingsRecord',
    method: 'post',
    data: transData(data),
  })
}
//法务管理-诉讼过程-新建-诉讼过程记录-删除
export function removeLegalProceedingsRecord(data) {
  return request({
    url: '/contract/legal/removeLegalProceedingsRecord',
    method: 'post',
    data: transData(data),
  })
}
//法务管理-诉讼过程-新建-诉讼过程记录-修改
export function proceedingsRecordModify(data) {
  return request({
    url: '/contract/legal/proceedingsRecordModify',
    method: 'post',
    data: transData(data),
  })
}
//法务管理-仲裁过程-列表
export function getArbitratSettlementInfoList(data) {
  return request({
    url: '/contract/legal/ArbitratSettlementInfoList',
    method: 'post',
    data: transData(data),
  })
}
//法务管理-仲裁过程-删除
export function RemoveArbitrat(data) {
  return request({
    url: '/contract/legal/ArbitratSettlementRemove',
    method: 'post',
    data: transData(data),
  })
}
//法务管理-仲裁过程-修改
export function ArbitratSettlementModify(data) {
  return request({
    url: '/contract/legal/ArbitratSettlementModify',
    method: 'post',
    data: transData(data),
  })
}
//法务管理-仲裁过程-新建-仲裁基本信息-协商信息选择接口
export function findNegotiatedSettlemenA(data) {
  return request({
    url: '/contract/legal/findNegotiatedSettlemenA',
    method: 'post',
    data: transData(data),
  })
}
//法务管理-仲裁过程-新建按钮/保存按钮
export function ArbitratSettlementSave(data) {
  return request({
    url: '/contract/legal/ArbitratSettlementSave',
    method: 'post',
    data: transData(data),
  })
}
//法务管理-仲裁过程-新建-仲裁过程信息-新增
export function ArbitrationRecordSave(data) {
  return request({
    url: '/contract/legal/ArbitrationRecordSave',
    method: 'post',
    data: transData(data),
  })
}
//法务管理-仲裁过程-新建-仲裁过程信息-修改
export function ArbitrationRecordToModify(data) {
  return request({
    url: '/contract/legal/ArbitrationRecordModify',
    method: 'post',
    data: transData(data),
  })
}
//法务管理-仲裁过程-新建-仲裁过程信息-删除
export function removeArbitrationRecord(data) {
  return request({
    url: '/contract/legal/removeArbitrationRecord',
    method: 'post',
    data: transData(data),
  })
}
//法务管理-仲裁过程-新增-仲裁过程信息-列表
export function ArbitrationRecord(data) {
  return request({
    url: '/contract/legal/ArbitrationRecord',
    method: 'post',
    data: transData(data),
  })
}
//法务管理-纠纷结案-列表
export function getdispute(data) {
  return request({
    url: '/contract/legal/disputeSettlementList',
    method: 'post',
    data: transData(data),
  })
}
//法务管理-纠纷结案-新增
export function disputeSettlementSave(data) {
  return request({
    url: '/contract/legal/disputeSettlementSave',
    method: 'post',
    data: transData(data),
  })
}
//法务管理-纠纷结案-修改
export function disputeSettlementModify(data) {
  return request({
    url: '/contract/legal/disputeSettlementModify',
    method: 'post',
    data: transData(data),
  })
}
//法务管理-纠纷结案-删除
export function Removedispute(data) {
  return request({
    url: '/contract/legal/disputeSettlementRemove',
    method: 'post',
    data: transData(data),
  })
}
//法务管理-资质保全-列表
export function getqualification(data) {
  return request({
    url: '/contract/legal/qualificationList',
    method: 'post',
    data: transData(data),
  })
}
//法务管理-资质保全-删除
export function Removequalification(data) {
  return request({
    url: '/contract/legal/qualificationRemove',
    method: 'post',
    data: transData(data),
  })
}
//法务管理-资质保全-添加-隶属纠纷-选择
export function getFindcaseInformationInfo(data) {
  return request({
    url: '/contract/legal/findcaseInformationInfo',
    method: 'post',
    data: transData(data),
  })
}
//法务管理-资质保全-添加保存
export function getQualificationSave(data) {
  return request({
    url: '/contract/legal/qualificationSave',
    method: 'post',
    data: transData(data),
  })
}
//法务管理-资质保全-修改
export function getQualificationModify(data) {
  return request({
    url: '/contract/legal/qualificationModify',
    method: 'post',
    data: transData(data),
  })
}
//法务管理-账户冻结-列表
export function getfrozen(data) {
  return request({
    url: '/contract/legal/frozenInformationList',
    method: 'post',
    data: transData(data),
  })
}
//法务管理-账户冻结-删除
export function Removefrozen(data) {
  return request({
    url: '/contract/legal/frozenAccountRemove',
    method: 'post',
    data: transData(data),
  })
}
//法务管理-法务台账-列表
export function getlegal(data) {
  return request({
    url: '/contract/legal/legalAccountList',
    method: 'post',
    data: transData(data),
  })
}
//法务管理-账户冻结-新增回显
export function frozenAccountAdd(data) {
  return request({
    url: '/contract/legal/frozenAccountAdd',
    method: 'post',
    data: transData(data),
  })
}
//法务管理-账户冻结-新增
export function frozenAccountSave(data) {
  return request({
    url: '/contract/legal/frozenAccountSave',
    method: 'post',
    data: transData(data),
  })
}
//法务管理-账户冻结-新增-诉讼阶段选择接口
export function RecordInfo(data) {
  return request({
    url: '/contract/legal/findProceedingsRecordInfo',
    method: 'post',
    data: transData(data),
  })
}
//法务管理-账户冻结-新增保存、修改保存回显
export function frozenAccountToModify(data) {
  return request({
    url: '/contract/legal/frozenAccountToModify',
    method: 'post',
    data: transData(data),
  })
}
//法务管理-账户冻结-修改
export function frozenAccountModify(data) {
  return request({
    url: '/contract/legal/frozenAccountModify',
    method: 'post',
    data: transData(data),
  })
}
//纠纷登记-查看详情
export function disputeRegisterDetail(data) {
  return request({
    url: '/contract/legal/disputeRegisterDetail',
    method: 'post',
    data: transData(data),
  })
}
//法务台账-查看详情
export function legalAccountDetail(data) {
  return request({
    url: '/contract/legal/legalAccountDetail',
    method: 'post',
    data: transData(data),
  })
}
