<template>
  <div class="valuation-management-wrapper">
    <el-card shadow="never">
      <div slot="header" class="card-header">
        <span>估值管理</span>
        <el-button type="primary" size="small" icon="el-icon-refresh" @click="refreshData">刷新</el-button>
      </div>

      <!-- 估值操作 -->
      <el-row :gutter="20" class="valuation-actions">
        <el-col :span="8">
          <el-button type="primary" size="medium" icon="el-icon-s-finance" class="action-btn" :loading="batchLoading" @click="handleBatchValuation">批量估值（全部持仓）</el-button>
        </el-col>
        <el-col :span="8">
          <el-button type="success" size="medium" icon="el-icon-edit-outline" class="action-btn" @click="handleSingleValuation">单笔估值</el-button>
        </el-col>
        <el-col :span="8">
          <el-button type="warning" size="medium" icon="el-icon-s-data" class="action-btn" :loading="reportLoading" @click="handleValuationReport">估值报告（下载CSV）</el-button>
        </el-col>
      </el-row>

      <!-- 估值结果 -->
      <el-tabs v-model="activeTab" class="valuation-tabs" @tab-click="handleTabClick">
        <el-tab-pane label="持仓估值" name="position">
          <el-table :data="valuationData" border stripe v-loading="loading">
            <el-table-column type="selection" width="55" align="center" />
            <el-table-column prop="contractCode" label="合约编号" width="150" show-overflow-tooltip />
            <el-table-column prop="productType" label="产品类型" width="100" align="center">
              <template slot-scope="scope">
                <el-tag :type="getProductTypeTag(scope.row.productType)" size="small">
                  {{ getProductTypeText(scope.row.productType) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="underlyingAsset" label="标的资产" width="120" />
            <el-table-column prop="notionalAmount" label="名义本金" width="120" align="right">
              <template slot-scope="scope">
                <span class="amount-text">{{ formatAmount(scope.row.notionalAmount) }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="marketValue" label="市场价值" width="120" align="right">
              <template slot-scope="scope">
                <span class="value-text">{{ formatAmount(scope.row.marketValue) }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="presentValue" label="现值(PV)" width="120" align="right">
              <template slot-scope="scope">
                <span class="pv-text">{{ formatAmount(scope.row.presentValue) }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="unrealizedPnL" label="未实现损益" width="120" align="right">
              <template slot-scope="scope">
                <span :class="getPnLClass(scope.row.unrealizedPnL)">
                  {{ formatAmount(scope.row.unrealizedPnL) }}
                </span>
              </template>
            </el-table-column>
            <el-table-column prop="valuationDate" label="估值日期" width="110" align="center" />
            <el-table-column prop="valuationMethod" label="估值方法" width="120" align="center" />
            <el-table-column label="操作" align="center" width="150" fixed="right">
              <template slot-scope="scope">
                <el-button size="mini" type="text" @click="handleRevaluate(scope.row)">重新估值</el-button>
                <el-button size="mini" type="text" @click="handleViewDetail(scope.row)">详情</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>

        <el-tab-pane label="估值汇总" name="summary">
          <el-row :gutter="20">
            <el-col :span="6">
              <el-card class="summary-card">
                <div class="summary-item">
                  <div class="summary-title">总持仓价值</div>
                  <div class="summary-value">{{ formatAmount(summaryData.totalValue) }}</div>
                </div>
              </el-card>
            </el-col>
            <el-col :span="6">
              <el-card class="summary-card">
                <div class="summary-item">
                  <div class="summary-title">总未实现损益</div>
                  <div class="summary-value" :class="getPnLClass(summaryData.totalUnrealizedPnL)">
                    {{ formatAmount(summaryData.totalUnrealizedPnL) }}
                  </div>
                </div>
              </el-card>
            </el-col>
            <el-col :span="6">
              <el-card class="summary-card">
                <div class="summary-item">
                  <div class="summary-title">远期合约</div>
                  <div class="summary-value">{{ formatAmount(summaryData.forwardValue) }}</div>
                </div>
              </el-card>
            </el-col>
            <el-col :span="6">
              <el-card class="summary-card">
                <div class="summary-item">
                  <div class="summary-title">期权合约</div>
                  <div class="summary-value">{{ formatAmount(summaryData.optionValue) }}</div>
                </div>
              </el-card>
            </el-col>
          </el-row>

          <!-- 估值分布图表 -->
          <el-card class="chart-card" style="margin-top: 20px">
            <div slot="header"><span>估值分布统计图</span></div>
            <div ref="pieChart" style="width: 100%; height: 320px;"></div>
          </el-card>
        </el-tab-pane>

        <el-tab-pane label="估值历史" name="history">
          <el-table :data="historyData" border stripe>
            <el-table-column prop="valuationDate" label="估值日期" width="120" align="center" />
            <el-table-column prop="productType" label="产品类型" width="100" align="center" />
            <el-table-column prop="totalValue" label="总价值" width="150" align="right">
              <template slot-scope="scope">
                <span>{{ formatAmount(scope.row.totalValue) }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="dailyChange" label="日变动" width="120" align="right">
              <template slot-scope="scope">
                <span :class="getChangeClass(scope.row.dailyChange)">
                  {{ formatAmount(scope.row.dailyChange) }}
                </span>
              </template>
            </el-table-column>
            <el-table-column prop="dailyChangeRate" label="日变动率(%)" width="130" align="right">
              <template slot-scope="scope">
                <span :class="getChangeClass(scope.row.dailyChangeRate)">
                  {{ formatPercent(scope.row.dailyChangeRate) }}
                </span>
              </template>
            </el-table-column>
            <el-table-column prop="remark" label="备注" min-width="200" show-overflow-tooltip />
          </el-table>
        </el-tab-pane>
      </el-tabs>
    </el-card>

    <!-- 单笔估值对话框 -->
    <el-dialog title="单笔估值" :visible.sync="singleValuationDialogVisible" width="560px">
      <el-form :model="valuationForm" label-width="100px">
        <el-form-item label="合约编号">
          <el-select v-model="valuationForm.contractCode" placeholder="请选择合约" style="width:100%">
            <el-option v-for="item in valuationData" :key="item.valuationId" :label="item.contractCode" :value="item.contractCode" />
          </el-select>
        </el-form-item>
        <el-form-item label="估值日期">
          <el-date-picker v-model="valuationForm.valuationDate" type="date" placeholder="选择估值日期" format="yyyy-MM-dd" value-format="yyyy-MM-dd" style="width:100%" />
        </el-form-item>
        <el-form-item label="估值方法">
          <el-select v-model="valuationForm.valuationMethod" style="width:100%">
            <el-option label="市场法" value="市场法" />
            <el-option label="收益法" value="收益法" />
            <el-option label="成本法" value="成本法" />
            <el-option label="Black-Scholes" value="Black-Scholes" />
            <el-option label="二叉树" value="二叉树" />
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button type="primary" :loading="singleLoading" @click="submitSingleValuation">开始估值</el-button>
        <el-button @click="singleValuationDialogVisible = false">取消</el-button>
      </div>
    </el-dialog>

    <!-- 详情弹窗 -->
    <el-dialog title="估值详情" :visible.sync="detailDialogVisible" width="600px">
      <el-descriptions :column="2" border v-if="detailRow">
        <el-descriptions-item label="合约编号">{{ detailRow.contractCode }}</el-descriptions-item>
        <el-descriptions-item label="产品类型">{{ getProductTypeText(detailRow.productType) }}</el-descriptions-item>
        <el-descriptions-item label="标的资产">{{ detailRow.underlyingAsset }}</el-descriptions-item>
        <el-descriptions-item label="币种">{{ detailRow.currency }}</el-descriptions-item>
        <el-descriptions-item label="名义本金">{{ formatAmount(detailRow.notionalAmount) }}</el-descriptions-item>
        <el-descriptions-item label="市场价值">{{ formatAmount(detailRow.marketValue) }}</el-descriptions-item>
        <el-descriptions-item label="现值(PV)">{{ formatAmount(detailRow.presentValue) }}</el-descriptions-item>
        <el-descriptions-item label="未实现损益">
          <span :class="getPnLClass(detailRow.unrealizedPnL)">{{ formatAmount(detailRow.unrealizedPnL) }}</span>
        </el-descriptions-item>
        <el-descriptions-item label="估值日期">{{ detailRow.valuationDate }}</el-descriptions-item>
        <el-descriptions-item label="估值方法">{{ detailRow.valuationMethod }}</el-descriptions-item>
      </el-descriptions>
      <div slot="footer">
        <el-button @click="detailDialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import {
  getValuationPositions,
  getValuationSummary,
  getValuationHistory,
  batchValuation,
  singleValuation,
  revaluate
} from '@/api/globalTreasurer/yspx'

export default {
  name: 'ValuationManagement',
  data() {
    return {
      loading: false,
      batchLoading: false,
      singleLoading: false,
      reportLoading: false,
      activeTab: 'position',
      singleValuationDialogVisible: false,
      detailDialogVisible: false,
      detailRow: null,
      valuationData: [],
      historyData: [],
      summaryData: { totalValue: 0, totalUnrealizedPnL: 0, forwardValue: 0, optionValue: 0 },
      valuationForm: { contractCode: '', valuationDate: null, valuationMethod: '市场法' },
      pieChart: null
    }
  },
  created() {
    this.loadValuationData()
    this.loadSummaryData()
    this.loadHistoryData()
  },
  methods: {
    refreshData() {
      this.loadValuationData()
      this.loadSummaryData()
      this.loadHistoryData()
      this.$message.success('数据已刷新')
    },
    async loadValuationData() {
      this.loading = true
      try {
        const res = await getValuationPositions()
        if (res && [1, '1'].includes(res.code)) {
          this.valuationData = (res.data && res.data.tlist) || []
        }
      } catch (e) {
        console.error('加载估值数据失败:', e)
      } finally {
        this.loading = false
      }
    },
    async loadSummaryData() {
      try {
        const res = await getValuationSummary()
        if (res && [1, '1'].includes(res.code)) {
          this.summaryData = res.data || {}
          this._pendingDistribution = res.data && res.data.distribution
          if (this.activeTab === 'summary') {
            this.$nextTick(() => this.renderPieChart(this._pendingDistribution))
          }
        }
      } catch (e) {
        console.error('加载汇总数据失败:', e)
      }
    },
    async loadHistoryData() {
      try {
        const res = await getValuationHistory()
        if (res && [1, '1'].includes(res.code)) {
          this.historyData = (res.data && res.data.tlist) || []
        }
      } catch (e) {
        console.error('加载历史数据失败:', e)
      }
    },
    renderPieChart(distribution) {
      if (!this.$refs.pieChart) return
      if (!this.pieChart) {
        this.pieChart = echarts.init(this.$refs.pieChart)
      }
      const data = (distribution || []).map(item => ({
        name: item.name,
        value: parseFloat(item.value) || 0
      }))
      this.pieChart.setOption({
        tooltip: { trigger: 'item', formatter: '{b}: {c} ({d}%)' },
        legend: { orient: 'vertical', right: '5%', top: 'center' },
        series: [{
          type: 'pie',
          radius: ['40%', '70%'],
          center: ['40%', '50%'],
          data,
          label: { formatter: '{b}\n{d}%' },
          emphasis: { itemStyle: { shadowBlur: 10, shadowOffsetX: 0, shadowColor: 'rgba(0,0,0,0.5)' } }
        }]
      })
    },
    async handleBatchValuation() {
      try {
        await this.$confirm('确认对所有持仓进行批量估值？', '提示', { type: 'warning' })
      } catch { return }
      this.batchLoading = true
      try {
        const res = await batchValuation()
        if (res && [1, '1'].includes(res.code)) {
          this.$message.success(res.msg || '批量估值完成')
          this.refreshData()
        } else {
          this.$message.error((res && res.msg) || '批量估值失败')
        }
      } catch (e) {
        this.$message.error('批量估值失败')
      } finally {
        this.batchLoading = false
      }
    },
    handleSingleValuation() {
      this.valuationForm = { contractCode: '', valuationDate: new Date().toISOString().slice(0, 10), valuationMethod: '市场法' }
      this.singleValuationDialogVisible = true
    },
    async submitSingleValuation() {
      if (!this.valuationForm.contractCode) {
        this.$message.warning('请选择合约编号')
        return
      }
      this.singleLoading = true
      try {
        const res = await singleValuation(this.valuationForm)
        if (res && [1, '1'].includes(res.code)) {
          this.$message.success('单笔估值完成')
          this.singleValuationDialogVisible = false
          this.refreshData()
        } else {
          this.$message.error((res && res.msg) || '估值失败')
        }
      } catch (e) {
        this.$message.error('估值失败')
      } finally {
        this.singleLoading = false
      }
    },
    async handleValuationReport() {
      this.reportLoading = true
      try {
        const token = this.$store.getters['user/token'] || this.$store.getters.token || ''
        const res = await fetch('/vab-mock-server/qqsk/derivatives/valuation/report', {
          method: 'GET',
          headers: { token, Authorization: `Bearer ${token}` }
        })
        if (!res.ok) { this.$message.error('报告生成失败，请重新登录后重试'); return }
        const blob = await res.blob()
        const url = URL.createObjectURL(blob)
        const a = document.createElement('a')
        a.href = url
        a.download = `估值报告_${new Date().toISOString().slice(0, 10)}.csv`
        document.body.appendChild(a)
        a.click()
        document.body.removeChild(a)
        URL.revokeObjectURL(url)
        this.$message.success('估值报告下载成功')
      } catch (e) {
        this.$message.error('估值报告下载失败')
      } finally {
        this.reportLoading = false
      }
    },
    handleTabClick(tab) {
      if (tab.name === 'summary') {
        this.$nextTick(() => {
          if (this.pieChart) {
            this.pieChart.resize()
          } else {
            this.renderPieChart(this._pendingDistribution)
          }
        })
      }
    },
    async handleRevaluate(row) {
      try {
        const res = await revaluate(row.valuationId)
        if (res && [1, '1'].includes(res.code)) {
          this.$message.success(`${row.contractCode} 重新估值成功`)
          const updated = res.data
          if (updated) {
            const idx = this.valuationData.findIndex(v => v.valuationId === row.valuationId)
            if (idx >= 0) this.$set(this.valuationData, idx, updated)
          }
        } else {
          this.$message.error((res && res.msg) || '重新估值失败')
        }
      } catch (e) {
        this.$message.error('重新估值失败')
      }
    },
    handleViewDetail(row) {
      this.detailRow = row
      this.detailDialogVisible = true
    },
    getProductTypeTag(type) {
      return { FORWARD: 'primary', OPTION: 'success', FUTURES: 'warning', SWAP: 'info' }[type] || ''
    },
    getProductTypeText(type) {
      return { FORWARD: '远期', OPTION: '期权', FUTURES: '期货', SWAP: '掉期' }[type] || type
    },
    getPnLClass(pnl) {
      if (pnl > 0) return 'profit'
      if (pnl < 0) return 'loss'
      return 'neutral'
    },
    getChangeClass(v) {
      if (v > 0) return 'positive-change'
      if (v < 0) return 'negative-change'
      return 'neutral-change'
    },
    formatAmount(amount) {
      if (amount == null) return '0.00'
      return parseFloat(amount).toLocaleString('zh-CN', { minimumFractionDigits: 2, maximumFractionDigits: 2 })
    },
    formatPercent(percent) {
      if (percent == null) return '0.00%'
      return (parseFloat(percent) * 100).toFixed(2) + '%'
    }
  },
  beforeDestroy() {
    if (this.pieChart) { this.pieChart.dispose(); this.pieChart = null }
  }
}
</script>


<style scoped>
.valuation-management-wrapper {
  height: 100%;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-weight: bold;
}

.valuation-actions {
  margin-bottom: 20px;
}

.action-card {
  cursor: pointer;
  transition: all 0.3s;
  text-align: center;
  padding: 20px;
  height: 120px;
}

.action-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.action-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100%;
}

.action-icon {
  font-size: 36px;
  margin-bottom: 12px;
}

.action-title {
  font-size: 16px;
  font-weight: bold;
  color: #303133;
  margin-bottom: 6px;
}

.action-desc {
  font-size: 12px;
  color: #909399;
}

.valuation-tabs {
  margin-top: 20px;
}

.summary-card {
  text-align: center;
  min-height: 110px;
}

::v-deep .summary-card .el-card__body {
  padding: 24px 16px;
}

.summary-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

.summary-title {
  font-size: 14px;
  color: #909399;
  margin-bottom: 12px;
}

.summary-value {
  font-size: 22px;
  font-weight: bold;
  color: #303133;
  word-break: break-all;
}

.chart-card {
  height: 400px;
}

.chart-placeholder {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 320px;
}

.amount-text {
  color: #409eff;
  font-weight: bold;
}

.value-text {
  color: #67c23a;
  font-weight: bold;
}

.pv-text {
  color: #e6a23c;
  font-weight: bold;
}

.profit {
  color: #67c23a;
  font-weight: bold;
}

.loss {
  color: #f56c6c;
  font-weight: bold;
}

.neutral {
  color: #909399;
}

.positive-change {
  color: #67c23a;
}

.negative-change {
  color: #f56c6c;
}

.neutral-change {
  color: #909399;
}
</style>
