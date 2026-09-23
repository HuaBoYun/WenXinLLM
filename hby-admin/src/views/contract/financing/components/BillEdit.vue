<template>
  <el-dialog
    :close-on-click-modal="false"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
  >
    <el-row v-if="show == 0" :gutter="15">
      <el-form ref="form" label-width="140px" :model="form" :rules="rules">
        <el-col :span="12">
          <el-form-item label="票据相对方" prop="budgetName">
            <el-input
              v-model.trim="form.budgetName"
              clearable
              placeholder="请选择票据相对方"
              readonly
              :style="{ width: 'calc(100% - 66px)' }"
            />
            <el-button
              :style="{ marginLeft: '10px', position: 'absolute' }"
              type="primary"
              @click="$refs.xdf.show()"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="纳税人识别号" prop="tinumber">
            <el-input
              v-model.trim="form.tinumber"
              clearable
              placeholder="请输入纳税人识别号"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="发票编号" prop="invoiceno">
            <el-input
              v-model="form.invoiceno"
              clearable
              placeholder="请输入发票编号"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="发票抬头" prop="invoiceheadtext">
            <el-input
              v-model="form.invoiceheadtext"
              clearable
              placeholder="请输入发票抬头"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="发票金额" prop="invoicemoney">
            <el-input
              v-model="form.invoicemoney"
              clearable
              placeholder="请输入发票金额"
              :style="{ width: '100%' }"
              type="number"
              @blur="invoicemoneyNum()"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="税率（%）" prop="invoicepost">
            <el-input
              v-model="form.invoicepost"
              clearable
              placeholder="请输入税率（%）"
              :style="{ width: '100%' }"
              type="number"
              @blur="invoicemoneyNum()"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="无税金额">
            <el-input
              v-model="form.wushuijine"
              clearable
              placeholder="请输入无税金额"
              readonly
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="税额">
            <el-input
              v-model="form.shuie"
              clearable
              placeholder="请输入税额"
              readonly
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="发票开具日期" prop="startdate1">
            <el-date-picker
              v-model.trim="form.startdate1"
              clearable
              format="yyyy-MM-dd"
              placeholder="请输入发票开具日期"
              :style="{ width: '100%' }"
              value-format="yyyy-MM-dd"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="收票日期" prop="enddate1">
            <el-date-picker
              v-model.trim="form.enddate1"
              clearable
              format="yyyy-MM-dd"
              placeholder="请输入收票日期"
              :style="{ width: '100%' }"
              value-format="yyyy-MM-dd"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="发票状态" prop="invoicestatus">
            <el-select
              v-model="form.invoicestatus"
              filterable
              placeholder="请选择发票状态"
              style="width: 100%"
            >
              <el-option
                v-for="item in options1"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="发票类型" prop="invoicetype">
            <el-select
              v-model="form.invoicetype"
              filterable
              placeholder="请选择发票类型"
              style="width: 100%"
            >
              <el-option
                v-for="item in options"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="收票单位">
            <el-input
              v-model="form.invoicesporg"
              clearable
              placeholder="请输入收票单位"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="开票单位">
            <el-input
              v-model="form.invoicekporg"
              clearable
              placeholder="请输入开票单位"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="发票内容">
            <el-input
              v-model="form.invoicecontent"
              clearable
              placeholder="请输入发票内容"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
      </el-form>
    </el-row>

    <el-row v-if="show == 1" :gutter="15">
      <el-form ref="form" label-width="140px" :model="form" :rules="rules">
        <el-col :span="12">
          <el-form-item label="票据相对方" prop="budgetName">
            <span>{{ form.budgetName }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="纳税人识别号" prop="tinumber">
            <span>{{ form.tinumber }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="发票编号" prop="invoiceno">
            <span>{{ form.invoiceno }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="发票抬头" prop="invoiceheadtext">
            <span>{{ form.invoiceheadtext }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="发票金额" prop="invoicemoney">
            <span>{{ form.invoicemoney }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="税率（%）" prop="invoicepost">
            <span>{{ form.invoicepost }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="无税金额">
            <span>{{ form.wushuijine }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="税额">
            <span>{{ form.shuie }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="发票开具日期" prop="startdate1">
            <span>{{ form.startdate1 }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="收票日期" prop="enddate1">
            <span>{{ form.enddate1 }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="发票状态" prop="invoicestatus">
            <span v-if="form.invoicestatus == 1">未开票</span>
            <span v-if="form.invoicestatus == 2">已开票</span>
            <span v-if="form.invoicestatus == 3">未收款</span>
            <span v-if="form.invoicestatus == 4">已收款</span>
            <span v-if="form.invoicestatus == 5">已退票</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="发票类型" prop="invoicetype">
            <span>{{ form.invoicetype }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="收票单位">
            <span>{{ form.invoicesporg }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="开票单位">
            <span>{{ form.invoicekporg }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="发票内容">
            <span>{{ form.invoicecontent }}</span>
          </el-form-item>
        </el-col>
      </el-form>
    </el-row>
    <template #footer>
      <el-button @click="close">取 消</el-button>
      <el-button v-if="show == 0" type="primary" @click="save">确 定</el-button>
    </template>
    <xdf-options ref="xdf" @selected="handleSsjd" />
  </el-dialog>
</template>

<script>
  import { saveInvoiceManageMen, viewInvoice } from '@/api/contract/financing'
  import xdfOptions from './options/xdf.vue'
  export default {
    name: 'DraftEdit',
    components: { xdfOptions },
    data() {
      return {
        form: {
          budgetId: undefined,
          budgetName: undefined,
          startdate1: undefined,
          enddate1: undefined,
          tinumber: undefined,
          invoiceno: undefined,
          invoiceheadtext: undefined,
          invoicemoney: undefined,
          invoicepost: undefined,
          wushuijine: undefined,
          shuie: undefined,
          invoicestatus: undefined,
          invoicetype: undefined,
          invoicesporg: undefined,
          invoicekporg: undefined,
          invoicecontent: undefined,
          invoiceid: undefined,
        },
        rules: {
          budgetName: [
            {
              required: true,
              message: '请输入票据相对方',
              trigger: 'blur',
            },
          ],
          tinumber: [
            {
              required: true,
              message: '请输入纳税人识别号',
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
          invoiceheadtext: [
            {
              required: true,
              message: '请输入发票抬头',
              trigger: 'blur',
            },
          ],
          invoicemoney: [
            {
              required: true,
              message: '请输入发票金额',
              trigger: 'blur',
            },
          ],
          invoicepost: [
            {
              required: true,
              message: '请输入税率',
              trigger: 'blur',
            },
          ],
          startdate1: [
            {
              required: true,
              message: '请输入发票开具日期',
              trigger: 'blur',
            },
          ],
          enddate1: [
            {
              required: true,
              message: '请输入收票日期',
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
          invoicetype: [
            {
              required: true,
              message: '请输入发票类型',
              trigger: 'blur',
            },
          ],
        },
        title: '',
        dialogFormVisible: false,
        options1: [
          {
            value: 1,
            label: '未开票',
          },
          {
            value: 2,
            label: '已开票',
          },
          {
            value: 5,
            label: '已退票',
          },
        ],
        options: [
          {
            value: '增值税专用发票',
            label: '增值税专用发票',
          },
          {
            value: '增值税普通发票',
            label: '增值税普通发票',
          },
        ],
        show: 0,
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
        this.form.startdate1 = yy + '-' + mm + '-' + dd
        this.form.enddate1 = yy + '-' + mm + '-' + dd
      },
      invoicemoneyNum() {
        //
        if (
          this.form.invoicemoney !== undefined &&
          this.form.invoicepost !== undefined
        ) {
          this.Num()
        }
      },
      Num() {
        //无税金额=发票金额/（1+税点/100) 保留两位小数
        this.form.wushuijine = (
          (this.form.invoicemoney * 100) /
          (100 + parseInt(this.form.invoicepost))
        ).toFixed(2)
        this.form.shuie = (
          this.form.invoicemoney - this.form.wushuijine
        ).toFixed(2)
      },
      showEdit(row) {
        this.show = 0
        this.time()
        if (!row) {
          this.title = '添加'
        } else {
          this.title = '编辑'
          Object.keys(this.form).forEach((key) => {
            this.form[key] = row[key]
          })
          this.invoicemoneyNum()
          this.form.startdate1 = row.invoicedate
          this.form.enddate1 = row.invoicespdate
          // 1-未开票，2-已开票，3-未收款，4-已收款，5-已退票
          // if (this.form.invoicestatus == 1) {
          //   this.form.invoicestatus = '未开票'
          // } else if (this.form.invoicestatus == 2) {
          //   this.form.invoicestatus = '已开票'
          // } else if (this.form.invoicestatus == 3) {
          //   this.form.invoicestatus = '未收款'
          // } else if (this.form.invoicestatus == 4) {
          //   this.form.invoicestatus = '已收款'
          // } else if (this.form.invoicestatus == 5) {
          //   this.form.invoicestatus = '已退票'
          // }
        }
        this.dialogFormVisible = true
      },
      showDetail(row) {
        this.form = row
        this.form.startdate1 = row.invoicedate
        this.form.enddate1 = row.invoicespdate
        this.title = '查看'
        this.show = 1
        this.invoicemoneyNum()
        this.dialogFormVisible = true
        // this.fetchInfo()
      },
      async fetchInfo() {
        this.listLoading = true
        const { invoice } = await viewInvoice({
          invoiceId: this.form.invoiceid,
        })
        this.form = invoice
        this.invoicemoneyNum()
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
            const { msg } = await saveInvoiceManageMen(this.form)
            this.$baseMessage(msg, 'success', 'vab-hey-message-success')
            this.$emit('fetch-data')
            this.close()
          }
        })
      },
      //回调
      handleSsjd(val) {
        this.form.budgetId = val.budgetid
        this.form.budgetName = val.budgetname
        this.form.tinumber = val.resultdescription
      },
    },
  }
</script>
<style scoped>
  .formula .el-form-item--small.el-form-item {
    margin-bottom: 5px;
  }
</style>
