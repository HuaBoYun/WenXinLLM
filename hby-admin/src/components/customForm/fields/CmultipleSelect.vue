<template>
  <el-form-item :prop="item.field" :label="item.name">
    <el-select
      v-model="form[item.field]"
      :placeholder="`请选择${item.name}`"
      :style="{ width: '100%' }"
      :disabled="item.isEdit == 0"
      @change="handleSelectChange"
      multiple
    >
      <el-option
        v-for="option in JSON.parse(item.extJson)"
        :key="option.value"
        :value="option.value"
        :label="option.label"
      />
    </el-select>
  </el-form-item>
</template>

<script>
  export default {
    name: 'CmultipleSelect',
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
    methods: {
      handleSelectChange(val) {
        this.$emit('cchange', val, this.item.field)
      },
    },
  }
</script>
