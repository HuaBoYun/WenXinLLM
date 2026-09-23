<template>
  <div class="depreciation-container">
    <div class="page-header">
      <h2>折旧管理</h2>
      <p>管理固定资产折旧计提、调整、统计分析等功能</p>
    </div>
    
    <!-- 折旧统计卡片 -->
    <div class="stats-cards">
      <el-row :gutter="20">
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon monthly">
              <i class="el-icon-date"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ formatAmount(stats.monthlyDepreciation) }}</div>
              <div class="stat-label">本月折旧</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon annual">
              <i class="el-icon-s-data"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ formatAmount(stats.annualDepreciation) }}</div>
              <div class="stat-label">年度折旧</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon accumulated">
              <i class="el-icon-pie-chart"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ formatAmount(stats.accumulatedDepreciation) }}</div>
              <div class="stat-label">累计折旧</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon rate">
              <i class="el-icon-data-line"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ stats.depreciationRate }}%</div>
              <div class="stat-label">折旧率</div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 操作区域 -->
    <div class="operation-section">
      <el-tabs v-model="activeTab" @tab-click="handleTabClick">
        <el-tab-pane label="折旧计提" name="calculate">
          <div class="calculate-panel">
            <el-form :model="calculateForm" :inline="true" class="calculate-form">
              <el-form-item label="计提期间">
                <el-date-picker
                  v-model="calculateForm.period"
                  type="month"
                  placeholder="选择计提期间"
                  format="yyyy-MM"
                  value-format="yyyy-MM"
                  style="width: 200px"
                />
              </el-form-item>
              <el-form-item label="资产类别">
                <el-select v-model="calculateForm.categoryId" placeholder="全部类别" style="width: 150px">
                  <el-option label="全部类别" value="" />
                  <el-option label="房屋建筑物" value="1001" />
                  <el-option label="机器设备" value="1002" />
                  <el-option label="运输工具" value="1003" />
                  <el-option label="电子设备" value="1004" />
                </el-select>
              </el-form-item>
              <el-form-item label="计提方式">
                <el-radio-group v-model="calculateForm.calculateType">
                  <el-radio label="NORMAL">正常计提</el-radio>
                  <el-radio label="BATCH">批量计提</el-radio>
                  <el-radio label="MANUAL">手工调整</el-radio>
                </el-radio-group>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="handleCalculateDepreciation" :loading="calculating">
                  开始计提
                </el-button>
                <el-button type="success" @click="handlePreviewDepreciation">预览</el-button>
                <el-button type="warning" @click="handleExportDepreciation">导出</el-button>
              </el-form-item>
            </el-form>
          </div>
        </el-tab-pane>
        
        <el-tab-pane label="折旧明细" name="details">
          <div class="details-panel">
            <!-- 搜索条件 -->
            <div class="search-bar">
              <el-form :model="searchForm" :inline="true" size="small">
                <el-form-item label="资产编码">
                  <el-input v-model="searchForm.assetCode" placeholder="请输入资产编码" style="width: 150px" />
                </el-form-item>
                <el-form-item label="计提期间">
                  <el-date-picker
                    v-model="searchForm.periodRange"
                    type="monthrange"
                    range-separator="至"
                    start-placeholder="开始月份"
                    end-placeholder="结束月份"
                    format="yyyy-MM"
                    value-format="yyyy-MM"
                    style="width: 240px"
                  />
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" @click="handleSearchDetails">查询</el-button>
                  <el-button @click="handleResetSearch">重置</el-button>
                </el-form-item>
              </el-form>
            </div>

            <!-- 折旧明细表格 -->
            <div class="details-table">
              <el-table :data="detailsData" v-loading="detailsLoading" border>
                <el-table-column prop="assetCode" label="资产编码" width="120" />
                <el-table-column prop="assetName" label="资产名称" min-width="150" show-overflow-tooltip />
                <el-table-column prop="period" label="计提期间" width="100" />
                <el-table-column prop="depreciationMethod" label="折旧方法" width="120" />
                <el-table-column prop="monthlyDepreciation" label="月折旧额" width="120" align="right">
                  <template slot-scope="scope">
                    <span class="amount">{{ formatAmount(scope.row.monthlyDepreciation) }}</span>
                  </template>
                </el-table-column>
                <el-table-column prop="accumulatedDepreciation" label="累计折旧" width="120" align="right">
                  <template slot-scope="scope">
                    <span class="amount">{{ formatAmount(scope.row.accumulatedDepreciation) }}</span>
                  </template>
                </el-table-column>
                <el-table-column prop="netBookValue" label="净值" width="120" align="right">
                  <template slot-scope="scope">
                    <span class="amount net">{{ formatAmount(scope.row.netBookValue) }}</span>
                  </template>
                </el-table-column>
                <el-table-column prop="voucherNumber" label="凭证号" width="120" />
                <el-table-column prop="status" label="状态" width="80">
                  <template slot-scope="scope">
                    <el-tag :type="getDepreciationStatusTagType(scope.row.status)" size="small">
                      {{ getDepreciationStatusText(scope.row.status) }}
                    </el-tag>
                  </template>
                </el-table-column>
                <el-table-column label="操作" width="150" fixed="right">
                  <template slot-scope="scope">
                    <el-button type="text" size="small" @click="handleViewDetail(scope.row)">
                      查看
                    </el-button>
                    <el-button
                      v-if="scope.row.status === 'CALCULATED'"
                      type="text"
                      size="small"
                      @click="handleAdjustDepreciation(scope.row)"
                    >
                      调整
                    </el-button>
                    <el-button
                      v-if="scope.row.status === 'POSTED'"
                      type="text"
                      size="small"
                      @click="handleViewVoucher(scope.row)"
                    >
                      凭证
                    </el-button>
                  </template>
                </el-table-column>
              </el-table>

              <!-- 分页 -->
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
          </div>
        </el-tab-pane>
        
        <el-tab-pane label="折旧统计" name="statistics">
          <div class="statistics-panel">
            <div class="chart-container">
              <div class="chart-item">
                <h4>月度折旧趋势</h4>
                <div id="depreciationTrendChart" style="width: 100%; height: 300px;"></div>
              </div>
              <div class="chart-item">
                <h4>资产类别折旧分布</h4>
                <div id="categoryDistributionChart" style="width: 100%; height: 300px;"></div>
              </div>
            </div>
          </div>
        </el-tab-pane>
      </el-tabs>
    </div>

    <!-- 折旧预览对话框 -->
    <depreciation-preview-dialog
      :visible.sync="previewDialogVisible"
      :preview-data="previewData"
      :preview-list="previewList"
      @confirm="handleConfirmDepreciation"
    />

    <!-- 折旧调整对话框 -->
    <depreciation-adjust-dialog
      :visible.sync="adjustDialogVisible"
      :depreciation-data="currentDepreciation"
      @submit="handleAdjustSubmit"
    />

    <!-- 凭证查看对话框 -->
    <voucher-view-dialog
      :visible.sync="voucherDialogVisible"
      :voucher-id="currentVoucherId"
    />
  </div>
</template>

<script>
import depreciationChartsMixin from './depreciationChartsMixin'
import DepreciationPreviewDialog from './components/DepreciationPreviewDialog.vue'
import DepreciationAdjustDialog from './components/DepreciationAdjustDialog.vue'
import VoucherViewDialog from './components/VoucherViewDialog.vue'

export default {
  name: 'DepreciationManagement',
  components: {
    DepreciationPreviewDialog,
    DepreciationAdjustDialog,
    VoucherViewDialog
  },
  mixins: [depreciationChartsMixin],
  data() {
    return {
      activeTab: 'calculate',
      calculating: false,
      detailsLoading: false,
      calculateForm: {
        period: new Date().toISOString().slice(0, 7),
        categoryId: '',
        calculateType: 'NORMAL'
      },
      searchForm: {
        assetCode: '',
        periodRange: []
      },
      pagination: {
        currentPage: 1,
        pageSize: 20,
        total: 0
      },
      stats: {
        monthlyDepreciation: 0,
        annualDepreciation: 0,
        accumulatedDepreciation: 0,
        depreciationRate: 0
      },
      detailsData: [],
      previewDialogVisible: false,
      previewData: {},
      previewList: [],
      adjustDialogVisible: false,
      currentDepreciation: {},
      voucherDialogVisible: false,
      currentVoucherId: ''
    }
  },
  mounted() {
    this.loadStats()
    this.loadDetailsData()
    // 初始化图表
    this.$nextTick(() => {
      this.initDepreciationTrendChart()
      this.initCategoryDistributionChart()
    })
  },
  methods: {
    async loadStats() {
      try {
        const period = this.calculateForm.period || new Date().toISOString().slice(0, 7)
        const response = await this.$http.get('/cwgxAi/financial/fixed-assets/depreciation/stats', {
          params: { period }
        })

        // axios 拦截器已经处理了响应，response.data 就是后端返回的数据
        const data = response.data || {}

        // 后端返回的字段名是全大写，需要适配
        this.stats = {
          monthlyDepreciation: data.TOTALAMOUNT || data.totalAmount || 0,
          annualDepreciation: data.ANNUALAMOUNT || data.annualAmount || 0,
          accumulatedDepreciation: data.ACCUMULATEDAMOUNT || data.accumulatedAmount || 0,
          depreciationRate: data.DEPRECIATIONRATE || data.depreciationRate || 0
        }
      } catch (error) {
        console.error('加载统计数据失败：', error)
      }
    },

    async loadDetailsData() {
      this.detailsLoading = true
      try {
        const params = {
          assetCode: this.searchForm.assetCode,
          pageNum: this.pagination.currentPage,
          pageSize: this.pagination.pageSize
        }

        if (this.searchForm.periodRange && this.searchForm.periodRange.length === 2) {
          params.startPeriod = this.searchForm.periodRange[0]
          params.endPeriod = this.searchForm.periodRange[1]
        }

        const response = await this.$http.get('/cwgxAi/financial/fixed-assets/depreciation/details', {
          params
        })

        // axios 拦截器已经处理了响应，response.data 就是后端返回的数据
        const result = response.data || {}
        this.detailsData = result.tlist || []
        this.pagination.total = result.totalRecord || 0
      } catch (error) {
        console.error('加载折旧明细失败：', error)
        this.$message.error('加载折旧明细失败：' + error.message)
        this.detailsData = []
        this.pagination.total = 0
      } finally {
        this.detailsLoading = false
      }
    },

    handleTabClick(tab) {
      console.log('切换到标签页：', tab.name)
    },

    async handleCalculateDepreciation() {
      if (!this.calculateForm.period) {
        this.$message.warning('请选择计提期间')
        return
      }

      this.calculating = true
      try {
        const response = await this.$http.post('/cwgxAi/financial/fixed-assets/depreciation/calculate', null, {
          params: {
            period: this.calculateForm.period,
            categoryId: this.calculateForm.categoryId,
            calculateType: this.calculateForm.calculateType
          }
        })

        // axios 拦截器已经处理了响应，response.data 就是后端返回的数据
        const result = response.data || {}
        this.$message.success(`折旧计提完成！成功：${result.successCount}，跳过：${result.skipCount}`)
        this.loadStats()
        this.loadDetailsData()
      } catch (error) {
        console.error('折旧计提失败：', error)
        this.$message.error('折旧计提失败：' + error.message)
      } finally {
        this.calculating = false
      }
    },

    async handlePreviewDepreciation() {
      if (!this.calculateForm.period) {
        this.$message.warning('请选择计提期间')
        return
      }

      try {
        const response = await this.$http.get('/cwgxAi/financial/fixed-assets/depreciation/preview', {
          params: {
            period: this.calculateForm.period,
            categoryId: this.calculateForm.categoryId
          }
        })

        // axios 拦截器已经处理了响应，response.data 就是后端返回的数据
        const result = response.data || {}
        this.previewData = {
          period: this.calculateForm.period,
          assetCount: result.totalCount || 0,
          totalAmount: result.totalAmount || 0,
          calculateType: this.calculateForm.calculateType
        }
        this.previewList = result.list || []
        this.previewDialogVisible = true
      } catch (error) {
        console.error('预览失败：', error)
        this.$message.error('预览失败：' + error.message)
      }
    },

    async handleConfirmDepreciation() {
      try {
        // TODO: 调用API确认计提
        await new Promise(resolve => setTimeout(resolve, 1000))
        this.$message.success('折旧计提成功')
        this.previewDialogVisible = false
        this.loadStats()
        this.loadDetailsData()
      } catch (error) {
        this.$message.error('折旧计提失败：' + error.message)
      }
    },

    handleExportDepreciation() {
      try {
        const data = this.detailsData || []
        if (data.length === 0) {
          this.$message.warning('暂无数据可导出')
          return
        }
        const jsonStr = JSON.stringify(data, null, 2)
        const blob = new Blob([jsonStr], { type: 'application/json' })
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = '折旧数据导出.json'
        link.click()
        window.URL.revokeObjectURL(url)
        this.$message.success('导出成功')
      } catch (error) {
        this.$message.error('导出失败')
      }
    },

    handleSearchDetails() {
      this.pagination.currentPage = 1
      this.loadDetailsData()
    },

    handleResetSearch() {
      this.searchForm = {
        assetCode: '',
        periodRange: []
      }
      this.handleSearchDetails()
    },

    handleViewDetail(row) {
      const content = `<p><b>资产编号：</b>${row.assetCode || '-'}</p><p><b>资产名称：</b>${row.assetName || '-'}</p><p><b>折旧金额：</b>${row.depreciationAmount || 0}</p><p><b>累计折旧：</b>${row.accumulatedDepreciation || 0}</p><p><b>净值：</b>${row.netValue || 0}</p><p><b>折旧方法：</b>${row.depreciationMethod || '-'}</p><p><b>折旧期间：</b>${row.period || '-'}</p>`
      this.$alert(content, '折旧详情', { dangerouslyUseHTMLString: true })
    },

    handleAdjustDepreciation(row) {
      this.currentDepreciation = { ...row }
      this.adjustDialogVisible = true
    },

    async handleAdjustSubmit(formData) {
      try {
        // TODO: 调用API提交调整
        await new Promise(resolve => setTimeout(resolve, 1000))
        this.$message.success('折旧调整成功')
        this.adjustDialogVisible = false
        this.loadDetailsData()
      } catch (error) {
        this.$message.error('折旧调整失败：' + error.message)
      }
    },

    handleViewVoucher(row) {
      this.currentVoucherId = row.voucherNumber
      this.voucherDialogVisible = true
    },

    handleSizeChange(size) {
      this.pagination.pageSize = size
      this.loadDetailsData()
    },

    handleCurrentChange(page) {
      this.pagination.currentPage = page
      this.loadDetailsData()
    },

    formatAmount(amount) {
      return amount ? '¥' + Number(amount).toLocaleString('zh-CN', { minimumFractionDigits: 2 }) : '¥0.00'
    },

    getDepreciationStatusTagType(status) {
      const typeMap = {
        'CALCULATED': 'warning',
        'POSTED': 'success',
        'ADJUSTED': 'info'
      }
      return typeMap[status] || 'default'
    },

    getDepreciationStatusText(status) {
      const textMap = {
        'CALCULATED': '已计算',
        'POSTED': '已过账',
        'ADJUSTED': '已调整'
      }
      return textMap[status] || status
    }
  }
}
</script>

<style lang="scss" scoped>
.depreciation-container {
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

      &.monthly {
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      }

      &.annual {
        background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
      }

      &.accumulated {
        background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
      }

      &.rate {
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

.operation-section {
  background: white;
  border-radius: 4px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  padding: 20px;

  .calculate-panel {
    .calculate-form {
      background: #f5f7fa;
      padding: 20px;
      border-radius: 4px;
      margin-bottom: 20px;
    }
  }

  .details-panel {
    .search-bar {
      background: #f5f7fa;
      padding: 15px;
      border-radius: 4px;
      margin-bottom: 20px;
    }

    .pagination-container {
      margin-top: 20px;
      text-align: right;
    }
  }

  .statistics-panel {
    .chart-container {
      display: grid;
      grid-template-columns: 1fr 1fr;
      gap: 20px;

      .chart-item {
        h4 {
          margin: 0 0 15px 0;
          color: #303133;
          text-align: center;
        }
      }
    }
  }
}

.amount {
  font-weight: 600;
  color: #f56c6c;
  
  &.net {
    color: #67c23a;
  }
}
</style>
