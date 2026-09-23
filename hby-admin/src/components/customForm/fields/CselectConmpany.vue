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
      @click="$refs.componeyRef.show()"
      :disabled="item.isEdit == 0"
    >
      选择
    </el-button>
    <CompanyTreeModel ref="componeyRef" @selected="handleExecutorSelected" />
  </el-form-item>
</template>

<script>
  import CompanyTreeModel from '@/components/CompanyTreeModel'
  export default {
    name: 'CselectConmpany',
    components: { CompanyTreeModel },
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
      handleExecutorSelected(node) {
        this.form[this.item.attachedField] = node.name
        this.form[this.item.field] = node.id
      },
    },
  }
</script>
