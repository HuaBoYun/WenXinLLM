import { transData } from '@/utils/requestData'
import request from '@/utils/request'

//前缀etl

//资源库
//查询资源库列表
export function getRepositoryList(params) {
  return request({
    url: '/etl/kettle/repository/list?pageNum=1&pageSize=10',
    method: 'get',
    params,
  })
}
//新增保存资源库
export function addRepository(params) {
  return request({
    url: '/etl/kettle/repository/add',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: JSON.stringify(params),
  })
}
//修改保存资源库
export function editRepository(params) {
  return request({
    url: '/etl/kettle/repository/edit',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: JSON.stringify(params),
  })
}
//查询所有资源库
export function getRepositoryListAll(params) {
  return request({
    url: '/etl/kettle/repository/listAll',
    method: 'get',
    params,
  })
}
//删除资源库
export function deleteRepositoryList(params) {
  return request({
    url: `etl/kettle/repository/remove/${params.ids}`,
    method: 'delete',
  })
}

//转换
//转换列表分页
export function getTransList(params) {
  return request({
    url: '/etl/kettle/trans/list?pageNum=1&pageSize=10 ',
    method: 'get',
    params,
  })
}
//新增转换
export function addTrans(params) {
  return request({
    url: '/etl/kettle/trans/add',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: JSON.stringify(params),
  })
}
//修改转换
export function editTrans(params) {
  return request({
    url: '/etl/kettle/trans/edit',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: JSON.stringify(params),
  })
}
//删除转换
export function deleteTrans(params) {
  return request({
    url: `/etl/kettle/trans/remove/${params.ids}`,
    method: 'delete',
  })
}
//通过名称查询转换
export function getTransByName(params) {
  return request({
    url: `/etl/kettle/trans/getByName/${params}`,
    method: 'get',
  })
}
//转换执行一次
export function transRunOnce(params) {
  return request({
    url: '/etl/kettle/trans/run',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: JSON.stringify(params),
  })
}
//停启转换定时任务
export function transRunStop(params) {
  return request({
    url: '/etl/kettle/trans/runStopTransQuartz',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: JSON.stringify(params),
  })
}
//查看日志
export function transLog(params) {
  return request({
    url: `/etl/kettle/trans/log/list`,
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: JSON.stringify(params),
  })
}

//作业
//作业列表分页
export function getJobList(params) {
  return request({
    url: '/etl/kettle/job/list?pageNum=1&pageSize=10 ',
    method: 'get',
    params,
  })
}
//新增作业
export function addJob(params) {
  return request({
    url: '/etl/kettle/job/add',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: JSON.stringify(params),
  })
}
//修改作业
export function editJob(params) {
  return request({
    url: '/etl/kettle/job/edit',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: JSON.stringify(params),
  })
}
//删除作业
export function deleteJob(params) {
  return request({
    url: `/etl/kettle/job/remove/${params.ids}`,
    method: 'delete',
  })
}
//通过名称查询作业
export function getJobByName(params) {
  return request({
    url: `/etl/kettle/job/getByName/${params}`,
    method: 'get',
  })
}
//作业执行一次
export function jobRunOnce(params) {
  return request({
    url: '/etl/kettle/job/run',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: JSON.stringify(params),
  })
}
//停启作业定时任务
export function jobRunStop(params) {
  return request({
    url: '/etl/kettle/job/runStopTransQuartz',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: JSON.stringify(params),
  })
}
//查看日志
export function jobLog(params) {
  return request({
    url: `/etl/kettle/job/log/list`,
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: JSON.stringify(params),
  })
}

//kettle
//跳转到kettle
export function gotoKettle(params) {
  return request({
    url: '/etl/api/kettle/getUrl',
    method: 'get',
    params,
  })
}
//统计接口
export function getCount(params) {
  return request({
    url: 'etl/api/kettle/count',
    method: 'get',
    params,
  })
}
