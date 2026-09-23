<template>
  <el-dialog
    title="股权变动影响分析"
    :visible.sync="dialogVisible"
    width="90%"
    :before-close="handleClose"
    class="impact-analysis-dialog"
  >
    <div v-loading="loading">
      <!-- 基本信息 -->
      <el-card shadow="never" class="mb-20">
        <div slot="header">
          <span>变动基本信息</span>
        </div>
        <el-descriptions :column="3" border>
          <el-descriptions-item label="企业名称">{{ changeData.investeeEnterpriseName }}</el-descriptions-item>
          <el-descriptions-item label="变动类型">
            <el-tag :type="getChangeTypeTag(changeData.changeType)" size="mini">
              {{ getChangeTypeText(changeData.changeType) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="变动日期">{{ changeData.changeDate }}</el-descriptions-item>
          <el-descriptions-item label="变动前比例">{{ changeData.beforeShareholdingRatio }}%</el-descriptions-item>
          <el-descriptions-item label="变动后比例">{{ changeData.afterShareholdingRatio }}%</el-descriptions-item>
          <el-descriptions-item label="变动幅度">
            <span :class="computedChangeRatio >= 0 ? 'text-success' : 'text-danger'">
              {{ computedChangeRatio > 0 ? '+' : '' }}{{ computedChangeRatio }}%
            </span>
          </el-descriptions-item>
        </el-descriptions>
      </el-card>

      <!-- 影响分析结果 -->
      <el-row :gutter="20">
        <!-- 控制权影响 -->
        <el-col :span="12">
          <el-card shadow="never" class="mb-20">
            <div slot="header">
              <span>控制权影响分析</span>
            </div>
            <div class="impact-content">
              <div class="impact-item">
                <div class="impact-label">控制权变化:</div>
                <div class="impact-value">
                  <el-tag :type="analysisResult.controlImpact?.level === 'HIGH' ? 'danger' : 'success'" size="mini">
                    {{ analysisResult.controlImpact?.description || '无重大影响' }}
                  </el-tag>
                </div>
              </div>
              <div class="impact-item">
                <div class="impact-label">决策权影响:</div>
                <div class="impact-value">{{ analysisResult.controlImpact?.decisionImpact || '无影响' }}</div>
              </div>
              <div class="impact-item">
                <div class="impact-label">治理结构影响:</div>
                <div class="impact-value">{{ analysisResult.controlImpact?.governanceImpact || '无影响' }}</div>
              </div>
            </div>
          </el-card>
        </el-col>

        <!-- 财务影响 -->
        <el-col :span="12">
          <el-card shadow="never" class="mb-20">
            <div slot="header">
              <span>财务影响分析</span>
            </div>
            <div class="impact-content">
              <div class="impact-item">
                <div class="impact-label">资产影响:</div>
                <div class="impact-value">{{ analysisResult.financialImpact?.assetImpact || '无影响' }}</div>
              </div>
              <div class="impact-item">
                <div class="impact-label">负债影响:</div>
                <div class="impact-value">{{ analysisResult.financialImpact?.liabilityImpact || '无影响' }}</div>
              </div>
              <div class="impact-item">
                <div class="impact-label">现金流影响:</div>
                <div class="impact-value">{{ analysisResult.financialImpact?.cashFlowImpact || '无影响' }}</div>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>

      <!-- 风险评估 -->
      <el-card shadow="never" class="mb-20">
        <div slot="header">
          <span>风险评估</span>
        </div>
        <el-row :gutter="20">
          <el-col :span="8">
            <div class="risk-item">
              <div class="risk-title">合规风险</div>
              <div class="risk-level">
                <el-progress
                  :percentage="analysisResult.riskAssessment?.complianceRisk || 0"
                  :color="getRiskColor(analysisResult.riskAssessment?.complianceRisk)"
                  :show-text="false"
                />
                <span class="risk-value">{{ analysisResult.riskAssessment?.complianceRisk || 0 }}%</span>
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="risk-item">
              <div class="risk-title">操作风险</div>
              <div class="risk-level">
                <el-progress
                  :percentage="analysisResult.riskAssessment?.operationalRisk || 0"
                  :color="getRiskColor(analysisResult.riskAssessment?.operationalRisk)"
                  :show-text="false"
                />
                <span class="risk-value">{{ analysisResult.riskAssessment?.operationalRisk || 0 }}%</span>
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="risk-item">
              <div class="risk-title">市场风险</div>
              <div class="risk-level">
                <el-progress
                  :percentage="analysisResult.riskAssessment?.marketRisk || 0"
                  :color="getRiskColor(analysisResult.riskAssessment?.marketRisk)"
                  :show-text="false"
                />
                <span class="risk-value">{{ analysisResult.riskAssessment?.marketRisk || 0 }}%</span>
              </div>
            </div>
          </el-col>
        </el-row>
      </el-card>

      <!-- 影响图表 -->
      <el-card shadow="never" class="mb-20">
        <div slot="header">
          <span>股权结构变化图</span>
        </div>
        <div ref="impactChart" class="impact-chart" style="height: 400px;"></div>
      </el-card>

      <!-- 建议措施 -->
      <el-card shadow="never">
        <div slot="header">
          <span>建议措施</span>
        </div>
        <el-timeline>
          <el-timeline-item
            v-for="(recommendation, index) in analysisResult.recommendations"
            :key="index"
            :type="getRecommendationType(recommendation.priority)"
            :icon="getRecommendationIcon(recommendation.priority)"
          >
            <div class="recommendation-item">
              <div class="recommendation-title">{{ recommendation.title }}</div>
              <div class="recommendation-content">{{ recommendation.content }}</div>
              <div class="recommendation-priority">
                <el-tag :type="getRecommendationType(recommendation.priority)" size="mini">
                  {{ recommendation.priority }}
                </el-tag>
              </div>
            </div>
          </el-timeline-item>
        </el-timeline>
      </el-card>
    </div>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">关闭</el-button>
      <el-button type="primary" @click="handleExportReport">导出报告</el-button>
    </div>
  </el-dialog>
</template>

<script>
import * as echarts from 'echarts'
import { analyzeEquityChangeImpact, getEquityChangeImpactReport } from '@/api/stateAssets/equityChanges'

export default {
  name: 'EquityImpactAnalysisDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    changeData: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      loading: false,
      chartInstance: null,
      analysisResult: {
        controlImpact: {},
        financialImpact: {},
        riskAssessment: {},
        recommendations: []
      }
    }
  },
  computed: {
    dialogVisible: {
      get() {
        return this.visible
      },
      set(val) {
        this.$emit('update:visible', val)
      }
    },
    computedChangeRatio() {
      const before = Number(this.changeData.beforeShareholdingRatio) || 0
      const after = Number(this.changeData.afterShareholdingRatio) || 0
      return (after - before).toFixed(2)
    }
  },
  watch: {
    visible(val) {
      if (val && this.changeData.changeId) {
        this.loadAnalysisData()
        this.$nextTick(() => {
          this.initChart()
        })
      }
    }
  },
  beforeDestroy() {
    if (this.chartInstance) {
      this.chartInstance.dispose()
    }
  },
  methods: {
    // 加载分析数据
    async loadAnalysisData() {
      this.loading = true
      try {
        const response = await analyzeEquityChangeImpact({
          changeId: this.changeData.changeId
        })
        if (response && response.result === 200 && response.data) {
          const data = response.data
          const before = Number(this.changeData.beforeShareholdingRatio) || 0
          const after = Number(this.changeData.afterShareholdingRatio) || 0
          const changeRatio = after - before
          const isControlChange = data.controlChange && data.controlChange !== '无变化'

          this.analysisResult = {
            controlImpact: {
              level: isControlChange ? 'HIGH' : 'LOW',
              description: data.controlChange || '无重大影响',
              decisionImpact: Math.abs(changeRatio) >= 10 ? '决策权发生变化' : '无影响',
              governanceImpact: isControlChange ? '治理结构需调整' : '无影响'
            },
            financialImpact: {
              assetImpact: data.changeAmount ? `涉及金额 ${this.changeData.changeAmount || 0} 万元` : '无影响',
              liabilityImpact: this.changeData.changeType === 'PLEDGE' ? '质押可能影响负债结构' : '无影响',
              cashFlowImpact: Math.abs(changeRatio) >= 20 ? '现金流可能受影响' : '无影响'
            },
            riskAssessment: {
              complianceRisk: data.riskAssessment === 'HIGH' ? 80 : data.riskAssessment === 'MEDIUM' ? 50 : 20,
              operationalRisk: isControlChange ? 60 : 20,
              marketRisk: Math.abs(changeRatio) >= 30 ? 70 : Math.abs(changeRatio) >= 10 ? 40 : 15
            },
            recommendations: [
              { title: '合规审查', content: '建议对本次股权变动进行合规性审查', priority: data.riskAssessment === 'HIGH' ? '高' : '中' },
              { title: '信息披露', content: Math.abs(changeRatio) >= 5 ? '建议及时进行信息披露' : '变动幅度较小，按常规披露', priority: Math.abs(changeRatio) >= 10 ? '高' : '低' },
              { title: '后续监控', content: '建议持续关注后续股权变动情况', priority: '中' }
            ],
            beforeStructure: [
              { name: this.changeData.transferorEnterpriseName || '转让方', value: before },
              { name: '其他股东', value: (100 - before).toFixed(2) }
            ],
            afterStructure: [
              { name: this.changeData.transfereeEnterpriseName || '受让方', value: after },
              { name: '其他股东', value: (100 - after).toFixed(2) }
            ]
          }
        } else {
          this.$message.warning('暂无分析数据')
        }
        this.renderChart()
      } catch (error) {
        console.error('加载分析数据失败:', error)
        this.$message.error('加载分析数据失败')
      } finally {
        this.loading = false
      }
    },

    // 初始化图表
    initChart() {
      if (this.chartInstance) {
        this.chartInstance.dispose()
      }
      
      const chartDom = this.$refs.impactChart
      this.chartInstance = echarts.init(chartDom)
    },

    // 渲染图表
    renderChart() {
      if (!this.chartInstance) return

      const option = {
        title: {
          text: '股权结构变化对比',
          left: 'center'
        },
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b}: {c}%'
        },
        legend: {
          orient: 'vertical',
          left: 'left'
        },
        series: [
          {
            name: '变动前',
            type: 'pie',
            radius: ['20%', '40%'],
            center: ['25%', '60%'],
            data: this.analysisResult.beforeStructure || [],
            label: {
              show: true,
              formatter: '{b}: {c}%'
            }
          },
          {
            name: '变动后',
            type: 'pie',
            radius: ['20%', '40%'],
            center: ['75%', '60%'],
            data: this.analysisResult.afterStructure || [],
            label: {
              show: true,
              formatter: '{b}: {c}%'
            }
          }
        ]
      }

      this.chartInstance.setOption(option)
    },

    // 获取变动类型标签
    getChangeTypeTag(type) {
      const tagMap = {
        'TRANSFER': 'primary',
        'INCREASE': 'success',
        'DECREASE': 'warning',
        'PLEDGE': 'info',
        'UNPLEDGE': 'success'
      }
      return tagMap[type] || 'info'
    },

    // 获取变动类型文本
    getChangeTypeText(type) {
      const textMap = {
        'TRANSFER': '股权转让',
        'INCREASE': '增资扩股',
        'DECREASE': '减资',
        'PLEDGE': '股权质押',
        'UNPLEDGE': '股权解押'
      }
      return textMap[type] || type
    },

    // 获取风险颜色
    getRiskColor(risk) {
      if (risk >= 70) return '#f56c6c'
      if (risk >= 40) return '#e6a23c'
      return '#67c23a'
    },

    // 获取建议类型
    getRecommendationType(priority) {
      const typeMap = {
        '高': 'danger',
        '中': 'warning',
        '低': 'success'
      }
      return typeMap[priority] || 'info'
    },

    // 获取建议图标
    getRecommendationIcon(priority) {
      const iconMap = {
        '高': 'el-icon-warning',
        '中': 'el-icon-info',
        '低': 'el-icon-success'
      }
      return iconMap[priority] || 'el-icon-info'
    },

    // 导出报告
    async handleExportReport() {
      try {
        const response = await getEquityChangeImpactReport({
          changeId: this.changeData.changeId
        })
        
        const blob = new Blob([response.data])
        const link = document.createElement('a')
        link.href = URL.createObjectURL(blob)
        link.download = `${this.changeData.enterpriseName}_股权变动影响分析报告.pdf`
        link.click()
        
        this.$message.success('报告导出成功')
      } catch (error) {
        this.$message.error('报告导出失败')
      }
    },

    // 关闭对话框
    handleClose() {
      this.dialogVisible = false
    }
  }
}
</script>

<style scoped>
.impact-analysis-dialog {
  .mb-20 {
    margin-bottom: 20px;
  }

  .impact-content {
    .impact-item {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 10px;
      
      .impact-label {
        font-weight: bold;
        color: #606266;
      }
      
      .impact-value {
        color: #303133;
      }
    }
  }

  .risk-item {
    text-align: center;
    
    .risk-title {
      font-weight: bold;
      margin-bottom: 10px;
      color: #606266;
    }
    
    .risk-level {
      display: flex;
      align-items: center;
      justify-content: center;
      
      .risk-value {
        margin-left: 10px;
        font-weight: bold;
      }
    }
  }

  .recommendation-item {
    .recommendation-title {
      font-weight: bold;
      margin-bottom: 5px;
      color: #303133;
    }
    
    .recommendation-content {
      color: #606266;
      margin-bottom: 5px;
    }
    
    .recommendation-priority {
      text-align: right;
    }
  }

  .text-success {
    color: #67c23a;
  }

  .text-danger {
    color: #f56c6c;
  }

  .dialog-footer {
    text-align: right;
  }
}
</style>
