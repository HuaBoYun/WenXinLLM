<template>
  <div class="inventory-alert-container">
    <div class="page-header">
      <h2>库存预警管理</h2>
      <p>设置库存预警规则，及时发现库存异常情况</p>
    </div>
    
    <!-- 搜索区域 -->
    <div class="search-container">
      <el-form :model="searchForm" ref="searchForm" :inline="true" class="search-form">
        <el-form-item label="预警类型" prop="alertType">
          <el-select
            v-model="searchForm.alertType"
            placeholder="请选择预警类型"
            clearable
            style="width: 150px"
          >
            <el-option label="库存不足" value="LOW_STOCK" />
            <el-option label="库存过量" value="HIGH_STOCK" />
            <el-option label="呆滞库存" value="SLOW_MOVING" />
            <el-option label="过期预警" value="EXPIRY" />
          </el-select>
        </el-form-item>
        <el-form-item label="预警状态" prop="status">
          <el-select
            v-model="searchForm.status"
            placeholder="请选择状态"
            clearable
            style="width: 120px"
          >
            <el-option label="待处理" value="PENDING" />
            <el-option label="已处理" value="PROCESSED" />
            <el-option label="已忽略" value="IGNORED" />
          </el-select>
        </el-form-item>
        <el-form-item label="存货名称" prop="inventoryName">
          <el-input
            v-model="searchForm.inventoryName"
            placeholder="请输入存货名称"
            clearable
            style="width: 180px"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 统计卡片 -->
    <div class="stats-cards">
      <el-row :gutter="20">
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon pending">
              <i class="el-icon-warning"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ stats.pendingCount }}</div>
              <div class="stat-label">待处理预警</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon low-stock">
              <i class="el-icon-bottom"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ stats.lowStockCount }}</div>
              <div class="stat-label">库存不足</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon high-stock">
              <i class="el-icon-top"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ stats.highStockCount }}</div>
              <div class="stat-label">库存过量</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon slow-moving">
              <i class="el-icon-time"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ stats.slowMovingCount }}</div>
              <div class="stat-label">呆滞库存</div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 工具栏 -->
    <div class="toolbar">
      <el-button type="primary" @click="handleCreateRule">新增预警规则</el-button>
      <el-button type="success" @click="handleBatchProcess" :disabled="!multipleSelection.length">
        批量处理
      </el-button>
      <el-button type="warning" @click="handleRefreshAlert">刷新预警</el-button>
    </div>

    <!-- 数据表格 -->
    <div class="table-container">
      <el-table
        :data="tableData"
        v-loading="loading"
        @selection-change="handleSelectionChange"
        stripe
        border
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="recordId" label="预警编号" width="120" />
        <el-table-column prop="alertTypeName" label="预警类型" width="100">
          <template slot-scope="scope">
            <el-tag :type="getAlertTypeTagType(scope.row.alertType)">
              {{ scope.row.alertTypeName }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="inventoryName" label="存货名称" min-width="150" show-overflow-tooltip />
        <el-table-column prop="warehouseName" label="仓库" width="120" />
        <el-table-column prop="currentQuantity" label="当前库存" width="100" align="right" />
        <el-table-column prop="alertValue" label="阈值" width="100" align="right" />
        <el-table-column prop="alertLevel" label="预警级别" width="100">
          <template slot-scope="scope">
            <el-tag :type="getAlertLevelTagType(scope.row.alertLevel)" size="small">
              {{ getAlertLevelText(scope.row.alertLevel) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="getStatusTagType(scope.row.status)">
              {{ getStatusText(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="alertTime" label="预警时间" width="160" />
        <el-table-column label="操作" width="200" fixed="right">
          <template slot-scope="scope">
            <el-button type="text" size="small" @click="handleView(scope.row)">
              查看
            </el-button>
            <el-button
              v-if="scope.row.status === 'PENDING'"
              type="text"
              size="small"
              @click="handleProcess(scope.row)"
            >
              处理
            </el-button>
            <el-button
              v-if="scope.row.status === 'PENDING'"
              type="text"
              size="small"
              class="warning-text"
              @click="handleIgnore(scope.row)"
            >
              忽略
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页组件 -->
      <div class="pagination-container">
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pagination.currentPage"
          :page-sizes="[10, 20, 50, 100]"
          :page-size="pagination.pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="pagination.total"
        />
      </div>
    </div>

    <!-- 预警规则弹窗 -->
    <alert-rule-dialog ref="alertRuleDialog" @success="handleDialogSuccess" />
  </div>
</template>

<script>
import { getInventoryAlertPage, getInventoryAlertStatistics, handleInventoryAlert, ignoreInventoryAlert, batchProcessInventoryAlert } from '@/api/financialSharing/inventory'
import AlertRuleDialog from './components/AlertRuleDialog.vue'

export default {
  name: 'InventoryAlert',
  components: {
    AlertRuleDialog
  },
  data() {
    return {
      loading: false,
      tableData: [],
      multipleSelection: [],
      searchForm: {
        alertType: '',
        status: '',
        inventoryName: ''
      },
      pagination: {
        currentPage: 1,
        pageSize: 15,
        total: 0
      },
      stats: {
        pendingCount: 0,
        lowStockCount: 0,
        highStockCount: 0,
        slowMovingCount: 0
      }
    }
  },
  mounted() {
    this.loadData()
    this.loadStats()
  },
  methods: {
    async loadData() {
      this.loading = true
      try {
        const params = {
          pageNum: this.pagination.currentPage,
          pageSize: this.pagination.pageSize,
          ...this.searchForm
        }
        const response = await getInventoryAlertPage(params)
        if (response.code === 1 && response.data) {
          this.tableData = response.data.tlist || []
          this.pagination.total = response.data.totalRecord || 0
        } else {
          this.$message.error(response.msg || '查询失败')
        }
      } catch (error) {
        console.error('查询失败：', error)
        this.$message.error('查询失败：' + (error.message || '未知错误'))
      } finally {
        this.loading = false
      }
    },

    async loadStats() {
      try {
        const response = await getInventoryAlertStatistics({})
        if (response.code === 1 && response.data) {
          this.stats = {
            pendingCount: response.data.pendingCount || 0,
            lowStockCount: response.data.lowStockCount || 0,
            highStockCount: response.data.highStockCount || 0,
            slowMovingCount: response.data.slowMovingCount || 0
          }
        }
      } catch (error) {
        console.error('加载统计数据失败：', error)
      }
    },

    handleSearch() {
      this.pagination.currentPage = 1
      this.loadData()
    },

    handleReset() {
      this.$refs.searchForm.resetFields()
      this.handleSearch()
    },

    handleCreateRule() {
      this.$refs.alertRuleDialog.open()
    },

    handleDialogSuccess() {
      this.loadData()
      this.loadStats()
    },

    handleView(row) {
      const content = `<p><b>预警编号：</b>${row.recordId || '-'}</p><p><b>存货名称：</b>${row.inventoryName || '-'}</p><p><b>预警类型：</b>${row.alertTypeName || row.alertType || '-'}</p><p><b>当前库存：</b>${row.currentQuantity || 0}</p><p><b>预警阈值：</b>${row.threshold || 0}</p><p><b>预警时间：</b>${row.alertTime || '-'}</p><p><b>状态：</b>${row.statusName || '-'}</p>`
      this.$alert(content, '预警详情', { dangerouslyUseHTMLString: true })
    },

    async handleProcess(row) {
      try {
        const { value: remark } = await this.$prompt('请输入处理备注', '处理预警', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          inputType: 'textarea'
        })

        const response = await handleInventoryAlert(row.recordId, {
          processStatus: 2,
          processRemark: remark
        })

        if (response.code === 1) {
          this.$message.success('处理成功')
          this.loadData()
          this.loadStats()
        } else {
          this.$message.error(response.msg || '处理失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('处理预警失败：', error)
          this.$message.error('处理失败：' + (error.message || '未知错误'))
        }
      }
    },

    async handleIgnore(row) {
      try {
        await this.$confirm('确定要忽略此预警吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })

        const response = await ignoreInventoryAlert(row.recordId)

        if (response.code === 1) {
          this.$message.success('操作成功')
          this.loadData()
          this.loadStats()
        } else {
          this.$message.error(response.msg || '操作失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('忽略预警失败：', error)
          this.$message.error('操作失败：' + (error.message || '未知错误'))
        }
      }
    },

    async handleBatchProcess() {
      if (!this.multipleSelection.length) {
        this.$message.warning('请先选择要处理的预警')
        return
      }

      try {
        const { value: remark } = await this.$prompt('请输入处理备注', '批量处理预警', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          inputType: 'textarea'
        })

        const alertIds = this.multipleSelection.map(item => item.recordId)
        const response = await batchProcessInventoryAlert({
          alertIds: alertIds,
          processStatus: 2,
          processRemark: remark
        })

        if (response.code === 1) {
          this.$message.success('批量处理成功')
          this.loadData()
          this.loadStats()
        } else {
          this.$message.error(response.msg || '批量处理失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('批量处理失败：', error)
          this.$message.error('批量处理失败：' + (error.message || '未知错误'))
        }
      }
    },

    handleRefreshAlert() {
      this.loadData()
      this.loadStats()
      this.$message.success('刷新成功')
    },

    handleSelectionChange(selection) {
      this.multipleSelection = selection
    },

    handleSizeChange(size) {
      this.pagination.pageSize = size
      this.loadData()
    },

    handleCurrentChange(page) {
      this.pagination.currentPage = page
      this.loadData()
    },

    getAlertTypeName(type) {
      const nameMap = {
        'LOW_STOCK': '库存不足',
        'HIGH_STOCK': '库存过量',
        'SLOW_MOVING': '呆滞库存',
        'EXPIRY': '过期预警',
        'LOWER_LIMIT': '库存不足',
        'UPPER_LIMIT': '库存过量',
        'STAGNANT': '呆滞库存',
        'EXPIRED': '过期预警'
      }
      return nameMap[type] || type
    },

    getAlertTypeTagType(type) {
      const typeMap = {
        'LOW_STOCK': 'danger',
        'HIGH_STOCK': 'warning',
        'SLOW_MOVING': 'info',
        'EXPIRY': 'primary',
        'LOWER_LIMIT': 'danger',
        'UPPER_LIMIT': 'warning',
        'STAGNANT': 'info',
        'EXPIRED': 'primary'
      }
      return typeMap[type] || 'default'
    },

    getAlertLevelTagType(level) {
      const typeMap = {
        1: 'info',
        2: 'warning',
        3: 'danger'
      }
      return typeMap[level] || 'default'
    },

    getAlertLevelText(level) {
      const textMap = {
        1: '低',
        2: '中',
        3: '高'
      }
      return textMap[level] || level
    },

    getStatusTagType(status) {
      const typeMap = {
        'PENDING': 'warning',
        'PROCESSING': 'primary',
        'PROCESSED': 'success',
        'IGNORED': 'info'
      }
      return typeMap[status] || 'default'
    },

    getStatusText(status) {
      const textMap = {
        'PENDING': '待处理',
        'PROCESSING': '处理中',
        'PROCESSED': '已处理',
        'IGNORED': '已忽略'
      }
      return textMap[status] || status
    }
  }
}
</script>

<style lang="scss" scoped>
.inventory-alert-container {
  padding: 20px;
}

.page-header {
  margin-bottom: 20px;
  
  h2 {
    margin: 0 0 8px 0;
    color: #303133;
  }
  
  p {
    margin: 0;
    color: #606266;
    font-size: 14px;
  }
}

.search-container {
  background: white;
  padding: 20px;
  border-radius: 4px;
  margin-bottom: 20px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.stats-cards {
  margin-bottom: 20px;

  .stat-card {
    background: white;
    border-radius: 8px;
    padding: 20px;
    display: flex;
    align-items: center;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);

    .stat-icon {
      width: 50px;
      height: 50px;
      border-radius: 8px;
      display: flex;
      align-items: center;
      justify-content: center;
      margin-right: 15px;

      i {
        font-size: 24px;
        color: white;
      }

      &.pending {
        background: linear-gradient(135deg, #ffa726 0%, #ff7043 100%);
      }

      &.low-stock {
        background: linear-gradient(135deg, #ef5350 0%, #e53935 100%);
      }

      &.high-stock {
        background: linear-gradient(135deg, #42a5f5 0%, #1e88e5 100%);
      }

      &.slow-moving {
        background: linear-gradient(135deg, #909399 0%, #606266 100%);
      }
    }

    .stat-content {
      .stat-value {
        font-size: 24px;
        font-weight: 600;
        color: #303133;
        margin-bottom: 4px;
      }

      .stat-label {
        font-size: 14px;
        color: #909399;
      }
    }
  }
}

.toolbar {
  margin-bottom: 20px;
}

.table-container {
  background: white;
  padding: 20px;
  border-radius: 4px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.pagination-container {
  margin-top: 20px;
  text-align: right;
}

.warning-text {
  color: #e6a23c;
}
</style>
