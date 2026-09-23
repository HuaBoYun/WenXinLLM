<template>
  <div class="swap-management-wrapper">
    <!-- 通过iframe或直接引用掉期交易页面组件 -->
    <dqjy ref="swapRef" />
  </div>
</template>

<script>
import Dqjy from '../dqjy.vue'

export default {
  name: 'SwapManagement',
  components: {
    Dqjy
  },
  methods: {
    showCreateDialog() {
      if (this.$refs.swapRef && this.$refs.swapRef.handleAddInterestRateSwap) {
        this.$refs.swapRef.handleAddInterestRateSwap()
      }
    },
    showCreateCurrencyDialog() {
      if (this.$refs.swapRef && this.$refs.swapRef.handleAddCurrencySwap) {
        this.$refs.swapRef.handleAddCurrencySwap()
      }
    },
    showCashflowDialog() {
      this.$message.info('显示现金流对话框')
    }
  }
}
</script>

<style scoped>
.swap-management-wrapper {
  height: 100%;
}
</style>
