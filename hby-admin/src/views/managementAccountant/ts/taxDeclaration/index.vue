<template>
  <div class="tax-declaration-container">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-left">
        <h2 class="page-title">
          <i class="el-icon-document"></i>
          税务申报管理
        </h2>
        <p class="page-description">
          智能化税务申报管理系统，支持多税种申报、自动计算、流程跟踪和合规检查
        </p>
      </div>
      <div class="header-right">
        <el-button-group>
          <el-button 
            type="primary" 
            icon="el-icon-plus" 
            @click="showCreateDialog = true"
          >
            新建申报
          </el-button>
          <el-button 
            type="success" 
            icon="el-icon-magic-stick" 
            @click="showPlanDialog = true"
          >
            生成计划
          </el-button>
          <el-button 
            type="info" 
            icon="el-icon-data-analysis" 
            @click="activeTab = 'dashboard'"
          >
            数据分析
          </el-button>
        </el-button-group>
      </div>
    </div>

    <!-- 快捷统计卡片 -->
    <div class="stats-cards">
      <el-row :gutter="20">
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon draft">
              <i class="el-icon-edit-outline"></i>
            </div>
            <div class="stat-content">
              <div class="stat-number">{{ overview.draftCount || 0 }}</div>
              <div class="stat-label">草稿申报</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon pending">
              <i class="el-icon-clock"></i>
            </div>
            <div class="stat-content">
              <div class="stat-number">{{ overview.pendingCount || 0 }}</div>
              <div class="stat-label">待处理</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon overdue">
              <i class="el-icon-warning"></i>
            </div>
            <div class="stat-content">
              <div class="stat-number">{{ overview.overdueCount || 0 }}</div>
              <div class="stat-label">逾期申报</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon completed">
              <i class="el-icon-check"></i>
            </div>
            <div class="stat-content">
              <div class="stat-number">{{ overview.completedCount || 0 }}</div>
              <div class="stat-label">已完成</div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 主要内容区域 -->
    <div class="main-content">
      <el-tabs v-model="activeTab" type="card" @tab-click="handleTabClick">
        <el-tab-pane label="申报列表" name="list">
          <TaxDeclarationList 
            ref="declarationList"
            @view-detail="handleViewDetail"
            @edit-declaration="handleEditDeclaration"
            @refresh-overview="loadOverview"
          />
        </el-tab-pane>
        
        <el-tab-pane label="数据分析" name="dashboard">
          <TaxDeclarationDashboard 
            ref="declarationDashboard"
          />
        </el-tab-pane>
        
        <el-tab-pane label="待处理事项" name="pending">
          <div class="pending-items">
            <el-row :gutter="20">
              <el-col :span="12">
                <el-card header="待申报列表" class="pending-card">
                  <div v-if="pendingDeclarations.length === 0" class="empty-state">
                    <i class="el-icon-document"></i>
                    <p>暂无待申报事项</p>
                  </div>
                  <div v-else>
                    <div 
                      v-for="item in pendingDeclarations" 
                      :key="item.declarationId"
                      class="pending-item"
                      @click="handleViewDetail(item)"
                    >
                      <div class="item-header">
                        <span class="item-title">{{ item.declarationName }}</span>
                        <el-tag 
                          :type="isDeclarationOverdue(item.deadline, item.declarationStatus) ? 'danger' : 'warning'"
                          size="mini"
                        >
                          {{ isDeclarationOverdue(item.deadline, item.declarationStatus) ? '已逾期' : '待处理' }}
                        </el-tag>
                      </div>
                      <div class="item-content">
                        <p>税种：{{ getTaxTypeLabel(item.taxType) }}</p>
                        <p>期间：{{ item.declarationPeriod }}</p>
                        <p>截止：{{ formatDate(item.deadline) }}</p>
                      </div>
                    </div>
                  </div>
                </el-card>
              </el-col>
              
              <el-col :span="12">
                <el-card header="待审核列表" class="pending-card">
                  <div v-if="pendingReviewDeclarations.length === 0" class="empty-state">
                    <i class="el-icon-view"></i>
                    <p>暂无待审核事项</p>
                  </div>
                  <div v-else>
                    <div 
                      v-for="item in pendingReviewDeclarations" 
                      :key="item.declarationId"
                      class="pending-item"
                      @click="handleViewDetail(item)"
                    >
                      <div class="item-header">
                        <span class="item-title">{{ item.declarationName }}</span>
                        <el-tag type="info" size="mini">待审核</el-tag>
                      </div>
                      <div class="item-content">
                        <p>税种：{{ getTaxTypeLabel(item.taxType) }}</p>
                        <p>提交时间：{{ formatDate(item.submitTime) }}</p>
                        <p>申报金额：¥{{ formatTaxAmount(item.taxAmount) }}</p>
                      </div>
                    </div>
                  </div>
                </el-card>
              </el-col>
            </el-row>
          </div>
        </el-tab-pane>
      </el-tabs>
    </div>

    <!-- 申报详情对话框 -->
    <el-dialog
      title="申报详情"
      :visible.sync="showDetailDialog"
      width="80%"
      :close-on-click-modal="false"
    >
      <TaxDeclarationDetail
        v-if="showDetailDialog && selectedDeclaration"
        :declaration-id="selectedDeclaration.declarationId"
        :tenant-id="selectedDeclaration.tenantId"
        @close="showDetailDialog = false"
        @refresh="handleRefresh"
      />
    </el-dialog>

    <!-- 创建申报对话框 -->
    <el-dialog
      title="新建申报"
      :visible.sync="showCreateDialog"
      width="60%"
      :close-on-click-modal="false"
    >
      <div class="create-form">
        <el-form 
          ref="createForm" 
          :model="createForm" 
          :rules="createRules" 
          label-width="120px"
        >
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="申报名称" prop="declarationName">
                <el-input v-model="createForm.declarationName" placeholder="请输入申报名称" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="税种" prop="taxType">
                <el-select v-model="createForm.taxType" placeholder="请选择税种" style="width: 100%">
                  <el-option
                    v-for="(label, value) in TAX_TYPE_LABELS"
                    :key="value"
                    :label="label"
                    :value="value"
                  />
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="申报类型" prop="declarationType">
                <el-select v-model="createForm.declarationType" placeholder="请选择申报类型" style="width: 100%">
                  <el-option
                    v-for="(label, value) in DECLARATION_TYPE_LABELS"
                    :key="value"
                    :label="label"
                    :value="value"
                  />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="申报期间" prop="declarationPeriod">
                <el-input v-model="createForm.declarationPeriod" placeholder="如：2024-01" />
              </el-form-item>
            </el-col>
          </el-row>
          
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="纳税人识别号" prop="taxpayerId">
                <el-input v-model="createForm.taxpayerId" placeholder="请输入纳税人识别号" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="纳税人名称" prop="taxpayerName">
                <el-input v-model="createForm.taxpayerName" placeholder="请输入纳税人名称" />
              </el-form-item>
            </el-col>
          </el-row>
          
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="申报截止日期" prop="deadline">
                <el-date-picker
                  v-model="createForm.deadline"
                  type="datetime"
                  placeholder="选择截止日期"
                  style="width: 100%"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="优先级" prop="priority">
                <el-select v-model="createForm.priority" placeholder="请选择优先级" style="width: 100%">
                  <el-option
                    v-for="(label, value) in PRIORITY_LABELS"
                    :key="value"
                    :label="label"
                    :value="value"
                  />
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          
          <el-form-item label="备注">
            <el-input
              v-model="createForm.remark"
              type="textarea"
              :rows="3"
              placeholder="请输入备注信息"
            />
          </el-form-item>
        </el-form>
      </div>
      
      <div slot="footer" class="dialog-footer">
        <el-button @click="showCreateDialog = false">取消</el-button>
        <el-button type="primary" @click="handleCreateDeclaration" :loading="createLoading">
          创建申报
        </el-button>
      </div>
    </el-dialog>

    <!-- 生成计划对话框 -->
    <el-dialog
      title="生成申报计划"
      :visible.sync="showPlanDialog"
      width="50%"
      :close-on-click-modal="false"
    >
      <div class="plan-form">
        <el-form 
          ref="planForm" 
          :model="planForm" 
          :rules="planRules" 
          label-width="120px"
        >
          <el-form-item label="税种" prop="taxType">
            <el-select v-model="planForm.taxType" placeholder="请选择税种" style="width: 100%">
              <el-option
                v-for="(label, value) in TAX_TYPE_LABELS"
                :key="value"
                :label="label"
                :value="value"
              />
            </el-select>
          </el-form-item>
          
          <el-form-item label="申报周期" prop="period">
            <el-select v-model="planForm.period" placeholder="请选择申报周期" style="width: 100%">
              <el-option label="月度申报" value="MONTHLY" />
              <el-option label="季度申报" value="QUARTERLY" />
              <el-option label="年度申报" value="YEARLY" />
            </el-select>
          </el-form-item>
          
          <el-form-item label="年度" prop="year">
            <el-date-picker
              v-model="planForm.year"
              type="year"
              placeholder="选择年度"
              style="width: 100%"
              value-format="yyyy"
            />
          </el-form-item>
        </el-form>
      </div>
      
      <div slot="footer" class="dialog-footer">
        <el-button @click="showPlanDialog = false">取消</el-button>
        <el-button type="primary" @click="handleGeneratePlan" :loading="planLoading">
          生成计划
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import TaxDeclarationList from './TaxDeclarationList'
import TaxDeclarationDetail from './TaxDeclarationDetail'
import TaxDeclarationDashboard from './TaxDeclarationDashboard'
import {
  createDeclaration,
  generateDeclarationPlan,
  getDeclarationOverview,
  getPendingDeclarations,
  getPendingReviewDeclarations,
  TAX_TYPE_LABELS,
  DECLARATION_TYPE_LABELS,
  PRIORITY_LABELS,
  getTaxTypeLabel,
  formatTaxAmount,
  isDeclarationOverdue
} from '@/api/managementAccountant/ts/taxDeclaration'

export default {
  name: 'TaxDeclarationIndex',
  components: {
    TaxDeclarationList,
    TaxDeclarationDetail,
    TaxDeclarationDashboard
  },
  data() {
    return {
      // 常量
      TAX_TYPE_LABELS,
      DECLARATION_TYPE_LABELS,
      PRIORITY_LABELS,
      
      // 页面状态
      activeTab: 'list',
      loading: false,
      
      // 概览数据
      overview: {},
      
      // 待处理数据
      pendingDeclarations: [],
      pendingReviewDeclarations: [],
      
      // 对话框状态
      showDetailDialog: false,
      showCreateDialog: false,
      showPlanDialog: false,
      selectedDeclaration: null,
      
      // 创建表单
      createForm: {
        tenantId: this.$store.getters.tenantId,
        declarationName: '',
        taxType: '',
        declarationType: '',
        declarationPeriod: '',
        taxpayerId: '',
        taxpayerName: '',
        deadline: null,
        priority: 'NORMAL',
        remark: ''
      },
      createLoading: false,
      createRules: {
        declarationName: [
          { required: true, message: '请输入申报名称', trigger: 'blur' }
        ],
        taxType: [
          { required: true, message: '请选择税种', trigger: 'change' }
        ],
        declarationType: [
          { required: true, message: '请选择申报类型', trigger: 'change' }
        ],
        declarationPeriod: [
          { required: true, message: '请输入申报期间', trigger: 'blur' }
        ],
        taxpayerId: [
          { required: true, message: '请输入纳税人识别号', trigger: 'blur' }
        ],
        taxpayerName: [
          { required: true, message: '请输入纳税人名称', trigger: 'blur' }
        ]
      },
      
      // 计划表单
      planForm: {
        tenantId: this.$store.getters.tenantId,
        taxType: '',
        period: '',
        year: new Date().getFullYear()
      },
      planLoading: false,
      planRules: {
        taxType: [
          { required: true, message: '请选择税种', trigger: 'change' }
        ],
        period: [
          { required: true, message: '请选择申报周期', trigger: 'change' }
        ],
        year: [
          { required: true, message: '请选择年度', trigger: 'change' }
        ]
      }
    }
  },
  created() {
    this.init()
  },
  methods: {
    // 初始化
    async init() {
      await this.loadOverview()
      await this.loadPendingItems()
    },
    
    // 加载概览数据
    async loadOverview() {
      try {
        const response = await getDeclarationOverview(this.$store.getters.tenantId)
        if (response.success) {
          this.overview = response.data || {}
        }
      } catch (error) {
        console.error('加载概览数据失败:', error)
      }
    },
    
    // 加载待处理事项
    async loadPendingItems() {
      try {
        const [pendingResponse, reviewResponse] = await Promise.all([
          getPendingDeclarations(this.$store.getters.tenantId, 10),
          getPendingReviewDeclarations(this.$store.getters.tenantId, 10)
        ])
        
        if (pendingResponse.success) {
          this.pendingDeclarations = pendingResponse.data || []
        }
        
        if (reviewResponse.success) {
          this.pendingReviewDeclarations = reviewResponse.data || []
        }
      } catch (error) {
        console.error('加载待处理事项失败:', error)
      }
    },
    
    // 标签页切换
    handleTabClick(tab) {
      if (tab.name === 'pending') {
        this.loadPendingItems()
      }
    },
    
    // 查看详情
    handleViewDetail(declaration) {
      this.selectedDeclaration = declaration
      this.showDetailDialog = true
    },
    
    // 编辑申报
    handleEditDeclaration(declaration) {
      this.selectedDeclaration = declaration
      this.showDetailDialog = true
    },
    
    // 刷新
    handleRefresh() {
      this.loadOverview()
      this.loadPendingItems()
      if (this.$refs.declarationList) {
        this.$refs.declarationList.loadData()
      }
    },
    
    // 创建申报
    async handleCreateDeclaration() {
      try {
        await this.$refs.createForm.validate()
        
        this.createLoading = true
        const response = await createDeclaration(this.createForm)
        
        if (response.success) {
          this.$message.success('创建申报成功')
          this.showCreateDialog = false
          this.resetCreateForm()
          this.handleRefresh()
        } else {
          this.$message.error(response.message || '创建申报失败')
        }
      } catch (error) {
        console.error('创建申报失败:', error)
        this.$message.error('创建申报失败')
      } finally {
        this.createLoading = false
      }
    },
    
    // 生成计划
    async handleGeneratePlan() {
      try {
        await this.$refs.planForm.validate()
        
        this.planLoading = true
        const response = await generateDeclarationPlan(this.planForm)
        
        if (response.success) {
          const count = response.data ? response.data.length : 0
          this.$message.success(`生成申报计划成功，共生成${count}个申报任务`)
          this.showPlanDialog = false
          this.resetPlanForm()
          this.handleRefresh()
        } else {
          this.$message.error(response.message || '生成申报计划失败')
        }
      } catch (error) {
        console.error('生成申报计划失败:', error)
        this.$message.error('生成申报计划失败')
      } finally {
        this.planLoading = false
      }
    },
    
    // 重置创建表单
    resetCreateForm() {
      this.createForm = {
        tenantId: this.$store.getters.tenantId,
        declarationName: '',
        taxType: '',
        declarationType: '',
        declarationPeriod: '',
        taxpayerId: '',
        taxpayerName: '',
        deadline: null,
        priority: 'NORMAL',
        remark: ''
      }
      if (this.$refs.createForm) {
        this.$refs.createForm.resetFields()
      }
    },
    
    // 重置计划表单
    resetPlanForm() {
      this.planForm = {
        tenantId: this.$store.getters.tenantId,
        taxType: '',
        period: '',
        year: new Date().getFullYear()
      }
      if (this.$refs.planForm) {
        this.$refs.planForm.resetFields()
      }
    },
    
    // 工具方法
    getTaxTypeLabel,
    formatTaxAmount,
    isDeclarationOverdue,
    
    formatDate(date) {
      if (!date) return '-'
      return new Date(date).toLocaleString('zh-CN')
    }
  }
}
</script>

<style lang="scss" scoped>
.tax-declaration-container {
  padding: 20px;
  background-color: #f5f7fa;
  min-height: calc(100vh - 84px);
}

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
      
      i {
        margin-right: 8px;
        color: #409EFF;
      }
    }
    
    .page-description {
      margin: 0;
      color: #909399;
      font-size: 14px;
    }
  }
}

.stats-cards {
  margin-bottom: 20px;
  
  .stat-card {
    display: flex;
    align-items: center;
    padding: 20px;
    background: white;
    border-radius: 8px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
    
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
      
      &.draft {
        background: linear-gradient(135deg, #909399, #b3b6bb);
      }
      
      &.pending {
        background: linear-gradient(135deg, #E6A23C, #f0b90b);
      }
      
      &.overdue {
        background: linear-gradient(135deg, #F56C6C, #f78989);
      }
      
      &.completed {
        background: linear-gradient(135deg, #67C23A, #85ce61);
      }
    }
    
    .stat-content {
      .stat-number {
        font-size: 28px;
        font-weight: 600;
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
}

.pending-items {
  padding: 20px;
  
  .pending-card {
    height: 500px;
    
    .empty-state {
      text-align: center;
      padding: 60px 20px;
      color: #909399;
      
      i {
        font-size: 48px;
        margin-bottom: 16px;
        display: block;
      }
      
      p {
        margin: 0;
        font-size: 14px;
      }
    }
    
    .pending-item {
      padding: 12px;
      border: 1px solid #EBEEF5;
      border-radius: 4px;
      margin-bottom: 8px;
      cursor: pointer;
      transition: all 0.3s;
      
      &:hover {
        border-color: #409EFF;
        background-color: #f0f9ff;
      }
      
      .item-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 8px;
        
        .item-title {
          font-weight: 500;
          color: #303133;
        }
      }
      
      .item-content {
        p {
          margin: 4px 0;
          font-size: 12px;
          color: #606266;
        }
      }
    }
  }
}

.create-form,
.plan-form {
  padding: 0 20px;
}

.dialog-footer {
  text-align: right;
}
</style>
