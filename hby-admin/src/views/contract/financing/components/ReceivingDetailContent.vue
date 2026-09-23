<template>
  <el-row :gutter="15">
    <el-form ref="form" label-width="140px" :model="form">
      <el-col :span="12">
        <el-form-item label="收款合同">
          <span>{{ form.contractname }}</span>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="收款日期">
          <span>{{ form.collectionskdate }}</span>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="合同编号">
          <span>{{ form.contractno }}</span>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="付款单位">
          <span>{{ form.collectionorgname }}</span>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="对应收款项">
          <span>{{ form.nodecontent }}</span>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="对应收款金额">
          <span>{{ form.nodemoney }}</span>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="付款银行账号">
          <span>{{ form.bankaccnum }}</span>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="收款银行账号">
          <span>{{ form.bankaccount }}</span>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="发票号">
          <span>{{ form.invoiceno }}</span>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="开票金额">
          <span>{{ form.invoicemoney }}</span>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="开票单位">
          <span>{{ form.invoicekporg }}</span>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="开票日期">
          <span>{{ form.invoicedate }}</span>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="发票类型">
          <span>{{ form.invoicetype }}</span>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="发票状态">
          <span>{{ invoicestatusTextArr[form.invoicestatus + 1] }}</span>
        </el-form-item>
      </el-col>
    </el-form>
  </el-row>
</template>

<script>
  import {
    saveCollectionManagemen,
    viewCollectionManagemen,
  } from '@/api/contract/financing'
  import skhtOptions from './options/skht.vue'
  import dykxOptions from './options/dykx.vue'
  import yhzhOptions from './options/yhzh.vue'
  import fpxxOptions from './options/fpxx.vue'
  import yhzhskOptions from './options/yhzhsk.vue'
  export default {
    name: 'DraftEdit',
    components: {
      skhtOptions,
      dykxOptions,
      yhzhOptions,
      fpxxOptions,
      yhzhskOptions,
    },
    props: {
      formData: {
        type: Object,
        default: () => {},
      },
    },
    data() {
      return {
        invoicestatusTextArr: [
          '未开票',
          '已开票',
          '未收款',
          '已收款',
          '已退票',
        ],
        form: this.formData,
        title: '',
        dialogFormVisible: false,
        radio: '',
        show: 0,
      }
    },
    watch: {
      formData: {
        handler() {
          this.form = this.formData
        },
        immediate: true,
      },
    },
    created() {},
    mounted() {},
    methods: {},
  }
</script>
<style scoped>
  .formula .el-form-item--small.el-form-item {
    margin-bottom: 5px;
  }
</style>
