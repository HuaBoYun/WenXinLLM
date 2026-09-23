<template>
  <div>
    <el-row v-if="show === 0" :gutter="15">
      <el-form
        ref="form"
        label-width="140px"
        :model="form"
        :rules="rules"
        :disabled="alldisabled"
      >
        <el-col :span="24">
          <el-form-item label="标题" prop="paymenttitle">
            <el-input
              v-model.trim="form.paymenttitle"
              clearable
              placeholder="请输入标题"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="经办人" prop="realname">
            <el-input
              v-model.trim="form.realname"
              clearable
              placeholder="请选择经办人"
              readonly
              :style="{ width: '256px' }"
            />
            <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              @click="$refs.executor.show()"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="申请部门" prop="orgname">
            <el-input
              v-model.trim="form.orgname"
              clearable
              placeholder="请选择申请部门"
              readonly
              :style="{ width: '256px' }"
            />
            <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              @click="$refs.department.show()"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="合同名称" prop="contractname">
            <el-input
              v-model.trim="form.contractname"
              clearable
              placeholder="请选择合同名称"
              readonly
              :style="{ width: '256px' }"
            />
            <el-button
              :style="{ marginLeft: '10px', position: 'absolute' }"
              type="primary"
              @click="$refs.htxx.show()"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="申请日期" prop="applyDateStr">
            <el-date-picker
              v-model.trim="form.applyDateStr"
              clearable
              format="yyyy-MM-dd"
              placeholder="请输入申请日期"
              :style="{ width: '100%' }"
              value-format="yyyy-MM-dd"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="合同编号">
            <el-input
              v-model.trim="form.contractno"
              clearable
              placeholder="请输入合同编号"
              readonly
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="合同总金额">
            <el-input
              v-model.trim="form.contractmoney"
              clearable
              placeholder="请输入合同总金额"
              readonly
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="已付款金额">
            <el-input
              v-model.trim="form.paymenmoney"
              clearable
              placeholder="请输入已付款金额"
              readonly
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="未付款金额">
            <el-input
              v-model.trim="form.noPaymoney"
              clearable
              placeholder="请输入未付款金额"
              readonly
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="执行人">
            <el-input
              v-model.trim="form.executestaff"
              clearable
              placeholder="请输入执行人"
              readonly
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="合同相对方">
            <el-input
              v-model.trim="form.counterpartno"
              clearable
              placeholder="请输入合同相对方"
              readonly
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="付款银行账号">
            <el-input
              v-model.trim="form.bankaccount"
              clearable
              placeholder="请选择付款银行账号"
              readonly
              :style="{ width: '256px' }"
            />
            <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              @click="$refs.yhzhsk.show()"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="收款银行账号">
            <el-input
              v-model.trim="form.bankaccnum"
              clearable
              placeholder="请选择收款银行账号"
              readonly
              :style="{ width: '256px' }"
            />
            <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              @click="
                !form.contractid
                  ? $message.error('请先选择对应合同')
                  : $refs.yhzh.show(form.contractid, form.budgetid)
              "
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="付款计划" prop="nodecontent">
            <el-input
              v-model.trim="form.nodecontent"
              clearable
              placeholder="请选择付款计划"
              readonly
              :style="{ width: '256px' }"
            />
            <el-button
              :style="{ marginLeft: '10px', position: 'absolute' }"
              type="primary"
              @click="
                !form.contractid
                  ? $message.error('请先选择对应合同')
                  : $refs.lxxx.show(form.contractid)
              "
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="付款金额">
            <el-input
              v-model.trim="form.nodemoney"
              clearable
              placeholder="请输入付款金额"
              readonly
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="发票号" prop="invoiceno">
            <el-input
              v-model.trim="form.invoiceno"
              clearable
              placeholder="请选择发票号"
              readonly
              :style="{ width: '256px' }"
            />
            <el-button
              :style="{ marginLeft: '10px', position: 'absolute' }"
              type="primary"
              @click="
                !form.contractid
                  ? $message.error('请先选择对应合同')
                  : $refs['fpxx'].show(form.contractid, form.budgetid)
              "
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="开票金额" prop="paymentmoney">
            <el-input
              v-model.trim="form.paymentmoney"
              clearable
              placeholder="请输入开票金额"
              readonly
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="发票类型" prop="invoicetype">
            <el-input
              v-model.trim="form.invoicetype"
              clearable
              placeholder="请输入发票类型"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="开票日期" prop="invoicedate">
            <!-- <el-input
              v-model.trim="form.invoicedate"
              clearable
              placeholder="请输入开票日期"
              :style="{ width: '100%' }"
            /> -->
            <el-date-picker
              v-model.trim="form.invoicedate"
              clearable
              format="yyyy-MM-dd"
              placeholder="请输入开票日期"
              :style="{ width: '100%' }"
              value-format="yyyy-MM-dd"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="发票状态" prop="invoicestatus">
            <el-input
              clearable
              placeholder="请输入发票状态"
              readonly
              :style="{ width: '100%' }"
              :value="invoicestatusTextArr[form.invoicestatus - 1]"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="款项类别" prop="paymentrecord">
            <el-input
              v-model.trim="form.paymentrecord"
              clearable
              placeholder="请输入款项类别"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="付款方式" prop="paymenttype">
            <el-input
              v-model.trim="form.paymenttype"
              clearable
              placeholder="请输入付款方式"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="最晚付款日期" prop="payLateDateStr">
            <!-- <el-input
              v-model.trim="form.paymentlatedate"
              clearable
              placeholder="请输入最晚付款日期"
              :style="{ width: '100%' }"
            /> -->
            <el-date-picker
              v-model.trim="form.payLateDateStr"
              clearable
              format="yyyy-MM-dd"
              placeholder="请输入最晚付款日期"
              :style="{ width: '100%' }"
              value-format="yyyy-MM-dd"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="备注">
            <el-input
              v-model.trim="form.paymentmemo"
              clearable
              placeholder="请输入备注"
              :style="{ width: '100%' }"
              type="textarea"
            />
          </el-form-item>
        </el-col>
      </el-form>
    </el-row>
    <div style="text-align: right" v-if="!alldisabled">
      <!-- <el-button @click="close">取 消</el-button> -->
      <el-button v-if="show == 0" type="primary" @click="save">确 定</el-button>
      <el-button @click="ymsubmit" type="primary" :disabled="btnLoading">
        提交
      </el-button>
    </div>
    <htxx-options ref="htxx" @selectedhtxx="handlehtxx" />
    <lxxx-options ref="lxxx" @selectedlxxx="handlelxxx" />
    <yhzh-options ref="yhzh" @selectedyhzh="handleyhzh" />
    <yhzhsk-options ref="yhzhsk" @selectedyhzhsk="handleyhzhsk" />
    <fpxx-options ref="fpxx" @selectedfpxx="handlefpxx" />
    <executor-options ref="executor" @selected="handleSelected" />
    <!-- <department-options ref="department" @selectedde="handledepartment" /> -->
    <SealDepartment ref="department" @selected="handledepartment" />
    <Resubmit
      ref="resubmit"
      :flowtaskinfoflowid="flowtaskinfoflowid"
      :fromId="fromId"
      :ymFromId="ymFromId"
      :fromIdcopy="fromIdcopy"
      @fetchClose="close"
      :status="status"
    />
  </div>
</template>

<script>
  import {
    savePaymentManagemen,
    viewPaymentManagemen,
  } from '@/api/contract/financing'
  import ExecutorOptions from '@/views/contract/financing/components/options/executor.vue'
  import fpxxOptions from '@/views/contract/financing/components/options/fpxx.vue'
  import htxxOptions from '@/views/contract/financing/components/options/htxx.vue'
  import lxxxOptions from '@/views/contract/financing/components/options/lxxx.vue'
  import yhzhOptions from '@/views/contract/financing/components/options/yhzh.vue'
  import yhzhskOptions from '@/views/contract/financing/components/options/yhzhsk.vue'
  // import departmentOptions from './options/department.vue'
  import SealDepartment from '@/views/contract/contractManage/components/options/sealDepartment.vue'
  import Resubmit from '@/views/msg/components/options/Resubmit.vue'
  export default {
    name: 'DraftEdit',
    components: {
      htxxOptions,
      lxxxOptions,
      yhzhOptions,
      fpxxOptions,
      ExecutorOptions,
      SealDepartment,
      yhzhskOptions,
      Resubmit,
    },
    data() {
      return {
        invoicestatusTextArr: [
          '未开票',
          '已开票',
          '未收款',
          '已收款',
          '已退票',
        ],
        form: {
          paymenttitle: undefined,
          realname: undefined,
          orgname: undefined,
          contractname: undefined,
          applydate: undefined,
          contractno: undefined,
          contractmoney: undefined,
          paymenmoney: undefined,
          noPaymoney: undefined,
          executestaff: undefined,
          counterpartno: undefined,
          bankaccnum: undefined,
          bankaccount: undefined,
          nodecontent: undefined,
          nodemoney: undefined,
          invoiceno: undefined,
          invoicetype: undefined,
          applyDateStr: undefined,
          invoicestatus: undefined,
          paymentrecord: undefined,
          paymenttype: undefined,
          payLateDateStr: undefined,
          paymentmemo: undefined,
          contractid: undefined,
          budgetid: undefined,
          applyStaffid: undefined,
          orgid: undefined,
          bankid: undefined,
          bankbankid: undefined,
          nodeid: undefined,
          invoiceid: undefined,
          applyOrgId: undefined,
          accumulatedpayments: undefined,
          invoicedate: undefined,
          paymentid: undefined,
        },
        rules: {
          paymenttitle: [
            {
              required: true,
              message: '请输入标题',
              trigger: 'change',
            },
          ],
          realname: [
            {
              required: true,
              message: '请输入经办人',
              trigger: 'change',
            },
          ],
          orgmeno: [
            {
              required: true,
              message: '请输入申请部门',
              trigger: 'change',
            },
          ],
          contractname: [
            {
              required: true,
              message: '请输入合同名称',
              trigger: 'change',
            },
          ],
          applydate: [
            {
              required: true,
              message: '请输入申请日期',
              trigger: 'change',
            },
          ],
          nodecontent: [
            {
              required: true,
              message: '请输入付款计划',
              trigger: 'change',
            },
          ],
          invoiceno: [
            {
              required: true,
              message: '请输入发票号',
              trigger: 'change',
            },
          ],
          paymenmoney: [
            {
              required: true,
              message: '请输入开票金额',
              trigger: 'change',
            },
          ],
          invoicetype: [
            {
              required: true,
              message: '请输入发票类型',
              trigger: 'change',
            },
          ],
          applyDateStr: [
            {
              required: true,
              message: '请输入开票日期',
              trigger: 'change',
            },
          ],
          invoicestatus: [
            {
              required: true,
              message: '请输入发票状态',
              trigger: 'change',
            },
          ],
          paymentrecord: [
            {
              required: true,
              message: '请输入款项类别',
              trigger: 'change',
            },
          ],
          paymenttype: [
            {
              required: true,
              message: '请输入付款方式',
              trigger: 'change',
            },
          ],
          payLateDateStr: [
            {
              required: true,
              message: '请输入最晚付款日期',
              trigger: 'change',
            },
          ],
        },
        title: '',
        dialogFormVisible: false,
        radio: '',
        show: 0,
        // 流程
        fromId: '',
        flowtaskinfoflowid: '',
        ymFromId: '',
        status: '',
        fromIdcopy: '',
        showMJ: false,
        MJoption: [],
        btnLoading: false,
        alldisabled: false,
      }
    },

    methods: {
      //时间处理
      time() {
        const yy = new Date().getFullYear()
        let mm = ''
        if (new Date().getMonth() + 1 < 10) {
          const mmm = new Date().getMonth() + 1
          mm = '0' + mmm
        } else {
          mm = new Date().getMonth() + 1
        }
        let dd = ''
        if (new Date().getDate() < 10) {
          const ddd = new Date().getDate()
          dd = '0' + ddd
        } else {
          dd = new Date().getDate()
        }
        this.form.applydate = yy + '-' + mm + '-' + dd
      },
      async showEdit(
        title,
        formId,
        flowtaskinfoflowid,
        ymFromId,
        isWfqdedit,
        status,
        nextNodeName,
        flowType
      ) {
        this.alldisabled = title == 'detail'
        this.time()

        this.fromId = formId
        this.flowtaskinfoflowid = flowtaskinfoflowid
        this.ymFromId = ymFromId
        this.status = status
        const { payment } = await viewPaymentManagemen({
          paymentId: formId,
        })

        Object.keys(this.form).forEach((key) => (this.form[key] = payment[key]))
        this.form.applyOrgId = payment.aPOORGID || ''
        this.form.orgname = payment.aPOORGNAME || ''
        this.form.orgid = payment.aPSSTAFFID || ''
        this.form.realname = payment.aPSREALNAME || ''
        this.form.paymenmoney = payment.paymenmoney || 0
        this.form.noPaymoney = payment.noPaymoney || 0
        this.form.paymentmoney = payment.invoicemoney || 0
        this.form.executestaff = payment.realname || ''
        this.form.applyDateStr = payment.applydate || ''
        this.form.payLateDateStr = payment.paymentlatedate || ''
        this.form.accumulatedpayments = payment.accumulatedpayments || 0
      },
      showDetail(row) {
        this.showEdit(row, true)
      },
      close() {
        this.form = {
          paymenttitle: undefined,
          realname: undefined,
          orgname: undefined,
          contractname: undefined,
          applydate: undefined,
          contractno: undefined,
          contractmoney: undefined,
          paymenmoney: undefined,
          noPaymoney: undefined,
          executestaff: undefined,
          counterpartno: undefined,
          bankaccnum: undefined,
          bankaccount: undefined,
          nodecontent: undefined,
          nodemoney: undefined,
          invoiceno: undefined,
          invoicetype: undefined,
          applyDateStr: undefined,
          invoicestatus: undefined,
          paymentrecord: undefined,
          paymenttype: undefined,
          payLateDateStr: undefined,
          paymentmemo: undefined,
          contractid: undefined,
          budgetid: undefined,
          applyStaffid: undefined,
          orgid: undefined,
          bankid: undefined,
          bankbankid: undefined,
          nodeid: undefined,
          invoiceid: undefined,
          applyOrgId: undefined,
          accumulatedpayments: undefined,
          invoicedate: undefined,
          paymentid: undefined,
        }
        this.$bus.$emit('updateMsg', 0)
      },
      save() {
        this.$refs['form'].validate(async (valid) => {
          if (valid) {
            const { msg } = await savePaymentManagemen(this.form)
            this.$baseMessage(msg, 'success', 'vab-hey-message-success')

            // this.$emit('fetch-data')
            // this.close()
          }
        })
      },
      //合同
      handlehtxx(val) {
        this.form.budgetid = val.budgetid
        this.form.contractid = val.contractid
        this.form.contractname = val.contractname //合同名称
        // this.form.applydate = val.createtime //申请日期
        this.form.contractno = val.contractno //合同编号
        this.form.contractmoney = val.contractmoney //合同总金额
        this.form.paymenmoney = val.paymenmoney || 0 //已付款金额
        this.form.noPaymoney = (
          val.contractmoney - (val.paymenmoney || 0)
        ).toFixed(2) //未付款金额
        this.form.executestaff = val.realname //执行人
        this.form.counterpartno = val.counterpartno //合同相对方
        this.time()
      },
      //付款计划
      handlelxxx(val) {
        //
        this.form.nodecontent = val.nodecontent //付款计划
        this.form.nodeid = val.nodeid
        this.form.nodemoney = val.yfMoney //付款金额
      },
      //收款银行
      handleyhzh(val) {
        this.form.bankbankid = val.bankid
        this.form.bankaccnum = val.bankaccount
      },
      //付款银行
      handleyhzhsk(val) {
        this.form.bankid = val.bankid
        this.form.bankaccount = val.bankaccnum
      },
      //发票
      handlefpxx(val) {
        //1-未开票，2-已开票，3-未收款，4-已收款，5-已退票
        this.form.invoiceid = val.invoiceid
        this.form.invoiceno = val.invoiceno
        this.form.paymentmoney = val.invoicemoney
        this.form.invoicekporg = val.invoicekporg
        this.form.applyDateStr = val.applyDateStr
        this.form.invoicetype = val.invoicetype
        this.form.invoicestatus = val.invoicestatus
        this.form.invoicedate = val.invoicedate
      },
      //经办人
      handleSelected(val) {
        this.form.applyStaffId = val.staffid
        this.form.realname = val.realname
      },
      //部门
      handledepartment(val) {
        // this.form.orgname = val.text
        this.form.applyOrgId = val.id
        this.form.orgname = val.name
      },
      async ymsubmit() {
        try {
          this.$refs['form'].validate(async (valid) => {
            if (valid) {
              this.btnLoading = true
              this.$refs.resubmit.ymsubmit()
            }
          })
        } catch (error) {
          this.btnLoading = false
        }
      },
    },
  }
</script>
<style scoped>
  .formula .el-form-item--small.el-form-item {
    margin-bottom: 5px;
  }
</style>
