<template>
  <div class="target-management-list">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-title">
        <h2>目标管理</h2>
        <p>管理和跟踪企业目标设定与执行</p>
      </div>
      <div class="header-actions">
        <el-button type="primary" icon="el-icon-plus" @click="handleCreate">
          新建目标
        </el-button>
        <el-button icon="el-icon-download" @click="handleExport">
          导出目标
        </el-button>
      </div>
    </div>

    <!-- 搜索筛选区域 -->
    <div class="search-section">
      <el-form :model="searchForm" inline class="search-form">
        <el-form-item label="目标名称">
          <el-input
            v-model="searchForm.keyword"
            placeholder="请输入目标名称"
            clearable
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="目标类型">
          <el-select v-model="searchForm.targetType" placeholder="请选择" clearable style="width: 150px">
            <el-option label="战略目标" value="STRATEGIC" />
            <el-option label="业务目标" value="BUSINESS" />
            <el-option label="个人目标" value="PERSONAL" />
          </el-select>
        </el-form-item>
        <el-form-item label="目标状态">
          <el-select v-model="searchForm.targetStatus" placeholder="请选择" clearable style="width: 150px">
            <el-option label="草稿" value="DRAFT" />
            <el-option label="进行中" value="IN_PROGRESS" />
            <el-option label="已完成" value="COMPLETED" />
            <el-option label="已暂停" value="PAUSED" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleSearch">
            搜索
          </el-button>
          <el-button icon="el-icon-refresh" @click="handleReset">
            重置
          </el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 数据表格 -->
    <div class="table-section">
      <el-table
        v-loading="loading"
        :data="tableData"
        stripe
        border
        style="width: 100%"
      >
        <el-table-column prop="targetCode" label="目标编码" width="150" />
        <el-table-column prop="targetName" label="目标名称" min-width="200" show-overflow-tooltip />
        <el-table-column prop="targetType" label="目标类型" width="120">
          <template slot-scope="scope">
            <el-tag :type="getTargetTypeTagType(scope.row.targetType)">
              {{ formatTargetType(scope.row.targetType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="targetStatus" label="目标状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="getTargetStatusTagType(scope.row.targetStatus)">
              {{ formatTargetStatus(scope.row.targetStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="progress" label="完成进度" width="120">
          <template slot-scope="scope">
            <el-progress :percentage="scope.row.progress || 0" :stroke-width="6" />
          </template>
        </el-table-column>
        <el-table-column prop="startDate" label="开始日期" width="120">
          <template slot-scope="scope">
            {{ formatDate(scope.row.startDate) }}
          </template>
        </el-table-column>
        <el-table-column prop="endDate" label="结束日期" width="120">
          <template slot-scope="scope">
            {{ formatDate(scope.row.endDate) }}
          </template>
        </el-table-column>
        <el-table-column prop="createdBy" label="创建人" width="100" />
        <el-table-column label="操作" width="180" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" @click="handleView(scope.row)">
              查看
            </el-button>
            <el-button size="mini" type="primary" @click="handleEdit(scope.row)">
              编辑
            </el-button>
            <el-button size="mini" type="danger" @click="handleDelete(scope.row)">
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-section">
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pagination.current"
          :page-sizes="[10, 20, 50, 100]"
          :page-size="pagination.size"
          layout="total, sizes, prev, pager, next, jumper"
          :total="pagination.total"
        />
      </div>
    </div>
  </div>
</template>

<script>
import {
  queryTargetPage,
  deleteTarget,
  exportTargets
} from '@/api/managementAccountant/pm/targetManagement'

export default {
  name: 'TargetManagementList',
  data() {
    return {
      loading: false,
      tableData: [],
      searchForm: {
        keyword: '',
        targetType: '',
        targetStatus: ''
      },
      pagination: {
        current: 1,
        size: 20,
        total: 0
      }
    }
  },
  created() {
    this.loadData()
  },
  methods: {
    // 加载数据
    async loadData() {
      this.loading = true
      try {
        const params = {
          current: this.pagination.current,
          size: this.pagination.size,
          keyword: this.searchForm.keyword,
          targetType: this.searchForm.targetType,
          targetStatus: this.searchForm.targetStatus
        }
        
        const response = await queryTargetPage(params)
        if (response.success) {
          this.tableData = response.data.records
          this.pagination.total = response.data.total
        } else {
          this.$message.error(response.message || '查询失败')
        }
      } catch (error) {
        this.$message.error('查询失败：' + error.message)
      } finally {
        this.loading = false
      }
    },

    // 搜索
    handleSearch() {
      this.pagination.current = 1
      this.loadData()
    },

    // 重置
    handleReset() {
      this.searchForm = {
        keyword: '',
        targetType: '',
        targetStatus: ''
      }
      this.pagination.current = 1
      this.loadData()
    },

    // 新建
    handleCreate() {
      this.$router.push('/pm/target-management/create')
    },

    // 查看
    handleView(row) {
      this.$router.push(`/pm/target-management/detail/${row.targetId}`)
    },

    // 编辑
    handleEdit(row) {
      this.$router.push(`/pm/target-management/edit/${row.targetId}`)
    },

    // 删除
    async handleDelete(row) {
      try {
        await this.$confirm('确认删除该目标？删除后不可恢复！', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        
        const response = await deleteTarget(row.targetId)
        if (response.success) {
          this.$message.success('删除成功')
          this.loadData()
        } else {
          this.$message.error(response.message || '删除失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('删除失败：' + error.message)
        }
      }
    },

    // 导出
    async handleExport() {
      try {
        const response = await exportTargets({
          exportType: 'ALL'
        })
        
        if (response.success) {
          this.$message.success('导出成功')
        } else {
          this.$message.error(response.message || '导出失败')
        }
      } catch (error) {
        this.$message.error('导出失败：' + error.message)
      }
    },

    // 分页大小变化
    handleSizeChange(size) {
      this.pagination.size = size
      this.pagination.current = 1
      this.loadData()
    },

    // 当前页变化
    handleCurrentChange(current) {
      this.pagination.current = current
      this.loadData()
    },

    // 格式化方法
    formatTargetType(type) {
      const typeMap = {
        'STRATEGIC': '战略目标',
        'BUSINESS': '业务目标',
        'PERSONAL': '个人目标'
      }
      return typeMap[type] || type
    },

    formatTargetStatus(status) {
      const statusMap = {
        'DRAFT': '草稿',
        'IN_PROGRESS': '进行中',
        'COMPLETED': '已完成',
        'PAUSED': '已暂停'
      }
      return statusMap[status] || status
    },

    formatDate(date) {
      if (!date) return ''
      return this.$moment(date).format('YYYY-MM-DD')
    },

    // 标签类型
    getTargetTypeTagType(type) {
      const typeMap = {
        'STRATEGIC': 'primary',
        'BUSINESS': 'success',
        'PERSONAL': 'warning'
      }
      return typeMap[type] || ''
    },

    getTargetStatusTagType(status) {
      const statusMap = {
        'DRAFT': 'info',
        'IN_PROGRESS': 'primary',
        'COMPLETED': 'success',
        'PAUSED': 'warning'
      }
      return statusMap[status] || ''
    }
  }
}
</script>

<style scoped>
.target-management-list {
  padding: 20px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding-bottom: 20px;
  border-bottom: 1px solid #e4e7ed;
}

.header-title h2 {
  margin: 0 0 5px 0;
  color: #303133;
}

.header-title p {
  margin: 0;
  color: #909399;
  font-size: 14px;
}

.search-section {
  background: #f5f7fa;
  padding: 20px;
  border-radius: 4px;
  margin-bottom: 20px;
}

.search-form {
  margin: 0;
}

.table-section {
  background: white;
  border-radius: 4px;
  overflow: hidden;
}

.pagination-section {
  padding: 20px;
  text-align: right;
  border-top: 1px solid #e4e7ed;
}
</style>
