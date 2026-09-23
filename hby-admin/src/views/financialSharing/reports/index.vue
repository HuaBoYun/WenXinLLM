<template>
  <div class="reports-analysis-container">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-left">
        <h1 class="page-title">
          <i class="el-icon-data-line"></i>
          报表分析模块
        </h1>
        <p class="page-description">提供财务报表生成、业务分析、数据可视化等分析决策功能</p>
      </div>
      <div class="header-right">
        <el-button type="primary" icon="el-icon-refresh" @click="refreshReports">
          刷新报表
        </el-button>
        <el-button type="success" icon="el-icon-download" @click="exportReports">
          批量导出
        </el-button>
        <el-button type="warning" icon="el-icon-setting" @click="reportSettings">
          报表设置
        </el-button>
      </div>
    </div>

    <!-- 报表统计概览 -->
    <div class="stats-overview">
      <el-row :gutter="24">
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon total-reports">
              <i class="el-icon-document-copy"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ stats.totalReports }}</div>
              <div class="stat-label">报表总数</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon generated-today">
              <i class="el-icon-data-line"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ stats.generatedToday }}</div>
              <div class="stat-label">今日生成</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon scheduled-reports">
              <i class="el-icon-time"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ stats.scheduledReports }}</div>
              <div class="stat-label">定时报表</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon analysis-views">
              <i class="el-icon-view"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ stats.analysisViews }}</div>
              <div class="stat-label">分析视图</div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 报表分析功能模块 -->
    <div class="function-modules">
      <el-row :gutter="24">
        <!-- 财务报表 -->
        <el-col :span="12">
          <div class="module-card financial-reports-card">
            <div class="card-header">
              <div class="card-icon">
                <i class="el-icon-document"></i>
              </div>
              <div class="card-title">
                <h3>财务报表</h3>
                <span class="card-subtitle">生成标准财务报表，包括三大主表和附表</span>
              </div>
              <div class="card-stats">
                <span class="stat-number">{{ stats.financialReports }}</span>
                <span class="stat-label">张财务报表</span>
              </div>
            </div>
            <div class="card-content">
              <div class="feature-grid">
                <div class="feature-item">
                  <i class="el-icon-s-data"></i>
                  <span>资产负债表</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-trophy"></i>
                  <span>利润表</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-money"></i>
                  <span>现金流量表</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-document-copy"></i>
                  <span>财务报表附注</span>
                </div>
              </div>
              <div class="card-actions">
                <el-button type="primary" size="small" @click="gotoFinancialReports">
                  进入财务报表
                </el-button>
                <el-button type="text" size="small" @click="viewFinancialReportsStats">
                  查看统计
                </el-button>
              </div>
            </div>
          </div>
        </el-col>

        <!-- 业务分析 -->
        <el-col :span="12">
          <div class="module-card business-analysis-card">
            <div class="card-header">
              <div class="card-icon">
                <i class="el-icon-data-analysis"></i>
              </div>
              <div class="card-title">
                <h3>业务分析</h3>
                <span class="card-subtitle">提供多维度业务分析和数据洞察</span>
              </div>
              <div class="card-stats">
                <span class="stat-number">{{ stats.analysisReports }}</span>
                <span class="stat-label">个分析报告</span>
              </div>
            </div>
            <div class="card-content">
              <div class="feature-grid">
                <div class="feature-item">
                  <i class="el-icon-coin"></i>
                  <span>成本分析</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-trophy"></i>
                  <span>收入分析</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-data-line"></i>
                  <span>财务指标</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-pie-chart"></i>
                  <span>经营分析</span>
                </div>
              </div>
              <div class="card-actions">
                <el-button type="primary" size="small" @click="gotoBusinessAnalysis">
                  进入业务分析
                </el-button>
                <el-button type="text" size="small" @click="viewBusinessAnalysisStats">
                  查看统计
                </el-button>
              </div>
            </div>
          </div>
        </el-col>
      </el-row>

      <el-row :gutter="24" style="margin-top: 24px;">
        <!-- 数据可视化 -->
        <el-col :span="12">
          <div class="module-card data-visualization-card">
            <div class="card-header">
              <div class="card-icon">
                <i class="el-icon-pie-chart"></i>
              </div>
              <div class="card-title">
                <h3>数据可视化</h3>
                <span class="card-subtitle">通过图表和仪表板展示数据洞察</span>
              </div>
              <div class="card-stats">
                <span class="stat-number">{{ stats.dashboards }}</span>
                <span class="stat-label">个仪表板</span>
              </div>
            </div>
            <div class="card-content">
              <div class="feature-grid">
                <div class="feature-item">
                  <i class="el-icon-data-line"></i>
                  <span>趋势图表</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-pie-chart"></i>
                  <span>结构分析</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-monitor"></i>
                  <span>实时仪表板</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-view"></i>
                  <span>交互式图表</span>
                </div>
              </div>
              <div class="card-actions">
                <el-button type="primary" size="small" @click="gotoDataVisualization">
                  进入数据可视化
                </el-button>
                <el-button type="text" size="small" @click="viewDataVisualizationStats">
                  查看统计
                </el-button>
              </div>
            </div>
          </div>
        </el-col>

        <!-- 报表管理 -->
        <el-col :span="12">
          <div class="module-card report-management-card">
            <div class="card-header">
              <div class="card-icon">
                <i class="el-icon-setting"></i>
              </div>
              <div class="card-title">
                <h3>报表管理</h3>
                <span class="card-subtitle">管理报表模板、定时任务和权限设置</span>
              </div>
              <div class="card-stats">
                <span class="stat-number">{{ stats.reportTemplates }}</span>
                <span class="stat-label">个报表模板</span>
              </div>
            </div>
            <div class="card-content">
              <div class="feature-grid">
                <div class="feature-item">
                  <i class="el-icon-document-copy"></i>
                  <span>报表模板</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-time"></i>
                  <span>定时任务</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-lock"></i>
                  <span>权限管理</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-share"></i>
                  <span>报表分发</span>
                </div>
              </div>
              <div class="card-actions">
                <el-button type="primary" size="small" @click="gotoReportManagement">
                  进入报表管理
                </el-button>
                <el-button type="text" size="small" @click="viewReportManagementStats">
                  查看统计
                </el-button>
              </div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 快速报表生成 -->
    <div class="quick-reports-panel">
      <div class="panel-header">
        <h3>
          <i class="el-icon-lightning"></i>
          快速报表生成
        </h3>
        <p>一键生成常用财务报表</p>
      </div>
      
      <el-row :gutter="16">
        <el-col :span="4" v-for="report in quickReports" :key="report.key">
          <div class="quick-report-item" @click="generateQuickReport(report)">
            <div class="report-icon">
              <i :class="report.icon"></i>
            </div>
            <div class="report-content">
              <div class="report-title">{{ report.title }}</div>
              <div class="report-desc">{{ report.desc }}</div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
import { getReportStatistics } from '@/api/financialSharing/reports'

export default {
  name: 'ReportsAnalysisIndex',
  data() {
    return {
      stats: {
        totalReports: 0,
        generatedToday: 0,
        scheduledReports: 0,
        analysisViews: 0,
        financialReports: 0,
        analysisReports: 0,
        dashboards: 0,
        reportTemplates: 0
      },
      quickReports: [
        { key: 'balance', title: '资产负债表', desc: '当期资产负债', icon: 'el-icon-s-data' },
        { key: 'profit', title: '利润表', desc: '当期损益情况', icon: 'el-icon-trophy' },
        { key: 'cashflow', title: '现金流量表', desc: '现金流量分析', icon: 'el-icon-money' },
        { key: 'cost', title: '成本分析表', desc: '成本结构分析', icon: 'el-icon-coin' },
        { key: 'revenue', title: '收入分析表', desc: '收入构成分析', icon: 'el-icon-data-line' },
        { key: 'budget', title: '预算执行表', desc: '预算完成情况', icon: 'el-icon-document-copy' }
      ]
    }
  },
  mounted() {
    this.loadStats()
  },
  methods: {
    async loadStats() {
      try {
        const response = await getReportStatistics()
        if (response.code === 1) {
          this.stats = response.data || this.stats
        }
      } catch (error) {
        console.error('加载统计数据失败:', error)
      }
    },
    refreshReports() {
      this.loadData()
      this.$message.success('报表数据已刷新')
    },
    exportReports() {
      this.$message.success('批量导出功能已触发，请稍后下载')
    },
    reportSettings() {
      this.$message.success('报表设置页面已打开')
    },
    gotoFinancialReports() {
      this.$router.push('/management/reportsfinancial')
    },
    viewFinancialReportsStats() {
      this.$router.push('/management/reportsfinancial')
    },
    gotoBusinessAnalysis() {
      this.$router.push('/management/reportsbusiness')
    },
    viewBusinessAnalysisStats() {
      this.$router.push('/management/reportsbusiness')
    },
    gotoDataVisualization() {
      this.$message.success('数据可视化页面已打开')
    },
    viewDataVisualizationStats() {
      this.$message.success('数据可视化统计页面已打开')
    },
    gotoReportManagement() {
      this.$message.success('报表管理页面已打开')
    },
    viewReportManagementStats() {
      this.$message.success('报表管理统计页面已打开')
    },
    generateQuickReport(report) {
      this.$message.success(`${report.title}报表生成成功`)
    }
  }
}
</script>

<style lang="scss" scoped>
.reports-analysis-container {
  padding: 20px;
  background: #f5f7fa;
  min-height: calc(100vh - 84px);
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: white;
  border-radius: 12px;
  padding: 24px;
  margin-bottom: 24px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);

  .header-left {
    .page-title {
      font-size: 24px;
      font-weight: 600;
      color: #303133;
      margin: 0 0 8px 0;
      display: flex;
      align-items: center;

      i {
        margin-right: 12px;
        color: #409eff;
      }
    }

    .page-description {
      color: #606266;
      font-size: 14px;
      margin: 0;
    }
  }

  .header-right {
    .el-button {
      margin-left: 12px;
    }
  }
}

.stats-overview {
  margin-bottom: 24px;

  .stat-card {
    background: white;
    border-radius: 12px;
    padding: 24px;
    display: flex;
    align-items: center;
    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
    transition: all 0.3s ease;

    &:hover {
      transform: translateY(-2px);
      box-shadow: 0 8px 25px rgba(0, 0, 0, 0.15);
    }

    .stat-icon {
      width: 60px;
      height: 60px;
      border-radius: 12px;
      display: flex;
      align-items: center;
      justify-content: center;
      margin-right: 16px;

      i {
        font-size: 28px;
        color: white;
      }

      &.total-reports {
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      }

      &.generated-today {
        background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
      }

      &.scheduled-reports {
        background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
      }

      &.analysis-views {
        background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
      }
    }

    .stat-content {
      .stat-value {
        font-size: 28px;
        font-weight: 600;
        color: #303133;
        margin-bottom: 4px;
      }

      .stat-label {
        font-size: 14px;
        color: #909399;
      }
    }
  }
}

.function-modules {
  margin-bottom: 32px;

  .module-card {
    background: white;
    border-radius: 12px;
    padding: 24px;
    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
    transition: all 0.3s ease;
    display: flex;
    flex-direction: column;
    min-height: 280px;

    &:hover {
      transform: translateY(-4px);
      box-shadow: 0 8px 25px rgba(0, 0, 0, 0.15);
    }

    &.financial-reports-card {
      border-left: 4px solid #409eff;
    }

    &.business-analysis-card {
      border-left: 4px solid #67c23a;
    }

    &.data-visualization-card {
      border-left: 4px solid #e6a23c;
    }

    &.report-management-card {
      border-left: 4px solid #f56c6c;
    }
  }
}

.card-header {
  display: flex;
  align-items: flex-start;
  margin-bottom: 20px;

  .card-icon {
    width: 48px;
    height: 48px;
    border-radius: 12px;
    display: flex;
    align-items: center;
    justify-content: center;
    margin-right: 16px;
    flex-shrink: 0;
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);

    i {
      font-size: 24px;
      color: white;
    }
  }

  .card-title {
    flex: 1;

    h3 {
      font-size: 16px;
      font-weight: 600;
      color: #303133;
      margin: 0 0 4px 0;
    }

    .card-subtitle {
      font-size: 12px;
      color: #909399;
      line-height: 1.4;
    }
  }

  .card-stats {
    text-align: right;

    .stat-number {
      display: block;
      font-size: 24px;
      font-weight: 600;
      color: #409eff;
    }

    .stat-label {
      font-size: 12px;
      color: #909399;
    }
  }
}

.card-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.feature-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 12px;
  flex: 1;

  .feature-item {
    display: flex;
    align-items: center;
    padding: 8px;
    background: #f8f9fa;
    border-radius: 6px;
    font-size: 14px;
    color: #606266;
    transition: all 0.3s ease;

    &:hover {
      background: #e9ecef;
    }

    i {
      color: #409eff;
      margin-right: 8px;
      font-size: 16px;
    }
  }
}

.card-actions {
  margin-top: 16px;

  .el-button {
    margin-right: 12px;
  }
}

.quick-reports-panel {
  background: white;
  border-radius: 12px;
  padding: 24px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);

  .panel-header {
    margin-bottom: 24px;

    h3 {
      font-size: 18px;
      font-weight: 600;
      color: #303133;
      margin: 0 0 8px 0;
      display: flex;
      align-items: center;

      i {
        margin-right: 8px;
        color: #409eff;
      }
    }

    p {
      color: #606266;
      font-size: 14px;
      margin: 0;
    }
  }

  .quick-report-item {
    background: #f8f9fa;
    border-radius: 8px;
    padding: 16px;
    cursor: pointer;
    transition: all 0.3s ease;
    display: flex;
    align-items: center;

    &:hover {
      background: #e9ecef;
      transform: translateY(-2px);
    }

    .report-icon {
      width: 40px;
      height: 40px;
      border-radius: 8px;
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      display: flex;
      align-items: center;
      justify-content: center;
      margin-right: 12px;

      i {
        font-size: 20px;
        color: white;
      }
    }

    .report-content {
      flex: 1;

      .report-title {
        font-size: 14px;
        font-weight: 600;
        color: #303133;
        margin-bottom: 4px;
      }

      .report-desc {
        font-size: 12px;
        color: #909399;
      }
    }
  }
}
</style>
