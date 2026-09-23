<template>
  <el-form-item :prop="item.field" :label="item.name">
    <el-input
      v-model="form[item.attachedField]"
      clearable
      :placeholder="`请选择${item.name}`"
      :style="{ width: '75%' }"
      disabled
    />
    <el-button
      :style="{ marginLeft: '10px', height: '30px' }"
      type="primary"
      @click="$refs.department.show()"
      :disabled="item.isEdit == 0"
    >
      选择
    </el-button>
    <DepartmentSelect ref="department" @selected="handleDepartmentSelected" />
  </el-form-item>
</template>

<script>
  import DepartmentSelect from './components/DepartmentSelect.vue'
  export default {
    name: 'CselectDepartments',
    components: { DepartmentSelect },
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
      handleDepartmentSelected(node) {
        const names = node.map((res) => res.name)
        const ids = node.map((res) => res.id)

        this.form[this.item.attachedField] = names.toString()
        this.form[this.item.field] = ids.toString()
      },
    },
  }
</script>
