<template>
  <el-dialog
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
    :close-on-click-modal="false"
  >
    <el-row :gutter="15">
      <el-form ref="form" label-width="100px" :model="formData" :rules="rules">
        <el-col :span="12">
          <el-form-item label="提醒时间" prop="date">
            <el-date-picker
              v-model="formData.date"
              clearable
              :picker-options="pickerOptions"
              placeholder="请输入提醒时间"
              :style="{ width: '100%' }"
              type="datetime"
              value-format="yyyy-MM-dd HH:mm:ss"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="提醒内容" prop="content">
            <UEditor
              v-model="formData.content"
              :height="300"
              placeholder="提醒内容"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-divider>合同基本信息</el-divider>
        </el-col>
        <el-col :span="12">
          <el-form-item label="合同编号" prop="contractno">
            <el-input
              v-model="row.contractno"
              clearable
              placeholder="请输入合同编号"
              readonly
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="合同名称" prop="contractname">
            <el-input
              v-model="row.contractname"
              clearable
              disabled
              placeholder="请输入合同名称"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="合同开始时间" prop="startdate">
            <el-date-picker
              v-model="row.startdate"
              clearable
              placeholder="请输入合同时间"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="合同结束时间" prop="enddate">
            <el-date-picker
              v-model="row.enddate"
              clearable
              placeholder="请输入合同时间"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="收付方向" prop="dctype">
            <el-input
              v-model="row.dctype"
              clearable
              placeholder="请输入合同名称"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="收付方向" prop="contractmoney">
            <el-input
              v-model="row.contractmoney"
              clearable
              placeholder="请输入合同名称"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="合同主要内容" prop="describe">
            <el-input
              v-model="formData.describe"
              :autosize="{ minRows: 4, maxRows: 4 }"
              placeholder="请输入合同主要内容"
              :style="{ width: '100%' }"
              type="textarea"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-divider>合同履行阶段</el-divider>
        </el-col>
        <el-col :span="24">
          <el-table :data="row.planNodeList">
            <el-table-column
              align="center"
              label="合同内容"
              prop="nodecontent"
            />
            <el-table-column
              align="center"
              label="预计开始时间"
              prop="planstartdate"
            />
            <el-table-column
              align="center"
              label="预计结束时间"
              prop="planenddate"
            />
            <el-table-column
              align="center"
              label="预计付款时间"
              prop="nodeplanpaydate"
            />
            <el-table-column align="center" label="付款比例" prop="nodepost" />
          </el-table>
        </el-col>
      </el-form>
    </el-row>
    <template #footer>
      <el-button @click="close">取 消</el-button>
      <el-button type="primary" @click="save">确 定</el-button>
    </template>
  </el-dialog>
</template>

<script>
  import { remindContract } from '@/api/contract/manage'
  import UEditor from '@/components/UEditor'

  export default {
    name: 'TemplateEdit',
    components: { UEditor },
    data() {
      return {
        formData: {
          date: undefined,
          content: undefined,
        },
        rules: {
          date: [
            {
              required: true,
              message: '请选择提醒时间',
              trigger: 'blur',
            },
          ],
          content: [
            {
              required: true,
              message: '请输入提醒内容',
              trigger: 'blur',
            },
          ],
        },
        title: '设置',
        dialogFormVisible: false,
        options: [],
        row: {},
      }
    },
    computed: {
      pickerOptions() {
        return {
          disabledDate(time) {
            return time.getTime() < Date.now()
          },
        }
      },
    },
    watch: {
      'formData.describe'(val) {
        if (this.$refs['ueditor'].editor.openTemplate) {
          this.$refs['ueditor'].editor.openTemplate = false
          let s = val
          const arr = [
            ['$[contract.contractno]', 'contractno'],
            ['$[contract.contractname]', 'contractname'],
            ['$[contract.contractamount]', 'contractmoney'],
            ['$[contract.contractItem]', 'contractitem'],
            ['$[contract.executor]', 'realname'],
            ['$[contract.rmbinwords]', 'hzsumowing'],
            ['$[counterpart.coupersion]', 'counterpartcode'],
            ['$[counterpart.personincharge]', 'contractbd'],
            ['$[counterpart.counterpartHank]', 'bankkhyh'],
            ['$[counterpart.counumber]', 'counterpartno'],
            ['$[counterpart.couname]', 'budgetname'],
            ['$[counterpart.couaddress]', 'counterpartaddress'],
            ['$[counterpart.coupersion]', 'contacts'],
            ['$[counterpart.contactsPhone]', 'contactsphone'],
            ['$[counterpart.counterpartHankAccount]', 'bankaccount'],
            ['$[counterpart.legarepresentative]', 'contacts'],
            ['$[counterpart.pctelephonenumber]', 'contractzd'],
            // ['$[counterpart.taxpayeridentification]', 'hzsumowing'], //纳税人识别号
          ]
          arr.forEach((i) => {
            if (this.formData[i[1]]) {
              s = s.replace(i[0], this.formData[i[1]])
            }
          })
          this.formData.content = s
        }
      },
    },
    created() {},

    methods: {
      showEdit(row) {
        this.row = row
        this.dialogFormVisible = true
      },
      close() {
        this.$refs['form'].resetFields()
        this.form = this.$options.data().form
        this.dialogFormVisible = false
      },
      //保存
      save() {
        this.$refs['form'].validate(async (valid) => {
          if (valid) {
            const { msg } = await remindContract({
              ...this.formData,
              tcu: this.row,
            })
            this.$baseMessage(msg, 'success', 'vab-hey-message-success')
            this.$emit('fetch-data')
            this.close()
          }
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
