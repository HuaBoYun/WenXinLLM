<template>
  <el-dialog
    :close-on-click-modal="false"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
  >
    <el-tabs
      v-model="activeName"
      class="demo-tabs"
      type="card"
      @tab-click="handleClick"
    >
      <el-tab-pane label="基本信息" name="first">
        <!-- <el-row :gutter="15">
          <el-form ref="form" label-width="140px" :model="form" :rules="rules">
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
        </el-row> -->
        <!-- <Approval
          v-if="show == 1"
          ref="approval"
          :process-data="processData"
          process-type="blprocesssk"
        /> -->
        <PaymentDetailContent v-if="!isSendBack" :form-data="form" />
        <PaymentDetailContentEdit
          v-else
          :form-data="form"
          :isSendBack="isSendBack"
          ref="edit"
        />

        <Approval
          v-show="show === 1"
          ref="approval"
          :process-data="processData"
          process-type="blprocessfk"
          :results="results"
          :spinfo="spinfo"
        />
      </el-tab-pane>
      <el-tab-pane label="审批查看" name="second">
        <img
          alt="审批图"
          :src="imgurl"
          style="margin-bottom: 20px; width: 90%"
        />
        <el-table
          v-loading="listLoading"
          :data="list"
          style="margin-bottom: 20px"
        >
          <el-table-column align="center" label="流程ID" prop="processName" />
          <el-table-column align="center" label="办理人" prop="approver" />
          <el-table-column
            align="center"
            label="办理角色"
            prop="approvalRole"
          />
          <el-table-column align="center" label="办理结果	" prop="result" />
          <el-table-column align="center" label="办理意见" prop="examination" />
          <el-table-column
            align="center"
            label="办理时间"
            prop="approvaldate"
          />
          <el-table-column
            align="center"
            label="下一步：办理人/办理角色"
            prop="handle"
          />
        </el-table>
      </el-tab-pane>
    </el-tabs>

    <!-- <template #footer>
      <el-button @click="close">取 消</el-button>
      <el-button type="primary" @click="close">确 定</el-button>
    </template> -->
  </el-dialog>
</template>

<script>
  import {
    fkglapproval,
    tosptzglinfo1,
    viewPaymentManagemen,
  } from '@/api/contract/financing'
  import Approval from '@/views/msg/components/options/Approval'
  import PaymentDetailContent from '@/views/contract/financing/components/PaymentDetailContent.vue'
  import PaymentDetailContentEdit from '@/views/contract/financing/components/PaymentDetailContentEdit.vue'

  export default {
    name: 'DraftEdit',
    components: { Approval, PaymentDetailContent, PaymentDetailContentEdit },
    data() {
      return {
        isSendBack: false,
        spinfo: {},
        results: undefined, // 操作按钮
        invoicestatusTextArr: [
          '未开票',
          '已开票',
          '未收款',
          '已收款',
          '已退票',
        ],
        activeName: 'first',
        form: {},
        list: [],
        list1: [],
        listLoading: true,
        title: '业务审批',
        dialogFormVisible: false,
        imgurl: '',
        show: 0,
        processData: {},
      }
    },
    created() {},
    methods: {
      async getList(row) {
        this.listLoading = true
        const { data } = await tosptzglinfo1({
          paymentId: row.paymentId,
          flowid: '759004',
        })

        this.list1 = data.cy
        this.processData = data
        this.spinfo = data

        if (data.results) {
          this.results = data.results
        } else {
          this.results = undefined
        }
        let userInfo = JSON.parse(localStorage.getItem('userInfo'))
        var that = this
        // 当viewOppsiteProcessInfo接口中的cystaffid与获取用户信息中的staffid相等且cystate等于需调整 基本信息改成可编辑 编号不可编辑
        if (
          data.cy.cystaffid == userInfo.staffid &&
          data.cy.cystate === '需调整'
        ) {
          that.isSendBack = true
        }

        this.listLoading = false
      },
      async getList2() {
        this.listLoading = true
        const { list, url } = await fkglapproval({
          paymentid: this.list1.taskid,
          flowid: '759004',
          taskid: this.list1.businesskey,
        })
        this.list = list
        this.imgurl = url
        this.listLoading = false
      },
      handleClick(e) {
        if (e.label === '审批查看') {
          this.getList2()
        }
      },
      showEdit(row, type) {
        if (type == 'wddb') {
          this.show = 1
        }
        this.processData = row
        this.dialogFormVisible = true
        this.fetchInfo(row)
        this.getList(row)
        this.approval()
      },
      async fetchInfo(row) {
        this.listLoading = true
        const { payment } = await viewPaymentManagemen({
          paymentId: row.taskid,
        })
        this.form = Object.assign({}, payment)
        this.form.applyOrgId = payment.aPOORGID
        this.form.orgname = payment.aPOORGNAME
        this.form.orgid = payment.aPSSTAFFID
        this.form.realname = payment.aPSREALNAME
        this.form.paymenmoney = payment.PAYMONEY || 0
        this.form.noPaymoney = payment.fKMONEY
        this.form.paymentmoney = payment.invoicemoney
        this.form.executestaff = payment.realname
        this.listLoading = false
      },
      close() {
        this.dialogFormVisible = false
        this.activeName = 'first'
      },
      approval() {
        this.$nextTick(() => {
          this.$refs['approval'].showDetail()
        })
      },
    },
  }
</script>
<style scoped>
  .formula .el-form-item--small.el-form-item {
    margin-bottom: 5px;
  }
</style>
