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
              <span class="score-text">{{ healthStatus.avgHealthScore || 0 }}</span>
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
            <span class="value critical">{{ (healthStatus.criticalAlertCount || 0) + (healthStatus.highAlertCount || 0) }}</span>
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
        <el-button style="float: right; padding: 3px 0" type="text" @click="getAlerts">刷新</el-button>
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
          v-hasPermi="['settlement:monitoring:add']"
        >新增监控</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-message"
          size="mini"
          :disabled="multiple"
          @click="handleBatchSendAlert"
          v-hasPermi="['settlement:monitoring:alert']"
        >批量发送告警</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="info"
          plain
          icon="el-icon-upload2"
          size="mini"
          @click="handleExport"
          v-hasPermi="['settlement:monitoring:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <!-- 数据表格 -->
    <el-table v-loading="loading" :data="monitoringList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="监控ID" align="center" prop="monitoringId" width="80" />
      <el-table-column label="监控类型" align="center" prop="monitorType" width="100">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.monitor_type" :value="scope.row.monitorType"/>
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
            v-hasPermi="['settlement:monitoring:query']"
          >详情</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-message"
            @click="handleSendAlert(scope.row)"
            v-hasPermi="['settlement:monitoring:alert']"
            v-if="scope.row.alertLevel && !scope.row.isAlertSent"
          >发送告警</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
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
  sendAlert
} from "@/api/globalTreasurer/jspt";
import * as echarts from 'echarts';

export default {
  name: "SettlementMonitoring",
  dicts: ['monitor_type'],
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
    this.getHealthStatus();
    this.getDashboard();
    this.getAlerts();
  },
  mounted() {
    this.$nextTick(() => {
      this.initCharts();
    });
  },
  methods: {
    /** 查询监控数据列表 */
    getList() {
      this.loading = true;
      const params = this.addDateRange(this.queryParams, this.dateRange);
      getMonitoringPage(params).then(response => {
        this.monitoringList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    /** 获取系统健康状态 */
    getHealthStatus() {
      getSystemHealthStatus(this.$store.getters.orgId).then(response => {
        this.healthStatus = response.data;
      });
    },
    /** 获取仪表板数据 */
    getDashboard() {
      getDashboardData(this.$store.getters.orgId).then(response => {
        this.dashboardData = response.data;
        this.$nextTick(() => {
          this.updateCharts();
        });
      });
    },
    /** 获取告警列表 */
    getAlerts() {
      getAlerts(this.$store.getters.orgId).then(response => {
        this.alertList = response.data.slice(0, 10); // 只显示前10条
      });
    },
    /** 初始化图表 */
    initCharts() {
      // 监控类型分布图表
      this.monitorTypeChart = echarts.init(document.getElementById('monitorTypeChart'));
      
      // 性能趋势图表
      this.performanceTrendChart = echarts.init(document.getElementById('performanceTrendChart'));
      
      this.updateCharts();
    },
    /** 更新图表 */
    updateCharts() {
      if (this.monitorTypeChart && this.dashboardData.length > 0) {
        const option = {
          tooltip: {
            trigger: 'item'
          },
          legend: {
            orient: 'vertical',
            left: 'left'
          },
          series: [
            {
              name: '监控类型',
              type: 'pie',
              radius: '50%',
              data: this.dashboardData.map(item => ({
                value: item.recordCount,
                name: item.monitorType
              })),
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
      
      if (this.performanceTrendChart && this.dashboardData.length > 0) {
        const option = {
          tooltip: {
            trigger: 'axis'
          },
          legend: {
            data: ['成功率', '平均处理时间']
          },
          xAxis: {
            type: 'category',
            data: this.dashboardData.map(item => item.monitorType)
          },
          yAxis: [
            {
              type: 'value',
              name: '成功率(%)',
              position: 'left'
            },
            {
              type: 'value',
              name: '处理时间(ms)',
              position: 'right'
            }
          ],
          series: [
            {
              name: '成功率',
              type: 'line',
              yAxisIndex: 0,
              data: this.dashboardData.map(item => item.avgSuccessRate)
            },
            {
              name: '平均处理时间',
              type: 'bar',
              yAxisIndex: 1,
              data: this.dashboardData.map(item => item.avgProcessingTime)
            }
          ]
        };
        this.performanceTrendChart.setOption(option);
      }
    },
    /** 刷新仪表板 */
    refreshDashboard() {
      this.getHealthStatus();
      this.getDashboard();
      this.getAlerts();
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
        transactionCount: 0,
        successCount: 0,
        failedCount: 0,
        avgProcessingTime: 0,
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
        this.getAlerts();
        this.$modal.msgSuccess("告警发送成功");
      }).catch(() => {});
    },
    /** 批量发送告警 */
    handleBatchSendAlert() {
      this.handleSendAlert();
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('settlement/monitoring/export', {
        ...this.queryParams
      }, `monitoring_${new Date().getTime()}.xlsx`)
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
