<template>
  <div class="currency-rate-container">
    <!-- 查询条件 -->
    <div class="search-container">
      <el-form :model="queryForm" ref="queryForm" :inline="true" label-width="100px">
        <el-form-item label="币种编码" prop="currencyCode">
          <el-input
            v-model="queryForm.currencyCode"
            placeholder="请输入币种编码"
            clearable
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="币种名称" prop="currencyName">
          <el-input
            v-model="queryForm.currencyName"
            placeholder="请输入币种名称"
            clearable
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="汇率类型" prop="rateType">
          <el-select
            v-model="queryForm.rateType"
            placeholder="请选择汇率类型"
            clearable
            style="width: 150px"
          >
            <el-option
              v-for="item in RATE_TYPE_OPTIONS"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="汇率日期" prop="rateDateRange">
          <el-date-picker
            v-model="queryForm.rateDateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            format="yyyy-MM-dd"
            value-format="yyyy-MM-dd"
            style="width: 240px"
          />
        </el-form-item>
        <el-form-item label="是否本位币" prop="isBaseCurrency">
          <el-select
            v-model="queryForm.isBaseCurrency"
            placeholder="请选择"
            clearable
            style="width: 120px"
          >
            <el-option
              v-for="item in IS_BASE_CURRENCY_OPTIONS"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="状态" prop="isEnabled">
          <el-select
            v-model="queryForm.isEnabled"
            placeholder="请选择状态"
            clearable
            style="width: 120px"
          >
            <el-option
              v-for="item in ENABLED_STATUS_OPTIONS"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleQuery" :loading="loading">
            <i class="el-icon-search"></i> 查询
          </el-button>
          <el-button @click="handleReset">
            <i class="el-icon-refresh"></i> 重置
          </el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 操作按钮 -->
    <div class="toolbar">
      <el-button type="primary" @click="handleAdd">
        <i class="el-icon-plus"></i> 新增
      </el-button>
      <el-button
        type="success"
        :disabled="!hasSelection"
        @click="handleBatchEnable"
      >
        <i class="el-icon-check"></i> 批量启用
      </el-button>
      <el-button
        type="warning"
        :disabled="!hasSelection"
        @click="handleBatchDisable"
      >
        <i class="el-icon-close"></i> 批量禁用
      </el-button>
      <el-button
        type="danger"
        :disabled="!hasSelection"
        @click="handleBatchDelete"
      >
        <i class="el-icon-delete"></i> 批量删除
      </el-button>
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
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column prop="currencyCode" label="币种编码" width="120" />
        <el-table-column prop="currencyName" label="币种名称" width="150" />
        <el-table-column prop="rateTypeName" label="汇率类型" width="120" />
        <el-table-column prop="exchangeRate" label="汇率" width="120" align="right">
          <template slot-scope="scope">
            {{ scope.row.exchangeRate ? Number(scope.row.exchangeRate).toFixed(6) : '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="rateDate" label="汇率日期" width="120" />
        <el-table-column prop="isBaseCurrency" label="是否本位币" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="scope.row.isBaseCurrency === 1 ? 'success' : 'info'" size="mini">
              {{ IS_BASE_CURRENCY_NAME[scope.row.isBaseCurrency] || '-' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="isEnabled" label="状态" width="80" align="center">
          <template slot-scope="scope">
            <el-switch
              v-model="scope.row.isEnabled"
              :active-value="1"
              :inactive-value="0"
              @change="handleStatusChange(scope.row)"
            />
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="160" />
        <el-table-column label="操作" width="200" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" @click="handleView(scope.row)">查看</el-button>
            <el-button size="mini" type="primary" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button
              v-if="scope.row.isBaseCurrency !== 1"
              size="mini"
              type="success"
              @click="handleSetBaseCurrency(scope.row)"
            >
              设为本位币
            </el-button>
            <el-button
              v-if="scope.row.isBaseCurrency !== 1"
              size="mini"
              type="danger"
              @click="handleDelete(scope.row)"
            >
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 分页 -->
    <div class="pagination-container">
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="pagination.pageNumber"
        :page-sizes="[10, 15, 20, 50]"
        :page-size="pagination.pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="pagination.total"
      />
    </div>

    <!-- 表单弹窗 -->
    <CurrencyRateForm
      ref="currencyRateForm"
      @success="handleFormSuccess"
    />
  </div>
</template>

<script>
import {
  getCurrencyRatePage,
  deleteCurrencyRate,
  batchDeleteCurrencyRates,
  updateCurrencyRateStatus,
  batchUpdateCurrencyRateStatus,
  setBaseCurrency
} from '@/api/financialSharing/system'
import {
  RATE_TYPE_OPTIONS,
  RATE_TYPE_NAME,
  IS_BASE_CURRENCY_OPTIONS,
  IS_BASE_CURRENCY_NAME,
  ENABLED_STATUS_OPTIONS,
  ENABLED_STATUS_NAME,
  DEFAULT_PAGE_CONFIG,
  DEFAULT_TENANT_CONFIG
} from '../consts'
import CurrencyRateForm from '../components/CurrencyRateForm'

export default {
  name: 'CurrencyRate',
  components: {
    CurrencyRateForm
  },
  data() {
    return {
      // 常量
      RATE_TYPE_OPTIONS,
      RATE_TYPE_NAME,
      IS_BASE_CURRENCY_OPTIONS,
      IS_BASE_CURRENCY_NAME,
      ENABLED_STATUS_OPTIONS,
      ENABLED_STATUS_NAME,
      
      // 查询表单
      queryForm: {
        currencyCode: '',
        currencyName: '',
        rateType: null,
        rateDateRange: [],
        isBaseCurrency: null,
        isEnabled: null,
        ...DEFAULT_TENANT_CONFIG
      },
      
      // 表格数据
      tableData: [],
      loading: false,
      selectedRows: [],
      
      // 分页
      pagination: {
        ...DEFAULT_PAGE_CONFIG,
        total: 0
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
    // 加载数据
    async loadData() {
      this.loading = true
      try {
        const params = {
          ...this.queryForm,
          ...this.pagination
        }
        
        // 处理日期范围
        if (this.queryForm.rateDateRange && this.queryForm.rateDateRange.length === 2) {
          params.rateDateStart = this.queryForm.rateDateRange[0]
          params.rateDateEnd = this.queryForm.rateDateRange[1]
        }
        delete params.rateDateRange
        
        const response = await getCurrencyRatePage(params)
        if (response.code === 1) {
          this.tableData = response.data.records || []
          // 确保total是数字类型，避免Vue类型警告
          this.pagination.total = parseInt(response.data.total) || 0
        } else {
          this.$message.error(response.msg || '查询失败')
        }
      } catch (error) {
        console.error('加载数据失败:', error)
        this.$message.error('查询失败')
      } finally {
        this.loading = false
      }
    },
    
    // 查询
    handleQuery() {
      this.pagination.pageNumber = 1
      this.loadData()
    },
    
    // 重置
    handleReset() {
      this.$refs.queryForm.resetFields()
      this.queryForm = {
        currencyCode: '',
        currencyName: '',
        rateType: null,
        rateDateRange: [],
        isBaseCurrency: null,
        isEnabled: null,
        ...DEFAULT_TENANT_CONFIG
      }
      this.handleQuery()
    },
    
    // 新增
    handleAdd() {
      this.$refs.currencyRateForm.open('add')
    },
    
    // 查看
    handleView(row) {
      this.$refs.currencyRateForm.open('view', row.rateId)
    },
    
    // 编辑
    handleEdit(row) {
      this.$refs.currencyRateForm.open('edit', row.rateId)
    },
    
    // 删除
    handleDelete(row) {
      this.$confirm(`确定要删除币种汇率"${row.currencyName}"吗？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const response = await deleteCurrencyRate(row.rateId)
          if (response.code === 1) {
            this.$message.success('删除成功')
            this.loadData()
          } else {
            this.$message.error(response.msg || '删除失败')
          }
        } catch (error) {
          console.error('删除失败:', error)
          this.$message.error('删除失败')
        }
      })
    },
    
    // 批量删除
    handleBatchDelete() {
      if (!this.hasSelection) {
        this.$message.warning('请选择要删除的数据')
        return
      }
      
      this.$confirm(`确定要删除选中的 ${this.selectedRows.length} 条数据吗？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const ids = this.selectedRows.map(row => row.rateId)
          const response = await batchDeleteCurrencyRates(ids)
          if (response.code === 1) {
            this.$message.success('批量删除成功')
            this.loadData()
          } else {
            this.$message.error(response.msg || '批量删除失败')
          }
        } catch (error) {
          console.error('批量删除失败:', error)
          this.$message.error('批量删除失败')
        }
      })
    },
    
    // 状态变更
    async handleStatusChange(row) {
      try {
        const response = await updateCurrencyRateStatus(row.rateId, row.isEnabled)
        if (response.code === 1) {
          this.$message.success(`${row.isEnabled === 1 ? '启用' : '禁用'}成功`)
        } else {
          // 恢复原状态
          row.isEnabled = row.isEnabled === 1 ? 0 : 1
          this.$message.error(response.msg || '状态更新失败')
        }
      } catch (error) {
        // 恢复原状态
        row.isEnabled = row.isEnabled === 1 ? 0 : 1
        console.error('状态更新失败:', error)
        this.$message.error('状态更新失败')
      }
    },
    
    // 批量启用
    handleBatchEnable() {
      this.handleBatchStatusChange(1, '启用')
    },
    
    // 批量禁用
    handleBatchDisable() {
      this.handleBatchStatusChange(0, '禁用')
    },
    
    // 批量状态变更
    async handleBatchStatusChange(status, action) {
      if (!this.hasSelection) {
        this.$message.warning(`请选择要${action}的数据`)
        return
      }
      
      try {
        const ids = this.selectedRows.map(row => row.rateId)
        const response = await batchUpdateCurrencyRateStatus(ids, status)
        if (response.code === 1) {
          this.$message.success(`批量${action}成功`)
          this.loadData()
        } else {
          this.$message.error(response.msg || `批量${action}失败`)
        }
      } catch (error) {
        console.error(`批量${action}失败:`, error)
        this.$message.error(`批量${action}失败`)
      }
    },
    
    // 设置本位币
    handleSetBaseCurrency(row) {
      this.$confirm(`确定要将"${row.currencyName}"设置为本位币吗？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const response = await setBaseCurrency(row.rateId, {
            bookId: row.bookId,
            tenantId: row.tenantId
          })
          if (response.code === 1) {
            this.$message.success('设置本位币成功')
            this.loadData()
          } else {
            this.$message.error(response.msg || '设置本位币失败')
          }
        } catch (error) {
          console.error('设置本位币失败:', error)
          this.$message.error('设置本位币失败')
        }
      })
    },
    
    // 选择变更
    handleSelectionChange(selection) {
      this.selectedRows = selection
    },
    
    // 分页大小变更
    handleSizeChange(size) {
      this.pagination.pageSize = size
      this.pagination.pageNumber = 1
      this.loadData()
    },
    
    // 当前页变更
    handleCurrentChange(page) {
      this.pagination.pageNumber = page
      this.loadData()
    },
    
    // 表单成功回调
    handleFormSuccess() {
      this.loadData()
    }
  }
}
</script>

<style scoped>
.currency-rate-container {
  padding: 20px;
}

.search-container {
  background: #fff;
  padding: 20px;
  margin-bottom: 20px;
  border-radius: 4px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.toolbar {
  margin-bottom: 20px;
}

.table-container {
  background: #fff;
  padding: 20px;
  border-radius: 4px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.pagination-container {
  margin-top: 20px;
  text-align: right;
}
</style>
