<template>
  <el-form-item :prop="item.field" :label="item.name">
    <el-input
      v-model="form[item.field]"
      clearable
      :placeholder="`请输入${item.name}`"
      :style="{ width: '100%' }"
      :disabled="item.isEdit == 0"
    />
  </el-form-item>
</template>

<script>
  export default {
    name: 'Cinput',
    props: ['item', 'form'],
    data() {
      return {}
    },
    watch: {
      deep: true,
      form(val) {
        this.$emit('input', this.form)
      },
    },
  }
</script>
