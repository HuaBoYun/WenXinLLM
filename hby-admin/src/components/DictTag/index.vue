<template>
  <el-tag v-if="label" :type="tagType" size="small">
    {{ label }}
  </el-tag>
  <span v-else>{{ value }}</span>
</template>

<script>
export default {
  name: 'DictTag',
  props: {
    options: {
      type: Array,
      default: () => []
    },
    value: {
      type: [String, Number],
      default: ''
    }
  },
  computed: {
    label() {
      if (!this.value && this.value !== 0) return ''
      const option = this.options.find(item => item.value === this.value)
      return option ? option.label : ''
    },
    tagType() {
      // 根据值返回不同的标签类型
      const typeMap = {
        'NORMAL': 'success',
        'SUCCESS': 'success',
        'ACTIVE': 'success',
        'ENABLED': 'success',
        'WARNING': 'warning',
        'PENDING': 'warning',
        'DRAFT': 'info',
        'INACTIVE': 'info',
        'CRITICAL': 'danger',
        'ERROR': 'danger',
        'FAILED': 'danger',
        'DISABLED': 'danger'
      }
      return typeMap[this.value] || ''
    }
  }
}
</script>
