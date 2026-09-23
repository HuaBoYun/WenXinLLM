<template>
  <el-dialog
    title="编辑经营计划"
    :visible.sync="dialogVisible"
    width="70%"
    :before-close="handleClose"
  >
    <el-form ref="form" :model="form" :rules="rules" label-width="120px">
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="计划名称" prop="planName">
            <el-input v-model="form.planName" placeholder="请输入计划名称" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="计划类型" prop="planType">
            <el-select v-model="form.planType" placeholder="请选择计划类型" style="width: 100%">
              <el-option label="年度经营计划" value="年度经营计划" />
              <el-option label="季度经营计划" value="季度经营计划" />
              <el-option label="月度经营计划" value="月度经营计划" />
              <el-option label="专项经营计划" value="专项经营计划" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="计划年度" prop="planYear">
            <el-date-picker
              v-model="form.planYear"
              type="year"
              placeholder="选择计划年度"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="负责人" prop="responsiblePerson">
            <el-input v-model="form.responsiblePerson" placeholder="请输入负责人姓名" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="计划开始时间" prop="plannedStartTime">
            <el-date-picker
              v-model="form.plannedStartTime"
              type="date"
              placeholder="选择开始时间"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="计划结束时间" prop="plannedEndTime">
            <el-date-picker
              v-model="form.plannedEndTime"
              type="date"
              placeholder="选择结束时间"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-form-item label="计划描述" prop="planDescription">
        <el-input
          v-model="form.planDescription"
          type="textarea"
          :rows="4"
          placeholder="请输入计划描述"
        />
      </el-form-item>

      <!-- 计划目标 -->
      <el-form-item label="计划目标">
        <el-button type="primary" size="small" @click="addTarget" style="margin-bottom: 10px;">
          添加目标
        </el-button>
        <el-table :data="form.targets" border style="width: 100%">
          <el-table-column label="目标名称" width="200">
            <template slot-scope="scope">
              <el-input v-model="scope.row.targetName" placeholder="目标名称" size="small" />
            </template>
          </el-table-column>
          <el-table-column label="目标值" width="120">
            <template slot-scope="scope">
              <el-input-number
                v-model="scope.row.targetValue"
                :precision="2"
                :min="0"
                size="small"
                style="width: 100%"
              />
            </template>
          </el-table-column>
          <el-table-column label="单位" width="80">
            <template slot-scope="scope">
              <el-input v-model="scope.row.unit" placeholder="单位" size="small" />
            </template>
          </el-table-column>
          <el-table-column label="备注">
            <template slot-scope="scope">
              <el-input v-model="scope.row.remarks" placeholder="备注" size="small" />
            </template>
          </el-table-column>
          <el-table-column label="操作" width="80">
            <template slot-scope="scope">
              <el-button
                type="danger"
                size="mini"
                @click="removeTarget(scope.$index)"
              >
                删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-form-item>

      <!-- 关键里程碑 -->
      <el-form-item label="关键里程碑">
        <el-button type="primary" size="small" @click="addMilestone" style="margin-bottom: 10px;">
          添加里程碑
        </el-button>
        <el-table :data="form.milestones" border style="width: 100%">
          <el-table-column label="里程碑名称" width="200">
            <template slot-scope="scope">
              <el-input v-model="scope.row.milestoneName" placeholder="里程碑名称" size="small" />
            </template>
          </el-table-column>
          <el-table-column label="计划完成时间" width="150">
            <template slot-scope="scope">
              <el-date-picker
                v-model="scope.row.plannedDate"
                type="date"
                placeholder="选择时间"
                size="small"
                style="width: 100%"
              />
            </template>
          </el-table-column>
          <el-table-column label="描述">
            <template slot-scope="scope">
              <el-input v-model="scope.row.description" placeholder="描述" size="small" />
            </template>
          </el-table-column>
          <el-table-column label="操作" width="80">
            <template slot-scope="scope">
              <el-button
                type="danger"
                size="mini"
                @click="removeMilestone(scope.$index)"
              >
                删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-form-item>

      <el-form-item label="关键措施">
        <el-input
          v-model="form.keyMeasures"
          type="textarea"
          :rows="3"
          placeholder="请输入关键措施"
        />
      </el-form-item>

      <el-form-item label="风险分析">
        <el-input
          v-model="form.riskAnalysis"
          type="textarea"
          :rows="3"
          placeholder="请输入风险分析"
        />
      </el-form-item>

      <el-form-item label="备注信息">
        <el-input
          v-model="form.remarks"
          type="textarea"
          :rows="3"
          placeholder="请输入备注信息"
        />
      </el-form-item>
    </el-form>

    <span slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取 消</el-button>
      <el-button type="primary" @click="handleSubmit" :loading="loading">确 定</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { addOperationPlan, updateOperationPlan } from '@/api/enterprise/operationPlan'

export default {
  name: 'PlanEditDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    planData: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      loading: false,
      form: {
        planName: '',
        planType: '',
        planYear: '',
        responsiblePerson: '',
        plannedStartTime: '',
        plannedEndTime: '',
        planDescription: '',
        keyMeasures: '',
        riskAnalysis: '',
        remarks: '',
        targets: [],
        milestones: []
      },
      rules: {
        planName: [
          { required: true, message: '请输入计划名称', trigger: 'blur' }
        ],
        planType: [
          { required: true, message: '请选择计划类型', trigger: 'change' }
        ],
        planYear: [
          { required: true, message: '请选择计划年度', trigger: 'change' }
        ],
        responsiblePerson: [
          { required: true, message: '请输入负责人', trigger: 'blur' }
        ],
        plannedStartTime: [
          { required: true, message: '请选择开始时间', trigger: 'change' }
        ],
        plannedEndTime: [
          { required: true, message: '请选择结束时间', trigger: 'change' }
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
    isEdit() {
      return this.planData && this.planData.planId
    }
  },
  watch: {
    planData: {
      handler(newVal) {
        if (newVal && Object.keys(newVal).length > 0) {
          // 映射后端实体字段到表单字段
          this.form = {
            ...newVal,
            planName: newVal.planName || '',
            planType: newVal.planType || '',
            planYear: newVal.planYear || '',
            // 负责人：后端字段是 planManager
            responsiblePerson: newVal.planManager || newVal.responsiblePerson || '',
            // 时间：后端字段是 planStartDate / formulationStartTime
            plannedStartTime: newVal.planStartDate || newVal.formulationStartTime || newVal.plannedStartTime || '',
            plannedEndTime: newVal.planEndDate || newVal.formulationEndTime || newVal.plannedEndTime || '',
            planDescription: newVal.planDescription || '',
            // 关键措施：后端字段是 strategicObjectives
            keyMeasures: newVal.keyMeasures || newVal.strategicObjectives || newVal.operationalObjectives || '',
            // 风险分析：后端字段是 riskAssessment
            riskAnalysis: newVal.riskAnalysis || newVal.riskAssessment || '',
            remarks: newVal.remarks || '',
            targets: newVal.targets || [],
            milestones: newVal.milestones || []
          }
        }
      },
      immediate: true,
      deep: true
    }
  },
  methods: {
    handleClose() {
      this.dialogVisible = false
      this.resetForm()
    },
    resetForm() {
      if (this.$refs.form) {
        this.$refs.form.resetFields()
      }
      this.form = {
        planName: '',
        planType: '',
        planYear: '',
        responsiblePerson: '',
        plannedStartTime: '',
        plannedEndTime: '',
        planDescription: '',
        keyMeasures: '',
        riskAnalysis: '',
        remarks: '',
        targets: [],
        milestones: []
      }
    },
    addTarget() {
      this.form.targets.push({
        targetName: '',
        targetValue: 0,
        unit: '',
        remarks: ''
      })
    },
    removeTarget(index) {
      this.form.targets.splice(index, 1)
    },
    addMilestone() {
      this.form.milestones.push({
        milestoneName: '',
        plannedDate: '',
        description: ''
      })
    },
    removeMilestone(index) {
      this.form.milestones.splice(index, 1)
    },
    async handleSubmit() {
      this.$refs.form.validate(async (valid) => {
        if (!valid) return

        // 验证时间逻辑
        if (this.form.plannedStartTime && this.form.plannedEndTime && this.form.plannedStartTime >= this.form.plannedEndTime) {
          this.$message.warning('结束时间必须晚于开始时间')
          return
        }

        this.loading = true
        try {
          // 构建提交数据
          const submitData = {
            ...this.form,
            enterpriseId: this.planData.enterpriseId || this.form.enterpriseId,
            // 设置默认状态
            planStatus: this.form.planStatus || '待制定',
            approvalStatus: this.form.approvalStatus || '未提交',
            executionStatus: this.form.executionStatus || '未开始',
            // 映射到后端实体字段
            planManager: this.form.responsiblePerson,
            monitoringManager: this.form.responsiblePerson,
            // 映射时间字段到后端实体
            formulationEndTime: this.form.plannedEndTime || null,
            formulationStartTime: this.form.plannedStartTime || null,
            planStartDate: this.form.plannedStartTime || null,
            planEndDate: this.form.plannedEndTime || null,
            // 映射关键措施和风险分析到后端字段
            strategicObjectives: this.form.keyMeasures || null,
            riskAssessment: this.form.riskAnalysis || null
          }

          // 处理年度格式（el-date-picker year类型返回Date对象）
          if (submitData.planYear instanceof Date) {
            submitData.planYear = submitData.planYear.getFullYear().toString()
          }

          if (this.isEdit) {
            await updateOperationPlan(submitData)
            this.$message.success('更新成功')
          } else {
            await addOperationPlan(submitData)
            this.$message.success('新增成功')
          }
          this.$emit('refresh')
          this.handleClose()
        } catch (error) {
          console.error('保存经营计划失败:', error)
          this.$message.error('保存失败：' + (error.message || '未知错误'))
        } finally {
          this.loading = false
        }
      })
    }
  }
}
</script>

<style scoped>
.el-table {
  margin-top: 10px;
}
</style>
