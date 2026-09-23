<template>
  <el-dialog
    title="资产配置优化"
    :visible.sync="dialogVisible"
    width="1200px"
    :before-close="handleClose"
  >
    <div class="optimization-content">
      <el-steps :active="currentStep" finish-status="success" class="mb-20">
        <el-step title="现状分析" description="分析当前配置"></el-step>
        <el-step title="优化建议" description="生成优化方案"></el-step>
        <el-step title="方案确认" description="确认优化方案"></el-step>
      </el-steps>
      
      <div v-if="currentStep === 0" class="step-content">
        <el-card shadow="never">
          <div slot="header">
            <span>当前资产配置分析</span>
          </div>
          <el-row :gutter="20">
            <el-col :span="12">
              <div class="chart-container">
                <div ref="currentAllocationChart" style="height: 300px;"></div>
              </div>
            </el-col>
            <el-col :span="12">
              <el-table :data="currentAllocation" border>
                <el-table-column label="资产类型" prop="type" />
                <el-table-column label="金额(万元)" prop="amount" align="center" />
                <el-table-column label="占比" prop="percentage" align="center" />
                <el-table-column label="收益率" prop="returnRate" align="center" />
              </el-table>
            </el-col>
          </el-row>
        </el-card>
      </div>
      
      <div v-if="currentStep === 1" class="step-content">
        <el-card shadow="never">
          <div slot="header">
            <span>AI智能优化建议</span>
            <el-button
              style="float: right; padding: 3px 0"
              type="text"
              @click="generateOptimization"
              :loading="optimizing"
            >
              重新生成
            </el-button>
          </div>
          <el-row :gutter="20">
            <el-col :span="12">
              <div class="optimization-comparison">
                <h4>优化前后对比</h4>
                <div ref="comparisonChart" style="height: 300px;"></div>
              </div>
            </el-col>
            <el-col :span="12">
              <div class="optimization-suggestions">
                <h4>优化建议</h4>
                <el-table :data="optimizationSuggestions" border>
                  <el-table-column label="调整项" prop="item" />
                  <el-table-column label="当前配置" prop="current" align="center" />
                  <el-table-column label="建议配置" prop="suggested" align="center" />
                  <el-table-column label="预期收益" prop="expectedReturn" align="center" />
                </el-table>
              </div>
            </el-col>
          </el-row>
        </el-card>
      </div>
      
      <div v-if="currentStep === 2" class="step-content">
        <el-card shadow="never">
          <div slot="header">
            <span>优化方案确认</span>
          </div>
          <el-form ref="confirmForm" :model="confirmForm" label-width="120px">
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="实施时间">
                  <el-date-picker
                    v-model="confirmForm.implementDate"
                    type="date"
                    placeholder="选择实施时间"
                    style="width: 100%;"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="实施方式">
                  <el-select v-model="confirmForm.implementMethod" placeholder="请选择实施方式" style="width: 100%;">
                    <el-option label="一次性调整" value="ONE_TIME"></el-option>
                    <el-option label="分阶段调整" value="PHASED"></el-option>
                    <el-option label="渐进式调整" value="GRADUAL"></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            
            <el-form-item label="风险承受度">
              <el-radio-group v-model="confirmForm.riskTolerance">
                <el-radio label="LOW">低风险</el-radio>
                <el-radio label="MEDIUM">中等风险</el-radio>
                <el-radio label="HIGH">高风险</el-radio>
              </el-radio-group>
            </el-form-item>
            
            <el-form-item label="预期目标">
              <el-checkbox-group v-model="confirmForm.objectives">
                <el-checkbox label="MAXIMIZE_RETURN">收益最大化</el-checkbox>
                <el-checkbox label="MINIMIZE_RISK">风险最小化</el-checkbox>
                <el-checkbox label="IMPROVE_LIQUIDITY">提升流动性</el-checkbox>
                <el-checkbox label="OPTIMIZE_STRUCTURE">优化结构</el-checkbox>
              </el-checkbox-group>
            </el-form-item>
            
            <el-form-item label="备注说明">
              <el-input
                v-model="confirmForm.notes"
                type="textarea"
                :rows="3"
                placeholder="请输入备注说明"
              />
            </el-form-item>
          </el-form>
          
          <div class="optimization-summary">
            <h4>优化效果预测</h4>
            <el-row :gutter="20">
              <el-col :span="6">
                <div class="summary-item">
                  <div class="summary-value">{{ optimizationPrediction.yieldImprovement || '-' }}</div>
                  <div class="summary-label">预期收益提升</div>
                </div>
              </el-col>
              <el-col :span="6">
                <div class="summary-item">
                  <div class="summary-value">{{ optimizationPrediction.riskReduction || '-' }}</div>
                  <div class="summary-label">风险降低</div>
                </div>
              </el-col>
              <el-col :span="6">
                <div class="summary-item">
                  <div class="summary-value">{{ optimizationPrediction.liquidityImprovement || '-' }}</div>
                  <div class="summary-label">流动性提升</div>
                </div>
              </el-col>
              <el-col :span="6">
                <div class="summary-item">
                  <div class="summary-value">{{ optimizationPrediction.reasonabilityScore || '-' }}</div>
                  <div class="summary-label">配置合理度</div>
                </div>
              </el-col>
            </el-row>
          </div>
        </el-card>
      </div>
    </div>
    
    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取消</el-button>
      <el-button v-if="currentStep > 0" @click="prevStep">上一步</el-button>
      <el-button v-if="currentStep < 2" type="primary" @click="nextStep">下一步</el-button>
      <el-button v-if="currentStep === 2" type="primary" @click="handleConfirm">确认优化</el-button>
    </div>
  </el-dialog>
</template>

<script>
import * as echarts from 'echarts'
import { getAssetAllocationOptimizationSuggestions, updateAssetAllocation } from '@/api/stateAssets/assetAllocation'

export default {
  name: 'AssetOptimizationDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    allocationData: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      currentStep: 0,
      optimizing: false,
      loading: false,
      currentAllocation: [],
      optimizationSuggestions: [],
      optimizationPrediction: {},
      confirmForm: {
        implementDate: '',
        implementMethod: '',
        riskTolerance: 'MEDIUM',
        objectives: [],
        notes: ''
      },
      // 图表实例
      currentAllocationChart: null,
      comparisonChart: null
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
    }
  },
  watch: {
    visible(val) {
      if (val) {
        this.initData()
        this.$nextTick(() => {
          this.initCharts()
        })
      }
    }
  },
  beforeDestroy() {
    this.destroyCharts()
  },
  methods: {
    async initData() {
      this.currentStep = 0
      this.loadCurrentAllocation()
      await this.loadOptimizationSuggestions()
    },

    loadCurrentAllocation() {
      const data = this.allocationData
      this.currentAllocation = [
        { type: this.getAssetTypeText(data.assetType), amount: data.assetValue || data.assetAmount || 0, percentage: (data.allocationRatio || 0) + '%', returnRate: (data.allocationEfficiency || data.yieldRate || 0) + '%' }
      ]
    },

    async loadOptimizationSuggestions() {
      this.loading = true
      try {
        const response = await getAssetAllocationOptimizationSuggestions({
          allocationId: this.allocationData.allocationId
        })
        if (response && response.result === 200 && response.data) {
          const data = response.data
          this.optimizationSuggestions = data.suggestions || []
          this.optimizationPrediction = data.prediction || {}
          if (data.currentConfig) {
            this.currentAllocation = [{
              type: this.getAssetTypeText(data.currentConfig.assetType),
              amount: data.currentConfig.assetAmount || 0,
              percentage: (data.currentConfig.allocationRatio || 0) + '%',
              returnRate: (data.currentConfig.yieldRate || 0) + '%'
            }]
          }
        } else {
          this.optimizationSuggestions = []
          this.optimizationPrediction = {}
        }
      } catch (error) {
        console.error('获取优化建议失败:', error)
        this.optimizationSuggestions = []
        this.optimizationPrediction = {}
      } finally {
        this.loading = false
      }
    },

    getAssetTypeText(type) {
      const map = { 'FIXED_ASSETS': '固定资产', 'CURRENT_ASSETS': '流动资产', 'INTANGIBLE_ASSETS': '无形资产', 'INVESTMENT_ASSETS': '投资性资产', 'FINANCIAL_ASSETS': '金融资产' }
      return map[type] || type || '-'
    },
    
    initCharts() {
      this.currentAllocationChart = echarts.init(this.$refs.currentAllocationChart)
      this.updateCurrentAllocationChart()
      
      window.addEventListener('resize', this.handleResize)
    },
    
    updateCurrentAllocationChart() {
      const option = {
        title: {
          text: '当前资产配置',
          left: 'center',
          textStyle: { fontSize: 14 }
        },
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b}: {c}万元 ({d}%)'
        },
        series: [
          {
            name: '资产配置',
            type: 'pie',
            radius: '60%',
            data: this.currentAllocation.map(item => ({
              name: item.type,
              value: item.amount
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
      }
      this.currentAllocationChart.setOption(option)
    },
    
    updateComparisonChart() {
      if (!this.comparisonChart) {
        this.comparisonChart = echarts.init(this.$refs.comparisonChart)
      }
      // 从优化建议中提取对比数据
      const items = this.optimizationSuggestions.map(s => s.item || '-')
      const currentValues = this.optimizationSuggestions.map(s => parseFloat(s.current) || 0)
      const suggestedValues = this.optimizationSuggestions.map(s => parseFloat(s.suggested) || 0)

      const option = {
        title: {
          text: '优化前后对比',
          left: 'center',
          textStyle: { fontSize: 14 }
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: { type: 'shadow' }
        },
        legend: {
          data: ['当前值', '建议值'],
          bottom: 10
        },
        xAxis: {
          type: 'category',
          data: items,
          axisLabel: { rotate: 15 }
        },
        yAxis: {
          type: 'value',
          name: '数值'
        },
        series: [
          {
            name: '当前值',
            type: 'bar',
            data: currentValues,
            itemStyle: { color: '#409EFF' }
          },
          {
            name: '建议值',
            type: 'bar',
            data: suggestedValues,
            itemStyle: { color: '#67C23A' }
          }
        ]
      }
      this.comparisonChart.setOption(option)
    },
    
    async generateOptimization() {
      this.optimizing = true
      try {
        const response = await getAssetAllocationOptimizationSuggestions({
          allocationId: this.allocationData.allocationId
        })
        if (response && response.result === 200 && response.data) {
          const data = response.data
          this.optimizationSuggestions = data.suggestions || []
          this.optimizationPrediction = data.prediction || {}
        }
        this.$message.success('优化方案已重新生成')
        this.$nextTick(() => {
          this.updateComparisonChart()
        })
      } catch (error) {
        this.$message.error('生成优化方案失败')
      } finally {
        this.optimizing = false
      }
    },
    
    nextStep() {
      if (this.currentStep < 2) {
        this.currentStep++
        if (this.currentStep === 1) {
          this.$nextTick(() => {
            this.updateComparisonChart()
          })
        }
      }
    },
    
    prevStep() {
      if (this.currentStep > 0) {
        this.currentStep--
      }
    },
    
    handleConfirm() {
      this.$confirm('确认执行优化方案？', '确认', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'info'
      }).then(async () => {
        try {
          const response = await updateAssetAllocation({
            allocationId: this.allocationData.allocationId,
            allocationStatus: 'OPTIMAL',
            remark: '已执行优化方案 - ' + (this.confirmForm.implementMethod || '一次性调整')
          })
          if (response && response.result === 200) {
            this.$message.success('优化方案已确认并执行')
            this.$emit('refresh')
            this.handleClose()
          } else {
            this.$message.error(response.msg || '优化执行失败')
          }
        } catch (error) {
          this.$message.error('优化执行失败')
        }
      })
    },
    
    handleClose() {
      this.$emit('update:visible', false)
      this.currentStep = 0
      this.destroyCharts()
    },
    
    handleResize() {
      if (this.currentAllocationChart) this.currentAllocationChart.resize()
      if (this.comparisonChart) this.comparisonChart.resize()
    },
    
    destroyCharts() {
      if (this.currentAllocationChart) {
        this.currentAllocationChart.dispose()
        this.currentAllocationChart = null
      }
      if (this.comparisonChart) {
        this.comparisonChart.dispose()
        this.comparisonChart = null
      }
      window.removeEventListener('resize', this.handleResize)
    }
  }
}
</script>

<style scoped>
.optimization-content {
  min-height: 500px;
}

.step-content {
  margin-top: 20px;
}

.chart-container {
  padding: 20px;
}

.optimization-comparison h4,
.optimization-suggestions h4 {
  margin: 0 0 15px 0;
  color: #303133;
}

.optimization-summary {
  margin-top: 30px;
  padding: 20px;
  background: #f5f7fa;
  border-radius: 4px;
}

.optimization-summary h4 {
  margin: 0 0 20px 0;
  color: #303133;
  text-align: center;
}

.summary-item {
  text-align: center;
  padding: 20px;
}

.summary-value {
  font-size: 24px;
  font-weight: bold;
  color: #67C23A;
  margin-bottom: 8px;
}

.summary-label {
  color: #606266;
  font-size: 14px;
}

.mb-20 {
  margin-bottom: 20px;
}
</style>
