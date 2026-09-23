/**
 * 财务共享 - 事项会计系统常量定义
 */

// 影响因素类型
export const FACTOR_TYPE = {
  BUSINESS: 1, // 业务类型
  ACCOUNT: 2,  // 科目类型
  AUXILIARY: 3, // 辅助核算
  OTHER: 4     // 其他
}

// 影响因素类型名称映射
export const FACTOR_TYPE_NAME = {
  [FACTOR_TYPE.BUSINESS]: '业务类型',
  [FACTOR_TYPE.ACCOUNT]: '科目类型',
  [FACTOR_TYPE.AUXILIARY]: '辅助核算',
  [FACTOR_TYPE.OTHER]: '其他'
}

// 数据类型
export const DATA_TYPE = {
  STRING: 1,  // 字符串
  NUMBER: 2,  // 数值
  DATE: 3,    // 日期
  BOOLEAN: 4  // 布尔
}

// 数据类型名称映射
export const DATA_TYPE_NAME = {
  [DATA_TYPE.STRING]: '字符串',
  [DATA_TYPE.NUMBER]: '数值',
  [DATA_TYPE.DATE]: '日期',
  [DATA_TYPE.BOOLEAN]: '布尔'
}

// 启用状态
export const ENABLED_STATUS = {
  DISABLED: 0, // 禁用
  ENABLED: 1   // 启用
}

// 启用状态名称映射
export const ENABLED_STATUS_NAME = {
  [ENABLED_STATUS.DISABLED]: '禁用',
  [ENABLED_STATUS.ENABLED]: '启用'
}

// 启用状态选项
export const ENABLED_STATUS_OPTIONS = [
  { label: '禁用', value: ENABLED_STATUS.DISABLED },
  { label: '启用', value: ENABLED_STATUS.ENABLED }
]

// 是否必填
export const REQUIRED_STATUS = {
  NO: 0,  // 否
  YES: 1  // 是
}

// 是否必填名称映射
export const REQUIRED_STATUS_NAME = {
  [REQUIRED_STATUS.NO]: '否',
  [REQUIRED_STATUS.YES]: '是'
}

// 会计凭证状态
export const VOUCHER_STATUS = {
  DRAFT: 0,      // 草稿
  GENERATED: 1,  // 已生成
  POSTED: 2,     // 已过账
  CANCELLED: 3   // 已取消
}

// 会计凭证状态名称映射
export const VOUCHER_STATUS_NAME = {
  [VOUCHER_STATUS.DRAFT]: '草稿',
  [VOUCHER_STATUS.GENERATED]: '已生成',
  [VOUCHER_STATUS.POSTED]: '已过账',
  [VOUCHER_STATUS.CANCELLED]: '已取消'
}

// 事项状态
export const MATTER_STATUS = {
  PENDING: 0,    // 待处理
  PROCESSING: 1, // 处理中
  COMPLETED: 2,  // 已完成
  FAILED: 3      // 失败
}

// 事项状态名称映射
export const MATTER_STATUS_NAME = {
  [MATTER_STATUS.PENDING]: '待处理',
  [MATTER_STATUS.PROCESSING]: '处理中',
  [MATTER_STATUS.COMPLETED]: '已完成',
  [MATTER_STATUS.FAILED]: '失败'
}

// 规则类型
export const RULE_TYPE = {
  RECOGNITION: 1, // 确认规则
  MEASUREMENT: 2, // 计量规则
  VOUCHER: 3,     // 凭证规则
  POSTING: 4      // 过账规则
}

// 规则类型名称映射
export const RULE_TYPE_NAME = {
  [RULE_TYPE.RECOGNITION]: '确认规则',
  [RULE_TYPE.MEASUREMENT]: '计量规则',
  [RULE_TYPE.VOUCHER]: '凭证规则',
  [RULE_TYPE.POSTING]: '过账规则'
}

// 默认分页配置
export const DEFAULT_PAGE_CONFIG = {
  pageNumber: 1,
  pageSize: 15
}

// 默认租户和账簿ID（TODO: 从用户信息中获取）
export const DEFAULT_TENANT_CONFIG = {
  tenantId: 1,
  bookId: 1
}

// ==================== 会计科目相关常量 ====================

// 科目类型
export const SUBJECT_TYPE = {
  ASSET: 1,      // 资产
  LIABILITY: 2,  // 负债
  EQUITY: 3,     // 权益
  INCOME: 4,     // 收入
  EXPENSE: 5     // 费用
}

// 科目类型名称映射
export const SUBJECT_TYPE_NAME = {
  [SUBJECT_TYPE.ASSET]: '资产',
  [SUBJECT_TYPE.LIABILITY]: '负债',
  [SUBJECT_TYPE.EQUITY]: '权益',
  [SUBJECT_TYPE.INCOME]: '收入',
  [SUBJECT_TYPE.EXPENSE]: '费用'
}

// 余额方向
export const BALANCE_DIRECTION = {
  DEBIT: 1,   // 借方
  CREDIT: 2   // 贷方
}

// 余额方向名称映射
export const BALANCE_DIRECTION_NAME = {
  [BALANCE_DIRECTION.DEBIT]: '借方',
  [BALANCE_DIRECTION.CREDIT]: '贷方'
}

// 是否末级
export const IS_LEAF = {
  NO: 0,   // 否
  YES: 1   // 是
}

// 是否末级名称映射
export const IS_LEAF_NAME = {
  [IS_LEAF.NO]: '否',
  [IS_LEAF.YES]: '是'
}

// ==================== 辅助核算项相关常量 ====================

// 辅助核算类型
export const AUXILIARY_TYPE = {
  DEPT: 'DEPT',         // 部门
  PERSON: 'PERSON',     // 人员
  PROJECT: 'PROJECT',   // 项目
  CUSTOMER: 'CUSTOMER', // 客户
  SUPPLIER: 'SUPPLIER', // 供应商
  PRODUCT: 'PRODUCT',   // 产品
  AREA: 'AREA',         // 地区
  OTHER: 'OTHER'        // 其他
}

// 辅助核算类型名称映射
export const AUXILIARY_TYPE_NAME = {
  [AUXILIARY_TYPE.DEPT]: '部门',
  [AUXILIARY_TYPE.PERSON]: '人员',
  [AUXILIARY_TYPE.PROJECT]: '项目',
  [AUXILIARY_TYPE.CUSTOMER]: '客户',
  [AUXILIARY_TYPE.SUPPLIER]: '供应商',
  [AUXILIARY_TYPE.PRODUCT]: '产品',
  [AUXILIARY_TYPE.AREA]: '地区',
  [AUXILIARY_TYPE.OTHER]: '其他'
}

// ==================== 币种汇率相关常量 ====================

// 汇率类型
export const RATE_TYPE = {
  SPOT: 1,     // 即期汇率
  FORWARD: 2,  // 远期汇率
  FIXED: 3     // 固定汇率
}

// 汇率类型名称映射
export const RATE_TYPE_NAME = {
  [RATE_TYPE.SPOT]: '即期汇率',
  [RATE_TYPE.FORWARD]: '远期汇率',
  [RATE_TYPE.FIXED]: '固定汇率'
}

// 汇率类型选项
export const RATE_TYPE_OPTIONS = [
  { label: '即期汇率', value: RATE_TYPE.SPOT },
  { label: '远期汇率', value: RATE_TYPE.FORWARD },
  { label: '固定汇率', value: RATE_TYPE.FIXED }
]

// 是否本位币
export const IS_BASE_CURRENCY = {
  NO: 0,   // 否
  YES: 1   // 是
}

// 是否本位币名称映射
export const IS_BASE_CURRENCY_NAME = {
  [IS_BASE_CURRENCY.NO]: '否',
  [IS_BASE_CURRENCY.YES]: '是'
}

// 是否本位币选项
export const IS_BASE_CURRENCY_OPTIONS = [
  { label: '否', value: IS_BASE_CURRENCY.NO },
  { label: '是', value: IS_BASE_CURRENCY.YES }
]

// ==================== 事项中心相关常量 ====================

// 事项状态
export const TRANSACTION_STATUS = {
  PENDING: 1,    // 待处理
  PROCESSED: 2,  // 已处理
  CANCELLED: 3   // 已取消
}

// 事项状态名称映射
export const TRANSACTION_STATUS_NAME = {
  [TRANSACTION_STATUS.PENDING]: '待处理',
  [TRANSACTION_STATUS.PROCESSED]: '已处理',
  [TRANSACTION_STATUS.CANCELLED]: '已取消'
}

// 事项状态选项
export const TRANSACTION_STATUS_OPTIONS = [
  { label: '待处理', value: TRANSACTION_STATUS.PENDING },
  { label: '已处理', value: TRANSACTION_STATUS.PROCESSED },
  { label: '已取消', value: TRANSACTION_STATUS.CANCELLED }
]

// 事项类型
export const TRANSACTION_TYPE = {
  SALES_ORDER: 'SALES_ORDER',       // 销售订单
  PURCHASE_ORDER: 'PURCHASE_ORDER', // 采购订单
  PAYMENT: 'PAYMENT',               // 付款
  RECEIPT: 'RECEIPT',               // 收款
  INVENTORY_IN: 'INVENTORY_IN',     // 入库
  INVENTORY_OUT: 'INVENTORY_OUT',   // 出库
  EXPENSE: 'EXPENSE',               // 费用
  INCOME: 'INCOME'                  // 收入
}

// 事项类型名称映射
export const TRANSACTION_TYPE_NAME = {
  [TRANSACTION_TYPE.SALES_ORDER]: '销售订单',
  [TRANSACTION_TYPE.PURCHASE_ORDER]: '采购订单',
  [TRANSACTION_TYPE.PAYMENT]: '付款',
  [TRANSACTION_TYPE.RECEIPT]: '收款',
  [TRANSACTION_TYPE.INVENTORY_IN]: '入库',
  [TRANSACTION_TYPE.INVENTORY_OUT]: '出库',
  [TRANSACTION_TYPE.EXPENSE]: '费用',
  [TRANSACTION_TYPE.INCOME]: '收入'
}

// 事项类型选项
export const TRANSACTION_TYPE_OPTIONS = [
  { label: '销售订单', value: TRANSACTION_TYPE.SALES_ORDER },
  { label: '采购订单', value: TRANSACTION_TYPE.PURCHASE_ORDER },
  { label: '付款', value: TRANSACTION_TYPE.PAYMENT },
  { label: '收款', value: TRANSACTION_TYPE.RECEIPT },
  { label: '入库', value: TRANSACTION_TYPE.INVENTORY_IN },
  { label: '出库', value: TRANSACTION_TYPE.INVENTORY_OUT },
  { label: '费用', value: TRANSACTION_TYPE.EXPENSE },
  { label: '收入', value: TRANSACTION_TYPE.INCOME }
]

// 来源系统
export const SOURCE_SYSTEM = {
  ERP: 'ERP',       // ERP系统
  CRM: 'CRM',       // CRM系统
  OA: 'OA',         // OA系统
  HR: 'HR',         // HR系统
  SCM: 'SCM',       // SCM系统
  WMS: 'WMS',       // WMS系统
  MANUAL: 'MANUAL'  // 手工录入
}

// 来源系统名称映射
export const SOURCE_SYSTEM_NAME = {
  [SOURCE_SYSTEM.ERP]: 'ERP系统',
  [SOURCE_SYSTEM.CRM]: 'CRM系统',
  [SOURCE_SYSTEM.OA]: 'OA系统',
  [SOURCE_SYSTEM.HR]: 'HR系统',
  [SOURCE_SYSTEM.SCM]: 'SCM系统',
  [SOURCE_SYSTEM.WMS]: 'WMS系统',
  [SOURCE_SYSTEM.MANUAL]: '手工录入'
}

// 来源系统选项
export const SOURCE_SYSTEM_OPTIONS = [
  { label: 'ERP系统', value: SOURCE_SYSTEM.ERP },
  { label: 'CRM系统', value: SOURCE_SYSTEM.CRM },
  { label: 'OA系统', value: SOURCE_SYSTEM.OA },
  { label: 'HR系统', value: SOURCE_SYSTEM.HR },
  { label: 'SCM系统', value: SOURCE_SYSTEM.SCM },
  { label: 'WMS系统', value: SOURCE_SYSTEM.WMS },
  { label: '手工录入', value: SOURCE_SYSTEM.MANUAL }
]

// ==================== 会计规则中心相关常量 ====================

// 会计规则类型
export const ACCOUNTING_RULE_TYPE = {
  ENTRY: 1,      // 分录规则
  DISPATCH: 2,   // 分发规则
  CONVERT: 3     // 转换规则
}

// 会计规则类型名称映射
export const ACCOUNTING_RULE_TYPE_NAME = {
  [ACCOUNTING_RULE_TYPE.ENTRY]: '分录规则',
  [ACCOUNTING_RULE_TYPE.DISPATCH]: '分发规则',
  [ACCOUNTING_RULE_TYPE.CONVERT]: '转换规则'
}

// 会计规则类型选项
export const ACCOUNTING_RULE_TYPE_OPTIONS = [
  { label: '分录规则', value: ACCOUNTING_RULE_TYPE.ENTRY },
  { label: '分发规则', value: ACCOUNTING_RULE_TYPE.DISPATCH },
  { label: '转换规则', value: ACCOUNTING_RULE_TYPE.CONVERT }
]

// 规则凭证类型
export const RULE_VOUCHER_TYPE = {
  PROFIT_LOSS: 1,  // 期末损益结转
  NORMAL: 2        // 普通规则凭证
}

// 规则凭证类型名称映射
export const RULE_VOUCHER_TYPE_NAME = {
  [RULE_VOUCHER_TYPE.PROFIT_LOSS]: '期末损益结转',
  [RULE_VOUCHER_TYPE.NORMAL]: '普通规则凭证'
}

// 规则凭证类型选项
export const RULE_VOUCHER_TYPE_OPTIONS = [
  { label: '期末损益结转', value: RULE_VOUCHER_TYPE.PROFIT_LOSS },
  { label: '普通规则凭证', value: RULE_VOUCHER_TYPE.NORMAL }
]
