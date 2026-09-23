<template>
  <div class="revenue-container">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-left">
        <h1 class="page-title">
          <i class="el-icon-trophy"></i>
          收入管理
        </h1>
        <p class="page-description">管理收入确认、收入分配、收入分析等收入管理全流程</p>
      </div>
      <div class="header-right">
        <el-button type="primary" icon="el-icon-plus" @click="addRevenue">
          新增收入
        </el-button>
        <el-button type="success" icon="el-icon-check" @click="confirmRevenue">
          收入确认
        </el-button>
        <el-button type="warning" icon="el-icon-data-analysis" @click="revenueAnalysis">
          收入分析
        </el-button>
      </div>
    </div>

    <!-- 收入统计概览 -->
    <div class="stats-overview">
      <el-row :gutter="24">
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon total-revenue">
              <i class="el-icon-trophy"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ formatAmount(stats.totalRevenue) }}</div>
              <div class="stat-label">总收入</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon monthly-revenue">
              <i class="el-icon-data-line"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ formatAmount(stats.monthlyRevenue) }}</div>
              <div class="stat-label">本月收入</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon confirmed-revenue">
              <i class="el-icon-check"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ formatAmount(stats.confirmedRevenue) }}</div>
              <div class="stat-label">已确认收入</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon growth-rate">
              <i class="el-icon-top"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ stats.growthRate }}%</div>
              <div class="stat-label">增长率</div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 功能模块 -->
    <div class="function-modules">
      <el-row :gutter="24">
        <!-- 收入确认 -->
        <el-col :span="8">
          <div class="module-card revenue-recognition">
            <div class="card-header">
              <div class="card-icon">
                <i class="el-icon-check"></i>
              </div>
              <div class="card-title">
                <h3>收入确认</h3>
                <span class="card-subtitle">管理收入的确认条件和确认时点</span>
              </div>
            </div>
            <div class="card-content">
              <div class="feature-list">
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>收入确认规则</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>确认条件检查</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>批量确认处理</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>确认结果审核</span>
                </div>
              </div>
              <div class="card-actions">
                <el-button type="primary" size="small" @click="manageRevenueRecognition">
                  管理收入确认
                </el-button>
              </div>
            </div>
          </div>
        </el-col>

        <!-- 收入分配 -->
        <el-col :span="8">
          <div class="module-card revenue-allocation">
            <div class="card-header">
              <div class="card-icon">
                <i class="el-icon-s-operation"></i>
              </div>
              <div class="card-title">
                <h3>收入分配</h3>
                <span class="card-subtitle">处理收入在不同维度的分配</span>
              </div>
            </div>
            <div class="card-content">
              <div class="feature-list">
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>部门收入分配</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>产品收入分配</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>项目收入分配</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>分配规则设置</span>
                </div>
              </div>
              <div class="card-actions">
                <el-button type="primary" size="small" @click="manageRevenueAllocation">
                  管理收入分配
                </el-button>
              </div>
            </div>
          </div>
        </el-col>

        <!-- 合同收入 -->
        <el-col :span="8">
          <div class="module-card contract-revenue">
            <div class="card-header">
              <div class="card-icon">
                <i class="el-icon-document"></i>
              </div>
              <div class="card-title">
                <h3>合同收入</h3>
                <span class="card-subtitle">管理基于合同的收入确认和核算</span>
              </div>
            </div>
            <div class="card-content">
              <div class="feature-list">
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>合同收入登记</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>履约义务识别</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>收入进度确认</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>合同变更处理</span>
                </div>
              </div>
              <div class="card-actions">
                <el-button type="primary" size="small" @click="manageContractRevenue">
                  管理合同收入
                </el-button>
              </div>
            </div>
          </div>
        </el-col>
      </el-row>

      <el-row :gutter="24" style="margin-top: 24px;">
        <!-- 递延收入 -->
        <el-col :span="8">
          <div class="module-card deferred-revenue">
            <div class="card-header">
              <div class="card-icon">
                <i class="el-icon-time"></i>
              </div>
              <div class="card-title">
                <h3>递延收入</h3>
                <span class="card-subtitle">管理预收款项和递延收入确认</span>
              </div>
            </div>
            <div class="card-content">
              <div class="feature-list">
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>递延收入登记</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>分期确认处理</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>确认计划制定</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>余额跟踪管理</span>
                </div>
              </div>
              <div class="card-actions">
                <el-button type="primary" size="small" @click="manageDeferredRevenue">
                  管理递延收入
                </el-button>
              </div>
            </div>
          </div>
        </el-col>

        <!-- 收入调整 -->
        <el-col :span="8">
          <div class="module-card revenue-adjustment">
            <div class="card-header">
              <div class="card-icon">
                <i class="el-icon-refresh"></i>
              </div>
              <div class="card-title">
                <h3>收入调整</h3>
                <span class="card-subtitle">处理收入的调整和冲回业务</span>
              </div>
            </div>
            <div class="card-content">
              <div class="feature-list">
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>收入调整申请</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>调整审批流程</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>调整凭证生成</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>调整影响分析</span>
                </div>
              </div>
              <div class="card-actions">
                <el-button type="primary" size="small" @click="manageRevenueAdjustment">
                  管理收入调整
                </el-button>
              </div>
            </div>
          </div>
        </el-col>

        <!-- 收入分析 -->
        <el-col :span="8">
          <div class="module-card revenue-analysis-module">
            <div class="card-header">
              <div class="card-icon">
                <i class="el-icon-pie-chart"></i>
              </div>
              <div class="card-title">
                <h3>收入分析</h3>
                <span class="card-subtitle">分析收入结构、趋势和绩效</span>
              </div>
            </div>
            <div class="card-content">
              <div class="feature-list">
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>收入结构分析</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>收入趋势分析</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>收入质量分析</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>收入预测模型</span>
                </div>
              </div>
              <div class="card-actions">
                <el-button type="primary" size="small" @click="analyzeRevenueData">
                  收入分析
                </el-button>
              </div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 快速操作面板 -->
    <div class="quick-actions-panel">
      <div class="panel-header">
        <h3>
          <i class="el-icon-lightning"></i>
          快速操作
        </h3>
        <p>常用的收入管理操作</p>
      </div>
      
      <el-row :gutter="16">
        <el-col :span="4" v-for="action in quickActions" :key="action.key">
          <div class="quick-action-item" @click="handleQuickAction(action)">
            <div class="action-icon">
              <i :class="action.icon"></i>
            </div>
            <div class="action-content">
              <div class="action-title">{{ action.title }}</div>
              <div class="action-desc">{{ action.desc }}</div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 新增收入对话框 -->
    <el-dialog
      title="新增收入"
      :visible.sync="addRevenueDialog"
      width="800px"
      :close-on-click-modal="false"
    >
      <el-form ref="revenueForm" :model="revenueForm" :rules="revenueRules" label-width="120px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="合同编号" prop="contractNo">
              <el-input v-model="revenueForm.contractNo" placeholder="请输入合同编号" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="合同名称" prop="contractName">
              <el-input v-model="revenueForm.contractName" placeholder="请输入合同名称" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="客户名称" prop="customerName">
              <el-input v-model="revenueForm.customerName" placeholder="请输入客户名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="合同金额" prop="contractAmount">
              <el-input v-model="revenueForm.contractAmount" placeholder="请输入合同金额">
                <template slot="append">元</template>
              </el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="签订日期" prop="signDate">
              <el-date-picker
                v-model="revenueForm.signDate"
                type="date"
                placeholder="选择签订日期"
                value-format="yyyy-MM-dd"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="生效日期" prop="effectiveDate">
              <el-date-picker
                v-model="revenueForm.effectiveDate"
                type="date"
                placeholder="选择生效日期"
                value-format="yyyy-MM-dd"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="到期日期" prop="expiryDate">
              <el-date-picker
                v-model="revenueForm.expiryDate"
                type="date"
                placeholder="选择到期日期"
                value-format="yyyy-MM-dd"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="确认方法" prop="recognitionMethod">
              <el-select v-model="revenueForm.recognitionMethod" placeholder="请选择确认方法" style="width: 100%">
                <el-option label="时点法" :value="1" />
                <el-option label="时段法" :value="2" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="收入描述" prop="revenueDesc">
          <el-input
            v-model="revenueForm.revenueDesc"
            type="textarea"
            :rows="3"
            placeholder="请输入收入描述"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="addRevenueDialog = false">取 消</el-button>
        <el-button type="primary" @click="submitRevenue" :loading="submitLoading">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getRevenueStatisticsOverview, createContractRevenue } from '@/api/financialSharing/revenueManagement'

export default {
  name: 'RevenueManagementIndex',
  data() {
    return {
      stats: {
        totalRevenue: 0,
        monthlyRevenue: 0,
        confirmedRevenue: 0,
        growthRate: 0
      },
      quickActions: [
        { key: 'add', title: '新增收入', desc: '登记收入信息', icon: 'el-icon-plus' },
        { key: 'confirm', title: '收入确认', desc: '确认收入实现', icon: 'el-icon-check' },
        { key: 'allocate', title: '收入分配', desc: '分配收入归属', icon: 'el-icon-s-operation' },
        { key: 'contract', title: '合同收入', desc: '管理合同收入', icon: 'el-icon-document' },
        { key: 'deferred', title: '递延收入', desc: '管理递延收入', icon: 'el-icon-time' },
        { key: 'report', title: '收入报表', desc: '生成收入报表', icon: 'el-icon-data-line' }
      ],
      addRevenueDialog: false,
      submitLoading: false,
      revenueForm: {
        contractNo: '',
        contractName: '',
        customerName: '',
        contractAmount: '',
        signDate: '',
        effectiveDate: '',
        expiryDate: '',
        recognitionMethod: 1,
        revenueDesc: ''
      },
      revenueRules: {
        contractNo: [
          { required: true, message: '请输入合同编号', trigger: 'blur' }
        ],
        contractName: [
          { required: true, message: '请输入合同名称', trigger: 'blur' }
        ],
        customerName: [
          { required: true, message: '请输入客户名称', trigger: 'blur' }
        ],
        contractAmount: [
          { required: true, message: '请输入合同金额', trigger: 'blur' },
          { pattern: /^\d+(\.\d{1,2})?$/, message: '请输入正确的金额格式', trigger: 'blur' }
        ],
        signDate: [
          { required: true, message: '请选择签订日期', trigger: 'change' }
        ],
        effectiveDate: [
          { required: true, message: '请选择生效日期', trigger: 'change' }
        ],
        recognitionMethod: [
          { required: true, message: '请选择确认方法', trigger: 'change' }
        ]
      }
    }
  },
  mounted() {
    this.loadStats()
  },
  methods: {
    async loadStats() {
      try {
        // 调用后端API获取统计数据
        const response = await getRevenueStatisticsOverview()
        if (response.code === 1 && response.data) {
          this.stats = {
            totalRevenue: response.data.totalRevenue || 0,
            monthlyRevenue: response.data.monthlyRevenue || 0,
            confirmedRevenue: response.data.recognizedRevenue || 0,
            growthRate: response.data.growthRate || 0
          }
        }
      } catch (error) {
        console.error('加载统计数据失败:', error)
        // 失败时使用默认值
        this.stats = {
          totalRevenue: 0,
          monthlyRevenue: 0,
          confirmedRevenue: 0,
          growthRate: 0
        }
      }
    },
    formatAmount(amount) {
      return (amount / 10000).toFixed(2) + '万'
    },
    addRevenue() {
      this.addRevenueDialog = true
      this.$nextTick(() => {
        if (this.$refs.revenueForm) {
          this.$refs.revenueForm.resetFields()
        }
      })
    },
    async submitRevenue() {
      try {
        await this.$refs.revenueForm.validate()
        this.submitLoading = true

        const response = await createContractRevenue(this.revenueForm)

        if (response.code === 1) {
          this.$message.success('新增收入成功')
          this.addRevenueDialog = false
          this.loadStats() // 刷新统计数据
        } else {
          this.$message.error(response.msg || '新增收入失败')
        }
      } catch (error) {
        if (error !== false) { // 表单验证失败时error为false
          console.error('新增收入失败:', error)
          this.$message.error('新增收入失败，请稍后重试')
        }
      } finally {
        this.submitLoading = false
      }
    },
    confirmRevenue() {
      this.$router.push('/srgl/revenueRecognition')
    },
    revenueAnalysis() {
      // 跳转到收入分析页面
      this.$router.push('/srgl/revenueAnalysis')
    },
    manageRevenueRecognition() {
      this.$router.push('/srgl/revenueRecognition')
    },
    manageRevenueAllocation() {
      this.$router.push('/srgl/revenueAllocation')
    },
    manageContractRevenue() {
      this.$router.push('/srgl/contractRevenue')
    },
    manageDeferredRevenue() {
      this.$router.push('/srgl/deferredRevenue')
    },
    manageRevenueAdjustment() {
      this.$router.push('/srgl/revenueAdjustment')
    },
    analyzeRevenueData() {
      this.$router.push('/srgl/revenueAnalysis')
    },
    handleQuickAction(action) {
      const routeMap = {
        'add': '/financialSharing/financial/revenueManagement/revenueRecognition',
        'confirm': '/financialSharing/financial/revenueManagement/revenueRecognition',
        'allocate': '/financialSharing/financial/revenueManagement/revenueAllocation',
        'contract': '/financialSharing/financial/revenueManagement/contractRevenue',
        'deferred': '/financialSharing/financial/revenueManagement/deferredRevenue',
        'report': '/financialSharing/financial/revenueManagement/revenueAnalysis'
      }

      const route = routeMap[action.key]
      if (route) {
        this.$router.push(route)
      } else {
        this.$message.warning(`暂无"${action.title}"对应的页面路由`)
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.revenue-container {
  padding: 20px;
  background: #f5f7fa;
  min-height: calc(100vh - 84px);
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: white;
  border-radius: 12px;
  padding: 24px;
  margin-bottom: 24px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);

  .header-left {
    .page-title {
      font-size: 24px;
      font-weight: 600;
      color: #303133;
      margin: 0 0 8px 0;
      display: flex;
      align-items: center;

      i {
        margin-right: 12px;
        color: #409eff;
      }
    }

    .page-description {
      color: #606266;
      font-size: 14px;
      margin: 0;
    }
  }

  .header-right {
    .el-button {
      margin-left: 12px;
    }
  }
}

.stats-overview {
  margin-bottom: 24px;

  .stat-card {
    background: white;
    border-radius: 12px;
    padding: 24px;
    display: flex;
    align-items: center;
    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
    transition: all 0.3s ease;

    &:hover {
      transform: translateY(-2px);
      box-shadow: 0 8px 25px rgba(0, 0, 0, 0.15);
    }

    .stat-icon {
      width: 60px;
      height: 60px;
      border-radius: 12px;
      display: flex;
      align-items: center;
      justify-content: center;
      margin-right: 16px;

      i {
        font-size: 28px;
        color: white;
      }

      &.total-revenue {
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      }

      &.monthly-revenue {
        background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
      }

      &.confirmed-revenue {
        background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
      }

      &.growth-rate {
        background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
      }
    }

    .stat-content {
      .stat-value {
        font-size: 28px;
        font-weight: 600;
        color: #303133;
        margin-bottom: 4px;
      }

      .stat-label {
        font-size: 14px;
        color: #909399;
      }
    }
  }
}

.function-modules {
  margin-bottom: 32px;

  .module-card {
    background: white;
    border-radius: 12px;
    padding: 24px;
    height: 280px;
    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
    transition: all 0.3s ease;
    display: flex;
    flex-direction: column;

    &:hover {
      transform: translateY(-4px);
      box-shadow: 0 8px 25px rgba(0, 0, 0, 0.15);
    }

    &.revenue-recognition {
      border-left: 4px solid #409eff;
    }

    &.revenue-allocation {
      border-left: 4px solid #67c23a;
    }

    &.contract-revenue {
      border-left: 4px solid #e6a23c;
    }

    &.deferred-revenue {
      border-left: 4px solid #f56c6c;
    }

    &.revenue-adjustment {
      border-left: 4px solid #909399;
    }

    &.revenue-analysis-module {
      border-left: 4px solid #9c27b0;
    }
  }
}

.card-header {
  display: flex;
  align-items: flex-start;
  margin-bottom: 20px;

  .card-icon {
    width: 48px;
    height: 48px;
    border-radius: 12px;
    display: flex;
    align-items: center;
    justify-content: center;
    margin-right: 16px;
    flex-shrink: 0;
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);

    i {
      font-size: 24px;
      color: white;
    }
  }

  .card-title {
    flex: 1;

    h3 {
      font-size: 16px;
      font-weight: 600;
      color: #303133;
      margin: 0 0 4px 0;
    }

    .card-subtitle {
      font-size: 12px;
      color: #909399;
      line-height: 1.4;
    }
  }
}

.card-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.feature-list {
  flex: 1;

  .feature-item {
    display: flex;
    align-items: center;
    margin-bottom: 12px;
    font-size: 14px;
    color: #606266;

    i {
      color: #67c23a;
      margin-right: 8px;
      font-size: 16px;
    }
  }
}

.card-actions {
  margin-top: 16px;
}

.quick-actions-panel {
  background: white;
  border-radius: 12px;
  padding: 24px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);

  .panel-header {
    margin-bottom: 24px;

    h3 {
      font-size: 18px;
      font-weight: 600;
      color: #303133;
      margin: 0 0 8px 0;
      display: flex;
      align-items: center;

      i {
        margin-right: 8px;
        color: #409eff;
      }
    }

    p {
      color: #606266;
      font-size: 14px;
      margin: 0;
    }
  }

  .quick-action-item {
    background: #f8f9fa;
    border-radius: 8px;
    padding: 16px;
    cursor: pointer;
    transition: all 0.3s ease;
    display: flex;
    align-items: center;

    &:hover {
      background: #e9ecef;
      transform: translateY(-2px);
    }

    .action-icon {
      width: 40px;
      height: 40px;
      border-radius: 8px;
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      display: flex;
      align-items: center;
      justify-content: center;
      margin-right: 12px;

      i {
        font-size: 20px;
        color: white;
      }
    }

    .action-content {
      flex: 1;

      .action-title {
        font-size: 14px;
        font-weight: 600;
        color: #303133;
        margin-bottom: 4px;
      }

      .action-desc {
        font-size: 12px;
        color: #909399;
      }
    }
  }
}
</style>
