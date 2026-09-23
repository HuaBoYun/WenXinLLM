import { resolve } from 'path'
import { hasAccess } from '@/utils/hasAccess'
import { isExternal } from '@/utils/validate'
import { recordRoute } from '@/config'

/**
 * @description all模式渲染后端返回路由,支持包含views路径的所有页面
 * @param asyncRoutes
 * @returns {*}
 */
export function convertRouterOld(asyncRoutes) {
  return asyncRoutes.map((route) => {
    if (route.component) {
      if (route.component === 'Layout') {
        route.component = (resolve) => require(['@/vab/layouts'], resolve)
      } else {
        const index = route.component.indexOf('views')
        const path =
          index > 0 ? route.component.slice(index) : `views/${route.component}`
        route.component = (resolve) => require([`@/${path}`], resolve)
      }
    }
    if (route.children && route.children.length)
      route.children = convertRouter(route.children)
    if (route.children && route.children.length === 0) delete route.children
    return route
  })
}

export function convertRouter(asyncRoutes) {
  return asyncRoutes.map((route) => {
    if (route.component || route.type === 0) {
      if (route.component === 'Layout' || route.type === 0) {
        route.component = (resolve) => require(['@/vab/layouts'], resolve)
        route.meta = { title: route.name, icon: route.icon, rightId: route.id ? String(route.id) : undefined }
        if (route.children && route.children.length === 1) {
          route.meta.breadcrumbHidden = true
          route.children[0].noColumn = true
        }
      } else {
        const index = route.component.indexOf('views')
        route.meta = {
          title: route.name,
          icon: route.icon,
          noColumn: route.noColumn,
          hidden: route.hidden,
          rightId: route.id ? String(route.id) : undefined
        }
        const path =
          index > 0 ? route.component.slice(index) : `views/${route.component}`
        route.component = (resolve) => require([`@/${path}`], resolve)
      }
    }
    if (route.children && route.children.length)
      route.children = convertRouter(route.children)
    if (route.children && route.children.length === 0) delete route.children
    return route
  })
}

export function doSetAuthKeys(asyncRoutes, authKeys) {
  if (!authKeys) {
    authKeys = []
  }
  asyncRoutes.map((route) => {
    if (route.type === 2) {
      authKeys.push(route.perms)
    }
    if (route.children && route.children.length)
      route.children = doSetAuthKeys(route.children, authKeys)
  })
  return authKeys
}

/**
 * @description 根据roles数组拦截路由
 * @param routes 路由
 * @param rolesControl 是否进行权限控制
 * @param baseUrl 基础路由
 * @returns {[]}
 */
export function filterRoutes(routes, rolesControl, baseUrl = '/') {
  return routes
    .filter((route) =>
      rolesControl && route.meta && route.meta.roles
        ? hasAccess(route.meta.roles)
        : true
    )
    .map((route) => {
      route = { ...route }
      route.path =
        route.path !== '*' && !isExternal(route.path)
          ? resolve(baseUrl, route.path)
          : route.path
      if (route.children) {
        route.children = filterRoutes(route.children, rolesControl, route.path)
        route.childrenPathList = route.children.flatMap(
          (_) => _.childrenPathList
        )
        if (!route.redirect)
          route.redirect = route.children[0].redirect
            ? route.children[0].redirect
            : route.children[0].path
      } else route.childrenPathList = [route.path]
      return route
    })
}

/**
 * 根据当前route获取激活菜单
 * @param route 当前路由
 * @param isTabsBar 是否是标签
 * @returns {string|*}
 */
export function handleActivePath(route, isTabsBar = false) {
  const { meta, path, fullPath } = route
  const rawPath = route.matched
    ? route.matched[route.matched.length - 1].path
    : path
  if (isTabsBar) return meta.dynamicNewTab ? fullPath : rawPath
  if (meta.activeMenu) return meta.activeMenu
  return fullPath ? fullPath : rawPath
}

/**
 * 获取当前跳转登录页的Route
 * @param currentPath 当前页面地址
 */
export function toLoginRoute(currentPath) {
  if (recordRoute && currentPath !== '/')
    return {
      path: '/login',
      // path: '/404',
      query: { redirect: currentPath },
      replace: true,
    }
  else return { path: '/login', replace: true }
  // else return { path: '/404', replace: true }
}
