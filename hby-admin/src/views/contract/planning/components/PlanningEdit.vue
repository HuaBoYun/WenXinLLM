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
          <el-form-item label="策划名称" prop="planningName">
            <el-input v-model="form.planningName" placeholder="请输入策划名称" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="策划类型" prop="planningType">
            <el-select v-model="form.planningType" placeholder="请选择策划类型" style="width: 100%">
              <el-option label="初步策划" :value="1" />
              <el-option label="详细策划" :value="2" />
              <el-option label="实施策划" :value="3" />
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
          <el-form-item label="策划人" prop="plannerId">
            <el-select v-model="form.plannerId" placeholder="请选择策划人" style="width: 100%">
              <el-option label="张三" :value="1" />
              <el-option label="李四" :value="2" />
              <el-option label="王五" :value="3" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="策划日期" prop="planningDate">
            <el-date-picker
              v-model="form.planningDate"
              type="date"
              placeholder="选择策划日期"
              style="width: 100%"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="策划状态" prop="planningStatus" v-if="isDetail">
            <el-select v-model="form.planningStatus" disabled style="width: 100%">
              <el-option label="草稿" :value="1" />
              <el-option label="待审核" :value="2" />
              <el-option label="已审核" :value="3" />
              <el-option label="已批准" :value="4" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-form-item label="项目概述" prop="projectOverview">
        <el-input
          v-model="form.projectOverview"
          type="textarea"
          :rows="3"
          placeholder="请输入项目概述"
        />
      </el-form-item>

      <el-form-item label="项目目标" prop="projectObjectives">
        <el-input
          v-model="form.projectObjectives"
          type="textarea"
          :rows="3"
          placeholder="请输入项目目标和预期成果"
        />
      </el-form-item>

      <el-form-item label="项目范围" prop="projectScope">
        <el-input
          v-model="form.projectScope"
          type="textarea"
          :rows="3"
          placeholder="请输入项目实施范围"
        />
      </el-form-item>

      <el-form-item label="交付成果" prop="deliverables">
        <el-input
          v-model="form.deliverables"
          type="textarea"
          :rows="2"
          placeholder="请输入项目交付成果清单"
        />
      </el-form-item>

      <el-form-item label="成功标准" prop="successCriteria">
        <el-input
          v-model="form.successCriteria"
          type="textarea"
          :rows="2"
          placeholder="请输入项目成功标准"
        />
      </el-form-item>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="假设条件" prop="assumptions">
            <el-input
              v-model="form.assumptions"
              type="textarea"
              :rows="2"
              placeholder="请输入项目假设条件"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="约束条件" prop="constraints">
            <el-input
              v-model="form.constraints"
              type="textarea"
              :rows="2"
              placeholder="请输入项目约束条件"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-form-item label="干系人分析" prop="stakeholderAnalysis">
        <el-input
          v-model="form.stakeholderAnalysis"
          type="textarea"
          :rows="2"
          placeholder="请输入项目干系人分析"
        />
      </el-form-item>

      <el-form-item label="沟通计划" prop="communicationPlan">
        <el-input
          v-model="form.communicationPlan"
          type="textarea"
          :rows="2"
          placeholder="请输入沟通管理计划"
        />
      </el-form-item>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="风险管理计划" prop="riskManagementPlan">
            <el-input
              v-model="form.riskManagementPlan"
              type="textarea"
              :rows="2"
              placeholder="请输入风险管理计划"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="质量管理计划" prop="qualityManagementPlan">
            <el-input
              v-model="form.qualityManagementPlan"
              type="textarea"
              :rows="2"
              placeholder="请输入质量管理计划"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-form-item label="采购管理计划" prop="procurementPlan">
        <el-input
          v-model="form.procurementPlan"
          type="textarea"
          :rows="2"
          placeholder="请输入采购管理计划"
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
    createProjectPlanning,
    updateProjectPlanning
  } from '@/api/contract/planning'

  export default {
    name: 'PlanningEdit',
    data() {
      return {
        dialogVisible: false,
        isDetail: false,
        title: '',
        form: {
          id: null,
          projectId: null,
          planningName: '',
          planningType: null,
          plannerId: null,
          planningDate: '',
          projectOverview: '',
          projectObjectives: '',
          projectScope: '',
          deliverables: '',
          successCriteria: '',
          assumptions: '',
          constraints: '',
          stakeholderAnalysis: '',
          communicationPlan: '',
          riskManagementPlan: '',
          qualityManagementPlan: '',
          procurementPlan: '',
          planningStatus: 1
        },
        rules: {
          planningName: [
            { required: true, message: '请输入策划名称', trigger: 'blur' }
          ],
          planningType: [
            { required: true, message: '请选择策划类型', trigger: 'change' }
          ],
          projectId: [
            { required: true, message: '请输入项目ID', trigger: 'blur' }
          ],
          plannerId: [
            { required: true, message: '请选择策划人', trigger: 'change' }
          ],
          planningDate: [
            { required: true, message: '请选择策划日期', trigger: 'change' }
          ]
        }
      }
    },
    methods: {
      showEdit(type, data) {
        this.dialogVisible = true
        this.isDetail = type === 'detail'
        
        if (type === 'add') {
          this.title = '新建项目策划'
          this.resetForm()
        } else if (type === 'edit') {
          this.title = '编辑项目策划'
          this.form = { ...data }
        } else if (type === 'detail') {
          this.title = '项目策划详情'
          this.form = { ...data }
        }
      },
      
      resetForm() {
        this.form = {
          id: null,
          projectId: null,
          planningName: '',
          planningType: null,
          plannerId: null,
          planningDate: '',
          projectOverview: '',
          projectObjectives: '',
          projectScope: '',
          deliverables: '',
          successCriteria: '',
          assumptions: '',
          constraints: '',
          stakeholderAnalysis: '',
          communicationPlan: '',
          riskManagementPlan: '',
          qualityManagementPlan: '',
          procurementPlan: '',
          planningStatus: 1
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

          let response
          if (this.form.id) {
            response = await updateProjectPlanning(this.form)
          } else {
            response = await createProjectPlanning(this.form)
          }

          console.log('项目策划保存接口返回数据:', response)

          // 修复：使用示例云标准状态码 1 表示成功
          if (response.code === 1) {
            this.$message.success(this.form.id ? '更新成功' : '创建成功')
            this.handleClose()
            this.$emit('fetch-data')
          } else {
            this.$message.error(response.msg || '操作失败')
          }
        } catch (error) {
          console.error('项目策划保存失败:', error)
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
