/**
 * @description router全局配置，如有必要可分文件抽离，其中asyncRoutes只有在intelligence模式下才会用到，pro版只支持remixIcon图标，具体配置请查看vip群文档
 */
const List = [
  {
    path: '/',
    name: 'Root',
    component: 'Layout',
    meta: {
      title: '首页',
      icon: 'home-2-line',
    },
    children: [
      {
        path: 'home',
        name: 'Home',
        component: '@/views/home/index',
        meta: {
          title: '首页',
          icon: 'home-2-line',
          noClosable: true,
        },
      },
    ],
  },
  {
    path: '/activiti',
    name: 'Activiti',
    component: 'Layout',
    meta: {
      title: '流程管理',
      icon: 'flow-chart',
    },
    children: [
      {
        path: 'create',
        name: 'Create',
        component: '@/views/setting/activiti/create',
        meta: {
          title: '创建流程',
          icon: 'add-box-line',
        },
      },
    ],
  },
  {
    path: '/org',
    name: 'Org',
    component: 'Layout',
    meta: {
      title: '机构管理',
      icon: 'organization-chart',
    },
    children: [
      {
        path: 'company',
        name: 'Company',
        component: '@/views/setting/org/company',
        meta: {
          title: '公司管理',
          icon: 'building-4-line',
        },
      },
      {
        path: 'menu',
        name: 'Menu',
        component: '@/views/setting/org/menu',
        meta: {
          title: '菜单设定',
          icon: 'menu-fill',
        },
      },
      {
        path: 'dep',
        name: 'Dep',
        component: '@/views/setting/org/dep',
        meta: {
          title: '组织架构',
          icon: 'organization-chart',
        },
      },
      {
        path: 'industry',
        name: 'Industry',
        component: '@/views/setting/org/industry',
        meta: {
          title: '行业架构',
          icon: 'briefcase-2-line',
        },
      },
    ],
  },
  {
    path: '/authm',
    name: 'Authm',
    component: 'Layout',
    meta: {
      title: '权限管理',
      icon: 'angularjs-line',
    },
    children: [
      {
        path: 'user',
        name: 'User',
        component: '@/views/setting/auth/user',
        type: 2,
        perms: 'AuthUser',
        meta: {
          title: '用户管理',
          icon: 'user-line',
        },
      },
      {
        path: 'role',
        name: 'Role',
        component: '@/views/setting/auth/role',
        type: 2,
        perms: 'AuthRole',
        meta: {
          title: '角色管理',
          icon: 'shield-user-line',
        },
      },
      {
        path: 'position',
        name: 'Position',
        component: '@/views/setting/auth/position',
        meta: {
          title: '岗位管理',
          icon: 'folder-user-line',
        },
      },
      {
        path: 'auth',
        name: 'Auth',
        component: '@/views/setting/auth/index',
        meta: {
          title: '权限设定',
          icon: 'user-settings-line',
        },
      },
      {
        path: 'accredit',
        name: 'Accredit',
        component: '@/views/setting/auth/accredit',
        meta: {
          title: '账套授权',
          icon: 'user-follow-line',
        },
      },
      {
        path: 'menu',
        name: 'menu',
        component: '@/views/setting/auth/menu',
        type: 2,
        perms: 'AuthMenu',
        meta: {
          title: '菜单管理',
          icon: 'menu-2-fill',
        },
      },
    ],
  },
  {
    path: '/specialist',
    name: 'Specialist',
    component: 'Layout',
    meta: {
      title: '专家资源',
      icon: 'user-2-line',
    },
    children: [
      {
        path: 'internal',
        name: 'Internal',
        component: '@/views/setting/specialist/internal',
        meta: {
          title: '内部专家',
          icon: 'user-received-2-line',
        },
      },
      {
        path: 'external',
        name: 'External',
        component: '@/views/setting/specialist/external',
        meta: {
          title: '外聘专家',
          icon: 'user-shared-2-line',
        },
      },
    ],
  },
  {
    path: '/themeRepertory',
    name: 'ThemeRepertory',
    component: 'Layout',
    meta: {
      title: '主题仓库',
      icon: 't-shirt-line',
    },
    children: [
      {
        path: 'theme',
        name: 'Theme',
        component: '@/views/setting/themeRepertory/theme',
        meta: {
          title: '主题展示',
          icon: 'apps-line',
        },
      },
      {
        path: 'phone',
        name: 'Phone',
        component: '@/views/setting/themeRepertory/phone',
        meta: {
          title: '手机主题',
          icon: 'table-alt-line',
        },
      },
      {
        path: 'classify',
        name: 'Classify',
        component: '@/views/setting/themeRepertory/risk',
        meta: {
          title: '风险分类',
          icon: 'file-copy-line',
        },
      },
      {
        path: 'model',
        name: 'Model',
        component: '@/views/setting/themeRepertory/model',
        meta: {
          title: '预测模型',
          icon: 'pie-chart-2-line',
        },
      },
    ],
  },
  {
    path: '/system',
    name: 'System',
    component: 'Layout',
    meta: {
      title: '系统配置',
      icon: 'settings-3-line',
    },
    children: [
      {
        path: 'ywcj',
        name: 'Ywcj',
        component: '@/views/setting/system/ywcj',
        meta: {
          title: '业务创建',
          icon: 'drag-drop-line',
        },
      },
      {
        path: 'lcdy',
        name: 'Lcdy',
        component: '@/views/setting/system/lcdy',
        meta: {
          title: '流程定义',
          icon: 'edit-circle-line',
        },
      },
      {
        path: 'htlcsz',
        name: 'Htlcsz',
        component: '@/views/setting/system/htlcsz',
        meta: {
          title: '合同流程设置',
          icon: 'edit-box-line',
        },
      },
      {
        path: 'ywlcsz',
        name: 'Ywlcsz',
        component: '@/views/setting/system/ywlcsz',
        meta: {
          title: '业务流程设置',
          icon: 'file-shield-2-line',
        },
      },
      {
        path: 'lcbbgl',
        name: 'Lcbbgl',
        component: '@/views/setting/system/lcbbgl',
        meta: {
          title: '流程版本管理',
          icon: 'file-copy-line',
        },
      },
      {
        path: 'bhsz',
        name: 'Bhsz',
        component: '@/views/setting/system/bhsz',
        meta: {
          title: '编号设置',
          icon: 'file-copy-line',
        },
      },
      {
        path: 'mkcj',
        name: 'Mkcj',
        component: '@/views/setting/system/mkcj',
        meta: {
          title: '模块创建',
          icon: 'dropbox-line',
        },
      },
      {
        path: 'cdsz',
        name: 'Cdsz',
        component: '@/views/setting/system/cdsz',
        meta: {
          title: '菜单设置',
          icon: 'file-copy-line',
        },
      },
      {
        path: 'datainit',
        name: 'Datainit',
        component: '@/views/setting/system/init',
        meta: {
          title: '数据初始化',
          icon: 'file-copy-line',
        },
      },
    ],
  },
  {
    path: '/template',
    name: 'Template',
    component: 'Layout',
    meta: {
      title: '模板管理',
      icon: 'device-line',
    },
    children: [
      {
        path: 'download',
        name: 'Download',
        component: '@/views/setting/template/download',
        meta: {
          title: '模板下载',
          icon: 'download-2-line',
        },
      },
    ],
  },
  {
    path: '/dataGather',
    name: 'DataGather',
    component: 'Layout',
    meta: {
      title: '数据采集',
      icon: 'database-2-line',
    },
    children: [
      {
        path: 'sjcj',
        name: 'Sjcj',
        component: '@/views/setting/dataGather/index',
        meta: {
          title: '数据采集',
          icon: 'coin-line',
        },
      },
      {
        path: 'cjcl',
        name: 'Cjcl',
        component: '@/views/setting/dataGather/cjcl',
        meta: {
          title: '采集策略',
          icon: 'cloud-windy-line',
        },
      },
      {
        path: 'sjpz',
        name: 'Sjpz',
        component: '@/views/setting/dataGather/sjpz',
        meta: {
          title: '数据配置',
          icon: 'codepen-fill',
        },
      },
      {
        path: 'sjdr',
        name: 'Sjdr',
        component: '@/views/setting/dataGather/sjdr',
        meta: {
          title: '数据导入',
          icon: 'anticlockwise-line',
        },
      },
    ],
  },
  {
    path: '/infoSearch',
    name: 'InfoSearch',
    component: 'Layout',
    meta: {
      title: '信息查询',
      icon: 'file-search-line',
    },
    children: [
      {
        path: 'xxcx',
        name: 'Xxcx',
        component: '@/views/setting/infoSearch/xxcx',
        meta: {
          title: '个人查询',
          icon: 'user-search-line',
        },
      },
      {
        path: 'fycx',
        name: 'Fyxc',
        component: '@/views/setting/infoSearch/fycx',
        meta: {
          title: '统计费用',
          icon: 'article-line',
        },
      },
      {
        path: 'fybz',
        name: 'Fybz',
        component: '@/views/setting/infoSearch/fybz',
        meta: {
          title: '费用标准',
          icon: 'article-line',
        },
      },
      {
        path: 'czjl',
        name: 'Czjl',
        component: '@/views/setting/infoSearch/czjl',
        meta: {
          title: '充值记录',
          icon: 'todo-line',
        },
      },
    ],
  },
  {
    path: '/course',
    name: 'Course',
    component: 'Layout',
    meta: {
      title: '课程管理',
      icon: 'book-2-line',
    },
    children: [
      {
        path: 'kclb',
        name: 'Kclb',
        component: '@/views/setting/course/kclb',
        meta: {
          title: '课程类别',
          icon: 'table-alt-line',
        },
      },
      {
        path: 'kcwh',
        name: 'Kcwh',
        component: '@/views/setting/course/kcwh',
        meta: {
          title: '课程维护',
          icon: 'table-2',
        },
      },
    ],
  },
  {
    path: '/loginPage',
    name: 'LoginPage',
    component: 'Layout',
    meta: {
      title: '登录管理',
      icon: 'todo-line',
    },
    children: [
      {
        path: 'list',
        name: 'list',
        component: '@/views/setting/loginPage/list',
        meta: {
          title: '列表',
          icon: 'todo-line',
        },
      },
    ],
  },
]

module.exports = [
  {
    url: '/routerSetting/getListForSetting',
    type: 'get',
    response() {
      return {
        code: 200,
        msg: 'success',
        data: { list: List },
      }
    },
  },
]
