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
        <div class="title-main">用户画像</div>
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

    <!-- 主体内容 - 3行×3列网格布局 -->
    <div class="screen-body">
      <!-- 第1行第1列: 基础用户信息 - 占2列宽度 -->
      <div class="panel-box grid-col-2">
        <div class="panel-title">
          <i class="el-icon-user"></i>
          <span>基础用户信息</span>
        </div>
        <div class="data-table">
          <div class="table-header">
            <div class="th th-1">用户名</div>
            <div class="th th-2">所属部门</div>
            <div class="th th-3">入职时间</div>
            <div class="th th-4">用户标签</div>
          </div>
          <div class="table-body">
            <div ref="list1TableBody" class="scroll-container">
              <div
                v-for="(item, index) in list1Data"
                :key="'list1-' + index + '-' + item.type1"
                class="tr"
                @mouseenter="onRowEnter"
                @mouseleave="onRowLeave"
              >
                <div class="td td-1" @mouseenter="(e) => onCellEnter(e, item.type1)" @mouseleave="onCellLeave">{{ item.type1 }}</div>
                <div class="td td-2" @mouseenter="(e) => onCellEnter(e, item.type2)" @mouseleave="onCellLeave">{{ item.type2 }}</div>
                <div class="td td-3" @mouseenter="(e) => onCellEnter(e, item.type4)" @mouseleave="onCellLeave">{{ item.type4 }}</div>
                <div class="td td-4" @mouseenter="(e) => onCellEnter(e, item.type3)" @mouseleave="onCellLeave">{{ item.type3 }}</div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 第1行第2列: 风险、内控、审计相关维度 -->
      <div class="panel-box chart-panel">
        <div class="panel-title">
          <i class="el-icon-data-line"></i>
          <span>风险、内控、审计相关维度</span>
        </div>
        <div class="chart-container">
          <div ref="chart2" class="chart"></div>
        </div>
      </div>

      <!-- 第2行第1列: 内控测试缺陷程度 -->
      <div class="panel-box chart-panel">
        <div class="panel-title">
          <i class="el-icon-warning-outline"></i>
          <span>内控测试缺陷程度</span>
        </div>
        <div class="chart-container">
          <div ref="chart3" class="chart"></div>
        </div>
      </div>

      <!-- 第2行第2列: 风险事件处理 -->
      <div class="panel-box chart-panel">
        <div class="panel-title">
          <i class="el-icon-warning"></i>
          <span>风险事件处理</span>
        </div>
        <div class="chart-container">
          <div ref="chart4" class="chart"></div>
        </div>
      </div>

      <!-- 第2行第3列: 审计问题数量 -->
      <div class="panel-box chart-panel">
        <div class="panel-title">
          <i class="el-icon-data-analysis"></i>
          <span>审计问题数量</span>
        </div>
        <div class="chart-container">
          <div ref="chart5" class="chart"></div>
        </div>
      </div>

      <!-- 第3行第1列: 内控缺陷整改跟进 -->
      <div class="panel-box chart-panel">
        <div class="panel-title">
          <i class="el-icon-circle-check"></i>
          <span>内控缺陷整改跟进</span>
        </div>
        <div class="chart-container">
          <div ref="chart6" class="chart"></div>
        </div>
      </div>

      <!-- 第3行第2列: 风险预警响应率 -->
      <div class="panel-box risk-warning-panel">
        <div class="panel-title">
          <i class="el-icon-warning"></i>
          <span>风险预警响应率</span>
        </div>
        <div class="gauge-container">
          <div ref="gaugeChart" class="gauge-chart"></div>
        </div>
      </div>

      <!-- 第3行第3列: 审计问题整改验证完成率 -->
      <div class="panel-box progress-panel">
        <div class="panel-title">
          <i class="el-icon-finished"></i>
          <span>审计问题整改验证完成率</span>
        </div>
        <div class="progress-container">
          <div class="progress-info">
            <div class="progress-label">{{ auditVerificationData.label }}</div>
            <div class="progress-stats">
              <span class="completed">已完成: {{ auditVerificationData.data }}</span>
              <span class="total">总数: {{ auditVerificationData.value }}</span>
            </div>
          </div>
          <div class="progress-bar-wrapper">
            <el-progress
              :percentage="auditVerificationData.rate"
              :stroke-width="26"
              :color="progressColor"
              :text-inside="true"
            ></el-progress>
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
  // eslint-disable-next-line no-unused-vars
  import * as echarts from 'echarts'
  import { getRiskWarningResponse, getBasicUserInfo, getRiskEventHandling, getInternalControlDefect, getRiskAuditDimension, getAuditIssueCount, getInternalControlRectification, getAuditIssueVerificationRate } from '@/api/setting/dataGrowthTrend'

  export default {
    name: 'UserPortraitScreen',
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

        // 7个部分的数据 - 留空,等待新接口
        list1Data: [],
        list1OriginalData: [],
        list2Data: [],
        list3Data: [],
        list4Data: { categories: [], series: [] },
        list5Data: { categories: [], series: [] },
        list6Data: [],
        list6OriginalData: [],
        list7Data: [],
        list7OriginalData: [],

        // 风险预警响应率数据
        riskWarningData: {
          label: '风险预警响应率',
          value: 0,
          data: 0,
          responseRate: 0
        },

        // 风险事件处理数据
        riskEventHandlingData: [],

        // 内控测试缺陷程度数据
        internalControlDefectData: [],

        // 风险、内控、审计相关维度数据
        riskAuditDimensionData: { categories: [], series: [] },

        // 审计问题数量数据
        auditIssueCountData: { categories: [], series: [] },

        // 内控缺陷整改跟进数据
        internalControlRectificationData: [],

        // 审计问题整改验证完成率数据
        auditVerificationData: {
          label: '审计问题整改验证完成率',
          value: 0,
          data: 0,
          rate: 0
        },

        // 6个图表实例
        chart2: null,
        chart3: null,
        chart4: null,
        chart5: null,
        chart6: null,
        gaugeChart: null,

        // 滚动相关
        scrollTimer: null,
        scrollTimer6: null,
        scrollTimer7: null,
        scrollSpeed: 50,

        // 气泡提示
        tooltip: { visible: false, top: 0, left: 0, arrowLeft: 0, text: '' },
      }
    },
    computed: {
      // 进度条颜色
      progressColor() {
        const rate = this.auditVerificationData.rate || 0
        if (rate >= 80) {
          return '#67C23A' // 绿色
        } else if (rate >= 50) {
          return '#E6A23C' // 橙色
        } else {
          return '#F56C6C' // 红色
        }
      }
    },
    mounted() {
      this.updateTime()
      this.timer = setInterval(this.updateTime, 1000)

      // 初始化年份选项
      this.initYearOptions()

      // 加载数据
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
      if (this.chart6) this.chart6.dispose()
      if (this.gaugeChart) this.gaugeChart.dispose()
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
        console.log('UserPortraitScreen: 开始加载数据')
        // 获取基础用户信息
        await this.fetchBasicUserInfo()
        // 获取风险、内控、审计相关维度
        await this.fetchRiskAuditDimension()
        // 获取内控测试缺陷程度
        await this.fetchInternalControlDefect()
        // 获取风险事件处理
        await this.fetchRiskEventHandling()
        // 获取审计问题数量
        await this.fetchAuditIssueCount()
        // 获取内控缺陷整改跟进
        await this.fetchInternalControlRectification()
        // 获取审计问题整改验证完成率
        await this.fetchAuditIssueVerificationRate()
        // 获取风险预警响应率
        await this.fetchRiskWarningData()
      },

      // 获取基础用户信息
      async fetchBasicUserInfo() {
        try {
          const response = await getBasicUserInfo()
          console.log('基础用户信息响应:', response)
          if (response.code === 1 && response.data) {
            this.list1OriginalData = response.data
            this.list1Data = [...response.data]
            // 启动滚动
            this.$nextTick(() => {
              this.startAutoScroll()
            })
          } else {
            console.error('获取基础用户信息失败:', response.msg || response.message)
          }
        } catch (error) {
          console.error('获取基础用户信息异常:', error)
        }
      },

      // 获取风险、内控、审计相关维度
      async fetchRiskAuditDimension() {
        try {
          const response = await getRiskAuditDimension()
          console.log('风险、内控、审计相关维度响应:', response)
          if (response.code === 1 && response.data) {
            this.riskAuditDimensionData = response.data
            // 初始化折线图
            this.$nextTick(() => {
              this.initChart2()
            })
          } else {
            console.error('获取风险、内控、审计相关维度失败:', response.msg || response.message)
          }
        } catch (error) {
          console.error('获取风险、内控、审计相关维度异常:', error)
        }
      },

      // 获取内控测试缺陷程度
      async fetchInternalControlDefect() {
        try {
          const response = await getInternalControlDefect()
          console.log('内控测试缺陷程度响应:', response)
          if (response.code === 1 && response.data) {
            this.internalControlDefectData = response.data
            // 初始化饼图
            this.$nextTick(() => {
              this.initChart3()
            })
          } else {
            console.error('获取内控测试缺陷程度失败:', response.msg || response.message)
          }
        } catch (error) {
          console.error('获取内控测试缺陷程度异常:', error)
        }
      },

      // 获取风险事件处理
      async fetchRiskEventHandling() {
        try {
          const response = await getRiskEventHandling()
          console.log('风险事件处理响应:', response)
          if (response.code === 1 && response.data) {
            this.riskEventHandlingData = response.data
            // 初始化饼图
            this.$nextTick(() => {
              this.initChart4()
            })
          } else {
            console.error('获取风险事件处理失败:', response.msg || response.message)
          }
        } catch (error) {
          console.error('获取风险事件处理异常:', error)
        }
      },

      // 获取审计问题数量
      async fetchAuditIssueCount() {
        try {
          const response = await getAuditIssueCount()
          console.log('审计问题数量响应:', response)
          if (response.code === 1 && response.data) {
            this.auditIssueCountData = response.data
            // 初始化折线图
            this.$nextTick(() => {
              this.initChart5()
            })
          } else {
            console.error('获取审计问题数量失败:', response.msg || response.message)
          }
        } catch (error) {
          console.error('获取审计问题数量异常:', error)
        }
      },

      // 获取内控缺陷整改跟进
      async fetchInternalControlRectification() {
        try {
          const response = await getInternalControlRectification()
          console.log('内控缺陷整改跟进响应:', response)
          if (response.code === 1 && response.data) {
            this.internalControlRectificationData = response.data
            // 初始化饼图
            this.$nextTick(() => {
              this.initChart6()
            })
          } else {
            console.error('获取内控缺陷整改跟进失败:', response.msg || response.message)
          }
        } catch (error) {
          console.error('获取内控缺陷整改跟进异常:', error)
        }
      },

      // 获取审计问题整改验证完成率
      async fetchAuditIssueVerificationRate() {
        try {
          const response = await getAuditIssueVerificationRate()
          console.log('审计问题整改验证完成率响应:', response)
          if (response.code === 1 && response.data) {
            this.auditVerificationData = response.data
          } else {
            console.error('获取审计问题整改验证完成率失败:', response.msg || response.message)
          }
        } catch (error) {
          console.error('获取审计问题整改验证完成率异常:', error)
        }
      },

      // 启动自动滚动（基础用户信息列表）
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

      // 获取风险预警响应率数据
      async fetchRiskWarningData() {
        try {
          const response = await getRiskWarningResponse()
          console.log('风险预警响应率响应:', response)
          if (response.code === 1 && response.data) {
            this.riskWarningData = response.data
            // 初始化仪表盘图表
            this.$nextTick(() => {
              setTimeout(() => {
                this.initGaugeChart()
              }, 500)
            })
          } else {
            console.error('获取风险预警响应率失败:', response.msg || response.message)
          }
        } catch (error) {
          console.error('获取风险预警响应率异常:', error)
        }
      },

      // 初始化风险、内控、审计相关维度折线图（chart2）
      initChart2() {
        if (!this.$refs.chart2) {
          console.error('chart2容器未找到')
          return
        }

        // 销毁旧实例
        if (this.chart2) {
          this.chart2.dispose()
        }

        this.chart2 = echarts.init(this.$refs.chart2)

        const option = {
          tooltip: {
            trigger: 'axis',
            axisPointer: {
              type: 'cross',
              label: {
                backgroundColor: '#6a7985'
              }
            }
          },
          legend: {
            data: this.riskAuditDimensionData.series.map(s => s.name),
            textStyle: {
              color: '#fff'
            },
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
            data: this.riskAuditDimensionData.categories,
            axisLabel: {
              color: '#fff',
              rotate: 45
            },
            axisLine: {
              lineStyle: {
                color: '#4a5568'
              }
            }
          },
          yAxis: {
            type: 'value',
            axisLabel: {
              color: '#fff'
            },
            axisLine: {
              lineStyle: {
                color: '#4a5568'
              }
            },
            splitLine: {
              lineStyle: {
                color: '#2d3748'
              }
            }
          },
          series: this.riskAuditDimensionData.series.map((s, index) => {
            // 使用对比度更高的颜色：红色、蓝色、绿色
            const colors = ['#FF6B6B', '#4ECDC4', '#FFE66D']
            return {
              name: s.name,
              type: 'line',
              smooth: true,
              data: s.data,
              itemStyle: {
                color: colors[index % colors.length]
              },
              lineStyle: {
                width: 3,  // 加粗线条
                color: colors[index % colors.length]
              },
              areaStyle: {
                opacity: 0.2,
                color: {
                  type: 'linear',
                  x: 0,
                  y: 0,
                  x2: 0,
                  y2: 1,
                  colorStops: [{
                    offset: 0,
                    color: colors[index % colors.length]
                  }, {
                    offset: 1,
                    color: 'rgba(255, 255, 255, 0.1)'
                  }]
                }
              }
            }
          })
        }

        this.chart2.setOption(option)
      },

      // 初始化内控测试缺陷程度饼图（chart3）
      initChart3() {
        if (!this.$refs.chart3) {
          console.error('chart3容器未找到')
          return
        }

        // 销毁旧实例
        if (this.chart3) {
          this.chart3.dispose()
        }

        this.chart3 = echarts.init(this.$refs.chart3)

        const option = {
          tooltip: {
            trigger: 'item',
            formatter: '{a} <br/>{b}: {c} ({d%})'
          },
          legend: {
            orient: 'vertical',
            right: '10%',
            top: 'center',
            textStyle: {
              color: '#fff',
              fontSize: 12
            }
          },
          series: [
            {
              name: '内控测试缺陷程度',
              type: 'pie',
              radius: ['40%', '70%'],
              center: ['35%', '50%'],
              avoidLabelOverlap: false,
              itemStyle: {
                borderRadius: 10,
                borderColor: '#1a1f3a',
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
              data: this.internalControlDefectData,
              color: ['#5470c6', '#91cc75', '#fac858', '#ee6666', '#73c0de', '#3ba272', '#fc8452', '#9a60b4', '#ea7ccc']
            }
          ]
        }

        this.chart3.setOption(option)
      },

      // 初始化内控缺陷整改跟进饼图（chart6）
      initChart6() {
        if (!this.$refs.chart6) {
          console.error('chart6容器未找到')
          return
        }

        // 销毁旧实例
        if (this.chart6) {
          this.chart6.dispose()
        }

        this.chart6 = echarts.init(this.$refs.chart6)

        const option = {
          tooltip: {
            trigger: 'item',
            formatter: '{a} <br/>{b}: {c} ({d%})'
          },
          legend: {
            orient: 'vertical',
            right: '10%',
            top: 'center',
            textStyle: {
              color: '#fff',
              fontSize: 12
            }
          },
          series: [
            {
              name: '内控缺陷整改跟进',
              type: 'pie',
              radius: ['40%', '70%'],
              center: ['35%', '50%'],
              avoidLabelOverlap: false,
              itemStyle: {
                borderRadius: 10,
                borderColor: '#1a1f3a',
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
              data: this.internalControlRectificationData,
              color: ['#91cc75', '#ee6666']  // 绿色（已完成）、红色（未完成）
            }
          ]
        }

        this.chart6.setOption(option)
      },

      // 初始化审计问题数量折线图（chart5）
      initChart5() {
        if (!this.$refs.chart5) {
          console.error('chart5容器未找到')
          return
        }

        // 销毁旧实例
        if (this.chart5) {
          this.chart5.dispose()
        }

        this.chart5 = echarts.init(this.$refs.chart5)

        const option = {
          tooltip: {
            trigger: 'axis',
            axisPointer: {
              type: 'cross',
              label: {
                backgroundColor: '#6a7985'
              }
            }
          },
          legend: {
            data: this.auditIssueCountData.series.map(s => s.name),
            textStyle: {
              color: '#fff'
            },
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
            data: this.auditIssueCountData.categories,
            axisLabel: {
              color: '#fff',
              rotate: 45
            },
            axisLine: {
              lineStyle: {
                color: '#4a5568'
              }
            }
          },
          yAxis: {
            type: 'value',
            axisLabel: {
              color: '#fff'
            },
            axisLine: {
              lineStyle: {
                color: '#4a5568'
              }
            },
            splitLine: {
              lineStyle: {
                color: '#2d3748'
              }
            }
          },
          series: this.auditIssueCountData.series.map((s) => {
            return {
              name: s.name,
              type: 'line',
              smooth: true,
              data: s.data,
              itemStyle: {
                color: '#91cc75'  // 绿色
              },
              lineStyle: {
                width: 3,
                color: '#91cc75'
              },
              areaStyle: {
                opacity: 0.3,
                color: {
                  type: 'linear',
                  x: 0,
                  y: 0,
                  x2: 0,
                  y2: 1,
                  colorStops: [{
                    offset: 0,
                    color: '#91cc75'
                  }, {
                    offset: 1,
                    color: 'rgba(255, 255, 255, 0.1)'
                  }]
                }
              }
            }
          })
        }

        this.chart5.setOption(option)
      },

      // 初始化风险事件处理饼图（chart4）
      initChart4() {
        if (!this.$refs.chart4) {
          console.error('chart4容器未找到')
          return
        }

        // 销毁旧实例
        if (this.chart4) {
          this.chart4.dispose()
        }

        this.chart4 = echarts.init(this.$refs.chart4)

        const option = {
          tooltip: {
            trigger: 'item',
            formatter: '{a} <br/>{b}: {c} ({d%})'
          },
          legend: {
            orient: 'vertical',
            right: '10%',
            top: 'center',
            textStyle: {
              color: '#fff',
              fontSize: 12
            }
          },
          series: [
            {
              name: '风险事件处理',
              type: 'pie',
              radius: ['40%', '70%'],
              center: ['35%', '50%'],
              avoidLabelOverlap: false,
              itemStyle: {
                borderRadius: 10,
                borderColor: '#1a1f3a',
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
              data: this.riskEventHandlingData,
              color: ['#5470c6', '#ee6666']
            }
          ]
        }

        this.chart4.setOption(option)
      },

      // 初始化仪表盘图表
      initGaugeChart() {
        if (!this.$refs.gaugeChart) {
          console.error('仪表盘图表容器未找到')
          return
        }

        // 销毁旧实例
        if (this.gaugeChart) {
          this.gaugeChart.dispose()
        }

        this.gaugeChart = echarts.init(this.$refs.gaugeChart)

        const responseRate = this.riskWarningData.responseRate || 0

        const option = {
          series: [
            {
              type: 'gauge',
              startAngle: 180,
              endAngle: 0,
              center: ['50%', '75%'],
              radius: '90%',
              min: 0,
              max: 100,
              splitNumber: 10,
              axisLine: {
                lineStyle: {
                  width: 6,
                  color: [
                    [0.3, '#FF6E76'],
                    [0.7, '#FDDD60'],
                    [1, '#58D9F9']
                  ]
                }
              },
              pointer: {
                icon: 'path://M12.8,0.7l12,40.1H0.7L12.8,0.7z',
                length: '12%',
                width: 20,
                offsetCenter: [0, '-60%'],
                itemStyle: {
                  color: 'auto'
                }
              },
              axisTick: {
                length: 12,
                lineStyle: {
                  color: 'auto',
                  width: 2
                }
              },
              splitLine: {
                length: 20,
                lineStyle: {
                  color: 'auto',
                  width: 5
                }
              },
              axisLabel: {
                color: '#fff',
                fontSize: 12,
                distance: -60,
                rotate: 'tangential',
                formatter: function (value) {
                  if (value === 0 || value === 50 || value === 100) {
                    return value
                  }
                  return ''
                }
              },
              title: {
                offsetCenter: [0, '-10%'],
                fontSize: 16,
                color: '#fff'
              },
              detail: {
                fontSize: 30,
                offsetCenter: [0, '-35%'],
                valueAnimation: true,
                formatter: function (value) {
                  return Math.round(value) + '%'
                },
                color: 'auto'
              },
              data: [
                {
                  value: responseRate,
                  name: '响应率'
                }
              ]
            }
          ]
        }

        this.gaugeChart.setOption(option)

        // 监听窗口大小变化
        window.addEventListener('resize', () => {
          if (this.gaugeChart) {
            this.gaugeChart.resize()
          }
        })
      },

      // 行悬浮：暂停滚动
      onRowEnter() {
        if (this.scrollTimer) {
          clearInterval(this.scrollTimer)
          this.scrollTimer = null
        }
      },

      // 行离开：恢复滚动 + 隐藏气泡
      onRowLeave() {
        this.tooltip.visible = false
        this.startAutoScroll()
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

    // 大屏主体 - 3行×3列网格布局
    .screen-body {
      flex: 1;
      display: grid;
      grid-template-columns: repeat(3, 1fr); // 3列等宽
      grid-template-rows: repeat(3, 1fr); // 3行等高
      padding: 20px;
      gap: 15px;
      overflow: hidden;
      position: relative;
      z-index: 1;

      // 第1个组件占2列宽度（左上角）
      .panel-box:nth-child(1) {
        grid-column: span 2;
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

      // 第1部分表格列宽（基础用户信息）
      .grid-col-2 {
        .th-1,
        .td-1 {
          flex: 1;
          min-width: 100px;
        }
        .th-2,
        .td-2 {
          flex: 1;
          min-width: 120px;
        }
        .th-3,
        .td-3 {
          flex: 1;
          min-width: 100px;
        }
        .th-4,
        .td-4 {
          flex: 1;
          min-width: 100px;
        }
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

      // 风险预警响应率面板样式
      .risk-warning-panel {
        .gauge-container {
          flex: 1;
          display: flex;
          align-items: center;
          justify-content: center;
          padding: 10px;
          min-height: 0;

          .gauge-chart {
            width: 100%;
            height: 100%;
            min-height: 200px;
          }
        }
      }

      // 进度条面板样式
      .progress-panel {
        .progress-container {
          flex: 1;
          display: flex;
          flex-direction: column;
          justify-content: center;
          padding: 20px;
          gap: 20px;

          .progress-info {
            display: flex;
            flex-direction: column;
            gap: 10px;

            .progress-label {
              font-size: 16px;
              color: #409eff;
              font-weight: 500;
              text-align: center;
            }

            .progress-stats {
              display: flex;
              justify-content: space-around;
              font-size: 14px;

              .completed {
                color: #67c23a;
                font-weight: 500;
              }

              .total {
                color: #909399;
                font-weight: 500;
              }
            }
          }

          .progress-bar-wrapper {
            padding: 0 10px;

            ::v-deep .el-progress {
              .el-progress-bar {
                .el-progress-bar__outer {
                  background-color: rgba(255, 255, 255, 0.1);
                }

                .el-progress-bar__inner {
                  transition: width 0.6s ease;
                }

                .el-progress-bar__innerText {
                  color: #fff;
                  font-size: 14px;
                  font-weight: bold;
                }
              }
            }
          }
        }
      }
    }
  }

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
