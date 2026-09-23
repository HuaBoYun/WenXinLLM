<template>
  <div class="fin-page" :style="themeVars">
    <div class="page-header">
      <div class="page-header-left"><i class="el-icon-coin"></i><span>融资记录台账</span></div>
      <div class="page-header-desc">登记各级企业融资信息，监控融资规模与到期预警</div>
    </div>

    <!-- 统计卡片 -->
    <el-row :gutter="16" class="mb-16">
      <el-col :span="6" v-for="s in statCards" :key="s.label">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-inner">
            <div class="stat-icon" :style="{ background: s.color + '18', color: s.color }"><i :class="s.icon"></i></div>
            <div class="stat-info">
              <div class="stat-value">{{ s.value }}<span v-if="s.unit" class="stat-unit">{{ s.unit }}</span></div>
              <div class="stat-label">{{ s.label }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 图表区域 -->
    <el-row :gutter="16" class="mb-16">
      <el-col :span="12">
        <el-card shadow="hover">
          <div slot="header"><span>融资类型结构分布</span></div>
          <div ref="typeChart" style="height:220px"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="hover">
          <div slot="header"><span>融资期限结构分布（亿元）</span></div>
          <div ref="termChart" style="height:220px"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 查询区 -->
    <el-card shadow="never" class="mb-16">
      <el-form :model="queryForm" :inline="true" size="small">
        <el-form-item label="企业名称">
          <el-input v-model="queryForm.companyName" placeholder="请输入企业名称" clearable style="width:180px" />
        </el-form-item>
        <el-form-item label="融资类型">
          <el-select v-model="queryForm.financingType" placeholder="请选择" clearable style="width:130px">
            <el-option label="银行贷款" value="BANK_LOAN" />
            <el-option label="债券" value="BOND" />
            <el-option label="票据" value="NOTE" />
            <el-option label="政策贷款" value="POLICY_LOAN" />
          </el-select>
        </el-form-item>
        <el-form-item label="融资状态">
          <el-select v-model="queryForm.financingStatus" placeholder="请选择" clearable style="width:120px">
            <el-option label="正常" value="ACTIVE" />
            <el-option label="逾期" value="OVERDUE" />
            <el-option label="已还清" value="REPAID" />
            <el-option label="已到期" value="EXPIRED" />
          </el-select>
        </el-form-item>
        <el-form-item label="风险等级">
          <el-select v-model="queryForm.riskLevel" placeholder="请选择" clearable style="width:110px">
            <el-option label="高风险" value="HIGH" />
            <el-option label="中风险" value="MEDIUM" />
            <el-option label="低风险" value="LOW" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleQuery">查询</el-button>
          <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
          <el-button type="success" icon="el-icon-plus" @click="handleAdd">新增</el-button>
          <el-button type="info" icon="el-icon-download" @click="handleExport">导出</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 数据表格 -->
    <el-card shadow="never">
      <div slot="header">
        <span>融资台账列表</span>
        <span style="font-size:13px;color:#909399;margin-left:10px">（共 {{ total }} 条）</span>
      </div>
      <el-table v-loading="loading" :data="list" border stripe style="width:100%"
        :row-class-name="getRowClass">
        <el-table-column label="借款企业" prop="companyName" min-width="160" show-overflow-tooltip />
        <el-table-column label="贷款机构" prop="lender" min-width="140" show-overflow-tooltip />
        <el-table-column label="融资类型" prop="financingType" width="110" align="center">
          <template slot-scope="scope">
            <el-tag :type="typeTagMap[scope.row.financingType] || 'info'" size="mini">
              {{ typeTextMap[scope.row.financingType] || scope.row.financingType }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="融资金额(万元)" prop="financingAmount" width="130" align="right">
          <template slot-scope="scope">
            <span :style="{fontWeight:'600',color:ipSecondary}">{{ scope.row.financingAmount ? Number(scope.row.financingAmount).toLocaleString() : '-' }}</span>
          </template>
        </el-table-column>
        <el-table-column label="利率(%)" prop="interestRate" width="80" align="center">
          <template slot-scope="scope">
            <span :style="{ color: scope.row.interestRate > 5 ? '#CF1322' : '#303133' }">{{ scope.row.interestRate }}</span>
          </template>
        </el-table-column>
        <el-table-column label="担保方式" prop="guaranteeMethod" width="100" align="center" />
        <el-table-column label="起始日期" prop="startDate" width="100" align="center" />
        <el-table-column label="到期日期" prop="maturityDate" width="100" align="center">
          <template slot-scope="scope">
            <span :style="{ color: isNearExpiry(scope.row.maturityDate) ? '#CF1322' : '' }">
              {{ scope.row.maturityDate }}
            </span>
          </template>
        </el-table-column>
        <el-table-column label="状态" prop="financingStatus" width="80" align="center">
          <template slot-scope="scope">
            <el-tag :type="getComputedStatusTag(scope.row)" size="mini">
              {{ getComputedStatusText(scope.row) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="风险等级" prop="riskLevel" width="90" align="center">
          <template slot-scope="scope">
            <el-tag :type="riskTagMap[scope.row.riskLevel]" size="mini">{{ riskTextMap[scope.row.riskLevel] || scope.row.riskLevel || '-' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="140" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" type="text" @click="handleView(scope.row)">查看</el-button>
            <el-button size="mini" type="text" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button size="mini" type="text" style="color:#F56C6C" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div style="margin-top:16px;text-align:right">
        <el-pagination background layout="total, sizes, prev, pager, next"
          :total="total" :page-sizes="[10, 20, 50]"
          :current-page.sync="queryForm.pageNumber" :page-size.sync="queryForm.pageSize"
          @current-change="getList" @size-change="getList" />
      </div>
    </el-card>

    <!-- 新增/编辑对话框 -->
    <el-dialog :title="dialogType === 'add' ? '新增融资记录' : dialogType === 'edit' ? '编辑融资记录' : '查看融资记录'" :visible.sync="dialogVisible" width="650px" :close-on-click-modal="false">
      <el-form ref="formRef" :model="formData" :rules="dialogType !== 'view' ? formRules : {}" label-width="100px" :disabled="dialogType === 'view'">
        <el-row :gutter="16">
          <el-col :span="12"><el-form-item label="企业名称" prop="companyName"><el-input v-model="formData.companyName" placeholder="请输入" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="贷款机构" prop="lender"><el-input v-model="formData.lender" placeholder="请输入" /></el-form-item></el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12"><el-form-item label="融资类型" prop="financingType"><el-select v-model="formData.financingType" placeholder="请选择" style="width:100%"><el-option label="银行贷款" value="BANK_LOAN" /><el-option label="债券" value="BOND" /><el-option label="票据" value="NOTE" /><el-option label="政策贷款" value="POLICY_LOAN" /></el-select></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="融资金额" prop="financingAmount"><el-input-number v-model="formData.financingAmount" :min="0" :max="99999999" :precision="2" style="width:100%" /></el-form-item></el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12"><el-form-item label="利率(%)" prop="interestRate"><el-input-number v-model="formData.interestRate" :min="0" :max="99" :precision="2" style="width:100%" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="担保方式" prop="guaranteeMethod"><el-select v-model="formData.guaranteeMethod" placeholder="请选择" style="width:100%"><el-option label="信用" value="信用" /><el-option label="抵押" value="抵押" /><el-option label="质押" value="质押" /><el-option label="保证" value="保证" /><el-option label="组合担保" value="组合担保" /></el-select></el-form-item></el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12"><el-form-item label="起始日期" prop="startDate"><el-date-picker v-model="formData.startDate" type="date" value-format="yyyy-MM-dd" placeholder="请选择" style="width:100%" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="到期日期" prop="maturityDate"><el-date-picker v-model="formData.maturityDate" type="date" value-format="yyyy-MM-dd" placeholder="请选择" style="width:100%" /></el-form-item></el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12"><el-form-item label="风险等级" prop="riskLevel"><el-select v-model="formData.riskLevel" placeholder="请选择" style="width:100%"><el-option label="高风险" value="HIGH" /><el-option label="中风险" value="MEDIUM" /><el-option label="低风险" value="LOW" /></el-select></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="期限(月)" prop="termMonths"><el-input v-model="formData.termMonths" placeholder="请输入" type="number" /></el-form-item></el-col>
        </el-row>
        <el-form-item label="备注" prop="remark"><el-input v-model="formData.remark" type="textarea" :rows="2" placeholder="请输入" /></el-form-item>
      </el-form>
      <div slot="footer" v-if="dialogType !== 'view'">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import { getFinancingRecordList, addFinancingRecord, updateFinancingRecord, deleteFinancingRecord, getFinancialRiskStatistics } from '@/api/stateAssets/financialRiskPenetration'
import { investThemeMixin } from '../../themeMixin'
export default {
  name: 'FinancialRiskFinancing',
  mixins: [investThemeMixin],
  data() {
    return {
      loading: false,
      total: 0,
      queryForm: { companyName: '', financingType: '', financingStatus: '', riskLevel: '', pageNumber: 1, pageSize: 20 },
      statCards: [
        { label: '融资总额', value: '0', unit: '万元', icon: 'el-icon-coin', color: '#0050A0' },
        { label: '平均利率', value: '0', unit: '%', icon: 'el-icon-s-finance', color: '#1677FF' },
        { label: '90天内到期', value: '0', unit: '万元', icon: 'el-icon-time', color: '#FA8C16' },
        { label: '逾期融资', value: '0', unit: '万元', icon: 'el-icon-warning', color: '#CF1322' }
      ],
      typeTagMap: { BANK_LOAN: 'primary', BOND: 'success', NOTE: 'warning', POLICY_LOAN: 'info', TRUST: '', LEASE: '' },
      typeTextMap: { BANK_LOAN: '银行贷款', BOND: '债券', NOTE: '票据', POLICY_LOAN: '政策贷款', TRUST: '信托', LEASE: '融资租赁' },
      statusTagMap: { ACTIVE: 'success', OVERDUE: 'danger', REPAID: 'info', EXPIRED: 'warning' },
      statusTextMap: { ACTIVE: '正常', OVERDUE: '逾期', REPAID: '已还清', EXPIRED: '已到期' },
      riskTagMap: { HIGH: 'danger', MEDIUM: 'warning', LOW: 'success' },
      riskTextMap: { HIGH: '高风险', MEDIUM: '中风险', LOW: '低风险' },
      charts: [],
      list: [],
      dialogVisible: false,
      dialogType: 'add',
      formData: { companyName: '', lender: '', financingType: '', financingAmount: null, interestRate: null, startDate: '', maturityDate: '', termMonths: '', guaranteeMethod: '', riskLevel: 'LOW', remark: '' },
      formRules: {
        companyName: [{ required: true, message: '请输入企业名称', trigger: 'blur' }],
        financingAmount: [{ required: true, message: '请输入融资金额', trigger: 'blur' }],
        financingType: [{ required: true, message: '请选择融资类型', trigger: 'change' }]
      }
    }
  },
  created() {
    // 主题色覆盖 data() 中的初始值
    this.statCards[0].color = this.ipSecondary
    this.statCards[1].color = this.ipBright
    this.getList(); this.getStatistics()
  },
  mounted() { this.$nextTick(() => { this.initTypeChart(); this.initTermChart() }) },
  beforeDestroy() { this.charts.forEach(c => c && c.dispose()) },
  methods: {
    getList() {
      this.loading = true
      getFinancingRecordList(this.queryForm).then(res => {
        if (res.data) {
          this.list = res.data.tlist || []
          this.total = res.data.totalRecord || 0
        }
      }).catch(() => { this.$message.error('查询融资记录失败') }).finally(() => { this.loading = false })
    },
    getStatistics() {
      getFinancialRiskStatistics().then(res => {
        if (res.data) {
          const d = res.data
          this.statCards[0].value = d.totalAmount ? Number(d.totalAmount).toLocaleString() : '0'
          this.statCards[1].value = d.avgRate ? Number(d.avgRate).toFixed(2) : '0'
          this.statCards[2].value = d.nearExpiryAmount ? Number(d.nearExpiryAmount).toLocaleString() : '0'
          this.statCards[3].value = d.overdueAmount ? Number(d.overdueAmount).toLocaleString() : '0'
          if (d.typeDistribution) this.updateTypeChart(d.typeDistribution)
          if (d.termDistribution) this.updateTermChart(d.termDistribution)
        }
      }).catch(() => {})
    },
    handleQuery() { this.queryForm.pageNumber = 1; this.getList() },
    resetQuery() { this.queryForm = { companyName: '', financingType: '', financingStatus: '', riskLevel: '', pageNumber: 1, pageSize: 20 }; this.getList() },
    handleAdd() { this.dialogType = 'add'; this.formData = { companyName: '', lender: '', financingType: '', financingAmount: null, interestRate: null, startDate: '', maturityDate: '', termMonths: '', guaranteeMethod: '', riskLevel: 'LOW', remark: '' }; this.dialogVisible = true },
    handleView(row) { this.dialogType = 'view'; this.formData = { ...row }; this.dialogVisible = true },
    handleEdit(row) { this.dialogType = 'edit'; this.formData = { ...row }; this.dialogVisible = true },
    handleDelete(row) {
      this.$confirm('确认删除该融资记录？', '提示', { type: 'warning' }).then(() => {
        deleteFinancingRecord(row.financingId).then(res => {
          if (res.result === 200) { this.$message.success('删除成功'); this.getList(); this.getStatistics() }
          else this.$message.error(res.msg || '删除失败')
        }).catch(() => { this.$message.error('删除失败') })
      }).catch(() => {})
    },
    handleSubmit() {
      this.$refs.formRef && this.$refs.formRef.validate(valid => {
        if (!valid) return
        const api = this.dialogType === 'add' ? addFinancingRecord : updateFinancingRecord
        api(this.formData).then(res => {
          if (res.result === 200) { this.$message.success(this.dialogType === 'add' ? '新增成功' : '更新成功'); this.dialogVisible = false; this.getList(); this.getStatistics() }
          else this.$message.error(res.msg || '操作失败')
        }).catch(() => { this.$message.error('操作失败') })
      })
    },
    handleExport() {
      const loading = this.$loading({ lock: true, text: '正在导出数据...', background: 'rgba(0, 0, 0, 0.7)' })
      const exportParams = {}
      if (this.queryForm.companyName) exportParams.companyName = this.queryForm.companyName
      if (this.queryForm.financingType) exportParams.financingType = this.queryForm.financingType
      if (this.queryForm.financingStatus) exportParams.financingStatus = this.queryForm.financingStatus
      if (this.queryForm.riskLevel) exportParams.riskLevel = this.queryForm.riskLevel
      import('@/api/stateAssets/financialRiskPenetration').then(api => {
        // 使用列表接口获取全部数据并前端生成CSV下载
        api.getFinancingRecordList({ ...exportParams, pageNumber: 1, pageSize: 10000 }).then(res => {
          loading.close()
          if (res.data && res.data.tlist) {
            const rows = res.data.tlist
            const headers = ['借款企业', '贷款机构', '融资类型', '融资金额(万元)', '利率(%)', '担保方式', '起始日期', '到期日期', '状态', '风险等级']
            const csvRows = [headers.join(',')]
            rows.forEach(r => {
              csvRows.push([
                r.companyName || '', r.lender || '', this.typeTextMap[r.financingType] || r.financingType || '',
                r.financingAmount || '', r.interestRate || '', r.guaranteeMethod || '',
                r.startDate || '', r.maturityDate || '',
                this.getComputedStatusText(r), this.riskTextMap[r.riskLevel] || r.riskLevel || ''
              ].map(v => '"' + String(v).replace(/"/g, '""') + '"').join(','))
            })
            const blob = new Blob(['\uFEFF' + csvRows.join('\n')], { type: 'text/csv;charset=utf-8' })
            const url = window.URL.createObjectURL(blob)
            const link = document.createElement('a')
            link.href = url
            link.download = `融资台账数据_${new Date().getTime()}.csv`
            link.click()
            window.URL.revokeObjectURL(url)
            this.$message.success('导出成功')
          } else {
            this.$message.warning('无数据可导出')
          }
        }).catch(() => { loading.close(); this.$message.error('导出失败') })
      })
    },
    isNearExpiry(date) {
      if (!date) return false
      const d = new Date(date), now = new Date()
      return (d - now) / (1000 * 3600 * 24) <= 90
    },
    // 根据到期日期动态计算状态
    getComputedStatusText(row) {
      if (row.financingStatus === 'REPAID') return '已还清'
      if (row.financingStatus === 'OVERDUE') return '逾期'
      if (row.maturityDate) {
        const maturity = new Date(row.maturityDate)
        const today = new Date()
        today.setHours(0, 0, 0, 0)
        if (maturity < today) return '已到期'
      }
      return this.statusTextMap[row.financingStatus] || row.financingStatus || '正常'
    },
    getComputedStatusTag(row) {
      if (row.financingStatus === 'REPAID') return 'info'
      if (row.financingStatus === 'OVERDUE') return 'danger'
      if (row.maturityDate) {
        const maturity = new Date(row.maturityDate)
        const today = new Date()
        today.setHours(0, 0, 0, 0)
        if (maturity < today) return 'warning'
      }
      return this.statusTagMap[row.financingStatus] || 'success'
    },
    getRowClass({ row }) {
      if (row.financingStatus === 'OVERDUE') return 'overdue-row'
      if (row.riskLevel === 'HIGH') return 'high-risk-row'
      return ''
    },
    initTypeChart() {
      const c = echarts.init(this.$refs.typeChart); this.charts.push(c)
      const colors = [this.ipSecondary, this.ipBright, '#FA8C16', '#52C41A', '#722ED1', '#13C2C2', '#CF1322', '#EB2F96']
      c.setOption({ color: colors, tooltip: { trigger: 'item' }, legend: { bottom: 0, itemWidth: 12 }, series: [{ type: 'pie', radius: ['35%', '65%'], center: ['50%', '45%'], data: [], label: { formatter: '{b}\n{d}%' } }] })
    },
    updateTypeChart(data) {
      const colorMap = { BANK_LOAN: this.ipSecondary, BOND: this.ipBright, NOTE: '#FA8C16', POLICY_LOAN: '#52C41A', TRUST: '#722ED1', LEASE: '#13C2C2' }
      const fallbackColors = [this.ipSecondary, this.ipBright, '#FA8C16', '#52C41A', '#722ED1', '#13C2C2', '#CF1322', '#EB2F96']
      let idx = 0
      const chartData = Object.entries(data).map(([k, v]) => ({ value: v, name: this.typeTextMap[k] || k, itemStyle: { color: colorMap[k] || fallbackColors[idx++ % fallbackColors.length] } }))
      if (this.charts[0]) this.charts[0].setOption({ series: [{ data: chartData }] })
    },
    initTermChart() {
      const c = echarts.init(this.$refs.termChart); this.charts.push(c)
      c.setOption({ tooltip: { trigger: 'axis' }, grid: { left: 50, right: 20, top: 30, bottom: 30 }, xAxis: { type: 'category', data: ['1年以内', '1-3年', '3-5年', '5年以上'] }, yAxis: { type: 'value', name: '亿元' }, series: [{ type: 'bar', barWidth: 40, data: [], label: { show: true, position: 'top', formatter: '{c}亿' } }] })
    },
    updateTermChart(data) {
      const colors = ['#CF1322', '#FA8C16', this.ipBright, '#52C41A']
      const chartData = (data || []).map((v, i) => ({ value: v, itemStyle: { color: colors[i] || '#909399' } }))
      if (this.charts[1]) this.charts[1].setOption({ series: [{ data: chartData }] })
    }
  }
}
</script>

<style lang="scss" scoped>
.fin-page { padding: 16px; background: #F0F2F5; min-height: calc(100vh - 84px); }
.mb-16 { margin-bottom: 16px; }
.page-header { display: flex; align-items: center; justify-content: space-between; margin-bottom: 14px; padding: 14px 20px; background: linear-gradient(135deg, var(--ip-primary, #003A6C) 0%, var(--ip-secondary, #0050A0) 60%, var(--ip-bright, #1677FF) 100%); border-radius: 6px; color: #fff; }
.page-header-left { display: flex; align-items: center; font-size: 16px; font-weight: 600; i { font-size: 22px; margin-right: 10px; } }
.page-header-desc { font-size: 13px; opacity: 0.85; }
.stat-card { border-radius: 6px; }
.stat-inner { display: flex; align-items: center; }
.stat-icon { width: 44px; height: 44px; border-radius: 10px; display: flex; align-items: center; justify-content: center; margin-right: 12px; i { font-size: 22px; } }
.stat-value { font-size: 20px; font-weight: 700; color: #303133; line-height: 1; .stat-unit { font-size: 12px; font-weight: 400; color: #909399; margin-left: 2px; } }
.stat-label { font-size: 12px; color: #909399; margin-top: 4px; }
::v-deep .el-table th { background: var(--ip-light-bg, #EBF1FF) !important; color: var(--ip-secondary, #0050A0); }
::v-deep .el-table .overdue-row { background: #FFF1F0 !important; }
::v-deep .el-table .high-risk-row { background: #FFF7E6 !important; }
::v-deep .el-card { border-radius: 6px; }
</style>
