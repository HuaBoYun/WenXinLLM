<template>
  <div class="app-container">
    <!-- 查询条件 -->
    <div class="filter-container">
      <el-form :inline="true" :model="queryParams" class="demo-form-inline">
        <el-form-item label="影响因素编码">
          <el-input
            v-model="queryParams.factorCode"
            placeholder="请输入影响因素编码"
            clearable
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="影响因素名称">
          <el-input
            v-model="queryParams.factorName"
            placeholder="请输入影响因素名称"
            clearable
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="影响因素类型">
          <el-select
            v-model="queryParams.factorType"
            placeholder="请选择影响因素类型"
            clearable
            style="width: 200px"
          >
            <el-option
              v-for="(name, value) in FACTOR_TYPE_NAME"
              :key="value"
              :label="name"
              :value="parseInt(value)"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="数据类型">
          <el-select
            v-model="queryParams.dataType"
            placeholder="请选择数据类型"
            clearable
            style="width: 200px"
          >
            <el-option
              v-for="(name, value) in DATA_TYPE_NAME"
              :key="value"
              :label="name"
              :value="parseInt(value)"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="启用状态">
          <el-select
            v-model="queryParams.isEnabled"
            placeholder="请选择启用状态"
            clearable
            style="width: 200px"
          >
            <el-option
              v-for="(name, value) in ENABLED_STATUS_NAME"
              :key="value"
              :label="name"
              :value="parseInt(value)"
            />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleQuery">查询</el-button>
          <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 操作按钮 -->
    <div class="filter-container">
      <el-button
        type="primary"
        icon="el-icon-plus"
        @click="handleAdd"
      >
        新增
      </el-button>
      <el-button
        type="success"
        icon="el-icon-check"
        :disabled="!multipleSelection.length"
        @click="handleBatchEnable"
      >
        批量启用
      </el-button>
      <el-button
        type="warning"
        icon="el-icon-close"
        :disabled="!multipleSelection.length"
        @click="handleBatchDisable"
      >
        批量禁用
      </el-button>
      <el-button
        type="danger"
        icon="el-icon-delete"
        :disabled="!multipleSelection.length"
        @click="handleBatchDelete"
      >
        批量删除
      </el-button>
    </div>

    <!-- 数据表格 -->
    <el-table
      v-loading="loading"
      :data="tableData"
      border
      stripe
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column prop="factorCode" label="影响因素编码" width="150" />
      <el-table-column prop="factorName" label="影响因素名称" width="200" />
      <el-table-column prop="factorTypeName" label="影响因素类型" width="120" />
      <el-table-column prop="dataTypeName" label="数据类型" width="100" />
      <el-table-column prop="dataLength" label="数据长度" width="100" />
      <el-table-column prop="decimalPlaces" label="小数位数" width="100" />
      <el-table-column prop="isRequiredName" label="是否必填" width="100" />
      <el-table-column prop="defaultValue" label="默认值" width="150" />
      <el-table-column prop="description" label="描述" min-width="200" show-overflow-tooltip />
      <el-table-column prop="isEnabledName" label="启用状态" width="100">
        <template slot-scope="scope">
          <el-tag :type="scope.row.isEnabled === 1 ? 'success' : 'danger'">
            {{ scope.row.isEnabledName }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="sortOrder" label="排序号" width="100" />
      <el-table-column label="操作" width="200" fixed="right">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handleView(scope.row)"
          >
            查看
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleEdit(scope.row)"
          >
            编辑
          </el-button>
          <el-button
            size="mini"
            type="text"
            :icon="scope.row.isEnabled === 1 ? 'el-icon-close' : 'el-icon-check'"
            @click="handleToggleStatus(scope.row)"
          >
            {{ scope.row.isEnabled === 1 ? '禁用' : '启用' }}
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            style="color: #f56c6c"
            @click="handleDelete(scope.row)"
          >
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <el-pagination
      v-show="total > 0"
      background
      :current-page="queryParams.pageNumber"
      :page-size="queryParams.pageSize"
      :total="total"
      layout="total, sizes, prev, pager, next, jumper"
      @current-change="handleCurrentChange"
      @size-change="handleSizeChange"
    />

    <!-- 表单弹窗 -->
    <influence-factor-form
      v-if="formVisible"
      :visible.sync="formVisible"
      :form-type="formType"
      :form-data="formData"
      @success="handleFormSuccess"
    />
  </div>
</template>

<script>
// import Pagination from '@/components/Pagination' // 使用 Element UI 的 el-pagination
import InfluenceFactorForm from './components/InfluenceFactorForm'
import { influenceFactorApi } from '@/api/financialSharing'
import {
  FACTOR_TYPE_NAME,
  DATA_TYPE_NAME,
  ENABLED_STATUS_NAME,
  DEFAULT_PAGE_CONFIG,
  DEFAULT_TENANT_CONFIG
} from '../consts'

export default {
  name: 'InfluenceFactorManagement',
  components: {
    // Pagination, // 使用 Element UI 的 el-pagination，不需要注册组件
    InfluenceFactorForm
  },
  data() {
    return {
      // 常量
      FACTOR_TYPE_NAME,
      DATA_TYPE_NAME,
      ENABLED_STATUS_NAME,
      
      // 查询参数
      queryParams: {
        ...DEFAULT_PAGE_CONFIG,
        ...DEFAULT_TENANT_CONFIG,
        factorCode: '',
        factorName: '',
        factorType: null,
        dataType: null,
        isEnabled: null
      },
      // 表格数据
      tableData: [],
      // 总记录数
      total: 0,
      // 加载状态
      loading: false,
      // 多选数据
      multipleSelection: [],
      // 表单相关
      formVisible: false,
      formType: 'add', // add, edit, view
      formData: {}
    }
  },
  created() {
    this.getList()
  },
  methods: {
    // 获取列表数据
    getList() {
      this.loading = true
      influenceFactorApi.getPage(this.queryParams)
        .then(response => {
          if (response.data.code === 1) {
            this.tableData = response.data.data.tlist || []
            this.total = response.data.data.totalRecord || 0
          } else {
            this.$message.error(response.data.msg || '查询失败')
          }
        })
        .catch(error => {
          console.error('查询影响因素列表失败:', error)
          this.$message.error('查询失败')
        })
        .finally(() => {
          this.loading = false
        })
    },

    // 查询
    handleQuery() {
      this.queryParams.pageNumber = 1
      this.getList()
    },

    // 重置查询
    resetQuery() {
      this.queryParams = {
        ...DEFAULT_PAGE_CONFIG,
        ...DEFAULT_TENANT_CONFIG,
        factorCode: '',
        factorName: '',
        factorType: null,
        dataType: null,
        isEnabled: null
      }
      this.getList()
    },

    // 新增
    handleAdd() {
      this.formType = 'add'
      this.formData = {}
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
      this.$confirm(`确定要删除影响因素"${row.factorName}"吗？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        influenceFactorApi.delete(row.factorId)
          .then(response => {
            if (response.data.code === 1) {
              this.$message.success('删除成功')
              this.getList()
            } else {
              this.$message.error(response.data.msg || '删除失败')
            }
          })
          .catch(error => {
            console.error('删除影响因素失败:', error)
            this.$message.error('删除失败')
          })
      })
    },

    // 切换启用状态
    handleToggleStatus(row) {
      const newStatus = row.isEnabled === 1 ? 0 : 1
      const statusText = newStatus === 1 ? '启用' : '禁用'

      this.$confirm(`确定要${statusText}影响因素"${row.factorName}"吗？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        influenceFactorApi.updateStatus(row.factorId, newStatus)
          .then(response => {
            if (response.data.code === 1) {
              this.$message.success(`${statusText}成功`)
              this.getList()
            } else {
              this.$message.error(response.data.msg || `${statusText}失败`)
            }
          })
          .catch(error => {
            console.error(`${statusText}影响因素失败:`, error)
            this.$message.error(`${statusText}失败`)
          })
      })
    },

    // 多选变化
    handleSelectionChange(selection) {
      this.multipleSelection = selection
    },

    // 批量启用
    handleBatchEnable() {
      if (!this.multipleSelection.length) {
        this.$message.warning('请选择要启用的数据')
        return
      }

      this.$confirm(`确定要启用选中的${this.multipleSelection.length}条数据吗？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const factorIds = this.multipleSelection.map(item => item.factorId)
        influenceFactorApi.batchUpdateStatus(factorIds, 1)
          .then(response => {
            if (response.data.code === 1) {
              this.$message.success('批量启用成功')
              this.getList()
            } else {
              this.$message.error(response.data.msg || '批量启用失败')
            }
          })
          .catch(error => {
            console.error('批量启用影响因素失败:', error)
            this.$message.error('批量启用失败')
          })
      })
    },

    // 批量禁用
    handleBatchDisable() {
      if (!this.multipleSelection.length) {
        this.$message.warning('请选择要禁用的数据')
        return
      }

      this.$confirm(`确定要禁用选中的${this.multipleSelection.length}条数据吗？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const factorIds = this.multipleSelection.map(item => item.factorId)
        influenceFactorApi.batchUpdateStatus(factorIds, 0)
          .then(response => {
            if (response.data.code === 1) {
              this.$message.success('批量禁用成功')
              this.getList()
            } else {
              this.$message.error(response.data.msg || '批量禁用失败')
            }
          })
          .catch(error => {
            console.error('批量禁用影响因素失败:', error)
            this.$message.error('批量禁用失败')
          })
      })
    },

    // 批量删除
    handleBatchDelete() {
      if (!this.multipleSelection.length) {
        this.$message.warning('请选择要删除的数据')
        return
      }

      this.$confirm(`确定要删除选中的${this.multipleSelection.length}条数据吗？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const factorIds = this.multipleSelection.map(item => item.factorId)
        influenceFactorApi.batchDelete(factorIds)
          .then(response => {
            if (response.data.code === 1) {
              this.$message.success('批量删除成功')
              this.getList()
            } else {
              this.$message.error(response.data.msg || '批量删除失败')
            }
          })
          .catch(error => {
            console.error('批量删除影响因素失败:', error)
            this.$message.error('批量删除失败')
          })
      })
    },

    // 表单成功回调
    handleFormSuccess() {
      this.formVisible = false
      this.getList()
    },

    /** 改变每一页请求数量 */
    handleSizeChange(val) {
      this.queryParams.pageSize = val
      this.getList()
    },

    /** 跳转页数 */
    handleCurrentChange(val) {
      this.queryParams.pageNumber = val
      this.getList()
    }
  }
}
</script>

<style scoped>
.filter-container {
  margin-bottom: 20px;
}
</style>
