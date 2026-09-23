<template>
  <div>
    <el-row v-if="show == 0" :gutter="15">
      <el-form
        ref="form"
        label-width="140px"
        :model="form"
        :rules="rules"
        :disabled="alldisabled"
      >
        <el-col :span="12">
          <el-form-item label="收款合同" prop="contractname">
            <el-input
              v-model.trim="form.contractname"
              clearable
              placeholder="请选择收款合同"
              readonly
              :style="{ width: '256px' }"
            />
            <el-button
              :style="{ marginLeft: '10px', position: 'absolute' }"
              type="primary"
              @click="$refs.skht.show()"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="收款日期" prop="collectionskdate">
            <el-date-picker
              v-model.trim="form.collectionskdate"
              clearable
              format="yyyy-MM-dd"
              placeholder="请输入收款日期"
              :style="{ width: '100%' }"
              value-format="yyyy-MM-dd"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="合同编号" prop="contractno">
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
          <el-form-item label="付款单位" prop="budgetname">
            <el-input
              v-model.trim="form.budgetname"
              clearable
              placeholder="请输入付款单位"
              readonly
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="对应收款项" prop="nodecontent">
            <el-input
              v-model.trim="form.nodecontent"
              clearable
              placeholder="请选择对应收款项"
              readonly
              :style="{ width: '256px' }"
            />
            <el-button
              :style="{ marginLeft: '10px', position: 'absolute' }"
              type="primary"
              @click="showht('dykx')"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="对应收款金额" prop="nodemoney">
            <el-input
              v-model.trim="form.nodemoney"
              clearable
              placeholder="请输入对应收款金额"
              readonly
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="付款银行账号" prop="bankaccount">
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
              @click="showht('yhzh')"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="收款银行账号" prop="bankaccnum">
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
              @click="showht('yhzhsk')"
            >
              选择
            </el-button>
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
              @click="showht('fpxx')"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="开票金额" prop="invoicemoney">
            <el-input
              v-model="form.invoicemoney"
              clearable
              placeholder="请输入开票金额"
              readonly
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="开票单位" prop="invoicekporg">
            <el-input
              v-model.trim="form.invoicekporg"
              clearable
              placeholder="请输入开票单位"
              readonly
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="开票日期" prop="invoicedate">
            <el-input
              v-model.trim="form.invoicedate"
              clearable
              placeholder="请输入开票日期"
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
              readonly
              :style="{ width: '100%' }"
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
      </el-form>
    </el-row>
    <div style="text-align: right" v-if="!alldisabled">
      <!-- <el-button @click="close">取 消</el-button> -->
      <el-button v-if="show == 0" type="primary" @click="save">确 定</el-button>
      <el-button @click="ymsubmit" type="primary" :disabled="btnLoading">
        提交
      </el-button>
    </div>
    <skht-options ref="skht" @selectedskht="handleskht" />
    <dykx-options ref="dykx" @selecteddykx="handledykx" />
    <yhzh-options ref="yhzh" @selectedyhzh="handleyhzh" />
    <yhzhsk-options ref="yhzhsk" @selectedyhzhsk="handleyhzhsk" />
    <fpxx-options ref="fpxx" @selectedfpxx="handlefpxx" />
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
    saveCollectionManagemen,
    viewCollectionManagemen,
  } from '@/api/contract/financing'
  import skhtOptions from '@/views/contract/financing/components/options/skht.vue'
  import dykxOptions from '@/views/contract/financing/components/options/dykx.vue'
  import yhzhOptions from '@/views/contract/financing/components/options/yhzh.vue'
  import fpxxOptions from '@/views/contract/financing/components/options/fpxx.vue'
  import yhzhskOptions from '@/views/contract/financing/components/options/yhzhsk.vue'
  import Resubmit from '@/views/msg/components/options/Resubmit.vue'
  export default {
    name: 'Skgl',
    components: {
      skhtOptions,
      dykxOptions,
      yhzhOptions,
      fpxxOptions,
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
          collectionid: undefined,
          contractname: undefined,
          collectionskdate: undefined,
          contractno: undefined,
          budgetid: undefined,
          budgetname: undefined,
          nodecontent: undefined,
          nodemoney: undefined,
          bankbankid: undefined,
          bankaccnum: undefined,
          bankid: undefined,
          bankaccount: undefined,
          invoiceno: undefined,
          invoicekporg: undefined,
          invoicedate: undefined,
          invoicetype: undefined,
          invoicestatus: undefined,
          invoicestatusText: undefined,
          invoicemoney: undefined,
          orgBankid: undefined,
          counterBankid: undefined,
          invoiceid: undefined,
          contractid: undefined,
          nodeid: undefined,
        },
        rules: {
          contractname: [
            {
              required: true,
              message: '请输入合同名称',
              trigger: 'change',
            },
          ],
          collectionskdate: [
            {
              required: true,
              message: '请输入收款日期',
              trigger: 'change',
            },
          ],
          contractno: [
            {
              required: true,
              message: '请输入合同编号',
              trigger: 'change',
            },
          ],
          budgetname: [
            {
              required: true,
              message: '请输入付款单位',
              trigger: 'change',
            },
          ],
          nodecontent: [
            {
              required: true,
              message: '请输入对应收款项',
              trigger: 'change',
            },
          ],
          nodemoney: [
            {
              required: true,
              message: '请输入对应收款金额',
              trigger: 'change',
            },
          ],
          bankaccnum: [
            {
              required: true,
              message: '请输入收款账户',
              trigger: 'change',
            },
          ],
          bankaccount: [
            {
              required: true,
              message: '请输入付款账户',
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
          invoicemoney: [
            {
              required: true,
              message: '请输入开票金额',
              trigger: 'change',
            },
          ],
          invoicekporg: [
            {
              required: true,
              message: '请输入开票单位',
              trigger: 'change',
            },
          ],
          invoicedate: [
            {
              required: true,
              message: '请输入开票日期',
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
          invoicestatus: [
            {
              required: true,
              message: '请输入发票状态',
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
    created() {},
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
        this.form.collectionskdate = yy + '-' + mm + '-' + dd
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
        console.log(title, '1111')
        this.alldisabled = title == 'detail'
        this.time()

        this.fromId = formId
        this.flowtaskinfoflowid = flowtaskinfoflowid
        this.ymFromId = ymFromId
        this.status = status
        const { collection } = await viewCollectionManagemen({
          collectionId: formId,
        })
        Object.keys(this.form).forEach(
          (key) => (this.form[key] = collection[key])
        )
        this.form.budgetname = collection.budgetname || ''
        //收款银行ID
        this.form.bankid = collection.orgbank
        //付款银行ID
        this.form.bankbankid = collection.bankid
        this.listLoading = false
      },
      showDetail(row) {
        this.showEdit(row, true)
      },
      async fetchInfo(row) {
        this.listLoading = true
        const { collection } = await viewCollectionManagemen({
          collectionId: row.collectionid,
        })
        Object.keys(this.form).forEach(
          (key) => (this.form[key] = collection[key])
        )
        this.form.budgetname = collection.budgetname || ''
        //收款银行ID
        this.form.bankid = collection.orgbank
        //付款银行ID
        this.form.bankbankid = collection.bankid
        this.listLoading = false
      },
      close() {
        this.form = {
          collectionid: undefined,
          contractname: undefined,
          collectionskdate: undefined,
          contractno: undefined,
          budgetid: undefined,
          budgetname: undefined,
          nodecontent: undefined,
          nodemoney: undefined,
          bankbankid: undefined,
          bankaccnum: undefined,
          bankid: undefined,
          bankaccount: undefined,
          invoiceno: undefined,
          invoicekporg: undefined,
          invoicedate: undefined,
          invoicetype: undefined,
          invoicestatus: undefined,
          invoicestatusText: undefined,
          invoicemoney: undefined,
          orgBankid: undefined,
          counterBankid: undefined,
          invoiceid: undefined,
          contractid: undefined,
          nodeid: undefined,
        }
        this.$bus.$emit('updateMsg', 0)
      },
      save() {
        this.$refs['form'].validate(async (valid) => {
          if (valid) {
            const { msg } = await saveCollectionManagemen(this.form)
            this.$baseMessage(msg, 'success', 'vab-hey-message-success')
            this.$emit('fetch-data')
          }
        })
      },
      //前置校验
      showht(type) {
        if (!this.form.contractid) {
          this.$baseMessage('请先选择合同', 'error')
        } else {
          this.$refs[type].show(this.form.contractid, this.form.budgetid)
        }
      },
      // 合同
      handleskht(val) {
        //
        // budgetid 相对方ID，选择付款银行列表时，需要传入
        this.form.budgetid = val.budgetid
        this.form.contractid = val.contractid
        this.form.contractname = val.contractname
        this.form.collectionskdate = val.startdate
        this.form.contractno = val.contractno
        this.form.budgetname = val.budgetname
        if (val.bankid && val.bankaccount) {
          this.form.bankbankid = val.bankid
          this.form.bankaccount = val.bankaccount
        }
      },
      //对应收款项
      handledykx(val) {
        //
        this.form.nodecontent = val.nodecontent
        this.form.nodemoney = val.yfMoney
        this.form.nodeid = val.nodeid
      },
      //付款银行
      handleyhzh(val) {
        this.form.bankbankid = val.bankid
        this.form.bankaccount = val.bankaccount
      },
      //收款银行
      handleyhzhsk(val) {
        this.form.bankid = val.bankid
        this.form.bankaccnum = val.bankaccnum
      },
      //发票
      handlefpxx(val) {
        //
        this.form.invoiceid = val.invoiceid
        this.form.invoiceno = val.invoiceno
        this.form.invoicemoney = val.invoicemoney
        this.form.invoicekporg = val.invoicekporg
        this.form.invoicedate = val.invoicedate
        this.form.invoicetype = val.invoicetype
        this.form.invoicestatus = val.invoicestatus
        //1-未开票，2-已开票，3-未收款，4-已收款，5-已退票
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
