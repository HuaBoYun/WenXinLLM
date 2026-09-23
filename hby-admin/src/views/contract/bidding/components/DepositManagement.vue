<template>
  <div class="deposit-management">
    <!-- 查询表单 -->
    <el-card shadow="never" class="search-card">
      <el-form
        ref="searchForm"
        :model="searchForm"
        :inline="true"
        label-width="80px"
        size="small"
      >
        <el-form-item label="保证金编号">
          <el-input
            v-model="searchForm.depositNo"
            placeholder="请输入保证金编号"
            clearable
            style="width: 180px"
          />
        </el-form-item>
        <el-form-item label="项目名称">
          <el-input
            v-model="searchForm.projectName"
            placeholder="请输入项目名称"
            clearable
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="保证金类型">
          <el-select
            v-model="searchForm.depositType"
            placeholder="请选择保证金类型"
            clearable
            style="width: 150px"
          >
            <el-option
              v-for="item in depositTypeOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="保证金状态">
          <el-select
            v-model="searchForm.depositStatus"
            placeholder="请选择保证金状态"
            clearable
            style="width: 120px"
          >
            <el-option
              v-for="item in depositStatusOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleSearch">
            查询
          </el-button>
          <el-button icon="el-icon-refresh" @click="handleReset">
            重置
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 操作按钮 -->
    <el-card shadow="never" class="operation-card">
      <el-button type="primary" icon="el-icon-plus" @click="handleAdd">
        新建保证金
      </el-button>
      <el-button
        type="success"
        icon="el-icon-money"
        @click="handleBatchPayment"
        :disabled="selectedRows.length === 0"
      >
        批量缴费
      </el-button>
      <el-button
        type="warning"
        icon="el-icon-refresh-left"
        @click="handleBatchRefund"
        :disabled="selectedRows.length === 0"
      >
        批量退费
      </el-button>
      <el-button
        type="info"
        icon="el-icon-download"
        @click="handleExport"
      >
        导出数据
      </el-button>
    </el-card>

    <!-- 统计卡片 -->
    <el-row :gutter="20" class="statistics-row">
      <el-col :span="6">
        <el-card class="statistics-card">
          <div class="statistics-item">
            <div class="statistics-value">{{ statistics.totalCount || 0 }}</div>
            <div class="statistics-label">保证金总数</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="statistics-card">
          <div class="statistics-item">
            <div class="statistics-value">{{ formatMoney(statistics.totalAmount) }}</div>
            <div class="statistics-label">保证金总额</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="statistics-card">
          <div class="statistics-item">
            <div class="statistics-value">{{ formatMoney(statistics.paidAmount) }}</div>
            <div class="statistics-label">已缴金额</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="statistics-card">
          <div class="statistics-item">
            <div class="statistics-value">{{ formatMoney(statistics.refundAmount) }}</div>
            <div class="statistics-label">已退金额</div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 数据表格 -->
    <el-card shadow="never" class="table-card">
      <el-table
        v-loading="tableLoading"
        :data="tableData"
        @selection-change="handleSelectionChange"
        stripe
        border
      >
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column
          prop="depositNo"
          label="保证金编号"
          width="140"
          align="center"
        />
        <el-table-column
          prop="projectName"
          label="项目名称"
          min-width="200"
          show-overflow-tooltip
        >
          <template #default="{ row }">
            <el-button type="text" @click="handleDetail(row)">
              {{ row.projectName }}
            </el-button>
          </template>
        </el-table-column>
        <el-table-column
          prop="depositType"
          label="保证金类型"
          width="120"
          align="center"
        >
          <template #default="{ row }">
            <el-tag :type="getDepositTypeTagType(row.depositType)">
              {{ getDepositTypeName(row.depositType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column
          prop="depositAmount"
          label="保证金金额"
          width="120"
          align="center"
        >
          <template #default="{ row }">
            {{ formatMoney(row.depositAmount) }}
          </template>
        </el-table-column>
        <el-table-column
          prop="paidAmount"
          label="已缴金额"
          width="120"
          align="center"
        >
          <template #default="{ row }">
            <span :class="getPaidAmountClass(row)">
              {{ formatMoney(row.paidAmount) }}
            </span>
          </template>
        </el-table-column>
        <el-table-column
          prop="depositStatus"
          label="状态"
          width="100"
          align="center"
        >
          <template #default="{ row }">
            <el-tag :type="getDepositStatusTagType(row.depositStatus)">
              {{ getDepositStatusName(row.depositStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column
          prop="paymentDate"
          label="缴费时间"
          width="160"
          align="center"
        >
          <template #default="{ row }">
            {{ formatDate(row.paymentDate) }}
          </template>
        </el-table-column>
        <el-table-column
          prop="managerName"
          label="负责人"
          width="100"
          align="center"
        />
        <el-table-column label="操作" width="200" align="center" fixed="right">
          <template #default="{ row }">
            <el-button
              type="text"
              size="small"
              icon="el-icon-view"
              @click="handleDetail(row)"
            >
              详情
            </el-button>
            <el-button
              type="text"
              size="small"
              icon="el-icon-edit"
              @click="handleEdit(row)"
            >
              编辑
            </el-button>
            <el-button
              v-if="row.depositStatus === 1"
              type="text"
              size="small"
              icon="el-icon-money"
              @click="handlePayment(row)"
            >
              缴费
            </el-button>
            <el-button
              v-if="row.depositStatus === 2"
              type="text"
              size="small"
              icon="el-icon-refresh-left"
              @click="handleRefund(row)"
            >
              退费
            </el-button>
            <el-popconfirm
              title="确定删除这条保证金记录吗？"
              @confirm="handleDelete(row)"
            >
              <el-button
                slot="reference"
                type="text"
                size="small"
                icon="el-icon-delete"
                style="color: #f56c6c"
              >
                删除
              </el-button>
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-container">
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pagination.current"
          :page-sizes="[10, 20, 50, 100]"
          :page-size="pagination.size"
          layout="total, sizes, prev, pager, next, jumper"
          :total="pagination.total"
        />
      </div>
    </el-card>

    <!-- 保证金表单对话框 -->
    <deposit-form
      ref="depositForm"
      @fetch-data="fetchData"
    />

    <!-- 保证金详情对话框 -->
    <deposit-detail
      ref="depositDetail"
    />

    <!-- 缴费对话框 -->
    <deposit-payment
      ref="depositPayment"
      @fetch-data="fetchData"
    />

    <!-- 退费对话框 -->
    <deposit-refund
      ref="depositRefund"
      @fetch-data="fetchData"
    />
  </div>
</template>

<script>
import {
  getDepositList,
  deleteDeposit,
  getDepositStatistics,
  exportDepositData
} from '@/api/contract/bidding'
import DepositForm from './DepositForm.vue'
import DepositDetail from './DepositDetail.vue'
import DepositPayment from './DepositPayment.vue'
import DepositRefund from './DepositRefund.vue'

export default {
  name: 'DepositManagement',
  components: {
    DepositForm,
    DepositDetail,
    DepositPayment,
    DepositRefund
  },
  data() {
    return {
      tableLoading: false,
      tableData: [],
      selectedRows: [],
      searchForm: {
        depositNo: '',
        projectName: '',
        depositType: null,
        depositStatus: null
      },
      pagination: {
        current: 1,
        size: 20,
        total: 0
      },
      statistics: {
        totalCount: 0,
        totalAmount: 0,
        paidAmount: 0,
        refundAmount: 0
      },
      depositTypeOptions: [
        { label: '投标保证金', value: 1 },
        { label: '履约保证金', value: 2 },
        { label: '质量保证金', value: 3 },
        { label: '农民工工资保证金', value: 4 }
      ],
      depositStatusOptions: [
        { label: '未缴费', value: 1 },
        { label: '已缴费', value: 2 },
        { label: '已退费', value: 3 },
        { label: '已没收', value: 4 }
      ]
    }
  },
  created() {
    this.fetchData()
    this.fetchStatistics()
  },
  methods: {
    async fetchData() {
      this.tableLoading = true
      try {
        const params = {
          ...this.searchForm,
          current: this.pagination.current,
          size: this.pagination.size
        }
        const response = await getDepositList(params)
        if (response.code === 200) {
          this.tableData = response.data.records || []
          this.pagination.total = response.data.total || 0
        } else {
          this.$message.error(response.message || '获取数据失败')
        }
      } catch (error) {
        this.$message.error('获取数据失败：' + error.message)
      } finally {
        this.tableLoading = false
      }
    },

    async fetchStatistics() {
      try {
        const response = await getDepositStatistics(this.searchForm)
        if (response.code === 200) {
          this.statistics = response.data || {}
        }
      } catch (error) {
        console.error('获取统计数据失败：', error)
      }
    },

    handleSearch() {
      this.pagination.current = 1
      this.fetchData()
      this.fetchStatistics()
    },

    handleReset() {
      this.$refs.searchForm.resetFields()
      this.searchForm = {
        depositNo: '',
        projectName: '',
        depositType: null,
        depositStatus: null
      }
      this.pagination.current = 1
      this.fetchData()
      this.fetchStatistics()
    },

    handleAdd() {
      this.$refs.depositForm.showEdit()
    },

    handleEdit(row) {
      this.$refs.depositForm.showEdit(row)
    },

    handleDetail(row) {
      this.$refs.depositDetail.showDetail(row)
    },

    handlePayment(row) {
      this.$refs.depositPayment.showPayment(row)
    },

    handleRefund(row) {
      this.$refs.depositRefund.showRefund(row)
    },

    async handleDelete(row) {
      try {
        const response = await deleteDeposit(row.id)
        if (response.code === 200) {
          this.$message.success('删除成功')
          this.fetchData()
          this.fetchStatistics()
        } else {
          this.$message.error(response.message || '删除失败')
        }
      } catch (error) {
        this.$message.error('删除失败：' + error.message)
      }
    },

    handleSelectionChange(selection) {
      this.selectedRows = selection
    },

    handleBatchPayment() {
      if (this.selectedRows.length === 0) {
        this.$message.warning('请选择要缴费的保证金')
        return
      }
      this.$refs.depositPayment.showBatchPayment(this.selectedRows)
    },

    handleBatchRefund() {
      if (this.selectedRows.length === 0) {
        this.$message.warning('请选择要退费的保证金')
        return
      }
      this.$refs.depositRefund.showBatchRefund(this.selectedRows)
    },

    async handleExport() {
      try {
        const response = await exportDepositData(this.searchForm)
        // 处理文件下载
        const blob = new Blob([response.data])
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = '保证金数据_' + new Date().getTime() + '.xlsx'
        link.click()
        window.URL.revokeObjectURL(url)
        this.$message.success('导出成功')
      } catch (error) {
        this.$message.error('导出失败：' + error.message)
      }
    },

    handleSizeChange(val) {
      this.pagination.size = val
      this.pagination.current = 1
      this.fetchData()
    },

    handleCurrentChange(val) {
      this.pagination.current = val
      this.fetchData()
    },

    getDepositTypeName(type) {
      const option = this.depositTypeOptions.find(item => item.value === type)
      return option ? option.label : '未知'
    },

    getDepositTypeTagType(type) {
      const typeMap = {
        1: 'primary',
        2: 'success',
        3: 'warning',
        4: 'info'
      }
      return typeMap[type] || 'info'
    },

    getDepositStatusName(status) {
      const option = this.depositStatusOptions.find(item => item.value === status)
      return option ? option.label : '未知'
    },

    getDepositStatusTagType(status) {
      const statusMap = {
        1: 'info',
        2: 'success',
        3: 'warning',
        4: 'danger'
      }
      return statusMap[status] || 'info'
    },

    getPaidAmountClass(row) {
      if (row.paidAmount >= row.depositAmount) {
        return 'paid-full'
      } else if (row.paidAmount > 0) {
        return 'paid-partial'
      }
      return 'paid-none'
    },

    formatMoney(amount) {
      if (!amount) return '¥0.00'
      return '¥' + Number(amount).toLocaleString('zh-CN', {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
      })
    },

    formatDate(date) {
      if (!date) return ''
      return new Date(date).toLocaleString()
    }
  }
}
</script>

<style scoped>
.deposit-management {
  padding: 20px;
}

.search-card,
.operation-card,
.table-card {
  margin-bottom: 20px;
}

.statistics-row {
  margin-bottom: 20px;
}

.statistics-card {
  text-align: center;
}

.statistics-item {
  padding: 20px;
}

.statistics-value {
  font-size: 24px;
  font-weight: bold;
  color: #409eff;
  margin-bottom: 8px;
}

.statistics-label {
  font-size: 14px;
  color: #606266;
}

.pagination-container {
  margin-top: 20px;
  text-align: right;
}

.paid-full {
  color: #67c23a;
  font-weight: bold;
}

.paid-partial {
  color: #e6a23c;
  font-weight: bold;
}

.paid-none {
  color: #f56c6c;
}
</style>
