<template>
  <div class="special-analysis-container">
    <!-- 分析条件 -->
    <el-card shadow="never" class="condition-card">
      <div slot="header" class="card-header">
        <span>分析条件设置</span>
      </div>
      <el-form
        ref="conditionForm"
        :model="conditionForm"
        :inline="true"
        label-width="100px"
      >
        <el-form-item label="分析类型">
          <el-select
            v-model="conditionForm.analysisType"
            placeholder="请选择分析类型"
            style="width: 200px"
            @change="handleAnalysisTypeChange"
          >
            <el-option label="综合分析" value="comprehensive" />
            <el-option label="对比分析" value="compare" />
            <el-option label="趋势分析" value="trend" />
            <el-option label="结构分析" value="structure" />
          </el-select>
        </el-form-item>
        <el-form-item label="分析维度">
          <el-select
            v-model="conditionForm.dimension"
            placeholder="请选择分析维度"
            style="width: 200px"
            multiple
          >
            <el-option label="项目成本" value="project" />
            <el-option label="作业成本" value="activity" />
            <el-option label="质量成本" value="quality" />
            <el-option label="环境成本" value="environment" />
            <el-option label="研发成本" value="rd" />
          </el-select>
        </el-form-item>
        <el-form-item label="时间范围">
          <el-date-picker
            v-model="conditionForm.dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            format="yyyy-MM-dd"
            value-format="yyyy-MM-dd"
          />
        </el-form-item>
        <el-form-item>
          <el-button
            type="primary"
            icon="el-icon-data-analysis"
            @click="performAnalysis"
          >
            开始分析
          </el-button>
          <el-button @click="resetCondition">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 分析结果 -->
    <div v-if="analysisResult" v-loading="analysisLoading">
      <!-- 综合概览 -->
      <el-card shadow="never" class="overview-card">
        <div slot="header" class="card-header">
          <span>专项成本综合概览</span>
          <div class="header-actions">
            <el-button
              size="small"
              icon="el-icon-download"
              @click="exportAnalysis"
            >
              导出报告
            </el-button>
          </div>
        </div>
        <el-row :gutter="20">
          <el-col :span="4" v-for="(item, index) in overviewStats" :key="index">
            <div class="overview-item" :class="item.type">
              <div class="overview-icon">
                <i :class="item.icon"></i>
              </div>
              <div class="overview-content">
                <div class="overview-title">{{ item.title }}</div>
                <div class="overview-value">{{ item.value }}</div>
                <div class="overview-change" :class="item.changeClass">
                  <i :class="item.changeIcon"></i>
                  {{ item.change }}
                </div>
              </div>
            </div>
          </el-col>
        </el-row>
      </el-card>

      <!-- 图表分析 -->
      <el-row :gutter="20">
        <el-col :span="12">
          <el-card shadow="never" class="chart-card">
            <div slot="header" class="card-header">
              <span>成本结构分析</span>
            </div>
            <div id="costStructureChart" style="height: 400px;"></div>
          </el-card>
        </el-col>
        <el-col :span="12">
          <el-card shadow="never" class="chart-card">
            <div slot="header" class="card-header">
              <span>成本趋势分析</span>
            </div>
            <div id="costTrendChart" style="height: 400px;"></div>
          </el-card>
        </el-col>
      </el-row>

      <!-- 对比分析 -->
      <el-card shadow="never" class="compare-card" v-if="conditionForm.analysisType === 'compare'">
        <div slot="header" class="card-header">
          <span>对比分析结果</span>
        </div>
        <el-table
          :data="compareData"
          border
          style="width: 100%"
        >
          <el-table-column
            prop="dimension"
            label="成本维度"
            width="150"
          >
            <template slot-scope="scope">
              <el-tag :type="getDimensionTag(scope.row.dimension)">
                {{ getDimensionName(scope.row.dimension) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column
            prop="currentAmount"
            label="当期金额"
            width="120"
            align="right"
          >
            <template slot-scope="scope">
              {{ formatAmount(scope.row.currentAmount) }}
            </template>
          </el-table-column>
          <el-table-column
            prop="compareAmount"
            label="对比期金额"
            width="120"
            align="right"
          >
            <template slot-scope="scope">
              {{ formatAmount(scope.row.compareAmount) }}
            </template>
          </el-table-column>
          <el-table-column
            prop="changeAmount"
            label="变化金额"
            width="120"
            align="right"
          >
            <template slot-scope="scope">
              <span :class="getChangeClass(scope.row.changeAmount)">
                {{ formatAmount(scope.row.changeAmount) }}
              </span>
            </template>
          </el-table-column>
          <el-table-column
            prop="changeRate"
            label="变化率"
            width="100"
            align="right"
          >
            <template slot-scope="scope">
              <span :class="getChangeClass(scope.row.changeAmount)">
                {{ scope.row.changeRate }}%
              </span>
            </template>
          </el-table-column>
          <el-table-column
            prop="analysis"
            label="变化分析"
            show-overflow-tooltip
          />
        </el-table>
      </el-card>

      <!-- 分析结论 -->
      <el-card shadow="never" class="conclusion-card">
        <div slot="header" class="card-header">
          <span>分析结论与建议</span>
        </div>
        <div class="conclusion-content">
          <div class="conclusion-section">
            <h4>主要发现</h4>
            <ul>
              <li v-for="finding in analysisResult.keyFindings" :key="finding">
                {{ finding }}
              </li>
            </ul>
          </div>
          <div class="conclusion-section">
            <h4>风险提示</h4>
            <ul>
              <li v-for="risk in analysisResult.riskWarnings" :key="risk" class="risk-item">
                <i class="el-icon-warning"></i>
                {{ risk }}
              </li>
            </ul>
          </div>
          <div class="conclusion-section">
            <h4>优化建议</h4>
            <ul>
              <li v-for="suggestion in analysisResult.suggestions" :key="suggestion" class="suggestion-item">
                <i class="el-icon-success"></i>
                {{ suggestion }}
              </li>
            </ul>
          </div>
        </div>
      </el-card>
    </div>

    <!-- 空状态 -->
    <el-card shadow="never" class="empty-card" v-else>
      <div class="empty-content">
        <i class="el-icon-data-analysis empty-icon"></i>
        <div class="empty-text">请设置分析条件并点击"开始分析"按钮</div>
      </div>
    </el-card>
  </div>
</template>

<script>
import { getSpecialCostComprehensiveAnalysis, getSpecialCostCompareAnalysis } from '@/api/financialSharing/specialCost'
import * as echarts from 'echarts'

export default {
  name: 'SpecialAnalysisIndex',
  data() {
    return {
      conditionForm: {
        analysisType: 'comprehensive',
        dimension: ['project', 'activity', 'quality', 'environment', 'rd'],
        dateRange: []
      },
      analysisLoading: false,
      analysisResult: null,
      overviewStats: [],
      compareData: [],
      structureChart: null,
      trendChart: null
    }
  },
  methods: {
    handleAnalysisTypeChange() {
      // 根据分析类型调整默认维度
      if (this.conditionForm.analysisType === 'comprehensive') {
        this.conditionForm.dimension = ['project', 'activity', 'quality', 'environment', 'rd']
      }
    },
    async performAnalysis() {
      if (!this.conditionForm.dimension || this.conditionForm.dimension.length === 0) {
        this.$baseMessage('请选择至少一个分析维度', 'warning')
        return
      }
      
      this.analysisLoading = true
      
      try {
        let apiCall
        if (this.conditionForm.analysisType === 'compare') {
          apiCall = getSpecialCostCompareAnalysis(this.conditionForm)
        } else {
          apiCall = getSpecialCostComprehensiveAnalysis(this.conditionForm)
        }
        
        const { code, data } = await apiCall
        if (code === 200) {
          this.analysisResult = data
          this.overviewStats = data.overviewStats || []
          this.compareData = data.compareData || []
          
          this.$nextTick(() => {
            this.initCharts()
          })
        }
      } catch (error) {
        this.$baseMessage('分析失败，请重试', 'error')
      } finally {
        this.analysisLoading = false
      }
    },
    resetCondition() {
      this.conditionForm = {
        analysisType: 'comprehensive',
        dimension: ['project', 'activity', 'quality', 'environment', 'rd'],
        dateRange: []
      }
      this.analysisResult = null
    },
    initCharts() {
      this.initStructureChart()
      this.initTrendChart()
    },
    initStructureChart() {
      const chartDom = document.getElementById('costStructureChart')
      this.structureChart = echarts.init(chartDom)
      
      const structureData = this.analysisResult.structureData || []
      
      const option = {
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b}: {c}万 ({d}%)'
        },
        legend: {
          orient: 'vertical',
          left: 'left'
        },
        series: [
          {
            name: '成本结构',
            type: 'pie',
            radius: ['40%', '70%'],
            avoidLabelOverlap: false,
            data: structureData.map(item => ({
              value: item.amount / 10000,
              name: this.getDimensionName(item.dimension)
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
      
      this.structureChart.setOption(option)
    },
    initTrendChart() {
      const chartDom = document.getElementById('costTrendChart')
      this.trendChart = echarts.init(chartDom)
      
      const trendData = this.analysisResult.trendData || []
      const months = trendData.map(item => item.month)
      const series = []
      
      // 为每个维度创建一个系列
      this.conditionForm.dimension.forEach(dim => {
        series.push({
          name: this.getDimensionName(dim),
          type: 'line',
          data: trendData.map(item => (item[dim] || 0) / 10000),
          smooth: true
        })
      })
      
      const option = {
        tooltip: {
          trigger: 'axis'
        },
        legend: {
          data: this.conditionForm.dimension.map(dim => this.getDimensionName(dim))
        },
        xAxis: {
          type: 'category',
          data: months
        },
        yAxis: {
          type: 'value',
          name: '金额(万)'
        },
        series: series
      }
      
      this.trendChart.setOption(option)
    },
    exportAnalysis() {
      try {
        const data = this.compareData || []
        if (data.length === 0 && !this.analysisResult) {
          this.$baseMessage('暂无数据可导出，请先执行分析', 'warning')
          return
        }
        const exportData = this.analysisResult || { compareData: data, overviewStats: this.overviewStats }
        const jsonStr = JSON.stringify(exportData, null, 2)
        const blob = new Blob([jsonStr], { type: 'application/json' })
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = '专项成本分析数据.json'
        link.click()
        window.URL.revokeObjectURL(url)
        this.$baseMessage('导出成功', 'success')
      } catch (error) {
        this.$baseMessage('导出失败', 'error')
      }
    },
    formatAmount(amount) {
      if (!amount) return '0.00'
      return (amount / 10000).toFixed(2) + '万'
    },
    getDimensionName(dimension) {
      const nameMap = {
        'project': '项目成本',
        'activity': '作业成本',
        'quality': '质量成本',
        'environment': '环境成本',
        'rd': '研发成本'
      }
      return nameMap[dimension] || '未知'
    },
    getDimensionTag(dimension) {
      const tagMap = {
        'project': 'primary',
        'activity': 'success',
        'quality': 'warning',
        'environment': 'info',
        'rd': 'danger'
      }
      return tagMap[dimension] || 'info'
    },
    getChangeClass(changeAmount) {
      if (changeAmount > 0) return 'text-danger'
      if (changeAmount < 0) return 'text-success'
      return 'text-info'
    }
  },
  beforeDestroy() {
    if (this.structureChart) {
      this.structureChart.dispose()
    }
    if (this.trendChart) {
      this.trendChart.dispose()
    }
  }
}
</script>

<style lang="scss" scoped>
.special-analysis-container {
  .condition-card,
  .overview-card,
  .chart-card,
  .compare-card,
  .conclusion-card,
  .empty-card {
    margin-bottom: 20px;
    
    &:last-child {
      margin-bottom: 0;
    }
  }
  
  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    font-weight: bold;
    color: #303133;
    
    .header-actions {
      .el-button {
        margin-left: 10px;
      }
    }
  }
  
  .overview-item {
    display: flex;
    align-items: center;
    padding: 20px;
    border-radius: 8px;
    color: white;
    margin-bottom: 10px;
    
    &.project {
      background: linear-gradient(135deg, #409eff 0%, #66b1ff 100%);
    }
    
    &.activity {
      background: linear-gradient(135deg, #67c23a 0%, #85ce61 100%);
    }
    
    &.quality {
      background: linear-gradient(135deg, #e6a23c 0%, #ebb563 100%);
    }
    
    &.environment {
      background: linear-gradient(135deg, #909399 0%, #a6a9ad 100%);
    }
    
    &.rd {
      background: linear-gradient(135deg, #f56c6c 0%, #f78989 100%);
    }
    
    .overview-icon {
      font-size: 40px;
      margin-right: 15px;
    }
    
    .overview-content {
      flex: 1;
      
      .overview-title {
        font-size: 14px;
        margin-bottom: 5px;
        opacity: 0.9;
      }
      
      .overview-value {
        font-size: 24px;
        font-weight: bold;
        margin-bottom: 5px;
      }
      
      .overview-change {
        font-size: 12px;
        opacity: 0.8;
        
        &.positive {
          color: #f78989;
        }
        
        &.negative {
          color: #85ce61;
        }
      }
    }
  }
  
  .conclusion-content {
    .conclusion-section {
      margin-bottom: 20px;
      
      &:last-child {
        margin-bottom: 0;
      }
      
      h4 {
        color: #303133;
        margin-bottom: 10px;
      }
      
      ul {
        margin: 0;
        padding-left: 20px;
        
        li {
          line-height: 1.6;
          color: #606266;
          margin-bottom: 8px;
          
          &.risk-item {
            color: #f56c6c;
            
            i {
              margin-right: 5px;
            }
          }
          
          &.suggestion-item {
            color: #67c23a;
            
            i {
              margin-right: 5px;
            }
          }
        }
      }
    }
  }
  
  .empty-content {
    text-align: center;
    padding: 60px 0;
    
    .empty-icon {
      font-size: 80px;
      color: #c0c4cc;
      margin-bottom: 20px;
    }
    
    .empty-text {
      font-size: 16px;
      color: #909399;
    }
  }
}

.text-success {
  color: #67c23a;
}

.text-warning {
  color: #e6a23c;
}

.text-danger {
  color: #f56c6c;
}

.text-info {
  color: #909399;
}
</style>
