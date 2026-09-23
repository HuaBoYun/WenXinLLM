<template>
  <div class="procurement-management" :style="themeVars">
    <!-- 搜索区域 -->
    <el-card shadow="never" class="search-card">
      <el-form :model="queryForm" ref="queryForm" :inline="true" label-width="80px">
        <el-form-item label="采购单号" prop="procurementNo">
          <el-input v-model="queryForm.procurementNo" placeholder="请输入采购单号" clearable />
        </el-form-item>
        <el-form-item label="供应商" prop="supplierName">
          <el-select v-model="queryForm.supplierName" placeholder="请选择供应商" clearable filterable>
            <el-option v-for="item in supplierOptions" :key="item.id" :label="item.name" :value="item.name" />
          </el-select>
        </el-form-item>
        <el-form-item label="采购类型" prop="procurementType">
          <el-select v-model="queryForm.procurementType" placeholder="请选择采购类型" clearable>
            <el-option label="设备采购" value="设备采购" />
            <el-option label="服务采购" value="服务采购" />
            <el-option label="原材料采购" value="原材料采购" />
            <el-option label="办公用品采购" value="办公用品采购" />
          </el-select>
        </el-form-item>
        <el-form-item label="采购状态" prop="status">
          <el-select v-model="queryForm.status" placeholder="请选择采购状态" clearable>
            <el-option label="待审批" value="待审批" />
            <el-option label="已审批" value="已审批" />
            <el-option label="采购中" value="采购中" />
            <el-option label="已完成" value="已完成" />
            <el-option label="已取消" value="已取消" />
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
          <el-button type="primary" icon="el-icon-plus" @click="handleAdd">新建采购</el-button>
          <el-button type="success" icon="el-icon-check" @click="handleBatchApprove" :disabled="multipleSelection.length === 0">批量审批</el-button>
          <el-button type="warning" icon="el-icon-download" @click="handleExport">导出采购数据</el-button>
        </el-col>
        <el-col :span="12" class="text-right">
          <el-button type="info" icon="el-icon-pie-chart" @click="handleProcurementAnalysis">采购分析</el-button>
          <el-button type="primary" icon="el-icon-setting" @click="handleProcurementPlan">采购计划</el-button>
        </el-col>
      </el-row>
    </el-card>

    <!-- 数据表格 -->
    <el-card shadow="never" class="table-card">
      <el-table
        :data="procurementList"
        v-loading="loading"
        @selection-change="handleSelectionChange"
        stripe
        border
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="procurementNo" label="采购单号" width="150" />
        <el-table-column prop="procurementName" label="采购名称" width="200" />
        <el-table-column prop="supplierName" label="供应商" width="150" />
        <el-table-column prop="procurementType" label="采购类型" width="120">
          <template slot-scope="scope">
            <el-tag :type="getTypeColor(scope.row.procurementType)">{{ scope.row.procurementType }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="quantity" label="采购数量" width="100" />
        <el-table-column prop="unitPrice" label="单价" width="120">
          <template slot-scope="scope">
            <span class="price">¥{{ scope.row.unitPrice.toLocaleString() }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="totalAmount" label="总金额" width="150">
          <template slot-scope="scope">
            <span class="total-amount">¥{{ scope.row.totalAmount.toLocaleString() }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="applicant" label="申请人" width="100" />
        <el-table-column prop="approver" label="审批人" width="100" />
        <el-table-column prop="applyDate" label="申请日期" width="120" />
        <el-table-column prop="expectedDate" label="期望交付" width="120" />
        <el-table-column prop="status" label="状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="getStatusType(scope.row.status)">{{ scope.row.status }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="250" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" @click="handleView(scope.row)">查看</el-button>
            <el-button size="mini" type="success" @click="handleApprove(scope.row)" v-if="scope.row.status === '待审批'">审批</el-button>
            <el-button size="mini" type="warning" @click="handleEdit(scope.row)" v-if="scope.row.status !== '已完成'">编辑</el-button>
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
    <ProcurementDetailDialog
      v-if="detailDialogVisible"
      :visible="detailDialogVisible"
      :data="selectedProcurement"
      @close="detailDialogVisible = false"
    />
    <ProcurementEditDialog
      v-if="editDialogVisible"
      :visible="editDialogVisible"
      :data="selectedProcurement"
      :is-edit="!!selectedProcurement"
      @close="editDialogVisible = false"
      @success="handleEditSuccess"
    />
    <ProcurementApprovalDialog
      v-if="approvalDialogVisible"
      :visible="approvalDialogVisible"
      :data="selectedProcurement"
      @close="approvalDialogVisible = false"
      @success="handleApprovalSuccess"
    />
    <ProcurementAnalysisDialog
      v-if="analysisDialogVisible"
      :visible="analysisDialogVisible"
      @close="analysisDialogVisible = false"
    />
    <ProcurementPlanDialog
      v-if="planDialogVisible"
      :visible="planDialogVisible"
      @close="planDialogVisible = false"
    />
  </div>
</template>

<script>
import request from '@/utils/request'
import ProcurementDetailDialog from './ProcurementDetailDialog.vue'
import ProcurementEditDialog from './ProcurementEditDialog.vue'
import ProcurementApprovalDialog from './ProcurementApprovalDialog.vue'
import ProcurementAnalysisDialog from './ProcurementAnalysisDialog.vue'
import ProcurementPlanDialog from './ProcurementPlanDialog.vue'
import { investThemeMixin } from '@/views/stateAssets/themeMixin'

export default {
  name: 'ProcurementManagement',
  mixins: [investThemeMixin],
  components: {
    ProcurementDetailDialog,
    ProcurementEditDialog,
    ProcurementApprovalDialog,
    ProcurementAnalysisDialog,
    ProcurementPlanDialog
  },
  data() {
    return {
      loading: false,
      queryForm: {
        procurementNo: '',
        supplierName: '',
        procurementType: '',
        status: ''
      },
      procurementList: [],
      multipleSelection: [],
      supplierOptions: [],
      pagination: {
        currentPage: 1,
        pageSize: 20,
        total: 0
      },
      detailDialogVisible: false,
      editDialogVisible: false,
      approvalDialogVisible: false,
      analysisDialogVisible: false,
      planDialogVisible: false,
      selectedProcurement: null
    }
  },
  mounted() {
    this.loadProcurementList()
    this.loadSupplierOptions()
  },
  methods: {
    // 加载供应商下拉选项
    async loadSupplierOptions() {
      try {
        const res = await request({ url: '/monitor/v1/enterprise/supply/options', method: 'get' })
        if (res && res.data) this.supplierOptions = res.data
      } catch (e) { console.error(e) }
    },

    // 加载采购列表
    async loadProcurementList() {
      this.loading = true
      try {
        const res = await request({
          url: '/monitor/v1/enterprise/supply/procurement/list',
          method: 'post',
          headers: { 'Content-Type': 'application/json;charset=UTF-8' },
          data: { ...this.queryForm, pageNumber: this.pagination.currentPage, pageSize: this.pagination.pageSize }
        })
        if (res && res.data) {
          this.procurementList = res.data.tlist || []
          this.pagination.total = res.data.totalRecord || 0
        } else {
          this.procurementList = []
          this.pagination.total = 0
        }
      } catch (error) {
        console.error('加载采购数据失败', error)
        this.procurementList = []
        this.pagination.total = 0
      } finally {
        this.loading = false
      }
    },

    // 搜索
    handleSearch() {
      this.pagination.currentPage = 1
      this.loadProcurementList()
    },

    // 重置
    handleReset() {
      this.$refs.queryForm.resetFields()
      this.loadProcurementList()
    },

    // 新建采购
    handleAdd() {
      this.selectedProcurement = null
      this.editDialogVisible = true
    },

    // 批量审批
    handleBatchApprove() {
      if (this.multipleSelection.length === 0) {
        this.$message.warning('请选择要审批的采购单')
        return
      }
      this.selectedProcurement = this.multipleSelection[0]
      this.approvalDialogVisible = true
    },

    // 导出采购数据
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
        link.download = '采购数据.xlsx'
        link.click()
        URL.revokeObjectURL(link.href)
      } catch (error) {
        console.error('导出采购数据失败', error)
        this.$message.error('导出失败')
      }
    },

    // 采购分析
    handleProcurementAnalysis() {
      this.analysisDialogVisible = true
    },

    // 采购计划
    handleProcurementPlan() {
      this.planDialogVisible = true
    },

    // 查看详情
    handleView(row) {
      this.selectedProcurement = row
      this.detailDialogVisible = true
    },

    // 审批
    handleApprove(row) {
      this.selectedProcurement = row
      this.approvalDialogVisible = true
    },

    // 编辑
    handleEdit(row) {
      this.selectedProcurement = row
      this.editDialogVisible = true
    },

    // 表格选择变化
    handleSelectionChange(selection) {
      this.multipleSelection = selection
    },

    // 分页大小变化
    handleSizeChange(val) {
      this.pagination.pageSize = val
      this.loadProcurementList()
    },

    // 当前页变化
    handleCurrentChange(val) {
      this.pagination.currentPage = val
      this.loadProcurementList()
    },

    // 获取采购类型颜色
    getTypeColor(type) {
      const typeMap = {
        '设备采购': 'primary',
        '服务采购': 'success',
        '原材料采购': 'warning',
        '办公用品采购': 'info'
      }
      return typeMap[type] || 'info'
    },

    // 获取状态类型
    getStatusType(status) {
      const statusMap = {
        '待审批': 'warning',
        '已审批': 'primary',
        '采购中': 'info',
        '已完成': 'success',
        '已取消': 'danger'
      }
      return statusMap[status] || 'info'
    },

    // 编辑成功回调
    handleEditSuccess() {
      this.editDialogVisible = false
      this.loadProcurementList()
    },

    // 删除采购
    handleDelete(row) {
      this.$confirm('确认删除该采购记录吗？', '提示', { confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning' }).then(async () => {
        try {
          const res = await request({ url: `/monitor/v1/enterprise/supply/procurement/${row.id}`, method: 'delete' })
          if (res && res.result === 200) { this.$message.success('删除成功'); this.loadProcurementList() }
          else this.$message.error(res.msg || '删除失败')
        } catch (e) { this.$message.error('删除失败') }
      })
    },

    // 审批成功回调
    handleApprovalSuccess() {
      this.approvalDialogVisible = false
      this.loadProcurementList()
    }
  }
}
</script>

<style lang="scss" scoped>
.procurement-management {
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

  .total-amount {
    color: #E6A23C;
    font-weight: bold;
    font-size: 14px;
  }

  .pagination-container {
    margin-top: 20px;
    text-align: right;
  }
}
</style>
