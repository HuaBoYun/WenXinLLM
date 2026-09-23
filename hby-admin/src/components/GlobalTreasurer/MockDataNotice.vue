<template>
  <div v-if="show" class="mock-data-notice">
    <el-alert
      :title="title"
      :type="type"
      :description="description"
      show-icon
      :closable="closable"
      @close="handleClose"
      :style="alertStyle">
      <template slot="title">
        <i :class="iconClass"></i>
        {{ title }}
      </template>
    </el-alert>
  </div>
</template>

<script>
export default {
  name: 'MockDataNotice',
  props: {
    show: {
      type: Boolean,
      default: false
    },
    title: {
      type: String,
      default: '当前使用模拟数据'
    },
    description: {
      type: String,
      default: '检测到网络连接问题，当前显示的是模拟数据，请检查网络连接后刷新页面获取真实数据'
    },
    type: {
      type: String,
      default: 'warning',
      validator: value => ['success', 'info', 'warning', 'error'].includes(value)
    },
    closable: {
      type: Boolean,
      default: true
    },
    autoClose: {
      type: Boolean,
      default: false
    },
    autoCloseDelay: {
      type: Number,
      default: 5000
    }
  },
  computed: {
    iconClass() {
      const iconMap = {
        success: 'el-icon-success',
        info: 'el-icon-info',
        warning: 'el-icon-warning',
        error: 'el-icon-error'
      }
      return iconMap[this.type] || 'el-icon-info'
    },
    alertStyle() {
      return {
        marginBottom: '20px',
        borderRadius: '6px',
        boxShadow: '0 2px 12px 0 rgba(0, 0, 0, 0.1)'
      }
    }
  },
  mounted() {
    if (this.autoClose && this.show) {
      setTimeout(() => {
        this.handleClose()
      }, this.autoCloseDelay)
    }
  },
  methods: {
    handleClose() {
      this.$emit('close')
    },
    refresh() {
      window.location.reload()
    }
  }
}
</script>

<style scoped>
.mock-data-notice {
  position: relative;
  z-index: 1000;
}

.mock-data-notice .el-alert {
  border-left: 4px solid #e6a23c;
}

.mock-data-notice .el-alert--warning {
  background-color: #fdf6ec;
  border-color: #f5dab1;
}

.mock-data-notice .el-alert__icon {
  font-size: 16px;
  margin-right: 8px;
}

.mock-data-notice .el-alert__title {
  font-weight: 600;
  font-size: 14px;
}

.mock-data-notice .el-alert__description {
  font-size: 13px;
  line-height: 1.5;
  margin-top: 5px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .mock-data-notice {
    margin: 0 10px;
  }
  
  .mock-data-notice .el-alert__title {
    font-size: 13px;
  }
  
  .mock-data-notice .el-alert__description {
    font-size: 12px;
  }
}

/* 动画效果 */
.mock-data-notice {
  animation: slideDown 0.3s ease-out;
}

@keyframes slideDown {
  from {
    opacity: 0;
    transform: translateY(-20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

/* 主题变体 */
.mock-data-notice .el-alert--error {
  background-color: #fef0f0;
  border-color: #fbc4c4;
  border-left-color: #f56c6c;
}

.mock-data-notice .el-alert--success {
  background-color: #f0f9ff;
  border-color: #b3d8ff;
  border-left-color: #67c23a;
}

.mock-data-notice .el-alert--info {
  background-color: #f4f4f5;
  border-color: #d3d4d6;
  border-left-color: #909399;
}
</style>
