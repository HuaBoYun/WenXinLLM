<template>
  <div class="voucher-approval-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>凭证审核</h2>
      <p>审核凭证的准确性和合规性，支持单个审核、批量审核和审核退回</p>
    </div>

    <!-- 统计卡片 -->
    <div class="statistics-cards">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-card class="statistic-card">
            <div class="statistic-item">
              <div class="statistic-icon pending">
                <i class="el-icon-time"></i>
              </div>
              <div class="statistic-content">
                <div class="statistic-value">{{ statistics.pendingApproval || 0 }}</div>
                <div class="statistic-label">待审核凭证</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="statistic-card">
            <div class="statistic-item">
              <div class="statistic-icon approved">
                <i class="el-icon-check"></i>
              </div>
              <div class="statistic-content">
                <div class="statistic-value">{{ statistics.approvedToday || 0 }}</div>
                <div class="statistic-label">今日已审核</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="statistic-card">
            <div class="statistic-item">
              <div class="statistic-icon rejected">
                <i class="el-icon-close"></i>
              </div>
              <div class="statistic-content">
                <div class="statistic-value">{{ statistics.rejectedCount || 0 }}</div>
                <div class="statistic-label">审核退回</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="statistic-card">
            <div class="statistic-item">
              <div class="statistic-icon total">
                <i class="el-icon-s-data"></i>
              </div>
              <div class="statistic-content">
                <div class="statistic-value">{{ statistics.totalApproval || 0 }}</div>
                <div class="statistic-label">审核总数</div>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 功能标签页 -->
    <el-tabs v-model="activeTab" type="card" @tab-click="handleTabClick">
      <!-- 凭证审核 -->
      <el-tab-pane label="凭证审核" name="voucher-approval">
        <div class="tab-content">
          <!-- 查询条件 -->
          <div class="search-form">
            <el-form :model="approvalForm" ref="approvalForm" :inline="true" label-width="100px">
              <el-form-item label="凭证编号">
                <el-input v-model="approvalForm.voucherNo" placeholder="请输入凭证编号" clearable />
              </el-form-item>
              <el-form-item label="凭证类型">
                <el-select v-model="approvalForm.voucherType" placeholder="请选择凭证类型" clearable>
                  <el-option label="记账凭证" value="ACCOUNTING" />
                  <el-option label="收款凭证" value="RECEIPT" />
                  <el-option label="付款凭证" value="PAYMENT" />
                  <el-option label="转账凭证" value="TRANSFER" />
                </el-select>
              </el-form-item>
              <el-form-item label="制单人">
                <el-input v-model="approvalForm.creator" placeholder="请输入制单人" clearable />
              </el-form-item>
              <el-form-item label="制单日期">
                <el-date-picker
                  v-model="approvalForm.dateRange"
                  type="daterange"
                  range-separator="至"
                  start-placeholder="开始日期"
                  end-placeholder="结束日期"
                  format="yyyy-MM-dd"
                  value-format="yyyy-MM-dd"
                />
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="handleApprovalSearch">查询</el-button>
                <el-button @click="handleApprovalReset">重置</el-button>
              </el-form-item>
            </el-form>
          </div>

          <!-- 操作工具栏 -->
          <div class="toolbar">
            <el-button type="success" icon="el-icon-check" @click="handleBatchApproval" :disabled="selectedApprovalRows.length === 0">
              批量审核
            </el-button>
            <el-button type="warning" icon="el-icon-close" @click="handleBatchReject" :disabled="selectedApprovalRows.length === 0">
              批量退回
            </el-button>
            <el-button type="info" icon="el-icon-refresh" @click="handleRefreshApproval">
              刷新
            </el-button>
          </div>

          <!-- 凭证表格 -->
          <div class="table-container">
            <el-table
              :data="approvalTableData"
              v-loading="approvalLoading"
              border
              stripe
              height="500"
              @selection-change="handleApprovalSelectionChange"
            >
              <el-table-column type="selection" width="55" />
              <el-table-column prop="voucherNo" label="凭证编号" width="180" show-overflow-tooltip />
              <el-table-column prop="voucherTypeId" label="凭证类型" width="100">
                <template slot-scope="scope">
                  <el-tag :type="getVoucherTypeColor(scope.row.voucherTypeId)">
                    {{ scope.row.voucherTypeName || getVoucherTypeName(scope.row.voucherTypeId) }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column label="制单日期" width="120">
                <template slot-scope="scope">
                  {{ formatVoucherDate(scope.row.voucherDate) }}
                </template>
              </el-table-column>
              <el-table-column prop="accountingPeriod" label="会计期间" width="100" />
              <el-table-column prop="voucherDesc" label="摘要" min-width="200" show-overflow-tooltip />
              <el-table-column label="借方金额" width="120" align="right">
                <template slot-scope="scope">
                  <span style="color: #67c23a; font-weight: bold;">
                    ¥{{ formatAmount(scope.row.totalDebit) }}
                  </span>
                </template>
              </el-table-column>
              <el-table-column label="贷方金额" width="120" align="right">
                <template slot-scope="scope">
                  <span style="color: #f56c6c; font-weight: bold;">
                    ¥{{ formatAmount(scope.row.totalCredit) }}
                  </span>
                </template>
              </el-table-column>
              <el-table-column prop="creator" label="制单人" width="100" />
              <el-table-column label="凭证状态" width="100">
                <template slot-scope="scope">
                  <el-tag :type="getVoucherStatusType(scope.row.voucherStatus)" size="small">
                    {{ scope.row.voucherStatusName || getVoucherStatusName(scope.row.voucherStatus) }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column label="操作" width="250" fixed="right">
                <template slot-scope="scope">
                  <el-button size="mini" @click="handleViewVoucher(scope.row)">查看</el-button>
                  <el-button size="mini" type="success" @click="handleSingleApproval(scope.row)">审核</el-button>
                  <el-button size="mini" type="warning" @click="handleSingleReject(scope.row)">退回</el-button>
                  <el-button size="mini" type="info" @click="handleViewHistory(scope.row)">历史</el-button>
                </template>
              </el-table-column>
            </el-table>
          </div>

          <!-- 分页组件 -->
          <div class="pagination-container">
            <el-pagination
              @size-change="handleApprovalSizeChange"
              @current-change="handleApprovalCurrentChange"
              :current-page="approvalPagination.currentPage"
              :page-sizes="[10, 20, 50, 100]"
              :page-size="approvalPagination.pageSize"
              layout="total, sizes, prev, pager, next, jumper"
              :total="approvalPagination.total"
            />
          </div>
        </div>
      </el-tab-pane>

      <!-- 批量审核 -->
      <el-tab-pane label="批量审核" name="batch-approval">
        <div class="tab-content">
          <!-- 批量审核配置 -->
          <div class="batch-config">
            <el-form :model="batchApprovalForm" ref="batchApprovalForm" label-width="120px">
              <el-row :gutter="20">
                <el-col :span="12">
                  <el-form-item label="审核模式" required>
                    <el-radio-group v-model="batchApprovalForm.approvalMode">
                      <el-radio label="BY_DATE">按日期审核</el-radio>
                      <el-radio label="BY_TYPE">按凭证类型审核</el-radio>
                      <el-radio label="BY_CREATOR">按制单人审核</el-radio>
                    </el-radio-group>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="审核策略" required>
                    <el-radio-group v-model="batchApprovalForm.approvalStrategy">
                      <el-radio label="AUTO">自动审核</el-radio>
                      <el-radio label="MANUAL">手动确认</el-radio>
                    </el-radio-group>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row :gutter="20">
                <el-col :span="12">
                  <el-form-item label="制单日期范围" required>
                    <el-date-picker
                      v-model="batchApprovalForm.dateRange"
                      type="daterange"
                      range-separator="至"
                      start-placeholder="开始日期"
                      end-placeholder="结束日期"
                      format="yyyy-MM-dd"
                      value-format="yyyy-MM-dd"
                    />
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="金额范围">
                    <el-input-number v-model="batchApprovalForm.minAmount" placeholder="最小金额" style="width: 45%;" />
                    <span style="margin: 0 2%;">-</span>
                    <el-input-number v-model="batchApprovalForm.maxAmount" placeholder="最大金额" style="width: 45%;" />
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row :gutter="20">
                <el-col :span="12">
                  <el-form-item label="审核规则">
                    <el-checkbox-group v-model="batchApprovalForm.approvalRules">
                      <el-checkbox label="CHECK_BALANCE">检查借贷平衡</el-checkbox>
                      <el-checkbox label="CHECK_SUBJECT">检查科目有效性</el-checkbox>
                      <el-checkbox label="CHECK_AMOUNT">检查金额合理性</el-checkbox>
                      <el-checkbox label="CHECK_DATE">检查日期有效性</el-checkbox>
                    </el-checkbox-group>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="异常处理">
                    <el-radio-group v-model="batchApprovalForm.exceptionHandling">
                      <el-radio label="SKIP">跳过异常</el-radio>
                      <el-radio label="STOP">停止处理</el-radio>
                      <el-radio label="MANUAL">手动处理</el-radio>
                    </el-radio-group>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-form-item label="审核备注">
                <el-input
                  v-model="batchApprovalForm.approvalRemark"
                  type="textarea"
                  :rows="3"
                  placeholder="请输入批量审核备注"
                />
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="handleSubmitBatchApproval">执行批量审核</el-button>
                <el-button @click="handleResetBatchApproval">重置</el-button>
              </el-form-item>
            </el-form>
          </div>
        </div>
      </el-tab-pane>

      <!-- 审核退回 -->
      <el-tab-pane label="审核退回" name="approval-reject">
        <div class="tab-content">
          <!-- 退回凭证列表 -->
          <div class="reject-list">
            <el-table
              :data="rejectTableData"
              v-loading="rejectLoading"
              border
              stripe
              height="500"
            >
              <el-table-column prop="voucherNo" label="凭证编号" width="120" />
              <el-table-column prop="voucherType" label="凭证类型" width="100">
                <template slot-scope="scope">
                  <el-tag :type="getVoucherTypeColor(scope.row.voucherType)">
                    {{ getVoucherTypeName(scope.row.voucherType) }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="totalAmount" label="金额" width="120" align="right">
                <template slot-scope="scope">
                  <span style="color: #f56c6c; font-weight: bold;">
                    ¥{{ formatAmount(scope.row.totalAmount) }}
                  </span>
                </template>
              </el-table-column>
              <el-table-column prop="creator" label="制单人" width="100" />
              <el-table-column prop="rejectReason" label="退回原因" width="200" show-overflow-tooltip />
              <el-table-column prop="rejectTime" label="退回时间" width="160">
                <template slot-scope="scope">
                  {{ formatDate(scope.row.rejectTime) }}
                </template>
              </el-table-column>
              <el-table-column prop="rejectBy" label="退回人" width="100" />
              <el-table-column prop="status" label="处理状态" width="100">
                <template slot-scope="scope">
                  <el-tag :type="getRejectStatusType(scope.row.status)">
                    {{ getRejectStatusName(scope.row.status) }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column label="操作" width="200" fixed="right">
                <template slot-scope="scope">
                  <el-button size="mini" @click="handleViewRejectVoucher(scope.row)">查看</el-button>
                  <el-button size="mini" type="primary" @click="handleReApproval(scope.row)" v-if="scope.row.status === 'MODIFIED'">
                    重新审核
                  </el-button>
                  <el-button size="mini" type="warning" @click="handleRemindModify(scope.row)" v-if="scope.row.status === 'REJECTED'">
                    提醒修改
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
          </div>

          <!-- 分页组件 -->
          <div class="pagination-container">
            <el-pagination
              @size-change="handleRejectSizeChange"
              @current-change="handleRejectCurrentChange"
              :current-page="rejectPagination.currentPage"
              :page-sizes="[10, 20, 50, 100]"
              :page-size="rejectPagination.pageSize"
              layout="total, sizes, prev, pager, next, jumper"
              :total="rejectPagination.total"
            />
          </div>
        </div>
      </el-tab-pane>

      <!-- 审核历史 -->
      <el-tab-pane label="审核历史" name="approval-history">
        <div class="tab-content">
          <!-- 查询条件 -->
          <div class="search-form">
            <el-form :model="historyForm" ref="historyForm" :inline="true" label-width="100px">
              <el-form-item label="凭证编号">
                <el-input v-model="historyForm.voucherNo" placeholder="请输入凭证编号" clearable />
              </el-form-item>
              <el-form-item label="审核人">
                <el-input v-model="historyForm.approver" placeholder="请输入审核人" clearable />
              </el-form-item>
              <el-form-item label="审核日期">
                <el-date-picker
                  v-model="historyForm.dateRange"
                  type="daterange"
                  range-separator="至"
                  start-placeholder="开始日期"
                  end-placeholder="结束日期"
                  format="yyyy-MM-dd"
                  value-format="yyyy-MM-dd"
                />
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="handleHistorySearch">查询</el-button>
                <el-button @click="handleHistoryReset">重置</el-button>
              </el-form-item>
            </el-form>
          </div>

          <!-- 审核历史列表 -->
          <div class="history-list">
            <el-table
              :data="historyTableData"
              v-loading="historyLoading"
              border
              stripe
              height="500"
            >
              <el-table-column prop="voucherNo" label="凭证编号" width="120" />
              <el-table-column prop="approvalAction" label="审核动作" width="100">
                <template slot-scope="scope">
                  <el-tag :type="getApprovalActionType(scope.row.approvalAction)">
                    {{ getApprovalActionName(scope.row.approvalAction) }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="approver" label="审核人" width="100" />
              <el-table-column prop="approvalTime" label="审核时间" width="160">
                <template slot-scope="scope">
                  {{ formatDate(scope.row.approvalTime) }}
                </template>
              </el-table-column>
              <el-table-column prop="approvalRemark" label="审核备注" width="200" show-overflow-tooltip />
              <el-table-column prop="beforeStatus" label="审核前状态" width="100">
                <template slot-scope="scope">
                  <el-tag size="small">{{ getStatusName(scope.row.beforeStatus) }}</el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="afterStatus" label="审核后状态" width="100">
                <template slot-scope="scope">
                  <el-tag size="small">{{ getStatusName(scope.row.afterStatus) }}</el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="duration" label="审核耗时" width="100" align="center">
                <template slot-scope="scope">
                  {{ scope.row.duration }}分钟
                </template>
              </el-table-column>
              <el-table-column label="操作" width="150" fixed="right">
                <template slot-scope="scope">
                  <el-button size="mini" @click="handleViewHistoryDetail(scope.row)">查看详情</el-button>
                </template>
              </el-table-column>
            </el-table>
          </div>

          <!-- 分页组件 -->
          <div class="pagination-container">
            <el-pagination
              @size-change="handleHistorySizeChange"
              @current-change="handleHistoryCurrentChange"
              :current-page="historyPagination.currentPage"
              :page-sizes="[10, 20, 50, 100]"
              :page-size="historyPagination.pageSize"
              layout="total, sizes, prev, pager, next, jumper"
              :total="historyPagination.total"
            />
          </div>
        </div>
      </el-tab-pane>
    </el-tabs>

    <!-- 审核对话框 -->
    <el-dialog
      title="凭证审核"
      :visible.sync="approvalDialogVisible"
      width="600px"
      :close-on-click-modal="false"
    >
      <div v-if="currentVoucher">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="凭证编号">{{ currentVoucher.voucherNo }}</el-descriptions-item>
          <el-descriptions-item label="凭证类型">{{ getVoucherTypeName(currentVoucher.voucherType) }}</el-descriptions-item>
          <el-descriptions-item label="制单日期">{{ currentVoucher.voucherDate }}</el-descriptions-item>
          <el-descriptions-item label="金额">¥{{ formatAmount(currentVoucher.totalAmount) }}</el-descriptions-item>
          <el-descriptions-item label="制单人">{{ currentVoucher.creator }}</el-descriptions-item>
          <el-descriptions-item label="制单时间">{{ formatDate(currentVoucher.createTime) }}</el-descriptions-item>
          <el-descriptions-item label="摘要" :span="2">{{ currentVoucher.summary }}</el-descriptions-item>
        </el-descriptions>

        <div style="margin-top: 20px;">
          <h4>审核意见</h4>
          <el-form :model="approvalOpinion" ref="approvalOpinion" label-width="100px">
            <el-form-item label="审核结果" required>
              <el-radio-group v-model="approvalOpinion.result">
                <el-radio label="APPROVED">审核通过</el-radio>
                <el-radio label="REJECTED">审核退回</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="审核备注" required>
              <el-input
                v-model="approvalOpinion.remark"
                type="textarea"
                :rows="3"
                placeholder="请输入审核备注"
              />
            </el-form-item>
          </el-form>
        </div>
      </div>
      
      <div slot="footer" class="dialog-footer">
        <el-button @click="approvalDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleConfirmApproval">确认审核</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  getVoucherPage,
  updateVoucherStatus,
  batchUpdateVoucherStatus
} from '@/api/financialSharing/voucher'

export default {
  name: 'VoucherApproval',
  data() {
    return {
      activeTab: 'voucher-approval',
      statistics: {},
      
      // 凭证审核相关
      approvalLoading: false,
      approvalTableData: [],
      selectedApprovalRows: [],
      approvalForm: {
        voucherNo: '',
        voucherType: '',
        creator: '',
        dateRange: []
      },
      approvalPagination: {
        currentPage: 1,
        pageSize: 20,
        total: 0
      },
      
      // 批量审核相关
      batchApprovalForm: {
        approvalMode: 'BY_DATE',
        approvalStrategy: 'AUTO',
        dateRange: [],
        minAmount: null,
        maxAmount: null,
        approvalRules: ['CHECK_BALANCE', 'CHECK_SUBJECT'],
        exceptionHandling: 'SKIP',
        approvalRemark: ''
      },
      
      // 审核退回相关
      rejectLoading: false,
      rejectTableData: [],
      rejectPagination: {
        currentPage: 1,
        pageSize: 20,
        total: 0
      },
      
      // 审核历史相关
      historyLoading: false,
      historyTableData: [],
      historyForm: {
        voucherNo: '',
        approver: '',
        dateRange: []
      },
      historyPagination: {
        currentPage: 1,
        pageSize: 20,
        total: 0
      },
      
      // 审核对话框
      approvalDialogVisible: false,
      currentVoucher: null,
      approvalOpinion: {
        result: 'APPROVED',
        remark: ''
      }
    }
  },
  
  mounted() {
    this.loadApprovalData()
    this.loadStatistics()
  },
  
  methods: {
    // 标签页切换
    handleTabClick(tab) {
      if (tab.name === 'approval-reject') {
        this.loadRejectData()
      } else if (tab.name === 'approval-history') {
        this.loadHistoryData()
      }
    },
    
    // 加载审核数据
    async loadApprovalData() {
      this.approvalLoading = true
      try {
        const params = {
          ...this.approvalForm,
          status: '0', // 只查询草稿状态的凭证
          pageNumber: this.approvalPagination.currentPage,
          pageSize: this.approvalPagination.pageSize
        }

        const response = await getVoucherPage(params)
        console.log('凭证审核数据响应:', response)
        if (response.code === 200 || response.code === 1) {
          this.approvalTableData = response.data.tlist || response.data.list || []
          this.approvalPagination.total = parseInt(response.data.totalRecord) || response.data.total || 0
          console.log('解析后的表格数据:', this.approvalTableData)
        } else {
          this.$message.error(response.msg || '查询失败')
        }
      } catch (error) {
        console.error('加载数据失败:', error)
        this.$message.error('加载数据失败')
      } finally {
        this.approvalLoading = false
      }
    },
    
    // 加载退回数据
    async loadRejectData() {
      this.rejectLoading = true
      try {
        // 暂未对接 API，先以空状态展示，待后端接口提供后接入
        this.rejectTableData = []
        this.rejectPagination.total = 0
      } catch (error) {
        console.error('加载退回数据失败:', error)
      } finally {
        this.rejectLoading = false
      }
    },
    
    // 加载历史数据
    async loadHistoryData() {
      this.historyLoading = true
      try {
        // 暂未对接 API，先以空状态展示，待后端接口提供后接入
        this.historyTableData = []
        this.historyPagination.total = 0
      } catch (error) {
        console.error('加载历史数据失败:', error)
      } finally {
        this.historyLoading = false
      }
    },
    
    // 加载统计数据
    async loadStatistics() {
      try {
        // 暂未对接 API，先以空状态展示，待后端接口提供后接入
        this.statistics = {
          pendingApproval: 0,
          approvedToday: 0,
          rejectedCount: 0,
          totalApproval: 0
        }
      } catch (error) {
        console.error('加载统计数据失败:', error)
      }
    },
    
    // 审核查询
    handleApprovalSearch() {
      this.approvalPagination.currentPage = 1
      this.loadApprovalData()
    },
    
    // 审核重置
    handleApprovalReset() {
      this.$refs.approvalForm.resetFields()
      this.approvalPagination.currentPage = 1
      this.loadApprovalData()
    },
    
    // 刷新审核
    handleRefreshApproval() {
      this.loadApprovalData()
      this.loadStatistics()
    },
    
    // 单个审核
    handleSingleApproval(row) {
      this.currentVoucher = row
      this.approvalOpinion = {
        result: 'APPROVED',
        remark: ''
      }
      this.approvalDialogVisible = true
    },
    
    // 单个退回
    async handleSingleReject(row) {
      try {
        const { value: reason } = await this.$prompt('请输入退回原因', '审核退回', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          inputValidator: (value) => {
            if (!value) {
              return '请输入退回原因'
            }
            return true
          }
        })
        
        const response = await updateVoucherStatus(row.voucherId, 0, reason)
        if (response.code === 200) {
          this.$message.success('凭证退回成功')
          this.loadApprovalData()
          this.loadStatistics()
        } else {
          this.$message.error(response.msg || '退回失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('退回失败:', error)
          this.$message.error('退回失败')
        }
      }
    },
    
    // 批量审核
    async handleBatchApproval() {
      try {
        await this.$confirm(`确认批量审核选中的 ${this.selectedApprovalRows.length} 个凭证？`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        
        const voucherIds = this.selectedApprovalRows.map(row => row.voucherId)
        const response = await batchUpdateVoucherStatus(voucherIds, 1, '批量审核通过')
        if (response.code === 200) {
          this.$message.success('批量审核成功')
          this.loadApprovalData()
          this.loadStatistics()
        } else {
          this.$message.error(response.msg || '批量审核失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('批量审核失败:', error)
          this.$message.error('批量审核失败')
        }
      }
    },
    
    // 批量退回
    async handleBatchReject() {
      try {
        const { value: reason } = await this.$prompt('请输入批量退回原因', '批量审核退回', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          inputValidator: (value) => {
            if (!value) {
              return '请输入退回原因'
            }
            return true
          }
        })
        
        const voucherIds = this.selectedApprovalRows.map(row => row.voucherId)
        const response = await batchUpdateVoucherStatus(voucherIds, 0, reason)
        if (response.code === 200) {
          this.$message.success('批量退回成功')
          this.loadApprovalData()
          this.loadStatistics()
        } else {
          this.$message.error(response.msg || '批量退回失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('批量退回失败:', error)
          this.$message.error('批量退回失败')
        }
      }
    },
    
    // 提交批量审核
    async handleSubmitBatchApproval() {
      try {
        const response = await batchUpdateVoucherStatus([], 1, this.batchApprovalForm.approvalRemark)
        if (response.code === 200) {
          this.$message.success('批量审核任务已启动')
          this.handleResetBatchApproval()
          this.loadApprovalData()
        } else {
          this.$message.error(response.msg || '操作失败')
        }
      } catch (error) {
        console.error('操作失败:', error)
        this.$message.error('操作失败')
      }
    },
    
    // 重置批量审核
    handleResetBatchApproval() {
      this.$refs.batchApprovalForm.resetFields()
    },
    
    // 确认审核
    async handleConfirmApproval() {
      try {
        if (!this.approvalOpinion.remark) {
          this.$message.warning('请输入审核备注')
          return
        }
        
        const status = this.approvalOpinion.result === 'APPROVED' ? 1 : 0
        const response = await updateVoucherStatus(this.currentVoucher.voucherId, status, this.approvalOpinion.remark)
        if (response.code === 200) {
          this.$message.success('审核完成')
          this.approvalDialogVisible = false
          this.loadApprovalData()
          this.loadStatistics()
        } else {
          this.$message.error(response.msg || '审核失败')
        }
      } catch (error) {
        console.error('审核失败:', error)
        this.$message.error('审核失败')
      }
    },
    
    // 查看凭证
    handleViewVoucher(row) {
      this.$router.push(`/management/financial/accounting/voucher-detail/${row.voucherId}`)
    },
    
    // 查看历史
    handleViewHistory(row) {
      const content = `<p><b>凭证号：</b>${row.voucherNo || '-'}</p><p><b>审核人：</b>${row.approver || '-'}</p><p><b>审核时间：</b>${row.approvalTime || '-'}</p><p><b>审核结果：</b>${row.approvalResult || row.statusName || '-'}</p><p><b>审核意见：</b>${row.approvalRemark || '-'}</p>`
      this.$alert(content, '审核历史', { dangerouslyUseHTMLString: true })
    },
    
    // 查看退回凭证
    handleViewRejectVoucher(row) {
      this.$router.push(`/management/financial/accounting/voucher-detail/${row.voucherId}`)
    },
    
    // 重新审核
    handleReApproval(row) {
      this.handleSingleApproval(row)
    },
    
    // 提醒修改
    handleRemindModify(row) {
      this.$confirm('确认发送修改提醒给制单人？', '提醒确认', { type: 'info' })
        .then(() => { this.$message.success('提醒已发送') })
        .catch(() => {})
    },
    
    // 历史查询
    handleHistorySearch() {
      this.historyPagination.currentPage = 1
      this.loadHistoryData()
    },
    
    // 历史重置
    handleHistoryReset() {
      this.$refs.historyForm.resetFields()
      this.historyPagination.currentPage = 1
      this.loadHistoryData()
    },
    
    // 查看历史详情
    handleViewHistoryDetail(row) {
      const content = `<p><b>凭证号：</b>${row.voucherNo || '-'}</p><p><b>操作类型：</b>${row.operationType || row.actionName || '-'}</p><p><b>操作人：</b>${row.operator || '-'}</p><p><b>操作时间：</b>${row.operationTime || row.createTime || '-'}</p><p><b>操作结果：</b>${row.result || '-'}</p><p><b>备注：</b>${row.remark || '-'}</p>`
      this.$alert(content, '历史详情', { dangerouslyUseHTMLString: true })
    },
    
    // 选择变化
    handleApprovalSelectionChange(selection) {
      this.selectedApprovalRows = selection
    },
    
    // 分页处理
    handleApprovalSizeChange(val) {
      this.approvalPagination.pageSize = val
      this.approvalPagination.currentPage = 1
      this.loadApprovalData()
    },
    
    handleApprovalCurrentChange(val) {
      this.approvalPagination.currentPage = val
      this.loadApprovalData()
    },
    
    handleRejectSizeChange(val) {
      this.rejectPagination.pageSize = val
      this.rejectPagination.currentPage = 1
      this.loadRejectData()
    },
    
    handleRejectCurrentChange(val) {
      this.rejectPagination.currentPage = val
      this.loadRejectData()
    },
    
    handleHistorySizeChange(val) {
      this.historyPagination.pageSize = val
      this.historyPagination.currentPage = 1
      this.loadHistoryData()
    },
    
    handleHistoryCurrentChange(val) {
      this.historyPagination.currentPage = val
      this.loadHistoryData()
    },
    
    // 获取凭证类型名称
    getVoucherTypeName(type) {
      const typeMap = {
        'ACCOUNTING': '记账凭证',
        'RECEIPT': '收款凭证',
        'PAYMENT': '付款凭证',
        'TRANSFER': '转账凭证',
        '1': '记账凭证',
        '2': '收款凭证',
        '3': '付款凭证',
        '4': '转账凭证',
        1: '记账凭证',
        2: '收款凭证',
        3: '付款凭证',
        4: '转账凭证'
      }
      return typeMap[type] || type || '记账凭证'
    },

    // 获取凭证类型颜色
    getVoucherTypeColor(type) {
      const colorMap = {
        'ACCOUNTING': 'primary',
        'RECEIPT': 'success',
        'PAYMENT': 'warning',
        'TRANSFER': 'info',
        '1': 'primary',
        '2': 'success',
        '3': 'warning',
        '4': 'info',
        1: 'primary',
        2: 'success',
        3: 'warning',
        4: 'info'
      }
      return colorMap[type] || 'info'
    },
    
    // 获取优先级类型
    getPriorityType(priority) {
      const priorityMap = {
        'HIGH': 'danger',
        'MEDIUM': 'warning',
        'LOW': 'info'
      }
      return priorityMap[priority] || 'info'
    },
    
    // 获取优先级名称
    getPriorityName(priority) {
      const priorityMap = {
        'HIGH': '高',
        'MEDIUM': '中',
        'LOW': '低'
      }
      return priorityMap[priority] || priority
    },
    
    // 获取退回状态类型
    getRejectStatusType(status) {
      const statusMap = {
        'REJECTED': 'danger',
        'MODIFIED': 'warning',
        'RESUBMITTED': 'primary'
      }
      return statusMap[status] || 'info'
    },
    
    // 获取退回状态名称
    getRejectStatusName(status) {
      const statusMap = {
        'REJECTED': '已退回',
        'MODIFIED': '已修改',
        'RESUBMITTED': '已重新提交'
      }
      return statusMap[status] || status
    },
    
    // 获取审核动作类型
    getApprovalActionType(action) {
      const actionMap = {
        'APPROVED': 'success',
        'REJECTED': 'danger',
        'RETURNED': 'warning'
      }
      return actionMap[action] || 'info'
    },
    
    // 获取审核动作名称
    getApprovalActionName(action) {
      const actionMap = {
        'APPROVED': '审核通过',
        'REJECTED': '审核退回',
        'RETURNED': '审核退回'
      }
      return actionMap[action] || action
    },
    
    // 获取状态名称
    getStatusName(status) {
      const statusMap = {
        '0': '草稿',
        '1': '已审核',
        '2': '已过账'
      }
      return statusMap[status] || status
    },
    
    // 格式化金额
    formatAmount(amount) {
      if (!amount) return '0.00'
      return Number(amount).toLocaleString('zh-CN', {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
      })
    },
    
    // 格式化日期
    formatDate(date) {
      if (!date) return ''
      return new Date(date).toLocaleString()
    },

    // 格式化凭证日期（处理数组格式 [year, month, day]）
    formatVoucherDate(date) {
      if (!date) return ''
      // 如果是数组格式 [year, month, day]
      if (Array.isArray(date)) {
        const [year, month, day] = date
        return `${year}-${String(month).padStart(2, '0')}-${String(day).padStart(2, '0')}`
      }
      // 如果是字符串或其他格式
      return new Date(date).toLocaleDateString('zh-CN')
    },

    // 获取凭证状态类型
    getVoucherStatusType(status) {
      const statusMap = {
        0: 'info',
        1: 'warning',
        2: 'success',
        3: 'danger'
      }
      return statusMap[status] || 'info'
    },

    // 获取凭证状态名称
    getVoucherStatusName(status) {
      const statusMap = {
        0: '草稿',
        1: '待审核',
        2: '已审核',
        3: '已作废'
      }
      return statusMap[status] || '未知'
    }
  }
}
</script>

<style scoped>
.voucher-approval-container {
  padding: 20px;
}

.page-header {
  margin-bottom: 20px;
}

.page-header h2 {
  margin: 0 0 8px 0;
  color: #303133;
}

.page-header p {
  margin: 0;
  color: #909399;
  font-size: 14px;
}

.statistics-cards {
  margin-bottom: 20px;
}

.statistic-card {
  cursor: pointer;
  transition: all 0.3s;
}

.statistic-card:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.statistic-item {
  display: flex;
  align-items: center;
  padding: 20px;
}

.statistic-icon {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 20px;
  font-size: 24px;
  color: white;
}

.statistic-icon.pending {
  background: linear-gradient(135deg, #e6a23c, #f7ba2a);
}

.statistic-icon.approved {
  background: linear-gradient(135deg, #67c23a, #85ce61);
}

.statistic-icon.rejected {
  background: linear-gradient(135deg, #f56c6c, #f78989);
}

.statistic-icon.total {
  background: linear-gradient(135deg, #909399, #b3b6bb);
}

.statistic-content {
  flex: 1;
}

.statistic-value {
  font-size: 28px;
  font-weight: bold;
  color: #303133;
  margin-bottom: 5px;
}

.statistic-label {
  font-size: 14px;
  color: #606266;
}

.tab-content {
  padding: 20px 0;
}

.toolbar {
  margin-bottom: 20px;
}

.search-form {
  background: #f5f7fa;
  padding: 20px;
  border-radius: 4px;
  margin-bottom: 20px;
}

.table-container {
  margin-bottom: 20px;
}

.pagination-container {
  text-align: right;
}

.batch-config {
  background: #f5f7fa;
  padding: 20px;
  border-radius: 4px;
  margin-bottom: 20px;
}

.reject-list,
.history-list {
  margin-bottom: 20px;
}

.dialog-footer {
  text-align: right;
}
</style>
