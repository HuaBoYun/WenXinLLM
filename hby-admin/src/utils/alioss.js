import { getOssConfig } from '@/api/common'
import OSS from 'ali-oss'

let accessid = ''
let policyBase64 = ''
let signature = ''
let callbackbody = ''
let key = ''
// eslint-disable-next-line no-unused-vars
let expire = 0
let host = ''
let g_object_name = ''
// eslint-disable-next-line no-unused-vars
let now = Date.parse(new Date()) / 1000

// 生成随机字符串
function random_string(len) {
  len = len || 32
  var chars = 'ABCDEFGHJKMNPQRSTWXYZabcdefhijkmnprstwxyz2345678'
  var maxPos = chars.length
  var pwd = ''
  for (let i = 0; i < len; i++) {
    pwd += chars.charAt(Math.floor(Math.random() * maxPos))
  }
  return pwd
}

// 获取用户上传原始文件名
function get_file_name(filename) {
  const pos = filename.lastIndexOf('.')
  let suffix = ''
  if (pos !== -1) {
    suffix = filename.substring(pos)
  }
  return suffix
}

//  把随机生成的字符串拼接在原始上传文件名后面生成新的唯一文件名
function set_file_name(filename) {
  const suffix = get_file_name(filename)
  g_object_name = key + random_string(10) + suffix
  return ''
}

// 获取后端返回的签名信息，生成oss参数
async function oss(filename = null) {
  // 可以判断当前expire是否超过了当前时间， 如果超过了当前时间， 就重新取一下， 3 s 作为缓冲。
  now = Date.parse(new Date()) / 1000
  // if (expire < (now + 15)) {
  // 调用后端服务器接口获取签名信息，利用axios返回promise，可以链式调用
  let res = await getOssConfig()
  if (res.code !== 1) {
    return ''
  } else {
    res = res.data
    res = JSON.parse(res)
    const NewPAth = res.dir
    policyBase64 = res['policy']
    accessid = res['accessid']
    signature = res['signature']
    expire = parseInt(res['expire'])
    callbackbody = res['callback']
    host = res['host']
    key = NewPAth
    if (filename != null) {
      set_file_name(filename)
    }
    // 返回表单上传需要的参数信息
    return {
      host: host,
      key: g_object_name,
      policy: policyBase64,
      OSSAccessKeyId: accessid,
      success_action_status: '200', // 让服务端返回200,不然，默认会返回204
      callback: callbackbody,
      signature: signature,
    }
  }
  // } else {
  //   this.$message.error(`上传文件过快,请${expire - (now + 15)}秒后再次尝试`);
  // }
}

function GetDownloadUrl(name) {
  const client = new OSS({
    region: 'oss-cn-beijing',
    accessKeyId: '-----',
    accessKeySecret: '----',
    bucket: '-----',
  })
  return client.signatureUrl(name, {
    expires: 3600,
    response: {
      'content-disposition': 'attachment;',
    },
  })
}

export { oss, GetDownloadUrl }
