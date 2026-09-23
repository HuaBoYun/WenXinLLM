<template>
  <div class="financial-compliance-container financial-ext-page">
    <div class="page-header" :style="{ background: 'linear-gradient(135deg, ' + themeColor + 'cc 0%, ' + themeColor + ' 100%)' }">
      <div class="page-header-left"><i class="el-icon-shield"></i><span>财务合规</span></div>
      <div class="page-header-desc">监控企业财务合规检查、违规事项与整改跟踪</div>
    </div>
    <!-- 统计概览 -->
    <el-row :gutter="20" class="mb-20">
      <el-col :span="6">
        <el-card shadow="hover" class="stats-card">
          <div class="stats-content">
            <div class="stats-icon">
              <i class="el-icon-shield" style="color: #eb2f96;"></i>
            </div>
            <div class="stats-info">
              <div class="stats-value">{{ statistics.totalCompliance }}</div>
              <div class="stats-label">合规检查总数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stats-card">
          <div class="stats-content">
            <div class="stats-icon">
              <i class="el-icon-success" style="color: #67C23A;"></i>
            </div>
            <div class="stats-info">
              <div class="stats-value">{{ statistics.compliantEnterprises }}</div>
              <div class="stats-label">合规企业</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stats-card">
          <div class="stats-content">
            <div class="stats-icon">
              <i class="el-icon-warning" style="color: #F56C6C;"></i>
            </div>
            <div class="stats-info">
              <div class="stats-value">{{ statistics.violationCases }}</div>
              <div class="stats-label">违规案例</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stats-card">
          <div class="stats-content">
            <div class="stats-icon">
              <i class="el-icon-data-analysis" style="color: #E6A23C;"></i>
            </div>
            <div class="stats-info">
              <div class="stats-value">{{ statistics.complianceRate }}%</div>
              <div class="stats-label">合规率</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 合规分析图表 -->
    <el-row :gutter="20" class="mb-20">
      <el-col :span="12">
        <el-card shadow="never">
          <div slot="header">
            <span>合规状态分布</span>
            <el-button
              style="float: right; padding: 3px 0"
              type="text"
              @click="refreshComplianceChart"
            >
              刷新
            </el-button>
          </div>
          <div ref="complianceDistributionChart" class="chart-container"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="never">
          <div slot="header">
            <span>合规趋势分析</span>
            <el-button
              style="float: right; padding: 3px 0"
              type="text"
              @click="refreshTrendChart"
            >
              刷新
            </el-button>
          </div>
          <div ref="complianceTrendChart" class="chart-container"></div>
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
          />
        </el-form-item>
        
        <el-form-item label="合规状态">
          <el-select
            v-model="searchForm.complianceStatus"
            placeholder="请选择合规状态"
            clearable
            style="width: 120px;"
          >
            <el-option label="合规" value="COMPLIANT"></el-option>
            <el-option label="违规" value="VIOLATION"></el-option>
            <el-option label="待检查" value="PENDING"></el-option>
            <el-option label="检查中" value="CHECKING"></el-option>
          </el-select>
        </el-form-item>
        
        <el-form-item label="违规类型">
          <el-select
            v-model="searchForm.violationType"
            placeholder="请选择违规类型"
            clearable
            style="width: 150px;"
          >
            <el-option label="财务造假" value="FINANCIAL_FRAUD"></el-option>
            <el-option label="信息披露违规" value="DISCLOSURE_VIOLATION"></el-option>
            <el-option label="内控缺陷" value="INTERNAL_CONTROL_DEFECT"></el-option>
            <el-option label="关联交易违规" value="RELATED_PARTY_VIOLATION"></el-option>
            <el-option label="资金占用" value="FUND_OCCUPATION"></el-option>
          </el-select>
        </el-form-item>
        
        <el-form-item label="风险等级">
          <el-select
            v-model="searchForm.riskLevel"
            placeholder="请选择风险等级"
            clearable
            style="width: 120px;"
          >
            <el-option label="低风险" value="LOW"></el-option>
            <el-option label="中风险" value="MEDIUM"></el-option>
            <el-option label="高风险" value="HIGH"></el-option>
            <el-option label="极高风险" value="CRITICAL"></el-option>
          </el-select>
        </el-form-item>
        
        <el-form-item label="检查时间">
          <el-date-picker
            v-model="searchForm.checkDateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            style="width: 240px;"
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
        <i class="el-icon-plus"></i> 新增检查
      </el-button>
      <el-button
        type="warning"
        :disabled="selectedCompliances.length === 0"
        @click="handleBatchAnalyze"
      >
        <i class="el-icon-data-analysis"></i> 批量分析
      </el-button>
      <el-button
        type="success"
        :disabled="selectedCompliances.length === 0"
        @click="handleBatchCheck"
      >
        <i class="el-icon-check"></i> 批量检查
      </el-button>
      <el-button type="info" @click="handleExport">
        <i class="el-icon-download"></i> 导出数据
      </el-button>
      <el-button type="danger" @click="handleGenerateReport">
        <i class="el-icon-document"></i> 合规报告
      </el-button>
    </el-card>

    <!-- 数据表格 -->
    <el-card shadow="never">
      <div slot="header">
        <span>财务合规监管列表</span>
        <span class="table-count">（共 {{ total }} 条）</span>
      </div>
      
      <el-table
        v-loading="loading"
        :data="compliancesList"
        @selection-change="handleSelectionChange"
        stripe
        border
      >
        <el-table-column type="selection" width="55" />
        
        <el-table-column label="企业名称" prop="enterpriseName" min-width="180">
          <template slot-scope="scope">
            <el-link type="primary" @click="handleView(scope.row)">
              {{ scope.row.enterpriseName }}
            </el-link>
          </template>
        </el-table-column>

        <el-table-column label="企业编号" prop="companyId" width="100" align="center">
          <template slot-scope="scope">
            {{ scope.row.companyId || '-' }}
          </template>
        </el-table-column>

        <el-table-column label="检查类型" prop="checkType" width="120" align="center">
          <template slot-scope="scope">
            <span>{{ getCheckTypeText(scope.row.checkType) }}</span>
          </template>
        </el-table-column>

        <el-table-column label="报告期间" prop="period" width="100" align="center">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.period" type="success" size="mini">{{ scope.row.period }}</el-tag>
            <span v-else>-</span>
          </template>
        </el-table-column>

        <el-table-column label="合规状态" prop="complianceStatus" width="100" align="center">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.complianceStatus" :type="getComplianceStatusType(scope.row.complianceStatus)" size="mini">
              {{ getComplianceStatusText(scope.row.complianceStatus) }}
            </el-tag>
            <span v-else>-</span>
          </template>
        </el-table-column>

        <el-table-column label="风险等级" prop="riskLevel" width="100" align="center">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.riskLevel" :type="getRiskLevelType(scope.row.riskLevel)" size="mini">
              {{ getRiskLevelText(scope.row.riskLevel) }}
            </el-tag>
            <span v-else>-</span>
          </template>
        </el-table-column>

        <el-table-column label="合规评分" prop="complianceScore" width="100" align="center">
          <template slot-scope="scope">
            <span v-if="scope.row.complianceScore" :style="{color: getScoreColor(scope.row.complianceScore)}">
              {{ scope.row.complianceScore }}
            </span>
            <span v-else>-</span>
          </template>
        </el-table-column>

        <el-table-column label="违规类型" prop="violationType" width="120" align="center">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.violationType" type="danger" size="mini">
              {{ getViolationTypeText(scope.row.violationType) }}
            </el-tag>
            <span v-else>-</span>
          </template>
        </el-table-column>

        <el-table-column label="检查人员" prop="inspector" width="100" align="center">
          <template slot-scope="scope">
            {{ scope.row.inspector || '-' }}
          </template>
        </el-table-column>

        <el-table-column label="检查日期" prop="checkDate" width="110" align="center">
          <template slot-scope="scope">
            {{ formatDate(scope.row.checkDate) }}
          </template>
        </el-table-column>

        <el-table-column label="审计状态" prop="auditStatus" width="100" align="center">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.auditStatus" :type="scope.row.auditStatus === '已审计' ? 'success' : 'warning'" size="mini">
              {{ scope.row.auditStatus }}
            </el-tag>
            <span v-else>-</span>
          </template>
        </el-table-column>
        
        <el-table-column label="操作" width="280" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" @click="handleView(scope.row)">查看</el-button>
            <el-button size="mini" type="primary" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button size="mini" type="success" @click="handleCheck(scope.row)">检查</el-button>
            <el-button size="mini" type="warning" @click="handleViolation(scope.row)">违规</el-button>
            <el-button size="mini" type="danger" @click="handleDelete(scope.row)">删除</el-button>
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
          @pagination="getCompliancesList"
        />
      </div>
    </el-card>

    <!-- 对话框组件 -->
    <FinancialComplianceDialog
      :visible.sync="complianceDialogVisible"
      :compliance-data="currentCompliance"
      :dialog-type="dialogType"
      @refresh="getCompliancesList"
    />

    <ComplianceCheckDialog
      :visible.sync="checkDialogVisible"
      :check-data="currentCompliance"
    />

    <ViolationManagementDialog
      :visible.sync="violationDialogVisible"
      :violation-data="currentCompliance"
    />
  </div>
</template>

<script>
import * as echarts from 'echarts'
import { mapGetters } from 'vuex'
import Pagination from '@/components/Pagination'
import FinancialComplianceDialog from './components/FinancialComplianceDialog'
import ComplianceCheckDialog from './components/ComplianceCheckDialog'
import ViolationManagementDialog from './components/ViolationManagementDialog'
import {
  getFinancialComplianceList,
  getComplianceStatistics,
  getComplianceTrend,
  getComplianceDistribution,
  executeComplianceCheck,
  batchComplianceCheck,
  batchComplianceAnalyze,
  exportComplianceData,
  generateComplianceReport,
  deleteFinancialCompliance
} from '@/api/stateAssets/financialCompliance'

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
  name: 'FinancialCompliance',
  components: {
    Pagination,
    FinancialComplianceDialog,
    ComplianceCheckDialog,
    ViolationManagementDialog
  },
  data() {
    return {
      loading: false,
      compliancesList: [],
      selectedCompliances: [],
      total: 0,
      statistics: {
        totalCompliance: 0,
        compliantEnterprises: 0,
        violationCases: 0,
        complianceRate: 0
      },
      searchForm: {
        enterpriseName: '',
        complianceStatus: '',
        violationType: '',
        riskLevel: '',
        checkDateRange: [],
        pageNum: 1,
        pageSize: 10
      },
      // 图表实例
      complianceDistributionChart: null,
      complianceTrendChart: null,
      // 对话框相关
      complianceDialogVisible: false,
      checkDialogVisible: false,
      violationDialogVisible: false,
      currentCompliance: {},
      dialogType: 'add',
      // 加载状态
      batchCheckLoading: false,
      exportLoading: false,
      reportLoading: false
    }
  },
  created() {
    this.getStatistics()
    this.getCompliancesList()
  },
  mounted() {
    this.initCharts()
    this.loadChartData()
  },
  beforeDestroy() {
    this.destroyCharts()
  },
  methods: {
    // 获取统计数据
    getStatistics() {
      getComplianceStatistics().then(response => {
        if (response && response.data) {
          this.statistics = {
            totalCompliance: response.data.totalCompliance || 0,
            compliantEnterprises: response.data.compliantEnterprises || 0,
            violationCases: response.data.violationCases || 0,
            complianceRate: response.data.complianceRate || 0
          }
        }
      }).catch(err => {
        console.error('获取统计数据失败:', err)
        // 使用默认数据
        this.statistics = {
          totalCompliance: 0,
          compliantEnterprises: 0,
          violationCases: 0,
          complianceRate: 0
        }
      })
    },

    // 获取合规列表
    getCompliancesList() {
      this.loading = true
      const params = {
        ...this.searchForm,
        checkDateStart: this.searchForm.checkDateRange ? this.searchForm.checkDateRange[0] : null,
        checkDateEnd: this.searchForm.checkDateRange ? this.searchForm.checkDateRange[1] : null
      }
      getFinancialComplianceList(params).then(response => {
        if (response && response.data) {
          this.compliancesList = response.data.list || []
          this.total = response.data.total || 0
        }
        this.loading = false
      }).catch(err => {
        console.error('获取合规列表失败:', err)
        this.$message.error('获取合规列表失败: ' + (err.message || '未知错误'))
        this.loading = false
      })
    },

    // 初始化图表
    initCharts() {
      this.complianceDistributionChart = echarts.init(this.$refs.complianceDistributionChart)
      this.complianceTrendChart = echarts.init(this.$refs.complianceTrendChart)
      
      // 监听窗口大小变化
      window.addEventListener('resize', this.handleResize)
    },

    // 加载图表数据
    loadChartData() {
      // 获取合规分布数据
      getComplianceDistribution().then(response => {
        if (response && response.data) {
          this.updateComplianceDistributionChart(response.data)
        }
      }).catch(err => {
        console.error('获取合规分布数据失败:', err)
      })

      // 获取合规趋势数据
      getComplianceTrend().then(response => {
        if (response && response.data) {
          this.updateComplianceTrendChart(response.data)
        }
      }).catch(err => {
        console.error('获取合规趋势数据失败:', err)
      })
    },

    // 更新合规分布图表
    updateComplianceDistributionChart(data) {
      const option = {
        title: {
          text: '合规状态分布',
          left: 'center',
          textStyle: { fontSize: 14 }
        },
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b}: {c} ({d}%)'
        },
        series: [{
          name: '合规状态',
          type: 'pie',
          radius: '60%',
          data: data,
          emphasis: {
            itemStyle: {
              shadowBlur: 10,
              shadowOffsetX: 0,
              shadowColor: 'rgba(0, 0, 0, 0.5)'
            }
          }
        }]
      }
      this.complianceDistributionChart.setOption(option)
    },

    // 更新合规趋势图表
    updateComplianceTrendChart(data) {
      const option = {
        title: {
          text: '合规趋势分析',
          left: 'center',
          textStyle: { fontSize: 14 }
        },
        tooltip: {
          trigger: 'axis'
        },
        legend: {
          data: ['合规', '违规'],
          bottom: 10
        },
        xAxis: {
          type: 'category',
          data: data.map(item => item.date)
        },
        yAxis: {
          type: 'value',
          name: '企业数量'
        },
        series: [
          {
            name: '合规',
            type: 'line',
            data: data.map(item => item.compliant),
            smooth: true,
            itemStyle: { color: '#67C23A' }
          },
          {
            name: '违规',
            type: 'line',
            data: data.map(item => item.violation),
            smooth: true,
            itemStyle: { color: '#F56C6C' }
          }
        ]
      }
      this.complianceTrendChart.setOption(option)
    },

    // 搜索
    handleSearch() {
      this.searchForm.pageNum = 1
      this.getCompliancesList()
    },

    // 重置搜索表单
    resetSearchForm() {
      this.$refs.searchForm.resetFields()
      this.searchForm = {
        enterpriseName: '',
        complianceStatus: '',
        violationType: '',
        riskLevel: '',
        checkDateRange: [],
        pageNum: 1,
        pageSize: 10
      }
      this.getCompliancesList()
    },

    // 表格选择变化
    handleSelectionChange(selection) {
      this.selectedCompliances = selection
    },

    // 批量检查
    handleBatchCheck() {
      if (this.selectedCompliances.length === 0) {
        this.$message.warning('请选择要检查的合规记录')
        return
      }
      this.$confirm('确认对选中的' + this.selectedCompliances.length + '条记录进行批量检查？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.batchCheckLoading = true
        const ids = this.selectedCompliances.map(item => item.id)
        batchComplianceCheck({ ids }).then(() => {
          this.$message.success('批量检查已提交')
          this.getCompliancesList()
          this.getStatistics()
        }).catch(err => {
          this.$message.error('批量检查失败: ' + (err.message || '未知错误'))
        }).finally(() => {
          this.batchCheckLoading = false
        })
      }).catch(() => {})
    },

    // 导出数据
    handleExport() {
      this.$confirm('确认导出财务合规数据？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'info'
      }).then(() => {
        this.exportLoading = true
        exportComplianceData(this.searchForm).then(response => {
          const blob = new Blob([response.data || response])
          const url = window.URL.createObjectURL(blob)
          const link = document.createElement('a')
          link.href = url
          link.setAttribute('download', '财务合规数据_' + new Date().getTime() + '.xlsx')
          document.body.appendChild(link)
          link.click()
          document.body.removeChild(link)
          window.URL.revokeObjectURL(url)
          this.$message.success('导出成功')
        }).catch(err => {
          this.$message.error('导出失败: ' + (err.message || '未知错误'))
        }).finally(() => {
          this.exportLoading = false
        })
      }).catch(() => {})
    },

    // 生成报告
    handleGenerateReport() {
      this.$confirm('确认生成合规报告？将根据当前筛选条件生成报告。', '生成合规报告', {
        confirmButtonText: '生成',
        cancelButtonText: '取消',
        type: 'info'
      }).then(() => {
        this.reportLoading = true
        const params = {}
        if (this.searchForm.checkDateRange && this.searchForm.checkDateRange.length === 2) {
          params.startDate = this.searchForm.checkDateRange[0]
          params.endDate = this.searchForm.checkDateRange[1]
        }
        if (this.searchForm.complianceStatus) {
          params.complianceStatus = this.searchForm.complianceStatus
        }
        generateComplianceReport(params).then(response => {
          const data = response && response.data ? response.data : response
          this.$alert(
            `<div style="line-height:2.2; font-size:14px;">
              <p><b>报告编号：</b>${data.reportId || '-'}</p>
              <p><b>生成时间：</b>${data.generateTime || '-'}</p>
              <p><b>检查总数：</b>${data.totalRecords || 0} 条</p>
              <p><b>合规数量：</b>${data.compliantCount || 0} 条</p>
              <p><b>违规数量：</b>${data.violationCount || 0} 条</p>
              <p><b>合规率：</b>${data.complianceRate || 0}%</p>
              <p><b>报告状态：</b><span style="color:#67C23A">已生成</span></p>
            </div>`,
            '合规报告生成成功',
            {
              dangerouslyUseHTMLString: true,
              confirmButtonText: '确定',
              type: 'success'
            }
          )
        }).catch(err => {
          this.$message.error('报告生成失败: ' + (err.message || '未知错误'))
        }).finally(() => {
          this.reportLoading = false
        })
      }).catch(() => {})
    },

    // 删除合规
    handleDelete(row) {
      this.$confirm('确认删除该财务合规监管记录？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteFinancialCompliance(row.id || row.complianceId).then(() => {
          this.$message.success('删除成功')
          this.getCompliancesList()
          this.getStatistics()
        }).catch(err => {
          this.$message.error('删除失败: ' + (err.message || '未知错误'))
        })
      }).catch(() => {})
    },

    // 批量分析
    handleBatchAnalyze() {
      if (this.selectedCompliances.length === 0) {
        this.$message.warning('请选择要分析的合规记录')
        return
      }
      this.$confirm('确认对选中的' + this.selectedCompliances.length + '条记录进行批量分析？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'info'
      }).then(() => {
        const ids = this.selectedCompliances.map(item => item.id || item.complianceId)
        batchComplianceAnalyze({ ids }).then(response => {
          if (response && response.data) {
            const data = response.data
            this.$alert(
              `<div style="line-height:2">
                <p><b>分析记录数：</b>${data.totalCount || ids.length}</p>
                <p><b>合规数量：</b>${data.compliantCount || 0}</p>
                <p><b>违规数量：</b>${data.violationCount || 0}</p>
                <p><b>平均评分：</b>${data.avgScore || '-'}</p>
                <p><b>合规率：</b>${data.complianceRate || 0}%</p>
              </div>`,
              '批量分析结果',
              { dangerouslyUseHTMLString: true, confirmButtonText: '确定' }
            )
          } else {
            this.$message.success('批量分析完成')
          }
        }).catch(err => {
          this.$message.error('批量分析失败: ' + (err.message || '未知错误'))
        })
      }).catch(() => {})
    },

    // 刷新图表
    refreshComplianceChart() {
      this.loadChartData()
    },

    refreshTrendChart() {
      this.loadChartData()
    },

    // 窗口大小变化
    handleResize() {
      if (this.complianceDistributionChart) this.complianceDistributionChart.resize()
      if (this.complianceTrendChart) this.complianceTrendChart.resize()
    },

    // 销毁图表
    destroyCharts() {
      if (this.complianceDistributionChart) {
        this.complianceDistributionChart.dispose()
        this.complianceDistributionChart = null
      }
      if (this.complianceTrendChart) {
        this.complianceTrendChart.dispose()
        this.complianceTrendChart = null
      }
      window.removeEventListener('resize', this.handleResize)
    },

    // 新增合规检查
    handleAdd() {
      this.currentCompliance = {}
      this.dialogType = 'add'
      this.complianceDialogVisible = true
    },

    // 编辑合规检查
    handleEdit(row) {
      this.currentCompliance = { ...row }
      this.dialogType = 'edit'
      this.complianceDialogVisible = true
    },

    // 查看合规检查
    handleView(row) {
      this.currentCompliance = { ...row }
      this.dialogType = 'view'
      this.complianceDialogVisible = true
    },

    // 合规检查详情
    handleCheck(row) {
      this.currentCompliance = { ...row }
      this.checkDialogVisible = true
    },

    // 违规管理
    handleViolation(row) {
      this.currentCompliance = { ...row }
      this.violationDialogVisible = true
    },

    // ==================== 辅助方法 ====================
    getCheckTypeText(type) {
      const map = { 'SYSTEM_EXECUTION': '制度执行', 'PROCESS_COMPLIANCE': '流程合规', 'VIOLATION_CHECK': '违规检查', 'RECTIFICATION_CHECK': '整改检查' }
      return map[type] || type || '-'
    },
    getComplianceStatusText(status) {
      const map = { 'COMPLIANT': '合规', 'BASICALLY_COMPLIANT': '基本合规', 'NON_COMPLIANT': '不合规', 'PENDING': '待检查', 'CHECKING': '检查中' }
      return map[status] || status || '-'
    },
    getComplianceStatusType(status) {
      const map = { 'COMPLIANT': 'success', 'BASICALLY_COMPLIANT': 'warning', 'NON_COMPLIANT': 'danger', 'PENDING': 'info', 'CHECKING': '' }
      return map[status] || 'info'
    },
    getRiskLevelText(level) {
      const map = { 'LOW': '低风险', 'MEDIUM': '中风险', 'HIGH': '高风险', 'CRITICAL': '极高风险' }
      return map[level] || level || '-'
    },
    getRiskLevelType(level) {
      const map = { 'LOW': 'success', 'MEDIUM': 'warning', 'HIGH': 'danger', 'CRITICAL': 'danger' }
      return map[level] || 'info'
    },
    getViolationTypeText(type) {
      const map = { 'FINANCIAL_FRAUD': '财务造假', 'DISCLOSURE_VIOLATION': '信息披露违规', 'INTERNAL_CONTROL_DEFECT': '内控缺陷', 'RELATED_PARTY_VIOLATION': '关联交易违规', 'FUND_OCCUPATION': '资金占用' }
      return map[type] || type || '-'
    },
    getScoreColor(score) {
      if (!score) return '#909399'
      if (score >= 90) return '#67C23A'
      if (score >= 80) return '#409EFF'
      if (score >= 70) return '#E6A23C'
      return '#F56C6C'
    },
    formatDate(dateStr) {
      if (!dateStr) return '-'
      if (dateStr.length > 10) return dateStr.substring(0, 10)
      return dateStr
    }
  }
}
</script>

<style lang="scss" scoped>
.financial-ext-page { padding: 16px; background: #f0f2f5; min-height: calc(100vh - 84px); }
.page-header { display: flex; align-items: center; justify-content: space-between; margin-bottom: 14px; padding: 14px 20px; border-radius: 6px; color: #fff; }
.page-header-left { display: flex; align-items: center; font-size: 16px; font-weight: 600; i { font-size: 22px; margin-right: 10px; } }
.page-header-desc { font-size: 13px; opacity: 0.85; }
::v-deep .el-table th { background: #fff0f6 !important; }
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
.chart-container { height: 300px; width: 100%; }
.table-count { color: #909399; font-size: 14px; }
.pagination-container { margin-top: 20px; text-align: right; }
</style>
