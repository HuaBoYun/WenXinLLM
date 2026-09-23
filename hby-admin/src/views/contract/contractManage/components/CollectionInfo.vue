<!--
 * @Date: 2022-04-21 10:21:17
 * @LastEditors: zengping.liu
 * @LastEditTime: 2022-04-22 20:00:36
 * @FilePath: /hb-admin/src/views/contract/contractManage/components/CollectionInfo.vue
-->
<template>
  <div>
    <el-table :data="list">
      <el-table-column
        v-if="type !== '收款'"
        align="center"
        label="标题"
        prop="paymenttitle"
      >
        <template #default="{ row }">
          <el-button type="text" @click="handleDetail(row)">
            {{ row.paymenttitle }}
          </el-button>
        </template>
      </el-table-column>
      <el-table-column
        v-for="item in options"
        :key="item.props"
        align="center"
        :label="item.label"
        :prop="item.props"
      />
      <!-- <el-table-column align="center" label="发票号" prop="invoiceno" />
      <el-table-column align="center" label="发票抬头" prop="invoiceheadtext" />
      <el-table-column align="center" label="到款金额" prop="invoicemoney" />
      <el-table-column
        align="center"
        label="到款日期"
        prop="collectionskdate"
      /> -->
    </el-table>
    <PaymentEdit ref="edit" />
  </div>
</template>
<script>
  import PaymentEdit from '@/views/contract/financing/components/PaymentEdit'
  export default {
    name: 'CollectionInfo',
    props: {
      list: {
        type: Array,
        default: () => [],
      },
      type: {
        type: String,
        default: '收款',
      },
    },
    computed: {
      options() {
        return this.type === '收款' ? this.payOption : this.cloOption
      },
    },
    data() {
      return {
        cloOption: [
          { label: '付款单位', props: 'budgetname' },
          { label: '发票号', props: 'invoiceno' },
          { label: '发票抬头', props: 'invoiceheadtext' },
          { label: '付款金额', props: 'paymenmoney' },
          { label: '申请日期', props: 'paymentlatedate' },
        ],
        payOption: [
          { label: '付款单位', props: 'collectionorgname' },
          { label: '发票号', props: 'invoiceno' },
          { label: '发票抬头', props: 'invoiceheadtext' },
          { label: '收款金额', props: 'invoicemoney' },
          { label: '收款日期', props: 'collectionskdate' },
        ],
      }
    },
    components: { PaymentEdit },
    methods: {
      /**
       * @description: 打开详情表单弹框
       * @param {*} row 选择的行数据
       * @return {*}
       */
      handleDetail(row) {
        this.$refs['edit'].showDetail(row)
      },
    },
  }
</script>
