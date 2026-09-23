<template>
  <div class="tax-planning-container">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <h2 class="page-title">
            <i class="el-icon-s-marketing"></i>
            税务筹划
          </h2>
          <p class="page-description">智能化税务筹划管理，提供筹划方案设计、风险评估、效益分析和执行跟踪</p>
        </div>
        <div class="header-right">
          <el-button type="primary" icon="el-icon-plus" @click="handleCreatePlanning">
            新建筹划
          </el-button>
          <el-button icon="el-icon-refresh" @click="refreshData">
            刷新
          </el-button>
        </div>
      </div>
    </div>

    <!-- 概览统计卡片 -->
    <div class="overview-cards">
      <el-row :gutter="20">
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon total">
              <i class="el-icon-s-marketing"></i>
            </div>
            <div class="stat-content">
              <div class="stat-number">{{ overview.totalCount || 0 }}</div>
              <div class="stat-label">总筹划数</div>
            </div>
          </div>
        </el-col>
        
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon executing">
              <i class="el-icon-loading"></i>
            </div>
            <div class="stat-content">
              <div class="stat-number">{{ overview.executingCount || 0 }}</div>
              <div class="stat-label">执行中</div>
            </div>
          </div>
        </el-col>
        
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon saving">
              <i class="el-icon-coin"></i>
            </div>
            <div class="stat-content">
              <div class="stat-number">{{ formatAmount(overview.totalTaxSaving) }}</div>
              <div class="stat-label">总节税金额(万元)</div>
            </div>
          </div>
        </el-col>
        
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon roi">
              <i class="el-icon-trophy"></i>
            </div>
            <div class="stat-content">
              <div class="stat-number">{{ formatPercentage(overview.avgRoi) }}</div>
              <div class="stat-label">平均投资回报率</div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 功能标签页 -->
    <div class="main-content">
      <el-tabs v-model="activeTab" @tab-click="handleTabClick">
        <!-- 筹划列表 -->
        <el-tab-pane label="筹划列表" name="list">
          <TaxPlanningList 
            ref="planningList"
            @view-detail="handleViewDetail"
            @edit-planning="handleEditPlanning"
            @delete-planning="handleDeletePlanning"
            @refresh="loadOverviewData"
          />
        </el-tab-pane>
        
        <!-- 数据分析 -->
        <el-tab-pane label="数据分析" name="dashboard">
          <TaxPlanningDashboard 
            ref="planningDashboard"
          />
        </el-tab-pane>
        
        <!-- 待处理事项 -->
        <el-tab-pane label="待处理事项" name="pending">
          <div class="pending-items">
            <el-row :gutter="20">
              <el-col :span="8">
                <el-card header="即将到期" class="pending-card">
                  <div class="pending-list">
                    <div 
                      v-for="item in expiringSoonPlannings" 
                      :key="item.planningId"
                      class="pending-item"
                      @click="handleViewDetail(item)"
                    >
                      <div class="item-title">{{ item.planningName }}</div>
                      <div class="item-meta">
                        <span class="item-date">{{ formatDate(item.endTime) }}</span>
                        <el-tag size="mini" type="warning">{{ getDaysUntilExpiry(item.endTime) }}天后到期</el-tag>
                      </div>
                    </div>
                    <div v-if="!expiringSoonPlannings.length" class="empty-state">
                      <i class="el-icon-check"></i>
                      <p>暂无即将到期的筹划</p>
                    </div>
                  </div>
                </el-card>
              </el-col>
              
              <el-col :span="8">
                <el-card header="高风险筹划" class="pending-card">
                  <div class="pending-list">
                    <div 
                      v-for="item in highRiskPlannings" 
                      :key="item.planningId"
                      class="pending-item"
                      @click="handleViewDetail(item)"
                    >
                      <div class="item-title">{{ item.planningName }}</div>
                      <div class="item-meta">
                        <span class="item-type">{{ getPlanningTypeLabel(item.planningType) }}</span>
                        <el-tag size="mini" type="danger">{{ getRiskLevelLabel(item.riskLevel) }}</el-tag>
                      </div>
                    </div>
                    <div v-if="!highRiskPlannings.length" class="empty-state">
                      <i class="el-icon-check"></i>
                      <p>暂无高风险筹划</p>
                    </div>
                  </div>
                </el-card>
              </el-col>
              
              <el-col :span="8">
                <el-card header="逾期筹划" class="pending-card">
                  <div class="pending-list">
                    <div 
                      v-for="item in overduePlannings" 
                      :key="item.planningId"
                      class="pending-item"
                      @click="handleViewDetail(item)"
                    >
                      <div class="item-title">{{ item.planningName }}</div>
                      <div class="item-meta">
                        <span class="item-date">{{ formatDate(item.endTime) }}</span>
                        <el-tag size="mini" type="danger">已逾期</el-tag>
                      </div>
                    </div>
                    <div v-if="!overduePlannings.length" class="empty-state">
                      <i class="el-icon-check"></i>
                      <p>暂无逾期筹划</p>
                    </div>
                  </div>
                </el-card>
              </el-col>
            </el-row>
          </div>
        </el-tab-pane>
      </el-tabs>
    </div>

    <!-- 创建筹划对话框 -->
    <el-dialog
      title="新建税务筹划"
      :visible.sync="createDialogVisible"
      width="800px"
      :close-on-click-modal="false"
    >
      <el-form
        ref="createForm"
        :model="createForm"
        :rules="createRules"
        label-width="120px"
        size="small"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="筹划名称" prop="planningName">
              <el-input v-model="createForm.planningName" placeholder="请输入筹划名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="筹划类型" prop="planningType">
              <el-select v-model="createForm.planningType" placeholder="请选择筹划类型" style="width: 100%">
                <el-option
                  v-for="(label, value) in planningTypeOptions"
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
            <el-form-item label="税种" prop="taxType">
              <el-select v-model="createForm.taxType" placeholder="请选择税种" style="width: 100%">
                <el-option
                  v-for="(label, value) in taxTypeOptions"
                  :key="value"
                  :label="label"
                  :value="value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="优先级" prop="priority">
              <el-select v-model="createForm.priority" placeholder="请选择优先级" style="width: 100%">
                <el-option
                  v-for="(label, value) in priorityOptions"
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
            <el-form-item label="责任人" prop="responsiblePerson">
              <el-input v-model="createForm.responsiblePerson" placeholder="请输入责任人" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="责任部门" prop="responsibleDepartment">
              <el-input v-model="createForm.responsibleDepartment" placeholder="请输入责任部门" />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-form-item label="筹划描述" prop="planningDescription">
          <el-input
            v-model="createForm.planningDescription"
            type="textarea"
            :rows="3"
            placeholder="请输入筹划描述"
          />
        </el-form-item>
        
        <el-form-item label="筹划目标" prop="planningObjective">
          <el-input
            v-model="createForm.planningObjective"
            type="textarea"
            :rows="2"
            placeholder="请输入筹划目标"
          />
        </el-form-item>
      </el-form>
      
      <div slot="footer" class="dialog-footer">
        <el-button @click="createDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleCreateSubmit" :loading="createLoading">
          创建
        </el-button>
      </div>
    </el-dialog>

    <!-- 筹划详情对话框 -->
    <el-dialog
      title="筹划详情"
      :visible.sync="detailDialogVisible"
      width="1200px"
      :close-on-click-modal="false"
    >
      <TaxPlanningDetail
        v-if="detailDialogVisible && selectedPlanningId"
        :planning-id="selectedPlanningId"
        :tenant-id="$store.getters.tenantId"
        @close="detailDialogVisible = false"
        @refresh="handleRefreshAfterUpdate"
      />
    </el-dialog>
  </div>
</template>

<script>
import {
  getPlanningOverview,
  createPlanning,
  getExpiringSoonPlannings,
  getHighRiskPlannings,
  getOverduePlannings,
  generatePlanningCode,
  getPlanningTypeLabel,
  getRiskLevelLabel,
  formatAmount,
  formatPercentage,
  PLANNING_TYPES,
  TAX_TYPES,
  PRIORITIES
} from '@/api/managementAccountant/ts/taxPlanning'

import TaxPlanningList from './TaxPlanningList'
import TaxPlanningDashboard from './TaxPlanningDashboard'
import TaxPlanningDetail from './TaxPlanningDetail'

export default {
  name: 'TaxPlanningIndex',
  components: {
    TaxPlanningList,
    TaxPlanningDashboard,
    TaxPlanningDetail
  },
  data() {
    return {
      loading: false,
      activeTab: 'list',
      
      // 概览数据
      overview: {},
      
      // 待处理事项
      expiringSoonPlannings: [],
      highRiskPlannings: [],
      overduePlannings: [],
      
      // 创建对话框
      createDialogVisible: false,
      createLoading: false,
      createForm: {
        planningName: '',
        planningType: '',
        taxType: '',
        priority: 'NORMAL',
        responsiblePerson: '',
        responsibleDepartment: '',
        planningDescription: '',
        planningObjective: ''
      },
      createRules: {
        planningName: [
          { required: true, message: '请输入筹划名称', trigger: 'blur' }
        ],
        planningType: [
          { required: true, message: '请选择筹划类型', trigger: 'change' }
        ],
        taxType: [
          { required: true, message: '请选择税种', trigger: 'change' }
        ],
        priority: [
          { required: true, message: '请选择优先级', trigger: 'change' }
        ]
      },
      
      // 详情对话框
      detailDialogVisible: false,
      selectedPlanningId: null,
      
      // 选项数据
      planningTypeOptions: {},
      taxTypeOptions: {},
      priorityOptions: {}
    }
  },
  created() {
    this.initOptions()
    this.loadData()
  },
  methods: {
    // 初始化选项数据
    initOptions() {
      // 筹划类型选项
      this.planningTypeOptions = {
        [PLANNING_TYPES.TAX_REDUCTION]: '减税筹划',
        [PLANNING_TYPES.TAX_DEFERRAL]: '延税筹划',
        [PLANNING_TYPES.TAX_EXEMPTION]: '免税筹划',
        [PLANNING_TYPES.TAX_CREDIT]: '税收抵免',
        [PLANNING_TYPES.STRUCTURE_OPTIMIZATION]: '结构优化',
        [PLANNING_TYPES.BUSINESS_RESTRUCTURING]: '业务重组',
        [PLANNING_TYPES.INVESTMENT_PLANNING]: '投资筹划',
        [PLANNING_TYPES.MERGER_ACQUISITION]: '并购筹划',
        [PLANNING_TYPES.INTERNATIONAL_PLANNING]: '国际筹划',
        [PLANNING_TYPES.OTHER]: '其他'
      }
      
      // 税种选项
      this.taxTypeOptions = {
        [TAX_TYPES.VAT]: '增值税',
        [TAX_TYPES.CORPORATE_INCOME_TAX]: '企业所得税',
        [TAX_TYPES.INDIVIDUAL_INCOME_TAX]: '个人所得税',
        [TAX_TYPES.BUSINESS_TAX]: '营业税',
        [TAX_TYPES.CONSUMPTION_TAX]: '消费税',
        [TAX_TYPES.STAMP_TAX]: '印花税',
        [TAX_TYPES.PROPERTY_TAX]: '房产税',
        [TAX_TYPES.LAND_USE_TAX]: '土地使用税',
        [TAX_TYPES.VEHICLE_TAX]: '车船税',
        [TAX_TYPES.OTHER]: '其他'
      }
      
      // 优先级选项
      this.priorityOptions = {
        [PRIORITIES.LOW]: '低',
        [PRIORITIES.NORMAL]: '普通',
        [PRIORITIES.HIGH]: '高',
        [PRIORITIES.URGENT]: '紧急'
      }
    },
    
    // 加载数据
    async loadData() {
      await Promise.all([
        this.loadOverviewData(),
        this.loadPendingItems()
      ])
    },
    
    // 加载概览数据
    async loadOverviewData() {
      try {
        const response = await getPlanningOverview(this.$store.getters.tenantId)
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
        const [expiringSoonRes, highRiskRes, overdueRes] = await Promise.all([
          getExpiringSoonPlannings(this.$store.getters.tenantId, 7),
          getHighRiskPlannings(this.$store.getters.tenantId),
          getOverduePlannings(this.$store.getters.tenantId)
        ])
        
        if (expiringSoonRes.success) {
          this.expiringSoonPlannings = expiringSoonRes.data || []
        }
        
        if (highRiskRes.success) {
          this.highRiskPlannings = highRiskRes.data || []
        }
        
        if (overdueRes.success) {
          this.overduePlannings = overdueRes.data || []
        }
      } catch (error) {
        console.error('加载待处理事项失败:', error)
      }
    },
    
    // 刷新数据
    refreshData() {
      this.loadData()
      if (this.$refs.planningList) {
        this.$refs.planningList.refreshData()
      }
      if (this.$refs.planningDashboard) {
        this.$refs.planningDashboard.refreshData()
      }
    },
    
    // 标签页切换
    handleTabClick(tab) {
      if (tab.name === 'pending') {
        this.loadPendingItems()
      }
    },
    
    // 创建筹划
    handleCreatePlanning() {
      this.createDialogVisible = true
      this.resetCreateForm()
    },
    
    // 重置创建表单
    resetCreateForm() {
      this.createForm = {
        planningName: '',
        planningType: '',
        taxType: '',
        priority: 'NORMAL',
        responsiblePerson: '',
        responsibleDepartment: '',
        planningDescription: '',
        planningObjective: ''
      }
      if (this.$refs.createForm) {
        this.$refs.createForm.clearValidate()
      }
    },
    
    // 提交创建
    async handleCreateSubmit() {
      try {
        await this.$refs.createForm.validate()
        
        this.createLoading = true
        const response = await createPlanning(this.$store.getters.tenantId, this.createForm)
        
        if (response.success) {
          this.$message.success('创建成功')
          this.createDialogVisible = false
          this.refreshData()
        } else {
          this.$message.error(response.message || '创建失败')
        }
      } catch (error) {
        if (error !== false) { // 表单验证失败时会返回false
          console.error('创建筹划失败:', error)
          this.$message.error('创建失败')
        }
      } finally {
        this.createLoading = false
      }
    },
    
    // 查看详情
    handleViewDetail(planning) {
      this.selectedPlanningId = planning.planningId
      this.detailDialogVisible = true
    },
    
    // 编辑筹划
    handleEditPlanning(planning) {
      // 实现编辑逻辑
      this.$message.info('编辑功能开发中')
    },
    
    // 删除筹划
    handleDeletePlanning(planning) {
      // 实现删除逻辑
      this.$message.info('删除功能开发中')
    },
    
    // 更新后刷新
    handleRefreshAfterUpdate() {
      this.refreshData()
    },
    
    // 工具方法
    getPlanningTypeLabel,
    getRiskLevelLabel,
    formatAmount,
    formatPercentage,
    
    formatDate(date) {
      if (!date) return '-'
      return new Date(date).toLocaleDateString('zh-CN')
    },
    
    getDaysUntilExpiry(endTime) {
      if (!endTime) return 0
      const now = new Date()
      const end = new Date(endTime)
      const diffTime = end - now
      const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24))
      return Math.max(0, diffDays)
    }
  }
}
</script>

<style lang="scss" scoped>
.tax-planning-container {
  padding: 20px;
  
  .page-header {
    margin-bottom: 20px;
    
    .header-content {
      display: flex;
      justify-content: space-between;
      align-items: flex-start;
      
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
      
      .header-right {
        .el-button {
          margin-left: 12px;
        }
      }
    }
  }
  
  .overview-cards {
    margin-bottom: 20px;
    
    .stat-card {
      display: flex;
      align-items: center;
      padding: 20px;
      background: white;
      border-radius: 8px;
      box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
      cursor: pointer;
      transition: all 0.3s ease;
      
      &:hover {
        transform: translateY(-2px);
        box-shadow: 0 4px 8px rgba(0, 0, 0, 0.15);
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
          background: linear-gradient(135deg, #667eea, #764ba2);
        }
        
        &.executing {
          background: linear-gradient(135deg, #f093fb, #f5576c);
        }
        
        &.saving {
          background: linear-gradient(135deg, #4facfe, #00f2fe);
        }
        
        &.roi {
          background: linear-gradient(135deg, #43e97b, #38f9d7);
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
    
    .el-tabs {
      padding: 20px;
    }
  }
  
  .pending-items {
    .pending-card {
      height: 400px;
      
      .pending-list {
        max-height: 320px;
        overflow-y: auto;
        
        .pending-item {
          padding: 12px;
          border-bottom: 1px solid #f0f0f0;
          cursor: pointer;
          transition: background-color 0.3s ease;
          
          &:hover {
            background-color: #f5f7fa;
          }
          
          &:last-child {
            border-bottom: none;
          }
          
          .item-title {
            font-weight: 500;
            color: #303133;
            margin-bottom: 8px;
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
          }
          
          .item-meta {
            display: flex;
            justify-content: space-between;
            align-items: center;
            
            .item-date,
            .item-type {
              font-size: 12px;
              color: #909399;
            }
          }
        }
        
        .empty-state {
          text-align: center;
          padding: 40px 20px;
          color: #909399;
          
          i {
            font-size: 48px;
            margin-bottom: 16px;
            color: #67C23A;
          }
          
          p {
            margin: 0;
            font-size: 14px;
          }
        }
      }
    }
  }
  
  .dialog-footer {
    text-align: right;
    
    .el-button {
      margin-left: 12px;
    }
  }
}
</style>
