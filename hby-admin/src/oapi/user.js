import { loginRSA } from '@/config'
import { encryptedData } from '@/utils/encrypt'
import request from '@/utils/request'

export async function login(data) {
  if (loginRSA) {
    data = await encryptedData(data)
  }
  return request({
    url: '/setting/login/loginCheck',
    method: 'post',
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8',
    },
    data,
  })
}

export async function socialLogin(data) {
  if (loginRSA) {
    data = await encryptedData(data)
  }
  return request({
    url: '/setting/socialLogin',
    method: 'post',
    data,
  })
}

export function getUserInfo(data) {
  return request({
    url: '/setting/user/info',
    // method: 'get',
    method: 'post',
    data,
  })
}

export function logout() {
  return request({
    url: '/setting/logout',
    method: 'get',
  })
}

export function register(data) {
  return request({
    url: '/setting/register',
    method: 'post',
    data,
  })
}

export function getUserInfoEntity(data) {
  return request({
    url: '/setting/user/getUserInfoEntity',
    method: 'get',
    params: data,
  })
}
//切换组织
export function changeOrgInfo(data) {
  return request({
    url: '/setting/handoff',
    method: 'get',
    params: data,
  })
}

// 通过ticket获取token
export function getTokenByTicket(data) {
  return request({
    url: '/setting/login/oaLoginCheck',
    method: 'get',
    params: data,
  })
}

// 通过ticket获取token
export function getTokenByV5ticket(data) {
  return request({
    url: '/setting/login/oaSendLoginCheck',
    method: 'get',
    params: data,
  })
}

// 通过username获取token
export function getTokenByUsername(data) {
  return request({
    url: '/setting/login/lcLoginCheck',
    method: 'get',
    params: data,
  })
}
