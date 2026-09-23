<template>
  <div class="fin-page" :style="themeVars">
    <div class="page-header">
      <div class="page-header-left"><i class="el-icon-s-data"></i><span>衍生品业务监控</span></div>
      <div class="page-header-desc">监控衍生品持仓规模、浮动盈亏、到期分布和对手方风险</div>
    </div>

    <!-- 统计卡 -->
    <el-row :gutter="16" class="mb-16">
      <el-col :span="6" v-for="s in statCards" :key="s.label">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-inner">
            <div class="stat-icon" :style="{ background: s.color + '18', color: s.color }"><i :class="s.icon"></i></div>
            <div class="stat-info">
              <div class="stat-value" :style="{ color: s.valueColor || '#303133' }">{{ s.value }}<span v-if="s.unit" class="stat-unit">{{ s.unit }}</span></div>
              <div class="stat-label">{{ s.label }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 图表 -->
    <el-row :gutter="16" class="mb-16">
      <el-col :span="12">
        <el-card shadow="hover">
          <div slot="header"><span>按品种分类分布</span></div>
          <div ref="typeChart" style="height:220px"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="hover">
          <div slot="header"><span>到期分布（按季度，名义金额，万元）</span></div>
          <div ref="maturityChart" style="height:220px"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 查询 -->
    <el-card shadow="never" class="mb-16">
      <el-form :model="queryForm" :inline="true" size="small">
        <el-form-item label="企业名称">
          <el-input v-model="queryForm.companyName" placeholder="请输入企业名称" clearable style="width:160px" />
        </el-form-item>
        <el-form-item label="产品类型">
          <el-select v-model="queryForm.productType" placeholder="请选择" clearable style="width:120px">
            <el-option label="期货" value="FUTURES" /><el-option label="期权" value="OPTIONS" />
            <el-option label="互换" value="SWAP" /><el-option label="远期" value="FORWARD" />
          </el-select>
        </el-form-item>
        <el-form-item label="风险等级">
          <el-select v-model="queryForm.riskLevel" placeholder="请选择" clearable style="width:110px">
            <el-option label="高风险" value="HIGH" /><el-option label="中风险" value="MEDIUM" /><el-option label="低风险" value="LOW" />
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

    <!-- 表格 -->
    <el-card shadow="never">
      <div slot="header">
        <span>衍生品持仓列表</span>
        <span style="font-size:13px;color:#909399;margin-left:10px">（共 {{ total }} 条）</span>
      </div>
      <el-table v-loading="loading" :data="list" border stripe style="width:100%"
        :row-class-name="getRowClass">
        <el-table-column label="持仓企业" prop="companyName" min-width="150" show-overflow-tooltip />
        <el-table-column label="产品名称" prop="productName" min-width="160" show-overflow-tooltip />
        <el-table-column label="品种" prop="productType" width="80" align="center">
          <template slot-scope="scope">
            <el-tag :type="typeTagMap[scope.row.productType]" size="mini">
              {{ typeTextMap[scope.row.productType] || scope.row.productType }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="名义金额(万元)" prop="notionalAmount" width="130" align="right">
          <template slot-scope="scope">
            <span :style="{fontWeight:'600',color:ipSecondary}">{{ scope.row.notionalAmount ? Number(scope.row.notionalAmount).toLocaleString() : '-' }}</span>
          </template>
        </el-table-column>
        <el-table-column label="市值(万元)" prop="marketValue" width="110" align="right">
          <template slot-scope="scope">
            {{ scope.row.marketValue ? Number(scope.row.marketValue).toLocaleString() : '-' }}
          </template>
        </el-table-column>
        <el-table-column label="浮动盈亏(万元)" prop="profitLoss" width="130" align="right">
          <template slot-scope="scope">
            <span :style="{ color: Number(scope.row.profitLoss) >= 0 ? '#52C41A' : '#CF1322', fontWeight: 600 }">
              {{ Number(scope.row.profitLoss) >= 0 ? '+' : '' }}{{ scope.row.profitLoss }}
            </span>
          </template>
        </el-table-column>
        <el-table-column label="到期日" prop="maturityDate" width="100" align="center" />
        <el-table-column label="对手方" prop="counterparty" width="100" align="center" show-overflow-tooltip />
        <el-table-column label="风险等级" prop="riskLevel" width="90" align="center">
          <template slot-scope="scope">
            <el-tag :type="{ HIGH: 'danger', MEDIUM: 'warning', LOW: 'success' }[scope.row.riskLevel]" size="mini">
              {{ riskTextMap[scope.row.riskLevel] || scope.row.riskLevel }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150" align="center" fixed="right">
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
          @current-change="getList" @size-change="handleSizeChange" />
      </div>
    </el-card>

    <!-- 新增/编辑对话框 -->
    <el-dialog :title="dialogType === 'add' ? '新增衍生品记录' : dialogType === 'edit' ? '编辑衍生品记录' : '查看衍生品记录'" :visible.sync="dialogVisible" width="700px" :close-on-click-modal="false">
      <el-form ref="formRef" :model="formData" :rules="dialogType !== 'view' ? formRules : {}" label-width="110px" :disabled="dialogType === 'view'">
        <el-row :gutter="16">
          <el-col :span="12"><el-form-item label="企业名称" prop="companyName"><el-input v-model="formData.companyName" placeholder="请输入" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="产品名称" prop="productName"><el-input v-model="formData.productName" placeholder="请输入" /></el-form-item></el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12"><el-form-item label="产品类型" prop="productType"><el-select v-model="formData.productType" placeholder="请选择" style="width:100%"><el-option label="期货" value="FUTURES" /><el-option label="期权" value="OPTIONS" /><el-option label="互换" value="SWAP" /><el-option label="远期" value="FORWARD" /></el-select></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="业务目的" prop="businessPurpose"><el-select v-model="formData.businessPurpose" placeholder="请选择" style="width:100%"><el-option label="套期保值" value="HEDGE" /><el-option label="投机" value="SPECULATE" /><el-option label="套利" value="ARBITRAGE" /></el-select></el-form-item></el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12"><el-form-item label="名义金额(万)" prop="notionalAmount"><el-input v-model="formData.notionalAmount" placeholder="请输入" type="number" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="市值(万)" prop="marketValue"><el-input v-model="formData.marketValue" placeholder="请输入" type="number" /></el-form-item></el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12"><el-form-item label="浮动盈亏(万)" prop="profitLoss"><el-input v-model="formData.profitLoss" placeholder="请输入" type="number" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="对手方" prop="counterparty"><el-input v-model="formData.counterparty" placeholder="请输入" /></el-form-item></el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12"><el-form-item label="起始日期" prop="startDate"><el-date-picker v-model="formData.startDate" type="date" value-format="yyyy-MM-dd" placeholder="请选择" style="width:100%" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="到期日期" prop="maturityDate"><el-date-picker v-model="formData.maturityDate" type="date" value-format="yyyy-MM-dd" placeholder="请选择" style="width:100%" /></el-form-item></el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12"><el-form-item label="风险等级" prop="riskLevel"><el-select v-model="formData.riskLevel" placeholder="请选择" style="width:100%"><el-option label="高风险" value="HIGH" /><el-option label="中风险" value="MEDIUM" /><el-option label="低风险" value="LOW" /></el-select></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="状态" prop="status"><el-select v-model="formData.status" placeholder="请选择" style="width:100%"><el-option label="活跃" value="ACTIVE" /><el-option label="已到期" value="MATURED" /><el-option label="已平仓" value="CLOSED" /></el-select></el-form-item></el-col>
        </el-row>
        <el-form-item label="备注" prop="remark"><el-input v-model="formData.remark" type="textarea" :rows="2" placeholder="请输入" /></el-form-item>
      </el-form>
      <div slot="footer" v-if="dialogType !== 'view'">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="handleSubmit">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import { getDerivativesList, addDerivatives, updateDerivatives, deleteDerivatives, exportDerivatives } from '@/api/stateAssets/financialRiskPenetration'
import { investThemeMixin } from '../../themeMixin'
export default {
  name: 'FinancialRiskDerivatives',
  mixins: [investThemeMixin],
  data() {
    return {
      loading: false,
      submitLoading: false,
      total: 0,
      queryForm: { companyName: '', productType: '', riskLevel: '', pageNumber: 1, pageSize: 20 },
      statCards: [
        { label: '持仓总名义金额', value: '0', unit: '万元', icon: 'el-icon-s-data', color: '#0050A0' },
        { label: '合计浮动盈亏', value: '0', unit: '万元', icon: 'el-icon-sort', color: '#CF1322', valueColor: '#CF1322' },
        { label: '高风险持仓', value: '0', unit: '笔', icon: 'el-icon-warning', color: '#FA8C16' },
        { label: '90天内到期', value: '0', unit: '笔', icon: 'el-icon-time', color: '#FAAD14' }
      ],
      typeTagMap: { FUTURES: 'primary', OPTIONS: 'danger', SWAP: 'success', FORWARD: 'warning' },
      typeTextMap: { FUTURES: '期货', OPTIONS: '期权', SWAP: '互换', FORWARD: '远期' },
      riskTextMap: { HIGH: '高风险', MEDIUM: '中风险', LOW: '低风险' },
      charts: [],
      list: [],
      dialogVisible: false,
      dialogType: 'add',
      formData: { companyName: '', productName: '', productType: '', notionalAmount: '', marketValue: '', profitLoss: '', counterparty: '', startDate: '', maturityDate: '', businessPurpose: '', status: 'ACTIVE', riskLevel: 'LOW', remark: '' },
      formRules: {
        companyName: [{ required: true, message: '请输入企业名称', trigger: 'blur' }],
        productName: [{ required: true, message: '请输入产品名称', trigger: 'blur' }],
        productType: [{ required: true, message: '请选择产品类型', trigger: 'change' }],
        notionalAmount: [{ required: true, message: '请输入名义金额', trigger: 'blur' }],
        maturityDate: [{ required: true, message: '请选择到期日期', trigger: 'change' }]
      }
    }
  },
  created() {
    this.statCards[0].color = this.ipSecondary
    this.getList()
  },
  mounted() { this.$nextTick(() => { this.initTypeChart(); this.initMaturityChart() }) },
  beforeDestroy() { this.charts.forEach(c => c && c.dispose()) },
  methods: {
    getList() {
      this.loading = true
      getDerivativesList(this.queryForm).then(res => {
        if (res.data) {
          this.list = res.data.tlist || []
          this.total = res.data.totalRecord || 0
          this.computeStats()
          this.updateCharts()
        }
      }).catch(() => { this.$message.error('查询衍生品列表失败') }).finally(() => { this.loading = false })
    },
    computeStats() {
      let totalNotional = 0
      let totalPnL = 0
      let highRiskCount = 0
      let nearExpiryCount = 0
      const now = new Date()
      this.list.forEach(item => {
        totalNotional += Number(item.notionalAmount) || 0
        totalPnL += Number(item.profitLoss) || 0
        if (item.riskLevel === 'HIGH') highRiskCount++
        if (item.maturityDate) {
          const diff = (new Date(item.maturityDate) - now) / (1000 * 3600 * 24)
          if (diff > 0 && diff <= 90) nearExpiryCount++
        }
      })
      this.statCards[0].value = totalNotional ? Number(totalNotional).toLocaleString() : '0'
      this.statCards[1].value = totalPnL ? (totalPnL >= 0 ? '+' : '') + Number(totalPnL).toLocaleString() : '0'
      this.statCards[1].valueColor = totalPnL >= 0 ? '#52C41A' : '#CF1322'
      this.statCards[2].value = String(highRiskCount)
      this.statCards[3].value = String(nearExpiryCount)
    },
    updateCharts() {
      this.updateTypeChart()
      this.updateMaturityChart()
    },
    handleQuery() { this.queryForm.pageNumber = 1; this.getList() },
    handleSizeChange() { this.queryForm.pageNumber = 1; this.getList() },
    resetQuery() { this.queryForm = { companyName: '', productType: '', riskLevel: '', pageNumber: 1, pageSize: 20 }; this.getList() },
    handleAdd() {
      this.dialogType = 'add'
      this.formData = { companyName: '', productName: '', productType: '', notionalAmount: '', marketValue: '', profitLoss: '', counterparty: '', startDate: '', maturityDate: '', businessPurpose: '', status: 'ACTIVE', riskLevel: 'LOW', remark: '' }
      this.dialogVisible = true
      this.$nextTick(() => { this.$refs.formRef && this.$refs.formRef.clearValidate() })
    },
    handleView(row) { this.dialogType = 'view'; this.formData = { ...row }; this.dialogVisible = true },
    handleEdit(row) {
      this.dialogType = 'edit'
      this.formData = { ...row }
      this.dialogVisible = true
      this.$nextTick(() => { this.$refs.formRef && this.$refs.formRef.clearValidate() })
    },
    handleDelete(row) {
      this.$confirm('确认删除该衍生品记录？', '提示', { type: 'warning' }).then(() => {
        deleteDerivatives(row.derivativeId).then(res => {
          if (res.result === 200) { this.$message.success('删除成功'); this.getList() }
          else this.$message.error(res.msg || '删除失败')
        }).catch(() => { this.$message.error('删除失败') })
      }).catch(() => {})
    },
    handleSubmit() {
      this.$refs.formRef && this.$refs.formRef.validate(valid => {
        if (!valid) return
        this.submitLoading = true
        const api = this.dialogType === 'add' ? addDerivatives : updateDerivatives
        api(this.formData).then(res => {
          if (res.result === 200) {
            this.$message.success(this.dialogType === 'add' ? '新增成功' : '更新成功')
            this.dialogVisible = false
            this.getList()
          } else {
            this.$message.error(res.msg || '操作失败')
          }
        }).catch(() => { this.$message.error('操作失败') }).finally(() => { this.submitLoading = false })
      })
    },
    handleExport() {
      exportDerivatives(this.queryForm).then(res => {
        const blob = new Blob([res], { type: 'application/vnd.ms-excel' })
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = '衍生品持仓台账.xls'
        link.click()
        window.URL.revokeObjectURL(url)
        this.$message.success('导出成功')
      }).catch(() => { this.$message.error('导出失败') })
    },
    getRowClass({ row }) {
      if (row.riskLevel === 'HIGH') return 'high-risk-row'
      return ''
    },
    initTypeChart() {
      const c = echarts.init(this.$refs.typeChart); this.charts.push(c)
      c.setOption({ tooltip: { trigger: 'item' }, legend: { bottom: 0, itemWidth: 12 }, series: [{ type: 'pie', radius: ['35%', '65%'], center: ['50%', '45%'], data: [], label: { formatter: '{b}\n{d}%' } }] })
    },
    updateTypeChart() {
      const colorMap = { FUTURES: this.ipSecondary, OPTIONS: '#CF1322', SWAP: this.ipBright, FORWARD: '#FA8C16' }
      const grouped = {}
      this.list.forEach(item => {
        const t = item.productType
        if (!grouped[t]) grouped[t] = 0
        grouped[t] += Number(item.notionalAmount) || 0
      })
      const chartData = Object.entries(grouped).map(([k, v]) => ({ value: v, name: this.typeTextMap[k] || k, itemStyle: { color: colorMap[k] || '#909399' } }))
      if (this.charts[0]) this.charts[0].setOption({ series: [{ data: chartData }] })
    },
    initMaturityChart() {
      const c = echarts.init(this.$refs.maturityChart); this.charts.push(c)
      c.setOption({ tooltip: { trigger: 'axis' }, legend: { bottom: 0, itemWidth: 12 }, grid: { left: 50, right: 20, top: 30, bottom: 40 }, xAxis: { type: 'category', data: [] }, yAxis: { type: 'value', name: '万元' }, series: [{ name: '高风险', type: 'bar', stack: 's', data: [], itemStyle: { color: '#CF1322' } }, { name: '中风险', type: 'bar', stack: 's', data: [], itemStyle: { color: '#FA8C16' } }, { name: '低风险', type: 'bar', stack: 's', data: [], itemStyle: { color: this.ipBright } }] })
    },
    updateMaturityChart() {
      const quarters = {}
      this.list.forEach(item => {
        if (!item.maturityDate) return
        const d = new Date(item.maturityDate)
        const q = d.getFullYear() + 'Q' + Math.ceil((d.getMonth() + 1) / 3)
        if (!quarters[q]) quarters[q] = { HIGH: 0, MEDIUM: 0, LOW: 0 }
        quarters[q][item.riskLevel || 'LOW'] += Number(item.notionalAmount) || 0
      })
      const sortedKeys = Object.keys(quarters).sort()
      const highData = sortedKeys.map(k => quarters[k].HIGH)
      const medData = sortedKeys.map(k => quarters[k].MEDIUM)
      const lowData = sortedKeys.map(k => quarters[k].LOW)
      if (this.charts[1]) {
        this.charts[1].setOption({ xAxis: { data: sortedKeys }, series: [{ name: '高风险', data: highData }, { name: '中风险', data: medData }, { name: '低风险', data: lowData }] })
      }
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
.stat-value { font-size: 20px; font-weight: 700; line-height: 1; .stat-unit { font-size: 12px; font-weight: 400; color: #909399; margin-left: 2px; } }
.stat-label { font-size: 12px; color: #909399; margin-top: 4px; }
::v-deep .el-table th { background: var(--ip-light-bg, #EBF1FF) !important; color: var(--ip-secondary, #0050A0); }
::v-deep .el-table .high-risk-row { background: #FFF1F0 !important; }
::v-deep .el-card { border-radius: 6px; }
</style>
