/**
 * @description 路由拦截状态管理，目前两种模式：all模式与intelligence模式，其中partialRoutes是菜单暂未使用
 */
import Vue from 'vue'
import {
  asyncRoutes,
  constantRoutes,
  backendRoutes,
  resetRouter,
} from '@/router'
import { getAuthListForUser } from '@/api/setting/auths'
import { getModuleList } from '@/api/setting/system'
import { convertRouter, filterRoutes } from '@/utils/routes'
import { authentication, rolesControl } from '@/config'
import { isArray } from '@/utils/validate'

const state = () => ({
  routes: [],
  activeMenu: '',
  authsKeys: [],
  levelKeys: [],
  moduleMenuCache: {},
  moduleAuthCache: {},
})
const getters = {
  routes: (state) => state.routes,
  activeMenu: (state) => state.activeMenu,
  authsKeys: (state) => state.authsKeys,
  levelKeys: (state) => state.levelKeys,
}
const mutations = {
  /**
   * @description 多模式设置路由
   * @param {*} state
   * @param {*} routes
   */
  setRoutes(state, routes) {
    state.routes = routes
  },
  /**
   * @description 修改Meta
   * @param {*} state
   * @param options
   */
  changeMenuMeta(state, options) {
    function handleRoutes(routes) {
      return routes.map((route) => {
        if (route.name === options.name) Object.assign(route.meta, options.meta)
        if (route.children && route.children.length)
          route.children = handleRoutes(route.children)
        return route
      })
    }
    state.routes = handleRoutes(state.routes)
  },
  /**
   * @description 修改 activeMenu
   * @param {*} state
   * @param value
   */
  changeActiveMenu(state, activeMenu) {
    state.activeMenu = activeMenu
  },
  setAuthsKeys(state, authsKeys) {
    state.authsKeys = authsKeys
  },
  setLevelKeys(state, levelKeys) {
    state.levelKeys = levelKeys
  },
  setModuleMenuCache(state, { moduleType, menu }) {
    Vue.set(state.moduleMenuCache, moduleType, menu)
  },
  setModuleAuthCache(state, { moduleType, auth }) {
    Vue.set(state.moduleAuthCache, moduleType, auth)
  },
  resetRoutesState(state) {
    state.routes = []
    state.activeMenu = ''
    state.authsKeys = []
    state.levelKeys = []
    state.moduleMenuCache = {}
    state.moduleAuthCache = {}
  },
}

const actions = {
  async getModuleMenu({ state, commit }, moduleType) {
    if (state.moduleMenuCache[moduleType])
      return JSON.parse(JSON.stringify(state.moduleMenuCache[moduleType]))
    const result = await getAuthListForUser({ moduleType })
    const menu = result.data.rightList || []
    commit('setModuleMenuCache', { moduleType, menu })
    return JSON.parse(JSON.stringify(menu))
  },
  async getModuleAuth({ state, commit }, moduleType) {
    if (state.moduleAuthCache[moduleType])
      return JSON.parse(JSON.stringify(state.moduleAuthCache[moduleType]))
    const result = await getAuthListForUser({
      moduleType,
      type: 2,
    })
    const auth = result.data.rightList || []
    commit('setModuleAuthCache', { moduleType, auth })
    return JSON.parse(JSON.stringify(auth))
  },
  async getAuthorizedModules({ dispatch }, forceRefresh = false) {
    if (!forceRefresh) {
      const cacheMenus = localStorage.getItem('allMenu')
      if (cacheMenus) {
        try {
          return JSON.parse(cacheMenus)
        } catch {
          localStorage.removeItem('allMenu')
        }
      }
    }

    const moduleListResult = await getModuleList({
      pageNumber: 1,
      pageSize: 20,
    })
    const moduleData = (moduleListResult && moduleListResult.data) || []

    // 优化：不立即加载所有模块菜单，只保存模块基本信息
    // 菜单将在用户进入特定模块时按需加载（懒加载策略）
    const authorizedModules = moduleData.map(item => ({
      ...item,
      menu: [],  // 空菜单，等待懒加载
      menuLoaded: false  // 标记：菜单未加载
    }))

    const allMenu = [
      {
        color: 'red',
        icon: 'apps-line',
        id: 1,
        projectName: '常用',
        menu: [],
        uniqueIdentification: 'wdyg',
      },
      ...authorizedModules,
    ]
    localStorage.setItem('allMenu', JSON.stringify(allMenu))
    return allMenu
  },
  /**
   * @description 多模式设置路由
   * @param {*} { commit }
   * @param mode
   * @returns
   */
  async setRoutes({ commit, dispatch }, mode = 'none') {
    await dispatch('getAuthorizedModules')

    // 默认前端路由
    let routes = [...asyncRoutes]
    // 设置游客路由关闭路由拦截(不需要可以删除)
    const control = mode === 'visit' ? false : rolesControl
    // 获取当前模块
    let model = localStorage.getItem('model')
    // 设置后端路由(不需要可以删除)
    if (authentication === 'all') {
      let list = []
      if (model != null && model !== 'home') {
        let [resultMenu, resultAuth] = await Promise.all([
          dispatch('getModuleMenu', model),
          dispatch('getModuleAuth', model),
        ])
        localStorage.setItem('renderMenu', JSON.stringify(resultMenu))
        list = resultMenu
        if (resultAuth) {
          let authKeys = ['SettingAuthmMenuAdd']
          for (let i = 0; i < resultAuth.length; i++) {
            authKeys.push(resultAuth[i].perms)
          }
          commit('setAuthsKeys', authKeys)
        }
        // 获取页面的密级
        if (resultMenu) {
          let levelKeys = []
          let allChildren = []
          resultMenu.forEach((res) => {
            allChildren.push(res.children)
          })
          const allChildrenData = allChildren.flat()
          allChildrenData.forEach((res) => {
            levelKeys.push({
              id: res.secrectLevelId,
              menuid: res.id,
              name: res.perms,
            })
          })

          commit('setLevelKeys', levelKeys)
        }
        // if (model === 'nkhg') {
        //   menu = await getRouterListForInternal()
        // }
        // if (model === 'xtsz') {
        //   menu = await getRouterListForSetting()
        // }
        // if (model === 'znsj') {
        //   menu = await getRouterListForAudit()
        // }
        // if (model === 'htgl') {
        //   menu = await getRouterListForContract()
        // }
        // if (model === 'znjk') {
        //   menu = await getRouterListForMonitor()
        // }
        // if (model === 'demo') {
        //   menu = await getRouterList()
        // }
        // if (model === 'fxgk') {
        //   menu = await getRouterListForRisk()
        // }
        // list = menu.data.list
      }
      if (list.length < 1) {
        localStorage.setItem('model', 'home')
        list = [
          {
            path: '/',
            name: 'Home',
            component: '@/views/index/index',
          },
        ]
      } else {
        list.push({
          path: '/',
          redirect: '/home/index',
          meta: { hidden: true },
        })
      }
      if (!isArray(list))
        Vue.prototype.$baseMessage(
          '路由格式返回有误！',
          'error',
          'vab-hey-message-error'
        )
      if (list[list.length - 1].path !== '*')
        list.push({ path: '*', redirect: '/404', meta: { hidden: true } })
      // let authKeys = doSetAuthKeys(JSON.parse(JSON.stringify(list)))
      // commit('setAuthsKeys', authKeys)

      //处理主题跳转
      for (let i = 0; i < list.length; i++) {
        if (list[i]['path'].includes('reportLink')) {
          list[i]['children'].forEach((res) => {
            localStorage.setItem(`${res.path}`, res.reportLinkUrl)
          })
        }
      }

      if (model === 'htgl') {
        for (let i = 0; i < list.length; i++) {
          if (list[i]['path'] === 'contractManage') {
            list[i]['children'].push({
              path: 'chooseType',
              name: '合同类型',
              component: '@/views/contract/contractManage/chooseType',
              hidden: true,
            })
            list[i]['children'].push({
              path: 'createEdit',
              name: '合同编辑',
              component: '@/views/contract/contractManage/createEdit',
              hidden: true,
            })
            list[i]['children'].push({
              path: 'fileComparison',
              name: '文件比对',
              component: '@/views/contract/contractManage/contractComparison',
              hidden: true,
            })
            list[i]['children'].push({
              path: 'contractComparison',
              name: '合同比对',
              component: '@/views/contract/contractManage/contractComparison',
              hidden: true,
            })
          }
        }
      }
      if (model === 'znsj') {
        for (let i = 0; i < list.length; i++) {
          if (list[i]['path'] === 'auditSjfx') {
            list[i]['children'].push({
              path: 'subject',
              name: '科目表',
              hidden: true,
              component: '@/views/workbench/companyData/sub/subject',
            })
            list[i]['children'].push({
              path: 'statement',
              name: '报表数据',
              hidden: true,
              component: '@/views/workbench/companyData/statement',
            })
            list[i]['children'].push({
              path: 'balance',
              name: '余额表',
              hidden: true,
              component: '@/views/workbench/companyData/sub/balance',
            })
            list[i]['children'].push({
              path: 'accountCate',
              name: '总分类账',
              hidden: true,
              component: '@/views/workbench/companyData/sub/accountCate',
            })
            list[i]['children'].push({
              path: 'accountDetail',
              name: '明细账',
              hidden: true,
              component: '@/views/workbench/companyData/sub/accountDetail',
            })
            list[i]['children'].push({
              path: 'accountDiary',
              name: '日记账',
              hidden: true,
              component: '@/views/workbench/companyData/sub/accountDiary',
            })
            list[i]['children'].push({
              path: 'accountAssist',
              name: '辅助账',
              hidden: true,
              component: '@/views/workbench/companyData/sub/accountAssist',
            })
            list[i]['children'].push({
              path: 'voucherLib',
              name: '凭证库',
              hidden: true,
              component: '@/views/workbench/companyData/sub/voucherLib',
            })
            list[i]['children'].push({
              path: 'businessData',
              name: '业务数据',
              hidden: true,
              component: '@/views/workbench/companyData/sub/businessData',
            })
          }
        }
      }
      if (model === 'yqns') {
        for (let i = 0; i < list.length; i++) {
          console.log(list[i])
          if (list[i]['path'] === 'XMPY') {
            list[i]['children'].push({
              path: 'sorceReview',
              name: '评分预览',
              hidden: true,
              component: '@/views/oilAudit/xmpy/sjgzzlpg/edit',
            })
          }
        }
      }
      if (model === 'fxgk') {
        for (let i = 0; i < list.length; i++) {
          if (list[i]['path'] === 'riskfill') {
            list[i]['children'].push({
              path: 'riskvalueList',
              name: '评估列表',
              hidden: true,
              component: '@/views/risk/riskvalue/valueList',
            })
          }
        }
      }
      if (model === 'cwsc') {
        for (let i = 0; i < list.length; i++) {
          if (list[i]['path'] === 'jcpz') {
            list[i]['children'].push({
              path: 'cjpz',
              name: '财务数据',
              hidden: true,
              component: '@/views/cwsc/jcpz/sqlDetail',
            })
            list[i]['children'].push({
              path: 'ywsj',
              name: '业务数据',
              hidden: true,
              component: '@/views/cwsc/jcpz/ywsj',
            })
          }
        }
      }

      // 成本管理模块路由配置 - 支持多个模块
      if (model === 'cbgl' || model === 'znfx') {
        for (let i = 0; i < list.length; i++) {
          // 成本中心路由配置
          if (
            list[i]['path'] === 'costCenter' ||
            list[i]['path'] === 'management/costCenter'
          ) {
            list[i]['children'].push({
              path: 'centerSetup',
              name: 'CostCenterSetup',
              hidden: true,
              component:
                '@/views/financialSharing/management/costCenter/centerSetup/index',
            })
            list[i]['children'].push({
              path: 'costCollection',
              name: 'CostCollection',
              hidden: true,
              component:
                '@/views/financialSharing/management/costCenter/costCollection/index',
            })
            list[i]['children'].push({
              path: 'costAllocation',
              name: 'CostAllocation',
              hidden: true,
              component:
                '@/views/financialSharing/management/costCenter/costAllocation/index',
            })
            list[i]['children'].push({
              path: 'costBudget',
              name: 'CostBudget',
              hidden: true,
              component:
                '@/views/financialSharing/management/costCenter/costBudget/index',
            })
            list[i]['children'].push({
              path: 'costControl',
              name: 'CostControl',
              hidden: true,
              component:
                '@/views/financialSharing/management/costCenter/costControl/index',
            })
            list[i]['children'].push({
              path: 'costAnalysis',
              name: 'CostAnalysis',
              hidden: true,
              component:
                '@/views/financialSharing/management/costCenter/costAnalysis/index',
            })
          }

          // 产品成本路由配置
          if (
            list[i]['path'] === 'productCost' ||
            list[i]['path'] === 'management/productCost'
          ) {
            list[i]['children'].push({
              path: 'productInfo',
              name: 'ProductInfo',
              hidden: true,
              component:
                '@/views/financialSharing/management/productCost/productInfo/index',
            })
            list[i]['children'].push({
              path: 'costAccounting',
              name: 'CostAccounting',
              hidden: true,
              component:
                '@/views/financialSharing/management/productCost/costAccounting/index',
            })
            list[i]['children'].push({
              path: 'costAnalysis',
              name: 'CostAnalysis',
              hidden: true,
              component:
                '@/views/financialSharing/management/productCost/costAnalysis/index',
            })
            list[i]['children'].push({
              path: 'costControl',
              name: 'CostControl',
              hidden: true,
              component:
                '@/views/financialSharing/management/productCost/costControl/index',
            })
            list[i]['children'].push({
              path: 'bomManagement',
              name: 'BomManagement',
              hidden: true,
              component:
                '@/views/financialSharing/management/productCost/bomManagement/index',
            })
            list[i]['children'].push({
              path: 'costReport',
              name: 'CostReport',
              hidden: true,
              component:
                '@/views/financialSharing/management/productCost/costReport/index',
            })
          }

          // 成本估算路由配置
          if (
            list[i]['path'] === 'costEstimation' ||
            list[i]['path'] === 'management/costEstimation'
          ) {
            list[i]['children'].push({
              path: 'schemeManagement',
              name: 'CostEstimationSchemeManagement',
              hidden: true,
              component:
                '@/views/financialSharing/management/costEstimation/schemeManagement/index',
            })
            list[i]['children'].push({
              path: 'costSimulation',
              name: 'CostEstimationCostSimulation',
              hidden: true,
              component:
                '@/views/financialSharing/management/costEstimation/costSimulation/index',
            })
            list[i]['children'].push({
              path: 'varianceAnalysis',
              name: 'CostEstimationVarianceAnalysis',
              hidden: true,
              component:
                '@/views/financialSharing/management/costEstimation/varianceAnalysis/index',
            })
            list[i]['children'].push({
              path: 'budgetPreparation',
              name: 'CostEstimationBudgetPreparation',
              hidden: true,
              component:
                '@/views/financialSharing/management/costEstimation/budgetPreparation/index',
            })
            list[i]['children'].push({
              path: 'costModel',
              name: 'CostEstimationCostModel',
              hidden: true,
              component:
                '@/views/financialSharing/management/costEstimation/costModel/index',
            })
            list[i]['children'].push({
              path: 'estimationReport',
              name: 'CostEstimationEstimationReport',
              hidden: true,
              component:
                '@/views/financialSharing/management/costEstimation/estimationReport/index',
            })
          }

          // 内部结算路由配置
          if (
            list[i]['path'] === 'internalSettlement' ||
            list[i]['path'] === 'management/internalSettlement'
          ) {
            list[i]['children'].push({
              path: 'internalTransaction',
              name: 'InternalTransaction',
              hidden: true,
              component:
                '@/views/financialSharing/management/internalSettlement/internalTransaction/index',
            })
            list[i]['children'].push({
              path: 'transferPricing',
              name: 'TransferPricing',
              hidden: true,
              component:
                '@/views/financialSharing/management/internalSettlement/transferPricing/index',
            })
            list[i]['children'].push({
              path: 'profitCenter',
              name: 'ProfitCenter',
              hidden: true,
              component:
                '@/views/financialSharing/management/internalSettlement/profitCenter/index',
            })
            list[i]['children'].push({
              path: 'settlementProcess',
              name: 'SettlementProcess',
              hidden: true,
              component:
                '@/views/financialSharing/management/internalSettlement/settlementProcess/index',
            })
            list[i]['children'].push({
              path: 'fundManagement',
              name: 'FundManagement',
              hidden: true,
              component:
                '@/views/financialSharing/management/internalSettlement/fundManagement/index',
            })
            list[i]['children'].push({
              path: 'settlementAnalysis',
              name: 'SettlementAnalysis',
              hidden: true,
              component:
                '@/views/financialSharing/management/internalSettlement/settlementAnalysis/index',
            })
          }
        }
      }

      routes = convertRouter(list)
    }
    // 根据权限和rolesControl过滤路由
    const accessRoutes = filterRoutes(
      [...constantRoutes, ...routes, ...backendRoutes],
      control
    )

    // 设置菜单所需路由
    commit('setRoutes', JSON.parse(JSON.stringify(accessRoutes)))
    // 根据可访问路由重置Vue Router
    await resetRouter(accessRoutes)
  },
  /**
   * @description 修改Route Meta
   * @param {*} { commit }
   * @param options
   */
  changeMenuMeta({ commit }, options = {}) {
    commit('changeMenuMeta', options)
  },
  /**
   * @description 修改 activeMenu
   * @param {*} { commit }
   * @param value
   */
  changeActiveMenu({ commit }, activeMenu) {
    commit('changeActiveMenu', activeMenu)
  },
}
export default { state, getters, mutations, actions }
