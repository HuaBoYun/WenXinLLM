<template>
  <div class="setting-home-container">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-left">
        <i class="el-icon-s-data"></i>
        <h2>系统设置统计</h2>
      </div>
      <div class="header-right">
        <el-button
          class="screen-btn user-portrait-btn"
          icon="el-icon-user"
          type="success"
          @click="openUserPortraitScreen"
        >
          用户画像
        </el-button>
        <el-button
          class="screen-btn system-portrait-btn"
          icon="el-icon-setting"
          type="primary"
          @click="openSystemPortraitScreen"
        >
          系统画像
        </el-button>
      </div>
    </div>

    <!-- 主要内容区域 -->
    <div class="main-content">
      <!-- 数据概览卡片 -->
      <div class="overview-section">
        <el-row :gutter="20">
          <el-col :span="6">
            <el-card class="overview-card card-blue">
              <div class="card-icon">
                <i class="el-icon-s-custom"></i>
              </div>
              <div class="card-content">
                <div class="card-label">内控数据</div>
                <div class="card-value">{{ dataTotalInfo.nk || 0 }}</div>
                <div class="card-desc">内控相关数据总量</div>
              </div>
            </el-card>
          </el-col>
          <el-col :span="6">
            <el-card class="overview-card card-green">
              <div class="card-icon">
                <i class="el-icon-warning-outline"></i>
              </div>
              <div class="card-content">
                <div class="card-label">风险数据</div>
                <div class="card-value">{{ dataTotalInfo.fx || 0 }}</div>
                <div class="card-desc">风险管理数据总量</div>
              </div>
            </el-card>
          </el-col>
          <el-col :span="6">
            <el-card class="overview-card card-orange">
              <div class="card-icon">
                <i class="el-icon-document-checked"></i>
              </div>
              <div class="card-content">
                <div class="card-label">审计数据</div>
                <div class="card-value">{{ dataTotalInfo.ck || 0 }}</div>
                <div class="card-desc">审计相关数据总量</div>
              </div>
            </el-card>
          </el-col>
          <el-col :span="6">
            <el-card class="overview-card card-red">
              <div class="card-icon">
                <i class="el-icon-edit-outline"></i>
              </div>
              <div class="card-content">
                <div class="card-label">整改数据</div>
                <div class="card-value">{{ dataTotalInfo.zg || 0 }}</div>
                <div class="card-desc">整改追踪数据总量</div>
              </div>
            </el-card>
          </el-col>
        </el-row>
      </div>

      <!-- 核心功能使用率 -->
      <div class="function-usage-section">
        <el-card>
          <div slot="header" class="card-title">
            <i class="el-icon-data-analysis"></i>
            <span>核心功能使用率（最近30天）</span>
          </div>
          <el-row :gutter="20">
            <el-col :span="6">
              <div class="usage-item">
                <div class="usage-icon" style="background: #409eff">
                  <i class="el-icon-s-custom"></i>
                </div>
                <div class="usage-content">
                  <div class="usage-label">内控功能</div>
                  <div class="usage-value">{{ coreFunctionData.controlCount || 0 }}</div>
                  <div class="usage-desc">访问次数</div>
                </div>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="usage-item">
                <div class="usage-icon" style="background: #67c23a">
                  <i class="el-icon-warning-outline"></i>
                </div>
                <div class="usage-content">
                  <div class="usage-label">风险功能</div>
                  <div class="usage-value">{{ coreFunctionData.riskCount || 0 }}</div>
                  <div class="usage-desc">访问次数</div>
                </div>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="usage-item">
                <div class="usage-icon" style="background: #e6a23c">
                  <i class="el-icon-document-checked"></i>
                </div>
                <div class="usage-content">
                  <div class="usage-label">审计功能</div>
                  <div class="usage-value">{{ coreFunctionData.auditCount || 0 }}</div>
                  <div class="usage-desc">访问次数</div>
                </div>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="usage-item">
                <div class="usage-icon" style="background: #f56c6c">
                  <i class="el-icon-edit-outline"></i>
                </div>
                <div class="usage-content">
                  <div class="usage-label">整改功能</div>
                  <div class="usage-value">{{ coreFunctionData.rectifyCount || 0 }}</div>
                  <div class="usage-desc">访问次数</div>
                </div>
              </div>
            </el-col>
          </el-row>
        </el-card>
      </div>

      <!-- 统计图表 -->
      <div class="charts-section">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-card>
              <div slot="header" class="card-title">
                <i class="el-icon-data-line"></i>
                <span>数据增长趋势（最近12个月）</span>
              </div>
              <div ref="growthTrendChart" style="height: 350px"></div>
            </el-card>
          </el-col>
          <el-col :span="12">
            <el-card>
              <div slot="header" class="card-title">
                <i class="el-icon-s-data"></i>
                <span>业务活跃度排名（TOP10）</span>
              </div>
              <div ref="activityRankingChart" style="height: 350px"></div>
            </el-card>
          </el-col>
        </el-row>
      </div>

      <!-- 用户统计图表 -->
      <div class="user-charts-section">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-card>
              <div slot="header" class="card-title">
                <i class="el-icon-user"></i>
                <span>新增用户统计（最近12个月）</span>
              </div>
              <div ref="newUserChart" style="height: 350px"></div>
            </el-card>
          </el-col>
          <el-col :span="12">
            <el-card>
              <div slot="header" class="card-title">
                <i class="el-icon-pie-chart"></i>
                <span>用户活跃度分布</span>
              </div>
              <div ref="userActivityChart" style="height: 350px"></div>
            </el-card>
          </el-col>
        </el-row>
      </div>
    </div>

    <!-- 用户画像大屏弹窗 -->
    <el-dialog
      custom-class="screen-dialog"
      fullscreen
      :show-close="false"
      :visible.sync="userPortraitVisible"
      @close="handleUserPortraitClose"
    >
      <UserPortraitScreen
        v-if="userPortraitVisible"
        :year="currentYear"
        @close="closeUserPortraitScreen"
      />
    </el-dialog>

    <!-- 系统画像大屏弹窗 -->
    <el-dialog
      custom-class="screen-dialog"
      fullscreen
      :show-close="false"
      :visible.sync="systemPortraitVisible"
      @close="handleSystemPortraitClose"
    >
      <SystemPortraitScreen
        v-if="systemPortraitVisible"
        :year="currentYear"
        @close="closeSystemPortraitScreen"
      />
    </el-dialog>
  </div>
</template>

<script>
  import * as echarts from 'echarts'
  import SystemPortraitScreen from './SystemPortraitScreen.vue'
  import UserPortraitScreen from './UserPortraitScreen.vue'
  import { getDataGrowthTrend } from '@/api/setting/dataGrowthTrend'
  import { getDataTotal } from '@/api/setting/dataTotal'
  import { getCoreFunctionUsage } from '@/api/setting/coreFunctionUsage'
  import { getBusinessActivityRanking } from '@/api/setting/businessActivityRanking'
  import { getNewUserCount } from '@/api/setting/newUserCount'
  import { getUserActivity } from '@/api/setting/userActivity'

  export default {
    name: 'SettingHome',
    components: {
      SystemPortraitScreen,
      UserPortraitScreen,
    },
    data() {
      return {
        currentYear: new Date().getFullYear().toString(),
        // 用户画像大屏相关
        userPortraitVisible: false,
        // 系统画像大屏相关
        systemPortraitVisible: false,

        // 数据总量
        dataTotalInfo: {
          nk: 0,
          fx: 0,
          ck: 0,
          zg: 0
        },

        // 核心功能使用率
        coreFunctionData: {
          controlCount: 0,  // 内控
          riskCount: 0,     // 风险
          auditCount: 0,    // 审计
          rectifyCount: 0   // 整改
        },

        // 数据增长趋势
        growthTrendData: [],

        // 业务活跃度排名
        activityRankingData: [],

        // 新增用户数据（后端返回 List<{m, c}>）
        newUserData: [],

        // 用户活跃度数据（后端返回 List<{yearMonth, c}>）
        userActivityData: [],

        // 图表实例
        growthTrendChart: null,
        activityRankingChart: null,
        newUserChart: null,
        userActivityChart: null,
      }
    },
    mounted() {
      // 加载所有数据
      this.loadAllData()

      // 添加窗口resize监听
      window.addEventListener('resize', this.handleResize)
    },
    beforeDestroy() {
      // 销毁图表实例
      if (this.growthTrendChart) this.growthTrendChart.dispose()
      if (this.activityRankingChart) this.activityRankingChart.dispose()
      if (this.newUserChart) this.newUserChart.dispose()
      if (this.userActivityChart) this.userActivityChart.dispose()

      // 移除窗口resize监听
      window.removeEventListener('resize', this.handleResize)
    },
    methods: {
      // 加载所有数据
      async loadAllData() {
        await Promise.all([
          this.fetchDataTotal(),
          this.fetchCoreFunctionUsage(),
          this.fetchGrowthTrend(),
          this.fetchActivityRanking(),
          this.fetchNewUserCount(),
          this.fetchUserActivity()
        ])
      },

      // 获取数据总量
      async fetchDataTotal() {
        try {
          const response = await getDataTotal()
          if (response.code === 1 && response.data) {
            this.dataTotalInfo = response.data
          }
        } catch (error) {
          console.error('获取数据总量失败:', error)
        }
      },

      // 获取核心功能使用率
      async fetchCoreFunctionUsage() {
        try {
          const response = await getCoreFunctionUsage()
          if (response.code === 1 && response.data) {
            this.coreFunctionData = response.data
          }
        } catch (error) {
          console.error('获取核心功能使用率失败:', error)
        }
      },

      // 获取数据增长趋势
      async fetchGrowthTrend() {
        try {
          const response = await getDataGrowthTrend()
          if (response.code === 1 && response.data) {
            this.growthTrendData = response.data
            this.$nextTick(() => {
              this.initGrowthTrendChart()
            })
          }
        } catch (error) {
          console.error('获取数据增长趋势失败:', error)
        }
      },

      // 获取业务活跃度排名
      async fetchActivityRanking() {
        try {
          const response = await getBusinessActivityRanking()
          if (response.code === 1 && response.data) {
            this.activityRankingData = response.data
            this.$nextTick(() => {
              this.initActivityRankingChart()
            })
          }
        } catch (error) {
          console.error('获取业务活跃度排名失败:', error)
        }
      },

      // 获取新增用户数
      async fetchNewUserCount() {
        try {
          const response = await getNewUserCount()
          if (response.code === 1 && response.data) {
            this.newUserData = response.data
            this.$nextTick(() => {
              this.initNewUserChart()
            })
          }
        } catch (error) {
          console.error('获取新增用户数失败:', error)
        }
      },

      // 获取用户活跃度
      async fetchUserActivity() {
        try {
          const response = await getUserActivity()
          if (response.code === 1 && response.data) {
            this.userActivityData = response.data
            this.$nextTick(() => {
              this.initUserActivityChart()
            })
          }
        } catch (error) {
          console.error('获取用户活跃度失败:', error)
        }
      },

      // 初始化数据增长趋势图
      initGrowthTrendChart() {
        if (!this.$refs.growthTrendChart) return

        if (this.growthTrendChart) {
          this.growthTrendChart.dispose()
        }

        this.growthTrendChart = echarts.init(this.$refs.growthTrendChart)

        const categories = this.growthTrendData.map(item => item.yearMonth)

        const option = {
          tooltip: {
            trigger: 'axis',
            axisPointer: {
              type: 'cross'
            }
          },
          legend: {
            data: ['内控', '风险', '审计', '整改'],
            top: '5%'
          },
          grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            top: '15%',
            containLabel: true
          },
          xAxis: {
            type: 'category',
            boundaryGap: false,
            data: categories,
            axisLabel: {
              rotate: 45
            }
          },
          yAxis: {
            type: 'value'
          },
          series: [
            {
              name: '内控',
              type: 'line',
              smooth: true,
              data: this.growthTrendData.map(item => item.nk),
              itemStyle: { color: '#409eff' },
              areaStyle: {
                color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                  { offset: 0, color: 'rgba(64, 158, 255, 0.3)' },
                  { offset: 1, color: 'rgba(64, 158, 255, 0.05)' }
                ])
              }
            },
            {
              name: '风险',
              type: 'line',
              smooth: true,
              data: this.growthTrendData.map(item => item.fx),
              itemStyle: { color: '#67c23a' },
              areaStyle: {
                color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                  { offset: 0, color: 'rgba(103, 194, 58, 0.3)' },
                  { offset: 1, color: 'rgba(103, 194, 58, 0.05)' }
                ])
              }
            },
            {
              name: '审计',
              type: 'line',
              smooth: true,
              data: this.growthTrendData.map(item => item.ck),
              itemStyle: { color: '#e6a23c' },
              areaStyle: {
                color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                  { offset: 0, color: 'rgba(230, 162, 60, 0.3)' },
                  { offset: 1, color: 'rgba(230, 162, 60, 0.05)' }
                ])
              }
            },
            {
              name: '整改',
              type: 'line',
              smooth: true,
              data: this.growthTrendData.map(item => item.zg),
              itemStyle: { color: '#f56c6c' },
              areaStyle: {
                color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                  { offset: 0, color: 'rgba(245, 108, 108, 0.3)' },
                  { offset: 1, color: 'rgba(245, 108, 108, 0.05)' }
                ])
              }
            }
          ]
        }

        this.growthTrendChart.setOption(option)
      },
      // 初始化业务活跃度排名图
      initActivityRankingChart() {
        if (!this.$refs.activityRankingChart) return

        if (this.activityRankingChart) {
          this.activityRankingChart.dispose()
        }

        this.activityRankingChart = echarts.init(this.$refs.activityRankingChart)

        // 后端返回的字段是 b(业务模块) 和 c(数量)
        const categories = this.activityRankingData.map(item => item.b || '未知')
        const values = this.activityRankingData.map(item => item.c || 0)

        const option = {
          tooltip: {
            trigger: 'axis',
            axisPointer: {
              type: 'shadow'
            }
          },
          grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            top: '5%',
            containLabel: true
          },
          xAxis: {
            type: 'value'
          },
          yAxis: {
            type: 'category',
            data: categories,
            inverse: true
          },
          series: [
            {
              name: '访问次数',
              type: 'bar',
              data: values,
              itemStyle: {
                color: new echarts.graphic.LinearGradient(0, 0, 1, 0, [
                  { offset: 0, color: '#409eff' },
                  { offset: 1, color: '#66b1ff' }
                ])
              },
              label: {
                show: true,
                position: 'right',
                formatter: '{c}'
              }
            }
          ]
        }

        this.activityRankingChart.setOption(option)
      },

      // 初始化新增用户图
      initNewUserChart() {
        if (!this.$refs.newUserChart) return

        if (this.newUserChart) {
          this.newUserChart.dispose()
        }

        this.newUserChart = echarts.init(this.$refs.newUserChart)

        // 后端返回的是 List<{m, c}>，m是年月，c是数量
        const categories = this.newUserData.map(item => item.m || '')
        const values = this.newUserData.map(item => item.c || 0)

        const option = {
          tooltip: {
            trigger: 'axis',
            axisPointer: {
              type: 'shadow'
            }
          },
          grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            top: '10%',
            containLabel: true
          },
          xAxis: {
            type: 'category',
            data: categories,
            axisLabel: {
              rotate: 45
            }
          },
          yAxis: {
            type: 'value',
            name: '新增用户数'
          },
          series: [
            {
              name: '新增用户',
              type: 'bar',
              data: values,
              itemStyle: {
                color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                  { offset: 0, color: '#83bff6' },
                  { offset: 0.5, color: '#188df0' },
                  { offset: 1, color: '#188df0' }
                ])
              },
              emphasis: {
                itemStyle: {
                  color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                    { offset: 0, color: '#2378f7' },
                    { offset: 0.7, color: '#2378f7' },
                    { offset: 1, color: '#83bff6' }
                  ])
                }
              }
            }
          ]
        }

        this.newUserChart.setOption(option)
      },

      // 初始化用户活跃度图
      initUserActivityChart() {
        if (!this.$refs.userActivityChart) return

        if (this.userActivityChart) {
          this.userActivityChart.dispose()
        }

        this.userActivityChart = echarts.init(this.$refs.userActivityChart)

        // 后端返回的是 List<{yearMonth, c}>，yearMonth是年月，c是活跃数量
        // 将数据转换为饼图需要的格式 {name, value}
        const pieData = this.userActivityData.map(item => ({
          name: item.yearMonth || '未知',
          value: item.c || 0
        }))

        const option = {
          tooltip: {
            trigger: 'item',
            formatter: '{a} <br/>{b}: {c} ({d}%)'
          },
          legend: {
            orient: 'vertical',
            right: '5%',
            top: 'center',
            textStyle: {
              fontSize: 12
            }
          },
          series: [
            {
              name: '用户活跃度',
              type: 'pie',
              radius: ['40%', '70%'],
              center: ['40%', '50%'],
              avoidLabelOverlap: false,
              itemStyle: {
                borderRadius: 10,
                borderColor: '#fff',
                borderWidth: 2
              },
              label: {
                show: false,
                position: 'center'
              },
              emphasis: {
                label: {
                  show: true,
                  fontSize: 16,
                  fontWeight: 'bold'
                }
              },
              labelLine: {
                show: false
              },
              data: pieData,
              color: [
                '#5470c6', '#91cc75', '#fac858', '#ee6666', '#73c0de',
                '#3ba272', '#fc8452', '#9a60b4', '#ea7ccc', '#d14a61',
                '#5793f3', '#675bba'
              ]
            }
          ]
        }

        this.userActivityChart.setOption(option)
      },

      // 窗口resize处理
      handleResize() {
        if (this.growthTrendChart) this.growthTrendChart.resize()
        if (this.activityRankingChart) this.activityRankingChart.resize()
        if (this.newUserChart) this.newUserChart.resize()
        if (this.userActivityChart) this.userActivityChart.resize()
      },

      // 打开用户画像大屏
      openUserPortraitScreen() {
        this.userPortraitVisible = true
        this.$nextTick(() => {
          this.enterFullscreen()
        })
      },

      // 关闭用户画像大屏
      closeUserPortraitScreen() {
        this.userPortraitVisible = false
        this.exitFullscreen()
      },

      // 用户画像大屏关闭事件
      handleUserPortraitClose() {
        this.userPortraitVisible = false
        this.exitFullscreen()
      },

      // 打开系统画像大屏
      openSystemPortraitScreen() {
        this.systemPortraitVisible = true
        this.$nextTick(() => {
          this.enterFullscreen()
        })
      },

      // 关闭系统画像大屏
      closeSystemPortraitScreen() {
        this.systemPortraitVisible = false
        this.exitFullscreen()
      },

      // 系统画像大屏关闭事件
      handleSystemPortraitClose() {
        this.systemPortraitVisible = false
        this.exitFullscreen()
      },

      // 进入浏览器全屏模式
      enterFullscreen() {
        const element = document.documentElement
        if (element.requestFullscreen) {
          element.requestFullscreen()
        } else if (element.webkitRequestFullscreen) {
          element.webkitRequestFullscreen()
        } else if (element.mozRequestFullScreen) {
          element.mozRequestFullScreen()
        } else if (element.msRequestFullscreen) {
          element.msRequestFullscreen()
        }
      },

      // 退出浏览器全屏模式
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
      },
    },
  }
</script>

<style lang="scss" scoped>
  .setting-home-container {
    padding: 20px;
    background: #f0f2f5;
    min-height: calc(100vh - 84px);

    .page-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 20px;
      padding: 20px 25px;
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      border-radius: 8px;
      box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);

      .header-left {
        display: flex;
        align-items: center;
        gap: 12px;

        i {
          font-size: 28px;
          color: #fff;
        }

        h2 {
          margin: 0;
          font-size: 22px;
          color: #fff;
          font-weight: 600;
        }
      }

      .screen-btn {
        font-size: 14px;
        padding: 10px 20px;
        border-radius: 6px;
        font-weight: 500;
        transition: all 0.3s;

        &.user-portrait-btn {
          background: #67c23a;
          border-color: #67c23a;

          &:hover {
            background: #85ce61;
            border-color: #85ce61;
            transform: translateY(-2px);
            box-shadow: 0 4px 8px rgba(103, 194, 58, 0.4);
          }
        }

        &.system-portrait-btn {
          background: #409eff;
          border-color: #409eff;

          &:hover {
            background: #66b1ff;
            border-color: #66b1ff;
            transform: translateY(-2px);
            box-shadow: 0 4px 8px rgba(64, 158, 255, 0.4);
          }
        }
      }

      .header-right {
        display: flex;
        gap: 12px;
      }
    }

    .main-content {
      .overview-section {
        margin-bottom: 20px;

        .overview-card {
          border-radius: 8px;
          overflow: hidden;
          transition: all 0.3s;
          border: none;
          box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);

          &:hover {
            transform: translateY(-4px);
            box-shadow: 0 6px 16px rgba(0, 0, 0, 0.15);
          }

          ::v-deep .el-card__body {
            padding: 25px;
            display: flex;
            align-items: center;
            gap: 20px;
          }

          .card-icon {
            width: 70px;
            height: 70px;
            border-radius: 12px;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 32px;
            color: #fff;
            flex-shrink: 0;
          }

          .card-content {
            flex: 1;
            text-align: left;

            .card-label {
              font-size: 14px;
              color: #909399;
              margin-bottom: 8px;
            }

            .card-value {
              font-size: 32px;
              font-weight: bold;
              color: #303133;
              margin-bottom: 6px;
            }

            .card-desc {
              font-size: 12px;
              color: #c0c4cc;
            }
          }

          &.card-blue .card-icon {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
          }

          &.card-green .card-icon {
            background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
          }

          &.card-orange .card-icon {
            background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
          }

          &.card-red .card-icon {
            background: linear-gradient(135deg, #fa709a 0%, #fee140 100%);
          }
        }
      }

      .function-usage-section {
        margin-bottom: 20px;

        ::v-deep .el-card {
          border-radius: 8px;
          box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
        }

        .card-title {
          display: flex;
          align-items: center;
          gap: 8px;
          font-size: 16px;
          font-weight: 600;
          color: #303133;

          i {
            font-size: 20px;
            color: #409eff;
          }
        }

        .usage-item {
          display: flex;
          align-items: center;
          gap: 15px;
          padding: 20px;
          background: #f5f7fa;
          border-radius: 8px;
          transition: all 0.3s;

          &:hover {
            background: #ecf5ff;
            transform: translateY(-2px);
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
          }

          .usage-icon {
            width: 60px;
            height: 60px;
            border-radius: 12px;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 28px;
            color: #fff;
            flex-shrink: 0;
          }

          .usage-content {
            flex: 1;
            text-align: left;

            .usage-label {
              font-size: 14px;
              color: #606266;
              margin-bottom: 8px;
              font-weight: 500;
            }

            .usage-value {
              font-size: 28px;
              font-weight: bold;
              color: #303133;
              margin-bottom: 4px;
            }

            .usage-desc {
              font-size: 12px;
              color: #909399;
            }
          }
        }
      }

      .charts-section,
      .user-charts-section {
        margin-bottom: 20px;

        ::v-deep .el-card {
          border-radius: 8px;
          box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
          transition: all 0.3s;

          &:hover {
            box-shadow: 0 4px 16px rgba(0, 0, 0, 0.15);
          }
        }

        .card-title {
          display: flex;
          align-items: center;
          gap: 8px;
          font-size: 16px;
          font-weight: 600;
          color: #303133;

          i {
            font-size: 20px;
            color: #409eff;
          }
        }
      }
    }
  }

  /* 弹窗样式 */
  ::v-deep .screen-dialog {
    background: transparent;
    margin: 0 !important;
    padding: 0 !important;

    .el-dialog__header {
      display: none !important;
    }

    .el-dialog__body {
      padding: 0 !important;
      height: 100vh !important;
      overflow: hidden !important;
    }
  }
</style>
