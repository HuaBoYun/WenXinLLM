<template>
  <div>
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
          <el-form-item label="项目名称" prop="projectName">
            <el-input v-model="form.projectName" placeholder="请选择项目" readonly>
              <el-button slot="append" @click="showProjectSelector">选择项目</el-button>
            </el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="变更类型" prop="changeType">
            <el-select v-model="form.changeType" placeholder="请选择变更类型" style="width: 100%">
              <el-option label="范围变更" :value="1" />
              <el-option label="时间变更" :value="2" />
              <el-option label="成本变更" :value="3" />
              <el-option label="质量变更" :value="4" />
              <el-option label="资源变更" :value="5" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="变更标题" prop="changeTitle">
            <el-input v-model="form.changeTitle" placeholder="请输入变更标题" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="变更ID" prop="changeId">
            <el-input v-model="form.changeId" placeholder="请输入变更ID" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="申请人" prop="applicantId">
            <el-select v-model="form.applicantId" placeholder="请选择申请人" style="width: 100%">
              <el-option label="张三" :value="1" />
              <el-option label="李四" :value="2" />
              <el-option label="王五" :value="3" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="变更类别" prop="changeCategory">
            <el-select v-model="form.changeCategory" placeholder="请选择变更类别" style="width: 100%">
              <el-option label="设计变更" :value="1" />
              <el-option label="工程变更" :value="2" />
              <el-option label="合同变更" :value="3" />
              <el-option label="进度变更" :value="4" />
              <el-option label="成本变更" :value="5" />
              <el-option label="其他变更" :value="6" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="影响程度" prop="impactLevel">
            <el-select v-model="form.impactLevel" placeholder="请选择影响程度" style="width: 100%">
              <el-option label="低" :value="1" />
              <el-option label="中" :value="2" />
              <el-option label="高" :value="3" />
              <el-option label="极高" :value="4" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="紧急程度" prop="urgencyLevel">
            <el-select v-model="form.urgencyLevel" placeholder="请选择紧急程度" style="width: 100%">
              <el-option label="低" :value="1" />
              <el-option label="中" :value="2" />
              <el-option label="高" :value="3" />
              <el-option label="紧急" :value="4" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="预估成本" prop="estimatedCost">
            <el-input-number
              v-model="form.estimatedCost"
              :min="0"
              :precision="2"
              placeholder="请输入预估成本"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="预估工期(天)" prop="estimatedDuration">
            <el-input-number
              v-model="form.estimatedDuration"
              :min="0"
              placeholder="请输入预估工期"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="申请日期" prop="applicationDate">
            <el-date-picker
              v-model="form.applicationDate"
              type="date"
              placeholder="选择申请日期"
              style="width: 100%"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="计划完成日期" prop="plannedCompletionDate">
            <el-date-picker
              v-model="form.plannedCompletionDate"
              type="date"
              placeholder="选择计划完成日期"
              style="width: 100%"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-form-item label="变更原因" prop="changeReason">
        <el-input
          v-model="form.changeReason"
          type="textarea"
          :rows="3"
          placeholder="请输入变更原因"
        />
      </el-form-item>

      <el-form-item label="变更内容" prop="changeContent">
        <el-input
          v-model="form.changeContent"
          type="textarea"
          :rows="4"
          placeholder="请输入变更内容"
        />
      </el-form-item>

      <el-form-item label="影响分析" prop="impactAnalysis">
        <el-input
          v-model="form.impactAnalysis"
          type="textarea"
          :rows="3"
          placeholder="请输入影响分析"
        />
      </el-form-item>

      <el-form-item label="风险评估" prop="riskAssessment">
        <el-input
          v-model="form.riskAssessment"
          type="textarea"
          :rows="3"
          placeholder="请输入风险评估"
        />
      </el-form-item>

      <el-form-item label="实施方案" prop="implementationPlan">
        <el-input
          v-model="form.implementationPlan"
          type="textarea"
          :rows="3"
          placeholder="请输入实施方案"
        />
      </el-form-item>

      <el-form-item label="资源需求" prop="resourceRequirements">
        <el-input
          v-model="form.resourceRequirements"
          type="textarea"
          :rows="2"
          placeholder="请输入资源需求"
        />
      </el-form-item>

      <el-form-item label="质量标准" prop="qualityStandards">
        <el-input
          v-model="form.qualityStandards"
          type="textarea"
          :rows="2"
          placeholder="请输入质量标准"
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

      <el-form-item label="变更状态" prop="changeStatus" v-if="isDetail">
        <el-select v-model="form.changeStatus" disabled style="width: 100%">
          <el-option label="待审核" :value="1" />
          <el-option label="审核中" :value="2" />
          <el-option label="已批准" :value="3" />
          <el-option label="已拒绝" :value="4" />
          <el-option label="已实施" :value="5" />
          <el-option label="已关闭" :value="6" />
        </el-select>
      </el-form-item>
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取消</el-button>
      <el-button type="primary" @click="handleSave" v-if="!isDetail">保存</el-button>
    </div>
  </el-dialog>

  <!-- 项目选择对话框 -->
  <el-dialog
    title="选择项目"
    :visible.sync="projectSelectorVisible"
    width="80%"
    :close-on-click-modal="false"
  >
    <el-form :inline="true" :model="projectQuery" class="demo-form-inline">
      <el-form-item label="项目名称">
        <el-input v-model="projectQuery.projectName" placeholder="请输入项目名称" clearable />
      </el-form-item>
      <el-form-item label="发包方">
        <el-input v-model="projectQuery.contractorFullName" placeholder="请输入发包方" clearable />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="fetchProjectList">查询</el-button>
        <el-button @click="resetProjectQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-table
      v-loading="projectListLoading"
      :data="projectList"
      @selection-change="handleProjectSelectionChange"
      style="width: 100%"
    >
      <el-table-column type="selection" width="55" />
      <el-table-column prop="registerNo" label="登记编号" width="150" />
      <el-table-column prop="projectName" label="项目名称" min-width="200" />
      <el-table-column prop="contractorFullName" label="发包方" width="200" />
      <el-table-column prop="projectStatusName" label="项目状态" width="100" />
      <el-table-column prop="registerTime" label="登记时间" width="150">
        <template #default="{ row }">
          {{ formatDate(row.registerTime) }}
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
      background
      :current-page="projectQuery.pageNumber"
      :page-size="projectQuery.pageSize"
      :total="projectTotal"
      layout="total, sizes, prev, pager, next, jumper"
      @size-change="handleProjectSizeChange"
      @current-change="handleProjectCurrentChange"
      style="margin-top: 20px"
    />

    <div slot="footer" class="dialog-footer">
      <el-button @click="projectSelectorVisible = false">取消</el-button>
      <el-button type="primary" @click="confirmProjectSelection">确定</el-button>
    </div>
  </el-dialog>
  </div>
</template>

<script>
  import {
    createProjectChange,
    updateProjectChange,
    getProjectList
  } from '@/api/contract/change'
  import { formatDate } from '@/utils/index'

  export default {
    name: 'ChangeEdit',
    data() {
      return {
        dialogVisible: false,
        isDetail: false,
        title: '',
        form: {
          id: null,
          projectId: null,
          projectName: '',
          changeTitle: '',
          changeType: null,
          changeId: '',
          applicantId: null,
          impactLevel: null,
          urgencyLevel: null,
          estimatedCost: null,
          estimatedDuration: null,
          applicationDate: '',
          plannedCompletionDate: '',
          changeReason: '',
          changeContent: '',
          impactAnalysis: '',
          riskAssessment: '',
          implementationPlan: '',
          resourceRequirements: '',
          qualityStandards: '',
          acceptanceCriteria: '',
          remarks: '',
          changeStatus: 1
        },
        // 项目选择相关数据
        projectSelectorVisible: false,
        projectListLoading: false,
        projectList: [],
        projectTotal: 0,
        selectedProjects: [],
        projectQuery: {
          projectName: '',
          contractorFullName: '',
          pageNumber: 1,
          pageSize: 10
        },
        rules: {
          projectName: [
            { required: true, message: '请输入项目名称', trigger: 'blur' }
          ],
          changeTitle: [
            { required: true, message: '请输入变更标题', trigger: 'blur' }
          ],
          changeType: [
            { required: true, message: '请选择变更类型', trigger: 'change' }
          ],
          changeId: [
            { required: true, message: '请输入变更ID', trigger: 'blur' }
          ],
          applicantId: [
            { required: true, message: '请选择申请人', trigger: 'change' }
          ],
          impactLevel: [
            { required: true, message: '请选择影响程度', trigger: 'change' }
          ],
          urgencyLevel: [
            { required: true, message: '请选择紧急程度', trigger: 'change' }
          ],
          applicationDate: [
            { required: true, message: '请选择申请日期', trigger: 'change' }
          ],
          changeReason: [
            { required: true, message: '请输入变更原因', trigger: 'blur' }
          ],
          changeContent: [
            { required: true, message: '请输入变更内容', trigger: 'blur' }
          ]
        }
      }
    },
    methods: {
      showEdit(type, data) {
        this.dialogVisible = true
        this.isDetail = type === 'detail'

        if (type === 'add') {
          this.title = '新建变更申请'
          this.resetForm()
        } else if (type === 'edit') {
          this.title = '编辑变更申请'
          this.form = { ...data }
        } else if (type === 'detail') {
          this.title = '变更申请详情'
          this.form = { ...data }
        }
      },
      
      resetForm() {
        this.form = {
          id: null,
          projectId: null,
          projectName: '',
          changeTitle: '',
          changeType: null,
          changeId: '',
          applicantId: null,
          impactLevel: null,
          urgencyLevel: null,
          estimatedCost: null,
          estimatedDuration: null,
          applicationDate: '',
          plannedCompletionDate: '',
          changeReason: '',
          changeContent: '',
          impactAnalysis: '',
          riskAssessment: '',
          implementationPlan: '',
          resourceRequirements: '',
          qualityStandards: '',
          acceptanceCriteria: '',
          remarks: '',
          changeStatus: 1
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
            // 编辑时，确保ID存在且有效
            if (!this.form.id || this.form.id === 'undefined') {
              this.$message.error('编辑数据异常，请重新打开编辑页面')
              return
            }
            response = await updateProjectChange(this.form)
          } else {
            response = await createProjectChange(this.form)
          }

          if (response.code === 1) {
            this.$message.success(this.form.id ? '更新成功' : '创建成功')
            this.handleClose()
            this.$emit('fetch-data')
          } else {
            this.$message.error(response.msg || '操作失败')
          }
        } catch (error) {
          console.error('保存失败:', error)
          this.$message.error('保存失败，请重试')
        }
      },

      // 项目选择相关方法
      showProjectSelector() {
        this.projectSelectorVisible = true
        this.fetchProjectList()
      },

      async fetchProjectList() {
        this.projectListLoading = true
        try {
          const response = await getProjectList(this.projectQuery)
          if (response.code === 1) {
            this.projectList = response.data.list || []
            this.projectTotal = response.data.total || 0
          } else {
            this.$message.error(response.msg || '获取项目列表失败')
          }
        } catch (error) {
          this.$message.error('获取项目列表失败：' + error.message)
        } finally {
          this.projectListLoading = false
        }
      },

      resetProjectQuery() {
        this.projectQuery = {
          projectName: '',
          contractorFullName: '',
          pageNumber: 1,
          pageSize: 10
        }
        this.fetchProjectList()
      },

      handleProjectSelectionChange(selection) {
        this.selectedProjects = selection
      },

      handleProjectSizeChange(val) {
        this.projectQuery.pageSize = val
        this.fetchProjectList()
      },

      handleProjectCurrentChange(val) {
        this.projectQuery.pageNumber = val
        this.fetchProjectList()
      },

      confirmProjectSelection() {
        if (this.selectedProjects.length === 0) {
          this.$message.warning('请选择一个项目')
          return
        }
        if (this.selectedProjects.length > 1) {
          this.$message.warning('只能选择一个项目')
          return
        }

        const selectedProject = this.selectedProjects[0]
        this.form.projectId = selectedProject.projectId
        this.form.projectName = selectedProject.projectName
        this.projectSelectorVisible = false
        this.selectedProjects = []
      },

      formatDate(date) {
        return formatDate(date)
      }
    }
  }
</script>

<style scoped>
  .dialog-footer {
    text-align: right;
  }
</style>
