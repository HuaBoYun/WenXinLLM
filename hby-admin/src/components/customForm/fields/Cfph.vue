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
      @click="$refs.fpxx.show()"
      :disabled="item.isEdit == 0"
    >
      选择
    </el-button>
    <fpxx-options ref="fpxx" @selectedfpxx="handleExecutorSelected" />
  </el-form-item>
</template>

<script>
  import fpxxOptions from '@/views/contract/financing/components/options/fpxx.vue'
  export default {
    name: 'Cfph',
    components: { fpxxOptions },
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
        this.form[this.item.attachedField] = node.invoiceno
        this.form[this.item.field] = node.invoiceid
        this.form.invoicemoney = node.invoicemoney
        this.form.invoicekporg = node.invoicekporg
        this.form.invoicedate = node.invoicedate
        this.form.invoicetype = node.invoicetype
        this.form.invoicestatus = node.invoicestatus
      },
    },
  }
</script>
