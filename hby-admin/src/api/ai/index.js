import request from '@/utils/request'
import { transData } from '@/utils/requestData'


// 题目生成
export function generateFinance(data) {
  return request({
    url: '/zbgl/generate/generateFinance',
    method: 'post',
    data: transData(data),
  })
}

// 数据库信息
export function getCwztDbInfo(data) {
  return request({
    url: '/zbgl/generate/getCwztDbInfo',
    method: 'post',
    data: transData(data),
  })
}

//获取上传excel信息
export function getTableList(params) {
  return request({
    url: '/zbgl/gbi/export/getTableList',
    method: 'get',
    params,
  })
}

//获取table信息
export function getTableData(data) {
  return request({
    url: '/zbgl/gbi/export/getTableData',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: data,
  })
}

//知识列表
export function getKnowledgeList(params) {
  return request({
    url: '/zbgl/gbi/export/getKnowledgeList',
    method: 'get',
    params,
  })
}

//获取用户应用配置
export function getApplicationConfig(params) {
  return request({
    url: '/zbgl/gbi/export/applicationConfig',
    method: 'get',
    params,
  })
}

//修改用户应用配置
export function saveApplicationConfig(data) {
  return request({
    url: '/zbgl/gbi/export/saveApplicationConfig',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: data,
  })
}

//新增知识
export function addKnowledge(data) {
  return request({
    url: '/zbgl/gbi/export/addKnowledge',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: data,
  })
}

//删除知识
export function deleteKnowledge(params) {
  return request({
    url: '/zbgl/gbi/export/deleteKnowledge',
    method: 'delete',
    params,
  })
}

//修改知识
export function updateKnowledge(data) {
  return request({
    url: '/zbgl/gbi/export/updateKnowledge',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: data,
  })
}

//修改表
export function updateTableColumns(data) {
  return request({
    url: '/zbgl/gbi/export/updateTableColumns',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: data,
  })
}

// 智能体
// 智能体列表
export function getAgentList(data) {
  return request({
    url: '/setting/agent/getList',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: data,
  })
}

// 新增或修改智能体
export function sageOrUpdateAgent(data) {
  return request({
    url: '/setting/agent/saveOrUpdate',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: data,
  })
}
// 删除智能体
export function deleteAgent(data) {
  return request({
    url: `/setting/agent/${data.id}`,
    method: 'delete',
  })
}

// 智能体下发
export function agentIssued(data) {
  return request({
    url: '/setting/agent-issued-permission',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: data,
  })
}

// 智能体模块下发
export function updateIssuseModule(data) {
  return request({
    url: '/setting/update-agent-issued-module',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: data,
  })
}

// 智能体下发列表
export function agentIssuedList(data) {
  return request({
    url: '/setting/agent-issued/getList',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: data,
  })
}

// 模块下智能体列表
export function moduleAgentList(data) {
  return request({
    url: '/setting/agent-module/getList',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: data,
  })
}

// 最近对话列表
export function getAgentDialogueList(data) {
  return request({
    url: '/setting/agent-dialogue/getList',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: data,
  })
}

// 新增或修改最近对话
export function sageOrUpdateAgentDialogue(data) {
  return request({
    url: '/setting/agent-dialogue/saveOrUpdate',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: data,
  })
}
