<template>
  <div class="control-chain-analysis-container equity-page" :style="themeVars">
    <div class="page-header" :style="{ background: 'linear-gradient(135deg, ' + themeColor + 'cc 0%, ' + themeColor + ' 100%)' }">
      <div class="page-header-left"><i class="el-icon-connection"></i><span>控制链穿透分析</span></div>
      <div class="page-header-desc">穿透分析控制链条长度、中间节点与控制强度</div>
    </div>
    <!-- 统计概览 -->
    <el-row :gutter="20" class="mb-20">
      <el-col :span="6">
        <el-card shadow="hover" class="stats-card">
          <div class="stats-content">
            <div class="stats-icon">
              <i class="el-icon-connection" style="color: #409EFF;"></i>
            </div>
            <div class="stats-info">
              <div class="stats-value">{{ statistics.totalChains }}</div>
              <div class="stats-label">控制链总数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stats-card">
          <div class="stats-content">
            <div class="stats-icon">
              <i class="el-icon-star-on" style="color: #67C23A;"></i>
            </div>
            <div class="stats-info">
              <div class="stats-value">{{ statistics.ultimateControllers }}</div>
              <div class="stats-label">实际控制人</div>
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
              <div class="stats-value">{{ statistics.complexChains }}</div>
              <div class="stats-label">复杂控制链</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stats-card">
          <div class="stats-content">
            <div class="stats-icon">
              <i class="el-icon-cpu" style="color: #F56C6C;"></i>
            </div>
            <div class="stats-info">
              <div class="stats-value">{{ statistics.analyzingCount }}</div>
              <div class="stats-label">分析中</div>
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
          @click="resetSearchForm"
        >
          重置
        </el-button>
      </div>
      
      <el-form
        ref="searchForm"
        :model="searchForm"
        :inline="true"
        label-width="100px"
      >
        <el-form-item label="企业名称">
          <el-input
            v-model="searchForm.enterpriseName"
            placeholder="请输入企业名称"
            clearable
            style="width: 200px;"
            @keyup.enter.native="handleSearch"
          />
        </el-form-item>

        <el-form-item label="统计周期">
          <el-select
            v-model="searchForm.statPeriod"
            placeholder="请选择统计周期"
            clearable
            style="width: 120px;"
          >
            <el-option label="月度" value="月度"></el-option>
            <el-option label="季度" value="季度"></el-option>
            <el-option label="年度" value="年度"></el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="控制强度">
          <el-input
            v-model="searchForm.minControlStrength"
            placeholder="最小强度"
            style="width: 100px;"
          />
          <span style="margin: 0 10px;">-</span>
          <el-input
            v-model="searchForm.maxControlStrength"
            placeholder="最大强度"
            style="width: 100px;"
          />
        </el-form-item>

        <el-form-item label="最大链长">
          <el-input-number
            v-model="searchForm.maxChainLength"
            :min="1"
            :max="15"
            placeholder="最大长度"
            style="width: 120px;"
          />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="handleSearch">
            <i class="el-icon-search"></i> 查询
          </el-button>
          <el-button @click="resetSearchForm">
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
        :disabled="selectedChains.length === 0"
        @click="handleBatchAnalyze"
      >
        <i class="el-icon-data-analysis"></i> 批量分析
      </el-button>
      <el-button
        type="warning"
        :disabled="selectedChains.length === 0"
        @click="handleBatchVisualize"
      >
        <i class="el-icon-view"></i> 批量可视化
      </el-button>
      <el-button type="info" @click="handleExport">
        <i class="el-icon-download"></i> 导出数据
      </el-button>
    </el-card>

    <!-- 数据表格 -->
    <el-card shadow="never">
      <div slot="header">
        <span>控制链分析列表</span>
        <span class="table-count">（共 {{ total }} 条）</span>
      </div>
      
      <el-table
        v-loading="loading"
        :data="chainsList"
        @selection-change="handleSelectionChange"
        stripe
        border
      >
        <el-table-column type="selection" width="55" />
        <el-table-column label="企业名称" prop="enterpriseName" min-width="200">
          <template slot-scope="scope">
            <el-link type="primary" @click="handleView(scope.row)">{{ scope.row.enterpriseName }}</el-link>
          </template>
        </el-table-column>
        <el-table-column label="控制链总数" prop="totalChains" width="100" align="center" />
        <el-table-column label="直接控制" prop="directChains" width="90" align="center" />
        <el-table-column label="间接控制" prop="indirectChains" width="90" align="center" />
        <el-table-column label="平均控制强度" prop="avgControlStrength" width="120" align="center">
          <template slot-scope="scope">
            <span>{{ scope.row.avgControlStrength }}%</span>
          </template>
        </el-table-column>
        <el-table-column label="最大链长" prop="maxChainLength" width="90" align="center">
          <template slot-scope="scope">
            <el-tag size="mini" :type="scope.row.maxChainLength > 4 ? 'danger' : 'primary'">{{ scope.row.maxChainLength }}级</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="环路数" prop="loopCount" width="80" align="center">
          <template slot-scope="scope">
            <el-tag size="mini" :type="scope.row.loopCount > 0 ? 'danger' : 'success'">{{ scope.row.loopCount }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="高风险数" prop="highRiskCount" width="90" align="center">
          <template slot-scope="scope">
            <el-tag size="mini" type="danger" v-if="scope.row.highRiskCount > 0">{{ scope.row.highRiskCount }}</el-tag>
            <span v-else>0</span>
          </template>
        </el-table-column>
        <el-table-column label="统计周期" prop="statPeriod" width="100" align="center" />
        <el-table-column label="统计时间" prop="statTime" width="150" align="center" />
        <el-table-column label="操作" width="320" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" @click="handleView(scope.row)">查看</el-button>
            <el-button size="mini" type="warning" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button size="mini" type="primary" @click="handleAnalyze(scope.row)">分析</el-button>
            <el-button size="mini" type="success" @click="handleVisualize(scope.row)">可视化</el-button>
            <el-dropdown @command="handleCommand($event, scope.row)">
              <el-button size="mini">
                更多<i class="el-icon-arrow-down el-icon--right"></i>
              </el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item command="chainDetail">链条详情</el-dropdown-item>
                <el-dropdown-item command="riskAssessment">风险评估</el-dropdown-item>
                <el-dropdown-item command="delete" divided>删除</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </template>
        </el-table-column>
      </el-table>
      
      <!-- 分页 -->
      <div class="pagination-container">
        <Pagination
          v-show="total > 0"
          :total="total"
          :page.sync="searchForm.pageNum"
          :limit.sync="searchForm.pageSize"
          @pagination="getChainsList"
        />
      </div>
    </el-card>

    <!-- 对话框组件 -->
    <ControlChainDialog
      :visible.sync="chainDialogVisible"
      :chain-data="currentChain"
      :dialog-type="dialogType"
      @refresh="getChainsList"
    />
    
    <ControlChainVisualizationDialog
      :visible.sync="visualizationDialogVisible"
      :chain-data="currentChain"
    />
    
    <ControlChainDetailDialog
      :visible.sync="detailDialogVisible"
      :chain-data="currentChain"
    />

    <RiskAssessmentDialog
      :visible.sync="riskDialogVisible"
      :chain-data="currentChain"
    />
  </div>
</template>

<script>
import Pagination from '@/components/Pagination'
import { mapGetters } from 'vuex'
import ControlChainDialog from './components/ControlChainDialog'
import ControlChainVisualizationDialog from './components/ControlChainVisualizationDialog'
import ControlChainDetailDialog from './components/ControlChainDetailDialog'
import RiskAssessmentDialog from './components/RiskAssessmentDialog'
import {
  getControlChainsList,
  getControlChainStatistics,
  deleteControlChain,
  batchAnalyzeControlChains,
  exportControlChainData,
  getControlChainVisualization,
  startControlChainAnalysis
} from '@/api/stateAssets/controlChainAnalysis'
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
  name: 'ControlChainAnalysis',
  mixins: [investThemeMixin],
  components: {
    Pagination,
    ControlChainDialog,
    ControlChainVisualizationDialog,
    ControlChainDetailDialog,
    RiskAssessmentDialog
  },
  data() {
    return {
      loading: false,
      chainsList: [],
      selectedChains: [],
      total: 0,
      statistics: {
        totalChains: 0,
        ultimateControllers: 0,
        complexChains: 0,
        analyzingCount: 0
      },
      searchForm: {
        enterpriseName: '',
        statPeriod: '',
        minControlStrength: '',
        maxControlStrength: '',
        maxChainLength: null,
        pageNum: 1,
        pageSize: 10
      },
      chainDialogVisible: false,
      visualizationDialogVisible: false,
      detailDialogVisible: false,
      riskDialogVisible: false,
      currentChain: {},
      dialogType: 'add'
    }
  },
  created() {
    this.getChainsList()
    this.getStatistics()
  },
  methods: {
    // 获取统计数据
    async getStatistics() {
      try {
        const response = await getControlChainStatistics({})
        if (response && response.result === 200) {
          this.statistics = {
            totalChains: response.data?.totalChains || 0,
            ultimateControllers: response.data?.totalEnterprises || 0,
            complexChains: response.data?.highRiskCount || 0,
            analyzingCount: response.data?.loopCount || 0,
          }
        } else {
          this.statistics = { totalChains: 0, ultimateControllers: 0, complexChains: 0, analyzingCount: 0 }
        }
      } catch (error) {
        console.error('获取统计数据异常:', error)
        this.statistics = { totalChains: 0, ultimateControllers: 0, complexChains: 0, analyzingCount: 0 }
      }
    },

    // 获取控制链列表
    async getChainsList() {
      this.loading = true
      try {
        // 构建干净的请求参数，避免el-input-number默认值污染
        const params = {
          pageNum: this.searchForm.pageNum,
          pageSize: this.searchForm.pageSize,
        }
        if (this.searchForm.enterpriseName && this.searchForm.enterpriseName.trim()) {
          params.enterpriseName = this.searchForm.enterpriseName.trim()
        }
        if (this.searchForm.statPeriod) {
          params.statPeriod = this.searchForm.statPeriod
        }
        if (this.searchForm.minControlStrength && this.searchForm.minControlStrength.trim()) {
          params.minControlStrength = this.searchForm.minControlStrength.trim()
        }
        if (this.searchForm.maxControlStrength && this.searchForm.maxControlStrength.trim()) {
          params.maxControlStrength = this.searchForm.maxControlStrength.trim()
        }
        // maxChainLength: 只在用户明确设置时才传（排除el-input-number的min默认值）
        if (this.searchForm.maxChainLength && this.searchForm.maxChainLength > 1) {
          params.maxChainLength = this.searchForm.maxChainLength
        }

        const response = await getControlChainsList(params)
        if (response && response.result === 200) {
          this.chainsList = response.data?.tlist || response.data?.list || []
          this.total = response.data?.totalRecord || response.data?.total || 0
        } else {
          this.chainsList = []
          this.total = 0
          console.warn('获取控制链列表失败:', response?.msg)
        }
      } catch (error) {
        console.error('获取控制链列表异常:', error)
        this.chainsList = []
        this.total = 0
      } finally {
        this.loading = false
      }
    },

    // 搜索
    handleSearch() {
      this.searchForm.pageNum = 1
      this.getChainsList()
    },

    // 重置搜索表单
    resetSearchForm() {
      this.$refs.searchForm.resetFields()
      this.searchForm = {
        enterpriseName: '',
        statPeriod: '',
        minControlStrength: '',
        maxControlStrength: '',
        maxChainLength: null,
        pageNum: 1,
        pageSize: 10
      }
      this.getChainsList()
    },

    // 表格选择变化
    handleSelectionChange(selection) {
      this.selectedChains = selection
    },

    // 新增分析
    handleAdd() {
      this.currentChain = {}
      this.dialogType = 'add'
      this.chainDialogVisible = true
    },

    // 查看控制链
    handleView(row) {
      this.currentChain = { ...row }
      this.dialogType = 'view'
      this.chainDialogVisible = true
    },

    // 编辑控制链
    handleEdit(row) {
      this.currentChain = { ...row }
      this.dialogType = 'edit'
      this.chainDialogVisible = true
    },

    // 分析控制链
    handleAnalyze(row) {
      this.$confirm('确认对该企业执行控制链穿透分析？', '提示', {
        confirmButtonText: '开始分析',
        cancelButtonText: '取消',
        type: 'info'
      }).then(() => {
        const loading = this.$loading({ lock: true, text: '正在执行控制链穿透分析...', background: 'rgba(0, 0, 0, 0.7)' })
        startControlChainAnalysis({ statId: row.statId, enterpriseId: row.enterpriseId })
          .then((response) => {
            loading.close()
            if (response && response.result === 200) {
              const data = response.data || {}
              this.$alert(
                `<div style="line-height:2;">
                  <p><b>企业：</b>${row.enterpriseName}</p>
                  <p><b>分析状态：</b><span style="color:#67C23A;font-weight:bold;">分析完成</span></p>
                  <p><b>控制链总数：</b>${data.totalChains || row.totalChains || 0} 条</p>
                  <p><b>高风险数：</b>${data.highRiskCount || row.highRiskCount || 0}</p>
                  <p><b>环路数：</b>${data.loopCount || row.loopCount || 0}</p>
                  <p><b>分析结果：</b>${data.message || '分析完成'}</p>
                  <p><b>分析时间：</b>${data.startTime || new Date().toLocaleString()}</p>
                </div>`,
                '控制链分析结果',
                { dangerouslyUseHTMLString: true, confirmButtonText: '确定' }
              )
              this.getChainsList()
              this.getStatistics()
            } else {
              this.$message.error(response?.msg || '分析失败')
            }
          })
          .catch((error) => {
            loading.close()
            console.error('分析失败:', error)
            this.$message.error('分析请求失败')
          })
      }).catch(() => {})
    },

    // 可视化控制链
    handleVisualize(row) {
      this.currentChain = { ...row }
      this.visualizationDialogVisible = true
    },

    // 批量分析
    handleBatchAnalyze() {
      if (this.selectedChains.length === 0) {
        this.$message.warning('请选择要分析的控制链')
        return
      }
      this.$confirm(
        `确认对选中的${this.selectedChains.length}条记录执行批量分析？`,
        '提示',
        { confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning' }
      ).then(() => {
        const loading = this.$loading({
          lock: true,
          text: '正在执行批量分析...',
          spinner: 'el-icon-loading',
          background: 'rgba(0, 0, 0, 0.7)',
        })

        const chainIds = this.selectedChains.map((item) => item.statId)
        batchAnalyzeControlChains({ ids: chainIds })
          .then((response) => {
            loading.close()
            if (response && response.result === 200) {
              this.$message.success('批量分析任务已提交')
              this.getChainsList()
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

    // 批量可视化
    handleBatchVisualize() {
      if (this.selectedChains.length === 0) {
        this.$message.warning('请选择要可视化的控制链')
        return
      }
      this.$confirm(
        `确认对选中的${this.selectedChains.length}条记录执行批量可视化？`,
        '提示',
        { confirmButtonText: '确定', cancelButtonText: '取消', type: 'info' }
      ).then(() => {
        // 批量可视化：逐条打开可视化对话框（以第一条为主展示）
        this.currentChain = { ...this.selectedChains[0] }
        this.visualizationDialogVisible = true
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

      const exportParams = {}
      if (this.searchForm.enterpriseName && this.searchForm.enterpriseName.trim()) {
        exportParams.enterpriseName = this.searchForm.enterpriseName.trim()
      }
      if (this.searchForm.statPeriod) {
        exportParams.statPeriod = this.searchForm.statPeriod
      }

      exportControlChainData(exportParams)
        .then((res) => {
          loading.close()
          if (res) {
            const blob = new Blob([res], { type: 'text/csv;charset=utf-8' })
            const url = window.URL.createObjectURL(blob)
            const link = document.createElement('a')
            link.href = url
            link.download = '控制链穿透分析_' + new Date().getTime() + '.csv'
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
    handleCommand(command, row) {
      switch (command) {
        case 'chainDetail':
          this.currentChain = { ...row }
          this.detailDialogVisible = true
          break
        case 'riskAssessment':
          this.handleRiskAssessment(row)
          break
        case 'delete':
          this.handleDelete(row)
          break
      }
    },

    // 风险评估
    handleRiskAssessment(row) {
      this.currentChain = { ...row }
      this.riskDialogVisible = true
    },

    // 删除控制链
    handleDelete(row) {
      this.$confirm('确认删除该控制链分析？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteControlChain({ statId: row.statId })
          .then((response) => {
            if (response && response.result === 200) {
              this.$message.success('删除成功')
              this.getChainsList()
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

    // 获取控制类型标签
    getControlTypeTag(type) {
      const tagMap = {
        'DIRECT': 'primary',
        'INDIRECT': 'success',
        'JOINT': 'warning',
        'ULTIMATE': 'danger'
      }
      return tagMap[type] || 'info'
    },

    // 获取控制类型文本
    getControlTypeText(type) {
      const textMap = {
        'DIRECT': '直接控制',
        'INDIRECT': '间接控制',
        'JOINT': '共同控制',
        'ULTIMATE': '实际控制'
      }
      return textMap[type] || type
    },

    // 获取状态标签类型
    getStatusTag(status) {
      const tagMap = {
        'PENDING': 'info',
        'ANALYZING': 'warning',
        'COMPLETED': 'success',
        'FAILED': 'danger'
      }
      return tagMap[status] || 'info'
    },

    // 获取状态文本
    getStatusText(status) {
      const textMap = {
        'PENDING': '待分析',
        'ANALYZING': '分析中',
        'COMPLETED': '已完成',
        'FAILED': '分析失败'
      }
      return textMap[status] || status
    }
  }
}
</script>

<style lang="scss" scoped>
.equity-page { padding: 16px; background: #f0f2f5; min-height: calc(100vh - 84px); }
.page-header { display: flex; align-items: center; justify-content: space-between; margin-bottom: 14px; padding: 14px 20px; border-radius: 6px; color: #fff; }
.page-header-left { display: flex; align-items: center; font-size: 16px; font-weight: 600; i { font-size: 22px; margin-right: 10px; } }
.page-header-desc { font-size: 13px; opacity: 0.85; }
::v-deep .el-table th { background: var(--ip-light-bg, #EBF1FF) !important; color: var(--ip-secondary, #0050A0); font-weight: 600; }
::v-deep .el-card { border-radius: 6px; }

.mb-20 { margin-bottom: 20px; }
.stats-card {
  .stats-content { display: flex; align-items: center;
    .stats-icon { font-size: 40px; margin-right: 20px; }
    .stats-info {
      .stats-value { font-size: 28px; font-weight: bold; color: #303133; line-height: 1; }
      .stats-label { font-size: 14px; color: #909399; margin-top: 5px; }
    }
  }
}
.table-count { color: #909399; font-size: 14px; }
.pagination-container { margin-top: 20px; text-align: right; }
</style>
