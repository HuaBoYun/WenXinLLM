import request from '@/utils/request'
import { transData } from '@/utils/requestData'

// export function getAlerts(params) {
//   return request({
//     url: '/api-auth/home/conference/getList',
//     method: 'post',
//     headers: {
//       'Content-Type': 'application/json;charset=UTF-8',
//     },
//     data: JSON.stringify(transData(params)),
//   })
// }

// 消息通知
export function getAlerts(data) {
  return request({
    url: '/fwgl/api-auth/home/conference/getList',
    method: 'POST',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data,
  })
}

// 法务人员持证上岗率
export function getPersonnel(data) {
  return request({
    url: '/fwgl/api-auth/home/legal/personnel',
    method: 'POST',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data,
  })
}

// 公司律师人数占比
export function getProportion(data) {
  return request({
    url: '/fwgl/api-auth/home/firm/legal/proportion',
    method: 'POST',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data,
  })
}

// 本年度制度法律合规审查数量
export function getLegal(data) {
  return request({
    url: '/fwgl/api-auth/home/institution/compliance',
    method: 'POST',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data,
  })
}
// 本年重大决策法律合规审查数量
export function getCompliance(data) {
  return request({
    url: '/fwgl/api-auth/home/business/institution/compliance',
    method: 'POST',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data,
  })
}
//在手法律诉讼案件数量，涉诉金额
export function getInHand(params) {
  return request({
    url: '/contract/contract/legalLitigationCaseStat',
    method: 'get',
    params: transData(params),
  })
}
//本年合同法律合规审查数量、金额
export function getContractLegal(params) {
  return request({
    url: '/contract/contract/contractLegalAprStat',
    method: 'get',
    params: transData(params),
  })
}

// 法律纠纷案件数量、涉及金额分析 disputeMoneyList
export function getDisputeMoneyList(params) {
  return request({
    url: '/contract/legal/disputeMoneyList',
    method: 'post',
    params: transData(params),
  })
}

//法务人员
export function getEcharts1(data) {
  return request({
    url: '/fwgl/api-auth/home/legal/personnel/count',
    method: 'POST',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data,
  })
}

//公司律师人数
export function getEcharts2(data) {
  return request({
    url: '/fwgl/api-auth/home/company/lawyer/count',
    method: 'POST',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data,
  })
}

// 公司律师人数占比
export function getEcharts3(data) {
  return request({
    url: '/fwgl/api-auth/home/firm/legal/proportion',
    method: 'POST',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data,
  })
}

// 法务人员持证上岗率
export function getEcharts4(data) {
  return request({
    url: '/fwgl/api-auth/home/legal/personnel',
    method: 'POST',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data,
  })
}

//法务人数数量-专职法务人鱼/兼职法务人 占比
export function getEcharts5(data) {
  return request({
    url: '/fwgl/api-auth/home/legal/fullime/percentage',
    method: 'POST',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data,
  })
}

//全体系经营事项及制度审核数量
export function getEcharts6(data) {
  return request({
    url: '/fwgl/api-auth/home/legal/institution',
    method: 'POST',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data,
  })
}

//本年度制度法律合规审查数量
export function getEcharts7(data) {
  return request({
    url: '/fwgl/api-auth/home/institution/compliance',
    method: 'POST',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data,
  })
}

//全体系经营事项及制度审核数量
export function getEcharts8(data) {
  return request({
    url: '/fwgl/api-auth/home/legal/institution',
    method: 'POST',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data,
  })
}
