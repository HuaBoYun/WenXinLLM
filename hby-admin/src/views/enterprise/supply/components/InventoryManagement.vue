<template>
  <div class="inventory-management" :style="themeVars">
    <!-- 搜索区域 -->
    <el-card shadow="never" class="search-card">
      <el-form :model="queryForm" ref="queryForm" :inline="true" label-width="80px">
        <el-form-item label="物料编码" prop="materialCode">
          <el-input v-model="queryForm.materialCode" placeholder="请输入物料编码" clearable />
        </el-form-item>
        <el-form-item label="物料名称" prop="materialName">
          <el-input v-model="queryForm.materialName" placeholder="请输入物料名称" clearable />
        </el-form-item>
        <el-form-item label="物料分类" prop="category">
          <el-select v-model="queryForm.category" placeholder="请选择物料分类" clearable>
            <el-option label="原材料" value="原材料" />
            <el-option label="半成品" value="半成品" />
            <el-option label="成品" value="成品" />
            <el-option label="办公用品" value="办公用品" />
          </el-select>
        </el-form-item>
        <el-form-item label="库存状态" prop="stockStatus">
          <el-select v-model="queryForm.stockStatus" placeholder="请选择库存状态" clearable>
            <el-option label="正常" value="正常" />
            <el-option label="偏低" value="偏低" />
            <el-option label="过高" value="过高" />
            <el-option label="缺货" value="缺货" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleSearch">搜索</el-button>
          <el-button icon="el-icon-refresh" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 操作按钮区域 -->
    <el-card shadow="never" class="action-card">
      <el-row :gutter="16">
        <el-col :span="12">
          <el-button type="primary" icon="el-icon-plus" @click="handleAdd">新增物料</el-button>
          <el-button type="success" icon="el-icon-upload2" @click="handleStockIn" :disabled="multipleSelection.length === 0">批量入库</el-button>
          <el-button type="warning" icon="el-icon-download" @click="handleExport">导出库存数据</el-button>
        </el-col>
        <el-col :span="12" class="text-right">
          <el-button type="info" icon="el-icon-pie-chart" @click="handleInventoryAnalysis">库存分析</el-button>
          <el-button type="primary" icon="el-icon-setting" @click="handleInventoryAlert">库存预警</el-button>
        </el-col>
      </el-row>
    </el-card>

    <!-- 数据表格 -->
    <el-card shadow="never" class="table-card">
      <el-table
        :data="inventoryList"
        v-loading="loading"
        @selection-change="handleSelectionChange"
        stripe
        border
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="materialCode" label="物料编码" width="120" />
        <el-table-column prop="materialName" label="物料名称" width="150" />
        <el-table-column prop="category" label="分类" width="100">
          <template slot-scope="scope">
            <el-tag :type="getCategoryColor(scope.row.category)">{{ scope.row.category }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="specification" label="规格型号" width="120" />
        <el-table-column prop="unit" label="单位" width="80" />
        <el-table-column prop="currentStock" label="当前库存" width="100">
          <template slot-scope="scope">
            <span :class="getStockClass(scope.row)">{{ scope.row.currentStock }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="minStock" label="最低库存" width="100" />
        <el-table-column prop="maxStock" label="最高库存" width="100" />
        <el-table-column prop="unitPrice" label="单价" width="100">
          <template slot-scope="scope">
            <span class="price">¥{{ scope.row.unitPrice.toLocaleString() }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="totalValue" label="库存价值" width="120">
          <template slot-scope="scope">
            <span class="total-value">¥{{ scope.row.totalValue.toLocaleString() }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="warehouse" label="仓库位置" width="120" />
        <el-table-column prop="lastUpdateDate" label="最后更新" width="120" />
        <el-table-column prop="stockStatus" label="库存状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="getStatusType(scope.row.stockStatus)">{{ scope.row.stockStatus }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="250" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" @click="handleView(scope.row)">查看</el-button>
            <el-button size="mini" type="success" @click="handleStockInOut(scope.row)">出入库</el-button>
            <el-button size="mini" type="warning" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button size="mini" type="danger" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
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
    </el-card>

    <!-- 对话框组件 -->
    <InventoryDetailDialog v-if="detailDialogVisible" :visible="detailDialogVisible" :data="selectedInventory" @close="detailDialogVisible = false" />
    <InventoryEditDialog v-if="editDialogVisible" :visible="editDialogVisible" :data="selectedInventory" :is-edit="!!selectedInventory" @close="editDialogVisible = false" @success="handleEditSuccess" />
    <InventoryStockDialog v-if="stockInOutDialogVisible" :visible="stockInOutDialogVisible" :data="selectedInventory" @close="stockInOutDialogVisible = false" @success="handleStockSuccess" />
    <InventoryAnalysisDialog v-if="analysisDialogVisible" :visible="analysisDialogVisible" @close="analysisDialogVisible = false" />
    <InventoryAlertDialog v-if="alertDialogVisible" :visible="alertDialogVisible" @close="alertDialogVisible = false" />
  </div>
</template>

<script>
import request from '@/utils/request'
import InventoryDetailDialog from './InventoryDetailDialog.vue'
import InventoryEditDialog from './InventoryEditDialog.vue'
import InventoryStockDialog from './InventoryStockDialog.vue'
import InventoryAnalysisDialog from './InventoryAnalysisDialog.vue'
import InventoryAlertDialog from './InventoryAlertDialog.vue'
import { investThemeMixin } from '@/views/stateAssets/themeMixin'

export default {
  name: 'InventoryManagement',
  mixins: [investThemeMixin],
  components: {
    InventoryDetailDialog,
    InventoryEditDialog,
    InventoryStockDialog,
    InventoryAnalysisDialog,
    InventoryAlertDialog
  },
  data() {
    return {
      loading: false,
      queryForm: {
        materialCode: '',
        materialName: '',
        category: '',
        stockStatus: ''
      },
      inventoryList: [],
      multipleSelection: [],
      pagination: {
        currentPage: 1,
        pageSize: 20,
        total: 0
      },
      detailDialogVisible: false,
      editDialogVisible: false,
      stockInOutDialogVisible: false,
      analysisDialogVisible: false,
      alertDialogVisible: false,
      selectedInventory: null
    }
  },
  mounted() {
    this.loadInventoryList()
  },
  methods: {
    // 加载库存列表
    async loadInventoryList() {
      this.loading = true
      try {
        const res = await request({
          url: '/monitor/v1/enterprise/supply/inventory/list',
          method: 'post',
          headers: { 'Content-Type': 'application/json;charset=UTF-8' },
          data: { ...this.queryForm, pageNumber: this.pagination.currentPage, pageSize: this.pagination.pageSize }
        })
        if (res && res.data) {
          this.inventoryList = res.data.tlist || []
          this.pagination.total = res.data.totalRecord || 0
        } else {
          this.inventoryList = []
          this.pagination.total = 0
        }
      } catch (error) {
        console.error('加载库存数据失败', error)
        this.inventoryList = []
        this.pagination.total = 0
      } finally {
        this.loading = false
      }
    },

    // 搜索
    handleSearch() {
      this.pagination.currentPage = 1
      this.loadInventoryList()
    },

    // 重置
    handleReset() {
      this.$refs.queryForm.resetFields()
      this.loadInventoryList()
    },

    // 新增物料
    handleAdd() {
      this.selectedInventory = null
      this.editDialogVisible = true
    },

    // 批量入库
    handleStockIn() {
      if (this.multipleSelection.length === 0) {
        this.$message.warning('请选择要入库的物料')
        return
      }
      this.selectedInventory = this.multipleSelection[0]
      this.stockInOutDialogVisible = true
    },

    // 导出库存数据
    async handleExport() {
      try {
        const res = await request({
          url: '/monitor/v1/enterprise/supply/export',
          method: 'get',
          params: { ...this.queryForm },
          responseType: 'blob'
        })
        const blob = new Blob([res.data], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' })
        const link = document.createElement('a')
        link.href = URL.createObjectURL(blob)
        link.download = '库存数据.xlsx'
        link.click()
        URL.revokeObjectURL(link.href)
      } catch (error) {
        console.error('导出库存数据失败', error)
        this.$message.error('导出失败')
      }
    },

    // 库存分析
    handleInventoryAnalysis() {
      this.analysisDialogVisible = true
    },

    // 库存预警
    handleInventoryAlert() {
      this.alertDialogVisible = true
    },

    // 查看详情
    handleView(row) {
      this.selectedInventory = row
      this.detailDialogVisible = true
    },

    // 出入库
    handleStockInOut(row) {
      this.selectedInventory = row
      this.stockInOutDialogVisible = true
    },

    // 编辑
    handleEdit(row) {
      this.selectedInventory = row
      this.editDialogVisible = true
    },

    // 表格选择变化
    handleSelectionChange(selection) {
      this.multipleSelection = selection
    },

    // 分页大小变化
    handleSizeChange(val) {
      this.pagination.pageSize = val
      this.loadInventoryList()
    },

    // 当前页变化
    handleCurrentChange(val) {
      this.pagination.currentPage = val
      this.loadInventoryList()
    },

    // 获取分类颜色
    getCategoryColor(category) {
      const categoryMap = {
        '原材料': 'primary',
        '半成品': 'warning',
        '成品': 'success',
        '办公用品': 'info'
      }
      return categoryMap[category] || 'info'
    },

    // 获取库存数量样式
    getStockClass(row) {
      if (row.currentStock === 0) return 'stock-empty'
      if (row.currentStock < row.minStock) return 'stock-low'
      if (row.currentStock > row.maxStock) return 'stock-high'
      return 'stock-normal'
    },

    // 获取状态类型
    getStatusType(status) {
      const statusMap = { '正常': 'success', '不足': 'warning', '超量': 'primary', '缺货': 'danger', '偏低': 'warning', '过高': 'primary' }
      return statusMap[status] || 'info'
    },
    handleEditSuccess() { this.editDialogVisible = false; this.loadInventoryList() },
    handleStockSuccess() { this.stockInOutDialogVisible = false; this.loadInventoryList() },
    handleDelete(row) {
      this.$confirm('确认删除该物料吗？', '提示', { confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning' }).then(async () => {
        try {
          const res = await request({ url: `/monitor/v1/enterprise/supply/inventory/${row.id}`, method: 'delete' })
          if (res && res.result === 200) { this.$message.success('删除成功'); this.loadInventoryList() }
          else this.$message.error(res.msg || '删除失败')
        } catch (e) { this.$message.error('删除失败') }
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.inventory-management {
  .search-card, .action-card, .table-card {
    margin-bottom: 16px;
  }

  .text-right {
    text-align: right;
  }

  .price {
    color: var(--ip-bright);
    font-weight: 500;
  }

  .total-value {
    color: #E6A23C;
    font-weight: bold;
    font-size: 14px;
  }

  .stock-empty {
    color: #F56C6C;
    font-weight: bold;
  }

  .stock-low {
    color: #E6A23C;
    font-weight: 500;
  }

  .stock-high {
    color: var(--ip-bright);
    font-weight: 500;
  }

  .stock-normal {
    color: #67C23A;
  }

  .pagination-container {
    margin-top: 20px;
    text-align: right;
  }
}
</style>
