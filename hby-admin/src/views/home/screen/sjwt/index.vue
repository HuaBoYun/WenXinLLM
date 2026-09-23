<template>
  <div class="sjwt-container">
    <!-- 页面头部 -->
    <div class="page-header">
      <el-button
        type="primary"
        icon="el-icon-full-screen"
        @click="openFullScreen"
      >
        大屏
      </el-button>
    </div>

    <!-- 数据概览卡片 -->
    <el-row :gutter="20" class="overview-cards">
      <el-col :span="6">
        <div class="card-item">
          <div
            class="card-icon"
            style="
              background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            "
          >
            <i class="el-icon-document-checked"></i>
          </div>
          <div class="card-content">
            <div class="card-title">审计项目总数</div>
            <div class="card-value">
              156
              <span class="unit">个</span>
            </div>
            <div class="card-trend up">
              <i class="el-icon-top"></i>
              8.5%
            </div>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="card-item">
          <div
            class="card-icon"
            style="
              background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
            "
          >
            <i class="el-icon-warning-outline"></i>
          </div>
          <div class="card-content">
            <div class="card-title">发现问题数</div>
            <div class="card-value">
              1245
              <span class="unit">个</span>
            </div>
            <div class="card-trend down">
              <i class="el-icon-bottom"></i>
              3.2%
            </div>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="card-item">
          <div
            class="card-icon"
            style="
              background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
            "
          >
            <i class="el-icon-circle-check"></i>
          </div>
          <div class="card-content">
            <div class="card-title">整改完成率</div>
            <div class="card-value">
              60
              <span class="unit">%</span>
            </div>
            <div class="card-trend up">
              <i class="el-icon-top"></i>
              5.3%
            </div>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="card-item">
          <div
            class="card-icon"
            style="
              background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
            "
          >
            <i class="el-icon-s-data"></i>
          </div>
          <div class="card-content">
            <div class="card-title">涉及单位</div>
            <div class="card-value">
              89
              <span class="unit">家</span>
            </div>
            <div class="card-trend up">
              <i class="el-icon-top"></i>
              2.1%
            </div>
          </div>
        </div>
      </el-col>
    </el-row>

    <!-- 第一行：审计问题整改迟缓事项表格 + 集团审计问题数量变化趋势 -->
    <el-row :gutter="20" style="margin-top: 20px">
      <el-col :span="12">
        <el-card shadow="hover">
          <div slot="header">
            <span>审计问题整改迟缓事项</span>
          </div>
          <el-table :data="delayedItems" height="300" stripe>
            <el-table-column
              type="index"
              label="序号"
              width="60"
            ></el-table-column>
            <el-table-column
              prop="projectName"
              label="审计项目"
              min-width="150"
            ></el-table-column>
            <el-table-column
              prop="auditType"
              label="审计类型"
              width="120"
            ></el-table-column>
            <el-table-column
              prop="date"
              label="计划完成时间"
              width="110"
            ></el-table-column>
            <el-table-column
              prop="type"
              label="审批类型"
              width="100"
            ></el-table-column>
            <el-table-column
              prop="person"
              label="责任人"
              width="80"
            ></el-table-column>
            <el-table-column
              prop="dept"
              label="责任部门"
              width="120"
            ></el-table-column>
            <el-table-column
              prop="amount"
              label="问题金额(亿元)"
              width="120"
            ></el-table-column>
          </el-table>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="hover">
          <div slot="header">
            <span>集团审计问题数量变化趋势</span>
          </div>
          <div ref="problemTrendChart" style="height: 300px"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 第二行：4个环形图 -->
    <el-row :gutter="20" style="margin-top: 20px">
      <el-col :span="6">
        <el-card shadow="hover">
          <div slot="header">
            <span>审计项目数</span>
          </div>
          <div ref="auditProjectChart" style="height: 250px"></div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div slot="header">
            <span>审计覆盖情况</span>
          </div>
          <div ref="auditCoverageChart" style="height: 250px"></div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div slot="header">
            <span>审计项目计划完成情况</span>
          </div>
          <div ref="projectCompletionChart" style="height: 250px"></div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div slot="header">
            <span>高风险评估事项</span>
          </div>
          <div ref="highRiskChart" style="height: 250px"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 全屏大屏弹窗 -->
    <el-dialog
      :visible.sync="screenVisible"
      fullscreen
      :show-close="false"
      :close-on-click-modal="false"
      :close-on-press-escape="false"
      custom-class="screen-dialog"
    >
      <screen-display
        v-if="screenVisible"
        :audit-data="allAuditData"
        @close="closeFullScreen"
      />
    </el-dialog>
  </div>
</template>

<script>
  import * as echarts from 'echarts'
  import ScreenDisplay from './components/ScreenDisplay'
  import { getAuditProblemData } from './api/auditProblem.js'

  export default {
    name: 'AuditProblem',
    components: {
      ScreenDisplay,
    },
    data() {
      return {
        screenVisible: false,
        allAuditData: null,
        delayedItems: [],
      }
    },
    mounted() {
      this.loadData()
      this.$nextTick(() => {
        this.initCharts()
      })
    },
    methods: {
      async loadData() {
        try {
          const data = await getAuditProblemData()
          this.allAuditData = data
          this.delayedItems = data.tlist || []
        } catch (error) {
          console.error('加载数据失败:', error)
        }
      },
      initCharts() {
        this.initProblemTrendChart()
        this.initAuditProjectChart()
        this.initAuditCoverageChart()
        this.initProjectCompletionChart()
        this.initHighRiskChart()
      },
      initProblemTrendChart() {
        if (!this.$refs.problemTrendChart) return

        const chart = echarts.init(this.$refs.problemTrendChart)

        const option = {
          tooltip: {
            trigger: 'axis',
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
            data: [
              '2018/2',
              '2018/3',
              '2018/4',
              '2018/5',
              '2018/6',
              '2018/7',
              '2018/8',
              '2018/9',
              '2018/10',
              '2018/11',
              '2018/12',
              '2019/1',
            ],
            axisLabel: {
              color: '#666',
            },
          },
          yAxis: {
            type: 'value',
            name: '(件)',
            axisLabel: {
              color: '#666',
            },
          },
          series: [
            {
              name: '审计问题数量',
              type: 'line',
              data: [
                2000, 4500, 3500, 5000, 4000, 3000, 4500, 3800, 4200, 3500,
                4000, 3200,
              ],
              smooth: true,
              itemStyle: {
                color: '#00d4ff',
              },
              areaStyle: {
                color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                  { offset: 0, color: 'rgba(0, 212, 255, 0.3)' },
                  { offset: 1, color: 'rgba(0, 212, 255, 0.05)' },
                ]),
              },
            },
          ],
        }

        chart.setOption(option)
        window.addEventListener('resize', () => chart.resize())
      },
      initAuditProjectChart() {
        if (!this.$refs.auditProjectChart) return

        const chart = echarts.init(this.$refs.auditProjectChart)

        const option = {
          tooltip: {
            trigger: 'item',
            formatter: '{b}: {c} ({d}%)',
          },
          series: [
            {
              type: 'pie',
              radius: ['40%', '70%'],
              center: ['50%', '50%'],
              data: [
                { name: '委托1', value: 26 },
                { name: '委托2', value: 18 },
                { name: '委托3', value: 16 },
                { name: '委托4', value: 30 },
                { name: '委托5', value: 15 },
              ],
              label: {
                show: true,
                formatter: '{b}: {d}%',
              },
              itemStyle: {
                borderRadius: 10,
                borderColor: '#fff',
                borderWidth: 2,
              },
              color: ['#00d4ff', '#00ff88', '#ffd700', '#ff9800', '#a78bfa'],
            },
          ],
        }

        chart.setOption(option)
        window.addEventListener('resize', () => chart.resize())
      },
      initAuditCoverageChart() {
        if (!this.$refs.auditCoverageChart) return

        const chart = echarts.init(this.$refs.auditCoverageChart)

        const option = {
          tooltip: {
            trigger: 'item',
            formatter: '{b}: {c} ({d}%)',
          },
          series: [
            {
              type: 'pie',
              radius: ['40%', '70%'],
              center: ['50%', '50%'],
              data: [
                { name: '已覆盖', value: 600 },
                { name: '未覆盖', value: 400 },
              ],
              label: {
                show: true,
                formatter: '{b}: {d}%',
              },
              itemStyle: {
                borderRadius: 10,
                borderColor: '#fff',
                borderWidth: 2,
              },
              color: ['#ffd700', '#00ff88'],
            },
          ],
        }

        chart.setOption(option)
        window.addEventListener('resize', () => chart.resize())
      },
      initProjectCompletionChart() {
        if (!this.$refs.projectCompletionChart) return

        const chart = echarts.init(this.$refs.projectCompletionChart)

        const option = {
          tooltip: {
            trigger: 'item',
            formatter: '{b}: {c} ({d}%)',
          },
          series: [
            {
              type: 'pie',
              radius: ['40%', '70%'],
              center: ['50%', '50%'],
              data: [
                { name: '已完成', value: 60 },
                { name: '进行中', value: 25 },
                { name: '未开始', value: 15 },
              ],
              label: {
                show: true,
                formatter: '{b}: {d}%',
              },
              itemStyle: {
                borderRadius: 10,
                borderColor: '#fff',
                borderWidth: 2,
              },
              color: ['#00ff88', '#ffd700', '#ff9800'],
            },
          ],
        }

        chart.setOption(option)
        window.addEventListener('resize', () => chart.resize())
      },
      initHighRiskChart() {
        if (!this.$refs.highRiskChart) return

        const chart = echarts.init(this.$refs.highRiskChart)

        const option = {
          tooltip: {
            trigger: 'item',
            formatter: '{b}: {c} ({d}%)',
          },
          series: [
            {
              type: 'pie',
              radius: ['40%', '70%'],
              center: ['50%', '50%'],
              data: [
                { name: '委托1', value: 32 },
                { name: '委托2', value: 12 },
                { name: '委托3', value: 5 },
                { name: '其他', value: 15 },
              ],
              label: {
                show: true,
                formatter: '{b}: {d}%',
              },
              itemStyle: {
                borderRadius: 10,
                borderColor: '#fff',
                borderWidth: 2,
              },
              color: ['#ff9800', '#00d4ff', '#ffd700', '#a78bfa'],
            },
          ],
        }

        chart.setOption(option)
        window.addEventListener('resize', () => chart.resize())
      },
      openFullScreen() {
        this.screenVisible = true
      },
      closeFullScreen() {
        this.screenVisible = false
      },
    },
    beforeDestroy() {
      window.removeEventListener('resize', () => {})
    },
  }
</script>

<style lang="scss" scoped>
  .sjwt-container {
    padding: 20px;
    background: #f0f2f5;
    min-height: calc(100vh - 84px);

    .page-header {
      margin-bottom: 20px;
      display: flex;
      justify-content: flex-end;
    }

    .overview-cards {
      .card-item {
        background: #fff;
        border-radius: 8px;
        padding: 20px;
        display: flex;
        align-items: center;
        box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
        transition: all 0.3s;

        &:hover {
          transform: translateY(-5px);
          box-shadow: 0 4px 20px 0 rgba(0, 0, 0, 0.15);
        }

        .card-icon {
          width: 60px;
          height: 60px;
          border-radius: 12px;
          display: flex;
          align-items: center;
          justify-content: center;
          margin-right: 15px;

          i {
            font-size: 28px;
            color: #fff;
          }
        }

        .card-content {
          flex: 1;

          .card-title {
            font-size: 14px;
            color: #909399;
            margin-bottom: 8px;
          }

          .card-value {
            font-size: 28px;
            font-weight: bold;
            color: #303133;
            margin-bottom: 5px;

            .unit {
              font-size: 14px;
              font-weight: normal;
              margin-left: 5px;
            }
          }

          .card-trend {
            font-size: 12px;
            color: #909399;

            &.up {
              color: #67c23a;
            }

            &.down {
              color: #f56c6c;
            }

            i {
              margin-right: 3px;
            }
          }
        }
      }
    }
  }
</style>

<style lang="scss">
  .screen-dialog {
    .el-dialog__header {
      display: none;
    }

    .el-dialog__body {
      padding: 0;
      height: 100vh;
      overflow: hidden;
    }
  }
</style>
