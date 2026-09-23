import JSEncrypt from 'jsencrypt'
import { getPublicKey } from '@/api/publicKey'

// 已脱敏：原 RSA 私钥已移除，开源环境不可用解密功能
const privateKey = ''

/**
 * 最长加密长度
 * @type {number}
 */
const MAX_ENCRYPT_BLOCK = 117
/**
 * 最长解码长度
 * @type {number}
 */
const MAX_DECRYPT_BLOCK = 128

/**
 * @description RSA加密(支持长字符加密)
 * @param data
 * @returns {Promise<{param: PromiseLike<ArrayBuffer>}|*>}
 */
export async function encryptedData(data) {
  let publicKey
  const res = await getPublicKey()
  publicKey = res.data.publicKey
  if (res.data.mockServer) {
    publicKey = ''
  }
  if (publicKey === '') {
    return data
  }
  const encrypt = new JSEncrypt()
  encrypt.setPublicKey(
    `-----BEGIN PUBLIC KEY-----${publicKey}-----END PUBLIC KEY-----`
  )
  let bufTmp = ''
  let hexTmp = ''
  let result = ''
  const buffer = Buffer.from(JSON.stringify(data))
  let offSet = 0
  const inputLen = buffer.length
  while (inputLen - offSet > 0) {
    if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
      bufTmp = buffer.slice(offSet, offSet + MAX_ENCRYPT_BLOCK)
    } else {
      bufTmp = buffer.slice(offSet, inputLen)
    }
    hexTmp = encrypt.encrypt(bufTmp.toString())
    result += atob(hexTmp)
    offSet += MAX_ENCRYPT_BLOCK
  }
  return btoa(result)
}

/**
 * @description RSA解密(支持长字符解密)
 * @param data
 * @returns {PromiseLike<ArrayBuffer>}
 */
export function decryptedData(data) {
  const decrypt = new JSEncrypt()
  decrypt.setPrivateKey(
    `-----BEGIN RSA PRIVATE KEY-----${privateKey}-----END RSA PRIVATE KEY-----`
  )
  let bufTmp = ''
  let hexTmp = ''
  let result = ''
  const buffer = atob(data)
  let offSet = 0
  const inputLen = buffer.length
  while (inputLen - offSet > 0) {
    if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
      bufTmp = buffer.slice(offSet, offSet + MAX_DECRYPT_BLOCK)
    } else {
      bufTmp = buffer.slice(offSet, inputLen)
    }
    hexTmp = decrypt.decrypt(btoa(bufTmp))
    result += hexTmp
    offSet += MAX_DECRYPT_BLOCK
  }
  return JSON.parse(result)
}
