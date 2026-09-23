<template>
  <div class="tax-declaration-detail" v-loading="loading">
    <div v-if="declaration">
      <!-- 基本信息卡片 -->
      <el-card class="detail-card" header="基本信息">
        <el-row :gutter="20">
          <el-col :span="8">
            <div class="info-item">
              <label>申报编号：</label>
              <span>{{ declaration.declarationCode }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <label>申报名称：</label>
              <span>{{ declaration.declarationName }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <label>申报状态：</label>
              <el-tag :type="getStatusTagType(declaration.declarationStatus)">
                {{ getDeclarationStatusLabel(declaration.declarationStatus) }}
              </el-tag>
            </div>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="8">
            <div class="info-item">
              <label>税种：</label>
              <el-tag type="info" size="small">
                {{ getTaxTypeLabel(declaration.taxType) }}
              </el-tag>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <label>申报类型：</label>
              <span>{{ getDeclarationTypeLabel(declaration.declarationType) }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <label>申报期间：</label>
              <span>{{ declaration.declarationPeriod }}</span>
            </div>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="8">
            <div class="info-item">
              <label>优先级：</label>
              <el-tag :type="getPriorityTagType(declaration.priority)" size="small">
                {{ getPriorityLabel(declaration.priority) }}
              </el-tag>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <label>申报截止日期：</label>
              <span 
                :class="{
                  'text-danger': isDeclarationOverdue(declaration.deadline, declaration.declarationStatus),
                  'text-warning': isDeclarationUpcoming(declaration.deadline)
                }"
              >
                {{ formatDate(declaration.deadline) }}
              </span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <label>创建时间：</label>
              <span>{{ formatDate(declaration.createdTime) }}</span>
            </div>
          </el-col>
        </el-row>
      </el-card>

      <!-- 纳税人信息卡片 -->
      <el-card class="detail-card" header="纳税人信息">
        <el-row :gutter="20">
          <el-col :span="12">
            <div class="info-item">
              <label>纳税人识别号：</label>
              <span>{{ declaration.taxpayerId || '-' }}</span>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="info-item">
              <label>纳税人名称：</label>
              <span>{{ declaration.taxpayerName || '-' }}</span>
            </div>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <div class="info-item">
              <label>纳税人地址：</label>
              <span>{{ declaration.taxpayerAddress || '-' }}</span>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="info-item">
              <label>联系电话：</label>
              <span>{{ declaration.taxpayerPhone || '-' }}</span>
            </div>
          </el-col>
        </el-row>
      </el-card>

      <!-- 税额信息卡片 -->
      <el-card class="detail-card" header="税额信息">
        <el-row :gutter="20">
          <el-col :span="8">
            <div class="info-item">
              <label>计税依据：</label>
              <span class="amount">
                {{ declaration.taxBase ? `¥${formatTaxAmount(declaration.taxBase)}` : '-' }}
              </span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <label>税率：</label>
              <span>{{ declaration.taxRate ? `${(declaration.taxRate * 100).toFixed(2)}%` : '-' }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <label>应纳税额：</label>
              <span class="amount primary">
                {{ declaration.taxAmount ? `¥${formatTaxAmount(declaration.taxAmount)}` : '-' }}
              </span>
            </div>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="8">
            <div class="info-item">
              <label>已缴税额：</label>
              <span class="amount">
                {{ declaration.paidAmount ? `¥${formatTaxAmount(declaration.paidAmount)}` : '-' }}
              </span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <label>应补税额：</label>
              <span class="amount success">
                {{ declaration.payableAmount ? `¥${formatTaxAmount(declaration.payableAmount)}` : '-' }}
              </span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <label>应退税额：</label>
              <span class="amount warning">
                {{ declaration.refundableAmount ? `¥${formatTaxAmount(declaration.refundableAmount)}` : '-' }}
              </span>
            </div>
          </el-col>
        </el-row>
      </el-card>

      <!-- 申报进度卡片 -->
      <el-card class="detail-card" header="申报进度">
        <div class="progress-section">
          <el-steps :active="getProgressStep(declaration.declarationStatus)" finish-status="success">
            <el-step title="创建申报" :description="formatDate(declaration.createdTime)"></el-step>
            <el-step title="填报数据" :description="declaration.fillTime ? formatDate(declaration.fillTime) : '待填报'"></el-step>
            <el-step title="提交申报" :description="declaration.submitTime ? formatDate(declaration.submitTime) : '待提交'"></el-step>
            <el-step title="审核通过" :description="declaration.reviewTime ? formatDate(declaration.reviewTime) : '待审核'"></el-step>
            <el-step title="申报完成" :description="declaration.completeTime ? formatDate(declaration.completeTime) : '待完成'"></el-step>
          </el-steps>
        </div>
        
        <div class="progress-info">
          <el-row :gutter="20">
            <el-col :span="8">
              <div class="info-item">
                <label>当前进度：</label>
                <span>{{ calculateDeclarationProgress(declaration.declarationStatus) }}%</span>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="info-item">
                <label>受理回执号：</label>
                <span>{{ declaration.receiptNumber || '-' }}</span>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="info-item">
                <label>受理时间：</label>
                <span>{{ formatDate(declaration.acceptTime) }}</span>
              </div>
            </el-col>
          </el-row>
        </div>
      </el-card>

      <!-- 审核信息卡片 -->
      <el-card v-if="declaration.reviewTime" class="detail-card" header="审核信息">
        <el-row :gutter="20">
          <el-col :span="8">
            <div class="info-item">
              <label>审核时间：</label>
              <span>{{ formatDate(declaration.reviewTime) }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <label>审核人员：</label>
              <span>{{ declaration.reviewBy || '-' }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <label>审核结果：</label>
              <el-tag :type="declaration.declarationStatus === 'APPROVED' ? 'success' : 'danger'">
                {{ declaration.declarationStatus === 'APPROVED' ? '通过' : '拒绝' }}
              </el-tag>
            </div>
          </el-col>
        </el-row>
        
        <div v-if="declaration.reviewComment" class="info-item">
          <label>审核意见：</label>
          <div class="review-comment">{{ declaration.reviewComment }}</div>
        </div>
      </el-card>

      <!-- 错误信息卡片 -->
      <el-card v-if="declaration.errorMessage" class="detail-card" header="错误信息">
        <el-alert
          :title="declaration.errorMessage"
          type="error"
          :closable="false"
          show-icon
        >
          <div v-if="declaration.errorCode">
            错误代码：{{ declaration.errorCode }}
          </div>
          <div v-if="declaration.errorTime">
            错误时间：{{ formatDate(declaration.errorTime) }}
          </div>
        </el-alert>
      </el-card>

      <!-- 备注信息卡片 -->
      <el-card v-if="declaration.remark" class="detail-card" header="备注信息">
        <div class="remark-content">{{ declaration.remark }}</div>
      </el-card>

      <!-- 操作按钮 -->
      <div class="action-buttons">
        <el-button
          v-if="canEdit(declaration.declarationStatus)"
          type="primary"
          icon="el-icon-edit"
          @click="handleEdit"
        >
          编辑申报
        </el-button>
        
        <el-button
          v-if="canAutoFill(declaration.declarationStatus)"
          type="success"
          icon="el-icon-magic-stick"
          @click="handleAutoFill"
          :loading="autoFillLoading"
        >
          自动填报
        </el-button>
        
        <el-button
          v-if="canSubmit(declaration.declarationStatus)"
          type="warning"
          icon="el-icon-upload2"
          @click="handleSubmit"
          :loading="submitLoading"
        >
          提交申报
        </el-button>
        
        <el-button
          v-if="canWithdraw(declaration.declarationStatus)"
          type="info"
          icon="el-icon-download"
          @click="handleWithdraw"
          :loading="withdrawLoading"
        >
          撤回申报
        </el-button>
        
        <el-button
          type="default"
          icon="el-icon-calculator"
          @click="handleCalculateTax"
          :loading="calculateLoading"
        >
          计算税额
        </el-button>
        
        <el-button
          type="default"
          icon="el-icon-view"
          @click="handleValidateData"
          :loading="validateLoading"
        >
          验证数据
        </el-button>
        
        <el-button
          type="default"
          icon="el-icon-message"
          @click="handleSendReminder"
          :loading="reminderLoading"
        >
          发送提醒
        </el-button>
        
        <el-button
          type="default"
          icon="el-icon-refresh"
          @click="loadDeclarationDetail"
        >
          刷新
        </el-button>
        
        <el-button
          type="default"
          icon="el-icon-close"
          @click="$emit('close')"
        >
          关闭
        </el-button>
      </div>
    </div>
  </div>
</template>

<script>
import {
  getDeclarationDetail,
  autoFillDeclaration,
  submitDeclaration,
  withdrawDeclaration,
  calculateTaxAmount,
  validateDeclarationData,
  sendDeclarationReminder,
  getTaxTypeLabel,
  getDeclarationTypeLabel,
  getDeclarationStatusLabel,
  getPriorityLabel,
  formatTaxAmount,
  isDeclarationOverdue,
  isDeclarationUpcoming,
  calculateDeclarationProgress
} from '@/api/managementAccountant/ts/taxDeclaration'

export default {
  name: 'TaxDeclarationDetail',
  props: {
    declarationId: {
      type: [String, Number],
      required: true
    },
    tenantId: {
      type: [String, Number],
      required: true
    }
  },
  data() {
    return {
      loading: false,
      declaration: null,
      
      // 操作加载状态
      autoFillLoading: false,
      submitLoading: false,
      withdrawLoading: false,
      calculateLoading: false,
      validateLoading: false,
      reminderLoading: false
    }
  },
  created() {
    this.loadDeclarationDetail()
  },
  methods: {
    // 加载申报详情
    async loadDeclarationDetail() {
      this.loading = true
      try {
        const response = await getDeclarationDetail(this.tenantId, this.declarationId)
        if (response.success) {
          this.declaration = response.data
        } else {
          this.$message.error(response.message || '加载申报详情失败')
        }
      } catch (error) {
        console.error('加载申报详情失败:', error)
        this.$message.error('加载申报详情失败')
      } finally {
        this.loading = false
      }
    },
    
    // 编辑申报
    handleEdit() {
      // 实现编辑逻辑
      this.$message.info('编辑功能开发中')
    },
    
    // 自动填报
    async handleAutoFill() {
      this.autoFillLoading = true
      try {
        const response = await autoFillDeclaration(this.tenantId, this.declarationId)
        if (response.success) {
          this.$message.success('自动填报成功')
          this.loadDeclarationDetail()
          this.$emit('refresh')
        } else {
          this.$message.error(response.message || '自动填报失败')
        }
      } catch (error) {
        console.error('自动填报失败:', error)
        this.$message.error('自动填报失败')
      } finally {
        this.autoFillLoading = false
      }
    },
    
    // 提交申报
    async handleSubmit() {
      try {
        await this.$confirm('确认提交该申报吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        
        this.submitLoading = true
        const response = await submitDeclaration(this.tenantId, this.declarationId)
        if (response.success) {
          this.$message.success('提交申报成功')
          this.loadDeclarationDetail()
          this.$emit('refresh')
        } else {
          this.$message.error(response.message || '提交申报失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('提交申报失败:', error)
          this.$message.error('提交申报失败')
        }
      } finally {
        this.submitLoading = false
      }
    },
    
    // 撤回申报
    async handleWithdraw() {
      try {
        await this.$confirm('确认撤回该申报吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        
        this.withdrawLoading = true
        const response = await withdrawDeclaration(this.tenantId, this.declarationId)
        if (response.success) {
          this.$message.success('撤回申报成功')
          this.loadDeclarationDetail()
          this.$emit('refresh')
        } else {
          this.$message.error(response.message || '撤回申报失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('撤回申报失败:', error)
          this.$message.error('撤回申报失败')
        }
      } finally {
        this.withdrawLoading = false
      }
    },
    
    // 计算税额
    async handleCalculateTax() {
      this.calculateLoading = true
      try {
        const response = await calculateTaxAmount(this.tenantId, this.declarationId)
        if (response.success) {
          this.$message.success('税额计算完成')
          this.loadDeclarationDetail()
          this.$emit('refresh')
        } else {
          this.$message.error(response.message || '税额计算失败')
        }
      } catch (error) {
        console.error('税额计算失败:', error)
        this.$message.error('税额计算失败')
      } finally {
        this.calculateLoading = false
      }
    },
    
    // 验证数据
    async handleValidateData() {
      this.validateLoading = true
      try {
        const response = await validateDeclarationData(this.tenantId, this.declarationId)
        if (response.success) {
          const result = response.data
          if (result.valid) {
            this.$message.success('数据验证通过')
          } else {
            this.$message.warning(`数据验证失败：${result.message}`)
          }
        } else {
          this.$message.error(response.message || '数据验证失败')
        }
      } catch (error) {
        console.error('数据验证失败:', error)
        this.$message.error('数据验证失败')
      } finally {
        this.validateLoading = false
      }
    },
    
    // 发送提醒
    async handleSendReminder() {
      this.reminderLoading = true
      try {
        const response = await sendDeclarationReminder(this.tenantId, this.declarationId)
        if (response.success) {
          this.$message.success('提醒发送成功')
        } else {
          this.$message.error(response.message || '提醒发送失败')
        }
      } catch (error) {
        console.error('提醒发送失败:', error)
        this.$message.error('提醒发送失败')
      } finally {
        this.reminderLoading = false
      }
    },
    
    // 状态判断方法
    canEdit(status) {
      return ['DRAFT', 'FILL_FAILED'].includes(status)
    },
    
    canAutoFill(status) {
      return ['DRAFT', 'FILL_FAILED'].includes(status)
    },
    
    canSubmit(status) {
      return ['DRAFT', 'FILLED', 'SUBMIT_FAILED'].includes(status)
    },
    
    canWithdraw(status) {
      return ['SUBMITTED', 'UNDER_REVIEW'].includes(status)
    },
    
    // 获取进度步骤
    getProgressStep(status) {
      const stepMap = {
        'DRAFT': 0,
        'FILLING': 1,
        'FILLED': 2,
        'SUBMITTING': 2,
        'SUBMITTED': 3,
        'UNDER_REVIEW': 3,
        'APPROVED': 4,
        'COMPLETED': 4,
        'REJECTED': 3,
        'WITHDRAWN': 2,
        'FILL_FAILED': 1,
        'SUBMIT_FAILED': 2
      }
      return stepMap[status] || 0
    },
    
    // 获取状态标签类型
    getStatusTagType(status) {
      const typeMap = {
        'DRAFT': '',
        'FILLING': 'warning',
        'FILLED': 'info',
        'SUBMITTING': 'warning',
        'SUBMITTED': 'info',
        'UNDER_REVIEW': 'warning',
        'APPROVED': 'success',
        'REJECTED': 'danger',
        'COMPLETED': 'success',
        'WITHDRAWN': '',
        'FILL_FAILED': 'danger',
        'SUBMIT_FAILED': 'danger'
      }
      return typeMap[status] || ''
    },
    
    // 获取优先级标签类型
    getPriorityTagType(priority) {
      const typeMap = {
        'LOW': 'info',
        'NORMAL': '',
        'HIGH': 'warning',
        'URGENT': 'danger'
      }
      return typeMap[priority] || ''
    },
    
    // 工具方法
    getTaxTypeLabel,
    getDeclarationTypeLabel,
    getDeclarationStatusLabel,
    getPriorityLabel,
    formatTaxAmount,
    isDeclarationOverdue,
    isDeclarationUpcoming,
    calculateDeclarationProgress,
    
    formatDate(date) {
      if (!date) return '-'
      return new Date(date).toLocaleString('zh-CN')
    }
  }
}
</script>

<style lang="scss" scoped>
.tax-declaration-detail {
  .detail-card {
    margin-bottom: 20px;
    
    .info-item {
      margin-bottom: 12px;
      
      label {
        font-weight: 500;
        color: #606266;
        margin-right: 8px;
        min-width: 100px;
        display: inline-block;
      }
      
      .amount {
        font-weight: 600;
        font-size: 16px;
        
        &.primary {
          color: #409EFF;
        }
        
        &.success {
          color: #67C23A;
        }
        
        &.warning {
          color: #E6A23C;
        }
      }
    }
    
    .review-comment {
      background: #f5f7fa;
      padding: 12px;
      border-radius: 4px;
      margin-top: 8px;
      color: #606266;
      line-height: 1.6;
    }
    
    .remark-content {
      background: #f5f7fa;
      padding: 12px;
      border-radius: 4px;
      color: #606266;
      line-height: 1.6;
    }
  }
  
  .progress-section {
    margin-bottom: 20px;
  }
  
  .progress-info {
    padding-top: 20px;
    border-top: 1px solid #EBEEF5;
  }
  
  .action-buttons {
    text-align: center;
    padding: 20px 0;
    border-top: 1px solid #EBEEF5;
    
    .el-button {
      margin: 0 8px 8px 0;
    }
  }
  
  .text-danger {
    color: #F56C6C;
  }
  
  .text-warning {
    color: #E6A23C;
  }
}
</style>
