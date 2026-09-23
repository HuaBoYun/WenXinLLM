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
        <div class="title-main">审计分析报告</div>
      </div>
      <div class="header-right">
        <el-button
          class="close-btn"
          size="small"
          icon="el-icon-close"
          @click="closeScreen"
        >
          关闭
        </el-button>
      </div>
    </div>

    <!-- 主体内容 -->
    <div class="screen-body">
      <!-- 第1部分: 审计项目列表(顶部大表格) -->
      <div class="panel-box section-1">
        <div class="panel-title">
          <i class="el-icon-document"></i>
          <span>审计项目列表</span>
        </div>
        <div class="data-table">
          <div class="table-header">
            <div class="th th-1">被审计单位</div>
            <div class="th th-2">项目名称</div>
            <div class="th th-3">审计开始时间</div>
            <div class="th th-4">审计类型</div>
            <div class="th th-5">审计来源</div>
            <div class="th th-6">审计结束时间</div>
            <div class="th th-7">未整改数量</div>
            <div class="th th-8">已整改数量</div>
            <div class="th th-9">整改总数</div>
          </div>
          <div class="table-body">
            <div class="scroll-container" ref="list1TableBody">
              <div
                class="tr"
                v-for="(item, index) in list1Data"
                :key="'list1-' + index + '-' + item.type2"
                @mouseenter="onRowEnter('scroll')"
                @mouseleave="onRowLeave('scroll')"
              >
                <div class="td td-1" @mouseenter="(e) => onCellEnter(e, item.type1)" @mouseleave="onCellLeave">{{ item.type1 }}</div>
                <div class="td td-2" @mouseenter="(e) => onCellEnter(e, item.type2)" @mouseleave="onCellLeave">{{ item.type2 }}</div>
                <div class="td td-3" @mouseenter="(e) => onCellEnter(e, item.type3)" @mouseleave="onCellLeave">{{ item.type3 }}</div>
                <div class="td td-4" @mouseenter="(e) => onCellEnter(e, item.type4)" @mouseleave="onCellLeave">{{ item.type4 }}</div>
                <div class="td td-5" @mouseenter="(e) => onCellEnter(e, item.type5)" @mouseleave="onCellLeave">{{ item.type5 }}</div>
                <div class="td td-6" @mouseenter="(e) => onCellEnter(e, item.type6)" @mouseleave="onCellLeave">{{ item.type6 }}</div>
                <div class="td td-7" @mouseenter="(e) => onCellEnter(e, item.type7)" @mouseleave="onCellLeave">{{ item.type7 }}</div>
                <div class="td td-8" @mouseenter="(e) => onCellEnter(e, item.type8)" @mouseleave="onCellLeave">{{ item.type8 }}</div>
                <div class="td td-9" @mouseenter="(e) => onCellEnter(e, item.type9)" @mouseleave="onCellLeave">{{ item.type9 }}</div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 中间行: 3个图表 -->
      <div class="middle-row">
        <!-- 第2部分: 审计项目统计(左侧饼图) -->
        <div class="panel-box chart-panel">
          <div class="panel-title">
            <i class="el-icon-pie-chart"></i>
            <span>审计项目统计</span>
          </div>
          <div class="chart-container">
            <div ref="chart2" class="chart"></div>
          </div>
        </div>

        <!-- 第3部分: 审计项目完成情况(中间饼图) -->
        <div class="panel-box chart-panel">
          <div class="panel-title">
            <i class="el-icon-pie-chart"></i>
            <span>审计项目计划完成情况</span>
          </div>
          <div class="chart-container">
            <div ref="chart3" class="chart"></div>
          </div>
        </div>

        <!-- 第4部分: 年度审计项目数趋势变化(右侧折线图) -->
        <div class="panel-box chart-panel">
          <div class="panel-title">
            <i class="el-icon-data-line"></i>
            <span>项目数趋势变化</span>
          </div>
          <div class="chart-container">
            <div ref="chart4" class="chart"></div>
          </div>
        </div>
      </div>

      <!-- 底部行: 2个表格 + 1个图表 -->
      <div class="bottom-row">
        <!-- 第6部分: 审计项目数一览表(左侧表格) -->
        <div class="panel-box">
          <div class="panel-title">
            <i class="el-icon-s-data"></i>
            <span>审计项目数一览表</span>
          </div>
          <div class="data-table">
            <div class="table-header">
              <div class="th th-1">被审计单位</div>
              <div class="th th-2">已完成项目数量</div>
              <div class="th th-3">未完成项目数量</div>
              <div class="th th-4">项目总数</div>
            </div>
            <div class="table-body">
              <div class="scroll-container" ref="list6TableBody">
                <div
                  class="tr"
                  v-for="(item, index) in list6Data"
                  :key="'list6-' + index + '-' + item.bsjdw"
                  @mouseenter="onRowEnter('scroll6')"
                  @mouseleave="onRowLeave('scroll6')"
                >
                  <div class="td td-1" @mouseenter="(e) => onCellEnter(e, item.bsjdw)" @mouseleave="onCellLeave">{{ item.bsjdw }}</div>
                  <div class="td td-2" @mouseenter="(e) => onCellEnter(e, item.wcs)" @mouseleave="onCellLeave">{{ item.wcs }}</div>
                  <div class="td td-3" @mouseenter="(e) => onCellEnter(e, item.wwcs)" @mouseleave="onCellLeave">{{ item.wwcs }}</div>
                  <div class="td td-4" @mouseenter="(e) => onCellEnter(e, item.zs)" @mouseleave="onCellLeave">{{ item.zs }}</div>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- 第7部分: 整改问题一览表(中间表格) -->
        <div class="panel-box">
          <div class="panel-title">
            <i class="el-icon-s-data"></i>
            <span>整改问题一览表</span>
          </div>
          <div class="data-table">
            <div class="table-header">
              <div class="th th-1">被审计单位</div>
              <div class="th th-2">已整改数量</div>
              <div class="th th-3">未整改数量</div>
              <div class="th th-4">整改总数</div>
              <div class="th th-5">已销号数量</div>
              <div class="th th-6">未销号数量</div>
            </div>
            <div class="table-body">
              <div class="scroll-container" ref="list7TableBody">
                <div
                  class="tr"
                  v-for="(item, index) in list7Data"
                  :key="'list7-' + index + '-' + item.orgname"
                  @mouseenter="onRowEnter('scroll7')"
                  @mouseleave="onRowLeave('scroll7')"
                >
                  <div class="td td-1" @mouseenter="(e) => onCellEnter(e, item.orgname)" @mouseleave="onCellLeave">{{ item.orgname }}</div>
                  <div class="td td-2" @mouseenter="(e) => onCellEnter(e, item.yzg)" @mouseleave="onCellLeave">{{ item.yzg }}</div>
                  <div class="td td-3" @mouseenter="(e) => onCellEnter(e, item.wzg)" @mouseleave="onCellLeave">{{ item.wzg }}</div>
                  <div class="td td-4" @mouseenter="(e) => onCellEnter(e, item.zs)" @mouseleave="onCellLeave">{{ item.zs }}</div>
                  <div class="td td-5" @mouseenter="(e) => onCellEnter(e, item.yxh)" @mouseleave="onCellLeave">{{ item.yxh }}</div>
                  <div class="td td-6" @mouseenter="(e) => onCellEnter(e, item.wxh)" @mouseleave="onCellLeave">{{ item.wxh }}</div>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- 第5部分: 问题统计分析(右侧柱状图) -->
        <div class="panel-box chart-panel">
          <div class="panel-title">
            <i class="el-icon-s-marketing"></i>
            <span>问题统计分析</span>
          </div>
          <div class="chart-container">
            <div ref="chart5" class="chart"></div>
          </div>
        </div>
      </div>
    </div>

    <!-- 单字段气泡框 -->
    <div
      v-if="tooltip.visible"
      class="cell-bubble"
      :style="{ top: tooltip.top + 'px', left: tooltip.left + 'px' }"
    >
      <span class="bubble-arrow" :style="{ left: tooltip.arrowLeft + 'px' }"></span>
      {{ tooltip.text }}
    </div>
  </div>
</template>

<script>
  import * as echarts from 'echarts'
  import {
    getSJXMData,
    getPlanCountData,
    getXMLXData,
    getXMQSData,
    getXMYLData,
    getZGWTData,
    getWTZTData,
    getProjectTrendData,
    getIssuesStatusStatisticsData,
  } from '@/api/audit/sjfx'
  // 修复: 从cwztfx导入getXMZTData,与驾驶舱保持一致
  import { getXMZTData } from '@/api/cwztfx.js'

  export default {
    name: 'ScreenDisplay',
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

        // 主题颜色配置(与驾驶舱保持一致)
        themeColor: '#1890ff',
        themeColors: {
          'vab-theme-default': '#1890ff',
          'vab-theme-red': '#e50113',
          'vab-theme-ocean': '#1890ff',
          'vab-theme-green': '#13ce66',
          'vab-theme-white': '#515a6e',
        },

        // 7个部分的数据
        list1Data: [],
        list1OriginalData: [], // 保存原始数据用于循环
        list2Data: [],
        list3Data: [],
        list4Data: { categories: [], series: [] },
        list5Data: { categories: [], series: [] },
        list6Data: [],
        list6OriginalData: [], // 保存原始数据用于循环
        list7Data: [],
        list7OriginalData: [], // 保存原始数据用于循环

        // 4个图表实例
        chart2: null,
        chart3: null,
        chart4: null,
        chart5: null,

        // 滚动相关
        scrollTimer: null,
        scrollTimer6: null,
        scrollTimer7: null,
        scrollSpeed: 50, // 滚动速度（毫秒）

        // 悬浮气泡
        tooltip: {
          visible: false,
          top: 0,
          left: 0,
          arrowLeft: 0,
          text: '',
        },
      }
    },
    mounted() {
      this.updateTime()
      this.timer = setInterval(this.updateTime, 1000)

      // 初始化主题颜色
      this.initThemeColor()

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
      if (this.scrollTimer6) {
        clearInterval(this.scrollTimer6)
      }
      if (this.scrollTimer7) {
        clearInterval(this.scrollTimer7)
      }
      // 销毁图表实例
      if (this.chart2) this.chart2.dispose()
      if (this.chart3) this.chart3.dispose()
      if (this.chart4) this.chart4.dispose()
      if (this.chart5) this.chart5.dispose()
      // 清理残留的 mousemove 监听
      if (this._cellTarget && this._cellMoveHandler) {
        this._cellTarget.removeEventListener('mousemove', this._cellMoveHandler)
      }
    },
    methods: {
      // 行悬浮：暂停对应滚动
      onRowEnter(timerKey) {
        const key = timerKey === 'scroll' ? 'scrollTimer' : timerKey === 'scroll6' ? 'scrollTimer6' : 'scrollTimer7'
        if (this[key]) {
          clearInterval(this[key])
          this[key] = null
        }
      },

      // 行离开：恢复滚动 + 隐藏气泡
      onRowLeave(timerKey) {
        this.tooltip.visible = false
        if (timerKey === 'scroll') this.startAutoScroll()
        else if (timerKey === 'scroll6') this.startAutoScroll6()
        else this.startAutoScroll7()
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

      // 单元格离开：移除监听 + 隐藏气泡
      onCellLeave() {
        this.tooltip.visible = false
        if (this._cellTarget && this._cellMoveHandler) {
          this._cellTarget.removeEventListener('mousemove', this._cellMoveHandler)
          this._cellTarget = null
          this._cellMoveHandler = null
        }
      },

      // 初始化主题颜色(与驾驶舱保持一致)
      initThemeColor() {
        const body = document.body
        let detectedTheme = 'vab-theme-ocean' // 默认主题

        // 根据body的class获取主题色
        for (const themeClass in this.themeColors) {
          if (body.classList.contains(themeClass)) {
            this.themeColor = this.themeColors[themeClass]
            detectedTheme = themeClass
            break
          }
        }

        // 设置CSS变量,使样式能够动态响应主题变化
        document.documentElement.style.setProperty(
          '--theme-color',
          this.themeColor
        )
      },
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
          // 加载第1部分、第2部分、第3部分、第4部分、第5部分、第6部分和第7部分数据
          await Promise.all([
            this.fetchList1Data(),
            this.fetchList2Data(),
            this.fetchList3Data(),
            this.fetchList4Data(),
            this.fetchList5Data(),
            this.fetchList6Data(),
            this.fetchList7Data(),
          ])
        } catch (error) {
          console.error('获取数据失败:', error)
          this.$message.error('数据加载失败')
        }
      },

      // 第1部分: 审计项目列表
      async fetchList1Data() {
        const res = await getSJXMData({
          year: this.selectedYear,
          pageNumber: 1,
          pageSize: 100000, // 分页大小为100000
        })
        if (res.code === 1 && res.data) {
          // 处理分页数据
          const dataList = res.data.list || []
          const processedData = this.processList1Data(dataList)

          // 保存原始数据
          this.list1OriginalData = [...processedData]
          // 初始显示数据（复制一份用于滚动）
          this.list1Data = [...processedData]

          // 启动自动滚动
          this.$nextTick(() => {
            this.startAutoScroll()
          })
        }
      },

      processList1Data(data) {
        const fillUnknown = (value) => {
          if (
            value === null ||
            value === undefined ||
            (typeof value === 'string' && value.trim() === '')
          ) {
            return '未知'
          }
          return value
        }

        const mapStatus = (statusValue) => {
          if (
            statusValue === null ||
            statusValue === undefined ||
            statusValue === ''
          )
            return '未启动'
          const status = String(statusValue)
          const statusMap = {
            0: '未启动',
            1: '启动',
            2: '实施',
            3: '完成',
            4: '归档',
          }
          return statusMap[status] || '未启动'
        }

        const formatNumber = (numValue) => {
          if (
            numValue === null ||
            numValue === undefined ||
            numValue === '' ||
            isNaN(Number(numValue))
          ) {
            return '0'
          }
          return Number(numValue).toString()
        }

        return (data || []).map((item) => ({
          type1: fillUnknown(item.orgname), // 审计单位
          type2: fillUnknown(item.prjoectname), // 项目名称
          type3: item.startdate ? item.startdate.slice(0, 10) : '未知', // 审计开始时间
          type4: fillUnknown(item.audittype), // 审计类型
          type5: fillUnknown(item.projectsource), // 审计来源
          type6: item.enddate ? item.enddate.slice(0, 10) : '未知', // 审计结束时间
          type7: formatNumber(item.wzg), // 未整改数量
          type8: formatNumber(item.yzg), // 已整改数量
          type9: formatNumber(item.zs), // 整改总数
        }))
      },

      // 第2部分: 审计项目统计（使用审计项目计划数接口）
      async fetchList2Data() {
        try {
          const res = await getPlanCountData({ year: this.selectedYear })
          console.log('审计项目统计接口返回:', res)
          if (res.code === 1 && res.data) {
            // 处理数据：接口已返回聚合后的统计数据
            const dataList = res.data.list || []
            console.log('提取的统计数据列表:', dataList)
            this.list2Data = this.processList2Data(dataList)
            console.log('处理后的饼图数据:', this.list2Data)
            console.log('饼图数据长度:', this.list2Data.length)
            this.$nextTick(() => {
              console.log('开始初始化图表2，DOM元素:', this.$refs.chart2)
              this.initChart2(this.list2Data)
            })
          }
        } catch (error) {
          console.error('获取审计项目统计数据失败:', error)
        }
      },

      processList2Data(data) {
        // 将审计项目计划数统计数据转换为饼图格式
        // 与审计驾驶舱保持一致的数据处理方式
        if (!Array.isArray(data)) {
          return []
        }

        // 接口返回的数据格式: [{ bsjdw: '被审计单位', zs: 总数, wcs: 已完成, wwcs: 未完成 }]
        return data.map((item) => ({
          name: item.bsjdw || '未知',
          value: Number(item.zs) || 0,
        }))
      },

      initChart2(data) {
        console.log('initChart2 被调用，数据:', data)
        if (!this.$refs.chart2) {
          console.error('chart2 DOM 元素不存在')
          return
        }
        if (this.chart2) {
          this.chart2.dispose()
        }
        this.chart2 = echarts.init(this.$refs.chart2)
        const option = {
          tooltip: {
            trigger: 'item',
            formatter: '{b}: {c} ({d}%)',
          },
          legend: {
            orient: 'horizontal',
            bottom: '0%',
            textStyle: { color: '#fff', fontSize: 11 },
            type: 'scroll', // 启用滚动翻页
            pageTextStyle: { color: '#fff' }, // 翻页按钮文字颜色
            pageIconColor: '#fff', // 翻页按钮颜色
            pageIconInactiveColor: 'rgba(255, 255, 255, 0.3)', // 翻页按钮禁用颜色
            pageIconSize: 12, // 翻页按钮大小
            pageButtonItemGap: 5, // 翻页按钮间隔
          },
          series: [
            {
              name: '项目数量',
              type: 'pie',
              radius: ['30%', '60%'],
              center: ['50%', '40%'],
              data: data,
              label: {
                color: '#fff',
                fontSize: 11,
                formatter: '{b}: {d}%',
              },
              emphasis: {
                label: {
                  show: true,
                  fontSize: 14,
                  fontWeight: 'bold',
                },
              },
            },
          ],
        }
        console.log('设置图表配置:', option)
        this.chart2.setOption(option)
        console.log('图表2初始化完成')
      },

      // 第3部分: 审计项目完成情况（使用审计项目计划完成情况接口）
      async fetchList3Data() {
        try {
          const res = await getXMZTData({ year: this.selectedYear })
          console.log('审计项目完成情况接口返回:', res)
          if (res.code === 1 && res.data) {
            // 与驾驶舱保持一致：从 res.data.list 中获取数据
            // 数据映射为: { value: item.sl, name: item.status }
            const dataList = res.data.list || []
            this.list3Data = this.processList3Data(dataList)
            console.log('处理后的饼图数据:', this.list3Data)
            this.$nextTick(() => {
              this.initChart3(this.list3Data)
            })
          }
        } catch (error) {
          console.error('获取审计项目完成情况数据失败:', error)
        }
      },

      processList3Data(data) {
        // 将审计项目计划完成情况数据转换为饼图格式
        // 与驾驶舱保持一致的数据处理方式
        if (!Array.isArray(data)) {
          return []
        }

        // 接口返回的数据格式: [{ sl: 数量, status: '状态名称' }]
        // 映射为饼图需要的格式: [{ value: item.sl, name: item.status }]
        return data.map((item) => ({
          name: item.status || '未知',
          value: Number(item.sl) || 0,
        }))
      },

      initChart3(data) {
        if (!this.$refs.chart3) return
        if (this.chart3) {
          this.chart3.dispose()
        }
        this.chart3 = echarts.init(this.$refs.chart3)
        const option = {
          tooltip: {
            trigger: 'item',
            formatter: '{b}: {c} ({d}%)',
            backgroundColor: 'rgba(255, 255, 255, 0.95)',
            borderColor: this.themeColor,
            borderWidth: 1,
            textStyle: {
              color: '#595959',
            },
          },
          legend: {
            orient: 'vertical',
            left: 'right',
            y: 'middle',
            x: 'right',
            textStyle: {
              color: '#fff',
              fontSize: 11,
            },
          },
          color: ['#52c41a', '#faad14', this.themeColor, '#ff4d4f'],
          series: [
            {
              name: '完成情况',
              type: 'pie',
              radius: ['45%', '70%'],
              center: ['35%', '50%'],
              data: data,
              label: {
                color: '#fff',
                fontSize: 11,
                formatter: '{b}: {d}%',
              },
              emphasis: {
                itemStyle: {
                  shadowBlur: 15,
                  shadowOffsetX: 0,
                  shadowColor: 'rgba(0, 0, 0, 0.5)',
                },
              },
            },
          ],
        }
        this.chart3.setOption(option)

        // 添加点击事件监听器(与驾驶舱保持一致)
        this.chart3.on('click', (params) => {
          if (params.componentType === 'series') {
            // 触发父组件的点击事件,传递状态名称
            this.$emit('chart-click', {
              type: 'status',
              value: params.name,
            })
          }
        })
      },

      // 第4部分: 项目数趋势变化
      async fetchList4Data() {
        try {
          const res = await getProjectTrendData()
          console.log('getProjectTrendData 接口返回:', res)
          if (res.code === 1 && res.data) {
            // 直接使用返回的原始数据，不需要额外处理
            const dataList = res.data || []
            console.log('处理后的数据列表:', dataList)
            this.list4Data = this.processList4Data(dataList)
            this.$nextTick(() => {
              this.initChart4(this.list4Data)
            })
          }
        } catch (error) {
          console.error('获取项目数趋势数据失败:', error)
        }
      },

      processList4Data(data) {
        // 数据格式: [{D: '2025-02', C: 6}, {D: '2025-03', C: 13}, ...] 或 [{d: '2025-02', c: 6}, ...]
        const rawData = data || []

        if (!Array.isArray(rawData) || rawData.length === 0) {
          return {
            categories: [],
            series: [
              {
                name: '项目数量',
                data: [],
              },
            ],
          }
        }

        // 兼容大写和小写字段名
        const categories = rawData.map(
          (item) => item.D || item.d || item.name || ''
        )
        const series = [
          {
            name: '项目数量',
            data: rawData.map(
              (item) => Number(item.C || item.c || item.value) || 0
            ),
          },
        ]

        return { categories, series }
      },

      initChart4(data) {
        if (!this.$refs.chart4) return
        if (this.chart4) {
          this.chart4.dispose()
        }
        this.chart4 = echarts.init(this.$refs.chart4)
        const option = {
          tooltip: {
            trigger: 'axis',
            axisPointer: {
              type: 'cross',
              crossStyle: {
                color: '#999',
              },
            },
          },
          legend: {
            data: ['项目数量', '趋势'],
            textStyle: { color: '#fff' },
            top: '5%',
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
            data: data.categories,
            axisLine: { lineStyle: { color: 'rgba(255,255,255,0.3)' } },
            axisLabel: { color: '#fff', fontSize: 11 },
          },
          yAxis: {
            type: 'value',
            axisLine: { lineStyle: { color: 'rgba(255,255,255,0.3)' } },
            axisLabel: { color: '#fff', fontSize: 11 },
            splitLine: { lineStyle: { color: 'rgba(255,255,255,0.1)' } },
          },
          series: [
            {
              name: '项目数量',
              type: 'bar',
              data: data.series[0].data,
              itemStyle: {
                color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                  { offset: 0, color: 'rgba(64,158,255,0.8)' },
                  { offset: 1, color: 'rgba(64,158,255,0.3)' },
                ]),
              },
              barWidth: '60%',
            },
            {
              name: '趋势',
              type: 'line',
              smooth: true,
              data: data.series[0].data,
              lineStyle: { color: '#67C23A', width: 2 },
              itemStyle: { color: '#67C23A' },
              symbol: 'circle',
              symbolSize: 6,
            },
          ],
        }
        this.chart4.setOption(option)
      },

      // 第6部分: 审计项目数一览表（使用聚合统计接口）
      async fetchList6Data() {
        const res = await getPlanCountData({ year: this.selectedYear })
        console.log('审计项目数一览表接口返回:', res)
        if (res.code === 1 && res.data) {
          // 处理数据
          const dataList = res.data.list || []
          const processedData = this.processList6Data(dataList)

          // 保存原始数据
          this.list6OriginalData = [...processedData]
          // 初始显示数据
          this.list6Data = [...processedData]

          // 启动自动滚动
          this.$nextTick(() => {
            this.startAutoScroll6()
          })
        }
      },

      processList6Data(data) {
        if (!Array.isArray(data)) {
          return []
        }

        // 与审计驾驶舱保持一致的数据处理方式
        return data.map((item) => ({
          bsjdw: item.bsjdw || '未知', // 被审计单位
          wcs: item.wcs !== null && item.wcs !== undefined ? item.wcs : 0, // 已完成项目数量
          wwcs: item.wwcs !== null && item.wwcs !== undefined ? item.wwcs : 0, // 未完成项目数量
          zs: item.zs !== null && item.zs !== undefined ? item.zs : 0, // 项目总数
        }))
      },

      // 第7部分: 整改问题一览表
      async fetchList7Data() {
        const res = await getZGWTData({
          year: this.selectedYear,
          pageNumber: 1,
          pageSize: 1000, // 获取所有数据，不分页
        })
        console.log('getZGWTData 接口返回:', res)
        if (res.code === 1 && res.data) {
          // 处理分页数据
          const dataList = res.data.list || res.data
          const processedData = this.processList7Data(dataList)

          // 保存原始数据
          this.list7OriginalData = [...processedData]
          // 初始显示数据
          this.list7Data = [...processedData]

          // 启动自动滚动
          this.$nextTick(() => {
            this.startAutoScroll7()
          })
        }
      },

      processList7Data(data) {
        if (!Array.isArray(data)) {
          return []
        }

        // 与审计驾驶舱保持一致的数据处理方式
        return data.map((item) => ({
          orgname: item.orgname || '未知', // 主管部门
          yzg: item.yzg !== null && item.yzg !== undefined ? item.yzg : 0, // 已整改数量
          wzg: item.wzg !== null && item.wzg !== undefined ? item.wzg : 0, // 未整改数量
          zs: item.zs !== null && item.zs !== undefined ? item.zs : 0, // 整改总数
          yxh: item.yxh !== null && item.yxh !== undefined ? item.yxh : 0, // 已销号数量
          wxh: item.wxh !== null && item.wxh !== undefined ? item.wxh : 0, // 未销号数量
        }))
      },

      // 第5部分: 问题统计分析
      async fetchList5Data() {
        try {
          const res = await getIssuesStatusStatisticsData()
          if (res.code === 1 && res.data) {
            // 处理返回的原始数据
            const dataList = res.data || []
            this.list5Data = this.processList5Data(dataList)
            this.$nextTick(() => {
              this.initChart5(this.list5Data)
            })
          }
        } catch (error) {
          console.error('获取问题统计数据失败:', error)
        }
      },

      processList5Data(data) {
        // 数据格式: [{yzg: 10, wzg: 25, yxh: 5, wxh: 15}] (单条记录)
        const rawData = data || []

        // 如果是数组,取第一条记录
        const item =
          Array.isArray(rawData) && rawData.length > 0 ? rawData[0] : rawData

        // 兼容大写和小写字段名
        const ywc = Number(item.YZG || item.yzg) || 0 // 已整改
        const wwc = Number(item.WZG || item.wzg) || 0 // 未整改
        const yxh = Number(item.YXH || item.yxh) || 0 // 已销号
        const wxh = Number(item.WXH || item.wxh) || 0 // 未销号

        return {
          categories: ['已整改', '未整改', '已销号', '未销号'],
          series: [
            {
              name: '问题数量',
              data: [ywc, wwc, yxh, wxh],
            },
          ],
        }
      },

      initChart5(data) {
        if (!this.$refs.chart5) return
        if (this.chart5) {
          this.chart5.dispose()
        }
        this.chart5 = echarts.init(this.$refs.chart5)
        const option = {
          tooltip: {
            trigger: 'axis',
            axisPointer: { type: 'shadow' },
          },
          grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            top: '10%',
            containLabel: true,
          },
          xAxis: {
            type: 'category',
            data: data.categories,
            axisLine: { lineStyle: { color: 'rgba(255,255,255,0.3)' } },
            axisLabel: { color: '#fff', fontSize: 11 },
          },
          yAxis: {
            type: 'value',
            axisLine: { lineStyle: { color: 'rgba(255,255,255,0.3)' } },
            axisLabel: { color: '#fff', fontSize: 11 },
            splitLine: { lineStyle: { color: 'rgba(255,255,255,0.1)' } },
          },
          series: data.series.map((s) => ({
            name: s.name,
            type: 'bar',
            data: s.data,
            barWidth: '60%',
            itemStyle: {
              color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                { offset: 0, color: '#67c23a' },
                { offset: 1, color: '#3a8f1f' },
              ]),
            },
            label: {
              show: true,
              position: 'top',
              color: '#fff',
              fontSize: 12,
            },
          })),
        }
        this.chart5.setOption(option)
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

      // 将hex颜色转换为rgba(与驾驶舱保持一致)
      hexToRgba(hex, alpha) {
        let r = 0,
          g = 0,
          b = 0
        // 处理3位hex
        if (hex.length === 4) {
          r = parseInt(hex[1] + hex[1], 16)
          g = parseInt(hex[2] + hex[2], 16)
          b = parseInt(hex[3] + hex[3], 16)
        } else if (hex.length === 7) {
          // 处理6位hex
          r = parseInt(hex.substring(1, 3), 16)
          g = parseInt(hex.substring(3, 5), 16)
          b = parseInt(hex.substring(5, 7), 16)
        }
        return `rgba(${r}, ${g}, ${b}, ${alpha})`
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

      // 第1部分: 审计项目列表（顶部行 - 占1/3）
      .section-1 {
        flex: 1;
        min-height: 0;
        overflow: hidden;
      }

      // 中间行: 3个图表（占1/3）
      .middle-row {
        display: flex;
        gap: 15px;
        flex: 1;
        min-height: 0;
        overflow: hidden;

        .panel-box {
          flex: 1;
          min-height: 0;
        }
      }

      // 底部行: 2个表格 + 1个图表（占1/3）
      .bottom-row {
        display: flex;
        gap: 15px;
        flex: 1;
        min-height: 0;
        overflow: hidden;

        .panel-box {
          flex: 1;
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

        .cell-tooltip-text {
          display: block;
          width: 100%;
          overflow: hidden;
          text-overflow: ellipsis;
          white-space: nowrap;
        }
      }

      // 第1部分表格列宽
      .section-1 {
        .th-1,
        .td-1 {
          flex: 0 0 200px;
        } // 审计单位（最大）
        .th-2,
        .td-2 {
          flex: 0 0 150px;
        } // 项目名称
        .th-3,
        .td-3 {
          flex: 1;
          min-width: 110px;
        } // 审计开始时间（弹性）
        .th-4,
        .td-4 {
          flex: 1;
          min-width: 130px;
        } // 审计类型（弹性）
        .th-5,
        .td-5 {
          flex: 1;
          min-width: 100px;
        } // 审计来源（弹性）
        .th-6,
        .td-6 {
          flex: 1;
          min-width: 110px;
        } // 审计结束时间（弹性）
        .th-7,
        .td-7 {
          flex: 1;
          min-width: 90px;
        } // 未整改数量（弹性）
        .th-8,
        .td-8 {
          flex: 1;
          min-width: 90px;
        } // 已整改数量（弹性）
        .th-9,
        .td-9 {
          flex: 1;
          min-width: 90px;
        } // 整改总数（弹性）
      }

      // 第6部分表格列宽
      .bottom-row .panel-box:nth-child(1) {
        .th-1,
        .td-1 {
          flex: 1;
          text-align: left;
        } // 被审计单位（最大宽度）
        .th-2,
        .td-2 {
          flex: 0 0 130px;
        } // 已完成项目数量
        .th-3,
        .td-3 {
          flex: 0 0 130px;
        } // 未完成项目数量
        .th-4,
        .td-4 {
          flex: 0 0 100px;
        } // 项目总数
      }

      // 第7部分表格列宽
      .bottom-row .panel-box:nth-child(2) {
        .th-1,
        .td-1 {
          flex: 1;
          text-align: left;
        } // 主管部门（最大宽度）
        .th-2,
        .td-2 {
          flex: 0 0 100px;
        } // 已整改数量
        .th-3,
        .td-3 {
          flex: 0 0 100px;
        } // 未整改数量
        .th-4,
        .td-4 {
          flex: 0 0 100px;
        } // 整改总数
        .th-5,
        .td-5 {
          flex: 0 0 100px;
        } // 已销号数量
        .th-6,
        .td-6 {
          flex: 0 0 100px;
        } // 未销号数量
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

  // 单字段气泡框
  .cell-bubble {
    position: fixed;
    z-index: 9999;
    transform: translateY(-100%);
    background: rgba(10, 20, 50, 0.95);
    border: 1px solid rgba(64, 158, 255, 0.6);
    border-radius: 6px;
    padding: 7px 12px;
    max-width: 280px;
    font-size: 13px;
    color: #e0e8ff;
    line-height: 1.5;
    word-break: break-all;
    box-shadow: 0 4px 16px rgba(0, 0, 0, 0.5), 0 0 10px rgba(64, 158, 255, 0.2);
    pointer-events: none;
    white-space: pre-wrap;

    .bubble-arrow {
      position: absolute;
      bottom: -6px;
      transform: translateX(-50%);
      width: 0;
      height: 0;
      border-left: 6px solid transparent;
      border-right: 6px solid transparent;
      border-top: 6px solid rgba(64, 158, 255, 0.6);

      &::after {
        content: '';
        position: absolute;
        top: -7px;
        left: -5px;
        width: 0;
        height: 0;
        border-left: 5px solid transparent;
        border-right: 5px solid transparent;
        border-top: 5px solid rgba(10, 20, 50, 0.95);
      }
    }
  }
</style>
