import request from '@/utils/request'

/**
 * 下载导入模板
 */
export function downloadTemplate() {
  return request({
    url: '/cwgxAi/enterpriseReport/reportDataImport/downloadTemplate',
    method: 'get',
    responseType: 'blob'
  })
}

/**
 * 导入Excel数据
 */
export function importExcel(formData) {
  return request({
    url: '/cwgxAi/enterpriseReport/reportDataImport/importExcel',
    method: 'post',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

/**
 * 导出数据为Excel
 */
export function exportExcel(data) {
  return request({
    url: '/cwgxAi/enterpriseReport/reportDataImport/exportExcel',
    method: 'post',
    data,
    responseType: 'blob'
  })
}

