import request from '@/utils/request'
import { transData } from '@/utils/requestData'


//违规追责列表查询
export function wgzzList(params) {
  return request({
    url: '/audit/wgzz/projectTeam/wgzzList',
    method: 'get',
    params: transData(params),
  })
}

//违规追责新增/修改
export function wgzzAdd(params) {
  return request({
    url: '/audit/wgzz/projectTeam/addwgzz',
    method: 'post',
    data: transData(params),
  })
}

//违规追责删除
export function deletewgzz(params) {
  return request({
    url: '/audit/wgzz/projectTeam/deletewgzz',
    method: 'post',
    data: transData(params),
  })
}

//违规追责列表AND违规经营投资问题线索管理台账详情
export function wgzzXQList(params) {
  return request({
    url: '/audit/wgzz/projectTeam/wgzzXQList',
    method: 'get',
    params: transData(params),
  })
}

//违规经营投资问题线索管理台账列表查询
export function wgzzArreyByList(params) {
  return request({
    url: '/audit/wgzz/projectTeam/wgzzArreyByList',
    method: 'get',
    params: transData(params),
  })
}

//违规经营投资问题线索管理台账新增/修改
export function saveupdatewgzz(params) {
  return request({
    url: '/audit/wgzz/projectTeam/saveupdatewgzz',
    method: 'post',
    data: transData(params),
  })
}

//违规经营投资问题线索管理台账删除
export function removewgzz(params) {
  return request({
    url: '/audit/wgzz/projectTeam/removewgzz',
    method: 'get',
    data: transData(params),
  })
}


//违规核实列表
export function wghsList(params) {
  return request({
    url: '/audit/wghs/projectTeam/wghsList',
    method: 'get',
    params: transData(params),
  })
}

//违规核实列表详情
export function wghsXQList(params) {
  return request({
    url: '/audit/wghs/projectTeam/wghsXQList',
    method: 'get',
    params: transData(params),
  })
}

//违规核实新增/修改
export function wghsSave(params) {
  return request({
    url: '/audit/wghs/projectTeam/wghsSave',
    method: 'post',
    data: transData(params),
  })
}

//违规核实删除
export function wghsRemove(params) {
  return request({
    url: '/audit/wghs/projectTeam/wghsRemove',
    method: 'post',
    data: transData(params),
  })
}

//审核报告附件列表
export function wghsFileList(params) {
  return request({
    url: '/audit/wgzz/projectTeam/wgzz_file_list',
    method: 'get',
    params: transData(params),
  })
}

// 违规核实附件删除
export function wghsRemoveFile(params) {
  return request({
    url: '/audit/wghs/projectTeam/wghsRemovefilue',
    method: 'post',
    params: transData(params),
  })
}

//审核报告列表
export function shbgList(params) {
  return request({
    url: '/audit/wghs/projectTeam/shbgList',
    method: 'get',
    params: transData(params),
  })
}

//审核报告列表详情
export function shbgXQList(params) {
  return request({
    url: '/audit/wghs/projectTeam/shbgXQList',
    method: 'get',
    params: transData(params),
  })
}

//审核报告新增/修改
export function shbgSave(params) {
  return request({
    url: '/audit/wghs/projectTeam/shbgSave',
    method: 'post',
    data: transData(params),
  })
}

//审核报告删除
export function shbgRemove(params) {
  return request({
    url: '/audit/wghs/projectTeam/shbgRemove',
    method: 'post',
    data: transData(params),
  })
}


//审核报告附件列表
export function wgbgFileList(params) {
  return request({
    url: '/audit/wghs/projectTeam/wgbg_file_list',
    method: 'get',
    params: transData(params),
  })
}
// 线索编号
export function wgzzExtList(params) {
  return request({
    url: '/audit/wgzz/projectTeam/wgzzExtList ',
    method: 'get',
    params: transData(params),
  })
}
// 线索编号
export function wgzzExtYsList(params) {
  return request({
    url: '/audit/wgzz/projectTeam/wgzzYsList',
    method: 'get',
    params: transData(params),
  })
}

//审核报告附件列表
export function getwghcFileList(params) {
  return request({
    url: '/audit/wghs/projectTeam/wghs_file_list',
    method: 'get',
    params: transData(params),
  })
}
//审核报告附件列表
export function wgbgFileListNew(params) {
  return request({
    url: '/audit/wghs/projectTeam/wghs_file_list',
    method: 'get',
    params: transData(params),
  })
}

// 审核报告附件删除
export function shbgRemovefilue(params) {
  return request({
    url: '/audit/wghs/projectTeam/shbgRemovefilue',
    method: 'post',
    data: transData(params),
  })
}
// 审核报告附件删除
export function shbgRemovefilueNew(params) {
  return request({
    url: '/audit/wghs/projectTeam/deleteatthyfile',
    method: 'post',
    data: transData(params),
  })
}

// 审核核查 详细
export function wghcbgDetail(params) {
  return request({
    url: '/audit/wghs/projectTeam/wghcbgDetail',
    method: 'get',
    params: transData(params),
  })
}



// 审核报告编号和内容查询
export function wghsListBY(params) {
  return request({
    url: '/audit/wghs/projectTeam/wghsListBY',
    method: 'get',
    params: transData(params),
  })
}
// 审核报告编号和内容查询
export function wghsListBYNew(params) {
  return request({
    url: '/audit/wgzz/projectTeam/wgzzList',
    method: 'get',
    params: transData(params),
  })
}

//违规核查列表
export function wghcList(params) {
  return request({
    url: '/audit/wgjyys/projectTeam/wgjyysList',
    method: 'get',
    params: transData(params),
  })
}

//违规核查列表详情
export function wghcXQList(params) {
  return request({
    url: '/audit/wghc/projectTeam/wghcXQList',
    method: 'get',
    params: transData(params),
  })
}


//违规核查报告列表
export function wghcbgList(params) {
  return request({
    url: '/audit/wghcbg/projectTeam/wghcbgList',
    method: 'get',
    params: transData(params),
  })
}

//违规核查报告新增/修改
export function wghcbgSave(params) {
  return request({
    url: '/audit/wghcbg/projectTeam/wghcbgSave',
    method: 'post',
    data: transData(params),
  })
}

//违规核查报告删除
export function wghsBgRemove(params) {
  return request({
    url: '/audit/wghcbg/projectTeam/wghsBgRemove',
    method: 'post',
    data: transData(params),
  })
}

//违规经营移送列表
export function wgjyysList(params) {
  return request({
    url: '/audit/wgjyys/projectTeam/wgjyysList',
    method: 'get',
    params: transData(params),
  })
}

//违规核查报告新增/修改
export function wgjyysSave(params) {
  return request({
    url: '/audit/wgjyys/projectTeam/wgjyysSave',
    method: 'post',
    data: transData(params),
  })
}


//违规经营移送删除
export function wgjyysRemove(params) {
  return request({
    url: '/audit/wgjyys/projectTeam/wgjyysRemove',
    method: 'post',
    data: transData(params),
  })
}

//违规经营移送删除
export function wghcJs(params) {
  return request({
    url: '/audit/wghc/projectTeam/wghcJs',
    method: 'post',
    data: transData(params),
  })
}
//上传决策文件附件列表
export function getSHBGFileList(params) {
  return request({
    url: '/audit/wghs/projectTeam/gethyfilelist',
    method: 'get',
    params: transData(params),
  })
}
//移送函线索编号
export function yshListBYNew(params) {
  return request({
    url: '/audit/wghs/projectTeam/wghsListBYyj',
    method: 'get',
    params: transData(params),
  })
}
//移送函线索编号
export function getwghcDetail(params) {
  return request({
    url: '/audit/wgjyys/projectTeam/getByidDetail',
    method: 'get',
    params: transData(params),
  })
}
//移送函线索编号
export function wghcDelete(params) {
  return request({
    url: '/audit/wgjyys/projectTeam/wgjyysRemove',
    method: 'post',
    data: transData(params),
  })
}




