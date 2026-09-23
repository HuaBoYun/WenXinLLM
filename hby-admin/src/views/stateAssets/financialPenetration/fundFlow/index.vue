<template>
  <div class="app-container financial-ext-page">
    <div class="page-header" :style="{ background: 'linear-gradient(135deg, ' + themeColor + 'cc 0%, ' + themeColor + ' 100%)' }">
      <div class="page-header-left"><i class="el-icon-guide"></i><span>资金流向</span></div>
      <div class="page-header-desc">追踪企业资金流入流出路径、异常资金流向预警</div>
    </div>
    <!-- 统计概览卡片 -->
    <el-row :gutter="20" class="mb-20">
      <el-col :span="6">
        <el-card class="statistics-card">
          <div class="statistics-content">
            <div class="statistics-icon">
              <i class="el-icon-guide" style="color: #eb2f96"></i>
            </div>
            <div class="statistics-info">
              <div class="statistics-number">
                {{ statistics.totalFlows || 0 }}
              </div>
              <div class="statistics-label">资金流向总数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="statistics-card">
          <div class="statistics-content">
            <div class="statistics-icon">
              <i class="el-icon-money" style="color: #67c23a"></i>
            </div>
            <div class="statistics-info">
              <div class="statistics-number">
                {{ formatAmount(statistics.totalAmount) }}
              </div>
              <div class="statistics-label">流转总额(万元)</div>
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
                {{ statistics.abnormalCount || 0 }}
              </div>
              <div class="statistics-label">异常流向</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="statistics-card">
          <div class="statistics-content">
            <div class="statistics-icon">
              <i class="el-icon-sort" style="color: #f56c6c"></i>
            </div>
            <div class="statistics-info">
              <div class="statistics-number">
                {{ statistics.avgVelocity || 0 }}
              </div>
              <div class="statistics-label">平均流转速度</div>
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
        <el-form-item label="企业选择" v-if="isFilterVisible('enterprise')">
          <el-input
            v-model="queryForm.enterpriseName"
            placeholder="请选择企业"
            readonly
            style="width: 200px; cursor: pointer"
            @click.native="openCompanyTree"
          >
            <i slot="suffix" class="el-input__icon el-icon-search" style="cursor: pointer" @click="openCompanyTree"></i>
          </el-input>
          <CompanyTreeModal ref="companyTreeModal" @selected="handleCompanySelected" />
        </el-form-item>
        <el-form-item label="流向类型" v-if="isFilterVisible('flowType')">
          <el-select
            v-model="queryForm.flowType"
            placeholder="请选择流向类型"
            clearable
            style="width: 150px"
          >
            <el-option label="资金流入" value="INFLOW"></el-option>
            <el-option label="资金流出" value="OUTFLOW"></el-option>
            <el-option label="内部转移" value="INTERNAL_TRANSFER"></el-option>
            <el-option label="投资收回" value="INVESTMENT_RECOVERY"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="资金性质" v-if="isFilterVisible('fundNature')">
          <el-select
            v-model="queryForm.fundNature"
            placeholder="请选择资金性质"
            clearable
            style="width: 150px"
          >
            <el-option label="经营性" value="OPERATING"></el-option>
            <el-option label="投资性" value="INVESTING"></el-option>
            <el-option label="筹资性" value="FINANCING"></el-option>
            <el-option label="其他" value="OTHER"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="流转金额" v-if="isFilterVisible('amount')">
          <el-input
            v-model="queryForm.minAmount"
            placeholder="最小金额"
            style="width: 100px"
          ></el-input>
          <span style="margin: 0 10px">-</span>
          <el-input
            v-model="queryForm.maxAmount"
            placeholder="最大金额"
            style="width: 100px"
          ></el-input>
        </el-form-item>
        <el-form-item label="流转时间" v-if="isFilterVisible('flowPeriod')">
          <el-date-picker
            v-model="queryForm.flowPeriod"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            style="width: 240px"
          ></el-date-picker>
        </el-form-item>
        <el-form-item>
          <!-- 筛选条件选择器 -->
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
                <el-button size="mini" @click="activeFilters = ['enterprise', 'flowType']">重置</el-button>
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
        <el-button size="small" type="success" @click="handleAdd" icon="el-icon-plus">新增追踪</el-button>
        <el-button size="small" type="warning" @click="handleBatchTrace" icon="el-icon-guide" :disabled="!multipleSelection.length">批量追踪</el-button>
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
        <el-table-column type="selection" width="55" align="center"></el-table-column>
        <el-table-column v-if="isColumnVisible('fromCompany')" prop="fromCompany" label="源企业" min-width="180" show-overflow-tooltip></el-table-column>
        <el-table-column v-if="isColumnVisible('toCompany')" prop="toCompany" label="目标企业" min-width="180" show-overflow-tooltip></el-table-column>
        <el-table-column v-if="isColumnVisible('flowType')" prop="flowType" label="流向类型" width="120" align="center">
          <template slot-scope="scope">
            <el-tag :type="getFlowTypeTag(scope.row.flowType)">
              {{ formatFlowType(scope.row.flowType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column v-if="isColumnVisible('purpose')" prop="purpose" label="交易类型" width="120" align="center">
          <template slot-scope="scope">
            <span>{{ formatPurpose(scope.row.purpose) }}</span>
          </template>
        </el-table-column>
        <el-table-column v-if="isColumnVisible('fundNature')" prop="fundNature" label="资金性质" width="120" align="center">
          <template slot-scope="scope">
            <span>{{ formatFundNature(scope.row.fundNature) }}</span>
          </template>
        </el-table-column>
        <el-table-column v-if="isColumnVisible('amount')" prop="amount" label="流转金额(万元)" width="140" align="center" sortable="custom">
          <template slot-scope="scope">
            <span>{{ formatAmount(scope.row.amount) }}</span>
          </template>
        </el-table-column>
        <el-table-column v-if="isColumnVisible('occurTime')" prop="occurTime" label="发生时间" width="120" align="center"></el-table-column>
        <el-table-column v-if="isColumnVisible('riskDesc')" prop="riskDesc" label="风险描述" min-width="200" show-overflow-tooltip></el-table-column>
        <el-table-column label="操作" width="240" align="center" fixed="right">
          <template slot-scope="scope">
            <div class="action-grid">
              <el-button size="mini" @click="handleView(scope.row)" icon="el-icon-view">查看</el-button>
              <el-button size="mini" type="primary" @click="handleTrace(scope.row)" icon="el-icon-guide">追踪</el-button>
              <el-button size="mini" type="success" @click="handleViewPath(scope.row)" icon="el-icon-share">路径图</el-button>
              <el-dropdown @command="handleCommand" trigger="click">
                <el-button size="mini" type="info">更多<i class="el-icon-arrow-down el-icon--right"></i></el-button>
                <el-dropdown-menu slot="dropdown">
                  <el-dropdown-item :command="{ action: 'edit', row: scope.row }" icon="el-icon-edit">编辑</el-dropdown-item>
                  <el-dropdown-item :command="{ action: 'analyze', row: scope.row }" icon="el-icon-data-analysis">深度分析</el-dropdown-item>
                  <el-dropdown-item :command="{ action: 'predict', row: scope.row }" icon="el-icon-trend-charts">预测流向</el-dropdown-item>
                  <el-dropdown-item :command="{ action: 'monitor', row: scope.row }" icon="el-icon-view">实时监控</el-dropdown-item>
                  <el-dropdown-item :command="{ action: 'alert', row: scope.row }" icon="el-icon-bell">设置预警</el-dropdown-item>
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

    <!-- 资金流向对话框 -->
    <FundFlowDialog
      :visible.sync="dialogVisible"
      :data="currentRow"
      :type="dialogType"
      @save="handleSaveFlow"
    />

    <!-- 资金流向路径图对话框 -->
    <FundFlowPathDialog
      :visible.sync="pathDialogVisible"
      :data="currentRow"
    />

    <!-- 深度分析对话框 -->
    <FundFlowAnalysisDialog
      :visible.sync="analysisDialogVisible"
      :data="currentRow"
    />

    <!-- 预测流向对话框 -->
    <FundFlowPredictDialog
      :visible.sync="predictDialogVisible"
      :data="currentRow"
    />

    <!-- 实时监控对话框 -->
    <FundFlowMonitorDialog
      :visible.sync="monitorDialogVisible"
      :data="currentRow"
    />

    <!-- 预警设置对话框 -->
    <FundFlowAlertDialog
      :visible.sync="alertDialogVisible"
      :data="currentRow"
      @refresh="getList"
    />
  </div>
</template>

<script>
  import {
    getFundFlowList,
    deleteFundFlow,
    traceFundFlow,
    getFundFlowStatistics,
    batchTraceFundFlow,
    exportFundFlowData,
    generateFundFlowReport,
  } from '@/api/stateAssets/fundFlow'
  import CompanyTreeModal from '@/components/CompanyTreeModal'
import { mapGetters } from 'vuex'
  import Pagination from '@/components/Pagination'
  import FundFlowDialog from './components/FundFlowDialog'
  import FundFlowPathDialog from './components/FundFlowPathDialog'
  import FundFlowAnalysisDialog from './components/FundFlowAnalysisDialog'
  import FundFlowPredictDialog from './components/FundFlowPredictDialog'
  import FundFlowMonitorDialog from './components/FundFlowMonitorDialog'
  import FundFlowAlertDialog from './components/FundFlowAlertDialog'

  const createDefaultQueryForm = () => ({
    pageNumber: 1,
    pageSize: 10,
    enterpriseId: '',
    enterpriseName: '',
    flowType: '',
    fundNature: '',
    minAmount: '',
    maxAmount: '',
    flowPeriod: null,
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
    name: 'FundFlow',
    components: {
      CompanyTreeModal,
      Pagination,
      FundFlowDialog,
      FundFlowPathDialog,
      FundFlowAnalysisDialog,
      FundFlowPredictDialog,
      FundFlowMonitorDialog,
      FundFlowAlertDialog,
    },
    data() {
      return {
        loading: false,
        tableData: [],
        total: 0,
        multipleSelection: [],
        statistics: {},
        // 表格列筛选
        columnOptions: [
          { prop: 'fromCompany', label: '源企业' },
          { prop: 'toCompany', label: '目标企业' },
          { prop: 'flowType', label: '流向类型' },
          { prop: 'purpose', label: '交易类型' },
          { prop: 'fundNature', label: '资金性质' },
          { prop: 'amount', label: '流转金额(万元)' },
          { prop: 'occurTime', label: '发生时间' },
          { prop: 'riskDesc', label: '风险描述' },
        ],
        checkedColumns: ['fromCompany', 'toCompany', 'flowType', 'purpose', 'fundNature', 'amount', 'occurTime', 'riskDesc'],
        // 筛选条件选择器
        filterOptions: [
          { value: 'enterprise', label: '企业选择' },
          { value: 'flowType', label: '流向类型' },
          { value: 'fundNature', label: '资金性质' },
          { value: 'amount', label: '流转金额' },
          { value: 'flowPeriod', label: '流转时间' },
        ],
        activeFilters: ['enterprise', 'flowType', 'fundNature', 'flowPeriod'],
        queryForm: createDefaultQueryForm(),
        dialogVisible: false,
        pathDialogVisible: false,
        analysisDialogVisible: false,
        predictDialogVisible: false,
        monitorDialogVisible: false,
        alertDialogVisible: false,
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

      // 判断列是否可见
      isColumnVisible(prop) {
        return this.checkedColumns.indexOf(prop) !== -1
      },

      // 打开企业选择弹窗
      openCompanyTree() {
        this.$refs.companyTreeModal.show()
      },

      // 企业选择回调
      handleCompanySelected(node) {
        if (node) {
          this.queryForm.enterpriseId = node.id || ''
          this.queryForm.enterpriseName = node.label || node.name || ''
        }
      },

      // 获取列表数据
      getList() {
        this.loading = true
        getFundFlowList({
          companyName: this.queryForm.enterpriseName || undefined,
          flowType: this.queryForm.flowType || undefined,
          fundNature: this.queryForm.fundNature || undefined,
          startDate: this.queryForm.flowPeriod ? this.formatDate(this.queryForm.flowPeriod[0]) : undefined,
          endDate: this.queryForm.flowPeriod ? this.formatDate(this.queryForm.flowPeriod[1]) : undefined,
        })
          .then((response) => {
            if (response && response.data) {
              const list = Array.isArray(response.data) ? response.data : (response.data.records || response.data.list || [])
              this.tableData = list
              this.total = list.length
            } else {
              this.tableData = []
              this.total = 0
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

      // 获取统计数据
      getStatistics() {
        getFundFlowStatistics().then((response) => {
          if (response && response.data) {
            const d = response.data
            this.statistics = {
              totalFlows: d.total || 0,
              totalAmount: d.totalAmount || 0,
              abnormalCount: d.abnormal || 0,
              avgVelocity: d.hidden || 0,
            }
          } else {
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
          totalFlows: list.length,
          totalAmount: list.reduce((sum, i) => sum + (i.amount || 0), 0),
          abnormalCount: list.filter(i => i.flowType === 'ABNORMAL').length,
          avgVelocity: 0,
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
      handleAdd() {
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

      // 查看路径图
      handleViewPath(row) {
        this.currentRow = { ...row }
        this.pathDialogVisible = true
      },

      // 执行追踪
      handleTrace(row) {
        this.$confirm('确认对该资金流向执行追踪分析？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        }).then(() => {
          const loading = this.$loading({
            lock: true,
            text: '正在追踪资金流向...',
            spinner: 'el-icon-loading',
            background: 'rgba(0, 0, 0, 0.7)',
          })
          traceFundFlow({ id: this.getRowId(row) })
            .then(() => {
              loading.close()
              this.$message.success('资金流向追踪完成')
              this.getList()
              this.getStatistics()
            })
            .catch(() => { loading.close() })
        })
      },

      // 批量追踪
      handleBatchTrace() {
        if (!this.multipleSelection.length) {
          this.$message.warning('请选择要追踪的记录')
          return
        }
        this.$confirm(
          `确认对选中的${this.multipleSelection.length}条记录执行批量追踪？`,
          '提示',
          { confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning' }
        ).then(() => {
          const loading = this.$loading({
            lock: true,
            text: '正在执行批量追踪...',
            spinner: 'el-icon-loading',
            background: 'rgba(0, 0, 0, 0.7)',
          })
          const ids = this.multipleSelection.map((item) => this.getRowId(item))
          batchTraceFundFlow({ ids })
            .then(() => {
              loading.close()
              this.$message.success('批量追踪任务已提交')
              this.getList()
              this.getStatistics()
            })
            .catch(() => { loading.close() })
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
        exportFundFlowData(this.queryForm)
          .then((response) => {
            loading.close()
            if (response && response.data && response.data.data) {
              const rows = response.data.data || []
              if (this.downloadCsv(rows, `资金流向数据_${new Date().getTime()}.csv`)) {
                this.$message.success('数据导出成功')
              }
            } else {
              const blob = new Blob([response.data || response])
              const link = document.createElement('a')
              link.href = URL.createObjectURL(blob)
              link.download = `资金流向数据_${new Date().getTime()}.xlsx`
              link.click()
              this.$message.success('数据导出成功')
            }
          })
          .catch(() => { loading.close() })
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
          case 'analyze':
            this.currentRow = { ...row }
            this.analysisDialogVisible = true
            break
          case 'predict':
            this.currentRow = { ...row }
            this.predictDialogVisible = true
            break
          case 'monitor':
            this.currentRow = { ...row }
            this.monitorDialogVisible = true
            break
          case 'alert':
            this.currentRow = { ...row }
            this.alertDialogVisible = true
            break
          case 'report':
            this.handleGenerateReport(row)
            break
          case 'delete':
            this.handleDelete(row)
            break
        }
      },

      // 保存资金流向
      handleSaveFlow() {
        this.$message.success('资金流向已保存')
        this.getList()
        this.getStatistics()
      },

      // 生成报告
      handleGenerateReport(row) {
        this.$confirm('确认生成资金流向专项分析报告？', '提示', {
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
          generateFundFlowReport({ id: this.getRowId(row) })
            .then((response) => {
              loading.close()
              if (response && response.data) {
                const data = response.data
                const rows = data.details || [data]
                if (this.downloadCsv(rows, `资金流向报告_${data.companyId || ''}_${new Date().getTime()}.csv`)) {
                  this.$message.success('报告生成成功')
                }
              } else {
                this.$message.success('报告生成任务已提交')
              }
            })
            .catch(() => { loading.close() })
        }).catch(() => {})
      },

      // 删除
      handleDelete(row) {
        this.$confirm('确认删除该资金流向记录？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        }).then(() => {
          deleteFundFlow(this.getRowId(row)).then(() => {
            this.$message.success('删除成功')
            this.getList()
            this.getStatistics()
          })
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

      // 获取行ID
      getRowId(row) {
        return row && (row.id || row.fundFlowId || row.partyId)
      },

      // CSV导出
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
        const body = rows.map(row => columns.map(col => {
          const val = row[col.prop]
          const text = String(val === undefined || val === null ? '' : val)
          return /[",\n]/.test(text) ? `"${text.replace(/"/g, '""')}"` : text
        }).join(',')).join('\n')
        const blob = new Blob(['\ufeff' + header + '\n' + body], { type: 'text/csv;charset=utf-8;' })
        const link = document.createElement('a')
        link.href = URL.createObjectURL(blob)
        link.download = fileName
        link.click()
        URL.revokeObjectURL(link.href)
        return true
      },

      // 格式化资金性质
      formatFundNature(value) {
        const map = {
          'OPERATING': '经营性',
          'INVESTING': '投资性',
          'FINANCING': '筹资性',
          'OWN_FUND': '自有资金',
          'BORROWED_FUND': '借入资金',
          'INVESTMENT_FUND': '投资资金',
          'OTHER': '其他',
        }
        return map[value] || value || '-'
      },

      // 格式化金额（万元单位）
      formatAmount(amount) {
        if (!amount && amount !== 0) return '-'
        if (amount >= 10000) {
          return (amount / 10000).toFixed(2) + '亿'
        }
        return Number(amount).toLocaleString('zh-CN', { minimumFractionDigits: 2, maximumFractionDigits: 2 })
      },

      // 格式化流向类型
      formatFlowType(value) {
        const map = {
          'INFLOW': '资金流入',
          'OUTFLOW': '资金流出',
          'INTERNAL_TRANSFER': '内部转移',
          'INVESTMENT_RECOVERY': '投资收回',
          'ABNORMAL': '重大关联',
          'NORMAL': '常规关联',
        }
        return map[value] || value || '-'
      },

      // 获取流向类型标签样式
      getFlowTypeTag(value) {
        const map = {
          'INFLOW': 'success',
          'OUTFLOW': 'warning',
          'INTERNAL_TRANSFER': 'info',
          'INVESTMENT_RECOVERY': '',
          'ABNORMAL': 'danger',
          'NORMAL': 'success',
        }
        return map[value] || 'info'
      },

      // 格式化交易类型（中文化）
      formatPurpose(value) {
        if (!value) return '-'
        // 如果已经是中文，直接返回
        if (/[\u4e00-\u9fa5]/.test(value)) return value
        // 英文映射
        const map = {
          'INFLOW': '资金流入',
          'OUTFLOW': '资金流出',
          'INTERNAL_TRANSFER': '内部转移',
          'INVESTMENT_RECOVERY': '投资收回',
          'EQUITY_INVESTMENT': '股权投资',
          'LOAN': '借款',
          'GUARANTEE': '担保',
          'LEASE': '租赁',
          'PURCHASE': '采购',
          'SALES': '销售',
          'SERVICE': '服务',
          'TRANSFER': '转让',
          'DIVIDEND': '分红',
          'CAPITAL_INJECTION': '注资',
          'DEBT_REPAYMENT': '还款',
          'INTEREST': '利息',
          'OTHER': '其他',
        }
        return map[value] || value
      },

      // 格式化日期为 yyyy-MM-dd
      formatDate(date) {
        if (!date) return ''
        const d = new Date(date)
        const y = d.getFullYear()
        const m = String(d.getMonth() + 1).padStart(2, '0')
        const day = String(d.getDate()).padStart(2, '0')
        return `${y}-${m}-${day}`
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
