<template>
  <div class="risk-prediction-model">
    <!-- 模型选择区域 -->
    <el-row :gutter="20" class="model-selector">
      <el-col :span="12">
        <el-card shadow="never">
          <div slot="header">风险预测模型</div>
          <el-select v-model="selectedModel" placeholder="请选择预测模型" style="width: 100%">
            <el-option label="财务风险预测模型" value="财务风险" />
            <el-option label="运营风险预测模型" value="运营风险" />
            <el-option label="合规风险预测模型" value="合规风险" />
            <el-option label="市场风险预测模型" value="市场风险" />
          </el-select>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="never">
          <div slot="header">预测周期</div>
          <el-select v-model="predictionPeriod" placeholder="请选择预测周期" style="width: 100%">
            <el-option label="1个月" value="1个月" />
            <el-option label="3个月" value="3个月" />
            <el-option label="6个月" value="6个月" />
            <el-option label="1年" value="1年" />
          </el-select>
        </el-card>
      </el-col>
    </el-row>

    <!-- AI 分析区域 -->
    <el-row v-if="selectedModel && predictionPeriod" class="ai-analysis-section">
      <el-col :span="24">
        <el-card shadow="never">
          <div slot="header">
            <span>AI 风险预测分析</span>
            <span v-if="isStreaming" class="streaming-tip">
              <i class="el-icon-loading"></i> AI 分析中...
            </span>
            <el-button v-if="!isStreaming && aiHtmlContent" type="text" @click="reAnalyze">
              <i class="el-icon-refresh"></i> 重新分析
            </el-button>
          </div>
          <div v-if="isLoading && !aiHtmlContent" class="ai-loading">
            <i class="el-icon-loading"></i>
            <span>正在生成风险预测报告，请稍候...</span>
          </div>
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
    <div v-if="!selectedModel || !predictionPeriod" class="empty-state">
      <el-empty description="请选择预测模型和预测周期开始分析" />
    </div>
  </div>
</template>

<script>
import { chatStream } from '@/api/ai/starlight'

export default {
  name: 'RiskPredictionModel',
  props: {
    enterpriseId: { type: String, default: '' },
    enterpriseName: { type: String, default: '' },
    moduleStatsData: { type: Object, default: () => ({}) }
  },
  data() {
    return {
      selectedModel: '',
      predictionPeriod: '',
      isStreaming: false,
      isLoading: false,
      aiHtmlContent: '',
      aiRawContent: '',
      abortController: null,
      sessionId: 'risk-prediction-' + Date.now()
    }
  },
  watch: {
    selectedModel(val) {
      if (val && this.predictionPeriod) this.triggerAiAnalysis()
    },
    predictionPeriod(val) {
      if (val && this.selectedModel) this.triggerAiAnalysis()
    }
  },
  methods: {
    triggerAiAnalysis() {
      if (this.abortController) {
        this.abortController.abort()
        this.abortController = null
      }
      this.aiHtmlContent = ''
      this.aiRawContent = ''
      this.isStreaming = true
      this.isLoading = true

      const entName = this.enterpriseName || '目标企业'
      const message = `请针对企业「${entName}」，使用「${this.selectedModel}预测模型」，对未来「${this.predictionPeriod}」进行风险预测分析。\n要求：用一个完整的 HTML 页面呈现，包含风险等级分布饼图（ECharts）、风险趋势折线图（ECharts）、关键风险指标卡片、高风险预警企业列表和风险应对建议，样式美观，使用红橙蓝配色方案，页面需内联所有 CSS 和 JS（ECharts 使用 CDN）。\n【时间要求】所有图表的时间轴、数据标注、历史趋势必须以当前年份为基准，不得出现2024年或更早年份作为最新数据。\n输出格式严格遵守：<<<HTML5_START:${this.selectedModel}预测报告>>> ... <<<HTML5_END>>>`

      this.abortController = chatStream(
        { message, sessionId: this.sessionId, enableThinking: false, enableWebSearch: false, stream: true },
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
            this._parseHtmlContent()
          }
        }
      )
    },

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

    reAnalyze() {
      this.aiHtmlContent = ''
      this.aiRawContent = ''
      this.triggerAiAnalysis()
    }
  },
  beforeDestroy() {
    if (this.abortController) this.abortController.abort()
  }
}
</script>

<style lang="scss" scoped>
.risk-prediction-model {
  .model-selector {
    margin-bottom: 20px;
  }

  .ai-analysis-section {
    .ai-loading {
      text-align: center;
      padding: 60px 0;
      color: #409EFF;
      font-size: 16px;
      i { margin-right: 8px; font-size: 20px; }
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
    }
  }

  .empty-state {
    text-align: center;
    padding: 100px 0;
  }
}
</style>
