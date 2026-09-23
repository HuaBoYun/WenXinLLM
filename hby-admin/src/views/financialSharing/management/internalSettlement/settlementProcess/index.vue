<template>
  <div class="settlement-process-container">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-left">
        <h1 class="page-title">
          <i class="el-icon-s-finance"></i>
          内部结算处理
        </h1>
        <p class="page-description">执行内部交易的结算处理、规则设置和凭证生成</p>
      </div>
      <div class="header-right">
        <el-button type="primary" icon="el-icon-plus" @click="handleAdd">
          新建结算规则
        </el-button>
        <el-button type="success" icon="el-icon-s-finance" @click="executeSettlement">
          执行结算
        </el-button>
        <el-button type="warning" icon="el-icon-document" @click="generateVoucher">
          生成凭证
        </el-button>
      </div>
    </div>

    <!-- 统计卡片 -->
    <el-row :gutter="20" class="stats-row">
      <el-col :span="6">
        <div class="stat-card">
          <div class="stat-icon settlement-rules">
            <i class="el-icon-setting"></i>
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ stats.settlementRules }}</div>
            <div class="stat-label">结算规则数</div>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card">
          <div class="stat-icon pending-settlement">
            <i class="el-icon-time"></i>
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ stats.pendingSettlement }}</div>
            <div class="stat-label">待结算笔数</div>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card">
          <div class="stat-icon settlement-amount">
            <i class="el-icon-coin"></i>
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ formatAmount(stats.settlementAmount) }}</div>
            <div class="stat-label">本月结算金额</div>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card">
          <div class="stat-icon voucher-count">
            <i class="el-icon-document"></i>
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ stats.voucherCount }}</div>
            <div class="stat-label">生成凭证数</div>
          </div>
        </div>
      </el-col>
    </el-row>

    <!-- 查询条件 -->
    <el-card shadow="never" class="search-card">
      <el-form :model="queryForm" :inline="true" label-width="80px">
        <el-form-item label="结算单号">
          <el-input v-model="queryForm.settlementNo" placeholder="请输入结算单号" clearable />
        </el-form-item>
        <el-form-item label="结算类型">
          <el-select v-model="queryForm.settlementType" placeholder="请选择结算类型" clearable>
            <el-option label="成本分摊" :value="1" />
            <el-option label="利润分配" :value="2" />
            <el-option label="资金调拨" :value="3" />
            <el-option label="费用分摊" :value="4" />
          </el-select>
        </el-form-item>
        <el-form-item label="结算状态">
          <el-select v-model="queryForm.settlementStatus" placeholder="请选择结算状态" clearable>
            <el-option label="待结算" :value="1" />
            <el-option label="已结算" :value="2" />
            <el-option label="已确认" :value="3" />
            <el-option label="已取消" :value="4" />
          </el-select>
        </el-form-item>
        <el-form-item label="结算日期">
          <el-date-picker
            v-model="queryForm.settlementDateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            format="yyyy-MM-dd"
            value-format="yyyy-MM-dd"
          />
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
        :data="settlementList"
        stripe
        border
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="settlementNo" label="结算单号" width="150" />
        <el-table-column prop="settlementType" label="结算类型" width="100">
          <template slot-scope="scope">
            <el-tag :type="getTypeTag(scope.row.settlementType)">
              {{ getTypeName(scope.row.settlementType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="fromCenterName" label="结算方" width="150" />
        <el-table-column prop="toCenterName" label="被结算方" width="150" />
        <el-table-column prop="settlementAmount" label="结算金额" width="120" align="right">
          <template slot-scope="scope">
            <span class="amount-text">{{ formatAmount(scope.row.settlementAmount) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="settlementDate" label="结算日期" width="120" />
        <el-table-column prop="settlementBasis" label="结算依据" width="150" show-overflow-tooltip />
        <el-table-column prop="settlementStatus" label="状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="getStatusTag(scope.row.settlementStatus)">
              {{ getStatusName(scope.row.settlementStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="voucherId" label="凭证号" width="120" />
        <el-table-column label="操作" width="200" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" type="text" @click="handleView(scope.row)">查看</el-button>
            <el-button size="mini" type="text" @click="handleEdit(scope.row)" v-if="scope.row.settlementStatus === 1">编辑</el-button>
            <el-button size="mini" type="text" @click="handleSettle(scope.row)" v-if="scope.row.settlementStatus === 1">结算</el-button>
            <el-button size="mini" type="text" @click="handleConfirm(scope.row)" v-if="scope.row.settlementStatus === 2">确认</el-button>
            <el-button size="mini" type="text" @click="handleVoucher(scope.row)" v-if="scope.row.settlementStatus === 3 && !scope.row.voucherId">生成凭证</el-button>
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
      width="800px"
      :close-on-click-modal="false"
    >
      <el-form :model="formData" :rules="formRules" ref="form" label-width="120px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="结算单号" prop="settlementNo">
              <el-input v-model="formData.settlementNo" placeholder="系统自动生成" :disabled="true" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="结算类型" prop="settlementType">
              <el-select v-model="formData.settlementType" placeholder="请选择结算类型">
                <el-option label="成本分摊" :value="1" />
                <el-option label="利润分配" :value="2" />
                <el-option label="资金调拨" :value="3" />
                <el-option label="费用分摊" :value="4" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="结算方" prop="fromCenterId">
              <el-select v-model="formData.fromCenterId" placeholder="请选择结算方" filterable>
                <el-option
                  v-for="center in centerList"
                  :key="center.centerId"
                  :label="center.centerName"
                  :value="center.centerId"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="被结算方" prop="toCenterId">
              <el-select v-model="formData.toCenterId" placeholder="请选择被结算方" filterable>
                <el-option
                  v-for="center in centerList"
                  :key="center.centerId"
                  :label="center.centerName"
                  :value="center.centerId"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="结算金额" prop="settlementAmount">
              <el-input-number
                v-model="formData.settlementAmount"
                :precision="2"
                :min="0"
                :max="999999999.99"
                placeholder="请输入结算金额"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="结算日期" prop="settlementDate">
              <el-date-picker
                v-model="formData.settlementDate"
                type="date"
                placeholder="请选择结算日期"
                format="yyyy-MM-dd"
                value-format="yyyy-MM-dd"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="结算依据" prop="settlementBasis">
          <el-input
            v-model="formData.settlementBasis"
            type="textarea"
            :rows="3"
            placeholder="请输入结算依据"
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
import { getSettlementProcessList, createSettlementProcess, updateSettlementProcess, deleteSettlementProcess, getSettlementProcessStats, getProfitCenterList, executeSettlement, confirmSettlementResult, generateSettlementVoucher, getSettlementProcessDetail } from '@/api/financialSharing/internalSettlement'

export default {
  name: 'SettlementProcess',
  data() {
    return {
      loading: false,
      submitLoading: false,
      settlementList: [],
      selectedRows: [],
      centerList: [],
      stats: {
        settlementRules: 0,
        pendingSettlement: 0,
        settlementAmount: 0,
        voucherCount: 0
      },
      queryForm: {
        settlementNo: '',
        settlementType: '',
        settlementStatus: '',
        settlementDateRange: []
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
        settlementId: null,
        settlementNo: '',
        settlementType: '',
        fromCenterId: '',
        toCenterId: '',
        settlementAmount: 0,
        settlementDate: '',
        settlementBasis: '',
        remark: ''
      },
      formRules: {
        settlementType: [{ required: true, message: '请选择结算类型', trigger: 'change' }],
        fromCenterId: [{ required: true, message: '请选择结算方', trigger: 'change' }],
        toCenterId: [{ required: true, message: '请选择被结算方', trigger: 'change' }],
        settlementAmount: [{ required: true, message: '请输入结算金额', trigger: 'blur' }],
        settlementDate: [{ required: true, message: '请选择结算日期', trigger: 'change' }],
        settlementBasis: [{ required: true, message: '请输入结算依据', trigger: 'blur' }]
      }
    }
  },
  mounted() {
    this.fetchData()
    this.loadStats()
    this.loadCenterList()
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
        if (this.queryForm.settlementDateRange && this.queryForm.settlementDateRange.length === 2) {
          params.startDate = this.queryForm.settlementDateRange[0]
          params.endDate = this.queryForm.settlementDateRange[1]
        }
        const response = await getSettlementProcessList(params)
        if (response.code === 1) {
          this.settlementList = response.data.records || []
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
        const response = await getSettlementProcessStats()
        if (response.code === 1) {
          this.stats = response.data || {
            settlementRules: 0,
            pendingSettlement: 0,
            settlementAmount: 0,
            voucherCount: 0
          }
        }
      } catch (error) {
        console.error('获取结算处理统计数据失败', error)
      }
    },
    async loadCenterList() {
      try {
        const response = await getProfitCenterList()
        if (response.code === 1) {
          const data = response.data
          this.centerList = Array.isArray(data) ? data : (data.records || data.tlist || data.list || [])
        }
      } catch (error) {
        console.error('获取责任中心列表失败', error)
      }
    },
    handleQuery() {
      this.pagination.currentPage = 1
      this.fetchData()
    },
    resetQuery() {
      this.queryForm = {
        settlementNo: '',
        settlementType: '',
        settlementStatus: '',
        settlementDateRange: []
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
      this.dialogTitle = '新建结算规则'
      this.isEdit = false
      this.resetForm()
      this.dialogVisible = true
    },
    handleEdit(row) {
      this.dialogTitle = '编辑结算规则'
      this.isEdit = true
      this.formData = { ...row }
      this.dialogVisible = true
    },
    handleView(row) {
      const content = `
        <p><b>结算编号：</b>${row.settlementNo || ''}</p>
        <p><b>结算规则：</b>${row.ruleName || ''}</p>
        <p><b>结算周期：</b>${row.settlementPeriod || ''}</p>
        <p><b>结算金额：</b>${row.settlementAmount || 0}</p>
        <p><b>状态：</b>${row.statusName || ''}</p>
        <p><b>创建时间：</b>${row.createTime || ''}</p>
      `
      this.$alert(content, '结算详情', { dangerouslyUseHTMLString: true })
    },
    async handleSettle(row) {
      try {
        await this.$confirm('确认执行该结算规则？', '执行结算确认', { type: 'warning' })
        const response = await executeSettlement({ settlementId: row.settlementId })
        if (response.code === 1) {
          this.$message.success('结算执行成功')
          this.fetchData()
        } else {
          this.$message.error(response.msg || '结算执行失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('操作失败')
        }
      }
    },
    async handleConfirm(row) {
      try {
        await this.$confirm('确认该结算结果？', '确认操作', { type: 'warning' })
        const response = await confirmSettlementResult(row.settlementId, {})
        if (response.code === 1) {
          this.$message.success('确认成功')
          this.fetchData()
        } else {
          this.$message.error(response.msg || '确认失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('操作失败')
        }
      }
    },
    async handleVoucher(row) {
      try {
        await this.$confirm('确认为该结算生成凭证？', '生成凭证确认', { type: 'warning' })
        const response = await generateSettlementVoucher(row.settlementId)
        if (response.code === 1) {
          this.$message.success('凭证生成成功')
          this.fetchData()
        } else {
          this.$message.error(response.msg || '凭证生成失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('操作失败')
        }
      }
    },
    async executeSettlement() {
      if (this.selectedRows.length === 0) {
        this.$message.warning('请选择要执行结算的记录')
        return
      }
      try {
        await this.$confirm(`确认执行选中的 ${this.selectedRows.length} 条结算？`, '批量执行结算', { type: 'warning' })
        let successCount = 0
        let failCount = 0
        for (const row of this.selectedRows) {
          try {
            const response = await executeSettlement({ settlementId: row.settlementId })
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
          this.$message.success(`批量结算完成，共 ${successCount} 条`)
        } else {
          this.$message.warning(`批量结算完成：成功 ${successCount} 条，失败 ${failCount} 条`)
        }
        this.fetchData()
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('批量结算失败')
        }
      }
    },
    async generateVoucher() {
      if (this.selectedRows.length === 0) {
        this.$message.warning('请选择要生成凭证的记录')
        return
      }
      try {
        await this.$confirm(`确认为选中的 ${this.selectedRows.length} 条记录生成凭证？`, '批量生成凭证', { type: 'warning' })
        let successCount = 0
        let failCount = 0
        for (const row of this.selectedRows) {
          try {
            const response = await generateSettlementVoucher(row.settlementId)
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
          this.$message.success(`批量生成凭证完成，共 ${successCount} 条`)
        } else {
          this.$message.warning(`批量生成凭证完成：成功 ${successCount} 条，失败 ${failCount} 条`)
        }
        this.fetchData()
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('批量生成凭证失败')
        }
      }
    },
    async handleSubmit() {
      try {
        await this.$refs.form.validate()
        this.submitLoading = true
        
        const response = this.isEdit 
          ? await updateSettlementProcess(this.formData)
          : await createSettlementProcess(this.formData)
          
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
        settlementId: null,
        settlementNo: '',
        settlementType: '',
        fromCenterId: '',
        toCenterId: '',
        settlementAmount: 0,
        settlementDate: '',
        settlementBasis: '',
        remark: ''
      }
      if (this.$refs.form) {
        this.$refs.form.resetFields()
      }
    },
    formatAmount(amount) {
      return amount ? `¥${(amount / 10000).toFixed(2)}万` : '¥0.00万'
    },
    getTypeName(type) {
      const types = {
        1: '成本分摊',
        2: '利润分配',
        3: '资金调拨',
        4: '费用分摊'
      }
      return types[type] || '未知'
    },
    getTypeTag(type) {
      const tags = {
        1: 'success',
        2: 'primary',
        3: 'warning',
        4: 'info'
      }
      return tags[type] || ''
    },
    getStatusName(status) {
      const statuses = {
        1: '待结算',
        2: '已结算',
        3: '已确认',
        4: '已取消'
      }
      return statuses[status] || '未知'
    },
    getStatusTag(status) {
      const tags = {
        1: 'warning',
        2: 'primary',
        3: 'success',
        4: 'danger'
      }
      return tags[status] || ''
    }
  }
}
</script>

<style lang="scss" scoped>
.settlement-process-container {
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
        color: #f56c6c;
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

      &.settlement-rules {
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      }

      &.pending-settlement {
        background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
      }

      &.settlement-amount {
        background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
      }

      &.voucher-count {
        background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
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

.amount-text {
  font-weight: 600;
  color: #f56c6c;
}

.dialog-footer {
  text-align: right;
}
</style>
