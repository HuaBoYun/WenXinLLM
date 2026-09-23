<template>
  <div class="leader-supervision" :style="themeVars">
    <!-- 统计概览 -->
    <el-row :gutter="16" class="overview-cards">
      <el-col :span="6">
        <el-card class="overview-card gradient-theme">
          <div class="card-content">
            <div class="icon-wrapper">
              <i class="el-icon-view"></i>
            </div>
            <div class="data-wrapper">
              <div class="number">{{ overviewData.totalSupervisions }}</div>
              <div class="label">监管总数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="overview-card gradient-theme">
          <div class="card-content">
            <div class="icon-wrapper">
              <i class="el-icon-warning"></i>
            </div>
            <div class="data-wrapper">
              <div class="number">{{ overviewData.riskCount }}</div>
              <div class="label">风险事项</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="overview-card gradient-theme">
          <div class="card-content">
            <div class="icon-wrapper">
              <i class="el-icon-success"></i>
            </div>
            <div class="data-wrapper">
              <div class="number">{{ overviewData.complianceRate }}%</div>
              <div class="label">合规率</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="overview-card gradient-theme">
          <div class="card-content">
            <div class="icon-wrapper">
              <i class="el-icon-circle-check"></i>
            </div>
            <div class="data-wrapper">
              <div class="number">{{ overviewData.qualifiedCount }}</div>
              <div class="label">资格合规</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 功能模块 -->
    <el-card class="function-card">
      <div slot="header" class="card-header">
        <span class="card-title">负责人监管</span>
        <div class="card-actions">
          <el-button type="primary" @click="startSupervision">发起监管</el-button>
          <el-button type="success" @click="refreshAll">刷新数据</el-button>
        </div>
      </div>
      <el-tabs v-model="activeTab" type="border-card" @tab-click="handleTabClick">
        <el-tab-pane label="任职资格" name="qualification">
          <SupervisionTabList
            ref="tabQualification"
            supervision-type="任职资格"
            tab-label="任职资格监管"
            @view="viewDetail"
            @report="generateReport"
          />
        </el-tab-pane>
        <el-tab-pane label="履职情况" name="performance">
          <SupervisionTabList
            ref="tabPerformance"
            supervision-type="履职情况"
            tab-label="履职情况监管"
            @view="viewDetail"
            @report="generateReport"
          />
        </el-tab-pane>
        <el-tab-pane label="合规性检查" name="compliance">
          <SupervisionTabList
            ref="tabCompliance"
            supervision-type="合规性检查"
            tab-label="合规性检查"
            @view="viewDetail"
            @report="generateReport"
          />
        </el-tab-pane>
        <el-tab-pane label="风险预警" name="risk">
          <SupervisionTabList
            ref="tabRisk"
            supervision-type="风险预警"
            tab-label="风险预警"
            @view="viewDetail"
            @report="generateReport"
          />
        </el-tab-pane>
        <el-tab-pane label="整改跟踪" name="rectification">
          <SupervisionTabList
            ref="tabRectification"
            supervision-type=""
            status-filter="待整改"
            tab-label="整改跟踪"
            @view="viewDetail"
            @report="generateReport"
          />
        </el-tab-pane>
      </el-tabs>
    </el-card>

    <!-- 图表分析 -->
    <el-row :gutter="16" class="chart-section">
      <el-col :span="12">
        <el-card class="chart-card">
          <div slot="header">监管问题分类</div>
          <div id="issueChart" style="height: 300px;"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card class="chart-card">
          <div slot="header">合规趋势分析</div>
          <div id="trendChart" style="height: 300px;"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 数据表格 -->
    <el-card class="data-card">
      <div slot="header" class="card-header">
        <span class="card-title">监管记录</span>
        <div class="search-wrapper">
          <el-input
            v-model="searchText"
            placeholder="搜索负责人或企业"
            prefix-icon="el-icon-search"
            style="width: 300px;"
            @input="handleSearch"
          />
        </div>
      </div>
      <el-table v-loading="loading" :data="tableData" stripe border style="width: 100%">
        <el-table-column prop="leaderName" label="负责人" width="120" />
        <el-table-column prop="company" label="所属企业" width="180" />
        <el-table-column prop="supervisionType" label="监管类型" width="120" />
        <el-table-column prop="supervisionDate" label="监管时间" width="120" />
        <el-table-column prop="riskLevel" label="风险等级" width="100">
          <template slot-scope="scope">
            <el-tag :type="getRiskType(scope.row.riskLevel)">
              {{ scope.row.riskLevel }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="complianceStatus" label="合规状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="getComplianceType(scope.row.complianceStatus)">
              {{ scope.row.complianceStatus }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="issueCount" label="问题数量" width="100" />
        <el-table-column prop="supervisor" label="监管人" width="120" />
        <el-table-column prop="status" label="状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="getStatusType(scope.row.status)">
              {{ scope.row.status }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="250" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" @click="viewDetail(scope.row)">查看</el-button>
            <el-button size="mini" type="primary" @click="editSupervision(scope.row)">编辑</el-button>
            <el-button size="mini" type="warning" @click="generateReport(scope.row)">报告</el-button>
            <el-button size="mini" type="danger" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <!-- 分页 -->
      <div class="pagination-wrapper">
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="queryForm.pageNumber"
          :page-sizes="[10, 15, 20, 50]"
          :page-size="queryForm.pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
        />
      </div>
    </el-card>

    <!-- 发起/编辑/查看监管对话框 -->
    <SupervisionDialog
      :visible.sync="dialogVisible"
      :dialog-type="dialogType"
      :supervision-data="currentSupervision"
      @refresh="handleDialogRefresh"
    />

    <!-- 监管报告对话框 -->
    <SupervisionReportDialog
      :visible.sync="reportDialogVisible"
      :data="currentSupervision"
    />
  </div>
</template>

<script>
import {
  getLeaderSupervisionList,
  deleteLeaderSupervision,
  getLeaderSupervisionStatistics
} from '@/api/leader/index'
import * as echarts from 'echarts'
import SupervisionDialog from './components/SupervisionDialog'
import SupervisionTabList from './components/SupervisionTabList'
import SupervisionReportDialog from './components/SupervisionReportDialog'
import { investThemeMixin } from '../../stateAssets/themeMixin'

export default {
  name: 'LeaderSupervision',
  components: { SupervisionDialog, SupervisionTabList, SupervisionReportDialog },
  mixins: [investThemeMixin],
  data() {
    return {
      activeTab: 'qualification',
      searchText: '',
      loading: false,
      total: 0,
      queryForm: {
        pageNumber: 1,
        pageSize: 15
      },
      overviewData: {
        totalSupervisions: 0,
        riskCount: 0,
        complianceRate: 0,
        qualifiedCount: 0
      },
      tableData: [],
      // 对话框相关
      dialogVisible: false,
      dialogType: 'add',
      currentSupervision: {},
      // 报告对话框
      reportDialogVisible: false
    }
  },
  mounted() {
    this.loadData()
    this.loadStatistics()
    this.initCharts()
  },
  watch: {
    // 主题切换时重绘图表
    ipBright() {
      this.$nextTick(() => {
        this.initIssueChart()
        this.initTrendChart()
      })
    }
  },
  methods: {
    async loadData() {
      this.loading = true
      try {
        const params = { ...this.queryForm, searchText: this.searchText }
        const res = await getLeaderSupervisionList(params)
        if (res && res.data) {
          const pageData = res.data
          this.tableData = pageData.tlist || []
          this.total = pageData.totalRecord || 0
        }
      } catch (e) {
        console.error('加载监管数据失败', e)
      } finally {
        this.loading = false
      }
    },
    async loadStatistics() {
      try {
        const res = await getLeaderSupervisionStatistics()
        if (res && res.data) {
          this.overviewData = res.data
        }
      } catch (e) {
        console.error('加载统计数据失败', e)
      }
    },
    initCharts() {
      this.$nextTick(() => {
        this.initIssueChart()
        this.initTrendChart()
      })
    },
    initIssueChart() {
      const chartDom = document.getElementById('issueChart')
      if (!chartDom) return
      const existing = echarts.getInstanceByDom(chartDom)
      if (existing) existing.dispose()
      const chart = echarts.init(chartDom)
      chart.setOption({
        tooltip: { trigger: 'item', formatter: '{b}: {c} ({d}%)' },
        legend: { bottom: 10, left: 'center' },
        series: [{
          type: 'pie',
          radius: ['35%', '60%'],
          center: ['50%', '45%'],
          avoidLabelOverlap: true,
          label: { formatter: '{b}\n{c}件' },
          data: [
            { value: 12, name: '履职问题', itemStyle: { color: '#F5222D' } },
            { value: 8, name: '合规问题', itemStyle: { color: '#FA8C16' } },
            { value: 15, name: '财务问题', itemStyle: { color: '#FAAD14' } },
            { value: 6, name: '决策问题', itemStyle: { color: this.ipBright } },
            { value: 4, name: '其他问题', itemStyle: { color: '#52C41A' } }
          ]
        }]
      })
      window.addEventListener('resize', () => chart.resize())
    },
    initTrendChart() {
      const chartDom = document.getElementById('trendChart')
      if (!chartDom) return
      const existing = echarts.getInstanceByDom(chartDom)
      if (existing) existing.dispose()
      const chart = echarts.init(chartDom)
      chart.setOption({
        tooltip: { trigger: 'axis' },
        legend: { data: ['合规率', '问题数'], top: 5 },
        grid: { left: 50, right: 30, bottom: 30, top: 45 },
        xAxis: { type: 'category', data: ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月'] },
        yAxis: [
          { type: 'value', name: '合规率(%)', min: 80, max: 100 },
          { type: 'value', name: '问题数', min: 0 }
        ],
        series: [
          {
            name: '合规率',
            type: 'line',
            smooth: true,
            data: [92, 93, 91, 94, 95, 93, 96, 95, 97, 96, 98, 97],
            itemStyle: { color: '#52C41A' },
            lineStyle: { width: 3 },
            areaStyle: { color: 'rgba(82, 196, 26, 0.1)' }
          },
          {
            name: '问题数',
            type: 'bar',
            yAxisIndex: 1,
            data: [5, 4, 6, 3, 3, 4, 2, 3, 2, 2, 1, 2],
            itemStyle: { color: this.ipBright, borderRadius: [4, 4, 0, 0] }
          }
        ]
      })
      window.addEventListener('resize', () => chart.resize())
    },
    startSupervision() {
      this.currentSupervision = {}
      this.dialogType = 'add'
      this.dialogVisible = true
    },
    viewDetail(row) {
      this.currentSupervision = { ...row }
      this.dialogType = 'view'
      this.dialogVisible = true
    },
    editSupervision(row) {
      this.currentSupervision = { ...row }
      this.dialogType = 'edit'
      this.dialogVisible = true
    },
    handleDelete(row) {
      this.$confirm('确认删除该监管记录？删除后不可恢复。', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          await deleteLeaderSupervision(row.id)
          this.$message.success('删除成功')
          this.loadData()
          this.loadStatistics()
          this.refreshCurrentTab()
        } catch (e) {
          this.$message.error('删除失败')
        }
      }).catch(() => {})
    },
    // 对话框提交后刷新
    handleDialogRefresh() {
      this.loadData()
      this.loadStatistics()
      this.refreshCurrentTab()
    },
    // 刷新当前激活的Tab列表
    refreshCurrentTab() {
      const refMap = {
        qualification: 'tabQualification',
        performance: 'tabPerformance',
        compliance: 'tabCompliance',
        risk: 'tabRisk',
        rectification: 'tabRectification'
      }
      const ref = this.$refs[refMap[this.activeTab]]
      if (ref && ref.loadData) ref.loadData()
    },
    // 刷新所有数据
    refreshAll() {
      this.loadData()
      this.loadStatistics()
      this.refreshCurrentTab()
    },
    handleTabClick() {
      // Tab切换时不需要额外操作，组件mounted时已加载
    },
    generateReport(row) {
      this.currentSupervision = { ...row }
      this.reportDialogVisible = true
    },
    handleSearch() {
      this.queryForm.pageNumber = 1
      this.loadData()
    },
    handleSizeChange(val) {
      this.queryForm.pageSize = val
      this.queryForm.pageNumber = 1
      this.loadData()
    },
    handleCurrentChange(val) {
      this.queryForm.pageNumber = val
      this.loadData()
    },
    getRiskType(level) {
      const typeMap = {
        '低风险': 'success',
        '中风险': 'warning',
        '高风险': 'danger',
        '极高风险': 'danger'
      }
      return typeMap[level] || 'info'
    },
    getComplianceType(status) {
      const typeMap = {
        '合规': 'success',
        '基本合规': 'warning',
        '不合规': 'danger',
        '待整改': 'info'
      }
      return typeMap[status] || 'info'
    },
    getStatusType(status) {
      const typeMap = {
        '进行中': 'warning',
        '已完成': 'success',
        '待整改': 'info',
        '已关闭': 'info'
      }
      return typeMap[status] || 'info'
    }
  }
}
</script>

<style lang="scss" scoped>
.leader-supervision {
  padding: 20px;

  .overview-cards {
    margin-bottom: 20px;
    
    .overview-card {
      height: 120px;
      
      &.gradient-theme {
        background: linear-gradient(135deg, var(--ip-secondary) 0%, var(--ip-bright) 100%);
        color: white;

        .card-content {
          display: flex;
          align-items: center;
          height: 100%;
          
          .icon-wrapper {
            font-size: 40px;
            margin-right: 15px;
            opacity: 0.8;
          }
          
          .data-wrapper {
            .number {
              font-size: 28px;
              font-weight: bold;
              line-height: 1;
            }
            
            .label {
              font-size: 14px;
              margin-top: 5px;
              opacity: 0.9;
            }
          }
        }
      }
    }
  }

  .function-card, .chart-section, .data-card {
    margin-bottom: 20px;
  }

  .chart-section {
    .chart-card {
      height: 380px;
    }
  }

  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    
    .card-title {
      font-size: 16px;
      font-weight: bold;
    }
  }

  .search-wrapper {
    display: flex;
    align-items: center;
  }

  .pagination-wrapper {
    margin-top: 15px;
    text-align: right;
  }

  .tab-content {
    padding: 20px;
    min-height: 200px;
    color: #666;
    line-height: 1.6;
  }

  ::v-deep .el-tabs__content {
    padding: 0;
  }
}
</style>
