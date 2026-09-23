<template>
  <div class="state-assets-dashboard">
    <!-- 顶部标题栏 -->
    <div class="dashboard-header">
      <div class="header-left">
        <i class="el-icon-s-platform header-icon"></i>
        <span class="header-title">国有资产穿透监管驾驶舱</span>
        <span class="header-sub">国资委综合监管平台</span>
      </div>
      <div class="header-right">
        <el-button type="primary" size="small" icon="el-icon-refresh" @click="handleRefresh" :loading="globalLoading">
          刷新数据
        </el-button>
        <el-button type="warning" size="small" icon="el-icon-full-screen" @click="openScreen">
          进入大屏
        </el-button>
        <span class="header-time">{{ currentTime }}</span>
      </div>
    </div>

    <!-- 统计汇总条 -->
    <stats-summary-bar
      :stats="statsData"
      :loading="statsLoading"
    />

    <!-- 第二行：组织树 + 领域风险网格 + 汇总 -->
    <div class="row-2">
      <div class="col-org">
        <org-tree-panel
          :org-data="orgData"
          :loading="orgLoading"
        />
      </div>
      <div class="col-domain">
        <domain-risk-grid
          :domain-list="domainList"
          :model-list="modelList"
          :warning-list="warningList"
        />
      </div>
      <div class="col-stats">
        <disposal-status-card
          :stats="statsData"
          :loading="statsLoading"
        />
      </div>
    </div>

    <!-- 第三行：模型组合列表 -->
    <div class="row-3">
      <model-list-card
        :model-list="modelList"
        :loading="modelLoading"
        @select-model="handleSelectModel"
      />
    </div>

    <!-- 第四行：预警数据表格 + 处置 -->
    <div class="row-4">
      <div class="col-warning">
        <warning-table-card
          :warning-list="warningList"
          :total="warningTotal"
          :loading="warningLoading"
          :selected-model-id="selectedModelId"
          @filter-model="handleSelectModel"
          @page-change="handleWarningPageChange"
        />
      </div>
    </div>
  </div>
</template>

<script>
import { findOrganizationData } from '@/api/setting/org'
import { getCombinationList, getRiskWarningList, getWarningStatistics } from '@/api/mxgl'
import StatsSummaryBar from './components/StatsSummaryBar.vue'
import DomainRiskGrid from './components/DomainRiskGrid.vue'
import OrgTreePanel from './components/OrgTreePanel.vue'
import ModelListCard from './components/ModelListCard.vue'
import WarningTableCard from './components/WarningTableCard.vue'
import DisposalStatusCard from './components/DisposalStatusCard.vue'

export default {
  name: 'StateAssetsDashboard',
  components: {
    StatsSummaryBar,
    DomainRiskGrid,
    OrgTreePanel,
    ModelListCard,
    WarningTableCard,
    DisposalStatusCard,
  },
  data() {
    return {
      globalLoading: false,
      orgLoading: false,
      modelLoading: false,
      warningLoading: false,
      statsLoading: false,
      orgData: [],
      modelList: [],
      warningList: [],
      warningTotal: 0,
      statsData: {},
      selectedModelId: '',
      warningQuery: {
        pageNum: 1,
        pageSize: 20,
        evalModelId: '',
      },
      currentTime: '',
      _timer: null,
      domainList: [
        { key: 'invest', name: '投资穿透', icon: 'el-icon-money', path: '/stateAssets/investPenetration/home' },
        { key: 'equity', name: '金融穿透', icon: 'el-icon-bank-card', path: '/stateAssets/equityPenetration/home' },
        { key: 'procurement', name: '采购穿透', icon: 'el-icon-shopping-cart-1', path: '/stateAssets/procurementPenetration/home' },
        { key: 'military', name: '军品穿透', icon: 'el-icon-star-on', path: '/stateAssets/militaryPenetration/home' },
        { key: 'overseas', name: '境外穿透', icon: 'el-icon-location', path: '/stateAssets/overseasPenetration/home' },
        { key: 'industry', name: '行业穿透', icon: 'el-icon-office-building', path: '/stateAssets/industryPenetration/home' },
        { key: 'contract', name: '合同穿透', icon: 'el-icon-document', path: '/stateAssets/contractPenetration/home' },
        { key: 'accounting', name: '会计穿透', icon: 'el-icon-s-order', path: '/stateAssets/accountingPenetration/home' },
        { key: 'financial', name: '财务穿透', icon: 'el-icon-s-finance', path: '/stateAssets/financialPenetration/home' },
        { key: 'fund', name: '资金穿透', icon: 'el-icon-coin', path: '/stateAssets/fundPenetration/home' },
        { key: 'salary', name: '薪酬穿透', icon: 'el-icon-user', path: '/stateAssets/salaryPenetration/home' },
        { key: 'property', name: '产权穿透', icon: 'el-icon-s-shop', path: '/stateAssets/propertyPenetration/home' },
      ],
    }
  },
  mounted() {
    this.updateTime()
    this._timer = setInterval(this.updateTime, 1000)
    this.loadAllData()
  },
  beforeDestroy() {
    if (this._timer) clearInterval(this._timer)
  },
  methods: {
    updateTime() {
      const now = new Date()
      const pad = (n) => String(n).padStart(2, '0')
      this.currentTime = `${now.getFullYear()}-${pad(now.getMonth() + 1)}-${pad(now.getDate())} ${pad(now.getHours())}:${pad(now.getMinutes())}:${pad(now.getSeconds())}`
    },
    async loadAllData() {
      this.globalLoading = true
      await Promise.allSettled([
        this.loadOrgData(),
        this.loadModelList(),
        this.loadWarningList(),
        this.loadStats(),
      ])
      this.globalLoading = false
    },
    async loadOrgData() {
      this.orgLoading = true
      try {
        const res = await findOrganizationData({})
        if (res && res.data) {
          this.orgData = Array.isArray(res.data) ? res.data : [res.data]
        } else if (res && res.code === 1) {
          this.orgData = Array.isArray(res.data) ? res.data : (res.data ? [res.data] : [])
        }
      } catch (e) {
        console.error('加载组织树失败', e)
      } finally {
        this.orgLoading = false
      }
    },
    async loadModelList() {
      this.modelLoading = true
      try {
        const res = await getCombinationList({ pageNum: 1, pageSize: 100 })
        if (res) {
          const data = res.data || res
          this.modelList = data.list || data.records || (Array.isArray(data) ? data : [])
        }
      } catch (e) {
        console.error('加载模型列表失败', e)
      } finally {
        this.modelLoading = false
      }
    },
    async loadWarningList() {
      this.warningLoading = true
      try {
        const res = await getRiskWarningList(this.warningQuery)
        if (res && res.code === 1 && res.data) {
          this.warningList = res.data.list || []
          this.warningTotal = res.data.total || 0
        } else if (res && res.data) {
          this.warningList = res.data.list || []
          this.warningTotal = res.data.total || 0
        }
      } catch (e) {
        console.error('加载预警列表失败', e)
      } finally {
        this.warningLoading = false
      }
    },
    async loadStats() {
      this.statsLoading = true
      try {
        const res = await getWarningStatistics()
        if (res) {
          const data = res.data || res
          this.statsData = {
            totalCount: data.totalCount || data.total || 0,
            pendingCount: data.pendingCount || (data.statusStatistics && data.statusStatistics['PENDING']) || 0,
            processedCount: data.processedCount || (data.statusStatistics && data.statusStatistics['PROCESSED']) || 0,
            highRiskCount: data.highRiskCount || (data.levelStatistics && data.levelStatistics['HIGH']) || 0,
            todayCount: data.todayCount || 0,
            modelCount: this.modelList.length,
          }
        }
      } catch (e) {
        console.error('加载统计数据失败', e)
      } finally {
        this.statsLoading = false
      }
    },
    handleRefresh() {
      this.loadAllData()
    },
    openScreen() {
      const route = this.$router.resolve({ name: 'StateAssetsScreen' })
      window.open(route.href, '_blank')
    },
    handleSelectModel(modelId) {
      this.selectedModelId = modelId
      this.warningQuery.evalModelId = modelId
      this.warningQuery.pageNum = 1
      this.loadWarningList()
    },
    handleWarningPageChange(page) {
      this.warningQuery.pageNum = page
      this.loadWarningList()
    },
  },
}
</script>

<style lang="scss" scoped>
.state-assets-dashboard {
  min-height: 100vh;
  background: linear-gradient(135deg, #e8f4fd 0%, #c3e0f5 50%, #a8d4f0 100%);
  padding: 12px;
  box-sizing: border-box;

  .dashboard-header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    background: linear-gradient(90deg, #1565c0 0%, #1976d2 50%, #1e88e5 100%);
    border-radius: 8px;
    padding: 12px 20px;
    margin-bottom: 12px;
    box-shadow: 0 4px 12px rgba(21, 101, 192, 0.3);

    .header-left {
      display: flex;
      align-items: center;
      gap: 10px;

      .header-icon {
        font-size: 24px;
        color: #ffd54f;
      }

      .header-title {
        font-size: 20px;
        font-weight: 700;
        color: #ffffff;
        letter-spacing: 2px;
      }

      .header-sub {
        font-size: 12px;
        color: rgba(255, 255, 255, 0.7);
        margin-left: 6px;
        padding: 2px 8px;
        border: 1px solid rgba(255, 255, 255, 0.3);
        border-radius: 10px;
      }
    }

    .header-right {
      display: flex;
      align-items: center;
      gap: 10px;

      .header-time {
        color: #e3f2fd;
        font-size: 13px;
        font-family: monospace;
        margin-left: 10px;
      }
    }
  }

  .row-2 {
    display: grid;
    grid-template-columns: 1fr 2.5fr 1fr;
    gap: 12px;
    margin-bottom: 12px;

    .col-org,
    .col-domain,
    .col-stats {
      min-height: 360px;
    }
  }

  .row-3 {
    margin-bottom: 12px;
  }

  .row-4 {
    display: grid;
    grid-template-columns: 1fr;
    gap: 12px;

    .col-warning {
      min-height: 300px;
    }
  }
}

// 通用卡片样式（供子组件继承参考）
:deep(.dashboard-card) {
  background: rgba(255, 255, 255, 0.95);
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
  overflow: hidden;
  height: 100%;
  display: flex;
  flex-direction: column;

  .card-header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 10px 16px;
    background: linear-gradient(90deg, #f5f7fa 0%, #ffffff 100%);
    border-bottom: 2px solid #e3f2fd;

    .card-title {
      font-size: 14px;
      font-weight: 600;
      color: #1565c0;
      display: flex;
      align-items: center;
      gap: 6px;

      i {
        color: #1976d2;
      }
    }
  }

  .card-body {
    flex: 1;
    padding: 12px;
    overflow: auto;
  }
}

@media (max-width: 1400px) {
  .row-2 {
    grid-template-columns: 1fr 2fr 1fr !important;
  }
}

@media (max-width: 1200px) {
  .row-2 {
    grid-template-columns: 1fr 1fr !important;
    .col-stats {
      grid-column: 1 / -1;
    }
  }
}
</style>
