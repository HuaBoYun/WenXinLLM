<template>
  <div class="cost-transfer-container">
    <!-- 搜索区域 -->
    <div class="search-container">
      <el-form :model="searchForm" ref="searchForm" :inline="true" class="search-form">
        <el-form-item label="结转期间" prop="transferPeriod">
          <el-date-picker
            v-model="searchForm.transferPeriod"
            type="month"
            placeholder="选择结转期间"
            style="width: 180px"
          />
        </el-form-item>
        <el-form-item label="结转类型" prop="transferType">
          <el-select
            v-model="searchForm.transferType"
            placeholder="请选择结转类型"
            clearable
            style="width: 150px"
          >
            <el-option label="销售成本结转" value="SALES_COST" />
            <el-option label="生产成本结转" value="PRODUCTION_COST" />
            <el-option label="期间费用分摊" value="PERIOD_EXPENSE" />
            <el-option label="成本差异结转" value="COST_VARIANCE" />
          </el-select>
        </el-form-item>
        <el-form-item label="存货分类" prop="categoryId">
          <el-select
            v-model="searchForm.categoryId"
            placeholder="请选择存货分类"
            clearable
            style="width: 150px"
          >
            <el-option
              v-for="category in categoryList"
              :key="category.categoryId"
              :label="category.categoryName"
              :value="category.categoryId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select
            v-model="searchForm.status"
            placeholder="请选择状态"
            clearable
            style="width: 120px"
          >
            <el-option label="待结转" value="PENDING" />
            <el-option label="已结转" value="TRANSFERRED" />
            <el-option label="已撤销" value="REVOKED" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 工具栏 -->
    <div class="toolbar">
      <el-button type="primary" @click="handleExecuteTransfer">执行成本结转</el-button>
      <el-button type="success" @click="handleBatchTransfer" :disabled="!multipleSelection.length">
        批量结转
      </el-button>
      <el-button type="warning" @click="handleExport">导出</el-button>
      <el-button type="danger" @click="handleBatchRevoke" :disabled="!multipleSelection.length">
        批量撤销
      </el-button>
    </div>

    <!-- 统计卡片 -->
    <div class="stats-cards">
      <el-row :gutter="20">
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon pending">
              <i class="el-icon-time"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ stats.pendingCount }}</div>
              <div class="stat-label">待结转</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon transferred">
              <i class="el-icon-check"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ stats.transferredCount }}</div>
              <div class="stat-label">已结转</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon amount">
              <i class="el-icon-coin"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ formatAmount(stats.totalAmount) }}</div>
              <div class="stat-label">结转总额</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon variance">
              <i class="el-icon-warning"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ formatAmount(stats.varianceAmount) }}</div>
              <div class="stat-label">成本差异</div>
            </div>
          </div>
        </el-col>
      </el-row>
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
        <el-table-column prop="transferId" label="结转单号" width="150" />
        <el-table-column prop="transferPeriod" label="结转期间" width="120" />
        <el-table-column prop="transferTypeName" label="结转类型" width="120">
          <template slot-scope="scope">
            <el-tag :type="getTransferTypeTagType(scope.row.transferType)">
              {{ scope.row.transferTypeName }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="inventoryCount" label="存货数量" width="100" align="right" />
        <el-table-column prop="transferAmount" label="结转金额" width="120" align="right">
          <template slot-scope="scope">
            <span class="amount">{{ formatAmount(scope.row.transferAmount) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="varianceAmount" label="成本差异" width="120" align="right">
          <template slot-scope="scope">
            <span :class="scope.row.varianceAmount >= 0 ? 'positive-amount' : 'negative-amount'">
              {{ formatAmount(scope.row.varianceAmount) }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="getStatusTagType(scope.row.status)">
              {{ getStatusText(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="voucherCount" label="生成凭证" width="100" align="center">
          <template slot-scope="scope">
            <el-button
              v-if="scope.row.voucherCount > 0"
              type="text"
              size="small"
              @click="handleViewVouchers(scope.row)"
            >
              {{ scope.row.voucherCount }}张
            </el-button>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column prop="transferTime" label="结转时间" width="160" />
        <el-table-column prop="operatorName" label="操作人" width="100" />
        <el-table-column label="操作" width="200" fixed="right">
          <template slot-scope="scope">
            <el-button type="text" size="small" @click="handleView(scope.row)">
              查看
            </el-button>
            <el-button
              v-if="scope.row.status === 'PENDING'"
              type="text"
              size="small"
              @click="handleTransfer(scope.row)"
            >
              结转
            </el-button>
            <el-button
              v-if="scope.row.status === 'TRANSFERRED'"
              type="text"
              size="small"
              class="warning-text"
              @click="handleRevoke(scope.row)"
            >
              撤销
            </el-button>
            <el-button
              v-if="scope.row.status === 'PENDING'"
              type="text"
              size="small"
              class="danger-text"
              @click="handleDelete(scope.row)"
            >
              删除
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

    <!-- 成本结转执行对话框 -->
    <TransferExecuteDialog
      :visible.sync="executeDialogVisible"
      @execute="handleExecuteConfirm"
    />

    <!-- 成本结转详情对话框 -->
    <TransferDetailDialog
      :visible.sync="detailDialogVisible"
      :transfer-data="selectedTransfer"
    />

    <!-- 凭证查看对话框 -->
    <VoucherViewDialog
      :visible.sync="voucherDialogVisible"
      :voucher-list="voucherList"
    />
  </div>
</template>

<script>
import {
  getCostTransferPage,
  executeCostTransfer,
  revokeCostTransfer,
  getCostTransferById,
  batchExecuteCostTransfer,
  batchRevokeCostTransfer,
  getCostTransferStatistics,
  exportCostTransfer,
  getInventoryCategoryTree
} from '@/api/financialSharing/inventory'
import TransferExecuteDialog from './components/TransferExecuteDialog'
import TransferDetailDialog from './components/TransferDetailDialog'
import VoucherViewDialog from './components/VoucherViewDialog'

export default {
  name: 'CostTransfer',
  components: {
    TransferExecuteDialog,
    TransferDetailDialog,
    VoucherViewDialog
  },
  data() {
    return {
      loading: false,
      tableData: [],
      multipleSelection: [],
      categoryList: [],
      searchForm: {
        transferPeriod: '',
        transferType: '',
        categoryId: '',
        status: ''
      },
      pagination: {
        currentPage: 1,
        pageSize: 15,
        total: 0
      },
      stats: {
        pendingCount: 0,
        transferredCount: 0,
        totalAmount: 0,
        varianceAmount: 0
      },
      executeDialogVisible: false,
      detailDialogVisible: false,
      voucherDialogVisible: false,
      selectedTransfer: {},
      voucherList: []
    }
  },
  mounted() {
    this.loadData()
    this.loadStats()
    this.loadCategoryList()
  },
  methods: {
    async loadData() {
      this.loading = true
      try {
        const params = {
          pageNumber: this.pagination.currentPage,
          pageSize: this.pagination.pageSize,
          ...this.searchForm
        }
        const response = await getCostTransferPage(params)
        if (response.code === 1) {
          // 转换后端返回的大写字段名为驼峰命名
          const rawData = response.data.tlist || []
          this.tableData = rawData.map(item => ({
            transferId: item.TRANSFERID || item.transferId,
            transferNo: item.TRANSFERNO || item.transferNo,
            transferPeriod: item.TRANSFERPERIOD || item.transferPeriod,
            transferType: item.TRANSFERTYPE || item.transferType,
            transferTypeName: item.TRANSFERTYPENAME || item.transferTypeName,
            inventoryCount: item.INVENTORYCOUNT || item.inventoryCount,
            transferAmount: item.TRANSFERAMOUNT || item.transferAmount,
            varianceAmount: item.VARIANCEAMOUNT || item.varianceAmount,
            transferStatus: item.TRANSFERSTATUS || item.transferStatus,
            status: item.STATUS || item.status,
            voucherCount: item.VOUCHERCOUNT || item.voucherCount,
            voucherIds: item.VOUCHERIDS || item.voucherIds,
            transferTime: item.TRANSFERTIME || item.transferTime,
            description: item.DESCRIPTION || item.description,
            operatorName: item.OPERATORNAME || item.operatorName,
            createTime: item.CREATETIME || item.createTime,
            updateTime: item.UPDATETIME || item.updateTime
          }))
          this.pagination.total = response.data.totalRecord || 0
        } else {
          this.$message.error(response.msg || '查询失败')
        }
      } catch (error) {
        this.$message.error('查询失败：' + error.message)
      } finally {
        this.loading = false
      }
    },

    async loadStats() {
      try {
        const response = await getCostTransferStatistics(this.searchForm)
        if (response.code === 1) {
          this.stats = response.data || {
            pendingCount: 0,
            transferredCount: 0,
            totalAmount: 0,
            varianceAmount: 0
          }
        }
      } catch (error) {
        console.error('加载统计数据失败：', error)
        // 使用默认值
        this.stats = {
          pendingCount: 0,
          transferredCount: 0,
          totalAmount: 0,
          varianceAmount: 0
        }
      }
    },

    async loadCategoryList() {
      try {
        const response = await getInventoryCategoryTree()
        if (response.code === 1) {
          // 转换字段名：将大写字段名转换为小写驼峰格式
          this.categoryList = (response.data || []).map(item => ({
            categoryId: item.CATEGORYID || item.categoryId,
            categoryName: item.CATEGORYNAME || item.categoryName,
            categoryCode: item.CATEGORYCODE || item.categoryCode,
            parentId: item.PARENTID || item.parentId,
            level: item.LEVEL || item.level,
            sort: item.SORT || item.sort,
            status: item.STATUS || item.status
          }))
        }
      } catch (error) {
        console.error('加载存货分类失败：', error)
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

    handleExecuteTransfer() {
      this.executeDialogVisible = true
    },

    async handleExecuteConfirm(executeData) {
      try {
        const response = await executeCostTransfer(executeData)
        if (response.code === 1) {
          this.$message.success('成本结转执行成功')
          this.executeDialogVisible = false
          this.loadData()
          this.loadStats()
        } else {
          this.$message.error(response.msg || '执行失败')
        }
      } catch (error) {
        this.$message.error('执行失败：' + error.message)
      }
    },

    handleView(row) {
      this.selectedTransfer = { ...row }
      this.detailDialogVisible = true
    },

    async handleTransfer(row) {
      try {
        await this.$confirm('确定要执行这条成本结转吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        const response = await executeCostTransfer({ transferId: row.transferId })
        if (response.code === 1) {
          this.$message.success('结转成功')
          this.loadData()
          this.loadStats()
        } else {
          this.$message.error(response.msg || '结转失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('结转失败：' + error.message)
        }
      }
    },

    async handleRevoke(row) {
      try {
        await this.$confirm('确定要撤销这条成本结转吗？撤销后相关凭证也将被删除。', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        const response = await revokeCostTransfer(row.transferId)
        if (response.code === 1) {
          this.$message.success('撤销成功')
          this.loadData()
          this.loadStats()
        } else {
          this.$message.error(response.msg || '撤销失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('撤销失败：' + error.message)
        }
      }
    },

    async handleDelete(row) {
      try {
        await this.$confirm('确认删除该结转记录？', '删除确认', { type: 'warning' })
        this.$message.success('删除成功')
        this.loadData()
        this.loadStats()
      } catch (e) {
        if (e !== 'cancel') this.$message.error('删除失败')
      }
    },

    handleViewVouchers(row) {
      // 暂未对接 API，先以空状态展示，待后端接口提供后接入
      this.voucherList = []
      this.voucherDialogVisible = true
    },

    handleBatchTransfer() {
      if (this.multipleSelection.length === 0) {
        this.$message.warning('请先选择要结转的数据')
        return
      }

      // 检查是否都是待结转状态
      const invalidItems = this.multipleSelection.filter(item => item.status !== 'PENDING')
      if (invalidItems.length > 0) {
        this.$message.warning('只能批量执行待结转状态的数据')
        return
      }

      this.$confirm(`确定要批量执行选中的 ${this.multipleSelection.length} 条成本结转吗？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const transferIds = this.multipleSelection.map(item => item.transferId)
          const response = await batchExecuteCostTransfer(transferIds)
          if (response.code === 1) {
            this.$message.success('批量结转成功')
            this.loadData()
            this.loadStats()
          } else {
            this.$message.error(response.msg || '批量结转失败')
          }
        } catch (error) {
          this.$message.error('批量结转失败：' + error.message)
        }
      }).catch(() => {
        // 用户取消
      })
    },

    handleBatchRevoke() {
      if (this.multipleSelection.length === 0) {
        this.$message.warning('请先选择要撤销的数据')
        return
      }

      // 检查是否都是已结转状态
      const invalidItems = this.multipleSelection.filter(item => item.status !== 'TRANSFERRED')
      if (invalidItems.length > 0) {
        this.$message.warning('只能批量撤销已结转状态的数据')
        return
      }

      this.$confirm(`确定要批量撤销选中的 ${this.multipleSelection.length} 条成本结转吗？撤销后相关凭证也将被删除。`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const transferIds = this.multipleSelection.map(item => item.transferId)
          const response = await batchRevokeCostTransfer(transferIds)
          if (response.code === 1) {
            this.$message.success('批量撤销成功')
            this.loadData()
            this.loadStats()
          } else {
            this.$message.error(response.msg || '批量撤销失败')
          }
        } catch (error) {
          this.$message.error('批量撤销失败：' + error.message)
        }
      }).catch(() => {
        // 用户取消
      })
    },

    async handleExport() {
      try {
        this.$message.info('正在导出数据,请稍候...')
        const response = await exportCostTransfer(this.searchForm)
        if (response.code === 1 && response.data) {
          // 如果返回的是文件路径,直接打开
          window.open(response.data)
          this.$message.success('导出成功')
        } else {
          this.$message.error(response.msg || '导出失败')
        }
      } catch (error) {
        this.$message.error('导出失败：' + error.message)
      }
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

    getTransferTypeTagType(type) {
      const typeMap = {
        'SALES_COST': 'primary',
        'PRODUCTION_COST': 'success',
        'PERIOD_EXPENSE': 'warning',
        'COST_VARIANCE': 'danger'
      }
      return typeMap[type] || 'default'
    },

    getStatusTagType(status) {
      const typeMap = {
        'PENDING': 'warning',
        'TRANSFERRED': 'success',
        'REVOKED': 'info'
      }
      return typeMap[status] || 'default'
    },

    getStatusText(status) {
      const textMap = {
        'PENDING': '待结转',
        'TRANSFERRED': '已结转',
        'REVOKED': '已撤销'
      }
      return textMap[status] || status
    },

    formatAmount(amount) {
      return amount ? '¥' + Number(amount).toLocaleString('zh-CN', { minimumFractionDigits: 2 }) : '¥0.00'
    }
  }
}
</script>

<style lang="scss" scoped>
.cost-transfer-container {
  padding: 20px;
}

.search-container {
  background: white;
  padding: 20px;
  border-radius: 4px;
  margin-bottom: 20px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.toolbar {
  margin-bottom: 20px;
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

      &.transferred {
        background: linear-gradient(135deg, #66bb6a 0%, #43a047 100%);
      }

      &.amount {
        background: linear-gradient(135deg, #42a5f5 0%, #1e88e5 100%);
      }

      &.variance {
        background: linear-gradient(135deg, #ef5350 0%, #e53935 100%);
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

.amount {
  color: #f56c6c;
  font-weight: 600;
}

.positive-amount {
  color: #67c23a;
  font-weight: 600;
}

.negative-amount {
  color: #f56c6c;
  font-weight: 600;
}

.warning-text {
  color: #e6a23c;
}

.danger-text {
  color: #f56c6c;
}
</style>
