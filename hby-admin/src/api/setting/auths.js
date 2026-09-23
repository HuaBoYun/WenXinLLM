import request from '@/utils/request'

export const getAuthList = (data) => {
  return request({
    url: '/setting/right/rightManageRightList',
    method: 'get',
    params: data,
  })
}

export const saveAuthList = (data) => {
  return request({
    url: '/setting/right/saveManageRight',
    method: 'post',
    data,
  })
}
export const updateAuthList = (data) => {
  return request({
    url: '/setting/right/saveManageRight',
    method: 'post',
    data,
  })
}

export const updAuthsStatus = (data) => {
  return request({
    url: '/setting/right/modifySystemRightVisible',
    method: 'get',
    params: data,
  })
}

export const delAuthInfo = (data) => {
  return request({
    url: '/setting/right/removeManageRight',
    method: 'post',
    data,
  })
}

export const getAuthListForUser = (data) => {
  return request({
    url: '/setting/right/getRoleRigetListByType',
    method: 'get',
    params: data,
  })
}

export const saveModelInfo = (data) => {
  return request({
    url: '/setting/customize/customizeScene/saveOrUpdate',
    method: 'post',
    data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
  })
}

export const getZDYTableData = (data) => {
  return request({
    url: '/setting/customize/customizeScene/getList',
    method: 'post',
    data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
  })
}

export const getLRData = (data) => {
  return request({
    url: '/setting/customize/customizeShow/getList',
    method: 'post',
    data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
  })
}
export const addLRZD = (data) => {
  return request({
    url: '/setting/customize/customizeShow/saveOrUpdate',
    method: 'post',
    data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
  })
}
//录入页面修改详情
export const getLRDetail = (data) => {
  return request({
    url: `/setting/customize/customizeShow/${data.id}`,
    method: 'get',
    params: data,
  })
}

//删除页面配置
export const delZDYData = (data) => {
  return request({
    url: `/setting/customize/customizeScene/${data.id}`,
    method: 'DELETE',
    data,
  })
}
//删除字段配置
export const delDetailData = (data) => {
  return request({
    url: `/setting/customize/customizeShow/${data.id}`,
    method: 'DELETE',
    data,
  })
}
//修改配置列表
export const getLRDetailData = (data) => {
  return request({
    url: `/setting/customize/customizeShow/ext/getList`,
    method: 'post',
    data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
  })
}
//修改字段详情
export const getEditZDDetail = (data) => {
  return request({
    url: `/setting/customize/customizeShowExt/${data.id}`,
    method: 'get',
    params: data,
  })
}
//更新修改字段
export const updateLRZD = (data) => {
  return request({
    url: `/setting/customize/customizeShowExt/saveOrUpdate`,
    method: 'post',
    data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
  })
}
//更新修改字段状态
export const changeEditStatus = (data) => {
  return request({
    url: `/setting/customize/customizeShowExt/updateState`,
    method: 'post',
    data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
  })
}
//更新页面配置状态
export const changeListStatus = (data) => {
  return request({
    url: `/setting/customize/customizeScene/updateState`,
    method: 'post',
    data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
  })
}
//批量更新字段状态
export const updateAllStatus = (data) => {
  return request({
    url: `/setting/customize/customizeShowExt/batchUpdateState `,
    method: 'post',
    data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
  })
}