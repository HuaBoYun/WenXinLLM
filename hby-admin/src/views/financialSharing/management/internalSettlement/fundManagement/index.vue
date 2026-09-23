<template>
  <div class="fund-management-container">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-left">
        <h1 class="page-title">
          <i class="el-icon-money"></i>
          资金管理
        </h1>
        <p class="page-description">管理内部资金的调配、利息计算和资金效率分析</p>
      </div>
      <div class="header-right">
        <el-button type="primary" icon="el-icon-plus" @click="handleAdd">
          新建资金调配
        </el-button>
        <el-button type="success" icon="el-icon-s-finance" @click="fundAllocation">
          资金调配
        </el-button>
        <el-button type="warning" icon="el-icon-data-analysis" @click="efficiencyAnalysis">
          效率分析
        </el-button>
      </div>
    </div>

    <!-- 统计卡片 -->
    <el-row :gutter="20" class="stats-row">
      <el-col :span="6">
        <div class="stat-card">
          <div class="stat-icon total-funds">
            <i class="el-icon-wallet"></i>
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ formatAmount(stats.totalFunds) }}</div>
            <div class="stat-label">资金池总额</div>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card">
          <div class="stat-icon allocated-funds">
            <i class="el-icon-s-finance"></i>
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ formatAmount(stats.allocatedFunds) }}</div>
            <div class="stat-label">已调配资金</div>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card">
          <div class="stat-icon interest-income">
            <i class="el-icon-coin"></i>
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ formatAmount(stats.interestIncome) }}</div>
            <div class="stat-label">利息收入</div>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card">
          <div class="stat-icon utilization-rate">
            <i class="el-icon-pie-chart"></i>
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ stats.utilizationRate }}%</div>
            <div class="stat-label">资金利用率</div>
          </div>
        </div>
      </el-col>
    </el-row>

    <!-- 查询条件 -->
    <el-card shadow="never" class="search-card">
      <el-form :model="queryForm" :inline="true" label-width="80px">
        <el-form-item label="调配单号">
          <el-input v-model="queryForm.allocationNo" placeholder="请输入调配单号" clearable />
        </el-form-item>
        <el-form-item label="调配类型">
          <el-select v-model="queryForm.allocationType" placeholder="请选择调配类型" clearable>
            <el-option label="内部借款" :value="1" />
            <el-option label="资金归集" :value="2" />
            <el-option label="资金下拨" :value="3" />
            <el-option label="利息结算" :value="4" />
          </el-select>
        </el-form-item>
        <el-form-item label="调配状态">
          <el-select v-model="queryForm.allocationStatus" placeholder="请选择调配状态" clearable>
            <el-option label="待审批" :value="1" />
            <el-option label="已审批" :value="2" />
            <el-option label="执行中" :value="3" />
            <el-option label="已完成" :value="4" />
            <el-option label="已取消" :value="5" />
          </el-select>
        </el-form-item>
        <el-form-item label="调配日期">
          <el-date-picker
            v-model="queryForm.allocationDateRange"
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
        :data="fundList"
        stripe
        border
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="allocationNo" label="调配单号" width="150" />
        <el-table-column prop="allocationType" label="调配类型" width="100">
          <template slot-scope="scope">
            <el-tag :type="getTypeTag(scope.row.allocationType)">
              {{ getTypeName(scope.row.allocationType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="fromCenterName" label="调出方" width="150" />
        <el-table-column prop="toCenterName" label="调入方" width="150" />
        <el-table-column prop="allocationAmount" label="调配金额" width="120" align="right">
          <template slot-scope="scope">
            <span class="amount-text">{{ formatAmount(scope.row.allocationAmount) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="interestRate" label="利率(%)" width="80" align="right">
          <template slot-scope="scope">
            <span class="rate-text">{{ scope.row.interestRate }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="allocationDate" label="调配日期" width="120" />
        <el-table-column prop="maturityDate" label="到期日期" width="120" />
        <el-table-column prop="allocationStatus" label="状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="getStatusTag(scope.row.allocationStatus)">
              {{ getStatusName(scope.row.allocationStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" type="text" @click="handleView(scope.row)">查看</el-button>
            <el-button size="mini" type="text" @click="handleEdit(scope.row)" v-if="scope.row.allocationStatus === 1">编辑</el-button>
            <el-button size="mini" type="text" @click="handleApprove(scope.row)" v-if="scope.row.allocationStatus === 1">审批</el-button>
            <el-button size="mini" type="text" @click="handleExecute(scope.row)" v-if="scope.row.allocationStatus === 2">执行</el-button>
            <el-button size="mini" type="text" @click="handleInterest(scope.row)" v-if="scope.row.allocationStatus === 3">计息</el-button>
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
            <el-form-item label="调配单号" prop="allocationNo">
              <el-input v-model="formData.allocationNo" placeholder="系统自动生成" :disabled="true" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="调配类型" prop="allocationType">
              <el-select v-model="formData.allocationType" placeholder="请选择调配类型">
                <el-option label="内部借款" :value="1" />
                <el-option label="资金归集" :value="2" />
                <el-option label="资金下拨" :value="3" />
                <el-option label="利息结算" :value="4" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="调出方" prop="fromCenterId">
              <el-select v-model="formData.fromCenterId" placeholder="请选择调出方" filterable>
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
            <el-form-item label="调入方" prop="toCenterId">
              <el-select v-model="formData.toCenterId" placeholder="请选择调入方" filterable>
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
            <el-form-item label="调配金额" prop="allocationAmount">
              <el-input-number
                v-model="formData.allocationAmount"
                :precision="2"
                :min="0"
                :max="999999999.99"
                placeholder="请输入调配金额"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="利率(%)" prop="interestRate">
              <el-input-number
                v-model="formData.interestRate"
                :precision="4"
                :min="0"
                :max="100"
                placeholder="请输入利率"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="调配日期" prop="allocationDate">
              <el-date-picker
                v-model="formData.allocationDate"
                type="date"
                placeholder="请选择调配日期"
                format="yyyy-MM-dd"
                value-format="yyyy-MM-dd"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="到期日期" prop="maturityDate">
              <el-date-picker
                v-model="formData.maturityDate"
                type="date"
                placeholder="请选择到期日期"
                format="yyyy-MM-dd"
                value-format="yyyy-MM-dd"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="调配说明" prop="allocationDesc">
          <el-input
            v-model="formData.allocationDesc"
            type="textarea"
            :rows="3"
            placeholder="请输入调配说明"
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
import { getFundManagementList, createFundManagement, updateFundManagement, deleteFundManagement, getFundManagementStats, getProfitCenterList, getFundManagementDetail, approveFundAllocation, executeFundAllocation, calculateInterest, analyzeFundEfficiency } from '@/api/financialSharing/internalSettlement'

export default {
  name: 'FundManagement',
  data() {
    return {
      loading: false,
      submitLoading: false,
      fundList: [],
      selectedRows: [],
      centerList: [],
      stats: {
        totalFunds: 0,
        allocatedFunds: 0,
        interestIncome: 0,
        utilizationRate: 0
      },
      queryForm: {
        allocationNo: '',
        allocationType: '',
        allocationStatus: '',
        allocationDateRange: []
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
        allocationId: null,
        allocationNo: '',
        allocationType: '',
        fromCenterId: '',
        toCenterId: '',
        allocationAmount: 0,
        interestRate: 0,
        allocationDate: '',
        maturityDate: '',
        allocationDesc: '',
        remark: ''
      },
      formRules: {
        allocationType: [{ required: true, message: '请选择调配类型', trigger: 'change' }],
        fromCenterId: [{ required: true, message: '请选择调出方', trigger: 'change' }],
        toCenterId: [{ required: true, message: '请选择调入方', trigger: 'change' }],
        allocationAmount: [{ required: true, message: '请输入调配金额', trigger: 'blur' }],
        interestRate: [{ required: true, message: '请输入利率', trigger: 'blur' }],
        allocationDate: [{ required: true, message: '请选择调配日期', trigger: 'change' }]
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
        if (this.queryForm.allocationDateRange && this.queryForm.allocationDateRange.length === 2) {
          params.startDate = this.queryForm.allocationDateRange[0]
          params.endDate = this.queryForm.allocationDateRange[1]
        }
        const response = await getFundManagementList(params)
        if (response.code === 1) {
          this.fundList = response.data.records || []
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
        const response = await getFundManagementStats()
        if (response.code === 1) {
          this.stats = response.data || {
            totalFunds: 0,
            allocatedFunds: 0,
            interestIncome: 0,
            utilizationRate: 0
          }
        }
      } catch (error) {
        console.error('获取资金管理统计数据失败', error)
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
        allocationNo: '',
        allocationType: '',
        allocationStatus: '',
        allocationDateRange: []
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
      this.dialogTitle = '新建资金调配'
      this.isEdit = false
      this.resetForm()
      this.dialogVisible = true
    },
    handleEdit(row) {
      this.dialogTitle = '编辑资金调配'
      this.isEdit = true
      this.formData = { ...row }
      this.dialogVisible = true
    },
    async handleView(row) {
      try {
        const response = await getFundManagementDetail(row.allocationId)
        if (response.code === 1) {
          const data = response.data || row
          const content = `
            <p><b>调配编号:</b> ${data.allocationId || ''}</p>
            <p><b>调配金额:</b> ${data.amount || ''}</p>
            <p><b>状态:</b> ${data.status || ''}</p>
            <p><b>调出中心:</b> ${data.fromCenter || ''}</p>
            <p><b>调入中心:</b> ${data.toCenter || ''}</p>
            <p><b>利率:</b> ${data.interestRate || ''}</p>
            <p><b>创建时间:</b> ${data.createTime || ''}</p>
            <p><b>备注:</b> ${data.remark || ''}</p>
          `
          this.$alert(content, '资金调配详情', { dangerouslyUseHTMLString: true })
        } else {
          this.$message.error(response.msg || '获取详情失败')
        }
      } catch (error) {
        const content = `
          <p><b>调配编号:</b> ${row.allocationId || ''}</p>
          <p><b>调配金额:</b> ${row.amount || ''}</p>
          <p><b>状态:</b> ${row.status || ''}</p>
        `
        this.$alert(content, '资金调配详情', { dangerouslyUseHTMLString: true })
      }
    },
    async handleApprove(row) {
      try {
        await this.$confirm('确认审批通过该资金调配？', '审批确认', { type: 'warning' })
        const response = await approveFundAllocation(row.allocationId, { status: 'approved' })
        if (response.code === 1) {
          this.$message.success('审批成功')
          this.fetchData()
        } else {
          this.$message.error(response.msg || '审批失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('审批操作失败')
        }
      }
    },
    async handleExecute(row) {
      try {
        await this.$confirm('确认执行该资金调配？执行后将进行实际资金划转。', '执行确认', { type: 'warning' })
        const response = await executeFundAllocation(row.allocationId, {})
        if (response.code === 1) {
          this.$message.success('执行成功')
          this.fetchData()
        } else {
          this.$message.error(response.msg || '执行失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('执行操作失败')
        }
      }
    },
    async handleInterest(row) {
      try {
        const response = await calculateInterest(row.allocationId, {})
        if (response.code === 1) {
          const data = response.data || {}
          const content = `
            <p><b>调配编号:</b> ${row.allocationId || ''}</p>
            <p><b>本金:</b> ${data.principal || row.amount || ''}</p>
            <p><b>利率:</b> ${data.interestRate || ''}</p>
            <p><b>计息天数:</b> ${data.days || ''}</p>
            <p><b>利息金额:</b> ${data.interest || ''}</p>
          `
          this.$alert(content, '计息结果', { dangerouslyUseHTMLString: true })
        } else {
          this.$message.error(response.msg || '计息失败')
        }
      } catch (error) {
        this.$message.error('计息操作失败')
      }
    },
    fundAllocation() {
      this.$message.info('请在列表中选择记录后进行资金调配操作，或点击"新建资金调配"创建新的调配单')
    },
    async efficiencyAnalysis() {
      try {
        const response = await analyzeFundEfficiency({})
        if (response.code === 1) {
          const data = response.data || {}
          const content = `
            <p><b>资金利用率:</b> ${data.utilizationRate || '--'}%</p>
            <p><b>平均调配周期:</b> ${data.avgCycle || '--'} 天</p>
            <p><b>资金周转率:</b> ${data.turnoverRate || '--'}</p>
            <p><b>收益率:</b> ${data.profitRate || '--'}%</p>
          `
          this.$alert(content, '资金效率分析', { dangerouslyUseHTMLString: true })
        } else {
          this.$message.error(response.msg || '效率分析失败')
        }
      } catch (error) {
        this.$message.error('效率分析操作失败')
      }
    },
    async handleSubmit() {
      try {
        await this.$refs.form.validate()
        this.submitLoading = true
        
        const response = this.isEdit 
          ? await updateFundManagement(this.formData)
          : await createFundManagement(this.formData)
          
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
        allocationId: null,
        allocationNo: '',
        allocationType: '',
        fromCenterId: '',
        toCenterId: '',
        allocationAmount: 0,
        interestRate: 0,
        allocationDate: '',
        maturityDate: '',
        allocationDesc: '',
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
        1: '内部借款',
        2: '资金归集',
        3: '资金下拨',
        4: '利息结算'
      }
      return types[type] || '未知'
    },
    getTypeTag(type) {
      const tags = {
        1: 'primary',
        2: 'success',
        3: 'warning',
        4: 'info'
      }
      return tags[type] || ''
    },
    getStatusName(status) {
      const statuses = {
        1: '待审批',
        2: '已审批',
        3: '执行中',
        4: '已完成',
        5: '已取消'
      }
      return statuses[status] || '未知'
    },
    getStatusTag(status) {
      const tags = {
        1: 'warning',
        2: 'primary',
        3: 'info',
        4: 'success',
        5: 'danger'
      }
      return tags[status] || ''
    }
  }
}
</script>

<style lang="scss" scoped>
.fund-management-container {
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
        color: #909399;
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

      &.total-funds {
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      }

      &.allocated-funds {
        background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
      }

      &.interest-income {
        background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
      }

      &.utilization-rate {
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

.rate-text {
  font-weight: 600;
  color: #67c23a;
}

.dialog-footer {
  text-align: right;
}
</style>
