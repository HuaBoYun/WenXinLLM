<template>
  <div class="intelligent-recommendation">
    <!-- 推荐配置 -->
    <el-row :gutter="20" class="recommendation-config">
      <el-col :span="12">
        <el-card shadow="never">
          <div slot="header">推荐类型</div>
          <el-select v-model="recommendationType" placeholder="请选择推荐类型" style="width: 100%">
            <el-option label="风险管控建议" value="风险管控" />
            <el-option label="投资决策建议" value="投资决策" />
            <el-option label="运营优化建议" value="运营优化" />
            <el-option label="政策应对建议" value="政策应对" />
          </el-select>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="never">
          <div slot="header">目标企业范围</div>
          <el-select v-model="targetEnterprise" placeholder="请选择目标范围" style="width: 100%">
            <el-option label="全部企业" value="全部企业" />
            <el-option label="高风险企业" value="高风险企业" />
            <el-option label="低效率企业" value="低效率企业" />
            <el-option label="特定企业" value="特定企业" />
          </el-select>
        </el-card>
      </el-col>
    </el-row>

    <!-- AI 分析区域 -->
    <el-row v-if="recommendationType && targetEnterprise" class="ai-analysis-section">
      <el-col :span="24">
        <el-card shadow="never">
          <div slot="header">
            <span>AI 智能推荐</span>
            <span v-if="isStreaming" class="streaming-tip">
              <i class="el-icon-loading"></i> AI 分析中...
            </span>
            <el-button v-if="!isStreaming && aiHtmlContent" type="text" @click="reAnalyze">
              <i class="el-icon-refresh"></i> 重新推荐
            </el-button>
          </div>
          <div v-if="isLoading && !aiHtmlContent" class="ai-loading">
            <i class="el-icon-loading"></i>
            <span>正在生成智能推荐方案，请稍候...</span>
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
    <div v-if="!recommendationType || !targetEnterprise" class="empty-state">
      <el-empty description="请选择推荐类型和目标企业范围开始获取智能推荐" />
    </div>
  </div>
</template>

<script>
import { chatStream } from '@/api/ai/starlight'

export default {
  name: 'IntelligentRecommendation',
  props: {
    enterpriseId: { type: String, default: '' },
    enterpriseName: { type: String, default: '' },
    moduleStatsData: { type: Object, default: () => ({}) }
  },
  data() {
    return {
      recommendationType: '',
      targetEnterprise: '',
      isStreaming: false,
      isLoading: false,
      aiHtmlContent: '',
      aiRawContent: '',
      abortController: null,
      sessionId: 'intelligent-rec-' + Date.now()
    }
  },
  watch: {
    recommendationType(val) {
      if (val && this.targetEnterprise) this.triggerAiAnalysis()
    },
    targetEnterprise(val) {
      if (val && this.recommendationType) this.triggerAiAnalysis()
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
      const message = `请针对企业「${entName}」及其「${this.targetEnterprise}」范围，生成一份「${this.recommendationType}」方向的智能推荐报告。\n要求：用一个完整的 HTML 页面呈现，包含推荐概览统计卡片（总数/紧急/重要/一般）、推荐建议列表（含紧急程度标签、置信度进度条、适用企业、预期效果、实施难度）、推荐采纳情况饼图（ECharts）、实施效果柱状图（ECharts），样式美观，使用蓝紫色主色调，页面需内联所有 CSS 和 JS（ECharts 使用 CDN）。\n【时间要求】所有图表的时间轴、数据标注、历史趋势必须以当前年份为基准，不得出现2024年或更早年份作为最新数据。\n输出格式严格遵守：<<<HTML5_START:${this.recommendationType}智能推荐报告>>> ... <<<HTML5_END>>>`

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
            this.$message.error('推荐失败：' + error)
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
.intelligent-recommendation {
  .recommendation-config {
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
