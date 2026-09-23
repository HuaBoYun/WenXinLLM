<template>
  <div class="datasource-management">
    <!-- 查询条件 -->
    <div class="search-form">
      <el-form :model="queryForm" ref="queryForm" :inline="true" label-width="100px">
        <el-form-item label="数据源名称">
          <el-input
            v-model="queryForm.sourceName"
            placeholder="请输入数据源名称"
            clearable
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="数据源类型">
          <el-select v-model="queryForm.sourceType" placeholder="请选择数据源类型" clearable style="width: 150px">
            <el-option label="达梦数据库" value="DM" />
            <el-option label="Oracle数据库" value="ORACLE" />
            <el-option label="MySQL数据库" value="MYSQL" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="queryForm.status" placeholder="请选择状态" clearable style="width: 120px">
            <el-option label="活跃" value="ACTIVE" />
            <el-option label="禁用" value="INACTIVE" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch" icon="el-icon-search">查询</el-button>
          <el-button @click="handleReset" icon="el-icon-refresh">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 操作按钮 -->
    <div class="toolbar">
      <el-button type="primary" @click="handleAdd" icon="el-icon-plus">新增数据源</el-button>
      <el-button type="danger" @click="handleBatchDelete" icon="el-icon-delete" :disabled="!multipleSelection.length">
        批量删除
      </el-button>
      <el-button type="info" @click="handleRefreshStatistics" icon="el-icon-refresh">刷新统计</el-button>
    </div>

    <!-- 统计信息 -->
    <div class="statistics-cards">
      <el-row :gutter="20">
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-number">{{ statistics.totalCount || 0 }}</div>
            <div class="stat-label">总数据源</div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card active">
            <div class="stat-number">{{ statistics.activeCount || 0 }}</div>
            <div class="stat-label">活跃数据源</div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card success">
            <div class="stat-number">{{ statistics.successCount || 0 }}</div>
            <div class="stat-label">连接成功</div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card failed">
            <div class="stat-number">{{ statistics.failedCount || 0 }}</div>
            <div class="stat-label">连接失败</div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 数据表格 -->
    <div class="table-container">
      <el-table
        :data="tableData"
        v-loading="loading"
        @selection-change="handleSelectionChange"
        border
        stripe
        style="width: 100%"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="sourceName" label="数据源名称" min-width="150" show-overflow-tooltip />
        <el-table-column prop="sourceType" label="数据源类型" width="120">
          <template slot-scope="scope">
            <el-tag :type="getSourceTypeTagType(scope.row.sourceType)">
              {{ getSourceTypeText(scope.row.sourceType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="hostIp" label="主机IP" width="130" />
        <el-table-column prop="port" label="端口" width="80" />
        <el-table-column prop="databaseName" label="数据库名" min-width="120" show-overflow-tooltip />
        <el-table-column prop="tableCount" label="表数量" width="80" />
        <el-table-column prop="status" label="状态" width="80">
          <template slot-scope="scope">
            <el-tag :type="scope.row.status === 'ACTIVE' ? 'success' : 'danger'">
              {{ scope.row.status === 'ACTIVE' ? '活跃' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="connectionTestResult" label="连接状态" width="100">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.connectionTestResult" :type="scope.row.connectionTestResult === 'SUCCESS' ? 'success' : 'danger'">
              {{ scope.row.connectionTestResult === 'SUCCESS' ? '成功' : '失败' }}
            </el-tag>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="160">
          <template slot-scope="scope">
            {{ formatDate(scope.row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="320" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" @click="handleView(scope.row)" icon="el-icon-view">查看</el-button>
            <el-button size="mini" type="primary" @click="handleEdit(scope.row)" icon="el-icon-edit">编辑</el-button>
            <el-button size="mini" type="warning" @click="handleTestConnection(scope.row)" icon="el-icon-connection">
              测试连接
            </el-button>
            <el-button size="mini" type="info" @click="handleSyncTables(scope.row)" icon="el-icon-refresh">
              同步表
            </el-button>
            <el-dropdown @command="handleMoreAction" trigger="click">
              <el-button size="mini" type="text">
                更多<i class="el-icon-arrow-down el-icon--right"></i>
              </el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item :command="{action: 'tableStructure', row: scope.row}">
                  <i class="el-icon-s-grid"></i> 表结构管理
                </el-dropdown-item>
                <el-dropdown-item :command="{action: 'enable', row: scope.row}" v-if="scope.row.status === 'INACTIVE'">
                  启用
                </el-dropdown-item>
                <el-dropdown-item :command="{action: 'disable', row: scope.row}" v-if="scope.row.status === 'ACTIVE'">
                  禁用
                </el-dropdown-item>
                <el-dropdown-item :command="{action: 'delete', row: scope.row}" divided>
                  删除
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

    <!-- 新增/编辑对话框 -->
    <DataSourceDialog
      :visible.sync="dialogVisible"
      :form-data="currentRow"
      :is-edit="isEdit"
      @success="handleDialogSuccess"
    />

    <!-- 查看详情对话框 -->
    <DataSourceDetailDialog
      :visible.sync="detailDialogVisible"
      :data="currentRow"
    />

    <!-- 表结构管理对话框 -->
    <TableStructureDialog
      :visible.sync="tableStructureDialogVisible"
      :data-source-id="currentDataSourceId"
      :data-source-info="currentDataSource"
    />
  </div>
</template>

<script>
import {
  getDataSourceList,
  deleteDataSource,
  batchDeleteDataSource,
  testDataSourceConnection,
  syncTableStructure,
  getDataSourceStatistics,
  enableDataSource,
  disableDataSource
} from '@/api/mxgl'
import DataSourceDialog from './components/DataSourceDialog'
import DataSourceDetailDialog from './components/DataSourceDetailDialog'
import TableStructureDialog from './components/TableStructureDialog'

export default {
  name: 'DataSourceManagement',
  components: {
    DataSourceDialog,
    DataSourceDetailDialog,
    TableStructureDialog
  },
  data() {
    return {
      // 查询表单
      queryForm: {
        pageNum: 1,
        pageSize: 20,
        sourceName: '',
        sourceType: '',
        status: ''
      },
      // 表格数据
      tableData: [],
      total: 0,
      loading: false,
      multipleSelection: [],
      // 统计信息
      statistics: {},
      // 对话框
      dialogVisible: false,
      detailDialogVisible: false,
      tableStructureDialogVisible: false,
      currentRow: {},
      isEdit: false,
      currentDataSourceId: '',
      currentDataSource: {}
    }
  },
  created() {
    this.loadData()
    this.loadStatistics()
  },
  methods: {
    // 加载数据
    async loadData() {
      this.loading = true
      try {
        const response = await getDataSourceList(this.queryForm)
        if (response.code === 1) {
          this.tableData = response.data.records || []
          this.total = response.data.total || 0
        } else {
          this.$message.error(response.msg || '查询失败')
        }
      } catch (error) {
        this.$message.error('查询失败')
        console.error('查询数据源列表失败:', error)
      } finally {
        this.loading = false
      }
    },

    // 加载统计信息
    async loadStatistics() {
      try {
        const response = await getDataSourceStatistics()
        if (response.code === 1) {
          this.statistics = response.data || {}
        }
      } catch (error) {
        console.error('获取统计信息失败:', error)
      }
    },

    // 查询
    handleSearch() {
      this.queryForm.pageNum = 1
      this.loadData()
    },

    // 重置
    handleReset() {
      this.$refs.queryForm.resetFields()
      this.queryForm.pageNum = 1
      this.loadData()
    },

    // 新增
    handleAdd() {
      this.currentRow = {}
      this.isEdit = false
      this.dialogVisible = true
    },

    // 编辑
    handleEdit(row) {
      this.currentRow = { ...row }
      this.isEdit = true
      this.dialogVisible = true
    },

    // 查看
    handleView(row) {
      this.currentRow = { ...row }
      this.detailDialogVisible = true
    },

    // 测试连接
    async handleTestConnection(row) {
      const loading = this.$loading({
        lock: true,
        text: '正在测试连接...',
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.7)'
      })

      try {
        const response = await testDataSourceConnection(row.sourceId)
        if (response.code === 1) {
          this.$message.success('连接测试成功')
        } else {
          this.$message.error(response.msg || '连接测试失败')
        }
        // 刷新数据
        this.loadData()
      } catch (error) {
        this.$message.error('连接测试失败')
        console.error('测试连接失败:', error)
      } finally {
        loading.close()
      }
    },

    // 同步表结构
    async handleSyncTables(row) {
      const loading = this.$loading({
        lock: true,
        text: '正在同步表结构...',
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.7)'
      })

      try {
        const response = await syncTableStructure(row.sourceId)
        if (response.code === 1) {
          this.$message.success(response.msg || '同步成功')
        } else {
          this.$message.error(response.msg || '同步失败')
        }
        // 刷新数据
        this.loadData()
      } catch (error) {
        this.$message.error('同步失败')
        console.error('同步表结构失败:', error)
      } finally {
        loading.close()
      }
    },

    // 表结构管理
    handleTableStructure(row) {
      this.currentDataSourceId = row.sourceId
      this.currentDataSource = row
      this.tableStructureDialogVisible = true
    },

    // 更多操作
    async handleMoreAction(command) {
      const { action, row } = command
      
      switch (action) {
        case 'tableStructure':
          this.handleTableStructure(row)
          break
        case 'enable':
          await this.handleEnable(row)
          break
        case 'disable':
          await this.handleDisable(row)
          break
        case 'delete':
          await this.handleDelete(row)
          break
      }
    },

    // 启用
    async handleEnable(row) {
      try {
        const response = await enableDataSource(row.sourceId)
        if (response.code === 1) {
          this.$message.success('启用成功')
          this.loadData()
          this.loadStatistics()
        } else {
          this.$message.error(response.msg || '启用失败')
        }
      } catch (error) {
        this.$message.error('启用失败')
        console.error('启用数据源失败:', error)
      }
    },

    // 禁用
    async handleDisable(row) {
      try {
        const response = await disableDataSource(row.sourceId)
        if (response.code === 1) {
          this.$message.success('禁用成功')
          this.loadData()
          this.loadStatistics()
        } else {
          this.$message.error(response.msg || '禁用失败')
        }
      } catch (error) {
        this.$message.error('禁用失败')
        console.error('禁用数据源失败:', error)
      }
    },

    // 删除
    async handleDelete(row) {
      try {
        await this.$confirm('确定要删除该数据源吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })

        const response = await deleteDataSource(row.sourceId)
        if (response.code === 1) {
          this.$message.success('删除成功')
          this.loadData()
          this.loadStatistics()
        } else {
          this.$message.error(response.msg || '删除失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('删除失败')
          console.error('删除数据源失败:', error)
        }
      }
    },

    // 批量删除
    async handleBatchDelete() {
      if (!this.multipleSelection.length) {
        this.$message.warning('请选择要删除的数据源')
        return
      }

      try {
        await this.$confirm(`确定要删除选中的 ${this.multipleSelection.length} 个数据源吗？`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })

        const sourceIds = this.multipleSelection.map(item => item.sourceId)
        const response = await batchDeleteDataSource(sourceIds)
        if (response.code === 1) {
          this.$message.success('批量删除成功')
          this.loadData()
          this.loadStatistics()
        } else {
          this.$message.error(response.msg || '批量删除失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('批量删除失败')
          console.error('批量删除数据源失败:', error)
        }
      }
    },

    // 刷新统计
    handleRefreshStatistics() {
      this.loadStatistics()
      this.$message.success('统计信息已刷新')
    },

    // 表格选择变化
    handleSelectionChange(selection) {
      this.multipleSelection = selection
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

    // 对话框成功回调
    handleDialogSuccess() {
      this.loadData()
      this.loadStatistics()
    },

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
    }
  }
}
</script>

<style scoped>
.datasource-management {
  padding: 20px;
}

.search-form {
  background: #fff;
  padding: 20px;
  border-radius: 4px;
  margin-bottom: 20px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.toolbar {
  margin-bottom: 20px;
}

.statistics-cards {
  margin-bottom: 20px;
}

.stat-card {
  background: #fff;
  padding: 20px;
  border-radius: 4px;
  text-align: center;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  border-left: 4px solid #409eff;
}

.stat-card.active {
  border-left-color: #67c23a;
}

.stat-card.success {
  border-left-color: #67c23a;
}

.stat-card.failed {
  border-left-color: #f56c6c;
}

.stat-number {
  font-size: 24px;
  font-weight: bold;
  color: #303133;
  margin-bottom: 8px;
}

.stat-label {
  font-size: 14px;
  color: #909399;
}

.table-container {
  background: #fff;
  border-radius: 4px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.pagination-container {
  padding: 20px;
  text-align: right;
}
</style>
