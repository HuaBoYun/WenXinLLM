<template>
  <div class="app-container">
    <el-card class="box-card">
      <!-- 查询表单 -->
      <el-form :inline="true" :model="queryForm" class="demo-form-inline">
        <el-form-item label="报表任务">
          <el-select v-model="queryForm.taskId" placeholder="请选择报表任务" clearable>
            <el-option
              v-for="item in taskOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="流程名称">
          <el-input v-model="queryForm.workflowName" placeholder="请输入流程名称" clearable />
        </el-form-item>
        <el-form-item label="流程类型">
          <el-select v-model="queryForm.workflowType" placeholder="请选择流程类型" clearable>
            <el-option label="编制" value="PREPARE" />
            <el-option label="审批" value="APPROVE" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="queryForm.status" placeholder="请选择状态" clearable>
            <el-option label="启用" value="ACTIVE" />
            <el-option label="停用" value="INACTIVE" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleQuery">查询</el-button>
          <el-button icon="el-icon-refresh" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>

      <!-- 操作按钮 -->
      <el-row :gutter="10" class="mb8">
        <el-col :span="1.5">
          <el-button
            type="primary"
            icon="el-icon-plus"
            size="mini"
            @click="handleAdd"
          >新增</el-button>
        </el-col>
      </el-row>

      <!-- 报表工作流表格 -->
      <el-table
        v-loading="loading"
        :data="reportWorkflowList"
        border
      >
        <el-table-column prop="workflowCode" label="流程编码" width="150" />
        <el-table-column prop="workflowName" label="流程名称" width="200" />
        <el-table-column prop="taskName" label="所属任务" width="150" />
        <el-table-column prop="workflowType" label="流程类型" width="100">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.workflowType === 'PREPARE'" type="primary">编制</el-tag>
            <el-tag v-else-if="scope.row.workflowType === 'APPROVE'" type="success">审批</el-tag>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column prop="messageType" label="消息提醒" width="100">
          <template slot-scope="scope">
            <span v-if="scope.row.messageType === 'IM'">即时消息</span>
            <span v-else-if="scope.row.messageType === 'EMAIL'">邮件</span>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column prop="sortNo" label="排序号" width="80" />
        <el-table-column prop="status" label="状态" width="100">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.status === 'ACTIVE'" type="success">启用</el-tag>
            <el-tag v-else-if="scope.row.status === 'INACTIVE'" type="info">停用</el-tag>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="160">
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.createTime) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" width="250" class-name="small-padding fixed-width">
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="text"
              icon="el-icon-edit"
              @click="handleUpdate(scope.row)"
            >修改</el-button>
            <el-button
              v-if="scope.row.status === 'ACTIVE'"
              size="mini"
              type="text"
              icon="el-icon-close"
              @click="handleDisable(scope.row)"
            >停用</el-button>
            <el-button
              v-if="scope.row.status === 'INACTIVE'"
              size="mini"
              type="text"
              icon="el-icon-check"
              @click="handleEnable(scope.row)"
            >启用</el-button>
            <el-button
              size="mini"
              type="text"
              icon="el-icon-delete"
              @click="handleDelete(scope.row)"
            >删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 添加或修改报表工作流对话框 -->
    <report-workflow-form
      ref="reportWorkflowForm"
      :visible.sync="dialogVisible"
      :form-data="formData"
      :task-options="taskOptions"
      @success="handleSuccess"
    />
  </div>
</template>

<script>
import { getReportWorkflowList, deleteReportWorkflow, updateReportWorkflowStatus } from '@/api/financialSharing/enterpriseReport/reportWorkflow'
import { getReportTaskList } from '@/api/financialSharing/enterpriseReport/reportTask'
import ReportWorkflowForm from './components/ReportWorkflowForm'

export default {
  name: 'ReportWorkflow',
  components: {
    ReportWorkflowForm
  },
  data() {
    return {
      // 遮罩层
      loading: true,
      // 查询参数
      queryForm: {
        taskId: '',
        workflowName: '',
        workflowType: '',
        status: ''
      },
      // 报表工作流列表
      reportWorkflowList: [],
      // 任务选项
      taskOptions: [],
      // 弹出层标题
      dialogVisible: false,
      // 表单参数
      formData: {}
    }
  },
  created() {
    this.getTaskOptions()
    this.getList()
  },
  methods: {
    /** 获取任务选项 */
    getTaskOptions() {
      getReportTaskList({ status: 'PUBLISHED' }).then(response => {
        if (response.code === 200) {
          this.taskOptions = (response.data || []).map(item => ({
            value: item.taskId,
            label: item.taskName
          }))
        }
      })
    },
    /** 查询报表工作流列表 */
    getList() {
      this.loading = true
      // 启用真实 API 调用，失败时显示空状态
      getReportWorkflowList(this.queryForm).then(response => {
        this.loading = false
        if (response.code === 1 || response.code === 200) {
          this.reportWorkflowList = response.data || []
        } else {
          this.$message.error(response.msg || '查询失败')
          this.reportWorkflowList = []
        }
      }).catch(err => {
        this.loading = false
        console.error('查询失败:', err)
        this.reportWorkflowList = []
      })
    },
    /** 查询按钮操作 */
    handleQuery() {
      this.getList()
    },
    /** 重置按钮操作 */
    handleReset() {
      this.queryForm = {
        taskId: '',
        workflowName: '',
        workflowType: '',
        status: ''
      }
      this.getList()
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.formData = {
        workflowId: '',
        workflowCode: '',
        workflowName: '',
        taskId: '',
        workflowType: 'PREPARE',
        branchCondition: '',
        participants: '',
        ccUsers: '',
        messageType: 'IM',
        messageContent: '',
        sortNo: 0,
        status: 'ACTIVE'
      }
      this.dialogVisible = true
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.formData = { ...row }
      this.dialogVisible = true
    },
    /** 停用按钮操作 */
    handleDisable(row) {
      this.$confirm('是否确认停用该工作流?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        return updateReportWorkflowStatus(row.workflowId, 'INACTIVE')
      }).then(response => {
        if (response.code === 200) {
          this.$message.success('停用成功')
          this.getList()
        } else {
          this.$message.error(response.msg || '停用失败')
        }
      }).catch(() => {})
    },
    /** 启用按钮操作 */
    handleEnable(row) {
      this.$confirm('是否确认启用该工作流?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        return updateReportWorkflowStatus(row.workflowId, 'ACTIVE')
      }).then(response => {
        if (response.code === 200) {
          this.$message.success('启用成功')
          this.getList()
        } else {
          this.$message.error(response.msg || '启用失败')
        }
      }).catch(() => {})
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      this.$confirm('是否确认删除该工作流?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        return deleteReportWorkflow(row.workflowId)
      }).then(response => {
        if (response.code === 200) {
          this.$message.success('删除成功')
          this.getList()
        } else {
          this.$message.error(response.msg || '删除失败')
        }
      }).catch(() => {})
    },
    /** 表单提交成功回调 */
    handleSuccess() {
      this.dialogVisible = false
      this.getList()
    },
    /** 时间格式化 */
    parseTime(time) {
      if (!time) return ''
      const date = new Date(time)
      const year = date.getFullYear()
      const month = String(date.getMonth() + 1).padStart(2, '0')
      const day = String(date.getDate()).padStart(2, '0')
      const hour = String(date.getHours()).padStart(2, '0')
      const minute = String(date.getMinutes()).padStart(2, '0')
      const second = String(date.getSeconds()).padStart(2, '0')
      return `${year}-${month}-${day} ${hour}:${minute}:${second}`
    }
  }
}
</script>

<style scoped>
.mb8 {
  margin-bottom: 8px;
}
</style>


