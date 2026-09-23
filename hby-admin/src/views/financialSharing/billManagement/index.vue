<template>
  <div class="bill-management-container">
    <div class="search-container">
      <el-form :model="searchForm" ref="searchForm" :inline="true" class="search-form">
        <el-form-item label="账单号码" prop="billNumber">
          <el-input
            v-model="searchForm.billNumber"
            placeholder="请输入账单号码"
            clearable
            style="width: 180px"
          />
        </el-form-item>
        <el-form-item label="账单类型" prop="billType">
          <el-select
            v-model="searchForm.billType"
            placeholder="请选择账单类型"
            clearable
            style="width: 150px"
          >
            <el-option label="发票" value="INVOICE" />
            <el-option label="收据" value="RECEIPT" />
            <el-option label="其他" value="OTHER" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select
            v-model="searchForm.status"
            placeholder="请选择状态"
            clearable
            style="width: 120px"
          >
            <el-option label="已采集" value="COLLECTED" />
            <el-option label="识别中" value="OCR_PROCESSING" />
            <el-option label="识别成功" value="OCR_SUCCESS" />
            <el-option label="识别失败" value="OCR_FAILED" />
            <el-option label="稽核中" value="AUDITING" />
            <el-option label="稽核通过" value="AUDIT_PASSED" />
            <el-option label="稽核失败" value="AUDIT_FAILED" />
            <el-option label="已应用" value="APPLIED" />
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
        <el-form-item label="账单日期" prop="dateRange">
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
      <el-upload
        class="upload-demo"
        :action="uploadUrl"
        :on-success="handleUploadSuccess"
        :on-error="handleUploadError"
        :before-upload="beforeUpload"
        :show-file-list="false"
        accept="image/*,.pdf"
        style="display: inline-block; margin-right: 10px;"
      >
        <el-button type="primary" icon="el-icon-upload">账单采集</el-button>
      </el-upload>
      <el-button type="success" @click="handleBatchOCR" :disabled="!multipleSelection.length">
        批量OCR识别
      </el-button>
      <el-button type="info" @click="handleBatchAudit" :disabled="!multipleSelection.length">
        批量智能稽核
      </el-button>
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
        <el-table-column prop="billNumber" label="账单号码" width="180" />
        <el-table-column prop="billTypeName" label="账单类型" width="100">
          <template slot-scope="scope">
            <el-tag :type="getBillTypeColor(scope.row.billType)" size="small">
              {{ scope.row.billTypeName }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="supplierName" label="供应商" min-width="150" show-overflow-tooltip />
        <el-table-column prop="billAmount" label="账单金额" width="120">
          <template slot-scope="scope">
            <span class="amount">¥{{ formatAmount(scope.row.billAmount) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="billDate" label="账单日期" width="120" />
        <el-table-column prop="statusName" label="状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="getStatusColor(scope.row.status)" size="small">
              {{ scope.row.statusName }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="ocrStatusName" label="OCR状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="getOCRStatusColor(scope.row.ocrStatus)" size="small">
              {{ scope.row.ocrStatusName }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="auditStatusName" label="稽核状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="getAuditStatusColor(scope.row.auditStatus)" size="small">
              {{ scope.row.auditStatusName }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="collectMethodName" label="采集方式" width="100" />
        <el-table-column prop="collectTime" label="采集时间" width="160">
          <template slot-scope="scope">
            {{ formatDate(scope.row.collectTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="300" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" @click="handleView(scope.row)">查看</el-button>
            <el-button 
              size="mini" 
              type="primary" 
              @click="handleOCR(scope.row)"
              v-if="scope.row.ocrStatus === 'PENDING' || scope.row.ocrStatus === 'FAILED'"
            >
              OCR识别
            </el-button>
            <el-button 
              size="mini" 
              type="success" 
              @click="handleAudit(scope.row)"
              v-if="scope.row.ocrStatus === 'SUCCESS' && scope.row.auditStatus === 'PENDING'"
            >
              智能稽核
            </el-button>
            <el-button 
              size="mini" 
              type="info" 
              @click="handleApply(scope.row)"
              v-if="scope.row.auditStatus === 'PASSED' && scope.row.status !== 'APPLIED'"
            >
              应用
            </el-button>
            <el-button 
              size="mini" 
              type="danger" 
              @click="handleDelete(scope.row)"
              v-if="scope.row.status === 'COLLECTED'"
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

    <!-- 账单详情对话框 -->
    <el-dialog
      title="账单详情"
      :visible.sync="detailDialogVisible"
      width="900px"
      @close="handleDetailDialogClose"
    >
      <div class="bill-detail" v-if="billDetail">
        <el-row :gutter="20">
          <el-col :span="12">
            <div class="bill-image">
              <h4>账单图片</h4>
              <img :src="billDetail.imageUrl" alt="账单图片" style="max-width: 100%; max-height: 400px;" />
            </div>
          </el-col>
          <el-col :span="12">
            <div class="bill-info">
              <h4>基本信息</h4>
              <el-descriptions :column="1" border>
                <el-descriptions-item label="账单号码">{{ billDetail.billNumber }}</el-descriptions-item>
                <el-descriptions-item label="账单类型">{{ billDetail.billTypeName }}</el-descriptions-item>
                <el-descriptions-item label="供应商">{{ billDetail.supplierName }}</el-descriptions-item>
                <el-descriptions-item label="税号">{{ billDetail.supplierTaxNumber }}</el-descriptions-item>
                <el-descriptions-item label="账单金额">¥{{ formatAmount(billDetail.billAmount) }}</el-descriptions-item>
                <el-descriptions-item label="税额">¥{{ formatAmount(billDetail.taxAmount) }}</el-descriptions-item>
                <el-descriptions-item label="账单日期">{{ billDetail.billDate }}</el-descriptions-item>
                <el-descriptions-item label="状态">
                  <el-tag :type="getStatusColor(billDetail.status)" size="small">
                    {{ billDetail.statusName }}
                  </el-tag>
                </el-descriptions-item>
                <el-descriptions-item label="采集时间">{{ formatDate(billDetail.collectTime) }}</el-descriptions-item>
                <el-descriptions-item label="采集方式">{{ billDetail.collectMethodName }}</el-descriptions-item>
              </el-descriptions>
            </div>
          </el-col>
        </el-row>

        <!-- OCR识别结果 -->
        <div class="ocr-result" v-if="billDetail.ocrResult && billDetail.ocrResult.status === 'SUCCESS'">
          <h4>OCR识别结果</h4>
          <el-descriptions :column="2" border>
            <el-descriptions-item label="识别置信度">{{ (billDetail.ocrResult.confidence * 100).toFixed(1) }}%</el-descriptions-item>
            <el-descriptions-item label="识别状态">
              <el-tag type="success" size="small">识别成功</el-tag>
            </el-descriptions-item>
          </el-descriptions>
          <div class="recognized-text" style="margin-top: 10px;">
            <strong>识别文本：</strong>
            <p>{{ billDetail.ocrResult.recognizedText }}</p>
          </div>
        </div>

        <!-- 稽核结果 -->
        <div class="audit-result" v-if="billDetail.auditResult && billDetail.auditResult.status !== 'PENDING'">
          <h4>稽核结果</h4>
          <el-descriptions :column="2" border>
            <el-descriptions-item label="稽核状态">
              <el-tag :type="getAuditStatusColor(billDetail.auditResult.status)" size="small">
                {{ billDetail.auditResult.status === 'PASSED' ? '稽核通过' : '稽核失败' }}
              </el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="稽核评分">{{ billDetail.auditResult.score }}分</el-descriptions-item>
          </el-descriptions>
          <div class="audit-message" style="margin-top: 10px;">
            <strong>稽核说明：</strong>
            <p>{{ billDetail.auditResult.message }}</p>
          </div>
        </div>
      </div>
    </el-dialog>

    <!-- 账单应用对话框 -->
    <el-dialog
      title="账单应用"
      :visible.sync="applyDialogVisible"
      width="600px"
    >
      <el-form :model="applyForm" :rules="applyRules" ref="applyFormRef" label-width="120px">
        <el-form-item label="应用类型" prop="targetType">
          <el-select v-model="applyForm.targetType" style="width: 100%">
            <el-option label="报销单" value="EXPENSE_REPORT" />
            <el-option label="预付款" value="PREPAYMENT" />
            <el-option label="借款单" value="LOAN" />
            <el-option label="预提单" value="PROVISION" />
          </el-select>
        </el-form-item>
        <el-form-item label="目标单据" prop="targetId">
          <el-select v-model="applyForm.targetId" style="width: 100%" filterable>
            <el-option 
              v-for="doc in targetDocuments" 
              :key="doc.id" 
              :label="doc.title" 
              :value="doc.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="应用金额" prop="applyAmount">
          <el-input-number 
            v-model="applyForm.applyAmount" 
            :min="0" 
            :precision="2" 
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="应用说明" prop="applyRemark">
          <el-input
            v-model="applyForm.applyRemark"
            type="textarea"
            :rows="3"
            placeholder="请输入应用说明"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="applyDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleConfirmApply" :loading="applyLoading">确认应用</el-button>
      </div>
    </el-dialog>

    <!-- 统计信息卡片 -->
    <div class="statistics-container" style="margin-top: 20px;">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-card class="stat-card">
            <div class="stat-item">
              <div class="stat-value">{{ statistics.totalCount || 0 }}</div>
              <div class="stat-label">总账单数</div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="stat-card">
            <div class="stat-item">
              <div class="stat-value">¥{{ formatAmount(statistics.totalAmount || 0) }}</div>
              <div class="stat-label">总金额</div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="stat-card">
            <div class="stat-item">
              <div class="stat-value">{{ statistics.ocrSuccessCount || 0 }}</div>
              <div class="stat-label">OCR成功</div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="stat-card">
            <div class="stat-item">
              <div class="stat-value">{{ statistics.appliedCount || 0 }}</div>
              <div class="stat-label">已应用</div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
import { billManagementApi } from '@/api/financialSharing/advancedFeatures'
import { expenseReportApi, prepaymentApi, loanApi, provisionApi } from '@/api/financialSharing/coreBusiness'

export default {
  name: 'BillManagement',
  data() {
    return {
      loading: false,
      applyLoading: false,
      tableData: [],
      multipleSelection: [],
      statistics: {},
      uploadUrl: process.env.VUE_APP_BASE_API + '/cwgxAi/bill/management/collect',
      searchForm: {
        billNumber: '',
        billType: '',
        status: '',
        supplier: '',
        dateRange: []
      },
      pagination: {
        currentPage: 1,
        pageSize: 15,
        total: 0
      },
      detailDialogVisible: false,
      billDetail: null,
      applyDialogVisible: false,
      applyForm: {
        billId: '',
        targetType: '',
        targetId: '',
        applyAmount: 0,
        applyRemark: ''
      },
      applyRules: {
        targetType: [
          { required: true, message: '请选择应用类型', trigger: 'change' }
        ],
        targetId: [
          { required: true, message: '请选择目标单据', trigger: 'change' }
        ],
        applyAmount: [
          { required: true, message: '请输入应用金额', trigger: 'blur' }
        ]
      },
      targetDocuments: [],
      currentBillRow: null
    }
  },
  mounted() {
    this.loadData()
    this.loadStatistics()
  },
  watch: {
    'applyForm.targetType'(newVal) {
      this.applyForm.targetId = ''
      this.loadTargetDocuments(newVal)
    }
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
        if (this.searchForm.dateRange && this.searchForm.dateRange.length === 2) {
          params.startDate = this.searchForm.dateRange[0]
          params.endDate = this.searchForm.dateRange[1]
        }
        const response = await billManagementApi.getList(params)
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
        const response = await billManagementApi.getStatistics()
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

    async handleView(row) {
      try {
        const response = await billManagementApi.getDetail(row.billId)
        if (response.code === 1) {
          this.billDetail = response.data
          this.detailDialogVisible = true
        } else {
          this.$message.error(response.msg || '获取详情失败')
        }
      } catch (error) {
        this.$message.error('获取详情失败：' + error.message)
      }
    },

    async handleOCR(row) {
      try {
        this.$message.info('正在进行OCR识别，请稍候...')
        const response = await billManagementApi.ocrRecognition(row.billId, {
          ocrType: 'AUTO',
          enhanceImage: true
        })
        if (response.code === 1) {
          this.$message.success('OCR识别成功')
          this.loadData()
        } else {
          this.$message.error(response.msg || 'OCR识别失败')
        }
      } catch (error) {
        this.$message.error('OCR识别失败：' + error.message)
      }
    },

    async handleAudit(row) {
      try {
        this.$message.info('正在进行智能稽核，请稍候...')
        const response = await billManagementApi.intelligentAudit(row.billId, {
          auditLevel: 'STANDARD',
          enableRiskCheck: true
        })
        if (response.code === 1) {
          this.$message.success('智能稽核完成')
          this.loadData()
        } else {
          this.$message.error(response.msg || '智能稽核失败')
        }
      } catch (error) {
        this.$message.error('智能稽核失败：' + error.message)
      }
    },

    handleApply(row) {
      this.currentBillRow = row
      this.applyForm = {
        billId: row.billId,
        targetType: '',
        targetId: '',
        applyAmount: row.billAmount,
        applyRemark: ''
      }
      this.targetDocuments = []
      this.applyDialogVisible = true
    },

    // 根据应用类型动态加载目标单据
    async loadTargetDocuments(targetType) {
      if (!targetType) {
        this.targetDocuments = []
        return
      }
      try {
        const params = { pageNum: 0, size: 100 }
        let res
        switch (targetType) {
          case 'EXPENSE_REPORT':
            res = await expenseReportApi.getList(params)
            break
          case 'PREPAYMENT':
            res = await prepaymentApi.getList(params)
            break
          case 'LOAN':
            res = await loanApi.getList(params)
            break
          case 'PROVISION':
            res = await provisionApi.getList(params)
            break
          default:
            this.targetDocuments = []
            return
        }
        if (res.code === 1 && res.data) {
          const list = res.data.tlist || []
          this.targetDocuments = list.map(item => ({
            id: item.reportId || item.prepaymentId || item.loanId || item.provisionId,
            title: item.reportNumber || item.prepaymentNumber || item.loanNumber || item.provisionNumber ||
                   (item.reportTitle || item.prepaymentTitle || item.loanTitle || item.provisionTitle || '')
          }))
        } else {
          this.targetDocuments = []
        }
      } catch (error) {
        console.error('加载目标单据失败:', error)
        this.targetDocuments = []
      }
    },

    async handleConfirmApply() {
      try {
        await this.$refs.applyFormRef.validate()
        this.applyLoading = true
        const response = await billManagementApi.applyBill(this.applyForm.billId, this.applyForm)
        if (response.code === 1) {
          this.$message.success('账单应用成功')
          this.applyDialogVisible = false
          this.loadData()
        } else {
          this.$message.error(response.msg || '账单应用失败')
        }
      } catch (error) {
        this.$message.error('账单应用失败：' + error.message)
      } finally {
        this.applyLoading = false
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

    getBillTypeColor(billType) {
      const colors = {
        'INVOICE': 'primary',
        'RECEIPT': 'success',
        'OTHER': 'info'
      }
      return colors[billType] || 'default'
    },

    getStatusColor(status) {
      const colors = {
        'COLLECTED': 'info',
        'OCR_PROCESSING': 'warning',
        'OCR_SUCCESS': 'success',
        'OCR_FAILED': 'danger',
        'AUDITING': 'warning',
        'AUDIT_PASSED': 'success',
        'AUDIT_FAILED': 'danger',
        'APPLIED': 'primary'
      }
      return colors[status] || 'default'
    },

    getOCRStatusColor(status) {
      const colors = {
        'PENDING': 'info',
        'PROCESSING': 'warning',
        'SUCCESS': 'success',
        'FAILED': 'danger'
      }
      return colors[status] || 'default'
    },

    getAuditStatusColor(status) {
      const colors = {
        'PENDING': 'info',
        'PROCESSING': 'warning',
        'PASSED': 'success',
        'FAILED': 'danger'
      }
      return colors[status] || 'default'
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

    handleDetailDialogClose() {
      this.billDetail = null
    },

    beforeUpload(file) {
      const isValidType = file.type.startsWith('image/') || file.type === 'application/pdf'
      const isLt10M = file.size / 1024 / 1024 < 10

      if (!isValidType) {
        this.$message.error('只能上传图片或PDF文件!')
        return false
      }
      if (!isLt10M) {
        this.$message.error('上传文件大小不能超过 10MB!')
        return false
      }
      return true
    },

    handleUploadSuccess(response) {
      if (response.code === 1) {
        this.$message.success('账单采集成功')
        this.loadData()
        this.loadStatistics()
      } else {
        this.$message.error(response.msg || '账单采集失败')
      }
    },

    handleUploadError(error) {
      this.$message.error('账单采集失败：' + error.message)
    },

    async handleDelete(row) {
      try {
        await this.$confirm('确定要删除该账单吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        const response = await billManagementApi.delete(row.billId)
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

    handleBatchOCR() {
      const billIds = this.multipleSelection.map(item => item.billId)
      this.$message.info('正在批量进行OCR识别，请稍候...')
      billManagementApi.batchProcess({
        operation: 'OCR',
        billIds: billIds
      }).then(response => {
        if (response.code === 1) {
          this.$message.success('批量OCR识别完成')
          this.loadData()
        } else {
          this.$message.error(response.msg || '批量OCR识别失败')
        }
      })
    },

    handleBatchAudit() {
      const billIds = this.multipleSelection.map(item => item.billId)
      this.$message.info('正在批量进行智能稽核，请稍候...')
      billManagementApi.batchProcess({
        operation: 'AUDIT',
        billIds: billIds
      }).then(response => {
        if (response.code === 1) {
          this.$message.success('批量智能稽核完成')
          this.loadData()
        } else {
          this.$message.error(response.msg || '批量智能稽核失败')
        }
      })
    },

    handleBatchDelete() {
      this.$confirm('确定要删除选中的账单吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const billIds = this.multipleSelection.map(item => item.billId)
        billManagementApi.batchProcess({
          operation: 'DELETE',
          billIds: billIds
        }).then(response => {
          if (response.code === 1) {
            this.$message.success('批量删除成功')
            this.loadData()
            this.loadStatistics()
          } else {
            this.$message.error(response.msg || '批量删除失败')
          }
        })
      }).catch(() => {})
    },

    handleExport() {
      const params = { ...this.searchForm }
      if (this.searchForm.dateRange && this.searchForm.dateRange.length === 2) {
        params.startDate = this.searchForm.dateRange[0]
        params.endDate = this.searchForm.dateRange[1]
      }
      billManagementApi.export(params).then(response => {
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
.bill-management-container {
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

.bill-detail {
  padding: 10px 0;
}

.bill-image img {
  border: 1px solid #ddd;
  border-radius: 4px;
}

.bill-info h4,
.ocr-result h4,
.audit-result h4 {
  margin: 20px 0 10px 0;
  color: #303133;
  font-size: 16px;
}

.recognized-text,
.audit-message {
  background: #f5f7fa;
  padding: 10px;
  border-radius: 4px;
  border-left: 4px solid #409eff;
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
