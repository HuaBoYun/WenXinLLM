// 财务共享平台 - 财务公共模块常量定义

// 导入上级目录的常量
export * from '../../consts.js'

// 会计科目相关常量
export const ACCOUNT_SUBJECT_CONSTANTS = {
  // 科目编码长度
  CODE_LENGTH: {
    MIN: 4,
    MAX: 20
  },
  
  // 科目级次
  LEVEL: {
    MIN: 1,
    MAX: 6
  },
  
  // 科目状态
  STATUS: {
    ACTIVE: 1,
    INACTIVE: 0
  }
}

// 辅助核算项相关常量
export const AUXILIARY_ITEM_CONSTANTS = {
  // 编码长度
  CODE_LENGTH: {
    MIN: 2,
    MAX: 20
  },
  
  // 名称长度
  NAME_LENGTH: {
    MIN: 1,
    MAX: 100
  }
}

// 币种汇率相关常量
export const CURRENCY_RATE_CONSTANTS = {
  // 汇率精度
  RATE_PRECISION: 6,
  
  // 汇率范围
  RATE_RANGE: {
    MIN: 0.000001,
    MAX: 999999.999999
  }
}

// 表单验证规则
export const VALIDATION_RULES = {
  // 必填项
  REQUIRED: { required: true, message: '此项为必填项', trigger: 'blur' },
  
  // 科目编码验证
  SUBJECT_CODE: [
    { required: true, message: '科目编码不能为空', trigger: 'blur' },
    { min: 4, max: 20, message: '科目编码长度在 4 到 20 个字符', trigger: 'blur' },
    { pattern: /^[A-Za-z0-9]+$/, message: '科目编码只能包含字母和数字', trigger: 'blur' }
  ],
  
  // 科目名称验证
  SUBJECT_NAME: [
    { required: true, message: '科目名称不能为空', trigger: 'blur' },
    { min: 1, max: 100, message: '科目名称长度在 1 到 100 个字符', trigger: 'blur' }
  ],
  
  // 辅助核算编码验证
  AUXILIARY_CODE: [
    { required: true, message: '编码不能为空', trigger: 'blur' },
    { min: 2, max: 20, message: '编码长度在 2 到 20 个字符', trigger: 'blur' },
    { pattern: /^[A-Za-z0-9]+$/, message: '编码只能包含字母和数字', trigger: 'blur' }
  ],
  
  // 辅助核算名称验证
  AUXILIARY_NAME: [
    { required: true, message: '名称不能为空', trigger: 'blur' },
    { min: 1, max: 100, message: '名称长度在 1 到 100 个字符', trigger: 'blur' }
  ],
  
  // 汇率验证
  EXCHANGE_RATE: [
    { required: true, message: '汇率不能为空', trigger: 'blur' },
    { type: 'number', min: 0.000001, max: 999999.999999, message: '汇率必须在 0.000001 到 999999.999999 之间', trigger: 'blur' }
  ]
}

// 表格列配置
export const TABLE_COLUMNS = {
  // 会计科目表格列
  ACCOUNT_SUBJECT: [
    { prop: 'subjectCode', label: '科目编码', width: 120, sortable: true },
    { prop: 'subjectName', label: '科目名称', minWidth: 150 },
    { prop: 'subjectTypeName', label: '科目类型', width: 100 },
    { prop: 'balanceDirectionName', label: '余额方向', width: 100 },
    { prop: 'isLeafName', label: '是否末级', width: 100 },
    { prop: 'enabledName', label: '状态', width: 80 },
    { prop: 'createTime', label: '创建时间', width: 160, sortable: true }
  ],
  
  // 辅助核算项表格列
  AUXILIARY_ITEM: [
    { prop: 'auxiliaryCode', label: '编码', width: 120, sortable: true },
    { prop: 'auxiliaryName', label: '名称', minWidth: 150 },
    { prop: 'auxiliaryTypeName', label: '类型', width: 100 },
    { prop: 'enabledName', label: '状态', width: 80 },
    { prop: 'createTime', label: '创建时间', width: 160, sortable: true }
  ],
  
  // 币种汇率表格列
  CURRENCY_RATE: [
    { prop: 'currencyCode', label: '币种代码', width: 100, sortable: true },
    { prop: 'currencyName', label: '币种名称', width: 120 },
    { prop: 'exchangeRate', label: '汇率', width: 120, sortable: true },
    { prop: 'rateTypeName', label: '汇率类型', width: 100 },
    { prop: 'isBaseCurrencyName', label: '是否本位币', width: 120 },
    { prop: 'effectiveDate', label: '生效日期', width: 120, sortable: true },
    { prop: 'enabledName', label: '状态', width: 80 }
  ]
}

// 搜索表单配置
export const SEARCH_FORMS = {
  // 会计科目搜索表单
  ACCOUNT_SUBJECT: {
    subjectCode: '',
    subjectName: '',
    subjectType: '',
    enabled: ''
  },
  
  // 辅助核算项搜索表单
  AUXILIARY_ITEM: {
    auxiliaryCode: '',
    auxiliaryName: '',
    auxiliaryType: '',
    enabled: ''
  },
  
  // 币种汇率搜索表单
  CURRENCY_RATE: {
    currencyCode: '',
    currencyName: '',
    rateType: '',
    isBaseCurrency: '',
    enabled: ''
  }
}

// 默认表单数据
export const DEFAULT_FORMS = {
  // 会计科目默认表单
  ACCOUNT_SUBJECT: {
    subjectCode: '',
    subjectName: '',
    subjectType: '',
    balanceDirection: '',
    parentId: null,
    isLeaf: 1,
    enabled: 1,
    remark: ''
  },
  
  // 辅助核算项默认表单
  AUXILIARY_ITEM: {
    auxiliaryCode: '',
    auxiliaryName: '',
    auxiliaryType: '',
    enabled: 1,
    remark: ''
  },
  
  // 币种汇率默认表单
  CURRENCY_RATE: {
    currencyCode: '',
    currencyName: '',
    exchangeRate: 1,
    rateType: 1,
    isBaseCurrency: 0,
    effectiveDate: '',
    enabled: 1,
    remark: ''
  }
}

// 操作按钮配置
export const ACTION_BUTTONS = {
  // 主要操作按钮
  PRIMARY: [
    { key: 'add', label: '新增', type: 'primary', icon: 'el-icon-plus' },
    { key: 'edit', label: '编辑', type: 'success', icon: 'el-icon-edit' },
    { key: 'delete', label: '删除', type: 'danger', icon: 'el-icon-delete' },
    { key: 'export', label: '导出', type: 'info', icon: 'el-icon-download' }
  ],
  
  // 次要操作按钮
  SECONDARY: [
    { key: 'view', label: '查看', type: 'info', icon: 'el-icon-view' },
    { key: 'copy', label: '复制', type: 'warning', icon: 'el-icon-copy-document' },
    { key: 'import', label: '导入', type: 'primary', icon: 'el-icon-upload2' }
  ]
}

// 消息提示配置
export const MESSAGE_CONFIG = {
  // 成功消息
  SUCCESS: {
    ADD: '新增成功',
    UPDATE: '更新成功',
    DELETE: '删除成功',
    IMPORT: '导入成功',
    EXPORT: '导出成功'
  },
  
  // 错误消息
  ERROR: {
    ADD: '新增失败',
    UPDATE: '更新失败',
    DELETE: '删除失败',
    IMPORT: '导入失败',
    EXPORT: '导出失败',
    NETWORK: '网络错误，请稍后重试'
  },
  
  // 警告消息
  WARNING: {
    DELETE_CONFIRM: '确定要删除选中的记录吗？',
    UNSAVED_CHANGES: '有未保存的更改，确定要离开吗？'
  }
}
