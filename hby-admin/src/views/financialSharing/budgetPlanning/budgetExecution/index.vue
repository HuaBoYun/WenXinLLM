<template>
  <div class="app-container">
    <!-- 统计卡片 -->
    <el-row :gutter="20" class="statistics-row">
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-content">
            <div class="stat-label">总记录数</div>
            <div class="stat-value">{{ statistics.totalCount || 0 }}</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card stat-normal">
          <div class="stat-content">
            <div class="stat-label">正常</div>
            <div class="stat-value">{{ statistics.normalCount || 0 }}</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card stat-warning">
          <div class="stat-content">
            <div class="stat-label">预警</div>
            <div class="stat-value">{{ statistics.warningCount || 0 }}</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card stat-exceeded">
          <div class="stat-content">
            <div class="stat-label">超支</div>
            <div class="stat-value">{{ statistics.exceededCount || 0 }}</div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 查询表单 -->
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="100px" class="query-form">
      <el-form-item label="预算模型" prop="modelId">
        <el-select v-model="queryParams.modelId" placeholder="请选择预算模型" clearable filterable>
          <el-option
            v-for="item in modelList"
            :key="item.modelId"
            :label="item.modelName"
            :value="item.modelId">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="组织" prop="orgId">
        <el-input v-model="queryParams.orgId" placeholder="请输入组织ID" clearable />
      </el-form-item>
      <el-form-item label="科目编码" prop="subjectCode">
        <el-input v-model="queryParams.subjectCode" placeholder="请输入科目编码" clearable />
      </el-form-item>
      <el-form-item label="期间" prop="period">
        <el-input v-model="queryParams.period" placeholder="请输入期间" clearable />
      </el-form-item>
      <el-form-item label="预算年度" prop="budgetYear">
        <el-input v-model="queryParams.budgetYear" placeholder="请输入预算年度" clearable />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
          <el-option label="正常" value="NORMAL" />
          <el-option label="预警" value="WARNING" />
          <el-option label="超支" value="EXCEEDED" />
        </el-select>
      </el-form-item>
      <el-form-item label="预警级别" prop="warningLevel">
        <el-select v-model="queryParams.warningLevel" placeholder="请选择预警级别" clearable>
          <el-option label="低" value="LOW" />
          <el-option label="中" value="MEDIUM" />
          <el-option label="高" value="HIGH" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" @click="handleQuery">查询</el-button>
        <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
        <el-button type="success" icon="el-icon-refresh-right" @click="handleRefresh">刷新数据</el-button>
      </el-form-item>
    </el-form>

    <!-- 数据表格 -->
    <el-table
      v-loading="loading"
      :data="executionList"
      border
      stripe
      style="width: 100%">
      <el-table-column type="index" label="序号" width="60" align="center" />
      <el-table-column prop="modelName" label="预算模型" min-width="150" show-overflow-tooltip />
      <el-table-column prop="orgName" label="组织" min-width="150" show-overflow-tooltip />
      <el-table-column prop="subjectCode" label="科目编码" min-width="120" show-overflow-tooltip />
      <el-table-column prop="subjectName" label="科目名称" min-width="150" show-overflow-tooltip />
      <el-table-column prop="period" label="期间" width="100" align="center" />
      <el-table-column prop="budgetYear" label="年度" width="80" align="center" />
      <el-table-column prop="budgetAmount" label="预算金额" width="120" align="right">
        <template slot-scope="scope">
          {{ formatMoney(scope.row.budgetAmount) }}
        </template>
      </el-table-column>
      <el-table-column prop="adjustedAmount" label="调整后预算" width="120" align="right">
        <template slot-scope="scope">
          {{ formatMoney(scope.row.adjustedAmount) }}
        </template>
      </el-table-column>
      <el-table-column prop="actualAmount" label="实际发生" width="120" align="right">
        <template slot-scope="scope">
          {{ formatMoney(scope.row.actualAmount) }}
        </template>
      </el-table-column>
      <el-table-column prop="availableAmount" label="可用金额" width="120" align="right">
        <template slot-scope="scope">
          <span :class="scope.row.availableAmount < 0 ? 'text-danger' : ''">
            {{ formatMoney(scope.row.availableAmount) }}
          </span>
        </template>
      </el-table-column>
      <el-table-column prop="executionRate" label="执行率" width="100" align="right">
        <template slot-scope="scope">
          <span :class="getExecutionRateClass(scope.row.executionRate)">
            {{ formatPercent(scope.row.executionRate) }}
          </span>
        </template>
      </el-table-column>
      <el-table-column prop="status" label="状态" width="80" align="center">
        <template slot-scope="scope">
          <el-tag :type="getStatusType(scope.row.status)" size="small">
            {{ getStatusText(scope.row.status) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="warningLevel" label="预警级别" width="90" align="center">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.warningLevel" :type="getWarningLevelType(scope.row.warningLevel)" size="small">
            {{ getWarningLevelText(scope.row.warningLevel) }}
          </el-tag>
          <span v-else style="color: #C0C4CC">—</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="100" align="center" fixed="right">
        <template slot-scope="scope">
          <el-button type="text" size="small" @click="handleDetail(scope.row)">详情</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 详情对话框 -->
    <el-dialog title="预算执行详情" :visible.sync="detailVisible" width="800px" @close="detailRow = null">
      <div v-if="detailRow" class="detail-wrapper">
        <el-descriptions :column="2" border size="small">
          <el-descriptions-item label="预算模型">{{ detailRow.modelName || '-' }}</el-descriptions-item>
          <el-descriptions-item label="组织">{{ detailRow.orgName || detailRow.orgCode || '-' }}</el-descriptions-item>
          <el-descriptions-item label="科目编码">{{ detailRow.subjectCode || '-' }}</el-descriptions-item>
          <el-descriptions-item label="科目名称">{{ detailRow.subjectName || '-' }}</el-descriptions-item>
          <el-descriptions-item label="期间">{{ detailRow.period || '-' }}</el-descriptions-item>
          <el-descriptions-item label="年度">{{ detailRow.budgetYear || '-' }}</el-descriptions-item>
          <el-descriptions-item label="版本号">{{ detailRow.versionNo || '-' }}</el-descriptions-item>
          <el-descriptions-item label="业务组织">{{ detailRow.bizOrgId || '-' }}</el-descriptions-item>
          <el-descriptions-item label="维度1">{{ detailRow.dimension1Name || detailRow.dimension1Code || '-' }}</el-descriptions-item>
          <el-descriptions-item label="维度2">{{ detailRow.dimension2Name || detailRow.dimension2Code || '-' }}</el-descriptions-item>
          <el-descriptions-item label="预算金额">{{ formatMoney(detailRow.budgetAmount) }}</el-descriptions-item>
          <el-descriptions-item label="调整后预算">{{ formatMoney(detailRow.adjustedAmount) }}</el-descriptions-item>
          <el-descriptions-item label="实际发生">{{ formatMoney(detailRow.actualAmount) }}</el-descriptions-item>
          <el-descriptions-item label="承诺金额">{{ formatMoney(detailRow.committedAmount) }}</el-descriptions-item>
          <el-descriptions-item label="占用金额">{{ formatMoney(detailRow.occupiedAmount) }}</el-descriptions-item>
          <el-descriptions-item label="可用金额">
            <span :class="detailRow.availableAmount < 0 ? 'text-danger' : ''">
              {{ formatMoney(detailRow.availableAmount) }}
            </span>
          </el-descriptions-item>
          <el-descriptions-item label="差异金额">{{ formatMoney(detailRow.varianceAmount) }}</el-descriptions-item>
          <el-descriptions-item label="差异率">{{ formatPercent(detailRow.varianceRate) }}</el-descriptions-item>
          <el-descriptions-item label="执行率">
            <span :class="getExecutionRateClass(detailRow.executionRate)">{{ formatPercent(detailRow.executionRate) }}</span>
          </el-descriptions-item>
          <el-descriptions-item label="状态">
            <el-tag :type="getStatusType(detailRow.status)" size="small">{{ getStatusText(detailRow.status) }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="预警级别">
            <el-tag v-if="detailRow.warningLevel" :type="getWarningLevelType(detailRow.warningLevel)" size="small">
              {{ getWarningLevelText(detailRow.warningLevel) }}
            </el-tag>
            <span v-else>-</span>
          </el-descriptions-item>
          <el-descriptions-item label="最后更新时间" :span="2">{{ detailRow.lastUpdateTime || '-' }}</el-descriptions-item>
        </el-descriptions>
      </div>
      <div slot="footer">
        <el-button @click="detailVisible = false">关闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getExecutionList, getExecutionStatistics, refreshExecutionData } from '@/api/financialSharing/budgetExecution'
import { getBudgetModelList } from '@/api/financialSharing/budgetModel'
import Pagination from '@/components/Pagination'

export default {
  name: 'BudgetExecutionIndex',
  components: {
    Pagination
  },
  data() {
    return {
      // 加载状态
      loading: false,
      // 执行列表
      executionList: [],
      // 总记录数
      total: 0,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        modelId: null,
        orgId: null,
        subjectCode: null,
        period: null,
        budgetYear: null,
        status: null,
        warningLevel: null
      },
      // 统计数据
      statistics: {
        totalCount: 0,
        normalCount: 0,
        warningCount: 0,
        exceededCount: 0
      },
      // 预算模型列表
      modelList: [],
      // 详情弹窗
      detailVisible: false,
      detailRow: null
    }
  },
  created() {
    this.getModelList()
    this.getList()
    this.getStatistics()
  },
  methods: {
    /** 查询执行列表 */
    getList() {
      this.loading = true
      getExecutionList(this.queryParams).then(response => {
        if (response.code === 1) {
          this.executionList = response.data.list || []
          this.total = response.data.total || 0
        }
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    /** 查询统计数据 */
    getStatistics() {
      getExecutionStatistics(this.queryParams).then(response => {
        // 调试用: 浏览器 DevTools Console 直接看接口返回
        console.log('[BudgetExecution] getExecutionStatistics 返回:', response)
        if (response.code === 1 && response.data) {
          this.statistics = {
            totalCount: Number(response.data.totalCount) || 0,
            normalCount: Number(response.data.normalCount) || 0,
            warningCount: Number(response.data.warningCount) || 0,
            exceededCount: Number(response.data.exceededCount) || 0
          }
        } else {
          this.$message.warning(response.msg || '加载统计信息失败, 请查看控制台')
        }
      }).catch(error => {
        console.error('加载统计信息失败:', error)
        this.$message.error('加载统计信息失败: ' + (error.message || error))
      })
    },
    /** 查询预算模型列表 */
    getModelList() {
      getBudgetModelList({ status: 'ACTIVE' }).then(response => {
        if (response.code === 1) {
          this.modelList = response.data || []
        }
      })
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
      this.getStatistics()
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.$refs.queryForm.resetFields()
      this.handleQuery()
    },
    /** 刷新数据 */
    handleRefresh() {
      this.$confirm('确认刷新预算执行数据吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        // 复用表格内的 v-loading, 不再用 this.$loading 全屏黑遮罩
        this.loading = true
        refreshExecutionData(this.queryParams).then(response => {
          if (response.code === 1) {
            this.$message.success((response.data && response.data.message) || '刷新成功')
            // 刷新成功后重新拉数据 / 统计 (各自管自己的 loading 状态)
            this.getList()
            this.getStatistics()
          } else {
            this.$message.error(response.msg || '刷新失败')
            this.loading = false
          }
        }).catch(() => {
          this.loading = false
        })
      }).catch(() => {})
    },
    /** 详情按钮 -> 弹窗(不再跳路由) */
    handleDetail(row) {
      this.detailRow = row
      this.detailVisible = true
    },
    /** 格式化金额 */
    formatMoney(value) {
      if (value == null) return '0.00'
      return parseFloat(value).toLocaleString('zh-CN', { minimumFractionDigits: 2, maximumFractionDigits: 2 })
    },
    /** 格式化百分比 */
    formatPercent(value) {
      if (value == null) return '0.00%'
      return parseFloat(value).toFixed(2) + '%'
    },
    /** 获取执行率样式 */
    getExecutionRateClass(rate) {
      if (rate > 100) return 'text-danger'
      if (rate > 90) return 'text-warning'
      return ''
    },
    /** 获取状态类型 */
    getStatusType(status) {
      const statusMap = {
        'NORMAL': 'success',
        'WARNING': 'warning',
        'EXCEEDED': 'danger'
      }
      return statusMap[status] || 'info'
    },
    /** 获取状态文本 */
    getStatusText(status) {
      const statusMap = {
        'NORMAL': '正常',
        'WARNING': '预警',
        'EXCEEDED': '超支'
      }
      return statusMap[status] || status
    },
    /** 获取预警级别类型 */
    getWarningLevelType(level) {
      const levelMap = {
        'LOW': 'info',
        'MEDIUM': 'warning',
        'HIGH': 'danger'
      }
      return levelMap[level] || 'info'
    },
    /** 获取预警级别文本 */
    getWarningLevelText(level) {
      const levelMap = {
        'LOW': '低',
        'MEDIUM': '中',
        'HIGH': '高'
      }
      return levelMap[level] || level
    }
  }
}
</script>

<style scoped>
.statistics-row {
  margin-bottom: 20px;
}

.stat-card {
  cursor: pointer;
  transition: all 0.3s;
}

.stat-card:hover {
  transform: translateY(-5px);
}

.stat-content {
  text-align: center;
}

.stat-label {
  font-size: 14px;
  color: #909399;
  margin-bottom: 10px;
}

.stat-value {
  font-size: 28px;
  font-weight: bold;
  color: #303133;
}

.stat-normal .stat-value {
  color: #67C23A;
}

.stat-warning .stat-value {
  color: #E6A23C;
}

.stat-exceeded .stat-value {
  color: #F56C6C;
}

.query-form {
  background: #fff;
  padding: 20px;
  margin-bottom: 10px;
}

.text-danger {
  color: #F56C6C;
}

.text-warning {
  color: #E6A23C;
}
</style>

