<template>
  <div class="contract-container">
    <div class="search-container">
      <el-form :model="searchForm" ref="searchForm" :inline="true" class="search-form">
        <el-form-item label="合同编号" prop="contractNumber">
          <el-input
            v-model="searchForm.contractNumber"
            placeholder="请输入合同编号"
            clearable
            style="width: 180px"
          />
        </el-form-item>
        <el-form-item label="合同名称" prop="contractName">
          <el-input
            v-model="searchForm.contractName"
            placeholder="请输入合同名称"
            clearable
            style="width: 180px"
          />
        </el-form-item>
        <el-form-item label="合同类型" prop="contractType">
          <el-select
            v-model="searchForm.contractType"
            placeholder="请选择合同类型"
            clearable
            style="width: 150px"
          >
            <el-option label="采购合同" value="PROCUREMENT" />
            <el-option label="服务合同" value="SERVICE" />
            <el-option label="租赁合同" value="LEASE" />
            <el-option label="咨询合同" value="CONSULTING" />
            <el-option label="其他合同" value="OTHER" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select
            v-model="searchForm.status"
            placeholder="请选择状态"
            clearable
            style="width: 120px"
          >
            <el-option label="草稿" value="DRAFT" />
            <el-option label="待审批" value="PENDING" />
            <el-option label="已审批" value="APPROVED" />
            <el-option label="执行中" value="EXECUTING" />
            <el-option label="已完成" value="COMPLETED" />
            <el-option label="已终止" value="TERMINATED" />
            <el-option label="已拒绝" value="REJECTED" />
          </el-select>
        </el-form-item>
        <el-form-item label="供应商" prop="supplier">
          <el-input
            v-model="searchForm.supplier"
            placeholder="请输入供应商名称"
            clearable
            style="width: 150px"
          />
        </el-form-item>
        <el-form-item label="签署日期" prop="dateRange">
          <el-date-picker
            v-model="searchForm.dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            style="width: 240px"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <div class="toolbar">
      <el-button type="primary" @click="handleAdd">新增合同</el-button>
      <el-button type="success" @click="handleBatchApprove" :disabled="!multipleSelection.length">
        批量审批
      </el-button>
      <el-button type="info" @click="handleContractAnalysis">合同分析</el-button>
      <el-button type="warning" @click="handleExport">导出</el-button>
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
        <el-table-column prop="contractNumber" label="合同编号" width="150" />
        <el-table-column prop="contractName" label="合同名称" min-width="200" show-overflow-tooltip />
        <el-table-column prop="contractTypeName" label="合同类型" width="120">
          <template slot-scope="scope">
            <el-tag :type="getContractTypeColor(scope.row.contractType)" size="small">
              {{ scope.row.contractTypeName }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="supplierName" label="供应商" min-width="150" show-overflow-tooltip />
        <el-table-column prop="contractAmount" label="合同金额" width="120">
          <template slot-scope="scope">
            <span class="amount">¥{{ formatAmount(scope.row.contractAmount) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="paidAmount" label="已付金额" width="120">
          <template slot-scope="scope">
            <span class="amount paid">¥{{ formatAmount(scope.row.paidAmount) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="remainingAmount" label="待付金额" width="120">
          <template slot-scope="scope">
            <span class="amount remaining">¥{{ formatAmount(scope.row.remainingAmount) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="paymentProgress" label="付款进度" width="120">
          <template slot-scope="scope">
            <el-progress 
              :percentage="scope.row.paymentProgress" 
              :color="getProgressColor(scope.row.paymentProgress)"
              :stroke-width="8"
              text-inside
            />
          </template>
        </el-table-column>
        <el-table-column prop="statusName" label="状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="getStatusColor(scope.row.status)" size="small">
              {{ scope.row.statusName }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="signDate" label="签署日期" width="120" />
        <el-table-column prop="endDate" label="结束日期" width="120" />
        <el-table-column prop="creator" label="创建人" width="100" />
        <el-table-column label="操作" width="400" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" @click="handleView(scope.row)">查看</el-button>
            <el-button 
              size="mini" 
              type="primary" 
              @click="handleEdit(scope.row)"
              v-if="scope.row.status === 'DRAFT'"
            >
              编辑
            </el-button>
            <el-button 
              size="mini" 
              type="success" 
              @click="handleApprove(scope.row)"
              v-if="scope.row.status === 'PENDING'"
            >
              审批
            </el-button>
            <el-button 
              size="mini" 
              type="info" 
              @click="handleFulfill(scope.row)"
              v-if="scope.row.status === 'EXECUTING'"
            >
              履约
            </el-button>
            <el-button 
              size="mini" 
              type="warning" 
              @click="handlePaymentRequest(scope.row)"
              v-if="scope.row.status === 'EXECUTING'"
            >
              付款申请
            </el-button>
            <el-button 
              size="mini" 
              type="danger" 
              @click="handleDelete(scope.row)"
              v-if="scope.row.status === 'DRAFT'"
            >
              删除
            </el-button>
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

    <!-- 合同详情对话框 -->
    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogVisible"
      width="1200px"
      @close="handleDialogClose"
    >
      <el-form :model="formData" :rules="formRules" ref="formRef" label-width="120px">
        <el-tabs v-model="activeTab">
          <el-tab-pane label="基本信息" name="basic">
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="合同编号" prop="contractNumber">
                  <el-input v-model="formData.contractNumber" :disabled="isViewMode" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="合同名称" prop="contractName">
                  <el-input v-model="formData.contractName" :disabled="isViewMode" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="合同类型" prop="contractType">
                  <el-select v-model="formData.contractType" :disabled="isViewMode" style="width: 100%">
                    <el-option label="采购合同" value="PROCUREMENT" />
                    <el-option label="服务合同" value="SERVICE" />
                    <el-option label="租赁合同" value="LEASE" />
                    <el-option label="咨询合同" value="CONSULTING" />
                    <el-option label="其他合同" value="OTHER" />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="供应商" prop="supplierId">
                  <el-select v-model="formData.supplierId" :disabled="isViewMode" style="width: 100%" filterable>
                    <el-option 
                      v-for="supplier in supplierList" 
                      :key="supplier.supplierId" 
                      :label="supplier.supplierName" 
                      :value="supplier.supplierId"
                    />
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="合同金额" prop="contractAmount">
                  <el-input-number 
                    v-model="formData.contractAmount" 
                    :min="0" 
                    :precision="2" 
                    :disabled="isViewMode"
                    style="width: 100%"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="12" v-if="isViewMode">
                <el-form-item label="已付金额">
                  <el-input-number 
                    v-model="formData.paidAmount" 
                    :precision="2" 
                    disabled
                    style="width: 100%"
                  />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="8">
                <el-form-item label="签署日期" prop="signDate">
                  <el-date-picker
                    v-model="formData.signDate"
                    type="date"
                    :disabled="isViewMode"
                    style="width: 100%"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="开始日期" prop="startDate">
                  <el-date-picker
                    v-model="formData.startDate"
                    type="date"
                    :disabled="isViewMode"
                    style="width: 100%"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="结束日期" prop="endDate">
                  <el-date-picker
                    v-model="formData.endDate"
                    type="date"
                    :disabled="isViewMode"
                    style="width: 100%"
                  />
                </el-form-item>
              </el-col>
            </el-row>
            <el-form-item label="合同描述" prop="contractDescription">
              <el-input
                v-model="formData.contractDescription"
                type="textarea"
                :rows="3"
                :disabled="isViewMode"
                placeholder="请输入合同描述"
              />
            </el-form-item>
            <el-form-item label="付款条款" prop="paymentTerms">
              <el-input
                v-model="formData.paymentTerms"
                type="textarea"
                :rows="2"
                :disabled="isViewMode"
                placeholder="请输入付款条款"
              />
            </el-form-item>
            <el-form-item label="交付条款" prop="deliveryTerms">
              <el-input
                v-model="formData.deliveryTerms"
                type="textarea"
                :rows="2"
                :disabled="isViewMode"
                placeholder="请输入交付条款"
              />
            </el-form-item>
            <el-form-item label="质量条款" prop="qualityTerms">
              <el-input
                v-model="formData.qualityTerms"
                type="textarea"
                :rows="2"
                :disabled="isViewMode"
                placeholder="请输入质量条款"
              />
            </el-form-item>
          </el-tab-pane>

          <el-tab-pane label="付款计划" name="payment" v-if="isViewMode">
            <el-table :data="formData.paymentPlan || []" border>
              <el-table-column prop="planName" label="付款节点" width="150" />
              <el-table-column prop="planAmount" label="计划金额" width="120">
                <template slot-scope="scope">
                  <span class="amount">¥{{ formatAmount(scope.row.planAmount) }}</span>
                </template>
              </el-table-column>
              <el-table-column prop="planDate" label="计划日期" width="120" />
              <el-table-column prop="actualAmount" label="实际金额" width="120">
                <template slot-scope="scope">
                  <span class="amount paid">¥{{ formatAmount(scope.row.actualAmount) }}</span>
                </template>
              </el-table-column>
              <el-table-column prop="actualDate" label="实际日期" width="120" />
              <el-table-column prop="statusName" label="状态" width="100">
                <template slot-scope="scope">
                  <el-tag :type="getPaymentStatusColor(scope.row.status)" size="small">
                    {{ scope.row.statusName }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column label="操作" width="150">
                <template slot-scope="scope">
                  <el-button 
                    size="mini" 
                    type="primary" 
                    @click="handlePayment(scope.row)"
                    v-if="scope.row.status === 'PENDING' || scope.row.status === 'PARTIAL_PAID'"
                  >
                    付款
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-tab-pane>

          <el-tab-pane label="履约记录" name="fulfillment" v-if="isViewMode">
            <el-table :data="formData.fulfillmentRecords || []" border>
              <el-table-column prop="recordTypeName" label="履约类型" width="120" />
              <el-table-column prop="recordDate" label="履约日期" width="120" />
              <el-table-column prop="recordAmount" label="履约金额" width="120">
                <template slot-scope="scope">
                  <span class="amount">¥{{ formatAmount(scope.row.recordAmount) }}</span>
                </template>
              </el-table-column>
              <el-table-column prop="recordDescription" label="履约描述" min-width="200" />
              <el-table-column prop="recordStatusName" label="状态" width="100">
                <template slot-scope="scope">
                  <el-tag :type="getFulfillmentStatusColor(scope.row.recordStatus)" size="small">
                    {{ scope.row.recordStatusName }}
                  </el-tag>
                </template>
              </el-table-column>
            </el-table>
            <div style="margin-top: 15px;" v-if="formData.status === 'EXECUTING'">
              <el-button type="primary" @click="handleAddFulfillment">添加履约记录</el-button>
            </div>
          </el-tab-pane>

          <el-tab-pane label="审批记录" name="approval" v-if="isViewMode">
            <el-timeline size="small">
              <el-timeline-item
                v-for="record in formData.approvalRecords"
                :key="record.recordId"
                :timestamp="formatDate(record.approvalTime)"
                :type="getApprovalType(record.approvalResult)"
              >
                <div class="approval-record">
                  <div class="approval-header">
                    <span class="approver">{{ record.approver }}</span>
                    <el-tag :type="getApprovalTagType(record.approvalResult)" size="mini">
                      {{ record.approvalResultName }}
                    </el-tag>
                  </div>
                  <div class="approval-comment" v-if="record.approvalComment">
                    {{ record.approvalComment }}
                  </div>
                </div>
              </el-timeline-item>
            </el-timeline>
          </el-tab-pane>
        </el-tabs>
      </el-form>

      <div slot="footer" class="dialog-footer" v-if="!isViewMode">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSave" :loading="saveLoading">保存</el-button>
        <el-button type="success" @click="handleSubmit" :loading="submitLoading">提交</el-button>
      </div>
    </el-dialog>

    <!-- 付款申请对话框 -->
    <el-dialog
      title="付款申请"
      :visible.sync="paymentDialogVisible"
      width="600px"
    >
      <el-form :model="paymentForm" :rules="paymentRules" ref="paymentFormRef" label-width="120px">
        <el-form-item label="付款节点" prop="planId">
          <el-select v-model="paymentForm.planId" style="width: 100%">
            <el-option 
              v-for="plan in paymentPlanOptions" 
              :key="plan.planId" 
              :label="plan.planName" 
              :value="plan.planId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="付款金额" prop="paymentAmount">
          <el-input-number 
            v-model="paymentForm.paymentAmount" 
            :min="0" 
            :precision="2" 
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="付款原因" prop="paymentReason">
          <el-input
            v-model="paymentForm.paymentReason"
            type="textarea"
            :rows="3"
            placeholder="请输入付款原因"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="paymentDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleConfirmPayment" :loading="paymentLoading">确认申请</el-button>
      </div>
    </el-dialog>

    <!-- 履约记录对话框 -->
    <el-dialog
      title="添加履约记录"
      :visible.sync="fulfillmentDialogVisible"
      width="600px"
    >
      <el-form :model="fulfillmentForm" :rules="fulfillmentRules" ref="fulfillmentFormRef" label-width="120px">
        <el-form-item label="履约类型" prop="fulfillType">
          <el-select v-model="fulfillmentForm.fulfillType" style="width: 100%">
            <el-option label="交货" value="DELIVERY" />
            <el-option label="验收" value="ACCEPTANCE" />
            <el-option label="服务" value="SERVICE" />
            <el-option label="其他" value="OTHER" />
          </el-select>
        </el-form-item>
        <el-form-item label="履约金额" prop="fulfillAmount">
          <el-input-number 
            v-model="fulfillmentForm.fulfillAmount" 
            :min="0" 
            :precision="2" 
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="履约描述" prop="fulfillDescription">
          <el-input
            v-model="fulfillmentForm.fulfillDescription"
            type="textarea"
            :rows="4"
            placeholder="请详细描述履约情况"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="fulfillmentDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleConfirmFulfillment" :loading="fulfillmentLoading">确认添加</el-button>
      </div>
    </el-dialog>

    <!-- 统计信息卡片 -->
    <div class="statistics-container" style="margin-top: 20px;">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-card class="stat-card">
            <div class="stat-item">
              <div class="stat-value">{{ statistics.totalContracts || 0 }}</div>
              <div class="stat-label">总合同数</div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="stat-card">
            <div class="stat-item">
              <div class="stat-value">¥{{ formatAmount(statistics.totalAmount || 0) }}</div>
              <div class="stat-label">合同总额</div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="stat-card">
            <div class="stat-item">
              <div class="stat-value">¥{{ formatAmount(statistics.totalPaidAmount || 0) }}</div>
              <div class="stat-label">已付金额</div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="stat-card">
            <div class="stat-item">
              <div class="stat-value">{{ statistics.executingContracts || 0 }}</div>
              <div class="stat-label">执行中</div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
import { contractApi } from '@/api/financialSharing/advancedFeatures'
import { getSupplierDropdownList, getContractDropdownList } from '@/api/financialSharing/common'

export default {
  name: 'Contract',
  data() {
    return {
      loading: false,
      saveLoading: false,
      submitLoading: false,
      paymentLoading: false,
      fulfillmentLoading: false,
      tableData: [],
      multipleSelection: [],
      statistics: {},
      supplierList: [],
      searchForm: {
        contractNumber: '',
        contractName: '',
        contractType: '',
        status: '',
        supplier: '',
        dateRange: []
      },
      pagination: {
        currentPage: 1,
        pageSize: 15,
        total: 0
      },
      dialogVisible: false,
      dialogTitle: '新增合同',
      isViewMode: false,
      activeTab: 'basic',
      formData: {
        contractId: null,
        contractNumber: '',
        contractName: '',
        contractType: '',
        supplierId: '',
        contractAmount: 0,
        signDate: '',
        startDate: '',
        endDate: '',
        contractDescription: '',
        paymentTerms: '',
        deliveryTerms: '',
        qualityTerms: '',
        paymentPlan: [],
        fulfillmentRecords: [],
        approvalRecords: []
      },
      formRules: {
        contractName: [
          { required: true, message: '请输入合同名称', trigger: 'blur' }
        ],
        contractType: [
          { required: true, message: '请选择合同类型', trigger: 'change' }
        ],
        supplierId: [
          { required: true, message: '请选择供应商', trigger: 'change' }
        ],
        contractAmount: [
          { required: true, message: '请输入合同金额', trigger: 'blur' }
        ],
        signDate: [
          { required: true, message: '请选择签署日期', trigger: 'change' }
        ]
      },
      paymentDialogVisible: false,
      paymentForm: {
        contractId: '',
        planId: '',
        paymentAmount: 0,
        paymentReason: ''
      },
      paymentRules: {
        planId: [
          { required: true, message: '请选择付款节点', trigger: 'change' }
        ],
        paymentAmount: [
          { required: true, message: '请输入付款金额', trigger: 'blur' }
        ],
        paymentReason: [
          { required: true, message: '请输入付款原因', trigger: 'blur' }
        ]
      },
      paymentPlanOptions: [],
      fulfillmentDialogVisible: false,
      fulfillmentForm: {
        contractId: '',
        fulfillType: '',
        fulfillAmount: 0,
        fulfillDescription: ''
      },
      fulfillmentRules: {
        fulfillType: [
          { required: true, message: '请选择履约类型', trigger: 'change' }
        ],
        fulfillAmount: [
          { required: true, message: '请输入履约金额', trigger: 'blur' }
        ],
        fulfillDescription: [
          { required: true, message: '请输入履约描述', trigger: 'blur' }
        ]
      },
      currentContractRow: null
    }
  },
  mounted() {
    this.loadData()
    this.loadStatistics()
    this.loadDropdownData()
  },
  methods: {
    async loadDropdownData() {
      try {
        const res = await getSupplierDropdownList()
        if (res.code === 1 && res.data) {
          this.supplierList = res.data
        }
      } catch (error) {
        console.error('加载供应商下拉数据失败:', error)
      }
    },
    async loadData() {
      this.loading = true
      try {
        const params = {
          page: this.pagination.currentPage - 1,
          size: this.pagination.pageSize,
          ...this.searchForm
        }
        if (this.searchForm.dateRange && this.searchForm.dateRange.length === 2) {
          params.startDate = this.searchForm.dateRange[0]
          params.endDate = this.searchForm.dateRange[1]
        }
        const response = await contractApi.getList(params)
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

    async loadStatistics() {
      try {
        const response = await contractApi.getStatistics()
        if (response.code === 1) {
          this.statistics = response.data
        }
      } catch (error) {
        console.error('加载统计数据失败：', error)
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
      this.dialogTitle = '新增合同'
      this.isViewMode = false
      this.activeTab = 'basic'
      this.formData = {
        contractId: null,
        contractNumber: '',
        contractName: '',
        contractType: '',
        supplierId: '',
        contractAmount: 0,
        signDate: '',
        startDate: '',
        endDate: '',
        contractDescription: '',
        paymentTerms: '',
        deliveryTerms: '',
        qualityTerms: '',
        paymentPlan: [],
        fulfillmentRecords: [],
        approvalRecords: []
      }
      this.dialogVisible = true
    },

    async handleView(row) {
      this.dialogTitle = '查看合同'
      this.isViewMode = true
      this.activeTab = 'basic'
      try {
        const response = await contractApi.getDetail(row.contractId)
        if (response.code === 1) {
          this.formData = response.data
          this.dialogVisible = true
        } else {
          this.$message.error(response.msg || '获取详情失败')
        }
      } catch (error) {
        this.$message.error('获取详情失败：' + error.message)
      }
    },

    handleEdit(row) {
      this.dialogTitle = '编辑合同'
      this.isViewMode = false
      this.activeTab = 'basic'
      this.formData = { ...row }
      this.dialogVisible = true
    },

    async handleSave() {
      try {
        await this.$refs.formRef.validate()
        this.saveLoading = true
        const response = await contractApi.save(this.formData)
        if (response.code === 1) {
          this.$message.success('保存成功')
          this.dialogVisible = false
          this.loadData()
          this.loadStatistics()
        } else {
          this.$message.error(response.msg || '保存失败')
        }
      } catch (error) {
        this.$message.error('保存失败：' + error.message)
      } finally {
        this.saveLoading = false
      }
    },

    async handleSubmit() {
      try {
        await this.$refs.formRef.validate()
        this.submitLoading = true
        const response = await contractApi.submit(this.formData.contractId, {
          submitComment: '提交合同审批'
        })
        if (response.code === 1) {
          this.$message.success('提交成功')
          this.dialogVisible = false
          this.loadData()
        } else {
          this.$message.error(response.msg || '提交失败')
        }
      } catch (error) {
        this.$message.error('提交失败：' + error.message)
      } finally {
        this.submitLoading = false
      }
    },

    async handlePaymentRequest(row) {
      this.currentContractRow = row
      this.paymentForm = {
        contractId: row.contractId,
        planId: '',
        paymentAmount: 0,
        paymentReason: ''
      }
      // 获取付款计划选项 - 从后端API获取
      try {
        const res = await contractApi.getPaymentPlans(row.contractId)
        if (res.code === 1 && res.data) {
          this.paymentPlanOptions = res.data
        } else {
          this.paymentPlanOptions = []
        }
      } catch (error) {
        console.error('获取付款计划失败:', error)
        this.paymentPlanOptions = []
      }
      this.paymentDialogVisible = true
    },

    async handleConfirmPayment() {
      try {
        await this.$refs.paymentFormRef.validate()
        this.paymentLoading = true
        const response = await contractApi.paymentRequest(this.paymentForm.contractId, this.paymentForm)
        if (response.code === 1) {
          this.$message.success('付款申请成功')
          this.paymentDialogVisible = false
          this.loadData()
        } else {
          this.$message.error(response.msg || '付款申请失败')
        }
      } catch (error) {
        this.$message.error('付款申请失败：' + error.message)
      } finally {
        this.paymentLoading = false
      }
    },

    handleAddFulfillment() {
      this.fulfillmentForm = {
        contractId: this.formData.contractId,
        fulfillType: '',
        fulfillAmount: 0,
        fulfillDescription: ''
      }
      this.fulfillmentDialogVisible = true
    },

    async handleConfirmFulfillment() {
      try {
        await this.$refs.fulfillmentFormRef.validate()
        this.fulfillmentLoading = true
        const response = await contractApi.fulfill(this.fulfillmentForm.contractId, this.fulfillmentForm)
        if (response.code === 1) {
          this.$message.success('履约记录添加成功')
          this.fulfillmentDialogVisible = false
          // 刷新合同详情
          this.handleView(this.formData)
        } else {
          this.$message.error(response.msg || '履约记录添加失败')
        }
      } catch (error) {
        this.$message.error('履约记录添加失败：' + error.message)
      } finally {
        this.fulfillmentLoading = false
      }
    },

    formatAmount(amount) {
      if (!amount) return '0.00'
      return Number(amount).toLocaleString('zh-CN', {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
      })
    },

    formatDate(dateStr) {
      if (!dateStr) return ''
      const date = new Date(dateStr)
      return date.toLocaleString('zh-CN')
    },

    getContractTypeColor(contractType) {
      const colors = {
        'PROCUREMENT': 'primary',
        'SERVICE': 'success',
        'LEASE': 'warning',
        'CONSULTING': 'info',
        'OTHER': 'default'
      }
      return colors[contractType] || 'default'
    },

    getStatusColor(status) {
      const colors = {
        'DRAFT': 'info',
        'PENDING': 'warning',
        'APPROVED': 'success',
        'EXECUTING': 'primary',
        'COMPLETED': 'success',
        'TERMINATED': 'danger',
        'REJECTED': 'danger'
      }
      return colors[status] || 'default'
    },

    getProgressColor(percentage) {
      if (percentage >= 80) return '#67c23a'
      if (percentage >= 50) return '#e6a23c'
      return '#f56c6c'
    },

    getPaymentStatusColor(status) {
      const colors = {
        'PENDING': 'warning',
        'PARTIAL_PAID': 'primary',
        'PAID': 'success'
      }
      return colors[status] || 'default'
    },

    getFulfillmentStatusColor(status) {
      const colors = {
        'PENDING': 'warning',
        'COMPLETED': 'success',
        'FAILED': 'danger'
      }
      return colors[status] || 'default'
    },

    getApprovalType(result) {
      return result === 'APPROVED' ? 'success' : result === 'REJECTED' ? 'danger' : 'primary'
    },

    getApprovalTagType(result) {
      return result === 'APPROVED' ? 'success' : result === 'REJECTED' ? 'danger' : 'primary'
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

    handleDialogClose() {
      if (this.$refs.formRef) {
        this.$refs.formRef.resetFields()
      }
    },

    async handleDelete(row) {
      try {
        await this.$confirm('确定要删除该合同吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        const response = await contractApi.delete(row.contractId)
        if (response.code === 1) {
          this.$message.success('删除成功')
          this.loadData()
          this.loadStatistics()
        } else {
          this.$message.error(response.msg || '删除失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('删除失败：' + error.message)
        }
      }
    },

    handleApprove(row) {
      // 跳转到审批页面
      this.$router.push({
        path: '/financialSharing/contract/approve',
        query: { id: row.contractId }
      })
    },

    handleFulfill(row) {
      // 跳转到履约管理页面
      this.$router.push({
        path: '/financialSharing/contract/fulfill',
        query: { id: row.contractId }
      })
    },

    handleContractAnalysis() {
      // 跳转到合同分析页面
      this.$router.push({
        path: '/financialSharing/contract/analysis'
      })
    },

    handleBatchApprove() {
      // 批量审批
      const contractIds = this.multipleSelection.map(item => item.contractId)
      this.$router.push({
        path: '/financialSharing/batch/approve',
        query: { type: 'contract', ids: contractIds.join(',') }
      })
    },

    handleBatchDelete() {
      this.$confirm('确定要删除选中的合同吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        const contractIds = this.multipleSelection.map(item => item.contractId)
        const response = await contractApi.batchDelete(contractIds)
        if (response.code === 1) {
          this.$message.success('批量删除成功')
          this.loadData()
          this.loadStatistics()
        } else {
          this.$message.error(response.msg || '批量删除失败')
        }
      }).catch(() => {})
    },

    handleExport() {
      const params = { ...this.searchForm }
      if (this.searchForm.dateRange && this.searchForm.dateRange.length === 2) {
        params.startDate = this.searchForm.dateRange[0]
        params.endDate = this.searchForm.dateRange[1]
      }
      contractApi.export(params).then(response => {
        if (response.code === 1) {
          this.$message.success('导出成功')
        } else {
          this.$message.error(response.msg || '导出失败')
        }
      })
    }
  }
}
</script>

<style scoped>
.contract-container {
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

.amount {
  font-weight: bold;
  color: #E6A23C;
}

.amount.paid {
  color: #67C23A;
}

.amount.remaining {
  color: #F56C6C;
}

.approval-record {
  padding: 5px 0;
}

.approval-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 5px;
}

.approver {
  font-weight: bold;
  color: #303133;
}

.approval-comment {
  color: #606266;
  font-size: 14px;
  line-height: 1.5;
}

.statistics-container {
  margin-top: 20px;
}

.stat-card {
  text-align: center;
}

.stat-item {
  padding: 20px;
}

.stat-value {
  font-size: 24px;
  font-weight: bold;
  color: #409eff;
  margin-bottom: 8px;
}

.stat-label {
  font-size: 14px;
  color: #606266;
}

.dialog-footer {
  text-align: right;
}
</style>
