/*
 * @Date: 2022-01-21 09:06:32
 * @LastEditors: zengping.liu
 * @LastEditTime: 2022-03-23 09:48:59
 * @FilePath: /hb-admin/src/vab/plugins/permissions.js
 */
/**
 * @description 路由守卫，目前两种模式：all模式与intelligence模式
 */
import router from '@/router'
import store from '@/store'
import VabProgress from 'nprogress'
import 'nprogress/nprogress.css'
import getPageTitle from '@/utils/pageTitle'
import { toLoginRoute } from '@/utils/routes'
import {
  authentication,
  loginInterception,
  routesWhiteList,
  supportVisit,
} from '@/config'
import { getTokenByUsername } from '@/oapi/user'

VabProgress.configure({
  easing: 'ease',
  speed: 500,
  trickleSpeed: 200,
  showSpinner: false,
})

router.beforeEach(async (to, from, next) => {

  // 处理hash前的URL参数（兼容hash模式下的参数位置问题）
  const urlParams = new URLSearchParams(window.location.search)
  const tokenFromUrl = urlParams.get('token')

  // 处理地址栏参数 - token 登录
  if (to.query.token) {
    await store.dispatch('user/tokenLogin', to.query.token)
  } else if (tokenFromUrl) {
    // 如果hash后没有token参数，尝试从hash前获取
    await store.dispatch('user/tokenLogin', tokenFromUrl)
  }
  // 处理地址栏参数 - model 模块
  if (to.query.model) {
    localStorage.setItem("model", to.query.model)
  }
  // 处理地址栏参数 - ticket oa系统跳转
  if (to.query.ticket) {
    await store.dispatch('user/loginByTicket', to.query.ticket)
  }
  // 处理地址栏参数 - v5ticket oa系统指定页面跳转
  if (to.query.v5ticket) {
    await store.dispatch('user/loginByV5Ticket', to.query.v5ticket)
  }
  const hasTokens = store.getters['user/token']

  if (to.query.username) {
    // 使用code获取token
    const aa = await getTokenByUsername({ userName: to.query.username })
    if (aa.code === 1) {
      await store.dispatch('user/tokenLogin', aa.data.token)
      // await store.dispatch('user/getUserInfo', { token: aa.data.token })
      if (to.query.type == 'htgl') {
        window.location.href = '/#/htgl'
        next({ path: '/htgl' })
        setTimeout(() => {
          store.dispatch('user/tokenLogin', '')
          next({ path: '/login' })
        }, 1200000);
        return
      } else if (to.query.type == 'fwgl') {
        window.location.href = '/#/fwgl'
        next({ path: '/fwgl' })
        setTimeout(() => {
          store.dispatch('user/tokenLogin', '')
          next({ path: '/login' })
        }, 1200000);
        return
      } else if (to.query.type == 'znsj') {
        window.location.href = '/#/znsj'
        next({ path: '/znsj' })
        setTimeout(() => {
          store.dispatch('user/tokenLogin', '')
          next({ path: '/login' })
        }, 1200000);
        return
      }
    }
  }
  // 有存储path，跳转到对应页面
  let jumpPath = localStorage.getItem("path")
  if (jumpPath) {
    localStorage.removeItem("path")
    next({ path: jumpPath })
    return
  }
  //判断登录页公司的背景图
  if (to.query.companyId) {
    localStorage.setItem('companyId', '')
    localStorage.setItem('companyId', to.query.companyId)
  } else {
    localStorage.setItem('companyId', '')
  }

  const { showProgressBar } = store.getters['settings/theme']
  if (showProgressBar) VabProgress.start()
  let hasToken = store.getters['user/token']

  if (!loginInterception) hasToken = true

  if (hasToken) {
    if (store.getters['routes/routes'].length) {
      // 禁止已登录用户返回登录页
      if (to.path === '/login') {
        next({ path: '/' })
        if (showProgressBar) VabProgress.done()
      } else {
        // [已注释] 离线版余额检查 - 阻塞导航导致页面卡死，暂时禁用
        // if (!to.path.includes('/recharge')) {
        //   try {
        //     const { checkFeeBalance } = await import('@/api/setting/fee')
        //     const balanceRes = await checkFeeBalance()
        //     const billingData = (balanceRes && balanceRes.data) || {}
        //     if (billingData.billingStatus === 'blocked') {
        //       const { MessageBox, Message } = await import('element-ui')
        //       try {
        //         const { value } = await MessageBox.prompt(billingData.msg || '余额不足，请输入充值密钥', '欠费提醒', {
        //           confirmButtonText: '充值',
        //           cancelButtonText: '取消',
        //           inputPlaceholder: '请输入充值密钥',
        //           type: 'error',
        //           showClose: false,
        //           closeOnClickModal: false,
        //           closeOnPressEscape: false,
        //         })
        //         if (value) {
        //           const { rechargeLicense } = await import('@/api/setting/fee')
        //           const res = await rechargeLicense({ licenseKey: value })
        //           if (res.code === 1 || res.code === 200) {
        //             Message.success('充值成功！')
        //             redirectToDBFromOA(to, () => next())
        //             return
        //           } else {
        //             Message.error(res.msg || '充值失败')
        //             window.location.hash = '#/infoSearch/recharge'
        //             return
        //           }
        //         }
        //       } catch (e) {
        //         window.location.hash = '#/infoSearch/recharge'
        //         return
        //       }
        //     } else if (billingData.billingStatus === 'warning') {
        //       const { Notification } = await import('element-ui')
        //       Notification.warning({ title: '余额预警', message: billingData.msg, duration: 5000 })
        //     }
        //   } catch (e) { /* 检查失败不阻断 */ }
        // }
        redirectToDBFromOA(to, () => next())
      }
    } else {
      try {
        if (loginInterception)
          await store.dispatch('user/getUserInfo', { token: hasToken })
        else await store.dispatch('user/setVirtualRoles')
        await store.dispatch('routes/setRoutes', authentication)

        // [已注释] 计费系统：登录后余额检查 - 阻塞导航导致页面卡死，暂时禁用
        // try {
        //   const { checkFeeBalance } = await import('@/api/setting/fee')
        //   const balanceRes = await checkFeeBalance()
        //   const billingData = (balanceRes && balanceRes.data) || {}
        //   if (billingData.billingStatus === 'blocked') {
        //     const { MessageBox, Message } = await import('element-ui')
        //     try {
        //       const { value } = await MessageBox.prompt(billingData.msg || '余额不足，请输入充值密钥', '欠费提醒', {
        //         confirmButtonText: '充值', cancelButtonText: '取消', inputPlaceholder: '请输入充值密钥',
        //         type: 'error', showClose: false, closeOnClickModal: false, closeOnPressEscape: false,
        //       })
        //       if (value) {
        //         const { rechargeLicense } = await import('@/api/setting/fee')
        //         const res = await rechargeLicense({ licenseKey: value })
        //         if (res.code === 1 || res.code === 200) { Message.success('充值成功！') }
        //         else { Message.error(res.msg || '充值失败'); window.location.hash = '#/infoSearch/recharge'; return }
        //       }
        //     } catch (e) { window.location.hash = '#/infoSearch/recharge'; return }
        //   } else if (billingData.billingStatus === 'warning') {
        //     const { Notification } = await import('element-ui')
        //     Notification.warning({ title: '余额预警', message: billingData.msg, duration: 5000 })
        //   }
        // } catch (e) {
        //   console.warn('余额检查跳过:', e)
        // }

        redirectToDBFromOA(to, () => next({ ...to, replace: true }))
      } catch (err) {
        console.error('vue-admin-beautiful错误拦截:', err)
        await store.dispatch('user/resetAll')
        next(toLoginRoute(to.path))
      }
    }
  } else {
    if (routesWhiteList.includes(to.path)) {
      // 设置游客路由(不需要可以删除)
      if (supportVisit && !store.getters['routes/routes'].length) {
        await store.dispatch('routes/setRoutes', 'visit')
        next({ ...to, replace: true })
      } else next()
    } else next(toLoginRoute(to.path))
  }
})


router.afterEach((to) => {
  document.title = getPageTitle(to.meta.title)
  if (VabProgress.status) VabProgress.done()
  // 计费SDK：记录当前页面路由和rightId
  window.__currentPageRoute = to.path || ''
  window.__currentPageRightId = (to.meta && to.meta.rightId) ? to.meta.rightId : ''

  // 计费SDK：采集完整页面身份（前端是唯一可靠来源）
  // 1. 页面名：当前路由的 meta.title
  window.__currentPageName = (to.meta && to.meta.title) ? to.meta.title : ''

  // 2. 模块名：优先通过路由前缀自动识别，解决跨模块导航时 localStorage.model 未更新的问题
  //    路由前缀 → 模块名映射表（当路由明确属于某模块时，自动修正模块信息）
  var routeModuleMap = [
    { prefix: '/stateAssets/', moduleName: '国资穿透' },
    { prefix: '/globalTreasurer', moduleName: '全球司库' },
    { prefix: '/contract/', moduleName: '合同管理' },
    { prefix: '/financialSharing/', moduleName: '财务共享' }
  ]

  var routeDetectedModule = ''
  var currentPath = to.path || ''
  for (var i = 0; i < routeModuleMap.length; i++) {
    if (currentPath.startsWith(routeModuleMap[i].prefix)) {
      routeDetectedModule = routeModuleMap[i].moduleName
      break
    }
  }

  // 如果路由前缀匹配到了模块，直接使用（最可靠）
  // 否则回退到 localStorage.model + allMenu 查找
  let resolvedModuleName = ''
  if (routeDetectedModule) {
    resolvedModuleName = routeDetectedModule
  } else {
    const model = localStorage.getItem('model') || ''
    try {
      const allMenuStr = localStorage.getItem('allMenu')
      if (allMenuStr && model) {
        const allMenu = JSON.parse(allMenuStr)
        const found = allMenu.find(function(m) { return m.uniqueIdentification === model })
        if (found && found.projectName) resolvedModuleName = found.projectName
      }
    } catch (e) { /* ignore */ }
  }
  window.__currentPageModuleName = resolvedModuleName

  // 3. 子模块名：取当前页面的上一级目录的 title
  //    三层: [模块Layout, 子模块Layout, 页面] → 取 matched[length-2] = 子模块
  //    两层: [父目录Layout, 页面]             → 取 matched[0] = 父目录
  let resolvedSubModuleName = ''
  if (to.matched && to.matched.length >= 2) {
    var parentIdx = to.matched.length >= 3 ? to.matched.length - 2 : 0
    var parent = to.matched[parentIdx]
    if (parent && parent.meta && parent.meta.title) {
      resolvedSubModuleName = parent.meta.title
    }
  }
  window.__currentSubModuleName = resolvedSubModuleName
})

/**
 *
 * @param {*} elseCB 非OA跳转走常规
 */
const redirectToDBFromOA = (to, elseCB) => {
  if (to.query.v5ticket) {
    /**
     * OA待办跳转后重定向
     * 根据是否有id判断
     *  1. 如果有id：我的待办
     *  2. 如果没有id: 我的发起
     */
    const wddbData = {
      processId: to.query.processId,
      flowId: to.query.flowId,
      thisStepId: to.query.thisStepId,
      v5ticket: to.query.v5ticket,
    }
    /* 根据设备宽度来判断是否是移动端 */
    const clientWidth = document.body.clientWidth
    if (clientWidth <= 768) store.dispatch('work/setProcessMobileAction', true)

    if (to.query.id) {
      wddbData.id = to.query.id
      store.dispatch('work/setWbbdDetailsAction', wddbData)
      store.dispatch('work/setWddbStatesAction', true)
      // next({ path: 'msg/wddb' })
      router.push({ name: 'wddb' })
    } else {
      store.dispatch('work/setWfqdDetailsAction', wddbData)
      store.dispatch('work/setWfqdStatesAction', true)
      // next({ path: 'msg/wfqd' })
      router.push({ name: 'wfqd' })
    }
  } else {
    elseCB && elseCB()
  }
}
