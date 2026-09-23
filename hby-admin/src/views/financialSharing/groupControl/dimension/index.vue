<template>
  <div class="dimension-container">
    <!-- 查询表单 -->
    <div class="search-container">
      <el-form :model="searchForm" ref="searchForm" :inline="true" class="search-form">
        <el-form-item label="维度编码" prop="dimensionCode">
          <el-input
            v-model="searchForm.dimensionCode"
            placeholder="请输入维度编码"
            clearable
            style="width: 180px"
          />
        </el-form-item>
        <el-form-item label="维度名称" prop="dimensionName">
          <el-input
            v-model="searchForm.dimensionName"
            placeholder="请输入维度名称"
            clearable
            style="width: 180px"
          />
        </el-form-item>
        <el-form-item label="维度类型" prop="dimensionType">
          <el-select
            v-model="searchForm.dimensionType"
            placeholder="请选择维度类型"
            clearable
            style="width: 150px"
          >
            <el-option label="账户" value="ACCOUNT" />
            <el-option label="组织" value="ORGANIZATION" />
            <el-option label="项目" value="PROJECT" />
            <el-option label="产品" value="PRODUCT" />
            <el-option label="客户" value="CUSTOMER" />
            <el-option label="供应商" value="SUPPLIER" />
            <el-option label="自定义" value="CUSTOM" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select
            v-model="searchForm.status"
            placeholder="请选择状态"
            clearable
            style="width: 120px"
          >
            <el-option label="启用" value="ACTIVE" />
            <el-option label="停用" value="INACTIVE" />
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
      <el-button type="primary" @click="handleAdd">新增维度</el-button>
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
        <el-table-column prop="dimensionCode" label="维度编码" width="150" />
        <el-table-column prop="dimensionName" label="维度名称" min-width="180" show-overflow-tooltip />
        <el-table-column prop="dimensionType" label="维度类型" width="120">
          <template slot-scope="scope">
            <el-tag :type="getDimensionTypeColor(scope.row.dimensionType)" size="small">
              {{ getDimensionTypeName(scope.row.dimensionType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="dimensionCategory" label="维度分类" width="120" />
        <el-table-column prop="isHierarchy" label="是否分层" width="100">
          <template slot-scope="scope">
            <el-tag :type="scope.row.isHierarchy === 'Y' ? 'success' : 'info'" size="small">
              {{ scope.row.isHierarchy === 'Y' ? '是' : '否' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="maxLevel" label="最大层级" width="100" />
        <el-table-column prop="status" label="状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="scope.row.status === 'ACTIVE' ? 'success' : 'danger'" size="small">
              {{ scope.row.status === 'ACTIVE' ? '启用' : '停用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="description" label="描述" min-width="200" show-overflow-tooltip />
        <el-table-column prop="createTime" label="创建时间" width="160" />
        <el-table-column label="操作" width="280" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" @click="handleView(scope.row)">查看</el-button>
            <el-button size="mini" type="primary" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button 
              size="mini" 
              :type="scope.row.status === 'ACTIVE' ? 'warning' : 'success'"
              @click="handleToggleStatus(scope.row)"
            >
              {{ scope.row.status === 'ACTIVE' ? '停用' : '启用' }}
            </el-button>
            <el-button size="mini" type="danger" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-container">
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pagination.pageNumber"
          :page-sizes="[10, 20, 50, 100]"
          :page-size="pagination.pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="pagination.total"
        />
      </div>
    </div>

    <!-- 维度表单对话框 -->
    <dimension-form
      v-if="formVisible"
      :visible.sync="formVisible"
      :form-type="formType"
      :form-data="formData"
      @success="handleFormSuccess"
    />
  </div>
</template>

<script>
import { getDimensionList, deleteDimension, batchDeleteDimension, updateDimensionStatus } from '@/api/financialSharing/groupControl/dimension'
import DimensionForm from './components/DimensionForm.vue'

export default {
  name: 'Dimension',
  components: {
    DimensionForm
  },
  data() {
    return {
      loading: false,
      tableData: [],
      multipleSelection: [],
      searchForm: {
        dimensionCode: '',
        dimensionName: '',
        dimensionType: '',
        status: ''
      },
      pagination: {
        pageNumber: 1,
        pageSize: 20,
        total: 0
      },
      formVisible: false,
      formType: 'add', // add, edit, view
      formData: null
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    // 查询数据
    async fetchData() {
      this.loading = true
      try {
        const params = {
          ...this.searchForm,
          pageNumber: this.pagination.pageNumber,
          pageSize: this.pagination.pageSize
        }
        const res = await getDimensionList(params)
        if (res.code === 1) {
          this.tableData = res.data.records || []
          this.pagination.total = res.data.total || 0
        } else {
          this.$message.error(res.msg || '查询失败')
        }
      } catch (error) {
        console.error('查询维度列表失败:', error)
        this.$message.error('查询失败')
      } finally {
        this.loading = false
      }
    },

    // 查询
    handleSearch() {
      this.pagination.pageNumber = 1
      this.fetchData()
    },

    // 重置
    handleReset() {
      this.$refs.searchForm.resetFields()
      this.pagination.pageNumber = 1
      this.fetchData()
    },

    // 分页大小改变
    handleSizeChange(val) {
      this.pagination.pageSize = val
      this.fetchData()
    },

    // 当前页改变
    handleCurrentChange(val) {
      this.pagination.pageNumber = val
      this.fetchData()
    },

    // 表格选择改变
    handleSelectionChange(selection) {
      this.multipleSelection = selection
    },

    // 新增
    handleAdd() {
      this.formType = 'add'
      this.formData = null
      this.formVisible = true
    },

    // 查看
    handleView(row) {
      this.formType = 'view'
      this.formData = { ...row }
      this.formVisible = true
    },

    // 编辑
    handleEdit(row) {
      this.formType = 'edit'
      this.formData = { ...row }
      this.formVisible = true
    },

    // 删除
    handleDelete(row) {
      this.$confirm('确定要删除该维度吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const res = await deleteDimension(row.dimensionId)
          if (res.code === 1) {
            this.$message.success('删除成功')
            this.fetchData()
          } else {
            this.$message.error(res.msg || '删除失败')
          }
        } catch (error) {
          console.error('删除维度失败:', error)
          this.$message.error('删除失败')
        }
      }).catch(() => {})
    },

    // 批量删除
    handleBatchDelete() {
      if (!this.multipleSelection.length) {
        this.$message.warning('请选择要删除的维度')
        return
      }
      this.$confirm(`确定要删除选中的${this.multipleSelection.length}个维度吗?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const dimensionIds = this.multipleSelection.map(item => item.dimensionId)
          const res = await batchDeleteDimension(dimensionIds)
          if (res.code === 1) {
            this.$message.success('批量删除成功')
            this.fetchData()
          } else {
            this.$message.error(res.msg || '批量删除失败')
          }
        } catch (error) {
          console.error('批量删除维度失败:', error)
          this.$message.error('批量删除失败')
        }
      }).catch(() => {})
    },

    // 切换状态
    handleToggleStatus(row) {
      const newStatus = row.status === 'ACTIVE' ? 'INACTIVE' : 'ACTIVE'
      const statusText = newStatus === 'ACTIVE' ? '启用' : '停用'
      this.$confirm(`确定要${statusText}该维度吗?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const res = await updateDimensionStatus(row.dimensionId, newStatus)
          if (res.code === 1) {
            this.$message.success(`${statusText}成功`)
            this.fetchData()
          } else {
            this.$message.error(res.msg || `${statusText}失败`)
          }
        } catch (error) {
          console.error('更新维度状态失败:', error)
          this.$message.error(`${statusText}失败`)
        }
      }).catch(() => {})
    },

    // 表单提交成功
    handleFormSuccess() {
      this.formVisible = false
      this.fetchData()
    },

    // 获取维度类型名称
    getDimensionTypeName(type) {
      const typeMap = {
        'ACCOUNT': '账户',
        'ORGANIZATION': '组织',
        'PROJECT': '项目',
        'PRODUCT': '产品',
        'CUSTOMER': '客户',
        'SUPPLIER': '供应商',
        'CUSTOM': '自定义'
      }
      return typeMap[type] || type
    },

    // 获取维度类型颜色
    getDimensionTypeColor(type) {
      const colorMap = {
        'ACCOUNT': 'primary',
        'ORGANIZATION': 'success',
        'PROJECT': 'warning',
        'PRODUCT': 'info',
        'CUSTOMER': 'danger',
        'SUPPLIER': '',
        'CUSTOM': 'warning'
      }
      return colorMap[type] || ''
    }
  }
}
</script>

<style scoped lang="scss">
.dimension-container {
  padding: 20px;

  .search-container {
    background: #fff;
    padding: 20px;
    margin-bottom: 20px;
    border-radius: 4px;
  }

  .toolbar {
    background: #fff;
    padding: 15px 20px;
    margin-bottom: 20px;
    border-radius: 4px;
  }

  .table-container {
    background: #fff;
    padding: 20px;
    border-radius: 4px;

    .pagination-container {
      margin-top: 20px;
      text-align: right;
    }
  }
}
</style>

