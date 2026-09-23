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
        <el-row :gutter="15">
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
        </el-row>
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

    <template #footer>
      <el-button @click="close">取 消</el-button>
      <el-button type="primary" @click="close">确 定</el-button>
    </template>
  </el-dialog>
</template>

<script>
  import {
    fkglapproval,
    tosptzglinfo,
    viewPaymentManagemen,
  } from '@/api/contract/financing'

  export default {
    name: 'DraftEdit',
    data() {
      return {
        invoicestatusTextArr: [
          '未开票',
          '已开票',
          '未收款',
          '已收款',
          '已退票',
        ],
        activeName: 'first',
        form: {},
        rules: {
          rule: [
            {
              required: true,
              message: '请输入账号',
              trigger: 'blur',
            },
          ],
        },
        list: [],
        list1: [],
        listLoading: true,
        title: '业务审批',
        dialogFormVisible: false,
        imgurl: '',
      }
    },
    created() {},
    methods: {
      async getList(row) {
        this.listLoading = true
        // this.queryForm.negotiaId = this.netotiaId
        const { cy } = await tosptzglinfo({
          collectionId: row.paymentid,
          flowid: '759004',
        })
        this.list1 = cy
        this.listLoading = false
      },
      async getList2() {
        //
        this.listLoading = true
        // this.queryForm.negotiaId = this.netotiaId
        const { list, url } = await fkglapproval({
          paymentid: this.form.paymentid,
          flowid: '759004',
          taskid: this.list1.businesskey,
        })
        this.list = list
        this.imgurl = url
        this.listLoading = false
      },
      //回调
      handleClick(e) {
        if (e.label === '审批查看') {
          //
          // this.getList(this.list)
          // const taskId = this.list[this.list.length-1].taskId
          this.getList2()
        }
      },
      showEdit(row) {
        this.dialogFormVisible = true
        this.fetchInfo(row)
        this.getList(row)
      },
      async fetchInfo(row) {
        this.listLoading = true
        const { payment } = await viewPaymentManagemen({
          paymentId: row.paymentid,
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
        // this.total = totalRecord
        this.listLoading = false
      },
      close() {
        this.dialogFormVisible = false
        this.activeName = 'first'
      },
      // save() {
      //   this.$refs['form'].validate(async (valid) => {
      //     if (valid) {
      //       const { msg } = await orgBankInfoSave(this.form)
      //       this.$baseMessage(msg, 'success', 'vab-hey-message-success')
      //       this.$emit('fetch-data')
      //       this.close()
      //     }
      //   })
      // },
    },
  }
</script>
<style scoped>
  .formula .el-form-item--small.el-form-item {
    margin-bottom: 5px;
  }
</style>
