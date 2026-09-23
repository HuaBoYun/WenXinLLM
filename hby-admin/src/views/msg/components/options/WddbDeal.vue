<template>
  <el-dialog
    :close-on-click-modal="false"
    :title="title"
    :visible.sync="dialogFormVisible"
    :append-to-body="true"
    width="1200px"
    @close="close"
    class="deal_dialog wddb_dialog"
    :show-close="!$store.state.work.processMobile"
    :destroy-on-close="true"
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
          :userName="userName"
          :userId="userId"
          :formId="formId"
          :is-dialog="false"
          :is-wdcy="false"
          :is-wfqdedit="wfqdIsEdit"
          :flowTaskOperatorList="flowTaskOperatorList"
          :approversProperties="approversProperties"
          :flowTaskInfo="flowTaskInfo"
          :flowTaskNodeList="flowTaskNodeList"
          :nextNodeName="this.nextNodeName"
          :nextStepId="this.nextStepId"
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
        <div
          :style="`display: flex;${
            $store.state.work.processMobile ? 'flex-wrap: wrap;' : ''
          }`"
        >
          <Process
            :conf="flowTemplateJson"
            v-if="flowTemplateJson.nodeId"
            :key="updateKey"
          />
          <div
            :style="
              $store.state.work.processMobile ? 'width: 100%;' : 'width: 900px;'
            "
          >
            <ProcessTable :data="flowTaskNodeList" />
          </div>
        </div>
      </el-tab-pane>
      <el-tab-pane label="流转记录" name="third">
        <recordList :list="flowTaskOperatorRecordList" :endTime="endTime" />
      </el-tab-pane>
      <el-tab-pane
        label="知会详情"
        name="four"
        v-if="!$store.state.work.processMobile"
      >
        <InformInfoList :flowTaskInfo="flowTaskInfo" />
      </el-tab-pane>
      <el-tab-pane
        label="审批单"
        name="five"
        v-if="flowType == 'HTYJ' && showD"
      >
        <CreateDetail :isDialog="false" :info="DetailInfo" />
      </el-tab-pane>
    </el-tabs>
    <div slot="footer" style="text-align: right" v-if="!isJudge">
      <el-button
        type="primary"
        @click="handleAudit('audit')"
        v-if="approversProperties.hasAuditBtn"
      >
        {{ approversProperties.auditBtnText }}
      </el-button>
      <el-button
        type="primary"
        @click="handleAudit('reject')"
        v-if="approversProperties.hasRejectBtn"
      >
        {{ approversProperties.rejectBtnText }}
      </el-button>
      <!-- <el-button type="primary" v-if="approversProperties.hasRevokeBtn">
        {{ approversProperties.revokeBtnText }}
      </el-button>
      <el-button type="primary" v-if="approversProperties.hasSaveBtn">
        {{ approversProperties.saveBtnText }}
      </el-button> -->
      <el-button
        type="primary"
        @click="handleTransfer"
        v-if="approversProperties.hasTransferBtn"
      >
        {{ approversProperties.transferBtnText }}
      </el-button>
      <!-- <el-button type="primary" v-if="approversProperties.hasSubmitBtn">
        {{ approversProperties.submitBtnText }}
      </el-button> -->
    </div>
    <BeforSubmit
      ref="befor"
      @close="beforClose"
      :hasSign="this.hasSign"
      :hasFreeApprover="this.hasFreeApprover"
      :isCustomCopy="this.isCustomCopy"
    />
    <ZfDialogVue ref="zf" @close="close" />
  </el-dialog>
</template>

<script>
  import {
    getContractItem,
    ymWorkAudit,
    getContractTranInfo,
  } from '@/api/contract/manage'
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
  import { getTodoInfo } from '@/api/setting/msg'
  import Process from '@/components/Process/Preview'
  import { formatDay } from '@/utils/index'
  import IndexEdit from '@/views/audit/plan/components/IndexEdit.vue'
  import project from '@/views/audit/project/components/IndexEdit2.vue'
  import BeforSubmit from './beforSubmit.vue'
  import recordList from './RecordList'
  import ZfDialogVue from './zfDialog.vue'
  import { wddbDialogStyleFunc } from '@/utils/processMobile'
  import ProcessTable from './ProcessTable.vue'
  import CreateDetail from '@/views/contract/contractManage/components/contractsEdit/CreateDetail.vue'
  export default {
    name: 'Deal',
    components: {
      ZfDialogVue,
      BeforSubmit,
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
      ProcessTable,
      CreateDetail,
    },
    data() {
      return {
        updateKey: 0,
        flowType: '',
        userName: '',
        userId: 0,
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
        flowTaskOperatorList: [],
        approversProperties: {},
        flowTaskInfo: {},
        endTime: 0,
        wfqdIsEdit: false,
        hasSign: false, //是否有签名
        hasFreeApprover: false, //是否加签
        isCustomCopy: false, //是否自定义抄送人
        //代办主键
        taskId: undefined,
        thisStepId: undefined,
        nextNodeName: undefined,
        isJudge: 0,
        DetailInfo: {},
        showD: false,
        nextStepId: undefined,
      }
    },
    watch: {},
    methods: {
      handleTransfer() {
        this.$refs['zf'].show(this.taskId, this.flowTaskInfo)
      },

      async show(row, isEdit) {
        this.dialogFormVisible = true
        this.loading = true
        this.wfqdIsEdit = isEdit
        this.thisStepId = row.thisStepId
        this.title = row.flowName

        let res = await getTodoInfo({
          flowId: row.flowId,
          id: row.id,
          processId: row.processId,
          thisStepId: row.thisStepId,
        })

        //请求合同移交详情接口请求合同参数展示审批单数据
        if (this.flowType == 'HTYJ') {
          getContractTranInfo({ tranId: res.data.formId }).then((item) => {
            this.DetailInfo = {
              contractid: item.data.tct.contractId,
              recordtype: item.data.tct.recordtype,
              flowid: item.data.tct.flowid,
            }
            this.showD = true
          })
        }
        //
        //
        this.buttonJudge(row.thisStepId, res.data.flowTaskNodeList)
        this.taskId = row.id
        this.nextStepId = res.data.nextStepId
        this.nextNodeName = res.data.flowTaskOperatorList?.[0]?.nodeName

        this.hasSign = res.data.approversProperties.hasSign //是否有签名
        this.hasFreeApprover = res.data.approversProperties.hasFreeApprover //是否加签
        this.isCustomCopy = res.data.approversProperties.isCustomCopy //是否自定义抄送人
        this.flowType = res.data.flowType
        this.userId = res.data.userId
        this.userName = res.data.userName
        this.formId = res.data.formId
        this.flowCurrtenForm = { ...res.data }
        this.flowTemplateJson = res.data.flowTaskInfo.flowTemplateJson
          ? JSON.parse(res.data.flowTaskInfo.flowTemplateJson)
          : {}
        this.flowTaskNodeList = res.data.flowTaskNodeList
        if (this.flowTaskNodeList.length) {
          // 先用 nodeCode 建索引 Map，O(N)；再单次遍历整棵树完成所有节点标注，O(M)
          // 原来是 O(N×M)：对每个节点都完整递归一遍树
          const nodeMap = {}
          for (const nodeItem of this.flowTaskNodeList) {
            nodeMap[nodeItem.nodeCode] = nodeItem
          }
          const loop = (data) => {
            if (Array.isArray(data)) {
              data.forEach((d) => loop(d))
              return
            }
            const nodeItem = nodeMap[data.nodeId]
            if (nodeItem) {
              if (nodeItem.type == 0) data.state = 'state-past'
              if (nodeItem.type == 1) data.state = 'state-curr'
              if (
                nodeItem.nodeType === 'approver' ||
                nodeItem.nodeType === 'start' ||
                nodeItem.nodeType === 'subFlow'
              )
                data.content = nodeItem.userName
            }
            if (data.conditionNodes && Array.isArray(data.conditionNodes))
              loop(data.conditionNodes)
            if (data.childNode) loop(data.childNode)
          }
          loop(this.flowTemplateJson)
          this.updateKey += 1
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
        this.loading = false

        // 合并为单个 $nextTick，减少一次微任务调度
        this.$nextTick(() => {
          if (this.$store.state.work.processMobile) wddbDialogStyleFunc()
          this.contract.flowId = this.queryForm.flowId
          if (res.data.flowType == 'TblCyhwUnit') {
            this.fetchInfo(res.data)
          }
        })
      },
      buttonJudge(thisStepId, flowTaskNodeList) {
        let t = 0
        flowTaskNodeList.map((item) => {
          if (item.id == thisStepId) {
            t = item.completion
          }
        })
        t == 1 ? (this.isJudge = 1) : (this.isJudge = 0)
      },
      beforClose() {
        this.close()
      },
      // 通过
      async handleAudit(e) {
        let obj = {
          flowId: this.flowTaskInfo.flowId,
          id: this.flowTaskInfo.id,
          operatorId: this.taskId,
          formId: this.flowCurrtenForm.formId,
          thisStepId: this.thisStepId,
          nextNodeName: this.nextNodeName,
          nextStepId: this.nextStepId,
        }
        // if (this.flowTaskOperatorList.length) {
        //   this.flowTaskOperatorList.map((item) => {
        //     if (
        //       this.flowTaskInfo.thisStepId === item.nodeCode &&
        //       item.handleId === this.ymStaffId
        //     ) {
        //       obj.operatorId = item.id
        //     }
        //   })
        // }
        if (this.approversProperties.auditBtnText == '已 阅') {
          const { code } = await ymWorkAudit({ ...obj, handleOpinion: '已阅' })
          if (code == 1) {
            this.$message.success('审批成功')
            this.$bus.$emit('updateMsg', 0)
          }
        } else {
          // if (
          //   this.flowType == 'ZGFA' &&
          //   this.flowTaskInfo.thisStepId != 'rmkfBQ2'
          // ) {
          //   const schemeData = this.$refs['createInfo'].getSchemeData()
          //   if (schemeData && schemeData.issuesList) {
          //     const hasEmptyPlan = schemeData.issuesList.some(
          //       (item) => !item.rectificationPlan
          //     )
          //     if (hasEmptyPlan) {
          //       this.$message.error('请编辑整改方案')
          //       return
          //     }
          //   }
          // }
          if (this.flowType == 'SJQZD' && this.nextNodeName == '业务人员') {
            const evidenceData = this.$refs['createInfo'].getEvidenceData()
            if (
              evidenceData.evidenceOpinion == '' ||
              !evidenceData.evidenceOpinion
            ) {
              return this.$message.error('请填写证据提供单位意见')
            }
            // 业务人员审核时，先保存审计取证单数据
            const saveResult = await this.$refs['createInfo'].saveEvidenceData()
            if (!saveResult) {
              return // 保存失败，不继续审核
            }
          }
          this.$refs['befor'].show(obj, e)
        }
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
            row.formId,
            this.formId,
            this.flowtaskinfoflowid,
            this.ymFromId
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
            taskid: row.formId,
            flowid: '622316',
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
          this.$emit('fetchData')
        })
        // this.activeName = this.$options.data().activeName
        // this.contract = this.$options.data().contract;
        this.contract = undefined
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
