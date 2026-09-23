<template>
  <div class="app-container financial-ext-page">
    <div class="page-header" :style="{ background: 'linear-gradient(135deg, ' + themeColor + 'cc 0%, ' + themeColor + ' 100%)' }">
      <div class="page-header-left"><i class="el-icon-connection"></i><span>关联交易</span></div>
      <div class="page-header-desc">监控企业关联方交易金额、定价公允性与合规审查</div>
    </div>
    <!-- 统计概览卡片 -->
    <el-row :gutter="20" class="mb-20">
      <el-col :span="6">
        <el-card class="statistics-card">
          <div class="statistics-content">
            <div class="statistics-icon">
              <i class="el-icon-connection" style="color: #eb2f96"></i>
            </div>
            <div class="statistics-info">
              <div class="statistics-number">
                {{ statistics.totalTransactions || 0 }}
              </div>
              <div class="statistics-label">关联交易总数</div>
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
              <div class="statistics-label">交易总额(万元)</div>
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
              <div class="statistics-label">异常交易</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="statistics-card">
          <div class="statistics-content">
            <div class="statistics-icon">
              <i class="el-icon-star-on" style="color: #f56c6c"></i>
            </div>
            <div class="statistics-info">
              <div class="statistics-number">
                {{ statistics.avgFairness || 0 }}%
              </div>
              <div class="statistics-label">平均公允性</div>
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
        <el-form-item label="企业选择" v-if="isFilterVisible('enterpriseId')">
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
        <el-form-item label="交易类型" v-if="isFilterVisible('transactionType')">
          <el-select
            v-model="queryForm.transactionType"
            placeholder="请选择交易类型"
            clearable
            style="width: 150px"
          >
            <el-option label="商品销售" value="商品销售"></el-option>
            <el-option label="服务提供" value="服务提供"></el-option>
            <el-option label="资金拆借" value="资金拆借"></el-option>
            <el-option label="资产转让" value="资产转让"></el-option>
            <el-option label="担保" value="担保"></el-option>
            <el-option label="商品购销" value="商品购销"></el-option>
            <el-option label="服务交易" value="服务交易"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="资金性质" v-if="isFilterVisible('relationshipType')">
          <el-select
            v-model="queryForm.relationshipType"
            placeholder="请选择资金性质"
            clearable
            style="width: 150px"
          >
            <el-option label="经营性" value="OPERATING"></el-option>
            <el-option label="投资性" value="INVESTING"></el-option>
            <el-option label="筹资性" value="FINANCING"></el-option>
            <el-option label="自有资金" value="OWN_FUND"></el-option>
            <el-option label="借入资金" value="BORROWED_FUND"></el-option>
            <el-option label="投资资金" value="INVESTMENT_FUND"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="交易金额" v-if="isFilterVisible('amount')">
          <el-input v-model="queryForm.minAmount" clearable placeholder="最小金额" style="width: 100px" />
          <span style="margin: 0 10px">-</span>
          <el-input v-model="queryForm.maxAmount" clearable placeholder="最大金额" style="width: 100px" />
        </el-form-item>
        <el-form-item label="公允性评级" v-if="isFilterVisible('fairnessLevel')">
          <el-select
            v-model="queryForm.fairnessLevel"
            placeholder="请选择公允性"
            clearable
            style="width: 120px"
          >
            <el-option label="公允" value="FAIR"></el-option>
            <el-option label="基本公允" value="BASICALLY_FAIR"></el-option>
            <el-option label="不公允" value="UNFAIR"></el-option>
            <el-option label="严重不公允" value="SEVERELY_UNFAIR"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="报告期间" v-if="isFilterVisible('period')">
          <el-input v-model="queryForm.period" clearable placeholder="如：2025-Q1" style="width: 160px" />
        </el-form-item>
        <el-form-item label="是否重大" v-if="isFilterVisible('isMajor')">
          <el-select v-model="queryForm.isMajor" placeholder="请选择" clearable style="width: 100px">
            <el-option label="是" value="1"></el-option>
            <el-option label="否" value="0"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <!-- 筛选条件选择器 -->
          <el-popover placement="bottom" width="280" trigger="click">
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
                <el-button size="mini" @click="activeFilters = ['enterpriseId', 'transactionType', 'relationshipType']">重置</el-button>
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
        <el-popover placement="bottom-start" width="200" trigger="hover">
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
        <el-button size="small" type="success" @click="handleAdd" icon="el-icon-plus">新增分析</el-button>
        <el-button size="small" type="warning" @click="handleBatchIdentify" icon="el-icon-connection" :disabled="!multipleSelection.length">批量识别</el-button>
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
        <el-table-column
          v-if="isColumnVisible('companyId')"
          prop="companyId"
          label="企业ID"
          width="100"
          align="center"
        ></el-table-column>
        <el-table-column
          v-if="isColumnVisible('partyName')"
          prop="partyName"
          label="关联方"
          min-width="180"
          show-overflow-tooltip
        ></el-table-column>
        <el-table-column
          v-if="isColumnVisible('transactionType')"
          prop="transactionType"
          label="交易类型"
          width="120"
          align="center"
        >
          <template slot-scope="scope">
            <el-tag type="primary">{{ formatTransactionType(scope.row.transactionType) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column
          v-if="isColumnVisible('relationType')"
          prop="relationType"
          label="资金性质"
          width="120"
          align="center"
        >
          <template slot-scope="scope">
            <el-tag type="primary">
              {{ formatRelationType(scope.row.relationType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column
          v-if="isColumnVisible('transactionAmount')"
          prop="transactionAmount"
          label="交易金额(万元)"
          width="140"
          align="center"
          sortable="custom"
        >
          <template slot-scope="scope">
            <span>{{ formatAmount(scope.row.transactionAmount) }}</span>
          </template>
        </el-table-column>
        <el-table-column
          v-if="isColumnVisible('balanceAmount')"
          prop="balanceAmount"
          label="余额(万元)"
          width="130"
          align="center"
          sortable="custom"
        >
          <template slot-scope="scope">
            <span>{{ formatAmount(scope.row.balanceAmount) }}</span>
          </template>
        </el-table-column>
        <el-table-column
          v-if="isColumnVisible('isMajor')"
          prop="isMajor"
          label="是否重大"
          width="100"
          align="center"
        >
          <template slot-scope="scope">
            <el-tag :type="scope.row.isMajor === '1' ? 'danger' : 'info'">
              {{ scope.row.isMajor === '1' ? '是' : '否' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column
          v-if="isColumnVisible('period')"
          prop="period"
          label="报告期间"
          width="120"
          align="center"
        ></el-table-column>
        <el-table-column label="操作" width="280" align="center" fixed="right">
          <template slot-scope="scope">
            <div class="action-grid">
              <el-button size="mini" @click="handleView(scope.row)" icon="el-icon-view">查看</el-button>
              <el-button size="mini" type="primary" @click="handleIdentify(scope.row)" icon="el-icon-connection">识别</el-button>
              <el-button size="mini" type="success" @click="handleViewNetwork(scope.row)" icon="el-icon-share">网络图</el-button>
              <el-dropdown @command="handleCommand" trigger="click">
                <el-button size="mini" type="info">更多<i class="el-icon-arrow-down el-icon--right"></i></el-button>
                <el-dropdown-menu slot="dropdown">
                  <el-dropdown-item :command="{ action: 'edit', row: scope.row }" icon="el-icon-edit">编辑</el-dropdown-item>
                  <el-dropdown-item :command="{ action: 'pricing', row: scope.row }" icon="el-icon-money">定价分析</el-dropdown-item>
                  <el-dropdown-item :command="{ action: 'fairness', row: scope.row }" icon="el-icon-star-on">公允性评估</el-dropdown-item>
                  <el-dropdown-item :command="{ action: 'trend', row: scope.row }" icon="el-icon-trend-charts">趋势分析</el-dropdown-item>
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

    <!-- 关联交易分析对话框 -->
    <RelatedTransactionDialog
      :visible.sync="dialogVisible"
      :form-data="currentRow"
      :dialog-type="dialogType"
      @refresh="getList"
    />

    <!-- 关联交易网络图对话框 -->
    <RelatedTransactionNetworkDialog
      :visible.sync="networkDialogVisible"
      :transaction-data="currentRow"
    />

    <!-- 定价分析对话框 -->
    <TransactionPricingDialog
      :visible.sync="pricingDialogVisible"
      :transaction-data="currentRow"
    />

    <!-- 公允性评估对话框 -->
    <TransactionFairnessDialog
      :visible.sync="fairnessDialogVisible"
      :transaction-data="currentRow"
    />

    <!-- 趋势分析对话框 -->
    <TransactionTrendDialog
      :visible.sync="trendDialogVisible"
      :transaction-data="currentRow"
    />
  </div>
</template>

<script>
  import {
    getRelatedTransactionList,
    deleteRelatedTransaction,
    identifyRelatedTransactions,
    getRelatedTransactionStatistics,
    batchIdentifyRelatedTransactions,
    exportRelatedTransactionData,
    analyzeRelatedTransactionNetwork,
    analyzeRelatedTransactionPricing,
    assessRelatedTransactionFairness,
    analyzeRelatedTransactionTrend,
    generateRelatedTransactionReport,
  } from '@/api/stateAssets/relatedTransaction'
  import CompanyTreeModal from '@/components/CompanyTreeModal'
import { mapGetters } from 'vuex'
  import Pagination from '@/components/Pagination'
  import RelatedTransactionDialog from './components/RelatedTransactionDialog'
  import RelatedTransactionNetworkDialog from './components/RelatedTransactionNetworkDialog'
  import TransactionPricingDialog from './components/TransactionPricingDialog'
  import TransactionFairnessDialog from './components/TransactionFairnessDialog'
  import TransactionTrendDialog from './components/TransactionTrendDialog'

  const createDefaultQueryForm = () => ({
    pageNumber: 1,
    pageSize: 10,
    enterpriseId: '',
    enterpriseName: '',
    transactionType: '',
    relationshipType: '',
    minAmount: '',
    maxAmount: '',
    fairnessLevel: '',
    period: '',
    isMajor: '',
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
    name: 'RelatedTransaction',
    components: {
      CompanyTreeModal,
      Pagination,
      RelatedTransactionDialog,
      RelatedTransactionNetworkDialog,
      TransactionPricingDialog,
      TransactionFairnessDialog,
      TransactionTrendDialog,
    },
    data() {
      return {
        loading: false,
        tableData: [],
        total: 0,
        multipleSelection: [],
        statistics: {},
        columnOptions: [
          { prop: 'companyId', label: '企业ID' },
          { prop: 'partyName', label: '关联方' },
          { prop: 'transactionType', label: '交易类型' },
          { prop: 'relationType', label: '资金性质' },
          { prop: 'transactionAmount', label: '交易金额(万元)' },
          { prop: 'balanceAmount', label: '余额(万元)' },
          { prop: 'isMajor', label: '是否重大' },
          { prop: 'period', label: '报告期间' },
        ],
        checkedColumns: [
          'companyId', 'partyName', 'transactionType', 'relationType',
          'transactionAmount', 'balanceAmount', 'isMajor', 'period',
        ],
        filterOptions: [
          { value: 'enterpriseId', label: '企业选择' },
          { value: 'transactionType', label: '交易类型' },
          { value: 'relationshipType', label: '资金性质' },
          { value: 'amount', label: '交易金额' },
          { value: 'fairnessLevel', label: '公允性评级' },
          { value: 'period', label: '报告期间' },
          { value: 'isMajor', label: '是否重大' },
        ],
        activeFilters: ['enterpriseId', 'transactionType', 'relationshipType', 'amount'],
        queryForm: createDefaultQueryForm(),
        dialogVisible: false,
        networkDialogVisible: false,
        pricingDialogVisible: false,
        fairnessDialogVisible: false,
        trendDialogVisible: false,
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
        getRelatedTransactionList(this.queryForm)
          .then((response) => {
            if (response && response.data) {
              this.tableData = response.data.records || response.data.list || []
              this.total = response.data.total || 0
              if (!this.statistics || !this.statistics.totalTransactions) {
                this.calcStatisticsFromList()
              }
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
        getRelatedTransactionStatistics(this.queryForm).then((response) => {
          if (response && response.data) {
            this.statistics = response.data
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
          totalTransactions: this.total || list.length,
          totalAmount: list.reduce((sum, i) => sum + (Number(i.transactionAmount) || 0), 0),
          abnormalCount: list.filter(i => i.isMajor === '1').length,
          avgFairness: list.length > 0 ? Math.round(list.reduce((sum, i) => {
            let score = 85
            const amt = Number(i.transactionAmount) || 0
            if (amt > 10000) score -= 10
            if (amt > 50000) score -= 15
            if (i.isMajor === '1') score -= 10
            return sum + Math.max(score, 0)
          }, 0) / list.length) : 0,
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
        this.$refs.queryForm && this.$refs.queryForm.resetFields()
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

      // 查看网络图
      handleViewNetwork(row) {
        const loading = this.$loading({ lock: true, text: '正在加载网络图数据...', spinner: 'el-icon-loading' })
        analyzeRelatedTransactionNetwork({ companyId: row.companyId })
          .then((response) => {
            const data = this.getResponseData(response)
            this.currentRow = { ...row, networkData: data || {} }
            this.networkDialogVisible = true
          })
          .finally(() => loading.close())
      },

      // 执行识别
      handleIdentify(row) {
        this.$confirm('确认对该企业执行关联交易识别？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        }).then(() => {
          const loading = this.$loading({
            lock: true,
            text: '正在识别关联交易...',
            spinner: 'el-icon-loading',
            background: 'rgba(0, 0, 0, 0.7)',
          })
          identifyRelatedTransactions({ id: row.id || row.partyId })
            .then(() => {
              loading.close()
              this.$message.success('关联交易识别完成')
              this.getList()
              this.getStatistics()
            })
            .catch(() => { loading.close() })
        })
      },

      // 批量识别
      handleBatchIdentify() {
        if (!this.multipleSelection.length) {
          this.$message.warning('请选择要识别的记录')
          return
        }
        const ids = this.multipleSelection.map(item => item.id || item.partyId)
        this.$confirm(
          `确认对选中的${ids.length}条记录执行批量识别？`,
          '提示',
          { confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning' }
        ).then(() => {
          const loading = this.$loading({
            lock: true,
            text: '正在执行批量识别...',
            spinner: 'el-icon-loading',
            background: 'rgba(0, 0, 0, 0.7)',
          })
          batchIdentifyRelatedTransactions({ ids })
            .then(() => {
              loading.close()
              this.$message.success('批量识别完成')
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
        exportRelatedTransactionData(this.queryForm)
          .then((response) => {
            loading.close()
            const data = this.getResponseData(response) || {}
            const rows = data.data || data.rows || []
            if (this.downloadCsv(rows, `关联交易数据_${new Date().getTime()}.csv`)) {
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
          case 'pricing':
            this.openPricingDialog(row)
            break
          case 'fairness':
            this.openFairnessDialog(row)
            break
          case 'trend':
            this.openTrendDialog(row)
            break
          case 'report':
            this.handleGenerateReport(row)
            break
          case 'delete':
            this.handleDelete(row)
            break
        }
      },

      // 定价分析
      openPricingDialog(row) {
        const loading = this.$loading({ lock: true, text: '正在加载定价数据...', spinner: 'el-icon-loading' })
        analyzeRelatedTransactionPricing({ id: row.id || row.partyId })
          .then((response) => {
            const data = this.getResponseData(response)
            this.currentRow = data ? { ...row, ...data } : { ...row }
            this.pricingDialogVisible = true
          })
          .finally(() => loading.close())
      },

      // 公允性评估
      openFairnessDialog(row) {
        const loading = this.$loading({ lock: true, text: '正在加载公允性数据...', spinner: 'el-icon-loading' })
        assessRelatedTransactionFairness({ id: row.id || row.partyId })
          .then((response) => {
            const data = this.getResponseData(response)
            this.currentRow = data ? { ...row, ...data } : { ...row }
            this.fairnessDialogVisible = true
          })
          .finally(() => loading.close())
      },

      // 趋势分析
      openTrendDialog(row) {
        const loading = this.$loading({ lock: true, text: '正在加载趋势数据...', spinner: 'el-icon-loading' })
        analyzeRelatedTransactionTrend({ companyId: row.companyId })
          .then((response) => {
            const data = this.getResponseData(response)
            this.currentRow = { ...row, trendRows: data ? data.rows || [] : [] }
            this.trendDialogVisible = true
          })
          .finally(() => loading.close())
      },

      // 生成报告
      handleGenerateReport(row) {
        this.$confirm('确认生成该关联交易的专项分析报告？', '提示', {
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
          generateRelatedTransactionReport({ id: row.id || row.partyId })
            .then((response) => {
              loading.close()
              const data = this.getResponseData(response) || {}
              const rows = [data]
              if (this.downloadCsv(rows, `关联交易报告_${row.partyName || ''}_${new Date().getTime()}.csv`)) {
                this.$message.success('报告生成成功')
              }
            })
            .catch(() => { loading.close() })
        })
      },

      // 删除
      handleDelete(row) {
        this.$confirm('确认删除该关联交易记录？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        }).then(() => {
          deleteRelatedTransaction(row.id || row.partyId).then(() => {
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

      getResponseData(response) {
        return response && response.data ? response.data : response
      },

      // 交易类型中英文映射
      formatTransactionType(type) {
        const map = {
          GOODS_SALE: '商品销售',
          SERVICE_PROVISION: '服务提供',
          FUND_LENDING: '资金拆借',
          ASSET_TRANSFER: '资产转让',
        }
        return map[type] || type || '-'
      },

      // 资金性质中英文映射
      formatRelationType(type) {
        const map = {
          OPERATING: '经营性',
          INVESTING: '投资性',
          FINANCING: '筹资性',
          OWN_FUND: '自有资金',
          BORROWED_FUND: '借入资金',
          INVESTMENT_FUND: '投资资金',
          OTHER: '其他',
        }
        return map[type] || type || '-'
      },

      // 格式化金额（万元单位）
      formatAmount(amount) {
        if (!amount && amount !== 0) return '-'
        const num = Number(amount)
        if (isNaN(num)) return '-'
        if (num >= 10000) {
          return (num / 10000).toFixed(2) + '亿'
        }
        return num.toLocaleString('zh-CN', { minimumFractionDigits: 2, maximumFractionDigits: 2 })
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
          let val = row[col.prop]
          if (col.prop === 'transactionType') val = this.formatTransactionType(val)
          if (col.prop === 'relationType') val = this.formatRelationType(val)
          if (col.prop === 'isMajor') val = val === '1' ? '是' : '否'
          return this.escapeCsv(val === undefined || val === null ? '' : val)
        }).join(',')).join('\n')
        const blob = new Blob(['\ufeff' + header + '\n' + body], { type: 'text/csv;charset=utf-8;' })
        const link = document.createElement('a')
        link.href = URL.createObjectURL(blob)
        link.download = fileName
        link.click()
        URL.revokeObjectURL(link.href)
        return true
      },

      escapeCsv(value) {
        const text = String(value)
        return /[",\n]/.test(text) ? `"${text.replace(/"/g, '""')}"` : text
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
