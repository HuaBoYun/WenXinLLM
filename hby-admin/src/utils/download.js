/**
 * 文件下载工具函数
 */

/**
 * 下载文件
 * @param {string} url 下载地址
 * @param {string} filename 文件名（可选）
 */
export function downloadFile(url, filename) {
  // 如果没有提供文件名，尝试从URL中提取
  if (!filename) {
    const urlParts = url.split('/')
    filename = urlParts[urlParts.length - 1] || 'download'
  }

  // 创建一个隐藏的a标签来触发下载
  const link = document.createElement('a')
  link.href = url
  link.download = filename
  link.style.display = 'none'

  // 处理跨域下载
  if (url.includes('://') && !url.includes(location.hostname)) {
    // 跨域情况下，需要使用代理服务或后端接口
    console.warn('跨域下载可能需要后端支持')
  }

  document.body.appendChild(link)
  link.click()
  document.body.removeChild(link)
}

/**
 * 通过数据URL下载文件
 * @param {string} dataUrl 数据URL（如：data:application/vnd.ms-excel;base64,xxx）
 * @param {string} filename 文件名
 */
export function downloadByUrl(dataUrl, filename) {
  const link = document.createElement('a')
  link.href = dataUrl
  link.download = filename
  link.style.display = 'none'

  document.body.appendChild(link)
  link.click()
  document.body.removeChild(link)
}

/**
 * 通过Blob下载文件
 * @param {Blob} blob 文件数据
 * @param {string} filename 文件名
 */
export function downloadByBlob(blob, filename) {
  const url = window.URL.createObjectURL(blob)
  downloadByUrl(url, filename)
  window.URL.revokeObjectURL(url)
}

/**
 * 通过API响应下载文件
 * @param {Response} response fetch响应对象
 * @param {string} filename 文件名（可选）
 * @returns {Promise<void>}
 */
export async function downloadByResponse(response, filename) {
  if (!filename) {
    // 尝试从响应头获取文件名
    const contentDisposition = response.headers.get('content-disposition')
    if (contentDisposition) {
      const filenameMatch = contentDisposition.match(/filename[^;=\n]*=((['"]).*?\2|[^;\n]*)/)
      if (filenameMatch && filenameMatch[1]) {
        filename = filenameMatch[1].replace(/['"]/g, '')
      }
    }

    // 如果还是没有文件名，使用默认名称
    if (!filename) {
      filename = 'download'
    }
  }

  const blob = await response.blob()
  downloadByBlob(blob, filename)
}

/**
 * 使用axios下载文件
 * @param {string} url 下载地址
 * @param {Object} params 请求参数
 * @param {string} filename 文件名（可选）
 * @param {Object} config axios配置（可选）
 * @returns {Promise<void>}
 */
export async function downloadByAxios(url, params = {}, filename, config = {}) {
  try {
    // 这里需要导入axios，为了避免循环依赖，使用window.axios或动态导入
    const axios = window.axios || require('axios')

    const response = await axios({
      method: 'get',
      url,
      params,
      responseType: 'blob',
      ...config
    })

    await downloadByResponse(response, filename)
  } catch (error) {
    console.error('下载文件失败:', error)
    throw error
  }
}

/**
 * 批量下载文件
 * @param {Array} fileList 文件列表 [{url, filename}, ...]
 * @param {number} delay 下载间隔时间（毫秒）
 */
export function batchDownload(fileList, delay = 1000) {
  fileList.forEach((file, index) => {
    setTimeout(() => {
      downloadFile(file.url, file.filename)
    }, index * delay)
  })
}

/**
 * 检查浏览器是否支持文件下载
 * @returns {boolean}
 */
export function isDownloadSupported() {
  return !!document.createElement('a').download
}

/**
 * 获取文件扩展名
 * @param {string} filename 文件名
 * @returns {string}
 */
export function getFileExtension(filename) {
  return filename.slice((filename.lastIndexOf('.') - 1 >>> 0) + 2)
}

/**
 * 创建下载任务
 * @param {string} url 下载地址
 * @param {string} filename 文件名
 * @param {Function} onProgress 进度回调
 * @returns {Promise}
 */
export function createDownloadTask(url, filename, onProgress) {
  return new Promise((resolve, reject) => {
    const axios = window.axios || require('axios')

    axios({
      method: 'get',
      url,
      responseType: 'blob',
      onDownloadProgress: (progressEvent) => {
        if (onProgress) {
          const progress = Math.round((progressEvent.loaded * 100) / progressEvent.total)
          onProgress(progress)
        }
      }
    })
    .then(response => {
      downloadByResponse(response, filename)
      resolve(response)
    })
    .catch(error => {
      console.error('下载失败:', error)
      reject(error)
    })
  })
}

// 默认导出常用的函数
export default {
  downloadFile,
  downloadByUrl,
  downloadByBlob,
  downloadByResponse,
  downloadByAxios,
  batchDownload,
  isDownloadSupported,
  getFileExtension,
  createDownloadTask
}