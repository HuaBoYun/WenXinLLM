 import request from '@/utils/request'
 import { transData } from '@/utils/requestData'
 
 const JSON_HEADERS = { 'Content-Type': 'application/json;charset=UTF-8' }
 
 /**
  * 实际控制人识别API
  */
 
 // 分页查询实际控制人列表
 export function getBeneficialOwnerList(data) {
   return request({
     url: '/monitor/v1/supervision/equity/beneficial-owner/getList',
     method: 'post',
     data: transData(data),
     headers: JSON_HEADERS,
   })
 }
 
 // 根据ID查询实际控制人详情
 export function getBeneficialOwnerById(id) {
   return request({
     url: `/monitor/v1/supervision/equity/beneficial-owner/getById/${id}`,
     method: 'get',
     data: transData(),
   })
 }
 
 // 新增实际控制人
 export function addBeneficialOwner(data) {
   return request({
     url: '/monitor/v1/supervision/equity/beneficial-owner/add',
     method: 'post',
     data: transData(data),
     headers: JSON_HEADERS,
   })
 }
 
 // 更新实际控制人
 export function updateBeneficialOwner(data) {
   return request({
     url: '/monitor/v1/supervision/equity/beneficial-owner/update',
     method: 'post',
     data: transData(data),
     headers: JSON_HEADERS,
   })
 }
 
 // 删除实际控制人
 export function deleteBeneficialOwner(data) {
   return request({
     url: '/monitor/v1/supervision/equity/beneficial-owner/delete',
     method: 'post',
     data: transData(data),
     headers: JSON_HEADERS,
   })
 }
 
 // 识别实际控制人
 export function identifyBeneficialOwner(data) {
   return request({
     url: '/monitor/v1/supervision/equity/beneficial-owner/identify',
     method: 'post',
     data: transData(data),
     headers: JSON_HEADERS,
   })
 }
 
 // 分析控制权归属
 export function analyzeControlOwnership(data) {
   return request({
     url: '/monitor/v1/supervision/equity/beneficial-owner/ownership',
     method: 'post',
     data: transData(data),
     headers: JSON_HEADERS,
   })
 }
 
 // 计算控制权比例
 export function calculateControlRatio(data) {
   return request({
     url: '/monitor/v1/supervision/equity/beneficial-owner/ratio',
     method: 'post',
     data: transData(data),
     headers: JSON_HEADERS,
   })
 }
 
 // 追溯控制权来源
 export function traceControlSource(data) {
   return request({
     url: '/monitor/v1/supervision/equity/beneficial-owner/trace',
     method: 'post',
     data: transData(data),
     headers: JSON_HEADERS,
   })
 }
 
 // 验证控制权有效性
 export function validateControlValidity(data) {
   return request({
     url: '/monitor/v1/supervision/equity/beneficial-owner/validate',
     method: 'post',
     data: transData(data),
     headers: JSON_HEADERS,
   })
 }
 
 // 分析控制权稳定性
 export function analyzeControlStability(data) {
   return request({
     url: '/monitor/v1/supervision/equity/beneficial-owner/stability',
     method: 'post',
     data: transData(data),
     headers: JSON_HEADERS,
   })
 }
 
 // 检测控制权变更
 export function detectControlChanges(data) {
   return request({
     url: '/monitor/v1/supervision/equity/beneficial-owner/changes',
     method: 'post',
     data: transData(data),
     headers: JSON_HEADERS,
   })
 }
 
 // 评估控制权风险
 export function assessControlRisk(data) {
   return request({
     url: '/monitor/v1/supervision/equity/beneficial-owner/risk',
     method: 'post',
     data: transData(data),
     headers: JSON_HEADERS,
   })
 }
 
 // 获取控制权图谱
 export function getControlMap(data) {
   return request({
     url: '/monitor/v1/supervision/equity/beneficial-owner/map',
     method: 'post',
     data: transData(data),
     headers: JSON_HEADERS,
   })
 }
 
 // 分析控制权集中度
 export function analyzeControlConcentration(data) {
   return request({
     url: '/monitor/v1/supervision/equity/beneficial-owner/concentration',
     method: 'post',
     data: transData(data),
     headers: JSON_HEADERS,
   })
 }
 
 // 识别隐藏控制人
 export function identifyHiddenControllers(data) {
   return request({
     url: '/monitor/v1/supervision/equity/beneficial-owner/hidden',
     method: 'post',
     data: transData(data),
     headers: JSON_HEADERS,
   })
 }
 
 // 分析代理控制
 export function analyzeProxyControl(data) {
   return request({
     url: '/monitor/v1/supervision/equity/beneficial-owner/proxy',
     method: 'post',
     data: transData(data),
     headers: JSON_HEADERS,
   })
 }
 
 // 检测一致行动人
 export function detectConcertedParties(data) {
   return request({
     url: '/monitor/v1/supervision/equity/beneficial-owner/concerted',
     method: 'post',
     data: transData(data),
     headers: JSON_HEADERS,
   })
 }
 
 // 分析控制权争夺
 export function analyzeControlContest(data) {
   return request({
     url: '/monitor/v1/supervision/equity/beneficial-owner/contest',
     method: 'post',
     data: transData(data),
     headers: JSON_HEADERS,
   })
 }
 
 // 模拟控制权变更
 export function simulateControlChange(data) {
   return request({
     url: '/monitor/v1/supervision/equity/beneficial-owner/simulate',
     method: 'post',
     data: transData(data),
     headers: JSON_HEADERS,
   })
 }
 
 // 获取控制权历史
 export function getControlHistory(data) {
   return request({
     url: '/monitor/v1/supervision/equity/beneficial-owner/history',
     method: 'post',
     data: transData(data),
     headers: JSON_HEADERS,
   })
 }
 
 // 验证实际控制人身份
 export function verifyBeneficialOwnerIdentity(data) {
   return request({
     url: '/monitor/v1/supervision/equity/beneficial-owner/verify',
     method: 'post',
     data: transData(data),
     headers: JSON_HEADERS,
   })
 }
 
 // 分析控制权传导
 export function analyzeControlTransmission(data) {
   return request({
     url: '/monitor/v1/supervision/equity/beneficial-owner/transmission',
     method: 'post',
     data: transData(data),
     headers: JSON_HEADERS,
   })
 }
 
 // 获取实际控制人统计
 export function getBeneficialOwnerStatistics(data) {
   return request({
     url: '/monitor/v1/supervision/equity/beneficial-owner/statistics',
     method: 'post',
     data: transData(data),
     headers: JSON_HEADERS,
   })
 }
 
 // 生成实际控制人报告
 export function generateBeneficialOwnerReport(data) {
   return request({
     url: '/monitor/v1/supervision/equity/beneficial-owner/report',
     method: 'post',
     data: transData(data),
     headers: JSON_HEADERS,
   })
 }
 
 // 检查合规性
 export function checkBeneficialOwnerCompliance(data) {
   return request({
     url: '/monitor/v1/supervision/equity/beneficial-owner/compliance',
     method: 'post',
     data: transData(data),
     headers: JSON_HEADERS,
   })
 }
 
 // 批量更新实际控制人
 export function batchUpdateBeneficialOwner(data) {
   return request({
     url: '/monitor/v1/supervision/equity/beneficial-owner/batch/update',
     method: 'post',
     data: transData(data),
     headers: JSON_HEADERS,
   })
 }
 
 // 批量删除实际控制人
 export function batchDeleteBeneficialOwner(data) {
   return request({
     url: '/monitor/v1/supervision/equity/beneficial-owner/batch/delete',
     method: 'post',
     data: transData(data),
     headers: JSON_HEADERS,
   })
 }
 
 // 导出实际控制人数据
 export function exportBeneficialOwnerData(data) {
   return request({
     url: '/monitor/v1/supervision/equity/beneficial-owner/export',
     method: 'post',
     data: transData(data),
     headers: JSON_HEADERS,
     responseType: 'blob',
   })
 }
 
 // 导入实际控制人数据
 export function importBeneficialOwnerData(data) {
   return request({
     url: '/monitor/v1/supervision/equity/beneficial-owner/import',
     method: 'post',
     data: transData(data),
     headers: { 'Content-Type': 'multipart/form-data' },
   })
 }
 
 // 获取控制权网络数据
 export function getControlNetworkData(data) {
   return request({
     url: '/monitor/v1/supervision/equity/beneficial-owner/network',
     method: 'post',
     data: transData(data),
     headers: JSON_HEADERS,
   })
 }

// 导出实际控制人（统一导出接口）
export function exportBeneficialOwner() {
  return request({ url: '/monitor/v1/supervision/export/equity/beneficialOwner', method: 'get', responseType: 'blob' })
}