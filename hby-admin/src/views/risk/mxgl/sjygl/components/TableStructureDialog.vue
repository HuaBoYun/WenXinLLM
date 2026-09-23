<template>
  <el-dialog
    title="表结构管理"
    :visible.sync="dialogVisible"
    width="1200px"
    :close-on-click-modal="false"
    :modal="true"
    :modal-append-to-body="true"
    :append-to-body="true"
    custom-class="table-structure-dialog"
    :z-index="2000"
    @close="handleClose"
  >
    <div class="table-structure-container">
      <!-- 数据源信息 -->
      <div class="datasource-info">
        <el-descriptions :column="4" border>
          <el-descriptions-item label="数据源名称">{{ dataSourceInfo.sourceName }}</el-descriptions-item>
          <el-descriptions-item label="数据源类型">{{ dataSourceInfo.sourceType }}</el-descriptions-item>
          <el-descriptions-item label="主机IP">{{ dataSourceInfo.hostIp }}</el-descriptions-item>
          <el-descriptions-item label="数据库名">{{ dataSourceInfo.databaseName }}</el-descriptions-item>
        </el-descriptions>
      </div>

      <!-- 操作工具栏 -->
      <div class="toolbar">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-input
              v-model="searchTableName"
              placeholder="搜索表名"
              clearable
              @clear="handleSearch"
              @keyup.enter.native="handleSearch"
            >
              <el-button slot="append" icon="el-icon-search" @click="handleSearch"></el-button>
            </el-input>
          </el-col>
          <el-col :span="12" style="text-align: right;">
            <el-button type="primary" @click="handleSyncAll" icon="el-icon-refresh" :loading="syncLoading">
              同步所有表
            </el-button>
            <el-button type="info" @click="handleRefreshStatistics" icon="el-icon-data-analysis">
              刷新统计
            </el-button>
          </el-col>
        </el-row>
      </div>

      <!-- 统计信息 -->
      <div class="statistics-info">
        <el-row :gutter="20">
          <el-col :span="8">
            <div class="stat-item">
              <span class="stat-label">表数量:</span>
              <span class="stat-value">{{ statistics.tableCount || 0 }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="stat-item">
              <span class="stat-label">列数量:</span>
              <span class="stat-value">{{ statistics.columnCount || 0 }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="stat-item">
              <span class="stat-label">最后同步:</span>
              <span class="stat-value">{{ formatDate(statistics.lastSyncTime) }}</span>
            </div>
          </el-col>
        </el-row>
      </div>

      <!-- 表列表 -->
      <div class="table-list">
        <el-table
          :data="tableData"
          v-loading="loading"
          border
          stripe
          style="width: 100%"
        >
          <el-table-column prop="TABLE_NAME" label="表名" min-width="200" show-overflow-tooltip />
          <el-table-column prop="TABLE_COMMENT" label="表注释" min-width="200" show-overflow-tooltip />
          <el-table-column prop="COLUMN_COUNT" label="列数量" width="100" />
          <el-table-column prop="SYNC_TIME" label="同步时间" width="160">
            <template slot-scope="scope">
              {{ formatDate(scope.row.SYNC_TIME) }}
            </template>
          </el-table-column>
          <el-table-column label="操作" width="300" fixed="right">
            <template slot-scope="scope">
              <el-button size="mini" @click.stop="handleViewColumns(scope.row)" icon="el-icon-view">
                查看列
              </el-button>
              <el-button size="mini" type="primary" @click.stop="handleSyncTable(scope.row)" icon="el-icon-refresh">
                同步
              </el-button>
              <el-button size="mini" type="info" @click.stop="handlePreviewData(scope.row)" icon="el-icon-document">
                预览数据
              </el-button>
              <el-dropdown @command="handleMoreAction" trigger="click" @click.stop>
                <el-button size="mini" type="text">
                  更多<i class="el-icon-arrow-down el-icon--right"></i>
                </el-button>
                <el-dropdown-menu slot="dropdown">
                  <el-dropdown-item :command="{action: 'ddl', row: scope.row}">
                    生成DDL
                  </el-dropdown-item>
                  <el-dropdown-item :command="{action: 'primaryKeys', row: scope.row}">
                    查看主键
                  </el-dropdown-item>
                  <el-dropdown-item :command="{action: 'foreignKeys', row: scope.row}">
                    查看外键
                  </el-dropdown-item>
                </el-dropdown-menu>
              </el-dropdown>
            </template>
          </el-table-column>
        </el-table>

        <!-- 分页 -->
        <div class="pagination-container">
          <el-pagination
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            :current-page="queryForm.pageNum"
            :page-sizes="[10, 20, 50, 100]"
            :page-size="queryForm.pageSize"
            layout="total, sizes, prev, pager, next, jumper"
            :total="total"
          />
        </div>
      </div>
    </div>

    <!-- 列详情对话框 -->
    <ColumnDetailDialog
      :visible.sync="columnDialogVisible"
      :table-name="currentTableName"
      :data-source-id="dataSourceId"
    />

    <!-- 数据预览对话框 -->
    <DataPreviewDialog
      :visible.sync="previewDialogVisible"
      :table-name="currentTableName"
      :data-source-id="dataSourceId"
    />

    <!-- DDL查看对话框 -->
    <DDLViewDialog
      :visible.sync="ddlDialogVisible"
      :table-name="currentTableName"
      :data-source-id="dataSourceId"
    />

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">关闭</el-button>
    </div>
  </el-dialog>
</template>

<script>
import {
  getTableList,
  getTableStatistics,
  syncTableStructure,
  syncSingleTable
} from '@/api/mxgl'
import ColumnDetailDialog from './ColumnDetailDialog'
import DataPreviewDialog from './DataPreviewDialog'
import DDLViewDialog from './DDLViewDialog'

export default {
  name: 'TableStructureDialog',
  components: {
    ColumnDetailDialog,
    DataPreviewDialog,
    DDLViewDialog
  },
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    dataSourceId: {
      type: String,
      default: ''
    },
    dataSourceInfo: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      loading: false,
      syncLoading: false,
      searchTableName: '',
      queryForm: {
        pageNum: 1,
        pageSize: 20,
        dataSourceId: '',
        tableName: ''
      },
      tableData: [],
      total: 0,
      statistics: {},
      // 子对话框
      columnDialogVisible: false,
      previewDialogVisible: false,
      ddlDialogVisible: false,
      currentTableName: ''
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
      if (val && this.dataSourceId) {
        this.queryForm.dataSourceId = this.dataSourceId
        this.loadData()
        this.loadStatistics()
      }
    }
  },
  methods: {
    // 加载表列表
    async loadData() {
      this.loading = true
      try {
        const response = await getTableList(this.queryForm)
        if (response.code === 1) {
          this.tableData = response.data.records || []
          this.total = response.data.total || 0
        } else {
          this.$message.error(response.msg || '查询失败')
        }
      } catch (error) {
        this.$message.error('查询失败')
        console.error('查询表列表失败:', error)
      } finally {
        this.loading = false
      }
    },

    // 加载统计信息
    async loadStatistics() {
      try {
        const response = await getTableStatistics(this.dataSourceId)
        if (response.code === 1) {
          this.statistics = response.data || {}
        }
      } catch (error) {
        console.error('获取统计信息失败:', error)
      }
    },

    // 搜索
    handleSearch() {
      this.queryForm.tableName = this.searchTableName
      this.queryForm.pageNum = 1
      this.loadData()
    },

    // 同步所有表
    async handleSyncAll() {
      try {
        await this.$confirm('确定要同步所有表结构吗？这可能需要一些时间。', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })

        this.syncLoading = true
        const response = await syncTableStructure(this.dataSourceId)
        if (response.code === 1) {
          this.$message.success(response.msg || '同步成功')
          this.loadData()
          this.loadStatistics()
        } else {
          this.$message.error(response.msg || '同步失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('同步失败')
          console.error('同步表结构失败:', error)
        }
      } finally {
        this.syncLoading = false
      }
    },

    // 同步单个表
    async handleSyncTable(row) {
      try {
        const response = await syncSingleTable({
          dataSourceId: this.dataSourceId,
          tableName: row.TABLE_NAME
        })
        if (response.code === 1) {
          this.$message.success(response.msg || '同步成功')
          this.loadData()
          this.loadStatistics()
        } else {
          this.$message.error(response.msg || '同步失败')
        }
      } catch (error) {
        this.$message.error('同步失败')
        console.error('同步表结构失败:', error)
      }
    },

    // 查看列详情
    handleViewColumns(row) {
      this.currentTableName = row.TABLE_NAME
      this.columnDialogVisible = true
    },

    // 预览数据
    handlePreviewData(row) {
      this.currentTableName = row.TABLE_NAME
      this.previewDialogVisible = true
    },

    // 更多操作
    handleMoreAction(command) {
      const { action, row } = command
      this.currentTableName = row.TABLE_NAME

      switch (action) {
        case 'ddl':
          this.ddlDialogVisible = true
          break
        case 'primaryKeys':
          this.showPrimaryKeys(row)
          break
        case 'foreignKeys':
          this.showForeignKeys(row)
          break
      }
    },

    // 显示主键信息
    async showPrimaryKeys(row) {
      // TODO: 实现主键查看功能
      this.$message.info('主键查看功能待实现')
    },

    // 显示外键信息
    async showForeignKeys(row) {
      // TODO: 实现外键查看功能
      this.$message.info('外键查看功能待实现')
    },



    // 刷新统计
    handleRefreshStatistics() {
      this.loadStatistics()
      this.$message.success('统计信息已刷新')
    },

    // 分页大小变化
    handleSizeChange(size) {
      this.queryForm.pageSize = size
      this.queryForm.pageNum = 1
      this.loadData()
    },

    // 当前页变化
    handleCurrentChange(page) {
      this.queryForm.pageNum = page
      this.loadData()
    },

    // 格式化日期
    formatDate(date) {
      if (!date) return '-'
      return new Date(date).toLocaleString('zh-CN')
    },

    // 关闭对话框
    handleClose() {
      this.dialogVisible = false
      this.searchTableName = ''
      this.queryForm.pageNum = 1
      this.queryForm.tableName = ''
      this.tableData = []
      this.statistics = {}
    }
  }
}
</script>

<style scoped>
.table-structure-container {
  padding: 0;
}

.datasource-info {
  margin-bottom: 20px;
}

.toolbar {
  margin-bottom: 20px;
}

.statistics-info {
  margin-bottom: 20px;
  padding: 15px;
  background-color: #f5f7fa;
  border-radius: 4px;
}

.stat-item {
  text-align: center;
}

.stat-label {
  font-size: 14px;
  color: #909399;
  margin-right: 8px;
}

.stat-value {
  font-size: 16px;
  font-weight: bold;
  color: #303133;
}

.table-list {
  background: #fff;
}

.pagination-container {
  padding: 20px 0;
  text-align: right;
}

.dialog-footer {
  text-align: right;
}

/* 表格行悬停效果 */
.el-table tbody tr:hover {
  cursor: pointer;
}

/* 父对话框层级设置 */
.table-structure-dialog {
  z-index: 2000 !important;
}

.table-structure-dialog .el-dialog__wrapper {
  z-index: 2000 !important;
}

.table-structure-dialog .el-overlay {
  z-index: 1999 !important;
}

.table-structure-dialog .el-dialog {
  z-index: 2001 !important;
}
</style>
