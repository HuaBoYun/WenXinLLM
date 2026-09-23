<template>
  <el-dialog
    :close-on-click-modal="false"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1200px"
    @close="close"
    :modal="false"
    class="deal_dialog"
  >
    <el-tabs
      v-if="dialogFormVisible"
      v-model="activeName"
      type="card"
      @tab-click="handleTabClick"
      v-loading="loading"
    >
      <el-tab-pane label="基本信息" name="first">
        <!-- <h3>{{ title }}</h3> -->
        <!-- <component
          :is="moduleName"
          ref="info"
          :is-dialog="false"
          style="min-height: 500px"
        /> -->
        <create
          ref="createInfo"
          :flowType="flowType"
          :formId="formId"
          :is-dialog="false"
          :is-wdcy="true"
          :is-wfqdedit="wfqdIsEdit"
        ></create>

        <Approval
          v-show="showBl"
          ref="approval"
          :process-data="contract"
          :process-type="processType"
          :results="results"
          :show-sign="showSign"
          :show-repeat="showRepeat"
          :spinfo="spinfo"
        />
        <!-- <repeat-user :show-repeat="showRepeat"></repeat-user> -->
      </el-tab-pane>
      <el-tab-pane label="流程信息" name="second">
        <div style="display: flex">
          <Process
            :conf="flowTemplateJson"
            v-if="flowTemplateJson.nodeId"
            :key="updateKey"
          />
          <div style="width: 900px">
            <ProcessTable :data="flowTaskNodeList" />
          </div>
        </div>
      </el-tab-pane>
      <el-tab-pane label="流转记录" name="third">
        <recordList :list="flowTaskOperatorRecordList" :endTime="endTime" />
      </el-tab-pane>
      <el-tab-pane
        label="审批单"
        name="four"
        v-if="flowType == 'TblCyhwUnit' || flowType == 'HTBG'"
      >
        <div>
          <Authorization ref="authorization" />
          <div style="text-align: right">
            <el-button type="primary" @click="handlePrint()">
              打印审批单
            </el-button>
          </div>
        </div>
      </el-tab-pane>
      <el-tab-pane label="知会详情" name="five">
        <InformInfoList :flowTaskInfo="flowTaskInfo" />
      </el-tab-pane>
    </el-tabs>

    <!-- <div style="text-align: right">
      <el-button type="primary" @click="handlePrint()">打印审批单</el-button>
    </div> -->

    <PrintCom ref="PrintCom" />
  </el-dialog>
</template>

<script>
  import PrintCom from '@/views/contract/contractManage/components/print'
  import Authorization from './Authorization.vue'
  import { getContractItem } from '@/api/contract/manage'
  import Approval from '@/views/msg/components/options/Approval'
  import DealApprovalInfo from './DealApprovalInfo.vue'
  import InformInfoList from './InformInfoList.vue'
  import create from '@/views/msg/components/operation/CreateDetail.vue'
  import payment from '@/views/msg/components/operation/PaymentDetail.vue'
  import receiving from '@/views/msg/components/operation/ReceivingDetail.vue'
  import sample from '@/views/msg/components/operation/TemplateDetail.vue'
  // 审计相关
  import { getFaqiInfo } from '@/api/setting/msg'
  import Process from '@/components/Process/Preview'
  import { formatDay } from '@/utils/index'
  import recordList from './RecordList'
  import ProcessTable from './ProcessTable.vue'
  export default {
    name: 'wfqd',
    components: {
      DealApprovalInfo,
      sample,
      create,
      receiving,
      payment,
      Approval,
      Process,
      recordList,
      PrintCom,
      Authorization,
      InformInfoList,
      ProcessTable,
    },
    data() {
      return {
        updateKey: 0,
        flowType: '',
        formId: '',
        activeName: 'first',
        title: '',
        dialogFormVisible: false,
        contract: undefined,
        moduleName: '',
        businessKey: undefined,
        queryForm: {
          flowId: undefined,
          contractId: undefined,
          budgetid: undefined,
        },
        showBl: false,
        showSign: false,
        showRepeat: false,
        processType: 'blprocessjc',
        results: undefined, // 操作按钮
        contractstatus: 0, // 合同状态
        status: 0, // 审批状态
        rolename: '', //角色
        spinfo: {},
        loading: false,
        auditIsEdit: false,
        imgurl: '',
        aoptionList: [],
        formatDay,
        flowTemplateJson: {},
        flowTaskOperatorRecordList: [],
        endTime: 0,
        wfqdIsEdit: false,
        flowtaskinfoflowid: '',
        formId: 0, // 我发起的获取表单id
        ymFromId: '', // 我发起的获取引迈表单id
        flowTaskInfo: {},
      }
    },
    watch: {},
    methods: {
      handlePrint() {
        this.$refs['PrintCom'].printClick(
          this.contract,
          this.flowTaskOperatorRecordList
        )
      },
      async show(row, isEdit) {
        this.loading = true
        this.wfqdIsEdit = isEdit
        // this.title = row.flowName
        let _id = row.id

        if (row.v5ticket) {
          // 如果是oa过来的，processId作为id传入
          _id = row.processId
        }
        getFaqiInfo({
          id: _id,
          flowId: row.flowId,
        }).then((res) => {
          this.flowType = res.data.flowType
          this.formId = res.data.formId
          this.flowTaskInfo = res.data.dataJson.flowTaskInfo

          this.ymFromId = res.id || res.data.dataJson.flowTaskInfo.id
          this.formId = res.data.formId
          this.flowTemplateJson = res.data.dataJson.flowTaskInfo
            .flowTemplateJson
            ? JSON.parse(res.data.dataJson.flowTaskInfo.flowTemplateJson)
            : null
          this.flowTaskNodeList = res.data.dataJson.flowTaskNodeList
          if (this.flowTaskNodeList.length) {
            let assignNodeList = []
            for (let i = 0; i < this.flowTaskNodeList.length; i++) {
              const nodeItem = this.flowTaskNodeList[i]
              const loop = (data) => {
                if (Array.isArray(data)) data.forEach((d) => loop(d))
                if (data.nodeId === nodeItem.nodeCode) {
                  if (nodeItem.type == 0) data.state = 'state-past'
                  if (nodeItem.type == 1) data.state = 'state-curr'
                  if (
                    nodeItem.nodeType === 'approver' ||
                    nodeItem.nodeType === 'start' ||
                    nodeItem.nodeType === 'subFlow'
                  )
                    data.content = nodeItem.userName
                  return
                }
                if (data.conditionNodes && Array.isArray(data.conditionNodes))
                  loop(data.conditionNodes)
                if (data.childNode) loop(data.childNode)
              }
              loop(this.flowTemplateJson)
            }
            this.assignNodeList = assignNodeList
          }
          this.flowTaskOperatorRecordList =
            res.data.dataJson.flowTaskOperatorRecordList
          this.endTime =
            res.data.dataJson.flowTaskInfo.completion == 100
              ? res.data.dataJson.flowTaskInfo.endTime
              : 0
          this.updateKey += 1
          // 提交前置所需flowid
          this.flowtaskinfoflowid = res.data.dataJson.flowTaskInfo.flowId
          this.status = res.data.dataJson.flowTaskInfo.status
          this.contract = row
          this.dialogFormVisible = true
          this.loading = false

          this.$nextTick(() => {
            this.contract.flowId = this.queryForm.flowId
            // this.fetchInfo(res.data)
            this.$refs['createInfo'].showDetail(
              this.contract,
              this.contract.contracttype,
              this.auditIsEdit,
              this.formId,
              this.flowtaskinfoflowid,
              this.ymFromId,
              res.data.dataJson.flowTaskInfo.status
            )
          })
        })
      },

      // showDetail() {
      //   this.$nextTick(() => {
      //     this.refs['info'].showDetail(
      //       this.contract,
      //       this.contracttype,
      //       this.auditIsEdit
      //     )
      //   })
      // },
      getQueryVariable(url, variable) {
        var query = url.substring(1)
        var vars = query.split('?')
        for (var i = 0; i < vars.length; i++) {
          var pair = vars[i].split('=')
          if (pair[0] == variable) {
            return pair[1]
          }
        }
        return false
      },

      async fetchInfo(row) {
        const res = await getContractItem({
          flowid: '622316',
          flowname: '',
          contractId: row.formId,
        })
        this.loading = false

        this.$nextTick(() => {
          this.contract.flowId = this.queryForm.flowId
          this.$refs['createInfo'].showDetail(
            this.contract,
            this.contract.contracttype,
            this.auditIsEdit,
            this.formId,
            this.flowtaskinfoflowid,
            this.ymFromId,
            this.status
          )
        })
        this.spinfo = res.data

        if (res.data && res.data.tcu) {
          // this.contractstatus = res.data.tcu || res.data.tcu.contractstatus // 合同状态
          // this.contractstatus = res.data.tcu.contractstatus
          this.contractstatus =
            res.data && res.data.tcu ? res.data.tcu.contractstatus : '' // 合同状态
          this.rolename = res.data && res.data.rolename //角色
        }

        if (res && res.data && this.contract) {
          const cy = res.data.cy
          this.contract = Object.assign(this.contract, res.data.tcu, {
            task: res.data.task,
          })
          if (cy) {
            this.businessKey = cy.businesskey
          }
          if (res.data.results) {
            this.results = res.data.results
          } else {
            this.results = undefined
          }
        }
      },
      close() {
        this.activeName = 'first'
        this.dialogFormVisible = false
        this.$nextTick(() => {
          this.loading = true
        })
        // this.activeName = this.$options.data().activeName
        this.contract = this.$options.data().contract
        // this.moduleName = this.$options.data().moduleName
        // this.businessKey = this.$options.data().businessKey
        // this.showBl = false
      },
      handleTabClick() {
        // this.$nextTick(() => {
        //   this.$refs['dealApprovalInfo'].fetchApproval()
        // })
        if (this.activeName == 'four') {
          console.log(this.contract, 'this.contract')
          this.$refs['authorization'].show(
            this.contract,
            this.flowTaskOperatorRecordList
          )
        }
      },
    },
  }
</script>

<style>
  .deal_dialog .el-loading-spinner {
    top: 200px !important;
    margin-top: -21px;
    width: 100%;
    text-align: center;
    position: absolute;
  }
</style>
