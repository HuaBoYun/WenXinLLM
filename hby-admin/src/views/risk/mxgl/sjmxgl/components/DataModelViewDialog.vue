<template>
  <el-dialog
    title="查看数据模型"
    :visible.sync="visible"
    width="900px"
    :before-close="handleClose"
    append-to-body
  >
    <el-tabs v-model="activeTab" type="border-card">
      <el-tab-pane label="基本信息" name="basic">
        <el-descriptions :column="2" border v-loading="loading">
          <el-descriptions-item label="模型名称">{{ currentData.modelName }}</el-descriptions-item>
          <el-descriptions-item label="模型编码">{{ currentData.modelCode }}</el-descriptions-item>
          <el-descriptions-item label="数据源">{{ currentData.dataSourceName }}</el-descriptions-item>
          <el-descriptions-item label="模型类型">{{ getModelTypeText(currentData.modelType) }}</el-descriptions-item>
          <el-descriptions-item label="状态">
            <el-tag :type="getStatusType(currentData.status)">{{ getStatusText(currentData.status) }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="是否启用">
            <el-tag :type="currentData.isEnabled === 'Y' ? 'success' : 'danger'">
              {{ currentData.isEnabled === 'Y' ? '启用' : '禁用' }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="创建时间">{{ currentData.createTime }}</el-descriptions-item>
          <el-descriptions-item label="更新时间">{{ currentData.updateTime }}</el-descriptions-item>
          <el-descriptions-item label="模型描述" :span="2">{{ currentData.businessMeaning || '暂无描述' }}</el-descriptions-item>
        </el-descriptions>
      </el-tab-pane>
      
      <el-tab-pane label="SQL内容" name="sql">
        <div class="sql-content">
          <el-input
            v-model="currentData.sqlStatement"
            type="textarea"
            :rows="15"
            readonly
            placeholder="暂无SQL内容"
          />
        </div>
      </el-tab-pane>
      
      <el-tab-pane label="字段信息" name="fields">

        <el-table :data="fieldList" border style="width: 100%" v-loading="fieldLoading">
          <el-table-column prop="fieldName" label="字段名" width="150" />
          <el-table-column prop="fieldType" label="字段类型" width="120" />
          <el-table-column prop="fieldLength" label="长度" width="80" />
          <el-table-column prop="nullable" label="可空" width="80">
            <template slot-scope="scope">
              <el-tag :type="scope.row.nullable ? 'success' : 'danger'">
                {{ scope.row.nullable ? '是' : '否' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="defaultValue" label="默认值" width="120" />
          <el-table-column prop="comment" label="备注" />
        </el-table>
      </el-tab-pane>
      

    </el-tabs>
    
    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">关闭</el-button>
      <el-button type="primary" @click="handleEdit">编辑</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { getDataModelDetail, parseSQLStatement } from '@/api/mxgl'

export default {
  name: 'DataModelViewDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    modelData: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      activeTab: 'basic',
      fieldList: [],
      loading: false,
      fieldLoading: false,
      detailData: {}
    }
  },
  computed: {
    // 合并列表数据和详情数据，详情数据优先
    currentData() {
      return { ...this.modelData, ...this.detailData }
    }
  },
  watch: {
    visible(val) {
      if (val && this.modelData.modelId) {
        this.loadModelDetail()
        this.loadFieldList()
      }
    }
  },
  methods: {
    // 加载模型详情
    async loadModelDetail() {
      if (!this.modelData.modelId) {
        return
      }

      this.loading = true
      try {
        const response = await getDataModelDetail(this.modelData.modelId)
        if (response.code === 1) {
          this.detailData = response.data || {}
        } else {
          this.$message.error(response.msg || '获取模型详情失败')
        }
      } catch (error) {
        console.error('获取模型详情失败:', error)
        this.$message.error('获取模型详情失败')
      } finally {
        this.loading = false
      }
    },

    getModelTypeText(type) {
      const typeMap = {
        'FINANCIAL': '财务模型',
        'RISK': '风险模型',
        'AUDIT': '审计模型',
        'BUSINESS': '业务模型'
      }
      return typeMap[type] || type
    },
    
    getStatusText(status) {
      const statusMap = {
        'DRAFT': '草稿',
        'PUBLISHED': '已发布',
        'ARCHIVED': '已归档'
      }
      return statusMap[status] || status
    },
    
    getStatusType(status) {
      const typeMap = {
        'DRAFT': 'warning',
        'PUBLISHED': 'success',
        'ARCHIVED': 'info'
      }
      return typeMap[status] || 'default'
    },
    
    async loadFieldList() {
      if (!this.currentData.sqlStatement) {
        this.fieldList = []
        return
      }

      this.fieldLoading = true

      try {
        const response = await parseSQLStatement({
          sqlStatement: this.currentData.sqlStatement,
          dataSourceId: this.currentData.dataSourceId
        })

        if (response.code === 1) {
          this.fieldList = response.data.fields || []
          if (this.fieldList.length === 0) {
            this.$message.warning('未能解析出字段信息，可能是SQL语句格式复杂')
          }
        } else {
          this.$message.error(response.msg || '解析SQL字段失败')
          // 显示空列表
          this.fieldList = []
        }
      } catch (error) {
        console.error('解析SQL字段失败:', error)
        this.$message.error('解析SQL字段失败')
        this.fieldList = []
      } finally {
        this.fieldLoading = false
      }
    },
    
    handleEdit() {
      this.$emit('edit', this.currentData)
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

.dialog-footer {
  text-align: right;
}
</style>
