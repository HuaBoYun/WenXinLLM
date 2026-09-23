import request from '@/utils/request'
import { transData } from '@/utils/requestData'

// 后端使用@RequestBody，需要JSON格式
const JSON_HEADERS = { 'Content-Type': 'application/json;charset=UTF-8' }

// ========== 负责人信息 ==========
export function getLeaderInfoList(data) {
  return request({ url: '/monitor/v1/leader/info/list', method: 'post', data: transData(data), headers: JSON_HEADERS })
}
export function getLeaderInfoDetail(id) {
  return request({ url: '/monitor/v1/leader/info/' + id, method: 'get' })
}
export function addLeaderInfo(data) {
  return request({ url: '/monitor/v1/leader/info/add', method: 'post', data: transData(data), headers: JSON_HEADERS })
}
export function updateLeaderInfo(data) {
  return request({ url: '/monitor/v1/leader/info/update', method: 'post', data: transData(data), headers: JSON_HEADERS })
}
export function deleteLeaderInfo(id) {
  return request({ url: '/monitor/v1/leader/info/' + id, method: 'delete' })
}
export function getLeaderInfoStatistics() {
  return request({ url: '/monitor/v1/leader/info/statistics', method: 'get' })
}
export function exportLeaderInfo() {
  return request({ url: '/monitor/v1/leader/info/export', method: 'get', responseType: 'blob' })
}

// ========== 负责人考核评价 ==========
export function getLeaderEvaluationList(data) {
  return request({ url: '/monitor/v1/leader/evaluation/list', method: 'post', data: transData(data), headers: JSON_HEADERS })
}
export function getLeaderEvaluationDetail(id) {
  return request({ url: '/monitor/v1/leader/evaluation/' + id, method: 'get' })
}
export function addLeaderEvaluation(data) {
  return request({ url: '/monitor/v1/leader/evaluation/add', method: 'post', data: transData(data), headers: JSON_HEADERS })
}
export function updateLeaderEvaluation(data) {
  return request({ url: '/monitor/v1/leader/evaluation/update', method: 'post', data: transData(data), headers: JSON_HEADERS })
}
export function deleteLeaderEvaluation(id) {
  return request({ url: '/monitor/v1/leader/evaluation/' + id, method: 'delete' })
}
export function getLeaderEvaluationStatistics() {
  return request({ url: '/monitor/v1/leader/evaluation/statistics', method: 'get' })
}
export function exportLeaderEvaluation() {
  return request({ url: '/monitor/v1/leader/evaluation/export', method: 'get', responseType: 'blob' })
}
// 单条考核记录导出（返回JSON数据，前端生成Excel）
export function exportLeaderEvaluationById(id) {
  return request({ url: '/monitor/v1/leader/evaluation/exportById?id=' + id, method: 'get' })
}
// 按Tab类型查询考核列表
export function getLeaderEvaluationTabList(data) {
  return request({ url: '/monitor/v1/leader/evaluation/tab-list', method: 'post', data: transData(data), headers: JSON_HEADERS })
}
// 考核结果分析
export function getLeaderEvaluationAnalysis() {
  return request({ url: '/monitor/v1/leader/evaluation/analysis', method: 'get' })
}
// ========== 考核指标管理 ==========
export function getEvaluationIndicatorList(data) {
  return request({ url: '/monitor/v1/leader/evaluation/indicator/list', method: 'post', data: transData(data), headers: JSON_HEADERS })
}
export function addEvaluationIndicator(data) {
  return request({ url: '/monitor/v1/leader/evaluation/indicator/add', method: 'post', data: transData(data), headers: JSON_HEADERS })
}
export function updateEvaluationIndicator(data) {
  return request({ url: '/monitor/v1/leader/evaluation/indicator/update', method: 'post', data: transData(data), headers: JSON_HEADERS })
}
export function deleteEvaluationIndicator(id) {
  return request({ url: '/monitor/v1/leader/evaluation/indicator/' + id, method: 'delete' })
}

// ========== 负责人发展 ==========
export function getLeaderDevelopmentList(data) {
  return request({ url: '/monitor/v1/leader/development/list', method: 'post', data: transData(data), headers: JSON_HEADERS })
}
export function getLeaderDevelopmentTabList(data) {
  return request({ url: '/monitor/v1/leader/development/tab-list', method: 'post', data: transData(data), headers: JSON_HEADERS })
}
export function getLeaderDevelopmentDetail(id) {
  return request({ url: '/monitor/v1/leader/development/' + id, method: 'get' })
}
export function addLeaderDevelopment(data) {
  return request({ url: '/monitor/v1/leader/development/add', method: 'post', data: transData(data), headers: JSON_HEADERS })
}
export function updateLeaderDevelopment(data) {
  return request({ url: '/monitor/v1/leader/development/update', method: 'post', data: transData(data), headers: JSON_HEADERS })
}
export function updateLeaderDevelopmentProgress(data) {
  return request({ url: '/monitor/v1/leader/development/update-progress', method: 'post', data: transData(data), headers: JSON_HEADERS })
}
export function deleteLeaderDevelopment(id) {
  return request({ url: '/monitor/v1/leader/development/' + id, method: 'delete' })
}
export function getLeaderDevelopmentStatistics() {
  return request({ url: '/monitor/v1/leader/development/statistics', method: 'get' })
}
export function getLeaderDevelopmentTypeChart() {
  return request({ url: '/monitor/v1/leader/development/chart/type-distribution', method: 'get' })
}
export function getLeaderDevelopmentTrendChart() {
  return request({ url: '/monitor/v1/leader/development/chart/progress-trend', method: 'get' })
}
export function exportLeaderDevelopment() {
  return request({ url: '/monitor/v1/leader/development/export', method: 'get', responseType: 'blob' })
}

// ========== 负责人监管 ==========
export function getLeaderSupervisionList(data) {
  return request({ url: '/monitor/v1/leader/supervision/list', method: 'post', data: transData(data), headers: JSON_HEADERS })
}
export function getLeaderSupervisionDetail(id) {
  return request({ url: '/monitor/v1/leader/supervision/' + id, method: 'get' })
}
export function addLeaderSupervision(data) {
  return request({ url: '/monitor/v1/leader/supervision/add', method: 'post', data: transData(data), headers: JSON_HEADERS })
}
export function updateLeaderSupervision(data) {
  return request({ url: '/monitor/v1/leader/supervision/update', method: 'post', data: transData(data), headers: JSON_HEADERS })
}
export function deleteLeaderSupervision(id) {
  return request({ url: '/monitor/v1/leader/supervision/' + id, method: 'delete' })
}
export function getLeaderSupervisionStatistics() {
  return request({ url: '/monitor/v1/leader/supervision/statistics', method: 'get' })
}
export function exportLeaderSupervision() {
  return request({ url: '/monitor/v1/leader/supervision/export', method: 'get', responseType: 'blob' })
}

// ========== 负责人履历 ==========
export function getLeaderResumeList(data) {
  return request({ url: '/monitor/v1/leader/resume/list', method: 'post', data: transData(data), headers: JSON_HEADERS })
}
export function getLeaderResumeDetail(id) {
  return request({ url: '/monitor/v1/leader/resume/' + id, method: 'get' })
}
export function addLeaderResume(data) {
  return request({ url: '/monitor/v1/leader/resume/add', method: 'post', data: transData(data), headers: JSON_HEADERS })
}
export function updateLeaderResume(data) {
  return request({ url: '/monitor/v1/leader/resume/update', method: 'post', data: transData(data), headers: JSON_HEADERS })
}
export function deleteLeaderResume(id) {
  return request({ url: '/monitor/v1/leader/resume/' + id, method: 'delete' })
}

// ========== 负责人任职情况 ==========
export function getLeaderPositionList(data) {
  return request({ url: '/monitor/v1/leader/position/list', method: 'post', data: transData(data), headers: JSON_HEADERS })
}
export function getLeaderPositionDetail(id) {
  return request({ url: '/monitor/v1/leader/position/' + id, method: 'get' })
}
export function addLeaderPosition(data) {
  return request({ url: '/monitor/v1/leader/position/add', method: 'post', data: transData(data), headers: JSON_HEADERS })
}
export function updateLeaderPosition(data) {
  return request({ url: '/monitor/v1/leader/position/update', method: 'post', data: transData(data), headers: JSON_HEADERS })
}
export function deleteLeaderPosition(id) {
  return request({ url: '/monitor/v1/leader/position/' + id, method: 'delete' })
}

// ========== 负责人资质证书 ==========
export function getLeaderCertificateList(data) {
  return request({ url: '/monitor/v1/leader/certificate/list', method: 'post', data: transData(data), headers: JSON_HEADERS })
}
export function getLeaderCertificateDetail(id) {
  return request({ url: '/monitor/v1/leader/certificate/' + id, method: 'get' })
}
export function addLeaderCertificate(data) {
  return request({ url: '/monitor/v1/leader/certificate/add', method: 'post', data: transData(data), headers: JSON_HEADERS })
}
export function updateLeaderCertificate(data) {
  return request({ url: '/monitor/v1/leader/certificate/update', method: 'post', data: transData(data), headers: JSON_HEADERS })
}
export function deleteLeaderCertificate(id) {
  return request({ url: '/monitor/v1/leader/certificate/' + id, method: 'delete' })
}

// ========== 负责人档案 ==========
export function getLeaderArchiveList(data) {
  return request({ url: '/monitor/v1/leader/archive/list', method: 'post', data: transData(data), headers: JSON_HEADERS })
}
export function getLeaderArchiveDetail(id) {
  return request({ url: '/monitor/v1/leader/archive/' + id, method: 'get' })
}
export function addLeaderArchive(data) {
  return request({ url: '/monitor/v1/leader/archive/add', method: 'post', data: transData(data), headers: JSON_HEADERS })
}
export function updateLeaderArchive(data) {
  return request({ url: '/monitor/v1/leader/archive/update', method: 'post', data: transData(data), headers: JSON_HEADERS })
}
export function deleteLeaderArchive(id) {
  return request({ url: '/monitor/v1/leader/archive/' + id, method: 'delete' })
}

// ========== 负责人变更记录 ==========
export function getLeaderChangeRecordList(data) {
  return request({ url: '/monitor/v1/leader/changeRecord/list', method: 'post', data: transData(data), headers: JSON_HEADERS })
}
export function getLeaderChangeRecordDetail(id) {
  return request({ url: '/monitor/v1/leader/changeRecord/' + id, method: 'get' })
}
export function addLeaderChangeRecord(data) {
  return request({ url: '/monitor/v1/leader/changeRecord/add', method: 'post', data: transData(data), headers: JSON_HEADERS })
}
export function updateLeaderChangeRecord(data) {
  return request({ url: '/monitor/v1/leader/changeRecord/update', method: 'post', data: transData(data), headers: JSON_HEADERS })
}
export function deleteLeaderChangeRecord(id) {
  return request({ url: '/monitor/v1/leader/changeRecord/' + id, method: 'delete' })
}
