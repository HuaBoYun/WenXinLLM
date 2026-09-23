/*
 * @Description: 财务共享 - 平台会计模块常量配置
 * @Author: system
 * @Date: 2024-12-19
 */

// 默认分页配置
export const DEFAULT_PAGE_CONFIG = {
  pageNumber: 1,
  pageSize: 20
}

// 默认租户配置
export const DEFAULT_TENANT_CONFIG = {
  tenantId: 'default_tenant',
  orgId: 'default_org'
}

// 凭证状态枚举
export const VOUCHER_STATUS = {
  DRAFT: '0',      // 草稿
  APPROVED: '1',   // 已审核
  POSTED: '2'      // 已过账
}

// 凭证类型枚举
export const VOUCHER_TYPE = {
  ACCOUNTING: 'ACCOUNTING',  // 记账凭证
  RECEIPT: 'RECEIPT',        // 收款凭证
  PAYMENT: 'PAYMENT',        // 付款凭证
  TRANSFER: 'TRANSFER'       // 转账凭证
}

// 凭证状态映射
export const VOUCHER_STATUS_MAP = {
  [VOUCHER_STATUS.DRAFT]: '草稿',
  [VOUCHER_STATUS.APPROVED]: '已审核',
  [VOUCHER_STATUS.POSTED]: '已过账'
}

// 凭证类型映射
export const VOUCHER_TYPE_MAP = {
  [VOUCHER_TYPE.ACCOUNTING]: '记账凭证',
  [VOUCHER_TYPE.RECEIPT]: '收款凭证',
  [VOUCHER_TYPE.PAYMENT]: '付款凭证',
  [VOUCHER_TYPE.TRANSFER]: '转账凭证'
}

// 凭证状态类型映射（Element UI tag type）
export const VOUCHER_STATUS_TYPE_MAP = {
  [VOUCHER_STATUS.DRAFT]: 'warning',
  [VOUCHER_STATUS.APPROVED]: 'primary',
  [VOUCHER_STATUS.POSTED]: 'success'
}

// 凭证类型类型映射（Element UI tag type）
export const VOUCHER_TYPE_TYPE_MAP = {
  [VOUCHER_TYPE.ACCOUNTING]: 'primary',
  [VOUCHER_TYPE.RECEIPT]: 'success',
  [VOUCHER_TYPE.PAYMENT]: 'warning',
  [VOUCHER_TYPE.TRANSFER]: 'info'
}