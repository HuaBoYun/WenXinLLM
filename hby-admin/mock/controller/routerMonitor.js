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
    path: '/formMonitor',
    name: 'FormMonitor',
    component: 'Layout',
    meta: {
      title: '表单监控',
      icon: 'article-line',
    },
    children: [
      {
        path: 'nc',
        name: 'Nc',
        component: '@/views/monitor/formMonitor/nc',
        meta: {
          title: 'NC表单监控',
          icon: 'bill-line',
        },
      },
      {
        path: 'u8',
        name: 'U8',
        component: '@/views/monitor/formMonitor/u8',
        meta: {
          title: 'U8表单监控',
          icon: 'bill-line',
        },
      },
      {
        path: 'eas',
        name: 'Eas',
        component: '@/views/monitor/formMonitor/eas',
        meta: {
          title: 'EAS表单监控',
          icon: 'bill-line',
        },
      },
      {
        path: 'k3',
        name: 'K3',
        component: '@/views/monitor/formMonitor/k3',
        meta: {
          title: 'K3表单监控',
          icon: 'bill-line',
        },
      },
      {
        path: 'sap',
        name: 'Sap',
        component: '@/views/monitor/formMonitor/sap',
        meta: {
          title: 'SAP表单监控',
          icon: 'bill-line',
        },
      },
      {
        path: 'business',
        name: 'Business',
        component: '@/views/monitor/formMonitor/business',
        meta: {
          title: '业务表单监控',
          icon: 'bill-line',
        },
      },
    ],
  },
  {
    path: '/ruleMonitor',
    name: 'RuleMonitor',
    component: 'Layout',
    meta: {
      title: '规则监控',
      icon: 'pencil-ruler-2-line',
    },
    children: [
      {
        path: 'ruleManage',
        name: 'RuleManage',
        component: '@/views/monitor/ruleMonitor/ruleManage',
        meta: {
          title: '规则管理',
          icon: 'settings-3-line',
        },
      },
      {
        path: 'ruleRemind',
        name: 'RuleRemind',
        component: '@/views/monitor/ruleMonitor/ruleRemind',
        meta: {
          title: '规则预警',
          icon: 'alarm-warning-line',
        },
      },
    ],
  },
  {
    path: '/monitorTarget',
    name: 'MonitorTarget',
    component: 'Layout',
    meta: {
      title: '监控指标',
      icon: 'line-chart-line',
    },
    children: [
      {
        path: 'targetManage',
        name: 'TargetManage',
        component: '@/views/monitor/monitorTarget/targetManage',
        meta: {
          title: '指标管理',
          icon: 'settings-3-line',
        },
      },
      {
        path: 'targetRemind',
        name: 'TargetRemind',
        component: '@/views/monitor/monitorTarget/targetRemind',
        meta: {
          title: '指标预警',
          icon: 'alarm-warning-line',
        },
      },
    ],
  },
  {
    path: '/modelMonitor',
    name: 'modelMonitor',
    component: 'Layout',
    meta: {
      title: '模型监控',
      icon: 'layout-4-line',
    },
    children: [
      {
        path: 'modelManage',
        name: 'ModelManage',
        component: '@/views/monitor/modelMonitor/modelManage',
        meta: {
          title: '模型管理',
          icon: 'settings-3-line',
        },
      },
      {
        path: 'modelRemind',
        name: 'ModelRemind',
        component: '@/views/monitor/modelMonitor/modelRemind',
        meta: {
          title: '模型预警',
          icon: 'alarm-warning-line',
        },
      },
    ],
  },
  {
    path: '/monitorExecute',
    name: 'MonitorExecute',
    component: 'Layout',
    meta: {
      title: '监控执行',
      icon: 'checkbox-multiple-fill',
    },
    children: [
      {
        path: 'ruleExecute',
        name: 'RuleExecute',
        component: '@/views/monitor/monitorExecute/ruleExecute',
        meta: {
          title: '规则执行',
          icon: 'pencil-ruler-2-line',
        },
      },
      {
        path: 'targetExecute',
        name: 'TargetExecute',
        component: '@/views/monitor/monitorExecute/targetExecute',
        meta: {
          title: '指标执行',
          icon: 'line-chart-line',
        },
      },
      {
        path: 'modelExecute',
        name: 'ModelExecute',
        component: '@/views/monitor/monitorExecute/modelExecute',
        meta: {
          title: '模型执行',
          icon: 'layout-4-line',
        },
      },
    ],
  },
  {
    path: '/monitorReport',
    name: 'MonitorReport',
    component: 'Layout',
    meta: {
      title: '监控报告',
      icon: 'slideshow-line',
    },
    children: [
      {
        path: 'index',
        name: 'Index',
        component: '@/views/monitor/monitorReport/index',
        meta: {
          title: '监控报告编制',
          icon: 'stack-line',
        },
      },
      {
        path: 'customer',
        name: 'Customer',
        component: '@/views/monitor/monitorReport/customer',
        meta: {
          title: '自定义报告编制',
          icon: 'stack-overflow-line',
        },
      },
    ],
  },
  {
    path: '/question',
    name: 'Question',
    component: 'Layout',
    meta: {
      title: '问题汇总',
      icon: 'question-line',
    },
    children: [
      {
        path: 'flaw',
        name: 'Flaw',
        component: '@/views/monitor/question/flaw',
        meta: {
          title: '缺陷管理',
          icon: 'airplay-line',
        },
      },
      {
        path: 'risk',
        name: 'risk',
        component: '@/views/monitor/question/risk',
        meta: {
          title: '风险发现',
          icon: 'alarm-warning-line',
        },
      },
      {
        path: 'draft',
        name: 'Draft',
        component: '@/views/monitor/question/draft',
        meta: {
          title: '底稿汇总',
          icon: 'edit-box-line',
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
      icon: 'ball-pen-line',
    },
    children: [
      {
        path: 'scheme',
        name: 'Scheme',
        component: '@/views/monitor/rectify/scheme',
        meta: {
          title: '整改方案',
          icon: 'bill-line',
        },
      },
      {
        path: 'assign',
        name: 'Assign',
        component: '@/views/monitor/rectify/assign',
        meta: {
          title: '整改分派',
          icon: 'anticlockwise-line',
        },
      },
      {
        path: 'follow',
        name: 'Follow',
        component: '@/views/monitor/rectify/follow',
        meta: {
          title: '整改跟踪',
          icon: 'arrow-left-right-line',
        },
      },
      {
        path: 'practicable',
        name: 'Practicable',
        component: '@/views/monitor/rectify/practicable',
        meta: {
          title: '整改落实',
          icon: 'auction-line',
        },
      },
      {
        path: 'query',
        name: 'Query',
        component: '@/views/monitor/rectify/query',
        meta: {
          title: '整改查询',
          icon: 'file-search-line',
        },
      },
    ],
  },
]

module.exports = [
  {
    url: '/routerMonitor/getListForMonitor',
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
