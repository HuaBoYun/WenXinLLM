<template>
  <el-dialog
    :title="title"
    :visible.sync="dialogVisible"
    width="80%"
    :close-on-click-modal="false"
    :close-on-press-escape="false"
    @close="handleClose"
  >
    <el-form
      ref="form"
      :model="form"
      :rules="rules"
      label-width="120px"
      :disabled="isDetail"
    >
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="任务书名称" prop="taskName">
            <el-input v-model="form.taskName" placeholder="请输入任务书名称" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="任务书类型" prop="taskType">
            <el-select v-model="form.taskType" placeholder="请选择任务书类型" style="width: 100%">
              <el-option label="勘察任务书" :value="1" />
              <el-option label="设计任务书" :value="2" />
              <el-option label="施工任务书" :value="3" />
              <el-option label="监理任务书" :value="4" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="项目ID" prop="projectId">
            <el-input v-model="form.projectId" placeholder="请输入项目ID" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="任务负责人" prop="taskLeaderId">
            <el-select v-model="form.taskLeaderId" placeholder="请选择任务负责人" style="width: 100%">
              <el-option label="张三" :value="1" />
              <el-option label="李四" :value="2" />
              <el-option label="王五" :value="3" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="计划开始日期" prop="plannedStartDate">
            <el-date-picker
              v-model="form.plannedStartDate"
              type="date"
              placeholder="选择计划开始日期"
              style="width: 100%"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="计划结束日期" prop="plannedEndDate">
            <el-date-picker
              v-model="form.plannedEndDate"
              type="date"
              placeholder="选择计划结束日期"
              style="width: 100%"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="任务书状态" prop="taskStatus" v-if="isDetail">
            <el-select v-model="form.taskStatus" disabled style="width: 100%">
              <el-option label="草稿" :value="1" />
              <el-option label="待审核" :value="2" />
              <el-option label="已审核" :value="3" />
              <el-option label="已批准" :value="4" />
              <el-option label="执行中" :value="5" />
              <el-option label="已完成" :value="6" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="优先级" prop="priority">
            <el-select v-model="form.priority" placeholder="请选择优先级" style="width: 100%">
              <el-option label="低" :value="1" />
              <el-option label="中" :value="2" />
              <el-option label="高" :value="3" />
              <el-option label="紧急" :value="4" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-form-item label="任务目标" prop="taskObjectives">
        <el-input
          v-model="form.taskObjectives"
          type="textarea"
          :rows="3"
          placeholder="请输入任务目标"
        />
      </el-form-item>

      <el-form-item label="任务范围" prop="taskScope">
        <el-input
          v-model="form.taskScope"
          type="textarea"
          :rows="3"
          placeholder="请输入任务范围"
        />
      </el-form-item>

      <el-form-item label="交付成果" prop="deliverables">
        <el-input
          v-model="form.deliverables"
          type="textarea"
          :rows="2"
          placeholder="请输入交付成果"
        />
      </el-form-item>

      <el-form-item label="质量要求" prop="qualityRequirements">
        <el-input
          v-model="form.qualityRequirements"
          type="textarea"
          :rows="2"
          placeholder="请输入质量要求"
        />
      </el-form-item>

      <el-form-item label="技术要求" prop="technicalRequirements">
        <el-input
          v-model="form.technicalRequirements"
          type="textarea"
          :rows="2"
          placeholder="请输入技术要求"
        />
      </el-form-item>

      <el-form-item label="安全要求" prop="safetyRequirements">
        <el-input
          v-model="form.safetyRequirements"
          type="textarea"
          :rows="2"
          placeholder="请输入安全要求"
        />
      </el-form-item>

      <el-form-item label="环保要求" prop="environmentalRequirements">
        <el-input
          v-model="form.environmentalRequirements"
          type="textarea"
          :rows="2"
          placeholder="请输入环保要求"
        />
      </el-form-item>

      <el-form-item label="资源配置" prop="resourceAllocation">
        <el-input
          v-model="form.resourceAllocation"
          type="textarea"
          :rows="2"
          placeholder="请输入资源配置要求"
        />
      </el-form-item>

      <el-form-item label="风险识别" prop="riskIdentification">
        <el-input
          v-model="form.riskIdentification"
          type="textarea"
          :rows="2"
          placeholder="请输入风险识别"
        />
      </el-form-item>

      <el-form-item label="应急预案" prop="contingencyPlan">
        <el-input
          v-model="form.contingencyPlan"
          type="textarea"
          :rows="2"
          placeholder="请输入应急预案"
        />
      </el-form-item>

      <el-form-item label="验收标准" prop="acceptanceCriteria">
        <el-input
          v-model="form.acceptanceCriteria"
          type="textarea"
          :rows="2"
          placeholder="请输入验收标准"
        />
      </el-form-item>

      <el-form-item label="备注" prop="remarks">
        <el-input
          v-model="form.remarks"
          type="textarea"
          :rows="2"
          placeholder="请输入备注"
        />
      </el-form-item>
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取消</el-button>
      <el-button type="primary" @click="handleSave" v-if="!isDetail">保存</el-button>
    </div>
  </el-dialog>
</template>

<script>
  import {
    createTaskAssignment,
    updateTaskAssignment
  } from '@/api/contract/task'

  export default {
    name: 'TaskEdit',
    data() {
      return {
        dialogVisible: false,
        isDetail: false,
        title: '',
        form: {
          id: null,
          projectId: null,
          taskName: '',
          taskType: null,
          taskLeaderId: null,
          plannedStartDate: '',
          plannedEndDate: '',
          priority: 2,
          taskObjectives: '',
          taskScope: '',
          deliverables: '',
          qualityRequirements: '',
          technicalRequirements: '',
          safetyRequirements: '',
          environmentalRequirements: '',
          resourceAllocation: '',
          riskIdentification: '',
          contingencyPlan: '',
          acceptanceCriteria: '',
          remarks: '',
          taskStatus: 1
        },
        rules: {
          taskName: [
            { required: true, message: '请输入任务书名称', trigger: 'blur' }
          ],
          taskType: [
            { required: true, message: '请选择任务书类型', trigger: 'change' }
          ],
          projectId: [
            { required: true, message: '请输入项目ID', trigger: 'blur' }
          ],
          taskLeaderId: [
            { required: true, message: '请选择任务负责人', trigger: 'change' }
          ],
          plannedStartDate: [
            { required: true, message: '请选择计划开始日期', trigger: 'change' }
          ],
          plannedEndDate: [
            { required: true, message: '请选择计划结束日期', trigger: 'change' }
          ]
        }
      }
    },
    methods: {
      showEdit(type, data) {
        this.dialogVisible = true
        this.isDetail = type === 'detail'
        
        if (type === 'add') {
          this.title = '新建任务书'
          this.resetForm()
        } else if (type === 'edit') {
          this.title = '编辑任务书'
          this.form = { ...data }
        } else if (type === 'detail') {
          this.title = '任务书详情'
          this.form = { ...data }
        }
      },
      
      resetForm() {
        this.form = {
          id: null,
          projectId: null,
          taskName: '',
          taskType: null,
          taskLeaderId: null,
          plannedStartDate: '',
          plannedEndDate: '',
          priority: 2,
          taskObjectives: '',
          taskScope: '',
          deliverables: '',
          qualityRequirements: '',
          technicalRequirements: '',
          safetyRequirements: '',
          environmentalRequirements: '',
          resourceAllocation: '',
          riskIdentification: '',
          contingencyPlan: '',
          acceptanceCriteria: '',
          remarks: '',
          taskStatus: 1
        }
        this.$nextTick(() => {
          this.$refs.form && this.$refs.form.clearValidate()
        })
      },
      
      handleClose() {
        this.dialogVisible = false
        this.resetForm()
      },
      
      async handleSave() {
        try {
          await this.$refs.form.validate()

          // 字段映射：前端字段名 -> 后端字段名
          const submitData = {
            ...this.form,
            assignedPersonId: this.form.taskLeaderId, // 任务负责人ID映射
            issuerId: this.form.taskLeaderId || 1, // 发布人ID，暂时使用负责人ID或默认值
            issueDate: new Date().toISOString().split('T')[0] // 发布日期，默认为当前日期
          }

          let response
          if (this.form.id) {
            response = await updateTaskAssignment(this.form.id, submitData)
          } else {
            response = await createTaskAssignment(submitData)
          }
          
          if (response.code === 1) {
            this.$message.success(this.form.id ? '更新成功' : '创建成功')
            this.handleClose()
            this.$emit('fetch-data')
          } else {
            this.$message.error(response.msg || '操作失败')
          }
        } catch (error) {
          if (error.message) {
            this.$message.error('操作失败：' + error.message)
          }
        }
      }
    }
  }
</script>

<style scoped>
  .dialog-footer {
    text-align: right;
  }
</style>
