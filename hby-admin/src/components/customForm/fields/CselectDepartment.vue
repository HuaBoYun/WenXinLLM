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
    <DepartmentOptions ref="department" @selected="handleDepartmentSelected" />
  </el-form-item>
</template>

<script>
  import DepartmentOptions from '@/views/contract/project/components/options/department.vue'
  export default {
    name: 'CselectDepartment',
    components: { DepartmentOptions },
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
        this.form[this.item.attachedField] = node.name
        this.form[this.item.field] = node.id
      },
    },
  }
</script>
