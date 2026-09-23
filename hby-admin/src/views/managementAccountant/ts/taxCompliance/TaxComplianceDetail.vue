<template>
  <div class="tax-compliance-detail" v-loading="loading">
    <div v-if="complianceData" class="detail-container">
      <!-- 头部信息 -->
      <div class="detail-header">
        <div class="header-left">
          <h3 class="compliance-name">{{ complianceData.complianceName }}</h3>
          <div class="compliance-meta">
            <span class="compliance-code">{{ complianceData.complianceCode }}</span>
            <el-tag :type="getCheckStatusColor(complianceData.checkStatus)" size="small">
              {{ formatCheckStatus(complianceData.checkStatus) }}
            </el-tag>
            <el-tag :type="getComplianceStatusColor(complianceData.complianceStatus)" size="small">
              {{ formatComplianceStatus(complianceData.complianceStatus) }}
            </el-tag>
            <el-tag :type="getRiskLevelColor(complianceData.riskLevel)" size="small">
              {{ formatRiskLevel(complianceData.riskLevel) }}
            </el-tag>
          </div>
        </div>
        <div class="header-right">
          <el-button-group>
            <el-button
              v-if="complianceData.checkStatus === 'DRAFT'"
              type="primary"
              icon="el-icon-video-play"
              @click="handleStart"
            >
              启动检查
            </el-button>
            <el-button
              v-if="complianceData.checkStatus === 'IN_PROGRESS'"
              type="warning"
              icon="el-icon-video-pause"
              @click="handlePause"
            >
              暂停检查
            </el-button>
            <el-button
              v-if="complianceData.checkStatus === 'PAUSED'"
              type="primary"
              icon="el-icon-video-play"
              @click="handleResume"
            >
              恢复检查
            </el-button>
            <el-button
              v-if="['IN_PROGRESS', 'PAUSED'].includes(complianceData.checkStatus)"
              type="success"
              icon="el-icon-circle-check"
              @click="handleComplete"
            >
              完成检查
            </el-button>
          </el-button-group>
          <el-button icon="el-icon-edit" @click="handleEdit">编辑</el-button>
          <el-button icon="el-icon-refresh" @click="refreshData">刷新</el-button>
        </div>
      </div>

      <!-- 关键指标卡片 -->
      <div class="metrics-cards">
        <el-row :gutter="20">
          <el-col :span="6">
            <div class="metric-card">
              <div class="metric-icon compliance-score">
                <i class="el-icon-trophy"></i>
              </div>
              <div class="metric-content">
                <div class="metric-value">{{ complianceData.complianceScore || '-' }}</div>
                <div class="metric-label">合规评分</div>
              </div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="metric-card">
              <div class="metric-icon risk-score">
                <i class="el-icon-warning"></i>
              </div>
              <div class="metric-content">
                <div class="metric-value">{{ complianceData.riskScore || '-' }}</div>
                <div class="metric-label">风险评分</div>
              </div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="metric-card">
              <div class="metric-icon check-progress">
                <i class="el-icon-pie-chart"></i>
              </div>
              <div class="metric-content">
                <div class="metric-value">{{ formatProgress(complianceData.checkProgress) }}</div>
                <div class="metric-label">检查进度</div>
              </div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="metric-card">
              <div class="metric-icon issue-count">
                <i class="el-icon-warning-outline"></i>
              </div>
              <div class="metric-content">
                <div class="metric-value">{{ complianceData.issueCount || 0 }}</div>
                <div class="metric-label">问题数量</div>
              </div>
            </div>
          </el-col>
        </el-row>
      </div>

      <!-- 详细信息标签页 -->
      <div class="detail-tabs">
        <el-tabs v-model="activeTab" type="border-card">
          <!-- 基本信息 -->
          <el-tab-pane label="基本信息" name="basic">
            <div class="info-section">
              <el-row :gutter="20">
                <el-col :span="12">
                  <div class="info-group">
                    <h4>检查信息</h4>
                    <div class="info-item">
                      <label>检查编号：</label>
                      <span>{{ complianceData.complianceCode }}</span>
                    </div>
                    <div class="info-item">
                      <label>检查名称：</label>
                      <span>{{ complianceData.complianceName }}</span>
                    </div>
                    <div class="info-item">
                      <label>检查类型：</label>
                      <span>{{ formatComplianceType(complianceData.complianceType) }}</span>
                    </div>
                    <div class="info-item">
                      <label>检查范围：</label>
                      <span>{{ complianceData.checkScope || '-' }}</span>
                    </div>
                    <div class="info-item">
                      <label>检查对象：</label>
                      <span>{{ complianceData.checkObject || '-' }}</span>
                    </div>
                    <div class="info-item">
                      <label>检查方法：</label>
                      <span>{{ complianceData.checkMethod || '-' }}</span>
                    </div>
                    <div class="info-item">
                      <label>检查频率：</label>
                      <span>{{ complianceData.checkFrequency || '-' }}</span>
                    </div>
                  </div>
                </el-col>
                <el-col :span="12">
                  <div class="info-group">
                    <h4>状态信息</h4>
                    <div class="info-item">
                      <label>检查状态：</label>
                      <el-tag :type="getCheckStatusColor(complianceData.checkStatus)" size="small">
                        {{ formatCheckStatus(complianceData.checkStatus) }}
                      </el-tag>
                    </div>
                    <div class="info-item">
                      <label>合规状态：</label>
                      <el-tag :type="getComplianceStatusColor(complianceData.complianceStatus)" size="small">
                        {{ formatComplianceStatus(complianceData.complianceStatus) }}
                      </el-tag>
                    </div>
                    <div class="info-item">
                      <label>风险等级：</label>
                      <el-tag :type="getRiskLevelColor(complianceData.riskLevel)" size="small">
                        {{ formatRiskLevel(complianceData.riskLevel) }}
                      </el-tag>
                    </div>
                    <div class="info-item">
                      <label>优先级：</label>
                      <span>{{ formatPriority(complianceData.priority) }}</span>
                    </div>
                    <div class="info-item">
                      <label>检查进度：</label>
                      <el-progress
                        :percentage="Math.round(complianceData.checkProgress || 0)"
                        :stroke-width="8"
                        style="width: 200px;"
                      />
                    </div>
                    <div class="info-item">
                      <label>整改进度：</label>
                      <el-progress
                        :percentage="Math.round(complianceData.rectificationProgress || 0)"
                        :stroke-width="8"
                        status="success"
                        style="width: 200px;"
                      />
                    </div>
                  </div>
                </el-col>
              </el-row>
              <el-row :gutter="20" style="margin-top: 20px;">
                <el-col :span="12">
                  <div class="info-group">
                    <h4>人员信息</h4>
                    <div class="info-item">
                      <label>检查人员：</label>
                      <span>{{ complianceData.checker || '-' }}</span>
                    </div>
                    <div class="info-item">
                      <label>检查部门：</label>
                      <span>{{ complianceData.checkDepartment || '-' }}</span>
                    </div>
                    <div class="info-item">
                      <label>审核人员：</label>
                      <span>{{ complianceData.reviewer || '-' }}</span>
                    </div>
                    <div class="info-item">
                      <label>整改负责人：</label>
                      <span>{{ complianceData.rectificationResponsible || '-' }}</span>
                    </div>
                  </div>
                </el-col>
                <el-col :span="12">
                  <div class="info-group">
                    <h4>时间信息</h4>
                    <div class="info-item">
                      <label>开始时间：</label>
                      <span>{{ formatDate(complianceData.startTime) }}</span>
                    </div>
                    <div class="info-item">
                      <label>结束时间：</label>
                      <span>{{ formatDate(complianceData.endTime) }}</span>
                    </div>
                    <div class="info-item">
                      <label>实际检查时间：</label>
                      <span>{{ formatDate(complianceData.actualCheckTime) }}</span>
                    </div>
                    <div class="info-item">
                      <label>完成时间：</label>
                      <span>{{ formatDate(complianceData.completionTime) }}</span>
                    </div>
                    <div class="info-item">
                      <label>整改截止时间：</label>
                      <span>{{ formatDate(complianceData.rectificationDeadline) }}</span>
                    </div>
                    <div class="info-item">
                      <label>创建时间：</label>
                      <span>{{ formatDate(complianceData.createdTime) }}</span>
                    </div>
                  </div>
                </el-col>
              </el-row>
            </div>
          </el-tab-pane>

          <!-- 检查结果 -->
          <el-tab-pane label="检查结果" name="result">
            <div class="result-section">
              <el-row :gutter="20">
                <el-col :span="12">
                  <el-card class="result-card">
                    <div slot="header">
                      <span>检查结果概览</span>
                    </div>
                    <div class="result-overview">
                      <div class="result-item">
                        <label>合规评分：</label>
                        <div class="score-display">
                          <span class="score-value">{{ complianceData.complianceScore || '-' }}</span>
                          <span class="score-level">{{ getComplianceScoreLevel(complianceData.complianceScore) }}</span>
                        </div>
                      </div>
                      <div class="result-item">
                        <label>风险评分：</label>
                        <div class="score-display">
                          <span class="score-value">{{ complianceData.riskScore || '-' }}</span>
                          <el-tag :type="getRiskLevelColor(complianceData.riskLevel)" size="mini">
                            {{ formatRiskLevel(complianceData.riskLevel) }}
                          </el-tag>
                        </div>
                      </div>
                      <div class="result-item">
                        <label>问题统计：</label>
                        <div class="issue-stats">
                          <span class="issue-total">总计：{{ complianceData.issueCount || 0 }}个</span>
                          <span class="issue-critical">严重：{{ complianceData.criticalIssueCount || 0 }}个</span>
                          <span class="issue-normal">一般：{{ complianceData.normalIssueCount || 0 }}个</span>
                          <span class="issue-minor">轻微：{{ complianceData.minorIssueCount || 0 }}个</span>
                        </div>
                      </div>
                    </div>
                  </el-card>
                </el-col>
                <el-col :span="12">
                  <el-card class="result-card">
                    <div slot="header">
                      <span>检查详情</span>
                    </div>
                    <div class="result-details">
                      <div class="detail-item">
                        <label>检查结果：</label>
                        <div class="detail-content">
                          {{ complianceData.checkResult || '暂无检查结果' }}
                        </div>
                      </div>
                      <div class="detail-item">
                        <label>问题描述：</label>
                        <div class="detail-content">
                          {{ complianceData.issueDescription || '暂无问题描述' }}
                        </div>
                      </div>
                      <div class="detail-item">
                        <label>整改建议：</label>
                        <div class="detail-content">
                          {{ complianceData.rectificationSuggestion || '暂无整改建议' }}
                        </div>
                      </div>
                    </div>
                  </el-card>
                </el-col>
              </el-row>
            </div>
          </el-tab-pane>

          <!-- 整改跟踪 -->
          <el-tab-pane label="整改跟踪" name="rectification">
            <div class="rectification-section">
              <el-row :gutter="20">
                <el-col :span="12">
                  <el-card class="rectification-card">
                    <div slot="header">
                      <span>整改信息</span>
                      <el-button
                        v-if="complianceData.rectificationStatus !== 'COMPLETED'"
                        type="text"
                        style="float: right; padding: 3px 0"
                        @click="handleCreateRectificationPlan"
                      >
                        创建整改计划
                      </el-button>
                    </div>
                    <div class="rectification-info">
                      <div class="info-item">
                        <label>整改状态：</label>
                        <el-tag :type="getRectificationStatusColor(complianceData.rectificationStatus)" size="small">
                          {{ formatRectificationStatus(complianceData.rectificationStatus) }}
                        </el-tag>
                      </div>
                      <div class="info-item">
                        <label>整改负责人：</label>
                        <span>{{ complianceData.rectificationResponsible || '-' }}</span>
                      </div>
                      <div class="info-item">
                        <label>整改截止时间：</label>
                        <span>{{ formatDate(complianceData.rectificationDeadline) }}</span>
                      </div>
                      <div class="info-item">
                        <label>整改进度：</label>
                        <el-progress
                          :percentage="Math.round(complianceData.rectificationProgress || 0)"
                          :stroke-width="8"
                          status="success"
                          style="width: 200px;"
                        />
                      </div>
                      <div class="info-item">
                        <label>整改完成时间：</label>
                        <span>{{ formatDate(complianceData.rectificationCompletionTime) }}</span>
                      </div>
                    </div>
                  </el-card>
                </el-col>
                <el-col :span="12">
                  <el-card class="rectification-card">
                    <div slot="header">
                      <span>复查信息</span>
                      <el-button
                        v-if="complianceData.rectificationStatus === 'COMPLETED' && complianceData.recheckStatus !== 'COMPLETED'"
                        type="text"
                        style="float: right; padding: 3px 0"
                        @click="handleRequestRecheck"
                      >
                        申请复查
                      </el-button>
                    </div>
                    <div class="recheck-info">
                      <div class="info-item">
                        <label>复查状态：</label>
                        <el-tag :type="getRecheckStatusColor(complianceData.recheckStatus)" size="small">
                          {{ formatRecheckStatus(complianceData.recheckStatus) }}
                        </el-tag>
                      </div>
                      <div class="info-item">
                        <label>复查时间：</label>
                        <span>{{ formatDate(complianceData.recheckTime) }}</span>
                      </div>
                      <div class="info-item">
                        <label>复查结果：</label>
                        <div class="detail-content">
                          {{ complianceData.recheckResult || '暂无复查结果' }}
                        </div>
                      </div>
                    </div>
                  </el-card>
                </el-col>
              </el-row>
            </div>
          </el-tab-pane>

          <!-- 相关文档 -->
          <el-tab-pane label="相关文档" name="documents">
            <div class="documents-section">
              <el-row :gutter="20">
                <el-col :span="12">
                  <el-card class="document-card">
                    <div slot="header">
                      <span>检查报告</span>
                      <el-button
                        type="text"
                        style="float: right; padding: 3px 0"
                        @click="handleGenerateReport"
                      >
                        生成报告
                      </el-button>
                    </div>
                    <div class="document-list">
                      <div v-if="complianceData.reportPath" class="document-item">
                        <i class="el-icon-document"></i>
                        <span class="document-name">合规检查报告.pdf</span>
                        <div class="document-actions">
                          <el-button type="text" size="mini">下载</el-button>
                          <el-button type="text" size="mini">预览</el-button>
                        </div>
                      </div>
                      <div v-else class="no-documents">
                        暂无检查报告
                      </div>
                    </div>
                  </el-card>
                </el-col>
                <el-col :span="12">
                  <el-card class="document-card">
                    <div slot="header">
                      <span>相关附件</span>
                      <el-button
                        type="text"
                        style="float: right; padding: 3px 0"
                        @click="handleUploadDocument"
                      >
                        上传附件
                      </el-button>
                    </div>
                    <div class="document-list">
                      <div class="no-documents">
                        暂无相关附件
                      </div>
                    </div>
                  </el-card>
                </el-col>
              </el-row>
            </div>
          </el-tab-pane>
        </el-tabs>
      </div>
    </div>

    <!-- 操作按钮 -->
    <div class="detail-footer">
      <el-button @click="handleClose">关闭</el-button>
      <el-button type="primary" @click="handleEdit">编辑</el-button>
    </div>
  </div>
</template>

<script>
import {
  getComplianceDetail,
  startComplianceCheck,
  pauseComplianceCheck,
  resumeComplianceCheck,
  completeComplianceCheck,
  createRectificationPlan,
  requestRecheck,
  generateComplianceReport,
  formatCheckStatus,
  formatComplianceStatus,
  formatRiskLevel,
  formatPriority,
  formatComplianceType,
  formatRectificationStatus,
  getCheckStatusColor,
  getComplianceStatusColor,
  getRiskLevelColor,
  getComplianceScoreLevel,
  formatProgress
} from '@/api/managementAccountant/ts/taxCompliance'

export default {
  name: 'TaxComplianceDetail',
  props: {
    complianceId: {
      type: [String, Number],
      required: true
    }
  },
  data() {
    return {
      loading: false,
      activeTab: 'basic',
      complianceData: null
    }
  },
  created() {
    this.loadData()
  },
  methods: {
    async loadData() {
      this.loading = true
      try {
        const response = await getComplianceDetail(this.complianceId)
        if (response.success) {
          this.complianceData = response.data
        } else {
          this.$message.error(response.message || '加载数据失败')
        }
      } catch (error) {
        console.error('加载数据失败:', error)
        this.$message.error('加载数据失败')
      } finally {
        this.loading = false
      }
    },

    async handleStart() {
      try {
        const response = await startComplianceCheck(this.complianceId)
        if (response.success) {
          this.$message.success('启动检查成功')
          this.loadData()
          this.$emit('refresh')
        } else {
          this.$message.error(response.message || '启动检查失败')
        }
      } catch (error) {
        console.error('启动检查失败:', error)
        this.$message.error('启动检查失败')
      }
    },

    async handlePause() {
      try {
        const response = await pauseComplianceCheck(this.complianceId)
        if (response.success) {
          this.$message.success('暂停检查成功')
          this.loadData()
          this.$emit('refresh')
        } else {
          this.$message.error(response.message || '暂停检查失败')
        }
      } catch (error) {
        console.error('暂停检查失败:', error)
        this.$message.error('暂停检查失败')
      }
    },

    async handleResume() {
      try {
        const response = await resumeComplianceCheck(this.complianceId)
        if (response.success) {
          this.$message.success('恢复检查成功')
          this.loadData()
          this.$emit('refresh')
        } else {
          this.$message.error(response.message || '恢复检查失败')
        }
      } catch (error) {
        console.error('恢复检查失败:', error)
        this.$message.error('恢复检查失败')
      }
    },

    async handleComplete() {
      try {
        const response = await completeComplianceCheck(this.complianceId, {})
        if (response.success) {
          this.$message.success('完成检查成功')
          this.loadData()
          this.$emit('refresh')
        } else {
          this.$message.error(response.message || '完成检查失败')
        }
      } catch (error) {
        console.error('完成检查失败:', error)
        this.$message.error('完成检查失败')
      }
    },

    async handleCreateRectificationPlan() {
      try {
        const planData = {
          rectificationResponsible: '系统管理员',
          rectificationDeadline: new Date(Date.now() + 30 * 24 * 60 * 60 * 1000), // 30天后
          rectificationSuggestion: '请根据检查结果制定详细的整改计划'
        }
        
        const response = await createRectificationPlan(this.complianceId, planData)
        if (response.success) {
          this.$message.success('创建整改计划成功')
          this.loadData()
          this.$emit('refresh')
        } else {
          this.$message.error(response.message || '创建整改计划失败')
        }
      } catch (error) {
        console.error('创建整改计划失败:', error)
        this.$message.error('创建整改计划失败')
      }
    },

    async handleRequestRecheck() {
      try {
        const response = await requestRecheck(this.complianceId)
        if (response.success) {
          this.$message.success('申请复查成功')
          this.loadData()
          this.$emit('refresh')
        } else {
          this.$message.error(response.message || '申请复查失败')
        }
      } catch (error) {
        console.error('申请复查失败:', error)
        this.$message.error('申请复查失败')
      }
    },

    async handleGenerateReport() {
      try {
        const response = await generateComplianceReport(this.complianceId)
        if (response.success) {
          this.$message.success('生成报告成功')
          this.loadData()
        } else {
          this.$message.error(response.message || '生成报告失败')
        }
      } catch (error) {
        console.error('生成报告失败:', error)
        this.$message.error('生成报告失败')
      }
    },

    handleUploadDocument() {
      this.$message.info('上传附件功能开发中...')
    },

    handleEdit() {
      this.$emit('edit', this.complianceData)
    },

    handleClose() {
      this.$emit('close')
    },

    refreshData() {
      this.loadData()
    },

    formatDate(date) {
      if (!date) return '-'
      return this.$moment(date).format('YYYY-MM-DD HH:mm')
    },

    getRectificationStatusColor(status) {
      const colorMap = {
        'NOT_REQUIRED': 'info',
        'PLANNED': 'warning',
        'IN_PROGRESS': 'primary',
        'COMPLETED': 'success',
        'OVERDUE': 'danger'
      }
      return colorMap[status] || 'info'
    },

    formatRectificationStatus(status) {
      const statusMap = {
        'NOT_REQUIRED': '无需整改',
        'PLANNED': '计划整改',
        'IN_PROGRESS': '整改中',
        'COMPLETED': '已完成',
        'OVERDUE': '逾期'
      }
      return statusMap[status] || status
    },

    getRecheckStatusColor(status) {
      const colorMap = {
        'NOT_REQUIRED': 'info',
        'REQUESTED': 'warning',
        'IN_PROGRESS': 'primary',
        'COMPLETED': 'success'
      }
      return colorMap[status] || 'info'
    },

    formatRecheckStatus(status) {
      const statusMap = {
        'NOT_REQUIRED': '无需复查',
        'REQUESTED': '已申请',
        'IN_PROGRESS': '复查中',
        'COMPLETED': '已完成'
      }
      return statusMap[status] || status
    },

    // 导入格式化函数
    formatCheckStatus,
    formatComplianceStatus,
    formatRiskLevel,
    formatPriority,
    formatComplianceType,
    formatRectificationStatus,
    getCheckStatusColor,
    getComplianceStatusColor,
    getRiskLevelColor,
    getComplianceScoreLevel,
    formatProgress
  }
}
</script>

<style lang="scss" scoped>
.tax-compliance-detail {
  .detail-container {
    .detail-header {
      display: flex;
      justify-content: space-between;
      align-items: flex-start;
      padding: 20px;
      background: white;
      border-radius: 8px;
      margin-bottom: 20px;
      box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);

      .header-left {
        .compliance-name {
          margin: 0 0 8px 0;
          font-size: 20px;
          font-weight: 600;
          color: #303133;
        }

        .compliance-meta {
          display: flex;
          align-items: center;
          gap: 8px;

          .compliance-code {
            background: #f5f7fa;
            padding: 4px 8px;
            border-radius: 4px;
            font-size: 12px;
            color: #909399;
          }
        }
      }

      .header-right {
        display: flex;
        align-items: center;
        gap: 8px;
      }
    }

    .metrics-cards {
      margin-bottom: 20px;

      .metric-card {
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

        .metric-icon {
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

          &.compliance-score {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
          }

          &.risk-score {
            background: linear-gradient(135deg, #fa709a 0%, #fee140 100%);
          }

          &.check-progress {
            background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
          }

          &.issue-count {
            background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
          }
        }

        .metric-content {
          flex: 1;

          .metric-value {
            font-size: 28px;
            font-weight: 700;
            color: #303133;
            line-height: 1;
            margin-bottom: 4px;
          }

          .metric-label {
            font-size: 14px;
            color: #909399;
          }
        }
      }
    }

    .detail-tabs {
      background: white;
      border-radius: 8px;
      box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
      overflow: hidden;

      .info-section {
        .info-group {
          h4 {
            margin: 0 0 16px 0;
            font-size: 16px;
            font-weight: 600;
            color: #303133;
            border-bottom: 2px solid #409eff;
            padding-bottom: 8px;
          }

          .info-item {
            display: flex;
            align-items: flex-start;
            margin-bottom: 12px;
            font-size: 14px;

            label {
              width: 120px;
              color: #909399;
              flex-shrink: 0;
            }

            span {
              color: #303133;
              flex: 1;
            }
          }
        }
      }

      .result-section {
        .result-card {
          .result-overview {
            .result-item {
              display: flex;
              align-items: center;
              margin-bottom: 16px;

              label {
                width: 100px;
                color: #909399;
                font-size: 14px;
              }

              .score-display {
                display: flex;
                align-items: center;
                gap: 8px;

                .score-value {
                  font-size: 24px;
                  font-weight: 600;
                  color: #409eff;
                }

                .score-level {
                  font-size: 12px;
                  color: #909399;
                }
              }

              .issue-stats {
                display: flex;
                gap: 12px;
                font-size: 12px;

                .issue-total {
                  color: #303133;
                  font-weight: 600;
                }

                .issue-critical {
                  color: #f56c6c;
                }

                .issue-normal {
                  color: #e6a23c;
                }

                .issue-minor {
                  color: #67c23a;
                }
              }
            }
          }

          .result-details {
            .detail-item {
              margin-bottom: 16px;

              label {
                display: block;
                color: #909399;
                font-size: 14px;
                margin-bottom: 4px;
              }

              .detail-content {
                color: #303133;
                font-size: 14px;
                line-height: 1.6;
                background: #f5f7fa;
                padding: 8px 12px;
                border-radius: 4px;
                min-height: 60px;
              }
            }
          }
        }
      }

      .rectification-section {
        .rectification-card {
          .rectification-info,
          .recheck-info {
            .info-item {
              display: flex;
              align-items: flex-start;
              margin-bottom: 12px;
              font-size: 14px;

              label {
                width: 120px;
                color: #909399;
                flex-shrink: 0;
              }

              span {
                color: #303133;
                flex: 1;
              }

              .detail-content {
                color: #303133;
                font-size: 14px;
                line-height: 1.6;
                background: #f5f7fa;
                padding: 8px 12px;
                border-radius: 4px;
                flex: 1;
              }
            }
          }
        }
      }

      .documents-section {
        .document-card {
          .document-list {
            .document-item {
              display: flex;
              align-items: center;
              padding: 12px 0;
              border-bottom: 1px solid #f0f0f0;

              &:last-child {
                border-bottom: none;
              }

              i {
                font-size: 20px;
                color: #409eff;
                margin-right: 8px;
              }

              .document-name {
                flex: 1;
                color: #303133;
                font-size: 14px;
              }

              .document-actions {
                .el-button {
                  margin-left: 8px;
                }
              }
            }

            .no-documents {
              text-align: center;
              color: #c0c4cc;
              font-size: 14px;
              padding: 40px 0;
            }
          }
        }
      }
    }
  }

  .detail-footer {
    text-align: right;
    padding: 20px;
    background: white;
    border-radius: 8px;
    margin-top: 20px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);

    .el-button {
      margin-left: 10px;
    }
  }
}

// 响应式设计
@media (max-width: 768px) {
  .tax-compliance-detail {
    .detail-container {
      .detail-header {
        flex-direction: column;
        align-items: stretch;
        gap: 16px;
      }

      .metrics-cards {
        .el-col {
          margin-bottom: 16px;
        }
      }

      .detail-tabs {
        .info-section {
          .info-group {
            .info-item {
              flex-direction: column;
              align-items: flex-start;

              label {
                width: auto;
                margin-bottom: 4px;
              }
            }
          }
        }

        .result-section {
          .result-card {
            .result-overview {
              .result-item {
                flex-direction: column;
                align-items: flex-start;

                label {
                  width: auto;
                  margin-bottom: 4px;
                }
              }
            }
          }
        }

        .rectification-section {
          .rectification-card {
            .rectification-info,
            .recheck-info {
              .info-item {
                flex-direction: column;
                align-items: flex-start;

                label {
                  width: auto;
                  margin-bottom: 4px;
                }
              }
            }
          }
        }
      }
    }
  }
}
</style>
