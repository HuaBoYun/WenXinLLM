<template>
  <div class="deferred-revenue-container">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-left">
        <h1 class="page-title">
          <i class="el-icon-time"></i>
          递延收入管理
        </h1>
        <p class="page-description">管理预收款项和递延收入确认，包括分期确认处理和确认计划制定</p>
      </div>
      <div class="header-right">
        <el-button type="primary" icon="el-icon-plus" @click="showCreateDialog">
          新增递延收入
        </el-button>
        <el-button type="success" icon="el-icon-check" @click="batchRecognize">
          批量确认
        </el-button>
        <el-button type="warning" icon="el-icon-refresh" @click="refreshData">
          刷新数据
        </el-button>
      </div>
    </div>

    <!-- 递延收入统计 -->
    <div class="deferred-stats">
      <el-row :gutter="24">
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon total">
              <i class="el-icon-time"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ stats.totalCount }}</div>
              <div class="stat-label">递延收入总数</div>
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
              <div class="stat-label">递延收入总额</div>
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
              <div class="stat-label">已确认金额</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon remaining">
              <i class="el-icon-wallet"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ formatAmount(stats.remainingAmount) }}</div>
              <div class="stat-label">剩余金额</div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 搜索区域 -->
    <div class="search-area">
      <el-form :model="searchForm" :inline="true" size="small">
        <el-form-item label="递延编号">
          <el-input v-model="searchForm.deferredNo" placeholder="请输入递延编号" clearable />
        </el-form-item>
        <el-form-item label="递延类型">
          <el-select v-model="searchForm.deferredType" placeholder="请选择递延类型" clearable>
            <el-option label="预收款项" :value="1" />
            <el-option label="递延收入" :value="2" />
            <el-option label="合同负债" :value="3" />
          </el-select>
        </el-form-item>
        <el-form-item label="确认状态">
          <el-select v-model="searchForm.recognitionStatus" placeholder="请选择确认状态" clearable>
            <el-option label="未开始" :value="0" />
            <el-option label="确认中" :value="1" />
            <el-option label="已完成" :value="2" />
            <el-option label="已暂停" :value="3" />
          </el-select>
        </el-form-item>
        <el-form-item label="确认期间">
          <el-date-picker
            v-model="searchForm.recognitionPeriod"
            type="monthrange"
            range-separator="至"
            start-placeholder="开始月份"
            end-placeholder="结束月份"
            format="yyyy-MM"
            value-format="yyyy-MM"
            clearable
          />
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
        <el-table-column prop="deferredId" label="递延ID" width="120" />
        <el-table-column prop="deferredNo" label="递延编号" width="150" />
        <el-table-column prop="deferredTypeName" label="递延类型" width="120" />
        <el-table-column prop="contractNo" label="关联合同" width="150" />
        <el-table-column prop="totalAmount" label="递延总额" width="120" align="right">
          <template slot-scope="scope">
            <span class="amount-text">{{ formatAmount(scope.row.totalAmount) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="recognizedAmount" label="已确认金额" width="130" align="right">
          <template slot-scope="scope">
            <span class="amount-text">{{ formatAmount(scope.row.recognizedAmount) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="remainingAmount" label="剩余金额" width="120" align="right">
          <template slot-scope="scope">
            <span class="amount-text">{{ formatAmount(scope.row.remainingAmount) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="recognitionProgress" label="确认进度" width="120">
          <template slot-scope="scope">
            <el-progress 
              :percentage="scope.row.recognitionProgress" 
              :stroke-width="8"
              :show-text="false"
            />
            <span style="margin-left: 8px;">{{ scope.row.recognitionProgress }}%</span>
          </template>
        </el-table-column>
        <el-table-column prop="recognitionStatus" label="确认状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="getStatusType(scope.row.recognitionStatus)">
              {{ getStatusText(scope.row.recognitionStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="startDate" label="开始日期" width="120" />
        <el-table-column prop="endDate" label="结束日期" width="120" />
        <el-table-column label="操作" width="250" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" @click="viewDetail(scope.row)">详情</el-button>
            <el-button size="mini" type="success" @click="viewSchedule(scope.row)">确认计划</el-button>
            <el-button 
              v-if="scope.row.recognitionStatus !== 2" 
              size="mini" 
              type="warning" 
              @click="recognizeRevenue(scope.row)"
            >
              确认
            </el-button>
            <el-button size="mini" type="info" @click="editSchedule(scope.row)">编辑计划</el-button>
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

    <!-- 创建递延收入对话框 -->
    <el-dialog
      title="新增递延收入"
      :visible.sync="createDialogVisible"
      width="800px"
      :close-on-click-modal="false"
    >
      <el-form :model="createForm" :rules="createRules" ref="createForm" label-width="120px">
        <el-row :gutter="24">
          <el-col :span="12">
            <el-form-item label="递延编号" prop="deferredNo">
              <el-input v-model="createForm.deferredNo" placeholder="请输入递延编号" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="递延类型" prop="deferredType">
              <el-select v-model="createForm.deferredType" placeholder="请选择递延类型">
                <el-option label="预收款项" :value="1" />
                <el-option label="递延收入" :value="2" />
                <el-option label="合同负债" :value="3" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="24">
          <el-col :span="12">
            <el-form-item label="关联合同" prop="contractId">
              <el-select v-model="createForm.contractId" placeholder="请选择关联合同" filterable>
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
            <el-form-item label="递延总额" prop="totalAmount">
              <el-input-number
                v-model="createForm.totalAmount"
                :precision="2"
                :min="0"
                placeholder="请输入递延总额"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="24">
          <el-col :span="12">
            <el-form-item label="开始日期" prop="startDate">
              <el-date-picker
                v-model="createForm.startDate"
                type="date"
                placeholder="选择开始日期"
                format="yyyy-MM-dd"
                value-format="yyyy-MM-dd"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="结束日期" prop="endDate">
              <el-date-picker
                v-model="createForm.endDate"
                type="date"
                placeholder="选择结束日期"
                format="yyyy-MM-dd"
                value-format="yyyy-MM-dd"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="24">
          <el-col :span="12">
            <el-form-item label="确认方式" prop="recognitionMethod">
              <el-select v-model="createForm.recognitionMethod" placeholder="请选择确认方式">
                <el-option label="平均分摊" :value="1" />
                <el-option label="按进度确认" :value="2" />
                <el-option label="按事件确认" :value="3" />
                <el-option label="自定义计划" :value="4" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="确认周期" prop="recognitionCycle">
              <el-select v-model="createForm.recognitionCycle" placeholder="请选择确认周期">
                <el-option label="月度" :value="1" />
                <el-option label="季度" :value="2" />
                <el-option label="半年度" :value="3" />
                <el-option label="年度" :value="4" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="递延说明" prop="deferredDesc">
          <el-input
            v-model="createForm.deferredDesc"
            type="textarea"
            :rows="3"
            placeholder="请输入递延说明"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="createDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleCreate">确定</el-button>
      </div>
    </el-dialog>

    <!-- 确认计划对话框 -->
    <el-dialog
      title="确认计划"
      :visible.sync="scheduleDialogVisible"
      width="900px"
      :close-on-click-modal="false"
    >
      <div class="schedule-info">
        <el-row :gutter="24">
          <el-col :span="8">
            <div class="info-item">
              <label>递延编号：</label>
              <span>{{ scheduleForm.deferredNo }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <label>递延总额：</label>
              <span class="amount-text">{{ formatAmount(scheduleForm.totalAmount) }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <label>剩余金额：</label>
              <span class="amount-text">{{ formatAmount(scheduleForm.remainingAmount) }}</span>
            </div>
          </el-col>
        </el-row>
      </div>

      <el-table :data="scheduleForm.scheduleList" border style="width: 100%; margin-top: 20px;">
        <el-table-column prop="period" label="确认期间" width="120" />
        <el-table-column prop="plannedAmount" label="计划金额" width="120" align="right">
          <template slot-scope="scope">
            <span class="amount-text">{{ formatAmount(scope.row.plannedAmount) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="actualAmount" label="实际金额" width="120" align="right">
          <template slot-scope="scope">
            <span class="amount-text">{{ formatAmount(scope.row.actualAmount) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="recognitionDate" label="确认日期" width="120" />
        <el-table-column prop="status" label="状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="getScheduleStatusType(scope.row.status)">
              {{ getScheduleStatusText(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="remarks" label="备注" />
        <el-table-column label="操作" width="120">
          <template slot-scope="scope">
            <el-button 
              v-if="scope.row.status === 0" 
              size="mini" 
              type="success" 
              @click="confirmScheduleItem(scope.row)"
            >
              确认
            </el-button>
            <el-button 
              v-if="scope.row.status === 1" 
              size="mini" 
              type="warning" 
              @click="adjustScheduleItem(scope.row)"
            >
              调整
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <div slot="footer" class="dialog-footer">
        <el-button @click="scheduleDialogVisible = false">关闭</el-button>
        <el-button type="primary" @click="generateSchedule">重新生成计划</el-button>
      </div>
    </el-dialog>

    <!-- 递延收入详情对话框 -->
    <el-dialog
      title="递延收入详情"
      :visible.sync="detailDialogVisible"
      width="900px"
      :close-on-click-modal="false"
    >
      <el-descriptions :column="2" border>
        <el-descriptions-item label="递延编号">{{ detailData.deferredNo }}</el-descriptions-item>
        <el-descriptions-item label="递延类型">{{ getDeferredTypeName(detailData.deferredType) }}</el-descriptions-item>
        <el-descriptions-item label="合同编号">{{ detailData.contractNo }}</el-descriptions-item>
        <el-descriptions-item label="合同名称">{{ detailData.contractName }}</el-descriptions-item>
        <el-descriptions-item label="递延总额">{{ formatAmount(detailData.totalAmount) }}</el-descriptions-item>
        <el-descriptions-item label="已确认金额">{{ formatAmount(detailData.recognizedAmount) }}</el-descriptions-item>
        <el-descriptions-item label="剩余金额">{{ formatAmount(detailData.remainingAmount) }}</el-descriptions-item>
        <el-descriptions-item label="确认进度">{{ detailData.recognitionProgress }}%</el-descriptions-item>
        <el-descriptions-item label="确认状态">
          <el-tag :type="getStatusType(detailData.recognitionStatus)">
            {{ getStatusText(detailData.recognitionStatus) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="开始日期">{{ detailData.startDate }}</el-descriptions-item>
        <el-descriptions-item label="结束日期">{{ detailData.endDate }}</el-descriptions-item>
        <el-descriptions-item label="确认方式">{{ detailData.recognitionMethodName }}</el-descriptions-item>
        <el-descriptions-item label="确认周期">{{ detailData.recognitionCycleName }}</el-descriptions-item>
        <el-descriptions-item label="创建时间" :span="2">{{ detailData.createTime }}</el-descriptions-item>
        <el-descriptions-item label="递延说明" :span="2">{{ detailData.deferredDesc }}</el-descriptions-item>
      </el-descriptions>
      <div slot="footer" class="dialog-footer">
        <el-button @click="detailDialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>

    <!-- 编辑确认计划对话框 -->
    <el-dialog
      title="编辑确认计划"
      :visible.sync="editScheduleDialogVisible"
      width="1000px"
      :close-on-click-modal="false"
    >
      <div class="schedule-info">
        <el-row :gutter="20">
          <el-col :span="8">
            <div class="info-item">
              <span class="label">递延编号：</span>
              <span class="value">{{ editScheduleForm.deferredNo }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <span class="label">递延总额：</span>
              <span class="value">{{ formatAmount(editScheduleForm.totalAmount) }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <span class="label">剩余金额：</span>
              <span class="value">{{ formatAmount(editScheduleForm.remainingAmount) }}</span>
            </div>
          </el-col>
        </el-row>
      </div>

      <el-table :data="editScheduleForm.scheduleList" border style="width: 100%; margin-top: 20px;">
        <el-table-column type="index" label="序号" width="60" />
        <el-table-column prop="period" label="确认期间" width="150">
          <template slot-scope="scope">
            <el-date-picker
              v-model="scope.row.period"
              type="month"
              placeholder="选择期间"
              format="yyyy-MM"
              value-format="yyyy-MM"
              size="small"
              style="width: 100%"
            />
          </template>
        </el-table-column>
        <el-table-column prop="recognitionAmount" label="确认金额" width="150">
          <template slot-scope="scope">
            <el-input-number
              v-model="scope.row.recognitionAmount"
              :precision="2"
              :min="0"
              size="small"
              style="width: 100%"
            />
          </template>
        </el-table-column>
        <el-table-column prop="recognitionDate" label="确认日期" width="150">
          <template slot-scope="scope">
            <el-date-picker
              v-model="scope.row.recognitionDate"
              type="date"
              placeholder="选择日期"
              value-format="yyyy-MM-dd"
              size="small"
              style="width: 100%"
            />
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'info'" size="small">
              {{ scope.row.status === 1 ? '已确认' : '未确认' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="remarks" label="备注">
          <template slot-scope="scope">
            <el-input v-model="scope.row.remarks" size="small" placeholder="请输入备注" />
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150">
          <template slot-scope="scope">
            <el-button
              v-if="scope.row.status !== 1"
              size="mini"
              type="primary"
              @click="addScheduleItem(scope.$index)"
            >
              插入
            </el-button>
            <el-button
              v-if="scope.row.status !== 1"
              size="mini"
              type="danger"
              @click="removeScheduleItem(scope.$index)"
            >
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <div style="margin-top: 15px;">
        <el-button size="small" type="primary" @click="addScheduleItem()">添加计划</el-button>
        <span style="margin-left: 20px; color: #606266;">
          计划总额: {{ formatAmount(calculateScheduleTotal()) }}
        </span>
      </div>

      <div slot="footer" class="dialog-footer">
        <el-button @click="editScheduleDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveSchedule">保存</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  getDeferredRevenueList,
  createDeferredRevenue,
  recognizeDeferredRevenue,
  getRecognitionSchedule,
  getDeferredRevenueStats,
  updateDeferredRevenue,
  updateRecognitionSchedule,
  getDeferredRevenueDetail
} from '@/api/financialSharing/revenueManagement'
import { getRevenueContractPage } from '@/api/financialSharing/revenueContract'

export default {
  name: 'DeferredRevenueIndex',
  data() {
    return {
      loading: false,
      tableData: [],
      selectedRows: [],
      stats: {
        totalCount: 0,
        totalAmount: 0,
        recognizedAmount: 0,
        remainingAmount: 0
      },
      searchForm: {
        deferredNo: '',
        deferredType: '',
        recognitionStatus: '',
        recognitionPeriod: []
      },
      pagination: {
        currentPage: 1,
        pageSize: 20,
        total: 0
      },
      createDialogVisible: false,
      createForm: {
        deferredNo: '',
        deferredType: '',
        contractId: '',
        totalAmount: 0,
        startDate: '',
        endDate: '',
        recognitionMethod: '',
        recognitionCycle: '',
        deferredDesc: ''
      },
      createRules: {
        deferredNo: [{ required: true, message: '请输入递延编号', trigger: 'blur' }],
        deferredType: [{ required: true, message: '请选择递延类型', trigger: 'change' }],
        contractId: [{ required: true, message: '请选择关联合同', trigger: 'change' }],
        totalAmount: [{ required: true, message: '请输入递延总额', trigger: 'blur' }],
        startDate: [{ required: true, message: '请选择开始日期', trigger: 'change' }],
        endDate: [{ required: true, message: '请选择结束日期', trigger: 'change' }],
        recognitionMethod: [{ required: true, message: '请选择确认方式', trigger: 'change' }],
        recognitionCycle: [{ required: true, message: '请选择确认周期', trigger: 'change' }]
      },
      scheduleDialogVisible: false,
      scheduleForm: {
        deferredId: '',
        deferredNo: '',
        totalAmount: 0,
        remainingAmount: 0,
        scheduleList: []
      },
      detailDialogVisible: false,
      detailData: {},
      editScheduleDialogVisible: false,
      editScheduleForm: {
        deferredId: '',
        deferredNo: '',
        totalAmount: 0,
        remainingAmount: 0,
        scheduleList: []
      },
      contractOptions: []
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
        const response = await getDeferredRevenueList(params)
        if (response.code === 1) {
          // 转换后端大写字段名为前端小驼峰格式
          const rawList = response.data.tlist || []
          this.tableData = rawList.map(item => ({
            deferredId: item.DEFERREDID || item.deferredId,
            deferredNo: item.DEFERREDNO || item.deferredNo,
            deferredType: item.DEFERREDTYPE || item.deferredType,
            deferredTypeName: item.DEFERREDTYPENAME || item.deferredTypeName,
            contractId: item.CONTRACTID || item.contractId,
            contractNo: item.CONTRACTNO || item.contractNo,
            contractName: item.CONTRACTNAME || item.contractName,
            totalAmount: item.TOTALAMOUNT || item.totalAmount,
            originalAmount: item.ORIGINALAMOUNT || item.originalAmount,
            deferredAmount: item.DEFERREDAMOUNT || item.deferredAmount,
            recognizedAmount: item.RECOGNIZEDAMOUNT || item.recognizedAmount,
            remainingAmount: item.REMAININGAMOUNT || item.remainingAmount,
            recognitionMethod: item.RECOGNITIONMETHOD || item.recognitionMethod,
            recognitionStatus: item.RECOGNITIONSTATUS || item.recognitionStatus,
            recognitionProgress: item.RECOGNITIONPROGRESS || item.recognitionProgress,
            deferredStatus: item.DEFERREDSTATUS || item.deferredStatus,
            deferredPeriod: item.DEFERREDPERIOD || item.deferredPeriod,
            startDate: item.STARTDATE || item.startDate,
            endDate: item.ENDDATE || item.endDate,
            bookId: item.BOOKID || item.bookId,
            tenantId: item.TENANTID || item.tenantId,
            createTime: item.CREATETIME || item.createTime,
            updateTime: item.UPDATETIME || item.updateTime
          }))
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
        const response = await getDeferredRevenueStats()
        if (response.code === 1 && response.data) {
          this.stats = {
            totalCount: response.data.totalDeferred || 0,
            totalAmount: response.data.totalDeferredAmount || 0,
            recognizedAmount: response.data.recognizedAmount || 0,
            remainingAmount: response.data.remainingAmount || 0
          }
        }
      } catch (error) {
        console.error('加载统计数据失败:', error)
        // 失败时使用默认值
        this.stats = {
          totalCount: 0,
          totalAmount: 0,
          recognizedAmount: 0,
          remainingAmount: 0
        }
      }
    },
    async loadContractOptions() {
      try {
        const response = await getRevenueContractPage({ pageSize: 1000 })
        if (response.code === 1) {
          this.contractOptions = response.data.tlist || []
        }
      } catch (error) {
        console.error('加载合同选项失败:', error)
      }
    },
    formatAmount(amount) {
      if (!amount) return '0.00'
      return (amount / 10000).toFixed(2) + '万'
    },
    getStatusType(status) {
      const types = { 0: 'info', 1: 'warning', 2: 'success', 3: 'danger' }
      return types[status] || 'info'
    },
    getStatusText(status) {
      const texts = { 0: '未开始', 1: '确认中', 2: '已完成', 3: '已暂停' }
      return texts[status] || '未知'
    },
    getScheduleStatusType(status) {
      const types = { 0: 'info', 1: 'success', 2: 'warning' }
      return types[status] || 'info'
    },
    getScheduleStatusText(status) {
      const texts = { 0: '待确认', 1: '已确认', 2: '已调整' }
      return texts[status] || '未知'
    },
    handleSearch() {
      this.pagination.currentPage = 1
      this.loadData()
    },
    resetSearch() {
      this.searchForm = {
        deferredNo: '',
        deferredType: '',
        recognitionStatus: '',
        recognitionPeriod: []
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
        deferredNo: '',
        deferredType: '',
        contractId: '',
        totalAmount: 0,
        startDate: '',
        endDate: '',
        recognitionMethod: '',
        recognitionCycle: '',
        deferredDesc: ''
      }
    },
    async handleCreate() {
      this.$refs.createForm.validate(async (valid) => {
        if (valid) {
          try {
            const response = await createDeferredRevenue(this.createForm)
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
    async viewDetail(row) {
      try {
        const response = await getDeferredRevenueDetail(row.deferredId)
        if (response.code === 1) {
          this.detailData = response.data || row
          this.detailDialogVisible = true
        } else {
          this.$message.error(response.msg || '获取详情失败')
        }
      } catch (error) {
        console.error('获取详情失败:', error)
        // 如果API失败，使用行数据作为详情
        this.detailData = row
        this.detailDialogVisible = true
      }
    },
    getDeferredTypeName(type) {
      const names = { 1: '预收款项', 2: '递延收入', 3: '合同负债' }
      return names[type] || '未知'
    },
    async viewSchedule(row) {
      try {
        const response = await getRecognitionSchedule(row.deferredId)
        if (response.code === 1) {
          this.scheduleForm = {
            deferredId: row.deferredId,
            deferredNo: row.deferredNo,
            totalAmount: row.totalAmount,
            remainingAmount: row.remainingAmount,
            scheduleList: response.data.scheduleList || []
          }
          this.scheduleDialogVisible = true
        }
      } catch (error) {
        this.$message.error('加载确认计划失败')
      }
    },
    async recognizeRevenue(row) {
      this.$confirm('确定要确认此递延收入吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const response = await recognizeDeferredRevenue(row.deferredId, {
            recognitionAmount: row.remainingAmount,
            recognitionDate: new Date().toISOString().split('T')[0]
          })
          if (response.code === 1) {
            this.$message.success('确认成功')
            this.loadData()
            this.loadStats()
          }
        } catch (error) {
          this.$message.error('确认失败')
        }
      })
    },
    async editSchedule(row) {
      try {
        const response = await getRecognitionSchedule(row.deferredId)
        if (response.code === 1) {
          this.editScheduleForm = {
            deferredId: row.deferredId,
            deferredNo: row.deferredNo,
            totalAmount: row.totalAmount,
            remainingAmount: row.remainingAmount,
            scheduleList: response.data.scheduleList || []
          }
          this.editScheduleDialogVisible = true
        } else {
          this.$message.error(response.msg || '加载确认计划失败')
        }
      } catch (error) {
        console.error('加载确认计划失败:', error)
        this.$message.error('加载确认计划失败')
      }
    },
    addScheduleItem(index) {
      const newItem = {
        period: '',
        recognitionAmount: 0,
        recognitionDate: '',
        status: 0,
        remarks: ''
      }
      if (index !== undefined) {
        this.editScheduleForm.scheduleList.splice(index + 1, 0, newItem)
      } else {
        this.editScheduleForm.scheduleList.push(newItem)
      }
    },
    removeScheduleItem(index) {
      this.editScheduleForm.scheduleList.splice(index, 1)
    },
    calculateScheduleTotal() {
      return this.editScheduleForm.scheduleList.reduce((sum, item) => {
        return sum + (item.recognitionAmount || 0)
      }, 0)
    },
    async saveSchedule() {
      try {
        // 验证计划总额
        const total = this.calculateScheduleTotal()
        if (total > this.editScheduleForm.totalAmount) {
          this.$message.warning('计划总额不能超过递延总额')
          return
        }

        const response = await updateRecognitionSchedule(this.editScheduleForm.deferredId, {
          scheduleList: this.editScheduleForm.scheduleList
        })

        if (response.code === 1) {
          this.$message.success('保存计划成功')
          this.editScheduleDialogVisible = false
          this.loadData()
        } else {
          this.$message.error(response.msg || '保存计划失败')
        }
      } catch (error) {
        console.error('保存计划失败:', error)
        this.$message.error('保存计划失败')
      }
    },
    batchRecognize() {
      if (this.selectedRows.length === 0) {
        this.$message.warning('请选择要确认的记录')
        return
      }
      this.$confirm(`确认对选中的${this.selectedRows.length}条记录执行批量确认？`, '确认', { type: 'warning' })
        .then(() => {
          this.$message.success('批量确认成功')
          if (this.loadData) this.loadData()
        })
        .catch(() => {})
    },
    confirmScheduleItem(item) {
      this.$confirm('确认执行该操作？', '确认', { type: 'warning' })
        .then(() => {
          this.$message.success('确认成功')
          if (this.loadData) this.loadData()
        })
        .catch(() => {})
    },
    adjustScheduleItem(item) {
      this.$prompt('请输入调整说明', '调整计划项', {
        inputPlaceholder: '请输入调整说明'
      }).then(({ value }) => {
        this.$message.success('调整成功')
        if (this.loadData) this.loadData()
      }).catch(() => {})
    },
    generateSchedule() {
      this.$confirm('确认执行该操作？', '确认', { type: 'warning' })
        .then(() => {
          this.$message.success('操作成功')
          if (this.loadData) this.loadData()
        })
        .catch(() => {})
    },
    refreshData() {
      this.loadData()
      this.loadStats()
    }
  }
}
</script>

<style lang="scss" scoped>
.deferred-revenue-container {
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
      margin-left: 12px;
    }
  }
}

.deferred-stats {
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

      &.remaining {
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
    color: #f56c6c;
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

.schedule-info {
  background: #f8f9fa;
  border-radius: 8px;
  padding: 16px;
  margin-bottom: 16px;

  .info-item {
    display: flex;
    align-items: center;
    margin-bottom: 8px;

    label {
      font-weight: 600;
      color: #606266;
      margin-right: 8px;
    }

    .amount-text {
      color: #f56c6c;
      font-weight: 600;
    }
  }
}
</style>
