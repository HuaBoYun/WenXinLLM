<template>
  <el-dialog
    title="财务报告详情"
    :visible.sync="dialogVisible"
    width="80%"
    :before-close="handleClose"
  >
    <el-tabs v-model="activeTab" type="border-card">
      <el-tab-pane label="报告概览" name="overview">
        <el-row :gutter="20">
          <el-col :span="6">
            <el-card>
              <el-statistic title="营业收入" :value="reportData.revenue" suffix="万元">
                <template slot="prefix">
                  <i class="el-icon-money" style="color: #409EFF"></i>
                </template>
              </el-statistic>
            </el-card>
          </el-col>
          <el-col :span="6">
            <el-card>
              <el-statistic title="净利润" :value="reportData.netProfit" suffix="万元">
                <template slot="prefix">
                  <i class="el-icon-coin" style="color: #67C23A"></i>
                </template>
              </el-statistic>
            </el-card>
          </el-col>
          <el-col :span="6">
            <el-card>
              <el-statistic title="总资产" :value="reportData.totalAssets" suffix="万元">
                <template slot="prefix">
                  <i class="el-icon-office-building" style="color: #E6A23C"></i>
                </template>
              </el-statistic>
            </el-card>
          </el-col>
          <el-col :span="6">
            <el-card>
              <el-statistic title="净资产" :value="reportData.netAssets" suffix="万元">
                <template slot="prefix">
                  <i class="el-icon-wallet" style="color: #F56C6C"></i>
                </template>
              </el-statistic>
            </el-card>
          </el-col>
        </el-row>
        
        <el-card style="margin-top: 20px;">
          <div slot="header">
            <span>财务指标</span>
          </div>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-descriptions :column="2" border>
                <el-descriptions-item label="资产负债率">
                  <span :class="getIndicatorClass(reportData.debtRatio)">{{ reportData.debtRatio }}%</span>
                </el-descriptions-item>
                <el-descriptions-item label="流动比率">
                  <span :class="getIndicatorClass(reportData.currentRatio)">{{ reportData.currentRatio }}</span>
                </el-descriptions-item>
                <el-descriptions-item label="速动比率">
                  <span :class="getIndicatorClass(reportData.quickRatio)">{{ reportData.quickRatio }}</span>
                </el-descriptions-item>
                <el-descriptions-item label="净资产收益率">
                  <span :class="getIndicatorClass(reportData.roe)">{{ reportData.roe }}%</span>
                </el-descriptions-item>
              </el-descriptions>
            </el-col>
            <el-col :span="12">
              <el-descriptions :column="2" border>
                <el-descriptions-item label="总资产周转率">
                  <span :class="getIndicatorClass(reportData.assetTurnover)">{{ reportData.assetTurnover }}</span>
                </el-descriptions-item>
                <el-descriptions-item label="存货周转率">
                  <span :class="getIndicatorClass(reportData.inventoryTurnover)">{{ reportData.inventoryTurnover }}</span>
                </el-descriptions-item>
                <el-descriptions-item label="应收账款周转率">
                  <span :class="getIndicatorClass(reportData.receivableTurnover)">{{ reportData.receivableTurnover }}</span>
                </el-descriptions-item>
                <el-descriptions-item label="毛利率">
                  <span :class="getIndicatorClass(reportData.grossMargin)">{{ reportData.grossMargin }}%</span>
                </el-descriptions-item>
              </el-descriptions>
            </el-col>
          </el-row>
        </el-card>
      </el-tab-pane>
      
      <el-tab-pane label="资产负债表" name="balance">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-card>
              <div slot="header">
                <span>资产</span>
              </div>
              <el-table :data="reportData.assets" border>
                <el-table-column prop="itemName" label="项目" width="200"></el-table-column>
                <el-table-column prop="currentAmount" label="期末余额" align="right">
                  <template slot-scope="scope">
                    {{ formatAmount(scope.row.currentAmount) }}
                  </template>
                </el-table-column>
                <el-table-column prop="previousAmount" label="期初余额" align="right">
                  <template slot-scope="scope">
                    {{ formatAmount(scope.row.previousAmount) }}
                  </template>
                </el-table-column>
              </el-table>
            </el-card>
          </el-col>
          <el-col :span="12">
            <el-card>
              <div slot="header">
                <span>负债及所有者权益</span>
              </div>
              <el-table :data="reportData.liabilities" border>
                <el-table-column prop="itemName" label="项目" width="200"></el-table-column>
                <el-table-column prop="currentAmount" label="期末余额" align="right">
                  <template slot-scope="scope">
                    {{ formatAmount(scope.row.currentAmount) }}
                  </template>
                </el-table-column>
                <el-table-column prop="previousAmount" label="期初余额" align="right">
                  <template slot-scope="scope">
                    {{ formatAmount(scope.row.previousAmount) }}
                  </template>
                </el-table-column>
              </el-table>
            </el-card>
          </el-col>
        </el-row>
      </el-tab-pane>
      
      <el-tab-pane label="利润表" name="income">
        <el-table :data="reportData.incomeStatement" border>
          <el-table-column prop="itemName" label="项目" width="300"></el-table-column>
          <el-table-column prop="currentAmount" label="本期金额" align="right">
            <template slot-scope="scope">
              {{ formatAmount(scope.row.currentAmount) }}
            </template>
          </el-table-column>
          <el-table-column prop="previousAmount" label="上期金额" align="right">
            <template slot-scope="scope">
              {{ formatAmount(scope.row.previousAmount) }}
            </template>
          </el-table-column>
          <el-table-column prop="changeRate" label="变动率" align="center">
            <template slot-scope="scope">
              <span :class="getChangeClass(scope.row.changeRate)">
                {{ scope.row.changeRate }}%
              </span>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>
      
      <el-tab-pane label="现金流量表" name="cashflow">
        <el-table :data="reportData.cashFlowStatement" border>
          <el-table-column prop="itemName" label="项目" width="300"></el-table-column>
          <el-table-column prop="currentAmount" label="本期金额" align="right">
            <template slot-scope="scope">
              {{ formatAmount(scope.row.currentAmount) }}
            </template>
          </el-table-column>
          <el-table-column prop="previousAmount" label="上期金额" align="right">
            <template slot-scope="scope">
              {{ formatAmount(scope.row.previousAmount) }}
            </template>
          </el-table-column>
          <el-table-column prop="changeRate" label="变动率" align="center">
            <template slot-scope="scope">
              <span :class="getChangeClass(scope.row.changeRate)">
                {{ scope.row.changeRate }}%
              </span>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>
      
      <el-tab-pane label="财务分析" name="analysis">
        <el-card>
          <div slot="header">
            <span>财务分析报告</span>
          </div>
          <div class="analysis-content">
            <h4>一、盈利能力分析</h4>
            <p>{{ reportData.profitabilityAnalysis }}</p>
            
            <h4>二、偿债能力分析</h4>
            <p>{{ reportData.solvencyAnalysis }}</p>
            
            <h4>三、营运能力分析</h4>
            <p>{{ reportData.operatingAnalysis }}</p>
            
            <h4>四、发展能力分析</h4>
            <p>{{ reportData.growthAnalysis }}</p>
            
            <h4>五、风险提示</h4>
            <p>{{ reportData.riskWarning }}</p>
          </div>
        </el-card>
      </el-tab-pane>
    </el-tabs>
    
    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">关闭</el-button>
      <el-button type="primary" @click="exportReport">导出报告</el-button>
    </div>
  </el-dialog>
</template>

<script>
export default {
  name: 'FinancialReportDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    reportData: {
      type: Object,
      default: () => ({
        revenue: 10000,
        netProfit: 1500,
        totalAssets: 50000,
        netAssets: 30000,
        debtRatio: 40.0,
        currentRatio: 1.5,
        quickRatio: 1.2,
        roe: 15.0,
        assetTurnover: 0.8,
        inventoryTurnover: 6.0,
        receivableTurnover: 8.0,
        grossMargin: 25.0,
        assets: [
          { itemName: '货币资金', currentAmount: 5000, previousAmount: 4500 },
          { itemName: '应收账款', currentAmount: 8000, previousAmount: 7500 },
          { itemName: '存货', currentAmount: 12000, previousAmount: 11000 },
          { itemName: '固定资产', currentAmount: 25000, previousAmount: 26000 }
        ],
        liabilities: [
          { itemName: '短期借款', currentAmount: 8000, previousAmount: 7000 },
          { itemName: '应付账款', currentAmount: 6000, previousAmount: 5500 },
          { itemName: '长期借款', currentAmount: 6000, previousAmount: 7000 },
          { itemName: '实收资本', currentAmount: 20000, previousAmount: 20000 },
          { itemName: '未分配利润', currentAmount: 10000, previousAmount: 8500 }
        ],
        incomeStatement: [
          { itemName: '营业收入', currentAmount: 10000, previousAmount: 9000, changeRate: 11.1 },
          { itemName: '营业成本', currentAmount: 7500, previousAmount: 6800, changeRate: 10.3 },
          { itemName: '营业利润', currentAmount: 1800, previousAmount: 1600, changeRate: 12.5 },
          { itemName: '净利润', currentAmount: 1500, previousAmount: 1300, changeRate: 15.4 }
        ],
        cashFlowStatement: [
          { itemName: '经营活动现金流量净额', currentAmount: 2000, previousAmount: 1800, changeRate: 11.1 },
          { itemName: '投资活动现金流量净额', currentAmount: -1000, previousAmount: -800, changeRate: -25.0 },
          { itemName: '筹资活动现金流量净额', currentAmount: -500, previousAmount: -600, changeRate: 16.7 }
        ],
        profitabilityAnalysis: '企业盈利能力良好，净利润同比增长15.4%，净资产收益率达到15%，高于行业平均水平。',
        solvencyAnalysis: '企业偿债能力稳定，资产负债率为40%，处于合理区间，流动比率和速动比率均符合安全标准。',
        operatingAnalysis: '企业营运能力有所提升，总资产周转率为0.8，存货周转率和应收账款周转率均有改善。',
        growthAnalysis: '企业发展能力强劲，营业收入增长11.1%，净利润增长15.4%，显示出良好的成长性。',
        riskWarning: '需关注应收账款增长较快的风险，建议加强应收账款管理，防范坏账风险。'
      })
    }
  },
  data() {
    return {
      activeTab: 'overview'
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
  methods: {
    handleClose() {
      this.dialogVisible = false
    },
    getIndicatorClass(value) {
      // 根据指标值返回样式类
      if (typeof value === 'string') {
        value = parseFloat(value)
      }
      if (value >= 15) return 'indicator-excellent'
      if (value >= 10) return 'indicator-good'
      if (value >= 5) return 'indicator-average'
      return 'indicator-poor'
    },
    getChangeClass(changeRate) {
      if (changeRate > 0) return 'change-positive'
      if (changeRate < 0) return 'change-negative'
      return 'change-neutral'
    },
    formatAmount(amount) {
      return amount ? amount.toLocaleString() + '万元' : '0万元'
    },
    exportReport() {
      this.$message.success('财务报告导出成功')
    }
  }
}
</script>

<style scoped>
.dialog-footer {
  text-align: right;
}
.analysis-content {
  line-height: 1.8;
}
.analysis-content h4 {
  color: #409EFF;
  margin-top: 20px;
  margin-bottom: 10px;
}
.analysis-content p {
  margin-bottom: 15px;
  text-indent: 2em;
}
.indicator-excellent {
  color: #67C23A;
  font-weight: bold;
}
.indicator-good {
  color: #409EFF;
  font-weight: bold;
}
.indicator-average {
  color: #E6A23C;
  font-weight: bold;
}
.indicator-poor {
  color: #F56C6C;
  font-weight: bold;
}
.change-positive {
  color: #67C23A;
  font-weight: bold;
}
.change-negative {
  color: #F56C6C;
  font-weight: bold;
}
.change-neutral {
  color: #909399;
}
</style>
