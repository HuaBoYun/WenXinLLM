/**
 * 技术创新管理API接口
 * 所有接口走 /monitor 前缀，由网关转发到后端服务
 */
import request from '@/utils/request'

const JSON_HEADERS = { 'Content-Type': 'application/json;charset=UTF-8' }

// ==================== 研发项目管理 ====================
export const projectApi = {
  getList(data) {
    return request({ url: '/monitor/v1/enterprise/innovation/project/list', method: 'post', data, headers: JSON_HEADERS })
  },
  getDetail(id) {
    return request({ url: `/monitor/v1/enterprise/innovation/project/${id}`, method: 'get' })
  },
  add(data) {
    return request({ url: '/monitor/v1/enterprise/innovation/project', method: 'post', data, headers: JSON_HEADERS })
  },
  update(id, data) {
    return request({ url: `/monitor/v1/enterprise/innovation/project/${id}`, method: 'put', data, headers: JSON_HEADERS })
  },
  delete(id) {
    return request({ url: `/monitor/v1/enterprise/innovation/project/${id}`, method: 'delete' })
  },
  batchDelete(data) {
    return request({ url: '/monitor/v1/enterprise/innovation/project/batch/delete', method: 'post', data, headers: JSON_HEADERS })
  },
  export() {
    return request({ url: '/monitor/v1/enterprise/innovation/project/export', method: 'get', responseType: 'blob' })
  }
}

// ==================== 知识产权管理 ====================
export const patentApi = {
  getList(data) {
    return request({ url: '/monitor/v1/enterprise/innovation/patent/list', method: 'post', data, headers: JSON_HEADERS })
  },
  getDetail(id) {
    return request({ url: `/monitor/v1/enterprise/innovation/patent/${id}`, method: 'get' })
  },
  add(data) {
    return request({ url: '/monitor/v1/enterprise/innovation/patent', method: 'post', data, headers: JSON_HEADERS })
  },
  update(id, data) {
    return request({ url: `/monitor/v1/enterprise/innovation/patent/${id}`, method: 'put', data, headers: JSON_HEADERS })
  },
  delete(id) {
    return request({ url: `/monitor/v1/enterprise/innovation/patent/${id}`, method: 'delete' })
  },
  batchDelete(data) {
    return request({ url: '/monitor/v1/enterprise/innovation/patent/batch/delete', method: 'post', data, headers: JSON_HEADERS })
  },
  export() {
    return request({ url: '/monitor/v1/enterprise/innovation/patent/export', method: 'get', responseType: 'blob' })
  }
}

// ==================== 技术合作管理 ====================
export const cooperationApi = {
  getList(data) {
    return request({ url: '/monitor/v1/enterprise/innovation/cooperation/list', method: 'post', data, headers: JSON_HEADERS })
  },
  getDetail(id) {
    return request({ url: `/monitor/v1/enterprise/innovation/cooperation/${id}`, method: 'get' })
  },
  add(data) {
    return request({ url: '/monitor/v1/enterprise/innovation/cooperation', method: 'post', data, headers: JSON_HEADERS })
  },
  update(id, data) {
    return request({ url: `/monitor/v1/enterprise/innovation/cooperation/${id}`, method: 'put', data, headers: JSON_HEADERS })
  },
  delete(id) {
    return request({ url: `/monitor/v1/enterprise/innovation/cooperation/${id}`, method: 'delete' })
  },
  batchDelete(data) {
    return request({ url: '/monitor/v1/enterprise/innovation/cooperation/batch/delete', method: 'post', data, headers: JSON_HEADERS })
  },
  export() {
    return request({ url: '/monitor/v1/enterprise/innovation/cooperation/export', method: 'get', responseType: 'blob' })
  }
}

// ==================== 成果转化管理 ====================
export const achievementApi = {
  getList(data) {
    return request({ url: '/monitor/v1/enterprise/innovation/achievement/list', method: 'post', data, headers: JSON_HEADERS })
  },
  getDetail(id) {
    return request({ url: `/monitor/v1/enterprise/innovation/achievement/${id}`, method: 'get' })
  },
  add(data) {
    return request({ url: '/monitor/v1/enterprise/innovation/achievement', method: 'post', data, headers: JSON_HEADERS })
  },
  update(id, data) {
    return request({ url: `/monitor/v1/enterprise/innovation/achievement/${id}`, method: 'put', data, headers: JSON_HEADERS })
  },
  delete(id) {
    return request({ url: `/monitor/v1/enterprise/innovation/achievement/${id}`, method: 'delete' })
  },
  batchDelete(data) {
    return request({ url: '/monitor/v1/enterprise/innovation/achievement/batch/delete', method: 'post', data, headers: JSON_HEADERS })
  },
  export() {
    return request({ url: '/monitor/v1/enterprise/innovation/achievement/export', method: 'get', responseType: 'blob' })
  }
}

// ==================== 创新团队管理 ====================
export const teamApi = {
  getList(data) {
    return request({ url: '/monitor/v1/enterprise/innovation/team/list', method: 'post', data, headers: JSON_HEADERS })
  },
  getDetail(id) {
    return request({ url: `/monitor/v1/enterprise/innovation/team/${id}`, method: 'get' })
  },
  add(data) {
    return request({ url: '/monitor/v1/enterprise/innovation/team', method: 'post', data, headers: JSON_HEADERS })
  },
  update(id, data) {
    return request({ url: `/monitor/v1/enterprise/innovation/team/${id}`, method: 'put', data, headers: JSON_HEADERS })
  },
  delete(id) {
    return request({ url: `/monitor/v1/enterprise/innovation/team/${id}`, method: 'delete' })
  },
  batchDelete(data) {
    return request({ url: '/monitor/v1/enterprise/innovation/team/batch/delete', method: 'post', data, headers: JSON_HEADERS })
  },
  export() {
    return request({ url: '/monitor/v1/enterprise/innovation/team/export', method: 'get', responseType: 'blob' })
  }
}

// ==================== 技术评估管理 ====================
export const evaluationApi = {
  getList(data) {
    return request({ url: '/monitor/v1/enterprise/innovation/evaluation/list', method: 'post', data, headers: JSON_HEADERS })
  },
  getDetail(id) {
    return request({ url: `/monitor/v1/enterprise/innovation/evaluation/${id}`, method: 'get' })
  },
  add(data) {
    return request({ url: '/monitor/v1/enterprise/innovation/evaluation', method: 'post', data, headers: JSON_HEADERS })
  },
  update(id, data) {
    return request({ url: `/monitor/v1/enterprise/innovation/evaluation/${id}`, method: 'put', data, headers: JSON_HEADERS })
  },
  delete(id) {
    return request({ url: `/monitor/v1/enterprise/innovation/evaluation/${id}`, method: 'delete' })
  },
  batchDelete(data) {
    return request({ url: '/monitor/v1/enterprise/innovation/evaluation/batch/delete', method: 'post', data, headers: JSON_HEADERS })
  },
  export() {
    return request({ url: '/monitor/v1/enterprise/innovation/evaluation/export', method: 'get', responseType: 'blob' })
  }
}

// ==================== 统计接口 ====================
export function getInnovationStatistics() {
  return request({ url: '/monitor/v1/enterprise/innovation/statistics', method: 'get' })
}

export default {
  projectApi,
  patentApi,
  cooperationApi,
  achievementApi,
  teamApi,
  evaluationApi,
  getInnovationStatistics
}
