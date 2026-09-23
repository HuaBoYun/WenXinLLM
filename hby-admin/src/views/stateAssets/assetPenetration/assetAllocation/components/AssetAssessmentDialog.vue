<template>
  <el-dialog
    title="资产配置评估"
    :visible.sync="dialogVisible"
    width="1000px"
    :before-close="handleClose"
  >
    <div class="assessment-content" v-loading="loading">
      <el-tabs v-model="activeTab" type="border-card">
        <el-tab-pane label="基础信息" name="basic">
          <el-descriptions :column="2" border>
            <el-descriptions-item label="企业名称">{{ assessmentData.enterpriseName || assessmentData.companyName }}</el-descriptions-item>
            <el-descriptions-item label="资产类型">{{ getAssetTypeText(assessmentData.assetType) }}</el-descriptions-item>
            <el-descriptions-item label="资产规模">{{ formatNumber(assessmentData.assetValue || assessmentData.assetAmount) }}万元</el-descriptions-item>
            <el-descriptions-item label="配置效率">{{ assessmentData.allocationEfficiency || assessmentData.yieldRate }}%</el-descriptions-item>
            <el-descriptions-item label="所属地区">{{ getRegionText(assessmentData.region) }}</el-descriptions-item>
            <el-descriptions-item label="风险等级">
              <el-tag :type="getRiskTag(assessmentData.riskLevel)">
                {{ getRiskText(assessmentData.riskLevel) }}
              </el-tag>
            </el-descriptions-item>
          </el-descriptions>
        </el-tab-pane>
        
        <el-tab-pane label="评估指标" name="indicators">
          <div class="indicators-panel">
            <el-table :data="assessmentIndicators" border>
              <el-table-column label="指标名称" prop="name" width="200" />
              <el-table-column label="当前值" prop="currentValue" align="center" width="120" />
              <el-table-column label="标准值" prop="standardValue" align="center" width="120" />
              <el-table-column label="评估结果" prop="result" align="center" width="120">
                <template slot-scope="scope">
                  <el-tag :type="getResultTag(scope.row.result)" size="mini">
                    {{ scope.row.result }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column label="权重" prop="weight" align="center" width="80" />
              <el-table-column label="得分" prop="score" align="center" width="80" />
              <el-table-column label="说明" prop="description" />
            </el-table>
          </div>
        </el-tab-pane>
        
        <el-tab-pane label="评估结果" name="result">
          <div class="result-panel">
            <el-row :gutter="20">
              <el-col :span="12">
                <el-card shadow="never">
                  <div slot="header">
                    <span>综合评分</span>
                  </div>
                  <div class="score-display">
                    <div class="score-circle">
                      <el-progress
                        type="circle"
                        :percentage="assessmentResult.totalScore"
                        :color="getScoreColor(assessmentResult.totalScore)"
                        :width="120"
                      />
                    </div>
                    <div class="score-info">
                      <h3>{{ assessmentResult.totalScore }}分</h3>
                      <p>{{ getScoreLevel(assessmentResult.totalScore) }}</p>
                    </div>
                  </div>
                </el-card>
              </el-col>
              <el-col :span="12">
                <el-card shadow="never">
                  <div slot="header">
                    <span>评估等级</span>
                  </div>
                  <div class="level-display">
                    <el-rate
                      v-model="assessmentResult.level"
                      disabled
                      show-text
                      :texts="['很差', '较差', '一般', '良好', '优秀']"
                    />
                    <div class="level-description">
                      <p>{{ assessmentResult.levelDescription }}</p>
                    </div>
                  </div>
                </el-card>
              </el-col>
            </el-row>
            
            <el-card shadow="never" class="mt-20">
              <div slot="header">
                <span>改进建议</span>
              </div>
              <div class="suggestions">
                <el-timeline>
                  <el-timeline-item
                    v-for="(suggestion, index) in assessmentResult.suggestions"
                    :key="index"
                    :color="getSuggestionColor(suggestion.priority)"
                  >
                    <h4>{{ suggestion.title }}</h4>
                    <p>{{ suggestion.content }}</p>
                    <el-tag size="mini" :type="getPriorityTag(suggestion.priority)">
                      {{ suggestion.priority }}
                    </el-tag>
                  </el-timeline-item>
                </el-timeline>
              </div>
            </el-card>
          </div>
        </el-tab-pane>
      </el-tabs>
    </div>
    
    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">关闭</el-button>
      <el-button type="primary" @click="handleExport">导出报告</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { getAssetAllocationRationalityAssessment } from '@/api/stateAssets/assetAllocation'

export default {
  name: 'AssetAssessmentDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    allocationData: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      activeTab: 'basic',
      loading: false,
      assessmentData: {},
      assessmentIndicators: [],
      assessmentResult: {
        totalScore: 0,
        level: 0,
        levelDescription: '',
        suggestions: []
      }
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
      if (val) {
        this.initData()
      }
    }
  },
  methods: {
    async initData() {
      this.assessmentData = { ...this.allocationData }
      this.loading = true
      try {
        const response = await getAssetAllocationRationalityAssessment({
          allocationId: this.allocationData.allocationId
        })
        if (response && response.result === 200 && response.data) {
          const data = response.data
          this.assessmentIndicators = data.indicators || []
          this.assessmentResult = data.result || { totalScore: 0, level: 0, levelDescription: '暂无评估数据', suggestions: [] }
        } else {
          this.assessmentIndicators = []
          this.assessmentResult = { totalScore: 0, level: 0, levelDescription: '暂无评估数据', suggestions: [] }
        }
      } catch (error) {
        console.error('获取评估数据失败:', error)
        this.assessmentIndicators = []
        this.assessmentResult = { totalScore: 0, level: 0, levelDescription: '获取评估数据失败', suggestions: [] }
      } finally {
        this.loading = false
      }
    },
    
    handleExport() {
      this.$message.success('正在导出评估报告...')
    },
    
    handleClose() {
      this.$emit('update:visible', false)
    },
    
    formatNumber(num) {
      if (!num) return '0'
      return num.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',')
    },
    
    getAssetTypeText(type) {
      const typeMap = {
        'CURRENT_ASSETS': '流动资产',
        'NON_CURRENT_ASSETS': '非流动资产',
        'FINANCIAL_ASSETS': '金融资产',
        'FIXED_ASSETS': '固定资产',
        'INTANGIBLE_ASSETS': '无形资产',
        'INVESTMENT_ASSETS': '投资性资产'
      }
      return typeMap[type] || type || '-'
    },

    getRegionText(region) {
      const regionMap = {
        'NORTH_CHINA': '华北地区',
        'EAST_CHINA': '华东地区',
        'SOUTH_CHINA': '华南地区',
        'CENTRAL_CHINA': '华中地区',
        'NORTHWEST_CHINA': '西北地区',
        'SOUTHWEST_CHINA': '西南地区',
        'NORTHEAST_CHINA': '东北地区'
      }
      return regionMap[region] || region || '-'
    },

    getRiskTag(level) {
      const tagMap = { 'LOW': 'success', 'MEDIUM': 'warning', 'HIGH': 'danger' }
      return tagMap[level] || 'info'
    },

    getRiskText(level) {
      const textMap = { 'LOW': '低风险', 'MEDIUM': '中风险', 'HIGH': '高风险' }
      return textMap[level] || level || '-'
    },
    
    getStatusTag(status) {
      const tagMap = {
        'COMPLETED': 'success',
        'IN_PROGRESS': 'warning',
        'PENDING': 'info'
      }
      return tagMap[status] || 'info'
    },
    
    getStatusText(status) {
      const textMap = {
        'COMPLETED': '已完成',
        'IN_PROGRESS': '进行中',
        'PENDING': '待评估'
      }
      return textMap[status] || status
    },
    
    getResultTag(result) {
      const tagMap = {
        '优秀': 'success',
        '良好': 'primary',
        '一般': 'warning',
        '较差': 'danger'
      }
      return tagMap[result] || 'info'
    },
    
    getScoreColor(score) {
      if (score >= 90) return '#67C23A'
      if (score >= 80) return '#409EFF'
      if (score >= 70) return '#E6A23C'
      return '#F56C6C'
    },
    
    getScoreLevel(score) {
      if (score >= 90) return '优秀'
      if (score >= 80) return '良好'
      if (score >= 70) return '一般'
      return '较差'
    },
    
    getSuggestionColor(priority) {
      const colorMap = {
        '高': '#F56C6C',
        '中': '#E6A23C',
        '低': '#909399'
      }
      return colorMap[priority] || '#909399'
    },
    
    getPriorityTag(priority) {
      const tagMap = {
        '高': 'danger',
        '中': 'warning',
        '低': 'info'
      }
      return tagMap[priority] || 'info'
    }
  }
}
</script>

<style scoped>
.assessment-content {
  min-height: 500px;
}

.indicators-panel {
  padding: 20px 0;
}

.result-panel {
  padding: 20px 0;
}

.score-display {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px;
}

.score-circle {
  margin-right: 30px;
}

.score-info h3 {
  margin: 0;
  font-size: 24px;
  color: #303133;
}

.score-info p {
  margin: 5px 0 0 0;
  color: #606266;
}

.level-display {
  padding: 20px;
  text-align: center;
}

.level-description {
  margin-top: 20px;
}

.level-description p {
  color: #606266;
  line-height: 1.6;
}

.suggestions {
  padding: 20px;
}

.suggestions h4 {
  margin: 0 0 10px 0;
  color: #303133;
}

.suggestions p {
  margin: 0 0 10px 0;
  color: #606266;
  line-height: 1.6;
}

.mt-20 {
  margin-top: 20px;
}
</style>
