<template>
  <div class="gjjgfx-container">
    <div class="page-header">
      <h2>国际监管分析</h2>
      <p>分析和监控国际监管要求的合规情况</p>
    </div>

    <!-- 查询条件 -->
    <el-card class="search-card" shadow="never">
      <el-form :model="searchForm" :inline="true" size="small">
        <el-form-item label="监管机构">
          <el-select v-model="searchForm.regulator" placeholder="请选择监管机构" clearable>
            <el-option label="中国人民银行" value="PBOC" />
            <el-option label="银保监会" value="CBIRC" />
            <el-option label="证监会" value="CSRC" />
            <el-option label="外汇管理局" value="SAFE" />
          </el-select>
        </el-form-item>
        <el-form-item label="分析类型">
          <el-select v-model="searchForm.analysisType" placeholder="请选择分析类型" clearable>
            <el-option label="跨境资金分析" value="CROSS_BORDER" />
            <el-option label="大额交易分析" value="LARGE_AMOUNT" />
            <el-option label="高频交易分析" value="FREQUENCY" />
            <el-option label="合规检查分析" value="COMPLIANCE" />
          </el-select>
        </el-form-item>
        <el-form-item label="分析日期">
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

    <!-- 监管指标概览 -->
    <el-row :gutter="20" class="metrics-row">
      <el-col :span="6">
        <el-card class="metric-card" shadow="never">
          <div class="metric-content">
            <div class="metric-value">{{ overviewData.complianceRate }}%</div>
            <div class="metric-label">合规率</div>
          </div>
          <i class="el-icon-success metric-icon success"></i>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="metric-card" shadow="never">
          <div class="metric-content">
            <div class="metric-value">{{ overviewData.pendingCount }}</div>
            <div class="metric-label">待处理事项</div>
          </div>
          <i class="el-icon-warning metric-icon warning"></i>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="metric-card" shadow="never">
          <div class="metric-content">
            <div class="metric-value">{{ overviewData.riskWarningCount }}</div>
            <div class="metric-label">风险预警</div>
          </div>
          <i class="el-icon-error metric-icon danger"></i>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="metric-card" shadow="never">
          <div class="metric-content">
            <div class="metric-value">{{ overviewData.reportCount }}</div>
            <div class="metric-label">监管报告</div>
          </div>
          <i class="el-icon-document metric-icon info"></i>
        </el-card>
      </el-col>
    </el-row>

    <!-- 分析图表 -->
    <el-row :gutter="20" class="chart-row">
      <el-col :span="12">
        <el-card title="合规性趋势分析" shadow="never">
          <div ref="complianceChart" style="height: 300px;"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card title="监管指标分布" shadow="never">
          <div ref="metricsChart" style="height: 300px;"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 分析结果表格 -->
    <el-card class="table-card" shadow="never">
      <div slot="header" class="card-header">
        <span>监管分析结果</span>
        <div>
          <el-button type="primary" size="small" icon="el-icon-plus" @click="handleAdd">新增分析</el-button>
          <el-button type="danger" size="small" icon="el-icon-delete" :disabled="multipleSelection.length === 0" @click="handleBatchDelete">批量删除</el-button>
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
        <el-table-column prop="analysisName" label="分析名称" min-width="150" />
        <el-table-column prop="regulator" label="监管机构" width="120">
          <template slot-scope="scope">
            <span>{{ getRegulatorName(scope.row.regulator) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="analysisType" label="分析类型" width="120">
          <template slot-scope="scope">
            <el-tag :type="getAnalysisTypeTag(scope.row.analysisType)">
              {{ getAnalysisTypeName(scope.row.analysisType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="complianceScore" label="合规评分" width="100" align="center">
          <template slot-scope="scope">
            <el-progress
              :percentage="scope.row.complianceScore || 0"
              :color="getScoreColor(scope.row.complianceScore)"
              :show-text="false"
              :stroke-width="8"
            />
            <div style="margin-top: 5px;">{{ scope.row.complianceScore || 0 }}%</div>
          </template>
        </el-table-column>
        <el-table-column prop="riskLevel" label="风险等级" width="100">
          <template slot-scope="scope">
            <el-tag :type="getRiskLevelTag(scope.row.riskLevel)">
              {{ getRiskLevelName(scope.row.riskLevel) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="analysisDate" label="分析日期" width="120">
          <template slot-scope="scope">
            <span>{{ formatDate(scope.row.analysisDate) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="analysisStatus" label="状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="getStatusTag(scope.row.analysisStatus)">
              {{ getStatusName(scope.row.analysisStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template slot-scope="scope">
            <el-button type="text" size="small" @click="handleView(scope.row)">查看</el-button>
            <el-button type="text" size="small" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button type="text" size="small" @click="handleReport(scope.row)">生成报告</el-button>
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
        @submit.native.prevent
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="分析名称" prop="analysisName">
              <el-input v-model="form.analysisName" placeholder="请输入分析名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="监管机构" prop="regulator">
              <el-select v-model="form.regulator" placeholder="请选择监管机构">
                <el-option label="中国人民银行" value="PBOC" />
                <el-option label="银保监会" value="CBIRC" />
                <el-option label="证监会" value="CSRC" />
                <el-option label="外汇管理局" value="SAFE" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="分析类型" prop="analysisType">
              <el-select v-model="form.analysisType" placeholder="请选择分析类型">
                <el-option label="跨境资金分析" value="CROSS_BORDER" />
                <el-option label="大额交易分析" value="LARGE_AMOUNT" />
                <el-option label="高频交易分析" value="FREQUENCY" />
                <el-option label="合规检查分析" value="COMPLIANCE" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="分析日期" prop="analysisDate">
              <el-date-picker
                v-model="form.analysisDate"
                type="date"
                placeholder="选择分析日期"
                format="yyyy-MM-dd"
                value-format="yyyy-MM-dd"
                style="width: 100%"
                :picker-options="pickerOptions"
                @change="handleDateChange"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="合规评分" prop="complianceScore">
              <el-input-number
                v-model="form.complianceScore"
                :min="0"
                :max="100"
                :precision="0"
                controls-position="right"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="风险等级" prop="riskLevel">
              <el-select v-model="form.riskLevel" placeholder="请选择风险等级">
                <el-option label="低风险" value="LOW" />
                <el-option label="中风险" value="MEDIUM" />
                <el-option label="高风险" value="HIGH" />
                <el-option label="严重风险" value="CRITICAL" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="分析描述">
          <el-input
            v-model="form.description"
            type="textarea"
            :rows="3"
            placeholder="请输入分析描述"
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
import { getRegulatoryAnalysisPage, getRegulatoryAnalysisById, createRegulatoryAnalysis, updateRegulatoryAnalysis, deleteRegulatoryAnalysis, generateRegulatoryReport, batchDeleteRegulatoryAnalysis, exportRegulatoryAnalysis, getRegulatoryOverviewStatistics, getComplianceStatistics, getRiskDistribution } from '@/api/globalTreasurer/zjjz'

export default {
  name: 'GjjgfxManage',
  data() {
    return {
      loading: false,
      overviewData: {
        complianceRate: 0,
        pendingCount: 0,
        riskWarningCount: 0,
        reportCount: 0
      },
      searchForm: {
        regulator: '',
        analysisType: '',
        dateRange: []
      },
      tableData: [],
      multipleSelection: [],
      pagination: {
        current: 1,
        size: 20,
        total: 0
      },
      dialogVisible: false,
      dialogTitle: '新增监管分析',
      pickerOptions: {
        disabledDate(time) {
          return time.getTime() > Date.now()
        }
      },
      form: {
        analysisName: '',
        regulator: '',
        analysisType: '',
        analysisDate: '',
        complianceScore: 0,
        riskLevel: '',
        description: ''
      },
      rules: {
        analysisName: [
          { required: true, message: '请输入分析名称', trigger: 'blur' }
        ],
        regulator: [
          { required: true, message: '请选择监管机构', trigger: 'change' }
        ],
        analysisType: [
          { required: true, message: '请选择分析类型', trigger: 'change' }
        ],
        analysisDate: [
          { required: true, message: '请选择分析日期', trigger: 'change' }
        ],
        complianceScore: [
          { required: true, message: '请输入合规评分', trigger: 'blur' }
        ],
        riskLevel: [
          { required: true, message: '请选择风险等级', trigger: 'change' }
        ]
      }
    }
  },
  mounted() {
    this.loadData()
    this.loadOverviewStatistics()
    this.initCharts()
  },
  methods: {
    async loadData() {
      this.loading = true
      try {
        const params = {
          pageNo: this.pagination.current,
          pageSize: this.pagination.size,
          analysisType: this.searchForm.analysisType || undefined
        }
        console.log('加载列表数据，参数:', params)
        const res = await getRegulatoryAnalysisPage(params)
        console.log('后端返回:', res)
        if (res.code === 1) {
          this.tableData = res.data.tlist || []
          this.pagination.total = res.data.totalRecord || 0
          console.log('加载成功，列表数量:', this.tableData.length)
        } else {
          this.$message.error(res.msg || '查询失败')
        }
      } catch (error) {
        console.error('加载监管分析数据失败:', error)
        this.$message.error('加载数据失败')
      } finally {
        this.loading = false
      }
    },
    async loadOverviewStatistics() {
      try {
        const res = await getRegulatoryOverviewStatistics()
        if (res.code === 1) {
          this.overviewData = res.data
        }
      } catch (error) {
        console.error('加载概览统计失败:', error)
      }
    },
    initCharts() {
      // 初始化图表
      this.$nextTick(() => {
        this.initComplianceChart()
        this.initMetricsChart()
      })
    },
    async initComplianceChart() {
      if (!this.$refs.complianceChart) return
      const chart = echarts.init(this.$refs.complianceChart)

      try {
        const res = await getComplianceStatistics()
        if (res.code === 1) {
          const stats = res.data
          // 使用合规统计数据展示
          const option = {
            title: { text: '合规状态分布', left: 'center', textStyle: { fontSize: 14 } },
            tooltip: { trigger: 'item', formatter: '{b}: {c} ({d}%)' },
            series: [{
              type: 'pie',
              radius: ['40%', '70%'],
              data: [
                { value: stats.compliantCount || 0, name: '合规', itemStyle: { color: '#67C23A' } },
                { value: stats.nonCompliantCount || 0, name: '不合规', itemStyle: { color: '#F56C6C' } },
                { value: stats.pendingReviewCount || 0, name: '待审核', itemStyle: { color: '#E6A23C' } }
              ],
              label: { show: true, formatter: '{b}\n{d}%' }
            }]
          }
          chart.setOption(option)
        }
      } catch (error) {
        console.error('加载合规统计失败:', error)
      }
    },
    async initMetricsChart() {
      if (!this.$refs.metricsChart) return
      const chart = echarts.init(this.$refs.metricsChart)

      try {
        const res = await getRiskDistribution()
        if (res.code === 1) {
          const dist = res.data
          // 使用风险分布数据展示
          const option = {
            title: { text: '风险等级分布', left: 'center', textStyle: { fontSize: 14 } },
            tooltip: { trigger: 'item', formatter: '{b}: {c} ({d}%)' },
            series: [{
              type: 'pie',
              radius: ['40%', '70%'],
              data: [
                { value: dist.lowCount || 0, name: '低风险', itemStyle: { color: '#67C23A' } },
                { value: dist.mediumCount || 0, name: '中风险', itemStyle: { color: '#E6A23C' } },
                { value: dist.highCount || 0, name: '高风险', itemStyle: { color: '#F56C6C' } },
                { value: dist.criticalCount || 0, name: '严重风险', itemStyle: { color: '#F56C6C' } }
              ],
              label: { show: true, formatter: '{b}\n{d}%' }
            }]
          }
          chart.setOption(option)
        }
      } catch (error) {
        console.error('加载风险分布失败:', error)
      }
    },
    handleSearch() {
      this.pagination.current = 1
      this.loadData()
    },
    handleReset() {
      this.searchForm = {
        regulator: '',
        analysisType: '',
        dateRange: []
      }
      this.handleSearch()
    },
    handleExport() {
      const params = {
        regulator: this.searchForm.regulator || undefined,
        analysisType: this.searchForm.analysisType || undefined,
        startDate: this.searchForm.dateRange && this.searchForm.dateRange[0] ? this.searchForm.dateRange[0] : undefined,
        endDate: this.searchForm.dateRange && this.searchForm.dateRange[1] ? this.searchForm.dateRange[1] : undefined
      }
      exportRegulatoryAnalysis(params).then(res => {
        const blob = new Blob([res], { type: 'application/vnd.ms-excel' })
        const url = window.URL.createObjectURL(blob)
        const a = document.createElement('a')
        a.href = url
        a.download = '监管分析_' + new Date().getTime() + '.xlsx'
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
      this.$confirm(`确认批量删除选中的 ${this.multipleSelection.length} 条记录？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const ids = this.multipleSelection.map(item => item.analysisId)
          const res = await batchDeleteRegulatoryAnalysis(ids)
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
      console.log('点击新增按钮')
      this.dialogTitle = '新增监管分析'
      // 创建新的表单对象，避免引用污染
      this.form = {
        analysisName: '',
        regulator: '',
        analysisType: '',
        analysisDate: '',
        complianceScore: 0,
        riskLevel: '',
        description: '',
        analysisId: undefined  // 使用 undefined 而不是 null
      }
      this.dialogVisible = true
      // 重置表单验证状态
      this.$nextTick(() => {
        if (this.$refs.form) {
          this.$refs.form.clearValidate()
        }
      })
    },
    async handleEdit(row) {
      console.log('点击编辑按钮，row:', JSON.parse(JSON.stringify(row)))
      this.dialogTitle = '编辑监管分析'
      try {
        const res = await getRegulatoryAnalysisById(row.analysisId)
        const sourceData = res.code === 1 ? (res.data || row) : row

        // 使用深拷贝避免引用污染
        this.form = JSON.parse(JSON.stringify(sourceData))

        // 处理日期字段：确保日期格式正确
        if (this.form.analysisDate) {
          // 如果是 Date 对象，转换为 yyyy-MM-dd 格式字符串
          if (this.form.analysisDate instanceof Date) {
            const d = this.form.analysisDate
            const year = d.getFullYear()
            const month = String(d.getMonth() + 1).padStart(2, '0')
            const day = String(d.getDate()).padStart(2, '0')
            this.form.analysisDate = `${year}-${month}-${day}`
          }
          // 如果是时间戳，转换为 yyyy-MM-dd 格式
          else if (typeof this.form.analysisDate === 'number' || /^\d+$/.test(this.form.analysisDate)) {
            const d = new Date(this.form.analysisDate)
            if (!isNaN(d.getTime())) {
              const year = d.getFullYear()
              const month = String(d.getMonth() + 1).padStart(2, '0')
              const day = String(d.getDate()).padStart(2, '0')
              this.form.analysisDate = `${year}-${month}-${day}`
            }
          }
          // 如果是 ISO 格式或其他格式，尝试解析并格式化
          else {
            const d = new Date(this.form.analysisDate)
            if (!isNaN(d.getTime())) {
              const year = d.getFullYear()
              const month = String(d.getMonth() + 1).padStart(2, '0')
              const day = String(d.getDate()).padStart(2, '0')
              this.form.analysisDate = `${year}-${month}-${day}`
            }
          }
        }

        console.log('设置编辑表单数据:', JSON.parse(JSON.stringify(this.form)))
      } catch (error) {
        console.error('获取详情失败:', error)
        this.form = JSON.parse(JSON.stringify(row))
      }
      this.dialogVisible = true
      this.$nextTick(() => {
        if (this.$refs.form) {
          this.$refs.form.clearValidate()
        }
        // 打印对话框打开后的表单日期
        console.log('对话框打开后表单日期:', this.form.analysisDate)
        console.log('表单完整数据:', JSON.parse(JSON.stringify(this.form)))
      })
    },
    handleDateChange(value) {
      console.log('日期变化:', value, '类型:', typeof value)
      this.form.analysisDate = value
    },
    async handleView(row) {
      try {
        const res = await getRegulatoryAnalysisById(row.analysisId)
        if (res.code === 1) {
          const data = res.data || row
          this.$alert(`
            <div style="line-height: 2;">
              <p><strong>分析名称：</strong>${data.analysisName || '-'}</p>
              <p><strong>监管机构：</strong>${this.getRegulatorName(data.regulator)}</p>
              <p><strong>分析类型：</strong>${this.getAnalysisTypeName(data.analysisType)}</p>
              <p><strong>分析日期：</strong>${this.formatDate(data.analysisDate)}</p>
              <p><strong>合规评分：</strong>${data.complianceScore || 0}</p>
              <p><strong>风险等级：</strong>${this.getRiskLevelName(data.riskLevel)}</p>
              <p><strong>状态：</strong>${this.getStatusName(data.analysisStatus)}</p>
              <p><strong>分析描述：</strong>${data.description || '-'}</p>
            </div>
          `, '监管分析详情', {
            dangerouslyUseHTMLString: true,
            confirmButtonText: '关闭'
          })
        }
      } catch (error) {
        this.$message.error('获取详情失败')
      }
    },
    async handleReport(row) {
      try {
        const res = await generateRegulatoryReport(row.analysisId, 'PDF')

        // 检查响应是否为 Blob 类型（文件下载）
        if (res instanceof Blob) {
          const blob = new Blob([res], { type: 'application/vnd.ms-excel' })
          const url = window.URL.createObjectURL(blob)
          const a = document.createElement('a')
          a.href = url
          a.download = '监管分析报告_' + row.analysisName + '_' + row.analysisId + '.xlsx'
          document.body.appendChild(a)
          a.click()
          window.URL.revokeObjectURL(url)
          document.body.removeChild(a)
          this.$message.success('报告生成成功')
        } else {
          // 如果返回的是 JSON 错误响应
          const reader = new FileReader()
          reader.onload = (e) => {
            try {
              const errorData = JSON.parse(e.target.result)
              this.$message.error(errorData.msg || '生成报告失败')
            } catch {
              this.$message.error('生成报告失败')
            }
          }
          reader.readAsText(res)
        }
      } catch (error) {
        console.error('生成报告失败:', error)
        this.$message.error('生成报告失败')
      }
    },
    handleDelete(row) {
      this.$confirm('确认删除该监管分析记录？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const res = await deleteRegulatoryAnalysis(row.analysisId)
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
      console.log('开始提交表单...')
      console.log('当前表单数据:', JSON.parse(JSON.stringify(this.form)))

      this.$refs.form.validate(async (valid) => {
        console.log('表单验证结果:', valid)

        if (!valid) {
          this.$message.warning('请填写完整的表单数据')
          return
        }

        try {
          const isEdit = !!this.form.analysisId
          console.log('是否为编辑操作:', isEdit)

          const api = isEdit ? updateRegulatoryAnalysis : createRegulatoryAnalysis
          console.log('选择的API:', isEdit ? 'updateRegulatoryAnalysis' : 'createRegulatoryAnalysis')

          // 准备提交数据
          const submitData = { ...this.form }
          console.log('准备提交的数据:', JSON.parse(JSON.stringify(submitData)))

          const res = await api(submitData)
          console.log('后端返回结果:', res)

          if (res.code === 1) {
            this.$message.success(isEdit ? '更新成功' : '新增成功')
            this.dialogVisible = false
            this.loadData()
            // 重置表单
            this.resetForm()
          } else {
            this.$message.error(res.msg || (isEdit ? '更新失败' : '新增失败'))
          }
        } catch (error) {
          console.error('提交失败:', error)
          this.$message.error('保存失败: ' + (error.message || '未知错误'))
        }
      })
    },
    resetForm() {
      console.log('重置表单')
      this.$refs.form.resetFields()
    },
    handleSizeChange(val) {
      this.pagination.size = val
      this.loadData()
    },
    handleCurrentChange(val) {
      this.pagination.current = val
      this.loadData()
    },
    formatDate(date) {
      if (!date) return '-'
      const d = new Date(date)
      const year = d.getFullYear()
      const month = String(d.getMonth() + 1).padStart(2, '0')
      const day = String(d.getDate()).padStart(2, '0')
      return `${year}年${month}月${day}日`
    },
    getRegulatorName(regulator) {
      const names = {
        pboc: '中国人民银行',
        cbirc: '银保监会',
        csrc: '证监会',
        safe: '外汇管理局',
        // 支持大写枚举值（后端可能返回）
        PBOC: '中国人民银行',
        CBIRC: '银保监会',
        CSRC: '证监会',
        SAFE: '外汇管理局'
      }
      return names[regulator] || regulator || '-'
    },
    getAnalysisTypeTag(type) {
      const tags = {
        // 前端期望的枚举值
        compliance: 'primary',
        risk_assessment: 'warning',
        regulatory_metrics: 'success',
        trend_analysis: 'info',
        // 后端返回的大写枚举值
        CROSS_BORDER: 'primary',
        LARGE_AMOUNT: 'warning',
        FREQUENCY: 'info',
        COMPLIANCE: 'success'
      }
      return tags[type] || 'default'
    },
    getAnalysisTypeName(type) {
      const names = {
        // 前端期望的枚举值
        compliance: '合规性分析',
        risk_assessment: '风险评估',
        regulatory_metrics: '监管指标',
        trend_analysis: '趋势分析',
        // 后端返回的大写枚举值
        CROSS_BORDER: '跨境资金分析',
        LARGE_AMOUNT: '大额交易分析',
        FREQUENCY: '高频交易分析',
        COMPLIANCE: '合规检查分析'
      }
      return names[type] || type || '-'
    },
    getRiskLevelTag(level) {
      const tags = {
        // 前端期望的枚举值
        low: 'success',
        medium: 'warning',
        high: 'danger',
        critical: 'danger',
        // 后端返回的大写枚举值
        LOW: 'success',
        MEDIUM: 'warning',
        HIGH: 'danger',
        CRITICAL: 'danger'
      }
      return tags[level] || 'default'
    },
    getRiskLevelName(level) {
      const names = {
        // 前端期望的枚举值
        low: '低风险',
        medium: '中风险',
        high: '高风险',
        critical: '极高风险',
        // 后端返回的大写枚举值
        LOW: '低风险',
        MEDIUM: '中风险',
        HIGH: '高风险',
        CRITICAL: '严重风险'
      }
      return names[level] || level || '-'
    },
    getStatusTag(status) {
      const tags = {
        // 前端期望的枚举值
        draft: 'info',
        processing: 'warning',
        completed: 'success',
        cancelled: 'danger',
        // 后端返回的大写枚举值
        DRAFT: 'info',
        ANALYZING: 'warning',
        COMPLETED: 'success',
        REVIEWED: 'success'
      }
      return tags[status] || 'default'
    },
    getStatusName(status) {
      const names = {
        // 前端期望的枚举值
        draft: '草稿',
        processing: '处理中',
        completed: '已完成',
        cancelled: '已取消',
        // 后端返回的大写枚举值
        DRAFT: '草稿',
        ANALYZING: '分析中',
        COMPLETED: '已完成',
        REVIEWED: '已审核'
      }
      return names[status] || status || '-'
    },
    getScoreColor(score) {
      if (score >= 90) return '#67C23A'
      if (score >= 70) return '#E6A23C'
      return '#F56C6C'
    }
  }
}
</script>

<style scoped>
.gjjgfx-container {
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

.metrics-row {
  margin-bottom: 20px;
}

.metric-card {
  position: relative;
  overflow: hidden;
}

.metric-content {
  padding: 20px;
}

.metric-value {
  font-size: 32px;
  font-weight: bold;
  color: #303133;
  line-height: 1;
}

.metric-label {
  font-size: 14px;
  color: #909399;
  margin-top: 8px;
}

.metric-icon {
  position: absolute;
  right: 20px;
  top: 50%;
  transform: translateY(-50%);
  font-size: 40px;
  opacity: 0.3;
}

.metric-icon.success {
  color: #67C23A;
}

.metric-icon.warning {
  color: #E6A23C;
}

.metric-icon.danger {
  color: #F56C6C;
}

.metric-icon.info {
  color: #409EFF;
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

.dialog-footer {
  text-align: right;
}
</style>
