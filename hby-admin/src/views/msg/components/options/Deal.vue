<template>
  <el-dialog
    :close-on-click-modal="false"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
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
        <h3>{{ title }}-审批</h3>
        <component :is="moduleName" ref="info" :is-dialog="false" />
        <!-- <el-col :span="24" v-if="isShenji">
          <el-divider>审批记录</el-divider>
        </el-col>
        <el-col :span="24" style="margin-bottom: 16px" v-if="isShenji">
          <el-table :data="aoptionList">
            <el-table-column
              align="center"
              label="审批人"
              width="200px"
              prop="staffidName"
            />
            <el-table-column align="center" label="意见" prop="optDesc" />
            <el-table-column align="center" label="时间" prop="createDate">
              <template slot-scope="scope">
                <div>{{ formatDay(scope.row.createDate) }}</div>
              </template>
            </el-table-column>

            <el-table-column align="center" label="结果" prop="optState" />
          </el-table>
        </el-col> -->

        <Approval
          v-show="showBl"
          ref="approval"
          :process-data="contract"
          :process-type="processType"
          :results="results"
          :show-sign="showSign"
          :show-repeat="showRepeat"
          :show-fgrepeat="showFgRepeat"
          :spinfo="spinfo"
        />
        <!-- <repeat-user :show-repeat="showRepeat"></repeat-user> -->
      </el-tab-pane>
      <el-tab-pane label="审批查看" name="second">
        <el-row :gutter="24">
          <el-col :span="24">
            <!-- <img
              v-if="isShenji"
              alt="审批图"
              :src="imgurl"
              style="margin-bottom: 20px; width: 100%"
            /> -->
            <DealApprovalInfo
              ref="dealApprovalInfo"
              :business-key="businessKey"
              :contract="contract"
              :module-name="moduleName"
            />
          </el-col>
        </el-row>
      </el-tab-pane>
    </el-tabs>
  </el-dialog>
</template>

<script>
  import {
    getAuditPlanApprovalInfo,
    getProjectApprovalInfo,
  } from '@/api/audit/implement'
  import {
    viewDealInfo,
    viewDealInfoForBorrow,
    viewDealInfoForSeal,
  } from '@/api/contract/manage'
  import Approval from '@/views/msg/components/options/Approval'
  import DealApprovalInfo from './DealApprovalInfo.vue'
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
  import { baseURL } from '@/config'
  import { formatDay } from '@/utils/index'
  import IndexEdit from '@/views/audit/plan/components/IndexEdit.vue'
  import project from '@/views/audit/project/components/IndexEdit2.vue'
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
    },
    data() {
      return {
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
        loading: true,
        auditIsEdit: false,
        imgurl: '',
        aoptionList: [],
        formatDay,
        isContract: true,
        isShenji: false,
        isBmfzr: '',
        showFgRepeat: false,
        isFgld: '',
      }
    },
    watch: {
      // moduleName: {
      //   handler(val) {
      //     // this.handleModuleName(val)
      //   },
      //   immediate: true,
      // },
    },
    methods: {
      async show(row, moduleName, showBl) {
        if (
          row.cyurl.indexOf('contract') != -1 ||
          row.cyurl.indexOf('cyhw') != -1
        ) {
          this.isContract = true
        } else {
          this.isContract = false
        }
        if (
          row.cyurl.indexOf('nbsj') != -1 ||
          row.cyurl.indexOf('audit') != -1
        ) {
          this.isShenji = true
        } else {
          this.isShenji = false
        }
        this.imgurl =
          baseURL + `/audit/nbsjapproval/picture?taskId=` + row.taskid
        this.handleModuleName(moduleName)
        this.showBl = showBl
        this.contract = row
        this.moduleName = moduleName
        this.dialogFormVisible = true
        this.loading = true
        // this.contract.flowId = this.queryForm.flowId
        // if (this.moduleName !== 'receiving' && this.moduleName !== 'payment') {
        //   this.fetchInfo()
        // }
        // this.$refs['info'].showDetail(this.contract, this.contract.contracttype)
        // this.handleModuleName(moduleName)

        this.$nextTick(() => {
          this.contract.flowId = this.queryForm.flowId
          if (
            this.moduleName !== 'receiving' &&
            this.moduleName !== 'payment'
          ) {
            if (
              this.moduleName == 'IndexEdit' ||
              this.moduleName == 'project'
            ) {
              this.auditFetchInfo(row)
            } else {
              this.fetchInfo()
            }
          }
          // this.$refs["info"].showDetail(
          //   this.contract,
          //   this.contract.contracttype,
          //   this.auditIsEdit
          // );
        })
        this.handleModuleName(moduleName)
      },
      handleModuleName(val) {
        this.showSign = false
        this.processType = 'blprocessjc'

        // 审计相关
        if (val == 'IndexEdit') {
          // this.queryForm.flowId = 733271
          this.title = '计划审批'
          this.showSign = false
          this.processType = 'dealAuditPlanApporvalInfo'
        }
        // 合同相关
        if (val == 'sample') {
          this.queryForm.flowId = 733271
          this.title = '合同范本'
          this.showSign = true
        }
        if (val == 'create') {
          this.queryForm.flowId = 622316
          this.title = '合同起草'
          this.showSign = true
        }
        if (val == 'change') {
          this.queryForm.flowId = 622325
          this.title = '合同变更'
          this.showSign = true
        }
        if (val == 'seal') {
          this.queryForm.flowId = 622324
          this.title = '合同用印'
          this.processType = 'blprocessyszc'
        }
        if (val == 'borrow') {
          this.queryForm.flowId = 622322
          this.title = '合同借阅'
          this.processType = 'blprocesshtjy'
        }
        if (val == 'opposite') {
          this.queryForm.flowId = 622322
          this.title = '相对方'
          this.processType = 'blprocessyszc'
        }
        if (val == 'receiving') {
          this.queryForm.flowId = 765525
          this.title = '收款管理'
          this.processType = 'blprocesssk'
        }
        if (val == 'payment') {
          this.queryForm.flowId = 759004
          this.title = '付款管理'
          this.processType = 'blprocessfk'
        }
        if (val == 'project') {
          this.queryForm.flowId = 759004
          this.title = '项目管理'
          this.processType = 'project'
        }
        if (val == 'reference') {
          // this.queryForm.flowId =
          this.title = '档案借阅'
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
      async auditFetchInfo(row) {
        var planid = this.getQueryVariable(this.contract.cyurl, 'planid')
        var projectId = this.getQueryVariable(this.contract.cyurl, 'spid')
        let fn = undefined
        let data = {}
        if (this.processType === 'project') {
          fn = getProjectApprovalInfo
          data = {
            cyId: this.contract.cyid ? this.contract.cyid : '',
            projectId: projectId,
            v: '',
            // taskId: this.contract.taskid ? this.contract.taskid : '',
          }
        } else {
          data = {
            planId: planid,
            // flowid: this.queryForm.flowId,
            // flowname: this.contract.recordtype,
            // contractId: this.contract.taskid,
            // budgetId: this.contract.taskid,
            cyId: this.contract.cyid ? this.contract.cyid : '',
            // taskId: this.contract.taskid ? this.contract.taskid : '',
            // tId: this.contract.taskid,
          }
          fn = getAuditPlanApprovalInfo
        }

        const res = await fn(data)

        // 当viewOppsiteProcessInfo接口中的cystaffid与获取用户信息中的staffid相等且cystate等于需调整 基本信息改成可编辑 编号不可编辑
        let userInfo = JSON.parse(localStorage.getItem('userInfo'))
        if (
          row.cystaffid == userInfo.staffid &&
          res.data.cy.cystate == '需调整'
        ) {
          this.auditIsEdit = true
        } else {
          this.auditIsEdit = false
        }
        // this.aoptionList = res.data.aoptionList || []
        this.loading = false
        this.$nextTick(() => {
          this.$refs['info'].showDetail(
            this.contract,
            this.contract.contracttype,
            this.auditIsEdit
          )
        })

        this.spinfo = res && res.data
        if (res.data && res.data.tcu) {
          // this.contractstatus = res.data.tcu || res.data.tcu.contractstatus // 合同状态
          // this.contractstatus = res.data.tcu.contractstatus
          this.contractstatus =
            res.data && res.data.tcu ? res.data.tcu.contractstatus : '' // 合同状态
          this.rolename = res.data && res.data.rolename //角色
          // this.isBmfzr = res.data && res.data.is_bmfzr //是否为部门负责人
        }

        if (res && res.data) {
          const cy = res.data.cy
          if (cy) {
            this.businessKey = cy.businesskey
          }
          if (res.data.btnList) {
            this.results = res.data.btnList
          } else {
            this.results = undefined
          }
        }
      },
      async fetchInfo() {
        let fn = null
        if (this.moduleName == 'seal' || this.moduleName == 'opposite') {
          fn = viewDealInfoForSeal
        } else if (this.moduleName == 'borrow') {
          fn = viewDealInfoForBorrow
        } else {
          fn = viewDealInfo
        }

        const res = await fn({
          flowid: this.queryForm.flowId,
          flowname: this.contract.recordtype,
          contractId: this.contract.taskid,
          budgetId: this.contract.taskid,
          cyId: this.contract.cyid ? this.contract.cyid : '',
          lendid: this.contract.taskid,
          // tId: this.contract.taskid,
        })

        this.loading = false
        if (
          res.data &&
          res.data.cy &&
          res.data.cy.cytype == '合同管理' &&
          res.data.flowname == 'HTGL007'
        ) {
          //合同范本
          this.moduleName = 'sample'
          this.title = '合同范本'
        }
        if (
          res.data &&
          res.data.cy &&
          res.data.cy.cytype == '合同管理' &&
          res.data.flowname == 'HTGL002'
        ) {
          //合同起草
          this.moduleName = 'create'
          this.title = '合同起草'
        }
        if (
          res.data &&
          res.data.cy &&
          res.data.cy.cytype == '合同管理' &&
          res.data.flowname == 'HTGL005'
        ) {
          //合同变更
          this.moduleName = 'change'
          this.title = '合同变更'
        }
        if (this.moduleName === 'borrow') {
          this.contract = {
            ...res.data.tblContractLead,
            ...res.data.cy,
            ...this.contract,
          }
        }
        this.$nextTick(() => {
          this.contract.flowId = this.queryForm.flowId
          this.$refs['info'].showDetail(
            this.contract,
            this.contract.contracttype,
            this.auditIsEdit
          )
        })

        this.spinfo = res.data

        if (res.data && res.data.tcu) {
          // this.contractstatus = res.data.tcu || res.data.tcu.contractstatus // 合同状态
          // this.contractstatus = res.data.tcu.contractstatus
          this.contractstatus =
            res.data && res.data.tcu ? res.data.tcu.contractstatus : '' // 合同状态
          this.rolename = res.data && res.data.rolename //角色
          this.isBmfzr = res.data && res.data.is_bmfzr //是否为部门负责人

          this.isFgld = res.data && res.data.is_fgld //是否为分管领导
        }
        // 是否显示接收人
        if (
          this.contractstatus !== 2 &&
          this.isBmfzr == '1'
          // this.rolename == '合同黑名单管理员'
          // (this.rolename === '法务负责人' || this.rolename === '财务负责人')
        ) {
          this.showRepeat = true
        } else {
          this.showRepeat = false
        }

        // 分管领导显示接收人
        if (
          this.contractstatus !== 2 &&
          this.isFgld == '1'
          // this.rolename == '合同黑名单管理员'
          // (this.rolename === '法务负责人' || this.rolename === '财务负责人')
        ) {
          this.showFgRepeat = true
        } else {
          this.showFgRepeat = false
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
        this.dialogFormVisible = false
        this.$nextTick(() => {
          this.loading = true
        })
        this.activeName = this.$options.data().activeName
        this.contract = this.$options.data().contract
        this.moduleName = this.$options.data().moduleName
        this.businessKey = this.$options.data().businessKey
        this.showBl = false
      },
      handleTabClick() {
        this.$nextTick(() => {
          this.$refs['dealApprovalInfo'].fetchApproval()
        })
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
