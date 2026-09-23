<template>
  <div class="contract-management">
    <!-- 搜索区域 -->
    <el-card shadow="never" class="search-card">
      <el-form :model="queryForm" ref="queryForm" :inline="true" label-width="80px">
        <el-form-item label="合同编号" prop="contractNo">
          <el-input v-model="queryForm.contractNo" placeholder="请输入合同编号" clearable />
        </el-form-item>
        <el-form-item label="合同名称" prop="contractName">
          <el-input v-model="queryForm.contractName" placeholder="请输入合同名称" clearable />
        </el-form-item>
        <el-form-item label="供应商" prop="supplierName">
          <el-select v-model="queryForm.supplierName" placeholder="请选择供应商" clearable filterable>
            <el-option v-for="item in supplierOptions" :key="item.id" :label="item.name" :value="item.name" />
          </el-select>
        </el-form-item>
        <el-form-item label="合同状态" prop="status">
          <el-select v-model="queryForm.status" placeholder="请选择合同状态" clearable>
            <el-option label="草稿" value="草稿" />
            <el-option label="待签署" value="待签署" />
            <el-option label="执行中" value="执行中" />
            <el-option label="已完成" value="已完成" />
            <el-option label="已终止" value="已终止" />
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
          <el-button type="primary" icon="el-icon-plus" @click="handleAdd">新建合同</el-button>
          <el-button type="success" icon="el-icon-edit" @click="handleBatchSign" :disabled="multipleSelection.length === 0">批量签署</el-button>
          <el-button type="warning" icon="el-icon-download" @click="handleExport">导出合同数据</el-button>
        </el-col>
        <el-col :span="12" class="text-right">
          <el-button type="info" icon="el-icon-pie-chart" @click="handleContractAnalysis">合同分析</el-button>
          <el-button type="primary" icon="el-icon-setting" @click="handleContractTemplate">合同模板</el-button>
        </el-col>
      </el-row>
    </el-card>

    <!-- 数据表格 -->
    <el-card shadow="never" class="table-card">
      <el-table
        :data="contractList"
        v-loading="loading"
        @selection-change="handleSelectionChange"
        stripe
        border
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="contractNo" label="合同编号" width="150" />
        <el-table-column prop="contractName" label="合同名称" width="200" />
        <el-table-column prop="supplierName" label="供应商" width="150" />
        <el-table-column prop="contractType" label="合同类型" width="120">
          <template slot-scope="scope">
            <el-tag :type="getTypeColor(scope.row.contractType)">{{ scope.row.contractType }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="contractAmount" label="合同金额" width="150">
          <template slot-scope="scope">
            <span class="contract-amount">¥{{ scope.row.contractAmount.toLocaleString() }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="signDate" label="签署日期" width="120" />
        <el-table-column prop="startDate" label="开始日期" width="120" />
        <el-table-column prop="endDate" label="结束日期" width="120" />
        <el-table-column prop="remainingDays" label="剩余天数" width="100">
          <template slot-scope="scope">
            <span :class="getRemainingDaysClass(scope.row.remainingDays)">{{ scope.row.remainingDays }}天</span>
          </template>
        </el-table-column>
        <el-table-column prop="executionProgress" label="执行进度" width="120">
          <template slot-scope="scope">
            <el-progress :percentage="scope.row.executionProgress" :stroke-width="8" />
          </template>
        </el-table-column>
        <el-table-column prop="responsiblePerson" label="负责人" width="100" />
        <el-table-column prop="status" label="状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="getStatusType(scope.row.status)">{{ scope.row.status }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="250" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" @click="handleView(scope.row)">查看</el-button>
            <el-button size="mini" type="success" @click="handleSign(scope.row)" v-if="scope.row.status === '待签署'">签署</el-button>
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
    <ContractDetailDialog v-if="detailDialogVisible" :visible="detailDialogVisible" :data="selectedContract" @close="detailDialogVisible = false" />
    <ContractEditDialog v-if="editDialogVisible" :visible="editDialogVisible" :data="selectedContract" :is-edit="!!selectedContract" @close="editDialogVisible = false" @success="handleEditSuccess" />
    <ContractSignDialog v-if="signDialogVisible" :visible="signDialogVisible" :data="selectedContract" @close="signDialogVisible = false" @success="handleSignSuccess" />
    <ContractAnalysisDialog v-if="analysisDialogVisible" :visible="analysisDialogVisible" @close="analysisDialogVisible = false" />
    <ContractTemplateDialog v-if="templateDialogVisible" :visible="templateDialogVisible" @close="templateDialogVisible = false" @use-template="handleUseTemplate" />
  </div>
</template>

<script>
import request from '@/utils/request'
import ContractDetailDialog from './ContractDetailDialog.vue'
import ContractEditDialog from './ContractEditDialog.vue'
import ContractSignDialog from './ContractSignDialog.vue'
import ContractAnalysisDialog from './ContractAnalysisDialog.vue'
import ContractTemplateDialog from './ContractTemplateDialog.vue'

export default {
  name: 'ContractManagement',
  components: {
    ContractDetailDialog,
    ContractEditDialog,
    ContractSignDialog,
    ContractAnalysisDialog,
    ContractTemplateDialog
  },
  data() {
    return {
      loading: false,
      queryForm: { contractNo: '', contractName: '', supplierName: '', status: '' },
      contractList: [],
      multipleSelection: [],
      supplierOptions: [],
      pagination: { currentPage: 1, pageSize: 20, total: 0 },
      detailDialogVisible: false,
      editDialogVisible: false,
      signDialogVisible: false,
      analysisDialogVisible: false,
      templateDialogVisible: false,
      selectedContract: null
    }
  },
  mounted() {
    this.loadContractList()
    this.loadSupplierOptions()
  },
  methods: {
    // 加载供应商下拉选项
    async loadSupplierOptions() {
      try { const res = await request({ url: '/monitor/v1/enterprise/supply/options', method: 'get' }); if (res && res.data) this.supplierOptions = res.data } catch (e) { console.error(e) }
    },

    // 加载合同列表
    async loadContractList() {
      this.loading = true
      try {
        const res = await request({
          url: '/monitor/v1/enterprise/supply/contract/list',
          method: 'post',
          headers: { 'Content-Type': 'application/json;charset=UTF-8' },
          data: { ...this.queryForm, pageNumber: this.pagination.currentPage, pageSize: this.pagination.pageSize }
        })
        if (res && res.data) {
          this.contractList = res.data.tlist || []
          this.pagination.total = res.data.totalRecord || 0
        } else {
          this.contractList = []
          this.pagination.total = 0
        }
      } catch (error) {
        console.error('加载合同数据失败', error)
        this.contractList = []
        this.pagination.total = 0
      } finally {
        this.loading = false
      }
    },

    // 搜索
    handleSearch() {
      this.pagination.currentPage = 1
      this.loadContractList()
    },

    // 重置
    handleReset() {
      this.$refs.queryForm.resetFields()
      this.loadContractList()
    },

    // 新建合同
    handleAdd() {
      this.selectedContract = null
      this.editDialogVisible = true
    },

    // 批量签署
    handleBatchSign() {
      if (this.multipleSelection.length === 0) {
        this.$message.warning('请选择要签署的合同')
        return
      }
      this.selectedContract = this.multipleSelection[0]
      this.signDialogVisible = true
    },

    // 导出合同数据
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
        link.download = '合同数据.xlsx'
        link.click()
        URL.revokeObjectURL(link.href)
      } catch (error) {
        console.error('导出合同数据失败', error)
        this.$message.error('导出失败')
      }
    },

    // 合同分析
    handleContractAnalysis() {
      this.analysisDialogVisible = true
    },

    // 合同模板
    handleContractTemplate() {
      this.templateDialogVisible = true
    },

    // 使用模板新建合同
    handleUseTemplate(template) {
      this.selectedContract = {
        contractType: template.contractType,
        contractAmount: template.contractAmount,
        responsiblePerson: template.responsiblePerson
      }
      this.editDialogVisible = true
    },

    // 查看详情
    handleView(row) {
      this.selectedContract = row
      this.detailDialogVisible = true
    },

    // 签署
    handleSign(row) {
      this.selectedContract = row
      this.signDialogVisible = true
    },

    // 编辑
    handleEdit(row) {
      this.selectedContract = row
      this.editDialogVisible = true
    },

    // 表格选择变化
    handleSelectionChange(selection) {
      this.multipleSelection = selection
    },

    // 分页大小变化
    handleSizeChange(val) {
      this.pagination.pageSize = val
      this.loadContractList()
    },

    // 当前页变化
    handleCurrentChange(val) {
      this.pagination.currentPage = val
      this.loadContractList()
    },

    // 获取合同类型颜色
    getTypeColor(type) {
      const typeMap = {
        '采购合同': 'primary',
        '服务合同': 'success',
        '租赁合同': 'warning',
        '技术合同': 'info'
      }
      return typeMap[type] || 'info'
    },

    // 获取剩余天数样式
    getRemainingDaysClass(days) {
      if (days <= 7) return 'remaining-urgent'
      if (days <= 30) return 'remaining-warning'
      return 'remaining-normal'
    },

    // 获取状态类型
    getStatusType(status) {
      const statusMap = { '草稿': 'info', '待签署': 'warning', '执行中': 'primary', '已完成': 'success', '已终止': 'danger' }
      return statusMap[status] || 'info'
    },
    handleEditSuccess() { this.editDialogVisible = false; this.loadContractList() },
    handleSignSuccess() { this.signDialogVisible = false; this.loadContractList() },
    handleDelete(row) {
      this.$confirm('确认删除该合同吗？', '提示', { confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning' }).then(async () => {
        try {
          const res = await request({ url: `/monitor/v1/enterprise/supply/contract/${row.id}`, method: 'delete' })
          if (res && res.result === 200) { this.$message.success('删除成功'); this.loadContractList() }
          else this.$message.error(res.msg || '删除失败')
        } catch (e) { this.$message.error('删除失败') }
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.contract-management {
  .search-card, .action-card, .table-card {
    margin-bottom: 16px;
  }

  .text-right {
    text-align: right;
  }

  .contract-amount {
    color: #E6A23C;
    font-weight: bold;
    font-size: 14px;
  }

  .remaining-urgent {
    color: #F56C6C;
    font-weight: bold;
  }

  .remaining-warning {
    color: #E6A23C;
    font-weight: 500;
  }

  .remaining-normal {
    color: #67C23A;
  }

  .pagination-container {
    margin-top: 20px;
    text-align: right;
  }
}
</style>
