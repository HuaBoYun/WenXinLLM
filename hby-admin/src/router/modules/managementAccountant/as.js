import Layout from '@/vab/layouts'

const asRouter = {
  path: '/management-accountant/as',
  component: Layout,
  redirect: '/management-accountant/as/intelligent-classification',
  name: 'ArchiveServices',
  meta: {
    title: '档案服务',
    icon: 'el-icon-folder-opened',
    roles: ['admin', 'manager', 'user']
  },
  children: [
    {
      path: 'intelligent-classification',
      component: () => import('@/views/managementAccountant/as/intelligentClassification/index'),
      name: 'IntelligentClassification',
      meta: {
        title: '智能分类',
        icon: 'el-icon-cpu',
        roles: ['admin', 'manager', 'user']
      }
    },
    {
      path: 'intelligent-classification/list',
      component: () => import('@/views/managementAccountant/as/intelligentClassification/IntelligentClassificationList'),
      name: 'IntelligentClassificationList',
      meta: {
        title: '分类列表',
        icon: 'el-icon-menu',
        roles: ['admin', 'manager', 'user'],
        activeMenu: '/management-accountant/as/intelligent-classification'
      },
      hidden: true
    },
    {
      path: 'intelligent-classification/dashboard',
      component: () => import('@/views/managementAccountant/as/intelligentClassification/IntelligentClassificationDashboard'),
      name: 'IntelligentClassificationDashboard',
      meta: {
        title: '分类仪表板',
        icon: 'el-icon-data-board',
        roles: ['admin', 'manager', 'user'],
        activeMenu: '/management-accountant/as/intelligent-classification'
      },
      hidden: true
    },
    {
      path: 'intelligent-classification/detail/:id',
      component: () => import('@/views/managementAccountant/as/intelligentClassification/IntelligentClassificationDetail'),
      name: 'IntelligentClassificationDetail',
      meta: {
        title: '分类详情',
        icon: 'el-icon-document',
        roles: ['admin', 'manager', 'user'],
        activeMenu: '/management-accountant/as/intelligent-classification'
      },
      hidden: true
    },
    {
      path: 'document-version',
      component: () => import('@/views/managementAccountant/as/documentVersion/index'),
      name: 'DocumentVersion',
      meta: {
        title: '文档版本',
        icon: 'el-icon-document-copy',
        roles: ['admin', 'manager', 'user']
      }
    },
    {
      path: 'document-version/list',
      component: () => import('@/views/managementAccountant/as/documentVersion/DocumentVersionList'),
      name: 'DocumentVersionList',
      meta: {
        title: '版本列表',
        icon: 'el-icon-menu',
        roles: ['admin', 'manager', 'user'],
        activeMenu: '/management-accountant/as/document-version'
      },
      hidden: true
    },
    {
      path: 'document-version/dashboard',
      component: () => import('@/views/managementAccountant/as/documentVersion/DocumentVersionDashboard'),
      name: 'DocumentVersionDashboard',
      meta: {
        title: '版本仪表板',
        icon: 'el-icon-data-board',
        roles: ['admin', 'manager', 'user'],
        activeMenu: '/management-accountant/as/document-version'
      },
      hidden: true
    },
    {
      path: 'document-version/detail/:id',
      component: () => import('@/views/managementAccountant/as/documentVersion/DocumentVersionDetail'),
      name: 'DocumentVersionDetail',
      meta: {
        title: '版本详情',
        icon: 'el-icon-document',
        roles: ['admin', 'manager', 'user'],
        activeMenu: '/management-accountant/as/document-version'
      },
      hidden: true
    },
    {
      path: 'document-version/comparison',
      component: () => import('@/views/managementAccountant/as/documentVersion/DocumentVersionComparison'),
      name: 'DocumentVersionComparison',
      meta: {
        title: '版本对比',
        icon: 'el-icon-copy-document',
        roles: ['admin', 'manager', 'user'],
        activeMenu: '/management-accountant/as/document-version'
      },
      hidden: true
    },
    {
      path: 'archive-search',
      component: () => import('@/views/managementAccountant/as/archiveSearch/index'),
      name: 'ArchiveSearch',
      meta: {
        title: '档案检索',
        icon: 'el-icon-search',
        roles: ['admin', 'manager', 'user']
      }
    },
    {
      path: 'archive-search/list',
      component: () => import('@/views/managementAccountant/as/archiveSearch/ArchiveSearchList'),
      name: 'ArchiveSearchList',
      meta: {
        title: '检索列表',
        icon: 'el-icon-menu',
        roles: ['admin', 'manager', 'user'],
        activeMenu: '/management-accountant/as/archive-search'
      },
      hidden: true
    },
    {
      path: 'archive-search/dashboard',
      component: () => import('@/views/managementAccountant/as/archiveSearch/ArchiveSearchDashboard'),
      name: 'ArchiveSearchDashboard',
      meta: {
        title: '检索仪表板',
        icon: 'el-icon-data-board',
        roles: ['admin', 'manager', 'user'],
        activeMenu: '/management-accountant/as/archive-search'
      },
      hidden: true
    },
    {
      path: 'archive-search/detail/:id',
      component: () => import('@/views/managementAccountant/as/archiveSearch/ArchiveSearchDetail'),
      name: 'ArchiveSearchDetail',
      meta: {
        title: '检索详情',
        icon: 'el-icon-document',
        roles: ['admin', 'manager', 'user'],
        activeMenu: '/management-accountant/as/archive-search'
      },
      hidden: true
    },
    {
      path: 'archive-permission',
      component: () => import('@/views/managementAccountant/as/archivePermission/index'),
      name: 'ArchivePermission',
      meta: {
        title: '档案权限',
        icon: 'el-icon-key',
        roles: ['admin', 'manager', 'user']
      }
    },
    {
      path: 'archive-permission/list',
      component: () => import('@/views/managementAccountant/as/archivePermission/ArchivePermissionList'),
      name: 'ArchivePermissionList',
      meta: {
        title: '权限列表',
        icon: 'el-icon-menu',
        roles: ['admin', 'manager', 'user'],
        activeMenu: '/management-accountant/as/archive-permission'
      },
      hidden: true
    },
    {
      path: 'archive-permission/dashboard',
      component: () => import('@/views/managementAccountant/as/archivePermission/ArchivePermissionDashboard'),
      name: 'ArchivePermissionDashboard',
      meta: {
        title: '权限仪表板',
        icon: 'el-icon-data-board',
        roles: ['admin', 'manager', 'user'],
        activeMenu: '/management-accountant/as/archive-permission'
      },
      hidden: true
    },
    {
      path: 'archive-permission/detail/:id',
      component: () => import('@/views/managementAccountant/as/archivePermission/ArchivePermissionDetail'),
      name: 'ArchivePermissionDetail',
      meta: {
        title: '权限详情',
        icon: 'el-icon-document',
        roles: ['admin', 'manager', 'user'],
        activeMenu: '/management-accountant/as/archive-permission'
      },
      hidden: true
    },
    {
      path: 'archive-permission/audit',
      component: () => import('@/views/managementAccountant/as/archivePermission/ArchivePermissionAudit'),
      name: 'ArchivePermissionAudit',
      meta: {
        title: '权限审计',
        icon: 'el-icon-view',
        roles: ['admin', 'manager', 'user'],
        activeMenu: '/management-accountant/as/archive-permission'
      },
      hidden: true
    }
  ]
}

export default asRouter
