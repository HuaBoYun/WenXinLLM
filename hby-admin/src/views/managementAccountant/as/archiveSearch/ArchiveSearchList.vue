<template>
  <div class="archive-search-list">
    <!-- 搜索区域 -->
    <div class="search-area">
      <el-form :model="searchForm" :inline="true" class="search-form">
        <el-form-item label="检索名称">
          <el-input
            v-model="searchForm.searchName"
            placeholder="请输入检索名称"
            clearable
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="检索类型">
          <el-select
            v-model="searchForm.searchType"
            placeholder="请选择检索类型"
            clearable
            style="width: 150px"
          >
            <el-option
              v-for="(label, value) in searchTypeOptions"
              :key="value"
              :label="label"
              :value="value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="检索状态">
          <el-select
            v-model="searchForm.searchStatus"
            placeholder="请选择检索状态"
            clearable
            style="width: 150px"
          >
            <el-option
              v-for="(label, value) in searchStatusOptions"
              :key="value"
              :label="label"
              :value="value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="检索引擎">
          <el-select
            v-model="searchForm.searchEngine"
            placeholder="请选择检索引擎"
            clearable
            style="width: 150px"
          >
            <el-option
              v-for="(label, value) in searchEngineOptions"
              :key="value"
              :label="label"
              :value="value"
            />
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

    <!-- 操作区域 -->
    <div class="operation-area">
      <div class="operation-left">
        <el-button type="primary" icon="el-icon-plus" @click="handleCreate">
          新建检索配置
        </el-button>
        <el-button
          type="success"
          icon="el-icon-video-play"
          :disabled="!hasSelection"
          @click="handleBatchStart"
        >
          批量启动
        </el-button>
        <el-button
          type="warning"
          icon="el-icon-video-pause"
          :disabled="!hasSelection"
          @click="handleBatchStop"
        >
          批量停止
        </el-button>
        <el-button
          type="danger"
          icon="el-icon-delete"
          :disabled="!hasSelection"
          @click="handleBatchDelete"
        >
          批量删除
        </el-button>
      </div>
      <div class="operation-right">
        <el-button icon="el-icon-download" @click="handleExport">
          导出
        </el-button>
        <el-button icon="el-icon-upload2" @click="handleImport">
          导入
        </el-button>
        <el-button icon="el-icon-refresh" @click="handleRefresh">
          刷新
        </el-button>
      </div>
    </div>

    <!-- 表格区域 -->
    <div class="table-area">
      <el-table
        v-loading="loading"
        :data="tableData"
        @selection-change="handleSelectionChange"
        @sort-change="handleSortChange"
        stripe
        style="width: 100%"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="searchCode" label="检索编号" width="120" sortable="custom" />
        <el-table-column prop="searchName" label="检索名称" width="180" show-overflow-tooltip />
        <el-table-column prop="searchType" label="检索类型" width="120">
          <template slot-scope="scope">
            <el-tag :type="getSearchTypeTagType(scope.row.searchType)">
              {{ formatSearchType(scope.row.searchType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="searchStatus" label="检索状态" width="120">
          <template slot-scope="scope">
            <el-tag :type="getSearchStatusTagType(scope.row.searchStatus)">
              {{ formatSearchStatus(scope.row.searchStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="searchEngine" label="检索引擎" width="120">
          <template slot-scope="scope">
            {{ formatSearchEngine(scope.row.searchEngine) }}
          </template>
        </el-table-column>
        <el-table-column prop="indexStatus" label="索引状态" width="120">
          <template slot-scope="scope">
            <el-tag :type="getIndexStatusTagType(scope.row.indexStatus)">
              {{ formatIndexStatus(scope.row.indexStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="searchAccuracy" label="检索准确率" width="120" sortable="custom">
          <template slot-scope="scope">
            {{ formatAccuracy(scope.row.searchAccuracy) }}
          </template>
        </el-table-column>
        <el-table-column prop="avgResponseTime" label="平均响应时间" width="140" sortable="custom">
          <template slot-scope="scope">
            {{ formatResponseTime(scope.row.avgResponseTime) }}
          </template>
        </el-table-column>
        <el-table-column prop="totalCount" label="检索次数" width="100" sortable="custom" />
        <el-table-column prop="lastSearchTime" label="最后检索时间" width="160">
          <template slot-scope="scope">
            {{ formatDateTime(scope.row.lastSearchTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="280" fixed="right">
          <template slot-scope="scope">
            <el-button type="text" size="small" @click="handleView(scope.row)">
              查看
            </el-button>
            <el-button type="text" size="small" @click="handleEdit(scope.row)">
              编辑
            </el-button>
            <el-button
              type="text"
              size="small"
              :class="{ 'text-success': scope.row.searchStatus === 'INACTIVE', 'text-warning': scope.row.searchStatus === 'ACTIVE' }"
              @click="handleToggleStatus(scope.row)"
            >
              {{ scope.row.searchStatus === 'ACTIVE' ? '停止' : '启动' }}
            </el-button>
            <el-button type="text" size="small" @click="handleSearch(scope.row)">
              检索
            </el-button>
            <el-dropdown @command="handleMoreAction">
              <el-button type="text" size="small">
                更多<i class="el-icon-arrow-down el-icon--right"></i>
              </el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item :command="{action: 'rebuildIndex', row: scope.row}">
                  重建索引
                </el-dropdown-item>
                <el-dropdown-item :command="{action: 'optimizeIndex', row: scope.row}">
                  优化索引
                </el-dropdown-item>
                <el-dropdown-item :command="{action: 'performance', row: scope.row}">
                  性能监控
                </el-dropdown-item>
                <el-dropdown-item :command="{action: 'copy', row: scope.row}">
                  复制配置
                </el-dropdown-item>
                <el-dropdown-item :command="{action: 'delete', row: scope.row}" divided>
                  删除
                </el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 分页区域 -->
    <div class="pagination-area">
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="pagination.current"
        :page-sizes="[10, 20, 50, 100]"
        :page-size="pagination.size"
        :total="pagination.total"
        layout="total, sizes, prev, pager, next, jumper"
      />
    </div>
  </div>
</template>

<script>
import {
  getArchiveSearchPage,
  deleteArchiveSearch,
  startSearchService,
  stopSearchService,
  batchDeleteArchiveSearches,
  batchUpdateStatus,
  rebuildIndex,
  optimizeIndex,
  exportArchiveSearchData,
  SEARCH_TYPES,
  SEARCH_STATUS,
  SEARCH_ENGINES,
  INDEX_STATUS,
  formatSearchType,
  formatSearchStatus,
  formatSearchEngine,
  formatIndexStatus,
  formatAccuracy,
  formatResponseTime
} from '@/api/managementAccountant/as/archiveSearch'

export default {
  name: 'ArchiveSearchList',
  data() {
    return {
      loading: false,
      searchForm: {
        searchName: '',
        searchType: '',
        searchStatus: '',
        searchEngine: ''
      },
      tableData: [],
      selectedRows: [],
      pagination: {
        current: 1,
        size: 20,
        total: 0
      },
      sortField: '',
      sortOrder: '',
      searchTypeOptions: {
        [SEARCH_TYPES.FULL_TEXT]: '全文检索',
        [SEARCH_TYPES.SEMANTIC]: '语义检索',
        [SEARCH_TYPES.IMAGE]: '图像检索',
        [SEARCH_TYPES.VOICE]: '语音检索',
        [SEARCH_TYPES.HYBRID]: '混合检索'
      },
      searchStatusOptions: {
        [SEARCH_STATUS.ACTIVE]: '活跃',
        [SEARCH_STATUS.INACTIVE]: '非活跃',
        [SEARCH_STATUS.INDEXING]: '索引中',
        [SEARCH_STATUS.OPTIMIZING]: '优化中',
        [SEARCH_STATUS.ERROR]: '错误'
      },
      searchEngineOptions: {
        [SEARCH_ENGINES.ELASTICSEARCH]: 'Elasticsearch',
        [SEARCH_ENGINES.SOLR]: 'Apache Solr',
        [SEARCH_ENGINES.LUCENE]: 'Apache Lucene',
        [SEARCH_ENGINES.CUSTOM]: '自定义引擎'
      }
    }
  },
  computed: {
    hasSelection() {
      return this.selectedRows.length > 0
    }
  },
  created() {
    this.loadData()
  },
  methods: {
    async loadData() {
      this.loading = true
      try {
        const tenantId = this.$store.getters.tenantId
        const params = {
          current: this.pagination.current,
          size: this.pagination.size,
          ...this.searchForm
        }
        
        if (this.sortField) {
          params.sortField = this.sortField
          params.sortOrder = this.sortOrder
        }
        
        const response = await getArchiveSearchPage(tenantId, params)
        if (response.success) {
          this.tableData = response.data.records || []
          this.pagination.total = response.data.total || 0
        }
      } catch (error) {
        this.$message.error('加载数据失败: ' + error.message)
      } finally {
        this.loading = false
      }
    },
    
    handleSearch() {
      this.pagination.current = 1
      this.loadData()
    },
    
    handleReset() {
      this.searchForm = {
        searchName: '',
        searchType: '',
        searchStatus: '',
        searchEngine: ''
      }
      this.pagination.current = 1
      this.loadData()
    },
    
    handleCreate() {
      this.$router.push('/managementAccountant/as/archiveSearch/create')
    },
    
    handleView(row) {
      this.$router.push(`/managementAccountant/as/archiveSearch/detail/${row.searchId}`)
    },
    
    handleEdit(row) {
      this.$router.push(`/managementAccountant/as/archiveSearch/edit/${row.searchId}`)
    },
    
    async handleToggleStatus(row) {
      try {
        const tenantId = this.$store.getters.tenantId
        if (row.searchStatus === 'ACTIVE') {
          await stopSearchService(tenantId, row.searchId)
          this.$message.success('检索服务已停止')
        } else {
          await startSearchService(tenantId, row.searchId)
          this.$message.success('检索服务已启动')
        }
        this.loadData()
      } catch (error) {
        this.$message.error('操作失败: ' + error.message)
      }
    },
    
    handleSearch(row) {
      this.$router.push(`/managementAccountant/as/archiveSearch/search/${row.searchId}`)
    },
    
    async handleMoreAction(command) {
      const { action, row } = command
      const tenantId = this.$store.getters.tenantId
      
      try {
        switch (action) {
          case 'rebuildIndex':
            await rebuildIndex(tenantId, row.searchId)
            this.$message.success('索引重建已启动')
            break
          case 'optimizeIndex':
            await optimizeIndex(tenantId, row.searchId)
            this.$message.success('索引优化已启动')
            break
          case 'performance':
            this.$router.push(`/managementAccountant/as/archiveSearch/performance/${row.searchId}`)
            break
          case 'copy':
            this.handleCopy(row)
            break
          case 'delete':
            this.handleDelete(row)
            break
        }
      } catch (error) {
        this.$message.error('操作失败: ' + error.message)
      }
    },
    
    handleCopy(row) {
      const copyData = { ...row }
      delete copyData.searchId
      copyData.searchName = row.searchName + '_副本'
      copyData.searchCode = ''
      
      this.$router.push({
        path: '/managementAccountant/as/archiveSearch/create',
        query: { copyData: JSON.stringify(copyData) }
      })
    },
    
    async handleDelete(row) {
      try {
        await this.$confirm('确定要删除这个检索配置吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        
        const tenantId = this.$store.getters.tenantId
        await deleteArchiveSearch(tenantId, row.searchId)
        this.$message.success('删除成功')
        this.loadData()
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('删除失败: ' + error.message)
        }
      }
    },
    
    handleSelectionChange(selection) {
      this.selectedRows = selection
    },
    
    async handleBatchStart() {
      try {
        const tenantId = this.$store.getters.tenantId
        const searchIds = this.selectedRows.map(row => row.searchId)
        await batchUpdateStatus(tenantId, searchIds, 'ACTIVE')
        this.$message.success('批量启动成功')
        this.loadData()
      } catch (error) {
        this.$message.error('批量启动失败: ' + error.message)
      }
    },
    
    async handleBatchStop() {
      try {
        const tenantId = this.$store.getters.tenantId
        const searchIds = this.selectedRows.map(row => row.searchId)
        await batchUpdateStatus(tenantId, searchIds, 'INACTIVE')
        this.$message.success('批量停止成功')
        this.loadData()
      } catch (error) {
        this.$message.error('批量停止失败: ' + error.message)
      }
    },
    
    async handleBatchDelete() {
      try {
        await this.$confirm(`确定要删除选中的 ${this.selectedRows.length} 个检索配置吗？`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        
        const tenantId = this.$store.getters.tenantId
        const searchIds = this.selectedRows.map(row => row.searchId)
        await batchDeleteArchiveSearches(tenantId, searchIds)
        this.$message.success('批量删除成功')
        this.loadData()
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('批量删除失败: ' + error.message)
        }
      }
    },
    
    async handleExport() {
      try {
        const tenantId = this.$store.getters.tenantId
        const searchIds = this.selectedRows.length > 0 
          ? this.selectedRows.map(row => row.searchId)
          : this.tableData.map(row => row.searchId)
        
        const response = await exportArchiveSearchData(tenantId, searchIds)
        // 处理导出逻辑
        this.$message.success('导出成功')
      } catch (error) {
        this.$message.error('导出失败: ' + error.message)
      }
    },
    
    handleImport() {
      // 处理导入逻辑
      this.$message.info('导入功能开发中')
    },
    
    handleRefresh() {
      this.loadData()
    },
    
    handleSizeChange(val) {
      this.pagination.size = val
      this.pagination.current = 1
      this.loadData()
    },
    
    handleCurrentChange(val) {
      this.pagination.current = val
      this.loadData()
    },
    
    handleSortChange({ column, prop, order }) {
      this.sortField = prop
      this.sortOrder = order === 'ascending' ? 'asc' : 'desc'
      this.loadData()
    },
    
    // 格式化方法
    formatSearchType,
    formatSearchStatus,
    formatSearchEngine,
    formatIndexStatus,
    formatAccuracy,
    formatResponseTime,
    
    getSearchTypeTagType(type) {
      const typeMap = {
        [SEARCH_TYPES.FULL_TEXT]: 'primary',
        [SEARCH_TYPES.SEMANTIC]: 'success',
        [SEARCH_TYPES.IMAGE]: 'warning',
        [SEARCH_TYPES.VOICE]: 'info',
        [SEARCH_TYPES.HYBRID]: 'danger'
      }
      return typeMap[type] || 'info'
    },
    
    getSearchStatusTagType(status) {
      const statusMap = {
        [SEARCH_STATUS.ACTIVE]: 'success',
        [SEARCH_STATUS.INACTIVE]: 'info',
        [SEARCH_STATUS.INDEXING]: 'warning',
        [SEARCH_STATUS.OPTIMIZING]: 'primary',
        [SEARCH_STATUS.ERROR]: 'danger'
      }
      return statusMap[status] || 'info'
    },
    
    getIndexStatusTagType(status) {
      const statusMap = {
        [INDEX_STATUS.BUILDING]: 'warning',
        [INDEX_STATUS.READY]: 'success',
        [INDEX_STATUS.UPDATING]: 'primary',
        [INDEX_STATUS.OPTIMIZING]: 'info',
        [INDEX_STATUS.ERROR]: 'danger',
        [INDEX_STATUS.DELETED]: 'info'
      }
      return statusMap[status] || 'info'
    },
    
    formatDateTime(date) {
      if (!date) return '-'
      return new Date(date).toLocaleString()
    }
  }
}
</script>

<style lang="scss" scoped>
.archive-search-list {
  padding: 20px;
  
  .search-area {
    background: white;
    padding: 20px;
    border-radius: 8px;
    margin-bottom: 20px;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  }
  
  .operation-area {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;
  }
  
  .table-area {
    background: white;
    border-radius: 8px;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  }
  
  .pagination-area {
    margin-top: 20px;
    text-align: right;
  }
  
  .text-success {
    color: #67C23A;
  }
  
  .text-warning {
    color: #E6A23C;
  }
}
</style>
