import request from '@/utils/request'

/**
 * 预算分析管理API
 */

// 获取预算执行分析
export function getBudgetExecutionAnalysis(params) {
  return request({
    url: '/accountant/eps/budget-analysis/execution',
    method: 'get',
    params
  })
}

// 获取预算差异分析
export function getBudgetVarianceAnalysis(params) {
  return request({
    url: '/accountant/eps/budget-analysis/variance',
    method: 'get',
    params
  })
}

// 获取预算趋势分析
export function getBudgetTrendAnalysis(params) {
  return request({
    url: '/accountant/eps/budget-analysis/trend',
    method: 'get',
    params
  })
}

// 获取预算结构分析
export function getBudgetStructureAnalysis(params) {
  return request({
    url: '/accountant/eps/budget-analysis/structure',
    method: 'get',
    params
  })
}

// 获取预算对比分析
export function getBudgetComparisonAnalysis(params) {
  return request({
    url: '/accountant/eps/budget-analysis/comparison',
    method: 'get',
    params
  })
}

// 获取预算完成率分析
export function getBudgetCompletionRateAnalysis(params) {
  return request({
    url: '/accountant/eps/budget-analysis/completion-rate',
    method: 'get',
    params
  })
}

// 获取预算排名分析
export function getBudgetRankingAnalysis(params) {
  return request({
    url: '/accountant/eps/budget-analysis/ranking',
    method: 'get',
    params
  })
}

// 获取预算异常分析
export function getBudgetAnomalyAnalysis(params) {
  return request({
    url: '/accountant/eps/budget-analysis/anomaly',
    method: 'get',
    params
  })
}

// 生成预算分析报表
export function generateBudgetAnalysisReport(reportParams) {
  return request({
    url: '/accountant/eps/budget-analysis/report',
    method: 'post',
    data: reportParams
  })
}

// 导出预算分析数据
export function exportBudgetAnalysisData(exportParams) {
  return request({
    url: '/accountant/eps/budget-analysis/export',
    method: 'post',
    data: exportParams
  })
}

// 获取预算分析仪表板
export function getBudgetAnalysisDashboard(params) {
  return request({
    url: '/accountant/eps/budget-analysis/dashboard',
    method: 'get',
    params
  })
}

// 获取预算分析图表数据
export function getBudgetAnalysisChartData(params) {
  return request({
    url: '/accountant/eps/budget-analysis/chart',
    method: 'get',
    params
  })
}

// 保存预算分析配置
export function saveBudgetAnalysisConfig(analysisConfig) {
  return request({
    url: '/accountant/eps/budget-analysis/config',
    method: 'post',
    data: analysisConfig
  })
}

// 获取预算分析配置
export function getBudgetAnalysisConfig(params) {
  return request({
    url: '/accountant/eps/budget-analysis/config',
    method: 'get',
    params
  })
}

// 获取预算分析模板
export function getBudgetAnalysisTemplates(params) {
  return request({
    url: '/accountant/eps/budget-analysis/template',
    method: 'get',
    params
  })
}

// 应用预算分析模板
export function applyBudgetAnalysisTemplate(templateParams) {
  return request({
    url: '/accountant/eps/budget-analysis/template/apply',
    method: 'post',
    data: templateParams
  })
}

// 获取预算分析洞察
export function getBudgetAnalysisInsights(params) {
  return request({
    url: '/accountant/eps/budget-analysis/insights',
    method: 'get',
    params
  })
}

// 获取预算分析建议
export function getBudgetAnalysisRecommendations(params) {
  return request({
    url: '/accountant/eps/budget-analysis/recommendations',
    method: 'get',
    params
  })
}

// 执行自定义分析
export function executeCustomAnalysis(customParams) {
  return request({
    url: '/accountant/eps/budget-analysis/custom',
    method: 'post',
    data: customParams
  })
}

// 获取预算分析历史
export function getBudgetAnalysisHistory(params) {
  return request({
    url: '/accountant/eps/budget-analysis/history',
    method: 'get',
    params
  })
}

// 刷新预算分析缓存
export function refreshBudgetAnalysisCache(params) {
  return request({
    url: '/accountant/eps/budget-analysis/refresh-cache',
    method: 'post',
    params
  })
}

// 预算分析快捷操作
export const budgetAnalysisQuickActions = {
  // 快速执行分析
  quickExecutionAnalysis(versionId, organizationId) {
    return getBudgetExecutionAnalysis({
      versionId,
      organizationId,
      analysisDimension: 'MONTH'
    })
  },

  // 快速差异分析
  quickVarianceAnalysis(versionId, compareVersionId) {
    return getBudgetVarianceAnalysis({
      versionId,
      compareVersionId,
      varianceType: 'AMOUNT'
    })
  },

  // 快速趋势分析
  quickTrendAnalysis(versionId, organizationId) {
    return getBudgetTrendAnalysis({
      versionId,
      organizationId,
      trendPeriods: 12,
      forecastPeriods: 3
    })
  },

  // 快速结构分析
  quickStructureAnalysis(versionId, analysisDimension = 'SUBJECT') {
    return getBudgetStructureAnalysis({
      versionId,
      analysisDimension,
      hierarchyDepth: 3
    })
  },

  // 快速完成率分析
  quickCompletionRateAnalysis(versionId, organizationId) {
    return getBudgetCompletionRateAnalysis({
      versionId,
      organizationId,
      statisticsPeriod: 'MONTH'
    })
  },

  // 快速排名分析
  quickRankingAnalysis(versionId, rankingDimension = 'ORGANIZATION') {
    return getBudgetRankingAnalysis({
      versionId,
      rankingDimension,
      rankingMetric: 'BUDGET_AMOUNT',
      rankingLimit: 10
    })
  },

  // 快速异常检测
  quickAnomalyDetection(versionId, organizationId) {
    return getBudgetAnomalyAnalysis({
      versionId,
      organizationId,
      anomalyType: 'ALL',
      anomalyThreshold: 0.2
    })
  }
}

// 预算分析工具函数
export const budgetAnalysisUtils = {
  // 格式化分析结果
  formatAnalysisResult(result) {
    if (!result || !result.data) return null
    
    return {
      ...result.data,
      formattedTime: this.formatDateTime(result.data.analysisTime)
    }
  },

  // 格式化日期时间
  formatDateTime(dateTime) {
    if (!dateTime) return ''
    return new Date(dateTime).toLocaleString('zh-CN')
  },

  // 计算增长率
  calculateGrowthRate(current, previous) {
    if (!previous || previous === 0) return 0
    return (((current - previous) / previous) * 100).toFixed(2)
  },

  // 计算完成率
  calculateCompletionRate(actual, budget) {
    if (!budget || budget === 0) return 0
    return ((actual / budget) * 100).toFixed(2)
  },

  // 计算差异率
  calculateVarianceRate(variance, budget) {
    if (!budget || budget === 0) return 0
    return ((variance / budget) * 100).toFixed(2)
  },

  // 获取趋势方向
  getTrendDirection(trendValue) {
    if (trendValue > 0) return 'up'
    if (trendValue < 0) return 'down'
    return 'stable'
  },

  // 获取趋势颜色
  getTrendColor(trendDirection) {
    const colorMap = {
      'up': '#67C23A',
      'down': '#F56C6C',
      'stable': '#909399'
    }
    return colorMap[trendDirection] || '#909399'
  },

  // 获取异常级别
  getAnomalyLevel(anomalyScore) {
    if (anomalyScore >= 0.8) return 'HIGH'
    if (anomalyScore >= 0.5) return 'MEDIUM'
    if (anomalyScore >= 0.2) return 'LOW'
    return 'NORMAL'
  },

  // 获取异常级别颜色
  getAnomalyLevelColor(level) {
    const colorMap = {
      'HIGH': '#F56C6C',
      'MEDIUM': '#E6A23C',
      'LOW': '#409EFF',
      'NORMAL': '#67C23A'
    }
    return colorMap[level] || '#909399'
  },

  // 生成图表配置
  generateChartConfig(chartType, data, options = {}) {
    const baseConfig = {
      type: chartType,
      data: data,
      options: {
        responsive: true,
        maintainAspectRatio: false,
        ...options
      }
    }

    // 根据图表类型添加特定配置
    switch (chartType) {
      case 'line':
        baseConfig.options.scales = {
          y: {
            beginAtZero: true
          }
        }
        break
      case 'bar':
        baseConfig.options.scales = {
          y: {
            beginAtZero: true
          }
        }
        break
      case 'pie':
        baseConfig.options.plugins = {
          legend: {
            position: 'right'
          }
        }
        break
    }

    return baseConfig
  },

  // 处理分析数据
  processAnalysisData(rawData, analysisType) {
    if (!rawData) return null

    switch (analysisType) {
      case 'execution':
        return this.processExecutionData(rawData)
      case 'variance':
        return this.processVarianceData(rawData)
      case 'trend':
        return this.processTrendData(rawData)
      case 'structure':
        return this.processStructureData(rawData)
      default:
        return rawData
    }
  },

  // 处理执行分析数据
  processExecutionData(data) {
    if (!data.executionSummary) return data

    const summary = data.executionSummary
    return {
      ...data,
      executionSummary: {
        ...summary,
        executionRateFormatted: `${summary.executionRate}%`,
        totalBudgetFormatted: this.formatAmount(summary.totalBudget),
        totalActualFormatted: this.formatAmount(summary.totalActual),
        totalVarianceFormatted: this.formatAmount(summary.totalVariance)
      }
    }
  },

  // 处理差异分析数据
  processVarianceData(data) {
    // TODO: 实现差异数据处理
    return data
  },

  // 处理趋势分析数据
  processTrendData(data) {
    // TODO: 实现趋势数据处理
    return data
  },

  // 处理结构分析数据
  processStructureData(data) {
    // TODO: 实现结构数据处理
    return data
  },

  // 格式化金额
  formatAmount(amount) {
    if (!amount) return '0.00'
    return Number(amount).toLocaleString('zh-CN', {
      minimumFractionDigits: 2,
      maximumFractionDigits: 2
    })
  },

  // 验证分析参数
  validateAnalysisParams(params, requiredFields = []) {
    const errors = []
    
    for (const field of requiredFields) {
      if (!params[field]) {
        errors.push(`${field}不能为空`)
      }
    }
    
    if (params.versionId && params.versionId <= 0) {
      errors.push('版本ID必须大于0')
    }
    
    if (params.anomalyThreshold && (params.anomalyThreshold < 0 || params.anomalyThreshold > 1)) {
      errors.push('异常阈值必须在0-1之间')
    }
    
    return {
      isValid: errors.length === 0,
      errors
    }
  }
}

// 预算分析图表配置
export const budgetAnalysisCharts = {
  // 执行分析图表
  executionChart: {
    type: 'line',
    options: {
      responsive: true,
      plugins: {
        title: {
          display: true,
          text: '预算执行趋势'
        }
      },
      scales: {
        y: {
          beginAtZero: true,
          title: {
            display: true,
            text: '金额'
          }
        }
      }
    }
  },

  // 差异分析图表
  varianceChart: {
    type: 'bar',
    options: {
      responsive: true,
      plugins: {
        title: {
          display: true,
          text: '预算差异分析'
        }
      },
      scales: {
        y: {
          beginAtZero: true,
          title: {
            display: true,
            text: '差异金额'
          }
        }
      }
    }
  },

  // 结构分析图表
  structureChart: {
    type: 'pie',
    options: {
      responsive: true,
      plugins: {
        title: {
          display: true,
          text: '预算结构分析'
        },
        legend: {
          position: 'right'
        }
      }
    }
  },

  // 排名分析图表
  rankingChart: {
    type: 'horizontalBar',
    options: {
      responsive: true,
      plugins: {
        title: {
          display: true,
          text: '预算排名分析'
        }
      },
      scales: {
        x: {
          beginAtZero: true,
          title: {
            display: true,
            text: '金额'
          }
        }
      }
    }
  }
}

export default {
  getBudgetExecutionAnalysis,
  getBudgetVarianceAnalysis,
  getBudgetTrendAnalysis,
  getBudgetStructureAnalysis,
  getBudgetComparisonAnalysis,
  getBudgetCompletionRateAnalysis,
  getBudgetRankingAnalysis,
  getBudgetAnomalyAnalysis,
  generateBudgetAnalysisReport,
  exportBudgetAnalysisData,
  getBudgetAnalysisDashboard,
  getBudgetAnalysisChartData,
  saveBudgetAnalysisConfig,
  getBudgetAnalysisConfig,
  getBudgetAnalysisTemplates,
  applyBudgetAnalysisTemplate,
  getBudgetAnalysisInsights,
  getBudgetAnalysisRecommendations,
  executeCustomAnalysis,
  getBudgetAnalysisHistory,
  refreshBudgetAnalysisCache,
  budgetAnalysisQuickActions,
  budgetAnalysisUtils,
  budgetAnalysisCharts
}
