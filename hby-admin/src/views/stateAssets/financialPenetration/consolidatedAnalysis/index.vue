<template>
  <div class="app-container financial-ext-page">
    <div class="page-header" :style="{ background: 'linear-gradient(135deg, ' + themeColor + 'cc 0%, ' + themeColor + ' 100%)' }">
      <div class="page-header-left"><i class="el-icon-s-finance"></i><span>合并分析</span></div>
      <div class="page-header-desc">穿透分析企业合并报表、抵消分录与合并范围变动</div>
    </div>
    <!-- 统计概览卡片 -->
    <el-row :gutter="20" class="mb-20">
      <el-col :span="6">
        <el-card class="statistics-card">
          <div class="statistics-content">
            <div class="statistics-icon">
              <i class="el-icon-s-finance" style="color: #eb2f96"></i>
            </div>
            <div class="statistics-info">
              <div class="statistics-number">
                {{ statistics.totalAnalysis || 0 }}
              </div>
              <div class="statistics-label">合并分析总数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="statistics-card">
          <div class="statistics-content">
            <div class="statistics-icon">
              <i class="el-icon-data-board" style="color: #67c23a"></i>
            </div>
            <div class="statistics-info">
              <div class="statistics-number">
                {{ statistics.consolidatedEntities || 0 }}
              </div>
              <div class="statistics-label">合并主体数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="statistics-card">
          <div class="statistics-content">
            <div class="statistics-icon">
              <i class="el-icon-warning" style="color: #e6a23c"></i>
            </div>
            <div class="statistics-info">
              <div class="statistics-number">
                {{ statistics.riskCount || 0 }}
              </div>
              <div class="statistics-label">风险项目</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="statistics-card">
          <div class="statistics-content">
            <div class="statistics-icon">
              <i class="el-icon-money" style="color: #f56c6c"></i>
            </div>
            <div class="statistics-info">
              <div class="statistics-number">
                {{ formatAmount(statistics.totalRevenue) }}
              </div>
              <div class="statistics-label">合并收入(万元)</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 查询表单 -->
    <el-card class="mb-20">
      <el-form
        :model="queryForm"
        ref="queryForm"
        :inline="true"
        label-width="100px"
      >
        <el-form-item label="企业选择" v-if="isFilterVisible('companyId')">
          <el-input
            v-model="queryForm.companyName"
            placeholder="请选择企业"
            readonly
            style="width: 200px; cursor: pointer"
            @click.native="openCompanyTree"
          >
            <i slot="suffix" class="el-input__icon el-icon-search" style="cursor: pointer" @click="openCompanyTree"></i>
          </el-input>
          <CompanyTreeModal ref="companyTreeModal" @selected="handleCompanySelected" />
        </el-form-item>
        <el-form-item label="报表类型" v-if="isFilterVisible('statementType')">
          <el-select
            v-model="queryForm.statementType"
            placeholder="请选择报表类型"
            clearable
            style="width: 150px"
          >
            <el-option label="合并" value="合并"></el-option>
            <el-option label="单体" value="单体"></el-option>
            <el-option label="资产负债表" value="BALANCE_SHEET"></el-option>
            <el-option label="利润表" value="INCOME_STATEMENT"></el-option>
            <el-option label="现金流量表" value="CASH_FLOW"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="审计状态" v-if="isFilterVisible('auditStatus')">
          <el-select
            v-model="queryForm.auditStatus"
            placeholder="请选择审计状态"
            clearable
            style="width: 150px"
          >
            <el-option label="已审计" value="APPROVED"></el-option>
            <el-option label="未审计" value="UNAUDITED"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="报告期间" v-if="isFilterVisible('period')">
          <el-input v-model="queryForm.period" clearable placeholder="如：2025-Q1" style="width: 160px" />
        </el-form-item>
        <el-form-item label="收入区间" v-if="isFilterVisible('revenue')">
          <el-input v-model="queryForm.minTotalRevenue" clearable placeholder="最小值" style="width: 100px" />
          <span style="margin: 0 10px">-</span>
          <el-input v-model="queryForm.maxTotalRevenue" clearable placeholder="最大值" style="width: 100px" />
        </el-form-item>
        <el-form-item label="资产区间" v-if="isFilterVisible('assets')">
          <el-input v-model="queryForm.minTotalAssets" clearable placeholder="最小值" style="width: 100px" />
          <span style="margin: 0 10px">-</span>
          <el-input v-model="queryForm.maxTotalAssets" clearable placeholder="最大值" style="width: 100px" />
        </el-form-item>
        <el-form-item label="负债区间" v-if="isFilterVisible('liabilities')">
          <el-input v-model="queryForm.minTotalLiabilities" clearable placeholder="最小值" style="width: 100px" />
          <span style="margin: 0 10px">-</span>
          <el-input v-model="queryForm.maxTotalLiabilities" clearable placeholder="最大值" style="width: 100px" />
        </el-form-item>
        <el-form-item label="净资产区间" v-if="isFilterVisible('netAssets')">
          <el-input v-model="queryForm.minNetAssets" clearable placeholder="最小值" style="width: 100px" />
          <span style="margin: 0 10px">-</span>
          <el-input v-model="queryForm.maxNetAssets" clearable placeholder="最大值" style="width: 100px" />
        </el-form-item>
        <el-form-item label="净利润区间" v-if="isFilterVisible('netProfit')">
          <el-input v-model="queryForm.minNetProfit" clearable placeholder="最小值" style="width: 100px" />
          <span style="margin: 0 10px">-</span>
          <el-input v-model="queryForm.maxNetProfit" clearable placeholder="最大值" style="width: 100px" />
        </el-form-item>
        <el-form-item label="现金流区间" v-if="isFilterVisible('cashflow')">
          <el-input v-model="queryForm.minOperatingCashflow" clearable placeholder="最小值" style="width: 100px" />
          <span style="margin: 0 10px">-</span>
          <el-input v-model="queryForm.maxOperatingCashflow" clearable placeholder="最大值" style="width: 100px" />
        </el-form-item>
        <el-form-item>
          <!-- 筛选条件选择器 - 小方块漏斗图标 -->
          <el-popover
            placement="bottom"
            width="280"
            trigger="click"
          >
            <div class="filter-selector-panel">
              <div class="filter-selector-title">选择筛选条件</div>
              <el-checkbox-group v-model="activeFilters">
                <el-checkbox
                  v-for="item in filterOptions"
                  :key="item.value"
                  :label="item.value"
                  class="filter-checkbox-item"
                >{{ item.label }}</el-checkbox>
              </el-checkbox-group>
              <div class="filter-selector-footer">
                <el-button size="mini" @click="activeFilters = filterOptions.map(i => i.value)">全选</el-button>
                <el-button size="mini" @click="activeFilters = ['companyId', 'statementType']">重置</el-button>
              </div>
            </div>
            <el-button slot="reference" class="filter-icon-btn" title="筛选条件">
              <i class="el-icon-set-up"></i>
            </el-button>
          </el-popover>
          <el-button type="primary" @click="handleQuery" icon="el-icon-search" style="margin-left: 10px">
            查询
          </el-button>
          <el-button @click="resetQuery" icon="el-icon-refresh">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 数据表格 -->
    <el-card>
      <!-- 表格工具栏 - 列筛选 + 操作按钮 右对齐 -->
      <div class="table-toolbar">
        <el-popover
          placement="bottom-start"
          width="200"
          trigger="hover"
        >
          <el-checkbox-group v-model="checkedColumns" class="column-dropdown-list">
            <el-checkbox
              v-for="item in columnOptions"
              :key="item.prop"
              :label="item.prop"
              class="column-dropdown-item"
            >{{ item.label }}</el-checkbox>
          </el-checkbox-group>
          <div slot="reference" class="column-icon-btn" title="表格列筛选">
            <i class="el-icon-s-grid"></i>
          </div>
        </el-popover>
        <el-button size="small" type="success" @click="openAddDialog" icon="el-icon-plus">新增分析</el-button>
        <el-button size="small" type="warning" @click="handleBatchAnalyze" icon="el-icon-s-finance" :disabled="!multipleSelection.length">批量分析</el-button>
        <el-button size="small" type="info" @click="handleExport" icon="el-icon-download">导出</el-button>
      </div>

      <el-table
        v-loading="loading"
        :data="tableData"
        @selection-change="handleSelectionChange"
        @sort-change="handleSortChange"
        stripe
        border
        style="width: 100%"
      >
        <el-table-column
          type="selection"
          width="55"
          align="center"
        ></el-table-column>
        <el-table-column
          v-if="isColumnVisible('companyName')"
          prop="companyName"
          label="企业名称"
          min-width="200"
          show-overflow-tooltip
        ></el-table-column>
        <el-table-column
          v-if="isColumnVisible('statementType')"
          prop="statementType"
          label="报表类型"
          width="120"
          align="center"
        >
          <template slot-scope="scope">
            <el-tag type="primary">{{ scope.row.statementType || '-' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column
          v-if="isColumnVisible('period')"
          prop="period"
          label="报告期间"
          width="120"
          align="center"
        ></el-table-column>
        <el-table-column
          v-if="isColumnVisible('totalRevenue')"
          prop="totalRevenue"
          label="合并收入(万元)"
          width="140"
          align="center"
          sortable="custom"
        >
          <template slot-scope="scope">
            <span>{{ formatAmount(scope.row.totalRevenue) }}</span>
          </template>
        </el-table-column>
        <el-table-column
          v-if="isColumnVisible('totalAssets')"
          prop="totalAssets"
          label="合并资产(万元)"
          width="140"
          align="center"
          sortable="custom"
        >
          <template slot-scope="scope">
            <span>{{ formatAmount(scope.row.totalAssets) }}</span>
          </template>
        </el-table-column>
        <el-table-column
          v-if="isColumnVisible('totalLiabilities')"
          prop="totalLiabilities"
          label="负债总额(万元)"
          width="140"
          align="center"
          sortable="custom"
        >
          <template slot-scope="scope">
            <span>{{ formatAmount(scope.row.totalLiabilities) }}</span>
          </template>
        </el-table-column>
        <el-table-column
          v-if="isColumnVisible('netAssets')"
          prop="netAssets"
          label="净资产(万元)"
          width="130"
          align="center"
          sortable="custom"
        >
          <template slot-scope="scope">
            <span>{{ formatAmount(scope.row.netAssets) }}</span>
          </template>
        </el-table-column>
        <el-table-column
          v-if="isColumnVisible('netProfit')"
          prop="netProfit"
          label="净利润(万元)"
          width="130"
          align="center"
          sortable="custom"
        >
          <template slot-scope="scope">
            <span>{{ formatAmount(scope.row.netProfit) }}</span>
          </template>
        </el-table-column>
        <el-table-column
          v-if="isColumnVisible('operatingCashflow')"
          prop="operatingCashflow"
          label="经营现金流(万元)"
          width="150"
          align="center"
          sortable="custom"
        >
          <template slot-scope="scope">
            <span>{{ formatAmount(scope.row.operatingCashflow) }}</span>
          </template>
        </el-table-column>
        <el-table-column
          v-if="isColumnVisible('auditStatus')"
          prop="auditStatus"
          label="审计状态"
          width="100"
          align="center"
        >
          <template slot-scope="scope">
            <el-tag :type="getAuditStatusTagType(scope.row.auditStatus)">
              {{ formatAuditStatus(scope.row.auditStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" align="center" fixed="right">
          <template slot-scope="scope">
            <div class="action-grid">
              <el-button size="mini" @click="handleView(scope.row)" icon="el-icon-view">查看</el-button>
              <el-button size="mini" type="primary" @click="handleAnalyze(scope.row)" icon="el-icon-s-finance">分析</el-button>
              <el-button size="mini" type="success" @click="handleViewStatement(scope.row)" icon="el-icon-data-board">报表</el-button>
              <el-dropdown @command="handleCommand" trigger="click">
                <el-button size="mini" type="info">更多<i class="el-icon-arrow-down el-icon--right"></i></el-button>
                <el-dropdown-menu slot="dropdown">
                  <el-dropdown-item :command="{ action: 'edit', row: scope.row }" icon="el-icon-edit">编辑</el-dropdown-item>
                  <el-dropdown-item :command="{ action: 'trend', row: scope.row }" icon="el-icon-trend-charts">趋势分析</el-dropdown-item>
                  <el-dropdown-item :command="{ action: 'compare', row: scope.row }" icon="el-icon-s-data">对比分析</el-dropdown-item>
                  <el-dropdown-item :command="{ action: 'predict', row: scope.row }" icon="el-icon-cpu">预测分析</el-dropdown-item>
                  <el-dropdown-item :command="{ action: 'report', row: scope.row }" icon="el-icon-document">生成报告</el-dropdown-item>
                  <el-dropdown-item :command="{ action: 'delete', row: scope.row }" icon="el-icon-delete" divided>删除</el-dropdown-item>
                </el-dropdown-menu>
              </el-dropdown>
            </div>
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
    </el-card>

    <!-- 合并财务分析对话框 -->
    <ConsolidatedAnalysisDialog
      :visible.sync="dialogVisible"
      :form-data="currentRow"
      :dialog-type="dialogType"
      @refresh="getList"
    />

    <!-- 合并报表对话框 -->
    <ConsolidatedStatementDialog
      :visible.sync="statementDialogVisible"
      :analysis-data="currentRow"
    />

    <!-- 趋势分析对话框 -->
    <ConsolidatedTrendDialog
      :visible.sync="trendDialogVisible"
      :analysis-data="currentRow"
    />

    <!-- 对比分析对话框 -->
    <ConsolidatedCompareDialog
      :visible.sync="compareDialogVisible"
      :analysis-data="currentRow"
    />

    <!-- 预测分析对话框 -->
    <ConsolidatedPredictDialog
      :visible.sync="predictDialogVisible"
      :analysis-data="currentRow"
    />
  </div>
</template>

<script>
  import {
    getConsolidatedAnalysisList,
    deleteConsolidatedAnalysis,
    performConsolidatedAnalysis,
    getConsolidatedStatistics,
    batchPerformConsolidated,
    exportConsolidatedData,
    buildConsolidatedStatement,
    analyzeConsolidatedTrend,
    compareConsolidatedData,
    predictConsolidatedPerformance,
    generateConsolidatedReport,
  } from '@/api/stateAssets/consolidatedAnalysis'
  import CompanyTreeModal from '@/components/CompanyTreeModal'
import { mapGetters } from 'vuex'
  import Pagination from '@/components/Pagination'
  import ConsolidatedAnalysisDialog from './components/ConsolidatedAnalysisDialog'
  import ConsolidatedStatementDialog from './components/ConsolidatedStatementDialog'
  import ConsolidatedTrendDialog from './components/ConsolidatedTrendDialog'
  import ConsolidatedCompareDialog from './components/ConsolidatedCompareDialog'
  import ConsolidatedPredictDialog from './components/ConsolidatedPredictDialog'

  const createDefaultQueryForm = () => ({
    pageNumber: 1,
    pageSize: 10,
    companyId: '',
    companyName: '',
    statementType: '',
    auditStatus: '',
    period: '',
    minTotalRevenue: '',
    maxTotalRevenue: '',
    minTotalAssets: '',
    maxTotalAssets: '',
    minTotalLiabilities: '',
    maxTotalLiabilities: '',
    minNetAssets: '',
    maxNetAssets: '',
    minNetProfit: '',
    maxNetProfit: '',
    minOperatingCashflow: '',
    maxOperatingCashflow: '',
  })

  export default {
  computed: {
    ...mapGetters({ theme: 'settings/theme' }),
    themeColor() {
      const map = { red: '#e50113', green: '#41b584', ocean: '#1890ff', white: '#1890ff', default: '#1890ff' }
      return map[(this.theme && this.theme.themeName) || 'default'] || '#1890ff'
    },
    themeColorLight() {
      const map = { red: '#fff1f0', green: '#f6ffed', ocean: '#e6f7ff', white: '#e6f7ff', default: '#e6f7ff' }
      return map[(this.theme && this.theme.themeName) || 'default'] || '#e6f7ff'
    },
  },
    name: 'ConsolidatedAnalysis',
    components: {
      CompanyTreeModal,
      Pagination,
      ConsolidatedAnalysisDialog,
      ConsolidatedStatementDialog,
      ConsolidatedTrendDialog,
      ConsolidatedCompareDialog,
      ConsolidatedPredictDialog,
    },
    data() {
      return {
        loading: false,
        tableData: [],
        total: 0,
        multipleSelection: [],
        statistics: {},
        columnOptions: [
          { prop: 'companyName', label: '企业名称' },
          { prop: 'statementType', label: '报表类型' },
          { prop: 'period', label: '报告期间' },
          { prop: 'totalRevenue', label: '合并收入(万元)' },
          { prop: 'totalAssets', label: '合并资产(万元)' },
          { prop: 'totalLiabilities', label: '负债总额(万元)' },
          { prop: 'netAssets', label: '净资产(万元)' },
          { prop: 'netProfit', label: '净利润(万元)' },
          { prop: 'operatingCashflow', label: '经营现金流(万元)' },
          { prop: 'auditStatus', label: '审计状态' },
        ],
        checkedColumns: [
          'companyName',
          'statementType',
          'period',
          'totalRevenue',
          'totalAssets',
          'totalLiabilities',
          'netAssets',
          'netProfit',
          'operatingCashflow',
          'auditStatus',
        ],
        // 筛选条件选择器
        filterOptions: [
          { value: 'companyId', label: '企业选择' },
          { value: 'statementType', label: '报表类型' },
          { value: 'auditStatus', label: '审计状态' },
          { value: 'period', label: '报告期间' },
          { value: 'revenue', label: '收入区间' },
          { value: 'assets', label: '资产区间' },
          { value: 'liabilities', label: '负债区间' },
          { value: 'netAssets', label: '净资产区间' },
          { value: 'netProfit', label: '净利润区间' },
          { value: 'cashflow', label: '现金流区间' },
        ],
        activeFilters: ['companyId', 'statementType', 'auditStatus', 'period'],
        queryForm: createDefaultQueryForm(),
        dialogVisible: false,
        statementDialogVisible: false,
        trendDialogVisible: false,
        compareDialogVisible: false,
        predictDialogVisible: false,
        dialogType: 'add',
        currentRow: {},
      }
    },
    created() {
      this.getList()
      this.getStatistics()
    },
    methods: {
      // 判断筛选条件是否可见
      isFilterVisible(filterKey) {
        return this.activeFilters.indexOf(filterKey) !== -1
      },

      // 打开企业选择弹窗
      openCompanyTree() {
        this.$refs.companyTreeModal.show()
      },

      // 企业选择回调
      handleCompanySelected(node) {
        if (node) {
          this.queryForm.companyId = node.id || ''
          this.queryForm.companyName = node.label || node.name || ''
        }
      },

      // 获取列表数据
      getList() {
        this.loading = true
        getConsolidatedAnalysisList(this.queryForm)
          .then((response) => {
            if (response && response.data) {
              this.tableData = response.data.records || response.data.list || []
              this.total = response.data.total || 0
              // 列表加载完成后更新统计
              if (!this.statistics || !this.statistics.totalAnalysis) {
                this.calcStatisticsFromList()
              }
            } else {
              this.tableData = []
              this.total = 0
              console.warn('获取列表失败:', response)
            }
            this.loading = false
          })
          .catch((error) => {
            console.error('获取列表异常:', error)
            this.tableData = []
            this.total = 0
            this.loading = false
          })
      },

      // 获取统计数据 - 从列表数据中提取
      getStatistics() {
        getConsolidatedStatistics(this.queryForm).then((response) => {
          if (response && response.data) {
            this.statistics = response.data
          } else {
            // 从列表数据中提取统计
            this.calcStatisticsFromList()
          }
        }).catch(() => {
          this.calcStatisticsFromList()
        })
      },

      // 从列表数据计算统计
      calcStatisticsFromList() {
        const list = this.tableData || []
        this.statistics = {
          totalAnalysis: this.total || list.length,
          consolidatedEntities: [...new Set(list.map(i => i.companyId))].length,
          riskCount: list.filter(i => ['UNAUDITED', '未审计', 'PENDING'].indexOf(i.auditStatus) !== -1).length,
          totalRevenue: list.reduce((sum, i) => sum + (i.totalRevenue || 0), 0),
        }
      },

      // 查询
      handleQuery() {
        this.queryForm.pageNumber = 1
        this.getList()
        this.getStatistics()
      },

      // 重置查询
      resetQuery() {
        this.$refs.queryForm.resetFields()
        this.queryForm = createDefaultQueryForm()
        this.getList()
        this.getStatistics()
      },

      // 新增
      openAddDialog() {
        this.currentRow = {}
        this.dialogType = 'add'
        this.dialogVisible = true
      },

      // 查看详情
      handleView(row) {
        this.currentRow = { ...row }
        this.dialogType = 'view'
        this.dialogVisible = true
      },

      // 查看报表
      handleViewStatement(row) {
        const loading = this.$loading({ lock: true, text: '正在加载报表...', spinner: 'el-icon-loading' })
        buildConsolidatedStatement({ id: this.getRowId(row) })
          .then((response) => {
            const data = this.getResponseData(response)
            this.currentRow = data ? { ...row, ...data } : { ...row }
            this.statementDialogVisible = true
          })
          .finally(() => loading.close())
      },

      // 执行分析
      handleAnalyze(row) {
        this.$confirm('确认对该记录执行合并财务分析？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'info',
        }).then(() => {
          const loading = this.$loading({
            lock: true,
            text: '正在执行分析...',
            spinner: 'el-icon-loading',
            background: 'rgba(0, 0, 0, 0.7)',
          })
          performConsolidatedAnalysis({ id: this.getRowId(row) })
            .then((response) => {
              loading.close()
              if (!this.isSuccess(response)) {
                this.$message.error(response.msg || '分析失败')
                return
              }
              this.$message.success('分析完成')
              this.getList()
              this.getStatistics()
            })
            .catch(() => {
              loading.close()
            })
        })
      },

      // 批量分析
      handleBatchAnalyze() {
        if (!this.multipleSelection.length) {
          this.$message.warning('请选择要分析的记录')
          return
        }
        const ids = this.multipleSelection.map(
          (item) => this.getRowId(item)
        )
        this.$confirm(
          `确认对选中的 ${ids.length} 条记录执行批量合并分析？`,
          '提示',
          {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'info',
          }
        ).then(() => {
          const loading = this.$loading({
            lock: true,
            text: '正在执行批量分析...',
            spinner: 'el-icon-loading',
            background: 'rgba(0, 0, 0, 0.7)',
          })
          batchPerformConsolidated({ ids })
            .then((response) => {
              loading.close()
              if (!this.isSuccess(response)) {
                this.$message.error(response.msg || '批量分析失败')
                return
              }
              this.$message.success('批量分析完成')
              this.getList()
              this.getStatistics()
            })
            .catch(() => {
              loading.close()
            })
        })
      },

      // 导出数据
      handleExport() {
        const loading = this.$loading({
          lock: true,
          text: '正在导出数据...',
          spinner: 'el-icon-loading',
          background: 'rgba(0, 0, 0, 0.7)',
        })

        exportConsolidatedData(this.queryForm)
          .then((response) => {
            loading.close()
            if (!this.isSuccess(response)) {
              this.$message.error(response.msg || '导出失败')
              return
            }
            const data = this.getResponseData(response) || {}
            const rows = data.data || data.rows || []
            if (this.downloadCsv(rows, `合并财务分析数据_${new Date().getTime()}.csv`)) {
              this.$message.success('数据导出成功')
            }
          })
          .catch(() => {
            loading.close()
          })
      },

      // 下拉菜单命令处理
      handleCommand(command) {
        const { action, row } = command
        switch (action) {
          case 'edit':
            this.currentRow = { ...row }
            this.dialogType = 'edit'
            this.dialogVisible = true
            break
          case 'trend':
            this.openTrendDialog(row)
            break
          case 'compare':
            this.openCompareDialog(row)
            break
          case 'predict':
            this.openPredictDialog(row)
            break
          case 'report':
            this.generateReport(row)
            break
          case 'delete':
            this.deleteRow(row)
            break
        }
      },

      openTrendDialog(row) {
        const loading = this.$loading({ lock: true, text: '正在加载趋势数据...', spinner: 'el-icon-loading' })
        analyzeConsolidatedTrend({ companyId: row.companyId })
          .then((response) => {
            if (!this.isSuccess(response)) {
              this.$message.error(response.msg || '趋势分析加载失败')
              return
            }
            const data = this.getResponseData(response) || {}
            this.currentRow = { ...row, trendRows: data.rows || [] }
            this.trendDialogVisible = true
          })
          .finally(() => loading.close())
      },

      openCompareDialog(row) {
        const loading = this.$loading({ lock: true, text: '正在加载对比数据...', spinner: 'el-icon-loading' })
        compareConsolidatedData({ id: this.getRowId(row) })
          .then((response) => {
            if (!this.isSuccess(response)) {
              this.$message.error(response.msg || '对比分析加载失败')
              return
            }
            const data = this.getResponseData(response) || {}
            this.currentRow = { ...row, compareData: data }
            this.compareDialogVisible = true
          })
          .finally(() => loading.close())
      },

      openPredictDialog(row) {
        const loading = this.$loading({ lock: true, text: '正在加载预测数据...', spinner: 'el-icon-loading' })
        predictConsolidatedPerformance({ companyId: row.companyId })
          .then((response) => {
            if (!this.isSuccess(response)) {
              this.$message.error(response.msg || '预测分析加载失败')
              return
            }
            const data = this.getResponseData(response) || {}
            this.currentRow = { ...row, predictRows: data.rows || [] }
            this.predictDialogVisible = true
          })
          .finally(() => loading.close())
      },

      // 生成报告
      generateReport(row) {
        this.$confirm('确认生成该记录的合并财务分析报告？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'info',
        }).then(() => {
          const loading = this.$loading({
            lock: true,
            text: '正在生成报告...',
            spinner: 'el-icon-loading',
            background: 'rgba(0, 0, 0, 0.7)',
          })
          generateConsolidatedReport({ id: this.getRowId(row) })
            .then((response) => {
              loading.close()
              if (!this.isSuccess(response)) {
                this.$message.error(response.msg || '报告生成失败')
                return
              }
              const data = this.getResponseData(response) || {}
              const rows = [
                data.summary || row,
                {
                  companyName: data.companyName,
                  period: data.period,
                  statementType: '分析指标',
                  totalRevenue: data.assetLiabilityRatio,
                  totalAssets: data.netProfitMargin,
                  totalLiabilities: data.cashRevenueRatio,
                  auditStatus: (data.riskTips || []).join('；'),
                },
              ]
              if (this.downloadCsv(rows, `合并财务分析报告_${row.companyName || ''}_${new Date().getTime()}.csv`)) {
                this.$message.success('报告生成成功')
              }
            })
            .catch(() => {
              loading.close()
            })
        })
      },

      // 删除
      deleteRow(row) {
        this.$confirm('确认删除该合并财务分析记录？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        }).then(() => {
          deleteConsolidatedAnalysis(this.getRowId(row)).then(
            (response) => {
              if (!this.isSuccess(response)) {
                this.$message.error(response.msg || '删除失败')
                return
              }
              this.$message.success('删除成功')
              this.getList()
              this.getStatistics()
            }
          )
        })
      },

      // 多选变化
      handleSelectionChange(selection) {
        this.multipleSelection = selection
      },

      // 排序变化
      handleSortChange({ prop, order }) {
        this.queryForm.orderBy = prop
        this.queryForm.orderDirection = order === 'ascending' ? 'ASC' : 'DESC'
        this.getList()
      },

      getRowId(row) {
        return row && (row.id || row.statementId || row.consolidatedAnalysisId)
      },

      getResponseData(response) {
        return response && response.data ? response.data : response
      },

      isSuccess(response) {
        if (!response) return true
        return response.code === undefined && response.result === undefined
          ? true
          : [1, '1', 200, '200'].indexOf(response.code) !== -1 || [1, '1', 200, '200'].indexOf(response.result) !== -1
      },

      isColumnVisible(prop) {
        return this.checkedColumns.indexOf(prop) !== -1
      },

      formatAuditStatus(status) {
        const map = {
          APPROVED: '已审计',
          UNAUDITED: '未审计',
          PENDING: '待审计',
          REJECTED: '已驳回',
        }
        return map[status] || status || '-'
      },

      getAuditStatusTagType(status) {
        if (status === 'APPROVED' || status === '已审计') return 'success'
        if (status === 'REJECTED') return 'danger'
        return 'warning'
      },

      downloadCsv(rows, fileName) {
        if (!rows || !rows.length) {
          this.$message.warning('暂无可导出的数据')
          return false
        }
        const columns = this.columnOptions.filter(item => this.isColumnVisible(item.prop))
        if (!columns.length) {
          this.$message.warning('请至少选择一个导出字段')
          return false
        }
        const header = columns.map(item => item.label).join(',')
        const body = rows.map(row => columns.map(col => this.escapeCsv(this.formatExportValue(row, col.prop))).join(',')).join('\n')
        const blob = new Blob(['\ufeff' + header + '\n' + body], { type: 'text/csv;charset=utf-8;' })
        const link = document.createElement('a')
        link.href = URL.createObjectURL(blob)
        link.download = fileName
        link.click()
        URL.revokeObjectURL(link.href)
        return true
      },

      formatExportValue(row, prop) {
        if (prop === 'auditStatus') return this.formatAuditStatus(row[prop])
        const value = row[prop]
        return value === undefined || value === null ? '' : value
      },

      escapeCsv(value) {
        const text = String(value)
        return /[",\n]/.test(text) ? `"${text.replace(/"/g, '""')}"` : text
      },


      // 格式化金额（API返回的已是万元单位，直接格式化显示）
      formatAmount(amount) {
        if (!amount && amount !== 0) return '-'
        if (amount >= 10000) {
          return (amount / 10000).toFixed(2) + '亿'
        }
        return Number(amount).toLocaleString('zh-CN', { minimumFractionDigits: 2, maximumFractionDigits: 2 })
      },
    },
  }
</script>

<style lang="scss" scoped>
.financial-ext-page { padding: 16px; background: #f0f2f5; min-height: calc(100vh - 84px); }
.page-header { display: flex; align-items: center; justify-content: space-between; margin-bottom: 14px; padding: 14px 20px; border-radius: 6px; color: #fff; }
.page-header-left { display: flex; align-items: center; font-size: 16px; font-weight: 600; i { font-size: 22px; margin-right: 10px; } }
.page-header-desc { font-size: 13px; opacity: 0.85; }
::v-deep .el-table th { background: #fff0f6 !important; }
::v-deep .el-card { border-radius: 6px; }
.statistics-card { margin-bottom: 20px; }
.statistics-content { display: flex; align-items: center; }
.statistics-icon { font-size: 40px; margin-right: 20px; }
.statistics-info { flex: 1; }
.statistics-number { font-size: 24px; font-weight: bold; color: #303133; line-height: 1; }
.statistics-label { font-size: 14px; color: #909399; margin-top: 5px; }
.mb-20 { margin-bottom: 20px; }

/* 筛选条件选择器面板 */
.filter-selector-panel {
  .filter-selector-title {
    font-size: 13px;
    font-weight: 600;
    color: #303133;
    margin-bottom: 8px;
    padding-bottom: 6px;
    border-bottom: 1px solid #ebeef5;
  }
  .filter-checkbox-item {
    display: block;
    margin-left: 0 !important;
    margin-bottom: 4px;
  }
  .filter-selector-footer {
    margin-top: 8px;
    padding-top: 6px;
    border-top: 1px solid #ebeef5;
    text-align: right;
  }
}

/* 筛选条件 - 小方块按钮 */
.filter-icon-btn {
  width: 32px !important;
  height: 32px !important;
  padding: 0 !important;
  display: inline-flex !important;
  align-items: center;
  justify-content: center;
  border-radius: 4px;
  border: 1px solid #dcdfe6;
  cursor: pointer;
  transition: all 0.2s;
  i { font-size: 16px; color: #606266; }
  &:hover {
    border-color: #409eff;
    background: #ecf5ff;
    i { color: #409eff; }
  }
}

/* 表格列筛选 - 小方块图标 + hover下拉 */
.table-toolbar {
  display: flex;
  align-items: center;
  justify-content: flex-end;
  gap: 10px;
  margin-bottom: 10px;
}
.column-icon-btn {
  width: 32px;
  height: 32px;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  border-radius: 4px;
  border: 1px solid #dcdfe6;
  cursor: pointer;
  transition: all 0.2s;
  i { font-size: 16px; color: #606266; }
  &:hover {
    border-color: #409eff;
    background: #ecf5ff;
    i { color: #409eff; }
  }
}
.column-dropdown-list {
  display: flex;
  flex-direction: column;
  .column-dropdown-item {
    margin-left: 0 !important;
    margin-bottom: 4px;
    &:last-child { margin-bottom: 0; }
  }
}

/* 操作列 2x2 网格布局 */
.action-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 4px;
  .el-button {
    margin-left: 0 !important;
    padding: 5px 8px;
    font-size: 12px;
  }
  .el-dropdown {
    .el-button {
      width: 100%;
    }
  }
}
</style>
