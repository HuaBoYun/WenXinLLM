<template>
  <div>
    <div class="stat-cards-row">
      <!-- 1. 环形饼图 -->
      <!-- <div class="stat-card card-pie narrow">
        <div ref="pieChart" class="chart-box"></div>
      </div> -->
      <!-- 2. 分类进度条 -->
      <!-- <div class="stat-card card-bar wide1">
        <div ref="barGroupChart" class="chart-box"></div>
      </div> -->
      <!-- 3. 仪表盘1 -->
      <div class="stat-card card-gauge narrow">
        <div ref="gauge1" class="chart-box gauge-box"></div>
        <div class="gauge-label">追责项目</div>
      </div>
      <!-- 4. 仪表盘2 -->
      <div class="stat-card card-gauge narrow">
        <div ref="gauge2" class="chart-box gauge-box"></div>
        <div class="gauge-label">项目总数</div>
      </div>
      <!-- 5. 仪表盘3 -->
      <div class="stat-card card-gauge narrow">
        <div ref="gauge3" class="chart-box gauge-box"></div>
        <div class="gauge-label">追责率</div>
      </div>
      <!-- 6. 仪表盘4 -->
      <div class="stat-card card-gauge narrow">
        <div ref="gauge4" class="chart-box gauge-box"></div>
        <div class="gauge-label">追责完成率</div>
      </div>
      <!-- 6. 右侧分组条形统计 -->
      <!-- <div class="stat-card card-rightbar wide">
        <div class="right-bar-group2">
          <div class="bar-section2">
            <div class="bar-title2">
              <span>未启动</span>
              <span class="bar-total2">43</span>
            </div>
            <div class="bar-row2">
              <div class="bar-label2">00尚未分配</div>
              <div class="bar-legend2">
                <div class="bar-bar2 bar-green2" style="width: 85%"></div>
                <span class="bar-value2">23</span>
              </div>
            </div>
            <div class="bar-row2">
              <div class="bar-label2">01下月应启</div>
              <div class="bar-legend2">
                <div class="bar-bar2 bar-blue2" style="width: 40%"></div>
                <span class="bar-value2">11</span>
              </div>
            </div>
            <div class="bar-row2">
              <div class="bar-label2">02本月应启</div>
              <div class="bar-legend2">
                <div class="bar-bar2 bar-yellow2" style="width: 22%"></div>
                <span class="bar-value2">6</span>
              </div>
            </div>
            <div class="bar-row2">
              <div class="bar-label2">03滞后轮次</div>
              <div class="bar-legend2">
                <div class="bar-bar2 bar-orange2" style="width: 10%"></div>
                <span class="bar-value2">3</span>
              </div>
            </div>
          </div>
          <div class="bar-section2">
            <div class="bar-title3">
              <span>已启动</span>
              <span class="bar-total2">119</span>
            </div>
            <div class="bar-row2">
              <div class="bar-label2">10本月已启</div>
              <div class="bar-legend2">
                <div class="bar-bar2 bar-green2" style="width: 10%"></div>
                <span class="bar-value2">9</span>
              </div>
            </div>
            <div class="bar-row2">
              <div class="bar-label2">11前月已启</div>
              <div class="bar-legend2">
                <div class="bar-bar2 bar-blue2" style="width: 90%"></div>
                <span class="bar-value2">110</span>
              </div>
            </div>
          </div>
          <div class="bar-summary2">
            本月应启动：
            <span class="bar-summary-num2">15</span>
            其中，本月应启动未启
            <span class="bar-summary-num2">6</span>
          </div>
        </div>
      </div> -->
    </div>
    <!-- 新增：仪表盘下面的两个柱状图 -->
    <div class="stat-cards-row">
      <div class="stat-card card-bar-chart wide">
        <div class="chart-title">追责项目完成情况</div>
        <div ref="barChart3" class="chart-box"></div>
      </div>

      <!-- 8. 柱状图2 -->
      <div class="stat-card card-bar-chart wide">
        <div class="chart-title">部门追责情况</div>
        <div ref="barChart2" class="chart-box"></div>
      </div>
    </div>
    <div class="stat-cards-row">
      <!-- 9. 柱状图3 -->
      <!-- 7. 柱状图1 -->
      <div class="stat-card card-bar-chart wide">
        <div class="chart-title">项目运行情况</div>
        <div ref="barChart1" class="chart-box"></div>
      </div>
    </div>
    <Modal ref="modal" />
  </div>
</template>

<script>
  import * as echarts from 'echarts'
  import {
    getProjectRunData,
    getPersonnelData,
    getOtherData,
  } from '@/api/cwztfx'
  import Modal from './modal.vue'
  import { formatYear } from '@/utils'
  export default {
    components: {
      Modal,
    },
    props: {
      filters: {
        type: Object,
        default: () => ({}),
      },
    },
    data() {
      return {
        projectRunData: [],
        personnelData: [],
        year: formatYear(new Date()).toString(), // 动态获取当前年份的日期对象，如 Mon Jan 01 2024 00:00:00 GMT+0800
        month: '', // 月份
        otherData: {},
        type: {
          sjbgNum: '审计报告数量',
          sjzgNum: '审计整改数量',
          sqzbNum: '审前准备数量',
          xcssNum: '现场实施数量',
          ywcNum: '已完成数量',
        },
        type1: {
          staffQjNum: '请假',
          staffWpNum: '外派',
          staffZgxmnNum: '在岗项目中',
          staffZgxzNum: '在岗闲置',
        },
      }
    },
    watch: {
      filters: {
        handler(newVal) {
          this.year = formatYear(new Date(newVal.year)).toString()
          getOtherData({
            queryYear: this.year,
          }).then((res) => {
            this.otherData = res.data
            // this.initPie()
            // this.initBarGroup()
            this.initGauge()
            this.initRightBar()
            this.initBarChart1()
            // this.initBarChart2()
            this.initBarChart3()
          })
        },
      },
    },
    async mounted() {
      // 获取其他数据
      getOtherData().then((res) => {
        this.otherData = res.data
        // this.initPie()
        // this.initBarGroup()
        this.initGauge()
        this.initRightBar()
        this.initBarChart1()
        this.initBarChart2()
        this.initBarChart3()
      })
    },
    methods: {
      // 1. 环形饼图
      initPie() {
        const chart = echarts.init(this.$refs.pieChart)
        chart.setOption({
          color: ['#5bb974', '#b2e0c6'],
          series: [
            {
              type: 'pie',
              radius: ['65%', '85%'],
              avoidLabelOverlap: false,
              label: {
                show: true,
                position: 'outside',
                formatter: (params) => {
                  if (params.name === '任务') return `任务\\n25\\n(15.43%)`
                  if (params.name === '项目') return `项目\\n137\\n(84.57%)`
                  return ''
                },
                fontSize: 14,
                color: '#333',
              },
              data: [
                { value: 25, name: '任务' },
                { value: 137, name: '项目' },
              ],
            },
          ],
        })
      },
      // 2. 分类进度条
      initBarGroup() {
        const chart = echarts.init(this.$refs.barGroupChart)
        chart.setOption({
          grid: { left: 60, right: 20, top: 20, bottom: 20 },
          xAxis: { show: false },
          yAxis: {
            type: 'category',
            data: [
              '0未启动',
              '1审前准备',
              '2远程审计',
              '3现场实施',
              '4报告撰写',
              '5报告审理',
              '6项目完成',
            ],
            axisLabel: { color: '#333', fontSize: 14 },
          },
          series: [
            {
              type: 'bar',
              data: [43, 11, 5, 18, 12, 35, 38],
              itemStyle: {
                color: (params) => {
                  const colors = [
                    '#5bb974',
                    '#409eff',
                    '#f6c85f',
                    '#f08a24',
                    '#a084e8',
                    '#6ed2e6',
                    '#f6b85f',
                  ]
                  return colors[params.dataIndex]
                },
              },
              barWidth: 16,
              label: {
                show: true,
                position: 'right',
                color: '#333',
                fontWeight: 'bold',
              },
            },
          ],
        })
      },
      // 3-5. 三个仪表盘
      initGauge() {
        const gaugeData = [
          {
            ref: 'gauge1',
            value: this.otherData.yearBeginPlanExcuteRate,
            label: '年初计划执行率',
          },
          {
            ref: 'gauge2',
            value: this.otherData.auditFindQuesRate,
            label: '问题整改率',
          },
          {
            ref: 'gauge3',
            value: this.otherData.auditFindMoneRate,
            label: '问题金额整改率',
          },
          {
            ref: 'gauge4',
            value: this.otherData.auditadoptionTotalNum,
            label: '建议采纳率',
          },
        ]
        gaugeData.forEach((g, idx) => {
          const chart = echarts.init(this.$refs[g.ref])
          chart.setOption({
            series: [
              {
                type: 'gauge',
                min: 0,
                max: 100,
                splitNumber: 4,
                radius: '100%',
                axisLine: { lineStyle: { width: 10, color: [[1, '#5bb974']] } },
                pointer: { width: 4, length: '60%' },
                progress: {
                  show: true,
                  width: 10,
                  itemStyle: { color: '#5bb974' },
                },
                detail: {
                  valueAnimation: true,
                  formatter: '{value}%',
                  fontSize: 14,
                  color: '#333',
                  offsetCenter: [0, '70%'],
                },
                axisLabel: { color: '#333', fontSize: 12, distance: 12 },
                data: [{ value: g.value }],
              },
            ],
          })
        })
      },
      // 6. 右侧分组条形统计
      initRightBar() {
        // This method is no longer needed as the right bar is now static HTML
      },
      // 7. 柱状图1
      initBarChart1() {
        getProjectRunData({
          queryYear: this.year,
        }).then((res) => {
          // 按照type的类型，转换res
          const data = Object.keys(this.type).map((key) => {
            return {
              name: this.type[key],
              value: res.data.data[key],
            }
          })

          const chart = echarts.init(this.$refs.barChart1)
          chart.setOption({
            grid: { left: 50, right: 20, top: 20, bottom: 20 },
            xAxis: {
              type: 'category',
              data: data.map((item) => item.name),
              axisLabel: { color: '#333', fontSize: 12 },
              axisLine: { lineStyle: { color: '#eee' } },
              axisTick: { show: false },
            },
            yAxis: {
              type: 'value',
              axisLabel: { color: '#333', fontSize: 12 },
              axisLine: { lineStyle: { color: '#eee' } },
              axisTick: { show: false },
              splitLine: { lineStyle: { color: '#eee' } },
            },
            series: [
              {
                type: 'bar',
                data: data.map((item) => item.value),
                itemStyle: {
                  color: '#409eff',
                },
                barWidth: 20,
                label: {
                  show: true,
                  position: 'top',
                  color: '#333',
                  fontSize: 12,
                },
              },
            ],
          })

          // 添加点击事件监听器
          chart.on('click', (params) => {
            this.$refs.modal.show()
          })
        })
      },
      // 8. 柱状图2
      initBarChart2() {
        getPersonnelData({
          queryYear: this.year,
        }).then((res) => {
          // 按照type的类型，转换res
          // const data = Object.keys(this.type1).map((key) => {
          //   return {
          //     name: this.type1[key],
          //     value: res.data.data[key],
          //   }
          // })
          const data = [
            {
              name: '1部门',
              value: 4,
            },
            {
              name: '2部门',
              value: 2,
            },
            {
              name: '3部门',
              value: 6,
            },
            {
              name: '4部门',
              value: 10,
            },
            {
              name: '5部门',
              value: 5,
            },
            {
              name: '6部门',
              value: 1,
            },
            {
              name: '7部门',
              value: 6,
            },
            {
              name: '8部门',
              value: 2,
            },
          ]
          const chart = echarts.init(this.$refs.barChart2)
          chart.setOption({
            grid: { left: 50, right: 20, top: 20, bottom: 20 },
            xAxis: {
              type: 'category',
              data: data.map((item) => item.name),
              axisLabel: { color: '#333', fontSize: 12 },
              axisLine: { lineStyle: { color: '#eee' } },
              axisTick: { show: false },
            },
            yAxis: {
              type: 'value',
              axisLabel: { color: '#333', fontSize: 12 },
              axisLine: { lineStyle: { color: '#eee' } },
              axisTick: { show: false },
              splitLine: { lineStyle: { color: '#eee' } },
            },
            series: [
              {
                type: 'bar',
                data: data.map((item) => item.value),
                itemStyle: {
                  color: '#5bb974',
                },
                barWidth: 20,
                label: {
                  show: true,
                  position: 'top',
                  color: '#333',
                  fontSize: 12,
                },
              },
            ],
          })

          // 添加点击事件监听器
          // chart.on('click', (params) => {
          //   if (params.componentType === 'series') {
          //     // 根据不同的变量类型跳转到不同的位置
          //     const dataIndex = params.dataIndex
          //     const urls = [
          //       '/zhgl/ryqjtz', // 请假
          //       '/zhgl/wprytz', // 外派
          //       '/zhgl/rytz?type=2', // 在岗项目中
          //       '/zhgl/rytz?type=1', // 在岗闲置
          //     ]

          //     if (urls[dataIndex]) {
          //       this.$router.push(urls[dataIndex])
          //     }
          //   }
          // })
        })
      },
      initBarChart3() {
        const chart = echarts.init(this.$refs.barChart3)
        chart.setOption({
          grid: { left: 50, right: 20, top: 20, bottom: 20 },
          xAxis: {
            type: 'category',
            data: [
              '追责问题数量',
              '追责问题金额',
              '已整改问题数量',
              '已整改问题金额',
            ],
            axisLabel: { color: '#333', fontSize: 12 },
            axisLine: { lineStyle: { color: '#eee' } },
            axisTick: { show: false },
          },
          yAxis: {
            type: 'value',
            axisLabel: { color: '#333', fontSize: 12, interval: 0 },
            axisLine: { lineStyle: { color: '#eee' } },
            axisTick: { show: false },
            splitLine: { lineStyle: { color: '#eee' } },
          },
          series: [
            {
              type: 'bar',
              data: [
                this.otherData.auditFindQuesNum,
                this.otherData.auditFindMoneyNum,
                this.otherData.auditRectQuesNum,
                this.otherData.auditQuesMoneyNum,
              ],
              itemStyle: {
                color: '#5bb974',
              },
              barWidth: 20,
              label: {
                show: true,
                position: 'top',
                color: '#333',
                fontSize: 12,
              },
            },
          ],
        })
      },
    },
  }
</script>

<style scoped>
  .stat-cards-row {
    display: flex;
    width: 100%;
    gap: 6px;
    margin: 6px 0;
  }
  .stat-card {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    min-width: 0;
    min-height: 240px;
    background: #f6fcff;
    border-radius: 8px;
    box-shadow: 0 2px 8px #b3e0fc33;
    padding: 18px 16px;
    box-sizing: border-box;
  }
  .stat-card.narrow {
    flex: 1.2 0;
  }
  .stat-card.wide {
    flex: 2.3 0;
  }
  .stat-card.wide1 {
    flex: 1.3 0;
  }
  .card-pie {
    flex: 1 1 0;
  }
  .card-bar {
    flex: 1.7 1 0;
  }
  .card-gauge {
    flex: 1 1 0;
  }
  .card-rightbar {
    flex: 2 1 0;
  }
  .card-bar-chart {
    flex: 1 1 0;
  }
  .chart-title {
    text-align: center;
    margin-bottom: 10px;
    font-size: 16px;
    color: #222;
    font-weight: bold;
  }
  .chart-box {
    width: 100%;
    height: 180px;
    min-width: 0;
  }
  .gauge-label {
    text-align: center;
    margin-top: 10px;
    font-size: 18px;
    color: #222;
    font-weight: bold;
  }
  .gauge-box {
    width: 80%;
    height: 140px;
    min-width: 0;
    margin: 0 auto;
  }
  .right-bar-group {
    width: 100%;
    background: #eaf6fa;
    padding: 8px 12px;
    border-radius: 8px;
  }
  .bar-section {
    padding-bottom: 8px;
    margin-bottom: 8px;
    border-bottom: 2px solid #bcdff1;
  }
  .bar-section:last-child,
  .bar-section-bottom {
    border-bottom: none;
    margin-bottom: 0;
    padding-bottom: 0;
  }
  .bar-title {
    font-weight: bold;
    font-size: 15px;
    color: #333;
    display: flex;
    justify-content: space-between;
    margin-bottom: 2px;
  }
  .bar-total {
    font-weight: bold;
    color: #333;
  }
  .bar-row {
    display: flex;
    align-items: center;
    margin-bottom: 4px;
  }
  .bar-label {
    width: 80px;
    font-size: 13px;
    color: #333;
  }
  .bar-bar {
    height: 18px;
    border-radius: 9px;
    margin: 0 6px;
    transition: width 0.3s;
    box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1) inset,
      0 1px 2px rgba(0, 0, 0, 0.08);
    background: #eee;
    position: relative;
  }
  .bar-green {
    background: linear-gradient(90deg, #e8f8ef 0%, #5bb974 60%, #388e3c 100%);
  }
  .bar-blue {
    background: linear-gradient(90deg, #eaf4fb 0%, #409eff 60%, #1a73e8 100%);
  }
  .bar-yellow {
    background: linear-gradient(90deg, #fffbe8 0%, #f6c85f 60%, #e6a800 100%);
  }
  .bar-orange {
    background: linear-gradient(90deg, #fff3e8 0%, #f08a24 60%, #c75b00 100%);
  }
  .bar-value {
    width: 24px;
    text-align: right;
    font-size: 13px;
    color: #333;
  }
  .bar-summary {
    margin-top: 6px;
    font-size: 14px;
    color: #333;
  }
  .bar-summary-num {
    font-size: 18px;
    font-weight: bold;
    color: #409eff;
    margin: 0 2px;
  }
  .right-bar-group2 {
    width: 100%;
    background: #f6fcff;
    border-radius: 16px;
    /* box-shadow: 0 2px 8px #b3e0fc33; */
    padding: 6px 8px 6px 8px;
  }
  .bar-section2 {
    border-bottom: 2.5px solid #e3eef7;
    margin-bottom: 12px;
    padding-bottom: 10px;
    position: relative;
  }
  .bar-section2:not(:last-child)::after {
    content: '';
    position: absolute;
    left: 12px;
    right: 12px;
    bottom: -2.5px;
    height: 2.5px;
    background: #e3eef7;
    border-radius: 2px;
    z-index: 1;
  }
  .bar-section2:last-child {
    border-bottom: none;
    margin-bottom: 0;
    padding-bottom: 0;
  }
  .bar-title2 {
    display: flex;
    justify-content: flex-end;
    gap: 3px;
    font-weight: bold;
    font-size: 12px;
    color: #222;
    margin-bottom: 6px;
    position: absolute;
    right: 8px;
    bottom: 4px;
  }
  .bar-title3 {
    display: flex;
    justify-content: flex-end;
    gap: 3px;
    font-weight: bold;
    font-size: 12px;
    color: #222;
    margin-bottom: 6px;
    position: absolute;
    right: 8px;
    top: -4px;
  }
  .bar-total2 {
    color: #222;
    font-weight: bold;
    font-size: 12px;
  }
  .bar-row2 {
    display: flex;
    align-items: center;
    margin-bottom: 8px;
  }
  .bar-label2 {
    flex: 0 0 90px;
    font-size: 12px;
    color: #222;
    text-align: left;
    line-height: 1.2;
  }
  .bar-legend2 {
    flex: 1;
    display: flex;
    align-items: center;
  }
  .bar-bar2 {
    height: 8px;
    /* border-radius: 4px; */
    margin-right: 8px;
    min-width: 24px;
    box-shadow: 0 2px 8px #b3e0fc33 inset;
  }
  .bar-green2 {
    background: linear-gradient(90deg, #e8f8ef 0%, #5bb974 60%, #388e3c 100%);
  }
  .bar-blue2 {
    background: linear-gradient(90deg, #eaf4fb 0%, #409eff 60%, #1a73e8 100%);
  }
  .bar-yellow2 {
    background: linear-gradient(90deg, #fffbe8 0%, #f6c85f 60%, #e6a800 100%);
  }
  .bar-orange2 {
    background: linear-gradient(90deg, #fff3e8 0%, #f08a24 60%, #c75b00 100%);
  }
  .bar-value2 {
    font-size: 15px;
    color: #222;
    font-weight: bold;
    min-width: 22px;
    text-align: right;
  }
  .bar-summary2 {
    margin-top: 14px;
    font-size: 15px;
    color: #222;
    text-align: left;
    padding-left: 2px;
  }
  .bar-summary-num2 {
    font-size: 18px;
    font-weight: bold;
    color: #2196f3;
    margin: 0 2px;
  }
</style>
