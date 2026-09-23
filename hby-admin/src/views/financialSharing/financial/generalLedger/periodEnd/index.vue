<template>
  <div class="period-end-container">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-left">
        <h1 class="page-title">
          <i class="el-icon-finished"></i>
          期末处理
        </h1>
        <p class="page-description">执行期末结账和账务处理</p>
      </div>
      <div class="header-right">
        <el-button type="primary" icon="el-icon-refresh" @click="refreshData">
          刷新数据
        </el-button>
        <el-button type="success" icon="el-icon-download" @click="exportData">
          导出报告
        </el-button>
      </div>
    </div>

    <!-- 期末处理统计概览 -->
    <div class="stats-overview">
      <el-row :gutter="24">
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon tasks">
              <i class="el-icon-s-order"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ stats.totalTasks }}</div>
              <div class="stat-label">处理任务数</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon completed">
              <i class="el-icon-circle-check"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ stats.completedTasks }}</div>
              <div class="stat-label">已完成任务</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon period">
              <i class="el-icon-date"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ stats.currentPeriod }}</div>
              <div class="stat-label">当前期间</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon status">
              <i class="el-icon-warning"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value" :class="{ 'success': stats.isBalanced, 'error': !stats.isBalanced }">
                {{ stats.isBalanced ? '平衡' : '不平衡' }}
              </div>
              <div class="stat-label">试算平衡状态</div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 期末处理功能标签页 -->
    <div class="period-end-tabs">
      <el-tabs v-model="activeTab" @tab-click="handleTabClick">
        <!-- 期末检查 -->
        <el-tab-pane label="期末检查" name="check">
          <div class="tab-content">
            <!-- 检查项列表 -->
            <div class="check-items">
              <h3>期末检查项</h3>
              <el-table
                :data="checkItems"
                v-loading="checkLoading"
                border
                stripe
              >
                <el-table-column prop="checkName" label="检查项名称" width="200" />
                <el-table-column prop="checkDescription" label="检查描述" />
                <el-table-column prop="checkStatus" label="检查状态" width="120">
                  <template slot-scope="scope">
                    <el-tag :type="getCheckStatusType(scope.row.checkStatus)">
                      {{ getCheckStatusText(scope.row.checkStatus) }}
                    </el-tag>
                  </template>
                </el-table-column>
                <el-table-column prop="checkResult" label="检查结果" width="150">
                  <template slot-scope="scope">
                    <el-tag :type="scope.row.checkResult === 'PASS' ? 'success' : 'danger'">
                      {{ scope.row.checkResult === 'PASS' ? '通过' : '未通过' }}
                    </el-tag>
                  </template>
                </el-table-column>
                <el-table-column prop="errorMessage" label="错误信息" width="200" />
                <el-table-column label="操作" width="150" fixed="right">
                  <template slot-scope="scope">
                    <el-button 
                      type="text" 
                      size="small" 
                      @click="executeCheck(scope.row)"
                      :disabled="scope.row.checkStatus === 'RUNNING'"
                    >
                      {{ scope.row.checkStatus === 'RUNNING' ? '检查中...' : '执行检查' }}
                    </el-button>
                    <el-button type="text" size="small" @click="viewCheckDetail(scope.row)">
                      查看详情
                    </el-button>
                  </template>
                </el-table-column>
              </el-table>

              <div class="check-actions">
                <el-button type="primary" @click="executeAllChecks" :loading="allCheckLoading">
                  执行全部检查
                </el-button>
                <el-button @click="refreshCheckItems">
                  刷新检查项
                </el-button>
              </div>
            </div>
          </div>
        </el-tab-pane>

        <!-- 损益结转 -->
        <el-tab-pane label="损益结转" name="profitLoss">
          <div class="tab-content">
            <!-- 损益结转设置 -->
            <div class="profit-loss-settings">
              <h3>损益结转设置</h3>
              <el-form :model="profitLossForm" ref="profitLossForm" label-width="120px" size="small">
                <el-row :gutter="24">
                  <el-col :span="12">
                    <el-form-item label="结转期间" prop="carryForwardPeriod">
                      <el-date-picker
                        v-model="profitLossForm.carryForwardPeriod"
                        type="month"
                        placeholder="选择结转期间"
                        format="yyyy-MM"
                        value-format="yyyy-MM"
                      />
                    </el-form-item>
                  </el-col>
                  <el-col :span="12">
                    <el-form-item label="结转方式" prop="carryForwardMethod">
                      <el-select v-model="profitLossForm.carryForwardMethod" placeholder="请选择结转方式">
                        <el-option label="账结法" value="ACCOUNT_BASED" />
                        <el-option label="表结法" value="STATEMENT_BASED" />
                      </el-select>
                    </el-form-item>
                  </el-col>
                </el-row>
                <el-row :gutter="24">
                  <el-col :span="12">
                    <el-form-item label="本年利润科目" prop="currentYearProfitSubject">
                      <el-input v-model="profitLossForm.currentYearProfitSubject" placeholder="请输入本年利润科目" />
                    </el-form-item>
                  </el-col>
                  <el-col :span="12">
                    <el-form-item label="凭证类型" prop="voucherType">
                      <el-select v-model="profitLossForm.voucherType" placeholder="请选择凭证类型">
                        <el-option label="记账凭证" value="GENERAL" />
                        <el-option label="转账凭证" value="TRANSFER" />
                        <el-option label="结转凭证" value="CARRY_FORWARD" />
                      </el-select>
                    </el-form-item>
                  </el-col>
                </el-row>
                <el-form-item label="结转说明" prop="carryForwardDescription">
                  <el-input 
                    v-model="profitLossForm.carryForwardDescription" 
                    type="textarea" 
                    :rows="3"
                    placeholder="请输入结转说明"
                  />
                </el-form-item>
              </el-form>

              <div class="profit-loss-actions">
                <el-button type="primary" @click="previewProfitLoss" :loading="previewLoading">
                  预览结转
                </el-button>
                <el-button type="success" @click="executeProfitLoss" :loading="executeLoading">
                  执行结转
                </el-button>
                <el-button @click="resetProfitLossForm">
                  重置
                </el-button>
              </div>
            </div>

            <!-- 损益结转预览 -->
            <div class="profit-loss-preview" v-if="profitLossPreviewData.length > 0">
              <h3>损益结转预览</h3>
              <el-table
                :data="profitLossPreviewData"
                border
                stripe
                show-summary
                :summary-method="getProfitLossSummaries"
              >
                <el-table-column prop="subjectCode" label="科目编码" width="120" />
                <el-table-column prop="subjectName" label="科目名称" width="200" />
                <el-table-column prop="debitAmount" label="借方金额" width="150" align="right">
                  <template slot-scope="scope">
                    {{ formatAmount(scope.row.debitAmount) }}
                  </template>
                </el-table-column>
                <el-table-column prop="creditAmount" label="贷方金额" width="150" align="right">
                  <template slot-scope="scope">
                    {{ formatAmount(scope.row.creditAmount) }}
                  </template>
                </el-table-column>
                <el-table-column prop="summary" label="摘要" />
              </el-table>
            </div>

            <!-- 损益结转历史 -->
            <div class="profit-loss-history">
              <h3>损益结转历史</h3>
              <el-table
                :data="profitLossHistory"
                v-loading="historyLoading"
                border
                stripe
              >
                <el-table-column prop="carryForwardPeriod" label="结转期间" width="120" />
                <el-table-column prop="carryForwardMethod" label="结转方式" width="120">
                  <template slot-scope="scope">
                    {{ scope.row.carryForwardMethod === 'ACCOUNT_BASED' ? '账结法' : '表结法' }}
                  </template>
                </el-table-column>
                <el-table-column prop="voucherNo" label="凭证号" width="150" />
                <el-table-column prop="totalAmount" label="结转金额" width="150" align="right">
                  <template slot-scope="scope">
                    {{ formatAmount(scope.row.totalAmount) }}
                  </template>
                </el-table-column>
                <el-table-column prop="carryForwardStatus" label="状态" width="100">
                  <template slot-scope="scope">
                    <el-tag :type="scope.row.carryForwardStatus === 'COMPLETED' ? 'success' : 'warning'">
                      {{ scope.row.carryForwardStatus === 'COMPLETED' ? '已完成' : '进行中' }}
                    </el-tag>
                  </template>
                </el-table-column>
                <el-table-column prop="createTime" label="创建时间" width="180" />
                <el-table-column label="操作" width="150" fixed="right">
                  <template slot-scope="scope">
                    <el-button type="text" size="small" @click="viewCarryForwardDetail(scope.row)">
                      查看详情
                    </el-button>
                    <el-button 
                      type="text" 
                      size="small" 
                      @click="reverseCarryForward(scope.row)"
                      v-if="scope.row.carryForwardStatus === 'COMPLETED'"
                    >
                      反结转
                    </el-button>
                  </template>
                </el-table-column>
              </el-table>

              <div class="pagination-container">
                <el-pagination
                  @size-change="handleHistorySizeChange"
                  @current-change="handleHistoryCurrentChange"
                  :current-page="historyPagination.currentPage"
                  :page-sizes="[10, 20, 50, 100]"
                  :page-size="historyPagination.pageSize"
                  layout="total, sizes, prev, pager, next, jumper"
                  :total="historyPagination.total"
                />
              </div>
            </div>
          </div>
        </el-tab-pane>

        <!-- 期末结账 -->
        <el-tab-pane label="期末结账" name="closing">
          <div class="tab-content">
            <!-- 结账设置 -->
            <div class="closing-settings">
              <h3>期末结账设置</h3>
              <el-form :model="closingForm" ref="closingForm" label-width="120px" size="small">
                <el-row :gutter="24">
                  <el-col :span="12">
                    <el-form-item label="结账期间" prop="closingPeriod">
                      <el-date-picker
                        v-model="closingForm.closingPeriod"
                        type="month"
                        placeholder="选择结账期间"
                        format="yyyy-MM"
                        value-format="yyyy-MM"
                      />
                    </el-form-item>
                  </el-col>
                  <el-col :span="12">
                    <el-form-item label="结账类型" prop="closingType">
                      <el-select v-model="closingForm.closingType" placeholder="请选择结账类型">
                        <el-option label="月结" value="MONTHLY" />
                        <el-option label="季结" value="QUARTERLY" />
                        <el-option label="年结" value="YEARLY" />
                      </el-select>
                    </el-form-item>
                  </el-col>
                </el-row>
                <el-form-item label="结账说明" prop="closingDescription">
                  <el-input 
                    v-model="closingForm.closingDescription" 
                    type="textarea" 
                    :rows="3"
                    placeholder="请输入结账说明"
                  />
                </el-form-item>
              </el-form>

              <div class="closing-actions">
                <el-button type="primary" @click="executeClosing" :loading="closingLoading">
                  执行结账
                </el-button>
                <el-button type="warning" @click="reverseClosing" :loading="reverseClosingLoading">
                  反结账
                </el-button>
                <el-button @click="resetClosingForm">
                  重置
                </el-button>
              </div>
            </div>

            <!-- 结账历史 -->
            <div class="closing-history">
              <h3>结账历史</h3>
              <el-table
                :data="closingHistory"
                v-loading="closingHistoryLoading"
                border
                stripe
              >
                <el-table-column prop="closingPeriod" label="结账期间" width="120" />
                <el-table-column prop="closingType" label="结账类型" width="100">
                  <template slot-scope="scope">
                    {{ getClosingTypeText(scope.row.closingType) }}
                  </template>
                </el-table-column>
                <el-table-column prop="closingStatus" label="结账状态" width="100">
                  <template slot-scope="scope">
                    <el-tag :type="getClosingStatusType(scope.row.closingStatus)">
                      {{ getClosingStatusText(scope.row.closingStatus) }}
                    </el-tag>
                  </template>
                </el-table-column>
                <el-table-column prop="closingTime" label="结账时间" width="180" />
                <el-table-column prop="closingUser" label="结账人" width="120" />
                <el-table-column prop="closingDescription" label="结账说明" />
                <el-table-column label="操作" width="120" fixed="right">
                  <template slot-scope="scope">
                    <el-button type="text" size="small" @click="viewClosingDetail(scope.row)">
                      查看详情
                    </el-button>
                  </template>
                </el-table-column>
              </el-table>

              <div class="pagination-container">
                <el-pagination
                  @size-change="handleClosingHistorySizeChange"
                  @current-change="handleClosingHistoryCurrentChange"
                  :current-page="closingHistoryPagination.currentPage"
                  :page-sizes="[10, 20, 50, 100]"
                  :page-size="closingHistoryPagination.pageSize"
                  layout="total, sizes, prev, pager, next, jumper"
                  :total="closingHistoryPagination.total"
                />
              </div>
            </div>
          </div>
        </el-tab-pane>

        <!-- 自动转账 -->
        <el-tab-pane label="自动转账" name="autoTransfer">
          <div class="tab-content">
            <!-- 自动转账设置 -->
            <div class="auto-transfer-settings">
              <h3>自动转账设置</h3>
              <el-form :model="autoTransferForm" ref="autoTransferForm" label-width="120px" size="small">
                <el-row :gutter="24">
                  <el-col :span="12">
                    <el-form-item label="转账期间" prop="transferPeriod">
                      <el-date-picker
                        v-model="autoTransferForm.transferPeriod"
                        type="month"
                        placeholder="选择转账期间"
                        format="yyyy-MM"
                        value-format="yyyy-MM"
                      />
                    </el-form-item>
                  </el-col>
                  <el-col :span="12">
                    <el-form-item label="转账类型" prop="transferType">
                      <el-select v-model="autoTransferForm.transferType" placeholder="请选择转账类型">
                        <el-option label="费用分摊" value="EXPENSE_ALLOCATION" />
                        <el-option label="成本结转" value="COST_CARRY_FORWARD" />
                        <el-option label="汇兑损益" value="EXCHANGE_GAIN_LOSS" />
                        <el-option label="其他转账" value="OTHER_TRANSFER" />
                      </el-select>
                    </el-form-item>
                  </el-col>
                </el-row>
                <el-form-item label="转账说明" prop="transferDescription">
                  <el-input 
                    v-model="autoTransferForm.transferDescription" 
                    type="textarea" 
                    :rows="3"
                    placeholder="请输入转账说明"
                  />
                </el-form-item>
              </el-form>

              <div class="auto-transfer-actions">
                <el-button type="primary" @click="executeAutoTransfer" :loading="autoTransferLoading">
                  执行自动转账
                </el-button>
                <el-button @click="resetAutoTransferForm">
                  重置
                </el-button>
              </div>
            </div>

            <!-- 自动转账历史 -->
            <div class="auto-transfer-history">
              <h3>自动转账历史</h3>
              <el-table
                :data="autoTransferHistory"
                v-loading="autoTransferHistoryLoading"
                border
                stripe
              >
                <el-table-column prop="transferPeriod" label="转账期间" width="120" />
                <el-table-column prop="transferType" label="转账类型" width="150">
                  <template slot-scope="scope">
                    {{ getTransferTypeText(scope.row.transferType) }}
                  </template>
                </el-table-column>
                <el-table-column prop="voucherNo" label="凭证号" width="150" />
                <el-table-column prop="transferAmount" label="转账金额" width="150" align="right">
                  <template slot-scope="scope">
                    {{ formatAmount(scope.row.transferAmount) }}
                  </template>
                </el-table-column>
                <el-table-column prop="transferStatus" label="状态" width="100">
                  <template slot-scope="scope">
                    <el-tag :type="scope.row.transferStatus === 'COMPLETED' ? 'success' : 'warning'">
                      {{ scope.row.transferStatus === 'COMPLETED' ? '已完成' : '进行中' }}
                    </el-tag>
                  </template>
                </el-table-column>
                <el-table-column prop="createTime" label="创建时间" width="180" />
                <el-table-column label="操作" width="120" fixed="right">
                  <template slot-scope="scope">
                    <el-button type="text" size="small" @click="viewTransferDetail(scope.row)">
                      查看详情
                    </el-button>
                  </template>
                </el-table-column>
              </el-table>

              <div class="pagination-container">
                <el-pagination
                  @size-change="handleAutoTransferHistorySizeChange"
                  @current-change="handleAutoTransferHistoryCurrentChange"
                  :current-page="autoTransferHistoryPagination.currentPage"
                  :page-sizes="[10, 20, 50, 100]"
                  :page-size="autoTransferHistoryPagination.pageSize"
                  layout="total, sizes, prev, pager, next, jumper"
                  :total="autoTransferHistoryPagination.total"
                />
              </div>
            </div>
          </div>
        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>

<script>
import * as generalLedgerApi from '@/api/financialSharing/generalLedger'

export default {
  name: 'PeriodEnd',
  data() {
    return {
      activeTab: 'check',
      stats: {
        totalTasks: 0,
        completedTasks: 0,
        currentPeriod: '',
        isBalanced: true
      },
      // 期末检查
      checkItems: [],
      checkLoading: false,
      allCheckLoading: false,
      // 损益结转
      profitLossForm: {
        carryForwardPeriod: '',
        carryForwardMethod: 'ACCOUNT_BASED',
        currentYearProfitSubject: '3131',
        voucherType: 'CARRY_FORWARD',
        carryForwardDescription: ''
      },
      profitLossPreviewData: [],
      profitLossHistory: [],
      previewLoading: false,
      executeLoading: false,
      historyLoading: false,
      historyPagination: {
        currentPage: 1,
        pageSize: 15,
        total: 0
      },
      // 期末结账
      closingForm: {
        closingPeriod: '',
        closingType: 'MONTHLY',
        closingDescription: ''
      },
      closingHistory: [],
      closingLoading: false,
      reverseClosingLoading: false,
      closingHistoryLoading: false,
      closingHistoryPagination: {
        currentPage: 1,
        pageSize: 15,
        total: 0
      },
      // 自动转账
      autoTransferForm: {
        transferPeriod: '',
        transferType: 'EXPENSE_ALLOCATION',
        transferDescription: ''
      },
      autoTransferHistory: [],
      autoTransferLoading: false,
      autoTransferHistoryLoading: false,
      autoTransferHistoryPagination: {
        currentPage: 1,
        pageSize: 15,
        total: 0
      }
    }
  },
  mounted() {
    this.loadStats()
    this.loadCheckItems()
  },
  methods: {
    async loadStats() {
      try {
        // 暂未对接 API，先以空状态展示
        this.stats = {
          totalTasks: 0,
          completedTasks: 0,
          currentPeriod: new Date().toISOString().slice(0, 7),
          isBalanced: false
        }
      } catch (error) {
        console.error('加载统计数据失败:', error)
      }
    },
    formatAmount(amount) {
      if (!amount) return '0.00'
      return (amount / 10000).toFixed(2) + '万'
    },
    refreshData() {
      this.loadStats()
      if (this.activeTab === 'check') {
        this.loadCheckItems()
      } else if (this.activeTab === 'profitLoss') {
        this.loadProfitLossHistory()
      } else if (this.activeTab === 'closing') {
        this.loadClosingHistory()
      } else if (this.activeTab === 'autoTransfer') {
        this.loadAutoTransferHistory()
      }
    },
    exportData() {
      try {
        const data = this.checkItems || []
        if (data.length === 0) {
          this.$message.warning('暂无数据可导出')
          return
        }
        const jsonStr = JSON.stringify(data, null, 2)
        const blob = new Blob([jsonStr], { type: 'application/json' })
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = '期末处理数据导出.json'
        link.click()
        window.URL.revokeObjectURL(url)
        this.$message.success('导出成功')
      } catch (error) {
        this.$message.error('导出失败')
      }
    },
    handleTabClick(tab) {
      if (tab.name === 'check') {
        this.loadCheckItems()
      } else if (tab.name === 'profitLoss') {
        this.loadProfitLossHistory()
      } else if (tab.name === 'closing') {
        this.loadClosingHistory()
      } else if (tab.name === 'autoTransfer') {
        this.loadAutoTransferHistory()
      }
    },

    // 期末检查方法
    async loadCheckItems() {
      this.checkLoading = true
      try {
        const response = await generalLedgerApi.getPeriodEndCheckItems({
          accountingPeriod: this.stats.currentPeriod
        })
        if (response.code === 1) {
          // 后端返回的数据结构：data: { checkItems: [...], period: '...', ... }
          this.checkItems = response.data?.checkItems || []
          // 更新统计信息
          if (response.data) {
            this.stats.currentPeriod = response.data.period || this.stats.currentPeriod
          }
        } else {
          this.$message.error(response.msg || '加载检查项失败')
        }
      } catch (error) {
        this.$message.error('加载检查项失败：' + error.message)
      } finally {
        this.checkLoading = false
      }
    },

    async executeCheck(item) {
      try {
        item.checkStatus = 'RUNNING'
        const response = await generalLedgerApi.executePeriodEndCheck({
          checkId: item.checkId,
          accountingPeriod: this.stats.currentPeriod
        })
        if (response.code === 1) {
          item.checkStatus = 'COMPLETED'
          item.checkResult = response.data.checkResult
          item.errorMessage = response.data.errorMessage || ''
          this.$message.success('检查执行成功')
        } else {
          item.checkStatus = 'FAILED'
          item.errorMessage = response.msg
          this.$message.error(response.msg || '检查执行失败')
        }
      } catch (error) {
        item.checkStatus = 'FAILED'
        item.errorMessage = error.message
        this.$message.error('检查执行失败：' + error.message)
      }
    },

    async executeAllChecks() {
      this.allCheckLoading = true
      try {
        for (const item of this.checkItems) {
          if (item.checkStatus !== 'COMPLETED') {
            await this.executeCheck(item)
          }
        }
        this.$message.success('全部检查执行完成')
      } catch (error) {
        this.$message.error('批量检查执行失败：' + error.message)
      } finally {
        this.allCheckLoading = false
      }
    },

    refreshCheckItems() {
      this.loadCheckItems()
    },

    viewCheckDetail(item) {
      this.$message.info(`查看检查项 ${item.checkName} 的详细信息`)
    },

    getCheckStatusType(status) {
      const typeMap = {
        'PENDING': 'info',
        'RUNNING': 'warning',
        'COMPLETED': 'success',
        'FAILED': 'danger'
      }
      return typeMap[status] || 'info'
    },

    getCheckStatusText(status) {
      const textMap = {
        'PENDING': '待检查',
        'RUNNING': '检查中',
        'COMPLETED': '已完成',
        'FAILED': '检查失败'
      }
      return textMap[status] || status
    },

    // 损益结转方法
    async previewProfitLoss() {
      this.previewLoading = true
      try {
        const response = await generalLedgerApi.getProfitLossCarryForwardPreview(this.profitLossForm)
        if (response.code === 1) {
          this.profitLossPreviewData = response.data || []
          this.$message.success('预览生成成功')
        } else {
          this.$message.error(response.msg || '预览生成失败')
        }
      } catch (error) {
        this.$message.error('预览生成失败：' + error.message)
      } finally {
        this.previewLoading = false
      }
    },

    async executeProfitLoss() {
      this.executeLoading = true
      try {
        const response = await generalLedgerApi.executeProfitLossCarryForward(this.profitLossForm)
        if (response.code === 1) {
          this.$message.success('损益结转执行成功')
          this.loadProfitLossHistory()
          this.profitLossPreviewData = []
        } else {
          this.$message.error(response.msg || '损益结转执行失败')
        }
      } catch (error) {
        this.$message.error('损益结转执行失败：' + error.message)
      } finally {
        this.executeLoading = false
      }
    },

    resetProfitLossForm() {
      this.$refs.profitLossForm.resetFields()
      this.profitLossPreviewData = []
    },

    async loadProfitLossHistory() {
      this.historyLoading = true
      try {
        const params = {
          pageNumber: this.historyPagination.currentPage,
          pageSize: this.historyPagination.pageSize
        }
        const response = await generalLedgerApi.getProfitLossCarryForwardHistory(params)
        if (response.code === 1) {
          this.profitLossHistory = response.data.tlist || []
          this.historyPagination.total = response.data.totalRecord || 0
        } else {
          this.$message.error(response.msg || '加载历史记录失败')
        }
      } catch (error) {
        this.$message.error('加载历史记录失败：' + error.message)
      } finally {
        this.historyLoading = false
      }
    },

    handleHistorySizeChange(size) {
      this.historyPagination.pageSize = size
      this.loadProfitLossHistory()
    },

    handleHistoryCurrentChange(page) {
      this.historyPagination.currentPage = page
      this.loadProfitLossHistory()
    },

    viewCarryForwardDetail(row) {
      this.$message.info(`查看结转记录 ${row.voucherNo} 的详细信息`)
    },

    async reverseCarryForward(row) {
      try {
        await this.$confirm('确定要反结转该记录吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })

        const response = await generalLedgerApi.reverseProfitLossCarryForward(row.carryForwardId, '用户手动反结转')
        if (response.code === 1) {
          this.$message.success('反结转成功')
          this.loadProfitLossHistory()
        } else {
          this.$message.error(response.msg || '反结转失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('反结转失败：' + error.message)
        }
      }
    },

    getProfitLossSummaries(param) {
      const { columns, data } = param
      const sums = []
      columns.forEach((column, index) => {
        if (index === 0) {
          sums[index] = '合计'
          return
        }
        const values = data.map(item => Number(item[column.property]))
        if (!values.every(value => isNaN(value))) {
          sums[index] = values.reduce((prev, curr) => {
            const value = Number(curr)
            if (!isNaN(value)) {
              return prev + curr
            } else {
              return prev
            }
          }, 0)
          if (column.property.includes('Amount')) {
            sums[index] = this.formatAmount(sums[index])
          }
        } else {
          sums[index] = ''
        }
      })
      return sums
    },

    // 期末结账方法
    async executeClosing() {
      this.closingLoading = true
      try {
        const response = await generalLedgerApi.executePeriodEndClosing(this.closingForm)
        if (response.code === 1) {
          this.$message.success('期末结账执行成功')
          this.loadClosingHistory()
          this.loadStats()
        } else {
          this.$message.error(response.msg || '期末结账执行失败')
        }
      } catch (error) {
        this.$message.error('期末结账执行失败：' + error.message)
      } finally {
        this.closingLoading = false
      }
    },

    async reverseClosing() {
      try {
        await this.$confirm('确定要执行反结账操作吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })

        this.reverseClosingLoading = true
        const response = await generalLedgerApi.reversePeriodEndClosing(this.closingForm)
        if (response.code === 1) {
          this.$message.success('反结账执行成功')
          this.loadClosingHistory()
          this.loadStats()
        } else {
          this.$message.error(response.msg || '反结账执行失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('反结账执行失败：' + error.message)
        }
      } finally {
        this.reverseClosingLoading = false
      }
    },

    resetClosingForm() {
      this.$refs.closingForm.resetFields()
    },

    async loadClosingHistory() {
      this.closingHistoryLoading = true
      try {
        const params = {
          pageNumber: this.closingHistoryPagination.currentPage,
          pageSize: this.closingHistoryPagination.pageSize
        }
        const response = await generalLedgerApi.getPeriodEndClosingHistory(params)
        if (response.code === 1) {
          this.closingHistory = response.data.tlist || []
          this.closingHistoryPagination.total = response.data.totalRecord || 0
        } else {
          this.$message.error(response.msg || '加载结账历史失败')
        }
      } catch (error) {
        this.$message.error('加载结账历史失败：' + error.message)
      } finally {
        this.closingHistoryLoading = false
      }
    },

    handleClosingHistorySizeChange(size) {
      this.closingHistoryPagination.pageSize = size
      this.loadClosingHistory()
    },

    handleClosingHistoryCurrentChange(page) {
      this.closingHistoryPagination.currentPage = page
      this.loadClosingHistory()
    },

    viewClosingDetail(row) {
      this.$message.info(`查看结账记录 ${row.closingPeriod} 的详细信息`)
    },

    getClosingTypeText(type) {
      const textMap = {
        'MONTHLY': '月结',
        'QUARTERLY': '季结',
        'YEARLY': '年结'
      }
      return textMap[type] || type
    },

    getClosingStatusType(status) {
      const typeMap = {
        'PENDING': 'info',
        'PROCESSING': 'warning',
        'COMPLETED': 'success',
        'FAILED': 'danger'
      }
      return typeMap[status] || 'info'
    },

    getClosingStatusText(status) {
      const textMap = {
        'PENDING': '待结账',
        'PROCESSING': '结账中',
        'COMPLETED': '已结账',
        'FAILED': '结账失败'
      }
      return textMap[status] || status
    },

    // 自动转账方法
    async executeAutoTransfer() {
      this.autoTransferLoading = true
      try {
        const response = await generalLedgerApi.executeAutoTransfer(this.autoTransferForm)
        if (response.code === 1) {
          this.$message.success('自动转账执行成功')
          this.loadAutoTransferHistory()
        } else {
          this.$message.error(response.msg || '自动转账执行失败')
        }
      } catch (error) {
        this.$message.error('自动转账执行失败：' + error.message)
      } finally {
        this.autoTransferLoading = false
      }
    },

    resetAutoTransferForm() {
      this.$refs.autoTransferForm.resetFields()
    },

    async loadAutoTransferHistory() {
      this.autoTransferHistoryLoading = true
      try {
        const params = {
          pageNumber: this.autoTransferHistoryPagination.currentPage,
          pageSize: this.autoTransferHistoryPagination.pageSize
        }
        const response = await generalLedgerApi.getPeriodEndTaskList(params)
        if (response.code === 1) {
          this.autoTransferHistory = response.data.tlist || []
          this.autoTransferHistoryPagination.total = response.data.totalRecord || 0
        } else {
          this.$message.error(response.msg || '加载转账历史失败')
        }
      } catch (error) {
        this.$message.error('加载转账历史失败：' + error.message)
      } finally {
        this.autoTransferHistoryLoading = false
      }
    },

    handleAutoTransferHistorySizeChange(size) {
      this.autoTransferHistoryPagination.pageSize = size
      this.loadAutoTransferHistory()
    },

    handleAutoTransferHistoryCurrentChange(page) {
      this.autoTransferHistoryPagination.currentPage = page
      this.loadAutoTransferHistory()
    },

    viewTransferDetail(row) {
      this.$message.info(`查看转账记录 ${row.voucherNo} 的详细信息`)
    },

    getTransferTypeText(type) {
      const textMap = {
        'EXPENSE_ALLOCATION': '费用分摊',
        'COST_CARRY_FORWARD': '成本结转',
        'EXCHANGE_GAIN_LOSS': '汇兑损益',
        'OTHER_TRANSFER': '其他转账'
      }
      return textMap[type] || type
    }
  }
}
</script>

<style lang="scss" scoped>
.period-end-container {
  padding: 20px;
  background: #f5f7fa;
  min-height: calc(100vh - 84px);
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: white;
  border-radius: 12px;
  padding: 24px;
  margin-bottom: 24px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);

  .header-left {
    .page-title {
      font-size: 24px;
      font-weight: 600;
      color: #303133;
      margin: 0 0 8px 0;
      display: flex;
      align-items: center;

      i {
        margin-right: 12px;
        color: #409eff;
      }
    }

    .page-description {
      color: #606266;
      font-size: 14px;
      margin: 0;
    }
  }

  .header-right {
    .el-button {
      margin-left: 12px;
    }
  }
}

.stats-overview {
  margin-bottom: 24px;

  .stat-card {
    background: white;
    border-radius: 12px;
    padding: 24px;
    display: flex;
    align-items: center;
    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
    transition: all 0.3s ease;

    &:hover {
      transform: translateY(-2px);
      box-shadow: 0 8px 25px rgba(0, 0, 0, 0.15);
    }

    .stat-icon {
      width: 60px;
      height: 60px;
      border-radius: 12px;
      display: flex;
      align-items: center;
      justify-content: center;
      margin-right: 16px;

      i {
        font-size: 28px;
        color: white;
      }

      &.tasks {
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      }

      &.completed {
        background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
      }

      &.period {
        background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
      }

      &.status {
        background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
      }
    }

    .stat-content {
      .stat-value {
        font-size: 28px;
        font-weight: 600;
        color: #303133;
        margin-bottom: 4px;

        &.success {
          color: #67c23a;
        }

        &.error {
          color: #f56c6c;
        }
      }

      .stat-label {
        font-size: 14px;
        color: #909399;
      }
    }
  }
}

.period-end-tabs {
  background: white;
  border-radius: 12px;
  padding: 24px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);

  .tab-content {
    h3 {
      margin-bottom: 20px;
      color: #303133;
      font-size: 16px;
      font-weight: 600;
    }

    .check-items,
    .profit-loss-settings,
    .profit-loss-preview,
    .profit-loss-history,
    .closing-settings,
    .closing-history,
    .auto-transfer-settings,
    .auto-transfer-history {
      margin-bottom: 30px;
    }

    .check-actions,
    .profit-loss-actions,
    .closing-actions,
    .auto-transfer-actions {
      margin-top: 20px;
      text-align: center;
    }

    .pagination-container {
      margin-top: 20px;
      text-align: right;
    }
  }
}
</style>
