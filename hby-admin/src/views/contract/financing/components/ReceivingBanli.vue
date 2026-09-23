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
            <el-col :span="12">
              <el-form-item label="收款合同" prop="rule">
                <span>{{ form.contractname }}</span>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="收款日期" prop="rule">
                <span>{{ form.collectionskdate }}</span>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="合同编号" prop="rule">
                <span>{{ form.contractno }}</span>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="付款单位" prop="rule">
                <span>{{ form.budgetname }}</span>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="对应收款项" prop="rule">
                <span>{{ form.nodecontent }}</span>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="对应收款金额" prop="rule">
                <span>{{ form.nodemoney }}</span>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="付款银行账号" prop="rule">
                <span>{{ form.bankaccnum }}</span>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="收款银行账号" prop="rule">
                <span>{{ form.bankaccount }}</span>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="发票号" prop="rule">
                <span>{{ form.invoiceno }}</span>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="开票金额" prop="rule">
                <span>{{ form.invoicemoney }}</span>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="开票单位" prop="rule">
                <span>{{ form.invoicekporg }}</span>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="开票日期" prop="rule">
                <span>{{ form.invoicedate }}</span>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="发票类型" prop="rule">
                <span>{{ form.invoicetype }}</span>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="发票状态" prop="rule">
                <span>{{ invoicestatusTextArr[form.invoicestatus + 1] }}</span>
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
  import { skgltosptzgl, tosptzglinfo } from '@/api/contract/financing'

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
    watch: {
      // imgurl: {
      //   handler(val) {
      //     this.imgurl = this.imgurl
      //   },
      // },
      list1: {
        handler(val) {
          this.listLoading = true
          this.getList2()
        },
      },
    },
    methods: {
      async getList() {
        this.listLoading = true
        // this.queryForm.negotiaId = this.netotiaId
        const { data } = await tosptzglinfo({
          collectionId: this.form.collectionid,
          flowid: '765525',
        })

        this.list1 = data.cy
        this.listLoading = false
        //
      },
      async getList2() {
        this.listLoading = true
        // this.queryForm.negotiaId = this.netotiaId
        if (this.list1 && this.list1.taskid) {
          const { data, url } = await skgltosptzgl({
            collectionId: this.form.collectionid,
            flowid: '765525',
            taskId: this.list.businesskey,
          })
          this.list = data
          this.imgurl = url + `?timestamp=${new Date().getTime()}`
          this.listLoading = false
        }

        //
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
        this.form = Object.assign({}, row)
        this.form.bankbankid = row.orgbank
        this.form.bankid = row.bankid
        this.dialogFormVisible = true
        this.getList()
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
