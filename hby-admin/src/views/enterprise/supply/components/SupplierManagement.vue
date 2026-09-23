<template>
  <div class="supplier-management">
    <!-- 搜索和操作区域 -->
    <div class="search-section">
      <el-form :model="queryForm" ref="queryForm" :inline="true" class="search-form">
        <el-form-item label="供应商名称" prop="supplierName">
          <el-input
            v-model="queryForm.supplierName"
            placeholder="请输入供应商名称"
            clearable
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="供应商类型" prop="supplierType">
          <el-select
            v-model="queryForm.supplierType"
            placeholder="请选择类型"
            clearable
            style="width: 150px"
          >
            <el-option label="原材料供应商" value="material" />
            <el-option label="设备供应商" value="equipment" />
            <el-option label="服务供应商" value="service" />
            <el-option label="技术供应商" value="technology" />
          </el-select>
        </el-form-item>
        <el-form-item label="合作状态" prop="status">
          <el-select
            v-model="queryForm.status"
            placeholder="请选择状态"
            clearable
            style="width: 150px"
          >
            <el-option label="合作中" value="active" />
            <el-option label="暂停合作" value="suspended" />
            <el-option label="终止合作" value="terminated" />
          </el-select>
        </el-form-item>
        <el-form-item label="评级" prop="rating">
          <el-select
            v-model="queryForm.rating"
            placeholder="请选择评级"
            clearable
            style="width: 120px"
          >
            <el-option label="A级" value="A" />
            <el-option label="B级" value="B" />
            <el-option label="C级" value="C" />
            <el-option label="D级" value="D" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">
            <i class="el-icon-search"></i>
            搜索
          </el-button>
          <el-button @click="handleReset">
            <i class="el-icon-refresh"></i>
            重置
          </el-button>
        </el-form-item>
      </el-form>

      <div class="action-buttons">
        <el-button type="primary" @click="handleAdd">
          <i class="el-icon-plus"></i>
          新增供应商
        </el-button>
        <el-button type="success" @click="handleBatchEvaluate">
          <i class="el-icon-star-on"></i>
          批量评估
        </el-button>
        <el-button type="warning" @click="handleExport">
          <i class="el-icon-download"></i>
          导出数据
        </el-button>
      </div>
    </div>

    <!-- 供应商列表 -->
    <div class="table-section">
      <el-table
        :data="supplierList"
        v-loading="loading"
        stripe
        border
        style="width: 100%"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="supplierCode" label="供应商编码" width="120" />
        <el-table-column prop="supplierName" label="供应商名称" width="200" />
        <el-table-column prop="supplierType" label="类型" width="120">
          <template #default="scope">
            <el-tag :type="getTypeTagType(scope.row.supplierType)" size="small">
              {{ getTypeText(scope.row.supplierType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="contactPerson" label="联系人" width="100" />
        <el-table-column prop="contactPhone" label="联系电话" width="130" />
        <el-table-column prop="address" label="地址" width="200" show-overflow-tooltip />
        <el-table-column prop="rating" label="评级" width="80">
          <template #default="scope">
            <el-tag :type="getRatingTagType(scope.row.rating)" size="small">
              {{ scope.row.rating }}级
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="cooperationYears" label="合作年限" width="100" />
        <el-table-column prop="totalAmount" label="合作金额(万)" width="120" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="scope">
            <el-tag :type="getStatusTagType(scope.row.status)" size="small">
              {{ getStatusText(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="scope">
            <el-button type="text" size="small" @click="handleView(scope.row)">
              查看
            </el-button>
            <el-button type="text" size="small" @click="handleEdit(scope.row)">
              编辑
            </el-button>
            <el-button type="text" size="small" @click="handleEvaluate(scope.row)">
              评估
            </el-button>
            <el-button
              type="text"
              size="small"
              style="color: #f56c6c"
              @click="handleDelete(scope.row)"
            >
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
          :current-page="pagination.currentPage"
          :page-sizes="[10, 20, 50, 100]"
          :page-size="pagination.pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="pagination.total"
        />
      </div>
    </div>

    <!-- 供应商详情对话框 -->
    <SupplierDetailDialog
      v-if="showDetailDialog"
      :visible="showDetailDialog"
      :supplier-data="selectedSupplier"
      @close="showDetailDialog = false"
    />

    <!-- 供应商编辑对话框 -->
    <SupplierEditDialog
      v-if="showEditDialog"
      :visible="showEditDialog"
      :supplier-data="selectedSupplier"
      :is-edit="isEdit"
      @close="showEditDialog = false"
      @success="handleEditSuccess"
    />

    <!-- 供应商评估对话框 -->
    <SupplierEvaluationDialog
      v-if="showEvaluationDialog"
      :visible="showEvaluationDialog"
      :supplier-data="selectedSupplier"
      @close="showEvaluationDialog = false"
      @success="handleEvaluationSuccess"
    />
  </div>
</template>

<script>
import request from '@/utils/request'
import SupplierDetailDialog from './SupplierDetailDialog.vue'
import SupplierEditDialog from './SupplierEditDialog.vue'
import SupplierEvaluationDialog from './SupplierEvaluationDialog.vue'

export default {
  name: 'SupplierManagement',
  components: {
    SupplierDetailDialog,
    SupplierEditDialog,
    SupplierEvaluationDialog
  },
  data() {
    return {
      loading: false,
      queryForm: {
        supplierName: '',
        supplierType: '',
        status: '',
        rating: ''
      },
      supplierList: [],
      selectedSuppliers: [],
      selectedSupplier: null,
      showDetailDialog: false,
      showEditDialog: false,
      showEvaluationDialog: false,
      isEdit: false,
      pagination: {
        currentPage: 1,
        pageSize: 20,
        total: 0
      }
    }
  },
  mounted() {
    this.loadSupplierList()
  },
  methods: {
    // 加载供应商列表
    async loadSupplierList() {
      this.loading = true
      try {
        const res = await request({
          url: '/monitor/v1/enterprise/supply/list',
          method: 'post',
          headers: { 'Content-Type': 'application/json;charset=UTF-8' },
          data: {
            ...this.queryForm,
            pageNumber: this.pagination.currentPage || 1,
            pageSize: this.pagination.pageSize || 15
          }
        })
        if (res && res.data) {
          this.supplierList = res.data.tlist || []
          this.pagination.total = res.data.totalRecord || 0
        } else {
          this.supplierList = []
          this.pagination.total = 0
        }
      } catch (error) {
        console.error('加载供应商列表失败', error)
        this.supplierList = []
        this.pagination.total = 0
      } finally {
        this.loading = false
      }
    },

    // 搜索
    handleSearch() {
      this.pagination.currentPage = 1
      this.loadSupplierList()
    },

    // 重置
    handleReset() {
      this.$refs.queryForm.resetFields()
      this.loadSupplierList()
    },

    // 新增供应商
    handleAdd() {
      this.selectedSupplier = null
      this.isEdit = false
      this.showEditDialog = true
    },

    // 查看供应商详情
    handleView(row) {
      this.selectedSupplier = row
      this.showDetailDialog = true
    },

    // 编辑供应商
    handleEdit(row) {
      this.selectedSupplier = row
      this.isEdit = true
      this.showEditDialog = true
    },

    // 评估供应商
    handleEvaluate(row) {
      this.selectedSupplier = row
      this.showEvaluationDialog = true
    },

    // 删除供应商
    handleDelete(row) {
      this.$confirm('确认删除该供应商吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const res = await request({
            url: `/monitor/v1/enterprise/supply/${row.id}`,
            method: 'delete'
          })
          if (res && res.result === 200) {
            this.$message.success('删除成功')
            this.loadSupplierList()
          } else {
            this.$message.error(res.msg || '删除失败')
          }
        } catch (error) {
          console.error('删除供应商失败', error)
          this.$message.error('删除失败')
        }
      })
    },

    // 批量评估
    handleBatchEvaluate() {
      if (this.selectedSuppliers.length === 0) {
        this.$message.warning('请选择要评估的供应商')
        return
      }
      // 取第一个选中的供应商进行评估
      this.selectedSupplier = this.selectedSuppliers[0]
      this.showEvaluationDialog = true
    },

    // 导出数据
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
        link.download = '供应商数据.xlsx'
        link.click()
        URL.revokeObjectURL(link.href)
        this.$message.success('导出成功')
      } catch (error) {
        console.error('导出供应商数据失败', error)
        this.$message.error('导出失败')
      }
    },

    // 选择变更
    handleSelectionChange(selection) {
      this.selectedSuppliers = selection
    },

    // 分页大小变更
    handleSizeChange(val) {
      this.pagination.pageSize = val
      this.loadSupplierList()
    },

    // 当前页变更
    handleCurrentChange(val) {
      this.pagination.currentPage = val
      this.loadSupplierList()
    },

    // 编辑成功回调
    handleEditSuccess() {
      this.showEditDialog = false
      this.loadSupplierList()
    },

    // 评估成功回调
    handleEvaluationSuccess() {
      this.showEvaluationDialog = false
      this.loadSupplierList()
    },

    // 获取类型标签类型
    getTypeTagType(type) {
      const typeMap = {
        material: 'primary',
        equipment: 'success',
        service: 'warning',
        technology: 'danger'
      }
      return typeMap[type] || 'info'
    },

    // 获取类型文本
    getTypeText(type) {
      const typeMap = {
        material: '原材料',
        equipment: '设备',
        service: '服务',
        technology: '技术'
      }
      return typeMap[type] || '未知'
    },

    // 获取评级标签类型
    getRatingTagType(rating) {
      const ratingMap = {
        A: 'success',
        B: 'primary',
        C: 'warning',
        D: 'danger'
      }
      return ratingMap[rating] || 'info'
    },

    // 获取状态标签类型
    getStatusTagType(status) {
      const statusMap = {
        active: 'success',
        suspended: 'warning',
        terminated: 'danger'
      }
      return statusMap[status] || 'info'
    },

    // 获取状态文本
    getStatusText(status) {
      const statusMap = {
        active: '合作中',
        suspended: '暂停合作',
        terminated: '终止合作'
      }
      return statusMap[status] || '未知'
    }
  }
}
</script>

<style lang="scss" scoped>
.supplier-management {
  .search-section {
    background: white;
    padding: 20px;
    border-radius: 8px;
    margin-bottom: 20px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);

    .search-form {
      margin-bottom: 20px;
    }

    .action-buttons {
      text-align: right;
    }
  }

  .table-section {
    background: white;
    padding: 20px;
    border-radius: 8px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);

    .pagination-section {
      margin-top: 20px;
      text-align: right;
    }
  }
}
</style>
