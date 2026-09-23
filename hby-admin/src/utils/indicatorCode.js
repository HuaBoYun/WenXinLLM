/**
 * 组合指标编码管理工具
 * 用于统一管理不同业务场景的指标编码
 */

// 🔥 指标编码映射表
export const INDICATOR_CODE_MAP = {
  // 风险分析类指标
  CONTRACT_RISK_ANALYSIS: '合同风险分析',
  SUPPLIER_EVALUATION: '供应商风险评估',
  PROCUREMENT_SPLIT_WARNING: '采购拆分预警',
  DEPARTMENT_FREQUENCY_WARNING: '部门申请频次预警',
  
  // 数据模型管理
  DATA_MODEL_MANAGEMENT: '数据模型管理',
  DATA_MODEL_TEST: '数据模型测试',
  
  // 评估模型
  EVALUATION_MODEL_TEST: '评估模型测试',
  
  // 默认指标
  DEFAULT_INDICATOR: '默认指标'
}

// 🔥 路径与指标编码映射
export const PATH_INDICATOR_MAP = {
  '/risk/contract': 'CONTRACT_RISK_ANALYSIS',
  '/risk/supplier': 'SUPPLIER_EVALUATION', 
  '/risk/procurement': 'PROCUREMENT_SPLIT_WARNING',
  '/risk/department': 'DEPARTMENT_FREQUENCY_WARNING',
  '/risk/mxgl/sjmxgl': 'DATA_MODEL_MANAGEMENT',
  '/risk/mxgl/pgmxgl': 'EVALUATION_MODEL_TEST'
}

/**
 * 根据当前路由获取指标编码
 * @param {Object} route Vue路由对象
 * @returns {string} 指标编码
 */
export function getIndicatorCodeByRoute(route) {
  if (!route || !route.path) {
    return 'DEFAULT_INDICATOR'
  }
  
  const currentPath = route.path
  
  // 精确匹配
  if (PATH_INDICATOR_MAP[currentPath]) {
    return PATH_INDICATOR_MAP[currentPath]
  }
  
  // 模糊匹配
  for (const [path, code] of Object.entries(PATH_INDICATOR_MAP)) {
    if (currentPath.includes(path)) {
      return code
    }
  }
  
  return 'DEFAULT_INDICATOR'
}

/**
 * 根据组件名称获取指标编码
 * @param {string} componentName 组件名称
 * @returns {string} 指标编码
 */
export function getIndicatorCodeByComponent(componentName) {
  const componentMap = {
    'ContractRiskAnalysis': 'CONTRACT_RISK_ANALYSIS',
    'SupplierEvaluation': 'SUPPLIER_EVALUATION',
    'ProcurementSplitWarning': 'PROCUREMENT_SPLIT_WARNING',
    'DepartmentFrequencyWarning': 'DEPARTMENT_FREQUENCY_WARNING',
    'DataModelManagement': 'DATA_MODEL_MANAGEMENT',
    'DataModelTest': 'DATA_MODEL_TEST',
    'EvaluationModelTest': 'EVALUATION_MODEL_TEST',
    'SqlVisualEditor': 'DATA_MODEL_MANAGEMENT'
  }
  
  return componentMap[componentName] || 'DEFAULT_INDICATOR'
}

/**
 * 获取指标编码的中文名称
 * @param {string} indicatorCode 指标编码
 * @returns {string} 中文名称
 */
export function getIndicatorName(indicatorCode) {
  return INDICATOR_CODE_MAP[indicatorCode] || '未知指标'
}

/**
 * 验证指标编码是否有效
 * @param {string} indicatorCode 指标编码
 * @returns {boolean} 是否有效
 */
export function isValidIndicatorCode(indicatorCode) {
  return Object.keys(INDICATOR_CODE_MAP).includes(indicatorCode)
}

/**
 * 生成基于时间戳的唯一指标编码
 * @param {string} prefix 前缀
 * @returns {string} 唯一指标编码
 */
export function generateUniqueIndicatorCode(prefix = 'CUSTOM') {
  const timestamp = Date.now()
  const random = Math.random().toString(36).substr(2, 5).toUpperCase()
  return `${prefix}_${timestamp}_${random}`
}

/**
 * Vue插件安装函数
 * @param {Object} Vue Vue构造函数
 */
export function install(Vue) {
  // 全局混入指标编码相关方法
  Vue.mixin({
    methods: {
      // 获取当前页面的指标编码
      getCurrentIndicatorCode() {
        return getIndicatorCodeByRoute(this.$route)
      },
      
      // 获取指标编码名称
      getIndicatorName(code) {
        return getIndicatorName(code)
      },
      
      // 验证指标编码
      isValidIndicatorCode(code) {
        return isValidIndicatorCode(code)
      }
    }
  })
  
  // 全局属性
  Vue.prototype.$indicatorCode = {
    getByRoute: getIndicatorCodeByRoute,
    getByComponent: getIndicatorCodeByComponent,
    getName: getIndicatorName,
    isValid: isValidIndicatorCode,
    generate: generateUniqueIndicatorCode,
    MAP: INDICATOR_CODE_MAP,
    PATH_MAP: PATH_INDICATOR_MAP
  }
}

// 默认导出
export default {
  install,
  getIndicatorCodeByRoute,
  getIndicatorCodeByComponent,
  getIndicatorName,
  isValidIndicatorCode,
  generateUniqueIndicatorCode,
  INDICATOR_CODE_MAP,
  PATH_INDICATOR_MAP
}
