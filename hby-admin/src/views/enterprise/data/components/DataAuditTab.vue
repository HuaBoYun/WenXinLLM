<template>
  <div class="data-audit-tab">
    <!-- 查询表单 -->
    <el-form :model="queryForm" ref="queryForm" :inline="true" label-width="100px" class="mb-20">
      <el-form-item label="审核类型">
        <el-select v-model="queryForm.auditType" placeholder="请选择审核类型" clearable style="width: 150px;">
          <el-option label="数据录入审核" value="DATA_ENTRY"></el-option>
          <el-option label="质量审核" value="QUALITY"></el-option>
          <el-option label="报送审核" value="SUBMISSION"></el-option>
          <el-option label="合规审核" value="COMPLIANCE"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="审核状态">
        <el-select v-model="queryForm.auditStatus" placeholder="请选择审核状态" clearable style="width: 150px;">
          <el-option label="待审核" value="PENDING"></el-option>
          <el-option label="审核中" value="REVIEWING"></el-option>
          <el-option label="已通过" value="APPROVED"></el-option>
          <el-option label="已拒绝" value="REJECTED"></el-option>
          <el-option label="需修改" value="REVISION"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="审核人">
        <el-input v-model="queryForm.auditPerson" placeholder="请输入审核人" clearable style="width: 120px;"></el-input>
      </el-form-item>
      <el-form-item label="审核时间">
        <el-date-picker
          v-model="queryForm.auditTimeRange"
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
        <el-button type="success" @click="handleBatchApprove" icon="el-icon-check">批量通过</el-button>
        <el-button type="danger" @click="handleBatchReject" icon="el-icon-close">批量拒绝</el-button>
      </el-form-item>
    </el-form>

    <!-- 审核概览统计 -->
    <el-row :gutter="20" class="mb-20">
      <el-col :span="6">
        <el-card class="audit-card">
          <div class="audit-content">
            <div class="audit-icon pending">
              <i class="el-icon-time"></i>
            </div>
            <div class="audit-info">
              <div class="audit-number">{{ auditStats.pendingCount || 0 }}</div>
              <div class="audit-label">待审核</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="audit-card">
          <div class="audit-content">
            <div class="audit-icon approved">
              <i class="el-icon-circle-check"></i>
            </div>
            <div class="audit-info">
              <div class="audit-number">{{ auditStats.approvedCount || 0 }}</div>
              <div class="audit-label">已通过</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="audit-card">
          <div class="audit-content">
            <div class="audit-icon rejected">
              <i class="el-icon-circle-close"></i>
            </div>
            <div class="audit-info">
              <div class="audit-number">{{ auditStats.rejectedCount || 0 }}</div>
              <div class="audit-label">已拒绝</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="audit-card">
          <div class="audit-content">
            <div class="audit-icon rate">
              <i class="el-icon-data-line"></i>
            </div>
            <div class="audit-info">
              <div class="audit-number">{{ auditStats.approvalRate || 0 }}%</div>
              <div class="audit-label">通过率</div>
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
      <el-table-column prop="dataType" label="数据类型" width="120" align="center">
        <template slot-scope="scope">
          <el-tag :type="getAuditTypeTag(scope.row.dataType)">
            {{ scope.row.dataType }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="dataCategory" label="数据类别" width="120" align="center"></el-table-column>
      <el-table-column prop="submitter" label="提交人" width="100" align="center"></el-table-column>
      <el-table-column prop="submitTime" label="提交时间" width="160" align="center"></el-table-column>
      <el-table-column prop="auditor" label="审核人" width="100" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.auditor || '-' }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="auditTime" label="审核时间" width="160" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.auditTime || '-' }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="status" label="审核状态" width="100" align="center">
        <template slot-scope="scope">
          <el-tag :type="getAuditStatusTag(scope.row.status)">
            {{ scope.row.status }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="qualityScore" label="质量评分" width="100" align="center">
        <template slot-scope="scope">
          <span v-if="scope.row.qualityScore">{{ scope.row.qualityScore }}分</span>
          <span v-else style="color:#909399">-</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="380" align="center" fixed="right">
        <template slot-scope="scope">
          <el-button size="mini" @click="handleView(scope.row)" icon="el-icon-view">查看</el-button>
          <el-button size="mini" type="success" @click="handleApprove(scope.row)" icon="el-icon-check" v-if="scope.row.status === '已提交' || scope.row.auditStatus === 'PENDING'">通过</el-button>
          <el-button size="mini" type="danger" @click="handleReject(scope.row)" icon="el-icon-close" v-if="scope.row.status === '已提交' || scope.row.auditStatus === 'PENDING'">拒绝</el-button>
          <el-button size="mini" type="danger" @click="handleDelete(scope.row)" icon="el-icon-delete" v-if="scope.row.auditStatus === 'PENDING' || scope.row.status === '草稿'">删除</el-button>
          <el-dropdown @command="handleCommand" style="margin-left: 10px;">
            <el-button size="mini" type="info">
              更多<i class="el-icon-arrow-down el-icon--right"></i>
            </el-button>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item :command="{action: 'workflow', row: scope.row}" icon="el-icon-share">审核流程</el-dropdown-item>
              <el-dropdown-item :command="{action: 'history', row: scope.row}" icon="el-icon-time">审核历史</el-dropdown-item>
              <el-dropdown-item :command="{action: 'revision', row: scope.row}" icon="el-icon-edit" v-if="scope.row.auditStatus === 'PENDING'">要求修改</el-dropdown-item>
              <el-dropdown-item :command="{action: 'assign', row: scope.row}" icon="el-icon-user" v-if="scope.row.auditStatus === 'PENDING'">分配审核人</el-dropdown-item>
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

    <!-- 审核详情对话框 -->
    <AuditDetailDialog
      :visible.sync="detailDialogVisible"
      :audit-id="currentRow.auditId"
    />

    <!-- 审核操作对话框 -->
    <AuditActionDialog
      :visible.sync="actionDialogVisible"
      :audit-data="currentRow"
      :action-type="actionType"
      @refresh="getList"
    />

    <!-- 审核流程对话框 -->
    <AuditWorkflowDialog
      :visible.sync="workflowDialogVisible"
      :audit-id="currentRow.auditId"
    />

    <!-- 批量审核对话框 -->
    <BatchAuditDialog
      :visible.sync="batchDialogVisible"
      :selected-audits="multipleSelection"
      :action-type="batchActionType"
      @refresh="getList"
    />
  </div>
</template>

<script>
import {
  getDataAuditList,
  approveDataAudit,
  rejectDataAudit,
  getAuditWorkflow,
  getAuditHistory,
  deleteDataAudit
} from '@/api/enterprise/data'
import Pagination from '@/components/Pagination'
import AuditDetailDialog from './AuditDetailDialog'
import AuditActionDialog from './AuditActionDialog'
import AuditWorkflowDialog from './AuditWorkflowDialog'
import BatchAuditDialog from './BatchAuditDialog'

export default {
  name: 'DataAuditTab',
  components: {
    Pagination,
    AuditDetailDialog,
    AuditActionDialog,
    AuditWorkflowDialog,
    BatchAuditDialog
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
      auditStats: {},
      queryForm: {
        pageNumber: 1,
        pageSize: 10,
        auditType: '',
        auditStatus: '',
        auditPerson: '',
        auditTimeRange: null
      },
      
      // 对话框状态
      detailDialogVisible: false,
      actionDialogVisible: false,
      workflowDialogVisible: false,
      batchDialogVisible: false,
      actionType: '',
      batchActionType: '',
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
    // 获取列表数据（审核Tab显示所有数据，不再硬编码状态）
    getList() {
      this.loading = true
      const params = {
        ...this.queryForm
      }
      // 如果选择了审核状态筛选，映射到status字段
      if (this.queryForm.auditStatus) {
        const statusMap = { 'PENDING': '已提交', 'REVIEWING': '审核中', 'APPROVED': '已审核', 'REJECTED': '已退回', 'REVISION': '需修改' }
        params.status = statusMap[this.queryForm.auditStatus] || this.queryForm.auditStatus
      }
      // 审核人筛选
      if (this.queryForm.auditPerson) {
        params.submitter = this.queryForm.auditPerson
      }
      // 审核时间范围
      if (this.queryForm.auditTimeRange && this.queryForm.auditTimeRange.length === 2) {
        const fmt = (d) => { const dt = new Date(d); return dt.getFullYear() + '-' + String(dt.getMonth() + 1).padStart(2, '0') + '-' + String(dt.getDate()).padStart(2, '0') }
        params.entryDateStart = fmt(this.queryForm.auditTimeRange[0])
        params.entryDateEnd = fmt(this.queryForm.auditTimeRange[1])
      }

      getDataAuditList(params).then(response => {
        const data = response.data || {}
        this.tableData = data.tlist || data.records || []
        this.total = data.totalRecord || data.total || this.tableData.length
        this.loading = false
        this.loadAuditStats()
      }).catch(() => {
        this.tableData = []
        this.total = 0
        this.loading = false
      })
    },

    // 加载审核统计（基于列表数据计算）
    loadAuditStats() {
      const list = this.tableData
      const approved = list.filter(i => i.status === '已审核').length
      const rejected = list.filter(i => i.status === '已退回').length
      const pending = list.filter(i => i.status === '已提交').length
      const total = approved + rejected + pending || 1
      this.auditStats = {
        pendingCount: pending,
        approvedCount: approved,
        rejectedCount: rejected,
        approvalRate: Math.round(approved / total * 100 * 10) / 10
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
        auditType: '',
        auditStatus: '',
        auditPerson: '',
        auditTimeRange: null
      }
      this.getList()
    },

    // 批量通过
    handleBatchApprove() {
      if (this.multipleSelection.length === 0) {
        this.$message.warning('请选择要审核的数据')
        return
      }
      this.batchActionType = 'approve'
      this.batchDialogVisible = true
    },

    // 批量拒绝
    handleBatchReject() {
      if (this.multipleSelection.length === 0) {
        this.$message.warning('请选择要审核的数据')
        return
      }
      this.batchActionType = 'reject'
      this.batchDialogVisible = true
    },

    // 查看
    handleView(row) {
      this.currentRow = { ...row }
      this.detailDialogVisible = true
    },

    // 审核通过
    handleApprove(row) {
      this.currentRow = { ...row }
      this.actionType = 'approve'
      this.actionDialogVisible = true
    },

    // 审核拒绝
    handleReject(row) {
      this.currentRow = { ...row }
      this.actionType = 'reject'
      this.actionDialogVisible = true
    },

    // 下拉菜单命令处理
    handleCommand(command) {
      const { action, row } = command
      this.currentRow = { ...row }

      switch (action) {
        case 'workflow':
          this.workflowDialogVisible = true
          break
        case 'history':
          this.handleViewAuditHistory(row)
          break
        case 'revision':
          this.actionType = 'revision'
          this.actionDialogVisible = true
          break
        case 'assign':
          this.handleAssignAuditor(row)
          break
      }
    },

    // 查看审核历史
    async handleViewAuditHistory(row) {
      try {
        const response = await getAuditHistory(row.id)
        if (response.result == 200 && response.data) {
          const list = response.data || []
          if (list.length > 0) {
            const historyHtml = list.map(item =>
              `<tr><td>${item.time || '-'}</td><td>${item.operator || '-'}</td><td>${item.action || '-'}</td><td>${item.remark || '-'}</td></tr>`
            ).join('')
            this.$alert(`<table border="1" cellpadding="8" cellspacing="0" style="width:100%;border-collapse:collapse;">
              <thead><tr><th>时间</th><th>操作人</th><th>操作</th><th>备注</th></tr></thead>
              <tbody>${historyHtml}</tbody>
            </table>`, '审核历史记录', {
              dangerouslyUseHTMLString: true,
              confirmButtonText: '确定'
            })
          } else {
            this.$message.info('暂无审核历史记录')
          }
        }
      } catch (error) {
        this.$message.error('获取审核历史失败')
      }
    },

    // 分配审核人
    handleAssignAuditor(row) {
      this.$prompt('请输入审核人姓名', '分配审核人', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputPattern: /\S+/,
        inputErrorMessage: '审核人姓名不能为空'
      }).then(async ({ value }) => {
        try {
          const { updateDataEntry } = require('@/api/enterprise/data')
          const response = await updateDataEntry({
            id: row.id,
            auditor: value
          })
          if (response.result == 200) {
            this.$message.success(`已分配审核人：${value}`)
            this.getList()
          } else {
            this.$message.error(response.msg || '分配失败')
          }
        } catch (error) {
          this.$message.error('分配审核人失败')
        }
      }).catch(() => {})
    },

    // 删除
    handleDelete(row) {
      this.$confirm(`确认删除该审核记录？`, '提示', {
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

          const response = await deleteDataAudit(row.id)
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
      }).catch(() => {
        // 用户取消删除
      })
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

    // 获取审核类型标签
    getAuditTypeTag(type) {
      const tagMap = {
        'DATA_ENTRY': 'primary',
        'QUALITY': 'success',
        'SUBMISSION': 'warning',
        'COMPLIANCE': 'danger'
      }
      return tagMap[type] || 'info'
    },

    // 获取审核类型文本
    getAuditTypeText(type) {
      const textMap = {
        'DATA_ENTRY': '数据录入',
        'QUALITY': '质量审核',
        'SUBMISSION': '报送审核',
        'COMPLIANCE': '合规审核'
      }
      return textMap[type] || type
    },

    // 获取审核状态标签
    getAuditStatusTag(status) {
      const tagMap = {
        'PENDING': 'info',
        'REVIEWING': 'warning',
        'APPROVED': 'success',
        'REJECTED': 'danger',
        'REVISION': 'primary'
      }
      return tagMap[status] || 'info'
    },

    // 获取审核状态文本
    getAuditStatusText(status) {
      const textMap = {
        'PENDING': '待审核',
        'REVIEWING': '审核中',
        'APPROVED': '已通过',
        'REJECTED': '已拒绝',
        'REVISION': '需修改'
      }
      return textMap[status] || status
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
    }
  }
}
</script>

<style scoped>
.data-audit-tab {
  padding: 20px 0;
}

.mb-20 {
  margin-bottom: 20px;
}

.audit-card {
  height: 100px;
}

.audit-content {
  display: flex;
  align-items: center;
  height: 100%;
}

.audit-icon {
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

.audit-icon.pending {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.audit-icon.approved {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
}

.audit-icon.rejected {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
}

.audit-icon.rate {
  background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
}

.audit-info {
  flex: 1;
}

.audit-number {
  font-size: 20px;
  font-weight: bold;
  color: #303133;
  line-height: 1;
  margin-bottom: 5px;
}

.audit-label {
  font-size: 12px;
  color: #909399;
}
</style>
