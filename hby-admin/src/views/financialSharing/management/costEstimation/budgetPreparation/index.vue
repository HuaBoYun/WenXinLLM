<template>
  <div class="budget-preparation-container">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-left">
        <h1 class="page-title">
          <i class="el-icon-money"></i>
          预算编制管理
        </h1>
        <p class="page-description">管理年度预算编制、预算调整、预算审批等全流程</p>
      </div>
      <div class="header-right">
        <el-button type="primary" icon="el-icon-plus" @click="handleCreateBudget">
          新建预算
        </el-button>
        <el-button type="success" icon="el-icon-upload2" @click="handleImportBudget">
          导入预算
        </el-button>
        <el-button type="warning" icon="el-icon-download" @click="handleExportBudget">
          导出预算
        </el-button>
      </div>
    </div>

    <!-- 搜索条件 -->
    <div class="search-container">
      <el-form :model="queryForm" ref="queryForm" :inline="true" class="search-form">
        <el-form-item label="预算年度" prop="budgetYear">
          <el-date-picker
            v-model="queryForm.budgetYear"
            type="year"
            placeholder="选择预算年度"
            format="yyyy"
            value-format="yyyy"
            style="width: 150px"
          />
        </el-form-item>
        <el-form-item label="预算名称" prop="budgetName">
          <el-input
            v-model="queryForm.budgetName"
            placeholder="请输入预算名称"
            clearable
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="预算类型" prop="budgetType">
          <el-select
            v-model="queryForm.budgetType"
            placeholder="请选择预算类型"
            clearable
            style="width: 150px"
          >
            <el-option label="年度预算" :value="1" />
            <el-option label="季度预算" :value="2" />
            <el-option label="月度预算" :value="3" />
            <el-option label="项目预算" :value="4" />
          </el-select>
        </el-form-item>
        <el-form-item label="审批状态" prop="approvalStatus">
          <el-select
            v-model="queryForm.approvalStatus"
            placeholder="请选择审批状态"
            clearable
            style="width: 150px"
          >
            <el-option label="草稿" :value="0" />
            <el-option label="待审批" :value="1" />
            <el-option label="审批中" :value="2" />
            <el-option label="已通过" :value="3" />
            <el-option label="已驳回" :value="4" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleQuery">
            查询
          </el-button>
          <el-button icon="el-icon-refresh" @click="handleReset">
            重置
          </el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 数据表格 -->
    <div class="table-container">
      <el-table
        v-loading="loading"
        :data="tableData"
        border
        stripe
        style="width: 100%"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column prop="budgetNo" label="预算编码" width="150" />
        <el-table-column prop="budgetName" label="预算名称" min-width="200" />
        <el-table-column prop="budgetYear" label="预算年度" width="100" />
        <el-table-column prop="budgetTypeName" label="预算类型" width="120" />
        <el-table-column prop="budgetAmount" label="预算总额" width="150" align="right">
          <template slot-scope="scope">
            {{ formatCurrency(scope.row.budgetAmount) }}
          </template>
        </el-table-column>
        <el-table-column prop="usedAmount" label="已使用" width="150" align="right">
          <template slot-scope="scope">
            {{ formatCurrency(scope.row.usedAmount) }}
          </template>
        </el-table-column>
        <el-table-column prop="remainingAmount" label="剩余预算" width="150" align="right">
          <template slot-scope="scope">
            {{ formatCurrency(scope.row.remainingAmount) }}
          </template>
        </el-table-column>
        <el-table-column prop="usageRate" label="使用率" width="100" align="right">
          <template slot-scope="scope">
            <span :class="getUsageRateClass(scope.row.usageRate)">
              {{ scope.row.usageRate }}%
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="approvalStatusName" label="审批状态" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getApprovalStatusType(scope.row.approvalStatus)">
              {{ scope.row.approvalStatusName }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" width="250" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" type="text" @click="handleView(scope.row)">
              查看
            </el-button>
            <el-button 
              size="mini" 
              type="text" 
              @click="handleEdit(scope.row)"
              :disabled="scope.row.approvalStatus !== 0"
            >
              编辑
            </el-button>
            <el-button 
              size="mini" 
              type="text" 
              @click="handleSubmitApproval(scope.row)"
              :disabled="scope.row.approvalStatus !== 0"
            >
              提交审批
            </el-button>
            <el-button 
              size="mini" 
              type="text" 
              @click="handleAdjust(scope.row)"
              :disabled="scope.row.approvalStatus !== 3"
            >
              预算调整
            </el-button>
            <el-button size="mini" type="text" class="danger-text" @click="handleDelete(scope.row)">
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页组件 -->
      <div class="pagination-container">
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="queryForm.pageNumber"
          :page-sizes="[10, 20, 50, 100]"
          :page-size="queryForm.pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
        />
      </div>
    </div>

    <!-- 新增/编辑对话框 -->
    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogVisible"
      width="1000px"
      :close-on-click-modal="false"
      @close="handleDialogClose"
    >
      <el-form
        :model="formData"
        :rules="formRules"
        ref="formRef"
        label-width="120px"
        class="dialog-form"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="预算编码" prop="budgetCode">
              <el-input
                v-model="formData.budgetCode"
                placeholder="请输入预算编码"
                :disabled="isView"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="预算名称" prop="budgetName">
              <el-input
                v-model="formData.budgetName"
                placeholder="请输入预算名称"
                :disabled="isView"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="预算年度" prop="budgetYear">
              <el-date-picker
                v-model="formData.budgetYear"
                type="year"
                placeholder="选择预算年度"
                format="yyyy"
                value-format="yyyy"
                style="width: 100%"
                :disabled="isView"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="预算类型" prop="budgetType">
              <el-select
                v-model="formData.budgetType"
                placeholder="请选择预算类型"
                style="width: 100%"
                :disabled="isView"
              >
                <el-option label="年度预算" value="ANNUAL" />
                <el-option label="季度预算" value="QUARTERLY" />
                <el-option label="月度预算" value="MONTHLY" />
                <el-option label="项目预算" value="PROJECT" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="预算总额" prop="totalAmount">
              <el-input-number
                v-model="formData.totalAmount"
                :min="0"
                :precision="2"
                placeholder="请输入预算总额"
                style="width: 100%"
                :disabled="isView"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="币种" prop="currency">
              <el-select
                v-model="formData.currency"
                placeholder="请选择币种"
                style="width: 100%"
                :disabled="isView"
              >
                <el-option label="人民币" value="CNY" />
                <el-option label="美元" value="USD" />
                <el-option label="欧元" value="EUR" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="预算描述" prop="description">
          <el-input
            v-model="formData.description"
            type="textarea"
            :rows="4"
            placeholder="请输入预算描述"
            :disabled="isView"
          />
        </el-form-item>

        <!-- 预算明细 -->
        <el-form-item label="预算明细">
          <el-table :data="formData.budgetDetails" border stripe style="width: 100%">
            <el-table-column prop="itemName" label="预算项目" width="200">
              <template slot-scope="scope">
                <el-input
                  v-model="scope.row.itemName"
                  placeholder="请输入项目名称"
                  :disabled="isView"
                />
              </template>
            </el-table-column>
            <el-table-column prop="itemAmount" label="预算金额" width="150">
              <template slot-scope="scope">
                <el-input-number
                  v-model="scope.row.itemAmount"
                  :min="0"
                  :precision="2"
                  style="width: 100%"
                  :disabled="isView"
                />
              </template>
            </el-table-column>
            <el-table-column prop="itemDescription" label="说明" min-width="200">
              <template slot-scope="scope">
                <el-input
                  v-model="scope.row.itemDescription"
                  placeholder="请输入说明"
                  :disabled="isView"
                />
              </template>
            </el-table-column>
            <el-table-column label="操作" width="100" align="center" v-if="!isView">
              <template slot-scope="scope">
                <el-button size="mini" type="text" class="danger-text" @click="handleDeleteDetail(scope.$index)">
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
          <div style="margin-top: 10px;" v-if="!isView">
            <el-button size="small" type="primary" @click="handleAddDetail">
              添加明细
            </el-button>
          </div>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button v-if="!isView" type="primary" @click="handleSubmit" :loading="submitLoading">
          确定
        </el-button>
      </div>
    </el-dialog>

    <!-- 预算调整对话框 -->
    <el-dialog
      title="预算调整"
      :visible.sync="adjustDialogVisible"
      width="600px"
      :close-on-click-modal="false"
    >
      <el-form :model="adjustForm" :rules="adjustRules" ref="adjustFormRef" label-width="120px">
        <el-form-item label="调整类型" prop="adjustType">
          <el-radio-group v-model="adjustForm.adjustType">
            <el-radio :label="1">增加预算</el-radio>
            <el-radio :label="2">减少预算</el-radio>
            <el-radio :label="3">项目调整</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="调整金额" prop="adjustAmount">
          <el-input-number
            v-model="adjustForm.adjustAmount"
            :min="0"
            :precision="2"
            placeholder="请输入调整金额"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="调整原因" prop="adjustReason">
          <el-input
            v-model="adjustForm.adjustReason"
            type="textarea"
            :rows="4"
            placeholder="请输入调整原因"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="adjustDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmitAdjust" :loading="adjustLoading">
          确定
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  getBudgetPreparationPage,
  saveOrUpdateBudgetPreparation,
  submitBudgetPreparation,
  getBudgetPreparationById,
  exportCostEstimateDataBlob,
  batchImportCostEstimate
} from '@/api/financialSharing/costEstimation'
import { normalizeKeysArray } from '@/utils/keyNormalize'

export default {
  name: 'BudgetPreparation',
  data() {
    return {
      loading: false,
      submitLoading: false,
      adjustLoading: false,
      tableData: [],
      total: 0,
      selectedRows: [],
      queryForm: {
        pageNumber: 1,
        pageSize: 20,
        budgetYear: '',
        budgetName: '',
        budgetType: null,
        approvalStatus: null
      },
      dialogVisible: false,
      adjustDialogVisible: false,
      dialogTitle: '',
      isView: false,
      formData: {
        budgetId: null,
        budgetCode: '',
        budgetName: '',
        budgetYear: '',
        budgetType: null,
        totalAmount: 0,
        currency: 'CNY',
        description: '',
        budgetDetails: []
      },
      adjustForm: {
        budgetId: null,
        adjustType: 1,
        adjustAmount: 0,
        adjustReason: ''
      },
      formRules: {
        budgetCode: [
          { required: true, message: '请输入预算编码', trigger: 'blur' }
        ],
        budgetName: [
          { required: true, message: '请输入预算名称', trigger: 'blur' }
        ],
        budgetYear: [
          { required: true, message: '请选择预算年度', trigger: 'change' }
        ],
        budgetType: [
          { required: true, message: '请选择预算类型', trigger: 'change' }
        ],
        totalAmount: [
          { required: true, message: '请输入预算总额', trigger: 'blur' }
        ]
      },
      adjustRules: {
        adjustAmount: [
          { required: true, message: '请输入调整金额', trigger: 'blur' }
        ],
        adjustReason: [
          { required: true, message: '请输入调整原因', trigger: 'blur' }
        ]
      }
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    // 获取数据
    async fetchData() {
      this.loading = true
      try {
        const response = await getBudgetPreparationPage(this.queryForm)

        // 后端原生返回 code=1，老代码错把 200 当成功，导致表格永远不渲染
        if (response && (response.code === 1 || response.code === 200)) {
          // 后端 PageResult: tlist + totalRecord（兼容 records/total）
          // 达梦 ALLCAPS key（BUDGETNAME / APPROVALSTATUS）需归一化为 camelCase
          const rawList = response.data.tlist || response.data.records || []
          this.tableData = normalizeKeysArray(rawList)
          this.total = response.data.totalRecord || response.data.total || 0

          // 处理数据格式
          this.tableData.forEach(item => {
            // 预算类型名称映射（兼容字符串枚举 ANNUAL/QUARTERLY/MONTHLY 与历史数字编码）
            const typeMap = {
              ANNUAL: '年度预算', QUARTERLY: '季度预算', MONTHLY: '月度预算',
              1: '年度预算', 2: '季度预算', 3: '月度预算', 4: '项目预算'
            }
            item.budgetTypeName = typeMap[item.budgetType] || (item.budgetType || '未知')

            // 审批状态名称映射（后端：0-草稿 1-待审批 2-审批中 3-已审批 4-已驳回）
            const statusMap = {
              0: '草稿', 1: '待审批', 2: '审批中', 3: '已通过', 4: '已驳回'
            }
            item.approvalStatusName = statusMap[item.approvalStatus] || '未知'

            // 派生字段：预算总额 / 已使用 / 剩余 / 使用率
            // budgetAmount = 总额（DB 字段）；usedAmount = 已使用（DB 字段，无值时按 0 兜底）
            const total = Number(item.budgetAmount || 0)
            const used  = Number(item.usedAmount || 0)
            item.usedAmount      = used
            item.remainingAmount = (total - used).toFixed(2)
            item.usageRate       = total > 0 ? ((used / total) * 100).toFixed(2) : '0.00'
          })
        } else if (response && response.msg) {
          this.$message.error(response.msg)
        }
      } catch (error) {
        this.$message.error('获取数据失败：' + error.message)
      } finally {
        this.loading = false
      }
    },

    // 查询
    handleQuery() {
      this.queryForm.pageNumber = 1
      this.fetchData()
    },

    // 重置
    handleReset() {
      this.$refs.queryForm.resetFields()
      // 复位分页避免在小页码上停留导致空数据
      this.queryForm.pageNumber = 1
      this.fetchData()
    },

    // 新建预算
    handleCreateBudget() {
      this.dialogTitle = '新建预算'
      this.isView = false
      this.resetFormData()
      this.dialogVisible = true
    },

    // 把表格行（后端命名）映射到详情/编辑表单（前端 form 命名）
    // 容错：兼容前端字段名 / 后端字段名 / 老数据
    rowToFormData(row) {
      if (!row) return this.emptyFormData()
      return {
        budgetId:       row.budgetId || null,
        budgetCode:     row.budgetCode || row.budgetNo || '',
        budgetName:     row.budgetName || '',
        budgetYear:     row.budgetYear ? String(row.budgetYear) : '',
        budgetType:     row.budgetType || null,
        totalAmount:    row.totalAmount != null ? row.totalAmount : (row.budgetAmount != null ? row.budgetAmount : 0),
        usedAmount:     row.usedAmount != null ? row.usedAmount : 0,
        currency:       row.currency || 'CNY',
        description:    row.description || row.remark || '',
        costCenterId:   row.costCenterId || null,
        departmentId:   row.departmentId || null,
        preparerId:     row.preparerId || null,
        preparerName:   row.preparerName || '',
        preparationDate: row.preparationDate || null,
        approvalStatus: row.approvalStatus,
        approverName:   row.approverName || '',
        approvalTime:   row.approvalTime || null,
        approvalComment: row.approvalComment || '',
        budgetDetails:  Array.isArray(row.budgetDetails) ? row.budgetDetails : []
      }
    },

    emptyFormData() {
      return {
        budgetId: null, budgetCode: '', budgetName: '', budgetYear: '',
        budgetType: null, totalAmount: 0, currency: 'CNY',
        description: '', budgetDetails: []
      }
    },

    // 查看
    handleView(row) {
      this.dialogTitle = '查看预算'
      this.isView = true
      this.formData = this.rowToFormData(row)
      this.dialogVisible = true
    },

    // 编辑
    handleEdit(row) {
      this.dialogTitle = '编辑预算'
      this.isView = false
      this.formData = this.rowToFormData(row)
      this.dialogVisible = true
    },

    // 提交审批
    async handleSubmitApproval(row) {
      try {
        await this.$confirm('确认提交该预算进行审批吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })

        await submitBudgetPreparation(row.budgetId)

        this.$message.success('提交审批成功')
        this.fetchData()
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('提交失败：' + error.message)
        }
      }
    },

    // 预算调整
    handleAdjust(row) {
      this.adjustForm.budgetId = row.budgetId
      this.adjustDialogVisible = true
    },

    // 删除
    async handleDelete(row) {
      try {
        await this.$confirm('确认删除该预算吗？删除后不可恢复！', '警告', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })

        await getBudgetPreparationById(row.budgetId)

        this.$message.success('删除成功')
        this.fetchData()
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('删除失败：' + error.message)
        }
      }
    },

    // 提交表单
    async handleSubmit() {
      try {
        await this.$refs.formRef.validate()
        this.submitLoading = true

        await saveOrUpdateBudgetPreparation(this.formData)

        this.$message.success('保存成功')
        this.dialogVisible = false
        this.fetchData()
      } catch (error) {
        if (error !== false) {
          this.$message.error('保存失败：' + error.message)
        }
      } finally {
        this.submitLoading = false
      }
    },

    // 提交调整
    async handleSubmitAdjust() {
      try {
        await this.$refs.adjustFormRef.validate()
        this.adjustLoading = true

        await saveOrUpdateBudgetPreparation(this.adjustForm)

        this.$message.success('预算调整提交成功')
        this.adjustDialogVisible = false
        this.fetchData()
      } catch (error) {
        if (error !== false) {
          this.$message.error('调整失败：' + error.message)
        }
      } finally {
        this.adjustLoading = false
      }
    },

    // 添加明细
    handleAddDetail() {
      this.formData.budgetDetails.push({
        itemName: '',
        itemAmount: 0,
        itemDescription: ''
      })
    },

    // 删除明细
    handleDeleteDetail(index) {
      this.formData.budgetDetails.splice(index, 1)
    },

    // 导入预算
    handleImportBudget() {
      const input = document.createElement('input')
      input.type = 'file'
      input.accept = '.xlsx,.xls'
      input.onchange = async (e) => {
        const file = e.target.files[0]
        if (!file) return
        const formData = new FormData()
        formData.append('file', file)
        try {
          const res = await batchImportCostEstimate(formData)
          if (res.code === 1 || res.code === 200) {
            this.$message.success('导入成功')
            this.fetchData()
          } else {
            this.$message.error(res.msg || '导入失败')
          }
        } catch (error) {
          this.$message.error('导入失败')
        }
      }
      input.click()
    },

    // 导出预算
    async handleExportBudget() {
      try {
        // 后端 blob 模式返回 { data, headers, status } 包装；老代码直接 Blob 整个响应会拿到 [object Object]
        const resp = await exportCostEstimateDataBlob(this.queryForm)
        const raw = (resp && resp.data) ? resp.data : resp
        const blob = raw instanceof Blob ? raw : new Blob([raw], { type: 'text/csv;charset=utf-8' })

        // 优先读 Content-Disposition 给的文件名，没有就回退到时间戳
        let filename = `预算编制_${new Date().toISOString().slice(0, 10)}.csv`
        const cd = resp && resp.headers && (resp.headers['content-disposition'] || resp.headers['Content-Disposition'])
        if (cd) {
          const m = /filename\*?=(?:UTF-8'')?"?([^";]+)"?/i.exec(cd)
          if (m && m[1]) {
            try { filename = decodeURIComponent(m[1]) } catch (e) { filename = m[1] }
          }
        }

        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = filename
        link.click()
        window.URL.revokeObjectURL(url)
        this.$message.success('导出成功')
      } catch (error) {
        this.$message.error('导出失败：' + (error && error.message ? error.message : ''))
      }
    },

    // 分页相关
    handleSizeChange(val) {
      this.queryForm.pageSize = val
      this.fetchData()
    },

    handleCurrentChange(val) {
      this.queryForm.pageNumber = val
      this.fetchData()
    },

    // 选择变化
    handleSelectionChange(selection) {
      this.selectedRows = selection
    },

    // 对话框关闭
    handleDialogClose() {
      this.$refs.formRef?.resetFields()
      this.resetFormData()
    },

    // 重置表单数据
    resetFormData() {
      this.formData = {
        budgetId: null,
        budgetCode: '',
        budgetName: '',
        budgetYear: '',
        budgetType: null,
        totalAmount: 0,
        currency: 'CNY',
        description: '',
        budgetDetails: []
      }
    },

    // 格式化货币
    formatCurrency(amount) {
      if (amount == null) return '0.00'
      return Number(amount).toLocaleString('zh-CN', {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
      })
    },

    // 获取使用率样式类
    getUsageRateClass(rate) {
      if (rate >= 90) return 'high-usage'
      if (rate >= 70) return 'medium-usage'
      return 'low-usage'
    },

    // 获取审批状态类型
    getApprovalStatusType(status) {
      const statusMap = {
        0: 'info',    // 草稿
        1: 'warning', // 待审批
        2: 'primary', // 审批中
        3: 'success', // 已通过
        4: 'danger'   // 已驳回
      }
      return statusMap[status] || 'info'
    }
  }
}
</script>

<style lang="scss" scoped>
.budget-preparation-container {
  padding: 20px;
  background-color: #f5f5f5;
  min-height: calc(100vh - 84px);
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding: 20px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);

  .header-left {
    .page-title {
      margin: 0 0 8px 0;
      font-size: 24px;
      font-weight: 600;
      color: #303133;

      i {
        margin-right: 8px;
        color: #67c23a;
      }
    }

    .page-description {
      margin: 0;
      color: #909399;
      font-size: 14px;
    }
  }

  .header-right {
    .el-button {
      margin-left: 10px;
    }
  }
}

.search-container {
  margin-bottom: 20px;
  padding: 20px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);

  .search-form {
    .el-form-item {
      margin-bottom: 0;
    }
  }
}

.table-container {
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  padding: 20px;

  .pagination-container {
    margin-top: 20px;
    text-align: right;
  }
}

.dialog-form {
  .el-form-item {
    margin-bottom: 20px;
  }
}

.danger-text {
  color: #f56c6c !important;
}

.high-usage {
  color: #f56c6c;
  font-weight: 600;
}

.medium-usage {
  color: #e6a23c;
  font-weight: 600;
}

.low-usage {
  color: #67c23a;
}
</style>
