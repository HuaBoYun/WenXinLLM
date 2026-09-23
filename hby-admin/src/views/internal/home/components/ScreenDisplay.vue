<template>
  <div class="audit-screen-display">
    <!-- 头部 -->
    <div class="screen-header">
      <div class="header-left">
        <div class="current-time">{{ currentDate }} {{ currentTime }}</div>
        <!-- 隐藏年份选择器 -->
        <!-- <div class="year-selector">
          <el-select v-model="selectedYear" placeholder="选择年份" @change="handleYearChange" size="small">
            <el-option
              v-for="year in yearOptions"
              :key="year"
              :label="year + '年'"
              :value="year">
            </el-option>
          </el-select>
        </div> -->
      </div>
      <div class="header-title">
        <div class="title-main">内控自评价缺陷分析报告</div>
      </div>
      <div class="header-right">
        <el-button class="close-btn" size="small" icon="el-icon-close" @click="closeScreen">关闭</el-button>
      </div>
    </div>

    <!-- 主体内容 -->
    <div class="screen-body">
      <!-- 第一行: 左中右三个图表 -->
      <div class="row-1">
        <!-- 第1部分: 各部门评价项目数(左侧柱状图) -->
        <div class="panel-box chart-panel">
          <div class="panel-title">
            <i class="el-icon-s-data"></i>
            <span>各部门评价项目数</span>
          </div>
          <div class="chart-container">
            <div ref="chart1" class="chart"></div>
          </div>
        </div>

        <!-- 第3部分: 控制有效性数据(中间表格) -->
        <div class="panel-box chart-panel">
          <div class="panel-title">
            <i class="el-icon-s-data"></i>
            <span>控制有效性</span>
          </div>
          <div class="data-table">
            <div class="table-header">
              <div class="th th-1">编号</div>
              <div class="th th-2">方案指定部门</div>
              <div class="th th-3">控制目标</div>
              <div class="th th-4">设计有效性</div>
              <div class="th th-5">执行有效性</div>
              <div class="th th-6">测试有效性</div>
            </div>
            <div class="table-body">
              <div class="scroll-container" ref="list3TableBody">
                <div
                  class="tr"
                  v-for="(item, index) in list3Data"
                  :key="'list3-' + index"
                  @mouseenter="onRowEnter"
                  @mouseleave="onRowLeave"
                >
                  <div class="td td-1" @mouseenter="(e) => onCellEnter(e, item.type1)" @mouseleave="onCellLeave">{{ item.type1 }}</div>
                  <div class="td td-2" @mouseenter="(e) => onCellEnter(e, item.type2)" @mouseleave="onCellLeave">{{ item.type2 }}</div>
                  <div class="td td-3" @mouseenter="(e) => onCellEnter(e, item.type3)" @mouseleave="onCellLeave">{{ item.type3 }}</div>
                  <div class="td td-4" @mouseenter="(e) => onCellEnter(e, item.type4)" @mouseleave="onCellLeave">{{ item.type4 }}</div>
                  <div class="td td-5" @mouseenter="(e) => onCellEnter(e, item.type5)" @mouseleave="onCellLeave">{{ item.type5 }}</div>
                  <div class="td td-6" @mouseenter="(e) => onCellEnter(e, item.type6)" @mouseleave="onCellLeave">{{ item.type6 }}</div>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- 第4部分: 各部门评价项目数占比(右侧饼图) -->
        <div class="panel-box chart-panel">
          <div class="panel-title">
            <i class="el-icon-pie-chart"></i>
            <span>各部门评价项目数占比</span>
          </div>
          <div class="chart-container">
            <div ref="chart4" class="chart"></div>
          </div>
        </div>
      </div>

      <!-- 第二行: 左右两个部分，左侧占1/3 -->
      <div class="row-2">
        <!-- 第6部分: 缺陷等级分布(左侧饼图，占1/3) -->
        <div class="panel-box left-panel chart-panel">
          <div class="panel-title">
            <i class="el-icon-pie-chart"></i>
            <span>缺陷等级分布</span>
          </div>
          <div class="chart-container">
            <div ref="chart6" class="chart"></div>
          </div>
        </div>

        <!-- 第1部分: 各单位缺陷数量对比分析(右侧柱状图，占2/3) -->
        <div class="panel-box right-panel chart-panel">
          <div class="panel-title">
            <i class="el-icon-data-analysis"></i>
            <span>各单位缺陷数量对比分析</span>
          </div>
          <div class="chart-container">
            <div ref="chart1Right" class="chart"></div>
          </div>
        </div>
      </div>

      <!-- 第三行: 左右两个部分，右侧占1/3 -->
      <div class="row-3">
        <!-- 第7部分: 本年缺陷项目趋势分析(左侧折线图，占2/3) -->
        <div class="panel-box left-panel chart-panel">
          <div class="panel-title">
            <i class="el-icon-data-line"></i>
            <span>本年缺陷项目趋势分析</span>
          </div>
          <div class="chart-container">
            <div ref="chart7" class="chart"></div>
          </div>
        </div>

        <!-- 第5部分: 缺陷类型分布(右侧饼图，占1/3) -->
        <div class="panel-box right-panel chart-panel">
          <div class="panel-title">
            <i class="el-icon-pie-chart"></i>
            <span>缺陷类型分布</span>
          </div>
          <div class="chart-container">
            <div ref="chart5" class="chart"></div>
          </div>
        </div>
      </div>
    </div>

    <!-- 气泡提示框 -->
    <div
      v-if="tooltip.visible"
      class="cell-bubble"
      :style="{ top: tooltip.top + 'px', left: tooltip.left + 'px' }"
    >
      <div class="bubble-content">{{ tooltip.text }}</div>
      <div class="bubble-arrow" :style="{ left: tooltip.arrowLeft + 'px' }"></div>
    </div>
  </div>
</template>


<script>
import * as echarts from 'echarts'
import { getDepartmentProjectCount, getControlEffectivenessData, getDepartmentProjectRatio, getDefectTypeDistribution, getDepartmentDefectComparison, getDefectTrendAnalysis, getDefectPropertyDistribution } from '@/api/audit/sjfx'

export default {
  name: 'ScreenDisplay',
  props: {
    year: {
      type: [String, Number],
      default: new Date().getFullYear()
    }
  },
  data() {
    return {
      currentDate: '',
      currentTime: '',
      timer: null,

      // 年份选择
      selectedYear: this.year || new Date().getFullYear(),
      yearOptions: [],

      // 数据
      list1Data: [], // 审计项目列表数据
      list1OriginalData: [], // 保存原始数据用于循环
      chart1Data: { categories: [], values: [] }, // 各部门评价项目数
      list3Data: [], // 控制有效性数据
      list3OriginalData: [], // 保存原始数据用于循环
      chart4Data: [], // 各部门评价项目数占比
      list5Data: { categories: [], series: [] },
      chart6Data: [], // 缺陷类型分布（第二行左侧）
      chart1RightData: { categories: [], series: [] }, // 各单位缺陷数量对比分析
      chart7Data: { categories: [], series: [] }, // 本年缺陷项目趋势分析
      chart5Data: [], // 缺陷属性分布（第三行右侧）

      // 图表实例
      chart1: null,
      chart1Right: null,
      chart3: null,
      chart4: null,
      chart5: null,
      chart6: null,
      chart7: null,

      // 滚动相关
      scrollTimer: null,
      scrollTimer3: null,
      scrollTimer6: null,
      scrollTimer7: null,
      scrollSpeed: 50, // 滚动速度（毫秒）

      // 气泡提示
      tooltip: { visible: false, top: 0, left: 0, arrowLeft: 0, text: '' }
    }
  },
  mounted() {
    this.updateTime()
    this.timer = setInterval(this.updateTime, 1000)

    // 初始化年份选项
    this.initYearOptions()

    // 加载所有数据
    this.fetchAllData()
  },
  beforeDestroy() {
    if (this.timer) {
      clearInterval(this.timer)
    }
    if (this.scrollTimer) {
      clearInterval(this.scrollTimer)
    }
    if (this.scrollTimer3) {
      clearInterval(this.scrollTimer3)
    }
    if (this.scrollTimer6) {
      clearInterval(this.scrollTimer6)
    }
    if (this.scrollTimer7) {
      clearInterval(this.scrollTimer7)
    }
    // 销毁图表实例
    if (this.chart1) this.chart1.dispose()
    if (this.chart3) this.chart3.dispose()
    if (this.chart4) this.chart4.dispose()
    if (this.chart5) this.chart5.dispose()
    // 清理气泡事件
    if (this._cellTarget && this._cellMoveHandler) {
      this._cellTarget.removeEventListener('mousemove', this._cellMoveHandler)
    }
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
      // 停止所有滚动
      if (this.scrollTimer) {
        clearInterval(this.scrollTimer)
        this.scrollTimer = null
      }
      if (this.scrollTimer3) {
        clearInterval(this.scrollTimer3)
        this.scrollTimer3 = null
      }
      if (this.scrollTimer6) {
        clearInterval(this.scrollTimer6)
        this.scrollTimer6 = null
      }
      if (this.scrollTimer7) {
        clearInterval(this.scrollTimer7)
        this.scrollTimer7 = null
      }
      // 重新加载所有数据
      this.fetchAllData()
    },

    // 获取所有数据
    async fetchAllData() {
      try {
        // 加载第1部分数据（各部门评价项目数）
        await this.fetchChart1Data()
        // 加载第3部分数据（控制有效性）
        await this.fetchList3Data()
        // 加载第4部分数据（各部门评价项目数占比）
        await this.fetchChart4Data()
        // 加载第6部分数据（缺陷类型分布-第二行左侧）
        await this.fetchChart6Data()
        // 加载第1右侧部分数据（各单位缺陷数量对比分析）
        await this.fetchChart1RightData()
        // 加载第7部分数据（本年缺陷项目趋势分析）
        await this.fetchChart7Data()
        // 加载第5部分数据（缺陷属性分布-第三行右侧）
        await this.fetchChart5Data()
      } catch (error) {
        console.error('获取数据失败:', error)
        this.$message.error('数据加载失败')
      }
    },

    // 注释：内控大屏不再使用审计项目列表接口
    // async fetchList1Data() { ... }
    // processList1Data(data) { ... }

    // 第1部分: 各部门评价项目数统计（新接口）
    async fetchChart1Data() {
      try {
        const res = await getDepartmentProjectCount()
        console.log('各部门评价项目数接口返回:', res)
        if (res.code === 1 && res.data) {
          this.chart1Data = res.data
          console.log('处理后的柱状图数据:', this.chart1Data)
          this.$nextTick(() => {
            console.log('开始初始化图表1，DOM元素:', this.$refs.chart1)
            this.initChart1(this.chart1Data)
          })
        }
      } catch (error) {
        console.error('获取各部门评价项目数统计数据失败:', error)
      }
    },

    initChart1(data) {
      console.log('initChart1 被调用，数据:', data)
      if (!this.$refs.chart1) {
        console.error('chart1 DOM 元素不存在')
        return
      }
      if (this.chart1) {
        this.chart1.dispose()
      }
      this.chart1 = echarts.init(this.$refs.chart1)
      const option = {
        tooltip: {
          trigger: 'axis',
          axisPointer: { type: 'shadow' }
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
          data: data.categories || [],
          axisLine: { lineStyle: { color: 'rgba(255,255,255,0.3)' } },
          axisLabel: {
            color: '#fff',
            fontSize: 11,
            rotate: 30,
            interval: 0
          }
        },
        yAxis: {
          type: 'value',
          axisLine: { lineStyle: { color: 'rgba(255,255,255,0.3)' } },
          axisLabel: { color: '#fff', fontSize: 11 },
          splitLine: { lineStyle: { color: 'rgba(255,255,255,0.1)' } }
        },
        series: [{
          name: '项目数量',
          type: 'bar',
          data: data.values || [],
          barWidth: '60%',
          itemStyle: {
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
              { offset: 0, color: 'rgba(64,158,255,0.8)' },
              { offset: 1, color: 'rgba(64,158,255,0.3)' }
            ])
          },
          label: {
            show: true,
            position: 'top',
            color: '#fff',
            fontSize: 12
          }
        }]
      }
      console.log('设置图表配置:', option)
      this.chart1.setOption(option)
      console.log('图表1初始化完成')
    },

    // 第3部分: 控制有效性数据查询
    async fetchList3Data() {
      try {
        const res = await getControlEffectivenessData()
        console.log('控制有效性数据接口返回:', res)
        if (res.code === 1 && res.data) {
          const dataList = res.data.list || []
          // 保存原始数据
          this.list3OriginalData = [...dataList]
          // 初始显示数据（复制一份用于滚动）
          this.list3Data = [...dataList]

          // 启动自动滚动
          this.$nextTick(() => {
            this.startAutoScroll3()
          })
        }
      } catch (error) {
        console.error('获取控制有效性数据失败:', error)
      }
    },

    // 第4部分: 各部门评价项目数占比统计（饼图）
    async fetchChart4Data() {
      try {
        const res = await getDepartmentProjectRatio()
        console.log('各部门评价项目数占比接口返回:', res)
        if (res.code === 1 && res.data) {
          const dataList = res.data.data || []
          this.chart4Data = dataList

          // 初始化饼图
          this.$nextTick(() => {
            this.initChart4()
          })
        }
      } catch (error) {
        console.error('获取各部门评价项目数占比失败:', error)
      }
    },

    // 初始化第4部分饼图
    initChart4() {
      if (!this.$refs.chart4) {
        console.error('chart4 ref 不存在')
        return
      }

      // 销毁旧实例
      if (this.chart4) {
        this.chart4.dispose()
      }

      // 创建新实例
      this.chart4 = echarts.init(this.$refs.chart4)

      const option = {
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b}: {c} ({d}%)',
          backgroundColor: 'rgba(0, 0, 0, 0.7)',
          borderColor: '#409eff',
          borderWidth: 1,
          textStyle: {
            color: '#fff'
          },
          confine: true
        },
        legend: {
          orient: 'vertical',
          right: '10%',
          top: 'center',
          textStyle: {
            color: '#fff',
            fontSize: 12
          },
          formatter: function(name) {
            return name.length > 8 ? name.substring(0, 8) + '...' : name
          }
        },
        series: [
          {
            name: '项目数',
            type: 'pie',
            radius: ['40%', '70%'],
            center: ['35%', '50%'],
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
                fontWeight: 'bold',
                color: '#fff'
              }
            },
            labelLine: {
              show: false
            },
            data: this.chart4Data
          }
        ]
      }

      this.chart4.setOption(option)
    },

    // 第6部分: 缺陷类型分布统计（饼图）
    async fetchChart6Data() {
      try {
        const res = await getDefectTypeDistribution()
        console.log('缺陷类型分布接口返回:', res)
        if (res.code === 1 && res.data) {
          const dataList = res.data.data || []
          console.log('缺陷类型分布数据:', dataList)
          this.chart6Data = dataList

          // 延迟初始化，确保 DOM 完全渲染
          setTimeout(() => {
            this.initChart6()
          }, 300)
        }
      } catch (error) {
        console.error('获取缺陷类型分布失败:', error)
      }
    },

    // 初始化第6部分饼图
    initChart6() {
      if (!this.$refs.chart6) {
        console.error('chart6 ref 不存在')
        return
      }

      if (!this.chart6Data || this.chart6Data.length === 0) {
        console.warn('chart6Data 为空，无法初始化图表')
        return
      }

      // 检查 DOM 尺寸
      const domWidth = this.$refs.chart6.clientWidth
      const domHeight = this.$refs.chart6.clientHeight
      console.log('chart6 DOM 尺寸:', domWidth, 'x', domHeight)

      if (domWidth === 0 || domHeight === 0) {
        console.warn('chart6 DOM 尺寸为 0，延迟重试')
        setTimeout(() => {
          this.initChart6()
        }, 200)
        return
      }

      console.log('开始初始化 chart6，数据:', this.chart6Data)

      // 销毁旧实例
      if (this.chart6) {
        this.chart6.dispose()
      }

      // 创建新实例
      this.chart6 = echarts.init(this.$refs.chart6)

      const option = {
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b}: {c} ({d}%)',
          backgroundColor: 'rgba(0, 0, 0, 0.7)',
          borderColor: '#409eff',
          borderWidth: 1,
          textStyle: {
            color: '#fff'
          }
        },
        legend: {
          orient: 'vertical',
          right: '10%',
          top: 'center',
          textStyle: {
            color: '#fff',
            fontSize: 12
          },
          formatter: function(name) {
            return name.length > 8 ? name.substring(0, 8) + '...' : name
          }
        },
        series: [
          {
            name: '缺陷数量',
            type: 'pie',
            radius: ['40%', '70%'],
            center: ['35%', '50%'],
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
                fontWeight: 'bold',
                color: '#fff'
              }
            },
            labelLine: {
              show: false
            },
            data: this.chart6Data
          }
        ]
      }

      console.log('chart6 配置:', option)
      this.chart6.setOption(option)
      console.log('chart6 初始化完成')
    },

    // 第1右侧部分: 各单位缺陷数量对比分析（柱状图）
    async fetchChart1RightData() {
      try {
        const res = await getDepartmentDefectComparison()
        console.log('各单位缺陷数量对比分析接口返回:', res)
        if (res.code === 1 && res.data) {
          this.chart1RightData = {
            categories: res.data.categories || [],
            series: res.data.series || []
          }

          // 初始化柱状图
          setTimeout(() => {
            this.initChart1Right()
          }, 300)
        }
      } catch (error) {
        console.error('获取各单位缺陷数量对比分析失败:', error)
      }
    },

    // 初始化第1右侧部分柱状图
    initChart1Right() {
      if (!this.$refs.chart1Right) {
        console.error('chart1Right ref 不存在')
        return
      }

      // 检查 DOM 尺寸
      const domWidth = this.$refs.chart1Right.clientWidth
      const domHeight = this.$refs.chart1Right.clientHeight
      console.log('chart1Right DOM 尺寸:', domWidth, 'x', domHeight)

      if (domWidth === 0 || domHeight === 0) {
        console.warn('chart1Right DOM 尺寸为 0，延迟重试')
        setTimeout(() => {
          this.initChart1Right()
        }, 200)
        return
      }

      console.log('开始初始化 chart1Right，数据:', this.chart1RightData)

      // 销毁旧实例
      if (this.chart1Right) {
        this.chart1Right.dispose()
      }

      // 创建新实例
      this.chart1Right = echarts.init(this.$refs.chart1Right)

      const option = {
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow'
          },
          backgroundColor: 'rgba(0, 0, 0, 0.7)',
          borderColor: '#409eff',
          borderWidth: 1,
          textStyle: {
            color: '#fff'
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
          data: this.chart1RightData.categories,
          axisLabel: {
            color: '#fff',
            fontSize: 12,
            interval: 0,
            rotate: 30
          },
          axisLine: {
            lineStyle: {
              color: 'rgba(255, 255, 255, 0.3)'
            }
          }
        },
        yAxis: {
          type: 'value',
          axisLabel: {
            color: '#fff',
            fontSize: 12
          },
          axisLine: {
            lineStyle: {
              color: 'rgba(255, 255, 255, 0.3)'
            }
          },
          splitLine: {
            lineStyle: {
              color: 'rgba(255, 255, 255, 0.1)'
            }
          }
        },
        series: this.chart1RightData.series.map(item => ({
          name: item.name,
          type: 'bar',
          data: item.data,
          itemStyle: {
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
              { offset: 0, color: '#409eff' },
              { offset: 1, color: '#67c23a' }
            ]),
            borderRadius: [5, 5, 0, 0]
          },
          barWidth: '60%'
        }))
      }

      console.log('chart1Right 配置:', option)
      this.chart1Right.setOption(option)
      console.log('chart1Right 初始化完成')
    },

    // 第7部分: 本年缺陷项目趋势分析（折线图）
    async fetchChart7Data() {
      try {
        const res = await getDefectTrendAnalysis()
        console.log('本年缺陷项目趋势分析接口返回:', res)
        if (res.code === 1 && res.data) {
          this.chart7Data = {
            categories: res.data.categories || [],
            series: res.data.series || []
          }

          // 初始化折线图
          setTimeout(() => {
            this.initChart7()
          }, 300)
        }
      } catch (error) {
        console.error('获取本年缺陷项目趋势分析失败:', error)
      }
    },

    // 初始化第7部分折线图
    initChart7() {
      if (!this.$refs.chart7) {
        console.error('chart7 ref 不存在')
        return
      }

      // 检查 DOM 尺寸
      const domWidth = this.$refs.chart7.clientWidth
      const domHeight = this.$refs.chart7.clientHeight
      console.log('chart7 DOM 尺寸:', domWidth, 'x', domHeight)

      if (domWidth === 0 || domHeight === 0) {
        console.warn('chart7 DOM 尺寸为 0，延迟重试')
        setTimeout(() => {
          this.initChart7()
        }, 200)
        return
      }

      console.log('开始初始化 chart7，数据:', this.chart7Data)

      // 销毁旧实例
      if (this.chart7) {
        this.chart7.dispose()
      }

      // 创建新实例
      this.chart7 = echarts.init(this.$refs.chart7)

      const option = {
        tooltip: {
          trigger: 'axis',
          backgroundColor: 'rgba(0, 0, 0, 0.7)',
          borderColor: '#409eff',
          borderWidth: 1,
          textStyle: {
            color: '#fff'
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
          data: this.chart7Data.categories,
          boundaryGap: false,
          axisLabel: {
            color: '#fff',
            fontSize: 12,
            interval: 0,
            rotate: 30
          },
          axisLine: {
            lineStyle: {
              color: 'rgba(255, 255, 255, 0.3)'
            }
          }
        },
        yAxis: {
          type: 'value',
          axisLabel: {
            color: '#fff',
            fontSize: 12
          },
          axisLine: {
            lineStyle: {
              color: 'rgba(255, 255, 255, 0.3)'
            }
          },
          splitLine: {
            lineStyle: {
              color: 'rgba(255, 255, 255, 0.1)'
            }
          }
        },
        series: this.chart7Data.series.map(item => ({
          name: item.name,
          type: 'line',
          data: item.data,
          smooth: true,
          symbol: 'circle',
          symbolSize: 8,
          lineStyle: {
            width: 3,
            color: new echarts.graphic.LinearGradient(0, 0, 1, 0, [
              { offset: 0, color: '#409eff' },
              { offset: 1, color: '#67c23a' }
            ])
          },
          itemStyle: {
            color: '#409eff',
            borderColor: '#fff',
            borderWidth: 2
          },
          areaStyle: {
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
              { offset: 0, color: 'rgba(64, 158, 255, 0.3)' },
              { offset: 1, color: 'rgba(64, 158, 255, 0.05)' }
            ])
          }
        }))
      }

      console.log('chart7 配置:', option)
      this.chart7.setOption(option)
      console.log('chart7 初始化完成')
    },

    // 第5部分: 缺陷属性分布（饼图）
    async fetchChart5Data() {
      try {
        const res = await getDefectPropertyDistribution()
        console.log('缺陷属性分布接口返回:', res)
        if (res.code === 1 && res.data && res.data.data) {
          this.chart5Data = res.data.data

          // 初始化饼图
          setTimeout(() => {
            this.initChart5()
          }, 300)
        }
      } catch (error) {
        console.error('获取缺陷属性分布失败:', error)
      }
    },

    // 初始化第5部分饼图
    initChart5() {
      if (!this.$refs.chart5) {
        console.error('chart5 ref 不存在')
        return
      }

      // 检查 DOM 尺寸
      const domWidth = this.$refs.chart5.clientWidth
      const domHeight = this.$refs.chart5.clientHeight
      console.log('chart5 DOM 尺寸:', domWidth, 'x', domHeight)

      if (domWidth === 0 || domHeight === 0) {
        console.warn('chart5 DOM 尺寸为 0，延迟重试')
        setTimeout(() => {
          this.initChart5()
        }, 200)
        return
      }

      console.log('开始初始化 chart5，数据:', this.chart5Data)

      // 销毁旧实例
      if (this.chart5) {
        this.chart5.dispose()
      }

      // 创建新实例
      this.chart5 = echarts.init(this.$refs.chart5)

      const option = {
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b}: {c} ({d}%)',
          backgroundColor: 'rgba(0, 0, 0, 0.7)',
          borderColor: '#409eff',
          borderWidth: 1,
          textStyle: {
            color: '#fff'
          }
        },
        legend: {
          orient: 'vertical',
          right: '10%',
          top: 'center',
          textStyle: {
            color: '#fff',
            fontSize: 12
          },
          formatter: (name) => {
            return name.length > 8 ? name.substring(0, 8) + '...' : name
          }
        },
        series: [
          {
            name: '缺陷属性',
            type: 'pie',
            radius: ['40%', '70%'],
            center: ['35%', '50%'],
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
                fontWeight: 'bold',
                color: '#fff'
              }
            },
            labelLine: {
              show: false
            },
            data: this.chart5Data
          }
        ]
      }

      console.log('chart5 配置:', option)
      this.chart5.setOption(option)
      console.log('chart5 初始化完成')
    },

    // 启动自动滚动
    startAutoScroll() {
      // 清除之前的定时器
      if (this.scrollTimer) {
        clearInterval(this.scrollTimer)
      }

      // 如果数据少于等于可见行数，不需要滚动
      if (this.list1Data.length <= 5) {
        return
      }

      // 获取表格容器
      const tableBody = this.$refs.list1TableBody
      if (!tableBody) return

      const rowHeight = 41 // 每行高度（包括padding和border）
      let accumulatedScroll = 0

      this.scrollTimer = setInterval(() => {
        accumulatedScroll += 1

        // 使用CSS transform实现平滑滚动
        tableBody.style.transform = `translateY(-${accumulatedScroll}px)`

        // 当滚动到一行的高度时，将第一行数据移到最后
        if (accumulatedScroll >= rowHeight) {
          // 将第一条数据移到最后
          const firstItem = this.list1Data.shift()
          this.list1Data.push(firstItem)

          // 重置滚动位置
          accumulatedScroll = 0

          // 等待Vue更新DOM后重置transform
          this.$nextTick(() => {
            tableBody.style.transform = 'translateY(0)'
          })
        }
      }, this.scrollSpeed)
    },

    // 停止自动滚动
    stopAutoScroll() {
      if (this.scrollTimer) {
        clearInterval(this.scrollTimer)
        this.scrollTimer = null
      }
    },

    // 启动第3部分自动滚动（控制有效性）
    startAutoScroll3() {
      if (this.scrollTimer3) {
        clearInterval(this.scrollTimer3)
      }

      // 如果数据少于等于可见行数，不需要滚动
      if (this.list3Data.length <= 5) {
        return
      }

      const tableBody = this.$refs.list3TableBody
      if (!tableBody) return

      const rowHeight = 42 // 每行高度（包括padding和border）
      let accumulatedScroll = 0
      this._scrolling3 = false

      this.scrollTimer3 = setInterval(() => {
        // $nextTick 执行期间暂停，防止 transform 累积导致跳动
        if (this._scrolling3) return

        accumulatedScroll += 1
        tableBody.style.transform = `translateY(-${accumulatedScroll}px)`

        if (accumulatedScroll >= rowHeight) {
          this._scrolling3 = true
          const firstItem = this.list3Data.shift()
          this.list3Data.push(firstItem)
          accumulatedScroll = 0

          this.$nextTick(() => {
            tableBody.style.transform = 'translateY(0)'
            this._scrolling3 = false
          })
        }
      }, this.scrollSpeed)
    },

    // 启动第6部分自动滚动
    startAutoScroll6() {
      if (this.scrollTimer6) {
        clearInterval(this.scrollTimer6)
      }

      if (this.list6Data.length <= 3) {
        return
      }

      const tableBody = this.$refs.list6TableBody
      if (!tableBody) return

      const rowHeight = 41
      let accumulatedScroll = 0

      this.scrollTimer6 = setInterval(() => {
        accumulatedScroll += 1
        tableBody.style.transform = `translateY(-${accumulatedScroll}px)`

        if (accumulatedScroll >= rowHeight) {
          const firstItem = this.list6Data.shift()
          this.list6Data.push(firstItem)
          accumulatedScroll = 0

          this.$nextTick(() => {
            tableBody.style.transform = 'translateY(0)'
          })
        }
      }, this.scrollSpeed)
    },

    // 启动第7部分自动滚动
    startAutoScroll7() {
      if (this.scrollTimer7) {
        clearInterval(this.scrollTimer7)
      }

      if (this.list7Data.length <= 3) {
        return
      }

      const tableBody = this.$refs.list7TableBody
      if (!tableBody) return

      const rowHeight = 41
      let accumulatedScroll = 0

      this.scrollTimer7 = setInterval(() => {
        accumulatedScroll += 1
        tableBody.style.transform = `translateY(-${accumulatedScroll}px)`

        if (accumulatedScroll >= rowHeight) {
          const firstItem = this.list7Data.shift()
          this.list7Data.push(firstItem)
          accumulatedScroll = 0

          this.$nextTick(() => {
            tableBody.style.transform = 'translateY(0)'
          })
        }
      }, this.scrollSpeed)
    },

    // 行悬浮：暂停滚动
    onRowEnter() {
      if (this.scrollTimer3) {
        clearInterval(this.scrollTimer3)
        this.scrollTimer3 = null
      }
    },

    // 行离开：恢复滚动 + 隐藏气泡
    onRowLeave() {
      this.tooltip.visible = false
      this.startAutoScroll3()
    },

    // 单元格悬浮：显示气泡并跟随光标
    onCellEnter(e, text) {
      if (text === null || text === undefined || text === '') return
      const content = String(text)
      const bubbleWidth = 220
      const arrowSize = 6
      const edgePadding = 8

      this._cellMoveHandler = (ev) => {
        let left = ev.clientX - bubbleWidth / 4
        if (left < edgePadding) left = edgePadding
        if (left + bubbleWidth > window.innerWidth - edgePadding) {
          left = window.innerWidth - bubbleWidth - edgePadding
        }
        const arrowLeft = Math.min(
          Math.max(ev.clientX - left, arrowSize + 4),
          bubbleWidth - arrowSize - 4
        )
        this.tooltip = { visible: true, top: ev.clientY - 12, left, arrowLeft, text: content }
      }
      this._cellMoveHandler(e)
      e.currentTarget.addEventListener('mousemove', this._cellMoveHandler)
      this._cellTarget = e.currentTarget
    },

    // 单元格离开：隐藏气泡
    onCellLeave() {
      if (this._cellTarget && this._cellMoveHandler) {
        this._cellTarget.removeEventListener('mousemove', this._cellMoveHandler)
      }
      this._cellTarget = null
      this._cellMoveHandler = null
      this.tooltip.visible = false
    }
  }
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
    background-image:
      radial-gradient(circle at 20% 50%, rgba(64, 158, 255, 0.1) 0%, transparent 50%),
      radial-gradient(circle at 80% 80%, rgba(103, 194, 58, 0.1) 0%, transparent 50%);
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

  // 大屏主体
  .screen-body {
    flex: 1;
    display: flex;
    flex-direction: column;
    padding: 20px;
    gap: 15px;
    overflow: hidden;
    position: relative;
    z-index: 1;

    // 第一行: 左中右三个图表
    .row-1 {
      display: flex;
      gap: 15px;
      flex: 0 0 auto;
      height: calc((100vh - 80px - 60px) / 3);
      min-height: 250px;

      // 左侧：各部门评价项目数（缩短1/4）
      .panel-box:nth-child(1) {
        flex: 0.75;
      }

      // 中间：控制有效性（增加宽度）
      .panel-box:nth-child(2) {
        flex: 1.5;
      }

      // 右侧：项目数趋势变化（缩短1/4）
      .panel-box:nth-child(3) {
        flex: 0.75;
      }
    }

    // 第二行: 左右两个部分，左侧占1/3
    .row-2 {
      display: flex;
      gap: 15px;
      flex: 0 0 auto;
      height: calc((100vh - 80px - 60px) / 3);
      min-height: 250px;

      .left-panel {
        flex: 0 0 calc(33.333% - 10px);
      }

      .right-panel {
        flex: 1;
      }
    }

    // 第三行: 左右两个部分，右侧占1/3
    .row-3 {
      display: flex;
      gap: 15px;
      flex: 0 0 auto;
      height: calc((100vh - 80px - 60px) / 3);
      min-height: 250px;

      .left-panel {
        flex: 1;
      }

      .right-panel {
        flex: 0 0 calc(33.333% - 10px);
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
        padding: 10px 5px;
        border-bottom: 1px solid rgba(64, 158, 255, 0.15);
      }

      .table-header {
        font-weight: bold;
        color: #409eff;
        background: rgba(0, 212, 255, 0.1);
        border-bottom: 1px solid rgba(0, 212, 255, 0.3);
        flex-shrink: 0;
        font-size: 13px;
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
          background: rgba(26, 35, 66, 0.5);
          color: #e0e0e0;
          font-size: 12px;
          min-height: 42px;
          box-sizing: border-box;

          &:hover {
            background: rgba(0, 212, 255, 0.1);
          }
        }
      }

      .th,
      .td {
        padding: 0 8px;
        font-size: 12px;
        text-align: left;
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
      }
    }

    // 第一行中间: 控制有效性表格列宽
    .row-1 .panel-box:nth-child(2) {
      .th-1, .td-1 { flex: 0 0 12%; }  // 编号（增加）
      .th-2, .td-2 { flex: 0 0 20%; }  // 方案指定部门（增加）
      .th-3, .td-3 { flex: 0 0 30%; }  // 控制目标（增加）
      .th-4, .td-4 { flex: 0 0 12%; }  // 设计有效性（缩短）
      .th-5, .td-5 { flex: 0 0 13%; }  // 执行有效性（缩短）
      .th-6, .td-6 { flex: 0 0 13%; }  // 测试有效性（缩短）
    }

    // 第二行右侧: 审计项目列表表格列宽
    .row-2 .right-panel {
      .th-1, .td-1 { flex: 0 0 200px; }  // 审计单位（最大）
      .th-2, .td-2 { flex: 0 0 150px; }  // 项目名称
      .th-3, .td-3 { flex: 1; min-width: 110px; }  // 审计开始时间（弹性）
      .th-4, .td-4 { flex: 1; min-width: 130px; }  // 审计类型（弹性）
      .th-5, .td-5 { flex: 1; min-width: 100px; }  // 审计来源（弹性）
      .th-6, .td-6 { flex: 1; min-width: 110px; }  // 审计结束时间（弹性）
      .th-7, .td-7 { flex: 1; min-width: 90px; }  // 未整改数量（弹性）
      .th-8, .td-8 { flex: 1; min-width: 90px; }  // 已整改数量（弹性）
      .th-9, .td-9 { flex: 1; min-width: 90px; }  // 整改总数（弹性）
    }

    // 第二行左侧: 审计项目数一览表列宽
    .row-2 .left-panel {
      .th-1, .td-1 { flex: 1; text-align: left; }  // 被审计单位（最大宽度）
      .th-2, .td-2 { flex: 0 0 130px; }  // 已完成项目数量
      .th-3, .td-3 { flex: 0 0 130px; }  // 未完成项目数量
      .th-4, .td-4 { flex: 0 0 100px; }  // 项目总数
    }

    // 第三行左侧: 整改问题一览表列宽
    .row-3 .left-panel {
      .th-1, .td-1 { flex: 1; text-align: left; }  // 主管部门（最大宽度）
      .th-2, .td-2 { flex: 0 0 100px; }  // 已整改数量
      .th-3, .td-3 { flex: 0 0 100px; }  // 未整改数量
      .th-4, .td-4 { flex: 0 0 100px; }  // 整改总数
      .th-5, .td-5 { flex: 0 0 100px; }  // 已销号数量
      .th-6, .td-6 { flex: 0 0 100px; }  // 未销号数量
    }

    // 图表容器
    .chart-panel {
      .chart-container {
        flex: 1;
        min-height: 0;

        .chart {
          width: 100%;
          height: 100%;
        }
      }
    }
  }
}

// 气泡提示框（fixed 定位，不受 scoped 限制）
.cell-bubble {
  position: fixed;
  z-index: 9999;
  transform: translateY(-100%);
  pointer-events: none;
  max-width: 220px;

  .bubble-content {
    background: rgba(10, 20, 50, 0.92);
    border: 1px solid rgba(64, 158, 255, 0.6);
    border-radius: 6px;
    padding: 6px 10px;
    color: #e0f0ff;
    font-size: 12px;
    line-height: 1.5;
    word-break: break-all;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.5);
  }

  .bubble-arrow {
    position: absolute;
    bottom: -6px;
    width: 0;
    height: 0;
    border-left: 6px solid transparent;
    border-right: 6px solid transparent;
    border-top: 6px solid rgba(64, 158, 255, 0.6);
    transform: translateX(-50%);
  }
}
</style>