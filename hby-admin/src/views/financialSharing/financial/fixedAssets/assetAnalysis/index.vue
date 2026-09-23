<template>
  <div class="asset-analysis-container">
    <div class="page-header">
      <h2>资产分析</h2>
      <p>固定资产统计分析、趋势分析、价值分析等综合报表</p>
    </div>
    
    <!-- 分析概览卡片 -->
    <div class="overview-cards">
      <el-row :gutter="20">
        <el-col :span="6">
          <div class="overview-card">
            <div class="card-icon total">
              <i class="el-icon-pie-chart"></i>
            </div>
            <div class="card-content">
              <div class="card-value">{{ formatAmount(overview.totalValue) }}</div>
              <div class="card-label">资产总值</div>
              <div class="card-trend">
                <span class="trend-icon up">↗</span>
                <span class="trend-text">较上月 +5.2%</span>
              </div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="overview-card">
            <div class="card-icon depreciation">
              <i class="el-icon-bottom"></i>
            </div>
            <div class="card-content">
              <div class="card-value">{{ overview.depreciationRate }}%</div>
              <div class="card-label">平均折旧率</div>
              <div class="card-trend">
                <span class="trend-icon up">↗</span>
                <span class="trend-text">较上月 +1.8%</span>
              </div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="overview-card">
            <div class="card-icon utilization">
              <i class="el-icon-data-analysis"></i>
            </div>
            <div class="card-content">
              <div class="card-value">{{ overview.utilizationRate }}%</div>
              <div class="card-label">资产利用率</div>
              <div class="card-trend">
                <span class="trend-icon down">↘</span>
                <span class="trend-text">较上月 -2.1%</span>
              </div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="overview-card">
            <div class="card-icon roi">
              <i class="el-icon-trophy"></i>
            </div>
            <div class="card-content">
              <div class="card-value">{{ overview.roi }}%</div>
              <div class="card-label">资产回报率</div>
              <div class="card-trend">
                <span class="trend-icon up">↗</span>
                <span class="trend-text">较上月 +0.5%</span>
              </div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 分析内容区域 -->
    <div class="analysis-content">
      <el-row :gutter="20">
        <!-- 左侧图表区域 -->
        <el-col :span="16">
          <div class="charts-section">
            <el-tabs v-model="activeChartTab" @tab-click="handleChartTabClick">
              <el-tab-pane label="资产结构分析" name="structure">
                <div class="chart-panel">
                  <div class="chart-header">
                    <h4>资产类别分布</h4>
                    <div class="chart-controls">
                      <el-radio-group v-model="structureViewType" size="small">
                        <el-radio-button label="value">按价值</el-radio-button>
                        <el-radio-button label="quantity">按数量</el-radio-button>
                      </el-radio-group>
                    </div>
                  </div>
                  <div id="structureChart" style="width: 100%; height: 350px;"></div>
                </div>
              </el-tab-pane>
              
              <el-tab-pane label="折旧趋势分析" name="depreciation">
                <div class="chart-panel">
                  <div class="chart-header">
                    <h4>月度折旧趋势</h4>
                    <div class="chart-controls">
                      <el-date-picker
                        v-model="depreciationDateRange"
                        type="monthrange"
                        range-separator="至"
                        start-placeholder="开始月份"
                        end-placeholder="结束月份"
                        format="yyyy-MM"
                        value-format="yyyy-MM"
                        size="small"
                        style="width: 240px"
                      />
                    </div>
                  </div>
                  <div id="depreciationTrendChart" style="width: 100%; height: 350px;"></div>
                </div>
              </el-tab-pane>
              
              <el-tab-pane label="价值变动分析" name="value">
                <div class="chart-panel">
                  <div class="chart-header">
                    <h4>资产价值变动</h4>
                    <div class="chart-controls">
                      <el-select v-model="valueAnalysisType" size="small" style="width: 120px">
                        <el-option label="原值" value="original" />
                        <el-option label="净值" value="net" />
                        <el-option label="累计折旧" value="depreciation" />
                      </el-select>
                    </div>
                  </div>
                  <div id="valueChangeChart" style="width: 100%; height: 350px;"></div>
                </div>
              </el-tab-pane>
              
              <el-tab-pane label="利用率分析" name="utilization">
                <div class="chart-panel">
                  <div class="chart-header">
                    <h4>部门资产利用率</h4>
                    <div class="chart-controls">
                      <el-button size="small" @click="refreshUtilizationData">刷新数据</el-button>
                    </div>
                  </div>
                  <div id="utilizationChart" style="width: 100%; height: 350px;"></div>
                </div>
              </el-tab-pane>
            </el-tabs>
          </div>
        </el-col>
        
        <!-- 右侧统计区域 -->
        <el-col :span="8">
          <div class="statistics-section">
            <!-- 资产状态统计 -->
            <div class="stat-panel">
              <div class="panel-header">
                <h4>资产状态分布</h4>
              </div>
              <div class="status-stats">
                <div class="status-item" v-for="item in statusStats" :key="item.status">
                  <div class="status-info">
                    <div class="status-label">
                      <el-tag :type="getStatusTagType(item.status)" size="small">
                        {{ item.statusName }}
                      </el-tag>
                    </div>
                    <div class="status-value">{{ item.count }}</div>
                  </div>
                  <div class="status-progress">
                    <el-progress
                      :percentage="item.percentage"
                      :stroke-width="8"
                      :show-text="false"
                      :color="getStatusColor(item.status)"
                    />
                  </div>
                </div>
              </div>
            </div>

            <!-- 部门资产排行 -->
            <div class="stat-panel">
              <div class="panel-header">
                <h4>部门资产排行</h4>
              </div>
              <div class="department-ranking">
                <div class="ranking-item" v-for="(item, index) in departmentRanking" :key="item.department">
                  <div class="ranking-number">{{ index + 1 }}</div>
                  <div class="ranking-info">
                    <div class="department-name">{{ item.department }}</div>
                    <div class="department-value">{{ formatAmount(item.value) }}</div>
                  </div>
                  <div class="ranking-progress">
                    <el-progress
                      :percentage="item.percentage"
                      :stroke-width="6"
                      :show-text="false"
                      color="#409eff"
                    />
                  </div>
                </div>
              </div>
            </div>

            <!-- 预警信息 -->
            <div class="stat-panel">
              <div class="panel-header">
                <h4>资产预警</h4>
              </div>
              <div class="warning-list">
                <div class="warning-item" v-for="item in warningList" :key="item.id">
                  <div class="warning-icon">
                    <i :class="getWarningIcon(item.type)" :style="{ color: getWarningColor(item.type) }"></i>
                  </div>
                  <div class="warning-content">
                    <div class="warning-title">{{ item.title }}</div>
                    <div class="warning-desc">{{ item.description }}</div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 详细报表区域 -->
    <div class="reports-section">
      <div class="section-header">
        <h3>详细报表</h3>
        <div class="section-actions">
          <el-button type="primary" @click="generateReport">生成报表</el-button>
          <el-button type="success" @click="exportReport">导出报表</el-button>
          <el-button type="info" @click="scheduleReport">定时报表</el-button>
        </div>
      </div>
      
      <div class="reports-grid">
        <el-row :gutter="20">
          <el-col :span="8" v-for="report in reportList" :key="report.id">
            <div class="report-card" @click="handleReportClick(report)">
              <div class="report-icon">
                <i :class="report.icon"></i>
              </div>
              <div class="report-info">
                <div class="report-title">{{ report.title }}</div>
                <div class="report-desc">{{ report.description }}</div>
                <div class="report-meta">
                  <span class="report-date">{{ report.lastUpdate }}</span>
                  <span class="report-status" :class="report.status">{{ getReportStatusText(report.status) }}</span>
                </div>
              </div>
            </div>
          </el-col>
        </el-row>
      </div>
    </div>

    <!-- 报表生成对话框 -->
    <report-generate-dialog
      :visible.sync="reportDialogVisible"
      @generate="handleReportGenerate"
    />

    <!-- 定时报表配置对话框 -->
    <schedule-report-dialog
      :visible.sync="scheduleDialogVisible"
      @submit="handleScheduleSubmit"
    />

    <!-- 报表查看对话框 -->
    <report-view-dialog
      :visible.sync="reportViewDialogVisible"
      :report-data="currentReport"
    />
  </div>
</template>

<script>
import chartsMixin from './chartsMixin'
import ReportGenerateDialog from './components/ReportGenerateDialog.vue'
import ScheduleReportDialog from './components/ScheduleReportDialog.vue'
import ReportViewDialog from './components/ReportViewDialog.vue'

export default {
  name: 'AssetAnalysis',
  components: {
    ReportGenerateDialog,
    ScheduleReportDialog,
    ReportViewDialog
  },
  mixins: [chartsMixin],
  data() {
    return {
      activeChartTab: 'structure',
      structureViewType: 'value',
      depreciationDateRange: [],
      valueAnalysisType: 'original',
      reportDialogVisible: false,
      scheduleDialogVisible: false,
      reportViewDialogVisible: false,
      currentReport: {},
      overview: {
        totalValue: 0,
        depreciationRate: 0,
        utilizationRate: 0,
        roi: 0
      },
      statusStats: [],
      departmentRanking: [],
      warningList: [],
      reportList: []
    }
  },
  mounted() {
    this.loadOverviewData()
    this.loadStatusStats()
    this.loadDepartmentRanking()
    this.loadWarningList()
    this.loadReportList()
    this.initCharts()
  },
  methods: {
    async loadOverviewData() {
      try {
        this.overview = {
          totalValue: 125680000,
          depreciationRate: 36.5,
          utilizationRate: 78.2,
          roi: 12.8
        }
      } catch (error) {
        console.error('加载概览数据失败：', error)
      }
    },

    async loadStatusStats() {
      try {
        this.statusStats = [
          { status: 'NORMAL', statusName: '正常', count: 856, percentage: 68 },
          { status: 'IDLE', statusName: '闲置', count: 245, percentage: 20 },
          { status: 'MAINTENANCE', statusName: '维修', count: 98, percentage: 8 },
          { status: 'SCRAPPED', statusName: '报废', count: 57, percentage: 4 }
        ]
      } catch (error) {
        console.error('加载状态统计失败：', error)
      }
    },

    async loadDepartmentRanking() {
      try {
        this.departmentRanking = [
          { department: '生产部', value: 45680000, percentage: 100 },
          { department: '研发部', value: 32450000, percentage: 71 },
          { department: '销售部', value: 18920000, percentage: 41 },
          { department: '行政部', value: 15630000, percentage: 34 },
          { department: '财务部', value: 12000000, percentage: 26 }
        ]
      } catch (error) {
        console.error('加载部门排行失败：', error)
      }
    },

    async loadWarningList() {
      try {
        this.warningList = [
          {
            id: 1,
            type: 'depreciation',
            title: '折旧异常',
            description: '5项资产折旧计算异常'
          },
          {
            id: 2,
            type: 'idle',
            title: '闲置资产',
            description: '12项资产长期闲置'
          },
          {
            id: 3,
            type: 'maintenance',
            title: '维修超期',
            description: '3项资产维修超期'
          },
          {
            id: 4,
            type: 'value',
            title: '价值异常',
            description: '2项资产价值波动异常'
          }
        ]
      } catch (error) {
        console.error('加载预警信息失败：', error)
      }
    },

    async loadReportList() {
      try {
        this.reportList = [
          {
            id: 1,
            title: '资产清单报表',
            description: '详细的固定资产清单',
            icon: 'el-icon-document',
            lastUpdate: '2024-12-19',
            status: 'ready'
          },
          {
            id: 2,
            title: '折旧明细报表',
            description: '资产折旧计算明细',
            icon: 'el-icon-data-line',
            lastUpdate: '2024-12-19',
            status: 'ready'
          },
          {
            id: 3,
            title: '资产变动报表',
            description: '资产变动情况统计',
            icon: 'el-icon-sort',
            lastUpdate: '2024-12-18',
            status: 'generating'
          },
          {
            id: 4,
            title: '资产利用率报表',
            description: '各部门资产利用情况',
            icon: 'el-icon-pie-chart',
            lastUpdate: '2024-12-17',
            status: 'ready'
          },
          {
            id: 5,
            title: '资产价值分析报表',
            description: '资产价值变动分析',
            icon: 'el-icon-data-analysis',
            lastUpdate: '2024-12-16',
            status: 'ready'
          },
          {
            id: 6,
            title: '资产处置报表',
            description: '资产处置情况统计',
            icon: 'el-icon-delete',
            lastUpdate: '2024-12-15',
            status: 'ready'
          }
        ]
      } catch (error) {
        console.error('加载报表列表失败：', error)
      }
    },

    initCharts() {
      // 初始化所有图表
      this.$nextTick(() => {
        this.initStructureChart()
        this.initDepreciationChart()
        this.initValueChart()
        this.initUtilizationChart()
      })
    },

    handleChartTabClick(tab) {
      console.log('切换图表标签页：', tab.name)
      // 根据不同的标签页重新渲染对应的图表
      this.$nextTick(() => {
        switch (tab.name) {
          case 'structure':
            this.initStructureChart()
            break
          case 'depreciation':
            this.initDepreciationChart()
            break
          case 'value':
            this.initValueChart()
            break
          case 'utilization':
            this.initUtilizationChart()
            break
        }
      })
    },

    refreshUtilizationData() {
      this.$message.success('利用率数据已刷新')
    },

    generateReport() {
      this.reportDialogVisible = true
    },

    async handleReportGenerate(formData) {
      try {
        // TODO: 调用API生成报表
        await new Promise(resolve => setTimeout(resolve, 2000))
        this.$message.success('报表生成成功')
        this.reportDialogVisible = false
        this.loadReportList()
      } catch (error) {
        this.$message.error('报表生成失败：' + error.message)
      }
    },

    exportReport() {
      try {
        const data = this.reportList || []
        if (data.length === 0) {
          this.$message.warning('暂无报表数据可导出')
          return
        }
        const jsonStr = JSON.stringify(data, null, 2)
        const blob = new Blob([jsonStr], { type: 'application/json' })
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = '资产分析报表导出.json'
        link.click()
        window.URL.revokeObjectURL(url)
        this.$message.success('导出成功')
      } catch (error) {
        this.$message.error('导出失败')
      }
    },

    scheduleReport() {
      this.scheduleDialogVisible = true
    },

    async handleScheduleSubmit(formData) {
      try {
        // TODO: 调用API保存定时报表配置
        await new Promise(resolve => setTimeout(resolve, 1000))
        this.$message.success('定时报表配置成功')
        this.scheduleDialogVisible = false
      } catch (error) {
        this.$message.error('配置失败：' + error.message)
      }
    },

    handleReportClick(report) {
      this.currentReport = report
      this.reportViewDialogVisible = true
    },

    formatAmount(amount) {
      return amount ? '¥' + Number(amount).toLocaleString('zh-CN', { minimumFractionDigits: 0 }) : '¥0'
    },

    getStatusTagType(status) {
      const typeMap = {
        'NORMAL': 'success',
        'IDLE': 'warning',
        'MAINTENANCE': 'info',
        'SCRAPPED': 'danger'
      }
      return typeMap[status] || 'default'
    },

    getStatusColor(status) {
      const colorMap = {
        'NORMAL': '#67c23a',
        'IDLE': '#e6a23c',
        'MAINTENANCE': '#409eff',
        'SCRAPPED': '#f56c6c'
      }
      return colorMap[status] || '#909399'
    },

    getWarningIcon(type) {
      const iconMap = {
        'depreciation': 'el-icon-warning',
        'idle': 'el-icon-time',
        'maintenance': 'el-icon-setting',
        'value': 'el-icon-data-line'
      }
      return iconMap[type] || 'el-icon-info'
    },

    getWarningColor(type) {
      const colorMap = {
        'depreciation': '#f56c6c',
        'idle': '#e6a23c',
        'maintenance': '#409eff',
        'value': '#f56c6c'
      }
      return colorMap[type] || '#909399'
    },

    getReportStatusText(status) {
      const textMap = {
        'ready': '就绪',
        'generating': '生成中',
        'error': '错误'
      }
      return textMap[status] || status
    }
  }
}
</script>

<style lang="scss" scoped>
.asset-analysis-container {
  padding: 20px;
}

.page-header {
  margin-bottom: 20px;
  
  h2 {
    margin: 0 0 8px 0;
    color: #303133;
  }
  
  p {
    margin: 0;
    color: #606266;
    font-size: 14px;
  }
}

.overview-cards {
  margin-bottom: 20px;

  .overview-card {
    background: white;
    border-radius: 8px;
    padding: 20px;
    display: flex;
    align-items: center;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);

    .card-icon {
      width: 60px;
      height: 60px;
      border-radius: 8px;
      display: flex;
      align-items: center;
      justify-content: center;
      margin-right: 15px;

      i {
        font-size: 28px;
        color: white;
      }

      &.total {
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      }

      &.depreciation {
        background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
      }

      &.utilization {
        background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
      }

      &.roi {
        background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
      }
    }

    .card-content {
      flex: 1;

      .card-value {
        font-size: 28px;
        font-weight: 600;
        color: #303133;
        margin-bottom: 4px;
      }

      .card-label {
        font-size: 14px;
        color: #909399;
        margin-bottom: 8px;
      }

      .card-trend {
        display: flex;
        align-items: center;
        font-size: 12px;

        .trend-icon {
          margin-right: 4px;
          font-weight: bold;

          &.up {
            color: #67c23a;
          }

          &.down {
            color: #f56c6c;
          }
        }

        .trend-text {
          color: #606266;
        }
      }
    }
  }
}

.analysis-content {
  margin-bottom: 20px;

  .charts-section {
    background: white;
    border-radius: 4px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
    padding: 20px;

    .chart-panel {
      .chart-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 20px;

        h4 {
          margin: 0;
          color: #303133;
        }
      }
    }
  }

  .statistics-section {
    .stat-panel {
      background: white;
      border-radius: 4px;
      box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
      padding: 20px;
      margin-bottom: 20px;

      .panel-header {
        margin-bottom: 15px;

        h4 {
          margin: 0;
          color: #303133;
        }
      }

      .status-stats {
        .status-item {
          display: flex;
          align-items: center;
          margin-bottom: 15px;

          .status-info {
            width: 120px;
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-right: 15px;

            .status-value {
              font-weight: 600;
              color: #303133;
            }
          }

          .status-progress {
            flex: 1;
          }
        }
      }

      .department-ranking {
        .ranking-item {
          display: flex;
          align-items: center;
          margin-bottom: 15px;

          .ranking-number {
            width: 24px;
            height: 24px;
            border-radius: 50%;
            background: #409eff;
            color: white;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 12px;
            font-weight: 600;
            margin-right: 10px;
          }

          .ranking-info {
            width: 120px;
            margin-right: 15px;

            .department-name {
              font-size: 14px;
              color: #303133;
              margin-bottom: 2px;
            }

            .department-value {
              font-size: 12px;
              color: #606266;
            }
          }

          .ranking-progress {
            flex: 1;
          }
        }
      }

      .warning-list {
        .warning-item {
          display: flex;
          align-items: flex-start;
          margin-bottom: 15px;

          .warning-icon {
            width: 20px;
            margin-right: 10px;
            margin-top: 2px;

            i {
              font-size: 16px;
            }
          }

          .warning-content {
            flex: 1;

            .warning-title {
              font-size: 14px;
              color: #303133;
              margin-bottom: 2px;
            }

            .warning-desc {
              font-size: 12px;
              color: #606266;
            }
          }
        }
      }
    }
  }
}

.reports-section {
  background: white;
  border-radius: 4px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  padding: 20px;

  .section-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;

    h3 {
      margin: 0;
      color: #303133;
    }
  }

  .reports-grid {
    .report-card {
      border: 1px solid #ebeef5;
      border-radius: 4px;
      padding: 20px;
      cursor: pointer;
      transition: all 0.3s;
      margin-bottom: 20px;

      &:hover {
        border-color: #409eff;
        box-shadow: 0 2px 8px rgba(64, 158, 255, 0.2);
      }

      .report-icon {
        text-align: center;
        margin-bottom: 15px;

        i {
          font-size: 32px;
          color: #409eff;
        }
      }

      .report-info {
        text-align: center;

        .report-title {
          font-size: 16px;
          font-weight: 600;
          color: #303133;
          margin-bottom: 8px;
        }

        .report-desc {
          font-size: 14px;
          color: #606266;
          margin-bottom: 12px;
        }

        .report-meta {
          display: flex;
          justify-content: space-between;
          align-items: center;
          font-size: 12px;

          .report-date {
            color: #909399;
          }

          .report-status {
            padding: 2px 6px;
            border-radius: 2px;
            
            &.ready {
              background: #f0f9ff;
              color: #409eff;
            }

            &.generating {
              background: #fdf6ec;
              color: #e6a23c;
            }

            &.error {
              background: #fef0f0;
              color: #f56c6c;
            }
          }
        }
      }
    }
  }
}
</style>
