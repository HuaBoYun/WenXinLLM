<template>
  <div class="ledger-query-container">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-left">
        <h1 class="page-title">
          <i class="el-icon-search"></i>
          总账查询
        </h1>
        <p class="page-description">多维度查询总账数据和统计信息</p>
      </div>
      <div class="header-right">
        <el-button type="primary" icon="el-icon-refresh" @click="refreshData">
          刷新数据
        </el-button>
        <el-button type="success" icon="el-icon-download" @click="exportData">
          导出数据
        </el-button>
      </div>
    </div>

    <!-- 查询统计概览 -->
    <div class="stats-overview">
      <el-row :gutter="24">
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon query">
              <i class="el-icon-search"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ stats.totalQueries }}</div>
              <div class="stat-label">查询记录数</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon subjects">
              <i class="el-icon-s-data"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ stats.totalSubjects }}</div>
              <div class="stat-label">涉及科目数</div>
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
              <div class="stat-label">查询金额合计</div>
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
      </el-row>
    </div>

    <!-- 查询功能标签页 -->
    <div class="query-tabs">
      <el-tabs v-model="activeTab" @tab-click="handleTabClick">
        <!-- 科目总账查询 -->
        <el-tab-pane label="科目总账查询" name="subject">
          <div class="tab-content">
            <!-- 查询条件 -->
            <div class="search-form">
              <el-form :model="subjectForm" ref="subjectForm" :inline="true" size="small">
                <el-form-item label="科目编码" prop="subjectCode">
                  <el-input v-model="subjectForm.subjectCode" placeholder="请输入科目编码" clearable />
                </el-form-item>
                <el-form-item label="科目名称" prop="subjectName">
                  <el-input v-model="subjectForm.subjectName" placeholder="请输入科目名称" clearable />
                </el-form-item>
                <el-form-item label="会计期间" prop="accountingPeriod">
                  <el-date-picker
                    v-model="subjectForm.accountingPeriod"
                    type="month"
                    placeholder="选择会计期间"
                    format="yyyy-MM"
                    value-format="yyyy-MM"
                  />
                </el-form-item>
                <el-form-item label="科目类型" prop="subjectType">
                  <el-select v-model="subjectForm.subjectType" placeholder="请选择科目类型" clearable>
                    <el-option label="资产" :value="1" />
                    <el-option label="负债" :value="2" />
                    <el-option label="权益" :value="3" />
                    <el-option label="收入" :value="4" />
                    <el-option label="费用" :value="5" />
                  </el-select>
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" @click="handleSubjectSearch">查询</el-button>
                  <el-button @click="handleSubjectReset">重置</el-button>
                </el-form-item>
              </el-form>
            </div>

            <!-- 科目总账表格 -->
            <div class="table-container">
              <el-table
                :data="subjectTableData"
                v-loading="subjectLoading"
                border
                stripe
                height="400"
              >
                <el-table-column prop="subjectCode" label="科目编码" width="120" />
                <el-table-column prop="subjectName" label="科目名称" width="200" />
                <el-table-column prop="beginningBalance" label="期初余额" width="120" align="right">
                  <template slot-scope="scope">
                    {{ formatAmount(scope.row.beginningBalance) }}
                  </template>
                </el-table-column>
                <el-table-column prop="debitAmount" label="借方发生额" width="120" align="right">
                  <template slot-scope="scope">
                    {{ formatAmount(scope.row.debitAmount) }}
                  </template>
                </el-table-column>
                <el-table-column prop="creditAmount" label="贷方发生额" width="120" align="right">
                  <template slot-scope="scope">
                    {{ formatAmount(scope.row.creditAmount) }}
                  </template>
                </el-table-column>
                <el-table-column prop="endingBalance" label="期末余额" width="120" align="right">
                  <template slot-scope="scope">
                    {{ formatAmount(scope.row.endingBalance) }}
                  </template>
                </el-table-column>
                <el-table-column prop="balanceDirection" label="余额方向" width="100">
                  <template slot-scope="scope">
                    <el-tag :type="scope.row.balanceDirection === 1 ? 'success' : 'warning'">
                      {{ scope.row.balanceDirection === 1 ? '借方' : '贷方' }}
                    </el-tag>
                  </template>
                </el-table-column>
                <el-table-column label="操作" width="150" fixed="right">
                  <template slot-scope="scope">
                    <el-button type="text" size="small" @click="viewSubjectDetail(scope.row)">
                      查看明细
                    </el-button>
                    <el-button type="text" size="small" @click="viewSubjectTrend(scope.row)">
                      趋势分析
                    </el-button>
                  </template>
                </el-table-column>
              </el-table>

              <div class="pagination-container">
                <el-pagination
                  @size-change="handleSubjectSizeChange"
                  @current-change="handleSubjectCurrentChange"
                  :current-page="subjectPagination.currentPage"
                  :page-sizes="[10, 20, 50, 100]"
                  :page-size="subjectPagination.pageSize"
                  layout="total, sizes, prev, pager, next, jumper"
                  :total="subjectPagination.total"
                />
              </div>
            </div>
          </div>
        </el-tab-pane>

        <!-- 多栏式总账查询 -->
        <el-tab-pane label="多栏式总账" name="multiColumn">
          <div class="tab-content">
            <!-- 查询条件 -->
            <div class="search-form">
              <el-form :model="multiColumnForm" ref="multiColumnForm" :inline="true" size="small">
                <el-form-item label="科目编码" prop="subjectCode">
                  <el-input v-model="multiColumnForm.subjectCode" placeholder="请输入科目编码" clearable />
                </el-form-item>
                <el-form-item label="期间范围" prop="periodRange">
                  <el-date-picker
                    v-model="multiColumnForm.periodRange"
                    type="monthrange"
                    range-separator="至"
                    start-placeholder="开始期间"
                    end-placeholder="结束期间"
                    format="yyyy-MM"
                    value-format="yyyy-MM"
                  />
                </el-form-item>
                <el-form-item label="显示方式" prop="displayType">
                  <el-select v-model="multiColumnForm.displayType" placeholder="请选择显示方式">
                    <el-option label="按月显示" value="month" />
                    <el-option label="按季度显示" value="quarter" />
                    <el-option label="按年显示" value="year" />
                  </el-select>
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" @click="handleMultiColumnSearch">查询</el-button>
                  <el-button @click="handleMultiColumnReset">重置</el-button>
                </el-form-item>
              </el-form>
            </div>

            <!-- 多栏式总账表格 -->
            <div class="table-container">
              <el-table
                :data="multiColumnTableData"
                v-loading="multiColumnLoading"
                border
                stripe
                height="400"
              >
                <el-table-column prop="subjectCode" label="科目编码" width="120" fixed="left" />
                <el-table-column prop="subjectName" label="科目名称" width="200" fixed="left" />
                <el-table-column
                  v-for="period in periodColumns"
                  :key="period.key"
                  :prop="period.key"
                  :label="period.label"
                  width="120"
                  align="right"
                >
                  <template slot-scope="scope">
                    {{ formatAmount(scope.row[period.key]) }}
                  </template>
                </el-table-column>
              </el-table>

              <div class="pagination-container">
                <el-pagination
                  @size-change="handleMultiColumnSizeChange"
                  @current-change="handleMultiColumnCurrentChange"
                  :current-page="multiColumnPagination.currentPage"
                  :page-sizes="[10, 20, 50, 100]"
                  :page-size="multiColumnPagination.pageSize"
                  layout="total, sizes, prev, pager, next, jumper"
                  :total="multiColumnPagination.total"
                />
              </div>
            </div>
          </div>
        </el-tab-pane>

        <!-- 辅助核算查询 -->
        <el-tab-pane label="辅助核算查询" name="auxiliary">
          <div class="tab-content">
            <!-- 查询条件 -->
            <div class="search-form">
              <el-form :model="auxiliaryForm" ref="auxiliaryForm" :inline="true" size="small">
                <el-form-item label="科目编码" prop="subjectCode">
                  <el-input v-model="auxiliaryForm.subjectCode" placeholder="请输入科目编码" clearable />
                </el-form-item>
                <el-form-item label="辅助核算项" prop="auxiliaryType">
                  <el-select v-model="auxiliaryForm.auxiliaryType" placeholder="请选择辅助核算项" clearable>
                    <el-option label="客户" value="customer" />
                    <el-option label="供应商" value="supplier" />
                    <el-option label="部门" value="department" />
                    <el-option label="项目" value="project" />
                    <el-option label="存货" value="inventory" />
                  </el-select>
                </el-form-item>
                <el-form-item label="辅助核算值" prop="auxiliaryValue">
                  <el-input v-model="auxiliaryForm.auxiliaryValue" placeholder="请输入辅助核算值" clearable />
                </el-form-item>
                <el-form-item label="会计期间" prop="accountingPeriod">
                  <el-date-picker
                    v-model="auxiliaryForm.accountingPeriod"
                    type="month"
                    placeholder="选择会计期间"
                    format="yyyy-MM"
                    value-format="yyyy-MM"
                  />
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" @click="handleAuxiliarySearch">查询</el-button>
                  <el-button @click="handleAuxiliaryReset">重置</el-button>
                </el-form-item>
              </el-form>
            </div>

            <!-- 辅助核算表格 -->
            <div class="table-container">
              <el-table
                :data="auxiliaryTableData"
                v-loading="auxiliaryLoading"
                border
                stripe
                height="400"
              >
                <el-table-column prop="subjectCode" label="科目编码" width="120" />
                <el-table-column prop="subjectName" label="科目名称" width="150" />
                <el-table-column prop="auxiliaryType" label="辅助核算项" width="120">
                  <template slot-scope="scope">
                    <el-tag size="small">{{ getAuxiliaryTypeName(scope.row.auxiliaryType) }}</el-tag>
                  </template>
                </el-table-column>
                <el-table-column prop="auxiliaryCode" label="辅助编码" width="120" />
                <el-table-column prop="auxiliaryName" label="辅助名称" width="150" />
                <el-table-column prop="beginningBalance" label="期初余额" width="120" align="right">
                  <template slot-scope="scope">
                    {{ formatAmount(scope.row.beginningBalance) }}
                  </template>
                </el-table-column>
                <el-table-column prop="debitAmount" label="借方发生额" width="120" align="right">
                  <template slot-scope="scope">
                    {{ formatAmount(scope.row.debitAmount) }}
                  </template>
                </el-table-column>
                <el-table-column prop="creditAmount" label="贷方发生额" width="120" align="right">
                  <template slot-scope="scope">
                    {{ formatAmount(scope.row.creditAmount) }}
                  </template>
                </el-table-column>
                <el-table-column prop="endingBalance" label="期末余额" width="120" align="right">
                  <template slot-scope="scope">
                    {{ formatAmount(scope.row.endingBalance) }}
                  </template>
                </el-table-column>
                <el-table-column label="操作" width="120" fixed="right">
                  <template slot-scope="scope">
                    <el-button type="text" size="small" @click="viewAuxiliaryDetail(scope.row)">
                      查看明细
                    </el-button>
                  </template>
                </el-table-column>
              </el-table>

              <div class="pagination-container">
                <el-pagination
                  @size-change="handleAuxiliarySizeChange"
                  @current-change="handleAuxiliaryCurrentChange"
                  :current-page="auxiliaryPagination.currentPage"
                  :page-sizes="[10, 20, 50, 100]"
                  :page-size="auxiliaryPagination.pageSize"
                  layout="total, sizes, prev, pager, next, jumper"
                  :total="auxiliaryPagination.total"
                />
              </div>
            </div>
          </div>
        </el-tab-pane>

        <!-- 汇总查询 -->
        <el-tab-pane label="汇总查询" name="summary">
          <div class="tab-content">
            <!-- 查询条件 -->
            <div class="search-form">
              <el-form :model="summaryForm" ref="summaryForm" :inline="true" size="small">
                <el-form-item label="科目级次" prop="subjectLevel">
                  <el-select v-model="summaryForm.subjectLevel" placeholder="请选择科目级次" clearable>
                    <el-option label="一级科目" :value="1" />
                    <el-option label="二级科目" :value="2" />
                    <el-option label="三级科目" :value="3" />
                    <el-option label="四级科目" :value="4" />
                    <el-option label="末级科目" :value="99" />
                  </el-select>
                </el-form-item>
                <el-form-item label="科目类型" prop="subjectType">
                  <el-select v-model="summaryForm.subjectType" placeholder="请选择科目类型" clearable>
                    <el-option label="资产" :value="1" />
                    <el-option label="负债" :value="2" />
                    <el-option label="权益" :value="3" />
                    <el-option label="收入" :value="4" />
                    <el-option label="费用" :value="5" />
                  </el-select>
                </el-form-item>
                <el-form-item label="会计期间" prop="accountingPeriod">
                  <el-date-picker
                    v-model="summaryForm.accountingPeriod"
                    type="month"
                    placeholder="选择会计期间"
                    format="yyyy-MM"
                    value-format="yyyy-MM"
                  />
                </el-form-item>
                <el-form-item label="汇总方式" prop="summaryType">
                  <el-select v-model="summaryForm.summaryType" placeholder="请选择汇总方式">
                    <el-option label="按科目汇总" value="subject" />
                    <el-option label="按类型汇总" value="type" />
                    <el-option label="按级次汇总" value="level" />
                  </el-select>
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" @click="handleSummarySearch">查询</el-button>
                  <el-button @click="handleSummaryReset">重置</el-button>
                </el-form-item>
              </el-form>
            </div>

            <!-- 汇总查询表格 -->
            <div class="table-container">
              <el-table
                :data="summaryTableData"
                v-loading="summaryLoading"
                border
                stripe
                height="400"
                show-summary
                :summary-method="getSummaries"
              >
                <el-table-column prop="groupName" label="汇总分组" width="200" />
                <el-table-column prop="subjectCount" label="科目数量" width="100" align="right" />
                <el-table-column prop="beginningBalance" label="期初余额合计" width="150" align="right">
                  <template slot-scope="scope">
                    {{ formatAmount(scope.row.beginningBalance) }}
                  </template>
                </el-table-column>
                <el-table-column prop="debitAmount" label="借方发生额合计" width="150" align="right">
                  <template slot-scope="scope">
                    {{ formatAmount(scope.row.debitAmount) }}
                  </template>
                </el-table-column>
                <el-table-column prop="creditAmount" label="贷方发生额合计" width="150" align="right">
                  <template slot-scope="scope">
                    {{ formatAmount(scope.row.creditAmount) }}
                  </template>
                </el-table-column>
                <el-table-column prop="endingBalance" label="期末余额合计" width="150" align="right">
                  <template slot-scope="scope">
                    {{ formatAmount(scope.row.endingBalance) }}
                  </template>
                </el-table-column>
                <el-table-column label="操作" width="120" fixed="right">
                  <template slot-scope="scope">
                    <el-button type="text" size="small" @click="viewSummaryDetail(scope.row)">
                      查看明细
                    </el-button>
                  </template>
                </el-table-column>
              </el-table>

              <div class="pagination-container">
                <el-pagination
                  @size-change="handleSummarySizeChange"
                  @current-change="handleSummaryCurrentChange"
                  :current-page="summaryPagination.currentPage"
                  :page-sizes="[10, 20, 50, 100]"
                  :page-size="summaryPagination.pageSize"
                  layout="total, sizes, prev, pager, next, jumper"
                  :total="summaryPagination.total"
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
  name: 'LedgerQuery',
  data() {
    return {
      activeTab: 'subject',
      stats: {
        totalQueries: 0,
        totalSubjects: 0,
        totalAmount: 0,
        currentPeriod: ''
      },
      // 科目总账查询
      subjectForm: {
        subjectCode: '',
        subjectName: '',
        accountingPeriod: '',
        subjectType: ''
      },
      subjectTableData: [],
      subjectLoading: false,
      subjectPagination: {
        currentPage: 1,
        pageSize: 15,
        total: 0
      },
      // 多栏式总账查询
      multiColumnForm: {
        subjectCode: '',
        periodRange: [],
        displayType: 'month'
      },
      multiColumnTableData: [],
      multiColumnLoading: false,
      multiColumnPagination: {
        currentPage: 1,
        pageSize: 15,
        total: 0
      },
      periodColumns: [],
      // 辅助核算查询
      auxiliaryForm: {
        subjectCode: '',
        auxiliaryType: '',
        auxiliaryValue: '',
        accountingPeriod: ''
      },
      auxiliaryTableData: [],
      auxiliaryLoading: false,
      auxiliaryPagination: {
        currentPage: 1,
        pageSize: 15,
        total: 0
      },
      // 汇总查询
      summaryForm: {
        subjectLevel: '',
        subjectType: '',
        accountingPeriod: '',
        summaryType: 'subject'
      },
      summaryTableData: [],
      summaryLoading: false,
      summaryPagination: {
        currentPage: 1,
        pageSize: 15,
        total: 0
      }
    }
  },
  mounted() {
    this.loadStats()
    this.loadSubjectData()
  },
  methods: {
    async loadStats() {
      try {
        const response = await generalLedgerApi.getLedgerQueryStatistics({
          accountingPeriod: new Date().toISOString().slice(0, 7)
        })
        if (response.code === 1) {
          this.stats = response.data
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
      if (this.activeTab === 'subject') {
        this.loadSubjectData()
      } else if (this.activeTab === 'multiColumn') {
        this.loadMultiColumnData()
      } else if (this.activeTab === 'auxiliary') {
        this.loadAuxiliaryData()
      } else if (this.activeTab === 'summary') {
        this.loadSummaryData()
      }
    },
    exportData() {
      try {
        const data = this.subjectTableData || []
        if (data.length === 0) {
          this.$message.warning('暂无数据可导出')
          return
        }
        const jsonStr = JSON.stringify(data, null, 2)
        const blob = new Blob([jsonStr], { type: 'application/json' })
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = '总账查询数据导出.json'
        link.click()
        window.URL.revokeObjectURL(url)
        this.$message.success('导出成功')
      } catch (error) {
        this.$message.error('导出失败')
      }
    },
    handleTabClick(tab) {
      if (tab.name === 'subject') {
        this.loadSubjectData()
      } else if (tab.name === 'multiColumn') {
        this.loadMultiColumnData()
      } else if (tab.name === 'auxiliary') {
        this.loadAuxiliaryData()
      } else if (tab.name === 'summary') {
        this.loadSummaryData()
      }
    },

    // 科目总账查询方法
    async loadSubjectData() {
      this.subjectLoading = true
      try {
        const params = {
          pageNumber: this.subjectPagination.currentPage,
          pageSize: this.subjectPagination.pageSize,
          ...this.subjectForm
        }
        const response = await generalLedgerApi.getSubjectLedgerQuery(params)
        if (response.code === 1) {
          this.subjectTableData = response.data.tlist || []
          this.subjectPagination.total = response.data.totalRecord || 0
        } else {
          this.$message.error(response.msg || '查询失败')
        }
      } catch (error) {
        this.$message.error('查询失败：' + error.message)
      } finally {
        this.subjectLoading = false
      }
    },

    handleSubjectSearch() {
      this.subjectPagination.currentPage = 1
      this.loadSubjectData()
    },

    handleSubjectReset() {
      this.$refs.subjectForm.resetFields()
      this.subjectPagination.currentPage = 1
      this.loadSubjectData()
    },

    handleSubjectSizeChange(size) {
      this.subjectPagination.pageSize = size
      this.loadSubjectData()
    },

    handleSubjectCurrentChange(page) {
      this.subjectPagination.currentPage = page
      this.loadSubjectData()
    },

    viewSubjectDetail(row) {
      this.$message.info(`查看科目 ${row.subjectName} 的明细`)
    },

    viewSubjectTrend(row) {
      this.$message.info(`查看科目 ${row.subjectName} 的趋势分析`)
    },

    // 多栏式总账查询方法
    async loadMultiColumnData() {
      this.multiColumnLoading = true
      try {
        const params = {
          pageNumber: this.multiColumnPagination.currentPage,
          pageSize: this.multiColumnPagination.pageSize,
          ...this.multiColumnForm
        }
        const response = await generalLedgerApi.getMultiColumnLedgerQuery(params)
        if (response.code === 1) {
          this.multiColumnTableData = response.data.tlist || []
          this.multiColumnPagination.total = response.data.totalRecord || 0
          this.periodColumns = response.data.periodColumns || []
        } else {
          this.$message.error(response.msg || '查询失败')
        }
      } catch (error) {
        this.$message.error('查询失败：' + error.message)
      } finally {
        this.multiColumnLoading = false
      }
    },

    handleMultiColumnSearch() {
      this.multiColumnPagination.currentPage = 1
      this.loadMultiColumnData()
    },

    handleMultiColumnReset() {
      this.$refs.multiColumnForm.resetFields()
      this.multiColumnPagination.currentPage = 1
      this.loadMultiColumnData()
    },

    handleMultiColumnSizeChange(size) {
      this.multiColumnPagination.pageSize = size
      this.loadMultiColumnData()
    },

    handleMultiColumnCurrentChange(page) {
      this.multiColumnPagination.currentPage = page
      this.loadMultiColumnData()
    },

    // 辅助核算查询方法
    async loadAuxiliaryData() {
      this.auxiliaryLoading = true
      try {
        const params = {
          pageNumber: this.auxiliaryPagination.currentPage,
          pageSize: this.auxiliaryPagination.pageSize,
          ...this.auxiliaryForm
        }
        const response = await generalLedgerApi.getAuxiliaryLedgerQuery(params)
        if (response.code === 1) {
          this.auxiliaryTableData = response.data.tlist || []
          this.auxiliaryPagination.total = response.data.totalRecord || 0
        } else {
          this.$message.error(response.msg || '查询失败')
        }
      } catch (error) {
        this.$message.error('查询失败：' + error.message)
      } finally {
        this.auxiliaryLoading = false
      }
    },

    handleAuxiliarySearch() {
      this.auxiliaryPagination.currentPage = 1
      this.loadAuxiliaryData()
    },

    handleAuxiliaryReset() {
      this.$refs.auxiliaryForm.resetFields()
      this.auxiliaryPagination.currentPage = 1
      this.loadAuxiliaryData()
    },

    handleAuxiliarySizeChange(size) {
      this.auxiliaryPagination.pageSize = size
      this.loadAuxiliaryData()
    },

    handleAuxiliaryCurrentChange(page) {
      this.auxiliaryPagination.currentPage = page
      this.loadAuxiliaryData()
    },

    viewAuxiliaryDetail(row) {
      this.$message.info(`查看辅助核算 ${row.auxiliaryName} 的明细`)
    },

    getAuxiliaryTypeName(type) {
      const typeMap = {
        customer: '客户',
        supplier: '供应商',
        department: '部门',
        project: '项目',
        inventory: '存货'
      }
      return typeMap[type] || type
    },

    // 汇总查询方法
    async loadSummaryData() {
      this.summaryLoading = true
      try {
        const params = {
          pageNumber: this.summaryPagination.currentPage,
          pageSize: this.summaryPagination.pageSize,
          ...this.summaryForm
        }
        const response = await generalLedgerApi.getLedgerSummaryQuery(params)
        if (response.code === 1) {
          this.summaryTableData = response.data.tlist || []
          this.summaryPagination.total = response.data.totalRecord || 0
        } else {
          this.$message.error(response.msg || '查询失败')
        }
      } catch (error) {
        this.$message.error('查询失败：' + error.message)
      } finally {
        this.summaryLoading = false
      }
    },

    handleSummarySearch() {
      this.summaryPagination.currentPage = 1
      this.loadSummaryData()
    },

    handleSummaryReset() {
      this.$refs.summaryForm.resetFields()
      this.summaryPagination.currentPage = 1
      this.loadSummaryData()
    },

    handleSummarySizeChange(size) {
      this.summaryPagination.pageSize = size
      this.loadSummaryData()
    },

    handleSummaryCurrentChange(page) {
      this.summaryPagination.currentPage = page
      this.loadSummaryData()
    },

    viewSummaryDetail(row) {
      this.$message.info(`查看汇总分组 ${row.groupName} 的明细`)
    },

    getSummaries(param) {
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
          if (column.property.includes('Amount') || column.property.includes('Balance')) {
            sums[index] = this.formatAmount(sums[index])
          }
        } else {
          sums[index] = ''
        }
      })
      return sums
    }
  }
}
</script>

<style lang="scss" scoped>
.ledger-query-container {
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

      &.query {
        background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
      }

      &.subjects {
        background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
      }

      &.amount {
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      }

      &.period {
        background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
      }
    }

    .stat-content {
      .stat-value {
        font-size: 28px;
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

.query-tabs {
  background: white;
  border-radius: 12px;
  padding: 24px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);

  .tab-content {
    .search-form {
      background: #f8f9fa;
      border-radius: 8px;
      padding: 20px;
      margin-bottom: 20px;
    }

    .table-container {
      .pagination-container {
        margin-top: 20px;
        text-align: right;
      }
    }
  }
}
</style>
