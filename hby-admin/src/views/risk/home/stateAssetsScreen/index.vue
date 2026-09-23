<template>
  <div class="state-assets-screen" ref="screenRoot">
    <!-- 顶部标题栏 -->
    <screen-header
      :org-name="rootOrgName"
      @go-back="goBack"
    />

    <!-- 主体内容区 -->
    <div class="screen-body">
      <!-- 左侧：12领域风险面板 -->
      <div class="screen-left">
        <domain-risk-panel
          :domain-list="domainList"
          :model-list="modelList"
          :warning-list="warningList"
        />
      </div>

      <!-- 中央：组织环形展示 -->
      <div class="screen-center">
        <org-ring-display
          :org-data="orgData"
          :loading="orgLoading"
        />
      </div>

      <!-- 右侧：模型统计 + 预警数据 -->
      <div class="screen-right">
        <model-stats-panel
          :model-list="modelList"
          :loading="modelLoading"
          :stats="statsData"
        />
        <disposal-stats-panel
          :stats="statsData"
          :loading="statsLoading"
        />
      </div>
    </div>

    <!-- 底部：预警滚动播报 -->
    <div class="screen-footer">
      <warning-scroll-panel
        :warning-list="warningList"
        :loading="warningLoading"
      />
    </div>
  </div>
</template>

<script>
import { findOrganizationData } from '@/api/setting/org'
import { getCombinationList, getRiskWarningList, getWarningStatistics } from '@/api/mxgl'
import ScreenHeader from './components/ScreenHeader.vue'
import OrgRingDisplay from './components/OrgRingDisplay.vue'
import DomainRiskPanel from './components/DomainRiskPanel.vue'
import ModelStatsPanel from './components/ModelStatsPanel.vue'
import WarningScrollPanel from './components/WarningScrollPanel.vue'
import DisposalStatsPanel from './components/DisposalStatsPanel.vue'

export default {
  name: 'StateAssetsScreen',
  components: {
    ScreenHeader,
    OrgRingDisplay,
    DomainRiskPanel,
    ModelStatsPanel,
    WarningScrollPanel,
    DisposalStatsPanel,
  },
  data() {
    return {
      orgData: [],
      modelList: [],
      warningList: [],
      statsData: {},
      orgLoading: false,
      modelLoading: false,
      warningLoading: false,
      statsLoading: false,
      rootOrgName: '',
      _refreshTimer: null,
      domainList: [
        { key: 'invest', name: '投资穿透', icon: '📈', path: '/stateAssets/investPenetration/home' },
        { key: 'equity', name: '金融穿透', icon: '💰', path: '/stateAssets/equityPenetration/home' },
        { key: 'procurement', name: '采购穿透', icon: '🛒', path: '/stateAssets/procurementPenetration/home' },
        { key: 'military', name: '军品穿透', icon: '⭐', path: '/stateAssets/militaryPenetration/home' },
        { key: 'overseas', name: '境外穿透', icon: '🌐', path: '/stateAssets/overseasPenetration/home' },
        { key: 'industry', name: '行业穿透', icon: '🏭', path: '/stateAssets/industryPenetration/home' },
        { key: 'contract', name: '合同穿透', icon: '📄', path: '/stateAssets/contractPenetration/home' },
        { key: 'accounting', name: '会计穿透', icon: '📊', path: '/stateAssets/accountingPenetration/home' },
        { key: 'financial', name: '财务穿透', icon: '💹', path: '/stateAssets/financialPenetration/home' },
        { key: 'fund', name: '资金穿透', icon: '🏦', path: '/stateAssets/fundPenetration/home' },
        { key: 'salary', name: '薪酬穿透', icon: '👥', path: '/stateAssets/salaryPenetration/home' },
        { key: 'property', name: '产权穿透', icon: '🏢', path: '/stateAssets/propertyPenetration/home' },
      ],
    }
  },
  mounted() {
    this.loadAllData()
    // 每5分钟自动刷新
    this._refreshTimer = setInterval(this.loadAllData, 5 * 60 * 1000)
  },
  beforeDestroy() {
    if (this._refreshTimer) clearInterval(this._refreshTimer)
  },
  methods: {
    async loadAllData() {
      await Promise.allSettled([
        this.loadOrgData(),
        this.loadModelList(),
        this.loadWarningList(),
        this.loadStats(),
      ])
    },
    async loadOrgData() {
      this.orgLoading = true
      try {
        const res = await findOrganizationData({})
        if (res && res.data) {
          const data = Array.isArray(res.data) ? res.data : [res.data]
          this.orgData = data
          if (data.length > 0) {
            this.rootOrgName = data[0].name || data[0].orgName || '国有资产监督管理委员会'
          }
        }
      } catch (e) {
        console.error('加载组织树失败', e)
        this.rootOrgName = '国有资产监督管理委员会'
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
        const res = await getRiskWarningList({ pageNum: 1, pageSize: 50 })
        if (res && res.code === 1 && res.data) {
          this.warningList = res.data.list || []
        } else if (res && res.data) {
          this.warningList = res.data.list || []
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
            processingCount: data.processingCount || (data.statusStatistics && data.statusStatistics['PROCESSING']) || 0,
            ignoredCount: data.ignoredCount || (data.statusStatistics && data.statusStatistics['IGNORED']) || 0,
            highRiskCount: data.highRiskCount || (data.levelStatistics && data.levelStatistics['HIGH']) || 0,
            todayCount: data.todayCount || 0,
            modelCount: this.modelList.length,
          }
        }
      } catch (e) {
        console.error('加载统计失败', e)
      } finally {
        this.statsLoading = false
      }
    },
    goBack() {
      this.$router.push({ name: 'StateAssetsDashboard' }).catch(() => {
        window.close()
      })
    },
  },
}
</script>

<style lang="scss">
/* 全局重置，大屏用全屏模式 */
.state-assets-screen {
  position: fixed;
  inset: 0;
  background: #030f2d;
  display: flex;
  flex-direction: column;
  overflow: hidden;
  font-family: 'PingFang SC', 'Microsoft YaHei', sans-serif;
  z-index: 9999;

  /* 扫描线背景效果 */
  &::before {
    content: '';
    position: absolute;
    inset: 0;
    background: repeating-linear-gradient(
      0deg,
      transparent,
      transparent 2px,
      rgba(0, 200, 255, 0.015) 2px,
      rgba(0, 200, 255, 0.015) 4px
    );
    pointer-events: none;
    z-index: 0;
  }

  > * {
    position: relative;
    z-index: 1;
  }

  .screen-body {
    flex: 1;
    display: grid;
    grid-template-columns: 280px 1fr 320px;
    gap: 12px;
    padding: 0 12px;
    min-height: 0;
    overflow: hidden;

    .screen-left,
    .screen-right {
      display: flex;
      flex-direction: column;
      gap: 10px;
      overflow: hidden;
    }

    .screen-center {
      display: flex;
      align-items: center;
      justify-content: center;
    }
  }

  .screen-footer {
    height: 120px;
    padding: 0 12px 10px;
    flex-shrink: 0;
  }

  @media (max-width: 1400px) {
    .screen-body {
      grid-template-columns: 240px 1fr 280px;
    }
  }
}
</style>
