<template>
  <el-dialog
    title="风险事件详情"
    :visible.sync="dialogVisible"
    width="1000px"
    top="5vh"
    :close-on-click-modal="false"
  >
    <div v-loading="loading" class="incident-detail">
      <!-- 基本信息卡片 -->
      <div class="detail-card">
        <div class="card-header">
          <h4>基本信息</h4>
          <div class="header-actions">
            <el-tag :type="getRiskLevelTagType(incidentData.riskLevel)" size="medium">
              {{ getRiskLevelLabel(incidentData.riskLevel) }}
            </el-tag>
            <el-tag :type="getProcessingStatusTagType(incidentData.processingStatus)" size="medium">
              {{ getProcessingStatusLabel(incidentData.processingStatus) }}
            </el-tag>
          </div>
        </div>
        <div class="card-content">
          <el-row :gutter="20">
            <el-col :span="8">
              <div class="info-item">
                <label>事件编号：</label>
                <span class="incident-number">{{ incidentData.incidentNumber || '-' }}</span>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="info-item">
                <label>企业名称：</label>
                <span>{{ incidentData.enterpriseName || '-' }}</span>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="info-item">
                <label>事件类型：</label>
                <el-tag :type="getIncidentTypeTagType(incidentData.incidentType)" size="small">
                  {{ getIncidentTypeLabel(incidentData.incidentType) }}
                </el-tag>
              </div>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="12">
              <div class="info-item">
                <label>事件标题：</label>
                <span class="incident-title">{{ incidentData.incidentTitle || '-' }}</span>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="info-item">
                <label>影响范围：</label>
                <span>{{ getImpactScopeLabel(incidentData.impactScope) }}</span>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="info-item">
                <label>紧急程度：</label>
                <el-tag :type="getUrgencyLevelTagType(incidentData.urgencyLevel)" size="small">
                  {{ getUrgencyLevelLabel(incidentData.urgencyLevel) }}
                </el-tag>
              </div>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="8">
              <div class="info-item">
                <label>发生时间：</label>
                <span>{{ incidentData.occurrenceTime || '-' }}</span>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="info-item">
                <label>发现时间：</label>
                <span>{{ incidentData.discoveryTime || '-' }}</span>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="info-item">
                <label>报告时间：</label>
                <span>{{ incidentData.reportTime || '-' }}</span>
              </div>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="8">
              <div class="info-item">
                <label>负责人：</label>
                <span>{{ incidentData.responsiblePerson || '-' }}</span>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="info-item">
                <label>联系电话：</label>
                <span>{{ incidentData.contactPhone || '-' }}</span>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="info-item">
                <label>报告人：</label>
                <span>{{ incidentData.reportBy || '-' }}</span>
              </div>
            </el-col>
          </el-row>
        </div>
      </div>

      <!-- 标签页内容 -->
      <el-tabs v-model="activeTab" type="border-card">
        <!-- 事件描述 -->
        <el-tab-pane label="事件描述" name="description">
          <div class="tab-content">
            <div class="description-section">
              <h5>事件详细描述</h5>
              <div class="description-content">
                {{ incidentData.incidentDescription || '暂无描述' }}
              </div>
            </div>
            
            <div class="description-section">
              <h5>风险因素</h5>
              <div class="risk-factors">
                <el-tag
                  v-for="(factor, index) in getRiskFactorsList()"
                  :key="index"
                  size="small"
                  type="warning"
                  style="margin-right: 8px; margin-bottom: 8px;"
                >
                  {{ factor }}
                </el-tag>
                <span v-if="getRiskFactorsList().length === 0" class="no-data">暂无风险因素</span>
              </div>
            </div>

            <div class="description-section">
              <h5>初步原因分析</h5>
              <div class="description-content">
                {{ incidentData.preliminaryCause || '暂无分析' }}
              </div>
            </div>
          </div>
        </el-tab-pane>

        <!-- 经济损失 -->
        <el-tab-pane label="经济损失" name="loss">
          <div class="tab-content">
            <div class="loss-overview">
              <el-row :gutter="20">
                <el-col :span="8">
                  <div class="loss-card direct-loss">
                    <div class="loss-icon">
                      <i class="el-icon-coin"></i>
                    </div>
                    <div class="loss-content">
                      <div class="loss-amount">¥{{ formatAmount(incidentData.directLoss) }}</div>
                      <div class="loss-label">直接损失</div>
                    </div>
                  </div>
                </el-col>
                <el-col :span="8">
                  <div class="loss-card indirect-loss">
                    <div class="loss-icon">
                      <i class="el-icon-wallet"></i>
                    </div>
                    <div class="loss-content">
                      <div class="loss-amount">¥{{ formatAmount(incidentData.indirectLoss) }}</div>
                      <div class="loss-label">间接损失</div>
                    </div>
                  </div>
                </el-col>
                <el-col :span="8">
                  <div class="loss-card total-loss">
                    <div class="loss-icon">
                      <i class="el-icon-money"></i>
                    </div>
                    <div class="loss-content">
                      <div class="loss-amount">¥{{ formatAmount(incidentData.economicLoss) }}</div>
                      <div class="loss-label">总损失</div>
                    </div>
                  </div>
                </el-col>
              </el-row>
            </div>

            <div class="loss-breakdown">
              <h5>损失构成分析</h5>
              <el-table :data="getLossBreakdown()" border stripe>
                <el-table-column prop="category" label="损失类别" width="150" />
                <el-table-column prop="amount" label="金额" width="150">
                  <template slot-scope="scope">
                    <span class="loss-amount-text">¥{{ formatAmount(scope.row.amount) }}</span>
                  </template>
                </el-table-column>
                <el-table-column prop="percentage" label="占比" width="100">
                  <template slot-scope="scope">
                    {{ scope.row.percentage }}%
                  </template>
                </el-table-column>
                <el-table-column prop="description" label="说明" show-overflow-tooltip />
              </el-table>
            </div>
          </div>
        </el-tab-pane>

        <!-- 处理进展 -->
        <el-tab-pane label="处理进展" name="progress">
          <div class="tab-content">
            <div class="progress-overview">
              <h5>处理状态</h5>
              <div class="status-timeline">
                <el-steps :active="getProcessingStep()" finish-status="success">
                  <el-step title="事件报告" :description="incidentData.reportTime"></el-step>
                  <el-step title="开始处理" :description="incidentData.processingStartTime || '待开始'"></el-step>
                  <el-step title="处理中" :description="incidentData.processingStatus === 'IN_PROGRESS' ? '进行中' : '待处理'"></el-step>
                  <el-step title="已解决" :description="incidentData.resolutionTime || '待解决'"></el-step>
                  <el-step title="已关闭" :description="incidentData.closureTime || '待关闭'"></el-step>
                </el-steps>
              </div>
            </div>

            <div class="actions-section">
              <h5>已采取措施</h5>
              <div class="actions-content">
                {{ incidentData.actionsTaken || '暂无措施记录' }}
              </div>
            </div>

            <div class="actions-section">
              <h5>后续计划</h5>
              <div class="actions-content">
                {{ incidentData.followUpPlan || '暂无后续计划' }}
              </div>
            </div>

            <div class="processing-info" v-if="incidentData.processingStatus !== 'PENDING'">
              <h5>处理信息</h5>
              <el-row :gutter="20">
                <el-col :span="12">
                  <div class="info-item">
                    <label>处理开始时间：</label>
                    <span>{{ incidentData.processingStartTime || '-' }}</span>
                  </div>
                </el-col>
                <el-col :span="12">
                  <div class="info-item">
                    <label>处理人员：</label>
                    <span>{{ incidentData.processingBy || '-' }}</span>
                  </div>
                </el-col>
              </el-row>
              <div class="info-item" v-if="incidentData.processingNotes">
                <label>处理说明：</label>
                <div class="processing-notes">{{ incidentData.processingNotes }}</div>
              </div>
            </div>
          </div>
        </el-tab-pane>

        <!-- 相关附件 -->
        <el-tab-pane label="相关附件" name="attachments">
          <div class="tab-content">
            <div class="attachments-section">
              <h5>事件附件</h5>
              <div v-if="getAttachmentsList().length > 0" class="attachments-list">
                <div
                  v-for="(attachment, index) in getAttachmentsList()"
                  :key="index"
                  class="attachment-item"
                >
                  <div class="attachment-icon">
                    <i :class="getFileIcon(attachment)"></i>
                  </div>
                  <div class="attachment-info">
                    <div class="attachment-name">{{ getFileName(attachment) }}</div>
                    <div class="attachment-actions">
                      <el-button type="text" size="small" @click="previewFile(attachment)">
                        预览
                      </el-button>
                      <el-button type="text" size="small" @click="downloadFile(attachment)">
                        下载
                      </el-button>
                    </div>
                  </div>
                </div>
              </div>
              <div v-else class="no-attachments">
                <i class="el-icon-document"></i>
                <p>暂无相关附件</p>
              </div>
            </div>
          </div>
        </el-tab-pane>

        <!-- 备注信息 -->
        <el-tab-pane label="备注信息" name="remarks">
          <div class="tab-content">
            <div class="remarks-section">
              <h5>事件备注</h5>
              <div class="remarks-content">
                {{ incidentData.remarks || '暂无备注信息' }}
              </div>
            </div>

            <div class="settings-section">
              <h5>处理设置</h5>
              <div class="settings-list">
                <div class="setting-item">
                  <el-checkbox :value="incidentData.requireImmediateAction" disabled>
                    需要立即处理
                  </el-checkbox>
                </div>
                <div class="setting-item">
                  <el-checkbox :value="incidentData.notifyManagement" disabled>
                    通知管理层
                  </el-checkbox>
                </div>
                <div class="setting-item">
                  <el-checkbox :value="incidentData.reportToRegulator" disabled>
                    上报监管部门
                  </el-checkbox>
                </div>
              </div>
            </div>

            <div class="metadata-section">
              <h5>元数据信息</h5>
              <el-row :gutter="20">
                <el-col :span="12">
                  <div class="info-item">
                    <label>创建时间：</label>
                    <span>{{ incidentData.createTime || '-' }}</span>
                  </div>
                </el-col>
                <el-col :span="12">
                  <div class="info-item">
                    <label>更新时间：</label>
                    <span>{{ incidentData.updateTime || '-' }}</span>
                  </div>
                </el-col>
              </el-row>
            </div>
          </div>
        </el-tab-pane>
      </el-tabs>
    </div>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">关闭</el-button>
      <el-button type="primary" @click="handleExportReport">
        导出报告
      </el-button>
      <el-button
        v-if="incidentData.processingStatus === 'PENDING'"
        type="warning"
        @click="handleStartProcessing"
      >
        开始处理
      </el-button>
      <el-button
        v-if="incidentData.processingStatus === 'IN_PROGRESS'"
        type="success"
        @click="handleEmergencyResponse"
      >
        应急响应
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { getRiskIncidentDetail, startProcessing, exportIncidentReport } from '@/api/stateAssets/riskIncident'

export default {
  name: 'RiskIncidentDetail',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    incidentId: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      loading: false,
      activeTab: 'description',
      incidentData: {}
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
    }
  },
  watch: {
    visible(val) {
      if (val && this.incidentId) {
        this.loadIncidentDetail()
      }
    }
  },
  methods: {
    // 加载事件详情
    async loadIncidentDetail() {
      this.loading = true
      try {
        const response = await getRiskIncidentDetail(this.incidentId)
        if (response.code === 200) {
          this.incidentData = response.data || {}
        }
      } catch (error) {
        this.$message.error('加载事件详情失败：' + error.message)
      } finally {
        this.loading = false
      }
    },

    // 开始处理
    async handleStartProcessing() {
      try {
        await this.$confirm('确定要开始处理这个风险事件吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        
        const response = await startProcessing({
          riskIncidentId: this.incidentId,
          startBy: this.$store.getters.userInfo.userName,
          processingStartTime: new Date().toISOString().slice(0, 19).replace('T', ' ')
        })
        
        if (response.code === 200) {
          this.$message.success('事件处理已开始')
          this.loadIncidentDetail()
          this.$emit('success')
        }
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('开始处理失败：' + error.message)
        }
      }
    },

    // 应急响应
    handleEmergencyResponse() {
      this.$emit('emergency-response', this.incidentData)
    },

    // 导出报告
    async handleExportReport() {
      try {
        const response = await exportIncidentReport(this.incidentId)
        const blob = new Blob([response], { type: 'application/vnd.openxmlformats-officedocument.wordprocessingml.document' })
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = `风险事件报告_${this.incidentData.incidentNumber}_${new Date().getTime()}.docx`
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
      this.activeTab = 'description'
      this.incidentData = {}
    },

    // 工具方法
    getRiskFactorsList() {
      if (!this.incidentData.riskFactors) return []
      return this.incidentData.riskFactors.split(';').filter(factor => factor.trim())
    },

    getAttachmentsList() {
      if (!this.incidentData.attachments) return []
      return this.incidentData.attachments.split(',').filter(url => url.trim())
    },

    getLossBreakdown() {
      const directLoss = this.incidentData.directLoss || 0
      const indirectLoss = this.incidentData.indirectLoss || 0
      const total = directLoss + indirectLoss

      return [
        {
          category: '直接损失',
          amount: directLoss,
          percentage: total > 0 ? Math.round((directLoss / total) * 100) : 0,
          description: '直接经济损失，包括设备损坏、资产减值等'
        },
        {
          category: '间接损失',
          amount: indirectLoss,
          percentage: total > 0 ? Math.round((indirectLoss / total) * 100) : 0,
          description: '间接经济损失，包括业务中断、声誉损失等'
        }
      ]
    },

    getProcessingStep() {
      const status = this.incidentData.processingStatus
      switch (status) {
        case 'PENDING': return 0
        case 'IN_PROGRESS': return 2
        case 'RESOLVED': return 3
        case 'CLOSED': return 4
        default: return 0
      }
    },

    formatAmount(amount) {
      if (!amount) return '0'
      return amount.toLocaleString()
    },

    getFileName(url) {
      return url.split('/').pop() || '未知文件'
    },

    getFileIcon(url) {
      const ext = url.split('.').pop()?.toLowerCase()
      switch (ext) {
        case 'pdf': return 'el-icon-document'
        case 'doc':
        case 'docx': return 'el-icon-document'
        case 'xls':
        case 'xlsx': return 'el-icon-s-grid'
        case 'jpg':
        case 'jpeg':
        case 'png':
        case 'gif': return 'el-icon-picture'
        default: return 'el-icon-document'
      }
    },

    previewFile(url) {
      window.open(url, '_blank')
    },

    downloadFile(url) {
      const link = document.createElement('a')
      link.href = url
      link.download = this.getFileName(url)
      link.click()
    },

    // 标签类型方法
    getRiskLevelTagType(level) {
      const levelMap = {
        'LOW': 'success',
        'MEDIUM': 'warning',
        'HIGH': 'danger',
        'CRITICAL': 'danger'
      }
      return levelMap[level] || ''
    },

    getRiskLevelLabel(level) {
      const labelMap = {
        'LOW': '低风险',
        'MEDIUM': '中风险',
        'HIGH': '高风险',
        'CRITICAL': '极高风险'
      }
      return labelMap[level] || level
    },

    getProcessingStatusTagType(status) {
      const statusMap = {
        'PENDING': 'info',
        'IN_PROGRESS': 'warning',
        'RESOLVED': 'success',
        'CLOSED': 'info'
      }
      return statusMap[status] || ''
    },

    getProcessingStatusLabel(status) {
      const labelMap = {
        'PENDING': '待处理',
        'IN_PROGRESS': '处理中',
        'RESOLVED': '已解决',
        'CLOSED': '已关闭'
      }
      return labelMap[status] || status
    },

    getIncidentTypeTagType(type) {
      const typeMap = {
        'FINANCIAL': 'danger',
        'OPERATIONAL': 'warning',
        'COMPLIANCE': 'primary',
        'INFORMATION_SECURITY': 'danger',
        'MARKET': 'warning',
        'CREDIT': 'info',
        'OTHER': 'info'
      }
      return typeMap[type] || ''
    },

    getIncidentTypeLabel(type) {
      const labelMap = {
        'FINANCIAL': '财务风险',
        'OPERATIONAL': '运营风险',
        'COMPLIANCE': '合规风险',
        'INFORMATION_SECURITY': '信息安全',
        'MARKET': '市场风险',
        'CREDIT': '信用风险',
        'OTHER': '其他'
      }
      return labelMap[type] || type
    },

    getImpactScopeLabel(scope) {
      const labelMap = {
        'DEPARTMENT': '部门级',
        'ENTERPRISE': '企业级',
        'GROUP': '集团级',
        'INDUSTRY': '行业级',
        'SOCIETY': '社会级'
      }
      return labelMap[scope] || scope
    },

    getUrgencyLevelTagType(level) {
      const levelMap = {
        'NORMAL': 'info',
        'URGENT': 'warning',
        'VERY_URGENT': 'danger',
        'CRITICAL': 'danger'
      }
      return levelMap[level] || ''
    },

    getUrgencyLevelLabel(level) {
      const labelMap = {
        'NORMAL': '一般',
        'URGENT': '紧急',
        'VERY_URGENT': '非常紧急',
        'CRITICAL': '特急'
      }
      return labelMap[level] || level
    }
  }
}
</script>

<style lang="scss" scoped>
.incident-detail {
  .detail-card {
    margin-bottom: 20px;
    background: white;
    border-radius: 8px;
    border: 1px solid #ebeef5;

    .card-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 16px 20px;
      border-bottom: 1px solid #ebeef5;
      background: #fafafa;

      h4 {
        margin: 0;
        color: #303133;
        font-size: 16px;
        font-weight: 600;
      }

      .header-actions {
        .el-tag {
          margin-left: 8px;
        }
      }
    }

    .card-content {
      padding: 20px;
    }
  }

  .info-item {
    margin-bottom: 12px;
    
    label {
      font-weight: 600;
      color: #606266;
      margin-right: 8px;
    }

    .incident-number {
      font-family: 'Courier New', monospace;
      color: #409eff;
      font-weight: 600;
    }

    .incident-title {
      font-weight: 600;
      color: #303133;
    }
  }

  .tab-content {
    padding: 20px;

    h5 {
      margin: 0 0 16px 0;
      color: #303133;
      font-size: 16px;
      font-weight: 600;
      border-bottom: 1px solid #ebeef5;
      padding-bottom: 8px;
    }
  }

  .description-section {
    margin-bottom: 24px;

    .description-content {
      padding: 16px;
      background: #f8f9fa;
      border-radius: 6px;
      line-height: 1.6;
      color: #606266;
    }

    .risk-factors {
      .no-data {
        color: #909399;
        font-style: italic;
      }
    }
  }

  .loss-overview {
    margin-bottom: 24px;

    .loss-card {
      display: flex;
      align-items: center;
      padding: 20px;
      background: white;
      border-radius: 8px;
      border: 1px solid #ebeef5;

      .loss-icon {
        width: 50px;
        height: 50px;
        border-radius: 50%;
        display: flex;
        align-items: center;
        justify-content: center;
        margin-right: 16px;

        i {
          font-size: 20px;
          color: white;
        }
      }

      &.direct-loss .loss-icon {
        background: linear-gradient(135deg, #f56c6c, #f89898);
      }

      &.indirect-loss .loss-icon {
        background: linear-gradient(135deg, #e6a23c, #f0c78a);
      }

      &.total-loss .loss-icon {
        background: linear-gradient(135deg, #409eff, #66b1ff);
      }

      .loss-content {
        .loss-amount {
          font-size: 24px;
          font-weight: 600;
          color: #303133;
          line-height: 1;
          margin-bottom: 4px;
        }

        .loss-label {
          font-size: 14px;
          color: #909399;
        }
      }
    }
  }

  .loss-breakdown {
    .loss-amount-text {
      color: #f56c6c;
      font-weight: 600;
    }
  }

  .progress-overview {
    margin-bottom: 24px;

    .status-timeline {
      padding: 20px;
      background: #f8f9fa;
      border-radius: 6px;
    }
  }

  .actions-section {
    margin-bottom: 24px;

    .actions-content {
      padding: 16px;
      background: #f8f9fa;
      border-radius: 6px;
      line-height: 1.6;
      color: #606266;
    }
  }

  .processing-info {
    padding: 16px;
    background: #f0f9ff;
    border-radius: 6px;
    border-left: 4px solid #409eff;

    .processing-notes {
      margin-top: 8px;
      padding: 12px;
      background: white;
      border-radius: 4px;
      line-height: 1.6;
      color: #606266;
    }
  }

  .attachments-section {
    .attachments-list {
      .attachment-item {
        display: flex;
        align-items: center;
        padding: 12px;
        background: #f8f9fa;
        border-radius: 6px;
        margin-bottom: 8px;

        .attachment-icon {
          width: 40px;
          height: 40px;
          display: flex;
          align-items: center;
          justify-content: center;
          background: #409eff;
          color: white;
          border-radius: 6px;
          margin-right: 12px;

          i {
            font-size: 18px;
          }
        }

        .attachment-info {
          flex: 1;

          .attachment-name {
            font-weight: 600;
            color: #303133;
            margin-bottom: 4px;
          }

          .attachment-actions {
            .el-button {
              padding: 0;
              margin-right: 12px;
            }
          }
        }
      }
    }

    .no-attachments {
      text-align: center;
      padding: 40px;
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
  }

  .remarks-section {
    margin-bottom: 24px;

    .remarks-content {
      padding: 16px;
      background: #f8f9fa;
      border-radius: 6px;
      line-height: 1.6;
      color: #606266;
    }
  }

  .settings-section {
    margin-bottom: 24px;

    .settings-list {
      .setting-item {
        margin-bottom: 12px;
      }
    }
  }

  .metadata-section {
    padding: 16px;
    background: #f8f9fa;
    border-radius: 6px;
  }
}

.dialog-footer {
  text-align: right;
}
</style>
