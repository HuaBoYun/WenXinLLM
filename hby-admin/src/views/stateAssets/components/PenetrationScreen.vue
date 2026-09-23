<template>
  <div class="penetration-fullscreen">
    <!-- 头部 -->
    <div class="fs-header">
      <div class="fs-header-left">
        <div class="fs-time">{{ currentTime }}</div>
        <el-radio-group v-model="viewMode" size="mini" @change="$emit('view-change', viewMode)" class="fs-toggle" style="flex-shrink:0">
          <el-radio-button label="group">集团</el-radio-button>
          <el-radio-button label="company">单公司</el-radio-button>
        </el-radio-group>
        <el-select v-if="viewMode==='company'" v-model="selectedCompany" size="mini"
          placeholder="选择公司" style="width:150px;margin-left:8px;flex-shrink:0"
          @change="$emit('company-change', selectedCompany)">
          <el-option v-for="c in companyList" :key="c.value || c" :label="c.label || c" :value="c.value || c" />
        </el-select>
      </div>
      <div class="fs-header-title">
        <i :class="titleIcon" class="fs-title-icon"></i>
        <span>{{ title }}</span>
      </div>
      <div class="fs-header-right">
        <el-button size="mini" class="fs-close-btn" icon="el-icon-close" @click="$emit('close')">关闭大屏</el-button>
      </div>
    </div>

    <!-- KPI 行 -->
    <div class="fs-kpi-row">
      <div class="fs-kpi-item" v-for="(card, idx) in kpiCards" :key="idx"
        :class="'kpi-color-' + (idx % 6 + 1)"
        @click="$emit('kpi-click', card)">
        <div class="fs-kpi-icon"><i :class="card.icon"></i></div>
        <div class="fs-kpi-body">
          <div class="fs-kpi-value">{{ card.value }}</div>
          <div class="fs-kpi-label">{{ card.label }}</div>
        </div>
      </div>
    </div>

    <!-- 图表区（插槽） -->
    <div class="fs-charts">
      <slot :viewMode="viewMode" :company="selectedCompany" />
    </div>

    <!-- 页面导航 -->
    <div class="fs-pages">
      <div
        v-for="page in pages" :key="page.route"
        class="fs-page-item"
        @click="$emit('goto', page.route)"
      >
        <i :class="page.icon || 'el-icon-document'"></i>
        <span>{{ page.label }}</span>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'PenetrationScreen',
  props: {
    title:       { type: String, default: '穿透大屏' },
    titleIcon:   { type: String, default: 'el-icon-data-analysis' },
    accentColor: { type: String, default: '#409EFF' },
    kpiCards:    { type: Array, default: () => [] },
    pages:       { type: Array, default: () => [] },
    companyList: { type: Array, default: () => [] },
  },
  data() {
    return {
      viewMode: 'group',
      selectedCompany: '',
      currentTime: '',
      timer: null,
    }
  },
  mounted() {
    this.updateTime()
    this.timer = setInterval(this.updateTime, 1000)
  },
  beforeDestroy() {
    if (this.timer) clearInterval(this.timer)
  },
  methods: {
    updateTime() {
      const now = new Date()
      this.currentTime = now.toLocaleDateString('zh-CN') + ' ' + now.toLocaleTimeString('zh-CN')
    },
  },
}
</script>


<style lang="scss" scoped>
.penetration-fullscreen {
  width: 100vw; height: 100vh;
  background: linear-gradient(180deg, #050d1a 0%, #0a1628 100%);
  color: #fff; display: flex; flex-direction: column; overflow: hidden; position: relative;
  &::before { content: ''; position: absolute; inset: 0; pointer-events: none;
    background: radial-gradient(circle at 20% 50%, rgba(64,158,255,0.08) 0%, transparent 50%),
                radial-gradient(circle at 80% 80%, rgba(103,194,58,0.06) 0%, transparent 50%); }
}
.fs-header {
  height: 64px; display: flex; align-items: center; justify-content: space-between;
  padding: 0 32px; flex-shrink: 0; position: relative; z-index: 10;
  background: rgba(5,13,26,0.9); border-bottom: 1px solid rgba(64,158,255,0.25);
  .fs-header-left { display: flex; align-items: center; flex: 1; min-width: 0; flex-wrap: nowrap; overflow: hidden; }
  .fs-time { font-size: 13px; color: #409EFF; font-family: monospace; margin-right: 12px; white-space: nowrap; flex-shrink: 0; }
  ::v-deep .fs-toggle .el-radio-button__inner { background: rgba(64,158,255,0.1); border-color: rgba(64,158,255,0.3); color: #aac8ff; }
  ::v-deep .fs-toggle .el-radio-button__orig-radio:checked + .el-radio-button__inner { background: #409EFF; border-color: #409EFF; color: #fff; }
  .fs-header-title { flex: 2; text-align: center; font-size: 26px; font-weight: 700; letter-spacing: 3px;
    background: linear-gradient(90deg, #409EFF 0%, #67C23A 100%);
    -webkit-background-clip: text; -webkit-text-fill-color: transparent; background-clip: text; }
  .fs-title-icon { margin-right: 10px; font-size: 28px; }
  .fs-header-right { flex: 1; display: flex; justify-content: flex-end; }
  .fs-close-btn { background: rgba(245,108,108,0.15); border-color: #f56c6c; color: #f56c6c;
    &:hover { background: #f56c6c; color: #fff; } }
}
.fs-kpi-row {
  display: flex; gap: 12px; padding: 12px 24px; flex-shrink: 0;
  .fs-kpi-item {
    flex: 1; display: flex; align-items: center; padding: 10px 14px; border-radius: 6px; cursor: pointer;
    background: rgba(255,255,255,0.04); border: 1px solid rgba(255,255,255,0.08); transition: all 0.25s;
    &:hover { transform: translateY(-2px); border-color: rgba(64,158,255,0.4); background: rgba(64,158,255,0.08); }
    .fs-kpi-icon { width: 36px; height: 36px; border-radius: 8px; display: flex; align-items: center; justify-content: center; margin-right: 10px; flex-shrink: 0;
      i { font-size: 18px; color: #fff; } }
    &.kpi-color-1 .fs-kpi-icon { background: linear-gradient(135deg,#1677FF,#40a9ff); }
    &.kpi-color-2 .fs-kpi-icon { background: linear-gradient(135deg,#FA8C16,#FAAD14); }
    &.kpi-color-3 .fs-kpi-icon { background: linear-gradient(135deg,#52C41A,#73D13D); }
    &.kpi-color-4 .fs-kpi-icon { background: linear-gradient(135deg,#F5222D,#FF4D4F); }
    &.kpi-color-5 .fs-kpi-icon { background: linear-gradient(135deg,#722ED1,#9254DE); }
    &.kpi-color-6 .fs-kpi-icon { background: linear-gradient(135deg,#EB2F96,#F759AB); }
    .fs-kpi-value { font-size: 20px; font-weight: 700; color: #e8f4ff; line-height: 1.2; }
    .fs-kpi-label { font-size: 11px; color: rgba(255,255,255,0.5); margin-top: 2px; }
  }
}
.fs-charts {
  flex: 1; min-height: 0; padding: 0 24px 8px;
  display: flex; flex-direction: column;
  /* 给 slot 直接子元素强制撑满 */
  > * { flex: 1; min-height: 0; display: flex; flex-direction: column; }
}
.fs-pages {
  display: flex; flex-wrap: wrap; gap: 6px; padding: 8px 24px 12px; flex-shrink: 0;
  border-top: 1px solid rgba(64,158,255,0.15);
  .fs-page-item {
    display: flex; align-items: center; gap: 4px; padding: 4px 10px; border-radius: 4px; cursor: pointer;
    font-size: 12px; color: rgba(255,255,255,0.6); background: rgba(255,255,255,0.04);
    border: 1px solid rgba(255,255,255,0.08); transition: all 0.2s;
    &:hover { background: rgba(64,158,255,0.15); color: #409EFF; border-color: rgba(64,158,255,0.3); }
    i { font-size: 13px; }
  }
}
</style>
