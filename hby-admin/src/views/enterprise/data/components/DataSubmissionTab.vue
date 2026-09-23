<template>
  <div class="data-submission-tab">
    <!-- 查询表单 -->
    <el-form :model="queryForm" ref="queryForm" :inline="true" label-width="100px" class="mb-20">
      <el-form-item label="报送类型">
        <el-select v-model="queryForm.submissionType" placeholder="请选择报送类型" clearable style="width: 150px;">
          <el-option label="财务报表" value="FINANCIAL"></el-option>
          <el-option label="经营数据" value="BUSINESS"></el-option>
          <el-option label="风险数据" value="RISK"></el-option>
          <el-option label="合规数据" value="COMPLIANCE"></el-option>
          <el-option label="其他数据" value="OTHER"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="报送状态">
        <el-select v-model="queryForm.submissionStatus" placeholder="请选择报送状态" clearable style="width: 150px;">
          <el-option label="待报送" value="PENDING"></el-option>
          <el-option label="报送中" value="SUBMITTING"></el-option>
          <el-option label="已报送" value="SUBMITTED"></el-option>
          <el-option label="已接收" value="RECEIVED"></el-option>
          <el-option label="已拒绝" value="REJECTED"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="优先级">
        <el-select v-model="queryForm.priority" placeholder="请选择优先级" clearable style="width: 120px;">
          <el-option label="高" value="HIGH"></el-option>
          <el-option label="中" value="MEDIUM"></el-option>
          <el-option label="低" value="LOW"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="报送时间">
        <el-date-picker
          v-model="queryForm.submissionTimeRange"
          type="daterange"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          style="width: 240px;">
        </el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleQuery" icon="el-icon-search">查询</el-button>
        <el-button @click="resetQuery" icon="el-icon-refresh">重置</el-button>
        <el-button type="success" @click="handleCreateSubmission" icon="el-icon-plus">创建报送</el-button>
        <el-button type="warning" @click="handleBatchSubmit" icon="el-icon-upload2">批量报送</el-button>
      </el-form-item>
    </el-form>

    <!-- 报送概览统计 -->
    <el-row :gutter="20" class="mb-20">
      <el-col :span="6">
        <el-card class="submission-card">
          <div class="submission-content">
            <div class="submission-icon total">
              <i class="el-icon-document"></i>
            </div>
            <div class="submission-info">
              <div class="submission-number">{{ submissionStats.totalTasks || 0 }}</div>
              <div class="submission-label">报送任务总数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="submission-card">
          <div class="submission-content">
            <div class="submission-icon completed">
              <i class="el-icon-circle-check"></i>
            </div>
            <div class="submission-info">
              <div class="submission-number">{{ submissionStats.completedTasks || 0 }}</div>
              <div class="submission-label">已完成报送</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="submission-card">
          <div class="submission-content">
            <div class="submission-icon pending">
              <i class="el-icon-time"></i>
            </div>
            <div class="submission-info">
              <div class="submission-number">{{ submissionStats.pendingTasks || 0 }}</div>
              <div class="submission-label">待报送任务</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="submission-card">
          <div class="submission-content">
            <div class="submission-icon rate">
              <i class="el-icon-data-line"></i>
            </div>
            <div class="submission-info">
              <div class="submission-number">{{ submissionStats.completionRate || 0 }}%</div>
              <div class="submission-label">完成率</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 数据表格 -->
    <el-table
      v-loading="loading"
      :data="tableData"
      @selection-change="handleSelectionChange"
      @sort-change="handleSortChange"
      stripe
      border
      style="width: 100%"
    >
      <el-table-column type="selection" width="55" align="center"></el-table-column>
      <el-table-column prop="enterpriseName" label="企业名称" width="160" show-overflow-tooltip></el-table-column>
      <el-table-column prop="dataType" label="报送类型" width="120" align="center">
        <template slot-scope="scope">
          <el-tag :type="getSubmissionTypeTag(scope.row.dataType)">
            {{ scope.row.dataType }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="dataCategory" label="报送内容" width="120" align="center"></el-table-column>
      <el-table-column prop="reportPeriod" label="报告期间" width="120" align="center"></el-table-column>
      <el-table-column prop="qualityScore" label="质量评分" width="100" align="center">
        <template slot-scope="scope">
          <span v-if="scope.row.qualityScore">{{ scope.row.qualityScore }}分</span>
          <span v-else style="color:#909399">-</span>
        </template>
      </el-table-column>
      <el-table-column prop="submitter" label="报送人" width="100" align="center"></el-table-column>
      <el-table-column prop="submitTime" label="报送时间" width="160" align="center"></el-table-column>
      <el-table-column prop="status" label="报送状态" width="100" align="center">
        <template slot-scope="scope">
          <el-tag :type="getSubmissionStatusTag(scope.row.status)">
            {{ scope.row.status }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="380" align="center" fixed="right">
        <template slot-scope="scope">
          <el-button size="mini" @click="handleView(scope.row)" icon="el-icon-view">查看</el-button>
          <el-button size="mini" type="primary" @click="handleEdit(scope.row)" icon="el-icon-edit" v-if="scope.row.status === '草稿'">编辑</el-button>
          <el-button size="mini" type="success" @click="handleSubmit(scope.row)" icon="el-icon-upload2" v-if="scope.row.status === '草稿'">报送</el-button>
          <el-button size="mini" type="danger" @click="handleDelete(scope.row)" icon="el-icon-delete" v-if="scope.row.submissionStatus === 'PENDING' || scope.row.status === '草稿'">删除</el-button>
          <el-dropdown @command="handleCommand" style="margin-left: 10px;">
            <el-button size="mini" type="info">
              更多<i class="el-icon-arrow-down el-icon--right"></i>
            </el-button>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item :command="{action: 'progress', row: scope.row}" icon="el-icon-data-line">查看进度</el-dropdown-item>
              <el-dropdown-item :command="{action: 'history', row: scope.row}" icon="el-icon-time">报送历史</el-dropdown-item>
              <el-dropdown-item :command="{action: 'withdraw', row: scope.row}" icon="el-icon-back" v-if="scope.row.submissionStatus === 'SUBMITTED'">撤回报送</el-dropdown-item>
              <el-dropdown-item :command="{action: 'copy', row: scope.row}" icon="el-icon-document-copy">复制任务</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页组件 -->
    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryForm.pageNumber"
      :limit.sync="queryForm.pageSize"
      @pagination="getList"
    />

    <!-- 创建报送任务对话框 -->
    <SubmissionTaskDialog
      :visible.sync="taskDialogVisible"
      :form-data="currentRow"
      :dialog-type="dialogType"
      :enterprise-id="enterpriseId"
      @refresh="getList"
    />

    <!-- 报送详情对话框 -->
    <SubmissionDetailDialog
      :visible.sync="detailDialogVisible"
      :submission-data="currentRow"
    />

    <!-- 报送进度对话框 -->
    <SubmissionProgressDialog
      :visible.sync="progressDialogVisible"
      :submission-data="currentRow"
    />

    <!-- 批量报送对话框 -->
    <BatchSubmissionDialog
      :visible.sync="batchDialogVisible"
      :selected-tasks="multipleSelection"
      @refresh="getList"
    />
  </div>
</template>

<script>
import {
  getDataSubmissionList,
  deleteDataSubmission,
  submitDataSubmission,
  withdrawDataSubmission,
  getSubmissionProgress
} from '@/api/enterprise/data'
import Pagination from '@/components/Pagination'
import SubmissionTaskDialog from './SubmissionTaskDialog'
import SubmissionDetailDialog from './SubmissionDetailDialog'
import SubmissionProgressDialog from './SubmissionProgressDialog'
import BatchSubmissionDialog from './BatchSubmissionDialog'

export default {
  name: 'DataSubmissionTab',
  components: {
    Pagination,
    SubmissionTaskDialog,
    SubmissionDetailDialog,
    SubmissionProgressDialog,
    BatchSubmissionDialog
  },
  props: {
    enterpriseId: {
      type: String,
      default: ''
    },
    enterpriseName: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      loading: false,
      tableData: [],
      total: 0,
      multipleSelection: [],
      submissionStats: {},
      queryForm: {
        pageNumber: 1,
        pageSize: 10,
        submissionType: '',
        submissionStatus: '',
        priority: '',
        submissionTimeRange: null
      },
      
      // 对话框状态
      taskDialogVisible: false,
      detailDialogVisible: false,
      progressDialogVisible: false,
      batchDialogVisible: false,
      dialogType: 'add',
      currentRow: {}
    }
  },
  watch: {
    enterpriseId: {
      handler() {
        this.getList()
      },
      immediate: true
    }
  },
  methods: {
    // 获取列表数据（报送Tab显示所有数据）
    getList() {
      this.loading = true
      const params = {
        ...this.queryForm
      }
      // 如果选择了报送状态筛选，映射到status字段
      if (this.queryForm.submissionStatus) {
        const statusMap = { 'PENDING': '草稿', 'SUBMITTING': '报送中', 'SUBMITTED': '已提交', 'RECEIVED': '已审核', 'REJECTED': '已退回' }
        params.status = statusMap[this.queryForm.submissionStatus] || this.queryForm.submissionStatus
      }
      // 报送时间范围
      if (this.queryForm.submissionTimeRange && this.queryForm.submissionTimeRange.length === 2) {
        const fmt = (d) => { const dt = new Date(d); return dt.getFullYear() + '-' + String(dt.getMonth() + 1).padStart(2, '0') + '-' + String(dt.getDate()).padStart(2, '0') }
        params.entryDateStart = fmt(this.queryForm.submissionTimeRange[0])
        params.entryDateEnd = fmt(this.queryForm.submissionTimeRange[1])
      }
      delete params.submissionTimeRange
      // 报送类型映射到dataType
      if (this.queryForm.submissionType) {
        params.dataType = this.queryForm.submissionType
      }

      getDataSubmissionList(params).then(response => {
        const data = response.data || {}
        this.tableData = data.tlist || data.records || []
        this.total = data.totalRecord || data.total || 0
        this.loading = false
        this.loadSubmissionStats()
      }).catch(() => {
        this.tableData = []
        this.total = 0
        this.loading = false
      })
    },

    // 加载报送统计（基于列表数据计算）
    loadSubmissionStats() {
      const list = this.tableData
      this.submissionStats = {
        totalTasks: this.total || list.length,
        completedTasks: list.filter(i => i.status === '已提交' || i.status === '已审核').length,
        pendingTasks: list.filter(i => i.status === '草稿').length,
        completionRate: this.total > 0 ? Math.round(list.filter(i => i.status !== '草稿').length / (this.total || 1) * 100 * 10) / 10 : 0
      }
    },

    // 查询
    handleQuery() {
      this.queryForm.pageNumber = 1
      this.getList()
    },

    // 重置查询
    resetQuery() {
      this.$refs.queryForm.resetFields()
      this.queryForm = {
        pageNumber: 1,
        pageSize: 10,
        submissionType: '',
        submissionStatus: '',
        priority: '',
        submissionTimeRange: null
      }
      this.getList()
    },

    // 创建报送任务
    handleCreateSubmission() {
      this.currentRow = {}
      this.dialogType = 'add'
      this.taskDialogVisible = true
    },

    // 批量报送
    handleBatchSubmit() {
      if (this.multipleSelection.length === 0) {
        this.$message.warning('请选择要报送的任务')
        return
      }
      this.batchDialogVisible = true
    },

    // 查看
    handleView(row) {
      this.currentRow = { ...row }
      this.detailDialogVisible = true
    },

    // 编辑
    handleEdit(row) {
      this.currentRow = { ...row }
      this.dialogType = 'edit'
      this.taskDialogVisible = true
    },

    // 报送
    async handleSubmit(row) {
      this.$confirm('确认提交该报送任务？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const response = await submitDataSubmission({
            id: row.id || row.submissionId
          })
          if (response.result == 200) {
            this.$message.success('报送成功')
            this.getList()
            this.$emit('refresh')
          } else {
            this.$message.error(response.msg || '报送失败')
          }
        } catch (error) {
          this.$message.error('报送失败')
        }
      })
    },

    // 下拉菜单命令处理
    handleCommand(command) {
      const { action, row } = command
      switch (action) {
        case 'progress':
          this.currentRow = { ...row }
          this.progressDialogVisible = true
          break
        case 'history':
          this.handleViewHistory(row)
          break
        case 'withdraw':
          this.handleWithdraw(row)
          break
        case 'copy':
          this.handleCopy(row)
          break
        case 'delete':
          this.handleDelete(row)
          break
      }
    },

    // 查看报送历史
    async handleViewHistory(row) {
      try {
        const { getDataEntryHistory } = require('@/api/enterprise/data')
        const response = await getDataEntryHistory({
          enterpriseId: row.enterpriseId,
          pageNumber: 1,
          pageSize: 20
        })
        if (response.result == 200 && response.data) {
          const list = response.data.tlist || []
          if (list.length > 0) {
            const historyHtml = list.slice(0, 10).map(item =>
              `<tr><td>${item.submitTime || item.updateTime || '-'}</td><td>${item.submitter || '-'}</td><td>${item.status || '-'}</td><td>${item.remark || '-'}</td></tr>`
            ).join('')
            this.$alert(`<table border="1" cellpadding="8" cellspacing="0" style="width:100%;border-collapse:collapse;">
              <thead><tr><th>报送时间</th><th>报送人</th><th>状态</th><th>备注</th></tr></thead>
              <tbody>${historyHtml}</tbody>
            </table>`, '报送历史记录', {
              dangerouslyUseHTMLString: true,
              confirmButtonText: '确定'
            })
          } else {
            this.$message.info('暂无报送历史记录')
          }
        }
      } catch (error) {
        this.$message.error('获取报送历史失败')
      }
    },

    // 撤回报送
    async handleWithdraw(row) {
      this.$confirm('确认撤回该报送任务？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const response = await withdrawDataSubmission({
            id: row.id || row.submissionId
          })
          if (response.result == 200) {
            this.$message.success('撤回成功')
            this.getList()
            this.$emit('refresh')
          } else {
            this.$message.error(response.msg || '撤回失败')
          }
        } catch (error) {
          this.$message.error('撤回失败')
        }
      })
    },

    // 复制任务
    handleCopy(row) {
      this.currentRow = { ...row, submissionId: '', submissionStatus: 'PENDING' }
      this.dialogType = 'add'
      this.taskDialogVisible = true
    },

    // 删除
    handleDelete(row) {
      this.$confirm(`确认删除该报送任务？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'info'
      }).then(async () => {
        let loading = null
        try {
          loading = this.$loading({
            lock: true,
            text: '正在删除...',
            spinner: 'el-icon-loading',
            background: 'rgba(0, 0, 0, 0.7)'
          })

          const response = await deleteDataSubmission(row.id || row.submissionId)
          loading.close()

          if (response && response.result != 500) {
            this.$message.success('删除成功')
            this.getList()
            this.$emit('refresh')
          } else {
            this.$message.error(response.msg || '删除失败')
          }
        } catch (error) {
          if (loading) loading.close()
          this.$message.error('删除失败：' + (error.message || '未知错误'))
        }
      }).catch(() => {})
    },

    // 多选变化
    handleSelectionChange(selection) {
      this.multipleSelection = selection
    },

    // 排序变化
    handleSortChange({ column, prop, order }) {
      this.queryForm.orderBy = prop
      this.queryForm.orderDirection = order === 'ascending' ? 'ASC' : 'DESC'
      this.getList()
    },

    // 获取报送类型标签
    getSubmissionTypeTag(type) {
      const tagMap = {
        'FINANCIAL': 'primary',
        'BUSINESS': 'success',
        'RISK': 'danger',
        'COMPLIANCE': 'warning',
        'OTHER': 'info'
      }
      return tagMap[type] || 'info'
    },

    // 获取报送类型文本
    getSubmissionTypeText(type) {
      const textMap = {
        'FINANCIAL': '财务报表',
        'BUSINESS': '经营数据',
        'RISK': '风险数据',
        'COMPLIANCE': '合规数据',
        'OTHER': '其他数据'
      }
      return textMap[type] || type
    },

    // 获取优先级标签
    getPriorityTag(priority) {
      const tagMap = {
        'HIGH': 'danger',
        'MEDIUM': 'warning',
        'LOW': 'info'
      }
      return tagMap[priority] || 'info'
    },

    // 获取优先级文本
    getPriorityText(priority) {
      const textMap = {
        'HIGH': '高',
        'MEDIUM': '中',
        'LOW': '低'
      }
      return textMap[priority] || priority
    },

    // 获取报送状态标签
    getSubmissionStatusTag(status) {
      const tagMap = {
        'PENDING': 'info',
        'SUBMITTING': 'warning',
        'SUBMITTED': 'primary',
        'RECEIVED': 'success',
        'REJECTED': 'danger'
      }
      return tagMap[status] || 'info'
    },

    // 获取报送状态文本
    getSubmissionStatusText(status) {
      const textMap = {
        'PENDING': '待报送',
        'SUBMITTING': '报送中',
        'SUBMITTED': '已报送',
        'RECEIVED': '已接收',
        'REJECTED': '已拒绝'
      }
      return textMap[status] || status
    },

    // 获取进度条颜色
    getProgressColor(percentage) {
      if (percentage >= 90) return '#67C23A'
      if (percentage >= 70) return '#E6A23C'
      return '#F56C6C'
    },

    // 获取截止时间样式类
    getDeadlineClass(deadline) {
      const now = new Date()
      const deadlineDate = new Date(deadline)
      const diffDays = Math.ceil((deadlineDate - now) / (1000 * 60 * 60 * 24))
      
      if (diffDays < 0) return 'deadline-overdue'
      if (diffDays <= 3) return 'deadline-urgent'
      if (diffDays <= 7) return 'deadline-warning'
      return 'deadline-normal'
    }
  }
}
</script>

<style scoped>
.data-submission-tab {
  padding: 20px 0;
}

.mb-20 {
  margin-bottom: 20px;
}

.submission-card {
  height: 100px;
}

.submission-content {
  display: flex;
  align-items: center;
  height: 100%;
}

.submission-icon {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 20px;
  color: white;
  margin-right: 15px;
}

.submission-icon.total {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.submission-icon.completed {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
}

.submission-icon.pending {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
}

.submission-icon.rate {
  background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
}

.submission-info {
  flex: 1;
}

.submission-number {
  font-size: 20px;
  font-weight: bold;
  color: #303133;
  line-height: 1;
  margin-bottom: 5px;
}

.submission-label {
  font-size: 12px;
  color: #909399;
}

.deadline-normal {
  color: #303133;
}

.deadline-warning {
  color: #E6A23C;
}

.deadline-urgent {
  color: #F56C6C;
  font-weight: bold;
}

.deadline-overdue {
  color: #F56C6C;
  font-weight: bold;
  text-decoration: line-through;
}
</style>
