import Layout from '@/vab/layouts'

const tsRouter = {
  path: '/managementAccountant/ts',
  component: Layout,
  redirect: '/managementAccountant/ts/invoiceManagement',
  name: 'TaxServices',
  meta: {
    title: '税务服务',
    icon: 'el-icon-money',
    roles: ['admin', 'tax_manager', 'accountant']
  },
  children: [
    {
      path: 'invoiceManagement',
      component: () => import('@/views/managementAccountant/ts/invoiceManagement/index'),
      name: 'InvoiceManagement',
      meta: {
        title: '发票管理',
        icon: 'el-icon-document',
        roles: ['admin', 'tax_manager', 'accountant'],
        keepAlive: true
      }
    },
    {
      path: 'invoiceManagement/dashboard',
      component: () => import('@/views/managementAccountant/ts/invoiceManagement/InvoiceManagementDashboard'),
      name: 'InvoiceManagementDashboard',
      meta: {
        title: '发票管理仪表板',
        icon: 'el-icon-data-analysis',
        roles: ['admin', 'tax_manager', 'accountant'],
        hidden: true,
        activeMenu: '/managementAccountant/ts/invoiceManagement'
      }
    },
    {
      path: 'invoiceManagement/list',
      component: () => import('@/views/managementAccountant/ts/invoiceManagement/InvoiceManagementList'),
      name: 'InvoiceManagementList',
      meta: {
        title: '发票列表',
        icon: 'el-icon-document',
        roles: ['admin', 'tax_manager', 'accountant'],
        hidden: true,
        activeMenu: '/managementAccountant/ts/invoiceManagement'
      }
    },
    {
      path: 'invoiceManagement/detail/:id?',
      component: () => import('@/views/managementAccountant/ts/invoiceManagement/InvoiceManagementDetail'),
      name: 'InvoiceManagementDetail',
      meta: {
        title: '发票详情',
        icon: 'el-icon-view',
        roles: ['admin', 'tax_manager', 'accountant'],
        hidden: true,
        activeMenu: '/managementAccountant/ts/invoiceManagement'
      }
    },
    {
      path: 'taxDeclaration',
      component: () => import('@/views/managementAccountant/ts/taxDeclaration/index'),
      name: 'TaxDeclaration',
      meta: {
        title: '税务申报',
        icon: 'el-icon-s-order',
        roles: ['admin', 'tax_manager', 'accountant'],
        keepAlive: true
      }
    },
    {
      path: 'taxPlanning',
      component: () => import('@/views/managementAccountant/ts/taxPlanning/index'),
      name: 'TaxPlanning',
      meta: {
        title: '税务筹划',
        icon: 'el-icon-s-marketing',
        roles: ['admin', 'tax_manager', 'accountant'],
        keepAlive: true
      }
    },
    {
      path: 'taxCompliance',
      component: () => import('@/views/managementAccountant/ts/taxCompliance/index'),
      name: 'TaxCompliance',
      meta: {
        title: '税务合规',
        icon: 'el-icon-s-check',
        roles: ['admin', 'tax_manager', 'accountant'],
        keepAlive: true
      }
    },
    {
      path: 'taxRisk',
      component: () => import('@/views/managementAccountant/ts/taxRisk/index'),
      name: 'TaxRisk',
      meta: {
        title: '税务风险',
        icon: 'el-icon-warning',
        roles: ['admin', 'tax_manager', 'accountant'],
        keepAlive: true
      }
    },
    {
      path: 'taxAudit',
      component: () => import('@/views/managementAccountant/ts/taxAudit/index'),
      name: 'TaxAudit',
      meta: {
        title: '税务稽查',
        icon: 'el-icon-search',
        roles: ['admin', 'tax_manager', 'accountant'],
        keepAlive: true
      }
    },
    {
      path: 'taxReporting',
      component: () => import('@/views/managementAccountant/ts/taxReporting/index'),
      name: 'TaxReporting',
      meta: {
        title: '税务报告',
        icon: 'el-icon-document-copy',
        roles: ['admin', 'tax_manager', 'accountant'],
        keepAlive: true
      }
    },
    {
      path: 'taxAnalytics',
      component: () => import('@/views/managementAccountant/ts/taxAnalytics/index'),
      name: 'TaxAnalytics',
      meta: {
        title: '税务分析',
        icon: 'el-icon-pie-chart',
        roles: ['admin', 'tax_manager', 'accountant'],
        keepAlive: true
      }
    },
    {
      path: 'taxSettings',
      component: () => import('@/views/managementAccountant/ts/taxSettings/index'),
      name: 'TaxSettings',
      meta: {
        title: '税务设置',
        icon: 'el-icon-setting',
        roles: ['admin', 'tax_manager'],
        keepAlive: true
      }
    }
  ]
}

export default tsRouter
