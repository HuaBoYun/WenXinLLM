import request from '@/utils/request'
import { transData } from '@/utils/requestData'

//财务管理-收款管理-列表
export function getCollectionManagemen(data) {
  return request({
    url: '/contract/contract/collectionManagemen',
    method: 'post',
    data: transData(data),
  })
}
//财务管理-收款管理-新增 - 合同选择
export function collectionChoiceContract(data) {
  return request({
    url: '/contract/contract/collectionChoiceContract',
    method: 'post',
    data: transData(data),
  })
}
//财务管理-收款管理-删除
export function removeCollection(data) {
  return request({
    url: '/contract/contract/removeCollectionManagemen',
    method: 'post',
    data: transData(data),
  })
}
//财务管理-收款管理-提交审批
export function skglTjsp(data) {
  return request({
    url: '/contract/skgl/tjsp_zcgl',
    method: 'post',
    data: transData(data),
  })
}
//付款管理-新增-合同名称选择接口
export function choiceContract(data) {
  return request({
    url: '/contract/paymentChoiceContract',
    method: 'post',
    data: transData(data),
  })
}
//-收款管理-新增-对应收款项
export function choicePlanNodeConlletion(data) {
  return request({
    url: '/contract/choicePlanNodeConlletion',
    method: 'post',
    data: transData(data),
  })
}
//财务管理-收款管理-办理基本信息
export function tosptzglinfo(data) {
  return request({
    url: '/contract/skgl/to_sptzgl_info',
    method: 'post',
    data: transData(data),
  })
}
//财务管理-收款管理-办理
export function skgltosptzgl(data) {
  return request({
    url: '/contract/skgl/approval_process',
    method: 'post',
    data: transData(data),
  })
}
//财务管理-付款管理-办理基本信息
export function tosptzglinfo1(data) {
  return request({
    url: '/contract/fkgl/to_sptzgl_info',
    method: 'post',
    data: transData(data),
  })
}
//付款管理-办理
export function fkglapproval(data) {
  return request({
    url: '/contract/fkgl/approval_process',
    method: 'post',
    data: transData(data),
  })
}
//收款管理-新增
export function saveCollectionManagemen(data) {
  return request({
    url: '/contract/contract/saveCollectionManagemen',
    method: 'post',
    data: transData(data),
  })
}
//付款管理-新增
export function savePaymentManagemen(data) {
  return request({
    url: '/contract/savePaymentManagemen',
    method: 'post',
    data: transData(data),
  })
}
//付款管理-新增-合同名称选择接口
export function ChoiceContract(data) {
  return request({
    url: '/contract/paymentChoiceContract',
    method: 'post',
    data: transData(data),
  })
}
//付款管理-新增-付款银行账号
export function choiceBankInfo(data) {
  return request({
    url: '/contract/choiceOrgselfBankInfo',
    method: 'post',
    data: transData(data),
  })
}
//付款管理-新增-收款银行账号
export function choiceCounterPartBankInfo(data) {
  return request({
    url: '/contract/choiceCounterPartBankInfo',
    method: 'post',
    data: transData(data),
  })
}
//付款管理-新增-付款计划
export function choicePlanNodePayment(data) {
  return request({
    url: '/contract/choicePlanNodePayment',
    method: 'post',
    data: transData(data),
  })
}
//付款管理-新增-发票号
export function collectionChoiceInvoice(data) {
  return request({
    url: '/contract/collectionChoiceInvoice',
    method: 'post',
    data: transData(data),
  })
}
//财务管理-付款管理-列表
export function getPaymentManagemen(data) {
  return request({
    url: '/contract/contract/paymentManagemen',
    method: 'post',
    data: transData(data),
  })
}
//财务管理-付款管理-删除
export function removePayment(data) {
  return request({
    url: '/contract/contract/removePaymentManagemen',
    method: 'post',
    data: transData(data),
  })
}
//财务管理-付款管理-提交审批
export function fkglTjsp(data) {
  return request({
    url: '/contract/fkgl/tjsp_zcgl',
    method: 'post',
    data: transData(data),
  })
}
//付款管理-新增-经办人选择接口-右侧列表接口
export function pjlxList(data) {
  return request({
    url: '/contract/htdl/list',
    method: 'post',
    data: transData(data),
  })
}
//付款管理-新增-经办人选择接口-左侧列表
export function pjlxLeftTree(data) {
  return request({
    url: '/setting/baseInfo/findOrganizationByTreeAllbm',
    method: 'post',
    data: transData(data),
  })
}
//财务管理-发票管理-列表
export function getInvoicesManageMen(data) {
  return request({
    url: '/contract/contract/invoicesManageMen',
    method: 'post',
    data: transData(data),
  })
}
//财务管理-发票管理-删除
export function removeInvoice(data) {
  return request({
    url: '/contract/contract/removeInvoiceManageMen',
    method: 'post',
    data: transData(data),
  })
}
//财务管理-发票管理-操作（状态）
export function InvoiceStatus(data) {
  return request({
    url: '/contract/contract/modifyInvoiceStatus',
    method: 'post',
    data: transData(data),
  })
}
//财务管理-发票管理-新增-票据向对方选择接口
export function invoiceCounterpartInfoList(data) {
  return request({
    url: '/contract/contract/invoiceCounterpartInfoList',
    method: 'post',
    data: transData(data),
  })
}
//财务管理-发票管理-新增
export function saveInvoiceManageMen(data) {
  return request({
    url: '/contract/contract/saveInvoiceManageMen',
    method: 'post',
    data: transData(data),
  })
}
//财务管理-银行账户-列表
export function getBankAccountList(data) {
  return request({
    url: '/contract/contract/bankAccountList',
    method: 'post',
    data: transData(data),
  })
}
//财务管理-银行账户-删除
export function removeOrgBank(data) {
  return request({
    url: '/contract/contract/removeOrgBankInfo',
    method: 'post',
    data: transData(data),
  })
}
//财务管理-银行账户-新增
export function orgBankInfoSave(data) {
  return request({
    url: '/contract/contract/orgBankInfoSave',
    method: 'post',
    data: transData(data),
  })
}
//收款管理-查看详情
export function viewCollectionManagemen(data) {
  return request({
    url: '/contract/contract/viewCollectionManagemen',
    method: 'post',
    data: transData(data),
  })
}
//付款管理-查看详情
export function viewPaymentManagemen(data) {
  return request({
    url: '/contract/contract/viewPaymentManagemen',
    method: 'post',
    data: transData(data),
  })
}
//发票管理-查看详情
export function viewInvoice(data) {
  return request({
    url: '/contract/contract/viewInvoice',
    method: 'post',
    data: transData(data),
  })
}
