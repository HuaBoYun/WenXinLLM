<template>
  <el-dialog
    append-to-body
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
    :close-on-click-modal="false"
  >
    <el-row :gutter="15">
      <el-form ref="form" label-width="150px" :model="formData" :rules="rules">
        <el-col :span="12">
          <el-form-item label="预计开始时间" prop="planstartdate">
            <el-date-picker
              v-model="formData.planstartdate"
              clearable
              placeholder="请输入预计开始时间"
              :style="{ width: '100%' }"
              value-format="yyyy-MM-dd"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="预计结束时间" prop="planenddate">
            <el-date-picker
              v-model="formData.planenddate"
              clearable
              placeholder="请输入预计结束时间"
              :style="{ width: '100%' }"
              value-format="yyyy-MM-dd"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            label="落实人"
            prop="realname"
            :style="{ height: '28px' }"
          >
            <el-input
              v-model="formData.realname"
              clearable
              readonly
              placeholder="请选择落实人"
              :style="{ width: '256px', height: '28px' }"
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
          <el-form-item label="承办部门" prop="orgname">
            <el-input
              v-model="formData.orgname"
              clearable
              readonly
              placeholder="请选择承办部门"
            />
            <!-- <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              @click="$refs.department.show()"
            >
              选择
            </el-button> -->
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="'付款方向'" prop="performanceCategory">
            <el-select
              style="width: 100%"
              @change="handleChange"
              v-model="formData.performanceCategory"
              disabled
            >
              <el-option label="付款" value="1"></el-option>
              <el-option label="收款" value="2"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col
          v-if="dctype != '无' && formData.performanceCategory"
          :span="12"
        >
          <el-form-item
            :label="dctype + '比例(%)'"
            prop="nodepost"
            :rules="[
              {
                required: true,
                message: `请输入${this.dctype}比例`,
                trigger: 'blur',
              },
            ]"
          >
            <el-input
              v-model.number="formData.nodepost"
              clearable
              :placeholder="'请输入' + dctype + '比例'"
              :style="{ width: '100%' }"
              type="number"
              @blur="handleBlur"
            />
          </el-form-item>
        </el-col>
        <el-col
          v-if="dctype != '无' && formData.performanceCategory"
          :span="12"
        >
          <el-form-item :label="dctype + '金额(元)'" prop="nodemoney">
            <el-input
              v-model.number="formData.nodemoney"
              disabled
              :placeholder="'请输入' + dctype + '金额(元)'"
              :style="{ width: '100%' }"
              type="number"
            />
          </el-form-item>
        </el-col>
        <el-col
          v-if="dctype != '无' && formData.performanceCategory"
          :span="12"
        >
          <el-form-item
            :label="'预计' + dctype + '时间'"
            prop="nodeplanpaydate"
            :rules="[
              {
                required: true,
                message: `请选择预计${this.dctype}时间`,
                trigger: 'blur',
              },
            ]"
          >
            <el-date-picker
              v-model="formData.nodeplanpaydate"
              clearable
              :placeholder="'请输入' + '预计' + dctype + '时间'"
              :style="{ width: '100%' }"
              value-format="yyyy-MM-dd"
            />
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="履行类别" prop="goodsName">
            <el-select style="width: 100%" v-model="formData.goodsName">
              <el-option label="货物" value="货物"></el-option>
              <el-option label="工期" value="工期"></el-option>
              <el-option label="服务期" value="服务期"></el-option>
              <el-option label="交付成果" value="交付成果"></el-option>
              <el-option label="其它" value="其它"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <!-- <el-col v-if="formData.performanceCategory === '2'" :span="12">
          <el-form-item label="货物数量" prop="goodsCount ">
            <el-input
              v-model.number="formData.goodsCount"
              clearable
              :placeholder="'请输入货物数量'"
              :style="{ width: '100%' }"
              oninput="value=value.replace(/^(0+)|[^\d]+/g,'')"
            />
          </el-form-item>
        </el-col> -->
        <el-col :span="24">
          <el-form-item label="履行内容" prop="nodecontent">
            <el-input
              v-model="formData.nodecontent"
              :autosize="{ minRows: 4, maxRows: 4 }"
              placeholder="请输入履行内容"
              :style="{ width: '100%' }"
              type="textarea"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="相对方信息" prop="budgetName">
            <el-input
              v-model="formData.budgetName"
              clearable
              readonly
              placeholder="请选择相对方信息"
              :style="{ width: '90%' }"
            />
            <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              @click="$refs.XDF.showEdit(XDFList)"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
      </el-form>
      <!-- <department-options
        ref="department"
        @selected="handleDepartmentSelected"
      /> -->
      <SealDepartment ref="department" @selected="handleDepartmentSelected" />
      <executor-options ref="executor" @selected="handleExecutorSelected" />
      <XDFSelect ref="XDF" @selected="handleXDFSelected" />
    </el-row>
    <template #footer>
      <el-button @click="close">取 消</el-button>
      <el-button type="primary" @click="save">确 定</el-button>
    </template>
  </el-dialog>
</template>

<script>
  import { saveJd } from '@/api/contract/manage'
  // import DepartmentOptions from '../options/department.vue'
  // import ExecutorOptions from '../options/executor.vue'
  import ExecutorOptions from '@/components/CompanySelectUserByTree'
  import SealDepartment from '../options/sealDepartment.vue'
  import XDFSelect from './Jdxdf.vue'

  export default {
    name: 'JieduanEdit',
    components: { SealDepartment, ExecutorOptions, XDFSelect },
    inject: ['fatherFetchItem'],
    props: {
      contract: {
        type: Object,
        default: () => {
          return {
            informationList: [],
            nodeList: [],
          }
        },
      },
    },
    data() {
      // var valiNumberPass1 = (rule, value, callback) => {
      //   //包含小数的数字
      //   let reg = /^[+-]?(0|([1-9]\d*))(\.\d+)?$/g
      //   if (value === '') {
      //     callback(new Error('请输入内容'))
      //   } else if (!reg.test(value)) {
      //     callback(new Error('请输入数字'))
      //   } else {
      //     callback()
      //   }
      // }
      return {
        dctype: '',
        formData: {
          contractid: undefined,
          nodeid: undefined,
          planstartdate: undefined,
          planenddate: undefined,
          jbstaffid: undefined,
          realname: undefined,
          orgname: undefined,
          jbunitid: undefined,
          nodepost: undefined,
          nodemoney: undefined,
          nodeplanpaydate: null,
          nodecontent: undefined,
          goodsName: undefined,
          budgetName: undefined,
        },
        XDFList: [],
        rules: {
          performanceCategory: [
            {
              required: true,
              message: '请选择履行类别',
              trigger: 'change',
            },
          ],
          planstartdate: [
            {
              required: true,
              message: '请选择预计开始时间',
              trigger: 'blur',
            },
          ],
          planenddate: [
            {
              required: true,
              message: '请选择预计结束时间',
              trigger: 'blur',
            },
          ],
          realname: [
            {
              required: true,
              message: '请选择落实人',
              trigger: 'blur',
            },
          ],
          orgname: [
            {
              required: true,
              message: '请选择承办部门',
              trigger: 'blur',
            },
          ],
          // nodepost: [
          //   {
          //     required: true,
          //     message: `请输入${this.dctype}比例`,
          //     // validator: valiNumberPass1,
          //     trigger: 'blur',
          //   },
          // ],
          // nodemoney: [
          //   {
          //     required: true,
          //     message: `请输入${this.dctype}金额`,
          //     // validator: valiNumberPass1,
          //     trigger: 'blur',
          //   },
          // ],
          // nodeplanpaydate: [
          //   {
          //     required: true,
          //     message: `请选择预计${this.dctype}时间`,
          //     trigger: 'blur',
          //   },
          // ],
          nodecontent: [
            {
              required: true,
              message: '请输入履行内容',
              trigger: 'blur',
            },
          ],
        },
        title: '',
        dialogFormVisible: false,
      }
    },
    created() {},
    watch: {
      // 'formData.nodepost': function (val) {
      //   if (isNaN(val)) {
      //     this.$baseMessage('收款比例必须是数字', 'error')
      //   }
      // },
      // 'formData.nodemoney': function (val) {
      //   if (isNaN(val)) {
      //     this.$baseMessage('收款金额必须是数字', 'error')
      //   }
      // },
    },
    methods: {
      //回调
      handleChange(e) {
        if (e !== '2') {
          this.formData.goodsName = undefined
          this.formData.goodsCount = undefined
        }
        this.$refs['form'].clearValidate()
      },
      showEdit(row) {
        const userInfo = JSON.parse(localStorage.getItem('userInfo'))
        this.formData.realname = userInfo.realname
        this.formData.jbstaffid = userInfo.staffid
        this.formData.orgname = userInfo.linkDetp.orgname
        this.formData.jbunitid = userInfo.linkDetp.orgid
        this.dctype = row.dctype
        this.formData.performanceCategory = row.dctype == '付款' ? '1' : '2'

        this.formData.contractid = row.contractid
        // this.formData.performanceCategory = '1'
        this.XDFList = row.XDFList.data.tcu.budgetList //相对方弹框选择列表数据

        if (!row.nodeid) {
          this.title = '添加'
          const info = row.XDFList.data.tcu.budgetList
            .map((res) => res.budgetname)
            .toString()
          const ids = row.XDFList.data.tcu.budgetList
            .map((res) => res.budgetid)
            .toString()
          this.$set(this.formData, 'budgetName', info)
          this.$set(this.formData, 'budgetIds', ids)
        } else {
          this.title = '编辑'
          this.formData = row
          // Object.keys(this.formData).forEach((key) => {
          //   this.$set(this.form, key, row[key])
          //   // this.formData[key] = row[key];
          // })
          const info = row.tblContractBudgetList
            ? row.tblContractBudgetList.map((res) => res.budgetname).toString()
            : [].map((res) => res.budgetname).toString()
          this.$set(this.formData, 'budgetName', info)
          this.$set(this.formData, 'budgetIds', row.budgetIds)
        }
        this.dialogFormVisible = true
      },
      close() {
        this.$refs['form'].resetFields()
        this.formData = this.$options.data().formData
        this.dctype = ''
        this.dialogFormVisible = false
      },
      //保存
      save() {
        if (this.formData.performanceCategory === '2') {
          // delete this.formData.nodepost
          // delete this.formData.nodemoney
        }
        delete this.formData.XDFList
        delete this.formData.tblContractBudgetList
        this.$refs['form'].validate(async (valid) => {
          if (valid) {
            const { msg, code } = await saveJd(this.formData)
            if (code == 1) {
              this.$baseMessage(msg, 'success', 'vab-hey-message-success')
              this.$emit('fetch-data')
              this.close()
            }
          }
        })
      },
      //回调函数
      handleDepartmentSelected(node) {
        console.log('🚀 ~ handleDepartmentSelected ~ node:', node)
        this.formData.jbunitid = node.id
        this.formData.orgname = node.name
      },
      //回调函数
      handleExecutorSelected(node) {
        console.log('🚀 ~ handleExecutorSelected ~ node:', node)
        this.formData.jbunitid = node.orgid
        this.formData.orgname = node.orgname
        this.formData.realname = node.realname
        this.formData.jbstaffid = node.staffid
      },
      //处理金额
      handleBlur() {
        const info =
          (this.contract.contractmoney || 0) * (this.formData.nodepost / 100)

        this.formData.nodemoney = info
      },
      //回调函数
      handleXDFSelected(val) {
        const name = val.map((res) => res.budgetname)
        const ids = val.map((res) => res.budgetid)
        this.$set(this.formData, 'budgetName', name.toString())
        this.$set(this.formData, 'budgetIds', ids.toString())
      },
    },
  }
</script>
<style scoped>
  .formula .el-form-item--small.el-form-item {
    margin-bottom: 5px;
  }
</style>
