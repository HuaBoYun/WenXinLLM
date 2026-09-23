<template>
  <el-dialog
    title="财务风险评估"
    :visible.sync="dialogVisible"
    width="1000px"
    :before-close="handleClose"
  >
    <div v-loading="loading" class="assessment-content">
      <el-tabs v-model="activeTab" type="border-card">
        <el-tab-pane label="基础信息" name="basic">
          <el-form :model="assessmentForm" label-width="120px">
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="企业名称">
                  <el-input v-model="assessmentForm.enterpriseName" disabled />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="风险类型">
                  <el-input v-model="assessmentForm.riskType" disabled />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="检测方法">
                  <el-input v-model="assessmentForm.detectionMethod" disabled />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="检测周期">
                  <el-input v-model="assessmentForm.period" disabled />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="当前风险等级">
                  <el-tag :type="getRiskLevelTag(assessmentForm.currentRiskLevel)" size="medium">
                    {{ getRiskLevelText(assessmentForm.currentRiskLevel) }}
                  </el-tag>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="当前风险评分">
                  <el-input-number
                    v-model="assessmentForm.currentRiskScore"
                    :min="0"
                    :max="100"
                    disabled
                  />
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
        </el-tab-pane>

        <el-tab-pane label="风险指标" name="indicators">
          <div v-loading="indicatorsLoading" class="indicators-panel">
            <h4>关键风险指标</h4>
            <el-table :data="riskIndicators" border>
              <el-table-column label="指标名称" prop="indicatorName" width="200" />
              <el-table-column label="当前值" prop="currentValue" width="120" align="center" />
              <el-table-column label="标准值" prop="standardValue" width="120" align="center" />
              <el-table-column label="偏差率" prop="deviationRate" width="120" align="center" />
              <el-table-column label="风险状态" prop="riskStatus" width="120" align="center">
                <template slot-scope="scope">
                  <el-tag :type="getIndicatorStatusTag(scope.row.riskStatus)" size="mini">
                    {{ getIndicatorStatusText(scope.row.riskStatus) }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column label="说明" prop="description" />
            </el-table>

            <template v-if="relatedStatement">
              <h4 class="mt-20">关联财务报表数据</h4>
              <el-row :gutter="20">
                <el-col :span="8">
                  <el-statistic title="总资产" :value="relatedStatement.totalAssets || 0" suffix="万元" />
                </el-col>
                <el-col :span="8">
                  <el-statistic title="营业收入" :value="relatedStatement.revenue || 0" suffix="万元" />
                </el-col>
                <el-col :span="8">
                  <el-statistic title="净利润" :value="relatedStatement.netProfit || 0" suffix="万元" />
                </el-col>
              </el-row>
            </template>
          </div>
        </el-tab-pane>

        <el-tab-pane label="评估结果" name="result">
          <div class="result-panel">
            <el-row :gutter="20">
              <el-col :span="12">
                <el-card shadow="never" class="result-card">
                  <div slot="header">
                    <span>综合评估结果</span>
                  </div>
                  <div class="result-content">
                    <div class="score-display">
                      <div class="score-value">{{ assessmentResult.totalScore }}</div>
                      <div class="score-label">综合评分</div>
                    </div>
                    <el-progress
                      :percentage="assessmentResult.totalScore"
                      :color="getScoreColor(assessmentResult.totalScore)"
                      :stroke-width="20"
                    />
                  </div>
                </el-card>
              </el-col>
              <el-col :span="12">
                <el-card shadow="never" class="result-card">
                  <div slot="header">
                    <span>风险等级调整</span>
                  </div>
                  <div class="result-content">
                    <el-form label-width="100px">
                      <el-form-item label="调整后等级">
                        <el-select v-model="assessmentResult.adjustedRiskLevel" style="width: 100%;">
                          <el-option label="低风险" value="LOW" />
                          <el-option label="中风险" value="MEDIUM" />
                          <el-option label="高风险" value="HIGH" />
                          <el-option label="极高风险" value="CRITICAL" />
                        </el-select>
                      </el-form-item>
                      <el-form-item label="调整原因">
                        <el-input
                          v-model="assessmentResult.adjustmentReason"
                          type="textarea"
                          :rows="3"
                          placeholder="请输入调整原因"
                        />
                      </el-form-item>
                    </el-form>
                  </div>
                </el-card>
              </el-col>
            </el-row>

            <el-card shadow="never" class="mt-20">
              <div slot="header">
                <span>风险建议措施</span>
              </div>
              <el-table v-loading="suggestionsLoading" :data="riskSuggestions" border>
                <el-table-column label="优先级" prop="priority" width="100" align="center">
                  <template slot-scope="scope">
                    <el-tag :type="getPriorityTag(scope.row.priority)" size="mini">
                      {{ getPriorityText(scope.row.priority) }}
                    </el-tag>
                  </template>
                </el-table-column>
                <el-table-column label="建议措施" prop="suggestion" />
                <el-table-column label="预期效果" prop="expectedEffect" width="150" />
                <el-table-column label="实施期限" prop="deadline" width="120" />
              </el-table>
            </el-card>
          </div>
        </el-tab-pane>
      </el-tabs>
    </div>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取消</el-button>
      <el-button type="primary" :loading="submitLoading" @click="handleSubmit">保存评估</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { assessFinancialRisk, getFinancialRiskDetail, getFinancialRiskSupervisionSuggestions } from '@/api/stateAssets/financialRisk'

export default {
  name: 'FinancialRiskAssessmentDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    riskData: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      activeTab: 'basic',
      loading: false,
      indicatorsLoading: false,
      suggestionsLoading: false,
      submitLoading: false,
      assessmentForm: {
        enterpriseName: '',
        riskType: '',
        detectionMethod: '',
        period: '',
        currentRiskLevel: '',
        currentRiskScore: 0
      },
      assessmentResult: {
        totalScore: 0,
        adjustedRiskLevel: '',
        adjustmentReason: ''
      },
      riskIndicators: [],
      riskSuggestions: [],
      relatedStatement: null
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
        this.initForm()
      }
    }
  },
  methods: {
    async initForm() {
      this.activeTab = 'basic'
      this.assessmentForm = {
        enterpriseName: this.riskData.enterpriseName || '',
        riskType: this.riskData.riskType || '',
        detectionMethod: '',
        period: this.riskData.period || '',
        currentRiskLevel: this.riskData.riskLevel || '',
        currentRiskScore: this.riskData.riskScore || 0
      }
      this.assessmentResult = {
        totalScore: this.riskData.riskScore || 0,
        adjustedRiskLevel: this.riskData.riskLevel || '',
        adjustmentReason: ''
      }
      this.riskIndicators = []
      this.riskSuggestions = []
      this.relatedStatement = null

      await this.loadDetail()
      this.loadRiskSuggestions()
    },

    /** 加载风险详情，填充基础信息和风险指标 */
    async loadDetail() {
      if (!this.riskData.id) return
      this.loading = true
      this.indicatorsLoading = true
      try {
        const res = await getFinancialRiskDetail({ id: this.riskData.id })
        if ((res.result === 200 || res.code === 1) && res.data) {
          const detail = res.data
          // 补充基础信息
          this.assessmentForm.detectionMethod = detail.detectionMethod || ''
          this.assessmentForm.period = detail.period || this.assessmentForm.period

          // 构建风险指标表格
          this.buildIndicators(detail)

          // 关联财务报表
          if (detail.relatedStatement) {
            this.relatedStatement = detail.relatedStatement
          }
        }
      } catch (e) {
        this.$message.error('加载风险详情失败')
      } finally {
        this.loading = false
        this.indicatorsLoading = false
      }
    },

    /** 根据详情数据构建指标列表 */
    buildIndicators(detail) {
      const indicators = []

      // 主指标：来自详情的核心偏差数据
      if (detail.indicatorName) {
        const deviation = parseFloat(detail.deviationRate) || 0
        indicators.push({
          indicatorName: detail.indicatorName,
          currentValue: detail.actualValue || '-',
          standardValue: detail.expectedValue || '-',
          deviationRate: detail.deviationRate ? `${detail.deviationRate}%` : '-',
          riskStatus: this.calcRiskStatus(deviation),
          description: `${detail.anomalyType || '异常指标'}检测`
        })
      }

      // 从关联报表派生财务比率指标
      if (detail.relatedStatement) {
        const stmt = detail.relatedStatement
        if (stmt.totalAssets && stmt.revenue) {
          const assetTurnover = (stmt.revenue / stmt.totalAssets).toFixed(2)
          indicators.push({
            indicatorName: '总资产周转率',
            currentValue: assetTurnover,
            standardValue: '≥0.5',
            deviationRate: '-',
            riskStatus: parseFloat(assetTurnover) < 0.5 ? 'HIGH' : 'LOW',
            description: '营业收入与总资产的比率'
          })
        }
        if (stmt.totalAssets && stmt.netProfit) {
          const roa = ((stmt.netProfit / stmt.totalAssets) * 100).toFixed(2)
          indicators.push({
            indicatorName: '总资产收益率(ROA)',
            currentValue: `${roa}%`,
            standardValue: '≥5%',
            deviationRate: '-',
            riskStatus: parseFloat(roa) < 5 ? 'MEDIUM' : 'LOW',
            description: '净利润与总资产的比率'
          })
        }
        if (stmt.revenue && stmt.netProfit) {
          const margin = ((stmt.netProfit / stmt.revenue) * 100).toFixed(2)
          indicators.push({
            indicatorName: '净利润率',
            currentValue: `${margin}%`,
            standardValue: '≥10%',
            deviationRate: '-',
            riskStatus: parseFloat(margin) < 10 ? 'MEDIUM' : 'LOW',
            description: '净利润与营业收入的比率'
          })
        }
      }

      this.riskIndicators = indicators
    },

    /** 根据偏差率计算风险状态 */
    calcRiskStatus(deviation) {
      const abs = Math.abs(deviation)
      if (abs >= 30) return 'HIGH'
      if (abs >= 15) return 'MEDIUM'
      return 'LOW'
    },

    /** 加载风险建议措施 */
    async loadRiskSuggestions() {
      if (!this.riskData.id) return
      this.suggestionsLoading = true
      try {
        const res = await getFinancialRiskSupervisionSuggestions({ id: this.riskData.id })
        if ((res.result === 200 || res.code === 1) && res.data) {
          this.riskSuggestions = Array.isArray(res.data) ? res.data : []
        }
      } catch (e) {
        // 建议加载失败不阻塞主流程
        console.warn('加载风险建议失败', e)
      } finally {
        this.suggestionsLoading = false
      }
    },

    /** 保存评估：调用后端评估接口 */
    async handleSubmit() {
      if (!this.riskData.id) {
        this.$message.warning('缺少风险记录ID，无法评估')
        return
      }
      this.submitLoading = true
      try {
        const res = await assessFinancialRisk({
          id: this.riskData.id,
          adjustedRiskLevel: this.assessmentResult.adjustedRiskLevel,
          adjustmentReason: this.assessmentResult.adjustmentReason
        })
        if (res.result === 200 || res.code === 1) {
          // 更新评估结果展示
          if (res.data) {
            this.assessmentResult.totalScore = res.data.riskScore || this.assessmentResult.totalScore
            this.assessmentResult.adjustedRiskLevel = res.data.riskLevel || this.assessmentResult.adjustedRiskLevel
          }
          this.$message.success('风险评估保存成功')
          this.$emit('refresh')
          this.handleClose()
        } else {
          this.$message.error(res.msg || '评估保存失败')
        }
      } catch (e) {
        this.$message.error('评估保存失败，请稍后重试')
      } finally {
        this.submitLoading = false
      }
    },

    handleClose() {
      this.$emit('update:visible', false)
    },

    getRiskLevelTag(level) {
      const tagMap = { LOW: 'success', MEDIUM: 'warning', HIGH: 'danger', CRITICAL: 'danger' }
      return tagMap[level] || 'info'
    },

    getRiskLevelText(level) {
      const textMap = { LOW: '低风险', MEDIUM: '中风险', HIGH: '高风险', CRITICAL: '极高风险' }
      return textMap[level] || level
    },

    getIndicatorStatusTag(status) {
      const tagMap = { LOW: 'success', MEDIUM: 'warning', HIGH: 'danger' }
      return tagMap[status] || 'info'
    },

    getIndicatorStatusText(status) {
      const textMap = { LOW: '正常', MEDIUM: '预警', HIGH: '风险' }
      return textMap[status] || status
    },

    getPriorityTag(priority) {
      const tagMap = { HIGH: 'danger', MEDIUM: 'warning', LOW: 'info' }
      return tagMap[priority] || 'info'
    },

    getPriorityText(priority) {
      const textMap = { HIGH: '高', MEDIUM: '中', LOW: '低' }
      return textMap[priority] || priority
    },

    getScoreColor(score) {
      if (score <= 30) return '#67C23A'
      if (score <= 60) return '#E6A23C'
      if (score <= 80) return '#F56C6C'
      return '#C0392B'
    }
  }
}
</script>

<style scoped>
.assessment-content {
  min-height: 500px;
}

.indicators-panel h4 {
  margin-bottom: 15px;
  color: #303133;
}

.result-panel .result-card {
  height: 280px;
}

.result-content {
  padding: 20px 0;
}

.score-display {
  text-align: center;
  margin-bottom: 20px;
}

.score-value {
  font-size: 48px;
  font-weight: bold;
  color: #303133;
  line-height: 1;
}

.score-label {
  font-size: 14px;
  color: #909399;
  margin-top: 10px;
}

.mt-20 {
  margin-top: 20px;
}
</style>
