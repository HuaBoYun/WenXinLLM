<template>
  <div class="statistics-card">
    <div class="card-icon" :style="{ backgroundColor: iconBgColor }">
      <i :class="icon" :style="{ color: iconColor }"></i>
    </div>
    <div class="card-content">
      <div class="card-title">{{ title }}</div>
      <div class="card-value">
        <span v-if="prefix" class="prefix">{{ prefix }}</span>
        <span class="value">{{ formattedValue }}</span>
        <span v-if="suffix" class="suffix">{{ suffix }}</span>
      </div>
      <div v-if="trend" class="card-trend" :class="trendClass">
        <i :class="trendIcon"></i>
        <span>{{ trendText }}</span>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'StatisticsCard',
  props: {
    title: {
      type: String,
      required: true,
    },
    value: {
      type: [Number, String],
      required: true,
    },
    prefix: {
      type: String,
      default: '',
    },
    suffix: {
      type: String,
      default: '',
    },
    icon: {
      type: String,
      default: 'el-icon-data-line',
    },
    iconColor: {
      type: String,
      default: '#409EFF',
    },
    iconBgColor: {
      type: String,
      default: '#ECF5FF',
    },
    trend: {
      type: Object,
      default: null,
      // trend: { type: 'up' | 'down', value: '12.5%' }
    },
    decimals: {
      type: Number,
      default: 2,
    },
  },
  computed: {
    formattedValue() {
      if (typeof this.value === 'number') {
        return this.value.toLocaleString('zh-CN', {
          minimumFractionDigits: this.decimals,
          maximumFractionDigits: this.decimals,
        })
      }
      return this.value
    },
    trendClass() {
      if (!this.trend) return ''
      return this.trend.type === 'up' ? 'trend-up' : 'trend-down'
    },
    trendIcon() {
      if (!this.trend) return ''
      return this.trend.type === 'up' ? 'el-icon-top' : 'el-icon-bottom'
    },
    trendText() {
      if (!this.trend) return ''
      return this.trend.value
    },
  },
}
</script>

<style scoped>
.statistics-card {
  display: flex;
  align-items: center;
  padding: 20px;
  background: #ffffff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  transition: all 0.3s;
  height: 100%;
}

.statistics-card:hover {
  box-shadow: 0 4px 16px 0 rgba(0, 0, 0, 0.15);
  transform: translateY(-2px);
}

.card-icon {
  width: 56px;
  height: 56px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 16px;
  flex-shrink: 0;
}

.card-icon i {
  font-size: 28px;
}

.card-content {
  flex: 1;
  min-width: 0;
}

.card-title {
  font-size: 14px;
  color: #909399;
  margin-bottom: 8px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.card-value {
  font-size: 24px;
  font-weight: bold;
  color: #303133;
  margin-bottom: 4px;
  display: flex;
  align-items: baseline;
}

.card-value .prefix {
  font-size: 16px;
  margin-right: 4px;
  color: #606266;
}

.card-value .suffix {
  font-size: 14px;
  margin-left: 4px;
  color: #606266;
}

.card-trend {
  font-size: 12px;
  display: flex;
  align-items: center;
}

.card-trend i {
  margin-right: 4px;
}

.trend-up {
  color: #67c23a;
}

.trend-down {
  color: #f56c6c;
}
</style>

