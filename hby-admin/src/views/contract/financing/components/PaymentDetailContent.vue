<template>
  <el-row :gutter="15">
    <el-form ref="form" label-width="140px" :model="form">
      <el-col :span="24">
        <el-form-item label="标题" prop="rule">
          <span>{{ form.paymenttitle }}</span>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="经办人" prop="rule">
          <span>{{ form.realname }}</span>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="申请部门" prop="rule">
          <span>{{ form.orgname }}</span>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="合同名称" prop="rule">
          <span>{{ form.contractname }}</span>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="申请日期" prop="rule">
          <span>{{ form.applydate }}</span>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="合同编号">
          <span>{{ form.contractno }}</span>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="合同总金额">
          <span>{{ form.contractmoney }}</span>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="已付款金额">
          <span>{{ form.paymenmoney }}</span>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="未付款金额">
          <span>{{ form.noPaymoney }}</span>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="执行人">
          <span>{{ form.realname }}</span>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="合同相对方">
          <span>{{ form.counterpartno }}</span>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="付款银行账号">
          <span>{{ form.bankaccnum }}</span>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="收款银行账号">
          <span>{{ form.bankaccname }}</span>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="付款计划" prop="rule">
          <span>{{ form.nodecontent }}</span>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="付款金额">
          <span>{{ form.nodemoney }}</span>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="发票号" prop="rule">
          <span>{{ form.invoiceno }}</span>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="开票金额" prop="rule">
          <span>{{ form.paymenmoney }}</span>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="发票类型" prop="rule">
          <span>{{ form.invoicetype }}</span>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="开票日期" prop="rule">
          <span>{{ form.invoicedate }}</span>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="发票状态" prop="rule">
          <span>{{ invoicestatusTextArr[form.invoicestatus + 1] }}</span>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="款项类别" prop="rule">
          <span>{{ form.paymentrecord }}</span>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="付款方式" prop="rule">
          <span>{{ form.paymenttype }}</span>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="最晚付款日期" prop="rule">
          <span>{{ form.paymentlatedate }}</span>
        </el-form-item>
      </el-col>
      <el-col :span="24">
        <el-form-item label="备注">
          <span>{{ form.paymentmemo }}</span>
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
