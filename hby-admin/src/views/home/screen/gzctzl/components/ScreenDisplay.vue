<template>
  <div class="screen-display">
    <!-- 大屏头部 -->
    <div class="screen-header">
      <div class="header-left">
        <div class="current-time">{{ currentTime }}</div>
      </div>
      <div class="header-title">
        <div class="title-main">国资穿透总览</div>
      </div>
      <div class="header-right">
        <el-button
          type="danger"
          icon="el-icon-close"
          circle
          size="small"
          @click="handleClose"
          class="close-btn"
        ></el-button>
      </div>
    </div>

    <!-- 大屏主体内容 -->
    <div class="screen-body">
      <!-- 左侧：社会侧数据资源 -->
      <div class="left-sidebar">
        <div class="sidebar-title">
          <i class="el-icon-share"></i>
          <span>社会侧数据资源</span>
        </div>
        <div class="sidebar-items">
          <div class="sidebar-item" v-for="(item, index) in socialDataResources" :key="index">
            <div class="item-icon">
              <i :class="item.icon"></i>
            </div>
            <div class="item-content">
              <div class="item-name">{{ item.name }}</div>
              <div class="item-desc">{{ item.desc }}</div>
            </div>
          </div>
        </div>
      </div>

      <!-- 中间：核心理念展示区 -->
      <div class="center-content">
        <!-- 顶部：核心理念 -->
        <div class="core-concept">
          <div class="concept-title">核心理念-从流程驱动到数据驱动</div>
          <div class="concept-cards">
            <div class="concept-card">
              <div class="card-title">风险数据模型</div>
              <div class="card-content">
                <div class="card-desc">网络合法监管、实时数据监控</div>
              </div>
            </div>
            <div class="concept-card">
              <div class="card-title">数字化管控模型</div>
              <div class="card-content">
                <div class="card-desc">内外数据融合、实时穿透监控</div>
              </div>
            </div>
            <div class="concept-card">
              <div class="card-title">业务及技术手段</div>
              <div class="card-content">
                <div class="card-desc">非现场审计、系统化手段、数据化技术、数据化技术</div>
              </div>
            </div>
          </div>
        </div>

        <!-- 中间：核心展示区 -->
        <div class="main-display">
          <!-- 左侧：外部数据模型 -->
          <div class="display-left">
            <div class="model-box">
              <div class="model-title">外部数据模型(建设中)</div>
              <div class="model-content">
                <div ref="externalModelChart" class="model-chart"></div>
              </div>
            </div>
          </div>

          <!-- 中间：国资风险穿透指标 -->
          <div class="display-center">
            <div class="penetration-core">
              <div class="core-title">国资穿透</div>
              <div class="core-indicators">
                <div class="indicator-item">收入</div>
                <div class="indicator-item">成本</div>
                <div class="indicator-item">投入</div>
                <div class="indicator-item">收益</div>
              </div>
            </div>
          </div>

          <!-- 右侧：嵌入大数据 -->
          <div class="display-right">
            <div class="model-box">
              <div class="model-title">内部数据模型(进入大数据)</div>
              <div class="model-content">
                <div ref="internalModelChart" class="model-chart"></div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 右侧：企业侧数据资源 -->
      <div class="right-sidebar">
        <div class="sidebar-title">
          <i class="el-icon-office-building"></i>
          <span>企业侧数据资源</span>
        </div>
        <div class="sidebar-items">
          <div class="sidebar-item" v-for="(item, index) in enterpriseDataResources" :key="index">
            <div class="item-icon">
              <i :class="item.icon"></i>
            </div>
            <div class="item-content">
              <div class="item-name">{{ item.name }}</div>
              <div class="item-desc">{{ item.desc }}</div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 底部：三大目标 -->
    <div class="screen-footer">
      <div class="footer-title">三大目标</div>
      <div class="footer-content">
        <div class="goal-item">
          <div class="goal-icon">
            <i class="el-icon-warning-outline"></i>
          </div>
          <div class="goal-text">
            内外部数据整合全面监管关联关系，实现风险预警的<span class="highlight">"事前预防"</span>；
          </div>
        </div>
        <div class="goal-item">
          <div class="goal-icon">
            <i class="el-icon-data-analysis"></i>
          </div>
          <div class="goal-text">
            内外数据融合全面监管企业经营，按照<span class="highlight">"事中监督"</span>的原则进行管控；
          </div>
        </div>
        <div class="goal-item">
          <div class="goal-icon">
            <i class="el-icon-document-checked"></i>
          </div>
          <div class="goal-text">
            内外数据融合全面监管企业经营，关注<span class="highlight">"事后监督"</span>的原则进行管控。
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import * as echarts from 'echarts'

export default {
  name: 'ScreenDisplay',
  props: {
    penetrationData: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      currentTime: '',
      timeTimer: null,
      externalModelChart: null,
      internalModelChart: null,
      socialDataResources: [
        { name: '工商数据', desc: '网络、内容、营业、审计', icon: 'el-icon-office-building' },
        { name: '经营数据', desc: '收入、成本、利润、审计', icon: 'el-icon-data-line' },
        { name: '财务数据', desc: '资产、负债、现金流', icon: 'el-icon-coin' },
        { name: '征信数据', desc: '信用评级、违约记录', icon: 'el-icon-document' }
      ],
      enterpriseDataResources: [
        { name: '自动网络', desc: '自动化数据采集', icon: 'el-icon-connection' },
        { name: '发票网络', desc: '发票数据管理', icon: 'el-icon-tickets' },
        { name: '财务网络', desc: '财务数据分析', icon: 'el-icon-money' },
        { name: '综合网络', desc: '综合数据整合', icon: 'el-icon-s-grid' },
        { name: '人员网络', desc: '人员信息管理', icon: 'el-icon-user' },
        { name: '内部网络', desc: '内部数据流转', icon: 'el-icon-share' }
      ]
    }
  },
  mounted() {
    this.updateTime()
    this.timeTimer = setInterval(this.updateTime, 1000)
    this.$nextTick(() => {
      this.initExternalModelChart()
      this.initInternalModelChart()
    })
  },
  beforeDestroy() {
    if (this.timeTimer) {
      clearInterval(this.timeTimer)
    }
    if (this.externalModelChart) {
      this.externalModelChart.dispose()
    }
    if (this.internalModelChart) {
      this.internalModelChart.dispose()
    }
  },
  methods: {
    updateTime() {
      const now = new Date()
      const year = now.getFullYear()
      const month = String(now.getMonth() + 1).padStart(2, '0')
      const day = String(now.getDate()).padStart(2, '0')
      const hours = String(now.getHours()).padStart(2, '0')
      const minutes = String(now.getMinutes()).padStart(2, '0')
      const seconds = String(now.getSeconds()).padStart(2, '0')
      this.currentTime = `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`
    },
    handleClose() {
      this.$emit('close')
    },
    initExternalModelChart() {
      if (!this.$refs.externalModelChart) return
      
      if (this.externalModelChart) {
        this.externalModelChart.dispose()
      }
      
      this.externalModelChart = echarts.init(this.$refs.externalModelChart)
      
      const option = {
        tooltip: {
          trigger: 'item'
        },
        series: [
          {
            type: 'pie',
            radius: ['40%', '70%'],
            center: ['50%', '50%'],
            data: [
              { value: 335, name: '工商数据' },
              { value: 310, name: '经营数据' },
              { value: 234, name: '财务数据' },
              { value: 135, name: '征信数据' }
            ],
            label: {
              show: true,
              color: '#fff',
              fontSize: 12
            },
            itemStyle: {
              borderRadius: 8,
              borderColor: '#0a0e27',
              borderWidth: 2
            }
          }
        ],
        color: ['#409eff', '#67c23a', '#e6a23c', '#f56c6c']
      }
      
      this.externalModelChart.setOption(option)
    },
    initInternalModelChart() {
      if (!this.$refs.internalModelChart) return
      
      if (this.internalModelChart) {
        this.internalModelChart.dispose()
      }
      
      this.internalModelChart = echarts.init(this.$refs.internalModelChart)
      
      const option = {
        tooltip: {
          trigger: 'item'
        },
        series: [
          {
            type: 'pie',
            radius: ['40%', '70%'],
            center: ['50%', '50%'],
            data: [
              { value: 400, name: '自动网络' },
              { value: 350, name: '发票网络' },
              { value: 300, name: '财务网络' },
              { value: 250, name: '综合网络' },
              { value: 200, name: '人员网络' },
              { value: 150, name: '内部网络' }
            ],
            label: {
              show: true,
              color: '#fff',
              fontSize: 12
            },
            itemStyle: {
              borderRadius: 8,
              borderColor: '#0a0e27',
              borderWidth: 2
            }
          }
        ],
        color: ['#409eff', '#67c23a', '#e6a23c', '#f56c6c', '#909399', '#c45656']
      }
      
      this.internalModelChart.setOption(option)
    }
  }
}
</script>

<style lang="scss" scoped>
.screen-display {
  width: 100vw;
  height: 100vh;
  background: linear-gradient(180deg, #0a0e27 0%, #1a1f3a 100%);
  color: #fff;
  display: flex;
  flex-direction: column;
  position: relative;
  overflow: hidden;

  // 大屏头部
  .screen-header {
    height: 80px;
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 0 40px;
    background: rgba(10, 14, 39, 0.8);
    border-bottom: 2px solid rgba(64, 158, 255, 0.3);
    position: relative;

    &::before {
      content: '';
      position: absolute;
      top: 0;
      left: 0;
      right: 0;
      height: 2px;
      background: linear-gradient(90deg, transparent, #409eff, transparent);
    }

    .header-left {
      flex: 1;

      .current-time {
        font-size: 16px;
        color: #409eff;
        font-family: 'Courier New', monospace;
        letter-spacing: 2px;
      }
    }

    .header-title {
      flex: 2;
      text-align: center;

      .title-main {
        font-size: 36px;
        font-weight: bold;
        background: linear-gradient(90deg, #409eff, #67c23a);
        -webkit-background-clip: text;
        -webkit-text-fill-color: transparent;
        letter-spacing: 8px;
        text-shadow: 0 0 20px rgba(64, 158, 255, 0.5);
      }
    }

    .header-right {
      flex: 1;
      display: flex;
      justify-content: flex-end;

      .close-btn {
        background: rgba(245, 108, 108, 0.2);
        border-color: #f56c6c;
        color: #f56c6c;

        &:hover {
          background: rgba(245, 108, 108, 0.4);
        }
      }
    }
  }

  // 大屏主体
  .screen-body {
    flex: 1;
    display: flex;
    padding: 20px;
    gap: 20px;
    position: relative;

    // 左侧边栏
    .left-sidebar {
      width: 200px;
      display: flex;
      flex-direction: column;
      gap: 15px;

      .sidebar-title {
        display: flex;
        align-items: center;
        gap: 8px;
        font-size: 16px;
        font-weight: bold;
        color: #409eff;
        padding: 10px;
        background: rgba(64, 158, 255, 0.1);
        border-left: 3px solid #409eff;
        border-radius: 4px;

        i {
          font-size: 18px;
        }
      }

      .sidebar-items {
        display: flex;
        flex-direction: column;
        gap: 12px;

        .sidebar-item {
          display: flex;
          align-items: center;
          gap: 10px;
          padding: 12px;
          background: rgba(26, 31, 58, 0.6);
          border: 1px solid rgba(64, 158, 255, 0.2);
          border-radius: 6px;
          transition: all 0.3s;
          cursor: pointer;

          &:hover {
            background: rgba(64, 158, 255, 0.15);
            border-color: #409eff;
            transform: translateX(5px);
          }

          .item-icon {
            width: 36px;
            height: 36px;
            display: flex;
            align-items: center;
            justify-content: center;
            background: rgba(64, 158, 255, 0.2);
            border-radius: 50%;

            i {
              font-size: 18px;
              color: #409eff;
            }
          }

          .item-content {
            flex: 1;

            .item-name {
              font-size: 14px;
              font-weight: bold;
              color: #fff;
              margin-bottom: 4px;
            }

            .item-desc {
              font-size: 11px;
              color: rgba(255, 255, 255, 0.6);
              line-height: 1.4;
            }
          }
        }
      }
    }

    // 右侧边栏
    .right-sidebar {
      width: 200px;
      display: flex;
      flex-direction: column;
      gap: 15px;

      .sidebar-title {
        display: flex;
        align-items: center;
        gap: 8px;
        font-size: 16px;
        font-weight: bold;
        color: #67c23a;
        padding: 10px;
        background: rgba(103, 194, 58, 0.1);
        border-right: 3px solid #67c23a;
        border-radius: 4px;

        i {
          font-size: 18px;
        }
      }

      .sidebar-items {
        display: flex;
        flex-direction: column;
        gap: 12px;

        .sidebar-item {
          display: flex;
          align-items: center;
          gap: 10px;
          padding: 12px;
          background: rgba(26, 31, 58, 0.6);
          border: 1px solid rgba(103, 194, 58, 0.2);
          border-radius: 6px;
          transition: all 0.3s;
          cursor: pointer;

          &:hover {
            background: rgba(103, 194, 58, 0.15);
            border-color: #67c23a;
            transform: translateX(-5px);
          }

          .item-icon {
            width: 36px;
            height: 36px;
            display: flex;
            align-items: center;
            justify-content: center;
            background: rgba(103, 194, 58, 0.2);
            border-radius: 50%;

            i {
              font-size: 18px;
              color: #67c23a;
            }
          }

          .item-content {
            flex: 1;

            .item-name {
              font-size: 14px;
              font-weight: bold;
              color: #fff;
              margin-bottom: 4px;
            }

            .item-desc {
              font-size: 11px;
              color: rgba(255, 255, 255, 0.6);
              line-height: 1.4;
            }
          }
        }
      }
    }

    // 中间内容区
    .center-content {
      flex: 1;
      display: flex;
      flex-direction: column;
      gap: 20px;

      // 核心理念
      .core-concept {
        .concept-title {
          font-size: 18px;
          font-weight: bold;
          color: #409eff;
          text-align: center;
          margin-bottom: 15px;
          letter-spacing: 2px;
        }

        .concept-cards {
          display: grid;
          grid-template-columns: repeat(3, 1fr);
          gap: 15px;

          .concept-card {
            background: rgba(26, 31, 58, 0.6);
            border: 1px solid rgba(64, 158, 255, 0.3);
            border-radius: 8px;
            padding: 15px;
            text-align: center;
            transition: all 0.3s;

            &:hover {
              background: rgba(64, 158, 255, 0.1);
              border-color: #409eff;
              transform: translateY(-3px);
              box-shadow: 0 8px 16px rgba(64, 158, 255, 0.3);
            }

            .card-title {
              font-size: 16px;
              font-weight: bold;
              color: #67c23a;
              margin-bottom: 10px;
            }

            .card-content {
              .card-desc {
                font-size: 12px;
                color: rgba(255, 255, 255, 0.7);
                line-height: 1.6;
              }
            }
          }
        }
      }

      // 主展示区
      .main-display {
        flex: 1;
        display: flex;
        gap: 20px;
        align-items: center;

        .display-left,
        .display-right {
          flex: 1;

          .model-box {
            background: rgba(26, 31, 58, 0.6);
            border: 1px solid rgba(64, 158, 255, 0.3);
            border-radius: 8px;
            padding: 15px;
            height: 100%;

            .model-title {
              font-size: 14px;
              font-weight: bold;
              color: #409eff;
              text-align: center;
              margin-bottom: 15px;
              padding-bottom: 10px;
              border-bottom: 1px solid rgba(64, 158, 255, 0.2);
            }

            .model-content {
              .model-chart {
                height: 250px;
              }
            }
          }
        }

        .display-center {
          flex: 1.2;
          display: flex;
          align-items: center;
          justify-content: center;

          .penetration-core {
            width: 280px;
            height: 280px;
            border-radius: 50%;
            background: radial-gradient(circle, rgba(64, 158, 255, 0.2), rgba(10, 14, 39, 0.8));
            border: 3px solid #409eff;
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
            position: relative;
            box-shadow: 0 0 40px rgba(64, 158, 255, 0.5), inset 0 0 40px rgba(64, 158, 255, 0.2);

            &::before {
              content: '';
              position: absolute;
              width: 320px;
              height: 320px;
              border-radius: 50%;
              border: 1px solid rgba(64, 158, 255, 0.3);
              animation: pulse 3s infinite;
            }

            .core-title {
              font-size: 28px;
              font-weight: bold;
              color: #67c23a;
              margin-bottom: 20px;
              letter-spacing: 4px;
              text-shadow: 0 0 10px rgba(103, 194, 58, 0.8);
            }

            .core-indicators {
              display: grid;
              grid-template-columns: repeat(2, 1fr);
              gap: 15px;

              .indicator-item {
                width: 80px;
                height: 40px;
                background: linear-gradient(135deg, #409eff, #67c23a);
                border-radius: 20px;
                display: flex;
                align-items: center;
                justify-content: center;
                font-size: 16px;
                font-weight: bold;
                color: #fff;
                cursor: pointer;
                transition: all 0.3s;
                box-shadow: 0 4px 8px rgba(0, 0, 0, 0.3);

                &:hover {
                  transform: scale(1.1);
                  box-shadow: 0 6px 12px rgba(64, 158, 255, 0.5);
                }
              }
            }
          }
        }
      }
    }
  }

  // 底部：三大目标
  .screen-footer {
    height: 120px;
    padding: 15px 40px;
    background: rgba(10, 14, 39, 0.8);
    border-top: 2px solid rgba(64, 158, 255, 0.3);
    position: relative;

    &::before {
      content: '';
      position: absolute;
      bottom: 0;
      left: 0;
      right: 0;
      height: 2px;
      background: linear-gradient(90deg, transparent, #409eff, transparent);
    }

    .footer-title {
      font-size: 20px;
      font-weight: bold;
      color: #409eff;
      text-align: center;
      margin-bottom: 15px;
      letter-spacing: 4px;
    }

    .footer-content {
      display: grid;
      grid-template-columns: repeat(3, 1fr);
      gap: 20px;

      .goal-item {
        display: flex;
        align-items: center;
        gap: 12px;
        padding: 12px;
        background: rgba(26, 31, 58, 0.6);
        border: 1px solid rgba(64, 158, 255, 0.2);
        border-radius: 6px;
        transition: all 0.3s;

        &:hover {
          background: rgba(64, 158, 255, 0.1);
          border-color: #409eff;
          transform: translateY(-2px);
        }

        .goal-icon {
          width: 40px;
          height: 40px;
          display: flex;
          align-items: center;
          justify-content: center;
          background: rgba(64, 158, 255, 0.2);
          border-radius: 50%;

          i {
            font-size: 20px;
            color: #409eff;
          }
        }

        .goal-text {
          flex: 1;
          font-size: 13px;
          color: rgba(255, 255, 255, 0.8);
          line-height: 1.6;

          .highlight {
            color: #e6a23c;
            font-weight: bold;
          }
        }
      }
    }
  }
}

@keyframes pulse {
  0%, 100% {
    opacity: 0.3;
    transform: scale(1);
  }
  50% {
    opacity: 0.6;
    transform: scale(1.05);
  }
}
</style>

