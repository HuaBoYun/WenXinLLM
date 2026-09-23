<template>
  <div class="audit-screen-display">
    <!-- 头部 -->
    <div class="screen-header">
      <div class="header-left">
        <div class="current-time">{{ currentDate }} {{ currentTime }}</div>
      </div>
      <div class="header-title">
        <div class="title-main">系统画像</div>
      </div>
      <div class="header-right">
        <el-button
          class="close-btn"
          icon="el-icon-close"
          size="small"
          @click="closeScreen"
        >
          关闭
        </el-button>
      </div>
    </div>

    <!-- 主体内容 -->
    <div class="screen-body">
      <!-- 上半部分 (3个组件均分高度) -->
      <div class="top-section">
        <!-- 左上数据增长趋势图表 -->
        <div class="panel-box top-left">
          <div class="panel-title">
            <i class="el-icon-data-line"></i>
            <span>数据增长趋势</span>
          </div>
          <div class="chart-container">
            <div ref="chart1" class="chart"></div>
          </div>
        </div>

        <!-- 右侧列 (2个图表垂直排列) -->
        <div class="top-right-column">
          <!-- 右上组件 -->
          <div class="panel-box top-right">
            <div class="panel-title">
              <i class="el-icon-pie-chart"></i>
              <span>数据总量</span>
            </div>
            <div class="chart-container">
              <div ref="chart2" class="chart"></div>
            </div>
          </div>

          <!-- 右中组件 -->
          <div class="panel-box middle-right">
            <div class="panel-title">
              <i class="el-icon-data-line"></i>
              <span>核心功能使用率</span>
            </div>
            <div class="chart-container">
              <div ref="chart3" class="chart"></div>
            </div>
          </div>
        </div>
      </div>

      <!-- 下半部分 (3个组件均分宽度) -->
      <div class="bottom-section">
        <!-- 左下组件 -->
        <div class="panel-box bottom-left">
          <div class="panel-title">
            <i class="el-icon-s-data"></i>
            <span>业务活跃度排名</span>
          </div>
          <div class="chart-container">
            <div ref="chart4" class="chart"></div>
          </div>
        </div>

        <!-- 中下组件 -->
        <div class="panel-box bottom-center">
          <div class="panel-title">
            <i class="el-icon-pie-chart"></i>
            <span>新增用户数</span>
          </div>
          <div class="chart-container">
            <div ref="chart5" class="chart"></div>
          </div>
        </div>

        <!-- 右下组件 -->
        <div class="panel-box bottom-right">
          <div class="panel-title">
            <i class="el-icon-s-data"></i>
            <span>用户活跃度</span>
          </div>
          <div class="chart-container">
            <div ref="chart6" class="chart"></div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
  import * as echarts from 'echarts'
  import { getDataGrowthTrend } from '@/api/setting/dataGrowthTrend'
  import { getDataTotal } from '@/api/setting/dataTotal'
  import { getCoreFunctionUsage } from '@/api/setting/coreFunctionUsage'
  import { getBusinessActivityRanking } from '@/api/setting/businessActivityRanking'
  import { getNewUserCount } from '@/api/setting/newUserCount'
  import { getUserActivity } from '@/api/setting/userActivity'

  export default {
    name: 'SystemPortraitScreen',
    props: {
      year: {
        type: [String, Number],
        default: new Date().getFullYear(),
      },
    },
    data() {
      return {
        currentDate: '',
        currentTime: '',
        timer: null,

        // 年份选择
        selectedYear: this.year || new Date().getFullYear(),
        yearOptions: [],

        // 数据增长趋势数据
        growthTrendData: [],

        // 数据总量
        dataTotal: null,

        // 核心功能使用率
        coreFunctionUsage: null,

        // 业务活跃度排名
        businessActivityRanking: null,

        // 新增用户数
        newUserCount: null,

        // 用户活跃度
        userActivity: null,

        // 其他部分数据 - 留空,等待新接口
        list2Data: [],
        list3Data: [],
        list4Data: { categories: [], series: [] },
        list5Data: { categories: [], series: [] },
        list6Data: [],
        list7Data: [],

        // 4个图表实例
        chart1: null,
        chart2: null,
        chart3: null,
        chart4: null,
        chart5: null,
        chart6: null,
      }
    },
    mounted() {
      this.updateTime()
      this.timer = setInterval(this.updateTime, 1000)

      // 初始化年份选项
      this.initYearOptions()

      // 加载数据增长趋势
      this.fetchGrowthTrendData()

      // 加载数据总量
      this.fetchDataTotal()

      // 加载核心功能使用率
      this.fetchCoreFunctionUsage()

      // 加载业务活跃度排名
      this.fetchBusinessActivityRanking()

      // 加载新增用户数
      this.fetchNewUserCount()

      // 加载用户活跃度
      this.fetchUserActivity()

      // 添加窗口resize监听
      window.addEventListener('resize', this.handleResize)
    },
    beforeDestroy() {
      if (this.timer) {
        clearInterval(this.timer)
      }
      // 移除窗口resize监听
      window.removeEventListener('resize', this.handleResize)
      // 销毁图表实例
      if (this.chart1) this.chart1.dispose()
      if (this.chart2) this.chart2.dispose()
      if (this.chart3) this.chart3.dispose()
      if (this.chart4) this.chart4.dispose()
      if (this.chart5) this.chart5.dispose()
      if (this.chart6) this.chart6.dispose()
    },
    methods: {
      updateTime() {
        const now = new Date()
        this.currentDate = now.toLocaleDateString('zh-CN')
        this.currentTime = now.toLocaleTimeString('zh-CN')
      },
      closeScreen() {
        this.$emit('close')
      },

      // 窗口大小改变时重新调整图表
      handleResize() {
        if (this.chart1) {
          this.chart1.resize()
        }
        if (this.chart2) {
          this.chart2.resize()
        }
        if (this.chart3) {
          this.chart3.resize()
        }
        if (this.chart4) {
          this.chart4.resize()
        }
        if (this.chart5) {
          this.chart5.resize()
        }
      },

      // 初始化年份选项（最近10年）
      initYearOptions() {
        const currentYear = new Date().getFullYear()
        this.yearOptions = []
        for (let i = 0; i < 10; i++) {
          this.yearOptions.push(currentYear - i)
        }
      },

      // 年份改变事件
      handleYearChange(year) {
        this.selectedYear = year
        // 重新加载所有数据
        this.fetchGrowthTrendData()
      },

      // 获取数据增长趋势
      async fetchGrowthTrendData() {
        try {
          const response = await getDataGrowthTrend()
          console.log('数据增长趋势响应:', response)
          if (response.code === 1 && response.data) {
            this.growthTrendData = response.data
            // 使用setTimeout确保DOM完全渲染且有尺寸，增加延迟到500ms
            this.$nextTick(() => {
              setTimeout(() => {
                this.initGrowthTrendChart()
              }, 500)
            })
          } else {
            console.error('获取数据增长趋势失败:', response.msg || response.message)
          }
        } catch (error) {
          console.error('获取数据增长趋势异常:', error)
        }
      },

      // 获取数据总量
      async fetchDataTotal() {
        try {
          const response = await getDataTotal()
          console.log('数据总量响应:', response)
          if (response.code === 1 && response.data) {
            this.dataTotal = response.data
            // 使用setTimeout确保DOM完全渲染且有尺寸
            this.$nextTick(() => {
              setTimeout(() => {
                this.initDataTotalChart()
              }, 500)
            })
          } else {
            console.error('获取数据总量失败:', response.msg || response.message)
          }
        } catch (error) {
          console.error('获取数据总量异常:', error)
        }
      },

      // 初始化数据总量柱状图
      initDataTotalChart() {
        console.log('初始化数据总量图表, chart2 ref:', this.$refs.chart2)
        console.log('数据总量数据:', this.dataTotal)

        if (!this.$refs.chart2) {
          console.error('chart2 ref 不存在')
          return
        }

        // 处理数据
        const record = this.dataTotal || {}
        const chartData = {
          categories: ['风险穿透', '智慧审计', '内控管理', '整改追责'],
          series: [
            {
              name: '数据总量',
              data: [
                Number(record.fx || 0),
                Number(record.ck || 0),
                Number(record.nk || 0),
                Number(record.zg || 0),
              ],
            },
          ],
        }

        console.log('处理后的图表数据:', chartData)

        // 获取容器并设置高度
        const chartDom = this.$refs.chart2
        const panelBox = chartDom.parentElement.parentElement
        const panelHeight = panelBox.clientHeight
        let chartHeight = panelHeight - 50

        if (chartHeight < 200) {
          chartHeight = 300
        }

        chartDom.style.height = chartHeight + 'px'
        console.log('设置数据总量图表高度为:', chartHeight, 'px')

        if (this.chart2) {
          this.chart2.dispose()
        }

        this.chart2 = echarts.init(chartDom)

        const option = {
          tooltip: {
            trigger: 'axis',
            backgroundColor: 'rgba(10, 14, 39, 0.9)',
            borderColor: '#409eff',
            borderWidth: 1,
            textStyle: {
              color: '#fff',
            },
            axisPointer: {
              type: 'shadow',
            },
          },
          grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            top: '15%',
            containLabel: true,
          },
          xAxis: {
            type: 'category',
            data: chartData.categories,
            axisLabel: {
              color: 'rgba(255, 255, 255, 0.7)',
              fontSize: 11,
              interval: 0,
              rotate: 0,
            },
            axisLine: {
              lineStyle: {
                color: 'rgba(64, 158, 255, 0.3)',
              },
            },
          },
          yAxis: {
            type: 'value',
            axisLabel: {
              color: 'rgba(255, 255, 255, 0.7)',
            },
            axisLine: {
              lineStyle: {
                color: 'rgba(64, 158, 255, 0.3)',
              },
            },
            splitLine: {
              lineStyle: {
                color: 'rgba(64, 158, 255, 0.1)',
              },
            },
          },
          series: [
            {
              name: chartData.series[0].name,
              type: 'bar',
              data: chartData.series[0].data,
              barWidth: '40%',
              itemStyle: {
                color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                  { offset: 0, color: '#409eff' },
                  { offset: 1, color: 'rgba(64, 158, 255, 0.3)' },
                ]),
                borderRadius: [4, 4, 0, 0],
              },
              label: {
                show: true,
                position: 'top',
                color: '#409eff',
                fontSize: 12,
              },
            },
          ],
        }

        this.chart2.setOption(option)
      },

      // 获取核心功能使用率
      async fetchCoreFunctionUsage() {
        try {
          const response = await getCoreFunctionUsage()
          console.log('核心功能使用率响应:', response)
          if (response.code === 1 && response.data) {
            this.coreFunctionUsage = response.data
            // 使用setTimeout确保DOM完全渲染且有尺寸
            this.$nextTick(() => {
              setTimeout(() => {
                this.initCoreFunctionUsageChart()
              }, 500)
            })
          } else {
            console.error('获取核心功能使用率失败:', response.msg || response.message)
          }
        } catch (error) {
          console.error('获取核心功能使用率异常:', error)
        }
      },

      // 初始化核心功能使用率饼图
      initCoreFunctionUsageChart() {
        console.log('初始化核心功能使用率图表, chart3 ref:', this.$refs.chart3)
        console.log('核心功能使用率数据:', this.coreFunctionUsage)

        if (!this.$refs.chart3) {
          console.error('chart3 ref 不存在')
          return
        }

        // 处理数据
        const firstItem = this.coreFunctionUsage || {}
        const chartData = [
          {
            name: '风险模块',
            value: Number(firstItem.riskCount) || 0,
          },
          {
            name: '内控模块',
            value: Number(firstItem.controlCount) || 0,
          },
          {
            name: '审计模块',
            value: Number(firstItem.auditCount) || 0,
          },
          {
            name: '整改模块',
            value: Number(firstItem.rectifyCount) || 0,
          },
        ]

        console.log('处理后的饼图数据:', chartData)

        // 获取容器并设置高度
        const chartDom = this.$refs.chart3
        const panelBox = chartDom.parentElement.parentElement
        const panelHeight = panelBox.clientHeight
        let chartHeight = panelHeight - 50

        if (chartHeight < 200) {
          chartHeight = 300
        }

        chartDom.style.height = chartHeight + 'px'
        console.log('设置核心功能使用率图表高度为:', chartHeight, 'px')

        if (this.chart3) {
          this.chart3.dispose()
        }

        this.chart3 = echarts.init(chartDom)

        const option = {
          tooltip: {
            trigger: 'item',
            backgroundColor: 'rgba(10, 14, 39, 0.9)',
            borderColor: '#409eff',
            borderWidth: 1,
            textStyle: {
              color: '#fff',
            },
            formatter: '{b}: {c} ({d}%)',
          },
          legend: {
            orient: 'vertical',
            right: '10%',
            top: 'center',
            textStyle: {
              color: 'rgba(255, 255, 255, 0.7)',
              fontSize: 12,
            },
          },
          series: [
            {
              name: '核心功能使用率',
              type: 'pie',
              radius: ['40%', '70%'],
              center: ['35%', '50%'],
              avoidLabelOverlap: false,
              itemStyle: {
                borderRadius: 10,
                borderColor: '#0a0e27',
                borderWidth: 2,
              },
              label: {
                show: false,
                position: 'center',
              },
              emphasis: {
                label: {
                  show: true,
                  fontSize: '20',
                  fontWeight: 'bold',
                  color: '#409eff',
                },
              },
              labelLine: {
                show: false,
              },
              data: chartData,
            },
          ],
          color: ['#409eff', '#67c23a', '#e6a23c', '#f56c6c'],
        }

        this.chart3.setOption(option)
      },

      // 获取业务活跃度排名
      async fetchBusinessActivityRanking() {
        try {
          const response = await getBusinessActivityRanking()
          console.log('业务活跃度排名响应:', response)
          if (response.code === 1 && response.data) {
            this.businessActivityRanking = response.data
            // 使用setTimeout确保DOM完全渲染且有尺寸
            this.$nextTick(() => {
              setTimeout(() => {
                this.initBusinessActivityRankingChart()
              }, 500)
            })
          } else {
            console.error('获取业务活跃度排名失败:', response.msg || response.message)
          }
        } catch (error) {
          console.error('获取业务活跃度排名异常:', error)
        }
      },

      // 初始化业务活跃度排名柱状图
      initBusinessActivityRankingChart() {
        console.log('初始化业务活跃度排名图表, chart4 ref:', this.$refs.chart4)
        console.log('业务活跃度排名数据:', this.businessActivityRanking)

        if (!this.$refs.chart4) {
          console.error('chart4 ref 不存在')
          return
        }

        // 处理数据
        const categories = this.businessActivityRanking.map((item) => item.b || '')
        const seriesData = this.businessActivityRanking.map((item) => Number(item.c || 0))

        const chartData = {
          categories: categories,
          series: [
            {
              name: '数据总量',
              data: seriesData,
            },
          ],
        }

        console.log('处理后的图表数据:', chartData)

        // 获取容器并设置高度
        const chartDom = this.$refs.chart4
        const panelBox = chartDom.parentElement.parentElement
        const panelHeight = panelBox.clientHeight
        let chartHeight = panelHeight - 50

        if (chartHeight < 200) {
          chartHeight = 300
        }

        chartDom.style.height = chartHeight + 'px'
        console.log('设置业务活跃度排名图表高度为:', chartHeight, 'px')

        if (this.chart4) {
          this.chart4.dispose()
        }

        this.chart4 = echarts.init(chartDom)

        const option = {
          tooltip: {
            trigger: 'axis',
            backgroundColor: 'rgba(10, 14, 39, 0.9)',
            borderColor: '#409eff',
            borderWidth: 1,
            textStyle: {
              color: '#fff',
            },
            axisPointer: {
              type: 'shadow',
            },
          },
          grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            top: '15%',
            containLabel: true,
          },
          xAxis: {
            type: 'category',
            data: chartData.categories,
            axisLabel: {
              color: 'rgba(255, 255, 255, 0.7)',
              fontSize: 11,
              interval: 0,
              rotate: 45,
            },
            axisLine: {
              lineStyle: {
                color: 'rgba(64, 158, 255, 0.3)',
              },
            },
          },
          yAxis: {
            type: 'value',
            axisLabel: {
              color: 'rgba(255, 255, 255, 0.7)',
            },
            axisLine: {
              lineStyle: {
                color: 'rgba(64, 158, 255, 0.3)',
              },
            },
            splitLine: {
              lineStyle: {
                color: 'rgba(64, 158, 255, 0.1)',
              },
            },
          },
          series: [
            {
              name: chartData.series[0].name,
              type: 'bar',
              data: chartData.series[0].data,
              barWidth: '40%',
              itemStyle: {
                color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                  { offset: 0, color: '#67c23a' },
                  { offset: 1, color: 'rgba(103, 194, 58, 0.3)' },
                ]),
                borderRadius: [4, 4, 0, 0],
              },
              label: {
                show: true,
                position: 'top',
                color: '#67c23a',
                fontSize: 12,
              },
            },
          ],
        }

        this.chart4.setOption(option)
      },

      // 获取新增用户数
      async fetchNewUserCount() {
        try {
          const response = await getNewUserCount()
          console.log('新增用户数响应:', response)
          if (response.code === 1 && response.data) {
            this.newUserCount = response.data
            // 使用setTimeout确保DOM完全渲染且有尺寸
            this.$nextTick(() => {
              setTimeout(() => {
                this.initNewUserCountChart()
              }, 500)
            })
          } else {
            console.error('获取新增用户数失败:', response.msg || response.message)
          }
        } catch (error) {
          console.error('获取新增用户数异常:', error)
        }
      },

      // 初始化新增用户数柱状图
      initNewUserCountChart() {
        console.log('初始化新增用户数图表, chart5 ref:', this.$refs.chart5)
        console.log('新增用户数数据:', this.newUserCount)

        if (!this.$refs.chart5) {
          console.error('chart5 ref 不存在')
          return
        }

        // 处理数据
        const categories = this.newUserCount.map((item) => item.m || '')
        const seriesData = this.newUserCount.map((item) => Number(item.c || 0))

        const chartData = {
          categories: categories,
          series: [
            {
              name: '数据总量',
              data: seriesData,
            },
          ],
        }

        console.log('处理后的图表数据:', chartData)

        // 获取容器并设置高度
        const chartDom = this.$refs.chart5
        const panelBox = chartDom.parentElement.parentElement
        const panelHeight = panelBox.clientHeight
        let chartHeight = panelHeight - 50

        if (chartHeight < 200) {
          chartHeight = 300
        }

        chartDom.style.height = chartHeight + 'px'
        console.log('设置新增用户数图表高度为:', chartHeight, 'px')

        if (this.chart5) {
          this.chart5.dispose()
        }

        this.chart5 = echarts.init(chartDom)

        const option = {
          tooltip: {
            trigger: 'axis',
            backgroundColor: 'rgba(10, 14, 39, 0.9)',
            borderColor: '#409eff',
            borderWidth: 1,
            textStyle: {
              color: '#fff',
            },
            axisPointer: {
              type: 'shadow',
            },
          },
          grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            top: '15%',
            containLabel: true,
          },
          xAxis: {
            type: 'category',
            data: chartData.categories,
            axisLabel: {
              color: 'rgba(255, 255, 255, 0.7)',
              fontSize: 11,
              interval: 0,
              rotate: 45,
            },
            axisLine: {
              lineStyle: {
                color: 'rgba(64, 158, 255, 0.3)',
              },
            },
          },
          yAxis: {
            type: 'value',
            axisLabel: {
              color: 'rgba(255, 255, 255, 0.7)',
            },
            axisLine: {
              lineStyle: {
                color: 'rgba(64, 158, 255, 0.3)',
              },
            },
            splitLine: {
              lineStyle: {
                color: 'rgba(64, 158, 255, 0.1)',
              },
            },
          },
          series: [
            {
              name: chartData.series[0].name,
              type: 'bar',
              data: chartData.series[0].data,
              barWidth: '40%',
              itemStyle: {
                color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                  { offset: 0, color: '#e6a23c' },
                  { offset: 1, color: 'rgba(230, 162, 60, 0.3)' },
                ]),
                borderRadius: [4, 4, 0, 0],
              },
              label: {
                show: true,
                position: 'top',
                color: '#e6a23c',
                fontSize: 12,
              },
            },
          ],
        }

        this.chart5.setOption(option)
      },

      // 获取用户活跃度
      async fetchUserActivity() {
        try {
          const response = await getUserActivity()
          console.log('用户活跃度响应:', response)
          if (response.code === 1 && response.data) {
            this.userActivity = response.data
            // 使用setTimeout确保DOM完全渲染且有尺寸
            this.$nextTick(() => {
              setTimeout(() => {
                this.initUserActivityChart()
              }, 500)
            })
          } else {
            console.error('获取用户活跃度失败:', response.msg || response.message)
          }
        } catch (error) {
          console.error('获取用户活跃度异常:', error)
        }
      },

      // 初始化用户活跃度折线图
      initUserActivityChart() {
        console.log('初始化用户活跃度图表, chart6 ref:', this.$refs.chart6)
        console.log('用户活跃度数据:', this.userActivity)

        if (!this.$refs.chart6) {
          console.error('chart6 ref 不存在')
          return
        }

        // 处理数据
        const categories = this.userActivity.map((item) => item.yearMonth || '')
        const seriesData = this.userActivity.map((item) => Number(item.c || 0))

        const chartData = {
          categories: categories,
          series: [
            {
              name: '用户活跃数',
              data: seriesData,
            },
          ],
        }

        console.log('处理后的图表数据:', chartData)

        // 获取容器并设置高度
        const chartDom = this.$refs.chart6
        const panelBox = chartDom.parentElement.parentElement
        const panelHeight = panelBox.clientHeight
        let chartHeight = panelHeight - 50

        if (chartHeight < 200) {
          chartHeight = 300
        }

        chartDom.style.height = chartHeight + 'px'
        console.log('设置用户活跃度图表高度为:', chartHeight, 'px')

        if (this.chart6) {
          this.chart6.dispose()
        }

        this.chart6 = echarts.init(chartDom)

        const option = {
          tooltip: {
            trigger: 'axis',
            backgroundColor: 'rgba(10, 14, 39, 0.9)',
            borderColor: '#f56c6c',
            borderWidth: 1,
            textStyle: {
              color: '#fff',
            },
            axisPointer: {
              type: 'line',
              lineStyle: {
                color: '#f56c6c',
                type: 'dashed',
              },
            },
          },
          grid: {
            left: '3%',
            right: '4%',
            bottom: '15%',
            top: '15%',
            containLabel: true,
          },
          xAxis: {
            type: 'category',
            data: chartData.categories,
            axisLabel: {
              color: 'rgba(255, 255, 255, 0.7)',
              fontSize: 11,
              interval: 0,
              rotate: 45,
            },
            axisLine: {
              lineStyle: {
                color: 'rgba(245, 108, 108, 0.3)',
              },
            },
          },
          yAxis: {
            type: 'value',
            axisLabel: {
              color: 'rgba(255, 255, 255, 0.7)',
            },
            axisLine: {
              lineStyle: {
                color: 'rgba(245, 108, 108, 0.3)',
              },
            },
            splitLine: {
              lineStyle: {
                color: 'rgba(245, 108, 108, 0.1)',
              },
            },
          },
          series: [
            {
              name: chartData.series[0].name,
              type: 'line',
              data: chartData.series[0].data,
              smooth: true,
              symbol: 'circle',
              symbolSize: 6,
              lineStyle: {
                color: '#f56c6c',
                width: 2,
              },
              itemStyle: {
                color: '#f56c6c',
                borderColor: '#fff',
                borderWidth: 2,
              },
              areaStyle: {
                color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                  { offset: 0, color: 'rgba(245, 108, 108, 0.5)' },
                  { offset: 1, color: 'rgba(245, 108, 108, 0.05)' },
                ]),
              },
            },
          ],
        }

        this.chart6.setOption(option)
      },

      // 初始化数据增长趋势图表
      initGrowthTrendChart() {
        console.log('初始化图表, chart1 ref:', this.$refs.chart1)
        console.log('增长趋势数据:', this.growthTrendData)

        if (!this.$refs.chart1) {
          console.error('chart1 ref 不存在')
          return
        }

        // 检查容器尺寸并设置明确的高度
        const chartDom = this.$refs.chart1
        const width = chartDom.clientWidth
        const height = chartDom.clientHeight
        console.log('容器尺寸:', { width, height })

        // 获取.panel-box.top-left（整个面板）的高度
        // chart-container -> panel-box.top-left
        const panelBox = chartDom.parentElement.parentElement
        const panelHeight = panelBox.clientHeight
        console.log('panel-box.top-left高度:', panelHeight)

        // 动态设置图表高度为面板高度减去50px
        let chartHeight = panelHeight - 50

        // 如果计算出的高度小于300px，使用默认高度600px
        if (chartHeight < 300) {
          chartHeight = 600
          console.log('面板高度不足，使用默认高度600px')
        } else {
          console.log('设置图表高度为:', chartHeight, 'px (面板高度 - 50)')
        }

        chartDom.style.height = chartHeight + 'px'

        if (this.chart1) {
          this.chart1.dispose()
        }

        this.chart1 = echarts.init(chartDom)

        const xData = this.growthTrendData.map((item) => item.yearMonth)
        const nkData = this.growthTrendData.map((item) => item.nk)
        const fxData = this.growthTrendData.map((item) => item.fx)
        const ckData = this.growthTrendData.map((item) => item.ck)
        const zgData = this.growthTrendData.map((item) => item.zg)

        console.log('xData:', xData)
        console.log('nkData:', nkData)
        console.log('fxData:', fxData)

        const option = {
          tooltip: {
            trigger: 'axis',
            backgroundColor: 'rgba(10, 14, 39, 0.9)',
            borderColor: '#409eff',
            borderWidth: 1,
            textStyle: {
              color: '#fff',
            },
          },
          legend: {
            data: ['内控管理', '风险穿透', '智慧审计', '整改追责'],
            textStyle: {
              color: '#409eff',
            },
            top: 10,
          },
          grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true,
          },
          xAxis: {
            type: 'category',
            boundaryGap: false,
            data: xData,
            axisLabel: {
              color: 'rgba(255, 255, 255, 0.7)',
              fontSize: 11,
            },
            axisLine: {
              lineStyle: {
                color: 'rgba(64, 158, 255, 0.3)',
              },
            },
          },
          yAxis: {
            type: 'value',
            axisLabel: {
              color: 'rgba(255, 255, 255, 0.7)',
            },
            axisLine: {
              lineStyle: {
                color: 'rgba(64, 158, 255, 0.3)',
              },
            },
            splitLine: {
              lineStyle: {
                color: 'rgba(64, 158, 255, 0.1)',
              },
            },
          },
          series: [
            {
              name: '内控管理',
              type: 'line',
              data: nkData,
              smooth: true,
              lineStyle: {
                color: '#409eff',
                width: 2,
              },
              itemStyle: {
                color: '#409eff',
              },
              areaStyle: {
                color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                  { offset: 0, color: 'rgba(64, 158, 255, 0.3)' },
                  { offset: 1, color: 'rgba(64, 158, 255, 0.05)' },
                ]),
              },
            },
            {
              name: '风险穿透',
              type: 'line',
              data: fxData,
              smooth: true,
              lineStyle: {
                color: '#67c23a',
                width: 2,
              },
              itemStyle: {
                color: '#67c23a',
              },
              areaStyle: {
                color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                  { offset: 0, color: 'rgba(103, 194, 58, 0.3)' },
                  { offset: 1, color: 'rgba(103, 194, 58, 0.05)' },
                ]),
              },
            },
            {
              name: '智慧审计',
              type: 'line',
              data: ckData,
              smooth: true,
              lineStyle: {
                color: '#e6a23c',
                width: 2,
              },
              itemStyle: {
                color: '#e6a23c',
              },
              areaStyle: {
                color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                  { offset: 0, color: 'rgba(230, 162, 60, 0.3)' },
                  { offset: 1, color: 'rgba(230, 162, 60, 0.05)' },
                ]),
              },
            },
            {
              name: '整改追责',
              type: 'line',
              data: zgData,
              smooth: true,
              lineStyle: {
                color: '#f56c6c',
                width: 2,
              },
              itemStyle: {
                color: '#f56c6c',
              },
              areaStyle: {
                color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                  { offset: 0, color: 'rgba(245, 108, 108, 0.3)' },
                  { offset: 1, color: 'rgba(245, 108, 108, 0.05)' },
                ]),
              },
            },
          ],
        }

        this.chart1.setOption(option)
      },
    },
  }
</script>

<style lang="scss" scoped>
  .audit-screen-display {
    width: 100vw;
    height: 100vh;
    background: linear-gradient(180deg, #0a0e27 0%, #1a1f3a 100%);
    color: #fff;
    display: flex;
    flex-direction: column;
    overflow: hidden;
    position: relative;

    // 添加背景装饰
    &::before {
      content: '';
      position: absolute;
      top: 0;
      left: 0;
      right: 0;
      bottom: 0;
      background-image: radial-gradient(
          circle at 20% 50%,
          rgba(64, 158, 255, 0.1) 0%,
          transparent 50%
        ),
        radial-gradient(
          circle at 80% 80%,
          rgba(103, 194, 58, 0.1) 0%,
          transparent 50%
        );
      pointer-events: none;
    }

    // 大屏头部
    .screen-header {
      height: 80px;
      display: flex;
      align-items: center;
      justify-content: space-between;
      padding: 0 40px;
      background: rgba(10, 14, 39, 0.8);
      border-bottom: 2px solid rgba(64, 158, 255, 0.3);
      box-shadow: 0 4px 20px rgba(0, 0, 0, 0.3);
      position: relative;
      z-index: 10;

      .header-left,
      .header-right {
        flex: 1;
      }

      .header-left {
        display: flex;
        align-items: center;
        gap: 20px;

        .current-time {
          font-size: 16px;
          color: #409eff;
          font-family: 'Courier New', monospace;
          font-weight: 500;
          letter-spacing: 1px;
        }

        .year-selector {
          ::v-deep .el-select {
            width: 120px;
          }

          ::v-deep .el-input__inner {
            background: rgba(26, 31, 58, 0.8);
            border-color: rgba(64, 158, 255, 0.3);
            color: #409eff;
            font-weight: 500;

            &:hover {
              border-color: rgba(64, 158, 255, 0.6);
            }

            &:focus {
              border-color: #409eff;
            }
          }

          ::v-deep .el-input__suffix {
            .el-select__caret {
              color: #409eff;
            }
          }
        }
      }

      .header-title {
        flex: 2;
        text-align: center;

        .title-main {
          font-size: 36px;
          font-weight: bold;
          background: linear-gradient(90deg, #409eff 0%, #67c23a 100%);
          -webkit-background-clip: text;
          -webkit-text-fill-color: transparent;
          background-clip: text;
          letter-spacing: 3px;
        }
      }

      .header-right {
        display: flex;
        justify-content: flex-end;

        .close-btn {
          background: rgba(245, 108, 108, 0.2);
          border-color: #f56c6c;
          color: #f56c6c;
          transition: all 0.3s;

          &:hover {
            background: #f56c6c;
            color: #fff;
            transform: scale(1.1);
            box-shadow: 0 0 10px rgba(245, 108, 108, 0.6);
          }
        }
      }
    }

    // 大屏主体 - 根据图片的布局结构
    .screen-body {
      flex: 1;
      display: flex;
      flex-direction: column; // 纵向布局
      padding: 20px;
      gap: 15px;
      overflow: hidden;
      position: relative;
      z-index: 1;

      // 上半部分: 左侧大表格 + 右侧2个图表
      .top-section {
        flex: 2; // 占2/3高度
        display: flex;
        gap: 15px;
        min-height: 0;

        .top-left {
          flex: 3; // 左侧大表格占3份宽度
          min-height: 0;
        }

        .top-right-column {
          flex: 1; // 右侧占1份宽度
          display: flex;
          flex-direction: column;
          gap: 15px;
          min-height: 0;

          .top-right,
          .middle-right {
            flex: 1; // 上下均分高度
            min-height: 0;
          }
        }
      }

      // 下半部分: 3个组件均分宽度
      .bottom-section {
        flex: 1; // 占1/3高度
        display: flex;
        gap: 15px;
        min-height: 0;

        .bottom-left,
        .bottom-center,
        .bottom-right {
          flex: 1; // 3个组件均分宽度
          min-height: 0;
        }
      }

      // 面板盒子通用样式
      .panel-box {
        background: rgba(26, 31, 58, 0.6);
        border: 1px solid rgba(64, 158, 255, 0.2);
        border-radius: 8px;
        padding: 15px;
        backdrop-filter: blur(10px);
        box-shadow: 0 4px 15px rgba(0, 0, 0, 0.3);
        display: flex;
        flex-direction: column;
        overflow: hidden;
        transition: all 0.3s;

        &:hover {
          border-color: rgba(64, 158, 255, 0.5);
          box-shadow: 0 6px 20px rgba(64, 158, 255, 0.2);
        }

        .panel-title {
          display: flex;
          align-items: center;
          gap: 10px;
          font-size: 16px;
          font-weight: bold;
          color: #409eff;
          margin-bottom: 12px;
          padding-bottom: 8px;
          border-bottom: 1px solid rgba(64, 158, 255, 0.2);

          i {
            font-size: 18px;
          }
        }
      }

      // 表格样式
      .data-table {
        flex: 1;
        display: flex;
        flex-direction: column;
        overflow: hidden;

        .table-header,
        .tr {
          display: flex;
          align-items: center;
          padding: 8px 0;
          border-bottom: 1px solid rgba(255, 255, 255, 0.1);
        }

        .table-header {
          font-weight: bold;
          color: #409eff;
          border-bottom: 2px solid rgba(64, 158, 255, 0.3);
          flex-shrink: 0;
        }

        .table-body {
          flex: 1;
          overflow: hidden;
          position: relative;

          .scroll-container {
            position: relative;
            transition: none;
          }

          .tr {
            transition: none;
            min-height: 41px;
            box-sizing: border-box;

            &:hover {
              background: rgba(64, 158, 255, 0.1);
            }
          }
        }

        .th,
        .td {
          padding: 0 8px;
          font-size: 12px;
          text-align: center;
          white-space: nowrap;
          overflow: hidden;
          text-overflow: ellipsis;
        }

        .th {
          color: #409eff;
        }

        .td {
          color: rgba(255, 255, 255, 0.9);
        }
      }

      // 第1部分表格列宽
      .section-1 {
        .th-1,
        .td-1 {
          flex: 0 0 200px;
        }
        .th-2,
        .td-2 {
          flex: 0 0 150px;
        }
        .th-3,
        .td-3 {
          flex: 1;
          min-width: 110px;
        }
        .th-4,
        .td-4 {
          flex: 1;
          min-width: 130px;
        }
        .th-5,
        .td-5 {
          flex: 1;
          min-width: 100px;
        }
        .th-6,
        .td-6 {
          flex: 1;
          min-width: 110px;
        }
        .th-7,
        .td-7 {
          flex: 1;
          min-width: 90px;
        }
        .th-8,
        .td-8 {
          flex: 1;
          min-width: 90px;
        }
        .th-9,
        .td-9 {
          flex: 1;
          min-width: 90px;
        }
      }

      // 第6部分表格列宽
      .bottom-section .panel-box:nth-child(1) {
        .th-1,
        .td-1 {
          flex: 1;
          text-align: left;
        }
        .th-2,
        .td-2 {
          flex: 0 0 130px;
        }
        .th-3,
        .td-3 {
          flex: 0 0 130px;
        }
        .th-4,
        .td-4 {
          flex: 0 0 100px;
        }
      }

      // 图表容器
      .chart-panel {
        .chart-container {
          flex: 1;
          min-height: 0;
          position: relative;
          display: flex;
          flex-direction: column;

          .chart {
            flex: 1;
            width: 100%;
            min-height: 300px;
          }
        }
      }

      // 确保top-left的数据增长趋势图表能自适应高度
      .top-left {
        .chart-container {
          min-height: 0;
        }
      }
    }
  }
</style>
