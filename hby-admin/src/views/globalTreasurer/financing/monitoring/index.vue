<template>
  <div class="app-container">
    <el-tabs v-model="activeTab" type="card" @tab-click="handleTabClick">
      <!-- 融资监控仪表板 -->
      <el-tab-pane label="融资监控仪表板" name="dashboard">
        <div class="dashboard-container">
          <!-- 关键指标卡片 -->
          <el-row :gutter="20" class="mb20">
            <el-col :span="6">
              <el-card class="indicator-card">
                <div class="indicator-content">
                  <div class="indicator-icon">
                    <i class="el-icon-money" style="color: #409EFF;"></i>
                  </div>
                  <div class="indicator-info">
                    <div class="indicator-value">{{ dashboardData.totalFinancing }}</div>
                    <div class="indicator-label">总融资规模（万元）</div>
                  </div>
                </div>
              </el-card>
            </el-col>
            <el-col :span="6">
              <el-card class="indicator-card">
                <div class="indicator-content">
                  <div class="indicator-icon">
                    <i class="el-icon-document" style="color: #67C23A;"></i>
                  </div>
                  <div class="indicator-info">
                    <div class="indicator-value">{{ dashboardData.activeContracts }}</div>
                    <div class="indicator-label">有效合同数量</div>
                  </div>
                </div>
              </el-card>
            </el-col>
            <el-col :span="6">
              <el-card class="indicator-card">
                <div class="indicator-content">
                  <div class="indicator-icon">
                    <i class="el-icon-warning" style="color: #E6A23C;"></i>
                  </div>
                  <div class="indicator-info">
                    <div class="indicator-value">{{ dashboardData.riskAlerts }}</div>
                    <div class="indicator-label">风险预警数量</div>
                  </div>
                </div>
              </el-card>
            </el-col>
            <el-col :span="6">
              <el-card class="indicator-card">
                <div class="indicator-content">
                  <div class="indicator-icon">
                    <i class="el-icon-data-line" style="color: #F56C6C;"></i>
                  </div>
                  <div class="indicator-info">
                    <div class="indicator-value">{{ dashboardData.avgCostRate }}%</div>
                    <div class="indicator-label">平均融资成本</div>
                  </div>
                </div>
              </el-card>
            </el-col>
          </el-row>

          <!-- 图表区域 -->
          <el-row :gutter="20">
            <el-col :span="12">
              <el-card>
                <div slot="header">
                  <span>融资规模趋势</span>
                </div>
                <div id="financingTrendChart" style="height: 300px;"></div>
              </el-card>
            </el-col>
            <el-col :span="12">
              <el-card>
                <div slot="header">
                  <span>融资结构分析</span>
                </div>
                <div id="financingStructureChart" style="height: 300px;"></div>
              </el-card>
            </el-col>
          </el-row>

          <!-- 预警信息 -->
          <el-row :gutter="20" class="mt20">
            <el-col :span="24">
              <el-card>
                <div slot="header">
                  <span>最新预警信息</span>
                  <el-button style="float: right; padding: 3px 0" type="text" @click="viewAllAlerts">查看全部</el-button>
                </div>
                <el-table :data="recentAlerts" style="width: 100%">
                  <el-table-column prop="alertType" label="预警类型" width="120">
                    <template slot-scope="scope">
                      <el-tag :type="getAlertTypeTag(scope.row.alertType)">{{ scope.row.alertType }}</el-tag>
                    </template>
                  </el-table-column>
                  <el-table-column prop="alertContent" label="预警内容" />
                  <el-table-column prop="riskLevel" label="风险等级" width="100">
                    <template slot-scope="scope">
                      <el-tag :type="getRiskLevelTag(scope.row.riskLevel)">{{ scope.row.riskLevel }}</el-tag>
                    </template>
                  </el-table-column>
                  <el-table-column prop="alertTime" label="预警时间" width="180">
                    <template slot-scope="scope">
                      <span>{{ parseTime(scope.row.alertTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
                    </template>
                  </el-table-column>
                  <el-table-column label="操作" width="100">
                    <template slot-scope="scope">
                      <el-button size="mini" type="text" @click="handleAlert(scope.row)">处理</el-button>
                    </template>
                  </el-table-column>
                </el-table>
              </el-card>
            </el-col>
          </el-row>
        </div>
      </el-tab-pane>

      <!-- 风险监控 -->
      <el-tab-pane label="风险监控" name="riskMonitoring">
        <div class="risk-monitoring-container">
          <!-- 概览卡片 -->
          <el-row :gutter="20" class="overview-cards">
            <el-col :span="6">
              <el-card class="overview-card">
                <div class="card-content">
                  <div class="card-icon total">
                    <i class="el-icon-monitor"></i>
                  </div>
                  <div class="card-info">
                    <div class="card-title">总监控数</div>
                    <div class="card-value">{{ riskMonitoringOverview.totalMonitorings || 0 }}</div>
                  </div>
                </div>
              </el-card>
            </el-col>
            <el-col :span="6">
              <el-card class="overview-card">
                <div class="card-content">
                  <div class="card-icon active">
                    <i class="el-icon-warning"></i>
                  </div>
                  <div class="card-info">
                    <div class="card-title">活跃监控</div>
                    <div class="card-value">{{ riskMonitoringOverview.activeMonitorings || 0 }}</div>
                  </div>
                </div>
              </el-card>
            </el-col>
            <el-col :span="6">
              <el-card class="overview-card">
                <div class="card-content">
                  <div class="card-icon critical">
                    <i class="el-icon-warning-outline"></i>
                  </div>
                  <div class="card-info">
                    <div class="card-title">严重风险</div>
                    <div class="card-value">{{ riskMonitoringOverview.criticalRisks || 0 }}</div>
                  </div>
                </div>
              </el-card>
            </el-col>
            <el-col :span="6">
              <el-card class="overview-card">
                <div class="card-content">
                  <div class="card-icon alert">
                    <i class="el-icon-bell"></i>
                  </div>
                  <div class="card-info">
                    <div class="card-title">活跃警报</div>
                    <div class="card-value">{{ riskMonitoringOverview.activeAlerts || 0 }}</div>
                  </div>
                </div>
              </el-card>
            </el-col>
          </el-row>

          <!-- 查询表单 -->
          <el-card class="query-card">
            <el-form :model="riskMonitoringQuery" ref="riskMonitoringQueryForm" :inline="true" label-width="100px">
              <el-form-item label="监控编号" prop="monitoringNo">
                <el-input v-model="riskMonitoringQuery.monitoringNo" placeholder="请输入监控编号" clearable style="width: 200px;"></el-input>
              </el-form-item>
              <el-form-item label="风险类型" prop="riskType">
                <el-select v-model="riskMonitoringQuery.riskType" placeholder="请选择风险类型" clearable style="width: 150px;">
                  <el-option label="信用风险" value="CREDIT_RISK"></el-option>
                  <el-option label="流动性风险" value="LIQUIDITY_RISK"></el-option>
                  <el-option label="市场风险" value="MARKET_RISK"></el-option>
                  <el-option label="操作风险" value="OPERATIONAL_RISK"></el-option>
                  <el-option label="合规风险" value="COMPLIANCE_RISK"></el-option>
                  <el-option label="集中度风险" value="CONCENTRATION_RISK"></el-option>
                  <el-option label="利率风险" value="INTEREST_RATE_RISK"></el-option>
                  <el-option label="汇率风险" value="CURRENCY_RISK"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="风险等级" prop="riskLevel">
                <el-select v-model="riskMonitoringQuery.riskLevel" placeholder="请选择风险等级" clearable style="width: 120px;">
                  <el-option label="低风险" value="LOW"></el-option>
                  <el-option label="中风险" value="MEDIUM"></el-option>
                  <el-option label="高风险" value="HIGH"></el-option>
                  <el-option label="严重风险" value="CRITICAL"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="风险状态" prop="riskStatus">
                <el-select v-model="riskMonitoringQuery.riskStatus" placeholder="请选择风险状态" clearable style="width: 120px;">
                  <el-option label="正常" value="NORMAL"></el-option>
                  <el-option label="预警" value="WARNING"></el-option>
                  <el-option label="严重" value="CRITICAL"></el-option>
                  <el-option label="违约" value="BREACH"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="公司名称" prop="companyName">
                <el-input v-model="riskMonitoringQuery.companyName" placeholder="请输入公司名称" clearable style="width: 200px;"></el-input>
              </el-form-item>
              <el-form-item label="监控日期" prop="monitoringDateRange">
                <el-date-picker
                  v-model="riskMonitoringQuery.monitoringDateRange"
                  type="daterange"
                  range-separator="至"
                  start-placeholder="开始日期"
                  end-placeholder="结束日期"
                  format="yyyy-MM-dd"
                  value-format="yyyy-MM-dd"
                  style="width: 240px;">
                </el-date-picker>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="handleRiskMonitoringQuery">查询</el-button>
                <el-button @click="resetRiskMonitoringQuery">重置</el-button>
              </el-form-item>
            </el-form>
          </el-card>

          <!-- 操作按钮 -->
          <el-card class="operation-card">
            <el-row>
              <el-col :span="12">
                <el-button type="primary" @click="handleAddRiskMonitoring">新增监控</el-button>
                <el-button type="danger" @click="handleBatchDeleteRiskMonitoring" :disabled="riskMonitoringSelection.length === 0">批量删除</el-button>
                <el-button type="warning" @click="handleExportRiskMonitoring">导出数据</el-button>
              </el-col>
            </el-row>
          </el-card>

          <!-- 数据表格 -->
          <el-card class="table-card">
            <el-table
              :data="riskMonitoringList"
              v-loading="riskMonitoringLoading"
              @selection-change="handleRiskMonitoringSelectionChange"
              border
              stripe
              style="width: 100%">
              <el-table-column type="selection" width="55"></el-table-column>
              <el-table-column prop="monitoringNo" label="监控编号" width="150" show-overflow-tooltip></el-table-column>
              <el-table-column prop="riskType" label="风险类型" width="120">
                <template slot-scope="scope">
                  <el-tag :type="getRiskTypeTagType(scope.row.riskType)" size="small">
                    {{ getRiskTypeText(scope.row.riskType) }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="riskLevel" label="风险等级" width="100">
                <template slot-scope="scope">
                  <el-tag :type="getRiskLevelTagType(scope.row.riskLevel)" size="small">
                    {{ getRiskLevelText(scope.row.riskLevel) }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="riskStatus" label="风险状态" width="100">
                <template slot-scope="scope">
                  <el-tag :type="getRiskStatusTagType(scope.row.riskStatus)" size="small">
                    {{ getRiskStatusText(scope.row.riskStatus) }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="companyName" label="公司名称" width="200" show-overflow-tooltip></el-table-column>
              <el-table-column prop="contractNo" label="合同编号" width="150" show-overflow-tooltip></el-table-column>
              <el-table-column prop="monitoringDate" label="监控日期" width="120"></el-table-column>
              <el-table-column prop="riskDescription" label="风险描述" min-width="200" show-overflow-tooltip></el-table-column>
              <el-table-column label="操作" width="200" fixed="right">
                <template slot-scope="scope">
                  <el-dropdown @command="(command) => handleRiskMonitoringOperation(command, scope.row)">
                    <el-button type="text" size="small">
                      操作<i class="el-icon-arrow-down el-icon--right"></i>
                    </el-button>
                    <el-dropdown-menu slot="dropdown">
                      <el-dropdown-item command="detail">查看详情</el-dropdown-item>
                      <el-dropdown-item command="edit">编辑</el-dropdown-item>
                      <el-dropdown-item command="triggerAlert" v-if="scope.row.riskStatus !== 'BREACH'">触发警报</el-dropdown-item>
                      <el-dropdown-item command="handle" v-if="scope.row.riskStatus === 'WARNING' || scope.row.riskStatus === 'CRITICAL'">处理</el-dropdown-item>
                      <el-dropdown-item command="close" v-if="scope.row.riskStatus !== 'NORMAL'">关闭</el-dropdown-item>
                      <el-dropdown-item command="reactivate" v-if="scope.row.riskStatus === 'NORMAL'">重新激活</el-dropdown-item>
                      <el-dropdown-item command="delete" divided>删除</el-dropdown-item>
                    </el-dropdown-menu>
                  </el-dropdown>
                </template>
              </el-table-column>
            </el-table>

            <!-- 分页 -->
            <div class="pagination-container">
              <el-pagination
                @size-change="handleRiskMonitoringSizeChange"
                @current-change="handleRiskMonitoringCurrentChange"
                :current-page="riskMonitoringQuery.pageNum"
                :page-sizes="[10, 20, 50, 100]"
                :page-size="riskMonitoringQuery.pageSize"
                layout="total, sizes, prev, pager, next, jumper"
                :total="riskMonitoringTotal">
              </el-pagination>
            </div>
          </el-card>
        </div>
      </el-tab-pane>

      <!-- 成本分析 -->
      <el-tab-pane label="成本分析" name="costAnalysis">
        <div class="cost-analysis-container">
          <p>成本分析功能开发中...</p>
        </div>
      </el-tab-pane>

      <!-- 报表管理 -->
      <el-tab-pane label="报表管理" name="reportManagement">
        <div class="report-management-container">
          <p>报表管理功能开发中...</p>
        </div>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
import {
  getFinancingDashboardData,
  getFinancingAlerts,
  handleFinancingAlert,
  getFinancingRiskMonitoringPage,
  getFinancingRiskMonitoring,
  createFinancingRiskMonitoring,
  updateFinancingRiskMonitoring,
  deleteFinancingRiskMonitoring,
  batchDeleteFinancingRiskMonitorings,
  triggerFinancingRiskAlert,
  handleFinancingRiskMonitoring,
  closeFinancingRiskMonitoring,
  reactivateFinancingRiskMonitoring,
  getFinancingRiskMonitoringOverview,
  getFinancingRiskMonitoringStatistics,
  getFinancingRiskMonitoringDashboard
} from "@/api/globalTreasurer/rzgl";
import { parseTime } from '@/utils'
import * as echarts from 'echarts'

export default {
  name: "FinancingMonitoring",
  data() {
    return {
      // 当前激活的标签页
      activeTab: "dashboard",
      
      // 仪表板数据
      dashboardData: {
        totalFinancing: 0,
        activeContracts: 0,
        riskAlerts: 0,
        avgCostRate: 0
      },
      
      // 最新预警信息
      recentAlerts: [],
      
      // 图表实例
      financingTrendChart: null,
      financingStructureChart: null,

      // 风险监控相关数据
      riskMonitoringOverview: {
        totalMonitorings: 0,
        activeMonitorings: 0,
        criticalRisks: 0,
        activeAlerts: 0
      },
      riskMonitoringQuery: {
        pageNum: 1,
        pageSize: 10,
        monitoringNo: '',
        riskType: '',
        riskLevel: '',
        riskStatus: '',
        companyName: '',
        monitoringDateRange: [],
        orgId: 1
      },
      riskMonitoringList: [],
      riskMonitoringTotal: 0,
      riskMonitoringLoading: false,
      riskMonitoringSelection: []
    };
  },
  created() {
    this.loadDashboardData();
    this.loadRecentAlerts();
  },
  mounted() {
    this.$nextTick(() => {
      this.initCharts();
    });
  },
  beforeDestroy() {
    if (this.financingTrendChart) {
      this.financingTrendChart.dispose();
    }
    if (this.financingStructureChart) {
      this.financingStructureChart.dispose();
    }
  },
  methods: {
    parseTime,
    
    /** 标签页切换 */
    handleTabClick(tab) {
      if (tab.name === 'dashboard') {
        this.loadDashboardData();
        this.$nextTick(() => {
          this.initCharts();
        });
      } else if (tab.name === 'riskMonitoring') {
        this.loadRiskMonitoringOverview();
        this.loadRiskMonitoringList();
      }
    },

    /** 加载仪表板数据 */
    loadDashboardData() {
      getFinancingDashboardData().then(response => {
        this.dashboardData = response.data;
      });
    },

    /** 加载最新预警信息 */
    loadRecentAlerts() {
      getFinancingAlerts({ pageNum: 1, pageSize: 5 }).then(response => {
        this.recentAlerts = response.rows || [];
      });
    },

    /** 初始化图表 */
    initCharts() {
      this.initFinancingTrendChart();
      this.initFinancingStructureChart();
    },

    /** 初始化融资规模趋势图 */
    initFinancingTrendChart() {
      const chartDom = document.getElementById('financingTrendChart');
      if (!chartDom) return;
      
      this.financingTrendChart = echarts.init(chartDom);
      const option = {
        title: {
          text: '融资规模趋势',
          left: 'center'
        },
        tooltip: {
          trigger: 'axis'
        },
        xAxis: {
          type: 'category',
          data: ['1月', '2月', '3月', '4月', '5月', '6月']
        },
        yAxis: {
          type: 'value',
          name: '金额（万元）'
        },
        series: [{
          data: [12000, 15000, 18000, 16000, 20000, 22000],
          type: 'line',
          smooth: true,
          itemStyle: {
            color: '#409EFF'
          }
        }]
      };
      this.financingTrendChart.setOption(option);
    },

    /** 初始化融资结构图 */
    initFinancingStructureChart() {
      const chartDom = document.getElementById('financingStructureChart');
      if (!chartDom) return;
      
      this.financingStructureChart = echarts.init(chartDom);
      const option = {
        title: {
          text: '融资结构分析',
          left: 'center'
        },
        tooltip: {
          trigger: 'item'
        },
        series: [{
          type: 'pie',
          radius: '50%',
          data: [
            { value: 35, name: '银行贷款' },
            { value: 25, name: '债券发行' },
            { value: 20, name: '信托融资' },
            { value: 15, name: '其他融资' },
            { value: 5, name: '票据融资' }
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
      this.financingStructureChart.setOption(option);
    },

    /** 获取预警类型标签样式 */
    getAlertTypeTag(type) {
      const tagMap = {
        '额度预警': 'warning',
        '到期预警': 'danger',
        '利率预警': 'info',
        '合规预警': 'danger'
      };
      return tagMap[type] || 'info';
    },

    /** 获取风险等级标签样式 */
    getRiskLevelTag(level) {
      const tagMap = {
        '高': 'danger',
        '中': 'warning',
        '低': 'success'
      };
      return tagMap[level] || 'info';
    },

    /** 处理预警 */
    handleAlert(row) {
      this.$router.push(`/globalTreasurer/financing/monitoring/alert/${row.alertId}`);
    },

    /** 查看全部预警 */
    viewAllAlerts() {
      this.activeTab = 'riskMonitoring';
    },

    // ==================== 风险监控相关方法 ====================

    /** 加载风险监控概览数据 */
    loadRiskMonitoringOverview() {
      getFinancingRiskMonitoringOverview(this.riskMonitoringQuery.orgId).then(response => {
        this.riskMonitoringOverview = response || {
          totalMonitorings: 0,
          activeMonitorings: 0,
          criticalRisks: 0,
          activeAlerts: 0
        };
      }).catch(error => {
        console.error('加载风险监控概览数据失败:', error);
        this.riskMonitoringOverview = {
          totalMonitorings: 0,
          activeMonitorings: 0,
          criticalRisks: 0,
          activeAlerts: 0
        };
      });
    },

    /** 加载风险监控列表 */
    loadRiskMonitoringList() {
      this.riskMonitoringLoading = true;
      const query = { ...this.riskMonitoringQuery };

      // 处理日期范围
      if (query.monitoringDateRange && query.monitoringDateRange.length === 2) {
        query.monitoringDateStart = query.monitoringDateRange[0];
        query.monitoringDateEnd = query.monitoringDateRange[1];
      }
      delete query.monitoringDateRange;

      getFinancingRiskMonitoringPage(query).then(response => {
        this.riskMonitoringList = response.records || [];
        this.riskMonitoringTotal = response.total || 0;
      }).catch(error => {
        console.error('加载风险监控列表失败:', error);
        this.riskMonitoringList = [];
        this.riskMonitoringTotal = 0;
        this.$message.error('加载风险监控列表失败');
      }).finally(() => {
        this.riskMonitoringLoading = false;
      });
    },

    /** 查询风险监控 */
    handleRiskMonitoringQuery() {
      this.riskMonitoringQuery.pageNum = 1;
      this.loadRiskMonitoringList();
    },

    /** 重置查询条件 */
    resetRiskMonitoringQuery() {
      this.$refs.riskMonitoringQueryForm.resetFields();
      this.riskMonitoringQuery = {
        pageNum: 1,
        pageSize: 10,
        monitoringNo: '',
        riskType: '',
        riskLevel: '',
        riskStatus: '',
        companyName: '',
        monitoringDateRange: [],
        orgId: 1
      };
      this.loadRiskMonitoringList();
    },

    /** 分页大小改变 */
    handleRiskMonitoringSizeChange(val) {
      this.riskMonitoringQuery.pageSize = val;
      this.loadRiskMonitoringList();
    },

    /** 当前页改变 */
    handleRiskMonitoringCurrentChange(val) {
      this.riskMonitoringQuery.pageNum = val;
      this.loadRiskMonitoringList();
    },

    /** 选择改变 */
    handleRiskMonitoringSelectionChange(selection) {
      this.riskMonitoringSelection = selection;
    },

    /** 新增风险监控 */
    handleAddRiskMonitoring() {
      this.$message.info('新增风险监控功能开发中...');
    },

    /** 批量删除风险监控 */
    handleBatchDeleteRiskMonitoring() {
      if (this.riskMonitoringSelection.length === 0) {
        this.$message.warning('请选择要删除的记录');
        return;
      }

      this.$confirm('确认删除选中的风险监控记录吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const monitoringIds = this.riskMonitoringSelection.map(item => item.monitoringId);
        batchDeleteFinancingRiskMonitorings(monitoringIds).then(() => {
          this.$message.success('删除成功');
          this.loadRiskMonitoringList();
          this.loadRiskMonitoringOverview();
        }).catch(error => {
          console.error('批量删除失败:', error);
          this.$message.error('删除失败');
        });
      });
    },

    /** 导出风险监控数据 */
    handleExportRiskMonitoring() {
      this.$message.info('导出功能开发中...');
    },

    /** 风险监控操作 */
    handleRiskMonitoringOperation(command, row) {
      switch (command) {
        case 'detail':
          this.viewRiskMonitoringDetail(row);
          break;
        case 'edit':
          this.editRiskMonitoring(row);
          break;
        case 'triggerAlert':
          this.triggerRiskAlert(row);
          break;
        case 'handle':
          this.handleRiskMonitoring(row);
          break;
        case 'close':
          this.closeRiskMonitoring(row);
          break;
        case 'reactivate':
          this.reactivateRiskMonitoring(row);
          break;
        case 'delete':
          this.deleteRiskMonitoring(row);
          break;
      }
    },

    /** 查看风险监控详情 */
    viewRiskMonitoringDetail(row) {
      this.$message.info('查看详情功能开发中...');
    },

    /** 编辑风险监控 */
    editRiskMonitoring(row) {
      this.$message.info('编辑功能开发中...');
    },

    /** 触发风险警报 */
    triggerRiskAlert(row) {
      this.$prompt('请输入警报消息', '触发风险警报', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputPattern: /.+/,
        inputErrorMessage: '警报消息不能为空'
      }).then(({ value }) => {
        triggerFinancingRiskAlert(row.monitoringId, value, 1).then(() => {
          this.$message.success('触发警报成功');
          this.loadRiskMonitoringList();
          this.loadRiskMonitoringOverview();
        }).catch(error => {
          console.error('触发警报失败:', error);
          this.$message.error('触发警报失败');
        });
      });
    },

    /** 处理风险监控 */
    handleRiskMonitoring(row) {
      this.$prompt('请输入处理措施', '处理风险监控', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputPattern: /.+/,
        inputErrorMessage: '处理措施不能为空'
      }).then(({ value }) => {
        handleFinancingRiskMonitoring(row.monitoringId, value, 1).then(() => {
          this.$message.success('处理成功');
          this.loadRiskMonitoringList();
          this.loadRiskMonitoringOverview();
        }).catch(error => {
          console.error('处理失败:', error);
          this.$message.error('处理失败');
        });
      });
    },

    /** 关闭风险监控 */
    closeRiskMonitoring(row) {
      this.$prompt('请输入关闭原因', '关闭风险监控', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputPattern: /.+/,
        inputErrorMessage: '关闭原因不能为空'
      }).then(({ value }) => {
        closeFinancingRiskMonitoring(row.monitoringId, value, 1).then(() => {
          this.$message.success('关闭成功');
          this.loadRiskMonitoringList();
          this.loadRiskMonitoringOverview();
        }).catch(error => {
          console.error('关闭失败:', error);
          this.$message.error('关闭失败');
        });
      });
    },

    /** 重新激活风险监控 */
    reactivateRiskMonitoring(row) {
      this.$prompt('请输入激活原因', '重新激活风险监控', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputPattern: /.+/,
        inputErrorMessage: '激活原因不能为空'
      }).then(({ value }) => {
        reactivateFinancingRiskMonitoring(row.monitoringId, value, 1).then(() => {
          this.$message.success('重新激活成功');
          this.loadRiskMonitoringList();
          this.loadRiskMonitoringOverview();
        }).catch(error => {
          console.error('重新激活失败:', error);
          this.$message.error('重新激活失败');
        });
      });
    },

    /** 删除风险监控 */
    deleteRiskMonitoring(row) {
      this.$confirm(`确认删除风险监控"${row.monitoringNo}"吗？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteFinancingRiskMonitoring(row.monitoringId).then(() => {
          this.$message.success('删除成功');
          this.loadRiskMonitoringList();
          this.loadRiskMonitoringOverview();
        }).catch(error => {
          console.error('删除失败:', error);
          this.$message.error('删除失败');
        });
      });
    },

    // ==================== 辅助方法 ====================

    /** 获取风险类型标签类型 */
    getRiskTypeTagType(riskType) {
      const typeMap = {
        'CREDIT_RISK': 'danger',
        'LIQUIDITY_RISK': 'warning',
        'MARKET_RISK': 'info',
        'OPERATIONAL_RISK': 'primary',
        'COMPLIANCE_RISK': 'danger',
        'CONCENTRATION_RISK': 'warning',
        'INTEREST_RATE_RISK': 'info',
        'CURRENCY_RISK': 'success'
      };
      return typeMap[riskType] || '';
    },

    /** 获取风险类型文本 */
    getRiskTypeText(riskType) {
      const typeMap = {
        'CREDIT_RISK': '信用风险',
        'LIQUIDITY_RISK': '流动性风险',
        'MARKET_RISK': '市场风险',
        'OPERATIONAL_RISK': '操作风险',
        'COMPLIANCE_RISK': '合规风险',
        'CONCENTRATION_RISK': '集中度风险',
        'INTEREST_RATE_RISK': '利率风险',
        'CURRENCY_RISK': '汇率风险'
      };
      return typeMap[riskType] || riskType;
    },

    /** 获取风险等级标签类型 */
    getRiskLevelTagType(riskLevel) {
      const levelMap = {
        'LOW': 'success',
        'MEDIUM': 'warning',
        'HIGH': 'danger',
        'CRITICAL': 'danger'
      };
      return levelMap[riskLevel] || '';
    },

    /** 获取风险等级文本 */
    getRiskLevelText(riskLevel) {
      const levelMap = {
        'LOW': '低风险',
        'MEDIUM': '中风险',
        'HIGH': '高风险',
        'CRITICAL': '严重风险'
      };
      return levelMap[riskLevel] || riskLevel;
    },

    /** 获取风险状态标签类型 */
    getRiskStatusTagType(riskStatus) {
      const statusMap = {
        'NORMAL': 'success',
        'WARNING': 'warning',
        'CRITICAL': 'danger',
        'BREACH': 'danger'
      };
      return statusMap[riskStatus] || '';
    },

    /** 获取风险状态文本 */
    getRiskStatusText(riskStatus) {
      const statusMap = {
        'NORMAL': '正常',
        'WARNING': '预警',
        'CRITICAL': '严重',
        'BREACH': '违约'
      };
      return statusMap[riskStatus] || riskStatus;
    }
  }
};
</script>

<style scoped>
.app-container {
  padding: 20px;
}

.mb20 {
  margin-bottom: 20px;
}

.mt20 {
  margin-top: 20px;
}

.indicator-card {
  height: 120px;
}

.indicator-content {
  display: flex;
  align-items: center;
  height: 100%;
}

.indicator-icon {
  font-size: 48px;
  margin-right: 20px;
}

.indicator-info {
  flex: 1;
}

.indicator-value {
  font-size: 24px;
  font-weight: bold;
  color: #303133;
  margin-bottom: 5px;
}

.indicator-label {
  font-size: 14px;
  color: #606266;
}

/* 风险监控相关样式 */
.risk-monitoring-container {
  padding: 20px;
}

.overview-cards {
  margin-bottom: 20px;
}

.overview-card {
  height: 120px;
}

.card-content {
  display: flex;
  align-items: center;
  height: 100%;
}

.card-icon {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 15px;
  font-size: 24px;
  color: white;
}

.card-icon.total {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.card-icon.active {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
}

.card-icon.critical {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
}

.card-icon.alert {
  background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
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

.query-card {
  margin-bottom: 20px;
}

.operation-card {
  margin-bottom: 20px;
}

.table-card {
  margin-bottom: 20px;
}

.pagination-container {
  text-align: right;
  margin-top: 20px;
}
</style>
