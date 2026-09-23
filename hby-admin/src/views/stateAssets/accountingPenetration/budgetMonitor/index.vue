<template>
  <div class="app-container accounting-page">
    <div class="ap-header" :style="{ background: 'linear-gradient(135deg, ' + themeColor + 'cc 0%, ' + themeColor + ' 100%)' }">
      <div class="ap-header-left"><i class="el-icon-pie-chart"></i><span>预算执行监管</span></div>
      <div class="ap-header-desc">全面预算穿透 · 超支预警 · 期末突击识别</div>
    </div>

    <el-card shadow="never" class="search-card">
      <el-form :inline="true" size="small">
        <el-form-item label="企业">
          <el-input v-model="queryForm.companyName" placeholder="企业名称" clearable style="width:160px" />
        </el-form-item>
        <el-form-item label="年度">
          <el-date-picker v-model="queryForm.year" type="year" value-format="yyyy" placeholder="选择年度" style="width:110px" />
        </el-form-item>
        <el-form-item label="预警状态">
          <el-select v-model="queryForm.alertStatus" clearable style="width:110px">
            <el-option label="全部" value="" /><el-option label="超支" value="OVER" /><el-option label="滞后" value="LAG" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleQuery">查询</el-button>
          <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- KPI -->
    <el-row :gutter="16" style="margin-top:14px">
      <el-col :span="6" v-for="(k, i) in kpis" :key="i">
        <div class="budget-kpi" :class="k.cls">
          <div class="bk-icon"><i :class="k.icon"></i></div>
          <div class="bk-body"><div class="bk-val">{{ k.val }}</div><div class="bk-label">{{ k.label }}</div></div>
        </div>
      </el-col>
    </el-row>

    <el-card shadow="never" style="margin-top:14px">
      <el-tabs v-model="activeTab">
        <!-- 总览 -->
        <el-tab-pane label="预算执行总览" name="overview">
          <div class="budget-table-header">
            <span style="font-size:13px;color:#606266"><i class="el-icon-warning-outline" style="color:#FA8C16"></i> 红色=超支，橙色=执行滞后，点击行穿透到明细</span>
          </div>
          <el-table v-loading="loading" :data="overviewList" border size="small" @row-click="drillToDetail" style="width:100%;cursor:pointer">
            <el-table-column label="企业名称" prop="companyName" min-width="130" show-overflow-tooltip />
            <el-table-column label="年度预算(万)" prop="annualBudget" width="110" align="right">
              <template slot-scope="s">{{ formatMoney(s.row.annualBudget) }}</template>
            </el-table-column>
            <el-table-column label="已执行(万)" prop="executed" width="110" align="right">
              <template slot-scope="s">{{ formatMoney(s.row.executed) }}</template>
            </el-table-column>
            <el-table-column label="执行率" prop="rate" width="200">
              <template slot-scope="s">
                <div class="budget-progress">
                  <el-progress
                    :percentage="Math.min(s.row.rate, 100)"
                    :color="s.row.rate > 100 ? '#FF4D4F' : s.row.rate > 90 ? '#FA8C16' : '#1890ff'"
                    :stroke-width="10"
                    :show-text="false"
                  />
                  <span :class="s.row.rate > 100 ? 'score-red' : s.row.rate < 50 ? 'score-orange' : ''">{{ s.row.rate }}%</span>
                </div>
              </template>
            </el-table-column>
            <el-table-column label="超支金额(万)" prop="overAmount" width="110" align="right">
              <template slot-scope="s">
                <span v-if="s.row.overAmount > 0" class="score-red">+{{ formatMoney(s.row.overAmount) }}</span>
                <span v-else class="score-green">-</span>
              </template>
            </el-table-column>
            <el-table-column label="期末突击" prop="endRush" width="80" align="center">
              <template slot-scope="s">
                <el-tag v-if="s.row.endRush" type="danger" size="mini" effect="plain">疑似突击</el-tag>
              </template>
            </el-table-column>
            <el-table-column label="预警" prop="alertLevel" width="80" align="center">
              <template slot-scope="s">
                <el-tag v-if="s.row.alertLevel" :type="s.row.alertLevel === 'RED' ? 'danger' : 'warning'" size="mini">
                  {{ s.row.alertLevel === 'RED' ? '红色' : '橙色' }}
                </el-tag>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>

        <!-- 明细 -->
        <el-tab-pane label="预算执行明细" name="detail">
          <div class="tab-tip" v-if="selectedCompany">当前企业：<strong>{{ selectedCompany }}</strong></div>
          <div v-if="!selectedCompany" class="empty-tip">
            <i class="el-icon-info"></i> 请在"预算执行总览"中点击某行企业进行穿透查看明细
          </div>
          <el-table v-if="selectedCompany" :data="detailList" border size="small" style="width:100%">
            <el-table-column label="预算科目" prop="category" min-width="140" />
            <el-table-column label="年度预算(万)" prop="annualBudget" width="110" align="right">
              <template slot-scope="s">{{ formatMoney(s.row.annualBudget) }}</template>
            </el-table-column>
            <el-table-column label="当月预算" prop="monthBudget" width="100" align="right">
              <template slot-scope="s">{{ formatMoney(s.row.monthBudget) }}</template>
            </el-table-column>
            <el-table-column label="当月实际" prop="monthActual" width="100" align="right">
              <template slot-scope="s">{{ formatMoney(s.row.monthActual) }}</template>
            </el-table-column>
            <el-table-column label="当月偏差" prop="monthGap" width="100" align="right">
              <template slot-scope="s">
                <span :class="s.row.monthGap > 0 ? 'score-red' : 'score-green'">
                  {{ s.row.monthGap > 0 ? '+' : '' }}{{ formatMoney(s.row.monthGap) }}
                </span>
              </template>
            </el-table-column>
            <el-table-column label="累计执行率" prop="cumRate" width="100" align="center">
              <template slot-scope="s">
                <span :class="s.row.cumRate > 100 ? 'score-red' : ''">{{ s.row.cumRate }}%</span>
              </template>
            </el-table-column>
            <el-table-column label="预警" width="80" align="center">
              <template slot-scope="s">
                <el-tag v-if="s.row.cumRate > 100" type="danger" size="mini">超支</el-tag>
                <el-tag v-else-if="s.row.cumRate < 50" type="warning" size="mini">滞后</el-tag>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>

        <!-- 预算调整台账 -->
        <el-tab-pane label="预算调整台账" name="adjust">
          <div class="tab-tip"><i class="el-icon-info"></i> 频繁调整（季内≥3次）自动预警</div>
          <el-table :data="adjustList" border size="small" style="width:100%">
            <el-table-column label="企业名称" prop="companyName" min-width="130" show-overflow-tooltip />
            <el-table-column label="调整项目" prop="item" min-width="140" show-overflow-tooltip />
            <el-table-column label="调整类型" prop="adjustType" width="90" align="center">
              <template slot-scope="s">
                <el-tag :type="s.row.adjustType === 'INCREASE' ? 'danger' : 'success'" size="mini">
                  {{ s.row.adjustType === 'INCREASE' ? '调增' : '调减' }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="调整金额(万)" prop="amount" width="110" align="right">
              <template slot-scope="s">{{ formatMoney(s.row.amount) }}</template>
            </el-table-column>
            <el-table-column label="调整原因" prop="reason" min-width="160" show-overflow-tooltip />
            <el-table-column label="审批状态" prop="approvalStatus" width="90" align="center">
              <template slot-scope="s">
                <el-tag :type="{ APPROVED: 'success', PENDING: 'warning', REJECTED: 'danger' }[s.row.approvalStatus]" size="mini">
                  {{ { APPROVED: '已批准', PENDING: '审批中', REJECTED: '已驳回' }[s.row.approvalStatus] }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="调整次数" prop="adjustCount" width="80" align="center">
              <template slot-scope="s">
                <span :class="s.row.adjustCount >= 3 ? 'score-red' : ''">{{ s.row.adjustCount }}次</span>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script>
import { getBudgetMonitorList, getBudgetDetailList, getBudgetAdjustList, getBudgetKpi } from '@/api/stateAssets/accountingPenetration'
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
  name: 'AccountingBudgetMonitor',
  data() {
    return {
      loading: false,
      activeTab: 'overview',
      selectedCompany: '',
      queryForm: { companyName: '', year: '', alertStatus: '' },
      kpis: [],
      overviewList: [],
      detailList: [],
      adjustList: [],
    }
  },
  created() { this.loadData() },
  watch: {
    activeTab() {
      // 仅在非穿透触发时加载（穿透由 drillToDetail 手动控制）
      if (this._isDrilling) return
      this.loadCurrentTab()
    }
  },
  methods: {
    /** 加载 KPI + 当前激活 tab 的数据 */
    async loadData() {
      try {
        const kRes = await getBudgetKpi(this.queryForm)
        if (kRes && kRes.result === 200 && kRes.data && kRes.data.kpis) {
          this.kpis = kRes.data.kpis
        }
      } catch (e) { this.kpis = [] }
      this.loadCurrentTab()
    },
    /** 根据当前 activeTab 加载对应数据 */
    loadCurrentTab() {
      if (this.activeTab === 'overview') this.loadOverviewList()
      else if (this.activeTab === 'detail') this.loadDetailList()
      else if (this.activeTab === 'adjust') this.loadAdjustList()
    },
    async loadOverviewList() {
      this.loading = true
      try {
        const res = await getBudgetMonitorList(this.queryForm)
        if (res && res.result === 200) { this.overviewList = (res.data && res.data.tlist) || [] }
        else { this.overviewList = [] }
      } catch (e) { this.overviewList = [] } finally { this.loading = false }
    },
    async loadDetailList() {
      // 未选中企业时不发请求，提示用户
      if (!this.selectedCompany) {
        this.detailList = []
        return
      }
      this.loading = true
      try {
        const res = await getBudgetDetailList({ ...this.queryForm, companyName: this.selectedCompany })
        if (res && res.result === 200) { this.detailList = (res.data && res.data.tlist) || [] }
        else { this.detailList = [] }
      } catch (e) { this.detailList = [] } finally { this.loading = false }
    },
    async loadAdjustList() {
      this.loading = true
      try {
        const res = await getBudgetAdjustList(this.queryForm)
        if (res && res.result === 200) { this.adjustList = (res.data && res.data.tlist) || [] }
        else { this.adjustList = [] }
      } catch (e) { this.adjustList = [] } finally { this.loading = false }
    },
    handleQuery() { this.loadData() },
    resetQuery() {
      this.queryForm = { companyName: '', year: '', alertStatus: '' }
      this.selectedCompany = ''
      this.loadData()
    },
    drillToDetail(row) {
      this.selectedCompany = row.companyName
      this._isDrilling = true
      this.activeTab = 'detail'
      this.$nextTick(() => {
        this._isDrilling = false
        this.loadDetailList()
      })
    },
    formatMoney(v) {
      if (!v && v !== 0) return '-'
      return Number(v).toLocaleString('zh-CN', { minimumFractionDigits: 2 })
    },
  },
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
.search-card { border-left: 3px solid #1A3A5C; ::v-deep .el-form-item { margin-bottom: 0; } }
.budget-kpi {
  display: flex; align-items: center; gap: 12px; background: #fff;
  border-radius: 10px; padding: 14px 16px; box-shadow: 0 2px 8px rgba(0,0,0,0.07);
  border-left: 3px solid #d9d9d9;
  .bk-icon { width: 36px; height: 36px; border-radius: 8px; background: #f0f2f5; display: flex; align-items: center; justify-content: center; i { font-size: 18px; color: #606266; } }
  .bk-val { font-size: 20px; font-weight: 700; color: #303133; }
  .bk-label { font-size: 11px; color: #909399; margin-top: 2px; }
  &.kpi-ok { border-left-color: #52C41A; .bk-icon { background: #f6ffed; i { color: #52C41A; } } }
  &.kpi-warn { border-left-color: #FA8C16; .bk-icon { background: #fff7e6; i { color: #FA8C16; } } }
  &.kpi-danger { border-left-color: #FF4D4F; .bk-icon { background: #fff1f0; i { color: #FF4D4F; } } }
}
.budget-progress { display: flex; align-items: center; gap: 8px; ::v-deep .el-progress { flex: 1; } }
.budget-table-header { margin-bottom: 10px; }
.tab-tip { font-size: 12px; color: #909399; margin-bottom: 10px; i { margin-right: 4px; color: #1A3A5C; } }
.score-red { color: #FF4D4F; font-weight: 700; }
.score-orange { color: #FA8C16; font-weight: 600; }
.score-green { color: #52C41A; font-weight: 600; }
.empty-tip { text-align: center; padding: 40px 0; color: #909399; font-size: 13px; i { margin-right: 6px; } }
::v-deep .el-table th { background: #f0f4f8; }
</style>
