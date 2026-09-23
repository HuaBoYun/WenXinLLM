<template>
  <el-dialog
    title="经营报告详情"
    :visible.sync="dialogVisible"
    width="80%"
    :before-close="handleClose"
  >
    <div v-loading="loading">
    <el-tabs v-model="activeTab" type="border-card">
      <el-tab-pane label="经营概况" name="overview">
        <el-row :gutter="20">
          <el-col :span="8">
            <el-card class="stat-card">
              <div class="stat-content">
                <i class="el-icon-money stat-icon" style="color: #409EFF"></i>
                <div class="stat-info">
                  <div class="stat-value">{{ reportData.revenue || 0 }}</div>
                  <div class="stat-label">营业收入(元)</div>
                </div>
              </div>
            </el-card>
          </el-col>
          <el-col :span="8">
            <el-card class="stat-card">
              <div class="stat-content">
                <i class="el-icon-coin stat-icon" style="color: #67C23A"></i>
                <div class="stat-info">
                  <div class="stat-value">{{ reportData.netProfit || 0 }}</div>
                  <div class="stat-label">净利润(元)</div>
                </div>
              </div>
            </el-card>
          </el-col>
          <el-col :span="8">
            <el-card class="stat-card">
              <div class="stat-content">
                <i class="el-icon-office-building stat-icon" style="color: #E6A23C"></i>
                <div class="stat-info">
                  <div class="stat-value">{{ reportData.totalAssets || 0 }}</div>
                  <div class="stat-label">总资产(元)</div>
                </div>
              </div>
            </el-card>
          </el-col>
        </el-row>

        <el-row :gutter="20" style="margin-top: 20px;">
          <el-col :span="12">
            <el-card>
              <div slot="header">
                <span>主要经营指标</span>
              </div>
              <el-descriptions :column="2" border>
                <el-descriptions-item label="营业收入增长率">
                  <span :class="getGrowthClass(reportData.revenueGrowth)">
                    {{ reportData.revenueGrowth || 0 }}%
                  </span>
                </el-descriptions-item>
                <el-descriptions-item label="净利润增长率">
                  <span :class="getGrowthClass(reportData.profitGrowth)">
                    {{ reportData.profitGrowth || 0 }}%
                  </span>
                </el-descriptions-item>
                <el-descriptions-item label="毛利率">
                  {{ reportData.grossMargin || 0 }}%
                </el-descriptions-item>
                <el-descriptions-item label="净利率">
                  {{ reportData.netMargin || 0 }}%
                </el-descriptions-item>
                <el-descriptions-item label="资产负债率">
                  {{ reportData.debtRatio || 0 }}%
                </el-descriptions-item>
                <el-descriptions-item label="净资产收益率">
                  {{ reportData.roe || 0 }}%
                </el-descriptions-item>
              </el-descriptions>
            </el-card>
          </el-col>
          <el-col :span="12">
            <el-card>
              <div slot="header">
                <span>业务结构分析</span>
              </div>
              <el-table :data="reportData.revenueBreakdown" border size="small">
                <el-table-column prop="category" label="业务类别"></el-table-column>
                <el-table-column prop="amount" label="金额(元)" align="right"></el-table-column>
                <el-table-column prop="percentage" label="占比" align="right">
                  <template slot-scope="scope">{{ scope.row.percentage }}%</template>
                </el-table-column>
              </el-table>
            </el-card>
          </el-col>
        </el-row>
      </el-tab-pane>
      
      <el-tab-pane label="收入分析" name="revenue">
        <el-row :gutter="20" style="margin-bottom: 20px;">
          <el-col :span="8">
            <el-card class="stat-card">
              <div class="stat-content">
                <i class="el-icon-money stat-icon" style="color: #409EFF"></i>
                <div class="stat-info">
                  <div class="stat-value">{{ reportData.revenue || 0 }}</div>
                  <div class="stat-label">总收入(元)</div>
                </div>
              </div>
            </el-card>
          </el-col>
          <el-col :span="8">
            <el-card class="stat-card">
              <div class="stat-content">
                <i class="el-icon-top stat-icon" style="color: #67C23A"></i>
                <div class="stat-info">
                  <div class="stat-value">{{ reportData.revenueGrowth || 0 }}%</div>
                  <div class="stat-label">收入增长率</div>
                </div>
              </div>
            </el-card>
          </el-col>
          <el-col :span="8">
            <el-card class="stat-card">
              <div class="stat-content">
                <i class="el-icon-data-analysis stat-icon" style="color: #E6A23C"></i>
                <div class="stat-info">
                  <div class="stat-value">{{ reportData.grossMargin || 0 }}%</div>
                  <div class="stat-label">毛利率</div>
                </div>
              </div>
            </el-card>
          </el-col>
        </el-row>
        <el-card>
          <div slot="header"><span>收入构成明细</span></div>
          <el-table :data="reportData.revenueBreakdown" border stripe>
            <el-table-column prop="category" label="收入类别" min-width="150"></el-table-column>
            <el-table-column prop="amount" label="金额(元)" width="150" align="right"></el-table-column>
            <el-table-column prop="percentage" label="占比" width="100" align="right">
              <template slot-scope="scope">{{ scope.row.percentage }}%</template>
            </el-table-column>
            <el-table-column prop="growth" label="增长率" width="100" align="right">
              <template slot-scope="scope">
                <span :class="getGrowthClass(scope.row.growth)">{{ scope.row.growth }}%</span>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-tab-pane>

      <el-tab-pane label="成本分析" name="cost">
        <el-row :gutter="20" style="margin-bottom: 20px;">
          <el-col :span="8">
            <el-card class="stat-card">
              <div class="stat-content">
                <i class="el-icon-goods stat-icon" style="color: #F56C6C"></i>
                <div class="stat-info">
                  <div class="stat-value">{{ costTotal }}</div>
                  <div class="stat-label">总成本(元)</div>
                </div>
              </div>
            </el-card>
          </el-col>
          <el-col :span="8">
            <el-card class="stat-card">
              <div class="stat-content">
                <i class="el-icon-pie-chart stat-icon" style="color: #E6A23C"></i>
                <div class="stat-info">
                  <div class="stat-value">{{ costRatio }}%</div>
                  <div class="stat-label">成本占收入比</div>
                </div>
              </div>
            </el-card>
          </el-col>
          <el-col :span="8">
            <el-card class="stat-card">
              <div class="stat-content">
                <i class="el-icon-coin stat-icon" style="color: #67C23A"></i>
                <div class="stat-info">
                  <div class="stat-value">{{ reportData.netMargin || 0 }}%</div>
                  <div class="stat-label">净利率</div>
                </div>
              </div>
            </el-card>
          </el-col>
        </el-row>
        <el-card>
          <div slot="header"><span>成本构成明细</span></div>
          <el-table :data="reportData.costBreakdown" border stripe>
            <el-table-column prop="category" label="成本类别" min-width="150"></el-table-column>
            <el-table-column prop="amount" label="金额(元)" width="150" align="right"></el-table-column>
            <el-table-column prop="percentage" label="占比" width="100" align="right">
              <template slot-scope="scope">{{ scope.row.percentage }}%</template>
            </el-table-column>
            <el-table-column prop="change" label="变动率" width="100" align="right">
              <template slot-scope="scope">
                <span :class="getChangeClass(scope.row.change)">{{ scope.row.change }}%</span>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-tab-pane>
      
      <el-tab-pane label="经营分析" name="analysis">
        <el-card>
          <div slot="header">
            <span>经营分析报告</span>
            <el-button style="float: right;" type="primary" size="small" @click="exportReport">
              导出报告
            </el-button>
          </div>
          
          <div class="analysis-content">
            <h3>一、经营业绩总体评价</h3>
            <p>{{ reportData.overallAnalysis }}</p>
            
            <h3>二、收入分析</h3>
            <p>{{ reportData.revenueAnalysis }}</p>
            
            <h3>三、成本费用分析</h3>
            <p>{{ reportData.costAnalysis }}</p>
            
            <h3>四、盈利能力分析</h3>
            <p>{{ reportData.profitabilityAnalysis }}</p>
            
            <h3>五、存在问题及改进建议</h3>
            <p>{{ reportData.recommendations }}</p>
          </div>
        </el-card>
      </el-tab-pane>
    </el-tabs>
    </div>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">关闭</el-button>
      <el-button type="primary" @click="exportReport">导出报告</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { getOperationReport } from '@/api/enterprise/operation'

export default {
  name: 'OperationReportDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    enterpriseId: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      activeTab: 'overview',
      loading: false,
      reportData: {
        revenue: 0,
        netProfit: 0,
        totalAssets: 0,
        revenueGrowth: 0,
        profitGrowth: 0,
        grossMargin: 0,
        netMargin: 0,
        debtRatio: 0,
        roe: 0,
        revenueBreakdown: [],
        costBreakdown: [],
        overallAnalysis: '暂无数据',
        revenueAnalysis: '暂无数据',
        costAnalysis: '暂无数据',
        profitabilityAnalysis: '暂无数据',
        recommendations: '暂无数据'
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
    costTotal() {
      if (!this.reportData.costBreakdown || this.reportData.costBreakdown.length === 0) return 0
      return this.reportData.costBreakdown.reduce((sum, item) => sum + (parseFloat(item.amount) || 0), 0)
    },
    costRatio() {
      if (!this.reportData.revenue || this.reportData.revenue === 0) return 0
      return Math.round(this.costTotal / parseFloat(this.reportData.revenue) * 1000) / 10
    }
  },
  watch: {
    visible(val) {
      if (val && this.enterpriseId) {
        this.loadReportData()
      }
    }
  },
  methods: {
    // 从API响应中提取数据
    extractData(response) {
      if (!response) return null
      const resData = response.data || response
      return resData && resData.pageInfo ? resData.pageInfo : resData
    },
    // 加载经营报告数据
    async loadReportData() {
      if (!this.enterpriseId) return
      this.loading = true
      try {
        const response = await getOperationReport({ enterpriseId: this.enterpriseId })
        const data = this.extractData(response)
        if (data) {
          this.reportData = {
            revenue: data.revenue || 0,
            netProfit: data.netProfit || 0,
            totalAssets: data.totalAssets || 0,
            revenueGrowth: data.revenueGrowth || 0,
            profitGrowth: data.profitGrowth || 0,
            grossMargin: data.grossMargin || 0,
            netMargin: data.netMargin || 0,
            debtRatio: data.debtRatio || 0,
            roe: data.roe || 0,
            revenueBreakdown: data.revenueBreakdown || [],
            costBreakdown: data.costBreakdown || [],
            overallAnalysis: data.overallAnalysis || '暂无数据',
            revenueAnalysis: data.revenueAnalysis || '暂无数据',
            costAnalysis: data.costAnalysis || '暂无数据',
            profitabilityAnalysis: data.profitabilityAnalysis || '暂无数据',
            recommendations: data.recommendations || '暂无数据'
          }
        }
      } catch (error) {
        console.error('加载经营报告数据失败:', error)
        this.$message.error('加载经营报告数据失败')
      } finally {
        this.loading = false
      }
    },
    handleClose() {
      this.dialogVisible = false
    },
    getGrowthClass(value) {
      const numValue = parseFloat(value)
      if (numValue > 10) return 'growth-high'
      if (numValue > 0) return 'growth-medium'
      return 'growth-low'
    },
    getChangeClass(value) {
      const numValue = parseFloat(value)
      if (numValue > 0) return 'change-increase'
      if (numValue < 0) return 'change-decrease'
      return 'change-stable'
    },
    // 格式化金额（元转万元）
    formatAmount(value) {
      if (!value) return '0'
      const num = typeof value === 'string' ? parseFloat(value) : value
      if (num >= 10000) return (num / 10000).toFixed(2)
      return num.toFixed(2)
    },
    exportReport() {
      const content = `经营报告\n\n一、经营业绩总体评价\n${this.reportData.overallAnalysis}\n\n二、收入分析\n${this.reportData.revenueAnalysis}\n\n三、成本费用分析\n${this.reportData.costAnalysis}\n\n四、盈利能力分析\n${this.reportData.profitabilityAnalysis}\n\n五、存在问题及改进建议\n${this.reportData.recommendations}`
      const blob = new Blob(['\uFEFF' + content], { type: 'text/plain;charset=utf-8;' })
      const url = window.URL.createObjectURL(blob)
      const link = document.createElement('a')
      link.href = url
      link.setAttribute('download', `经营报告_${new Date().toLocaleDateString()}.txt`)
      document.body.appendChild(link)
      link.click()
      document.body.removeChild(link)
      window.URL.revokeObjectURL(url)
      this.$message.success('导出成功')
    },
    saveReport() {
      this.$message.success('保存经营报告成功')
      this.handleClose()
    }
  }
}
</script>

<style scoped>
.stat-card {
  height: 100px;
}
.stat-content {
  display: flex;
  align-items: center;
  height: 60px;
}
.stat-icon {
  font-size: 36px;
  margin-right: 16px;
}
.stat-info {
  flex: 1;
}
.stat-value {
  font-size: 22px;
  font-weight: bold;
  color: #303133;
  line-height: 1.2;
}
.stat-label {
  font-size: 13px;
  color: #909399;
  margin-top: 4px;
}
.analysis-content {
  line-height: 1.8;
}
.analysis-content h3 {
  color: #409EFF;
  margin-top: 20px;
  margin-bottom: 10px;
}
.analysis-content p {
  text-indent: 2em;
  margin-bottom: 15px;
}
.growth-high {
  color: #67C23A;
  font-weight: bold;
}
.growth-medium {
  color: #E6A23C;
  font-weight: bold;
}
.growth-low {
  color: #F56C6C;
  font-weight: bold;
}
.change-increase {
  color: #F56C6C;
}
.change-decrease {
  color: #67C23A;
}
.change-stable {
  color: #909399;
}
</style>
