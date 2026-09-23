<template>
  <div class="inventory-check-container">
    <!-- 搜索区域 -->
    <div class="search-container">
      <el-form :model="searchForm" ref="searchForm" :inline="true" class="search-form">
        <el-form-item label="盘点单号" prop="checkNumber">
          <el-input
            v-model="searchForm.checkNumber"
            placeholder="请输入盘点单号"
            clearable
            style="width: 180px"
          />
        </el-form-item>
        <el-form-item label="盘点类型" prop="checkType">
          <el-select
            v-model="searchForm.checkType"
            placeholder="请选择盘点类型"
            clearable
            style="width: 150px"
          >
            <el-option label="全盘" value="FULL" />
            <el-option label="抽盘" value="PARTIAL" />
            <el-option label="循环盘点" value="CYCLE" />
            <el-option label="动态盘点" value="DYNAMIC" />
          </el-select>
        </el-form-item>
        <el-form-item label="盘点状态" prop="status">
          <el-select
            v-model="searchForm.status"
            placeholder="请选择状态"
            clearable
            style="width: 120px"
          >
            <el-option label="计划中" value="PLANNING" />
            <el-option label="盘点中" value="CHECKING" />
            <el-option label="已完成" value="COMPLETED" />
            <el-option label="已审批" value="APPROVED" />
            <el-option label="已取消" value="CANCELLED" />
          </el-select>
        </el-form-item>
        <el-form-item label="仓库" prop="warehouseId">
          <el-select
            v-model="searchForm.warehouseId"
            placeholder="请选择仓库"
            clearable
            style="width: 150px"
          >
            <el-option
              v-for="warehouse in warehouseList"
              :key="warehouse.warehouseId"
              :label="warehouse.warehouseName"
              :value="warehouse.warehouseId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="盘点日期" prop="dateRange">
          <el-date-picker
            v-model="searchForm.dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            style="width: 240px"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 工具栏 -->
    <div class="toolbar">
      <el-button type="primary" @click="handleCreateCheck">创建盘点任务</el-button>
      <el-button type="success" @click="handleBatchApprove" :disabled="!multipleSelection.length">
        批量审批
      </el-button>
      <el-button type="warning" @click="handleExport">导出</el-button>
      <el-button type="danger" @click="handleBatchCancel" :disabled="!multipleSelection.length">
        批量取消
      </el-button>
    </div>

    <!-- 统计卡片 -->
    <div class="stats-cards">
      <el-row :gutter="20">
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon planning">
              <i class="el-icon-document"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ stats.planningCount }}</div>
              <div class="stat-label">计划中</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon checking">
              <i class="el-icon-view"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ stats.checkingCount }}</div>
              <div class="stat-label">盘点中</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon completed">
              <i class="el-icon-check"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ stats.completedCount }}</div>
              <div class="stat-label">已完成</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon variance">
              <i class="el-icon-warning"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ formatAmount(stats.totalVariance) }}</div>
              <div class="stat-label">盘点差异</div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 标签页 -->
    <div class="tabs-container">
      <el-tabs v-model="activeTab" @tab-click="handleTabClick">
        <!-- 盘点任务标签页 -->
        <el-tab-pane label="盘点任务" name="tasks">
          <div class="table-container">
            <el-table
              :data="tableData"
              v-loading="loading"
              @selection-change="handleSelectionChange"
              stripe
              border
            >
              <el-table-column type="selection" width="55" />
              <el-table-column prop="checkNumber" label="盘点单号" width="150" />
              <el-table-column prop="checkName" label="盘点名称" min-width="150" show-overflow-tooltip />
              <el-table-column prop="checkTypeName" label="盘点类型" width="100">
                <template slot-scope="scope">
                  <el-tag :type="getCheckTypeTagType(scope.row.checkType)">
                    {{ scope.row.checkTypeName }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="warehouseName" label="仓库" width="120" />
              <el-table-column prop="inventoryCount" label="存货数量" width="100" align="right" />
              <el-table-column prop="checkProgress" label="盘点进度" width="120">
                <template slot-scope="scope">
                  <el-progress
                    :percentage="scope.row.checkProgress"
                    :color="getProgressColor(scope.row.checkProgress)"
                    :stroke-width="8"
                  />
                </template>
              </el-table-column>
              <el-table-column prop="varianceAmount" label="盘点差异" width="120" align="right">
                <template slot-scope="scope">
                  <span v-if="scope.row.varianceAmount !== null" :class="scope.row.varianceAmount >= 0 ? 'positive-amount' : 'negative-amount'">
                    {{ formatAmount(scope.row.varianceAmount) }}
                  </span>
                  <span v-else>-</span>
                </template>
              </el-table-column>
              <el-table-column prop="status" label="状态" width="100">
                <template slot-scope="scope">
                  <el-tag :type="getStatusTagType(scope.row.status)">
                    {{ getStatusText(scope.row.status) }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="checkDate" label="盘点日期" width="120" />
              <el-table-column prop="operatorName" label="操作人" width="100" />
              <el-table-column label="操作" width="250" fixed="right">
                <template slot-scope="scope">
                  <el-button type="text" size="small" @click="handleView(scope.row)">
                    查看
                  </el-button>
                  <el-button
                    v-if="scope.row.status === 'PLANNING'"
                    type="text"
                    size="small"
                    @click="handleStartCheck(scope.row)"
                  >
                    开始盘点
                  </el-button>
                  <el-button
                    v-if="scope.row.status === 'CHECKING'"
                    type="text"
                    size="small"
                    @click="handleInputResult(scope.row)"
                  >
                    录入结果
                  </el-button>
                  <el-button
                    v-if="scope.row.status === 'COMPLETED'"
                    type="text"
                    size="small"
                    @click="handleApprove(scope.row)"
                  >
                    审批
                  </el-button>
                  <el-button
                    v-if="['PLANNING', 'CHECKING'].includes(scope.row.status)"
                    type="text"
                    size="small"
                    class="danger-text"
                    @click="handleCancel(scope.row)"
                  >
                    取消
                  </el-button>
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
        </el-tab-pane>

        <!-- 盘点结果标签页 -->
        <el-tab-pane label="盘点结果" name="results">
          <div class="results-container">
            <!-- 结果筛选条件 -->
            <div class="filter-bar">
              <el-form :inline="true" size="small">
                <el-form-item label="存货编码">
                  <el-input v-model="resultFilter.inventoryCode" placeholder="请输入存货编码" style="width: 150px" />
                </el-form-item>
                <el-form-item label="盘点结果">
                  <el-select v-model="resultFilter.varianceType" placeholder="全部" style="width: 120px">
                    <el-option label="全部" value="" />
                    <el-option label="盘盈" value="SURPLUS" />
                    <el-option label="盘亏" value="SHORTAGE" />
                    <el-option label="正常" value="NORMAL" />
                  </el-select>
                </el-form-item>
                <el-form-item label="处理状态">
                  <el-select v-model="resultFilter.processStatus" placeholder="全部" style="width: 120px">
                    <el-option label="全部" value="" />
                    <el-option label="待处理" value="PENDING" />
                    <el-option label="已处理" value="HANDLED" />
                  </el-select>
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" @click="loadResultData">查询</el-button>
                  <el-button @click="resetResultFilter">重置</el-button>
                </el-form-item>
              </el-form>
            </div>

            <!-- 结果数据表格 -->
            <el-table
              :data="resultData"
              v-loading="resultLoading"
              border
              stripe
            >
              <el-table-column prop="inventoryCode" label="存货编码" width="120" />
              <el-table-column prop="inventoryName" label="存货名称" min-width="150" show-overflow-tooltip />
              <el-table-column prop="bookQuantity" label="账面数量" width="100" align="right" />
              <el-table-column prop="actualQuantity" label="实盘数量" width="100" align="right" />
              <el-table-column prop="varianceQuantity" label="差异数量" width="100" align="right">
                <template slot-scope="scope">
                  <span :class="getVarianceClass(scope.row.varianceQuantity)">
                    {{ scope.row.varianceQuantity }}
                  </span>
                </template>
              </el-table-column>
              <el-table-column prop="varianceType" label="盘点结果" width="100">
                <template slot-scope="scope">
                  <el-tag :type="getVarianceTypeTag(scope.row.varianceQuantity)" size="small">
                    {{ getVarianceTypeText(scope.row.varianceQuantity) }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="varianceAmount" label="差异金额" width="120" align="right">
                <template slot-scope="scope">
                  <span :class="['amount', getVarianceClass(scope.row.varianceAmount)]">
                    {{ formatAmount(scope.row.varianceAmount) }}
                  </span>
                </template>
              </el-table-column>
              <el-table-column prop="location" label="存放地点" width="120" />
              <el-table-column prop="checkDate" label="盘点日期" width="120" />
              <el-table-column prop="checkerName" label="盘点人" width="100" />
              <el-table-column prop="processStatus" label="处理状态" width="100">
                <template slot-scope="scope">
                  <el-tag :type="scope.row.processStatus === 'HANDLED' ? 'success' : 'warning'" size="small">
                    {{ scope.row.processStatus === 'HANDLED' ? '已处理' : '待处理' }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column label="操作" width="150" fixed="right">
                <template slot-scope="scope">
                  <el-button type="text" size="small" @click="handleViewResult(scope.row)">
                    查看
                  </el-button>
                  <el-button
                    v-if="scope.row.processStatus === 'PENDING' && scope.row.varianceQuantity !== 0"
                    type="text"
                    size="small"
                    @click="handleProcessDifference(scope.row)"
                  >
                    处理差异
                  </el-button>
                </template>
              </el-table-column>
            </el-table>

            <!-- 结果分页 -->
            <div class="pagination-container">
              <el-pagination
                @size-change="handleResultSizeChange"
                @current-change="handleResultCurrentChange"
                :current-page="resultPagination.currentPage"
                :page-sizes="[10, 20, 50, 100]"
                :page-size="resultPagination.pageSize"
                layout="total, sizes, prev, pager, next, jumper"
                :total="resultPagination.total"
              />
            </div>
          </div>
        </el-tab-pane>
      </el-tabs>
    </div>

    <!-- 创建盘点任务对话框 -->
    <CheckCreateDialog
      :visible.sync="createDialogVisible"
      @create="handleCreateConfirm"
    />

    <!-- 盘点详情对话框 -->
    <CheckDetailDialog
      :visible.sync="detailDialogVisible"
      :check-data="selectedCheck"
    />

    <!-- 录入盘点结果对话框 -->
    <CheckResultDialog
      :visible.sync="resultDialogVisible"
      :check-data="selectedCheck"
      @submit="handleResultSubmit"
    />

    <!-- 盘点审批对话框 -->
    <CheckApprovalDialog
      :visible.sync="approvalDialogVisible"
      :check-data="selectedCheck"
      @approve="handleApprovalConfirm"
    />

    <!-- 差异处理对话框 -->
    <CheckDifferenceDialog
      :visible.sync="differenceDialogVisible"
      :difference-data="selectedDifference"
      @success="handleDifferenceProcessed"
    />
  </div>
</template>

<script>
import {
  getInventoryCheckPage,
  createInventoryCheck,
  executeInventoryCheck,
  submitInventoryCheckResult,
  approveInventoryCheck,
  getWarehouseList,
  getInventoryCheckStatistics,
  batchExecuteInventoryCheck,
  exportInventoryCheck,
  getInventoryCheckResults
} from '@/api/financialSharing/inventory'
import CheckCreateDialog from './components/CheckCreateDialog'
import CheckDetailDialog from './components/CheckDetailDialog'
import CheckResultDialog from './components/CheckResultDialog'
import CheckApprovalDialog from './components/CheckApprovalDialog'
import CheckDifferenceDialog from './components/CheckDifferenceDialog'

export default {
  name: 'InventoryCheck',
  components: {
    CheckCreateDialog,
    CheckDetailDialog,
    CheckResultDialog,
    CheckApprovalDialog,
    CheckDifferenceDialog
  },
  data() {
    return {
      loading: false,
      tableData: [],
      multipleSelection: [],
      warehouseList: [],
      activeTab: 'tasks',
      searchForm: {
        checkNumber: '',
        checkType: '',
        status: '',
        warehouseId: '',
        dateRange: []
      },
      pagination: {
        currentPage: 1,
        pageSize: 15,
        total: 0
      },
      stats: {
        planningCount: 0,
        checkingCount: 0,
        completedCount: 0,
        totalVariance: 0
      },
      // 盘点结果相关
      resultLoading: false,
      resultData: [],
      resultFilter: {
        inventoryCode: '',
        varianceType: '',
        processStatus: ''
      },
      resultPagination: {
        currentPage: 1,
        pageSize: 20,
        total: 0
      },
      // 对话框
      createDialogVisible: false,
      detailDialogVisible: false,
      resultDialogVisible: false,
      approvalDialogVisible: false,
      differenceDialogVisible: false,
      selectedCheck: {},
      selectedDifference: {}
    }
  },
  mounted() {
    this.loadData()
    this.loadStats()
    this.loadWarehouseList()
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
        if (this.searchForm.dateRange && this.searchForm.dateRange.length === 2) {
          params.startDate = this.searchForm.dateRange[0]
          params.endDate = this.searchForm.dateRange[1]
        }
        const response = await getInventoryCheckPage(params)
        if (response.code === 1) {
          // 转换大写字段名为驼峰命名
          const list = (response.data.tlist || []).map(item => ({
            checkId: item.CHECKID || item.checkId,
            checkNumber: item.CHECKNUMBER || item.checkNumber,
            checkName: item.CHECKNAME || item.checkName,
            checkType: item.CHECKTYPE || item.checkType,
            checkTypeName: item.CHECKTYPENAME || item.checkTypeName,
            warehouseId: item.WAREHOUSEID || item.warehouseId,
            warehouseName: item.WAREHOUSENAME || item.warehouseName,
            inventoryCount: item.INVENTORYCOUNT || item.inventoryCount,
            checkProgress: item.CHECKPROGRESS || item.checkProgress,
            varianceAmount: item.VARIANCEAMOUNT || item.varianceAmount,
            status: item.STATUS || item.status,
            checkDate: item.CHECKDATE || item.checkDate,
            operatorName: item.OPERATORNAME || item.operatorName,
            description: item.DESCRIPTION || item.description,
            createTime: item.CREATETIME || item.createTime
          }))
          this.tableData = list
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

    async loadStats() {
      try {
        const response = await getInventoryCheckStatistics({})
        if (response.code === 1 && response.data) {
          this.stats = {
            planningCount: response.data.planningCount || 0,
            checkingCount: response.data.checkingCount || 0,
            completedCount: response.data.completedCount || 0,
            totalVariance: response.data.totalVariance || 0
          }
        }
      } catch (error) {
        console.error('加载统计数据失败：', error)
        // 使用默认值
        this.stats = {
          planningCount: 0,
          checkingCount: 0,
          completedCount: 0,
          totalVariance: 0
        }
      }
    },

    async loadWarehouseList() {
      try {
        const response = await getWarehouseList()
        if (response.code === 1) {
          this.warehouseList = response.data || []
        }
      } catch (error) {
        console.error('加载仓库列表失败：', error)
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

    handleCreateCheck() {
      this.createDialogVisible = true
    },

    async handleCreateConfirm(checkData) {
      try {
        const response = await createInventoryCheck(checkData)
        if (response.code === 1) {
          this.$message.success('盘点任务创建成功')
          this.createDialogVisible = false
          this.loadData()
          this.loadStats()
        } else {
          this.$message.error(response.msg || '创建失败')
        }
      } catch (error) {
        this.$message.error('创建失败：' + error.message)
      }
    },

    handleView(row) {
      this.selectedCheck = { ...row }
      this.detailDialogVisible = true
    },

    async handleStartCheck(row) {
      try {
        await this.$confirm('确定要开始这个盘点任务吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        const response = await executeInventoryCheck(row.checkId)
        if (response.code === 1) {
          this.$message.success('盘点任务已开始')
          this.loadData()
          this.loadStats()
        } else {
          this.$message.error(response.msg || '开始失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('开始失败：' + error.message)
        }
      }
    },

    handleInputResult(row) {
      this.selectedCheck = { ...row }
      this.resultDialogVisible = true
    },

    async handleResultSubmit(resultData) {
      try {
        const response = await submitInventoryCheckResult(this.selectedCheck.checkId, resultData)
        if (response.code === 1) {
          this.$message.success('盘点结果提交成功')
          this.resultDialogVisible = false
          this.loadData()
          this.loadStats()
        } else {
          this.$message.error(response.msg || '提交失败')
        }
      } catch (error) {
        this.$message.error('提交失败：' + error.message)
      }
    },

    handleApprove(row) {
      this.selectedCheck = { ...row }
      this.approvalDialogVisible = true
    },

    async handleApprovalConfirm(approvalData) {
      try {
        const response = await approveInventoryCheck(this.selectedCheck.checkId, approvalData)
        if (response.code === 1) {
          this.$message.success('审批完成')
          this.approvalDialogVisible = false
          this.loadData()
          this.loadStats()
        } else {
          this.$message.error(response.msg || '审批失败')
        }
      } catch (error) {
        this.$message.error('审批失败：' + error.message)
      }
    },

    handleCancel(row) {
      this.$confirm('确定要取消这个盘点任务吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          // TODO: 调用取消盘点API
          this.$message.success('取消成功')
          this.loadData()
          this.loadStats()
        } catch (error) {
          this.$message.error('取消失败：' + error.message)
        }
      }).catch(() => {
        // 用户取消
      })
    },

    async handleBatchApprove() {
      if (this.multipleSelection.length === 0) {
        this.$message.warning('请先选择要审批的盘点任务')
        return
      }

      const completedItems = this.multipleSelection.filter(item => item.status === 'COMPLETED')
      if (completedItems.length === 0) {
        this.$message.warning('所选任务中没有可审批的任务（状态必须为已完成）')
        return
      }

      this.$confirm(`确定要批量审批 ${completedItems.length} 个盘点任务吗？`, '批量审批', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const checkIds = completedItems.map(item => item.checkId)
          // TODO: 调用批量审批API
          this.$message.success('批量审批成功')
          this.loadData()
          this.loadStats()
        } catch (error) {
          this.$message.error('批量审批失败：' + error.message)
        }
      }).catch(() => {
        // 用户取消
      })
    },

    async handleBatchCancel() {
      if (this.multipleSelection.length === 0) {
        this.$message.warning('请先选择要取消的盘点任务')
        return
      }

      const cancelableItems = this.multipleSelection.filter(item =>
        ['PLANNING', 'CHECKING'].includes(item.status)
      )
      if (cancelableItems.length === 0) {
        this.$message.warning('所选任务中没有可取消的任务（状态必须为计划中或盘点中）')
        return
      }

      this.$confirm(`确定要批量取消 ${cancelableItems.length} 个盘点任务吗？`, '批量取消', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const checkIds = cancelableItems.map(item => item.checkId)
          // TODO: 调用批量取消API
          this.$message.success('批量取消成功')
          this.loadData()
          this.loadStats()
        } catch (error) {
          this.$message.error('批量取消失败：' + error.message)
        }
      }).catch(() => {
        // 用户取消
      })
    },

    async handleExport() {
      try {
        const params = {
          ...this.searchForm
        }
        if (this.searchForm.dateRange && this.searchForm.dateRange.length === 2) {
          params.startDate = this.searchForm.dateRange[0]
          params.endDate = this.searchForm.dateRange[1]
        }

        const response = await exportInventoryCheck(params)
        if (response.code === 1) {
          this.$message.success('导出成功')
          // TODO: 处理文件下载
        } else {
          this.$message.error(response.msg || '导出失败')
        }
      } catch (error) {
        this.$message.error('导出失败：' + error.message)
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

    getCheckTypeTagType(type) {
      const typeMap = {
        'FULL': 'primary',
        'PARTIAL': 'success',
        'CYCLE': 'warning',
        'DYNAMIC': 'info'
      }
      return typeMap[type] || 'default'
    },

    getStatusTagType(status) {
      const typeMap = {
        'PLANNING': 'info',
        'CHECKING': 'warning',
        'COMPLETED': 'success',
        'APPROVED': 'primary',
        'CANCELLED': 'danger'
      }
      return typeMap[status] || 'default'
    },

    getStatusText(status) {
      const textMap = {
        'PLANNING': '计划中',
        'CHECKING': '盘点中',
        'COMPLETED': '已完成',
        'APPROVED': '已审批',
        'CANCELLED': '已取消'
      }
      return textMap[status] || status
    },

    getProgressColor(percentage) {
      if (percentage < 30) return '#f56c6c'
      if (percentage < 70) return '#e6a23c'
      return '#67c23a'
    },

    formatAmount(amount) {
      return amount ? '¥' + Number(amount).toLocaleString('zh-CN', { minimumFractionDigits: 2 }) : '¥0.00'
    },

    // 标签页切换
    handleTabClick(tab) {
      if (tab.name === 'results') {
        this.loadResultData()
      }
    },

    // 加载盘点结果数据
    async loadResultData() {
      this.resultLoading = true
      try {
        const params = {
          pageNumber: this.resultPagination.currentPage,
          pageSize: this.resultPagination.pageSize,
          ...this.resultFilter
        }

        // 获取所有已完成的盘点任务的结果
        const response = await getInventoryCheckResults('all', params)
        if (response.code === 1) {
          this.resultData = response.data.tlist || []
          this.resultPagination.total = response.data.totalRecord || 0
        } else {
          this.$message.error(response.msg || '查询失败')
        }
      } catch (error) {
        this.$message.error('查询失败：' + error.message)
      } finally {
        this.resultLoading = false
      }
    },

    // 重置结果筛选条件
    resetResultFilter() {
      this.resultFilter = {
        inventoryCode: '',
        varianceType: '',
        processStatus: ''
      }
      this.loadResultData()
    },

    // 查看盘点结果详情
    handleViewResult(row) {
      const content = `<p><b>存货编码：</b>${row.inventoryCode || '-'}</p><p><b>存货名称：</b>${row.inventoryName || '-'}</p><p><b>账面数量：</b>${row.bookQuantity || 0}</p><p><b>实盘数量：</b>${row.actualQuantity || 0}</p><p><b>差异数量：</b>${row.varianceQuantity || 0}</p><p><b>差异金额：</b>${row.varianceAmount || 0}</p><p><b>处理状态：</b>${row.processStatusName || row.processStatus || '-'}</p>`
      this.$alert(content, '盘点结果详情', { dangerouslyUseHTMLString: true })
    },

    // 处理盘点差异
    handleProcessDifference(row) {
      this.selectedDifference = { ...row }
      this.differenceDialogVisible = true
    },

    // 差异处理完成
    handleDifferenceProcessed() {
      this.loadResultData()
      this.$message.success('差异处理完成')
    },

    // 结果分页
    handleResultSizeChange(size) {
      this.resultPagination.pageSize = size
      this.loadResultData()
    },

    handleResultCurrentChange(page) {
      this.resultPagination.currentPage = page
      this.loadResultData()
    },

    // 差异相关辅助方法
    getVarianceClass(variance) {
      if (variance > 0) return 'surplus'
      if (variance < 0) return 'shortage'
      return ''
    },

    getVarianceTypeTag(variance) {
      if (variance > 0) return 'success'
      if (variance < 0) return 'danger'
      return 'info'
    },

    getVarianceTypeText(variance) {
      if (variance > 0) return '盘盈'
      if (variance < 0) return '盘亏'
      return '正常'
    }
  }
}
</script>

<style lang="scss" scoped>
.inventory-check-container {
  padding: 20px;
}

.search-container {
  background: white;
  padding: 20px;
  border-radius: 4px;
  margin-bottom: 20px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.toolbar {
  margin-bottom: 20px;
}

.stats-cards {
  margin-bottom: 20px;

  .stat-card {
    background: white;
    border-radius: 8px;
    padding: 20px;
    display: flex;
    align-items: center;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);

    .stat-icon {
      width: 50px;
      height: 50px;
      border-radius: 8px;
      display: flex;
      align-items: center;
      justify-content: center;
      margin-right: 15px;

      i {
        font-size: 24px;
        color: white;
      }

      &.planning {
        background: linear-gradient(135deg, #909399 0%, #606266 100%);
      }

      &.checking {
        background: linear-gradient(135deg, #ffa726 0%, #ff7043 100%);
      }

      &.completed {
        background: linear-gradient(135deg, #66bb6a 0%, #43a047 100%);
      }

      &.variance {
        background: linear-gradient(135deg, #ef5350 0%, #e53935 100%);
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

.tabs-container {
  background: white;
  padding: 20px;
  border-radius: 4px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.table-container {
  .pagination-container {
    margin-top: 20px;
    text-align: right;
  }
}

.results-container {
  .filter-bar {
    margin-bottom: 15px;
    padding: 15px;
    background: #f5f7fa;
    border-radius: 4px;
  }

  .pagination-container {
    margin-top: 20px;
    text-align: right;
  }
}

.positive-amount {
  color: #67c23a;
  font-weight: 600;
}

.negative-amount {
  color: #f56c6c;
  font-weight: 600;
}

.danger-text {
  color: #f56c6c;
}

.amount {
  font-weight: 600;
}

.surplus {
  color: #67c23a;
  font-weight: 600;
}

.shortage {
  color: #f56c6c;
  font-weight: 600;
}
</style>
