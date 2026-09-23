<template>
  <el-dialog
    title="风险评估详情"
    :visible.sync="dialogVisible"
    width="1200px"
    :close-on-click-modal="false"
    top="5vh"
  >
    <div v-loading="loading" class="assessment-detail">
      <el-tabs v-model="activeTab" type="border-card">
        <!-- 基本信息 -->
        <el-tab-pane label="基本信息" name="basic">
          <div class="detail-section">
            <el-row :gutter="20">
              <el-col :span="12">
                <div class="detail-item">
                  <label>企业名称：</label>
                  <span>{{ assessmentData.enterpriseName || '-' }}</span>
                </div>
              </el-col>
              <el-col :span="12">
                <div class="detail-item">
                  <label>评估类型：</label>
                  <el-tag :type="getAssessmentTypeTagType(assessmentData.assessmentType)">
                    {{ getAssessmentTypeLabel(assessmentData.assessmentType) }}
                  </el-tag>
                </div>
              </el-col>
            </el-row>
            
            <el-row :gutter="20">
              <el-col :span="12">
                <div class="detail-item">
                  <label>评估年度：</label>
                  <span>{{ assessmentData.assessmentYear || '-' }}</span>
                </div>
              </el-col>
              <el-col :span="12">
                <div class="detail-item">
                  <label>评估季度：</label>
                  <span>{{ assessmentData.assessmentQuarter || '-' }}</span>
                </div>
              </el-col>
            </el-row>

            <el-row :gutter="20">
              <el-col :span="12">
                <div class="detail-item">
                  <label>评估日期：</label>
                  <span>{{ assessmentData.assessmentDate || '-' }}</span>
                </div>
              </el-col>
              <el-col :span="12">
                <div class="detail-item">
                  <label>评估方法：</label>
                  <span>{{ getAssessmentMethodLabel(assessmentData.assessmentMethod) }}</span>
                </div>
              </el-col>
            </el-row>

            <div class="detail-item">
              <label>评估目标：</label>
              <p>{{ assessmentData.assessmentObjective || '-' }}</p>
            </div>

            <div class="detail-item">
              <label>评估范围：</label>
              <p>{{ assessmentData.assessmentScope || '-' }}</p>
            </div>
          </div>
        </el-tab-pane>

        <!-- 风险评分 -->
        <el-tab-pane label="风险评分" name="scores">
          <div class="detail-section">
            <!-- 综合风险评分卡片 -->
            <div class="risk-score-card overall-score">
              <div class="score-header">
                <h3>综合风险评分</h3>
                <div class="score-value" :class="getRiskScoreClass(assessmentData.overallRiskScore)">
                  {{ assessmentData.overallRiskScore || '-' }}
                </div>
              </div>
              <div class="score-level">
                <el-tag :type="getRiskLevelTagType(assessmentData.overallRiskLevel)" size="large">
                  {{ getRiskLevelLabel(assessmentData.overallRiskLevel) }}
                </el-tag>
              </div>
            </div>

            <!-- 各维度风险评分 -->
            <el-row :gutter="20" class="score-dimensions">
              <el-col :span="8">
                <div class="dimension-card">
                  <div class="dimension-header">
                    <i class="el-icon-money"></i>
                    <span>财务风险</span>
                  </div>
                  <div class="dimension-score" :class="getRiskScoreClass(assessmentData.financialRiskScore)">
                    {{ assessmentData.financialRiskScore || '-' }}
                  </div>
                  <div class="dimension-progress">
                    <el-progress
                      :percentage="assessmentData.financialRiskScore || 0"
                      :color="getProgressColor(assessmentData.financialRiskScore)"
                      :show-text="false"
                    />
                  </div>
                </div>
              </el-col>
              
              <el-col :span="8">
                <div class="dimension-card">
                  <div class="dimension-header">
                    <i class="el-icon-office-building"></i>
                    <span>经营风险</span>
                  </div>
                  <div class="dimension-score" :class="getRiskScoreClass(assessmentData.operationalRiskScore)">
                    {{ assessmentData.operationalRiskScore || '-' }}
                  </div>
                  <div class="dimension-progress">
                    <el-progress
                      :percentage="assessmentData.operationalRiskScore || 0"
                      :color="getProgressColor(assessmentData.operationalRiskScore)"
                      :show-text="false"
                    />
                  </div>
                </div>
              </el-col>

              <el-col :span="8">
                <div class="dimension-card">
                  <div class="dimension-header">
                    <i class="el-icon-document-checked"></i>
                    <span>合规风险</span>
                  </div>
                  <div class="dimension-score" :class="getRiskScoreClass(assessmentData.complianceRiskScore)">
                    {{ assessmentData.complianceRiskScore || '-' }}
                  </div>
                  <div class="dimension-progress">
                    <el-progress
                      :percentage="assessmentData.complianceRiskScore || 0"
                      :color="getProgressColor(assessmentData.complianceRiskScore)"
                      :show-text="false"
                    />
                  </div>
                </div>
              </el-col>
            </el-row>

            <el-row :gutter="20" class="score-dimensions">
              <el-col :span="8">
                <div class="dimension-card">
                  <div class="dimension-header">
                    <i class="el-icon-user"></i>
                    <span>治理风险</span>
                  </div>
                  <div class="dimension-score" :class="getRiskScoreClass(assessmentData.governanceRiskScore)">
                    {{ assessmentData.governanceRiskScore || '-' }}
                  </div>
                  <div class="dimension-progress">
                    <el-progress
                      :percentage="assessmentData.governanceRiskScore || 0"
                      :color="getProgressColor(assessmentData.governanceRiskScore)"
                      :show-text="false"
                    />
                  </div>
                </div>
              </el-col>

              <el-col :span="8">
                <div class="dimension-card">
                  <div class="dimension-header">
                    <i class="el-icon-globe"></i>
                    <span>外部风险</span>
                  </div>
                  <div class="dimension-score" :class="getRiskScoreClass(assessmentData.externalRiskScore)">
                    {{ assessmentData.externalRiskScore || '-' }}
                  </div>
                  <div class="dimension-progress">
                    <el-progress
                      :percentage="assessmentData.externalRiskScore || 0"
                      :color="getProgressColor(assessmentData.externalRiskScore)"
                      :show-text="false"
                    />
                  </div>
                </div>
              </el-col>
            </el-row>
          </div>
        </el-tab-pane>

        <!-- 预警信息 -->
        <el-tab-pane label="预警信息" name="warning">
          <div class="detail-section">
            <el-row :gutter="20">
              <el-col :span="12">
                <div class="warning-card" :class="{ 'warning-triggered': assessmentData.isWarningTriggered }">
                  <div class="warning-header">
                    <i :class="assessmentData.isWarningTriggered ? 'el-icon-warning' : 'el-icon-success'"></i>
                    <span>预警状态</span>
                  </div>
                  <div class="warning-status">
                    <el-tag :type="assessmentData.isWarningTriggered ? 'danger' : 'success'" size="large">
                      {{ assessmentData.isWarningTriggered ? '已触发预警' : '正常' }}
                    </el-tag>
                  </div>
                  <div v-if="assessmentData.isWarningTriggered" class="warning-level">
                    <label>预警等级：</label>
                    <el-tag :type="getWarningLevelTagType(assessmentData.warningLevel)">
                      {{ getWarningLevelLabel(assessmentData.warningLevel) }}
                    </el-tag>
                  </div>
                </div>
              </el-col>
              
              <el-col :span="12">
                <div class="threshold-card">
                  <div class="threshold-item">
                    <label>预警阈值：</label>
                    <span class="threshold-value warning">{{ assessmentData.warningThreshold || '-' }}</span>
                  </div>
                  <div class="threshold-item">
                    <label>危险阈值：</label>
                    <span class="threshold-value danger">{{ assessmentData.dangerThreshold || '-' }}</span>
                  </div>
                  <div class="threshold-item">
                    <label>当前评分：</label>
                    <span class="threshold-value" :class="getRiskScoreClass(assessmentData.overallRiskScore)">
                      {{ assessmentData.overallRiskScore || '-' }}
                    </span>
                  </div>
                </div>
              </el-col>
            </el-row>

            <div v-if="assessmentData.warningMessage" class="warning-message">
              <label>预警信息：</label>
              <el-alert
                :title="assessmentData.warningMessage"
                :type="getWarningLevelTagType(assessmentData.warningLevel)"
                :closable="false"
                show-icon
              />
            </div>
          </div>
        </el-tab-pane>

        <!-- 风险因素 -->
        <el-tab-pane label="风险因素" name="factors">
          <div class="detail-section">
            <div class="risk-factors">
              <div class="factors-list">
                <label>主要风险因素：</label>
                <div v-if="riskFactorsList.length > 0" class="factors-tags">
                  <el-tag
                    v-for="(factor, index) in riskFactorsList"
                    :key="index"
                    type="warning"
                    class="factor-tag"
                  >
                    {{ factor }}
                  </el-tag>
                </div>
                <span v-else>暂无风险因素信息</span>
              </div>
            </div>

            <div class="assessment-description">
              <label>评估说明：</label>
              <p>{{ assessmentData.assessmentDescription || '暂无评估说明' }}</p>
            </div>
          </div>
        </el-tab-pane>

        <!-- 评估状态 -->
        <el-tab-pane label="评估状态" name="status">
          <div class="detail-section">
            <el-row :gutter="20">
              <el-col :span="12">
                <div class="status-item">
                  <label>评估状态：</label>
                  <el-tag :type="getAssessmentStatusTagType(assessmentData.assessmentStatus)" size="large">
                    {{ getAssessmentStatusLabel(assessmentData.assessmentStatus) }}
                  </el-tag>
                </div>
              </el-col>
              <el-col :span="12">
                <div class="status-item">
                  <label>创建时间：</label>
                  <span>{{ assessmentData.createTime || '-' }}</span>
                </div>
              </el-col>
            </el-row>

            <el-row :gutter="20">
              <el-col :span="12">
                <div class="status-item">
                  <label>创建人：</label>
                  <span>{{ assessmentData.createBy || '-' }}</span>
                </div>
              </el-col>
              <el-col :span="12">
                <div class="status-item">
                  <label>更新时间：</label>
                  <span>{{ assessmentData.updateTime || '-' }}</span>
                </div>
              </el-col>
            </el-row>

            <div v-if="assessmentData.reviewComments" class="review-comments">
              <label>审核意见：</label>
              <p>{{ assessmentData.reviewComments }}</p>
            </div>
          </div>
        </el-tab-pane>
      </el-tabs>
    </div>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">关闭</el-button>
      <el-button type="primary" @click="handleExportReport">导出报告</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { getRiskAssessmentById, exportAssessmentReport } from '@/api/stateAssets/riskAssessment'

export default {
  name: 'RiskAssessmentDetail',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    assessmentId: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      loading: false,
      activeTab: 'basic',
      assessmentData: {}
    }
  },
  computed: {
    dialogVisible: {
      get() {
        return this.visible
      },
      set(val) {
        this.$emit('update:visible', val)
      }
    },
    riskFactorsList() {
      if (!this.assessmentData.riskFactors) return []
      return this.assessmentData.riskFactors.split(';').filter(factor => factor.trim())
    }
  },
  watch: {
    visible(val) {
      if (val && this.assessmentId) {
        this.loadAssessmentDetail()
      }
    }
  },
  methods: {
    // 加载评估详情
    async loadAssessmentDetail() {
      this.loading = true
      try {
        const response = await getRiskAssessmentById(this.assessmentId)
        if (response.code === 200) {
          this.assessmentData = response.data || {}
        }
      } catch (error) {
        this.$message.error('加载详情失败：' + error.message)
      } finally {
        this.loading = false
      }
    },

    // 导出报告
    async handleExportReport() {
      try {
        const response = await exportAssessmentReport({
          riskAssessmentId: this.assessmentId
        })
        
        const blob = new Blob([response], { type: 'application/pdf' })
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = `风险评估报告_${this.assessmentData.enterpriseName}_${new Date().getTime()}.pdf`
        link.click()
        window.URL.revokeObjectURL(url)
        this.$message.success('报告导出成功')
      } catch (error) {
        this.$message.error('导出报告失败：' + error.message)
      }
    },

    // 关闭对话框
    handleClose() {
      this.dialogVisible = false
      this.activeTab = 'basic'
      this.assessmentData = {}
    },

    // 工具方法
    getAssessmentTypeTagType(type) {
      const typeMap = {
        'COMPREHENSIVE': 'primary',
        'FINANCIAL': 'success',
        'OPERATIONAL': 'warning',
        'COMPLIANCE': 'info',
        'GOVERNANCE': 'danger',
        'EXTERNAL': ''
      }
      return typeMap[type] || ''
    },

    getAssessmentTypeLabel(type) {
      const labelMap = {
        'COMPREHENSIVE': '综合评估',
        'FINANCIAL': '财务风险评估',
        'OPERATIONAL': '经营风险评估',
        'COMPLIANCE': '合规风险评估',
        'GOVERNANCE': '治理风险评估',
        'EXTERNAL': '外部风险评估'
      }
      return labelMap[type] || type
    },

    getAssessmentMethodLabel(method) {
      const labelMap = {
        'QUANTITATIVE': '定量分析',
        'QUALITATIVE': '定性分析',
        'MIXED': '混合分析',
        'EXPERT': '专家评估',
        'MODEL': '模型评估'
      }
      return labelMap[method] || method
    },

    getRiskLevelTagType(level) {
      const levelMap = {
        'VERY_LOW': 'success',
        'LOW': 'success',
        'MEDIUM': 'warning',
        'HIGH': 'danger',
        'VERY_HIGH': 'danger',
        'CRITICAL': 'danger'
      }
      return levelMap[level] || ''
    },

    getRiskLevelLabel(level) {
      const labelMap = {
        'VERY_LOW': '极低风险',
        'LOW': '低风险',
        'MEDIUM': '中等风险',
        'HIGH': '高风险',
        'VERY_HIGH': '极高风险',
        'CRITICAL': '临界风险'
      }
      return labelMap[level] || level
    },

    getWarningLevelTagType(level) {
      const levelMap = {
        'GREEN': 'success',
        'YELLOW': 'warning',
        'ORANGE': 'warning',
        'RED': 'danger'
      }
      return levelMap[level] || ''
    },

    getWarningLevelLabel(level) {
      const labelMap = {
        'GREEN': '绿色预警',
        'YELLOW': '黄色预警',
        'ORANGE': '橙色预警',
        'RED': '红色预警'
      }
      return labelMap[level] || level
    },

    getAssessmentStatusTagType(status) {
      const statusMap = {
        'DRAFT': 'info',
        'IN_PROGRESS': 'warning',
        'COMPLETED': 'success',
        'REVIEWED': 'primary',
        'APPROVED': 'success',
        'REJECTED': 'danger'
      }
      return statusMap[status] || ''
    },

    getAssessmentStatusLabel(status) {
      const labelMap = {
        'DRAFT': '草稿',
        'IN_PROGRESS': '评估中',
        'COMPLETED': '已完成',
        'REVIEWED': '已审核',
        'APPROVED': '已批准',
        'REJECTED': '已拒绝'
      }
      return labelMap[status] || status
    },

    getRiskScoreClass(score) {
      if (!score) return ''
      if (score >= 80) return 'risk-score-high'
      if (score >= 60) return 'risk-score-medium'
      return 'risk-score-low'
    },

    getProgressColor(score) {
      if (!score) return '#67c23a'
      if (score >= 80) return '#f56c6c'
      if (score >= 60) return '#e6a23c'
      return '#67c23a'
    }
  }
}
</script>

<style lang="scss" scoped>
.assessment-detail {
  .detail-section {
    padding: 20px;
  }

  .detail-item {
    margin-bottom: 16px;
    
    label {
      font-weight: 600;
      color: #606266;
      margin-right: 8px;
    }
    
    p {
      margin: 8px 0 0 0;
      color: #303133;
      line-height: 1.6;
    }
  }

  // 风险评分样式
  .risk-score-card {
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    color: white;
    padding: 30px;
    border-radius: 12px;
    text-align: center;
    margin-bottom: 20px;

    .score-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 16px;

      h3 {
        margin: 0;
        font-size: 20px;
      }

      .score-value {
        font-size: 48px;
        font-weight: 700;
      }
    }
  }

  .score-dimensions {
    margin-top: 20px;
  }

  .dimension-card {
    background: white;
    border: 1px solid #ebeef5;
    border-radius: 8px;
    padding: 20px;
    text-align: center;
    margin-bottom: 20px;

    .dimension-header {
      display: flex;
      align-items: center;
      justify-content: center;
      margin-bottom: 12px;
      color: #606266;

      i {
        font-size: 18px;
        margin-right: 8px;
      }
    }

    .dimension-score {
      font-size: 32px;
      font-weight: 600;
      margin-bottom: 12px;
    }

    .dimension-progress {
      margin-top: 8px;
    }
  }

  // 预警信息样式
  .warning-card {
    background: #f0f9ff;
    border: 1px solid #e1f5fe;
    border-radius: 8px;
    padding: 20px;
    text-align: center;

    &.warning-triggered {
      background: #fff3e0;
      border-color: #ffcc02;
    }

    .warning-header {
      display: flex;
      align-items: center;
      justify-content: center;
      margin-bottom: 16px;
      color: #606266;

      i {
        font-size: 20px;
        margin-right: 8px;
      }
    }

    .warning-status {
      margin-bottom: 16px;
    }

    .warning-level {
      label {
        color: #606266;
        margin-right: 8px;
      }
    }
  }

  .threshold-card {
    background: white;
    border: 1px solid #ebeef5;
    border-radius: 8px;
    padding: 20px;

    .threshold-item {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 12px;

      label {
        color: #606266;
      }

      .threshold-value {
        font-weight: 600;
        
        &.warning {
          color: #e6a23c;
        }
        
        &.danger {
          color: #f56c6c;
        }
      }
    }
  }

  .warning-message {
    margin-top: 20px;
    
    label {
      display: block;
      margin-bottom: 8px;
      font-weight: 600;
      color: #606266;
    }
  }

  // 风险因素样式
  .risk-factors {
    .factors-list {
      label {
        display: block;
        margin-bottom: 12px;
        font-weight: 600;
        color: #606266;
      }

      .factors-tags {
        .factor-tag {
          margin-right: 8px;
          margin-bottom: 8px;
        }
      }
    }
  }

  .assessment-description {
    margin-top: 20px;
    
    label {
      display: block;
      margin-bottom: 8px;
      font-weight: 600;
      color: #606266;
    }
    
    p {
      background: #f5f7fa;
      padding: 16px;
      border-radius: 4px;
      margin: 0;
      line-height: 1.6;
    }
  }

  // 状态信息样式
  .status-item {
    margin-bottom: 16px;
    
    label {
      font-weight: 600;
      color: #606266;
      margin-right: 8px;
    }
  }

  .review-comments {
    margin-top: 20px;
    
    label {
      display: block;
      margin-bottom: 8px;
      font-weight: 600;
      color: #606266;
    }
    
    p {
      background: #f5f7fa;
      padding: 16px;
      border-radius: 4px;
      margin: 0;
      line-height: 1.6;
    }
  }

  // 风险评分颜色
  .risk-score-high {
    color: #f56c6c;
  }

  .risk-score-medium {
    color: #e6a23c;
  }

  .risk-score-low {
    color: #67c23a;
  }
}

.dialog-footer {
  text-align: right;
}
</style>
