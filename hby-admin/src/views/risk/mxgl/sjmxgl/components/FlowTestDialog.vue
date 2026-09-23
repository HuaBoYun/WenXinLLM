<template>
  <el-dialog
    title="流程图渲染测试"
    :visible.sync="dialogVisible"
    width="80%"
    :close-on-click-modal="false"
    class="flow-test-dialog"
  >
    <div class="test-container">
      <!-- 测试控制面板 -->
      <div class="test-controls">
        <el-button type="primary" @click="testBasicMermaid">测试基础Mermaid</el-button>
        <el-button type="success" @click="testComplexMermaid">测试复杂流程图</el-button>
        <el-button type="warning" @click="testAPICall">测试API调用</el-button>
        <el-button @click="clearTest">清空测试</el-button>
      </div>

      <!-- 测试结果显示 -->
      <div class="test-results">
        <div class="result-section">
          <h4>测试日志</h4>
          <div class="log-container">
            <div v-for="(log, index) in testLogs" :key="index" :class="['log-item', log.type]">
              <span class="log-time">{{ log.time }}</span>
              <span class="log-message">{{ log.message }}</span>
            </div>
          </div>
        </div>

        <div class="result-section">
          <h4>Mermaid渲染区域</h4>
          <div ref="mermaidTestContainer" class="mermaid-test-area">
            <div id="mermaid-test-graph" class="mermaid-graph"></div>
          </div>
        </div>
      </div>
    </div>
  </el-dialog>
</template>

<script>
import { getCombinationFlow } from '@/api/mxgl'

export default {
  name: 'FlowTestDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      dialogVisible: false,
      testLogs: []
    }
  },
  watch: {
    visible: {
      handler(val) {
        this.dialogVisible = val
        if (val) {
          this.initTest()
        }
      },
      immediate: true
    },
    dialogVisible(val) {
      this.$emit('update:visible', val)
    }
  },
  methods: {
    // 初始化测试
    initTest() {
      this.addLog('info', '测试对话框已打开')
      this.checkMermaidAvailability()
    },

    // 检查Mermaid可用性
    checkMermaidAvailability() {
      if (window.mermaid) {
        this.addLog('success', '✅ Mermaid库已加载')
        this.addLog('info', `Mermaid版本: ${window.mermaid.version || '未知'}`)
      } else {
        this.addLog('error', '❌ Mermaid库未加载')
        this.loadMermaid()
      }
    },

    // 动态加载Mermaid
    async loadMermaid() {
      try {
        this.addLog('info', '正在加载Mermaid库...')
        
        if (!window.mermaid) {
          const script = document.createElement('script')
          script.src = 'https://cdn.jsdelivr.net/npm/mermaid@9.4.3/dist/mermaid.min.js'
          document.head.appendChild(script)
          
          await new Promise((resolve, reject) => {
            script.onload = resolve
            script.onerror = reject
          })
        }
        
        this.addLog('success', '✅ Mermaid库加载成功')
      } catch (error) {
        this.addLog('error', `❌ Mermaid库加载失败: ${error.message}`)
      }
    },

    // 测试基础Mermaid
    async testBasicMermaid() {
      this.addLog('info', '开始测试基础Mermaid渲染...')
      
      const basicDefinition = `graph TD
    A[开始] --> B[处理]
    B --> C[结束]`
      
      await this.renderMermaid(basicDefinition)
    },

    // 测试复杂流程图
    async testComplexMermaid() {
      this.addLog('info', '开始测试复杂流程图渲染...')
      
      const complexDefinition = `graph TD
    %% 样式定义
    classDef startNode fill:#e8f5e8,stroke:#4caf50,stroke-width:4px,color:#2e7d32,font-weight:bold
    classDef endNode fill:#fce4ec,stroke:#e91e63,stroke-width:4px,color:#c2185b,font-weight:bold
    classDef processNode fill:#e3f2fd,stroke:#2196f3,stroke-width:3px,color:#1976d2,font-weight:bold
    
    START(["🚀 开始<br/>指标组合分析"]):::startNode
    IND1["📊 指标1<br/>🏷️ 编码: TEST001"]:::processNode
    IND2["📊 指标2<br/>🏷️ 编码: TEST002"]:::processNode
    MERGE["🔄 汇聚结果"]:::processNode
    END(["✅ 完成<br/>分析结果"]):::endNode
    
    START --> IND1
    START --> IND2
    IND1 --> MERGE
    IND2 --> MERGE
    MERGE --> END`
      
      await this.renderMermaid(complexDefinition)
    },

    // 测试API调用
    async testAPICall() {
      this.addLog('info', '开始测试API调用...')
      
      try {
        // 使用一个测试组合ID
        const testCombinationId = 'COMB1759146935496'
        this.addLog('info', `调用API: /riskcontrol/model/combination/flow/${testCombinationId}`)
        
        const response = await getCombinationFlow(testCombinationId)
        this.addLog('info', `API响应: ${JSON.stringify(response, null, 2)}`)
        
        if (response && response.code === 1 && response.data && response.data.mermaidDefinition) {
          this.addLog('success', '✅ API调用成功，获取到Mermaid定义')
          await this.renderMermaid(response.data.mermaidDefinition)
        } else {
          this.addLog('warning', '⚠️ API调用成功但没有获取到有效的Mermaid定义')
        }
      } catch (error) {
        this.addLog('error', `❌ API调用失败: ${error.message}`)
      }
    },

    // 渲染Mermaid
    async renderMermaid(definition) {
      try {
        this.addLog('info', '开始渲染Mermaid图表...')
        
        const container = document.getElementById('mermaid-test-graph')
        if (!container) {
          this.addLog('error', '❌ 找不到渲染容器')
          return
        }
        
        // 清空容器
        container.innerHTML = ''
        
        if (!window.mermaid) {
          this.addLog('error', '❌ Mermaid库未加载')
          return
        }
        
        // 初始化Mermaid
        window.mermaid.initialize({
          startOnLoad: false,
          theme: 'default',
          securityLevel: 'loose'
        })
        
        // 渲染
        if (typeof window.mermaid.render === 'function') {
          const renderResult = await window.mermaid.render('test-svg-' + Date.now(), definition)
          
          if (renderResult && renderResult.svg) {
            container.innerHTML = renderResult.svg
            this.addLog('success', '✅ Mermaid渲染成功 (新版本API)')
          } else if (typeof renderResult === 'string') {
            container.innerHTML = renderResult
            this.addLog('success', '✅ Mermaid渲染成功 (新版本API - 字符串返回)')
          }
        } else {
          // 使用传统方法
          const mermaidDiv = document.createElement('div')
          mermaidDiv.className = 'mermaid'
          mermaidDiv.textContent = definition
          container.appendChild(mermaidDiv)
          
          window.mermaid.init(undefined, mermaidDiv)
          this.addLog('success', '✅ Mermaid渲染成功 (传统API)')
        }
      } catch (error) {
        this.addLog('error', `❌ Mermaid渲染失败: ${error.message}`)
        console.error('Mermaid渲染错误:', error)
      }
    },

    // 清空测试
    clearTest() {
      this.testLogs = []
      const container = document.getElementById('mermaid-test-graph')
      if (container) {
        container.innerHTML = ''
      }
      this.addLog('info', '测试已清空')
    },

    // 添加日志
    addLog(type, message) {
      const now = new Date()
      const time = `${now.getHours().toString().padStart(2, '0')}:${now.getMinutes().toString().padStart(2, '0')}:${now.getSeconds().toString().padStart(2, '0')}`
      
      this.testLogs.push({
        type,
        time,
        message
      })
      
      // 自动滚动到底部
      this.$nextTick(() => {
        const logContainer = this.$el.querySelector('.log-container')
        if (logContainer) {
          logContainer.scrollTop = logContainer.scrollHeight
        }
      })
    }
  }
}
</script>

<style scoped>
.test-container {
  padding: 20px;
}

.test-controls {
  margin-bottom: 20px;
  text-align: center;
}

.test-controls .el-button {
  margin: 0 8px;
}

.test-results {
  display: flex;
  gap: 20px;
  height: 500px;
}

.result-section {
  flex: 1;
  border: 1px solid #ddd;
  border-radius: 4px;
  padding: 16px;
}

.result-section h4 {
  margin: 0 0 16px 0;
  color: #303133;
}

.log-container {
  height: 400px;
  overflow-y: auto;
  background: #f5f5f5;
  padding: 8px;
  border-radius: 4px;
  font-family: 'Courier New', monospace;
  font-size: 12px;
}

.log-item {
  margin-bottom: 4px;
  padding: 2px 4px;
  border-radius: 2px;
}

.log-item.info {
  color: #606266;
}

.log-item.success {
  color: #67c23a;
  background: #f0f9ff;
}

.log-item.warning {
  color: #e6a23c;
  background: #fdf6ec;
}

.log-item.error {
  color: #f56c6c;
  background: #fef0f0;
}

.log-time {
  color: #909399;
  margin-right: 8px;
}

.mermaid-test-area {
  height: 400px;
  overflow: auto;
  border: 1px solid #eee;
  border-radius: 4px;
  padding: 16px;
  background: #fafafa;
}

.mermaid-graph {
  width: 100%;
  height: 100%;
  min-height: 300px;
}
</style>
