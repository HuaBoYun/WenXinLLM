<template>
  <div class="app-container">
    <el-card class="box-card">
      <!-- 查询表单 -->
      <el-form :inline="true" :model="queryForm" class="demo-form-inline">
        <el-form-item label="表单组">
          <el-select v-model="queryForm.groupId" placeholder="请选择表单组" clearable>
            <el-option
              v-for="item in groupOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="任务名称">
          <el-input v-model="queryForm.taskName" placeholder="请输入任务名称" clearable />
        </el-form-item>
        <el-form-item label="周期类型">
          <el-select v-model="queryForm.periodType" placeholder="请选择周期类型" clearable>
            <el-option label="年度" value="YEAR" />
            <el-option label="半年度" value="HALF_YEAR" />
            <el-option label="季度" value="QUARTER" />
            <el-option label="月度" value="MONTH" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="queryForm.status" placeholder="请选择状态" clearable>
            <el-option label="草稿" value="DRAFT" />
            <el-option label="已发布" value="PUBLISHED" />
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

      <!-- 报表任务表格 -->
      <el-table
        v-loading="loading"
        :data="reportTaskList"
        border
      >
        <el-table-column prop="taskCode" label="任务编码" width="150" />
        <el-table-column prop="taskName" label="任务名称" width="200" />
        <el-table-column prop="groupName" label="所属表单组" width="150" />
        <el-table-column prop="periodType" label="周期类型" width="100">
          <template slot-scope="scope">
            <span v-if="scope.row.periodType === 'YEAR'">年度</span>
            <span v-else-if="scope.row.periodType === 'HALF_YEAR'">半年度</span>
            <span v-else-if="scope.row.periodType === 'QUARTER'">季度</span>
            <span v-else-if="scope.row.periodType === 'MONTH'">月度</span>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column prop="startPeriod" label="起始期间" width="100" />
        <el-table-column prop="endPeriod" label="终止期间" width="100" />
        <el-table-column prop="isHierarchical" label="逐级上报" width="100">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.isHierarchical === 'Y'" type="success">是</el-tag>
            <el-tag v-else type="info">否</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="deadlineDays" label="截止天数" width="100" />
        <el-table-column prop="status" label="状态" width="100">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.status === 'DRAFT'" type="info">草稿</el-tag>
            <el-tag v-else-if="scope.row.status === 'PUBLISHED'" type="success">已发布</el-tag>
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
              v-if="scope.row.status === 'DRAFT'"
              size="mini"
              type="text"
              icon="el-icon-upload2"
              @click="handlePublish(scope.row)"
            >发布</el-button>
            <el-button
              v-if="scope.row.status === 'PUBLISHED'"
              size="mini"
              type="text"
              icon="el-icon-download"
              @click="handleWithdraw(scope.row)"
            >撤回</el-button>
            <el-button
              v-if="scope.row.status === 'DRAFT'"
              size="mini"
              type="text"
              icon="el-icon-delete"
              @click="handleDelete(scope.row)"
            >删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 添加或修改报表任务对话框 -->
    <report-task-form
      ref="reportTaskForm"
      :visible.sync="dialogVisible"
      :form-data="formData"
      :group-options="groupOptions"
      @success="handleSuccess"
    />
  </div>
</template>

<script>
import { getReportTaskList, deleteReportTask, publishReportTask, withdrawReportTask } from '@/api/financialSharing/enterpriseReport/reportTask'
import { getFormGroupList } from '@/api/financialSharing/enterpriseReport/formGroup'
import ReportTaskForm from './components/ReportTaskForm'

export default {
  name: 'ReportTask',
  components: {
    ReportTaskForm
  },
  data() {
    return {
      // 遮罩层
      loading: true,
      // 查询参数
      queryForm: {
        groupId: '',
        taskName: '',
        periodType: '',
        status: ''
      },
      // 报表任务列表
      reportTaskList: [],
      // 表单组选项
      groupOptions: [],
      // 弹出层标题
      dialogVisible: false,
      // 表单参数
      formData: {}
    }
  },
  created() {
    this.getGroupOptions()
    this.getList()
  },
  methods: {
    /** 获取表单组选项 */
    getGroupOptions() {
      getFormGroupList({ status: 'ACTIVE' }).then(response => {
        if (response.code === 200) {
          this.groupOptions = (response.data || []).map(item => ({
            value: item.groupId,
            label: item.groupName
          }))
        }
      })
    },
    /** 查询报表任务列表 */
    getList() {
      this.loading = true
      // 启用真实 API 调用，失败时显示空状态
      getReportTaskList(this.queryForm).then(response => {
        this.loading = false
        if (response.code === 1 || response.code === 200) {
          this.reportTaskList = response.data.list || response.data.records || response.data || []
        } else {
          this.$message.error(response.msg || '查询失败')
          this.reportTaskList = []
        }
      }).catch(err => {
        this.loading = false
        console.error('查询失败:', err)
        this.reportTaskList = []
      })
    },
    /** 查询按钮操作 */
    handleQuery() {
      this.getList()
    },
    /** 重置按钮操作 */
    handleReset() {
      this.queryForm = {
        groupId: '',
        taskName: '',
        periodType: '',
        status: ''
      }
      this.getList()
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.formData = {
        taskId: '',
        taskCode: '',
        taskName: '',
        groupId: '',
        orgSystem: '',
        periodType: 'MONTH',
        periodOffset: 0,
        startPeriod: '',
        endPeriod: '',
        isHierarchical: 'N',
        deadlineDays: null,
        isNodeCheck: 'N',
        isArchiveControl: 'N',
        status: 'DRAFT'
      }
      this.dialogVisible = true
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.formData = { ...row }
      this.dialogVisible = true
    },
    /** 发布按钮操作 */
    handlePublish(row) {
      this.$confirm('是否确认发布该报表任务?发布后将可以创建工作流实例。', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        return publishReportTask(row.taskId)
      }).then(response => {
        if (response.code === 200) {
          this.$message.success('发布成功')
          this.getList()
        } else {
          this.$message.error(response.msg || '发布失败')
        }
      }).catch(() => {})
    },
    /** 撤回按钮操作 */
    handleWithdraw(row) {
      this.$confirm('是否确认撤回该报表任务?撤回后将变为草稿状态。', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        return withdrawReportTask(row.taskId)
      }).then(response => {
        if (response.code === 200) {
          this.$message.success('撤回成功')
          this.getList()
        } else {
          this.$message.error(response.msg || '撤回失败')
        }
      }).catch(() => {})
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      this.$confirm('是否确认删除该报表任务?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        return deleteReportTask(row.taskId)
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


