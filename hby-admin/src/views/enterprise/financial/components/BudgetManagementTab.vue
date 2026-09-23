<template>
  <div class="budget-management-tab">
    <!-- 统计概览 -->
    <el-row :gutter="20" class="mb-20">
      <el-col :span="6">
        <el-card class="statistics-card">
          <div class="statistics-content">
            <div class="statistics-icon total">
              <i class="el-icon-money"></i>
            </div>
            <div class="statistics-info">
              <div class="statistics-number">{{ budgetStatistics.totalBudgets || 0 }}</div>
              <div class="statistics-label">预算总数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="statistics-card">
          <div class="statistics-content">
            <div class="statistics-icon compilation">
              <i class="el-icon-edit-outline"></i>
            </div>
            <div class="statistics-info">
              <div class="statistics-number">{{ budgetStatistics.compilationBudgets || 0 }}</div>
              <div class="statistics-label">编制中预算</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="statistics-card">
          <div class="statistics-content">
            <div class="statistics-icon execution">
              <i class="el-icon-loading"></i>
            </div>
            <div class="statistics-info">
              <div class="statistics-number">{{ budgetStatistics.executionBudgets || 0 }}</div>
              <div class="statistics-label">执行中预算</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="statistics-card">
          <div class="statistics-content">
            <div class="statistics-icon amount">
              <i class="el-icon-coin"></i>
            </div>
            <div class="statistics-info">
              <div class="statistics-number">{{ formatAmount(budgetStatistics.totalAmount) }}</div>
              <div class="statistics-label">预算总额(万元)</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 查询表单 -->
    <el-card class="mb-20">
      <el-form :model="queryForm" ref="queryForm" :inline="true" label-width="100px">
        <el-form-item label="预算类型" prop="budgetType">
          <el-select v-model="queryForm.budgetType" placeholder="请选择预算类型" clearable style="width: 150px;">
            <el-option label="年度预算" value="年度预算"></el-option>
            <el-option label="季度预算" value="季度预算"></el-option>
            <el-option label="月度预算" value="月度预算"></el-option>
            <el-option label="专项预算" value="专项预算"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="预算状态" prop="budgetStatus">
          <el-select v-model="queryForm.budgetStatus" placeholder="请选择预算状态" clearable style="width: 150px;">
            <el-option label="待编制" value="待编制"></el-option>
            <el-option label="编制中" value="编制中"></el-option>
            <el-option label="执行中" value="执行中"></el-option>
            <el-option label="已完成" value="已完成"></el-option>
            <el-option label="已暂停" value="已暂停"></el-option>
            <el-option label="已取消" value="已取消"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleQuery" icon="el-icon-search">查询</el-button>
          <el-button @click="handleReset" icon="el-icon-refresh">重置</el-button>
          <el-button type="success" @click="handleAdd" icon="el-icon-plus">新增预算</el-button>
          <el-button type="warning" @click="handleBatchOperation" icon="el-icon-setting">批量操作</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 数据表格 -->
    <el-table
      v-loading="loading"
      :data="tableData"
      @selection-change="handleSelectionChange"
      @sort-change="handleSortChange"
      stripe
      border
      style="width: 100%"
      empty-text="暂无预算数据"
    >
      <el-table-column type="selection" width="55" align="center"></el-table-column>
      <el-table-column prop="budgetType" label="预算类型" width="120" align="center">
        <template slot-scope="scope">
          <el-tag :type="getBudgetTypeTag(scope.row.budgetType)">
            {{ getBudgetTypeText(scope.row.budgetType) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="budgetName" label="预算名称" min-width="200" show-overflow-tooltip></el-table-column>
      <el-table-column prop="budgetYear" label="预算年度" width="100" align="center"></el-table-column>
      <el-table-column prop="budgetAmount" label="预算金额" width="120" align="right">
        <template slot-scope="scope">
          <span class="amount-text">{{ formatAmount(scope.row.budgetAmount) }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="executedAmount" label="已执行" width="120" align="right">
        <template slot-scope="scope">
          <span class="amount-text">{{ formatAmount(scope.row.executedAmount) }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="executionRate" label="执行率" width="120" align="center">
        <template slot-scope="scope">
          <el-progress
            :percentage="scope.row.executionRate"
            :color="getExecutionColor(scope.row.executionRate)"
            :show-text="false"
            style="width: 80px;"
          ></el-progress>
          <span style="margin-left: 5px;">{{ scope.row.executionRate }}%</span>
        </template>
      </el-table-column>
      <el-table-column prop="budgetStatus" label="预算状态" width="100" align="center">
        <template slot-scope="scope">
          <el-tag :type="getBudgetStatusTag(scope.row.budgetStatus)">
            {{ getBudgetStatusText(scope.row.budgetStatus) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createPerson" label="创建人" width="100" align="center"></el-table-column>
      <el-table-column prop="createTime" label="创建时间" width="160" align="center"></el-table-column>
      <el-table-column label="操作" width="280" align="center" fixed="right">
        <template slot-scope="scope">
          <el-button size="mini" @click="handleView(scope.row)" icon="el-icon-view">查看</el-button>
          <el-button size="mini" type="primary" @click="handleEdit(scope.row)" icon="el-icon-edit" v-if="scope.row.budgetStatus === '待编制' || scope.row.budgetStatus === '编制中' || scope.row.budgetStatus === 'DRAFTING'">编辑</el-button>
          <el-button size="mini" type="success" @click="handleExecute(scope.row)" icon="el-icon-video-play" v-if="scope.row.budgetStatus === '已编制' || scope.row.budgetStatus === 'APPROVED'">执行</el-button>
          <el-dropdown @command="handleCommand" style="margin-left: 10px;">
            <el-button size="mini" type="info">
              更多<i class="el-icon-arrow-down el-icon--right"></i>
            </el-button>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item :command="{action: 'analysis', row: scope.row}" icon="el-icon-data-analysis">执行分析</el-dropdown-item>
              <el-dropdown-item :command="{action: 'adjust', row: scope.row}" icon="el-icon-edit" v-if="scope.row.budgetStatus === '执行中' || scope.row.budgetStatus === 'EXECUTING'">预算调整</el-dropdown-item>
              <el-dropdown-item :command="{action: 'report', row: scope.row}" icon="el-icon-document">执行报告</el-dropdown-item>
              <el-dropdown-item :command="{action: 'copy', row: scope.row}" icon="el-icon-document-copy">复制预算</el-dropdown-item>
              <el-dropdown-item :command="{action: 'history', row: scope.row}" icon="el-icon-time">变更历史</el-dropdown-item>
              <el-dropdown-item :command="{action: 'delete', row: scope.row}" icon="el-icon-delete" divided v-if="scope.row.budgetStatus === '待编制' || scope.row.budgetStatus === 'DRAFTING'">删除</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页组件 -->
    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryForm.pageNumber"
      :limit.sync="queryForm.pageSize"
      @pagination="getList"
    />

    <!-- 预算对话框 -->
    <BudgetDialog
      :visible.sync="budgetDialogVisible"
      :form-data="currentRow"
      :dialog-type="dialogType"
      :enterprise-id="enterpriseId"
      @refresh="getList"
    />

    <!-- 预算详情对话框 -->
    <BudgetDetailDialog
      :visible.sync="detailDialogVisible"
      :budget-id="currentRow.budgetId"
    />

    <!-- 预算分析对话框 -->
    <BudgetAnalysisDialog
      :visible.sync="analysisDialogVisible"
      :budget-data="currentRow"
    />

    <!-- 预算调整对话框 -->
    <BudgetAdjustDialog
      :visible.sync="adjustDialogVisible"
      :budget-data="currentRow"
      @refresh="getList"
    />

    <!-- 执行报告对话框 -->
    <el-dialog title="预算执行报告" :visible.sync="reportDialogVisible" width="650px" append-to-body>
      <el-descriptions :column="2" border size="medium">
        <el-descriptions-item label="预算名称" :span="2">{{ currentRow.budgetName }}</el-descriptions-item>
        <el-descriptions-item label="预算类型">{{ getBudgetTypeText(currentRow.budgetType) }}</el-descriptions-item>
        <el-descriptions-item label="预算年度">{{ currentRow.budgetYear }}</el-descriptions-item>
        <el-descriptions-item label="预算总额">{{ formatAmount(currentRow.budgetAmount) }}</el-descriptions-item>
        <el-descriptions-item label="已执行金额">{{ formatAmount(currentRow.executedAmount) }}</el-descriptions-item>
        <el-descriptions-item label="剩余金额">{{ formatAmount((currentRow.budgetAmount || 0) - (currentRow.executedAmount || 0)) }}</el-descriptions-item>
        <el-descriptions-item label="预算状态">
          <el-tag :type="getBudgetStatusTag(currentRow.budgetStatus)" size="small">{{ getBudgetStatusText(currentRow.budgetStatus) }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="执行率" :span="2">
          <el-progress
            :percentage="Number(currentRow.executionRate) || 0"
            :color="getExecutionColor(currentRow.executionRate)"
            :stroke-width="18"
            :text-inside="true"
            style="width: 100%;"
          ></el-progress>
        </el-descriptions-item>
        <el-descriptions-item label="创建人">{{ currentRow.createPerson }}</el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ currentRow.createTime }}</el-descriptions-item>
      </el-descriptions>
      <div style="margin-top: 20px;">
        <h4 style="margin-bottom: 10px;">预算执行对比</h4>
        <el-table :data="reportCompareData" border size="small" style="width: 100%;">
          <el-table-column prop="item" label="项目" width="150"></el-table-column>
          <el-table-column prop="budgetValue" label="预算金额" align="right"></el-table-column>
          <el-table-column prop="actualValue" label="实际金额" align="right"></el-table-column>
          <el-table-column prop="deviation" label="偏差" align="right">
            <template slot-scope="scope">
              <span :style="{ color: scope.row.deviationRate > 0 ? '#F56C6C' : '#67C23A' }">{{ scope.row.deviation }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="deviationRateText" label="偏差率" align="center">
            <template slot-scope="scope">
              <span :style="{ color: scope.row.deviationRate > 0 ? '#F56C6C' : '#67C23A' }">{{ scope.row.deviationRateText }}</span>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <span slot="footer">
        <el-button @click="reportDialogVisible = false">关闭</el-button>
      </span>
    </el-dialog>

    <!-- 变更历史对话框 -->
    <el-dialog title="预算变更历史" :visible.sync="historyDialogVisible" width="550px" append-to-body>
      <div style="margin-bottom: 15px;">
        <span style="font-weight: bold;">{{ currentRow.budgetName }}</span>
        <el-tag :type="getBudgetStatusTag(currentRow.budgetStatus)" size="small" style="margin-left: 10px;">{{ getBudgetStatusText(currentRow.budgetStatus) }}</el-tag>
      </div>
      <el-timeline>
        <el-timeline-item
          v-for="(item, index) in historyList"
          :key="index"
          :timestamp="item.timestamp"
          :type="item.type"
          :icon="item.icon"
          placement="top"
        >
          <el-card shadow="never" class="history-card">
            <h4>{{ item.title }}</h4>
            <p style="color: #909399; margin-top: 5px;">{{ item.description }}</p>
          </el-card>
        </el-timeline-item>
      </el-timeline>
      <div v-if="historyList.length === 0" style="text-align: center; color: #909399; padding: 30px 0;">
        暂无变更记录
      </div>
      <span slot="footer">
        <el-button @click="historyDialogVisible = false">关闭</el-button>
      </span>
    </el-dialog>

    <!-- 批量操作对话框 -->
    <el-dialog title="批量操作" :visible.sync="batchDialogVisible" width="420px" append-to-body>
      <p style="margin-bottom: 15px;">已选择 <strong>{{ multipleSelection.length }}</strong> 条预算记录，请选择操作：</p>
      <el-button type="danger" icon="el-icon-delete" @click="handleBatchDelete" style="width: 100%; margin-bottom: 10px;">批量删除</el-button>
      <el-button type="primary" icon="el-icon-video-play" @click="handleBatchSubmit" style="width: 100%; margin-left: 0;">批量提交执行</el-button>
      <span slot="footer">
        <el-button @click="batchDialogVisible = false">取消</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import {
  getBudgetList,
  deleteBudget,
  executeBudget,
  getBudgetAnalysis,
  getBudgetStatistics,
  getBudgetManagementStatistics,
  batchDeleteBudget,
  batchUpdateBudgetStatus
} from '@/api/enterprise/financial'
import Pagination from '@/components/Pagination'
import BudgetDialog from './BudgetDialog'
import BudgetDetailDialog from './BudgetDetailDialog'
import BudgetAnalysisDialog from './BudgetAnalysisDialog'
import BudgetAdjustDialog from './BudgetAdjustDialog'

export default {
  name: 'BudgetManagementTab',
  components: {
    Pagination,
    BudgetDialog,
    BudgetDetailDialog,
    BudgetAnalysisDialog,
    BudgetAdjustDialog
  },
  props: {
    enterpriseId: {
      type: String,
      default: ''
    },
    enterpriseName: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      loading: false,
      tableData: [],
      total: 0,
      multipleSelection: [],
      budgetStatistics: {},
      queryForm: {
        pageNumber: 1,
        pageSize: 10,
        budgetType: '',
        budgetStatus: '',
        budgetYear: null
      },
      
      // 对话框状态
      budgetDialogVisible: false,
      detailDialogVisible: false,
      analysisDialogVisible: false,
      adjustDialogVisible: false,
      reportDialogVisible: false,
      historyDialogVisible: false,
      batchDialogVisible: false,
      dialogType: 'add',
      currentRow: {},
      historyList: []
    }
  },
  watch: {
    enterpriseId: {
      handler(val) {
        if (val) {
          this.getList()
          this.loadBudgetStats()
        }
      },
      immediate: true
    }
  },
  computed: {
    // 执行报告对比数据
    reportCompareData() {
      const budget = this.currentRow.budgetAmount || 0
      const executed = this.currentRow.executedAmount || 0
      const remaining = budget - executed
      const deviationRate = budget > 0 ? ((executed - budget) / budget * 100).toFixed(1) : 0
      return [
        {
          item: '预算总额',
          budgetValue: this.formatAmount(budget),
          actualValue: this.formatAmount(executed),
          deviation: this.formatAmount(executed - budget),
          deviationRate: Number(deviationRate),
          deviationRateText: deviationRate + '%'
        },
        {
          item: '剩余预算',
          budgetValue: this.formatAmount(budget),
          actualValue: this.formatAmount(remaining),
          deviation: this.formatAmount(-remaining),
          deviationRate: remaining < 0 ? 1 : -1,
          deviationRateText: budget > 0 ? (remaining / budget * 100).toFixed(1) + '%' : '0%'
        }
      ]
    }
  },
  methods: {
    // 获取列表数据
    getList() {
      if (!this.enterpriseId) return

      this.loading = true
      const params = {
        ...this.queryForm,
        enterpriseId: this.enterpriseId
      }

      getBudgetList(params).then(response => {
        // 防御性处理：接口返回为空或无数据
        if (!response || !response.data) {
          this.tableData = []
          this.total = 0
          this.loading = false
          return
        }
        const data = response.data || {}
        const rawList = data.tlist || data.records || []
        // 后端BudgetManagement实体字段映射
        this.tableData = rawList.map(item => ({
          ...item,
          // 兼容BudgetManagement实体字段: totalBudgetAmount→budgetAmount, createBy→createPerson
          budgetAmount: item.totalBudgetAmount || item.budgetAmount || 0,
          executedAmount: item.executedAmount || item.actualAmount || 0,
          executionRate: item.executionRate || (item.budgetAmount && item.budgetAmount > 0 ? ((item.executedAmount || item.actualAmount || 0) / item.budgetAmount * 100).toFixed(1) : 0),
          budgetStatus: item.budgetStatus || item.status || '',
          createPerson: item.createBy || item.createPerson || ''
        }))
        this.total = data.totalRecord || data.total || 0
        this.loading = false
      }).catch((error) => {
        this.tableData = []
        this.total = 0
        this.loading = false
        console.error('查询预算列表失败:', error)
        this.$message.error('查询预算列表失败，请检查网络连接')
      })
    },

    // 加载预算统计
    loadBudgetStats() {
      // 使用BudgetManagementController的统计接口(GZCT_BUDGET_MANAGEMENT表)
      getBudgetManagementStatistics(this.enterpriseId).then(response => {
        const data = response.data || {}
        // 兼容达梦数据库返回大写key
        this.budgetStatistics = {
          totalBudgets: data.totalBudgets || data.TOTALBUDGETS || 0,
          compilationBudgets: data.compilationBudgets || data.COMPILATIONBUDGETS || 0,
          executionBudgets: data.executionBudgets || data.EXECUTIONBUDGETS || 0,
          totalAmount: data.totalAmount || data.TOTALAMOUNT || 0
        }
      }).catch(() => {
        // 备用: 使用EnterpriseFinancialController的统计接口
        getBudgetStatistics({ enterpriseId: this.enterpriseId }).then(response => {
          const data = response.data || {}
          this.budgetStatistics = {
            totalBudgets: data.totalBudgets || data.TOTALBUDGETS || 0,
            compilationBudgets: data.compilationBudgets || data.COMPILATIONBUDGETS || 0,
            executionBudgets: data.executionBudgets || data.EXECUTIONBUDGETS || 0,
            totalAmount: data.totalAmount || data.TOTALAMOUNT || 0
          }
        }).catch(() => {
          this.budgetStatistics = {}
        })
      })
    },

    // 查询
    handleQuery() {
      this.queryForm.pageNumber = 1
      this.getList()
    },

    // 重置查询
    handleReset() {
      this.$refs.queryForm.resetFields()
      this.queryForm = {
        pageNumber: 1,
        pageSize: 10,
        budgetType: '',
        budgetStatus: '',
        budgetYear: null
      }
      this.getList()
    },

    // 新建预算
    handleAdd() {
      this.currentRow = {}
      this.dialogType = 'add'
      this.budgetDialogVisible = true
    },

    // 预算分析
    handleAnalysis() {
      this.analysisDialogVisible = true
    },

    // 查看
    handleView(row) {
      this.currentRow = { ...row }
      this.detailDialogVisible = true
    },

    // 编辑
    handleEdit(row) {
      this.currentRow = { ...row }
      this.dialogType = 'edit'
      this.budgetDialogVisible = true
    },

    // 执行预算
    async handleExecute(row) {
      this.$confirm('确认开始执行该预算？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          await executeBudget(row.budgetId)
          this.$message.success('预算执行启动成功')
          this.getList()
          this.$emit('refresh')
        } catch (error) {
          this.$message.error('预算执行启动失败')
        }
      })
    },

    // 下拉菜单命令处理
    handleCommand(command) {
      const { action, row } = command
      this.currentRow = { ...row }
      
      switch (action) {
        case 'analysis':
          this.analysisDialogVisible = true
          break
        case 'adjust':
          this.adjustDialogVisible = true
          break
        case 'report':
          this.reportDialogVisible = true
          break
        case 'copy':
          this.handleCopy(row)
          break
        case 'history':
          this.buildHistoryList(row)
          this.historyDialogVisible = true
          break
        case 'delete':
          this.handleDelete(row)
          break
      }
    },

    // 复制预算
    handleCopy(row) {
      this.currentRow = { ...row, budgetId: '', budgetStatus: 'DRAFTING' }
      this.dialogType = 'add'
      this.budgetDialogVisible = true
    },

    // 删除
    handleDelete(row) {
      this.$confirm('确认删除该预算？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          await deleteBudget(row.budgetId || row.id)
          this.$message.success('删除成功')
          this.getList()
          this.loadBudgetStats()
          this.$emit('refresh')
        } catch (error) {
          this.$message.error('删除失败')
        }
      })
    },

    // 多选变化
    handleSelectionChange(selection) {
      this.multipleSelection = selection
    },

    // 批量操作
    handleBatchOperation() {
      if (!this.multipleSelection || this.multipleSelection.length === 0) {
        this.$message.warning('请先选择需要操作的预算记录')
        return
      }
      this.batchDialogVisible = true
    },

    // 批量删除
    handleBatchDelete() {
      const deletableItems = this.multipleSelection.filter(item => item.budgetStatus === 'DRAFTING')
      if (deletableItems.length === 0) {
        this.$message.warning('所选预算中没有可删除的记录（仅编制中状态可删除）')
        return
      }
      this.$confirm(`确认删除选中的 ${deletableItems.length} 条预算记录？`, '批量删除确认', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        this.batchDialogVisible = false
        this.loading = true
        try {
          const budgetIds = deletableItems.map(item => item.budgetId || item.id)
          const updateBy = this.$store.getters.name || 'system'
          await batchDeleteBudget(budgetIds, updateBy)
          this.$message.success(`批量删除成功，共删除 ${deletableItems.length} 条记录`)
          this.getList()
          this.loadBudgetStats()
          this.$emit('refresh')
        } catch (error) {
          console.error('批量删除失败:', error)
          this.$message.error('批量删除失败，请稍后重试')
        } finally {
          this.loading = false
        }
      })
    },

    // 批量提交执行
    handleBatchSubmit() {
      const submittableItems = this.multipleSelection.filter(item => item.budgetStatus === 'APPROVED')
      if (submittableItems.length === 0) {
        this.$message.warning('所选预算中没有可执行的记录（仅已批准状态可执行）')
        return
      }
      this.$confirm(`确认提交执行选中的 ${submittableItems.length} 条预算？`, '批量执行确认', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        this.batchDialogVisible = false
        this.loading = true
        try {
          const budgetIds = submittableItems.map(item => item.budgetId || item.id)
          const updateBy = this.$store.getters.name || 'system'
          await batchUpdateBudgetStatus(budgetIds, 'EXECUTING', updateBy)
          this.$message.success(`批量执行成功，共提交 ${submittableItems.length} 条预算`)
          this.getList()
          this.loadBudgetStats()
          this.$emit('refresh')
        } catch (error) {
          console.error('批量执行失败:', error)
          this.$message.error('批量执行失败，请稍后重试')
        } finally {
          this.loading = false
        }
      })
    },

    // 构建变更历史列表
    buildHistoryList(row) {
      const list = []
      // 创建记录
      if (row.createTime) {
        list.push({
          timestamp: row.createTime,
          type: 'primary',
          icon: 'el-icon-plus',
          title: '预算创建',
          description: `由 ${row.createPerson || '系统'} 创建预算，预算金额：${this.formatAmount(row.budgetAmount)}`
        })
      }
      // 根据状态推断历史节点
      const status = row.budgetStatus
      if (status === 'PENDING' || status === 'APPROVED' || status === 'EXECUTING' || status === 'COMPLETED') {
        list.push({
          timestamp: row.updateTime || row.createTime,
          type: 'warning',
          icon: 'el-icon-s-check',
          title: '预算提交审批',
          description: '预算编制完成，提交审批流程'
        })
      }
      if (status === 'APPROVED' || status === 'EXECUTING' || status === 'COMPLETED') {
        list.push({
          timestamp: row.approvalTime || row.updateTime || row.createTime,
          type: 'success',
          icon: 'el-icon-circle-check',
          title: '审批通过',
          description: '预算审批通过，可开始执行'
        })
      }
      if (status === 'EXECUTING' || status === 'COMPLETED') {
        list.push({
          timestamp: row.executionStartTime || row.updateTime || row.createTime,
          type: 'primary',
          icon: 'el-icon-video-play',
          title: '开始执行',
          description: `预算进入执行阶段，当前执行率：${row.executionRate || 0}%`
        })
      }
      if (status === 'COMPLETED') {
        list.push({
          timestamp: row.completionTime || row.updateTime,
          type: 'success',
          icon: 'el-icon-success',
          title: '执行完成',
          description: `预算执行完毕，最终执行率：${row.executionRate || 0}%`
        })
      }
      this.historyList = list
    },

    // 排序变化
    handleSortChange({ column, prop, order }) {
      this.queryForm.orderBy = prop
      this.queryForm.orderDirection = order === 'ascending' ? 'ASC' : 'DESC'
      this.getList()
    },

    // 格式化金额
    formatAmount(amount) {
      if (!amount) return '0'
      return (amount / 10000).toFixed(2) + '万'
    },

    // 获取执行率颜色
    getExecutionColor(rate) {
      if (rate >= 90) return '#67C23A'
      if (rate >= 70) return '#E6A23C'
      return '#F56C6C'
    },

    // 获取预算类型标签
    getBudgetTypeTag(type) {
      const tagMap = {
        '年度预算': 'primary',
        '季度预算': 'success',
        '月度预算': 'warning',
        '专项预算': 'info',
        'ANNUAL': 'primary',
        'QUARTERLY': 'success',
        'MONTHLY': 'warning',
        'SPECIAL': 'info',
        'REVENUE': 'success',
        'EXPENSE': 'warning',
        'INVESTMENT': 'primary',
        'CASH_FLOW': 'info'
      }
      return tagMap[type] || 'info'
    },

    // 获取预算类型文本
    getBudgetTypeText(type) {
      const textMap = {
        '年度预算': '年度预算',
        '季度预算': '季度预算',
        '月度预算': '月度预算',
        '专项预算': '专项预算',
        'ANNUAL': '年度预算',
        'QUARTERLY': '季度预算',
        'MONTHLY': '月度预算',
        'SPECIAL': '专项预算',
        'REVENUE': '收入预算',
        'EXPENSE': '支出预算',
        'INVESTMENT': '投资预算',
        'CASH_FLOW': '现金流预算'
      }
      return textMap[type] || type
    },

    // 获取预算状态标签
    getBudgetStatusTag(status) {
      const tagMap = {
        '待编制': 'info',
        '编制中': 'warning',
        '执行中': 'primary',
        '已完成': 'success',
        '已暂停': 'danger',
        '已取消': 'danger',
        'DRAFTING': 'info',
        'PENDING': 'warning',
        'APPROVED': 'primary',
        'EXECUTING': 'success',
        'COMPLETED': 'success'
      }
      return tagMap[status] || 'info'
    },

    // 获取预算状态文本
    getBudgetStatusText(status) {
      const textMap = {
        '待编制': '待编制',
        '编制中': '编制中',
        '执行中': '执行中',
        '已完成': '已完成',
        '已暂停': '已暂停',
        '已取消': '已取消',
        'DRAFTING': '编制中',
        'PENDING': '待审批',
        'APPROVED': '已批准',
        'EXECUTING': '执行中',
        'COMPLETED': '已完成'
      }
      return textMap[status] || status
    }
  }
}
</script>

<style scoped>
.budget-management-tab {
  padding: 20px 0;
}

.mb-20 {
  margin-bottom: 20px;
}

.budget-card {
  height: 100px;
}

.budget-content {
  display: flex;
  align-items: center;
  height: 100%;
}

.budget-icon {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 20px;
  color: white;
  margin-right: 15px;
}

.budget-icon.total {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.budget-icon.executed {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
}

.budget-icon.remaining {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
}

.budget-icon.rate {
  background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
}

.budget-info {
  flex: 1;
}

.budget-amount {
  font-size: 18px;
  font-weight: bold;
  color: #303133;
  line-height: 1;
  margin-bottom: 5px;
}

.budget-label {
  font-size: 12px;
  color: #909399;
}

.amount-text {
  color: #303133;
  font-weight: 500;
}

.history-card {
  padding: 0;
}
.history-card h4 {
  margin: 0;
  font-size: 14px;
  color: #303133;
}
.history-card p {
  margin: 0;
  font-size: 12px;
}
</style>
