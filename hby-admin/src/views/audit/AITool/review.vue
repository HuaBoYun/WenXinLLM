<template>
  <div class="review-page-container">
    <div class="split-layout">
      <!-- 左侧：审查结果 -->
      <div class="left-panel">
        <el-card shadow="never" class="result-card">
          <div class="panel-header">
            <span class="panel-title">审查结果</span>
          </div>

          <!-- 风险统计卡片 -->
          <div v-if="reviewResults.length > 0" class="risk-statistics">
            <el-card class="risk-card high-risk" shadow="hover">
              <div class="risk-title">高风险</div>
              <div class="risk-count">{{ highRiskCount }}项</div>
            </el-card>
            <el-card class="risk-card medium-risk" shadow="hover">
              <div class="risk-title">中风险</div>
              <div class="risk-count">{{ mediumRiskCount }}项</div>
            </el-card>
            <el-card class="risk-card low-risk" shadow="hover">
              <div class="risk-title">低风险</div>
              <div class="risk-count">{{ lowRiskCount }}项</div>
            </el-card>
          </div>

          <!-- 审查项详情列表 -->
          <div v-if="reviewResults.length > 0" class="review-items-container">
            <el-card
              v-for="(item, index) in reviewResults"
              :key="index"
              class="review-item-card"
              shadow="hover"
              :class="'risk-level-' + item.risklevel"
            >
              <div class="item-header">
                <span class="item-title">{{ item.reviewpointname }}</span>
              </div>

              <div class="item-content">
                <div class="content-row">
                  <span class="label">审查项:</span>
                  <span class="value">{{ item.reviewitemname }}</span>
                </div>

                <div class="content-row">
                  <span class="label">风险提示:</span>
                  <span class="value">{{ item.riskwarning }}</span>
                </div>

                <div class="content-row">
                  <span class="label">审查项说明:</span>
                  <span class="value">{{ item.itemdescription || '-' }}</span>
                </div>
              </div>

              <div class="item-footer">
                <span
                  class="risk-badge"
                  :class="getRiskLevelClass(item.risklevel)"
                  @click="locateInEditor(item)"
                >
                  <i class="el-icon-location"></i>
                  审查定位: {{ getRiskLevelText(item.risklevel) }}
                </span>
              </div>
            </el-card>
          </div>

          <!-- 空状态 -->
          <div v-else class="empty-state">
            <i class="el-icon-document"></i>
            <p>暂无审查结果</p>
            <p class="tip">请在右侧编辑器输入文档内容后，点击"启动一键审查"</p>
          </div>
        </el-card>
      </div>

      <!-- 右侧：文档编辑器 -->
      <div class="right-panel">
        <el-card shadow="never" class="editor-card">
          <div class="panel-header">
            <span class="panel-title">文档编辑</span>
            <div class="action-buttons">
              <el-button
                type="primary"
                icon="el-icon-check"
                size="small"
                :loading="loading"
                @click="startReview"
              >
                启动一键审查
              </el-button>
              <el-button size="small" @click="clearContent">清空内容</el-button>
            </div>
          </div>

          <!-- 编辑器区域 -->
          <div class="editor-section">
            <UEditor
              ref="ueditor"
              v-model="content"
              :height="editorHeight"
              style="width: 100%"
              placeholder="请在此输入或粘贴需要审查的文档内容..."
            />
          </div>
        </el-card>
      </div>
    </div>
  </div>
</template>

<script>
  import UEditor from '@/components/UEditor'

  export default {
    name: 'Review',
    components: {
      UEditor,
    },
    data() {
      return {
        content: '',
        loading: false,
        reviewResults: [],
        editorHeight: 600,
      }
    },
    computed: {
      // 高风险数量
      highRiskCount() {
        return this.reviewResults.filter((item) => item.risklevel === '01')
          .length
      },
      // 中风险数量
      mediumRiskCount() {
        return this.reviewResults.filter((item) => item.risklevel === '02')
          .length
      },
      // 低风险数量
      lowRiskCount() {
        return this.reviewResults.filter((item) => item.risklevel === '03')
          .length
      },
    },
    mounted() {
      // 计算编辑器高度
      this.calculateEditorHeight()
      window.addEventListener('resize', this.calculateEditorHeight)

      // 清理 UEditor 的 localStorage 缓存，避免配额溢出
      try {
        localStorage.removeItem('ueditor_preference')
        console.log('已清理 UEditor localStorage 缓存')
      } catch (e) {
        console.warn('清理 localStorage 失败:', e)
      }
    },
    beforeDestroy() {
      window.removeEventListener('resize', this.calculateEditorHeight)
    },
    methods: {
      // 计算编辑器高度
      calculateEditorHeight() {
        // 根据窗口高度动态计算编辑器高度
        const windowHeight = window.innerHeight
        this.editorHeight = windowHeight - 250
      },

      // 清空内容
      clearContent() {
        this.content = ''
        this.reviewResults = []
      },

      // 启动审查
      async startReview() {
        if (!this.content || this.content.trim() === '') {
          this.$message.warning('请先输入文档内容')
          return
        }

        this.loading = true
        try {
          // 解析编辑器内容,提取纯文本
          const contentList = this.extractTextContent(this.content)

          console.log('提取的内容列表:', contentList)
          console.log('内容列表长度:', contentList.length)

          const response = await fetch(
            'https://office.wenxin.example.com/api/app/doc/audit/',
            {
              method: 'POST',
              headers: {
                accept: '*/*',
                'content-type': 'application/json',
                'x-guesttoken':
                  'eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyIjp7InVzZXJuYW1lIjoiYWRtaW4iLCJsYXN0X25hbWUiOiJhZG1pbiIsImZpcnN0X25hbWUiOiJhZG1pbiJ9LCJyZXNvdXJjZXMiOlt7ImlkIjoiNCIsInR5cGUiOiJkYXNoYm9hcmQifV0sInJsc19ydWxlcyI6W10sImlhdCI6MTcxODY5NTQ5OS42NjcwNDEsImV4cCI6MTcxODY5ODQ5OS42NjcwNDEsImF1ZCI6Imh0dHA6Ly8wLjAuMC4wOjgwODAvIiwidHlwZSI6Imd1ZXN0In0.yWJrKnmlSO_CFkghyTL3Jp6jPfeC6Ip5etnsMwpfVgM',
              },
              body: JSON.stringify({
                contents_list: contentList,
              }),
            }
          )

          if (!response.ok) {
            throw new Error('审查请求失败')
          }

          const data = await response.json()

          if (data.results && Array.isArray(data.results)) {
            this.reviewResults = data.results
            this.$message.success(
              `审查完成，发现 ${data.results.length} 个问题`
            )
          } else {
            this.reviewResults = []
            this.$message.info('审查完成，未发现问题')
          }
        } catch (error) {
          console.error('审查失败:', error)
          this.$message.error('审查失败，请稍后重试')
        } finally {
          this.loading = false
        }
      },

      // 提取文本内容(去除HTML标签)
      extractTextContent(html) {
        if (!html) return []

        const div = document.createElement('div')
        div.innerHTML = html

        // 获取所有的段落元素(p, div, li等)和换行符(br)
        const elements = div.querySelectorAll(
          'p, div, li, h1, h2, h3, h4, h5, h6'
        )
        const contentList = []

        if (elements.length > 0) {
          // 如果有段落元素，提取每个段落的文本
          elements.forEach((el) => {
            const text = (el.textContent || el.innerText || '').trim()
            if (text) {
              contentList.push(text)
            }
          })
        } else {
          // 如果没有段落元素，按br标签或换行符分割
          const htmlWithLineBreaks = html
            .replace(/<br\s*\/?>/gi, '\n')
            .replace(/<\/p>/gi, '\n')
            .replace(/<\/div>/gi, '\n')
            .replace(/<\/li>/gi, '\n')

          const tempDiv = document.createElement('div')
          tempDiv.innerHTML = htmlWithLineBreaks
          const text = tempDiv.textContent || tempDiv.innerText || ''

          // 按换行符分割
          const lines = text
            .split('\n')
            .map((line) => line.trim())
            .filter((line) => line !== '')
          contentList.push(...lines)
        }

        // 如果还是没有内容，尝试直接获取文本并按换行分割
        if (contentList.length === 0) {
          const text = (div.textContent || div.innerText || '').trim()
          if (text) {
            const lines = text
              .split(/[\r\n]+/)
              .map((line) => line.trim())
              .filter((line) => line !== '')
            contentList.push(...lines)
          }
        }

        return contentList
      },

      // 获取风险等级类型
      getRiskLevelType(level) {
        const typeMap = {
          '01': 'danger', // 高风险
          '02': 'warning', // 中风险
          '03': 'info', // 低风险
        }
        return typeMap[level] || 'info'
      },

      // 获取风险等级文本
      getRiskLevelText(level) {
        const textMap = {
          '01': '高亮显示',
          '02': '高亮显示',
          '03': '高亮显示',
        }
        return textMap[level] || '高亮显示'
      },

      // 获取风险等级样式类名
      getRiskLevelClass(level) {
        const classMap = {
          '01': 'high',
          '02': 'medium',
          '03': 'low',
        }
        return classMap[level] || 'low'
      },

      // 在编辑器中定位并高亮显示
      locateInEditor(item) {
        const editor = this.$refs.ueditor?.editor
        if (!editor) {
          this.$message.warning('编辑器未就绪')
          return
        }

        // 获取要查找的关键词
        let searchText = item.reviewitemname || item.reviewpointname

        // 如果有false_list，优先使用第一个问题内容作为搜索关键词
        if (item.false_list && item.false_list.length > 0) {
          searchText = item.false_list[0]
        }

        if (!searchText) {
          this.$message.warning('无法定位，未找到相关文本')
          return
        }

        try {
          // 先清除之前的高亮
          this.clearHighlight()

          // 获取编辑器HTML内容
          let htmlContent = editor.getContent()

          // 获取纯文本内容
          const textContent = editor.getContentTxt()

          console.log('查找文本:', searchText)
          console.log('文档纯文本长度:', textContent.length)

          // 文本规范化：移除所有空白字符
          const normalizeText = (text) => {
            return text.replace(/[\s\r\n\t]+/g, '').trim()
          }

          const normalizedSearch = normalizeText(searchText)
          const normalizedTextContent = normalizeText(textContent)

          console.log('规范化搜索文本长度:', normalizedSearch.length)

          // 在规范化文本中查找
          const textIndex = normalizedTextContent.indexOf(normalizedSearch)

          if (textIndex !== -1) {
            console.log('在规范化文本中找到匹配')

            // 使用更简单高效的方法：基于字符匹配而非正则
            const highlightColor = this.getHighlightColor(item.risklevel)
            const result = this.findAndHighlightInHtml(
              htmlContent,
              searchText,
              highlightColor
            )

            if (result.success) {
              // 禁用自动保存，避免 localStorage 配额溢出
              const autoSave = editor.getOpt('autoSave')
              editor.setOpt('autoSave', false)

              try {
                editor.setContent(result.html)

                // 滚动到高亮位置
                setTimeout(() => {
                  const highlightElement =
                    editor.document.querySelector('.audit-highlight')
                  if (highlightElement) {
                    highlightElement.scrollIntoView({
                      behavior: 'smooth',
                      block: 'center',
                    })
                    this.$message.success('已定位到相关内容')
                  } else {
                    this.$message.warning('高亮失败，请手动查找')
                  }
                }, 100)
              } finally {
                // 恢复自动保存设置
                editor.setOpt('autoSave', autoSave)
              }
            } else {
              this.$message.warning('内容已找到但无法精确定位，请手动查找')
              console.log('无法在HTML中定位')
            }
          } else {
            // 尝试部分匹配
            const partialLength = Math.min(20, searchText.length)
            const partialSearch = normalizeText(
              searchText.substring(0, partialLength)
            )
            const partialIndex = normalizedTextContent.indexOf(partialSearch)

            if (partialIndex !== -1) {
              this.$message.warning(
                `未找到完整匹配，但找到部分内容（前${partialLength}字符）`
              )
            } else {
              this.$message.error(
                `未在文档中找到相关文本："${searchText.substring(0, 30)}${
                  searchText.length > 30 ? '...' : ''
                }"`
              )
              console.log('搜索文本:', searchText)
              console.log('文档开头:', textContent.substring(0, 100))
            }
          }
        } catch (error) {
          console.error('定位失败:', error)
          this.$message.error('定位失败: ' + error.message)
        }
      },

      // 在HTML中查找并高亮文本（避免使用超长正则）
      findAndHighlightInHtml(html, searchText, highlightColor) {
        try {
          // 规范化函数
          const normalize = (text) => text.replace(/[\s\r\n\t]+/g, '')

          const normalizedSearch = normalize(searchText)
          const searchLength = normalizedSearch.length

          console.log('开始在HTML中查找')
          console.log('搜索文本:', searchText.substring(0, 50))
          console.log('规范化后长度:', searchLength)

          // 先尝试简单字符串匹配
          if (html.includes(searchText)) {
            console.log('✓ 直接字符串匹配成功')
            const index = html.indexOf(searchText)
            const highlightHtml = `<span style="background-color: ${highlightColor}; padding: 2px 4px; border-radius: 2px;" class="audit-highlight">${searchText}</span>`
            const newHtml =
              html.substring(0, index) +
              highlightHtml +
              html.substring(index + searchText.length)
            return { success: true, html: newHtml }
          }

          console.log('直接匹配失败，尝试跨标签匹配')

          // 使用DOM解析HTML，正确处理HTML实体（如 &nbsp;）
          const tempDiv = document.createElement('div')
          tempDiv.innerHTML = html

          // 获取真实的纯文本（会自动解析HTML实体）
          const domPureText = (
            tempDiv.textContent ||
            tempDiv.innerText ||
            ''
          ).trim()
          const normalizedDomText = normalize(domPureText)

          console.log('DOM纯文本长度:', normalizedDomText.length)
          console.log('DOM纯文本前50字符:', normalizedDomText.substring(0, 50))

          // 在DOM纯文本中查找
          const domIndex = normalizedDomText.indexOf(normalizedSearch)

          if (domIndex !== -1) {
            console.log('✓ 在DOM纯文本中找到匹配，位置:', domIndex)

            // 现在需要在原始HTML中找到对应的位置
            // 策略：逐字符遍历HTML，跳过标签，构建位置映射
            const textChars = [] // 纯文本字符数组
            const htmlPositions = [] // 对应的HTML起始位置
            let inTag = false
            let entityBuffer = ''
            let entityStartPos = -1

            for (let i = 0; i < html.length; i++) {
              const char = html[i]

              if (char === '<') {
                inTag = true
                continue
              } else if (char === '>') {
                inTag = false
                continue
              }

              if (inTag) {
                continue
              }

              // 处理HTML实体（如 &nbsp; &#160; 等）
              if (char === '&') {
                entityBuffer = '&'
                entityStartPos = i
                continue
              } else if (entityBuffer) {
                entityBuffer += char
                if (char === ';') {
                  // 实体结束，解析它
                  const div = document.createElement('div')
                  div.innerHTML = entityBuffer
                  const decoded = div.textContent || div.innerText || ''

                  // 处理解码后的每个字符
                  for (let j = 0; j < decoded.length; j++) {
                    const decodedChar = decoded[j]
                    if (!/[\s\r\n\t]/.test(decodedChar)) {
                      textChars.push(decodedChar)
                      htmlPositions.push(entityStartPos)
                    }
                  }

                  entityBuffer = ''
                  entityStartPos = -1
                  continue
                } else if (entityBuffer.length > 10) {
                  // 不是实体，添加之前的字符
                  for (let j = 0; j < entityBuffer.length - 1; j++) {
                    const c = entityBuffer[j]
                    if (!/[\s\r\n\t]/.test(c)) {
                      textChars.push(c)
                      htmlPositions.push(entityStartPos + j)
                    }
                  }
                  entityBuffer = ''
                  entityStartPos = -1
                  // 继续处理当前字符
                  if (!/[\s\r\n\t]/.test(char)) {
                    textChars.push(char)
                    htmlPositions.push(i)
                  }
                }
                continue
              }

              // 普通字符
              if (!/[\s\r\n\t]/.test(char)) {
                textChars.push(char)
                htmlPositions.push(i)
              }
            }

            const pureText = textChars.join('')
            console.log('映射表纯文本长度:', pureText.length)
            console.log('映射表纯文本前50字符:', pureText.substring(0, 50))

            // 在映射表纯文本中查找
            const index = pureText.indexOf(normalizedSearch)

            if (index !== -1) {
              console.log('✓ 在映射表中找到匹配，位置:', index)

              // 获取匹配在HTML中的起始和结束位置
              const htmlStart = htmlPositions[index]
              const htmlEnd =
                htmlPositions[
                  Math.min(index + searchLength - 1, htmlPositions.length - 1)
                ]

              console.log('HTML位置:', htmlStart, '到', htmlEnd)

              // 提取完整的HTML片段（包含标签和空格）
              let start = htmlStart
              let end = htmlEnd + 1

              // 确保包含完整的实体
              while (
                start > 0 &&
                html[start - 1] !== '>' &&
                html[start - 1] !== ' '
              ) {
                start--
              }
              while (
                end < html.length &&
                html[end] !== '<' &&
                html[end] !== ' '
              ) {
                end++
              }

              const matchedHtml = html.substring(start, end)
              console.log('提取的HTML片段长度:', matchedHtml.length)
              console.log('HTML片段预览:', matchedHtml.substring(0, 100))

              const highlightHtml = `<span style="background-color: ${highlightColor}; padding: 2px 4px; border-radius: 2px;" class="audit-highlight">${matchedHtml}</span>`

              const newHtml =
                html.substring(0, start) + highlightHtml + html.substring(end)

              console.log('✓ 生成新HTML成功')
              return { success: true, html: newHtml }
            }
          }

          console.log('✗ 所有匹配方法都失败')
          console.log('规范化搜索文本:', normalizedSearch.substring(0, 50))
          console.log(
            'DOM纯文本前100字符:',
            normalizedDomText.substring(0, 100)
          )
          return { success: false, html: html }
        } catch (error) {
          console.error('高亮处理失败:', error)
          return { success: false, html: html }
        }
      },

      // 清除编辑器中的高亮
      clearHighlight() {
        const editor = this.$refs.ueditor?.editor
        if (!editor) return

        try {
          let content = editor.getContent()
          // 移除所有高亮标记
          content = content.replace(
            /<span[^>]*class="audit-highlight"[^>]*>(.*?)<\/span>/g,
            '$1'
          )

          // 禁用自动保存，避免 localStorage 配额溢出
          const autoSave = editor.getOpt('autoSave')
          editor.setOpt('autoSave', false)

          try {
            editor.setContent(content)
          } finally {
            editor.setOpt('autoSave', autoSave)
          }
        } catch (error) {
          console.error('清除高亮失败:', error)
        }
      },

      // 根据风险等级获取高亮颜色
      getHighlightColor(level) {
        const colorMap = {
          '01': '#ffcccc', // 高风险 - 浅红色
          '02': '#ffe4b5', // 中风险 - 浅橙色
          '03': '#fffacd', // 低风险 - 浅黄色
        }
        return colorMap[level] || '#fffacd'
      },
    },
  }
</script>

<style scoped lang="scss">
  .review-page-container {
    padding: 10px;
    height: calc(100vh - 120px);

    .split-layout {
      display: flex;
      gap: 15px;
      height: 100%;

      .left-panel,
      .right-panel {
        flex: 1;
        height: 100%;
        overflow: hidden;
      }

      .left-panel {
        flex: 0 0 45%;
        max-width: 45%;
      }

      .right-panel {
        flex: 0 0 55%;
        max-width: 55%;
      }
    }

    .result-card,
    .editor-card {
      height: 100%;
      display: flex;
      flex-direction: column;

      ::v-deep .el-card__body {
        flex: 1;
        display: flex;
        flex-direction: column;
        overflow: hidden;
        padding: 15px;
      }
    }

    .panel-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 15px;
      padding-bottom: 10px;
      border-bottom: 2px solid #ebeef5;
      height: 45px;

      .panel-title {
        font-size: 18px;
        font-weight: bold;
        color: #303133;
      }

      .action-buttons {
        display: flex;
        gap: 10px;
      }
    }

    .editor-section {
      flex: 1;
      overflow: hidden;
    }

    // 空状态样式
    .empty-state {
      flex: 1;
      display: flex;
      flex-direction: column;
      justify-content: center;
      align-items: center;
      color: #909399;

      i {
        font-size: 64px;
        margin-bottom: 20px;
        color: #c0c4cc;
      }

      p {
        margin: 5px 0;
        font-size: 14px;

        &.tip {
          font-size: 12px;
          color: #c0c4cc;
        }
      }
    }

    // 风险统计卡片样式
    .risk-statistics {
      display: flex;
      gap: 12px;
      margin-bottom: 20px;

      .risk-card {
        flex: 1;
        text-align: center;
        padding: 15px 10px;
        border-radius: 6px;
        cursor: pointer;
        transition: all 0.3s;

        &:hover {
          transform: translateY(-3px);
        }

        .risk-title {
          font-size: 14px;
          margin-bottom: 8px;
          font-weight: 500;
        }

        .risk-count {
          font-size: 22px;
          font-weight: bold;
        }

        &.high-risk {
          background: linear-gradient(135deg, #ffb3ba 0%, #ff8a95 100%);

          .risk-title,
          .risk-count {
            color: #721c24;
          }
        }

        &.medium-risk {
          background: linear-gradient(135deg, #ffd89b 0%, #ffb347 100%);

          .risk-title,
          .risk-count {
            color: #7d4e00;
          }
        }

        &.low-risk {
          background: linear-gradient(135deg, #fff4a3 0%, #ffd700 100%);

          .risk-title,
          .risk-count {
            color: #6b5600;
          }
        }
      }
    }

    // 审查项详情卡片容器
    .review-items-container {
      flex: 1;
      overflow-y: auto;
      overflow-x: hidden;
      padding-right: 10px;

      &::-webkit-scrollbar {
        width: 8px;
      }

      &::-webkit-scrollbar-track {
        background: #f1f1f1;
        border-radius: 4px;
      }

      &::-webkit-scrollbar-thumb {
        background: #888;
        border-radius: 4px;

        &:hover {
          background: #555;
        }
      }

      .review-item-card {
        margin-bottom: 20px;
        border-left: 4px solid #e0e0e0;
        transition: all 0.3s;

        &:hover {
          box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
        }

        &.risk-level-01 {
          border-left-color: #ff8a95;
          background: linear-gradient(to right, #fff0f2 0%, #ffffff 100%);
        }

        &.risk-level-02 {
          border-left-color: #ffb347;
          background: linear-gradient(to right, #fff8ed 0%, #ffffff 100%);
        }

        &.risk-level-03 {
          border-left-color: #ffd700;
          background: linear-gradient(to right, #fffef5 0%, #ffffff 100%);
        }

        .item-header {
          margin-bottom: 15px;
          padding-bottom: 10px;
          border-bottom: 1px solid #ebeef5;

          .item-title {
            font-size: 18px;
            font-weight: bold;
            color: #303133;
          }
        }

        .item-content {
          .content-row {
            margin-bottom: 12px;
            line-height: 1.8;

            &:last-child {
              margin-bottom: 0;
            }

            .label {
              font-weight: 500;
              color: #606266;
              margin-right: 8px;
            }

            .value {
              color: #303133;
            }
          }
        }

        .item-footer {
          margin-top: 15px;
          padding-top: 10px;
          border-top: 1px solid #ebeef5;
          text-align: right;

          .risk-badge {
            padding: 4px 12px;
            border-radius: 4px;
            font-size: 12px;
            font-weight: 500;
            cursor: pointer;
            transition: all 0.3s;
            display: inline-flex;
            align-items: center;
            gap: 4px;

            i {
              font-size: 13px;
            }

            &:hover {
              transform: translateY(-2px);
              box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
            }

            &:active {
              transform: translateY(0);
            }

            &.high {
              background: #ff6b6b;
              color: #ffffff;

              &:hover {
                background: #ff5252;
              }
            }

            &.medium {
              background: #ff8c00;
              color: #ffffff;

              &:hover {
                background: #ff7300;
              }
            }

            &.low {
              background: #ffd700;
              color: #6b5600;

              &:hover {
                background: #ffc700;
              }
            }
          }
        }
      }
    }
  }
</style>
