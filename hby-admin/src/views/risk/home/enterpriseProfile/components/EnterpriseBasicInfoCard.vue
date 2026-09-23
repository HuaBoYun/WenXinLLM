<template>
  <div class="enterprise-basic-info-card">
    <div class="card-header">
      <i class="el-icon-office-building"></i>
      <h4>企业基本信息</h4>
    </div>
    
    <div class="card-content" v-loading="loading">
      <div v-if="displayData" class="info-container">
        <!-- 企业名称突出显示 -->
        <div class="enterprise-name-section">
          <div class="enterprise-name">
            <i class="el-icon-office-building name-icon"></i>
            {{ displayData.enterpriseName || '--' }}
          </div>
          <div class="enterprise-status">
            <el-tag
              :type="getStatusTagType(displayData.enterpriseStatus)"
              size="mini"
              class="status-tag"
            >
              <i :class="getStatusIcon(displayData.enterpriseStatus)"></i>
              {{ displayData.enterpriseStatusName || displayData.status || '正常' }}
            </el-tag>
          </div>
        </div>

        <!-- 基本信息网格 -->
        <div class="info-grid">
          <div class="info-card">
            <div class="info-icon">
              <i class="el-icon-postcard"></i>
            </div>
            <div class="info-content">
              <div class="info-label">统一社会信用代码</div>
              <div class="info-value">{{ displayData.creditCode || displayData.unifiedSocialCreditCode || '--' }}</div>
            </div>
          </div>

          <div class="info-card">
            <div class="info-icon">
              <i class="el-icon-user"></i>
            </div>
            <div class="info-content">
              <div class="info-label">法定代表人</div>
              <div class="info-value">{{ displayData.legalRepresentative || '--' }}</div>
            </div>
          </div>

          <div class="info-card">
            <div class="info-icon">
              <i class="el-icon-coin"></i>
            </div>
            <div class="info-content">
              <div class="info-label">注册资本</div>
              <div class="info-value highlight">{{ formatNumber(displayData.registeredCapital) }}万元</div>
            </div>
          </div>

          <div class="info-card">
            <div class="info-icon">
              <i class="el-icon-date"></i>
            </div>
            <div class="info-content">
              <div class="info-label">成立日期</div>
              <div class="info-value">{{ formatDate(displayData.establishDate || displayData.establishmentDate) }}</div>
            </div>
          </div>
        </div>

        <!-- 经营信息 -->
        <div class="business-section" v-if="enterpriseDetailData">
          <div class="section-header">
            <i class="el-icon-suitcase"></i>
            <span class="section-title">经营信息</span>
          </div>
          <div class="business-cards">
            <div class="business-card">
              <div class="business-icon">
                <i class="el-icon-document"></i>
              </div>
              <div class="business-content">
                <div class="business-label">经营范围</div>
                <div class="business-value" :title="enterpriseDetailData.businessScope">
                  {{ truncateText(enterpriseDetailData.businessScope, 60) }}
                </div>
              </div>
            </div>
            <div class="business-card">
              <div class="business-icon">
                <i class="el-icon-menu"></i>
              </div>
              <div class="business-content">
                <div class="business-label">行业类型</div>
                <div class="business-value">{{ enterpriseDetailData.industryTypeName || '--' }}</div>
              </div>
            </div>
            <div class="business-card">
              <div class="business-icon">
                <i class="el-icon-data-board"></i>
              </div>
              <div class="business-content">
                <div class="business-label">企业规模</div>
                <div class="business-value">{{ enterpriseDetailData.enterpriseScaleName || '--' }}</div>
              </div>
            </div>
          </div>
        </div>

        <!-- 联系信息 -->
        <div class="contact-section" v-if="enterpriseDetailData">
          <div class="section-header">
            <i class="el-icon-phone"></i>
            <span class="section-title">联系信息</span>
          </div>
          <div class="contact-cards">
            <div class="contact-card">
              <div class="contact-icon">
                <i class="el-icon-location"></i>
              </div>
              <div class="contact-content">
                <div class="contact-label">注册地址</div>
                <div class="contact-value" :title="enterpriseDetailData.registeredAddress">
                  {{ truncateText(enterpriseDetailData.registeredAddress, 50) }}
                </div>
              </div>
            </div>
            <div class="contact-card">
              <div class="contact-icon">
                <i class="el-icon-phone-outline"></i>
              </div>
              <div class="contact-content">
                <div class="contact-label">联系电话</div>
                <div class="contact-value">{{ enterpriseDetailData.contactPhone || '--' }}</div>
              </div>
            </div>
            <div class="contact-card">
              <div class="contact-icon">
                <i class="el-icon-message"></i>
              </div>
              <div class="contact-content">
                <div class="contact-label">电子邮箱</div>
                <div class="contact-value">{{ enterpriseDetailData.email || '--' }}</div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div v-else class="no-data">
        <i class="el-icon-info"></i>
        <span>暂无企业基本信息</span>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'EnterpriseBasicInfoCard',
  props: {
    enterpriseData: {
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
  computed: {
    /**
     * 显示数据 - 数据来源于数据库接口
     */
    displayData() {
      if (!this.enterpriseData) return null

      return {
        enterpriseName: this.enterpriseData.enterpriseName || this.enterpriseData.name || this.enterpriseData.companyName,
        creditCode: this.enterpriseData.creditCode || this.enterpriseData.unifiedSocialCreditCode || this.enterpriseData.socialCreditCode,
        legalRepresentative: this.enterpriseData.legalRepresentative || this.enterpriseData.legalPerson || this.enterpriseData.representative,
        registeredCapital: this.enterpriseData.registeredCapital || this.enterpriseData.capital || (this.enterpriseDetailData && this.enterpriseDetailData.registeredCapital),
        establishDate: this.enterpriseData.establishDate || this.enterpriseData.establishmentDate || this.enterpriseData.foundDate,
        enterpriseStatus: this.enterpriseData.enterpriseStatus || this.enterpriseData.status,
        enterpriseStatusName: this.enterpriseData.enterpriseStatusName || this.enterpriseData.statusName
      }
    }
  },
  methods: {
    /**
     * 格式化数字
     */
    formatNumber(value) {
      if (value === null || value === undefined || value === '') return '--'
      const num = Number(value)
      if (isNaN(num) || num === 0) return '--'
      return num.toLocaleString()
    },

    /**
     * 格式化日期
     */
    formatDate(dateStr) {
      if (!dateStr) return '--'
      try {
        const date = new Date(dateStr)
        return date.toLocaleDateString('zh-CN')
      } catch (error) {
        return dateStr
      }
    },

    /**
     * 获取状态样式类
     */
    getStatusClass(status) {
      const statusMap = {
        '1': 'status-active',    // 正常
        '2': 'status-warning',   // 异常
        '3': 'status-danger'     // 注销
      }
      return statusMap[status] || 'status-normal'
    },

    /**
     * 获取状态标签类型
     */
    getStatusTagType(status) {
      switch (status) {
        case '1':
        case 'active':
        case '正常':
          return 'success'
        case '2':
        case 'warning':
        case '异常':
          return 'warning'
        case '3':
        case 'danger':
        case '注销':
          return 'danger'
        default:
          return 'info'
      }
    },

    /**
     * 获取状态图标
     */
    getStatusIcon(status) {
      switch (status) {
        case '1':
        case 'active':
        case '正常':
          return 'el-icon-success'
        case '2':
        case 'warning':
        case '异常':
          return 'el-icon-warning'
        case '3':
        case 'danger':
        case '注销':
          return 'el-icon-error'
        default:
          return 'el-icon-info'
      }
    },

    /**
     * 截断文本
     */
    truncateText(text, maxLength) {
      if (!text) return '--'
      if (text.length <= maxLength) return text
      return text.substring(0, maxLength) + '...'
    }
  }
}
</script>

<style lang="scss" scoped>
.enterprise-basic-info-card {
  background: linear-gradient(135deg, #ffffff 0%, #f8fafe 100%);
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  height: 100%;
  display: flex;
  flex-direction: column;
  border: 1px solid rgba(30, 60, 114, 0.1);
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
      color: #1e3c72;
    }

    h4 {
      margin: 0;
      font-size: 16px;
      font-weight: 600;
      color: #1e3c72;
      letter-spacing: 0.5px;
    }
  }

  .card-content {
    flex: 1;
    overflow: hidden;

    .info-container {
      .enterprise-name-section {
        margin-bottom: 20px;
        padding: 16px;
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        border-radius: 10px;
        color: white;

        .enterprise-name {
          font-size: 18px;
          font-weight: 600;
          margin-bottom: 8px;
          display: flex;
          align-items: center;
          gap: 8px;

          .name-icon {
            font-size: 20px;
          }
        }

        .enterprise-status {
          display: flex;
          justify-content: flex-end;

          .status-tag {
            background: rgba(255, 255, 255, 0.2);
            border: 1px solid rgba(255, 255, 255, 0.3);
            color: white;

            i {
              margin-right: 4px;
            }
          }
        }
      }

      .info-grid {
        display: grid;
        grid-template-columns: 1fr 1fr;
        gap: 12px;

        .info-card {
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

          .info-icon {
            width: 32px;
            height: 32px;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            color: white;
            font-size: 14px;
            flex-shrink: 0;
          }

          .info-content {
            flex: 1;
            min-width: 0;

            .info-label {
              font-size: 11px;
              color: #666;
              margin-bottom: 4px;
              line-height: 1.2;
            }

            .info-value {
              font-size: 13px;
              font-weight: 600;
              color: #333;
              word-break: break-all;
              line-height: 1.3;

              &.highlight {
                color: #667eea;
                font-weight: 700;
              }
            }
          }
        }
      }

      .business-section, .contact-section {
        margin-top: 20px;
        padding-top: 16px;
        border-top: 1px solid #e8f4fd;

        .section-header {
          display: flex;
          align-items: center;
          gap: 8px;
          margin-bottom: 12px;

          i {
            font-size: 14px;
            color: #1e3c72;
          }

          .section-title {
            font-size: 13px;
            font-weight: 600;
            color: #333;
          }
        }

        .business-cards, .contact-cards {
          display: flex;
          flex-direction: column;
          gap: 8px;

          .business-card, .contact-card {
            background: #f8fafe;
            border-radius: 6px;
            padding: 8px;
            display: flex;
            align-items: center;
            gap: 8px;
            border: 1px solid #e8f4fd;
            transition: all 0.3s ease;

            &:hover {
              background: #f0f8ff;
              transform: translateX(4px);
              box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
            }

            .business-icon, .contact-icon {
              width: 20px;
              height: 20px;
              background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
              border-radius: 50%;
              display: flex;
              align-items: center;
              justify-content: center;
              color: white;
              font-size: 9px;
              flex-shrink: 0;
            }

            .business-content, .contact-content {
              flex: 1;
              min-width: 0;

              .business-label, .contact-label {
                font-size: 9px;
                color: #666;
                margin-bottom: 2px;
              }

              .business-value, .contact-value {
                font-size: 10px;
                font-weight: 600;
                color: #333;
                word-break: break-all;
                line-height: 1.3;
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
