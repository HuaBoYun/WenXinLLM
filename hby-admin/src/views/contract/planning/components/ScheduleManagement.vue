<template>
  <div class="schedule-management">
    <!-- 时间进度管理对话框 -->
    <el-dialog
      title="时间进度安排"
      :visible.sync="dialogVisible"
      width="90%"
      :close-on-click-modal="false"
      @close="handleDialogClose"
    >
      <div class="schedule-dialog-content">
        <!-- 操作按钮 -->
        <div class="operation-bar">
          <el-button type="primary" icon="el-icon-plus" @click="handleAdd">
            添加进度节点
          </el-button>
          <el-button
            type="success"
            icon="el-icon-check"
            @click="handleBatchApprove"
            :disabled="selectedRows.length === 0"
          >
            批量确认
          </el-button>
          <el-button type="info" icon="el-icon-view" @click="showGanttChart">
            甘特图
          </el-button>
          <el-button type="warning" icon="el-icon-download" @click="exportSchedule">
            导出进度表
          </el-button>
        </div>

        <!-- 进度节点列表 -->
        <el-table
          v-loading="tableLoading"
          :data="scheduleList"
          @selection-change="handleSelectionChange"
          stripe
          border
          style="margin-top: 20px"
        >
          <el-table-column type="selection" width="55" align="center" />
          <el-table-column
            prop="taskName"
            label="任务名称"
            min-width="200"
            show-overflow-tooltip
          />
          <el-table-column
            prop="taskType"
            label="任务类型"
            width="120"
            align="center"
          >
            <template #default="{ row }">
              <el-tag :type="getTaskTypeColor(row.taskType)">
                {{ getTaskTypeName(row.taskType) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column
            prop="plannedStartDate"
            label="计划开始"
            width="120"
            align="center"
          >
            <template #default="{ row }">
              {{ formatDate(row.plannedStartDate) }}
            </template>
          </el-table-column>
          <el-table-column
            prop="plannedEndDate"
            label="计划结束"
            width="120"
            align="center"
          >
            <template #default="{ row }">
              {{ formatDate(row.plannedEndDate) }}
            </template>
          </el-table-column>
          <el-table-column
            prop="actualStartDate"
            label="实际开始"
            width="120"
            align="center"
          >
            <template #default="{ row }">
              {{ formatDate(row.actualStartDate) }}
            </template>
          </el-table-column>
          <el-table-column
            prop="actualEndDate"
            label="实际结束"
            width="120"
            align="center"
          >
            <template #default="{ row }">
              {{ formatDate(row.actualEndDate) }}
            </template>
          </el-table-column>
          <el-table-column
            prop="progress"
            label="完成进度"
            width="120"
            align="center"
          >
            <template #default="{ row }">
              <el-progress
                :percentage="row.progress || 0"
                :color="getProgressColor(row.progress)"
                :stroke-width="8"
              />
            </template>
          </el-table-column>
          <el-table-column
            prop="responsiblePerson"
            label="负责人"
            width="100"
            align="center"
          />
          <el-table-column
            prop="taskStatus"
            label="状态"
            width="100"
            align="center"
          >
            <template #default="{ row }">
              <el-tag :type="getStatusColor(row.taskStatus)">
                {{ getStatusName(row.taskStatus) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="200" align="center" fixed="right">
            <template #default="{ row }">
              <el-button
                type="text"
                size="small"
                icon="el-icon-view"
                @click="handleDetail(row)"
              >
                详情
              </el-button>
              <el-button
                type="text"
                size="small"
                icon="el-icon-edit"
                @click="handleEdit(row)"
              >
                编辑
              </el-button>
              <el-button
                v-if="row.taskStatus === 1"
                type="text"
                size="small"
                icon="el-icon-video-play"
                @click="handleStart(row)"
              >
                开始
              </el-button>
              <el-button
                type="text"
                size="small"
                icon="el-icon-delete"
                style="color: #f56c6c"
                @click="handleDelete(row)"
              >
                删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>

        <!-- 分页 -->
        <div class="pagination-container">
          <el-pagination
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            :current-page="pagination.current"
            :page-sizes="[10, 20, 50, 100]"
            :page-size="pagination.size"
            layout="total, sizes, prev, pager, next, jumper"
            :total="pagination.total"
          />
        </div>
      </div>
    </el-dialog>

    <!-- 进度节点编辑对话框 -->
    <el-dialog
      :title="editDialogTitle"
      :visible.sync="editDialogVisible"
      width="60%"
      :close-on-click-modal="false"
    >
      <el-form
        ref="editForm"
        :model="editForm"
        :rules="editRules"
        label-width="120px"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="任务名称" prop="taskName">
              <el-input
                v-model="editForm.taskName"
                placeholder="请输入任务名称"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="任务类型" prop="taskType">
              <el-select
                v-model="editForm.taskType"
                placeholder="请选择任务类型"
                style="width: 100%"
              >
                <el-option label="设计阶段" :value="1" />
                <el-option label="采购阶段" :value="2" />
                <el-option label="施工阶段" :value="3" />
                <el-option label="验收阶段" :value="4" />
                <el-option label="其他" :value="5" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="任务描述" prop="taskDescription">
          <el-input
            v-model="editForm.taskDescription"
            type="textarea"
            :rows="3"
            placeholder="请输入任务描述"
          />
        </el-form-item>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="计划开始时间" prop="plannedStartDate">
              <el-date-picker
                v-model="editForm.plannedStartDate"
                type="date"
                placeholder="选择计划开始时间"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="计划结束时间" prop="plannedEndDate">
              <el-date-picker
                v-model="editForm.plannedEndDate"
                type="date"
                placeholder="选择计划结束时间"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="负责人" prop="responsiblePerson">
              <el-select
                v-model="editForm.responsiblePerson"
                placeholder="请选择负责人"
                style="width: 100%"
                filterable
              >
                <el-option label="张三" value="张三" />
                <el-option label="李四" value="李四" />
                <el-option label="王五" value="王五" />
                <el-option label="赵六" value="赵六" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="优先级" prop="priority">
              <el-select
                v-model="editForm.priority"
                placeholder="请选择优先级"
                style="width: 100%"
              >
                <el-option label="高" :value="1" />
                <el-option label="中" :value="2" />
                <el-option label="低" :value="3" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="预计工期(天)" prop="estimatedDuration">
              <el-input-number
                v-model="editForm.estimatedDuration"
                :min="1"
                controls-position="right"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="前置任务" prop="predecessorTasks">
              <el-select
                v-model="editForm.predecessorTasks"
                placeholder="请选择前置任务"
                style="width: 100%"
                multiple
                filterable
              >
                <el-option
                  v-for="task in availableTasks"
                  :key="task.id"
                  :label="task.taskName"
                  :value="task.id"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="备注" prop="remarks">
          <el-input
            v-model="editForm.remarks"
            type="textarea"
            :rows="2"
            placeholder="请输入备注信息"
          />
        </el-form-item>
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button @click="editDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSave" :loading="saveLoading">
          保存
        </el-button>
      </div>
    </el-dialog>

    <!-- 甘特图对话框 -->
    <el-dialog
      title="项目进度甘特图"
      :visible.sync="ganttDialogVisible"
      width="95%"
      :close-on-click-modal="false"
    >
      <div class="gantt-container">
        <div id="gantt-chart" style="height: 500px; width: 100%"></div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'ScheduleManagement',
  data() {
    return {
      dialogVisible: false,
      currentPlanningId: null,
      tableLoading: false,
      scheduleList: [],
      selectedRows: [],
      availableTasks: [],
      pagination: {
        current: 1,
        size: 20,
        total: 0
      },
      editDialogVisible: false,
      editDialogTitle: '添加进度节点',
      saveLoading: false,
      ganttDialogVisible: false,
      editForm: {
        planningId: null,
        taskName: '',
        taskType: null,
        taskDescription: '',
        plannedStartDate: null,
        plannedEndDate: null,
        responsiblePerson: '',
        priority: 2,
        estimatedDuration: 1,
        predecessorTasks: [],
        remarks: ''
      },
      editRules: {
        taskName: [
          { required: true, message: '请输入任务名称', trigger: 'blur' }
        ],
        taskType: [
          { required: true, message: '请选择任务类型', trigger: 'change' }
        ],
        plannedStartDate: [
          { required: true, message: '请选择计划开始时间', trigger: 'change' }
        ],
        plannedEndDate: [
          { required: true, message: '请选择计划结束时间', trigger: 'change' }
        ],
        responsiblePerson: [
          { required: true, message: '请选择负责人', trigger: 'change' }
        ]
      }
    }
  },
  methods: {
    // 显示时间进度管理对话框
    showEdit(row) {
      this.currentPlanningId = row.id
      this.dialogVisible = true
      this.fetchData()
    },

    // 关闭对话框
    handleDialogClose() {
      this.dialogVisible = false
      this.currentPlanningId = null
      this.scheduleList = []
      this.selectedRows = []
    },

    async fetchData() {
      if (!this.currentPlanningId) return
      
      this.tableLoading = true
      try {
        // 模拟数据，实际应该调用API
        const mockData = [
          {
            id: 1,
            taskName: '项目启动',
            taskType: 1,
            plannedStartDate: '2025-01-10',
            plannedEndDate: '2025-01-15',
            actualStartDate: '2025-01-10',
            actualEndDate: null,
            progress: 80,
            responsiblePerson: '张三',
            taskStatus: 2
          },
          {
            id: 2,
            taskName: '需求分析',
            taskType: 1,
            plannedStartDate: '2025-01-16',
            plannedEndDate: '2025-01-25',
            actualStartDate: null,
            actualEndDate: null,
            progress: 0,
            responsiblePerson: '李四',
            taskStatus: 1
          }
        ]
        
        this.scheduleList = mockData
        this.pagination.total = mockData.length
        this.availableTasks = mockData.map(item => ({
          id: item.id,
          taskName: item.taskName
        }))
      } catch (error) {
        this.$message.error('获取数据失败：' + error.message)
      } finally {
        this.tableLoading = false
      }
    },

    handleAdd() {
      this.editDialogTitle = '添加进度节点'
      this.editForm = {
        planningId: this.currentPlanningId,
        taskName: '',
        taskType: null,
        taskDescription: '',
        plannedStartDate: null,
        plannedEndDate: null,
        responsiblePerson: '',
        priority: 2,
        estimatedDuration: 1,
        predecessorTasks: [],
        remarks: ''
      }
      this.editDialogVisible = true
    },

    handleEdit(row) {
      this.editDialogTitle = '编辑进度节点'
      this.editForm = { ...row }
      this.editDialogVisible = true
    },

    handleDetail(row) {
      this.editDialogTitle = '进度节点详情'
      this.editForm = { ...row }
      this.editDialogVisible = true
    },

    async handleSave() {
      try {
        await this.$refs.editForm.validate()
        
        this.saveLoading = true
        // 这里应该调用API保存数据
        await new Promise(resolve => setTimeout(resolve, 1000))
        
        this.$message.success('保存成功')
        this.editDialogVisible = false
        this.fetchData()
      } catch (error) {
        if (error !== 'validation failed') {
          this.$message.error('保存失败：' + error.message)
        }
      } finally {
        this.saveLoading = false
      }
    },

    handleDelete(row) {
      this.$confirm('确定要删除这个进度节点吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$message.success('删除成功')
        this.fetchData()
      }).catch(() => {})
    },

    handleStart(row) {
      this.$confirm('确定要开始这个任务吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'info'
      }).then(() => {
        this.$message.success('任务已开始')
        this.fetchData()
      }).catch(() => {})
    },

    handleBatchApprove() {
      if (this.selectedRows.length === 0) {
        this.$message.warning('请选择要确认的进度节点')
        return
      }
      this.$confirm(`确定要批量确认选中的${this.selectedRows.length}个进度节点吗？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'info'
      }).then(() => {
        this.$message.success('批量确认成功')
        this.fetchData()
      }).catch(() => {})
    },

    showGanttChart() {
      this.ganttDialogVisible = true
      this.$nextTick(() => {
        this.renderGanttChart()
      })
    },

    renderGanttChart() {
      // 这里可以集成甘特图库，如 dhtmlx-gantt 或 frappe-gantt
      const container = document.getElementById('gantt-chart')
      if (container) {
        container.innerHTML = '<div style="text-align: center; line-height: 500px; color: #999;">甘特图功能开发中...</div>'
      }
    },

    exportSchedule() {
      this.$message.info('导出功能开发中...')
    },

    handleSelectionChange(selection) {
      this.selectedRows = selection
    },

    handleSizeChange(val) {
      this.pagination.size = val
      this.pagination.current = 1
      this.fetchData()
    },

    handleCurrentChange(val) {
      this.pagination.current = val
      this.fetchData()
    },

    getTaskTypeColor(type) {
      const colorMap = {
        1: 'primary',
        2: 'success',
        3: 'warning',
        4: 'info',
        5: 'danger'
      }
      return colorMap[type] || 'info'
    },

    getTaskTypeName(type) {
      const nameMap = {
        1: '设计阶段',
        2: '采购阶段',
        3: '施工阶段',
        4: '验收阶段',
        5: '其他'
      }
      return nameMap[type] || '未知'
    },

    getStatusColor(status) {
      const colorMap = {
        1: 'info',
        2: 'warning',
        3: 'success',
        4: 'danger'
      }
      return colorMap[status] || 'info'
    },

    getStatusName(status) {
      const nameMap = {
        1: '未开始',
        2: '进行中',
        3: '已完成',
        4: '已暂停'
      }
      return nameMap[status] || '未知'
    },

    getProgressColor(progress) {
      if (progress >= 80) return '#67c23a'
      if (progress >= 50) return '#e6a23c'
      return '#f56c6c'
    },

    formatDate(date) {
      if (!date) return ''
      return new Date(date).toLocaleDateString()
    }
  }
}
</script>

<style scoped>
.schedule-management {
  padding: 20px;
}

.operation-bar {
  margin-bottom: 20px;
}

.pagination-container {
  margin-top: 20px;
  text-align: right;
}

.dialog-footer {
  text-align: right;
}

.gantt-container {
  width: 100%;
  height: 500px;
}
</style>
