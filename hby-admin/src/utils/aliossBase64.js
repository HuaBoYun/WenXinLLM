const OSS = require('ali-oss')

const client = new OSS({
  region: '-------',
  endpoint: '--------',
  accessKeyId: '--------',
  accessKeySecret: '-------',
  bucket: '---',
})

//上传base64图片到阿里云
function uploadOssBase64(base64Img, callback) {
  let suffix = 'jpg'
  let imgType = base64Img.split(';base64')[0]
  if (imgType === 'data:image/png') {
    suffix = 'png'
  }
  // base64转blob
  const blob = dataURLtoBlob(base64Img)
  // blob转arrayBuffer
  const reader = new FileReader()
  reader.readAsArrayBuffer(blob)
  reader.onload = (event) => {
    // arrayBuffer转Buffer
    const buffer = toBuffer(event.target.result)
    //命名规则 以后缀为文件目录  md5为文件名  可自定义
    //${this.suffix}/${this.pw}.${this.suffix}
    client
      .put('mchl/' + getDatePath() + randomString(10) + '.' + suffix, buffer)
      .then((res) => {
        console.log(res)
        callback(res)
      })
      .catch((err) => {
        console.log(err)
        callback('')
      })
  }
}

//上传本地文件
// eslint-disable-next-line no-unused-vars
function ajax(url, callback, options) {
  window.URL = window.URL || window.webkitURL
  var xhr = new XMLHttpRequest()
  xhr.open('get', url, true)
  if (options.responseType) {
    xhr.responseType = options.responseType
  }
  xhr.onreadystatechange = function () {
    if (xhr.readyState === 4 && xhr.status === 200) {
      callback(xhr)
    }
  }
  xhr.send()
}

//上传项目内文件到阿里云
// eslint-disable-next-line no-unused-vars
function updateModel() {
  //要上传的文件
  let url = './model.obj'
  this.ajax(
    url,
    (xhr) => {
      var blob = new Blob([xhr.response])
      // blob转arrayBuffer
      const reader = new FileReader()
      reader.readAsArrayBuffer(blob)
      reader.onload = (event) => {
        // arrayBuffer转Buffer
        const buffer = toBuffer(event.target.result)
        client
          .put(`obj/${this.pw}.obj`, buffer)
          .then((res) => {
            console.log(res.url) //文件在阿里云路径,不能在地址栏访问
            let signUrl = client.signatureUrl(res.name, { expires: 3600 })
            console.log(signUrl) //文件可以在地址栏访问
            //把生成的阿里云地址保存起来
            this.$store.dispatch('getObjUrl', res.url)
          })
          .catch((err) => {
            console.log(err)
          })
      }
    },
    {
      responseType: 'blob',
    }
  )
}

// base64转blob
function dataURLtoBlob(dataurl) {
  var arr = dataurl.split(','),
    mime = arr[0].match(/:(.*?);/)[1],
    bstr = atob(arr[1]),
    n = bstr.length,
    u8arr = new Uint8Array(n)
  while (n--) {
    u8arr[n] = bstr.charCodeAt(n)
  }
  return new Blob([u8arr], { type: mime })
}
//ArrayBuffer ---> Buffer
function toBuffer(ab) {
  var buf = new Buffer(ab.byteLength)
  var view = new Uint8Array(ab)
  for (var i = 0; i < buf.length; ++i) {
    buf[i] = view[i]
  }
  return buf
}
//生成日期目录
function getDatePath() {
  var now = new Date()
  var year = now.getFullYear() //得到年份
  var month = now.getMonth() //得到月份
  var date = now.getDate() //得到日期
  // eslint-disable-next-line no-unused-vars
  var hour = now.getHours() //得到小时
  // eslint-disable-next-line no-unused-vars
  var minu = now.getMinutes() //得到分钟
  month = month + 1
  if (month < 10) month = '0' + month
  if (date < 10) date = '0' + date
  return year + '/' + month + '/' + date + '/'
}
//随机字符串
function randomString(length) {
  var str = '0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ'
  var result = ''
  for (var i = length; i > 0; --i)
    result += str[Math.floor(Math.random() * str.length)]
  return result
}

export { uploadOssBase64 }
