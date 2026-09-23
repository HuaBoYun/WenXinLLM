<template>
  <el-dialog
    title="资金流向预测"
    :visible.sync="dialogVisible"
    width="80%"
    :before-close="handleClose"
  >
    <div v-loading="loading" class="predict-dialog-container">
      <!-- 预测参数 -->
      <el-card class="mb-20">
        <div slot="header">
          <span>预测参数设置</span>
        </div>
        <el-form :model="predictParams" label-width="120px">
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="预测周期">
                <el-select v-model="predictParams.period" placeholder="请选择预测周期">
                  <el-option label="1个月" value="1M"></el-option>
                  <el-option label="3个月" value="3M"></el-option>
                  <el-option label="6个月" value="6M"></el-option>
                  <el-option label="1年" value="1Y"></el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="预测模型">
                <el-select v-model="predictParams.model" placeholder="请选择预测模型">
                  <el-option label="线性回归" value="LINEAR"></el-option>
                  <el-option label="时间序列" value="TIMESERIES"></el-option>
                  <el-option label="神经网络" value="NEURAL"></el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
      </el-card>

      <!-- 预测结果 -->
      <el-card class="mb-20">
        <div slot="header">
          <span>预测结果</span>
        </div>
        <el-row :gutter="20" class="mb-20">
          <el-col :span="12">
            <div ref="predictChart" style="height: 350px;"></div>
          </el-col>
          <el-col :span="12">
            <el-table :data="predictResults" stripe border size="small">
              <el-table-column prop="period" label="预测期" width="100" align="center" />
              <el-table-column prop="predictAmount" label="预测金额" width="120" align="center" />
              <el-table-column prop="confidence" label="置信度" width="100" align="center">
                <template slot-scope="scope">
                  <el-progress :percentage="scope.row.confidence" :show-text="false" />
                  <span style="margin-left: 5px;">{{ scope.row.confidence }}%</span>
                </template>
              </el-table-column>
              <el-table-column prop="riskLevel" label="风险等级" width="100" align="center">
                <template slot-scope="scope">
                  <el-tag :type="getRiskLevelType(scope.row.riskLevel)">
                    {{ scope.row.riskLevel }}
                  </el-tag>
                </template>
              </el-table-column>
            </el-table>
          </el-col>
        </el-row>
      </el-card>

      <!-- 预测建议 -->
      <el-card>
        <div slot="header">
          <span>预测建议</span>
        </div>
        <el-timeline>
          <el-timeline-item
            v-for="(item, index) in predictAdvice"
            :key="index"
            :timestamp="item.time"
            placement="top"
          >
            <p><strong>{{ item.title }}</strong></p>
            <p>{{ item.content }}</p>
          </el-timeline-item>
        </el-timeline>
      </el-card>
    </div>

    <div slot="footer" class="dialog-footer">
      <el-button type="primary" @click="executePrediction">执行预测</el-button>
      <el-button @click="handleClose">关闭</el-button>
    </div>
  </el-dialog>
</template>

<script>
import * as echarts from 'echarts'
import { predictFundFlow } from '@/api/stateAssets/fundFlow'

export default {
  name: 'FundFlowPredictDialog',
  props: {
    visible: { type: Boolean, default: false },
    data: { type: Object, default: () => ({}) }
  },
  data() {
    return {
      loading: false,
      predictParams: {
        period: '3M',
        model: 'TIMESERIES'
      },
      predictResults: [],
      predictAdvice: [],
      predictChart: null
    }
  },
  computed: {
    dialogVisible: {
      get() { return this.visible },
      set(val) { this.$emit('update:visible', val) }
    }
  },
  watch: {
    visible(val) {
      if (val) {
        this.$nextTick(() => {
          this.initChart()
          this.loadPredictData()
        })
      }
    }
  },
  beforeDestroy() {
    if (this.predictChart) {
      this.predictChart.dispose()
    }
  },
  methods: {
    initChart() {
      if (this.$refs.predictChart) {
        this.predictChart = echarts.init(this.$refs.predictChart)
      }
    },
    async loadPredictData() {
      this.loading = true
      try {
        const params = {
          fundFlowId: this.data.fundFlowId || this.data.id || this.data.partyId,
          period: this.predictParams.period
        }
        const response = await predictFundFlow(params)
        const resData = response.data || {}

        this.predictResults = resData.predictResults || []
        this.predictAdvice = resData.advice || []

        this.$nextTick(() => {
          this.drawPredictChart(resData)
        })
      } catch (error) {
        console.error('资金流向预测失败', error)
        this.$message.error('预测数据加载失败')
        this.predictResults = []
        this.predictAdvice = []
      } finally {
        this.loading = false
      }
    },
    drawPredictChart(resData) {
      if (!this.predictChart) {
        this.initChart()
      }
      if (!this.predictChart) return

      const historyMonths = (resData && resData.historyMonths) || []
      const historyValues = (resData && resData.historyValues) || []
      const predictResults = (resData && resData.predictResults) || []

      const predictLabels = predictResults.map(item => item.period)
      const predictValues = predictResults.map(item => item.predictAmount)

      // 合并X轴：历史月份 + 预测期
      const xAxisData = [...historyMonths, ...predictLabels]

      // 历史数据线：历史值 + 预测区域填null
      const historyLine = [...historyValues, ...predictLabels.map(() => null)]

      // 预测数据线：历史区域填null（最后一个历史值作为连接点） + 预测值
      const predictionLine = [
        ...historyValues.slice(0, -1).map(() => null),
        historyValues.length > 0 ? historyValues[historyValues.length - 1] : null,
        ...predictValues
      ]

      const option = {
        title: { text: '资金流向预测趋势', left: 'center' },
        tooltip: { trigger: 'axis' },
        legend: { data: ['历史数据', '预测数据'] },
        xAxis: {
          type: 'category',
          data: xAxisData
        },
        yAxis: { type: 'value' },
        series: [
          {
            name: '历史数据',
            data: historyLine,
            type: 'line',
            smooth: true,
            itemStyle: { color: '#409EFF' }
          },
          {
            name: '预测数据',
            data: predictionLine,
            type: 'line',
            smooth: true,
            itemStyle: { color: '#67C23A' },
            lineStyle: { type: 'dashed' }
          }
        ]
      }
      this.predictChart.setOption(option, true)
    },
    getRiskLevelType(level) {
      const typeMap = {
        '低': 'success',
        '中': 'warning',
        '高': 'danger'
      }
      return typeMap[level] || 'info'
    },
    async executePrediction() {
      await this.loadPredictData()
      if (this.predictResults.length > 0) {
        this.$message.success('预测已执行')
      }
    },
    handleClose() {
      this.dialogVisible = false
    }
  }
}
</script>

<style scoped>
.predict-dialog-container {
  padding: 10px;
}
.mb-20 {
  margin-bottom: 20px;
}
.dialog-footer {
  text-align: right;
}
</style>