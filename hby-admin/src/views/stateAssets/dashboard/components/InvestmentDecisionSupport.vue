<template>
  <div class="investment-decision-support">
    <!-- 投资项目选择 -->
    <el-row :gutter="20" class="project-selector">
      <el-col :span="12">
        <el-card shadow="never">
          <div slot="header">投资项目类型</div>
          <el-select v-model="projectType" placeholder="请选择项目类型" style="width: 100%">
            <el-option label="基础设施建设" value="基础设施建设" />
            <el-option label="技术升级改造" value="技术升级改造" />
            <el-option label="并购重组" value="并购重组" />
            <el-option label="新兴产业投资" value="新兴产业投资" />
          </el-select>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="never">
          <div slot="header">投资规模</div>
          <el-select v-model="investmentScale" placeholder="请选择投资规模" style="width: 100%">
            <el-option label="小型项目（<1亿）" value="小型项目（<1亿）" />
            <el-option label="中型项目（1-10亿）" value="中型项目（1-10亿）" />
            <el-option label="大型项目（10-100亿）" value="大型项目（10-100亿）" />
            <el-option label="特大型项目（>100亿）" value="特大型项目（>100亿）" />
          </el-select>
        </el-card>
      </el-col>
    </el-row>

    <!-- AI 分析区域 -->
    <el-row v-if="projectType && investmentScale" class="ai-analysis-section">
      <el-col :span="24">
        <el-card shadow="never">
          <div slot="header">
            <span>AI 投资决策分析</span>
            <span v-if="isStreaming" class="streaming-tip">
              <i class="el-icon-loading"></i> AI 分析中...
            </span>
            <el-button v-if="!isStreaming && aiHtmlContent" type="text" @click="reAnalyze">
              <i class="el-icon-refresh"></i> 重新分析
            </el-button>
          </div>
          <div v-if="isLoading && !aiHtmlContent" class="ai-loading">
            <i class="el-icon-loading"></i>
            <span>正在生成投资决策报告，请稍候...</span>
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
    <div v-if="!projectType || !investmentScale" class="empty-state">
      <el-empty description="请选择投资项目类型和投资规模开始分析" />
    </div>
  </div>
</template>

<script>
import { chatStream } from '@/api/ai/starlight'

export default {
  name: 'InvestmentDecisionSupport',
  props: {
    enterpriseId: { type: String, default: '' },
    enterpriseName: { type: String, default: '' },
    moduleStatsData: { type: Object, default: () => ({}) }
  },
  data() {
    return {
      projectType: '',
      investmentScale: '',
      isStreaming: false,
      isLoading: false,
      aiHtmlContent: '',
      aiRawContent: '',
      abortController: null,
      sessionId: 'investment-decision-' + Date.now()
    }
  },
  watch: {
    projectType(val) {
      if (val && this.investmentScale) this.triggerAiAnalysis()
    },
    investmentScale(val) {
      if (val && this.projectType) this.triggerAiAnalysis()
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
      const message = `请针对企业「${entName}」，围绕「${this.projectType}」方向、规模为「${this.investmentScale}」的投资项目，做一份投资决策支持分析报告。\n要求：用一个完整的 HTML 页面呈现，包含投资收益预测折线图（ECharts，展示预期/保守/乐观三种情景）、风险评估雷达图（ECharts）、推荐项目列表（表格）、投资建议和决策要点，样式美观，使用绿色蓝色主色调，页面需内联所有 CSS 和 JS（ECharts 使用 CDN）。\n【时间要求】所有图表的时间轴、数据标注、历史趋势必须以当前年份为基准，不得出现2024年或更早年份作为最新数据。\n输出格式严格遵守：<<<HTML5_START:${this.projectType}投资决策报告>>> ... <<<HTML5_END>>>`

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
.investment-decision-support {
  .project-selector {
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
