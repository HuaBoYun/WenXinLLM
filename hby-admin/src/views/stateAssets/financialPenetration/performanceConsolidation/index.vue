<template>
  <div class="app-container financial-ext-page">
    <div class="page-header" :style="{ background: 'linear-gradient(135deg, ' + themeColor + 'cc 0%, ' + themeColor + ' 100%)' }">
      <div class="page-header-left"><i class="el-icon-data-board"></i><span>业绩合并</span></div>
      <div class="page-header-desc">合并分析子公司业绩数据、抵消调整与合并口径</div>
    </div>
    <!-- 统计概览卡片 -->
    <el-row :gutter="20" class="mb-20">
      <el-col :span="6">
        <el-card class="statistics-card">
          <div class="statistics-content">
            <div class="statistics-icon">
              <i class="el-icon-data-board" style="color: #eb2f96"></i>
            </div>
            <div class="statistics-info">
              <div class="statistics-number">
                {{ statistics.totalAnalysis || 0 }}
              </div>
              <div class="statistics-label">业绩分析总数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="statistics-card">
          <div class="statistics-content">
            <div class="statistics-icon">
              <i class="el-icon-star-on" style="color: #67c23a"></i>
            </div>
            <div class="statistics-info">
              <div class="statistics-number">
                {{ statistics.avgContribution || 0 }}%
              </div>
              <div class="statistics-label">平均贡献度</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="statistics-card">
          <div class="statistics-content">
            <div class="statistics-icon">
              <i class="el-icon-connection" style="color: #e6a23c"></i>
            </div>
            <div class="statistics-info">
              <div class="statistics-number">
                {{ statistics.synergyScore || 0 }}
              </div>
              <div class="statistics-label">协同效应评分</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="statistics-card">
          <div class="statistics-content">
            <div class="statistics-icon">
              <i class="el-icon-warning" style="color: #f56c6c"></i>
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
    </el-row>

    <!-- 查询表单 -->
    <el-card class="mb-20">
      <el-form
        :model="queryForm"
        ref="queryForm"
        :inline="true"
        label-width="100px"
      >
        <el-form-item label="企业选择">
          <CompanyTreeModal
            v-model="queryForm.enterpriseId"
            :enterprise-name.sync="queryForm.enterpriseName"
            placeholder="请选择企业"
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="合并类型">
          <el-select
            v-model="queryForm.consolidationType"
            placeholder="请选择合并类型"
            clearable
            style="width: 150px"
          >
            <el-option label="收入合并" value="REVENUE"></el-option>
            <el-option label="利润合并" value="PROFIT"></el-option>
            <el-option label="成本合并" value="COST"></el-option>
            <el-option label="综合合并" value="COMPREHENSIVE"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="分析状态">
          <el-select
            v-model="queryForm.analysisStatus"
            placeholder="请选择分析状态"
            clearable
            style="width: 150px"
          >
            <el-option label="待分析" value="PENDING"></el-option>
            <el-option label="分析中" value="ANALYZING"></el-option>
            <el-option label="已完成" value="COMPLETED"></el-option>
            <el-option label="分析失败" value="FAILED"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="合并期间">
          <el-date-picker
            v-model="queryForm.consolidationPeriod"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            style="width: 240px"
          ></el-date-picker>
        </el-form-item>
        <el-form-item label="质量评分">
          <el-input
            v-model="queryForm.minQuality"
            placeholder="最小值"
            style="width: 100px"
          ></el-input>
          <span style="margin: 0 10px">-</span>
          <el-input
            v-model="queryForm.maxQuality"
            placeholder="最大值"
            style="width: 100px"
          ></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleQuery" icon="el-icon-search">
            查询
          </el-button>
          <el-button @click="resetQuery" icon="el-icon-refresh">重置</el-button>
          <el-button type="success" @click="handleAdd" icon="el-icon-plus">
            新增分析
          </el-button>
          <el-button
            type="warning"
            @click="handleBatchAnalyze"
            icon="el-icon-data-board"
            :disabled="!multipleSelection.length"
          >
            批量分析
          </el-button>
          <el-button type="info" @click="handleExport" icon="el-icon-download">
            导出
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 数据表格 -->
    <el-card>
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
          prop="enterpriseName"
          label="企业名称"
          min-width="200"
          show-overflow-tooltip
        ></el-table-column>
        <el-table-column
          prop="consolidationType"
          label="合并类型"
          width="120"
          align="center"
        >
          <template slot-scope="scope">
            <el-tag
              :type="getConsolidationTypeTag(scope.row.consolidationType)"
            >
              {{ getConsolidationTypeText(scope.row.consolidationType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column
          prop="consolidationPeriod"
          label="合并期间"
          width="120"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="consolidationScope"
          label="合并范围"
          width="120"
          align="center"
          sortable="custom"
        >
          <template slot-scope="scope">
            <span>{{ scope.row.consolidationScope }}家</span>
          </template>
        </el-table-column>
        <el-table-column
          prop="totalRevenue"
          label="合并收入"
          width="120"
          align="center"
          sortable="custom"
        >
          <template slot-scope="scope">
            <span>{{ formatAmount(scope.row.totalRevenue) }}万</span>
          </template>
        </el-table-column>
        <el-table-column
          prop="totalProfit"
          label="合并利润"
          width="120"
          align="center"
          sortable="custom"
        >
          <template slot-scope="scope">
            <span>{{ formatAmount(scope.row.totalProfit) }}万</span>
          </template>
        </el-table-column>
        <el-table-column
          prop="contributionRatio"
          label="贡献度"
          width="120"
          align="center"
          sortable="custom"
        >
          <template slot-scope="scope">
            <el-progress
              :percentage="scope.row.contributionRatio"
              :color="getContributionColor(scope.row.contributionRatio)"
              :show-text="false"
              style="width: 60px"
            ></el-progress>
            <span style="margin-left: 10px">
              {{ scope.row.contributionRatio }}%
            </span>
          </template>
        </el-table-column>
        <el-table-column
          prop="synergyEffect"
          label="协同效应"
          width="120"
          align="center"
          sortable="custom"
        >
          <template slot-scope="scope">
            <el-tag :type="getSynergyEffectTag(scope.row.synergyEffect)">
              {{ getSynergyEffectText(scope.row.synergyEffect) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column
          prop="qualityScore"
          label="质量评分"
          width="120"
          align="center"
          sortable="custom"
        >
          <template slot-scope="scope">
            <el-progress
              :percentage="scope.row.qualityScore"
              :color="getQualityColor(scope.row.qualityScore)"
              :show-text="false"
              style="width: 60px"
            ></el-progress>
            <span style="margin-left: 10px">{{ scope.row.qualityScore }}</span>
          </template>
        </el-table-column>
        <el-table-column
          prop="analysisStatus"
          label="分析状态"
          width="120"
          align="center"
        >
          <template slot-scope="scope">
            <el-tag :type="getAnalysisStatusTag(scope.row.analysisStatus)">
              {{ getAnalysisStatusText(scope.row.analysisStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column
          prop="riskLevel"
          label="风险等级"
          width="100"
          align="center"
        >
          <template slot-scope="scope">
            <el-tag
              :type="getRiskLevelTag(scope.row.riskLevel)"
              v-if="scope.row.riskLevel"
            >
              {{ scope.row.riskLevel }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column
          prop="lastAnalysisTime"
          label="最后分析时间"
          width="160"
          align="center"
        ></el-table-column>
        <el-table-column label="操作" width="320" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button
              size="mini"
              @click="handleView(scope.row)"
              icon="el-icon-view"
            >
              查看
            </el-button>
            <el-button
              size="mini"
              type="primary"
              @click="handleAnalyze(scope.row)"
              icon="el-icon-data-board"
            >
              分析
            </el-button>
            <el-button
              size="mini"
              type="success"
              @click="handleViewStatement(scope.row)"
              icon="el-icon-s-finance"
            >
              报表
            </el-button>
            <el-dropdown @command="handleCommand" style="margin-left: 10px">
              <el-button size="mini" type="info">
                更多
                <i class="el-icon-arrow-down el-icon--right"></i>
              </el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item
                  :command="{ action: 'edit', row: scope.row }"
                  icon="el-icon-edit"
                >
                  编辑
                </el-dropdown-item>
                <el-dropdown-item
                  :command="{ action: 'trend', row: scope.row }"
                  icon="el-icon-trend-charts"
                >
                  趋势分析
                </el-dropdown-item>
                <el-dropdown-item
                  :command="{ action: 'compare', row: scope.row }"
                  icon="el-icon-s-data"
                >
                  对比分析
                </el-dropdown-item>
                <el-dropdown-item
                  :command="{ action: 'simulate', row: scope.row }"
                  icon="el-icon-cpu"
                >
                  模拟分析
                </el-dropdown-item>
                <el-dropdown-item
                  :command="{ action: 'optimize', row: scope.row }"
                  icon="el-icon-setting"
                >
                  优化建议
                </el-dropdown-item>
                <el-dropdown-item
                  :command="{ action: 'report', row: scope.row }"
                  icon="el-icon-document"
                >
                  生成报告
                </el-dropdown-item>
                <el-dropdown-item
                  :command="{ action: 'delete', row: scope.row }"
                  icon="el-icon-delete"
                  divided
                >
                  删除
                </el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
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

    <!-- 业绩合并分析对话框 -->
    <PerformanceConsolidationDialog
      :visible.sync="dialogVisible"
      :form-data="currentRow"
      :dialog-type="dialogType"
      @refresh="getList"
    />

    <!-- 业绩合并报表对话框 -->
    <PerformanceStatementDialog
      :visible.sync="statementDialogVisible"
      :performance-data="currentRow"
    />

    <!-- 趋势分析对话框 -->
    <PerformanceTrendDialog
      :visible.sync="trendDialogVisible"
      :performance-data="currentRow"
    />

    <!-- 对比分析对话框 -->
    <PerformanceCompareDialog
      :visible.sync="compareDialogVisible"
      :performance-data="currentRow"
    />

    <!-- 模拟分析对话框 -->
    <PerformanceSimulateDialog
      :visible.sync="simulateDialogVisible"
      :performance-data="currentRow"
    />

    <!-- 优化建议对话框 -->
    <PerformanceOptimizeDialog
      :visible.sync="optimizeDialogVisible"
      :performance-data="currentRow"
    />
  </div>
</template>

<script>
  import {
    getPerformanceConsolidationList,
    deletePerformanceConsolidation,
    performPerformanceAnalysis,
    getPerformanceStatistics,
    batchPerformPerformanceAnalysis,
    exportPerformanceData,
  } from '@/api/stateAssets/performanceConsolidation'
  import CompanyTreeModal from '@/components/CompanyTreeModal'
import { mapGetters } from 'vuex'
  import Pagination from '@/components/Pagination'
  import PerformanceConsolidationDialog from './components/PerformanceConsolidationDialog'
  import PerformanceStatementDialog from './components/PerformanceStatementDialog'
  import PerformanceTrendDialog from './components/PerformanceTrendDialog'
  import PerformanceCompareDialog from './components/PerformanceCompareDialog'
  import PerformanceSimulateDialog from './components/PerformanceSimulateDialog'
  import PerformanceOptimizeDialog from './components/PerformanceOptimizeDialog'

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
    name: 'PerformanceConsolidation',
    components: {
      CompanyTreeModal,
      Pagination,
      PerformanceConsolidationDialog,
      PerformanceStatementDialog,
      PerformanceTrendDialog,
      PerformanceCompareDialog,
      PerformanceSimulateDialog,
      PerformanceOptimizeDialog,
    },
    data() {
      return {
        loading: false,
        tableData: [],
        total: 0,
        multipleSelection: [],
        statistics: {},
        queryForm: {
          pageNumber: 1,
          pageSize: 10,
          enterpriseId: '',
          enterpriseName: '',
          consolidationType: '',
          analysisStatus: '',
          consolidationPeriod: null,
          minQuality: '',
          maxQuality: '',
        },
        dialogVisible: false,
        statementDialogVisible: false,
        trendDialogVisible: false,
        compareDialogVisible: false,
        simulateDialogVisible: false,
        optimizeDialogVisible: false,
        dialogType: 'add',
        currentRow: {},
      }
    },
    created() {
      this.getList()
      this.getStatistics()
    },
    methods: {
      // 获取列表数据
      getList() {
        this.loading = true
        getPerformanceConsolidationList(this.queryForm)
          .then((response) => {
            if (response && response.data) {
              this.tableData = response.data.records || response.data.list || []
              this.total = response.data.total || 0
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

      // 获取统计数据
      getStatistics() {
        getPerformanceStatistics().then((response) => {
          if (response && response.data) {
            this.statistics = response.data
          } else {
            this.statistics = {}
          }
        }).catch((error) => {
          console.error('获取统计数据异常:', error)
          this.statistics = {}
        })
      },

      // 查询
      handleQuery() {
        this.queryForm.pageNumber = 1
        this.getList()
      },

      // 重置查询
      resetQuery() {
        this.$refs.queryForm.resetFields()
        this.queryForm = {
          pageNumber: 1,
          pageSize: 10,
          enterpriseId: '',
          enterpriseName: '',
          consolidationType: '',
          analysisStatus: '',
          consolidationPeriod: null,
          minQuality: '',
          maxQuality: '',
        }
        this.getList()
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

      // 查看报表
      handleViewStatement(row) {
        this.currentRow = { ...row }
        this.statementDialogVisible = true
      },

      // 执行分析
      handleAnalyze(row) {
        this.$confirm('确认对该企业执行业绩合并分析？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        }).then(() => {
          const loading = this.$loading({
            lock: true,
            text: '正在执行业绩合并分析...',
            spinner: 'el-icon-loading',
            background: 'rgba(0, 0, 0, 0.7)',
          })

          performPerformanceAnalysis({
            performanceConsolidationId: row.performanceConsolidationId,
          })
            .then((response) => {
              loading.close()
              this.$message.success('业绩合并分析完成')
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

        this.$confirm(
          `确认对选中的${this.multipleSelection.length}条记录执行批量分析？`,
          '提示',
          {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning',
          }
        ).then(() => {
          const loading = this.$loading({
            lock: true,
            text: '正在执行批量分析...',
            spinner: 'el-icon-loading',
            background: 'rgba(0, 0, 0, 0.7)',
          })

          const performanceConsolidationIds = this.multipleSelection.map(
            (item) => item.performanceConsolidationId
          )
          batchPerformPerformanceAnalysis({
            performanceConsolidationIds,
            updateBy: this.$store.getters.name,
          })
            .then((response) => {
              loading.close()
              this.$message.success('批量分析任务已提交')
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

        exportPerformanceData(this.queryForm)
          .then((response) => {
            loading.close()
            const blob = new Blob([response.data])
            const link = document.createElement('a')
            link.href = URL.createObjectURL(blob)
            link.download = `业绩合并分析数据_${new Date().getTime()}.xlsx`
            link.click()
            this.$message.success('数据导出成功')
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
            this.currentRow = { ...row }
            this.trendDialogVisible = true
            break
          case 'compare':
            this.currentRow = { ...row }
            this.compareDialogVisible = true
            break
          case 'simulate':
            this.currentRow = { ...row }
            this.simulateDialogVisible = true
            break
          case 'optimize':
            this.currentRow = { ...row }
            this.optimizeDialogVisible = true
            break
          case 'report':
            this.handleGenerateReport(row)
            break
          case 'delete':
            this.handleDelete(row)
            break
        }
      },

      // 生成报告
      handleGenerateReport(row) {
        this.$confirm('确认生成业绩合并分析报告？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'info'
        }).then(() => {
          this.$message.success('业绩合并报告生成任务已提交，请稍后查看')
        }).catch(() => {})
      },

      // 删除
      handleDelete(row) {
        this.$confirm('确认删除该业绩合并分析记录？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        }).then(() => {
          deletePerformanceConsolidation(row.performanceConsolidationId).then(
            (response) => {
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
      handleSortChange({ column, prop, order }) {
        this.queryForm.orderBy = prop
        this.queryForm.orderDirection = order === 'ascending' ? 'ASC' : 'DESC'
        this.getList()
      },

      // 获取合并类型标签
      getConsolidationTypeTag(type) {
        const tagMap = {
          REVENUE: 'primary',
          PROFIT: 'success',
          COST: 'warning',
          COMPREHENSIVE: 'danger',
        }
        return tagMap[type] || 'info'
      },

      // 获取合并类型文本
      getConsolidationTypeText(type) {
        const textMap = {
          REVENUE: '收入合并',
          PROFIT: '利润合并',
          COST: '成本合并',
          COMPREHENSIVE: '综合合并',
        }
        return textMap[type] || type
      },

      // 获取协同效应标签
      getSynergyEffectTag(effect) {
        const tagMap = {
          POSITIVE: 'success',
          NEUTRAL: 'warning',
          NEGATIVE: 'danger',
        }
        return tagMap[effect] || 'info'
      },

      // 获取协同效应文本
      getSynergyEffectText(effect) {
        const textMap = {
          POSITIVE: '正向',
          NEUTRAL: '中性',
          NEGATIVE: '负向',
        }
        return textMap[effect] || effect
      },

      // 获取分析状态标签
      getAnalysisStatusTag(status) {
        const tagMap = {
          PENDING: 'info',
          ANALYZING: 'warning',
          COMPLETED: 'success',
          FAILED: 'danger',
        }
        return tagMap[status] || 'info'
      },

      // 获取分析状态文本
      getAnalysisStatusText(status) {
        const textMap = {
          PENDING: '待分析',
          ANALYZING: '分析中',
          COMPLETED: '已完成',
          FAILED: '分析失败',
        }
        return textMap[status] || status
      },

      // 获取风险等级标签
      getRiskLevelTag(level) {
        const tagMap = {
          LOW: 'success',
          MEDIUM: 'warning',
          HIGH: 'danger',
          CRITICAL: 'danger',
        }
        return tagMap[level] || 'info'
      },

      // 获取贡献度颜色
      getContributionColor(ratio) {
        if (ratio >= 80) return '#67C23A'
        if (ratio >= 60) return '#409EFF'
        if (ratio >= 40) return '#E6A23C'
        return '#F56C6C'
      },

      // 获取质量评分颜色
      getQualityColor(score) {
        if (score >= 80) return '#67C23A'
        if (score >= 60) return '#E6A23C'
        return '#F56C6C'
      },

      // 格式化金额
      formatAmount(amount) {
        if (!amount) return '0'
        return (amount / 10000).toFixed(2)
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
</style>
