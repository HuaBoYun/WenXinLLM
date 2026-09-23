<template>
  <div class="archive-permission-audit">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <h1 class="page-title">
            <i class="el-icon-document-checked"></i>
            权限审计
          </h1>
          <p class="page-description">
            权限使用记录、审计日志和安全分析
          </p>
        </div>
        <div class="header-right">
          <el-button type="primary" icon="el-icon-download" @click="exportAuditLog">
            导出审计日志
          </el-button>
          <el-button type="success" icon="el-icon-refresh" @click="refreshData">
            刷新数据
          </el-button>
        </div>
      </div>
    </div>

    <!-- 筛选区域 -->
    <div class="filter-container">
      <el-form :model="filterForm" :inline="true" class="filter-form">
        <el-form-item label="时间范围">
          <el-date-picker
            v-model="filterForm.dateRange"
            type="datetimerange"
            range-separator="至"
            start-placeholder="开始时间"
            end-placeholder="结束时间"
            format="yyyy-MM-dd HH:mm:ss"
            value-format="yyyy-MM-dd HH:mm:ss"
          />
        </el-form-item>
        <el-form-item label="用户ID">
          <el-input
            v-model="filterForm.userId"
            placeholder="请输入用户ID"
            clearable
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="操作类型">
          <el-select
            v-model="filterForm.operationType"
            placeholder="请选择操作类型"
            clearable
            style="width: 150px"
          >
            <el-option label="权限申请" value="APPLY" />
            <el-option label="权限授予" value="GRANT" />
            <el-option label="权限撤销" value="REVOKE" />
            <el-option label="权限使用" value="USE" />
            <el-option label="权限审批" value="APPROVE" />
          </el-select>
        </el-form-item>
        <el-form-item label="风险等级">
          <el-select
            v-model="filterForm.riskLevel"
            placeholder="请选择风险等级"
            clearable
            style="width: 150px"
          >
            <el-option label="低风险" value="LOW" />
            <el-option label="中风险" value="MEDIUM" />
            <el-option label="高风险" value="HIGH" />
            <el-option label="严重风险" value="CRITICAL" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleFilter">
            筛选
          </el-button>
          <el-button icon="el-icon-refresh" @click="handleReset">
            重置
          </el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 统计卡片 -->
    <div class="statistics-cards">
      <el-row :gutter="20">
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon total">
              <i class="el-icon-document"></i>
            </div>
            <div class="stat-content">
              <div class="stat-number">{{ auditStats.totalLogs || 0 }}</div>
              <div class="stat-label">审计日志总数</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon today">
              <i class="el-icon-time"></i>
            </div>
            <div class="stat-content">
              <div class="stat-number">{{ auditStats.todayLogs || 0 }}</div>
              <div class="stat-label">今日新增</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon risk">
              <i class="el-icon-warning"></i>
            </div>
            <div class="stat-content">
              <div class="stat-number">{{ auditStats.riskLogs || 0 }}</div>
              <div class="stat-label">风险操作</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon anomaly">
              <i class="el-icon-warning-outline"></i>
            </div>
            <div class="stat-content">
              <div class="stat-number">{{ auditStats.anomalyLogs || 0 }}</div>
              <div class="stat-label">异常操作</div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 审计日志表格 -->
    <div class="audit-table-container">
      <el-table
        v-loading="loading"
        :data="auditLogs"
        stripe
        border
        style="width: 100%"
        @sort-change="handleSortChange"
      >
        <el-table-column
          prop="logId"
          label="日志ID"
          width="120"
          show-overflow-tooltip
        />
        <el-table-column
          prop="operationType"
          label="操作类型"
          width="120"
          align="center"
        >
          <template slot-scope="scope">
            <el-tag :type="getOperationTypeColor(scope.row.operationType)">
              {{ formatOperationType(scope.row.operationType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column
          prop="userId"
          label="用户ID"
          width="120"
          show-overflow-tooltip
        />
        <el-table-column
          prop="userName"
          label="用户名称"
          width="120"
          show-overflow-tooltip
        />
        <el-table-column
          prop="permissionName"
          label="权限名称"
          width="200"
          show-overflow-tooltip
        />
        <el-table-column
          prop="resourceType"
          label="资源类型"
          width="120"
          align="center"
        >
          <template slot-scope="scope">
            {{ formatResourceType(scope.row.resourceType) }}
          </template>
        </el-table-column>
        <el-table-column
          prop="resourceId"
          label="资源ID"
          width="150"
          show-overflow-tooltip
        />
        <el-table-column
          prop="operationResult"
          label="操作结果"
          width="100"
          align="center"
        >
          <template slot-scope="scope">
            <el-tag :type="scope.row.operationResult === 'SUCCESS' ? 'success' : 'danger'">
              {{ scope.row.operationResult === 'SUCCESS' ? '成功' : '失败' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column
          prop="riskLevel"
          label="风险等级"
          width="100"
          align="center"
        >
          <template slot-scope="scope">
            <el-tag :type="getRiskLevelColor(scope.row.riskLevel)">
              {{ formatRiskLevel(scope.row.riskLevel) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column
          prop="accessIp"
          label="访问IP"
          width="120"
          show-overflow-tooltip
        />
        <el-table-column
          prop="operationTime"
          label="操作时间"
          width="160"
          align="center"
          sortable="custom"
        >
          <template slot-scope="scope">
            {{ formatDateTime(scope.row.operationTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="120" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button
              type="primary"
              size="mini"
              @click="viewDetail(scope.row)"
            >
              查看详情
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 分页 -->
    <div class="pagination-container">
      <el-pagination
        :current-page="pagination.current"
        :page-sizes="[10, 20, 50, 100]"
        :page-size="pagination.size"
        :total="pagination.total"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>

    <!-- 详情对话框 -->
    <el-dialog
      title="审计日志详情"
      :visible.sync="detailDialogVisible"
      width="800px"
    >
      <div class="audit-detail">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="日志ID">
            {{ auditDetail.logId }}
          </el-descriptions-item>
          <el-descriptions-item label="操作类型">
            <el-tag :type="getOperationTypeColor(auditDetail.operationType)">
              {{ formatOperationType(auditDetail.operationType) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="用户ID">
            {{ auditDetail.userId }}
          </el-descriptions-item>
          <el-descriptions-item label="用户名称">
            {{ auditDetail.userName }}
          </el-descriptions-item>
          <el-descriptions-item label="权限名称">
            {{ auditDetail.permissionName }}
          </el-descriptions-item>
          <el-descriptions-item label="权限类型">
            {{ formatPermissionType(auditDetail.permissionType) }}
          </el-descriptions-item>
          <el-descriptions-item label="资源类型">
            {{ formatResourceType(auditDetail.resourceType) }}
          </el-descriptions-item>
          <el-descriptions-item label="资源ID">
            {{ auditDetail.resourceId }}
          </el-descriptions-item>
          <el-descriptions-item label="操作结果">
            <el-tag :type="auditDetail.operationResult === 'SUCCESS' ? 'success' : 'danger'">
              {{ auditDetail.operationResult === 'SUCCESS' ? '成功' : '失败' }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="风险等级">
            <el-tag :type="getRiskLevelColor(auditDetail.riskLevel)">
              {{ formatRiskLevel(auditDetail.riskLevel) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="访问IP">
            {{ auditDetail.accessIp }}
          </el-descriptions-item>
          <el-descriptions-item label="用户代理">
            {{ auditDetail.userAgent }}
          </el-descriptions-item>
          <el-descriptions-item label="操作时间" :span="2">
            {{ formatDateTime(auditDetail.operationTime) }}
          </el-descriptions-item>
          <el-descriptions-item label="操作描述" :span="2">
            {{ auditDetail.operationDescription || '无' }}
          </el-descriptions-item>
          <el-descriptions-item label="失败原因" :span="2" v-if="auditDetail.operationResult === 'FAILURE'">
            {{ auditDetail.failureReason || '无' }}
          </el-descriptions-item>
        </el-descriptions>

        <!-- 操作详情 -->
        <div class="operation-details" v-if="auditDetail.operationDetails">
          <h4>操作详情</h4>
          <el-input
            type="textarea"
            :rows="6"
            :value="JSON.stringify(auditDetail.operationDetails, null, 2)"
            readonly
          />
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  formatPermissionType,
  formatResourceType,
  formatRiskLevel,
  getRiskLevelColor
} from '@/api/managementAccountant/as/archivePermission'

export default {
  name: 'ArchivePermissionAudit',
  data() {
    return {
      // 筛选表单
      filterForm: {
        dateRange: [],
        userId: '',
        operationType: '',
        riskLevel: ''
      },
      // 审计统计
      auditStats: {
        totalLogs: 0,
        todayLogs: 0,
        riskLogs: 0,
        anomalyLogs: 0
      },
      // 审计日志
      auditLogs: [],
      loading: false,
      // 分页
      pagination: {
        current: 1,
        size: 20,
        total: 0
      },
      // 排序
      sortField: 'operationTime',
      sortOrder: 'desc',
      // 详情对话框
      detailDialogVisible: false,
      auditDetail: {}
    }
  },
  created() {
    this.initDateRange()
    this.loadData()
  },
  methods: {
    // 初始化日期范围
    initDateRange() {
      const end = new Date()
      const start = new Date()
      start.setTime(start.getTime() - 3600 * 1000 * 24 * 7) // 7天前
      this.filterForm.dateRange = [
        start.toISOString().replace('T', ' ').substring(0, 19),
        end.toISOString().replace('T', ' ').substring(0, 19)
      ]
    },
    // 加载数据
    async loadData() {
      this.loading = true
      try {
        await Promise.all([
          this.loadAuditStats(),
          this.loadAuditLogs()
        ])
      } catch (error) {
        console.error('加载数据失败:', error)
        this.$message.error('加载数据失败')
      } finally {
        this.loading = false
      }
    },
    // 加载审计统计
    async loadAuditStats() {
      // TODO: 实现加载审计统计逻辑
      this.auditStats = {
        totalLogs: 1250,
        todayLogs: 45,
        riskLogs: 12,
        anomalyLogs: 3
      }
    },
    // 加载审计日志
    async loadAuditLogs() {
      // TODO: 实现加载审计日志逻辑
      this.auditLogs = [
        {
          logId: 'LOG001',
          operationType: 'GRANT',
          userId: 'user001',
          userName: '张三',
          permissionName: '档案读取权限',
          permissionType: 'RESOURCE',
          resourceType: 'ARCHIVE',
          resourceId: 'ARC001',
          operationResult: 'SUCCESS',
          riskLevel: 'LOW',
          accessIp: '192.0.2.200',
          userAgent: 'Mozilla/5.0...',
          operationTime: new Date(),
          operationDescription: '授予用户档案读取权限'
        },
        {
          logId: 'LOG002',
          operationType: 'USE',
          userId: 'user002',
          userName: '李四',
          permissionName: '文档编辑权限',
          permissionType: 'OPERATION',
          resourceType: 'DOCUMENT',
          resourceId: 'DOC001',
          operationResult: 'FAILURE',
          riskLevel: 'HIGH',
          accessIp: '192.0.2.200',
          userAgent: 'Mozilla/5.0...',
          operationTime: new Date(Date.now() - 3600000),
          operationDescription: '尝试编辑文档但权限不足',
          failureReason: '权限已过期'
        }
      ]
      this.pagination.total = this.auditLogs.length
    },
    // 筛选
    handleFilter() {
      this.pagination.current = 1
      this.loadAuditLogs()
    },
    // 重置筛选
    handleReset() {
      this.initDateRange()
      this.filterForm.userId = ''
      this.filterForm.operationType = ''
      this.filterForm.riskLevel = ''
      this.pagination.current = 1
      this.loadAuditLogs()
    },
    // 刷新数据
    refreshData() {
      this.loadData()
      this.$message.success('数据刷新成功')
    },
    // 导出审计日志
    exportAuditLog() {
      this.$message.info('导出功能开发中...')
    },
    // 查看详情
    viewDetail(row) {
      this.auditDetail = { ...row }
      this.detailDialogVisible = true
    },
    // 排序变化
    handleSortChange({ column, prop, order }) {
      this.sortField = prop
      this.sortOrder = order === 'ascending' ? 'asc' : 'desc'
      this.loadAuditLogs()
    },
    // 分页事件
    handleSizeChange(size) {
      this.pagination.size = size
      this.pagination.current = 1
      this.loadAuditLogs()
    },
    handleCurrentChange(current) {
      this.pagination.current = current
      this.loadAuditLogs()
    },
    // 格式化方法
    formatPermissionType,
    formatResourceType,
    formatRiskLevel,
    getRiskLevelColor,
    formatOperationType(type) {
      const typeMap = {
        APPLY: '权限申请',
        GRANT: '权限授予',
        REVOKE: '权限撤销',
        USE: '权限使用',
        APPROVE: '权限审批'
      }
      return typeMap[type] || type
    },
    getOperationTypeColor(type) {
      const colorMap = {
        APPLY: 'primary',
        GRANT: 'success',
        REVOKE: 'warning',
        USE: 'info',
        APPROVE: 'success'
      }
      return colorMap[type] || 'info'
    },
    formatDateTime(dateTime) {
      if (!dateTime) return '-'
      return new Date(dateTime).toLocaleString()
    }
  }
}
</script>

<style lang="scss" scoped>
.archive-permission-audit {
  padding: 20px;
  background-color: #f5f7fa;
  min-height: calc(100vh - 84px);

  .page-header {
    background: white;
    border-radius: 8px;
    padding: 24px;
    margin-bottom: 20px;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);

    .header-content {
      display: flex;
      justify-content: space-between;
      align-items: center;

      .header-left {
        .page-title {
          margin: 0 0 8px 0;
          font-size: 24px;
          font-weight: 600;
          color: #303133;
          display: flex;
          align-items: center;

          i {
            margin-right: 12px;
            color: #409eff;
          }
        }

        .page-description {
          margin: 0;
          color: #909399;
          font-size: 14px;
        }
      }

      .header-right {
        display: flex;
        gap: 12px;
      }
    }
  }

  .filter-container {
    background: white;
    border-radius: 8px;
    padding: 20px;
    margin-bottom: 20px;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);

    .filter-form {
      .el-form-item {
        margin-bottom: 16px;
      }
    }
  }

  .statistics-cards {
    margin-bottom: 20px;

    .stat-card {
      background: white;
      border-radius: 8px;
      padding: 24px;
      display: flex;
      align-items: center;
      box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
      transition: transform 0.2s;

      &:hover {
        transform: translateY(-2px);
      }

      .stat-icon {
        width: 60px;
        height: 60px;
        border-radius: 50%;
        display: flex;
        align-items: center;
        justify-content: center;
        margin-right: 16px;

        i {
          font-size: 24px;
          color: white;
        }

        &.total {
          background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        }

        &.today {
          background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
        }

        &.risk {
          background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
        }

        &.anomaly {
          background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
        }
      }

      .stat-content {
        flex: 1;

        .stat-number {
          font-size: 28px;
          font-weight: 700;
          color: #303133;
          line-height: 1;
          margin-bottom: 4px;
        }

        .stat-label {
          font-size: 14px;
          color: #909399;
        }
      }
    }
  }

  .audit-table-container {
    background: white;
    border-radius: 8px;
    padding: 20px;
    margin-bottom: 20px;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);

    .el-table {
      .el-table__header {
        th {
          background-color: #f5f7fa;
          color: #606266;
          font-weight: 600;
        }
      }

      .el-table__row {
        &:hover {
          background-color: #f5f7fa;
        }
      }
    }
  }

  .pagination-container {
    background: white;
    border-radius: 8px;
    padding: 16px 20px;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
    display: flex;
    justify-content: center;
  }

  .audit-detail {
    .operation-details {
      margin-top: 20px;

      h4 {
        margin: 0 0 12px 0;
        font-size: 14px;
        font-weight: 600;
        color: #303133;
      }
    }
  }
}

// 标签样式
.el-tag {
  font-size: 12px;
  padding: 0 8px;
  height: 24px;
  line-height: 22px;
  border-radius: 4px;
}

// 描述列表样式
::v-deep .el-descriptions {
  .el-descriptions__header {
    margin-bottom: 16px;
  }

  .el-descriptions__body {
    .el-descriptions__table {
      .el-descriptions__cell {
        padding: 12px 16px;
      }
    }
  }
}

// 对话框样式
::v-deep .el-dialog {
  .el-dialog__header {
    background: #f5f7fa;
    padding: 20px 24px;
    border-bottom: 1px solid #ebeef5;

    .el-dialog__title {
      font-size: 16px;
      font-weight: 600;
      color: #303133;
    }
  }

  .el-dialog__body {
    padding: 24px;
    max-height: 60vh;
    overflow-y: auto;
  }
}

// 响应式设计
@media (max-width: 1200px) {
  .archive-permission-audit {
    .statistics-cards {
      .el-col {
        margin-bottom: 12px;
      }
    }
  }
}

@media (max-width: 768px) {
  .archive-permission-audit {
    padding: 12px;

    .page-header {
      padding: 16px;

      .header-content {
        flex-direction: column;
        align-items: flex-start;
        gap: 16px;

        .header-right {
          width: 100%;
          justify-content: flex-end;
        }
      }
    }

    .filter-container {
      padding: 16px;

      .filter-form {
        .el-form-item {
          display: block;
          margin-bottom: 12px;

          .el-form-item__content {
            margin-left: 0 !important;
          }
        }
      }
    }

    .statistics-cards {
      .stat-card {
        padding: 16px;

        .stat-icon {
          width: 48px;
          height: 48px;

          i {
            font-size: 20px;
          }
        }

        .stat-content {
          .stat-number {
            font-size: 24px;
          }
        }
      }
    }

    .audit-table-container {
      padding: 12px;
      overflow-x: auto;
    }

    .pagination-container {
      padding: 12px;
    }
  }
}

// 表格滚动条样式
.audit-table-container {
  ::-webkit-scrollbar {
    width: 6px;
    height: 6px;
  }

  ::-webkit-scrollbar-track {
    background: #f1f1f1;
    border-radius: 3px;
  }

  ::-webkit-scrollbar-thumb {
    background: #c1c1c1;
    border-radius: 3px;

    &:hover {
      background: #a8a8a8;
    }
  }
}

// 加载状态样式
.el-loading-mask {
  border-radius: 8px;
}
</style>
