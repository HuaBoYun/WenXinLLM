<template>
  <div class="model-stats-panel">
    <div class="panel-title">
      <span class="title-bar"></span>
      <span>模型统计</span>
      <span class="total-badge">{{ modelList.length }}</span>
    </div>

    <!-- 统计数字 -->
    <div class="stat-row">
      <div class="stat-box stat-box--blue">
        <div class="stat-num">{{ modelList.length }}</div>
        <div class="stat-lbl">模型总数</div>
      </div>
      <div class="stat-box stat-box--green">
        <div class="stat-num">{{ activeCount }}</div>
        <div class="stat-lbl">启用模型</div>
      </div>
      <div class="stat-box stat-box--orange">
        <div class="stat-num">{{ stats.totalCount || 0 }}</div>
        <div class="stat-lbl">预警总数</div>
      </div>
    </div>

    <!-- 模型列表（滚动） -->
    <div class="model-scroll-wrap">
      <div class="scroll-title">最新模型</div>
      <div class="model-scroll-list" ref="scrollList">
        <div
          v-for="(model, idx) in displayModels"
          :key="idx"
          class="model-scroll-item"
        >
          <span class="item-idx">{{ String(idx + 1).padStart(2, '0') }}</span>
          <span class="item-name" :title="model.combinationName || model.name">
            {{ model.combinationName || model.name || '未命名' }}
          </span>
          <span class="item-status" :class="isActive(model) ? 'status--active' : 'status--inactive'">
            {{ isActive(model) ? '启用' : '停用' }}
          </span>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'ModelStatsPanel',
  props: {
    modelList: { type: Array, default: () => [] },
    loading: { type: Boolean, default: false },
    stats: { type: Object, default: () => ({}) },
  },
  data() {
    return {
      scrollOffset: 0,
      _scrollTimer: null,
    }
  },
  computed: {
    activeCount() {
      return this.modelList.filter((m) => m.status === 'ACTIVE' || m.status === 1 || m.status === '1').length
    },
    displayModels() {
      return this.modelList.slice(0, 12)
    },
  },
  mounted() {
    this.startAutoScroll()
  },
  beforeDestroy() {
    if (this._scrollTimer) clearInterval(this._scrollTimer)
  },
  methods: {
    isActive(model) {
      return model.status === 'ACTIVE' || model.status === 1 || model.status === '1'
    },
    startAutoScroll() {
      this._scrollTimer = setInterval(() => {
        const el = this.$refs.scrollList
        if (!el) return
        el.scrollTop += 28
        if (el.scrollTop >= el.scrollHeight - el.clientHeight) {
          el.scrollTop = 0
        }
      }, 2500)
    },
  },
}
</script>

<style lang="scss" scoped>
.model-stats-panel {
  background: rgba(0, 20, 60, 0.7);
  border: 1px solid rgba(0, 200, 255, 0.2);
  border-radius: 6px;
  padding: 10px;
  display: flex;
  flex-direction: column;
  flex: 1;
  overflow: hidden;
  backdrop-filter: blur(4px);

  .panel-title {
    display: flex;
    align-items: center;
    gap: 8px;
    font-size: 13px;
    font-weight: 600;
    color: rgba(0, 200, 255, 0.9);
    margin-bottom: 10px;
    letter-spacing: 1px;
    flex-shrink: 0;

    .title-bar {
      display: inline-block;
      width: 3px;
      height: 14px;
      background: rgba(0, 200, 255, 0.8);
      border-radius: 2px;
    }

    .total-badge {
      margin-left: auto;
      padding: 1px 7px;
      border-radius: 8px;
      background: rgba(0, 200, 255, 0.15);
      border: 1px solid rgba(0, 200, 255, 0.3);
      font-size: 11px;
      color: rgba(0, 200, 255, 0.8);
    }
  }

  .stat-row {
    display: grid;
    grid-template-columns: repeat(3, 1fr);
    gap: 6px;
    margin-bottom: 10px;
    flex-shrink: 0;

    .stat-box {
      padding: 8px 6px;
      border-radius: 4px;
      text-align: center;
      border: 1px solid transparent;

      &--blue {
        background: rgba(0, 100, 200, 0.2);
        border-color: rgba(0, 100, 200, 0.3);
        .stat-num { color: rgba(100, 180, 255, 0.9); }
      }
      &--green {
        background: rgba(0, 180, 80, 0.15);
        border-color: rgba(0, 180, 80, 0.25);
        .stat-num { color: rgba(80, 220, 130, 0.9); }
      }
      &--orange {
        background: rgba(255, 140, 0, 0.15);
        border-color: rgba(255, 140, 0, 0.25);
        .stat-num { color: rgba(255, 180, 60, 0.9); }
      }

      .stat-num {
        font-size: 22px;
        font-weight: 700;
        line-height: 1;
        font-family: 'Courier New', monospace;
      }

      .stat-lbl {
        font-size: 9px;
        color: rgba(255, 255, 255, 0.4);
        margin-top: 2px;
        letter-spacing: 1px;
      }
    }
  }

  .model-scroll-wrap {
    flex: 1;
    display: flex;
    flex-direction: column;
    min-height: 0;
    overflow: hidden;

    .scroll-title {
      font-size: 10px;
      color: rgba(255, 255, 255, 0.3);
      letter-spacing: 1px;
      margin-bottom: 6px;
      flex-shrink: 0;
    }

    .model-scroll-list {
      flex: 1;
      overflow: hidden;
      display: flex;
      flex-direction: column;
      gap: 3px;
      transition: scroll-top 0.4s ease;

      .model-scroll-item {
        display: flex;
        align-items: center;
        gap: 6px;
        padding: 4px 6px;
        border-radius: 3px;
        background: rgba(255, 255, 255, 0.03);
        flex-shrink: 0;
        height: 25px;

        .item-idx {
          font-size: 10px;
          color: rgba(0, 200, 255, 0.4);
          font-family: monospace;
          flex-shrink: 0;
        }

        .item-name {
          flex: 1;
          font-size: 11px;
          color: rgba(255, 255, 255, 0.7);
          overflow: hidden;
          text-overflow: ellipsis;
          white-space: nowrap;
        }

        .item-status {
          font-size: 9px;
          padding: 1px 5px;
          border-radius: 6px;
          flex-shrink: 0;

          &--active {
            background: rgba(0, 180, 80, 0.2);
            color: rgba(80, 220, 130, 0.8);
            border: 1px solid rgba(0, 180, 80, 0.3);
          }
          &--inactive {
            background: rgba(100, 100, 100, 0.2);
            color: rgba(160, 160, 160, 0.6);
            border: 1px solid rgba(100, 100, 100, 0.2);
          }
        }
      }
    }
  }
}
</style>
