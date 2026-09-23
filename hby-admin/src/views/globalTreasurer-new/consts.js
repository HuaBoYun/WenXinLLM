/*
 * @Date: 2025-09-26 16:00:00
 * @LastEditors: AI Assistant
 * @LastEditTime: 2025-09-26 16:00:00
 * @FilePath: /hb-admin/src/views/globalTreasurer-new/consts.js
 * @Description: 全球司库系统常量定义
 */

// 网关前缀
export const GATEWAY_PREFIX = '/qqsk'

// 成功状态码 - 示例云规范: code=1 表示成功，经过网关处理后会被转换为 200
export const SUCCESS_CODE = [1, '1', 200, '200']

// 通用状态
export const COMMON_STATUS = [
  {
    label: '启用',
    value: 1,
  },
  {
    label: '禁用',
    value: 0,
  },
]

// 审批状态
export const APPROVAL_STATUS = [
  {
    label: '待审批',
    value: 0,
  },
  {
    label: '审批中',
    value: 1,
  },
  {
    label: '已退回',
    value: 2,
  },
  {
    label: '已通过',
    value: 3,
  },
  {
    label: '已终止',
    value: 4,
  },
  {
    label: '已完成',
    value: 5,
  },
]

// 申请状态选项
export const APPLICATION_STATUS_OPTIONS = [
  {
    label: '待提交',
    value: 'DRAFT',
  },
  {
    label: '待审批',
    value: 'PENDING',
  },
  {
    label: '审批中',
    value: 'REVIEWING',
  },
  {
    label: '已通过',
    value: 'APPROVED',
  },
  {
    label: '已拒绝',
    value: 'REJECTED',
  },
  {
    label: '已取消',
    value: 'CANCELLED',
  },
  {
    label: '已完成',
    value: 'COMPLETED',
  },
]

// 参数类型
export const PARAM_TYPES = [
  {
    label: '系统参数',
    value: 'SYSTEM',
  },
  {
    label: '业务参数',
    value: 'BUSINESS',
  },
  {
    label: '安全参数',
    value: 'SECURITY',
  },
  {
    label: '接口参数',
    value: 'INTERFACE',
  },
]

// 印鉴类型
export const SEAL_TYPES = [
  {
    label: '公章',
    value: 'OFFICIAL',
  },
  {
    label: '财务章',
    value: 'FINANCIAL',
  },
  {
    label: '法人章',
    value: 'LEGAL',
  },
  {
    label: '合同章',
    value: 'CONTRACT',
  },
]

// 账户状态
export const ACCOUNT_STATUS = [
  {
    label: '正常',
    value: 'NORMAL',
  },
  {
    label: '冻结',
    value: 'FROZEN',
  },
  {
    label: '销户',
    value: 'CLOSED',
  },
  {
    label: '待激活',
    value: 'PENDING',
  },
]

// 账户类型
export const ACCOUNT_TYPES = [
  {
    label: '活期账户',
    value: 'CURRENT',
  },
  {
    label: '定期账户',
    value: 'FIXED',
  },
  {
    label: '储蓄账户',
    value: 'SAVINGS',
  },
  {
    label: '基本户',
    value: 'BASIC',
  },
  {
    label: '一般户',
    value: 'GENERAL',
  },
  {
    label: '专用户',
    value: 'SPECIAL',
  },
  {
    label: '临时户',
    value: 'TEMPORARY',
  },
]

// 币种
export const CURRENCIES = [
  {
    label: '人民币',
    value: 'CNY',
  },
  {
    label: '美元',
    value: 'USD',
  },
  {
    label: '欧元',
    value: 'EUR',
  },
  {
    label: '日元',
    value: 'JPY',
  },
  {
    label: '英镑',
    value: 'GBP',
  },
  {
    label: '港币',
    value: 'HKD',
  },
]

// 交易状态
export const TRANSACTION_STATUS = [
  {
    label: '待处理',
    value: 'PENDING',
  },
  {
    label: '处理中',
    value: 'PROCESSING',
  },
  {
    label: '成功',
    value: 'SUCCESS',
  },
  {
    label: '失败',
    value: 'FAILED',
  },
  {
    label: '已撤销',
    value: 'CANCELLED',
  },
]

// 风险等级
export const RISK_LEVELS = [
  {
    label: '低风险',
    value: 'LOW',
  },
  {
    label: '中风险',
    value: 'MEDIUM',
  },
  {
    label: '高风险',
    value: 'HIGH',
  },
  {
    label: '极高风险',
    value: 'CRITICAL',
  },
]

// 业务类型
export const BUSINESS_TYPES = [
  {
    label: '收款',
    value: 'RECEIVE',
  },
  {
    label: '付款',
    value: 'PAYMENT',
  },
  {
    label: '转账',
    value: 'TRANSFER',
  },
  {
    label: '投资',
    value: 'INVESTMENT',
  },
  {
    label: '融资',
    value: 'FINANCING',
  },
]

// 银行类型
export const BANK_TYPES = [
  {
    label: '国有银行',
    value: 'STATE_OWNED',
  },
  {
    label: '股份制银行',
    value: 'JOINT_STOCK',
  },
  {
    label: '城商行',
    value: 'CITY_COMMERCIAL',
  },
  {
    label: '农商行',
    value: 'RURAL_COMMERCIAL',
  },
  {
    label: '外资银行',
    value: 'FOREIGN',
  },
]

// 票据状态
export const BILL_STATUS = [
  {
    label: '待背书',
    value: 'PENDING_ENDORSE',
  },
  {
    label: '已背书',
    value: 'ENDORSED',
  },
  {
    label: '已贴现',
    value: 'DISCOUNTED',
  },
  {
    label: '已到期',
    value: 'MATURED',
  },
  {
    label: '已兑付',
    value: 'PAID',
  },
]

// 投资产品类型
export const INVESTMENT_TYPES = [
  {
    label: '银行理财',
    value: 'BANK_WEALTH',
  },
  {
    label: '债券投资',
    value: 'BOND',
  },
  {
    label: '股票投资',
    value: 'STOCK',
  },
  {
    label: '基金投资',
    value: 'FUND',
  },
  {
    label: '信托产品',
    value: 'TRUST',
  },
]

// 融资方式
export const FINANCING_TYPES = [
  {
    label: '银行贷款',
    value: 'BANK_LOAN',
  },
  {
    label: '债券发行',
    value: 'BOND_ISSUE',
  },
  {
    label: '融资租赁',
    value: 'FINANCIAL_LEASE',
  },
  {
    label: '保理业务',
    value: 'FACTORING',
  },
  {
    label: '信用证',
    value: 'LETTER_OF_CREDIT',
  },
]

// 衍生品类型
export const DERIVATIVE_TYPES = [
  {
    label: '远期合约',
    value: 'FORWARD',
  },
  {
    label: '期货合约',
    value: 'FUTURES',
  },
  {
    label: '期权合约',
    value: 'OPTIONS',
  },
  {
    label: '掉期合约',
    value: 'SWAP',
  },
]

// 监管报告类型
export const REPORT_TYPES = [
  {
    label: '央行报告',
    value: 'PBOC',
  },
  {
    label: '银保监报告',
    value: 'CBIRC',
  },
  {
    label: '证监会报告',
    value: 'CSRC',
  },
  {
    label: '外管局报告',
    value: 'SAFE',
  },
]

// 数据状态
export const DATA_STATUS = [
  {
    label: '草稿',
    value: 'DRAFT',
  },
  {
    label: '待审核',
    value: 'PENDING_REVIEW',
  },
  {
    label: '已审核',
    value: 'REVIEWED',
  },
  {
    label: '已发布',
    value: 'PUBLISHED',
  },
  {
    label: '已归档',
    value: 'ARCHIVED',
  },
]

// 操作类型
export const OPERATION_TYPES = [
  {
    label: '新增',
    value: 'CREATE',
  },
  {
    label: '修改',
    value: 'UPDATE',
  },
  {
    label: '删除',
    value: 'DELETE',
  },
  {
    label: '查询',
    value: 'QUERY',
  },
  {
    label: '审批',
    value: 'APPROVE',
  },
]

// 分页默认配置
export const PAGINATION_CONFIG = {
  pageNumber: 1,
  pageSize: 20,
  pageSizes: [10, 20, 50, 100],
  layout: 'total, sizes, prev, pager, next, jumper',
}

// 表格默认配置
export const TABLE_CONFIG = {
  stripe: true,
  border: true,
  size: 'small',
  'highlight-current-row': true,
}

// 表单默认配置
export const FORM_CONFIG = {
  'label-width': '120px',
  size: 'small',
}

// 日期格式
export const DATE_FORMATS = {
  DATE: 'yyyy-MM-dd',
  DATETIME: 'yyyy-MM-dd HH:mm:ss',
  TIME: 'HH:mm:ss',
  MONTH: 'yyyy-MM',
  YEAR: 'yyyy',
}

// 金额格式化
export const AMOUNT_FORMATS = {
  YUAN: '元',
  WAN_YUAN: '万元',
  YI_YUAN: '亿元',
}

// 银行选项
export const BANK_OPTIONS = [
  {
    label: '中国工商银行',
    value: 'ICBC',
  },
  {
    label: '中国建设银行',
    value: 'CCB',
  },
  {
    label: '中国农业银行',
    value: 'ABC',
  },
  {
    label: '中国银行',
    value: 'BOC',
  },
  {
    label: '交通银行',
    value: 'BOCOM',
  },
  {
    label: '招商银行',
    value: 'CMB',
  },
  {
    label: '中信银行',
    value: 'CITIC',
  },
  {
    label: '光大银行',
    value: 'CEB',
  },
  {
    label: '华夏银行',
    value: 'HXB',
  },
  {
    label: '民生银行',
    value: 'CMBC',
  },
  {
    label: '广发银行',
    value: 'CGB',
  },
  {
    label: '平安银行',
    value: 'PAB',
  },
  {
    label: '浦发银行',
    value: 'SPDB',
  },
  {
    label: '兴业银行',
    value: 'CIB',
  },
]

// 为了兼容性，添加别名
export const ACCOUNT_TYPE_OPTIONS = ACCOUNT_TYPES
export const CURRENCY_OPTIONS = CURRENCIES
