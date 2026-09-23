<template>
  <el-form-item :prop="item.field" :label="item.name">
    <el-radio-group
      v-model="form[item.field]"
      :disabled="item.isEdit == 0"
      @change="handleSelectChange"
    >
      <el-radio
        v-for="option in JSON.parse(item.extJson)"
        :key="option.value"
        :label="option.label"
      >
        {{ option.value }}
      </el-radio>
    </el-radio-group>
  </el-form-item>
</template>

<script>
  export default {
    name: 'Cradio',
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
