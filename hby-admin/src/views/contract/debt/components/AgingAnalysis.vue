<template>
  <el-dialog
    title="账龄分析"
    :visible.sync="dialogVisible"
    width="90%"
    :close-on-click-modal="false"
    :close-on-press-escape="false"
    @close="handleClose"
  >
    <el-tabs v-model="activeTab" type="card">
      <!-- 账龄分布 -->
      <el-tab-pane label="账龄分布" name="distribution">
        <div class="aging-distribution">
          <el-row :gutter="20">
            <el-col :span="6">
              <el-card>
                <div slot="header">
                  <span>30天以内</span>
                </div>
                <div class="aging-item">
                  <div class="aging-count">{{ agingData.within30Days.count }}笔</div>
                  <div class="aging-amount normal">{{ formatMoney(agingData.within30Days.amount) }}</div>
                </div>
              </el-card>
            </el-col>
            <el-col :span="6">
              <el-card>
                <div slot="header">
                  <span>31-90天</span>
                </div>
                <div class="aging-item">
                  <div class="aging-count">{{ agingData.within90Days.count }}笔</div>
                  <div class="aging-amount warning">{{ formatMoney(agingData.within90Days.amount) }}</div>
                </div>
              </el-card>
            </el-col>
            <el-col :span="6">
              <el-card>
                <div slot="header">
                  <span>91-180天</span>
                </div>
                <div class="aging-item">
                  <div class="aging-count">{{ agingData.within180Days.count }}笔</div>
                  <div class="aging-amount danger">{{ formatMoney(agingData.within180Days.amount) }}</div>
                </div>
              </el-card>
            </el-col>
            <el-col :span="6">
              <el-card>
                <div slot="header">
                  <span>180天以上</span>
                </div>
                <div class="aging-item">
                  <div class="aging-count">{{ agingData.over180Days.count }}笔</div>
                  <div class="aging-amount critical">{{ formatMoney(agingData.over180Days.amount) }}</div>
                </div>
              </el-card>
            </el-col>
          </el-row>

          <el-row :gutter="20" style="margin-top: 20px;">
            <el-col :span="12">
              <el-card>
                <div slot="header">
                  <span>账龄结构占比</span>
                </div>
                <div class="chart-container">
                  <div class="chart-placeholder">
                    <i class="el-icon-pie-chart" style="font-size: 48px; color: #ddd;"></i>
                    <p>饼图占位符</p>
                  </div>
                </div>
              </el-card>
            </el-col>
            <el-col :span="12">
              <el-card>
                <div slot="header">
                  <span>风险等级分布</span>
                </div>
                <div class="risk-distribution">
                  <div class="risk-item">
                    <el-tag type="success">低风险</el-tag>
                    <span class="risk-amount">{{ formatMoney(riskData.lowRisk.amount) }}</span>
                    <span class="risk-count">({{ riskData.lowRisk.count }}笔)</span>
                  </div>
                  <div class="risk-item">
                    <el-tag type="warning">中风险</el-tag>
                    <span class="risk-amount">{{ formatMoney(riskData.mediumRisk.amount) }}</span>
                    <span class="risk-count">({{ riskData.mediumRisk.count }}笔)</span>
                  </div>
                  <div class="risk-item">
                    <el-tag type="danger">高风险</el-tag>
                    <span class="risk-amount">{{ formatMoney(riskData.highRisk.amount) }}</span>
                    <span class="risk-count">({{ riskData.highRisk.count }}笔)</span>
                  </div>
                </div>
              </el-card>
            </el-col>
          </el-row>
        </div>
      </el-tab-pane>

      <!-- 详细列表 -->
      <el-tab-pane label="详细列表" name="details">
        <div style="margin-bottom: 20px;">
          <el-form :inline="true" :model="filterForm" size="small">
            <el-form-item label="账龄范围">
              <el-select v-model="filterForm.agingRange" placeholder="请选择账龄范围" @change="handleFilterChange">
                <el-option label="全部" value="" />
                <el-option label="30天以内" value="0-30" />
                <el-option label="31-90天" value="31-90" />
                <el-option label="91-180天" value="91-180" />
                <el-option label="180天以上" value="180+" />
              </el-select>
            </el-form-item>
            <el-form-item label="风险等级">
              <el-select v-model="filterForm.riskLevel" placeholder="请选择风险等级" @change="handleFilterChange">
                <el-option label="全部" value="" />
                <el-option label="低风险" :value="1" />
                <el-option label="中风险" :value="2" />
                <el-option label="高风险" :value="3" />
                <el-option label="极高风险" :value="4" />
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleExport">导出报表</el-button>
            </el-form-item>
          </el-form>
        </div>

        <el-table :data="filteredDebtList" border style="width: 100%">
          <el-table-column label="项目名称" prop="projectName" min-width="200" />
          <el-table-column label="债务人" prop="debtorName" width="150" />
          <el-table-column label="债权金额" prop="debtAmount" width="120">
            <template slot-scope="{ row }">
              {{ formatMoney(row.debtAmount) }}
            </template>
          </el-table-column>
          <el-table-column label="剩余金额" prop="remainingAmount" width="120">
            <template slot-scope="{ row }">
              {{ formatMoney(row.remainingAmount) }}
            </template>
          </el-table-column>
          <el-table-column label="逾期天数" prop="overdueDays" width="100">
            <template slot-scope="{ row }">
              <span :class="getOverdueClass(row.overdueDays)">
                {{ row.overdueDays }}天
              </span>
            </template>
          </el-table-column>
          <el-table-column label="账龄区间" prop="agingRange" width="100">
            <template slot-scope="{ row }">
              <el-tag :type="getAgingRangeType(row.overdueDays)">
                {{ getAgingRangeName(row.overdueDays) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="风险等级" prop="riskLevel" width="100">
            <template slot-scope="{ row }">
              <el-tag :type="getRiskLevelType(row.riskLevel)">
                {{ getRiskLevelName(row.riskLevel) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="到期日期" prop="dueDate" width="120" />
          <el-table-column label="催收状态" prop="collectionStatus" width="100">
            <template slot-scope="{ row }">
              <el-tag :type="getCollectionStatusType(row.collectionStatus)">
                {{ getCollectionStatusName(row.collectionStatus) }}
              </el-tag>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>

      <!-- 趋势分析 -->
      <el-tab-pane label="趋势分析" name="trend">
        <div class="trend-analysis">
          <el-row :gutter="20">
            <el-col :span="12">
              <el-card>
                <div slot="header">
                  <span>月度债权变化趋势</span>
                </div>
                <div class="chart-container">
                  <div class="chart-placeholder">
                    <i class="el-icon-s-data" style="font-size: 48px; color: #ddd;"></i>
                    <p>折线图占位符</p>
                  </div>
                </div>
              </el-card>
            </el-col>
            <el-col :span="12">
              <el-card>
                <div slot="header">
                  <span>回收率趋势</span>
                </div>
                <div class="chart-container">
                  <div class="chart-placeholder">
                    <i class="el-icon-s-marketing" style="font-size: 48px; color: #ddd;"></i>
                    <p>柱状图占位符</p>
                  </div>
                </div>
              </el-card>
            </el-col>
          </el-row>

          <el-row :gutter="20" style="margin-top: 20px;">
            <el-col :span="24">
              <el-card>
                <div slot="header">
                  <span>关键指标趋势</span>
                </div>
                <div class="trend-indicators">
                  <el-row :gutter="20">
                    <el-col :span="6">
                      <div class="indicator-item">
                        <div class="indicator-label">平均账龄</div>
                        <div class="indicator-value">{{ trendData.averageAging }}天</div>
                        <div class="indicator-change" :class="trendData.agingChange > 0 ? 'increase' : 'decrease'">
                          <i :class="trendData.agingChange > 0 ? 'el-icon-top' : 'el-icon-bottom'"></i>
                          {{ Math.abs(trendData.agingChange) }}%
                        </div>
                      </div>
                    </el-col>
                    <el-col :span="6">
                      <div class="indicator-item">
                        <div class="indicator-label">回收率</div>
                        <div class="indicator-value">{{ trendData.recoveryRate }}%</div>
                        <div class="indicator-change" :class="trendData.recoveryChange > 0 ? 'increase' : 'decrease'">
                          <i :class="trendData.recoveryChange > 0 ? 'el-icon-top' : 'el-icon-bottom'"></i>
                          {{ Math.abs(trendData.recoveryChange) }}%
                        </div>
                      </div>
                    </el-col>
                    <el-col :span="6">
                      <div class="indicator-item">
                        <div class="indicator-label">逾期率</div>
                        <div class="indicator-value">{{ trendData.overdueRate }}%</div>
                        <div class="indicator-change" :class="trendData.overdueChange > 0 ? 'increase' : 'decrease'">
                          <i :class="trendData.overdueChange > 0 ? 'el-icon-top' : 'el-icon-bottom'"></i>
                          {{ Math.abs(trendData.overdueChange) }}%
                        </div>
                      </div>
                    </el-col>
                    <el-col :span="6">
                      <div class="indicator-item">
                        <div class="indicator-label">坏账率</div>
                        <div class="indicator-value">{{ trendData.badDebtRate }}%</div>
                        <div class="indicator-change" :class="trendData.badDebtChange > 0 ? 'increase' : 'decrease'">
                          <i :class="trendData.badDebtChange > 0 ? 'el-icon-top' : 'el-icon-bottom'"></i>
                          {{ Math.abs(trendData.badDebtChange) }}%
                        </div>
                      </div>
                    </el-col>
                  </el-row>
                </div>
              </el-card>
            </el-col>
          </el-row>
        </div>
      </el-tab-pane>
    </el-tabs>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">关闭</el-button>
      <el-button type="primary" @click="handleRefresh">刷新数据</el-button>
    </div>
  </el-dialog>
</template>

<script>
  import {
    getDebtAgingAnalysis,
    exportDebtAgingReport
  } from '@/api/contract/debt'

  export default {
    name: 'AgingAnalysis',
    data() {
      return {
        dialogVisible: false,
        activeTab: 'distribution',
        agingData: {
          within30Days: { count: 0, amount: 0 },
          within90Days: { count: 0, amount: 0 },
          within180Days: { count: 0, amount: 0 },
          over180Days: { count: 0, amount: 0 }
        },
        riskData: {
          lowRisk: { count: 0, amount: 0 },
          mediumRisk: { count: 0, amount: 0 },
          highRisk: { count: 0, amount: 0 }
        },
        trendData: {
          averageAging: 0,
          agingChange: 0,
          recoveryRate: 0,
          recoveryChange: 0,
          overdueRate: 0,
          overdueChange: 0,
          badDebtRate: 0,
          badDebtChange: 0
        },
        debtList: [],
        filteredDebtList: [],
        filterForm: {
          agingRange: '',
          riskLevel: ''
        }
      }
    },
    methods: {
      async showEdit() {
        this.dialogVisible = true
        await this.loadAgingData()
      },

      async loadAgingData() {
        try {
          const response = await getDebtAgingAnalysis()
          if (response.code === 1) {
            const data = response.data || {}
            this.agingData = data.agingData || this.agingData
            this.riskData = data.riskData || this.riskData
            this.trendData = data.trendData || this.trendData
            this.debtList = data.debtList || []
            this.filteredDebtList = [...this.debtList]
          } else {
            this.$message.error(response.msg || '获取账龄分析数据失败')
          }
        } catch (error) {
          console.error('加载账龄分析数据失败：', error)
          this.$message.error('加载账龄分析数据失败：' + error.message)
        }
      },

      handleClose() {
        this.dialogVisible = false
        this.activeTab = 'distribution'
        this.filterForm = {
          agingRange: '',
          riskLevel: ''
        }
      },

      handleFilterChange() {
        this.filteredDebtList = this.debtList.filter(item => {
          let agingMatch = true
          let riskMatch = true

          // 账龄范围筛选
          if (this.filterForm.agingRange) {
            const overdueDays = item.overdueDays || 0
            switch (this.filterForm.agingRange) {
              case '0-30':
                agingMatch = overdueDays <= 30
                break
              case '31-90':
                agingMatch = overdueDays > 30 && overdueDays <= 90
                break
              case '91-180':
                agingMatch = overdueDays > 90 && overdueDays <= 180
                break
              case '180+':
                agingMatch = overdueDays > 180
                break
            }
          }

          // 风险等级筛选
          if (this.filterForm.riskLevel) {
            riskMatch = item.riskLevel === this.filterForm.riskLevel
          }

          return agingMatch && riskMatch
        })
      },

      async handleExport() {
        try {
          const response = await exportDebtAgingReport({
            agingRange: this.filterForm.agingRange,
            riskLevel: this.filterForm.riskLevel
          })

          if (response.code === 200) {
            this.$message.success('报表导出成功')
            // 这里可以添加文件下载逻辑
          } else {
            this.$message.error(response.message || '导出失败')
          }
        } catch (error) {
          this.$message.error('导出失败：' + error.message)
        }
      },

      async handleRefresh() {
        await this.loadAgingData()
        this.$message.success('数据刷新成功')
      },

      formatMoney(amount) {
        if (!amount) return '0'
        return (amount / 10000).toFixed(2) + '万元'
      },

      getOverdueClass(days) {
        if (days <= 30) return 'normal'
        if (days <= 90) return 'warning'
        if (days <= 180) return 'danger'
        return 'critical'
      },

      getAgingRangeName(days) {
        if (days <= 30) return '30天内'
        if (days <= 90) return '31-90天'
        if (days <= 180) return '91-180天'
        return '180天以上'
      },

      getAgingRangeType(days) {
        if (days <= 30) return 'success'
        if (days <= 90) return 'warning'
        if (days <= 180) return 'danger'
        return 'danger'
      },

      getRiskLevelName(level) {
        const levelMap = {
          1: '低风险',
          2: '中风险',
          3: '高风险',
          4: '极高风险'
        }
        return levelMap[level] || '未知'
      },

      getRiskLevelType(level) {
        const typeMap = {
          1: 'success',
          2: 'warning',
          3: 'danger',
          4: 'danger'
        }
        return typeMap[level] || 'info'
      },

      getCollectionStatusName(status) {
        const statusMap = {
          1: '未催收',
          2: '催收中',
          3: '已承诺',
          4: '法律程序',
          5: '已放弃'
        }
        return statusMap[status] || '未知'
      },

      getCollectionStatusType(status) {
        const typeMap = {
          1: 'info',
          2: 'primary',
          3: 'success',
          4: 'warning',
          5: 'danger'
        }
        return typeMap[status] || 'info'
      }
    }
  }
</script>

<style scoped>
  .dialog-footer {
    text-align: right;
  }

  .aging-distribution .aging-item {
    text-align: center;
    padding: 20px 0;
  }

  .aging-count {
    font-size: 16px;
    color: #666;
    margin-bottom: 10px;
  }

  .aging-amount {
    font-size: 20px;
    font-weight: bold;
  }

  .aging-amount.normal {
    color: #67c23a;
  }

  .aging-amount.warning {
    color: #e6a23c;
  }

  .aging-amount.danger {
    color: #f56c6c;
  }

  .aging-amount.critical {
    color: #909399;
  }

  .chart-container {
    height: 200px;
    display: flex;
    align-items: center;
    justify-content: center;
  }

  .chart-placeholder {
    text-align: center;
    color: #999;
  }

  .risk-distribution {
    padding: 20px;
  }

  .risk-item {
    display: flex;
    align-items: center;
    margin-bottom: 15px;
  }

  .risk-amount {
    margin-left: 15px;
    font-weight: bold;
    font-size: 16px;
  }

  .risk-count {
    margin-left: 10px;
    color: #666;
  }

  .trend-indicators {
    padding: 20px;
  }

  .indicator-item {
    text-align: center;
    padding: 20px;
    border: 1px solid #ebeef5;
    border-radius: 4px;
  }

  .indicator-label {
    font-size: 14px;
    color: #666;
    margin-bottom: 10px;
  }

  .indicator-value {
    font-size: 24px;
    font-weight: bold;
    color: #303133;
    margin-bottom: 5px;
  }

  .indicator-change {
    font-size: 12px;
  }

  .indicator-change.increase {
    color: #f56c6c;
  }

  .indicator-change.decrease {
    color: #67c23a;
  }

  .normal {
    color: #67c23a;
  }

  .warning {
    color: #e6a23c;
  }

  .danger {
    color: #f56c6c;
  }

  .critical {
    color: #909399;
    font-weight: bold;
  }
</style>
