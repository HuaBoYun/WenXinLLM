<template>
  <el-dialog
    :title="`表 ${tableName} 数据预览`"
    :visible.sync="dialogVisible"
    width="1200px"
    :close-on-click-modal="false"
    :modal="true"
    :modal-append-to-body="true"
    :append-to-body="true"
    custom-class="data-preview-dialog"
    :z-index="3100"
  >
    <div class="data-preview-container">
      <!-- 预览设置 -->
      <div class="preview-settings">
        <el-row :gutter="20">
          <el-col :span="8">
            <el-input-number
              v-model="previewLimit"
              :min="1"
              :max="1000"
              label="预览行数"
              @change="handleLimitChange"
            />
            <span style="margin-left: 10px;">行</span>
          </el-col>
          <el-col :span="16" style="text-align: right;">
            <el-button @click="loadPreviewData" icon="el-icon-refresh" :loading="loading">
              刷新数据
            </el-button>
            <el-button @click="handleExport" icon="el-icon-download">
              导出数据
            </el-button>
          </el-col>
        </el-row>
      </div>

      <!-- 数据表格 -->
      <div class="preview-table">
        <el-table
          :data="previewData"
          v-loading="loading"
          border
          stripe
          style="width: 100%"
          max-height="500"
          :show-overflow-tooltip="true"
        >
          <el-table-column
            v-for="column in tableColumns"
            :key="column.prop"
            :prop="column.prop"
            :label="column.label"
            :width="column.width"
            show-overflow-tooltip
          >
            <template slot-scope="scope">
              <span v-if="scope.row[column.prop] !== null && scope.row[column.prop] !== undefined">
                {{ formatCellValue(scope.row[column.prop], column.type) }}
              </span>
              <span v-else class="null-value">NULL</span>
            </template>
          </el-table-column>
        </el-table>
      </div>

      <!-- 数据统计 -->
      <div class="data-statistics" v-if="previewData.length > 0">
        <el-row :gutter="20">
          <el-col :span="4">
            <div class="stat-card">
              <div class="stat-number">{{ actualRows }}</div>
              <div class="stat-label">预览行数</div>
            </div>
          </el-col>
          <el-col :span="4">
            <div class="stat-card">
              <div class="stat-number">{{ totalCount }}</div>
              <div class="stat-label">总记录数</div>
            </div>
          </el-col>
          <el-col :span="4">
            <div class="stat-card">
              <div class="stat-number">{{ tableColumns.length }}</div>
              <div class="stat-label">列数</div>
            </div>
          </el-col>
          <el-col :span="4">
            <div class="stat-card">
              <div class="stat-number">{{ nullValueCount }}</div>
              <div class="stat-label">空值数量</div>
            </div>
          </el-col>
          <el-col :span="4">
            <div class="stat-card">
              <div class="stat-number">{{ uniqueValueCount }}</div>
              <div class="stat-label">唯一值数量</div>
            </div>
          </el-col>
          <el-col :span="4">
            <div class="stat-card">
              <div class="stat-number">{{ previewLimit }}</div>
              <div class="stat-label">限制条数</div>
            </div>
          </el-col>
        </el-row>
      </div>

      <!-- 空数据提示 -->
      <div v-if="!loading && previewData.length === 0" class="empty-data">
        <el-empty description="暂无数据" />
      </div>
    </div>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">关闭</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { getTableDataPreview, getTableColumns } from '@/api/mxgl'

export default {
  name: 'DataPreviewDialog',
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
      previewLimit: 10,
      previewData: [],
      tableColumns: [],
      totalCount: 0,
      actualRows: 0
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
    },
    // 空值数量统计
    nullValueCount() {
      let count = 0
      this.previewData.forEach(row => {
        this.tableColumns.forEach(col => {
          if (row[col.prop] === null || row[col.prop] === undefined || row[col.prop] === '') {
            count++
          }
        })
      })
      return count
    },
    // 唯一值数量统计(简单估算)
    uniqueValueCount() {
      const uniqueValues = new Set()
      this.previewData.forEach(row => {
        this.tableColumns.forEach(col => {
          if (row[col.prop] !== null && row[col.prop] !== undefined) {
            uniqueValues.add(`${col.prop}:${row[col.prop]}`)
          }
        })
      })
      return uniqueValues.size
    }
  },
  watch: {
    visible(val) {
      if (val && this.tableName && this.dataSourceId) {
        this.loadTableStructure()
        this.loadPreviewData()
      }
    }
  },
  methods: {
    // 加载表结构
    async loadTableStructure() {
      try {
        const response = await getTableColumns({
          dataSourceId: this.dataSourceId,
          tableName: this.tableName
        })
        if (response.code === 1) {
          const columns = response.data || []
          this.tableColumns = columns.map(col => ({
            prop: col.columnName,
            label: col.columnComment || col.columnName,
            type: col.columnType,
            width: this.getColumnWidth(col.columnType, col.columnName)
          }))
        }
      } catch (error) {
        console.error('获取表结构失败:', error)
      }
    },

    // 加载预览数据
    async loadPreviewData() {
      this.loading = true
      try {
        const response = await getTableDataPreview({
          dataSourceId: this.dataSourceId,
          tableName: this.tableName,
          limit: this.previewLimit
        })
        if (response.code === 1) {
          // 后端返回的数据结构：{ rows: [], columns: [], totalCount: 0, ... }
          const data = response.data || {}
          this.previewData = data.rows || []
          this.tableColumns = (data.columns || []).map(col => ({
            prop: col.columnName,
            label: col.columnLabel || col.columnName,
            type: col.columnType,
            width: this.getColumnWidth(col.columnType, col.columnName)
          }))
          this.totalCount = data.totalCount || 0
          this.actualRows = data.actualRows || 0
        } else {
          this.$message.error(response.msg || '查询失败')
          this.previewData = []
          this.tableColumns = []
          this.totalCount = 0
          this.actualRows = 0
        }
      } catch (error) {
        this.$message.error('查询失败')
        console.error('获取预览数据失败:', error)
        this.previewData = []
        this.tableColumns = []
        this.totalCount = 0
        this.actualRows = 0
      } finally {
        this.loading = false
      }
    },

    // 获取列宽度
    getColumnWidth(columnType, columnName) {
      // 根据数据类型和列名设置合适的宽度
      if (columnType.includes('DATE') || columnType.includes('TIME')) {
        return 160
      } else if (columnType.includes('NUMBER') || columnType.includes('INT')) {
        return 120
      } else if (columnName.length > 15) {
        return 200
      } else {
        return 150
      }
    },

    // 格式化单元格值
    formatCellValue(value, type) {
      if (value === null || value === undefined) {
        return 'NULL'
      }

      // 根据数据类型格式化显示
      if (type && type.includes('DATE')) {
        try {
          return new Date(value).toLocaleString('zh-CN')
        } catch (e) {
          return value
        }
      } else if (type && type.includes('NUMBER')) {
        // 数字类型保留适当精度
        if (typeof value === 'number') {
          return value.toLocaleString()
        }
      }

      // 字符串长度限制
      const str = String(value)
      if (str.length > 100) {
        return str.substring(0, 100) + '...'
      }

      return str
    },

    // 预览行数变化
    handleLimitChange() {
      if (this.previewLimit > 0) {
        this.loadPreviewData()
      }
    },

    // 导出数据
    handleExport() {
      // TODO: 实现数据导出功能
      this.$message.info('数据导出功能待实现')
    },

    // 关闭对话框
    handleClose() {
      this.dialogVisible = false
      this.previewData = []
      this.tableColumns = []
      this.totalCount = 0
      this.actualRows = 0
      this.previewLimit = 10
    }
  }
}
</script>

<style scoped>
.data-preview-container {
  padding: 0;
}

.preview-settings {
  margin-bottom: 20px;
  padding: 15px;
  background-color: #f5f7fa;
  border-radius: 4px;
}

.preview-table {
  margin-bottom: 20px;
}

.data-statistics {
  margin-bottom: 20px;
}

.stat-card {
  background: #fff;
  padding: 15px;
  border-radius: 4px;
  text-align: center;
  border: 1px solid #e4e7ed;
}

.stat-number {
  font-size: 18px;
  font-weight: bold;
  color: #409eff;
  margin-bottom: 5px;
}

.stat-label {
  font-size: 12px;
  color: #909399;
}

.empty-data {
  text-align: center;
  padding: 50px 0;
}

.null-value {
  color: #c0c4cc;
  font-style: italic;
}

.dialog-footer {
  text-align: right;
}

/* 表格样式优化 */
.el-table .el-table__cell {
  padding: 6px 0;
}

.el-table .cell {
  padding-left: 8px;
  padding-right: 8px;
  font-size: 12px;
}

/* 数字输入框样式 */
.el-input-number {
  width: 120px;
}

/* 修复嵌套对话框层级问题 */
.data-preview-dialog {
  z-index: 3100 !important;
}

.data-preview-dialog .el-dialog__wrapper {
  z-index: 3100 !important;
}

.data-preview-dialog .el-overlay {
  z-index: 3099 !important;
}

.data-preview-dialog .el-dialog {
  z-index: 3101 !important;
  position: relative;
}

.data-preview-dialog .el-dialog__body {
  position: relative;
  z-index: 3102 !important;
}

.data-preview-dialog .el-dialog__footer {
  position: relative;
  z-index: 3102 !important;
}
</style>
