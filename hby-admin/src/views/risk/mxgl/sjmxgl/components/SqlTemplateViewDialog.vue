<template>
  <el-dialog
    title="查看SQL模板"
    :visible.sync="visible"
    width="900px"
    :before-close="handleClose"
    append-to-body
  >
    <el-descriptions :column="2" border>
      <el-descriptions-item label="模板名称">{{ data.templateName }}</el-descriptions-item>
      <el-descriptions-item label="模板编码">{{ data.templateCode }}</el-descriptions-item>
      <el-descriptions-item label="模板类型">
        <el-tag :type="getTypeColor(data.templateType)">{{ data.templateType }}</el-tag>
      </el-descriptions-item>
      <el-descriptions-item label="分类">{{ getCategoryText(data.category) }}</el-descriptions-item>
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
      <el-descriptions-item label="模板描述" :span="2">{{ data.description || '暂无描述' }}</el-descriptions-item>
    </el-descriptions>
    
    <el-divider content-position="left">SQL内容</el-divider>
    <div class="sql-content">
      <el-input
        v-model="data.sqlContent"
        type="textarea"
        :rows="12"
        readonly
        placeholder="暂无SQL内容"
      />
    </div>
    
    <el-divider content-position="left">参数定义</el-divider>
    <div v-if="data.parameters && data.parameters.length > 0">
      <el-table :data="data.parameters" border style="width: 100%">
        <el-table-column prop="name" label="参数名" width="150" />
        <el-table-column prop="type" label="类型" width="100">
          <template slot-scope="scope">
            <el-tag size="small">{{ scope.row.type }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="description" label="说明" />
        <el-table-column prop="example" label="示例" width="150" />
      </el-table>
    </div>
    <div v-else class="no-params">
      <el-empty description="暂无参数定义" />
    </div>
    
    <el-divider content-position="left">使用统计</el-divider>
    <el-row :gutter="20">
      <el-col :span="6">
        <el-statistic title="使用次数" :value="data.useCount || 0" />
      </el-col>
      <el-col :span="6">
        <el-statistic title="最后使用" :value="data.lastUseTime || '未使用'" />
      </el-col>
      <el-col :span="6">
        <el-statistic title="平均执行时间" :value="data.avgExecuteTime || 0" suffix="ms" />
      </el-col>
      <el-col :span="6">
        <el-statistic title="成功率" :value="data.successRate || 0" suffix="%" />
      </el-col>
    </el-row>
    
    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">关闭</el-button>
      <el-button type="success" @click="handleUse">使用模板</el-button>
      <el-button type="primary" @click="handleEdit">编辑</el-button>
    </div>
  </el-dialog>
</template>

<script>
export default {
  name: 'SqlTemplateViewDialog',
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
    getTypeColor(type) {
      const colorMap = {
        'SELECT': 'success',
        'INSERT': 'primary',
        'UPDATE': 'warning',
        'DELETE': 'danger'
      }
      return colorMap[type] || 'default'
    },
    
    getCategoryText(category) {
      const categoryMap = {
        'BASIC': '基础查询',
        'ANALYSIS': '统计分析',
        'REPORT': '报表查询',
        'MAINTENANCE': '数据维护'
      }
      return categoryMap[category] || category
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
    
    handleUse() {
      this.$emit('use', this.data)
      this.handleClose()
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
.sql-content {
  margin: 10px 0;
}

.no-params {
  text-align: center;
  padding: 30px 0;
}

.dialog-footer {
  text-align: right;
}
</style>
