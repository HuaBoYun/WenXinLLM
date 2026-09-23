<template>
  <div class="collection-management-container">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-left">
        <h1 class="page-title">
          <i class="el-icon-coin"></i>
          收款管理
        </h1>
        <p class="page-description">处理客户收款、核销业务和预收款管理</p>
      </div>
      <div class="header-right">
        <el-button type="primary" icon="el-icon-plus" @click="showCreateDialog">
          新增收款
        </el-button>
        <el-button type="success" icon="el-icon-check" @click="batchWriteOff">
          批量核销
        </el-button>
        <el-button type="warning" icon="el-icon-document" @click="generateCollectionPlan">
          生成收款计划
        </el-button>
      </div>
    </div>

    <!-- 收款统计 -->
    <div class="collection-stats">
      <el-row :gutter="24">
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon pending">
              <i class="el-icon-time"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ stats.pendingCollectionCount }}</div>
              <div class="stat-label">待收款</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon collected">
              <i class="el-icon-check"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ formatAmount(stats.collectedAmount) }}</div>
              <div class="stat-label">本月收款</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon writeoff">
              <i class="el-icon-finished"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ stats.writeOffCount }}</div>
              <div class="stat-label">已核销</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon advance">
              <i class="el-icon-wallet"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ formatAmount(stats.advanceAmount) }}</div>
              <div class="stat-label">预收款余额</div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 标签页 -->
    <div class="tabs-container">
      <el-tabs v-model="activeTab" @tab-click="handleTabClick">
        <!-- 收款单管理 -->
        <el-tab-pane label="收款单管理" name="collection">
          <div class="tab-content">
            <!-- 搜索区域 -->
            <div class="search-area">
              <el-form :model="collectionSearchForm" :inline="true" size="small">
                <el-form-item label="收款单号">
                  <el-input v-model="collectionSearchForm.receiptNo" placeholder="请输入收款单号" clearable />
                </el-form-item>
                <el-form-item label="客户">
                  <el-select v-model="collectionSearchForm.customerId" placeholder="请选择客户" clearable filterable>
                    <el-option
                      v-for="customer in customerOptions"
                      :key="customer.customerId"
                      :label="customer.customerName"
                      :value="customer.customerId"
                    />
                  </el-select>
                </el-form-item>
                <el-form-item label="收款状态">
                  <el-select v-model="collectionSearchForm.receiptStatus" placeholder="请选择状态" clearable>
                    <el-option label="待收款" :value="0" />
                    <el-option label="已收款" :value="1" />
                    <el-option label="已核销" :value="2" />
                    <el-option label="已取消" :value="3" />
                  </el-select>
                </el-form-item>
                <el-form-item label="收款日期">
                  <el-date-picker
                    v-model="collectionSearchForm.dateRange"
                    type="daterange"
                    range-separator="至"
                    start-placeholder="开始日期"
                    end-placeholder="结束日期"
                    format="yyyy-MM-dd"
                    value-format="yyyy-MM-dd"
                    clearable
                  />
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" icon="el-icon-search" @click="handleCollectionSearch">搜索</el-button>
                  <el-button icon="el-icon-refresh" @click="resetCollectionSearch">重置</el-button>
                </el-form-item>
              </el-form>
            </div>

            <!-- 收款单表格 -->
            <div class="table-container">
              <el-table
                v-loading="collectionLoading"
                :data="collectionTableData"
                stripe
                border
                height="400"
                @selection-change="handleCollectionSelectionChange"
              >
                <el-table-column type="selection" width="55" />
                <el-table-column prop="receiptNo" label="收款单号" width="180" />
                <el-table-column prop="customerName" label="客户" width="180" />
                <el-table-column prop="receiptAmount" label="收款金额" width="120" align="right">
                  <template slot-scope="scope">
                    <span class="amount-text">{{ formatPaymentAmount(scope.row.receiptAmount) }}</span>
                  </template>
                </el-table-column>
                <el-table-column prop="paymentMethod" label="收款方式" width="100">
                  <template slot-scope="scope">
                    {{ getPaymentMethodText(scope.row.paymentMethod) }}
                  </template>
                </el-table-column>
                <el-table-column prop="receiptStatus" label="收款状态" width="100">
                  <template slot-scope="scope">
                    <el-tag :type="getPaymentStatusType(scope.row.receiptStatus)">
                      {{ getReceiptStatusText(scope.row.receiptStatus) }}
                    </el-tag>
                  </template>
                </el-table-column>
                <el-table-column prop="receiptDate" label="收款日期" width="120" />
                <el-table-column prop="createBy" label="创建人" width="100" />
                <el-table-column prop="createTime" label="创建时间" width="160">
                  <template slot-scope="scope">
                    {{ formatDateTime(scope.row.createTime) }}
                  </template>
                </el-table-column>
                <el-table-column label="操作" width="280" fixed="right">
                  <template slot-scope="scope">
                    <div style="white-space: nowrap;">
                      <el-button size="mini" type="primary" @click="viewCollectionDetail(scope.row)">详情</el-button>
                      <el-button v-if="scope.row.receiptStatus === 0" size="mini" type="warning" @click="editCollection(scope.row)">编辑</el-button>
                      <el-button v-if="scope.row.receiptStatus === 0" size="mini" type="success" @click="confirmCollection(scope.row)">确认收款</el-button>
                      <el-button v-if="scope.row.receiptStatus === 1" size="mini" type="info" @click="writeOffCollection(scope.row)">核销</el-button>
                    </div>
                  </template>
                </el-table-column>
              </el-table>

              <!-- 分页组件 -->
              <div class="pagination-container">
                <el-pagination
                  @size-change="handleCollectionSizeChange"
                  @current-change="handleCollectionCurrentChange"
                  :current-page="collectionPagination.currentPage"
                  :page-sizes="[10, 20, 50, 100]"
                  :page-size="collectionPagination.pageSize"
                  layout="total, sizes, prev, pager, next, jumper"
                  :total="collectionPagination.total"
                />
              </div>
            </div>
          </div>
        </el-tab-pane>

        <!-- 核销管理 -->
        <el-tab-pane label="核销管理" name="writeoff">
          <div class="tab-content">
            <!-- 搜索区域 -->
            <div class="search-area">
              <el-form :model="writeOffSearchForm" :inline="true" size="small">
                <el-form-item label="核销单号">
                  <el-input v-model="writeOffSearchForm.writeOffNo" placeholder="请输入核销单号" clearable />
                </el-form-item>
                <el-form-item label="客户">
                  <el-select v-model="writeOffSearchForm.customerId" placeholder="请选择客户" clearable filterable>
                    <el-option
                      v-for="customer in customerOptions"
                      :key="customer.customerId"
                      :label="customer.customerName"
                      :value="customer.customerId"
                    />
                  </el-select>
                </el-form-item>
                <el-form-item label="核销状态">
                  <el-select v-model="writeOffSearchForm.writeOffStatus" placeholder="请选择状态" clearable>
                    <el-option label="待核销" :value="0" />
                    <el-option label="已核销" :value="1" />
                    <el-option label="已撤销" :value="2" />
                  </el-select>
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" icon="el-icon-search" @click="handleWriteOffSearch">搜索</el-button>
                  <el-button icon="el-icon-refresh" @click="resetWriteOffSearch">重置</el-button>
                </el-form-item>
              </el-form>
            </div>

            <!-- 核销记录表格 -->
            <div class="table-container">
              <el-table
                v-loading="writeOffLoading"
                :data="writeOffTableData"
                stripe
                border
                height="400"
              >
                <el-table-column prop="writeOffId" label="核销ID" width="180" show-overflow-tooltip />
                <el-table-column prop="customerName" label="客户" width="150" />
                <el-table-column prop="receiptNo" label="收款单号" width="180" />
                <el-table-column prop="documentNo" label="应收单号" width="180" />
                <el-table-column prop="writeOffAmount" label="核销金额" width="120" align="right">
                  <template slot-scope="scope">
                    <span class="amount-text">{{ formatPaymentAmount(scope.row.writeOffAmount) }}</span>
                  </template>
                </el-table-column>
                <el-table-column prop="writeOffType" label="核销方式" width="100">
                  <template slot-scope="scope">
                    <el-tag :type="scope.row.writeOffType === 1 ? 'success' : 'warning'">
                      {{ scope.row.writeOffType === 1 ? '全额核销' : '部分核销' }}
                    </el-tag>
                  </template>
                </el-table-column>
                <el-table-column prop="writeOffDate" label="核销日期" width="120" />
                <el-table-column prop="createBy" label="操作人" width="100" />
                <el-table-column prop="createTime" label="创建时间" width="160">
                  <template slot-scope="scope">
                    {{ formatDateTime(scope.row.createTime) }}
                  </template>
                </el-table-column>
                <el-table-column label="操作" width="150" fixed="right">
                  <template slot-scope="scope">
                    <el-button size="mini" type="primary" @click="viewWriteOffDetail(scope.row)">详情</el-button>
                    <el-button
                      v-if="scope.row.isLocked !== 1"
                      size="mini"
                      type="danger"
                      @click="revokeWriteOff(scope.row)"
                    >
                      撤销
                    </el-button>
                  </template>
                </el-table-column>
              </el-table>

              <!-- 分页组件 -->
              <div class="pagination-container">
                <el-pagination
                  @size-change="handleWriteOffSizeChange"
                  @current-change="handleWriteOffCurrentChange"
                  :current-page="writeOffPagination.currentPage"
                  :page-sizes="[10, 20, 50, 100]"
                  :page-size="writeOffPagination.pageSize"
                  layout="total, sizes, prev, pager, next, jumper"
                  :total="writeOffPagination.total"
                />
              </div>
            </div>
          </div>
        </el-tab-pane>

        <!-- 预收款管理 -->
        <el-tab-pane label="预收款管理" name="advance">
          <div class="tab-content">
            <!-- 搜索区域 -->
            <div class="search-area">
              <el-form :model="advanceSearchForm" :inline="true" size="small">
                <el-form-item label="预收款单号">
                  <el-input v-model="advanceSearchForm.advanceNo" placeholder="请输入预收款单号" clearable />
                </el-form-item>
                <el-form-item label="客户">
                  <el-select v-model="advanceSearchForm.customerId" placeholder="请选择客户" clearable filterable>
                    <el-option
                      v-for="customer in customerOptions"
                      :key="customer.customerId"
                      :label="customer.customerName"
                      :value="customer.customerId"
                    />
                  </el-select>
                </el-form-item>
                <el-form-item label="预收状态">
                  <el-select v-model="advanceSearchForm.advanceStatus" placeholder="请选择状态" clearable>
                    <el-option label="未冲销" :value="0" />
                    <el-option label="部分冲销" :value="1" />
                    <el-option label="全部冲销" :value="2" />
                  </el-select>
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" icon="el-icon-search" @click="handleAdvanceSearch">搜索</el-button>
                  <el-button icon="el-icon-refresh" @click="resetAdvanceSearch">重置</el-button>
                </el-form-item>
              </el-form>
            </div>

            <!-- 预收款表格 -->
            <div class="table-container">
              <el-table
                v-loading="advanceLoading"
                :data="advanceTableData"
                stripe
                border
                height="400"
              >
                <el-table-column prop="advanceId" label="预收款ID" width="180" show-overflow-tooltip />
                <el-table-column prop="advanceNo" label="预收款单号" width="180" />
                <el-table-column prop="customerName" label="客户" width="150" />
                <el-table-column prop="advanceAmount" label="预收金额" width="120" align="right">
                  <template slot-scope="scope">
                    <span class="amount-text">{{ formatPaymentAmount(scope.row.advanceAmount) }}</span>
                  </template>
                </el-table-column>
                <el-table-column prop="offsetAmount" label="已冲销金额" width="120" align="right">
                  <template slot-scope="scope">
                    <span class="amount-text">{{ formatPaymentAmount(scope.row.offsetAmount) }}</span>
                  </template>
                </el-table-column>
                <el-table-column prop="remainingAmount" label="剩余金额" width="120" align="right">
                  <template slot-scope="scope">
                    <span class="amount-text">{{ formatPaymentAmount(scope.row.remainingAmount) }}</span>
                  </template>
                </el-table-column>
                <el-table-column prop="advanceStatus" label="预收状态" width="100">
                  <template slot-scope="scope">
                    <el-tag :type="getAdvanceStatusType(scope.row.advanceStatus)">
                      {{ getAdvanceStatusText(scope.row.advanceStatus) }}
                    </el-tag>
                  </template>
                </el-table-column>
                <el-table-column prop="advanceDate" label="预收日期" width="120" />
                <el-table-column prop="createTime" label="创建时间" width="160">
                  <template slot-scope="scope">
                    {{ formatDateTime(scope.row.createTime) }}
                  </template>
                </el-table-column>
                <el-table-column label="操作" width="150" fixed="right">
                  <template slot-scope="scope">
                    <el-button size="mini" type="primary" @click="viewAdvanceDetail(scope.row)">详情</el-button>
                    <el-button 
                      v-if="scope.row.remainingAmount > 0" 
                      size="mini" 
                      type="success" 
                      @click="offsetAdvance(scope.row)"
                    >
                      冲销
                    </el-button>
                  </template>
                </el-table-column>
              </el-table>

              <!-- 分页组件 -->
              <div class="pagination-container">
                <el-pagination
                  @size-change="handleAdvanceSizeChange"
                  @current-change="handleAdvanceCurrentChange"
                  :current-page="advancePagination.currentPage"
                  :page-sizes="[10, 20, 50, 100]"
                  :page-size="advancePagination.pageSize"
                  layout="total, sizes, prev, pager, next, jumper"
                  :total="advancePagination.total"
                />
              </div>
            </div>
          </div>
        </el-tab-pane>
      </el-tabs>
    </div>

    <!-- 收款详情弹窗 -->
    <collection-detail-dialog
      ref="collectionDetailDialog"
      @write-off="handleWriteOffFromDetail"
    />

    <!-- 核销弹窗 -->
    <write-off-dialog
      ref="writeOffDialog"
      @success="handleWriteOffSuccess"
    />

    <!-- 新增/编辑收款单弹窗 -->
    <create-receipt-dialog
      ref="createReceiptDialog"
      @success="handleCreateSuccess"
    />
  </div>
</template>

<script>
import {
  getPaymentReceiptPage,
  saveOrUpdatePaymentReceipt,
  writeOffPayment,
  batchWriteOffPayment,
  confirmPaymentReceipt,
  getCustomerPage,
  getWriteOffPage,
  getAdvanceReceiptPage
} from '@/api/financialSharing/receivables'
import CollectionDetailDialog from './components/CollectionDetailDialog.vue'
import WriteOffDialog from './components/WriteOffDialog.vue'
import CreateReceiptDialog from './components/CreateReceiptDialog.vue'

export default {
  name: 'CollectionManagementIndex',
  components: {
    CollectionDetailDialog,
    WriteOffDialog,
    CreateReceiptDialog
  },
  data() {
    return {
      activeTab: 'collection',
      stats: {
        pendingCollectionCount: 0,
        collectedAmount: 0,
        writeOffCount: 0,
        advanceAmount: 0
      },
      // 收款单相关数据
      collectionLoading: false,
      collectionTableData: [],
      collectionSelectedRows: [],
      collectionSearchForm: {
        receiptNo: '',
        customerId: '',
        receiptStatus: '',
        dateRange: []
      },
      collectionPagination: {
        currentPage: 1,
        pageSize: 20,
        total: 0
      },
      // 核销相关数据
      writeOffLoading: false,
      writeOffTableData: [],
      writeOffSearchForm: {
        writeOffNo: '',
        customerId: '',
        writeOffStatus: ''
      },
      writeOffPagination: {
        currentPage: 1,
        pageSize: 20,
        total: 0
      },
      // 预收款相关数据
      advanceLoading: false,
      advanceTableData: [],
      advanceSearchForm: {
        advanceNo: '',
        customerId: '',
        advanceStatus: ''
      },
      advancePagination: {
        currentPage: 1,
        pageSize: 20,
        total: 0
      },
      customerOptions: []
    }
  },
  mounted() {
    this.loadStats()
    this.loadCustomerOptions()
    this.loadCollectionData()
  },
  methods: {
    async loadStats() {
      // 暂未对接 API，先以空状态展示，待后端接口提供后接入
      this.stats = {
        pendingCollectionCount: 0,
        collectedAmount: 0,
        writeOffCount: 0,
        advanceAmount: 0
      }
    },
    async loadCustomerOptions() {
      try {
        const response = await getCustomerPage({ pageSize: 1000 })
        if (response.code === 1) {
          this.customerOptions = response.data.tlist || []
        }
      } catch (error) {
        console.error('加载客户选项失败:', error)
      }
    },
    formatAmount(amount) {
      if (!amount) return '0.00'
      return (amount / 10000).toFixed(2) + '万'
    },
    // 格式化收款金额（保留两位小数，带千分位）
    formatPaymentAmount(amount) {
      if (!amount && amount !== 0) return '0.00'
      return Number(amount).toLocaleString('zh-CN', { minimumFractionDigits: 2, maximumFractionDigits: 2 })
    },
    // 获取收款状态标签类型
    getPaymentStatusType(status) {
      const types = { 0: 'warning', 1: 'success', 2: 'info', 3: 'danger' }
      return types[status] || 'info'
    },
    getReceiptStatusType(status) {
      const types = { 0: 'warning', 1: 'success', 2: 'info', 3: 'danger' }
      return types[status] || 'info'
    },
    getReceiptStatusText(status) {
      const texts = { 0: '待收款', 1: '已收款', 2: '已核销', 3: '已取消' }
      return texts[status] || '未知'
    },
    // 获取收款方式文本
    getPaymentMethodText(method) {
      const texts = { 1: '现金', 2: '银行转账', 3: '支票', 4: '承兑汇票' }
      return texts[method] || '未知'
    },
    // 格式化日期时间
    formatDateTime(dateTime) {
      if (!dateTime) return ''
      // 处理 ISO 格式日期时间
      const date = new Date(dateTime)
      if (isNaN(date.getTime())) return dateTime
      const year = date.getFullYear()
      const month = String(date.getMonth() + 1).padStart(2, '0')
      const day = String(date.getDate()).padStart(2, '0')
      const hours = String(date.getHours()).padStart(2, '0')
      const minutes = String(date.getMinutes()).padStart(2, '0')
      const seconds = String(date.getSeconds()).padStart(2, '0')
      return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`
    },
    getWriteOffStatusType(status) {
      const types = { 0: 'warning', 1: 'success', 2: 'danger' }
      return types[status] || 'info'
    },
    getWriteOffStatusText(status) {
      const texts = { 0: '待核销', 1: '已核销', 2: '已撤销' }
      return texts[status] || '未知'
    },
    getAdvanceStatusType(status) {
      const types = { 0: 'warning', 1: 'primary', 2: 'success' }
      return types[status] || 'info'
    },
    getAdvanceStatusText(status) {
      const texts = { 0: '未冲销', 1: '部分冲销', 2: '全部冲销' }
      return texts[status] || '未知'
    },
    handleTabClick(tab) {
      this.activeTab = tab.name
      if (tab.name === 'collection') {
        this.loadCollectionData()
      } else if (tab.name === 'writeoff') {
        this.loadWriteOffData()
      } else if (tab.name === 'advance') {
        this.loadAdvanceData()
      }
    },
    // 收款单相关方法
    async loadCollectionData() {
      this.collectionLoading = true
      try {
        const params = {
          pageNumber: this.collectionPagination.currentPage,
          pageSize: this.collectionPagination.pageSize,
          ...this.collectionSearchForm
        }
        const response = await getPaymentReceiptPage(params)
        if (response.code === 1) {
          this.collectionTableData = response.data.tlist || []
          this.collectionPagination.total = response.data.totalRecord || 0
        }
      } catch (error) {
        this.$message.error('加载收款数据失败')
      } finally {
        this.collectionLoading = false
      }
    },
    handleCollectionSearch() {
      this.collectionPagination.currentPage = 1
      this.loadCollectionData()
    },
    resetCollectionSearch() {
      this.collectionSearchForm = {
        receiptNo: '',
        customerId: '',
        receiptStatus: '',
        dateRange: []
      }
      this.handleCollectionSearch()
    },
    handleCollectionSizeChange(val) {
      this.collectionPagination.pageSize = val
      this.loadCollectionData()
    },
    handleCollectionCurrentChange(val) {
      this.collectionPagination.currentPage = val
      this.loadCollectionData()
    },
    handleCollectionSelectionChange(selection) {
      this.collectionSelectedRows = selection
    },
    showCreateDialog() {
      this.$refs.createReceiptDialog.open()
    },
    handleCreateSuccess() {
      this.loadCollectionData()
      this.loadStats()
    },
    viewCollectionDetail(row) {
      this.$refs.collectionDetailDialog.open(row)
    },
    editCollection(row) {
      this.$refs.createReceiptDialog.open(row)
    },
    writeOffCollection(row) {
      this.$refs.writeOffDialog.open(row)
    },
    handleWriteOffFromDetail(row) {
      this.$refs.writeOffDialog.open(row)
    },
    handleWriteOffSuccess() {
      this.loadCollectionData()
      this.$message.success('核销操作完成，数据已刷新')
    },
    batchWriteOff() {
      if (this.collectionSelectedRows.length === 0) {
        this.$message.warning('请选择要核销的收款记录')
        return
      }
      // 过滤出可核销的收款单（状态为已收款=1）
      const validRows = this.collectionSelectedRows.filter(row => row.receiptStatus === 1)
      if (validRows.length === 0) {
        this.$message.warning('所选收款单中没有可核销的记录（只有已收款状态的收款单才能核销）')
        return
      }
      const invalidCount = this.collectionSelectedRows.length - validRows.length
      let confirmMsg = `确定要批量核销选中的 ${validRows.length} 笔收款单吗？`
      if (invalidCount > 0) {
        confirmMsg += `\n（${invalidCount} 笔状态不符的收款单将被跳过）`
      }
      this.$confirm(confirmMsg, '批量核销确认', {
        confirmButtonText: '确定核销',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.doBatchWriteOff(validRows)
      }).catch(() => {})
    },
    async doBatchWriteOff(rows) {
      const loading = this.$loading({
        lock: true,
        text: '正在批量核销...',
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.7)'
      })
      try {
        const receiptIds = rows.map(row => row.receiptId)
        const res = await batchWriteOffPayment({
          receiptIds: receiptIds,
          operatorId: this.$store.getters.userId || '',
          remark: '批量核销'
        })
        if (res.code === 1) {
          this.$message.success(res.data || '批量核销成功')
          this.loadCollectionData()
          this.loadStats()
        } else {
          this.$message.error(res.msg || '批量核销失败')
        }
      } catch (error) {
        console.error('批量核销失败:', error)
        this.$message.error('批量核销失败，请稍后重试')
      } finally {
        loading.close()
      }
    },
    // 确认收款（将待收款状态改为已收款）
    confirmCollection(row) {
      this.$confirm(`确定要确认收款单【${row.receiptNo}】吗？`, '确认收款', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'info'
      }).then(async () => {
        const loading = this.$loading({
          lock: true,
          text: '正在确认收款...',
          spinner: 'el-icon-loading',
          background: 'rgba(0, 0, 0, 0.7)'
        })
        try {
          const res = await confirmPaymentReceipt(row.receiptId, {
            confirmAmount: row.receiptAmount,
            confirmBy: this.$store.getters.userId || ''
          })
          if (res.code === 1) {
            this.$message.success('确认收款成功')
            this.loadCollectionData()
            this.loadStats()
          } else {
            this.$message.error(res.msg || '确认收款失败')
          }
        } catch (error) {
          console.error('确认收款失败:', error)
          this.$message.error('确认收款失败，请稍后重试')
        } finally {
          loading.close()
        }
      }).catch(() => {})
    },
    generateCollectionPlan() {
      this.$confirm('确认为当前应收单据生成收款计划？', '生成收款计划', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$message.success('收款计划生成成功')
        this.loadCollectionData()
      }).catch(() => {})
    },
    // 核销相关方法
    async loadWriteOffData() {
      this.writeOffLoading = true
      try {
        const params = {
          pageNumber: this.writeOffPagination.currentPage,
          pageSize: this.writeOffPagination.pageSize,
          ...this.writeOffSearchForm
        }
        const response = await getWriteOffPage(params)
        if (response.code === 1) {
          this.writeOffTableData = response.data.tlist || []
          this.writeOffPagination.total = response.data.totalRecord || 0
        }
      } catch (error) {
        console.error('加载核销数据失败:', error)
        this.$message.error('加载核销数据失败')
      } finally {
        this.writeOffLoading = false
      }
    },
    handleWriteOffSearch() {
      this.writeOffPagination.currentPage = 1
      this.loadWriteOffData()
    },
    resetWriteOffSearch() {
      this.writeOffSearchForm = {
        writeOffNo: '',
        customerId: '',
        writeOffStatus: ''
      }
      this.handleWriteOffSearch()
    },
    handleWriteOffSizeChange(val) {
      this.writeOffPagination.pageSize = val
      this.loadWriteOffData()
    },
    handleWriteOffCurrentChange(val) {
      this.writeOffPagination.currentPage = val
      this.loadWriteOffData()
    },
    viewWriteOffDetail(row) {
      const content = `
        <p><b>核销编号：</b>${row.writeOffNo || row.id || '-'}</p>
        <p><b>客户名称：</b>${row.customerName || '-'}</p>
        <p><b>核销金额：</b>${row.writeOffAmount || row.amount || 0}</p>
        <p><b>核销状态：</b>${row.statusName || row.writeOffStatus || '-'}</p>
        <p><b>核销日期：</b>${row.writeOffDate || row.createTime || '-'}</p>
        <p><b>备注：</b>${row.remark || '-'}</p>
      `
      this.$alert(content, '核销详情', { dangerouslyUseHTMLString: true })
    },
    revokeWriteOff(row) {
      this.$confirm('确定要撤销此核销记录吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$message.success('撤销成功')
        this.loadWriteOffData()
      })
    },
    // 预收款相关方法
    async loadAdvanceData() {
      this.advanceLoading = true
      try {
        const params = {
          pageNumber: this.advancePagination.currentPage,
          pageSize: this.advancePagination.pageSize,
          ...this.advanceSearchForm
        }
        const response = await getAdvanceReceiptPage(params)
        if (response.code === 1) {
          this.advanceTableData = response.data.tlist || []
          this.advancePagination.total = response.data.totalRecord || 0
        }
      } catch (error) {
        console.error('加载预收款数据失败:', error)
        this.$message.error('加载预收款数据失败')
      } finally {
        this.advanceLoading = false
      }
    },
    handleAdvanceSearch() {
      this.advancePagination.currentPage = 1
      this.loadAdvanceData()
    },
    resetAdvanceSearch() {
      this.advanceSearchForm = {
        advanceNo: '',
        customerId: '',
        advanceStatus: ''
      }
      this.handleAdvanceSearch()
    },
    handleAdvanceSizeChange(val) {
      this.advancePagination.pageSize = val
      this.loadAdvanceData()
    },
    handleAdvanceCurrentChange(val) {
      this.advancePagination.currentPage = val
      this.loadAdvanceData()
    },
    viewAdvanceDetail(row) {
      const content = `
        <p><b>预收款编号：</b>${row.advanceNo || row.id || '-'}</p>
        <p><b>客户名称：</b>${row.customerName || '-'}</p>
        <p><b>预收金额：</b>${row.advanceAmount || row.amount || 0}</p>
        <p><b>已冲销金额：</b>${row.offsetAmount || 0}</p>
        <p><b>剩余金额：</b>${row.remainingAmount || 0}</p>
        <p><b>状态：</b>${row.statusName || row.advanceStatus || '-'}</p>
        <p><b>收款日期：</b>${row.receiptDate || row.createTime || '-'}</p>
      `
      this.$alert(content, '预收款详情', { dangerouslyUseHTMLString: true })
    },
    offsetAdvance(row) {
      this.$confirm(`确认对预收款「${row.advanceNo || row.id || ''}」执行冲销操作？`, '预收款冲销', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$message.success('冲销操作成功')
        this.loadAdvanceData()
      }).catch(() => {})
    }
  }
}
</script>

<style lang="scss" scoped>
.collection-management-container {
  padding: 20px;
  background: #f5f7fa;
  min-height: calc(100vh - 84px);
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: white;
  border-radius: 12px;
  padding: 24px;
  margin-bottom: 24px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);

  .header-left {
    .page-title {
      font-size: 24px;
      font-weight: 600;
      color: #303133;
      margin: 0 0 8px 0;
      display: flex;
      align-items: center;

      i {
        margin-right: 12px;
        color: #67c23a;
      }
    }

    .page-description {
      color: #606266;
      font-size: 14px;
      margin: 0;
    }
  }

  .header-right {
    .el-button {
      margin-left: 12px;
    }
  }
}

.collection-stats {
  margin-bottom: 24px;

  .stat-card {
    background: white;
    border-radius: 12px;
    padding: 24px;
    display: flex;
    align-items: center;
    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
    transition: all 0.3s ease;

    &:hover {
      transform: translateY(-2px);
      box-shadow: 0 8px 25px rgba(0, 0, 0, 0.15);
    }

    .stat-icon {
      width: 60px;
      height: 60px;
      border-radius: 12px;
      display: flex;
      align-items: center;
      justify-content: center;
      margin-right: 16px;

      i {
        font-size: 28px;
        color: white;
      }

      &.pending {
        background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
      }

      &.collected {
        background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
      }

      &.writeoff {
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      }

      &.advance {
        background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
      }
    }

    .stat-content {
      .stat-value {
        font-size: 28px;
        font-weight: 600;
        color: #303133;
        margin-bottom: 4px;
      }

      .stat-label {
        font-size: 14px;
        color: #909399;
      }
    }
  }
}

.tabs-container {
  background: white;
  border-radius: 12px;
  padding: 24px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);

  .tab-content {
    .search-area {
      background: #f8f9fa;
      border-radius: 8px;
      padding: 20px;
      margin-bottom: 20px;
    }

    .table-container {
      .amount-text {
        color: #67c23a;
        font-weight: 600;
      }
    }

    .pagination-container {
      margin-top: 20px;
      text-align: right;
    }
  }
}
</style>
