import { transData } from '@/utils/requestData'

// 获取党建管理统计数据
export function getPartyBuildingStatistics() {
  return transData({
    url: '/api/party-building/statistics',
    method: 'post'
  })
}

// ==================== 党组织管理 ====================

// 获取党组织列表
export function getPartyOrganizationsList(params) {
  return transData({
    url: '/api/party-building/organizations/list',
    method: 'post',
    data: params
  })
}

// 保存党组织
export function savePartyOrganization(data) {
  return transData({
    url: '/api/party-building/organizations/save',
    method: 'post',
    data: data
  })
}

// 删除党组织
export function deletePartyOrganization(id) {
  return transData({
    url: '/api/party-building/organizations/delete',
    method: 'post',
    data: { id }
  })
}

// 获取党组织详情
export function getPartyOrganizationDetail(id) {
  return transData({
    url: '/api/party-building/organizations/detail',
    method: 'post',
    data: { id }
  })
}

// 导出党组织数据
export function exportPartyOrganizations(params) {
  return transData({
    url: '/api/party-building/organizations/export',
    method: 'post',
    data: params
  })
}

// 获取组织架构树
export function getOrganizationTree() {
  return transData({
    url: '/api/party-building/organizations/tree',
    method: 'post'
  })
}

// 批量操作党组织
export function batchOperateOrganizations(operation, ids) {
  return transData({
    url: '/api/party-building/organizations/batch-operate',
    method: 'post',
    data: { operation, ids }
  })
}

// ==================== 党员管理 ====================

// 获取党员列表
export function getPartyMembersList(params) {
  return transData({
    url: '/api/party-building/members/list',
    method: 'post',
    data: params
  })
}

// 保存党员
export function savePartyMember(data) {
  return transData({
    url: '/api/party-building/members/save',
    method: 'post',
    data: data
  })
}

// 删除党员
export function deletePartyMember(id) {
  return transData({
    url: '/api/party-building/members/delete',
    method: 'post',
    data: { id }
  })
}

// 获取党员详情
export function getPartyMemberDetail(id) {
  return transData({
    url: '/api/party-building/members/detail',
    method: 'post',
    data: { id }
  })
}

// 转移党员
export function transferPartyMember(memberId, targetOrganizationId) {
  return transData({
    url: '/api/party-building/members/transfer',
    method: 'post',
    data: { memberId, targetOrganizationId }
  })
}

// 导出党员数据
export function exportPartyMembers(params) {
  return transData({
    url: '/api/party-building/members/export',
    method: 'post',
    data: params
  })
}

// 批量导入党员
export function importPartyMembers(formData) {
  return transData({
    url: '/api/party-building/members/import',
    method: 'post',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

// 党员转正
export function convertMemberToFormal(memberId, conversionData) {
  return transData({
    url: '/api/party-building/members/convert-to-formal',
    method: 'post',
    data: { memberId, conversionData }
  })
}

// 获取党员统计信息
export function getPartyMemberStatistics(params) {
  return transData({
    url: '/api/party-building/members/statistics',
    method: 'post',
    data: params
  })
}

// ==================== 党建活动管理 ====================

// 获取党建活动列表
export function getPartyActivitiesList(params) {
  return transData({
    url: '/api/party-building/activities/list',
    method: 'post',
    data: params
  })
}

// 保存党建活动
export function savePartyActivity(data) {
  return transData({
    url: '/api/party-building/activities/save',
    method: 'post',
    data: data
  })
}

// 删除党建活动
export function deletePartyActivity(id) {
  return transData({
    url: '/api/party-building/activities/delete',
    method: 'post',
    data: { id }
  })
}

// 获取党建活动详情
export function getPartyActivityDetail(id) {
  return transData({
    url: '/api/party-building/activities/detail',
    method: 'post',
    data: { id }
  })
}

// 活动报名
export function registerForActivity(activityId, memberIds) {
  return transData({
    url: '/api/party-building/activities/register',
    method: 'post',
    data: { activityId, memberIds }
  })
}

// 取消活动报名
export function cancelActivityRegistration(activityId, memberId) {
  return transData({
    url: '/api/party-building/activities/cancel-registration',
    method: 'post',
    data: { activityId, memberId }
  })
}

// 获取活动参与者列表
export function getActivityParticipants(activityId) {
  return transData({
    url: '/api/party-building/activities/participants',
    method: 'post',
    data: { activityId }
  })
}

// 活动签到
export function activityCheckIn(activityId, memberId) {
  return transData({
    url: '/api/party-building/activities/check-in',
    method: 'post',
    data: { activityId, memberId }
  })
}

// 完成活动
export function completeActivity(activityId, completionData) {
  return transData({
    url: '/api/party-building/activities/complete',
    method: 'post',
    data: { activityId, completionData }
  })
}

// 导出党建活动数据
export function exportPartyActivities(params) {
  return transData({
    url: '/api/party-building/activities/export',
    method: 'post',
    data: params
  })
}

// 获取活动统计信息
export function getActivityStatistics(params) {
  return transData({
    url: '/api/party-building/activities/statistics',
    method: 'post',
    data: params
  })
}

// ==================== 党建评估管理 ====================

// 获取党建评估列表
export function getPartyEvaluationsList(params) {
  return transData({
    url: '/api/party-building/evaluations/list',
    method: 'post',
    data: params
  })
}

// 保存党建评估
export function savePartyEvaluation(data) {
  return transData({
    url: '/api/party-building/evaluations/save',
    method: 'post',
    data: data
  })
}

// 删除党建评估
export function deletePartyEvaluation(id) {
  return transData({
    url: '/api/party-building/evaluations/delete',
    method: 'post',
    data: { id }
  })
}

// 获取党建评估详情
export function getPartyEvaluationDetail(id) {
  return transData({
    url: '/api/party-building/evaluations/detail',
    method: 'post',
    data: { id }
  })
}

// 提交评估
export function submitEvaluation(evaluationId) {
  return transData({
    url: '/api/party-building/evaluations/submit',
    method: 'post',
    data: { evaluationId }
  })
}

// 审核评估
export function auditEvaluation(evaluationId, auditResult) {
  return transData({
    url: '/api/party-building/evaluations/audit',
    method: 'post',
    data: { evaluationId, auditResult }
  })
}

// 获取评估模板
export function getEvaluationTemplates() {
  return transData({
    url: '/api/party-building/evaluations/templates',
    method: 'post'
  })
}

// 保存评估模板
export function saveEvaluationTemplate(templateData) {
  return transData({
    url: '/api/party-building/evaluations/templates/save',
    method: 'post',
    data: templateData
  })
}

// 导出评估报告
export function exportEvaluationReport(evaluationId) {
  return transData({
    url: '/api/party-building/evaluations/export-report',
    method: 'post',
    data: { evaluationId }
  })
}

// 获取评估统计数据
export function getEvaluationStatistics(params) {
  return transData({
    url: '/api/party-building/evaluations/statistics',
    method: 'post',
    data: params
  })
}

// ==================== 党费管理 ====================

// 获取党费缴纳记录
export function getPartyDuesRecords(params) {
  return transData({
    url: '/api/party-building/dues/records',
    method: 'post',
    data: params
  })
}

// 缴纳党费
export function payPartyDues(paymentData) {
  return transData({
    url: '/api/party-building/dues/pay',
    method: 'post',
    data: paymentData
  })
}

// 获取党费统计
export function getPartyDuesStatistics(params) {
  return transData({
    url: '/api/party-building/dues/statistics',
    method: 'post',
    data: params
  })
}

// 导出党费记录
export function exportPartyDuesRecords(params) {
  return transData({
    url: '/api/party-building/dues/export',
    method: 'post',
    data: params
  })
}

// ==================== 学习培训管理 ====================

// 获取学习培训列表
export function getTrainingList(params) {
  return transData({
    url: '/api/party-building/training/list',
    method: 'post',
    data: params
  })
}

// 保存学习培训
export function saveTraining(data) {
  return transData({
    url: '/api/party-building/training/save',
    method: 'post',
    data: data
  })
}

// 参加培训
export function joinTraining(trainingId, memberId) {
  return transData({
    url: '/api/party-building/training/join',
    method: 'post',
    data: { trainingId, memberId }
  })
}

// 完成培训
export function completeTraining(trainingId, memberId, completionData) {
  return transData({
    url: '/api/party-building/training/complete',
    method: 'post',
    data: { trainingId, memberId, completionData }
  })
}

// 获取培训统计
export function getTrainingStatistics(params) {
  return transData({
    url: '/api/party-building/training/statistics',
    method: 'post',
    data: params
  })
}

// ==================== 系统配置 ====================

// 获取党建系统配置
export function getPartyBuildingConfig() {
  return transData({
    url: '/api/party-building/config',
    method: 'post'
  })
}

// 保存党建系统配置
export function savePartyBuildingConfig(config) {
  return transData({
    url: '/api/party-building/config/save',
    method: 'post',
    data: config
  })
}

// 获取权限配置
export function getPermissionConfig() {
  return transData({
    url: '/api/party-building/permission-config',
    method: 'post'
  })
}

// 保存权限配置
export function savePermissionConfig(config) {
  return transData({
    url: '/api/party-building/permission-config/save',
    method: 'post',
    data: config
  })
}
