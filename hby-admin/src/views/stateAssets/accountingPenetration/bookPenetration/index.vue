<template>
  <div class="app-container accounting-page">
    <div class="ap-header" :style="{ background: 'linear-gradient(135deg, ' + themeColor + 'cc 0%, ' + themeColor + ' 100%)' }">
      <div class="ap-header-left"><i class="el-icon-notebook-1"></i><span>账簿穿透查询</span></div>
      <div class="ap-header-desc">科目余额→总账→明细账→辅助账→日记账 · 层层穿透</div>
    </div>

    <!-- 公共查询条 -->
    <el-card shadow="never" class="search-card">
      <el-form :model="queryForm" ref="queryForm" :inline="true" size="small">
        <el-form-item label="企业">
          <el-input v-model="queryForm.companyName" placeholder="企业名称" clearable style="width:160px" />
        </el-form-item>
        <el-form-item label="会计期间">
          <el-date-picker v-model="queryForm.yearMonth" type="month" value-format="yyyy-MM" placeholder="选择月份" style="width:130px" />
        </el-form-item>
        <el-form-item label="科目">
          <el-input v-model="queryForm.subjectCode" placeholder="科目编码/名称" clearable style="width:150px" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleQuery">查询</el-button>
          <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
          <el-button icon="el-icon-download" @click="handleExport">导出</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card shadow="never" style="margin-top:12px">
      <el-tabs v-model="activeTab" @tab-click="handleTabChange">
        <!-- Tab1: 科目余额表 -->
        <el-tab-pane label="科目余额表" name="balance">
          <div class="tab-tip"><i class="el-icon-info"></i> 点击科目行可穿透到总分类账</div>
          <el-table
            v-loading="loading"
            :data="balanceList"
            border
            size="small"
            @row-click="drillToGeneral"
            row-key="subjectCode"
            :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
            style="width:100%;cursor:pointer"
          >
            <el-table-column label="科目编码" prop="subjectCode" width="110" />
            <el-table-column label="科目名称" prop="subjectName" min-width="160" />
            <el-table-column label="方向" prop="direction" width="60" align="center">
              <template slot-scope="s">
                <el-tag size="mini" :type="s.row.direction === '借' ? 'primary' : 'success'" effect="plain">{{ s.row.direction }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column label="期初余额" prop="openingBalance" width="120" align="right">
              <template slot-scope="s">{{ formatMoney(s.row.openingBalance) }}</template>
            </el-table-column>
            <el-table-column label="本期借方" prop="debitAmount" width="120" align="right">
              <template slot-scope="s">{{ formatMoney(s.row.debitAmount) }}</template>
            </el-table-column>
            <el-table-column label="本期贷方" prop="creditAmount" width="120" align="right">
              <template slot-scope="s">{{ formatMoney(s.row.creditAmount) }}</template>
            </el-table-column>
            <el-table-column label="期末余额" prop="closingBalance" width="120" align="right">
              <template slot-scope="s">
                <span :class="s.row.changeRate > 30 ? 'text-warning' : ''">{{ formatMoney(s.row.closingBalance) }}</span>
              </template>
            </el-table-column>
            <el-table-column label="环比变动" prop="changeRate" width="90" align="center">
              <template slot-scope="s">
                <span v-if="s.row.changeRate" :class="Math.abs(s.row.changeRate) > 30 ? 'text-warning' : 'text-normal'">
                  {{ s.row.changeRate > 0 ? '+' : '' }}{{ s.row.changeRate }}%
                </span>
                <span v-else>-</span>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>

        <!-- Tab2: 总分类账 -->
        <el-tab-pane label="总分类账" name="general">
          <div class="tab-tip" v-if="drillSubject">
            <i class="el-icon-share"></i> 当前科目：<strong>{{ drillSubject }}</strong>
            <el-button type="text" size="mini" @click="drillSubject = ''" style="margin-left:8px">清除筛选</el-button>
          </div>
          <el-table v-loading="loading" :data="generalList" border size="small" @row-click="drillToSub" style="width:100%;cursor:pointer">
            <el-table-column label="日期" prop="date" width="90" align="center" />
            <el-table-column label="凭证字号" prop="voucherNo" width="110" align="center" />
            <el-table-column label="摘要" prop="summary" min-width="160" show-overflow-tooltip />
            <el-table-column label="借方金额" prop="debitAmount" width="120" align="right">
              <template slot-scope="s">{{ formatMoney(s.row.debitAmount) }}</template>
            </el-table-column>
            <el-table-column label="贷方金额" prop="creditAmount" width="120" align="right">
              <template slot-scope="s">{{ formatMoney(s.row.creditAmount) }}</template>
            </el-table-column>
            <el-table-column label="余额" prop="balance" width="120" align="right">
              <template slot-scope="s">{{ formatMoney(s.row.balance) }}</template>
            </el-table-column>
            <el-table-column label="操作" width="80" align="center">
              <template slot-scope="s">
                <el-button type="text" size="mini" @click.stop="viewVoucher(s.row)">看凭证</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>

        <!-- Tab3: 明细账 -->
        <el-tab-pane label="明细账" name="sub">
          <div class="tab-tip">
            <i class="el-icon-info"></i> 按辅助核算维度展开，点击凭证号穿透到凭证
            <template v-if="drillSubject">
              <span style="margin-left:10px">· 当前科目：<strong>{{ drillSubject }}</strong></span>
              <el-button type="text" size="mini" @click="drillSubject = ''; loadSubLedger()" style="margin-left:8px">清除科目筛选</el-button>
            </template>
          </div>
          <el-form :inline="true" size="small" style="margin-bottom:10px">
            <el-form-item label="辅助维度">
              <el-select v-model="auxDimension" style="width:120px" @change="loadSubLedger">
                <el-option label="部门" value="dept" /><el-option label="项目" value="project" />
                <el-option label="客户" value="customer" /><el-option label="供应商" value="supplier" />
              </el-select>
            </el-form-item>
          </el-form>
          <el-table v-loading="loading" :data="subList" border size="small" style="width:100%">
            <el-table-column label="辅助核算" prop="auxName" min-width="120" />
            <el-table-column label="日期" prop="date" width="90" align="center" />
            <el-table-column label="凭证号" prop="voucherNo" width="110" align="center">
              <template slot-scope="s">
                <el-button type="text" size="mini" @click="viewVoucher(s.row)">{{ s.row.voucherNo }}</el-button>
              </template>
            </el-table-column>
            <el-table-column label="摘要" prop="summary" min-width="140" show-overflow-tooltip />
            <el-table-column label="借方" prop="debitAmount" width="110" align="right">
              <template slot-scope="s">{{ formatMoney(s.row.debitAmount) }}</template>
            </el-table-column>
            <el-table-column label="贷方" prop="creditAmount" width="110" align="right">
              <template slot-scope="s">{{ formatMoney(s.row.creditAmount) }}</template>
            </el-table-column>
            <el-table-column label="余额" prop="balance" width="110" align="right">
              <template slot-scope="s">{{ formatMoney(s.row.balance) }}</template>
            </el-table-column>
          </el-table>
        </el-tab-pane>

        <!-- Tab4: 辅助账 -->
        <el-tab-pane label="辅助账" name="aux">
          <el-form :inline="true" size="small" style="margin-bottom:10px">
            <el-form-item label="核算维度">
              <el-select v-model="auxType" style="width:120px" @change="loadAux">
                <el-option label="应收账款账龄" value="ar_age" />
                <el-option label="存货账龄" value="inv_age" />
                <el-option label="客户往来" value="customer" />
                <el-option label="供应商往来" value="supplier" />
              </el-select>
            </el-form-item>
          </el-form>
          <el-table v-loading="loading" :data="auxList" border size="small" style="width:100%">
            <el-table-column label="名称" prop="name" min-width="160" show-overflow-tooltip />
            <el-table-column label="余额(万元)" prop="balance" width="120" align="right">
              <template slot-scope="s">{{ formatMoney(s.row.balance) }}</template>
            </el-table-column>
            <el-table-column label="1年以内" prop="within1y" width="110" align="right">
              <template slot-scope="s">{{ formatMoney(s.row.within1y) }}</template>
            </el-table-column>
            <el-table-column label="1-2年" prop="y1to2" width="100" align="right" />
            <el-table-column label="2-3年" prop="y2to3" width="100" align="right" />
            <el-table-column label="3年以上" prop="over3y" width="110" align="right">
              <template slot-scope="s">
                <span :class="s.row.over3yRate > 0.2 ? 'text-danger' : ''">{{ s.row.over3y }}</span>
              </template>
            </el-table-column>
            <el-table-column label="风险标记" prop="risk" width="90" align="center">
              <template slot-scope="s">
                <el-tag v-if="s.row.risk" :type="s.row.risk === 'HIGH' ? 'danger' : 'warning'" size="mini">
                  {{ s.row.risk === 'HIGH' ? '高风险' : '关注' }}
                </el-tag>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>

        <!-- Tab5: 日记账 -->
        <el-tab-pane label="日记账" name="diary">
          <div class="tab-tip"><i class="el-icon-info"></i> 银行流水与账面记录对比，识别未达账项</div>
          <el-form :inline="true" size="small" style="margin-bottom:10px">
            <el-form-item label="账户类型">
              <el-select v-model="diaryType" style="width:120px" @change="loadDiary">
                <el-option label="银行账户" value="bank" /><el-option label="现金" value="cash" />
              </el-select>
            </el-form-item>
          </el-form>
          <el-table v-loading="loading" :data="diaryList" border size="small" style="width:100%">
            <el-table-column label="日期" prop="date" width="90" align="center" />
            <el-table-column label="摘要" prop="summary" min-width="160" show-overflow-tooltip />
            <el-table-column label="借方(收入)" prop="debit" width="120" align="right">
              <template slot-scope="s">{{ formatMoney(s.row.debit) }}</template>
            </el-table-column>
            <el-table-column label="贷方(支出)" prop="credit" width="120" align="right">
              <template slot-scope="s">{{ formatMoney(s.row.credit) }}</template>
            </el-table-column>
            <el-table-column label="余额" prop="balance" width="120" align="right">
              <template slot-scope="s">{{ formatMoney(s.row.balance) }}</template>
            </el-table-column>
            <el-table-column label="未达账" prop="unreconciled" width="80" align="center">
              <template slot-scope="s">
                <el-tag v-if="s.row.unreconciled" type="warning" size="mini" effect="plain">未达账</el-tag>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
      </el-tabs>
    </el-card>

    <!-- 凭证详情对话框 -->
    <el-dialog :visible.sync="voucherDialog.visible" title="凭证详情" width="700px" append-to-body>
      <div v-loading="voucherDialog.loading" style="min-height:120px">
        <el-descriptions v-if="voucherDialog.data" :column="2" border size="small">
          <el-descriptions-item label="公司">{{ voucherDialog.data.companyName || '-' }}</el-descriptions-item>
          <el-descriptions-item label="凭证号">{{ voucherDialog.data.voucherNo || '-' }}</el-descriptions-item>
          <el-descriptions-item label="日期">{{ voucherDialog.data.voucherDate || '-' }}</el-descriptions-item>
          <el-descriptions-item label="期间">{{ voucherDialog.data.period || '-' }}</el-descriptions-item>
          <el-descriptions-item label="摘要" :span="2">{{ voucherDialog.data.summary || '-' }}</el-descriptions-item>
          <el-descriptions-item label="借方科目">{{ voucherDialog.data.debitSubject || '-' }}</el-descriptions-item>
          <el-descriptions-item label="贷方科目">{{ voucherDialog.data.creditSubject || '-' }}</el-descriptions-item>
          <el-descriptions-item label="借方金额">{{ formatMoney(voucherDialog.data.debitAmount) }}</el-descriptions-item>
          <el-descriptions-item label="贷方金额">{{ formatMoney(voucherDialog.data.creditAmount) }}</el-descriptions-item>
          <el-descriptions-item label="录入人">{{ voucherDialog.data.inputUser || '-' }}</el-descriptions-item>
          <el-descriptions-item label="异常标识">{{ voucherDialog.data.anomalyFlag || '-' }}</el-descriptions-item>
        </el-descriptions>
        <div v-else-if="!voucherDialog.loading" style="text-align:center;color:#909399;padding:24px 0">未查询到详情</div>
      </div>
      <div slot="footer"><el-button size="small" @click="voucherDialog.visible = false">关闭</el-button></div>
    </el-dialog>
  </div>
</template>

<script>
import { getSubjectBalanceList, getGeneralLedgerList, getSubLedgerList, getAuxLedgerList, getDiaryLedgerList, getVoucherDetail, exportBook } from '@/api/stateAssets/accountingPenetration'
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
  name: 'AccountingBookPenetration',
  data() {
    return {
      loading: false,
      activeTab: 'balance',
      drillSubject: '',
      auxDimension: 'dept',
      auxType: 'ar_age',
      diaryType: 'bank',
      queryForm: { pageNumber: 1, pageSize: 50, companyName: '', yearMonth: '', subjectCode: '' },
      balanceList: [],
      generalList: [],
      subList: [],
      auxList: [],
      diaryList: [],
      voucherDialog: { visible: false, loading: false, data: null },
    }
  },
  created() { this.loadBalance() },
  methods: {
    // 提取统一请求参数：自动剔除空串，避免 "" 被带去后端模糊匹配
    buildParams(extra) {
      const p = { ...this.queryForm, ...(extra || {}) }
      Object.keys(p).forEach(k => {
        if (p[k] === '' || p[k] === null || p[k] === undefined) delete p[k]
      })
      return p
    },
    handleQuery() { this.loadDataByTab() },
    resetQuery() {
      this.queryForm = { pageNumber: 1, pageSize: 50, companyName: '', yearMonth: '', subjectCode: '' }
      this.drillSubject = ''
      this.loadDataByTab()
    },
    handleTabChange() { this.loadDataByTab() },
    loadDataByTab() {
      const tab = this.activeTab
      if (tab === 'balance') this.loadBalance()
      else if (tab === 'general') this.loadGeneral()
      else if (tab === 'sub') this.loadSubLedger()
      else if (tab === 'aux') this.loadAux()
      else if (tab === 'diary') this.loadDiary()
    },
    async loadBalance() {
      this.loading = true
      try {
        const res = await getSubjectBalanceList(this.buildParams())
        if (res && res.result === 200) { this.balanceList = (res.data && res.data.tlist) || [] }
        else { this.balanceList = [] }
      } catch (e) { this.balanceList = [] } finally { this.loading = false }
    },
    async loadGeneral() {
      this.loading = true
      try {
        // drillSubject 形如 "1001 库存现金"，传给后端 subjectName 做 like
        const extra = this.drillSubject ? { subjectName: this.drillSubject } : {}
        const res = await getGeneralLedgerList(this.buildParams(extra))
        if (res && res.result === 200) { this.generalList = (res.data && res.data.tlist) || [] }
        else { this.generalList = [] }
      } catch (e) { this.generalList = [] } finally { this.loading = false }
    },
    async loadSubLedger() {
      this.loading = true
      try {
        const extra = { auxDimension: this.auxDimension }
        if (this.drillSubject) extra.subjectName = this.drillSubject
        const res = await getSubLedgerList(this.buildParams(extra))
        if (res && res.result === 200) { this.subList = (res.data && res.data.tlist) || [] }
        else { this.subList = [] }
      } catch (e) { this.subList = [] } finally { this.loading = false }
    },
    async loadAux() {
      this.loading = true
      try {
        const res = await getAuxLedgerList(this.buildParams({ auxType: this.auxType }))
        if (res && res.result === 200) { this.auxList = (res.data && res.data.tlist) || [] }
        else { this.auxList = [] }
      } catch (e) { this.auxList = [] } finally { this.loading = false }
    },
    async loadDiary() {
      this.loading = true
      try {
        const res = await getDiaryLedgerList(this.buildParams({ diaryType: this.diaryType }))
        if (res && res.result === 200) { this.diaryList = (res.data && res.data.tlist) || [] }
        else { this.diaryList = [] }
      } catch (e) { this.diaryList = [] } finally { this.loading = false }
    },
    drillToGeneral(row) {
      // 科目余额 → 总账：带上科目筛选
      if (!row || !row.subjectCode) return
      this.drillSubject = row.subjectCode + ' ' + (row.subjectName || '')
      this.activeTab = 'general'
      this.loadGeneral()
    },
    drillToSub(row) {
      // 总账 → 明细：科目条件继续带下去
      this.activeTab = 'sub'
      this.loadSubLedger()
    },
    viewVoucher(row) {
      // 账簿中的凭证通过 voucherId 拉详情；没有 voucherId 时降级用 voucherNo 反查
      this.voucherDialog.visible = true
      this.voucherDialog.loading = true
      this.voucherDialog.data = null
      const id = row.voucherId || row.id
      if (id) {
        this.fetchVoucherDetail(id, row)
        return
      }
      if (!row.voucherNo) {
        this.voucherDialog.loading = false
        this.voucherDialog.data = null
        this.$message.warning('该行无可定位的凭证信息')
        return
      }
      // 用 voucherNo 通过凭证列表接口反查，拿到真实 voucherId 后再取详情
      getVoucherPenetrationListByNo(row.voucherNo).then(list => {
        if (list && list.length) {
          const found = list.find(v => v.voucherNo === row.voucherNo) || list[0]
          if (found && found.id) { this.fetchVoucherDetail(found.id, row); return }
        }
        // 反查失败，用行里已有字段兜底渲染
        this.voucherDialog.data = {
          voucherNo: row.voucherNo,
          voucherDate: row.date,
          summary: row.summary,
          debitAmount: row.debitAmount,
          creditAmount: row.creditAmount,
        }
        this.voucherDialog.loading = false
      }).catch(() => {
        this.voucherDialog.data = {
          voucherNo: row.voucherNo,
          voucherDate: row.date,
          summary: row.summary,
          debitAmount: row.debitAmount,
          creditAmount: row.creditAmount,
        }
        this.voucherDialog.loading = false
      })
    },
    fetchVoucherDetail(id, fallbackRow) {
      getVoucherDetail(id).then(res => {
        if (res && res.result === 200 && res.data) {
          this.voucherDialog.data = res.data
        } else {
          this.voucherDialog.data = fallbackRow || null
        }
      }).catch(() => {
        this.$message.error('凭证详情加载失败')
        this.voucherDialog.data = fallbackRow || null
      }).finally(() => { this.voucherDialog.loading = false })
    },
    async handleExport() {
      try {
        const tabToType = { balance: 'BALANCE', general: 'GENERAL', sub: 'SUB', aux: 'AUX', diary: 'DIARY' }
        const extra = { bookType: tabToType[this.activeTab] || 'BALANCE' }
        if (this.activeTab === 'general' && this.drillSubject) extra.subjectName = this.drillSubject
        if (this.activeTab === 'sub') { extra.auxDimension = this.auxDimension; if (this.drillSubject) extra.subjectName = this.drillSubject }
        if (this.activeTab === 'aux') extra.auxType = this.auxType
        if (this.activeTab === 'diary') extra.diaryType = this.diaryType
        const resp = await exportBook(this.buildParams(extra))
        downloadBlob(resp, '账簿穿透_' + extra.bookType + '.csv')
      } catch (e) { this.$message.error('导出失败') }
    },
    formatMoney(v) {
      if (!v && v !== 0) return '-'
      return Number(v).toLocaleString('zh-CN', { minimumFractionDigits: 2 })
    },
  },
}

// ========== 通过凭证号反查凭证 id（简单封装，复用凭证列表接口） ==========
function getVoucherPenetrationListByNo(voucherNo) {
  const { getVoucherPenetrationList } = require('@/api/stateAssets/accountingPenetration')
  return getVoucherPenetrationList({ voucherNo, pageSize: 5 }).then(res => {
    if (res && res.result === 200 && res.data) return res.data.tlist || res.data.list || []
    return []
  })
}

// ========== 辅助：下载 Blob ==========
function downloadBlob(resp, defaultName) {
  const blob = new Blob([resp.data], { type: resp.headers['content-type'] || 'application/octet-stream' })
  const disposition = resp.headers['content-disposition'] || ''
  let fileName = defaultName
  const m = disposition.match(/filename\*?=(?:UTF-8''|")?([^;"']+)/i)
  if (m && m[1]) { try { fileName = decodeURIComponent(m[1].replace(/^"|"$/g, '')) } catch (e) {} }
  const url = URL.createObjectURL(blob)
  const a = document.createElement('a')
  a.href = url; a.download = fileName
  document.body.appendChild(a); a.click(); document.body.removeChild(a)
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
.search-card { border-left: 3px solid #1A3A5C; margin-bottom: 0; ::v-deep .el-form-item { margin-bottom: 0; } }
.tab-tip { font-size: 12px; color: #909399; margin-bottom: 10px; i { margin-right: 4px; color: #1A3A5C; } }
.text-warning { color: #FA8C16; font-weight: 600; }
.text-danger { color: #FF4D4F; font-weight: 600; }
.text-normal { color: #52C41A; }
::v-deep .el-table th { background: #f0f4f8; }
</style>
