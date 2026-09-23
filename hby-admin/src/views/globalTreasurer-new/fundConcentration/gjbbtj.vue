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
            <el-option label="归集报表" value="COLLECTION" />
            <el-option label="下拨报表" value="ALLOCATION" />
            <el-option label="借贷报表" value="LOAN" />
            <el-option label="综合报表" value="COMPREHENSIVE" />
          </el-select>
        </el-form-item>
        <el-form-item label="统计周期">
          <el-select v-model="searchForm.period" placeholder="请选择统计周期" clearable>
            <el-option label="日报" value="DAILY" />
            <el-option label="周报" value="WEEKLY" />
            <el-option label="月报" value="MONTHLY" />
            <el-option label="季报" value="QUARTERLY" />
            <el-option label="年报" value="YEARLY" />
          </el-select>
        </el-form-item>
        <el-form-item label="创建时间">
          <el-date-picker
            v-model="searchForm.createTime"
            type="date"
            placeholder="选择创建时间"
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
        <div>
          <el-button type="danger" size="small" icon="el-icon-delete" :disabled="multipleSelection.length === 0" @click="handleBatchDelete">批量删除</el-button>
          <el-button type="primary" size="small" icon="el-icon-plus" @click="handleAdd">新增统计</el-button>
        </div>
      </div>
      
      <el-table
        v-loading="loading"
        :data="tableData"
        border
        stripe
        style="width: 100%"
        @selection-change="handleSelectionChange"
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
        <el-table-column prop="reportPeriod" label="统计周期" width="100">
          <template slot-scope="scope">
            <span>{{ getPeriodName(scope.row.reportPeriod) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="报表日期" width="220">
          <template slot-scope="scope">
            <span>{{ formatDate(scope.row.startDate) }} ~ {{ formatDate(scope.row.endDate) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="统计金额" width="150" align="right">
          <template slot-scope="scope">
            <span class="amount">{{ formatAmount(scope.row.totalCollection || scope.row.totalAllocation || scope.row.totalLoan || 0) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="reportStatus" label="状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="getStatusTag(scope.row.reportStatus)">
              {{ getStatusName(scope.row.reportStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="创建时间" width="120">
          <template slot-scope="scope">
            <span>{{ formatDate(scope.row.createTime) }}</span>
          </template>
        </el-table-column>
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
                <el-option label="归集报表" value="COLLECTION" />
                <el-option label="下拨报表" value="ALLOCATION" />
                <el-option label="借贷报表" value="LOAN" />
                <el-option label="综合报表" value="COMPREHENSIVE" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="统计周期" prop="reportPeriod">
              <el-select v-model="form.reportPeriod" placeholder="请选择统计周期">
                <el-option label="日报" value="DAILY" />
                <el-option label="周报" value="WEEKLY" />
                <el-option label="月报" value="MONTHLY" />
                <el-option label="季报" value="QUARTERLY" />
                <el-option label="年报" value="YEARLY" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="开始日期" prop="startDate">
              <el-date-picker
                v-model="form.startDate"
                type="date"
                placeholder="选择开始日期"
                format="yyyy-MM-dd"
                value-format="yyyy-MM-dd"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="结束日期" prop="endDate">
              <el-date-picker
                v-model="form.endDate"
                type="date"
                placeholder="选择结束日期"
                format="yyyy-MM-dd"
                value-format="yyyy-MM-dd"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
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
import * as echarts from 'echarts'
import { getReportStatisticsPage, getReportStatisticsById, createReportStatistics, updateReportStatistics, deleteReportStatistics, analyzeReport, batchDeleteReportStatistics, exportReportStatistics } from '@/api/globalTreasurer/zjjz'

export default {
  name: 'GjbbtjManage',
  data() {
    return {
      loading: false,
      searchForm: {
        reportType: '',
        period: '',
        createTime: ''
      },
      tableData: [],
      multipleSelection: [],
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
        reportPeriod: '',
        startDate: '',
        endDate: '',
        remark: ''
      },
      rules: {
        reportName: [
          { required: true, message: '请输入报表名称', trigger: 'blur' }
        ],
        reportType: [
          { required: true, message: '请选择报表类型', trigger: 'change' }
        ],
        reportPeriod: [
          { required: true, message: '请选择统计周期', trigger: 'change' }
        ],
        startDate: [
          { required: true, message: '请选择开始日期', trigger: 'change' }
        ],
        endDate: [
          { required: true, message: '请选择结束日期', trigger: 'change' }
        ]
      }
    }
  },
  mounted() {
    this.loadData()
    this.initCharts()
  },
  beforeDestroy() {
    if (this.countChart) {
      this.countChart.dispose()
      this.countChart = null
    }
    if (this.trendChart) {
      this.trendChart.dispose()
      this.trendChart = null
    }
  },
  methods: {
    async loadData() {
      this.loading = true
      try {
        const params = {
          pageNo: this.pagination.current,
          pageSize: this.pagination.size,
          reportType: this.searchForm.reportType || undefined,
          reportPeriod: this.searchForm.period || undefined,
          createTime: this.searchForm.createTime || undefined
        }
        const res = await getReportStatisticsPage(params)
        if (res.code === 1) {
          this.tableData = res.data.tlist || []
          this.pagination.total = res.data.totalRecord || 0
          this.updateCharts(this.tableData)
        } else {
          this.$message.error(res.msg || '查询失败')
        }
      } catch (error) {
        console.error('加载报表数据失败:', error)
        this.$message.error('加载数据失败')
      } finally {
        this.loading = false
      }
    },
    initCharts() {
      this.$nextTick(() => {
        // 报表数量统计饼图
        this.countChart = echarts.init(this.$refs.reportCountChart)
        this.countChart.setOption({
          title: { text: '报表类型分布', left: 'center', textStyle: { fontSize: 14 } },
          tooltip: { trigger: 'item', formatter: '{b}: {c} ({d}%)' },
          legend: { bottom: 0, type: 'scroll' },
          series: [{
            type: 'pie',
            radius: ['40%', '65%'],
            center: ['50%', '45%'],
            data: [],
            label: { show: true, formatter: '{b}\n{d}%' }
          }]
        })
        // 报表趋势折线图
        this.trendChart = echarts.init(this.$refs.reportTrendChart)
        this.trendChart.setOption({
          title: { text: '报表统计周期分布', left: 'center', textStyle: { fontSize: 14 } },
          tooltip: { trigger: 'axis' },
          xAxis: { type: 'category', data: [] },
          yAxis: { type: 'value', name: '数量' },
          series: [{ name: '报表数量', type: 'bar', data: [], itemStyle: { color: '#409EFF' } }]
        })
      })
    },
    updateCharts(data) {
      if (!data || data.length === 0) return
      this.$nextTick(() => {
        // 统计各类型数量
        const typeCount = {}
        const periodCount = {}
        const periodNameMap = { DAILY: '日报', WEEKLY: '周报', MONTHLY: '月报', QUARTERLY: '季报', YEARLY: '年报' }
        const typeNameMap = { COLLECTION: '归集报表', ALLOCATION: '下拨报表', LOAN: '借贷报表', COMPREHENSIVE: '综合报表' }
        data.forEach(item => {
          const t = typeNameMap[item.reportType] || item.reportType || '未知'
          typeCount[t] = (typeCount[t] || 0) + 1
          const p = periodNameMap[item.reportPeriod] || item.reportPeriod || '未知'
          periodCount[p] = (periodCount[p] || 0) + 1
        })
        if (this.countChart) {
          this.countChart.setOption({
            series: [{ data: Object.entries(typeCount).map(([name, value]) => ({ name, value })) }]
          })
        }
        if (this.trendChart) {
          const keys = Object.keys(periodCount)
          this.trendChart.setOption({
            xAxis: { data: keys },
            series: [{ data: keys.map(k => periodCount[k]) }]
          })
        }
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
        createTime: ''
      }
      this.handleSearch()
    },
    handleExport() {
      const params = {
        reportType: this.searchForm.reportType || undefined,
        reportPeriod: this.searchForm.period || undefined,
        startDate: this.searchForm.dateRange && this.searchForm.dateRange[0] ? this.searchForm.dateRange[0] : undefined,
        endDate: this.searchForm.dateRange && this.searchForm.dateRange[1] ? this.searchForm.dateRange[1] : undefined
      }
      exportReportStatistics(params).then(res => {
        const blob = new Blob([res], { type: 'application/vnd.ms-excel' })
        const url = window.URL.createObjectURL(blob)
        const a = document.createElement('a')
        a.href = url
        a.download = '报表统计_' + new Date().getTime() + '.xlsx'
        a.click()
        window.URL.revokeObjectURL(url)
        this.$message.success('导出成功')
      }).catch(() => {
        this.$message.error('导出失败')
      })
    },
    handleSelectionChange(val) {
      this.multipleSelection = val
    },
    handleBatchDelete() {
      if (this.multipleSelection.length === 0) {
        this.$message.warning('请选择要删除的记录')
        return
      }
      this.$confirm(`确认删除选中的 ${this.multipleSelection.length} 条记录？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const ids = this.multipleSelection.map(item => item.reportId)
          const res = await batchDeleteReportStatistics(ids)
          if (res.code === 1) {
            this.$message.success('批量删除成功')
            this.loadData()
          } else {
            this.$message.error(res.msg || '批量删除失败')
          }
        } catch (error) {
          this.$message.error('批量删除失败')
        }
      })
    },
    handleAdd() {
      this.dialogTitle = '新增报表统计'
      this.form = {
        reportName: '',
        reportType: '',
        reportPeriod: '',
        startDate: '',
        endDate: '',
        remark: ''
      }
      this.dialogVisible = true
    },
    async handleEdit(row) {
      this.dialogTitle = '编辑报表统计'
      try {
        const res = await getReportStatisticsById(row.reportId)
        if (res.code === 1) {
          this.form = res.data || { ...row }
        } else {
          this.form = { ...row }
        }
      } catch (error) {
        this.form = { ...row }
      }
      this.dialogVisible = true
    },
    async handleView(row) {
      try {
        const res = await getReportStatisticsById(row.reportId)
        if (res.code === 1) {
          this.$alert(`
            <div style="line-height: 2;">
              <p><strong>报表名称：</strong>${res.data.reportName || '-'}</p>
              <p><strong>报表类型：</strong>${this.getReportTypeName(res.data.reportType)}</p>
              <p><strong>统计周期：</strong>${this.getPeriodName(res.data.reportPeriod)}</p>
              <p><strong>统计金额：</strong>${this.formatAmount(res.data.totalAmount)}</p>
              <p><strong>备注：</strong>${res.data.remark || '-'}</p>
            </div>
          `, '报表详情', {
            dangerouslyUseHTMLString: true,
            confirmButtonText: '关闭'
          })
        }
      } catch (error) {
        this.$message.error('获取详情失败')
      }
    },
    async handleAnalyze(row) {
      try {
        const res = await analyzeReport(row.reportId)
        if (res.code === 1) {
          this.$alert(`
            <div style="line-height: 2;">
              <p><strong>分析结果：</strong></p>
              <pre>${JSON.stringify(res.data, null, 2)}</pre>
            </div>
          `, '报表分析', {
            dangerouslyUseHTMLString: true,
            confirmButtonText: '关闭'
          })
        } else {
          this.$message.error(res.msg || '分析失败')
        }
      } catch (error) {
        this.$message.error('分析失败')
      }
    },
    handleDelete(row) {
      this.$confirm('确认删除该报表统计记录？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const res = await deleteReportStatistics(row.reportId)
          if (res.code === 1) {
            this.$message.success('删除成功')
            this.loadData()
          } else {
            this.$message.error(res.msg || '删除失败')
          }
        } catch (error) {
          this.$message.error('删除失败')
        }
      })
    },
    async handleSubmit() {
      this.$refs.form.validate(async (valid) => {
        if (valid) {
          try {
            const api = this.form.reportId ? updateReportStatistics : createReportStatistics
            const res = await api(this.form)
            if (res.code === 1) {
              this.$message.success('保存成功')
              this.dialogVisible = false
              this.loadData()
            } else {
              this.$message.error(res.msg || '保存失败')
            }
          } catch (error) {
            this.$message.error('保存失败')
          }
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
        COLLECTION: 'primary',
        ALLOCATION: 'success',
        LOAN: 'warning',
        COMPREHENSIVE: 'info'
      }
      return tags[type] || 'default'
    },
    getReportTypeName(type) {
      const names = {
        COLLECTION: '归集报表',
        ALLOCATION: '下拨报表',
        LOAN: '借贷报表',
        COMPREHENSIVE: '综合报表'
      }
      return names[type] || type
    },
    getPeriodName(period) {
      const names = {
        DAILY: '日报',
        WEEKLY: '周报',
        MONTHLY: '月报',
        QUARTERLY: '季报',
        YEARLY: '年报'
      }
      return names[period] || period
    },
    getStatusTag(status) {
      const tags = {
        DRAFT: 'info',
        GENERATED: 'success',
        PUBLISHED: 'primary'
      }
      return tags[status] || 'default'
    },
    getStatusName(status) {
      const names = {
        DRAFT: '草稿',
        GENERATED: '已生成',
        PUBLISHED: '已发布'
      }
      return names[status] || status
    },
    formatAmount(amount) {
      if (amount === null || amount === undefined || amount === '') return '-'
      return new Intl.NumberFormat('zh-CN', {
        style: 'currency',
        currency: 'CNY'
      }).format(amount)
    },
    formatDate(val) {
      if (!val) return '-'
      const d = new Date(val)
      if (isNaN(d.getTime())) return val
      const y = d.getFullYear()
      const m = String(d.getMonth() + 1).padStart(2, '0')
      const day = String(d.getDate()).padStart(2, '0')
      return `${y}-${m}-${day}`
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
