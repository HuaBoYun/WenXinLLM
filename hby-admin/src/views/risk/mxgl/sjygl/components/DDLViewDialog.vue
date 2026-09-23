<template>
  <el-dialog
    :title="`表 ${tableName} 的DDL语句`"
    :visible.sync="dialogVisible"
    width="800px"
    :close-on-click-modal="false"
    :modal="true"
    :modal-append-to-body="true"
    :append-to-body="true"
    custom-class="ddl-view-dialog"
    :z-index="3500"
    :destroy-on-close="true"
  >
    <div class="ddl-view-container">
      <!-- 操作工具栏 -->
      <div class="toolbar">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-button @click="generateDDL" icon="el-icon-refresh" :loading="loading">
              重新生成
            </el-button>
            <el-button @click="copyDDL" icon="el-icon-document-copy">
              复制DDL
            </el-button>
          </el-col>
          <el-col :span="12" style="text-align: right;">
            <el-button @click="downloadDDL" icon="el-icon-download">
              下载文件
            </el-button>
          </el-col>
        </el-row>
      </div>

      <!-- DDL内容显示 -->
      <div class="ddl-content">
        <el-input
          v-model="ddlContent"
          type="textarea"
          :rows="20"
          placeholder="DDL语句将在这里显示..."
          readonly
          v-loading="loading"
        />
      </div>

      <!-- DDL信息统计 -->
      <div class="ddl-info" v-if="ddlContent">
        <el-descriptions :column="3" border size="small">
          <el-descriptions-item label="字符数">{{ ddlContent.length }}</el-descriptions-item>
          <el-descriptions-item label="行数">{{ ddlContent.split('\n').length }}</el-descriptions-item>
          <el-descriptions-item label="生成时间">{{ generateTime }}</el-descriptions-item>
        </el-descriptions>
      </div>

      <!-- 语法高亮提示 -->
      <div class="syntax-tips">
        <el-alert
          title="DDL语句说明"
          type="info"
          :closable="false"
          show-icon
        >
          <template slot="default">
            <p>• 此DDL语句基于当前表结构生成，可用于创建相同结构的表</p>
            <p>• 语句兼容达梦数据库语法，可能需要根据目标数据库进行调整</p>
            <p>• 建议在执行前先在测试环境验证语句的正确性</p>
          </template>
        </el-alert>
      </div>
    </div>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">关闭</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { generateTableDDL } from '@/api/mxgl'

export default {
  name: 'DDLViewDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    tableName: {
      type: String,
      default: ''
    },
    dataSourceId: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      loading: false,
      ddlContent: '',
      generateTime: ''
    }
  },
  computed: {
    dialogVisible: {
      get() {
        return this.visible
      },
      set(val) {
        this.$emit('update:visible', val)
      }
    }
  },
  watch: {
    visible(val) {
      if (val && this.tableName && this.dataSourceId) {
        this.generateDDL()
      }
    }
  },
  methods: {
    // 生成DDL语句
    async generateDDL() {
      this.loading = true
      try {
        const response = await generateTableDDL({
          dataSourceId: this.dataSourceId,
          tableName: this.tableName
        })
        if (response.code === 1) {
          this.ddlContent = response.data || ''
          this.generateTime = new Date().toLocaleString('zh-CN')
          
          if (!this.ddlContent.trim()) {
            this.$message.warning('未能生成DDL语句，请检查表结构是否完整')
          }
        } else {
          this.$message.error(response.msg || '生成DDL失败')
          this.ddlContent = ''
        }
      } catch (error) {
        this.$message.error('生成DDL失败')
        console.error('生成DDL失败:', error)
        this.ddlContent = ''
      } finally {
        this.loading = false
      }
    },

    // 复制DDL到剪贴板
    async copyDDL() {
      if (!this.ddlContent.trim()) {
        this.$message.warning('没有可复制的DDL内容')
        return
      }

      try {
        // 使用现代浏览器的Clipboard API
        if (navigator.clipboard && window.isSecureContext) {
          await navigator.clipboard.writeText(this.ddlContent)
          this.$message.success('DDL已复制到剪贴板')
        } else {
          // 降级方案：使用传统的document.execCommand
          this.fallbackCopyToClipboard(this.ddlContent)
        }
      } catch (error) {
        console.error('复制失败:', error)
        this.$message.error('复制失败，请手动选择复制')
      }
    },

    // 降级复制方案
    fallbackCopyToClipboard(text) {
      const textArea = document.createElement('textarea')
      textArea.value = text
      textArea.style.position = 'fixed'
      textArea.style.left = '-999999px'
      textArea.style.top = '-999999px'
      document.body.appendChild(textArea)
      textArea.focus()
      textArea.select()
      
      try {
        const successful = document.execCommand('copy')
        if (successful) {
          this.$message.success('DDL已复制到剪贴板')
        } else {
          this.$message.error('复制失败')
        }
      } catch (err) {
        this.$message.error('复制失败')
      } finally {
        document.body.removeChild(textArea)
      }
    },

    // 下载DDL文件
    downloadDDL() {
      if (!this.ddlContent.trim()) {
        this.$message.warning('没有可下载的DDL内容')
        return
      }

      try {
        const blob = new Blob([this.ddlContent], { type: 'text/plain;charset=utf-8' })
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = `${this.tableName}_DDL_${new Date().getTime()}.sql`
        document.body.appendChild(link)
        link.click()
        document.body.removeChild(link)
        window.URL.revokeObjectURL(url)
        
        this.$message.success('DDL文件下载成功')
      } catch (error) {
        console.error('下载失败:', error)
        this.$message.error('下载失败')
      }
    },

    // 关闭对话框
    handleClose() {
      this.dialogVisible = false
      this.ddlContent = ''
      this.generateTime = ''
    }
  }
}
</script>

<style scoped>
.ddl-view-container {
  padding: 0;
}

.toolbar {
  margin-bottom: 20px;
  padding: 15px;
  background-color: #f5f7fa;
  border-radius: 4px;
}

.ddl-content {
  margin-bottom: 20px;
}

.ddl-content .el-textarea {
  font-family: 'Courier New', Consolas, monospace;
}

.ddl-content .el-textarea__inner {
  font-family: 'Courier New', Consolas, monospace;
  font-size: 13px;
  line-height: 1.5;
  background-color: #fafafa;
  border: 1px solid #dcdfe6;
}

.ddl-info {
  margin-bottom: 20px;
}

.syntax-tips {
  margin-bottom: 20px;
}

.syntax-tips .el-alert__content p {
  margin: 5px 0;
  font-size: 13px;
}

.dialog-footer {
  text-align: right;
}

/* 加载状态样式 */
.el-loading-mask {
  background-color: rgba(255, 255, 255, 0.8);
}

/* 文本域滚动条样式 */
.el-textarea__inner::-webkit-scrollbar {
  width: 8px;
  height: 8px;
}

.el-textarea__inner::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 4px;
}

.el-textarea__inner::-webkit-scrollbar-thumb {
  background: #c1c1c1;
  border-radius: 4px;
}

.el-textarea__inner::-webkit-scrollbar-thumb:hover {
  background: #a8a8a8;
}
</style>
