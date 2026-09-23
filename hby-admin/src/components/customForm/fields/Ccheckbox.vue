<template>
  <el-form-item :prop="item.field" :label="item.name">
    <el-checkbox-group
      v-model="form[item.field]"
      :disabled="item.isEdit == 0"
      @change="handleSelectChange"
    >
      <el-checkbox
        v-for="option in JSON.parse(item.extJson)"
        :key="option.value"
        :label="option.value"
      >
        {{ option.value }}
      </el-checkbox>
    </el-checkbox-group>
  </el-form-item>
</template>

<script>
  export default {
    name: 'Ccheckbox',
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
