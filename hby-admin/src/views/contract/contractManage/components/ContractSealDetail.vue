<!--
 * @Date: 2022-04-18 10:30:16
 * @LastEditors: zengping.liu
 * @LastEditTime: 2022-04-18 14:05:32
 * @FilePath: /hb-admin/src/views/contract/contractManage/components/ContractSealDetail.vue
-->
<template>
  <el-dialog
    v-if="isDialog"
    :close-on-click-modal="false"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
  >
    <ContractSealDetailContent :form-data="formData" :node="node" />
    <template #footer>
      <el-button @click="close">取 消</el-button>
    </template>
  </el-dialog>
  <ContractSealDetailContent v-else :form-data="formData" :node="node" />
</template>

<script>
  import {
    getContractTypes,
    getContractSealDetail,
  } from '@/api/contract/manage'
  import ContractSealDetailContent from './ContractSealDetailContent.vue'

  export default {
    name: 'ContractSealDetail',
    components: { ContractSealDetailContent },
    props: {
      isDialog: {
        type: Boolean,
        default: true,
      },
    },
    data() {
      return {
        node: {},
        formData: {
          flowId: 622324,
          budgetid: undefined,
          recordparent: undefined,
          counterpartcode: undefined,
          counterparthank: undefined,
          createtime: undefined,
          projectgoal: undefined,
          attList: [],
        },
        typeOptions: [],
        title: '',
        dialogFormVisible: false,
        disabled: false,
      }
    },
    created() {
      this.fetchTypes()
    },
    methods: {
      async fetchTypes() {
        const res = await getContractTypes()
        this.typeOptions = res.typeofList.map((item) => {
          return {
            label: item.typename,
            value: item.typeid,
          }
        })
      },
      //请求数据
      async fetchItem(row) {
        const res = await getContractSealDetail({
          budgetId: row.budgetid,
          flowId: row.flowid,
        })
        Object.keys(this.formData).forEach((key) => {
          this.formData[key] = res.data.tcpb[key]
        })
        this.formData.flowId = res.data.flowId
        this.node = res.data.tcu
        this.formData.attList = res.data.signingList || []
      },
      showDetail(row) {
        this.title = '查看'
        this.disabled = true
        this.fetchItem(row)
        this.dialogFormVisible = true
      },
      close() {
        this.formData = this.$options.data().formData
        this.dialogFormVisible = false
      },
    },
  }
</script>
<style scoped>
  .formula .el-form-item--small.el-form-item {
    margin-bottom: 5px;
  }
  .el-table {
    margin-top: 10px;
    margin-bottom: 18px;
  }
</style>
