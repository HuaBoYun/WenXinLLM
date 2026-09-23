<template>
  <div class="key-financial-indicators-card">
    <div class="card-header">
      <i class="el-icon-data-board"></i>
      <h4>关键财务指标</h4>
      <div class="header-actions">
        <el-button size="mini" type="text" @click="refreshData">
          <i class="el-icon-refresh"></i>
        </el-button>
      </div>
    </div>
    
    <div class="card-content" v-loading="loading">
      <div v-if="displayIndicators && displayIndicators.length > 0" class="indicators-container">
        <div class="indicators-grid">
          <div
            v-for="indicator in displayIndicators"
            :key="indicator.indicatorCode"
            class="indicator-card"
            :class="getIndicatorClass(indicator)"
          >
            <div class="indicator-icon">
              <i :class="getIndicatorIcon(indicator.indicatorCode)"></i>
            </div>
            <div class="indicator-content">
              <div class="indicator-name">{{ indicator.indicatorName }}</div>
              <div class="indicator-value">
                {{ formatValue(indicator.currentValue) }}
                <span class="indicator-unit">{{ indicator.unit }}</span>
              </div>
              <div class="indicator-trend" v-if="indicator.trend">
                <i :class="getTrendIcon(indicator.trend)"></i>
                <span :class="getTrendClass(indicator.trend)">
                  {{ getTrendText(indicator.trend) }}
                </span>
              </div>
            </div>
            <div class="indicator-status">
              <div 
                class="status-dot" 
                :class="getStatusClass(indicator.warningLevel)"
                :title="getStatusText(indicator.warningLevel)"
              ></div>
            </div>
          </div>
        </div>
      </div>
      
      <div v-else class="no-data">
        <i class="el-icon-info"></i>
        <span>暂无财务指标数据</span>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'KeyFinancialIndicatorsCard',
  props: {
    indicatorsData: {
      type: Array,
      default: () => []
    },
    loading: {
      type: Boolean,
      default: false
    }
  },
  computed: {
    /**
     * 显示指标 - 优先使用真实数据，否则使用模拟数据
     */
    displayIndicators() {
      console.log('KeyFinancialIndicatorsCard - indicatorsData:', this.indicatorsData)
      if (this.indicatorsData && this.indicatorsData.length > 0) {
        console.log('使用真实财务指标数据:', this.indicatorsData.length, '个指标')
        return this.indicatorsData
      }

      console.log('使用模拟财务指标数据')
      // 返回模拟数据
      return [
        {
          indicatorCode: 'ROE',
          indicatorName: '净资产收益率',
          currentValue: 15.6,
          unit: '%',
          changeRate: 2.3,
          changeDirection: 'up',
          warningLevel: 'normal',
          level: '良好'
        },
        {
          indicatorCode: 'ROA',
          indicatorName: '总资产报酬率',
          currentValue: 8.9,
          unit: '%',
          changeRate: -1.2,
          changeDirection: 'down',
          warningLevel: 'warning',
          level: '一般'
        },
        {
          indicatorCode: 'PROFIT_MARGIN',
          indicatorName: '销售利润率',
          currentValue: 12.3,
          unit: '%',
          changeRate: 0.8,
          changeDirection: 'up',
          warningLevel: 'normal',
          level: '良好'
        },
        {
          indicatorCode: 'CURRENT_RATIO',
          indicatorName: '流动比率',
          currentValue: 1.85,
          unit: '倍',
          changeRate: 0.15,
          changeDirection: 'up',
          warningLevel: 'normal',
          level: '良好'
        },
        {
          indicatorCode: 'DEBT_RATIO',
          indicatorName: '资产负债率',
          currentValue: 45.2,
          unit: '%',
          changeRate: -2.1,
          changeDirection: 'down',
          warningLevel: 'normal',
          level: '良好'
        },
        {
          indicatorCode: 'TURNOVER_RATIO',
          indicatorName: '总资产周转率',
          currentValue: 1.2,
          unit: '次',
          changeRate: 0.05,
          changeDirection: 'up',
          warningLevel: 'normal',
          level: '一般'
        }
      ]
    }
  },
  methods: {
    /**
     * 刷新数据
     */
    refreshData() {
      this.$emit('refresh')
    },

    /**
     * 获取指标图标
     */
    getIndicatorIcon(indicatorCode) {
      const iconMap = {
        'ROE': 'el-icon-trophy',
        'ROA': 'el-icon-coin',
        'NET_ASSET_RETURN_RATE': 'el-icon-trophy',
        'TOTAL_ASSET_RETURN_RATE': 'el-icon-coin',
        'SALES_PROFIT_MARGIN': 'el-icon-data-line',
        'ASSET_LIABILITY_RATIO': 'el-icon-scale',
        'CURRENT_RATIO': 'el-icon-data-analysis',
        'QUICK_RATIO': 'el-icon-timer',
        'TOTAL_ASSET_TURNOVER': 'el-icon-refresh',
        'RECEIVABLES_TURNOVER': 'el-icon-document',
        'INVENTORY_TURNOVER': 'el-icon-box',
        'SALES_GROWTH_RATE': 'el-icon-top',
        'PROFIT_GROWTH_RATE': 'el-icon-top-right',
        'TOTAL_ASSET_GROWTH_RATE': 'el-icon-trend-charts'
      }
      return iconMap[indicatorCode] || 'el-icon-data-board'
    },

    /**
     * 获取指标样式类
     */
    getIndicatorClass(indicator) {
      const classes = []
      
      // 根据指标类型添加类
      if (indicator.indicatorCode.includes('GROWTH') || indicator.indicatorCode.includes('RETURN')) {
        classes.push('indicator-growth')
      } else if (indicator.indicatorCode.includes('RATIO') || indicator.indicatorCode.includes('LIABILITY')) {
        classes.push('indicator-ratio')
      } else if (indicator.indicatorCode.includes('TURNOVER')) {
        classes.push('indicator-efficiency')
      }

      return classes
    },

    /**
     * 格式化数值
     */
    formatValue(value) {
      if (!value) return '0'
      const num = parseFloat(value)
      if (num >= 10000) {
        return (num / 10000).toFixed(1) + '万'
      }
      return num.toFixed(2)
    },

    /**
     * 获取趋势图标
     */
    getTrendIcon(trend) {
      if (trend > 0) return 'el-icon-top'
      if (trend < 0) return 'el-icon-bottom'
      return 'el-icon-minus'
    },

    /**
     * 获取趋势样式类
     */
    getTrendClass(trend) {
      if (trend > 0) return 'trend-up'
      if (trend < 0) return 'trend-down'
      return 'trend-stable'
    },

    /**
     * 获取趋势文本
     */
    getTrendText(trend) {
      if (trend > 0) return `+${trend.toFixed(1)}%`
      if (trend < 0) return `${trend.toFixed(1)}%`
      return '持平'
    },

    /**
     * 获取状态样式类
     */
    getStatusClass(warningLevel) {
      switch (warningLevel) {
        case 1: return 'status-normal'
        case 2: return 'status-warning'
        case 3: return 'status-danger'
        default: return 'status-unknown'
      }
    },

    /**
     * 获取状态文本
     */
    getStatusText(warningLevel) {
      switch (warningLevel) {
        case 1: return '正常'
        case 2: return '预警'
        case 3: return '风险'
        default: return '未知'
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.key-financial-indicators-card {
  background: linear-gradient(135deg, #ffffff 0%, #f8f9fa 100%);
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  height: 100%;
  display: flex;
  flex-direction: column;
  border: 1px solid rgba(52, 152, 219, 0.1);
  transition: all 0.3s ease;

  &:hover {
    box-shadow: 0 6px 30px rgba(0, 0, 0, 0.12);
    transform: translateY(-2px);
  }

  .card-header {
    display: flex;
    align-items: center;
    gap: 10px;
    margin-bottom: 16px;
    padding-bottom: 12px;
    border-bottom: 2px solid #e8f4fd;

    i {
      font-size: 18px;
      color: #3498db;
    }

    h4 {
      margin: 0;
      font-size: 16px;
      font-weight: 600;
      color: #3498db;
      letter-spacing: 0.5px;
      flex: 1;
    }

    .header-actions {
      .el-button {
        color: #666;
        
        &:hover {
          color: #3498db;
        }
      }
    }
  }

  .card-content {
    flex: 1;
    overflow: hidden;

    .indicators-container {
      height: 100%;
      display: flex;
      flex-direction: column;

      .indicators-grid {
        display: grid;
        grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
        gap: 12px;
        max-height: 590px; // 再增加一个指标的长度（总共增加140px）
        overflow-y: auto;
        padding-right: 4px;

        // 美化滚动条
        &::-webkit-scrollbar {
          width: 6px;
        }

        &::-webkit-scrollbar-track {
          background: #f1f1f1;
          border-radius: 3px;
        }

        &::-webkit-scrollbar-thumb {
          background: #c1c1c1;
          border-radius: 3px;

          &:hover {
            background: #a8a8a8;
          }
        }

        .indicator-card {
          background: #f8fafe;
          border-radius: 10px;
          padding: 16px;
          display: flex;
          align-items: center;
          gap: 12px;
          border: 1px solid #e8f4fd;
          transition: all 0.3s ease;
          position: relative;

          &:hover {
            background: #f0f8ff;
            transform: translateY(-2px);
            box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
          }

          &.indicator-growth {
            border-left: 4px solid #67c23a;
          }

          &.indicator-ratio {
            border-left: 4px solid #e6a23c;
          }

          &.indicator-efficiency {
            border-left: 4px solid #409eff;
          }

          .indicator-icon {
            width: 36px;
            height: 36px;
            background: linear-gradient(135deg, #3498db 0%, #2980b9 100%);
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            color: white;
            font-size: 16px;
            flex-shrink: 0;
          }

          .indicator-content {
            flex: 1;
            min-width: 0;

            .indicator-name {
              font-size: 12px;
              color: #666;
              margin-bottom: 4px;
              line-height: 1.2;
            }

            .indicator-value {
              font-size: 16px;
              font-weight: 700;
              color: #333;
              margin-bottom: 4px;
              line-height: 1.2;

              .indicator-unit {
                font-size: 11px;
                font-weight: 500;
                color: #999;
                margin-left: 2px;
              }
            }

            .indicator-trend {
              display: flex;
              align-items: center;
              gap: 4px;
              font-size: 10px;

              .trend-up {
                color: #67c23a;
              }

              .trend-down {
                color: #f56c6c;
              }

              .trend-stable {
                color: #909399;
              }
            }
          }

          .indicator-status {
            .status-dot {
              width: 8px;
              height: 8px;
              border-radius: 50%;

              &.status-normal {
                background: #67c23a;
              }

              &.status-warning {
                background: #e6a23c;
              }

              &.status-danger {
                background: #f56c6c;
              }

              &.status-unknown {
                background: #909399;
              }
            }
          }
        }
      }
    }

    .no-data {
      display: flex;
      align-items: center;
      justify-content: center;
      gap: 8px;
      color: #999;
      font-size: 12px;
      padding: 40px 20px;

      i {
        font-size: 16px;
      }
    }
  }
}
</style>
