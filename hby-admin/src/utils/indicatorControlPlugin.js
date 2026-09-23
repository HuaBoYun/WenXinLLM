/**
 * 指标控制全局拦截插件
 *
 * 功能：
 * 1. 在 Vue.prototype 上挂载 $indicatorCheck(operationType, formData) 方法
 * 2. 全局 mixin 同步包装 handleSave / handleDelete 等常见方法
 * 3. 按 rightId + operationType 缓存"是否需要控制"的结果，避免重复请求
 * 4. 页面跳转时自动传递 rightId —— 按钮跳转的衍生页面继承控制，菜单跳转不继承
 *
 * 核心原则：
 * - 方法包装在 created 中同步完成，不存在时序问题
 * - 控制检查在方法实际调用时执行（lazy check）
 * - 无控制配置的页面：首次调用产生一次 API 探测，结果缓存后后续零开销
 * - 失败时放行（fail-open），不阻塞正常业务
 * - 菜单页面（路由自带 rightId）使用自身配置，衍生页面（无 rightId）继承来源页面
 *
 * 使用方式：
 *   import IndicatorControlPlugin from '@/utils/indicatorControlPlugin'
 *   Vue.use(IndicatorControlPlugin)
 */

import { checkIndicatorControl } from '@/utils/indicatorControl'

/* ---------- 缓存层 ---------- */
const _cache = new Map()
const CACHE_TTL = 5 * 60 * 1000 // 5 分钟

function getCached(key) {
  const entry = _cache.get(key)
  if (entry && Date.now() - entry.ts < CACHE_TTL) return entry.value
  return undefined // 区分"缓存为 false"和"无缓存"
}
function setCache(key, value) {
  _cache.set(key, { value, ts: Date.now() })
}

/* ---------- rightId 继承链 ---------- */
const INHERITED_KEY = '_indicatorControl_inheritedRightId'

function getInheritedRightId() {
  try { return sessionStorage.getItem(INHERITED_KEY) } catch (e) { return null }
}
function setInheritedRightId(id) {
  try { if (id) sessionStorage.setItem(INHERITED_KEY, String(id)) } catch (e) { /* ignore */ }
}
function clearInheritedRightId() {
  try { sessionStorage.removeItem(INHERITED_KEY) } catch (e) { /* ignore */ }
}

/**
 * 获取当前生效的 rightId
 * 优先使用路由自身的 rightId（菜单页面），否则使用继承的 rightId（衍生页面）
 */
function resolveRightId(route) {
  const own = route && route.meta && route.meta.rightId
  if (own) return own
  return getInheritedRightId()
}

/* ---------- 自动拦截的方法映射 ---------- */
// value = 固定操作类型，'AUTO' 表示根据 form.id 自动判断 CREATE / UPDATE
const WRAP_MAP = {
  save: 'AUTO',
  handleSave: 'AUTO',
  handleSubmit: 'AUTO',
  doSave: 'AUTO',
  doSubmit: 'AUTO',
  submitForm: 'AUTO',
  add: 'CREATE',
  handleAdd: 'CREATE',
  handleAdds: 'CREATE',
  handleDelete: 'DELETE',
  doDelete: 'DELETE',
  batchDelete: 'DELETE'
}

/** 从组件实例上猜测表单数据 */
function guessFormData(vm, args) {
  // 1. 方法入参优先（有些方法直接接收 row / formData 作为参数）
  //    但要跳过 Event 对象 —— @click="save" 时 Vue 会自动传入 MouseEvent
  if (args && args.length > 0 && args[0] && typeof args[0] === 'object' && !Array.isArray(args[0])) {
    const first = args[0]
    const isEvent = (first instanceof Event)
      || (first.nativeEvent instanceof Event)
      || (typeof first.isTrusted === 'boolean' && typeof first.type === 'string' && typeof first.target === 'object')
    if (!isEvent) {
      return first
    }
  }
  // 2. 组件上常见的表单属性
  return vm.form || vm.formData || vm.editForm || vm.submitData || vm.ruleForm || {}
}

/**
 * 收集组件上所有可能的业务参数，合并为一个扁平对象
 * 供指标模型 ${参数名} 替换使用
 */
function collectParams(vm, args) {
  const formData = guessFormData(vm, args)
  // 浅拷贝，避免污染原始数据
  const params = { ...formData }

  // 补充 route 上的常用参数（如 id、rightId 等）
  if (vm.$route) {
    const q = vm.$route.query || {}
    const p = vm.$route.params || {}
    Object.keys(q).forEach(k => { if (params[k] === undefined) params[k] = q[k] })
    Object.keys(p).forEach(k => { if (params[k] === undefined) params[k] = p[k] })
  }

  return params
}

/** 根据表单数据判断是 CREATE 还是 UPDATE */
function guessOperationType(vm, hint) {
  if (hint !== 'AUTO') return hint
  const form = guessFormData(vm, null)
  return (form.id || form.ID || form.pkid || form.dataId) ? 'UPDATE' : 'CREATE'
}

/**
 * 执行指标控制检查（带缓存）
 * 如果该 rightId + operationType 已缓存为"无需控制"，直接放行
 */
async function doCheck(rightId, operationType, formData) {
  const cacheKey = `${rightId}_${operationType}`
  const cached = getCached(cacheKey)

  // 缓存命中且无需控制 → 直接放行
  if (cached !== undefined && cached === false) {
    console.log(`[IndicatorControl] 缓存命中(${cacheKey})，无控制配置，放行`)
    return { needControl: false, canProceed: true }
  }

  // 调用实际校验
  const result = await checkIndicatorControl(rightId, operationType, formData)

  // 如果本次结果是"无需控制"，缓存起来，后续同类操作不再请求
  if (!result.needControl) {
    setCache(cacheKey, false)
  }

  return result
}

/* ---------- Vue 插件 ---------- */
let _guardInstalled = false

export default {
  install(Vue) {
    /**
     * 全局方法：手动调用指标控制校验（供非标准方法名的页面使用）
     * @param {String} operationType - CREATE / UPDATE / DELETE
     * @param {Object} formData - 表单数据（可选，默认取 this.form）
     * @returns {Promise<{needControl, canProceed, controlType?}>}
     */
    Vue.prototype.$indicatorCheck = async function (operationType, formData) {
      const rightId = resolveRightId(this.$route)
      if (!rightId) return { needControl: false, canProceed: true }
      return doCheck(rightId, operationType, formData || guessFormData(this, null))
    }

    /**
     * 全局 mixin —— 同步包装，调用时检查
     * 对拥有 rightId（自身或继承）且定义了目标方法的组件生效
     */
    Vue.mixin({
      created() {
        // ---- 1. 安装路由守卫（仅一次） ----
        if (!_guardInstalled && this.$router) {
          _guardInstalled = true
          this.$router.beforeEach((to, from, next) => {
            const toRightId = to.meta && to.meta.rightId
            const fromRightId = from.meta && from.meta.rightId

            if (toRightId) {
              // 目标页面有自己的 rightId（菜单页面），清除继承链
              clearInheritedRightId()
              console.log(`[IndicatorControl] 导航到菜单页面, 使用自身 rightId=${toRightId}`)
            } else {
              // 目标页面无 rightId（衍生页面），继承来源页面的 rightId
              const sourceRightId = fromRightId || getInheritedRightId()
              if (sourceRightId) {
                setInheritedRightId(sourceRightId)
                console.log(`[IndicatorControl] 导航到衍生页面, 继承 rightId=${sourceRightId}`)
              }
            }
            next()
          })
          console.log('[IndicatorControl] 路由守卫已安装')
        }

        // ---- 2. 解析生效的 rightId（自身 > 继承） ----
        const rightId = resolveRightId(this.$route)
        if (!rightId) return

        // 防止重复包装
        if (this._indicatorControlWrapped) return

        // 只处理自身 $options.methods 中定义的目标方法（不误伤继承/混入的方法）
        const ownMethods = this.$options.methods || {}
        const needWrap = Object.keys(WRAP_MAP).filter(m => typeof ownMethods[m] === 'function')
        if (needWrap.length === 0) return

        this._indicatorControlWrapped = true
        const isInherited = !(this.$route && this.$route.meta && this.$route.meta.rightId)
        const componentName = this.$options.name || '(anonymous)'
        console.log(`[IndicatorControl] 包装组件: ${componentName}, rightId=${rightId}${isInherited ? '(继承)' : ''}, 方法=${needWrap.join(',')}`)

        // 同步包装 —— 不依赖任何异步操作，created 结束时方法已被替换
        needWrap.forEach(methodName => {
          const original = this[methodName]
          if (!original || original._isWrapped) return

          const hint = WRAP_MAP[methodName]
          const vm = this

          const wrapped = async function (...args) {
            // 每次调用时重新解析 rightId，确保拿到最新值
            const effectiveRightId = resolveRightId(vm.$route)
            if (!effectiveRightId) {
              return original.apply(vm, args)
            }
            const opType = guessOperationType(vm, hint)
            const params = collectParams(vm, args)
            console.log(`[IndicatorControl] 拦截 ${componentName}.${methodName}(), rightId=${effectiveRightId}, opType=${opType}, params=`, Object.keys(params))
            const result = await doCheck(effectiveRightId, opType, params)
            console.log(`[IndicatorControl] 校验结果:`, JSON.stringify(result))
            if (!result.canProceed) {
              console.warn(`[IndicatorControl] 操作被阻止: ${result.controlType}`)
              return
            }
            return original.apply(vm, args)
          }
          wrapped._isWrapped = true
          this[methodName] = wrapped
        })
      }
    })
  }
}

