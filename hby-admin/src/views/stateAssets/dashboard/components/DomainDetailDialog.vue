<template>
  <el-dialog
    :title="domain ? domain.name + ' - 穿透监管详情' : ''"
    :visible.sync="dialogVisible"
    width="720px"
    :close-on-click-modal="false"
    custom-class="domain-detail-dialog"
    @close="handleClose"
  >
    <div v-if="domain" class="domain-detail-content">
      <!-- 顶部：领域信息 -->
      <div class="detail-header" :style="{ borderLeft: '4px solid ' + domain.color }">
        <div class="dh-icon" :style="{ background: domain.color + '18' }">
          <i :class="domain.icon" :style="{ color: domain.color, fontSize: '28px' }"></i>
        </div>
        <div class="dh-info">
          <div class="dh-name">{{ domain.name }}</div>
          <div class="dh-desc">{{ domain.desc }}</div>
        </div>
        <el-badge
          v-if="alertCount > 0"
          :value="alertCount"
          type="danger"
          class="dh-badge"
        />
      </div>

      <!-- 统计数据区 -->
      <div class="detail-stats">
        <div class="stat-item" v-for="(stat, idx) in stats" :key="idx">
          <div class="stat-value" :class="stat.cls">{{ stat.value }}</div>
          <div class="stat-label">{{ stat.label }}</div>
        </div>
      </div>

      <!-- 预警信息区 -->
      <div class="detail-alerts" v-if="alertCount > 0">
        <div class="alerts-title">
          <i class="el-icon-warning" style="color: #F56C6C"></i>
          风险预警提示
        </div>
        <div class="alerts-desc">
          当前领域存在 <span class="alert-num">{{ alertCount }}</span> 条风险预警，建议及时处理。
        </div>
      </div>

      <!-- 领域说明 -->
      <div class="detail-description">
        <div class="desc-title">监管范围</div>
        <div class="desc-text">{{ domainDescription }}</div>
      </div>
    </div>

    <!-- 底部操作 -->
    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">关 闭</el-button>
      <el-button type="primary" @click="goDetail">
        <i class="el-icon-arrow-right"></i> 进入详情页
      </el-button>
      <el-button type="warning" plain @click="goDashboard">
        <i class="el-icon-odometer"></i> 驾驶舱
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
/**
 * 穿透监管领域详情弹窗
 * @description 展示单个领域的统计数据、预警信息，提供跳转入口
 */
export default {
  name: 'DomainDetailDialog',
  props: {
    visible: { type: Boolean, default: false },
    domain: { type: Object, default: null },
    stats: { type: Array, default: () => [] },
    alertCount: { type: Number, default: 0 }
  },
  computed: {
    dialogVisible: {
      get() { return this.visible },
      set(val) { this.$emit('update:visible', val) }
    },
    domainDescription() {
      if (!this.domain) return ''
      const descMap = {
        invest: '覆盖投资项目立项、审批、执行、退出全生命周期，实时监控投资风险敞口与合规情况。',
        financial: '穿透监管融资、担保、金融衍生品等业务，识别金融风险传导链路。',
        procurement: '全链条监管采购计划、招标、合同签订、验收付款，防范围标串标与利益输送。',
        military: '监管军品科研生产任务执行、资质管理、保密合规，确保军品业务安全可控。',
        overseas: '监控境外单位经营状况、资金流向、合规风险，防范境外资产流失。',
        industry: '分析行业布局合理性、市场竞争力、产业链协同效应，支撑战略决策。',
        contract: '合同签订、履行、变更、终止全过程监管，识别合同风险与违约预警。',
        accounting: '穿透监管会计凭证、账簿、报表，识别异常会计处理与财务造假风险。',
        finance: '财务报表合规性穿透分析，识别财务指标异常与粉饰报表行为。',
        fund: '资金流向全链路穿透监控，识别异常资金划转、体外循环等风险。',
        salary: '薪酬分配合规性监管，识别高管薪酬异常、违规发放等问题。',
        property: '产权登记、评估、交易全链条监管，防范国有资产流失。'
      }
      return descMap[this.domain.key] || this.domain.desc
    }
  },
  methods: {
    handleClose() {
      this.$emit('update:visible', false)
      this.$emit('close')
    },
    goDetail() {
      if (this.domain && this.domain.route) {
        this.$router.push(this.domain.route)
        this.handleClose()
      }
    },
    goDashboard() {
      const route = this.domain && (this.domain.dashboardRoute || this.domain.route)
      if (route) {
        this.$router.push(route)
        this.handleClose()
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.domain-detail-content {
  padding: 0 4px;

  .detail-header {
    display: flex;
    align-items: center;
    padding: 16px;
    background: #f8f9ff;
    border-radius: 8px;
    margin-bottom: 20px;

    .dh-icon {
      width: 52px; height: 52px;
      border-radius: 10px;
      display: flex; align-items: center; justify-content: center;
      margin-right: 14px;
      flex-shrink: 0;
    }

    .dh-info {
      flex: 1;
      .dh-name { font-size: 18px; font-weight: 700; color: #1a237e; margin-bottom: 4px; }
      .dh-desc { font-size: 13px; color: #7986cb; }
    }
  }

  .detail-stats {
    display: flex;
    gap: 12px;
    margin-bottom: 20px;

    .stat-item {
      flex: 1;
      text-align: center;
      padding: 16px 12px;
      background: #f5f7ff;
      border-radius: 8px;
      border: 1px solid #eef2ff;

      .stat-value {
        font-size: 24px; font-weight: 800; color: #1a237e; margin-bottom: 6px;
        &.danger  { color: #c62828; }
        &.warning { color: #e65100; }
        &.success { color: #2e7d32; }
      }
      .stat-label { font-size: 12px; color: #7986cb; }
    }
  }

  .detail-alerts {
    background: #fff3f3;
    border: 1px solid #ffcdd2;
    border-radius: 8px;
    padding: 14px 16px;
    margin-bottom: 20px;

    .alerts-title {
      font-size: 14px; font-weight: 600; color: #c62828; margin-bottom: 6px;
      display: flex; align-items: center; gap: 6px;
    }
    .alerts-desc {
      font-size: 13px; color: #666;
      .alert-num { color: #c62828; font-weight: 700; font-size: 15px; }
    }
  }

  .detail-description {
    background: #f9fafb;
    border-radius: 8px;
    padding: 14px 16px;
    border: 1px solid #e8eaf6;

    .desc-title { font-size: 13px; font-weight: 600; color: #1a237e; margin-bottom: 6px; }
    .desc-text { font-size: 13px; color: #555; line-height: 1.7; }
  }
}

.dialog-footer {
  text-align: right;
}
</style>
