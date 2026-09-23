<template>
  <div class="accounting-home-container">
    <!-- 顶部Banner：会计穿透特色标识 -->
    <div class="accounting-banner" :style="{ background: 'linear-gradient(135deg, ' + themeColor + 'cc 0%, ' + themeColor + ' 100%)' }">
      <div class="banner-left">
        <div class="banner-icon-wrap">
          <i class="el-icon-s-order banner-icon"></i>
        </div>
        <div class="banner-text">
          <h1 class="banner-title">会计穿透监管</h1>
          <p class="banner-subtitle">穿透凭证链 · 穿透报表链 · 穿透造假链 · 实现"一穿到底"</p>
          <div class="banner-tags">
            <span class="banner-tag">业务</span>
            <i class="el-icon-arrow-right tag-arrow"></i>
            <span class="banner-tag">财务</span>
            <i class="el-icon-arrow-right tag-arrow"></i>
            <span class="banner-tag">合同</span>
            <i class="el-icon-arrow-right tag-arrow"></i>
            <span class="banner-tag">法务</span>
            <i class="el-icon-arrow-right tag-arrow"></i>
            <span class="banner-tag">审计</span>
            <i class="el-icon-arrow-right tag-arrow"></i>
            <span class="banner-tag highlight-tag">一体化穿透</span>
          </div>
        </div>
      </div>
      <div class="banner-right">
        <el-button type="primary" size="small" icon="el-icon-full-screen" @click="openScreen">大屏展示</el-button>
        <el-button class="banner-btn" @click="goTo('dashboard')">
          <i class="el-icon-odometer"></i> 监控驾驶舱
        </el-button>
        <el-button class="banner-btn primary" @click="goTo('fraudDetection')">
          <i class="el-icon-warning"></i> 造假风险识别
        </el-button>
      </div>
    </div>

    <!-- 穿透链路图 -->
    <div class="penetration-chain-card">
      <div class="chain-header">
        <span class="chain-title"><i class="el-icon-share"></i> 会计穿透链路</span>
        <span class="chain-desc">点击任意节点进入对应功能模块</span>
      </div>
      <div class="chain-flow">
        <div
          v-for="(node, idx) in chainNodes"
          :key="idx"
          class="chain-node"
          :class="'chain-node-' + (idx + 1)"
          @click="goTo(node.key)"
        >
          <div class="node-icon"><i :class="node.icon"></i></div>
          <div class="node-label">{{ node.label }}</div>
          <div class="node-sub">{{ node.sub }}</div>
        </div>
        <div v-for="n in 6" :key="'arrow-' + n" class="chain-arrow">
          <i class="el-icon-arrow-right"></i>
        </div>
      </div>
    </div>

    <!-- 核心指标 -->
    <div class="stats-row">
      <div
        v-for="(stat, idx) in statCards"
        :key="idx"
        class="stat-card"
        :class="stat.warning ? 'stat-warning' : ''"
        @click="goTo(stat.key)"
      >
        <div class="stat-icon-wrap" :style="{ background: stat.gradient }">
          <i :class="stat.icon"></i>
        </div>
        <div class="stat-body">
          <div class="stat-value">{{ stat.value }}</div>
          <div class="stat-label">{{ stat.label }}</div>
        </div>
        <div v-if="stat.warning" class="stat-badge">
          <el-badge :value="stat.value" type="danger" />
        </div>
      </div>
    </div>

    <!-- 功能模块 -->
    <div class="modules-section">
      <div class="section-header">
        <span class="section-title"><i class="el-icon-grid"></i> 核心功能模块</span>
      </div>
      <el-row :gutter="20">
        <el-col :span="8" v-for="(mod, idx) in modules" :key="mod.key" style="margin-bottom:20px">
          <div class="module-card" :class="'mc-' + mod.colorKey" @click="goTo(mod.key)">
            <div class="mc-header">
              <div class="mc-icon"><i :class="mod.icon"></i></div>
              <div class="mc-title-wrap">
                <div class="mc-title">{{ mod.title }}</div>
                <div class="mc-desc">{{ mod.desc }}</div>
              </div>
              <div v-if="mod.badge" class="mc-badge">
                <el-tag size="mini" :type="mod.badgeType || 'danger'">{{ mod.badge }}</el-tag>
              </div>
            </div>
            <div class="mc-features">
              <div class="mc-feature" v-for="f in mod.features" :key="f">
                <i class="el-icon-check"></i><span>{{ f }}</span>
              </div>
            </div>
            <div class="mc-footer">
              <span class="mc-enter">进入模块 <i class="el-icon-arrow-right"></i></span>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 底部：造假风险快速识别入口 + 预警摘要 -->
    <el-row :gutter="20" class="bottom-row">
      <el-col :span="14">
        <div class="fraud-entry-card">
          <div class="fraud-header">
            <i class="el-icon-s-flag"></i>
            <span>重点打击 · 五类财务造假</span>
          </div>
          <div class="fraud-types">
            <div
              v-for="(ft, idx) in fraudTypes"
              :key="idx"
              class="fraud-type-item"
              @click="goTo('fraudDetection')"
            >
              <div class="ft-index">{{ idx + 1 }}</div>
              <div class="ft-name">{{ ft.name }}</div>
              <div class="ft-desc">{{ ft.desc }}</div>
              <el-tag size="mini" :type="ft.level === 'red' ? 'danger' : 'warning'">
                {{ ft.level === 'red' ? '高风险' : '中风险' }}
              </el-tag>
            </div>
          </div>
        </div>
      </el-col>
      <el-col :span="10">
        <div class="warning-summary-card">
          <div class="ws-header">
            <i class="el-icon-bell"></i>
            <span>最新预警</span>
            <el-button type="text" size="mini" @click="goTo('dashboard')">查看全部</el-button>
          </div>
          <div class="ws-list">
            <div v-for="(w, idx) in recentWarnings" :key="idx" class="ws-item">
              <el-tag size="mini" :type="{ HIGH: 'danger', MEDIUM: 'warning', LOW: 'info' }[w.level]">
                {{ { HIGH: '高', MEDIUM: '中', LOW: '低' }[w.level] }}
              </el-tag>
              <span class="ws-text">{{ w.text }}</span>
              <span class="ws-time">{{ w.time }}</span>
            </div>
          </div>
        </div>
      </el-col>
    </el-row>

    <div v-if="screenVisible" ref="screenContainer" class="fullscreen-container">
      <AccountingScreen @close="closeScreen" />
    </div>
  </div>
</template>

<script>
import { getAccountingDashboard, getAccountingWarningList } from '@/api/stateAssets/accountingPenetration'
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
  name: 'AccountingPenetrationHome',
  components: { AccountingScreen: () => import('../screen/index.vue') },
  data() {
    return {
      screenVisible: false,
      chainNodes: [
        { key: 'voucherPenetration', icon: 'el-icon-document', label: '业务单据', sub: '采购/销售/费用' },
        { key: 'accountingVoucher', icon: 'el-icon-tickets', label: '会计凭证', sub: '记账凭证穿透' },
        { key: 'bookPenetration', icon: 'el-icon-notebook-1', label: '明细账簿', sub: '科目/辅助/日记' },
        { key: 'bookPenetration', icon: 'el-icon-s-data', label: '科目余额', sub: '总账汇总' },
        { key: 'financialReport', icon: 'el-icon-data-analysis', label: '财务报表', sub: '三表一注' },
        { key: 'reportPenetration', icon: 'el-icon-connection', label: '合并报表', sub: '一键生成' },
        { key: 'fraudDetection', icon: 'el-icon-aim', label: '造假识别', sub: '模型穿透分析' },
      ],
      statCards: [
        { key: 'dashboard', icon: 'el-icon-office-building', label: '纳管企业数', value: '-', gradient: 'linear-gradient(135deg,#1A3A5C,#2d6a9f)', warning: false },
        { key: 'reportPenetration', icon: 'el-icon-data-analysis', label: '报表质量评分', value: '-', gradient: 'linear-gradient(135deg,#13c2c2,#006d75)', warning: false },
        { key: 'fraudDetection', icon: 'el-icon-warning', label: '造假风险企业', value: '-', gradient: 'linear-gradient(135deg,#fa541c,#d4380d)', warning: true },
        { key: 'budgetMonitor', icon: 'el-icon-pie-chart', label: '预算超支企业', value: '-', gradient: 'linear-gradient(135deg,#fa8c16,#d46b08)', warning: true },
        { key: 'twoGoldMonitor', icon: 'el-icon-coin', label: '"两金"未达标', value: '-', gradient: 'linear-gradient(135deg,#722ed1,#531dab)', warning: true },
        { key: 'voucherPenetration', icon: 'el-icon-s-check', label: '凭证异常数', value: '-', gradient: 'linear-gradient(135deg,#52c41a,#237804)', warning: false },
      ],
      modules: [
        {
          key: 'dashboard', title: '会计穿透驾驶舱', desc: '全集团会计监管总览', icon: 'el-icon-odometer', colorKey: 'blue',
          features: ['造假风险热力图', '报表质量雷达图', '两金压降趋势', '预警态势总览'],
        },
        {
          key: 'voucherPenetration', title: '会计凭证穿透', desc: '凭证链穿透 + 异常检测', icon: 'el-icon-tickets', colorKey: 'teal',
          badge: '核心', badgeType: 'primary',
          features: ['从凭证穿透到原始单据', '期末突击录凭证识别', '无原始凭证异常标记', '借贷科目不匹配预警'],
        },
        {
          key: 'bookPenetration', title: '账簿穿透查询', desc: '科目余额→总账→明细账联动', icon: 'el-icon-notebook-1', colorKey: 'purple',
          features: ['科目余额表对比', '总账→明细账穿透', '辅助账维度分析', '应收账龄风险识别'],
        },
        {
          key: 'reportPenetration', title: '财务报表穿透', desc: '报表质量评估 + 勾稽核查', icon: 'el-icon-data-analysis', colorKey: 'green',
          badge: '核心', badgeType: 'primary',
          features: ['三表勾稽关系自动校验', '合并报表范围核查', '报表质量五维评分', '历史数据对比分析'],
        },
        {
          key: 'budgetMonitor', title: '预算执行监管', desc: '全面预算穿透 + 超支预警', icon: 'el-icon-pie-chart', colorKey: 'orange',
          features: ['预算执行进度可视化', '超预算自动预警', '期末突击支出识别', '预算调整合规核查'],
        },
        {
          key: 'twoGoldMonitor', title: '"两金"压降监控', desc: '应收账款+存货穿透监管', icon: 'el-icon-coin', colorKey: 'cyan',
          badge: '重点', badgeType: 'warning',
          features: ['期末突击压降造假识别', '应收账款账龄穿透', '存货积压风险预警', '关联方虚假转移识别'],
        },
        {
          key: 'fraudDetection', title: '财务造假识别', desc: '五类造假模型自动检测', icon: 'el-icon-s-flag', colorKey: 'red',
          badge: '关键', badgeType: 'danger',
          features: ['经营业绩造假识别', '财务杠杆造假识别', '出清出表造假识别', '研发统计造假识别'],
        },
        {
          key: 'policyAndEstimate', title: '会计政策与估计', desc: '政策一致性 + 估计变更监控', icon: 'el-icon-document-checked', colorKey: 'gray',
          features: ['跨企业政策一致性检查', '估计变更利润影响分析', '准则合规性评估', '变更审批合规核查'],
        },
      ],
      fraudTypes: [
        { name: '经营业绩假', desc: '收入增长率偏离行业均值>2倍标准差', level: 'red' },
        { name: '财务杠杆假', desc: '表外负债超过表内50%，资产虚增', level: 'red' },
        { name: '出清出表假', desc: '资产处置含回购条款，形式出售实质控制', level: 'red' },
        { name: '研发统计假', desc: '研发资本化率>50%，将费用虚列研发', level: 'medium' },
        { name: '"两金"压降假', desc: '期末突击压降，12月减少>全年50%', level: 'red' },
      ],
      recentWarnings: [],
    }
  },
  mounted() { this.loadData() },
  methods: {
    async loadData() {
      try {
        const res = await getAccountingDashboard({})
        if (res && res.result === 200 && res.data) {
          const d = res.data
          this.statCards[0].value = (d.companyCount || 0) + '家'
          this.statCards[1].value = d.qualityScore || '-'
          this.statCards[2].value = (d.fraudRiskCount || 0) + '家'
          this.statCards[3].value = (d.budgetOverCount || 0) + '家'
          this.statCards[4].value = (d.twoGoldWarnCount || 0) + '家'
          this.statCards[5].value = (d.voucherAnomalyCount || 0) + '张'
        }
      } catch (e) { /* use default */ }
      try {
        const wRes = await getAccountingWarningList({ pageSize: 5 })
        if (wRes && wRes.result === 200 && wRes.data) {
          this.recentWarnings = (wRes.data.tlist || wRes.data.list || []).map(w => ({
            level: w.level || 'MEDIUM',
            text: w.title || w.text || '',
            time: w.time || ''
          }))
        }
      } catch (e) { this.recentWarnings = [] }
    },
    goTo(key) {
      // 按 path 方式跳转，与 /compliance/Cwbbct 风格保持一致
      const pathMap = {
        dashboard: '/compliance/Jkctjkjsc',
        voucherPenetration: '/compliance/Kjpzct',
        accountingVoucher: '/compliance/Kjpzct',
        bookPenetration: '/compliance/Zbctcx',
        financialReport: '/compliance/Cwbbct',
        reportPenetration: '/compliance/Cwbbct',
        budgetMonitor: '/compliance/Yszxjk',
        twoGoldMonitor: '/compliance/Ljysjk',
        fraudDetection: '/compliance/Cwzjsb',
        policyAndEstimate: '/compliance/Kjzcypg',
      }
      const path = pathMap[key]
      if (path) {
        this.$router.push(path).catch(() => {})
      } else {
        this.$message.info(key + ' 功能模块加载中...')
      }
    },
    openScreen() {
      this.screenVisible = true
      this.$nextTick(() => {
        const el = this.$refs.screenContainer
        if (el && el.requestFullscreen) el.requestFullscreen()
        else if (el && el.webkitRequestFullscreen) el.webkitRequestFullscreen()
      })
    },
    closeScreen() {
      this.screenVisible = false
      if (document.exitFullscreen) document.exitFullscreen()
      else if (document.webkitExitFullscreen) document.webkitExitFullscreen()
    },
  },
}
</script>

<style lang="scss" scoped>
.fullscreen-container { position: fixed; top: 0; left: 0; width: 100vw; height: 100vh; z-index: 9999; }
$primary: #1A3A5C;
$accent: #FA8C16;
$teal: #13c2c2;
$bg: #f5f7fa;

.accounting-home-container {
  padding: 20px;
  background: $bg;
  min-height: calc(100vh - 84px);
}

/* ===== Banner ===== */
.accounting-banner {
  display: flex;
  align-items: center;
  justify-content: space-between;
  border-radius: 12px;
  padding: 28px 32px;
  margin-bottom: 20px;
  box-shadow: 0 4px 20px rgba(26,58,92,0.25);
  .banner-left {
    display: flex;
    align-items: center;
    gap: 20px;
  }
  .banner-icon-wrap {
    width: 72px;
    height: 72px;
    border-radius: 16px;
    background: rgba(250,140,22,0.18);
    border: 2px solid rgba(250,140,22,0.5);
    display: flex;
    align-items: center;
    justify-content: center;
    flex-shrink: 0;
    .banner-icon {
      font-size: 36px;
      color: $accent;
    }
  }
  .banner-title {
    font-size: 26px;
    font-weight: 700;
    color: #fff;
    margin: 0 0 4px 0;
  }
  .banner-subtitle {
    font-size: 13px;
    color: rgba(255,255,255,0.7);
    margin: 0 0 12px 0;
  }
  .banner-tags {
    display: flex;
    align-items: center;
    gap: 6px;
    flex-wrap: wrap;
    .banner-tag {
      background: rgba(255,255,255,0.12);
      color: rgba(255,255,255,0.85);
      border-radius: 20px;
      padding: 2px 12px;
      font-size: 12px;
      border: 1px solid rgba(255,255,255,0.2);
    }
    .highlight-tag {
      background: rgba(250,140,22,0.25);
      color: $accent;
      border-color: rgba(250,140,22,0.5);
      font-weight: 600;
    }
    .tag-arrow {
      color: rgba(255,255,255,0.4);
      font-size: 11px;
    }
  }
  .banner-right {
    display: flex;
    flex-direction: column;
    gap: 10px;
  }
  .banner-btn {
    background: rgba(255,255,255,0.1);
    border: 1px solid rgba(255,255,255,0.3);
    color: #fff;
    border-radius: 8px;
    &:hover { background: rgba(255,255,255,0.2); }
    &.primary {
      background: rgba(250,140,22,0.25);
      border-color: rgba(250,140,22,0.6);
      color: $accent;
      &:hover { background: rgba(250,140,22,0.4); }
    }
  }
}

/* ===== 穿透链路图 ===== */
.penetration-chain-card {
  background: #fff;
  border-radius: 12px;
  padding: 20px 24px;
  margin-bottom: 20px;
  box-shadow: 0 2px 12px rgba(0,0,0,0.08);
  .chain-header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    margin-bottom: 16px;
    .chain-title {
      font-size: 15px;
      font-weight: 600;
      color: #303133;
      i { color: $teal; margin-right: 6px; }
    }
    .chain-desc { font-size: 12px; color: #909399; }
  }
  .chain-flow {
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 0;
  }
  .chain-node {
    display: flex;
    flex-direction: column;
    align-items: center;
    background: linear-gradient(135deg, #f0f7ff, #e8f4fd);
    border: 1px solid #c6e2ff;
    border-radius: 10px;
    padding: 12px 14px;
    cursor: pointer;
    transition: all 0.25s;
    min-width: 88px;
    &:hover {
      background: linear-gradient(135deg, #1A3A5C, #2d6a9f);
      border-color: #1A3A5C;
      transform: translateY(-3px);
      box-shadow: 0 6px 16px rgba(26,58,92,0.25);
      .node-icon i, .node-label, .node-sub { color: #fff; }
    }
    &-7 {
      background: linear-gradient(135deg, #fff2e8, #ffe7ba);
      border-color: #ffd591;
      .node-icon i { color: $accent; }
      .node-label { color: #d46b08; }
      &:hover {
        background: linear-gradient(135deg, #fa8c16, #d46b08);
        border-color: #fa8c16;
        .node-icon i, .node-label, .node-sub { color: #fff; }
      }
    }
    .node-icon {
      width: 36px;
      height: 36px;
      border-radius: 8px;
      background: rgba(26,58,92,0.08);
      display: flex;
      align-items: center;
      justify-content: center;
      margin-bottom: 6px;
      i { font-size: 18px; color: $primary; }
    }
    .node-label { font-size: 13px; font-weight: 600; color: #303133; margin-bottom: 2px; }
    .node-sub { font-size: 11px; color: #909399; white-space: nowrap; }
  }
  .chain-arrow {
    color: #c0c4cc;
    font-size: 16px;
    padding: 0 4px;
    flex-shrink: 0;
  }
}

/* ===== 核心指标 ===== */
.stats-row {
  display: flex;
  gap: 16px;
  margin-bottom: 20px;
  .stat-card {
    flex: 1;
    background: #fff;
    border-radius: 10px;
    padding: 16px;
    display: flex;
    align-items: center;
    gap: 12px;
    box-shadow: 0 2px 10px rgba(0,0,0,0.07);
    cursor: pointer;
    transition: all 0.25s;
    position: relative;
    &:hover { transform: translateY(-2px); box-shadow: 0 6px 20px rgba(0,0,0,0.12); }
    &.stat-warning {
      border-left: 3px solid #FF4D4F;
    }
    .stat-icon-wrap {
      width: 48px; height: 48px; border-radius: 10px;
      display: flex; align-items: center; justify-content: center; flex-shrink: 0;
      i { font-size: 22px; color: #fff; }
    }
    .stat-body {
      flex: 1;
      .stat-value { font-size: 22px; font-weight: 700; color: #303133; line-height: 1.2; }
      .stat-label { font-size: 12px; color: #909399; margin-top: 2px; }
    }
  }
}

/* ===== 功能模块 ===== */
.modules-section {
  margin-bottom: 20px;
  .section-header {
    margin-bottom: 16px;
    .section-title {
      font-size: 15px;
      font-weight: 600;
      color: #303133;
      i { color: $primary; margin-right: 6px; }
    }
  }
}

$colorKeys: (
  'blue': (#1A3A5C, #c6d9f0, #e8f0fa),
  'teal': (#13c2c2, #b5e3e3, #e6fffb),
  'purple': (#722ed1, #d3adf7, #f9f0ff),
  'green': (#389e0d, #b7eb8f, #f6ffed),
  'orange': (#fa8c16, #ffd591, #fff7e6),
  'cyan': (#0891b2, #bae6fd, #f0f9ff),
  'red': (#cf1322, #ffa39e, #fff1f0),
  'gray': (#595959, #d9d9d9, #fafafa),
);

.module-card {
  background: #fff;
  border-radius: 12px;
  padding: 20px;
  height: 220px;
  display: flex;
  flex-direction: column;
  cursor: pointer;
  transition: all 0.25s;
  box-shadow: 0 2px 10px rgba(0,0,0,0.07);
  position: relative;
  overflow: hidden;
  &:hover {
    transform: translateY(-4px);
    box-shadow: 0 8px 24px rgba(0,0,0,0.14);
    .mc-enter { opacity: 1; }
  }
  &::before {
    content: '';
    position: absolute;
    top: 0; left: 0; right: 0;
    height: 3px;
  }
  @each $key, $colors in $colorKeys {
    &.mc-#{$key}::before { background: nth($colors, 1); }
    &.mc-#{$key} .mc-icon {
      background: nth($colors, 3);
      color: nth($colors, 1);
      i { color: nth($colors, 1); }
    }
  }
}
.mc-header {
  display: flex;
  align-items: flex-start;
  gap: 12px;
  margin-bottom: 12px;
  .mc-icon {
    width: 42px; height: 42px; border-radius: 10px;
    display: flex; align-items: center; justify-content: center; flex-shrink: 0;
    i { font-size: 20px; }
  }
  .mc-title-wrap { flex: 1; }
  .mc-title { font-size: 15px; font-weight: 600; color: #303133; }
  .mc-desc { font-size: 12px; color: #909399; margin-top: 2px; }
  .mc-badge { flex-shrink: 0; }
}
.mc-features {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 6px;
  .mc-feature {
    display: flex;
    align-items: center;
    font-size: 12px;
    color: #606266;
    i { color: #52c41a; margin-right: 6px; font-size: 13px; flex-shrink: 0; }
  }
}
.mc-footer {
  margin-top: 10px;
  .mc-enter {
    font-size: 12px;
    color: #409eff;
    opacity: 0;
    transition: opacity 0.2s;
    i { margin-left: 4px; }
  }
}

/* ===== 底部区域 ===== */
.bottom-row { margin-bottom: 0; }

.fraud-entry-card {
  background: #fff;
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 2px 10px rgba(0,0,0,0.07);
  height: 100%;
  .fraud-header {
    display: flex;
    align-items: center;
    gap: 8px;
    font-size: 15px;
    font-weight: 600;
    color: #cf1322;
    margin-bottom: 16px;
    i { font-size: 18px; }
  }
  .fraud-types {
    display: flex;
    flex-direction: column;
    gap: 10px;
  }
  .fraud-type-item {
    display: flex;
    align-items: center;
    gap: 10px;
    padding: 10px 12px;
    background: #fafafa;
    border-radius: 8px;
    cursor: pointer;
    transition: background 0.2s;
    &:hover { background: #fff1f0; }
    .ft-index {
      width: 22px; height: 22px; border-radius: 50%;
      background: #ff4d4f; color: #fff;
      font-size: 12px; font-weight: 700;
      display: flex; align-items: center; justify-content: center;
      flex-shrink: 0;
    }
    .ft-name { font-size: 13px; font-weight: 600; color: #303133; min-width: 80px; }
    .ft-desc { font-size: 12px; color: #606266; flex: 1; }
  }
}

.warning-summary-card {
  background: #fff;
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 2px 10px rgba(0,0,0,0.07);
  height: 100%;
  .ws-header {
    display: flex;
    align-items: center;
    gap: 8px;
    font-size: 15px;
    font-weight: 600;
    color: #303133;
    margin-bottom: 14px;
    i { color: $accent; font-size: 18px; }
    .el-button { margin-left: auto; }
  }
  .ws-list {
    display: flex;
    flex-direction: column;
    gap: 8px;
  }
  .ws-item {
    display: flex;
    align-items: center;
    gap: 8px;
    padding: 8px 0;
    border-bottom: 1px solid #f0f0f0;
    &:last-child { border-bottom: none; }
    .ws-text {
      flex: 1;
      font-size: 12px;
      color: #606266;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
    }
    .ws-time { font-size: 11px; color: #c0c4cc; white-space: nowrap; }
  }
}
</style>
