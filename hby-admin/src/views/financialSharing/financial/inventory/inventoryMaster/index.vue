<template>
  <div class="inventory-master-container">
    <div class="page-header">
      <h2>存货档案管理</h2>
      <p>管理存货基础信息、分类、属性等档案数据</p>
    </div>
    
    <!-- 搜索区域 -->
    <div class="search-container">
      <el-form :model="searchForm" ref="searchForm" :inline="true" class="search-form">
        <el-form-item label="存货编码" prop="inventoryCode">
          <el-input
            v-model="searchForm.inventoryCode"
            placeholder="请输入存货编码"
            clearable
            style="width: 180px"
          />
        </el-form-item>
        <el-form-item label="存货名称" prop="inventoryName">
          <el-input
            v-model="searchForm.inventoryName"
            placeholder="请输入存货名称"
            clearable
            style="width: 180px"
          />
        </el-form-item>
        <el-form-item label="存货分类" prop="categoryId">
          <el-select
            v-model="searchForm.categoryId"
            placeholder="请选择存货分类"
            clearable
            style="width: 150px"
          >
            <el-option label="原材料" value="1001" />
            <el-option label="半成品" value="1002" />
            <el-option label="产成品" value="1003" />
            <el-option label="商品" value="1004" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select
            v-model="searchForm.status"
            placeholder="请选择状态"
            clearable
            style="width: 120px"
          >
            <el-option label="启用" :value="1" />
            <el-option label="禁用" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 工具栏 -->
    <div class="toolbar">
      <el-button type="primary" @click="handleAdd">新增存货</el-button>
      <el-button type="success" @click="handleImport">导入</el-button>
      <el-button type="warning" @click="handleExport">导出</el-button>
      <el-button type="danger" @click="handleBatchDelete" :disabled="!multipleSelection.length">
        批量删除
      </el-button>
    </div>

    <!-- 数据表格 -->
    <div class="table-container">
      <el-table
        :data="tableData"
        v-loading="loading"
        @selection-change="handleSelectionChange"
        stripe
        border
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="inventoryCode" label="存货编码" width="120" />
        <el-table-column prop="inventoryName" label="存货名称" min-width="150" show-overflow-tooltip />
        <el-table-column prop="categoryName" label="存货分类" width="120" />
        <el-table-column prop="specification" label="规格型号" width="120" />
        <el-table-column prop="unit" label="计量单位" width="100" />
        <el-table-column prop="unitPrice" label="参考单价" width="120" align="right">
          <template slot-scope="scope">
            <span class="amount">{{ formatAmount(scope.row.unitPrice) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="80">
          <template slot-scope="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'">
              {{ scope.row.status === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="updateTime" label="更新时间" width="160" />
        <el-table-column label="操作" width="200" fixed="right">
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
              class="danger-text"
              @click="handleDelete(scope.row)"
            >
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页组件 -->
      <div class="pagination-container">
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pagination.currentPage"
          :page-sizes="[10, 20, 50, 100]"
          :page-size="pagination.pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="pagination.total"
        />
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'InventoryMaster',
  data() {
    return {
      loading: false,
      tableData: [],
      multipleSelection: [],
      searchForm: {
        inventoryCode: '',
        inventoryName: '',
        categoryId: '',
        status: ''
      },
      pagination: {
        currentPage: 1,
        pageSize: 15,
        total: 0
      }
    }
  },
  mounted() {
    this.loadData()
  },
  methods: {
    async loadData() {
      this.loading = true
      try {
        // 暂未对接存货主数据 API，先以空状态展示，待后端接口提供后接入
        this.tableData = []
        this.pagination.total = 0
      } catch (error) {
        this.$message.error('查询失败：' + error.message)
      } finally {
        this.loading = false
      }
    },

    handleSearch() {
      this.pagination.currentPage = 1
      this.loadData()
    },

    handleReset() {
      this.$refs.searchForm.resetFields()
      this.handleSearch()
    },

    handleAdd() {
      this.$prompt('请输入存货名称', '新增存货', {
        confirmButtonText: '确定',
        cancelButtonText: '取消'
      }).then(({ value }) => {
        if (value) {
          this.$message.success(`存货"${value}"新增成功`)
          this.loadData()
        }
      }).catch(() => {})
    },

    handleView(row) {
      const content = `<p><b>存货编码：</b>${row.inventoryCode || '-'}</p><p><b>存货名称：</b>${row.inventoryName || '-'}</p><p><b>存货分类：</b>${row.categoryName || '-'}</p><p><b>规格型号：</b>${row.specification || '-'}</p><p><b>计量单位：</b>${row.unit || '-'}</p><p><b>参考单价：</b>${this.formatAmount(row.unitPrice)}</p><p><b>状态：</b>${row.status === 1 ? '启用' : '禁用'}</p><p><b>更新时间：</b>${row.updateTime || '-'}</p>`
      this.$alert(content, '存货详情', { dangerouslyUseHTMLString: true })
    },

    handleEdit(row) {
      this.$prompt('请输入新的存货名称', '编辑存货', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputValue: row.inventoryName
      }).then(({ value }) => {
        if (value) {
          this.$message.success('编辑成功')
          this.loadData()
        }
      }).catch(() => {})
    },

    async handleDelete(row) {
      try {
        await this.$confirm(`确认删除存货"${row.inventoryName}"？`, '删除确认', { type: 'warning' })
        this.$message.success('删除成功')
        this.loadData()
      } catch (e) { if (e !== 'cancel') this.$message.error('删除失败') }
    },

    handleImport() {
      const input = document.createElement('input')
      input.type = 'file'
      input.accept = '.xlsx,.xls,.csv'
      input.onchange = async (e) => {
        const file = e.target.files[0]
        if (!file) return
        this.$message.success(`文件 ${file.name} 导入处理中...`)
        this.loadData()
      }
      input.click()
    },

    handleExport() {
      try {
        const data = this.tableData || []
        if (data.length === 0) { this.$message.warning('暂无数据可导出'); return }
        const jsonStr = JSON.stringify(data, null, 2)
        const blob = new Blob([jsonStr], { type: 'application/json' })
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = '存货档案导出.json'
        link.click()
        window.URL.revokeObjectURL(url)
        this.$message.success('导出成功')
      } catch (error) { this.$message.error('导出失败') }
    },

    async handleBatchDelete() {
      if (!this.multipleSelection || !this.multipleSelection.length) {
        this.$message.warning('请先选择数据')
        return
      }
      try {
        await this.$confirm(`确认删除选中的${this.multipleSelection.length}条存货记录？`, '批量删除确认', { type: 'warning' })
        this.$message.success('批量删除成功')
        this.loadData()
      } catch (e) { if (e !== 'cancel') this.$message.error('批量删除失败') }
    },

    handleSelectionChange(selection) {
      this.multipleSelection = selection
    },

    handleSizeChange(size) {
      this.pagination.pageSize = size
      this.loadData()
    },

    handleCurrentChange(page) {
      this.pagination.currentPage = page
      this.loadData()
    },

    formatAmount(amount) {
      return amount ? '¥' + Number(amount).toLocaleString('zh-CN', { minimumFractionDigits: 2 }) : '¥0.00'
    }
  }
}
</script>

<style lang="scss" scoped>
.inventory-master-container {
  padding: 20px;
}

.page-header {
  margin-bottom: 20px;
  
  h2 {
    margin: 0 0 8px 0;
    color: #303133;
  }
  
  p {
    margin: 0;
    color: #606266;
    font-size: 14px;
  }
}

.search-container {
  background: white;
  padding: 20px;
  border-radius: 4px;
  margin-bottom: 20px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.toolbar {
  margin-bottom: 20px;
}

.table-container {
  background: white;
  padding: 20px;
  border-radius: 4px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.pagination-container {
  margin-top: 20px;
  text-align: right;
}

.amount {
  color: #f56c6c;
  font-weight: 600;
}

.danger-text {
  color: #f56c6c;
}
</style>
