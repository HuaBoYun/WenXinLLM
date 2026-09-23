<template>
  <div class="app-container equity-page" :style="themeVars">
    <div class="page-header" :style="{ background: 'linear-gradient(135deg, ' + themeColor + 'cc 0%, ' + themeColor + ' 100%)' }">
      <div class="page-header-left"><i class="el-icon-share"></i><span>股权结构分析</span></div>
      <div class="page-header-desc">分析企业股权结构类型、控制方式与集中度</div>
    </div>
    <!-- 统计概览卡片 -->
    <el-row :gutter="20" class="mb-20">
      <el-col :span="6">
        <el-card class="statistics-card">
          <div class="statistics-content">
            <div class="statistics-icon">
              <i class="el-icon-share" style="color: #409eff"></i>
            </div>
            <div class="statistics-info">
              <div class="statistics-number">
                {{ statistics.totalStructures || 0 }}
              </div>
              <div class="statistics-label">股权结构总数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="statistics-card">
          <div class="statistics-content">
            <div class="statistics-icon">
              <i class="el-icon-pie-chart" style="color: #67c23a"></i>
            </div>
            <div class="statistics-info">
              <div class="statistics-number">
                {{ statistics.avgConcentration || 0 }}%
              </div>
              <div class="statistics-label">平均集中度</div>
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
              <div class="statistics-label">风险结构</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="statistics-card">
          <div class="statistics-content">
            <div class="statistics-icon">
              <i class="el-icon-refresh" style="color: #f56c6c"></i>
            </div>
            <div class="statistics-info">
              <div class="statistics-number">
                {{ statistics.changedCount || 0 }}
              </div>
              <div class="statistics-label">本月变更</div>
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
        <el-form-item label="企业ID">
          <el-input
            v-model="queryForm.enterpriseId"
            placeholder="请输入企业ID"
            clearable
            style="width: 200px"
            @keyup.enter.native="handleQuery"
          ></el-input>
        </el-form-item>
        <el-form-item label="投资方类型">
          <el-select v-model="queryForm.investorType" placeholder="请选择" clearable style="width:130px">
            <el-option label="企业" value="ENTERPRISE" />
            <el-option label="个人" value="INDIVIDUAL" />
            <el-option label="政府" value="GOVERNMENT" />
            <el-option label="基金" value="FUND" />
          </el-select>
        </el-form-item>
        <el-form-item label="控制类型">
          <el-select v-model="queryForm.controlType" placeholder="请选择" clearable style="width:130px">
            <el-option label="直接控制" value="DIRECT" />
            <el-option label="间接控制" value="INDIRECT" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="queryForm.status" placeholder="请选择" clearable style="width:120px">
            <el-option label="正常" value="NORMAL" />
            <el-option label="已质押" value="PLEDGED" />
            <el-option label="冻结" value="FROZEN" />
          </el-select>
        </el-form-item>
        <el-form-item label="关键词">
          <el-input v-model="queryForm.keyword" placeholder="投资方名称" clearable style="width:150px" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleQuery" icon="el-icon-search">
            查询
          </el-button>
          <el-button @click="resetQuery" icon="el-icon-refresh">重置</el-button>
          <el-button type="success" @click="handleAdd" icon="el-icon-plus">
            新增结构
          </el-button>
          <el-button
            type="warning"
            @click="handleBatchAnalyze"
            icon="el-icon-data-analysis"
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
          prop="enterpriseId"
          label="企业ID"
          min-width="120"
          show-overflow-tooltip
        ></el-table-column>
        <el-table-column
          prop="investorName"
          label="投资方"
          min-width="180"
          show-overflow-tooltip
        ></el-table-column>
        <el-table-column
          prop="investorType"
          label="投资方类型"
          width="120"
          align="center"
        >
          <template slot-scope="scope">
            <el-tag :type="{ENTERPRISE:'success',INDIVIDUAL:'primary',GOVERNMENT:'danger',FUND:'warning'}[scope.row.investorType] || 'info'" size="small">
              {{ {ENTERPRISE:'企业',INDIVIDUAL:'个人',GOVERNMENT:'政府',INSTITUTION:'机构',FUND:'基金'}[scope.row.investorType] || scope.row.investorType }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column
          prop="shareholdingRatio"
          label="持股比例"
          width="150"
          align="center"
          sortable="custom"
        >
          <template slot-scope="scope">
            <el-progress :percentage="Number(scope.row.shareholdingRatio)||0" :color="Number(scope.row.shareholdingRatio)>=50?'#f56c6c':'#409eff'" :show-text="false" style="width:60px;display:inline-block"></el-progress>
            <span style="margin-left:8px">{{ scope.row.shareholdingRatio }}%</span>
          </template>
        </el-table-column>
        <el-table-column
          prop="shareholdingAmount"
          label="持股金额(万)"
          width="120"
          align="right"
        >
          <template slot-scope="scope">
            {{ scope.row.shareholdingAmount ? Number(scope.row.shareholdingAmount).toLocaleString() : '-' }}
          </template>
        </el-table-column>
        <el-table-column
          prop="votingRatio"
          label="表决权(%)"
          width="100"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="controlType"
          label="控制类型"
          width="100"
          align="center"
        >
          <template slot-scope="scope">
            <el-tag :type="getControlTypeTag(scope.row.controlType)" size="small">
              {{ getControlTypeText(scope.row.controlType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column
          prop="status"
          label="状态"
          width="90"
          align="center"
        >
          <template slot-scope="scope">
            <el-tag :type="{NORMAL:'success',PLEDGED:'warning',FROZEN:'danger',TRANSFERRED:'info'}[scope.row.status]||'info'" size="small">
              {{ {NORMAL:'正常',PLEDGED:'已质押',FROZEN:'冻结',TRANSFERRED:'已转让',CANCELLED:'已注销'}[scope.row.status] || scope.row.status }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column
          prop="createTime"
          label="创建时间"
          width="160"
          align="center"
        ></el-table-column>
        <el-table-column label="操作" width="300" align="center" fixed="right">
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
              @click="handleViewChart(scope.row)"
              icon="el-icon-share"
            >
              结构图
            </el-button>
            <el-button
              size="mini"
              type="success"
              @click="handleAnalyze(scope.row)"
              icon="el-icon-data-analysis"
            >
              分析
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
                  :command="{ action: 'compare', row: scope.row }"
                  icon="el-icon-s-data"
                >
                  对比分析
                </el-dropdown-item>
                <el-dropdown-item
                  :command="{ action: 'simulate', row: scope.row }"
                  icon="el-icon-cpu"
                >
                  模拟变更
                </el-dropdown-item>
                <el-dropdown-item
                  :command="{ action: 'optimize', row: scope.row }"
                  icon="el-icon-magic-stick"
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

    <!-- 股权结构对话框 -->
    <EquityStructureDialog
      :visible.sync="dialogVisible"
      :form-data="currentRow"
      :dialog-type="dialogType"
      @refresh="getList"
    />

    <!-- 股权结构图对话框 -->
    <EquityStructureChartDialog
      :visible.sync="chartDialogVisible"
      :structure-data="currentRow"
    />

    <!-- 结构对比对话框 -->
    <EquityCompareDialog
      :visible.sync="compareDialogVisible"
      :structure-data="currentRow"
    />

    <!-- 变更模拟对话框 -->
    <EquitySimulateDialog
      :visible.sync="simulateDialogVisible"
      :structure-data="currentRow"
    />

    <!-- 优化建议对话框 -->
    <EquityOptimizeDialog
      :visible.sync="optimizeDialogVisible"
      :structure-data="currentRow"
    />
  </div>
</template>

<script>
  import {
    getEquityStructureList,
    deleteEquityStructure,
    buildEquityStructureChart,
    getEquityStatistics,
    batchUpdateEquity,
    exportEquityData,
    analyzeEquityStructure,
    generateEquityReport,
  } from '@/api/stateAssets/equityStructure'
  import Pagination from '@/components/Pagination'
import { mapGetters } from 'vuex'
  import EquityStructureDialog from './components/EquityStructureDialog'
  import EquityStructureChartDialog from './components/EquityStructureChartDialog'
  import EquityCompareDialog from './components/EquityCompareDialog'
  import EquitySimulateDialog from './components/EquitySimulateDialog'
  import EquityOptimizeDialog from './components/EquityOptimizeDialog'
  import { investThemeMixin } from '../../themeMixin'

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
    name: 'EquityStructure',
    mixins: [investThemeMixin],
    components: {
      Pagination,
      EquityStructureDialog,
      EquityStructureChartDialog,
      EquityCompareDialog,
      EquitySimulateDialog,
      EquityOptimizeDialog,
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
          investorType: '',
          controlType: '',
          status: '',
          keyword: '',
        },
        dialogVisible: false,
        chartDialogVisible: false,
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
      async getList() {
        this.loading = true
        try {
          const response = await getEquityStructureList(this.queryForm)
          if (response && response.result === 200) {
            this.tableData = response.data?.tlist || response.data?.list || []
            this.total = response.data?.totalRecord || response.data?.total || 0
          } else {
            // 接口调用失败，显示空数据
            this.tableData = []
            this.total = 0
            console.warn('获取股权结构列表失败:', response?.msg)
          }
        } catch (error) {
          console.error('获取股权结构列表异常:', error)
          // 发生异常时显示空数据，不弹出错误提示
          this.tableData = []
          this.total = 0
        } finally {
          this.loading = false
        }
      },

      // 获取统计数据
      async getStatistics() {
        try {
          const response = await getEquityStatistics({})
          if (response && response.result === 200) {
            this.statistics = {
              totalStructures: response.data?.totalCount || 0,
              avgConcentration: response.data?.avgConcentration || 0,
              riskCount: response.data?.riskCount || 0,
              changedCount: response.data?.changedCount || 0
            }
          } else {
            this.statistics = { totalStructures: 0, avgConcentration: 0, riskCount: 0, changedCount: 0 }
          }
        } catch (error) {
          console.error('获取统计数据异常:', error)
          this.statistics = { totalStructures: 0, avgConcentration: 0, riskCount: 0, changedCount: 0 }
        }
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
          investorType: '',
          controlType: '',
          status: '',
          keyword: '',
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

      // 查看结构图
      handleViewChart(row) {
        this.currentRow = { ...row }
        this.chartDialogVisible = true
      },

      // 执行分析
      handleAnalyze(row) {
        const loading = this.$loading({
          lock: true,
          text: '正在分析股权结构...',
          spinner: 'el-icon-loading',
          background: 'rgba(0, 0, 0, 0.7)',
        })
        analyzeEquityStructure({ enterpriseId: row.enterpriseId })
          .then((response) => {
            loading.close()
            if (response && response.result === 200) {
              this.currentRow = { ...row, analysisResult: response.data }
              this.chartDialogVisible = true
            } else {
              this.$message.error(response?.msg || '分析失败')
            }
          })
          .catch((error) => {
            loading.close()
            console.error('分析失败:', error)
            this.$message.error('分析请求失败')
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

          const ids = this.multipleSelection.map(
            (item) => item.equityId
          )
          batchUpdateEquity({ ids })
            .then((response) => {
              loading.close()
              if (response && response.result === 200) {
                this.$message.success('批量分析完成')
                this.getList()
                this.getStatistics()
              } else {
                this.$message.error(response?.msg || '批量分析失败')
              }
            })
            .catch((error) => {
              loading.close()
              console.error('批量分析失败:', error)
              this.$message.error('批量分析请求失败')
            })
        }).catch(() => {
          // 用户取消操作
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

        exportEquityData(this.queryForm)
          .then((res) => {
            loading.close()
            if (res && res.data) {
              const blob = new Blob([res.data], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' })
              const url = window.URL.createObjectURL(blob)
              const link = document.createElement('a')
              link.href = url
              const disposition = res.headers && res.headers['content-disposition']
              let filename = `股权结构数据_${new Date().getTime()}.xlsx`
              if (disposition) {
                const match = disposition.match(/filename[^;=\n]*=((['"]).*?\2|[^;\n]*)/)
                if (match && match[1]) {
                  filename = decodeURIComponent(match[1].replace(/['"]/g, ''))
                }
              }
              link.download = filename
              link.click()
              window.URL.revokeObjectURL(url)
              this.$message.success('导出成功')
            }
          })
          .catch((error) => {
            loading.close()
            console.error('导出失败:', error)
            this.$message.error('导出失败')
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
        this.$confirm('确认生成该股权结构的分析报告？', '提示', {
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
          generateEquityReport({ enterpriseId: row.enterpriseId, equityId: row.equityId })
            .then((response) => {
              loading.close()
              if (response && response.result === 200) {
                this.$message.success('报告生成成功')
              } else {
                this.$message.error(response?.msg || '报告生成失败')
              }
            })
            .catch((error) => {
              loading.close()
              console.error('报告生成失败:', error)
              this.$message.error('报告生成请求失败')
            })
        }).catch(() => {
          // 用户取消操作
        })
      },

      // 删除
      handleDelete(row) {
        this.$confirm('确认删除该股权结构记录？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        }).then(() => {
          deleteEquityStructure(row.equityId)
            .then((response) => {
              if (response && response.result === 200) {
                this.$message.success('删除成功')
                this.getList()
                this.getStatistics()
              } else {
                this.$message.error(response && response.msg ? response.msg : '删除失败')
              }
            })
            .catch((error) => {
              console.error('删除失败:', error)
              const errMsg = (error && error.response && error.response.data && error.response.data.msg)
                || (error && error.message)
                || '删除请求失败，请稍后重试'
              this.$message.error(errMsg)
            })
        }).catch(() => {
          // 用户取消操作
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

      // 获取结构类型标签
      getStructureTypeTag(type) {
        const tagMap = {
          SIMPLE: 'success',
          COMPLEX: 'warning',
          CROSS_HOLDING: 'danger',
          PYRAMID: 'primary',
        }
        return tagMap[type] || 'info'
      },

      // 获取结构类型文本
      getStructureTypeText(type) {
        const textMap = {
          SIMPLE: '简单结构',
          COMPLEX: '复杂结构',
          CROSS_HOLDING: '交叉持股',
          PYRAMID: '金字塔结构',
        }
        return textMap[type] || type
      },

      // 获取控制类型标签
      getControlTypeTag(type) {
        const tagMap = {
          DIRECT: 'success',
          INDIRECT: 'primary',
          ABSOLUTE: 'success',
          RELATIVE: 'primary',
          JOINT: 'warning',
          NONE: 'info',
        }
        return tagMap[type] || 'info'
      },

      // 获取控制类型文本
      getControlTypeText(type) {
        const textMap = {
          DIRECT: '直接控制',
          INDIRECT: '间接控制',
          ABSOLUTE: '绝对控制',
          RELATIVE: '相对控制',
          JOINT: '共同控制',
          NONE: '无控制',
        }
        return textMap[type] || type
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

      // 获取稳定性颜色
      getStabilityColor(score) {
        if (score >= 80) return '#67C23A'
        if (score >= 60) return '#E6A23C'
        return '#F56C6C'
      },
    },
  }
</script>

<style lang="scss" scoped>
  .equity-page { padding: 16px; background: #f0f2f5; min-height: calc(100vh - 84px); }
  .page-header { display: flex; align-items: center; justify-content: space-between; margin-bottom: 14px; padding: 14px 20px; border-radius: 6px; color: #fff; }
  .page-header-left { display: flex; align-items: center; font-size: 16px; font-weight: 600; i { font-size: 22px; margin-right: 10px; } }
  .page-header-desc { font-size: 13px; opacity: 0.85; }
  ::v-deep .el-table th { background: var(--ip-light-bg, #EBF1FF) !important; color: var(--ip-secondary, #0050A0); font-weight: 600; }
  ::v-deep .el-card { border-radius: 6px; }

  .statistics-card {
    margin-bottom: 20px;
  }

  .statistics-content {
    display: flex;
    align-items: center;
  }

  .statistics-icon {
    font-size: 40px;
    margin-right: 20px;
  }

  .statistics-info {
    flex: 1;
  }

  .statistics-number {
    font-size: 24px;
    font-weight: bold;
    color: #303133;
    line-height: 1;
  }

  .statistics-label {
    font-size: 14px;
    color: #909399;
    margin-top: 5px;
  }

  .mb-20 {
    margin-bottom: 20px;
  }
</style>
