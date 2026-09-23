<!-- 智能风控管理驾驶舱 -->
<template>
  <div class="risk-cockpit-container">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-left">
        <i class="el-icon-data-analysis"></i>
        <h2>智能风控管理驾驶舱</h2>
      </div>
      <div class="header-right">
        <el-button
          type="primary"
          icon="el-icon-full-screen"
          @click="openFullScreen"
          class="screen-btn"
        >
          大屏
        </el-button>
      </div>
    </div>

    <!-- 主要内容区域 -->
    <div class="main-content">
      <!-- 按集团风险分析标题 -->
      <div class="section-title">
        <i class="el-icon-s-data"></i>
        <span>按集团风险分析</span>
      </div>
    <el-row :gutter="10">
      <el-col :span="24">
        <!-- 数量统计 6块 -->
        <first :orgid="orgid"></first>
      </el-col>
      <!-- 按公司风险分析标题 -->
      <div class="section-title">
        <i class="el-icon-office-building"></i>
        <span>按公司风险分析</span>
      </div>

      <!-- 公司选择器 -->
      <el-card class="company-selector-card">
        <div class="selector-wrapper">
          <span class="selector-label">公司</span>
          <el-select v-model="orgid" placeholder="请选择公司" class="selector-input">
            <el-option
              v-for="(item, index) in companyList"
              :key="index"
              :label="item.orgname"
              :value="item.orgid"
            ></el-option>
          </el-select>
        </div>
      </el-card>
      <el-card>
        <el-row :gutter="10">
          <el-col :span="6">
            <el-col :span="24">
              <div class="one-item">
                <div>
                  <div class="title">{{ zs }}</div>
                  <p>风险总数量</p>
                </div>
                <div>
                  <i class="el-icon-money"></i>
                </div>
              </div>
            </el-col>
            <el-col :span="24">
              <div class="one-item">
                <div>
                  <div class="title">{{ ysp }}</div>
                  <p>已审批确认风险数量</p>
                </div>
                <div>
                  <i class="el-icon-collection"></i>
                </div>
              </div>
            </el-col>
            <el-col :span="24">
              <div class="one-item">
                <div>
                  <div class="title">{{ wsp }}</div>
                  <p>未审批确认风险数量</p>
                </div>
                <div>
                  <i class="el-icon-umbrella"></i>
                </div>
              </div>
            </el-col>
          </el-col>
          <el-col :span="6">
            <div style="border: 1px solid #d2d2d2">
              <chat8 :orgid="orgid"></chat8>
            </div>
          </el-col>
          <el-col :span="6">
            <div style="border: 1px solid #d2d2d2">
              <chat9 :orgid="orgid"></chat9>
            </div>
          </el-col>
          <el-col :span="6">
            <el-col :span="24">
              <div class="one-item">
                <div>
                  <div class="title">{{ yiBan + zhongDa }}</div>
                  <p>风险事件总数量</p>
                </div>
                <div>
                  <i class="el-icon-umbrella"></i>
                </div>
              </div>
            </el-col>
            <el-col :span="24">
              <div class="one-item">
                <div>
                  <div class="title">{{ yiBan }}</div>
                  <p>一般风险事件数量</p>
                </div>
                <div>
                  <i class="el-icon-umbrella"></i>
                </div>
              </div>
            </el-col>
            <el-col :span="24">
              <div class="one-item">
                <div>
                  <div class="title">{{ zhongDa }}</div>
                  <p>重大风险事件数量</p>
                </div>
                <div>
                  <i class="el-icon-umbrella"></i>
                </div>
              </div>
            </el-col>
          </el-col>
        </el-row>
      </el-card>
      <el-col :span="12">
        <el-card
          style="width: 100%; height: 360px; padding: 10px; margin-bottom: 10px"
        >
          <chat4 :orgid="orgid"></chat4>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card
          style="width: 100%; height: 360px; padding: 10px; margin-bottom: 10px"
        >
          <chat10 :orgid="orgid"></chat10>
        </el-card>
      </el-col>
      <el-col :span="24">
        <el-card
          style="width: 100%; height: 360px; padding: 10px; margin-bottom: 10px"
        >
          <chat5 :orgid="orgid"></chat5>
        </el-card>
      </el-col>

      <el-col :span="12">
        <el-card
          style="width: 100%; height: 360px; padding: 10px; margin-bottom: 10px"
        >
          <chat6 :orgid="orgid"></chat6>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card
          style="width: 100%; height: 360px; padding: 10px; margin-bottom: 10px"
        >
          <chat19 :orgid="orgid"></chat19>
        </el-card>
      </el-col>
      <el-col :span="24">
        <el-card
          style="width: 100%; height: 360px; padding: 10px; margin-bottom: 10px"
        >
          <chat2 :orgid="orgid"></chat2>
        </el-card>
      </el-col>
      <el-col :span="24">
        <el-card style="width: 100%">
          <pgrlt :orgid="String(orgid)"></pgrlt>
        </el-card>
      </el-col>

      <!-- <el-col :span="12">
          <chat7 :orgid="orgid"></chat7>
        </el-col> -->
    </el-row>
    </div>

    <!-- 全屏大屏容器 -->
    <div v-if="screenVisible" ref="fullscreenContainer" class="fullscreen-container">
      <ScreenDisplay
        :risk-data="riskScreenData"
        @close="closeFullScreen"
      />
    </div>
  </div>
</template>

<script>
  import { getRiskCompanyList } from '@/api/risk/home.js'
  import {
    numberRisks,
    numberEventsRisks,
    riskNumbers,
  } from '@/api/risk/home.js'
  import first from './fxComponents/first.vue'
  import chat1 from './fxComponents/echat1.vue'
  import chat2 from './fxComponents/echat2.vue'
  import chat3 from './fxComponents/echat3.vue'
  import chat4 from './fxComponents/echat4.vue'
  import chat5 from './fxComponents/echat5.vue'
  import chat6 from './fxComponents/echat6.vue'
  import chat7 from './fxComponents/echat7.vue'
  import chat8 from './fxComponents/echat8.vue'
  import chat9 from './fxComponents/echat9.vue'
  import chat10 from './fxComponents/echat10.vue'
  import chat19 from './fxComponents/echat19.vue'
  import pgrlt from './fxComponents/pgrlt.vue'
  import ScreenDisplay from './ScreenDisplay.vue'
  export default {
    components: {
      chat1,
      chat2,
      chat3,
      chat4,
      chat5,
      chat6,
      chat7,
      chat8,
      chat9,
      chat10,
      first,
      chat19,
      pgrlt,
      ScreenDisplay,
    },
    data() {
      return {
        calendarValue: new Date(),
        companyList: [],
        orgid: localStorage.getItem('userInfo')
          ? JSON.parse(localStorage.getItem('userInfo')).currentOrg.orgid
          : '',
        risksYiBan: 0,
        riskszhongDa: 0,
        yiBan: 0,
        zhongDa: 0,
        wsp: 0,
        ysp: 0,
        zs: 0,
        // 大屏相关
        screenVisible: false,
        riskScreenData: {},
      }
    },
    watch: {
      orgid: {
        handler(newVal) {
          this.getNumberRisks()
          this.getNumberEventsRisks()
          this.getRiskNumbers()
        },
        immediate: true,
      },
    },
    mounted() {
      this.getData()
      // 监听全屏变化事件
      document.addEventListener('fullscreenchange', this.handleFullscreenChange)
      document.addEventListener('webkitfullscreenchange', this.handleFullscreenChange)
      document.addEventListener('mozfullscreenchange', this.handleFullscreenChange)
      document.addEventListener('MSFullscreenChange', this.handleFullscreenChange)
    },
    beforeDestroy() {
      // 移除全屏变化事件监听
      document.removeEventListener('fullscreenchange', this.handleFullscreenChange)
      document.removeEventListener('webkitfullscreenchange', this.handleFullscreenChange)
      document.removeEventListener('mozfullscreenchange', this.handleFullscreenChange)
      document.removeEventListener('MSFullscreenChange', this.handleFullscreenChange)
    },
    methods: {
      async getData() {
        const { data, code, msg } = await getRiskCompanyList()
        this.companyList = data.data
      },
      getNumberRisks() {
        numberRisks({ company: this.orgid }).then((res) => {
          if (res && res.code == 1) {
            // 一般风险和最大风险
            this.risksYiBan = res.data.risksYiBan || 0
            this.riskszhongDa = res.data.riskszhongDa || 0
          }
        })
      },
      getNumberEventsRisks() {
        numberEventsRisks({ company: this.orgid }).then((res) => {
          if (res && res.code == 1) {
            // 一般风险事件和最大风险事件
            this.yiBan = res.data.yiBan || 0
            this.zhongDa = res.data.zhongDa || 0
          }
        })
      },
      getRiskNumbers() {
        riskNumbers({ company: this.orgid }).then((res) => {
          if (res && res.code == 1) {
            // 一般风险事件和最大风险事件
            this.wsp = res.data.wsp || 0
            this.ysp = res.data.ysp || 0
            this.zs = res.data.zs || 0
          }
        })
      },
      // 打开全屏大屏
      openFullScreen() {
        console.log('openFullScreen被调用,准备打开全屏大屏')
        // 准备大屏数据 - 确保 orgid 是字符串类型
        this.riskScreenData = {
          orgid: String(this.orgid),
          companyList: this.companyList,
          risksYiBan: this.risksYiBan,
          riskszhongDa: this.riskszhongDa,
          yiBan: this.yiBan,
          zhongDa: this.zhongDa,
          wsp: this.wsp,
          ysp: this.ysp,
          zs: this.zs,
        }
        console.log('设置screenVisible为true')
        this.screenVisible = true

        // 使用 $nextTick 确保 DOM 更新后再进入全屏
        this.$nextTick(() => {
          console.log('screenVisible当前值:', this.screenVisible)
          this.enterFullscreen()
        })
      },
      // 进入全屏模式
      enterFullscreen() {
        const container = this.$refs.fullscreenContainer
        if (container) {
          if (container.requestFullscreen) {
            container.requestFullscreen()
          } else if (container.webkitRequestFullscreen) {
            container.webkitRequestFullscreen()
          } else if (container.mozRequestFullScreen) {
            container.mozRequestFullScreen()
          } else if (container.msRequestFullscreen) {
            container.msRequestFullscreen()
          }
          console.log('已请求进入全屏模式')
        }
      },
      // 退出全屏模式
      exitFullscreen() {
        if (document.exitFullscreen) {
          document.exitFullscreen()
        } else if (document.webkitExitFullscreen) {
          document.webkitExitFullscreen()
        } else if (document.mozCancelFullScreen) {
          document.mozCancelFullScreen()
        } else if (document.msExitFullscreen) {
          document.msExitFullscreen()
        }
        console.log('已请求退出全屏模式')
      },
      // 关闭全屏大屏
      closeFullScreen() {
        this.exitFullscreen()
        this.screenVisible = false
      },
      // 监听全屏状态变化
      handleFullscreenChange() {
        const isFullscreen = !!(document.fullscreenElement ||
                               document.webkitFullscreenElement ||
                               document.mozFullScreenElement ||
                               document.msFullscreenElement)

        // 如果退出了全屏，同时关闭大屏组件
        if (!isFullscreen && this.screenVisible) {
          console.log('检测到退出全屏，关闭大屏组件')
          this.screenVisible = false
        }
      },
    },
  }
</script>

<style scoped lang="scss">
  .risk-cockpit-container {
    padding: 20px;
    background: #f0f2f5;
    min-height: calc(100vh - 84px);
  }

  .page-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;
    padding: 15px 20px;
    background: #fff;
    border-radius: 4px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);

    .header-left {
      display: flex;
      align-items: center;
      gap: 10px;

      i {
        font-size: 24px;
        color: #409eff;
      }

      h2 {
        margin: 0;
        font-size: 20px;
        color: #303133;
      }
    }

    .header-right {
      .screen-btn {
        font-size: 14px;
      }
    }
  }

  .main-content {
    .section-title {
      display: flex;
      align-items: center;
      gap: 8px;
      font-size: 18px;
      font-weight: bold;
      color: #303133;
      margin-bottom: 15px;
      padding: 12px 20px;
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      color: #fff;
      border-radius: 4px;
      box-shadow: 0 2px 8px rgba(102, 126, 234, 0.3);

      i {
        font-size: 20px;
      }
    }

    .company-selector-card {
      margin-bottom: 20px;
      border-radius: 4px;
      box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);

      .selector-wrapper {
        display: flex;
        align-items: center;
        gap: 15px;

        .selector-label {
          font-size: 14px;
          color: #606266;
          font-weight: 500;
        }

        .selector-input {
          width: 300px;
        }
      }
    }
  }

  .is-selected {
    color: #1989fa;
  }
  .headTitle {
    font-size: 20px;
  }
  ::v-deep .search_input {
    .el-card__body {
      padding-top: 12px !important;
    }
  }
  .title-text {
    font-size: 26px;
    text-align: center;
    color: #ff8c00;
    font-weight: bold;
  }
  .titleBar {
    font-size: 30px;
    font-weight: 600;
    background-color: #80b6f4;
    padding: 10px;
    margin-bottom: 10px;
  }
  .one-item {
    display: flex;
    justify-content: space-between;
    background: white;
    padding: 5px;
    margin-bottom: 10px;
    border: 1px solid #d2d2d2;
    .title {
      font-size: 24px;
      font-weight: 700;
    }
  }
  i {
    font-size: 24px;
  }

  // 全屏容器样式
  .fullscreen-container {
    position: fixed;
    top: 0;
    left: 0;
    width: 100vw;
    height: 100vh;
    z-index: 9999;
    background: #0a0e27;
    overflow: hidden;
  }
</style>
