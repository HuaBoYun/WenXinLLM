<template>
  <div class="risk-screen-display">
    <!-- 头部 -->
    <div class="screen-header">
      <div class="header-left">
        <div class="current-time">{{ currentDate }} {{ currentTime }}</div>
      </div>
      <div class="header-title">
        <div class="title-main">风险总览</div>
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
      <!-- 第一行：风险Top10（只查看） -->
      <div class="panel-box top10-panel">
        <div class="panel-title">
          <i class="el-icon-s-claim"></i>
          <span>风险Top10</span>
        </div>
        <div class="data-table top10-table">
          <div class="table-header">
            <div class="th t10-th-1">序号</div>
            <div class="th t10-th-2">风险描述</div>
            <div class="th t10-th-3">风险变化趋势</div>
            <div class="th t10-th-4">责任部门</div>
            <div class="th t10-th-5">责任领导</div>
          </div>
          <div class="table-body">
            <div class="scroll-container" ref="listTop10TableBody">
              <div
                class="tr"
                v-for="(item, index) in listTop10Data"
                :key="'top10-' + index"
                @mouseenter="onRowEnter(0)"
                @mouseleave="onRowLeave(0)"
              >
                <div class="td t10-td-1">{{ index + 1 }}</div>
                <div
                  class="td t10-td-2"
                  @mouseenter="(e) => onCellEnter(e, item.riskdes)"
                  @mouseleave="onCellLeave"
                >
                  {{ item.riskdes }}
                </div>
                <div class="td t10-td-3">
                  {{ formatRiskChange(item.riskchange) }}
                </div>
                <div
                  class="td t10-td-4"
                  @mouseenter="(e) => onCellEnter(e, item.orgname)"
                  @mouseleave="onCellLeave"
                >
                  {{ item.orgname }}
                </div>
                <div
                  class="td t10-td-5"
                  @mouseenter="(e) => onCellEnter(e, item.field6)"
                  @mouseleave="onCellLeave"
                >
                  {{ item.field6 }}
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div class="main-columns">
        <!-- 左侧列 -->
        <div class="left-column">
          <!-- 左上: 催办情况(表格) -->
          <div class="panel-box">
            <div class="panel-title">
              <i class="el-icon-bell"></i>
              <span>催办情况</span>
            </div>
            <div class="data-table">
              <div class="table-header">
                <div class="th th-1">通知内容</div>
                <div class="th th-2">类型</div>
                <div class="th th-3">下发人</div>
                <div class="th th-4">下发时间</div>
              </div>
              <div class="table-body">
                <div class="scroll-container" ref="list1TableBody">
                  <div
                    class="tr"
                    v-for="(item, index) in list1Data"
                    :key="'list1-' + index"
                    @mouseenter="onRowEnter(1)"
                    @mouseleave="onRowLeave(1)"
                  >
                    <div
                      class="td td-1"
                      @mouseenter="(e) => onCellEnter(e, item.type1)"
                      @mouseleave="onCellLeave"
                    >
                      {{ item.type1 }}
                    </div>
                    <div
                      class="td td-2"
                      @mouseenter="(e) => onCellEnter(e, item.type2)"
                      @mouseleave="onCellLeave"
                    >
                      {{ item.type2 }}
                    </div>
                    <div
                      class="td td-3"
                      @mouseenter="(e) => onCellEnter(e, item.type3)"
                      @mouseleave="onCellLeave"
                    >
                      {{ item.type3 }}
                    </div>
                    <div
                      class="td td-4"
                      @mouseenter="(e) => onCellEnter(e, item.type4)"
                      @mouseleave="onCellLeave"
                    >
                      {{ item.type4 }}
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <!-- 左中: 月度评估情况一览表(表格) -->
          <div class="panel-box">
            <div class="panel-title">
              <i class="el-icon-s-data"></i>
              <span>月度评估情况一览表</span>
            </div>
            <div class="data-table">
              <div class="table-header">
                <div class="th th-1">风险编号</div>
                <div class="th th-2">风险名称</div>
                <div class="th th-3">所属公司</div>
                <div class="th th-4">创建时间</div>
                <div class="th th-5">是否关闭</div>
                <div class="th th-6">趋势</div>
                <div class="th th-7">控制措施数</div>
              </div>
              <div class="table-body">
                <div class="scroll-container" ref="list2TableBody">
                  <div
                    class="tr"
                    v-for="(item, index) in list2Data"
                    :key="'list2-' + index"
                    @mouseenter="onRowEnter(2)"
                    @mouseleave="onRowLeave(2)"
                  >
                    <div
                      class="td td-1"
                      @mouseenter="(e) => onCellEnter(e, item.risknumber)"
                      @mouseleave="onCellLeave"
                    >
                      {{ item.risknumber }}
                    </div>
                    <div
                      class="td td-2"
                      @mouseenter="(e) => onCellEnter(e, item.riskname)"
                      @mouseleave="onCellLeave"
                    >
                      {{ item.riskname }}
                    </div>
                    <div
                      class="td td-3"
                      @mouseenter="(e) => onCellEnter(e, item.unitname)"
                      @mouseleave="onCellLeave"
                    >
                      {{ item.unitname }}
                    </div>
                    <div
                      class="td td-4"
                      @mouseenter="(e) => onCellEnter(e, item.riskcreatedt)"
                      @mouseleave="onCellLeave"
                    >
                      {{ item.riskcreatedt }}
                    </div>
                    <div
                      class="td td-5"
                      @mouseenter="(e) => onCellEnter(e, item.riskstatusText)"
                      @mouseleave="onCellLeave"
                    >
                      {{ item.riskstatusText }}
                    </div>
                    <div
                      class="td td-6"
                      @mouseenter="(e) => onCellEnter(e, item.riskchangeText)"
                      @mouseleave="onCellLeave"
                    >
                      {{ item.riskchangeText }}
                    </div>
                    <div
                      class="td td-7"
                      @mouseenter="(e) => onCellEnter(e, item.copingcount)"
                      @mouseleave="onCellLeave"
                    >
                      {{ item.copingcount }}
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <!-- 左下: 风险审查情况分析表(近12个月) -->
          <div class="panel-box chart-panel">
            <div class="panel-title">
              <i class="el-icon-data-line"></i>
              <span>风险审查情况分析表(近12个月)</span>
            </div>
            <div class="chart-container">
              <div
                ref="chart1"
                class="chart"
                @mousemove="
                  (e) => onChartAxisHover(e, 'chart1', 'chart1Data', 'orgname')
                "
                @mouseleave="onChartAxisLeave"
              ></div>
            </div>
          </div>
        </div>

        <!-- 右侧列 -->
        <div class="right-column">
          <!-- 右上: 风险数据库一览表(表格) -->
          <div class="panel-box">
            <div class="panel-title">
              <i class="el-icon-s-data"></i>
              <span>风险数据库一览表</span>
            </div>
            <div class="data-table">
              <div class="table-header">
                <div class="th th-1">风险领域名称</div>
                <div class="th th-2">措施数量</div>
                <div class="th th-3">完成措施数量</div>
                <div class="th th-4">未完成措施数量</div>
              </div>
              <div class="table-body">
                <div class="scroll-container" ref="list3TableBody">
                  <div
                    class="tr"
                    v-for="(item, index) in list3Data"
                    :key="'list3-' + index"
                    @mouseenter="onRowEnter(3)"
                    @mouseleave="onRowLeave(3)"
                  >
                    <div
                      class="td td-1"
                      @mouseenter="(e) => onCellEnter(e, item.riskcatname)"
                      @mouseleave="onCellLeave"
                    >
                      {{ item.riskcatname }}
                    </div>
                    <div
                      class="td td-2"
                      @mouseenter="(e) => onCellEnter(e, item.totalCount)"
                      @mouseleave="onCellLeave"
                    >
                      {{ item.totalCount }}
                    </div>
                    <div
                      class="td td-3"
                      @mouseenter="(e) => onCellEnter(e, item.completedCount)"
                      @mouseleave="onCellLeave"
                    >
                      {{ item.completedCount }}
                    </div>
                    <div
                      class="td td-4"
                      @mouseenter="(e) => onCellEnter(e, item.uncompletedCount)"
                      @mouseleave="onCellLeave"
                    >
                      {{ item.uncompletedCount }}
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <!-- 右中: 双饼图区域 -->
          <div class="panel-box chart-panel dual-chart-panel">
            <div class="panel-title">
              <i class="el-icon-pie-chart"></i>
              <span>风险统计</span>
            </div>
            <div class="dual-chart-container">
              <!-- 左侧: 风险完成情况 -->
              <div class="chart-wrapper left-chart">
                <div class="sub-chart-title">风险完成情况</div>
                <div ref="chart2Left" class="chart"></div>
              </div>
              <!-- 右侧: 风险措施状态 -->
              <div class="chart-wrapper right-chart">
                <div class="sub-chart-title">风险措施状态</div>
                <div ref="chart2Right" class="chart"></div>
              </div>
            </div>
          </div>

          <!-- 右下: 风险事件数(图表) -->
          <div class="panel-box chart-panel">
            <div class="panel-title">
              <i class="el-icon-s-marketing"></i>
              <span>风险事件数</span>
            </div>
            <div class="chart-container">
              <div
                ref="chart3"
                class="chart"
                @mousemove="
                  (e) =>
                    onChartAxisHover(
                      e,
                      'chart3',
                      'chart3Data',
                      'occurredDepartment'
                    )
                "
                @mouseleave="onChartAxisLeave"
              ></div>
            </div>
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
      <span
        class="bubble-arrow"
        :style="{ left: tooltip.arrowLeft + 'px' }"
      ></span>
      {{ tooltip.text }}
    </div>
  </div>
</template>

<script>
  import * as echarts from 'echarts'
  import {
    getRiskTopList,
    getUnreadReminderList,
    getMonthlyEvaluationList,
    getRiskDatabaseList,
    getRiskReviewAnalysis,
    getRiskEventCount,
    getRiskMeasureStatus,
    getRiskCompletion,
  } from '@/api/risk/home'

  export default {
    name: 'ScreenDisplay',
    props: {
      riskData: {
        type: Object,
        default: () => ({}),
      },
    },
    data() {
      return {
        currentDate: '',
        currentTime: '',
        timer: null,

        // 表格区域数据
        listTop10Data: [], // 第一行: 风险Top10
        listTop10OriginalData: [],
        list1Data: [], // 左上: 催办情况
        list1OriginalData: [],
        list2Data: [], // 左中: 月度评估情况一览表
        list2OriginalData: [],
        list3Data: [], // 右上: 风险数据库一览表
        list3OriginalData: [],

        // 3个图表实例
        chart1: null, // 左下: 风险审查情况分析表
        chart1Data: [], // 风险审查情况分析数据

        // 图表2实例(双饼图)
        chart2Left: null, // 右中左侧: 风险完成情况
        chart2Right: null, // 右中右侧: 风险措施状态

        // 图表2数据
        chart2LeftData: null, // 风险完成情况数据(模拟)
        chart2RightData: null, // 风险措施状态数据(真实)

        chart3: null, // 右下: 风险事件数
        chart3Data: [], // 风险事件数数据

        // 滚动相关
        scrollTimer0: null,
        scrollTimer1: null,
        scrollTimer2: null,
        scrollTimer3: null,
        scrollSpeed: 50, // 滚动速度（毫秒）

        // 悬浮详情框
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

      // 加载所有数据
      this.loadAllData()
    },
    beforeDestroy() {
      if (this.timer) {
        clearInterval(this.timer)
      }
      // 清除滚动定时器
      if (this.scrollTimer0) {
        clearInterval(this.scrollTimer0)
      }
      if (this.scrollTimer1) {
        clearInterval(this.scrollTimer1)
      }
      if (this.scrollTimer2) {
        clearInterval(this.scrollTimer2)
      }
      if (this.scrollTimer3) {
        clearInterval(this.scrollTimer3)
      }
      // 销毁图表实例
      if (this.chart1) this.chart1.dispose()
      if (this.chart2Left) this.chart2Left.dispose()
      if (this.chart2Right) this.chart2Right.dispose()
      if (this.chart3) this.chart3.dispose()
      // 清理残留的 mousemove 监听
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

      // 图表 x 轴标签悬浮气泡
      onChartAxisHover(e, chartRef, dataKey, nameField) {
        const chartInstance = this[chartRef]
        if (!chartInstance) return

        // 获取图表 DOM 的位置
        const rect = e.currentTarget.getBoundingClientRect()
        const offsetX = e.clientX - rect.left
        const offsetY = e.clientY - rect.top

        // 获取图表 grid 底部边界（x 轴标签区域在 grid 下方）
        const gridBottom = chartInstance
          .getModel()
          .getComponent('grid')
          .coordinateSystem.getRect()
        const axisAreaTop = gridBottom.y + gridBottom.height

        // 只在 x 轴标签区域响应（grid 底部以下）
        if (offsetY < axisAreaTop) {
          this.tooltip.visible = false
          return
        }

        // 用 convertFromPixel 找到最近的 x 轴索引
        const dataIndex = chartInstance.convertFromPixel({ xAxisIndex: 0 }, [
          offsetX,
          offsetY,
        ])
        const index = Math.round(dataIndex)
        const dataArr = this[dataKey]
        if (index < 0 || index >= dataArr.length) {
          this.tooltip.visible = false
          return
        }

        const fullName = dataArr[index][nameField] || ''
        if (!fullName) {
          this.tooltip.visible = false
          return
        }

        // 气泡定位：光标上方，箭头对齐光标
        const bubbleWidth = 220
        const edgePadding = 8
        let left = e.clientX - bubbleWidth / 4
        if (left < edgePadding) left = edgePadding
        if (left + bubbleWidth > window.innerWidth - edgePadding) {
          left = window.innerWidth - bubbleWidth - edgePadding
        }
        const arrowLeft = Math.min(
          Math.max(e.clientX - left, 10),
          bubbleWidth - 10
        )
        this.tooltip = {
          visible: true,
          top: e.clientY - 12,
          left,
          arrowLeft,
          text: fullName,
        }
      },

      onChartAxisLeave() {
        this.tooltip.visible = false
      },

      // 行悬浮：暂停滚动
      onRowEnter(listIndex) {
        const timerKey = `scrollTimer${listIndex}`
        if (this[timerKey]) {
          clearInterval(this[timerKey])
          this[timerKey] = null
        }
      },

      // 行离开：恢复滚动 + 隐藏气泡
      onRowLeave(listIndex) {
        this.tooltip.visible = false
        this[`startScroll${listIndex}`]()
      },

      // 单元格悬浮：显示气泡并跟随光标
      onCellEnter(e, text) {
        if (text === null || text === undefined || text === '') return
        const content = String(text)
        const bubbleWidth = 220
        const arrowSize = 6 // 箭头半宽
        const edgePadding = 8

        this._cellMoveHandler = (ev) => {
          // 气泡左边缘：让光标落在气泡左侧 1/4 处，视觉上更自然
          let left = ev.clientX - bubbleWidth / 4
          // 防止超出左右边界
          if (left < edgePadding) left = edgePadding
          if (left + bubbleWidth > window.innerWidth - edgePadding) {
            left = window.innerWidth - bubbleWidth - edgePadding
          }
          // 箭头相对气泡的偏移 = 光标位置 - 气泡左边缘，夹在合理范围内
          const arrowLeft = Math.min(
            Math.max(ev.clientX - left, arrowSize + 4),
            bubbleWidth - arrowSize - 4
          )
          const top = ev.clientY - 12
          this.tooltip = { visible: true, top, left, arrowLeft, text: content }
        }
        this._cellMoveHandler(e)
        e.currentTarget.addEventListener('mousemove', this._cellMoveHandler)
        this._cellTarget = e.currentTarget
      },

      // 单元格离开：移除监听 + 隐藏气泡
      onCellLeave() {
        this.tooltip.visible = false
        if (this._cellTarget && this._cellMoveHandler) {
          this._cellTarget.removeEventListener(
            'mousemove',
            this._cellMoveHandler
          )
          this._cellTarget = null
          this._cellMoveHandler = null
        }
      },
      async loadAllData() {
        // 加载风险Top10数据
        await this.fetchTop10Data()

        // 加载催办情况数据
        await this.fetchList1Data()

        // 加载月度评估情况数据
        await this.fetchList2Data()

        // 加载风险数据库一览表数据
        await this.fetchList3Data()

        // 加载风险审查情况分析数据
        await this.fetchChart1Data()

        // 加载右侧饼图数据(风险措施状态)
        await this.fetchChart2RightData()
        // 加载左侧饼图数据(风险完成情况)
        await this.fetchChart2LeftData()

        // 加载风险事件数统计数据
        await this.fetchChart3Data()

        // 生成其他模拟数据
        this.generateMockData()

        // 初始化图表
        this.$nextTick(() => {
          this.initChart1()
          this.initChart2Left() // 初始化左侧饼图
          this.initChart2Right() // 初始化右侧饼图
          this.initChart3()

          // 启动表格滚动
          this.startScroll0()
          this.startScroll1()
          this.startScroll2()
          this.startScroll3()
        })
      },

      // 获取风险Top10数据
      async fetchTop10Data() {
        try {
          const res = await getRiskTopList()
          const list =
            (res &&
              res.code === 1 &&
              res.data &&
              (res.data.data || res.data.list || res.data)) ||
            []
          if (Array.isArray(list)) {
            const processedData = this.processTop10Data(list)
            this.listTop10OriginalData = [...processedData]
            this.listTop10Data = [...processedData]
          } else {
            this.generateMockTop10Data()
          }
        } catch (error) {
          console.error('获取风险Top10数据失败:', error)
          this.generateMockTop10Data()
        }
      },

      // 获取催办情况数据
      async fetchList1Data() {
        try {
          const res = await getUnreadReminderList()
          if (res.code === 1 && res.data) {
            const processedData = this.processList1Data(res.data)
            this.list1OriginalData = [...processedData]
            // 只保留一份数据，通过 shift/push 实现循环
            this.list1Data = [...processedData]
          }
        } catch (error) {
          console.error('获取催办情况数据失败:', error)
          // 失败时使用模拟数据
          this.generateMockList1Data()
        }
      },

      // 获取月度评估情况数据
      async fetchList2Data() {
        try {
          console.log('🔵 开始调用月度评估接口...')
          const res = await getMonthlyEvaluationList()
          console.log('🔵 月度评估接口返回:', res)
          if (res.code === 1 && res.data) {
            const processedData = this.processList2Data(res.data)
            console.log('🔵 处理后的月度评估数据:', processedData)
            this.list2OriginalData = [...processedData]
            // 只保留一份数据，通过 shift/push 实现循环
            this.list2Data = [...processedData]
          } else {
            console.warn('⚠️ 月度评估接口返回数据格式不正确，使用模拟数据')
            this.generateMockList2Data()
          }
        } catch (error) {
          console.error('❌ 获取月度评估情况数据失败:', error)
          // 失败时使用模拟数据
          this.generateMockList2Data()
        }
      },

      // 获取风险数据库一览表数据
      async fetchList3Data() {
        try {
          console.log('🟢 开始调用风险数据库一览表接口...')
          const res = await getRiskDatabaseList()
          console.log('🟢 风险数据库一览表接口返回:', res)
          if (res.code === 1 && res.data) {
            const processedData = this.processList3Data(res.data)
            console.log('🟢 处理后的风险数据库数据:', processedData)
            this.list3OriginalData = [...processedData]
            // 只保留一份数据，通过 shift/push 实现循环
            this.list3Data = [...processedData]
          } else {
            console.warn(
              '⚠️ 风险数据库一览表接口返回数据格式不正确，使用模拟数据'
            )
            this.generateMockList3Data()
          }
        } catch (error) {
          console.error('❌ 获取风险数据库一览表数据失败:', error)
          // 失败时使用模拟数据
          this.generateMockList3Data()
        }
      },

      // 获取风险审查情况分析数据
      async fetchChart1Data() {
        try {
          console.log('🟡 开始调用风险审查情况分析接口...')
          const res = await getRiskReviewAnalysis()
          console.log('🟡 风险审查情况分析接口返回:', res)
          if (res.code === 1 && res.data) {
            this.chart1Data = res.data || []
            console.log('🟡 风险审查情况分析数据:', this.chart1Data)
          } else {
            console.warn(
              '⚠️ 风险审查情况分析接口返回数据格式不正确，使用模拟数据'
            )
            this.generateMockChart1Data()
          }
        } catch (error) {
          console.error('❌ 获取风险审查情况分析数据失败:', error)
          // 失败时使用模拟数据
          this.generateMockChart1Data()
        }
      },

      // 获取右侧饼图数据(风险措施状态)
      async fetchChart2RightData() {
        try {
          console.log('🔵 开始调用风险措施状态统计接口...')
          const res = await getRiskMeasureStatus()
          console.log('🔵 风险措施状态统计接口返回:', res)
          if (res.code === 1 && res.data) {
            this.chart2RightData = res.data
            console.log('🔵 风险措施状态统计数据:', this.chart2RightData)
          } else {
            console.warn(
              '⚠️ 风险措施状态统计接口返回数据格式不正确，使用模拟数据'
            )
            this.generateMockChart2RightData()
          }
        } catch (error) {
          console.error('❌ 获取风险措施状态统计数据失败:', error)
          // 失败时使用模拟数据
          this.generateMockChart2RightData()
        }
      },

      // 获取左侧饼图数据(风险完成情况)
      async fetchChart2LeftData() {
        try {
          console.log('🟢 开始调用风险完成情况统计接口...')
          const res = await getRiskCompletion()
          console.log('🟢 风险完成情况统计接口返回:', res)
          if (res.code === 1 && res.data) {
            // 将后端返回的数据转换为前端需要的格式
            const dataList = res.data || []
            let closedCount = 0
            let unclosedCount = 0

            dataList.forEach((item) => {
              if (item.gb === 0) {
                closedCount = item.count || 0
              } else if (item.gb === 1) {
                unclosedCount = item.count || 0
              }
            })

            this.chart2LeftData = {
              closedCount: closedCount, // 已关闭
              unclosedCount: unclosedCount, // 未关闭
            }
            console.log('🟢 风险完成情况统计数据:', this.chart2LeftData)
          } else {
            console.warn(
              '⚠️ 风险完成情况统计接口返回数据格式不正确，使用模拟数据'
            )
            this.generateMockChart2LeftData()
          }
        } catch (error) {
          console.error('❌ 获取风险完成情况统计数据失败:', error)
          // 失败时使用模拟数据
          this.generateMockChart2LeftData()
        }
      },

      // 生成右侧备用模拟数据(风险措施状态)
      generateMockChart2RightData() {
        this.chart2RightData = {
          nullRisklevelCount: Math.floor(Math.random() * 50) + 20,
          notNullRisklevelCount: Math.floor(Math.random() * 80) + 30,
        }
      },

      // 生成左侧备用模拟数据(风险完成情况)
      generateMockChart2LeftData() {
        this.chart2LeftData = {
          closedCount: Math.floor(Math.random() * 30) + 10, // 已关闭
          unclosedCount: Math.floor(Math.random() * 50) + 20, // 未关闭
        }
      },

      // 获取风险事件数统计数据
      async fetchChart3Data() {
        try {
          console.log('🟣 开始调用风险事件数统计接口...')
          const res = await getRiskEventCount()
          console.log('🟣 风险事件数统计接口返回:', res)
          if (res.code === 1 && res.data) {
            this.chart3Data = res.data || []
            console.log('🟣 风险事件数统计数据:', this.chart3Data)
          } else {
            console.warn(
              '⚠️ 风险事件数统计接口返回数据格式不正确，使用模拟数据'
            )
            this.generateMockChart3Data()
          }
        } catch (error) {
          console.error('❌ 获取风险事件数统计数据失败:', error)
          // 失败时使用模拟数据
          this.generateMockChart3Data()
        }
      },

      // 处理催办情况数据
      processList1Data(data) {
        const rawData = data || []

        const formatDateTime = (dateStr) => {
          if (!dateStr) return '无时间'
          return dateStr
        }

        return rawData.map((item) => {
          return {
            type1: item.nr || '无内容', // 通知内容
            type2: item.lx || '其他类型', // 类型（后端已处理映射）
            type3: item.xfr || '自动催办', // 下发人
            type4: formatDateTime(item.xfsj), // 下发时间
          }
        })
      },

      // 处理风险Top10数据
      processTop10Data(data) {
        const rawData = data || []

        const sorted = [...rawData].sort(
          (a, b) => Number(a.riskorder || 999) - Number(b.riskorder || 999)
        )

        return sorted.slice(0, 10).map((item) => {
          return {
            ...item,
            riskdes: item.riskdes || '--',
            riskchange: item.riskchange,
            orgname: item.orgname || '--',
            field6: item.field6 || '--',
          }
        })
      },

      formatRiskChange(value) {
        const levelMap = {
          1: '升高',
          2: '持平',
          3: '下降',
        }
        return levelMap[value] || value || '--'
      },

      // 处理月度评估情况数据
      processList2Data(data) {
        const rawData = data || []

        const getRiskStatusText = (status) => {
          switch (status) {
            case 0:
              return '已关闭'
            case 1:
              return '未关闭'
            case 2:
              return '开启'
            default:
              return '-'
          }
        }

        const getRiskChangeText = (change) => {
          // change 现在是字符串类型
          const changeStr = String(change || '0')
          switch (changeStr) {
            case '1':
              return '升高'
            case '2':
              return '持平'
            case '3':
              return '下降'
            default:
              return '未评估'
          }
        }

        return rawData.map((item) => {
          return {
            risknumber: item.risknumber || '-',
            riskname: item.riskname || '-',
            unitname: item.unitname || '-',
            riskcreatedt: item.riskcreatedt
              ? item.riskcreatedt.slice(0, 10)
              : '-',
            riskstatusText: getRiskStatusText(item.riskstatus),
            riskchangeText: getRiskChangeText(item.riskchange),
            copingcount: item.copingcount || 0,
          }
        })
      },

      // 处理风险数据库一览表数据
      processList3Data(data) {
        const rawData = data || []

        return rawData.map((item) => {
          return {
            riskcatname: item.riskcatname || '-',
            totalCount: item.totalCount || 0,
            completedCount: item.completedCount || 0,
            uncompletedCount: item.uncompletedCount || 0,
          }
        })
      },

      // 生成模拟催办数据（备用）
      generateMockList1Data() {
        const mockList1 = []
        const contents = [
          '请及时完成风险评估报告',
          '月度风险管控措施需要更新',
          '风险隐患排查工作待完成',
          '风险应急预案需要审核',
          '风险监测数据需要填报',
        ]
        const types = [
          '月度评估-一体化管控措施',
          '风险排查',
          '应急预案',
          '数据填报',
        ]
        const persons = ['张三', '李四', '王五', '自动催办']

        for (let i = 0; i < 15; i++) {
          mockList1.push({
            type1: contents[i % contents.length],
            type2: types[i % types.length],
            type3: persons[i % persons.length],
            type4: new Date(Date.now() - i * 3600000).toLocaleString('zh-CN'),
          })
        }
        this.list1OriginalData = [...mockList1]
        this.list1Data = [...mockList1]
      },

      // 生成模拟风险Top10数据（备用）
      generateMockTop10Data() {
        const riskPool = [
          '资金流动性波动风险',
          '供应链关键节点中断风险',
          '重点项目合规执行风险',
          '重大合同履约偏差风险',
          '关键信息系统稳定性风险',
          '应收账款回收进度风险',
          '核心岗位人员流失风险',
          '投资项目收益偏离风险',
          '市场价格剧烈波动风险',
          '外部政策变化传导风险',
        ]
        const depts = [
          '财务部',
          '运营部',
          '法务部',
          '市场部',
          '技术部',
          '审计部',
        ]
        const leaders = ['张三', '李四', '王五', '赵六', '孙七', '周八']
        const changes = ['升高', '持平', '下降']

        const list = riskPool.map((riskdes, index) => ({
          riskdes,
          riskchange: changes[index % changes.length],
          orgname: depts[index % depts.length],
          field6: leaders[index % leaders.length],
        }))

        this.listTop10OriginalData = [...list]
        this.listTop10Data = [...list]
      },

      // 生成模拟月度评估数据（备用）
      generateMockList2Data() {
        const mockList2 = []
        const riskNames = [
          '市场风险',
          '信用风险',
          '操作风险',
          '流动性风险',
          '合规风险',
        ]
        const companies = [
          '示例集团',
          '子公司A',
          '子公司B',
          '子公司C',
          '子公司D',
        ]
        const statuses = [0, 1]
        const changes = [1, 2, 3, null]

        for (let i = 0; i < 10; i++) {
          const status = statuses[i % 2]
          const change = changes[i % 4]
          mockList2.push({
            risknumber: `RISK-2025-${String(i + 1).padStart(4, '0')}`,
            riskname: riskNames[i % riskNames.length],
            unitname: companies[i % companies.length],
            riskcreatedt: new Date(
              Date.now() - i * 86400000
            ).toLocaleDateString('zh-CN'),
            riskstatusText: status === 0 ? '已关闭' : '未关闭',
            riskchangeText:
              change === 1
                ? '升高'
                : change === 2
                ? '持平'
                : change === 3
                ? '下降'
                : '未评估',
            copingcount: Math.floor(Math.random() * 10) + 1,
          })
        }
        this.list2OriginalData = [...mockList2]
        this.list2Data = [...mockList2]
      },

      // 生成模拟风险数据库数据（备用）
      generateMockList3Data() {
        const mockList3 = []
        const riskTypes = [
          '市场风险',
          '信用风险',
          '操作风险',
          '流动性风险',
          '合规风险',
          '战略风险',
          '声誉风险',
        ]

        for (let i = 0; i < 10; i++) {
          const total = Math.floor(Math.random() * 50) + 10
          const completed = Math.floor(Math.random() * total)
          const uncompleted = total - completed
          mockList3.push({
            riskcatname: riskTypes[i % riskTypes.length],
            totalCount: total,
            completedCount: completed,
            uncompletedCount: uncompleted,
          })
        }
        this.list3OriginalData = [...mockList3]
        this.list3Data = [...mockList3]
      },

      // 生成模拟风险审查情况分析数据（备用）
      generateMockChart1Data() {
        const mockData = []
        const depts = [
          '技术部',
          '财务部',
          '市场部',
          '运营部',
          '人力资源部',
          '行政部',
          '法务部',
        ]

        for (let i = 0; i < depts.length; i++) {
          mockData.push({
            orgname: depts[i],
            riskReviewCount: Math.floor(Math.random() * 50) + 10,
          })
        }
        this.chart1Data = mockData
      },

      // 生成模拟风险事件数统计数据（备用）
      generateMockChart3Data() {
        const mockData = []
        const depts = [
          '技术部',
          '财务部',
          '市场部',
          '运营部',
          '人力资源部',
          '行政部',
          '法务部',
          '研发部',
          '销售部',
          '客服部',
        ]

        for (let i = 0; i < 10; i++) {
          mockData.push({
            occurredDepartment: depts[i],
            category1: Math.floor(Math.random() * 30) + 5,
            category2: Math.floor(Math.random() * 15) + 2,
          })
        }
        this.chart3Data = mockData
      },

      // 生成其他模拟数据
      generateMockData() {
        // 其他图表的模拟数据可以在这里添加
      },

      // 表格滚动功能 - 参考审计分析大屏的实现
      startScroll0() {
        if (this.scrollTimer0) {
          clearInterval(this.scrollTimer0)
        }

        if (this.listTop10Data.length <= 5) {
          return
        }

        const tableBody = this.$refs.listTop10TableBody
        if (!tableBody) return

        const rowHeight = 42
        let accumulatedScroll = 0

        this.scrollTimer0 = setInterval(() => {
          accumulatedScroll += 1
          tableBody.style.transform = `translateY(-${accumulatedScroll}px)`

          if (accumulatedScroll >= rowHeight) {
            const firstItem = this.listTop10Data.shift()
            this.listTop10Data.push(firstItem)
            accumulatedScroll = 0
            this.$nextTick(() => {
              tableBody.style.transform = 'translateY(0)'
            })
          }
        }, this.scrollSpeed)
      },

      startScroll1() {
        // 清除之前的定时器
        if (this.scrollTimer1) {
          clearInterval(this.scrollTimer1)
        }

        // 如果数据少于等于可见行数，不需要滚动
        if (this.list1Data.length <= 5) {
          return
        }

        // 获取表格容器
        const tableBody = this.$refs.list1TableBody
        if (!tableBody) return

        const rowHeight = 42 // 每行高度（包括padding和border）
        let accumulatedScroll = 0

        this.scrollTimer1 = setInterval(() => {
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

      startScroll2() {
        if (this.scrollTimer2) {
          clearInterval(this.scrollTimer2)
        }

        if (this.list2Data.length <= 5) {
          return
        }

        const tableBody = this.$refs.list2TableBody
        if (!tableBody) return

        const rowHeight = 42
        let accumulatedScroll = 0

        this.scrollTimer2 = setInterval(() => {
          accumulatedScroll += 1
          tableBody.style.transform = `translateY(-${accumulatedScroll}px)`

          if (accumulatedScroll >= rowHeight) {
            const firstItem = this.list2Data.shift()
            this.list2Data.push(firstItem)
            accumulatedScroll = 0
            this.$nextTick(() => {
              tableBody.style.transform = 'translateY(0)'
            })
          }
        }, this.scrollSpeed)
      },

      startScroll3() {
        if (this.scrollTimer3) {
          clearInterval(this.scrollTimer3)
        }

        if (this.list3Data.length <= 5) {
          return
        }

        const tableBody = this.$refs.list3TableBody
        if (!tableBody) return

        const rowHeight = 42
        let accumulatedScroll = 0

        this.scrollTimer3 = setInterval(() => {
          accumulatedScroll += 1
          tableBody.style.transform = `translateY(-${accumulatedScroll}px)`

          if (accumulatedScroll >= rowHeight) {
            const firstItem = this.list3Data.shift()
            this.list3Data.push(firstItem)
            accumulatedScroll = 0
            this.$nextTick(() => {
              tableBody.style.transform = 'translateY(0)'
            })
          }
        }, this.scrollSpeed)
      },

      // 图表1: 风险审查情况分析表(近12个月) - 柱状图
      initChart1() {
        if (!this.$refs.chart1) return
        if (this.chart1) this.chart1.dispose()

        this.chart1 = echarts.init(this.$refs.chart1)

        // 从 chart1Data 中提取数据
        const xAxisData = this.chart1Data.map((item) => item.orgname || '未知')
        const seriesData = this.chart1Data.map(
          (item) => item.riskReviewCount || 0
        )

        const option = {
          tooltip: {
            trigger: 'axis',
            backgroundColor: 'rgba(0, 0, 0, 0.7)',
            borderColor: '#409eff',
            textStyle: { color: '#fff' },
            axisPointer: {
              type: 'shadow',
            },
          },
          grid: {
            left: '12%',
            right: '8%',
            top: '15%',
            bottom: '20%',
          },
          xAxis: {
            type: 'category',
            data: xAxisData,
            axisLine: { lineStyle: { color: '#409eff' } },
            axisLabel: {
              color: '#fff',
              rotate: 30,
              interval: 0,
              fontSize: 11,
              formatter: function (val) {
                return val.length > 4 ? val.slice(0, 4) + '…' : val
              },
            },
          },
          yAxis: {
            type: 'value',
            name: '审查次数',
            nameTextStyle: {
              color: '#fff',
              fontSize: 12,
            },
            axisLine: { lineStyle: { color: '#409eff' } },
            axisLabel: { color: '#fff' },
            splitLine: { lineStyle: { color: 'rgba(64, 158, 255, 0.2)' } },
          },
          series: [
            {
              name: '风险审查次数',
              type: 'bar',
              data: seriesData,
              barWidth: '50%',
              itemStyle: {
                color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                  { offset: 0, color: '#409eff' },
                  { offset: 1, color: '#1e90ff' },
                ]),
                borderRadius: [4, 4, 0, 0],
              },
              label: {
                show: true,
                position: 'top',
                color: '#fff',
                fontSize: 11,
              },
            },
          ],
        }
        this.chart1.setOption(option)
      },

      // 图表2左: 风险完成情况（饼图）
      initChart2Left() {
        if (!this.$refs.chart2Left) return
        if (this.chart2Left) this.chart2Left.dispose()

        this.chart2Left = echarts.init(this.$refs.chart2Left)

        const data = this.chart2LeftData || {}

        const option = {
          tooltip: {
            trigger: 'item',
            formatter: '{b}: {c} ({d}%)',
            backgroundColor: 'rgba(0, 0, 0, 0.7)',
            borderColor: '#67c23a',
            textStyle: { color: '#fff' },
          },
          legend: {
            orient: 'vertical',
            right: '5%',
            top: 'center',
            textStyle: { color: '#fff', fontSize: 10 },
          },
          series: [
            {
              name: '风险完成情况',
              type: 'pie',
              radius: ['35%', '65%'],
              center: ['35%', '50%'],
              data: [
                {
                  value: data.closedCount || 0,
                  name: '已关闭',
                  itemStyle: {
                    color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                      { offset: 0, color: '#67c23a' },
                      { offset: 1, color: '#85ce61' },
                    ]),
                  },
                },
                {
                  value: data.unclosedCount || 0,
                  name: '未关闭',
                  itemStyle: {
                    color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                      { offset: 0, color: '#e6a23c' },
                      { offset: 1, color: '#f0b854' },
                    ]),
                  },
                },
              ],
              emphasis: {
                itemStyle: {
                  shadowBlur: 10,
                  shadowOffsetX: 0,
                  shadowColor: 'rgba(0, 0, 0, 0.5)',
                },
              },
              label: {
                color: '#fff',
                fontSize: 10,
                formatter: '{b}\n{c} ({d}%)',
              },
            },
          ],
        }
        this.chart2Left.setOption(option)
      },

      // 图表2右: 风险措施状态（饼图）
      initChart2Right() {
        if (!this.$refs.chart2Right) return
        if (this.chart2Right) this.chart2Right.dispose()

        this.chart2Right = echarts.init(this.$refs.chart2Right)

        // 从 chart2RightData 中提取数据
        const completedCount = this.chart2RightData?.notNullRisklevelCount || 0
        const uncompletedCount = this.chart2RightData?.nullRisklevelCount || 0

        const option = {
          tooltip: {
            trigger: 'item',
            formatter: '{b}: {c} ({d}%)',
            backgroundColor: 'rgba(0, 0, 0, 0.7)',
            borderColor: '#409eff',
            textStyle: { color: '#fff' },
          },
          legend: {
            orient: 'vertical',
            right: '5%',
            top: 'center',
            textStyle: { color: '#fff', fontSize: 10 },
          },
          series: [
            {
              name: '风险措施状态',
              type: 'pie',
              radius: ['35%', '65%'],
              center: ['35%', '50%'],
              data: [
                {
                  value: completedCount,
                  name: '未完成措施数量',

                  itemStyle: {
                    color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                      { offset: 0, color: '#409eff' },
                      { offset: 1, color: '#66b1ff' },
                    ]),
                  },
                },
                {
                  value: uncompletedCount,
                  name: '完成措施数量',
                  itemStyle: {
                    color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                      { offset: 0, color: '#1e3a8a' },
                      { offset: 1, color: '#3b82f6' },
                    ]),
                  },
                },
              ],
              emphasis: {
                itemStyle: {
                  shadowBlur: 10,
                  shadowOffsetX: 0,
                  shadowColor: 'rgba(0, 0, 0, 0.5)',
                },
              },
              label: {
                color: '#fff',
                fontSize: 10,
                formatter: '{b}\n{c} ({d}%)',
              },
            },
          ],
        }
        this.chart2Right.setOption(option)
      },

      // 图表3: 风险事件数（柱状图）
      initChart3() {
        if (!this.$refs.chart3) return
        if (this.chart3) this.chart3.dispose()

        this.chart3 = echarts.init(this.$refs.chart3)

        // 从 chart3Data 中提取数据
        const xAxisData = this.chart3Data.map(
          (item) => item.occurredDepartment || '未知'
        )
        const category1Data = this.chart3Data.map((item) => item.category1 || 0)
        const category2Data = this.chart3Data.map((item) => item.category2 || 0)

        const option = {
          tooltip: {
            trigger: 'axis',
            backgroundColor: 'rgba(0, 0, 0, 0.7)',
            borderColor: '#409eff',
            textStyle: { color: '#fff' },
            axisPointer: {
              type: 'shadow',
            },
          },
          legend: {
            data: ['一般事件数', '重大事件数'],
            top: '5%',
            textStyle: {
              color: '#fff',
              fontSize: 12,
            },
          },
          grid: {
            left: '12%',
            right: '8%',
            top: '20%',
            bottom: '20%',
          },
          xAxis: {
            type: 'category',
            data: xAxisData,
            axisLine: { lineStyle: { color: '#409eff' } },
            axisLabel: {
              color: '#fff',
              rotate: 30,
              interval: 0,
              fontSize: 11,
              formatter: function (val) {
                return val.length > 4 ? val.slice(0, 4) + '…' : val
              },
            },
          },
          yAxis: {
            type: 'value',
            name: '事件数',
            nameTextStyle: {
              color: '#fff',
              fontSize: 12,
            },
            axisLine: { lineStyle: { color: '#409eff' } },
            axisLabel: { color: '#fff' },
            splitLine: { lineStyle: { color: 'rgba(64, 158, 255, 0.2)' } },
          },
          series: [
            {
              name: '一般事件数',
              type: 'bar',
              data: category1Data,
              barWidth: '35%',
              itemStyle: {
                color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                  { offset: 0, color: '#409eff' },
                  { offset: 1, color: '#66b1ff' },
                ]),
                borderRadius: [4, 4, 0, 0],
              },
              label: {
                show: true,
                position: 'top',
                color: '#fff',
                fontSize: 10,
              },
            },
            {
              name: '重大事件数',
              type: 'bar',
              data: category2Data,
              barWidth: '35%',
              itemStyle: {
                color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                  { offset: 0, color: '#1e3a8a' },
                  { offset: 1, color: '#3b82f6' },
                ]),
                borderRadius: [4, 4, 0, 0],
              },
              label: {
                show: true,
                position: 'top',
                color: '#fff',
                fontSize: 10,
              },
            },
          ],
        }
        this.chart3.setOption(option)
      },
    },
  }
</script>

<style lang="scss" scoped>
  .risk-screen-display {
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
          color: #ffffff;
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
      gap: 15px;
      padding: 20px;
      overflow: hidden;
      position: relative;
      z-index: 1;

      .top10-panel {
        flex: 0 0 clamp(220px, 27vh, 320px);
        min-height: 0;

        .data-table .table-body {
          overflow-y: auto;
          overflow-x: hidden;
        }
      }

      .main-columns {
        flex: 1;
        display: flex;
        gap: 20px;
        overflow: hidden;
        min-height: 0;

        // 左侧列
        .left-column {
          flex: 1;
          display: flex;
          flex-direction: column;
          gap: 15px;
          overflow: hidden;

          .panel-box {
            flex: 1;
            min-height: 0;
          }

          // 左上：催办情况
          .panel-box:nth-child(1) {
            flex: 0.8;
          }

          // 左中：月度评估情况一览表（加长）
          .panel-box:nth-child(2) {
            flex: 1.5;
          }

          // 左下：风险趋势分析
          .panel-box:nth-child(3) {
            flex: 1;
          }
        }

        // 右侧列
        .right-column {
          flex: 1;
          display: flex;
          flex-direction: column;
          gap: 15px;
          overflow: hidden;

          .panel-box {
            flex: 1;
            min-height: 0;
          }

          // 右上：风险处理情况
          .panel-box:nth-child(1) {
            flex: 0.8;
          }

          // 右中：风险类型统计（缩短）
          .panel-box:nth-child(2) {
            flex: 0.7;
          }

          // 右下：风险状态分布
          .panel-box:nth-child(3) {
            flex: 1;
          }
        }
      }

      // 右中: 双饼图面板特殊样式
      .main-columns .right-column .panel-box:nth-child(2).dual-chart-panel {
        .dual-chart-container {
          flex: 1;
          display: flex;
          gap: 10px;
          min-height: 0;
          padding: 0 5px;
        }

        .chart-wrapper {
          flex: 1;
          display: flex;
          flex-direction: column;
          min-width: 0;
          overflow: hidden;
        }

        .sub-chart-title {
          text-align: center;
          font-size: 13px;
          color: #409eff;
          margin-bottom: 8px;
          font-weight: 500;
          padding-bottom: 5px;
          border-bottom: 1px solid rgba(64, 158, 255, 0.15);
        }

        .chart-wrapper .chart {
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
            min-height: 41px;
            box-sizing: border-box;
            font-size: 12px;

            &:hover {
              background: rgba(0, 212, 255, 0.1);
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
      }

      // 左上表格列宽 (催办情况)
      .main-columns .left-column .panel-box:nth-child(1) {
        .th-1,
        .td-1 {
          flex: 0 0 25%;
        } // 通知内容
        .th-2,
        .td-2 {
          flex: 0 0 35%;
        } // 类型（最大）
        .th-3,
        .td-3 {
          flex: 0 0 15%;
        } // 下发人（最小）
        .th-4,
        .td-4 {
          flex: 0 0 25%;
        } // 下发时间
      }

      // 左中表格列宽 (月度评估情况一览表)
      .main-columns .left-column .panel-box:nth-child(2) {
        .th-1,
        .td-1 {
          flex: 0 0 12%;
        } // 风险编号
        .th-2,
        .td-2 {
          flex: 0 0 18%;
        } // 风险名称
        .th-3,
        .td-3 {
          flex: 0 0 18%;
        } // 所属公司
        .th-4,
        .td-4 {
          flex: 0 0 16%;
        } // 创建时间
        .th-5,
        .td-5 {
          flex: 0 0 12%;
        } // 是否关闭
        .th-6,
        .td-6 {
          flex: 0 0 12%;
        } // 趋势
        .th-7,
        .td-7 {
          flex: 0 0 12%;
        } // 控制措施数
      }

      // 右上表格列宽 (风险数据库一览表)
      .main-columns .right-column .panel-box:nth-child(1) {
        .th-1,
        .td-1 {
          flex: 0 0 40%;
        } // 风险类型名称
        .th-2,
        .td-2 {
          flex: 0 0 20%;
        } // 措施数量
        .th-3,
        .td-3 {
          flex: 0 0 20%;
        } // 完成措施数量
        .th-4,
        .td-4 {
          flex: 0 0 20%;
        } // 未完成措施数量
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

      // 第一行 Top10 列宽
      .top10-table {
        .t10-th-1,
        .t10-td-1 {
          flex: 0 0 8%;
        }
        .t10-th-2,
        .t10-td-2 {
          flex: 0 0 42%;
        }
        .t10-th-3,
        .t10-td-3 {
          flex: 0 0 14%;
        }
        .t10-th-4,
        .t10-td-4 {
          flex: 0 0 18%;
        }
        .t10-th-5,
        .t10-td-5 {
          flex: 0 0 18%;
        }
      }
    }
  }

  // 自定义滚动容器样式 - 隐藏滚动条，使用原生滚动
  ::v-deep .custom-scroll {
    // Webkit浏览器
    &::-webkit-scrollbar {
      width: 0 !important;
      height: 0 !important;
      background: transparent !important;
    }

    // Firefox浏览器
    &::-webkit-scrollbar {
      width: 0 !important;
      height: 0 !important;
      background: transparent !important;
    }

    // 滚动条按钮样式
    &::-webkit-scrollbar-button {
      background: transparent !important;
      display: none !important;
    }

    &::-webkit-scrollbar-thumb {
      background: transparent !important;
      display: none !important;
    }

    // 鼠标样式
    &::-webkit-scrollbar-track {
      background: transparent !important;
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

    // 向下的小三角箭头（left 由内联样式动态控制）
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
