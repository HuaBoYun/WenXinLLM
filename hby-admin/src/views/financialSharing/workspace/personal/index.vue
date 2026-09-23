<template>
  <div class="personal-workspace">
    <!-- 统计卡片 -->
    <el-row :gutter="20" class="stats-cards">
      <el-col :span="6">
        <el-card class="stats-card">
          <div class="stats-content">
            <div class="stats-icon pending">
              <i class="el-icon-document"></i>
            </div>
            <div class="stats-info">
              <div class="stats-number">{{ todoStats.pendingExpense || 0 }}</div>
              <div class="stats-label">待提交报销</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stats-card">
          <div class="stats-content">
            <div class="stats-icon loan">
              <i class="el-icon-money"></i>
            </div>
            <div class="stats-info">
              <div class="stats-number">{{ todoStats.pendingLoan || 0 }}</div>
              <div class="stats-label">待处理借款</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stats-card">
          <div class="stats-content">
            <div class="stats-icon approval">
              <i class="el-icon-check"></i>
            </div>
            <div class="stats-info">
              <div class="stats-number">{{ todoStats.pendingApproval || 0 }}</div>
              <div class="stats-label">待我审批</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stats-card">
          <div class="stats-content">
            <div class="stats-icon reimbursement">
              <i class="el-icon-wallet"></i>
            </div>
            <div class="stats-info">
              <div class="stats-number">{{ todoStats.pendingReimbursement || 0 }}</div>
              <div class="stats-label">待报销金额</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="main-content">
      <!-- 左侧内容 -->
      <el-col :span="16">
        <!-- 本月费用统计 -->
        <el-card class="monthly-stats" title="本月费用统计">
          <div slot="header" class="card-header">
            <span>本月费用统计</span>
            <el-button style="float: right; padding: 3px 0" type="text" @click="viewMonthlyDetail">查看详情</el-button>
          </div>
          <el-row :gutter="20">
            <el-col :span="6">
              <div class="stat-item">
                <div class="stat-value">¥{{ formatAmount(monthlyStats.totalExpense) }}</div>
                <div class="stat-label">总费用</div>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="stat-item">
                <div class="stat-value">¥{{ formatAmount(monthlyStats.totalLoan) }}</div>
                <div class="stat-label">总借款</div>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="stat-item">
                <div class="stat-value">¥{{ formatAmount(monthlyStats.totalReimbursement) }}</div>
                <div class="stat-label">已报销</div>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="stat-item">
                <div class="stat-value">¥{{ formatAmount(monthlyStats.remainingBudget) }}</div>
                <div class="stat-label">剩余预算</div>
              </div>
            </el-col>
          </el-row>
        </el-card>

        <!-- 最近单据 -->
        <el-card class="recent-bills" title="最近单据">
          <div slot="header" class="card-header">
            <span>最近单据</span>
            <el-button style="float: right; padding: 3px 0" type="text" @click="viewAllBills">查看全部</el-button>
          </div>
          <el-table :data="recentBills" style="width: 100%" :show-header="false">
            <el-table-column prop="billTypeName" label="类型" width="100">
              <template slot-scope="scope">
                <el-tag :type="getBillTypeColor(scope.row.billType)" size="small">
                  {{ scope.row.billTypeName }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="billId" label="单据号" width="150" />
            <el-table-column prop="amount" label="金额" width="120">
              <template slot-scope="scope">
                <span class="amount">¥{{ formatAmount(scope.row.amount) }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="statusName" label="状态" width="100">
              <template slot-scope="scope">
                <el-tag :type="getStatusColor(scope.row.status)" size="small">
                  {{ scope.row.statusName }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="createTime" label="创建时间" width="160">
              <template slot-scope="scope">
                {{ formatDate(scope.row.createTime) }}
              </template>
            </el-table-column>
            <el-table-column label="操作" width="120">
              <template slot-scope="scope">
                <el-button size="mini" type="text" @click="viewBillDetail(scope.row)">查看</el-button>
                <el-button size="mini" type="text" @click="editBill(scope.row)">编辑</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>

      <!-- 右侧内容 -->
      <el-col :span="8">
        <!-- 快捷操作 -->
        <el-card class="quick-actions" title="快捷操作">
          <div class="action-grid">
            <div 
              v-for="action in quickActions" 
              :key="action.actionId"
              class="action-item"
              @click="handleQuickAction(action)"
            >
              <div class="action-icon">
                <i :class="action.actionIcon"></i>
              </div>
              <div class="action-name">{{ action.actionName }}</div>
            </div>
          </div>
        </el-card>

        <!-- 待办事项 -->
        <el-card class="todo-list" title="待办事项">
          <div slot="header" class="card-header">
            <span>待办事项</span>
            <el-button style="float: right; padding: 3px 0" type="text" @click="viewAllTodos">查看全部</el-button>
          </div>
          <div v-if="todoList.length === 0" class="empty-todo">
            <i class="el-icon-check"></i>
            <p>暂无待办事项</p>
          </div>
          <div v-else class="todo-items">
            <div 
              v-for="todo in todoList" 
              :key="todo.todoId"
              class="todo-item"
              @click="handleTodo(todo)"
            >
              <div class="todo-content">
                <div class="todo-title">{{ todo.title }}</div>
                <div class="todo-desc">{{ todo.description }}</div>
                <div class="todo-meta">
                  <span class="todo-time">{{ formatDate(todo.createTime) }}</span>
                  <el-tag :type="getPriorityColor(todo.priority)" size="mini">
                    {{ todo.priorityName }}
                  </el-tag>
                </div>
              </div>
            </div>
          </div>
        </el-card>

        <!-- 个人信息 -->
        <el-card class="user-info" title="个人信息">
          <div class="user-profile">
            <div class="user-avatar">
              <el-avatar :size="60" :src="userInfo.avatar" icon="el-icon-user-solid"></el-avatar>
            </div>
            <div class="user-details">
              <div class="user-name">{{ userInfo.name }}</div>
              <div class="user-dept">{{ userInfo.departmentName }}</div>
              <div class="user-position">{{ userInfo.position }}</div>
            </div>
          </div>
          <div class="user-stats">
            <div class="user-stat-item">
              <span class="label">本月提交：</span>
              <span class="value">{{ userStats.monthlySubmit }}笔</span>
            </div>
            <div class="user-stat-item">
              <span class="label">累计报销：</span>
              <span class="value">¥{{ formatAmount(userStats.totalReimbursement) }}</span>
            </div>
            <div class="user-stat-item">
              <span class="label">信用评级：</span>
              <el-rate v-model="userStats.creditRating" disabled show-score text-color="#ff9900" />
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { workspaceApi } from '@/api/financialSharing/coreBusiness'

export default {
  name: 'PersonalWorkspace',
  data() {
    return {
      loading: false,
      todoStats: {},
      monthlyStats: {},
      recentBills: [],
      quickActions: [],
      todoList: [],
      userInfo: {},
      userStats: {}
    }
  },
  mounted() {
    this.loadWorkspaceData()
    this.loadTodoList()
  },
  methods: {
    async loadWorkspaceData() {
      this.loading = true
      try {
        const userId = this.$store.getters.userId || 'USER001'
        const response = await workspaceApi.getPersonalWorkspace(userId)
        if (response.code === 1) {
          const data = response.data
          this.todoStats = data.todoStats || {}
          this.monthlyStats = data.monthlyStats || {}
          this.recentBills = data.recentBills || []
          this.quickActions = data.quickActions || []
          this.userInfo = data.userInfo || {}
          this.userStats = data.userStats || {}
        } else {
          this.$message.error(response.msg || '加载工作台数据失败')
        }
      } catch (error) {
        this.$message.error('加载工作台数据失败：' + error.message)
      } finally {
        this.loading = false
      }
    },

    async loadTodoList() {
      try {
        const userId = this.$store.getters.userId || 'USER001'
        const params = {
          page: 0,
          size: 5,
          userId
        }
        const response = await workspaceApi.getTodoList(params)
        if (response.code === 1) {
          this.todoList = response.data.tlist || []
        }
      } catch (error) {
        console.error('加载待办事项失败：', error)
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
        year: 'numeric',
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
        'PENDING': 'warning',
        'APPROVED': 'success',
        'REJECTED': 'danger',
        'PAID': 'info'
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

    handleQuickAction(action) {
      if (action.actionUrl) {
        this.$router.push(action.actionUrl)
      } else {
        // 根据actionId执行相应操作
        switch (action.actionId) {
          case 'NEW_EXPENSE':
            this.$router.push('/financialSharing/expenseReport/new')
            break
          case 'NEW_LOAN':
            this.$router.push('/financialSharing/loan/new')
            break
          default:
            this.$message.info('请使用左侧菜单导航至对应功能页面')
        }
      }
    },

    handleTodo(todo) {
      // 跳转到待办事项详情或处理页面
      this.$router.push({
        path: '/financialSharing/todo/detail',
        query: { todoId: todo.todoId }
      })
    },

    viewBillDetail(bill) {
      // 根据单据类型跳转到相应详情页面
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

    editBill(bill) {
      // 根据单据类型跳转到相应编辑页面
      const routes = {
        'EXPENSE': '/financialSharing/expenseReport/edit',
        'LOAN': '/financialSharing/loan/edit',
        'PREPAYMENT': '/financialSharing/prepayment/edit',
        'PROVISION': '/financialSharing/provision/edit'
      }
      const route = routes[bill.billType]
      if (route) {
        this.$router.push({
          path: route,
          query: { id: bill.billId }
        })
      }
    },

    viewMonthlyDetail() {
      this.$router.push('/financialSharing/statistics/monthly')
    },

    viewAllBills() {
      this.$router.push('/financialSharing/bills')
    },

    viewAllTodos() {
      this.$router.push('/financialSharing/todos')
    }
  }
}
</script>

<style scoped>
.personal-workspace {
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

.stats-icon.pending {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.stats-icon.loan {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
}

.stats-icon.approval {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
}

.stats-icon.reimbursement {
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
.recent-bills,
.quick-actions,
.todo-list,
.user-info {
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

.empty-todo {
  text-align: center;
  padding: 40px 0;
  color: #909399;
}

.empty-todo i {
  font-size: 48px;
  margin-bottom: 10px;
}

.todo-items {
  max-height: 300px;
  overflow-y: auto;
}

.todo-item {
  padding: 15px;
  border-bottom: 1px solid #EBEEF5;
  cursor: pointer;
  transition: background-color 0.3s;
}

.todo-item:hover {
  background-color: #f5f7fa;
}

.todo-item:last-child {
  border-bottom: none;
}

.todo-title {
  font-size: 14px;
  font-weight: bold;
  color: #303133;
  margin-bottom: 5px;
}

.todo-desc {
  font-size: 12px;
  color: #606266;
  margin-bottom: 8px;
}

.todo-meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.todo-time {
  font-size: 12px;
  color: #909399;
}

.user-profile {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
}

.user-avatar {
  margin-right: 15px;
}

.user-details {
  flex: 1;
}

.user-name {
  font-size: 16px;
  font-weight: bold;
  color: #303133;
  margin-bottom: 5px;
}

.user-dept,
.user-position {
  font-size: 14px;
  color: #606266;
  margin-bottom: 3px;
}

.user-stats {
  border-top: 1px solid #EBEEF5;
  padding-top: 15px;
}

.user-stat-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.user-stat-item:last-child {
  margin-bottom: 0;
}

.user-stat-item .label {
  font-size: 14px;
  color: #606266;
}

.user-stat-item .value {
  font-size: 14px;
  font-weight: bold;
  color: #303133;
}
</style>
