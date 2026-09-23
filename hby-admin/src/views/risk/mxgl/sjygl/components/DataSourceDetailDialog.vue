<template>
  <el-dialog
    title="数据源详情"
    :visible.sync="dialogVisible"
    width="700px"
    :close-on-click-modal="false"
  >
    <div class="detail-container">
      <el-descriptions :column="2" border>
        <el-descriptions-item label="数据源名称">
          {{ data.sourceName || '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="数据源类型">
          <el-tag :type="getSourceTypeTagType(data.sourceType)">
            {{ getSourceTypeText(data.sourceType) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="主机IP">
          {{ data.hostIp || '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="端口">
          {{ data.port || '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="数据库名">
          {{ data.databaseName || '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="用户名">
          {{ data.username || '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="data.status === 'ACTIVE' ? 'success' : 'danger'">
            {{ data.status === 'ACTIVE' ? '活跃' : '禁用' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="连接状态">
          <el-tag v-if="data.connectionTestResult" :type="data.connectionTestResult === 'SUCCESS' ? 'success' : 'danger'">
            {{ data.connectionTestResult === 'SUCCESS' ? '连接成功' : '连接失败' }}
          </el-tag>
          <span v-else>-</span>
        </el-descriptions-item>
        <el-descriptions-item label="表数量">
          {{ data.tableCount || 0 }}
        </el-descriptions-item>
        <el-descriptions-item label="最后同步时间">
          {{ formatDate(data.lastSyncTime) }}
        </el-descriptions-item>
        <el-descriptions-item label="创建人">
          {{ data.createUser || '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="创建时间">
          {{ formatDate(data.createTime) }}
        </el-descriptions-item>
        <el-descriptions-item label="更新人">
          {{ data.updateUser || '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="更新时间">
          {{ formatDate(data.updateTime) }}
        </el-descriptions-item>
      </el-descriptions>

      <el-descriptions :column="1" border style="margin-top: 20px;">
        <el-descriptions-item label="连接URL">
          <div class="url-container">
            <el-input
              :value="data.connectionUrl"
              readonly
              placeholder="连接URL"
            >
              <el-button
                slot="append"
                icon="el-icon-copy-document"
                @click="copyToClipboard(data.connectionUrl)"
              >
                复制
              </el-button>
            </el-input>
          </div>
        </el-descriptions-item>
        <el-descriptions-item label="描述">
          <div class="description-container">
            {{ data.description || '暂无描述' }}
          </div>
        </el-descriptions-item>
      </el-descriptions>

      <!-- 操作历史 -->
      <div class="operation-history" style="margin-top: 20px;">
        <h4>操作历史</h4>
        <el-timeline>
          <el-timeline-item
            v-if="data.createTime"
            timestamp="创建"
            placement="top"
          >
            <el-card>
              <h4>数据源创建</h4>
              <p>创建人: {{ data.createUser || '-' }}</p>
              <p>创建时间: {{ formatDate(data.createTime) }}</p>
            </el-card>
          </el-timeline-item>
          <el-timeline-item
            v-if="data.lastSyncTime"
            timestamp="同步"
            placement="top"
          >
            <el-card>
              <h4>表结构同步</h4>
              <p>同步时间: {{ formatDate(data.lastSyncTime) }}</p>
              <p>表数量: {{ data.tableCount || 0 }}</p>
            </el-card>
          </el-timeline-item>
          <el-timeline-item
            v-if="data.updateTime"
            timestamp="更新"
            placement="top"
          >
            <el-card>
              <h4>配置更新</h4>
              <p>更新人: {{ data.updateUser || '-' }}</p>
              <p>更新时间: {{ formatDate(data.updateTime) }}</p>
            </el-card>
          </el-timeline-item>
        </el-timeline>
      </div>
    </div>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">关闭</el-button>
      <el-button type="primary" @click="handleEdit">编辑</el-button>
    </div>
  </el-dialog>
</template>

<script>
export default {
  name: 'DataSourceDetailDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    data: {
      type: Object,
      default: () => ({})
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
  methods: {
    // 获取数据源类型标签类型
    getSourceTypeTagType(sourceType) {
      const typeMap = {
        'DM': 'primary',
        'ORACLE': 'success',
        'MYSQL': 'warning'
      }
      return typeMap[sourceType] || 'info'
    },

    // 获取数据源类型文本
    getSourceTypeText(sourceType) {
      const typeMap = {
        'DM': '达梦数据库',
        'ORACLE': 'Oracle数据库',
        'MYSQL': 'MySQL数据库'
      }
      return typeMap[sourceType] || sourceType
    },

    // 格式化日期
    formatDate(date) {
      if (!date) return '-'
      return new Date(date).toLocaleString('zh-CN')
    },

    // 复制到剪贴板
    copyToClipboard(text) {
      if (!text) {
        this.$message.warning('没有可复制的内容')
        return
      }

      // 创建临时文本域
      const textarea = document.createElement('textarea')
      textarea.value = text
      document.body.appendChild(textarea)
      textarea.select()
      
      try {
        document.execCommand('copy')
        this.$message.success('复制成功')
      } catch (error) {
        this.$message.error('复制失败')
        console.error('复制失败:', error)
      }
      
      document.body.removeChild(textarea)
    },

    // 编辑
    handleEdit() {
      this.$emit('edit', this.data)
      this.handleClose()
    },

    // 关闭对话框
    handleClose() {
      this.dialogVisible = false
    }
  }
}
</script>

<style scoped>
.detail-container {
  max-height: 600px;
  overflow-y: auto;
}

.url-container {
  width: 100%;
}

.description-container {
  min-height: 60px;
  padding: 10px;
  background-color: #f5f7fa;
  border-radius: 4px;
  word-break: break-all;
}

.operation-history h4 {
  margin-bottom: 15px;
  color: #303133;
  font-weight: 600;
}

.el-timeline-item .el-card {
  margin-bottom: 10px;
}

.el-timeline-item .el-card h4 {
  margin: 0 0 10px 0;
  font-size: 14px;
  color: #409eff;
}

.el-timeline-item .el-card p {
  margin: 5px 0;
  font-size: 13px;
  color: #606266;
}

.dialog-footer {
  text-align: right;
}

/* 自定义描述列表样式 */
.el-descriptions {
  margin-bottom: 0;
}

.el-descriptions ::v-deep .el-descriptions__label {
  font-weight: 600;
  color: #303133;
  background-color: #fafafa;
}

.el-descriptions ::v-deep .el-descriptions__content {
  color: #606266;
}
</style>
