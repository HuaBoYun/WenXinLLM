<template>
  <el-dialog
    title="查看评估模型"
    :visible.sync="dialogVisible"
    width="800px"
    :close-on-click-modal="false"
    @close="handleClose"
  >
    <div v-loading="loading" class="model-detail">
      <el-descriptions :column="2" border>
        <el-descriptions-item label="模型编码">
          {{ modelData.modelCode || '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="模型名称">
          {{ modelData.modelName || '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="业务场景">
          {{ getBusinessScenarioText(modelData.businessScenario) }}
        </el-descriptions-item>
        <el-descriptions-item label="行业类型">
          {{ modelData.industryType || '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="评分算法">
          {{ getScoreAlgorithmText(modelData.scoreAlgorithm) }}
        </el-descriptions-item>
        <el-descriptions-item label="总权重">
          {{ modelData.totalWeight || '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="版本号">
          {{ modelData.version || '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="getStatusType(modelData.status)">
            {{ getStatusText(modelData.status) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="是否启用">
          <el-tag :type="modelData.isEnabled === 'Y' ? 'success' : 'info'">
            {{ modelData.isEnabled === 'Y' ? '已启用' : '未启用' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="准确率">
          {{ modelData.accuracyRate ? modelData.accuracyRate + '%' : '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="创建人">
          {{ modelData.createUser || '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="创建时间">
          {{ formatDate(modelData.createTime) }}
        </el-descriptions-item>
      </el-descriptions>

      <!-- 风险阈值配置 -->
      <div class="threshold-section" v-if="hasThresholdData">
        <h4>风险阈值配置</h4>
        <el-row :gutter="20">
          <el-col :span="8">
            <div class="threshold-item low">
              <div class="threshold-label">低风险阈值</div>
              <div class="threshold-value">{{ modelData.riskThresholdLow || '-' }}</div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="threshold-item medium">
              <div class="threshold-label">中风险阈值</div>
              <div class="threshold-value">{{ modelData.riskThresholdMedium || '-' }}</div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="threshold-item high">
              <div class="threshold-label">高风险阈值</div>
              <div class="threshold-value">{{ modelData.riskThresholdHigh || '-' }}</div>
            </div>
          </el-col>
        </el-row>
      </div>

      <!-- 模型描述 -->
      <div class="description-section" v-if="modelData.description">
        <h4>模型描述</h4>
        <div class="description-content">
          {{ modelData.description }}
        </div>
      </div>

      <!-- 时间信息 -->
      <div class="time-section">
        <h4>时间信息</h4>
        <el-descriptions :column="2" border>
          <el-descriptions-item label="最后测试时间">
            {{ formatDate(modelData.lastTestTime) }}
          </el-descriptions-item>
          <el-descriptions-item label="发布时间">
            {{ formatDate(modelData.publishTime) }}
          </el-descriptions-item>
          <el-descriptions-item label="更新人">
            {{ modelData.updateUser || '-' }}
          </el-descriptions-item>
          <el-descriptions-item label="更新时间">
            {{ formatDate(modelData.updateTime) }}
          </el-descriptions-item>
        </el-descriptions>
      </div>
    </div>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">关闭</el-button>
    </div>
  </el-dialog>
</template>

<script>
export default {
  name: 'EvaluationModelView',
  data() {
    return {
      dialogVisible: false,
      loading: false,
      modelData: {}
    }
  },
  computed: {
    hasThresholdData() {
      return this.modelData.riskThresholdLow || 
             this.modelData.riskThresholdMedium || 
             this.modelData.riskThresholdHigh
    }
  },
  methods: {
    // 显示对话框
    show(modelData) {
      this.modelData = modelData || {}
      this.dialogVisible = true
    },

    // 关闭对话框
    handleClose() {
      this.dialogVisible = false
      this.modelData = {}
    },

    // 获取业务场景文本
    getBusinessScenarioText(scenario) {
      const scenarioMap = {
        'PROCUREMENT': '采购风险评估',
        'FINANCE': '财务风险评估', 
        'AUDIT': '审计风险评估',
        'COMPLIANCE': '合规风险评估',
        'CREDIT': '信用风险评估'
      }
      return scenarioMap[scenario] || scenario || '-'
    },

    // 获取评分算法文本
    getScoreAlgorithmText(algorithm) {
      const algorithmMap = {
        'WEIGHTED_SUM': '加权求和',
        'NEURAL_NETWORK': '神经网络',
        'DECISION_TREE': '决策树',
        'RANDOM_FOREST': '随机森林'
      }
      return algorithmMap[algorithm] || algorithm || '-'
    },

    // 获取状态文本
    getStatusText(status) {
      const statusMap = {
        'DRAFT': '草稿',
        'TESTING': '测试中',
        'PUBLISHED': '已发布',
        'ARCHIVED': '已归档'
      }
      return statusMap[status] || status || '-'
    },

    // 获取状态类型
    getStatusType(status) {
      const typeMap = {
        'DRAFT': 'info',
        'TESTING': 'warning', 
        'PUBLISHED': 'success',
        'ARCHIVED': 'danger'
      }
      return typeMap[status] || 'info'
    },

    // 格式化日期
    formatDate(dateStr) {
      if (!dateStr) return '-'
      try {
        const date = new Date(dateStr)
        return date.toLocaleString('zh-CN', {
          year: 'numeric',
          month: '2-digit',
          day: '2-digit',
          hour: '2-digit',
          minute: '2-digit',
          second: '2-digit'
        })
      } catch (error) {
        return dateStr
      }
    }
  }
}
</script>

<style scoped>
.model-detail {
  max-height: 600px;
  overflow-y: auto;
}

.threshold-section,
.description-section,
.time-section {
  margin-top: 20px;
}

.threshold-section h4,
.description-section h4,
.time-section h4 {
  margin-bottom: 15px;
  color: #303133;
  font-size: 16px;
  font-weight: 600;
}

.threshold-item {
  text-align: center;
  padding: 15px;
  border-radius: 6px;
  background-color: #f5f7fa;
}

.threshold-item.low {
  border-left: 4px solid #67c23a;
}

.threshold-item.medium {
  border-left: 4px solid #e6a23c;
}

.threshold-item.high {
  border-left: 4px solid #f56c6c;
}

.threshold-label {
  font-size: 14px;
  color: #606266;
  margin-bottom: 8px;
}

.threshold-value {
  font-size: 18px;
  font-weight: 600;
  color: #303133;
}

.description-content {
  padding: 15px;
  background-color: #f5f7fa;
  border-radius: 6px;
  line-height: 1.6;
  color: #606266;
}

.dialog-footer {
  text-align: right;
}
</style>
