/*
 * @Description: 财务共享 - 事项会计系统 API 统一入口
 * @Author: system
 * @Date: 2024-12-19
 */

// 基础配置管理 API
export * from './system'

// 会计规则管理 API
export * from './rules'

// 事项管理 API
export * from './matter'

// 凭证管理 API
export * from './voucher'

// 影响因素定义 API (为了向后兼容，保留原有的导出方式)
import {
  getInfluenceFactorPage,
  getInfluenceFactorById,
  getInfluenceFactorByCode,
  getInfluenceFactorsByType,
  saveInfluenceFactor,
  updateInfluenceFactor,
  deleteInfluenceFactor,
  batchDeleteInfluenceFactors,
  updateInfluenceFactorStatus,
  batchUpdateInfluenceFactorStatus,
  checkInfluenceFactorCodeExists
} from './system'

// 影响因素定义 API 对象 (向后兼容)
export const influenceFactorApi = {
  getPage: getInfluenceFactorPage,
  getById: getInfluenceFactorById,
  getByCode: getInfluenceFactorByCode,
  getByType: getInfluenceFactorsByType,
  save: saveInfluenceFactor,
  update: updateInfluenceFactor,
  delete: deleteInfluenceFactor,
  batchDelete: batchDeleteInfluenceFactors,
  updateStatus: updateInfluenceFactorStatus,
  batchUpdateStatus: batchUpdateInfluenceFactorStatus,
  checkCodeExists: checkInfluenceFactorCodeExists
}

// 基础配置 API 对象
export const systemApi = {
  // 影响因素定义
  influenceFactor: influenceFactorApi,
  
  // 会计科目配置
  accountSubject: {
    getPage: require('./system').getAccountSubjectPage,
    getTree: require('./system').getAccountSubjectTree,
    save: require('./system').saveAccountSubject,
    update: require('./system').updateAccountSubject,
    delete: require('./system').deleteAccountSubject
  },
  
  // 辅助核算项目
  auxiliaryAccounting: {
    getPage: require('./system').getAuxiliaryAccountingPage,
    save: require('./system').saveAuxiliaryAccounting,
    update: require('./system').updateAuxiliaryAccounting,
    delete: require('./system').deleteAuxiliaryAccounting
  },
  
  // 币种配置
  currency: {
    getList: require('./system').getCurrencyList,
    save: require('./system').saveCurrency,
    update: require('./system').updateCurrency,
    delete: require('./system').deleteCurrency
  }
}

// 会计规则 API 对象
export const rulesApi = {
  // 确认规则
  recognition: {
    getPage: require('./rules').getRecognitionRulePage,
    getById: require('./rules').getRecognitionRuleById,
    save: require('./rules').saveRecognitionRule,
    update: require('./rules').updateRecognitionRule,
    delete: require('./rules').deleteRecognitionRule,
    updateStatus: require('./rules').updateRecognitionRuleStatus
  },
  
  // 计量规则
  measurement: {
    getPage: require('./rules').getMeasurementRulePage,
    getById: require('./rules').getMeasurementRuleById,
    save: require('./rules').saveMeasurementRule,
    update: require('./rules').updateMeasurementRule,
    delete: require('./rules').deleteMeasurementRule
  },
  
  // 凭证规则
  voucher: {
    getPage: require('./rules').getVoucherRulePage,
    getById: require('./rules').getVoucherRuleById,
    save: require('./rules').saveVoucherRule,
    update: require('./rules').updateVoucherRule,
    delete: require('./rules').deleteVoucherRule,
    getTemplateByBusinessType: require('./rules').getVoucherTemplateByBusinessType
  },
  
  // 过账规则
  posting: {
    getPage: require('./rules').getPostingRulePage,
    getById: require('./rules').getPostingRuleById,
    save: require('./rules').savePostingRule,
    update: require('./rules').updatePostingRule,
    delete: require('./rules').deletePostingRule
  },
  
  // 规则引擎
  engine: {
    test: require('./rules').testRuleExecution,
    batchExecute: require('./rules').batchExecuteRules,
    getExecutionLog: require('./rules').getRuleExecutionLog
  }
}

// 事项管理 API 对象
export const matterApi = {
  // 事项数据管理
  data: {
    getPage: require('./matter').getMatterDataPage,
    getById: require('./matter').getMatterDataById,
    save: require('./matter').saveMatterData,
    update: require('./matter').updateMatterData,
    delete: require('./matter').deleteMatterData,
    import: require('./matter').importMatterData,
    export: require('./matter').exportMatterData,
    process: require('./matter').processMatterData,
    batchProcess: require('./matter').batchProcessMatterData,
    reprocessFailed: require('./matter').reprocessFailedMatterData,
    getProcessProgress: require('./matter').getMatterProcessProgress
  },
  
  // 事项状态管理
  status: {
    update: require('./matter').updateMatterStatus,
    batchUpdate: require('./matter').batchUpdateMatterStatus,
    getStatistics: require('./matter').getMatterStatusStatistics
  },
  
  // 事项类型管理
  type: {
    getList: require('./matter').getMatterTypeList,
    save: require('./matter').saveMatterType,
    update: require('./matter').updateMatterType,
    delete: require('./matter').deleteMatterType
  },
  
  // 事项模板管理
  template: {
    getPage: require('./matter').getMatterTemplatePage,
    getById: require('./matter').getMatterTemplateById,
    save: require('./matter').saveMatterTemplate,
    update: require('./matter').updateMatterTemplate,
    delete: require('./matter').deleteMatterTemplate,
    getByType: require('./matter').getMatterTemplateByType
  }
}

// 凭证管理 API 对象
export const voucherApi = {
  // 会计凭证管理
  voucher: {
    getPage: require('./voucher').getVoucherPage,
    getById: require('./voucher').getVoucherById,
    save: require('./voucher').saveVoucher,
    update: require('./voucher').updateVoucher,
    delete: require('./voucher').deleteVoucher,
    batchDelete: require('./voucher').batchDeleteVouchers,
    updateStatus: require('./voucher').updateVoucherStatus,
    batchUpdateStatus: require('./voucher').batchUpdateVoucherStatus,
    post: require('./voucher').postVouchers,
    unpost: require('./voucher').unpostVouchers
  },
  
  // 凭证生成
  generation: {
    generateByMatter: require('./voucher').generateVouchersByMatter,
    batchGenerate: require('./voucher').batchGenerateVouchers,
    preview: require('./voucher').previewVoucherGeneration,
    getProgress: require('./voucher').getVoucherGenerationProgress
  },
  
  // 凭证模板管理
  template: {
    getPage: require('./voucher').getVoucherTemplatePage,
    getById: require('./voucher').getVoucherTemplateById,
    save: require('./voucher').saveVoucherTemplate,
    update: require('./voucher').updateVoucherTemplate,
    delete: require('./voucher').deleteVoucherTemplate,
    copy: require('./voucher').copyVoucherTemplate
  },
  
  // 凭证导入导出
  importExport: {
    import: require('./voucher').importVouchers,
    export: require('./voucher').exportVouchers,
    downloadTemplate: require('./voucher').downloadVoucherImportTemplate
  },
  
  // 凭证统计分析
  statistics: {
    getStatistics: require('./voucher').getVoucherStatistics,
    getStatusDistribution: require('./voucher').getVoucherStatusDistribution,
    getGenerationTrend: require('./voucher').getVoucherGenerationTrend
  }
}

// 默认导出
export default {
  systemApi,
  rulesApi,
  matterApi,
  voucherApi,
  influenceFactorApi // 向后兼容
}
