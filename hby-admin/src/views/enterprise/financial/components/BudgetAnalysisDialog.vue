<template>
  <el-dialog
    title="预算分析"
    :visible.sync="dialogVisible"
    width="1000px"
    @close="handleClose"
  >
    <el-tabs v-model="activeTab" type="border-card">
      <el-tab-pane label="执行分析" name="execution">
        <el-row :gutter="20">
          <el-col :span="6">
            <el-statistic title="预算总额" :value="analysisData.totalBudget || 0" suffix="万元">
              <template slot="prefix">
                <i class="el-icon-money" style="color: #409EFF"></i>
              </template>
            </el-statistic>
          </el-col>
          <el-col :span="6">
            <el-statistic title="已执行" :value="analysisData.executedAmount || 0" suffix="万元">
              <template slot="prefix">
                <i class="el-icon-success" style="color: #67C23A"></i>
              </template>
            </el-statistic>
          </el-col>
          <el-col :span="6">
            <el-statistic title="执行率" :value="analysisData.executionRate || 0" suffix="%">
              <template slot="prefix">
                <i class="el-icon-data-analysis" style="color: #E6A23C"></i>
              </template>
            </el-statistic>
          </el-col>
          <el-col :span="6">
            <el-statistic title="剩余预算" :value="analysisData.remainingBudget || 0" suffix="万元">
              <template slot="prefix">
                <i class="el-icon-wallet" style="color: #F56C6C"></i>
              </template>
            </el-statistic>
          </el-col>
        </el-row>
        
        <el-divider></el-divider>
        
        <el-table :data="executionData" border>
          <el-table-column prop="itemName" label="预算项目" width="200"></el-table-column>
          <el-table-column prop="budgetAmount" label="预算金额" width="120" align="right">
            <template slot-scope="scope">
              {{ formatAmount(scope.row.budgetAmount) }}
            </template>
          </el-table-column>
          <el-table-column prop="executedAmount" label="已执行" width="120" align="right">
            <template slot-scope="scope">
              {{ formatAmount(scope.row.executedAmount) }}
            </template>
          </el-table-column>
          <el-table-column prop="executionRate" label="执行率" width="100" align="right">
            <template slot-scope="scope">
              <el-progress
                :percentage="scope.row.executionRate"
                :color="getProgressColor(scope.row.executionRate)"
                :stroke-width="15"
                text-inside
              ></el-progress>
            </template>
          </el-table-column>
          <el-table-column prop="variance" label="差异" width="120" align="right">
            <template slot-scope="scope">
              <span :class="getVarianceClass(scope.row.variance)">
                {{ formatAmount(scope.row.variance) }}
              </span>
            </template>
          </el-table-column>
          <el-table-column prop="status" label="状态" width="100">
            <template slot-scope="scope">
              <el-tag :type="getStatusType(scope.row.status)">
                {{ scope.row.status }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="remark" label="备注"></el-table-column>
        </el-table>
      </el-tab-pane>
      
      <el-tab-pane label="趋势分析" name="trend">
        <div class="chart-container">
          <h4>预算执行趋势</h4>
          <div id="trendChart" style="height: 400px;"></div>
        </div>
      </el-tab-pane>
      
      <el-tab-pane label="对比分析" name="compare">
        <el-form :model="compareForm" :inline="true" style="margin-bottom: 20px;">
          <el-form-item label="对比期间">
            <el-select v-model="compareForm.period" placeholder="请选择对比期间">
              <el-option label="同期对比" value="same_period"></el-option>
              <el-option label="环比对比" value="sequential"></el-option>
              <el-option label="年度对比" value="annual"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="generateCompare">生成对比</el-button>
          </el-form-item>
        </el-form>
        
        <el-table :data="compareData" border>
          <el-table-column prop="itemName" label="预算项目" width="200"></el-table-column>
          <el-table-column prop="currentAmount" label="本期金额" width="120" align="right">
            <template slot-scope="scope">
              {{ formatAmount(scope.row.currentAmount) }}
            </template>
          </el-table-column>
          <el-table-column prop="compareAmount" label="对比金额" width="120" align="right">
            <template slot-scope="scope">
              {{ formatAmount(scope.row.compareAmount) }}
            </template>
          </el-table-column>
          <el-table-column prop="changeAmount" label="变动金额" width="120" align="right">
            <template slot-scope="scope">
              <span :class="getChangeClass(scope.row.changeAmount)">
                {{ formatAmount(scope.row.changeAmount) }}
              </span>
            </template>
          </el-table-column>
          <el-table-column prop="changeRate" label="变动率" width="100" align="right">
            <template slot-scope="scope">
              <span :class="getChangeClass(scope.row.changeAmount)">
                {{ formatPercent(scope.row.changeRate) }}
              </span>
            </template>
          </el-table-column>
          <el-table-column prop="analysis" label="分析说明"></el-table-column>
        </el-table>
      </el-tab-pane>
      
      <el-tab-pane label="预警分析" name="warning">
        <el-alert
          title="预算预警"
          type="warning"
          :closable="false"
          style="margin-bottom: 20px;"
        >
          <template slot="description">
            发现 <strong>{{ warningItems.length }}</strong> 项预算异常，请及时关注和处理
          </template>
        </el-alert>
        
        <el-table :data="warningItems" border>
          <el-table-column prop="itemName" label="预算项目" width="200"></el-table-column>
          <el-table-column prop="warningType" label="预警类型" width="120">
            <template slot-scope="scope">
              <el-tag :type="getWarningType(scope.row.warningType)">
                {{ scope.row.warningType }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="warningLevel" label="预警级别" width="100">
            <template slot-scope="scope">
              <el-tag :type="getWarningLevelType(scope.row.warningLevel)">
                {{ scope.row.warningLevel }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="currentValue" label="当前值" width="120" align="right">
            <template slot-scope="scope">
              {{ formatAmount(scope.row.currentValue) }}
            </template>
          </el-table-column>
          <el-table-column prop="thresholdValue" label="阈值" width="120" align="right">
            <template slot-scope="scope">
              {{ formatAmount(scope.row.thresholdValue) }}
            </template>
          </el-table-column>
          <el-table-column prop="warningDescription" label="预警说明"></el-table-column>
          <el-table-column label="操作" width="120">
            <template slot-scope="scope">
              <el-button type="text" size="small" @click="handleWarning(scope.row)">
                处理
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>
    </el-tabs>
    
    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">关闭</el-button>
      <el-button type="primary" @click="exportAnalysis">导出分析报告</el-button>
    </div>
  </el-dialog>
</template>

<script>
export default {
  name: 'BudgetAnalysisDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    budgetData: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      activeTab: 'execution',
      compareForm: {
        period: 'same_period'
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
    analysisData() {
      return this.budgetData.analysisData || {
        totalBudget: 1000,
        executedAmount: 750,
        executionRate: 75,
        remainingBudget: 250
      }
    },
    executionData() {
      return this.budgetData.executionData || [
        {
          itemName: '人员费用',
          budgetAmount: 500000,
          executedAmount: 400000,
          executionRate: 80,
          variance: -100000,
          status: '正常'
        },
        {
          itemName: '办公费用',
          budgetAmount: 200000,
          executedAmount: 180000,
          executionRate: 90,
          variance: -20000,
          status: '正常'
        }
      ]
    },
    compareData() {
      return [
        {
          itemName: '人员费用',
          currentAmount: 400000,
          compareAmount: 350000,
          changeAmount: 50000,
          changeRate: 14.29,
          analysis: '人员成本上升'
        }
      ]
    },
    warningItems() {
      return [
        {
          itemName: '营销费用',
          warningType: '超支预警',
          warningLevel: '高',
          currentValue: 120000,
          thresholdValue: 100000,
          warningDescription: '营销费用超出预算20%'
        }
      ]
    }
  },
  methods: {
    formatAmount(amount) {
      if (!amount) return '0.00'
      return (amount / 10000).toFixed(2) + '万元'
    },
    formatPercent(rate) {
      if (!rate) return '0.00%'
      return rate.toFixed(2) + '%'
    },
    getProgressColor(progress) {
      if (progress >= 80) return '#67C23A'
      if (progress >= 50) return '#E6A23C'
      return '#F56C6C'
    },
    getVarianceClass(variance) {
      if (variance > 0) return 'positive-variance'
      if (variance < 0) return 'negative-variance'
      return ''
    },
    getChangeClass(amount) {
      if (amount > 0) return 'positive-change'
      if (amount < 0) return 'negative-change'
      return ''
    },
    getStatusType(status) {
      const statusMap = {
        '正常': 'success',
        '预警': 'warning',
        '超支': 'danger'
      }
      return statusMap[status] || 'info'
    },
    getWarningType(type) {
      const typeMap = {
        '超支预警': 'danger',
        '进度预警': 'warning',
        '余额预警': 'info'
      }
      return typeMap[type] || 'info'
    },
    getWarningLevelType(level) {
      const levelMap = {
        '高': 'danger',
        '中': 'warning',
        '低': 'info'
      }
      return levelMap[level] || 'info'
    },
    generateCompare() {
      this.$message.success('对比分析生成成功')
    },
    handleWarning(row) {
      this.$message.info('处理预警：' + row.itemName)
    },
    exportAnalysis() {
      this.$message.success('分析报告导出中...')
    },
    handleClose() {
      this.dialogVisible = false
    }
  }
}
</script>

<style scoped>
.positive-variance {
  color: #67C23A;
}
.negative-variance {
  color: #F56C6C;
}
.positive-change {
  color: #67C23A;
}
.negative-change {
  color: #F56C6C;
}
.chart-container {
  border: 1px solid #EBEEF5;
  border-radius: 4px;
  padding: 20px;
}
</style>
