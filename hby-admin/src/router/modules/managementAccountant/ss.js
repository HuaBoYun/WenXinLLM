import Layout from '@/vab/layouts'

const ssRouter = {
  path: '/management-accountant/ss',
  component: Layout,
  redirect: '/management-accountant/ss/intelligent-audit',
  name: 'ManagementAccountantSS',
  meta: {
    title: '共享服务',
    icon: 'el-icon-share',
    roles: ['admin', 'management_accountant']
  },
  children: [
    {
      path: 'intelligent-audit',
      component: () => import('@/views/managementAccountant/ss/intelligentAudit'),
      name: 'IntelligentAudit',
      meta: {
        title: '智能审核',
        icon: 'el-icon-cpu',
        roles: ['admin', 'management_accountant']
      },
      redirect: '/management-accountant/ss/intelligent-audit/dashboard',
      children: [
        {
          path: 'dashboard',
          component: () => import('@/views/managementAccountant/ss/intelligentAudit/IntelligentAuditDashboard'),
          name: 'IntelligentAuditDashboard',
          meta: {
            title: '智能审核仪表板',
            icon: 'el-icon-data-analysis',
            roles: ['admin', 'management_accountant']
          }
        },
        {
          path: 'list',
          component: () => import('@/views/managementAccountant/ss/intelligentAudit/IntelligentAuditList'),
          name: 'IntelligentAuditList',
          meta: {
            title: '智能审核列表',
            icon: 'el-icon-document',
            roles: ['admin', 'management_accountant']
          }
        },
        {
          path: 'detail/:id',
          component: () => import('@/views/managementAccountant/ss/intelligentAudit/IntelligentAuditDetail'),
          name: 'IntelligentAuditDetail',
          meta: {
            title: '智能审核详情',
            icon: 'el-icon-view',
            roles: ['admin', 'management_accountant'],
            hidden: true,
            activeMenu: '/management-accountant/ss/intelligent-audit/list'
          }
        },
        {
          path: 'create',
          component: () => import('@/views/managementAccountant/ss/intelligentAudit/IntelligentAuditForm'),
          name: 'IntelligentAuditCreate',
          meta: {
            title: '新建智能审核',
            icon: 'el-icon-plus',
            roles: ['admin', 'management_accountant'],
            hidden: true,
            activeMenu: '/management-accountant/ss/intelligent-audit/list'
          }
        },
        {
          path: 'edit/:id',
          component: () => import('@/views/managementAccountant/ss/intelligentAudit/IntelligentAuditForm'),
          name: 'IntelligentAuditEdit',
          meta: {
            title: '编辑智能审核',
            icon: 'el-icon-edit',
            roles: ['admin', 'management_accountant'],
            hidden: true,
            activeMenu: '/management-accountant/ss/intelligent-audit/list'
          }
        }
      ]
    },
    {
      path: 'job-schedule',
      component: () => import('@/views/managementAccountant/ss/jobSchedule'),
      name: 'JobSchedule',
      meta: {
        title: '作业调度',
        icon: 'el-icon-time',
        roles: ['admin', 'management_accountant']
      },
      redirect: '/management-accountant/ss/job-schedule/dashboard',
      children: [
        {
          path: 'dashboard',
          component: () => import('@/views/managementAccountant/ss/jobSchedule/JobScheduleDashboard'),
          name: 'JobScheduleDashboard',
          meta: {
            title: '作业调度仪表板',
            icon: 'el-icon-data-analysis',
            roles: ['admin', 'management_accountant']
          }
        },
        {
          path: 'list',
          component: () => import('@/views/managementAccountant/ss/jobSchedule/JobScheduleList'),
          name: 'JobScheduleList',
          meta: {
            title: '作业调度列表',
            icon: 'el-icon-document',
            roles: ['admin', 'management_accountant']
          }
        },
        {
          path: 'detail/:id',
          component: () => import('@/views/managementAccountant/ss/jobSchedule/JobScheduleDetail'),
          name: 'JobScheduleDetail',
          meta: {
            title: '作业调度详情',
            icon: 'el-icon-view',
            roles: ['admin', 'management_accountant'],
            hidden: true,
            activeMenu: '/management-accountant/ss/job-schedule/list'
          }
        },
        {
          path: 'create',
          component: () => import('@/views/managementAccountant/ss/jobSchedule/JobScheduleForm'),
          name: 'JobScheduleCreate',
          meta: {
            title: '新建作业调度',
            icon: 'el-icon-plus',
            roles: ['admin', 'management_accountant'],
            hidden: true,
            activeMenu: '/management-accountant/ss/job-schedule/list'
          }
        },
        {
          path: 'edit/:id',
          component: () => import('@/views/managementAccountant/ss/jobSchedule/JobScheduleForm'),
          name: 'JobScheduleEdit',
          meta: {
            title: '编辑作业调度',
            icon: 'el-icon-edit',
            roles: ['admin', 'management_accountant'],
            hidden: true,
            activeMenu: '/management-accountant/ss/job-schedule/list'
          }
        }
      ]
    },
    {
      path: 'quality-control',
      component: () => import('@/views/managementAccountant/ss/qualityControl'),
      name: 'QualityControl',
      meta: {
        title: '质量管控',
        icon: 'el-icon-medal',
        roles: ['admin', 'management_accountant']
      },
      redirect: '/management-accountant/ss/quality-control/dashboard',
      children: [
        {
          path: 'dashboard',
          component: () => import('@/views/managementAccountant/ss/qualityControl/QualityControlDashboard'),
          name: 'QualityControlDashboard',
          meta: {
            title: '质量管控仪表板',
            icon: 'el-icon-data-analysis',
            roles: ['admin', 'management_accountant']
          }
        },
        {
          path: 'list',
          component: () => import('@/views/managementAccountant/ss/qualityControl/QualityControlList'),
          name: 'QualityControlList',
          meta: {
            title: '质量管控列表',
            icon: 'el-icon-document',
            roles: ['admin', 'management_accountant']
          }
        },
        {
          path: 'detail/:id',
          component: () => import('@/views/managementAccountant/ss/qualityControl/QualityControlDetail'),
          name: 'QualityControlDetail',
          meta: {
            title: '质量管控详情',
            icon: 'el-icon-view',
            roles: ['admin', 'management_accountant'],
            hidden: true,
            activeMenu: '/management-accountant/ss/quality-control/list'
          }
        },
        {
          path: 'create',
          component: () => import('@/views/managementAccountant/ss/qualityControl/QualityControlForm'),
          name: 'QualityControlCreate',
          meta: {
            title: '新建质量管控',
            icon: 'el-icon-plus',
            roles: ['admin', 'management_accountant'],
            hidden: true,
            activeMenu: '/management-accountant/ss/quality-control/list'
          }
        },
        {
          path: 'edit/:id',
          component: () => import('@/views/managementAccountant/ss/qualityControl/QualityControlForm'),
          name: 'QualityControlEdit',
          meta: {
            title: '编辑质量管控',
            icon: 'el-icon-edit',
            roles: ['admin', 'management_accountant'],
            hidden: true,
            activeMenu: '/management-accountant/ss/quality-control/list'
          }
        }
      ]
    },
    {
      path: 'cost-allocation',
      component: () => import('@/views/managementAccountant/ss/costAllocation'),
      name: 'CostAllocation',
      meta: {
        title: '成本分摊',
        icon: 'el-icon-money',
        roles: ['admin', 'management_accountant']
      },
      redirect: '/management-accountant/ss/cost-allocation/dashboard',
      children: [
        {
          path: 'dashboard',
          component: () => import('@/views/managementAccountant/ss/costAllocation/CostAllocationDashboard'),
          name: 'CostAllocationDashboard',
          meta: {
            title: '成本分摊仪表板',
            icon: 'el-icon-data-analysis',
            roles: ['admin', 'management_accountant']
          }
        },
        {
          path: 'list',
          component: () => import('@/views/managementAccountant/ss/costAllocation/CostAllocationList'),
          name: 'CostAllocationList',
          meta: {
            title: '成本分摊列表',
            icon: 'el-icon-document',
            roles: ['admin', 'management_accountant']
          }
        },
        {
          path: 'detail/:id',
          component: () => import('@/views/managementAccountant/ss/costAllocation/CostAllocationDetail'),
          name: 'CostAllocationDetail',
          meta: {
            title: '成本分摊详情',
            icon: 'el-icon-view',
            roles: ['admin', 'management_accountant'],
            hidden: true,
            activeMenu: '/management-accountant/ss/cost-allocation/list'
          }
        },
        {
          path: 'create',
          component: () => import('@/views/managementAccountant/ss/costAllocation/CostAllocationForm'),
          name: 'CostAllocationCreate',
          meta: {
            title: '新建成本分摊',
            icon: 'el-icon-plus',
            roles: ['admin', 'management_accountant'],
            hidden: true,
            activeMenu: '/management-accountant/ss/cost-allocation/list'
          }
        },
        {
          path: 'edit/:id',
          component: () => import('@/views/managementAccountant/ss/costAllocation/CostAllocationForm'),
          name: 'CostAllocationEdit',
          meta: {
            title: '编辑成本分摊',
            icon: 'el-icon-edit',
            roles: ['admin', 'management_accountant'],
            hidden: true,
            activeMenu: '/management-accountant/ss/cost-allocation/list'
          }
        }
      ]
    },
    {
      path: '/digitalEmployee',
      name: 'SsDigitalEmployee',
      component: () => import('@/views/managementAccountant/ss/digitalEmployee/index'),
      meta: { title: '数字员工', icon: 'el-icon-cpu' },
      children: [
        {
          path: 'list',
          name: 'SsDigitalEmployeeList',
          component: () => import('@/views/managementAccountant/ss/digitalEmployee/DigitalEmployeeList'),
          meta: { title: '数字员工列表', icon: 'el-icon-menu' }
        },
        {
          path: 'dashboard',
          name: 'SsDigitalEmployeeDashboard',
          component: () => import('@/views/managementAccountant/ss/digitalEmployee/DigitalEmployeeDashboard'),
          meta: { title: '数据仪表板', icon: 'el-icon-data-analysis' }
        },
        {
          path: 'create',
          name: 'SsDigitalEmployeeCreate',
          component: () => import('@/views/managementAccountant/ss/digitalEmployee/DigitalEmployeeDetail'),
          meta: { title: '新增数字员工', icon: 'el-icon-plus' },
          hidden: true
        },
        {
          path: 'detail/:robotId',
          name: 'SsDigitalEmployeeDetail',
          component: () => import('@/views/managementAccountant/ss/digitalEmployee/DigitalEmployeeDetail'),
          meta: { title: '数字员工详情', icon: 'el-icon-view' },
          hidden: true
        },
        {
          path: 'edit/:robotId',
          name: 'SsDigitalEmployeeEdit',
          component: () => import('@/views/managementAccountant/ss/digitalEmployee/DigitalEmployeeDetail'),
          meta: { title: '编辑数字员工', icon: 'el-icon-edit' },
          hidden: true
        }
      ]
    },
    {
      path: 'service-portal',
      component: () => import('@/views/managementAccountant/ss/servicePortal'),
      name: 'ServicePortal',
      meta: {
        title: '服务门户',
        icon: 'el-icon-s-grid',
        roles: ['admin', 'management_accountant']
      },
      redirect: '/management-accountant/ss/service-portal/dashboard',
      children: [
        {
          path: 'dashboard',
          component: () => import('@/views/managementAccountant/ss/servicePortal/ServicePortalDashboard'),
          name: 'ServicePortalDashboard',
          meta: {
            title: '服务门户仪表板',
            icon: 'el-icon-data-analysis',
            roles: ['admin', 'management_accountant']
          }
        },
        {
          path: 'list',
          component: () => import('@/views/managementAccountant/ss/servicePortal/ServicePortalList'),
          name: 'ServicePortalList',
          meta: {
            title: '服务门户列表',
            icon: 'el-icon-document',
            roles: ['admin', 'management_accountant']
          }
        },
        {
          path: 'detail/:portalId',
          name: 'ServicePortalDetail',
          component: () => import('@/views/managementAccountant/ss/servicePortal/ServicePortalDetail'),
          meta: {
            title: '服务门户详情',
            icon: 'el-icon-view',
            roles: ['admin', 'management_accountant'],
            hidden: true,
            activeMenu: '/management-accountant/ss/service-portal/list'
          }
        },
        {
          path: 'create',
          component: () => import('@/views/managementAccountant/ss/servicePortal/ServicePortalDetail'),
          name: 'ServicePortalCreate',
          meta: {
            title: '新建服务门户',
            icon: 'el-icon-plus',
            roles: ['admin', 'management_accountant'],
            hidden: true,
            activeMenu: '/management-accountant/ss/service-portal/list'
          }
        },
        {
          path: 'edit/:portalId',
          component: () => import('@/views/managementAccountant/ss/servicePortal/ServicePortalDetail'),
          name: 'ServicePortalEdit',
          meta: {
            title: '编辑服务门户',
            icon: 'el-icon-edit',
            roles: ['admin', 'management_accountant'],
            hidden: true,
            activeMenu: '/management-accountant/ss/service-portal/list'
          }
        }
      ]
    }
  ]
}

export default ssRouter
