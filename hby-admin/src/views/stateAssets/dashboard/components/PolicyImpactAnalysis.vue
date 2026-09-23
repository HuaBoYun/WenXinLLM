<template>
  <div class="policy-impact-analysis">
    <!-- 政策选择区域 -->
    <el-row :gutter="20" class="policy-selector">
      <el-col :span="12">
        <el-card shadow="never" class="selector-card">
          <div slot="header">
            <span>政策类型选择</span>
          </div>
          <el-select v-model="selectedPolicyType" placeholder="请选择政策类型" style="width: 100%">
            <el-option
              v-for="type in policyTypes"
              :key="type.value"
              :label="type.label"
              :value="type.value"
            />
          </el-select>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="never" class="selector-card">
          <div slot="header">
            <span>分析维度</span>
          </div>
          <el-select v-model="analysisDimension" placeholder="请选择分析维度" style="width: 100%">
            <el-option label="财务影响" value="financial" />
            <el-option label="运营影响" value="operational" />
            <el-option label="合规影响" value="compliance" />
            <el-option label="战略影响" value="strategic" />
          </el-select>
        </el-card>
      </el-col>
    </el-row>

    <!-- AI 分析区域（自动触发） -->
    <el-row v-if="selectedPolicyType && analysisDimension" class="ai-analysis-section">
      <el-col :span="24">
        <el-card shadow="never">
          <div slot="header">
            <span>AI 政策影响分析</span>
            <span v-if="isStreaming" class="streaming-tip">
              <i class="el-icon-loading"></i> AI 分析中...
            </span>
            <el-button v-if="!isStreaming && aiHtmlContent" type="text" @click="reAnalyze">
              <i class="el-icon-refresh"></i> 重新分析
            </el-button>
          </div>

          <!-- 加载状态 -->
          <div v-if="isLoading && !aiHtmlContent" class="ai-loading">
            <i class="el-icon-loading"></i>
            <span>正在生成分析报告，请稍候...</span>
          </div>

          <!-- HTML 渲染区域 -->
          <iframe
            v-if="aiHtmlContent"
            :srcdoc="aiHtmlContent"
            frameborder="0"
            sandbox="allow-scripts allow-same-origin"
            class="ai-html-frame"
          ></iframe>
        </el-card>
      </el-col>
    </el-row>

    <!-- 空状态 -->
    <div v-else class="empty-state">
      <el-empty description="请选择政策类型和分析维度开始分析" />
    </div>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import { chatStream } from '@/api/ai/starlight'

const DOMAIN_NAMES = {
  invest: '投资', financial: '金融', procurement: '采购',
  military: '军品', overseas: '境外', industry: '行业',
  contract: '合同', accounting: '会计', finance: '财务',
  fund: '资金', salary: '薪酬', property: '产权'
}

export default {
  name: 'PolicyImpactAnalysis',
  props: {
    enterpriseId: { type: String, default: '' },
    enterpriseName: { type: String, default: '' },
    moduleStatsData: { type: Object, default: () => ({}) }
  },
  data() {
    return {
      loading: false,
      selectedPolicyType: '',
      selectedPolicy: '',
      analysisDimension: '',
      policyTypes: [
        { label: '国资改革政策', value: 'reform' },
        { label: '财税政策', value: 'tax' },
        { label: '金融政策', value: 'finance' },
        { label: '产业政策', value: 'industry' },
        { label: '环保政策', value: 'environment' }
      ],
      policies: [],
      trendData: null,
      impactOverview: [],
      riskAssessment: { level: 'medium', levelText: '中等风险', score: 65, factors: [], recommendations: [] },
      affectedEnterprises: [],
      // AI 流式对话相关
      isStreaming: false,
      isLoading: false,
      aiHtmlContent: '',
      aiRawContent: '',
      abortController: null,
      sessionId: 'policy-analysis-' + Date.now()
    }
  },
  computed: {
    filteredPolicies() {
      return this.policies.filter(policy => policy.type === this.selectedPolicyType)
    },
    // 从moduleStatsData推导总风险数
    totalRiskCount() {
      let sum = 0
      Object.values(this.moduleStatsData).forEach(stats => {
        if (!Array.isArray(stats)) return
        const s = stats.find(x => x.cls === 'danger')
        if (s && s.value !== '--') sum += (parseInt(s.value) || 0)
      })
      return sum
    },
    avgComplianceRate() {
      const rates = []
      Object.values(this.moduleStatsData).forEach(stats => {
        if (!Array.isArray(stats)) return
        const s = stats.find(x => x.cls === 'success')
        if (s && s.value !== '--') { const v = parseFloat(s.value); if (!isNaN(v)) rates.push(v) }
      })
      return rates.length ? (rates.reduce((a, b) => a + b, 0) / rates.length) : 90
    }
  },
  watch: {
    enterpriseId: {
      handler(val) { if (val) this.loadPolicyData() },
      immediate: true
    },
    selectedPolicyType(val) {
      if (val && this.analysisDimension) this.triggerAiAnalysis()
    },
    analysisDimension(val) {
      if (val && this.selectedPolicyType) this.triggerAiAnalysis()
    }
  },
  methods: {
    loadPolicyData() {
      // 接口暂未对接
    },

    // 获取政策类型和分析维度的中文标签
    _getPolicyTypeLabel(val) {
      const item = this.policyTypes.find(t => t.value === val)
      return item ? item.label : val
    },
    _getDimensionLabel(val) {
      const map = { financial: '财务影响', operational: '运营影响', compliance: '合规影响', strategic: '战略影响' }
      return map[val] || val
    },

    // 触发 AI 分析
    triggerAiAnalysis() {
      // 如果正在流式输出，先中断
      if (this.abortController) {
        this.abortController.abort()
        this.abortController = null
      }
      this.aiHtmlContent = ''
      this.aiRawContent = ''
      this.isStreaming = true
      this.isLoading = true

      const policyTypeLabel = this._getPolicyTypeLabel(this.selectedPolicyType)
      const dimensionLabel = this._getDimensionLabel(this.analysisDimension)
      const entName = this.enterpriseName || '目标企业'

      const message = `请针对企业「${entName}」，围绕「${policyTypeLabel}」政策，从「${dimensionLabel}」维度做一份政策影响分析报告。\n要求：用一个完整的 HTML 页面呈现，包含政策影响概览数据卡片、影响趋势折线图（ECharts）、企业分布饼图（ECharts）、风险评估说明和应对建议，样式美观，使用蓝色主色调，页面需内联所有 CSS 和 JS（ECharts 使用 CDN）。\n【时间要求】所有图表的时间轴、数据标注、历史趋势必须以当前年份为基准，横坐标最新时间节点为当前月份，不得出现2024年或更早年份作为最新数据。\n输出格式严格遵守：<<<HTML5_START:${policyTypeLabel}政策影响分析报告>>> ... <<<HTML5_END>>>`

      this.abortController = chatStream(
        {
          message,
          sessionId: this.sessionId,
          enableThinking: false,
          enableWebSearch: false,
          stream: true
        },
        {
          onMessage: (content) => {
            this.aiRawContent += content
            this._parseHtmlContent()
            this.isLoading = false
          },
          onError: (error) => {
            this.isLoading = false
            this.isStreaming = false
            this.$message.error('分析失败：' + error)
          },
          onComplete: () => {
            this.isLoading = false
            this.isStreaming = false
            this.abortController = null
            // 最后再解析一次确保完整
            this._parseHtmlContent()
          }
        }
      )
    },

    // 实时解析 HTML 内容
    _parseHtmlContent() {
      const content = this.aiRawContent
      const startTag = '<<<HTML5_START'
      const endTag = '<<<HTML5_END>>>'
      const startIndex = content.indexOf(startTag)
      if (startIndex === -1) return
      const htmlStartIndex = content.indexOf('>>>', startIndex) + 3
      if (htmlStartIndex <= startIndex) return
      const endIndex = content.indexOf(endTag)
      // 流式渲染：有结束标记取到结束，没有就取到当前末尾（实时增长）
      const htmlEndIndex = endIndex !== -1 ? endIndex : content.length
      this.aiHtmlContent = content.substring(htmlStartIndex, htmlEndIndex).trim()
    },

    // 重新分析
    reAnalyze() {
      this.aiHtmlContent = ''
      this.aiRawContent = ''
      this.triggerAiAnalysis()
    }
  }
}
</script>

<style lang="scss" scoped>
.policy-impact-analysis {
  .policy-selector {
    margin-bottom: 20px;

    .selector-card {
      .el-card__header {
        padding: 15px 20px;
        font-weight: bold;
      }
    }
  }

  .ai-analysis-section {
    .ai-loading {
      text-align: center;
      padding: 60px 0;
      color: #409EFF;
      font-size: 16px;
      i {
        font-size: 24px;
        margin-right: 8px;
        vertical-align: middle;
      }
      span {
        vertical-align: middle;
      }
    }

    .ai-html-frame {
      width: 100%;
      min-height: 700px;
      border: none;
      display: block;
    }

    .streaming-tip {
      color: #409EFF;
      font-size: 13px;
      margin-left: 12px;
      i {
        margin-right: 4px;
      }
    }
  }

  .empty-state {
    text-align: center;
    padding: 100px 0;
  }
}
</style>
