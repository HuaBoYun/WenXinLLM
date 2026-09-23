<template>
  <div class="prepayment-container">
    <div class="search-container">
      <el-form :model="searchForm" ref="searchForm" :inline="true" class="search-form">
        <el-form-item label="预付款单号" prop="prepaymentNumber">
          <el-input
            v-model="searchForm.prepaymentNumber"
            placeholder="请输入预付款单号"
            clearable
            style="width: 180px"
          />
        </el-form-item>
        <el-form-item label="申请人" prop="applicant">
          <el-input
            v-model="searchForm.applicant"
            placeholder="请输入申请人"
            clearable
            style="width: 150px"
          />
        </el-form-item>
        <el-form-item label="供应商" prop="supplierName">
          <el-input
            v-model="searchForm.supplierName"
            placeholder="请输入供应商名称"
            clearable
            style="width: 150px"
          />
        </el-form-item>
        <el-form-item label="预付款类型" prop="prepaymentType">
          <el-select
            v-model="searchForm.prepaymentType"
            placeholder="请选择预付款类型"
            clearable
            style="width: 150px"
          >
            <el-option label="采购预付款" value="PROCUREMENT" />
            <el-option label="服务预付款" value="SERVICE" />
            <el-option label="租金预付款" value="RENT" />
            <el-option label="工程预付款" value="PROJECT" />
            <el-option label="其他预付款" value="OTHER" />
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
            <el-option label="已支付" value="PAID" />
            <el-option label="部分核销" value="PARTIAL_WRITEOFF" />
            <el-option label="已核销" value="WRITEOFF" />
            <el-option label="已拒绝" value="REJECTED" />
          </el-select>
        </el-form-item>
        <el-form-item label="申请日期" prop="dateRange">
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
      <el-button type="primary" @click="handleAdd">新增预付款</el-button>
      <el-button type="success" @click="handleBatchApprove" :disabled="!multipleSelection.length">
        批量审批
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
        <el-table-column prop="prepaymentNumber" label="预付款单号" width="180" />
        <el-table-column prop="prepaymentTitle" label="预付款标题" min-width="200" show-overflow-tooltip />
        <el-table-column prop="applicant" label="申请人" width="100" />
        <el-table-column prop="departmentName" label="部门" width="120" />
        <el-table-column prop="supplierName" label="供应商" width="150" show-overflow-tooltip />
        <el-table-column prop="prepaymentTypeName" label="预付款类型" width="120">
          <template slot-scope="scope">
            <el-tag :type="getPrepaymentTypeColor(scope.row.prepaymentType)" size="small">
              {{ scope.row.prepaymentTypeName }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="prepaymentAmount" label="预付款金额" width="120">
          <template slot-scope="scope">
            <span class="amount">¥{{ formatAmount(scope.row.prepaymentAmount) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="writeOffAmount" label="已核销金额" width="120">
          <template slot-scope="scope">
            <span class="amount writeoff">¥{{ formatAmount(scope.row.writeOffAmount) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="remainingAmount" label="待核销金额" width="120">
          <template slot-scope="scope">
            <span class="amount remaining">¥{{ formatAmount(scope.row.remainingAmount) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="getStatusColor(scope.row.status)" size="small">
              {{ scope.row.statusName }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="submitTime" label="提交时间" width="160">
          <template slot-scope="scope">
            {{ formatDate(scope.row.submitTime) }}
          </template>
        </el-table-column>
        <el-table-column prop="paymentTime" label="支付时间" width="160">
          <template slot-scope="scope">
            {{ formatDate(scope.row.paymentTime) }}
          </template>
        </el-table-column>
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
              type="warning"
              @click="handleWriteOff(scope.row)"
              v-if="scope.row.status === 'PAID' && scope.row.remainingAmount > 0"
            >
              核销
            </el-button>
            <el-button
              size="mini"
              type="info"
              @click="handleRefund(scope.row)"
              v-if="scope.row.status === 'PAID' && scope.row.remainingAmount > 0"
            >
              退款
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

    <!-- 预付款详情对话框 -->
    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogVisible"
      width="900px"
      @close="handleDialogClose"
    >
      <el-form :model="formData" :rules="formRules" ref="formRef" label-width="120px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="预付款单号" prop="prepaymentNumber">
              <el-input v-model="formData.prepaymentNumber" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="预付款标题" prop="prepaymentTitle">
              <el-input v-model="formData.prepaymentTitle" :disabled="isViewMode" placeholder="请输入预付款标题" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="预付款类型" prop="prepaymentType">
              <el-select v-model="formData.prepaymentType" :disabled="isViewMode" style="width: 100%" placeholder="请选择预付款类型">
                <el-option label="采购预付款" value="PROCUREMENT" />
                <el-option label="服务预付款" value="SERVICE" />
                <el-option label="租金预付款" value="RENT" />
                <el-option label="工程预付款" value="PROJECT" />
                <el-option label="其他预付款" value="OTHER" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="预付款金额" prop="prepaymentAmount">
              <el-input-number v-model="formData.prepaymentAmount" :disabled="isViewMode" :min="0" :precision="2" style="width: 100%" placeholder="请输入预付款金额" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="供应商" prop="supplierId">
              <el-select v-model="formData.supplierId" :disabled="isViewMode" filterable placeholder="请选择供应商" style="width: 100%">
                <el-option
                  v-for="item in supplierList"
                  :key="item.supplierId"
                  :label="item.supplierName"
                  :value="item.supplierId"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="关联合同" prop="contractId">
              <el-select v-model="formData.contractId" :disabled="isViewMode" filterable clearable placeholder="请选择关联合同" style="width: 100%" @change="handleContractChange">
                <el-option
                  v-for="item in contractList"
                  :key="item.contractId"
                  :label="item.contractNumber + '-' + item.contractName"
                  :value="item.contractId"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <!-- 合同付款信息 -->
        <el-row :gutter="20" v-if="contractInfo">
          <el-col :span="24">
            <div class="contract-info-panel">
              <div class="contract-info-title">合同付款信息</div>
              <el-row :gutter="20">
                <el-col :span="6">
                  <div class="info-item">
                    <span class="info-label">合同金额</span>
                    <span class="info-value">¥{{ formatAmount(contractInfo.contractAmount) }}</span>
                  </div>
                </el-col>
                <el-col :span="6">
                  <div class="info-item">
                    <span class="info-label">已付金额</span>
                    <span class="info-value paid">¥{{ formatAmount(contractInfo.paidAmount) }}</span>
                  </div>
                </el-col>
                <el-col :span="6">
                  <div class="info-item">
                    <span class="info-label">待付金额</span>
                    <span class="info-value remaining">¥{{ formatAmount(contractInfo.contractAmount - contractInfo.paidAmount) }}</span>
                  </div>
                </el-col>
                <el-col :span="6">
                  <div class="info-item">
                    <span class="info-label">付款进度</span>
                    <span class="info-value">{{ contractInfo.contractAmount > 0 ? ((contractInfo.paidAmount / contractInfo.contractAmount) * 100).toFixed(1) : 0 }}%</span>
                  </div>
                </el-col>
              </el-row>
            </div>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="预计付款日期" prop="expectedPaymentDate">
              <el-date-picker
                v-model="formData.expectedPaymentDate"
                :disabled="isViewMode"
                type="date"
                placeholder="请选择预计付款日期"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="紧急程度" prop="urgencyLevel">
              <el-select v-model="formData.urgencyLevel" :disabled="isViewMode" placeholder="请选择紧急程度" style="width: 100%">
                <el-option label="一般" value="NORMAL" />
                <el-option label="紧急" value="URGENT" />
                <el-option label="非常紧急" value="VERY_URGENT" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="24">
            <el-form-item label="预付款事由" prop="prepaymentReason">
              <el-input
                v-model="formData.prepaymentReason"
                :disabled="isViewMode"
                type="textarea"
                :rows="3"
                placeholder="请输入预付款事由"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="24">
            <el-form-item label="备注" prop="remark">
              <el-input
                v-model="formData.remark"
                :disabled="isViewMode"
                type="textarea"
                :rows="2"
                placeholder="请输入备注信息"
              />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSave" v-if="!isViewMode">保存</el-button>
        <el-button type="success" @click="handleSubmit" v-if="!isViewMode && formData.prepaymentId">提交审批</el-button>
      </div>
    </el-dialog>

    <!-- 核销对话框 -->
    <WriteOffDialog
      :visible.sync="writeOffDialogVisible"
      :prepayment-data="currentRow"
      @success="handleWriteOffSuccess"
    />

    <!-- 退款对话框 -->
    <RefundDialog
      :visible.sync="refundDialogVisible"
      :prepayment-data="currentRow"
      @success="handleRefundSuccess"
    />
  </div>
</template>

<script>
import { prepaymentApi } from '@/api/financialSharing/coreBusiness'
import { contractApi, budgetApi } from '@/api/financialSharing/advancedFeatures'
import { getSupplierDropdownList, getContractDropdownList } from '@/api/financialSharing/common'
import WriteOffDialog from './components/WriteOffDialog.vue'
import RefundDialog from './components/RefundDialog.vue'

export default {
  name: 'PrepaymentManagement',
  components: {
    WriteOffDialog,
    RefundDialog
  },
  data() {
    return {
      loading: false,
      tableData: [],
      multipleSelection: [],
      searchForm: {
        prepaymentNumber: '',
        applicant: '',
        supplierName: '',
        prepaymentType: '',
        status: '',
        dateRange: null
      },
      pagination: {
        currentPage: 1,
        pageSize: 10,
        total: 0
      },
      dialogVisible: false,
      dialogTitle: '',
      isViewMode: false,
      formData: {
        prepaymentId: '',
        prepaymentNumber: '',
        prepaymentTitle: '',
        prepaymentType: '',
        prepaymentAmount: 0,
        supplierId: '',
        contractId: '',
        expectedPaymentDate: '',
        urgencyLevel: 'NORMAL',
        prepaymentReason: '',
        remark: ''
      },
      formRules: {
        prepaymentTitle: [{ required: true, message: '请输入预付款标题', trigger: 'blur' }],
        prepaymentType: [{ required: true, message: '请选择预付款类型', trigger: 'change' }],
        prepaymentAmount: [{ required: true, message: '请输入预付款金额', trigger: 'blur' }],
        supplierId: [{ required: true, message: '请选择供应商', trigger: 'change' }],
        expectedPaymentDate: [{ required: true, message: '请选择预计付款日期', trigger: 'change' }],
        prepaymentReason: [{ required: true, message: '请输入预付款事由', trigger: 'blur' }]
      },
      supplierList: [],
      contractList: [],
      contractInfo: null,
      writeOffDialogVisible: false,
      refundDialogVisible: false,
      currentRow: null
    }
  },
  created() {
    this.loadData()
    this.loadSuppliers()
    this.loadContracts()
  },
  methods: {
    // 加载数据
    async loadData() {
      this.loading = true
      try {
        const params = {
          pageNum: this.pagination.currentPage - 1,
          size: this.pagination.pageSize,
          ...this.searchForm
        }

        if (this.searchForm.dateRange && this.searchForm.dateRange.length === 2) {
          params.startDate = this.formatDate(this.searchForm.dateRange[0])
          params.endDate = this.formatDate(this.searchForm.dateRange[1])
        }

        const res = await prepaymentApi.getList(params)
        if (res.code === 1) {
          this.tableData = res.data.tlist || []
          this.pagination.total = res.data.totalRecord || 0
        } else {
          this.$message.error(res.message || '查询失败')
        }
      } catch (error) {
        console.error('查询失败:', error)
        this.$message.error('查询失败')
      } finally {
        this.loading = false
      }
    },

    // 加载供应商列表
    async loadSuppliers() {
      try {
        const res = await getSupplierDropdownList()
        if (res.code === 1 && res.data) {
          this.supplierList = res.data
        }
      } catch (error) {
        console.error('加载供应商失败:', error)
      }
    },

    // 加载合同列表
    async loadContracts() {
      try {
        const res = await getContractDropdownList()
        if (res.code === 1 && res.data) {
          this.contractList = res.data
        }
      } catch (error) {
        console.error('加载合同失败:', error)
      }
    },

    // 合同选择变更 - 加载合同付款信息
    async handleContractChange(contractId) {
      if (!contractId) {
        this.contractInfo = null
        return
      }
      try {
        const res = await contractApi.getDetail(contractId)
        if (res.code === 1 && res.data) {
          this.contractInfo = {
            contractAmount: res.data.contractAmount || 0,
            paidAmount: res.data.paidAmount || 0,
            contractNumber: res.data.contractNumber || ''
          }
        }
      } catch (error) {
        console.error('获取合同信息失败:', error)
        this.contractInfo = null
      }
    },

    // 查询
    handleSearch() {
      this.pagination.currentPage = 1
      this.loadData()
    },

    // 重置
    handleReset() {
      this.$refs.searchForm.resetFields()
      this.searchForm.dateRange = null
      this.handleSearch()
    },

    // 新增
    handleAdd() {
      this.dialogTitle = '新增预付款'
      this.isViewMode = false
      this.contractInfo = null
      this.resetForm()
      this.formData.prepaymentNumber = 'PREP' + Date.now()
      this.dialogVisible = true
    },

    // 编辑
    handleEdit(row) {
      this.dialogTitle = '编辑预付款'
      this.isViewMode = false
      this.formData = { ...row }
      this.contractInfo = null
      if (row.contractId) {
        this.handleContractChange(row.contractId)
      }
      this.dialogVisible = true
    },

    // 查看
    handleView(row) {
      this.dialogTitle = '预付款详情'
      this.isViewMode = true
      this.formData = { ...row }
      this.contractInfo = null
      if (row.contractId) {
        this.handleContractChange(row.contractId)
      }
      this.dialogVisible = true
    },

    // 保存
    async handleSave() {
      this.$refs.formRef.validate(async (valid) => {
        if (valid) {
          try {
            const res = await prepaymentApi.save(this.formData)
            if (res.code === 1) {
              this.$message.success('保存成功')
              this.dialogVisible = false
              this.loadData()
            } else {
              this.$message.error(res.message || '保存失败')
            }
          } catch (error) {
            console.error('保存失败:', error)
            this.$message.error('保存失败')
          }
        }
      })
    },

    // 提交审批
    async handleSubmit() {
      this.$refs.formRef.validate(async (valid) => {
        if (valid) {
          // 预算校验
          try {
            const budgetCheck = await budgetApi.checkControl({
              businessType: 'PREPAYMENT',
              departmentId: this.formData.departmentId,
              amount: this.formData.prepaymentAmount,
              currency: 'CNY'
            })
            if (budgetCheck.code === 1 && budgetCheck.data && !budgetCheck.data.passed) {
              try {
                await this.$confirm(
                  `预算校验未通过：${budgetCheck.data.message || '超出预算额度'}，是否继续提交？`,
                  '预算预警',
                  { confirmButtonText: '继续提交', cancelButtonText: '取消', type: 'warning' }
                )
              } catch {
                return
              }
            }
          } catch (budgetError) {
            console.warn('预算校验失败，继续提交流程:', budgetError)
          }
          try {
            const res = await prepaymentApi.submit(this.formData.prepaymentId, {})
            if (res.code === 1) {
              this.$message.success('提交成功')
              this.dialogVisible = false
              this.loadData()
            } else {
              this.$message.error(res.message || '提交失败')
            }
          } catch (error) {
            console.error('提交失败:', error)
            this.$message.error('提交失败')
          }
        }
      })
    },

    // 审批
    handleApprove(row) {
      this.$confirm('确认审批通过该预付款申请?', '提示', {
        confirmButtonText: '通过',
        cancelButtonText: '驳回',
        distinguishCancelAndClose: true,
        type: 'warning'
      }).then(async () => {
        try {
          const res = await prepaymentApi.approve(row.prepaymentId, { approved: true })
          if (res.code === 1) {
            // 审批通过 - 占用预算
            try {
              await budgetApi.occupy({
                businessType: 'PREPAYMENT',
                businessId: row.prepaymentId,
                departmentId: row.departmentId,
                amount: row.prepaymentAmount,
                currency: 'CNY'
              })
            } catch (budgetError) {
              console.warn('预算占用失败:', budgetError)
            }
            this.$message.success('审批成功')
            this.loadData()
          } else {
            this.$message.error(res.message || '审批失败')
          }
        } catch (error) {
          console.error('审批失败:', error)
          this.$message.error('审批失败')
        }
      }).catch((action) => {
        if (action === 'cancel') {
          // 驳回操作
          this.$prompt('请输入驳回原因', '驳回', {
            confirmButtonText: '确定',
            cancelButtonText: '取消'
          }).then(async ({ value }) => {
            try {
              const res = await prepaymentApi.approve(row.prepaymentId, {
                approved: false,
                rejectReason: value
              })
              if (res.code === 1) {
                // 审批驳回 - 释放已占用预算
                try {
                  await budgetApi.release({
                    businessType: 'PREPAYMENT',
                    businessId: row.prepaymentId
                  })
                } catch (budgetError) {
                  console.warn('预算释放失败:', budgetError)
                }
                this.$message.success('已驳回')
                this.loadData()
              }
            } catch (error) {
              this.$message.error('驳回失败')
            }
          })
        }
      })
    },

    // 核销
    handleWriteOff(row) {
      this.currentRow = row
      this.writeOffDialogVisible = true
    },

    // 核销成功回调
    handleWriteOffSuccess() {
      this.loadData()
    },

    // 退款
    handleRefund(row) {
      this.currentRow = row
      this.refundDialogVisible = true
    },

    // 退款成功回调
    handleRefundSuccess() {
      this.loadData()
    },

    // 删除
    handleDelete(row) {
      this.$confirm('确认删除该预付款申请?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const res = await prepaymentApi.delete(row.prepaymentId)
          if (res.code === 1) {
            this.$message.success('删除成功')
            this.loadData()
          } else {
            this.$message.error(res.message || '删除失败')
          }
        } catch (error) {
          console.error('删除失败:', error)
          this.$message.error('删除失败')
        }
      })
    },

    // 批量审批
    handleBatchApprove() {
      if (this.multipleSelection.length === 0) {
        this.$message.warning('请选择要审批的预付款')
        return
      }
      this.$confirm(`确认批量审批${this.multipleSelection.length}条预付款?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const ids = this.multipleSelection.map(item => item.prepaymentId)
          const promises = ids.map(id => prepaymentApi.approve(id, { approved: true }))
          const results = await Promise.allSettled(promises)
          const successCount = results.filter(r => r.status === 'fulfilled' && r.value && r.value.code === 1).length
          this.$message.success(`批量审批完成，成功 ${successCount} 条`)
          this.loadData()
        } catch (error) {
          this.$message.error('批量审批失败：' + error.message)
        }
      })
    },

    // 批量删除
    handleBatchDelete() {
      if (this.multipleSelection.length === 0) {
        this.$message.warning('请选择要删除的预付款')
        return
      }
      this.$confirm(`确认删除${this.multipleSelection.length}条预付款?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const ids = this.multipleSelection.map(item => item.prepaymentId)
          const res = await prepaymentApi.batchDelete(ids)
          if (res.code === 1) {
            this.$message.success('删除成功')
            this.loadData()
          } else {
            this.$message.error(res.message || '删除失败')
          }
        } catch (error) {
          console.error('删除失败:', error)
          this.$message.error('删除失败')
        }
      })
    },

    // 导出
    handleExport() {
      const params = { ...this.searchForm }
      if (this.searchForm.dateRange && this.searchForm.dateRange.length === 2) {
        params.startDate = this.formatDate(this.searchForm.dateRange[0])
        params.endDate = this.formatDate(this.searchForm.dateRange[1])
      }
      prepaymentApi.export(params).then(res => {
        if (res.code === 1) {
          this.$message.success('导出成功')
        } else {
          this.$message.error(res.msg || '导出失败')
        }
      }).catch(error => {
        this.$message.error('导出失败：' + error.message)
      })
    },

    // 选择变化
    handleSelectionChange(selection) {
      this.multipleSelection = selection
    },

    // 分页大小变化
    handleSizeChange(val) {
      this.pagination.pageSize = val
      this.loadData()
    },

    // 当前页变化
    handleCurrentChange(val) {
      this.pagination.currentPage = val
      this.loadData()
    },

    // 对话框关闭
    handleDialogClose() {
      this.resetForm()
    },

    // 重置表单
    resetForm() {
      this.formData = {
        prepaymentId: '',
        prepaymentNumber: '',
        prepaymentTitle: '',
        prepaymentType: '',
        prepaymentAmount: 0,
        supplierId: '',
        contractId: '',
        expectedPaymentDate: '',
        urgencyLevel: 'NORMAL',
        prepaymentReason: '',
        remark: ''
      }
      this.$nextTick(() => {
        this.$refs.formRef && this.$refs.formRef.clearValidate()
      })
    },

    // 获取预付款类型颜色
    getPrepaymentTypeColor(type) {
      const colorMap = {
        'PROCUREMENT': 'primary',
        'SERVICE': 'success',
        'RENT': 'warning',
        'PROJECT': 'info',
        'OTHER': 'info'
      }
      return colorMap[type] || 'info'
    },

    // 获取状态颜色
    getStatusColor(status) {
      const colorMap = {
        'DRAFT': 'info',
        'PENDING': 'warning',
        'APPROVED': 'success',
        'PAID': 'primary',
        'PARTIAL_WRITEOFF': '',
        'WRITEOFF': 'success',
        'REJECTED': 'danger'
      }
      return colorMap[status] || 'info'
    },

    // 格式化金额
    formatAmount(amount) {
      if (!amount && amount !== 0) return '0.00'
      return parseFloat(amount).toLocaleString('zh-CN', {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
      })
    },

    // 格式化日期
    formatDate(date) {
      if (!date) return ''
      const d = new Date(date)
      const year = d.getFullYear()
      const month = String(d.getMonth() + 1).padStart(2, '0')
      const day = String(d.getDate()).padStart(2, '0')
      const hour = String(d.getHours()).padStart(2, '0')
      const minute = String(d.getMinutes()).padStart(2, '0')
      const second = String(d.getSeconds()).padStart(2, '0')

      // 如果只有日期部分,返回日期
      if (hour === '00' && minute === '00' && second === '00') {
        return `${year}-${month}-${day}`
      }

      return `${year}-${month}-${day} ${hour}:${minute}:${second}`
    }
  }
}
</script>

<style scoped lang="scss">
.prepayment-container {
  padding: 20px;
  background: #fff;

  .search-container {
    margin-bottom: 20px;
    padding: 20px;
    background: #f5f7fa;
    border-radius: 4px;

    .search-form {
      .el-form-item {
        margin-bottom: 0;
      }
    }
  }

  .toolbar {
    margin-bottom: 20px;
  }

  .table-container {
    .amount {
      font-weight: 600;
      color: #303133;

      &.writeoff {
        color: #67c23a;
      }

      &.remaining {
        color: #e6a23c;
      }
    }

    .pagination-container {
      margin-top: 20px;
      text-align: right;
    }
  }

  .dialog-footer {
    text-align: right;
  }

  .contract-info-panel {
    background: #f0f9eb;
    border: 1px solid #e1f3d8;
    border-radius: 4px;
    padding: 12px 16px;
    margin-bottom: 12px;

    .contract-info-title {
      font-weight: 600;
      color: #67c23a;
      margin-bottom: 8px;
      font-size: 14px;
    }

    .info-item {
      text-align: center;

      .info-label {
        display: block;
        font-size: 12px;
        color: #909399;
        margin-bottom: 4px;
      }

      .info-value {
        font-size: 16px;
        font-weight: 600;
        color: #303133;

        &.paid {
          color: #67c23a;
        }

        &.remaining {
          color: #e6a23c;
        }
      }
    }
  }
}
</style>
