<template>
  <div class="intelligent-audit-detail">
    <!-- 页面头部 -->
    <div class="page-header">
      <el-page-header @back="handleBack" content="智能审核详情" />
      <div class="header-actions">
        <el-button v-if="canStart" type="primary" @click="handleStart">启动审核</el-button>
        <el-button v-if="canPause" type="warning" @click="handlePause">暂停审核</el-button>
        <el-button v-if="canResume" type="success" @click="handleResume">恢复审核</el-button>
        <el-button v-if="canComplete" type="success" @click="handleComplete">完成审核</el-button>
        <el-button v-if="canReview" type="primary" @click="handleReview">复核审核</el-button>
        <el-button v-if="canIntelligentAudit" type="primary" @click="handleIntelligentAudit">智能审核</el-button>
        <el-button type="info" @click="handleEdit">编辑</el-button>
      </div>
    </div>

    <div v-loading="loading" class="detail-content">
      <!-- 基本信息 -->
      <el-card class="info-card" shadow="never">
        <div slot="header" class="card-header">
          <span>基本信息</span>
          <el-tag :type="getStatusTagType(auditData.auditStatus)">
            {{ formatAuditStatus(auditData.auditStatus) }}
          </el-tag>
        </div>
        <el-row :gutter="24">
          <el-col :span="8">
            <div class="info-item">
              <label>审核编码：</label>
              <span>{{ auditData.auditCode }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <label>审核标题：</label>
              <span>{{ auditData.auditTitle }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <label>审核类型：</label>
              <el-tag type="primary">{{ formatAuditType(auditData.auditType) }}</el-tag>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <label>风险等级：</label>
              <el-tag :type="getRiskLevelTagType(auditData.riskLevel)">
                {{ formatRiskLevel(auditData.riskLevel) }}
              </el-tag>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <label>风险评分：</label>
              <span>{{ auditData.riskScore ? auditData.riskScore + '%' : '-' }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <label>置信度：</label>
              <span>{{ auditData.confidenceLevel ? (auditData.confidenceLevel * 100).toFixed(2) + '%' : '-' }}</span>
            </div>
          </el-col>
          <el-col :span="24">
            <div class="info-item">
              <label>审核描述：</label>
              <span>{{ auditData.auditDescription || '-' }}</span>
            </div>
          </el-col>
        </el-row>
      </el-card>

      <!-- 审核人员信息 -->
      <el-card class="info-card" shadow="never">
        <div slot="header" class="card-header">
          <span>审核人员信息</span>
        </div>
        <el-row :gutter="24">
          <el-col :span="8">
            <div class="info-item">
              <label>审核人员：</label>
              <span>{{ auditData.auditorName || '-' }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <label>审核部门：</label>
              <span>{{ auditData.auditDeptName || '-' }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <label>复核人员：</label>
              <span>{{ auditData.reviewerName || '-' }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <label>处理人员：</label>
              <span>{{ auditData.processorName || '-' }}</span>
            </div>
          </el-col>
        </el-row>
      </el-card>

      <!-- 时间信息 -->
      <el-card class="info-card" shadow="never">
        <div slot="header" class="card-header">
          <span>时间信息</span>
        </div>
        <el-row :gutter="24">
          <el-col :span="8">
            <div class="info-item">
              <label>计划开始时间：</label>
              <span>{{ formatDateTime(auditData.plannedStartTime) }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <label>计划结束时间：</label>
              <span>{{ formatDateTime(auditData.plannedEndTime) }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <label>实际开始时间：</label>
              <span>{{ formatDateTime(auditData.auditStartTime) }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <label>实际结束时间：</label>
              <span>{{ formatDateTime(auditData.auditEndTime) }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <label>审核耗时：</label>
              <span>{{ auditData.auditDuration ? auditData.auditDuration + '秒' : '-' }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <label>复核时间：</label>
              <span>{{ formatDateTime(auditData.reviewTime) }}</span>
            </div>
          </el-col>
        </el-row>
      </el-card>

      <!-- 审核结果 -->
      <el-card class="info-card" shadow="never">
        <div slot="header" class="card-header">
          <span>审核结果</span>
        </div>
        <el-row :gutter="24">
          <el-col :span="12">
            <div class="info-item">
              <label>审核结果：</label>
              <span>{{ auditData.auditResult || '-' }}</span>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="info-item">
              <label>复核结果：</label>
              <span>{{ auditData.reviewResult || '-' }}</span>
            </div>
          </el-col>
          <el-col :span="24">
            <div class="info-item">
              <label>审核结论：</label>
              <div class="text-content">{{ auditData.auditConclusion || '-' }}</div>
            </div>
          </el-col>
          <el-col :span="24">
            <div class="info-item">
              <label>复核意见：</label>
              <div class="text-content">{{ auditData.reviewComments || '-' }}</div>
            </div>
          </el-col>
        </el-row>
      </el-card>

      <!-- 智能分析 -->
      <el-card class="info-card" shadow="never">
        <div slot="header" class="card-header">
          <span>智能分析</span>
        </div>
        <el-row :gutter="24">
          <el-col :span="8">
            <div class="info-item">
              <label>异常类型：</label>
              <span>{{ auditData.anomalyType || '-' }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <label>异常严重程度：</label>
              <span>{{ auditData.anomalySeverity || '-' }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <label>预警级别：</label>
              <span>{{ auditData.warningLevel || '-' }}</span>
            </div>
          </el-col>
          <el-col :span="24">
            <div class="info-item">
              <label>异常描述：</label>
              <div class="text-content">{{ auditData.anomalyDescription || '-' }}</div>
            </div>
          </el-col>
          <el-col :span="24">
            <div class="info-item">
              <label>预警消息：</label>
              <div class="text-content">{{ auditData.warningMessage || '-' }}</div>
            </div>
          </el-col>
        </el-row>
      </el-card>

      <!-- 机器学习信息 -->
      <el-card class="info-card" shadow="never">
        <div slot="header" class="card-header">
          <span>机器学习信息</span>
        </div>
        <el-row :gutter="24">
          <el-col :span="8">
            <div class="info-item">
              <label>模型ID：</label>
              <span>{{ auditData.mlModelId || '-' }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <label>模型版本：</label>
              <span>{{ auditData.mlModelVersion || '-' }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <label>预测概率：</label>
              <span>{{ auditData.predictionProbability ? (auditData.predictionProbability * 100).toFixed(2) + '%' : '-' }}</span>
            </div>
          </el-col>
        </el-row>
      </el-card>

      <!-- 跟进信息 -->
      <el-card class="info-card" shadow="never">
        <div slot="header" class="card-header">
          <span>跟进信息</span>
          <el-button size="mini" type="primary" @click="handleAddFollowUp">添加跟进</el-button>
        </div>
        <el-row :gutter="24">
          <el-col :span="8">
            <div class="info-item">
              <label>跟进状态：</label>
              <span>{{ auditData.followUpStatus || '-' }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <label>下次跟进时间：</label>
              <span>{{ formatDateTime(auditData.nextFollowUpTime) }}</span>
            </div>
          </el-col>
          <el-col :span="24">
            <div class="info-item">
              <label>跟进记录：</label>
              <div class="text-content">{{ auditData.followUpRecords || '-' }}</div>
            </div>
          </el-col>
        </el-row>
      </el-card>

      <!-- 其他信息 -->
      <el-card class="info-card" shadow="never">
        <div slot="header" class="card-header">
          <span>其他信息</span>
        </div>
        <el-row :gutter="24">
          <el-col :span="8">
            <div class="info-item">
              <label>创建人：</label>
              <span>{{ auditData.createdBy || '-' }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <label>创建时间：</label>
              <span>{{ formatDateTime(auditData.createdTime) }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <label>更新时间：</label>
              <span>{{ formatDateTime(auditData.updatedTime) }}</span>
            </div>
          </el-col>
          <el-col :span="24">
            <div class="info-item">
              <label>备注：</label>
              <div class="text-content">{{ auditData.remarks || '-' }}</div>
            </div>
          </el-col>
        </el-row>
      </el-card>
    </div>

    <!-- 添加跟进对话框 -->
    <el-dialog
      title="添加跟进记录"
      :visible.sync="followUpVisible"
      width="600px"
    >
      <el-form :model="followUpForm" label-width="100px">
        <el-form-item label="跟进记录" required>
          <el-input
            v-model="followUpForm.followUpRecord"
            type="textarea"
            :rows="4"
            placeholder="请输入跟进记录"
          />
        </el-form-item>
        <el-form-item label="跟进状态">
          <el-select v-model="followUpForm.followUpStatus" placeholder="请选择跟进状态">
            <el-option label="不需要跟进" value="NOT_REQUIRED" />
            <el-option label="需要跟进" value="REQUIRED" />
            <el-option label="跟进中" value="IN_PROGRESS" />
            <el-option label="跟进完成" value="COMPLETED" />
          </el-select>
        </el-form-item>
        <el-form-item label="下次跟进时间">
          <el-date-picker
            v-model="followUpForm.nextFollowUpTime"
            type="datetime"
            placeholder="选择下次跟进时间"
            format="yyyy-MM-dd HH:mm:ss"
            value-format="yyyy-MM-dd HH:mm:ss"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="followUpVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSaveFollowUp">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { 
  getAuditById,
  startAudit,
  pauseAudit,
  resumeAudit,
  completeAudit,
  reviewAudit,
  executeIntelligentAudit,
  addFollowUpRecord,
  updateFollowUpStatus,
  auditUtils
} from '@/api/managementAccountant/ss/intelligentAudit'

export default {
  name: 'IntelligentAuditDetail',
  data() {
    return {
      loading: false,
      auditData: {},
      followUpVisible: false,
      followUpForm: {
        followUpRecord: '',
        followUpStatus: '',
        nextFollowUpTime: ''
      }
    }
  },
  computed: {
    auditId() {
      return this.$route.params.id
    },
    tenantId() {
      return this.$store.getters.tenantId || 1
    },
    canStart() {
      return ['DRAFT', 'PAUSED'].includes(this.auditData.auditStatus)
    },
    canPause() {
      return this.auditData.auditStatus === 'IN_PROGRESS'
    },
    canResume() {
      return this.auditData.auditStatus === 'PAUSED'
    },
    canComplete() {
      return this.auditData.auditStatus === 'IN_PROGRESS'
    },
    canReview() {
      return this.auditData.auditStatus === 'COMPLETED'
    },
    canIntelligentAudit() {
      return ['DRAFT', 'IN_PROGRESS'].includes(this.auditData.auditStatus)
    }
  },
  created() {
    this.loadData()
  },
  methods: {
    // 加载数据
    async loadData() {
      this.loading = true
      try {
        const response = await getAuditById(this.auditId, this.tenantId)
        if (response.success) {
          this.auditData = response.data
        }
      } catch (error) {
        this.$message.error('加载数据失败: ' + error.message)
      } finally {
        this.loading = false
      }
    },

    // 返回
    handleBack() {
      this.$router.go(-1)
    },

    // 编辑
    handleEdit() {
      this.$router.push({
        name: 'IntelligentAuditEdit',
        params: { id: this.auditId }
      })
    },

    // 格式化方法
    formatAuditStatus: auditUtils.formatAuditStatus,
    formatRiskLevel: auditUtils.formatRiskLevel,
    formatAuditType: auditUtils.formatAuditType,

    formatDateTime(dateTime) {
      if (!dateTime) return '-'
      return this.$moment(dateTime).format('YYYY-MM-DD HH:mm:ss')
    },

    // 获取标签类型
    getStatusTagType(status) {
      const typeMap = {
        'DRAFT': 'info',
        'IN_PROGRESS': 'primary',
        'PAUSED': 'warning',
        'COMPLETED': 'success',
        'CANCELLED': 'danger',
        'REVIEWED': 'success'
      }
      return typeMap[status] || 'info'
    },

    getRiskLevelTagType(level) {
      const typeMap = {
        'LOW': 'success',
        'MEDIUM': 'warning',
        'HIGH': 'danger',
        'CRITICAL': 'danger'
      }
      return typeMap[level] || 'info'
    },

    // 启动审核
    async handleStart() {
      try {
        // 这里应该打开一个选择审核人员的对话框
        // 暂时使用当前用户信息
        const auditorInfo = {
          auditorId: this.$store.getters.userId,
          auditorName: this.$store.getters.userName,
          auditDeptId: this.$store.getters.deptId,
          auditDeptName: this.$store.getters.deptName,
          tenantId: this.tenantId
        }

        await startAudit(this.auditId, auditorInfo)
        this.$message.success('启动审核成功')
        this.loadData()
      } catch (error) {
        this.$message.error('启动审核失败: ' + error.message)
      }
    },

    // 暂停审核
    async handlePause() {
      try {
        const { value: reason } = await this.$prompt('请输入暂停原因', '暂停审核', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          inputPattern: /.+/,
          inputErrorMessage: '暂停原因不能为空'
        })

        await pauseAudit(this.auditId, reason, this.tenantId)
        this.$message.success('暂停审核成功')
        this.loadData()
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('暂停审核失败: ' + error.message)
        }
      }
    },

    // 恢复审核
    async handleResume() {
      try {
        await this.$confirm('确认恢复此审核吗？', '恢复审核', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })

        await resumeAudit(this.auditId, this.tenantId)
        this.$message.success('恢复审核成功')
        this.loadData()
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('恢复审核失败: ' + error.message)
        }
      }
    },

    // 完成审核
    async handleComplete() {
      try {
        const { value: result } = await this.$prompt('请输入审核结果', '完成审核', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          inputPattern: /.+/,
          inputErrorMessage: '审核结果不能为空'
        })

        const { value: conclusion } = await this.$prompt('请输入审核结论', '完成审核', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          inputPattern: /.+/,
          inputErrorMessage: '审核结论不能为空'
        })

        await completeAudit(this.auditId, {
          auditResult: result,
          auditConclusion: conclusion,
          tenantId: this.tenantId
        })
        this.$message.success('完成审核成功')
        this.loadData()
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('完成审核失败: ' + error.message)
        }
      }
    },

    // 复核审核
    async handleReview() {
      try {
        const { value: comments } = await this.$prompt('请输入复核意见', '复核审核', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          inputPattern: /.+/,
          inputErrorMessage: '复核意见不能为空'
        })

        // 这里应该打开一个复核对话框，包含复核结果选择
        const reviewInfo = {
          reviewerId: this.$store.getters.userId,
          reviewerName: this.$store.getters.userName,
          reviewComments: comments,
          reviewResult: 'APPROVED', // 默认通过
          tenantId: this.tenantId
        }

        await reviewAudit(this.auditId, reviewInfo)
        this.$message.success('复核审核成功')
        this.loadData()
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('复核审核失败: ' + error.message)
        }
      }
    },

    // 智能审核
    async handleIntelligentAudit() {
      try {
        await this.$confirm('确认执行智能审核吗？', '智能审核', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })

        this.loading = true
        await executeIntelligentAudit(this.auditId, this.tenantId)
        this.$message.success('智能审核执行成功')
        this.loadData()
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('智能审核执行失败: ' + error.message)
        }
      } finally {
        this.loading = false
      }
    },

    // 添加跟进
    handleAddFollowUp() {
      this.followUpForm = {
        followUpRecord: '',
        followUpStatus: '',
        nextFollowUpTime: ''
      }
      this.followUpVisible = true
    },

    // 保存跟进
    async handleSaveFollowUp() {
      try {
        if (!this.followUpForm.followUpRecord) {
          this.$message.error('请输入跟进记录')
          return
        }

        // 添加跟进记录
        await addFollowUpRecord(this.auditId, this.followUpForm.followUpRecord, this.tenantId)

        // 更新跟进状态
        if (this.followUpForm.followUpStatus || this.followUpForm.nextFollowUpTime) {
          await updateFollowUpStatus(this.auditId, {
            followUpStatus: this.followUpForm.followUpStatus,
            nextFollowUpTime: this.followUpForm.nextFollowUpTime,
            tenantId: this.tenantId
          })
        }

        this.$message.success('添加跟进记录成功')
        this.followUpVisible = false
        this.loadData()
      } catch (error) {
        this.$message.error('添加跟进记录失败: ' + error.message)
      }
    }
  }
}
</script>

<style scoped>
.intelligent-audit-detail {
  padding: 20px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.header-actions .el-button {
  margin-left: 10px;
}

.info-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.info-item {
  margin-bottom: 15px;
}

.info-item label {
  font-weight: bold;
  color: #606266;
  margin-right: 10px;
}

.text-content {
  margin-top: 5px;
  padding: 10px;
  background-color: #f5f7fa;
  border-radius: 4px;
  min-height: 60px;
  white-space: pre-wrap;
}
</style>
