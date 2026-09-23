<template>
  <div class="enterprise-info-card">
    <div class="card-header">
      <i class="el-icon-document"></i>
      <h4>企业信息</h4>
    </div>
    
    <div class="card-content" v-loading="loading">
      <div v-if="displayData" class="info-content">
        <!-- 经营信息 -->
        <div class="info-section">
          <div class="section-header">
            <i class="el-icon-suitcase"></i>
            <span class="section-title">经营信息</span>
          </div>
          <div class="info-cards">
            <div class="info-card">
              <div class="info-icon">
                <i class="el-icon-document"></i>
              </div>
              <div class="info-content">
                <div class="info-label">经营范围</div>
                <div class="info-value" :title="displayData.businessScope">
                  {{ truncateText(displayData.businessScope, 45) }}
                </div>
              </div>
            </div>
            <div class="info-card">
              <div class="info-icon">
                <i class="el-icon-menu"></i>
              </div>
              <div class="info-content">
                <div class="info-label">行业类型</div>
                <div class="info-value">{{ displayData.industryTypeName || '--' }}</div>
              </div>
            </div>
            <div class="info-card">
              <div class="info-icon">
                <i class="el-icon-data-board"></i>
              </div>
              <div class="info-content">
                <div class="info-label">企业规模</div>
                <div class="info-value">{{ displayData.enterpriseScaleName || '--' }}</div>
              </div>
            </div>
          </div>
        </div>

        <!-- 联系信息 -->
        <div class="info-section">
          <div class="section-header">
            <i class="el-icon-phone"></i>
            <span class="section-title">联系信息</span>
          </div>
          <div class="contact-grid">
            <div class="contact-item">
              <div class="contact-icon">
                <i class="el-icon-location"></i>
              </div>
              <div class="contact-content">
                <div class="contact-label">注册地址</div>
                <div class="contact-value" :title="displayData.registeredAddress">
                  {{ truncateText(displayData.registeredAddress, 35) }}
                </div>
              </div>
            </div>
            <div class="contact-item">
              <div class="contact-icon">
                <i class="el-icon-phone-outline"></i>
              </div>
              <div class="contact-content">
                <div class="contact-label">联系电话</div>
                <div class="contact-value">{{ displayData.contactPhone || '--' }}</div>
              </div>
            </div>
            <div class="contact-item">
              <div class="contact-icon">
                <i class="el-icon-message"></i>
              </div>
              <div class="contact-content">
                <div class="contact-label">电子邮箱</div>
                <div class="contact-value">{{ displayData.email || '--' }}</div>
              </div>
            </div>
          </div>
        </div>

        <!-- 财务概况 -->
        <div class="info-section">
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
                <div class="financial-value">{{ formatMoney(displayData.totalAssets) }}</div>
                <div class="financial-label">总资产</div>
              </div>
            </div>
            <div class="financial-card">
              <div class="financial-icon">
                <i class="el-icon-money"></i>
              </div>
              <div class="financial-content">
                <div class="financial-value">{{ formatMoney(displayData.totalRevenue) }}</div>
                <div class="financial-label">营业收入</div>
              </div>
            </div>
          </div>
        </div>
      </div>
      
      <div v-else class="no-data">
        <i class="el-icon-info"></i>
        <span>暂无企业信息</span>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'EnterpriseInfoCard',
  props: {
    enterpriseInfo: {
      type: Object,
      default: null
    },
    loading: {
      type: Boolean,
      default: false
    }
  },
  computed: {
    /**
     * 显示数据 - 优先使用真实数据，否则使用模拟数据
     */
    displayData() {
      if (this.enterpriseInfo) {
        return this.enterpriseInfo
      }

      // 返回模拟数据
      return {
        businessScope: '软件开发、技术咨询、系统集成、数据处理、云计算服务、人工智能技术开发',
        industryTypeName: '软件和信息技术服务业',
        enterpriseScaleName: '中型企业',
        registeredAddress: '北京市海淀区中关村软件园二期21号楼',
        contactPhone: '010-12345678',
        email: 'contact@huaboyun.com',
        totalAssets: 452000000,
        totalRevenue: 156000000
      }
    }
  },
  methods: {
    /**
     * 截断文本
     */
    truncateText(text, maxLength) {
      if (!text) return '--'
      if (text.length <= maxLength) return text
      return text.substring(0, maxLength) + '...'
    },

    /**
     * 格式化金额
     */
    formatMoney(value) {
      if (!value || value === 0) return '0万元'
      
      const num = Number(value)
      if (num >= 100000000) {
        return (num / 100000000).toFixed(1) + '亿元'
      } else if (num >= 10000) {
        return (num / 10000).toFixed(1) + '万元'
      } else {
        return num.toFixed(1) + '元'
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.enterprise-info-card {
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
    }
  }

  .card-content {
    flex: 1;
    overflow-y: auto;

    .info-content {
      .info-section {
        margin-bottom: 18px;

        .section-header {
          display: flex;
          align-items: center;
          gap: 8px;
          margin-bottom: 12px;

          i {
            font-size: 14px;
            color: #3498db;
          }

          .section-title {
            font-size: 13px;
            font-weight: 600;
            color: #333;
          }
        }

        .info-cards {
          display: flex;
          flex-direction: column;
          gap: 8px;

          .info-card {
            background: #f8fafe;
            border-radius: 8px;
            padding: 10px;
            display: flex;
            align-items: center;
            gap: 10px;
            border: 1px solid #e8f4fd;
            transition: all 0.3s ease;

            &:hover {
              background: #f0f8ff;
              transform: translateX(4px);
              box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
            }

            .info-icon {
              width: 24px;
              height: 24px;
              background: linear-gradient(135deg, #3498db 0%, #2980b9 100%);
              border-radius: 50%;
              display: flex;
              align-items: center;
              justify-content: center;
              color: white;
              font-size: 11px;
              flex-shrink: 0;
            }

            .info-content {
              flex: 1;
              min-width: 0;

              .info-label {
                font-size: 10px;
                color: #666;
                margin-bottom: 2px;
              }

              .info-value {
                font-size: 11px;
                font-weight: 600;
                color: #333;
                word-break: break-all;
                line-height: 1.3;
              }
            }
          }
        }

        .contact-grid {
          display: flex;
          flex-direction: column;
          gap: 8px;

          .contact-item {
            background: #f8fafe;
            border-radius: 8px;
            padding: 10px;
            display: flex;
            align-items: center;
            gap: 10px;
            border: 1px solid #e8f4fd;
            transition: all 0.3s ease;

            &:hover {
              background: #f0f8ff;
              transform: translateX(4px);
              box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
            }

            .contact-icon {
              width: 24px;
              height: 24px;
              background: linear-gradient(135deg, #3498db 0%, #2980b9 100%);
              border-radius: 50%;
              display: flex;
              align-items: center;
              justify-content: center;
              color: white;
              font-size: 11px;
              flex-shrink: 0;
            }

            .contact-content {
              flex: 1;
              min-width: 0;

              .contact-label {
                font-size: 10px;
                color: #666;
                margin-bottom: 2px;
              }

              .contact-value {
                font-size: 11px;
                font-weight: 600;
                color: #333;
                word-break: break-all;
                line-height: 1.3;
              }
            }
          }
        }

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
              background: linear-gradient(135deg, #3498db 0%, #2980b9 100%);
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
                color: #3498db;
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
