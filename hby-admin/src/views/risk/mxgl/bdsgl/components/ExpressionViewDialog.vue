<template>
  <el-dialog
    title="查看表达式规则"
    :visible.sync="visible"
    width="800px"
    :before-close="handleClose"
    append-to-body
  >
    <el-descriptions :column="2" border>
      <el-descriptions-item label="规则名称">{{ data.ruleName }}</el-descriptions-item>
      <el-descriptions-item label="规则编码">{{ data.ruleCode }}</el-descriptions-item>
      <el-descriptions-item label="规则类型">{{ getRuleTypeText(data.ruleType) }}</el-descriptions-item>
      <el-descriptions-item label="优先级">{{ data.priority }}</el-descriptions-item>
      <el-descriptions-item label="状态">
        <el-tag :type="getStatusType(data.status)">{{ getStatusText(data.status) }}</el-tag>
      </el-descriptions-item>
      <el-descriptions-item label="是否公开">
        <el-tag :type="data.isPublic ? 'success' : 'info'">
          {{ data.isPublic ? '是' : '否' }}
        </el-tag>
      </el-descriptions-item>
      <el-descriptions-item label="创建时间">{{ data.createTime }}</el-descriptions-item>
      <el-descriptions-item label="更新时间">{{ data.updateTime }}</el-descriptions-item>
      <el-descriptions-item label="规则描述" :span="2">{{ data.description || '暂无描述' }}</el-descriptions-item>
    </el-descriptions>
    
    <el-divider content-position="left">表达式内容</el-divider>
    <el-input
      v-model="data.expression"
      type="textarea"
      :rows="6"
      readonly
      placeholder="暂无表达式内容"
    />
    
    <el-divider content-position="left">标签</el-divider>
    <el-tag
      v-for="tag in data.tags"
      :key="tag"
      style="margin-right: 10px;"
    >
      {{ tag }}
    </el-tag>
    <span v-if="!data.tags || data.tags.length === 0" class="no-tags">暂无标签</span>
    
    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">关闭</el-button>
      <el-button type="primary" @click="handleEdit">编辑</el-button>
    </div>
  </el-dialog>
</template>

<script>
export default {
  name: 'ExpressionViewDialog',
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
  methods: {
    getRuleTypeText(type) {
      const typeMap = {
        'NUMERIC': '数值计算',
        'STRING': '字符串处理',
        'DATE': '日期处理',
        'LOGIC': '逻辑判断'
      }
      return typeMap[type] || type
    },
    
    getStatusText(status) {
      const statusMap = {
        'DRAFT': '草稿',
        'ACTIVE': '启用',
        'INACTIVE': '禁用'
      }
      return statusMap[status] || status
    },
    
    getStatusType(status) {
      const typeMap = {
        'DRAFT': 'warning',
        'ACTIVE': 'success',
        'INACTIVE': 'danger'
      }
      return typeMap[status] || 'default'
    },
    
    handleEdit() {
      this.$emit('edit', this.data)
      this.handleClose()
    },
    
    handleClose() {
      this.$emit('update:visible', false)
    }
  }
}
</script>

<style scoped>
.no-tags {
  color: #999;
  font-style: italic;
}

.dialog-footer {
  text-align: right;
}
</style>
