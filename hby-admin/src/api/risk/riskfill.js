import request from '@/utils/request'
import { transData } from '@/utils/requestData'

// 风险事件填报 -- 列表数据
export const riskReportingList = (params) => {
  return request({
    url: '/riskcontrol/reporting/riskReportingList' + `?pageNumber=${params.pageNumber}&pageSize=${params.pageSize}&entName=${params.entName}`,
    method: 'POST',
    // data: transData(params),
    // headers: {
    //   'Content-Type': 'application/json;charset=UTF-8',
    // },
  })
}
// 风险事件填报 -- 删除
export const riskReportingDelete = (params) => {
  return request({
    url: '/riskcontrol/reporting/riskReportingDelete',
    method: 'POST',
    data: transData(params),
  })
}
// 风险事件填报 -- 新增修改
export const riskReportingSaveOrUpdate = (params) => {
  return request({
    url: '/riskcontrol/reporting/insertOrUpdate' + '?attIds=' + params.attIds,
    method: 'POST',
    data: transData(params),
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
  })
}
// 风险事件填报 -- 获取附件信息
export const getRiskReportingAttInfo = (params) => {
  return request({
    url: '/riskcontrol/reporting/getRiskReportingAttInfo',
    method: 'POST',
    data: transData(params),
  })
}
// 风险事件填报 -- 详情
export const riskReportingDetails = (params) => {
  return request({
    url: '/riskcontrol/reporting/riskReportingDetails',
    method: 'POST',
    data: transData(params),
  })
}

// 风险事件填报 -- 下发列表 -- 列表数据
export const issuedImplementList = (params) => {
  return request({
    url: '/riskcontrol/implement/issuedImplementList' + `?pageNumber=${params.pageNumber}&pageSize=${params.pageSize}&impRiskName=${params.impRiskName}`,
    method: 'POST',
    data: transData(params),
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
  })
}

// 风险事件填报 -- 下发
export const issuedAndSubmit = (params) => {
  return request({
    url: '/riskcontrol/implement/issuedAndSubmit',
    method: 'POST',
    data: transData(params),
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
  })
}

// 风险事件填报 -- 下发详情
export const issuedImplementDetails = (params) => {
  return request({
    url: '/riskcontrol/implement/issuedImplementDetails',
    method: 'POST',
    data: transData(params),
  })
}


// 风险事件库 -- 文件下载
export const downFieldById = (params) => {
  return request({
    url: '/riskcontrol/download',
    method: 'post',
    responseType: 'blob',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    params,
  })
}

// 月度评估(分公司） -- 列表数据
export const queryMonthlyEvaluationList = (params) => {
  return request({
    url: '/riskcontrol/monthlyEvaluation/queryMonthlyEvaluationList' + `?riskid=${params.riskid}&pageNumber=${params.pageNumber}&pageSize=${params.pageSize}`,
    method: 'POST',
    data: transData(params),
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
  })
}
// 月度评估(分公司） -- 保存修改
export const evaluationInsertOrUpdate = (data, params) => {
  return request({
    url: '/riskcontrol/monthlyEvaluation/insertOrUpdate' + `?attIds=${params.attIds}`,
    method: 'POST',
    data: transData(data),
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
  })
}
// 月度评估(分公司） -- 详情
export const monthlyEvaluationDetails = (params) => {
  return request({
    url: '/riskcontrol/monthlyEvaluation/monthlyEvaluationDetails',
    method: 'POST',
    data: transData(params),
  })
}
// 月度评估(分公司） -- 附件
export const getMonthlyEvaluationAttInfo = (params) => {
  return request({
    url: '/riskcontrol/monthlyEvaluation/getMonthlyEvaluationAttInfo',
    method: 'POST',
    data: transData(params),
  })
}
// 月度评估(分公司） -- 删除
export const monthlyEvaluationADelete = (params) => {
  return request({
    url: '/riskcontrol/monthlyEvaluation/monthlyEvaluationADelete',
    method: 'POST',
    data: transData(params),

  })
}
// 月度评估(分公司） -- 上报
// export const monthlyEvaluationSubmit = (params) => {
// 	return request({
// 		url: '/riskcontrol/monthlyEvaluation/monthlyEvaluationSubmit' + `?id=${params.id}`,
// 		method: 'POST',
// 		data: transData(params),
// 		headers: {
// 			'Content-Type': 'application/json;charset=UTF-8',
// 		},
// 	})
// }
// 风险评估汇总表
export const queryRiskAssessmentSummaryList = (params) => {
  return request({
    url: '/riskcontrol/monthlyEvaluation/queryRiskAssessmentSummaryList',
    method: 'POST',
    params,

  })
}
// 风险监督改进列表
export const riskImprovementList = (params) => {
  return request({
    url: '/riskcontrol/improvement/riskImprovementList',
    method: 'POST',
    params: transData(params),
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
  })
}
// 风险监督改进详情
export const riskImprovementDetailsList = (params) => {
  return request({
    url: '/riskcontrol/improvementDetails/riskImprovementDetailsList' + `?pageNumber=${params.pageNumber}&pageSize=${params.pageSize}&riskImplementID=${params.riskImplementID}`,
    method: 'POST',
    data: transData(params),
  })
}
// 风险监督改进修改分数
export const updateScore = (params) => {
  return request({
    url: '/riskcontrol/improvementDetails/updateScoreNew',
    method: 'POST',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    params: transData(params),
  })
}

// 导出
export const exportList = (params) => {
  return request({
    url: '/riskcontrol/reporting/exportRiskReport',
    method: 'post',
    responseType: 'blob',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    params,
  })
}
// 导出
export const exportXiafaList = (params) => {
  return request({
    url: '/riskcontrol/implement/exportIssuedImplement',
    method: 'post',
    responseType: 'blob',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    params,
  })
}
// 导出
export const exportPGList = (params) => {
  return request({
    url: '/riskcontrol/monthlyEvaluation/exportMonthlyEvaluation',
    method: 'post',
    responseType: 'blob',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    params,
  })
}
// 导出
export const exportZDFXYDList = (params) => {
  return request({
    url: '/riskcontrol/implement/exportIssuedImplement',
    method: 'post',
    responseType: 'blob',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    params,
  })
}
// 导出
export const exportFXPGHZList = (params) => {
  return request({
    url: '/riskcontrol/monthlyEvaluation/exportMonthlyEvaluation',
    method: 'post',
    responseType: 'blob',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    params,
  })
}
// 导出
export const exportFXJDGJList = (params) => {
  return request({
    url: '/riskcontrol/improvement/exportRiskImprovementList',
    method: 'post',
    responseType: 'blob',
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded',
    },
    data: require('qs').stringify(params),
  })
}
// 导出
export const exportFXJDGJDetailList = (params) => {
  return request({
    url: '/riskcontrol/improvementDetails/exportRiskReport',
    method: 'post',
    responseType: 'blob',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    params,
  })
}

//重大风险填报导出
export const exportIssuedImplementSummary = (params) => {
  return request({
    url: '/riskcontrol/implement/exportIssuedImplementSummary',
    method: 'post',
    responseType: 'blob',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    params,
  })
}

//重大风险上报
export const reportToLeader = (params) => {
  return request({
    url: '/riskcontrol/implement/reportToLeader',
    method: 'POST',
    data: transData(params),

  })
}
//重大风险新增修改
export const issuedSaveOrUpdate = (params) => {
  return request({
    url: '/riskcontrol/implement/issuedSaveOrUpdate',
    method: 'POST',
    data: transData(params),
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
  })
}
// 重大风险 -- 删除
export const issuedImplementDelete = (params) => {
  return request({
    url: '/riskcontrol/implement/issuedImplementDelete',
    method: 'POST',
    data: transData(params),
  })
}
//重大风险汇总列表
export const issuedImplementSummary = (params) => {
  return request({
    url: '/riskcontrol/implement/issuedImplementSummary',

    method: 'POST',
    params: transData(params),
  })
}

//月度评估列表
export const queryAssessedRiskList = (params) => {
  return request({
    url: '/riskcontrol/monthlyEvaluation/queryAssessedRiskList',
    method: 'POST',
    params: transData(params),
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
  })
}
//月度评估 -- 上报
export const monthlyEvaluationSubmit = (params) => {
  return request({
    url: '/riskcontrol/monthlyEvaluation/monthlyEvaluationSubmit',
    method: 'POST',
    params: transData(params),
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
  })
}

//月度评估导出
export const exportRiskEvaluated = (params) => {
  return request({
    url: '/riskcontrol/monthlyEvaluation/exportRiskEvaluated',
    method: 'post',
    responseType: 'blob',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    params,
  })
}
//月度评估汇总导出
export const exportMonthlyEvaluationSummary = (params) => {
  return request({
    url: '/riskcontrol/monthlyEvaluation/exportMonthlyEvaluationSummary',
    method: 'post',
    responseType: 'blob',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    params,
  })
}

//重大风险填报导出(新)
export const exportIssuedImplementSummaryOrder = (params) => {
  return request({
    url: '/riskcontrol/implement/exportIssuedImplementSummaryOrder',
    method: 'post',
    responseType: 'blob',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    params,
  })
}
//重大风险汇总列表新
export const issuedImplementSummaryOrder = (params) => {
  return request({
    url: '/riskcontrol/implement/issuedImplementSummaryOrder',

    method: 'POST',
    params: transData(params),
  })
}
