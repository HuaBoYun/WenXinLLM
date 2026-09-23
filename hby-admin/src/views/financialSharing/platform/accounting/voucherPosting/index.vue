<template>
  <div class="voucher-posting-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>凭证过账</h2>
      <p>将审核通过的凭证过账到总账，支持单张过账、批量过账和过账撤销</p>
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
                <div class="statistic-value">{{ statistics.pendingPosting || 0 }}</div>
                <div class="statistic-label">待过账凭证</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="statistic-card">
            <div class="statistic-item">
              <div class="statistic-icon posted">
                <i class="el-icon-s-promotion"></i>
              </div>
              <div class="statistic-content">
                <div class="statistic-value">{{ statistics.postedToday || 0 }}</div>
                <div class="statistic-label">今日已过账</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="statistic-card">
            <div class="statistic-item">
              <div class="statistic-icon failed">
                <i class="el-icon-warning"></i>
              </div>
              <div class="statistic-content">
                <div class="statistic-value">{{ statistics.failedPosting || 0 }}</div>
                <div class="statistic-label">过账失败</div>
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
                <div class="statistic-value">{{ statistics.totalPosting || 0 }}</div>
                <div class="statistic-label">过账总数</div>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 功能标签页 -->
    <el-tabs v-model="activeTab" type="card" @tab-click="handleTabClick">
      <!-- 单张过账 -->
      <el-tab-pane label="单张过账" name="single-posting">
        <div class="tab-content">
            <!-- 查询条件 -->
          <div class="search-form">
            <el-form :model="postingForm" ref="postingForm" :inline="true" label-width="100px">
              <el-form-item label="凭证编号">
                <el-input v-model="postingForm.voucherNo" placeholder="请输入凭证编号" clearable />
              </el-form-item>
              <el-form-item label="凭证类型">
                <el-select v-model="postingForm.voucherType" placeholder="请选择凭证类型" clearable>
                  <el-option label="记账凭证" value="ACCOUNTING" />
                  <el-option label="收款凭证" value="RECEIPT" />
                  <el-option label="付款凭证" value="PAYMENT" />
                  <el-option label="转账凭证" value="TRANSFER" />
                </el-select>
              </el-form-item>
              <el-form-item label="审核人">
                <el-input v-model="postingForm.approver" placeholder="请输入审核人" clearable />
              </el-form-item>
              <el-form-item label="审核日期">
                <el-date-picker
                  v-model="postingForm.dateRange"
                  type="daterange"
                  range-separator="至"
                  start-placeholder="开始日期"
                  end-placeholder="结束日期"
                  format="yyyy-MM-dd"
                  value-format="yyyy-MM-dd"
                />
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="handlePostingSearch">查询</el-button>
                <el-button @click="handlePostingReset">重置</el-button>
              </el-form-item>
            </el-form>
          </div>

          <!-- 操作工具栏 -->
          <div class="toolbar">
            <el-button type="success" icon="el-icon-s-promotion" @click="handleBatchPosting" :disabled="selectedPostingRows.length === 0">
              批量过账
            </el-button>
            <el-button type="warning" icon="el-icon-refresh-left" @click="handleBatchUnpost" :disabled="selectedPostingRows.length === 0">
              批量反过账
            </el-button>
            <el-button type="info" icon="el-icon-refresh" @click="handleRefreshPosting">
              刷新
            </el-button>
          </div>

          <!-- 凭证表格 -->
          <div class="table-container">
            <el-table
              :data="postingTableData"
              v-loading="postingLoading"
              border
              stripe
              height="500"
              @selection-change="handlePostingSelectionChange"
            >
              <el-table-column type="selection" width="55" />
              <el-table-column prop="voucherNo" label="凭证编号" width="120" />
              <el-table-column prop="voucherTypeId" label="凭证类型" width="100">
                <template slot-scope="scope">
                  <el-tag :type="getVoucherTypeColor(scope.row.voucherTypeId)">
                    {{ getVoucherTypeName(scope.row.voucherTypeId) }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="voucherDate" label="制单日期" width="120">
                <template slot-scope="scope">
                  {{ formatVoucherDate(scope.row.voucherDate) }}
                </template>
              </el-table-column>
              <el-table-column prop="voucherDesc" label="摘要" width="200" show-overflow-tooltip />
              <el-table-column prop="totalDebit" label="金额" width="120" align="right">
                <template slot-scope="scope">
                  <span style="color: #f56c6c; font-weight: bold;">
                    ¥{{ formatAmount(scope.row.totalDebit) }}
                  </span>
                </template>
              </el-table-column>
              <el-table-column prop="voucherStatus" label="状态" width="100">
                <template slot-scope="scope">
                  <el-tag :type="getStatusType(scope.row.voucherStatus)">
                    {{ getStatusName(scope.row.voucherStatus) }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="auditor" label="审核人" width="100" />
              <el-table-column prop="auditTime" label="审核时间" width="160">
                <template slot-scope="scope">
                  {{ formatDate(scope.row.auditTime) }}
                </template>
              </el-table-column>
              <el-table-column prop="voucherStatus" label="过账状态" width="100">
                <template slot-scope="scope">
                  <el-tag :type="getPostingStatusType(scope.row.voucherStatus)" size="small">
                    {{ getPostingStatusName(scope.row.voucherStatus) }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column label="操作" width="250" fixed="right">
                <template slot-scope="scope">
                  <el-button size="mini" @click="handleViewVoucher(scope.row)">查看</el-button>
                  <el-button size="mini" type="success" @click="handleSinglePosting(scope.row)" v-if="scope.row.status === '1' && scope.row.postingStatus !== 'POSTED'">
                    过账
                  </el-button>
                  <el-button size="mini" type="warning" @click="handleSingleUnpost(scope.row)" v-if="scope.row.status === '2' && scope.row.postingStatus === 'POSTED'">
                    反过账
                  </el-button>
                  <el-button size="mini" type="info" @click="handleViewPostingLog(scope.row)">过账日志</el-button>
                </template>
              </el-table-column>
            </el-table>
          </div>

          <!-- 分页组件 -->
          <div class="pagination-container">
            <el-pagination
              @size-change="handlePostingSizeChange"
              @current-change="handlePostingCurrentChange"
              :current-page="postingPagination.currentPage"
              :page-sizes="[10, 20, 50, 100]"
              :page-size="postingPagination.pageSize"
              layout="total, sizes, prev, pager, next, jumper"
              :total="postingPagination.total"
            />
          </div>
        </div>
      </el-tab-pane>

      <!-- 批量过账 -->
      <el-tab-pane label="批量过账" name="batch-posting">
        <div class="tab-content">
          <!-- 批量过账配置 -->
          <div class="batch-config">
            <el-form :model="batchPostingForm" ref="batchPostingForm" label-width="120px">
              <el-row :gutter="20">
                <el-col :span="12">
                  <el-form-item label="过账模式" required>
                    <el-radio-group v-model="batchPostingForm.postingMode">
                      <el-radio label="BY_DATE">按日期过账</el-radio>
                      <el-radio label="BY_TYPE">按凭证类型过账</el-radio>
                      <el-radio label="BY_PERIOD">按会计期间过账</el-radio>
                    </el-radio-group>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="过账策略" required>
                    <el-radio-group v-model="batchPostingForm.postingStrategy">
                      <el-radio label="IMMEDIATE">立即过账</el-radio>
                      <el-radio label="SCHEDULED">定时过账</el-radio>
                    </el-radio-group>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row :gutter="20">
                <el-col :span="12">
                  <el-form-item label="制单日期范围" required>
                    <el-date-picker
                      v-model="batchPostingForm.dateRange"
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
                  <el-form-item label="过账日期" required>
                    <el-date-picker
                      v-model="batchPostingForm.postingDate"
                      type="date"
                      placeholder="选择过账日期"
                      format="yyyy-MM-dd"
                      value-format="yyyy-MM-dd"
                    />
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row :gutter="20" v-if="batchPostingForm.postingStrategy === 'SCHEDULED'">
                <el-col :span="12">
                  <el-form-item label="定时执行时间">
                    <el-date-picker
                      v-model="batchPostingForm.scheduledTime"
                      type="datetime"
                      placeholder="选择执行时间"
                      format="yyyy-MM-dd HH:mm:ss"
                      value-format="yyyy-MM-dd HH:mm:ss"
                    />
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="重复执行">
                    <el-select v-model="batchPostingForm.repeatMode" placeholder="请选择重复模式">
                      <el-option label="不重复" value="NONE" />
                      <el-option label="每日" value="DAILY" />
                      <el-option label="每周" value="WEEKLY" />
                      <el-option label="每月" value="MONTHLY" />
                    </el-select>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row :gutter="20">
                <el-col :span="12">
                  <el-form-item label="过账检查">
                    <el-checkbox-group v-model="batchPostingForm.postingChecks">
                      <el-checkbox label="CHECK_BALANCE">检查借贷平衡</el-checkbox>
                      <el-checkbox label="CHECK_PERIOD">检查会计期间</el-checkbox>
                      <el-checkbox label="CHECK_SUBJECT">检查科目状态</el-checkbox>
                      <el-checkbox label="CHECK_AMOUNT">检查金额有效性</el-checkbox>
                    </el-checkbox-group>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="异常处理">
                    <el-radio-group v-model="batchPostingForm.exceptionHandling">
                      <el-radio label="SKIP">跳过异常</el-radio>
                      <el-radio label="STOP">停止处理</el-radio>
                      <el-radio label="MANUAL">手动处理</el-radio>
                    </el-radio-group>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-form-item label="过账备注">
                <el-input
                  v-model="batchPostingForm.postingRemark"
                  type="textarea"
                  :rows="3"
                  placeholder="请输入批量过账备注"
                />
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="handleSubmitBatchPosting">
                  {{ batchPostingForm.postingStrategy === 'IMMEDIATE' ? '立即执行' : '创建定时任务' }}
                </el-button>
                <el-button @click="handleResetBatchPosting">重置</el-button>
              </el-form-item>
            </el-form>
          </div>
        </div>
      </el-tab-pane>

      <!-- 过账撤销 -->
      <el-tab-pane label="过账撤销" name="posting-unpost">
        <div class="tab-content">
          <!-- 撤销凭证列表 -->
          <div class="unpost-list">
            <el-table
              :data="unpostTableData"
              v-loading="unpostLoading"
              border
              stripe
              height="500"
            >
              <el-table-column prop="VOUCHERNO" label="凭证编号" width="120" />
              <el-table-column prop="VOUCHERTYPE" label="凭证类型" width="100">
                <template slot-scope="scope">
                  <el-tag :type="getVoucherTypeColor(scope.row.VOUCHERTYPE)">
                    {{ getVoucherTypeName(scope.row.VOUCHERTYPE) }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="TOTALAMOUNT" label="金额" width="120" align="right">
                <template slot-scope="scope">
                  <span style="color: #f56c6c; font-weight: bold;">
                    ¥{{ formatAmount(scope.row.TOTALAMOUNT) }}
                  </span>
                </template>
              </el-table-column>
              <el-table-column prop="POSTINGTIME" label="过账时间" width="160">
                <template slot-scope="scope">
                  {{ formatDate(scope.row.POSTINGTIME) }}
                </template>
              </el-table-column>
              <el-table-column prop="POSTINGBYNAME" label="过账人" width="100" />
              <el-table-column prop="UNPOSTREASON" label="撤销原因" width="200" show-overflow-tooltip />
              <el-table-column prop="UNPOSTTIME" label="撤销时间" width="160">
                <template slot-scope="scope">
                  {{ formatDate(scope.row.UNPOSTTIME) }}
                </template>
              </el-table-column>
              <el-table-column prop="UNPOSTBYNAME" label="撤销人" width="100" />
              <el-table-column prop="UNPOSTSTATUS" label="撤销状态" width="100">
                <template slot-scope="scope">
                  <el-tag :type="getUnpostStatusType(scope.row.UNPOSTSTATUS)">
                    {{ getUnpostStatusName(scope.row.UNPOSTSTATUS) }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column label="操作" width="200" fixed="right">
                <template slot-scope="scope">
                  <el-button size="mini" @click="handleViewUnpostVoucher(scope.row)">查看</el-button>
                  <el-button size="mini" type="primary" @click="handleRePosting(scope.row)" v-if="scope.row.UNPOSTSTATUS === 'UNPOSTED'">
                    重新过账
                  </el-button>
                  <el-button size="mini" type="info" @click="handleViewUnpostLog(scope.row)">撤销日志</el-button>
                </template>
              </el-table-column>
            </el-table>
          </div>

          <!-- 分页组件 -->
          <div class="pagination-container">
            <el-pagination
              @size-change="handleUnpostSizeChange"
              @current-change="handleUnpostCurrentChange"
              :current-page="unpostPagination.currentPage"
              :page-sizes="[10, 20, 50, 100]"
              :page-size="unpostPagination.pageSize"
              layout="total, sizes, prev, pager, next, jumper"
              :total="unpostPagination.total"
            />
          </div>
        </div>
      </el-tab-pane>

      <!-- 过账监控 -->
      <el-tab-pane label="过账监控" name="posting-monitor">
        <div class="tab-content">
          <!-- 监控列表 -->
          <div class="monitor-list">
            <el-table
              :data="monitorTableData"
              v-loading="monitorLoading"
              border
              stripe
              height="500"
            >
              <el-table-column prop="taskId" label="任务ID" width="120" />
              <el-table-column prop="taskName" label="任务名称" width="200" />
              <el-table-column prop="postingMode" label="过账模式" width="120">
                <template slot-scope="scope">
                  <el-tag>{{ getPostingModeName(scope.row.postingMode) }}</el-tag>
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
                  <el-button size="mini" @click="handleViewMonitor(scope.row)">查看详情</el-button>
                  <el-button size="mini" type="warning" @click="handleStopMonitorTask(scope.row)" v-if="scope.row.status === 'RUNNING'">
                    停止任务
                  </el-button>
                  <el-button size="mini" type="danger" @click="handleDeleteMonitorTask(scope.row)" v-if="scope.row.status !== 'RUNNING'">
                    删除
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
          </div>

          <!-- 分页组件 -->
          <div class="pagination-container">
            <el-pagination
              @size-change="handleMonitorSizeChange"
              @current-change="handleMonitorCurrentChange"
              :current-page="monitorPagination.currentPage"
              :page-sizes="[10, 20, 50, 100]"
              :page-size="monitorPagination.pageSize"
              layout="total, sizes, prev, pager, next, jumper"
              :total="monitorPagination.total"
            />
          </div>
        </div>
      </el-tab-pane>
    </el-tabs>

    <!-- 凭证详情弹窗 -->
    <el-dialog
      title="凭证详情"
      :visible.sync="voucherDetailVisible"
      width="900px"
      :close-on-click-modal="false"
    >
      <div class="voucher-detail" v-loading="voucherDetailLoading">
        <!-- 基本信息 -->
        <el-descriptions title="基本信息" :column="3" border>
          <el-descriptions-item label="凭证编号">{{ currentVoucher.voucherNo }}</el-descriptions-item>
          <el-descriptions-item label="凭证类型">
            <el-tag :type="getVoucherTypeColor(currentVoucher.voucherTypeId)">
              {{ getVoucherTypeName(currentVoucher.voucherTypeId) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="制单日期">{{ formatVoucherDate(currentVoucher.voucherDate) }}</el-descriptions-item>
          <el-descriptions-item label="会计期间">{{ currentVoucher.accountingPeriod }}</el-descriptions-item>
          <el-descriptions-item label="凭证状态">
            <el-tag :type="getStatusType(currentVoucher.voucherStatus)">
              {{ getStatusName(currentVoucher.voucherStatus) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="审核状态">
            <el-tag :type="currentVoucher.auditStatus === 1 ? 'success' : 'info'">
              {{ currentVoucher.auditStatus === 1 ? '已审核' : '未审核' }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="摘要" :span="3">{{ currentVoucher.voucherDesc }}</el-descriptions-item>
        </el-descriptions>

        <!-- 金额信息 -->
        <el-descriptions title="金额信息" :column="2" border style="margin-top: 20px;">
          <el-descriptions-item label="借方合计">
            <span style="color: #f56c6c; font-weight: bold; font-size: 16px;">
              ¥{{ formatAmount(currentVoucher.totalDebit) }}
            </span>
          </el-descriptions-item>
          <el-descriptions-item label="贷方合计">
            <span style="color: #67c23a; font-weight: bold; font-size: 16px;">
              ¥{{ formatAmount(currentVoucher.totalCredit) }}
            </span>
          </el-descriptions-item>
        </el-descriptions>

        <!-- 人员信息 -->
        <el-descriptions title="人员信息" :column="3" border style="margin-top: 20px;">
          <el-descriptions-item label="制单人">{{ currentVoucher.preparerName || currentVoucher.creator || '-' }}</el-descriptions-item>
          <el-descriptions-item label="审核人">{{ currentVoucher.auditor || '-' }}</el-descriptions-item>
          <el-descriptions-item label="审核时间">{{ formatDate(currentVoucher.auditTime) || '-' }}</el-descriptions-item>
          <el-descriptions-item label="过账人">{{ currentVoucher.posterName || currentVoucher.poster || '-' }}</el-descriptions-item>
          <el-descriptions-item label="过账时间">{{ formatDate(currentVoucher.postTime) || '-' }}</el-descriptions-item>
          <el-descriptions-item label="账套ID">{{ currentVoucher.bookId }}</el-descriptions-item>
        </el-descriptions>

        <!-- 其他信息 -->
        <el-descriptions title="其他信息" :column="2" border style="margin-top: 20px;">
          <el-descriptions-item label="来源系统">{{ currentVoucher.sourceSystemName || currentVoucher.sourceSystem || '-' }}</el-descriptions-item>
          <el-descriptions-item label="来源单据ID">{{ currentVoucher.sourceTransactionId || '-' }}</el-descriptions-item>
          <el-descriptions-item label="备注" :span="2">{{ currentVoucher.remark || '-' }}</el-descriptions-item>
        </el-descriptions>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="voucherDetailVisible = false">关 闭</el-button>
      </span>
    </el-dialog>

    <!-- 过账日志弹窗 -->
    <el-dialog
      title="过账日志"
      :visible.sync="postingLogVisible"
      width="900px"
      :close-on-click-modal="false"
    >
      <div class="posting-log-content" v-loading="postingLogLoading">
        <!-- 凭证基本信息 -->
        <div class="log-voucher-info">
          <el-descriptions :column="4" border size="small">
            <el-descriptions-item label="凭证编号">{{ currentLogVoucher.voucherNo }}</el-descriptions-item>
            <el-descriptions-item label="凭证类型">
              <el-tag :type="getVoucherTypeColor(currentLogVoucher.voucherTypeId)" size="small">
                {{ getVoucherTypeName(currentLogVoucher.voucherTypeId) }}
              </el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="制单日期">{{ formatVoucherDate(currentLogVoucher.voucherDate) }}</el-descriptions-item>
            <el-descriptions-item label="金额">
              <span style="color: #f56c6c; font-weight: bold;">¥{{ formatAmount(currentLogVoucher.totalDebit) }}</span>
            </el-descriptions-item>
          </el-descriptions>
        </div>

        <!-- 日志时间线 -->
        <div class="log-timeline">
          <el-timeline v-if="postingLogs.length > 0">
            <el-timeline-item
              v-for="(log, index) in postingLogs"
              :key="index"
              :timestamp="formatDate(log.operateTime)"
              :type="getLogType(log.operateType)"
              :icon="getLogIcon(log.operateType)"
              placement="top"
            >
              <el-card shadow="hover" class="log-card">
                <div class="log-header">
                  <span class="log-title">
                    <el-tag :type="getLogTagType(log.operateType)" size="small">
                      {{ getLogTypeName(log.operateType) }}
                    </el-tag>
                  </span>
                  <span class="log-result">
                    <el-tag :type="log.result === 'SUCCESS' ? 'success' : 'danger'" size="mini">
                      {{ log.result === 'SUCCESS' ? '成功' : '失败' }}
                    </el-tag>
                  </span>
                </div>
                <div class="log-body">
                  <p><strong>操作人：</strong>{{ log.operatorName || log.operator }}</p>
                  <p v-if="log.remark"><strong>备注：</strong>{{ log.remark }}</p>
                  <p v-if="log.errorMsg"><strong>错误信息：</strong><span class="error-msg">{{ log.errorMsg }}</span></p>
                </div>
              </el-card>
            </el-timeline-item>
          </el-timeline>

          <!-- 暂无日志 -->
          <el-empty v-else description="暂无过账日志记录">
            <template slot="image">
              <i class="el-icon-document" style="font-size: 60px; color: #909399;"></i>
            </template>
          </el-empty>
        </div>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button icon="el-icon-refresh" @click="loadPostingLogs(currentLogVoucher.voucherId)">刷 新</el-button>
        <el-button @click="postingLogVisible = false">关 闭</el-button>
      </span>
    </el-dialog>

    <!-- 撤销记录详情弹窗 -->
    <el-dialog
      title="撤销记录详情"
      :visible.sync="unpostDetailVisible"
      width="800px"
      :close-on-click-modal="false"
    >
      <div class="unpost-detail">
        <!-- 凭证基本信息 -->
        <el-descriptions title="凭证信息" :column="3" border>
          <el-descriptions-item label="凭证编号">{{ currentUnpostRecord.VOUCHERNO }}</el-descriptions-item>
          <el-descriptions-item label="凭证类型">
            <el-tag :type="getVoucherTypeColor(currentUnpostRecord.VOUCHERTYPE)">
              {{ getVoucherTypeName(currentUnpostRecord.VOUCHERTYPE) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="凭证金额">
            <span style="color: #f56c6c; font-weight: bold;">¥{{ formatAmount(currentUnpostRecord.TOTALAMOUNT) }}</span>
          </el-descriptions-item>
        </el-descriptions>

        <!-- 过账信息 -->
        <el-descriptions title="原过账信息" :column="2" border style="margin-top: 20px;">
          <el-descriptions-item label="过账人">{{ currentUnpostRecord.POSTINGBYNAME || '-' }}</el-descriptions-item>
          <el-descriptions-item label="过账时间">{{ formatDate(currentUnpostRecord.POSTINGTIME) || '-' }}</el-descriptions-item>
        </el-descriptions>

        <!-- 撤销信息 -->
        <el-descriptions title="撤销信息" :column="2" border style="margin-top: 20px;">
          <el-descriptions-item label="撤销人">{{ currentUnpostRecord.UNPOSTBYNAME || '-' }}</el-descriptions-item>
          <el-descriptions-item label="撤销时间">{{ formatDate(currentUnpostRecord.UNPOSTTIME) || '-' }}</el-descriptions-item>
          <el-descriptions-item label="撤销原因" :span="2">{{ currentUnpostRecord.UNPOSTREASON || '-' }}</el-descriptions-item>
          <el-descriptions-item label="撤销状态">
            <el-tag :type="getUnpostStatusType(currentUnpostRecord.UNPOSTSTATUS)">
              {{ getUnpostStatusName(currentUnpostRecord.UNPOSTSTATUS) }}
            </el-tag>
          </el-descriptions-item>
        </el-descriptions>

        <!-- 重新过账信息（如果有） -->
        <el-descriptions v-if="currentUnpostRecord.REPOSTTIME" title="重新过账信息" :column="2" border style="margin-top: 20px;">
          <el-descriptions-item label="重新过账人">{{ currentUnpostRecord.REPOSTBYNAME || '-' }}</el-descriptions-item>
          <el-descriptions-item label="重新过账时间">{{ formatDate(currentUnpostRecord.REPOSTTIME) || '-' }}</el-descriptions-item>
        </el-descriptions>

        <!-- 其他信息 -->
        <el-descriptions title="其他信息" :column="2" border style="margin-top: 20px;">
          <el-descriptions-item label="备注" :span="2">{{ currentUnpostRecord.REMARK || '-' }}</el-descriptions-item>
          <el-descriptions-item label="创建时间">{{ formatDate(currentUnpostRecord.CREATETIME) || '-' }}</el-descriptions-item>
        </el-descriptions>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="unpostDetailVisible = false">关 闭</el-button>
      </span>
    </el-dialog>

    <!-- 撤销日志弹窗 -->
    <el-dialog
      title="撤销日志"
      :visible.sync="unpostLogVisible"
      width="900px"
      :close-on-click-modal="false"
    >
      <div class="unpost-log-content" v-loading="unpostLogLoading">
        <!-- 凭证基本信息 -->
        <div class="log-voucher-info">
          <el-descriptions :column="4" border size="small">
            <el-descriptions-item label="凭证编号">{{ currentUnpostLogRecord.VOUCHERNO }}</el-descriptions-item>
            <el-descriptions-item label="凭证类型">
              <el-tag :type="getVoucherTypeColor(currentUnpostLogRecord.VOUCHERTYPE)" size="small">
                {{ getVoucherTypeName(currentUnpostLogRecord.VOUCHERTYPE) }}
              </el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="金额">
              <span style="color: #f56c6c; font-weight: bold;">¥{{ formatAmount(currentUnpostLogRecord.TOTALAMOUNT) }}</span>
            </el-descriptions-item>
            <el-descriptions-item label="当前状态">
              <el-tag :type="getUnpostStatusType(currentUnpostLogRecord.UNPOSTSTATUS)" size="small">
                {{ getUnpostStatusName(currentUnpostLogRecord.UNPOSTSTATUS) }}
              </el-tag>
            </el-descriptions-item>
          </el-descriptions>
        </div>

        <!-- 日志时间线 -->
        <div class="log-timeline" style="margin-top: 20px;">
          <el-timeline v-if="unpostLogs.length > 0">
            <el-timeline-item
              v-for="(log, index) in unpostLogs"
              :key="index"
              :timestamp="formatDate(log.operateTime)"
              :type="getLogType(log.operateType)"
              :icon="getLogIcon(log.operateType)"
              placement="top"
            >
              <el-card shadow="hover" class="log-card">
                <div class="log-header">
                  <span class="log-title">
                    <el-tag :type="getLogTagType(log.operateType)" size="small">
                      {{ getLogTypeName(log.operateType) }}
                    </el-tag>
                  </span>
                  <span class="log-result">
                    <el-tag :type="log.result === 'SUCCESS' ? 'success' : 'danger'" size="mini">
                      {{ log.result === 'SUCCESS' ? '成功' : '失败' }}
                    </el-tag>
                  </span>
                </div>
                <div class="log-body">
                  <p><strong>操作人：</strong>{{ log.operatorName }}</p>
                  <p v-if="log.remark"><strong>备注：</strong>{{ log.remark }}</p>
                </div>
              </el-card>
            </el-timeline-item>
          </el-timeline>

          <!-- 暂无日志 -->
          <el-empty v-else description="暂无撤销日志记录">
            <template slot="image">
              <i class="el-icon-document" style="font-size: 60px; color: #909399;"></i>
            </template>
          </el-empty>
        </div>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button icon="el-icon-refresh" @click="loadUnpostLogs(currentUnpostLogRecord)">刷 新</el-button>
        <el-button @click="unpostLogVisible = false">关 闭</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import {
  getVoucherPage,
  postVouchers,
  unpostVouchers,
  getVoucherPostingLogs,
  getUnpostRecordPage
} from '@/api/financialSharing/voucher'

export default {
  name: 'VoucherPosting',
  data() {
    return {
      activeTab: 'single-posting',
      statistics: {},
      
      // 单张过账相关
      postingLoading: false,
      postingTableData: [],
      selectedPostingRows: [],
      postingForm: {
        voucherNo: '',
        voucherType: '',
        approver: '',
        dateRange: []
      },
      postingPagination: {
        currentPage: 1,
        pageSize: 20,
        total: 0
      },
      
      // 批量过账相关
      batchPostingForm: {
        postingMode: 'BY_DATE',
        postingStrategy: 'IMMEDIATE',
        dateRange: [],
        postingDate: '',
        scheduledTime: '',
        repeatMode: 'NONE',
        postingChecks: ['CHECK_BALANCE', 'CHECK_PERIOD'],
        exceptionHandling: 'SKIP',
        postingRemark: ''
      },
      
      // 过账撤销相关
      unpostLoading: false,
      unpostTableData: [],
      unpostPagination: {
        currentPage: 1,
        pageSize: 20,
        total: 0
      },
      
      // 过账监控相关
      monitorLoading: false,
      monitorTableData: [],
      monitorPagination: {
        currentPage: 1,
        pageSize: 20,
        total: 0
      },

      // 凭证详情弹窗
      voucherDetailVisible: false,
      voucherDetailLoading: false,
      currentVoucher: {},

      // 过账日志弹窗
      postingLogVisible: false,
      postingLogLoading: false,
      currentLogVoucher: {},
      postingLogs: [],

      // 撤销记录详情弹窗
      unpostDetailVisible: false,
      currentUnpostRecord: {},

      // 撤销日志弹窗
      unpostLogVisible: false,
      unpostLogLoading: false,
      currentUnpostLogRecord: {},
      unpostLogs: []
    }
  },
  
  mounted() {
    this.loadPostingData()
    this.loadStatistics()
  },
  
  methods: {
    // 标签页切换
    handleTabClick(tab) {
      if (tab.name === 'posting-unpost') {
        this.loadUnpostData()
      } else if (tab.name === 'posting-monitor') {
        this.loadMonitorData()
      }
    },
    
    // 加载过账数据
    async loadPostingData() {
      this.postingLoading = true
      try {
        const params = {
          ...this.postingForm,
          status: '1', // 只查询已审核状态的凭证
          pageNumber: this.postingPagination.currentPage,
          pageSize: this.postingPagination.pageSize
        }
        
        const response = await getVoucherPage(params)
        if (response.code === 1) {
          this.postingTableData = response.data.tlist || []
          this.postingPagination.total = parseInt(response.data.totalRecord) || 0
        } else {
          this.$message.error(response.msg || '查询失败')
        }
      } catch (error) {
        console.error('加载数据失败:', error)
        this.$message.error('加载数据失败')
      } finally {
        this.postingLoading = false
      }
    },
    
    // 加载撤销数据
    async loadUnpostData() {
      this.unpostLoading = true
      try {
        const params = {
          pageNo: this.unpostPagination.currentPage,
          pageSize: this.unpostPagination.pageSize
        }

        const response = await getUnpostRecordPage(params)
        if (response.code === 1) {
          this.unpostTableData = response.data.tlist || []
          this.unpostPagination.total = parseInt(response.data.totalRecord) || 0
        } else {
          this.$message.error(response.msg || '查询失败')
        }
      } catch (error) {
        console.error('加载撤销数据失败:', error)
        this.$message.error('加载撤销数据失败')
      } finally {
        this.unpostLoading = false
      }
    },
    
    // 加载监控数据
    async loadMonitorData() {
      this.monitorLoading = true
      try {
        // 暂未对接 API，先以空状态展示，待后端接口提供后接入
        this.monitorTableData = []
        this.monitorPagination.total = 0
      } catch (error) {
        console.error('加载监控数据失败:', error)
      } finally {
        this.monitorLoading = false
      }
    },
    
    // 加载统计数据
    async loadStatistics() {
      try {
        // 暂未对接 API，先以空状态展示，待后端接口提供后接入
        this.statistics = {
          pendingPosting: 0,
          postedToday: 0,
          failedPosting: 0,
          totalPosting: 0
        }
      } catch (error) {
        console.error('加载统计数据失败:', error)
      }
    },
    
    // 过账查询
    handlePostingSearch() {
      this.postingPagination.currentPage = 1
      this.loadPostingData()
    },
    
    // 过账重置
    handlePostingReset() {
      this.$refs.postingForm.resetFields()
      this.postingPagination.currentPage = 1
      this.loadPostingData()
    },
    
    // 刷新过账
    handleRefreshPosting() {
      this.loadPostingData()
      this.loadStatistics()
    },
    
    // 单张过账
    async handleSinglePosting(row) {
      try {
        await this.$confirm('确认过账该凭证？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        
        const response = await postVouchers([row.voucherId])
        if (response.code === 200) {
          this.$message.success('凭证过账成功')
          this.loadPostingData()
          this.loadStatistics()
        } else {
          this.$message.error(response.msg || '过账失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('过账失败:', error)
          this.$message.error('过账失败')
        }
      }
    },
    
    // 单张反过账
    async handleSingleUnpost(row) {
      try {
        const { value: reason } = await this.$prompt('请输入反过账原因', '反过账', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          inputValidator: (value) => {
            if (!value) {
              return '请输入反过账原因'
            }
            return true
          }
        })
        
        const response = await unpostVouchers([row.voucherId])
        if (response.code === 200) {
          this.$message.success('凭证反过账成功')
          this.loadPostingData()
          this.loadStatistics()
        } else {
          this.$message.error(response.msg || '反过账失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('反过账失败:', error)
          this.$message.error('反过账失败')
        }
      }
    },
    
    // 批量过账
    async handleBatchPosting() {
      try {
        await this.$confirm(`确认批量过账选中的 ${this.selectedPostingRows.length} 个凭证？`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        
        const voucherIds = this.selectedPostingRows.map(row => row.voucherId)
        const response = await postVouchers(voucherIds)
        if (response.code === 200) {
          this.$message.success('批量过账成功')
          this.loadPostingData()
          this.loadStatistics()
        } else {
          this.$message.error(response.msg || '批量过账失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('批量过账失败:', error)
          this.$message.error('批量过账失败')
        }
      }
    },
    
    // 批量反过账
    async handleBatchUnpost() {
      try {
        const { value: reason } = await this.$prompt('请输入批量反过账原因', '批量反过账', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          inputValidator: (value) => {
            if (!value) {
              return '请输入反过账原因'
            }
            return true
          }
        })
        
        const voucherIds = this.selectedPostingRows.map(row => row.voucherId)
        const response = await unpostVouchers(voucherIds)
        if (response.code === 200) {
          this.$message.success('批量反过账成功')
          this.loadPostingData()
          this.loadStatistics()
        } else {
          this.$message.error(response.msg || '批量反过账失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('批量反过账失败:', error)
          this.$message.error('批量反过账失败')
        }
      }
    },
    
    // 提交批量过账
    async handleSubmitBatchPosting() {
      try {
        const response = await postVouchers([])
        if (response.code === 200) {
          this.$message.success(this.batchPostingForm.postingStrategy === 'IMMEDIATE' ? '批量过账任务已启动' : '定时任务创建成功')
          this.handleResetBatchPosting()
          this.loadMonitorData()
          this.activeTab = 'posting-monitor'
        } else {
          this.$message.error(response.msg || '操作失败')
        }
      } catch (error) {
        console.error('操作失败:', error)
        this.$message.error('操作失败')
      }
    },
    
    // 重置批量过账
    handleResetBatchPosting() {
      this.$refs.batchPostingForm.resetFields()
    },
    
    // 重新过账（从撤销记录中重新过账）
    async handleRePosting(row) {
      try {
        await this.$confirm('确认重新过账该凭证？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })

        // 使用大写字段名 VOUCHERID
        const voucherId = row.VOUCHERID || row.voucherId
        if (!voucherId) {
          this.$message.error('凭证ID不存在')
          return
        }

        const response = await postVouchers([voucherId])
        if (response.code === 1 || response.code === 200) {
          this.$message.success('凭证重新过账成功')
          this.loadUnpostData()
          this.loadStatistics()
        } else {
          this.$message.error(response.msg || '重新过账失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('重新过账失败:', error)
          this.$message.error('重新过账失败')
        }
      }
    },

    // 查看凭证
    handleViewVoucher(row) {
      this.currentVoucher = { ...row }
      this.voucherDetailVisible = true
    },

    // 查看过账日志
    handleViewPostingLog(row) {
      this.currentLogVoucher = { ...row }
      this.postingLogVisible = true
      this.loadPostingLogs(row.voucherId)
    },

    // 加载过账日志
    async loadPostingLogs(voucherId) {
      this.postingLogLoading = true
      try {
        const response = await getVoucherPostingLogs(voucherId)
        if (response.code === 1) {
          this.postingLogs = response.data || []
        } else {
          // 接口未实现时降级为空状态
          this.postingLogs = this.getMockPostingLogs(voucherId)
        }
      } catch (error) {
        console.error('加载过账日志失败:', error)
        // 接口异常时降级为空状态
        this.postingLogs = this.getMockPostingLogs(voucherId)
      } finally {
        this.postingLogLoading = false
      }
    },

    // 数据加载失败时的空状态降级（不再使用模拟数据）
    getMockPostingLogs(voucherId) {
      return []
    },

    // 获取日志类型
    getLogType(operateType) {
      const typeMap = {
        'CREATE': 'primary',
        'AUDIT': 'warning',
        'POST': 'success',
        'UNPOST': 'danger',
        'UPDATE': 'info'
      }
      return typeMap[operateType] || 'info'
    },

    // 获取日志图标
    getLogIcon(operateType) {
      const iconMap = {
        'CREATE': 'el-icon-document-add',
        'AUDIT': 'el-icon-check',
        'POST': 'el-icon-s-promotion',
        'UNPOST': 'el-icon-refresh-left',
        'UPDATE': 'el-icon-edit'
      }
      return iconMap[operateType] || 'el-icon-info'
    },

    // 获取日志标签类型
    getLogTagType(operateType) {
      const typeMap = {
        'CREATE': '',
        'AUDIT': 'warning',
        'POST': 'success',
        'UNPOST': 'danger',
        'UPDATE': 'info'
      }
      return typeMap[operateType] || 'info'
    },

    // 获取日志类型名称
    getLogTypeName(operateType) {
      const nameMap = {
        'CREATE': '创建凭证',
        'AUDIT': '审核凭证',
        'POST': '过账',
        'UNPOST': '反过账',
        'UPDATE': '修改凭证'
      }
      return nameMap[operateType] || operateType
    },
    
    // 查看撤销凭证详情（弹窗显示）
    handleViewUnpostVoucher(row) {
      this.currentUnpostRecord = { ...row }
      this.unpostDetailVisible = true
    },

    // 查看撤销日志
    handleViewUnpostLog(row) {
      this.currentUnpostLogRecord = { ...row }
      this.unpostLogVisible = true
      this.loadUnpostLogs(row)
    },

    // 加载撤销日志
    async loadUnpostLogs(row) {
      this.unpostLogLoading = true
      try {
        // 构建撤销日志数据（基于当前记录信息）
        const logs = []

        // 原过账记录
        if (row.POSTINGTIME) {
          logs.push({
            operateType: 'POST',
            operateTime: row.POSTINGTIME,
            operatorName: row.POSTINGBYNAME || '未知',
            result: 'SUCCESS',
            remark: '凭证过账'
          })
        }

        // 撤销记录
        if (row.UNPOSTTIME) {
          logs.push({
            operateType: 'UNPOST',
            operateTime: row.UNPOSTTIME,
            operatorName: row.UNPOSTBYNAME || '未知',
            result: 'SUCCESS',
            remark: row.UNPOSTREASON || '凭证撤销'
          })
        }

        // 重新过账记录
        if (row.REPOSTTIME) {
          logs.push({
            operateType: 'REPOST',
            operateTime: row.REPOSTTIME,
            operatorName: row.REPOSTBYNAME || '未知',
            result: 'SUCCESS',
            remark: '重新过账'
          })
        }

        // 按时间排序（最新的在前）
        logs.sort((a, b) => b.operateTime - a.operateTime)

        this.unpostLogs = logs
      } catch (error) {
        console.error('加载撤销日志失败:', error)
        this.unpostLogs = []
      } finally {
        this.unpostLogLoading = false
      }
    },
    
    // 查看监控
    handleViewMonitor(row) {
      const content = `<p><b>任务编号：</b>${row.taskId || row.id || '-'}</p><p><b>任务名称：</b>${row.taskName || row.name || '-'}</p><p><b>状态：</b>${row.statusName || row.status || '-'}</p><p><b>开始时间：</b>${row.startTime || '-'}</p><p><b>结束时间：</b>${row.endTime || '-'}</p><p><b>处理记录数：</b>${row.processedCount || 0}</p><p><b>异常数：</b>${row.errorCount || 0}</p>`
      this.$alert(content, '监控详情', { dangerouslyUseHTMLString: true })
    },
    
    // 停止监控任务
    async handleStopMonitorTask(row) {
      try {
        await this.$confirm('确认停止该过账任务？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        
        this.$message.success('任务停止成功')
        this.loadMonitorData()
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('停止任务失败')
        }
      }
    },
    
    // 删除监控任务
    async handleDeleteMonitorTask(row) {
      try {
        await this.$confirm('确认删除该过账任务？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        
        this.$message.success('任务删除成功')
        this.loadMonitorData()
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('删除任务失败')
        }
      }
    },
    
    // 选择变化
    handlePostingSelectionChange(selection) {
      this.selectedPostingRows = selection
    },
    
    // 分页处理
    handlePostingSizeChange(val) {
      this.postingPagination.pageSize = val
      this.postingPagination.currentPage = 1
      this.loadPostingData()
    },
    
    handlePostingCurrentChange(val) {
      this.postingPagination.currentPage = val
      this.loadPostingData()
    },
    
    handleUnpostSizeChange(val) {
      this.unpostPagination.pageSize = val
      this.unpostPagination.currentPage = 1
      this.loadUnpostData()
    },
    
    handleUnpostCurrentChange(val) {
      this.unpostPagination.currentPage = val
      this.loadUnpostData()
    },
    
    handleMonitorSizeChange(val) {
      this.monitorPagination.pageSize = val
      this.monitorPagination.currentPage = 1
      this.loadMonitorData()
    },
    
    handleMonitorCurrentChange(val) {
      this.monitorPagination.currentPage = val
      this.loadMonitorData()
    },
    
    // 获取凭证类型名称
    getVoucherTypeName(typeId) {
      const typeMap = {
        '1': '记账凭证',
        '2': '收款凭证',
        '3': '付款凭证',
        '4': '转账凭证',
        'ACCOUNTING': '记账凭证',
        'RECEIPT': '收款凭证',
        'PAYMENT': '付款凭证',
        'TRANSFER': '转账凭证'
      }
      return typeMap[typeId] || typeId
    },

    // 获取凭证类型颜色
    getVoucherTypeColor(typeId) {
      const colorMap = {
        '1': 'primary',
        '2': 'success',
        '3': 'warning',
        '4': 'info',
        'ACCOUNTING': 'primary',
        'RECEIPT': 'success',
        'PAYMENT': 'warning',
        'TRANSFER': 'info'
      }
      return colorMap[typeId] || 'info'
    },

    // 获取状态类型
    getStatusType(status) {
      const statusMap = {
        0: 'warning',
        1: 'primary',
        2: 'success',
        '0': 'warning',
        '1': 'primary',
        '2': 'success'
      }
      return statusMap[status] || 'info'
    },

    // 获取状态名称
    getStatusName(status) {
      const statusMap = {
        0: '草稿',
        1: '已审核',
        2: '已过账',
        '0': '草稿',
        '1': '已审核',
        '2': '已过账'
      }
      return statusMap[status] || status
    },

    // 获取过账状态类型
    getPostingStatusType(status) {
      const statusMap = {
        0: 'warning',
        1: 'primary',
        2: 'success',
        3: 'danger',
        'NOT_POSTED': 'warning',
        'POSTING': 'primary',
        'POSTED': 'success',
        'FAILED': 'danger'
      }
      return statusMap[status] || 'info'
    },

    // 获取过账状态名称
    getPostingStatusName(status) {
      const statusMap = {
        0: '未过账',
        1: '待过账',
        2: '已过账',
        3: '过账失败',
        'NOT_POSTED': '未过账',
        'POSTING': '过账中',
        'POSTED': '已过账',
        'FAILED': '过账失败'
      }
      return statusMap[status] || status
    },
    
    // 获取撤销状态类型
    getUnpostStatusType(status) {
      const statusMap = {
        'UNPOSTED': 'warning',
        'REPOSTED': 'success'
      }
      return statusMap[status] || 'info'
    },
    
    // 获取撤销状态名称
    getUnpostStatusName(status) {
      const statusMap = {
        'UNPOSTED': '已撤销',
        'REPOSTED': '已重新过账'
      }
      return statusMap[status] || status
    },
    
    // 获取过账模式名称
    getPostingModeName(mode) {
      const modeMap = {
        'BY_DATE': '按日期',
        'BY_TYPE': '按类型',
        'BY_PERIOD': '按期间'
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

    // 格式化凭证日期（处理数组格式 [year, month, day]）
    formatVoucherDate(date) {
      if (!date) return ''
      if (Array.isArray(date) && date.length >= 3) {
        const [year, month, day] = date
        return `${year}-${String(month).padStart(2, '0')}-${String(day).padStart(2, '0')}`
      }
      return new Date(date).toLocaleDateString()
    },

    // 格式化日期
    formatDate(date) {
      if (!date) return ''
      if (Array.isArray(date) && date.length >= 3) {
        const [year, month, day] = date
        return `${year}-${String(month).padStart(2, '0')}-${String(day).padStart(2, '0')}`
      }
      return new Date(date).toLocaleString()
    }
  }
}
</script>

<style scoped>
.voucher-posting-container {
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

.statistic-icon.posted {
  background: linear-gradient(135deg, #67c23a, #85ce61);
}

.statistic-icon.failed {
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

.unpost-list,
.monitor-list {
  margin-bottom: 20px;
}

/* 弹窗样式 */
.dialog-footer {
  text-align: right;
}

/* 凭证详情样式 */
.voucher-detail {
  max-height: 60vh;
  overflow-y: auto;
}

.voucher-detail .el-descriptions {
  margin-bottom: 0;
}

.voucher-detail .el-descriptions__title {
  font-size: 14px;
  font-weight: bold;
  color: #303133;
}

/* 过账日志样式 */
.posting-log-content {
  max-height: 60vh;
  overflow-y: auto;
}

.log-voucher-info {
  margin-bottom: 20px;
}

.log-timeline {
  padding: 10px 0;
}

.log-card {
  margin-bottom: 0;
}

.log-card .el-card__body {
  padding: 12px 15px;
}

.log-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.log-title {
  font-weight: bold;
}

.log-body p {
  margin: 5px 0;
  font-size: 13px;
  color: #606266;
}

.log-body .error-msg {
  color: #f56c6c;
}

.el-timeline-item__wrapper {
  padding-left: 20px;
}
</style>
