/**
 * 业务梳理（BusinessReview）相关 API
 * 后端服务：exampleBigModelService，路由路径 /v1/ai/business
 * 网关前缀：/bigmodel
 */
import request from '@/utils/request'

// ============ 模板（左侧树） ============

/** 模板列表（左侧树） */
export function getTemplateList() {
  return request({
    url: '/bigmodel/v1/ai/business/template/list',
    method: 'get'
  })
}

/** 新建模板 */
export function createTemplate(data) {
  return request({
    url: '/bigmodel/v1/ai/business/template/create',
    method: 'post',
    data,
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

/** 删除模板 */
export function deleteTemplate(id) {
  return request({
    url: `/bigmodel/v1/ai/business/template/${id}`,
    method: 'delete'
  })
}

/** 模板详情 */
export function getTemplateDetail(id) {
  return request({
    url: `/bigmodel/v1/ai/business/template/${id}`,
    method: 'get'
  })
}

// ============ 文档版本 ============

/** 获取最新版本（含 content） */
export function getLatestDoc(templateId) {
  return request({
    url: `/bigmodel/v1/ai/business/doc/latest/${templateId}`,
    method: 'get'
  })
}

/** 发布新版本 */
export function publishDocVersion(data) {
  return request({
    url: '/bigmodel/v1/ai/business/doc/save-version',
    method: 'post',
    data,
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

/** 首次种子内容写入（仅在该模板无版本时生效） */
export function seedDocVersion(data) {
  return request({
    url: '/bigmodel/v1/ai/business/doc/seed',
    method: 'post',
    data,
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

/** 版本列表（不含 content） */
export function listDocVersions(templateId) {
  return request({
    url: `/bigmodel/v1/ai/business/doc/versions/${templateId}`,
    method: 'get'
  })
}

/** 获取指定版本（含 content） */
export function getDocVersion(versionId) {
  return request({
    url: `/bigmodel/v1/ai/business/doc/version/${versionId}`,
    method: 'get'
  })
}

// ============ 用户草稿 ============

/** 保存草稿 */
export function saveUserDraft(data) {
  return request({
    url: '/bigmodel/v1/ai/business/draft/save',
    method: 'post',
    data,
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

/** 用户草稿列表 */
export function listUserDrafts(userId, templateId) {
  return request({
    url: '/bigmodel/v1/ai/business/draft/list',
    method: 'get',
    params: { userId, templateId }
  })
}

/** 草稿详情 */
export function getUserDraft(id) {
  return request({
    url: `/bigmodel/v1/ai/business/draft/${id}`,
    method: 'get'
  })
}

/** 删除草稿 */
export function deleteUserDraft(id) {
  return request({
    url: `/bigmodel/v1/ai/business/draft/${id}`,
    method: 'delete'
  })
}

/** 业务蓝图分析：AI 分析文档内容，返回结构化需求条目 */
export function analyzeBlueprintRequirements(data) {
  return request({
    url: '/bigmodel/v1/ai/blueprint/analyze',
    method: 'post',
    data,
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

// ============ 签字确认 ============

/** 对需求最新版本签字确认 */
export function confirmDoc(data) {
  return request({
    url: '/bigmodel/v1/ai/business/confirm',
    method: 'post',
    data,
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

/** 某需求的确认记录列表 */
export function getConfirmRecords(templateId) {
  return request({
    url: `/bigmodel/v1/ai/business/confirm/${templateId}`,
    method: 'get'
  })
}

