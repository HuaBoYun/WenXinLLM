<template>
  <el-dialog
    :title="dialogTitle"
    :visible.sync="dialogVisible"
    width="800px"
    :before-close="handleClose"
    :close-on-click-modal="false"
  >
    <el-form
      ref="projectForm"
      :model="form"
      :rules="formRules"
      label-width="120px"
      :disabled="dialogType === 'view'"
    >
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="项目名称" prop="projectName">
            <el-input v-model="form.projectName" placeholder="请输入项目名称" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="投资类型" prop="investType">
            <el-select v-model="form.investType" placeholder="请选择投资类型" style="width: 100%;">
              <el-option label="股权" value="EQUITY"></el-option>
              <el-option label="债权" value="DEBT"></el-option>
              <el-option label="混合" value="MIXED"></el-option>
              <el-option label="基金" value="FUND"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="投资企业" prop="companyName">
            <el-input v-model="form.companyName" placeholder="请输入投资企业名称" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="被投资企业" prop="targetCompany">
            <el-input v-model="form.targetCompany" placeholder="请输入被投资企业名称" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="投资金额" prop="investAmount">
            <el-input-number
              v-model="form.investAmount"
              :min="0"
              :precision="2"
              placeholder="请输入投资金额"
              style="width: 100%;"
            />
            <span style="margin-left: 10px; color: #909399;">万元</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="是否主业" prop="isMainBiz">
            <el-radio-group v-model="form.isMainBiz">
              <el-radio label="Y">是</el-radio>
              <el-radio label="N">否</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="项目状态" prop="projectStatus">
            <el-select v-model="form.projectStatus" placeholder="请选择项目状态" style="width: 100%;">
              <el-option label="规划中" value="PLANNING"></el-option>
              <el-option label="已审批" value="APPROVED"></el-option>
              <el-option label="执行中" value="EXECUTING"></el-option>
              <el-option label="已完成" value="COMPLETED"></el-option>
              <el-option label="已暂停" value="SUSPENDED"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="预期收益率" prop="expectedReturn">
            <el-input-number
              v-model="form.expectedReturn"
              :min="0"
              :precision="2"
              placeholder="请输入预期收益率"
              style="width: 100%;"
            />
            <span style="margin-left: 10px; color: #909399;">%</span>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="审批日期" prop="approvalDate">
            <el-date-picker
              v-model="form.approvalDate"
              type="date"
              placeholder="选择审批日期"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              style="width: 100%;"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="实际收益率" prop="actualReturn">
            <el-input-number
              v-model="form.actualReturn"
              :min="0"
              :precision="2"
              placeholder="请输入实际收益率"
              style="width: 100%;"
            />
            <span style="margin-left: 10px; color: #909399;">%</span>
          </el-form-item>
        </el-col>
      </el-row>

      <el-form-item label="备注" prop="remark">
        <el-input
          v-model="form.remark"
          type="textarea"
          :rows="3"
          placeholder="请输入备注"
          maxlength="500"
          show-word-limit
        />
      </el-form-item>

      <!-- 查看模式下显示额外信息 -->
      <template v-if="dialogType === 'view'">
        <el-divider content-position="left">其他信息</el-divider>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="创建时间">
              <span>{{ form.createTime }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="更新时间">
              <span>{{ form.updateTime }}</span>
            </el-form-item>
          </el-col>
        </el-row>
      </template>
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">{{ dialogType === 'view' ? '关闭' : '取消' }}</el-button>
      <el-button
        v-if="dialogType !== 'view'"
        type="primary"
        @click="handleSubmit"
        :loading="submitLoading"
      >
        确定
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import {
  addInvestmentProject,
  updateInvestmentProject,
  getInvestmentProjectDetail
} from '@/api/stateAssets/investmentDecision'

export default {
  name: 'InvestmentProjectDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    projectData: {
      type: Object,
      default: () => ({})
    },
    dialogType: {
      type: String,
      default: 'add'
    }
  },
  data() {
    return {
      submitLoading: false,
      form: {
        projectId: '',
        projectName: '',
        investType: '',
        companyName: '',
        targetCompany: '',
        investAmount: null,
        isMainBiz: 'N',
        projectStatus: 'PLANNING',
        expectedReturn: null,
        actualReturn: null,
        approvalDate: '',
        remark: '',
        createTime: '',
        updateTime: ''
      },
      formRules: {
        projectName: [
          { required: true, message: '请输入项目名称', trigger: 'blur' }
        ],
        investType: [
          { required: true, message: '请选择投资类型', trigger: 'change' }
        ],
        companyName: [
          { required: true, message: '请输入投资企业名称', trigger: 'blur' }
        ]
      }
    }
  },
  computed: {
    dialogVisible: {
      get() {
        return this.visible
      },
      set(val) {
        this.$emit('update:visible', val)
      }
    },
    dialogTitle() {
      const titleMap = {
        add: '新增投资项目',
        edit: '编辑投资项目',
        view: '查看投资项目'
      }
      return titleMap[this.dialogType] || '投资项目'
    }
  },
  watch: {
    visible(val) {
      if (val) {
        this.initForm()
        if (this.dialogType !== 'add' && this.projectData.projectId) {
          this.loadProjectDetail()
        }
      }
    }
  },
  methods: {
    initForm() {
      if (this.dialogType === 'add') {
        this.form = {
          projectId: '',
          projectName: '',
          investType: '',
          companyName: '',
          targetCompany: '',
          investAmount: null,
          isMainBiz: 'N',
          projectStatus: 'PLANNING',
          expectedReturn: null,
          actualReturn: null,
          approvalDate: '',
          remark: '',
          createTime: '',
          updateTime: ''
        }
      } else {
        this.form = { ...this.projectData }
      }
      this.$nextTick(() => {
        this.$refs.projectForm && this.$refs.projectForm.clearValidate()
      })
    },

    async loadProjectDetail() {
      try {
        const response = await getInvestmentProjectDetail({
          projectId: this.projectData.projectId
        })
        if (response.result === 200) {
          this.form = { ...response.data }
        }
      } catch (error) {
        this.$message.error('加载项目详情失败')
      }
    },

    handleSubmit() {
      this.$refs.projectForm.validate(async (valid) => {
        if (!valid) return

        this.submitLoading = true
        try {
          let response
          if (this.dialogType === 'add') {
            response = await addInvestmentProject(this.form)
          } else {
            response = await updateInvestmentProject(this.form)
          }
          if (response.result === 200) {
            this.$message.success(this.dialogType === 'add' ? '新增成功' : '更新成功')
            this.handleClose()
            this.$emit('refresh')
          } else {
            this.$message.error(response.msg || (this.dialogType === 'add' ? '新增失败' : '更新失败'))
          }
        } catch (error) {
          this.$message.error(this.dialogType === 'add' ? '新增失败' : '更新失败')
        } finally {
          this.submitLoading = false
        }
      })
    },

    handleClose() {
      this.dialogVisible = false
      this.initForm()
    }
  }
}
</script>

<style scoped>
.dialog-footer {
  text-align: right;
}
</style>
