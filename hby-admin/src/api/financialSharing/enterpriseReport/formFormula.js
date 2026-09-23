import request from '@/utils/request'

/**
 * 查询表单公式列表
 */
export function getFormFormulaList(data) {
  return request({
    url: '/cwgxAi/enterpriseReport/formFormula/getList',
    method: 'post',
    data
  })
}

/**
 * 查询表单公式详情
 */
export function getFormFormulaDetail(formulaId) {
  return request({
    url: '/cwgxAi/enterpriseReport/formFormula/detail',
    method: 'post',
    data: { formulaId }
  })
}

/**
 * 保存表单公式
 */
export function saveFormFormula(data) {
  return request({
    url: '/cwgxAi/enterpriseReport/formFormula/save',
    method: 'post',
    data
  })
}

/**
 * 删除表单公式
 */
export function deleteFormFormula(formulaId) {
  return request({
    url: '/cwgxAi/enterpriseReport/formFormula/delete',
    method: 'post',
    data: { formulaId }
  })
}

/**
 * 根据模板ID查询表单公式列表
 */
export function getFormFormulaListByTemplateId(templateId) {
  return request({
    url: '/cwgxAi/enterpriseReport/formFormula/getListByTemplateId',
    method: 'post',
    data: { templateId }
  })
}

/**
 * 批量删除表单公式
 */
export function batchDeleteFormFormula(formulaIds) {
  return request({
    url: '/cwgxAi/enterpriseReport/formFormula/batchDelete',
    method: 'post',
    data: { formulaIds }
  })
}

