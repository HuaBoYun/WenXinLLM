<template>
  <el-dialog
    :title="dialogType === 'add' ? '新增控制措施' : '编辑控制措施'"
    :visible.sync="dialogVisible"
    width="800px"
    :close-on-click-modal="false"
    :close-on-press-escape="false"
    @close="handleClose"
  >
    <el-form
      ref="form"
      :model="form"
      :rules="rules"
      label-width="120px"
      v-loading="loading"
    >
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="企业名称" prop="enterpriseId">
            <CompanyTreeModal
              v-model="form.enterpriseId"
              :company-name.sync="form.enterpriseName"
              placeholder="请选择企业"
              :disabled="dialogType === 'edit'"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="措施名称" prop="measureName">
            <el-input v-model="form.measureName" placeholder="请输入措施名称" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="措施类型" prop="measureType">
            <el-select v-model="form.measureType" placeholder="请选择措施类型">
              <el-option label="预防措施" value="PREVENTIVE" />
              <el-option label="纠正措施" value="CORRECTIVE" />
              <el-option label="改进措施" value="IMPROVEMENT" />
              <el-option label="应急措施" value="EMERGENCY" />
              <el-option label="监控措施" value="MONITORING" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="优先级" prop="priority">
            <el-select v-model="form.priority" placeholder="请选择优先级">
              <el-option label="高" value="HIGH" />
              <el-option label="中" value="MEDIUM" />
              <el-option label="低" value="LOW" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="负责人" prop="responsiblePerson">
            <el-input v-model="form.responsiblePerson" placeholder="请输入负责人" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="负责部门" prop="responsibleDepartment">
            <el-input v-model="form.responsibleDepartment" placeholder="请输入负责部门" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-form-item label="措施描述" prop="measureDescription">
        <el-input
          v-model="form.measureDescription"
          type="textarea"
          :rows="3"
          placeholder="请输入措施描述"
        />
      </el-form-item>

      <el-form-item label="实施目标" prop="implementationGoal">
        <el-input
          v-model="form.implementationGoal"
          type="textarea"
          :rows="2"
          placeholder="请输入实施目标"
        />
      </el-form-item>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="计划开始日期" prop="plannedStartDate">
            <el-date-picker
              v-model="form.plannedStartDate"
              type="date"
              placeholder="选择开始日期"
              value-format="yyyy-MM-dd"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="计划完成日期" prop="plannedEndDate">
            <el-date-picker
              v-model="form.plannedEndDate"
              type="date"
              placeholder="选择完成日期"
              value-format="yyyy-MM-dd"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="预算金额" prop="budgetAmount">
            <el-input-number
              v-model="form.budgetAmount"
              :precision="2"
              :min="0"
              placeholder="预算金额"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="预期效果" prop="expectedEffect">
            <el-select v-model="form.expectedEffect" placeholder="请选择预期效果">
              <el-option label="显著改善" value="SIGNIFICANT" />
              <el-option label="明显改善" value="OBVIOUS" />
              <el-option label="一般改善" value="MODERATE" />
              <el-option label="轻微改善" value="SLIGHT" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-form-item label="关联风险" prop="relatedRisks">
        <el-input
          v-model="form.relatedRisks"
          placeholder="请输入关联的风险因素，多个用分号分隔"
        />
      </el-form-item>

      <el-form-item label="实施步骤" prop="implementationSteps">
        <div class="steps-container">
          <div
            v-for="(step, index) in form.implementationSteps"
            :key="index"
            class="step-item"
          >
            <el-row :gutter="10">
              <el-col :span="4">
                <el-input
                  v-model="step.stepNumber"
                  placeholder="步骤序号"
                  :disabled="true"
                />
              </el-col>
              <el-col :span="16">
                <el-input
                  v-model="step.stepDescription"
                  placeholder="请输入步骤描述"
                />
              </el-col>
              <el-col :span="4">
                <el-button
                  type="danger"
                  icon="el-icon-delete"
                  size="small"
                  @click="removeStep(index)"
                >
                  删除
                </el-button>
              </el-col>
            </el-row>
          </div>
          <el-button
            type="primary"
            icon="el-icon-plus"
            size="small"
            @click="addStep"
          >
            添加步骤
          </el-button>
        </div>
      </el-form-item>

      <el-form-item label="成功标准" prop="successCriteria">
        <el-input
          v-model="form.successCriteria"
          type="textarea"
          :rows="2"
          placeholder="请输入成功标准"
        />
      </el-form-item>

      <el-form-item label="风险评估" prop="riskAssessment">
        <el-input
          v-model="form.riskAssessment"
          type="textarea"
          :rows="2"
          placeholder="请输入实施过程中可能遇到的风险"
        />
      </el-form-item>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="监控频率" prop="monitoringFrequency">
            <el-select v-model="form.monitoringFrequency" placeholder="请选择监控频率">
              <el-option label="每日" value="DAILY" />
              <el-option label="每周" value="WEEKLY" />
              <el-option label="每月" value="MONTHLY" />
              <el-option label="每季度" value="QUARTERLY" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="报告频率" prop="reportingFrequency">
            <el-select v-model="form.reportingFrequency" placeholder="请选择报告频率">
              <el-option label="每周" value="WEEKLY" />
              <el-option label="每月" value="MONTHLY" />
              <el-option label="每季度" value="QUARTERLY" />
              <el-option label="里程碑" value="MILESTONE" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-form-item label="备注" prop="remarks">
        <el-input
          v-model="form.remarks"
          type="textarea"
          :rows="2"
          placeholder="请输入备注信息"
        />
      </el-form-item>
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取消</el-button>
      <el-button type="primary" @click="handleSubmit" :loading="loading">
        {{ dialogType === 'add' ? '新增' : '更新' }}
      </el-button>
      <el-button
        v-if="dialogType === 'add'"
        type="success"
        @click="handleSubmitAndStart"
        :loading="loading"
      >
        保存并开始实施
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { addRiskControlMeasure, updateRiskControlMeasure } from '@/api/stateAssets/riskControlMeasure'
import CompanyTreeModal from '@/components/CompanyTreeModal'

export default {
  name: 'RiskControlMeasureDialog',
  components: {
    CompanyTreeModal
  },
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    formData: {
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
      loading: false,
      form: {
        enterpriseId: '',
        enterpriseName: '',
        measureName: '',
        measureType: '',
        priority: 'MEDIUM',
        responsiblePerson: '',
        responsibleDepartment: '',
        measureDescription: '',
        implementationGoal: '',
        plannedStartDate: '',
        plannedEndDate: '',
        budgetAmount: null,
        expectedEffect: '',
        relatedRisks: '',
        implementationSteps: [
          { stepNumber: '1', stepDescription: '' }
        ],
        successCriteria: '',
        riskAssessment: '',
        monitoringFrequency: 'WEEKLY',
        reportingFrequency: 'MONTHLY',
        remarks: ''
      },
      rules: {
        enterpriseId: [
          { required: true, message: '请选择企业', trigger: 'change' }
        ],
        measureName: [
          { required: true, message: '请输入措施名称', trigger: 'blur' },
          { min: 2, max: 100, message: '长度在 2 到 100 个字符', trigger: 'blur' }
        ],
        measureType: [
          { required: true, message: '请选择措施类型', trigger: 'change' }
        ],
        priority: [
          { required: true, message: '请选择优先级', trigger: 'change' }
        ],
        responsiblePerson: [
          { required: true, message: '请输入负责人', trigger: 'blur' }
        ],
        responsibleDepartment: [
          { required: true, message: '请输入负责部门', trigger: 'blur' }
        ],
        measureDescription: [
          { required: true, message: '请输入措施描述', trigger: 'blur' }
        ],
        implementationGoal: [
          { required: true, message: '请输入实施目标', trigger: 'blur' }
        ],
        plannedStartDate: [
          { required: true, message: '请选择计划开始日期', trigger: 'change' }
        ],
        plannedEndDate: [
          { required: true, message: '请选择计划完成日期', trigger: 'change' }
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
    }
  },
  watch: {
    visible(val) {
      if (val) {
        this.initForm()
      }
    },
    'form.plannedStartDate'(newVal) {
      // 当开始日期变化时，确保结束日期不早于开始日期
      if (newVal && this.form.plannedEndDate && this.form.plannedEndDate < newVal) {
        this.form.plannedEndDate = ''
      }
    }
  },
  methods: {
    // 初始化表单
    initForm() {
      if (this.dialogType === 'edit' && this.formData) {
        this.form = {
          ...this.formData,
          implementationSteps: this.formData.implementationSteps || [
            { stepNumber: '1', stepDescription: '' }
          ]
        }
      } else {
        this.form = {
          enterpriseId: '',
          enterpriseName: '',
          measureName: '',
          measureType: '',
          priority: 'MEDIUM',
          responsiblePerson: '',
          responsibleDepartment: '',
          measureDescription: '',
          implementationGoal: '',
          plannedStartDate: '',
          plannedEndDate: '',
          budgetAmount: null,
          expectedEffect: '',
          relatedRisks: '',
          implementationSteps: [
            { stepNumber: '1', stepDescription: '' }
          ],
          successCriteria: '',
          riskAssessment: '',
          monitoringFrequency: 'WEEKLY',
          reportingFrequency: 'MONTHLY',
          remarks: ''
        }
      }
    },

    // 添加实施步骤
    addStep() {
      const stepNumber = (this.form.implementationSteps.length + 1).toString()
      this.form.implementationSteps.push({
        stepNumber: stepNumber,
        stepDescription: ''
      })
    },

    // 删除实施步骤
    removeStep(index) {
      if (this.form.implementationSteps.length > 1) {
        this.form.implementationSteps.splice(index, 1)
        // 重新编号
        this.form.implementationSteps.forEach((step, idx) => {
          step.stepNumber = (idx + 1).toString()
        })
      } else {
        this.$message.warning('至少需要保留一个实施步骤')
      }
    },

    // 提交表单
    async handleSubmit() {
      try {
        await this.$refs.form.validate()
        
        // 验证日期逻辑
        if (this.form.plannedEndDate <= this.form.plannedStartDate) {
          this.$message.error('计划完成日期必须晚于计划开始日期')
          return
        }

        // 验证实施步骤
        const hasEmptyStep = this.form.implementationSteps.some(step => !step.stepDescription.trim())
        if (hasEmptyStep) {
          this.$message.error('请完善所有实施步骤的描述')
          return
        }

        this.loading = true

        const formData = {
          ...this.form,
          implementationSteps: JSON.stringify(this.form.implementationSteps),
          createBy: this.$store.getters.userInfo.userName,
          updateBy: this.$store.getters.userInfo.userName
        }

        let response
        if (this.dialogType === 'add') {
          response = await addRiskControlMeasure(formData)
        } else {
          response = await updateRiskControlMeasure(formData)
        }

        if (response.code === 200) {
          this.$message.success(this.dialogType === 'add' ? '新增成功' : '更新成功')
          this.$emit('success')
          this.handleClose()
        }
      } catch (error) {
        if (error !== 'validation failed') {
          this.$message.error((this.dialogType === 'add' ? '新增' : '更新') + '失败：' + error.message)
        }
      } finally {
        this.loading = false
      }
    },

    // 保存并开始实施
    async handleSubmitAndStart() {
      try {
        await this.$refs.form.validate()
        
        // 验证日期逻辑
        if (this.form.plannedEndDate <= this.form.plannedStartDate) {
          this.$message.error('计划完成日期必须晚于计划开始日期')
          return
        }

        // 验证实施步骤
        const hasEmptyStep = this.form.implementationSteps.some(step => !step.stepDescription.trim())
        if (hasEmptyStep) {
          this.$message.error('请完善所有实施步骤的描述')
          return
        }

        this.loading = true

        const formData = {
          ...this.form,
          implementationSteps: JSON.stringify(this.form.implementationSteps),
          implementationStatus: 'IN_PROGRESS',
          actualStartDate: new Date().toISOString().split('T')[0],
          createBy: this.$store.getters.userInfo.userName,
          updateBy: this.$store.getters.userInfo.userName
        }

        const response = await addRiskControlMeasure(formData)

        if (response.code === 200) {
          this.$message.success('新增成功并已开始实施')
          this.$emit('success')
          this.handleClose()
        }
      } catch (error) {
        if (error !== 'validation failed') {
          this.$message.error('保存并开始实施失败：' + error.message)
        }
      } finally {
        this.loading = false
      }
    },

    // 关闭对话框
    handleClose() {
      this.dialogVisible = false
      this.$refs.form.resetFields()
      this.form = {
        enterpriseId: '',
        enterpriseName: '',
        measureName: '',
        measureType: '',
        priority: 'MEDIUM',
        responsiblePerson: '',
        responsibleDepartment: '',
        measureDescription: '',
        implementationGoal: '',
        plannedStartDate: '',
        plannedEndDate: '',
        budgetAmount: null,
        expectedEffect: '',
        relatedRisks: '',
        implementationSteps: [
          { stepNumber: '1', stepDescription: '' }
        ],
        successCriteria: '',
        riskAssessment: '',
        monitoringFrequency: 'WEEKLY',
        reportingFrequency: 'MONTHLY',
        remarks: ''
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.steps-container {
  .step-item {
    margin-bottom: 10px;
  }
}

.dialog-footer {
  text-align: right;
}
</style>
