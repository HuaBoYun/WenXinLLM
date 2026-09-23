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
        component: '@/views/risk/home',
        meta: {
          title: '首页',
          icon: 'home-2-line',
          noClosable: true,
        },
      },
    ],
  },
  // {
  //   path: '/system',
  //   name: 'System',
  //   component: 'Layout',
  //   meta: {
  //     title: '系统首页',
  //     icon: 'computer-line',
  //   },
  //   children: [
  //     {
  //       path: '/overview',
  //       name: 'Overview',
  //       component: '@/views/risk/system/overview',
  //       meta: {
  //         title: '风控总览',
  //         icon: 'archive-drawer-line',
  //       },
  //     },
  //     {
  //       path: '/events',
  //       name: 'Events',
  //       component: '@/views/risk/system/events',
  //       meta: {
  //         title: '风险事件',
  //         icon: 'user-2-line',
  //       },
  //     },
  //     {
  //       path: '/estimate',
  //       name: 'Estimate',
  //       component: '@/views/risk/system/estimate',
  //       meta: {
  //         title: '风险评估',
  //         icon: 'money-cny-circle-line',
  //       },
  //     },
  //   ],
  // },
  {
    path: '/fillin',
    name: 'Fillin',
    component: 'Layout',
    meta: {
      title: '风险填报',
      icon: 'slideshow-line',
    },
    children: [
      {
        path: '/fillin',
        name: 'Fillin',
        component: '@/views/risk/fillin',
        meta: {
          title: '风险填报',
          icon: 'slideshow-line',
        },
      },
    ],
  },
  {
    path: '/identify',
    name: 'Identify',
    component: 'Layout',
    meta: {
      title: '风险识别',
      icon: 'qr-scan-2-line',
    },
    children: [
      {
        path: '/creation',
        name: 'Creation',
        component: '@/views/risk/identify/creation',
        meta: {
          title: '风险创建',
          icon: 'file-edit-line',
        },
      },
      {
        path: '/database',
        name: 'Database',
        component: '@/views/risk/identify/database',
        meta: {
          title: '风险数据库',
          icon: 'database-2-fill',
        },
      },
      {
        path: '/version',
        name: 'Version',
        component: '@/views/risk/identify/version',
        meta: {
          title: '版本管理',
          icon: 'play-list-add-fill',
        },
      },
    ],
  },
  {
    path: '/assessment',
    name: 'Assessment',
    component: 'Layout',
    meta: {
      title: '风险评估',
      icon: 'scales-3-fill',
    },
    children: [
      {
        path: '/plan',
        name: 'Plan',
        component: '@/views/risk/assessment/plan',
        meta: {
          title: '评估计划',
          icon: 'file-list-2-line',
        },
      },
      {
        path: '/task',
        name: 'Task',
        component: '@/views/risk/assessment/task',
        meta: {
          title: '评估任务',
          icon: 'task-line',
        },
      },
      {
        path: '/result',
        name: 'Result',
        component: '@/views/risk/assessment/result',
        meta: {
          title: '评估结果',
          icon: 'list-check',
        },
      },
    ],
  },
  {
    path: '/treatment',
    name: 'Treatment',
    component: 'Layout',
    meta: {
      title: '风险应对',
      icon: 'archive-drawer-line',
    },
    children: [
      {
        path: '/treatment',
        name: 'Treatment',
        component: '@/views/risk/treatment/index',
        meta: {
          title: '风险应对',
          icon: 'align-justify',
        },
      },
    ],
  },
  {
    path: '/events',
    name: 'Events',
    component: 'Layout',
    meta: {
      title: '事件管理',
      icon: 'contacts-book-upload-line',
    },
    children: [
      {
        path: '/eventBase',
        name: 'EventBase',
        component: '@/views/risk/events/eventBase',
        meta: {
          title: '风险事件库',
          icon: 'book-2-line',
        },
      },
      {
        path: '/eventHandling',
        name: 'EventHandling',
        component: '@/views/risk/events/eventHandling',
        meta: {
          title: '风险事件处理',
          icon: 'shield-check-line',
        },
      },
    ],
  },
  {
    path: '/standingBook',
    name: 'StandingBook',
    component: 'Layout',
    meta: {
      title: '风险台账',
      icon: 'booklet-line',
    },
    children: [
      {
        path: 'index',
        name: 'Index',
        component: '@/views/risk/standingBook/index',
        meta: {
          title: '风险台账',
          icon: 'booklet-line',
        },
      },
    ],
  },
  {
    path: '/monitor',
    name: 'Monitor',
    component: 'Layout',
    meta: {
      title: '风险监控',
      icon: '24-hours-line',
    },
    children: [
      {
        path: '/supplier',
        name: 'Supplier',
        component: '@/views/risk/monitor/supplier',
        meta: {
          title: '供应商监控',
          icon: 'user-search-fill',
        },
      },
    ],
  },
  {
    path: '/report',
    name: 'Report',
    component: 'Layout',
    meta: {
      title: '风险报告',
      icon: 'file-list-3-line',
    },
    children: [
      {
        path: '/report/normal',
        name: 'Supplier',
        component: '@/views/risk/report/normal',
        meta: {
          title: '风险报告编制',
          icon: 'play-list-add-line',
        },
      },
      {
        path: '/report/custom',
        name: 'Supplier',
        component: '@/views/risk/report/custom',
        meta: {
          title: '自定义报告编制',
          icon: 'list-settings-line',
        },
      },
    ],
  },
  {
    path: '/question',
    name: 'Question',
    component: 'Layout',
    meta: {
      title: '相关问题',
      icon: 'question-line',
    },
    children: [
      {
        path: 'flaw',
        name: 'Flaw',
        component: '@/views/risk/question/flaw',
        meta: {
          title: '缺陷管理',
          icon: 'anticlockwise-line',
        },
      },
      {
        path: 'draft',
        name: 'Draft',
        component: '@/views/risk/question/draft',
        meta: {
          title: '底稿汇总',
          icon: 'book-2-line',
        },
      },
      {
        path: 'risk',
        name: 'Risk',
        component: '@/views/risk/question/risk',
        meta: {
          title: '风险发现',
          icon: 'alarm-warning-line',
        },
      },
    ],
  },
  {
    path: '/rectify',
    name: 'Rectify',
    component: 'Layout',
    meta: {
      title: '整改跟踪',
      icon: 'barricade-line',
    },
    children: [
      {
        path: 'scheme',
        name: 'Scheme',
        component: '@/views/risk/rectify/scheme',
        meta: {
          title: '整改方案',
          icon: 'book-read-line',
        },
      },
      {
        path: 'track',
        name: 'Track',
        component: '@/views/risk/rectify/track',
        meta: {
          title: '整改跟踪',
          icon: 'barricade-line',
        },
      },
      {
        path: 'assign',
        name: 'Assign',
        component: '@/views/risk/rectify/assign',
        meta: {
          title: '整改分派',
          icon: 'bring-to-front',
        },
      },
      {
        path: 'practicable',
        name: 'Practicable',
        component: '@/views/risk/rectify/practicable',
        meta: {
          title: '整改落实',
          icon: 'book-read-line',
        },
      },
      {
        path: 'valuation',
        name: 'Valuation',
        component: '@/views/risk/rectify/valuation',
        meta: {
          title: '整改评价',
          icon: 'edit-fill',
        },
      },
      {
        path: 'query',
        name: 'Query',
        component: '@/views/risk/rectify/query',
        meta: {
          title: '整改查询',
          icon: 'file-search-line',
        },
      },
      {
        path: 'unregisteredProblem',
        name: 'UnregisteredProblem',
        component: '@/views/risk/rectify/unregisteredProblem',
        meta: {
          title: '未销号问题',
          icon: 'question-line',
        },
      },
    ],
  },
]

module.exports = [
  {
    url: '/routerRisk/getListForRisk',
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
