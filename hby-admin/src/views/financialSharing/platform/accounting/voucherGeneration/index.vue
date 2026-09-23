<template>
  <div class="voucher-generation-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>凭证生成</h2>
      <p>基于业务事项自动生成会计凭证，支持批量生成和预览功能</p>
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
                <div class="statistic-value">{{ statistics.pendingGeneration || 0 }}</div>
                <div class="statistic-label">待生成事项</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="statistic-card">
            <div class="statistic-item">
              <div class="statistic-icon processing">
                <i class="el-icon-loading"></i>
              </div>
              <div class="statistic-content">
                <div class="statistic-value">{{ statistics.processingGeneration || 0 }}</div>
                <div class="statistic-label">生成中任务</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="statistic-card">
            <div class="statistic-item">
              <div class="statistic-icon completed">
                <i class="el-icon-success"></i>
              </div>
              <div class="statistic-content">
                <div class="statistic-value">{{ statistics.completedGeneration || 0 }}</div>
                <div class="statistic-label">已生成凭证</div>
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
                <div class="statistic-value">{{ statistics.totalGeneration || 0 }}</div>
                <div class="statistic-label">生成总数</div>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 功能标签页 -->
    <el-tabs v-model="activeTab" type="card" @tab-click="handleTabClick">
      <!-- 自动生成凭证 -->
      <el-tab-pane label="自动生成凭证" name="auto-generation">
        <div class="tab-content">
          <!-- 查询条件 -->
          <div class="search-form">
            <el-form :model="autoGenerationForm" ref="autoGenerationForm" :inline="true" label-width="120px">
              <el-form-item label="事项类型">
                <el-select v-model="autoGenerationForm.transactionType" placeholder="请选择事项类型" clearable>
                  <el-option label="销售订单" value="SALES_ORDER" />
                  <el-option label="采购订单" value="PURCHASE_ORDER" />
                  <el-option label="费用报销" value="EXPENSE_REPORT" />
                  <el-option label="资产采购" value="ASSET_PURCHASE" />
                </el-select>
              </el-form-item>
              <el-form-item label="业务日期">
                <el-date-picker
                  v-model="autoGenerationForm.dateRange"
                  type="daterange"
                  range-separator="至"
                  start-placeholder="开始日期"
                  end-placeholder="结束日期"
                  format="yyyy-MM-dd"
                  value-format="yyyy-MM-dd"
                />
              </el-form-item>
              <el-form-item label="处理状态">
                <el-select v-model="autoGenerationForm.processStatus" placeholder="请选择处理状态" clearable>
                  <el-option label="待处理" value="0" />
                  <el-option label="处理中" value="1" />
                  <el-option label="已完成" value="2" />
                </el-select>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="handleAutoGenerationSearch">查询</el-button>
                <el-button @click="handleAutoGenerationReset">重置</el-button>
              </el-form-item>
            </el-form>
          </div>

          <!-- 操作工具栏 -->
          <div class="toolbar">
            <el-button type="primary" icon="el-icon-magic-stick" @click="handlePreviewGeneration" :disabled="selectedAutoRows.length === 0">
              预览生成
            </el-button>
            <el-button type="success" icon="el-icon-check" @click="handleBatchGeneration" :disabled="selectedAutoRows.length === 0">
              批量生成
            </el-button>
            <el-button type="warning" icon="el-icon-refresh" @click="handleRefreshAutoGeneration">
              刷新
            </el-button>
          </div>

          <!-- 事项数据表格 -->
          <div class="table-container">
            <el-table
              :data="autoGenerationTableData"
              v-loading="autoGenerationLoading"
              border
              stripe
              height="400"
              @selection-change="handleAutoSelectionChange"
            >
              <el-table-column type="selection" width="55" />
              <el-table-column prop="transactionNo" label="事项编号" width="150" />
              <el-table-column prop="transactionType" label="事项类型" width="120">
                <template slot-scope="scope">
                  <el-tag :type="getTransactionTypeColor(scope.row.transactionType)">
                    {{ scope.row.transactionTypeName || getTransactionTypeName(scope.row.transactionType) }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="transactionDate" label="业务日期" width="120">
                <template slot-scope="scope">
                  {{ formatDate(scope.row.transactionDate) }}
                </template>
              </el-table-column>
              <el-table-column label="事项金额" width="120" align="right">
                <template slot-scope="scope">
                  <span style="color: #f56c6c; font-weight: bold;">
                    ¥{{ formatAmount(getAmountFromBusinessData(scope.row)) }}
                  </span>
                </template>
              </el-table-column>
              <el-table-column prop="transactionStatus" label="处理状态" width="100">
                <template slot-scope="scope">
                  <el-tag :type="getProcessStatusType(scope.row.transactionStatus)">
                    {{ scope.row.transactionStatusName || getProcessStatusName(scope.row.transactionStatus) }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="sourceSystem" label="来源系统" width="100" />
              <el-table-column label="业务摘要" min-width="200" show-overflow-tooltip>
                <template slot-scope="scope">
                  {{ getSummaryFromBusinessData(scope.row) }}
                </template>
              </el-table-column>
              <el-table-column prop="createTime" label="创建时间" width="160">
                <template slot-scope="scope">
                  {{ formatDate(scope.row.createTime) }}
                </template>
              </el-table-column>
              <el-table-column label="操作" width="200" fixed="right">
                <template slot-scope="scope">
                  <el-button size="mini" @click="handleViewTransaction(scope.row)">查看</el-button>
                  <el-button size="mini" type="primary" @click="handleSingleGeneration(scope.row)" v-if="scope.row.transactionStatus === 1">
                    生成凭证
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
          </div>

          <!-- 分页组件 -->
          <div class="pagination-container">
            <el-pagination
              @size-change="handleAutoSizeChange"
              @current-change="handleAutoCurrentChange"
              :current-page="autoGenerationPagination.currentPage"
              :page-sizes="[10, 20, 50, 100]"
              :page-size="autoGenerationPagination.pageSize"
              layout="total, sizes, prev, pager, next, jumper"
              :total="autoGenerationPagination.total"
            />
          </div>
        </div>
      </el-tab-pane>

      <!-- 批量生成凭证 -->
      <el-tab-pane label="批量生成凭证" name="batch-generation">
        <div class="tab-content">
          <!-- 批量生成配置 -->
          <div class="batch-config">
            <el-form :model="batchGenerationForm" ref="batchGenerationForm" label-width="120px">
              <el-row :gutter="20">
                <el-col :span="12">
                  <el-form-item label="生成模式" required>
                    <el-radio-group v-model="batchGenerationForm.generationMode">
                      <el-radio label="BY_DATE">按日期生成</el-radio>
                      <el-radio label="BY_TYPE">按事项类型生成</el-radio>
                      <el-radio label="BY_SYSTEM">按来源系统生成</el-radio>
                    </el-radio-group>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="生成策略" required>
                    <el-radio-group v-model="batchGenerationForm.generationStrategy">
                      <el-radio label="IMMEDIATE">立即生成</el-radio>
                      <el-radio label="SCHEDULED">定时生成</el-radio>
                    </el-radio-group>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row :gutter="20">
                <el-col :span="12">
                  <el-form-item label="业务日期范围" required>
                    <el-date-picker
                      v-model="batchGenerationForm.businessDateRange"
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
                  <el-form-item label="凭证日期" required>
                    <el-date-picker
                      v-model="batchGenerationForm.voucherDate"
                      type="date"
                      placeholder="选择凭证日期"
                      format="yyyy-MM-dd"
                      value-format="yyyy-MM-dd"
                    />
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row :gutter="20" v-if="batchGenerationForm.generationStrategy === 'SCHEDULED'">
                <el-col :span="12">
                  <el-form-item label="定时执行时间">
                    <el-date-picker
                      v-model="batchGenerationForm.scheduledTime"
                      type="datetime"
                      placeholder="选择执行时间"
                      format="yyyy-MM-dd HH:mm:ss"
                      value-format="yyyy-MM-dd HH:mm:ss"
                    />
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="重复执行">
                    <el-select v-model="batchGenerationForm.repeatMode" placeholder="请选择重复模式">
                      <el-option label="不重复" value="NONE" />
                      <el-option label="每日" value="DAILY" />
                      <el-option label="每周" value="WEEKLY" />
                      <el-option label="每月" value="MONTHLY" />
                    </el-select>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-form-item label="生成描述">
                <el-input
                  v-model="batchGenerationForm.description"
                  type="textarea"
                  :rows="3"
                  placeholder="请输入生成任务描述"
                />
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="handleSubmitBatchGeneration">
                  {{ batchGenerationForm.generationStrategy === 'IMMEDIATE' ? '立即执行' : '创建定时任务' }}
                </el-button>
                <el-button @click="handleResetBatchGeneration">重置</el-button>
              </el-form-item>
            </el-form>
          </div>
        </div>
      </el-tab-pane>

      <!-- 生成预览 -->
      <el-tab-pane label="生成预览" name="generation-preview">
        <div class="tab-content">
          <div class="preview-container" v-if="previewData.length > 0">
            <div class="preview-header">
              <h3>凭证生成预览</h3>
              <p>预览将要生成的凭证信息，确认无误后可执行生成操作</p>
            </div>
            
            <!-- 预览统计 -->
            <div class="preview-stats">
              <el-row :gutter="20">
                <el-col :span="6">
                  <div class="preview-stat-item">
                    <div class="stat-value">{{ previewData.length }}</div>
                    <div class="stat-label">预计生成凭证数</div>
                  </div>
                </el-col>
                <el-col :span="6">
                  <div class="preview-stat-item">
                    <div class="stat-value">{{ calculateTotalAmount() }}</div>
                    <div class="stat-label">预计总金额</div>
                  </div>
                </el-col>
                <el-col :span="6">
                  <div class="preview-stat-item">
                    <div class="stat-value">{{ calculateTotalEntries() }}</div>
                    <div class="stat-label">预计分录条数</div>
                  </div>
                </el-col>
                <el-col :span="6">
                  <div class="preview-stat-item">
                    <div class="stat-value">{{ selectedTransactionIds.length }}</div>
                    <div class="stat-label">选中事项数</div>
                  </div>
                </el-col>
              </el-row>
            </div>

            <!-- 预览表格 -->
            <div class="preview-table">
              <el-table
                :data="previewData"
                border
                stripe
                height="400"
              >
                <el-table-column prop="transactionNo" label="事项编号" width="120" />
                <el-table-column prop="voucherNo" label="预计凭证号" width="120" />
                <el-table-column prop="voucherType" label="凭证类型" width="100" />
                <el-table-column prop="voucherDate" label="凭证日期" width="120" />
                <el-table-column prop="summary" label="摘要" width="200" show-overflow-tooltip />
                <el-table-column prop="totalAmount" label="金额" width="120" align="right">
                  <template slot-scope="scope">
                    <span style="color: #f56c6c; font-weight: bold;">
                      ¥{{ formatAmount(scope.row.totalAmount) }}
                    </span>
                  </template>
                </el-table-column>
                <el-table-column prop="entryCount" label="分录条数" width="100" align="center" />
                <el-table-column label="操作" width="150" fixed="right">
                  <template slot-scope="scope">
                    <el-button size="mini" @click="handleViewPreviewDetail(scope.row)">查看分录</el-button>
                  </template>
                </el-table-column>
              </el-table>
            </div>

            <!-- 预览操作 -->
            <div class="preview-actions">
              <el-button type="primary" @click="handleConfirmGeneration">确认生成</el-button>
              <el-button @click="handleCancelPreview">取消预览</el-button>
            </div>
          </div>
          <div v-else class="empty-preview">
            <el-empty description="暂无预览数据，请先在自动生成页面选择事项并点击预览生成"></el-empty>
          </div>
        </div>
      </el-tab-pane>

      <!-- 生成进度监控 -->
      <el-tab-pane label="生成进度监控" name="generation-progress">
        <div class="tab-content">
          <!-- 操作工具栏 -->
          <div class="toolbar">
            <el-button type="primary" icon="el-icon-refresh" @click="loadProgressData">刷新进度</el-button>
            <el-button type="warning" icon="el-icon-delete" @click="handleClearProgressHistory">清空历史</el-button>
          </div>

          <!-- 进度监控列表 -->
          <div class="progress-list" v-if="progressTableData.length > 0">
            <el-table
              :data="progressTableData"
              v-loading="progressLoading"
              border
              stripe
              height="500"
            >
              <el-table-column prop="batchId" label="批次ID" width="120" />
              <el-table-column prop="taskName" label="任务名称" width="200" />
              <el-table-column prop="generationMode" label="生成模式" width="120">
                <template slot-scope="scope">
                  <el-tag>{{ getGenerationModeName(scope.row.generationMode) }}</el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="totalCount" label="总数量" width="100" align="center" />
              <el-table-column prop="completedCount" label="已完成" width="100" align="center" />
              <el-table-column prop="failedCount" label="失败数" width="100" align="center" />
              <el-table-column prop="progress" label="进度" width="150">
                <template slot-scope="scope">
                  <el-progress :percentage="scope.row.progress" :status="getProgressStatus(scope.row.status)"></el-progress>
                </template>
              </el-table-column>
              <el-table-column prop="status" label="状态" width="100">
                <template slot-scope="scope">
                  <el-tag :type="getTaskStatusType(scope.row.status)">
                    {{ getTaskStatusName(scope.row.status) }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="startTime" label="开始时间" width="160">
                <template slot-scope="scope">
                  {{ formatDate(scope.row.startTime) }}
                </template>
              </el-table-column>
              <el-table-column label="操作" width="200" fixed="right">
                <template slot-scope="scope">
                  <el-button size="mini" @click="handleViewProgress(scope.row)">查看详情</el-button>
                  <el-button size="mini" type="warning" @click="handleStopTask(scope.row)" v-if="scope.row.status === 'RUNNING'">
                    停止任务
                  </el-button>
                  <el-button size="mini" type="danger" @click="handleDeleteTask(scope.row)" v-if="scope.row.status !== 'RUNNING'">
                    删除
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
          </div>

          <!-- 分页组件 -->
          <div class="pagination-container" v-if="progressTableData.length > 0">
            <el-pagination
              @size-change="handleProgressSizeChange"
              @current-change="handleProgressCurrentChange"
              :current-page="progressPagination.currentPage"
              :page-sizes="[10, 20, 50, 100]"
              :page-size="progressPagination.pageSize"
              layout="total, sizes, prev, pager, next, jumper"
              :total="progressPagination.total"
            />
          </div>

          <!-- 空状态提示 -->
          <div class="empty-progress" v-if="progressTableData.length === 0 && !progressLoading">
            <el-empty description="暂无生成任务记录">
              <el-button type="primary" @click="activeTab = 'auto-generation'">去生成凭证</el-button>
            </el-empty>
          </div>
        </div>
      </el-tab-pane>
    </el-tabs>

    <!-- 事项详情弹窗 -->
    <el-dialog title="业务事项详情" :visible.sync="transactionDetailVisible" width="700px">
      <div v-if="currentTransaction" class="transaction-detail">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="事项编号">{{ currentTransaction.transactionNo }}</el-descriptions-item>
          <el-descriptions-item label="事项类型">
            <el-tag :type="getTransactionTypeColor(currentTransaction.transactionType)">
              {{ currentTransaction.transactionTypeName || getTransactionTypeName(currentTransaction.transactionType) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="业务日期">{{ formatDate(currentTransaction.transactionDate) }}</el-descriptions-item>
          <el-descriptions-item label="处理状态">
            <el-tag :type="getProcessStatusType(currentTransaction.transactionStatus)">
              {{ currentTransaction.transactionStatusName || getProcessStatusName(currentTransaction.transactionStatus) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="来源系统">{{ currentTransaction.sourceSystem }}</el-descriptions-item>
          <el-descriptions-item label="事项金额">
            <span style="color: #f56c6c; font-weight: bold;">¥{{ formatAmount(getAmountFromBusinessData(currentTransaction)) }}</span>
          </el-descriptions-item>
          <el-descriptions-item label="创建时间">{{ formatDate(currentTransaction.createTime) }}</el-descriptions-item>
          <el-descriptions-item label="更新时间">{{ formatDate(currentTransaction.updateTime) }}</el-descriptions-item>
        </el-descriptions>

        <el-divider content-position="left">业务数据</el-divider>
        <div class="business-data-detail">
          <el-descriptions :column="2" border v-if="parseBusinessData(currentTransaction)">
            <el-descriptions-item v-for="(value, key) in parseBusinessData(currentTransaction)" :key="key" :label="getBusinessDataLabel(key)">
              {{ value }}
            </el-descriptions-item>
          </el-descriptions>
        </div>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="transactionDetailVisible = false">关 闭</el-button>
        <el-button type="primary" @click="handleSingleGeneration(currentTransaction)" v-if="currentTransaction && currentTransaction.transactionStatus === 1">
          生成凭证
        </el-button>
      </span>
    </el-dialog>

    <!-- 预览详情弹窗 -->
    <el-dialog title="凭证预览详情" :visible.sync="previewDetailVisible" width="800px">
      <div v-if="currentPreviewDetail" class="preview-detail">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="事项编号">{{ currentPreviewDetail.transactionNo }}</el-descriptions-item>
          <el-descriptions-item label="预计凭证号">{{ currentPreviewDetail.voucherNo }}</el-descriptions-item>
          <el-descriptions-item label="凭证类型">{{ currentPreviewDetail.voucherType }}</el-descriptions-item>
          <el-descriptions-item label="凭证日期">{{ currentPreviewDetail.voucherDate }}</el-descriptions-item>
          <el-descriptions-item label="摘要" :span="2">{{ currentPreviewDetail.summary }}</el-descriptions-item>
          <el-descriptions-item label="总金额">
            <span style="color: #f56c6c; font-weight: bold;">¥{{ formatAmount(currentPreviewDetail.totalAmount) }}</span>
          </el-descriptions-item>
          <el-descriptions-item label="分录条数">{{ currentPreviewDetail.entryCount }}</el-descriptions-item>
        </el-descriptions>

        <el-divider content-position="left">分录明细</el-divider>
        <el-table :data="currentPreviewDetail.entries || []" border stripe max-height="300">
          <el-table-column prop="summary" label="摘要" min-width="150" />
          <el-table-column prop="accountCode" label="科目编码" width="120" />
          <el-table-column prop="accountName" label="科目名称" width="150" />
          <el-table-column prop="debitAmount" label="借方金额" width="120" align="right">
            <template slot-scope="scope">
              <span v-if="scope.row.debitAmount" style="color: #67c23a;">{{ formatAmount(scope.row.debitAmount) }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="creditAmount" label="贷方金额" width="120" align="right">
            <template slot-scope="scope">
              <span v-if="scope.row.creditAmount" style="color: #f56c6c;">{{ formatAmount(scope.row.creditAmount) }}</span>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="previewDetailVisible = false">关 闭</el-button>
      </span>
    </el-dialog>

    <!-- 进度详情弹窗 -->
    <el-dialog title="生成进度详情" :visible.sync="progressDetailVisible" width="800px">
      <div v-if="currentProgressDetail" class="progress-detail">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="批次ID">{{ currentProgressDetail.batchId }}</el-descriptions-item>
          <el-descriptions-item label="任务名称">{{ currentProgressDetail.taskName }}</el-descriptions-item>
          <el-descriptions-item label="生成模式">{{ getGenerationModeName(currentProgressDetail.generationMode) }}</el-descriptions-item>
          <el-descriptions-item label="任务状态">
            <el-tag :type="getTaskStatusType(currentProgressDetail.status)">
              {{ getTaskStatusName(currentProgressDetail.status) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="总数量">{{ currentProgressDetail.totalCount }}</el-descriptions-item>
          <el-descriptions-item label="已完成">{{ currentProgressDetail.completedCount }}</el-descriptions-item>
          <el-descriptions-item label="失败数">{{ currentProgressDetail.failedCount }}</el-descriptions-item>
          <el-descriptions-item label="进度">
            <el-progress :percentage="currentProgressDetail.progress || 0" :status="getProgressStatus(currentProgressDetail.status)" />
          </el-descriptions-item>
          <el-descriptions-item label="开始时间">{{ formatDate(currentProgressDetail.startTime) }}</el-descriptions-item>
          <el-descriptions-item label="预计结束">{{ formatDate(currentProgressDetail.estimatedEndTime) }}</el-descriptions-item>
        </el-descriptions>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="progressDetailVisible = false">关 闭</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import {
  batchGenerateVouchers,
  previewVoucherGeneration,
  getVoucherGenerationProgress
} from '@/api/financialSharing/voucher'
import {
  getBusinessTransactionPage
} from '@/api/financialSharing/transaction'

export default {
  name: 'VoucherGeneration',
  data() {
    return {
      activeTab: 'auto-generation',
      statistics: {},
      
      // 自动生成相关
      autoGenerationLoading: false,
      autoGenerationTableData: [],
      selectedAutoRows: [],
      autoGenerationForm: {
        transactionType: '',
        dateRange: [],
        processStatus: ''
      },
      autoGenerationPagination: {
        currentPage: 1,
        pageSize: 20,
        total: 0
      },
      
      // 批量生成相关
      batchGenerationForm: {
        generationMode: 'BY_DATE',
        generationStrategy: 'IMMEDIATE',
        businessDateRange: [],
        voucherDate: '',
        scheduledTime: '',
        repeatMode: 'NONE',
        description: ''
      },
      
      // 预览相关
      previewData: [],
      selectedTransactionIds: [],
      
      // 进度监控相关
      progressLoading: false,
      progressTableData: [],
      progressPagination: {
        currentPage: 1,
        pageSize: 20,
        total: 0
      },

      // 弹窗相关
      transactionDetailVisible: false,
      currentTransaction: null,
      previewDetailVisible: false,
      currentPreviewDetail: null,
      progressDetailVisible: false,
      currentProgressDetail: null
    }
  },
  
  mounted() {
    this.loadAutoGenerationData()
    this.loadProgressData()
    this.loadStatistics()
  },
  
  methods: {
    // 标签页切换
    handleTabClick(tab) {
      console.log('切换到标签页:', tab.name)
      if (tab.name === 'generation-progress') {
        console.log('开始加载进度数据...')
        this.loadProgressData()
      } else if (tab.name === 'generation-preview') {
        // 如果预览数据为空，提示用户
        if (this.previewData.length === 0) {
          this.$message.info('请先在"自动生成凭证"页面选择事项并点击"预览生成"')
        }
      }
    },
    
    // 加载自动生成数据
    async loadAutoGenerationData() {
      this.autoGenerationLoading = true
      try {
        // 构建符合后端 BusinessTransactionQueryParam 结构的参数
        // 注意：不传递 null 值的字段，避免 MyBatis 类型推断问题
        const params = {
          pageNumber: this.autoGenerationPagination.currentPage,
          pageSize: this.autoGenerationPagination.pageSize,
          // 后端必填参数 bookId 和 tenantId，从 store 获取或使用默认值
          bookId: this.$store.getters['user/bookId'] || 1,
          tenantId: this.$store.getters['user/tenantId'] || this.$store.getters['user/orgId'] || 1
        }

        // 只有当字段有值时才添加到参数中，避免传递 null 值
        if (this.autoGenerationForm.transactionType) {
          params.transactionType = this.autoGenerationForm.transactionType
        }
        if (this.autoGenerationForm.dateRange && this.autoGenerationForm.dateRange[0]) {
          params.transactionDateStart = this.autoGenerationForm.dateRange[0]
        }
        if (this.autoGenerationForm.dateRange && this.autoGenerationForm.dateRange[1]) {
          params.transactionDateEnd = this.autoGenerationForm.dateRange[1]
        }
        if (this.autoGenerationForm.processStatus) {
          params.transactionStatus = parseInt(this.autoGenerationForm.processStatus)
        }

        const response = await getBusinessTransactionPage(params)
        if (response.code === 200 || response.code === 1) {
          this.autoGenerationTableData = response.data.tlist || response.data.list || []
          this.autoGenerationPagination.total = response.data.totalRecord || response.data.total || 0
        } else {
          this.$message.error(response.msg || '查询失败')
        }
      } catch (error) {
        console.error('加载数据失败:', error)
        this.$message.error('加载数据失败')
      } finally {
        this.autoGenerationLoading = false
      }
    },
    
    // 加载进度数据
    async loadProgressData() {
      this.progressLoading = true
      try {
        // 从本地存储获取批次ID列表
        const batchIds = JSON.parse(localStorage.getItem('voucherGenerationBatchIds') || '[]')

        if (batchIds.length > 0) {
          // 获取最近的批次进度
          const progressList = []
          for (const batchId of batchIds.slice(-10)) { // 只获取最近10个
            try {
              const response = await getVoucherGenerationProgress(batchId)
              if (response.code === 200 || response.code === 1) {
                progressList.push(response.data)
              }
            } catch (e) {
              console.warn('获取批次进度失败:', batchId, e)
            }
          }

          if (progressList.length > 0) {
            this.progressTableData = progressList
            this.progressPagination.total = progressList.length
            return
          }
        }

        // 如果没有真实数据，显示空列表或提示
        this.progressTableData = []
        this.progressPagination.total = 0
      } catch (error) {
        console.error('加载进度数据失败:', error)
        this.progressTableData = []
        this.progressPagination.total = 0
      } finally {
        this.progressLoading = false
      }
    },
    
    // 加载统计数据
    async loadStatistics() {
      try {
        // 暂未对接 API，先以空状态展示，待后端接口提供后接入
        this.statistics = {
          pendingGeneration: 0,
          processingGeneration: 0,
          completedGeneration: 0,
          totalGeneration: 0
        }
      } catch (error) {
        console.error('加载统计数据失败:', error)
      }
    },
    
    // 自动生成查询
    handleAutoGenerationSearch() {
      this.autoGenerationPagination.currentPage = 1
      this.loadAutoGenerationData()
    },
    
    // 自动生成重置
    handleAutoGenerationReset() {
      this.$refs.autoGenerationForm.resetFields()
      this.autoGenerationPagination.currentPage = 1
      this.loadAutoGenerationData()
    },
    
    // 刷新自动生成
    handleRefreshAutoGeneration() {
      this.loadAutoGenerationData()
    },
    
    // 预览生成
    async handlePreviewGeneration() {
      try {
        const transactionIds = this.selectedAutoRows.map(row => row.transactionId)
        const response = await previewVoucherGeneration(transactionIds)
        if (response.code === 200) {
          this.previewData = response.data || []
          this.selectedTransactionIds = transactionIds
          this.activeTab = 'generation-preview'
          this.$message.success('预览生成成功')
        } else {
          this.$message.error(response.msg || '预览生成失败')
        }
      } catch (error) {
        console.error('预览生成失败:', error)
        this.$message.error('预览生成失败')
      }
    },
    
    // 批量生成
    async handleBatchGeneration() {
      try {
        await this.$confirm('确认批量生成选中事项的凭证？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        
        const transactionIds = this.selectedAutoRows.map(row => row.transactionId)
        const response = await batchGenerateVouchers({ transactionIds })
        if (response.code === 200) {
          this.$message.success('批量生成任务已启动')
          this.loadAutoGenerationData()
          this.loadProgressData()
          this.activeTab = 'generation-progress'
        } else {
          this.$message.error(response.msg || '批量生成失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('批量生成失败:', error)
          this.$message.error('批量生成失败')
        }
      }
    },
    
    // 单个生成
    async handleSingleGeneration(row) {
      try {
        await this.$confirm('确认生成该事项的凭证？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        
        const response = await batchGenerateVouchers({ transactionIds: [row.transactionId] })
        if (response.code === 200) {
          this.$message.success('凭证生成任务已启动')
          this.loadAutoGenerationData()
        } else {
          this.$message.error(response.msg || '生成失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('生成失败:', error)
          this.$message.error('生成失败')
        }
      }
    },
    
    // 提交批量生成
    async handleSubmitBatchGeneration() {
      try {
        const response = await batchGenerateVouchers(this.batchGenerationForm)
        if (response.code === 200 || response.code === 1) {
          // 保存批次ID到本地存储，用于进度监控
          if (response.data && response.data.batchId) {
            const batchIds = JSON.parse(localStorage.getItem('voucherGenerationBatchIds') || '[]')
            batchIds.push(response.data.batchId)
            // 只保留最近20个批次
            if (batchIds.length > 20) {
              batchIds.splice(0, batchIds.length - 20)
            }
            localStorage.setItem('voucherGenerationBatchIds', JSON.stringify(batchIds))
          }

          this.$message.success(this.batchGenerationForm.generationStrategy === 'IMMEDIATE' ? '批量生成任务已启动' : '定时任务创建成功')
          this.handleResetBatchGeneration()
          this.loadProgressData()
          this.activeTab = 'generation-progress'
        } else {
          this.$message.error(response.msg || '操作失败')
        }
      } catch (error) {
        console.error('操作失败:', error)
        this.$message.error('操作失败')
      }
    },
    
    // 重置批量生成
    handleResetBatchGeneration() {
      this.$refs.batchGenerationForm.resetFields()
    },
    
    // 确认生成
    async handleConfirmGeneration() {
      try {
        await this.$confirm('确认生成预览中的所有凭证？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        
        const response = await batchGenerateVouchers({ transactionIds: this.selectedTransactionIds })
        if (response.code === 200) {
          this.$message.success('凭证生成任务已启动')
          this.handleCancelPreview()
          this.loadAutoGenerationData()
          this.loadProgressData()
          this.activeTab = 'generation-progress'
        } else {
          this.$message.error(response.msg || '生成失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('生成失败:', error)
          this.$message.error('生成失败')
        }
      }
    },
    
    // 取消预览
    handleCancelPreview() {
      this.previewData = []
      this.selectedTransactionIds = []
      this.activeTab = 'auto-generation'
    },
    
    // 查看事项
    handleViewTransaction(row) {
      this.currentTransaction = row
      this.transactionDetailVisible = true
    },

    // 查看预览详情
    handleViewPreviewDetail(row) {
      this.currentPreviewDetail = row
      this.previewDetailVisible = true
    },

    // 查看进度
    handleViewProgress(row) {
      this.currentProgressDetail = row
      this.progressDetailVisible = true
    },
    
    // 停止任务
    async handleStopTask(row) {
      try {
        await this.$confirm('确认停止该生成任务？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        
        this.$message.success('任务停止成功')
        this.loadProgressData()
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('停止任务失败')
        }
      }
    },
    
    // 删除任务
    async handleDeleteTask(row) {
      try {
        await this.$confirm('确认删除该生成任务？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })

        // 从本地存储中移除该批次ID
        const batchIds = JSON.parse(localStorage.getItem('voucherGenerationBatchIds') || '[]')
        const newBatchIds = batchIds.filter(id => id !== row.batchId)
        localStorage.setItem('voucherGenerationBatchIds', JSON.stringify(newBatchIds))

        this.$message.success('任务删除成功')
        this.loadProgressData()
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('删除任务失败')
        }
      }
    },

    // 清空进度历史
    async handleClearProgressHistory() {
      try {
        await this.$confirm('确认清空所有生成任务历史记录？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })

        localStorage.removeItem('voucherGenerationBatchIds')
        this.progressTableData = []
        this.progressPagination.total = 0
        this.$message.success('历史记录已清空')
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('清空历史记录失败')
        }
      }
    },
    
    // 选择变化
    handleAutoSelectionChange(selection) {
      this.selectedAutoRows = selection
    },
    
    // 分页处理
    handleAutoSizeChange(val) {
      this.autoGenerationPagination.pageSize = val
      this.autoGenerationPagination.currentPage = 1
      this.loadAutoGenerationData()
    },
    
    handleAutoCurrentChange(val) {
      this.autoGenerationPagination.currentPage = val
      this.loadAutoGenerationData()
    },
    
    handleProgressSizeChange(val) {
      this.progressPagination.pageSize = val
      this.progressPagination.currentPage = 1
      this.loadProgressData()
    },
    
    handleProgressCurrentChange(val) {
      this.progressPagination.currentPage = val
      this.loadProgressData()
    },
    
    // 计算预览统计
    calculateTotalAmount() {
      const total = this.previewData.reduce((sum, item) => sum + (item.totalAmount || 0), 0)
      return `¥${this.formatAmount(total)}`
    },
    
    calculateTotalEntries() {
      return this.previewData.reduce((sum, item) => sum + (item.entryCount || 0), 0)
    },
    
    // 获取事项类型名称
    getTransactionTypeName(type) {
      const typeMap = {
        'SALES_ORDER': '销售订单',
        'PURCHASE_ORDER': '采购订单',
        'EXPENSE_REPORT': '费用报销',
        'ASSET_PURCHASE': '资产采购'
      }
      return typeMap[type] || type
    },
    
    // 获取事项类型颜色
    getTransactionTypeColor(type) {
      const colorMap = {
        'SALES_ORDER': 'success',
        'PURCHASE_ORDER': 'primary',
        'EXPENSE_REPORT': 'warning',
        'ASSET_PURCHASE': 'danger'
      }
      return colorMap[type] || 'info'
    },
    
    // 获取处理状态类型
    getProcessStatusType(status) {
      const statusMap = {
        0: 'warning',
        1: 'warning',
        2: 'success',
        '0': 'warning',
        '1': 'warning',
        '2': 'success'
      }
      return statusMap[status] || 'info'
    },

    // 获取处理状态名称
    getProcessStatusName(status) {
      const statusMap = {
        0: '待处理',
        1: '待处理',
        2: '已处理',
        '0': '待处理',
        '1': '待处理',
        '2': '已处理'
      }
      return statusMap[status] || status
    },

    // 从业务数据中获取金额
    getAmountFromBusinessData(row) {
      if (row.transactionAmount) return row.transactionAmount
      try {
        const businessData = typeof row.businessData === 'string' ? JSON.parse(row.businessData) : row.businessData
        return businessData?.amount || 0
      } catch (e) {
        return 0
      }
    },

    // 从业务数据中获取摘要
    getSummaryFromBusinessData(row) {
      if (row.summary) return row.summary
      try {
        const businessData = typeof row.businessData === 'string' ? JSON.parse(row.businessData) : row.businessData
        if (!businessData) return ''

        // 根据事项类型生成摘要
        switch (row.transactionType) {
          case 'SALES_ORDER':
            return `客户: ${businessData.customerName || ''}, 产品: ${businessData.productName || ''}`
          case 'PURCHASE_ORDER':
            return `供应商: ${businessData.supplierName || ''}, 产品: ${businessData.productName || ''}`
          case 'EXPENSE_REPORT':
            return `${businessData.employeeName || ''} - ${businessData.expenseType || ''}: ${businessData.description || ''}`
          case 'ASSET_PURCHASE':
            return `资产: ${businessData.assetName || ''}, 位置: ${businessData.location || ''}`
          default:
            return businessData.description || JSON.stringify(businessData).substring(0, 50)
        }
      } catch (e) {
        return ''
      }
    },
    
    // 获取生成模式名称
    getGenerationModeName(mode) {
      const modeMap = {
        'BY_DATE': '按日期',
        'BY_TYPE': '按类型',
        'BY_SYSTEM': '按系统'
      }
      return modeMap[mode] || mode
    },
    
    // 获取任务状态类型
    getTaskStatusType(status) {
      const statusMap = {
        'RUNNING': 'primary',
        'COMPLETED': 'success',
        'FAILED': 'danger'
      }
      return statusMap[status] || 'info'
    },
    
    // 获取任务状态名称
    getTaskStatusName(status) {
      const statusMap = {
        'RUNNING': '运行中',
        'COMPLETED': '已完成',
        'FAILED': '失败'
      }
      return statusMap[status] || status
    },
    
    // 获取进度状态
    getProgressStatus(status) {
      const statusMap = {
        'RUNNING': null,
        'COMPLETED': 'success',
        'FAILED': 'exception'
      }
      return statusMap[status]
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
      const d = new Date(date)
      if (isNaN(d.getTime())) return date
      return d.toLocaleString('zh-CN', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit'
      }).replace(/\//g, '-')
    },

    // 解析业务数据
    parseBusinessData(row) {
      if (!row || !row.businessData) return null
      try {
        return typeof row.businessData === 'string' ? JSON.parse(row.businessData) : row.businessData
      } catch (e) {
        return null
      }
    },

    // 获取业务数据字段标签
    getBusinessDataLabel(key) {
      const labelMap = {
        'customerName': '客户名称',
        'supplierName': '供应商名称',
        'employeeName': '员工姓名',
        'department': '部门',
        'amount': '金额',
        'currency': '币种',
        'productName': '产品名称',
        'quantity': '数量',
        'expenseType': '费用类型',
        'description': '描述',
        'assetName': '资产名称',
        'assetCategory': '资产类别',
        'location': '位置'
      }
      return labelMap[key] || key
    }
  }
}
</script>

<style scoped>
.voucher-generation-container {
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

.statistic-icon.processing {
  background: linear-gradient(135deg, #409eff, #66b1ff);
}

.statistic-icon.completed {
  background: linear-gradient(135deg, #67c23a, #85ce61);
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

.preview-container {
  background: #f5f7fa;
  padding: 20px;
  border-radius: 4px;
}

.preview-header {
  margin-bottom: 20px;
}

.preview-header h3 {
  margin: 0 0 8px 0;
  color: #303133;
}

.preview-header p {
  margin: 0;
  color: #909399;
  font-size: 14px;
}

.preview-stats {
  margin-bottom: 20px;
}

.preview-stat-item {
  text-align: center;
  padding: 20px;
  background: white;
  border-radius: 4px;
}

.preview-stat-item .stat-value {
  font-size: 24px;
  font-weight: bold;
  color: #303133;
  margin-bottom: 5px;
}

.preview-stat-item .stat-label {
  font-size: 14px;
  color: #606266;
}

.preview-table {
  margin-bottom: 20px;
}

.preview-actions {
  text-align: center;
}

.empty-preview {
  text-align: center;
  padding: 40px;
}

.progress-list {
  margin-bottom: 20px;
}

/* 弹窗样式 */
.transaction-detail,
.preview-detail,
.progress-detail {
  padding: 10px 0;
}

.business-data-detail {
  margin-top: 10px;
}

.dialog-footer {
  text-align: right;
}
</style>
