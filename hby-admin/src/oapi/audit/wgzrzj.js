import request from '@/utils/request'
import { transData } from '@/utils/requestData'


//分类处置--列表
export const flczyjList = (params) => request({
	url: `/oiaudit/wgzrzj/flczyjList`,
	method: 'get',
	params,
})
//分类处置--详情
export const flczyjDetail = (params) => request({
	url: `/oiaudit/wgzrzj/flczyjDetail`,
	method: 'get',
	params: transData(params),
})
//分类处置--删除
export const flczyjDelete = (params) => request({
	url: `/oiaudit/wgzrzj/flczyjDelete`,
	method: 'post',
	data: transData(params),
})
//分类处置--新增编辑
export const flczyjSave = (params) => request({
	url: `/oiaudit/wgzrzj/flczyjSave`,
	method: 'post',
	data: transData(params),
})
//分类处置--附件删除
export const deleteReportFile = (params) => request({
	url: `/oiaudit/wgzrzj/flczyjFileDelete`,
	method: 'post',
	data: transData(params),
})
//分类处置--附件列表
export const flczyjFileList = (params) => request({
	url: `/oiaudit/wgzrzj/flczyjFileList`,
	method: 'get',
	params: transData(params),
})



//问题核查--列表
export const wthcList = (params) => request({
	url: `/oiaudit/wgzrzj/wthcList`,
	method: 'get',
	params,
})
//问题核查--详情
export const wthcDetail = (params) => request({
	url: `/oiaudit/wgzrzj/wthcDetail`,
	method: 'get',
	params: transData(params),
})
//问题核查--删除
export const wthcDelete = (params) => request({
	url: `/oiaudit/wgzrzj/wthcDelete`,
	method: 'post',
	data: transData(params),
})
//问题核查--新增编辑
export const wthcSave = (params) => request({
	url: `/oiaudit/wgzrzj/wthcSave`,
	method: 'post',
	data: transData(params),
})
// //问题核查--附件删除
// export const deleteReportFile = (params) => request({
// 	url: `/oiaudit/wgzrzj/flczyjFileDelete`,
// 	method: 'post',
// 	data: transData(params),
// })
//问题核查--附件列表
export const wthcFileList = (params) => request({
	url: `/oiaudit/wgzrzj/wthcFileList`,
	method: 'get',
	params: transData(params),
})



//问题定责--列表
export const wtdzList = (params) => request({
	url: `/oiaudit/wgzrzj/wtdzList`,
	method: 'get',
	params,
})
//问题定责--详情
export const wtdzDetail = (params) => request({
	url: `/oiaudit/wgzrzj/wtdzDetail`,
	method: 'get',
	params: transData(params),
})
//问题定责--删除
export const wtdzDelete = (params) => request({
	url: `/oiaudit/wgzrzj/wtdzDelete`,
	method: 'post',
	data: transData(params),
})
//问题定责--新增编辑
export const wtdzSave = (params) => request({
	url: `/oiaudit/wgzrzj/wtdzSave`,
	method: 'post',
	data: transData(params),
})
// //问题定责--附件删除
// export const deleteReportFile = (params) => request({
// 	url: `/oiaudit/wgzrzj/flczyjFileDelete`,
// 	method: 'post',
// 	data: transData(params),
// })
//问题定责--附件列表
export const wtdzFileList = (params) => request({
	url: `/oiaudit/wgzrzj/wtdzFileList`,
	method: 'get',
	params: transData(params),
})
//问题定责--下发
export const wtdzIssued = (params) => request({
	url: `/oiaudit/wgzrzj/wtdzIssued`,
	method: 'get',
	params: transData(params),
})




//移送结果文书--列表
export const ysjgwsList = (params) => request({
	url: `/oiaudit/wgzrzj/ysjgwsList`,
	method: 'get',
	params,
})
//移送结果文书--详情
export const ysjgwsDetail = (params) => request({
	url: `/oiaudit/wgzrzj/ysjgwsDetail`,
	method: 'get',
	params: transData(params),
})
//移送结果文书--删除
export const ysjgwsDelete = (params) => request({
	url: `/oiaudit/wgzrzj/ysjgwsDelete`,
	method: 'post',
	data: transData(params),
})
//移送结果文书--新增编辑
export const ysjgwsSave = (params) => request({
	url: `/oiaudit/wgzrzj/ysjgwsSave`,
	method: 'post',
	data: transData(params),
})
// //移送结果文书--附件删除
// export const deleteReportFile = (params) => request({
// 	url: `/oiaudit/wgzrzj/flczyjFileDelete`,
// 	method: 'post',
// 	data: transData(params),
// })
//移送结果文书--附件列表
export const ysjgwsFileList = (params) => request({
	url: `/oiaudit/wgzrzj/ysjgwsFileList`,
	method: 'get',
	params: transData(params),
})




//移送台账--列表
export const ystzList = (params) => request({
	url: `/oiaudit/wgzrzj/ystzList`,
	method: 'get',
	params,
})
//移送台账--详情
export const ystzDetail = (params) => request({
	url: `/oiaudit/wgzrzj/ystzDetail`,
	method: 'get',
	params: transData(params),
})
//移送台账--删除
export const ystzDelete = (params) => request({
	url: `/oiaudit/wgzrzj/ystzDelete`,
	method: 'post',
	data: transData(params),
})
//移送台账--新增编辑
export const ystzSave = (params) => request({
	url: `/oiaudit/wgzrzj/ystzSave`,
	method: 'post',
	data: transData(params),
})
// //移送台账--附件删除
// export const deleteReportFile = (params) => request({
// 	url: `/oiaudit/wgzrzj/flczyjFileDelete`,
// 	method: 'post',
// 	data: transData(params),
// })
//移送台账--附件列表
export const ystzFileList = (params) => request({
	url: `/oiaudit/wgzrzj/ystzFileList`,
	method: 'get',
	params: transData(params),
})

// 问题线索受理--列表
export const wtslList = (params) => request({
	url: `/oiaudit/wgzrzj/wtslList`,
	method: 'get',
	params: transData(params),
})
// 问题线索受理--删除
export const wtslDelete = (params) => request({
	url: `/oiaudit/wgzrzj/wtslDelete`,
	method: 'post',
	data: transData(params),
})

// 问题线索受理--保存
export const wtslSave = (params) => request({
	url: `/oiaudit/wgzrzj/wtslSave`,
	method: 'post',
	data: transData(params),
})

// 问题线索受理--附件列表
export const wtslFileList = (params) => request({
	url: `/oiaudit/wgzrzj/wtslFileList`,
	method: 'get',
	params: transData(params),
})
// 问题线索受理--附件删除
export const wtslFileDelete = (params) => request({
	url: `/oiaudit/wgzrzj/wtslFileDelete`,
	method: 'post',
	data: transData(params),
})
// 问题线索受理--详情
export const wtslDetail = (params) => request({
	url: `/oiaudit/wgzrzj/wtslDetail`,
	method: 'get',
	params: transData(params),
})

//处理结果
// 处理结果--列表
export const cljgList = (params) => request({
	url: `/oiaudit/wgzrzj/cljgList`,
	method: 'get',
	params: transData(params),
})
// 处理结果--删除
export const cljgDelete = (params) => request({
	url: `/oiaudit/wgzrzj/cljgDelete`,
	method: 'post',
	data: transData(params),
})
// 处理结果--保存
export const cljgSave = (params) => request({
	url: `/oiaudit/wgzrzj/cljgSave`,
	method: 'post',
	data: transData(params),
})

// 处理结果--附件列表
export const cljgFileList = (params) => request({
	url: `/oiaudit/wgzrzj/cljgFileList`,
	method: 'get',
	params: transData(params),
})
// 处理结果--附件删除
export const cljgFileDelete = (params) => request({
	url: `/oiaudit/wgzrzj/cljgFileDelete`,
	method: 'get',
	params: transData(params),
})
// 处理结果--详情
export const cljgDetail = (params) => request({
	url: `/oiaudit/wgzrzj/cljgDetail`,
	method: 'get',
	params: transData(params),
})