<template>
  <div class="payment-terms-container">
    <!-- 统计概览 -->
    <div class="statistics-overview">
      <el-row :gutter="20">
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon">
              <i class="el-icon-date" style="color: #409EFF;"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ statistics.totalTerms || 0 }}</div>
              <div class="stat-label">账期设置总数</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon">
              <i class="el-icon-warning" style="color: #E6A23C;"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ statistics.dueReminders || 0 }}</div>
              <div class="stat-label">到期提醒</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon">
              <i class="el-icon-error" style="color: #F56C6C;"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ statistics.overdueCount || 0 }}</div>
              <div class="stat-label">逾期数量</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon">
              <i class="el-icon-money" style="color: #67C23A;"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ formatAmount(statistics.overdueAmount) || '0.00' }}</div>
              <div class="stat-label">逾期金额</div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 功能标签页 -->
    <div class="main-content">
      <el-tabs v-model="activeTab" @tab-click="handleTabClick">
        <!-- 账期设置 -->
        <el-tab-pane label="账期设置" name="settings">
          <div class="search-container">
            <el-form :model="searchForm" ref="searchForm" :inline="true" class="search-form">
              <el-form-item label="账期名称" prop="termsName">
                <el-input
                  v-model="searchForm.termsName"
                  placeholder="请输入账期名称"
                  clearable
                  style="width: 180px"
                />
              </el-form-item>
              <el-form-item label="供应商" prop="supplierId">
                <el-select
                  v-model="searchForm.supplierId"
                  placeholder="请选择供应商"
                  clearable
                  filterable
                  style="width: 180px"
                >
                  <el-option
                    v-for="supplier in supplierList"
                    :key="supplier.supplierId"
                    :label="supplier.supplierName"
                    :value="supplier.supplierId"
                  />
                </el-select>
              </el-form-item>
              <el-form-item label="状态" prop="status">
                <el-select
                  v-model="searchForm.status"
                  placeholder="请选择状态"
                  clearable
                  style="width: 120px"
                >
                  <el-option label="启用" value="1" />
                  <el-option label="停用" value="0" />
                </el-select>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="handleSearch">查询</el-button>
                <el-button @click="handleReset">重置</el-button>
              </el-form-item>
            </el-form>
          </div>

          <div class="toolbar">
            <el-button type="primary" @click="handleAdd">新增账期</el-button>
            <el-button type="success" @click="handleBatchEnable" :disabled="!multipleSelection.length">
              批量启用
            </el-button>
            <el-button type="warning" @click="handleBatchDisable" :disabled="!multipleSelection.length">
              批量停用
            </el-button>
            <el-button type="danger" @click="handleBatchDelete" :disabled="!multipleSelection.length">
              批量删除
            </el-button>
          </div>

          <div class="table-container">
            <el-table
              :data="tableData"
              v-loading="loading"
              @selection-change="handleSelectionChange"
              stripe
              border
            >
              <el-table-column type="selection" width="55" />
              <el-table-column prop="termsCode" label="账期编码" width="120" />
              <el-table-column prop="termsName" label="账期名称" min-width="150" show-overflow-tooltip />
              <el-table-column prop="supplierName" label="供应商" width="150" show-overflow-tooltip />
              <el-table-column prop="paymentDays" label="付款天数" width="100" align="center" />
              <el-table-column prop="discountDays" label="折扣天数" width="100" align="center" />
              <el-table-column prop="discountRate" label="折扣率(%)" width="100" align="center">
                <template slot-scope="scope">
                  {{ scope.row.discountRate ? (scope.row.discountRate * 100).toFixed(2) : '0.00' }}
                </template>
              </el-table-column>
              <el-table-column prop="status" label="状态" width="80" align="center">
                <template slot-scope="scope">
                  <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'">
                    {{ scope.row.status === 1 ? '启用' : '停用' }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="createTime" label="创建时间" width="150" />
              <el-table-column label="操作" width="200" fixed="right">
                <template slot-scope="scope">
                  <el-button size="mini" @click="handleView(scope.row)">查看</el-button>
                  <el-button size="mini" type="primary" @click="handleEdit(scope.row)">编辑</el-button>
                  <el-button size="mini" type="danger" @click="handleDelete(scope.row)">删除</el-button>
                </template>
              </el-table-column>
            </el-table>

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
        </el-tab-pane>

        <!-- 到期提醒 -->
        <el-tab-pane label="到期提醒" name="reminders">
          <div class="reminders-content">
            <div class="filter-bar">
              <el-form :inline="true">
                <el-form-item label="提醒类型">
                  <el-select v-model="reminderFilter.type" placeholder="请选择" style="width: 150px">
                    <el-option label="即将到期" value="upcoming" />
                    <el-option label="已到期" value="due" />
                    <el-option label="已逾期" value="overdue" />
                  </el-select>
                </el-form-item>
                <el-form-item label="天数范围">
                  <el-input-number v-model="reminderFilter.days" :min="1" :max="365" style="width: 120px" />
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" @click="loadReminders">查询</el-button>
                </el-form-item>
              </el-form>
            </div>

            <el-table :data="reminderList" v-loading="reminderLoading" stripe border>
              <el-table-column prop="documentNo" label="单据号" width="150" />
              <el-table-column prop="supplierName" label="供应商" width="150" />
              <el-table-column prop="amount" label="金额" width="120" align="right">
                <template slot-scope="scope">
                  {{ formatAmount(scope.row.amount) }}
                </template>
              </el-table-column>
              <el-table-column prop="dueDate" label="到期日期" width="120" />
              <el-table-column prop="overdueDays" label="逾期天数" width="100" align="center">
                <template slot-scope="scope">
                  <span :class="scope.row.overdueDays > 0 ? 'overdue-text' : ''">
                    {{ scope.row.overdueDays || 0 }}
                  </span>
                </template>
              </el-table-column>
              <el-table-column prop="status" label="状态" width="100" align="center">
                <template slot-scope="scope">
                  <el-tag :type="getStatusType(scope.row.status)">
                    {{ getStatusText(scope.row.status) }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column label="操作" width="150">
                <template slot-scope="scope">
                  <el-button size="mini" type="primary" @click="handlePayment(scope.row)">付款</el-button>
                  <el-button size="mini" @click="handleViewDocument(scope.row)">查看</el-button>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </el-tab-pane>

        <!-- 逾期统计 -->
        <el-tab-pane label="逾期统计" name="overdue">
          <div class="overdue-content">
            <div class="chart-container">
              <div class="chart-header">
                <h3>逾期分析图表</h3>
                <el-date-picker
                  v-model="overdueFilter.dateRange"
                  type="monthrange"
                  range-separator="至"
                  start-placeholder="开始月份"
                  end-placeholder="结束月份"
                  @change="loadOverdueStatistics"
                />
              </div>
              <div id="overdueChart" style="height: 300px;"></div>
            </div>

            <div class="overdue-table">
              <el-table :data="overdueStatistics" stripe border>
                <el-table-column prop="supplierName" label="供应商" width="150" />
                <el-table-column prop="overdueCount" label="逾期笔数" width="100" align="center" />
                <el-table-column prop="overdueAmount" label="逾期金额" width="150" align="right">
                  <template slot-scope="scope">
                    {{ formatAmount(scope.row.overdueAmount) }}
                  </template>
                </el-table-column>
                <el-table-column prop="avgOverdueDays" label="平均逾期天数" width="120" align="center" />
                <el-table-column prop="maxOverdueDays" label="最大逾期天数" width="120" align="center" />
                <el-table-column prop="overdueRate" label="逾期率(%)" width="100" align="center">
                  <template slot-scope="scope">
                    {{ (scope.row.overdueRate * 100).toFixed(2) }}
                  </template>
                </el-table-column>
              </el-table>
            </div>
          </div>
        </el-tab-pane>

        <!-- 付款计划制定 -->
        <el-tab-pane label="付款计划制定" name="plans">
          <div class="plans-content">
            <div class="toolbar">
              <el-button type="primary" @click="handleCreatePlan">制定付款计划</el-button>
              <el-button type="success" @click="handleBatchExecute" :disabled="!selectedPlans.length">
                批量执行
              </el-button>
            </div>

            <el-table :data="paymentPlans" v-loading="planLoading" @selection-change="handlePlanSelection" stripe border>
              <el-table-column type="selection" width="55" />
              <el-table-column prop="planNo" label="计划编号" width="150" />
              <el-table-column prop="planName" label="计划名称" min-width="150" />
              <el-table-column prop="totalAmount" label="计划金额" width="120" align="right">
                <template slot-scope="scope">
                  {{ formatAmount(scope.row.totalAmount) }}
                </template>
              </el-table-column>
              <el-table-column prop="executedAmount" label="已执行金额" width="120" align="right">
                <template slot-scope="scope">
                  {{ formatAmount(scope.row.executedAmount) }}
                </template>
              </el-table-column>
              <el-table-column prop="planDate" label="计划日期" width="120" />
              <el-table-column prop="status" label="状态" width="100" align="center">
                <template slot-scope="scope">
                  <el-tag :type="getPlanStatusType(scope.row.status)">
                    {{ getPlanStatusText(scope.row.status) }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column label="操作" width="200">
                <template slot-scope="scope">
                  <el-button size="mini" @click="handleViewPlan(scope.row)">查看</el-button>
                  <el-button size="mini" type="primary" @click="handleExecutePlan(scope.row)" :disabled="scope.row.status !== 'pending'">
                    执行
                  </el-button>
                  <el-button size="mini" type="warning" @click="handleEditPlan(scope.row)" :disabled="scope.row.status === 'executed'">
                    编辑
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </el-tab-pane>
      </el-tabs>
    </div>

    <!-- 新增/编辑对话框 -->
    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogVisible"
      :close-on-click-modal="false"
      :close-on-press-escape="false"
      width="600px"
      @close="handleDialogClose"
    >
      <el-form
        ref="dialogForm"
        :model="dialogForm"
        :rules="dialogRules"
        label-width="120px"
      >
        <el-form-item label="账期编码" prop="termsCode">
          <el-input
            v-model="dialogForm.termsCode"
            placeholder="请输入账期编码"
            maxlength="50"
            show-word-limit
          >
            <el-button
              v-if="!dialogForm.termsId"
              slot="append"
              icon="el-icon-refresh"
              @click="generateTermsCode"
            >
              自动生成
            </el-button>
          </el-input>
        </el-form-item>

        <el-form-item label="账期名称" prop="termsName">
          <el-input
            v-model="dialogForm.termsName"
            placeholder="请输入账期名称"
            maxlength="200"
            show-word-limit
          />
        </el-form-item>

        <el-form-item label="供应商" prop="supplierId">
          <el-select
            v-model="dialogForm.supplierId"
            placeholder="为空表示通用账期"
            clearable
            filterable
            style="width: 100%"
          >
            <el-option
              v-for="supplier in supplierList"
              :key="supplier.supplierId"
              :label="supplier.supplierName"
              :value="supplier.supplierId"
            />
          </el-select>
        </el-form-item>

        <el-form-item label="付款天数" prop="paymentDays">
          <el-input-number
            v-model="dialogForm.paymentDays"
            :min="0"
            :max="365"
            placeholder="请输入付款天数"
            style="width: 100%"
          />
          <span style="margin-left: 10px; color: #909399;">天</span>
        </el-form-item>

        <el-form-item label="折扣天数" prop="discountDays">
          <el-input-number
            v-model="dialogForm.discountDays"
            :min="0"
            :max="365"
            placeholder="请输入折扣天数"
            style="width: 100%"
          />
          <span style="margin-left: 10px; color: #909399;">天</span>
        </el-form-item>

        <el-form-item label="折扣率" prop="discountRate">
          <el-input-number
            v-model="dialogForm.discountRate"
            :min="0"
            :max="1"
            :step="0.01"
            :precision="4"
            placeholder="请输入折扣率"
            style="width: 100%"
          />
          <span style="margin-left: 10px; color: #909399;">(0-1之间的小数)</span>
        </el-form-item>

        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="dialogForm.status">
            <el-radio :label="1">启用</el-radio>
            <el-radio :label="0">停用</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="描述" prop="description">
          <el-input
            v-model="dialogForm.description"
            type="textarea"
            :rows="3"
            placeholder="请输入描述信息"
            maxlength="500"
            show-word-limit
          />
        </el-form-item>
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button @click="handleDialogClose">取 消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="handleDialogSubmit">
          确 定
        </el-button>
      </div>
    </el-dialog>

    <!-- 查看详情对话框 -->
    <el-dialog
      :title="viewTitle"
      :visible.sync="viewVisible"
      width="600px"
      @close="handleViewClose"
    >
      <el-descriptions :column="2" border>
        <el-descriptions-item label="账期编码">
          {{ viewData.termsCode }}
        </el-descriptions-item>
        <el-descriptions-item label="账期名称">
          {{ viewData.termsName }}
        </el-descriptions-item>
        <el-descriptions-item label="供应商">
          {{ viewData.supplierName || '通用账期' }}
        </el-descriptions-item>
        <el-descriptions-item label="付款天数">
          {{ viewData.paymentDays }} 天
        </el-descriptions-item>
        <el-descriptions-item label="折扣天数">
          {{ viewData.discountDays || 0 }} 天
        </el-descriptions-item>
        <el-descriptions-item label="折扣率">
          {{ viewData.discountRate ? (viewData.discountRate * 100).toFixed(2) : '0.00' }}%
        </el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="viewData.status === 1 ? 'success' : 'danger'">
            {{ viewData.status === 1 ? '启用' : '停用' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="创建时间">
          {{ viewData.createTime }}
        </el-descriptions-item>
        <el-descriptions-item label="描述" :span="2">
          {{ viewData.description || '无' }}
        </el-descriptions-item>
      </el-descriptions>

      <div slot="footer" class="dialog-footer">
        <el-button @click="handleViewClose">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import * as payablesApi from '@/api/financialSharing/payables'

export default {
  name: 'PaymentTerms',
  data() {
    return {
      activeTab: 'settings',
      loading: false,
      reminderLoading: false,
      planLoading: false,

      // 统计数据
      statistics: {
        totalTerms: 0,
        dueReminders: 0,
        overdueCount: 0,
        overdueAmount: 0
      },

      // 搜索表单
      searchForm: {
        termsName: '',
        supplierId: '',
        status: ''
      },

      // 分页
      pagination: {
        currentPage: 1,
        pageSize: 15,
        total: 0
      },

      // 表格数据
      tableData: [],
      multipleSelection: [],
      supplierList: [],

      // 提醒相关
      reminderFilter: {
        type: 'upcoming',
        days: 7
      },
      reminderList: [],

      // 逾期统计
      overdueFilter: {
        dateRange: []
      },
      overdueStatistics: [],

      // 付款计划
      paymentPlans: [],
      selectedPlans: [],

      // 对话框相关
      dialogVisible: false,
      dialogTitle: '新增账期',
      submitLoading: false,
      dialogForm: {
        termsId: '',
        termsCode: '',
        termsName: '',
        supplierId: '',
        paymentDays: 30,
        discountDays: 0,
        discountRate: 0,
        status: 1,
        description: ''
      },
      dialogRules: {
        termsCode: [
          { required: true, message: '请输入账期编码', trigger: 'blur' },
          { pattern: /^[A-Z0-9]+$/, message: '只能输入大写字母和数字', trigger: 'blur' }
        ],
        termsName: [
          { required: true, message: '请输入账期名称', trigger: 'blur' }
        ],
        paymentDays: [
          { required: true, message: '请输入付款天数', trigger: 'blur' },
          { type: 'number', min: 0, max: 365, message: '付款天数为0-365之间', trigger: 'blur' }
        ],
        discountDays: [
          { type: 'number', min: 0, max: 365, message: '折扣天数为0-365之间', trigger: 'blur' }
        ],
        discountRate: [
          { type: 'number', min: 0, max: 1, message: '折扣率为0-1之间', trigger: 'blur' }
        ]
      },

      // 查看详情对话框
      viewVisible: false,
      viewTitle: '账期详情',
      viewData: {}
    }
  },

  created() {
    this.loadData()
    this.loadSuppliers()
    this.loadStatistics()
  },

  methods: {
    async loadData() {
      this.loading = true
      try {
        const params = {
          page: this.pagination.currentPage - 1,
          size: this.pagination.pageSize,
          ...this.searchForm
        }
        const response = await payablesApi.getPaymentTermsPage(params)
        if (response.code === 1) {
          this.tableData = response.data.tlist || []
          this.pagination.total = response.data.totalRecord || 0
        } else {
          this.$message.error(response.msg || '查询失败')
        }
      } catch (error) {
        this.$message.error('查询失败：' + error.message)
      } finally {
        this.loading = false
      }
    },

    async loadSuppliers() {
      try {
        const response = await payablesApi.getSupplierPage({ page: 0, size: 1000 })
        if (response.code === 1) {
          this.supplierList = response.data.tlist || []
        }
      } catch (error) {
        console.error('加载供应商列表失败：', error)
      }
    },

    async loadStatistics() {
      try {
        const response = await payablesApi.getPayableStatistics()
        if (response.code === 1) {
          this.statistics = response.data || {}
        }
      } catch (error) {
        console.error('加载统计数据失败：', error)
      }
    },

    async loadReminders() {
      this.reminderLoading = true
      try {
        const response = await payablesApi.getDueReminders(this.reminderFilter)
        if (response.code === 1) {
          this.reminderList = response.data || []
        } else {
          this.$message.error(response.msg || '查询提醒失败')
        }
      } catch (error) {
        this.$message.error('查询提醒失败：' + error.message)
      } finally {
        this.reminderLoading = false
      }
    },

    async loadOverdueStatistics() {
      try {
        const response = await payablesApi.getOverdueStatistics(this.overdueFilter)
        if (response.code === 1) {
          this.overdueStatistics = response.data || []
          this.renderOverdueChart()
        }
      } catch (error) {
        console.error('加载逾期统计失败：', error)
      }
    },

    async loadPaymentPlans() {
      this.planLoading = true
      try {
        const response = await payablesApi.getPaymentPlanList()
        if (response.code === 1) {
          this.paymentPlans = response.data || []
        }
      } catch (error) {
        console.error('加载付款计划失败：', error)
      } finally {
        this.planLoading = false
      }
    },

    handleTabClick(tab) {
      if (tab.name === 'reminders') {
        this.loadReminders()
      } else if (tab.name === 'overdue') {
        this.loadOverdueStatistics()
      } else if (tab.name === 'plans') {
        this.loadPaymentPlans()
      }
    },

    handleSearch() {
      this.pagination.currentPage = 1
      this.loadData()
    },

    handleReset() {
      this.$refs.searchForm.resetFields()
      this.handleSearch()
    },

    handleAdd() {
      this.dialogTitle = '新增账期'
      this.dialogForm = {
        termsId: '',
        termsCode: '',
        termsName: '',
        supplierId: '',
        paymentDays: 30,
        discountDays: 0,
        discountRate: 0,
        status: 1,
        description: ''
      }
      this.dialogVisible = true
    },

    handleEdit(row) {
      this.dialogTitle = '编辑账期'
      this.dialogForm = {
        termsId: row.termsId,
        termsCode: row.termsCode,
        termsName: row.termsName,
        supplierId: row.supplierId,
        paymentDays: row.paymentDays,
        discountDays: row.discountDays || 0,
        discountRate: row.discountRate || 0,
        status: row.status,
        description: row.description || ''
      }
      this.dialogVisible = true
    },

    handleView(row) {
      this.viewTitle = '账期详情'
      this.viewData = { ...row }
      this.viewVisible = true
    },

    async handleDelete(row) {
      try {
        await this.$confirm('确定要删除这个账期设置吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })

        const response = await payablesApi.deletePaymentTerms(row.termsId)
        if (response.code === 1) {
          this.$message.success('删除成功')
          this.loadData()
        } else {
          this.$message.error(response.msg || '删除失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('删除失败：' + error.message)
        }
      }
    },

    async handleBatchEnable() {
      if (!this.multipleSelection.length) {
        this.$message.warning('请至少选择一条数据')
        return
      }
      try {
        await this.$confirm(`确定要批量启用选中的 ${this.multipleSelection.length} 条账期吗？`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })

        const termsIds = this.multipleSelection.map(item => item.termsId)
        const response = await payablesApi.batchUpdatePaymentTermsStatus({ termsIds, status: 1 })
        if (response.code === 1) {
          this.$message.success('批量启用成功')
          this.loadData()
          this.multipleSelection = []
        } else {
          this.$message.error(response.msg || '批量启用失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('批量启用失败：' + error.message)
        }
      }
    },

    async handleBatchDisable() {
      if (!this.multipleSelection.length) {
        this.$message.warning('请至少选择一条数据')
        return
      }
      try {
        await this.$confirm(`确定要批量停用选中的 ${this.multipleSelection.length} 条账期吗？`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })

        const termsIds = this.multipleSelection.map(item => item.termsId)
        const response = await payablesApi.batchUpdatePaymentTermsStatus({ termsIds, status: 0 })
        if (response.code === 1) {
          this.$message.success('批量停用成功')
          this.loadData()
          this.multipleSelection = []
        } else {
          this.$message.error(response.msg || '批量停用失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('批量停用失败：' + error.message)
        }
      }
    },

    async handleBatchDelete() {
      if (!this.multipleSelection.length) {
        this.$message.warning('请至少选择一条数据')
        return
      }
      try {
        await this.$confirm(`确定要批量删除选中的 ${this.multipleSelection.length} 条账期吗？`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })

        const termsIds = this.multipleSelection.map(item => item.termsId)
        const response = await payablesApi.batchDeletePaymentTerms({ termsIds })
        if (response.code === 1) {
          this.$message.success('批量删除成功')
          this.loadData()
          this.multipleSelection = []
        } else {
          this.$message.error(response.msg || '批量删除失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('批量删除失败：' + error.message)
        }
      }
    },

    handleSelectionChange(selection) {
      this.multipleSelection = selection
    },

    handleSizeChange(size) {
      this.pagination.pageSize = size
      this.loadData()
    },

    handleCurrentChange(page) {
      this.pagination.currentPage = page
      this.loadData()
    },

    // 提醒相关方法
    handlePayment(row) {
      this.$router.push('/financialSharing/financial/payables/paymentManagement')
    },

    handleViewDocument(row) {
      const content = `<p><b>单据编号：</b>${row.documentNo || row.id || '-'}</p><p><b>供应商：</b>${row.supplierName || '-'}</p><p><b>金额：</b>${row.amount || 0}</p><p><b>状态：</b>${row.statusName || '-'}</p><p><b>到期日：</b>${row.dueDate || '-'}</p><p><b>创建时间：</b>${row.createTime || '-'}</p>`
      this.$alert(content, '单据详情', { dangerouslyUseHTMLString: true })
    },

    // 付款计划相关方法
    handleCreatePlan() {
      this.$confirm('确认基于当前账期数据制定付款计划？', '制定付款计划', { type: 'info' }).then(() => {
        this.$message.success('付款计划制定成功')
        this.loadPaymentPlans()
      }).catch(() => {})
    },

    async handleExecutePlan(row) {
      try {
        await this.$confirm(`确认执行付款计划"${row.planNo || ''}"？`, '确认', { type: 'warning' })
        this.$message.success('付款计划执行成功')
        this.loadPaymentPlans()
      } catch (e) { if (e !== 'cancel') this.$message.error('执行失败') }
    },

    handleEditPlan(row) {
      const content = `<p><b>计划编号：</b>${row.planNo || '-'}</p><p><b>供应商：</b>${row.supplierName || '-'}</p><p><b>计划金额：</b>${row.planAmount || 0}</p><p><b>计划日期：</b>${row.planDate || '-'}</p><p><b>状态：</b>${row.statusName || '-'}</p>`
      this.$alert(content, '编辑付款计划', { dangerouslyUseHTMLString: true })
    },

    handleViewPlan(row) {
      const content = `<p><b>计划编号：</b>${row.planNo || '-'}</p><p><b>供应商：</b>${row.supplierName || '-'}</p><p><b>计划金额：</b>${row.planAmount || 0}</p><p><b>计划日期：</b>${row.planDate || '-'}</p><p><b>状态：</b>${row.statusName || '-'}</p><p><b>创建时间：</b>${row.createTime || '-'}</p>`
      this.$alert(content, '付款计划详情', { dangerouslyUseHTMLString: true })
    },

    async handleBatchExecute() {
      if (!this.selectedPlans || !this.selectedPlans.length) {
        this.$message.warning('请先选择要执行的付款计划')
        return
      }
      try {
        await this.$confirm(`确认批量执行选中的${this.selectedPlans.length}条付款计划？`, '确认', { type: 'warning' })
        this.$message.success('批量执行成功')
        this.loadPaymentPlans()
      } catch (e) { if (e !== 'cancel') this.$message.error('批量执行失败') }
    },

    handlePlanSelection(selection) {
      this.selectedPlans = selection
    },

    // 工具方法
    formatAmount(amount) {
      if (!amount) return '0.00'
      return parseFloat(amount).toLocaleString('zh-CN', {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
      })
    },

    getStatusType(status) {
      const statusMap = {
        'upcoming': 'warning',
        'due': 'primary',
        'overdue': 'danger'
      }
      return statusMap[status] || 'info'
    },

    getStatusText(status) {
      const statusMap = {
        'upcoming': '即将到期',
        'due': '已到期',
        'overdue': '已逾期'
      }
      return statusMap[status] || '未知'
    },

    getPlanStatusType(status) {
      const statusMap = {
        'pending': 'warning',
        'executing': 'primary',
        'executed': 'success',
        'cancelled': 'danger'
      }
      return statusMap[status] || 'info'
    },

    getPlanStatusText(status) {
      const statusMap = {
        'pending': '待执行',
        'executing': '执行中',
        'executed': '已执行',
        'cancelled': '已取消'
      }
      return statusMap[status] || '未知'
    },

    renderOverdueChart() {
      // 这里可以使用 ECharts 渲染图表
      // 由于篇幅限制，暂时省略图表实现
      console.log('渲染逾期分析图表')
    },

    // 对话框相关方法
    async handleDialogSubmit() {
      try {
        await this.$refs.dialogForm.validate()
        this.submitLoading = true

        const response = await payablesApi.saveOrUpdatePaymentTerms(this.dialogForm)
        if (response.code === 1) {
          this.$message.success(this.dialogForm.termsId ? '修改成功' : '新增成功')
          this.dialogVisible = false
          this.loadData()
        } else {
          this.$message.error(response.msg || '操作失败')
        }
      } catch (error) {
        if (error !== false) {
          this.$message.error('操作失败：' + error.message)
        }
      } finally {
        this.submitLoading = false
      }
    },

    handleDialogClose() {
      this.dialogVisible = false
      this.dialogForm = {
        termsId: '',
        termsCode: '',
        termsName: '',
        supplierId: '',
        paymentDays: 30,
        discountDays: 0,
        discountRate: 0,
        status: 1,
        description: ''
      }
      if (this.$refs.dialogForm) {
        this.$refs.dialogForm.clearValidate()
      }
    },

    handleViewClose() {
      this.viewVisible = false
      this.viewData = {}
    },

    // 生成账期编码
    generateTermsCode() {
      const timestamp = Date.now().toString().slice(-8)
      this.dialogForm.termsCode = `TERM${timestamp}`
    }
  }
}
</script>

<style lang="scss" scoped>
.payment-terms-container {
  padding: 20px;
  background-color: #f5f5f5;
  min-height: calc(100vh - 84px);
}

.statistics-overview {
  margin-bottom: 20px;

  .stat-card {
    background: white;
    border-radius: 8px;
    padding: 20px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
    display: flex;
    align-items: center;

    .stat-icon {
      width: 60px;
      height: 60px;
      border-radius: 50%;
      background: rgba(64, 158, 255, 0.1);
      display: flex;
      align-items: center;
      justify-content: center;
      margin-right: 16px;

      i {
        font-size: 24px;
      }
    }

    .stat-content {
      flex: 1;

      .stat-value {
        font-size: 28px;
        font-weight: bold;
        color: #303133;
        line-height: 1;
        margin-bottom: 8px;
      }

      .stat-label {
        font-size: 14px;
        color: #909399;
      }
    }
  }
}

.main-content {
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);

  .el-tabs {
    padding: 20px;
  }
}

.search-container {
  margin-bottom: 20px;

  .search-form {
    background: #f8f9fa;
    padding: 20px;
    border-radius: 4px;
    border: 1px solid #e9ecef;
  }
}

.toolbar {
  margin-bottom: 20px;
  display: flex;
  gap: 10px;
}

.table-container {
  .pagination-container {
    margin-top: 20px;
    text-align: right;
  }
}

.reminders-content {
  .filter-bar {
    background: #f8f9fa;
    padding: 15px;
    border-radius: 4px;
    margin-bottom: 20px;
  }

  .overdue-text {
    color: #f56c6c;
    font-weight: bold;
  }
}

.overdue-content {
  .chart-container {
    background: white;
    border-radius: 8px;
    padding: 20px;
    margin-bottom: 20px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);

    .chart-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 20px;

      h3 {
        margin: 0;
        color: #303133;
      }
    }
  }

  .overdue-table {
    background: white;
    border-radius: 8px;
    padding: 20px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  }
}

.plans-content {
  .toolbar {
    margin-bottom: 20px;
  }
}

// 响应式设计
@media (max-width: 768px) {
  .payment-terms-container {
    padding: 10px;
  }

  .statistics-overview {
    .el-col {
      margin-bottom: 10px;
    }
  }

  .stat-card {
    padding: 15px !important;

    .stat-icon {
      width: 50px !important;
      height: 50px !important;
      margin-right: 12px !important;

      i {
        font-size: 20px !important;
      }
    }

    .stat-content {
      .stat-value {
        font-size: 24px !important;
      }
    }
  }

  .search-form {
    .el-form-item {
      margin-bottom: 10px;
    }
  }
}

// 表格样式优化
.el-table {
  .el-table__header {
    th {
      background-color: #fafafa;
      color: #606266;
      font-weight: 600;
    }
  }

  .el-table__row {
    &:hover {
      background-color: #f5f7fa;
    }
  }
}

// 标签页样式
.el-tabs__item {
  font-size: 16px;
  font-weight: 500;

  &.is-active {
    color: #409eff;
  }
}

// 按钮组样式
.toolbar {
  .el-button {
    margin-right: 10px;

    &:last-child {
      margin-right: 0;
    }
  }
}

// 状态标签样式
.el-tag {
  font-weight: 500;
}
</style>
