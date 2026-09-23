<template>
  <div class="risk-warning-page" :style="themeVars">
    <!-- Banner -->
    <div class="page-banner" :style="{ background: 'linear-gradient(135deg, ' + ipPrimary + ' 0%, ' + ipSecondary + ' 60%, ' + ipBright + ' 100%)' }">
      <div class="banner-content">
        <div class="banner-title"><i class="el-icon-warning-outline" style="margin-right:8px;"/>行业风险预警管理</div>
        <div class="banner-sub">实时监控行业风险态势，快速响应处置各类预警事件</div>
      </div>
      <div class="banner-stats">
        <div class="b-stat" v-for="s in bannerStats" :key="s.label">
          <div class="b-val" :style="{color: s.color}">{{ s.value }}</div>
          <div class="b-lbl">{{ s.label }}</div>
        </div>
      </div>
    </div>

    <!-- 统计卡 -->
    <el-row :gutter="16" style="margin:16px 0;">
      <el-col :span="6" v-for="c in statCards" :key="c.label">
        <el-card class="stat-card" shadow="never" :style="{'border-left': '4px solid ' + c.color}">
          <div class="stat-inner">
            <div>
              <div class="stat-num" :style="{color: c.color}">{{ c.value }}</div>
              <div class="stat-lbl">{{ c.label }}</div>
            </div>
            <i :class="c.icon" :style="{fontSize:'28px', color: c.color, opacity: 0.35}"/>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 查询栏 -->
    <el-card shadow="never" style="margin-bottom:16px;">
      <el-form :inline="true" :model="query" size="small">
        <el-form-item label="预警编号">
          <el-input v-model="query.warnNo" placeholder="请输入" clearable style="width:150px;"/>
        </el-form-item>
        <el-form-item label="企业名称">
          <el-input v-model="query.companyName" placeholder="请输入" clearable style="width:150px;"/>
        </el-form-item>
        <el-form-item label="行业分类">
          <el-select v-model="query.industry" placeholder="全部" clearable style="width:130px;">
            <el-option v-for="o in industryOptions" :key="o" :label="o" :value="o"/>
          </el-select>
        </el-form-item>
        <el-form-item label="预警级别">
          <el-select v-model="query.level" placeholder="全部" clearable style="width:120px;">
            <el-option label="高危" value="HIGH"/>
            <el-option label="中危" value="MEDIUM"/>
            <el-option label="低危" value="LOW"/>
          </el-select>
        </el-form-item>
        <el-form-item label="处置状态">
          <el-select v-model="query.status" placeholder="全部" clearable style="width:120px;">
            <el-option label="待处置" value="PENDING"/>
            <el-option label="处置中" value="PROCESSING"/>
            <el-option label="已关闭" value="CLOSED"/>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleSearch">查询</el-button>
          <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 预警列表 -->
    <el-card shadow="never">
      <div slot="header" style="display:flex;align-items:center;justify-content:space-between;">
        <span style="font-weight:600;">预警事件列表</span>
        <div>
          <el-button size="small" type="danger" plain icon="el-icon-bell" @click="handleBatchDispose">批量处置</el-button>
          <el-button size="small" type="success" plain icon="el-icon-download" :loading="exportLoading" @click="handleExport">导出记录</el-button>
        </div>
      </div>
      <el-table :data="list" border stripe :row-class-name="tableRowClass" @selection-change="handleSelectionChange" @row-click="handleViewDetail" style="cursor:pointer;" v-loading="loading">
        <el-table-column type="selection" width="50" fixed/>
        <el-table-column prop="warnNo" label="预警编号" width="170" fixed/>
        <el-table-column prop="warnTime" label="预警时间" width="160"/>
        <el-table-column prop="companyName" label="企业名称" min-width="140"/>
        <el-table-column prop="industry" label="行业分类" width="110">
          <template slot-scope="{row}">
            <span class="industry-tag" :style="industryTagStyle(row.industry)">{{ row.industry }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="warnType" label="预警类型" min-width="140"/>
        <el-table-column prop="level" label="预警级别" width="90" align="center">
          <template slot-scope="{row}">
            <el-tag :type="levelTagType(row.level)" size="small" effect="dark">{{ levelLabel(row.level) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="riskScore" label="风险评分" width="90" align="center">
          <template slot-scope="{row}">
            <span :style="{color: row.riskScore >= 80 ? '#F5222D' : row.riskScore >= 60 ? '#FA8C16' : '#52C41A', fontWeight:'600'}">{{ row.riskScore }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="处置状态" width="100" align="center">
          <template slot-scope="{row}">
            <el-tag :type="statusTagType(row.status)" size="small">{{ statusLabel(row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="handler" label="处置负责人" width="110"/>
        <el-table-column label="操作" width="160" align="center" fixed="right">
          <template slot-scope="{row}">
            <el-button size="mini" type="text" @click.stop="handleDispose(row)" :disabled="row.status === 'CLOSED'">处置</el-button>
            <el-divider direction="vertical"/>
            <el-button size="mini" type="text" @click.stop="handleViewDetail(row)">详情</el-button>
            <el-divider direction="vertical"/>
            <el-button size="mini" type="text" style="color:#F5222D;" @click.stop="handleClose(row)" :disabled="row.status === 'CLOSED'">关闭</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div style="margin-top:12px;text-align:right;">
        <el-pagination background layout="total, sizes, prev, pager, next" :total="total" :page-size="pageSize" :current-page="currentPage" @size-change="handleSizeChange" @current-change="handlePageChange"/>
      </div>
    </el-card>

    <!-- 处置弹窗 -->
    <el-dialog :title="'预警处置 — ' + (disposeForm.warnNo || '')" :visible.sync="disposeVisible" width="560px">
      <el-form :model="disposeForm" label-width="90px" size="small">
        <el-form-item label="预警事件">
          <el-input :value="disposeForm.warnType" disabled/>
        </el-form-item>
        <el-form-item label="处置措施" required>
          <el-select v-model="disposeForm.measure" style="width:100%;">
            <el-option label="立即整改" value="RECTIFY"/>
            <el-option label="专项检查" value="INSPECT"/>
            <el-option label="约谈负责人" value="INTERVIEW"/>
            <el-option label="上报监管机构" value="REPORT"/>
            <el-option label="暂停相关业务" value="SUSPEND"/>
          </el-select>
        </el-form-item>
        <el-form-item label="处置负责人" required>
          <el-input v-model="disposeForm.handler" placeholder="请输入负责人姓名"/>
        </el-form-item>
        <el-form-item label="计划完成时间" required>
          <el-date-picker v-model="disposeForm.dueDate" type="date" value-format="yyyy-MM-dd" style="width:100%;"/>
        </el-form-item>
        <el-form-item label="处置说明">
          <el-input v-model="disposeForm.remark" type="textarea" :rows="4" placeholder="请填写处置详细说明..."/>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="disposeVisible = false">取消</el-button>
        <el-button type="primary" @click="submitDispose">确认处置</el-button>
      </div>
    </el-dialog>

    <!-- 批量处置弹窗 -->
    <el-dialog title="批量处置预警" :visible.sync="batchDisposeVisible" width="560px">
      <el-alert :title="`已选择 ${selectedRows.filter(r => r.status !== 'CLOSED').length} 条待处置预警`" type="info" :closable="false" show-icon style="margin-bottom:16px;"/>
      <el-form :model="batchDisposeForm" label-width="90px" size="small">
        <el-form-item label="处置措施" required>
          <el-select v-model="batchDisposeForm.measure" style="width:100%;">
            <el-option label="立即整改" value="RECTIFY"/>
            <el-option label="专项检查" value="INSPECT"/>
            <el-option label="约谈负责人" value="INTERVIEW"/>
            <el-option label="上报监管机构" value="REPORT"/>
            <el-option label="暂停相关业务" value="SUSPEND"/>
          </el-select>
        </el-form-item>
        <el-form-item label="处置负责人" required>
          <el-input v-model="batchDisposeForm.handler" placeholder="请输入负责人姓名"/>
        </el-form-item>
        <el-form-item label="计划完成时间" required>
          <el-date-picker v-model="batchDisposeForm.dueDate" type="date" value-format="yyyy-MM-dd" style="width:100%;"/>
        </el-form-item>
        <el-form-item label="处置说明">
          <el-input v-model="batchDisposeForm.remark" type="textarea" :rows="3" placeholder="请填写批量处置说明..."/>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="batchDisposeVisible = false">取消</el-button>
        <el-button type="primary" @click="submitBatchDispose">确认批量处置</el-button>
      </div>
    </el-dialog>

    <!-- 详情抽屉 -->
    <el-drawer title="预警详情" :visible.sync="detailVisible" size="520px" direction="rtl">
      <div v-if="currentRow" class="detail-drawer">
        <div class="detail-header" :class="'level-' + currentRow.level.toLowerCase()">
          <div class="detail-level-badge">{{ levelLabel(currentRow.level) }}</div>
          <div class="detail-warn-no">{{ currentRow.warnNo }}</div>
          <div class="detail-warn-type">{{ currentRow.warnType }}</div>
        </div>
        <div class="detail-body">
          <el-descriptions :column="2" border size="small">
            <el-descriptions-item label="企业名称">{{ currentRow.companyName }}</el-descriptions-item>
            <el-descriptions-item label="行业分类">
              <span class="industry-tag" :style="industryTagStyle(currentRow.industry)">{{ currentRow.industry }}</span>
            </el-descriptions-item>
            <el-descriptions-item label="预警时间">{{ currentRow.warnTime }}</el-descriptions-item>
            <el-descriptions-item label="风险评分">
              <span :style="{color: currentRow.riskScore >= 80 ? '#F5222D' : '#FA8C16', fontWeight:'600'}">{{ currentRow.riskScore }}分</span>
            </el-descriptions-item>
            <el-descriptions-item label="处置状态">
              <el-tag :type="statusTagType(currentRow.status)" size="small">{{ statusLabel(currentRow.status) }}</el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="处置负责人">{{ currentRow.handler || '—' }}</el-descriptions-item>
            <el-descriptions-item label="预警描述" :span="2">{{ currentRow.description }}</el-descriptions-item>
          </el-descriptions>
          <div class="detail-timeline-title">处置进度</div>
          <el-timeline>
            <el-timeline-item v-for="(t, i) in currentRow.timeline" :key="i" :type="t.type" :timestamp="t.time" placement="top">{{ t.content }}</el-timeline-item>
          </el-timeline>
        </div>
      </div>
    </el-drawer>
  </div>
</template>

<script>
import { getIndustryWarningList, handleIndustryWarning, batchHandleIndustryWarning, exportIndustryWarning } from '@/api/stateAssets/industryPenetration'
import { investThemeMixin } from '../../themeMixin'

export default {
  name: 'IndustryRiskWarning',
  mixins: [investThemeMixin],
  data() {
    return {
      list: [],
      total: 0,
      currentPage: 1,
      pageSize: 10,
      loading: false,
      query: { warnNo: '', companyName: '', industry: '', level: '', status: '' },
      industryOptions: ['能源行业', '金融行业', '制造行业', '基础设施', '公共服务'],
      disposeVisible: false,
      disposeForm: {},
      batchDisposeVisible: false,
      batchDisposeForm: { measure: '', handler: '', dueDate: '', remark: '' },
      selectedRows: [],
      detailVisible: false,
      currentRow: null,
      exportLoading: false,
    }
  },
  computed: {
    statCards() {
      const d = this.list
      return [
        { label: '预警总数', value: this.total, color: this.ipBright, icon: 'el-icon-bell' },
        { label: '高危预警', value: d.filter(r => r.level === 'HIGH').length, color: '#F5222D', icon: 'el-icon-warning' },
        { label: '待处置', value: d.filter(r => r.status === 'PENDING').length, color: '#FA8C16', icon: 'el-icon-time' },
        { label: '已关闭', value: d.filter(r => r.status === 'CLOSED').length, color: '#52C41A', icon: 'el-icon-circle-check' },
      ]
    },
    bannerStats() {
      const d = this.list
      return [
        { label: '高危', value: d.filter(r => r.level === 'HIGH').length, color: '#ff7875' },
        { label: '中危', value: d.filter(r => r.level === 'MEDIUM').length, color: '#ffd591' },
        { label: '低危', value: d.filter(r => r.level === 'LOW').length, color: '#b7eb8f' },
        { label: '处置中', value: d.filter(r => r.status === 'PROCESSING').length, color: '#91caff' },
      ]
    },
  },
  created() { this.loadData() },
  methods: {
    async loadData() {
      this.loading = true
      try {
        const params = { ...this.query, pageNumber: this.currentPage, pageSize: this.pageSize }
        const res = await getIndustryWarningList(params)
        if (res && res.result === 200 && res.data) {
          this.list = res.data.tlist || res.data.list || []
          this.total = res.data.totalRecord || this.list.length
        } else {
          this.list = []
          this.total = 0
        }
      } catch (e) {
        this.list = []
        this.total = 0
      } finally {
        this.loading = false
      }
    },
    resetQuery() {
      this.query = { warnNo: '', companyName: '', industry: '', level: '', status: '' }
      this.currentPage = 1
      this.loadData()
    },
    handleSearch() {
      this.currentPage = 1
      this.loadData()
    },
    handlePageChange(page) {
      this.currentPage = page
      this.loadData()
    },
    handleSizeChange(size) {
      this.pageSize = size
      this.currentPage = 1
      this.loadData()
    },
    handleSelectionChange(rows) {
      this.selectedRows = rows
    },
    tableRowClass({ row }) {
      if (row.level === 'HIGH' && row.status !== 'CLOSED') return 'row-risk-high'
      if (row.level === 'MEDIUM' && row.status !== 'CLOSED') return 'row-risk-medium'
      return ''
    },
    handleDispose(row) {
      this.disposeForm = { warnNo: row.warnNo, warnType: row.warnType, measure: '', handler: '', dueDate: '', remark: '', id: row.warnNo }
      this.disposeVisible = true
    },
    submitDispose() {
      if (!this.disposeForm.measure || !this.disposeForm.handler || !this.disposeForm.dueDate) {
        return this.$message.warning('请完整填写处置信息')
      }
      handleIndustryWarning(this.disposeForm).then(() => {
        this.$message.success('处置任务已创建')
        this.disposeVisible = false
        this.loadData()
      }).catch(() => {
        this.$message.error('处置失败')
      })
    },
    // 批量处置
    handleBatchDispose() {
      if (!this.selectedRows.length) {
        return this.$message.warning('请先选择需要处置的预警记录')
      }
      const pendingRows = this.selectedRows.filter(r => r.status !== 'CLOSED')
      if (!pendingRows.length) {
        return this.$message.warning('所选记录均已关闭，无需处置')
      }
      this.batchDisposeForm = { measure: '', handler: '', dueDate: '', remark: '' }
      this.batchDisposeVisible = true
    },
    submitBatchDispose() {
      if (!this.batchDisposeForm.measure || !this.batchDisposeForm.handler || !this.batchDisposeForm.dueDate) {
        return this.$message.warning('请完整填写处置信息')
      }
      const ids = this.selectedRows.filter(r => r.status !== 'CLOSED').map(r => r.warnNo)
      batchHandleIndustryWarning({
        ids,
        status: 'PROCESSING',
        handler: this.batchDisposeForm.handler,
        measure: this.batchDisposeForm.measure,
      }).then(() => {
        this.$message.success(`已成功处置 ${ids.length} 条预警`)
        this.batchDisposeVisible = false
        this.selectedRows = []
        this.loadData()
      }).catch(() => {
        this.$message.error('批量处置失败')
      })
    },
    // 导出记录
    async handleExport() {
      this.exportLoading = true
      try {
        const res = await exportIndustryWarning(this.query)
        const blobData = res.data || res
        const blob = new Blob([blobData], { type: 'application/vnd.ms-excel' })
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = '行业风险预警数据.xls'
        document.body.appendChild(link)
        link.click()
        document.body.removeChild(link)
        window.URL.revokeObjectURL(url)
        this.$message.success('导出成功')
      } catch (e) {
        this.$message.error('导出失败')
      } finally {
        this.exportLoading = false
      }
    },
    handleClose(row) {
      this.$confirm('确认关闭该预警事件？', '关闭预警', { type: 'warning' }).then(() => {
        handleIndustryWarning({ id: row.warnNo, status: 'CLOSED' }).then(() => {
          this.$message.success('预警已关闭')
          this.loadData()
        }).catch(() => {
          this.$message.error('操作失败')
        })
      }).catch(() => {})
    },
    handleViewDetail(row) {
      this.currentRow = row
      this.detailVisible = true
    },
    levelLabel(v) { return { HIGH: '高危', MEDIUM: '中危', LOW: '低危' }[v] || v },
    levelTagType(v) { return { HIGH: 'danger', MEDIUM: 'warning', LOW: 'success' }[v] || '' },
    statusLabel(v) { return { PENDING: '待处置', PROCESSING: '处置中', CLOSED: '已关闭' }[v] || v },
    statusTagType(v) { return { PENDING: 'danger', PROCESSING: 'warning', CLOSED: 'info' }[v] || '' },
    industryTagStyle(v) {
      const map = {
        '能源行业': { background: '#FFF7E6', color: '#FA8C16', border: '1px solid #FFD591' },
        '金融行业': { background: this.ipLightBg, color: this.ipBright, border: '1px solid #ADC6FF' },
        '制造行业': { background: '#F6FFED', color: '#52C41A', border: '1px solid #B7EB8F' },
        '基础设施': { background: '#E8F4FF', color: this.ipSecondary, border: '1px solid #91CAFF' },
        '公共服务': { background: '#F9F0FF', color: '#722ED1', border: '1px solid #D3ADF7' },
      }
      return map[v] || {}
    },
  },
}
</script>

<style lang="scss" scoped>
.risk-warning-page { padding: 16px; background: #f5f7fa; min-height: 100%; }
.page-banner {
  border-radius: 8px; padding: 24px 32px; display: flex; align-items: center; justify-content: space-between; margin-bottom: 4px;
  .banner-title { font-size: 22px; font-weight: 700; color: #fff; }
  .banner-sub { font-size: 13px; color: rgba(255,255,255,0.8); margin-top: 6px; }
  .banner-stats { display: flex; gap: 32px; }
  .b-stat { text-align: center; }
  .b-val { font-size: 24px; font-weight: 700; }
  .b-lbl { font-size: 12px; color: rgba(255,255,255,0.8); margin-top: 2px; }
}
.stat-card { border-radius: 6px; }
.stat-inner { display: flex; align-items: center; justify-content: space-between; padding: 4px 0; }
.stat-num { font-size: 28px; font-weight: 700; }
.stat-lbl { font-size: 12px; color: #8c8c8c; margin-top: 4px; }
.industry-tag { padding: 2px 8px; border-radius: 4px; font-size: 12px; }
::v-deep .row-risk-high td { background: #FFF1F0 !important; }
::v-deep .row-risk-medium td { background: #FFFBE6 !important; }
.detail-drawer { height: 100%; overflow-y: auto; }
.detail-header {
  padding: 20px 24px; margin-bottom: 16px;
  &.level-high { background: linear-gradient(135deg, #fff1f0, #ffccc7); }
  &.level-medium { background: linear-gradient(135deg, #fffbe6, #ffe58f); }
  &.level-low { background: linear-gradient(135deg, #f6ffed, #d9f7be); }
  .detail-level-badge { display: inline-block; padding: 2px 12px; border-radius: 12px; font-weight: 700; font-size: 13px; background: rgba(0,0,0,0.08); margin-bottom: 8px; }
  .detail-warn-no { font-size: 13px; color: #666; margin-bottom: 4px; }
  .detail-warn-type { font-size: 16px; font-weight: 700; }
}
.detail-body { padding: 0 24px 24px; }
.detail-timeline-title { font-weight: 600; margin: 16px 0 12px; }
</style>
