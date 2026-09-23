/**
 * 业务蓝图草稿 API
 */
import request from '@/utils/request'

/** 保存草稿 */
export function saveBlueprintDraft(data) {
  return request({
    url: '/bigmodel/v1/ai/blueprint/draft/save',
    method: 'post',
    data,
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

/** 查询草稿列表（不含 formData） */
export function getBlueprintDraftList(userId) {
  return request({
    url: '/bigmodel/v1/ai/blueprint/draft/list',
    method: 'get',
    params: { userId }
  })
}

/** 获取草稿详情（含 formData） */
export function getBlueprintDraftDetail(id) {
  return request({
    url: `/bigmodel/v1/ai/blueprint/draft/${id}`,
    method: 'get'
  })
}

/** 删除草稿 */
export function deleteBlueprintDraft(id) {
  return request({
    url: `/bigmodel/v1/ai/blueprint/draft/${id}`,
    method: 'delete'
  })
}
