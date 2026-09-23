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
      @click="$refs.skht.show()"
      :disabled="item.isEdit == 0"
    >
      选择
    </el-button>
    <skht-options ref="skht" @selectedskht="handleExecutorSelected" />
  </el-form-item>
</template>

<script>
  import skhtOptions from '@/views/contract/financing/components/options/skht.vue'
  export default {
    name: 'Cskht',
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
        this.form['budgetid'] = node.budgetid
        this.form['collectionskdate'] = node.startdate
        this.form['contractno'] = node.contractno
        this.form['budgetname'] = node.budgetname
        if (node.bankid && node.bankaccount) {
          this.form['bankbankid'] = node.bankid
          this.form['bankaccount'] = node.bankaccount
        }
      },
    },
  }
</script>
