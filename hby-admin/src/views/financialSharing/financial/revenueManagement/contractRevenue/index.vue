<template>
  <div class="contract-revenue-container">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-left">
        <h1 class="page-title">
          <i class="el-icon-document"></i>
          合同收入管理
        </h1>
        <p class="page-description">管理基于合同的收入确认和核算，包括履约义务识别和收入进度确认</p>
      </div>
      <div class="header-right">
        <el-button type="primary" icon="el-icon-plus" @click="showCreateDialog">
          新增合同收入
        </el-button>
        <el-button type="success" icon="el-icon-view" @click="identifyObligations">
          识别履约义务
        </el-button>
        <el-button type="warning" icon="el-icon-refresh" @click="refreshData">
          刷新数据
        </el-button>
      </div>
    </div>

    <!-- 合同收入统计 -->
    <div class="contract-stats">
      <el-row :gutter="24">
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon total">
              <i class="el-icon-document"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ stats.totalContracts }}</div>
              <div class="stat-label">合同总数</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon amount">
              <i class="el-icon-money"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ formatAmount(stats.totalAmount) }}</div>
              <div class="stat-label">合同总额</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon recognized">
              <i class="el-icon-check"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ formatAmount(stats.recognizedAmount) }}</div>
              <div class="stat-label">已确认收入</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon progress">
              <i class="el-icon-data-line"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ stats.avgProgress }}%</div>
              <div class="stat-label">平均进度</div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 搜索区域 -->
    <div class="search-area">
      <el-form :model="searchForm" :inline="true" size="small">
        <el-form-item label="合同编号">
          <el-input v-model="searchForm.contractNo" placeholder="请输入合同编号" clearable />
        </el-form-item>
        <el-form-item label="合同名称">
          <el-input v-model="searchForm.contractName" placeholder="请输入合同名称" clearable />
        </el-form-item>
        <el-form-item label="客户名称">
          <el-input v-model="searchForm.customerName" placeholder="请输入客户名称" clearable />
        </el-form-item>
        <el-form-item label="合同状态">
          <el-select v-model="searchForm.contractStatus" placeholder="请选择合同状态" clearable>
            <el-option label="生效" :value="1" />
            <el-option label="履行中" :value="2" />
            <el-option label="已完成" :value="3" />
            <el-option label="已终止" :value="4" />
          </el-select>
        </el-form-item>
        <el-form-item label="确认方法">
          <el-select v-model="searchForm.recognitionMethod" placeholder="请选择确认方法" clearable>
            <el-option label="按时间" :value="1" />
            <el-option label="按进度" :value="2" />
            <el-option label="按事件" :value="3" />
          </el-select>
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
        <el-table-column prop="contractNo" label="合同编号" width="150" />
        <el-table-column prop="contractName" label="合同名称" width="200" show-overflow-tooltip />
        <el-table-column prop="customerName" label="客户名称" width="150" />
        <el-table-column prop="contractAmount" label="合同金额" width="120" align="right">
          <template slot-scope="scope">
            <span class="amount-text">{{ formatAmount(scope.row.contractAmount) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="recognizedAmount" label="已确认收入" width="130" align="right">
          <template slot-scope="scope">
            <span class="amount-text">{{ formatAmount(scope.row.recognizedAmount) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="remainingAmount" label="剩余收入" width="120" align="right">
          <template slot-scope="scope">
            <span class="amount-text">{{ formatAmount(scope.row.remainingAmount) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="performanceProgress" label="履约进度" width="100">
          <template slot-scope="scope">
            <el-progress 
              :percentage="scope.row.performanceProgress" 
              :stroke-width="8"
              :show-text="false"
            />
            <span style="margin-left: 8px;">{{ scope.row.performanceProgress }}%</span>
          </template>
        </el-table-column>
        <el-table-column prop="contractStatus" label="合同状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="getContractStatusType(scope.row.contractStatus)">
              {{ getContractStatusText(scope.row.contractStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="recognitionMethodName" label="确认方法" width="100" />
        <el-table-column prop="signDate" label="签订日期" width="120" />
        <el-table-column label="操作" width="250" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" @click="viewDetail(scope.row)">详情</el-button>
            <el-button size="mini" type="success" @click="viewObligations(scope.row)">履约义务</el-button>
            <el-button size="mini" type="warning" @click="updateProgress(scope.row)">更新进度</el-button>
            <el-button size="mini" type="info" @click="viewRecognition(scope.row)">确认记录</el-button>
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

    <!-- 创建合同收入对话框 -->
    <el-dialog
      title="新增合同收入"
      :visible.sync="createDialogVisible"
      width="800px"
      :close-on-click-modal="false"
    >
      <el-form :model="createForm" :rules="createRules" ref="createForm" label-width="120px">
        <el-row :gutter="24">
          <el-col :span="12">
            <el-form-item label="合同编号" prop="contractNo">
              <el-input v-model="createForm.contractNo" placeholder="请输入合同编号" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="合同名称" prop="contractName">
              <el-input v-model="createForm.contractName" placeholder="请输入合同名称" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="24">
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
          <el-col :span="12">
            <el-form-item label="合同金额" prop="contractAmount">
              <el-input-number
                v-model="createForm.contractAmount"
                :precision="2"
                :min="0"
                placeholder="请输入合同金额"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="24">
          <el-col :span="12">
            <el-form-item label="签订日期" prop="signDate">
              <el-date-picker
                v-model="createForm.signDate"
                type="date"
                placeholder="选择签订日期"
                format="yyyy-MM-dd"
                value-format="yyyy-MM-dd"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="生效日期" prop="effectiveDate">
              <el-date-picker
                v-model="createForm.effectiveDate"
                type="date"
                placeholder="选择生效日期"
                format="yyyy-MM-dd"
                value-format="yyyy-MM-dd"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="24">
          <el-col :span="12">
            <el-form-item label="到期日期" prop="expiryDate">
              <el-date-picker
                v-model="createForm.expiryDate"
                type="date"
                placeholder="选择到期日期"
                format="yyyy-MM-dd"
                value-format="yyyy-MM-dd"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="确认方法" prop="recognitionMethod">
              <el-select v-model="createForm.recognitionMethod" placeholder="请选择确认方法">
                <el-option label="按时间确认" :value="1" />
                <el-option label="按进度确认" :value="2" />
                <el-option label="按事件确认" :value="3" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="履约义务" prop="performanceObligations">
          <el-input
            v-model="createForm.performanceObligations"
            type="textarea"
            :rows="3"
            placeholder="请输入履约义务（JSON格式）"
          />
        </el-form-item>
        <el-form-item label="合同描述" prop="contractDesc">
          <el-input
            v-model="createForm.contractDesc"
            type="textarea"
            :rows="3"
            placeholder="请输入合同描述"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="createDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleCreate">确定</el-button>
      </div>
    </el-dialog>

    <!-- 更新进度对话框 -->
    <el-dialog
      title="更新履约进度"
      :visible.sync="progressDialogVisible"
      width="600px"
      :close-on-click-modal="false"
    >
      <el-form :model="progressForm" :rules="progressRules" ref="progressForm" label-width="120px">
        <el-form-item label="合同编号">
          <el-input v-model="progressForm.contractNo" disabled />
        </el-form-item>
        <el-form-item label="合同名称">
          <el-input v-model="progressForm.contractName" disabled />
        </el-form-item>
        <el-form-item label="当前进度">
          <el-input v-model="progressForm.currentProgress" disabled>
            <template slot="append">%</template>
          </el-input>
        </el-form-item>
        <el-form-item label="新进度" prop="newProgress">
          <el-input-number
            v-model="progressForm.newProgress"
            :precision="2"
            :min="0"
            :max="100"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="进度说明" prop="progressDesc">
          <el-input
            v-model="progressForm.progressDesc"
            type="textarea"
            :rows="3"
            placeholder="请输入进度更新说明"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="progressDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleUpdateProgress">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  getContractRevenueList,
  createContractRevenue,
  updateContractRevenue,
  identifyPerformanceObligations,
  updatePerformanceProgress,
  getContractRevenueStats
} from '@/api/financialSharing/revenueManagement'

export default {
  name: 'ContractRevenueIndex',
  data() {
    return {
      loading: false,
      tableData: [],
      selectedRows: [],
      stats: {
        totalContracts: 0,
        totalAmount: 0,
        recognizedAmount: 0,
        avgProgress: 0
      },
      searchForm: {
        contractNo: '',
        contractName: '',
        customerName: '',
        contractStatus: '',
        recognitionMethod: ''
      },
      pagination: {
        currentPage: 1,
        pageSize: 20,
        total: 0
      },
      createDialogVisible: false,
      createForm: {
        contractNo: '',
        contractName: '',
        customerId: '',
        contractAmount: 0,
        signDate: '',
        effectiveDate: '',
        expiryDate: '',
        recognitionMethod: '',
        performanceObligations: '',
        contractDesc: ''
      },
      createRules: {
        contractNo: [{ required: true, message: '请输入合同编号', trigger: 'blur' }],
        contractName: [{ required: true, message: '请输入合同名称', trigger: 'blur' }],
        customerId: [{ required: true, message: '请选择客户', trigger: 'change' }],
        contractAmount: [{ required: true, message: '请输入合同金额', trigger: 'blur' }],
        signDate: [{ required: true, message: '请选择签订日期', trigger: 'change' }],
        effectiveDate: [{ required: true, message: '请选择生效日期', trigger: 'change' }],
        recognitionMethod: [{ required: true, message: '请选择确认方法', trigger: 'change' }]
      },
      progressDialogVisible: false,
      progressForm: {
        contractId: '',
        contractNo: '',
        contractName: '',
        currentProgress: 0,
        newProgress: 0,
        progressDesc: ''
      },
      progressRules: {
        newProgress: [{ required: true, message: '请输入新进度', trigger: 'blur' }],
        progressDesc: [{ required: true, message: '请输入进度说明', trigger: 'blur' }]
      },
      customerOptions: []
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
        const response = await getContractRevenueList(params)
        if (response.code === 1) {
          this.tableData = response.data.tlist || []
          this.pagination.total = response.data.totalRecord || 0
        }
      } catch (error) {
        this.$message.error('加载数据失败')
      } finally {
        this.loading = false
      }
    },
    async loadStats() {
      try {
        // 调用后端API获取统计数据
        const response = await getContractRevenueStats()
        if (response.code === 1 && response.data) {
          this.stats = {
            totalContracts: response.data.totalContracts || 0,
            totalAmount: response.data.totalContractAmount || 0,
            recognizedAmount: response.data.recognizedAmount || 0,
            avgProgress: response.data.recognitionRate || 0
          }
        }
      } catch (error) {
        console.error('加载统计数据失败:', error)
        // 失败时使用默认值
        this.stats = {
          totalContracts: 0,
          totalAmount: 0,
          recognizedAmount: 0,
          avgProgress: 0
        }
      }
    },
    async loadCustomerOptions() {
      // 暂未对接 API，先以空状态展示，待后端接口提供后接入
      this.customerOptions = []
    },
    formatAmount(amount) {
      if (!amount) return '0.00'
      return (amount / 10000).toFixed(2) + '万'
    },
    getContractStatusType(status) {
      const types = { 1: 'info', 2: 'warning', 3: 'success', 4: 'danger' }
      return types[status] || 'info'
    },
    getContractStatusText(status) {
      const texts = { 1: '生效', 2: '履行中', 3: '已完成', 4: '已终止' }
      return texts[status] || '未知'
    },
    handleSearch() {
      this.pagination.currentPage = 1
      this.loadData()
    },
    resetSearch() {
      this.searchForm = {
        contractNo: '',
        contractName: '',
        customerName: '',
        contractStatus: '',
        recognitionMethod: ''
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
      this.createDialogVisible = true
      this.createForm = {
        contractNo: '',
        contractName: '',
        customerId: '',
        contractAmount: 0,
        signDate: '',
        effectiveDate: '',
        expiryDate: '',
        recognitionMethod: '',
        performanceObligations: '',
        contractDesc: ''
      }
    },
    async handleCreate() {
      this.$refs.createForm.validate(async (valid) => {
        if (valid) {
          try {
            const response = await createContractRevenue(this.createForm)
            if (response.code === 1) {
              this.$message.success('创建成功')
              this.createDialogVisible = false
              this.loadData()
              this.loadStats()
            }
          } catch (error) {
            this.$message.error('创建失败')
          }
        }
      })
    },
    viewDetail(row) {
      const content = `<p><b>合同编号：</b>${row.contractNo || '-'}</p>` +
        `<p><b>合同名称：</b>${row.contractName || '-'}</p>` +
        `<p><b>客户名称：</b>${row.customerName || '-'}</p>` +
        `<p><b>合同金额：</b>${row.contractAmount || 0}</p>` +
        `<p><b>履约进度：</b>${row.performanceProgress || 0}%</p>` +
        `<p><b>状态：</b>${row.statusName || row.status || '-'}</p>`
      this.$alert(content, '合同详情', { dangerouslyUseHTMLString: true })
    },
    async viewObligations(row) {
      try {
        const response = await identifyPerformanceObligations(row.contractId)
        if (response.code === 1) {
          this.$message.success('履约义务识别完成')
          // 这里可以显示履约义务详情
        }
      } catch (error) {
        this.$message.error('履约义务识别失败')
      }
    },
    updateProgress(row) {
      this.progressDialogVisible = true
      this.progressForm = {
        contractId: row.contractId,
        contractNo: row.contractNo,
        contractName: row.contractName,
        currentProgress: row.performanceProgress || 0,
        newProgress: row.performanceProgress || 0,
        progressDesc: ''
      }
    },
    async handleUpdateProgress() {
      this.$refs.progressForm.validate(async (valid) => {
        if (valid) {
          try {
            const response = await updatePerformanceProgress(this.progressForm.contractId, {
              performanceProgress: this.progressForm.newProgress,
              progressDesc: this.progressForm.progressDesc
            })
            if (response.code === 1) {
              this.$message.success('进度更新成功')
              this.progressDialogVisible = false
              this.loadData()
              this.loadStats()
            }
          } catch (error) {
            this.$message.error('进度更新失败')
          }
        }
      })
    },
    viewRecognition(row) {
      const content = `<p><b>合同编号：</b>${row.contractNo || '-'}</p>` +
        `<p><b>已确认收入：</b>${row.recognizedAmount || 0}</p>` +
        `<p><b>未确认收入：</b>${(row.contractAmount || 0) - (row.recognizedAmount || 0)}</p>` +
        `<p><b>最近确认时间：</b>${row.lastRecognitionTime || '-'}</p>`
      this.$alert(content, '收入确认记录', { dangerouslyUseHTMLString: true })
    },
    async identifyObligations() {
      if (this.selectedRows.length === 0) {
        this.$message.warning('请选择要识别履约义务的合同')
        return
      }
      try {
        await this.$confirm(`确认对选中的${this.selectedRows.length}条合同批量识别履约义务？`, '确认', { type: 'warning' })
        for (const row of this.selectedRows) {
          try {
            await identifyPerformanceObligations(row.contractId)
          } catch (e) {
            // 单条失败继续处理后续
          }
        }
        this.$message.success('批量识别完成')
        this.loadData()
        this.loadStats()
      } catch (e) {
        if (e !== 'cancel') this.$message.error('批量识别失败')
      }
    },
    refreshData() {
      this.loadData()
      this.loadStats()
    }
  }
}
</script>

<style lang="scss" scoped>
.contract-revenue-container {
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
        color: #e6a23c;
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

.contract-stats {
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

      &.amount {
        background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
      }

      &.recognized {
        background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
      }

      &.progress {
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
    color: #e6a23c;
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
</style>
