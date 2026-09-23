/**
 * 把后端返回的 Map 结果集 key 归一化为前端模板使用的 camelCase。
 *
 * 背景：达梦/Oracle 默认把不加引号的标识符全部转大写，
 * `SELECT scheme_id AS schemeId` 实际返回列名是 `SCHEMEID`，
 * 导致前端模板按 `schemeId` 取不到值。
 *
 * 三种 key 形态兼容：
 *   1) ALLCAPS（达梦默认）       SCHEMECODE        -> 字典 / 字典未命中则 toLowerCase
 *   2) snake / SNAKE_CASE        SCHEME_CODE       -> 按下划线分词
 *   3) 已是 camelCase            schemeCode        -> 原样保留
 *
 * 字典优先于规则，确保 ALLCAPS 形态一定能还原到精确的 camelCase。
 *
 * 设计原则：单向、幂等、保守。已是 camelCase 的输入再次归一化结果不变。
 */

// 财务共享 - 成本估算模块所有页面用到的字段字典
// 新增字段时只动这里一处
const ALLCAPS_TO_CAMEL = {
  // 主键 / 公共字段
  ID: 'id',
  TENANTID: 'tenantId',
  BOOKID: 'bookId',
  REMARK: 'remark',
  VERSION: 'version',
  ISENABLED: 'isEnabled',
  CREATEBY: 'createBy',
  CREATETIME: 'createTime',
  UPDATEBY: 'updateBy',
  UPDATETIME: 'updateTime',
  STATUS: 'status',

  // 估算方案
  SCHEMEID: 'schemeId',
  SCHEMECODE: 'schemeCode',
  SCHEMENAME: 'schemeName',
  SCHEMETYPE: 'schemeType',
  SCHEMEDESCRIPTION: 'schemeDescription',
  ESTIMATIONTYPE: 'estimationType',
  ESTIMATIONMETHOD: 'estimationMethod',
  APPLICABLESCOPE: 'applicableScope',
  EFFECTIVEDATE: 'effectiveDate',
  EXPIRYDATE: 'expiryDate',
  DESCRIPTION: 'description',

  // 成本模型
  MODELID: 'modelId',
  MODELCODE: 'modelCode',
  MODELNAME: 'modelName',
  MODELTYPE: 'modelType',
  MODELDESCRIPTION: 'modelDescription',
  MODELFORMULA: 'modelFormula',
  FORMULA: 'formula',
  ACCURACY: 'accuracy',
  ACCURACYSCORE: 'accuracyScore',
  ACCURACYRATE: 'accuracyRate',
  USAGECOUNT: 'usageCount',
  APPLICABLESCENARIO: 'applicableScenario',
  INDUSTRY: 'industry',
  ALGORITHM: 'algorithm',

  // 成本模拟
  SIMULATIONID: 'simulationId',
  SIMULATIONNO: 'simulationNo',
  SIMULATIONNAME: 'simulationName',
  SIMULATIONPERIOD: 'simulationPeriod',
  SIMULATIONQUANTITY: 'simulationQuantity',
  SIMULATIONSCENARIOS: 'simulationScenarios',
  MATERIALCOST: 'materialCost',
  LABORCOST: 'laborCost',
  OVERHEADCOST: 'overheadCost',
  TOTALCOST: 'totalCost',
  UNITCOST: 'unitCost',
  MATERIALCOSTRATE: 'materialCostRate',
  LABORCOSTRATE: 'laborCostRate',
  OVERHEADCOSTRATE: 'overheadCostRate',

  // 差异分析
  ANALYSISID: 'analysisId',
  ANALYSISNO: 'analysisNo',
  ANALYSISPERIOD: 'analysisPeriod',
  COSTCENTERID: 'costCenterId',
  COSTCENTERNAME: 'costCenterName',
  PRODUCTID: 'productId',
  PRODUCTNAME: 'productName',
  PRODUCTCATEGORYID: 'productCategoryId',
  VARIANCEAMOUNT: 'varianceAmount',
  VARIANCERATE: 'varianceRate',
  VARIANCETYPE: 'varianceType',
  VARIANCETYPES: 'varianceTypes',
  RISKLEVEL: 'riskLevel',
  RISKLEVELNAME: 'riskLevelName',
  REASON: 'reason',
  RECOMMENDATION: 'recommendation',
  OVERALLVARIANCE: 'overallVariance',
  MATERIALVARIANCE: 'materialVariance',
  LABORVARIANCE: 'laborVariance',
  OVERHEADVARIANCE: 'overheadVariance',

  // 预算编制
  BUDGETID: 'budgetId',
  BUDGETNO: 'budgetNo',
  BUDGETNAME: 'budgetName',
  BUDGETYEAR: 'budgetYear',
  BUDGETTYPE: 'budgetType',
  BUDGETPERIOD: 'budgetPeriod',
  BUDGETAMOUNT: 'budgetAmount',
  BUDGETPROJECTS: 'budgetProjects',
  TOTALBUDGET: 'totalBudget',
  TOTALAMOUNT: 'totalAmount',
  ACTUALCOST: 'actualCost',
  ACTUALAMOUNT: 'actualAmount',
  USEDAMOUNT: 'usedAmount',
  BUDGETDETAILSJSON: 'budgetDetailsJson',
  PREPARATIONID: 'preparationId',
  PREPARATIONSTATUS: 'preparationStatus',
  PREPARATIONDATE: 'preparationDate',
  PREPARERID: 'preparerId',
  PREPARERNAME: 'preparerName',
  APPROVALSTATUS: 'approvalStatus',
  APPROVALTIME: 'approvalTime',
  APPROVALCOMMENT: 'approvalComment',
  APPROVERID: 'approverId',
  APPROVERNAME: 'approverName',
  DEPARTMENTID: 'departmentId',

  // 估算报告
  REPORTID: 'reportId',
  REPORTNO: 'reportNo',
  REPORTNAME: 'reportName',
  REPORTTYPE: 'reportType',
  REPORTSUMMARY: 'reportSummary',
  REPORTPERIOD: 'reportPeriod',
  REPORTSTATUS: 'reportStatus',
  REPORTCONTENT: 'reportContent',
  GENERATIONTIME: 'generationTime',
  GENERATORID: 'generatorId',
  GENERATORNAME: 'generatorName',
  RELATEDID: 'relatedId',
  FILEPATH: 'filePath',
  DATASOURCE: 'dataSource',
  FILESIZE: 'fileSize',
  PUBLISHSTATUS: 'publishStatus',
  PUBLISHTIME: 'publishTime'
}

/**
 * 归一化单行对象的 key。
 * @param {Object} row 任意对象
 * @returns {Object} 新对象，key 全部为 camelCase（已是 camelCase 的保留原样）
 */
export function normalizeKeys(row) {
  if (!row || typeof row !== 'object' || Array.isArray(row)) return row
  const out = {}
  Object.keys(row).forEach(k => {
    let camel
    if (ALLCAPS_TO_CAMEL[k]) {
      camel = ALLCAPS_TO_CAMEL[k]
    } else if (k.indexOf('_') >= 0) {
      camel = k.toLowerCase().replace(/_([a-z0-9])/g, (_, c) => c.toUpperCase())
    } else if (/^[A-Z0-9]+$/.test(k)) {
      // 字典未命中的全大写：保守降小写（至少不丢值）
      camel = k.toLowerCase()
    } else {
      camel = k
    }
    // 已有非空值优先（防止 ALLCAPS 形态覆盖前面已正确赋值的 camelCase）
    if (out[camel] == null || out[camel] === '') {
      out[camel] = row[k]
    }
  })
  return out
}

/**
 * 归一化数组里每一行的 key。
 * @param {Array} list 任意对象数组
 * @returns {Array}
 */
export function normalizeKeysArray(list) {
  if (!Array.isArray(list)) return []
  return list.map(normalizeKeys)
}

export default { normalizeKeys, normalizeKeysArray }
