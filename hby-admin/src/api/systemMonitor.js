import request from '@/utils/request'

/**
 * 系统监控 API
 * 后端服务：exampleBigModelService (8380端口)
 * 接口路径前缀：/v1/monitor
 */

/** 获取全部微服务实例列表 */
export function getMonitorServices() {
  return request({
    url: '/api/bigmodel/v1/monitor/services',
    method: 'get'
  })
}

/** 服务健康汇总 */
export function getHealthSummary() {
  return request({
    url: '/api/bigmodel/v1/monitor/healthSummary',
    method: 'get'
  })
}

/** 服务器基础信息 */
export function getServerInfo() {
  return request({
    url: '/api/bigmodel/v1/monitor/serverInfo',
    method: 'get'
  })
}

/** 服务器实时指标（轮询） */
export function getServerMetrics() {
  return request({
    url: '/api/bigmodel/v1/monitor/serverMetrics',
    method: 'get'
  })
}

/** 磁盘分区信息 */
export function getDiskInfos() {
  return request({
    url: '/api/bigmodel/v1/monitor/disks',
    method: 'get'
  })
}

/** JVM 详情 */
export function getJvmDetails() {
  return request({
    url: '/api/bigmodel/v1/monitor/jvm',
    method: 'get'
  })
}
