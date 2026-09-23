<template>
  <el-dialog
    title="变更实施"
    :visible.sync="dialogVisible"
    width="80%"
    :close-on-click-modal="false"
    :close-on-press-escape="false"
    @close="handleClose"
  >
    <el-tabs v-model="activeTab" type="card">
      <!-- 变更信息 -->
      <el-tab-pane label="变更信息" name="info">
        <el-form
          ref="infoForm"
          :model="changeInfo"
          label-width="120px"
          :disabled="true"
        >
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="项目名称">
                <el-input v-model="changeInfo.projectName" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="变更类型">
                <el-tag :type="getChangeTypeType(changeInfo.changeType)">
                  {{ getChangeTypeName(changeInfo.changeType) }}
                </el-tag>
              </el-form-item>
            </el-col>
          </el-row>

          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="变更ID">
                <el-input v-model="changeInfo.changeId" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="申请人">
                <el-input v-model="changeInfo.applicantName" />
              </el-form-item>
            </el-col>
          </el-row>

          <el-form-item label="变更内容">
            <el-input
              v-model="changeInfo.changeContent"
              type="textarea"
              :rows="4"
            />
          </el-form-item>

          <el-form-item label="实施方案">
            <el-input
              v-model="changeInfo.implementationPlan"
              type="textarea"
              :rows="3"
            />
          </el-form-item>
        </el-form>
      </el-tab-pane>

      <!-- 实施计划 -->
      <el-tab-pane label="实施计划" name="plan">
        <div style="margin-bottom: 20px;">
          <el-button type="primary" size="small" icon="el-icon-plus" @click="handleAddTask">
            添加实施任务
          </el-button>
        </div>

        <el-table :data="implementationTasks" border style="width: 100%">
          <el-table-column label="任务名称" prop="taskName" min-width="200" />
          <el-table-column label="负责人" prop="assigneeName" width="120" />
          <el-table-column label="计划开始" prop="plannedStartDate" width="120" />
          <el-table-column label="计划结束" prop="plannedEndDate" width="120" />
          <el-table-column label="实际开始" prop="actualStartDate" width="120" />
          <el-table-column label="实际结束" prop="actualEndDate" width="120" />
          <el-table-column label="完成进度" prop="completionProgress" width="120">
            <template #default="{ row }">
              <el-progress :percentage="row.completionProgress || 0" :stroke-width="6" />
            </template>
          </el-table-column>
          <el-table-column label="任务状态" prop="taskStatus" width="100">
            <template #default="{ row }">
              <el-tag :type="getTaskStatusType(row.taskStatus)">
                {{ getTaskStatusName(row.taskStatus) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="150" fixed="right">
            <template #default="{ row, $index }">
              <el-button type="text" size="small" @click="handleEditTask(row, $index)">编辑</el-button>
              <el-button type="text" size="small" style="color: #f56c6c;" @click="handleDeleteTask($index)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>

      <!-- 实施记录 -->
      <el-tab-pane label="实施记录" name="record">
        <div style="margin-bottom: 20px;">
          <el-button type="primary" size="small" icon="el-icon-plus" @click="handleAddRecord">
            添加实施记录
          </el-button>
        </div>

        <el-table :data="implementationRecords" border style="width: 100%">
          <el-table-column label="记录时间" prop="recordTime" width="150" />
          <el-table-column label="记录人" prop="recorderName" width="120" />
          <el-table-column label="实施内容" prop="implementationContent" min-width="200" />
          <el-table-column label="完成情况" prop="completionStatus" width="100">
            <template #default="{ row }">
              <el-tag :type="getCompletionStatusType(row.completionStatus)">
                {{ getCompletionStatusName(row.completionStatus) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="遇到问题" prop="encounteredIssues" min-width="200" />
          <el-table-column label="操作" width="100" fixed="right">
            <template #default="{ row, $index }">
              <el-button type="text" size="small" style="color: #f56c6c;" @click="handleDeleteRecord($index)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>

      <!-- 实施总结 -->
      <el-tab-pane label="实施总结" name="summary">
        <el-form
          ref="summaryForm"
          :model="summaryForm"
          :rules="summaryRules"
          label-width="120px"
        >
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="实施负责人" prop="implementationManagerId">
                <el-select v-model="summaryForm.implementationManagerId" placeholder="请选择实施负责人" style="width: 100%">
                  <el-option label="张三" :value="1" />
                  <el-option label="李四" :value="2" />
                  <el-option label="王五" :value="3" />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="实施状态" prop="implementationStatus">
                <el-select v-model="summaryForm.implementationStatus" placeholder="请选择实施状态" style="width: 100%">
                  <el-option label="实施中" :value="1" />
                  <el-option label="已完成" :value="2" />
                  <el-option label="部分完成" :value="3" />
                  <el-option label="暂停" :value="4" />
                  <el-option label="失败" :value="5" />
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>

          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="实际开始日期" prop="actualStartDate">
                <el-date-picker
                  v-model="summaryForm.actualStartDate"
                  type="date"
                  placeholder="选择实际开始日期"
                  style="width: 100%"
                  format="yyyy-MM-dd"
                  value-format="yyyy-MM-dd"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="实际结束日期" prop="actualEndDate">
                <el-date-picker
                  v-model="summaryForm.actualEndDate"
                  type="date"
                  placeholder="选择实际结束日期"
                  style="width: 100%"
                  format="yyyy-MM-dd"
                  value-format="yyyy-MM-dd"
                />
              </el-form-item>
            </el-col>
          </el-row>

          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="实际成本" prop="actualCost">
                <el-input-number
                  v-model="summaryForm.actualCost"
                  :min="0"
                  :precision="2"
                  style="width: 100%"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="完成进度(%)" prop="overallProgress">
                <el-input-number
                  v-model="summaryForm.overallProgress"
                  :min="0"
                  :max="100"
                  :precision="2"
                  style="width: 100%"
                />
              </el-form-item>
            </el-col>
          </el-row>

          <el-form-item label="实施总结" prop="implementationSummary">
            <el-input
              v-model="summaryForm.implementationSummary"
              type="textarea"
              :rows="4"
              placeholder="请输入实施总结"
            />
          </el-form-item>

          <el-form-item label="遇到的问题" prop="encounteredProblems">
            <el-input
              v-model="summaryForm.encounteredProblems"
              type="textarea"
              :rows="3"
              placeholder="请输入遇到的问题"
            />
          </el-form-item>

          <el-form-item label="解决方案" prop="solutions">
            <el-input
              v-model="summaryForm.solutions"
              type="textarea"
              :rows="3"
              placeholder="请输入解决方案"
            />
          </el-form-item>

          <el-form-item label="经验教训" prop="lessonsLearned">
            <el-input
              v-model="summaryForm.lessonsLearned"
              type="textarea"
              :rows="3"
              placeholder="请输入经验教训"
            />
          </el-form-item>

          <el-form-item label="后续建议" prop="followUpRecommendations">
            <el-input
              v-model="summaryForm.followUpRecommendations"
              type="textarea"
              :rows="2"
              placeholder="请输入后续建议"
            />
          </el-form-item>
        </el-form>
      </el-tab-pane>
    </el-tabs>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取消</el-button>
      <el-button type="primary" @click="handleSubmitImplementation">提交实施</el-button>
    </div>
  </el-dialog>
</template>

<script>
  import {
    submitProjectChangeImplementation,
    getProjectChangeImplementationData
  } from '@/api/contract/change'

  export default {
    name: 'ChangeImplement',
    data() {
      return {
        dialogVisible: false,
        activeTab: 'info',
        changeInfo: {},
        implementationTasks: [],
        implementationRecords: [],
        summaryForm: {
          implementationManagerId: null,
          implementationStatus: null,
          actualStartDate: '',
          actualEndDate: '',
          actualCost: null,
          overallProgress: 0,
          implementationSummary: '',
          encounteredProblems: '',
          solutions: '',
          lessonsLearned: '',
          followUpRecommendations: ''
        },
        summaryRules: {
          implementationManagerId: [
            { required: true, message: '请选择实施负责人', trigger: 'change' }
          ],
          implementationStatus: [
            { required: true, message: '请选择实施状态', trigger: 'change' }
          ],
          actualStartDate: [
            { required: true, message: '请选择实际开始日期', trigger: 'change' }
          ],
          implementationSummary: [
            { required: true, message: '请输入实施总结', trigger: 'blur' }
          ]
        }
      }
    },
    methods: {
      async showEdit(data) {
        this.dialogVisible = true
        this.changeInfo = { ...data }
        await this.loadImplementationData()
      },

      async loadImplementationData() {
        try {
          const response = await getProjectChangeImplementationData({
            changeId: this.changeInfo.id
          })
          if (response.code === 200) {
            const data = response.data || {}
            this.implementationTasks = data.implementationTasks || []
            this.implementationRecords = data.implementationRecords || []
            if (data.summaryForm) {
              this.summaryForm = { ...data.summaryForm }
            }
          }
        } catch (error) {
          console.error('加载实施数据失败：', error)
        }
      },

      handleClose() {
        this.dialogVisible = false
        this.changeInfo = {}
        this.implementationTasks = []
        this.implementationRecords = []
        this.activeTab = 'info'
        this.resetSummaryForm()
      },

      resetSummaryForm() {
        this.summaryForm = {
          implementationManagerId: null,
          implementationStatus: null,
          actualStartDate: '',
          actualEndDate: '',
          actualCost: null,
          overallProgress: 0,
          implementationSummary: '',
          encounteredProblems: '',
          solutions: '',
          lessonsLearned: '',
          followUpRecommendations: ''
        }
      },

      handleAddTask() {
        this.$message.info('添加实施任务功能待实现')
      },

      handleEditTask(row, index) {
        this.$message.info('编辑实施任务功能待实现')
      },

      handleDeleteTask(index) {
        this.$confirm('确定要删除这个实施任务吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.implementationTasks.splice(index, 1)
          this.$message.success('删除成功')
        })
      },

      handleAddRecord() {
        this.$message.info('添加实施记录功能待实现')
      },

      handleDeleteRecord(index) {
        this.$confirm('确定要删除这条实施记录吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.implementationRecords.splice(index, 1)
          this.$message.success('删除成功')
        })
      },

      async handleSubmitImplementation() {
        try {
          await this.$refs.summaryForm.validate()

          const implementationData = {
            changeId: this.changeInfo.id,
            implementationTasks: this.implementationTasks,
            implementationRecords: this.implementationRecords,
            summaryForm: this.summaryForm
          }

          const response = await submitProjectChangeImplementation(implementationData)
          if (response.code === 200) {
            this.$message.success('实施提交成功')
            this.handleClose()
            this.$emit('fetch-data')
          } else {
            this.$message.error(response.message || '实施提交失败')
          }
        } catch (error) {
          if (error.message) {
            this.$message.error('实施提交失败：' + error.message)
          }
        }
      },

      getChangeTypeName(type) {
        const typeMap = {
          1: '范围变更',
          2: '时间变更',
          3: '成本变更',
          4: '质量变更',
          5: '资源变更'
        }
        return typeMap[type] || '未知'
      },

      getChangeTypeType(type) {
        const typeMap = {
          1: 'primary',
          2: 'success',
          3: 'warning',
          4: 'danger',
          5: 'info'
        }
        return typeMap[type] || 'info'
      },

      getTaskStatusName(status) {
        const statusMap = {
          1: '未开始',
          2: '进行中',
          3: '已完成',
          4: '已延期',
          5: '已暂停'
        }
        return statusMap[status] || '未知'
      },

      getTaskStatusType(status) {
        const typeMap = {
          1: 'info',
          2: 'primary',
          3: 'success',
          4: 'danger',
          5: 'warning'
        }
        return typeMap[status] || 'info'
      },

      getCompletionStatusName(status) {
        const statusMap = {
          1: '按计划完成',
          2: '提前完成',
          3: '延期完成',
          4: '部分完成',
          5: '未完成'
        }
        return statusMap[status] || '未知'
      },

      getCompletionStatusType(status) {
        const typeMap = {
          1: 'success',
          2: 'success',
          3: 'warning',
          4: 'primary',
          5: 'danger'
        }
        return typeMap[status] || 'info'
      }
    }
  }
</script>

<style scoped>
  .dialog-footer {
    text-align: right;
  }
</style>
