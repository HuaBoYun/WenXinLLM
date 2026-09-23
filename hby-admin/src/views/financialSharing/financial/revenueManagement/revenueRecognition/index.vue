<template>
  <div class="revenue-recognition-container">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-left">
        <h1 class="page-title">
          <i class="el-icon-check"></i>
          收入确认管理
        </h1>
        <p class="page-description">管理收入确认规则、确认条件检查、批量确认处理等功能</p>
      </div>
      <div class="header-right">
        <el-button type="primary" icon="el-icon-plus" @click="showCreateDialog">
          新增确认
        </el-button>
        <el-button type="success" icon="el-icon-check" @click="batchRecognition">
          批量确认
        </el-button>
        <el-button type="warning" icon="el-icon-refresh" @click="refreshData">
          刷新数据
        </el-button>
      </div>
    </div>

    <!-- 统计卡片 -->
    <div class="stats-cards">
      <el-row :gutter="24">
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon pending">
              <i class="el-icon-time"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ stats.pendingCount }}</div>
              <div class="stat-label">待确认</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon confirmed">
              <i class="el-icon-check"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ stats.confirmedCount }}</div>
              <div class="stat-label">已确认</div>
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
              <div class="stat-label">确认金额</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon rate">
              <i class="el-icon-data-line"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ stats.recognitionRate }}%</div>
              <div class="stat-label">确认率</div>
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
        <el-form-item label="确认类型">
          <el-select v-model="searchForm.recognitionType" placeholder="请选择确认类型" clearable>
            <el-option label="按时间确认" :value="1" />
            <el-option label="按进度确认" :value="2" />
            <el-option label="按事件确认" :value="3" />
          </el-select>
        </el-form-item>
        <el-form-item label="确认期间">
          <el-date-picker
            v-model="searchForm.recognitionPeriod"
            type="month"
            placeholder="选择确认期间"
            format="yyyy-MM"
            value-format="yyyy-MM"
            clearable
          />
        </el-form-item>
        <el-form-item label="确认状态">
          <el-select v-model="searchForm.recognitionStatus" placeholder="请选择确认状态" clearable>
            <el-option label="待确认" :value="0" />
            <el-option label="已确认" :value="1" />
            <el-option label="已撤销" :value="2" />
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
        <el-table-column prop="recognitionId" label="确认ID" width="120" />
        <el-table-column prop="contractNo" label="合同编号" width="150" />
        <el-table-column prop="recognitionTypeName" label="确认类型" width="120" />
        <el-table-column prop="recognitionPeriod" label="确认期间" width="120" />
        <el-table-column prop="recognitionAmount" label="确认金额" width="120" align="right">
          <template slot-scope="scope">
            <span class="amount-text">{{ formatAmount(scope.row.recognitionAmount) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="recognitionDate" label="确认日期" width="120" />
        <el-table-column prop="recognitionStatus" label="确认状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="getStatusType(scope.row.recognitionStatus)">
              {{ getStatusText(scope.row.recognitionStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="deferredRevenue" label="递延收入" width="120" align="right">
          <template slot-scope="scope">
            <span class="amount-text">{{ formatAmount(scope.row.deferredRevenue) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="cumulativeRecognition" label="累计确认" width="120" align="right">
          <template slot-scope="scope">
            <span class="amount-text">{{ formatAmount(scope.row.cumulativeRecognition) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="remainingRevenue" label="剩余收入" width="120" align="right">
          <template slot-scope="scope">
            <span class="amount-text">{{ formatAmount(scope.row.remainingRevenue) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" @click="viewDetail(scope.row)">详情</el-button>
            <el-button 
              v-if="scope.row.recognitionStatus === 0" 
              size="mini" 
              type="success" 
              @click="confirmRecognition(scope.row)"
            >
              确认
            </el-button>
            <el-button 
              v-if="scope.row.recognitionStatus === 1" 
              size="mini" 
              type="warning" 
              @click="revokeRecognition(scope.row)"
            >
              撤销
            </el-button>
            <el-button size="mini" type="info" @click="viewVoucher(scope.row)">凭证</el-button>
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

    <!-- 创建确认对话框 -->
    <el-dialog
      title="新增收入确认"
      :visible.sync="createDialogVisible"
      width="800px"
      :close-on-click-modal="false"
    >
      <el-form :model="createForm" :rules="createRules" ref="createForm" label-width="120px">
        <el-row :gutter="24">
          <el-col :span="12">
            <el-form-item label="合同" prop="contractId">
              <el-select v-model="createForm.contractId" placeholder="请选择合同" filterable>
                <el-option
                  v-for="contract in contractOptions"
                  :key="contract.contractId"
                  :label="contract.contractName"
                  :value="contract.contractId"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="确认类型" prop="recognitionType">
              <el-select v-model="createForm.recognitionType" placeholder="请选择确认类型">
                <el-option label="按时间确认" :value="1" />
                <el-option label="按进度确认" :value="2" />
                <el-option label="按事件确认" :value="3" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="24">
          <el-col :span="12">
            <el-form-item label="确认期间" prop="recognitionPeriod">
              <el-date-picker
                v-model="createForm.recognitionPeriod"
                type="month"
                placeholder="选择确认期间"
                format="yyyy-MM"
                value-format="yyyy-MM"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="确认金额" prop="recognitionAmount">
              <el-input-number
                v-model="createForm.recognitionAmount"
                :precision="2"
                :min="0"
                placeholder="请输入确认金额"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="24">
          <el-col :span="12">
            <el-form-item label="确认比例" prop="recognitionRate">
              <el-input-number
                v-model="createForm.recognitionRate"
                :precision="2"
                :min="0"
                :max="100"
                placeholder="请输入确认比例"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="履约进度" prop="performanceProgress">
              <el-input-number
                v-model="createForm.performanceProgress"
                :precision="2"
                :min="0"
                :max="100"
                placeholder="请输入履约进度"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="确认依据" prop="recognitionBasis">
          <el-input
            v-model="createForm.recognitionBasis"
            type="textarea"
            :rows="3"
            placeholder="请输入确认依据"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="createDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleCreate">确定</el-button>
      </div>
    </el-dialog>

    <!-- 详情对话框 -->
    <el-dialog
      title="收入确认详情"
      :visible.sync="detailDialogVisible"
      width="700px"
      :close-on-click-modal="false"
    >
      <div v-loading="detailLoading">
        <el-descriptions :column="2" border v-if="Object.keys(detailData).length > 0">
          <el-descriptions-item label="确认ID">{{ detailData.recognitionId }}</el-descriptions-item>
          <el-descriptions-item label="合同编号">{{ detailData.contractNo }}</el-descriptions-item>
          <el-descriptions-item label="合同名称" :span="2">{{ detailData.contractName }}</el-descriptions-item>
          <el-descriptions-item label="确认类型">{{ detailData.recognitionTypeName }}</el-descriptions-item>
          <el-descriptions-item label="确认期间">{{ detailData.recognitionPeriod }}</el-descriptions-item>
          <el-descriptions-item label="确认日期">{{ detailData.recognitionDate }}</el-descriptions-item>
          <el-descriptions-item label="确认金额">
            <span class="amount-text">{{ formatAmount(detailData.recognitionAmount) }}</span>
          </el-descriptions-item>
          <el-descriptions-item label="累计确认金额">
            <span class="amount-text">{{ formatAmount(detailData.cumulativeAmount) }}</span>
          </el-descriptions-item>
          <el-descriptions-item label="剩余收入">
            <span class="amount-text">{{ formatAmount(detailData.remainingRevenue) }}</span>
          </el-descriptions-item>
          <el-descriptions-item label="确认比例">{{ detailData.recognitionRate }}%</el-descriptions-item>
          <el-descriptions-item label="履约进度">{{ detailData.performanceProgress }}%</el-descriptions-item>
          <el-descriptions-item label="确认依据" :span="2">{{ detailData.recognitionBasis || '-' }}</el-descriptions-item>
          <el-descriptions-item label="凭证ID">{{ detailData.voucherId || '-' }}</el-descriptions-item>
          <el-descriptions-item label="创建时间">{{ detailData.createTime }}</el-descriptions-item>
        </el-descriptions>
        <el-empty v-else description="暂无详情数据"></el-empty>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="detailDialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>

    <!-- 凭证对话框 -->
    <el-dialog
      title="关联凭证信息"
      :visible.sync="voucherDialogVisible"
      width="800px"
      :close-on-click-modal="false"
    >
      <div v-loading="voucherLoading">
        <el-descriptions :column="2" border v-if="Object.keys(voucherData).length > 0">
          <el-descriptions-item label="凭证编号">{{ voucherData.voucherNo }}</el-descriptions-item>
          <el-descriptions-item label="凭证类型">{{ voucherData.voucherTypeName }}</el-descriptions-item>
          <el-descriptions-item label="凭证日期">{{ voucherData.voucherDate }}</el-descriptions-item>
          <el-descriptions-item label="会计期间">{{ voucherData.accountingPeriod }}</el-descriptions-item>
          <el-descriptions-item label="借方金额">
            <span class="amount-text debit">{{ formatAmount(voucherData.debitAmount) }}</span>
          </el-descriptions-item>
          <el-descriptions-item label="贷方金额">
            <span class="amount-text credit">{{ formatAmount(voucherData.creditAmount) }}</span>
          </el-descriptions-item>
          <el-descriptions-item label="摘要" :span="2">{{ voucherData.summary || '-' }}</el-descriptions-item>
          <el-descriptions-item label="凭证状态">
            <el-tag :type="getVoucherStatusType(voucherData.voucherStatus)">
              {{ voucherData.voucherStatusName }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="制单人">{{ voucherData.creatorName || '-' }}</el-descriptions-item>
          <el-descriptions-item label="创建时间">{{ voucherData.createTime }}</el-descriptions-item>
          <el-descriptions-item label="审核人">{{ voucherData.auditorName || '-' }}</el-descriptions-item>
          <el-descriptions-item label="审核时间">{{ voucherData.auditTime || '-' }}</el-descriptions-item>
        </el-descriptions>
        <el-empty v-else description="该记录暂无关联凭证"></el-empty>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="voucherDialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  getRevenueRecognitionList,
  executeRevenueRecognition,
  revokeRevenueRecognition,
  batchRevenueRecognition,
  getRevenueStatisticsOverview,
  getRevenueRecognitionDetail,
  getRecognitionVoucher
} from '@/api/financialSharing/revenueManagement'
import { getRevenueContractPage } from '@/api/financialSharing/revenueContract'

export default {
  name: 'RevenueRecognitionIndex',
  data() {
    return {
      loading: false,
      tableData: [],
      selectedRows: [],
      stats: {
        pendingCount: 0,
        confirmedCount: 0,
        totalAmount: 0,
        recognitionRate: 0
      },
      searchForm: {
        contractNo: '',
        recognitionType: '',
        recognitionPeriod: '',
        recognitionStatus: ''
      },
      pagination: {
        currentPage: 1,
        pageSize: 20,
        total: 0
      },
      createDialogVisible: false,
      createForm: {
        contractId: '',
        recognitionType: '',
        recognitionPeriod: '',
        recognitionAmount: 0,
        recognitionRate: 0,
        performanceProgress: 0,
        recognitionBasis: ''
      },
      createRules: {
        contractId: [{ required: true, message: '请选择合同', trigger: 'change' }],
        recognitionType: [{ required: true, message: '请选择确认类型', trigger: 'change' }],
        recognitionPeriod: [{ required: true, message: '请选择确认期间', trigger: 'change' }],
        recognitionAmount: [{ required: true, message: '请输入确认金额', trigger: 'blur' }]
      },
      contractOptions: [],
      // 详情对话框
      detailDialogVisible: false,
      detailLoading: false,
      detailData: {},
      // 凭证对话框
      voucherDialogVisible: false,
      voucherLoading: false,
      voucherData: {}
    }
  },
  mounted() {
    this.loadData()
    this.loadStats()
    this.loadContractOptions()
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
        const response = await getRevenueRecognitionList(params)
        // 状态码处理：1表示成功，0表示失败
        if (response.code === 1) {
          this.tableData = response.data.tlist || []
          this.pagination.total = response.data.totalRecord || 0
        } else {
          this.$message.error(response.msg || '查询失败')
          this.tableData = []
          this.pagination.total = 0
        }
      } catch (error) {
        this.$message.error('查询失败：' + error.message)
        this.tableData = []
        this.pagination.total = 0
      } finally {
        this.loading = false
      }
    },
    async loadStats() {
      try {
        const response = await getRevenueStatisticsOverview({})
        // 状态码处理：1表示成功
        if (response.code === 1) {
          this.stats = response.data || this.stats
        } else {
          console.warn('加载统计数据失败:', response.msg)
        }
      } catch (error) {
        console.error('加载统计数据失败:', error)
      }
    },
    async loadContractOptions() {
      try {
        const response = await getRevenueContractPage({ pageSize: 1000 })
        // 状态码处理：1表示成功
        if (response.code === 1) {
          this.contractOptions = response.data.tlist || []
        } else {
          console.warn('加载合同选项失败:', response.msg)
          this.contractOptions = []
        }
      } catch (error) {
        console.error('加载合同选项失败:', error)
        this.contractOptions = []
      }
    },
    formatAmount(amount) {
      if (!amount) return '0.00'
      return (amount / 10000).toFixed(2) + '万'
    },
    getStatusType(status) {
      const types = { 0: 'warning', 1: 'success', 2: 'info' }
      return types[status] || 'info'
    },
    getStatusText(status) {
      const texts = { 0: '待确认', 1: '已确认', 2: '已撤销' }
      return texts[status] || '未知'
    },
    handleSearch() {
      this.pagination.currentPage = 1
      this.loadData()
    },
    resetSearch() {
      this.searchForm = {
        contractNo: '',
        recognitionType: '',
        recognitionPeriod: '',
        recognitionStatus: ''
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
        contractId: '',
        recognitionType: '',
        recognitionPeriod: '',
        recognitionAmount: 0,
        recognitionRate: 0,
        performanceProgress: 0,
        recognitionBasis: ''
      }
    },
    async handleCreate() {
      this.$refs.createForm.validate(async (valid) => {
        if (valid) {
          try {
            const response = await executeRevenueRecognition(this.createForm)
            // 状态码处理：1表示成功，0表示失败
            if (response.code === 1) {
              this.$message.success('创建成功')
              this.createDialogVisible = false
              this.loadData()
              this.loadStats()
            } else {
              this.$message.error(response.msg || '创建失败')
            }
          } catch (error) {
            this.$message.error('创建失败：' + error.message)
          }
        }
      })
    },
    async confirmRecognition(row) {
      try {
        const response = await executeRevenueRecognition({
          recognitionId: row.recognitionId
        })
        // 状态码处理：1表示成功，0表示失败
        if (response.code === 1) {
          this.$message.success('确认成功')
          this.loadData()
          this.loadStats()
        } else {
          this.$message.error(response.msg || '确认失败')
        }
      } catch (error) {
        this.$message.error('确认失败：' + error.message)
      }
    },
    async revokeRecognition(row) {
      this.$confirm('确定要撤销此收入确认吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const response = await revokeRevenueRecognition(row.recognitionId, {
            reason: '手动撤销'
          })
          // 状态码处理：1表示成功，0表示失败
          if (response.code === 1) {
            this.$message.success('撤销成功')
            this.loadData()
            this.loadStats()
          } else {
            this.$message.error(response.msg || '撤销失败')
          }
        } catch (error) {
          this.$message.error('撤销失败：' + error.message)
        }
      })
    },
    async batchRecognition() {
      if (this.selectedRows.length === 0) {
        this.$message.warning('请选择要确认的记录')
        return
      }

      const pendingRows = this.selectedRows.filter(row => row.recognitionStatus === 0)
      if (pendingRows.length === 0) {
        this.$message.warning('所选记录中没有待确认的记录')
        return
      }

      this.$confirm(`确定要批量确认 ${pendingRows.length} 条记录吗？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const recognitionIds = pendingRows.map(row => row.recognitionId)
          const response = await batchRevenueRecognition({ recognitionIds })
          // 状态码处理：1表示成功，0表示失败
          if (response.code === 1) {
            this.$message.success('批量确认成功')
            this.loadData()
            this.loadStats()
          } else {
            this.$message.error(response.msg || '批量确认失败')
          }
        } catch (error) {
          this.$message.error('批量确认失败：' + error.message)
        }
      })
    },
    async viewDetail(row) {
      this.detailDialogVisible = true
      this.detailLoading = true
      try {
        const response = await getRevenueRecognitionDetail(row.recognitionId)
        if (response.code === 1) {
          this.detailData = response.data || {}
        } else {
          this.$message.error(response.msg || '获取详情失败')
          this.detailData = {}
        }
      } catch (error) {
        this.$message.error('获取详情失败：' + error.message)
        this.detailData = {}
      } finally {
        this.detailLoading = false
      }
    },
    async viewVoucher(row) {
      this.voucherDialogVisible = true
      this.voucherLoading = true
      try {
        const response = await getRecognitionVoucher(row.recognitionId)
        if (response.code === 1) {
          this.voucherData = response.data || {}
        } else {
          this.$message.warning(response.msg || '该记录暂无关联凭证')
          this.voucherData = {}
        }
      } catch (error) {
        this.$message.error('获取凭证失败：' + error.message)
        this.voucherData = {}
      } finally {
        this.voucherLoading = false
      }
    },
    refreshData() {
      this.loadData()
      this.loadStats()
    },
    getVoucherStatusType(status) {
      const statusMap = {
        0: 'info',     // 草稿
        1: 'success',  // 已审核
        2: 'primary',  // 已过账
        3: 'danger'    // 已作废
      }
      return statusMap[status] || 'info'
    }
  }
}
</script>

<style lang="scss" scoped>
.revenue-recognition-container {
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

.stats-cards {
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

      &.pending {
        background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
      }

      &.confirmed {
        background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
      }

      &.amount {
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      }

      &.rate {
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

.amount-text {
  font-weight: 600;
  color: #409eff;

  &.debit {
    color: #e6a23c;
  }

  &.credit {
    color: #67c23a;
  }
}

::v-deep .el-descriptions-item__label {
  width: 120px;
  font-weight: 500;
}
</style>
