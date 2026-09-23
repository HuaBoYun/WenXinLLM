<template>
  <div class="coordination-management">
    <!-- 协同部门协调管理对话框 -->
    <el-dialog
      title="协同部门协调"
      :visible.sync="dialogVisible"
      width="90%"
      :close-on-click-modal="false"
      @close="handleDialogClose"
    >
      <div class="coordination-dialog-content">
        <!-- 操作按钮 -->
        <div class="operation-bar">
          <el-button type="primary" icon="el-icon-plus" @click="handleAdd">
            添加协调事项
          </el-button>
          <el-button
            type="success"
            icon="el-icon-message"
            @click="sendNotification"
            :disabled="selectedRows.length === 0"
          >
            发送通知
          </el-button>
          <el-button type="warning" icon="el-icon-date" @click="scheduleMeeting">
            安排会议
          </el-button>
          <el-button type="info" icon="el-icon-download" @click="exportData">
            导出数据
          </el-button>
        </div>

        <!-- 协调事项列表 -->
        <el-table
          v-loading="tableLoading"
          :data="coordinationList"
          @selection-change="handleSelectionChange"
          stripe
          border
          style="margin-top: 20px"
        >
          <el-table-column type="selection" width="55" align="center" />
          <el-table-column
            prop="coordinationTitle"
            label="协调事项"
            min-width="200"
            show-overflow-tooltip
          />
          <el-table-column
            prop="coordinationType"
            label="协调类型"
            width="120"
            align="center"
          >
            <template #default="{ row }">
              <el-tag :type="getTypeColor(row.coordinationType)">
                {{ getTypeName(row.coordinationType) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column
            prop="involvedDepartments"
            label="涉及部门"
            min-width="200"
            show-overflow-tooltip
          >
            <template #default="{ row }">
              <el-tag
                v-for="dept in row.involvedDepartments"
                :key="dept"
                size="mini"
                style="margin-right: 5px"
              >
                {{ dept }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column
            prop="coordinationStatus"
            label="协调状态"
            width="100"
            align="center"
          >
            <template #default="{ row }">
              <el-tag :type="getStatusColor(row.coordinationStatus)">
                {{ getStatusName(row.coordinationStatus) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column
            prop="priority"
            label="优先级"
            width="80"
            align="center"
          >
            <template #default="{ row }">
              <el-tag :type="getPriorityColor(row.priority)" size="mini">
                {{ getPriorityName(row.priority) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column
            prop="plannedDate"
            label="计划完成时间"
            width="130"
            align="center"
          >
            <template #default="{ row }">
              {{ formatDate(row.plannedDate) }}
            </template>
          </el-table-column>
          <el-table-column
            prop="responsiblePerson"
            label="负责人"
            width="100"
            align="center"
          />
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
                v-if="row.coordinationStatus === 1"
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

    <!-- 协调事项编辑对话框 -->
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
            <el-form-item label="协调事项" prop="coordinationTitle">
              <el-input
                v-model="editForm.coordinationTitle"
                placeholder="请输入协调事项标题"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="协调类型" prop="coordinationType">
              <el-select
                v-model="editForm.coordinationType"
                placeholder="请选择协调类型"
                style="width: 100%"
              >
                <el-option label="技术协调" :value="1" />
                <el-option label="资源协调" :value="2" />
                <el-option label="进度协调" :value="3" />
                <el-option label="质量协调" :value="4" />
                <el-option label="其他协调" :value="5" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="协调内容" prop="coordinationContent">
          <el-input
            v-model="editForm.coordinationContent"
            type="textarea"
            :rows="4"
            placeholder="请详细描述需要协调的具体内容"
          />
        </el-form-item>

        <el-form-item label="涉及部门" prop="involvedDepartments">
          <el-select
            v-model="editForm.involvedDepartments"
            placeholder="请选择涉及的部门"
            style="width: 100%"
            multiple
            filterable
          >
            <el-option label="技术部" value="技术部" />
            <el-option label="采购部" value="采购部" />
            <el-option label="财务部" value="财务部" />
            <el-option label="人事部" value="人事部" />
            <el-option label="质量部" value="质量部" />
            <el-option label="安全部" value="安全部" />
            <el-option label="项目部" value="项目部" />
            <el-option label="市场部" value="市场部" />
          </el-select>
        </el-form-item>

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
            <el-form-item label="计划完成时间" prop="plannedDate">
              <el-date-picker
                v-model="editForm.plannedDate"
                type="date"
                placeholder="选择计划完成时间"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="协调方式" prop="coordinationMethod">
              <el-select
                v-model="editForm.coordinationMethod"
                placeholder="请选择协调方式"
                style="width: 100%"
              >
                <el-option label="会议协调" value="会议协调" />
                <el-option label="邮件协调" value="邮件协调" />
                <el-option label="电话协调" value="电话协调" />
                <el-option label="现场协调" value="现场协调" />
                <el-option label="书面协调" value="书面协调" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="预期结果" prop="expectedResult">
          <el-input
            v-model="editForm.expectedResult"
            type="textarea"
            :rows="3"
            placeholder="请描述协调后的预期结果"
          />
        </el-form-item>

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

    <!-- 会议安排对话框 -->
    <el-dialog
      title="安排协调会议"
      :visible.sync="meetingDialogVisible"
      width="50%"
      :close-on-click-modal="false"
    >
      <el-form
        ref="meetingForm"
        :model="meetingForm"
        :rules="meetingRules"
        label-width="120px"
      >
        <el-form-item label="会议主题" prop="meetingTitle">
          <el-input
            v-model="meetingForm.meetingTitle"
            placeholder="请输入会议主题"
          />
        </el-form-item>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="会议时间" prop="meetingTime">
              <el-date-picker
                v-model="meetingForm.meetingTime"
                type="datetime"
                placeholder="选择会议时间"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="会议地点" prop="meetingLocation">
              <el-input
                v-model="meetingForm.meetingLocation"
                placeholder="请输入会议地点"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="参会人员" prop="attendees">
          <el-select
            v-model="meetingForm.attendees"
            placeholder="请选择参会人员"
            style="width: 100%"
            multiple
            filterable
          >
            <el-option label="张三" value="张三" />
            <el-option label="李四" value="李四" />
            <el-option label="王五" value="王五" />
            <el-option label="赵六" value="赵六" />
          </el-select>
        </el-form-item>

        <el-form-item label="会议议程" prop="meetingAgenda">
          <el-input
            v-model="meetingForm.meetingAgenda"
            type="textarea"
            :rows="4"
            placeholder="请输入会议议程"
          />
        </el-form-item>
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button @click="meetingDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleScheduleMeeting" :loading="meetingLoading">
          确定安排
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'CoordinationManagement',
  data() {
    return {
      dialogVisible: false,
      currentPlanningId: null,
      tableLoading: false,
      coordinationList: [],
      selectedRows: [],
      pagination: {
        current: 1,
        size: 20,
        total: 0
      },
      editDialogVisible: false,
      editDialogTitle: '添加协调事项',
      saveLoading: false,
      meetingDialogVisible: false,
      meetingLoading: false,
      editForm: {
        planningId: null,
        coordinationTitle: '',
        coordinationType: null,
        coordinationContent: '',
        involvedDepartments: [],
        responsiblePerson: '',
        priority: 2,
        plannedDate: null,
        coordinationMethod: '',
        expectedResult: '',
        remarks: ''
      },
      editRules: {
        coordinationTitle: [
          { required: true, message: '请输入协调事项标题', trigger: 'blur' }
        ],
        coordinationType: [
          { required: true, message: '请选择协调类型', trigger: 'change' }
        ],
        coordinationContent: [
          { required: true, message: '请输入协调内容', trigger: 'blur' }
        ],
        involvedDepartments: [
          { required: true, message: '请选择涉及部门', trigger: 'change' }
        ],
        responsiblePerson: [
          { required: true, message: '请选择负责人', trigger: 'change' }
        ],
        plannedDate: [
          { required: true, message: '请选择计划完成时间', trigger: 'change' }
        ]
      },
      meetingForm: {
        meetingTitle: '',
        meetingTime: null,
        meetingLocation: '',
        attendees: [],
        meetingAgenda: ''
      },
      meetingRules: {
        meetingTitle: [
          { required: true, message: '请输入会议主题', trigger: 'blur' }
        ],
        meetingTime: [
          { required: true, message: '请选择会议时间', trigger: 'change' }
        ],
        meetingLocation: [
          { required: true, message: '请输入会议地点', trigger: 'blur' }
        ],
        attendees: [
          { required: true, message: '请选择参会人员', trigger: 'change' }
        ]
      }
    }
  },
  methods: {
    // 显示协同部门协调管理对话框
    showEdit(row) {
      this.currentPlanningId = row.id
      this.dialogVisible = true
      this.fetchData()
    },

    // 关闭对话框
    handleDialogClose() {
      this.dialogVisible = false
      this.currentPlanningId = null
      this.coordinationList = []
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
            coordinationTitle: '技术方案协调',
            coordinationType: 1,
            involvedDepartments: ['技术部', '项目部'],
            coordinationStatus: 1,
            priority: 1,
            plannedDate: '2025-01-20',
            responsiblePerson: '张三'
          },
          {
            id: 2,
            coordinationTitle: '资源配置协调',
            coordinationType: 2,
            involvedDepartments: ['人事部', '采购部', '财务部'],
            coordinationStatus: 2,
            priority: 2,
            plannedDate: '2025-01-25',
            responsiblePerson: '李四'
          }
        ]
        
        this.coordinationList = mockData
        this.pagination.total = mockData.length
      } catch (error) {
        this.$message.error('获取数据失败：' + error.message)
      } finally {
        this.tableLoading = false
      }
    },

    handleAdd() {
      this.editDialogTitle = '添加协调事项'
      this.editForm = {
        planningId: this.currentPlanningId,
        coordinationTitle: '',
        coordinationType: null,
        coordinationContent: '',
        involvedDepartments: [],
        responsiblePerson: '',
        priority: 2,
        plannedDate: null,
        coordinationMethod: '',
        expectedResult: '',
        remarks: ''
      }
      this.editDialogVisible = true
    },

    handleEdit(row) {
      this.editDialogTitle = '编辑协调事项'
      this.editForm = { ...row }
      this.editDialogVisible = true
    },

    handleDetail(row) {
      this.editDialogTitle = '协调事项详情'
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
      this.$confirm('确定要删除这个协调事项吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$message.success('删除成功')
        this.fetchData()
      }).catch(() => {})
    },

    handleStart(row) {
      this.$confirm('确定要开始这个协调事项吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'info'
      }).then(() => {
        this.$message.success('协调事项已开始')
        this.fetchData()
      }).catch(() => {})
    },

    sendNotification() {
      if (this.selectedRows.length === 0) {
        this.$message.warning('请选择要发送通知的协调事项')
        return
      }
      this.$confirm(`确定要向相关部门发送${this.selectedRows.length}个协调事项的通知吗？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'info'
      }).then(() => {
        this.$message.success('通知发送成功')
      }).catch(() => {})
    },

    scheduleMeeting() {
      this.meetingForm = {
        meetingTitle: '',
        meetingTime: null,
        meetingLocation: '',
        attendees: [],
        meetingAgenda: ''
      }
      this.meetingDialogVisible = true
    },

    async handleScheduleMeeting() {
      try {
        await this.$refs.meetingForm.validate()
        
        this.meetingLoading = true
        // 这里应该调用API安排会议
        await new Promise(resolve => setTimeout(resolve, 1000))
        
        this.$message.success('会议安排成功')
        this.meetingDialogVisible = false
      } catch (error) {
        if (error !== 'validation failed') {
          this.$message.error('会议安排失败：' + error.message)
        }
      } finally {
        this.meetingLoading = false
      }
    },

    exportData() {
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

    getTypeColor(type) {
      const colorMap = {
        1: 'primary',
        2: 'success',
        3: 'warning',
        4: 'info',
        5: 'danger'
      }
      return colorMap[type] || 'info'
    },

    getTypeName(type) {
      const nameMap = {
        1: '技术协调',
        2: '资源协调',
        3: '进度协调',
        4: '质量协调',
        5: '其他协调'
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
        1: '待开始',
        2: '协调中',
        3: '已完成',
        4: '已取消'
      }
      return nameMap[status] || '未知'
    },

    getPriorityColor(priority) {
      const colorMap = {
        1: 'danger',
        2: 'warning',
        3: 'info'
      }
      return colorMap[priority] || 'info'
    },

    getPriorityName(priority) {
      const nameMap = {
        1: '高',
        2: '中',
        3: '低'
      }
      return nameMap[priority] || '未知'
    },

    formatDate(date) {
      if (!date) return ''
      return new Date(date).toLocaleDateString()
    }
  }
}
</script>

<style scoped>
.coordination-management {
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
</style>
