<template>
  <div class="invoice-analytics-container">
    <div class="analytics-header">
      <h3>发票分析</h3>
      <el-button type="primary" size="small" @click="refreshAnalytics">
        <i class="el-icon-refresh"></i> 刷新数据
      </el-button>
    </div>

    <!-- 统计卡片 -->
    <el-row :gutter="20" class="analytics-cards">
      <el-col :span="6">
        <div class="analytics-card">
          <div class="card-icon invoice">
            <i class="el-icon-document"></i>
          </div>
          <div class="card-content">
            <div class="card-number">{{ analyticsData.totalInvoices || 0 }}</div>
            <div class="card-label">发票总数</div>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="analytics-card">
          <div class="card-icon amount">
            <i class="el-icon-money"></i>
          </div>
          <div class="card-content">
            <div class="card-number">{{ formatAmount(analyticsData.totalAmount) }}</div>
            <div class="card-label">总金额</div>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="analytics-card">
          <div class="card-icon verified">
            <i class="el-icon-check"></i>
          </div>
          <div class="card-content">
            <div class="card-number">{{ analyticsData.verifiedCount || 0 }}</div>
            <div class="card-label">已验证</div>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="analytics-card">
          <div class="card-icon risk">
            <i class="el-icon-warning"></i>
          </div>
          <div class="card-content">
            <div class="card-number">{{ analyticsData.riskCount || 0 }}</div>
            <div class="card-label">风险发票</div>
          </div>
        </div>
      </el-col>
    </el-row>

    <!-- 图表区域 -->
    <el-row :gutter="20" class="charts-section">
      <el-col :span="12">
        <div class="chart-container">
          <h4>发票类型分布</h4>
          <div id="invoiceTypeChart" style="height: 300px;"></div>
        </div>
      </el-col>
      <el-col :span="12">
        <div class="chart-container">
          <h4>月度发票趋势</h4>
          <div id="monthlyTrendChart" style="height: 300px;"></div>
        </div>
      </el-col>
    </el-row>

    <!-- 详细分析表格 -->
    <div class="analysis-table">
      <h4>详细分析</h4>
      <el-table :data="analysisTableData" border stripe>
        <el-table-column prop="category" label="分类" width="120" />
        <el-table-column prop="count" label="数量" width="100" />
        <el-table-column prop="amount" label="金额" width="150">
          <template slot-scope="scope">
            {{ formatAmount(scope.row.amount) }}
          </template>
        </el-table-column>
        <el-table-column prop="percentage" label="占比" width="100">
          <template slot-scope="scope">
            {{ scope.row.percentage }}%
          </template>
        </el-table-column>
        <el-table-column prop="remark" label="备注" />
      </el-table>
    </div>
  </div>
</template>

<script>
export default {
  name: 'InvoiceAnalytics',
  data() {
    return {
      analyticsData: {
        totalInvoices: 1256,
        totalAmount: 125600000,
        verifiedCount: 1180,
        riskCount: 23
      },
      analysisTableData: [
        {
          category: '增值税专用发票',
          count: 856,
          amount: 85600000,
          percentage: 68.1,
          remark: '主要业务发票'
        },
        {
          category: '增值税普通发票',
          count: 324,
          amount: 32400000,
          percentage: 25.8,
          remark: '零售业务发票'
        },
        {
          category: '电子发票',
          count: 76,
          amount: 7600000,
          percentage: 6.1,
          remark: '电商业务发票'
        }
      ]
    }
  },
  mounted() {
    this.initCharts()
  },
  methods: {
    formatAmount(amount) {
      if (!amount) return '0'
      return (amount / 10000).toFixed(1) + '万'
    },
    refreshAnalytics() {
      this.$message.success('数据刷新成功')
      // 这里可以调用API刷新数据
    },
    initCharts() {
      // 这里可以初始化图表，使用 ECharts 或其他图表库
      this.$message.info('图表功能开发中...')
    }
  }
}
</script>

<style lang="scss" scoped>
.invoice-analytics-container {
  padding: 20px;
}

.analytics-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  
  h3 {
    margin: 0;
    color: #303133;
  }
}

.analytics-cards {
  margin-bottom: 30px;
}

.analytics-card {
  background: white;
  border-radius: 8px;
  padding: 20px;
  display: flex;
  align-items: center;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.card-icon {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 15px;
  
  i {
    font-size: 20px;
    color: white;
  }
  
  &.invoice {
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  }
  
  &.amount {
    background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
  }
  
  &.verified {
    background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
  }
  
  &.risk {
    background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
  }
}

.card-content {
  flex: 1;
}

.card-number {
  font-size: 24px;
  font-weight: bold;
  color: #303133;
  margin-bottom: 5px;
}

.card-label {
  font-size: 14px;
  color: #909399;
}

.charts-section {
  margin-bottom: 30px;
}

.chart-container {
  background: white;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  
  h4 {
    margin: 0 0 15px 0;
    color: #303133;
  }
}

.analysis-table {
  background: white;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  
  h4 {
    margin: 0 0 15px 0;
    color: #303133;
  }
}
</style>
