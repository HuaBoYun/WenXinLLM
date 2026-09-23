/**
 * 预算模型API - 快捷导出
 * 从 budgetPlanning/budgetModel.js 导出所有方法
 */
export * from './budgetPlanning/budgetModel'

// 额外导出一个别名方法，用于兼容不同的调用方式
import { getModelListNoPage } from './budgetPlanning/budgetModel'

export function getBudgetModelList(data) {
  return getModelListNoPage(data)
}

