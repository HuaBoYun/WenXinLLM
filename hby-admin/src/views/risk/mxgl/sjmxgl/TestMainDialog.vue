<template>
  <div class="test-main-dialog">
    <div class="page-header">
      <h2>测试指标配置主对话框</h2>
      <p>测试主对话框的z-index层级问题</p>
    </div>

    <div class="test-content">
      <!-- 测试按钮 -->
      <div class="test-buttons">
        <el-button type="primary" size="large" @click="openMainDialog">
          <i class="el-icon-s-grid"></i>
          打开指标配置对话框
        </el-button>
        
        <el-button type="warning" size="large" @click="openMultipleDialogs">
          <i class="el-icon-warning"></i>
          测试多层对话框
        </el-button>
      </div>

      <!-- 状态显示 -->
      <div class="status-info">
        <el-card>
          <div slot="header">
            <span>对话框状态</span>
          </div>
          <div class="status-list">
            <div class="status-item">
              <span>主对话框：</span>
              <el-tag :type="mainDialogVisible ? 'success' : 'info'">
                {{ mainDialogVisible ? '已打开' : '已关闭' }}
              </el-tag>
            </div>
            <div class="status-item">
              <span>测试对话框：</span>
              <el-tag :type="testDialogVisible ? 'success' : 'info'">
                {{ testDialogVisible ? '已打开' : '已关闭' }}
              </el-tag>
            </div>
          </div>
        </el-card>
      </div>

      <!-- 操作日志 -->
      <div class="operation-log">
        <el-card>
          <div slot="header">
            <span>操作日志</span>
            <el-button style="float: right; padding: 3px 0" type="text" @click="clearLog">
              清空
            </el-button>
          </div>
          <div class="log-list">
            <div v-if="operationLog.length === 0" class="empty-log">
              <p>暂无操作记录</p>
            </div>
            <div
              v-for="(log, index) in operationLog"
              :key="index"
              class="log-item"
            >
              <span class="log-time">{{ log.time }}</span>
              <span class="log-content">{{ log.content }}</span>
            </div>
          </div>
        </el-card>
      </div>
    </div>

    <!-- 主对话框 -->
    <IndicatorConfigDialog
      :visible.sync="mainDialogVisible"
      :combination="mockCombination"
      @close="handleMainDialogClose"
    />

    <!-- 测试对话框 -->
    <el-dialog
      title="测试对话框"
      :visible.sync="testDialogVisible"
      width="50%"
      :z-index="2600"
    >
      <p>这是一个测试对话框，用于验证层级关系</p>
      <div slot="footer" class="dialog-footer">
        <el-button @click="testDialogVisible = false">关闭</el-button>
        <el-button type="primary" @click="openMainDialog">打开主对话框</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import IndicatorConfigDialog from './components/IndicatorConfigDialog.vue'

export default {
  name: 'TestMainDialog',
  components: {
    IndicatorConfigDialog
  },
  data() {
    return {
      mainDialogVisible: false,
      testDialogVisible: false,
      
      // 模拟数据
      mockCombination: {
        combinationId: 'TEST_COMB_001',
        combinationName: '测试指标组合',
        description: '用于测试主对话框显示问题'
      },
      
      // 操作日志
      operationLog: []
    }
  },
  methods: {
    // 打开主对话框
    openMainDialog() {
      this.addLog('打开指标配置主对话框')
      this.mainDialogVisible = true
    },
    
    // 打开多层对话框测试
    openMultipleDialogs() {
      this.addLog('打开测试对话框')
      this.testDialogVisible = true
    },
    
    // 处理主对话框关闭
    handleMainDialogClose() {
      this.addLog('关闭指标配置主对话框')
      this.mainDialogVisible = false
    },
    
    // 添加日志
    addLog(content) {
      const log = {
        time: new Date().toLocaleTimeString(),
        content
      }
      this.operationLog.unshift(log)
      
      // 限制日志数量
      if (this.operationLog.length > 20) {
        this.operationLog = this.operationLog.slice(0, 20)
      }
    },
    
    // 清空日志
    clearLog() {
      this.operationLog = []
    }
  }
}
</script>

<style lang="scss" scoped>
.test-main-dialog {
  padding: 20px;
  background: #f5f7fa;
  min-height: 100vh;

  .page-header {
    text-align: center;
    margin-bottom: 30px;

    h2 {
      color: #303133;
      margin-bottom: 10px;
    }

    p {
      color: #606266;
      font-size: 14px;
    }
  }

  .test-content {
    max-width: 800px;
    margin: 0 auto;

    .test-buttons {
      text-align: center;
      margin-bottom: 30px;

      .el-button {
        margin: 0 15px;
        padding: 15px 30px;
        font-size: 16px;
      }
    }

    .status-info,
    .operation-log {
      margin-bottom: 30px;

      .status-list {
        .status-item {
          display: flex;
          justify-content: space-between;
          align-items: center;
          padding: 8px 0;
          border-bottom: 1px solid #f0f0f0;

          &:last-child {
            border-bottom: none;
          }

          span {
            font-size: 14px;
            color: #606266;
          }
        }
      }

      .log-list {
        max-height: 300px;
        overflow-y: auto;

        .empty-log {
          text-align: center;
          padding: 40px 20px;
          color: #909399;

          p {
            margin: 0;
            font-size: 14px;
          }
        }

        .log-item {
          display: flex;
          gap: 12px;
          padding: 8px 0;
          border-bottom: 1px solid #f0f0f0;
          font-size: 13px;

          &:last-child {
            border-bottom: none;
          }

          .log-time {
            color: #909399;
            min-width: 80px;
          }

          .log-content {
            color: #606266;
            flex: 1;
          }
        }
      }
    }
  }
}
</style>
