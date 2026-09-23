<template>
  <div class="organization-panel">
    <div class="section-title">
      <i class="el-icon-s-custom"></i>
      组织架构 & 人员分析
    </div>
    
    <div class="panel-content" v-loading="loading">
      <!-- 开发中提示 -->
      <div class="development-notice">
        <el-alert
          title="功能开发中"
          type="success"
          description="组织架构与人员分析功能正在开发中"
          :closable="false"
          show-icon
        />
        
        <!-- 预览内容 -->
        <div class="preview-content">
          <div class="content-grid">
            <!-- 组织架构图表 -->
            <div class="org-structure chart-card">
              <div class="chart-header">
                <i class="el-icon-s-custom"></i>
                <h4>组织架构图</h4>
              </div>
              <div class="org-tree">
                <div class="org-node root">
                  <div class="node-content">
                    <div class="node-title">董事会</div>
                    <div class="node-count">3人</div>
                  </div>
                  <div class="children">
                    <div class="org-node">
                      <div class="node-content">
                        <div class="node-title">总经理办</div>
                        <div class="node-count">5人</div>
                      </div>
                      <div class="children">
                        <div class="org-node">
                          <div class="node-content">
                            <div class="node-title">财务部</div>
                            <div class="node-count">12人</div>
                          </div>
                        </div>
                        <div class="org-node">
                          <div class="node-content">
                            <div class="node-title">技术部</div>
                            <div class="node-count">25人</div>
                          </div>
                        </div>
                        <div class="org-node">
                          <div class="node-content">
                            <div class="node-title">市场部</div>
                            <div class="node-count">18人</div>
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>

            <!-- 人员统计图表 -->
            <div class="personnel-stats chart-card">
              <div class="chart-header">
                <i class="el-icon-user"></i>
                <h4>人员统计图</h4>
              </div>
              <div class="stats-grid">
                <div class="stat-item">
                  <div class="stat-value">63</div>
                  <div class="stat-label">总人数</div>
                </div>
                <div class="stat-item">
                  <div class="stat-value">4</div>
                  <div class="stat-label">组织层级</div>
                </div>
                <div class="stat-item">
                  <div class="stat-value">5</div>
                  <div class="stat-label">部门数量</div>
                </div>
                <div class="stat-item">
                  <div class="stat-value">85%</div>
                  <div class="stat-label">本科以上</div>
                </div>
              </div>

              <!-- 学历分布 -->
              <div class="education-distribution">
                <h5>学历分布</h5>
                <div class="education-bars">
                  <div class="education-item">
                    <span class="label">博士</span>
                    <div class="bar">
                      <div class="fill" style="width: 8%"></div>
                    </div>
                    <span class="value">5人</span>
                  </div>
                  <div class="education-item">
                    <span class="label">硕士</span>
                    <div class="bar">
                      <div class="fill" style="width: 32%"></div>
                    </div>
                    <span class="value">20人</span>
                  </div>
                  <div class="education-item">
                    <span class="label">本科</span>
                    <div class="bar">
                      <div class="fill" style="width: 45%"></div>
                    </div>
                    <span class="value">28人</span>
                  </div>
                  <div class="education-item">
                    <span class="label">专科</span>
                    <div class="bar">
                      <div class="fill" style="width: 15%"></div>
                    </div>
                    <span class="value">10人</span>
                  </div>
                </div>
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
  name: 'OrganizationPanel',
  props: {
    enterpriseId: {
      type: String,
      required: true
    },
    organizationData: {
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
.organization-panel {
  background: rgba(255, 255, 255, 0.95);
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
  height: 100%;
  display: flex;
  flex-direction: column;

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

    .development-notice {
      .preview-content {
        margin-top: 20px;

        .content-grid {
          display: grid;
          grid-template-columns: 1fr 1fr;
          gap: 30px;

          .chart-card {
            background: #ffffff;
            border-radius: 8px;
            padding: 20px;
            box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
            border: 1px solid #e9ecef;

            .chart-header {
              display: flex;
              align-items: center;
              gap: 8px;
              margin-bottom: 15px;
              padding-bottom: 10px;
              border-bottom: 1px solid #e9ecef;

              i {
                font-size: 16px;
                color: #1e3c72;
              }

              h4 {
                font-size: 14px;
                font-weight: bold;
                color: #333;
                margin: 0;
              }
            }
          }

          h4, h5 {
            font-size: 14px;
            color: #666;
            margin: 0 0 15px 0;
          }

          h5 {
            font-size: 13px;
            margin: 15px 0 10px 0;
          }

          .org-structure {
            .org-tree {
              .org-node {
                margin-bottom: 10px;

                .node-content {
                  background: #f8f9fa;
                  padding: 8px 12px;
                  border-radius: 6px;
                  border-left: 3px solid #677eea;
                  display: flex;
                  justify-content: space-between;
                  align-items: center;

                  .node-title {
                    font-size: 13px;
                    font-weight: bold;
                    color: #333;
                  }

                  .node-count {
                    font-size: 12px;
                    color: #666;
                  }
                }

                .children {
                  margin-left: 20px;
                  margin-top: 8px;
                  position: relative;

                  &::before {
                    content: '';
                    position: absolute;
                    left: -10px;
                    top: 0;
                    bottom: 0;
                    width: 1px;
                    background: #ddd;
                  }

                  .org-node {
                    position: relative;

                    &::before {
                      content: '';
                      position: absolute;
                      left: -10px;
                      top: 15px;
                      width: 10px;
                      height: 1px;
                      background: #ddd;
                    }
                  }
                }

                &.root .node-content {
                  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                  color: white;
                  border-left-color: transparent;

                  .node-title,
                  .node-count {
                    color: white;
                  }
                }
              }
            }
          }

          .personnel-stats {
            .stats-grid {
              display: grid;
              grid-template-columns: 1fr 1fr;
              gap: 10px;
              margin-bottom: 20px;

              .stat-item {
                background: #f8f9fa;
                padding: 15px;
                border-radius: 8px;
                text-align: center;

                .stat-value {
                  font-size: 20px;
                  font-weight: bold;
                  color: #1e3c72;
                  margin-bottom: 5px;
                }

                .stat-label {
                  font-size: 12px;
                  color: #666;
                }
              }
            }

            .education-distribution {
              .education-bars {
                .education-item {
                  display: flex;
                  align-items: center;
                  gap: 10px;
                  margin-bottom: 8px;

                  .label {
                    width: 40px;
                    font-size: 12px;
                    color: #666;
                  }

                  .bar {
                    flex: 1;
                    height: 8px;
                    background: #e9ecef;
                    border-radius: 4px;
                    overflow: hidden;

                    .fill {
                      height: 100%;
                      background: linear-gradient(90deg, #667eea, #764ba2);
                      transition: width 0.3s ease;
                    }
                  }

                  .value {
                    width: 30px;
                    font-size: 12px;
                    color: #666;
                    text-align: right;
                  }
                }
              }
            }
          }
        }
      }
    }
  }
}

@media (max-width: 1600px) {
  .organization-panel {
    .panel-content {
      .development-notice {
        .preview-content {
          .content-grid {
            grid-template-columns: 1fr;
            gap: 20px;
          }
        }
      }
    }
  }
}
</style>
