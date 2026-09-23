<template>
  <div class="app-container accounting-page">
    <div class="ap-header" :style="{ background: 'linear-gradient(135deg, ' + themeColor + 'cc 0%, ' + themeColor + ' 100%)' }">
      <div class="ap-header-left"><i class="el-icon-tickets"></i><span>会计凭证穿透</span></div>
      <div class="ap-header-desc">多维查询凭证 · 穿透原始单据 · 自动异常检测</div>
    </div>

    <!-- 异常统计 -->
    <el-row :gutter="16" class="anomaly-bar">
      <el-col :span="6" v-for="(s, i) in anomalyStats" :key="i">
        <div class="anomaly-card" :class="s.cls">
          <i :class="s.icon"></i>
          <div class="ac-body">
            <div class="ac-num">{{ s.num }}</div>
            <div class="ac-label">{{ s.label }}</div>
          </div>
        </div>
      </el-col>
    </el-row>

    <el-card shadow="never" class="search-card">
      <el-form :model="queryForm" ref="queryForm" :inline="true" size="small">
        <el-form-item label="企业名称">
          <el-input v-model="queryForm.companyName" placeholder="请输入" clearable style="width:150px" />
        </el-form-item>
        <el-form-item label="凭证字号">
          <el-input v-model="queryForm.voucherNo" placeholder="请输入" clearable style="width:130px" />
        </el-form-item>
        <el-form-item label="会计期间">
          <el-date-picker v-model="queryForm.dateRange" type="monthrange" value-format="yyyy-MM"
            range-separator="至" start-placeholder="开始月" end-placeholder="结束月" style="width:210px" />
        </el-form-item>
        <el-form-item label="会计科目">
          <el-input v-model="queryForm.subject" placeholder="科目编码/名称" clearable style="width:140px" />
        </el-form-item>
        <el-form-item label="异常标记">
          <el-select v-model="queryForm.anomalyFlag" placeholder="全部" clearable style="width:100px">
            <el-option label="正常" value="N" />
            <el-option label="待核查" value="PENDING" />
            <el-option label="已确认异常" value="Y" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleQuery">查询</el-button>
          <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-row :gutter="16" style="margin-top:12px">
      <!-- 凭证列表 -->
      <el-col :span="selectedVoucher ? 14 : 24">
        <el-card shadow="never">
          <div slot="header" class="card-header">
            <span>凭证列表（共 {{ total }} 条）</span>
            <el-button size="mini" icon="el-icon-download" @click="handleExport">导出</el-button>
          </div>
          <el-table
            v-loading="loading"
            :data="list"
            border
            stripe
            size="small"
            @row-click="handleRowClick"
            :row-class-name="getRowClass"
            style="width:100%;cursor:pointer"
          >
            <el-table-column label="企业名称" prop="companyName" min-width="110" show-overflow-tooltip />
            <el-table-column label="凭证日期" prop="voucherDate" width="90" align="center" />
            <el-table-column label="字号" prop="voucherNo" width="80" align="center" />
            <el-table-column label="摘要" prop="summary" min-width="130" show-overflow-tooltip />
            <el-table-column label="借方科目" prop="debitSubject" min-width="110" show-overflow-tooltip />
            <el-table-column label="借方金额" prop="debitAmount" width="100" align="right">
              <template slot-scope="s">{{ formatMoney(s.row.debitAmount) }}</template>
            </el-table-column>
            <el-table-column label="贷方科目" prop="creditSubject" min-width="110" show-overflow-tooltip />
            <el-table-column label="贷方金额" prop="creditAmount" width="100" align="right">
              <template slot-scope="s">{{ formatMoney(s.row.creditAmount) }}</template>
            </el-table-column>
            <el-table-column label="录入人" prop="inputUser" width="70" align="center" />
            <el-table-column label="异常" prop="anomalyFlag" width="80" align="center">
              <template slot-scope="s">
                <el-tag v-if="s.row.anomalyFlag === 'Y'" type="danger" size="mini" effect="plain">已异常</el-tag>
                <el-tag v-else-if="s.row.anomalyFlag === 'PENDING'" type="warning" size="mini" effect="plain">待核查</el-tag>
                <span v-else class="normal-tag">正常</span>
              </template>
            </el-table-column>
          </el-table>
          <el-pagination
            background style="margin-top:12px;text-align:right"
            :current-page="queryForm.pageNumber" :page-sizes="[10,20,50]"
            :page-size="queryForm.pageSize" layout="total, sizes, prev, pager, next, jumper"
            :total="total"
            @size-change="val => { queryForm.pageSize = val; fetchData() }"
            @current-change="val => { queryForm.pageNumber = val; fetchData() }"
          />
        </el-card>
      </el-col>

      <!-- 穿透路径面板 -->
      <el-col :span="10" v-if="selectedVoucher">
        <el-card shadow="never" class="penetration-panel">
          <div slot="header" class="card-header">
            <span><i class="el-icon-share" style="color:#1A3A5C;margin-right:4px"></i>穿透路径</span>
            <el-button size="mini" type="text" @click="selectedVoucher = null">关闭</el-button>
          </div>
          <div class="pen-path">
            <div
              v-for="(node, idx) in penetrationPath"
              :key="idx"
              class="pen-node"
              :class="{ 'pen-node-active': idx === penetrationPath.length - 1 }"
            >
              <div class="pen-node-dot">
                <i :class="node.icon"></i>
              </div>
              <div class="pen-node-body">
                <div class="pen-node-title">{{ node.title }}</div>
                <div class="pen-node-detail">{{ node.detail }}</div>
              </div>
              <div v-if="idx < penetrationPath.length - 1" class="pen-node-line"></div>
            </div>
          </div>

          <!-- 异常检测结果 -->
          <div v-if="selectedVoucher.anomalyFlag !== 'N'" class="anomaly-detail">
            <div class="ad-title"><i class="el-icon-warning" style="color:#FA8C16"></i> 异常检测结果</div>
            <div v-for="(rule, idx) in anomalyRules" :key="idx" class="ad-rule">
              <el-tag :type="rule.level === 'danger' ? 'danger' : rule.level === 'warning' ? 'warning' : 'info'" size="mini">
                {{ rule.levelText }}
              </el-tag>
              <span class="ad-rule-text">{{ rule.desc }}</span>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { getVoucherPenetrationList, getVoucherAnomalyStats, getVoucherPenetration, exportVoucher } from '@/api/stateAssets/accountingPenetration'
import { mapGetters } from 'vuex'
export default {
  computed: {
    ...mapGetters({ theme: 'settings/theme' }),
    themeColor() {
      const map = { red: '#e50113', green: '#41b584', ocean: '#1890ff', white: '#1890ff', default: '#1890ff' }
      return map[(this.theme && this.theme.themeName) || 'default'] || '#1890ff'
    },
    themeColorLight() {
      const map = { red: '#fff1f0', green: '#f6ffed', ocean: '#e6f7ff', white: '#e6f7ff', default: '#e6f7ff' }
      return map[(this.theme && this.theme.themeName) || 'default'] || '#e6f7ff'
    },
  },
  name: 'AccountingVoucherPenetration',
  data() {
    return {
      loading: false,
      list: [],
      total: 0,
      selectedVoucher: null,
      queryForm: { pageNumber: 1, pageSize: 10, companyName: '', voucherNo: '', dateRange: [], subject: '', anomalyFlag: '' },
      anomalyStats: [
        { num: 0, label: '凭证异常总数', icon: 'el-icon-warning', cls: 'ac-danger' },
        { num: 0, label: '无原始凭证', icon: 'el-icon-document-delete', cls: 'ac-danger' },
        { num: 0, label: '期末突击录入', icon: 'el-icon-time', cls: 'ac-warning' },
        { num: 0, label: '待核查', icon: 'el-icon-question', cls: 'ac-info' },
      ],
      penetrationPath: [],
      anomalyRules: [],
    }
  },
  created() { this.fetchData(); this.loadAnomalyStats() },
  methods: {
    // 把前端的 dateRange(monthrange) 转成后端使用的 startPeriod/endPeriod
    buildParams() {
      const params = { ...this.queryForm }
      if (params.dateRange && params.dateRange.length === 2) {
        params.startPeriod = params.dateRange[0] // 'YYYY-MM'
        params.endPeriod = params.dateRange[1]
      }
      delete params.dateRange
      return params
    },
    async fetchData() {
      this.loading = true
      try {
        const res = await getVoucherPenetrationList(this.buildParams())
        if (res && res.result === 200) {
          this.list = (res.data && res.data.tlist) || []
          this.total = (res.data && res.data.totalRecord) || 0
        } else { this.list = []; this.total = 0 }
      } catch (e) { this.list = []; this.total = 0 } finally { this.loading = false }
    },
    async loadAnomalyStats() {
      try {
        const res = await getVoucherAnomalyStats({})
        if (res && res.result === 200 && res.data) {
          const d = res.data
          this.anomalyStats[0].num = d.totalAnomaly || 0
          this.anomalyStats[1].num = d.noOriginalVoucher || 0
          this.anomalyStats[2].num = d.endPeriodRush || 0
          this.anomalyStats[3].num = d.pendingCheck || 0
        }
      } catch (e) { /* keep defaults */ }
    },
    handleQuery() { this.queryForm.pageNumber = 1; this.fetchData() },
    resetQuery() {
      this.queryForm = { pageNumber: 1, pageSize: this.queryForm.pageSize, companyName: '', voucherNo: '', dateRange: [], subject: '', anomalyFlag: '' }
      this.fetchData()
    },
    async handleRowClick(row) {
      this.selectedVoucher = row
      this.penetrationPath = []
      this.anomalyRules = []
      const voucherId = row.id || row.voucherId
      if (!voucherId) return
      try {
        const res = await getVoucherPenetration({ voucherId })
        if (res && res.result === 200 && res.data) {
          this.penetrationPath = res.data.chain || []
          this.anomalyRules = res.data.rules || []
        }
      } catch (e) { /* 留空 */ }
    },
    getRowClass({ row }) {
      if (row.anomalyFlag === 'Y') return 'row-danger'
      if (row.anomalyFlag === 'PENDING') return 'row-warning'
      return ''
    },
    formatMoney(v) {
      if (!v) return '-'
      return Number(v).toLocaleString('zh-CN', { minimumFractionDigits: 2 })
    },
    async handleExport() {
      try {
        const resp = await exportVoucher(this.buildParams())
        downloadBlob(resp, '凭证穿透列表.csv')
      } catch (e) { this.$message.error('导出失败') }
    },
  },
}

// ========== 辅助函数：触发浏览器下载 ==========
function downloadBlob(resp, defaultName) {
  const blob = new Blob([resp.data], { type: resp.headers['content-type'] || 'application/octet-stream' })
  const disposition = resp.headers['content-disposition'] || ''
  let fileName = defaultName
  const m = disposition.match(/filename\*?=(?:UTF-8''|")?([^;"']+)/i)
  if (m && m[1]) { try { fileName = decodeURIComponent(m[1].replace(/^"|"$/g, '')) } catch (e) {} }
  const url = URL.createObjectURL(blob)
  const a = document.createElement('a')
  a.href = url
  a.download = fileName
  document.body.appendChild(a)
  a.click()
  document.body.removeChild(a)
  URL.revokeObjectURL(url)
}
</script>

<style lang="scss" scoped>
.accounting-page { padding: 16px; background: #f5f7fa; min-height: calc(100vh - 84px); }
.ap-header {
  display: flex; align-items: center; justify-content: space-between;
  padding: 14px 20px; 
  border-radius: 8px; color: #fff; margin-bottom: 14px;
  .ap-header-left { display: flex; align-items: center; font-size: 16px; font-weight: 600; i { font-size: 22px; margin-right: 10px; color: #FA8C16; } }
  .ap-header-desc { font-size: 13px; opacity: 0.8; }
}
.anomaly-bar { margin-bottom: 14px; }
.anomaly-card {
  display: flex; align-items: center; gap: 12px; padding: 14px 16px;
  border-radius: 8px; i { font-size: 26px; }
  .ac-body { .ac-num { font-size: 22px; font-weight: 700; } .ac-label { font-size: 12px; } }
  &.ac-danger { background: #fff1f0; color: #FF4D4F; border: 1px solid #ffa39e; }
  &.ac-warning { background: #fff7e6; color: #FA8C16; border: 1px solid #ffd591; }
  &.ac-info { background: #e6f7ff; color: #1890ff; border: 1px solid #91d5ff; }
}
.search-card { border-left: 3px solid #1A3A5C; ::v-deep .el-form-item { margin-bottom: 0; } }
.card-header { display: flex; justify-content: space-between; align-items: center; font-size: 14px; font-weight: 600; color: #303133; }
.normal-tag { font-size: 12px; color: #52C41A; }
::v-deep .row-danger td { background: rgba(255,77,79,0.06) !important; }
::v-deep .row-warning td { background: rgba(250,140,22,0.06) !important; }
::v-deep .el-table th { background: #f0f4f8; }

/* 穿透路径 */
.penetration-panel { height: 100%; }
.pen-path { padding: 8px 0; }
.pen-node {
  display: flex; align-items: flex-start; gap: 12px; position: relative; padding-bottom: 16px;
  &.pen-node-active .pen-node-dot { background: #1A3A5C; color: #fff; }
  .pen-node-dot {
    width: 32px; height: 32px; border-radius: 50%; background: #f0f4f8;
    display: flex; align-items: center; justify-content: center; flex-shrink: 0;
    i { font-size: 15px; color: #1A3A5C; }
  }
  .pen-node-body { flex: 1; padding-top: 4px; }
  .pen-node-title { font-size: 13px; font-weight: 600; color: #303133; }
  .pen-node-detail { font-size: 12px; color: #909399; margin-top: 2px; }
  .pen-node-line {
    position: absolute; left: 15px; top: 32px; bottom: 0;
    width: 2px; background: #e8edf2;
  }
}
.anomaly-detail {
  border-top: 1px dashed #ffd591; margin-top: 8px; padding-top: 12px;
  .ad-title { font-size: 13px; font-weight: 600; color: #FA8C16; margin-bottom: 10px; i { margin-right: 4px; } }
  .ad-rule { display: flex; align-items: flex-start; gap: 8px; margin-bottom: 8px; }
  .ad-rule-text { font-size: 12px; color: #606266; flex: 1; }
}
</style>
