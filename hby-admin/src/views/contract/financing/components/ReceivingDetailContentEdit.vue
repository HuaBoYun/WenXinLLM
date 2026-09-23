<template>
  <div>
    <el-row v-if="show == 0" :gutter="15">
      <el-form ref="form" label-width="140px" :model="form" :rules="rules">
        <el-col :span="12">
          <el-form-item label="收款合同" prop="contractname">
            <el-input
              v-model.trim="form.contractname"
              clearable
              placeholder="请选择收款合同"
              :style="{ width: '256px' }"
              :disabled="true"
            />
            <el-button
              :style="{ marginLeft: '10px', position: 'absolute' }"
              type="primary"
              @click="$refs.skht.show()"
              :disabled="true"
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
              :style="{ width: '100%' }"
              :disabled="true"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="付款单位" prop="collectionorgname">
            <el-input
              v-model.trim="form.collectionorgname"
              clearable
              placeholder="请输入付款单位"
              :style="{ width: '100%' }"
              :disabled="true"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="对应收款项" prop="nodecontent">
            <el-input
              v-model.trim="form.nodecontent"
              clearable
              placeholder="请选择对应收款项"
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
          <el-form-item label="发票状态" prop="invoicestatus">
            <el-input
              clearable
              placeholder="请输入发票状态"
              :style="{ width: '100%' }"
              :value="invoicestatusTextArr[form.invoicestatus - 1]"
            />
          </el-form-item>
        </el-col>
      </el-form>
    </el-row>
    <el-row v-if="show == 1" :gutter="15">
      <el-form ref="form" label-width="140px" :model="form" :rules="rules">
        <el-col :span="12">
          <el-form-item label="收款合同" prop="contractname">
            <span>{{ form.contractname }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="收款日期" prop="collectionskdate">
            <span>{{ form.collectionskdate }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="合同编号" prop="contractno">
            <span>{{ form.contractno }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="付款单位" prop="budgetname">
            <span>{{ form.budgetname }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="对应收款项" prop="nodecontent">
            <span>{{ form.nodecontent }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="对应收款金额" prop="nodemoney">
            <span>{{ form.nodemoney }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="付款银行账号" prop="bankaccount">
            <span>{{ form.bankaccount }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="收款银行账号" prop="bankaccnum">
            <span>{{ form.bankaccnum }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="发票号" prop="invoiceno">
            <span>{{ form.invoiceno }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="开票金额" prop="invoicemoney">
            <span>{{ form.invoicemoney }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="开票单位" prop="invoicekporg">
            <span>{{ form.invoicekporg }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="开票日期" prop="invoicedate">
            <span>{{ form.invoicedate }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="发票类型" prop="invoicetype">
            <span>{{ form.invoicetype }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="发票状态" prop="invoicestatus">
            <!-- 1-未开票，2-已开票，3-未收款，4-已收款，5-已退票 -->
            <span>{{ invoicestatusTextArr[form.invoicestatus - 1] }}</span>
            <!-- <span>{{ form.invoicestatus }}</span> -->
          </el-form-item>
        </el-col>
      </el-form>
    </el-row>
    <div style="text-align: right; margin-top: 10px">
      <!-- <el-button @click="close">取 消</el-button> -->
      <el-button
        v-if="show == 0"
        type="primary"
        @click="save"
        :disabled="changeSaveBtn"
      >
        确 定
      </el-button>
    </div>
    <skht-options ref="skht" @selectedskht="handleskht" />
    <dykx-options ref="dykx" @selecteddykx="handledykx" />
    <yhzh-options ref="yhzh" @selectedyhzh="handleyhzh" />
    <yhzhsk-options ref="yhzhsk" @selectedyhzhsk="handleyhzhsk" />
    <fpxx-options ref="fpxx" @selectedfpxx="handlefpxx" />
  </div>
</template>

<script>
  import {
    saveCollectionManagemen,
    viewCollectionManagemen,
  } from '@/api/contract/financing'
  import skhtOptions from './options/skht.vue'
  import dykxOptions from './options/dykx.vue'
  import yhzhOptions from './options/yhzh.vue'
  import fpxxOptions from './options/fpxx.vue'
  import yhzhskOptions from './options/yhzhsk.vue'
  export default {
    name: 'DraftEdit',
    components: {
      skhtOptions,
      dykxOptions,
      yhzhOptions,
      fpxxOptions,
      yhzhskOptions,
    },
    props: {
      formData: {
        type: Object,
        default: () => {},
      },
    },
    data() {
      return {
        changeSaveBtn: false,
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
          collectionorgname: undefined,
        },
        rules: {
          contractname: [
            {
              required: true,
              message: '请输入合同名称',
              trigger: 'blur',
            },
          ],
          collectionskdate: [
            {
              required: true,
              message: '请输入收款日期',
              trigger: 'blur',
            },
          ],
          contractno: [
            {
              required: true,
              message: '请输入合同编号',
              trigger: 'blur',
            },
          ],
          budgetname: [
            {
              required: true,
              message: '请输入付款单位',
              trigger: 'blur',
            },
          ],
          nodecontent: [
            {
              required: true,
              message: '请输入对应收款项',
              trigger: 'blur',
            },
          ],
          nodemoney: [
            {
              required: true,
              message: '请输入对应收款金额',
              trigger: 'blur',
            },
          ],
          bankaccnum: [
            {
              required: true,
              message: '请输入收款账户',
              trigger: 'blur',
            },
          ],
          bankaccount: [
            {
              required: true,
              message: '请输入付款账户',
              trigger: 'blur',
            },
          ],
          invoiceno: [
            {
              required: true,
              message: '请输入发票号',
              trigger: 'blur',
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
              trigger: 'blur',
            },
          ],
          invoicedate: [
            {
              required: true,
              message: '请输入开票日期',
              trigger: 'blur',
            },
          ],
          invoicetype: [
            {
              required: true,
              message: '请输入发票类型',
              trigger: 'blur',
            },
          ],
          invoicestatus: [
            {
              required: true,
              message: '请输入发票状态',
              trigger: 'blur',
            },
          ],
        },
        title: '',
        dialogFormVisible: false,
        radio: '',
        show: 0,
      }
    },
    mounted() {
      this.showEdit(this.formData)

      this.changeSaveBtn = false
    },
    created() {
      this.$bus.$off('changeSaveBtn').$on('changeSaveBtn', () => {
        this.changeSaveBtn = true
      })
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
        this.form.collectionskdate = yy + '-' + mm + '-' + dd
      },
      showEdit(row, disabled) {
        this.show = 0
        this.time()
        if (!row) {
          this.title = '添加'
        } else {
          if (disabled) {
            this.title = '查看'
            this.show = 1
          } else {
            this.title = '编辑'
          }
          Object.keys(this.form).forEach((key) => {
            this.form[key] = row[key]
          })
          //收款银行ID
          this.form.bankid = row.orgbank
          //付款银行ID
          this.form.bankbankid = row.bankid
        }
        this.dialogFormVisible = true
      },
      showDetail(row) {
        this.showEdit(row, true)
      },
      //数据回填
      async fetchInfo(row) {
        this.listLoading = true
        const { collection } = await viewCollectionManagemen({
          collectionId: row.collectionid,
        })
        Object.keys(this.form).forEach(
          (key) => (this.form[key] = collection[key])
        )

        this.form.budgetname = row.collectionorgname
        // this.total = totalRecord
        this.listLoading = false
      },
      close() {
        this.$refs['form'].resetFields()
        this.form = this.$options.data().form
        this.dialogFormVisible = false
      },
      save() {
        this.$refs['form'].validate(async (valid) => {
          if (valid) {
            if (this.title === '添加') {
              const { msg } = await saveCollectionManagemen(this.form)
              this.$baseMessage(msg, 'success', 'vab-hey-message-success')
            } else if (this.title === '编辑') {
              const { msg } = await saveCollectionManagemen(this.form)
              this.$baseMessage(msg, 'success', 'vab-hey-message-success')
            }
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
        this.form.invoiceid = val.invoiceid
        this.form.invoiceno = val.invoiceno
        this.form.invoicemoney = val.invoicemoney
        this.form.invoicekporg = val.invoicekporg
        this.form.invoicedate = val.invoicedate
        this.form.invoicetype = val.invoicetype
        this.form.invoicestatus = val.invoicestatus
        //1-未开票，2-已开票，3-未收款，4-已收款，5-已退票
      },
    },
  }
</script>
<style scoped>
  .formula .el-form-item--small.el-form-item {
    margin-bottom: 5px;
  }
</style>
