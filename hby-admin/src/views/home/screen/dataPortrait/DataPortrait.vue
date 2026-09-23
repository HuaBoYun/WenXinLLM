<template>
  <div class="data-portrait-container">
    <!-- 页面头部 -->
    <div class="portrait-header">
      <div class="header-title">
        <i class="el-icon-data-analysis"></i>
        <span>数据画像总览</span>
      </div>
      <el-button
        type="primary"
        icon="el-icon-full-screen"
        @click="openFullScreen"
        class="fullscreen-btn"
      >
        全屏展示
      </el-button>
    </div>

    <!-- 数据分类展示 -->
    <div class="portrait-body">
      <!-- 社会侧数据 -->
      <div class="data-section social-data">
        <div class="section-header">
          <div class="header-line"></div>
          <div class="header-title-text">
            <i class="el-icon-share"></i>
            <span>社会侧数据</span>
          </div>
          <div class="header-line"></div>
        </div>
        <div class="section-content">
          <div class="data-card" @click="navigateTo('/ruleMonitor/Telescope')">
            <div class="card-icon social">
              <i class="el-icon-document-checked"></i>
            </div>
            <div class="card-content">
              <div class="card-title">司法数据</div>
              <div class="card-desc">裁判文书、法院公告、被执行人等司法风险数据</div>
              <div class="card-stats">
                <span class="stat-item">
                  <i class="el-icon-warning"></i>
                  实时监控
                </span>
              </div>
            </div>
            <div class="card-arrow">
              <i class="el-icon-arrow-right"></i>
            </div>
          </div>
        </div>
      </div>

      <!-- 内部数据 -->
      <div class="data-section internal-data">
        <div class="section-header">
          <div class="header-line"></div>
          <div class="header-title-text">
            <i class="el-icon-office-building"></i>
            <span>内部数据</span>
          </div>
          <div class="header-line"></div>
        </div>
        <div class="section-content">
          <div class="data-grid">
            <div class="data-card" @click="navigateTo('/ruleMonitor/TableQuerySK')">
              <div class="card-icon internal">
                <i class="el-icon-coin"></i>
              </div>
              <div class="card-content">
                <div class="card-title">司库数据</div>
                <div class="card-desc">资金管理、现金流、银行账户等司库信息</div>
                <div class="card-stats">
                  <span class="stat-item">
                    <i class="el-icon-data-line"></i>
                    数据分析
                  </span>
                </div>
              </div>
              <div class="card-arrow">
                <i class="el-icon-arrow-right"></i>
              </div>
            </div>

            <div class="data-card" @click="navigateTo('/ruleMonitor/TableQueryHT')">
              <div class="card-icon internal">
                <i class="el-icon-document"></i>
              </div>
              <div class="card-content">
                <div class="card-title">合同数据</div>
                <div class="card-desc">合同管理、合同履行、合同风险等信息</div>
                <div class="card-stats">
                  <span class="stat-item">
                    <i class="el-icon-files"></i>
                    智能管理
                  </span>
                </div>
              </div>
              <div class="card-arrow">
                <i class="el-icon-arrow-right"></i>
              </div>
            </div>

            <div class="data-card" @click="navigateTo('/ruleMonitor/TableQueryCW')">
              <div class="card-icon internal">
                <i class="el-icon-money"></i>
              </div>
              <div class="card-content">
                <div class="card-title">财务数据</div>
                <div class="card-desc">资产负债、损益表、现金流量等财务报表</div>
                <div class="card-stats">
                  <span class="stat-item">
                    <i class="el-icon-s-finance"></i>
                    财务分析
                  </span>
                </div>
              </div>
              <div class="card-arrow">
                <i class="el-icon-arrow-right"></i>
              </div>
            </div>

            <div class="data-card" @click="navigateTo('/ruleMonitor/TableQueryFP')">
              <div class="card-icon internal">
                <i class="el-icon-tickets"></i>
              </div>
              <div class="card-content">
                <div class="card-title">发票数据</div>
                <div class="card-desc">发票管理、发票验真、税务合规等信息</div>
                <div class="card-stats">
                  <span class="stat-item">
                    <i class="el-icon-check"></i>
                    合规检查
                  </span>
                </div>
              </div>
              <div class="card-arrow">
                <i class="el-icon-arrow-right"></i>
              </div>
            </div>

            <div class="data-card" @click="navigateTo('/ruleMonitor/TableQueryRY')">
              <div class="card-icon internal">
                <i class="el-icon-user"></i>
              </div>
              <div class="card-content">
                <div class="card-title">人员数据</div>
                <div class="card-desc">员工信息、组织架构、薪酬福利等人力资源数据</div>
                <div class="card-stats">
                  <span class="stat-item">
                    <i class="el-icon-s-custom"></i>
                    人力管理
                  </span>
                </div>
              </div>
              <div class="card-arrow">
                <i class="el-icon-arrow-right"></i>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 数据关联展示 -->
      <div class="data-relation">
        <div class="relation-title">
          <i class="el-icon-connection"></i>
          <span>数据关联与融合</span>
        </div>
        <div class="relation-content">
          <div class="relation-item">
            <div class="relation-icon">
              <i class="el-icon-link"></i>
            </div>
            <div class="relation-text">
              社会侧数据与内部数据实时关联，形成全方位数据画像
            </div>
          </div>
          <div class="relation-item">
            <div class="relation-icon">
              <i class="el-icon-data-analysis"></i>
            </div>
            <div class="relation-text">
              多维度数据分析，支持风险预警与决策支持
            </div>
          </div>
          <div class="relation-item">
            <div class="relation-icon">
              <i class="el-icon-monitor"></i>
            </div>
            <div class="relation-text">
              实时监控数据变化，确保数据准确性与时效性
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 大屏弹窗 -->
    <el-dialog
      :visible.sync="showFullScreen"
      fullscreen
      :show-close="false"
      custom-class="fullscreen-dialog"
      append-to-body
    >
      <data-screen v-if="showFullScreen" @close="closeFullScreen" />
    </el-dialog>
  </div>
</template>

<script>
import DataScreen from './DataScreen.vue'

export default {
  name: 'DataPortrait',
  components: {
    DataScreen
  },
  data() {
    return {
      showFullScreen: false,
      // 标志位：防止重复检测自动打开大屏
      hasCheckedAutoOpen: false
    }
  },
  mounted() {
    // 检查是否需要自动打开大屏
    this.checkAutoOpenScreen()
  },
  methods: {
    /**
     * 检查是否需要自动打开大屏
     * 当 URL 中包含 openScreen=true 参数时，自动打开大屏
     */
    checkAutoOpenScreen() {
      // 如果已经检测过，不再重复检测
      if (this.hasCheckedAutoOpen) {
        console.log('⏭️ 已经检测过自动打开大屏，跳过')
        return
      }

      const { openScreen } = this.$route.query
      console.log('🔍 检查自动打开大屏参数:', { openScreen, query: this.$route.query })

      // 标记已检测
      this.hasCheckedAutoOpen = true

      if (openScreen === 'true') {
        console.log('✅ 检测到 openScreen=true，准备打开大屏...')

        // 先清除 URL 参数，再打开大屏
        this.$router.replace({
          path: this.$route.path,
          query: {}
        }).then(() => {
          // 延迟一点时间，确保页面已经渲染完成
          this.$nextTick(() => {
            setTimeout(() => {
              console.log('🚀 正在打开大屏...')
              this.showFullScreen = true
            }, 300)
          })
        }).catch(err => {
          // 忽略导航重复错误
          if (err.name !== 'NavigationDuplicated') {
            console.error('路由跳转错误:', err)
          }
          // 即使路由跳转失败，也要打开大屏
          this.$nextTick(() => {
            setTimeout(() => {
              console.log('🚀 正在打开大屏...')
              this.showFullScreen = true
            }, 300)
          })
        })
      } else {
        console.log('❌ 未检测到 openScreen=true 参数，不自动打开大屏')
      }
    },
    openFullScreen() {
      this.showFullScreen = true
    },
    closeFullScreen() {
      this.showFullScreen = false
    },
    navigateTo(path, query = {}) {
      // 添加来源参数，让目标页面知道是从数据画像跳转过来的
      const finalQuery = {
        ...query,
        from: 'dataPortrait'
      }
      this.$router.push({
        path,
        query: finalQuery
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.data-portrait-container {
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 20px;

  .portrait-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 30px;
    padding: 20px 30px;
    background: rgba(255, 255, 255, 0.95);
    border-radius: 12px;
    box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);

    .header-title {
      display: flex;
      align-items: center;
      gap: 12px;
      font-size: 28px;
      font-weight: bold;
      color: #2c3e50;

      i {
        font-size: 32px;
        color: #667eea;
      }
    }

    .fullscreen-btn {
      padding: 12px 24px;
      font-size: 16px;
      border-radius: 8px;
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      border: none;
      box-shadow: 0 4px 15px rgba(102, 126, 234, 0.4);
      transition: all 0.3s;

      &:hover {
        transform: translateY(-2px);
        box-shadow: 0 6px 20px rgba(102, 126, 234, 0.6);
      }
    }
  }

  .portrait-body {
    display: flex;
    flex-direction: column;
    gap: 30px;

    .data-section {
      background: rgba(255, 255, 255, 0.95);
      border-radius: 12px;
      padding: 25px;
      box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);

      .section-header {
        display: flex;
        align-items: center;
        margin-bottom: 25px;
        gap: 15px;

        .header-line {
          flex: 1;
          height: 2px;
          background: linear-gradient(90deg, transparent, #667eea, transparent);
        }

        .header-title-text {
          display: flex;
          align-items: center;
          gap: 10px;
          font-size: 22px;
          font-weight: bold;
          color: #2c3e50;
          white-space: nowrap;

          i {
            font-size: 24px;
          }
        }
      }

      .section-content {
        .data-grid {
          display: grid;
          grid-template-columns: repeat(auto-fit, minmax(350px, 1fr));
          gap: 20px;
        }

        .data-card {
          display: flex;
          align-items: center;
          gap: 15px;
          padding: 20px;
          background: #fff;
          border: 2px solid #e8eaf6;
          border-radius: 10px;
          cursor: pointer;
          transition: all 0.3s;
          position: relative;
          overflow: hidden;

          &::before {
            content: '';
            position: absolute;
            top: 0;
            left: 0;
            width: 4px;
            height: 100%;
            background: linear-gradient(180deg, #667eea, #764ba2);
            transform: scaleY(0);
            transition: transform 0.3s;
          }

          &:hover {
            transform: translateY(-5px);
            box-shadow: 0 12px 24px rgba(102, 126, 234, 0.2);
            border-color: #667eea;

            &::before {
              transform: scaleY(1);
            }

            .card-arrow {
              transform: translateX(5px);
            }
          }

          .card-icon {
            width: 60px;
            height: 60px;
            display: flex;
            align-items: center;
            justify-content: center;
            border-radius: 12px;
            flex-shrink: 0;

            &.social {
              background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            }

            &.internal {
              background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
            }

            i {
              font-size: 28px;
              color: #fff;
            }
          }

          .card-content {
            flex: 1;

            .card-title {
              font-size: 18px;
              font-weight: bold;
              color: #2c3e50;
              margin-bottom: 8px;
            }

            .card-desc {
              font-size: 13px;
              color: #7f8c8d;
              line-height: 1.6;
              margin-bottom: 10px;
            }

            .card-stats {
              display: flex;
              gap: 15px;

              .stat-item {
                display: flex;
                align-items: center;
                gap: 5px;
                font-size: 12px;
                color: #667eea;
                font-weight: 500;

                i {
                  font-size: 14px;
                }
              }
            }
          }

          .card-arrow {
            font-size: 24px;
            color: #667eea;
            transition: transform 0.3s;
          }
        }
      }

      &.social-data {
        .section-header .header-title-text {
          color: #667eea;

          i {
            color: #667eea;
          }
        }
      }

      &.internal-data {
        .section-header .header-title-text {
          color: #f5576c;

          i {
            color: #f5576c;
          }
        }
      }
    }

    .data-relation {
      background: rgba(255, 255, 255, 0.95);
      border-radius: 12px;
      padding: 25px;
      box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);

      .relation-title {
        display: flex;
        align-items: center;
        gap: 10px;
        font-size: 20px;
        font-weight: bold;
        color: #2c3e50;
        margin-bottom: 20px;

        i {
          font-size: 24px;
          color: #667eea;
        }
      }

      .relation-content {
        display: grid;
        grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
        gap: 20px;

        .relation-item {
          display: flex;
          align-items: center;
          gap: 15px;
          padding: 20px;
          background: linear-gradient(135deg, rgba(102, 126, 234, 0.05) 0%, rgba(118, 75, 162, 0.05) 100%);
          border-radius: 10px;
          border: 1px solid rgba(102, 126, 234, 0.2);

          .relation-icon {
            width: 50px;
            height: 50px;
            display: flex;
            align-items: center;
            justify-content: center;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            border-radius: 50%;
            flex-shrink: 0;

            i {
              font-size: 24px;
              color: #fff;
            }
          }

          .relation-text {
            flex: 1;
            font-size: 14px;
            color: #2c3e50;
            line-height: 1.6;
          }
        }
      }
    }
  }
}

::v-deep .fullscreen-dialog {
  background: transparent;
  margin: 0;
  padding: 0;

  .el-dialog__header {
    display: none;
  }

  .el-dialog__body {
    padding: 0;
    height: 100vh;
  }
}
</style>

