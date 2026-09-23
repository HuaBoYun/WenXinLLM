<template>
  <!--  <div id="abc" class="system-log-container"></div>-->
  <div id="app">
    <div class="header fixed-top">
      <el-form :inline="true" label-width="80px">
        <el-form-item label="年">
          <el-select v-model="queryYear" class="form-select" @change="fetchData">
            <el-option value="2026">2026</el-option>
            <el-option value="2025">2025</el-option>
            <el-option value="2024">2024</el-option>
            <el-option value="2023">2023</el-option>
            <el-option value="2022">2022</el-option>
            <el-option value="2021">2021</el-option>
            <el-option value="2020">2020</el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button icon="el-icon-refresh-right" type="primary" @click="fetchData" />
        </el-form-item>
      </el-form>
    </div>
    <div class="content">
      <el-row :gutter="10">
        <el-col class="border_none" :lg="6" :md="6" :sm="24">
          <el-row>
            <el-col :lg="24" :md="24" :sm="24">
              <div class="one-item flex-col padding-10 margin-bottom-0">
                <div class="flex align-baseline margin-bottom-10">
                  <div class="price">{{ overviewData.operatingProfit }}</div>
                  <div class="unit">万元</div>
                </div>
                <div class="title margin-bottom-10">营业利润</div>
                <div class="compare flex">
                  <div class="margin-right-10">同比</div>
                  <el-link :type="overviewData.operatingProfitYoy >= 0 ? 'success' : 'danger'">{{ overviewData.operatingProfitYoy != null ? (overviewData.operatingProfitYoy >= 0 ? '⬆' : '⬇') + (overviewData.operatingProfitYoy * 100).toFixed(2) + '%' : '--' }}</el-link>
                </div>
              </div>
            </el-col>
          </el-row>
          <el-row>
            <el-col :lg="24" :md="24" :sm="24">
              <div class="one-item flex-col padding-10 margin-bottom-0">
                <div class="flex align-baseline margin-bottom-10">
                  <div class="price">{{ overviewData.netProfit }}</div>
                  <div class="unit">万元</div>
                </div>
                <div class="title margin-bottom-10">净利润</div>
                <div class="compare flex">
                  <div class="margin-right-10">同比</div>
                  <el-link :type="overviewData.netProfitYoy >= 0 ? 'success' : 'danger'">{{ overviewData.netProfitYoy != null ? (overviewData.netProfitYoy >= 0 ? '⬆' : '⬇') + (overviewData.netProfitYoy * 100).toFixed(2) + '%' : '--' }}</el-link>
                </div>
              </div>
            </el-col>
          </el-row>
          <el-row>
            <el-col :lg="24" :md="24" :sm="24">
              <div class="one-item flex-col padding-10 margin-bottom-0">
                <div class="flex align-baseline margin-bottom-10">
                  <div class="price">{{ overviewData.totalProfit }}</div>
                  <div class="unit">万元</div>
                </div>
                <div class="title margin-bottom-10">利润总额</div>
                <div class="compare flex">
                  <div class="margin-right-10">同比</div>
                  <el-link :type="overviewData.totalProfitYoy >= 0 ? 'success' : 'danger'">{{ overviewData.totalProfitYoy != null ? (overviewData.totalProfitYoy >= 0 ? '⬆' : '⬇') + (overviewData.totalProfitYoy * 100).toFixed(2) + '%' : '--' }}</el-link>
                </div>
              </div>
            </el-col>
          </el-row>
        </el-col>
        <el-col :lg="6" :md="6" :sm="24">
          <div id="chats-1" style="width: 100%; height: 326px"></div>
        </el-col>
        <el-col :lg="6" :md="6" :sm="24">
          <div id="chats-2" style="width: 100%; height: 326px"></div>
        </el-col>
        <el-col :lg="6" :md="6" :sm="24">
          <div id="chats-3" style="width: 100%; height: 326px"></div>
        </el-col>
      </el-row>

      <el-row :gutter="10">
        <el-col :lg="8" :md="8" :sm="24">
          <div id="chats-4" style="width: 100%; height: 300px"></div>
        </el-col>
        <el-col :lg="8" :md="8" :sm="24">
          <div id="chats-5" style="width: 100%; height: 300px"></div>
        </el-col>
        <el-col :lg="8" :md="8" :sm="24">
          <div id="chats-6" style="width: 100%; height: 300px"></div>
        </el-col>
      </el-row>

      <el-row :gutter="10">
        <el-col :lg="12" :md="12" :sm="24">
          <div id="chats-7" style="width: 100%; height: 300px"></div>
        </el-col>
        <el-col :lg="12" :md="12" :sm="24">
          <div id="chats-8" style="width: 100%; height: 300px"></div>
        </el-col>
      </el-row>

      <el-row :gutter="10">
        <el-col :lg="12" :md="12" :sm="24">
          <div id="chats-9" style="width: 100%; height: 300px"></div>
        </el-col>
        <el-col :lg="12" :md="12" :sm="24">
          <div id="chats-10" style="width: 100%; height: 300px"></div>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
  import * as echarts from 'echarts'
  import { getLrfxData } from '@/api/stateAssets/cwztfx'

  export default {
    name: 'Lrfx',
    components: {},
    data() {
      return {
        queryYear: new Date().getFullYear().toString(),
        overviewData: {
          operatingProfit: '--', operatingProfitYoy: null,
          netProfit: '--', netProfitYoy: null,
          totalProfit: '--', totalProfitYoy: null
        },
        trendMonths: ['1','2','3','4','5','6','7','8','9','10','11','12'],
        profitRateGauge1: 0, profitRateGauge2: 0,
        rankData: [], totalProfitTrend: [], netProfitRate: []
      }
    },
    mounted() {
      this.initCharts()
      this.fetchData()
    },
    created() {},
    methods: {
      fetchData() {
        getLrfxData({ year: this.queryYear }).then(res => {
          const d = res.data || res
          if (d.overview) {
            const o = d.overview
            this.overviewData = {
              operatingProfit: o.operatingProfit || '--',
              operatingProfitYoy: o.operatingProfitYoy,
              netProfit: o.netProfit || '--',
              netProfitYoy: o.netProfitYoy,
              totalProfit: o.totalProfit || '--',
              totalProfitYoy: o.totalProfitYoy
            }
          }
          // 刷新图表
          this.refreshCharts(d)
        }).catch(() => {})
      },
      refreshCharts(d) {
        if (!d) return
        // 营业利润率仪表
        const c1 = echarts.getInstanceByDom(document.getElementById('chats-1'))
        if (c1 && d.operatingProfitRate !== undefined) {
          c1.setOption({ series: [{ data: [{ value: +(d.operatingProfitRate * 100).toFixed(2), name: '营业利润率' }] }] })
        }
        // 净利润率仪表
        const c2 = echarts.getInstanceByDom(document.getElementById('chats-2'))
        if (c2 && d.netProfitRate !== undefined) {
          c2.setOption({ series: [{ data: [{ value: +(d.netProfitRate * 100).toFixed(2), name: '净利润率' }] }] })
        }
        // 公司净利润排名
        const c3 = echarts.getInstanceByDom(document.getElementById('chats-3'))
        if (c3 && d.companyRank && d.companyRank.length) {
          c3.setOption({
            xAxis: [{ data: d.companyRank.map(r => r.companyName) }],
            series: [{ data: d.companyRank.map(r => r.netProfit) }]
          })
        }
        // 利润总额计划达成率
        const c4 = echarts.getInstanceByDom(document.getElementById('chats-4'))
        if (c4 && d.quarterAchieve && d.quarterAchieve.length) {
          c4.setOption({
            yAxis: { data: d.quarterAchieve.map(r => r.label) },
            series: [{ data: d.quarterAchieve.map(r => r.rate) }]
          })
        }
        // 利润年趋势
        const c5 = echarts.getInstanceByDom(document.getElementById('chats-5'))
        if (c5 && d.yearTrend && d.yearTrend.length) {
          c5.setOption({
            xAxis: [{ data: d.yearTrend.map(r => r.period) }],
            series: [{ data: d.yearTrend.map(r => r.amount) }, { data: d.yearTrend.map(r => r.yoy) }]
          })
        }
        // 利润月趋势
        const c6 = echarts.getInstanceByDom(document.getElementById('chats-6'))
        if (c6 && d.monthTrend && d.monthTrend.length) {
          c6.setOption({
            xAxis: [{ data: d.monthTrend.map(r => r.month) }],
            series: [
              { data: d.monthTrend.map(r => r.totalProfit) },
              { data: d.monthTrend.map(r => r.yoy) },
              { data: d.monthTrend.map(r => r.mom) }
            ]
          })
        }
        // 营业利润月趋势
        const c7 = echarts.getInstanceByDom(document.getElementById('chats-7'))
        if (c7 && d.monthTrend && d.monthTrend.length) {
          c7.setOption({
            xAxis: [{ data: d.monthTrend.map(r => r.month) }],
            series: [
              { data: d.monthTrend.map(r => r.operatingProfit) },
              { data: d.monthTrend.map(r => r.yoy) },
              { data: d.monthTrend.map(r => r.mom) }
            ]
          })
        }
        // 净利润月趋势
        const c8 = echarts.getInstanceByDom(document.getElementById('chats-8'))
        if (c8 && d.monthTrend && d.monthTrend.length) {
          c8.setOption({
            xAxis: [{ data: d.monthTrend.map(r => r.month) }],
            series: [
              { data: d.monthTrend.map(r => r.netProfit) },
              { data: d.monthTrend.map(r => r.yoy) },
              { data: d.monthTrend.map(r => r.mom) }
            ]
          })
        }
        // 利润趋势对比
        const c9 = echarts.getInstanceByDom(document.getElementById('chats-9'))
        if (c9 && d.monthTrend && d.monthTrend.length) {
          c9.setOption({
            xAxis: [{ data: d.monthTrend.map(r => r.month) }],
            series: [
              { data: d.monthTrend.map(r => r.operatingProfit) },
              { data: d.monthTrend.map(r => r.totalProfit) },
              { data: d.monthTrend.map(r => r.netProfit) }
            ]
          })
        }
        // 净利润率月度趋势
        const c10 = echarts.getInstanceByDom(document.getElementById('chats-10'))
        if (c10 && d.monthTrend && d.monthTrend.length) {
          c10.setOption({
            xAxis: { data: d.monthTrend.map(r => r.month) },
            series: [{ data: d.monthTrend.map(r => r.netProfitRate) }]
          })
        }
      },
      initCharts() {
      const chats1 = echarts.init(document.getElementById('chats-1'))

      // 指定图表的配置项和数据
      chats1.setOption({
        title: {
          text: '营业利润率',
          left: 'left',
        },
        tooltip: {
          formatter: '{a} <br/>{b} : {c}%',
        },
        series: [
          {
            name: 'Pressure',
            type: 'gauge',
            progress: {
              show: true,
            },
            detail: {
              valueAnimation: true,
              formatter: '{value}',
            },
            data: [
              {
                value: 50,
                name: '营业利润率',
              },
            ],
          },
        ],
      })

      const chats2 = echarts.init(document.getElementById('chats-2'))

      // 指定图表的配置项和数据
      chats2.setOption({
        title: {
          text: '净利润率',
          left: 'left',
        },
        tooltip: {
          formatter: '{a} <br/>{b} : {c}%',
        },
        series: [
          {
            name: 'Pressure',
            type: 'gauge',
            progress: {
              show: true,
            },
            detail: {
              valueAnimation: true,
              formatter: '{value}',
            },
            data: [
              {
                value: 50,
                name: '净利润率',
              },
            ],
          },
        ],
      })

      // 基于准备好的dom，初始化echarts实例
      const chats3 = echarts.init(document.getElementById('chats-3'))
      // 绘制图表
      chats3.setOption({
        title: {
          text: '公司净利润排名',
          left: 'left',
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow',
          },
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true,
        },
        xAxis: [
          {
            type: 'category',
            data: [
              '公司001',
              '公司002',
              '公司003',
              '公司004',
              '公司005',
              '公司006',
              '公司007',
              '公司008',
              '公司009',
              '公司010',
            ],
            axisTick: {
              alignWithLabel: true,
            },
          },
        ],
        yAxis: [
          {
            type: 'value',
          },
        ],
        series: [
          {
            name: '合同数量',
            type: 'bar',
            barWidth: '60%',
            data: [20, 18, 17, 16, 15, 14, 12, 11, 12, 8],
          },
        ],
      })

      const chats4 = echarts.init(document.getElementById('chats-4'))

      // 指定图表的配置项和数据
      chats4.setOption({
        title: {
          text: '利润总额计划达成率',
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow',
          },
          position: ['50%', '50%'],
        },
        grid: {
          top: 80,
          bottom: 30,
        },
        xAxis: {
          type: 'value',
          position: 'top',
          splitLine: {
            lineStyle: {
              type: 'dashed',
            },
          },
        },
        yAxis: {
          type: 'category',
          axisLine: { show: false },
          axisLabel: { show: false },
          axisTick: { show: false },
          splitLine: { show: false },
          data: [
            '第一季度 本期金额: 820,268，本期预算金额：846,884',
            '第二季度 本期金额: 820,268，本期预算金额：846,884',
            '第三季度 本期金额: 820,268，本期预算金额：846,884',
            '第四季度 本期金额: 820,268，本期预算金额：846,884',
          ],
        },
        series: [
          {
            name: 'Cost',
            type: 'bar',
            stack: 'Total',
            label: {
              show: true,
              formatter: '{b}',
            },
            data: [96.86, 100.93, 99.73, 103.78],
          },
        ],
      })

      const chats5 = echarts.init(document.getElementById('chats-5'))

      // 指定图表的配置项和数据
      chats5.setOption({
        title: {
          text: '总利润年趋势【联动】',
          subtext: '单位：万元 / %',
        },
        tooltip: {
          trigger: 'axis',
        },
        legend: {
          data: ['本期金额', '同比'],
          // padding: [10, 0, 10, 0],
          right: 0,
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true,
        },
        xAxis: [
          {
            type: 'category',
            boundaryGap: false,
            data: [
              '2014',
              '2015',
              '2016',
              '2017',
              '2018',
              '2019',
              '2020',
              '2021',
              '2022',
            ],
          },
        ],
        yAxis: [
          {
            type: 'value',
            scale: true,
            // name: 'Price',
            boundaryGap: [0.2, 0.2],
          },
          {
            type: 'value',
            scale: true,
            // name: 'Order',
            max: 0.1,
            min: -0.1,
            boundaryGap: [0.2, 0.2],
          },
        ],
        series: [
          {
            name: '本期金额',
            type: 'bar',
            stack: 'Total',
            data: [160, 60, 180, 180, 350, 300, 150, 50, 50],
          },
          {
            name: '同比',
            type: 'line',
            stack: 'Total',
            data: [0, -0.02, -0.03, 0, 0, 0.06, 0.01, 0.015, -0.06],
          },
        ],
      })

      const chats6 = echarts.init(document.getElementById('chats-6'))
      // 指定图表的配置项和数据
      chats6.setOption({
        title: {
          text: '利润总额月趋势',
          subtext: '单位：万元 / %',
        },
        tooltip: {
          trigger: 'axis',
        },
        legend: {
          data: ['本期金额', '同比', '环比'],
          right: 0,
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true,
        },
        xAxis: [
          {
            type: 'category',
            boundaryGap: false,
            data: [
              '2014',
              '2015',
              '2016',
              '2017',
              '2018',
              '2019',
              '2020',
              '2021',
              '2022',
            ],
          },
        ],
        yAxis: [
          {
            type: 'value',
            scale: true,
            name: 'Price',
            boundaryGap: [0.2, 0.2],
          },
          {
            type: 'value',
            scale: true,
            name: 'Order',
            max: 0.1,
            min: -0.1,
            boundaryGap: [0.2, 0.2],
          },
        ],
        series: [
          {
            name: '本期金额',
            type: 'bar',
            stack: 'Total',
            data: [160, 60, 180, 180, 350, 300, 150, 50, 50],
          },
          {
            name: '同比',
            type: 'line',
            stack: 'Total',
            data: [0, -0.02, -0.03, 0, 0, 0.06, 0.01, 0.015, -0.06],
          },
          {
            name: '环比',
            type: 'line',
            stack: 'Total',
            data: [0, 0.05, 0.03, 0, 0, -0.06, -0.01, 0.015, -0.06],
          },
        ],
      })

      const chats7 = echarts.init(document.getElementById('chats-7'))

      // 指定图表的配置项和数据
      chats7.setOption({
        title: {
          text: '营业利润月趋势',
          subtext: '单位：万元 / %',
        },
        tooltip: {
          trigger: 'axis',
        },
        legend: {
          data: ['本期金额', '同比', '环比'],
          right: 0,
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true,
        },
        xAxis: [
          {
            type: 'category',
            boundaryGap: false,
            data: [
              '1',
              '2',
              '3',
              '4',
              '5',
              '6',
              '7',
              '8',
              '9',
              '10',
              '11',
              '12',
            ],
          },
        ],
        yAxis: [
          {
            type: 'value',
            scale: true,
            boundaryGap: [0.2, 0.2],
          },
          {
            type: 'value',
            scale: true,
            boundaryGap: [0.2, 0.2],
          },
        ],
        series: [
          {
            name: '本期金额',
            type: 'bar',
            stack: 'Total',
            data: [160, 60, 180, 180, 350, 300, 150, 50, 50, 150, 50, 50],
          },
          {
            name: '同比',
            type: 'line',
            stack: 'Total',
            data: [
              0, -0.02, -0.03, 0, 0, 0.06, 0.01, 0.015, -0.06, 0.01, 0.015,
              -0.06,
            ],
          },
          {
            name: '环比',
            type: 'line',
            stack: 'Total',
            data: [
              0, 0.05, 0.03, 0, 0, -0.06, -0.01, 0.015, -0.06, -0.01, 0.015,
              -0.06,
            ],
          },
        ],
      })

      const chats8 = echarts.init(document.getElementById('chats-8'))

      // 指定图表的配置项和数据
      chats8.setOption({
        title: {
          text: '净利润月趋势',
          subtext: '单位：万元 / %',
        },
        tooltip: {
          trigger: 'axis',
        },
        legend: {
          data: ['本期金额', '同比', '环比'],
          right: 0,
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true,
        },
        xAxis: [
          {
            type: 'category',
            boundaryGap: false,
            data: [
              '1',
              '2',
              '3',
              '4',
              '5',
              '6',
              '7',
              '8',
              '9',
              '10',
              '11',
              '12',
            ],
          },
        ],
        yAxis: [
          {
            type: 'value',
            scale: true,
            boundaryGap: [0.2, 0.2],
          },
          {
            type: 'value',
            scale: true,
            boundaryGap: [0.2, 0.2],
          },
        ],
        series: [
          {
            name: '本期金额',
            type: 'bar',
            stack: 'Total',
            data: [160, 60, 180, 180, 350, 300, 150, 50, 50, 150, 50, 50],
          },
          {
            name: '同比',
            type: 'line',
            stack: 'Total',
            data: [
              0, -0.02, -0.03, 0, 0, 0.06, 0.01, 0.015, -0.06, 0.01, 0.015,
              -0.06,
            ],
          },
          {
            name: '环比',
            type: 'line',
            stack: 'Total',
            data: [
              0, 0.05, 0.03, 0, 0, -0.06, -0.01, 0.015, -0.06, -0.01, 0.015,
              -0.06,
            ],
          },
        ],
      })

      const chats9 = echarts.init(document.getElementById('chats-9'))

      // 指定图表的配置项和数据
      chats9.setOption({
        title: {
          text: '利润趋势对比分析',
          subtext: '单位：万元',
        },
        tooltip: {
          trigger: 'axis',
        },
        legend: {
          data: ['营业利润', '利润总额', '净利润'],
          right: 0,
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true,
        },
        xAxis: [
          {
            type: 'category',
            boundaryGap: false,
            data: [
              '1',
              '2',
              '3',
              '4',
              '5',
              '6',
              '7',
              '8',
              '9',
              '10',
              '11',
              '12',
            ],
          },
        ],
        yAxis: [
          {
            type: 'value',
          },
        ],
        series: [
          {
            name: '营业利润',
            type: 'bar',
            data: [160, 60, 180, 180, 350, 300, 150, 50, 50, 150, 50, 50],
          },
          {
            name: '利润总额',
            type: 'bar',
            data: [160, 60, 180, 180, 350, 300, 150, 50, 50, 150, 50, 50],
          },
          {
            name: '净利润',
            type: 'bar',
            data: [160, 60, 180, 180, 350, 300, 150, 50, 50, 150, 50, 50],
          },
        ],
      })

      const chats10 = echarts.init(document.getElementById('chats-10'))

      // 指定图表的配置项和数据
      chats10.setOption({
        title: {
          text: '净利润率月度趋势',
          subtext: '单位：%',
        },
        tooltip: {
          trigger: 'axis',
        },
        legend: {
          data: ['净利润率'],
          right: 0,
        },
        xAxis: {
          type: 'category',
          data: ['1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11', '12'],
        },
        yAxis: {
          type: 'value',
        },
        series: [
          {
            data: [-7, 4, -3, 3, -1, -5, 6, -6, 2, -5, -6, -7],
            type: 'line',
          },
        ],
      })
    },  // end initCharts
  },  // end methods
}
</script>
<style scoped>
  .el-col > div {
    border: 1px solid #dcdfe5;
    margin-bottom: 10px;
  }
  .header {
    display: flex;
    padding: 15px 20px 0 20px;
    box-shadow: 5px -2px 5px 0;
    background: white;
  }
  .header .input-group {
    width: 180px;
    margin-right: 20px;
  }
  .header .btn-sm {
    height: 38px;
    padding: 0 20px;
  }
  .content {
    padding: 20px;
  }
  .one-item {
  }
  .flex {
    display: flex;
  }

  .j-between {
    justify-content: space-between;
  }

  .align-baseline {
    align-items: baseline;
  }

  .flex-col {
    display: flex;
    flex-direction: column;
  }

  .one-item .price {
    font-size: 24px;
    font-weight: 700;
  }

  .padding-20 {
    padding: 20px;
  }

  .padding-10 {
    padding: 10px;
  }

  .border_none >>> .el-row {
    border: none !important;
  }

  .margin-bottom-0 {
    margin-bottom: 0 !important;
  }

  .margin-top-10 {
    margin-top: 10px;
  }
  .margin-right-10 {
    margin-right: 10px;
  }
  .margin-bottom-10 {
    margin-bottom: 10px;
  }
  .margin-left-10 {
    margin-left: 10px;
  }

  /* .one-item {
    display: flex;
    justify-content: space-between;
    background: white;
    padding: 10px;
    margin-bottom: 20px;
  }
  .one-item i {
    font-size: 24px;
  } */
  .chats > div > div {
    background: white;
    padding: 10px;
    margin-bottom: 20px;
  }
</style>
