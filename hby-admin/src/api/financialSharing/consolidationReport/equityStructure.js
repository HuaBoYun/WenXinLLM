import request from '@/utils/request'

/**
 * 计算股权结构
 */
export function calculateEquityStructure(data) {
  return request({
    url: '/cwgxAi/consolidationReport/equityStructure/calculateEquityStructure',
    method: 'post',
    data
  })
}

/**
 * 查询股权结构列表
 */
export function getEquityStructureList(data) {
  return request({
    url: '/cwgxAi/consolidationReport/equityStructure/getEquityStructureList',
    method: 'post',
    data
  })
}

/**
 * 查询股权结构树
 */
export function getEquityStructureTree(data) {
  return request({
    url: '/cwgxAi/consolidationReport/equityStructure/getEquityStructureTree',
    method: 'post',
    data
  })
}

/**
 * 删除股权结构
 */
export function deleteEquityStructure(data) {
  return request({
    url: '/cwgxAi/consolidationReport/equityStructure/deleteEquityStructure',
    method: 'post',
    data
  })
}

