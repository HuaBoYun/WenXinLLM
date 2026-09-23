import request from '@/utils/request'
import { transData } from '@/utils/requestData'
import qs from 'qs'

/**
 * 获取公司树
 * @param data
 * @returns {AxiosPromise}
 */
export const getAllCompanyTree = (data) => {
  return request({
    url: '/setting/redisorg/getAllCompanyTree',
    method: 'get',
    params: data,
  })
}

/**
 * 获取部门
 * @param data
 * @returns {AxiosPromise}
 */
export const getDetpList = (data) => {
  return request({
    url: '/setting/redisorg/getDetpList',
    method: 'get',
    params: data,
  })
}

/**
 * @description: 机构管理-公司管理-列表+查询
 * @param {*} data
 * @return {*}
 */
export function orgListorg(data) {
  return request({
    url: '/setting/org/listorg',
    method: 'post',
    data: transData(data),
  })
}
/**
 * @description: 机构管理-公司管理-新建
 * @param {*} data
 * @return {*}
 */
export function orgSaveorg(data) {
  return request({
    url: '/setting/org/saveorg',
    method: 'post',
    data: transData(data),
  })
}
/**
 * @description: 机构管理-公司管理-启用禁用
 * @param {*} data
 * @return {*}
 */
export function orgGsdel(data) {
  return request({
    url: '/setting/org/gsdel',
    method: 'post',
    data: transData(data),
  })
}
/**
 * @description: 机构管理-公司管理-左侧列表
 * @param {*} data
 * @return {*}
 */
export function qxsdLeftorg(data) {
  return request({
    url: '/setting/qxsd/leftorg',
    method: 'post',
    data: transData(data),
  })
}
/**
 * @description: 机构管理-公司管理-删除/机构管理-组织架构-删除
 * @param {*} data
 * @return {*}
 */
export function orgisQY(data) {
  return request({
    url: '/setting/org/isQY',
    method: 'post',
    data: transData(data),
  })
}
/**
 * @description: 组织架构列表
 * @param {*} data
 * @return {*}
 */
export function orgList(data) {
  return request({
    url: '/setting/org/list',
    method: 'post',
    data: transData(data),
  })
}

/**
 * @description: 组织架构详情
 * @param {*} data
 * @return {*}
 */
export function orgDetail(data) {
  return request({
    url: '/setting/org/getDeptInfoDetial',
    method: 'get',
    params: data,
  })
}

/**
 * @description: 组织架构新建
 * @param {*} data
 * @return {*}
 */
export function orgSave(data) {
  return request({
    url: '/setting/org/save',
    method: 'post',
    data: transData(data),
  })
}
/**
 * @description: 行业架构-列表页
 * @param {*} data
 * @return {*}
 */
export function orgHylist(data) {
  return request({
    url: '/setting/org/inihy_list',
    method: 'post',
    data: transData(data),
  })
}
/**
 * @description: 行业架构-新建
 * @param {*} data
 * @return {*}
 */
export function orgHysave(data) {
  return request({
    url: '/setting/org/hy_save',
    method: 'post',
    data: transData(data),
  })
}
/**
 * @description: 行业架构-取消授权列表
 * @param {*} data
 * @return {*}
 */
export function getAuthorizeDelList(data) {
  return request({
    url: '/setting/authorize_to_del',
    method: 'post',
    data,
  })
}

/**
 * @description: 行业架构-确定取消授权
 * @param {*} data
 * @return {*}
 */
export function authorizeDel(data) {
  return request({
    url: '/setting/authorize_del',
    method: 'post',
    params: data,
    paramsSerializer: (params) => {
      return qs.stringify(params, { indices: false })
    },
  })
}

/**
 * @description: 行业架构-删除
 * @param {*} data
 * @return {*}
 */
export function orgHydel(data) {
  return request({
    url: '/setting/org/hy_del',
    method: 'post',
    data: transData(data),
  })
}
/**
 * @description: 行业架构-授权分配
 * @param {*} data
 * @return {*}
 */
export function organAccreditByHY(data) {
  return request({
    url: '/setting/organAccreditByHY',
    method: 'post',
    data: transData(data),
  })
}
/**
 * @description: 行业架构-授权分配确定
 * @param {*} data
 * @return {*}
 */
export function addhyaccredit(data) {
  return request({
    url: '/setting/add_hy_accredit',
    method: 'post',
    data,
  })
}

/**
 * @description: 行业架构-授权分配确定
 * @param {*} data
 * @return {*}
 */
export function orghy_left(data) {
  return request({
    url: '/setting/org/hy_left',
    method: 'post',
    data: transData(data),
  })
}
/**
 * @description: 设置模块-菜单设定-列表
 * @param {*} data
 * @return {*}
 */
export function qxsdCdqx(data) {
  return request({
    url: '/setting/qxsd/listorg',
    method: 'post',
    data: transData(data),
  })
}
/**
 * @description: 设置模块-菜单设定-授权列表
 * @param {*} data
 * @return {*}
 */
export function qxSaveCD(data) {
  return request({
    url: '/setting/qxsd/mgnsqorg',
    method: 'post',
    data: transData(data),
  })
}
/**
 * @description: 设置模块-菜单设定-授权列表确定
 * @param {*} data
 * @return {*}
 */
export function qxSaveCD2(data) {
  return request({
    url: '/setting/qxsd/qx_saveCD',
    method: 'post',
    data: transData(data),
  })
}
/**
 * @description: 机构管理-公司管理-左侧列表
 * @param {*} data
 * @return {*}
 */
export function findOrganization(data) {
  return request({
    url: '/setting/redisorg/findOrganizationByJTTreeAllGS',
    method: 'post',
    data: transData(data),
  })
}
export function findOrganizationData(data) {
  return request({
    url: '/setting/redisorg/getRootOrganizationTree',
    method: 'post',
    data: transData(data),
  })
}

/**
 * @description: 设置模块-组织架构-左侧列表
 * @param {*} data
 * @return {*}
 */
export function zgjkLeft(data) {
  return request({
    url: '/setting/baseInfo/findOrganizationByTreeAllbm',
    method: 'post',
    data: transData(data),
  })
}

/**
 * @description: 顶部 - 切换公司
 * @param {*} data
 * @return {*}
 */
export function hbOrgBuild(data) {
  return request({
    url: '/setting/qxsd/hbOrgBuild',
    method: 'post',
    data: transData(data),
  })
}

/**
 * 获取公司授权
 * @param data
 * @returns {AxiosPromise}
 */
export const getCompanyRightList = (data) => {
  return request({
    url: '/setting/right/getCompanyRightList',
    method: 'get',
    params: data,
  })
}
/**
 * 保存公司授权
 * @param data
 * @returns {AxiosPromise}
 */
export function grantCompanyRight(data) {
  return request({
    url: '/setting/right/grantCompanyRight',
    method: 'post',
    data: transData(data),
  })
}

export function getTreeData(data) {
  return request({
    url: '/audit/nbsjworkSpace/sjmx/getTree',
    method: 'get',
    params: transData(data),
  })
}

export function saveInfo(data) {
  return request({
    url: '/audit/nbsjworkSpace/sjmx/typeSaveOrupdate',
    method: 'post',
    data: transData(data),
  })
}

export function getDefaultTreeInfo(data) {
  return request({
    url: '/audit/nbsjworkSpace/sjmx/gettypeDetail',
    method: 'get',
    params: transData(data),
  })
}

export function deleteTree(data) {
  return request({
    url: '/audit/nbsjworkSpace/sjmx/deletetype',
    method: 'post',
    data: transData(data),
  })
}

export function getSJMXKList(data) {
  return request({
    url: '/audit/nbsjworkSpace/sjmx/getSteplist',
    method: 'get',
    params: transData(data),
  })
}

export function createSJMXKData(data) {
  return request({
    url: '/audit/nbsjworkSpace/sjmx/stepsaveOrUpdate',
    method: 'post',
    data: transData(data),
  })
}

export function deleteSJMXKInfo(data) {
  return request({
    url: '/audit/nbsjworkSpace/sjmx/deletestep',
    method: 'post',
    data: transData(data),
  })
}

export function getSJMXKDetailInfo(data) {
  return request({
    url: '/audit/nbsjworkSpace/sjmx/getStepDetail',
    method: 'get',
    params: transData(data),
  })
}

export function getBaseSelectList(data) {
  return request({
    url: '/setting/sjmx/getlist',
    method: 'post',
    data: transData(data),
  })
}

export function getSQLList(data) {
  return request({
    url: '/audit/nbsjworkSpace/sjmx/sqlyz',
    method: 'post',
    data: transData(data),
  })
}

export function getResultData(data) {
  return request({
    url: '/audit/nbsjworkSpace/sjmx/zxReslut',
    method: 'get',
    params: transData(data),
  })
}

export function executeSql(data) {
  return request({
    url: '/audit/nbsjworkSpace/sjmx/zxsql',
    method: 'get',
    params: transData(data),
  })
}

export function getDataBase(data) {
  return request({
    url: '/audit/nbsjworkSpace/sjmx/database/table/get-list',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: JSON.stringify(transData(data)),
  })
}
export function getSQLData(data) {
  return request({
    url: '/audit/nbsjworkSpace/sjmx/database/table/field/get-list',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: JSON.stringify(transData(data)),
  })
}

export function getDataSourceList(params) {
  return request({
    url: '/audit/model/data/source/getList',
    method: 'POST',
    data: transData(params),
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
  })
}

export function editDataSource(params) {
  return request({
    url: '/audit/model/data/source/saveOrUpdate',
    method: 'POST',
    data: transData(params),
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
  })
}

export function getDataSourceDefaultInfo(data) {
  return request({
    url: `/audit/model/data/source/${data.id}`,
    method: 'GET',
    params: transData(data),
  })
}

export function DataSourceDelete(params) {
  return request({
    url: `/audit/model/data/source/${params.id}`,
    method: 'delete',
  })
}

//新增

export function getExcelExportList(params) {
  return request({
    url: '/audit/model/excel/getList',
    method: 'POST',
    data: transData(params),
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
  })
}

export function editExcelExport(params) {
  return request({
    url: '/audit/model/excel/saveOrUpdate',
    method: 'POST',
    data: transData(params),
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
  })
}

export function getExcelExportDefaultInfo(data) {
  return request({
    url: `/audit/model/excel/${data.id}`,
    method: 'GET',
    params: transData(data),
  })
}

export function ExcelExportDelete(params) {
  return request({
    url: `/audit/model/excel/${params.id}`,
    method: 'delete',
  })
}

export function getExcelDetailList(params) {
  return request({
    url: '/audit/model/excel/ext/getList',
    method: 'POST',
    data: transData(params),
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
  })
}
export function getExcelFiledList(params) {
  return request({
    url: '/audit/model/excel/table/getList',
    method: 'POST',
    data: transData(params),
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
  })
}
export function editExcelField(params) {
  return request({
    url: '/audit/model/excel/table/saveOrUpdate',
    method: 'POST',
    data: transData(params),
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
  })
}
export function checkName(params) {
  return request({
    url: '/audit/model/excel/check',
    method: 'POST',
    data: transData(params),
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
  })
}
export function makeB(params) {
  return request({
    url: '/audit/model/excel/table/generatingTable',
    method: 'POST',
    data: transData(params),
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
  })
}
export function exportDataBase(params) {
  return request({
    url: '/audit/nbsjworkSpace/sjmx/sqljgexport',
    method: 'post',
    data: transData(params),
    responseType: 'blob',
  })
}
export function getModelNo(params) {
  return request({
    url: '/audit/nbsjworkSpace/sjmx/gaincode',
    method: 'get',
    params: transData(params),
  })
}
export function LinkTest(params) {
  return request({
    url: `/audit/nbsjworkSpace/sjmx/check/connection`,
    method: 'post',
    data: transData(params),
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
  })
}
export function exportExcel(params) {
  return request({
    url: '/audit/nbsjworkSpace/sjmx/exportsql',
    method: 'post',
    data: transData(params),
    responseType: 'blob',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
  })
}
