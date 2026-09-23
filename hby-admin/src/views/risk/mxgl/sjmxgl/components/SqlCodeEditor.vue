<template>
  <div class="sql-code-editor">
    <div class="editor-container">
      <!-- 工具栏 -->
      <div class="editor-toolbar">
        <div class="toolbar-left">
          <el-button size="mini" @click="formatCode">
            <i class="el-icon-magic-stick"></i> 格式化
          </el-button>
          <el-button size="mini" @click="toggleFullscreen">
            <i class="el-icon-full-screen"></i> 全屏
          </el-button>
          <el-button size="mini" @click="insertTemplate">
            <i class="el-icon-document-add"></i> 模板
          </el-button>
        </div>
        
        <div class="toolbar-right">
          <span class="editor-info">
            行: {{ currentLine }} | 列: {{ currentColumn }} | 长度: {{ sqlContent.length }}
          </span>
        </div>
      </div>

      <!-- 代码编辑器 -->
      <div class="code-editor-wrapper" :class="{ fullscreen: isFullscreen }">
        <textarea
          ref="codeEditor"
          v-model="sqlContent"
          class="code-editor"
          placeholder="请输入SQL语句..."
          @input="handleInput"
          @keydown="handleKeydown"
          @scroll="handleScroll"
          @click="updateCursorPosition"
          @keyup="updateCursorPosition"
        />
        
        <!-- 行号 -->
        <div class="line-numbers" ref="lineNumbers">
          <div
            v-for="n in lineCount"
            :key="n"
            class="line-number"
          >
            {{ n }}
          </div>
        </div>
        
        <!-- 语法高亮层 -->
        <div class="syntax-highlight" ref="syntaxHighlight">
          <pre v-html="highlightedCode"></pre>
        </div>
      </div>

      <!-- 自动补全提示 -->
      <div
        v-if="showSuggestions"
        class="suggestions-popup"
        :style="suggestionStyle"
      >
        <div
          v-for="(suggestion, index) in filteredSuggestions"
          :key="index"
          class="suggestion-item"
          :class="{ active: selectedSuggestionIndex === index }"
          @click="applySuggestion(suggestion)"
        >
          <i :class="suggestion.icon"></i>
          <span class="suggestion-text">{{ suggestion.text }}</span>
          <span class="suggestion-type">{{ suggestion.type }}</span>
        </div>
      </div>
    </div>

    <!-- SQL模板选择对话框 -->
    <el-dialog
      title="SQL模板"
      :visible.sync="templateDialogVisible"
      width="600px"
      append-to-body
    >
      <div class="template-list">
        <div
          v-for="template in sqlTemplates"
          :key="template.id"
          class="template-item"
          @click="selectTemplate(template)"
        >
          <div class="template-header">
            <h4>{{ template.name }}</h4>
            <el-tag size="mini">{{ template.category }}</el-tag>
          </div>
          <div class="template-description">
            {{ template.description }}
          </div>
          <div class="template-preview">
            <pre>{{ template.sql }}</pre>
          </div>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'SqlCodeEditor',
  props: {
    value: {
      type: String,
      default: ''
    },
    dataSourceId: {
      type: String,
      default: ''
    },
    readonly: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      sqlContent: '',
      isFullscreen: false,
      templateDialogVisible: false,
      
      // 光标位置
      currentLine: 1,
      currentColumn: 1,
      
      // 自动补全
      showSuggestions: false,
      suggestions: [],
      selectedSuggestionIndex: 0,
      suggestionStyle: {},
      
      // SQL关键字
      sqlKeywords: [
        'SELECT', 'FROM', 'WHERE', 'GROUP BY', 'ORDER BY', 'HAVING',
        'INSERT', 'UPDATE', 'DELETE', 'CREATE', 'ALTER', 'DROP',
        'JOIN', 'LEFT JOIN', 'RIGHT JOIN', 'INNER JOIN', 'OUTER JOIN',
        'UNION', 'UNION ALL', 'DISTINCT', 'AS', 'AND', 'OR', 'NOT',
        'IN', 'EXISTS', 'BETWEEN', 'LIKE', 'IS NULL', 'IS NOT NULL',
        'COUNT', 'SUM', 'AVG', 'MAX', 'MIN', 'CASE', 'WHEN', 'THEN', 'ELSE', 'END'
      ],
      
      // SQL函数
      sqlFunctions: [
        'COUNT', 'SUM', 'AVG', 'MAX', 'MIN', 'UPPER', 'LOWER', 'TRIM',
        'SUBSTRING', 'LENGTH', 'CONCAT', 'REPLACE', 'NOW', 'DATE_FORMAT',
        'YEAR', 'MONTH', 'DAY', 'HOUR', 'MINUTE', 'SECOND'
      ],
      
      // SQL模板
      sqlTemplates: [
        {
          id: 1,
          name: '基础查询',
          category: '查询',
          description: '基本的SELECT查询模板',
          sql: 'SELECT column1, column2\nFROM table_name\nWHERE condition;'
        },
        {
          id: 2,
          name: '聚合查询',
          category: '查询',
          description: '带聚合函数的查询模板',
          sql: 'SELECT column1, COUNT(*) as count\nFROM table_name\nGROUP BY column1\nHAVING COUNT(*) > 1;'
        },
        {
          id: 3,
          name: '连接查询',
          category: '查询',
          description: '多表连接查询模板',
          sql: 'SELECT t1.column1, t2.column2\nFROM table1 t1\nLEFT JOIN table2 t2 ON t1.id = t2.table1_id\nWHERE t1.status = \'active\';'
        }
      ]
    }
  },
  computed: {
    lineCount() {
      return this.sqlContent.split('\n').length
    },
    
    highlightedCode() {
      return this.highlightSQL(this.sqlContent)
    },
    
    filteredSuggestions() {
      return this.suggestions.slice(0, 10) // 最多显示10个建议
    }
  },
  watch: {
    value: {
      handler(val) {
        this.sqlContent = val || ''
      },
      immediate: true
    },
    sqlContent(val) {
      this.$emit('input', val)
      this.$emit('change', val)
    }
  },
  mounted() {
    this.initEditor()
  },
  methods: {
    // 初始化编辑器
    initEditor() {
      this.updateCursorPosition()
      this.syncScroll()
    },

    // 处理输入
    handleInput(event) {
      this.updateCursorPosition()
      this.checkAutoComplete()
    },

    // 处理按键
    handleKeydown(event) {
      // Tab键缩进
      if (event.key === 'Tab') {
        event.preventDefault()
        this.insertText('  ') // 插入两个空格
        return
      }
      
      // 自动补全快捷键
      if (event.ctrlKey && event.key === ' ') {
        event.preventDefault()
        this.showAutoComplete()
        return
      }
      
      // 处理自动补全选择
      if (this.showSuggestions) {
        if (event.key === 'ArrowDown') {
          event.preventDefault()
          this.selectedSuggestionIndex = Math.min(
            this.selectedSuggestionIndex + 1,
            this.filteredSuggestions.length - 1
          )
        } else if (event.key === 'ArrowUp') {
          event.preventDefault()
          this.selectedSuggestionIndex = Math.max(this.selectedSuggestionIndex - 1, 0)
        } else if (event.key === 'Enter') {
          event.preventDefault()
          this.applySuggestion(this.filteredSuggestions[this.selectedSuggestionIndex])
        } else if (event.key === 'Escape') {
          this.hideSuggestions()
        }
      }
    },

    // 处理滚动
    handleScroll() {
      this.syncScroll()
    },

    // 同步滚动
    syncScroll() {
      const editor = this.$refs.codeEditor
      const lineNumbers = this.$refs.lineNumbers
      const syntaxHighlight = this.$refs.syntaxHighlight
      
      if (editor && lineNumbers) {
        lineNumbers.scrollTop = editor.scrollTop
      }
      if (editor && syntaxHighlight) {
        syntaxHighlight.scrollTop = editor.scrollTop
        syntaxHighlight.scrollLeft = editor.scrollLeft
      }
    },

    // 更新光标位置
    updateCursorPosition() {
      const editor = this.$refs.codeEditor
      if (!editor) return
      
      const cursorPos = editor.selectionStart
      const textBeforeCursor = this.sqlContent.substring(0, cursorPos)
      const lines = textBeforeCursor.split('\n')
      
      this.currentLine = lines.length
      this.currentColumn = lines[lines.length - 1].length + 1
    },

    // 插入文本
    insertText(text) {
      const editor = this.$refs.codeEditor
      const start = editor.selectionStart
      const end = editor.selectionEnd
      
      const before = this.sqlContent.substring(0, start)
      const after = this.sqlContent.substring(end)
      
      this.sqlContent = before + text + after
      
      this.$nextTick(() => {
        editor.selectionStart = editor.selectionEnd = start + text.length
        editor.focus()
      })
    },

    // 检查自动补全
    checkAutoComplete() {
      const editor = this.$refs.codeEditor
      const cursorPos = editor.selectionStart
      const textBeforeCursor = this.sqlContent.substring(0, cursorPos)
      const words = textBeforeCursor.split(/\s+/)
      const currentWord = words[words.length - 1]
      
      if (currentWord.length >= 2) {
        this.showAutoComplete(currentWord)
      } else {
        this.hideSuggestions()
      }
    },

    // 显示自动补全
    showAutoComplete(prefix = '') {
      this.suggestions = []
      
      // 添加关键字建议
      this.sqlKeywords.forEach(keyword => {
        if (keyword.toLowerCase().startsWith(prefix.toLowerCase())) {
          this.suggestions.push({
            text: keyword,
            type: 'keyword',
            icon: 'el-icon-key'
          })
        }
      })
      
      // 添加函数建议
      this.sqlFunctions.forEach(func => {
        if (func.toLowerCase().startsWith(prefix.toLowerCase())) {
          this.suggestions.push({
            text: func + '()',
            type: 'function',
            icon: 'el-icon-cpu'
          })
        }
      })
      
      if (this.suggestions.length > 0) {
        this.showSuggestions = true
        this.selectedSuggestionIndex = 0
        this.updateSuggestionPosition()
      } else {
        this.hideSuggestions()
      }
    },

    // 隐藏建议
    hideSuggestions() {
      this.showSuggestions = false
      this.suggestions = []
      this.selectedSuggestionIndex = 0
    },

    // 更新建议位置
    updateSuggestionPosition() {
      const editor = this.$refs.codeEditor
      const rect = editor.getBoundingClientRect()
      
      // 简单的位置计算，实际应该更精确
      this.suggestionStyle = {
        left: rect.left + 'px',
        top: (rect.top + 20) + 'px'
      }
    },

    // 应用建议
    applySuggestion(suggestion) {
      const editor = this.$refs.codeEditor
      const cursorPos = editor.selectionStart
      const textBeforeCursor = this.sqlContent.substring(0, cursorPos)
      const words = textBeforeCursor.split(/\s+/)
      const currentWord = words[words.length - 1]
      
      // 替换当前单词
      const start = cursorPos - currentWord.length
      const before = this.sqlContent.substring(0, start)
      const after = this.sqlContent.substring(cursorPos)
      
      this.sqlContent = before + suggestion.text + after
      
      this.$nextTick(() => {
        const newPos = start + suggestion.text.length
        editor.selectionStart = editor.selectionEnd = newPos
        editor.focus()
      })
      
      this.hideSuggestions()
    },

    // SQL语法高亮
    highlightSQL(sql) {
      let highlighted = sql
      
      // 高亮关键字
      this.sqlKeywords.forEach(keyword => {
        const regex = new RegExp(`\\b${keyword}\\b`, 'gi')
        highlighted = highlighted.replace(regex, `<span class="sql-keyword">${keyword}</span>`)
      })
      
      // 高亮字符串
      highlighted = highlighted.replace(/'([^']*)'/g, '<span class="sql-string">\'$1\'</span>')
      
      // 高亮数字
      highlighted = highlighted.replace(/\b\d+\b/g, '<span class="sql-number">$&</span>')
      
      // 高亮注释
      highlighted = highlighted.replace(/--.*$/gm, '<span class="sql-comment">$&</span>')
      
      return highlighted
    },

    // 格式化代码
    formatCode() {
      if (!this.sqlContent.trim()) return
      
      // 简单的SQL格式化
      let formatted = this.sqlContent
        .replace(/\s+/g, ' ')
        .replace(/\s*,\s*/g, ',\n  ')
        .replace(/\s*(SELECT|FROM|WHERE|GROUP BY|ORDER BY|HAVING|JOIN|LEFT JOIN|RIGHT JOIN|INNER JOIN)\s+/gi, '\n$1 ')
        .replace(/\s*AND\s+/gi, '\n  AND ')
        .replace(/\s*OR\s+/gi, '\n  OR ')
        .trim()
      
      this.sqlContent = formatted
      this.$message.success('代码格式化完成')
    },

    // 切换全屏
    toggleFullscreen() {
      this.isFullscreen = !this.isFullscreen
    },

    // 插入模板
    insertTemplate() {
      this.templateDialogVisible = true
    },

    // 选择模板
    selectTemplate(template) {
      this.sqlContent = template.sql
      this.templateDialogVisible = false
      this.$message.success('模板已插入')
    }
  }
}
</script>

<style scoped>
.sql-code-editor {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.editor-container {
  flex: 1;
  position: relative;
  border: 1px solid #e4e7ed;
  border-radius: 4px;
  background: #fff;
}

.editor-toolbar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 8px 12px;
  border-bottom: 1px solid #e4e7ed;
  background: #f8f9fa;
}

.toolbar-left {
  display: flex;
  gap: 8px;
}

.editor-info {
  font-size: 12px;
  color: #666;
}

.code-editor-wrapper {
  position: relative;
  height: calc(100% - 45px);
  overflow: hidden;
}

.code-editor-wrapper.fullscreen {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  z-index: 9999;
  height: 100vh;
  background: #fff;
}

.code-editor {
  width: 100%;
  height: 100%;
  padding: 12px 12px 12px 60px;
  border: none;
  outline: none;
  font-family: 'Courier New', monospace;
  font-size: 14px;
  line-height: 1.5;
  resize: none;
  background: transparent;
  color: transparent;
  caret-color: #333;
  position: relative;
  z-index: 2;
}

.line-numbers {
  position: absolute;
  top: 0;
  left: 0;
  width: 50px;
  height: 100%;
  padding: 12px 8px;
  background: #f8f9fa;
  border-right: 1px solid #e4e7ed;
  font-family: 'Courier New', monospace;
  font-size: 14px;
  line-height: 1.5;
  color: #999;
  text-align: right;
  overflow: hidden;
  z-index: 1;
}

.line-number {
  height: 21px;
}

.syntax-highlight {
  position: absolute;
  top: 0;
  left: 60px;
  right: 0;
  height: 100%;
  padding: 12px;
  font-family: 'Courier New', monospace;
  font-size: 14px;
  line-height: 1.5;
  overflow: hidden;
  pointer-events: none;
  z-index: 1;
}

.syntax-highlight pre {
  margin: 0;
  white-space: pre-wrap;
  word-break: break-all;
}

.suggestions-popup {
  position: fixed;
  background: #fff;
  border: 1px solid #e4e7ed;
  border-radius: 4px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  max-height: 200px;
  overflow-y: auto;
  z-index: 1000;
}

.suggestion-item {
  display: flex;
  align-items: center;
  padding: 8px 12px;
  cursor: pointer;
  transition: background-color 0.2s;
}

.suggestion-item:hover,
.suggestion-item.active {
  background: #f0f9ff;
}

.suggestion-text {
  flex: 1;
  margin-left: 8px;
}

.suggestion-type {
  font-size: 12px;
  color: #999;
}

.template-list {
  max-height: 400px;
  overflow-y: auto;
}

.template-item {
  padding: 16px;
  border: 1px solid #e4e7ed;
  border-radius: 4px;
  margin-bottom: 12px;
  cursor: pointer;
  transition: all 0.2s;
}

.template-item:hover {
  border-color: #409eff;
  box-shadow: 0 2px 8px rgba(64, 158, 255, 0.1);
}

.template-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 8px;
}

.template-header h4 {
  margin: 0;
  font-size: 16px;
}

.template-description {
  color: #666;
  margin-bottom: 12px;
}

.template-preview {
  background: #f8f9fa;
  padding: 12px;
  border-radius: 4px;
  font-family: 'Courier New', monospace;
  font-size: 12px;
}

.template-preview pre {
  margin: 0;
  white-space: pre-wrap;
}

/* SQL语法高亮样式 */
::v-deep .sql-keyword {
  color: #0066cc;
  font-weight: bold;
}

::v-deep .sql-string {
  color: #009900;
}

::v-deep .sql-number {
  color: #ff6600;
}

::v-deep .sql-comment {
  color: #999;
  font-style: italic;
}
</style>
