<template>
  <div class="report-analysis-wrapper">
    <el-card shadow="never">
      <div slot="header" class="card-header">
        <span>报表分析</span>
        <el-button type="primary" size="small" icon="el-icon-refresh" @click="refreshData">刷新</el-button>
      </div>

      <!-- 报表操作 -->
      <el-row :gutter="16" class="report-actions">
        <el-col :span="6">
          <div class="action-card action-card--blue" @click="generatePositionReport">
            <i class="el-icon-s-order action-icon"></i>
            <div class="action-title">持仓报表</div>
            <div class="action-desc">生成持仓分析报表</div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="action-card action-card--green" @click="generatePnLReport">
            <i class="el-icon-s-finance action-icon"></i>
            <div class="action-title">损益报表</div>
            <div class="action-desc">生成损益分析报表</div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="action-card action-card--orange" @click="generateRiskReport">
            <i class="el-icon-warning-outline action-icon"></i>
            <div class="action-title">风险报表</div>
            <div class="action-desc">生成风险分析报表</div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="action-card action-card--red" @click="generateCustomReport">
            <i class="el-icon-s-data action-icon"></i>
            <div class="action-title">自定义报表</div>
            <div class="action-desc">生成自定义分析报表</div>
          </div>
        </el-col>
      </el-row>

      <!-- 报表列表 -->
      <el-tabs v-model="activeTab" class="report-tabs" @tab-click="handleTabClick">
        <el-tab-pane label="报表列表" name="list">
          <div class="table-header">
            <el-button type="primary" size="small" icon="el-icon-plus" @click="handleAddReport">新建报表</el-button>
            <el-button type="danger" size="small" icon="el-icon-delete" :disabled="multiple" @click="handleBatchDelete">批量删除</el-button>
          </div>
          <el-table :data="reportList" border stripe v-loading="loading" @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="55" align="center" />
            <el-table-column prop="reportName" label="报表名称" width="200" show-overflow-tooltip />
            <el-table-column prop="reportType" label="报表类型" width="120" align="center">
              <template slot-scope="scope">
                <el-tag :type="getReportTypeTag(scope.row.reportType)" size="small">
                  {{ getReportTypeText(scope.row.reportType) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="reportPeriod" label="报表周期" width="120" align="center">
              <template slot-scope="scope">
                {{ getReportPeriodText(scope.row.reportPeriod) }}
              </template>
            </el-table-column>
            <el-table-column prop="generatedBy" label="生成人" width="120" />
            <el-table-column prop="generateTime" label="生成时间" width="160" align="center" />
            <el-table-column prop="fileSize" label="文件大小" width="100" align="right">
              <template slot-scope="scope">
                <span>{{ formatFileSize(scope.row.fileSize) }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="status" label="状态" width="100" align="center">
              <template slot-scope="scope">
                <el-tag :type="scope.row.status === 'COMPLETED' ? 'success' : 'warning'" size="small">
                  {{ scope.row.status === 'COMPLETED' ? '已完成' : '生成中' }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" align="center" width="260" fixed="right">
              <template slot-scope="scope">
                <el-button size="mini" type="text" icon="el-icon-view" @click="handleViewReport(scope.row)">查看</el-button>
                <el-button size="mini" type="text" icon="el-icon-edit" @click="handleEditReport(scope.row)">修改</el-button>
                <el-button size="mini" type="text" icon="el-icon-download" @click="handleDownloadReport(scope.row)">下载</el-button>
                <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDeleteReport(scope.row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>

        <el-tab-pane label="报表模板" name="template">
          <div class="table-header">
            <el-button type="primary" size="small" icon="el-icon-plus" @click="handleAddTemplate">新建模板</el-button>
          </div>
          <el-table :data="templateList" border stripe>
            <el-table-column prop="templateName" label="模板名称" width="200" />
            <el-table-column prop="templateType" label="模板类型" width="120" align="center">
              <template slot-scope="scope">
                <el-tag :type="getReportTypeTag(scope.row.templateType)" size="small">
                  {{ getReportTypeText(scope.row.templateType) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="description" label="描述" min-width="200" show-overflow-tooltip />
            <el-table-column prop="createdBy" label="创建人" width="120" />
            <el-table-column prop="createTime" label="创建时间" width="160" align="center">
              <template slot-scope="scope">{{ formatDate(scope.row.createTime) }}</template>
            </el-table-column>
            <el-table-column label="操作" align="center" width="200">
              <template slot-scope="scope">
                <el-button size="mini" type="text" @click="handleEditTemplate(scope.row)">编辑</el-button>
                <el-button size="mini" type="text" @click="handleUseTemplate(scope.row)">使用</el-button>
                <el-button size="mini" type="text" @click="handleDeleteTemplate(scope.row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>

        <el-tab-pane label="统计图表" name="chart">
          <el-row :gutter="20">
            <el-col :span="12">
              <el-card class="chart-card">
                <div slot="header"><span>持仓分布</span></div>
                <div ref="positionChart" style="width:100%;height:280px;"></div>
              </el-card>
            </el-col>
            <el-col :span="12">
              <el-card class="chart-card">
                <div slot="header"><span>损益趋势（近7天）</span></div>
                <div ref="pnlChart" style="width:100%;height:280px;"></div>
              </el-card>
            </el-col>
          </el-row>
          <el-row :gutter="20" style="margin-top: 20px">
            <el-col :span="12">
              <el-card class="chart-card">
                <div slot="header"><span>风险价值（VaR）</span></div>
                <div ref="varChart" style="width:100%;height:280px;"></div>
              </el-card>
            </el-col>
            <el-col :span="12">
              <el-card class="chart-card">
                <div slot="header"><span>收益率分布</span></div>
                <div ref="returnChart" style="width:100%;height:280px;"></div>
              </el-card>
            </el-col>
          </el-row>
        </el-tab-pane>
      </el-tabs>
    </el-card>

    <!-- 生成报表对话框 -->
    <el-dialog title="生成报表" :visible.sync="generateDialogVisible" width="600px">
      <el-form :model="reportForm" label-width="120px">
        <el-form-item label="报表名称">
          <el-input v-model="reportForm.reportName" placeholder="请输入报表名称" />
        </el-form-item>
        <el-form-item label="报表类型">
          <el-select v-model="reportForm.reportType" style="width: 100%">
            <el-option label="持仓报表" value="POSITION" />
            <el-option label="损益报表" value="PNL" />
            <el-option label="风险报表" value="RISK" />
            <el-option label="交易报表" value="TRANSACTION" />
          </el-select>
        </el-form-item>
        <el-form-item label="报表周期">
          <el-select v-model="reportForm.reportPeriod" style="width: 100%">
            <el-option label="日报" value="DAILY" />
            <el-option label="周报" value="WEEKLY" />
            <el-option label="月报" value="MONTHLY" />
            <el-option label="季报" value="QUARTERLY" />
            <el-option label="年报" value="YEARLY" />
          </el-select>
        </el-form-item>
        <el-form-item label="开始日期">
          <el-date-picker
            v-model="reportForm.startDate"
            type="date"
            placeholder="选择开始日期"
            format="yyyy-MM-dd"
            value-format="yyyy-MM-dd"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="结束日期">
          <el-date-picker
            v-model="reportForm.endDate"
            type="date"
            placeholder="选择结束日期"
            format="yyyy-MM-dd"
            value-format="yyyy-MM-dd"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="输出格式">
          <el-checkbox-group v-model="reportForm.outputFormats">
            <el-checkbox label="PDF">PDF</el-checkbox>
            <el-checkbox label="EXCEL">Excel</el-checkbox>
            <el-checkbox label="WORD">Word</el-checkbox>
          </el-checkbox-group>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button type="primary" :loading="generateLoading" @click="submitGenerateReport">生成</el-button>
        <el-button @click="generateDialogVisible = false">取消</el-button>
      </div>
    </el-dialog>

    <!-- 模板编辑对话框 -->
    <el-dialog :title="templateForm.templateId ? '编辑模板' : '新建模板'" :visible.sync="templateDialogVisible" width="500px">
      <el-form :model="templateForm" label-width="100px">
        <el-form-item label="模板名称">
          <el-input v-model="templateForm.templateName" placeholder="请输入模板名称" />
        </el-form-item>
        <el-form-item label="模板类型">
          <el-select v-model="templateForm.templateType" style="width:100%">
            <el-option label="持仓报表" value="POSITION" />
            <el-option label="损益报表" value="PNL" />
            <el-option label="风险报表" value="RISK" />
            <el-option label="交易报表" value="TRANSACTION" />
          </el-select>
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="templateForm.description" type="textarea" :rows="3" placeholder="请输入描述" />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button type="primary" :loading="templateLoading" @click="submitTemplate">保存</el-button>
        <el-button @click="templateDialogVisible = false">取消</el-button>
      </div>
    </el-dialog>

    <!-- 查看报表详情对话框 -->
    <el-dialog title="报表详情" :visible.sync="viewDialogVisible" width="600px">
      <el-descriptions :column="2" border>
        <el-descriptions-item label="报表名称">{{ viewForm.reportName }}</el-descriptions-item>
        <el-descriptions-item label="报表类型">{{ getReportTypeText(viewForm.reportType) }}</el-descriptions-item>
        <el-descriptions-item label="报表周期">{{ getReportPeriodText(viewForm.reportPeriod) }}</el-descriptions-item>
        <el-descriptions-item label="输出格式">{{ viewForm.outputFormat }}</el-descriptions-item>
        <el-descriptions-item label="开始日期">{{ viewForm.startDate }}</el-descriptions-item>
        <el-descriptions-item label="结束日期">{{ viewForm.endDate }}</el-descriptions-item>
        <el-descriptions-item label="生成人">{{ viewForm.generatedBy }}</el-descriptions-item>
        <el-descriptions-item label="生成时间">{{ viewForm.generateTime }}</el-descriptions-item>
        <el-descriptions-item label="文件大小">{{ formatFileSize(viewForm.fileSize) }}</el-descriptions-item>
        <el-descriptions-item label="状态">{{ viewForm.status === 'COMPLETED' ? '已完成' : '生成中' }}</el-descriptions-item>
      </el-descriptions>
      <div slot="footer">
        <el-button @click="viewDialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>

    <!-- 修改报表对话框 -->
    <el-dialog title="修改报表" :visible.sync="editReportDialogVisible" width="600px">
      <el-form :model="editReportForm" label-width="100px">
        <el-form-item label="报表名称">
          <el-input v-model="editReportForm.reportName" placeholder="请输入报表名称" />
        </el-form-item>
        <el-form-item label="报表类型">
          <el-select v-model="editReportForm.reportType" style="width:100%">
            <el-option label="持仓报表" value="POSITION" />
            <el-option label="损益报表" value="PNL" />
            <el-option label="风险报表" value="RISK" />
            <el-option label="自定义报表" value="CUSTOM" />
          </el-select>
        </el-form-item>
        <el-form-item label="报表周期">
          <el-select v-model="editReportForm.reportPeriod" style="width:100%">
            <el-option label="日报" value="DAILY" />
            <el-option label="周报" value="WEEKLY" />
            <el-option label="月报" value="MONTHLY" />
            <el-option label="季报" value="QUARTERLY" />
            <el-option label="年报" value="YEARLY" />
          </el-select>
        </el-form-item>
        <el-form-item label="开始日期">
          <el-date-picker v-model="editReportForm.startDate" type="date" value-format="yyyy-MM-dd" style="width:100%" />
        </el-form-item>
        <el-form-item label="结束日期">
          <el-date-picker v-model="editReportForm.endDate" type="date" value-format="yyyy-MM-dd" style="width:100%" />
        </el-form-item>
        <el-form-item label="输出格式">
          <el-select v-model="editReportForm.outputFormat" style="width:100%">
            <el-option label="PDF" value="PDF" />
            <el-option label="Excel" value="EXCEL" />
            <el-option label="Word" value="WORD" />
            <el-option label="CSV" value="CSV" />
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button type="primary" :loading="editReportLoading" @click="submitEditReport">保存</el-button>
        <el-button @click="editReportDialogVisible = false">取消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import {
  getReportList, addReport, deleteReport, batchDeleteReport,
  getTemplateList, addTemplate, editTemplate, deleteTemplate,
  getReportChartData, getReportDetail, editReport
} from '@/api/globalTreasurer/yspx'

export default {
  name: 'ReportAnalysis',
  data() {
    return {
      loading: false,
      generateLoading: false,
      templateLoading: false,
      activeTab: 'list',
      generateDialogVisible: false,
      templateDialogVisible: false,
      viewDialogVisible: false,
      editReportDialogVisible: false,
      editReportLoading: false,
      multiple: true,
      selectedIds: [],
      reportList: [],
      templateList: [],
      reportForm: { reportName: '', reportType: 'POSITION', reportPeriod: 'DAILY', startDate: null, endDate: null, outputFormats: ['PDF'] },
      templateForm: { templateId: null, templateName: '', templateType: 'POSITION', description: '' },
      viewForm: {},
      editReportForm: { reportId: null, reportName: '', reportType: 'POSITION', reportPeriod: 'DAILY', startDate: null, endDate: null, outputFormat: 'PDF' },
      charts: { position: null, pnl: null, var: null, return: null },
      _chartData: null
    }
  },
  created() {
    this.loadReportList()
    this.loadTemplateList()
  },
  methods: {
    refreshData() {
      this.loadReportList()
      this.loadTemplateList()
      if (this.activeTab === 'chart') this.loadChartData()
      this.$message.success('数据已刷新')
    },
    async loadReportList() {
      this.loading = true
      try {
        const res = await getReportList()
        if (res && [1, '1'].includes(res.code)) {
          this.reportList = (res.data && res.data.tlist) || []
        }
      } catch (e) { console.error(e) } finally { this.loading = false }
    },
    async loadTemplateList() {
      try {
        const res = await getTemplateList()
        if (res && [1, '1'].includes(res.code)) {
          this.templateList = (res.data && res.data.tlist) || []
        }
      } catch (e) { console.error(e) }
    },
    async loadChartData() {
      try {
        const res = await getReportChartData()
        if (res && [1, '1'].includes(res.code)) {
          this._chartData = res.data
          this.$nextTick(() => this.renderAllCharts(res.data))
        }
      } catch (e) { console.error(e) }
    },
    renderAllCharts(data) {
      if (!data) return
      this.renderPositionChart(data.positionDist)
      this.renderPnlChart(data.pnlDates, data.pnlValues)
      this.renderVarChart(data.varTypes, data.varValues)
      this.renderReturnChart(data.returnRanges, data.returnCounts)
    },
    renderPositionChart(dist) {
      if (!this.$refs.positionChart) return
      if (!this.charts.position) this.charts.position = echarts.init(this.$refs.positionChart)
      this.charts.position.setOption({
        tooltip: { trigger: 'item', formatter: '{b}: {d}%' },
        legend: { orient: 'vertical', right: '5%', top: 'center' },
        series: [{ type: 'pie', radius: ['40%', '68%'], center: ['38%', '50%'],
          data: (dist || []).map(d => ({ name: d.name, value: parseFloat(d.value) || 0 })),
          label: { formatter: '{b}\n{d}%' },
          emphasis: { itemStyle: { shadowBlur: 10, shadowColor: 'rgba(0,0,0,0.4)' } }
        }]
      })
    },
    renderPnlChart(dates, values) {
      if (!this.$refs.pnlChart) return
      if (!this.charts.pnl) this.charts.pnl = echarts.init(this.$refs.pnlChart)
      this.charts.pnl.setOption({
        tooltip: { trigger: 'axis' },
        xAxis: { type: 'category', data: dates || [], axisLabel: { fontSize: 11 } },
        yAxis: { type: 'value', axisLabel: { formatter: v => (v / 10000).toFixed(1) + '万' } },
        series: [{ type: 'line', data: (values || []).map(v => parseFloat(v) || 0),
          smooth: true, areaStyle: { opacity: 0.15 },
          lineStyle: { color: '#409eff' }, itemStyle: { color: '#409eff' },
          markLine: { data: [{ type: 'average', name: '均值' }] }
        }]
      })
    },
    renderVarChart(types, values) {
      if (!this.$refs.varChart) return
      if (!this.charts.var) this.charts.var = echarts.init(this.$refs.varChart)
      this.charts.var.setOption({
        tooltip: { trigger: 'axis' },
        xAxis: { type: 'category', data: types || [] },
        yAxis: { type: 'value', axisLabel: { formatter: v => (v / 10000).toFixed(0) + '万' } },
        series: [{ type: 'bar', data: (values || []).map(v => parseFloat(v) || 0),
          itemStyle: { color: (p) => ['#409eff','#67c23a','#e6a23c','#f56c6c'][p.dataIndex] || '#409eff' },
          barMaxWidth: 60
        }]
      })
    },
    renderReturnChart(ranges, counts) {
      if (!this.$refs.returnChart) return
      if (!this.charts.return) this.charts.return = echarts.init(this.$refs.returnChart)
      this.charts.return.setOption({
        tooltip: { trigger: 'axis' },
        xAxis: { type: 'category', data: ranges || [] },
        yAxis: { type: 'value', name: '频次' },
        series: [{ type: 'bar', data: counts || [],
          itemStyle: { color: '#67c23a' }, barMaxWidth: 60
        }]
      })
    },
    handleTabClick(tab) {
      if (tab.name === 'chart') {
        if (!this._chartData) {
          this.loadChartData()
        } else {
          this.$nextTick(() => {
            Object.values(this.charts).forEach(c => c && c.resize())
            this.renderAllCharts(this._chartData)
          })
        }
      }
    },
    generatePositionReport() { this.openGenerateDialog('POSITION') },
    generatePnLReport() { this.openGenerateDialog('PNL') },
    generateRiskReport() { this.openGenerateDialog('RISK') },
    generateCustomReport() { this.openGenerateDialog('POSITION') },
    openGenerateDialog(type) {
      const today = new Date().toISOString().slice(0, 10)
      const typeMap = { POSITION: '持仓报表', PNL: '损益报表', RISK: '风险报表' }
      this.reportForm = {
        reportName: today + (typeMap[type] || '自定义报表'),
        reportType: type, reportPeriod: 'MONTHLY',
        startDate: today.slice(0, 7) + '-01', endDate: today, outputFormats: ['PDF']
      }
      this.generateDialogVisible = true
    },
    async submitGenerateReport() {
      if (!this.reportForm.reportName) { this.$message.warning('请输入报表名称'); return }
      this.generateLoading = true
      try {
        const params = {
          reportName: this.reportForm.reportName,
          reportType: this.reportForm.reportType,
          reportPeriod: this.reportForm.reportPeriod,
          startDate: this.reportForm.startDate,
          endDate: this.reportForm.endDate,
          outputFormat: (this.reportForm.outputFormats || ['PDF']).join(',')
        }
        const res = await addReport(params)
        if (res && [1, '1'].includes(res.code)) {
          this.$message.success('报表生成成功')
          this.generateDialogVisible = false
          this.loadReportList()
        } else {
          this.$message.error((res && res.msg) || '生成失败')
        }
      } catch (e) { this.$message.error('生成失败') } finally { this.generateLoading = false }
    },
    handleAddReport() {
      this.reportForm = { reportName: '', reportType: 'POSITION', reportPeriod: 'DAILY', startDate: null, endDate: null, outputFormats: [] }
      this.generateDialogVisible = true
    },
    getReportPeriodText(period) {
      return { DAILY: '日报', WEEKLY: '周报', MONTHLY: '月报', QUARTERLY: '季报', YEARLY: '年报' }[period] || period
    },
    formatDate(val) {
      if (!val) return '-'
      const d = new Date(val)
      if (isNaN(d.getTime())) return val
      const y = d.getFullYear()
      const m = String(d.getMonth() + 1).padStart(2, '0')
      const day = String(d.getDate()).padStart(2, '0')
      return `${y}-${m}-${day}`
    },
    getReportTypeText(type) {
      return { POSITION: '持仓报表', PNL: '损益报表', RISK: '风险报表', CUSTOM: '自定义报表', TRANSACTION: '交易报表' }[type] || type
    },
    formatFileSize(size) {
      if (!size) return '-'
      if (size < 1024) return size + ' B'
      if (size < 1024 * 1024) return (size / 1024).toFixed(1) + ' KB'
      return (size / 1024 / 1024).toFixed(1) + ' MB'
    },
    async handleViewReport(row) {
      try {
        const res = await getReportDetail(row.reportId)
        if (res && [1, '1'].includes(res.code)) {
          this.viewForm = res.data || {}
          this.viewDialogVisible = true
        } else {
          this.$message.error((res && res.msg) || '获取详情失败')
        }
      } catch (e) { this.$message.error('获取详情失败') }
    },
    async handleEditReport(row) {
      try {
        const res = await getReportDetail(row.reportId)
        if (res && [1, '1'].includes(res.code)) {
          const d = res.data || {}
          this.editReportForm = {
            reportId: d.reportId,
            reportName: d.reportName || '',
            reportType: d.reportType || 'POSITION',
            reportPeriod: d.reportPeriod || 'DAILY',
            startDate: d.startDate || null,
            endDate: d.endDate || null,
            outputFormat: d.outputFormat || 'PDF'
          }
          this.editReportDialogVisible = true
        } else {
          this.$message.error((res && res.msg) || '获取详情失败')
        }
      } catch (e) { this.$message.error('获取详情失败') }
    },
    async submitEditReport() {
      if (!this.editReportForm.reportName) { this.$message.warning('请输入报表名称'); return }
      this.editReportLoading = true
      try {
        const { reportId, reportName, reportType, reportPeriod, startDate, endDate, outputFormat } = this.editReportForm
        const res = await editReport(reportId, { reportName, reportType, reportPeriod, startDate, endDate, outputFormat })
        if (res && [1, '1'].includes(res.code)) {
          this.$message.success('修改成功')
          this.editReportDialogVisible = false
          this.loadReportList()
        } else {
          this.$message.error((res && res.msg) || '修改失败')
        }
      } catch (e) { this.$message.error('修改失败') } finally { this.editReportLoading = false }
    },
    async handleDownloadReport(row) {
      try {
        const token = this.$store.getters['user/token'] || this.$store.getters.token || ''
        const res = await fetch(`/vab-mock-server/qqsk/derivatives/report/download/${row.reportId}`, {
          method: 'GET', headers: { token, Authorization: `Bearer ${token}` }
        })
        if (!res.ok) { this.$message.error('下载失败，请重新登录后重试'); return }
        const blob = await res.blob()
        const url = URL.createObjectURL(blob)
        const a = document.createElement('a')
        a.href = url
        a.download = row.reportName + '.csv'
        document.body.appendChild(a); a.click(); document.body.removeChild(a)
        URL.revokeObjectURL(url)
        this.$message.success('下载成功')
      } catch (e) { this.$message.error('下载失败') }
    },
    async handleDeleteReport(row) {
      try { await this.$confirm(`确认删除报表"${row.reportName}"？`, '提示', { type: 'warning' }) } catch { return }
      try {
        const res = await deleteReport(row.reportId)
        if (res && [1, '1'].includes(res.code)) { this.$message.success('删除成功'); this.loadReportList() }
        else this.$message.error((res && res.msg) || '删除失败')
      } catch (e) { this.$message.error('删除失败') }
    },
    async handleBatchDelete() {
      if (!this.selectedIds.length) return
      try { await this.$confirm(`确认删除选中的 ${this.selectedIds.length} 个报表？`, '提示', { type: 'warning' }) } catch { return }
      try {
        const res = await batchDeleteReport(this.selectedIds.join(','))
        if (res && [1, '1'].includes(res.code)) { this.$message.success('批量删除成功'); this.loadReportList() }
        else this.$message.error((res && res.msg) || '删除失败')
      } catch (e) { this.$message.error('删除失败') }
    },
    handleSelectionChange(selection) {
      this.selectedIds = selection.map(item => item.reportId)
      this.multiple = !selection.length
    },
    handleAddTemplate() {
      this.templateForm = { templateId: null, templateName: '', templateType: 'POSITION', description: '' }
      this.templateDialogVisible = true
    },
    handleEditTemplate(row) {
      this.templateForm = { templateId: row.templateId, templateName: row.templateName, templateType: row.templateType, description: row.description }
      this.templateDialogVisible = true
    },
    async handleUseTemplate(row) {
      this.openGenerateDialog(row.templateType)
      this.reportForm.reportName = new Date().toISOString().slice(0, 10) + '-' + row.templateName
    },
    async handleDeleteTemplate(row) {
      try { await this.$confirm(`确认删除模板"${row.templateName}"？`, '提示', { type: 'warning' }) } catch { return }
      try {
        const res = await deleteTemplate(row.templateId)
        if (res && [1, '1'].includes(res.code)) { this.$message.success('删除成功'); this.loadTemplateList() }
        else this.$message.error((res && res.msg) || '删除失败')
      } catch (e) { this.$message.error('删除失败') }
    },
    async submitTemplate() {
      if (!this.templateForm.templateName) { this.$message.warning('请输入模板名称'); return }
      this.templateLoading = true
      try {
        const params = { templateName: this.templateForm.templateName, templateType: this.templateForm.templateType, description: this.templateForm.description }
        let res
        if (this.templateForm.templateId) {
          res = await editTemplate(this.templateForm.templateId, params)
        } else {
          res = await addTemplate(params)
        }
        if (res && [1, '1'].includes(res.code)) {
          this.$message.success(this.templateForm.templateId ? '编辑成功' : '新建成功')
          this.templateDialogVisible = false
          this.loadTemplateList()
        } else {
          this.$message.error((res && res.msg) || '操作失败')
        }
      } catch (e) { this.$message.error('操作失败') } finally { this.templateLoading = false }
    },
    getReportTypeTag(type) {
      return { POSITION: 'primary', PNL: 'success', RISK: 'warning', TRANSACTION: 'info' }[type] || ''
    },
    getReportTypeText(type) {
      return { POSITION: '持仓报表', PNL: '损益报表', RISK: '风险报表', TRANSACTION: '交易报表' }[type] || type
    },
    getReportPeriodText(period) {
      return { DAILY: '日报', WEEKLY: '周报', MONTHLY: '月报', QUARTERLY: '季报', YEARLY: '年报' }[period] || period
    },


    formatFileSize(size) {
      if (!size) return '0 B'
      const units = ['B', 'KB', 'MB', 'GB']
      let idx = 0, s = size
      while (s >= 1024 && idx < units.length - 1) { s /= 1024; idx++ }
      return s.toFixed(2) + ' ' + units[idx]
    }
  },
  beforeDestroy() {
    Object.values(this.charts).forEach(c => { if (c) c.dispose() })
  }
}
</script>

<style scoped>
.report-analysis-wrapper { height: 100%; }

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-weight: bold;
}

.report-actions { margin-bottom: 20px; }

.action-card {
  cursor: pointer;
  border-radius: 8px;
  padding: 24px 16px;
  height: 130px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  transition: all 0.25s;
  user-select: none;
}

.action-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 6px 18px rgba(0, 0, 0, 0.18);
  filter: brightness(1.06);
}

.action-card--blue  { background: linear-gradient(135deg, #409eff, #66b1ff); color: #fff; }
.action-card--green { background: linear-gradient(135deg, #67c23a, #85ce61); color: #fff; }
.action-card--orange{ background: linear-gradient(135deg, #e6a23c, #ebb563); color: #fff; }
.action-card--red   { background: linear-gradient(135deg, #f56c6c, #f78989); color: #fff; }

.action-icon { font-size: 34px; margin-bottom: 10px; }

.action-title {
  font-size: 16px;
  font-weight: bold;
  margin-bottom: 4px;
}

.action-desc { font-size: 12px; opacity: 0.85; }

.report-tabs { margin-top: 20px; }

.table-header { margin-bottom: 15px; }

.chart-card { min-height: 340px; }
</style>
