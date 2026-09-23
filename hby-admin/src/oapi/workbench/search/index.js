import request from '@/utils/request'
import { transData } from '@/utils/requestData'

export function fetchApi(urlData, params, noRest) {
  // console.log(noRest)
  const fetchData = {
    url: urlData.url,
    method: urlData.method,
  }

  if (urlData.method === 'post') {
    fetchData.headers = { 'Content-Type': 'application/json;charset=UTF-8' }
    fetchData.data = JSON.stringify(transData(params))
  }

  if (urlData.method === 'get' || urlData.method === 'delete') {
    fetchData.url = !noRest ? urlData.url : urlData.url + '/' + params.id
    fetchData.params = params
  }

  if (noRest) {
    fetchData.responseType = 'blob'
  }

  return request(fetchData)
}

//法律搜索
export const lawSearch = {
  getList: {
    url: '/es/es/rule/outDataByPage',
    method: 'post',
  },
}
//制度搜索
export const orderSearch = {
  getList: {
    url: '/es/es/rule/inDataByPage',
    method: 'post',
  },
}
export const accSearch = {
  getList: {
    url: '/es/acc/accDataByPage',
    method: 'post',
  },
}
