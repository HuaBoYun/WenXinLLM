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
        component: '@/views/internal/home',
        meta: {
          title: '首页',
          icon: 'home-2-line',
          noClosable: true,
        },
      },
    ],
  },
  {
    path: '/evaluationManagement',
    name: 'EvaluationManagement',
    component: 'Layout',
    meta: {
      title: '评价管理',
      icon: 'edit-box-line',
    },
    children: [
      {
        path: 'project',
        name: 'Project',
        component: '@/views/internal/evaluationManagement/project',
        meta: {
          title: '评价立项',
          icon: 'folder-4-line',
        },
      },
      {
        path: 'score',
        name: 'Score',
        component: '@/views/internal/evaluationManagement/score',
        meta: {
          title: '评价评分',
          icon: 'folder-reduce-line',
        },
      },
      {
        path: 'trace',
        name: 'Trace',
        component: '@/views/internal/evaluationManagement/trace',
        meta: {
          title: '评价跟踪',
          icon: 'folder-shared-line',
        },
      },
      {
        path: 'result',
        name: 'Result',
        component: '@/views/internal/evaluationManagement/result',
        meta: {
          title: '评价结果',
          icon: 'folder-chart-line',
        },
      },
    ],
  },
  {
    path: '/internalTest',
    name: 'InternalTest',
    component: 'Layout',
    meta: {
      title: '内控测试',
      icon: 'file-damage-line',
    },
    children: [
      {
        path: 'plan',
        name: 'Plan',
        component: '@/views/internal/internalTest/plan',
        meta: {
          title: '测试方案',
          icon: 'file-mark-line',
        },
      },
      {
        path: 'task',
        name: 'Task',
        component: '@/views/internal/internalTest/task',
        meta: {
          title: '测试任务',
          icon: 'article-line',
        },
      },
      {
        path: 'track',
        name: 'Track',
        component: '@/views/internal/internalTest/track',
        meta: {
          title: '测试跟踪',
          icon: 'device-recover-line',
        },
      },
      {
        path: 'results',
        name: 'Results',
        component: '@/views/internal/internalTest/results',
        meta: {
          title: '测试结果汇总',
          icon: 'book-read-line',
        },
      },
    ],
  },
  {
    path: '/report',
    name: 'Report',
    component: 'Layout',
    meta: {
      title: '内控报告',
      icon: 'booklet-line',
    },
    children: [
      {
        path: 'index',
        name: 'Index',
        component: '@/views/internal/report/index',
        meta: {
          title: '评价报告编制',
          icon: 'file-text-line',
        },
      },
      {
        path: 'custom',
        name: 'Custom',
        component: '@/views/internal/report/custom',
        meta: {
          title: '自定义报告编制',
          icon: 'file-add-line',
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
        component: '@/views/internal/question/flaw',
        meta: {
          title: '缺陷管理',
          icon: 'anticlockwise-line',
        },
      },
      {
        path: 'draft',
        name: 'Draft',
        component: '@/views/internal/question/draft',
        meta: {
          title: '底稿汇总',
          icon: 'book-2-line',
        },
      },
      {
        path: 'risk',
        name: 'Risk',
        component: '@/views/internal/question/risk',
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
        component: '@/views/internal/rectify/scheme',
        meta: {
          title: '整改方案',
          icon: 'book-read-line',
        },
      },
      {
        path: 'track',
        name: 'Track',
        component: '@/views/internal/rectify/track',
        meta: {
          title: '整改跟踪',
          icon: 'barricade-line',
        },
      },
      {
        path: 'assign',
        name: 'Assign',
        component: '@/views/internal/rectify/assign',
        meta: {
          title: '整改分派',
          icon: 'bring-to-front',
        },
      },
      {
        path: 'practicable',
        name: 'Practicable',
        component: '@/views/internal/rectify/practicable',
        meta: {
          title: '整改落实',
          icon: 'book-read-line',
        },
      },
      {
        path: 'valuation',
        name: 'Valuation',
        component: '@/views/internal/rectify/valuation',
        meta: {
          title: '整改评价',
          icon: 'edit-fill',
        },
      },
      {
        path: 'query',
        name: 'Query',
        component: '@/views/internal/rectify/query',
        meta: {
          title: '整改查询',
          icon: 'file-search-line',
        },
      },
      {
        path: 'unregisteredProblem',
        name: 'UnregisteredProblem',
        component: '@/views/internal/rectify/unregisteredProblem',
        meta: {
          title: '未销号问题',
          icon: 'question-line',
        },
      },
    ],
  },
  // {
  //   path: '/project',
  //   name: 'Project',
  //   component: 'Layout',
  //   meta: {
  //     title: '项目管理',
  //     icon: 'projector-line',
  //   },
  //   children: [
  //     {
  //       path: 'index',
  //       name: 'Index',
  //       component: '@/views/audit/project/index',
  //       meta: {
  //         title: '项目管理',
  //         icon: 'projector-line',
  //       },
  //     },
  //     {
  //       path: 'task',
  //       name: 'Task',
  //       component: '@/views/audit/project/task',
  //       meta: {
  //         title: '任务分配',
  //         icon: 'calendar-todo-line',
  //       },
  //     },
  //     {
  //       path: 'execute',
  //       name: 'Execute',
  //       component: '@/views/audit/project/execute',
  //       meta: {
  //         title: '执行一览',
  //         icon: 'airplay-line',
  //       },
  //     },
  //   ],
  // },
  // {
  //   path: '/prepare',
  //   name: 'Prepare',
  //   component: 'Layout',
  //   meta: {
  //     title: '审计准备',
  //     icon: 'artboard-line',
  //   },
  //   children: [
  //     {
  //       path: 'guide',
  //       name: 'Guide',
  //       component: '@/views/audit/prepare/guide',
  //       meta: {
  //         title: '审计指引',
  //         icon: 'anticlockwise-line',
  //       },
  //     },
  //     {
  //       path: 'projectData',
  //       name: 'ProjectData',
  //       component: '@/views/audit/prepare/projectData',
  //       meta: {
  //         title: '项目资料',
  //         icon: 'book-2-line',
  //       },
  //     },
  //     {
  //       path: 'auditData',
  //       name: 'AuditData',
  //       component: '@/views/audit/prepare/auditData',
  //       meta: {
  //         title: '审计资料',
  //         icon: 'book-mark-line',
  //       },
  //     },
  //     {
  //       path: 'notice',
  //       name: 'Notice',
  //       component: '@/views/audit/prepare/notice',
  //       meta: {
  //         title: '审计通知',
  //         icon: 'chat-2-line',
  //       },
  //     },
  //   ],
  // },
  // {
  //   path: '/implement',
  //   name: 'Implement',
  //   component: 'Layout',
  //   meta: {
  //     title: '审计实施',
  //     icon: 'calendar-todo-line',
  //   },
  //   children: [
  //     {
  //       path: 'summary',
  //       name: 'Summary',
  //       component: '@/views/audit/implement/summary',
  //       meta: {
  //         title: '进场纪要',
  //         icon: 'file-transfer-line',
  //       },
  //     },
  //     {
  //       path: 'task',
  //       name: 'Task',
  //       component: '@/views/audit/implement/task',
  //       meta: {
  //         title: '我的任务',
  //         icon: 'checkbox-multiple-line',
  //       },
  //     },
  //     {
  //       path: 'log',
  //       name: 'Log',
  //       component: '@/views/audit/implement/log',
  //       meta: {
  //         title: '工作日志',
  //         icon: 'cloud-windy-line',
  //       },
  //     },
  //     {
  //       path: 'myDraft',
  //       name: 'MyDraft',
  //       component: '@/views/audit/implement/myDraft',
  //       meta: {
  //         title: '我的底稿',
  //         icon: 'draft-line',
  //       },
  //     },
  //     {
  //       path: 'doubtful',
  //       name: 'Doubtful',
  //       component: '@/views/audit/implement/doubtful',
  //       meta: {
  //         title: '疑点管理',
  //         icon: 'file-unknow-line',
  //       },
  //     },
  //     {
  //       path: 'draftManage',
  //       name: 'DraftManage',
  //       component: '@/views/audit/implement/draftManage',
  //       meta: {
  //         title: '底稿管理',
  //         icon: 'file-copy-line',
  //       },
  //     },
  //     {
  //       path: 'discover',
  //       name: 'Discover',
  //       component: '@/views/audit/implement/discover',
  //       meta: {
  //         title: '审计发现',
  //         icon: 'file-search-line',
  //       },
  //     },
  //     {
  //       path: 'confirm',
  //       name: 'Confirm',
  //       component: '@/views/audit/implement/confirm',
  //       meta: {
  //         title: '事实确认书',
  //         icon: 'file-shield-line',
  //       },
  //     },
  //     {
  //       path: 'leaveSummary',
  //       name: 'LeaveSummary',
  //       component: '@/views/audit/implement/leaveSummary',
  //       meta: {
  //         title: '离场纪要',
  //         icon: 'file-reduce-line',
  //       },
  //     },
  //   ],
  // },
  // {
  //   path: '/report',
  //   name: 'Report',
  //   component: 'Layout',
  //   meta: {
  //     title: '审计报告',
  //     icon: 'bar-chart-box-line',
  //   },
  //   children: [
  //     {
  //       path: 'index',
  //       name: 'Index',
  //       component: '@/views/audit/report/index',
  //       meta: {
  //         title: '报告编制',
  //         icon: 'bar-chart-2-line',
  //       },
  //     },
  //     {
  //       path: 'suggest',
  //       name: 'Suggest',
  //       component: '@/views/audit/report/suggest',
  //       meta: {
  //         title: '建议书编制',
  //         icon: 'book-line',
  //       },
  //     },
  //     {
  //       path: 'custom',
  //       name: 'Custom',
  //       component: '@/views/audit/report/custom',
  //       meta: {
  //         title: '自定义编制',
  //         icon: 'mark-pen-line',
  //       },
  //     },
  //   ],
  // },
  // {
  //   path: '/data',
  //   name: 'Data',
  //   component: 'Layout',
  //   meta: {
  //     title: '项目资料',
  //     icon: 'folder-open-line',
  //   },
  //   children: [
  //     {
  //       path: 'task',
  //       name: 'Task',
  //       component: '@/views/audit/data/task',
  //       meta: {
  //         title: '任务管理',
  //         icon: 'mail-open-line',
  //       },
  //     },
  //     {
  //       path: 'file',
  //       name: 'File',
  //       component: '@/views/audit/data/file',
  //       meta: {
  //         title: '附件列表',
  //         icon: 'file-copy-2-line',
  //       },
  //     },
  //     {
  //       path: 'look',
  //       name: 'Look',
  //       component: '@/views/audit/data/look',
  //       meta: {
  //         title: '项目查看',
  //         icon: 'search-eye-line',
  //       },
  //     },
  //     {
  //       path: 'package',
  //       name: 'Package',
  //       component: '@/views/audit/data/package',
  //       meta: {
  //         title: '项目归档',
  //         icon: 'archive-line',
  //       },
  //     },
  //   ],
  // },
  // {
  //   path: '/auditRecord',
  //   name: 'AuditRecord',
  //   component: 'Layout',
  //   meta: {
  //     title: '审计档案',
  //     icon: 'book-2-line',
  //   },
  //   children: [
  //     {
  //       path: 'list',
  //       name: 'List',
  //       component: '@/views/audit/auditRecord/list',
  //       meta: {
  //         title: '档案列表',
  //         icon: 'align-justify',
  //       },
  //     },
  //     {
  //       path: 'read',
  //       name: 'Read',
  //       component: '@/views/audit/auditRecord/read',
  //       meta: {
  //         title: '档案借阅',
  //         icon: 'book-read-fill',
  //       },
  //     },
  //     {
  //       path: 'log',
  //       name: 'Log',
  //       component: '@/views/audit/auditRecord/log',
  //       meta: {
  //         title: '借阅日志',
  //         icon: 'bill-line',
  //       },
  //     },
  //   ],
  // },
  // {
  //   path: '/question',
  //   name: 'Question',
  //   component: 'Layout',
  //   meta: {
  //     title: '相关问题',
  //     icon: 'question-line',
  //   },
  //   children: [
  //     {
  //       path: 'flaw',
  //       name: 'Flaw',
  //       component: '@/views/audit/question/flaw',
  //       meta: {
  //         title: '缺陷管理',
  //         icon: 'anticlockwise-line',
  //       },
  //     },
  //     {
  //       path: 'draft',
  //       name: 'Draft',
  //       component: '@/views/audit/question/draft',
  //       meta: {
  //         title: '底稿汇总',
  //         icon: 'book-2-line',
  //       },
  //     },
  //     {
  //       path: 'risk',
  //       name: 'Risk',
  //       component: '@/views/audit/question/risk',
  //       meta: {
  //         title: '风险发现',
  //         icon: 'alarm-warning-line',
  //       },
  //     },
  //   ],
  // },
  // {
  //   path: '/rectify',
  //   name: 'Rectify',
  //   component: 'Layout',
  //   meta: {
  //     title: '整改跟踪',
  //     icon: 'barricade-line',
  //   },
  //   children: [
  //     {
  //       path: 'scheme',
  //       name: 'Scheme',
  //       component: '@/views/audit/rectify/scheme',
  //       meta: {
  //         title: '整改方案',
  //         icon: 'book-read-line',
  //       },
  //     },
  //     {
  //       path: 'track',
  //       name: 'Track',
  //       component: '@/views/audit/rectify/track',
  //       meta: {
  //         title: '整改跟踪',
  //         icon: 'barricade-line',
  //       },
  //     },
  //     {
  //       path: 'assign',
  //       name: 'Assign',
  //       component: '@/views/audit/rectify/assign',
  //       meta: {
  //         title: '整改分派',
  //         icon: 'bring-to-front',
  //       },
  //     },
  //     {
  //       path: 'practicable',
  //       name: 'Practicable',
  //       component: '@/views/audit/rectify/practicable',
  //       meta: {
  //         title: '整改落实',
  //         icon: 'book-read-line',
  //       },
  //     },
  //     {
  //       path: 'valuation',
  //       name: 'Valuation',
  //       component: '@/views/audit/rectify/valuation',
  //       meta: {
  //         title: '整改评价',
  //         icon: 'edit-fill',
  //       },
  //     },
  //     {
  //       path: 'query',
  //       name: 'Query',
  //       component: '@/views/audit/rectify/query',
  //       meta: {
  //         title: '整改查询',
  //         icon: 'file-search-line',
  //       },
  //     },
  //     {
  //       path: 'unregisteredProblem',
  //       name: 'UnregisteredProblem',
  //       component: '@/views/audit/rectify/unregisteredProblem',
  //       meta: {
  //         title: '未销号问题',
  //         icon: 'question-line',
  //       },
  //     },
  //   ],
  // },
]

module.exports = [
  {
    url: '/routerInternal/getListForInternal',
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
