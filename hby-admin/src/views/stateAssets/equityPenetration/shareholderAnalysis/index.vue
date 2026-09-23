<template>
  <div class="shareholder-analysis-container equity-page" :style="themeVars">
    <div class="page-header" :style="{ background: 'linear-gradient(135deg, ' + themeColor + 'cc 0%, ' + themeColor + ' 100%)' }">
      <div class="page-header-left"><i class="el-icon-user"></i><span>股东穿透分析</span></div>
      <div class="page-header-desc">穿透分析股东持股比例、类型与风险等级</div>
    </div>
    <!-- 统计概览卡片 -->
    <el-row :gutter="20" class="mb-20">
      <el-col :span="6">
        <el-card shadow="hover" class="stats-card">
          <div class="stats-content">
            <div class="stats-icon">
              <i class="el-icon-user" style="color: #409EFF;"></i>
            </div>
            <div class="stats-info">
              <div class="stats-value">{{ statistics.totalShareholders }}</div>
              <div class="stats-label">股东总数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stats-card">
          <div class="stats-content">
            <div class="stats-icon">
              <i class="el-icon-share" style="color: #67C23A;"></i>
            </div>
            <div class="stats-info">
              <div class="stats-value">{{ statistics.analyzedCount }}</div>
              <div class="stats-label">已分析数量</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stats-card">
          <div class="stats-content">
            <div class="stats-icon">
              <i class="el-icon-warning" style="color: #E6A23C;"></i>
            </div>
            <div class="stats-info">
              <div class="stats-value">{{ statistics.anomalyCount }}</div>
              <div class="stats-label">异常股东</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stats-card">
          <div class="stats-content">
            <div class="stats-icon">
              <i class="el-icon-star-on" style="color: #F56C6C;"></i>
            </div>
            <div class="stats-info">
              <div class="stats-value">{{ statistics.keyShareholderCount }}</div>
              <div class="stats-label">关键股东</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 查询条件 -->
    <el-card shadow="never" class="mb-20">
      <div slot="header">
        <span>查询条件</span>
        <el-button
          style="float: right; padding: 3px 0"
          type="text"
          @click="resetQuery"
        >
          重置
        </el-button>
      </div>

      <el-form
        ref="queryForm"
        :model="queryForm"
        :inline="true"
        label-width="100px"
      >
        <el-form-item label="企业名称">
          <el-input
            v-model="queryForm.enterpriseName"
            placeholder="请输入企业名称"
            clearable
            style="width: 200px;"
          />
        </el-form-item>

        <el-form-item label="股东名称">
          <el-input
            v-model="queryForm.shareholderName"
            placeholder="请输入股东名称"
            clearable
            style="width: 200px;"
          />
        </el-form-item>

        <el-form-item label="股东类型">
          <el-select
            v-model="queryForm.shareholderType"
            placeholder="请选择股东类型"
            clearable
            style="width: 150px;"
          >
            <el-option label="企业" value="ENTERPRISE"></el-option>
            <el-option label="个人" value="INDIVIDUAL"></el-option>
            <el-option label="政府" value="GOVERNMENT"></el-option>
            <el-option label="基金" value="FUND"></el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="分析状态">
          <el-select
            v-model="queryForm.analysisStatus"
            placeholder="请选择分析状态"
            clearable
            style="width: 120px;"
          >
            <el-option label="待分析" value="PENDING"></el-option>
            <el-option label="分析中" value="ANALYZING"></el-option>
            <el-option label="已完成" value="COMPLETED"></el-option>
            <el-option label="分析失败" value="FAILED"></el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="风险等级">
          <el-select
            v-model="queryForm.riskLevel"
            placeholder="请选择风险等级"
            clearable
            style="width: 120px;"
          >
            <el-option label="低风险" value="LOW"></el-option>
            <el-option label="中风险" value="MEDIUM"></el-option>
            <el-option label="高风险" value="HIGH"></el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="持股比例">
          <el-input
            v-model="queryForm.minHoldingRatio"
            placeholder="最小比例"
            style="width: 100px;"
          />
          <span style="margin: 0 10px;">-</span>
          <el-input
            v-model="queryForm.maxHoldingRatio"
            placeholder="最大比例"
            style="width: 100px;"
          />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="handleQuery">
            <i class="el-icon-search"></i> 查询
          </el-button>
          <el-button @click="resetQuery">
            <i class="el-icon-refresh"></i> 重置
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 操作按钮 -->
    <el-card shadow="never" class="mb-20">
      <el-button type="primary" @click="handleAdd">
        <i class="el-icon-plus"></i> 新增分析
      </el-button>
      <el-button
        type="success"
        :disabled="multipleSelection.length === 0"
        @click="handleBatchAnalyze"
      >
        <i class="el-icon-data-analysis"></i> 批量分析
      </el-button>
      <el-button
        type="warning"
        :disabled="multipleSelection.length === 0"
        @click="handleBatchPenetration"
      >
        <i class="el-icon-share"></i> 批量穿透
      </el-button>
      <el-button type="info" @click="handleExport">
        <i class="el-icon-download"></i> 导出数据
      </el-button>
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
          prop="shareholderName"
          label="股东名称"
          min-width="180"
          show-overflow-tooltip
        ></el-table-column>
        <el-table-column
          prop="shareholderType"
          label="股东类型"
          width="120"
          align="center"
        >
          <template slot-scope="scope">
            <el-tag :type="getShareholderTypeTag(scope.row.shareholderType)">
              {{ getShareholderTypeText(scope.row.shareholderType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column
          prop="shareholdingRatio"
          label="持股比例"
          width="120"
          align="center"
          sortable="custom"
        >
          <template slot-scope="scope">
            <span>{{ scope.row.shareholdingRatio }}%</span>
          </template>
        </el-table-column>
        <el-table-column
          prop="penetrationLevel"
          label="穿透层级"
          width="100"
          align="center"
          sortable="custom"
        >
          <template slot-scope="scope">
            <el-tag
              size="mini"
              :type="scope.row.penetrationLevel > 5 ? 'danger' : 'primary'"
            >
              {{ scope.row.penetrationLevel }}层
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column
          prop="ultimateController"
          label="最终控制人"
          width="120"
          align="center"
          show-overflow-tooltip
        ></el-table-column>
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
              {{ {LOW:'低风险',MEDIUM:'中风险',HIGH:'高风险'}[scope.row.riskLevel] || scope.row.riskLevel }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column
          prop="analysisTime"
          label="最后分析时间"
          width="160"
          align="center"
        ></el-table-column>
        <el-table-column label="操作" width="280" align="center" fixed="right">
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
              icon="el-icon-data-analysis"
            >
              分析
            </el-button>
            <el-button
              size="mini"
              type="success"
              @click="handleViewChart(scope.row)"
              icon="el-icon-share"
            >
              图谱
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
                  :command="{ action: 'history', row: scope.row }"
                  icon="el-icon-time"
                >
                  变更历史
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

    <!-- 股东穿透分析对话框 -->
    <ShareholderAnalysisDialog
      :visible.sync="dialogVisible"
      :form-data="currentRow"
      :dialog-type="dialogType"
      @refresh="getList"
    />

    <!-- 股东穿透图谱对话框 -->
    <ShareholderChartDialog
      :visible.sync="chartDialogVisible"
      :shareholder-data="currentRow"
    />

    <!-- 变更历史对话框 -->
    <ShareholderHistoryDialog
      :visible.sync="historyDialogVisible"
      :shareholder-data="currentRow"
    />

    <!-- 生成报告对话框 -->
    <ShareholderReportDialog
      :visible.sync="reportDialogVisible"
      :row-data="currentRow"
    />
  </div>
</template>

<script>
  import {
    getShareholderAnalysisList,
    deleteShareholderAnalysis,
    performShareholderAnalysis,
    getShareholderStatistics,
    batchUpdateShareholderStatus,
    exportShareholderData,
    getShareholderChangeHistory,
    getShareholderNetworkData,
  } from '@/api/stateAssets/shareholderAnalysis'
  import Pagination from '@/components/Pagination'
import { mapGetters } from 'vuex'
  import ShareholderAnalysisDialog from './components/ShareholderAnalysisDialog'
  import ShareholderChartDialog from './components/ShareholderChartDialog'
  import ShareholderHistoryDialog from './components/ShareholderHistoryDialog'
  import ShareholderReportDialog from './components/ShareholderReportDialog'
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
    name: 'ShareholderAnalysis',
    mixins: [investThemeMixin],
    components: {
      Pagination,
      ShareholderAnalysisDialog,
      ShareholderChartDialog,
      ShareholderHistoryDialog,
      ShareholderReportDialog,
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
          shareholderName: '',
          shareholderType: '',
          analysisStatus: '',
          riskLevel: '',
          minHoldingRatio: '',
          maxHoldingRatio: '',
        },
        dialogVisible: false,
        chartDialogVisible: false,
        historyDialogVisible: false,
        reportDialogVisible: false,
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
          const response = await getShareholderAnalysisList(this.queryForm)
          if (response && response.result === 200) {
            this.tableData = response.data?.tlist || response.data?.list || []
            this.total = response.data?.totalRecord || response.data?.total || 0
          } else {
            this.tableData = []
            this.total = 0
            console.warn('获取股东分析列表失败:', response?.msg)
          }
        } catch (error) {
          console.error('获取股东分析列表异常:', error)
          this.tableData = []
          this.total = 0
        } finally {
          this.loading = false
        }
      },

      // 获取统计数据
      async getStatistics() {
        try {
          const response = await getShareholderStatistics({})
          if (response && response.result === 200) {
            this.statistics = {
              totalShareholders: response.data?.totalCount || 0,
              analyzedCount: response.data?.completedCount || 0,
              anomalyCount: response.data?.highRiskCount || 0,
              keyShareholderCount: response.data?.relatedPartyCount || 0,
            }
          } else {
            this.statistics = { totalShareholders: 0, analyzedCount: 0, anomalyCount: 0, keyShareholderCount: 0 }
          }
        } catch (error) {
          console.error('获取统计数据异常:', error)
          this.statistics = { totalShareholders: 0, analyzedCount: 0, anomalyCount: 0, keyShareholderCount: 0 }
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
          enterpriseName: '',
          shareholderName: '',
          shareholderType: '',
          analysisStatus: '',
          riskLevel: '',
          minHoldingRatio: '',
          maxHoldingRatio: '',
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

      // 执行分析
      handleAnalyze(row) {
        this.$confirm('确认对该股东执行穿透分析？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        }).then(() => {
          const loading = this.$loading({
            lock: true,
            text: '正在执行股东穿透分析...',
            spinner: 'el-icon-loading',
            background: 'rgba(0, 0, 0, 0.7)',
          })

          performShareholderAnalysis({
            analysisId: row.analysisId,
            enterpriseId: row.enterpriseId,
          })
            .then((response) => {
              loading.close()
              if (response && response.result === 200) {
                this.$message.success('股东穿透分析执行成功')
                this.getList()
                this.getStatistics()
              } else {
                this.$message.warning(response?.msg || '分析失败')
              }
            })
            .catch((error) => {
              loading.close()
              console.error('分析失败:', error)
              this.$message.error('分析请求失败')
            })
        }).catch(() => {})
      },

      // 查看图谱
      handleViewChart(row) {
        this.currentRow = { ...row }
        this.chartDialogVisible = true
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

          const analysisIds = this.multipleSelection.map((item) => item.analysisId)
          batchUpdateShareholderStatus({
            analysisIds,
            status: 'ANALYZING',
          })
            .then((response) => {
              loading.close()
              if (response && response.result === 200) {
                this.$message.success('批量分析任务已提交')
                this.getList()
                this.getStatistics()
              } else {
                this.$message.warning(response?.msg || '批量分析失败')
              }
            })
            .catch((error) => {
              loading.close()
              console.error('批量分析失败:', error)
              this.$message.error('批量分析请求失败')
            })
        }).catch(() => {})
      },

      // 批量穿透
      handleBatchPenetration() {
        if (!this.multipleSelection.length) {
          this.$message.warning('请选择要穿透的记录')
          return
        }

        this.$confirm(
          `确认对选中的${this.multipleSelection.length}条记录执行批量穿透？`,
          '提示',
          {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning',
          }
        ).then(() => {
          const loading = this.$loading({
            lock: true,
            text: '正在执行批量穿透...',
            spinner: 'el-icon-loading',
            background: 'rgba(0, 0, 0, 0.7)',
          })

          const analysisIds = this.multipleSelection.map((item) => item.analysisId)
          batchUpdateShareholderStatus({
            analysisIds,
            status: 'COMPLETED',
          })
            .then((response) => {
              loading.close()
              if (response && response.result === 200) {
                this.$message.success('批量穿透任务已完成')
                this.getList()
                this.getStatistics()
              } else {
                this.$message.warning(response?.msg || '批量穿透失败')
              }
            })
            .catch((error) => {
              loading.close()
              console.error('批量穿透失败:', error)
              this.$message.error('批量穿透请求失败')
            })
        }).catch(() => {})
      },

      // 导出数据
      handleExport() {
        const loading = this.$loading({
          lock: true,
          text: '正在导出数据...',
          spinner: 'el-icon-loading',
          background: 'rgba(0, 0, 0, 0.7)',
        })

        // 只发送用户主动设置的筛选条件，不发送分页和默认值
        const exportParams = {}
        if (this.queryForm.enterpriseName && this.queryForm.enterpriseName.trim()) {
          exportParams.enterpriseName = this.queryForm.enterpriseName.trim()
        }
        if (this.queryForm.shareholderName && this.queryForm.shareholderName.trim()) {
          exportParams.shareholderName = this.queryForm.shareholderName.trim()
        }
        if (this.queryForm.shareholderType) exportParams.shareholderType = this.queryForm.shareholderType
        if (this.queryForm.analysisStatus) exportParams.analysisStatus = this.queryForm.analysisStatus
        if (this.queryForm.riskLevel) exportParams.riskLevel = this.queryForm.riskLevel
        if (this.queryForm.minHoldingRatio && this.queryForm.minHoldingRatio.trim()) {
          exportParams.minHoldingRatio = this.queryForm.minHoldingRatio.trim()
        }
        if (this.queryForm.maxHoldingRatio && this.queryForm.maxHoldingRatio.trim()) {
          exportParams.maxHoldingRatio = this.queryForm.maxHoldingRatio.trim()
        }

        exportShareholderData(exportParams)
          .then((res) => {
            loading.close()
            // 拦截器对 blob 响应返回完整 axios response 对象，需从 res.data 取实际 blob
            const blobData = res && res.data ? res.data : res
            if (!blobData) {
              this.$message.error('导出失败：未获取到文件数据')
              return
            }
            // 后端出错时可能返回 JSON 错误信息，需要区分
            if (blobData.type && blobData.type.includes('application/json')) {
              const reader = new FileReader()
              reader.onload = () => {
                try {
                  const errJson = JSON.parse(reader.result)
                  this.$message.error(errJson.msg || '导出失败')
                } catch (e) {
                  this.$message.error('导出失败')
                }
              }
              reader.readAsText(blobData)
              return
            }
            const blob = new Blob([blobData], {
              type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet',
            })
            const url = window.URL.createObjectURL(blob)
            const link = document.createElement('a')
            link.href = url
            link.download = `股东穿透分析数据_${new Date().getTime()}.xlsx`
            document.body.appendChild(link)
            link.click()
            document.body.removeChild(link)
            window.URL.revokeObjectURL(url)
            this.$message.success('导出成功')
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
          case 'history':
            this.currentRow = { ...row }
            this.historyDialogVisible = true
            break
          case 'report':
            this.handleGenerateReport(row)
            break
          case 'delete':
            this.handleDelete(row)
            break
        }
      },

      // 生成报告 - 打开报告弹窗
      handleGenerateReport(row) {
        this.currentRow = { ...row }
        this.reportDialogVisible = true
      },

      // 删除
      handleDelete(row) {
        this.$confirm('确认删除该股东穿透分析记录？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        }).then(() => {
          deleteShareholderAnalysis(row.analysisId)
            .then((response) => {
              if (response && response.result === 200) {
                this.$message.success('删除成功')
                this.getList()
                this.getStatistics()
              } else {
                this.$message.warning(response?.msg || '删除失败')
              }
            })
            .catch((error) => {
              console.error('删除失败:', error)
              this.$message.error('删除请求失败')
            })
        }).catch(() => {})
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

      // 获取股东类型标签
      getShareholderTypeTag(type) {
        const tagMap = {
          ENTERPRISE: 'success',
          INDIVIDUAL: 'primary',
          GOVERNMENT: 'danger',
          FUND: 'warning',
        }
        return tagMap[type] || 'info'
      },

      // 获取股东类型文本（统一使用 ENTERPRISE/INDIVIDUAL/GOVERNMENT/FUND）
      getShareholderTypeText(type) {
        const textMap = {
          ENTERPRISE: '企业',
          INDIVIDUAL: '个人',
          GOVERNMENT: '政府',
          FUND: '基金',
        }
        return textMap[type] || type || '-'
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
        return textMap[status] || '未知'
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

  .stats-card { margin-bottom: 20px; }
  .stats-content { display: flex; align-items: center; }
  .stats-icon { font-size: 40px; margin-right: 20px; }
  .stats-info { flex: 1; }
  .stats-value { font-size: 24px; font-weight: bold; color: #303133; line-height: 1; }
  .stats-label { font-size: 14px; color: #909399; margin-top: 5px; }
  .mb-20 { margin-bottom: 20px; }
</style>
