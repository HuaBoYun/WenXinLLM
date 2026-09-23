<template>
  <div class="page-container" :style="themeVars">
    <!-- Banner -->
    <div class="page-header">
      <div class="header-left">
        <h2 class="page-title"><i class="el-icon-s-custom"></i> 供应商档案管理</h2>
        <p class="page-desc">供应商全档案管理，支持资质查看、关联关系识别、集中度分析和黑名单管理</p>
      </div>
    </div>

    <!-- 统计卡 -->
    <el-row :gutter="16" class="stat-row">
      <el-col :span="6" v-for="s in statCards" :key="s.label">
        <div class="stat-card" :style="{ borderLeft: '4px solid ' + s.color }">
          <div class="stat-val" :style="{ color: s.color }">{{ s.value }}</div>
          <div class="stat-lb">{{ s.label }}</div>
        </div>
      </el-col>
    </el-row>

    <!-- 图表区 -->
    <el-row :gutter="16" class="chart-row">
      <el-col :span="14">
        <el-card shadow="never">
          <div slot="header" class="card-hd">采购集中度TOP10（亿元）</div>
          <div ref="concChart" class="chart-sm"></div>
        </el-card>
      </el-col>
      <el-col :span="10">
        <el-card shadow="never">
          <div slot="header" class="card-hd">供应商类型分布</div>
          <div ref="typeChart" class="chart-sm"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 查询区 -->
    <el-card shadow="never" class="search-card">
      <el-form :model="queryForm" inline size="small">
        <el-form-item label="供应商名称">
          <el-input v-model="queryForm.supplierName" placeholder="请输入" clearable style="width:160px" />
        </el-form-item>
        <el-form-item label="资质等级">
          <el-select v-model="queryForm.qualificationLevel" placeholder="请选择" clearable style="width:100px">
            <el-option label="A级" value="A" /><el-option label="B级" value="B" /><el-option label="C级" value="C" />
          </el-select>
        </el-form-item>
        <el-form-item label="关联状态">
          <el-select v-model="queryForm.isRelated" placeholder="请选择" clearable style="width:100px">
            <el-option label="关联方" value="Y" /><el-option label="非关联" value="N" />
          </el-select>
        </el-form-item>
        <el-form-item label="黑名单状态">
          <el-select v-model="queryForm.blacklistStatus" placeholder="请选择" clearable style="width:110px">
            <el-option label="正常" value="NORMAL" /><el-option label="预警" value="WARNING" /><el-option label="黑名单" value="BLACKLIST" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleQuery">查询</el-button>
          <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 黑名单预警 -->
    <el-alert
      v-if="blacklistCount > 0"
      :title="`发现 ${blacklistCount} 家黑名单供应商仍在交易记录中，请立即核查`"
      type="error" show-icon :closable="false" style="margin-bottom:12px"
    />

    <!-- 表格 -->
    <el-card shadow="never">
      <div style="margin-bottom:10px">
        <el-button size="small" type="primary" icon="el-icon-plus" @click="handleAdd">新增</el-button>
      </div>
      <el-table v-loading="loading" :data="list" border :row-class-name="rowClassName">
        <el-table-column label="供应商编号" prop="supplierCode" width="100" />
        <el-table-column label="供应商名称" prop="supplierName" min-width="170" show-overflow-tooltip />
        <el-table-column label="供应商类型" width="90" align="center">
          <template slot-scope="{ row }">
            <el-tag size="mini" type="info">{{ typeMap[row.supplierType] || row.supplierType }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="资质等级" prop="qualificationLevel" width="80" align="center" />
        <el-table-column label="所在区域" prop="region" width="90" align="center" />
        <el-table-column label="累计采购额(万元)" prop="totalPurchase" width="140" align="right">
          <template slot-scope="{ row }">{{ (row.totalPurchase || 0).toLocaleString() }}</template>
        </el-table-column>
        <el-table-column label="采购占比" prop="purchaseRatio" width="90" align="center">
          <template slot-scope="{ row }">{{ row.purchaseRatio || 0 }}%</template>
        </el-table-column>
        <el-table-column label="关联方" width="75" align="center">
          <template slot-scope="{ row }">
            <el-tag v-if="row.isRelated === true || row.isRelated === 'Y'" size="mini" color="#F9F0FF" style="color:#722ED1;border-color:#722ED1">关联</el-tag>
            <span v-else style="color:#8C8C8C;font-size:12px">—</span>
          </template>
        </el-table-column>
        <el-table-column label="黑名单状态" width="100" align="center">
          <template slot-scope="{ row }">
            <el-tag :type="{ NORMAL: 'success', WARNING: 'warning', BLACKLIST: 'danger' }[row.blacklistStatus]" size="mini">
              {{ { NORMAL: '正常', WARNING: '预警', BLACKLIST: '黑名单' }[row.blacklistStatus] }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="风险等级" width="85" align="center">
          <template slot-scope="{ row }">
            <el-tag :type="{ HIGH: 'danger', MEDIUM: 'warning', LOW: 'success' }[row.riskLevel]" size="mini">
              {{ { HIGH: '高', MEDIUM: '中', LOW: '低' }[row.riskLevel] }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="最近审计日期" prop="lastAuditDate" width="120" align="center" />
        <el-table-column label="操作" width="160" align="center" fixed="right">
          <template slot-scope="{ row }">
            <el-button size="mini" type="text" @click="handleEdit(row)">编辑</el-button>
            <el-button size="mini" type="text" style="color:#CF1322" @click="handleBlacklist(row)" v-if="row.blacklistStatus !== 'BLACKLIST'">加入黑名单</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination background style="margin-top:12px;text-align:right" :current-page="queryForm.pageNumber" :page-sizes="[10, 20, 50]" :page-size="queryForm.pageSize" layout="total, sizes, prev, pager, next, jumper" :total="total" @size-change="val => { queryForm.pageSize = val; fetchData() }" @current-change="val => { queryForm.pageNumber = val; fetchData() }" />
    </el-card>

    <!-- 新增/编辑弹窗 -->
    <el-dialog :title="dialogType === 'add' ? '新增供应商' : '编辑供应商'" :visible.sync="dialogVisible" width="700px" :close-on-click-modal="false">
      <el-form :model="form" :rules="rules" ref="form" label-width="120px">
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="供应商名称" prop="supplierName"><el-input v-model="form.supplierName" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="供应商编号"><el-input v-model="form.supplierCode" /></el-form-item></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="供应商类型"><el-select v-model="form.supplierType" style="width:100%"><el-option label="工程" value="ENGINEERING" /><el-option label="货物" value="GOODS" /><el-option label="服务" value="SERVICE" /><el-option label="IT" value="IT" /></el-select></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="资质等级"><el-select v-model="form.qualificationLevel" style="width:100%"><el-option label="A级" value="A" /><el-option label="B级" value="B" /><el-option label="C级" value="C" /></el-select></el-form-item></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="所在区域"><el-input v-model="form.region" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="是否关联方"><el-radio-group v-model="form.isRelated"><el-radio label="Y">是</el-radio><el-radio label="N">否</el-radio></el-radio-group></el-form-item></el-col>
        </el-row>
        <el-form-item label="关联公司" v-if="form.isRelated === 'Y' || form.isRelated === true"><el-input v-model="form.relatedCompany" /></el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="handleSubmit">确 定</el-button>
      </div>
    </el-dialog>

    <!-- 黑名单弹窗 -->
    <el-dialog title="加入黑名单" :visible.sync="blacklistDialogVisible" width="450px">
      <el-form label-width="80px">
        <el-form-item label="原因"><el-input v-model="blacklistForm.reason" type="textarea" :rows="3" placeholder="请输入加入黑名单的原因" /></el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="blacklistDialogVisible = false">取 消</el-button>
        <el-button type="danger" @click="confirmBlacklist">确认加入</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import { getSupplierProfileList, addSupplierProfile, updateSupplierProfile, blacklistSupplier, getSupplierConcentration } from '@/api/stateAssets/procurementPenetration'
import { investThemeMixin } from '../../themeMixin'

export default {
  name: 'SupplierProfile',
  mixins: [investThemeMixin],
  data() {
    return {
      loading: false,
      list: [],
      total: 0,
      queryForm: { pageNumber: 1, pageSize: 10, supplierName: '', qualificationLevel: '', isRelated: '', blacklistStatus: '' },
      typeMap: { ENGINEERING: '工程', GOODS: '货物', SERVICE: '服务', IT: 'IT' },
      statCards: [
        { label: '供应商总数', value: '-', color: '#0050A0' },
        { label: '合格供应商', value: '-', color: '#52C41A' },
        { label: '关联供应商', value: '-', color: '#722ED1' },
        { label: '黑名单供应商', value: '-', color: '#CF1322' },
      ],
      charts: [],
      dialogVisible: false,
      dialogType: 'add',
      form: {},
      submitLoading: false,
      blacklistDialogVisible: false,
      blacklistForm: { id: '', reason: '' },
      rules: { supplierName: [{ required: true, message: '请输入供应商名称', trigger: 'blur' }] },
    }
  },
  computed: {
    blacklistCount() {
      return this.list.filter(r => r.blacklistStatus === 'BLACKLIST').length
    },
  },
  created() { this.fetchData(); this.fetchConcentration() },
  mounted() {
    window.addEventListener('resize', this.handleResize)
  },
  beforeDestroy() {
    window.removeEventListener('resize', this.handleResize)
    this.charts.forEach(c => c.dispose())
  },
  methods: {
    async fetchData() {
      this.loading = true
      try {
        const res = await getSupplierProfileList(this.queryForm)
        if (res && res.result === 200) {
          this.list = (res.data && res.data.tlist) || []
          this.total = (res.data && res.data.totalRecord) || 0
          this.updateStatCards()
          this.$nextTick(() => this.renderTypeChart())
        } else {
          this.list = []; this.total = 0
        }
      } catch (e) {
        this.$message.error('获取供应商列表失败')
        this.list = []; this.total = 0
      } finally { this.loading = false }
    },
    updateStatCards() {
      const total = this.total
      const qualified = this.list.filter(r => r.blacklistStatus !== 'BLACKLIST').length
      const related = this.list.filter(r => r.isRelated === true || r.isRelated === 'Y').length
      const blacklisted = this.list.filter(r => r.blacklistStatus === 'BLACKLIST').length
      this.statCards = [
        { label: '供应商总数', value: total.toLocaleString(), color: this.ipSecondary },
        { label: '合格供应商', value: qualified.toLocaleString(), color: '#52C41A' },
        { label: '关联供应商', value: related.toLocaleString(), color: '#722ED1' },
        { label: '黑名单供应商', value: blacklisted.toLocaleString(), color: '#CF1322' },
      ]
    },
    async fetchConcentration() {
      try {
        const res = await getSupplierConcentration()
        if (res && res.result === 200 && res.data) {
          // 后端返回供应商对象数组，转换为图表需要的格式
          const suppliers = Array.isArray(res.data) ? res.data : []
          const chartData = {
            names: suppliers.map(s => s.supplierName || ''),
            values: suppliers.map(s => s.totalPurchase ? (s.totalPurchase / 10000).toFixed(1) : 0),
          }
          this.renderConcChart(chartData)
        }
      } catch (e) { /* chart stays empty */ }
    },
    handleQuery() { this.queryForm.pageNumber = 1; this.fetchData() },
    resetQuery() {
      this.queryForm = { pageNumber: 1, pageSize: 10, supplierName: '', qualificationLevel: '', isRelated: '', blacklistStatus: '' }
      this.fetchData()
    },
    handleAdd() { this.dialogType = 'add'; this.form = { blacklistStatus: 'NORMAL', isRelated: 'N' }; this.dialogVisible = true },
    handleEdit(row) { this.dialogType = 'edit'; this.form = { ...row }; this.dialogVisible = true },
    async handleSubmit() {
      this.$refs.form.validate(async (valid) => {
        if (!valid) return
        this.submitLoading = true
        try {
          const submitData = { ...this.form }
          // 确保 isRelated 为 "Y"/"N" 格式
          if (submitData.isRelated === true || submitData.isRelated === 'true') submitData.isRelated = 'Y'
          else if (submitData.isRelated === false || submitData.isRelated === 'false' || !submitData.isRelated) submitData.isRelated = 'N'
          const apiFn = this.dialogType === 'add' ? addSupplierProfile : updateSupplierProfile
          const res = await apiFn(submitData)
          if (res && res.result === 200) {
            this.$message.success('操作成功'); this.dialogVisible = false; this.fetchData()
          } else { this.$message.error(res.msg || '操作失败') }
        } catch (e) { this.$message.error('操作失败') } finally { this.submitLoading = false }
      })
    },
    handleBlacklist(row) {
      this.blacklistForm = { id: row.id, reason: '' }
      this.blacklistDialogVisible = true
    },
    async confirmBlacklist() {
      if (!this.blacklistForm.reason) { this.$message.warning('请输入黑名单原因'); return }
      try {
        const res = await blacklistSupplier(this.blacklistForm.id, this.blacklistForm.reason)
        if (res && res.result === 200) {
          this.$message.success('已加入黑名单'); this.blacklistDialogVisible = false; this.fetchData()
        } else { this.$message.error(res.msg || '操作失败') }
      } catch (e) { this.$message.error('操作失败') }
    },
    rowClassName({ row }) {
      if (row.blacklistStatus === 'BLACKLIST') return 'row-blacklist'
      if (row.isRelated === true || row.isRelated === 'Y') return 'row-related'
      if (row.blacklistStatus === 'WARNING') return 'row-warning'
      return ''
    },
    handleResize() { this.charts.forEach(c => c.resize()) },
    renderConcChart(data) {
      if (!this.$refs.concChart) return
      const c1 = this.charts[0] || echarts.init(this.$refs.concChart)
      if (!this.charts[0]) this.charts.push(c1)
      const names = (data.names || []).slice(0, 10)
      const vals = (data.values || []).slice(0, 10)
      c1.setOption({
        tooltip: { trigger: 'axis', axisPointer: { type: 'shadow' } },
        grid: { left: 130, right: 50, top: 8, bottom: 30 },
        xAxis: { type: 'value', name: '万元' },
        yAxis: { type: 'category', data: names, axisLabel: { fontSize: 11 } },
        series: [{
          type: 'bar',
          data: vals,
          itemStyle: { color: (p) => p.dataIndex >= 8 ? '#CF1322' : (p.dataIndex >= 6 ? '#FA8C16' : this.ipSecondary) },
          label: { show: true, position: 'right', formatter: '{c}' },
        }],
      })
    },
    initCharts() {
      // Initialize empty charts; data will be populated by fetchConcentration
      if (this.$refs.concChart) {
        const c1 = echarts.init(this.$refs.concChart)
        this.charts.push(c1)
      }
      if (this.$refs.typeChart) {
        const c2 = echarts.init(this.$refs.typeChart)
        this.charts.push(c2)
        this.renderTypeChart()
      }
    },
    renderTypeChart() {
      if (!this.$refs.typeChart) return
      const typeCount = {}
      this.list.forEach(r => { typeCount[r.supplierType] = (typeCount[r.supplierType] || 0) + 1 })
      const typeNames = { ENGINEERING: '工程类', GOODS: '货物类', SERVICE: '服务类', IT: 'IT类' }
      const colors = { ENGINEERING: this.ipSecondary, GOODS: this.ipBright, SERVICE: '#69B1FF', IT: '#FAAD14' }
      const pieData = Object.keys(typeCount).map(k => ({
        value: typeCount[k], name: typeNames[k] || k, itemStyle: { color: colors[k] || '#999' },
      })).filter(d => d.value > 0)
      // 自初始化：如果图表实例不存在则创建
      if (!this._typeChartInstance) {
        this._typeChartInstance = echarts.init(this.$refs.typeChart)
        this.charts.push(this._typeChartInstance)
      }
      this._typeChartInstance.setOption({
        tooltip: { trigger: 'item' },
        legend: { bottom: 4 },
        series: [{ type: 'pie', radius: ['40%', '70%'], center: ['50%', '44%'], data: pieData, label: { formatter: '{b}: {c}家' } }],
      })
    },
  },
}
</script>

<style lang="scss" scoped>
.page-container { padding: 16px; background: #F0F2F5; min-height: calc(100vh - 84px); }
.page-header {
  padding: 18px 24px; margin-bottom: 16px;
  background: linear-gradient(135deg, var(--ip-primary, #003A6C) 0%, var(--ip-secondary, #0050A0) 60%, var(--ip-bright, #1677FF) 100%);
  border-radius: 8px; color: #fff;
  .page-title { margin: 0 0 4px 0; font-size: 18px; font-weight: 700; i { margin-right: 8px; } }
  .page-desc { margin: 0; font-size: 13px; opacity: .85; }
}
.stat-row { margin-bottom: 16px; }
.stat-card { background: #fff; border-radius: 6px; padding: 14px 16px; box-shadow: 0 1px 4px rgba(0,0,0,.07);
  .stat-val { font-size: 22px; font-weight: 700; }
  .stat-lb { font-size: 12px; color: #8C8C8C; margin-top: 4px; }
}
.chart-row { margin-bottom: 16px; }
.chart-sm { height: 210px; }
.search-card { margin-bottom: 12px; }
.card-hd { font-size: 14px; font-weight: 600; color: #303133; }
::v-deep .el-table th { background: var(--ip-light-bg, #EBF1FF) !important; color: var(--ip-secondary, #0050A0); font-weight: 600; }
::v-deep .el-table .row-blacklist td { background: #F5F5F5 !important; color: #8C8C8C; }
::v-deep .el-table .row-related td { background: #F9F0FF !important; }
::v-deep .el-table .row-warning td { background: #FFF7E6 !important; }
::v-deep .el-card { border-radius: 6px; }
</style>
