<template>
  <div class="integration-monitor">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>集成监控中心</h2>
      <p>实时监控所有系统集成状态、性能指标和异常告警</p>
    </div>

    <!-- 操作工具栏 -->
    <el-card class="toolbar-card" shadow="never">
      <el-row :gutter="16">
        <el-col :span="12">
          <el-button-group>
            <el-button type="primary" icon="el-icon-refresh" @click="handleRefresh">刷新监控</el-button>
            <el-button type="success" icon="el-icon-view" @click="handleRealTimeView">实时视图</el-button>
            <el-button type="warning" icon="el-icon-warning" @click="handleAlerts">告警管理</el-button>
            <el-button type="info" icon="el-icon-data-analysis" @click="handleAnalytics">性能分析</el-button>
          </el-button-group>
        </el-col>
        <el-col :span="12" class="text-right">
          <el-button-group>
            <el-button icon="el-icon-setting" @click="handleSettings">监控设置</el-button>
            <el-button icon="el-icon-download" @click="handleExport">导出报告</el-button>
            <el-button icon="el-icon-help" @click="handleHelp">帮助</el-button>
          </el-button-group>
        </el-col>
      </el-row>
    </el-card>

    <!-- 监控概览统计 -->
    <el-row :gutter="20" class="stats-row">
      <el-col :span="6">
        <el-card class="stat-card total-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ monitorStats.totalIntegrations }}</div>
            <div class="stat-label">集成总数</div>
            <div class="stat-description">监控的集成数量</div>
            <div class="stat-trend">
              <i class="el-icon-connection"></i>
              <span>系统集成</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-connection"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card healthy-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ monitorStats.healthyIntegrations }}</div>
            <div class="stat-label">健康集成</div>
            <div class="stat-description">运行正常的集成</div>
            <div class="stat-trend">
              <i class="el-icon-success"></i>
              <span>运行正常</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-success"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card alerts-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ monitorStats.activeAlerts }}</div>
            <div class="stat-label">活跃告警</div>
            <div class="stat-description">当前未处理告警</div>
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
        <el-card class="stat-card availability-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ monitorStats.avgAvailability }}%</div>
            <div class="stat-label">平均可用性</div>
            <div class="stat-description">系统可用性指标</div>
            <div class="stat-trend">
              <i class="el-icon-odometer"></i>
              <span>高可用</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-odometer"></i>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 监控类型选择 -->
    <el-card class="monitor-types-card" shadow="never">
      <div slot="header" class="card-header">
        <span>监控类型</span>
        <el-button icon="el-icon-refresh" size="mini" @click="refreshMonitorTypes">刷新</el-button>
      </div>
      <el-row :gutter="16">
        <el-col :span="6" v-for="monitorType in monitorTypes" :key="monitorType.id">
          <el-card 
            class="monitor-type-item" 
            shadow="hover" 
            @click.native="handleSelectMonitorType(monitorType)"
            :class="{ 'selected': selectedMonitorType === monitorType.id }"
          >
            <div class="monitor-type-icon">
              <i :class="monitorType.icon"></i>
            </div>
            <div class="monitor-type-title">{{ monitorType.name }}</div>
            <div class="monitor-type-description">{{ monitorType.description }}</div>
            <div class="monitor-type-stats">
              <span class="integration-count">{{ monitorType.integrationCount }} 个集成</span>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </el-card>

    <!-- 实时监控面板 -->
    <el-row :gutter="20">
      <el-col :span="16">
        <!-- 集成状态监控 -->
        <el-card class="integration-status-card" shadow="never">
          <div slot="header" class="card-header">
            <span>集成状态监控</span>
            <div class="header-tools">
              <el-select v-model="selectedTimeRange" size="mini" style="width: 120px; margin-right: 10px;">
                <el-option value="1h" label="最近1小时" />
                <el-option value="6h" label="最近6小时" />
                <el-option value="24h" label="最近24小时" />
                <el-option value="7d" label="最近7天" />
              </el-select>
              <el-button icon="el-icon-refresh" size="mini" @click="getIntegrationStatus">刷新</el-button>
            </div>
          </div>
          
          <el-table
            :data="displayStatusList"
            border
            stripe
            highlight-current-row
            v-loading="loading"
            @row-click="handleRowClick"
          >
            <el-table-column type="index" label="序号" width="60" align="center" />
            <el-table-column prop="integrationName" label="集成名称" width="200" show-overflow-tooltip>
              <template slot-scope="scope">
                <el-link type="primary" @click="handleViewDetails(scope.row)">
                  {{ scope.row.integrationName }}
                </el-link>
              </template>
            </el-table-column>
            <el-table-column prop="integrationType" label="集成类型" width="120" align="center">
              <template slot-scope="scope">
                <el-tag :type="getIntegrationTypeColor(scope.row.integrationType)" size="mini">
                  {{ getIntegrationTypeText(scope.row.integrationType) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="status" label="运行状态" width="100" align="center">
              <template slot-scope="scope">
                <el-tag :type="getStatusColor(scope.row.status)" size="mini">
                  {{ getStatusText(scope.row.status) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="availability" label="可用性" width="100" align="center">
              <template slot-scope="scope">
                <span :class="getAvailabilityClass(scope.row.availability)">
                  {{ scope.row.availability }}%
                </span>
              </template>
            </el-table-column>
            <el-table-column prop="responseTime" label="响应时间" width="100" align="center">
              <template slot-scope="scope">
                <span :class="getResponseTimeClass(scope.row.responseTime)">
                  {{ scope.row.responseTime }}ms
                </span>
              </template>
            </el-table-column>
            <el-table-column prop="throughput" label="吞吐量" width="100" align="center">
              <template slot-scope="scope">
                <span class="throughput">{{ scope.row.throughput }}/s</span>
              </template>
            </el-table-column>
            <el-table-column prop="errorRate" label="错误率" width="100" align="center">
              <template slot-scope="scope">
                <span :class="getErrorRateClass(scope.row.errorRate)">
                  {{ scope.row.errorRate }}%
                </span>
              </template>
            </el-table-column>
            <el-table-column prop="lastCheck" label="最后检查" width="150" align="center">
              <template slot-scope="scope">
                <span class="last-check">{{ scope.row.lastCheck }}</span>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="150" align="center" fixed="right">
              <template slot-scope="scope">
                <el-button
                  type="text"
                  size="mini"
                  icon="el-icon-view"
                  @click="handleViewDetails(scope.row)"
                >详情</el-button>
                <el-button
                  type="text"
                  size="mini"
                  icon="el-icon-monitor"
                  @click="handleMonitorDetail(scope.row)"
                >监控</el-button>
                <el-dropdown @command="(command) => handleMoreAction(command, scope.row)">
                  <el-button type="text" size="mini">
                    更多<i class="el-icon-arrow-down el-icon--right"></i>
                  </el-button>
                  <el-dropdown-menu slot="dropdown">
                    <el-dropdown-item command="restart">重启</el-dropdown-item>
                    <el-dropdown-item command="logs">日志</el-dropdown-item>
                    <el-dropdown-item command="alerts">告警</el-dropdown-item>
                    <el-dropdown-item command="config">配置</el-dropdown-item>
                  </el-dropdown-menu>
                </el-dropdown>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
      
      <el-col :span="8">
        <!-- 告警信息 -->
        <el-card class="alerts-card" shadow="never">
          <div slot="header" class="card-header">
            <span>实时告警</span>
            <el-badge :value="alertList.length" class="alert-badge">
              <el-button icon="el-icon-bell" size="mini" @click="handleViewAllAlerts">查看全部</el-button>
            </el-badge>
          </div>
          
          <div class="alerts-list">
            <div v-for="alert in alertList" :key="alert.id" class="alert-item" :class="'alert-' + alert.level">
              <div class="alert-header">
                <el-tag :type="getAlertLevelColor(alert.level)" size="mini">
                  {{ getAlertLevelText(alert.level) }}
                </el-tag>
                <span class="alert-time">{{ alert.alertTime }}</span>
              </div>
              <div class="alert-title">{{ alert.title }}</div>
              <div class="alert-message">{{ alert.message }}</div>
              <div class="alert-actions">
                <el-button type="text" size="mini" @click="handleAcknowledgeAlert(alert)">
                  确认
                </el-button>
                <el-button type="text" size="mini" @click="handleViewAlert(alert)">
                  详情
                </el-button>
              </div>
            </div>
          </div>
        </el-card>

        <!-- 性能指标 -->
        <el-card class="performance-card" shadow="never" style="margin-top: 20px;">
          <div slot="header" class="card-header">
            <span>性能指标</span>
            <el-button icon="el-icon-refresh" size="mini" @click="getPerformanceMetrics">刷新</el-button>
          </div>
          
          <div class="performance-metrics">
            <div class="metric-item">
              <div class="metric-label">平均响应时间</div>
              <div class="metric-value">{{ performanceMetrics.avgResponseTime }}ms</div>
              <div class="metric-trend" :class="performanceMetrics.responseTimeTrend">
                <i :class="getTrendIcon(performanceMetrics.responseTimeTrend)"></i>
                {{ performanceMetrics.responseTimeChange }}%
              </div>
            </div>
            <div class="metric-item">
              <div class="metric-label">总吞吐量</div>
              <div class="metric-value">{{ performanceMetrics.totalThroughput }}/s</div>
              <div class="metric-trend" :class="performanceMetrics.throughputTrend">
                <i :class="getTrendIcon(performanceMetrics.throughputTrend)"></i>
                {{ performanceMetrics.throughputChange }}%
              </div>
            </div>
            <div class="metric-item">
              <div class="metric-label">错误率</div>
              <div class="metric-value">{{ performanceMetrics.errorRate }}%</div>
              <div class="metric-trend" :class="performanceMetrics.errorRateTrend">
                <i :class="getTrendIcon(performanceMetrics.errorRateTrend)"></i>
                {{ performanceMetrics.errorRateChange }}%
              </div>
            </div>
            <div class="metric-item">
              <div class="metric-label">系统可用性</div>
              <div class="metric-value">{{ performanceMetrics.availability }}%</div>
              <div class="metric-trend" :class="performanceMetrics.availabilityTrend">
                <i :class="getTrendIcon(performanceMetrics.availabilityTrend)"></i>
                {{ performanceMetrics.availabilityChange }}%
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 监控详情抽屉 -->
    <el-drawer
      title="集成监控详情"
      :visible.sync="detailDrawerVisible"
      direction="rtl"
      size="80%"
    >
      <div class="detail-content" v-if="currentIntegration" v-loading="detailLoading">
        <el-tabs v-model="detailActiveTab" type="card">
          <el-tab-pane label="实时监控" name="realtime">
            <el-row :gutter="20">
              <el-col :span="12">
                <el-card class="monitor-chart-card">
                  <div slot="header">响应时间趋势</div>
                  <div class="chart-container">
                    <el-table :data="detailData.responseTimeTrend || []" border size="mini" max-height="200">
                      <el-table-column prop="time" label="时间" width="160">
                        <template slot-scope="scope">{{ formatTime(scope.row.time) }}</template>
                      </el-table-column>
                      <el-table-column prop="value" label="响应时间(ms)" align="center" />
                    </el-table>
                  </div>
                </el-card>
              </el-col>
              <el-col :span="12">
                <el-card class="monitor-chart-card">
                  <div slot="header">性能概览</div>
                  <div class="chart-container">
                    <el-descriptions :column="1" border size="mini">
                      <el-descriptions-item label="平均响应时间">{{ detailData.avgResponseTime || 0 }}ms</el-descriptions-item>
                      <el-descriptions-item label="最大响应时间">{{ detailData.maxResponseTime || 0 }}ms</el-descriptions-item>
                      <el-descriptions-item label="平均吞吐量">{{ detailData.avgThroughput || 0 }}/s</el-descriptions-item>
                      <el-descriptions-item label="错误率">{{ detailData.errorRate || 0 }}%</el-descriptions-item>
                      <el-descriptions-item label="可用性">{{ detailData.availability || 0 }}%</el-descriptions-item>
                    </el-descriptions>
                  </div>
                </el-card>
              </el-col>
            </el-row>
            <el-row :gutter="20" style="margin-top: 20px;">
              <el-col :span="24">
                <el-card class="monitor-chart-card">
                  <div slot="header">执行统计</div>
                  <el-descriptions :column="4" border size="mini">
                    <el-descriptions-item label="总请求数">{{ detailData.totalRequests || 0 }}</el-descriptions-item>
                    <el-descriptions-item label="成功请求数">{{ detailData.successRequests || 0 }}</el-descriptions-item>
                    <el-descriptions-item label="执行状态">{{ getStatusText(detailData.status) }}</el-descriptions-item>
                    <el-descriptions-item label="重试次数">{{ detailData.retryCount || 0 }}</el-descriptions-item>
                  </el-descriptions>
                </el-card>
              </el-col>
            </el-row>
          </el-tab-pane>
          <el-tab-pane label="告警历史" name="alerts">
            <el-table :data="integrationAlerts" border size="mini" v-loading="alertsLoading">
              <el-table-column prop="alertTime" label="告警时间" width="160">
                <template slot-scope="scope">{{ formatTime(scope.row.alertTime) }}</template>
              </el-table-column>
              <el-table-column prop="level" label="告警级别" width="100" align="center">
                <template slot-scope="scope">
                  <el-tag :type="getAlertLevelColor(scope.row.level)" size="mini">
                    {{ getAlertLevelText(scope.row.level) }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="title" label="告警标题" width="200" show-overflow-tooltip />
              <el-table-column prop="message" label="告警信息" show-overflow-tooltip />
              <el-table-column prop="status" label="状态" width="100" align="center">
                <template slot-scope="scope">
                  <el-tag :type="getAlertStatusColor(scope.row.status)" size="mini">
                    {{ getAlertStatusText(scope.row.status) }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="acknowledgedBy" label="确认人" width="120" />
              <el-table-column prop="resolvedTime" label="解决时间" width="160">
                <template slot-scope="scope">{{ formatTime(scope.row.resolvedTime) }}</template>
              </el-table-column>
            </el-table>
          </el-tab-pane>
          <el-tab-pane label="性能报告" name="performance">
            <el-descriptions title="性能指标汇总" :column="2" border>
              <el-descriptions-item label="平均响应时间">{{ detailData.avgResponseTime || 0 }}ms</el-descriptions-item>
              <el-descriptions-item label="最大响应时间">{{ detailData.maxResponseTime || 0 }}ms</el-descriptions-item>
              <el-descriptions-item label="平均吞吐量">{{ detailData.avgThroughput || 0 }}/s</el-descriptions-item>
              <el-descriptions-item label="峰值吞吐量">{{ detailData.maxThroughput || 0 }}/s</el-descriptions-item>
              <el-descriptions-item label="错误率">{{ detailData.errorRate || 0 }}%</el-descriptions-item>
              <el-descriptions-item label="可用性">{{ detailData.availability || 0 }}%</el-descriptions-item>
              <el-descriptions-item label="总请求数">{{ detailData.totalRequests || 0 }}</el-descriptions-item>
              <el-descriptions-item label="成功请求数">{{ detailData.successRequests || 0 }}</el-descriptions-item>
            </el-descriptions>
          </el-tab-pane>
          <el-tab-pane label="配置信息" name="config">
            <el-form label-width="120px" size="small" v-loading="configLoading">
              <el-form-item label="集成名称">
                <el-input :value="configData.integrationName || ''" readonly />
              </el-form-item>
              <el-form-item label="集成类型">
                <el-input :value="getIntegrationTypeText(configData.integrationType)" readonly />
              </el-form-item>
              <el-form-item label="执行类型">
                <el-input :value="configData.executionType || ''" readonly />
              </el-form-item>
              <el-form-item label="监控间隔">
                <el-input :value="(configData.monitorInterval || 30) + '秒'" readonly />
              </el-form-item>
              <el-form-item label="告警阈值">
                <el-input :value="configData.alertThreshold || ''" readonly />
              </el-form-item>
              <el-form-item label="重试次数">
                <el-input :value="String(configData.retryCount != null ? configData.retryCount : 3)" readonly />
              </el-form-item>
              <el-form-item label="超时设置">
                <el-input :value="(configData.timeout || 30000) + 'ms'" readonly />
              </el-form-item>
              <el-form-item label="备注">
                <el-input :value="configData.remark || ''" readonly />
              </el-form-item>
            </el-form>
          </el-tab-pane>
        </el-tabs>
      </div>
    </el-drawer>

    <!-- 实时视图弹窗 -->
    <el-dialog title="实时视图" :visible.sync="realTimeDialogVisible" width="80%">
      <el-table :data="integrationStatusList" border stripe size="mini" v-loading="loading">
        <el-table-column type="index" label="序号" width="60" align="center" />
        <el-table-column prop="integrationName" label="集成名称" width="180" show-overflow-tooltip />
        <el-table-column prop="integrationType" label="类型" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getIntegrationTypeColor(scope.row.integrationType)" size="mini">{{ getIntegrationTypeText(scope.row.integrationType) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getStatusColor(scope.row.status)" size="mini">{{ getStatusText(scope.row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="availability" label="可用性" width="100" align="center">
          <template slot-scope="scope">{{ scope.row.availability }}%</template>
        </el-table-column>
        <el-table-column prop="responseTime" label="响应时间" width="100" align="center">
          <template slot-scope="scope">{{ scope.row.responseTime }}ms</template>
        </el-table-column>
        <el-table-column prop="errorRate" label="错误率" width="100" align="center">
          <template slot-scope="scope">{{ scope.row.errorRate }}%</template>
        </el-table-column>
        <el-table-column prop="lastCheck" label="最后检查" width="160">
          <template slot-scope="scope">{{ formatTime(scope.row.lastCheck) }}</template>
        </el-table-column>
      </el-table>
    </el-dialog>

    <!-- 告警管理弹窗 -->
    <el-dialog title="告警管理" :visible.sync="alertsDialogVisible" width="80%">
      <el-table :data="allAlertList" border stripe size="mini" v-loading="allAlertsLoading">
        <el-table-column type="index" label="序号" width="60" align="center" />
        <el-table-column prop="alertTime" label="告警时间" width="160">
          <template slot-scope="scope">{{ formatTime(scope.row.alertTime) }}</template>
        </el-table-column>
        <el-table-column prop="level" label="级别" width="80" align="center">
          <template slot-scope="scope">
            <el-tag :type="getAlertLevelColor(scope.row.level)" size="mini">{{ getAlertLevelText(scope.row.level) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="integrationName" label="集成名称" width="160" show-overflow-tooltip />
        <el-table-column prop="title" label="告警标题" width="200" show-overflow-tooltip />
        <el-table-column prop="message" label="告警信息" show-overflow-tooltip />
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getAlertStatusColor(scope.row.status)" size="mini">{{ getAlertStatusText(scope.row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="120" align="center">
          <template slot-scope="scope">
            <el-button v-if="scope.row.status === 'pending'" type="text" size="mini" @click="handleAcknowledgeAlert(scope.row)">确认</el-button>
            <el-button type="text" size="mini" @click="handleViewAlertDetail(scope.row)">详情</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        style="margin-top: 16px; text-align: right;"
        :current-page="alertPagination.current"
        :page-size="alertPagination.size"
        :total="alertPagination.total"
        layout="total, prev, pager, next"
        @current-change="handleAlertPageChange"
      />
    </el-dialog>

    <!-- 性能分析弹窗 -->
    <el-dialog title="性能分析" :visible.sync="analyticsDialogVisible" width="70%">
      <el-descriptions title="性能指标概览" :column="2" border>
        <el-descriptions-item label="平均响应时间">{{ performanceMetrics.avgResponseTime }}ms</el-descriptions-item>
        <el-descriptions-item label="总吞吐量">{{ performanceMetrics.totalThroughput }}/s</el-descriptions-item>
        <el-descriptions-item label="错误率">{{ performanceMetrics.errorRate }}%</el-descriptions-item>
        <el-descriptions-item label="系统可用性">{{ performanceMetrics.availability }}%</el-descriptions-item>
      </el-descriptions>
      <el-divider />
      <h4>各集成性能对比</h4>
      <el-table :data="integrationStatusList" border stripe size="mini">
        <el-table-column prop="integrationName" label="集成名称" width="180" show-overflow-tooltip />
        <el-table-column prop="responseTime" label="响应时间(ms)" width="120" align="center" sortable />
        <el-table-column prop="throughput" label="吞吐量(/s)" width="120" align="center" sortable />
        <el-table-column prop="errorRate" label="错误率(%)" width="120" align="center" sortable />
        <el-table-column prop="availability" label="可用性(%)" width="120" align="center" sortable />
      </el-table>
    </el-dialog>

    <!-- 监控设置弹窗 -->
    <el-dialog title="监控设置" :visible.sync="settingsDialogVisible" width="600px">
      <el-form :model="monitorSettingsForm" label-width="140px" size="small" v-loading="settingsLoading">
        <el-form-item label="刷新间隔(秒)">
          <el-input-number v-model="monitorSettingsForm.refreshInterval" :min="10" :max="300" />
        </el-form-item>
        <el-form-item label="错误率告警阈值(%)">
          <el-input-number v-model="monitorSettingsForm.alertThresholdErrorRate" :min="1" :max="100" />
        </el-form-item>
        <el-form-item label="响应时间阈值(ms)">
          <el-input-number v-model="monitorSettingsForm.alertThresholdResponseTime" :min="100" :max="60000" />
        </el-form-item>
        <el-form-item label="可用性阈值(%)">
          <el-input-number v-model="monitorSettingsForm.alertThresholdAvailability" :min="50" :max="100" />
        </el-form-item>
        <el-form-item label="数据保留天数">
          <el-input-number v-model="monitorSettingsForm.retentionDays" :min="7" :max="365" />
        </el-form-item>
        <el-form-item label="邮件告警">
          <el-switch v-model="monitorSettingsForm.enableEmailAlert" />
        </el-form-item>
        <el-form-item label="短信告警">
          <el-switch v-model="monitorSettingsForm.enableSmsAlert" />
        </el-form-item>
        <el-form-item label="告警接收人">
          <el-input v-model="monitorSettingsForm.alertRecipients" />
        </el-form-item>
      </el-form>
      <span slot="footer">
        <el-button @click="settingsDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveMonitorSettings" :loading="settingsSaving">保存</el-button>
      </span>
    </el-dialog>

    <!-- 导出报告弹窗 -->
    <el-dialog title="导出监控报告" :visible.sync="exportDialogVisible" width="500px">
      <p>将导出当前所有监控数据的完整报告，包含集成状态、告警信息和性能指标。</p>
      <span slot="footer">
        <el-button @click="exportDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="doExportReport" :loading="exportLoading">确认导出</el-button>
      </span>
    </el-dialog>

    <!-- 日志弹窗 -->
    <el-dialog title="集成日志" :visible.sync="logsDialogVisible" width="80%">
      <el-table :data="integrationLogs" border stripe size="mini" v-loading="logsLoading">
        <el-table-column prop="time" label="时间" width="160">
          <template slot-scope="scope">{{ formatTime(scope.row.time) }}</template>
        </el-table-column>
        <el-table-column prop="level" label="级别" width="80" align="center">
          <template slot-scope="scope">
            <el-tag :type="scope.row.level === 'ERROR' ? 'danger' : 'info'" size="mini">{{ scope.row.level }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="message" label="日志信息" show-overflow-tooltip />
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getStatusColor(scope.row.status)" size="mini">{{ getStatusText(scope.row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="duration" label="耗时(ms)" width="100" align="center" />
        <el-table-column prop="errorMessage" label="错误信息" width="200" show-overflow-tooltip />
      </el-table>
    </el-dialog>

    <!-- 告警详情弹窗 -->
    <el-dialog title="告警详情" :visible.sync="alertDetailDialogVisible" width="600px">
      <el-descriptions :column="1" border v-if="currentAlertDetail">
        <el-descriptions-item label="告警标题">{{ currentAlertDetail.title }}</el-descriptions-item>
        <el-descriptions-item label="告警级别">
          <el-tag :type="getAlertLevelColor(currentAlertDetail.level)" size="mini">{{ getAlertLevelText(currentAlertDetail.level) }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="告警时间">{{ formatTime(currentAlertDetail.alertTime) }}</el-descriptions-item>
        <el-descriptions-item label="告警状态">
          <el-tag :type="getAlertStatusColor(currentAlertDetail.status)" size="mini">{{ getAlertStatusText(currentAlertDetail.status) }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="集成名称">{{ currentAlertDetail.integrationName || '' }}</el-descriptions-item>
        <el-descriptions-item label="告警信息">{{ currentAlertDetail.message }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script>
import { systemIntegrationApi, integrationMonitorApi } from '@/api/managementAccountant/ncv65/systemIntegration'

export default {
  name: 'IntegrationMonitor',
  data() {
    return {
      // 统计数据
      monitorStats: {
        totalIntegrations: 0,
        healthyIntegrations: 0,
        activeAlerts: 0,
        avgAvailability: 0
      },

      // 监控类型
      monitorTypes: [],
      selectedMonitorType: null,
      selectedTimeRange: '24h',

      // 集成状态列表
      integrationStatusList: [],
      filteredStatusList: [],
      loading: false,

      // 告警列表
      alertList: [],

      // 性能指标
      performanceMetrics: {
        avgResponseTime: 0,
        responseTimeTrend: 'stable',
        responseTimeChange: 0,
        totalThroughput: 0,
        throughputTrend: 'stable',
        throughputChange: 0,
        errorRate: 0,
        errorRateTrend: 'stable',
        errorRateChange: 0,
        availability: 0,
        availabilityTrend: 'stable',
        availabilityChange: 0
      },

      // 详情抽屉
      detailDrawerVisible: false,
      detailActiveTab: 'realtime',
      currentIntegration: null,
      integrationAlerts: [],
      detailData: {},
      configData: {},
      detailLoading: false,
      alertsLoading: false,
      configLoading: false,

      // 弹窗控制
      realTimeDialogVisible: false,
      alertsDialogVisible: false,
      analyticsDialogVisible: false,
      settingsDialogVisible: false,
      exportDialogVisible: false,
      logsDialogVisible: false,
      alertDetailDialogVisible: false,

      // 告警管理
      allAlertList: [],
      allAlertsLoading: false,
      alertPagination: { current: 1, size: 20, total: 0 },
      currentAlertDetail: null,

      // 监控设置
      monitorSettingsForm: {
        refreshInterval: 30,
        alertThresholdErrorRate: 5,
        alertThresholdResponseTime: 5000,
        alertThresholdAvailability: 95,
        retentionDays: 30,
        enableEmailAlert: false,
        enableSmsAlert: false,
        alertRecipients: ''
      },
      settingsLoading: false,
      settingsSaving: false,

      // 导出
      exportLoading: false,

      // 日志
      integrationLogs: [],
      logsLoading: false
    }
  },

  computed: {
    // 根据选中的监控类型过滤列表
    displayStatusList() {
      if (!this.selectedMonitorType) {
        return this.integrationStatusList
      }
      return this.integrationStatusList.filter(item => item.integrationType === this.selectedMonitorType)
    }
  },

  created() {
    this.getIntegrationStatus()
    this.getMonitorStats()
    this.getAlertList()
    this.getPerformanceMetrics()
    this.getMonitorTypes()

    // 设置定时刷新
    this.refreshTimer = setInterval(() => {
      this.getIntegrationStatus()
      this.getAlertList()
      this.getPerformanceMetrics()
    }, 30000)
  },

  beforeDestroy() {
    if (this.refreshTimer) {
      clearInterval(this.refreshTimer)
    }
  },

  methods: {
    // 格式化时间
    formatTime(time) {
      if (!time) return '-'
      const d = new Date(time)
      if (isNaN(d.getTime())) return time
      const pad = (n) => String(n).padStart(2, '0')
      return d.getFullYear() + '-' + pad(d.getMonth() + 1) + '-' + pad(d.getDate()) + ' ' + pad(d.getHours()) + ':' + pad(d.getMinutes()) + ':' + pad(d.getSeconds())
    },

    async getIntegrationStatus() {
      this.loading = true
      try {
        const response = await systemIntegrationApi.getIntegrationStatus({
          timeRange: this.selectedTimeRange
        })
        if (response.code === 1 && response.data) {
          this.integrationStatusList = response.data
        }
      } catch (error) {
        this.$message.error('获取集成状态失败：' + error.message)
      } finally {
        this.loading = false
      }
    },
    async getMonitorStats() {
      try {
        const response = await systemIntegrationApi.getMonitorStats()
        if (response.code === 1 && response.data) { this.monitorStats = response.data }
      } catch (error) { console.error('获取监控统计数据失败：', error) }
    },
    async getAlertList() {
      try {
        const response = await systemIntegrationApi.getAlertList()
        if (response.code === 1 && response.data) {
          const records = response.data.records || response.data
          this.alertList = Array.isArray(records) ? records : []
        }
      } catch (error) { console.error('获取告警列表失败：', error) }
    },
    async getPerformanceMetrics() {
      try {
        const response = await systemIntegrationApi.getPerformanceMetrics()
        if (response.code === 1 && response.data) { this.performanceMetrics = response.data }
      } catch (error) { console.error('获取性能指标失败：', error) }
    },
    async getMonitorTypes() {
      try {
        const response = await systemIntegrationApi.getIntegrationStatus({ timeRange: this.selectedTimeRange })
        if (response.code === 1 && response.data) {
          const typeMap = {}
          response.data.forEach(item => {
            const t = item.integrationType || 'OTHER'
            if (!typeMap[t]) { typeMap[t] = { id: t, name: this.getIntegrationTypeText(t), description: t + '集成监控', icon: 'el-icon-monitor', integrationCount: 0 } }
            typeMap[t].integrationCount++
          })
          this.monitorTypes = Object.values(typeMap)
        }
      } catch (error) { console.error('获取监控类型失败：', error) }
    },
    refreshMonitorTypes() { this.getMonitorTypes(); this.$message.success('监控类型已刷新') },
    handleSelectMonitorType(monitorType) {
      // 切换选中状态，computed displayStatusList 自动过滤
      this.selectedMonitorType = this.selectedMonitorType === monitorType.id ? null : monitorType.id
    },

    // 行点击事件 - 不自动打开详情抽屉
    handleRowClick() {
      // 不做任何操作，避免误触
    },

    // 查看详情 - 加载完整数据
    async handleViewDetails(row) {
      this.currentIntegration = row
      this.detailDrawerVisible = true
      this.detailActiveTab = 'realtime'
      await this.loadDetailData(row)
    },

    // 监控详情 - 打开抽屉并加载实时数据
    async handleMonitorDetail(row) {
      this.currentIntegration = row
      this.detailDrawerVisible = true
      this.detailActiveTab = 'realtime'
      await this.loadDetailData(row)
    },

    // 加载详情数据
    async loadDetailData(row) {
      this.detailLoading = true
      try {
        const id = row.integrationId || row.id
        const response = await integrationMonitorApi.getDetail(id)
        if (response.code === 1 && response.data) {
          this.detailData = response.data
        }
      } catch (error) {
        this.$message.error('获取详情失败：' + error.message)
      } finally {
        this.detailLoading = false
      }
      // 同时加载告警历史
      this.loadAlertHistory(row)
      // 同时加载配置
      this.loadConfigData(row)
    },

    // 加载告警历史
    async loadAlertHistory(row) {
      this.alertsLoading = true
      try {
        const id = row.integrationId || row.id
        const response = await integrationMonitorApi.getAlerts(id)
        if (response.code === 1 && response.data) {
          this.integrationAlerts = Array.isArray(response.data) ? response.data : []
        }
      } catch (error) {
        console.error('获取告警历史失败：', error)
      } finally {
        this.alertsLoading = false
      }
    },

    // 加载配置信息
    async loadConfigData(row) {
      this.configLoading = true
      try {
        const id = row.integrationId || row.id
        const response = await integrationMonitorApi.getConfig(id)
        if (response.code === 1 && response.data) {
          this.configData = response.data
        }
      } catch (error) {
        console.error('获取配置信息失败：', error)
      } finally {
        this.configLoading = false
      }
    },

    // 更多操作
    handleMoreAction(command, row) {
      const actions = {
        'restart': async () => {
          try {
            await this.$confirm(`确认重启集成: ${row.integrationName}？`, '确认重启', { type: 'warning' })
            const id = row.integrationId || row.id
            const response = await integrationMonitorApi.restart(id)
            if (response.code === 1) {
              this.$message.success('已重启集成: ' + row.integrationName)
              this.getIntegrationStatus()
            } else {
              this.$message.error(response.msg || '重启集成失败')
            }
          } catch (error) {
            if (error !== 'cancel') { this.$message.error('重启集成失败：' + (error.message || '')) }
          }
        },
        'logs': () => { this.loadIntegrationLogs(row) },
        'alerts': () => {
          this.currentIntegration = row
          this.detailDrawerVisible = true
          this.detailActiveTab = 'alerts'
          this.loadAlertHistory(row)
        },
        'config': () => {
          this.currentIntegration = row
          this.detailDrawerVisible = true
          this.detailActiveTab = 'config'
          this.loadConfigData(row)
        }
      }
      if (actions[command]) {
        actions[command]()
      }
    },

    // 加载集成日志
    async loadIntegrationLogs(row) {
      this.logsDialogVisible = true
      this.logsLoading = true
      try {
        const id = row.integrationId || row.id
        const response = await integrationMonitorApi.getLogs(id)
        if (response.code === 1 && response.data) {
          this.integrationLogs = Array.isArray(response.data) ? response.data : []
        }
      } catch (error) {
        this.$message.error('获取日志失败：' + error.message)
      } finally {
        this.logsLoading = false
      }
    },

    // 刷新数据
    handleRefresh() {
      this.getIntegrationStatus()
      this.getMonitorStats()
      this.getAlertList()
      this.getPerformanceMetrics()
      this.$message.success('数据已刷新')
    },

    // 实时视图 - 打开弹窗
    handleRealTimeView() {
      this.getIntegrationStatus()
      this.realTimeDialogVisible = true
    },

    // 告警管理 - 打开弹窗
    handleAlerts() {
      this.loadAllAlerts()
      this.alertsDialogVisible = true
    },

    // 加载全部告警（分页）
    async loadAllAlerts() {
      this.allAlertsLoading = true
      try {
        const response = await integrationMonitorApi.getAllAlerts({
          current: this.alertPagination.current,
          size: this.alertPagination.size
        })
        if (response.code === 1 && response.data) {
          const data = response.data
          this.allAlertList = data.records || data.list || (Array.isArray(data) ? data : [])
          this.alertPagination.total = data.total || this.allAlertList.length
        }
      } catch (error) {
        this.$message.error('获取告警列表失败：' + error.message)
      } finally {
        this.allAlertsLoading = false
      }
    },

    // 告警分页切换
    handleAlertPageChange(page) {
      this.alertPagination.current = page
      this.loadAllAlerts()
    },

    // 性能分析 - 打开弹窗
    handleAnalytics() {
      this.getPerformanceMetrics()
      this.analyticsDialogVisible = true
    },

    // 监控设置 - 打开弹窗
    handleSettings() {
      this.loadMonitorSettings()
      this.settingsDialogVisible = true
    },

    // 加载监控设置
    async loadMonitorSettings() {
      this.settingsLoading = true
      try {
        const response = await integrationMonitorApi.getSettings()
        if (response.code === 1 && response.data) {
          Object.assign(this.monitorSettingsForm, response.data)
        }
      } catch (error) {
        console.error('获取监控设置失败：', error)
      } finally {
        this.settingsLoading = false
      }
    },

    // 保存监控设置
    async saveMonitorSettings() {
      this.settingsSaving = true
      try {
        const response = await integrationMonitorApi.saveSettings(this.monitorSettingsForm)
        if (response.code === 1) {
          this.$message.success('监控设置已保存')
          this.settingsDialogVisible = false
        } else {
          this.$message.error(response.msg || '保存设置失败')
        }
      } catch (error) {
        this.$message.error('保存设置失败：' + error.message)
      } finally {
        this.settingsSaving = false
      }
    },

    // 导出报告 - 打开弹窗
    handleExport() {
      this.exportDialogVisible = true
    },

    // 执行导出
    async doExportReport() {
      this.exportLoading = true
      try {
        const response = await integrationMonitorApi.exportReport({
          timeRange: this.selectedTimeRange
        })
        if (response.code === 1 && response.data) {
          // 后端返回导出数据，前端生成文件下载
          const dataStr = JSON.stringify(response.data, null, 2)
          const blob = new Blob([dataStr], { type: 'application/json' })
          const url = URL.createObjectURL(blob)
          const a = document.createElement('a')
          a.href = url
          a.download = '集成监控报告_' + new Date().toISOString().slice(0, 10) + '.json'
          a.click()
          URL.revokeObjectURL(url)
          this.$message.success('监控报告导出成功')
          this.exportDialogVisible = false
        } else {
          this.$message.error(response.msg || '导出失败')
        }
      } catch (error) {
        this.$message.error('导出失败：' + error.message)
      } finally {
        this.exportLoading = false
      }
    },

    // 帮助
    handleHelp() {
      this.$alert('集成监控中心：实时监控所有集成连接的运行状态、性能指标、告警信息和执行日志。\n\n功能说明：\n- 刷新监控：刷新所有监控数据\n- 实时视图：查看所有集成的实时状态\n- 告警管理：管理和确认系统告警\n- 性能分析：查看性能指标和对比\n- 监控设置：配置告警阈值和通知\n- 导出报告：导出监控数据报告', '帮助', { confirmButtonText: '知道了' })
    },

    // 查看全部告警 - 打开告警管理弹窗
    handleViewAllAlerts() {
      this.loadAllAlerts()
      this.alertsDialogVisible = true
    },

    // 确认告警
    async handleAcknowledgeAlert(alert) {
      try {
        await this.$confirm('确认处理告警: ' + alert.title + '？', '确认告警', { type: 'warning' })
        const response = await integrationMonitorApi.acknowledgeAlert(alert.id)
        if (response.code === 1) {
          this.$message.success('已确认告警: ' + alert.title)
          this.getAlertList()
          // 如果告警管理弹窗打开，也刷新
          if (this.alertsDialogVisible) { this.loadAllAlerts() }
        } else {
          this.$message.error(response.msg || '确认告警失败')
        }
      } catch (error) {
        if (error !== 'cancel') { this.$message.error('确认告警失败：' + (error.message || '')) }
      }
    },

    // 查看告警详情（侧边栏中的）
    handleViewAlert(alert) {
      this.currentAlertDetail = alert
      this.alertDetailDialogVisible = true
    },

    // 查看告警详情（告警管理弹窗中的）
    handleViewAlertDetail(alert) {
      this.currentAlertDetail = alert
      this.alertDetailDialogVisible = true
    },

    // ===== 辅助方法 =====

    // 获取告警级别颜色
    getAlertLevelColor(level) {
      const colorMap = {
        'critical': 'danger', 'CRITICAL': 'danger',
        'high': 'warning', 'HIGH': 'warning',
        'medium': 'warning', 'MEDIUM': 'warning',
        'low': 'info', 'LOW': 'info'
      }
      return colorMap[level] || 'info'
    },

    // 获取告警级别文本
    getAlertLevelText(level) {
      const textMap = {
        'critical': '严重', 'CRITICAL': '严重',
        'high': '高', 'HIGH': '高',
        'medium': '中', 'MEDIUM': '中',
        'low': '低', 'LOW': '低'
      }
      return textMap[level] || level || '未知'
    },

    // 获取告警状态颜色
    getAlertStatusColor(status) {
      const colorMap = {
        'pending': 'warning', 'PENDING': 'warning',
        'acknowledged': 'info', 'ACKNOWLEDGED': 'info',
        'resolved': 'success', 'RESOLVED': 'success'
      }
      return colorMap[status] || 'info'
    },

    // 获取告警状态文本
    getAlertStatusText(status) {
      const textMap = {
        'pending': '待处理', 'PENDING': '待处理',
        'acknowledged': '已确认', 'ACKNOWLEDGED': '已确认',
        'resolved': '已解决', 'RESOLVED': '已解决'
      }
      return textMap[status] || status || '未知'
    },

    // 获取集成类型颜色
    getIntegrationTypeColor(type) {
      const colorMap = {
        'ERP': 'primary',
        'API': 'success',
        'DATABASE': 'warning',
        'CLOUD': 'danger'
      }
      return colorMap[type] || 'info'
    },

    // 获取集成类型文本
    getIntegrationTypeText(type) {
      const textMap = {
        'ERP': 'ERP集成',
        'API': 'API集成',
        'DATABASE': '数据库集成',
        'CLOUD': '云平台集成'
      }
      return textMap[type] || type || '未知'
    },

    // 获取状态颜色 - 支持大写和小写
    getStatusColor(status) {
      const colorMap = {
        'running': 'success', 'RUNNING': 'success',
        'SUCCESS': 'success', 'success': 'success',
        'stopped': 'danger', 'STOPPED': 'danger',
        'FAILURE': 'danger', 'failure': 'danger',
        'error': 'danger', 'ERROR': 'danger',
        'warning': 'warning', 'WARNING': 'warning',
        'PARTIAL': 'warning', 'partial': 'warning',
        'TIMEOUT': 'danger', 'timeout': 'danger'
      }
      return colorMap[status] || 'info'
    },

    // 获取状态文本 - 支持大写和小写
    getStatusText(status) {
      const textMap = {
        'running': '运行中', 'RUNNING': '运行中',
        'SUCCESS': '成功', 'success': '成功',
        'stopped': '已停止', 'STOPPED': '已停止',
        'FAILURE': '失败', 'failure': '失败',
        'error': '错误', 'ERROR': '错误',
        'warning': '警告', 'WARNING': '警告',
        'PARTIAL': '部分成功', 'partial': '部分成功',
        'TIMEOUT': '超时', 'timeout': '超时'
      }
      return textMap[status] || status || '未知'
    },

    // 获取可用性样式类
    getAvailabilityClass(availability) {
      if (availability >= 99) return 'high-availability'
      if (availability >= 95) return 'medium-availability'
      return 'low-availability'
    },

    // 获取响应时间样式类
    getResponseTimeClass(responseTime) {
      if (responseTime <= 100) return 'fast-response'
      if (responseTime <= 500) return 'medium-response'
      return 'slow-response'
    },

    // 获取错误率样式类
    getErrorRateClass(errorRate) {
      if (errorRate <= 1) return 'low-error'
      if (errorRate <= 5) return 'medium-error'
      return 'high-error'
    },

    // 获取趋势图标
    getTrendIcon(trend) {
      const iconMap = {
        'up': 'el-icon-arrow-up',
        'down': 'el-icon-arrow-down',
        'stable': 'el-icon-minus'
      }
      return iconMap[trend] || 'el-icon-minus'
    }
  }
}
</script>

<style lang="scss" scoped>
.integration-monitor {
  padding: 20px;
  background-color: #f0f2f5;
  min-height: calc(100vh - 84px);

  .page-header {
    margin-bottom: 20px;
    background: white;
    padding: 20px;
    border-radius: 4px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);

    h2 {
      color: #303133;
      font-size: 24px;
      margin: 0 0 8px 0;
      font-weight: 600;
    }

    p {
      color: #606266;
      font-size: 14px;
      margin: 0;
      line-height: 1.5;
    }
  }

  .toolbar-card {
    margin-bottom: 20px;
    border: none;

    .text-right {
      text-align: right;
    }
  }

  .stats-row {
    margin-bottom: 20px;

    .stat-card {
      border: none;
      border-radius: 8px;
      position: relative;
      overflow: hidden;
      transition: all 0.3s;

      &:hover {
        transform: translateY(-4px);
        box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
      }

      .stat-content {
        position: relative;
        z-index: 2;

        .stat-number {
          font-size: 32px;
          font-weight: 700;
          margin-bottom: 8px;
        }

        .stat-label {
          font-size: 14px;
          color: #606266;
          margin-bottom: 4px;
        }

        .stat-description {
          font-size: 12px;
          color: #909399;
          margin-bottom: 8px;
        }

        .stat-trend {
          font-size: 12px;
          display: flex;
          align-items: center;
          gap: 4px;

          i {
            font-size: 14px;
          }
        }
      }

      .stat-icon {
        position: absolute;
        right: 20px;
        top: 50%;
        transform: translateY(-50%);
        font-size: 64px;
        opacity: 0.15;
        z-index: 1;
      }

      &.total-card {
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        color: white;

        .stat-label,
        .stat-description,
        .stat-trend {
          color: rgba(255, 255, 255, 0.9);
        }
      }

      &.healthy-card {
        background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
        color: white;

        .stat-label,
        .stat-description,
        .stat-trend {
          color: rgba(255, 255, 255, 0.9);
        }
      }

      &.warning-card {
        background: linear-gradient(135deg, #fa709a 0%, #fee140 100%);
        color: white;

        .stat-label,
        .stat-description,
        .stat-trend {
          color: rgba(255, 255, 255, 0.9);
        }
      }

      &.error-card {
        background: linear-gradient(135deg, #ff6b6b 0%, #ee5a6f 100%);
        color: white;

        .stat-label,
        .stat-description,
        .stat-trend {
          color: rgba(255, 255, 255, 0.9);
        }
      }
    }
  }

  // 可用性样式类
  .high-availability {
    color: #67C23A;
    font-weight: 600;
  }

  .medium-availability {
    color: #E6A23C;
    font-weight: 500;
  }

  .low-availability {
    color: #F56C6C;
    font-weight: 500;
  }

  // 响应时间样式类
  .fast-response {
    color: #67C23A;
  }

  .medium-response {
    color: #E6A23C;
  }

  .slow-response {
    color: #F56C6C;
  }

  // 错误率样式类
  .low-error {
    color: #67C23A;
  }

  .medium-error {
    color: #E6A23C;
  }

  .high-error {
    color: #F56C6C;
  }
}
</style>
