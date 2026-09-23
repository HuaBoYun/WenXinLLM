<template>
  <div class="inventory-valuation-container">
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
        <el-form-item label="计价方法" prop="pricingMethod">
          <el-select
            v-model="searchForm.pricingMethod"
            placeholder="请选择计价方法"
            clearable
            style="width: 150px"
          >
            <el-option label="移动平均法" :value="1" />
            <el-option label="先进先出法" :value="2" />
            <el-option label="加权平均法" :value="3" />
            <el-option label="个别计价法" :value="4" />
          </el-select>
        </el-form-item>
        <el-form-item label="仓库" prop="warehouseId">
          <el-select
            v-model="searchForm.warehouseId"
            placeholder="请选择仓库"
            clearable
            style="width: 150px"
          >
            <el-option
              v-for="warehouse in warehouseList"
              :key="warehouse.warehouseId"
              :label="warehouse.warehouseName"
              :value="warehouse.warehouseId"
            />
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
      <el-button type="primary" @click="handleAdd">新增计价方法</el-button>
      <el-button type="success" @click="handleBatchUpdate" :disabled="!multipleSelection.length">
        批量更新
      </el-button>
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
        <el-table-column prop="warehouseName" label="仓库" width="120" />
        <el-table-column prop="pricingMethodName" label="计价方法" width="120">
          <template slot-scope="scope">
            <el-tag :type="getPricingMethodTagType(scope.row.pricingMethod)">
              {{ scope.row.pricingMethodName }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="unitCost" label="单位成本" width="120" align="right">
          <template slot-scope="scope">
            <span class="amount">{{ formatAmount(scope.row.unitCost) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="currentQuantity" label="当前库存" width="120" align="right" />
        <el-table-column prop="totalValue" label="库存总值" width="120" align="right">
          <template slot-scope="scope">
            <span class="amount">{{ formatAmount(scope.row.totalValue) }}</span>
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
            <el-button type="text" size="small" @click="handleCostCalculate(scope.row)">
              成本计算
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

    <!-- 计价方法编辑对话框 -->
    <ValuationEditDialog
      :visible.sync="editDialogVisible"
      :form-data="editFormData"
      :is-view-mode="isViewMode"
      @save="handleSave"
    />

    <!-- 成本计算对话框 -->
    <CostCalculateDialog
      :visible.sync="costCalculateDialogVisible"
      :inventory-data="selectedInventory"
      @calculate="handleCostCalculateConfirm"
    />
  </div>
</template>

<script>
import {
  getInventoryValuationPage,
  saveOrUpdateInventoryValuation,
  deleteInventoryValuation,
  batchUpdateInventoryValuation,
  batchDeleteInventoryValuation,
  calculateInventoryCost,
  exportInventoryValuation,
  getWarehouseList
} from '@/api/financialSharing/inventory'
import ValuationEditDialog from './components/ValuationEditDialog'
import CostCalculateDialog from './components/CostCalculateDialog'

export default {
  name: 'InventoryValuation',
  components: {
    ValuationEditDialog,
    CostCalculateDialog
  },
  data() {
    return {
      loading: false,
      tableData: [],
      multipleSelection: [],
      warehouseList: [],
      searchForm: {
        inventoryCode: '',
        inventoryName: '',
        pricingMethod: '',
        warehouseId: '',
        status: ''
      },
      pagination: {
        currentPage: 1,
        pageSize: 15,
        total: 0
      },
      editDialogVisible: false,
      costCalculateDialogVisible: false,
      editFormData: {},
      selectedInventory: {},
      isViewMode: false
    }
  },
  mounted() {
    this.loadData()
    this.loadWarehouseList()
  },
  methods: {
    async loadData() {
      this.loading = true
      try {
        const params = {
          pageNumber: this.pagination.currentPage,
          pageSize: this.pagination.pageSize,
          ...this.searchForm
        }
        const response = await getInventoryValuationPage(params)
        if (response.code === 1) {
          // 处理数据：兼容大写字段（Mock数据）和小驼峰字段（真实数据）
          this.tableData = (response.data.tlist || response.data.records || []).map(item => {
            // 判断是大写字段还是小驼峰字段
            const isUpperCase = item.INVENTORYCODE !== undefined

            return {
              inventoryCode: isUpperCase ? item.INVENTORYCODE : item.inventoryCode,
              inventoryName: isUpperCase ? item.INVENTORYNAME : item.inventoryName,
              inventoryId: isUpperCase ? item.INVENTORYID : item.inventoryId,
              // 存货分类：数据库表中没有此字段，暂时显示为 "-"
              categoryName: isUpperCase ? (item.CATEGORYNAME || '-') : (item.categoryName || '-'),
              warehouseId: isUpperCase ? item.WAREHOUSEID : item.warehouseId,
              warehouseName: isUpperCase ? (item.WAREHOUSENAME || '-') : (item.warehouseName || '-'),
              pricingMethod: isUpperCase ? item.PRICINGMETHOD : item.pricingMethod,
              pricingMethodName: isUpperCase ? item.PRICINGMETHODNAME : item.pricingMethodName,
              unitCost: isUpperCase ? item.UNITCOST : item.unitCost,
              currentQuantity: isUpperCase ? item.CURRENTQUANTITY : item.currentQuantity,
              // 库存总值：优先使用接口返回的值，否则计算
              totalValue: isUpperCase
                ? (item.TOTALVALUE || (item.UNITCOST || 0) * (item.CURRENTQUANTITY || 0))
                : (item.totalValue || (item.unitCost || 0) * (item.currentQuantity || 0)),
              status: isUpperCase ? item.STATUS : item.status,
              valuationId: isUpperCase ? item.VALUATIONID : item.valuationId,
              updateTime: isUpperCase ? (item.UPDATETIME || '-') : (item.updateTime || '-'),
              rowId: isUpperCase ? item.ROW_ID : item.rowId
            }
          })
          this.pagination.total = response.data.totalRecord || response.data.total || 0
        } else {
          this.$message.error(response.msg || '查询失败')
        }
      } catch (error) {
        this.$message.error('查询失败：' + error.message)
      } finally {
        this.loading = false
      }
    },

    async loadWarehouseList() {
      try {
        const response = await getWarehouseList()
        if (response.code === 1) {
          this.warehouseList = response.data || []
        }
      } catch (error) {
        console.error('加载仓库列表失败：', error)
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
      this.editFormData = {}
      this.isViewMode = false
      this.editDialogVisible = true
    },

    handleView(row) {
      this.editFormData = { ...row }
      this.isViewMode = true
      this.editDialogVisible = true
    },

    handleEdit(row) {
      this.editFormData = { ...row }
      this.isViewMode = false
      this.editDialogVisible = true
    },

    handleCostCalculate(row) {
      this.selectedInventory = { ...row }
      this.costCalculateDialogVisible = true
    },

    async handleSave(formData) {
      try {
        const response = await saveOrUpdateInventoryValuation(formData)
        if (response.code === 1) {
          this.$message.success('保存成功')
          this.editDialogVisible = false
          this.loadData()
        } else {
          this.$message.error(response.msg || '保存失败')
        }
      } catch (error) {
        this.$message.error('保存失败：' + error.message)
      }
    },

    async handleDelete(row) {
      try {
        await this.$confirm('确定要删除这条计价方法吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        const response = await deleteInventoryValuation(row.valuationId)
        if (response.code === 1) {
          this.$message.success('删除成功')
          this.loadData()
        } else {
          this.$message.error(response.msg || '删除失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('删除失败：' + error.message)
        }
      }
    },

    handleBatchUpdate() {
      if (this.multipleSelection.length === 0) {
        this.$message.warning('请先选择要更新的数据')
        return
      }

      this.$prompt('请输入新的计价方法(1-移动平均法,2-先进先出法,3-加权平均法,4-个别计价法)', '批量更新', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputPattern: /^[1-4]$/,
        inputErrorMessage: '请输入1-4之间的数字'
      }).then(async ({ value }) => {
        try {
          const valuationIds = this.multipleSelection.map(item => item.valuationId)
          const response = await batchUpdateInventoryValuation({
            valuationIds,
            pricingMethod: parseInt(value)
          })
          if (response.code === 1) {
            this.$message.success('批量更新成功')
            this.loadData()
          } else {
            this.$message.error(response.msg || '批量更新失败')
          }
        } catch (error) {
          this.$message.error('批量更新失败：' + error.message)
        }
      }).catch(() => {
        // 用户取消
      })
    },

    async handleBatchDelete() {
      if (this.multipleSelection.length === 0) {
        this.$message.warning('请先选择要删除的数据')
        return
      }

      try {
        await this.$confirm(`确定要删除选中的 ${this.multipleSelection.length} 条计价方法吗？`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })

        const valuationIds = this.multipleSelection.map(item => item.valuationId)
        const response = await batchDeleteInventoryValuation(valuationIds)
        if (response.code === 1) {
          this.$message.success('批量删除成功')
          this.loadData()
        } else {
          this.$message.error(response.msg || '批量删除失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('批量删除失败：' + error.message)
        }
      }
    },

    async handleExport() {
      try {
        this.$message.info('正在导出数据,请稍候...')
        const response = await exportInventoryValuation(this.searchForm)
        if (response.code === 1 && response.data) {
          // 如果返回的是文件路径,直接打开
          window.open(response.data)
          this.$message.success('导出成功')
        } else {
          this.$message.error(response.msg || '导出失败')
        }
      } catch (error) {
        this.$message.error('导出失败：' + error.message)
      }
    },

    async handleCostCalculateConfirm(result) {
      try {
        const response = await calculateInventoryCost(result.valuationId)
        if (response.code === 1) {
          this.$message.success('成本计算完成')
          this.costCalculateDialogVisible = false
          this.loadData()
        } else {
          this.$message.error(response.msg || '成本计算失败')
        }
      } catch (error) {
        this.$message.error('成本计算失败：' + error.message)
      }
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

    getPricingMethodTagType(method) {
      const typeMap = {
        1: 'primary',  // 移动平均法
        2: 'success',  // 先进先出法
        3: 'warning',  // 加权平均法
        4: 'info'      // 个别计价法
      }
      return typeMap[method] || 'default'
    },

    formatAmount(amount) {
      return amount ? '¥' + Number(amount).toLocaleString('zh-CN', { minimumFractionDigits: 2 }) : '¥0.00'
    }
  }
}
</script>

<style lang="scss" scoped>
.inventory-valuation-container {
  padding: 20px;
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
