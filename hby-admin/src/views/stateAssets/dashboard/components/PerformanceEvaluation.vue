<template>
  <div class="performance-evaluation">
    <!-- 评估配置 -->
    <el-row :gutter="20" class="evaluation-config">
      <el-col :span="12">
        <el-card shadow="never">
          <div slot="header">评估周期</div>
          <el-select v-model="evaluationPeriod" placeholder="请选择评估周期" style="width: 100%">
            <el-option label="月度评估" value="月度" />
            <el-option label="季度评估" value="季度" />
            <el-option label="年度评估" value="年度" />
          </el-select>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="never">
          <div slot="header">评估维度</div>
          <el-select v-model="evaluationDimension" placeholder="请选择评估维度" style="width: 100%">
            <el-option label="财务绩效" value="财务绩效" />
            <el-option label="运营绩效" value="运营绩效" />
            <el-option label="创新绩效" value="创新绩效" />
            <el-option label="综合绩效" value="综合绩效" />
          </el-select>
        </el-card>
      </el-col>
    </el-row>

    <!-- AI 分析区域 -->
    <el-row v-if="evaluationPeriod && evaluationDimension" class="ai-analysis-section">
      <el-col :span="24">
        <el-card shadow="never">
          <div slot="header">
            <span>AI 绩效评估分析</span>
            <span v-if="isStreaming" class="streaming-tip">
              <i class="el-icon-loading"></i> AI 分析中...
            </span>
            <el-button v-if="!isStreaming && aiHtmlContent" type="text" @click="reAnalyze">
              <i class="el-icon-refresh"></i> 重新分析
            </el-button>
          </div>
          <div v-if="isLoading && !aiHtmlContent" class="ai-loading">
            <i class="el-icon-loading"></i>
            <span>正在生成绩效评估报告，请稍候...</span>
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
    <div v-if="!evaluationPeriod || !evaluationDimension" class="empty-state">
      <el-empty description="请选择评估周期和评估维度开始分析" />
    </div>
  </div>
</template>

<script>
import { chatStream } from '@/api/ai/starlight'

export default {
  name: 'PerformanceEvaluation',
  props: {
    enterpriseId: { type: String, default: '' },
    enterpriseName: { type: String, default: '' },
    moduleStatsData: { type: Object, default: () => ({}) }
  },
  data() {
    return {
      evaluationPeriod: '',
      evaluationDimension: '',
      isStreaming: false,
      isLoading: false,
      aiHtmlContent: '',
      aiRawContent: '',
      abortController: null,
      sessionId: 'performance-eval-' + Date.now()
    }
  },
  watch: {
    evaluationPeriod(val) {
      if (val && this.evaluationDimension) this.triggerAiAnalysis()
    },
    evaluationDimension(val) {
      if (val && this.evaluationPeriod) this.triggerAiAnalysis()
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
      const message = `请针对企业「${entName}」，以「${this.evaluationPeriod}」为周期、从「${this.evaluationDimension}」角度，做一份绩效评估分析报告。\n要求：用一个完整的 HTML 页面呈现，包含绩效总览数据卡片（综合/财务/运营/创新四个指数）、绩效趋势折线图（ECharts）、绩效等级分布饼图（ECharts）、企业绩效排名表格和改进建议，样式美观，使用蓝绿色主色调，页面需内联所有 CSS 和 JS（ECharts 使用 CDN）。\n【时间要求】所有图表的时间轴、数据标注、历史趋势必须以当前年份为基准，不得出现2024年或更早年份作为最新数据。\n输出格式严格遵守：<<<HTML5_START:${this.evaluationDimension}绩效评估报告>>> ... <<<HTML5_END>>>`

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
.performance-evaluation {
  .evaluation-config {
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
