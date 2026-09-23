<template>
  <div class="asset-cards-container">
    <div class="page-header">
      <h2>资产卡片管理</h2>
      <p>管理固定资产基础信息、折旧计算、变动记录等</p>
    </div>
    
    <!-- 统计卡片 -->
    <div class="stats-cards">
      <el-row :gutter="20">
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon total">
              <i class="el-icon-office-building"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ stats.totalAssets }}</div>
              <div class="stat-label">资产总数</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon value">
              <i class="el-icon-money"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ formatAmount(stats.totalValue) }}</div>
              <div class="stat-label">资产原值</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon depreciation">
              <i class="el-icon-bottom"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ formatAmount(stats.depreciationValue) }}</div>
              <div class="stat-label">累计折旧</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon net">
              <i class="el-icon-top"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ formatAmount(stats.netValue) }}</div>
              <div class="stat-label">净值</div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 搜索区域 -->
    <div class="search-container">
      <el-form :model="searchForm" ref="searchForm" :inline="true" class="search-form">
        <el-form-item label="资产编码" prop="assetCode">
          <el-input
            v-model="searchForm.assetCode"
            placeholder="请输入资产编码"
            clearable
            style="width: 180px"
          />
        </el-form-item>
        <el-form-item label="资产名称" prop="assetName">
          <el-input
            v-model="searchForm.assetName"
            placeholder="请输入资产名称"
            clearable
            style="width: 180px"
          />
        </el-form-item>
        <el-form-item label="资产类别" prop="categoryId">
          <el-select
            v-model="searchForm.categoryId"
            placeholder="请选择资产类别"
            clearable
            style="width: 150px"
          >
            <el-option label="房屋建筑物" value="1001" />
            <el-option label="机器设备" value="1002" />
            <el-option label="运输工具" value="1003" />
            <el-option label="电子设备" value="1004" />
            <el-option label="办公设备" value="1005" />
          </el-select>
        </el-form-item>
        <el-form-item label="资产状态" prop="status">
          <el-select
            v-model="searchForm.status"
            placeholder="请选择状态"
            clearable
            style="width: 120px"
          >
            <el-option label="正常" value="NORMAL" />
            <el-option label="闲置" value="IDLE" />
            <el-option label="维修" value="MAINTENANCE" />
            <el-option label="报废" value="SCRAPPED" />
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
      <el-button type="primary" @click="handleAdd">新增资产</el-button>
      <el-button type="success" @click="handleImport">批量导入</el-button>
      <el-button type="warning" @click="handleExport">导出</el-button>
      <el-button type="info" @click="handleDepreciation">计提折旧</el-button>
      <el-button type="danger" @click="handleBatchDelete" :disabled="!multipleSelection.length">
        批量删除
      </el-button>
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
        <el-table-column prop="assetCode" label="资产编码" width="120" />
        <el-table-column prop="assetName" label="资产名称" min-width="150" show-overflow-tooltip />
        <el-table-column prop="categoryName" label="资产类别" width="120" />
        <el-table-column prop="originalValue" label="资产原值" width="120" align="right">
          <template slot-scope="scope">
            <span class="amount">{{ formatAmount(scope.row.originalValue) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="accumulatedDepreciation" label="累计折旧" width="120" align="right">
          <template slot-scope="scope">
            <span class="amount depreciation">{{ formatAmount(scope.row.accumulatedDepreciation) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="netBookValue" label="净值" width="120" align="right">
          <template slot-scope="scope">
            <span class="amount net">{{ formatAmount(scope.row.netBookValue) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="depreciationMethod" label="折旧方法" width="100" />
        <el-table-column prop="status" label="状态" width="80">
          <template slot-scope="scope">
            <el-tag :type="getStatusTagType(scope.row.status)">
              {{ getStatusText(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="responsiblePerson" label="责任人" width="100" />
        <el-table-column prop="purchaseDate" label="购置日期" width="120" />
        <el-table-column label="操作" width="200" fixed="right">
          <template slot-scope="scope">
            <el-button type="text" size="small" @click="handleView(scope.row)">
              查看
            </el-button>
            <el-button type="text" size="small" @click="handleEdit(scope.row)">
              编辑
            </el-button>
            <el-button type="text" size="small" @click="handleDepreciationSchedule(scope.row)">
              折旧计划
            </el-button>
            <el-button
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

    <!-- 资产表单对话框 -->
    <asset-form-dialog
      :visible.sync="formDialogVisible"
      :mode="formMode"
      :asset-data="currentAsset"
      @submit="handleFormSubmit"
    />

    <!-- 折旧计划对话框 -->
    <depreciation-schedule-dialog
      :visible.sync="scheduleDialogVisible"
      :asset-info="currentAsset"
    />

    <!-- 批量导入对话框 -->
    <import-dialog
      :visible.sync="importDialogVisible"
      @success="handleImportSuccess"
    />
  </div>
</template>

<script>
import {
  getAssetCardList,
  getAssetCardById,
  createAssetCard,
  updateAssetCard,
  deleteAssetCard,
  batchDeleteAssetCard,
  getAssetSummary
} from '@/api/financialSharing/fixedAssetCard'
import AssetFormDialog from './components/AssetFormDialog.vue'
import DepreciationScheduleDialog from './components/DepreciationScheduleDialog.vue'
import ImportDialog from './components/ImportDialog.vue'

export default {
  name: 'AssetCards',
  components: {
    AssetFormDialog,
    DepreciationScheduleDialog,
    ImportDialog
  },
  data() {
    return {
      loading: false,
      tableData: [],
      multipleSelection: [],
      searchForm: {
        assetCode: '',
        assetName: '',
        categoryId: '',
        status: ''
      },
      pagination: {
        currentPage: 1,
        pageSize: 15,
        total: 0
      },
      stats: {
        totalAssets: 0,
        totalValue: 0,
        depreciationValue: 0,
        netValue: 0
      },
      formDialogVisible: false,
      formMode: 'add',
      currentAsset: {},
      scheduleDialogVisible: false,
      importDialogVisible: false
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
          ...this.searchForm,
          pageNumber: this.pagination.currentPage,
          pageSize: this.pagination.pageSize
        }
        const response = await getAssetCardList(params)
        if (response.code === 1) {
          this.tableData = response.data.tlist || []
          this.pagination.total = response.data.totalRecord || 0
        } else {
          this.$message.error(response.msg || '查询失败')
        }
      } catch (error) {
        console.error('查询失败：', error)
        this.$message.error('查询失败：' + error.message)
      } finally {
        this.loading = false
      }
    },

    async loadStats() {
      try {
        const response = await getAssetSummary()
        if (response.code === 1 && response.data) {
          this.stats = {
            totalAssets: response.data.totalCount || 0,
            totalValue: response.data.totalOriginalValue || 0,
            depreciationValue: response.data.totalAccumulatedDepreciation || 0,
            netValue: response.data.totalNetValue || 0
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

    handleAdd() {
      this.formMode = 'add'
      this.currentAsset = {}
      this.formDialogVisible = true
    },

    handleView(row) {
      this.formMode = 'view'
      this.currentAsset = { ...row }
      this.formDialogVisible = true
    },

    handleEdit(row) {
      this.formMode = 'edit'
      this.currentAsset = { ...row }
      this.formDialogVisible = true
    },

    async handleFormSubmit(formData) {
      try {
        let response
        if (this.formMode === 'add') {
          response = await createAssetCard(formData)
        } else {
          response = await updateAssetCard(this.currentAsset.assetId, formData)
        }

        if (response.code === 1) {
          this.$message.success(this.formMode === 'add' ? '新增成功' : '更新成功')
          this.formDialogVisible = false
          this.loadData()
          this.loadStats()
        } else {
          this.$message.error(response.msg || '操作失败')
        }
      } catch (error) {
        console.error('操作失败：', error)
        this.$message.error('操作失败：' + error.message)
      }
    },

    async handleDelete(row) {
      try {
        await this.$confirm('确定要删除该资产吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })

        const response = await deleteAssetCard(row.assetId)
        if (response.code === 1) {
          this.$message.success('删除成功')
          this.loadData()
          this.loadStats()
        } else {
          this.$message.error(response.msg || '删除失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('删除失败：', error)
          this.$message.error('删除失败：' + error.message)
        }
      }
    },

    handleImport() {
      this.importDialogVisible = true
    },

    handleImportSuccess() {
      this.loadData()
      this.loadStats()
    },

    handleExport() {
      try {
        const data = this.tableData || []
        if (data.length === 0) {
          this.$message.warning('暂无数据可导出')
          return
        }
        const jsonStr = JSON.stringify(data, null, 2)
        const blob = new Blob([jsonStr], { type: 'application/json' })
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = '资产卡片导出.json'
        link.click()
        window.URL.revokeObjectURL(url)
        this.$message.success('导出成功')
      } catch (error) {
        this.$message.error('导出失败')
      }
    },

    handleDepreciation() {
      this.$router.push('/gdzc/depreciation')
    },

    handleDepreciationSchedule(row) {
      this.currentAsset = { ...row }
      this.scheduleDialogVisible = true
    },

    async handleBatchDelete() {
      if (!this.multipleSelection.length) {
        this.$message.warning('请选择要删除的资产')
        return
      }

      try {
        await this.$confirm(`确定要删除选中的 ${this.multipleSelection.length} 条资产吗？`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })

        const assetIds = this.multipleSelection.map(item => item.assetId)
        const response = await batchDeleteAssetCard(assetIds)
        if (response.code === 1) {
          this.$message.success('批量删除成功')
          this.loadData()
          this.loadStats()
        } else {
          this.$message.error(response.msg || '批量删除失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('批量删除失败：', error)
          this.$message.error('批量删除失败：' + error.message)
        }
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

    formatAmount(amount) {
      return amount ? '¥' + Number(amount).toLocaleString('zh-CN', { minimumFractionDigits: 2 }) : '¥0.00'
    },

    getStatusTagType(status) {
      const typeMap = {
        'NORMAL': 'success',
        'IDLE': 'warning',
        'MAINTENANCE': 'info',
        'SCRAPPED': 'danger'
      }
      return typeMap[status] || 'default'
    },

    getStatusText(status) {
      const textMap = {
        'NORMAL': '正常',
        'IDLE': '闲置',
        'MAINTENANCE': '维修',
        'SCRAPPED': '报废'
      }
      return textMap[status] || status
    }
  }
}
</script>

<style lang="scss" scoped>
.asset-cards-container {
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

      &.total {
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      }

      &.value {
        background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
      }

      &.depreciation {
        background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
      }

      &.net {
        background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
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
  font-weight: 600;
  
  &.depreciation {
    color: #f56c6c;
  }
  
  &.net {
    color: #67c23a;
  }
}

.danger-text {
  color: #f56c6c;
}
</style>
