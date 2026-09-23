<template>
  <div class="test-all-dialogs">
    <div class="page-header">
      <h2>测试所有对话框z-index问题</h2>
      <p>测试数据模型管理的4个主要对话框是否都能正常显示</p>
    </div>

    <div class="test-content">
      <!-- 测试按钮 -->
      <div class="test-buttons">
        <el-row :gutter="20">
          <el-col :span="6">
            <el-button type="primary" size="large" @click="openSqlTemplate" block>
              <i class="el-icon-document-copy"></i>
              <br>SQL模板管理
            </el-button>
          </el-col>
          <el-col :span="6">
            <el-button type="success" size="large" @click="openDataModel" block>
              <i class="el-icon-s-data"></i>
              <br>数据模型管理
            </el-button>
          </el-col>
          <el-col :span="6">
            <el-button type="warning" size="large" @click="openSqlEditor" block>
              <i class="el-icon-edit"></i>
              <br>SQL可视化编辑器
            </el-button>
          </el-col>
          <el-col :span="6">
            <el-button type="info" size="large" @click="openIndicatorCombination" block>
              <i class="el-icon-connection"></i>
              <br>指标组合分析
            </el-button>
          </el-col>
        </el-row>
      </div>

      <!-- 状态显示 -->
      <div class="status-panel">
        <el-card>
          <div slot="header">
            <span>对话框状态监控</span>
            <el-button style="float: right; padding: 3px 0" type="text" @click="resetAll">
              全部关闭
            </el-button>
          </div>
          <div class="status-grid">
            <div class="status-item">
              <div class="status-label">SQL模板管理</div>
              <el-tag :type="sqlTemplateVisible ? 'success' : 'info'">
                {{ sqlTemplateVisible ? '已打开' : '已关闭' }}
              </el-tag>
            </div>
            <div class="status-item">
              <div class="status-label">数据模型管理</div>
              <el-tag :type="dataModelVisible ? 'success' : 'info'">
                {{ dataModelVisible ? '已打开' : '已关闭' }}
              </el-tag>
            </div>
            <div class="status-item">
              <div class="status-label">SQL编辑器</div>
              <el-tag :type="sqlEditorVisible ? 'success' : 'info'">
                {{ sqlEditorVisible ? '已打开' : '已关闭' }}
              </el-tag>
            </div>
            <div class="status-item">
              <div class="status-label">指标组合分析</div>
              <el-tag :type="indicatorCombinationVisible ? 'success' : 'info'">
                {{ indicatorCombinationVisible ? '已打开' : '已关闭' }}
              </el-tag>
            </div>
          </div>
        </el-card>
      </div>

      <!-- 测试说明 -->
      <div class="test-instructions">
        <el-card>
          <div slot="header">
            <span>测试说明</span>
          </div>
          <div class="instructions-content">
            <h4>测试步骤：</h4>
            <ol>
              <li>点击上方任意一个按钮打开对应的对话框</li>
              <li>检查是否出现黑色遮罩层覆盖对话框的问题</li>
              <li>确认对话框中的所有按钮都可以正常点击</li>
              <li>尝试打开多个对话框，测试层级关系</li>
            </ol>
            
            <h4>预期结果：</h4>
            <ul>
              <li>✅ 对话框能正常显示，没有黑色遮罩层覆盖</li>
              <li>✅ 所有按钮都可以正常点击</li>
              <li>✅ 多个对话框可以正常叠加显示</li>
              <li>✅ 关闭对话框功能正常</li>
            </ul>

            <h4>z-index层级设计：</h4>
            <div class="z-index-info">
              <div class="z-level">
                <span class="level">2499</span>
                <span class="desc">遮罩层 (v-modal, el-overlay)</span>
              </div>
              <div class="z-level">
                <span class="level">2500</span>
                <span class="desc">主对话框包装器</span>
              </div>
              <div class="z-level">
                <span class="level">2501</span>
                <span class="desc">主对话框内容</span>
              </div>
              <div class="z-level">
                <span class="level">2600+</span>
                <span class="desc">子对话框（如指标配置）</span>
              </div>
              <div class="z-level">
                <span class="level">3000+</span>
                <span class="desc">拖拽构建器</span>
              </div>
            </div>
          </div>
        </el-card>
      </div>
    </div>

    <!-- 所有对话框组件 -->
    <SqlTemplateDialog :visible.sync="sqlTemplateVisible" />
    <DataModelDialog :visible.sync="dataModelVisible" />
    <SqlEditorDialog :visible.sync="sqlEditorVisible" />
    <IndicatorCombinationDialog :visible.sync="indicatorCombinationVisible" />
  </div>
</template>

<script>
import SqlTemplateDialog from './components/SqlTemplateDialog.vue'
import DataModelDialog from './components/DataModelDialog.vue'
import SqlEditorDialog from './components/SqlEditorDialog.vue'
import IndicatorCombinationDialog from './components/IndicatorCombinationDialog.vue'

export default {
  name: 'TestAllDialogs',
  components: {
    SqlTemplateDialog,
    DataModelDialog,
    SqlEditorDialog,
    IndicatorCombinationDialog
  },
  data() {
    return {
      sqlTemplateVisible: false,
      dataModelVisible: false,
      sqlEditorVisible: false,
      indicatorCombinationVisible: false
    }
  },
  methods: {
    openSqlTemplate() {
      this.sqlTemplateVisible = true
      this.$message.success('打开SQL模板管理对话框')
    },
    
    openDataModel() {
      this.dataModelVisible = true
      this.$message.success('打开数据模型管理对话框')
    },
    
    openSqlEditor() {
      this.sqlEditorVisible = true
      this.$message.success('打开SQL可视化编辑器对话框')
    },
    
    openIndicatorCombination() {
      this.indicatorCombinationVisible = true
      this.$message.success('打开指标组合分析对话框')
    },
    
    resetAll() {
      this.sqlTemplateVisible = false
      this.dataModelVisible = false
      this.sqlEditorVisible = false
      this.indicatorCombinationVisible = false
      this.$message.info('所有对话框已关闭')
    }
  }
}
</script>

<style lang="scss" scoped>
.test-all-dialogs {
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
    max-width: 1200px;
    margin: 0 auto;

    .test-buttons {
      margin-bottom: 30px;

      .el-button {
        height: 80px;
        font-size: 14px;
        line-height: 1.4;

        i {
          font-size: 24px;
          margin-bottom: 8px;
          display: block;
        }
      }
    }

    .status-panel,
    .test-instructions {
      margin-bottom: 30px;

      .status-grid {
        display: grid;
        grid-template-columns: repeat(2, 1fr);
        gap: 16px;

        .status-item {
          display: flex;
          justify-content: space-between;
          align-items: center;
          padding: 12px;
          background: #f8f9fa;
          border-radius: 6px;

          .status-label {
            font-weight: 500;
            color: #303133;
          }
        }
      }

      .instructions-content {
        h4 {
          color: #303133;
          margin: 16px 0 8px 0;
        }

        ol, ul {
          margin: 8px 0;
          padding-left: 20px;

          li {
            margin: 4px 0;
            color: #606266;
            line-height: 1.5;
          }
        }

        .z-index-info {
          margin-top: 12px;
          padding: 12px;
          background: #f8f9fa;
          border-radius: 6px;

          .z-level {
            display: flex;
            align-items: center;
            margin: 6px 0;

            .level {
              display: inline-block;
              width: 60px;
              padding: 2px 8px;
              background: #409eff;
              color: white;
              border-radius: 4px;
              text-align: center;
              font-weight: 500;
              margin-right: 12px;
            }

            .desc {
              color: #606266;
              font-size: 13px;
            }
          }
        }
      }
    }
  }
}
</style>
