/**
 * 敏捷审计智慧首页 API
 * 复用已有后端统计接口，前端聚合数据
 * @author AI
 * @date 2025-01-21
 */
import request from '@/utils/request'

/** 项目运行阶段统计 */
export function getProjectStageCount() {
  return request({ url: '/oiaudit/analysisReport/projectYxStageCount', method: 'get' })
}

/** 问题整改-审计成果统计 */
export function getRectifyStatistics() {
  return request({ url: '/oiaudit/wtzg/selectWtzgAuditResultsStatistics', method: 'get' })
}

/** 问题清单总数 */
export function getIssueList() {
  return request({ url: '/oiaudit/audit/auditRectify/issue/getAllIssueInfo', method: 'get', params: { pageNumber: 1, pageSize: 1 } })
}

/** 问题整改列表（取totalRecord） */
export function getRectifyList() {
  return request({ url: '/oiaudit/wtzg/list', method: 'get', params: { pageNumber: 1, pageSize: 1 } })
}

/** 跟踪回访列表（取totalRecord） */
export function getFollowUpList() {
  return request({ url: '/oiaudit/gzhf/list', method: 'get', params: { pageNumber: 1, pageSize: 1 } })
}

/** 后续整改列表（取totalRecord） */
export function getSubsequentList() {
  return request({ url: '/oiaudit/hxzg/list', method: 'get', params: { pageNumber: 1, pageSize: 1 } })
}

/** 质量评议统计表（取totalRecord和评分） */
export function getQualityList() {
  return request({ url: '/oiaudit/qualityResult/getList', method: 'get', params: { pageNumber: 1, pageSize: 1 } })
}
