<template>
  <div class="app-container">
    <!-- 系统健康状态概览 -->
    <el-row :gutter="20" class="mb20">
      <el-col :span="6">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>系统健康评分</span>
          </div>
          <div class="health-score">
            <el-progress
              type="circle"
              :percentage="healthStatus.avgHealthScore || 0"
              :color="getHealthColor(healthStatus.avgHealthScore)"
              :width="120"
            >
              <span class="score-text">{{ (healthStatus.avgHealthScore || 0).toFixed(2) }}</span>
            </el-progress>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>成功率</span>
          </div>
          <div class="metric-value">
            <span class="value">{{ (healthStatus.avgSuccessRate || 0).toFixed(2) }}%</span>
            <i class="el-icon-success" style="color: #67C23A;"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>平均处理时间</span>
          </div>
          <div class="metric-value">
            <span class="value">{{ (healthStatus.avgProcessingTime || 0).toFixed(0) }}ms</span>
            <i class="el-icon-time" style="color: #E6A23C;"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>告警数量</span>
          </div>
          <div class="metric-value">
            <span class="value critical">{{ (alertSummary.criticalAlertCount || 0) + (alertSummary.highAlertCount || 0) }}</span>
            <i class="el-icon-warning" style="color: #F56C6C;"></i>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 监控仪表板 -->
    <el-row :gutter="20" class="mb20">
      <el-col :span="12">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>监控类型分布</span>
            <el-button style="float: right; padding: 3px 0" type="text" @click="refreshDashboard">刷新</el-button>
          </div>
          <div id="monitorTypeChart" style="height: 300px;"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>性能趋势</span>
          </div>
          <div id="performanceTrendChart" style="height: 300px;"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 告警列表 -->
    <el-card class="box-card mb20">
      <div slot="header" class="clearfix">
        <span>实时告警</span>
        <el-button style="float: right; padding: 3px 0" type="text" @click="loadAlerts">刷新</el-button>
      </div>
      <el-table :data="alertList" style="width: 100%" max-height="300">
        <el-table-column label="告警级别" width="100">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.alertLevel === 'CRITICAL'" type="danger">严重</el-tag>
            <el-tag v-else-if="scope.row.alertLevel === 'HIGH'" type="warning">高</el-tag>
            <el-tag v-else-if="scope.row.alertLevel === 'MEDIUM'" type="info">中</el-tag>
            <el-tag v-else type="success">低</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="monitorType" label="监控类型" width="120" />
        <el-table-column prop="monitorName" label="监控名称" width="150" />
        <el-table-column prop="alertMessage" label="告警信息" show-overflow-tooltip />
        <el-table-column prop="monitorTime" label="发生时间" width="160">
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.monitorTime) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="100">
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="text"
              @click="handleSendAlert(scope.row)"
              v-if="!scope.row.isAlertSent"
            >发送告警</el-button>
            <span v-else style="color: #67C23A;">已发送</span>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 查询条件 -->
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="监控类型" prop="monitorType">
        <el-select v-model="queryParams.monitorType" placeholder="请选择监控类型" clearable>
          <el-option label="系统监控" value="SYSTEM" />
          <el-option label="接口监控" value="INTERFACE" />
          <el-option label="业务监控" value="BUSINESS" />
          <el-option label="性能监控" value="PERFORMANCE" />
        </el-select>
      </el-form-item>
      <el-form-item label="告警级别" prop="alertLevel">
        <el-select v-model="queryParams.alertLevel" placeholder="请选择告警级别" clearable>
          <el-option label="严重" value="CRITICAL" />
          <el-option label="高" value="HIGH" />
          <el-option label="中" value="MEDIUM" />
          <el-option label="低" value="LOW" />
        </el-select>
      </el-form-item>
      <el-form-item label="监控时间">
        <el-date-picker
          v-model="dateRange"
          style="width: 240px"
          value-format="yyyy-MM-dd"
          type="daterange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
        ></el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 操作按钮 -->
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
        >新增监控</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleEdit"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-message"
          size="mini"
          :disabled="multiple"
          @click="handleBatchSendAlert"
        >批量发送告警</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="info"
          plain
          icon="el-icon-upload2"
          size="mini"
          @click="handleExport"
        >导出</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          icon="el-icon-search"
          size="mini"
          @click="showSearch = !showSearch"
        >{{ showSearch ? '隐藏搜索' : '显示搜索' }}</el-button>
      </el-col>
    </el-row>

    <!-- 数据表格 -->
    <el-table v-loading="loading" :data="monitoringList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="监控ID" align="center" prop="monitoringId" width="80" />
      <el-table-column label="监控类型" align="center" prop="monitorType" width="100">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.monitorType === 'SYSTEM'" type="primary">系统监控</el-tag>
          <el-tag v-else-if="scope.row.monitorType === 'INTERFACE'" type="success">接口监控</el-tag>
          <el-tag v-else-if="scope.row.monitorType === 'BUSINESS'" type="warning">业务监控</el-tag>
          <el-tag v-else-if="scope.row.monitorType === 'PERFORMANCE'" type="info">性能监控</el-tag>
          <span v-else>{{ scope.row.monitorType }}</span>
        </template>
      </el-table-column>
      <el-table-column label="监控名称" align="center" prop="monitorName" width="150" />
      <el-table-column label="监控时间" align="center" prop="monitorTime" width="160">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.monitorTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="交易数量" align="center" prop="transactionCount" width="100" />
      <el-table-column label="成功数量" align="center" prop="successCount" width="100" />
      <el-table-column label="失败数量" align="center" prop="failedCount" width="100" />
      <el-table-column label="成功率" align="center" prop="successRate" width="100">
        <template slot-scope="scope">
          <span>{{ (scope.row.successRate || 0).toFixed(2) }}%</span>
        </template>
      </el-table-column>
      <el-table-column label="平均处理时间" align="center" prop="avgProcessingTime" width="120">
        <template slot-scope="scope">
          <span>{{ scope.row.avgProcessingTime }}ms</span>
        </template>
      </el-table-column>
      <el-table-column label="健康评分" align="center" prop="healthScore" width="100">
        <template slot-scope="scope">
          <el-tag :type="getHealthScoreType(scope.row.healthScore)">{{ scope.row.healthScore }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="告警级别" align="center" prop="alertLevel" width="100">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.alertLevel === 'CRITICAL'" type="danger">严重</el-tag>
          <el-tag v-else-if="scope.row.alertLevel === 'HIGH'" type="warning">高</el-tag>
          <el-tag v-else-if="scope.row.alertLevel === 'MEDIUM'" type="info">中</el-tag>
          <el-tag v-else-if="scope.row.alertLevel === 'LOW'" type="success">低</el-tag>
          <span v-else>-</span>
        </template>
      </el-table-column>
      <el-table-column label="告警状态" align="center" prop="isAlertSent" width="100">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.isAlertSent" type="success">已发送</el-tag>
          <el-tag v-else-if="scope.row.alertLevel" type="warning">未发送</el-tag>
          <span v-else>-</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="150">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handleView(scope.row)"
          >详情</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-message"
            @click="handleSendAlert(scope.row)"
            v-if="scope.row.alertLevel && !scope.row.isAlertSent"
          >发送告警</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <el-pagination
      v-show="total > 0"
      :total="total"
      :page-size.sync="queryParams.pageSize"
      :current-page.sync="queryParams.pageNum"
      layout="total, sizes, prev, pager, next, jumper"
      @size-change="getList"
      @current-change="getList"
      style="margin-top: 20px; text-align: right;"
    />

    <!-- 添加监控数据对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="监控类型" prop="monitorType">
          <el-select v-model="form.monitorType" placeholder="请选择监控类型">
            <el-option label="系统监控" value="SYSTEM" />
            <el-option label="接口监控" value="INTERFACE" />
            <el-option label="业务监控" value="BUSINESS" />
            <el-option label="性能监控" value="PERFORMANCE" />
          </el-select>
        </el-form-item>
        <el-form-item label="监控名称" prop="monitorName">
          <el-input v-model="form.monitorName" placeholder="请输入监控名称" />
        </el-form-item>
        <el-form-item label="监控时间" prop="monitorTime">
          <el-date-picker
            v-model="form.monitorTime"
            type="datetime"
            placeholder="请选择监控时间"
            value-format="yyyy-MM-dd HH:mm:ss"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="交易数量" prop="transactionCount">
          <el-input-number v-model="form.transactionCount" :min="0" />
        </el-form-item>
        <el-form-item label="成功数量" prop="successCount">
          <el-input-number v-model="form.successCount" :min="0" />
        </el-form-item>
        <el-form-item label="失败数量" prop="failedCount">
          <el-input-number v-model="form.failedCount" :min="0" />
        </el-form-item>
        <el-form-item label="平均处理时间" prop="avgProcessingTime">
          <el-input-number v-model="form.avgProcessingTime" :min="0" />
        </el-form-item>
        <el-form-item label="健康评分" prop="healthScore">
          <el-input-number v-model="form.healthScore" :min="0" :max="100" :precision="2" placeholder="留空则自动计算" />
          <span style="color:#909399; font-size:12px; margin-left:8px;">范围 0-100，留空将由系统自动计算</span>
        </el-form-item>
        <el-form-item label="告警级别" prop="alertLevel">
          <el-select v-model="form.alertLevel" placeholder="请选择告警级别" clearable>
            <el-option label="严重" value="CRITICAL" />
            <el-option label="高" value="HIGH" />
            <el-option label="中" value="MEDIUM" />
            <el-option label="低" value="LOW" />
          </el-select>
        </el-form-item>
        <el-form-item label="告警信息" prop="alertMessage">
          <el-input v-model="form.alertMessage" type="textarea" placeholder="请输入告警信息" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 详情对话框 -->
    <el-dialog title="监控数据详情" :visible.sync="detailOpen" width="800px" append-to-body>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="监控ID">{{ detailData.monitoringId }}</el-descriptions-item>
        <el-descriptions-item label="监控类型">{{ detailData.monitorType }}</el-descriptions-item>
        <el-descriptions-item label="监控名称">{{ detailData.monitorName }}</el-descriptions-item>
        <el-descriptions-item label="监控时间">{{ detailData.monitorTime }}</el-descriptions-item>
        <el-descriptions-item label="交易数量">{{ detailData.transactionCount }}</el-descriptions-item>
        <el-descriptions-item label="成功数量">{{ detailData.successCount }}</el-descriptions-item>
        <el-descriptions-item label="失败数量">{{ detailData.failedCount }}</el-descriptions-item>
        <el-descriptions-item label="成功率">{{ (detailData.successRate || 0).toFixed(2) }}%</el-descriptions-item>
        <el-descriptions-item label="平均处理时间">{{ detailData.avgProcessingTime }}ms</el-descriptions-item>
        <el-descriptions-item label="最大处理时间">{{ detailData.maxProcessingTime }}ms</el-descriptions-item>
        <el-descriptions-item label="最小处理时间">{{ detailData.minProcessingTime }}ms</el-descriptions-item>
        <el-descriptions-item label="总金额">{{ detailData.totalAmount }}</el-descriptions-item>
        <el-descriptions-item label="健康评分">{{ detailData.healthScore }}</el-descriptions-item>
        <el-descriptions-item label="系统负载">{{ detailData.systemLoad }}%</el-descriptions-item>
        <el-descriptions-item label="内存使用率">{{ detailData.memoryUsage }}%</el-descriptions-item>
        <el-descriptions-item label="CPU使用率">{{ detailData.cpuUsage }}%</el-descriptions-item>
        <el-descriptions-item label="磁盘使用率">{{ detailData.diskUsage }}%</el-descriptions-item>
        <el-descriptions-item label="网络延迟">{{ detailData.networkLatency }}ms</el-descriptions-item>
        <el-descriptions-item label="告警级别">{{ detailData.alertLevel }}</el-descriptions-item>
        <el-descriptions-item label="告警状态">{{ detailData.isAlertSent ? '已发送' : '未发送' }}</el-descriptions-item>
        <el-descriptions-item label="告警发送时间">{{ detailData.alertSentTime }}</el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ detailData.createTime }}</el-descriptions-item>
        <el-descriptions-item label="更新时间">{{ detailData.updateTime }}</el-descriptions-item>
        <el-descriptions-item label="告警信息" :span="2">{{ detailData.alertMessage }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script>
import {
  getMonitoringPage,
  getMonitoringById,
  addMonitoring,
  updateMonitoring,
  deleteMonitoring,
  getSystemHealthStatus,
  getDashboardData,
  getAlerts,
  sendAlert,
  exportPendingData
} from "@/api/globalTreasurer/jspt";
import * as echarts from 'echarts';

export default {
  name: "SettlementMonitoring",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 监控数据表格数据
      monitoringList: [],
      // 告警列表
      alertList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 详情弹出层
      detailOpen: false,
      // 日期范围
      dateRange: [],
      // 系统健康状态
      healthStatus: {},
      // 告警汇总（来自 getDashboardData 接口）
      alertSummary: {},
      // 仪表板数据
      dashboardData: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        monitorType: null,
        alertLevel: null
      },
      // 表单参数
      form: {},
      // 详情数据
      detailData: {},
      // 表单校验
      rules: {
        monitorType: [
          { required: true, message: "监控类型不能为空", trigger: "change" }
        ],
        monitorName: [
          { required: true, message: "监控名称不能为空", trigger: "blur" }
        ]
      }
    };
  },
  created() {
    this.getList();
  },
  mounted() {
    this.$nextTick(() => {
      // 确保 DOM 挂载后再初始化图表，然后请求数据
      this.initCharts();
      this.getHealthStatus();
      this.getDashboard();
      this.loadAlerts();
    });
  },
  methods: {
    /** 查询监控数据列表 */
    getList() {
      this.loading = true;
      try {
        const params = this.addDateRange({ ...this.queryParams }, this.dateRange);
        getMonitoringPage(params).then(response => {
          this.monitoringList = (response.data && response.data.rows) ? response.data.rows : [];
          this.total = (response.data && response.data.total) ? response.data.total : 0;
          this.loading = false;
          // 列表数据更新后重新构造图表数据
          this.dashboardData = this.buildChartDataFromList(this.monitoringList);
          this.updateCharts();
        }).catch(error => {
          console.error('获取监控数据列表失败:', error);
          this.monitoringList = [];
          this.total = 0;
          this.loading = false;
        });
      } catch (error) {
        console.error('getList方法异常:', error);
        this.loading = false;
      }
    },
    /** 从 map 中按 key 做大小写不敏感查找，找不到返回 undefined */
    _getVal(raw, ...keys) {
      // 先按原始 key 查找
      for (const k of keys) {
        if (raw[k] !== undefined && raw[k] !== null) return raw[k];
      }
      // 再做大小写不敏感兜底（达梦/MyBatis 可能返回全大写或全小写）
      const lowerMap = {};
      for (const k of Object.keys(raw)) {
        lowerMap[k.toLowerCase()] = raw[k];
      }
      for (const k of keys) {
        const v = lowerMap[k.toLowerCase()];
        if (v !== undefined && v !== null) return v;
      }
      return undefined;
    },
    /** 将后端字段映射为驼峰字段，兼容驼峰/全大写/全小写等各种情况 */
    normalizeHealthData(raw) {
      if (!raw || typeof raw !== 'object') return {};
      const g = (def, ...keys) => {
        const v = this._getVal(raw, ...keys);
        return v !== undefined ? v : def;
      };
      return {
        avgHealthScore:     parseFloat(g(0, 'avgHealthScore',    'AVGHEALTHSCORE',    'avg_health_score')),
        avgSuccessRate:     parseFloat(g(0, 'avgSuccessRate',    'AVGSUCCESSRATE',    'avg_success_rate')),
        avgProcessingTime:  parseFloat(g(0, 'avgProcessingTime', 'AVGPROCESSINGTIME', 'avg_processing_time')),
        avgSystemLoad:      parseFloat(g(0, 'avgSystemLoad',     'AVGSYSTEMLOAD',     'avg_system_load')),
        totalMonitors:      parseInt(g(0,   'totalMonitors',     'TOTALMONITORS',     'total_monitors')),
        totalTransactions:  parseInt(g(0,   'totalTransactions', 'TOTALTRANSACTIONS', 'total_transactions')),
        totalSuccess:       parseInt(g(0,   'totalSuccess',      'TOTALSUCCESS',      'total_success')),
        totalFailed:        parseInt(g(0,   'totalFailed',       'TOTALFAILED',       'total_failed')),
        criticalAlertCount: parseInt(g(0,   'criticalAlertCount','CRITICALALERTS',    'criticalAlerts',    'critical_alert_count')),
        highAlertCount:     parseInt(g(0,   'highAlertCount',    'HIGHALERTS',        'highAlerts',        'high_alert_count')),
        mediumAlertCount:   parseInt(g(0,   'mediumAlertCount',  'MEDIUMALERTS',      'mediumAlerts',      'medium_alert_count')),
        lowAlertCount:      parseInt(g(0,   'lowAlertCount',     'LOWALERTS',         'lowAlerts',         'low_alert_count')),
        totalAlerts:        parseInt(g(0,   'totalAlerts',       'TOTALALERTS',       'total_alerts')),
      };
    },
    /** 获取系统健康状态 */
    getHealthStatus() {
      const orgId = this.getOrgId();
      getSystemHealthStatus(orgId).then(response => {
        // axios 拦截器已返回完整 { code, msg, data }，response.data 即为健康数据 Map
        // 兼容 response.data 直接是数据 或 response.data.data 再嵌套一层的情况
        const payload = response && response.data ? response.data : response;
        const raw = (payload && payload.data && typeof payload.data === 'object' && !Array.isArray(payload.data))
          ? payload.data
          : payload;
        console.debug('[jsjk] health-status raw keys:', raw ? Object.keys(raw) : null, raw);
        this.healthStatus = this.normalizeHealthData(raw);
      }).catch(error => {
        console.error('获取系统健康状态失败:', error);
        this.healthStatus = {};
      });
    },
    /** 获取仪表板数据 */
    getDashboard() {
      const orgId = this.getOrgId();
      getDashboardData(orgId).then(response => {
        const data = response.data;
        if (Array.isArray(data)) {
          // 返回列表数组，直接用于图表
          this.dashboardData = data;
          this.updateCharts();
        } else if (data && typeof data === 'object') {
          // 返回汇总对象（含 CRITICALALERTS 等字段），存到 alertSummary
          this.alertSummary = this.normalizeHealthData(data);
          // 图表用 monitoringList 按类型分组构造
          this.dashboardData = this.buildChartDataFromList(this.monitoringList);
          this.updateCharts();
        } else {
          this.dashboardData = [];
          this.updateCharts();
        }
      }).catch(error => {
        console.error('获取仪表板数据失败:', error);
        this.dashboardData = [];
        this.updateCharts();
      });
    },
    /** 用 monitoringList 按监控类型分组，构造图表数据 */
    buildChartDataFromList(list) {
      if (!list || !list.length) return [];
      const map = {};
      list.forEach(item => {
        const type = item.monitorType || '未知';
        if (!map[type]) {
          map[type] = { monitorType: type, totalTransactions: 0, totalSuccess: 0, totalFailed: 0, avgProcessingTime: 0, _count: 0 };
        }
        map[type].totalTransactions += (item.transactionCount || 0);
        map[type].totalSuccess     += (item.successCount || 0);
        map[type].totalFailed      += (item.failedCount || 0);
        map[type].avgProcessingTime += (item.avgProcessingTime || 0);
        map[type]._count++;
      });
      return Object.values(map).map(g => ({
        monitorType: g.monitorType,
        totalTransactions: g.totalTransactions,
        totalSuccess: g.totalSuccess,
        totalFailed: g.totalFailed,
        avgProcessingTime: g._count > 0 ? parseFloat((g.avgProcessingTime / g._count).toFixed(2)) : 0
      }));
    },
    /** 获取 orgId，兼容多种来源 */
    getOrgId() {
      try {
        const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}');
        return userInfo?.currentOrg?.orgid ||
               userInfo?.orgId ||
               this.$store.getters?.orgId ||
               this.$store.getters?.['user/orgId'] ||
               this.$store.state?.user?.orgId ||
               1;
      } catch (e) {
        return 1;
      }
    },
    /** 获取告警列表 */
    loadAlerts() {
      const orgId = this.getOrgId();
      getAlerts(orgId).then(response => {
        const data = response.data;
        if (Array.isArray(data)) {
          this.alertList = data.slice(0, 10);
        } else if (data && Array.isArray(data.rows)) {
          this.alertList = data.rows.slice(0, 10);
        } else if (data && Array.isArray(data.list)) {
          this.alertList = data.list.slice(0, 10);
        } else {
          this.alertList = [];
        }
      }).catch(error => {
        console.error('获取告警列表失败:', error);
        this.alertList = [];
      });
    },
    /** 初始化图表 */
    initCharts() {
      const typeEl = document.getElementById('monitorTypeChart');
      const trendEl = document.getElementById('performanceTrendChart');
      if (typeEl && !this.monitorTypeChart) {
        this.monitorTypeChart = echarts.init(typeEl);
      }
      if (trendEl && !this.performanceTrendChart) {
        this.performanceTrendChart = echarts.init(trendEl);
      }
      this.updateCharts();
    },
    /** 更新图表 */
    updateCharts() {
      // 图表实例不存在时先初始化
      const typeEl = document.getElementById('monitorTypeChart');
      const trendEl = document.getElementById('performanceTrendChart');
      if (typeEl && !this.monitorTypeChart) {
        this.monitorTypeChart = echarts.init(typeEl);
      }
      if (trendEl && !this.performanceTrendChart) {
        this.performanceTrendChart = echarts.init(trendEl);
      }

      const data = (this.dashboardData && Array.isArray(this.dashboardData)) ? this.dashboardData : [];

      if (this.monitorTypeChart) {
        const option = {
          tooltip: { trigger: 'item' },
          legend: { orient: 'vertical', left: 'left' },
          series: [
            {
              name: '监控类型',
              type: 'pie',
              radius: '50%',
              data: data.map(item => {
                const typeMap = { SYSTEM: '系统监控', INTERFACE: '接口监控', BUSINESS: '业务监控', PERFORMANCE: '性能监控' };
                return {
                  value: item.totalTransactions || 0,
                  name: typeMap[item.monitorType] || item.monitorType || '未知'
                };
              }),
              emphasis: {
                itemStyle: {
                  shadowBlur: 10,
                  shadowOffsetX: 0,
                  shadowColor: 'rgba(0, 0, 0, 0.5)'
                }
              }
            }
          ]
        };
        this.monitorTypeChart.setOption(option);
      }

      if (this.performanceTrendChart) {
        const option = {
          tooltip: { trigger: 'axis' },
          legend: { data: ['成功率', '平均处理时间'] },
          xAxis: {
            type: 'category',
            data: data.map(item => {
              const typeMap = { SYSTEM: '系统监控', INTERFACE: '接口监控', BUSINESS: '业务监控', PERFORMANCE: '性能监控' };
              return typeMap[item.monitorType] || item.monitorType || '未知';
            })
          },
          yAxis: [
            { type: 'value', name: '成功率(%)', position: 'left' },
            { type: 'value', name: '处理时间(ms)', position: 'right' }
          ],
          series: [
            {
              name: '成功率',
              type: 'line',
              yAxisIndex: 0,
              data: data.map(item => this.calculateSuccessRate(item))
            },
            {
              name: '平均处理时间',
              type: 'bar',
              yAxisIndex: 1,
              data: data.map(item => item.avgProcessingTime || 0)
            }
          ]
        };
        this.performanceTrendChart.setOption(option);
      }
    },
    /** 计算成功率 */
    calculateSuccessRate(item) {
      if (!item || !item.totalTransactions || item.totalTransactions === 0) {
        return 0;
      }
      const rate = (item.totalSuccess || 0) * 100.0 / item.totalTransactions;
      return parseFloat(rate.toFixed(2));
    },
    /** 刷新仪表板 */
    refreshDashboard() {
      this.getHealthStatus();
      this.getDashboard();
      this.loadAlerts();
    },
    /** 获取健康评分颜色 */
    getHealthColor(score) {
      if (score >= 90) return '#67C23A';
      if (score >= 70) return '#E6A23C';
      if (score >= 50) return '#F56C6C';
      return '#909399';
    },
    /** 获取健康评分类型 */
    getHealthScoreType(score) {
      if (score >= 90) return 'success';
      if (score >= 70) return 'warning';
      if (score >= 50) return 'danger';
      return 'info';
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        monitoringId: null,
        monitorType: null,
        monitorName: null,
        monitorTime: null,
        transactionCount: 0,
        successCount: 0,
        failedCount: 0,
        avgProcessingTime: 0,
        healthScore: null,
        alertLevel: null,
        alertMessage: null
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.dateRange = [];
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.monitoringId);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加监控数据";
    },
    /** 修改按钮操作 */
    handleEdit(row) {
      this.reset();
      const id = (row && row.monitoringId) ? row.monitoringId : this.ids[0];
      getMonitoringById(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改监控数据";
      });
    },
    /** 详情按钮操作 */
    handleView(row) {
      getMonitoringById(row.monitoringId).then(response => {
        this.detailData = response.data;
        this.detailOpen = true;
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.monitoringId != null) {
            updateMonitoring(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addMonitoring(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 发送告警 */
    handleSendAlert(row) {
      const monitoringIds = row && row.monitoringId ? [row.monitoringId] : this.ids;
      this.$modal.confirm('是否确认发送告警？').then(() => {
        const promises = monitoringIds.map(id => sendAlert(id));
        return Promise.all(promises);
      }).then(() => {
        this.getList();
        this.loadAlerts();
        this.$modal.msgSuccess("告警发送成功");
      }).catch(() => {});
    },
    /** 批量发送告警 */
    handleBatchSendAlert() {
      this.handleSendAlert();
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const monitoringIds = (row && row.monitoringId) ? row.monitoringId : this.ids.join(',');
      this.$modal.confirm('是否确认删除选中的监控数据？').then(function() {
        return deleteMonitoring(monitoringIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      const params = this.addDateRange({ ...this.queryParams }, this.dateRange);
      this.$modal.confirm('是否确认导出监控数据?').then(() => {
        exportPendingData(params).then(response => {
          const blob = new Blob([response], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' });
          const url = window.URL.createObjectURL(blob);
          const link = document.createElement('a');
          link.href = url;
          link.download = `监控数据_${new Date().getTime()}.xlsx`;
          link.click();
          window.URL.revokeObjectURL(url);
          this.$modal.msgSuccess("导出成功");
        }).catch(() => {});
      }).catch(() => {});
    },
    /** 添加日期范围 */
    addDateRange(params, dateRange, propName) {
      const search = params;
      dateRange = Array.isArray(dateRange) ? dateRange : [];
      if (typeof(propName) === 'undefined') {
        search['beginTime'] = dateRange[0];
        search['endTime'] = dateRange[1];
      } else {
        search['begin' + propName] = dateRange[0];
        search['end' + propName] = dateRange[1];
      }
      return search;
    },
    /** 重置表单 */
    resetForm(refName) {
      if (this.$refs[refName]) {
        this.$refs[refName].resetFields();
      }
    }
  }
};
</script>

<style scoped>
.health-score {
  text-align: center;
  padding: 20px 0;
}

.score-text {
  font-size: 18px;
  font-weight: bold;
}

.metric-value {
  text-align: center;
  padding: 20px 0;
}

.metric-value .value {
  font-size: 24px;
  font-weight: bold;
  margin-right: 10px;
}

.metric-value .value.critical {
  color: #F56C6C;
}

.mb20 {
  margin-bottom: 20px;
}
</style>
