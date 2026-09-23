<template>
  <el-dialog
    :title="`进度管理 - ${managementInfo.projectName || '未知项目'}`"
    :visible.sync="dialogVisible"
    width="90%"
    :close-on-click-modal="false"
    :close-on-press-escape="false"
    @close="handleClose"
  >
    <!-- 项目信息概览 -->
    <div class="project-info-header" style="margin-bottom: 20px; padding: 15px; background: #f5f7fa; border-radius: 4px;">
      <el-row :gutter="20">
        <el-col :span="6">
          <div><strong>项目名称：</strong>{{ managementInfo.projectName || '-' }}</div>
        </el-col>
        <el-col :span="6">
          <div><strong>项目ID：</strong>{{ managementInfo.projectId || '-' }}</div>
        </el-col>
        <el-col :span="6">
          <div><strong>负责人：</strong>{{ managementInfo.managerName || '-' }}</div>
        </el-col>
        <el-col :span="6">
          <div><strong>管理状态：</strong>
            <el-tag :type="getManagementStatusType(managementInfo.managementStatus)" size="mini">
              {{ getManagementStatusName(managementInfo.managementStatus) }}
            </el-tag>
          </div>
        </el-col>
      </el-row>
    </div>

    <el-tabs v-model="activeTab" type="card">
      <!-- 进度计划 -->
      <el-tab-pane label="进度计划" name="plan">
        <div style="margin-bottom: 20px;">
          <el-button type="primary" size="small" icon="el-icon-plus" @click="handleAddTask">
            添加任务节点
          </el-button>
        </div>

        <el-table :data="taskList" border style="width: 100%">
          <el-table-column label="里程碑名称" prop="milestoneName" min-width="200" />
          <el-table-column label="里程碑类型" prop="milestoneType" width="120">
            <template #default="{ row }">
              <el-tag :type="getMilestoneTypeTag(row.milestoneType)" size="mini">
                {{ getMilestoneTypeName(row.milestoneType) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="计划开始" prop="plannedStartDate" width="120" />
          <el-table-column label="计划结束" prop="plannedEndDate" width="120" />
          <el-table-column label="实际开始" prop="actualStartDate" width="120" />
          <el-table-column label="实际结束" prop="actualEndDate" width="120" />
          <el-table-column label="完成进度" prop="actualProgress" width="120">
            <template #default="{ row }">
              <el-progress :percentage="row.actualProgress || 0" :stroke-width="6" />
            </template>
          </el-table-column>
          <el-table-column label="进度状态" prop="progressStatus" width="100">
            <template #default="{ row }">
              <el-tag :type="getTaskStatusType(row.progressStatus)">
                {{ getTaskStatusName(row.progressStatus) }}
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

      <!-- 进度统计 -->
      <el-tab-pane label="进度统计" name="statistics">
        <div class="progress-statistics">
          <el-row :gutter="20">
            <el-col :span="6">
              <el-card>
                <div slot="header">
                  <span>总任务数</span>
                </div>
                <div class="stat-item">
                  <span class="stat-number">{{ progressSummary.totalTasks }}</span>
                </div>
              </el-card>
            </el-col>
            <el-col :span="6">
              <el-card>
                <div slot="header">
                  <span>已完成</span>
                </div>
                <div class="stat-item">
                  <span class="stat-number completed">{{ progressSummary.completedTasks }}</span>
                </div>
              </el-card>
            </el-col>
            <el-col :span="6">
              <el-card>
                <div slot="header">
                  <span>进行中</span>
                </div>
                <div class="stat-item">
                  <span class="stat-number in-progress">{{ progressSummary.inProgressTasks }}</span>
                </div>
              </el-card>
            </el-col>
            <el-col :span="6">
              <el-card>
                <div slot="header">
                  <span>延期任务</span>
                </div>
                <div class="stat-item">
                  <span class="stat-number delayed">{{ progressSummary.delayedTasks }}</span>
                </div>
              </el-card>
            </el-col>
          </el-row>

          <el-row :gutter="20" style="margin-top: 20px;">
            <el-col :span="12">
              <el-card>
                <div slot="header">
                  <span>整体进度</span>
                </div>
                <el-progress 
                  :percentage="progressSummary.overallProgress" 
                  :stroke-width="20"
                  :color="getProgressColor(progressSummary.overallProgress)"
                />
              </el-card>
            </el-col>
            <el-col :span="12">
              <el-card>
                <div slot="header">
                  <span>进度状态</span>
                </div>
                <div class="progress-status">
                  <el-tag :type="getProgressStatusType(progressSummary.scheduleVariance)" size="large">
                    {{ getProgressStatusText(progressSummary.scheduleVariance) }}
                  </el-tag>
                </div>
              </el-card>
            </el-col>
          </el-row>
        </div>
      </el-tab-pane>

      <!-- 里程碑管理 -->
      <el-tab-pane label="里程碑管理" name="milestone">
        <div style="margin-bottom: 20px;">
          <el-button type="primary" size="small" icon="el-icon-plus" @click="handleAddMilestone">
            添加里程碑
          </el-button>
        </div>

        <el-table :data="milestoneList" border style="width: 100%">
          <el-table-column label="里程碑名称" prop="milestoneName" min-width="200" />
          <el-table-column label="计划日期" prop="plannedDate" width="120" />
          <el-table-column label="实际日期" prop="actualDate" width="120" />
          <el-table-column label="重要程度" prop="importance" width="100">
            <template #default="{ row }">
              <el-rate v-model="row.importance" :max="5" disabled show-score />
            </template>
          </el-table-column>
          <el-table-column label="完成状态" prop="completionStatus" width="100">
            <template #default="{ row }">
              <el-tag :type="getMilestoneStatusType(row.completionStatus)">
                {{ getMilestoneStatusName(row.completionStatus) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="描述" prop="description" min-width="200" />
          <el-table-column label="操作" width="150" fixed="right">
            <template #default="{ row, $index }">
              <el-button type="text" size="small" @click="handleEditMilestone(row, $index)">编辑</el-button>
              <el-button type="text" size="small" style="color: #f56c6c;" @click="handleDeleteMilestone($index)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>
    </el-tabs>

    <!-- 任务编辑弹窗 -->
    <el-dialog
      :title="taskEditTitle"
      :visible.sync="taskEditDialogVisible"
      width="60%"
      append-to-body
    >
      <el-form
        ref="taskEditForm"
        :model="taskEditForm"
        :rules="taskEditRules"
        label-width="120px"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="里程碑名称" prop="milestoneName">
              <el-input v-model="taskEditForm.milestoneName" placeholder="请输入里程碑名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="里程碑类型" prop="milestoneType">
              <el-select v-model="taskEditForm.milestoneType" placeholder="请选择里程碑类型" style="width: 100%">
                <el-option label="项目开始" :value="1" />
                <el-option label="关键节点" :value="2" />
                <el-option label="项目完成" :value="3" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="负责人" prop="responsiblePersonId">
              <el-select v-model="taskEditForm.responsiblePersonId" placeholder="请选择负责人" style="width: 100%">
                <el-option label="张三" :value="1" />
                <el-option label="李四" :value="2" />
                <el-option label="王五" :value="3" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="汇报人" prop="reporterId">
              <el-select v-model="taskEditForm.reporterId" placeholder="请选择汇报人" style="width: 100%">
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
                v-model="taskEditForm.plannedStartDate"
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
                v-model="taskEditForm.plannedEndDate"
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
            <el-form-item label="实际开始日期" prop="actualStartDate">
              <el-date-picker
                v-model="taskEditForm.actualStartDate"
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
                v-model="taskEditForm.actualEndDate"
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
            <el-form-item label="实际进度(%)" prop="actualProgress">
              <el-input-number
                v-model="taskEditForm.actualProgress"
                :min="0"
                :max="100"
                :precision="2"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="进度状态" prop="progressStatus">
              <el-select v-model="taskEditForm.progressStatus" placeholder="请选择进度状态" style="width: 100%">
                <el-option label="未开始" :value="1" />
                <el-option label="进行中" :value="2" />
                <el-option label="已完成" :value="3" />
                <el-option label="已暂停" :value="4" />
                <el-option label="已取消" :value="5" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="进度描述" prop="progressDescription">
          <el-input
            v-model="taskEditForm.progressDescription"
            type="textarea"
            :rows="3"
            placeholder="请输入进度描述"
          />
        </el-form-item>

        <el-form-item label="备注" prop="remarks">
          <el-input
            v-model="taskEditForm.remarks"
            type="textarea"
            :rows="2"
            placeholder="请输入备注"
          />
        </el-form-item>
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button @click="taskEditDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSaveTaskEdit">确定</el-button>
      </div>
    </el-dialog>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">关闭</el-button>
      <el-button type="primary" @click="handleSaveAll">保存全部</el-button>
    </div>
  </el-dialog>
</template>

<script>
  import {
    getProgressManagementData,
    saveProgressManagementData,
    updateProgressRecord,
    createProgressRecord
  } from '@/api/contract/management'

  export default {
    name: 'ProgressManagement',
    data() {
      return {
        dialogVisible: false,
        activeTab: 'plan',
        managementInfo: {},
        taskList: [],
        milestoneList: [],
        progressSummary: {
          totalTasks: 0,
          completedTasks: 0,
          inProgressTasks: 0,
          delayedTasks: 0,
          overallProgress: 0,
          scheduleVariance: 0
        },
        taskEditDialogVisible: false,
        taskEditTitle: '',
        taskEditForm: {
          progressName: '',
          progressType: 2, // 默认为阶段进度
          managerId: null,
          plannedStartTime: '',
          plannedEndTime: '',
          actualStartTime: '',
          actualEndTime: '',
          actualProgress: 0,
          progressStatus: 1,
          progressDescription: '',
          remarks: ''
        },
        taskEditIndex: -1,
        taskEditRules: {
          milestoneName: [
            { required: true, message: '请输入里程碑名称', trigger: 'blur' }
          ],
          milestoneType: [
            { required: true, message: '请选择里程碑类型', trigger: 'change' }
          ],
          responsiblePersonId: [
            { required: true, message: '请选择负责人', trigger: 'change' }
          ],
          reporterId: [
            { required: true, message: '请选择汇报人', trigger: 'change' }
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
      async showEdit(data) {
        this.dialogVisible = true
        this.managementInfo = { ...data }
        await this.loadProgressData()
      },

      async loadProgressData() {
        try {
          const response = await getProgressManagementData({
            pageNum: 1,
            pageSize: 100,
            projectId: this.managementInfo.projectId
          })
          if (response.result === 200) {
            const data = response.data || {}
            // 将分页数据转换为列表格式
            this.taskList = data.records || []
            this.milestoneList = data.milestones || []
            this.progressSummary = data.progressSummary || this.progressSummary
          }
        } catch (error) {
          console.error('加载进度数据失败：', error)
          this.$message.error('加载进度数据失败：' + error.message)
        }
      },

      handleClose() {
        this.dialogVisible = false
        this.taskList = []
        this.milestoneList = []
        this.managementInfo = {}
        this.activeTab = 'plan'
      },

      handleAddTask() {
        this.taskEditTitle = '添加任务节点'
        this.taskEditIndex = -1
        this.resetTaskEditForm()
        this.taskEditDialogVisible = true
      },

      handleEditTask(row, index) {
        this.taskEditTitle = '编辑任务节点'
        this.taskEditIndex = index
        this.taskEditForm = { ...row }
        this.taskEditDialogVisible = true
      },

      handleDeleteTask(index) {
        this.$confirm('确定要删除这个任务节点吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.taskList.splice(index, 1)
          this.$message.success('删除成功')
        })
      },

      handleAddMilestone() {
        // 里程碑添加逻辑
        this.$message.info('里程碑添加功能待实现')
      },

      handleEditMilestone(row, index) {
        // 里程碑编辑逻辑
        this.$message.info('里程碑编辑功能待实现')
      },

      handleDeleteMilestone(index) {
        this.$confirm('确定要删除这个里程碑吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.milestoneList.splice(index, 1)
          this.$message.success('删除成功')
        })
      },

      resetTaskEditForm() {
        this.taskEditForm = {
          id: null,
          projectId: this.managementInfo.projectId,
          taskId: null,
          milestoneName: '',
          milestoneType: 2, // 默认为关键节点
          plannedStartDate: '',
          plannedEndDate: '',
          actualStartDate: '',
          actualEndDate: '',
          plannedProgress: 0,
          actualProgress: 0,
          progressStatus: 1, // 默认未开始
          delayDays: 0,
          delayReason: '',
          correctiveMeasures: '',
          responsiblePersonId: null,
          reporterId: null,
          reportDate: new Date().toISOString().split('T')[0], // 默认今天
          progressDescription: '',
          nextPlan: '',
          issuesRisks: '',
          supportNeeded: ''
        }
      },

      async handleSaveTaskEdit() {
        try {
          await this.$refs.taskEditForm.validate()

          // 准备API调用的数据
          const taskData = {
            ...this.taskEditForm,
            projectId: this.managementInfo.projectId, // 使用管理信息中的项目ID
            reporterId: this.taskEditForm.reporterId || 1001, // 确保有汇报人ID
            reportDate: this.taskEditForm.reportDate || new Date()
          }

          if (this.taskEditIndex === -1) {
            // 添加新任务 - 调用创建API
            const response = await createProgressRecord(taskData)
            if (response.result === 200) {
              // 添加到本地数据，使用返回的ID
              const newTask = { ...taskData, id: response.data.id || Date.now() }
              this.taskList.push(newTask)
              this.$message.success('任务创建成功')
            } else {
              this.$message.error('任务创建失败：' + response.msg)
              return
            }
          } else {
            // 编辑现有任务 - 调用更新API
            const existingTask = this.taskList[this.taskEditIndex]
            if (existingTask && existingTask.id) {
              const response = await updateProgressRecord(existingTask.id, taskData)
              if (response.result === 200) {
                // 更新本地数据
                this.taskList.splice(this.taskEditIndex, 1, { ...taskData, id: existingTask.id })
                this.$message.success('任务更新成功')
              } else {
                this.$message.error('任务更新失败：' + response.msg)
                return
              }
            } else {
              this.$message.error('无法找到要更新的任务')
              return
            }
          }

          this.taskEditDialogVisible = false
        } catch (error) {
          console.error('保存失败：', error)
          this.$message.error('保存失败：' + error.message)
        }
      },

      async handleSaveAll() {
        try {
          // 批量保存所有任务数据
          const savePromises = []

          // 遍历所有任务，分别调用创建或更新API
          for (const task of this.taskList) {
            const taskData = {
              ...task,
              projectId: this.managementInfo.projectId,
              reporterId: task.reporterId || 1001,
              reportDate: task.reportDate || new Date()
            }

            if (task.id && task.id !== 'temp_' + task.tempId) {
              // 已存在的任务，调用更新API
              savePromises.push(updateProgressRecord(task.id, taskData))
            } else {
              // 新任务，调用创建API
              savePromises.push(createProgressRecord(taskData))
            }
          }

          // 等待所有保存操作完成
          const results = await Promise.all(savePromises)

          // 检查保存结果
          const failedCount = results.filter(result => result.result !== 200).length
          if (failedCount === 0) {
            this.$message.success(`成功保存 ${results.length} 个任务`)
            // 重新加载数据
            await this.loadProgressData()
          } else {
            this.$message.warning(`保存完成，其中 ${failedCount} 个任务保存失败`)
          }

          this.handleClose()
        } catch (error) {
          console.error('批量保存失败：', error)
          this.$message.error('批量保存失败：' + error.message)
        }
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

      getMilestoneStatusName(status) {
        const statusMap = {
          1: '未开始',
          2: '进行中',
          3: '已完成',
          4: '已延期'
        }
        return statusMap[status] || '未知'
      },

      getMilestoneStatusType(status) {
        const typeMap = {
          1: 'info',
          2: 'primary',
          3: 'success',
          4: 'danger'
        }
        return typeMap[status] || 'info'
      },

      getProgressColor(percentage) {
        if (percentage <= 50) return '#f56c6c'
        if (percentage <= 80) return '#e6a23c'
        return '#67c23a'
      },

      getProgressStatusType(variance) {
        if (variance <= 0) return 'success'
        if (variance <= 7) return 'warning'
        return 'danger'
      },

      getProgressStatusText(variance) {
        if (variance <= 0) return '正常'
        if (variance <= 7) return '轻微延期'
        return '严重延期'
      },

      // 获取管理状态名称
      getManagementStatusName(status) {
        const statusMap = {
          1: '正常',
          2: '预警',
          3: '异常',
          4: '暂停'
        }
        return statusMap[status] || '未知'
      },

      // 获取管理状态样式
      getManagementStatusType(status) {
        const statusMap = {
          1: 'success',
          2: 'warning',
          3: 'danger',
          4: 'info'
        }
        return statusMap[status] || 'info'
      },

      // 获取里程碑类型名称
      getMilestoneTypeName(type) {
        const typeMap = {
          1: '项目开始',
          2: '关键节点',
          3: '项目完成'
        }
        return typeMap[type] || '未知'
      },

      // 获取里程碑类型标签样式
      getMilestoneTypeTag(type) {
        const tagMap = {
          1: 'success',
          2: 'primary',
          3: 'warning'
        }
        return tagMap[type] || 'info'
      }
    }
  }
</script>

<style scoped>
  .dialog-footer {
    text-align: right;
  }
  .progress-statistics .stat-item {
    text-align: center;
    padding: 20px 0;
  }
  .progress-statistics .stat-number {
    font-size: 24px;
    font-weight: bold;
  }
  .progress-statistics .completed {
    color: #67c23a;
  }
  .progress-statistics .in-progress {
    color: #409eff;
  }
  .progress-statistics .delayed {
    color: #f56c6c;
  }
  .progress-status {
    text-align: center;
    padding: 20px 0;
  }
</style>
