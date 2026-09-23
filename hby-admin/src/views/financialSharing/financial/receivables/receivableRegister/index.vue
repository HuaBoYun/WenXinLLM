<template>
  <div class="receivable-register-container">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-left">
        <h1 class="page-title">
          <i class="el-icon-edit"></i>
          应收登记
        </h1>
        <p class="page-description">管理应收账款的登记、审核和确认</p>
      </div>
      <div class="header-right">
        <el-button type="primary" icon="el-icon-plus" @click="showCreateDialog">
          新增应收
        </el-button>
        <el-button type="success" icon="el-icon-check" @click="batchAudit">
          批量审核
        </el-button>
        <el-button type="warning" icon="el-icon-upload2" @click="importReceivables">
          批量导入
        </el-button>
      </div>
    </div>

    <!-- 应收统计 -->
    <div class="receivable-stats">
      <el-row :gutter="24">
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon total">
              <i class="el-icon-money"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ formatAmount(stats.totalReceivables) }}</div>
              <div class="stat-label">应收总额</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon new">
              <i class="el-icon-plus"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ stats.newReceivablesCount }}</div>
              <div class="stat-label">本月新增</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon pending">
              <i class="el-icon-time"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ stats.pendingAuditCount }}</div>
              <div class="stat-label">待审核</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon approved">
              <i class="el-icon-check"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ stats.approvedCount }}</div>
              <div class="stat-label">已审核</div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 搜索区域 -->
    <div class="search-area">
      <el-form :model="searchForm" :inline="true" size="small">
        <el-form-item label="单据编号">
          <el-input v-model="searchForm.documentNo" placeholder="请输入单据编号" clearable />
        </el-form-item>
        <el-form-item label="客户名称">
          <el-select v-model="searchForm.customerId" placeholder="请选择客户" clearable filterable>
            <el-option
              v-for="customer in customerOptions"
              :key="customer.customerId"
              :label="customer.customerName"
              :value="customer.customerId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="单据状态">
          <el-select v-model="searchForm.documentStatus" placeholder="请选择状态" clearable>
            <el-option label="草稿" :value="0" />
            <el-option label="待审核" :value="1" />
            <el-option label="已审核" :value="2" />
            <el-option label="已拒绝" :value="3" />
          </el-select>
        </el-form-item>
        <el-form-item label="业务类型">
          <el-select v-model="searchForm.businessType" placeholder="请选择业务类型" clearable>
            <el-option label="销售收入" :value="1" />
            <el-option label="服务收入" :value="2" />
            <el-option label="租赁收入" :value="3" />
            <el-option label="其他收入" :value="4" />
          </el-select>
        </el-form-item>
        <el-form-item label="登记日期">
          <el-date-picker
            v-model="searchForm.dateRange"
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
          <el-button type="primary" icon="el-icon-search" @click="handleSearch">搜索</el-button>
          <el-button icon="el-icon-refresh" @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 数据表格 -->
    <div class="table-container">
      <el-table
        v-loading="loading"
        :data="tableData"
        stripe
        border
        height="500"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="documentNo" label="单据编号" width="180" />
        <el-table-column prop="customerName" label="客户名称" width="150" />
        <el-table-column prop="businessTypeName" label="业务类型" width="100" />
        <el-table-column prop="receivableAmount" label="应收金额" width="120" align="right">
          <template slot-scope="scope">
            <span class="amount-text">{{ formatAmount(scope.row.receivableAmount) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="receivedAmount" label="已收金额" width="120" align="right">
          <template slot-scope="scope">
            <span class="amount-text">{{ formatAmount(scope.row.receivedAmount) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="remainingAmount" label="未收金额" width="120" align="right">
          <template slot-scope="scope">
            <span class="amount-text">{{ formatAmount(scope.row.remainingAmount) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="documentStatus" label="单据状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="getStatusType(scope.row.documentStatus)">
              {{ getStatusText(scope.row.documentStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="dueDate" label="到期日期" width="120" />
        <el-table-column prop="createTime" label="创建时间" width="150" />
        <el-table-column label="操作" width="200" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" @click="viewDetail(scope.row)">详情</el-button>
            <el-button 
              v-if="scope.row.documentStatus === 1" 
              size="mini" 
              type="success" 
              @click="auditReceivable(scope.row)"
            >
              审核
            </el-button>
            <el-button 
              v-if="scope.row.documentStatus === 0" 
              size="mini" 
              type="warning" 
              @click="editReceivable(scope.row)"
            >
              编辑
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

    <!-- 创建/编辑应收对话框 -->
    <el-dialog
      :title="dialogTitle"
      :visible.sync="createDialogVisible"
      width="800px"
      :close-on-click-modal="false"
    >
      <el-form :model="createForm" :rules="createRules" ref="createForm" label-width="120px">
        <el-row :gutter="24">
          <el-col :span="12">
            <el-form-item label="单据编号" prop="documentNo">
              <el-input v-model="createForm.documentNo" placeholder="请输入单据编号" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="客户" prop="customerId">
              <el-select v-model="createForm.customerId" placeholder="请选择客户" filterable>
                <el-option
                  v-for="customer in customerOptions"
                  :key="customer.customerId"
                  :label="customer.customerName"
                  :value="customer.customerId"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="24">
          <el-col :span="12">
            <el-form-item label="业务类型" prop="businessType">
              <el-select v-model="createForm.businessType" placeholder="请选择业务类型">
                <el-option label="销售收入" :value="1" />
                <el-option label="服务收入" :value="2" />
                <el-option label="租赁收入" :value="3" />
                <el-option label="其他收入" :value="4" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="应收金额" prop="receivableAmount">
              <el-input-number
                v-model="createForm.receivableAmount"
                :precision="2"
                :min="0"
                placeholder="请输入应收金额"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="24">
          <el-col :span="12">
            <el-form-item label="到期日期" prop="dueDate">
              <el-date-picker
                v-model="createForm.dueDate"
                type="date"
                placeholder="请选择到期日期"
                format="yyyy-MM-dd"
                value-format="yyyy-MM-dd"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="币种" prop="currency">
              <el-select v-model="createForm.currency" placeholder="请选择币种">
                <el-option label="人民币" value="CNY" />
                <el-option label="美元" value="USD" />
                <el-option label="欧元" value="EUR" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="24">
          <el-col :span="12">
            <el-form-item label="单据状态" prop="documentStatus">
              <el-select v-model="createForm.documentStatus" placeholder="请选择单据状态">
                <el-option label="草稿" :value="0" />
                <el-option label="待审核" :value="1" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="备注">
          <el-input
            v-model="createForm.remarks"
            type="textarea"
            :rows="3"
            placeholder="请输入备注信息"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="createDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleCreate">保存</el-button>
      </div>
    </el-dialog>

    <!-- 审核对话框 -->
    <el-dialog
      title="应收审核"
      :visible.sync="auditDialogVisible"
      width="600px"
      :close-on-click-modal="false"
    >
      <div class="audit-info">
        <el-row :gutter="24">
          <el-col :span="12">
            <div class="info-item">
              <label>单据编号：</label>
              <span>{{ auditForm.documentNo }}</span>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="info-item">
              <label>客户名称：</label>
              <span>{{ auditForm.customerName }}</span>
            </div>
          </el-col>
        </el-row>
        <el-row :gutter="24">
          <el-col :span="12">
            <div class="info-item">
              <label>应收金额：</label>
              <span class="amount-text">{{ formatAmount(auditForm.receivableAmount) }}</span>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="info-item">
              <label>到期日期：</label>
              <span>{{ auditForm.dueDate }}</span>
            </div>
          </el-col>
        </el-row>
      </div>

      <el-form :model="auditForm" :rules="auditRules" ref="auditForm" label-width="100px">
        <el-form-item label="审核结果" prop="auditResult">
          <el-radio-group v-model="auditForm.auditResult">
            <el-radio :label="2">通过</el-radio>
            <el-radio :label="3">拒绝</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="审核意见" prop="auditComments">
          <el-input
            v-model="auditForm.auditComments"
            type="textarea"
            :rows="4"
            placeholder="请输入审核意见"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="auditDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleAudit">确认审核</el-button>
      </div>
    </el-dialog>

    <!-- 详情查看对话框 -->
    <el-dialog
      title="应收单据详情"
      :visible.sync="detailDialogVisible"
      width="800px"
      :close-on-click-modal="true"
    >
      <div class="detail-container">
        <!-- 基本信息 -->
        <div class="detail-section">
          <div class="section-title">基本信息</div>
          <el-row :gutter="24">
            <el-col :span="12">
              <div class="detail-item">
                <label>单据编号：</label>
                <span>{{ detailData.documentNo }}</span>
              </div>
            </el-col>
            <el-col :span="12">
              <div class="detail-item">
                <label>单据状态：</label>
                <el-tag :type="getStatusType(detailData.documentStatus)">
                  {{ getStatusText(detailData.documentStatus) }}
                </el-tag>
              </div>
            </el-col>
          </el-row>
          <el-row :gutter="24">
            <el-col :span="12">
              <div class="detail-item">
                <label>客户名称：</label>
                <span>{{ detailData.customerName || detailData.customerId }}</span>
              </div>
            </el-col>
            <el-col :span="12">
              <div class="detail-item">
                <label>业务类型：</label>
                <span>{{ detailData.businessTypeName || getBusinessTypeName(detailData.businessType) }}</span>
              </div>
            </el-col>
          </el-row>
        </div>

        <!-- 金额信息 -->
        <div class="detail-section">
          <div class="section-title">金额信息</div>
          <el-row :gutter="24">
            <el-col :span="8">
              <div class="detail-item">
                <label>应收金额：</label>
                <span class="amount-text">{{ formatAmount(detailData.receivableAmount) }}</span>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="detail-item">
                <label>已收金额：</label>
                <span class="amount-success">{{ formatAmount(detailData.receivedAmount) }}</span>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="detail-item">
                <label>未收金额：</label>
                <span class="amount-warning">{{ formatAmount(detailData.remainingAmount) }}</span>
              </div>
            </el-col>
          </el-row>
          <el-row :gutter="24">
            <el-col :span="12">
              <div class="detail-item">
                <label>币种：</label>
                <span>{{ detailData.currency || 'CNY' }}</span>
              </div>
            </el-col>
            <el-col :span="12">
              <div class="detail-item">
                <label>汇率：</label>
                <span>{{ detailData.exchangeRate || '1.0000' }}</span>
              </div>
            </el-col>
          </el-row>
        </div>

        <!-- 日期信息 -->
        <div class="detail-section">
          <div class="section-title">日期信息</div>
          <el-row :gutter="24">
            <el-col :span="12">
              <div class="detail-item">
                <label>到期日期：</label>
                <span>{{ detailData.dueDate }}</span>
              </div>
            </el-col>
            <el-col :span="12">
              <div class="detail-item">
                <label>创建时间：</label>
                <span>{{ detailData.createTime }}</span>
              </div>
            </el-col>
          </el-row>
          <el-row :gutter="24">
            <el-col :span="12">
              <div class="detail-item">
                <label>创建人：</label>
                <span>{{ detailData.createBy }}</span>
              </div>
            </el-col>
            <el-col :span="12">
              <div class="detail-item">
                <label>更新时间：</label>
                <span>{{ detailData.updateTime || '-' }}</span>
              </div>
            </el-col>
          </el-row>
        </div>

        <!-- 审核信息（已审核时显示） -->
        <div v-if="detailData.documentStatus === 2 || detailData.documentStatus === 3" class="detail-section">
          <div class="section-title">审核信息</div>
          <el-row :gutter="24">
            <el-col :span="12">
              <div class="detail-item">
                <label>审核人：</label>
                <span>{{ detailData.auditorId || '-' }}</span>
              </div>
            </el-col>
            <el-col :span="12">
              <div class="detail-item">
                <label>审核时间：</label>
                <span>{{ detailData.auditTime || '-' }}</span>
              </div>
            </el-col>
          </el-row>
          <el-row :gutter="24">
            <el-col :span="24">
              <div class="detail-item">
                <label>审核意见：</label>
                <span>{{ detailData.auditComments || '-' }}</span>
              </div>
            </el-col>
          </el-row>
        </div>

        <!-- 备注信息 -->
        <div class="detail-section">
          <div class="section-title">备注信息</div>
          <el-row :gutter="24">
            <el-col :span="24">
              <div class="detail-item">
                <label>备注：</label>
                <span>{{ detailData.remarks || '无' }}</span>
              </div>
            </el-col>
          </el-row>
        </div>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="detailDialogVisible = false">关闭</el-button>
        <el-button
          v-if="detailData.documentStatus === 0"
          type="warning"
          @click="editFromDetail"
        >
          编辑
        </el-button>
        <el-button
          v-if="detailData.documentStatus === 1"
          type="success"
          @click="auditFromDetail"
        >
          审核
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { 
  getReceivableDocumentPage,
  saveOrUpdateReceivableDocument,
  auditReceivableDocument,
  getCustomerPage
} from '@/api/financialSharing/receivables'

export default {
  name: 'ReceivableRegisterIndex',
  data() {
    return {
      loading: false,
      tableData: [],
      selectedRows: [],
      stats: {
        totalReceivables: 0,
        newReceivablesCount: 0,
        pendingAuditCount: 0,
        approvedCount: 0
      },
      searchForm: {
        documentNo: '',
        customerId: '',
        documentStatus: '',
        businessType: '',
        dateRange: []
      },
      pagination: {
        currentPage: 1,
        pageSize: 20,
        total: 0
      },
      customerOptions: [],
      createDialogVisible: false,
      createForm: {
        documentNo: '',
        customerId: '',
        businessType: '',
        receivableAmount: 0,
        dueDate: '',
        currency: 'CNY',
        documentStatus: 0,
        remarks: ''
      },
      createRules: {
        documentNo: [{ required: true, message: '请输入单据编号', trigger: 'blur' }],
        customerId: [{ required: true, message: '请选择客户', trigger: 'change' }],
        businessType: [{ required: true, message: '请选择业务类型', trigger: 'change' }],
        receivableAmount: [{ required: true, message: '请输入应收金额', trigger: 'blur' }],
        dueDate: [{ required: true, message: '请选择到期日期', trigger: 'change' }]
      },
      auditDialogVisible: false,
      auditForm: {
        receivableId: '',
        documentNo: '',
        customerName: '',
        receivableAmount: 0,
        dueDate: '',
        auditResult: 2,
        auditComments: ''
      },
      auditRules: {
        auditResult: [{ required: true, message: '请选择审核结果', trigger: 'change' }],
        auditComments: [{ required: true, message: '请输入审核意见', trigger: 'blur' }]
      },
      isEdit: false,
      // 详情对话框
      detailDialogVisible: false,
      detailData: {}
    }
  },
  computed: {
    dialogTitle() {
      return this.isEdit ? '编辑应收' : '新增应收'
    }
  },
  mounted() {
    this.loadData()
    this.loadStats()
    this.loadCustomerOptions()
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
        const response = await getReceivableDocumentPage(params)
        if (response.code === 1) {
          // 字段映射：后端返回字段 -> 前端表格字段
          const list = (response.data.tlist || response.data.records || []).map(item => ({
            ...item,
            receivableId: item.receivableId || item.documentId,
            documentNo: item.documentNo,
            receivableAmount: item.receivableAmount ?? item.totalAmount ?? 0,
            receivedAmount: item.receivedAmount ?? item.paidAmount ?? 0,
            remainingAmount: item.remainingAmount ?? item.unpaidAmount ?? 0,
            businessTypeName: item.businessTypeName || this.getBusinessTypeName(item.businessType),
            customerName: item.customerName || this.getCustomerName(item.customerId),
            // 使用 ?? 避免 0 被当作 falsy 值跳过
            documentStatus: item.documentStatus ?? item.status ?? 0,
            dueDate: item.dueDate || ''
          }))
          this.tableData = list
          this.pagination.total = response.data.totalRecord || response.data.total || 0
        }
      } catch (error) {
        this.$message.error('加载数据失败')
      } finally {
        this.loading = false
      }
    },
    // 将状态字符串映射为数字
    mapStatusToNumber(status) {
      if (typeof status === 'number') return status
      const statusMap = {
        'DRAFT': 0,
        'UNPAID': 1,
        'PAID': 2,
        'PARTIAL_PAID': 1,
        'OVERDUE': 3
      }
      return statusMap[status] !== undefined ? statusMap[status] : status
    },
    // 根据业务类型ID获取名称
    getBusinessTypeName(businessType) {
      const typeMap = { 1: '销售收入', 2: '服务收入', 3: '租赁收入', 4: '其他收入' }
      return typeMap[businessType] || '未知'
    },
    // 根据客户ID获取客户名称
    getCustomerName(customerId) {
      const customer = this.customerOptions.find(c => c.customerId === customerId)
      return customer ? customer.customerName : customerId || '未知客户'
    },
    async loadStats() {
      // 暂未对接 API，先以空状态展示，待后端接口提供后接入
      this.stats = {
        totalReceivables: 0,
        newReceivablesCount: 0,
        pendingAuditCount: 0,
        approvedCount: 0
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
      if (amount === null || amount === undefined) return '0.00'
      return Number(amount).toFixed(2)
    },
    getStatusType(status) {
      const types = { 0: 'info', 1: 'warning', 2: 'success', 3: 'danger' }
      // 确保 status 为 0 时也能正确匹配
      return status !== null && status !== undefined ? (types[status] ?? 'info') : 'info'
    },
    getStatusText(status) {
      const texts = { 0: '草稿', 1: '待审核', 2: '已审核', 3: '已拒绝' }
      // 确保 status 为 0 时也能正确匹配
      return status !== null && status !== undefined ? (texts[status] ?? '未知') : '草稿'
    },
    handleSearch() {
      this.pagination.currentPage = 1
      this.loadData()
    },
    resetSearch() {
      this.searchForm = {
        documentNo: '',
        customerId: '',
        documentStatus: '',
        businessType: '',
        dateRange: []
      }
      this.handleSearch()
    },
    handleSizeChange(val) {
      this.pagination.pageSize = val
      this.loadData()
    },
    handleCurrentChange(val) {
      this.pagination.currentPage = val
      this.loadData()
    },
    handleSelectionChange(selection) {
      this.selectedRows = selection
    },
    showCreateDialog() {
      this.isEdit = false
      this.createDialogVisible = true
      this.createForm = {
        documentNo: '',
        customerId: '',
        businessType: '',
        receivableAmount: 0,
        dueDate: '',
        currency: 'CNY',
        documentStatus: 0,
        remarks: ''
      }
    },
    async handleCreate() {
      this.$refs.createForm.validate(async (valid) => {
        if (valid) {
          try {
            const response = await saveOrUpdateReceivableDocument(this.createForm)
            if (response.code === 1) {
              this.$message.success(this.isEdit ? '修改成功' : '创建成功')
              this.createDialogVisible = false
              this.loadData()
              this.loadStats()
            }
          } catch (error) {
            this.$message.error(this.isEdit ? '修改失败' : '创建失败')
          }
        }
      })
    },
    viewDetail(row) {
      this.detailData = { ...row }
      this.detailDialogVisible = true
    },
    editFromDetail() {
      this.detailDialogVisible = false
      this.editReceivable(this.detailData)
    },
    auditFromDetail() {
      this.detailDialogVisible = false
      this.auditReceivable(this.detailData)
    },
    editReceivable(row) {
      this.isEdit = true
      this.createDialogVisible = true
      this.createForm = { ...row }
    },
    auditReceivable(row) {
      this.auditDialogVisible = true
      this.auditForm = {
        receivableId: row.receivableId,
        documentNo: row.documentNo,
        customerName: row.customerName,
        receivableAmount: row.receivableAmount,
        dueDate: row.dueDate,
        auditResult: 2,
        auditComments: ''
      }
    },
    async handleAudit() {
      this.$refs.auditForm.validate(async (valid) => {
        if (valid) {
          try {
            const response = await auditReceivableDocument(this.auditForm.receivableId, this.auditForm)
            if (response.code === 1) {
              this.$message.success('审核成功')
              this.auditDialogVisible = false
              this.loadData()
              this.loadStats()
            }
          } catch (error) {
            this.$message.error('审核失败')
          }
        }
      })
    },
    batchAudit() {
      if (this.selectedRows.length === 0) {
        this.$message.warning('请选择要审核的应收记录')
        return
      }
      this.$confirm(`确认批量审核选中的${this.selectedRows.length}条记录？`, '批量审核', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          for (const row of this.selectedRows) {
            await auditReceivableDocument(row.receivableId, { auditResult: 2, auditComments: '批量审核通过' })
          }
          this.$message.success('批量审核成功')
          this.loadData()
          this.loadStats()
        } catch (error) {
          this.$message.error('批量审核失败')
        }
      }).catch(() => {})
    },
    importReceivables() {
      const input = document.createElement('input')
      input.type = 'file'
      input.accept = '.xlsx,.xls,.csv'
      input.onchange = async (e) => {
        const file = e.target.files[0]
        if (!file) return
        this.$message.success(`已选择文件: ${file.name}，导入处理中...`)
        this.loadData()
      }
      input.click()
    }
  }
}
</script>

<style lang="scss" scoped>
.receivable-register-container {
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
        color: #409eff;
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

.receivable-stats {
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

      &.total {
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      }

      &.new {
        background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
      }

      &.pending {
        background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
      }

      &.approved {
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

.search-area {
  background: white;
  border-radius: 12px;
  padding: 24px;
  margin-bottom: 24px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.table-container {
  background: white;
  border-radius: 12px;
  padding: 24px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);

  .amount-text {
    color: #409eff;
    font-weight: 600;
  }
}

.pagination-container {
  margin-top: 20px;
  text-align: right;
}

.dialog-footer {
  text-align: right;
}

.audit-info {
  background: #f8f9fa;
  border-radius: 8px;
  padding: 16px;
  margin-bottom: 16px;

  .info-item {
    display: flex;
    align-items: center;
    margin-bottom: 12px;

    label {
      font-weight: 600;
      color: #606266;
      margin-right: 8px;
      min-width: 80px;
    }

    .amount-text {
      color: #409eff;
      font-weight: 600;
    }
  }
}

// 详情对话框样式
.detail-container {
  .detail-section {
    margin-bottom: 24px;
    padding-bottom: 16px;
    border-bottom: 1px solid #ebeef5;

    &:last-child {
      border-bottom: none;
      margin-bottom: 0;
    }

    .section-title {
      font-size: 16px;
      font-weight: 600;
      color: #303133;
      margin-bottom: 16px;
      padding-left: 10px;
      border-left: 3px solid #409eff;
    }
  }

  .detail-item {
    display: flex;
    align-items: center;
    margin-bottom: 12px;
    font-size: 14px;

    label {
      color: #909399;
      min-width: 80px;
      margin-right: 8px;
    }

    span {
      color: #303133;
    }

    .amount-text {
      color: #409eff;
      font-weight: 600;
      font-size: 16px;
    }

    .amount-success {
      color: #67c23a;
      font-weight: 600;
      font-size: 16px;
    }

    .amount-warning {
      color: #e6a23c;
      font-weight: 600;
      font-size: 16px;
    }
  }
}
</style>
