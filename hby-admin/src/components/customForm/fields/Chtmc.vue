<template>
  <el-form-item :prop="item.field" :label="item.name">
    <el-input
      v-model="form[item.attachedField]"
      clearable
      :placeholder="`请选择${item.name}`"
      :style="{ width: '256px' }"
      :disabled="false"
    />
    <el-button
      :style="{ marginLeft: '10px' }"
      type="primary"
      @click="$refs.executor.show()"
      :disabled="item.isEdit == 0"
    >
      选择
    </el-button>
    <skht-options ref="executor" @selectedskht="handleExecutorSelected" />
  </el-form-item>
</template>

<script>
  import skhtOptions from '@/views/contract/financing/components/options/skht.vue'
  export default {
    name: 'Chtmc',
    components: { skhtOptions },
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
        this.form[this.item.attachedField] = node.contractname
        this.form[this.item.field] = node.contractid
        this.form.budgetid = val.budgetid
        this.form.collectionskdate = val.startdate
        this.form.contractno = val.contractno
        this.form.budgetname = val.budgetname
        if (val.bankid && val.bankaccount) {
          this.form.bankbankid = val.bankid
          this.form.bankaccount = val.bankaccount
        }
      },
    },
  }
</script>
