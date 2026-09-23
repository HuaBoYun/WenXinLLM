<template>
  <el-dialog
    :title="title"
    :visible.sync="dialogFormVisible"
    :close-on-click-modal="false"
    width="1200px"
    @close="close"
  >
    <el-form
      ref="elForm"
      label-width="120px"
      :model="formData"
      :rules="rules"
      size="medium"
    >
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="登记编号" prop="registerNo">
            <el-input
              v-model="formData.registerNo"
              clearable
              placeholder="请输入登记编号"
              :disabled="!footer"
            >
              <el-button
                slot="append"
                icon="el-icon-refresh"
                @click="generateRegisterNo"
                :disabled="!footer"
              >
                生成
              </el-button>
            </el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="项目名称" prop="projectName">
            <el-input
              v-model="formData.projectName"
              clearable
              placeholder="请输入项目名称"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="发包方全称" prop="contractorFullName">
            <el-input
              v-model="formData.contractorFullName"
              clearable
              placeholder="请输入发包方全称"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="统一社会信用代码" prop="contractorCreditCode">
            <el-input
              v-model="formData.contractorCreditCode"
              clearable
              placeholder="请输入统一社会信用代码"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="资金来源" prop="fundingSource">
            <el-input
              v-model="formData.fundingSource"
              clearable
              placeholder="请输入资金来源"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="项目金额" prop="projectAmount">
            <el-input-number
              v-model="formData.projectAmount"
              :precision="2"
              :step="1000"
              :min="0"
              :max="999999999.99"
              placeholder="请输入项目金额"
              style="width: 100%"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="预算收入" prop="budgetIncome">
            <el-input-number
              v-model="formData.budgetIncome"
              :precision="2"
              :step="1000"
              :min="0"
              :max="999999999.99"
              placeholder="请输入预算收入"
              style="width: 100%"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="预算支出" prop="budgetExpense">
            <el-input-number
              v-model="formData.budgetExpense"
              :precision="2"
              :step="1000"
              :min="0"
              :max="999999999.99"
              placeholder="请输入预算支出"
              style="width: 100%"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item label="服务内容" prop="serviceContent">
            <el-input
              v-model="formData.serviceContent"
              type="textarea"
              :rows="4"
              placeholder="请输入服务内容"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="8">
          <el-form-item label="项目状态" prop="projectStatus">
            <el-select
              v-model="formData.projectStatus"
              placeholder="请选择项目状态"
              style="width: 100%"
              :disabled="!footer"
            >
              <el-option label="登记" :value="1" />
              <el-option label="承接" :value="2" />
              <el-option label="执行" :value="3" />
              <el-option label="完成" :value="4" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="是否首谈报备">
            <el-tag :type="formData.isFirstTalkReport === 1 ? 'warning' : 'info'">
              {{ formData.isFirstTalkReport === 1 ? '是（≥500万）' : '否（<500万）' }}
            </el-tag>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="报备状态" v-if="formData.isFirstTalkReport === 1">
            <el-select
              v-model="formData.reportStatus"
              placeholder="请选择报备状态"
              style="width: 100%"
              :disabled="!footer"
            >
              <el-option label="未报备" :value="0" />
              <el-option label="已报备" :value="1" />
              <el-option label="已审核" :value="2" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item label="备注" prop="remark">
            <el-input
              v-model="formData.remark"
              type="textarea"
              :rows="3"
              placeholder="请输入备注信息"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>

    <template v-if="footer" slot="footer">
      <el-button @click="close">取消</el-button>
      <el-button type="primary" @click="save" :loading="saveLoading">确定</el-button>
    </template>
  </el-dialog>
</template>

<script>
import {
  saveProjectInfoRegister,
  generateRegisterNo
} from '@/api/contract/projectInfoRegister'

export default {
  name: 'ProjectInfoRegisterEdit',
  data() {
    return {
      formData: {
        projectId: undefined,
        registerNo: '',
        projectName: '',
        contractorFullName: '',
        contractorCreditCode: '',
        fundingSource: '',
        projectAmount: null,
        budgetIncome: null,
        budgetExpense: null,
        serviceContent: '',
        isFirstTalkReport: 0,
        reportStatus: 0,
        projectStatus: 1,
        remark: '',
        registerUserId: '',
        registerUserName: ''
      },
      rules: {
        registerNo: [
          { required: true, message: '请输入登记编号', trigger: 'blur' }
        ],
        projectName: [
          { required: true, message: '请输入项目名称', trigger: 'blur' },
          { min: 2, max: 200, message: '项目名称长度在 2 到 200 个字符', trigger: 'blur' }
        ],
        contractorFullName: [
          { required: true, message: '请输入发包方全称', trigger: 'blur' },
          { min: 2, max: 200, message: '发包方全称长度在 2 到 200 个字符', trigger: 'blur' }
        ],
        contractorCreditCode: [
          { pattern: /^[0-9A-HJ-NPQRTUWXY]{2}\d{6}[0-9A-HJ-NPQRTUWXY]{10}$/, message: '请输入正确的统一社会信用代码', trigger: 'blur' }
        ],
        projectAmount: [
          { required: true, message: '请输入项目金额', trigger: 'blur' },
          { type: 'number', min: 0, message: '项目金额必须大于等于0', trigger: 'blur' }
        ],
        budgetIncome: [
          { type: 'number', min: 0, message: '预算收入必须大于等于0', trigger: 'blur' }
        ],
        budgetExpense: [
          { type: 'number', min: 0, message: '预算支出必须大于等于0', trigger: 'blur' }
        ],
        projectStatus: [
          { required: true, message: '请选择项目状态', trigger: 'change' }
        ]
      },
      dialogFormVisible: false,
      title: '',
      footer: true,
      saveLoading: false
    }
  },
  watch: {
    'formData.projectAmount': {
      handler(newVal) {
        // 自动判断是否需要首谈报备（项目金额大于等于500万元）
        if (newVal && newVal >= 5000000) {
          this.formData.isFirstTalkReport = 1
          if (this.formData.reportStatus === undefined) {
            this.formData.reportStatus = 0
          }
        } else {
          this.formData.isFirstTalkReport = 0
          this.formData.reportStatus = 0
        }
      },
      immediate: true
    }
  },
  methods: {
    showEdit(type, data) {
      this.dialogFormVisible = true
      this.footer = type !== 'detail'

      if (type === 'add') {
        this.title = '新建项目信息登记'
        this.resetForm()
        // 自动生成登记编号
        this.generateRegisterNo()
      } else if (type === 'edit') {
        this.title = '编辑项目信息登记'
        this.formData = { ...data }
      } else if (type === 'detail') {
        this.title = '项目信息登记详情'
        this.formData = { ...data }
      }
    },
    resetForm() {
      this.formData = {
        projectId: undefined,
        registerNo: '',
        projectName: '',
        contractorFullName: '',
        contractorCreditCode: '',
        fundingSource: '',
        projectAmount: null,
        budgetIncome: null,
        budgetExpense: null,
        serviceContent: '',
        isFirstTalkReport: 0,
        reportStatus: 0,
        projectStatus: 1,
        remark: '',
        registerUserId: '',
        registerUserName: ''
      }
    },
    async generateRegisterNo() {
      try {
        const response = await generateRegisterNo()
        if (response.code === 1) {
          this.formData.registerNo = response.data
        } else {
          this.$message.error(response.msg || '生成登记编号失败')
        }
      } catch (error) {
        this.$message.error('生成登记编号失败：' + error.message)
      }
    },
    save() {
      this.$refs.elForm.validate(async (valid) => {
        if (!valid) return false

        this.saveLoading = true
        try {
          const response = await saveProjectInfoRegister(this.formData)
          if (response.code === 1) {
            this.$message.success(response.msg || '保存成功')
            this.close()
            this.$emit('fetch-data')
          } else {
            this.$message.error(response.msg || '保存失败')
          }
        } catch (error) {
          this.$message.error('保存失败：' + error.message)
        }
        this.saveLoading = false
      })
    },
    close() {
      this.dialogFormVisible = false
      this.$refs.elForm.resetFields()
      this.resetForm()
    }
  }
}
</script>
