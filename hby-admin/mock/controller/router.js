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
    path: '/vab',
    name: 'Vab',
    component: 'Layout',
    meta: {
      title: '组件',
      icon: 'code-box-line',
    },
    children: [
      {
        path: 'icon',
        name: 'Icon',
        meta: {
          title: '图标',
          icon: 'remixicon-line',
        },
        children: [
          {
            path: 'remixIcon',
            name: 'RemixIcon',
            component: '@/views/demo/vab/icon/remixIcon',
            meta: {
              title: '小清新图标',
            },
          },
          {
            path: 'iconSelector',
            name: 'IconSelector',
            component: '@/views/demo/vab/icon/iconSelector',
            meta: {
              title: '图标选择器',
            },
          },
        ],
      },
      {
        path: 'roles',
        name: 'Role',
        component: '@/views/demo/vab/roles',
        meta: {
          title: '角色权限',
          icon: 'user-3-line',
          badge: 'Pro',
        },
      },
      {
        path: 'table',
        name: 'Table',
        meta: {
          title: '表格',
          roles: {
            role: ['editor'],
            mode: 'except',
          },
          icon: 'table-2',
        },
        children: [
          {
            path: 'comprehensiveTable',
            name: 'ComprehensiveTable',
            component: '@/views/demo/vab/table/comprehensiveTable',
            meta: {
              title: '综合表格',
            },
          },
          {
            path: 'detail',
            name: 'Detail',
            component: '@/views/demo/vab/table/detail',
            meta: {
              hidden: true,
              title: '详情页',
              activeMenu: '/vab/table/comprehensiveTable',
              dynamicNewTab: true,
            },
          },
          {
            path: 'inlineEditTable',
            name: 'InlineEditTable',
            component: '@/views/demo/vab/table/inlineEditTable',
            meta: {
              title: '行内编辑表格',
              noKeepAlive: true,
            },
          },
          {
            path: 'customTable',
            name: 'CustomTable',
            component: '@/views/demo/vab/table/customTable',
            meta: {
              title: '自定义表格',
            },
          },
        ],
      },
      {
        path: 'card',
        name: 'Card',
        component: '@/views/demo/vab/card',
        meta: {
          title: '卡片',
          roles: ['admin'],
          icon: 'inbox-line',
        },
      },
      {
        path: 'list',
        name: 'List',
        component: '@/views/demo/vab/list',
        meta: {
          title: '列表',
          roles: ['admin'],
          icon: 'list-check-2',
        },
      },
      {
        path: 'form',
        name: 'Form',
        meta: {
          title: '表单',
          roles: ['admin'],
          icon: 'file-list-2-line',
        },
        children: [
          {
            path: 'comprehensiveForm',
            name: 'ComprehensiveForm',
            component: '@/views/demo/vab/form/comprehensiveForm',
            meta: {
              title: '综合表单',
            },
          },
          {
            path: 'stepForm',
            name: 'StepForm',
            component: '@/views/demo/vab/form/stepForm',
            meta: {
              title: '分步表单',
            },
          },
          {
            path: 'button',
            name: 'Button',
            component: '@/views/demo/vab/form/button',
            meta: {
              title: '按钮',
            },
          },
          {
            path: 'link',
            name: 'Link',
            component: '@/views/demo/vab/form/link',
            meta: {
              title: '文字链接',
            },
          },
          {
            path: 'radio',
            name: 'Radio',
            component: '@/views/demo/vab/form/radio',
            meta: {
              title: '单选框',
            },
          },
          {
            path: 'checkbox',
            name: 'Checkbox',
            component: '@/views/demo/vab/form/checkbox',
            meta: {
              title: '多选框',
            },
          },
          {
            path: 'input',
            name: 'Input',
            component: '@/views/demo/vab/form/input',
            meta: {
              title: '输入框',
            },
          },
          {
            path: 'inputNumber',
            name: 'InputNumber',
            component: '@/views/demo/vab/form/inputNumber',
            meta: {
              title: '计数器',
            },
          },
          {
            path: 'select',
            name: 'Select',
            component: '@/views/demo/vab/form/select',
            meta: {
              title: '选择器',
            },
          },
          {
            path: 'switch',
            name: 'Switch',
            component: '@/views/demo/vab/form/switch',
            meta: {
              title: '开关',
            },
          },
          {
            path: 'slider',
            name: 'Slider',
            component: '@/views/demo/vab/form/slider',
            meta: {
              title: '滑块',
            },
          },
          {
            path: 'timePicker',
            name: 'TimePicker',
            component: '@/views/demo/vab/form/timePicker',
            meta: {
              title: '时间选择器',
            },
          },
          {
            path: 'datePicker',
            name: 'DatePicker',
            component: '@/views/demo/vab/form/datePicker',
            meta: {
              title: '日期选择器',
            },
          },
          {
            path: 'dateTimePicker',
            name: 'DateTimePicker',
            component: '@/views/demo/vab/form/dateTimePicker',
            meta: {
              title: '日期时间选择器',
            },
          },
          {
            path: 'rate',
            name: 'Rate',
            component: '@/views/demo/vab/form/rate',
            meta: {
              title: '评分',
            },
          },
        ],
      },
      {
        path: 'editor',
        name: 'Editor',
        meta: {
          title: '编辑器',
          roles: ['admin'],
          icon: 'edit-2-line',
        },
        children: [
          {
            path: 'richTextEditor',
            name: 'RichTextEditor',
            component: '@/views/demo/vab/editor/richTextEditor',
            meta: {
              title: '富文本编辑器',
              roles: ['admin'],
            },
          },
          {
            path: 'markdownEditor',
            name: 'MarkdownEditor',
            component: '@/views/demo/vab/editor/markdownEditor',
            meta: {
              title: 'Markdown编辑器',
              roles: ['admin'],
            },
          },
        ],
      },
    ],
  },

  {
    path: '/other',
    name: 'Other',
    component: 'Layout',
    meta: {
      title: '其他',
      icon: 'archive-line',
      roles: ['admin'],
    },
    children: [
      {
        path: 'workflow',
        name: 'Workflow',
        component: '@/views/demo/other/workflow',
        meta: {
          title: '工作流',
          roles: ['admin'],
          icon: 'flow-chart',
        },
      },
      {
        path: 'echarts',
        name: 'Echarts',
        component: '@/views/demo/other/echarts',
        meta: {
          title: '图表',
          roles: ['admin'],
          icon: 'bubble-chart-line',
        },
      },
      {
        path: 'print',
        name: 'Print',
        component: '@/views/demo/other/print',
        meta: {
          title: '打印',
          roles: ['admin'],
          icon: 'printer-line',
        },
      },
      {
        path: 'cropper',
        name: 'Cropper',
        component: '@/views/demo/other/cropper',
        meta: {
          title: '头像裁剪',
          roles: ['admin'],
          icon: 'crop-line',
        },
      },
      {
        path: 'notice',
        name: 'Notice',
        component: '@/views/demo/other/notice',
        meta: {
          title: '通知',
          roles: ['admin'],
          icon: 'message-2-line',
        },
      },
      {
        path: 'timeline',
        name: 'Timeline',
        component: '@/views/demo/other/timeline',
        meta: {
          title: '时间线',
          roles: ['admin'],
          icon: 'time-line',
        },
      },
      {
        path: 'count',
        name: 'Count',
        component: '@/views/demo/other/count',
        meta: {
          title: '数字自增长',
          roles: ['admin'],
          icon: 'number-9',
        },
      },
      {
        path: 'tabs',
        name: 'tabs',
        component: '@/views/demo/other/tabs',
        meta: {
          title: '多标签',
          roles: ['admin'],
          icon: 'bank-card-line',
        },
      },
      {
        path: 'dynamicMeta',
        name: 'DynamicMeta',
        component: '@/views/demo/other/dynamicMeta',
        meta: {
          title: '动态Meta',
          roles: ['admin'],
          icon: 'notification-badge-line',
          badge: '0',
        },
      },
      {
        path: 'dynamicSegment',
        name: 'DynamicSegment',
        redirect: '/other/dynamicSegment/test1/1',
        meta: {
          title: '动态路径参数',
          roles: ['admin'],
          icon: 'arrow-left-right-line',
        },
        children: [
          {
            path: 'test1/:id',
            name: 'Test1',
            component: '@/views/demo/other/dynamicSegment/test1',
            meta: {
              hidden: true,
              title: 'Params',
              dynamicNewTab: true,
            },
          },
          {
            path: 'test1/1',
            name: 'test1/1',
            component: '@/views/demo/other/dynamicSegment/test1',
            meta: { title: 'Params id=1' },
          },
          {
            path: 'test2',
            name: 'Test2',
            component: '@/views/demo/other/dynamicSegment/test2',
            meta: {
              hidden: true,
              title: 'Query',
              dynamicNewTab: true,
            },
          },
          {
            path: 'test2?id=1',
            name: 'test2?id=1',
            component: '@/views/demo/other/dynamicSegment/test2',
            meta: { title: 'Query id=1' },
          },
        ],
      },
      {
        path: 'drag',
        name: 'Drag',
        meta: {
          title: '拖拽',
          roles: ['admin'],
          icon: 'drag-drop-line',
        },
        children: [
          {
            path: 'dialogDrag',
            name: 'DialogDrag',
            component: '@/views/demo/other/drag/dialogDrag',
            meta: {
              title: '弹窗拖拽',
            },
          },
          {
            path: 'cardDrag',
            name: 'CardDrag',
            component: '@/views/demo/other/drag/cardDrag',
            meta: {
              title: '卡片拖拽',
            },
          },
          {
            path: 'flowSheetDrag',
            name: 'FlowSheetDrag',
            component: '@/views/demo/other/drag/flowSheetDrag',
            meta: {
              title: '流程图拖拽',
              noKeepAlive: true,
            },
          },
        ],
      },
      {
        path: 'contextmenu',
        name: 'Contextmenu',
        component: '@/views/demo/other/contextmenu',
        meta: {
          title: '右键菜单',
          roles: ['admin'],
          icon: 'menu-2-fill',
        },
      },
      {
        path: 'loading',
        name: 'Loading',
        component: '@/views/demo/other/loading',
        meta: {
          title: '加载',
          roles: ['admin'],
          icon: 'loader-line',
        },
      },
      {
        path: 'player',
        name: 'Player',
        component: '@/views/demo/other/player',
        meta: {
          title: '视频播放器',
          roles: ['admin'],
          icon: 'video-line',
          noKeepAlive: true,
        },
      },
      {
        path: 'upload',
        name: 'Upload',
        component: '@/views/demo/other/upload',
        meta: {
          title: '上传',
          roles: ['admin'],
          icon: 'chat-upload-line',
        },
      },
      {
        path: 'menu1',
        name: 'Menu1',
        meta: {
          title: '多级路由缓存',
          roles: ['admin'],
          icon: 'route-line',
        },
        children: [
          {
            path: 'menu1-1',
            name: 'Menu11',
            meta: {
              title: '多级路由1-1',
            },
            children: [
              {
                path: 'menu1-1-1',
                name: 'Menu111',
                meta: {
                  title: '多级路由1-1-1',
                },
                children: [
                  {
                    path: 'menu1-1-1-1',
                    name: 'Menu1111',
                    meta: {
                      title: '多级路由1-1-1-1',
                    },
                    component:
                      '@/views/demo/other/nested/menu1/menu1-1/menu1-1-1/menu1-1-1-1',
                  },
                ],
              },
            ],
          },
        ],
      },
      {
        path: 'log',
        name: 'Log',
        component: '@/views/demo/other/errorLog',
        meta: {
          title: '错误日志模拟',
          roles: ['admin'],
          icon: 'error-warning-line',
        },
      },
      {
        path: 'cssfx',
        name: 'Cssfx',
        component: '@/views/demo/other/cssfx',
        meta: {
          title: 'Css动画',
          roles: ['admin'],
          icon: 'css3-line',
        },
      },
      {
        path: 'social',
        name: 'Social',
        component: '@/views/demo/other/social',
        meta: {
          title: '第三方登录',
          roles: ['admin'],
          icon: 'github-fill',
        },
      },
      {
        path: '//github.com/chuzhixin/vue-admin-beautiful?utm_source=gold_browser_extension',
        name: 'ExternalLink',
        meta: {
          title: '外链',
          target: '_blank',
          roles: {
            role: ['admin', 'editor'],
            mode: 'oneOf',
          },
          icon: 'external-link-line',
        },
      },
      {
        path: 'iframe',
        name: 'Iframe',
        redirect: '/other/iframe/search',
        meta: {
          title: 'Iframe',
          roles: ['admin'],
          icon: 'window-line',
        },
        children: [
          {
            path: 'view',
            name: 'IframeView',
            component: '@/views/demo/other/iframe/view',
            meta: {
              hidden: true,
              title: 'Iframe',
              icon: 'window-line',
              dynamicNewTab: true,
            },
          },
          {
            path: 'view?url=https%3A%2Fwww.baidu.com&title=%E7%99%BE%E5%BA%A6',
            name: 'baiduIframe',
            component: '@/views/demo/other/iframe/view',
            meta: { title: '百度', icon: 'baidu-fill' },
          },
          {
            path: 'view?url=https%3A%2Fgitee.com%2Fchu1204505056%2Fvue-admin-beautiful&title=Gitee',
            name: 'githubIframe',
            component: '@/views/demo/other/iframe/view',
            meta: { title: 'Gitee', icon: 'github-fill' },
          },
          {
            path: 'search',
            name: 'IframeSearch',
            component: '@/views/demo/other/iframe/search',
            meta: {
              title: '自定义Iframe',
              icon: 'search-2-line',
            },
          },
        ],
      },
      {
        path: 'excel',
        name: 'Excel',
        meta: {
          title: 'Excel',
          roles: ['admin'],
          icon: 'file-excel-2-line',
        },
        children: [
          {
            path: 'exportExcel',
            name: 'ExportExcel',
            component: '@/views/demo/other/excel/exportExcel',
            meta: {
              title: '导出Excel',
            },
          },
          {
            path: 'exportSelectedExcel',
            name: 'SelectExcel',
            component: '@/views/demo/other/excel/exportSelectExcel',
            meta: {
              title: '导出选中行Excel',
            },
          },
          {
            path: 'exportMergeHeaderExcel',
            name: 'MergeHeaderExcel',
            component: '@/views/demo/other/excel/exportMergeHeaderExcel',
            meta: {
              title: '导出合并Excel',
            },
          },
        ],
      },
    ],
  },
  {
    path: '/mall',
    name: 'Mall',
    component: 'Layout',
    meta: {
      title: '物料源',
      icon: 'apps-line',
      roles: ['admin'],
      levelHidden: true,
    },
    children: [
      {
        path: 'goods',
        name: 'Goods',
        component: '@/views/demo/mall/goods',
        meta: {
          title: '物料市场',
          icon: 'shopping-cart-line',
          badge: 'Hot',
        },
      },
    ],
  },
  {
    path: '/setting',
    name: 'PersonnelManagement',
    component: 'Layout',
    meta: {
      title: '配置',
      icon: 'user-settings-line',
      roles: ['admin'],
    },
    children: [
      {
        path: 'personalCenter',
        name: 'PersonalCenter',
        component: '@/views/demo/setting/personalCenter',
        meta: {
          title: '个人中心',
          icon: 'map-pin-user-line',
        },
      },
      {
        path: 'userManagement',
        name: 'UserManagement',
        component: '@/views/demo/setting/userManagement',
        meta: {
          title: '用户管理',
          icon: 'user-3-line',
        },
      },
      {
        path: 'roleManagement',
        name: 'RoleManagement',
        component: '@/views/demo/setting/roleManagement',
        meta: {
          title: '角色管理',
          icon: 'admin-line',
        },
      },
      {
        path: 'departmentManagement',
        name: 'DepartmentManagement',
        component: '@/views/demo/setting/departmentManagement',
        meta: {
          title: '部门管理',
          icon: 'group-line',
        },
      },
      {
        path: 'menuManagement',
        name: 'MenuManagement',
        component: '@/views/demo/setting/menuManagement',
        meta: {
          title: '菜单管理',
          icon: 'menu-2-fill',
        },
      },
      {
        path: 'systemLog',
        name: 'SystemLog',
        component: '@/views/demo/setting/systemLog',
        meta: {
          title: '系统日志',
          icon: 'file-shield-2-line',
        },
      },
    ],
  },
  {
    path: '/error',
    name: 'Error',
    component: 'Layout',
    meta: {
      title: '错误页',
      icon: 'error-warning-line',
      levelHidden: true,
    },
    children: [
      {
        path: '403',
        name: 'Error403',
        component: '@/views/403',
        meta: {
          title: '403',
          icon: 'error-warning-line',
        },
      },
      {
        path: '404',
        name: 'Error404',
        component: '@/views/404',
        meta: {
          title: '404',
          icon: 'error-warning-line',
        },
      },
    ],
  },
]

module.exports = [
  {
    url: '/router/getList',
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
