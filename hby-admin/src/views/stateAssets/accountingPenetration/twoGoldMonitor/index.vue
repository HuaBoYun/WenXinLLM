<template>
  <div class="app-container accounting-page">
    <div class="ap-header" :style="{ background: 'linear-gradient(135deg, ' + themeColor + 'cc 0%, ' + themeColor + ' 100%)' }">
      <div class="ap-header-left"><i class="el-icon-coin"></i><span>"两金"压降监控</span></div>
      <div class="ap-header-desc">应收账款 + 存货全穿透 · 期末突击造假识别</div>
    </div>

    <!-- KPI -->
    <el-row :gutter="16" style="margin-bottom:14px">
      <el-col :span="4" v-for="(k, i) in kpis" :key="i">
        <div class="tg-kpi" :class="k.cls">
          <div class="tk-top">
            <i :class="k.icon"></i>
            <span class="tk-tag">{{ k.tag }}</span>
          </div>
          <div class="tk-val">{{ k.val }}</div>
          <div class="tk-label">{{ k.label }}</div>
        </div>
      </el-col>
    </el-row>

    <el-card shadow="never" class="search-card" style="margin-bottom:12px">
      <el-form :inline="true" size="small">
        <el-form-item label="企业">
          <el-input v-model="queryForm.companyName" placeholder="企业名称" clearable style="width:160px" />
        </el-form-item>
        <el-form-item label="期间">
          <el-date-picker v-model="queryForm.month" type="month" value-format="yyyy-MM" placeholder="选择月份" style="width:130px" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleQuery">查询</el-button>
          <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card shadow="never">
      <el-tabs v-model="activeTab">
        <!-- 两金总览 -->
        <el-tab-pane label="两金总览" name="overview">
          <el-table v-loading="loading" :data="overviewList" border size="small" style="width:100%">
            <el-table-column label="企业名称" prop="companyName" min-width="130" show-overflow-tooltip />
            <el-table-column label="应收账款(万)" prop="receivable" width="120" align="right">
              <template slot-scope="s">{{ formatMoney(s.row.receivable) }}</template>
            </el-table-column>
            <el-table-column label="存货(万)" prop="inventory" width="110" align="right">
              <template slot-scope="s">{{ formatMoney(s.row.inventory) }}</template>
            </el-table-column>
            <el-table-column label="两金合计(万)" prop="total" width="120" align="right">
              <template slot-scope="s"><strong>{{ formatMoney(s.row.total) }}</strong></template>
            </el-table-column>
            <el-table-column label="占收入比" prop="revenueRate" width="90" align="center">
              <template slot-scope="s">
                <span :class="s.row.revenueRate > 0.6 ? 'score-red' : ''">{{ (s.row.revenueRate * 100).toFixed(1) }}%</span>
              </template>
            </el-table-column>
            <el-table-column label="压降目标(万)" prop="targetReduce" width="120" align="right" />
            <el-table-column label="压降完成率" prop="reduceRate" width="100" align="center">
              <template slot-scope="s">
                <span :class="s.row.reduceRate < 1 ? 'score-red' : 'score-green'">{{ (s.row.reduceRate * 100).toFixed(0) }}%</span>
              </template>
            </el-table-column>
            <el-table-column label="风险标记" prop="riskFlag" width="110" align="center">
              <template slot-scope="s">
                <el-tag v-if="s.row.riskFlag" :type="s.row.riskFlag === 'RUSH' ? 'danger' : 'warning'" size="mini">
                  {{ s.row.riskFlag === 'RUSH' ? '期末突击' : s.row.riskFlag === 'FAKE_TRANSFER' ? '虚假转移' : '关注' }}
                </el-tag>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>

        <!-- 应收账款穿透 -->
        <el-tab-pane label="应收账款穿透" name="receivable">
          <div class="section-tip">
            <el-tag type="danger" size="mini" style="margin-right:8px">红色预警</el-tag>3年以上未收回应收：占比>20%
            <el-tag type="warning" size="mini" style="margin:0 8px">橙色预警</el-tag>1年以上应收：占比>20%
          </div>
          <el-table :data="receivableList" border size="small" style="width:100%">
            <el-table-column label="企业" prop="companyName" min-width="110" show-overflow-tooltip />
            <el-table-column label="客户名称" prop="customerName" min-width="120" show-overflow-tooltip />
            <el-table-column label="应收余额(万)" prop="balance" width="110" align="right">
              <template slot-scope="s">{{ formatMoney(s.row.balance) }}</template>
            </el-table-column>
            <el-table-column label="1年以内" prop="within1y" width="100" align="right" />
            <el-table-column label="1-2年" prop="y1to2" width="90" align="right" />
            <el-table-column label="2-3年" prop="y2to3" width="90" align="right" />
            <el-table-column label="3年以上" prop="over3y" width="90" align="right">
              <template slot-scope="s">
                <span :class="s.row.over3yRate > 0.2 ? 'score-red' : ''">{{ s.row.over3y }}</span>
              </template>
            </el-table-column>
            <el-table-column label="坏账准备" prop="badDebt" width="90" align="right" />
            <el-table-column label="风险" prop="risk" width="80" align="center">
              <template slot-scope="s">
                <el-tag v-if="s.row.risk" :type="s.row.risk === 'HIGH' ? 'danger' : 'warning'" size="mini">{{ s.row.risk === 'HIGH' ? '高' : '中' }}</el-tag>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>

        <!-- 存货穿透 -->
        <el-tab-pane label="存货穿透" name="inventory">
          <el-table :data="inventoryList" border size="small" style="width:100%">
            <el-table-column label="企业" prop="companyName" min-width="110" show-overflow-tooltip />
            <el-table-column label="存货类别" prop="category" min-width="120" />
            <el-table-column label="账面金额(万)" prop="bookValue" width="120" align="right">
              <template slot-scope="s">{{ formatMoney(s.row.bookValue) }}</template>
            </el-table-column>
            <el-table-column label="跌价准备" prop="impairment" width="100" align="right" />
            <el-table-column label="库龄>12月" prop="slow" width="100" align="right">
              <template slot-scope="s">
                <span :class="s.row.slowRate > 0.2 ? 'score-red' : ''">{{ s.row.slow }}</span>
              </template>
            </el-table-column>
            <el-table-column label="周转率" prop="turnover" width="90" align="center">
              <template slot-scope="s">
                <span :class="s.row.turnover < 3 ? 'score-orange' : ''">{{ s.row.turnover }}次</span>
              </template>
            </el-table-column>
            <el-table-column label="风险" prop="risk" width="80" align="center">
              <template slot-scope="s">
                <el-tag v-if="s.row.risk" :type="s.row.risk === 'HIGH' ? 'danger' : 'warning'" size="mini">{{ s.row.risk === 'HIGH' ? '积压' : '关注' }}</el-tag>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>

        <!-- 造假风险识别 -->
        <el-tab-pane label="造假风险识别" name="fraud">
          <div class="fraud-tip">
            <i class="el-icon-s-flag" style="color:#FF4D4F;margin-right:6px"></i>
            <strong style="color:#FF4D4F">重点打击："两金"压降造假</strong>
            <span style="margin-left:12px;color:#606266;font-size:12px">系统自动识别期末突击压降、关联方虚假转移等造假行为</span>
          </div>
          <el-table :data="fraudList" border size="small" style="width:100%">
            <el-table-column label="企业名称" prop="companyName" min-width="130" show-overflow-tooltip />
            <el-table-column label="造假类型" prop="fraudTypeName" min-width="130">
              <template slot-scope="s">
                <el-tag type="danger" size="mini">{{ s.row.fraudTypeName }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column label="识别规则" prop="rule" min-width="200" show-overflow-tooltip />
            <el-table-column label="风险值" prop="riskScore" width="80" align="center">
              <template slot-scope="s">
                <span :class="s.row.riskScore >= 70 ? 'score-red' : 'score-orange'">{{ s.row.riskScore }}</span>
              </template>
            </el-table-column>
            <el-table-column label="预警级别" prop="alertLevel" width="90" align="center">
              <template slot-scope="s">
                <el-tag :type="s.row.alertLevel === 'RED' ? 'danger' : 'warning'" size="mini">{{ s.row.alertLevel === 'RED' ? '红色' : '橙色' }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column label="发现时间" prop="foundTime" width="100" align="center" />
            <el-table-column label="处置状态" prop="status" width="90" align="center">
              <template slot-scope="s">
                <el-tag :type="{ PENDING: 'warning', PROCESSING: 'primary', DONE: 'success' }[s.row.status]" size="mini">
                  {{ { PENDING: '待核查', PROCESSING: '核查中', DONE: '已处置' }[s.row.status] }}
                </el-tag>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
      </el-tabs>
      <el-pagination
        v-if="pagination.totalRecord > 0"
        style="margin-top:12px;text-align:right"
        background
        layout="total, sizes, prev, pager, next"
        :total="pagination.totalRecord"
        :page-size="pagination.pageSize"
        :current-page="pagination.pageNumber"
        :page-sizes="[10, 20, 50]"
        @current-change="handlePageChange"
        @size-change="handleSizeChange"
      />
    </el-card>
  </div>
</template>

<script>
import { getTwoGoldOverview, getReceivableList, getInventoryList, getTwoGoldReductionList, getTwoGoldKpi } from '@/api/stateAssets/accountingPenetration'
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
  name: 'AccountingTwoGoldMonitor',
  data() {
    return {
      loading: false,
      activeTab: 'overview',
      queryForm: { companyName: '', month: '' },
      kpis: [],
      overviewList: [],
      receivableList: [],
      inventoryList: [],
      fraudList: [],
      pagination: { pageNumber: 1, pageSize: 10, totalRecord: 0 },
    }
  },
  created() { this.loadData() },
  watch: {
    activeTab() {
      this.pagination.pageNumber = 1
      this.loadActiveTab()
    }
  },
  methods: {
    async loadData() {
      try {
        const kRes = await getTwoGoldKpi(this.queryForm)
        if (kRes && kRes.result === 200 && kRes.data && kRes.data.kpis) {
          this.kpis = kRes.data.kpis
        }
      } catch (e) { this.kpis = [] }
      this.loadActiveTab()
    },
    /** 根据当前激活的tab加载对应数据 */
    loadActiveTab() {
      if (this.activeTab === 'overview') this.loadOverviewList()
      else if (this.activeTab === 'receivable') this.loadReceivableList()
      else if (this.activeTab === 'inventory') this.loadInventoryList()
      else if (this.activeTab === 'fraud') this.loadFraudList()
    },
    async loadOverviewList() {
      this.loading = true
      try {
        const params = { ...this.queryForm, pageNumber: this.pagination.pageNumber, pageSize: this.pagination.pageSize }
        const res = await getTwoGoldOverview(params)
        if (res && res.result === 200 && res.data) {
          this.overviewList = res.data.tlist || []
          this.pagination.totalRecord = res.data.totalRecord || 0
        } else { this.overviewList = []; this.pagination.totalRecord = 0 }
      } catch (e) { this.overviewList = []; this.pagination.totalRecord = 0 } finally { this.loading = false }
    },
    async loadReceivableList() {
      this.loading = true
      try {
        const params = { ...this.queryForm, pageNumber: this.pagination.pageNumber, pageSize: this.pagination.pageSize }
        const res = await getReceivableList(params)
        if (res && res.result === 200 && res.data) {
          this.receivableList = res.data.tlist || []
          this.pagination.totalRecord = res.data.totalRecord || 0
        } else { this.receivableList = []; this.pagination.totalRecord = 0 }
      } catch (e) { this.receivableList = []; this.pagination.totalRecord = 0 } finally { this.loading = false }
    },
    async loadInventoryList() {
      this.loading = true
      try {
        const params = { ...this.queryForm, pageNumber: this.pagination.pageNumber, pageSize: this.pagination.pageSize }
        const res = await getInventoryList(params)
        if (res && res.result === 200 && res.data) {
          this.inventoryList = res.data.tlist || []
          this.pagination.totalRecord = res.data.totalRecord || 0
        } else { this.inventoryList = []; this.pagination.totalRecord = 0 }
      } catch (e) { this.inventoryList = []; this.pagination.totalRecord = 0 } finally { this.loading = false }
    },
    async loadFraudList() {
      this.loading = true
      try {
        const params = { ...this.queryForm, pageNumber: this.pagination.pageNumber, pageSize: this.pagination.pageSize }
        const res = await getTwoGoldReductionList(params)
        if (res && res.result === 200 && res.data) {
          this.fraudList = res.data.tlist || []
          this.pagination.totalRecord = res.data.totalRecord || 0
        } else { this.fraudList = []; this.pagination.totalRecord = 0 }
      } catch (e) { this.fraudList = []; this.pagination.totalRecord = 0 } finally { this.loading = false }
    },
    handleQuery() { this.pagination.pageNumber = 1; this.loadData() },
    resetQuery() { this.queryForm = { companyName: '', month: '' }; this.pagination.pageNumber = 1; this.loadData() },
    handlePageChange(page) { this.pagination.pageNumber = page; this.loadActiveTab() },
    handleSizeChange(size) { this.pagination.pageSize = size; this.pagination.pageNumber = 1; this.loadActiveTab() },
    formatMoney(v) {
      if (!v && v !== 0) return '-'
      return Number(v).toLocaleString('zh-CN', { minimumFractionDigits: 0 })
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
.tg-kpi {
  background: #fff; border-radius: 10px; padding: 14px 16px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.07); border-left: 3px solid #d9d9d9;
  .tk-top { display: flex; align-items: center; justify-content: space-between; margin-bottom: 8px;
    i { font-size: 20px; color: #909399; }
    .tk-tag { font-size: 11px; color: #c0c4cc; }
  }
  .tk-val { font-size: 22px; font-weight: 700; color: #303133; }
  .tk-label { font-size: 11px; color: #909399; margin-top: 2px; }
  &.kpi-warn { border-left-color: #FA8C16; .tk-top i { color: #FA8C16; } }
  &.kpi-danger { border-left-color: #FF4D4F; .tk-top i { color: #FF4D4F; } }
}
.section-tip { font-size: 12px; color: #606266; margin-bottom: 10px; display: flex; align-items: center; flex-wrap: wrap; gap: 4px; }
.fraud-tip { display: flex; align-items: center; padding: 8px 12px; background: #fff1f0; border-radius: 6px; margin-bottom: 12px; font-size: 13px; }
.score-red { color: #FF4D4F; font-weight: 700; }
.score-orange { color: #FA8C16; font-weight: 600; }
.score-green { color: #52C41A; font-weight: 600; }
::v-deep .el-table th { background: #f0f4f8; }
</style>
