/**
 * NCV65全面预算系统路由配置
 * 
 * @description NCV65全面预算系统的完整路由配置，包含所有功能模块
 * @author AI Assistant
 * @date 2025-01-08
 * @version 1.0.0
 */

import Layout from '@/vab/layouts'

const ncv65Router = {
  path: '/ncv65',
  component: Layout,
  redirect: '/ncv65/budget-system',
  name: 'NCV65',
  meta: {
    title: 'NCV65全面预算系统',
    icon: 'el-icon-s-finance',
    roles: ['admin', 'budget_manager', 'budget_user']
  },
  children: [

    // 预算体系管理 - 首页
    {
      path: 'budget-system',
      component: () => import('@/views/managementAccountant/ncv65/budgetSystem'),
      name: 'BudgetSystem',
      meta: {
        title: '预算体系管理',
        icon: 'el-icon-s-grid'
      }
    },

    // 预算体系管理 - 组织体系管理
    {
      path: 'budget-system/organization-structure',
      component: () => import('@/views/managementAccountant/ncv65/budgetSystem/OrganizationStructure'),
      name: 'OrganizationStructure',
      meta: {
        title: '组织体系管理',
        icon: 'el-icon-office-building'
      }
    },

    // 预算体系管理 - 维度配置
    {
      path: 'budget-system/dimension-configuration',
      component: () => import('@/views/managementAccountant/ncv65/budgetSystem/DimensionConfiguration'),
      name: 'DimensionConfiguration',
      meta: {
        title: '维度配置',
        icon: 'el-icon-menu'
      }
    },

    // 预算体系管理 - 指标管理
    {
      path: 'budget-system/indicator-management',
      component: () => import('@/views/managementAccountant/ncv65/budgetSystem/IndicatorManagement'),
      name: 'IndicatorManagement',
      meta: {
        title: '指标管理',
        icon: 'el-icon-data-analysis'
      }
    },

    // 预算体系管理 - 预算模型管理
    {
      path: 'budget-system/budget-model',
      component: () => import('@/views/managementAccountant/ncv65/budgetSystem/BudgetModel'),
      name: 'BudgetModel',
      meta: {
        title: '预算模型管理',
        icon: 'el-icon-s-operation'
      }
    },

    // 预算体系管理 - 权限配置
    {
      path: 'budget-system/permission-configuration',
      component: () => import('@/views/managementAccountant/ncv65/budgetSystem/PermissionConfiguration'),
      name: 'PermissionConfiguration',
      meta: {
        title: '权限配置',
        icon: 'el-icon-lock'
      }
    },

    // 预算体系管理 - 数据集成
    {
      path: 'budget-system/data-integration',
      component: () => import('@/views/managementAccountant/ncv65/budgetSystem/DataIntegration'),
      name: 'BudgetDataIntegration',
      meta: {
        title: '数据集成',
        icon: 'el-icon-connection'
      }
    },

    // 预算体系管理 - 系统监控
    {
      path: 'budget-system/system-monitor',
      component: () => import('@/views/managementAccountant/ncv65/budgetSystem/SystemMonitor'),
      name: 'BudgetSystemMonitor',
      meta: {
        title: '系统监控',
        icon: 'el-icon-monitor'
      }
    },

    // 预算体系管理 - 审计跟踪
    {
      path: 'budget-system/audit-trail',
      component: () => import('@/views/managementAccountant/ncv65/budgetSystem/AuditTrail'),
      name: 'AuditTrail',
      meta: {
        title: '审计跟踪',
        icon: 'el-icon-document-checked'
      }
    },

    // 预算体系管理 - 备份恢复
    {
      path: 'budget-system/backup-restore',
      component: () => import('@/views/managementAccountant/ncv65/budgetSystem/BackupRestore'),
      name: 'BackupRestore',
      meta: {
        title: '备份恢复',
        icon: 'el-icon-upload'
      }
    },

    // 预算编制管理
    {
      path: 'budget-preparation',
      component: () => import('@/views/managementAccountant/ncv65/budgetPreparation'),
      name: 'BudgetPreparation',
      meta: {
        title: '预算编制管理',
        icon: 'el-icon-edit-outline'
      },
      children: [
        {
          path: 'task-management',
          component: () => import('@/views/managementAccountant/ncv65/budgetPreparation/TaskManagement'),
          name: 'TaskManagement',
          meta: {
            title: '预算任务管理',
            icon: 'el-icon-s-order'
          }
        },
        {
          path: 'budget-data-entry',
          component: () => import('@/views/managementAccountant/ncv65/budgetPreparation/BudgetDataEntry'),
          name: 'BudgetDataEntry',
          meta: {
            title: '预算数据编制',
            icon: 'el-icon-document'
          }
        },
        {
          path: 'approval-flow',
          component: () => import('@/views/managementAccountant/ncv65/budgetPreparation/ApprovalFlow'),
          name: 'ApprovalFlow',
          meta: {
            title: '预算审批流程',
            icon: 'el-icon-s-check'
          }
        },
        {
          path: 'budget-adjustment',
          component: () => import('@/views/managementAccountant/ncv65/budgetPreparation/BudgetAdjustment'),
          name: 'BudgetAdjustment',
          meta: {
            title: '预算调整',
            icon: 'el-icon-refresh'
          }
        },
        {
          path: 'budget-workflow',
          component: () => import('@/views/managementAccountant/ncv65/budgetPreparation/BudgetWorkflow'),
          name: 'BudgetWorkflow',
          meta: {
            title: '预算工作流管理',
            icon: 'el-icon-s-operation'
          }
        }
      ]
    },

    // 预算分析管理
    {
      path: 'budget-analysis',
      component: () => import('@/views/managementAccountant/ncv65/budgetAnalysis'),
      name: 'BudgetAnalysis',
      meta: {
        title: '预算分析管理',
        icon: 'el-icon-pie-chart'
      },
      children: [
        {
          path: 'comparison-analysis',
          component: () => import('@/views/managementAccountant/ncv65/budgetAnalysis/ComparisonAnalysis'),
          name: 'ComparisonAnalysis',
          meta: {
            title: '对比分析',
            icon: 'el-icon-s-data'
          }
        },
        {
          path: 'variance-analysis',
          component: () => import('@/views/managementAccountant/ncv65/budgetAnalysis/VarianceAnalysis'),
          name: 'VarianceAnalysis',
          meta: {
            title: '差异分析',
            icon: 'el-icon-trend-charts'
          }
        },
        {
          path: 'trend-analysis',
          component: () => import('@/views/managementAccountant/ncv65/budgetAnalysis/TrendAnalysis'),
          name: 'TrendAnalysis',
          meta: {
            title: '趋势分析',
            icon: 'el-icon-cpu'
          }
        },
        {
          path: 'performance-analysis',
          component: () => import('@/views/managementAccountant/ncv65/budgetAnalysis/PerformanceAnalysis'),
          name: 'PerformanceAnalysis',
          meta: {
            title: '绩效分析',
            icon: 'el-icon-medal'
          }
        },
        {
          path: 'forecast-analysis',
          component: () => import('@/views/managementAccountant/ncv65/budgetAnalysis/ForecastAnalysis'),
          name: 'ForecastAnalysis',
          meta: {
            title: '预测分析',
            icon: 'el-icon-magic-stick'
          }
        },
        {
          path: 'rolling-forecast',
          component: () => import('@/views/managementAccountant/ncv65/budgetAnalysis/RollingForecast'),
          name: 'RollingForecast',
          meta: {
            title: '滚动预测',
            icon: 'el-icon-refresh-right'
          }
        },
        {
          path: 'scenario-analysis',
          component: () => import('@/views/managementAccountant/ncv65/budgetAnalysis/ScenarioAnalysis'),
          name: 'ScenarioAnalysis',
          meta: {
            title: '场景分析',
            icon: 'el-icon-film'
          }
        },
        {
          path: 'sensitivity-analysis',
          component: () => import('@/views/managementAccountant/ncv65/budgetAnalysis/SensitivityAnalysis'),
          name: 'SensitivityAnalysis',
          meta: {
            title: '敏感性分析',
            icon: 'el-icon-discover'
          }
        },
        {
          path: 'analysis-dashboard',
          component: () => import('@/views/managementAccountant/ncv65/budgetAnalysis/AnalysisDashboard'),
          name: 'AnalysisDashboard',
          meta: {
            title: '分析仪表盘',
            icon: 'el-icon-odometer'
          }
        },
        {
          path: 'analysis-report',
          component: () => import('@/views/managementAccountant/ncv65/budgetAnalysis/AnalysisReport'),
          name: 'AnalysisReport',
          meta: {
            title: '分析报告',
            icon: 'el-icon-document'
          }
        },
        {
          path: 'analysis-chart',
          component: () => import('@/views/managementAccountant/ncv65/budgetAnalysis/AnalysisChart'),
          name: 'AnalysisChart',
          meta: {
            title: '图表管理',
            icon: 'el-icon-picture-outline'
          }
        }
      ]
    },

    // 预算控制管理
    {
      path: 'budget-control',
      component: () => import('@/views/managementAccountant/ncv65/budgetControl'),
      name: 'BudgetControl',
      meta: {
        title: '预算控制管理',
        icon: 'el-icon-lock'
      },
      children: [
        {
          path: 'budget-control',
          component: () => import('@/views/managementAccountant/ncv65/budgetControl/BudgetControl'),
          name: 'BudgetControl',
          meta: {
            title: '预算控制',
            icon: 'el-icon-s-tools'
          }
        },
        {
          path: 'budget-monitor',
          component: () => import('@/views/managementAccountant/ncv65/budgetControl/BudgetMonitor'),
          name: 'BudgetMonitor',
          meta: {
            title: '预算监控',
            icon: 'el-icon-lightning'
          }
        },
        {
          path: 'budget-warning',
          component: () => import('@/views/managementAccountant/ncv65/budgetControl/BudgetWarning'),
          name: 'BudgetWarning',
          meta: {
            title: '预算预警',
            icon: 'el-icon-warning'
          }
        },
        {
          path: 'budget-execution',
          component: () => import('@/views/managementAccountant/ncv65/budgetControl/BudgetExecution'),
          name: 'BudgetExecution',
          meta: {
            title: '预算执行',
            icon: 'el-icon-view'
          }
        }
      ]
    },

    // 高级功能 - 首页
    {
      path: 'advanced-features',
      component: () => import('@/views/managementAccountant/ncv65/advancedFeatures'),
      name: 'AdvancedFeatures',
      meta: {
        title: '高级功能',
        icon: 'el-icon-magic-stick'
      }
    },

    // 高级功能 - 管理页
    {
      path: 'advanced-features/home',
      component: () => import('@/views/managementAccountant/ncv65/advancedFeatures/Home.vue'),
      name: 'AdvancedFeaturesHome',
      meta: {
        title: '高级功能管理',
        icon: 'el-icon-s-home'
      }
    },

    // 高级功能 - 滚动预算
    {
      path: 'advanced-features/rolling-budget',
      component: () => import('@/views/managementAccountant/ncv65/advancedFeatures/RollingBudget.vue'),
      name: 'RollingBudget',
      meta: {
        title: '滚动预算',
        icon: 'el-icon-refresh-right'
      }
    },

    // 高级功能 - 公式追溯
    {
      path: 'advanced-features/formula-trace',
      component: () => import('@/views/managementAccountant/ncv65/advancedFeatures/FormulaTrace.vue'),
      name: 'FormulaTrace',
      meta: {
        title: '公式追溯',
        icon: 'el-icon-share'
      }
    },

    // 高级功能 - 催报管理
    {
      path: 'advanced-features/reminder-management',
      component: () => import('@/views/managementAccountant/ncv65/advancedFeatures/ReminderManagement.vue'),
      name: 'ReminderManagement',
      meta: {
        title: '催报管理',
        icon: 'el-icon-bell'
      }
    },

    // 高级功能 - 穿透查询
    {
      path: 'advanced-features/drill-through-query',
      component: () => import('@/views/managementAccountant/ncv65/advancedFeatures/DrillThroughQuery.vue'),
      name: 'DrillThroughQuery',
      meta: {
        title: '穿透查询',
        icon: 'el-icon-search'
      }
    },

    // 高级功能 - 多币种管理
    {
      path: 'advanced-features/currency-management',
      component: () => import('@/views/managementAccountant/ncv65/advancedFeatures/CurrencyManagement.vue'),
      name: 'CurrencyManagement',
      meta: {
        title: '多币种管理',
        icon: 'el-icon-money'
      }
    },

    // 高级功能 - 批量计算
    {
      path: 'advanced-features/batch-calculation',
      component: () => import('@/views/managementAccountant/ncv65/advancedFeatures/BatchCalculation.vue'),
      name: 'BatchCalculation',
      meta: {
        title: '批量计算',
        icon: 'el-icon-cpu'
      }
    },

    // 高级功能 - 智能推荐
    {
      path: 'advanced-features/intelligent-recommendation',
      component: () => import('@/views/managementAccountant/ncv65/advancedFeatures/IntelligentRecommendation.vue'),
      name: 'IntelligentRecommendation',
      meta: {
        title: '智能推荐',
        icon: 'el-icon-magic-stick'
      }
    },

    // 高级功能 - 预算模拟
    {
      path: 'advanced-features/budget-simulation',
      component: () => import('@/views/managementAccountant/ncv65/advancedFeatures/BudgetSimulation.vue'),
      name: 'BudgetSimulation',
      meta: {
        title: '预算模拟',
        icon: 'el-icon-data-analysis'
      }
    },

    // 高级功能 - 数据挖掘
    {
      path: 'advanced-features/data-mining',
      component: () => import('@/views/managementAccountant/ncv65/advancedFeatures/DataMining.vue'),
      name: 'DataMining',
      meta: {
        title: '数据挖掘',
        icon: 'el-icon-pie-chart'
      }
    },

    // 高级功能 - 预算优化
    {
      path: 'advanced-features/budget-optimization',
      component: () => import('@/views/managementAccountant/ncv65/advancedFeatures/BudgetOptimization.vue'),
      name: 'BudgetOptimization',
      meta: {
        title: '预算优化',
        icon: 'el-icon-s-opportunity'
      }
    },

    // 高级功能 - 风险评估
    {
      path: 'advanced-features/risk-assessment',
      component: () => import('@/views/managementAccountant/ncv65/advancedFeatures/RiskAssessment.vue'),
      name: 'RiskAssessment',
      meta: {
        title: '风险评估',
        icon: 'el-icon-warning'
      }
    },

    // 高级功能 - 协同编制
    {
      path: 'advanced-features/collaborative-budgeting',
      component: () => import('@/views/managementAccountant/ncv65/advancedFeatures/CollaborativeBudgeting.vue'),
      name: 'CollaborativeBudgeting',
      meta: {
        title: '协同编制',
        icon: 'el-icon-s-cooperation'
      }
    },

    // 高级功能 - 版本对比
    {
      path: 'advanced-features/version-comparison',
      component: () => import('@/views/managementAccountant/ncv65/advancedFeatures/VersionComparison.vue'),
      name: 'VersionComparison',
      meta: {
        title: '版本对比',
        icon: 'el-icon-document-copy'
      }
    },

    // 高级功能 - 自动化流程
    {
      path: 'advanced-features/automation-workflow',
      component: () => import('@/views/managementAccountant/ncv65/advancedFeatures/AutomationWorkflow.vue'),
      name: 'AutomationWorkflow',
      meta: {
        title: '自动化流程',
        icon: 'el-icon-setting'
      }
    },

    // 高级功能 - 高级报表
    {
      path: 'advanced-features/advanced-reports',
      component: () => import('@/views/managementAccountant/ncv65/advancedFeatures/AdvancedReports.vue'),
      name: 'AdvancedReports',
      meta: {
        title: '高级报表',
        icon: 'el-icon-document'
      }
    },

    // 系统集成
    {
      path: 'system-integration',
      component: () => import('@/views/managementAccountant/ncv65/systemIntegration'),
      name: 'SystemIntegration',
      meta: {
        title: '系统集成',
        icon: 'el-icon-connection'
      },
      children: [
        {
          path: 'erp-integration',
          component: () => import('@/views/managementAccountant/ncv65/systemIntegration/ErpIntegration'),
          name: 'ErpIntegration',
          meta: {
            title: 'ERP系统集成',
            icon: 'el-icon-link'
          }
        },
        {
          path: 'bi-integration',
          component: () => import('@/views/managementAccountant/ncv65/systemIntegration/BiIntegration'),
          name: 'BiIntegration',
          meta: {
            title: 'BI系统集成',
            icon: 'el-icon-data-line'
          }
        },
        {
          path: 'api-integration',
          component: () => import('@/views/managementAccountant/ncv65/systemIntegration/ApiIntegration'),
          name: 'ApiIntegration',
          meta: {
            title: 'API接口集成',
            icon: 'el-icon-paperclip'
          }
        }
      ]
    },

    // 注释掉暂未创建的模块
    /*
    // 数据管理
    {
      path: 'data-management',
      component: () => import('@/views/managementAccountant/ncv65/dataManagement'),
      name: 'DataManagement',
      meta: {
        title: '数据管理',
        icon: 'el-icon-folder-opened'
      }
    },

    // Excel客户端
    {
      path: 'excel-client',
      component: () => import('@/views/managementAccountant/ncv65/excelClient'),
      name: 'ExcelClient',
      meta: {
        title: 'Excel客户端',
        icon: 'el-icon-document-copy'
      }
    },

    // 预算查阅
    {
      path: 'budget-view',
      component: () => import('@/views/managementAccountant/ncv65/budgetView'),
      name: 'BudgetView',
      meta: {
        title: '预算查阅',
        icon: 'el-icon-view'
      }
    },

    // 系统管理
    {
      path: 'system-management',
      component: () => import('@/views/managementAccountant/ncv65/systemManagement'),
      name: 'SystemManagement',
      meta: {
        title: '系统管理',
        icon: 'el-icon-s-tools',
        roles: ['admin', 'system_manager']
      }
    }
    */
  ]
}

export default ncv65Router
