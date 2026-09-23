<template>
  <div class="forward-management-wrapper">
    <!-- 通过iframe或直接引用远期交易页面组件 -->
    <yqjy ref="forwardRef" />
  </div>
</template>

<script>
import Yqjy from '../yqjy.vue'

export default {
  name: 'ForwardManagement',
  components: {
    Yqjy
  },
  methods: {
    showCreateDialog() {
      if (this.$refs.forwardRef && this.$refs.forwardRef.handleAdd) {
        this.$refs.forwardRef.handleAdd()
      }
    },
    showRatesDialog() {
      this.$message.info('显示远期汇率对话框')
    },
    showDeliveryAlerts() {
      this.$message.info('显示交割提醒')
    }
  }
}
</script>

<style scoped>
.forward-management-wrapper {
  height: 100%;
}
</style>
