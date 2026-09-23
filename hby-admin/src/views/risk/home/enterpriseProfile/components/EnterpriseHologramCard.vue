<template>
  <div class="enterprise-hologram-card">
    <div class="card-header">
      <i class="el-icon-view"></i>
      <h4>企业全息画像</h4>
    </div>
    
    <div class="card-content" v-loading="loading">
      <div v-if="hologramData" class="hologram-content">
        <!-- 企业画像标签云 -->
        <div class="tags-section">
          <div class="section-header">
            <i class="el-icon-collection-tag"></i>
            <span class="section-title">企业标签云</span>
          </div>
          <div class="word-cloud-container">
            <div
              v-for="(tag, index) in hologramData.enterpriseTags"
              :key="tag.tagId"
              :class="['cloud-tag', `tag-size-${getTagSize(index)}`, `tag-color-${getTagColor(tag.tagType)}`]"
              :style="getTagStyle(index)"
            >
              <i :class="getTagIcon(tag.tagType)"></i>
              {{ tag.tagName }}
            </div>
          </div>
        </div>

        <!-- 财务概况 -->
        <div class="financial-section" v-if="enterpriseDetailData">
          <div class="section-header">
            <i class="el-icon-coin"></i>
            <span class="section-title">财务概况</span>
          </div>
          <div class="financial-grid">
            <div class="financial-card">
              <div class="financial-icon">
                <i class="el-icon-wallet"></i>
              </div>
              <div class="financial-content">
                <div class="financial-value">{{ formatMoney(enterpriseDetailData.totalAssets) }}</div>
                <div class="financial-label">总资产</div>
              </div>
            </div>
            <div class="financial-card">
              <div class="financial-icon">
                <i class="el-icon-money"></i>
              </div>
              <div class="financial-content">
                <div class="financial-value">{{ formatMoney(enterpriseDetailData.totalRevenue) }}</div>
                <div class="financial-label">营业收入</div>
              </div>
            </div>
          </div>
        </div>

        <!-- 关键指标概览 -->
        <div class="indicators-section">
          <div class="section-header">
            <i class="el-icon-data-analysis"></i>
            <span class="section-title">关键指标</span>
          </div>
          <div class="indicators-grid">
            <div
              v-for="indicator in hologramData.keyIndicators"
              :key="indicator.code"
              class="indicator-card"
            >
              <div class="indicator-icon">
                <i :class="getIndicatorIcon(indicator.code)"></i>
              </div>
              <div class="indicator-content">
                <div class="indicator-value">{{ indicator.value }}</div>
                <div class="indicator-name">{{ indicator.name }}</div>
              </div>
            </div>
          </div>
        </div>

        <!-- 风险等级 -->
        <div class="risk-section">
          <div class="section-header">
            <i class="el-icon-warning-outline"></i>
            <span class="section-title">风险等级</span>
          </div>
          <div class="risk-level-card" :class="getRiskLevelClass(hologramData.riskLevel)">
            <div class="risk-icon">
              <i :class="getRiskIcon(hologramData.riskLevel)"></i>
            </div>
            <div class="risk-content">
              <div class="risk-text">{{ getRiskLevelText(hologramData.riskLevel) }}</div>
              <div class="risk-score">风险评分: {{ hologramData.riskScore || '--' }}</div>
            </div>
          </div>
        </div>
      </div>
      
      <div v-else class="no-data">
        <i class="el-icon-info"></i>
        <span>暂无全息画像数据</span>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'EnterpriseHologramCard',
  props: {
    hologramData: {
      type: Object,
      default: null
    },
    enterpriseDetailData: {
      type: Object,
      default: null
    },
    loading: {
      type: Boolean,
      default: false
    }
  },
  methods: {
    /**
     * 获取标签类型
     */
    getTagType(tagType) {
      const typeMap = {
        '1': 'success',   // 优势标签
        '2': 'warning',   // 关注标签
        '3': 'danger',    // 风险标签
        '4': 'info'       // 普通标签
      }
      return typeMap[tagType] || 'info'
    },

    /**
     * 获取风险等级样式类
     */
    getRiskLevelClass(riskLevel) {
      const classMap = {
        '1': 'risk-low',
        '2': 'risk-medium',
        '3': 'risk-high',
        '4': 'risk-critical'
      }
      return classMap[riskLevel] || 'risk-unknown'
    },

    /**
     * 获取风险等级图标
     */
    getRiskIcon(riskLevel) {
      const iconMap = {
        '1': 'el-icon-success',
        '2': 'el-icon-warning',
        '3': 'el-icon-error',
        '4': 'el-icon-close'
      }
      return iconMap[riskLevel] || 'el-icon-question'
    },

    /**
     * 获取风险等级文本
     */
    getRiskLevelText(riskLevel) {
      const textMap = {
        '1': '低风险',
        '2': '中风险',
        '3': '高风险',
        '4': '极高风险'
      }
      return textMap[riskLevel] || '未知'
    },

    /**
     * 获取标签图标
     */
    getTagIcon(tagType) {
      const iconMap = {
        '1': 'el-icon-star-on',     // 优势标签
        '2': 'el-icon-warning',     // 风险标签
        '3': 'el-icon-info',        // 中性标签
        '4': 'el-icon-question'     // 其他标签
      }
      return iconMap[tagType] || 'el-icon-price-tag'
    },

    /**
     * 获取指标图标
     */
    getIndicatorIcon(indicatorCode) {
      const iconMap = {
        'ROE': 'el-icon-trophy',
        'ROA': 'el-icon-coin',
        'PROFIT_MARGIN': 'el-icon-data-line',
        'DEBT_RATIO': 'el-icon-scale',
        'CURRENT_RATIO': 'el-icon-data-analysis'
      }
      return iconMap[indicatorCode] || 'el-icon-data-board'
    },

    /**
     * 格式化金额
     */
    formatMoney(value) {
      if (!value || value === 0) return '0元'
      const num = Number(value)
      if (num >= 100000000) {
        return (num / 100000000).toFixed(1) + '亿元'
      } else if (num >= 10000) {
        return (num / 10000).toFixed(1) + '万元'
      } else {
        return num.toFixed(1) + '元'
      }
    },

    /**
     * 获取标签大小
     */
    getTagSize(index) {
      const sizes = ['large', 'medium', 'small', 'medium', 'large', 'small']
      return sizes[index % sizes.length]
    },

    /**
     * 获取标签颜色
     */
    getTagColor(tagType) {
      const colorMap = {
        '1': 'success',   // 优势标签 - 绿色系
        '2': 'warning',   // 风险标签 - 橙色系
        '3': 'info',      // 中性标签 - 蓝色系
        '4': 'primary'    // 其他标签 - 紫色系
      }
      return colorMap[tagType] || 'primary'
    },

    /**
     * 获取标签样式
     */
    getTagStyle(index) {
      // 创建随机但固定的位置布局
      const positions = [
        { transform: 'rotate(-5deg)', zIndex: 3 },
        { transform: 'rotate(8deg)', zIndex: 2 },
        { transform: 'rotate(-12deg)', zIndex: 1 },
        { transform: 'rotate(3deg)', zIndex: 4 },
        { transform: 'rotate(-8deg)', zIndex: 2 },
        { transform: 'rotate(10deg)', zIndex: 3 },
        { transform: 'rotate(-3deg)', zIndex: 1 },
        { transform: 'rotate(6deg)', zIndex: 4 }
      ]
      return positions[index % positions.length]
    }
  }
}
</script>

<style lang="scss" scoped>
.enterprise-hologram-card {
  background: linear-gradient(135deg, #ffffff 0%, #f0f8ff 100%);
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  height: 100%;
  display: flex;
  flex-direction: column;
  border: 1px solid rgba(102, 126, 234, 0.1);
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
      color: #667eea;
    }

    h4 {
      margin: 0;
      font-size: 16px;
      font-weight: 600;
      color: #667eea;
      letter-spacing: 0.5px;
    }
  }

  .card-content {
    flex: 1;
    overflow: hidden;

    .hologram-content {
      .section-header {
        display: flex;
        align-items: center;
        gap: 8px;
        margin-bottom: 12px;

        i {
          font-size: 14px;
          color: #667eea;
        }

        .section-title {
          font-size: 13px;
          font-weight: 600;
          color: #333;
        }
      }

      .tags-section {
        margin-bottom: 18px;

        .word-cloud-container {
          position: relative;
          min-height: 120px;
          background: linear-gradient(135deg, #f8fafe 0%, #e8f4fd 100%);
          border-radius: 12px;
          padding: 16px;
          overflow: hidden;
          display: flex;
          flex-wrap: wrap;
          align-items: center;
          justify-content: center;
          gap: 8px;

          &::before {
            content: '';
            position: absolute;
            top: 0;
            left: 0;
            right: 0;
            bottom: 0;
            background: radial-gradient(circle at 30% 20%, rgba(102, 126, 234, 0.1) 0%, transparent 50%),
                        radial-gradient(circle at 70% 80%, rgba(118, 75, 162, 0.1) 0%, transparent 50%);
            pointer-events: none;
          }

          .cloud-tag {
            position: relative;
            display: inline-flex;
            align-items: center;
            gap: 4px;
            padding: 6px 12px;
            border-radius: 20px;
            font-weight: 600;
            cursor: pointer;
            transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
            box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
            backdrop-filter: blur(10px);

            i {
              font-size: 10px;
            }

            &:hover {
              transform: scale(1.1) rotate(0deg) !important;
              z-index: 10 !important;
              box-shadow: 0 8px 25px rgba(0, 0, 0, 0.2);
            }

            // 大小变化
            &.tag-size-large {
              font-size: 14px;
              padding: 8px 16px;
              font-weight: 700;
            }

            &.tag-size-medium {
              font-size: 12px;
              padding: 6px 12px;
              font-weight: 600;
            }

            &.tag-size-small {
              font-size: 10px;
              padding: 4px 8px;
              font-weight: 500;
            }

            // 颜色主题
            &.tag-color-success {
              background: linear-gradient(135deg, #67c23a 0%, #85ce61 100%);
              color: white;
              border: 2px solid rgba(255, 255, 255, 0.3);
            }

            &.tag-color-warning {
              background: linear-gradient(135deg, #e6a23c 0%, #f0a020 100%);
              color: white;
              border: 2px solid rgba(255, 255, 255, 0.3);
            }

            &.tag-color-info {
              background: linear-gradient(135deg, #409eff 0%, #66b1ff 100%);
              color: white;
              border: 2px solid rgba(255, 255, 255, 0.3);
            }

            &.tag-color-primary {
              background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
              color: white;
              border: 2px solid rgba(255, 255, 255, 0.3);
            }
          }
        }
      }

      .financial-section {
        margin-bottom: 18px;

        .financial-grid {
          display: grid;
          grid-template-columns: 1fr 1fr;
          gap: 10px;

          .financial-card {
            background: #f8fafe;
            border-radius: 8px;
            padding: 12px;
            display: flex;
            align-items: center;
            gap: 10px;
            border: 1px solid #e8f4fd;
            transition: all 0.3s ease;

            &:hover {
              background: #f0f8ff;
              transform: translateY(-2px);
              box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
            }

            .financial-icon {
              width: 28px;
              height: 28px;
              background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
              border-radius: 50%;
              display: flex;
              align-items: center;
              justify-content: center;
              color: white;
              font-size: 12px;
              flex-shrink: 0;
            }

            .financial-content {
              flex: 1;
              text-align: center;

              .financial-value {
                font-size: 13px;
                font-weight: 700;
                color: #667eea;
                margin-bottom: 2px;
                line-height: 1.2;
              }

              .financial-label {
                font-size: 10px;
                color: #666;
                line-height: 1.2;
              }
            }
          }
        }
      }

      .indicators-section {
        margin-bottom: 18px;

        .indicators-grid {
          display: grid;
          grid-template-columns: 1fr 1fr;
          gap: 10px;

          .indicator-card {
            background: #f8fafe;
            border-radius: 8px;
            padding: 12px;
            display: flex;
            align-items: center;
            gap: 10px;
            border: 1px solid #e8f4fd;
            transition: all 0.3s ease;

            &:hover {
              background: #f0f8ff;
              transform: translateY(-2px);
              box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
            }

            .indicator-icon {
              width: 28px;
              height: 28px;
              background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
              border-radius: 50%;
              display: flex;
              align-items: center;
              justify-content: center;
              color: white;
              font-size: 12px;
              flex-shrink: 0;
            }

            .indicator-content {
              flex: 1;
              min-width: 0;

              .indicator-value {
                font-size: 14px;
                font-weight: 700;
                color: #667eea;
                margin-bottom: 2px;
                line-height: 1.2;
              }

              .indicator-name {
                font-size: 10px;
                color: #666;
                line-height: 1.2;
              }
            }
          }
        }
      }

      .risk-section {
        .risk-level-card {
          background: #f8fafe;
          border-radius: 10px;
          padding: 14px;
          display: flex;
          align-items: center;
          gap: 12px;
          border: 1px solid #e8f4fd;
          transition: all 0.3s ease;

          &:hover {
            transform: translateY(-2px);
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
          }

          &.risk-low {
            background: linear-gradient(135deg, #f0f9ff 0%, #e6f7ff 100%);
            border-color: #b3e19d;

            .risk-icon {
              background: #67c23a;
            }
          }

          &.risk-medium {
            background: linear-gradient(135deg, #fdf6ec 0%, #fef3e2 100%);
            border-color: #f5dab1;

            .risk-icon {
              background: #e6a23c;
            }
          }

          &.risk-high {
            background: linear-gradient(135deg, #fef0f0 0%, #fde2e2 100%);
            border-color: #fbc4c4;

            .risk-icon {
              background: #f56c6c;
            }
          }

          &.risk-extreme {
            background: linear-gradient(135deg, #f5f5f5 0%, #eeeeee 100%);
            border-color: #d3d4d6;

            .risk-icon {
              background: #909399;
            }
          }

          .risk-icon {
            width: 36px;
            height: 36px;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            color: white;
            font-size: 16px;
            flex-shrink: 0;
          }

          .risk-content {
            flex: 1;

            .risk-text {
              font-size: 14px;
              font-weight: 600;
              color: #333;
              margin-bottom: 4px;
            }

            .risk-score {
              font-size: 11px;
              color: #666;
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
      padding: 20px;

      i {
        font-size: 16px;
      }
    }
  }
}
</style>
