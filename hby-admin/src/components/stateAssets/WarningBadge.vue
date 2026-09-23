<template>
  <el-tag :type="tagType" :size="size" :effect="effect">{{ levelText }}</el-tag>
</template>
<script>
/**
 * 预警等级徽标组件
 * 用于在列表、详情等页面展示预警等级标识
 * Props:
 *   level - 预警等级: HIGH / MEDIUM / LOW
 *   size  - 标签尺寸: medium / small / mini (默认 mini)
 *   effect - 标签主题: dark / light / plain (默认 light)
 */
export default {
  name: 'WarningBadge',
  props: {
    level: { type: String, default: '' },
    size: { type: String, default: 'mini' },
    effect: { type: String, default: 'light' },
  },
  computed: {
    tagType() {
      return { HIGH: 'danger', MEDIUM: 'warning', LOW: 'info' }[this.level] || 'info'
    },
    levelText() {
      return { HIGH: '一级红色', MEDIUM: '二级橙色', LOW: '三级黄色' }[this.level] || this.level || '未知'
    },
  },
}
</script>

