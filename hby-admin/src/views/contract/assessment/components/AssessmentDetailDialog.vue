<template>
  <el-dialog
    title="考核详情"
    :visible.sync="dialogVisible"
    width="1000px"
    :close-on-click-modal="false"
    @open="handleOpen"
    @close="handleClose"
  >
    <div v-loading="loading" class="detail-container">
      <!-- 基本信息 -->
      <el-card class="info-card" shadow="never">
        <div slot="header" class="card-header">
          <span>基本信息</span>
        </div>
        <el-row :gutter="20">
          <el-col :span="8">
            <div class="info-item">
              <label>项目ID：</label>
              <span>{{ assessmentInfo.projectId }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <label>考核期间：</label>
              <span>{{ assessmentInfo.assessmentPeriod }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <label>考核类型：</label>
              <span>{{ getAssessmentTypeName(assessmentInfo.assessmentType) }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <label>总分：</label>
              <span class="score-text" :style="{ color: getScoreColor(assessmentInfo.totalScore) }">
                {{ formatScore(assessmentInfo.totalScore) }}
              </span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <label>考核等级：</label>
              <el-tag
                :type="getLevelTagType(assessmentInfo.assessmentLevel)"
                size="small"
              >
                {{ assessmentInfo.assessmentLevel }}
              </el-tag>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <label>考核状态：</label>
              <el-tag
                :type="getStatusTagType(assessmentInfo.assessmentStatus)"
                size="small"
              >
                {{ getAssessmentStatusName(assessmentInfo.assessmentStatus) }}
              </el-tag>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <label>考核人：</label>
              <span>{{ assessmentInfo.assessorName }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <label>考核日期：</label>
              <span>{{ assessmentInfo.assessmentDate }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <label>审核人：</label>
              <span>{{ assessmentInfo.reviewerName || '--' }}</span>
            </div>
          </el-col>
        </el-row>
        <el-row v-if="assessmentInfo.remarks" :gutter="20">
          <el-col :span="24">
            <div class="info-item">
              <label>备注：</label>
              <span>{{ assessmentInfo.remarks }}</span>
            </div>
          </el-col>
        </el-row>
      </el-card>

      <!-- 指标详情 -->
      <el-card class="detail-card" shadow="never">
        <div slot="header" class="card-header">
          <span>指标详情</span>
        </div>
        <el-table
          :data="indicatorDetails"
          stripe
          border
          style="width: 100%"
        >
          <el-table-column prop="indicatorCode" label="指标编码" width="120" />
          <el-table-column prop="indicatorName" label="指标名称" width="200" show-overflow-tooltip />
          <el-table-column prop="indicatorType" label="指标类型" width="100" align="center">
            <template slot-scope="scope">
              <el-tag size="mini" :type="scope.row.indicatorType === 1 ? 'primary' : 'success'">
                {{ scope.row.indicatorType === 1 ? '定量' : '定性' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="weight" label="权重" width="80" align="center">
            <template slot-scope="scope">
              {{ (scope.row.weight * 100).toFixed(1) }}%
            </template>
          </el-table-column>
          <el-table-column prop="targetValue" label="目标值" width="100" align="center">
            <template slot-scope="scope">
              {{ scope.row.targetValue }}{{ scope.row.unit || '' }}
            </template>
          </el-table-column>
          <el-table-column prop="actualValue" label="实际值" width="100" align="center">
            <template slot-scope="scope">
              {{ scope.row.actualValue }}{{ scope.row.unit || '' }}
            </template>
          </el-table-column>
          <el-table-column prop="score" label="得分" width="80" align="center">
            <template slot-scope="scope">
              <span :style="{ color: getScoreColor(scope.row.score) }">
                {{ formatScore(scope.row.score) }}
              </span>
            </template>
          </el-table-column>
          <el-table-column prop="weightedScore" label="加权得分" width="100" align="center">
            <template slot-scope="scope">
              <span :style="{ color: getScoreColor(scope.row.weightedScore) }">
                {{ formatScore(scope.row.weightedScore) }}
              </span>
            </template>
          </el-table-column>
          <el-table-column prop="dataSource" label="数据来源" width="120" show-overflow-tooltip />
          <el-table-column prop="remarks" label="备注" min-width="150" show-overflow-tooltip />
        </el-table>
      </el-card>

      <!-- 审核信息 -->
      <el-card v-if="assessmentInfo.reviewResult" class="review-card" shadow="never">
        <div slot="header" class="card-header">
          <span>审核信息</span>
        </div>
        <el-row :gutter="20">
          <el-col :span="8">
            <div class="info-item">
              <label>审核结果：</label>
              <el-tag
                :type="assessmentInfo.reviewResult === 'approved' ? 'success' : 'danger'"
                size="small"
              >
                {{ assessmentInfo.reviewResult === 'approved' ? '通过' : '驳回' }}
              </el-tag>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <label>审核日期：</label>
              <span>{{ assessmentInfo.reviewDate }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <label>审核人：</label>
              <span>{{ assessmentInfo.reviewerName }}</span>
            </div>
          </el-col>
        </el-row>
        <el-row v-if="assessmentInfo.reviewComments" :gutter="20">
          <el-col :span="24">
            <div class="info-item">
              <label>审核意见：</label>
              <span>{{ assessmentInfo.reviewComments }}</span>
            </div>
          </el-col>
        </el-row>
      </el-card>
    </div>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">关闭</el-button>
      <el-button type="primary" @click="handleGenerateReport">
        生成报告
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import {
  getAssessmentDetail,
  getAssessmentTypeOptions,
  getAssessmentStatusOptions,
  formatAssessmentScore
} from '@/api/contract/assessment'

export default {
  name: 'AssessmentDetailDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    assessmentId: {
      type: [String, Number],
      default: null
    }
  },
  data() {
    return {
      loading: false,
      assessmentInfo: {},
      indicatorDetails: [],
      assessmentTypeOptions: getAssessmentTypeOptions(),
      assessmentStatusOptions: getAssessmentStatusOptions()
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
  methods: {
    // 对话框打开
    async handleOpen() {
      if (this.assessmentId) {
        await this.loadDetail()
      }
    },

    // 加载详情数据
    async loadDetail() {
      this.loading = true
      try {
        const response = await getAssessmentDetail(this.assessmentId)
        if (response.code === 1) {
          this.assessmentInfo = response.data.assessment || {}
          this.indicatorDetails = response.data.details || []
        } else {
          this.$message.error(response.msg || '加载详情失败')
        }
      } catch (error) {
        console.error('加载详情失败:', error)
        this.$message.error('加载详情失败，请稍后重试')
      } finally {
        this.loading = false
      }
    },

    // 关闭对话框
    handleClose() {
      this.dialogVisible = false
      this.assessmentInfo = {}
      this.indicatorDetails = []
    },

    // 生成报告
    handleGenerateReport() {
      this.$message.info('报告生成功能开发中...')
    },

    // 获取考核类型名称
    getAssessmentTypeName(type) {
      const option = this.assessmentTypeOptions.find(item => item.value === type)
      return option ? option.label : '--'
    },

    // 获取考核状态名称
    getAssessmentStatusName(status) {
      const option = this.assessmentStatusOptions.find(item => item.value === status)
      return option ? option.label : '--'
    },

    // 格式化分数
    formatScore(score) {
      return formatAssessmentScore(score)
    },

    // 获取分数颜色
    getScoreColor(score) {
      if (score >= 90) return '#67C23A'
      if (score >= 80) return '#409EFF'
      if (score >= 70) return '#E6A23C'
      if (score >= 60) return '#F56C6C'
      return '#909399'
    },

    // 获取等级标签类型
    getLevelTagType(level) {
      const typeMap = {
        '优秀': 'success',
        '良好': 'primary',
        '一般': 'warning',
        '合格': 'danger',
        '不合格': 'info'
      }
      return typeMap[level] || 'info'
    },

    // 获取状态标签类型
    getStatusTagType(status) {
      const typeMap = {
        1: 'warning',  // 进行中
        2: 'success',  // 已完成
        3: 'primary'   // 已审核
      }
      return typeMap[status] || 'info'
    }
  }
}
</script>

<style lang="scss" scoped>
.detail-container {
  .info-card,
  .detail-card,
  .review-card {
    margin-bottom: 20px;
    
    &:last-child {
      margin-bottom: 0;
    }
  }

  .card-header {
    font-weight: 500;
    color: #303133;
  }

  .info-item {
    margin-bottom: 15px;
    
    label {
      font-weight: 500;
      color: #606266;
      margin-right: 8px;
    }
    
    .score-text {
      font-weight: 500;
      font-size: 16px;
    }
  }
}

.dialog-footer {
  text-align: right;
}
</style>
