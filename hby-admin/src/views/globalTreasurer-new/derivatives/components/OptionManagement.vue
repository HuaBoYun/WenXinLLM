<template>
  <div class="option-management-wrapper">
    <qqjy ref="optionRef" />
  </div>
</template>

<script>
import Qqjy from '../qqjy.vue'

export default {
  name: 'OptionManagement',
  components: {
    Qqjy
  },
  methods: {
    showCreateDialog() {
      if (this.$refs.optionRef && this.$refs.optionRef.handleAdd) {
        this.$refs.optionRef.handleAdd()
      }
    },
    showPricingDialog() {
      if (this.$refs.optionRef && this.$refs.optionRef.handlePricingModel) {
        this.$refs.optionRef.handlePricingModel()
      }
    },
    showGreeksDialog() {
      this.$message.info('显示希腊字母对话框')
    }
  }
}
</script>

<style scoped>
.option-management-wrapper {
  height: 100%;
}
</style>
