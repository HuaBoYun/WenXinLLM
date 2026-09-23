<template>
  <div class="finance-workspace">
    <!-- 统计卡片 -->
    <el-row :gutter="20" class="stats-cards">
      <el-col :span="6">
        <el-card class="stats-card">
          <div class="stats-content">
            <div class="stats-icon review">
              <i class="el-icon-view"></i>
            </div>
            <div class="stats-info">
              <div class="stats-number">{{ pendingStats.pendingReview || 0 }}</div>
              <div class="stats-label">待审核</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stats-card">
          <div class="stats-content">
            <div class="stats-icon payment">
              <i class="el-icon-wallet"></i>
            </div>
            <div class="stats-info">
              <div class="stats-number">{{ pendingStats.pendingPayment || 0 }}</div>
              <div class="stats-label">待付款</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stats-card">
          <div class="stats-content">
            <div class="stats-icon audit">
              <i class="el-icon-finished"></i>
            </div>
            <div class="stats-info">
              <div class="stats-number">{{ pendingStats.pendingAudit || 0 }}</div>
              <div class="stats-label">待稽核</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stats-card">
          <div class="stats-content">
            <div class="stats-icon archive">
              <i class="el-icon-folder"></i>
            </div>
            <div class="stats-info">
              <div class="stats-number">{{ pendingStats.pendingArchive || 0 }}</div>
              <div class="stats-label">待归档</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="main-content">
      <!-- 左侧内容 -->
      <el-col :span="16">
        <!-- 本月处理统计 -->
        <el-card class="monthly-stats" title="本月处理统计">
          <div slot="header" class="card-header">
            <span>本月处理统计</span>
            <el-button style="float: right; padding: 3px 0" type="text" @click="viewMonthlyDetail">查看详情</el-button>
          </div>
          <el-row :gutter="20">
            <el-col :span="6">
              <div class="stat-item">
                <div class="stat-value">{{ monthlyStats.processedCount || 0 }}</div>
                <div class="stat-label">处理单据</div>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="stat-item">
                <div class="stat-value">¥{{ formatAmount(monthlyStats.processedAmount) }}</div>
                <div class="stat-label">处理金额</div>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="stat-item">
                <div class="stat-value">{{ monthlyStats.averageProcessTime || 0 }}天</div>
                <div class="stat-label">平均处理时间</div>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="stat-item">
                <div class="stat-value">{{ monthlyStats.rejectionRate || 0 }}%</div>
                <div class="stat-label">退回率</div>
              </div>
            </el-col>
          </el-row>
        </el-card>

        <!-- 待处理单据 -->
        <el-card class="pending-bills" title="待处理单据">
          <div slot="header" class="card-header">
            <span>待处理单据</span>
            <el-button style="float: right; padding: 3px 0" type="text" @click="viewAllPendingBills">查看全部</el-button>
          </div>
          <el-table :data="pendingBills" style="width: 100%" :show-header="true">
            <el-table-column prop="billTypeName" label="类型" width="100">
              <template slot-scope="scope">
                <el-tag :type="getBillTypeColor(scope.row.billType)" size="small">
                  {{ scope.row.billTypeName }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="billId" label="单据号" width="150" />
            <el-table-column prop="applicant" label="申请人" width="100" />
            <el-table-column prop="amount" label="金额" width="120">
              <template slot-scope="scope">
                <span class="amount">¥{{ formatAmount(scope.row.amount) }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="statusName" label="状态" width="120">
              <template slot-scope="scope">
                <el-tag :type="getStatusColor(scope.row.status)" size="small">
                  {{ scope.row.statusName }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="priority" label="优先级" width="80">
              <template slot-scope="scope">
                <el-tag :type="getPriorityColor(scope.row.priority)" size="mini">
                  {{ scope.row.priority === 'HIGH' ? '高' : scope.row.priority === 'NORMAL' ? '中' : '低' }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="submitTime" label="提交时间" width="160">
              <template slot-scope="scope">
                {{ formatDate(scope.row.submitTime) }}
              </template>
            </el-table-column>
            <el-table-column label="操作" width="150">
              <template slot-scope="scope">
                <el-button size="mini" type="primary" @click="handleBill(scope.row)">处理</el-button>
                <el-button size="mini" type="text" @click="viewBillDetail(scope.row)">查看</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>

      <!-- 右侧内容 -->
      <el-col :span="8">
        <!-- 异常提醒 -->
        <el-card class="alerts" title="异常提醒">
          <div v-if="alerts.length === 0" class="empty-alerts">
            <i class="el-icon-success"></i>
            <p>暂无异常提醒</p>
          </div>
          <div v-else class="alert-items">
            <div 
              v-for="alert in alerts" 
              :key="alert.alertId"
              class="alert-item"
              :class="'alert-' + alert.alertLevel.toLowerCase()"
              @click="handleAlert(alert)"
            >
              <div class="alert-icon">
                <i :class="getAlertIcon(alert.alertLevel)"></i>
              </div>
              <div class="alert-content">
                <div class="alert-message">{{ alert.alertMessage }}</div>
                <div class="alert-time">{{ formatDate(alert.alertTime) }}</div>
              </div>
            </div>
          </div>
        </el-card>

        <!-- 处理效率 -->
        <el-card class="efficiency" title="处理效率">
          <div class="efficiency-chart">
            <div class="efficiency-item">
              <div class="efficiency-label">今日处理</div>
              <div class="efficiency-value">{{ todayStats.processedCount || 0 }}笔</div>
              <div class="efficiency-progress">
                <el-progress 
                  :percentage="getTodayProgress()" 
                  :stroke-width="8"
                  :show-text="false"
                />
              </div>
            </div>
            <div class="efficiency-item">
              <div class="efficiency-label">平均用时</div>
              <div class="efficiency-value">{{ todayStats.averageTime || 0 }}分钟</div>
              <div class="efficiency-progress">
                <el-progress 
                  :percentage="getTimeProgress()" 
                  :stroke-width="8"
                  :show-text="false"
                  color="#67C23A"
                />
              </div>
            </div>
            <div class="efficiency-item">
              <div class="efficiency-label">准确率</div>
              <div class="efficiency-value">{{ todayStats.accuracyRate || 0 }}%</div>
              <div class="efficiency-progress">
                <el-progress 
                  :percentage="todayStats.accuracyRate || 0" 
                  :stroke-width="8"
                  :show-text="false"
                  color="#E6A23C"
                />
              </div>
            </div>
          </div>
        </el-card>

        <!-- 快捷操作 -->
        <el-card class="quick-actions" title="快捷操作">
          <div class="action-grid">
            <div class="action-item" @click="batchApprove">
              <div class="action-icon">
                <i class="el-icon-check"></i>
              </div>
              <div class="action-name">批量审批</div>
            </div>
            <div class="action-item" @click="batchPayment">
              <div class="action-icon">
                <i class="el-icon-wallet"></i>
              </div>
              <div class="action-name">批量付款</div>
            </div>
            <div class="action-item" @click="exportReport">
              <div class="action-icon">
                <i class="el-icon-download"></i>
              </div>
              <div class="action-name">导出报表</div>
            </div>
            <div class="action-item" @click="systemSettings">
              <div class="action-icon">
                <i class="el-icon-setting"></i>
              </div>
              <div class="action-name">系统设置</div>
            </div>
          </div>
        </el-card>

        <!-- 工作日历 -->
        <el-card class="work-calendar" title="工作日历">
          <el-calendar v-model="calendarValue" :range="calendarRange">
            <template slot="dateCell" slot-scope="{date, data}">
              <div class="calendar-cell">
                <div class="date-number">{{ data.day.split('-').slice(2).join('-') }}</div>
                <div class="date-tasks" v-if="getDateTasks(data.day).length > 0">
                  <div 
                    v-for="task in getDateTasks(data.day).slice(0, 2)" 
                    :key="task.id"
                    class="task-dot"
                    :class="'task-' + task.type"
                  ></div>
                  <div v-if="getDateTasks(data.day).length > 2" class="task-more">+{{ getDateTasks(data.day).length - 2 }}</div>
                </div>
              </div>
            </template>
          </el-calendar>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { workspaceApi } from '@/api/financialSharing/coreBusiness'

export default {
  name: 'FinanceWorkspace',
  data() {
    return {
      loading: false,
      pendingStats: {},
      monthlyStats: {},
      pendingBills: [],
      alerts: [],
      todayStats: {},
      calendarValue: new Date(),
      calendarRange: [new Date(), new Date(new Date().getTime() + 30 * 24 * 60 * 60 * 1000)],
      workTasks: []
    }
  },
  mounted() {
    this.loadWorkspaceData()
  },
  methods: {
    async loadWorkspaceData() {
      this.loading = true
      try {
        const userId = this.$store.getters.userId || 'USER001'
        const response = await workspaceApi.getFinanceWorkspace(userId)
        if (response.code === 1) {
          const data = response.data
          this.pendingStats = data.pendingStats || {}
          this.monthlyStats = data.monthlyStats || {}
          this.pendingBills = data.pendingBills || []
          this.alerts = data.alerts || []
          this.todayStats = data.todayStats || {}
          this.workTasks = data.workTasks || []
        } else {
          this.$message.error(response.msg || '加载工作台数据失败')
        }
      } catch (error) {
        this.$message.error('加载工作台数据失败：' + error.message)
      } finally {
        this.loading = false
      }
    },

    formatAmount(amount) {
      if (!amount) return '0.00'
      return Number(amount).toLocaleString('zh-CN', {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
      })
    },

    formatDate(dateStr) {
      if (!dateStr) return ''
      const date = new Date(dateStr)
      return date.toLocaleString('zh-CN', {
        month: '2-digit',
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit'
      })
    },

    getBillTypeColor(billType) {
      const colors = {
        'EXPENSE': 'primary',
        'LOAN': 'success',
        'PREPAYMENT': 'warning',
        'PROVISION': 'info'
      }
      return colors[billType] || 'default'
    },

    getStatusColor(status) {
      const colors = {
        'PENDING_REVIEW': 'warning',
        'PENDING_PAYMENT': 'primary',
        'PENDING_AUDIT': 'info',
        'APPROVED': 'success'
      }
      return colors[status] || 'default'
    },

    getPriorityColor(priority) {
      const colors = {
        'HIGH': 'danger',
        'NORMAL': 'primary',
        'LOW': 'info'
      }
      return colors[priority] || 'default'
    },

    getAlertIcon(level) {
      const icons = {
        'HIGH': 'el-icon-warning',
        'MEDIUM': 'el-icon-info',
        'LOW': 'el-icon-question'
      }
      return icons[level] || 'el-icon-info'
    },

    getTodayProgress() {
      const target = 20 // 目标处理数量
      return Math.min((this.todayStats.processedCount / target) * 100, 100)
    },

    getTimeProgress() {
      const target = 30 // 目标时间（分钟）
      return Math.max(100 - (this.todayStats.averageTime / target) * 100, 0)
    },

    getDateTasks(date) {
      return this.workTasks.filter(task => task.date === date)
    },

    handleBill(bill) {
      // 根据单据类型和状态跳转到相应处理页面
      const routes = {
        'EXPENSE': '/financialSharing/expenseReport/process',
        'LOAN': '/financialSharing/loan/process',
        'PREPAYMENT': '/financialSharing/prepayment/process',
        'PROVISION': '/financialSharing/provision/process'
      }
      const route = routes[bill.billType]
      if (route) {
        this.$router.push({
          path: route,
          query: { id: bill.billId, action: 'process' }
        })
      }
    },

    viewBillDetail(bill) {
      // 跳转到单据详情页面
      const routes = {
        'EXPENSE': '/financialSharing/expenseReport/detail',
        'LOAN': '/financialSharing/loan/detail',
        'PREPAYMENT': '/financialSharing/prepayment/detail',
        'PROVISION': '/financialSharing/provision/detail'
      }
      const route = routes[bill.billType]
      if (route) {
        this.$router.push({
          path: route,
          query: { id: bill.billId }
        })
      }
    },

    handleAlert(alert) {
      // 处理异常提醒
      this.$alert(alert.alertMessage, '异常提醒', {
        confirmButtonText: '确定',
        type: alert.alertLevel === 'HIGH' ? 'error' : 'warning'
      })
    },

    batchApprove() {
      this.$router.push('/financialSharing/batch/approve')
    },

    batchPayment() {
      this.$router.push('/financialSharing/batch/payment')
    },

    exportReport() {
      this.$router.push('/financialSharing/reports/export')
    },

    systemSettings() {
      this.$router.push('/financialSharing/settings')
    },

    viewMonthlyDetail() {
      this.$router.push('/financialSharing/statistics/monthly')
    },

    viewAllPendingBills() {
      this.$router.push('/financialSharing/bills/pending')
    }
  }
}
</script>

<style scoped>
.finance-workspace {
  padding: 20px;
  background-color: #f5f5f5;
  min-height: calc(100vh - 84px);
}

.stats-cards {
  margin-bottom: 20px;
}

.stats-card {
  border: none;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.stats-content {
  display: flex;
  align-items: center;
}

.stats-icon {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 15px;
  font-size: 24px;
  color: white;
}

.stats-icon.review {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.stats-icon.payment {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
}

.stats-icon.audit {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
}

.stats-icon.archive {
  background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
}

.stats-info {
  flex: 1;
}

.stats-number {
  font-size: 28px;
  font-weight: bold;
  color: #303133;
  line-height: 1;
}

.stats-label {
  font-size: 14px;
  color: #909399;
  margin-top: 5px;
}

.main-content {
  margin-top: 20px;
}

.monthly-stats,
.pending-bills,
.alerts,
.efficiency,
.quick-actions,
.work-calendar {
  margin-bottom: 20px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.stat-item {
  text-align: center;
  padding: 20px 0;
}

.stat-value {
  font-size: 24px;
  font-weight: bold;
  color: #303133;
  margin-bottom: 5px;
}

.stat-label {
  font-size: 14px;
  color: #909399;
}

.amount {
  font-weight: bold;
  color: #E6A23C;
}

.empty-alerts {
  text-align: center;
  padding: 40px 0;
  color: #909399;
}

.empty-alerts i {
  font-size: 48px;
  margin-bottom: 10px;
  color: #67C23A;
}

.alert-items {
  max-height: 300px;
  overflow-y: auto;
}

.alert-item {
  display: flex;
  align-items: center;
  padding: 15px;
  border-left: 4px solid #E6A23C;
  margin-bottom: 10px;
  background-color: #fdf6ec;
  cursor: pointer;
  transition: all 0.3s;
}

.alert-item:hover {
  background-color: #faecd8;
}

.alert-item.alert-high {
  border-left-color: #F56C6C;
  background-color: #fef0f0;
}

.alert-item.alert-high:hover {
  background-color: #fde2e2;
}

.alert-icon {
  margin-right: 15px;
  font-size: 20px;
  color: #E6A23C;
}

.alert-high .alert-icon {
  color: #F56C6C;
}

.alert-content {
  flex: 1;
}

.alert-message {
  font-size: 14px;
  color: #303133;
  margin-bottom: 5px;
}

.alert-time {
  font-size: 12px;
  color: #909399;
}

.efficiency-chart {
  padding: 20px 0;
}

.efficiency-item {
  margin-bottom: 20px;
}

.efficiency-item:last-child {
  margin-bottom: 0;
}

.efficiency-label {
  font-size: 14px;
  color: #606266;
  margin-bottom: 5px;
}

.efficiency-value {
  font-size: 18px;
  font-weight: bold;
  color: #303133;
  margin-bottom: 8px;
}

.action-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 15px;
}

.action-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20px;
  border: 1px solid #EBEEF5;
  border-radius: 4px;
  cursor: pointer;
  transition: all 0.3s;
}

.action-item:hover {
  border-color: #409EFF;
  background-color: #f0f9ff;
}

.action-icon {
  font-size: 32px;
  color: #409EFF;
  margin-bottom: 10px;
}

.action-name {
  font-size: 14px;
  color: #303133;
}

.calendar-cell {
  height: 60px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

.date-number {
  font-size: 14px;
  color: #303133;
}

.date-tasks {
  display: flex;
  align-items: center;
  margin-top: 5px;
}

.task-dot {
  width: 6px;
  height: 6px;
  border-radius: 50%;
  margin-right: 2px;
}

.task-dot.task-review {
  background-color: #409EFF;
}

.task-dot.task-payment {
  background-color: #67C23A;
}

.task-dot.task-audit {
  background-color: #E6A23C;
}

.task-more {
  font-size: 10px;
  color: #909399;
}
</style>
