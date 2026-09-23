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
        component: '@/views/contract/home/index',
        meta: {
          title: '首页',
          icon: 'home-2-line',
          noClosable: true,
        },
      },
    ],
  },
  {
    path: '/system',
    name: 'System',
    component: 'Layout',
    meta: {
      title: '系统首页',
      icon: 'computer-line',
    },
    children: [
      {
        path: 'manage',
        name: 'Manage',
        component: '@/views/contract/system/manage',
        meta: {
          title: '合同管理首页',
          icon: 'archive-drawer-line',
        },
      },
      {
        path: 'executives',
        name: 'Executives',
        component: '@/views/contract/system/executives',
        meta: {
          title: '合同高管首页',
          icon: 'user-2-line',
        },
      },
      {
        path: 'finance',
        name: 'Finance',
        component: '@/views/contract/system/finance',
        meta: {
          title: '合同财务首页',
          icon: 'money-cny-circle-line',
        },
      },
    ],
  },
  {
    path: '/risk',
    name: 'OperateRisk',
    component: 'Layout',
    meta: {
      title: '运营风险',
      icon: 'alarm-warning-line',
    },
    children: [
      {
        path: 'delivery',
        name: 'Delivery',
        component: '@/views/contract/risk/delivery',
        meta: {
          title: '合同交付分析',
          icon: 'scan-2-line',
        },
      },
      {
        path: 'remittance',
        name: 'Remittance',
        component: '@/views/contract/risk/remittance',
        meta: {
          title: '合同回款分析',
          icon: 'refund-2-line',
        },
      },
    ],
  },
  {
    path: '/opposite',
    name: 'Opposite',
    component: 'Layout',
    meta: {
      title: '相对方',
      icon: 'device-line',
    },
    children: [
      {
        path: 'maintain',
        name: 'Maintain',
        component: '@/views/contract/opposite/maintain',
        meta: {
          title: '相对方维护',
          icon: 'repeat-2-line',
        },
      },
      {
        path: 'warning',
        name: 'Warning',
        component: '@/views/contract/opposite/warning',
        meta: {
          title: '相对方预警',
          icon: 'pulse-line',
        },
      },
      {
        path: 'blacklist',
        name: 'Blacklist',
        component: '@/views/contract/opposite/blacklist',
        meta: {
          title: '黑名单管理',
          icon: 'shield-user-line',
        },
      },
      {
        path: 'monitoring',
        name: 'Monitoring',
        component: '@/views/contract/opposite/monitoring',
        meta: {
          title: '相对方监控',
          icon: 'webcam-line',
        },
      },
    ],
  },
  {
    path: '/project',
    name: 'Project',
    component: 'Layout',
    meta: {
      title: '项目管理',
      icon: 'projector-line',
      hidden: true,
    },
    children: [
      {
        path: 'index',
        name: 'Index',
        component: '@/views/contract/project/index',
        meta: {
          title: '立项管理',
          icon: 'projector-line',
        },
      },
    ],
  },
  {
    path: '/contractManage',
    name: 'ContractManage',
    component: 'Layout',
    meta: {
      title: '合同管理',
      icon: 'archive-drawer-line',
    },
    children: [
      {
        path: 'category',
        name: 'contractManageCategory',
        component: '@/views/contract/contractManage/category',
        meta: {
          title: '合同类型',
          icon: 'align-justify',
        },
      },
      {
        path: 'template',
        name: 'Template',
        component: '@/views/contract/contractManage/template',
        meta: {
          title: '合同范本',
          icon: 'article-line',
        },
      },
      {
        path: 'create',
        name: 'Create',
        component: '@/views/contract/contractManage/create',
        meta: {
          title: '合同订立',
          icon: 'ball-pen-line',
        },
      },
      {
        path: 'seal',
        name: 'Seal',
        component: '@/views/contract/contractManage/seal',
        meta: {
          title: '合同用印',
          icon: 'dv-line',
        },
      },
      {
        path: 'change',
        name: 'Change',
        component: '@/views/contract/contractManage/change',
        meta: {
          title: '合同变更',
          icon: 'draft-line',
        },
      },
      {
        path: 'package',
        name: 'Package',
        component: '@/views/contract/contractManage/package',
        meta: {
          title: '合同归档',
          icon: 'archive-line',
        },
      },
    ],
  },
  {
    path: '/execute',
    name: 'Execute',
    component: 'Layout',
    meta: {
      title: '合同履行',
      icon: 'contacts-book-upload-line',
    },
    children: [
      {
        path: 'mine',
        name: 'Mine',
        component: '@/views/contract/execute/mine',
        meta: {
          title: '我的合同',
          icon: 'book-2-line',
        },
      },
      {
        path: 'track',
        name: 'Track',
        component: '@/views/contract/execute/track',
        meta: {
          title: '履行跟踪',
          icon: 'barricade-line',
        },
      },
      {
        path: 'practicable',
        name: 'Practicable',
        component: '@/views/contract/execute/practicable',
        meta: {
          title: '履行落实',
          icon: 'book-read-line',
        },
      },
    ],
  },
  {
    path: '/account',
    name: 'Account',
    component: 'Layout',
    meta: {
      title: '合同台账',
      icon: 'book-3-line',
    },
    children: [
      {
        path: 'index',
        name: 'Index',
        component: '@/views/contract/account/index',
        meta: {
          title: '合同台账',
          icon: 'book-3-line',
        },
      },
    ],
  },
  {
    path: '/financing',
    name: 'Financing',
    component: 'Layout',
    meta: {
      title: '财务管理',
      icon: 'exchange-cny-line',
    },
    children: [
      {
        path: 'receiving',
        name: 'Receiving',
        component: '@/views/contract/financing/receiving',
        meta: {
          title: '收款管理',
          icon: 'exchange-cny-line',
        },
      },
      {
        path: 'payment',
        name: 'Payment',
        component: '@/views/contract/financing/payment',
        meta: {
          title: '付款管理',
          icon: 'secure-payment-line',
        },
      },
      {
        path: 'bill',
        name: 'Bill',
        component: '@/views/contract/financing/bill',
        meta: {
          title: '发票管理',
          icon: 'bill-line',
        },
      },
      {
        path: 'bank',
        name: 'Bank',
        component: '@/views/contract/financing/bank',
        meta: {
          title: '银行账户',
          icon: 'barricade-line',
        },
      },
    ],
  },
  {
    path: '/legal',
    name: 'Legal',
    component: 'Layout',
    meta: {
      title: '法务管理',
      icon: 'auction-line',
    },
    children: [
      {
        path: 'dispute',
        name: 'Dispute',
        component: '@/views/contract/legal/dispute',
        meta: {
          title: '纠纷登记',
          icon: 'file-edit-line',
        },
      },
      {
        path: 'consult',
        name: 'Consult',
        component: '@/views/contract/legal/consult',
        meta: {
          title: '协商过程',
          icon: 'a-b',
        },
      },
      {
        path: 'lawsuit',
        name: 'Lawsuit',
        component: '@/views/contract/legal/lawsuit',
        meta: {
          title: '诉讼过程',
          icon: 'align-top',
        },
      },
      {
        path: 'arbitration',
        name: 'Arbitration',
        component: '@/views/contract/legal/arbitration',
        meta: {
          title: '仲裁过程',
          icon: 'book-read-line',
        },
      },
      {
        path: 'close',
        name: 'Close',
        component: '@/views/contract/legal/close',
        meta: {
          title: '纠纷结案',
          icon: 'mail-close-line',
        },
      },
      {
        path: 'preserve',
        name: 'Preserve',
        component: '@/views/contract/legal/preserve',
        meta: {
          title: '资产保全',
          icon: 'money-pound-box-line',
        },
      },
      {
        path: 'freeze',
        name: 'Freeze',
        component: '@/views/contract/legal/freeze',
        meta: {
          title: '账户冻结',
          icon: 'money-cny-box-line',
        },
      },
      {
        path: 'account',
        name: 'Account',
        component: '@/views/contract/legal/account',
        meta: {
          title: '法务台账',
          icon: 'book-3-line',
        },
      },
    ],
  },
  {
    path: '/riskAssessment',
    name: 'RiskAssessment',
    component: 'Layout',
    meta: {
      title: '风险评估',
      icon: 'shield-check-line',
    },
    children: [
      {
        path: 'index',
        name: 'RiskAssessmentIndex',
        component: '@/views/contract/riskAssessment/index',
        meta: {
          title: '风险评估管理',
          icon: 'shield-check-line',
        },
      },
    ],
  },
]

module.exports = [
  {
    url: '/routerContract/getListForContract',
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
