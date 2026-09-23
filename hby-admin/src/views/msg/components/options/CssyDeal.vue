<template>
  <el-dialog
    :close-on-click-modal="false"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1200px"
    @close="close"
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
          :is-wfqdedit="cssyIsEdit"
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
        <Process :conf="flowTemplateJson" v-if="flowTemplateJson.nodeId" />
      </el-tab-pane>
      <el-tab-pane label="流转记录" name="third">
        <recordList :list="flowTaskOperatorRecordList" :endTime="endTime" />
      </el-tab-pane>
      <el-tab-pane label="知会详情" name="four">
        <InformInfoList :flowTaskInfo="flowTaskInfo" />
      </el-tab-pane>
    </el-tabs>
  </el-dialog>
</template>

<script>
  import { getContractItem } from '@/api/contract/manage'
  import Approval from '@/views/msg/components/options/Approval'
  import DealApprovalInfo from './DealApprovalInfo.vue'
  import InformInfoList from './InformInfoList.vue'
  // import RepeatUser from '@/views/msg/components/options/RepeatUser.vue'
  import borrow from '@/views/msg/components/operation/BorrowDetail.vue'
  import change from '@/views/msg/components/operation/ChangeDetail.vue'
  import seal from '@/views/msg/components/operation/ContractSealDetail.vue'
  import create from '@/views/msg/components/operation/CreateDetail.vue'
  import opposite from '@/views/msg/components/operation/MaintainDetail.vue'
  import payment from '@/views/msg/components/operation/PaymentDetail.vue'
  import receiving from '@/views/msg/components/operation/ReceivingDetail.vue'
  import sample from '@/views/msg/components/operation/TemplateDetail.vue'
  // 审计相关
  import { getShiYiInfo } from '@/api/setting/msg'
  import Process from '@/components/Process/Preview'
  import { formatDay } from '@/utils/index'
  import IndexEdit from '@/views/audit/plan/components/IndexEdit.vue'
  import project from '@/views/audit/project/components/IndexEdit2.vue'
  import recordList from './RecordList'
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
      receiving,
      payment,
      Approval,
      IndexEdit,
      project,
      Process,
      recordList,
      InformInfoList,
    },
    data() {
      return {
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
        cssyIsEdit: false,
        flowtaskinfoflowid: '',
        formId: 0, // 我发起的获取表单id
        ymFromId: '', // 我发起的获取引迈表单id
        flowTaskInfo: {},
      }
    },
    watch: {},
    methods: {
      async show(row, isEdit) {
        this.loading = true
        this.cssyIsEdit = isEdit
        this.title = row.flowName
        let res = await getShiYiInfo({
          id: row.id,
          flowId: row.flowId,
          thisStepId: row.thisStepId,
        })

        this.flowType = res.data.flowType
        this.formId = res.data.formId

        // this.ymFromId = res.id || res.data.dataJson.flowTaskInfo.id
        // this.formId = res.data.formId
        // this.flowTemplateJson = res.data.dataJson.flowTaskInfo.flowTemplateJson
        //   ? JSON.parse(res.data.dataJson.flowTaskInfo.flowTemplateJson)
        //   : null
        // this.flowTemplateJson.state = 'state-curr'
        // this.flowTaskOperatorRecordList =
        //   res.data.dataJson.flowTaskOperatorRecordList
        // this.endTime =
        //   res.data.dataJson.flowTaskInfo.completion == 100
        //     ? res.data.dataJson.flowTaskInfo.endTime
        //     : 0
        // // 提交前置所需flowid
        // this.flowtaskinfoflowid = res.data.dataJson.flowTaskInfo.flowId
        // this.contract = row
        // this.dialogFormVisible = true
        // this.loading = false
        this.flowTaskInfo = res.data.flowTaskInfo
        this.flowCurrtenForm = { ...res.data }
        this.flowTemplateJson = res.data.flowTaskInfo.flowTemplateJson
          ? JSON.parse(res.data.flowTaskInfo.flowTemplateJson)
          : null
        this.flowTaskNodeList = res.data.flowTaskNodeList
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
        this.flowTaskOperatorRecordList = res.data.flowTaskOperatorRecordList
        this.flowTaskOperatorList = res.data.flowTaskOperatorList
        this.flowTaskInfo = res.data.flowTaskInfo
        this.ymStaffId = res.data.ymStaffId
        this.endTime =
          res.data.flowTaskInfo.completion == 100
            ? res.data.flowTaskInfo.endTime
            : 0
        this.approversProperties = res.data.approversProperties
        this.contract = row
        this.dialogFormVisible = true
        this.loading = false

        this.$nextTick(() => {
          this.contract.flowId = this.queryForm.flowId
          this.fetchInfo(res.data)
          // this.$refs['createInfo'].showDetail(
          //   this.contract,
          //   this.contract.contracttype,
          //   this.auditIsEdit,
          //   this.formId,
          //   this.flowtaskinfoflowid,
          //   this.ymFromId
          // )
        })
      },

      showDetail() {
        this.$nextTick(() => {
          this.refs['info'].showDetail(
            this.contract,
            this.contracttype,
            this.auditIsEdit
          )
        })
      },
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
            row.formId
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
