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
      @click="$refs.componeysRef.show(1, null, item.name)"
      :disabled="item.isEdit == 0"
    >
      选择
    </el-button>
    <CompanyTreeModel ref="componeysRef" @selected="handleExecutorSelected" />
  </el-form-item>
</template>

<script>
  import CompanyTreeModel from '@/components/CompanyTreeModal'
  export default {
    name: 'CselectConmpanys',
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
        this.form[this.item.attachedField] =
          node.length > 0 && node.map((item) => item.label).join(',')
        this.form[this.item.field] =
          node.length > 0 && node.map((item) => item.id).join(',')
      },
    },
  }
</script>
