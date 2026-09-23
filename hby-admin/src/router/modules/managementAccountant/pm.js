import Layout from '@/vab/layouts'

const pmRouter = {
  path: '/pm',
  component: Layout,
  redirect: '/pm/index',
  name: 'PerformanceManagement',
  meta: {
    title: '绩效管理',
    icon: 'el-icon-trophy'
  },
  children: [
    {
      path: 'index',
      component: () => import('@/views/managementAccountant/pm/index'),
      name: 'PerformanceManagementIndex',
      meta: {
        title: '绩效管理概览',
        icon: 'el-icon-data-board'
      }
    },
    {
      path: 'target-management',
      component: () => import('@/views/managementAccountant/pm/targetManagement/index'),
      name: 'TargetManagement',
      meta: {
        title: '目标管理',
        icon: 'el-icon-aim'
      }
    },
    {
      path: 'target-management/list',
      component: () => import('@/views/managementAccountant/pm/targetManagement/TargetManagementList'),
      name: 'TargetManagementList',
      meta: {
        title: '目标列表',
        hidden: true,
        activeMenu: '/pm/target-management'
      }
    },
    {
      path: 'target-dashboard',
      component: () => import('@/views/managementAccountant/pm/targetManagement/TargetManagementDashboard'),
      name: 'TargetManagementDashboard',
      meta: {
        title: '目标仪表板',
        icon: 'el-icon-data-analysis'
      }
    },
    {
      path: 'target-management/create',
      component: () => import('@/views/managementAccountant/pm/targetManagement/TargetManagementDetail'),
      name: 'TargetManagementCreate',
      meta: {
        title: '新建目标',
        hidden: true,
        activeMenu: '/pm/target-management'
      }
    },
    {
      path: 'target-management/edit/:id',
      component: () => import('@/views/managementAccountant/pm/targetManagement/TargetManagementDetail'),
      name: 'TargetManagementEdit',
      meta: {
        title: '编辑目标',
        hidden: true,
        activeMenu: '/pm/target-management'
      }
    },
    {
      path: 'target-management/detail/:id',
      component: () => import('@/views/managementAccountant/pm/targetManagement/TargetManagementDetail'),
      name: 'TargetManagementDetail',
      meta: {
        title: '目标详情',
        hidden: true,
        activeMenu: '/pm/target-management'
      }
    },
    {
      path: 'assessment-plan',
      component: () => import('@/views/managementAccountant/pm/assessmentPlan/index'),
      name: 'AssessmentPlan',
      meta: {
        title: '考核方案配置',
        icon: 'el-icon-document-checked'
      }
    },
    {
      path: 'assessment-plan/list',
      component: () => import('@/views/managementAccountant/pm/assessmentPlan/AssessmentPlanList'),
      name: 'AssessmentPlanList',
      meta: {
        title: '考核方案列表',
        hidden: true,
        activeMenu: '/pm/assessment-plan'
      }
    },
    {
      path: 'assessment-plan/create',
      component: () => import('@/views/managementAccountant/pm/assessmentPlan/AssessmentPlanDetail'),
      name: 'AssessmentPlanCreate',
      meta: {
        title: '新建考核方案',
        hidden: true,
        activeMenu: '/pm/assessment-plan'
      }
    },
    {
      path: 'assessment-plan/edit/:id',
      component: () => import('@/views/managementAccountant/pm/assessmentPlan/AssessmentPlanDetail'),
      name: 'AssessmentPlanEdit',
      meta: {
        title: '编辑考核方案',
        hidden: true,
        activeMenu: '/pm/assessment-plan'
      }
    },
    {
      path: 'assessment-plan/detail/:id',
      component: () => import('@/views/managementAccountant/pm/assessmentPlan/AssessmentPlanDetail'),
      name: 'AssessmentPlanDetail',
      meta: {
        title: '考核方案详情',
        hidden: true,
        activeMenu: '/pm/assessment-plan'
      }
    },
    {
      path: '360-assessment',
      component: () => import('@/views/managementAccountant/pm/assessment360/index'),
      name: 'Assessment360',
      meta: {
        title: '360度评估',
        icon: 'el-icon-view'
      }
    },
    {
      path: '360-assessment/list',
      component: () => import('@/views/managementAccountant/pm/assessment360/Assessment360List'),
      name: 'Assessment360List',
      meta: {
        title: '360度评估列表',
        hidden: true,
        activeMenu: '/pm/360-assessment'
      }
    },
    {
      path: '360-assessment/dashboard',
      component: () => import('@/views/managementAccountant/pm/assessment360/Assessment360Dashboard'),
      name: 'Assessment360Dashboard',
      meta: {
        title: '360度评估仪表板',
        icon: 'el-icon-data-analysis'
      }
    },
    {
      path: '360-assessment/create',
      component: () => import('@/views/managementAccountant/pm/assessment360/Assessment360Detail'),
      name: 'Assessment360Create',
      meta: {
        title: '新建360度评估',
        hidden: true,
        activeMenu: '/pm/360-assessment'
      }
    },
    {
      path: '360-assessment/edit/:id',
      component: () => import('@/views/managementAccountant/pm/assessment360/Assessment360Detail'),
      name: 'Assessment360Edit',
      meta: {
        title: '编辑360度评估',
        hidden: true,
        activeMenu: '/pm/360-assessment'
      }
    },
    {
      path: '360-assessment/detail/:id',
      component: () => import('@/views/managementAccountant/pm/assessment360/Assessment360Detail'),
      name: 'Assessment360Detail',
      meta: {
        title: '360度评估详情',
        hidden: true,
        activeMenu: '/pm/360-assessment'
      }
    },
    {
      path: '360-assessment/evaluation/:id',
      component: () => import('@/views/managementAccountant/pm/assessment360/Assessment360Evaluation'),
      name: 'Assessment360Evaluation',
      meta: {
        title: '360度评估执行',
        hidden: true,
        activeMenu: '/pm/360-assessment'
      }
    },
    {
      path: 'assessment-plan/wizard',
      component: () => import('@/views/managementAccountant/pm/assessmentPlan/AssessmentPlanWizard'),
      name: 'AssessmentPlanWizard',
      meta: {
        title: '考核方案配置向导',
        hidden: true,
        activeMenu: '/pm/assessment-plan'
      }
    },

    {
      path: 'performance-interview',
      component: () => import('@/views/managementAccountant/pm/performanceInterview/index'),
      name: 'PerformanceInterview',
      meta: {
        title: '绩效面谈管理',
        icon: 'el-icon-chat-dot-round'
      }
    },
    {
      path: 'performance-interview/list',
      component: () => import('@/views/managementAccountant/pm/performanceInterview/PerformanceInterviewList'),
      name: 'PerformanceInterviewList',
      meta: {
        title: '绩效面谈列表',
        hidden: true,
        activeMenu: '/pm/performance-interview'
      }
    },
    {
      path: 'performance-interview/dashboard',
      component: () => import('@/views/managementAccountant/pm/performanceInterview/PerformanceInterviewDashboard'),
      name: 'PerformanceInterviewDashboard',
      meta: {
        title: '绩效面谈仪表板',
        icon: 'el-icon-data-analysis'
      }
    },
    {
      path: 'performance-interview/create',
      component: () => import('@/views/managementAccountant/pm/performanceInterview/PerformanceInterviewDetail'),
      name: 'PerformanceInterviewCreate',
      meta: {
        title: '新建绩效面谈',
        hidden: true,
        activeMenu: '/pm/performance-interview'
      }
    },
    {
      path: 'performance-interview/edit/:id',
      component: () => import('@/views/managementAccountant/pm/performanceInterview/PerformanceInterviewDetail'),
      name: 'PerformanceInterviewEdit',
      meta: {
        title: '编辑绩效面谈',
        hidden: true,
        activeMenu: '/pm/performance-interview'
      }
    },
    {
      path: 'performance-interview/detail/:id',
      component: () => import('@/views/managementAccountant/pm/performanceInterview/PerformanceInterviewDetail'),
      name: 'PerformanceInterviewDetail',
      meta: {
        title: '绩效面谈详情',
        hidden: true,
        activeMenu: '/pm/performance-interview'
      }
    },
    {
      path: 'performance-calibration',
      component: () => import('@/views/managementAccountant/pm/performanceCalibration/index'),
      name: 'PerformanceCalibration',
      meta: {
        title: '绩效校准管理',
        icon: 'el-icon-scale-to-original'
      }
    },
    {
      path: 'performance-calibration/list',
      component: () => import('@/views/managementAccountant/pm/performanceCalibration/PerformanceCalibrationList'),
      name: 'PerformanceCalibrationList',
      meta: {
        title: '绩效校准列表',
        hidden: true,
        activeMenu: '/pm/performance-calibration'
      }
    },
    {
      path: 'performance-calibration/dashboard',
      component: () => import('@/views/managementAccountant/pm/performanceCalibration/PerformanceCalibrationDashboard'),
      name: 'PerformanceCalibrationDashboard',
      meta: {
        title: '绩效校准仪表板',
        icon: 'el-icon-data-analysis'
      }
    },
    {
      path: 'performance-calibration/create',
      component: () => import('@/views/managementAccountant/pm/performanceCalibration/PerformanceCalibrationDetail'),
      name: 'PerformanceCalibrationCreate',
      meta: {
        title: '新建绩效校准',
        hidden: true,
        activeMenu: '/pm/performance-calibration'
      }
    },
    {
      path: 'performance-calibration/edit/:id',
      component: () => import('@/views/managementAccountant/pm/performanceCalibration/PerformanceCalibrationDetail'),
      name: 'PerformanceCalibrationEdit',
      meta: {
        title: '编辑绩效校准',
        hidden: true,
        activeMenu: '/pm/performance-calibration'
      }
    },
    {
      path: 'performance-calibration/detail/:id',
      component: () => import('@/views/managementAccountant/pm/performanceCalibration/PerformanceCalibrationDetail'),
      name: 'PerformanceCalibrationDetail',
      meta: {
        title: '绩效校准详情',
        hidden: true,
        activeMenu: '/pm/performance-calibration'
      }
    },
    {
      path: 'incentive-management',
      component: () => import('@/views/managementAccountant/pm/incentiveManagement/index'),
      name: 'IncentiveManagement',
      meta: {
        title: '激励管理',
        icon: 'el-icon-medal'
      }
    },
    {
      path: 'incentive-management/list',
      component: () => import('@/views/managementAccountant/pm/incentiveManagement/IncentiveManagementList'),
      name: 'IncentiveManagementList',
      meta: {
        title: '激励管理列表',
        hidden: true,
        activeMenu: '/pm/incentive-management'
      }
    },
    {
      path: 'incentive-management/dashboard',
      component: () => import('@/views/managementAccountant/pm/incentiveManagement/IncentiveManagementDashboard'),
      name: 'IncentiveManagementDashboard',
      meta: {
        title: '激励仪表板',
        icon: 'el-icon-data-analysis'
      }
    },
    {
      path: 'incentive-management/create',
      component: () => import('@/views/managementAccountant/pm/incentiveManagement/IncentiveManagementDetail'),
      name: 'IncentiveManagementCreate',
      meta: {
        title: '新建激励方案',
        hidden: true,
        activeMenu: '/pm/incentive-management'
      }
    },
    {
      path: 'incentive-management/edit/:id',
      component: () => import('@/views/managementAccountant/pm/incentiveManagement/IncentiveManagementDetail'),
      name: 'IncentiveManagementEdit',
      meta: {
        title: '编辑激励方案',
        hidden: true,
        activeMenu: '/pm/incentive-management'
      }
    },
    {
      path: 'incentive-management/detail/:id',
      component: () => import('@/views/managementAccountant/pm/incentiveManagement/IncentiveManagementDetail'),
      name: 'IncentiveManagementDetail',
      meta: {
        title: '激励方案详情',
        hidden: true,
        activeMenu: '/pm/incentive-management'
      }
    }
  ]
}

export default pmRouter
