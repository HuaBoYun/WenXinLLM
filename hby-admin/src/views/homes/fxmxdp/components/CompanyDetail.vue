<template>
  <div class="company-detail">
    <el-tabs v-model="activeTab" type="border-card">
      <!-- 基本信息 -->
      <el-tab-pane label="基本信息" name="basic">
        <div class="info-section">
          <el-row :gutter="20">
            <el-col :span="12">
              <div class="info-item">
                <span class="label">企业名称:</span>
                <span class="value">{{ company.companyName }}</span>
              </div>
            </el-col>
            <el-col :span="12">
              <div class="info-item">
                <span class="label">企业代码:</span>
                <span class="value">{{ company.companyId }}</span>
              </div>
            </el-col>
            <el-col :span="12">
              <div class="info-item">
                <span class="label">风险等级:</span>
                <el-tag :type="getRiskLevelType(company.riskLevel)">
                  {{ company.riskLevel }}
                </el-tag>
              </div>
            </el-col>
            <el-col :span="12">
              <div class="info-item">
                <span class="label">预警数量:</span>
                <span class="value warning">{{ company.warningCount }} 条</span>
              </div>
            </el-col>
          </el-row>
        </div>
      </el-tab-pane>

      <!-- 预警详情 -->
      <el-tab-pane label="预警详情" name="warnings">
        <el-table
          :data="warningList"
          style="width: 100%"
          max-height="400"
        >
          <el-table-column prop="warningTitle" label="预警标题" width="200" />
          <el-table-column prop="warningLevel" label="风险等级" width="100">
            <template slot-scope="scope">
              <el-tag :type="getRiskLevelType(scope.row.warningLevel)" size="small">
                {{ scope.row.warningLevel }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="warningContent" label="预警内容" show-overflow-tooltip />
          <el-table-column prop="createTime" label="创建时间" width="180">
            <template slot-scope="scope">
              {{ formatDateTime(scope.row.createTime) }}
            </template>
          </el-table-column>
          <el-table-column prop="status" label="状态" width="100">
            <template slot-scope="scope">
              <el-tag :type="getStatusType(scope.row.status)" size="small">
                {{ getStatusText(scope.row.status) }}
              </el-tag>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>

      <!-- 数据来源 -->
      <el-tab-pane label="数据来源" name="datasource">
        <div class="datasource-section">
          <el-timeline>
            <el-timeline-item
              v-for="(item, index) in dataSourceFlow"
              :key="index"
              :timestamp="item.timestamp"
              placement="top"
            >
              <el-card>
                <h4>{{ item.title }}</h4>
                <p>{{ item.description }}</p>
                <div v-if="item.tables" class="table-list">
                  <el-tag
                    v-for="table in item.tables"
                    :key="table"
                    size="small"
                    style="margin-right: 10px;"
                  >
                    {{ table }}
                  </el-tag>
                </div>
              </el-card>
            </el-timeline-item>
          </el-timeline>
        </div>
      </el-tab-pane>

      <!-- 趋势分析 -->
      <el-tab-pane label="趋势分析" name="trend">
        <div class="trend-section">
          <div class="chart-wrapper">
            <div ref="trendChart" class="chart-container"></div>
          </div>
          <div class="trend-summary">
            <el-alert
              title="趋势分析"
              type="info"
              :closable="false"
            >
              <div>
                <p>近6个月预警趋势分析:</p>
                <ul>
                  <li>预警数量呈上升趋势,需要重点关注</li>
                  <li>高风险预警占比较高,建议及时处理</li>
                  <li>资金头寸预警频繁,需要优化资金管理</li>
                </ul>
              </div>
            </el-alert>
          </div>
        </div>
      </el-tab-pane>

      <!-- 原始数据 -->
      <el-tab-pane label="原始数据" name="rawdata">
        <el-table
          :data="rawDataList"
          style="width: 100%"
          max-height="400"
          border
        >
          <el-table-column prop="indicatorName" label="指标名称" width="200" />
          <el-table-column prop="indicatorValue" label="指标值" width="150" />
          <el-table-column prop="threshold" label="阈值" width="150" />
          <el-table-column prop="status" label="状态" width="100">
            <template slot-scope="scope">
              <el-tag :type="scope.row.status === '正常' ? 'success' : 'danger'" size="small">
                {{ scope.row.status }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="remark" label="备注" show-overflow-tooltip />
        </el-table>
      </el-tab-pane>

      <!-- 注意事项 -->
      <el-tab-pane label="注意事项" name="attention">
        <div class="attention-section">
          <el-alert
            v-for="(item, index) in attentionPoints"
            :key="index"
            :title="item.title"
            :type="item.type"
            :description="item.description"
            show-icon
            :closable="false"
            style="margin-bottom: 15px;"
          />
        </div>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
import * as echarts from 'echarts'

export default {
  name: 'CompanyDetail',
  props: {
    company: {
      type: Object,
      required: true
    },
    modelId: {
      type: String,
      required: true
    }
  },
  data() {
    return {
      activeTab: 'basic',
      warningList: [],
      dataSourceFlow: [
        {
          timestamp: '步骤1',
          title: '数据采集',
          description: '从业务系统采集原始数据',
          tables: ['bus_capital_stock', 'bus_bank_cash_flow', 'bus_capital_plan']
        },
        {
          timestamp: '步骤2',
          title: '数据清洗',
          description: '过滤无效数据,处理异常值',
          tables: []
        },
        {
          timestamp: '步骤3',
          title: '指标计算',
          description: '根据模型配置计算各项指标',
          tables: []
        },
        {
          timestamp: '步骤4',
          title: '风险评估',
          description: '基于指标值进行风险等级评估',
          tables: []
        },
        {
          timestamp: '步骤5',
          title: '预警生成',
          description: '生成预警信息并推送',
          tables: ['tbl_model_warning']
        }
      ],
      rawDataList: [],
      attentionPoints: [
        {
          title: '资金头寸预警',
          type: 'error',
          description: '当前账户余额充裕度低于阈值,建议及时补充资金或调整资金计划'
        },
        {
          title: '银行流水异常',
          type: 'warning',
          description: '近期银行流水波动较大,需要关注异常交易情况'
        },
        {
          title: '资金计划偏差',
          type: 'info',
          description: '实际资金使用与计划存在偏差,建议优化资金计划编制'
        }
      ],
      trendChart: null
    }
  },
  mounted() {
    this.loadWarningList()
    this.loadRawData()
    this.$nextTick(() => {
      this.initTrendChart()
    })
  },
  beforeDestroy() {
    if (this.trendChart) {
      this.trendChart.dispose()
    }
  },
  methods: {
    loadWarningList() {
      // 模拟数据
      this.warningList = [
        {
          warningTitle: '资金头寸预警',
          warningLevel: '高风险',
          warningContent: '账户余额充裕度低于阈值',
          createTime: new Date(),
          status: 'PENDING'
        },
        {
          warningTitle: '银行流水异常',
          warningLevel: '中风险',
          warningContent: '近期银行流水波动较大',
          createTime: new Date(Date.now() - 86400000),
          status: 'PROCESSING'
        }
      ]
    },
    loadRawData() {
      // 模拟数据
      this.rawDataList = [
        {
          indicatorName: '账户余额充裕度',
          indicatorValue: '0.65',
          threshold: '≥0.8',
          status: '异常',
          remark: '低于阈值,需要关注'
        },
        {
          indicatorName: '可用资金充裕度',
          indicatorValue: '0.72',
          threshold: '≥0.7',
          status: '正常',
          remark: '符合要求'
        },
        {
          indicatorName: '资金周转率',
          indicatorValue: '1.25',
          threshold: '≥1.0',
          status: '正常',
          remark: '资金周转良好'
        }
      ]
    },
    initTrendChart() {
      if (!this.$refs.trendChart) return
      this.trendChart = echarts.init(this.$refs.trendChart)
      
      const option = {
        title: {
          text: '近6个月预警趋势',
          textStyle: { color: '#303133', fontSize: 16 }
        },
        tooltip: {
          trigger: 'axis'
        },
        legend: {
          data: ['高风险', '中风险', '低风险']
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          data: ['6月', '7月', '8月', '9月', '10月', '11月']
        },
        yAxis: {
          type: 'value'
        },
        series: [
          {
            name: '高风险',
            type: 'line',
            data: [2, 3, 4, 5, 6, 7],
            itemStyle: { color: '#f56c6c' }
          },
          {
            name: '中风险',
            type: 'line',
            data: [3, 4, 3, 5, 4, 6],
            itemStyle: { color: '#e6a23c' }
          },
          {
            name: '低风险',
            type: 'line',
            data: [1, 2, 1, 2, 2, 2],
            itemStyle: { color: '#67c23a' }
          }
        ]
      }
      
      this.trendChart.setOption(option)
    },
    getRiskLevelType(level) {
      const typeMap = {
        '高风险': 'danger',
        '中风险': 'warning',
        '低风险': 'success',
        'HIGH': 'danger',
        'MEDIUM': 'warning',
        'LOW': 'success'
      }
      return typeMap[level] || 'info'
    },
    getStatusType(status) {
      const typeMap = {
        'PENDING': 'danger',
        'PROCESSING': 'warning',
        'PROCESSED': 'success',
        'IGNORED': 'info'
      }
      return typeMap[status] || 'info'
    },
    getStatusText(status) {
      const textMap = {
        'PENDING': '待处理',
        'PROCESSING': '处理中',
        'PROCESSED': '已处理',
        'IGNORED': '已忽略'
      }
      return textMap[status] || status
    },
    formatDateTime(time) {
      if (!time) return '-'
      const date = new Date(time)
      return date.toLocaleString('zh-CN')
    }
  }
}
</script>

<style lang="scss" scoped>
.company-detail {
  .info-section {
    padding: 20px;

    .info-item {
      padding: 15px 0;
      border-bottom: 1px solid #ebeef5;

      .label {
        color: #909399;
        margin-right: 10px;
      }

      .value {
        color: #303133;
        font-weight: bold;

        &.warning {
          color: #f56c6c;
        }
      }
    }
  }

  .datasource-section {
    padding: 20px;

    .table-list {
      margin-top: 10px;
    }
  }

  .trend-section {
    padding: 20px;

    .chart-wrapper {
      margin-bottom: 20px;

      .chart-container {
        width: 100%;
        height: 400px;
      }
    }

    .trend-summary {
      ul {
        margin: 10px 0;
        padding-left: 20px;

        li {
          margin: 5px 0;
          color: #606266;
        }
      }
    }
  }

  .attention-section {
    padding: 20px;
  }
}
</style>

