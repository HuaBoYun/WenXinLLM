/**
 * 业务蓝图结构化落库 API
 * 后端服务：exampleBigModelService，路由 /v1/ai/blueprint
 * 网关前缀：/bigmodel
 */
import request from '@/utils/request'

/** 保存蓝图（主表 + 需求点 + Bug，子表全量重写） */
export function saveBlueprint(data) {
  return request({
    url: '/bigmodel/v1/ai/blueprint/save',
    method: 'post',
    data,
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

/** 蓝图详情（含 requirements 与 bugs） */
export function getBlueprintDetail(id) {
  return request({
    url: `/bigmodel/v1/ai/blueprint/${id}`,
    method: 'get'
  })
}

/** 蓝图列表（不含子表明细） */
export function getBlueprintList(userId) {
  return request({
    url: '/bigmodel/v1/ai/blueprint/list',
    method: 'get',
    params: { userId }
  })
}
