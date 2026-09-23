<template>
  <el-dialog
    :title="`表 ${tableName} 的列信息`"
    :visible.sync="dialogVisible"
    width="1000px"
    :close-on-click-modal="false"
    :modal="true"
    :modal-append-to-body="true"
    :append-to-body="true"
    custom-class="column-detail-dialog"
    :z-index="3000"
  >
    <div class="column-detail-container">
      <!-- 表信息 -->
      <div class="table-info">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="表名">{{ tableName }}</el-descriptions-item>
          <el-descriptions-item label="列数量">{{ columnData.length }}</el-descriptions-item>
        </el-descriptions>
      </div>

      <!-- 列信息表格 -->
      <div class="column-table">
        <el-table
          :data="columnData"
          v-loading="loading"
          border
          stripe
          style="width: 100%"
          max-height="500"
        >
          <el-table-column prop="columnOrder" label="序号" width="60" />
          <el-table-column prop="columnName" label="列名" min-width="150" show-overflow-tooltip />
          <el-table-column prop="columnType" label="数据类型" width="120" />
          <el-table-column label="长度/精度" width="100">
            <template slot-scope="scope">
              <span v-if="scope.row.columnLength">
                {{ scope.row.columnLength }}
                <span v-if="scope.row.columnScale">,{{ scope.row.columnScale }}</span>
              </span>
              <span v-else>-</span>
            </template>
          </el-table-column>
          <el-table-column prop="isNullable" label="可空" width="60">
            <template slot-scope="scope">
              <el-tag :type="scope.row.isNullable === 'Y' ? 'success' : 'danger'" size="mini">
                {{ scope.row.isNullable === 'Y' ? '是' : '否' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="isPrimaryKey" label="主键" width="60">
            <template slot-scope="scope">
              <el-tag v-if="scope.row.isPrimaryKey === 'Y'" type="warning" size="mini">
                <i class="el-icon-key"></i>
              </el-tag>
              <span v-else>-</span>
            </template>
          </el-table-column>
          <el-table-column prop="columnDefault" label="默认值" width="100" show-overflow-tooltip>
            <template slot-scope="scope">
              {{ scope.row.columnDefault || '-' }}
            </template>
          </el-table-column>
          <el-table-column prop="columnComment" label="注释" min-width="200" show-overflow-tooltip>
            <template slot-scope="scope">
              {{ scope.row.columnComment || '-' }}
            </template>
          </el-table-column>
        </el-table>
      </div>

      <!-- 统计信息 -->
      <div class="column-statistics">
        <el-row :gutter="20">
          <el-col :span="6">
            <div class="stat-card">
              <div class="stat-number">{{ columnData.length }}</div>
              <div class="stat-label">总列数</div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="stat-card">
              <div class="stat-number">{{ primaryKeyCount }}</div>
              <div class="stat-label">主键列</div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="stat-card">
              <div class="stat-number">{{ nullableCount }}</div>
              <div class="stat-label">可空列</div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="stat-card">
              <div class="stat-number">{{ commentedCount }}</div>
              <div class="stat-label">有注释列</div>
            </div>
          </el-col>
        </el-row>
      </div>

      <!-- 数据类型分布 -->
      <div class="type-distribution">
        <h4>数据类型分布</h4>
        <el-row :gutter="10">
          <el-col :span="4" v-for="(count, type) in typeDistribution" :key="type">
            <div class="type-item">
              <el-tag>{{ type }}</el-tag>
              <span class="type-count">{{ count }}</span>
            </div>
          </el-col>
        </el-row>
      </div>
    </div>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleExport" icon="el-icon-download">导出Excel</el-button>
      <el-button @click="handleClose">关闭</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { getTableColumns } from '@/api/mxgl'

export default {
  name: 'ColumnDetailDialog',
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
      columnData: []
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
    // 主键列数量
    primaryKeyCount() {
      return this.columnData.filter(col => col.isPrimaryKey === 'Y').length
    },
    // 可空列数量
    nullableCount() {
      return this.columnData.filter(col => col.isNullable === 'Y').length
    },
    // 有注释的列数量
    commentedCount() {
      return this.columnData.filter(col => col.columnComment && col.columnComment.trim()).length
    },
    // 数据类型分布
    typeDistribution() {
      const distribution = {}
      this.columnData.forEach(col => {
        const type = col.columnType
        distribution[type] = (distribution[type] || 0) + 1
      })
      return distribution
    }
  },
  watch: {
    visible(val) {
      if (val && this.tableName && this.dataSourceId) {
        this.loadColumnData()
      }
    }
  },
  methods: {
    // 加载列数据
    async loadColumnData() {
      this.loading = true
      try {
        const response = await getTableColumns({
          dataSourceId: this.dataSourceId,
          tableName: this.tableName
        })
        if (response.code === 1) {
          this.columnData = response.data || []
        } else {
          this.$message.error(response.msg || '查询失败')
        }
      } catch (error) {
        this.$message.error('查询失败')
        console.error('查询列信息失败:', error)
      } finally {
        this.loading = false
      }
    },

    // 导出Excel
    handleExport() {
      // TODO: 实现Excel导出功能
      this.$message.info('Excel导出功能待实现')
    },

    // 关闭对话框
    handleClose() {
      this.dialogVisible = false
      this.columnData = []
    }
  }
}
</script>

<style scoped>
.column-detail-container {
  padding: 0;
}

.table-info {
  margin-bottom: 20px;
}

.column-table {
  margin-bottom: 20px;
}

.column-statistics {
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
  font-size: 20px;
  font-weight: bold;
  color: #409eff;
  margin-bottom: 5px;
}

.stat-label {
  font-size: 12px;
  color: #909399;
}

.type-distribution {
  margin-bottom: 20px;
}

.type-distribution h4 {
  margin-bottom: 15px;
  color: #303133;
  font-weight: 600;
}

.type-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 8px 12px;
  margin-bottom: 8px;
  background-color: #f5f7fa;
  border-radius: 4px;
}

.type-count {
  font-weight: bold;
  color: #409eff;
}

.dialog-footer {
  text-align: right;
}

/* 表格样式优化 */
.el-table .el-table__cell {
  padding: 8px 0;
}

.el-table .cell {
  padding-left: 10px;
  padding-right: 10px;
}

/* 修复嵌套对话框层级问题 */
.column-detail-dialog {
  z-index: 3000 !important;
}

.column-detail-dialog .el-dialog__wrapper {
  z-index: 3000 !important;
}

.column-detail-dialog .el-overlay {
  z-index: 2999 !important;
}

/* 确保对话框内容可点击 */
.column-detail-dialog .el-dialog {
  z-index: 3001 !important;
  position: relative;
}

.column-detail-dialog .el-dialog__body {
  position: relative;
  z-index: 3002 !important;
}

.column-detail-dialog .el-dialog__footer {
  position: relative;
  z-index: 3002 !important;
}
</style>
