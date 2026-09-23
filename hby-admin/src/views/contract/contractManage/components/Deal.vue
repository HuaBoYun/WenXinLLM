<!--
 * @Date: 2022-03-25 16:26:05
 * @LastEditors: zengping.liu
 * @LastEditTime: 2022-05-07 14:43:16
 * @FilePath: /hb-admin/src/views/contract/contractManage/components/Deal.vue
-->
<template>
  <el-dialog
    append-to-body
    :close-on-click-modal="false"
    fullscreen
    :title="title"
    :visible.sync="dialogFormVisible"
    width="500px"
    @close="close"
  >
    <el-tabs v-model="activeName" type="card" @tab-click="handleTabClick">
      <el-tab-pane label="基本信息" name="first">
        <h3>{{ title }}-审批</h3>
        <component :is="moduleName" ref="info" :is-dialog="false" />
        <!-- <sample v-show="moduleName == 'sample'" />
        <create v-show="moduleName == 'create'" />
        <change v-show="moduleName == 'change'" />
        <seal v-show="moduleName == 'seal'" />
        <opposite v-show="moduleName == 'opposite'" /> -->
      </el-tab-pane>
      <el-tab-pane label="审批查看" name="second">
        <DealApprovalInfo
          ref="approval"
          :borrow="borrow"
          :business-key="businessKey"
          :contract="contract"
          :module-name="moduleName"
        />
      </el-tab-pane>

      <!-- <el-tab-pane v-if="authorizationType" label="审批单" name="authorization">
        <div v-if="authorizationType == 1">
          <AuthorizationCreate :form="authorizationForm" :cy="cy" />
        </div>
        <div v-if="authorizationType == 2">
          <AuthorizationSeal :form="authorizationForm" />
        </div>
      </el-tab-pane> -->
    </el-tabs>
  </el-dialog>
</template>

<script>
  import DealApprovalInfo from './DealApprovalInfo.vue'
  import AuthorizationCreate from './contractsEdit/AuthorizationCreate.vue'
  // import AuthorizationSeal from './contractsEdit/AuthorizationSeal.vue'
  import {
    viewDealInfo,
    viewDealInfoForSeal,
    viewDealInfoForBorrow,
    viewDealProcess,
  } from '@/api/contract/manage'

  import sample from '@/views/contract/contractManage/components/TemplateDetail.vue'
  import create from '@/views/contract/contractManage/components/contractsEdit/CreateDetail.vue'
  import change from '@/views/contract/contractManage/components/contractsEdit/ChangeDetail.vue'
  import seal from '@/views/contract/contractManage/components/ContractSealDetail.vue'
  import opposite from '@/views/contract/opposite/components/MaintainDetail.vue'
  import borrow from '@/views/contract/contractManage/components/BorrowDetail.vue'

  export default {
    name: 'Deal',
    components: {
      DealApprovalInfo,
      sample,
      create,
      change,
      seal,
      opposite,
      borrow,
      AuthorizationCreate,

      // sample: () =>
      //   import('@/views/contract/contractManage/components/TemplateDetail.vue'),
      // create: () =>
      //   import(
      //     '@/views/contract/contractManage/components/contractsEdit/CreateDetail.vue'
      //   ),
      // change: () =>
      //   import(
      //     '@/views/contract/contractManage/components/contractsEdit/ChangeDetail.vue'
      //   ),
      // seal: () =>
      //   import(
      //     '@/views/contract/contractManage/components/ContractSealDetail.vue'
      //   ),
      // opposite: () =>
      //   import('@/views/contract/opposite/components/MaintainDetail.vue'),
    },
    data() {
      return {
        activeName: 'first',
        title: '',
        dialogFormVisible: false,
        contract: undefined,
        borrow: undefined,
        moduleName: 'sample',
        businessKey: undefined,
        queryForm: {
          flowId: undefined,
          contractId: undefined,
          budgetid: undefined,
          lendid: undefined, // 借阅id
        },
        authorizationType: 0,
        authorizationForm: undefined,
        cy: undefined,
      }
    },
    watch: {
      moduleName: {
        //监听变量赋值
        handler(val) {
          if (val == 'sample') {
            this.queryForm.flowId = 733271
            this.title = '合同范本'
          }
          if (val == 'create') {
            this.queryForm.flowId = 622316
            this.title = '合同起草'
          }
          if (val == 'change') {
            this.queryForm.flowId = 622325
            this.title = '合同变更'
          }
          if (val == 'seal') {
            this.queryForm.flowId = 622324
            this.title = '合同用印'
          }
          if (val == 'opposite') {
            this.queryForm.flowId = 622322
            this.title = '相对方'
          }
          if (val == 'borrow') {
            this.queryForm.flowId = 622327
            this.title = '借阅信息'
          }
        },
        immediate: true,
      },
    },
    methods: {
      show(row, moduleName) {
        if (moduleName == 'borrow') {
          this.borrow = row
        } else {
          this.contract = row
        }
        this.moduleName = moduleName
        this.dialogFormVisible = true
        this.$nextTick(() => {
          this.fetchInfo()
          if (this.moduleName == 'borrow') {
            this.$refs['info'].showDetail(this.borrow)
          } else {
            this.$refs['info'].showDetail(
              this.contract,
              this.contract.contracttype
            )
          }
        })

        if (moduleName == 'create') {
          this.authorizationType = 1
        } else if (moduleName == 'seal') {
          this.authorizationType = 2
        }
      },
      showDetail() {
        this.$nextTick(() => {
          this.refs['info'].showDetail(this.contract, this.contracttype)
        })
      },
      //一系列的数据处理
      async fetchInfo() {
        let fn = null
        if (this.moduleName == 'seal' || this.moduleName == 'opposite') {
          fn = viewDealInfoForSeal
        } else if (this.moduleName == 'borrow') {
          fn = viewDealInfoForBorrow
        } else {
          fn = viewDealInfo
        }

        let param = {
          flowid: this.queryForm.flowId,
        }
        if (this.moduleName == 'borrow') {
          param.lendid = this.borrow.lendid
        } else {
          param.contractId = this.contract.contractid
          param.budgetId = this.contract.budgetid
          param.tId = this.contract.budgetid
        }
        const res = await fn(param)
        const cy = res.data.cy
        const tcu = res.data.tcu
        if (cy) {
          this.businessKey = cy.businesskey
          this.cy = cy
        }
        if (this.moduleName == 'create') {
          if (cy && tcu) {
            this.getDealProcess(cy.taskid, tcu.contractid)
          }
        }
      },
      async getDealProcess(taskid, contractid) {
        const data = await viewDealProcess({
          contractId: contractid,
          taskId: taskid,
          budgetId: contractid,
        })
        this.authorizationForm = data.data || {}
      },
      close() {
        this.dialogFormVisible = false
      },
      handleTabClick() {
        this.$nextTick(() => {
          this.$refs['approval'].fetchApproval()
        })
      },
    },
  }
</script>
