<template>
  <div class="template-designer">
    <div class="designer-header">
      <h3>模板设计器</h3>
      <div class="header-actions">
        <el-button @click="handlePreview">预览</el-button>
        <el-button type="primary" @click="handleSave">保存</el-button>
        <el-button @click="handleCancel">取消</el-button>
      </div>
    </div>

    <div class="designer-body">
      <div class="designer-toolbar">
        <el-button-group>
          <el-button size="small" icon="el-icon-plus">添加行</el-button>
          <el-button size="small" icon="el-icon-plus">添加列</el-button>
          <el-button size="small" icon="el-icon-delete">删除</el-button>
        </el-button-group>
        
        <el-divider direction="vertical" />
        
        <el-button-group>
          <el-button size="small" icon="el-icon-edit">文本</el-button>
          <el-button size="small" icon="el-icon-s-data">数字</el-button>
          <el-button size="small" icon="el-icon-date">日期</el-button>
          <el-button size="small" icon="el-icon-s-operation">公式</el-button>
        </el-button-group>
      </div>

      <div class="designer-content">
        <div class="design-canvas">
          <div class="canvas-placeholder">
            <el-empty description="模板设计器功能开发中...">
              <template #image>
                <i class="el-icon-s-grid" style="font-size: 64px; color: #ddd;"></i>
              </template>
              <el-button type="primary" @click="handleCreateTemplate">创建模板</el-button>
            </el-empty>
          </div>
        </div>

        <div class="property-panel">
          <el-card header="属性面板">
            <el-form label-width="80px" size="small">
              <el-form-item label="标题">
                <el-input v-model="templateProperties.title" placeholder="请输入标题" />
              </el-form-item>
              <el-form-item label="宽度">
                <el-input-number v-model="templateProperties.width" :min="100" :max="2000" />
              </el-form-item>
              <el-form-item label="高度">
                <el-input-number v-model="templateProperties.height" :min="100" :max="2000" />
              </el-form-item>
              <el-form-item label="背景色">
                <el-color-picker v-model="templateProperties.backgroundColor" />
              </el-form-item>
            </el-form>
          </el-card>
        </div>
      </div>
    </div>

    <!-- 预览对话框 -->
    <el-dialog
      title="模板预览"
      :visible.sync="previewVisible"
      width="80%"
      :close-on-click-modal="false"
    >
      <div class="template-preview">
        <p>模板预览功能开发中...</p>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'TemplateDesigner',
  props: {
    templateData: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      previewVisible: false,
      templateProperties: {
        title: '新建模板',
        width: 800,
        height: 600,
        backgroundColor: '#ffffff'
      }
    }
  },
  watch: {
    templateData: {
      handler(newVal) {
        if (newVal && newVal.templateId) {
          // 加载模板数据
          this.loadTemplateData(newVal)
        }
      },
      immediate: true,
      deep: true
    }
  },
  methods: {
    // 加载模板数据
    loadTemplateData(templateData) {
      // TODO: 解析模板数据并渲染到设计器
      console.log('加载模板数据:', templateData)
    },

    // 创建模板
    handleCreateTemplate() {
      this.$message.info('模板创建功能开发中...')
    },

    // 预览
    handlePreview() {
      this.previewVisible = true
    },

    // 保存
    handleSave() {
      // TODO: 保存模板设计
      this.$message.success('保存成功')
      this.$emit('save')
    },

    // 取消
    handleCancel() {
      this.$emit('cancel')
    }
  }
}
</script>

<style scoped>
.template-designer {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.designer-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px;
  border-bottom: 1px solid #e8e8e8;
}

.designer-header h3 {
  margin: 0;
}

.designer-body {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.designer-toolbar {
  padding: 12px 16px;
  border-bottom: 1px solid #e8e8e8;
  background-color: #fafafa;
}

.designer-content {
  flex: 1;
  display: flex;
}

.design-canvas {
  flex: 1;
  background-color: #f5f5f5;
  position: relative;
}

.canvas-placeholder {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
}

.property-panel {
  width: 300px;
  border-left: 1px solid #e8e8e8;
  background-color: #fff;
  padding: 16px;
}

.template-preview {
  text-align: center;
  padding: 40px;
  color: #999;
}
</style>
