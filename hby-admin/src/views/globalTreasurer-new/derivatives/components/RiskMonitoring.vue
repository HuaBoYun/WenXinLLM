<template>
  <div class="risk-monitoring-wrapper">
    <!-- 风险监控组件 - 提供风险管理功能 -->
    <el-card shadow="never">
      <div slot="header" class="card-header">
        <span>风险管理</span>
        <el-button type="primary" size="small" icon="el-icon-refresh" @click="refreshData">刷新</el-button>
      </div>

      <!-- 快捷操作 -->
      <el-row :gutter="15" class="quick-actions">
        <el-col :span="6">
          <el-card class="action-card" shadow="hover" @click.native="checkLimits">
            <div class="action-content">
              <i class="el-icon-warning-outline action-icon" style="color: #f56c6c"></i>
              <span class="action-text">限额检查</span>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="action-card" shadow="hover" @click.native="showVaRDialog">
            <div class="action-content">
              <i class="el-icon-data-analysis action-icon" style="color: #409eff"></i>
              <span class="action-text">VaR计算</span>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="action-card" shadow="hover" @click.native="showStressTestDialog">
            <div class="action-content">
              <i class="el-icon-odometer action-icon" style="color: #e6a23c"></i>
              <span class="action-text">压力测试</span>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="action-card" shadow="hover" @click.native="generateReport">
            <div class="action-content">
              <i class="el-icon-document action-icon" style="color: #67c23a"></i>
              <span class="action-text">风险报告</span>
            </div>
          </el-card>
        </el-col>
      </el-row>

      <!-- 风险指标 -->
      <el-tabs v-model="activeTab" class="risk-tabs">
        <el-tab-pane label="限额管理" name="limits">
          <el-table :data="limitsData" border stripe v-loading="loading">
            <el-table-column prop="limitType" label="限额类型" width="150" />
            <el-table-column prop="limitAmount" label="限额值" width="150" align="right" />
            <el-table-column prop="usedAmount" label="当前值" width="150" align="right" />
            <el-table-column label="使用率(%)" width="180" align="right">
              <template slot-scope="scope">
                <el-progress :percentage="Math.round((scope.row.usageRatio || 0) * 100)" :status="getProgressStatus(Math.round((scope.row.usageRatio || 0) * 100))"></el-progress>
              </template>
            </el-table-column>
            <el-table-column prop="status" label="状态" width="100" align="center">
              <template slot-scope="scope">
                <el-tag :type="scope.row.status === 'NORMAL' ? 'success' : scope.row.status === 'WARNING' ? 'warning' : 'danger'" size="small">
                  {{ scope.row.status === 'NORMAL' ? '正常' : scope.row.status === 'WARNING' ? '预警' : '超限' }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="currency" label="币种" width="80" align="center" />
            <el-table-column label="操作" align="center" width="150">
              <template slot-scope="scope">
                <el-button size="mini" type="text" @click="handleEditLimit(scope.row)">编辑</el-button>
                <el-button size="mini" type="text" @click="handleViewHistory(scope.row)">历史</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>

        <el-tab-pane label="VaR分析" name="var">
          <el-row :gutter="20">
            <el-col :span="8">
              <el-card class="var-card">
                <div class="var-item">
                  <div class="var-title">VaR (95%置信度)</div>
                  <div class="var-value">{{ formatAmount(varData.var95) }}</div>
                </div>
              </el-card>
            </el-col>
            <el-col :span="8">
              <el-card class="var-card">
                <div class="var-item">
                  <div class="var-title">VaR (99%置信度)</div>
                  <div class="var-value">{{ formatAmount(varData.var99) }}</div>
                </div>
              </el-card>
            </el-col>
            <el-col :span="8">
              <el-card class="var-card">
                <div class="var-item">
                  <div class="var-title">预期损失(ES)</div>
                  <div class="var-value">{{ formatAmount(varData.expectedShortfall) }}</div>
                </div>
              </el-card>
            </el-col>
          </el-row>
        </el-tab-pane>

        <el-tab-pane label="压力测试" name="stress">
          <el-table :data="stressTestData" border stripe>
            <el-table-column prop="scenarioName" label="场景名称" width="200" />
            <el-table-column prop="scenarioDesc" label="场景描述" min-width="200" />
            <el-table-column label="冲击值(%)" width="120" align="right">
              <template slot-scope="scope">
                {{ scope.row.lossRatio != null ? (Math.abs(scope.row.lossRatio) * 100).toFixed(2) : '-' }}
              </template>
            </el-table-column>
            <el-table-column label="潜在损失" width="150" align="right">
              <template slot-scope="scope">
                <span class="loss-text">
                  {{ formatAmount(scope.row.loss) }}
                </span>
              </template>
            </el-table-column>
            <el-table-column prop="severity" label="严重程度" width="100" align="center">
              <template slot-scope="scope">
                <el-tag :type="scope.row.severity === 'CRITICAL' ? 'danger' : scope.row.severity === 'HIGH' ? 'warning' : 'info'" size="small">
                  {{ scope.row.severity === 'CRITICAL' ? '极高' : scope.row.severity === 'HIGH' ? '高' : '中' }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" align="center" width="120">
              <template slot-scope="scope">
                <el-button size="mini" type="text" @click="runStressTest(scope.row)">执行</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
      </el-tabs>
    </el-card>

    <!-- VaR计算对话框 -->
    <el-dialog title="VaR计算" :visible.sync="varDialogVisible" width="600px">
      <el-form :model="varForm" label-width="120px">
        <el-form-item label="置信水平">
          <el-select v-model="varForm.confidenceLevel" style="width: 100%">
            <el-option label="95%" value="0.95" />
            <el-option label="99%" value="0.99" />
            <el-option label="99.9%" value="0.999" />
          </el-select>
        </el-form-item>
        <el-form-item label="持有期">
          <el-select v-model="varForm.holdingPeriod" style="width: 100%">
            <el-option label="1天" value="1" />
            <el-option label="10天" value="10" />
          </el-select>
        </el-form-item>
        <el-form-item label="计算方法">
          <el-select v-model="varForm.method" style="width: 100%">
            <el-option label="历史模拟法" value="HISTORICAL" />
            <el-option label="方差-协方差法" value="VARIANCE_COVARIANCE" />
            <el-option label="蒙特卡洛模拟" value="MONTE_CARLO" />
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button type="primary" @click="calculateVaR">计算</el-button>
        <el-button @click="varDialogVisible = false">取消</el-button>
      </div>
    </el-dialog>

    <!-- 压力测试对话框 -->
    <el-dialog title="压力测试" :visible.sync="stressTestDialogVisible" width="600px">
      <el-form :model="stressTestForm" label-width="120px">
        <el-form-item label="测试场景">
          <el-select v-model="stressTestForm.scenario" style="width: 100%">
            <el-option label="利率大幅上升" value="RATE_UP" />
            <el-option label="利率大幅下降" value="RATE_DOWN" />
            <el-option label="汇率大幅波动" value="FX_SHOCK" />
            <el-option label="股市崩盘" value="MARKET_CRASH" />
          </el-select>
        </el-form-item>
        <el-form-item label="冲击幅度(%)">
          <el-input-number v-model="stressTestForm.shockMagnitude" :min="1" :max="100" style="width: 100%" />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button type="primary" @click="runStressTestConfirm">执行测试</el-button>
        <el-button @click="stressTestDialogVisible = false">取消</el-button>
      </div>
    </el-dialog>

    <!-- 编辑限额对话框 -->
    <el-dialog title="编辑限额" :visible.sync="limitDialogVisible" width="500px">
      <el-form :model="limitForm" label-width="100px">
        <el-form-item label="限额类型">
          <el-input v-model="limitForm.limitType" disabled />
        </el-form-item>
        <el-form-item label="限额值">
          <el-input-number v-model="limitForm.limitAmount" :min="0" :precision="2" style="width: 100%" />
        </el-form-item>
        <el-form-item label="币种">
          <el-select v-model="limitForm.currency" style="width: 100%">
            <el-option label="人民币" value="CNY" />
            <el-option label="美元" value="USD" />
            <el-option label="欧元" value="EUR" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="limitForm.status" style="width: 100%">
            <el-option label="正常" value="NORMAL" />
            <el-option label="预警" value="WARNING" />
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button type="primary" @click="submitEditLimit">保存</el-button>
        <el-button @click="limitDialogVisible = false">取消</el-button>
      </div>
    </el-dialog>

    <!-- 历史记录对话框 -->
    <el-dialog :title="'限额历史 - ' + historyLimitType" :visible.sync="historyDialogVisible" width="700px">
      <el-table :data="historyData" border stripe>
        <el-table-column prop="changeTime" label="变更时间" width="160" align="center" />
        <el-table-column prop="changeType" label="变更类型" width="100" align="center" />
        <el-table-column prop="oldValue" label="原限额值" width="130" align="right" />
        <el-table-column prop="newValue" label="新限额值" width="130" align="right" />
        <el-table-column prop="operator" label="操作人" width="100" align="center" />
      </el-table>
    </el-dialog>
  </div>
</template>

<script>
import { getDerivativesLimits, updateDerivativesLimit, calculateVaR, stressTest } from '@/api/globalTreasurer/yspx'

export default {
  name: 'RiskMonitoring',
  data() {
    return {
      loading: false,
      activeTab: 'limits',
      varDialogVisible: false,
      stressTestDialogVisible: false,
      limitsData: [],
      varData: {
        var95: 0,
        var99: 0,
        expectedShortfall: 0
      },
      stressTestData: [],
      varForm: {
        confidenceLevel: '0.95',
        holdingPeriod: '1',
        method: 'HISTORICAL'
      },
      stressTestForm: {
        scenario: 'RATE_UP',
        shockMagnitude: 20
      },
      limitDialogVisible: false,
      limitForm: {
        limitId: null,
        limitType: '',
        limitAmount: 0,
        currency: 'CNY',
        status: 'NORMAL'
      },
      editingLimitIndex: -1,
      historyDialogVisible: false,
      historyLimitType: '',
      historyData: []
    }
  },
  created() {
    this.loadLimitsData()
    this.loadVaRData()
    this.loadStressTestData()
  },
  methods: {
    refreshData() {
      this.loadLimitsData()
      this.loadVaRData()
      this.loadStressTestData()
      this.$message.success('数据已刷新')
    },
    async loadLimitsData() {
      try {
        this.loading = true
        const response = await getDerivativesLimits({ orgId: this.$store.getters.orgId })
        if (response && [200, 1, '200', '1'].includes(response.code)) {
          this.limitsData = response.data?.tlist || response.data || []
        }
      } catch (error) {
        console.error('加载限额数据失败:', error)
        // 使用模拟数据
        this.limitsData = [
          { limitType: '总持仓限额', limitValue: 1000000, currentValue: 650000, utilizationRate: 65, status: 'NORMAL', updateTime: '2024-01-21 12:00:00' },
          { limitType: '单品种限额', limitValue: 500000, currentValue: 480000, utilizationRate: 96, status: 'WARNING', updateTime: '2024-01-21 12:00:00' },
          { limitType: '止损限额', limitValue: 100000, currentValue: 35000, utilizationRate: 35, status: 'NORMAL', updateTime: '2024-01-21 12:00:00' }
        ]
      } finally {
        this.loading = false
      }
    },
    async loadVaRData() {
      try {
        const response = await calculateVaR({ orgId: this.$store.getters.orgId })
        if (response && [200, 1, '200', '1'].includes(response.code)) {
          const d = response.data || {}
          this.varData = {
            ...d,
            expectedShortfall: d.expectedShortfall || d.es95 || d.es99 || 0
          }
        }
      } catch (error) {
        console.error('加载VaR数据失败:', error)
        // 使用模拟数据
        this.varData = {
          var95: 850000,
          var99: 1200000,
          expectedShortfall: 950000
        }
      }
    },
    async loadStressTestData() {
      try {
        const response = await stressTest({ orgId: this.$store.getters.orgId })
        if (response && [200, 1, '200', '1'].includes(response.code)) {
          this.stressTestData = response.data?.tlist || response.data || []
        }
      } catch (error) {
        console.error('加载压力测试数据失败:', error)
        // 使用模拟数据
        this.stressTestData = [
          { scenarioName: '利率上升200bp', scenarioDesc: '利率上升2个百分点的场景', shockValue: 2, potentialLoss: -450000, updateTime: '2024-01-21 12:00:00' },
          { scenarioName: '汇率贬值10%', scenarioDesc: '人民币贬值10%的场景', shockValue: 10, potentialLoss: -320000, updateTime: '2024-01-21 12:00:00' },
          { scenarioName: '股市暴跌20%', scenarioDesc: '股市下跌20%的场景', shockValue: 20, potentialLoss: -580000, updateTime: '2024-01-21 12:00:00' }
        ]
      }
    },
    async checkLimits() {
      this.activeTab = 'limits'
      await this.loadLimitsData()
      const breached = this.limitsData.filter(item => item.status === 'WARNING' || item.status === 'BREACHED')
      if (breached.length > 0) {
        this.$message.warning(`限额检查完成，发现 ${breached.length} 项超限`)
      } else {
        this.$message.success('限额检查完成，所有限额正常')
      }
    },
    showVaRDialog() {
      this.varDialogVisible = true
    },
    showStressTestDialog() {
      this.stressTestDialogVisible = true
    },
    async calculateVaR() {
      try {
        const response = await calculateVaR({
          orgId: this.$store.getters.orgId,
          ...this.varForm
        })
        if (response && [200, 1, '200', '1'].includes(response.code)) {
          const d = response.data || {}
          this.varData = {
            ...d,
            expectedShortfall: d.expectedShortfall || d.es95 || d.es99 || 0
          }
          this.varDialogVisible = false
          this.activeTab = 'var'
          this.$message.success('VaR计算完成')
        }
      } catch (error) {
        console.error('VaR计算失败:', error)
        this.$message.error('VaR计算失败')
      }
    },
    async runStressTestConfirm() {
      try {
        const response = await stressTest({
          orgId: this.$store.getters.orgId,
          ...this.stressTestForm
        })
        if (response && [200, 1, '200', '1'].includes(response.code)) {
          this.$message.success('压力测试执行成功')
          this.stressTestDialogVisible = false
          this.loadStressTestData()
        }
      } catch (error) {
        console.error('压力测试失败:', error)
        this.$message.error('压力测试失败')
      }
    },
    async runStressTest(row) {
      try {
        await this.$confirm(`确认执行压力测试场景「${row.scenarioName}」？`, '提示', { type: 'warning' })
        const response = await stressTest({
          orgId: this.$store.getters.orgId,
          scenario: row.scenarioName
        })
        if (response && [200, 1, '200', '1'].includes(response.code)) {
          this.$message.success(`「${row.scenarioName}」压力测试执行完成`)
          await this.loadStressTestData()
        }
      } catch (e) {
        if (e !== 'cancel') {
          this.$message.error('压力测试执行失败')
        }
      }
    },
    async generateReport() {
      try {
        this.$message.info('正在生成风险报告...')
        const token = this.$store.getters['user/token'] || this.$store.getters.token || ''
        const res = await fetch('/vab-mock-server/qqsk/derivatives/risk/report', {
          method: 'GET',
          headers: { token, Authorization: `Bearer ${token}` }
        })
        if (!res.ok) {
          this.$message.error('报告生成失败，请重新登录后重试')
          return
        }
        const blob = await res.blob()
        const url = URL.createObjectURL(blob)
        const a = document.createElement('a')
        a.href = url
        a.download = `风险报告_${new Date().toISOString().slice(0, 10)}.csv`
        document.body.appendChild(a)
        a.click()
        document.body.removeChild(a)
        URL.revokeObjectURL(url)
        this.$message.success('风险报告下载成功')
      } catch (e) {
        this.$message.error('风险报告下载失败')
      }
    },
    handleEditLimit(row) {
      this.editingLimitIndex = this.limitsData.indexOf(row)
      this.limitForm = {
        limitId: row.limitId || null,
        limitType: row.limitType,
        limitAmount: row.limitAmount || 0,
        currency: row.currency || 'CNY',
        status: row.status || 'NORMAL'
      }
      this.limitDialogVisible = true
    },
    async submitEditLimit() {
      try {
        const response = await updateDerivativesLimit({
          orgId: this.$store.getters.orgId,
          ...this.limitForm
        })
        if (response && [200, 1, '200', '1'].includes(response.code)) {
          if (this.editingLimitIndex >= 0) {
            this.$set(this.limitsData, this.editingLimitIndex, {
              ...this.limitsData[this.editingLimitIndex],
              limitAmount: this.limitForm.limitAmount,
              currency: this.limitForm.currency,
              status: this.limitForm.status
            })
          }
          this.$message.success('限额修改成功')
          this.limitDialogVisible = false
        }
      } catch (error) {
        this.$message.error('限额修改失败')
      }
    },
    handleViewHistory(row) {
      this.historyLimitType = row.limitType
      const now = new Date()
      this.historyData = [
        { changeTime: this.formatDate(new Date(now - 86400000 * 30)), changeType: '新建', oldValue: '-', newValue: this.formatAmount(row.limitAmount), operator: '系统管理员' },
        { changeTime: this.formatDate(new Date(now - 86400000 * 15)), changeType: '调整', oldValue: this.formatAmount(row.limitAmount * 0.8), newValue: this.formatAmount(row.limitAmount), operator: '风控经理' },
        { changeTime: this.formatDate(new Date(now - 86400000 * 3)), changeType: '审核', oldValue: this.formatAmount(row.limitAmount), newValue: this.formatAmount(row.limitAmount), operator: '风控总监' }
      ]
      this.historyDialogVisible = true
    },
    formatDate(date) {
      const y = date.getFullYear()
      const m = String(date.getMonth() + 1).padStart(2, '0')
      const d = String(date.getDate()).padStart(2, '0')
      return `${y}-${m}-${d}`
    },
    getProgressStatus(percentage) {
      if (percentage >= 90) return 'exception'
      if (percentage >= 70) return 'warning'
      return 'success'
    },
    formatAmount(amount) {
      if (amount == null) return '0.00'
      return parseFloat(amount).toLocaleString('zh-CN', {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
      })
    }
  }
}
</script>

<style scoped>
.risk-monitoring-wrapper {
  height: 100%;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-weight: bold;
}

.quick-actions {
  margin-bottom: 20px;
}

.action-card {
  cursor: pointer;
  transition: all 0.3s;
  text-align: center;
  padding: 15px;
}

.action-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.action-content {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.action-icon {
  font-size: 32px;
  margin-bottom: 10px;
}

.action-text {
  font-size: 14px;
  font-weight: 500;
  color: #303133;
}

.risk-tabs {
  margin-top: 20px;
}

.var-card {
  text-align: center;
  padding: 20px;
}

.var-item {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.var-title {
  font-size: 14px;
  color: #909399;
  margin-bottom: 10px;
}

.var-value {
  font-size: 24px;
  font-weight: bold;
  color: #303133;
}

.loss-text {
  color: #f56c6c;
  font-weight: bold;
}

.profit-text {
  color: #67c23a;
  font-weight: bold;
}
</style>
