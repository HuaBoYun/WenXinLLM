<template>
  <el-dialog
    title="财务分析详情"
    :visible.sync="dialogVisible"
    width="80%"
    :before-close="handleClose"
  >
    <el-tabs v-model="activeTab" type="border-card">
      <el-tab-pane label="基础指标" name="basic">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-card>
              <div slot="header">
                <span>盈利能力指标</span>
              </div>
              <el-descriptions :column="2" border>
                <el-descriptions-item label="净资产收益率">
                  <span :class="getIndicatorClass(analysisData.roe)">{{ analysisData.roe }}%</span>
                </el-descriptions-item>
                <el-descriptions-item label="总资产收益率">
                  <span :class="getIndicatorClass(analysisData.roa)">{{ analysisData.roa }}%</span>
                </el-descriptions-item>
                <el-descriptions-item label="销售净利率">
                  <span :class="getIndicatorClass(analysisData.netProfitMargin)">{{ analysisData.netProfitMargin }}%</span>
                </el-descriptions-item>
                <el-descriptions-item label="毛利率">
                  <span :class="getIndicatorClass(analysisData.grossMargin)">{{ analysisData.grossMargin }}%</span>
                </el-descriptions-item>
              </el-descriptions>
            </el-card>
          </el-col>
          <el-col :span="12">
            <el-card>
              <div slot="header">
                <span>偿债能力指标</span>
              </div>
              <el-descriptions :column="2" border>
                <el-descriptions-item label="流动比率">
                  <span :class="getIndicatorClass(analysisData.currentRatio)">{{ analysisData.currentRatio }}</span>
                </el-descriptions-item>
                <el-descriptions-item label="速动比率">
                  <span :class="getIndicatorClass(analysisData.quickRatio)">{{ analysisData.quickRatio }}</span>
                </el-descriptions-item>
                <el-descriptions-item label="资产负债率">
                  <span :class="getIndicatorClass(analysisData.debtRatio, true)">{{ analysisData.debtRatio }}%</span>
                </el-descriptions-item>
                <el-descriptions-item label="利息保障倍数">
                  <span :class="getIndicatorClass(analysisData.interestCoverage)">{{ analysisData.interestCoverage }}</span>
                </el-descriptions-item>
              </el-descriptions>
            </el-card>
          </el-col>
        </el-row>
        
        <el-row :gutter="20" style="margin-top: 20px;">
          <el-col :span="12">
            <el-card>
              <div slot="header">
                <span>营运能力指标</span>
              </div>
              <el-descriptions :column="2" border>
                <el-descriptions-item label="总资产周转率">
                  <span :class="getIndicatorClass(analysisData.assetTurnover)">{{ analysisData.assetTurnover }}</span>
                </el-descriptions-item>
                <el-descriptions-item label="存货周转率">
                  <span :class="getIndicatorClass(analysisData.inventoryTurnover)">{{ analysisData.inventoryTurnover }}</span>
                </el-descriptions-item>
                <el-descriptions-item label="应收账款周转率">
                  <span :class="getIndicatorClass(analysisData.receivableTurnover)">{{ analysisData.receivableTurnover }}</span>
                </el-descriptions-item>
                <el-descriptions-item label="现金周转天数">
                  <span :class="getIndicatorClass(analysisData.cashCycle, true)">{{ analysisData.cashCycle }}天</span>
                </el-descriptions-item>
              </el-descriptions>
            </el-card>
          </el-col>
          <el-col :span="12">
            <el-card>
              <div slot="header">
                <span>发展能力指标</span>
              </div>
              <el-descriptions :column="2" border>
                <el-descriptions-item label="营业收入增长率">
                  <span :class="getIndicatorClass(analysisData.revenueGrowth)">{{ analysisData.revenueGrowth }}%</span>
                </el-descriptions-item>
                <el-descriptions-item label="净利润增长率">
                  <span :class="getIndicatorClass(analysisData.profitGrowth)">{{ analysisData.profitGrowth }}%</span>
                </el-descriptions-item>
                <el-descriptions-item label="总资产增长率">
                  <span :class="getIndicatorClass(analysisData.assetGrowth)">{{ analysisData.assetGrowth }}%</span>
                </el-descriptions-item>
                <el-descriptions-item label="净资产增长率">
                  <span :class="getIndicatorClass(analysisData.equityGrowth)">{{ analysisData.equityGrowth }}%</span>
                </el-descriptions-item>
              </el-descriptions>
            </el-card>
          </el-col>
        </el-row>
      </el-tab-pane>
      
      <el-tab-pane label="趋势分析" name="trend">
        <div id="trendChart" style="width: 100%; height: 400px;"></div>
      </el-tab-pane>
      
      <el-tab-pane label="同行对比" name="comparison">
        <div id="comparisonChart" style="width: 100%; height: 400px;"></div>
      </el-tab-pane>
      
      <el-tab-pane label="分析报告" name="report">
        <el-card>
          <div slot="header">
            <span>财务分析报告</span>
            <el-button style="float: right;" type="primary" size="small" @click="exportReport">
              导出报告
            </el-button>
          </div>
          
          <div class="report-content">
            <h3>一、盈利能力分析</h3>
            <p>{{ analysisData.profitabilityAnalysis }}</p>
            
            <h3>二、偿债能力分析</h3>
            <p>{{ analysisData.solvencyAnalysis }}</p>
            
            <h3>三、营运能力分析</h3>
            <p>{{ analysisData.operatingAnalysis }}</p>
            
            <h3>四、发展能力分析</h3>
            <p>{{ analysisData.growthAnalysis }}</p>
            
            <h3>五、综合评价与建议</h3>
            <p>{{ analysisData.recommendations }}</p>
          </div>
        </el-card>
      </el-tab-pane>
    </el-tabs>
    
    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">关闭</el-button>
      <el-button type="primary" @click="saveAnalysis">保存分析</el-button>
    </div>
  </el-dialog>
</template>

<script>
export default {
  name: 'FinancialAnalysisDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    analysisData: {
      type: Object,
      default: () => ({
        roe: 15.2,
        roa: 8.5,
        netProfitMargin: 12.3,
        grossMargin: 35.6,
        currentRatio: 2.1,
        quickRatio: 1.8,
        debtRatio: 45.2,
        interestCoverage: 8.5,
        assetTurnover: 1.2,
        inventoryTurnover: 6.8,
        receivableTurnover: 8.2,
        cashCycle: 45,
        revenueGrowth: 12.5,
        profitGrowth: 18.3,
        assetGrowth: 8.9,
        equityGrowth: 15.6,
        profitabilityAnalysis: '公司盈利能力较强，净资产收益率15.2%，高于行业平均水平。销售净利率12.3%，表明公司具有良好的盈利质量。',
        solvencyAnalysis: '公司偿债能力良好，流动比率2.1，速动比率1.8，均处于合理区间。资产负债率45.2%，财务风险可控。',
        operatingAnalysis: '公司营运能力稳定，总资产周转率1.2次，存货周转率6.8次，应收账款周转率8.2次，资产运营效率较高。',
        growthAnalysis: '公司发展能力强劲，营业收入增长率12.5%，净利润增长率18.3%，显示出良好的成长性。',
        recommendations: '建议公司继续保持良好的盈利能力，适度控制负债水平，提高资产运营效率，加强现金流管理。'
      })
    }
  },
  data() {
    return {
      activeTab: 'basic'
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
        this.$nextTick(() => {
          this.initCharts()
        })
      }
    }
  },
  methods: {
    handleClose() {
      this.dialogVisible = false
    },
    getIndicatorClass(value, reverse = false) {
      const numValue = parseFloat(value)
      if (reverse) {
        if (numValue <= 30) return 'indicator-good'
        if (numValue <= 60) return 'indicator-normal'
        return 'indicator-poor'
      } else {
        if (numValue >= 15) return 'indicator-good'
        if (numValue >= 10) return 'indicator-normal'
        return 'indicator-poor'
      }
    },
    initCharts() {
      this.initTrendChart()
      this.initComparisonChart()
    },
    initTrendChart() {
      // 趋势图表初始化逻辑
      this.$message.info('趋势图表初始化')
    },
    initComparisonChart() {
      // 对比图表初始化逻辑
      this.$message.info('对比图表初始化')
    },
    exportReport() {
      this.$message.success('导出财务分析报告')
    },
    saveAnalysis() {
      this.$message.success('保存财务分析成功')
      this.handleClose()
    }
  }
}
</script>

<style scoped>
.report-content {
  line-height: 1.8;
}
.report-content h3 {
  color: #409EFF;
  margin-top: 20px;
  margin-bottom: 10px;
}
.report-content p {
  text-indent: 2em;
  margin-bottom: 15px;
}
.indicator-good {
  color: #67C23A;
  font-weight: bold;
}
.indicator-normal {
  color: #E6A23C;
  font-weight: bold;
}
.indicator-poor {
  color: #F56C6C;
  font-weight: bold;
}
</style>
