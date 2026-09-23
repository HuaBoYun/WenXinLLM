<template>
  <div class="transfer-pricing-container">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-left">
        <h1 class="page-title">
          <i class="el-icon-price-tag"></i>
          转移定价管理
        </h1>
        <p class="page-description">制定和管理内部交易的定价策略、定价方法和价格调整机制</p>
      </div>
      <div class="header-right">
        <el-button type="primary" icon="el-icon-plus" @click="handleAdd">
          新建定价策略
        </el-button>
        <el-button type="success" icon="el-icon-check" @click="batchApprove">
          批量审批
        </el-button>
        <el-button type="warning" icon="el-icon-refresh" @click="priceAdjustment">
          价格调整
        </el-button>
      </div>
    </div>

    <!-- 统计卡片 -->
    <el-row :gutter="20" class="stats-row">
      <el-col :span="6">
        <div class="stat-card">
          <div class="stat-icon pricing-policies">
            <i class="el-icon-document"></i>
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ stats.pricingPolicies }}</div>
            <div class="stat-label">定价策略数</div>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card">
          <div class="stat-icon active-policies">
            <i class="el-icon-success"></i>
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ stats.activePolicies }}</div>
            <div class="stat-label">生效策略数</div>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card">
          <div class="stat-icon avg-margin">
            <i class="el-icon-trend-charts"></i>
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ stats.avgMargin }}%</div>
            <div class="stat-label">平均利润率</div>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card">
          <div class="stat-icon price-adjustments">
            <i class="el-icon-refresh"></i>
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ stats.priceAdjustments }}</div>
            <div class="stat-label">本月调价次数</div>
          </div>
        </div>
      </el-col>
    </el-row>

    <!-- 查询条件 -->
    <el-card shadow="never" class="search-card">
      <el-form :model="queryForm" :inline="true" label-width="80px">
        <el-form-item label="策略名称">
          <el-input v-model="queryForm.policyName" placeholder="请输入策略名称" clearable />
        </el-form-item>
        <el-form-item label="定价方法">
          <el-select v-model="queryForm.pricingMethod" placeholder="请选择定价方法" clearable>
            <el-option label="成本加成法" :value="1" />
            <el-option label="市场价格法" :value="2" />
            <el-option label="利润分割法" :value="3" />
            <el-option label="交易净利润法" :value="4" />
            <el-option label="其他合理方法" :value="5" />
          </el-select>
        </el-form-item>
        <el-form-item label="策略状态">
          <el-select v-model="queryForm.policyStatus" placeholder="请选择策略状态" clearable>
            <el-option label="草稿" :value="1" />
            <el-option label="待审批" :value="2" />
            <el-option label="已生效" :value="3" />
            <el-option label="已失效" :value="4" />
          </el-select>
        </el-form-item>
        <el-form-item label="适用产品">
          <el-input v-model="queryForm.productName" placeholder="请输入产品名称" clearable />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleQuery">查询</el-button>
          <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 数据表格 -->
    <el-card shadow="never" class="table-card">
      <el-table
        v-loading="loading"
        :data="pricingList"
        stripe
        border
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="policyCode" label="策略编码" width="120" />
        <el-table-column prop="policyName" label="策略名称" width="180" />
        <el-table-column prop="pricingMethod" label="定价方法" width="120">
          <template slot-scope="scope">
            <el-tag :type="getMethodTag(scope.row.pricingMethod)">
              {{ getMethodName(scope.row.pricingMethod) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="productName" label="适用产品" width="150" />
        <el-table-column prop="basePrice" label="基准价格" width="100" align="right">
          <template slot-scope="scope">
            <span class="price-text">{{ formatPrice(scope.row.basePrice) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="marginRate" label="利润率" width="80" align="right">
          <template slot-scope="scope">
            <span class="margin-text">{{ scope.row.marginRate }}%</span>
          </template>
        </el-table-column>
        <el-table-column prop="effectiveDate" label="生效日期" width="120" />
        <el-table-column prop="policyStatus" label="状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="getStatusTag(scope.row.policyStatus)">
              {{ getStatusName(scope.row.policyStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" type="text" @click="handleView(scope.row)">查看</el-button>
            <el-button size="mini" type="text" @click="handleEdit(scope.row)" v-if="scope.row.policyStatus === 1">编辑</el-button>
            <el-button size="mini" type="text" @click="handleApprove(scope.row)" v-if="scope.row.policyStatus === 2">审批</el-button>
            <el-button size="mini" type="text" @click="handleAdjust(scope.row)" v-if="scope.row.policyStatus === 3">调价</el-button>
            <el-button size="mini" type="text" @click="handleDelete(scope.row)" v-if="scope.row.policyStatus === 1">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="pagination.currentPage"
        :page-sizes="[10, 20, 50, 100]"
        :page-size="pagination.pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="pagination.total"
        style="margin-top: 20px; text-align: right;"
      />
    </el-card>

    <!-- 新增/编辑对话框 -->
    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogVisible"
      width="900px"
      :close-on-click-modal="false"
    >
      <el-form :model="formData" :rules="formRules" ref="form" label-width="120px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="策略编码" prop="policyCode">
              <el-input v-model="formData.policyCode" placeholder="系统自动生成" :disabled="true" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="策略名称" prop="policyName">
              <el-input v-model="formData.policyName" placeholder="请输入策略名称" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="定价方法" prop="pricingMethod">
              <el-select v-model="formData.pricingMethod" placeholder="请选择定价方法">
                <el-option label="成本加成法" :value="1" />
                <el-option label="市场价格法" :value="2" />
                <el-option label="利润分割法" :value="3" />
                <el-option label="交易净利润法" :value="4" />
                <el-option label="其他合理方法" :value="5" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="适用产品" prop="productName">
              <el-input v-model="formData.productName" placeholder="请输入适用产品" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="基准价格" prop="basePrice">
              <el-input-number
                v-model="formData.basePrice"
                :precision="2"
                :min="0"
                :max="999999.99"
                placeholder="请输入基准价格"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="利润率(%)" prop="marginRate">
              <el-input-number
                v-model="formData.marginRate"
                :precision="2"
                :min="0"
                :max="100"
                placeholder="请输入利润率"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="生效日期" prop="effectiveDate">
              <el-date-picker
                v-model="formData.effectiveDate"
                type="date"
                placeholder="请选择生效日期"
                format="yyyy-MM-dd"
                value-format="yyyy-MM-dd"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="失效日期" prop="expiryDate">
              <el-date-picker
                v-model="formData.expiryDate"
                type="date"
                placeholder="请选择失效日期"
                format="yyyy-MM-dd"
                value-format="yyyy-MM-dd"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="定价依据" prop="pricingBasis">
          <el-input
            v-model="formData.pricingBasis"
            type="textarea"
            :rows="3"
            placeholder="请输入定价依据和计算方法"
          />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input
            v-model="formData.remark"
            type="textarea"
            :rows="2"
            placeholder="请输入备注信息"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitLoading">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getTransferPricingList, createTransferPricing, updateTransferPricing, deleteTransferPricing, getTransferPricingStats, approveTransferPricing, adjustTransferPrice, getTransferPricingDetail } from '@/api/financialSharing/internalSettlement'

export default {
  name: 'TransferPricing',
  data() {
    return {
      loading: false,
      submitLoading: false,
      pricingList: [],
      selectedRows: [],
      stats: {
        pricingPolicies: 0,
        activePolicies: 0,
        avgMargin: 0,
        priceAdjustments: 0
      },
      queryForm: {
        policyName: '',
        pricingMethod: '',
        policyStatus: '',
        productName: ''
      },
      pagination: {
        currentPage: 1,
        pageSize: 20,
        total: 0
      },
      dialogVisible: false,
      dialogTitle: '',
      isEdit: false,
      formData: {
        policyId: null,
        policyCode: '',
        policyName: '',
        pricingMethod: '',
        productName: '',
        basePrice: 0,
        marginRate: 0,
        effectiveDate: '',
        expiryDate: '',
        pricingBasis: '',
        remark: ''
      },
      formRules: {
        policyName: [{ required: true, message: '请输入策略名称', trigger: 'blur' }],
        pricingMethod: [{ required: true, message: '请选择定价方法', trigger: 'change' }],
        productName: [{ required: true, message: '请输入适用产品', trigger: 'blur' }],
        basePrice: [{ required: true, message: '请输入基准价格', trigger: 'blur' }],
        marginRate: [{ required: true, message: '请输入利润率', trigger: 'blur' }],
        effectiveDate: [{ required: true, message: '请选择生效日期', trigger: 'change' }]
      }
    }
  },
  mounted() {
    this.fetchData()
    this.loadStats()
  },
  methods: {
    async fetchData() {
      this.loading = true
      try {
        const params = {
          ...this.queryForm,
          pageNumber: this.pagination.currentPage,
          pageSize: this.pagination.pageSize
        }
        const response = await getTransferPricingList(params)
        if (response.code === 1) {
          this.pricingList = response.data.records || []
          this.pagination.total = response.data.total || 0
        }
      } catch (error) {
        this.$message.error('获取数据失败')
      } finally {
        this.loading = false
      }
    },
    async loadStats() {
      try {
        const response = await getTransferPricingStats({})
        if (response.code === 1) {
          const data = response.data || {}
          this.stats = {
            pricingPolicies: data.pricingPolicies || 0,
            activePolicies: data.activePolicies || 0,
            avgMargin: data.avgMargin || 0,
            priceAdjustments: data.priceAdjustments || 0
          }
        } else {
          console.error('获取统计数据失败:', response.msg)
        }
      } catch (error) {
        console.error('获取统计数据失败:', error)
      }
    },
    handleQuery() {
      this.pagination.currentPage = 1
      this.fetchData()
    },
    resetQuery() {
      this.queryForm = {
        policyName: '',
        pricingMethod: '',
        policyStatus: '',
        productName: ''
      }
      this.handleQuery()
    },
    handleSizeChange(val) {
      this.pagination.pageSize = val
      this.fetchData()
    },
    handleCurrentChange(val) {
      this.pagination.currentPage = val
      this.fetchData()
    },
    handleSelectionChange(selection) {
      this.selectedRows = selection
    },
    handleAdd() {
      this.dialogTitle = '新建定价策略'
      this.isEdit = false
      this.resetForm()
      this.dialogVisible = true
    },
    handleEdit(row) {
      this.dialogTitle = '编辑定价策略'
      this.isEdit = true
      this.formData = { ...row }
      this.dialogVisible = true
    },
    handleView(row) {
      const content = `
        <p><b>策略名称：</b>${row.policyName || ''}</p>
        <p><b>定价方法：</b>${row.pricingMethod || ''}</p>
        <p><b>适用范围：</b>${row.applicableScope || ''}</p>
        <p><b>基准价格：</b>${row.basePrice || 0}</p>
        <p><b>调整系数：</b>${row.adjustFactor || ''}</p>
        <p><b>状态：</b>${row.statusName || ''}</p>
        <p><b>生效日期：</b>${row.effectiveDate || ''}</p>
      `
      this.$alert(content, '定价策略详情', { dangerouslyUseHTMLString: true })
    },
    async handleApprove(row) {
      try {
        await this.$confirm('确认审批通过该定价策略？', '审批确认', { type: 'warning' })
        const response = await approveTransferPricing(row.policyId, { status: 'approved' })
        if (response.code === 1) {
          this.$message.success('审批成功')
          this.fetchData()
        } else {
          this.$message.error(response.msg || '审批失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('操作失败')
        }
      }
    },
    handleAdjust(row) {
      this.$prompt('请输入调整后的价格', '价格调整', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputPattern: /^[0-9]+(\.[0-9]{1,4})?$/,
        inputErrorMessage: '请输入有效的价格（最多4位小数）',
        inputValue: row.basePrice ? String(row.basePrice) : ''
      }).then(async ({ value }) => {
        try {
          const response = await adjustTransferPrice(row.policyId, { newPrice: parseFloat(value) })
          if (response.code === 1) {
            this.$message.success('价格调整成功')
            this.fetchData()
          } else {
            this.$message.error(response.msg || '价格调整失败')
          }
        } catch (error) {
          this.$message.error('操作失败')
        }
      }).catch(() => {})
    },
    async handleDelete(row) {
      try {
        await this.$confirm('确定要删除这条记录吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        const response = await deleteTransferPricing(row.policyId)
        if (response.code === 1) {
          this.$message.success('删除成功')
          this.fetchData()
        }
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('删除失败')
        }
      }
    },
    async batchApprove() {
      if (this.selectedRows.length === 0) {
        this.$message.warning('请选择要审批的记录')
        return
      }
      try {
        await this.$confirm(`确认批量审批选中的 ${this.selectedRows.length} 条记录？`, '批量审批确认', { type: 'warning' })
        let successCount = 0
        let failCount = 0
        for (const row of this.selectedRows) {
          try {
            const response = await approveTransferPricing(row.policyId, { status: 'approved' })
            if (response.code === 1) {
              successCount++
            } else {
              failCount++
            }
          } catch (e) {
            failCount++
          }
        }
        if (failCount === 0) {
          this.$message.success(`批量审批完成，共 ${successCount} 条`)
        } else {
          this.$message.warning(`批量审批完成：成功 ${successCount} 条，失败 ${failCount} 条`)
        }
        this.fetchData()
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('批量审批失败')
        }
      }
    },
    priceAdjustment() {
      if (this.selectedRows.length === 0) {
        this.$message.warning('请选择要调整价格的记录')
        return
      }
      if (this.selectedRows.length === 1) {
        this.handleAdjust(this.selectedRows[0])
        return
      }
      this.$prompt('请输入统一调整后的价格', '批量价格调整', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputPattern: /^[0-9]+(\.[0-9]{1,4})?$/,
        inputErrorMessage: '请输入有效的价格（最多4位小数）'
      }).then(async ({ value }) => {
        let successCount = 0
        let failCount = 0
        for (const row of this.selectedRows) {
          try {
            const response = await adjustTransferPrice(row.policyId, { newPrice: parseFloat(value) })
            if (response.code === 1) {
              successCount++
            } else {
              failCount++
            }
          } catch (e) {
            failCount++
          }
        }
        if (failCount === 0) {
          this.$message.success(`批量价格调整完成，共 ${successCount} 条`)
        } else {
          this.$message.warning(`批量调整完成：成功 ${successCount} 条，失败 ${failCount} 条`)
        }
        this.fetchData()
      }).catch(() => {})
    },
    async handleSubmit() {
      try {
        await this.$refs.form.validate()
        this.submitLoading = true
        
        const response = this.isEdit 
          ? await updateTransferPricing(this.formData)
          : await createTransferPricing(this.formData)
          
        if (response.code === 1) {
          this.$message.success(this.isEdit ? '更新成功' : '创建成功')
          this.dialogVisible = false
          this.fetchData()
        }
      } catch (error) {
        this.$message.error(this.isEdit ? '更新失败' : '创建失败')
      } finally {
        this.submitLoading = false
      }
    },
    resetForm() {
      this.formData = {
        policyId: null,
        policyCode: '',
        policyName: '',
        pricingMethod: '',
        productName: '',
        basePrice: 0,
        marginRate: 0,
        effectiveDate: '',
        expiryDate: '',
        pricingBasis: '',
        remark: ''
      }
      if (this.$refs.form) {
        this.$refs.form.resetFields()
      }
    },
    formatPrice(price) {
      return price ? `¥${price.toLocaleString()}` : '¥0.00'
    },
    getMethodName(method) {
      const methods = {
        1: '成本加成法',
        2: '市场价格法',
        3: '利润分割法',
        4: '交易净利润法',
        5: '其他合理方法'
      }
      return methods[method] || '未知'
    },
    getMethodTag(method) {
      const tags = {
        1: 'success',
        2: 'primary',
        3: 'warning',
        4: 'info',
        5: 'danger'
      }
      return tags[method] || ''
    },
    getStatusName(status) {
      const statuses = {
        1: '草稿',
        2: '待审批',
        3: '已生效',
        4: '已失效'
      }
      return statuses[status] || '未知'
    },
    getStatusTag(status) {
      const tags = {
        1: 'info',
        2: 'warning',
        3: 'success',
        4: 'danger'
      }
      return tags[status] || ''
    }
  }
}
</script>

<style lang="scss" scoped>
.transfer-pricing-container {
  padding: 20px;
  background: #f5f7fa;
  min-height: calc(100vh - 84px);
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: white;
  border-radius: 8px;
  padding: 20px;
  margin-bottom: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);

  .header-left {
    .page-title {
      font-size: 20px;
      font-weight: 600;
      color: #303133;
      margin: 0 0 8px 0;
      display: flex;
      align-items: center;

      i {
        margin-right: 8px;
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
      margin-left: 8px;
    }
  }
}

.stats-row {
  margin-bottom: 20px;

  .stat-card {
    background: white;
    border-radius: 8px;
    padding: 20px;
    display: flex;
    align-items: center;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
    transition: all 0.3s ease;

    &:hover {
      transform: translateY(-2px);
      box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
    }

    .stat-icon {
      width: 50px;
      height: 50px;
      border-radius: 8px;
      display: flex;
      align-items: center;
      justify-content: center;
      margin-right: 16px;

      i {
        font-size: 24px;
        color: white;
      }

      &.pricing-policies {
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      }

      &.active-policies {
        background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
      }

      &.avg-margin {
        background: linear-gradient(135deg, #fa709a 0%, #fee140 100%);
      }

      &.price-adjustments {
        background: linear-gradient(135deg, #a8edea 0%, #fed6e3 100%);
      }
    }

    .stat-content {
      .stat-value {
        font-size: 24px;
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

.search-card, .table-card {
  margin-bottom: 20px;
}

.price-text {
  font-weight: 600;
  color: #f56c6c;
}

.margin-text {
  font-weight: 600;
  color: #67c23a;
}

.dialog-footer {
  text-align: right;
}
</style>
