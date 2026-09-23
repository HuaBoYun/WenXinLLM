<template>
  <div class="data-quality-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>数据质量管理</h2>
      <p>管理和监控企业数据质量，确保数据的完整性、准确性和一致性</p>
    </div>

    <!-- 统计概览 -->
    <div class="overview-section">
      <el-row :gutter="20">
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon total">
              <i class="el-icon-data-line"></i>
            </div>
            <div class="stat-content">
              <div class="stat-number">{{ overviewData.totalCount || 0 }}</div>
              <div class="stat-label">数据质量总数</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon checking">
              <i class="el-icon-loading"></i>
            </div>
            <div class="stat-content">
              <div class="stat-number">{{ overviewData.checkingCount || 0 }}</div>
              <div class="stat-label">检查中</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon passed">
              <i class="el-icon-circle-check"></i>
            </div>
            <div class="stat-content">
              <div class="stat-number">{{ overviewData.passedCount || 0 }}</div>
              <div class="stat-label">检查通过</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon failed">
              <i class="el-icon-circle-close"></i>
            </div>
            <div class="stat-content">
              <div class="stat-number">{{ overviewData.failedCount || 0 }}</div>
              <div class="stat-label">检查失败</div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 功能导航 -->
    <div class="navigation-section">
      <el-row :gutter="20">
        <el-col :span="8">
          <div class="nav-card" @click="navigateToList">
            <div class="nav-icon">
              <i class="el-icon-menu"></i>
            </div>
            <div class="nav-content">
              <h3>数据质量列表</h3>
              <p>查看和管理所有数据质量规则</p>
            </div>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="nav-card" @click="navigateToMonitor">
            <div class="nav-icon">
              <i class="el-icon-monitor"></i>
            </div>
            <div class="nav-content">
              <h3>质量监控</h3>
              <p>实时监控数据质量状态</p>
            </div>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="nav-card" @click="navigateToDashboard">
            <div class="nav-icon">
              <i class="el-icon-data-analysis"></i>
            </div>
            <div class="nav-content">
              <h3>质量分析</h3>
              <p>数据质量趋势分析和报告</p>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 最近活动 -->
    <div class="activity-section">
      <h3>最近活动</h3>
      <el-timeline>
        <el-timeline-item
          v-for="activity in recentActivities"
          :key="activity.id"
          :timestamp="activity.time"
          :type="activity.type"
        >
          {{ activity.description }}
        </el-timeline-item>
      </el-timeline>
    </div>

    <!-- 快速访问 -->
    <div class="quick-access-section">
      <h3>快速访问</h3>
      <el-row :gutter="15">
        <el-col :span="4">
          <div class="quick-item" @click="quickCreateDataQuality">
            <i class="el-icon-plus"></i>
            <span>创建质量规则</span>
          </div>
        </el-col>
        <el-col :span="4">
          <div class="quick-item" @click="quickExecuteCheck">
            <i class="el-icon-refresh"></i>
            <span>执行质量检查</span>
          </div>
        </el-col>
        <el-col :span="4">
          <div class="quick-item" @click="quickViewPending">
            <i class="el-icon-warning"></i>
            <span>待检查项目</span>
          </div>
        </el-col>
        <el-col :span="4">
          <div class="quick-item" @click="quickViewFailed">
            <i class="el-icon-error"></i>
            <span>检查失败项目</span>
          </div>
        </el-col>
        <el-col :span="4">
          <div class="quick-item" @click="quickViewStats">
            <i class="el-icon-pie-chart"></i>
            <span>质量统计</span>
          </div>
        </el-col>
        <el-col :span="4">
          <div class="quick-item" @click="quickViewDocs">
            <i class="el-icon-document"></i>
            <span>帮助文档</span>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 创建数据质量对话框 -->
    <el-dialog
      title="创建数据质量规则"
      :visible.sync="createDialogVisible"
      width="600px"
      @close="resetCreateForm"
    >
      <el-form
        ref="createForm"
        :model="createForm"
        :rules="createRules"
        label-width="120px"
      >
        <el-form-item label="质量编码" prop="qualityCode">
          <el-input v-model="createForm.qualityCode" placeholder="请输入质量编码" />
        </el-form-item>
        <el-form-item label="质量名称" prop="qualityName">
          <el-input v-model="createForm.qualityName" placeholder="请输入质量名称" />
        </el-form-item>
        <el-form-item label="质量类型" prop="qualityType">
          <el-select v-model="createForm.qualityType" placeholder="请选择质量类型" style="width: 100%">
            <el-option
              v-for="option in qualityTypeOptions"
              :key="option.value"
              :label="option.label"
              :value="option.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="质量分类" prop="qualityCategory">
          <el-select v-model="createForm.qualityCategory" placeholder="请选择质量分类" style="width: 100%">
            <el-option
              v-for="option in qualityCategoryOptions"
              :key="option.value"
              :label="option.label"
              :value="option.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="质量模块" prop="qualityModule">
          <el-select v-model="createForm.qualityModule" placeholder="请选择质量模块" style="width: 100%">
            <el-option label="预算模块" value="BUDGET" />
            <el-option label="报表模块" value="REPORT" />
            <el-option label="分析模块" value="ANALYSIS" />
          </el-select>
        </el-form-item>
        <el-form-item label="数据源" prop="dataSourceId">
          <el-input v-model="createForm.dataSourceId" placeholder="请输入数据源ID" />
        </el-form-item>
        <el-form-item label="质量描述">
          <el-input
            v-model="createForm.remark"
            type="textarea"
            :rows="3"
            placeholder="请输入质量描述"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="createDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleCreateDataQuality">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  getDataQualitySystemOverview,
  createDataQuality,
  autoExecuteDataQualityCheck,
  getPendingCheckDataQuality,
  getFailedCheckDataQuality,
  getDataQualityOptions
} from '@/api/managementAccountant/eps/dataQuality'

export default {
  name: 'DataQualityIndex',
  data() {
    return {
      // 概览数据
      overviewData: {
        totalCount: 0,
        checkingCount: 0,
        passedCount: 0,
        failedCount: 0
      },
      // 最近活动
      recentActivities: [
        {
          id: 1,
          description: '执行了预算数据完整性检查',
          time: '2024-01-20 10:30:00',
          type: 'primary'
        },
        {
          id: 2,
          description: '修复了报表数据一致性问题',
          time: '2024-01-20 09:15:00',
          type: 'success'
        },
        {
          id: 3,
          description: '创建了新的数据质量规则',
          time: '2024-01-20 08:45:00',
          type: 'info'
        },
        {
          id: 4,
          description: '数据质量检查发现异常',
          time: '2024-01-19 16:20:00',
          type: 'warning'
        }
      ],
      // 创建对话框
      createDialogVisible: false,
      createForm: {
        qualityCode: '',
        qualityName: '',
        qualityType: '',
        qualityCategory: '',
        qualityModule: '',
        dataSourceId: '',
        remark: ''
      },
      createRules: {
        qualityCode: [
          { required: true, message: '请输入质量编码', trigger: 'blur' },
          { pattern: /^[A-Z0-9_]{3,50}$/, message: '编码格式不正确', trigger: 'blur' }
        ],
        qualityName: [
          { required: true, message: '请输入质量名称', trigger: 'blur' }
        ],
        qualityType: [
          { required: true, message: '请选择质量类型', trigger: 'change' }
        ],
        qualityCategory: [
          { required: true, message: '请选择质量分类', trigger: 'change' }
        ],
        qualityModule: [
          { required: true, message: '请选择质量模块', trigger: 'change' }
        ]
      },
      // 选项数据
      qualityTypeOptions: [],
      qualityCategoryOptions: []
    }
  },
  created() {
    this.loadOverviewData()
    this.loadOptions()
  },
  methods: {
    // 加载概览数据
    async loadOverviewData() {
      try {
        const response = await getDataQualitySystemOverview()
        if (response.success) {
          this.overviewData = response.data || {}
        }
      } catch (error) {
        console.error('加载概览数据失败:', error)
      }
    },
    // 加载选项数据
    loadOptions() {
      const options = getDataQualityOptions()
      this.qualityTypeOptions = options.qualityTypes
      this.qualityCategoryOptions = options.qualityCategories
    },
    // 导航到列表页面
    navigateToList() {
      this.$router.push('/eps/data-quality/list')
    },
    // 导航到监控页面
    navigateToMonitor() {
      this.$router.push('/eps/data-quality/monitor')
    },
    // 导航到分析页面
    navigateToDashboard() {
      this.$router.push('/eps/data-quality/dashboard')
    },
    // 显示创建对话框
    quickCreateDataQuality() {
      this.createDialogVisible = true
    },
    // 快速执行检查
    async quickExecuteCheck() {
      try {
        this.$loading({
          lock: true,
          text: '正在执行质量检查...',
          spinner: 'el-icon-loading'
        })
        
        const response = await autoExecuteDataQualityCheck()
        this.$loading().close()
        
        if (response.success) {
          this.$message.success('质量检查执行成功')
          this.loadOverviewData()
        } else {
          this.$message.error(response.message || '质量检查执行失败')
        }
      } catch (error) {
        this.$loading().close()
        this.$message.error('质量检查执行异常')
        console.error('执行质量检查失败:', error)
      }
    },
    // 查看待检查项目
    async quickViewPending() {
      try {
        const response = await getPendingCheckDataQuality()
        if (response.success) {
          const count = response.data ? response.data.length : 0
          this.$message.info(`当前有 ${count} 个待检查项目`)
          if (count > 0) {
            this.navigateToList()
          }
        }
      } catch (error) {
        this.$message.error('获取待检查项目失败')
        console.error('获取待检查项目失败:', error)
      }
    },
    // 查看检查失败项目
    async quickViewFailed() {
      try {
        const response = await getFailedCheckDataQuality()
        if (response.success) {
          const count = response.data ? response.data.length : 0
          this.$message.info(`当前有 ${count} 个检查失败项目`)
          if (count > 0) {
            this.navigateToList()
          }
        }
      } catch (error) {
        this.$message.error('获取检查失败项目失败')
        console.error('获取检查失败项目失败:', error)
      }
    },
    // 查看质量统计
    quickViewStats() {
      this.navigateToDashboard()
    },
    // 查看帮助文档
    quickViewDocs() {
      this.$message.info('帮助文档功能开发中...')
    },
    // 处理创建数据质量
    handleCreateDataQuality() {
      this.$refs.createForm.validate(async (valid) => {
        if (valid) {
          try {
            const response = await createDataQuality(this.createForm)
            if (response.success) {
              this.$message.success('创建数据质量规则成功')
              this.createDialogVisible = false
              this.loadOverviewData()
            } else {
              this.$message.error(response.message || '创建数据质量规则失败')
            }
          } catch (error) {
            this.$message.error('创建数据质量规则异常')
            console.error('创建数据质量规则失败:', error)
          }
        }
      })
    },
    // 重置创建表单
    resetCreateForm() {
      this.$refs.createForm && this.$refs.createForm.resetFields()
      this.createForm = {
        qualityCode: '',
        qualityName: '',
        qualityType: '',
        qualityCategory: '',
        qualityModule: '',
        dataSourceId: '',
        remark: ''
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.data-quality-container {
  padding: 20px;
  
  .page-header {
    margin-bottom: 30px;
    
    h2 {
      margin: 0 0 10px 0;
      color: #303133;
      font-size: 24px;
      font-weight: 600;
    }
    
    p {
      margin: 0;
      color: #606266;
      font-size: 14px;
    }
  }
  
  .overview-section {
    margin-bottom: 30px;
    
    .stat-card {
      display: flex;
      align-items: center;
      padding: 20px;
      background: #fff;
      border-radius: 8px;
      box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
      
      .stat-icon {
        width: 60px;
        height: 60px;
        border-radius: 50%;
        display: flex;
        align-items: center;
        justify-content: center;
        margin-right: 15px;
        
        i {
          font-size: 24px;
          color: #fff;
        }
        
        &.total {
          background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        }
        
        &.checking {
          background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
        }
        
        &.passed {
          background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
        }
        
        &.failed {
          background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
        }
      }
      
      .stat-content {
        .stat-number {
          font-size: 28px;
          font-weight: 600;
          color: #303133;
          line-height: 1;
        }
        
        .stat-label {
          font-size: 14px;
          color: #909399;
          margin-top: 5px;
        }
      }
    }
  }
  
  .navigation-section {
    margin-bottom: 30px;
    
    .nav-card {
      display: flex;
      align-items: center;
      padding: 20px;
      background: #fff;
      border-radius: 8px;
      box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
      cursor: pointer;
      transition: all 0.3s;
      
      &:hover {
        transform: translateY(-2px);
        box-shadow: 0 4px 20px 0 rgba(0, 0, 0, 0.15);
      }
      
      .nav-icon {
        width: 50px;
        height: 50px;
        border-radius: 8px;
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        display: flex;
        align-items: center;
        justify-content: center;
        margin-right: 15px;
        
        i {
          font-size: 20px;
          color: #fff;
        }
      }
      
      .nav-content {
        h3 {
          margin: 0 0 5px 0;
          color: #303133;
          font-size: 16px;
          font-weight: 600;
        }
        
        p {
          margin: 0;
          color: #909399;
          font-size: 12px;
        }
      }
    }
  }
  
  .activity-section {
    margin-bottom: 30px;
    padding: 20px;
    background: #fff;
    border-radius: 8px;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
    
    h3 {
      margin: 0 0 20px 0;
      color: #303133;
      font-size: 18px;
      font-weight: 600;
    }
  }
  
  .quick-access-section {
    padding: 20px;
    background: #fff;
    border-radius: 8px;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
    
    h3 {
      margin: 0 0 20px 0;
      color: #303133;
      font-size: 18px;
      font-weight: 600;
    }
    
    .quick-item {
      display: flex;
      flex-direction: column;
      align-items: center;
      padding: 20px;
      border: 1px solid #e4e7ed;
      border-radius: 8px;
      cursor: pointer;
      transition: all 0.3s;
      
      &:hover {
        border-color: #409eff;
        background: #f0f9ff;
      }
      
      i {
        font-size: 24px;
        color: #409eff;
        margin-bottom: 10px;
      }
      
      span {
        font-size: 12px;
        color: #606266;
        text-align: center;
      }
    }
  }
}
</style>
