<template>
  <div class="internal-transaction-container">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-left">
        <h1 class="page-title">
          <i class="el-icon-s-cooperation"></i>
          内部交易管理
        </h1>
        <p class="page-description">管理企业内部各单位间的交易单据、审批流程和对账处理</p>
      </div>
      <div class="header-right">
        <el-button type="primary" icon="el-icon-plus" @click="handleAdd">
          新建交易
        </el-button>
        <el-button type="success" icon="el-icon-check" @click="batchApprove">
          批量审批
        </el-button>
        <el-button type="warning" icon="el-icon-s-finance" @click="batchSettle">
          批量结算
        </el-button>
      </div>
    </div>

    <!-- 查询条件 -->
    <el-card shadow="never" class="search-card">
      <el-form :model="queryForm" :inline="true" label-width="80px">
        <el-form-item label="交易单号">
          <el-input v-model="queryForm.transactionNo" placeholder="请输入交易单号" clearable />
        </el-form-item>
        <el-form-item label="交易类型">
          <el-select v-model="queryForm.transactionType" placeholder="请选择交易类型" clearable>
            <el-option label="商品销售" :value="1" />
            <el-option label="服务提供" :value="2" />
            <el-option label="资产转让" :value="3" />
            <el-option label="资金调拨" :value="4" />
            <el-option label="费用分摊" :value="5" />
          </el-select>
        </el-form-item>
        <el-form-item label="交易状态">
          <el-select v-model="queryForm.transactionStatus" placeholder="请选择交易状态" clearable>
            <el-option label="草稿" :value="1" />
            <el-option label="待审批" :value="2" />
            <el-option label="已审批" :value="3" />
            <el-option label="已确认" :value="4" />
            <el-option label="已结算" :value="5" />
            <el-option label="已取消" :value="6" />
          </el-select>
        </el-form-item>
        <el-form-item label="交易日期">
          <el-date-picker
            v-model="queryForm.transactionDateRange"
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
        :data="transactionList"
        stripe
        border
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="transactionNo" label="交易单号" width="150" />
        <el-table-column prop="transactionType" label="交易类型" width="120">
          <template slot-scope="scope">
            <el-tag :type="getTransactionTypeTag(scope.row.transactionType)">
              {{ getTransactionTypeName(scope.row.transactionType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="fromCenterName" label="交易方" width="150" />
        <el-table-column prop="toCenterName" label="接收方" width="150" />
        <el-table-column prop="transactionAmount" label="交易金额" width="120" align="right">
          <template slot-scope="scope">
            <span class="amount-text">{{ formatAmount(scope.row.transactionAmount) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="transactionDate" label="交易日期" width="120" />
        <el-table-column prop="transactionStatus" label="状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="getStatusTag(scope.row.transactionStatus)">
              {{ getStatusName(scope.row.transactionStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="150" />
        <el-table-column label="操作" width="200" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" type="text" @click="handleView(scope.row)">查看</el-button>
            <el-button size="mini" type="text" @click="handleEdit(scope.row)" v-if="scope.row.transactionStatus === 1">编辑</el-button>
            <el-button size="mini" type="text" @click="handleApprove(scope.row)" v-if="scope.row.transactionStatus === 2">审批</el-button>
            <el-button size="mini" type="text" @click="handleConfirm(scope.row)" v-if="scope.row.transactionStatus === 3">确认</el-button>
            <el-button size="mini" type="text" @click="handleSettle(scope.row)" v-if="scope.row.transactionStatus === 4">结算</el-button>
            <el-button size="mini" type="text" @click="handleDelete(scope.row)" v-if="scope.row.transactionStatus === 1">删除</el-button>
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
            <el-form-item label="交易单号" prop="transactionNo">
              <el-input v-model="formData.transactionNo" placeholder="系统自动生成" :disabled="true" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="交易类型" prop="transactionType">
              <el-select v-model="formData.transactionType" placeholder="请选择交易类型">
                <el-option label="商品销售" :value="1" />
                <el-option label="服务提供" :value="2" />
                <el-option label="资产转让" :value="3" />
                <el-option label="资金调拨" :value="4" />
                <el-option label="费用分摊" :value="5" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="交易方" prop="fromCenterId">
              <el-select v-model="formData.fromCenterId" placeholder="请选择交易方" filterable>
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
            <el-form-item label="接收方" prop="toCenterId">
              <el-select v-model="formData.toCenterId" placeholder="请选择接收方" filterable>
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
            <el-form-item label="交易金额" prop="transactionAmount">
              <el-input-number
                v-model="formData.transactionAmount"
                :precision="2"
                :min="0"
                :max="999999999.99"
                placeholder="请输入交易金额"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="交易日期" prop="transactionDate">
              <el-date-picker
                v-model="formData.transactionDate"
                type="date"
                placeholder="请选择交易日期"
                format="yyyy-MM-dd"
                value-format="yyyy-MM-dd"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="交易描述" prop="transactionDesc">
          <el-input
            v-model="formData.transactionDesc"
            type="textarea"
            :rows="3"
            placeholder="请输入交易描述"
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
import { getInternalTransactionList, createInternalTransaction, updateInternalTransaction, deleteInternalTransaction, getProfitCenterList, auditInternalTransaction, confirmInternalTransactionExtended, settleInternalTransaction } from '@/api/financialSharing/internalSettlement'

export default {
  name: 'InternalTransaction',
  data() {
    return {
      loading: false,
      submitLoading: false,
      transactionList: [],
      selectedRows: [],
      centerList: [],
      queryForm: {
        transactionNo: '',
        transactionType: '',
        transactionStatus: '',
        transactionDateRange: []
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
        transactionId: null,
        transactionNo: '',
        transactionType: '',
        fromCenterId: '',
        toCenterId: '',
        transactionAmount: 0,
        transactionDate: '',
        transactionDesc: '',
        remark: ''
      },
      formRules: {
        transactionType: [{ required: true, message: '请选择交易类型', trigger: 'change' }],
        fromCenterId: [{ required: true, message: '请选择交易方', trigger: 'change' }],
        toCenterId: [{ required: true, message: '请选择接收方', trigger: 'change' }],
        transactionAmount: [{ required: true, message: '请输入交易金额', trigger: 'blur' }],
        transactionDate: [{ required: true, message: '请选择交易日期', trigger: 'change' }]
      }
    }
  },
  mounted() {
    this.fetchData()
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
        if (this.queryForm.transactionDateRange && this.queryForm.transactionDateRange.length === 2) {
          params.startDate = this.queryForm.transactionDateRange[0]
          params.endDate = this.queryForm.transactionDateRange[1]
        }
        const response = await getInternalTransactionList(params)
        if (response.code === 1) {
          this.transactionList = response.data.records || []
          this.pagination.total = response.data.total || 0
        }
      } catch (error) {
        this.$message.error('获取数据失败')
      } finally {
        this.loading = false
      }
    },
    async loadCenterList() {
      try {
        const response = await getProfitCenterList({})
        if (response.code === 1) {
          const data = response.data
          this.centerList = Array.isArray(data) ? data : (data.records || data.tlist || data.list || [])
        } else {
          console.error('获取责任中心列表失败:', response.msg)
        }
      } catch (error) {
        console.error('获取责任中心列表失败:', error)
      }
    },
    handleQuery() {
      this.pagination.currentPage = 1
      this.fetchData()
    },
    resetQuery() {
      this.queryForm = {
        transactionNo: '',
        transactionType: '',
        transactionStatus: '',
        transactionDateRange: []
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
      this.dialogTitle = '新建内部交易'
      this.isEdit = false
      this.resetForm()
      this.dialogVisible = true
    },
    handleEdit(row) {
      this.dialogTitle = '编辑内部交易'
      this.isEdit = true
      this.formData = { ...row }
      this.dialogVisible = true
    },
    handleView(row) {
      const content = `
        <p><b>交易单号：</b>${row.transactionNo || ''}</p>
        <p><b>交易类型：</b>${row.transactionType || ''}</p>
        <p><b>发起方：</b>${row.initiatorName || ''}</p>
        <p><b>接收方：</b>${row.receiverName || ''}</p>
        <p><b>金额：</b>${row.amount || 0}</p>
        <p><b>状态：</b>${row.statusName || ''}</p>
        <p><b>创建时间：</b>${row.createTime || ''}</p>
      `
      this.$alert(content, '交易详情', { dangerouslyUseHTMLString: true })
    },
    async handleApprove(row) {
      try {
        await this.$confirm('确认审批通过该交易记录？', '审批确认', { type: 'warning' })
        const response = await auditInternalTransaction(row.transactionId, { status: 'approved' })
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
    async handleConfirm(row) {
      try {
        await this.$confirm('确认该交易记录？', '确认操作', { type: 'warning' })
        const response = await confirmInternalTransactionExtended(row.transactionId, {})
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
    async handleSettle(row) {
      try {
        await this.$confirm('确认对该交易进行结算？', '结算确认', { type: 'warning' })
        const response = await settleInternalTransaction(row.transactionId, {})
        if (response.code === 1) {
          this.$message.success('结算成功')
          this.fetchData()
        } else {
          this.$message.error(response.msg || '结算失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('操作失败')
        }
      }
    },
    async handleDelete(row) {
      try {
        await this.$confirm('确定要删除这条记录吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        const response = await deleteInternalTransaction(row.transactionId)
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
            const response = await auditInternalTransaction(row.transactionId, { status: 'approved' })
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
    async batchSettle() {
      if (this.selectedRows.length === 0) {
        this.$message.warning('请选择要结算的记录')
        return
      }
      try {
        await this.$confirm(`确认批量结算选中的 ${this.selectedRows.length} 条记录？`, '批量结算确认', { type: 'warning' })
        let successCount = 0
        let failCount = 0
        for (const row of this.selectedRows) {
          try {
            const response = await settleInternalTransaction(row.transactionId, {})
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
    async handleSubmit() {
      try {
        await this.$refs.form.validate()
        this.submitLoading = true
        
        const response = this.isEdit 
          ? await updateInternalTransaction(this.formData)
          : await createInternalTransaction(this.formData)
          
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
        transactionId: null,
        transactionNo: '',
        transactionType: '',
        fromCenterId: '',
        toCenterId: '',
        transactionAmount: 0,
        transactionDate: '',
        transactionDesc: '',
        remark: ''
      }
      if (this.$refs.form) {
        this.$refs.form.resetFields()
      }
    },
    formatAmount(amount) {
      return amount ? `¥${amount.toLocaleString()}` : '¥0.00'
    },
    getTransactionTypeName(type) {
      const types = {
        1: '商品销售',
        2: '服务提供', 
        3: '资产转让',
        4: '资金调拨',
        5: '费用分摊'
      }
      return types[type] || '未知'
    },
    getTransactionTypeTag(type) {
      const tags = {
        1: 'success',
        2: 'primary',
        3: 'warning',
        4: 'info',
        5: 'danger'
      }
      return tags[type] || ''
    },
    getStatusName(status) {
      const statuses = {
        1: '草稿',
        2: '待审批',
        3: '已审批',
        4: '已确认',
        5: '已结算',
        6: '已取消'
      }
      return statuses[status] || '未知'
    },
    getStatusTag(status) {
      const tags = {
        1: 'info',
        2: 'warning',
        3: 'primary',
        4: 'success',
        5: 'success',
        6: 'danger'
      }
      return tags[status] || ''
    }
  }
}
</script>

<style lang="scss" scoped>
.internal-transaction-container {
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
      margin-left: 8px;
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
