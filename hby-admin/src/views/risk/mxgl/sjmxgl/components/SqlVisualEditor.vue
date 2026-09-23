<template>
  <div class="sql-visual-editor">
    <!-- 顶部工具栏 -->
    <div class="editor-toolbar">
      <div class="toolbar-left">
        <el-button-group>
          <el-button 
            :type="activeMode === 'visual' ? 'primary' : ''" 
            size="mini" 
            @click="switchMode('visual')"
          >
            <i class="el-icon-view"></i> 可视化构建
          </el-button>
          <el-button 
            :type="activeMode === 'code' ? 'primary' : ''" 
            size="mini" 
            @click="switchMode('code')"
          >
            <i class="el-icon-edit"></i> SQL编辑器
          </el-button>
          <el-button 
            :type="activeMode === 'result' ? 'primary' : ''" 
            size="mini" 
            @click="switchMode('result')"
          >
            <i class="el-icon-data-line"></i> 执行结果
          </el-button>
        </el-button-group>
      </div>
      
      <div class="toolbar-center">
        <el-select 
          v-model="selectedDataSource" 
          placeholder="选择数据源" 
          size="mini" 
          @change="handleDataSourceChange"
          style="width: 200px; margin-right: 12px;"
        >
          <el-option
            v-for="ds in dataSourceList"
            :key="ds.sourceId"
            :label="ds.sourceName"
            :value="ds.sourceId"
          />
        </el-select>
        
        <el-button 
          type="success" 
          size="mini" 
          :loading="executing" 
          @click="executeSQL"
          :disabled="!sqlContent.trim()"
        >
          <i class="el-icon-caret-right"></i> 执行SQL
        </el-button>
      </div>
      
      <div class="toolbar-right">
        <el-button size="mini" @click="formatSQL">
          <i class="el-icon-magic-stick"></i> 格式化
        </el-button>
        <el-button size="mini" @click="validateSQL">
          <i class="el-icon-circle-check"></i> 验证
        </el-button>
        <el-button size="mini" @click="clearAll">
          <i class="el-icon-delete"></i> 清空
        </el-button>
      </div>
    </div>

    <!-- 主要内容区域 -->
    <div class="editor-content">
      <!-- 可视化构建器 -->
      <div v-show="activeMode === 'visual'" class="visual-builder">
        <SqlVisualBuilder
          :data-source-id="selectedDataSource"
          :sql-content="sqlContent"
          @sql-change="handleSQLChange"
        />
      </div>

      <!-- SQL代码编辑器 -->
      <div v-show="activeMode === 'code'" class="code-editor">
        <SqlCodeEditor
          v-model="sqlContent"
          :data-source-id="selectedDataSource"
          @change="handleSQLChange"
        />
      </div>

      <!-- 执行结果 -->
      <div v-show="activeMode === 'result'" class="result-viewer">
        <SqlResultViewer
          :result-data="executionResult"
          :loading="executing"
          @export="handleExport"
        />
      </div>
    </div>

    <!-- 底部状态栏 -->
    <div class="editor-status">
      <div class="status-left">
        <span v-if="sqlContent.trim()">
          <i class="el-icon-document"></i> 
          SQL长度: {{ sqlContent.length }} 字符
        </span>
        <span v-if="executionResult && executionResult.records">
          <i class="el-icon-data-line"></i> 
          结果: {{ executionResult.total || executionResult.records.length }} 条记录
        </span>
      </div>
      
      <div class="status-right">
        <span v-if="lastExecutionTime">
          <i class="el-icon-time"></i> 
          执行时间: {{ lastExecutionTime }}ms
        </span>
        <span v-if="validationStatus" :class="validationStatus.type">
          <i :class="validationStatus.icon"></i> 
          {{ validationStatus.message }}
        </span>
      </div>
    </div>
  </div>
</template>

<script>
import SqlVisualBuilder from './SqlVisualBuilder.vue'
import SqlCodeEditor from './SqlCodeEditor.vue'
import SqlResultViewer from './SqlResultViewer.vue'
import { getDataSourceList, executeSQL, validateSQL } from '@/api/mxgl'
import { getIndicatorCodeByRoute } from '@/utils/indicatorCode' // 🔥 新增

export default {
  name: 'SqlVisualEditor',
  components: {
    SqlVisualBuilder,
    SqlCodeEditor,
    SqlResultViewer
  },
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
    },
    // 🔥 新增：组合指标编码
    indicatorCode: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      // 编辑器状态
      activeMode: 'visual', // visual, code, result
      sqlContent: '',
      selectedDataSource: '',
      
      // 数据源
      dataSourceList: [],
      
      // 执行相关
      executing: false,
      executionResult: null,
      lastExecutionTime: null,
      
      // 验证状态
      validationStatus: null
    }
  },
  watch: {
    value: {
      handler(val) {
        this.sqlContent = val || ''
      },
      immediate: true
    },
    dataSourceId: {
      handler(val) {
        this.selectedDataSource = val || ''
      },
      immediate: true
    },
    sqlContent(val) {
      this.$emit('input', val)
      this.$emit('change', val)
    }
  },
  mounted() {
    this.loadDataSources()
  },
  methods: {
    // 加载数据源列表
    async loadDataSources() {
      try {
        const response = await getDataSourceList({
          pageNum: 1,
          pageSize: 1000,
          status: 'ACTIVE'
        })
        if (response.code === 1) {
          this.dataSourceList = response.data?.records || []
        }
      } catch (error) {
        console.error('加载数据源失败:', error)
      }
    },

    // 切换编辑模式
    switchMode(mode) {
      this.activeMode = mode
      this.$emit('mode-change', mode)
    },

    // 数据源变化
    handleDataSourceChange(dataSourceId) {
      this.selectedDataSource = dataSourceId
      this.$emit('datasource-change', dataSourceId)
    },

    // SQL内容变化
    handleSQLChange(sql) {
      this.sqlContent = sql
      this.clearValidation()
    },

    // 执行SQL
    async executeSQL() {
      if (!this.sqlContent.trim()) {
        this.$message.warning('请输入SQL语句')
        return
      }
      
      if (!this.selectedDataSource) {
        this.$message.warning('请选择数据源')
        return
      }

      try {
        this.executing = true
        const startTime = Date.now()
        
        const response = await executeSQL({
          dataSourceId: this.selectedDataSource,
          sqlContent: this.sqlContent,
          pageNum: 1,
          pageSize: 100,
          indicatorCode: this.indicatorCode || this.getDefaultIndicatorCode() // 🔥 新增指标编码
        })
        
        this.lastExecutionTime = Date.now() - startTime
        
        if (response.code === 1) {
          this.executionResult = response.data
          this.activeMode = 'result'
          this.$message.success(`执行成功，耗时 ${this.lastExecutionTime}ms`)
          this.$emit('execute-success', this.executionResult)
        } else {
          this.$message.error(response.msg || 'SQL执行失败')
          this.$emit('execute-error', response.msg)
        }
      } catch (error) {
        console.error('SQL执行失败:', error)
        this.$message.error('SQL执行失败')
        this.$emit('execute-error', error.message)
      } finally {
        this.executing = false
      }
    },

    // 格式化SQL
    formatSQL() {
      if (!this.sqlContent.trim()) return
      
      // 简单的SQL格式化逻辑
      let formatted = this.sqlContent
        .replace(/\s+/g, ' ')
        .replace(/\s*,\s*/g, ',\n  ')
        .replace(/\s*(SELECT|FROM|WHERE|GROUP BY|ORDER BY|HAVING|JOIN|LEFT JOIN|RIGHT JOIN|INNER JOIN)\s+/gi, '\n$1 ')
        .replace(/\s*AND\s+/gi, '\n  AND ')
        .replace(/\s*OR\s+/gi, '\n  OR ')
        .trim()
      
      this.sqlContent = formatted
      this.$message.success('SQL格式化完成')
    },

    // 验证SQL
    async validateSQL() {
      if (!this.sqlContent.trim()) {
        this.validationStatus = {
          type: 'warning',
          icon: 'el-icon-warning',
          message: 'SQL为空'
        }
        return
      }

      try {
        const response = await validateSQL({
          dataSourceId: this.selectedDataSource,
          sqlContent: this.sqlContent
        })
        
        if (response.code === 1) {
          this.validationStatus = {
            type: 'success',
            icon: 'el-icon-circle-check',
            message: 'SQL语法正确'
          }
        } else {
          this.validationStatus = {
            type: 'error',
            icon: 'el-icon-circle-close',
            message: response.msg || 'SQL语法错误'
          }
        }
      } catch (error) {
        this.validationStatus = {
          type: 'error',
          icon: 'el-icon-circle-close',
          message: '验证失败'
        }
      }
    },

    // 清空验证状态
    clearValidation() {
      this.validationStatus = null
    },

    // 清空所有内容
    clearAll() {
      this.$confirm('确定要清空所有内容吗？', '确认清空', {
        type: 'warning'
      }).then(() => {
        this.sqlContent = ''
        this.executionResult = null
        this.validationStatus = null
        this.lastExecutionTime = null
        this.$message.success('已清空')
      }).catch(() => {})
    },

    // 导出结果
    handleExport(format) {
      this.$emit('export', {
        format,
        data: this.executionResult,
        sql: this.sqlContent
      })
    },

    // 获取当前SQL
    getSQL() {
      return this.sqlContent
    },

    // 获取执行结果
    getResult() {
      return this.executionResult
    },

    // 🔥 新增：获取默认指标编码
    getDefaultIndicatorCode() {
      return getIndicatorCodeByRoute(this.$route)
    }
  }
}
</script>

<style scoped>
.sql-visual-editor {
  display: flex;
  flex-direction: column;
  height: 600px;
  border: 1px solid #e4e7ed;
  border-radius: 4px;
  background: #fff;
}

.editor-toolbar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 12px 16px;
  border-bottom: 1px solid #e4e7ed;
  background: #f8f9fa;
}

.toolbar-left,
.toolbar-center,
.toolbar-right {
  display: flex;
  align-items: center;
  gap: 8px;
}

.editor-content {
  flex: 1;
  overflow: hidden;
}

.visual-builder,
.code-editor,
.result-viewer {
  height: 100%;
}

.editor-status {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 8px 16px;
  border-top: 1px solid #e4e7ed;
  background: #f8f9fa;
  font-size: 12px;
  color: #666;
}

.status-left,
.status-right {
  display: flex;
  align-items: center;
  gap: 16px;
}

.status-right .success {
  color: #67c23a;
}

.status-right .error {
  color: #f56c6c;
}

.status-right .warning {
  color: #e6a23c;
}
</style>
