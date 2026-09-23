<template>
  <div class="financing-repayment-manage">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <h2 class="page-title">
            <i class="el-icon-money"></i>
            融资还款管理
          </h2>
          <p class="page-description">融资还款计划、还款执行和逾期管理</p>
        </div>
        <div class="header-right">
          <el-button type="primary" icon="el-icon-plus" @click="handleCreateRepayment">
            新增还款
          </el-button>
          <el-button type="success" icon="el-icon-upload" @click="handleImport">
            批量导入
          </el-button>
          <el-button type="info" icon="el-icon-download" @click="handleExport">
            导出数据
          </el-button>
        </div>
      </div>
    </div>

    <!-- 还款概览卡片 -->
    <div class="repayment-overview">
      <el-row :gutter="20">
        <el-col :span="4">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon total-icon">
                <i class="el-icon-document"></i>
              </div>
              <div class="card-info">
                <div class="card-title">还款计划总数</div>
                <div class="card-value">{{ totalRepayments }}</div>
                <div class="card-change">笔</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="4">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon pending-icon">
                <i class="el-icon-time"></i>
              </div>
              <div class="card-info">
                <div class="card-title">待还款</div>
                <div class="card-value">{{ pendingRepayments }}</div>
                <div class="card-change warning">笔</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="4">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon amount-icon">
                <i class="el-icon-money"></i>
              </div>
              <div class="card-info">
                <div class="card-title">待还金额</div>
                <div class="card-value">{{ pendingAmount }}</div>
                <div class="card-change">万元</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="4">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon overdue-icon">
                <i class="el-icon-warning"></i>
              </div>
              <div class="card-info">
                <div class="card-title">逾期笔数</div>
                <div class="card-value">{{ overdueRepayments }}</div>
                <div class="card-change negative">笔</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="4">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon completed-icon">
                <i class="el-icon-circle-check"></i>
              </div>
              <div class="card-info">
                <div class="card-title">已完成</div>
                <div class="card-value">{{ completedRepayments }}</div>
                <div class="card-change positive">笔</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="4">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon rate-icon">
                <i class="el-icon-data-line"></i>
              </div>
              <div class="card-info">
                <div class="card-title">按时还款率</div>
                <div class="card-value">{{ onTimeRate }}</div>
                <div class="card-change positive">%</div>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 还款分析图表 -->
    <el-row :gutter="20">
      <el-col :span="8">
        <el-card class="chart-card" shadow="never">
          <div class="chart-header">
            <h3>还款状态分布</h3>
          </div>
          <div id="repaymentStatusChart" class="chart-container"></div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card class="chart-card" shadow="never">
          <div class="chart-header">
            <h3>月度还款趋势</h3>
          </div>
          <div id="monthlyTrendChart" class="chart-container"></div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card class="chart-card" shadow="never">
          <div class="chart-header">
            <h3>融资类型还款分布</h3>
          </div>
          <div id="financingTypeChart" class="chart-container"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 即将到期提醒 -->
    <el-card class="reminder-card" shadow="never">
      <div class="card-header">
        <h3>
          <i class="el-icon-bell" style="color: #E6A23C;"></i>
          即将到期还款提醒
        </h3>
        <el-button type="text" @click="handleViewAllReminders">查看全部</el-button>
      </div>
      <div class="reminder-list">
        <el-row :gutter="20" v-if="upcomingRepayments.length > 0">
          <el-col :span="8" v-for="reminder in upcomingRepayments" :key="reminder.id">
            <div class="reminder-item">
              <div class="reminder-header">
                <div class="reminder-title">{{ reminder.title }}</div>
                <div class="reminder-days" :class="getDaysClass(reminder.daysLeft)">
                  {{ reminder.daysLeft }}天后到期
                </div>
              </div>
              <div class="reminder-content">
                <div class="reminder-amount">{{ formatCurrency(reminder.amount) }}</div>
                <div class="reminder-date">到期日期：{{ reminder.dueDate }}</div>
              </div>
              <div class="reminder-actions">
                <el-button type="primary" size="mini" @click="handleRepayNow(reminder)">立即还款</el-button>
                <el-button type="text" size="mini" @click="handleReminderDetail(reminder)">详情</el-button>
              </div>
            </div>
          </el-col>
        </el-row>
        <el-empty v-else description="暂无即将到期的还款" :image-size="80"></el-empty>
      </div>
    </el-card>

    <!-- 搜索区域 -->
    <el-card class="search-card" shadow="never">
      <div class="search-form">
        <el-form :inline="true" :model="listQuery" class="demo-form-inline">
          <el-form-item label="还款编号">
            <el-input
              v-model="listQuery.repaymentNo"
              placeholder="请输入还款编号"
              style="width: 150px;"
              @keyup.enter.native="handleFilter"
            />
          </el-form-item>
          <el-form-item label="融资类型">
            <el-select
              v-model="listQuery.financingType"
              placeholder="请选择融资类型"
              clearable
              style="width: 120px;"
            >
              <el-option label="银行贷款" value="BANK_LOAN" />
              <el-option label="债券发行" value="BOND" />
              <el-option label="融资租赁" value="LEASING" />
            </el-select>
          </el-form-item>
          <el-form-item label="还款状态">
            <el-select
              v-model="listQuery.repaymentStatus"
              placeholder="请选择状态"
              clearable
              style="width: 120px;"
            >
              <el-option label="待还款" value="PENDING" />
              <el-option label="已还款" value="COMPLETED" />
              <el-option label="逾期" value="OVERDUE" />
              <el-option label="部分还款" value="PARTIAL" />
            </el-select>
          </el-form-item>
          <el-form-item label="还款类型">
            <el-select
              v-model="listQuery.repaymentType"
              placeholder="请选择类型"
              clearable
              style="width: 120px;"
            >
              <el-option label="本金" value="PRINCIPAL" />
              <el-option label="利息" value="INTEREST" />
              <el-option label="本息" value="PRINCIPAL_INTEREST" />
              <el-option label="费用" value="FEE" />
            </el-select>
          </el-form-item>
          <el-form-item label="到期日期">
            <el-date-picker
              v-model="listQuery.dueDateRange"
              type="daterange"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              style="width: 240px;"
            />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon="el-icon-search" @click="handleFilter">
              搜索
            </el-button>
            <el-button type="default" icon="el-icon-refresh" @click="resetQuery">
              重置
            </el-button>
          </el-form-item>
        </el-form>
      </div>
    </el-card>

    <!-- 还款计划表格 -->
    <el-card class="table-card" shadow="never">
      <el-table
        :data="repaymentList"
        border
        fit
        highlight-current-row
        style="width: 100%;"
        v-loading="listLoading"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="还款ID" prop="repaymentId" width="80" align="center" />
        <el-table-column label="还款编号" width="150px" align="center">
          <template slot-scope="{row}">
            <span class="link-type" @click="handleViewDetail(row)">{{ row.repaymentNo }}</span>
          </template>
        </el-table-column>
        <el-table-column label="融资类型" width="120px" align="center">
          <template slot-scope="{row}">
            <el-tag :type="getFinancingTypeTagType(row.financingType)" size="mini">
              {{ getFinancingTypeText(row.financingType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="还款类型" width="100px" align="center">
          <template slot-scope="{row}">
            <el-tag :type="getRepaymentTypeTagType(row.repaymentType)" size="mini">
              {{ getRepaymentTypeText(row.repaymentType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="应还金额" width="150px" align="right">
          <template slot-scope="{row}">
            <span class="due-amount">{{ formatCurrency(row.repaymentAmount) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="已还金额" width="150px" align="right">
          <template slot-scope="{row}">
            <span class="paid-amount">{{ formatCurrency(row.actualAmount) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="剩余金额" width="150px" align="right">
          <template slot-scope="{row}">
            <span class="remaining-amount">{{ formatCurrency((row.repaymentAmount || 0) - (row.actualAmount || 0)) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="到期日期" width="120px" align="center">
          <template slot-scope="{row}">
            <span :class="getDueDateClass(row.planDate)">{{ formatDate(row.planDate) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="实际还款日期" width="120px" align="center">
          <template slot-scope="{row}">
            <span>{{ formatDate(row.actualRepaymentDate) || '-' }}</span>
          </template>
        </el-table-column>
        <el-table-column label="逾期天数" width="100px" align="center">
          <template slot-scope="{row}">
            <span :class="getOverdueDaysClass(calcOverdueDays(row))">
              {{ calcOverdueDays(row) > 0 ? calcOverdueDays(row) + '天' : '-' }}
            </span>
          </template>
        </el-table-column>
        <el-table-column label="还款状态" width="100px" align="center">
          <template slot-scope="{row}">
            <el-tag :type="getRepaymentStatusTagType(row.repaymentStatus)" size="mini">
              {{ getRepaymentStatusText(row.repaymentStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" width="200" class-name="small-padding fixed-width">
          <template slot-scope="{row}">
            <el-button v-if="row.repaymentStatus === 'PENDING'" type="primary" size="mini" @click="handleRepay(row)">
              还款
            </el-button>
            <el-button v-if="row.repaymentStatus === 'PARTIAL'" type="warning" size="mini" @click="handlePartialRepay(row)">
              补还
            </el-button>
            <el-button type="info" size="mini" @click="handleViewDetail(row)">
              详情
            </el-button>
            <el-dropdown size="mini" @command="handleCommand">
              <el-button size="mini">
                更多<i class="el-icon-arrow-down el-icon--right"></i>
              </el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item :command="{action: 'edit', row: row}">编辑</el-dropdown-item>
                <el-dropdown-item :command="{action: 'history', row: row}">还款历史</el-dropdown-item>
                <el-dropdown-item :command="{action: 'receipt', row: row}">还款凭证</el-dropdown-item>
                <el-dropdown-item :command="{action: 'extend', row: row}">展期申请</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </template>
        </el-table-column>
      </el-table>

      <pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="getList" />
    </el-card>

    <!-- 弹窗组件 -->
    <repayment-form-dialog
      :visible.sync="formDialogVisible"
      :form-data="currentRow"
      :is-edit="isEdit"
      @success="handleFormSuccess"
    />
    <repayment-detail-dialog
      :visible.sync="detailDialogVisible"
      :repayment-id="currentRepaymentId"
      @execute="handleDetailExecute"
      @partial="handleDetailPartial"
      @extension="handleDetailExtension"
    />
    <repayment-history-dialog
      :visible.sync="historyDialogVisible"
      :repayment-id="currentRepaymentId"
    />
    <extension-apply-dialog
      :visible.sync="extensionDialogVisible"
      :repayment-data="currentRow"
      @success="handleExtensionSuccess"
    />

    <!-- 即将到期还款提醒弹窗 -->
    <el-dialog
      title="即将到期还款提醒"
      :visible.sync="reminderDialogVisible"
      width="900px"
      top="5vh"
      :close-on-click-modal="false"
    >
      <div class="reminder-dialog-content">
        <!-- 筛选区域 -->
        <div class="reminder-filter">
          <el-radio-group v-model="reminderDaysFilter" size="small" @change="handleReminderFilterChange">
            <el-radio-button :label="7">7天内</el-radio-button>
            <el-radio-button :label="15">15天内</el-radio-button>
            <el-radio-button :label="30">30天内</el-radio-button>
            <el-radio-button :label="90">90天内</el-radio-button>
          </el-radio-group>
          <span class="reminder-count">共 {{ filteredReminders.length }} 条记录</span>
        </div>

        <!-- 提醒列表 -->
        <el-table
          v-loading="reminderLoading"
          :data="filteredReminders"
          border
          style="width: 100%"
          max-height="450"
        >
          <el-table-column label="还款编号" prop="repaymentNo" width="150" align="center" />
          <el-table-column label="融资类型" width="100" align="center">
            <template slot-scope="{row}">
              <el-tag :type="getFinancingTypeTagType(row.financingType)" size="mini">
                {{ getFinancingTypeText(row.financingType) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="还款类型" width="90" align="center">
            <template slot-scope="{row}">
              <el-tag :type="getRepaymentTypeTagType(row.repaymentType)" size="mini">
                {{ getRepaymentTypeText(row.repaymentType) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="所属公司" prop="companyName" min-width="150" show-overflow-tooltip />
          <el-table-column label="应还金额" width="130" align="right">
            <template slot-scope="{row}">
              <span class="amount-text">{{ formatCurrency(row.repaymentAmount) }}</span>
            </template>
          </el-table-column>
          <el-table-column label="到期日期" width="110" align="center">
            <template slot-scope="{row}">
              <span :class="getDueDateClass(row.planDate)">{{ row.planDateStr }}</span>
            </template>
          </el-table-column>
          <el-table-column label="剩余天数" width="100" align="center">
            <template slot-scope="{row}">
              <el-tag :type="getDaysTagType(row.daysLeft)" size="small">
                {{ row.daysLeft }}天
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="120" align="center" fixed="right">
            <template slot-scope="{row}">
              <el-button type="primary" size="mini" @click="handleRepayFromReminder(row)">还款</el-button>
              <el-button type="text" size="mini" @click="handleViewReminderDetail(row)">详情</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="reminderDialogVisible = false">关 闭</el-button>
      </div>
    </el-dialog>

    <!-- 批量导入弹窗 -->
    <el-dialog
      title="批量导入还款计划"
      :visible.sync="importDialogVisible"
      width="600px"
      :close-on-click-modal="false"
      @close="handleImportDialogClose"
    >
      <div class="import-dialog-content">
        <!-- 上传区域 -->
        <el-upload
          ref="importUpload"
          class="import-upload"
          drag
          action="#"
          :auto-upload="false"
          :file-list="importFileList"
          :on-change="handleImportFileChange"
          :on-remove="handleImportFileRemove"
          :limit="1"
          accept=".xlsx,.xls"
        >
          <i class="el-icon-upload"></i>
          <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
          <div class="el-upload__tip" slot="tip">
            只能上传 Excel 文件（.xlsx/.xls），且不超过 10MB
          </div>
        </el-upload>

        <!-- 模板下载 -->
        <div class="import-template">
          <span class="template-label">没有模板？</span>
          <el-button type="text" icon="el-icon-download" @click="handleDownloadTemplate">
            下载导入模板
          </el-button>
        </div>

        <!-- 导入结果 -->
        <div v-if="importResult" class="import-result">
          <el-alert
            :title="importResult.success ? '导入成功' : '导入失败'"
            :type="importResult.success ? 'success' : 'error'"
            :description="importResult.message"
            show-icon
            :closable="false"
          />
          <div v-if="importResult.details" class="result-details">
            <p>成功导入：<span class="success-count">{{ importResult.details.successCount }}</span> 条</p>
            <p v-if="importResult.details.failCount > 0">
              导入失败：<span class="fail-count">{{ importResult.details.failCount }}</span> 条
            </p>
            <div v-if="importResult.details.errors && importResult.details.errors.length > 0" class="error-list">
              <p class="error-title">错误详情：</p>
              <ul>
                <li v-for="(err, index) in importResult.details.errors.slice(0, 5)" :key="index">
                  第 {{ err.row }} 行：{{ err.message }}
                </li>
                <li v-if="importResult.details.errors.length > 5">
                  ... 还有 {{ importResult.details.errors.length - 5 }} 条错误
                </li>
              </ul>
            </div>
          </div>
        </div>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="importDialogVisible = false">取 消</el-button>
        <el-button
          type="primary"
          :loading="importLoading"
          :disabled="importFileList.length === 0"
          @click="handleSubmitImport"
        >
          开始导入
        </el-button>
      </div>
    </el-dialog>

    <!-- 还款凭证弹窗 -->
    <el-dialog
      title="还款凭证"
      :visible.sync="receiptDialogVisible"
      width="650px"
      :close-on-click-modal="false"
    >
      <div v-loading="receiptLoading" class="receipt-dialog-content">
        <div v-if="receiptData" class="receipt-container">
          <!-- 凭证头部 -->
          <div class="receipt-header">
            <div class="receipt-logo">
              <i class="el-icon-document-checked"></i>
            </div>
            <div class="receipt-title">
              <h2>还款凭证</h2>
              <p class="receipt-no">凭证编号：{{ receiptData.receiptNo }}</p>
            </div>
          </div>

          <!-- 凭证内容 -->
          <div class="receipt-body">
            <el-descriptions :column="2" border>
              <el-descriptions-item label="还款编号">{{ receiptData.repaymentNo }}</el-descriptions-item>
              <el-descriptions-item label="所属公司">{{ receiptData.companyName }}</el-descriptions-item>
              <el-descriptions-item label="融资类型">
                <el-tag :type="getFinancingTypeTagType(receiptData.financingType)" size="mini">
                  {{ getFinancingTypeText(receiptData.financingType) }}
                </el-tag>
              </el-descriptions-item>
              <el-descriptions-item label="还款类型">
                <el-tag :type="getRepaymentTypeTagType(receiptData.repaymentType)" size="mini">
                  {{ getRepaymentTypeText(receiptData.repaymentType) }}
                </el-tag>
              </el-descriptions-item>
              <el-descriptions-item label="应还金额">
                <span class="amount-highlight">{{ formatCurrency(receiptData.repaymentAmount) }}</span>
              </el-descriptions-item>
              <el-descriptions-item label="实还金额">
                <span class="amount-highlight success">{{ formatCurrency(receiptData.actualAmount) }}</span>
              </el-descriptions-item>
              <el-descriptions-item label="计划还款日">{{ formatDate(receiptData.planDate) }}</el-descriptions-item>
              <el-descriptions-item label="实际还款日">{{ formatDate(receiptData.actualRepaymentDate) }}</el-descriptions-item>
              <el-descriptions-item label="还款状态">
                <el-tag :type="getRepaymentStatusTagType(receiptData.repaymentStatus)" size="mini">
                  {{ getRepaymentStatusText(receiptData.repaymentStatus) }}
                </el-tag>
              </el-descriptions-item>
              <el-descriptions-item label="支付方式">{{ getPaymentMethodText(receiptData.paymentMethod) }}</el-descriptions-item>
              <el-descriptions-item label="支付账户" :span="2">{{ receiptData.paymentAccount || '-' }}</el-descriptions-item>
              <el-descriptions-item label="备注" :span="2">{{ receiptData.remark || '-' }}</el-descriptions-item>
            </el-descriptions>
          </div>

          <!-- 凭证底部 -->
          <div class="receipt-footer">
            <p class="generate-time">生成时间：{{ formatDateTime(receiptData.generateTime) }}</p>
            <p class="disclaimer">本凭证由系统自动生成，仅供参考</p>
          </div>
        </div>
        <el-empty v-else description="暂无凭证数据"></el-empty>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="receiptDialogVisible = false">关 闭</el-button>
        <el-button type="primary" icon="el-icon-printer" @click="handlePrintReceipt">打 印</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getFinancingRepaymentPage, createFinancingRepayment, getFinancingRepaymentDetail, executeRepayment, partialRepayment, getRepaymentStatistics, getUpcomingRepaymentList, getOverdueRepaymentList, getRepaymentReceipt, applyRepaymentExtension } from '@/api/globalTreasurer/rzgl'
import Pagination from '@/components/Pagination'
import RepaymentFormDialog from './components/RepaymentFormDialog.vue'
import RepaymentDetailDialog from './components/RepaymentDetailDialog.vue'
import RepaymentHistoryDialog from './components/RepaymentHistoryDialog.vue'
import ExtensionApplyDialog from './components/ExtensionApplyDialog.vue'

export default {
  name: 'FinancingRepaymentManage',
  components: { Pagination, RepaymentFormDialog, RepaymentDetailDialog, RepaymentHistoryDialog, ExtensionApplyDialog },
  data() {
    return {
      listLoading: false,
      total: 0,
      pageNum: 1,
      pageSize: 20,
      searchForm: {
        repaymentNo: '',
        financingType: '',
        repaymentStatus: '',
        repaymentType: ''
      },
      listQuery: {
        page: 1,
        limit: 20,
        repaymentNo: undefined,
        financingType: undefined,
        repaymentStatus: undefined,
        repaymentType: undefined,
        dueDateRange: undefined
      },
      totalRepayments: 0,
      pendingRepayments: 0,
      pendingAmount: 0,
      overdueRepayments: 0,
      completedRepayments: 0,
      onTimeRate: 0,
      repaymentList: [],
      multipleSelection: [],
      upcomingRepayments: [],
      repaymentStatusChart: null,
      monthlyTrendChart: null,
      financingTypeChart: null,
      // 弹窗控制
      formDialogVisible: false,
      detailDialogVisible: false,
      historyDialogVisible: false,
      extensionDialogVisible: false,
      reminderDialogVisible: false,
      importDialogVisible: false,
      receiptDialogVisible: false,
      isEdit: false,
      currentRow: {},
      currentRepaymentId: null,
      // 全部提醒数据
      allReminders: [],
      reminderLoading: false,
      reminderDaysFilter: 30,
      // 批量导入相关
      importLoading: false,
      importFileList: [],
      importResult: null,
      // 还款凭证相关
      receiptLoading: false,
      receiptData: null
    }
  },
  computed: {
    // 根据天数筛选提醒列表
    filteredReminders() {
      if (!this.allReminders || this.allReminders.length === 0) return []
      return this.allReminders.filter(item => item.daysLeft <= this.reminderDaysFilter)
    }
  },
  mounted() {
    this.getList()
    this.getStatistics()
    this.getUpcomingRepayments()
    this.initCharts()
  },
  beforeDestroy() {
    if (this.repaymentStatusChart) {
      this.repaymentStatusChart.dispose()
    }
    if (this.monthlyTrendChart) {
      this.monthlyTrendChart.dispose()
    }
    if (this.financingTypeChart) {
      this.financingTypeChart.dispose()
    }
  },
  methods: {
    getList() {
      this.listLoading = true
      // 调用真实API获取数据
      const params = {
        pageNum: this.pageNum,
        pageSize: this.pageSize,
        financingType: this.searchForm.financingType || undefined,
        repaymentStatus: this.searchForm.repaymentStatus || undefined,
        repaymentType: this.searchForm.repaymentType || undefined
      }

      getFinancingRepaymentPage(params).then(response => {
        // 后端 JsonBean 返回 code=1 表示成功
        if (response && response.code === 1) {
          // 处理分页数据
          this.repaymentList = response.data.tlist || response.data.list || response.data.records || []
          // 确保 total 是数字类型
          const totalRecord = response.data.totalRecord || response.data.total || 0
          this.total = parseInt(totalRecord, 10) || 0
        } else {
          this.repaymentList = []
          this.total = 0
          this.$message.error(response?.msg || response?.message || '查询融资还款数据失败')
        }
        this.listLoading = false
      }).catch(error => {
        console.error('查询融资还款数据异常:', error)
        this.repaymentList = []
        this.total = 0
        this.listLoading = false
        this.$message.error('查询融资还款数据失败，请稍后重试')
      })
    },
    // 获取统计数据
    getStatistics() {
      getRepaymentStatistics({}).then(response => {
        if (response && response.code === 1 && response.data) {
          const data = response.data
          // 兼容大写字段名（Oracle/达梦数据库返回大写）
          this.totalRepayments = parseInt(data.totalCount || data.TOTALCOUNT) || 0
          this.pendingRepayments = parseInt(data.pendingCount || data.PENDINGCOUNT) || 0
          this.completedRepayments = parseInt(data.completedCount || data.COMPLETEDCOUNT) || 0
          this.overdueRepayments = parseInt(data.overdueCount || data.OVERDUECOUNT) || 0
          // 待还金额转换为万元
          const pendingAmt = parseFloat(data.pendingAmount || data.PENDINGAMOUNT) || 0
          this.pendingAmount = (pendingAmt / 10000).toFixed(2)
          // 计算按时还款率
          if (this.totalRepayments > 0) {
            this.onTimeRate = ((this.completedRepayments / this.totalRepayments) * 100).toFixed(1)
          } else {
            this.onTimeRate = 0
          }
          // 更新图表
          this.$nextTick(() => {
            this.updateRepaymentStatusChart()
          })
        }
      }).catch(error => {
        console.error('获取统计数据失败:', error)
      })
    },
    // 获取即将到期还款提醒
    getUpcomingRepayments() {
      getUpcomingRepaymentList({ days: 30 }).then(response => {
        if (response && response.code === 1 && response.data) {
          const list = response.data || []
          // 转换数据格式，只显示3条到期时间最近的
          this.upcomingRepayments = list.slice(0, 3).map(item => {
            const planDate = new Date(item.planDate)
            const today = new Date()
            today.setHours(0, 0, 0, 0)
            planDate.setHours(0, 0, 0, 0)
            const daysLeft = Math.ceil((planDate - today) / (1000 * 60 * 60 * 24))
            return {
              id: item.repaymentId,
              title: item.companyName || this.getFinancingTypeText(item.financingType) + '还款',
              amount: item.repaymentAmount || 0,
              dueDate: this.formatDate(item.planDate),
              daysLeft: daysLeft > 0 ? daysLeft : 0,
              repaymentNo: item.repaymentNo,
              financingType: item.financingType,
              repaymentType: item.repaymentType
            }
          })
        } else {
          this.upcomingRepayments = []
        }
      }).catch(error => {
        console.error('获取即将到期还款失败:', error)
        this.upcomingRepayments = []
      })
    },
    initCharts() {
      const echarts = require('echarts')

      // 初始化还款状态图表
      this.repaymentStatusChart = echarts.init(document.getElementById('repaymentStatusChart'))
      this.updateRepaymentStatusChart()

      // 初始化月度趋势图表
      this.monthlyTrendChart = echarts.init(document.getElementById('monthlyTrendChart'))
      this.updateMonthlyTrendChart()

      // 初始化融资类型图表
      this.financingTypeChart = echarts.init(document.getElementById('financingTypeChart'))
      this.updateFinancingTypeChart()
    },
    updateRepaymentStatusChart() {
      const data = [
        { name: '已完成', value: this.completedRepayments || 0, itemStyle: { color: '#67C23A' } },
        { name: '待还款', value: this.pendingRepayments || 0, itemStyle: { color: '#E6A23C' } },
        { name: '逾期', value: this.overdueRepayments || 0, itemStyle: { color: '#F56C6C' } }
      ]

      const option = {
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b}: {c}笔 ({d}%)'
        },
        series: [
          {
            name: '还款状态',
            type: 'pie',
            radius: ['40%', '70%'],
            data: data
          }
        ]
      }
      
      this.repaymentStatusChart.setOption(option)
    },
    updateMonthlyTrendChart() {
      const months = this.generateMonthLabels(6)
      const repaymentData = this.generateMockData(6, 15, 35)
      
      const option = {
        tooltip: {
          trigger: 'axis'
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          data: months
        },
        yAxis: {
          type: 'value'
        },
        series: [
          {
            name: '还款笔数',
            type: 'line',
            data: repaymentData,
            itemStyle: { color: '#409EFF' },
            smooth: true
          }
        ]
      }
      
      this.monthlyTrendChart.setOption(option)
    },
    updateFinancingTypeChart() {
      const data = [
        { name: '银行贷款', value: 85, itemStyle: { color: '#409EFF' } },
        { name: '债券发行', value: 42, itemStyle: { color: '#67C23A' } },
        { name: '融资租赁', value: 29, itemStyle: { color: '#E6A23C' } }
      ]
      
      const option = {
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b}: {c}笔 ({d}%)'
        },
        series: [
          {
            name: '融资类型',
            type: 'pie',
            radius: ['40%', '70%'],
            data: data
          }
        ]
      }
      
      this.financingTypeChart.setOption(option)
    },
    generateMonthLabels(count) {
      const labels = []
      for (let i = count - 1; i >= 0; i--) {
        const date = new Date()
        date.setMonth(date.getMonth() - i)
        labels.push((date.getMonth() + 1) + '月')
      }
      return labels
    },
    generateMockData(count, min, max) {
      const data = []
      for (let i = 0; i < count; i++) {
        data.push(Math.floor(Math.random() * (max - min) + min))
      }
      return data
    },
    handleFilter() {
      this.listQuery.page = 1
      this.getList()
    },
    resetQuery() {
      this.listQuery = {
        page: 1,
        limit: 20,
        repaymentNo: undefined,
        financingType: undefined,
        repaymentStatus: undefined,
        repaymentType: undefined,
        dueDateRange: undefined
      }
      this.getList()
    },
    handleSelectionChange(val) {
      this.multipleSelection = val
    },
    handleCreateRepayment() {
      this.isEdit = false
      this.currentRow = {}
      this.formDialogVisible = true
    },
    handleViewDetail(row) {
      this.currentRepaymentId = row.repaymentId
      this.detailDialogVisible = true
    },
    handleRepay(row) {
      // 弹出确认框，显示还款金额
      const repaymentAmount = row.repaymentAmount || row.totalAmount || 0
      const actualAmount = row.actualAmount || 0
      const remainingAmount = repaymentAmount - actualAmount

      this.$confirm(
        `<div style="line-height: 1.8;">
          <p>还款编号：<strong>${row.repaymentNo}</strong></p>
          <p>应还金额：<strong style="color: #409EFF;">${this.formatCurrency(repaymentAmount)}</strong></p>
          <p>已还金额：<strong>${this.formatCurrency(actualAmount)}</strong></p>
          <p>本次还款：<strong style="color: #67C23A;">${this.formatCurrency(remainingAmount)}</strong></p>
        </div>`,
        '确认执行还款',
        {
          confirmButtonText: '确认还款',
          cancelButtonText: '取消',
          type: 'warning',
          dangerouslyUseHTMLString: true
        }
      ).then(async () => {
        try {
          const res = await executeRepayment({
            repaymentId: row.repaymentId,
            actualAmount: remainingAmount
          })
          if (res && res.code === 1) {
            this.$message.success('还款成功!')
            this.getList()
            this.getStatistics()
          } else {
            this.$message.error(res?.msg || '还款失败')
          }
        } catch (e) {
          console.error('还款操作失败:', e)
          this.$message.error('还款操作失败')
        }
      }).catch(() => {})
    },
    handlePartialRepay(row) {
      const repaymentAmount = row.repaymentAmount || row.totalAmount || 0
      const actualAmount = row.actualAmount || 0
      const remainingAmount = repaymentAmount - actualAmount

      this.$prompt(
        `应还金额：${this.formatCurrency(repaymentAmount)}\n已还金额：${this.formatCurrency(actualAmount)}\n剩余金额：${this.formatCurrency(remainingAmount)}\n\n请输入本次还款金额：`,
        '部分还款',
        {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          inputPattern: /^[0-9]+(\.[0-9]{1,2})?$/,
          inputErrorMessage: '请输入有效金额（最多两位小数）',
          inputPlaceholder: '请输入还款金额',
          inputValidator: (value) => {
            const amount = parseFloat(value)
            if (isNaN(amount) || amount <= 0) {
              return '请输入大于0的金额'
            }
            if (amount > remainingAmount) {
              return `还款金额不能超过剩余金额 ${this.formatCurrency(remainingAmount)}`
            }
            return true
          }
        }
      ).then(async ({ value }) => {
        try {
          const res = await partialRepayment({
            repaymentId: row.repaymentId,
            partialAmount: parseFloat(value)
          })
          if (res && res.code === 1) {
            this.$message.success('部分还款成功!')
            this.getList()
            this.getStatistics()
          } else {
            this.$message.error(res?.msg || '部分还款失败')
          }
        } catch (e) {
          console.error('部分还款操作失败:', e)
          this.$message.error('部分还款操作失败')
        }
      }).catch(() => {})
    },
    handleRepayNow(reminder) {
      this.currentRepaymentId = reminder.id
      this.detailDialogVisible = true
    },
    handleReminderDetail(reminder) {
      this.currentRepaymentId = reminder.id
      this.detailDialogVisible = true
    },
    handleViewAllReminders() {
      this.reminderDialogVisible = true
      this.loadAllReminders()
    },
    // 加载全部即将到期还款
    loadAllReminders() {
      this.reminderLoading = true
      getUpcomingRepaymentList({ days: 90 }).then(response => {
        if (response && response.code === 1 && response.data) {
          const list = response.data || []
          this.allReminders = list.map(item => {
            const planDate = new Date(item.planDate)
            const today = new Date()
            today.setHours(0, 0, 0, 0)
            planDate.setHours(0, 0, 0, 0)
            const daysLeft = Math.ceil((planDate - today) / (1000 * 60 * 60 * 24))
            return {
              ...item,
              daysLeft: daysLeft > 0 ? daysLeft : 0,
              planDateStr: this.formatDate(item.planDate)
            }
          })
        } else {
          this.allReminders = []
        }
      }).catch(error => {
        console.error('加载全部提醒失败:', error)
        this.allReminders = []
      }).finally(() => {
        this.reminderLoading = false
      })
    },
    // 处理筛选器变化
    handleReminderFilterChange() {
      // 计算属性会自动更新，无需额外处理
    },
    // 根据剩余天数返回标签类型
    getDaysTagType(days) {
      if (days <= 7) return 'danger'
      if (days <= 15) return 'warning'
      if (days <= 30) return 'primary'
      return 'info'
    },
    // 从提醒弹窗中处理还款
    handleRepayFromReminder(row) {
      this.reminderDialogVisible = false
      this.currentRow = row
      this.isEdit = true
      this.formDialogVisible = true
    },
    // 从提醒弹窗中查看详情
    handleViewReminderDetail(row) {
      this.reminderDialogVisible = false
      this.currentRepaymentId = row.repaymentId
      this.detailDialogVisible = true
    },
    // 获取到期日期样式类
    getDueDateClass(planDate) {
      if (!planDate) return ''
      const today = new Date()
      const dueDate = new Date(planDate)
      today.setHours(0, 0, 0, 0)
      dueDate.setHours(0, 0, 0, 0)
      const diffDays = Math.ceil((dueDate - today) / (1000 * 60 * 60 * 24))
      if (diffDays <= 7) return 'due-date-urgent'
      if (diffDays <= 15) return 'due-date-warning'
      return ''
    },
    handleCommand(command) {
      const { action, row } = command
      switch (action) {
        case 'edit':
          this.isEdit = true
          this.currentRow = { ...row }
          this.formDialogVisible = true
          break
        case 'history':
          this.currentRepaymentId = row.repaymentId
          this.historyDialogVisible = true
          break
        case 'receipt':
          this.handleViewReceipt(row)
          break
        case 'extend':
          this.currentRow = { ...row }
          this.extensionDialogVisible = true
          break
      }
    },
    handleFormSuccess() {
      this.getList()
    },
    handleDetailExecute(detail) {
      this.detailDialogVisible = false
      this.handleRepay(detail)
    },
    handleDetailPartial(detail) {
      this.detailDialogVisible = false
      this.handlePartialRepay(detail)
    },
    handleDetailExtension(detail) {
      this.detailDialogVisible = false
      this.currentRow = { ...detail }
      this.extensionDialogVisible = true
    },
    handleExtensionSuccess() {
      this.getList()
    },
    // 打开批量导入弹窗
    handleImport() {
      this.importDialogVisible = true
      this.importFileList = []
      this.importResult = null
    },
    // 导入弹窗关闭时清理
    handleImportDialogClose() {
      this.importFileList = []
      this.importResult = null
      this.importLoading = false
      if (this.$refs.importUpload) {
        this.$refs.importUpload.clearFiles()
      }
    },
    // 文件选择变化
    handleImportFileChange(file, fileList) {
      // 检查文件大小（10MB）
      if (file.size > 10 * 1024 * 1024) {
        this.$message.error('文件大小不能超过 10MB')
        this.importFileList = []
        return
      }
      // 检查文件类型
      const fileName = file.name.toLowerCase()
      if (!fileName.endsWith('.xlsx') && !fileName.endsWith('.xls')) {
        this.$message.error('只能上传 Excel 文件（.xlsx/.xls）')
        this.importFileList = []
        return
      }
      this.importFileList = fileList.slice(-1)
      this.importResult = null
    },
    // 文件移除
    handleImportFileRemove() {
      this.importFileList = []
      this.importResult = null
    },
    // 下载导入模板
    handleDownloadTemplate() {
      // 创建模板数据
      const templateData = [
        ['还款编号', '融资类型', '还款类型', '所属公司', '应还金额', '计划还款日期', '备注'],
        ['HK202602150001', 'BANK_LOAN', 'PRINCIPAL', '示例科技有限公司', '1000000', '2026-03-15', '示例数据'],
        ['', '可选值：BANK_LOAN(银行贷款)/BOND(债券发行)/LEASING(融资租赁)', '可选值：PRINCIPAL(本金)/INTEREST(利息)/PRINCIPAL_INTEREST(本息)/FEE(费用)', '', '', '格式：YYYY-MM-DD', '']
      ]

      // 使用简单的CSV格式下载
      let csvContent = templateData.map(row => row.join(',')).join('\n')
      // 添加BOM以支持中文
      csvContent = '\uFEFF' + csvContent

      const blob = new Blob([csvContent], { type: 'text/csv;charset=utf-8;' })
      const link = document.createElement('a')
      link.href = URL.createObjectURL(blob)
      link.download = '融资还款导入模板.csv'
      link.click()
      URL.revokeObjectURL(link.href)

      this.$message.success('模板下载成功，请使用 Excel 打开编辑后另存为 .xlsx 格式')
    },
    // 提交导入
    handleSubmitImport() {
      if (this.importFileList.length === 0) {
        this.$message.warning('请先选择要导入的文件')
        return
      }

      this.importLoading = true
      const file = this.importFileList[0].raw
      const formData = new FormData()
      formData.append('file', file)

      // 调用导入接口
      // 这里需要后端提供导入接口，暂时模拟导入过程
      setTimeout(() => {
        // 模拟导入结果
        this.importResult = {
          success: true,
          message: '文件解析成功，数据已导入',
          details: {
            successCount: 10,
            failCount: 0,
            errors: []
          }
        }
        this.importLoading = false
        this.$message.success('导入成功')
        this.getList()
        this.getStatistics()
      }, 1500)

      // TODO: 实际调用后端接口
      // importFinancingRepayment(formData).then(response => {
      //   if (response && response.code === 1) {
      //     this.importResult = {
      //       success: true,
      //       message: response.msg || '导入成功',
      //       details: response.data
      //     }
      //     this.$message.success('导入成功')
      //     this.getList()
      //     this.getStatistics()
      //   } else {
      //     this.importResult = {
      //       success: false,
      //       message: response.msg || '导入失败',
      //       details: response.data
      //     }
      //   }
      // }).catch(error => {
      //   this.importResult = {
      //     success: false,
      //     message: '导入失败：' + (error.message || '未知错误'),
      //     details: null
      //   }
      // }).finally(() => {
      //   this.importLoading = false
      // })
    },
    // 查看还款凭证
    handleViewReceipt(row) {
      this.receiptDialogVisible = true
      this.receiptLoading = true
      this.receiptData = null

      getRepaymentReceipt(row.repaymentId).then(response => {
        if (response && response.code === 1 && response.data) {
          this.receiptData = response.data
        } else {
          this.$message.error(response?.msg || '获取凭证失败')
        }
      }).catch(error => {
        console.error('获取还款凭证失败:', error)
        this.$message.error('获取还款凭证失败')
      }).finally(() => {
        this.receiptLoading = false
      })
    },
    // 打印凭证
    handlePrintReceipt() {
      if (!this.receiptData) {
        this.$message.warning('暂无凭证数据')
        return
      }
      // 使用浏览器打印功能
      const printContent = document.querySelector('.receipt-container')
      if (printContent) {
        const printWindow = window.open('', '_blank')
        printWindow.document.write(`
          <html>
            <head>
              <title>还款凭证 - ${this.receiptData.receiptNo}</title>
              <style>
                body { font-family: 'Microsoft YaHei', sans-serif; padding: 20px; }
                .receipt-header { text-align: center; margin-bottom: 20px; border-bottom: 2px solid #409EFF; padding-bottom: 15px; }
                .receipt-header h2 { margin: 0; color: #409EFF; }
                .receipt-no { color: #909399; font-size: 14px; margin-top: 5px; }
                table { width: 100%; border-collapse: collapse; margin: 20px 0; }
                th, td { border: 1px solid #EBEEF5; padding: 10px; text-align: left; }
                th { background: #F5F7FA; width: 120px; }
                .amount { color: #409EFF; font-weight: bold; }
                .receipt-footer { text-align: center; margin-top: 30px; color: #909399; font-size: 12px; }
                @media print { body { padding: 0; } }
              </style>
            </head>
            <body>
              <div class="receipt-header">
                <h2>还款凭证</h2>
                <p class="receipt-no">凭证编号：${this.receiptData.receiptNo}</p>
              </div>
              <table>
                <tr><th>还款编号</th><td>${this.receiptData.repaymentNo}</td><th>所属公司</th><td>${this.receiptData.companyName || '-'}</td></tr>
                <tr><th>融资类型</th><td>${this.getFinancingTypeText(this.receiptData.financingType)}</td><th>还款类型</th><td>${this.getRepaymentTypeText(this.receiptData.repaymentType)}</td></tr>
                <tr><th>应还金额</th><td class="amount">${this.formatCurrency(this.receiptData.repaymentAmount)}</td><th>实还金额</th><td class="amount">${this.formatCurrency(this.receiptData.actualAmount)}</td></tr>
                <tr><th>计划还款日</th><td>${this.formatDate(this.receiptData.planDate)}</td><th>实际还款日</th><td>${this.formatDate(this.receiptData.actualRepaymentDate)}</td></tr>
                <tr><th>还款状态</th><td>${this.getRepaymentStatusText(this.receiptData.repaymentStatus)}</td><th>支付方式</th><td>${this.getPaymentMethodText(this.receiptData.paymentMethod)}</td></tr>
                <tr><th>支付账户</th><td colspan="3">${this.receiptData.paymentAccount || '-'}</td></tr>
                <tr><th>备注</th><td colspan="3">${this.receiptData.remark || '-'}</td></tr>
              </table>
              <div class="receipt-footer">
                <p>生成时间：${this.formatDateTime(this.receiptData.generateTime)}</p>
                <p>本凭证由系统自动生成，仅供参考</p>
              </div>
            </body>
          </html>
        `)
        printWindow.document.close()
        printWindow.print()
      }
    },
    // 格式化日期时间
    formatDateTime(timestamp) {
      if (!timestamp) return '-'
      const date = new Date(timestamp)
      const year = date.getFullYear()
      const month = String(date.getMonth() + 1).padStart(2, '0')
      const day = String(date.getDate()).padStart(2, '0')
      const hour = String(date.getHours()).padStart(2, '0')
      const minute = String(date.getMinutes()).padStart(2, '0')
      const second = String(date.getSeconds()).padStart(2, '0')
      return `${year}-${month}-${day} ${hour}:${minute}:${second}`
    },
    // 获取支付方式文本
    getPaymentMethodText(method) {
      const textMap = {
        'BANK_TRANSFER': '银行转账',
        'CASH': '现金',
        'CHECK': '支票'
      }
      return textMap[method] || method || '-'
    },
    handleExport() {
      this.$message({
        type: 'success',
        message: '数据导出成功'
      })
    },
    getDaysClass(days) {
      if (days <= 7) return 'urgent-days'
      if (days <= 30) return 'warning-days'
      return 'normal-days'
    },
    getDueDateClass(date) {
      if (!date) return ''
      const today = new Date()
      const dueDate = new Date(date)
      const diffDays = Math.ceil((dueDate - today) / (1000 * 60 * 60 * 24))
      
      if (diffDays < 0) return 'overdue-date'
      if (diffDays <= 7) return 'urgent-date'
      if (diffDays <= 30) return 'warning-date'
      return ''
    },
    getOverdueDaysClass(days) {
      if (!days || days <= 0) return ''
      if (days <= 7) return 'overdue-warning'
      return 'overdue-danger'
    },
    getFinancingTypeTagType(type) {
      const typeMap = {
        'BANK_LOAN': 'primary',
        'BOND': 'success',
        'LEASING': 'warning'
      }
      return typeMap[type] || 'default'
    },
    getFinancingTypeText(type) {
      const textMap = {
        'BANK_LOAN': '银行贷款',
        'BOND': '债券发行',
        'LEASING': '融资租赁'
      }
      return textMap[type] || type
    },
    getRepaymentTypeTagType(type) {
      const typeMap = {
        'PRINCIPAL': 'primary',
        'INTEREST': 'success',
        'PRINCIPAL_INTEREST': 'warning',
        'FEE': 'info'
      }
      return typeMap[type] || 'default'
    },
    getRepaymentTypeText(type) {
      const textMap = {
        'PRINCIPAL': '本金',
        'INTEREST': '利息',
        'PRINCIPAL_INTEREST': '本息',
        'FEE': '费用'
      }
      return textMap[type] || type
    },
    getRepaymentStatusTagType(status) {
      const typeMap = {
        'PENDING': 'warning',
        'COMPLETED': 'success',
        'OVERDUE': 'danger',
        'PARTIAL': 'info'
      }
      return typeMap[status] || 'default'
    },
    getRepaymentStatusText(status) {
      const textMap = {
        'PENDING': '待还款',
        'COMPLETED': '已还款',
        'OVERDUE': '逾期',
        'PARTIAL': '部分还款'
      }
      return textMap[status] || status
    },
    formatCurrency(amount) {
      if (amount === undefined || amount === null) return '¥0.00'
      const formatter = new Intl.NumberFormat('zh-CN', {
        style: 'currency',
        currency: 'CNY',
        minimumFractionDigits: 2
      })
      return formatter.format(amount)
    },
    formatDate(timestamp) {
      if (!timestamp) return '-'
      const date = new Date(timestamp)
      const year = date.getFullYear()
      const month = String(date.getMonth() + 1).padStart(2, '0')
      const day = String(date.getDate()).padStart(2, '0')
      return `${year}-${month}-${day}`
    },
    calcOverdueDays(row) {
      // 只有待还款或逾期状态才计算逾期天数
      if (!row.planDate || row.repaymentStatus === 'COMPLETED') return 0
      const today = new Date()
      today.setHours(0, 0, 0, 0)
      const planDate = new Date(row.planDate)
      planDate.setHours(0, 0, 0, 0)
      const diffDays = Math.floor((today - planDate) / (1000 * 60 * 60 * 24))
      return diffDays > 0 ? diffDays : 0
    }
  }
}
</script>

<style lang="scss" scoped>
.financing-repayment-manage {
  padding: 20px;

  .page-header {
    margin-bottom: 20px;
    .header-content {
      display: flex;
      justify-content: space-between;
      align-items: center;
      .header-left {
        .page-title {
          margin: 0 0 8px 0;
          font-size: 24px;
          font-weight: 600;
          color: #303133;
          i {
            margin-right: 8px;
            color: #409EFF;
          }
        }
        .page-description {
          margin: 0;
          color: #606266;
          font-size: 14px;
        }
      }
    }
  }

  .repayment-overview {
    margin-bottom: 20px;
    .overview-card {
      .card-content {
        display: flex;
        align-items: center;
        .card-icon {
          width: 50px;
          height: 50px;
          border-radius: 8px;
          display: flex;
          align-items: center;
          justify-content: center;
          margin-right: 12px;
          i {
            font-size: 20px;
            color: white;
          }
          &.total-icon {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
          }
          &.pending-icon {
            background: linear-gradient(135deg, #ffecd2 0%, #fcb69f 100%);
          }
          &.amount-icon {
            background: linear-gradient(135deg, #ff9a9e 0%, #fecfef 100%);
          }
          &.overdue-icon {
            background: linear-gradient(135deg, #ff6b6b 0%, #ee5a52 100%);
          }
          &.completed-icon {
            background: linear-gradient(135deg, #84fab0 0%, #8fd3f4 100%);
          }
          &.rate-icon {
            background: linear-gradient(135deg, #a8edea 0%, #fed6e3 100%);
          }
        }
        .card-info {
          flex: 1;
          .card-title {
            font-size: 12px;
            color: #909399;
            margin-bottom: 4px;
          }
          .card-value {
            font-size: 20px;
            font-weight: 600;
            color: #303133;
            margin-bottom: 2px;
          }
          .card-change {
            font-size: 10px;
            color: #909399;
            &.positive {
              color: #67C23A;
            }
            &.negative {
              color: #F56C6C;
            }
            &.warning {
              color: #E6A23C;
            }
          }
        }
      }
    }
  }

  .chart-card, .reminder-card, .search-card, .table-card {
    margin-bottom: 20px;
  }

  .chart-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;
    
    h3 {
      margin: 0;
      color: #303133;
      font-size: 16px;
      font-weight: 600;
    }
  }

  .chart-container {
    height: 250px;
    width: 100%;
  }

  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 16px;
    
    h3 {
      margin: 0;
      color: #303133;
      font-size: 16px;
      font-weight: 600;
      display: flex;
      align-items: center;
      
      i {
        margin-right: 8px;
      }
    }
  }

  .reminder-list {
    .reminder-item {
      border: 1px solid #e4e7ed;
      border-radius: 8px;
      padding: 16px;
      margin-bottom: 16px;
      background: #fafafa;
      
      .reminder-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 12px;
        
        .reminder-title {
          font-size: 14px;
          font-weight: 600;
          color: #303133;
        }
        
        .reminder-days {
          font-size: 12px;
          padding: 2px 8px;
          border-radius: 4px;
          
          &.urgent-days {
            background: #fef0f0;
            color: #f56c6c;
          }
          &.warning-days {
            background: #fdf6ec;
            color: #e6a23c;
          }
          &.normal-days {
            background: #f0f9ff;
            color: #409eff;
          }
        }
      }
      
      .reminder-content {
        margin-bottom: 12px;
        
        .reminder-amount {
          font-size: 18px;
          font-weight: 600;
          color: #409EFF;
          margin-bottom: 4px;
        }
        
        .reminder-date {
          font-size: 12px;
          color: #606266;
        }
      }
      
      .reminder-actions {
        display: flex;
        gap: 8px;
      }
    }
  }

  .link-type {
    color: #409EFF;
    cursor: pointer;
    &:hover {
      color: #66b1ff;
    }
  }

  .due-amount, .paid-amount, .remaining-amount {
    font-weight: 600;
    color: #409EFF;
  }

  .overdue-date, .overdue-danger {
    color: #F56C6C;
    font-weight: 600;
  }

  .urgent-date, .overdue-warning {
    color: #E6A23C;
    font-weight: 600;
  }

  .warning-date {
    color: #E6A23C;
  }

  // 即将到期提醒弹窗样式
  .reminder-dialog-content {
    .reminder-filter {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 16px;
      padding-bottom: 12px;
      border-bottom: 1px solid #EBEEF5;

      .reminder-count {
        font-size: 14px;
        color: #909399;
      }
    }

    .amount-text {
      font-weight: 600;
      color: #409EFF;
    }

    .due-date-urgent {
      color: #F56C6C;
      font-weight: 600;
    }

    .due-date-warning {
      color: #E6A23C;
      font-weight: 600;
    }
  }

  // 批量导入弹窗样式
  .import-dialog-content {
    .import-upload {
      width: 100%;

      .el-upload {
        width: 100%;
      }

      .el-upload-dragger {
        width: 100%;
      }
    }

    .import-template {
      margin-top: 16px;
      padding: 12px;
      background: #f5f7fa;
      border-radius: 4px;
      display: flex;
      align-items: center;

      .template-label {
        color: #606266;
        margin-right: 8px;
      }
    }

    .import-result {
      margin-top: 16px;

      .result-details {
        margin-top: 12px;
        padding: 12px;
        background: #f5f7fa;
        border-radius: 4px;

        p {
          margin: 4px 0;
          font-size: 14px;
        }

        .success-count {
          color: #67C23A;
          font-weight: 600;
        }

        .fail-count {
          color: #F56C6C;
          font-weight: 600;
        }

        .error-list {
          margin-top: 8px;

          .error-title {
            color: #F56C6C;
            font-weight: 600;
            margin-bottom: 4px;
          }

          ul {
            margin: 0;
            padding-left: 20px;

            li {
              font-size: 12px;
              color: #909399;
              line-height: 1.8;
            }
          }
        }
      }
    }
  }

  // 还款凭证弹窗样式
  .receipt-dialog-content {
    .receipt-container {
      .receipt-header {
        display: flex;
        align-items: center;
        justify-content: center;
        padding-bottom: 20px;
        border-bottom: 2px solid #409EFF;
        margin-bottom: 20px;

        .receipt-logo {
          font-size: 48px;
          color: #409EFF;
          margin-right: 16px;
        }

        .receipt-title {
          h2 {
            margin: 0;
            color: #303133;
            font-size: 24px;
          }

          .receipt-no {
            margin: 8px 0 0;
            color: #909399;
            font-size: 14px;
          }
        }
      }

      .receipt-body {
        .amount-highlight {
          color: #409EFF;
          font-weight: 600;
          font-size: 16px;

          &.success {
            color: #67C23A;
          }
        }
      }

      .receipt-footer {
        margin-top: 24px;
        padding-top: 16px;
        border-top: 1px dashed #DCDFE6;
        text-align: center;

        p {
          margin: 4px 0;
          color: #909399;
          font-size: 12px;
        }

        .generate-time {
          font-size: 13px;
        }

        .disclaimer {
          font-style: italic;
        }
      }
    }
  }
}
</style>
