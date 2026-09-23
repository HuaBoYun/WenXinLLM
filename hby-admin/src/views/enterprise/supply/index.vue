<template>
  <div class="supply-chain-management" :style="themeVars">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <h1 class="page-title">
          <i class="el-icon-truck"></i>
          供应链管理
        </h1>
        <p class="page-description">供应商管理、采购管理、库存管理、供应链风险管控一体化平台</p>
      </div>
    </div>

    <!-- 统计概览 -->
    <div class="stats-overview">
      <el-row :gutter="20">
        <el-col :span="6">
          <div class="stats-card">
            <div class="stats-icon supplier">
              <i class="el-icon-office-building"></i>
            </div>
            <div class="stats-content">
              <div class="stats-value">{{ supplyStats.totalSuppliers }}</div>
              <div class="stats-label">合作供应商</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stats-card">
            <div class="stats-icon procurement">
              <i class="el-icon-shopping-cart-2"></i>
            </div>
            <div class="stats-content">
              <div class="stats-value">{{ supplyStats.monthlyProcurement }}</div>
              <div class="stats-label">本月采购(万)</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stats-card">
            <div class="stats-icon inventory">
              <i class="el-icon-box"></i>
            </div>
            <div class="stats-content">
              <div class="stats-value">{{ supplyStats.inventoryValue }}</div>
              <div class="stats-label">库存价值(万)</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stats-card">
            <div class="stats-icon risk">
              <i class="el-icon-warning"></i>
            </div>
            <div class="stats-content">
              <div class="stats-value">{{ supplyStats.riskAlerts }}</div>
              <div class="stats-label">风险预警</div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 功能导航 -->
    <div class="function-nav">
      <el-tabs v-model="activeTab" @tab-click="handleTabClick">
        <el-tab-pane label="供应商管理" name="supplier">
          <SupplierManagement ref="supplierRef" />
        </el-tab-pane>
        <el-tab-pane label="采购管理" name="procurement">
          <ProcurementManagement ref="procurementRef" />
        </el-tab-pane>
        <el-tab-pane label="合同管理" name="contract">
          <ContractManagement ref="contractRef" />
        </el-tab-pane>
        <el-tab-pane label="库存管理" name="inventory">
          <InventoryManagement ref="inventoryRef" />
        </el-tab-pane>
        <el-tab-pane label="质量管理" name="quality">
          <QualityManagement ref="qualityRef" />
        </el-tab-pane>
        <el-tab-pane label="风险管控" name="risk">
          <SupplyRiskManagement ref="riskRef" />
        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>

<script>
import { getSupplyStatistics } from '@/api/enterprise/supply'
import SupplierManagement from './components/SupplierManagement.vue'
import ProcurementManagement from './components/ProcurementManagement.vue'
import ContractManagement from './components/ContractManagement.vue'
import InventoryManagement from './components/InventoryManagement.vue'
import QualityManagement from './components/QualityManagement.vue'
import SupplyRiskManagement from './components/SupplyRiskManagement.vue'
import { investThemeMixin } from '../../stateAssets/themeMixin'

export default {
  name: 'SupplyChainManagement',
  mixins: [investThemeMixin],
  components: {
    SupplierManagement,
    ProcurementManagement,
    ContractManagement,
    InventoryManagement,
    QualityManagement,
    SupplyRiskManagement
  },
  data() {
    return {
      activeTab: 'supplier',
      supplyStats: {
        totalSuppliers: 0,
        monthlyProcurement: 0,
        inventoryValue: 0,
        riskAlerts: 0
      }
    }
  },
  mounted() {
    this.loadSupplyStats()
  },
  methods: {
    // 加载供应链统计数据
    async loadSupplyStats() {
      try {
        const res = await getSupplyStatistics()
        if (res.data) {
          this.supplyStats = res.data
        }
      } catch (error) {
        console.error('加载供应链统计数据失败:', error)
      }
    },

    // 处理标签页切换
    handleTabClick(tab) {
      console.log('切换到标签页:', tab.name)
    }
  }
}
</script>

<style lang="scss" scoped>
.supply-chain-management {
  padding: 20px;
  background-color: #f5f7fa;
  min-height: calc(100vh - 84px);

  .page-header {
    background: linear-gradient(135deg, var(--ip-primary) 0%, var(--ip-secondary) 100%);
    border-radius: 12px;
    padding: 30px;
    margin-bottom: 20px;
    color: white;

    .header-content {
      .page-title {
        font-size: 28px;
        font-weight: 600;
        margin: 0 0 10px 0;
        display: flex;
        align-items: center;

        i {
          margin-right: 12px;
          font-size: 32px;
        }
      }

      .page-description {
        font-size: 16px;
        opacity: 0.9;
        margin: 0;
      }
    }
  }

  .stats-overview {
    margin-bottom: 20px;

    .stats-card {
      background: white;
      border-radius: 12px;
      padding: 24px;
      box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
      display: flex;
      align-items: center;
      transition: transform 0.3s ease;

      &:hover {
        transform: translateY(-2px);
      }

      .stats-icon {
        width: 60px;
        height: 60px;
        border-radius: 12px;
        display: flex;
        align-items: center;
        justify-content: center;
        margin-right: 16px;

        i {
          font-size: 24px;
          color: white;
        }

        &.supplier {
          background: linear-gradient(135deg, var(--ip-primary) 0%, var(--ip-secondary) 100%);
        }

        &.procurement {
          background: linear-gradient(135deg, #66bb6a 0%, #43a047 100%);
        }

        &.inventory {
          background: linear-gradient(135deg, #ffa726 0%, #ff9800 100%);
        }

        &.risk {
          background: linear-gradient(135deg, #ef5350 0%, #e53935 100%);
        }
      }

      .stats-content {
        .stats-value {
          font-size: 24px;
          font-weight: 600;
          color: #303133;
          margin-bottom: 4px;
        }

        .stats-label {
          font-size: 14px;
          color: #909399;
        }
      }
    }
  }

  .function-nav {
    background: white;
    border-radius: 12px;
    padding: 20px;
    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);

    :deep(.el-tabs__header) {
      margin-bottom: 20px;
    }

    :deep(.el-tabs__nav-wrap::after) {
      display: none;
    }

    :deep(.el-tabs__item) {
      font-size: 16px;
      font-weight: 500;
      padding: 0 20px;
    }

    :deep(.el-tabs__item.is-active) {
      color: var(--ip-primary);
    }

    :deep(.el-tabs__active-bar) {
      background-color: var(--ip-primary);
    }
  }
}
</style>
