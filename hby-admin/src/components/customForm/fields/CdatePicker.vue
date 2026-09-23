<template>
  <el-form-item :prop="item.field" :label="item.name">
    <el-date-picker
      v-model="form[item.field]"
      type="date"
      clearable
      :placeholder="`请选择${item.name}`"
      :style="{ width: '100%' }"
      :disabled="item.isEdit == 0"
    />
  </el-form-item>
</template>

<script>
  export default {
    name: 'CdatePicker',
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
