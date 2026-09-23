/**
 * AI 文件解析 API
 * 上传文档到后端，抽取为纯文本，供 AI 咨询分析。
 * 支持格式：md / txt / doc / docx / xls / xlsx / pdf
 */
import request from '@/utils/request'

/**
 * 上传文件并解析为文本
 * @param {File} file 浏览器 File 对象
 * @returns {Promise} data: { fileName, content, length }
 */
export function parseFile(file) {
  const formData = new FormData()
  formData.append('file', file)
  return request({
    url: '/bigmodel/v1/ai/file/parse',
    method: 'post',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data'
    },
    // 文件解析可能较慢，单独放宽超时
    timeout: 120000
  })
}
