<template>
  <div class="analysis-dashboard">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>分析仪表板</h2>
      <p>实时展示预算分析关键指标和趋势，提供可视化的决策支持仪表板</p>
    </div>

    <!-- 操作工具栏 -->
    <el-card class="toolbar-card" shadow="never">
      <el-row :gutter="16">
        <el-col :span="12">
          <el-button-group>
            <el-button type="primary" icon="el-icon-refresh" @click="handleRefreshDashboard">刷新仪表板</el-button>
            <el-button type="success" icon="el-icon-setting" @click="handleCustomizeDashboard">自定义布局</el-button>
            <el-button type="warning" icon="el-icon-download" @click="handleExportDashboard">导出仪表板</el-button>
            <el-button type="info" icon="el-icon-full-screen" @click="handleFullScreen">全屏显示</el-button>
          </el-button-group>
        </el-col>
        <el-col :span="12" class="text-right">
          <el-button-group>
            <el-button icon="el-icon-timer" @click="handleAutoRefreshSettings">自动刷新</el-button>
            <el-button icon="el-icon-share" @click="handleShareDashboard">分享</el-button>
            <el-button icon="el-icon-help" @click="handleHelp">帮助</el-button>
          </el-button-group>
        </el-col>
      </el-row>
    </el-card>

    <!-- 仪表板概览 -->
    <el-row :gutter="20" class="stats-row">
      <el-col :span="6">
        <el-card class="stat-card kpi-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ dashboardStats.totalKPIs }}</div>
            <div class="stat-label">关键指标</div>
            <div class="stat-description">监控的KPI指标数量</div>
            <div class="stat-trend">
              <i class="el-icon-data-line"></i>
              <span>实时监控</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-data-line"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card widgets-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ dashboardStats.totalWidgets }}</div>
            <div class="stat-label">仪表板组件</div>
            <div class="stat-description">当前显示的组件数</div>
            <div class="stat-trend">
              <i class="el-icon-s-grid"></i>
              <span>可自定义</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-s-grid"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card alerts-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ dashboardStats.activeAlerts }}</div>
            <div class="stat-label">活跃预警</div>
            <div class="stat-description">当前活跃的预警数</div>
            <div class="stat-trend">
              <i class="el-icon-warning"></i>
              <span>需要关注</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-warning"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card performance-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ dashboardStats.performanceScore }}%</div>
            <div class="stat-label">综合绩效</div>
            <div class="stat-description">整体预算绩效评分</div>
            <div class="stat-trend">
              <i class="el-icon-trophy"></i>
              <span>表现良好</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-trophy"></i>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 仪表板配置 -->
    <el-card class="config-card" shadow="never">
      <div class="config-header">
        <span class="config-title">仪表板配置</span>
        <div class="config-tools">
          <el-tooltip content="自动刷新" placement="top">
            <el-switch
              v-model="autoRefresh"
              active-text="自动刷新"
              @change="handleAutoRefreshChange"
            />
          </el-tooltip>
          <el-tooltip content="实时数据" placement="top">
            <el-switch
              v-model="realTimeData"
              active-text="实时数据"
              @change="handleRealTimeDataChange"
            />
          </el-tooltip>
          <el-button type="text" @click="handleResetConfig">重置配置</el-button>
        </div>
      </div>
      <el-form :model="dashboardForm" :inline="true" size="small">
        <el-form-item label="时间范围">
          <el-select
            v-model="dashboardForm.timeRange"
            placeholder="请选择时间范围"
            style="width: 150px"
          >
            <el-option value="TODAY" label="今日" />
            <el-option value="WEEK" label="本周" />
            <el-option value="MONTH" label="本月" />
            <el-option value="QUARTER" label="本季度" />
            <el-option value="YEAR" label="本年度" />
            <el-option value="CUSTOM" label="自定义" />
          </el-select>
        </el-form-item>
        <el-form-item label="组织单元">
          <el-cascader
            v-model="dashboardForm.organizationPath"
            :options="organizationOptions"
            :props="{ checkStrictly: true, emitPath: false }"
            placeholder="请选择组织单元"
            clearable
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="显示模式">
          <el-radio-group v-model="dashboardForm.displayMode">
            <el-radio-button label="GRID">网格布局</el-radio-button>
            <el-radio-button label="LIST">列表布局</el-radio-button>
            <el-radio-button label="CARD">卡片布局</el-radio-button>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="刷新间隔">
          <el-select
            v-model="dashboardForm.refreshInterval"
            placeholder="请选择刷新间隔"
            style="width: 120px"
          >
            <el-option :value="10" label="10秒" />
            <el-option :value="30" label="30秒" />
            <el-option :value="60" label="1分钟" />
            <el-option :value="300" label="5分钟" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleApplyConfig">应用配置</el-button>
          <el-button icon="el-icon-refresh-left" @click="handleResetDashboard">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 核心KPI指标 -->
    <el-row :gutter="20" class="kpi-row">
      <el-col :span="8">
        <el-card class="kpi-card" shadow="never">
          <div slot="header" class="card-header">
            <span>预算执行率</span>
            <div class="header-tools">
              <el-tag :type="getKPIStatusColor(kpiData.executionRate.status)" size="mini">
                {{ getKPIStatusText(kpiData.executionRate.status) }}
              </el-tag>
            </div>
          </div>
          <div class="kpi-content">
            <div class="kpi-value">{{ kpiData.executionRate.value }}%</div>
            <div class="kpi-target">目标: {{ kpiData.executionRate.target }}%</div>
            <el-progress
              :percentage="kpiData.executionRate.value"
              :color="getKPIProgressColor(kpiData.executionRate.value)"
              :stroke-width="8"
            />
            <div class="kpi-trend">
              <i :class="getKPITrendIcon(kpiData.executionRate.trend)"></i>
              <span>{{ getKPITrendText(kpiData.executionRate.trend) }}</span>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card class="kpi-card" shadow="never">
          <div slot="header" class="card-header">
            <span>预算偏差率</span>
            <div class="header-tools">
              <el-tag :type="getKPIStatusColor(kpiData.varianceRate.status)" size="mini">
                {{ getKPIStatusText(kpiData.varianceRate.status) }}
              </el-tag>
            </div>
          </div>
          <div class="kpi-content">
            <div class="kpi-value">{{ kpiData.varianceRate.value }}%</div>
            <div class="kpi-target">目标: ≤{{ kpiData.varianceRate.target }}%</div>
            <el-progress
              :percentage="Math.abs(kpiData.varianceRate.value)"
              :color="getVarianceProgressColor(kpiData.varianceRate.value)"
              :stroke-width="8"
            />
            <div class="kpi-trend">
              <i :class="getKPITrendIcon(kpiData.varianceRate.trend)"></i>
              <span>{{ getKPITrendText(kpiData.varianceRate.trend) }}</span>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card class="kpi-card" shadow="never">
          <div slot="header" class="card-header">
            <span>预算准确率</span>
            <div class="header-tools">
              <el-tag :type="getKPIStatusColor(kpiData.accuracyRate.status)" size="mini">
                {{ getKPIStatusText(kpiData.accuracyRate.status) }}
              </el-tag>
            </div>
          </div>
          <div class="kpi-content">
            <div class="kpi-value">{{ kpiData.accuracyRate.value }}%</div>
            <div class="kpi-target">目标: ≥{{ kpiData.accuracyRate.target }}%</div>
            <el-progress
              :percentage="kpiData.accuracyRate.value"
              :color="getKPIProgressColor(kpiData.accuracyRate.value)"
              :stroke-width="8"
            />
            <div class="kpi-trend">
              <i :class="getKPITrendIcon(kpiData.accuracyRate.trend)"></i>
              <span>{{ getKPITrendText(kpiData.accuracyRate.trend) }}</span>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 主要图表区域 -->
    <el-row :gutter="20" class="chart-row">
      <el-col :span="12">
        <el-card class="chart-card" shadow="never">
          <div slot="header" class="card-header">
            <span>预算执行趋势</span>
            <div class="header-tools">
              <el-radio-group v-model="executionChartType" size="mini">
                <el-radio-button label="line">趋势图</el-radio-button>
                <el-radio-button label="bar">柱状图</el-radio-button>
                <el-radio-button label="area">面积图</el-radio-button>
              </el-radio-group>
            </div>
          </div>
          <div id="executionTrendChart" class="chart-container"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card class="chart-card" shadow="never">
          <div slot="header" class="card-header">
            <span>预算分布分析</span>
            <div class="header-tools">
              <el-radio-group v-model="distributionChartType" size="mini">
                <el-radio-button label="pie">饼图</el-radio-button>
                <el-radio-button label="doughnut">环形图</el-radio-button>
                <el-radio-button label="sunburst">旭日图</el-radio-button>
              </el-radio-group>
            </div>
          </div>
          <div id="distributionChart" class="chart-container"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 次要图表区域 -->
    <el-row :gutter="20" class="secondary-chart-row">
      <el-col :span="8">
        <el-card class="chart-card" shadow="never">
          <div slot="header" class="card-header">
            <span>部门绩效排名</span>
            <el-button icon="el-icon-refresh" size="mini" @click="refreshDepartmentRanking" />
          </div>
          <div class="ranking-list">
            <div
              v-for="(item, index) in departmentRanking"
              :key="item.id"
              class="ranking-item"
              :class="getDepartmentRankingClass(index)"
            >
              <div class="ranking-number">{{ index + 1 }}</div>
              <div class="ranking-content">
                <div class="ranking-name">{{ item.departmentName }}</div>
                <div class="ranking-score">绩效: {{ item.score }}分</div>
              </div>
              <div class="ranking-badge">
                <el-tag :type="getPerformanceLevelColor(item.level)" size="mini">
                  {{ getPerformanceLevelText(item.level) }}
                </el-tag>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card class="chart-card" shadow="never">
          <div slot="header" class="card-header">
            <span>预警监控</span>
            <el-button icon="el-icon-refresh" size="mini" @click="refreshAlertMonitor" />
          </div>
          <div id="alertMonitorChart" class="chart-container"></div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card class="chart-card" shadow="never">
          <div slot="header" class="card-header">
            <span>资金流向</span>
            <el-button icon="el-icon-refresh" size="mini" @click="refreshCashFlow" />
          </div>
          <div id="cashFlowChart" class="chart-container"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 实时数据流 -->
    <el-card class="realtime-card" shadow="never" v-if="realTimeData">
      <div slot="header" class="card-header">
        <span>实时数据流</span>
        <div class="header-tools">
          <el-tag type="success" size="mini">
            <i class="el-icon-video-play"></i>
            实时更新
          </el-tag>
          <el-button icon="el-icon-pause" size="mini" @click="handlePauseRealTime">暂停</el-button>
        </div>
      </div>
      <div class="realtime-content">
        <el-row :gutter="16">
          <el-col :span="6" v-for="metric in realTimeMetrics" :key="metric.id">
            <div class="realtime-metric">
              <div class="metric-name">{{ metric.name }}</div>
              <div class="metric-value" :class="getMetricValueClass(metric.change)">
                {{ metric.value }}
                <span class="metric-change">
                  <i :class="getMetricChangeIcon(metric.change)"></i>
                  {{ metric.change }}%
                </span>
              </div>
              <div class="metric-time">{{ metric.updateTime }}</div>
            </div>
          </el-col>
        </el-row>
      </div>
    </el-card>

    <!-- 快捷操作面板 -->
    <el-card class="quick-actions-card" shadow="never">
      <div slot="header" class="card-header">
        <span>快捷操作</span>
        <el-button icon="el-icon-setting" size="mini" @click="handleCustomizeActions">自定义</el-button>
      </div>
      <el-row :gutter="16">
        <el-col :span="4" v-for="action in quickActions" :key="action.id">
          <el-card class="quick-action-item" shadow="hover" @click.native="handleQuickAction(action)">
            <div class="action-icon">
              <i :class="action.icon"></i>
            </div>
            <div class="action-title">{{ action.title }}</div>
            <div class="action-description">{{ action.description }}</div>
          </el-card>
        </el-col>
      </el-row>
    </el-card>
    <!-- 设置对话框 -->
    <el-dialog title="自动刷新设置" :visible.sync="settingsDialogVisible" width="500px">
      <el-form label-width="120px" size="small">
        <el-form-item label="刷新间隔">
          <el-select v-model="dashboardForm.refreshInterval" style="width: 100%">
            <el-option label="10秒" :value="10" />
            <el-option label="30秒" :value="30" />
            <el-option label="1分钟" :value="60" />
            <el-option label="5分钟" :value="300" />
          </el-select>
        </el-form-item>
        <el-form-item label="自动刷新">
          <el-switch v-model="autoRefresh" />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="settingsDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSaveAutoRefreshSettings">保存</el-button>
      </div>
    </el-dialog>

    <!-- 自定义布局对话框 -->
    <el-dialog title="自定义仪表板布局" :visible.sync="customizeDialogVisible" width="700px">
      <div class="customize-layout">
        <p style="color:#606266;margin-bottom:16px">拖拽调整组件顺序，勾选控制显示/隐藏</p>
        <el-table :data="layoutWidgets" border size="small">
          <el-table-column label="组件名称" prop="name" />
          <el-table-column label="描述" prop="description" />
          <el-table-column label="显示" width="80" align="center">
            <template slot-scope="scope">
              <el-switch v-model="scope.row.visible" />
            </template>
          </el-table-column>
          <el-table-column label="排序" width="120" align="center">
            <template slot-scope="scope">
              <el-button icon="el-icon-arrow-up" size="mini" circle @click="moveWidget(scope.$index, -1)" :disabled="scope.$index === 0" />
              <el-button icon="el-icon-arrow-down" size="mini" circle @click="moveWidget(scope.$index, 1)" :disabled="scope.$index === layoutWidgets.length - 1" />
            </template>
          </el-table-column>
        </el-table>
      </div>
      <div slot="footer">
        <el-button @click="customizeDialogVisible = false">取消</el-button>
        <el-button @click="resetLayout">重置默认</el-button>
        <el-button type="primary" @click="handleSaveLayout">保存布局</el-button>
      </div>
    </el-dialog>

    <!-- 快捷操作弹窗：差异分析 -->
    <el-dialog title="差异分析" :visible.sync="varianceDialogVisible" width="600px">
      <el-form :model="varianceForm" label-width="100px" size="small">
        <el-form-item label="分析时间范围">
          <el-select v-model="varianceForm.timeRange" style="width:100%">
            <el-option value="MONTH" label="本月" />
            <el-option value="QUARTER" label="本季度" />
            <el-option value="YEAR" label="本年度" />
          </el-select>
        </el-form-item>
        <el-form-item label="差异阈值(%)">
          <el-input-number v-model="varianceForm.threshold" :min="0" :max="100" style="width:100%" />
        </el-form-item>
      </el-form>
      <div v-if="varianceResult" class="variance-result">
        <el-divider>分析结果</el-divider>
        <el-table :data="varianceResult" border size="small" max-height="300">
          <el-table-column prop="department" label="部门" />
          <el-table-column prop="budget" label="预算金额" />
          <el-table-column prop="actual" label="实际金额" />
          <el-table-column prop="variance" label="差异率">
            <template slot-scope="scope">
              <el-tag :type="Math.abs(scope.row.variance) > varianceForm.threshold ? 'danger' : 'success'" size="mini">
                {{ scope.row.variance }}%
              </el-tag>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <div slot="footer">
        <el-button @click="varianceDialogVisible = false">关闭</el-button>
        <el-button type="primary" @click="runVarianceAnalysis" :loading="varianceLoading">执行分析</el-button>
      </div>
    </el-dialog>

    <!-- 快捷操作弹窗：趋势分析 -->
    <el-dialog title="趋势分析" :visible.sync="trendDialogVisible" width="600px">
      <el-form :model="trendForm" label-width="100px" size="small">
        <el-form-item label="分析周期">
          <el-select v-model="trendForm.period" style="width:100%">
            <el-option value="MONTH" label="月度" />
            <el-option value="QUARTER" label="季度" />
            <el-option value="YEAR" label="年度" />
          </el-select>
        </el-form-item>
        <el-form-item label="预测期数">
          <el-input-number v-model="trendForm.forecastPeriods" :min="1" :max="12" style="width:100%" />
        </el-form-item>
      </el-form>
      <div id="trendAnalysisChart" style="height:280px;margin-top:12px"></div>
      <div slot="footer">
        <el-button @click="trendDialogVisible = false">关闭</el-button>
        <el-button type="primary" @click="runTrendAnalysis" :loading="trendLoading">生成趋势图</el-button>
      </div>
    </el-dialog>

    <!-- 快捷操作弹窗：绩效报告 -->
    <el-dialog title="生成绩效报告" :visible.sync="reportDialogVisible" width="500px">
      <el-form :model="reportForm" label-width="100px" size="small">
        <el-form-item label="报告名称">
          <el-input v-model="reportForm.reportName" placeholder="请输入报告名称" />
        </el-form-item>
        <el-form-item label="报告类型">
          <el-select v-model="reportForm.reportType" style="width:100%">
            <el-option value="MONTHLY" label="月度报告" />
            <el-option value="QUARTERLY" label="季度报告" />
            <el-option value="ANNUAL" label="年度报告" />
          </el-select>
        </el-form-item>
        <el-form-item label="时间范围">
          <el-select v-model="reportForm.timeRange" style="width:100%">
            <el-option value="MONTH" label="本月" />
            <el-option value="QUARTER" label="本季度" />
            <el-option value="YEAR" label="本年度" />
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="reportDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="generateReport" :loading="reportLoading">生成报告</el-button>
      </div>
    </el-dialog>

    <!-- 快捷操作弹窗：预警设置 -->
    <el-dialog title="预警规则设置" :visible.sync="alertDialogVisible" width="600px">
      <el-form :model="alertForm" label-width="120px" size="small">
        <el-form-item label="执行率预警阈值">
          <el-slider v-model="alertForm.executionThreshold" :min="50" :max="100" show-input />
        </el-form-item>
        <el-form-item label="偏差率预警阈值">
          <el-slider v-model="alertForm.varianceThreshold" :min="0" :max="30" show-input />
        </el-form-item>
        <el-form-item label="预警通知方式">
          <el-checkbox-group v-model="alertForm.notifyMethods">
            <el-checkbox label="SYSTEM">系统通知</el-checkbox>
            <el-checkbox label="EMAIL">邮件通知</el-checkbox>
          </el-checkbox-group>
        </el-form-item>
        <el-form-item label="启用预警">
          <el-switch v-model="alertForm.enabled" />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="alertDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveAlertSettings">保存设置</el-button>
      </div>
    </el-dialog>

    <!-- 快捷操作弹窗：系统设置 -->
    <el-dialog title="系统参数配置" :visible.sync="sysSettingsDialogVisible" width="500px">
      <el-form :model="sysSettingsForm" label-width="130px" size="small">
        <el-form-item label="默认时间范围">
          <el-select v-model="sysSettingsForm.defaultTimeRange" style="width:100%">
            <el-option value="MONTH" label="本月" />
            <el-option value="QUARTER" label="本季度" />
            <el-option value="YEAR" label="本年度" />
          </el-select>
        </el-form-item>
        <el-form-item label="默认显示模式">
          <el-radio-group v-model="sysSettingsForm.defaultDisplayMode">
            <el-radio-button label="GRID">网格</el-radio-button>
            <el-radio-button label="LIST">列表</el-radio-button>
            <el-radio-button label="CARD">卡片</el-radio-button>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="图表动画">
          <el-switch v-model="sysSettingsForm.chartAnimation" />
        </el-form-item>
        <el-form-item label="数据精度(小数位)">
          <el-input-number v-model="sysSettingsForm.precision" :min="0" :max="4" style="width:100%" />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="sysSettingsDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveSysSettings">保存</el-button>
      </div>
    </el-dialog>

    <!-- 帮助对话框 -->
    <el-dialog title="分析仪表盘帮助" :visible.sync="helpDialogVisible" width="600px">
      <div class="help-content">
        <h4>功能说明</h4>
        <p>分析仪表盘提供预算分析的可视化概览，支持自定义布局和多维度数据展示。</p>
        <h4>操作指南</h4>
        <p>1. 使用顶部工具栏的按钮进行刷新、自定义布局和导出操作。</p>
        <p>2. 在仪表板配置区域选择时间范围、组织单元后点击"应用配置"刷新数据。</p>
        <p>3. 点击图表上方的切换按钮可切换图表类型（趋势图/柱状图/面积图、饼图/环形图/旭日图）。</p>
        <p>4. 快捷操作面板提供差异分析、趋势分析、绩效报告、预警设置、数据导出等功能。</p>
      </div>
      <div slot="footer"><el-button type="primary" @click="helpDialogVisible = false">我知道了</el-button></div>
    </el-dialog>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import { budgetAnalysisApi } from '@/api/managementAccountant/ncv65/budgetAnalysis'

export default {
  name: 'AnalysisDashboard',
  data() {
    return {
      createDialogVisible: false,
      settingsDialogVisible: false,
      helpDialogVisible: false,
      customizeDialogVisible: false,
      varianceDialogVisible: false,
      trendDialogVisible: false,
      reportDialogVisible: false,
      alertDialogVisible: false,
      sysSettingsDialogVisible: false,
      queryParams: { pageSize: 10 },
      // 自定义布局
      layoutWidgets: [
        { name: '仪表板概览', description: '关键指标统计卡片', visible: true },
        { name: '核心KPI指标', description: '执行率/偏差率/准确率', visible: true },
        { name: '预算执行趋势', description: '趋势折线/柱状/面积图', visible: true },
        { name: '预算分布分析', description: '饼图/环形图/旭日图', visible: true },
        { name: '部门绩效排名', description: '部门绩效排行榜', visible: true },
        { name: '预警监控', description: '预警级别分布图', visible: true },
        { name: '资金流向', description: '资金流入流出图', visible: true },
        { name: '快捷操作', description: '常用功能快捷入口', visible: true }
      ],
      // 差异分析表单
      varianceForm: { timeRange: 'MONTH', threshold: 5 },
      varianceResult: null,
      varianceLoading: false,
      // 趋势分析表单
      trendForm: { period: 'MONTH', forecastPeriods: 3 },
      trendLoading: false,
      // 绩效报告表单
      reportForm: { reportName: '', reportType: 'MONTHLY', timeRange: 'MONTH' },
      reportLoading: false,
      // 预警设置表单
      alertForm: { executionThreshold: 80, varianceThreshold: 10, notifyMethods: ['SYSTEM'], enabled: true },
      // 系统设置表单
      sysSettingsForm: { defaultTimeRange: 'MONTH', defaultDisplayMode: 'GRID', chartAnimation: true, precision: 2 },
      // 仪表板配置
      dashboardForm: {
        timeRange: 'MONTH',
        organizationPath: '',
        displayMode: 'GRID',
        refreshInterval: 30
      },
      
      // 控制开关
      autoRefresh: true,
      realTimeData: true,
      refreshTimer: null,
      
      // 图表类型
      executionChartType: 'line',
      distributionChartType: 'pie',
      
      // 统计数据
      dashboardStats: {
        totalKPIs: 0,
        totalWidgets: 0,
        activeAlerts: 0,
        performanceScore: 0
      },
      
      // KPI数据
      kpiData: {
        executionRate: {
          value: 87.5,
          target: 85,
          status: 'GOOD',
          trend: 'UP'
        },
        varianceRate: {
          value: -3.2,
          target: 5,
          status: 'GOOD',
          trend: 'DOWN'
        },
        accuracyRate: {
          value: 92.8,
          target: 90,
          status: 'EXCELLENT',
          trend: 'UP'
        }
      },
      
      // 部门排名
      departmentRanking: [
        { id: 1, departmentName: '销售部', score: 95.2, level: 'EXCELLENT' },
        { id: 2, departmentName: '研发部', score: 89.7, level: 'GOOD' },
        { id: 3, departmentName: '市场部', score: 85.3, level: 'GOOD' },
        { id: 4, departmentName: '财务部', score: 82.1, level: 'AVERAGE' },
        { id: 5, departmentName: '行政部', score: 78.9, level: 'AVERAGE' }
      ],
      
      // 实时指标
      realTimeMetrics: [
        { id: 1, name: '当日执行', value: '1,250万', change: 2.3, updateTime: '刚刚' },
        { id: 2, name: '当日差异', value: '-35万', change: -1.2, updateTime: '1分钟前' },
        { id: 3, name: '预警数量', value: '3个', change: 0, updateTime: '2分钟前' },
        { id: 4, name: '审批待办', value: '8个', change: -2.1, updateTime: '3分钟前' }
      ],
      
      // 快捷操作
      quickActions: [
        {
          id: 1,
          title: '差异分析',
          description: '查看预算差异',
          icon: 'el-icon-data-analysis'
        },
        {
          id: 2,
          title: '趋势分析',
          description: '查看执行趋势',
          icon: 'el-icon-trend-charts'
        },
        {
          id: 3,
          title: '绩效报告',
          description: '生成绩效报告',
          icon: 'el-icon-document'
        },
        {
          id: 4,
          title: '预警设置',
          description: '配置预警规则',
          icon: 'el-icon-warning'
        },
        {
          id: 5,
          title: '数据导出',
          description: '导出分析数据',
          icon: 'el-icon-download'
        },
        {
          id: 6,
          title: '系统设置',
          description: '配置系统参数',
          icon: 'el-icon-setting'
        }
      ],
      
      // 选项数据
      organizationOptions: []
    }
  },

  watch: {
    // 监听执行趋势图表类型切换
    executionChartType() {
      this.$nextTick(() => {
        const dom = document.getElementById('executionTrendChart')
        if (!dom) return
        const chart = echarts.getInstanceByDom(dom)
        if (chart) {
          chart.setOption({
            series: [
              { type: this.executionChartType === 'area' ? 'line' : this.executionChartType, areaStyle: this.executionChartType === 'area' ? {} : null },
              { type: this.executionChartType === 'area' ? 'line' : this.executionChartType, areaStyle: this.executionChartType === 'area' ? {} : null },
              { type: 'line' }
            ]
          })
        }
      })
    },
    // 监听分布图表类型切换
    distributionChartType() {
      this.$nextTick(() => {
        const dom = document.getElementById('distributionChart')
        if (!dom) return
        let chart = echarts.getInstanceByDom(dom)
        if (chart) {
          chart.dispose()
        }
        chart = echarts.init(dom)
        this.updateDistributionChart(chart)
        // 重新加载数据
        this.loadDashboardChartData()
      })
    }
  },

  created() {
    this.loadOrganizationOptions()
    this.loadDashboardStats()
  },

  mounted() {
    this.initCharts()
    this.startAutoRefresh()
  },

  beforeDestroy() {
    this.stopAutoRefresh()
  },

  methods: {
    // 加载组织选项
    async loadOrganizationOptions() {
      try {
        const response = await budgetAnalysisApi.getOrganizations()
        this.organizationOptions = response.data
      } catch (error) {
        console.error('加载组织选项失败：', error)
      }
    },

    // 初始化图表
    initCharts() {
      this.$nextTick(() => {
        this.initExecutionTrendChart()
        this.initDistributionChart()
        this.initAlertMonitorChart()
        this.initCashFlowChart()
        this.loadDashboardChartData()
      })
    },

    // 加载统计数据
    async loadDashboardStats() {
      try {
        const response = await budgetAnalysisApi.getDashboardData()
        if (response.code === 1 && response.data) {
          const d = response.data
          this.dashboardStats = {
            totalKPIs: d.totalKPIs || 0,
            totalWidgets: d.totalWidgets || 0,
            activeAlerts: d.activeAlerts || 0,
            performanceScore: d.performanceScore || 0
          }
          // 更新KPI数据
          if (d.executionRate) this.kpiData.executionRate.value = d.executionRate
          if (d.varianceRate) this.kpiData.varianceRate.value = d.varianceRate
          if (d.accuracyRate) this.kpiData.accuracyRate.value = d.accuracyRate
          // 更新部门排名
          if (d.departmentRanking && d.departmentRanking.length > 0) {
            this.departmentRanking = d.departmentRanking.map((dept, idx) => ({
              id: idx + 1,
              departmentName: dept.name,
              score: dept.executionRate,
              level: dept.executionRate >= 90 ? 'EXCELLENT' : dept.executionRate >= 80 ? 'GOOD' : 'AVERAGE'
            }))
          }
        }
      } catch (error) {
        console.error('加载仪表板统计数据失败：', error)
      }
    },

    // 加载图表数据
    async loadDashboardChartData() {
      try {
        const response = await budgetAnalysisApi.getDashboardChartData(this.dashboardForm)
        if (response.code === 1 && response.data) {
          const d = response.data
          const getChart = (domId) => {
            const dom = document.getElementById(domId)
            if (!dom) return null
            return echarts.getInstanceByDom(dom) || echarts.init(dom)
          }
          // 预算执行趋势
          if (d.executionTrend && d.executionTrend.length > 0) {
            const chart = getChart('executionTrendChart')
            if (chart) {
              const categories = d.categories || d.executionTrend.map(item => item.month)
              chart.setOption({
                xAxis: { data: categories },
                series: [
                  { data: d.executionTrend.map(item => item.budget) },
                  { data: d.executionTrend.map(item => item.actual) },
                  { data: d.executionTrend.map(item => item.rate) }
                ]
              })
            }
          }
          // 预算分布
          if (d.distributionData && d.distributionData.length > 0) {
            const chart = getChart('distributionChart')
            if (chart) {
              chart.setOption({
                series: [{ data: d.distributionData }]
              })
            }
          }
          // 预警监控
          if (d.alertData && d.alertData.length > 0) {
            const chart = getChart('alertMonitorChart')
            if (chart) {
              const levelNameMap = { 'HIGH': '高级预警', 'MEDIUM': '中级预警', 'LOW': '低级预警', '高': '高级预警', '中': '中级预警', '低': '低级预警' }
              const levelColorMap = { 'HIGH': '#F56C6C', 'MEDIUM': '#E6A23C', 'LOW': '#67C23A', '高': '#F56C6C', '中': '#E6A23C', '低': '#67C23A' }
              const alertPieData = d.alertData.map(item => ({
                name: levelNameMap[item.level] || item.level,
                value: item.count,
                itemStyle: { color: levelColorMap[item.level] || '#409EFF' }
              }))
              chart.setOption({
                series: [{ data: alertPieData }]
              })
            }
          }
          // 资金流向
          if (d.cashFlowData && d.cashFlowData.length > 0) {
            const chart = getChart('cashFlowChart')
            if (chart) {
              const months = d.cashFlowData.map(item => item.month)
              chart.setOption({
                legend: { data: ['资金流入', '资金流出', '净流量'], top: 30 },
                xAxis: { data: months },
                series: [
                  { name: '资金流入', type: 'bar', data: d.cashFlowData.map(item => item.inflow), itemStyle: { color: '#67C23A' } },
                  { name: '资金流出', type: 'bar', data: d.cashFlowData.map(item => item.outflow), itemStyle: { color: '#F56C6C' } },
                  { name: '净流量', type: 'line', data: d.cashFlowData.map(item => item.net), itemStyle: { color: '#409EFF' } }
                ]
              })
            }
          }
        }
      } catch (error) {
        console.error('加载仪表板图表数据失败：', error)
      }
    },

    // 初始化执行趋势图表
    initExecutionTrendChart() {
      const chartDom = document.getElementById('executionTrendChart')
      if (chartDom) {
        const myChart = echarts.init(chartDom)
        this.updateExecutionTrendChart(myChart)
      }
    },
    
    // 更新执行趋势图表
    updateExecutionTrendChart(chart) {
      const option = {
        title: {
          text: '预算执行趋势',
          textStyle: { fontSize: 14 }
        },
        tooltip: {
          trigger: 'axis'
        },
        legend: {
          data: ['预算金额', '实际执行', '执行率'],
          top: 30
        },
        xAxis: {
          type: 'category',
          data: []
        },
        yAxis: [
          {
            type: 'value',
            name: '金额(万元)',
            position: 'left'
          },
          {
            type: 'value',
            name: '执行率(%)',
            position: 'right',
            max: 100
          }
        ],
        series: [
          {
            name: '预算金额',
            type: this.executionChartType,
            data: [],
            itemStyle: { color: '#409EFF' }
          },
          {
            name: '实际执行',
            type: this.executionChartType,
            data: [],
            itemStyle: { color: '#67C23A' }
          },
          {
            name: '执行率',
            type: 'line',
            yAxisIndex: 1,
            data: [],
            itemStyle: { color: '#E6A23C' }
          }
        ]
      }
      chart.setOption(option)
    },
    
    // 初始化分布图表
    initDistributionChart() {
      const chartDom = document.getElementById('distributionChart')
      if (chartDom) {
        const myChart = echarts.init(chartDom)
        this.updateDistributionChart(myChart)
      }
    },
    
    // 更新分布图表
    updateDistributionChart(chart) {
      let option = {}
      
      if (this.distributionChartType === 'pie') {
        option = {
          title: {
            text: '预算分布',
            textStyle: { fontSize: 14 }
          },
          tooltip: {
            trigger: 'item'
          },
          legend: {
            orient: 'vertical',
            left: 'left'
          },
          series: [
            {
              name: '预算分布',
              type: 'pie',
              radius: '50%',
              data: [],
              emphasis: {
                itemStyle: {
                  shadowBlur: 10,
                  shadowOffsetX: 0,
                  shadowColor: 'rgba(0, 0, 0, 0.5)'
                }
              }
            }
          ]
        }
      } else if (this.distributionChartType === 'doughnut') {
        option = {
          title: { text: '预算分布', textStyle: { fontSize: 14 } },
          tooltip: { trigger: 'item' },
          legend: { orient: 'vertical', left: 'left' },
          series: [{ name: '预算分布', type: 'pie', radius: ['40%', '70%'], data: [] }]
        }
      } else if (this.distributionChartType === 'sunburst') {
        option = {
          title: { text: '预算分布', textStyle: { fontSize: 14 } },
          tooltip: { trigger: 'item' },
          series: [{
            name: '预算分布',
            type: 'sunburst',
            radius: ['15%', '80%'],
            data: [],
            emphasis: { focus: 'ancestor' },
            levels: [
              {},
              { r0: '15%', r: '45%', itemStyle: { borderWidth: 2 }, label: { rotate: 'tangential' } },
              { r0: '45%', r: '70%', label: { align: 'right' } },
              { r0: '70%', r: '72%', label: { position: 'outside', padding: 3, silent: false }, itemStyle: { borderWidth: 3 } }
            ]
          }]
        }
      }
      
      chart.setOption(option)
    },
    
    // 初始化预警监控图表
    initAlertMonitorChart() {
      const chartDom = document.getElementById('alertMonitorChart')
      if (chartDom) {
        const myChart = echarts.init(chartDom)
        const option = {
          title: {
            text: '预警监控',
            textStyle: { fontSize: 14 }
          },
          tooltip: {
            trigger: 'item'
          },
          series: [
            {
              name: '预警级别',
              type: 'pie',
              radius: ['40%', '70%'],
              data: []
            }
          ]
        }
        myChart.setOption(option)
      }
    },
    
    // 初始化资金流向图表
    initCashFlowChart() {
      const chartDom = document.getElementById('cashFlowChart')
      if (chartDom) {
        const myChart = echarts.init(chartDom)
        const option = {
          title: {
            text: '资金流向',
            textStyle: { fontSize: 14 }
          },
          tooltip: {
            trigger: 'axis'
          },
          xAxis: {
            type: 'category',
            data: []
          },
          yAxis: {
            type: 'value',
            name: '金额(万元)'
          },
          series: [
            {
              name: '资金流向',
              type: 'bar',
              data: []
            }
          ]
        }
        myChart.setOption(option)
      }
    },
    
    // 刷新仪表板
    handleRefreshDashboard() {
      this.loadDashboardStats()
      this.initCharts()
      this.$message.success('仪表板已刷新')
    },

    // 自定义仪表板 - 打开布局配置弹窗
    handleCustomizeDashboard() {
      this.customizeDialogVisible = true
    },

    // 移动布局组件顺序
    moveWidget(index, direction) {
      const newIndex = index + direction
      if (newIndex < 0 || newIndex >= this.layoutWidgets.length) return
      const arr = [...this.layoutWidgets]
      const tmp = arr[index]
      arr[index] = arr[newIndex]
      arr[newIndex] = tmp
      this.layoutWidgets = arr
    },

    // 重置默认布局
    resetLayout() {
      this.layoutWidgets.forEach(w => { w.visible = true })
      this.$message.success('已重置为默认布局')
    },

    // 保存布局
    async handleSaveLayout() {
      try {
        const response = await budgetAnalysisApi.saveLayout({ layoutWidgets: this.layoutWidgets })
        if (response.code === 1) {
          this.customizeDialogVisible = false
          this.$message.success('布局已保存')
        } else {
          this.$message.error(response.msg || '保存失败')
        }
      } catch (e) {
        this.$message.error('保存失败：' + e.message)
      }
    },

    // 导出仪表板 - 触发文件下载
    async handleExportDashboard() {
      try {
        this.$message.info('正在导出，请稍候...')
        const response = await budgetAnalysisApi.exportDashboard(this.dashboardForm)
        const blob = response instanceof Blob ? response : new Blob([response], { type: 'application/vnd.ms-excel' })
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = `仪表板_${new Date().toLocaleDateString()}.xlsx`
        document.body.appendChild(link)
        link.click()
        document.body.removeChild(link)
        window.URL.revokeObjectURL(url)
        this.$message.success('导出成功')
      } catch (error) {
        this.$message.error('导出失败：' + (error.message || '请检查网络'))
      }
    },

    // 全屏显示
    handleFullScreen() {
      if (document.fullscreenElement) {
        document.exitFullscreen()
      } else {
        document.documentElement.requestFullscreen()
      }
    },

    // 自动刷新设置
    handleAutoRefreshSettings() {
      this.settingsDialogVisible = true
    },

    // 保存自动刷新设置
    handleSaveAutoRefreshSettings() {
      this.stopAutoRefresh()
      if (this.autoRefresh) {
        this.startAutoRefresh()
      }
      this.settingsDialogVisible = false
      this.$message.success('设置已保存')
    },

    // 分享仪表板
    handleShareDashboard() {
      const url = window.location.href
      if (navigator.clipboard) {
        navigator.clipboard.writeText(url).then(() => {
          this.$message.success('链接已复制到剪贴板')
        })
      } else {
        this.$message.info('当前链接：' + url)
      }
    },

    // 帮助
    handleHelp() {
      this.helpDialogVisible = true
    },

    // 应用配置 - 重新拉取后端数据
    handleApplyConfig() {
      this.loadDashboardStats()
      this.loadDashboardChartData()
      this.$message.success('配置已应用，数据已刷新')
    },

    // 重置仪表板
    handleResetDashboard() {
      this.dashboardForm = {
        timeRange: 'MONTH',
        organizationPath: '',
        displayMode: 'GRID',
        refreshInterval: 30
      }
      this.handleApplyConfig()
    },

    // 重置配置
    handleResetConfig() {
      this.handleResetDashboard()
    },

    // 自动刷新切换
    handleAutoRefreshChange(value) {
      if (value) {
        this.startAutoRefresh()
        this.$message.info('已开启自动刷新')
      } else {
        this.stopAutoRefresh()
        this.$message.info('已关闭自动刷新')
      }
    },

    // 实时数据切换
    handleRealTimeDataChange(value) {
      this.$message.info(value ? '已开启实时数据' : '已关闭实时数据')
    },

    // 开始自动刷新
    startAutoRefresh() {
      if (this.autoRefresh) {
        this.refreshTimer = setInterval(() => {
          this.loadDashboardChartData()
          this.updateRealTimeMetrics()
        }, this.dashboardForm.refreshInterval * 1000)
      }
    },

    // 停止自动刷新
    stopAutoRefresh() {
      if (this.refreshTimer) {
        clearInterval(this.refreshTimer)
        this.refreshTimer = null
      }
    },

    // 更新实时指标
    updateRealTimeMetrics() {
      this.realTimeMetrics.forEach(metric => {
        metric.updateTime = '刚刚'
        metric.change = parseFloat(((Math.random() - 0.5) * 10).toFixed(1))
      })
    },

    // 暂停实时数据
    handlePauseRealTime() {
      this.realTimeData = false
      this.$message.info('实时数据已暂停')
    },

    // 刷新部门排名
    refreshDepartmentRanking() {
      this.loadDashboardStats()
      this.$message.success('部门排名已刷新')
    },

    // 刷新预警监控
    refreshAlertMonitor() {
      this.loadDashboardChartData()
    },

    // 刷新资金流向
    refreshCashFlow() {
      this.loadDashboardChartData()
    },

    // 快捷操作
    handleQuickAction(action) {
      switch (action.id) {
        case 1:
          this.varianceResult = null
          this.varianceDialogVisible = true
          break
        case 2:
          this.trendDialogVisible = true
          break
        case 3:
          this.reportForm.reportName = `绩效报告_${new Date().toLocaleDateString()}`
          this.reportDialogVisible = true
          break
        case 4:
          this.alertDialogVisible = true
          break
        case 5:
          this.handleExportDashboard()
          break
        case 6:
          this.sysSettingsDialogVisible = true
          break
      }
    },

    // 执行差异分析
    async runVarianceAnalysis() {
      this.varianceLoading = true
      try {
        const response = await budgetAnalysisApi.runVarianceAnalysis(this.varianceForm)
        if (response.code === 1 && response.data && response.data.length > 0) {
          this.varianceResult = response.data
        } else {
          this.varianceResult = []
          this.$message.warning('暂无差异数据')
        }
      } catch (e) {
        this.$message.error('分析失败：' + (e.message || '请检查网络'))
      } finally {
        this.varianceLoading = false
      }
    },

    // 执行趋势分析
    async runTrendAnalysis() {
      this.trendLoading = true
      try {
        const response = await budgetAnalysisApi.getDashboardChartData(this.dashboardForm)
        if (response.code === 1 && response.data && response.data.executionTrend) {
          this.$nextTick(() => {
            const dom = document.getElementById('trendAnalysisChart')
            if (!dom) return
            let chart = echarts.getInstanceByDom(dom)
            if (chart) chart.dispose()
            chart = echarts.init(dom)
            const d = response.data
            const categories = d.categories || d.executionTrend.map(i => i.month)
            chart.setOption({
              tooltip: { trigger: 'axis' },
              legend: { data: ['预算金额', '实际执行'] },
              xAxis: { type: 'category', data: categories },
              yAxis: { type: 'value', name: '金额(万元)' },
              series: [
                { name: '预算金额', type: this.trendForm.period === 'MONTH' ? 'line' : 'bar', data: d.executionTrend.map(i => i.budget), itemStyle: { color: '#409EFF' } },
                { name: '实际执行', type: this.trendForm.period === 'MONTH' ? 'line' : 'bar', data: d.executionTrend.map(i => i.actual), itemStyle: { color: '#67C23A' } }
              ]
            })
          })
        }
      } catch (e) {
        this.$message.error('趋势分析失败：' + (e.message || '请检查网络'))
      } finally {
        this.trendLoading = false
      }
    },

    // 生成绩效报告
    async generateReport() {
      if (!this.reportForm.reportName) {
        this.$message.warning('请输入报告名称')
        return
      }
      this.reportLoading = true
      try {
        await budgetAnalysisApi.createAnalysisReport({
          reportName: this.reportForm.reportName,
          reportType: this.reportForm.reportType,
          timeRange: this.reportForm.timeRange
        })
        this.$message.success('报告生成成功，可在分析报告页面查看')
        this.reportDialogVisible = false
      } catch (e) {
        this.$message.error('生成失败：' + (e.message || '请检查网络'))
      } finally {
        this.reportLoading = false
      }
    },

    // 保存预警设置
    async saveAlertSettings() {
      try {
        const response = await budgetAnalysisApi.saveAlertSettings(this.alertForm)
        if (response.code === 1) {
          this.alertDialogVisible = false
          this.$message.success('预警规则已保存')
        } else {
          this.$message.error(response.msg || '保存失败')
        }
      } catch (e) {
        this.$message.error('保存失败：' + e.message)
      }
    },

    // 保存系统设置
    async saveSysSettings() {
      try {
        const response = await budgetAnalysisApi.saveSysSettings(this.sysSettingsForm)
        if (response.code === 1) {
          this.dashboardForm.timeRange = this.sysSettingsForm.defaultTimeRange
          this.dashboardForm.displayMode = this.sysSettingsForm.defaultDisplayMode
          this.sysSettingsDialogVisible = false
          this.$message.success('系统设置已保存')
        } else {
          this.$message.error(response.msg || '保存失败')
        }
      } catch (e) {
        this.$message.error('保存失败：' + e.message)
      }
    },

    // 自定义操作
    handleCustomizeActions() {
      this.customizeDialogVisible = true
    },

    // 获取KPI状态颜色
    getKPIStatusColor(status) {
      const colorMap = {
        'EXCELLENT': 'success',
        'GOOD': 'primary',
        'AVERAGE': 'warning',
        'POOR': 'danger'
      }
      return colorMap[status] || 'info'
    },
    
    // 获取KPI状态文本
    getKPIStatusText(status) {
      const textMap = {
        'EXCELLENT': '优秀',
        'GOOD': '良好',
        'AVERAGE': '一般',
        'POOR': '较差'
      }
      return textMap[status] || status
    },
    
    // 获取KPI进度颜色
    getKPIProgressColor(value) {
      if (value >= 90) return '#67C23A'
      if (value >= 80) return '#409EFF'
      if (value >= 70) return '#E6A23C'
      return '#F56C6C'
    },
    
    // 获取差异进度颜色
    getVarianceProgressColor(value) {
      const absValue = Math.abs(value)
      if (absValue <= 3) return '#67C23A'
      if (absValue <= 5) return '#409EFF'
      if (absValue <= 8) return '#E6A23C'
      return '#F56C6C'
    },
    
    // 获取KPI趋势图标
    getKPITrendIcon(trend) {
      const iconMap = {
        'UP': 'el-icon-arrow-up',
        'DOWN': 'el-icon-arrow-down',
        'STABLE': 'el-icon-minus'
      }
      return iconMap[trend] || 'el-icon-minus'
    },
    
    // 获取KPI趋势文本
    getKPITrendText(trend) {
      const textMap = {
        'UP': '上升',
        'DOWN': '下降',
        'STABLE': '稳定'
      }
      return textMap[trend] || trend
    },
    
    // 获取部门排名样式类
    getDepartmentRankingClass(index) {
      if (index === 0) return 'first-place'
      if (index === 1) return 'second-place'
      if (index === 2) return 'third-place'
      return 'other-place'
    },
    
    // 获取绩效等级颜色
    getPerformanceLevelColor(level) {
      const colorMap = {
        'EXCELLENT': 'success',
        'GOOD': 'primary',
        'AVERAGE': 'warning',
        'POOR': 'danger'
      }
      return colorMap[level] || 'info'
    },
    
    // 获取绩效等级文本
    getPerformanceLevelText(level) {
      const textMap = {
        'EXCELLENT': '优秀',
        'GOOD': '良好',
        'AVERAGE': '一般',
        'POOR': '较差'
      }
      return textMap[level] || level
    },
    
    // 获取指标值样式类
    getMetricValueClass(change) {
      if (change > 0) return 'metric-up'
      if (change < 0) return 'metric-down'
      return 'metric-stable'
    },
    
    // 获取指标变化图标
    getMetricChangeIcon(change) {
      if (change > 0) return 'el-icon-arrow-up'
      if (change < 0) return 'el-icon-arrow-down'
      return 'el-icon-minus'
    }
  }
}
</script>

<style lang="scss" scoped>
.analysis-dashboard {
  padding: 20px;

  .page-header {
    margin-bottom: 20px;

    h2 {
      color: #303133;
      font-size: 24px;
      margin: 0 0 8px 0;
    }

    p {
      color: #606266;
      font-size: 14px;
      margin: 0;
    }
  }

  .toolbar-card,
  .config-card,
  .realtime-card,
  .quick-actions-card {
    margin-bottom: 20px;
  }

  .stats-row {
    margin-bottom: 20px;

    .stat-card {
      border: none;
      border-radius: 8px;
      position: relative;
      overflow: hidden;

      &.kpi-card {
        background: linear-gradient(135deg, #409EFF, #66B1FF);
        color: white;
      }

      &.widgets-card {
        background: linear-gradient(135deg, #67C23A, #85CE61);
        color: white;
      }

      &.alerts-card {
        background: linear-gradient(135deg, #F56C6C, #F78989);
        color: white;
      }

      &.performance-card {
        background: linear-gradient(135deg, #E6A23C, #EEBE77);
        color: white;
      }

      .stat-content {
        position: relative;
        z-index: 2;

        .stat-number {
          font-size: 24px;
          font-weight: 600;
          margin-bottom: 4px;
        }

        .stat-label {
          font-size: 14px;
          opacity: 0.9;
          margin-bottom: 2px;
        }

        .stat-description {
          font-size: 12px;
          opacity: 0.8;
          margin-bottom: 8px;
        }

        .stat-trend {
          font-size: 12px;
          opacity: 0.9;
        }
      }

      .stat-icon {
        position: absolute;
        top: 20px;
        right: 20px;
        font-size: 48px;
        opacity: 0.3;
      }
    }
  }

  .config-card {
    .config-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 16px;

      .config-title {
        font-size: 16px;
        font-weight: 500;
        color: #303133;
      }

      .config-tools {
        display: flex;
        align-items: center;
        gap: 12px;
      }
    }
  }

  .kpi-row,
  .chart-row,
  .secondary-chart-row {
    margin-bottom: 20px;

    .kpi-card,
    .chart-card {
      .card-header {
        display: flex;
        justify-content: space-between;
        align-items: center;

        .header-tools {
          display: flex;
          align-items: center;
          gap: 10px;
        }
      }

      .chart-container {
        height: 300px;
      }
    }

    .kpi-card {
      .kpi-content {
        text-align: center;

        .kpi-value {
          font-size: 32px;
          font-weight: 600;
          color: #409EFF;
          margin-bottom: 8px;
        }

        .kpi-target {
          font-size: 12px;
          color: #909399;
          margin-bottom: 12px;
        }

        .kpi-trend {
          margin-top: 12px;
          font-size: 12px;
          color: #606266;

          i {
            margin-right: 4px;
          }
        }
      }
    }

    .ranking-list {
      max-height: 300px;
      overflow-y: auto;

      .ranking-item {
        display: flex;
        align-items: center;
        padding: 12px;
        margin-bottom: 8px;
        border-radius: 6px;
        background: #F5F7FA;
        transition: all 0.3s ease;

        &:hover {
          background: #E4E7ED;
          transform: translateX(4px);
        }

        &.first-place {
          background: linear-gradient(135deg, #FFD700, #FFA500);
          color: white;
          box-shadow: 0 4px 8px rgba(255, 215, 0, 0.3);
        }

        &.second-place {
          background: linear-gradient(135deg, #C0C0C0, #A9A9A9);
          color: white;
          box-shadow: 0 4px 8px rgba(192, 192, 192, 0.3);
        }

        &.third-place {
          background: linear-gradient(135deg, #CD7F32, #B8860B);
          color: white;
          box-shadow: 0 4px 8px rgba(205, 127, 50, 0.3);
        }

        .ranking-number {
          width: 32px;
          height: 32px;
          border-radius: 50%;
          background: rgba(255, 255, 255, 0.2);
          display: flex;
          align-items: center;
          justify-content: center;
          font-weight: 600;
          margin-right: 12px;
        }

        .ranking-content {
          flex: 1;

          .ranking-name {
            font-size: 14px;
            font-weight: 500;
            margin-bottom: 2px;
          }

          .ranking-score {
            font-size: 12px;
            opacity: 0.8;
          }
        }

        .ranking-badge {
          margin-left: 8px;
        }
      }
    }
  }

  .realtime-card {
    .realtime-content {
      .realtime-metric {
        text-align: center;
        padding: 16px;
        background: #F5F7FA;
        border-radius: 6px;

        .metric-name {
          font-size: 12px;
          color: #909399;
          margin-bottom: 8px;
        }

        .metric-value {
          font-size: 18px;
          font-weight: 600;
          margin-bottom: 4px;

          &.metric-up {
            color: #67C23A;
          }

          &.metric-down {
            color: #F56C6C;
          }

          &.metric-stable {
            color: #409EFF;
          }

          .metric-change {
            font-size: 12px;
            margin-left: 8px;
          }
        }

        .metric-time {
          font-size: 11px;
          color: #C0C4CC;
        }
      }
    }
  }

  .quick-actions-card {
    .quick-action-item {
      text-align: center;
      cursor: pointer;
      transition: all 0.3s ease;

      &:hover {
        transform: translateY(-4px);
        box-shadow: 0 8px 16px rgba(0, 0, 0, 0.1);
      }

      .action-icon {
        font-size: 32px;
        color: #409EFF;
        margin-bottom: 12px;
      }

      .action-title {
        font-size: 14px;
        font-weight: 500;
        color: #303133;
        margin-bottom: 8px;
      }

      .action-description {
        font-size: 12px;
        color: #606266;
        line-height: 1.4;
      }
    }
  }

  .text-right {
    text-align: right;
  }
}
</style>
