<template>
  <advanced-features-home />
</template>

<script>
// 兼容层：数据库菜单配置指向此路径，实际组件在 ncv65/advancedFeatures/Home.vue
import AdvancedFeaturesHome from '@/views/managementAccountant/ncv65/advancedFeatures/Home.vue'

export default {
  name: 'AdvancedFeaturesHomeCompat',
  components: { AdvancedFeaturesHome }
}
</script>
