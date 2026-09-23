/**
 * 下载文件
 */
export const downloadFile = (boloObj, fileName) => {
  const elink = document.createElement('a')
  elink.download = fileName //定义表格名称，后缀是文件格式
  elink.style.display = 'none'

  // 如果已经是 Blob 对象，直接使用；否则创建新的 Blob
  const blob = boloObj instanceof Blob ? boloObj : new Blob([boloObj], {
    type: 'text/csv,charset=UTF-8',
  })

  elink.href = URL.createObjectURL(blob)
  document.body.appendChild(elink)
  elink.click()
  document.body.removeChild(elink)
}
//下载word
export const downloadFileWord = (boloObj, fileName) => {
  const elink = document.createElement('a')
  elink.download = fileName //定义表格名称，后缀是文件格式
  elink.style.display = 'none'
  const blob = new Blob([boloObj], {
    type: 'application/vnd.openxmlformats-officedocument.wordprocessingml.document'
  })
  elink.href = URL.createObjectURL(blob)
  document.body.appendChild(elink)
  elink.click()
  document.body.removeChild(elink)
}
