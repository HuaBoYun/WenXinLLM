<template>
  <div class="test-drag-indicator">
    <div class="page-header">
      <h2>拖拽式指标创建功能测试</h2>
      <p>测试新的拖拽模式架构：独立对话框 + 模式选择</p>
    </div>

    <div class="test-content">
      <!-- 测试按钮 -->
      <div class="test-buttons">
        <el-button type="primary" size="large" @click="openIndicatorDialog">
          <i class="el-icon-s-grid"></i>
          测试指标配置对话框
        </el-button>
        
        <el-button type="success" size="large" @click="openDragBuilderDirectly">
          <i class="el-icon-magic-stick"></i>
          直接打开拖拽构建器
        </el-button>
      </div>

      <!-- 功能说明 -->
      <div class="feature-info">
        <el-card>
          <div slot="header">
            <span>新架构特点</span>
          </div>
          <div class="features">
            <div class="feature-item">
              <el-tag type="primary">模式选择</el-tag>
              <span>在"新建指标"标签页提供表单模式和拖拽模式选择</span>
            </div>
            <div class="feature-item">
              <el-tag type="success">独立对话框</el-tag>
              <span>拖拽模式使用独立的全屏对话框，提供更大操作空间</span>
            </div>
            <div class="feature-item">
              <el-tag type="warning">三栏布局</el-tag>
              <span>字段库、SQL构建器、预览配置三栏式布局</span>
            </div>
            <div class="feature-item">
              <el-tag type="info">实时预览</el-tag>
              <span>实时生成SQL语句，支持格式化和验证</span>
            </div>
          </div>
        </el-card>
      </div>

      <!-- 测试结果 -->
      <div class="test-results">
        <el-card>
          <div slot="header">
            <span>测试结果</span>
            <el-button style="float: right; padding: 3px 0" type="text" @click="clearResults">
              清空
            </el-button>
          </div>
          <div class="results-list">
            <div v-if="testResults.length === 0" class="empty-results">
              <i class="el-icon-info"></i>
              <p>暂无测试结果，请点击上方按钮进行测试</p>
            </div>
            <div
              v-for="(result, index) in testResults"
              :key="index"
              class="result-item"
            >
              <div class="result-header">
                <span class="result-time">{{ result.time }}</span>
                <el-tag :type="result.success ? 'success' : 'danger'">
                  {{ result.success ? '成功' : '失败' }}
                </el-tag>
              </div>
              <div class="result-content">
                <p><strong>操作：</strong>{{ result.action }}</p>
                <p><strong>结果：</strong>{{ result.message }}</p>
                <div v-if="result.data" class="result-data">
                  <p><strong>数据：</strong></p>
                  <pre>{{ JSON.stringify(result.data, null, 2) }}</pre>
                </div>
              </div>
            </div>
          </div>
        </el-card>
      </div>
    </div>

    <!-- 指标配置对话框 -->
    <IndicatorConfigDialog
      :visible.sync="indicatorDialogVisible"
      :combination="mockCombination"
      @close="handleIndicatorDialogClose"
    />

    <!-- 直接打开的拖拽构建器 -->
    <DragSQLBuilderDialog
      :visible.sync="dragBuilderVisible"
      :initial-data="dragInitialData"
      @save="handleDragBuilderSave"
      @close="handleDragBuilderClose"
    />
  </div>
</template>

<script>
import IndicatorConfigDialog from './components/IndicatorConfigDialog.vue'
import DragSQLBuilderDialog from './components/DragSQLBuilderDialog.vue'

export default {
  name: 'TestDragIndicator',
  components: {
    IndicatorConfigDialog,
    DragSQLBuilderDialog
  },
  data() {
    return {
      indicatorDialogVisible: false,
      dragBuilderVisible: false,
      
      // 模拟数据
      mockCombination: {
        combinationId: 'TEST_COMB_001',
        combinationName: '测试指标组合',
        description: '用于测试拖拽式指标创建功能'
      },
      
      dragInitialData: {
        indicatorName: '测试指标',
        indicatorCode: 'TEST_INDICATOR',
        description: '这是一个测试指标',
        category: 'FINANCIAL'
      },
      
      // 测试结果
      testResults: []
    }
  },
  methods: {
    // 打开指标配置对话框
    openIndicatorDialog() {
      this.addTestResult('打开指标配置对话框', true, '成功打开对话框，可以测试模式选择功能')
      this.indicatorDialogVisible = true
    },
    
    // 直接打开拖拽构建器
    openDragBuilderDirectly() {
      this.addTestResult('直接打开拖拽构建器', true, '成功打开独立的拖拽SQL构建器对话框')
      this.dragBuilderVisible = true
    },
    
    // 处理指标对话框关闭
    handleIndicatorDialogClose() {
      this.addTestResult('关闭指标配置对话框', true, '对话框正常关闭')
      this.indicatorDialogVisible = false
    },
    
    // 处理拖拽构建器保存
    handleDragBuilderSave(indicatorData) {
      this.addTestResult('拖拽构建器保存', true, '成功保存指标数据', indicatorData)
      this.dragBuilderVisible = false
    },
    
    // 处理拖拽构建器关闭
    handleDragBuilderClose() {
      this.addTestResult('关闭拖拽构建器', true, '拖拽构建器正常关闭')
      this.dragBuilderVisible = false
    },
    
    // 添加测试结果
    addTestResult(action, success, message, data = null) {
      const result = {
        time: new Date().toLocaleTimeString(),
        action,
        success,
        message,
        data
      }
      this.testResults.unshift(result)
      
      // 限制结果数量
      if (this.testResults.length > 10) {
        this.testResults = this.testResults.slice(0, 10)
      }
    },
    
    // 清空测试结果
    clearResults() {
      this.testResults = []
    }
  }
}
</script>

<style lang="scss" scoped>
.test-drag-indicator {
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
    max-width: 1000px;
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

    .feature-info,
    .test-results {
      margin-bottom: 30px;

      .features {
        .feature-item {
          display: flex;
          align-items: center;
          gap: 12px;
          margin-bottom: 12px;
          padding: 8px 0;

          span {
            color: #606266;
            line-height: 1.5;
          }
        }
      }

      .results-list {
        .empty-results {
          text-align: center;
          padding: 40px 20px;
          color: #909399;

          i {
            font-size: 48px;
            margin-bottom: 16px;
            display: block;
          }

          p {
            margin: 0;
            font-size: 14px;
          }
        }

        .result-item {
          border: 1px solid #e4e7ed;
          border-radius: 6px;
          margin-bottom: 12px;
          overflow: hidden;

          .result-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding: 12px 16px;
            background: #f5f7fa;
            border-bottom: 1px solid #e4e7ed;

            .result-time {
              font-size: 12px;
              color: #909399;
            }
          }

          .result-content {
            padding: 16px;

            p {
              margin: 0 0 8px 0;
              font-size: 14px;
              line-height: 1.5;

              strong {
                color: #303133;
              }
            }

            .result-data {
              margin-top: 12px;

              pre {
                background: #f5f7fa;
                border: 1px solid #e4e7ed;
                border-radius: 4px;
                padding: 12px;
                font-size: 12px;
                line-height: 1.4;
                color: #303133;
                overflow-x: auto;
                margin: 8px 0 0 0;
              }
            }
          }
        }
      }
    }
  }
}
</style>
