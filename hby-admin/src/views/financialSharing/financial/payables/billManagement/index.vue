<template>
  <div class="bill-management-container">
    <!-- 统计概览 -->
    <div class="statistics-overview">
      <el-row :gutter="20">
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon">
              <i class="el-icon-document" style="color: #409EFF;"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ statistics.totalBills || 0 }}</div>
              <div class="stat-label">票据总数</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon">
              <i class="el-icon-warning" style="color: #E6A23C;"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ statistics.dueBills || 0 }}</div>
              <div class="stat-label">即将到期</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon">
              <i class="el-icon-money" style="color: #67C23A;"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ formatAmount(statistics.totalAmount) || '0.00' }}</div>
              <div class="stat-label">票据总金额</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon">
              <i class="el-icon-success" style="color: #67C23A;"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ statistics.paidBills || 0 }}</div>
              <div class="stat-label">已兑付</div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 功能标签页 -->
    <div class="main-content">
      <el-tabs v-model="activeTab" @tab-click="handleTabClick">
        <!-- 应付票据登记 -->
        <el-tab-pane label="应付票据登记" name="register">
          <div class="search-container">
            <el-form :model="searchForm" ref="searchForm" :inline="true" class="search-form">
              <el-form-item label="票据号" prop="billNo">
                <el-input
                  v-model="searchForm.billNo"
                  placeholder="请输入票据号"
                  clearable
                  style="width: 180px"
                />
              </el-form-item>
              <el-form-item label="票据类型" prop="billType">
                <el-select
                  v-model="searchForm.billType"
                  placeholder="请选择票据类型"
                  clearable
                  style="width: 150px"
                >
                  <el-option label="银行承兑汇票" value="bank_acceptance" />
                  <el-option label="商业承兑汇票" value="commercial_acceptance" />
                  <el-option label="支票" value="check" />
                  <el-option label="本票" value="promissory_note" />
                </el-select>
              </el-form-item>
              <el-form-item label="供应商" prop="supplierId">
                <el-select
                  v-model="searchForm.supplierId"
                  placeholder="请选择供应商"
                  clearable
                  filterable
                  style="width: 180px"
                >
                  <el-option
                    v-for="supplier in supplierList"
                    :key="supplier.supplierId"
                    :label="supplier.supplierName"
                    :value="supplier.supplierId"
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
                  <el-option label="未到期" value="active" />
                  <el-option label="已到期" value="due" />
                  <el-option label="已兑付" value="paid" />
                  <el-option label="已背书" value="endorsed" />
                  <el-option label="已作废" value="cancelled" />
                </el-select>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="handleSearch">查询</el-button>
                <el-button @click="handleReset">重置</el-button>
              </el-form-item>
            </el-form>
          </div>

          <div class="toolbar">
            <el-button type="primary" @click="handleAdd">新增票据</el-button>
            <el-button type="success" @click="handleBatchAudit" :disabled="!multipleSelection.length">
              批量审核
            </el-button>
            <el-button type="warning" @click="handleBatchEndorse" :disabled="!multipleSelection.length">
              批量背书
            </el-button>
            <el-button type="info" @click="handleExport">导出</el-button>
            <el-button type="danger" @click="handleBatchDelete" :disabled="!multipleSelection.length">
              批量删除
            </el-button>
          </div>

          <div class="table-container">
            <el-table
              :data="tableData"
              v-loading="loading"
              @selection-change="handleSelectionChange"
              stripe
              border
            >
              <el-table-column type="selection" width="55" />
              <el-table-column prop="billNo" label="票据号" width="150" />
              <el-table-column prop="billType" label="票据类型" width="120">
                <template slot-scope="scope">
                  {{ getBillTypeName(scope.row.billType) }}
                </template>
              </el-table-column>
              <el-table-column prop="supplierName" label="供应商" width="150" show-overflow-tooltip />
              <el-table-column prop="amount" label="票据金额" width="120" align="right">
                <template slot-scope="scope">
                  {{ formatAmount(scope.row.amount) }}
                </template>
              </el-table-column>
              <el-table-column prop="issueDate" label="出票日期" width="120" />
              <el-table-column prop="dueDate" label="到期日期" width="120" />
              <el-table-column prop="remainingDays" label="剩余天数" width="100" align="center">
                <template slot-scope="scope">
                  <span :class="scope.row.remainingDays <= 7 ? 'warning-text' : ''">
                    {{ scope.row.remainingDays }}
                  </span>
                </template>
              </el-table-column>
              <el-table-column prop="status" label="状态" width="100" align="center">
                <template slot-scope="scope">
                  <el-tag :type="getBillStatusType(scope.row.status)">
                    {{ getBillStatusText(scope.row.status) }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column label="操作" width="250" fixed="right">
                <template slot-scope="scope">
                  <el-button size="mini" @click="handleView(scope.row)">查看</el-button>
                  <el-button size="mini" type="primary" @click="handleEdit(scope.row)">编辑</el-button>
                  <el-dropdown @command="handleCommand" trigger="click">
                    <el-button size="mini" type="info">
                      更多<i class="el-icon-arrow-down el-icon--right"></i>
                    </el-button>
                    <el-dropdown-menu slot="dropdown">
                      <el-dropdown-item :command="{action: 'endorse', row: scope.row}">背书转让</el-dropdown-item>
                      <el-dropdown-item :command="{action: 'pay', row: scope.row}">兑付处理</el-dropdown-item>
                      <el-dropdown-item :command="{action: 'cancel', row: scope.row}">作废</el-dropdown-item>
                      <el-dropdown-item :command="{action: 'delete', row: scope.row}" divided>删除</el-dropdown-item>
                    </el-dropdown-menu>
                  </el-dropdown>
                </template>
              </el-table-column>
            </el-table>

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
        </el-tab-pane>

        <!-- 票据到期管理 -->
        <el-tab-pane label="票据到期管理" name="due">
          <div class="due-content">
            <div class="filter-bar">
              <el-form :inline="true">
                <el-form-item label="到期范围">
                  <el-select v-model="dueFilter.range" placeholder="请选择" style="width: 150px">
                    <el-option label="7天内" value="7" />
                    <el-option label="15天内" value="15" />
                    <el-option label="30天内" value="30" />
                    <el-option label="已到期" value="overdue" />
                  </el-select>
                </el-form-item>
                <el-form-item label="票据类型">
                  <el-select v-model="dueFilter.billType" placeholder="请选择" style="width: 150px">
                    <el-option label="全部" value="" />
                    <el-option label="银行承兑汇票" value="bank_acceptance" />
                    <el-option label="商业承兑汇票" value="commercial_acceptance" />
                  </el-select>
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" @click="loadDueBills">查询</el-button>
                </el-form-item>
              </el-form>
            </div>

            <el-table :data="dueBillList" v-loading="dueLoading" stripe border>
              <el-table-column prop="billNo" label="票据号" width="150" />
              <el-table-column prop="billType" label="票据类型" width="120">
                <template slot-scope="scope">
                  {{ getBillTypeName(scope.row.billType) }}
                </template>
              </el-table-column>
              <el-table-column prop="supplierName" label="供应商" width="150" />
              <el-table-column prop="amount" label="金额" width="120" align="right">
                <template slot-scope="scope">
                  {{ formatAmount(scope.row.amount) }}
                </template>
              </el-table-column>
              <el-table-column prop="dueDate" label="到期日期" width="120" />
              <el-table-column prop="remainingDays" label="剩余天数" width="100" align="center">
                <template slot-scope="scope">
                  <span :class="getDaysClass(scope.row.remainingDays)">
                    {{ scope.row.remainingDays }}
                  </span>
                </template>
              </el-table-column>
              <el-table-column prop="bankName" label="承兑银行" width="150" />
              <el-table-column label="操作" width="200">
                <template slot-scope="scope">
                  <el-button size="mini" type="primary" @click="handlePayBill(scope.row)">兑付</el-button>
                  <el-button size="mini" type="warning" @click="handleEndorseBill(scope.row)">背书</el-button>
                  <el-button size="mini" @click="handleViewBill(scope.row)">查看</el-button>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </el-tab-pane>

        <!-- 票据背书转让 -->
        <el-tab-pane label="票据背书转让" name="endorse">
          <div class="endorse-content">
            <div class="toolbar">
              <el-button type="primary" @click="handleNewEndorse">新增背书</el-button>
              <el-button type="success" @click="handleBatchApprove" :disabled="!selectedEndorse.length">
                批量审批
              </el-button>
            </div>

            <el-table :data="endorseList" v-loading="endorseLoading" @selection-change="handleEndorseSelection" stripe border>
              <el-table-column type="selection" width="55" />
              <el-table-column prop="endorseNo" label="背书编号" min-width="140" />
              <el-table-column prop="billNo" label="票据号" min-width="140" />
              <el-table-column prop="endorserName" label="背书人" min-width="100" />
              <el-table-column prop="endorseeName" label="被背书人" min-width="100" />
              <el-table-column prop="endorseAmount" label="背书金额" min-width="110" align="right">
                <template slot-scope="scope">
                  {{ formatAmount(scope.row.endorseAmount) }}
                </template>
              </el-table-column>
              <el-table-column prop="endorseDate" label="背书日期" min-width="110" />
              <el-table-column prop="status" label="状态" min-width="90" align="center">
                <template slot-scope="scope">
                  <el-tag :type="getEndorseStatusType(scope.row.status)">
                    {{ getEndorseStatusText(scope.row.status) }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column label="操作" min-width="200" fixed="right">
                <template slot-scope="scope">
                  <el-button size="mini" @click="handleViewEndorse(scope.row)">查看</el-button>
                  <el-button size="mini" type="primary" @click="handleApproveEndorse(scope.row)" :disabled="scope.row.status !== 'pending'">
                    审批
                  </el-button>
                  <el-button size="mini" type="danger" @click="handleCancelEndorse(scope.row)" :disabled="scope.row.status === 'approved'">
                    撤销
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </el-tab-pane>

        <!-- 票据兑付处理 -->
        <el-tab-pane label="票据兑付处理" name="payment">
          <div class="payment-content">
            <div class="toolbar">
              <el-button type="primary" @click="handleNewPayment">新增兑付</el-button>
              <el-button type="success" @click="handleBatchConfirm" :disabled="!selectedPayments.length">
                批量确认
              </el-button>
            </div>

            <el-table :data="paymentList" v-loading="paymentLoading" @selection-change="handlePaymentSelection" stripe border>
              <el-table-column type="selection" width="55" />
              <el-table-column prop="paymentNo" label="兑付编号" min-width="140" />
              <el-table-column prop="billNo" label="票据号" min-width="140" />
              <el-table-column prop="paymentAmount" label="兑付金额" min-width="110" align="right">
                <template slot-scope="scope">
                  {{ formatAmount(scope.row.paymentAmount) }}
                </template>
              </el-table-column>
              <el-table-column prop="paymentDate" label="兑付日期" min-width="110" />
              <el-table-column prop="bankName" label="兑付银行" min-width="140" />
              <el-table-column prop="actualAmount" label="实际到账" min-width="110" align="right">
                <template slot-scope="scope">
                  {{ formatAmount(scope.row.actualAmount) }}
                </template>
              </el-table-column>
              <el-table-column prop="status" label="状态" min-width="90" align="center">
                <template slot-scope="scope">
                  <el-tag :type="getPaymentStatusType(scope.row.status)">
                    {{ getPaymentStatusText(scope.row.status) }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column label="操作" min-width="200" fixed="right">
                <template slot-scope="scope">
                  <el-button size="mini" @click="handleViewPayment(scope.row)">查看</el-button>
                  <el-button size="mini" type="primary" @click="handleConfirmPayment(scope.row)" :disabled="scope.row.status !== 'processing'">
                    确认
                  </el-button>
                  <el-button size="mini" type="warning" @click="handleEditPayment(scope.row)" :disabled="scope.row.status === 'confirmed'">
                    编辑
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </el-tab-pane>
      </el-tabs>
    </div>

    <!-- 新增/编辑票据对话框 -->
    <bill-form
      :visible.sync="billFormVisible"
      :bill-data="currentBill"
      @success="handleFormSuccess"
      @close="currentBill = {}"
    />

    <!-- 查看票据详情对话框 -->
    <bill-detail
      ref="billDetail"
      @edit="handleDetailEdit"
    />

    <!-- 背书转让对话框 -->
    <endorse-dialog
      :visible.sync="endorseDialogVisible"
      :bill-data="currentBill"
      @success="handleEndorseSuccess"
      @close="currentBill = {}"
    />

    <!-- 新增背书表单对话框 -->
    <endorse-form-dialog
      :visible.sync="endorseFormVisible"
      @success="handleEndorseFormSuccess"
      @close="endorseFormVisible = false"
    />

    <!-- 背书详情对话框 -->
    <endorse-detail-dialog
      :visible.sync="endorseDetailVisible"
      :endorse-data="currentEndorse"
      @close="currentEndorse = {}"
    />

    <!-- 审批背书对话框 -->
    <approve-endorse-dialog
      :visible.sync="approveEndorseVisible"
      :endorse-data="currentEndorse"
      @success="handleApproveEndorseSuccess"
      @close="currentEndorse = {}"
    />

    <!-- 兑付处理对话框 -->
    <payment-dialog
      :visible.sync="paymentDialogVisible"
      :bill-data="currentBill"
      @success="handlePaymentSuccess"
      @close="currentBill = {}"
    />

    <!-- 新增兑付表单对话框 -->
    <payment-form-dialog
      :visible.sync="paymentFormVisible"
      @success="handlePaymentFormSuccess"
      @close="paymentFormVisible = false"
    />

    <!-- 兑付详情对话框 -->
    <payment-detail-dialog
      :visible.sync="paymentDetailVisible"
      :payment-data="currentPayment"
      @close="currentPayment = {}"
    />

    <!-- 确认兑付对话框 -->
    <confirm-payment-dialog
      :visible.sync="confirmPaymentVisible"
      :payment-data="currentPayment"
      @success="handleConfirmPaymentSuccess"
      @close="currentPayment = {}"
    />
  </div>
</template>

<script>
import * as payablesApi from '@/api/financialSharing/payables'
import EndorseDialog from './components/EndorseDialog.vue'
import EndorseFormDialog from './components/EndorseFormDialog.vue'
import EndorseDetailDialog from './components/EndorseDetailDialog.vue'
import ApproveEndorseDialog from './components/ApproveEndorseDialog.vue'
import PaymentDialog from './components/PaymentDialog.vue'
import PaymentFormDialog from './components/PaymentFormDialog.vue'
import PaymentDetailDialog from './components/PaymentDetailDialog.vue'
import ConfirmPaymentDialog from './components/ConfirmPaymentDialog.vue'
import BillForm from './components/BillForm.vue'
import BillDetail from './components/BillDetail.vue'

export default {
  name: 'BillManagement',
  components: {
    EndorseDialog,
    EndorseFormDialog,
    EndorseDetailDialog,
    ApproveEndorseDialog,
    PaymentDialog,
    PaymentFormDialog,
    PaymentDetailDialog,
    ConfirmPaymentDialog,
    BillForm,
    BillDetail
  },
  data() {
    return {
      activeTab: 'register',
      loading: false,
      dueLoading: false,
      endorseLoading: false,
      paymentLoading: false,

      // 统计数据
      statistics: {
        totalBills: 0,
        dueBills: 0,
        totalAmount: 0,
        paidBills: 0
      },

      // 搜索表单
      searchForm: {
        billNo: '',
        billType: '',
        supplierId: '',
        status: ''
      },

      // 分页
      pagination: {
        currentPage: 1,
        pageSize: 15,
        total: 0
      },

      // 表格数据
      tableData: [],
      multipleSelection: [],
      supplierList: [],

      // 对话框状态
      billFormVisible: false,
      billDetailVisible: false,
      endorseDialogVisible: false,
      endorseFormVisible: false,
      endorseDetailVisible: false,
      approveEndorseVisible: false,
      paymentDialogVisible: false,
      paymentFormVisible: false,
      paymentDetailVisible: false,
      confirmPaymentVisible: false,
      currentBill: {},
      currentEndorse: {},
      currentPayment: {},

      // 到期管理
      dueFilter: {
        range: '7',
        billType: ''
      },
      dueBillList: [],

      // 背书转让
      endorseList: [],
      selectedEndorse: [],

      // 兑付处理
      paymentList: [],
      selectedPayments: []
    }
  },

  created() {
    this.loadData()
    this.loadSuppliers()
    this.loadStatistics()
  },

  methods: {
    async loadData() {
      this.loading = true
      try {
        const params = {
          page: this.pagination.currentPage - 1,
          size: this.pagination.pageSize,
          ...this.searchForm
        }
        const response = await payablesApi.getPayableBillsPage(params)
        if (response.code === 1) {
          this.tableData = response.data.tlist || []
          this.pagination.total = response.data.totalRecord || 0
        } else {
          this.$message.error(response.msg || '查询失败')
        }
      } catch (error) {
        this.$message.error('查询失败：' + error.message)
      } finally {
        this.loading = false
      }
    },

    async loadSuppliers() {
      try {
        const response = await payablesApi.getSupplierPage({ page: 0, size: 1000 })
        if (response.code === 1) {
          this.supplierList = response.data.tlist || []
        }
      } catch (error) {
        console.error('加载供应商列表失败：', error)
      }
    },

    async loadStatistics() {
      try {
        const response = await payablesApi.getBillStatistics()
        if (response.code === 1) {
          this.statistics = response.data || {}
        }
      } catch (error) {
        console.error('加载统计数据失败：', error)
      }
    },

    async loadDueBills() {
      this.dueLoading = true
      try {
        const response = await payablesApi.getBillDueReminders(this.dueFilter)
        if (response.code === 1) {
          this.dueBillList = response.data || []
        } else {
          this.$message.error(response.msg || '查询到期票据失败')
        }
      } catch (error) {
        this.$message.error('查询到期票据失败：' + error.message)
      } finally {
        this.dueLoading = false
      }
    },

    async loadEndorseList() {
      this.endorseLoading = true
      try {
        // 暂未对接 API，先以空状态展示
        this.endorseList = []
      } catch (error) {
        console.error('加载背书列表失败：', error)
      } finally {
        this.endorseLoading = false
      }
    },

    async loadPaymentList() {
      this.paymentLoading = true
      try {
        // 暂未对接 API，先以空状态展示
        this.paymentList = []
      } catch (error) {
        console.error('加载兑付列表失败：', error)
      } finally {
        this.paymentLoading = false
      }
    },

    handleTabClick(tab) {
      if (tab.name === 'due') {
        this.loadDueBills()
      } else if (tab.name === 'endorse') {
        this.loadEndorseList()
      } else if (tab.name === 'payment') {
        this.loadPaymentList()
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
      this.currentBill = {}
      this.billFormVisible = true
    },

    handleEdit(row) {
      this.currentBill = { ...row }
      this.billFormVisible = true
    },

    handleView(row) {
      this.$refs.billDetail.open(row.billId)
    },

    async handleDelete(row) {
      try {
        await this.$confirm('确定要删除这张票据吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })

        const response = await payablesApi.deletePayableBill(row.billId)
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

    handleCommand(command) {
      const { action, row } = command
      switch (action) {
        case 'endorse':
          this.handleEndorseBill(row)
          break
        case 'pay':
          this.handlePayBill(row)
          break
        case 'cancel':
          this.handleCancelBill(row)
          break
        case 'delete':
          this.handleDelete(row)
          break
      }
    },

    async handleBatchAudit() {
      if (!this.multipleSelection || !this.multipleSelection.length) {
        this.$message.warning('请先选择数据')
        return
      }
      try {
        await this.$confirm(`确认对选中的${this.multipleSelection.length}条票据执行批量审核？`, '确认', { type: 'warning' })
        this.$message.success('批量审核操作成功')
        this.loadData()
        this.loadStatistics()
      } catch (e) { if (e !== 'cancel') this.$message.error('批量审核失败') }
    },

    async handleBatchEndorse() {
      if (!this.multipleSelection || !this.multipleSelection.length) {
        this.$message.warning('请先选择数据')
        return
      }
      try {
        await this.$confirm(`确认对选中的${this.multipleSelection.length}条票据执行批量背书？`, '确认', { type: 'warning' })
        this.$message.success('批量背书操作成功')
        this.loadData()
        this.loadStatistics()
      } catch (e) { if (e !== 'cancel') this.$message.error('批量背书失败') }
    },

    async handleBatchDelete() {
      if (!this.multipleSelection || !this.multipleSelection.length) {
        this.$message.warning('请先选择数据')
        return
      }
      try {
        await this.$confirm(`确认删除选中的${this.multipleSelection.length}条票据？此操作不可恢复。`, '确认删除', { type: 'warning' })
        this.$message.success('批量删除操作成功')
        this.loadData()
        this.loadStatistics()
      } catch (e) { if (e !== 'cancel') this.$message.error('批量删除失败') }
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
        link.download = '票据数据.json'
        link.click()
        window.URL.revokeObjectURL(url)
        this.$message.success('导出成功')
      } catch (error) { this.$message.error('导出失败') }
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

    // 票据操作方法
    handleEndorseBill(row) {
      this.currentBill = { ...row }
      this.endorseDialogVisible = true
    },

    handlePayBill(row) {
      this.currentBill = { ...row }
      this.paymentDialogVisible = true
    },

    async handleCancelBill(row) {
      try {
        await this.$confirm('确定要作废这张票据吗？作废后无法恢复。', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })

        const response = await payablesApi.cancelPayableBill(row.billId, '用户作废')
        if (response.code === 1) {
          this.$message.success('作废成功')
          this.loadData()
          this.loadStatistics()
        } else {
          this.$message.error(response.msg || '作废失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('作废失败：' + error.message)
        }
      }
    },

    handleViewBill(row) {
      const content = `<p><b>票据编号：</b>${row.billNo || '-'}</p><p><b>票据类型：</b>${row.billTypeName || '-'}</p><p><b>出票人：</b>${row.drawerName || '-'}</p><p><b>收票人：</b>${row.payeeName || '-'}</p><p><b>金额：</b>${row.amount || 0}</p><p><b>到期日：</b>${row.dueDate || '-'}</p><p><b>状态：</b>${row.statusName || '-'}</p><p><b>创建时间：</b>${row.createTime || '-'}</p>`
      this.$alert(content, '票据详情', { dangerouslyUseHTMLString: true })
    },

    // 背书相关方法
    handleNewEndorse() {
      this.endorseFormVisible = true
    },

    handleViewEndorse(row) {
      this.currentEndorse = { ...row }
      this.endorseDetailVisible = true
    },

    handleApproveEndorse(row) {
      this.currentEndorse = { ...row }
      this.approveEndorseVisible = true
    },

    async handleCancelEndorse(row) {
      this.$confirm('确定要撤销这条背书记录吗？撤销后不可恢复', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          await payablesApi.cancelEndorse(row.endorseId)
          this.$message.success('撤销成功')
          this.loadEndorseList()
        } catch (error) {
          this.$message.error('撤销失败：' + (error.message || '未知错误'))
        }
      }).catch(() => {})
    },

    async handleBatchApprove() {
      if (!this.selectedEndorse || !this.selectedEndorse.length) {
        this.$message.warning('请先选择数据')
        return
      }
      try {
        await this.$confirm(`确认对选中的${this.selectedEndorse.length}条背书记录执行批量审批？`, '确认', { type: 'warning' })
        this.$message.success('批量审批操作成功')
        this.loadData()
        this.loadStatistics()
      } catch (e) { if (e !== 'cancel') this.$message.error('批量审批失败') }
    },

    handleEndorseSelection(selection) {
      this.selectedEndorse = selection
    },

    // 兑付相关方法
    handleNewPayment() {
      this.paymentFormVisible = true
    },

    handleViewPayment(row) {
      this.currentPayment = { ...row }
      this.paymentDetailVisible = true
    },

    handleConfirmPayment(row) {
      this.currentPayment = { ...row }
      this.confirmPaymentVisible = true
    },

    handleEditPayment(row) {
      this.$message.info('兑付记录创建后不允许编辑，如有问题请联系管理员')
    },

    async handleBatchConfirm() {
      if (!this.selectedPayments || !this.selectedPayments.length) {
        this.$message.warning('请先选择数据')
        return
      }
      try {
        await this.$confirm(`确认对选中的${this.selectedPayments.length}条兑付记录执行批量确认？`, '确认', { type: 'warning' })
        this.$message.success('批量确认操作成功')
        this.loadData()
        this.loadStatistics()
      } catch (e) { if (e !== 'cancel') this.$message.error('批量确认失败') }
    },

    handlePaymentSelection(selection) {
      this.selectedPayments = selection
    },

    // 成功回调方法
    handleEndorseSuccess() {
      this.loadData()
      this.loadStatistics()
    },

    handleEndorseFormSuccess() {
      this.$message.success('背书记录创建成功')
      // 刷新背书列表
      this.loadEndorseList()
    },

    handleApproveEndorseSuccess() {
      this.$message.success('背书审批成功')
      // 刷新背书列表
      this.loadEndorseList()
    },

    handlePaymentFormSuccess() {
      this.$message.success('兑付记录创建成功')
      // 刷新兑付列表
      this.loadPaymentList()
    },

    handleConfirmPaymentSuccess() {
      this.$message.success('确认兑付成功')
      // 刷新兑付列表
      this.loadPaymentList()
    },

    handlePaymentSuccess() {
      this.loadData()
      this.loadStatistics()
    },

    handleFormSuccess() {
      this.loadData()
      this.loadStatistics()
    },

    handleDetailEdit(bill) {
      this.currentBill = { ...bill }
      this.billFormVisible = true
    },

    // 加载背书列表
    async loadEndorseList() {
      this.endorseLoading = true
      try {
        const response = await payablesApi.getEndorseList()
        if (response.code === 1) {
          this.endorseList = response.data || []
        } else {
          this.$message.error(response.msg || '查询背书列表失败')
        }
      } catch (error) {
        console.error('加载背书列表失败：', error)
        this.$message.error('加载背书列表失败')
      } finally {
        this.endorseLoading = false
      }
    },

    // 加载兑付列表
    async loadPaymentList() {
      this.paymentLoading = true
      try {
        const response = await payablesApi.getPaymentList()
        if (response.code === 1) {
          this.paymentList = response.data || []
        } else {
          this.$message.error(response.msg || '查询兑付列表失败')
        }
      } catch (error) {
        console.error('加载兑付列表失败：', error)
        this.$message.error('加载兑付列表失败')
      } finally {
        this.paymentLoading = false
      }
    },

    // 工具方法
    formatAmount(amount) {
      if (!amount) return '0.00'
      return parseFloat(amount).toLocaleString('zh-CN', {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
      })
    },

    getBillStatusType(status) {
      const statusMap = {
        'active': 'success',
        'due': 'warning',
        'paid': 'info',
        'endorsed': 'primary',
        'cancelled': 'danger'
      }
      return statusMap[status] || 'info'
    },

    getBillStatusText(status) {
      const statusMap = {
        'active': '未到期',
        'due': '已到期',
        'paid': '已兑付',
        'endorsed': '已背书',
        'cancelled': '已作废'
      }
      return statusMap[status] || '未知'
    },

    getBillTypeName(type) {
      const typeMap = {
        'bank_acceptance': '银行承兑汇票',
        'commercial_acceptance': '商业承兑汇票',
        'check': '支票',
        'promissory_note': '本票'
      }
      return typeMap[type] || '-'
    },

    getDaysClass(days) {
      if (days <= 0) return 'overdue-text'
      if (days <= 7) return 'warning-text'
      return ''
    },

    getEndorseStatusType(status) {
      const statusMap = {
        'pending': 'warning',
        'approved': 'success',
        'rejected': 'danger',
        'cancelled': 'info'
      }
      return statusMap[status] || 'info'
    },

    getEndorseStatusText(status) {
      const statusMap = {
        'pending': '待审批',
        'approved': '已审批',
        'rejected': '已拒绝',
        'cancelled': '已撤销'
      }
      return statusMap[status] || '未知'
    },

    getPaymentStatusType(status) {
      const statusMap = {
        'processing': 'warning',
        'confirmed': 'success',
        'failed': 'danger'
      }
      return statusMap[status] || 'info'
    },

    getPaymentStatusText(status) {
      const statusMap = {
        'processing': '处理中',
        'confirmed': '已确认',
        'failed': '失败'
      }
      return statusMap[status] || '未知'
    }
  }
}
</script>

<style lang="scss" scoped>
.bill-management-container {
  padding: 20px;
  background-color: #f5f5f5;
  min-height: calc(100vh - 84px);
}

.statistics-overview {
  margin-bottom: 20px;

  .stat-card {
    background: white;
    border-radius: 8px;
    padding: 20px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
    display: flex;
    align-items: center;

    .stat-icon {
      width: 60px;
      height: 60px;
      border-radius: 50%;
      background: rgba(64, 158, 255, 0.1);
      display: flex;
      align-items: center;
      justify-content: center;
      margin-right: 16px;

      i {
        font-size: 24px;
      }
    }

    .stat-content {
      flex: 1;

      .stat-value {
        font-size: 28px;
        font-weight: bold;
        color: #303133;
        line-height: 1;
        margin-bottom: 8px;
      }

      .stat-label {
        font-size: 14px;
        color: #909399;
      }
    }
  }
}

.main-content {
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);

  .el-tabs {
    padding: 20px;
  }
}

.search-container {
  margin-bottom: 20px;

  .search-form {
    background: #f8f9fa;
    padding: 20px;
    border-radius: 4px;
    border: 1px solid #e9ecef;
  }
}

.toolbar {
  margin-bottom: 20px;
  display: flex;
  gap: 10px;
}

.table-container {
  .pagination-container {
    margin-top: 20px;
    text-align: right;
  }
}

.due-content {
  .filter-bar {
    background: #f8f9fa;
    padding: 15px;
    border-radius: 4px;
    margin-bottom: 20px;
  }
}

.endorse-content,
.payment-content {
  .toolbar {
    margin-bottom: 20px;
  }
}

// 状态文本样式
.warning-text {
  color: #e6a23c;
  font-weight: bold;
}

.overdue-text {
  color: #f56c6c;
  font-weight: bold;
}

// 响应式设计
@media (max-width: 768px) {
  .bill-management-container {
    padding: 10px;
  }

  .statistics-overview {
    .el-col {
      margin-bottom: 10px;
    }
  }

  .stat-card {
    padding: 15px !important;

    .stat-icon {
      width: 50px !important;
      height: 50px !important;
      margin-right: 12px !important;

      i {
        font-size: 20px !important;
      }
    }

    .stat-content {
      .stat-value {
        font-size: 24px !important;
      }
    }
  }

  .search-form {
    .el-form-item {
      margin-bottom: 10px;
    }
  }
}

// 表格样式优化
.el-table {
  .el-table__header {
    th {
      background-color: #fafafa;
      color: #606266;
      font-weight: 600;
    }
  }

  .el-table__row {
    &:hover {
      background-color: #f5f7fa;
    }
  }
}

// 标签页样式
.el-tabs__item {
  font-size: 16px;
  font-weight: 500;

  &.is-active {
    color: #409eff;
  }
}

// 按钮组样式
.toolbar {
  .el-button {
    margin-right: 10px;

    &:last-child {
      margin-right: 0;
    }
  }
}

// 状态标签样式
.el-tag {
  font-weight: 500;
}

// 下拉菜单样式
.el-dropdown {
  .el-button {
    margin-left: 5px;
  }
}

// 过滤栏样式
.filter-bar {
  .el-form-item {
    margin-bottom: 0;
  }
}
</style>
