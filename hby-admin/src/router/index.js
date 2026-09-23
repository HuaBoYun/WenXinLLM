/**
 * @description router全局配置，如有必要可分文件抽离，其中asyncRoutes只有在intelligence模式下才会用到，pro版只支持remixIcon图标，具体配置请查看vip群文档
 */
import { publicPath, routerMode } from '@/config'
import Layout from '@/vab/layouts'
import Vue from 'vue'
import VueRouter from 'vue-router'
import ncv65Router from './modules/ncv65'
import systemMonitorRouter from './modules/systemMonitor'

let hiddenWorkbench = true
// let hiddenWorkbench = checkHiddenWorkbench()
// function checkHiddenWorkbench() {
// 	// 打包时要打开
// 	var moduleName = localStorage.getItem('model')
// 	if (moduleName === 'htgl' || moduleName === 'znsj' || moduleName == 'nkhg' || moduleName == 'fxgk') {
// 		return true
// 	}
// 	return true
// }
//单独智能审计的工作台
let isZNSJ = checkHiddenWorkbench()
function checkHiddenWorkbench() {
	var moduleName = localStorage.getItem('model')
	if (moduleName === 'znsj' || moduleName === 'nkhg' || moduleName === 'fxgk') {
		return false
	}
	return true
}

function checkHiddenNowModo() {
	var moduleName = localStorage.getItem('model')
	if (moduleName === 'nkhg') {
		return false
	}
	return true
}
Vue.use(VueRouter)
export const constantRoutes = [
	{
		path: '/login',
		component: () => import('@/views/login'),
		meta: {
			hidden: true,
		},
	},
	{
		path: '/register',
		component: () => import('@/views/demo/register'),
		meta: {
			hidden: true,
		},
	},
	{
		path: '/callback',
		component: () => import('@/views/demo/callback'),
		meta: {
			hidden: true,
		},
	},
	{
		path: '/403',
		name: '403',
		component: () => import('@/views/403'),
		meta: {
			hidden: true,
		},
	},
	{
		path: '/404',
		name: '404',
		component: () => import('@/views/404'),
		meta: {
			hidden: true,
		},
	},
	{
		path: '/htgl',
		name: 'htgl',
		component: () => import('@/views/contract/home/index.vue'),
		meta: {
			hidden: true,
		},
	},
	{
		path: '/fwgl',
		name: 'fwgl',
		component: () => import('@/views/fwgl/home/index.vue'),
		meta: {
			hidden: true,
		},
	},
	{
		path: '/znsj',
		name: 'znsj',
		component: () => import('@/views/audit/home/sjfx.vue'),
		meta: {
			hidden: true,
		},
	},
	{
		path: '/shenpi',
		name: 'shenpi',
		component: () => import('@/views/msg/components/options/mobileDb.vue'),
		meta: {
			hidden: true,
		},
	},
	{
		path: '/msg',
		name: 'msgcenter',
		component: Layout,
		meta: {
			hidden: true,
			title: '消息中心',
			icon: 'menu-notice',
		},
		children: [
			{
				path: 'wddb',
				name: 'wddb',
				component: () => import('@/views/msg/components/wddb'),
				meta: {
					title: '待办事宜',
					icon: 'menu-ls',
				},
			},
			{
				path: 'wdcy',
				name: 'wdcy',
				component: () => import('@/views/msg/components/wdcy'),
				meta: {
					title: '已办事宜',
					icon: 'menu-search',
				},
			},
			{
				path: 'wfqd',
				name: 'wfqd',
				component: () => import('@/views/msg/components/wfqd'),
				meta: {
					title: '我发起的',
					icon: 'menu-comment',
				},
			},
			{
				path: 'cssy',
				name: 'cssy',
				component: () => import('@/views/msg/components/cssy'),
				meta: {
					title: '知会事宜',
					icon: 'menu-monitor',
				},
			},
			{
				path: 'dfsy',
				name: 'dfsy',
				component: () => import('@/views/msg/components/dfsy'),
				meta: {
					title: '待发事宜',
					icon: 'menu-data',
				},
			},
			{
				path: 'bhsy',
				name: 'bhsy',
				component: () => import('@/views/msg/components/bhsy'),
				meta: {
					title: '驳回事宜',
					icon: 'menu-data',
				},
			},
			{
				path: 'oadb',
				name: 'oadb',
				component: () => import('@/views/msg/components/oadb'),
				meta: {
					title: 'OA待办',
					icon: 'menu-search',
				},
			},
			{
				path: 'oayb',
				name: 'oayb',
				component: () => import('@/views/msg/components/oayb'),
				meta: {
					title: 'OA已办',
					icon: 'menu-handle',
				},
			},
			{
				path: 'wysx',
				name: 'wysx',
				component: () => import('@/views/msg/components/wysx'),
				meta: {
					title: '未阅事项',
					icon: 'menu-handle',
				},
			},
			{
				path: 'yysx',
				name: 'yysx',
				component: () => import('@/views/msg/components/yysx'),
				meta: {
					title: '已阅事项',
					icon: 'menu-handle',
				},
			},
		],
	},

	{
		path: '/shenji',
		name: 'Root',
		component: Layout,
		children: [
			{
				path: 'statement/profitSheet',
				name: 'profitSheet',
				component: () =>
					import('@/views/workbench/companyData/reportForm/profitSheet'),
				meta: {
					title: '利润表',
					hidden: true,
				},
			},
			{
				path: 'statement/balanceSheet',
				name: 'balanceSheet',
				component: () =>
					import('@/views/workbench/companyData/reportForm/balanceSheet'),
				meta: {
					title: '资产负债表',
					hidden: true,
				},
			},
			{
				path: 'accountData',
				name: 'AccountData',
				hidden: true,
				component: () =>
					import('@/views/workbench/companyData/accountData'),
				meta: {
					title: '账簿数据',
					icon: 'menu-fuzhu',

				},
			},
			{
				path: 'accountManage',
				name: 'AccountManage',

				component: () =>
					import('@/views/workbench/companyData/accountManage'),
				meta: {
					title: '账簿管理',
					icon: 'menu-account',

				},
			},

			{
				path: 'AI',
				name: 'AI',
				component: () =>
					import('@/views/workbench/companyData/sub/ai'),
				meta: {
					title: 'AI问答',
				},
			},
		],
	},
	{
		path: '/workbench',
		name: 'workbench',
		component: Layout,
		meta: {
			title: '工作台',
			icon: 'menu-work',
			hidden: hiddenWorkbench,
		},
		children: [
			{
				path: 'industryData',
				name: 'IndustryData',
				meta: {
					title: '行业数据',
					icon: 'menu-kemu',

				},
				children: [
					{
						path: 'telescope',
						name: 'Telescope',
						component: () => import('@/views/workbench/industryData/telescope'),
						meta: {
							title: '望远镜',
							icon: 'menu-fx',

						},
					},
				],
			},
			{
				path: 'index',
				name: 'Index',
				component: () => import('@/views/workbench/index'),
				meta: {
					title: '工作台',
					icon: 'menu-work',

				},
			},


			{
				path: 'companyData',
				name: 'CompanyData',
				meta: {
					title: '企业数据',
					icon: 'menu-org',

				},
				children: [
					{
						path: 'statement',
						name: 'Statement',
						component: () => import('@/views/workbench/companyData/statement'),
						meta: {
							title: '报表数据',
							icon: 'menu-baobiao',

						},
					},
					{
						path: 'accountData',
						name: 'AccountData',
						hidden: true,
						component: () =>
							import('@/views/workbench/companyData/accountData'),
						meta: {
							title: '账簿数据',
							icon: 'menu-fuzhu',

						},
					},
					{
						path: 'accountManage',
						name: 'AccountManage',

						component: () =>
							import('@/views/workbench/companyData/accountManage'),
						meta: {
							title: '账簿管理',
							icon: 'menu-account',

						},
					},
					{
						path: 'accountData/subject',
						name: 'Subject',
						hidden: true,
						component: () =>
							import('@/views/workbench/companyData/sub/subject'),
						meta: {
							title: '科目表',

						},
					},
					{
						path: 'accountData/balance',
						name: 'Balance',
						hidden: true,
						component: () =>
							import('@/views/workbench/companyData/sub/balance'),
						meta: {
							title: '余额表',

						},
					},
					{
						path: 'accountData/accountCate',
						name: 'AccountCate',
						hidden: true,
						component: () =>
							import('@/views/workbench/companyData/sub/accountCate'),
						meta: {
							title: '总账分类',

						},
					},
					{
						path: 'accountData/accountDetail',
						name: 'AccountDetail',
						component: () =>
							import('@/views/workbench/companyData/sub/accountDetail'),
						meta: {
							title: '明细账',

						},
					},
					{
						path: 'accountData/accountDiary',
						name: 'AccountDiary',
						component: () =>
							import('@/views/workbench/companyData/sub/accountDiary'),
						meta: {
							title: '日记账',

						},
					},
					{
						path: 'accountData/accountAssist',
						name: 'AccountAssist',
						component: () =>
							import('@/views/workbench/companyData/sub/accountAssist'),
						meta: {
							title: '辅助账',

						},
					},
					{
						path: 'accountData/voucherLib',
						name: 'VoucherLib',
						component: () =>
							import('@/views/workbench/companyData/sub/voucherLib'),
						meta: {
							title: '凭证库',

						},
					},
					{
						path: 'accountData/businessData',
						name: 'BusinessData',
						component: () =>
							import('@/views/workbench/companyData/sub/businessData'),
						meta: {
							title: '业务数据',

						},
					},
					{
						path: 'magnifyingGlass',
						name: 'MagnifyingGlass',
						component: () =>
							import('@/views/workbench/auditTools/magnifyingGlass'),
						meta: {
							title: '放大镜',
							icon: 'menu-fx',

						},
					},
				],
			},
			{
				path: 'controlLib',
				name: 'ControlLib',
				meta: {
					title: '合规标准',
					icon: 'menu-kemu',

				},
				children: [

					{
						path: 'lawSearch',
						name: 'LawSearch',
						component: () => import('@/views/workbench/auditTools/lawSearch'),
						meta: {
							title: '法律搜索',
							icon: 'menu-search',

						},
					},

					{
						path: 'law',
						name: 'WorkbenchLaw',
						component: () => import('@/views/workbench/controlLib/law'),
						meta: {
							title: '法律规章',
							icon: 'menu-handle',
						},
					},
					{
						path: 'flfgk',
						name: 'flfgk',
						component: () => import('@/views/internal/new/hggfk/flfgk'),
						meta: {
							title: '法律法规库',
							icon: 'menu-handle',

						},
					},
					{
						path: 'orderSearch',
						name: 'OrderSearch',
						component: () => import('@/views/workbench/auditTools/orderSearch'),
						meta: {
							title: '制度搜索',
							icon: 'menu-search',

						},
					},
					{
						path: 'jtgsgzzdk',
						name: 'jtgsgzzdk',
						component: () => import('@/views/internal/new/hggfk/jtgsgzzdk'),
						meta: {
							title: '集团规章制度',
							icon: 'menu-handle',

						},
					},
					{
						path: 'manage',
						name: 'Manage',
						component: () => import('@/views/workbench/controlLib/manage'),
						meta: {
							title: '管理制度',
							icon: 'menu-ls',

						},
					},
					{
						path: 'knowledge',
						name: 'Knowledge',
						component: () => import('@/views/workbench/controlLib/knowledge'),
						meta: {
							title: '行业知识库',
							icon: 'menu-lixiang',

						},
					},
				],
			},
		],
	},
]

export const backendRoutes = [
	{
		path: '/workbenchZNSJ',
		name: 'workbenchZNSJ',
		component: Layout,
		meta: {
			title: '管理制度',
			icon: 'menu-work',
			hidden: isZNSJ,
		},
		children: [
			{
				path: 'controlLib/groupmanage',
				name: 'groupmanage',
				component: () => import('@/views/workbench/controlLib/manage'),
				meta: {
					title: '集团制度',
					icon: 'menu-ls',
				},
				perms: 'ManageSystem',
			},
			{
				path: 'controlLib/sharesmanage',
				name: 'sharesmanage',
				component: () => import('@/views/workbench/controlLib/manage'),
				meta: {
					title: '矿办制度',
					icon: 'menu-ls',
				},
				perms: 'ManageSystem',

			},
			{
				path: 'controlLib/companymanage',
				name: 'companymanage',
				component: () => import('@/views/workbench/controlLib/manage'),
				meta: {
					title: '公司制度',
					icon: 'menu-ls',
				},
				perms: 'ManageSystem',

			},
			{
				path: 'controlLib/law',
				name: 'ZNSJLaw',
				component: () => import('@/views/workbench/controlLib/law'),
				meta: {
					title: '法律章程',
					icon: 'menu-handle',
				},
				perms: 'ManageSystem',
			},
			{
				path: 'controlLib/knowledge',
				name: 'ZNSJKnowledge',
				component: () => import('@/views/workbench/controlLib/knowledgePlatform'),
				meta: {
					title: '知识平台',
					icon: 'menu-handle',
				},
				perms: 'ManageSystem',
			},
			{
				path: 'controlLib/nowModo',
				name: 'ZNSJNowModel',
				component: () => import('@/views/internal/internalTest/nowModo'),
				meta: {
					title: '现行标准',
					icon: 'menu-account',
					hidden: checkHiddenNowModo(),
				},
				perms: 'ManageSystem',
			},
			// {
			// 	path: 'controlLib',
			// 	name: 'ControlLib',
			// 	meta: {
			// 		title: '规章制度',
			// 		icon: 'menu-kemu',
			// 	},
			// 	children: [

			// 	],
			// },
			// {
			// 	path: 'https://flk.npc.gov.cn/',
			// 	name: 'CountryLaw',
			// 	meta: {
			// 		title: '国家法律法规数据库',
			// 		icon: 'menu-customer',
			// 		target: '_blank',
			// 	},
			// },
		],
	},
]

export const asyncRoutes = [
	{
		path: '/',
		name: 'Root',
		component: Layout,
		meta: {
			title: '首页',
			icon: 'home-2-line',
		},
		children: [
			{
				path: 'index',
				name: 'Index',
				component: () => import('@/views/demo/index'),
				meta: {
					title: '首页',
					icon: 'home-2-line',
					noClosable: true,
				},
			},
			{
				path: 'dashboard',
				name: 'Dashboard',
				component: () => import('@/views/demo/index/dashboard'),
				meta: {
					title: '看板',
					icon: 'dashboard-line',
				},
			},
			{
				path: 'workbench',
				name: 'Workbench',
				component: () => import('@/views/demo/index/workbench'),
				meta: {
					title: '工作台',
					icon: 'settings-6-line',
					dot: true,
				},
			},
		],
	},
	{
		path: '/vab',
		name: 'Vab',
		component: Layout,
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
						component: () => import('@/views/demo/vab/icon/remixIcon'),
						meta: {
							title: '小清新图标',
						},
					},
					{
						path: 'iconSelector',
						name: 'IconSelector',
						component: () => import('@/views/demo/vab/icon/iconSelector'),
						meta: {
							title: '图标选择器',
						},
					},
				],
			},
			{
				path: 'roles',
				name: 'Role',
				component: () => import('@/views/demo/vab/roles'),
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
					// 非editor角色的用户可见
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
						component: () =>
							import('@/views/demo/vab/table/comprehensiveTable'),
						meta: {
							title: '综合表格',
						},
					},
					{
						path: 'detail',
						name: 'Detail',
						component: () => import('@/views/demo/vab/table/detail'),
						meta: {
							hidden: true,
							title: '详情页',
							activeMenu: '/vab/table/comprehensiveTable',
							dynamicNewTab: true, //详情页根据id传参不同可打开多个
						},
					},
					{
						path: 'inlineEditTable',
						name: 'InlineEditTable',
						component: () => import('@/views/demo/vab/table/inlineEditTable'),
						meta: {
							title: '行内编辑表格',
							noKeepAlive: true,
						},
					},
					{
						path: 'customTable',
						name: 'CustomTable',
						component: () => import('@/views/demo/vab/table/customTable'),
						meta: {
							title: '自定义表格',
						},
					},
				],
			},
			{
				path: 'card',
				name: 'Card',
				component: () => import('@/views/demo/vab/card'),
				meta: {
					title: '卡片',
					roles: ['admin'],
					icon: 'inbox-line',
				},
			},
			{
				path: 'list',
				name: 'List',
				component: () => import('@/views/demo/vab/list'),
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
						component: () => import('@/views/demo/vab/form/comprehensiveForm'),
						meta: {
							title: '综合表单',
						},
					},
					{
						path: 'stepForm',
						name: 'StepForm',
						component: () => import('@/views/demo/vab/form/stepForm'),
						meta: {
							title: '分步表单',
						},
					},
					{
						path: 'button',
						name: 'Button',
						component: () => import('@/views/demo/vab/form/button'),
						meta: {
							title: '按钮',
						},
					},
					{
						path: 'link',
						name: 'Link',
						component: () => import('@/views/demo/vab/form/link'),
						meta: {
							title: '文字链接',
						},
					},
					{
						path: 'radio',
						name: 'Radio',
						component: () => import('@/views/demo/vab/form/radio'),
						meta: {
							title: '单选框',
						},
					},
					{
						path: 'checkbox',
						name: 'Checkbox',
						component: () => import('@/views/demo/vab/form/checkbox'),
						meta: {
							title: '多选框',
						},
					},
					{
						path: 'input',
						name: 'Input',
						component: () => import('@/views/demo/vab/form/input'),
						meta: {
							title: '输入框',
						},
					},
					{
						path: 'inputNumber',
						name: 'InputNumber',
						component: () => import('@/views/demo/vab/form/inputNumber'),
						meta: {
							title: '计数器',
						},
					},
					{
						path: 'select',
						name: 'Select',
						component: () => import('@/views/demo/vab/form/select'),
						meta: {
							title: '选择器',
						},
					},
					{
						path: 'switch',
						name: 'Switch',
						component: () => import('@/views/demo/vab/form/switch'),
						meta: {
							title: '开关',
						},
					},
					{
						path: 'slider',
						name: 'Slider',
						component: () => import('@/views/demo/vab/form/slider'),
						meta: {
							title: '滑块',
						},
					},
					{
						path: 'timePicker',
						name: 'TimePicker',
						component: () => import('@/views/demo/vab/form/timePicker'),
						meta: {
							title: '时间选择器',
						},
					},
					{
						path: 'datePicker',
						name: 'DatePicker',
						component: () => import('@/views/demo/vab/form/datePicker'),
						meta: {
							title: '日期选择器',
						},
					},
					{
						path: 'dateTimePicker',
						name: 'DateTimePicker',
						component: () => import('@/views/demo/vab/form/dateTimePicker'),
						meta: {
							title: '日期时间选择器',
						},
					},
					{
						path: 'rate',
						name: 'Rate',
						component: () => import('@/views/demo/vab/form/rate'),
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
						component: () => import('@/views/demo/vab/editor/richTextEditor'),
						meta: {
							title: '富文本编辑器',
							roles: ['admin'],
						},
					},
					{
						path: 'markdownEditor',
						name: 'MarkdownEditor',
						component: () => import('@/views/demo/vab/editor/markdownEditor'),
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
		component: Layout,
		meta: {
			title: '其他',
			icon: 'archive-line',
			roles: ['admin'],
		},
		children: [
			{
				path: 'workflow',
				name: 'Workflow',
				component: () => import('@/views/demo/other/workflow'),
				meta: {
					title: '工作流',
					roles: ['admin'],
					icon: 'flow-chart',
				},
			},
			{
				path: 'echarts',
				name: 'Echarts',
				component: () => import('@/views/demo/other/echarts'),
				meta: {
					title: '图表',
					roles: ['admin'],
					icon: 'bubble-chart-line',
				},
			},
			{
				path: 'print',
				name: 'Print',
				component: () => import('@/views/demo/other/print'),
				meta: {
					title: '打印',
					roles: ['admin'],
					icon: 'printer-line',
				},
			},
			{
				path: 'cropper',
				name: 'Cropper',
				component: () => import('@/views/demo/other/cropper'),
				meta: {
					title: '头像裁剪',
					roles: ['admin'],
					icon: 'crop-line',
				},
			},
			{
				path: 'notice',
				name: 'Notice',
				component: () => import('@/views/demo/other/notice'),
				meta: {
					title: '通知',
					roles: ['admin'],
					icon: 'message-2-line',
				},
			},
			{
				path: 'timeline',
				name: 'Timeline',
				component: () => import('@/views/demo/other/timeline'),
				meta: {
					title: '时间线',
					roles: ['admin'],
					icon: 'time-line',
				},
			},
			{
				path: 'count',
				name: 'Count',
				component: () => import('@/views/demo/other/count'),
				meta: {
					title: '数字自增长',
					roles: ['admin'],
					icon: 'number-9',
				},
			},
			{
				path: 'tabs',
				name: 'tabs',
				component: () => import('@/views/demo/other/tabs'),
				meta: {
					title: '多标签',
					roles: ['admin'],
					icon: 'bank-card-line',
				},
			},
			{
				path: 'dynamicMeta',
				name: 'DynamicMeta',
				component: () => import('@/views/demo/other/dynamicMeta'),
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
						component: () => import('@/views/demo/other/dynamicSegment/test1'),
						meta: {
							hidden: true,
							title: 'Params',
							dynamicNewTab: true,
						},
					},
					{
						path: 'test1/1',
						name: 'test1/1',
						component: () => import('@/views/demo/other/dynamicSegment/test1'),
						meta: { title: 'Params id=1' },
					},
					{
						path: 'test2',
						name: 'Test2',
						component: () => import('@/views/demo/other/dynamicSegment/test2'),
						meta: {
							hidden: true,
							title: 'Query',
							dynamicNewTab: true,
						},
					},
					{
						path: 'test2?id=1',
						name: 'test2?id=1',
						component: () => import('@/views/demo/other/dynamicSegment/test2'),
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
						component: () => import('@/views/demo/other/drag/dialogDrag'),
						meta: {
							title: '弹窗拖拽',
						},
					},
					{
						path: 'cardDrag',
						name: 'CardDrag',
						component: () => import('@/views/demo/other/drag/cardDrag'),
						meta: {
							title: '卡片拖拽',
						},
					},
					{
						path: 'flowSheetDrag',
						name: 'FlowSheetDrag',
						component: () => import('@/views/demo/other/drag/flowSheetDrag'),
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
				component: () => import('@/views/demo/other/contextmenu'),
				meta: {
					title: '右键菜单',
					roles: ['admin'],
					icon: 'menu-2-fill',
				},
			},
			{
				path: 'loading',
				name: 'Loading',
				component: () => import('@/views/demo/other/loading'),
				meta: {
					title: '加载',
					roles: ['admin'],
					icon: 'loader-line',
				},
			},
			{
				path: 'player',
				name: 'Player',
				component: () => import('@/views/demo/other/player'),
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
				component: () => import('@/views/demo/other/upload'),
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
										component: () =>
											import(
												'@/views/demo/other/nested/menu1/menu1-1/menu1-1-1/menu1-1-1-1'
											),
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
				component: () => import('@/views/demo/other/errorLog'),
				meta: {
					title: '错误日志模拟',
					roles: ['admin'],
					icon: 'error-warning-line',
				},
			},
			{
				path: 'cssfx',
				name: 'Cssfx',
				component: () => import('@/views/demo/other/cssfx'),
				meta: {
					title: 'Css动画',
					roles: ['admin'],
					icon: 'css3-line',
				},
			},
			{
				path: 'social',
				name: 'Social',
				component: () => import('@/views/demo/other/social'),
				meta: {
					title: '第三方登录',
					roles: ['admin'],
					icon: 'github-fill',
				},
			},
			// {
			//   path: 'mobilePreview',
			//   name: 'MobilePreview',
			//   component: () => import('@/views/vab/mobilePreview'),
			//   meta: {
			//     title: '手机预览',
			//     roles: ['admin'],
			//     icon: 'smartphone-line',
			//   },
			// },
			{
				path: '//github.com/chuzhixin/vue-admin-beautiful?utm_source=gold_browser_extension',
				name: 'ExternalLink',
				meta: {
					title: '外链',
					target: '_blank',
					// 等价roles: ['admin', 'editor'],
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
						component: () => import('@/views/demo/other/iframe/view'),
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
						component: () => import('@/views/demo/other/iframe/view'),
						meta: { title: '百度', icon: 'baidu-fill' },
					},
					{
						path: 'view?url=https%3A%2Fgitee.com%2Fchu1204505056%2Fvue-admin-beautiful&title=Gitee',
						name: 'githubIframe',
						component: () => import('@/views/demo/other/iframe/view'),
						meta: { title: 'Gitee', icon: 'github-fill' },
					},
					{
						path: 'search',
						name: 'IframeSearch',
						component: () => import('@/views/demo/other/iframe/search'),
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
						component: () => import('@/views/demo/other/excel/exportExcel'),
						meta: {
							title: '导出Excel',
						},
					},
					{
						path: 'exportSelectedExcel',
						name: 'SelectExcel',
						component: () =>
							import('@/views/demo/other/excel/exportSelectExcel'),
						meta: {
							title: '导出选中行Excel',
						},
					},
					{
						path: 'exportMergeHeaderExcel',
						name: 'MergeHeaderExcel',
						component: () =>
							import('@/views/demo/other/excel/exportMergeHeaderExcel'),
						meta: {
							title: '导出合并Excel',
						},
					},
				],
			},
		],
	},
	// {
	//   path: '/mall',
	//   name: 'Mall',
	//   component: Layout,
	//   meta: {
	//     title: '物料源',
	//     icon: 'apps-line',
	//     levelHidden: true,
	//     roles: ['admin'],
	//   },
	//   children: [
	//     {
	//       path: 'goods',
	//       name: 'Goods',
	//       component: () => import('@/views/mall/goods'),
	//       meta: {
	//         title: '物料市场',
	//         icon: 'shopping-cart-line',
	//         badge: 'Hot',
	//       },
	//     },
	//   ],
	// },
	{
		path: '/setting',
		name: 'PersonnelManagement',
		component: Layout,
		meta: {
			title: '配置',
			icon: 'user-settings-line',
			roles: ['admin'],
		},
		children: [
			{
				path: 'personalCenter',
				name: 'PersonalCenter',
				component: () => import('@/views/demo/setting/personalCenter'),
				meta: {
					title: '个人中心',
					icon: 'map-pin-user-line',
				},
			},
			{
				path: 'userManagement',
				name: 'UserManagement',
				component: () => import('@/views/demo/setting/userManagement'),
				meta: {
					title: '用户管理',
					icon: 'user-3-line',
				},
			},
			{
				path: 'roleManagement',
				name: 'RoleManagement',
				component: () => import('@/views/demo/setting/roleManagement'),
				meta: {
					title: '角色管理',
					icon: 'admin-line',
				},
			},
			{
				path: 'departmentManagement',
				name: 'DepartmentManagement',
				component: () => import('@/views/demo/setting/departmentManagement'),
				meta: {
					title: '部门管理',
					icon: 'group-line',
				},
			},
			{
				path: 'menuManagement',
				name: 'MenuManagement',
				component: () => import('@/views/demo/setting/menuManagement'),
				meta: {
					title: '菜单管理',
					icon: 'menu-2-fill',
				},
			},
			{
				path: 'systemLog',
				name: 'SystemLog',
				component: () => import('@/views/demo/setting/systemLog'),
				meta: {
					title: '系统日志',
					icon: 'file-shield-2-line',
				},
			},
			// ========== 计费管理 ==========
			{
				path: 'fee/standard',
				name: 'FeeStandard',
				component: () => import('@/views/setting/fee/standard'),
				meta: {
					title: '费用标准配置',
					icon: 'money-cny-circle-line',
				},
			},
			{
				path: 'fee/personal',
				name: 'FeePersonal',
				component: () => import('@/views/setting/fee/personal'),
				meta: {
					title: '个人费用查询',
					icon: 'file-list-3-line',
				},
			},
			{
				path: 'fee/statistics',
				name: 'FeeStatistics',
				component: () => import('@/views/setting/fee/statistics'),
				meta: {
					title: '统计费用查询',
					icon: 'bar-chart-box-line',
				},
			},
			{
				path: 'fee/recharge',
				name: 'FeeRecharge',
				component: () => import('@/views/setting/fee/recharge'),
				meta: {
					title: '密钥充值',
					icon: 'wallet-3-line',
				},
			},
			{
				path: 'fee/purchase',
				name: 'FeePurchase',
				component: () => import('@/views/setting/fee/purchase'),
				meta: {
					title: '密钥购买',
					icon: 'shopping-cart-line',
				},
			},
		],
	},
	{
		path: '/error',
		name: 'Error',
		component: Layout,
		meta: {
			title: '错误页',
			icon: 'error-warning-line',
			levelHidden: true,
		},
		children: [
			{
				path: '403',
				name: 'Error403',
				component: () => import('@/views/403'),
				meta: {
					title: '403',
					icon: 'error-warning-line',
					// tabHidden: true,
				},
			},
			{
				path: '404',
				name: 'Error404',
				component: () => import('@/views/404'),
				meta: {
					title: '404',
					icon: 'error-warning-line',
					// tabHidden: true,
				},
			},
		],
	},
	// 全球司库路由配置
	{
		path: '/czpzglz',
		name: 'GlobalTreasurer',
		component: Layout,
		meta: {
			title: '财资配置',
			icon: 'bank-line',
		},
		children: [
			{
				path: 'BusinessSystemRegister',
				name: 'BusinessSystemRegister',
				component: () => import('@/views/globalTreasurer-new/treasuryCommon/basicConfig/BusinessSystemRegister'),
				meta: {
					title: '业务系统注册',
					icon: 'settings-line',
				},
			},
			{
				path: 'DataMappingConfig',
				name: 'DataMappingConfig',
				component: () => import('@/views/globalTreasurer-new/treasuryCommon/basicConfig/DataMappingConfig'),
				meta: {
					title: '数据映射配置管理',
					icon: 'database-line',
				},
			},
			{
				path: 'SealTypeManage',
				name: 'SealTypeManage',
				component: () => import('@/views/globalTreasurer-new/treasuryCommon/basicConfig/SealTypeManage'),
				meta: {
					title: '印鉴类型管理',
					icon: 'shield-line',
				},
			},
			{
				path: 'SealArchiveManage',
				name: 'SealArchiveManage',
				component: () => import('@/views/globalTreasurer-new/treasuryCommon/basicConfig/SealArchiveManage'),
				meta: {
					title: '印鉴档案管理',
					icon: 'archive-line',
				},
			},
			{
				path: 'ETicketAccountConfig',
				name: 'ETicketAccountConfig',
				component: () => import('@/views/globalTreasurer-new/treasuryCommon/basicConfig/ETicketAccountConfig'),
				meta: {
					title: '电票账户设置',
					icon: 'ticket-line',
				},
			},
			{
				path: 'ThirdPartyAccountManage',
				name: 'ThirdPartyAccountManage',
				component: () => import('@/views/globalTreasurer-new/treasuryCommon/basicConfig/ThirdPartyAccountManage'),
				meta: {
					title: '第三方账户管理',
					icon: 'bank-card-line',
				},
			},
			{
				path: 'SecurityParamConfig',
				name: 'SecurityParamConfig',
				component: () => import('@/views/globalTreasurer-new/treasuryCommon/basicConfig/SecurityParamConfig'),
				meta: {
					title: '安全配置',
					icon: 'lock-line',
				},
			},
			{
				path: 'UkeyVendorManage',
				name: 'UkeyVendorManage',
				component: () => import('@/views/globalTreasurer-new/treasuryCommon/basicConfig/UkeyVendorManage'),
				meta: {
					title: 'Ukey厂商',
					icon: 'key-line',
				},
			},
			{
				path: 'SealCombinationConfig',
				name: 'SealCombinationConfig',
				component: () => import('@/views/globalTreasurer-new/treasuryCommon/basicConfig/SealCombinationConfig'),
				meta: {
					title: '印鉴组合配置',
					icon: 'group-line',
				},
			},
			{
				path: 'SealUsageRecord',
				name: 'SealUsageRecord',
				component: () => import('@/views/globalTreasurer-new/treasuryCommon/basicConfig/SealUsageRecord'),
				meta: {
					title: '印鉴使用记录',
					icon: 'history-line',
				},
			},
			// 数据规则管理组
			{
				path: 'ExchangeRateManage',
				name: 'ExchangeRateManage',
				component: () => import('@/views/globalTreasurer-new/treasuryCommon/dataRulesManage/ExchangeRateManage'),
				meta: {
					title: '汇率数据管理',
					icon: 'money-dollar-circle-line',
				},
			},
			{
				path: 'InterestRateManage',
				name: 'InterestRateManage',
				component: () => import('@/views/globalTreasurer-new/treasuryCommon/dataRulesManage/InterestRateManage'),
				meta: {
					title: '利率数据管理',
					icon: 'line-chart-line',
				},
			},
			{
				path: 'BusinessRuleManage',
				name: 'BusinessRuleManage',
				component: () => import('@/views/globalTreasurer-new/treasuryCommon/dataRulesManage/BusinessRuleManage'),
				meta: {
					title: '业务规则管理',
					icon: 'settings-3-line',
				},
			},
			{
				path: 'DataSourceConfigManage',
				name: 'DataSourceConfigManage',
				component: () => import('@/views/globalTreasurer-new/treasuryCommon/dataRulesManage/DataSourceConfigManage'),
				meta: {
					title: '数据源配置管理',
					icon: 'database-2-line',
				},
			},
			{
				path: 'MarketDataManage',
				name: 'MarketDataManage',
				component: () => import('@/views/globalTreasurer-new/treasuryCommon/dataRulesManage/MarketDataManage'),
				meta: {
					title: '市场数据管理',
					icon: 'stock-line',
				},
			},
			// 伙伴直连管理组
			{
				path: 'PartnerTypeManage',
				name: 'PartnerTypeManage',
				component: () => import('@/views/globalTreasurer-new/treasuryCommon/partnerDirectConnection/PartnerTypeManage'),
				meta: {
					title: '伙伴类型管理',
					icon: 'team-line',
				},
			},
			{
				path: 'PartnerArchiveManage',
				name: 'PartnerArchiveManage',
				component: () => import('@/views/globalTreasurer-new/treasuryCommon/partnerDirectConnection/PartnerArchiveManage'),
				meta: {
					title: '伙伴档案管理',
					icon: 'contacts-line',
				},
			},
			{
				path: 'PartnerRiskManage',
				name: 'PartnerRiskManage',
				component: () => import('@/views/globalTreasurer-new/treasuryCommon/partnerDirectConnection/PartnerRiskManage'),
				meta: {
					title: '伙伴风险管理',
					icon: 'shield-check-line',
				},
			},
			{
				path: 'BankDirectConnectionManage',
				name: 'BankDirectConnectionManage',
				component: () => import('@/views/globalTreasurer-new/treasuryCommon/partnerDirectConnection/BankDirectConnectionManage'),
				meta: {
					title: '银企直连管理',
					icon: 'bank-line',
				},
			},
			{
				path: 'BankInterfaceConfigManage',
				name: 'BankInterfaceConfigManage',
				component: () => import('@/views/globalTreasurer-new/treasuryCommon/partnerDirectConnection/BankInterfaceConfigManage'),
				meta: {
					title: '银企接口配置',
					icon: 'settings-4-line',
				},
			},
			{
				path: 'CloudConnectionContractManage',
				name: 'CloudConnectionContractManage',
				component: () => import('@/views/globalTreasurer-new/treasuryCommon/partnerDirectConnection/CloudConnectionContractManage'),
				meta: {
					title: '云直连签约管理',
					icon: 'cloud-line',
				},
			},
			{
				path: 'BankConnectLogManage',
				name: 'BankConnectLogManage',
				component: () => import('@/views/globalTreasurer-new/treasuryCommon/partnerDirectConnection/BankConnectLogManage'),
				meta: {
					title: '银企直连日志管理',
					icon: 'file-list-line',
				},
			},
			// 金融产品定义组
			{
				path: 'FinancialCategoryManage',
				name: 'FinancialCategoryManage',
				component: () => import('@/views/globalTreasurer-new/treasuryCommon/financialProductDefinition/FinancialCategoryManage'),
				meta: {
					title: '金融业务分类管理',
					icon: 's-finance',
				},
			},
			{
				path: 'FinancialProductManage',
				name: 'FinancialProductManage',
				component: () => import('@/views/globalTreasurer-new/treasuryCommon/financialProductDefinition/FinancialProductManage'),
				meta: {
					title: '金融产品定义管理',
					icon: 'money-cny-circle-line',
				},
			},
			{
				path: 'CashflowTypeManage',
				name: 'CashflowTypeManage',
				component: () => import('@/views/globalTreasurer-new/treasuryCommon/financialProductDefinition/CashflowTypeManage'),
				meta: {
					title: '现金流类型管理',
					icon: 'exchange-line',
				},
			},
			// 新增的金融产品定义组页面
			{
				path: 'ProductPrincipalRuleManage',
				name: 'ProductPrincipalRuleManage',
				component: () => import('@/views/globalTreasurer-new/treasuryCommon/financialProductDefinition/ProductPrincipalRuleManage'),
				meta: {
					title: '产品本金规则配置管理',
					icon: 'money-cny-box-line',
				},
			},
			{
				path: 'ProductTermRuleManage',
				name: 'ProductTermRuleManage',
				component: () => import('@/views/globalTreasurer-new/treasuryCommon/financialProductDefinition/ProductTermRuleManage'),
				meta: {
					title: '产品期限规则配置管理',
					icon: 'calendar-line',
				},
			},
			{
				path: 'ProductInterestRuleManage',
				name: 'ProductInterestRuleManage',
				component: () => import('@/views/globalTreasurer-new/treasuryCommon/financialProductDefinition/ProductInterestRuleManage'),
				meta: {
					title: '产品利息规则配置管理',
					icon: 'percent-line',
				},
			},
			{
				path: 'TransactionTypeManage',
				name: 'TransactionTypeManage',
				component: () => import('@/views/globalTreasurer-new/treasuryCommon/financialProductDefinition/TransactionTypeManage'),
				meta: {
					title: '交易类型管理',
					icon: 'exchange-cny-line',
				},
			},
			{
				path: 'ProductTransactionEventManage',
				name: 'ProductTransactionEventManage',
				component: () => import('@/views/globalTreasurer-new/treasuryCommon/financialProductDefinition/ProductTransactionEventManage'),
				meta: {
					title: '产品交易事件配置管理',
					icon: 'settings-5-line',
				},
			},
			{
				path: 'ProductRiskControlManage',
				name: 'ProductRiskControlManage',
				component: () => import('@/views/globalTreasurer-new/treasuryCommon/financialProductDefinition/ProductRiskControlManage'),
				meta: {
					title: '产品风险控制配置管理',
					icon: 'shield-check-line',
				},
			},
			{
				path: 'ProductAccountingAttrManage',
				name: 'ProductAccountingAttrManage',
				component: () => import('@/views/globalTreasurer-new/treasuryCommon/financialProductDefinition/ProductAccountingAttrManage'),
				meta: {
					title: '产品核算属性配置管理',
					icon: 'calculator-line',
				},
			},
		],
	},
	// 全球司库账户管理路由配置
	{
		path: '/globalTreasurerAccount',
		name: 'GlobalTreasurerAccount',
		component: Layout,
		meta: {
			title: '账户管理',
			icon: 'bank-card-line',
		},
		children: [
			{
				path: 'zhxx',
				name: 'AccountInfo',
				component: () => import('@/views/globalTreasurer-new/accountManagement/zhxx'),
				meta: {
					title: '账户信息管理',
					icon: 'bank-card-line',
				},
			},
			{
				path: 'AccountOpeningManage',
				name: 'AccountOpeningManage',
				component: () => import('@/views/globalTreasurer-new/accountManagement/AccountOpeningManage'),
				meta: {
					title: '开户申请管理',
					icon: 'add-circle-line',
				},
			},
			{
				path: 'AccountClosingManage',
				name: 'AccountClosingManage',
				component: () => import('@/views/globalTreasurer-new/accountManagement/AccountClosingManage'),
				meta: {
					title: '销户申请管理',
					icon: 'close-circle-line',
				},
			},
			{
				path: 'AccountChangeManage',
				name: 'AccountChangeManage',
				component: () => import('@/views/globalTreasurer-new/accountManagement/AccountChangeManage'),
				meta: {
					title: '账户变更申请管理',
					icon: 'edit-circle-line',
				},
			},
			{
				path: 'AccountFreezeManage',
				name: 'AccountFreezeManage',
				component: () => import('@/views/globalTreasurer-new/accountManagement/AccountFreezeManage'),
				meta: {
					title: '账户冻结记录管理',
					icon: 'lock-line',
				},
			},
			{
				path: 'DirectConnectAuthManage',
				name: 'DirectConnectAuthManage',
				component: () => import('@/views/globalTreasurer-new/accountManagement/DirectConnectAuthManage'),
				meta: {
					title: '银企直联授权管理',
					icon: 'key-line',
				},
			},
			{
				path: 'AccountCheckManage',
				name: 'AccountCheckManage',
				component: () => import('@/views/globalTreasurer-new/accountManagement/AccountCheckManage'),
				meta: {
					title: '账户检查管理',
					icon: 'search-line',
				},
			},
			{
				path: 'AccountLimitManage',
				name: 'AccountLimitManage',
				component: () => import('@/views/globalTreasurer-new/accountManagement/AccountLimitManage'),
				meta: {
					title: '账户限额管理',
					icon: 'alarm-warning-line',
				},
			},
			{
				path: 'UKeyManage',
				name: 'UKeyManage',
				component: () => import('@/views/globalTreasurer-new/accountManagement/UKeyManage'),
				meta: {
					title: 'U盾信息管理',
					icon: 'shield-keyhole-line',
				},
			},
		],
	},
	// 全球司库现金管理路由配置
	{
		path: '/globalTreasurerCash',
		name: 'GlobalTreasurerCash',
		component: Layout,
		meta: {
			title: '现金管理',
			icon: 'money-cny-circle-line',
		},
		children: [
			{
				path: 'skgl',
				name: 'CashReceiptManage',
				component: () => import('@/views/globalTreasurer-new/cashManagement/skgl'),
				meta: {
					title: '收款管理',
					icon: 'arrow-down-circle-line',
				},
			},
			{
				path: 'fkgl',
				name: 'CashPaymentManage',
				component: () => import('@/views/globalTreasurer-new/cashManagement/fkgl'),
				meta: {
					title: '付款管理',
					icon: 'arrow-up-circle-line',
				},
			},
			{
				path: 'zjdb',
				name: 'FundTransferManage',
				component: () => import('@/views/globalTreasurer-new/cashManagement/zjdb'),
				meta: {
					title: '资金调拨管理',
					icon: 'exchange-line',
				},
			},
			{
				path: 'xjlyc',
				name: 'CashFlowForecast',
				component: () => import('@/views/globalTreasurer-new/cashManagement/xjlyc'),
				meta: {
					title: '现金流预测',
					icon: 'line-chart-line',
				},
			},
			{
				path: 'yhdz',
				name: 'BankReconciliation',
				component: () => import('@/views/globalTreasurer-new/cashManagement/yhdz'),
				meta: {
					title: '银行对账管理',
					icon: 'file-check-line',
				},
			},
		],
	},
	// 全球司库票据管理路由配置
	{
		path: '/globalTreasurerBill',
		name: 'GlobalTreasurerBill',
		component: Layout,
		meta: {
			title: '票据管理',
			icon: 'file-list-3-line',
		},
		children: [
			{
				path: 'pzdjgl',
				name: 'BillRegistrationManage',
				component: () => import('@/views/globalTreasurer-new/billManagement/pzdjgl'),
				meta: {
					title: '票据登记管理',
					icon: 'file-add-line',
				},
			},
			{
				path: 'pzbsgl',
				name: 'BillEndorsementManage',
				component: () => import('@/views/globalTreasurer-new/billManagement/pzbsgl'),
				meta: {
					title: '票据背书管理',
					icon: 'file-transfer-line',
				},
			},
			{
				path: 'pztxgl',
				name: 'BillDiscountManage',
				component: () => import('@/views/globalTreasurer-new/billManagement/pztxgl'),
				meta: {
					title: '票据贴现管理',
					icon: 'money-dollar-box-line',
				},
			},
			{
				path: 'pzdqgl',
				name: 'BillMaturityManage',
				component: () => import('@/views/globalTreasurer-new/billManagement/pzdqgl'),
				meta: {
					title: '票据到期管理',
					icon: 'calendar-check-line',
				},
			},
			{
				path: 'pzcxtj',
				name: 'BillQueryStatistics',
				component: () => import('@/views/globalTreasurer-new/billManagement/pzcxtj'),
				meta: {
					title: '票据查询统计',
					icon: 'bar-chart-line',
				},
			},
			{
				path: 'pzfxgl',
				name: 'BillRiskManage',
				component: () => import('@/views/globalTreasurer-new/billManagement/pzfxgl'),
				meta: {
					title: '票据风险管理',
					icon: 'shield-check-line',
				},
			},
			{
				path: 'dzpzgl',
				name: 'ElectronicBillManage',
				component: () => import('@/views/globalTreasurer-new/billManagement/dzpzgl'),
				meta: {
					title: '电子票据管理',
					icon: 'file-code-line',
				},
			},
			{
				path: 'pzc',
				name: 'BillPoolManage',
				component: () => import('@/views/globalTreasurer-new/billManagement/pzc'),
				meta: {
					title: '票据池管理',
					icon: 'database-2-line',
				},
			},
		],
	},
	// 全球司库资金归集路由配置
	{
		path: '/globalTreasurerConcentration',
		name: 'GlobalTreasurerConcentration',
		component: Layout,
		meta: {
			title: '资金归集',
			icon: 'funds-line',
		},
		children: [
			{
				path: 'gjclpz',
				name: 'ConcentrationStrategyConfig',
				component: () => import('@/views/globalTreasurer-new/fundConcentration/gjclpz'),
				meta: {
					title: '归集策略配置',
					icon: 'settings-3-line',
				},
			},
			{
				path: 'gjjhgl',
				name: 'ConcentrationPlanManage',
				component: () => import('@/views/globalTreasurer-new/fundConcentration/gjjhgl'),
				meta: {
					title: '归集计划管理',
					icon: 'calendar-todo-line',
				},
			},
			{
				path: 'gjzxjk',
				name: 'ConcentrationExecutionMonitor',
				component: () => import('@/views/globalTreasurer-new/fundConcentration/gjzxjk'),
				meta: {
					title: '归集执行监控',
					icon: 'dashboard-line',
				},
			},
			{
				path: 'gjjgfx',
				name: 'ConcentrationResultAnalysis',
				component: () => import('@/views/globalTreasurer-new/fundConcentration/gjjgfx'),
				meta: {
					title: '归集结果分析',
					icon: 'pie-chart-line',
				},
			},
			{
				path: 'gjyccl',
				name: 'ConcentrationExceptionHandle',
				component: () => import('@/views/globalTreasurer-new/fundConcentration/gjyccl'),
				meta: {
					title: '归集异常处理',
					icon: 'error-warning-line',
				},
			},
			{
				path: 'gjbbtj',
				name: 'ConcentrationReportStatistics',
				component: () => import('@/views/globalTreasurer-new/fundConcentration/gjbbtj'),
				meta: {
					title: '归集报表统计',
					icon: 'file-chart-line',
				},
			},
		],
	},
	// 全球司库融资管理路由配置
	{
		path: '/globalTreasurerFinancing',
		name: 'GlobalTreasurerFinancing',
		component: Layout,
		meta: {
			title: '融资管理',
			icon: 'money-dollar-circle-line',
		},
		children: [
			{
				path: 'rzjhgl',
				name: 'FinancingPlanManage',
				component: () => import('@/views/globalTreasurer-new/financingManagement/rzjhgl'),
				meta: {
					title: '融资计划管理',
					icon: 'calendar-todo-line',
				},
			},
			{
				path: 'yhdkgl',
				name: 'BankLoanManage',
				component: () => import('@/views/globalTreasurer-new/financingManagement/yhdkgl'),
				meta: {
					title: '银行贷款管理',
					icon: 'bank-line',
				},
			},
			{
				path: 'zqfxgl',
				name: 'BondIssuanceManage',
				component: () => import('@/views/globalTreasurer-new/financingManagement/zqfxgl'),
				meta: {
					title: '债券发行管理',
					icon: 'file-list-2-line',
				},
			},
			{
				path: 'rzzlgl',
				name: 'FinancialLeasingManage',
				component: () => import('@/views/globalTreasurer-new/financingManagement/rzzlgl'),
				meta: {
					title: '融资租赁管理',
					icon: 'truck-line',
				},
			},
			{
				path: 'rzjkgl',
				name: 'FinancingMonitoringManage',
				component: () => import('@/views/globalTreasurer-new/financingManagement/rzjkgl'),
				meta: {
					title: '融资监控管理',
					icon: 'dashboard-3-line',
				},
			},
			{
				path: 'rzhkgl',
				name: 'FinancingRepaymentManage',
				component: () => import('@/views/globalTreasurer-new/financingManagement/rzhkgl'),
				meta: {
					title: '融资还款管理',
					icon: 'refund-line',
				},
			},
			{
				path: 'sxywgl',
				name: 'CreditBusinessManage',
				component: () => import('@/views/globalTreasurer/financing/credit/index'),
				meta: {
					title: '授信业务管理',
					icon: 'document-line',
				},
			},
		],
	},
	// 全球司库投资管理路由配置
	{
		path: '/globalTreasurerInvestment',
		name: 'GlobalTreasurerInvestment',
		component: Layout,
		meta: {
			title: '投资管理',
			icon: 'line-chart-line',
		},
		children: [
			{
				path: 'tzjh',
				name: 'InvestmentPlanManage',
				component: () => import('@/views/globalTreasurer-new/investmentManagement/tzjh'),
				meta: {
					title: '投资计划管理',
					icon: 'calendar-todo-line',
				},
			},
			{
				path: 'tzcp',
				name: 'InvestmentProductManage',
				component: () => import('@/views/globalTreasurer-new/investmentManagement/tzcp'),
				meta: {
					title: '投资产品管理',
					icon: 'product-hunt-line',
				},
			},
			{
				path: 'yhlc',
				name: 'BankWealthManage',
				component: () => import('@/views/globalTreasurer-new/investmentManagement/yhlc'),
				meta: {
					title: '银行理财管理',
					icon: 'bank-line',
				},
			},
			{
				path: 'zqtz',
				name: 'BondInvestmentManage',
				component: () => import('@/views/globalTreasurer-new/investmentManagement/zqtz'),
				meta: {
					title: '债券投资管理',
					icon: 'file-list-2-line',
				},
			},
			{
				path: 'tzjk',
				name: 'InvestmentMonitoringManage',
				component: () => import('@/views/globalTreasurer-new/investmentManagement/tzjk'),
				meta: {
					title: '投资监控管理',
					icon: 'dashboard-3-line',
				},
			},
		],
	},
	// 全球司库衍生品管理路由配置
	{
		path: '/globalTreasurerDerivatives',
		name: 'GlobalTreasurerDerivatives',
		component: Layout,
		meta: {
			title: '衍生品管理',
			icon: 'stock-line',
		},
		children: [
			{
				path: 'yspx',
				name: 'DerivativesOverview',
				component: () => import('@/views/globalTreasurer-new/derivatives/yspx'),
				meta: {
					title: '衍生品概览',
					icon: 'dashboard-line',
				},
			},
			{
				path: 'yqjy',
				name: 'ForwardTrading',
				component: () => import('@/views/globalTreasurer-new/derivatives/yqjy'),
				meta: {
					title: '远期交易',
					icon: 'arrow-right-line',
				},
			},
			{
				path: 'qqjy',
				name: 'OptionTrading',
				component: () => import('@/views/globalTreasurer-new/derivatives/qqjy'),
				meta: {
					title: '期权交易',
					icon: 'settings-3-line',
				},
			},
			{
				path: 'yspxjk',
				name: 'DerivativesMonitoring',
				component: () => import('@/views/globalTreasurer-new/derivatives/yspxjk'),
				meta: {
					title: '衍生品监控',
					icon: 'radar-line',
				},
			},
		],
	},
	// 全球司库风险管理路由配置
	{
		path: '/globalTreasurerRisk',
		name: 'GlobalTreasurerRisk',
		component: Layout,
		meta: {
			title: '风险管理',
			icon: 'shield-check-line',
		},
		children: [
			{
				path: 'fxgl',
				name: 'RiskManagement',
				component: () => import('@/views/globalTreasurer-new/riskManagement/fxgl'),
				meta: {
					title: '风险管理',
					icon: 'shield-line',
				},
			},
			{
				path: 'fxsb',
				name: 'RiskIdentification',
				component: () => import('@/views/globalTreasurer-new/riskManagement/fxsb'),
				meta: {
					title: '风险识别',
					icon: 'search-line',
				},
			},
			{
				path: 'fxpg',
				name: 'RiskAssessment',
				component: () => import('@/views/globalTreasurer-new/riskManagement/fxpg'),
				meta: {
					title: '风险评估',
					icon: 'data-analysis',
				},
			},
		],
	},
	// 全球司库决策支持路由配置
	{
		path: '/globalTreasurerDecision',
		name: 'GlobalTreasurerDecision',
		component: Layout,
		meta: {
			title: '决策支持',
			icon: 'bar-chart-line',
		},
		children: [
			{
				path: 'jczc',
				name: 'DecisionSupport',
				component: () => import('@/views/globalTreasurer-new/decisionSupport/index'),
				meta: {
					title: '决策支持',
					icon: 'pie-chart-line',
				},
			},
		],
	},
	// 全球司库监管报告路由配置
	{
		path: '/globalTreasurerRegulatory',
		name: 'GlobalTreasurerRegulatory',
		component: Layout,
		meta: {
			title: '监管报告',
			icon: 'file-list-line',
		},
		children: [
			{
				path: 'jgbs',
				name: 'RegulatoryReporting',
				component: () => import('@/views/globalTreasurer-new/regulatoryReporting/jgbs'),
				meta: {
					title: '监管报送',
					icon: 'send-plane-line',
				},
			},
			{
				path: 'bbsj',
				name: 'ReportData',
				component: () => import('@/views/globalTreasurer-new/regulatoryReporting/bbsj'),
				meta: {
					title: '报表数据',
					icon: 'database-line',
				},
			},
		],
	},
	{
		path: '/contract',
		name: 'Contract',
		component: Layout,
		meta: {
			title: '合同管理',
			icon: 'file-text-line',
			roles: ['admin', 'editor'],
		},
		children: [
			{
				path: 'assessment',
				name: 'Assessment',
				component: () => import('@/views/contract/assessment/index'),
				meta: {
					title: '项目考核',
					icon: 'bar-chart-line',
				},
			},
			{
				path: 'assessment-indicator',
				name: 'AssessmentIndicator',
				component: () => import('@/views/contract/assessment/indicator'),
				meta: {
					title: '考核指标配置',
					icon: 'settings-line',
				},
			},
		],
	},
	{
		path: '/stateAssets',
		name: 'StateAssets',
		component: Layout,
		meta: {
			title: '国资监管',
			icon: 'building-line',
			roles: ['admin', 'editor'],
		},
		children: [
			{
				path: 'dashboard',
				name: 'StateAssetsDashboard',
				component: () => import('@/views/stateAssets/dashboard/index'),
				meta: {
					title: '监管驾驶舱',
					icon: 'dashboard-line',
				},
			},
			{
				path: 'enterprise',
				name: 'Enterprise',
				component: () => import('@/views/stateAssets/enterprise/index'),
				meta: {
					title: '企业信息管理',
					icon: 'building-2-line',
				},
			},
			{
				path: 'dataCollection',
				name: 'DataCollection',
				component: () => import('@/views/stateAssets/dataCollection/index'),
				meta: {
					title: '数据采集管理',
					icon: 'database-2-line',
				},
			},
			// 股权穿透监管
			{
				path: 'equityPenetration',
				name: 'EquityPenetration',
				meta: {
					title: '股权穿透监管',
					icon: 'share-line',
				},
				children: [
					{
						path: 'shareholderAnalysis',
						name: 'ShareholderAnalysis',
						component: () => import('@/views/stateAssets/equityPenetration/shareholderAnalysis/index'),
						meta: {
							title: '股东穿透分析',
							icon: 'user-3-line',
						},
					},
					{
						path: 'equityStructure',
						name: 'EquityStructure',
						component: () => import('@/views/stateAssets/equityPenetration/equityStructure/index'),
						meta: {
							title: '股权结构管理',
							icon: 'organization-chart',
						},
					},
					{
						path: 'controlChain',
						name: 'ControlChain',
						component: () => import('@/views/stateAssets/equityPenetration/controlChain/index'),
						meta: {
							title: '控制链分析',
							icon: 'links-line',
						},
					},
					{
						path: 'beneficialOwner',
						name: 'BeneficialOwner',
						component: () => import('@/views/stateAssets/equityPenetration/beneficialOwner/index'),
						meta: {
							title: '实际控制人识别',
							icon: 'user-star-line',
						},
					},
					{
						path: 'equityChanges',
						name: 'EquityChanges',
						component: () => import('@/views/stateAssets/equityPenetration/equityChanges/index'),
						meta: {
							title: '股权变动监控',
							icon: 'exchange-line',
						},
					},
					{
						path: 'investmentDecision',
						name: 'InvestmentDecision',
						component: () => import('@/views/stateAssets/equityPenetration/investmentDecision/index'),
						meta: {
							title: '投资决策监管',
							icon: 'funds-line',
						},
					},
					{
						path: 'controlChainAnalysis',
						name: 'ControlChainAnalysis',
						component: () => import('@/views/stateAssets/equityPenetration/controlChainAnalysis/index'),
						meta: {
							title: '控制链分析',
							icon: 'connection-line',
						},
					},
					{
						path: 'Rzjltz',
						name: 'EquityFinancingLedger',
						component: () => import('@/views/stateAssets/equityPenetration/Rzjltz/index'),
						meta: {
							title: '融资记录台账',
							icon: 'coin-line',
						},
					},
					{
						path: 'Dbjltz',
						name: 'EquityGuaranteeLedger',
						component: () => import('@/views/stateAssets/equityPenetration/Dbjltz/index'),
						meta: {
							title: '担保记录台账',
							icon: 'shield-check-line',
						},
					},
					{
						path: 'Jrfxjsc',
						name: 'EquityRiskDashboard',
						component: () => import('@/views/stateAssets/equityPenetration/Jrfxjsc/index'),
						meta: {
							title: '风险驾驶舱',
							icon: 'dashboard-line',
						},
					}
				]
			},
			{
				path: '/stateAssets/assetPenetration',
				component: Layout,
				redirect: '/stateAssets/assetPenetration/assetAllocation',
				name: 'AssetPenetration',
				meta: {
					title: '资产穿透管理',
					icon: 'money-cny-circle-line',
				},
				children: [
					{
						path: 'assetAllocation',
						name: 'AssetAllocation',
						component: () => import('@/views/stateAssets/assetPenetration/assetAllocation/index'),
						meta: {
							title: '资产配置监管',
							icon: 'pie-chart-line',
						},
					},
					{
						path: 'assetQuality',
						name: 'AssetQuality',
						component: () => import('@/views/stateAssets/assetPenetration/assetQuality/index'),
						meta: {
							title: '资产质量评估',
							icon: 'medal-line',
						},
					},
					{
						path: 'assetFlow',
						name: 'AssetFlow',
						component: () => import('@/views/stateAssets/assetPenetration/assetFlow/index'),
						meta: {
							title: '资产流向追踪',
							icon: 'flow-chart',
						},
					},
					{
						path: 'assetMapping',
						name: 'AssetMapping',
						component: () => import('@/views/stateAssets/assetPenetration/assetMapping/index'),
						meta: {
							title: '资产运营分析',
							icon: 'bar-chart-line',
						},
					}
				]
			},
			{
				path: '/stateAssets/financialPenetration',
				component: Layout,
				redirect: '/stateAssets/financialPenetration/financialAnalysis',
				name: 'FinancialPenetration',
				meta: {
					title: '财务穿透分析',
					icon: 'line-chart-line',
				},
				children: [
					{
						path: 'financialAnalysis',
						name: 'FinancialAnalysis',
						component: () => import('@/views/stateAssets/financialPenetration/financialAnalysis/index'),
						meta: {
							title: '财务数据穿透分析',
							icon: 'funds-line',
						},
					},
					{
						path: 'financialRisk',
						name: 'FinancialRisk',
						component: () => import('@/views/stateAssets/financialPenetration/financialRisk/index'),
						meta: {
							title: '财务风险识别',
							icon: 'error-warning-line',
						},
					},
					{
						path: 'financialPerformance',
						name: 'FinancialPerformance',
						component: () => import('@/views/stateAssets/financialPenetration/financialPerformance/index'),
						meta: {
							title: '财务绩效评价',
							icon: 'trophy-line',
						},
					},
					{
						path: 'financialCompliance',
						name: 'FinancialCompliance',
						component: () => import('@/views/stateAssets/financialPenetration/financialCompliance/index'),
						meta: {
							title: '财务合规监管',
							icon: 'shield-check-line',
						},
					},
				],
			},
			// 资产穿透管理
			{
				path: 'assetPenetration',
				name: 'AssetPenetration',
				meta: {
					title: '资产穿透管理',
					icon: 'safe-line',
				},
				children: [
					{
						path: 'assetMapping',
						name: 'AssetMapping',
						component: () => import('@/views/stateAssets/assetPenetration/assetMapping/index'),
						meta: {
							title: '资产映射管理',
							icon: 'map-line',
						},
					},
					{
						path: 'assetConcentration',
						name: 'AssetConcentration',
						component: () => import('@/views/stateAssets/assetPenetration/assetConcentration/index'),
						meta: {
							title: '资产集中度分析',
							icon: 'pie-chart-line',
						},
					},
					{
						path: 'crossHolding',
						name: 'CrossHolding',
						component: () => import('@/views/stateAssets/assetPenetration/crossHolding/index'),
						meta: {
							title: '交叉持股分析',
							icon: 'shuffle-line',
						},
					},
					{
						path: 'assetFlow',
						name: 'AssetFlow',
						component: () => import('@/views/stateAssets/assetPenetration/assetFlow/index'),
						meta: {
							title: '资产流向追踪',
							icon: 'flow-chart',
						},
					},
				],
			},
			// 财务穿透分析
			{
				path: 'financialPenetration',
				name: 'FinancialPenetration',
				meta: {
					title: '财务穿透分析',
					icon: 'line-chart-line',
				},
				children: [
					{
						path: 'consolidatedAnalysis',
						name: 'ConsolidatedAnalysis',
						component: () => import('@/views/stateAssets/financialPenetration/consolidatedAnalysis/index'),
						meta: {
							title: '合并财务分析',
							icon: 'bar-chart-grouped-line',
						},
					},
					{
						path: 'relatedTransaction',
						name: 'RelatedTransaction',
						component: () => import('@/views/stateAssets/financialPenetration/relatedTransaction/index'),
						meta: {
							title: '关联交易分析',
							icon: 'exchange-line',
						},
					},
					{
						path: 'fundFlow',
						name: 'FundFlow',
						component: () => import('@/views/stateAssets/financialPenetration/fundFlow/index'),
						meta: {
							title: '资金流向分析',
							icon: 'money-dollar-circle-line',
						},
					},
					{
						path: 'performanceConsolidation',
						name: 'PerformanceConsolidation',
						component: () => import('@/views/stateAssets/financialPenetration/performanceConsolidation/index'),
						meta: {
							title: '业绩合并分析',
							icon: 'trophy-line',
						},
					},
				],
			},
			// 风险穿透管控
			{
				path: 'riskPenetration',
				name: 'RiskPenetration',
				meta: {
					title: '风险穿透管控',
					icon: 'shield-line',
				},
				children: [
					{
						path: 'riskAssessment',
						name: 'RiskAssessment',
						component: () => import('@/views/stateAssets/riskPenetration/riskAssessment/index'),
						meta: {
							title: '风险评估管理',
							icon: 'file-shield-line',
						},
					},
					{
						path: 'riskMonitoring',
						name: 'RiskMonitoring',
						component: () => import('@/views/stateAssets/riskPenetration/riskMonitoring/index'),
						meta: {
							title: '风险监控管理',
							icon: 'eye-line',
						},
					},
					{
						path: 'riskControlMeasure',
						name: 'RiskControlMeasure',
						component: () => import('@/views/stateAssets/riskPenetration/riskControlMeasure/index'),
						meta: {
							title: '风险控制措施',
							icon: 'settings-4-line',
						},
					},
					{
						path: 'riskIncident',
						name: 'RiskIncident',
						component: () => import('@/views/stateAssets/riskPenetration/riskIncident/index'),
						meta: {
							title: '风险事件管理',
							icon: 'alarm-warning-line',
						},
					},
				],
			},
			// 双端协同
			{
				path: 'collaboration',
				name: 'StateAssetsCollaboration',
				meta: {
					title: '双端协同',
					icon: 'team-line',
				},
				children: [
					{
						path: 'dataCollaboration',
						name: 'DataCollaboration',
						component: () => import('@/views/stateAssets/collaboration/dataCollaboration/index'),
						meta: {
							title: '数据协同',
							icon: 'database-2-line',
						},
					},
					{
						path: 'supervisionInstruction',
						name: 'SupervisionInstruction',
						component: () => import('@/views/stateAssets/collaboration/supervisionInstruction/index'),
						meta: {
							title: '监管指令协同',
							icon: 'file-list-3-line',
						},
					},
					{
						path: 'riskCollaboration',
						name: 'RiskCollaboration',
						component: () => import('@/views/stateAssets/collaboration/riskCollaboration/index'),
						meta: {
							title: '风险协同预警',
							icon: 'alarm-warning-line',
						},
					},
				],
			},
		],
	},
	// 投资穿透监管（独立路由）
	{
		path: '/stateAssets/investPenetration',
		name: 'InvestPenetrationModule',
		component: Layout,
		meta: { title: '投资穿透监管', icon: 'bank-card-line', roles: ['admin', 'editor'] },
		children: [
			{ path: 'home', name: 'InvestPenetrationHome', component: () => import('@/views/stateAssets/investPenetration/home/index'), meta: { title: '投资穿透首页', icon: 'home-2-line' } },
			{ path: 'dashboard', name: 'InvestPenetrationDashboard', component: () => import('@/views/stateAssets/investPenetration/dashboard/index'), meta: { title: '监控驾驶舱', icon: 'dashboard-line' } },
			{ path: 'project', name: 'InvestPenetrationProject', component: () => import('@/views/stateAssets/investPenetration/project/index'), meta: { title: '投资项目台账', icon: 'file-list-2-line' } },
			{ path: 'drillDown', name: 'InvestPenetrationDrillDown', component: () => import('@/views/stateAssets/investPenetration/drillDown/index'), meta: { title: '穿透分析', icon: 'share-line' } },
			{ path: 'compliance', name: 'InvestPenetrationCompliance', component: () => import('@/views/stateAssets/investPenetration/compliance/index'), meta: { title: '合规追踪', icon: 'shield-check-line' } },
			{ path: 'postEval', name: 'InvestPenetrationPostEval', component: () => import('@/views/stateAssets/investPenetration/postEval/index'), meta: { title: '投后评价', icon: 'survey-line' } },
			{ path: 'nonMainBiz', name: 'InvestPenetrationNonMainBiz', component: () => import('@/views/stateAssets/investPenetration/nonMainBiz/index'), meta: { title: '非主业分析', icon: 'pie-chart-2-line' } },
			{ path: 'riskWarning', name: 'InvestPenetrationRiskWarning', component: () => import('@/views/stateAssets/investPenetration/riskWarning/index'), meta: { title: '风险预警管理', icon: 'alarm-warning-line' } },
			{ path: 'progressMonitor', name: 'InvestPenetrationProgressMonitor', component: () => import('@/views/stateAssets/investPenetration/progressMonitor/index'), meta: { title: '投资进度监控', icon: 'progress-8-line' } },
		],
	},
	// 境外穿透监管
	{
		path: '/stateAssets/overseasPenetration',
		name: 'OverseasPenetrationModule',
		component: Layout,
		meta: { title: '境外穿透监管', icon: 'earth-line', roles: ['admin', 'editor'] },
		children: [
			{ path: 'home', name: 'OverseasPenetrationHome', component: () => import('@/views/stateAssets/overseasPenetration/home/index'), meta: { title: '境外穿透首页', icon: 'home-2-line' } },
			{ path: 'dashboard', name: 'OverseasPenetrationDashboard', component: () => import('@/views/stateAssets/overseasPenetration/dashboard/index'), meta: { title: '监控驾驶舱', icon: 'dashboard-line' } },
			{ path: 'unit', name: 'OverseasPenetrationUnit', component: () => import('@/views/stateAssets/overseasPenetration/unit/index'), meta: { title: '境外单位台账', icon: 'building-2-line' } },
			{ path: 'investMgmt', name: 'OverseasInvestMgmt', component: () => import('@/views/stateAssets/overseasPenetration/investMgmt/index'), meta: { title: '境外投资管理', icon: 'funds-line' } },
			{ path: 'operationAnalysis', name: 'OverseasOperationAnalysis', component: () => import('@/views/stateAssets/overseasPenetration/operationAnalysis/index'), meta: { title: '境外经营分析', icon: 'bar-chart-line' } },
			{ path: 'countryRiskMap', name: 'OverseasCountryRiskMap', component: () => import('@/views/stateAssets/overseasPenetration/countryRiskMap/index'), meta: { title: '国别风险地图', icon: 'map-2-line' } },
			{ path: 'forexAnalysis', name: 'OverseasForexAnalysis', component: () => import('@/views/stateAssets/overseasPenetration/forexAnalysis/index'), meta: { title: '外汇风险分析', icon: 'exchange-dollar-line' } },
			{ path: 'complianceMgmt', name: 'OverseasComplianceMgmt', component: () => import('@/views/stateAssets/overseasPenetration/complianceMgmt/index'), meta: { title: '合规管理', icon: 'file-shield-2-line' } },
			{ path: 'personnelSafety', name: 'OverseasPersonnelSafety', component: () => import('@/views/stateAssets/overseasPenetration/personnelSafety/index'), meta: { title: '人员安全管理', icon: 'user-star-line' } },
			{ path: 'emergencyCommand', name: 'OverseasEmergencyCommand', component: () => import('@/views/stateAssets/overseasPenetration/emergencyCommand/index'), meta: { title: '应急指挥中心', icon: 'alarm-warning-line' } },
			{ path: 'riskWarning', name: 'OverseasRiskWarning', component: () => import('@/views/stateAssets/overseasPenetration/riskWarning/index'), meta: { title: '风险预警管理', icon: 'notification-2-line' } },
			{ path: 'leader', name: 'OverseasLeaderMgmt', component: () => import('@/views/stateAssets/overseasPenetration/leader/index'), meta: { title: '负责人管理', icon: 'admin-line' } },
		],
	},
	// 财务穿透监管
	{
		path: '/stateAssets/financialPenetration',
		name: 'FinancialPenetrationModule',
		component: Layout,
		meta: { title: '财务穿透监管', icon: 'bar-chart-2-line', roles: ['admin', 'editor'] },
		children: [
			{ path: 'home', name: 'FinancialPenetrationHome', component: () => import('@/views/stateAssets/financialPenetration/home/index'), meta: { title: '财务穿透首页', icon: 'home-2-line' } },
			{ path: 'dashboard', name: 'FinancialPenetrationDashboard', component: () => import('@/views/stateAssets/financialPenetration/dashboard/index'), meta: { title: '监控驾驶舱', icon: 'dashboard-line' } },
			{ path: 'statementQuery', name: 'FinancialStatementQuery', component: () => import('@/views/stateAssets/financialPenetration/statementQuery/index'), meta: { title: '财务报表穿透', icon: 'file-chart-2-line' } },
			{ path: 'benchmark', name: 'FinancialBenchmark', component: () => import('@/views/stateAssets/financialPenetration/benchmark/index'), meta: { title: '指标对标分析', icon: 'bar-chart-grouped-line' } },
			{ path: 'relatedParty', name: 'FinancialRelatedParty', component: () => import('@/views/stateAssets/financialPenetration/relatedParty/index'), meta: { title: '关联交易监控', icon: 'links-line' } },
			{ path: 'expenseMonitor', name: 'FinancialExpenseMonitor', component: () => import('@/views/stateAssets/financialPenetration/expenseMonitor/index'), meta: { title: '费用管控监控', icon: 'money-cny-circle-line' } },
			{ path: 'anomalyDetect', name: 'FinancialAnomalyDetect', component: () => import('@/views/stateAssets/financialPenetration/anomalyDetect/index'), meta: { title: '财务异常检测', icon: 'spy-line' } },
			{ path: 'riskWarning', name: 'FinancialRiskWarning', component: () => import('@/views/stateAssets/financialPenetration/riskWarning/index'), meta: { title: '风险预警管理', icon: 'alarm-warning-line' } },
			{ path: 'drillDown', name: 'FinancialDrillDown', component: () => import('@/views/stateAssets/financialPenetration/drillDown/index'), meta: { title: '财务穿透分析', icon: 'zoom-in-line' } },
		],
	},
	// 财务穿透监管
	{
		path: '/stateAssets/financialPenetration',
		name: 'FinancialPenetrationModule',
		component: Layout,
		meta: { title: '财务穿透监管', icon: 'bar-chart-2-line', roles: ['admin', 'editor'] },
		children: [
			{ path: 'home', name: 'FinancialPenetrationHome', component: () => import('@/views/stateAssets/financialPenetration/home/index'), meta: { title: '财务穿透首页', icon: 'home-2-line' } },
			{ path: 'dashboard', name: 'FinancialPenetrationDashboard', component: () => import('@/views/stateAssets/financialPenetration/dashboard/index'), meta: { title: '监控驾驶舱', icon: 'dashboard-line' } },
			{ path: 'statementQuery', name: 'FinancialStatementQuery', component: () => import('@/views/stateAssets/financialPenetration/statementQuery/index'), meta: { title: '财务报表穿透', icon: 'file-chart-2-line' } },
			{ path: 'benchmark', name: 'FinancialBenchmark', component: () => import('@/views/stateAssets/financialPenetration/benchmark/index'), meta: { title: '指标对标分析', icon: 'bar-chart-grouped-line' } },
			{ path: 'relatedParty', name: 'FinancialRelatedParty', component: () => import('@/views/stateAssets/financialPenetration/relatedParty/index'), meta: { title: '关联交易监控', icon: 'links-line' } },
			{ path: 'expenseMonitor', name: 'FinancialExpenseMonitor', component: () => import('@/views/stateAssets/financialPenetration/expenseMonitor/index'), meta: { title: '费用管控监控', icon: 'money-cny-circle-line' } },
			{ path: 'anomalyDetect', name: 'FinancialAnomalyDetect', component: () => import('@/views/stateAssets/financialPenetration/anomalyDetect/index'), meta: { title: '财务异常检测', icon: 'spy-line' } },
			{ path: 'riskWarning', name: 'FinancialRiskWarning', component: () => import('@/views/stateAssets/financialPenetration/riskWarning/index'), meta: { title: '风险预警管理', icon: 'alarm-warning-line' } },
			{ path: 'drillDown', name: 'FinancialDrillDown', component: () => import('@/views/stateAssets/financialPenetration/drillDown/index'), meta: { title: '财务穿透分析', icon: 'zoom-in-line' } },
		],
	},
	// 产权穿透监管
	{
		path: '/stateAssets/propertyPenetration',
		name: 'PropertyPenetrationModule',
		component: Layout,
		meta: { title: '产权穿透监管', icon: 'organization', roles: ['admin', 'editor'] },
		children: [
			{ path: 'home', name: 'PropertyPenetrationHome', component: () => import('@/views/stateAssets/propertyPenetration/home/index'), meta: { title: '产权穿透首页', icon: 'home-4-line' } },
			{ path: 'registryList', name: 'PropertyRegistryList', component: () => import('@/views/stateAssets/propertyPenetration/registryList/index'), meta: { title: '产权登记台账', icon: 'file-list-3-line' } },
			{ path: 'equityChart', name: 'PropertyEquityChart', component: () => import('@/views/stateAssets/propertyPenetration/equityChart/index'), meta: { title: '股权穿透图', icon: 'share-line' } },
			{ path: 'changeRecord', name: 'PropertyChangeRecord', component: () => import('@/views/stateAssets/propertyPenetration/changeRecord/index'), meta: { title: '产权变动登记', icon: 'exchange-line' } },
			{ path: 'tradeReview', name: 'PropertyTradeReview', component: () => import('@/views/stateAssets/propertyPenetration/tradeReview/index'), meta: { title: '产权交易审查', icon: 'shake-hands-line' } },
			{ path: 'shareholding', name: 'PropertyShareholding', component: () => import('@/views/stateAssets/propertyPenetration/shareholding/index'), meta: { title: '参股企业分析', icon: 'pie-chart-2-line' } },
			{ path: 'dashboard', name: 'PropertyDashboard', component: () => import('@/views/stateAssets/propertyPenetration/dashboard/index'), meta: { title: '产权监控驾驶舱', icon: 'dashboard-line' } },
		],
	},
	// 薪酬穿透监管
	{
		path: '/stateAssets/salaryPenetration',
		name: 'SalaryPenetrationModule',
		component: Layout,
		meta: { title: '薪酬穿透监管', icon: 'money-cny-circle-line', roles: ['admin', 'editor'] },
		children: [
			{ path: 'home', name: 'SalaryPenetrationHome', component: () => import('@/views/stateAssets/salaryPenetration/home/index'), meta: { title: '薪酬穿透首页', icon: 'home-2-line' } },
			{ path: 'dashboard', name: 'SalaryPenetrationDashboard', component: () => import('@/views/stateAssets/salaryPenetration/dashboard/index'), meta: { title: '监控驾驶舱', icon: 'dashboard-line' } },
			{ path: 'totalList', name: 'SalaryTotalList', component: () => import('@/views/stateAssets/salaryPenetration/totalList/index'), meta: { title: '工资总额管理', icon: 'bill-line' } },
			{ path: 'performanceLink', name: 'SalaryPerformanceLink', component: () => import('@/views/stateAssets/salaryPenetration/performanceLink/index'), meta: { title: '效益联动分析', icon: 'link-m' } },
			{ path: 'executivePay', name: 'SalaryExecutivePay', component: () => import('@/views/stateAssets/salaryPenetration/executivePay/index'), meta: { title: '高管薪酬监控', icon: 'user-star-line' } },
			{ path: 'incentivePlan', name: 'SalaryIncentivePlan', component: () => import('@/views/stateAssets/salaryPenetration/incentivePlan/index'), meta: { title: '中长期激励计划', icon: 'trophy-line' } },
			{ path: 'laborCost', name: 'SalaryLaborCost', component: () => import('@/views/stateAssets/salaryPenetration/laborCost/index'), meta: { title: '人工成本分析', icon: 'group-line' } },
			{ path: 'complianceCheck', name: 'SalaryComplianceCheck', component: () => import('@/views/stateAssets/salaryPenetration/complianceCheck/index'), meta: { title: '薪酬合规检查', icon: 'shield-check-line' } },
			{ path: 'riskWarning', name: 'SalaryRiskWarning', component: () => import('@/views/stateAssets/salaryPenetration/riskWarning/index'), meta: { title: '薪酬风险预警', icon: 'alarm-warning-line' } },
			{ path: 'drillDown', name: 'SalaryDrillDown', component: () => import('@/views/stateAssets/salaryPenetration/drillDown/index'), meta: { title: '薪酬穿透分析', icon: 'zoom-in-line' } },
		],
	},
	// 合同穿透监管
	{
		path: '/stateAssets/contractPenetration',
		name: 'ContractPenetrationModule',
		component: Layout,
		meta: { title: '合同穿透监管', icon: 'file-contract-line', roles: ['admin', 'editor'] },
		children: [
			{ path: 'home', name: 'ContractPenetrationHome', component: () => import('@/views/stateAssets/contractPenetration/home/index'), meta: { title: '合同穿透首页', icon: 'home-2-line' } },
			{ path: 'dashboard', name: 'ContractPenetrationDashboard', component: () => import('@/views/stateAssets/contractPenetration/dashboard/index'), meta: { title: '监控驾驶舱', icon: 'dashboard-line' } },
			{ path: 'contractList', name: 'ContractListMgmt', component: () => import('@/views/stateAssets/contractPenetration/contractList/index'), meta: { title: '合同台账管理', icon: 'file-list-3-line' } },
			{ path: 'lifecycle', name: 'ContractLifecycle', component: () => import('@/views/stateAssets/contractPenetration/lifecycle/index'), meta: { title: '合同全生命周期', icon: 'time-line' } },
			{ path: 'compliance', name: 'ContractCompliance', component: () => import('@/views/stateAssets/contractPenetration/compliance/index'), meta: { title: '审批合规追踪', icon: 'file-shield-2-line' } },
			{ path: 'execution', name: 'ContractExecution', component: () => import('@/views/stateAssets/contractPenetration/execution/index'), meta: { title: '合同履行监控', icon: 'file-check-line' } },
			{ path: 'dispute', name: 'ContractDispute', component: () => import('@/views/stateAssets/contractPenetration/dispute/index'), meta: { title: '纠纷与诉讼管理', icon: 'scales-3-line' } },
			{ path: 'caseManagement', name: 'ContractCaseManagement', component: () => import('@/views/stateAssets/contractPenetration/caseManagement/index'), meta: { title: '案件管理', icon: 'file-list-2-line' } },
			{ path: 'counterparty', name: 'ContractCounterparty', component: () => import('@/views/stateAssets/contractPenetration/counterparty/index'), meta: { title: '对方信用监控', icon: 'user-star-line' } },
			{ path: 'riskWarning', name: 'ContractRiskWarning', component: () => import('@/views/stateAssets/contractPenetration/riskWarning/index'), meta: { title: '风险预警管理', icon: 'alarm-warning-line' } },
			{ path: 'drillDown', name: 'ContractDrillDown', component: () => import('@/views/stateAssets/contractPenetration/drillDown/index'), meta: { title: '合同穿透分析', icon: 'zoom-in-line' } },
		],
	},
	// 行业穿透监管
	{
		path: '/stateAssets/industryPenetration',
		name: 'IndustryPenetrationModule',
		component: Layout,
		meta: { title: '行业穿透监管', icon: 'building-2-line', roles: ['admin', 'editor'] },
		children: [
{ path: 'home', name: 'IndustryPenetrationHome', component: () => import('@/views/stateAssets/industryPenetration/home/index'), meta: { title: '行业穿透首页', icon: 'home-2-line' } },
{ path: 'dashboard', name: 'IndustryPenetrationDashboard', component: () => import('@/views/stateAssets/industryPenetration/dashboard/index'), meta: { title: '监控驾驶舱', icon: 'dashboard-line' } },
{ path: 'industryLayout', name: 'IndustryLayoutMgmt', component: () => import('@/views/stateAssets/industryPenetration/industryLayout/index'), meta: { title: '行业布局台账', icon: 'file-list-3-line' } },
{ path: 'energy', name: 'IndustryEnergy', component: () => import('@/views/stateAssets/industryPenetration/energy/index'), meta: { title: '能源行业监管', icon: 'flashlight-line' } },
{ path: 'financial', name: 'IndustryFinancial', component: () => import('@/views/stateAssets/industryPenetration/financial/index'), meta: { title: '金融行业监管', icon: 'bank-line' } },
{ path: 'manufacturing', name: 'IndustryManufacturing', component: () => import('@/views/stateAssets/industryPenetration/manufacturing/index'), meta: { title: '制造行业监管', icon: 'settings-3-line' } },
{ path: 'infrastructure', name: 'IndustryInfrastructure', component: () => import('@/views/stateAssets/industryPenetration/infrastructure/index'), meta: { title: '基础设施监管', icon: 'building-4-line' } },
{ path: 'publicService', name: 'IndustryPublicService', component: () => import('@/views/stateAssets/industryPenetration/publicService/index'), meta: { title: '公共服务监管', icon: 'community-line' } },
{ path: 'competitiveness', name: 'IndustryCompetitiveness', component: () => import('@/views/stateAssets/industryPenetration/competitiveness/index'), meta: { title: '竞争力分析', icon: 'medal-line' } },
{ path: 'synergy', name: 'IndustrySynergy', component: () => import('@/views/stateAssets/industryPenetration/synergy/index'), meta: { title: '产业协同分析', icon: 'links-line' } },
{ path: 'riskWarning', name: 'IndustryRiskWarning', component: () => import('@/views/stateAssets/industryPenetration/riskWarning/index'), meta: { title: '风险预警管理', icon: 'alarm-warning-line' } },
{ path: 'drillDown', name: 'IndustryDrillDown', component: () => import('@/views/stateAssets/industryPenetration/drillDown/index'), meta: { title: '行业穿透分析', icon: 'zoom-in-line' } },
		],
	},
// 采购穿透监管
	{
		path: '/stateAssets/procurementPenetration',
		name: 'ProcurementPenetrationModule',
		component: Layout,
		meta: { title: '采购穿透监管', icon: 'shopping-cart-2-line', roles: ['admin', 'editor'] },
		children: [
			{ path: 'home', name: 'ProcurementHome', component: () => import('@/views/stateAssets/procurementPenetration/home/index'), meta: { title: '采购穿透首页', icon: 'home-2-line' } },
			{ path: 'dashboard', name: 'ProcurementDashboard', component: () => import('@/views/stateAssets/procurementPenetration/dashboard/index'), meta: { title: '采购驾驶舱', icon: 'dashboard-line' } },
			{ path: 'purchaseRecord', name: 'PurchaseRecord', component: () => import('@/views/stateAssets/procurementPenetration/purchaseRecord/index'), meta: { title: '采购台账', icon: 'file-list-2-line' } },
			{ path: 'supplierProfile', name: 'SupplierProfile', component: () => import('@/views/stateAssets/procurementPenetration/supplierProfile/index'), meta: { title: '供应商档案', icon: 'user-3-line' } },
			{ path: 'relatedTransaction', name: 'RelatedTransaction', component: () => import('@/views/stateAssets/procurementPenetration/relatedTransaction/index'), meta: { title: '关联交易监控', icon: 'links-line' } },
			{ path: 'biddingCompliance', name: 'BiddingCompliance', component: () => import('@/views/stateAssets/procurementPenetration/biddingCompliance/index'), meta: { title: '招投标合规', icon: 'file-shield-2-line' } },
			{ path: 'contractExecution', name: 'ContractExecution', component: () => import('@/views/stateAssets/procurementPenetration/contractExecution/index'), meta: { title: '合同履约追踪', icon: 'file-check-line' } },
			{ path: 'drillDown', name: 'ProcurementDrillDown', component: () => import('@/views/stateAssets/procurementPenetration/drillDown/index'), meta: { title: '采购风险穿透', icon: 'share-line' } },
			{ path: 'project', name: 'ProcurementPenetrationProject', component: () => import('@/views/stateAssets/procurementPenetration/project/index'), meta: { title: '采购项目台账', icon: 'file-list-line' } },
			{ path: 'supplier', name: 'ProcurementPenetrationSupplier', component: () => import('@/views/stateAssets/procurementPenetration/supplier/index'), meta: { title: '供应商管理', icon: 'team-line' } },
			{ path: 'biddingMonitor', name: 'ProcurementBiddingMonitor', component: () => import('@/views/stateAssets/procurementPenetration/biddingMonitor/index'), meta: { title: '招标过程监控', icon: 'survey-line' } },
			{ path: 'fakeTrade', name: 'ProcurementFakeTrade', component: () => import('@/views/stateAssets/procurementPenetration/fakeTrade/index'), meta: { title: '虚假贸易核查', icon: 'search-eye-line' } },
			{ path: 'priceBenchmark', name: 'ProcurementPriceBenchmark', component: () => import('@/views/stateAssets/procurementPenetration/priceBenchmark/index'), meta: { title: '价格对标分析', icon: 'bar-chart-line' } },
			{ path: 'supplyChainRisk', name: 'ProcurementSupplyChainRisk', component: () => import('@/views/stateAssets/procurementPenetration/supplyChainRisk/index'), meta: { title: '供应链风险分析', icon: 'links-fill' } },
		],
	},
	// 军品穿透监管
	{
		path: '/stateAssets/militaryPenetration',
		name: 'MilitaryPenetrationModule',
		component: Layout,
		meta: { title: '军品穿透监管', icon: 's-flag-line', roles: ['admin', 'editor'] },
		children: [
			{ path: 'home', name: 'MilitaryHome', component: () => import('@/views/stateAssets/militaryPenetration/home/index'), meta: { title: '军品穿透首页', icon: 'home-2-line' } },
			{ path: 'dashboard', name: 'MilitaryDashboard', component: () => import('@/views/stateAssets/militaryPenetration/dashboard/index'), meta: { title: '军品驾驶舱', icon: 'dashboard-line' } },
			{ path: 'taskRecord', name: 'MilitaryTaskRecord', component: () => import('@/views/stateAssets/militaryPenetration/taskRecord/index'), meta: { title: '任务台账', icon: 'file-list-2-line' } },
			{ path: 'qualificationProfile', name: 'QualificationProfile', component: () => import('@/views/stateAssets/militaryPenetration/qualificationProfile/index'), meta: { title: '资质档案', icon: 'shield-star-line' } },
			{ path: 'supplyChainSecurity', name: 'SupplyChainSecurity', component: () => import('@/views/stateAssets/militaryPenetration/supplyChainSecurity/index'), meta: { title: '供应链安全', icon: 'links-line' } },
			{ path: 'subcontractCompliance', name: 'SubcontractCompliance', component: () => import('@/views/stateAssets/militaryPenetration/subcontractCompliance/index'), meta: { title: '分包合规', icon: 'file-shield-2-line' } },
			{ path: 'contractExecution', name: 'MilitaryContractExecution', component: () => import('@/views/stateAssets/militaryPenetration/contractExecution/index'), meta: { title: '合同履约追踪', icon: 'file-check-line' } },
			{ path: 'drillDown', name: 'MilitaryDrillDown', component: () => import('@/views/stateAssets/militaryPenetration/drillDown/index'), meta: { title: '军品风险穿透', icon: 'share-line' } },
		],
	},
	// 会计穿透监管（独立路由）
	{
		path: '/stateAssets/accountingPenetration',
		name: 'AccountingPenetrationModule',
		component: Layout,
		meta: { title: '会计穿透监管', icon: 'calculator-line', roles: ['admin', 'editor'] },
		children: [
			{ path: 'home', name: 'AccountingPenetrationHome', component: () => import('@/views/stateAssets/accountingPenetration/home/index'), meta: { title: '会计穿透首页', icon: 'home-2-line' } },
			{ path: 'dashboard', name: 'AccountingPenetrationDashboard', component: () => import('@/views/stateAssets/accountingPenetration/dashboard/index'), meta: { title: '监控驾驶舱', icon: 'dashboard-line' } },
			{ path: 'voucherPenetration', name: 'VoucherPenetration', component: () => import('@/views/stateAssets/accountingPenetration/voucherPenetration/index'), meta: { title: '凭证穿透查询', icon: 'file-paper-line' } },
			{ path: 'bookPenetration', name: 'BookPenetration', component: () => import('@/views/stateAssets/accountingPenetration/bookPenetration/index'), meta: { title: '账簿穿透查询', icon: 'book-2-line' } },
			{ path: 'reportPenetration', name: 'ReportPenetration', component: () => import('@/views/stateAssets/accountingPenetration/reportPenetration/index'), meta: { title: '财务报表穿透', icon: 'file-chart-line' } },
			{ path: 'budgetMonitor', name: 'BudgetMonitor', component: () => import('@/views/stateAssets/accountingPenetration/budgetMonitor/index'), meta: { title: '预算执行监管', icon: 'funds-box-line' } },
			{ path: 'twoGoldMonitor', name: 'TwoGoldMonitor', component: () => import('@/views/stateAssets/accountingPenetration/twoGoldMonitor/index'), meta: { title: '两金压降监控', icon: 'coin-line' } },
			{ path: 'fraudDetection', name: 'FraudDetection', component: () => import('@/views/stateAssets/accountingPenetration/fraudDetection/index'), meta: { title: '财务造假识别', icon: 'spy-line' } },
			{ path: 'policyAndEstimate', name: 'PolicyAndEstimate', component: () => import('@/views/stateAssets/accountingPenetration/policyAndEstimate/index'), meta: { title: '会计政策与估计', icon: 'scales-3-line' } },
		],
	},
	{
		path: '/financialSharing',
		name: 'FinancialSharing',
		component: Layout,
		meta: {
			title: '财务共享',
			icon: 'money-dollar-circle-line',
			roles: ['admin', 'editor'],
		},
		children: [
			{
				path: 'index',
				name: 'FinancialSharingIndex',
				component: () => import('@/views/financialSharing/index'),
				meta: {
					title: '财务共享首页',
					icon: 'home-2-line',
				},
			},
			// 事项会计中台
			{
				path: 'platform',
				name: 'Platform',
				meta: {
					title: '事项会计中台',
					icon: 'settings-3-line',
				},
				children: [
					{
						path: 'common',
						name: 'PlatformCommon',
						component: () => import('@/views/financialSharing/platform/common/index'),
						meta: {
							title: '财务公共',
							icon: 'settings-line',
						},
					},
					{
						path: 'matter',
						name: 'PlatformMatter',
						component: () => import('@/views/financialSharing/platform/matter/index'),
						meta: {
							title: '事项中心',
							icon: 'file-list-3-line',
						},
					},
					{
						path: 'rules',
						name: 'PlatformRules',
						component: () => import('@/views/financialSharing/platform/rules/index'),
						meta: {
							title: '会计规则中心',
							icon: 'rule-line',
						},
					},
					{
						path: 'accounting',
						name: 'PlatformAccounting',
						component: () => import('@/views/financialSharing/platform/accounting/index'),
						meta: {
							title: '会计中心',
							icon: 'calculator-line',
						},
						children: [
							{
								path: 'voucher-management',
								name: 'VoucherManagement',
								component: () => import('@/views/financialSharing/platform/accounting/voucherManagement/index'),
								meta: {
									title: '凭证管理',
									icon: 'document-line',
								},
							},
							{
								path: 'voucher-generation',
								name: 'VoucherGeneration',
								component: () => import('@/views/financialSharing/platform/accounting/voucherGeneration/index'),
								meta: {
									title: '凭证生成',
									icon: 'add-line',
								},
							},
							{
								path: 'voucher-approval',
								name: 'VoucherApproval',
								component: () => import('@/views/financialSharing/platform/accounting/voucherApproval/index'),
								meta: {
									title: '凭证审核',
									icon: 'check-line',
								},
							},
							{
								path: 'voucher-posting',
								name: 'VoucherPosting',
								component: () => import('@/views/financialSharing/platform/accounting/voucherPosting/index'),
								meta: {
									title: '凭证过账',
									icon: 'send-plane-line',
								},
							},
							{
								path: 'voucher-template',
								name: 'VoucherTemplate',
								component: () => import('@/views/financialSharing/platform/accounting/voucherTemplate/index'),
								meta: {
									title: '凭证模板',
									icon: 'file-copy-line',
								},
							},
							{
								path: 'voucher-analysis',
								name: 'VoucherAnalysis',
								component: () => import('@/views/financialSharing/platform/accounting/voucherAnalysis/index'),
								meta: {
									title: '凭证分析',
									icon: 'bar-chart-line',
								},
							},
						],
					},
				],
			},
			// 财务会计模块
			{
				path: 'financial',
				name: 'Financial',
				meta: {
					title: '财务会计',
					icon: 'money-dollar-box-line',
				},
				children: [
					{
						path: 'generalLedger',
						name: 'GeneralLedger',
						component: () => import('@/views/financialSharing/financial/generalLedger/index'),
						meta: {
							title: '总账',
							icon: 'book-line',
						},
					},
					{
						path: 'generalLedger/accountBalance',
						name: 'AccountBalance',
						component: () => import('@/views/financialSharing/financial/generalLedger/accountBalance/index'),
						meta: {
							title: '科目余额',
							icon: 'money-dollar-circle-line',
						},
					},
					{
						path: 'generalLedger/detailLedger',
						name: 'DetailLedger',
						component: () => import('@/views/financialSharing/financial/generalLedger/detailLedger/index'),
						meta: {
							title: '明细账查询',
							icon: 'file-list-3-line',
						},
					},
					{
						path: 'generalLedger/trialBalance',
						name: 'TrialBalance',
						component: () => import('@/views/financialSharing/financial/generalLedger/trialBalance/index'),
						meta: {
							title: '试算平衡表',
							icon: 'scales-3-line',
						},
					},
					{
						path: 'generalLedger/ledgerQuery',
						name: 'LedgerQuery',
						component: () => import('@/views/financialSharing/financial/generalLedger/ledgerQuery/index'),
						meta: {
							title: '总账查询',
							icon: 'search-line',
						},
					},
					{
						path: 'generalLedger/periodEnd',
						name: 'PeriodEnd',
						component: () => import('@/views/financialSharing/financial/generalLedger/periodEnd/index'),
						meta: {
							title: '期末处理',
							icon: 'calendar-check-line',
						},
					},
					{
						path: 'generalLedger/ledgerAnalysis',
						name: 'LedgerAnalysis',
						component: () => import('@/views/financialSharing/financial/generalLedger/ledgerAnalysis/index'),
						meta: {
							title: '总账分析',
							icon: 'line-chart-line',
						},
					},
					{
						path: 'fixedAssets',
						name: 'FixedAssets',
						component: () => import('@/views/financialSharing/financial/fixedAssets/index'),
						meta: {
							title: '固定资产',
							icon: 'building-line',
						},
						children: [
							{
								path: 'asset-cards',
								name: 'AssetCards',
								component: () => import('@/views/financialSharing/financial/fixedAssets/assetCards/index'),
								meta: { title: '资产卡片管理', icon: 'file-list-3-line' }
							},
							{
								path: 'depreciation',
								name: 'DepreciationManagement',
								component: () => import('@/views/financialSharing/financial/fixedAssets/depreciation/index'),
								meta: { title: '折旧管理', icon: 'line-chart-line' }
							},
							{
								path: 'asset-change',
								name: 'AssetChange',
								component: () => import('@/views/financialSharing/financial/fixedAssets/assetChange/index'),
								meta: { title: '资产变动管理', icon: 'exchange-line' }
							},
							{
								path: 'asset-disposal',
								name: 'AssetDisposal',
								component: () => import('@/views/financialSharing/financial/fixedAssets/assetDisposal/index'),
								meta: { title: '资产处置管理', icon: 'delete-bin-line' }
							},
							{
								path: 'asset-inventory',
								name: 'AssetInventory',
								component: () => import('@/views/financialSharing/financial/fixedAssets/assetInventory/index'),
								meta: { title: '资产盘点管理', icon: 'task-line' }
							},
							{
								path: 'asset-analysis',
								name: 'AssetAnalysis',
								component: () => import('@/views/financialSharing/financial/fixedAssets/assetAnalysis/index'),
								meta: { title: '资产分析', icon: 'bar-chart-box-line' }
							}
						]
					},
					{
						path: 'receivables',
						name: 'Receivables',
						component: () => import('@/views/financialSharing/financial/receivables/index'),
						meta: {
							title: '应收管理',
							icon: 'hand-coin-line',
						},
						children: [
							{
								path: 'receivableRegister',
								name: 'ReceivableRegister',
								component: () => import('@/views/financialSharing/financial/receivables/receivableRegister/index'),
								meta: { title: '应收登记', icon: 'edit-line' }
							},
							{
								path: 'collectionManagement',
								name: 'CollectionManagement',
								component: () => import('@/views/financialSharing/financial/receivables/collectionManagement/index'),
								meta: { title: '收款管理', icon: 'coin-line' }
							},
							{
								path: 'customerManagement',
								name: 'CustomerManagement',
								component: () => import('@/views/financialSharing/financial/receivables/customerManagement/index'),
								meta: { title: '客户管理', icon: 'user-line' }
							},
							{
								path: 'agingAnalysis',
								name: 'AgingAnalysis',
								component: () => import('@/views/financialSharing/financial/receivables/agingAnalysis/index'),
								meta: { title: '账龄分析', icon: 'bar-chart-line' }
							},
							{
								path: 'badDebtManagement',
								name: 'BadDebtManagement',
								component: () => import('@/views/financialSharing/financial/receivables/badDebtManagement/index'),
								meta: { title: '坏账管理', icon: 'error-warning-line' }
							},
							{
								path: 'receivablesAnalysis',
								name: 'ReceivablesAnalysis',
								component: () => import('@/views/financialSharing/financial/receivables/receivablesAnalysis/index'),
								meta: { title: '应收分析', icon: 'pie-chart-line' }
							}
						]
					},
					{
						path: 'payables',
						name: 'Payables',
						component: () => import('@/views/financialSharing/financial/payables/index'),
						meta: {
							title: '应付管理',
							icon: 'secure-payment-line',
						},
						children: [
							{
								path: 'payableRegister',
								name: 'PayableRegister',
								component: () => import('@/views/financialSharing/financial/payables/payableRegister/index'),
								meta: {
									title: '应付登记',
									icon: 'document-line',
								},
							},
							{
								path: 'paymentManagement',
								name: 'PaymentManagement',
								component: () => import('@/views/financialSharing/financial/payables/paymentManagement/index'),
								meta: {
									title: '付款管理',
									icon: 'money-dollar-circle-line',
								},
							},
							{
								path: 'supplierManagement',
								name: 'SupplierManagement',
								component: () => import('@/views/financialSharing/financial/payables/supplierManagement/index'),
								meta: {
									title: '供应商管理',
									icon: 'user-line',
								},
							},
							{
								path: 'paymentTerms',
								name: 'PaymentTerms',
								component: () => import('@/views/financialSharing/financial/payables/paymentTerms/index'),
								meta: {
									title: '账期管理',
									icon: 'calendar-line',
								},
							},
							{
								path: 'billManagement',
								name: 'BillManagement',
								component: () => import('@/views/financialSharing/financial/payables/billManagement/index'),
								meta: {
									title: '票据管理',
									icon: 'file-text-line',
								},
							},
							{
								path: 'payableAnalysis',
								name: 'PayableAnalysis',
								component: () => import('@/views/financialSharing/financial/payables/payableAnalysis/index'),
								meta: {
									title: '应付分析',
									icon: 'pie-chart-line',
								},
							},
						],
					},
					{
						path: 'inventory',
						name: 'Inventory',
						component: () => import('@/views/financialSharing/financial/inventory/index'),
						meta: {
							title: '存货核算',
							icon: 'archive-line',
						},
						children: [
							{
								path: 'valuation',
								name: 'InventoryValuation',
								component: () => import('@/views/financialSharing/financial/inventory/inventoryValuation/index'),
								meta: {
									title: '存货计价',
									icon: 'coin-line',
								},
							},
							{
								path: 'costTransfer',
								name: 'CostTransfer',
								component: () => import('@/views/financialSharing/financial/inventory/costTransfer/index'),
								meta: {
									title: '成本结转',
									icon: 'exchange-line',
								},
							},
							{
								path: 'check',
								name: 'InventoryCheck',
								component: () => import('@/views/financialSharing/financial/inventory/inventoryCheck/index'),
								meta: {
									title: '存货盘点',
									icon: 'eye-line',
								},
							},
							{
								path: 'category',
								name: 'InventoryCategory',
								component: () => import('@/views/financialSharing/financial/inventory/inventoryCategory/index'),
								meta: {
									title: '存货分类',
									icon: 'folder-line',
								},
							},
							{
								path: 'alert',
								name: 'InventoryAlert',
								component: () => import('@/views/financialSharing/financial/inventory/inventoryAlert/index'),
								meta: {
									title: '库存预警',
									icon: 'alarm-warning-line',
								},
							},
							{
								path: 'analysis',
								name: 'InventoryAnalysis',
								component: () => import('@/views/financialSharing/financial/inventory/inventoryAnalysis/index'),
								meta: {
									title: '存货分析',
									icon: 'pie-chart-line',
								},
							},
							{
								path: 'master',
								name: 'InventoryMaster',
								component: () => import('@/views/financialSharing/financial/inventory/inventoryMaster/index'),
								meta: {
									title: '存货档案',
									icon: 'file-list-line',
								},
							}
						]
					},
					{
						path: 'revenueManagement',
						name: 'RevenueManagement',
						component: () => import('@/views/financialSharing/financial/revenueManagement/index'),
						meta: {
							title: '收入管理',
							icon: 'line-chart-line',
						},
						children: [
							{
								path: 'revenueRecognition',
								name: 'RevenueRecognition',
								component: () => import('@/views/financialSharing/financial/revenueManagement/revenueRecognition/index'),
								meta: {
									title: '收入确认管理',
									icon: 'check-circle-line',
								},
							},
							{
								path: 'revenueAllocation',
								name: 'RevenueAllocation',
								component: () => import('@/views/financialSharing/financial/revenueManagement/revenueAllocation/index'),
								meta: {
									title: '收入分配管理',
									icon: 'share-line',
								},
							},
							{
								path: 'contractRevenue',
								name: 'ContractRevenue',
								component: () => import('@/views/financialSharing/financial/revenueManagement/contractRevenue/index'),
								meta: {
									title: '合同收入管理',
									icon: 'file-text-line',
								},
							},
							{
								path: 'deferredRevenue',
								name: 'DeferredRevenue',
								component: () => import('@/views/financialSharing/financial/revenueManagement/deferredRevenue/index'),
								meta: {
									title: '递延收入管理',
									icon: 'time-line',
								},
							},
							{
								path: 'revenueAdjustment',
								name: 'RevenueAdjustment',
								component: () => import('@/views/financialSharing/financial/revenueManagement/revenueAdjustment/index'),
								meta: {
									title: '收入调整管理',
									icon: 'refresh-line',
								},
							},
							{
								path: 'revenueAnalysis',
								name: 'RevenueAnalysis',
								component: () => import('@/views/financialSharing/financial/revenueManagement/revenueAnalysis/index'),
								meta: {
									title: '收入分析',
									icon: 'pie-chart-line',
								},
							}
						]
					},
				],
			},
			// 管理会计模块
			{
				path: 'management',
				name: 'Management',
				component: () => import('@/views/financialSharing/management/layout'),
				meta: {
					title: '管理会计',
					icon: 'pie-chart-line',
				},
				children: [
					{
						path: 'index',
						name: 'ManagementIndex',
						component: () => import('@/views/financialSharing/management/index'),
						meta: {
							title: '管理会计总览',
							icon: 'dashboard-3-line',
						},
					},
					{
						path: 'costCenter',
						name: 'CostCenter',
						component: () => import('@/views/financialSharing/management/costCenter/index'),
						meta: {
							title: '成本中心',
							icon: 'focus-3-line',
						},
					},
					{
						path: 'costCenter/centerSetup',
						name: 'CostCenterSetup',
						hidden: true,
						component: () => import('@/views/financialSharing/management/costCenter/centerSetup/index.vue'),
						meta: {
							title: '成本中心设置',
							icon: 'settings-3-line',
						},
					},
					{
						path: 'costCenter/costCollection',
						name: 'CostCollection',
						hidden: true,
						component: () => import('@/views/financialSharing/management/costCenter/costCollection/index.vue'),
						meta: {
							title: '成本归集',
							icon: 'inbox-line',
						},
					},
					{
						path: 'costCenter/costAllocation',
						name: 'CostAllocation',
						hidden: true,
						component: () => import('@/views/financialSharing/management/costCenter/costAllocation/index.vue'),
						meta: {
							title: '成本分摊',
							icon: 'share-line',
						},
					},
					{
						path: 'costCenter/costBudget',
						name: 'CostBudget',
						hidden: true,
						component: () => import('@/views/financialSharing/management/costCenter/costBudget/index.vue'),
						meta: {
							title: '成本预算',
							icon: 'money-dollar-circle-line',
						},
					},
					{
						path: 'costCenter/costControl',
						name: 'CostControl',
						hidden: true,
						component: () => import('@/views/financialSharing/management/costCenter/costControl/index.vue'),
						meta: {
							title: '成本控制',
							icon: 'shield-check-line',
						},
					},
					{
						path: 'costCenter/costAnalysis',
						name: 'CostAnalysisPage',
						hidden: true,
						component: () => import('@/views/financialSharing/management/costCenter/costAnalysis/index.vue'),
						meta: {
							title: '成本分析',
							icon: 'bar-chart-line',
						},
					},
					{
						path: 'productCost',
						name: 'ProductCost',
						component: () => import('@/views/financialSharing/management/productCost/index'),
						meta: {
							title: '产品成本',
							icon: 'product-hunt-line',
						},
					},
					{
						path: 'productCost/productInfo',
						name: 'ProductInfo',
						hidden: true,
						component: () => import('@/views/financialSharing/management/productCost/productInfo/index'),
						meta: {
							title: '产品信息管理',
							icon: 'product-hunt-line',
						},
					},
					{
						path: 'productCost/costAccounting',
						name: 'CostAccounting',
						hidden: true,
						component: () => import('@/views/financialSharing/management/productCost/costAccounting/index'),
						meta: {
							title: '成本核算',
							icon: 'calculator-line',
						},
					},
					{
						path: 'productCost/costAnalysis',
						name: 'ProductCostAnalysis',
						hidden: true,
						component: () => import('@/views/financialSharing/management/productCost/costAnalysis/index'),
						meta: {
							title: '成本分析',
							icon: 'bar-chart-line',
						},
					},
					{
						path: 'productCost/costControl',
						name: 'CostControl',
						hidden: true,
						component: () => import('@/views/financialSharing/management/productCost/costControl/index'),
						meta: {
							title: '成本控制',
							icon: 'shield-check-line',
						},
					},
					{
						path: 'productCost/bomManagement',
						name: 'BomManagement',
						hidden: true,
						component: () => import('@/views/financialSharing/management/productCost/bomManagement/index'),
						meta: {
							title: 'BOM管理',
							icon: 'list-check-line',
						},
					},
					{
						path: 'productCost/costReport',
						name: 'CostReport',
						hidden: true,
						component: () => import('@/views/financialSharing/management/productCost/costReport/index'),
						meta: {
							title: '成本报告',
							icon: 'file-chart-line',
						},
					},
					{
						path: 'costEstimation',
						name: 'CostEstimation',
						component: () => import('@/views/financialSharing/management/costEstimation/index'),
						meta: {
							title: '成本估算',
							icon: 'calculator-line',
						},
						children: [
							{
								path: 'schemeManagement',
								name: 'CostEstimationSchemeManagement',
								hidden: true,
								component: () => import('@/views/financialSharing/management/costEstimation/schemeManagement/index'),
								meta: {
									title: '估算方案管理',
									icon: 'file-list-3-line',
								},
							},
							{
								path: 'costSimulation',
								name: 'CostEstimationCostSimulation',
								hidden: true,
								component: () => import('@/views/financialSharing/management/costEstimation/costSimulation/index'),
								meta: {
									title: '成本模拟',
									icon: 'line-chart-line',
								},
							},
							{
								path: 'varianceAnalysis',
								name: 'CostEstimationVarianceAnalysis',
								hidden: true,
								component: () => import('@/views/financialSharing/management/costEstimation/varianceAnalysis/index'),
								meta: {
									title: '差异分析',
									icon: 'bar-chart-box-line',
								},
							},
							{
								path: 'budgetPreparation',
								name: 'CostEstimationBudgetPreparation',
								hidden: true,
								component: () => import('@/views/financialSharing/management/costEstimation/budgetPreparation/index'),
								meta: {
									title: '预算编制',
									icon: 'money-dollar-circle-line',
								},
							},
							{
								path: 'costModel',
								name: 'CostEstimationCostModel',
								hidden: true,
								component: () => import('@/views/financialSharing/management/costEstimation/costModel/index'),
								meta: {
									title: '成本模型',
									icon: 'cpu-line',
								},
							},
							{
								path: 'estimationReport',
								name: 'CostEstimationEstimationReport',
								hidden: true,
								component: () => import('@/views/financialSharing/management/costEstimation/estimationReport/index'),
								meta: {
									title: '估算报告',
									icon: 'document-line',
								},
							},
						],
					},
					{
						path: 'specialCost',
						name: 'SpecialCost',
						component: () => import('@/views/financialSharing/management/specialCost/index'),
						meta: {
							title: '专项成本',
							icon: 'funds-line',
						},
					},
					{
						path: 'specialCost/projectCost',
						name: 'ProjectCostIndex',
						hidden: true,
						component: () => import('@/views/financialSharing/management/specialCost/projectCost/index.vue'),
						meta: {
							title: '项目成本管理',
							icon: 'folder-line',
						},
					},
					{
						path: 'specialCost/activityCost',
						name: 'ActivityCostIndex',
						hidden: true,
						component: () => import('@/views/financialSharing/management/specialCost/activityCost/index.vue'),
						meta: {
							title: '作业成本管理',
							icon: 'task-line',
						},
					},
					{
						path: 'specialCost/qualityCost',
						name: 'QualityCostIndex',
						hidden: true,
						component: () => import('@/views/financialSharing/management/specialCost/qualityCost/index.vue'),
						meta: {
							title: '质量成本管理',
							icon: 'award-line',
						},
					},
					{
						path: 'specialCost/environmentCost',
						name: 'EnvironmentCostIndex',
						hidden: true,
						component: () => import('@/views/financialSharing/management/specialCost/environmentCost/index.vue'),
						meta: {
							title: '环境成本管理',
							icon: 'leaf-line',
						},
					},
					{
						path: 'specialCost/rdCost',
						name: 'RdCostIndex',
						hidden: true,
						component: () => import('@/views/financialSharing/management/specialCost/rdCost/index.vue'),
						meta: {
							title: '研发成本管理',
							icon: 'flask-line',
						},
					},
					{
						path: 'specialCost/specialAnalysis',
						name: 'SpecialAnalysisIndex',
						hidden: true,
						component: () => import('@/views/financialSharing/management/specialCost/specialAnalysis/index.vue'),
						meta: {
							title: '专项分析',
							icon: 'line-chart-line',
						},
					},
					{
						path: 'internalSettlement',
						name: 'InternalSettlement',
						component: () => import('@/views/financialSharing/management/internalSettlement/index'),
						meta: {
							title: '内部结算',
							icon: 's-cooperation',
						},
					},
					{
						path: 'internalSettlement/internalTransaction',
						name: 'InternalTransaction',
						component: () => import('@/views/financialSharing/management/internalSettlement/internalTransaction/index'),
						meta: {
							title: '内部交易管理',
							icon: 's-cooperation',
						},
					},
					{
						path: 'internalSettlement/transferPricing',
						name: 'TransferPricing',
						component: () => import('@/views/financialSharing/management/internalSettlement/transferPricing/index'),
						meta: {
							title: '转移定价管理',
							icon: 'price-tag',
						},
					},
					{
						path: 'internalSettlement/profitCenter',
						name: 'ProfitCenter',
						component: () => import('@/views/financialSharing/management/internalSettlement/profitCenter/index'),
						meta: {
							title: '利润中心管理',
							icon: 'trophy',
						},
					},
					{
						path: 'internalSettlement/settlementProcess',
						name: 'SettlementProcess',
						component: () => import('@/views/financialSharing/management/internalSettlement/settlementProcess/index'),
						meta: {
							title: '结算处理',
							icon: 's-finance',
						},
					},
					{
						path: 'internalSettlement/fundManagement',
						name: 'FundManagement',
						component: () => import('@/views/financialSharing/management/internalSettlement/fundManagement/index'),
						meta: {
							title: '资金管理',
							icon: 'money',
						},
					},
					{
						path: 'internalSettlement/settlementAnalysis',
						name: 'SettlementAnalysis',
						component: () => import('@/views/financialSharing/management/internalSettlement/settlementAnalysis/index'),
						meta: {
							title: '结算分析',
							icon: 'data-analysis',
						},
					},
				],
			},
			// 报表分析模块
			{
				path: 'reports',
				name: 'Reports',
				meta: {
					title: '报表分析',
					icon: 'bar-chart-box-line',
				},
				children: [
					{
						path: 'index',
						name: 'ReportsIndex',
						component: () => import('@/views/financialSharing/reports/index'),
						meta: {
							title: '报表分析总览',
							icon: 'dashboard-line',
						},
					},
					{
						path: 'financial',
						name: 'FinancialReports',
						component: () => import('@/views/financialSharing/reports/financial/index.vue'),
						meta: {
							title: '财务报表',
							icon: 'file-text-line',
						},
					},
					{
						path: 'business',
						name: 'BusinessAnalysis',
						component: () => import('@/views/financialSharing/reports/business/index.vue'),
						meta: {
							title: '业务分析',
							icon: 'bar-chart-line',
						},
					},
				],
			},
			// 基础配置模块
			{
				path: 'baseConfig/index',
				name: 'BaseConfigIndex',
				component: () => import('@/views/financialSharing/baseConfig/index'),
				meta: {
					title: '基础配置',
					icon: 'settings-3-line',
				},
			},
			{
				path: 'baseConfig/projectConfig',
				name: 'ProjectConfig',
				component: () => import('@/views/financialSharing/baseConfig/projectConfig/index'),
				meta: {
					title: '项目配置',
					icon: 'folder-opened',
				},
			},
			{
				path: 'baseConfig/travelStandard',
				name: 'TravelStandard',
				component: () => import('@/views/financialSharing/baseConfig/travelStandard/index'),
				meta: {
					title: '差旅标准',
					icon: 'guide',
				},
			},
			{
				path: 'baseConfig/auditRule',
				name: 'AuditRule',
				component: () => import('@/views/financialSharing/baseConfig/auditRule/index'),
				meta: {
					title: '审批规则',
					icon: 'document-checked',
				},
			},
			{
				path: 'baseConfig/budgetControlRule',
				name: 'BudgetControlRule',
				component: () => import('@/views/financialSharing/baseConfig/budgetControlRule/index'),
				meta: {
					title: '预算控制规则',
					icon: 'coin',
				},
			},
			{
				path: 'baseConfig/billConfig',
				name: 'BillConfig',
				component: () => import('@/views/financialSharing/baseConfig/billConfig/index'),
				meta: {
					title: '单据配置',
					icon: 'tickets',
				},
			},
			{
				path: 'baseConfig/travelArchive',
				name: 'TravelArchive',
				component: () => import('@/views/financialSharing/baseConfig/travelArchive/index'),
				meta: {
					title: '差旅归档',
					icon: 'files',
				},
			},
			{
				path: 'baseConfig/generalStandard',
				name: 'GeneralStandard',
				component: () => import('@/views/financialSharing/baseConfig/generalStandard/index'),
				meta: {
					title: '通用标准',
					icon: 'data-board',
				},
			},
			{
				path: 'baseConfig/expenseParameter',
				name: 'ExpenseParameter',
				component: () => import('@/views/financialSharing/baseConfig/expenseParameter/index'),
				meta: {
					title: '费用参数',
					icon: 's-tools',
				},
			},
			{
				path: 'baseConfig/proxyDelegation',
				name: 'ProxyDelegation',
				component: () => import('@/views/financialSharing/baseConfig/proxyDelegation/index'),
				meta: {
					title: '代理委托',
					icon: 'user',
				},
			},
			{
				path: 'baseConfig/mobileSettings',
				name: 'MobileSettings',
				component: () => import('@/views/financialSharing/baseConfig/mobileSettings/index'),
				meta: {
					title: '移动设置',
					icon: 'smartphone-line',
				},
			},
			{
				path: 'baseConfig/accountingRule',
				name: 'AccountingRule',
				component: () => import('@/views/financialSharing/baseConfig/accountingRule/index'),
				meta: {
					title: '会计规则',
					icon: 'notebook-2',
				},
			},
			// 移动设置模板配置（隐藏路由）
			{
				path: 'baseConfig/mobileSettings/templates',
				name: 'MobileSettingsTemplates',
				hidden: true,
				component: () => import('@/views/financialSharing/baseConfig/mobileSettings/templates'),
				meta: {
					title: '模板配置',
					icon: 'file-list-3-line',
				},
			},
		],
	},
	// 企业管理功能模块
	{
		path: '/enterprise',
		name: 'Enterprise',
		component: Layout,
		meta: {
			title: '企业管理',
			icon: 'building-line',
			roles: ['admin', 'editor'],
		},
		children: [
			{
				path: 'dashboard',
				name: 'EnterpriseDashboard',
				component: () => import('@/views/enterprise/dashboard/index'),
				meta: {
					title: '管理驾驶舱',
					icon: 'dashboard-line',
				},
			},
			{
				path: 'data',
				name: 'EnterpriseData',
				component: () => import('@/views/enterprise/data/index'),
				meta: {
					title: '数据管理',
					icon: 'database-line',
				},
			},
			{
				path: 'financial',
				name: 'EnterpriseFinancial',
				component: () => import('@/views/enterprise/financial/index'),
				meta: {
					title: '财务管理',
					icon: 'money-dollar-circle-line',
				},
			},
			{
				path: 'operation',
				name: 'EnterpriseOperation',
				component: () => import('@/views/enterprise/operation/index'),
				meta: {
					title: '经营管理',
					icon: 'line-chart-line',
				},
			},
			{
				path: 'risk',
				name: 'EnterpriseRisk',
				component: () => import('@/views/enterprise/risk/index'),
				meta: {
					title: '风险管理',
					icon: 'shield-check-line',
				},
			},
			{
				path: 'hr',
				name: 'EnterpriseHR',
				component: () => import('@/views/enterprise/hr/index'),
				meta: {
					title: '人力资源管理',
					icon: 'user-line',
				},
			},
			{
				path: 'supply',
				name: 'EnterpriseSupply',
				component: () => import('@/views/enterprise/supply/index'),
				meta: {
					title: '供应链管理',
					icon: 'truck-line',
				},
			},
			{
				path: 'innovation',
				name: 'EnterpriseInnovation',
				component: () => import('@/views/enterprise/innovation/index'),
				meta: {
					title: '技术创新管理',
					icon: 'lightbulb-line',
				},
			},
			{
				path: 'strategy',
				name: 'EnterpriseStrategy',
				component: () => import('@/views/enterprise/strategy/index'),
				meta: {
					title: '战略管理',
					icon: 'compass-line',
				},
			},
		],
	},
	// 行业特色功能模块
	{
		path: '/industry',
		name: 'Industry',
		component: Layout,
		meta: {
			title: '行业特色监管',
			icon: 'building-4-line',
			roles: ['admin', 'editor'],
		},
		children: [
			{
				path: 'financial',
				name: 'IndustryFinancial',
				component: () => import('@/views/industry/financial/index'),
				meta: {
					title: '金融类国企监管',
					icon: 'bank-line',
				},
			},
			{
				path: 'energy',
				name: 'IndustryEnergy',
				component: () => import('@/views/industry/energy/index'),
				meta: {
					title: '能源类国企监管',
					icon: 'flashlight-line',
				},
			},
			{
				path: 'manufacturing',
				name: 'IndustryManufacturing',
				component: () => import('@/views/industry/manufacturing/index'),
				meta: {
					title: '制造类国企监管',
					icon: 'settings-3-line',
				},
			},
			{
				path: 'infrastructure',
				name: 'IndustryInfrastructure',
				component: () => import('@/views/industry/infrastructure/index'),
				meta: {
					title: '基础设施监管',
					icon: 'road-map-line',
				},
			},
			{
				path: 'publicService',
				name: 'IndustryPublicService',
				component: () => import('@/views/industry/publicService/index'),
				meta: {
					title: '公共服务监管',
					icon: 'service-line',
				},
			},
		],
	},
	// 智能化功能模块
	{
		path: '/intelligent',
		name: 'Intelligent',
		component: Layout,
		meta: {
			title: '智能化监管',
			icon: 'brain-line',
			roles: ['admin', 'editor'],
		},
		children: [
			{
				path: 'riskIdentification',
				name: 'IntelligentRiskIdentification',
				component: () => import('@/views/intelligent/riskIdentification/index'),
				meta: {
					title: '智能风险识别',
					icon: 'shield-check-line',
				},
			},
			{
				path: 'decisionSupport',
				name: 'IntelligentDecisionSupport',
				component: () => import('@/views/intelligent/decisionSupport/index'),
				meta: {
					title: '智能决策支持',
					icon: 'lightbulb-line',
				},
			},
			{
				path: 'dataAnalysis',
				name: 'IntelligentDataAnalysis',
				component: () => import('@/views/intelligent/dataAnalysis/index'),
				meta: {
					title: '智能数据分析',
					icon: 'bar-chart-line',
				},
			},
		],
	},
	// 高级分析功能模块
	{
		path: '/advanced',
		name: 'Advanced',
		component: Layout,
		meta: {
			title: '高级分析',
			icon: 'function-line',
			roles: ['admin', 'editor'],
		},
		children: [
			{
				path: 'decisionAnalysis',
				name: 'AdvancedDecisionAnalysis',
				component: () => import('@/views/advanced/decisionAnalysis/index'),
				meta: {
					title: '决策支持分析',
					icon: 'compass-3-line',
				},
			},
			{
				path: 'policyManagement',
				name: 'AdvancedPolicyManagement',
				component: () => import('@/views/advanced/policyManagement/index'),
				meta: {
					title: '政策管理',
					icon: 'government-line',
				},
			},
			{
				path: 'capitalLayout',
				name: 'AdvancedCapitalLayout',
				component: () => import('@/views/advanced/capitalLayout/index'),
				meta: {
					title: '国有资本布局优化',
					icon: 'funds-line',
				},
			},
			{
				path: 'knowledgeManagement',
				name: 'AdvancedKnowledgeManagement',
				component: () => import('@/views/advanced/knowledgeManagement/index'),
				meta: {
					title: '知识管理',
					icon: 'book-open-line',
				},
			},
		],
	},

	// 企业负责人管理模块
	{
		path: '/leader',
		name: 'Leader',
		component: Layout,
		meta: {
			title: '企业负责人管理',
			icon: 'user-star-line',
			roles: ['admin', 'editor'],
		},
		children: [
			{
				path: 'management',
				name: 'LeaderManagement',
				component: () => import('@/views/leader/management/index'),
				meta: { title: '负责人信息管理', icon: 'user-settings-line' },
			},
			{
				path: 'evaluation',
				name: 'LeaderEvaluation',
				component: () => import('@/views/leader/evaluation/index'),
				meta: { title: '负责人考核评价', icon: 'medal-line' },
			},
			{
				path: 'supervision',
				name: 'LeaderSupervision',
				component: () => import('@/views/leader/supervision/index'),
				meta: { title: '负责人监管', icon: 'shield-user-line' },
			},
			{
				path: 'development',
				name: 'LeaderDevelopment',
				component: () => import('@/views/leader/development/index'),
				meta: { title: '负责人发展', icon: 'graduation-cap-line' },
			},
		],
	},

	// 监管报告模块
	{
		path: '/report',
		name: 'Report',
		component: Layout,
		meta: {
			title: '监管报告',
			icon: 'file-chart-line',
			roles: ['admin', 'editor'],
		},
		children: [
			{
				path: 'template',
				name: 'ReportTemplate',
				component: () => import('@/views/report/template/index'),
				meta: { title: '报告模板管理', icon: 'file-text-line' },
			},
			{
				path: 'generation',
				name: 'ReportGeneration',
				component: () => import('@/views/report/generation/index'),
				meta: { title: '报告生成', icon: 'file-add-line' },
			},
			{
				path: 'quality',
				name: 'ReportQuality',
				component: () => import('@/views/report/quality/index'),
				meta: { title: '报告质量控制', icon: 'file-shield-line' },
			},
			{
				path: 'distribution',
				name: 'ReportDistribution',
				component: () => import('@/views/report/distribution/index'),
				meta: { title: '报告分发管理', icon: 'file-transfer-line' },
			},
		],
	},

	// 内控合规模块
	{
		path: '/compliance',
		name: 'Compliance',
		component: Layout,
		meta: {
			title: '内控合规管理',
			icon: 'shield-check-line',
			roles: ['admin', 'editor'],
		},
		children: [
			{
				path: 'system',
				name: 'ComplianceSystem',
				component: () => import('@/views/compliance/system/index'),
				meta: { title: '内控制度管理', icon: 'book-2-line' },
			},
			{
				path: 'execution',
				name: 'ComplianceExecution',
				component: () => import('@/views/compliance/execution/index'),
				meta: { title: '内控执行监控', icon: 'task-line' },
			},
			{
				path: 'rules',
				name: 'ComplianceRules',
				component: () => import('@/views/compliance/rules/index'),
				meta: { title: '合规规则管理', icon: 'scales-line' },
			},
			{
				path: 'check',
				name: 'ComplianceCheck',
				component: () => import('@/views/compliance/check/index'),
				meta: { title: '合规性检查', icon: 'search-line' },
			},
		],
	},

	// 党建社会责任模块
	{
		path: '/party',
		name: 'Party',
		component: Layout,
		meta: {
			title: '党建社会责任',
			icon: 'flag-line',
			roles: ['admin', 'editor'],
		},
		children: [
			{
				path: 'building',
				name: 'PartyBuilding',
				component: () => import('@/views/party/building/index'),
				meta: { title: '党建工作管理', icon: 'team-line' },
			},
			{
				path: 'responsibility',
				name: 'SocialResponsibility',
				component: () => import('@/views/party/responsibility/index'),
				meta: { title: '社会责任管理', icon: 'heart-line' },
			},
			{
				path: 'activities',
				name: 'PartyActivities',
				component: () => import('@/views/party/activities/index'),
				meta: { title: '党建活动管理', icon: 'calendar-event-line' },
			},
			{
				path: 'reports',
				name: 'ResponsibilityReports',
				component: () => import('@/views/party/reports/index'),
				meta: { title: '社会责任报告', icon: 'file-paper-line' },
			},
		],
	},

  // NCV65全面预算系统模块
  ncv65Router,

  // 系统监控模块
  systemMonitorRouter,
  // 全面预算快捷路由
  {
    path: '/qmys',
    component: Layout,
    redirect: '/qmys/taskManagement',
    name: 'Qmys',
    meta: { title: '全面预算', icon: 'el-icon-s-finance' },
    children: [
      {
        path: 'taskManagement',
        component: () => import('@/views/managementAccountant/ncv65/budgetPreparation/TaskManagement'),
        name: 'QmysTaskManagement',
        meta: { title: '预算任务管理' },
      },
      {
        path: 'budgetDataEntry',
        component: () => import('@/views/managementAccountant/ncv65/budgetPreparation/BudgetDataEntry'),
        name: 'QmysBudgetDataEntry',
        meta: { title: '预算数据录入' },
      },
      {
        path: 'approvalFlow',
        component: () => import('@/views/managementAccountant/ncv65/budgetPreparation/ApprovalFlow'),
        name: 'QmysApprovalFlow',
        meta: { title: '审批流程任务' },
      },
      {
        path: 'budgetAdjustment',
        component: () => import('@/views/managementAccountant/ncv65/budgetPreparation/BudgetAdjustment'),
        name: 'QmysBudgetAdjustment',
        meta: { title: '预算调整管理' },
      },
      {
        path: 'budgetVersion',
        component: () => import('@/views/managementAccountant/ncv65/budgetPreparation/BudgetVersion'),
        name: 'QmysBudgetVersion',
        meta: { title: '版本控制管理' },
      },
      {
        path: 'budgetTemplate',
        component: () => import('@/views/managementAccountant/ncv65/budgetPreparation/BudgetTemplate'),
        name: 'QmysBudgetTemplate',
        meta: { title: '模版管理' },
      },
      {
        path: 'budgetRule',
        component: () => import('@/views/managementAccountant/ncv65/budgetPreparation/BudgetRule'),
        name: 'QmysBudgetRule',
        meta: { title: '预算规则管理' },
      },
      {
        path: 'budgetWorkflow',
        component: () => import('@/views/managementAccountant/ncv65/budgetPreparation/BudgetWorkflow'),
        name: 'QmysBudgetWorkflow',
        meta: { title: '预算工作流管理' },
      },
      {
        path: 'BudgetExecution',
        component: () => import('@/views/managementAccountant/ncv65/budgetControl/BudgetExecution'),
        name: 'QmysBudgetExecution',
        meta: { title: '预算执行监控' },
      },
      {
        path: 'BudgetControl',
        component: () => import('@/views/managementAccountant/ncv65/budgetControl/BudgetControl'),
        name: 'QmysBudgetControlPage',
        meta: { title: '预算控制管理' },
      },
      {
        path: 'BudgetWarning',
        component: () => import('@/views/managementAccountant/ncv65/budgetControl/BudgetWarning'),
        name: 'QmysBudgetWarning',
        meta: { title: '预算预警管理' },
      },
      {
        path: 'BudgetAlert',
        component: () => import('@/views/managementAccountant/ncv65/budgetControl/BudgetAlert'),
        name: 'QmysBudgetAlert',
        meta: { title: '预算警报管理' },
      },
      {
        path: 'BudgetLimit',
        component: () => import('@/views/managementAccountant/ncv65/budgetControl/BudgetLimit'),
        name: 'QmysBudgetLimit',
        meta: { title: '预算限额管理' },
      },
      {
        path: 'BudgetQuota',
        component: () => import('@/views/managementAccountant/ncv65/budgetControl/BudgetQuota'),
        name: 'QmysBudgetQuota',
        meta: { title: '预算配额管理' },
      },
      {
        path: 'RollingBudget',
        component: () => import('@/views/managementAccountant/ncv65/advancedFeatures/RollingBudget.vue'),
        name: 'QmysRollingBudget',
        meta: { title: '滚动预算' },
      },
      {
        path: 'FormulaTrace',
        component: () => import('@/views/managementAccountant/ncv65/advancedFeatures/FormulaTrace.vue'),
        name: 'QmysFormulaTrace',
        meta: { title: '公式追踪' },
      },
      {
        path: 'ReminderManagement',
        component: () => import('@/views/managementAccountant/ncv65/advancedFeatures/ReminderManagement.vue'),
        name: 'QmysReminderManagement',
        meta: { title: '催报管理' },
      },
      {
        path: 'DrillThroughQuery',
        component: () => import('@/views/managementAccountant/ncv65/advancedFeatures/DrillThroughQuery.vue'),
        name: 'QmysDrillThroughQuery',
        meta: { title: '穿透查询' },
      },
      {
        path: 'CurrencyManagement',
        component: () => import('@/views/managementAccountant/ncv65/advancedFeatures/CurrencyManagement.vue'),
        name: 'QmysCurrencyManagement',
        meta: { title: '多币管理' },
      },
      {
        path: 'BatchCalculation',
        component: () => import('@/views/managementAccountant/ncv65/advancedFeatures/BatchCalculation.vue'),
        name: 'QmysBatchCalculation',
        meta: { title: '批量计算' },
      },
      {
        path: 'IntelligentRecommendation',
        component: () => import('@/views/managementAccountant/ncv65/advancedFeatures/IntelligentRecommendation.vue'),
        name: 'QmysIntelligentRecommendation',
        meta: { title: '智能推荐' },
      },
      {
        path: 'BudgetSimulation',
        component: () => import('@/views/managementAccountant/ncv65/advancedFeatures/BudgetSimulation.vue'),
        name: 'QmysBudgetSimulation',
        meta: { title: '预算模拟' },
      },
      {
        path: 'DataMining',
        component: () => import('@/views/managementAccountant/ncv65/advancedFeatures/DataMining.vue'),
        name: 'QmysDataMining',
        meta: { title: '数据挖掘' },
      },
      {
        path: 'BudgetOptimization',
        component: () => import('@/views/managementAccountant/ncv65/advancedFeatures/BudgetOptimization.vue'),
        name: 'QmysBudgetOptimization',
        meta: { title: '预算优化' },
      },
      {
        path: 'RiskAssessment',
        component: () => import('@/views/managementAccountant/ncv65/advancedFeatures/RiskAssessment.vue'),
        name: 'QmysRiskAssessment',
        meta: { title: '风险评估' },
      },
      {
        path: 'CollaborativeBudgeting',
        component: () => import('@/views/managementAccountant/ncv65/advancedFeatures/CollaborativeBudgeting.vue'),
        name: 'QmysCollaborativeBudgeting',
        meta: { title: '协同编制' },
      },
      {
        path: 'VersionComparison',
        component: () => import('@/views/managementAccountant/ncv65/advancedFeatures/VersionComparison.vue'),
        name: 'QmysVersionComparison',
        meta: { title: '版本对比' },
      },
      {
        path: 'AutomationWorkflow',
        component: () => import('@/views/managementAccountant/ncv65/advancedFeatures/AutomationWorkflow.vue'),
        name: 'QmysAutomationWorkflow',
        meta: { title: '自动化流程' },
      },
      {
        path: 'AdvancedReports',
        component: () => import('@/views/managementAccountant/ncv65/advancedFeatures/AdvancedReports.vue'),
        name: 'QmysAdvancedReports',
        meta: { title: '高级报表' },
      },
      {
        path: 'OrganizationStructure',
        component: () => import('@/views/managementAccountant/ncv65/budgetSystem/OrganizationStructure'),
        name: 'QmysOrganizationStructure',
        meta: { title: '组织架构管理' },
      },
      {
        path: 'DimensionConfiguration',
        component: () => import('@/views/managementAccountant/ncv65/budgetSystem/DimensionConfiguration'),
        name: 'QmysDimensionConfiguration',
        meta: { title: '维度设置' },
      },
      {
        path: 'IndicatorManagement',
        component: () => import('@/views/managementAccountant/ncv65/budgetSystem/IndicatorManagement'),
        name: 'QmysIndicatorManagement',
        meta: { title: '指标管理' },
      },
      {
        path: 'BudgetModel',
        component: () => import('@/views/managementAccountant/ncv65/budgetSystem/BudgetModel'),
        name: 'QmysBudgetModel',
        meta: { title: '预算模型' },
      },
      {
        path: 'PermissionConfiguration',
        component: () => import('@/views/managementAccountant/ncv65/budgetSystem/PermissionConfiguration'),
        name: 'QmysPermissionConfiguration',
        meta: { title: '权限配置' },
      },
      {
        path: 'DataIntegration',
        component: () => import('@/views/managementAccountant/ncv65/budgetSystem/DataIntegration'),
        name: 'QmysDataIntegration',
        meta: { title: '数据集成' },
      },
      {
        path: 'SystemMonitor',
        component: () => import('@/views/managementAccountant/ncv65/budgetSystem/SystemMonitor'),
        name: 'QmysSystemMonitor',
        meta: { title: '系统监控' },
      },
      {
        path: 'AuditTrail',
        component: () => import('@/views/managementAccountant/ncv65/budgetSystem/AuditTrail'),
        name: 'QmysAuditTrail',
        meta: { title: '审计跟踪' },
      },
      {
        path: 'BackupRestore',
        component: () => import('@/views/managementAccountant/ncv65/budgetSystem/BackupRestore'),
        name: 'QmysBackupRestore',
        meta: { title: '备份恢复' },
      },
    ],
  },
  // 现金管理路由配置 (xjgl)
  {
    path: '/xjgl',
    name: 'Xjgl',
    component: Layout,
    meta: {
      title: '现金管理',
      icon: 'money-cny-box-line',
    },
    children: [
      {
        path: 'basicConfigBusinessSystemRegister',
        name: 'BasicConfigBusinessSystemRegister',
        component: () => import('@/views/globalTreasurer/treasuryCommon/basicConfig/BusinessSystemRegister'),
        meta: {
          title: '业务系统注册',
          icon: 'settings-line',
        },
      },
      {
        path: 'basicConfigDataMappingConfig',
        name: 'BasicConfigDataMappingConfig',
        component: () => import('@/views/globalTreasurer/treasuryCommon/basicConfig/DataMappingConfig'),
        meta: {
          title: '数据映射配置',
          icon: 'database-line',
        },
      },
      {
        path: 'basicConfigETicketAccountConfig',
        name: 'BasicConfigETicketAccountConfig',
        component: () => import('@/views/globalTreasurer/treasuryCommon/basicConfig/ETicketAccountConfig'),
        meta: {
          title: '电票账户配置',
          icon: 'bank-card-line',
        },
      },
    ],
  },

	{
		path: '*',
		redirect: '/404',
		meta: {
			hidden: true,
		},
	},
	// 国资穿透监管驾驶舱
	{
		path: '/risk/home/stateAssetsDashboard',
		name: 'StateAssetsDashboard',
		component: Layout,
		meta: {
			title: '国资穿透驾驶舱',
			icon: 'dashboard-line',
			hidden: false,
		},
		children: [
			{
				path: 'index',
				name: 'StateAssetsDashboardIndex',
				component: () => import('@/views/risk/home/stateAssetsDashboard/index'),
				meta: {
					title: '国资穿透驾驶舱',
					icon: 'dashboard-line',
				},
			},
		],
	},
	// 国资穿透监管大屏（全屏，无导航栏）
	{
		path: '/risk/home/stateAssetsScreen',
		name: 'StateAssetsScreen',
		component: () => import('@/views/risk/home/stateAssetsScreen/index'),
		meta: {
			title: '国资穿透大屏',
			hidden: true,
			noLayout: true,
		},
	},
]

const router = createRouter()

function fatteningRoutes(routes) {
	return routes.flatMap((route) => {
		return route.children ? fatteningRoutes(route.children) : route
	})
}

export function resetRouter(routes = constantRoutes) {
	routes.map((route) => {
		if (route.children) {
			route.children = fatteningRoutes(route.children)
		}
	})
	router.matcher = createRouter(routes).matcher
}

function createRouter(routes = constantRoutes) {
	return new VueRouter({
		base: publicPath,
		mode: routerMode,
		scrollBehavior: () => ({
			y: 0,
		}),
		routes: routes,
	})
}

const originalPush = VueRouter.prototype.push
VueRouter.prototype.push = function push(location, onResolve, onReject) {
	if (onResolve || onReject)
		return originalPush.call(this, location, onResolve, onReject)
	return originalPush.call(this, location).catch((err) => err)
}

const originalReplace = VueRouter.prototype.replace
VueRouter.prototype.replace = function replace(location, onResolve, onReject) {
	if (onResolve || onReject)
		return originalReplace.call(this, location, onResolve, onReject)
	return originalReplace.call(this, location).catch((err) => err)
}

export default router
