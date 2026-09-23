import request from '@/utils/request'

// 获取文档归档列表
export function getDocumentArchiveList(params) {
  return request({
    url: '/contract/archive/document/list',
    method: 'post',
    data: params,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

// 根据ID获取文档归档详情
export function getDocumentArchiveById(id) {
  return request({
    url: `/contract/archive/document/${id}`,
    method: 'get'
  })
}

// 保存文档归档
export function saveDocumentArchive(data) {
  return request({
    url: '/contract/archive/document/create',
    method: 'post',
    data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

// 更新文档归档
export function updateDocumentArchive(id, data) {
  return request({
    url: `/contract/archive/${id}`,
    method: 'put',
    data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

// 删除文档归档
export function deleteDocumentArchive(id) {
  return request({
    url: `/contract/archive/document/${id}`,
    method: 'delete'
  })
}

// 批量删除文档归档
export function batchDeleteDocumentArchive(ids) {
  return request({
    url: '/contract/archive/batch-delete',
    method: 'post',
    data: { ids },
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

// 导出文档归档
export function exportDocumentArchive(params) {
  return request({
    url: '/contract/archive/export',
    method: 'post',
    data: params,
    responseType: 'blob',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

// 下载文档文件
export function downloadDocumentFile(id) {
  return request({
    url: `/contract/archive/download/${id}`,
    method: 'get',
    responseType: 'blob'
  })
}

// 获取文档归档统计信息
export function getDocumentArchiveStats() {
  return request({
    url: '/contract/archive/stats',
    method: 'get'
  })
}

// 搜索文档归档
export function searchDocumentArchive(keyword) {
  return request({
    url: '/contract/archive/search',
    method: 'get',
    params: { keyword }
  })
}

// 获取项目列表
export function getProjectList(params) {
  return request({
    url: '/contract/project/info/register/list',
    method: 'post',
    data: params,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}
