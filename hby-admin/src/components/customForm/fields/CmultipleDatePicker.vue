<template>
  <el-form-item :prop="item.field" :label="item.name">
    <el-date-picker
      v-model="form[item.field]"
      type="daterange"
      range-separator="至"
      start-placeholder="开始日期"
      end-placeholder="结束日期"
      :disabled="item.isEdit == 0"
      :placeholder="`请选择${item.name}`"
      :style="{ width: '100%' }"
    ></el-date-picker>
  </el-form-item>
</template>

<script>
  export default {
    name: 'CmultipleDatePicker',
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
