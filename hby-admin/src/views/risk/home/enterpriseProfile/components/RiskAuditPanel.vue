<template>
  <div class="risk-audit-container">
    <div class="risk-audit-panel">
      <div class="section-title">
      <i class="el-icon-warning"></i>
      风险预警 & 审计情况
      </div>

      <div class="panel-content" v-loading="loading">
        <!-- 主要内容区域 -->
        <div v-if="riskData && !riskData.message" class="main-content">
          <!-- 这里将来放置真实的风险审计数据 -->
        </div>

        <!-- 预览内容或开发中提示 -->
        <div class="preview-content">
          <!-- 风险等级分布 -->
          <div class="risk-distribution">
            <h4>风险等级分布（示例）</h4>
            <div class="risk-items">
              <div class="risk-item high">
                <div class="risk-level">高风险</div>
                <div class="risk-count">3</div>
              </div>
              <div class="risk-item medium">
                <div class="risk-level">中风险</div>
                <div class="risk-count">8</div>
              </div>
              <div class="risk-item low">
                <div class="risk-level">低风险</div>
                <div class="risk-count">15</div>
              </div>
            </div>
          </div>

          <!-- 审计情况统计 -->
          <div class="audit-statistics">
            <h4>审计情况统计（示例）</h4>
            <div class="audit-items">
              <div class="audit-item">
                <div class="audit-label">内部审计项目</div>
                <div class="audit-value">12</div>
              </div>
              <div class="audit-item">
                <div class="audit-label">发现问题</div>
                <div class="audit-value">25</div>
              </div>
              <div class="audit-item">
                <div class="audit-label">已整改</div>
                <div class="audit-value">20</div>
              </div>
              <div class="audit-item">
                <div class="audit-label">整改率</div>
                <div class="audit-value">80%</div>
              </div>
            </div>
          </div>

          <!-- 法律案件 -->
          <div class="legal-cases">
            <h4>法律案件（示例）</h4>
            <div class="case-list">
              <div class="case-item">
                <div class="case-title">合同纠纷案件</div>
                <div class="case-status pending">审理中</div>
              </div>
              <div class="case-item">
                <div class="case-title">知识产权案件</div>
                <div class="case-status completed">已结案</div>
              </div>
            </div>
          </div>


        </div>
      </div>
    </div>


  </div>
</template>

<script>
export default {
  name: 'RiskAuditPanel',
  props: {
    enterpriseId: {
      type: String,
      required: true
    },
    riskData: {
      type: Object,
      default: null
    },
    loading: {
      type: Boolean,
      default: false
    }
  }
}
</script>

<style lang="scss" scoped>
.risk-audit-container {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.risk-audit-panel {
  background: rgba(255, 255, 255, 0.95);
  border-radius: 12px;
  padding: 15px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
  height: 100%;
  display: flex;
  flex-direction: column;
  overflow: hidden;
  box-sizing: border-box;

  .section-title {
    display: flex;
    align-items: center;
    gap: 8px;
    font-size: 16px;
    font-weight: bold;
    color: #1e3c72;
    margin-bottom: 15px;

    i {
      font-size: 18px;
    }
  }

  .panel-content {
    flex: 1;
    overflow-y: auto;
    min-height: 0;

    .main-content {
      flex: 1;
      min-height: 0;
    }

    .preview-content {
      margin-top: 10px;

      h4 {
        font-size: 14px;
        color: #666;
        margin: 15px 0 10px 0;
      }

      .risk-distribution {
        .risk-items {
          display: flex;
          gap: 10px;
          margin-bottom: 20px;

          .risk-item {
            flex: 1;
            padding: 15px;
            border-radius: 8px;
            text-align: center;
            color: white;

            &.high {
              background: linear-gradient(135deg, #ff6b6b, #ee5a52);
            }

            &.medium {
              background: linear-gradient(135deg, #feca57, #ff9ff3);
            }

            &.low {
              background: linear-gradient(135deg, #48dbfb, #0abde3);
            }

            .risk-level {
              font-size: 12px;
              margin-bottom: 5px;
            }

            .risk-count {
              font-size: 20px;
              font-weight: bold;
            }
          }
        }
      }

      .audit-statistics {
        .audit-items {
          display: grid;
          grid-template-columns: 1fr 1fr;
          gap: 10px;
          margin-bottom: 20px;

          .audit-item {
            background: #f8f9fa;
            padding: 12px;
            border-radius: 6px;
            text-align: center;

            .audit-label {
              font-size: 12px;
              color: #666;
              margin-bottom: 5px;
            }

            .audit-value {
              font-size: 16px;
              font-weight: bold;
              color: #1e3c72;
            }
          }
        }
      }

      .legal-cases {
        .case-list {
          .case-item {
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding: 10px;
            background: #f8f9fa;
            border-radius: 6px;
            margin-bottom: 8px;

            .case-title {
              font-size: 13px;
              color: #333;
            }

            .case-status {
              font-size: 12px;
              padding: 2px 8px;
              border-radius: 12px;

              &.pending {
                background: #fff3cd;
                color: #856404;
              }

              &.completed {
                background: #d4edda;
                color: #155724;
              }
            }
          }
        }
      }

      // 新增：指标预警区域样式
      .indicator-warnings {
        margin-top: 20px;

        .warning-indicators {
          min-height: 120px;

          .warning-indicator-item {
            display: flex;
            align-items: center;
            padding: 8px 12px;
            background: #f8f9fa;
            border-radius: 6px;
            margin-bottom: 6px;
            cursor: pointer;
            transition: all 0.3s ease;
            border-left: 3px solid transparent;

            &:hover {
              background: #e9ecef;
              transform: translateX(2px);
              box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
            }

            .warning-light {
              margin-right: 10px;
              flex-shrink: 0;

              .light-dot {
                width: 12px;
                height: 12px;
                border-radius: 50%;
                box-shadow: 0 0 4px rgba(0, 0, 0, 0.3);

                &.light-green {
                  background: #52c41a;
                  box-shadow: 0 0 6px rgba(82, 196, 26, 0.6);
                }

                &.light-yellow {
                  background: #faad14;
                  box-shadow: 0 0 6px rgba(250, 173, 20, 0.6);
                }

                &.light-red {
                  background: #ff4d4f;
                  box-shadow: 0 0 6px rgba(255, 77, 79, 0.6);
                  animation: pulse-red 2s infinite;
                }

                &.light-gray {
                  background: #d9d9d9;
                }
              }
            }

            .indicator-name {
              flex: 1;
              font-size: 12px;
              color: #333;
              font-weight: 500;
              overflow: hidden;
              text-overflow: ellipsis;
              white-space: nowrap;
            }

            .warning-value {
              font-size: 11px;
              color: #666;
              font-weight: bold;
              margin-left: 8px;
              flex-shrink: 0;
            }

            // 根据预警级别设置边框颜色
            &:hover {
              &[data-warning-level="1"] {
                border-left-color: #52c41a;
              }
              &[data-warning-level="2"] {
                border-left-color: #faad14;
              }
              &[data-warning-level="3"] {
                border-left-color: #ff4d4f;
              }
            }
          }

          .no-data {
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
            padding: 30px 20px;
            color: #999;
            font-size: 12px;

            i {
              font-size: 24px;
              margin-bottom: 8px;
              color: #d9d9d9;
            }
          }
        }
      }
    }
  }
}

// 红色预警灯闪烁动画
@keyframes pulse-red {
  0% {
    box-shadow: 0 0 6px rgba(255, 77, 79, 0.6);
  }
  50% {
    box-shadow: 0 0 12px rgba(255, 77, 79, 0.9);
  }
  100% {
    box-shadow: 0 0 6px rgba(255, 77, 79, 0.6);
  }
}
</style>
