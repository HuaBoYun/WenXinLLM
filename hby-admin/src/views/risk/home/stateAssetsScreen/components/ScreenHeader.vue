<template>
  <div class="screen-header">
    <!-- 左侧：组织名称 + 返回按钮 -->
    <div class="header-left">
      <button class="back-btn" @click="$emit('go-back')">
        <span class="back-icon">&#9664;</span> 返回驾驶舱
      </button>
      <div class="org-badge" v-if="orgName">
        <span class="org-icon">&#127968;</span>
        <span>{{ orgName }}</span>
      </div>
    </div>

    <!-- 中央：主标题 -->
    <div class="header-center">
      <div class="title-decoration left">
        <span v-for="i in 3" :key="i" class="deco-line" :style="{ opacity: 0.3 + i * 0.2 }"></span>
      </div>
      <div class="title-main">
        <div class="title-en">STATE-OWNED ASSETS PENETRATION SUPERVISION</div>
        <div class="title-zh">国有资产穿透监管平台</div>
        <div class="title-sub">综合穿透监管 · 智能风险预警 · 全域数字化治理</div>
      </div>
      <div class="title-decoration right">
        <span v-for="i in 3" :key="i" class="deco-line" :style="{ opacity: 0.3 + i * 0.2 }"></span>
      </div>
    </div>

    <!-- 右侧：时间 -->
    <div class="header-right">
      <div class="time-display">
        <div class="time-clock">{{ currentTime }}</div>
        <div class="time-date">{{ currentDate }}</div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'ScreenHeader',
  props: {
    orgName: {
      type: String,
      default: '',
    },
  },
  data() {
    return {
      currentTime: '',
      currentDate: '',
      _timer: null,
    }
  },
  mounted() {
    this.updateTime()
    this._timer = setInterval(this.updateTime, 1000)
  },
  beforeDestroy() {
    if (this._timer) clearInterval(this._timer)
  },
  methods: {
    updateTime() {
      const now = new Date()
      const pad = (n) => String(n).padStart(2, '0')
      this.currentTime = `${pad(now.getHours())}:${pad(now.getMinutes())}:${pad(now.getSeconds())}`
      const weekDays = ['日', '一', '二', '三', '四', '五', '六']
      this.currentDate = `${now.getFullYear()}年${now.getMonth() + 1}月${now.getDate()}日 星期${weekDays[now.getDay()]}`
    },
  },
}
</script>

<style lang="scss" scoped>
.screen-header {
  height: 80px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
  background: linear-gradient(180deg, rgba(0, 30, 80, 0.95) 0%, rgba(3, 15, 45, 0.8) 100%);
  border-bottom: 1px solid rgba(0, 200, 255, 0.3);
  box-shadow: 0 2px 20px rgba(0, 200, 255, 0.1);
  flex-shrink: 0;

  .header-left {
    display: flex;
    align-items: center;
    gap: 12px;
    width: 260px;

    .back-btn {
      display: flex;
      align-items: center;
      gap: 4px;
      padding: 5px 12px;
      background: rgba(0, 200, 255, 0.1);
      border: 1px solid rgba(0, 200, 255, 0.3);
      border-radius: 4px;
      color: rgba(0, 200, 255, 0.9);
      font-size: 12px;
      cursor: pointer;
      transition: all 0.2s;
      white-space: nowrap;

      &:hover {
        background: rgba(0, 200, 255, 0.2);
        border-color: rgba(0, 200, 255, 0.6);
      }

      .back-icon {
        font-size: 10px;
      }
    }

    .org-badge {
      display: flex;
      align-items: center;
      gap: 4px;
      font-size: 12px;
      color: rgba(255, 213, 79, 0.9);
      padding: 3px 8px;
      border: 1px solid rgba(255, 213, 79, 0.3);
      border-radius: 4px;
      background: rgba(255, 213, 79, 0.05);
      max-width: 160px;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
    }
  }

  .header-center {
    flex: 1;
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 16px;

    .title-decoration {
      display: flex;
      flex-direction: column;
      gap: 4px;
      align-items: center;

      &.left { transform: scaleX(-1); }

      .deco-line {
        display: block;
        width: 40px;
        height: 2px;
        background: linear-gradient(90deg, transparent, rgba(0, 200, 255, 0.8));
        border-radius: 1px;
      }
    }

    .title-main {
      text-align: center;

      .title-en {
        font-size: 10px;
        letter-spacing: 3px;
        color: rgba(0, 200, 255, 0.5);
        margin-bottom: 2px;
      }

      .title-zh {
        font-size: 26px;
        font-weight: 700;
        background: linear-gradient(180deg, #ffffff 0%, rgba(0, 200, 255, 0.9) 100%);
        -webkit-background-clip: text;
        -webkit-text-fill-color: transparent;
        background-clip: text;
        letter-spacing: 4px;
        text-shadow: none;
        line-height: 1.2;
      }

      .title-sub {
        font-size: 11px;
        color: rgba(255, 255, 255, 0.4);
        letter-spacing: 2px;
        margin-top: 2px;
      }
    }
  }

  .header-right {
    width: 180px;
    display: flex;
    justify-content: flex-end;

    .time-display {
      text-align: right;

      .time-clock {
        font-size: 24px;
        font-weight: 700;
        color: rgba(0, 200, 255, 0.9);
        font-family: 'Courier New', monospace;
        letter-spacing: 2px;
      }

      .time-date {
        font-size: 11px;
        color: rgba(255, 255, 255, 0.5);
        margin-top: 2px;
      }
    }
  }
}
</style>
