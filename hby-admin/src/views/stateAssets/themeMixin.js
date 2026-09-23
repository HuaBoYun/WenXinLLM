/**
 * 国资监管模块 - 主题色适配 Mixin（共享）
 * 根据全局主题配置动态切换品牌色，替代硬编码色值
 *
 * 使用方式：
 *   import { investThemeMixin } from '../../themeMixin'  // 从 stateAssets/ 下的子模块引入
 *   export default { mixins: [investThemeMixin], ... }
 *
 * 提供的 computed：
 *   - themeVars: 绑定到组件根元素的 style 对象（CSS 变量）
 *   - ipPrimary: 主色（深色）
 *   - ipSecondary: 渐变辅色（中间色）
 *   - ipBright: 亮色（渐变终点）
 *   - ipAccent: 强调色
 *   - ipPrimaryRgb: 主色 RGB 值（用于 rgba）
 *   - ipLightBg: 浅色背景
 */

// 主题色映射表 —— 与 src/vab/styles/themes 保持一致
const THEME_MAP = {
  default: { primary: '#003A6C', secondary: '#0050A0', bright: '#1677FF', accent: '#FAAD14', rgb: '0,58,108', lightBg: '#EBF1FF' },
  white:   { primary: '#1890ff', secondary: '#399efd', bright: '#69c0ff', accent: '#FAAD14', rgb: '24,144,255', lightBg: '#e6f7ff' },
  ocean:   { primary: '#1890ff', secondary: '#399efd', bright: '#69c0ff', accent: '#FAAD14', rgb: '24,144,255', lightBg: '#e6f7ff' },
  green:   { primary: '#41b584', secondary: '#2d9a6e', bright: '#73d8a9', accent: '#FAAD14', rgb: '65,181,132', lightBg: '#e8f5f0' },
  red:     { primary: '#e50113', secondary: '#c70011', bright: '#ff4d4f', accent: '#FAAD14', rgb: '229,1,19', lightBg: '#fff1f0' },
}

export const investThemeMixin = {
  computed: {
    // 当前主题名
    _ipThemeName() {
      const settings = this.$store && this.$store.state && this.$store.state.settings
      const theme = settings && settings.theme
      return (theme && theme.themeName) || 'default'
    },
    // 当前主题色组
    _ipColors() {
      return THEME_MAP[this._ipThemeName] || THEME_MAP.default
    },
    // 主色（深色）
    ipPrimary() {
      return this._ipColors.primary
    },
    // 渐变辅色（中间色）
    ipSecondary() {
      return this._ipColors.secondary
    },
    // 亮色（渐变终点）
    ipBright() {
      return this._ipColors.bright
    },
    // 强调色
    ipAccent() {
      return this._ipColors.accent
    },
    // 主色 RGB（用于 rgba 场景）
    ipPrimaryRgb() {
      return this._ipColors.rgb
    },
    // 浅色背景
    ipLightBg() {
      return this._ipColors.lightBg
    },
    // CSS 变量对象 —— 绑定到组件根元素 :style="themeVars"
    themeVars() {
      const c = this._ipColors
      return {
        '--ip-primary': c.primary,
        '--ip-secondary': c.secondary,
        '--ip-bright': c.bright,
        '--ip-accent': c.accent,
        '--ip-primary-rgb': c.rgb,
        '--ip-light-bg': c.lightBg,
      }
    },
  },
  mounted() {
    this._applyThemeVarsToEl()
  },
  watch: {
    _ipThemeName() {
      this._applyThemeVarsToEl()
    },
  },
  methods: {
    // 直接在 DOM 元素上设置 CSS 变量（兜底方案，确保变量一定生效）
    _applyThemeVarsToEl() {
      const el = this.$el
      if (!el || !el.style) return
      const c = this._ipColors
      el.style.setProperty('--ip-primary', c.primary)
      el.style.setProperty('--ip-secondary', c.secondary)
      el.style.setProperty('--ip-bright', c.bright)
      el.style.setProperty('--ip-accent', c.accent)
      el.style.setProperty('--ip-primary-rgb', c.rgb)
      el.style.setProperty('--ip-light-bg', c.lightBg)
    },
  },
}
