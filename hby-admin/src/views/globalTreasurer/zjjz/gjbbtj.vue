<template>
  <div class="gjbbtj-container">
    <div class="page-header">
      <h2>国际报表统计</h2>
      <p>管理和统计各类国际财务报表数据</p>
    </div>

    <!-- 查询条件 -->
    <el-card class="search-card" shadow="never">
      <el-form :model="searchForm" :inline="true" size="small">
        <el-form-item label="报表类型">
          <el-select v-model="searchForm.reportType" placeholder="请选择报表类型" clearable>
            <el-option label="资产负债表" value="balance_sheet" />
            <el-option label="利润表" value="income_statement" />
            <el-option label="现金流量表" value="cash_flow" />
            <el-option label="所有者权益变动表" value="equity_change" />
          </el-select>
        </el-form-item>
        <el-form-item label="统计周期">
          <el-select v-model="searchForm.period" placeholder="请选择统计周期" clearable>
            <el-option label="月度" value="monthly" />
            <el-option label="季度" value="quarterly" />
            <el-option label="年度" value="yearly" />
          </el-select>
        </el-form-item>
        <el-form-item label="统计日期">
          <el-date-picker
            v-model="searchForm.dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            format="yyyy-MM-dd"
            value-format="yyyy-MM-dd"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleSearch">查询</el-button>
          <el-button icon="el-icon-refresh" @click="handleReset">重置</el-button>
          <el-button type="success" icon="el-icon-download" @click="handleExport">导出</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 统计图表 -->
    <el-row :gutter="20" class="chart-row">
      <el-col :span="12">
        <el-card title="报表数量统计" shadow="never">
          <div ref="reportCountChart" style="height: 300px;"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card title="报表趋势分析" shadow="never">
          <div ref="reportTrendChart" style="height: 300px;"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 数据表格 -->
    <el-card class="table-card" shadow="never">
      <div slot="header" class="card-header">
        <span>报表统计列表</span>
        <el-button type="primary" size="small" icon="el-icon-plus" @click="handleAdd">新增统计</el-button>
      </div>
      
      <el-table
        v-loading="loading"
        :data="tableData"
        border
        stripe
        style="width: 100%"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="reportName" label="报表名称" min-width="150" />
        <el-table-column prop="reportType" label="报表类型" width="120">
          <template slot-scope="scope">
            <el-tag :type="getReportTypeTag(scope.row.reportType)">
              {{ getReportTypeName(scope.row.reportType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="period" label="统计周期" width="100">
          <template slot-scope="scope">
            <span>{{ getPeriodName(scope.row.period) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="reportDate" label="报表日期" width="120" />
        <el-table-column prop="totalAmount" label="统计金额" width="150" align="right">
          <template slot-scope="scope">
            <span class="amount">{{ formatAmount(scope.row.totalAmount) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="getStatusTag(scope.row.status)">
              {{ getStatusName(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="160" />
        <el-table-column label="操作" width="200" fixed="right">
          <template slot-scope="scope">
            <el-button type="text" size="small" @click="handleView(scope.row)">查看</el-button>
            <el-button type="text" size="small" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button type="text" size="small" @click="handleAnalyze(scope.row)">分析</el-button>
            <el-button type="text" size="small" style="color: #f56c6c" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-container">
        <el-pagination
          :current-page="pagination.current"
          :page-sizes="[10, 20, 50, 100]"
          :page-size="pagination.size"
          :total="pagination.total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <!-- 新增/编辑对话框 -->
    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogVisible"
      width="800px"
      :close-on-click-modal="false"
    >
      <el-form
        ref="form"
        :model="form"
        :rules="rules"
        label-width="120px"
        size="small"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="报表名称" prop="reportName">
              <el-input v-model="form.reportName" placeholder="请输入报表名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="报表类型" prop="reportType">
              <el-select v-model="form.reportType" placeholder="请选择报表类型">
                <el-option label="资产负债表" value="balance_sheet" />
                <el-option label="利润表" value="income_statement" />
                <el-option label="现金流量表" value="cash_flow" />
                <el-option label="所有者权益变动表" value="equity_change" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="统计周期" prop="period">
              <el-select v-model="form.period" placeholder="请选择统计周期">
                <el-option label="月度" value="monthly" />
                <el-option label="季度" value="quarterly" />
                <el-option label="年度" value="yearly" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="报表日期" prop="reportDate">
              <el-date-picker
                v-model="form.reportDate"
                type="date"
                placeholder="选择报表日期"
                format="yyyy-MM-dd"
                value-format="yyyy-MM-dd"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="统计金额" prop="totalAmount">
          <el-input-number
            v-model="form.totalAmount"
            :precision="2"
            :step="1000"
            :min="0"
            controls-position="right"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="备注">
          <el-input
            v-model="form.remark"
            type="textarea"
            :rows="3"
            placeholder="请输入备注信息"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'GjbbtjManage',
  data() {
    return {
      loading: false,
      searchForm: {
        reportType: '',
        period: '',
        dateRange: []
      },
      tableData: [],
      pagination: {
        current: 1,
        size: 20,
        total: 0
      },
      dialogVisible: false,
      dialogTitle: '新增报表统计',
      form: {
        reportName: '',
        reportType: '',
        period: '',
        reportDate: '',
        totalAmount: 0,
        remark: ''
      },
      rules: {
        reportName: [
          { required: true, message: '请输入报表名称', trigger: 'blur' }
        ],
        reportType: [
          { required: true, message: '请选择报表类型', trigger: 'change' }
        ],
        period: [
          { required: true, message: '请选择统计周期', trigger: 'change' }
        ],
        reportDate: [
          { required: true, message: '请选择报表日期', trigger: 'change' }
        ],
        totalAmount: [
          { required: true, message: '请输入统计金额', trigger: 'blur' }
        ]
      }
    }
  },
  mounted() {
    this.loadData()
    this.initCharts()
  },
  methods: {
    loadData() {
      this.loading = true
      // 模拟数据
      setTimeout(() => {
        this.tableData = [
          {
            id: 1,
            reportName: '2024年第一季度资产负债表',
            reportType: 'balance_sheet',
            period: 'quarterly',
            reportDate: '2024-03-31',
            totalAmount: 50000000,
            status: 'completed',
            createTime: '2024-04-01 10:00:00'
          }
        ]
        this.pagination.total = 1
        this.loading = false
      }, 1000)
    },
    initCharts() {
      // 初始化图表
      this.$nextTick(() => {
        // 报表数量统计图表
        // 报表趋势分析图表
      })
    },
    handleSearch() {
      this.pagination.current = 1
      this.loadData()
    },
    handleReset() {
      this.searchForm = {
        reportType: '',
        period: '',
        dateRange: []
      }
      this.handleSearch()
    },
    handleExport() {
      this.$message.success('导出功能开发中...')
    },
    handleAdd() {
      this.dialogTitle = '新增报表统计'
      this.form = {
        reportName: '',
        reportType: '',
        period: '',
        reportDate: '',
        totalAmount: 0,
        remark: ''
      }
      this.dialogVisible = true
    },
    handleEdit(row) {
      this.dialogTitle = '编辑报表统计'
      this.form = { ...row }
      this.dialogVisible = true
    },
    handleView(row) {
      this.$message.info('查看功能开发中...')
    },
    handleAnalyze(row) {
      this.$message.info('分析功能开发中...')
    },
    handleDelete(row) {
      this.$confirm('确认删除该报表统计记录？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$message.success('删除成功')
        this.loadData()
      })
    },
    handleSubmit() {
      this.$refs.form.validate((valid) => {
        if (valid) {
          this.$message.success('保存成功')
          this.dialogVisible = false
          this.loadData()
        }
      })
    },
    handleSizeChange(val) {
      this.pagination.size = val
      this.loadData()
    },
    handleCurrentChange(val) {
      this.pagination.current = val
      this.loadData()
    },
    getReportTypeTag(type) {
      const tags = {
        balance_sheet: 'primary',
        income_statement: 'success',
        cash_flow: 'warning',
        equity_change: 'info'
      }
      return tags[type] || 'default'
    },
    getReportTypeName(type) {
      const names = {
        balance_sheet: '资产负债表',
        income_statement: '利润表',
        cash_flow: '现金流量表',
        equity_change: '所有者权益变动表'
      }
      return names[type] || type
    },
    getPeriodName(period) {
      const names = {
        monthly: '月度',
        quarterly: '季度',
        yearly: '年度'
      }
      return names[period] || period
    },
    getStatusTag(status) {
      const tags = {
        draft: 'info',
        processing: 'warning',
        completed: 'success',
        cancelled: 'danger'
      }
      return tags[status] || 'default'
    },
    getStatusName(status) {
      const names = {
        draft: '草稿',
        processing: '处理中',
        completed: '已完成',
        cancelled: '已取消'
      }
      return names[status] || status
    },
    formatAmount(amount) {
      return new Intl.NumberFormat('zh-CN', {
        style: 'currency',
        currency: 'CNY'
      }).format(amount)
    }
  }
}
</script>

<style scoped>
.gjbbtj-container {
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

.search-card {
  margin-bottom: 20px;
}

.chart-row {
  margin-bottom: 20px;
}

.table-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.pagination-container {
  margin-top: 20px;
  text-align: right;
}

.amount {
  font-weight: bold;
  color: #67C23A;
}

.dialog-footer {
  text-align: right;
}
</style>
