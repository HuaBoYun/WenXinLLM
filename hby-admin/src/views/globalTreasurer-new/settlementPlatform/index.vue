<template>
  <div class="app-container">
    <!-- 结算平台概览 -->
    <el-row :gutter="20" class="mb20">
      <el-col :span="6">
        <el-card class="overview-card">
          <div class="card-content">
            <div class="card-icon pending">
              <i class="el-icon-time"></i>
            </div>
            <div class="card-info">
              <div class="card-title">待结算数据</div>
              <div class="card-value">{{ overviewData.pendingCount || 0 }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="overview-card">
          <div class="card-content">
            <div class="card-icon processing">
              <i class="el-icon-loading"></i>
            </div>
            <div class="card-info">
              <div class="card-title">处理中</div>
              <div class="card-value">{{ overviewData.processingCount || 0 }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="overview-card">
          <div class="card-content">
            <div class="card-icon success">
              <i class="el-icon-success"></i>
            </div>
            <div class="card-info">
              <div class="card-title">已结算</div>
              <div class="card-value">{{ overviewData.settledCount || 0 }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="overview-card">
          <div class="card-content">
            <div class="card-icon failed">
              <i class="el-icon-error"></i>
            </div>
            <div class="card-info">
              <div class="card-title">失败</div>
              <div class="card-value">{{ overviewData.failedCount || 0 }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 系统状态 -->
    <el-row :gutter="20" class="mb20">
      <el-col :span="12">
        <el-card>
          <div slot="header" class="clearfix">
            <span>系统健康状态</span>
            <el-button style="float: right; padding: 3px 0" type="text" @click="refreshHealthStatus">刷新</el-button>
          </div>
          <div class="health-status">
            <div class="health-item">
              <span class="label">健康评分：</span>
              <el-progress 
                :percentage="healthStatus.avgHealthScore || 0" 
                :color="getHealthColor(healthStatus.avgHealthScore)"
                :show-text="true"
              />
            </div>
            <div class="health-item">
              <span class="label">成功率：</span>
              <span class="value">{{ (healthStatus.avgSuccessRate || 0).toFixed(2) }}%</span>
            </div>
            <div class="health-item">
              <span class="label">平均处理时间：</span>
              <span class="value">{{ (healthStatus.avgProcessingTime || 0).toFixed(0) }}ms</span>
            </div>
            <div class="health-item">
              <span class="label">系统负载：</span>
              <span class="value">{{ (healthStatus.avgSystemLoad || 0).toFixed(1) }}%</span>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <div slot="header" class="clearfix">
            <span>实时告警</span>
            <el-button style="float: right; padding: 3px 0" type="text" @click="refreshAlerts">刷新</el-button>
          </div>
          <div class="alert-list">
            <div v-if="alertList.length === 0" class="no-alert">
              <i class="el-icon-success" style="color: #67C23A;"></i>
              <span>暂无告警</span>
            </div>
            <div v-else>
              <div v-for="alert in alertList.slice(0, 5)" :key="alert.monitoringId" class="alert-item">
                <el-tag 
                  :type="getAlertType(alert.alertLevel)" 
                  size="mini"
                >{{ getAlertLevelText(alert.alertLevel) }}</el-tag>
                <span class="alert-message">{{ alert.alertMessage }}</span>
                <span class="alert-time">{{ formatTime(alert.monitorTime) }}</span>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 数据统计 -->
    <el-row :gutter="20" class="mb20">
      <el-col :span="12">
        <el-card>
          <div slot="header" class="clearfix">
            <span>今日结算统计</span>
          </div>
          <div id="todayStatsChart" style="height: 300px;"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <div slot="header" class="clearfix">
            <span>结算趋势</span>
          </div>
          <div id="trendChart" style="height: 300px;"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 待处理事项 -->
    <el-row :gutter="20">
      <el-col :span="12">
        <el-card>
          <div slot="header" class="clearfix">
            <span>高优先级待结算</span>
            <el-button style="float: right; padding: 3px 0" type="text" @click="refreshHighPriority">刷新</el-button>
          </div>
          <el-table :data="highPriorityList" style="width: 100%" max-height="300">
            <el-table-column prop="businessNo" label="业务编号" width="150" />
            <el-table-column prop="settlementAmount" label="金额" width="100">
              <template slot-scope="scope">
                <span>{{ parseFloat(scope.row.settlementAmount).toLocaleString() }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="priority" label="优先级" width="80">
              <template slot-scope="scope">
                <el-tag v-if="scope.row.priority === 'URGENT'" type="danger" size="mini">紧急</el-tag>
                <el-tag v-else type="warning" size="mini">高</el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="expectedSettlementDate" label="预期日期" width="100">
              <template slot-scope="scope">
                <span>{{ formatDate(scope.row.expectedSettlementDate) }}</span>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="80">
              <template slot-scope="scope">
                <el-button size="mini" type="text" @click="handlePendingData(scope.row)">处理</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <div slot="header" class="clearfix">
            <span>异常处理</span>
            <el-button style="float: right; padding: 3px 0" type="text" @click="refreshExceptions">刷新</el-button>
          </div>
          <el-table :data="exceptionList" style="width: 100%" max-height="300">
            <el-table-column prop="exceptionNo" label="异常编号" width="120" />
            <el-table-column prop="exceptionLevel" label="级别" width="80">
              <template slot-scope="scope">
                <el-tag 
                  :type="getAlertType(scope.row.exceptionLevel)" 
                  size="mini"
                >{{ getAlertLevelText(scope.row.exceptionLevel) }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="errorMessage" label="错误信息" show-overflow-tooltip />
            <el-table-column prop="createTime" label="发生时间" width="100">
              <template slot-scope="scope">
                <span>{{ formatTime(scope.row.createTime) }}</span>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="80">
              <template slot-scope="scope">
                <el-button size="mini" type="text" @click="handleException(scope.row)">处理</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { 
  getPendingDataSummary,
  getHighPriorityPending,
  getSystemHealthStatus,
  getAlerts,
  getCriticalExceptions
} from "@/api/globalTreasurer/jspt";
import * as echarts from 'echarts';

export default {
  name: "SettlementPlatformIndex",
  data() {
    return {
      // 概览数据
      overviewData: {},
      // 系统健康状态
      healthStatus: {},
      // 告警列表
      alertList: [],
      // 高优先级待结算列表
      highPriorityList: [],
      // 异常列表
      exceptionList: [],
      // 趋势数据
      trendData: {
        dates: [],
        successData: [],
        failedData: []
      },
      // 图表实例
      todayStatsChart: null,
      trendChart: null
    };
  },
  created() {
    this.loadData();
  },
  mounted() {
    this.$nextTick(() => {
      this.initCharts();
    });
  },
  methods: {
    /** 加载数据 */
    loadData() {
      this.loadOverviewData();
      this.refreshHealthStatus();
      this.refreshAlerts();
      this.refreshHighPriority();
      this.refreshExceptions();
    },
    /**
     * 将达梦数据库返回的大写 key Map 转换为小写驼峰
     * 例如 PENDINGCOUNT -> pendingCount
     */
    normalizeMapKeys(raw, keyMap) {
      if (!raw) return {};
      const result = {};
      Object.keys(keyMap).forEach(camelKey => {
        const upperKey = camelKey.toUpperCase();
        // 优先取小写驼峰（mock/其他DB），其次取全大写（达梦）
        result[camelKey] = raw[camelKey] !== undefined ? raw[camelKey] : (raw[upperKey] !== undefined ? raw[upperKey] : 0);
      });
      return result;
    },
    /** 加载概览数据 */
    loadOverviewData() {
      getPendingDataSummary().then(response => {
        const raw = response.data || {};
        this.overviewData = this.normalizeMapKeys(raw, {
          pendingCount: 0, processingCount: 0, settledCount: 0, failedCount: 0,
          totalCount: 0, pendingAmount: 0, processingAmount: 0, settledAmount: 0,
          failedAmount: 0, totalAmount: 0
        });
        this.updateTodayStatsChart();
        this.updateTrendChart();
      }).catch(error => {
        console.warn('获取概览数据失败:', error);
        this.overviewData = { pendingCount: 0, processingCount: 0, settledCount: 0, failedCount: 0 };
        this.updateTodayStatsChart();
        this.updateTrendChart();
      });
    },
    /** 刷新健康状态 */
    refreshHealthStatus() {
      getSystemHealthStatus().then(response => {
        const raw = response.data || {};
        this.healthStatus = this.normalizeMapKeys(raw, {
          avgHealthScore: 100, avgSuccessRate: 100, avgProcessingTime: 0, avgSystemLoad: 0,
          totalMonitors: 0, totalTransactions: 0, totalSuccess: 0, totalFailed: 0
        });
      }).catch(error => {
        console.warn('获取健康状态失败:', error);
        this.healthStatus = {
          avgHealthScore: 100,
          avgSuccessRate: 100,
          avgProcessingTime: 0,
          avgSystemLoad: 0
        };
      });
    },
    /** 刷新告警 */
    refreshAlerts() {
      getAlerts().then(response => {
        this.alertList = Array.isArray(response.data) ? response.data : [];
      }).catch(error => {
        console.warn('获取告警信息失败:', error);
        this.alertList = [];
      });
    },
    /** 刷新高优先级待结算 */
    refreshHighPriority() {
      getHighPriorityPending().then(response => {
        const data = response.data;
        this.highPriorityList = Array.isArray(data) ? data.slice(0, 10) : [];
      }).catch(error => {
        console.warn('获取高优先级数据失败:', error);
        this.highPriorityList = [];
      });
    },
    /** 刷新异常 */
    refreshExceptions() {
      getCriticalExceptions().then(response => {
        const data = response.data;
        this.exceptionList = Array.isArray(data) ? data.slice(0, 10) : [];
      }).catch(error => {
        console.warn('获取异常信息失败:', error);
        this.exceptionList = [];
      });
    },
    /** 初始化图表 */
    initCharts() {
      this.todayStatsChart = echarts.init(document.getElementById('todayStatsChart'));
      this.trendChart = echarts.init(document.getElementById('trendChart'));
      this.updateTodayStatsChart();
      this.updateTrendChart();
    },
    /** 更新今日统计饼图 */
    updateTodayStatsChart() {
      if (!this.todayStatsChart) return;
      const option = {
        tooltip: { trigger: 'item' },
        legend: { orient: 'vertical', left: 'left' },
        series: [{
          name: '结算状态',
          type: 'pie',
          radius: '50%',
          data: [
            { value: this.overviewData.pendingCount || 0, name: '待结算' },
            { value: this.overviewData.processingCount || 0, name: '处理中' },
            { value: this.overviewData.settledCount || 0, name: '已结算' },
            { value: this.overviewData.failedCount || 0, name: '失败' }
          ],
          emphasis: {
            itemStyle: {
              shadowBlur: 10,
              shadowOffsetX: 0,
              shadowColor: 'rgba(0, 0, 0, 0.5)'
            }
          }
        }]
      };
      this.todayStatsChart.setOption(option);
    },
    /** 更新结算趋势图（用概览数据构造柱状图） */
    updateTrendChart() {
      if (!this.trendChart) return;
      const option = {
        tooltip: { trigger: 'axis' },
        legend: { data: ['待结算', '处理中', '已结算', '失败'] },
        xAxis: {
          type: 'category',
          data: ['当前状态']
        },
        yAxis: { type: 'value' },
        series: [
          { name: '待结算', type: 'bar', data: [this.overviewData.pendingCount || 0], itemStyle: { color: '#E6A23C' } },
          { name: '处理中', type: 'bar', data: [this.overviewData.processingCount || 0], itemStyle: { color: '#409EFF' } },
          { name: '已结算', type: 'bar', data: [this.overviewData.settledCount || 0], itemStyle: { color: '#67C23A' } },
          { name: '失败', type: 'bar', data: [this.overviewData.failedCount || 0], itemStyle: { color: '#F56C6C' } }
        ]
      };
      this.trendChart.setOption(option);
    },
    /** 获取健康评分颜色 */
    getHealthColor(score) {
      if (score >= 90) return '#67C23A';
      if (score >= 70) return '#E6A23C';
      if (score >= 50) return '#F56C6C';
      return '#909399';
    },
    /** 获取告警类型 */
    getAlertType(level) {
      switch (level) {
        case 'CRITICAL': return 'danger';
        case 'HIGH': return 'warning';
        case 'MEDIUM': return 'info';
        case 'LOW': return 'success';
        default: return 'info';
      }
    },
    /** 获取告警级别文本 */
    getAlertLevelText(level) {
      switch (level) {
        case 'CRITICAL': return '严重';
        case 'HIGH': return '高';
        case 'MEDIUM': return '中';
        case 'LOW': return '低';
        default: return '未知';
      }
    },
    /** 格式化时间 */
    formatTime(time) {
      if (!time) return '-';
      return this.parseTime(time, '{m}-{d} {h}:{i}');
    },
    /** 格式化日期 */
    formatDate(date) {
      if (!date) return '-';
      return this.parseTime(date, '{m}-{d}');
    },
    /** 处理待结算数据 */
    handlePendingData(row) {
      this.$router.push({
        path: '/globalTreasurer/jspt/djssjgl',
        query: { id: row.pendingId }
      });
    },
    /** 处理异常 */
    handleException(row) {
      this.$router.push({
        path: '/globalTreasurer/jspt/jsycgl',
        query: { id: row.exceptionId }
      });
    }
  }
};
</script>

<style scoped>
.overview-card {
  margin-bottom: 20px;
}

.card-content {
  display: flex;
  align-items: center;
  padding: 20px;
}

.card-icon {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 20px;
  font-size: 24px;
  color: white;
}

.card-icon.pending {
  background-color: #E6A23C;
}

.card-icon.processing {
  background-color: #409EFF;
}

.card-icon.success {
  background-color: #67C23A;
}

.card-icon.failed {
  background-color: #F56C6C;
}

.card-info {
  flex: 1;
}

.card-title {
  font-size: 14px;
  color: #909399;
  margin-bottom: 8px;
}

.card-value {
  font-size: 28px;
  font-weight: bold;
  color: #303133;
}

.health-status {
  padding: 10px 0;
}

.health-item {
  display: flex;
  align-items: center;
  margin-bottom: 15px;
}

.health-item .label {
  width: 120px;
  color: #606266;
}

.health-item .value {
  font-weight: bold;
  color: #303133;
}

.alert-list {
  max-height: 200px;
  overflow-y: auto;
}

.no-alert {
  text-align: center;
  padding: 40px 0;
  color: #909399;
}

.no-alert i {
  font-size: 32px;
  display: block;
  margin-bottom: 10px;
}

.alert-item {
  display: flex;
  align-items: center;
  padding: 8px 0;
  border-bottom: 1px solid #f0f0f0;
}

.alert-item:last-child {
  border-bottom: none;
}

.alert-message {
  flex: 1;
  margin: 0 10px;
  font-size: 13px;
  color: #606266;
}

.alert-time {
  font-size: 12px;
  color: #909399;
}

.mb20 {
  margin-bottom: 20px;
}
</style>
