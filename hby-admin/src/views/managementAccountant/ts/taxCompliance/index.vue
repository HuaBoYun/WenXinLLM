<template>
  <div class="tax-compliance-container">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-left">
        <h2 class="page-title">
          <i class="el-icon-s-check"></i>
          税务合规检查
        </h2>
        <p class="page-description">全面的税务合规检查管理，确保企业税务合规性</p>
      </div>
      <div class="header-right">
        <el-button type="primary" icon="el-icon-plus" @click="showCreateDialog">
          新建检查
        </el-button>
        <el-button icon="el-icon-refresh" @click="refreshData">
          刷新
        </el-button>
      </div>
    </div>

    <!-- 统计卡片 -->
    <div class="stats-cards">
      <el-row :gutter="20">
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon total">
              <i class="el-icon-s-data"></i>
            </div>
            <div class="stat-content">
              <div class="stat-number">{{ overviewStats.totalCount || 0 }}</div>
              <div class="stat-label">总检查数</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon in-progress">
              <i class="el-icon-loading"></i>
            </div>
            <div class="stat-content">
              <div class="stat-number">{{ overviewStats.inProgressCount || 0 }}</div>
              <div class="stat-label">进行中</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon completed">
              <i class="el-icon-circle-check"></i>
            </div>
            <div class="stat-content">
              <div class="stat-number">{{ overviewStats.completedCount || 0 }}</div>
              <div class="stat-label">已完成</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon high-risk">
              <i class="el-icon-warning"></i>
            </div>
            <div class="stat-content">
              <div class="stat-number">{{ overviewStats.highRiskCount || 0 }}</div>
              <div class="stat-label">高风险</div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 功能标签页 -->
    <div class="main-content">
      <el-tabs v-model="activeTab" @tab-click="handleTabClick">
        <el-tab-pane label="检查列表" name="list">
          <TaxComplianceList ref="complianceList" @view-detail="handleViewDetail" />
        </el-tab-pane>
        <el-tab-pane label="数据分析" name="dashboard">
          <TaxComplianceDashboard ref="complianceDashboard" />
        </el-tab-pane>
        <el-tab-pane label="待处理事项" name="pending">
          <div class="pending-items">
            <el-row :gutter="20">
              <el-col :span="12">
                <el-card class="pending-card">
                  <div slot="header" class="card-header">
                    <span>即将到期检查</span>
                    <el-badge :value="expiringSoonList.length" class="item">
                      <el-button type="text" @click="viewExpiringSoon">查看全部</el-button>
                    </el-badge>
                  </div>
                  <div class="pending-list">
                    <div v-for="item in expiringSoonList.slice(0, 5)" :key="item.complianceId" class="pending-item">
                      <div class="item-info">
                        <div class="item-name">{{ item.complianceName }}</div>
                        <div class="item-meta">
                          <span class="item-code">{{ item.complianceCode }}</span>
                          <span class="item-deadline">{{ formatDate(item.plannedCheckTime) }}</span>
                        </div>
                      </div>
                      <el-button type="text" size="mini" @click="handleViewDetail(item)">
                        查看
                      </el-button>
                    </div>
                  </div>
                </el-card>
              </el-col>
              <el-col :span="12">
                <el-card class="pending-card">
                  <div slot="header" class="card-header">
                    <span>高风险检查</span>
                    <el-badge :value="highRiskList.length" class="item">
                      <el-button type="text" @click="viewHighRisk">查看全部</el-button>
                    </el-badge>
                  </div>
                  <div class="pending-list">
                    <div v-for="item in highRiskList.slice(0, 5)" :key="item.complianceId" class="pending-item">
                      <div class="item-info">
                        <div class="item-name">{{ item.complianceName }}</div>
                        <div class="item-meta">
                          <span class="item-code">{{ item.complianceCode }}</span>
                          <el-tag :type="getRiskLevelColor(item.riskLevel)" size="mini">
                            {{ formatRiskLevel(item.riskLevel) }}
                          </el-tag>
                        </div>
                      </div>
                      <el-button type="text" size="mini" @click="handleViewDetail(item)">
                        查看
                      </el-button>
                    </div>
                  </div>
                </el-card>
              </el-col>
            </el-row>
            <el-row :gutter="20" style="margin-top: 20px;">
              <el-col :span="12">
                <el-card class="pending-card">
                  <div slot="header" class="card-header">
                    <span>需要整改</span>
                    <el-badge :value="needRectificationList.length" class="item">
                      <el-button type="text" @click="viewNeedRectification">查看全部</el-button>
                    </el-badge>
                  </div>
                  <div class="pending-list">
                    <div v-for="item in needRectificationList.slice(0, 5)" :key="item.complianceId" class="pending-item">
                      <div class="item-info">
                        <div class="item-name">{{ item.complianceName }}</div>
                        <div class="item-meta">
                          <span class="item-code">{{ item.complianceCode }}</span>
                          <span class="item-issues">{{ item.issueCount }}个问题</span>
                        </div>
                      </div>
                      <el-button type="text" size="mini" @click="handleViewDetail(item)">
                        查看
                      </el-button>
                    </div>
                  </div>
                </el-card>
              </el-col>
              <el-col :span="12">
                <el-card class="pending-card">
                  <div slot="header" class="card-header">
                    <span>逾期检查</span>
                    <el-badge :value="overdueList.length" class="item">
                      <el-button type="text" @click="viewOverdue">查看全部</el-button>
                    </el-badge>
                  </div>
                  <div class="pending-list">
                    <div v-for="item in overdueList.slice(0, 5)" :key="item.complianceId" class="pending-item">
                      <div class="item-info">
                        <div class="item-name">{{ item.complianceName }}</div>
                        <div class="item-meta">
                          <span class="item-code">{{ item.complianceCode }}</span>
                          <span class="item-overdue">逾期 {{ getDaysOverdue(item.plannedCheckTime) }} 天</span>
                        </div>
                      </div>
                      <el-button type="text" size="mini" @click="handleViewDetail(item)">
                        查看
                      </el-button>
                    </div>
                  </div>
                </el-card>
              </el-col>
            </el-row>
          </div>
        </el-tab-pane>
      </el-tabs>
    </div>

    <!-- 创建检查对话框 -->
    <el-dialog
      title="新建合规检查"
      :visible.sync="createDialogVisible"
      width="800px"
      :close-on-click-modal="false"
    >
      <el-form
        ref="createForm"
        :model="createForm"
        :rules="createRules"
        label-width="120px"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="检查名称" prop="complianceName">
              <el-input v-model="createForm.complianceName" placeholder="请输入检查名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="检查类型" prop="complianceType">
              <el-select v-model="createForm.complianceType" placeholder="请选择检查类型" style="width: 100%">
                <el-option label="税务合规" value="TAX_COMPLIANCE" />
                <el-option label="财务合规" value="FINANCIAL_COMPLIANCE" />
                <el-option label="监管合规" value="REGULATORY_COMPLIANCE" />
                <el-option label="内部合规" value="INTERNAL_COMPLIANCE" />
                <el-option label="外部合规" value="EXTERNAL_COMPLIANCE" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="检查范围" prop="checkScope">
              <el-input v-model="createForm.checkScope" placeholder="请输入检查范围" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="检查对象" prop="checkObject">
              <el-input v-model="createForm.checkObject" placeholder="请输入检查对象" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="优先级" prop="priority">
              <el-select v-model="createForm.priority" placeholder="请选择优先级" style="width: 100%">
                <el-option label="低" value="LOW" />
                <el-option label="普通" value="NORMAL" />
                <el-option label="高" value="HIGH" />
                <el-option label="紧急" value="URGENT" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="检查人员" prop="checker">
              <el-input v-model="createForm.checker" placeholder="请输入检查人员" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="开始时间" prop="startTime">
              <el-date-picker
                v-model="createForm.startTime"
                type="datetime"
                placeholder="选择开始时间"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="结束时间" prop="endTime">
              <el-date-picker
                v-model="createForm.endTime"
                type="datetime"
                placeholder="选择结束时间"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="检查描述">
          <el-input
            v-model="createForm.remarks"
            type="textarea"
            :rows="3"
            placeholder="请输入检查描述"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="createDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleCreate" :loading="createLoading">确定</el-button>
      </div>
    </el-dialog>

    <!-- 详情对话框 -->
    <el-dialog
      title="合规检查详情"
      :visible.sync="detailDialogVisible"
      width="1200px"
      :close-on-click-modal="false"
    >
      <TaxComplianceDetail
        v-if="detailDialogVisible && selectedCompliance"
        :compliance-id="selectedCompliance.complianceId"
        @close="detailDialogVisible = false"
        @refresh="refreshData"
      />
    </el-dialog>
  </div>
</template>

<script>
import TaxComplianceList from './TaxComplianceList'
import TaxComplianceDashboard from './TaxComplianceDashboard'
import TaxComplianceDetail from './TaxComplianceDetail'
import {
  getComplianceOverview,
  getExpiringSoonCompliances,
  getHighRiskCompliances,
  getNeedRectificationCompliances,
  getOverdueCompliances,
  createCompliance,
  formatRiskLevel,
  getRiskLevelColor
} from '@/api/managementAccountant/ts/taxCompliance'

export default {
  name: 'TaxComplianceIndex',
  components: {
    TaxComplianceList,
    TaxComplianceDashboard,
    TaxComplianceDetail
  },
  data() {
    return {
      activeTab: 'list',
      overviewStats: {},
      expiringSoonList: [],
      highRiskList: [],
      needRectificationList: [],
      overdueList: [],
      createDialogVisible: false,
      detailDialogVisible: false,
      selectedCompliance: null,
      createLoading: false,
      createForm: {
        complianceName: '',
        complianceType: '',
        checkScope: '',
        checkObject: '',
        priority: 'NORMAL',
        checker: '',
        startTime: '',
        endTime: '',
        remarks: ''
      },
      createRules: {
        complianceName: [
          { required: true, message: '请输入检查名称', trigger: 'blur' }
        ],
        complianceType: [
          { required: true, message: '请选择检查类型', trigger: 'change' }
        ],
        checkScope: [
          { required: true, message: '请输入检查范围', trigger: 'blur' }
        ],
        checkObject: [
          { required: true, message: '请输入检查对象', trigger: 'blur' }
        ]
      }
    }
  },
  created() {
    this.loadData()
  },
  methods: {
    async loadData() {
      try {
        await Promise.all([
          this.loadOverviewStats(),
          this.loadPendingItems()
        ])
      } catch (error) {
        console.error('加载数据失败:', error)
        this.$message.error('加载数据失败')
      }
    },

    async loadOverviewStats() {
      try {
        const response = await getComplianceOverview()
        if (response.success) {
          this.overviewStats = response.data || {}
        }
      } catch (error) {
        console.error('加载概览统计失败:', error)
      }
    },

    async loadPendingItems() {
      try {
        const [expiringSoon, highRisk, needRectification, overdue] = await Promise.all([
          getExpiringSoonCompliances(7),
          getHighRiskCompliances(),
          getNeedRectificationCompliances(),
          getOverdueCompliances()
        ])

        this.expiringSoonList = expiringSoon.success ? expiringSoon.data : []
        this.highRiskList = highRisk.success ? highRisk.data : []
        this.needRectificationList = needRectification.success ? needRectification.data : []
        this.overdueList = overdue.success ? overdue.data : []
      } catch (error) {
        console.error('加载待处理事项失败:', error)
      }
    },

    handleTabClick(tab) {
      this.activeTab = tab.name
      if (tab.name === 'dashboard' && this.$refs.complianceDashboard) {
        this.$refs.complianceDashboard.refreshCharts()
      }
    },

    showCreateDialog() {
      this.createDialogVisible = true
      this.resetCreateForm()
    },

    resetCreateForm() {
      this.createForm = {
        complianceName: '',
        complianceType: '',
        checkScope: '',
        checkObject: '',
        priority: 'NORMAL',
        checker: '',
        startTime: '',
        endTime: '',
        remarks: ''
      }
      if (this.$refs.createForm) {
        this.$refs.createForm.clearValidate()
      }
    },

    async handleCreate() {
      try {
        const valid = await this.$refs.createForm.validate()
        if (!valid) return

        this.createLoading = true
        const response = await createCompliance(this.createForm)
        
        if (response.success) {
          this.$message.success('创建合规检查成功')
          this.createDialogVisible = false
          this.refreshData()
        } else {
          this.$message.error(response.message || '创建合规检查失败')
        }
      } catch (error) {
        console.error('创建合规检查失败:', error)
        this.$message.error('创建合规检查失败')
      } finally {
        this.createLoading = false
      }
    },

    handleViewDetail(compliance) {
      this.selectedCompliance = compliance
      this.detailDialogVisible = true
    },

    refreshData() {
      this.loadData()
      if (this.$refs.complianceList) {
        this.$refs.complianceList.refreshData()
      }
      if (this.$refs.complianceDashboard) {
        this.$refs.complianceDashboard.refreshCharts()
      }
    },

    viewExpiringSoon() {
      this.activeTab = 'list'
      this.$nextTick(() => {
        if (this.$refs.complianceList) {
          this.$refs.complianceList.filterByExpiringSoon()
        }
      })
    },

    viewHighRisk() {
      this.activeTab = 'list'
      this.$nextTick(() => {
        if (this.$refs.complianceList) {
          this.$refs.complianceList.filterByRiskLevel('HIGH')
        }
      })
    },

    viewNeedRectification() {
      this.activeTab = 'list'
      this.$nextTick(() => {
        if (this.$refs.complianceList) {
          this.$refs.complianceList.filterByNeedRectification()
        }
      })
    },

    viewOverdue() {
      this.activeTab = 'list'
      this.$nextTick(() => {
        if (this.$refs.complianceList) {
          this.$refs.complianceList.filterByOverdue()
        }
      })
    },

    formatDate(date) {
      if (!date) return '-'
      return this.$moment(date).format('YYYY-MM-DD HH:mm')
    },

    getDaysOverdue(plannedTime) {
      if (!plannedTime) return 0
      const now = new Date()
      const planned = new Date(plannedTime)
      const diffTime = now - planned
      const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24))
      return Math.max(0, diffDays)
    },

    formatRiskLevel,
    getRiskLevelColor
  }
}
</script>

<style lang="scss" scoped>
.tax-compliance-container {
  padding: 20px;
  background-color: #f5f7fa;
  min-height: calc(100vh - 84px);

  .page-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;
    padding: 20px;
    background: white;
    border-radius: 8px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);

    .header-left {
      .page-title {
        margin: 0 0 8px 0;
        font-size: 24px;
        font-weight: 600;
        color: #303133;
        display: flex;
        align-items: center;

        i {
          margin-right: 8px;
          color: #409eff;
        }
      }

      .page-description {
        margin: 0;
        color: #909399;
        font-size: 14px;
      }
    }

    .header-right {
      .el-button {
        margin-left: 10px;
      }
    }
  }

  .stats-cards {
    margin-bottom: 20px;

    .stat-card {
      background: white;
      border-radius: 8px;
      padding: 20px;
      box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
      display: flex;
      align-items: center;
      transition: transform 0.2s;

      &:hover {
        transform: translateY(-2px);
      }

      .stat-icon {
        width: 60px;
        height: 60px;
        border-radius: 50%;
        display: flex;
        align-items: center;
        justify-content: center;
        margin-right: 16px;

        i {
          font-size: 24px;
          color: white;
        }

        &.total {
          background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        }

        &.in-progress {
          background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
        }

        &.completed {
          background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
        }

        &.high-risk {
          background: linear-gradient(135deg, #fa709a 0%, #fee140 100%);
        }
      }

      .stat-content {
        flex: 1;

        .stat-number {
          font-size: 28px;
          font-weight: 700;
          color: #303133;
          line-height: 1;
          margin-bottom: 4px;
        }

        .stat-label {
          font-size: 14px;
          color: #909399;
        }
      }
    }
  }

  .main-content {
    background: white;
    border-radius: 8px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
    overflow: hidden;

    ::v-deep .el-tabs__header {
      margin: 0;
      padding: 0 20px;
      background: #fafbfc;
      border-bottom: 1px solid #e4e7ed;
    }

    ::v-deep .el-tab-pane {
      padding: 20px;
    }
  }

  .pending-items {
    .pending-card {
      .card-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        font-weight: 600;
      }

      .pending-list {
        .pending-item {
          display: flex;
          justify-content: space-between;
          align-items: center;
          padding: 12px 0;
          border-bottom: 1px solid #f0f0f0;

          &:last-child {
            border-bottom: none;
          }

          .item-info {
            flex: 1;

            .item-name {
              font-size: 14px;
              font-weight: 500;
              color: #303133;
              margin-bottom: 4px;
            }

            .item-meta {
              font-size: 12px;
              color: #909399;
              display: flex;
              align-items: center;
              gap: 8px;

              .item-code {
                background: #f0f2f5;
                padding: 2px 6px;
                border-radius: 4px;
              }

              .item-deadline,
              .item-issues,
              .item-overdue {
                color: #f56c6c;
              }
            }
          }
        }
      }
    }
  }

  .dialog-footer {
    text-align: right;

    .el-button {
      margin-left: 10px;
    }
  }
}

// 响应式设计
@media (max-width: 768px) {
  .tax-compliance-container {
    padding: 10px;

    .page-header {
      flex-direction: column;
      align-items: flex-start;
      gap: 16px;

      .header-right {
        align-self: stretch;
        display: flex;
        justify-content: flex-end;
      }
    }

    .stats-cards {
      .el-col {
        margin-bottom: 16px;
      }
    }
  }
}
</style>
