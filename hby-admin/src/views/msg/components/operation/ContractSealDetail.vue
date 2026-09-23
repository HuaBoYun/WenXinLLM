<template>
  <el-dialog
    v-if="isDialog"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
    :close-on-click-modal="false"
  >
    <ContractSealDetailContent
      v-if="!isSendBack"
      :form-data="formData"
      :node="node"
    />
    <ContractSealDetailContentEdit
      v-else
      :form-data="formData"
      :node="node"
      ref="edit"
    />
    <template #footer>
      <el-button @click="close">取 消</el-button>
    </template>
  </el-dialog>
  <div v-else>
    <ContractSealDetailContent
      v-if="!isSendBack"
      :form-data="formData"
      :node="node"
    />
    <ContractSealDetailContentEdit
      v-else
      :form-data="formData"
      :node="node"
      ref="edit"
    />
  </div>
</template>

<script>
  import {
    getContractSealDetail,
    getContractTypes,
  } from '@/api/contract/manage'
  import ContractSealDetailContent from '@/views/contract/contractManage/components/ContractSealDetailContent.vue'
  import ContractSealDetailContentEdit from '@/views/contract/contractManage/components/ContractSealDetailContentEdit.vue'

  export default {
    name: 'ContractSealDetail',
    components: { ContractSealDetailContent, ContractSealDetailContentEdit },
    props: {
      isDialog: {
        type: Boolean,
        default: true,
      },
    },
    data() {
      return {
        isSendBack: false,
        node: {},
        formData: {
          flowId: 622324,
          budgetid: undefined,
          recordparent: undefined,
          counterpartcode: undefined,
          counterparthank: undefined,
          createtime: undefined,
          projectgoal: undefined,
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
      async fetchItem(row) {
        const res = await getContractSealDetail({
          budgetId: row.taskid,
          flowId: row.flowid,
        })
        Object.keys(this.formData).forEach((key) => {
          this.formData[key] = res.data.tcpb[key]
        })
        this.formData.flowId = res.data.flowId
        this.node = res.data.tcu
        this.formData.attList = res.data.signingList.map((i) => {
          return {
            attname: i.singingName,
            attsize: i.singingSize,
            uploader: i.uploaderName,
            attid: i.singingId,
          }
        })
      },
      showDetail(row, type, isWdcy) {
        this.title = '查看'
        this.disabled = true
        this.fetchItem(row)
        this.dialogFormVisible = true

        let userInfo = JSON.parse(localStorage.getItem('userInfo'))

        // 当viewOppsiteProcessInfo接口中的cystaffid与获取用户信息中的staffid相等且cystate等于需调整 基本信息改成可编辑 编号不可编辑
        if (
          row.cystaffid == userInfo.staffid &&
          row.cystate === '需调整' &&
          !isWdcy
        ) {
          this.isSendBack = true
          this.$nextTick(() => {
            this.$refs['edit'].showEdit(this.formData)
          })
        } else {
          this.isSendBack = false
        }
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
