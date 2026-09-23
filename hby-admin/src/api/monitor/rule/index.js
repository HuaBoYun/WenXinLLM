import request from '@/utils/request'
import { transData } from '@/utils/requestData'

//  监控管理
export function Rulesmgmt(params) {
  return request({
    url: 'monitor/cyber/GzglController/gzjk/rule/rulesmgmt',
    method: 'get',
    params: transData(params),
  })
}

//  监控预警
export function Solutionmgmt(params) {
  return request({
    url: 'monitor/cyber/GzyjController/gzjk/rule/solutionmgmt',
    method: 'get',
    params: transData(params),
  })
}

//  状态修改
export function SolutionUpdateStatus(params) {
  return request({
    url: 'monitor/cyber/GzyjController/gzjk/solution/solutionUpdateStatus',
    method: 'get',
    params: transData(params),
  })
}
//  结果页面
export function Resultmgmtlist(params) {
  return request({
    url: 'monitor/cyber/GzyjController/result/resultmgmtlist',
    method: 'get',
    params: transData(params),
  })
}
//  保存
export function SolutionAdd(params) {
  return request({
    url: 'monitor/cyber/GzyjController/gzjk/rule/solution_add',
    method: 'get',
    params: transData(params),
  })
}
//  保存
export function solutionDel(params) {
  return request({
    url: 'monitor/cyber/GzyjController/gzjk/rule/solution_del',
    method: 'get',
    params: transData(params),
  })
}
// 执行
export function SolutionExecute(params) {
  return request({
    url: 'monitor/cyber/GzyjController/gzjk/rule/solutionExecute',
    method: 'get',
    params: transData(params),
  })
}
// 修改
export function SolutioncheckUpdate(params) {
  return request({
    url: 'monitor/cyber/GzyjController/gzjk/rule/solutioncheckUpdate',
    method: 'get',
    params: transData(params),
  })
}
// 添加规则列表
export function RuleslistSelector(params) {
  return request({
    url: 'monitor/cyber/GzyjController/gzjk/rule/ruleslistSelector',
    method: 'get',
    params: transData(params),
  })
}

// 添加跳转
export function ToSolutionAdd(params) {
  return request({
    url: 'monitor/cyber/GzyjController/gzjk/rule/to_solution_add',
    method: 'get',
    params: transData(params),
  })
}
// 规则监控添加跳转
export function ToRuleAdd(params) {
  return request({
    url: 'monitor/cyber/GzglController/gzjk/rule/to_rule_add',
    method: 'get',
    params: transData(params),
  })
}
// 规则监控修改跳转
export function ToRuleModify(params) {
  return request({
    url: 'monitor/cyber/GzglController/gzjk/rule/to_rule_modify',
    method: 'get',
    params: transData(params),
  })
}
// 规则监控 --保存操作
export function ruleAdd(data) {
  return request({
    url: 'monitor/cyber/GzglController/gzjk/rule/rule_add',
    method: 'post',
    data: transData(data),
  })
}
// 规则监控 --保存操作
export function ruleEdit(data) {
  return request({
    url: 'monitor/cyber/GzglController/gzjk/rule/rule_modify',
    method: 'post',
    data: transData(data),
  })
}
// 规则预警--修改跳转
export function ToSolutionModify(params) {
  return request({
    url: 'monitor/cyber/GzyjController/gzjk/rule/to_solution_modify',
    method: 'get',
    params: transData(params),
  })
}
// 添加规则
export function addRule(params) {
  return request({
    url: 'monitor/cyber/GzyjController/gzjk/add_rule',
    method: 'get',
    params: transData(params),
  })
}
// 查看规则预警执行结果
export function resultMgmtyj(params) {
  return request({
    url: 'monitor/cyber/GzyjController/result/resultmgmtyj',
    method: 'get',
    params: transData(params),
  })
}
// ^^^^^^^^^^^ 预警

// rule delete
export function RuleDel(params) {
  return request({
    url: 'monitor/cyber/GzglController/gzjk/rule/rule_del',
    method: 'get',
    params: transData(params),
  })
}
// start
export function RuleExecute(params) {
  return request({
    url: 'monitor/cyber/GzglController/gzjk/rule/ruleExecute',
    method: 'get',
    params: transData(params),
  })
}
// copy to library
export function CopyExchange(params) {
  return request({
    url: 'monitor/cyber/GzglController/rule/copyExchange',
    method: 'get',
    params: transData(params),
  })
}
// action and hide
export function RuleUpdateStatus(params) {
  return request({
    url: 'monitor/cyber/GzglController/gzjk/rule/ruleUpdateStatus',
    method: 'get',
    params: transData(params),
  })
}
// rule excution result
export function Resultmgmt(params) {
  return request({
    url: 'monitor/cyber/GzglController/result/resultmgmt',
    method: 'get',
    params: transData(params),
  })
}
// 规则管理-SQL执行
export function SjmxZxsql(params) {
  return request({
    url: 'monitor/sjmx/zxsql',
    method: 'get',
    params: transData(params),
  })
}
// 规则管理-sql执行过程查询
export function SjmxZxReslut(params) {
  return request({
    url: 'monitor/sjmx/zxReslut',
    method: 'get',
    params: transData(params),
  })
}
// 规则管理-sql执行结果查询
export function SjmxGetDatelist(params) {
  return request({
    url: 'monitor/sjmx/getDatelist',
    method: 'get',
    params: transData(params),
  })
}

// modify
export function CheckUpdate(params) {
  return request({
    url: 'monitor/cyber/GzglController/gzjk/rule/checkUpdate',
    method: 'get',
    params: transData(params),
  })
}

// check
export function RuleDisp(params) {
  return request({
    url: 'monitor/cyber/GzglController/gzjk/rule/rule_disp',
    method: 'get',
    params: transData(params),
  })
}
// 规则库列表
export function hyRulesmgmt(params) {
  return request({
    url: 'monitor/cyber/GzyjRuleBaseController/rule/hy_rulesmgmt',
    method: 'get',
    params: transData(params),
  })
}

// 规则库 --保存操作
export function ruleBaseAdd(data) {
  return request({
    url: 'monitor/cyber/GzyjRuleBaseController/rule/rule_base_add',
    method: 'post',
    data: transData(data),
  })
}

// 规则库详情
export function toRuleModify(params) {
  return request({
    url: 'monitor/cyber/GzyjRuleBaseController/rule/to_rule_modify',
    method: 'get',
    params: transData(params),
  })
}

//指标库列表
export function hyRulesmgmtZB(params) {
  return request({
    url: 'monitor/cyber/ZbRuleBaseController/rule/hy_rulesmgmt',
    method: 'get',
    params: transData(params),
  })
}
//指标库 -- 保存操作
export function ruleBaseAddZB(data) {
  return request({
    url: 'monitor/cyber/ZbRuleBaseController/rule/rule_base_add',
    method: 'post',
    data: transData(data),
  })
}
export function hyRulesmgmtMX(params) {
  return request({
    url: 'monitor/cyber/MxgzRuleBaseController/rule/hy_rulesmgmt',
    method: 'get',
    params: transData(params),
  })
}

//模型库 -- 保存操作
export function ruleBaseAddMX(data) {
  return request({
    url: 'monitor/cyber/MxgzRuleBaseController/rule/rule_base_add',
    method: 'post',
    data: transData(data),
  })
}
//数据源管理列表
export function getDataSourceList(data) {
  return request({
    url: 'monitor/model/data/source/getList',
    method: 'post',
    data: transData(data),
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
  })
}
//数据源管理列表删除
export function DataSourceDelete(data) {
  return request({
    url: `monitor/model/data/source/${data.id}`,
    method: 'delete',
    params: transData(data),
  })
}
//数据源管理编辑保存
export function editDataSource(data) {
  return request({
    url: `monitor/model/data/source/saveOrUpdate`,
    method: 'post',
    data: transData(data),
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
  })
}
//数据源管理获取详情
export function getDataSourceDefaultInfo(data) {
  return request({
    url: `monitor/model/data/source/${data.id}`,
    method: 'get',
    params: transData(data),
  })
}
//数据源测试连接
export function LinkTest(data) {
  return request({
    url: `monitor/sjmx/check/connection`,
    method: 'post',
    data: transData(data),
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
  })
}
export function getDataBase(data) {
  return request({
    url: 'monitor/sjmx/database/table/get-list',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: JSON.stringify(transData(data)),
  })
}
export function getSQLData(data) {
  return request({
    url: 'monitor/sjmx/database/table/field/get-list',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: JSON.stringify(transData(data)),
  })
}
export function getSQLList(data) {
  return request({
    url: 'monitor/sjmx/sqlyz',
    method: 'get',
    params: transData(data),
  })
}
export function exportDataBase(params) {
  return request({
    url: 'monitor/sjmx/sqljgexport',
    method: 'post',
    data: transData(params),
    responseType: 'blob',
  })
}
export function exportExcel(params) {
  return request({
    url: 'monitor/sjmx/exportsql',
    method: 'post',
    data: transData(params),
    responseType: 'blob',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
  })
}
