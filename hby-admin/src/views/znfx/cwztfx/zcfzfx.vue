<template>
  <div id="app">
    <div class="header fixed-top">
      <el-form :inline="true" label-width="80px">
        <el-form-item label="年">
          <el-select id="inputGroupSelectYear" class="form-select">
            <el-option value="2021">2021</el-option>
            <el-option value="2020">2020</el-option>
            <el-option value="2019">2019</el-option>
            <el-option value="2018">2018</el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button icon="el-icon-refresh-right" type="primary" />
        </el-form-item>
      </el-form>
    </div>
    <div class="content">
      <el-col :lg="24" :md="6" :sm="24">
        <div class="titleBar">资产情况分析</div>
      </el-col>

      <el-row :gutter="10">
        <el-col :lg="8" :md="6" :sm="12" class="border_none">
          <el-row :gutter="10">
            <el-col :lg="12" :md="6" :sm="12">
              <div
                class="one-item flex-col padding-10 margin-bottom-0"
                style="padding: 20px 10px"
              >
                <div class="flex align-baseline margin-bottom-10">
                  <div class="price">163.62</div>
                  <div class="unit">万元</div>
                </div>
                <div class="title margin-bottom-10">资产合计</div>
                <div class="compare flex">
                  <div class="margin-right-10">同比</div>
                  <el-link type="success">↑-0.46%</el-link>
                  <div class="margin-left-10 margin-right-10">环比</div>
                  <el-link type="success">↑-2.95%</el-link>
                </div>
              </div>
            </el-col>
            <el-col :lg="12" :md="6" :sm="12">
              <div
                class="one-item flex-col padding-10 margin-bottom-0"
                style="padding: 20px 10px"
              >
                <div class="flex align-baseline margin-bottom-10">
                  <div class="price">285.04</div>
                  <div class="unit">万元</div>
                </div>
                <div class="title margin-bottom-10">流动资产合计</div>
                <div class="compare flex">
                  <div class="margin-right-10">同比</div>
                  <el-link type="danger">⬇-5.5%</el-link>
                  <div class="margin-left-10 margin-right-10">环比</div>
                  <el-link type="danger">⬇-3.59%</el-link>
                </div>
              </div>
            </el-col>
          </el-row>
          <el-row :gutter="10">
            <el-col :lg="12" :md="6" :sm="12">
              <div
                class="one-item flex-col padding-10 margin-bottom-0"
                style="padding: 20px 10px"
              >
                <div class="flex align-baseline margin-bottom-10">
                  <div class="price">153.66</div>
                  <div class="unit">万元</div>
                </div>
                <div class="title margin-bottom-10">非流动资产合计</div>
                <div class="compare flex">
                  <div class="margin-right-10">同比</div>
                  <el-link type="success">↑-4.76%</el-link>
                  <div class="margin-left-10 margin-right-10">环比</div>
                  <el-link type="success">↑-4.76%</el-link>
                </div>
              </div>
            </el-col>
            <el-col :lg="12" :md="6" :sm="12">
              <div
                class="one-item flex-col padding-10 margin-bottom-0"
                style="padding: 20px 10px"
              >
                <div class="flex align-baseline margin-bottom-10">
                  <div class="price">0</div>
                  <div class="unit">万元</div>
                </div>
                <div class="title margin-bottom-10">其他非流动资产合计</div>
                <div class="compare flex">
                  <div class="margin-right-10">同比</div>
                  <el-link type="warning">-0%</el-link>
                  <div class="margin-left-10 margin-right-10">环比</div>
                  <el-link type="warning">-0%</el-link>
                </div>
              </div>
            </el-col>
          </el-row>
          <el-row :gutter="10">
            <el-col :lg="12" :md="6" :sm="12">
              <div
                class="one-item flex-col padding-10 margin-bottom-0"
                style="padding: 20px 10px"
              >
                <div class="flex align-baseline margin-bottom-10">
                  <div class="price">139.37</div>
                  <div class="unit">万元</div>
                </div>
                <div class="title margin-bottom-10">其他流动资产</div>
                <div class="compare flex">
                  <div class="margin-right-10">同比</div>
                  <el-link type="danger">⬇-4.76%</el-link>
                  <div class="margin-left-10 margin-right-10">环比</div>
                  <el-link type="danger">⬇-4.76%</el-link>
                </div>
              </div>
            </el-col>
            <el-col :lg="12" :md="6" :sm="12">
              <div
                class="one-item flex-col padding-10 margin-bottom-0"
                style="padding: 20px 10px"
              >
                <div class="flex align-baseline margin-bottom-10">
                  <div class="price">298.11</div>
                  <div class="unit">万元</div>
                </div>
                <div class="title margin-bottom-10">应收账款</div>
                <div class="compare flex">
                  <div class="margin-right-10">同比</div>
                  <el-link type="danger">⬇-4.76%</el-link>
                  <div class="margin-left-10 margin-right-10">环比</div>
                  <el-link type="danger">⬇-4.76%</el-link>
                </div>
              </div>
            </el-col>
          </el-row>
          <el-row :gutter="10">
            <el-col :lg="12" :md="6" :sm="12">
              <div
                class="one-item flex-col padding-10 margin-bottom-0"
                style="padding: 20px 10px"
              >
                <div class="flex align-baseline margin-bottom-10">
                  <div class="price">2152.15</div>
                  <div class="unit">万元</div>
                </div>
                <div class="title margin-bottom-10">存货</div>
                <div class="compare flex">
                  <div class="margin-right-10">同比</div>
                  <el-link type="danger">⬇-4.76%</el-link>
                  <div class="margin-left-10 margin-right-10">环比</div>
                  <el-link type="danger">⬇-4.76%</el-link>
                </div>
              </div>
            </el-col>
            <el-col :lg="12" :md="6" :sm="12">
              <div
                class="one-item flex-col padding-10 margin-bottom-0"
                style="padding: 20px 10px"
              >
                <div class="flex align-baseline margin-bottom-10">
                  <div class="price">1'408'727</div>
                  <!-- <div class="unit">万元</div> -->
                </div>
                <div class="title margin-bottom-10">货币资金</div>
                <div class="compare flex">
                  <div class="margin-right-10">同比</div>
                  <el-link type="danger">⬇-4.76%</el-link>
                  <div class="margin-left-10 margin-right-10">环比</div>
                  <el-link type="danger">⬇-4.76%</el-link>
                </div>
              </div>
            </el-col>
          </el-row>
        </el-col>
        <el-col :lg="6" :md="6" :sm="24">
          <div id="chats-1" style="width: 100%; height: 535px"></div>
        </el-col>
        <el-col :lg="10" :md="10" :sm="24">
          <div id="chats-2" style="width: 100%; height: 535px"></div>
        </el-col>
      </el-row>
      <el-row :gutter="10">
        <el-col :lg="12" :md="8" :sm="24">
          <div id="chats-3" style="width: 100%; height: 300px"></div>
        </el-col>
        <el-col :lg="12" :md="16" :sm="24">
          <div id="chats-4" style="width: 100%; height: 300px"></div>
        </el-col>
      </el-row>
      <el-row :gutter="10">
        <el-col :lg="12" :md="8" :sm="24">
          <div id="chats-5" style="width: 100%; height: 300px"></div>
        </el-col>
        <el-col :lg="12" :md="16" :sm="24">
          <div id="chats-6" style="width: 100%; height: 300px"></div>
        </el-col>
      </el-row>

      <!-- 负债情况分析 -->
      <el-col :lg="24" :md="6" :sm="24">
        <div class="titleBar">负债情况分析</div>
      </el-col>

      <el-row :gutter="10">
        <el-col :lg="8" :md="6" :sm="12" class="border_none">
          <el-row :gutter="10">
            <el-col :lg="12" :md="6" :sm="12">
              <div
                class="one-item flex-col padding-10 margin-bottom-0"
                style="padding: 20px 10px"
              >
                <div class="flex align-baseline margin-bottom-10">
                  <div class="price">163.62</div>
                  <div class="unit">万元</div>
                </div>
                <div class="title margin-bottom-10">负债合计</div>
                <div class="compare flex">
                  <div class="margin-right-10">同比</div>
                  <el-link type="success">↑-0.46%</el-link>
                  <div class="margin-left-10 margin-right-10">环比</div>
                  <el-link type="success">↑-2.95%</el-link>
                </div>
              </div>
            </el-col>
            <el-col :lg="12" :md="6" :sm="12">
              <div
                class="one-item flex-col padding-10 margin-bottom-0"
                style="padding: 20px 10px"
              >
                <div class="flex align-baseline margin-bottom-10">
                  <div class="price">285.04</div>
                  <div class="unit">万元</div>
                </div>
                <div class="title margin-bottom-10">流动负债合计</div>
                <div class="compare flex">
                  <div class="margin-right-10">同比</div>
                  <el-link type="danger">⬇-5.5%</el-link>
                  <div class="margin-left-10 margin-right-10">环比</div>
                  <el-link type="danger">⬇-3.59%</el-link>
                </div>
              </div>
            </el-col>
          </el-row>
          <el-row :gutter="10">
            <el-col :lg="12" :md="6" :sm="12">
              <div
                class="one-item flex-col padding-10 margin-bottom-0"
                style="padding: 20px 10px"
              >
                <div class="flex align-baseline margin-bottom-10">
                  <div class="price">153.66</div>
                  <div class="unit">万元</div>
                </div>
                <div class="title margin-bottom-10">非流动负债合计</div>
                <div class="compare flex">
                  <div class="margin-right-10">同比</div>
                  <el-link type="success">↑-4.76%</el-link>
                  <div class="margin-left-10 margin-right-10">环比</div>
                  <el-link type="success">↑-4.76%</el-link>
                </div>
              </div>
            </el-col>
            <el-col :lg="12" :md="6" :sm="12">
              <div
                class="one-item flex-col padding-10 margin-bottom-0"
                style="padding: 20px 10px"
              >
                <div class="flex align-baseline margin-bottom-10">
                  <div class="price">0</div>
                  <div class="unit">万元</div>
                </div>
                <div class="title margin-bottom-10">其他非流动负债合计</div>
                <div class="compare flex">
                  <div class="margin-right-10">同比</div>
                  <el-link type="warning">-0%</el-link>
                  <div class="margin-left-10 margin-right-10">环比</div>
                  <el-link type="warning">-0%</el-link>
                </div>
              </div>
            </el-col>
          </el-row>
          <el-row :gutter="10">
            <el-col :lg="12" :md="6" :sm="12">
              <div
                class="one-item flex-col padding-10 margin-bottom-0"
                style="padding: 20px 10px"
              >
                <div class="flex align-baseline margin-bottom-10">
                  <div class="price">139.37</div>
                  <div class="unit">万元</div>
                </div>
                <div class="title margin-bottom-10">其他流动负债</div>
                <div class="compare flex">
                  <div class="margin-right-10">同比</div>
                  <el-link type="danger">⬇-4.76%</el-link>
                  <div class="margin-left-10 margin-right-10">环比</div>
                  <el-link type="danger">⬇-4.76%</el-link>
                </div>
              </div>
            </el-col>
            <el-col :lg="12" :md="6" :sm="12">
              <div
                class="one-item flex-col padding-10 margin-bottom-0"
                style="padding: 20px 10px"
              >
                <div class="flex align-baseline margin-bottom-10">
                  <div class="price">298.11</div>
                  <div class="unit">万元</div>
                </div>
                <div class="title margin-bottom-10">应收账款</div>
                <div class="compare flex">
                  <div class="margin-right-10">同比</div>
                  <el-link type="danger">⬇-4.76%</el-link>
                  <div class="margin-left-10 margin-right-10">环比</div>
                  <el-link type="danger">⬇-4.76%</el-link>
                </div>
              </div>
            </el-col>
          </el-row>
          <el-row :gutter="10">
            <el-col :lg="12" :md="6" :sm="12">
              <div
                class="one-item flex-col padding-10 margin-bottom-0"
                style="padding: 20px 10px"
              >
                <div class="flex align-baseline margin-bottom-10">
                  <div class="price">2152.15</div>
                  <div class="unit">万元</div>
                </div>
                <div class="title margin-bottom-10">递延所得税负债</div>
                <div class="compare flex">
                  <div class="margin-right-10">同比</div>
                  <el-link type="danger">⬇-4.76%</el-link>
                  <div class="margin-left-10 margin-right-10">环比</div>
                  <el-link type="danger">⬇-4.76%</el-link>
                </div>
              </div>
            </el-col>
            <el-col :lg="12" :md="6" :sm="12">
              <div
                class="one-item flex-col padding-10 margin-bottom-0"
                style="padding: 20px 10px"
              >
                <div class="flex align-baseline margin-bottom-10">
                  <div class="price">112</div>
                  <div class="unit">万元</div>
                </div>
                <div class="title margin-bottom-10">其他应付款</div>
                <div class="compare flex">
                  <div class="margin-right-10">同比</div>
                  <el-link type="danger">⬇-4.76%</el-link>
                  <div class="margin-left-10 margin-right-10">环比</div>
                  <el-link type="danger">⬇-4.76%</el-link>
                </div>
              </div>
            </el-col>
          </el-row>
        </el-col>
        <el-col :lg="6" :md="6" :sm="24">
          <div id="chats-7" style="width: 100%; height: 535px"></div>
        </el-col>
        <el-col :lg="10" :md="10" :sm="24">
          <div id="chats-8" style="width: 100%; height: 535px"></div>
        </el-col>
      </el-row>
      <el-row :gutter="10">
        <el-col :lg="12" :md="8" :sm="24">
          <div id="chats-9" style="width: 100%; height: 300px"></div>
        </el-col>
        <el-col :lg="12" :md="16" :sm="24">
          <div id="chats-10" style="width: 100%; height: 300px"></div>
        </el-col>
      </el-row>
      <el-row :gutter="10">
        <el-col :lg="12" :md="8" :sm="24">
          <div id="chats-11" style="width: 100%; height: 300px"></div>
        </el-col>
        <el-col :lg="12" :md="16" :sm="24">
          <div id="chats-12" style="width: 100%; height: 300px"></div>
        </el-col>
      </el-row>
    </div>
  </div>
</template>
<script>
  import * as echarts from 'echarts'
  import { getZcfzfxData } from '@/api/stateAssets/cwztfx'
  export default {
    name: 'Zcfzfx',
    components: {},
    data() {
      return {
        queryYear: new Date().getFullYear().toString(),
        queryMonth: '0',
        overviewData: {
          totalAssets: '--', totalAssetsYoy: null,
          totalLiabilities: '--', totalLiabilitiesYoy: null,
          equity: '--', equityYoy: null,
          debtRatio: '--'
        }
      }
    },
    mounted() {
      this.initCharts()
      this.fetchData()
    },
    created() {},
    methods: {
      fetchData() {
        getZcfzfxData({ year: this.queryYear, month: this.queryMonth }).then(res => {
          const d = res.data || res
          if (d.overview) {
            const o = d.overview
            this.overviewData = {
              totalAssets: o.totalAssets || '--',
              totalAssetsYoy: o.totalAssetsYoy,
              totalLiabilities: o.totalLiabilities || '--',
              totalLiabilitiesYoy: o.totalLiabilitiesYoy,
              equity: o.equity || '--',
              equityYoy: o.equityYoy,
              debtRatio: o.debtRatio !== undefined ? (o.debtRatio * 100).toFixed(2) + '%' : '--'
            }
          }
          this.refreshCharts(d)
        }).catch(() => {})
      },
      refreshCharts(d) {
        if (!d) return
        const refreshChart = (id, option) => {
          const el = document.getElementById(id)
          if (!el) return
          const c = echarts.getInstanceByDom(el)
          if (c) c.setOption(option)
        }
        if (d.monthTrend && d.monthTrend.length) {
          const months = d.monthTrend.map(r => r.month)
          refreshChart('chats-1', { xAxis: { data: months }, series: [{ data: d.monthTrend.map(r => r.debtRatio * 100) }] })
          refreshChart('chats-2', { xAxis: { data: months }, series: [{ data: d.monthTrend.map(r => r.currentRatio) }, { data: d.monthTrend.map(r => r.quickRatio) }] })
        }
        if (d.companyRank && d.companyRank.length) {
          refreshChart('chats-3', { xAxis: { data: d.companyRank.map(r => r.companyName) }, series: [{ data: d.companyRank.map(r => r.totalAssets) }] })
        }
        if (d.yearTrend && d.yearTrend.length) {
          refreshChart('chats-4', {
            xAxis: [{ data: d.yearTrend.map(r => r.period) }],
            series: [{ data: d.yearTrend.map(r => r.totalAssets) }, { data: d.yearTrend.map(r => r.totalLiabilities) }, { data: d.yearTrend.map(r => r.equity) }]
          })
        }
      },
      initCharts() {
      var chartDom = document.getElementById('chats-1')
      var myChart = echarts.init(chartDom)
      var option
      option = {
        tooltip: {
          formatter: '{a} <br/>{b} : {c}%',
        },
        title: {
          text: '应收账款周转率',
          left: 'center',
        },
        grid: {
          left: '3%',
          right: '2%',
          bottom: '3%',
          containLabel: true,
        },
        series: [
          {
            name: 'Pressure',
            type: 'gauge',
            detail: {
              formatter: '{value}%',
            },
            data: [
              {
                value: 50,
                name: '应收账款周转率',
              },
            ],
          },
        ],
      }
      option && myChart.setOption(option)
      //资产合计月趋势
      var chartDom2 = document.getElementById('chats-2')
      var myChart2 = echarts.init(chartDom2)
      var option2
      option2 = {
        title: {
          text: '资产合计月趋势',
          left: 'left',
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'cross',
          },
        },
        grid: {
          left: '3%',
          right: '2%',
          bottom: '3%',
          containLabel: true,
        },
        toolbox: {
          feature: {
            dataView: { show: true, readOnly: false },
            restore: { show: true },
            saveAsImage: { show: true },
          },
        },
        legend: {
          data: ['资产合计', '股东权益合计', '资产负债率'],
        },
        xAxis: [
          {
            type: 'category',
            axisTick: {
              alignWithLabel: true,
            },
            // prettier-ignore
            data: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'],
          },
        ],
        yAxis: [
          {
            type: 'value',
            name: '',
            position: 'right',
            alignTicks: true,
            axisLine: {
              show: true,
            },
            axisLabel: {
              formatter: '{value} %',
            },
          },
          {
            type: 'value',
            name: '万元/%',
            position: 'left',
            alignTicks: true,
            axisLine: {
              show: true,
            },
            axisLabel: {
              formatter: '{value} ',
            },
          },
        ],
        series: [
          {
            name: '资产合计',
            type: 'bar',
            data: [
              2.0, 4.9, 7.0, 23.2, 25.6, 76.7, 135.6, 162.2, 32.6, 20.0, 6.4,
              3.3,
            ],
          },
          {
            name: '股东权益合计',
            type: 'bar',
            data: [
              2.0, 4.9, 7.0, 23.2, 25.6, 76.7, 135.6, 162.2, 32.6, 20.0, 6.4,
              3.3,
            ],
          },
          {
            name: '资产负债率',
            type: 'line',
            yAxisIndex: 1,
            data: [
              2.0, 2.2, 3.3, 4.5, 6.3, 10.2, 20.3, 23.4, 23.0, 16.5, 12.0, 6.2,
            ],
          },
        ],
      }
      option2 && myChart2.setOption(option2)
      //应收账款Top10【联动】
      var chartDom3 = document.getElementById('chats-3')
      var myChart3 = echarts.init(chartDom3)
      var option3
      option3 = {
        title: {
          text: '应收账款Top10【联动】',
          left: 'left',
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'cross',
          },
        },
        grid: {
          left: '3%',
          right: '2%',
          bottom: '3%',
          containLabel: true,
        },
        toolbox: {
          feature: {
            dataView: { show: true, readOnly: false },
            restore: { show: true },
            saveAsImage: { show: true },
          },
        },
        legend: {
          data: ['应收金额'],
        },
        xAxis: [
          {
            type: 'category',

            // prettier-ignore
            data: ['部门1', '部门1', '部门1', '部门1', '部门1', '部门1', '部门1', '部门1', '部门1', '部门1', '部门1', '部门1'],
          },
        ],
        yAxis: [
          {
            type: 'value',
            name: '万元',
            position: 'left',
            alignTicks: true,
            axisLine: {
              show: true,
            },
            axisLabel: {
              formatter: '{value} ',
            },
          },
        ],
        series: [
          {
            name: '应收金额',
            type: 'bar',
            data: [
              2.0, 4.9, 7.0, 23.2, 25.6, 76.7, 135.6, 162.2, 32.6, 20.0, 6.4,
              3.3,
            ],
          },
        ],
      }
      option3 && myChart3.setOption(option3)
      //应收账款月趋势
      var chartDom4 = document.getElementById('chats-4')
      var myChart4 = echarts.init(chartDom4)
      var option4
      option4 = {
        title: {
          text: '应收账款月趋势',
          left: 'left',
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'cross',
          },
        },
        grid: {
          left: '3%',
          right: '2%',
          bottom: '3%',
          containLabel: true,
        },
        toolbox: {
          feature: {
            dataView: { show: true, readOnly: false },
            restore: { show: true },
            saveAsImage: { show: true },
          },
        },
        legend: {
          data: ['应收金额', '环比'],
        },
        xAxis: [
          {
            type: 'category',
            // prettier-ignore
            data: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'],
          },
        ],
        yAxis: [
          {
            type: 'value',
            name: '',
            position: 'right',
            alignTicks: true,
            axisLine: {
              show: true,
            },
            axisLabel: {
              formatter: '{value} %',
            },
          },
          {
            type: 'value',
            name: '万元/%',
            position: 'left',
            alignTicks: true,
            axisLine: {
              show: true,
            },
            axisLabel: {
              formatter: '{value} ',
            },
          },
        ],
        series: [
          {
            name: '环比',
            type: 'line',
            data: [
              2.0, 4.9, 7.0, 23.2, 25.6, 76.7, 135.6, 162.2, 32.6, 20.0, 6.4,
              3.3,
            ],
          },
          {
            name: '应收金额',
            type: 'bar',
            yAxisIndex: 1,
            data: [
              2.0, 2.2, 3.3, 4.5, 6.3, 10.2, 20.3, 23.4, 23.0, 16.5, 12.0, 6.2,
            ],
          },
        ],
      }
      option4 && myChart4.setOption(option4)
      //资产排名Top10【联动】
      var chartDom5 = document.getElementById('chats-5')
      var myChart5 = echarts.init(chartDom5)
      var option5
      option5 = {
        title: {
          text: '资产排名Top10【联动】',
          left: 'left',
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'cross',
          },
        },
        grid: {
          left: '3%',
          right: '2%',
          bottom: '3%',
          containLabel: true,
        },
        toolbox: {
          feature: {
            dataView: { show: true, readOnly: false },
            restore: { show: true },
            saveAsImage: { show: true },
          },
        },
        legend: {
          data: ['资产合计'],
        },
        xAxis: [
          {
            type: 'category',

            // prettier-ignore
            data: ['公司1', '公司2', '公司3', '公司4', '公司5', '公司6', '公司7', '公司8', '公司9', '公司10'],
          },
        ],
        yAxis: [
          {
            type: 'value',
            name: '万元',
            position: 'left',
            alignTicks: true,
            axisLine: {
              show: true,
            },
            axisLabel: {
              formatter: '{value} ',
            },
          },
        ],
        series: [
          {
            name: '资产合计',
            type: 'bar',
            data: [2.0, 4.9, 7.0, 23.2, 25.6, 76.7, 135.6, 162.2, 32.6, 20.0],
          },
        ],
      }
      option5 && myChart5.setOption(option5)
      //资产月趋势对比
      var chartDom6 = document.getElementById('chats-6')
      var myChart6 = echarts.init(chartDom6)
      var option6
      option6 = {
        title: {
          text: '资产月趋势对比',
          left: 'left',
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'cross',
          },
        },
        grid: {
          left: '3%',
          right: '2%',
          bottom: '3%',
          containLabel: true,
        },
        toolbox: {
          feature: {
            dataView: { show: true, readOnly: false },
            restore: { show: true },
            saveAsImage: { show: true },
          },
        },
        legend: {
          data: ['资产合计', '流动资产', '非流动资产'],
        },
        xAxis: [
          {
            type: 'category',

            // prettier-ignore
            data: ['公司1', '公司2', '公司3', '公司4', '公司5', '公司6', '公司7', '公司8', '公司9', '公司10'],
          },
        ],
        yAxis: [
          {
            type: 'value',
            name: '万元',
            position: 'left',
            alignTicks: true,
            axisLine: {
              show: true,
            },
            axisLabel: {
              formatter: '{value} ',
            },
          },
        ],
        series: [
          {
            name: '资产合计',
            type: 'bar',
            data: [12.0, 14.9, 27.0, 23.2, 25.6, 30.7, 51.6, 42.2, 32.6, 20.0],
          },
          {
            name: '流动资产',
            type: 'bar',
            data: [14.0, 16.2, 17.0, 33.2, 25.6, 31.7, 51.6, 41.2, 22.6, 10.0],
          },
          {
            name: '非流动资产',
            type: 'bar',
            data: [17.0, 19.9, 21.0, 22.2, 27.6, 31.7, 41.6, 43.2, 12.6, 22.0],
          },
        ],
      }
      option6 && myChart6.setOption(option6)
      //应付账款周转率
      var chartDom7 = document.getElementById('chats-7')
      var myChart7 = echarts.init(chartDom7)
      var option7
      option7 = {
        tooltip: {
          formatter: '{a} <br/>{b} : {c}%',
        },
        title: {
          text: '应付账款周转率',
          left: 'center',
        },
        grid: {
          left: '3%',
          right: '2%',
          bottom: '3%',
          containLabel: true,
        },
        series: [
          {
            name: 'Pressure',
            type: 'gauge',
            detail: {
              formatter: '{value}%',
            },
            data: [
              {
                value: 50,
                name: '应付账款周转率',
              },
            ],
          },
        ],
      }
      option7 && myChart7.setOption(option7)
      //负债合计月趋势
      var chartDom8 = document.getElementById('chats-8')
      var myChart8 = echarts.init(chartDom8)
      var option8
      option8 = {
        title: {
          text: '负债合计月趋势',
          left: 'left',
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'cross',
          },
        },
        grid: {
          left: '3%',
          right: '2%',
          bottom: '3%',
          containLabel: true,
        },
        toolbox: {
          feature: {
            dataView: { show: true, readOnly: false },
            restore: { show: true },
            saveAsImage: { show: true },
          },
        },
        legend: {
          data: ['负债合计', '股东权益合计', '资产负债率'],
        },
        xAxis: [
          {
            type: 'category',
            axisTick: {
              alignWithLabel: true,
            },
            // prettier-ignore
            data: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'],
          },
        ],
        yAxis: [
          {
            type: 'value',
            name: '',
            position: 'right',
            alignTicks: true,
            axisLine: {
              show: true,
            },
            axisLabel: {
              formatter: '{value} %',
            },
          },
          {
            type: 'value',
            name: '万元/%',
            position: 'left',
            alignTicks: true,
            axisLine: {
              show: true,
            },
            axisLabel: {
              formatter: '{value} ',
            },
          },
        ],
        series: [
          {
            name: '负债合计',
            type: 'bar',
            data: [
              2.0, 4.9, 7.0, 23.2, 25.6, 76.7, 135.6, 162.2, 32.6, 20.0, 6.4,
              3.3,
            ],
          },
          {
            name: '股东权益合计',
            type: 'bar',
            data: [
              2.0, 4.9, 7.0, 23.2, 25.6, 76.7, 135.6, 162.2, 32.6, 20.0, 6.4,
              3.3,
            ],
          },
          {
            name: '资产负债率',
            type: 'line',
            yAxisIndex: 1,
            data: [
              2.0, 2.2, 3.3, 4.5, 6.3, 10.2, 20.3, 23.4, 23.0, 16.5, 12.0, 6.2,
            ],
          },
        ],
      }
      option8 && myChart8.setOption(option8)
      //应付账款Top10【联动】
      var chartDom9 = document.getElementById('chats-9')
      var myChart9 = echarts.init(chartDom9)
      var option9
      option9 = {
        title: {
          text: '应付账款Top10【联动】',
          left: 'left',
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'cross',
          },
        },
        grid: {
          left: '3%',
          right: '2%',
          bottom: '3%',
          containLabel: true,
        },
        toolbox: {
          feature: {
            dataView: { show: true, readOnly: false },
            restore: { show: true },
            saveAsImage: { show: true },
          },
        },
        legend: {
          data: ['应付金额'],
        },
        xAxis: [
          {
            type: 'category',

            // prettier-ignore
            data: ['部门1', '部门1', '部门1', '部门1', '部门1', '部门1', '部门1', '部门1', '部门1', '部门1', '部门1', '部门1'],
          },
        ],
        yAxis: [
          {
            type: 'value',
            name: '万元',
            position: 'left',
            alignTicks: true,
            axisLine: {
              show: true,
            },
            axisLabel: {
              formatter: '{value} ',
            },
          },
        ],
        series: [
          {
            name: '应付金额',
            type: 'bar',
            data: [
              2.0, 4.9, 7.0, 23.2, 25.6, 76.7, 135.6, 162.2, 32.6, 20.0, 6.4,
              3.3,
            ],
          },
        ],
      }
      option9 && myChart9.setOption(option9)
      //应付账款月趋势
      var chartDom10 = document.getElementById('chats-10')
      var myChart10 = echarts.init(chartDom10)
      var option10
      option10 = {
        title: {
          text: '应付账款月趋势',
          left: 'left',
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'cross',
          },
        },
        grid: {
          left: '3%',
          right: '2%',
          bottom: '3%',
          containLabel: true,
        },
        toolbox: {
          feature: {
            dataView: { show: true, readOnly: false },
            restore: { show: true },
            saveAsImage: { show: true },
          },
        },
        legend: {
          data: ['应付金额', '环比'],
        },
        xAxis: [
          {
            type: 'category',
            // prettier-ignore
            data: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'],
          },
        ],
        yAxis: [
          {
            type: 'value',
            name: '',
            position: 'right',
            alignTicks: true,
            axisLine: {
              show: true,
            },
            axisLabel: {
              formatter: '{value} %',
            },
          },
          {
            type: 'value',
            name: '万元/%',
            position: 'left',
            alignTicks: true,
            axisLine: {
              show: true,
            },
            axisLabel: {
              formatter: '{value} ',
            },
          },
        ],
        series: [
          {
            name: '环比',
            type: 'line',
            data: [
              2.0, 4.9, 7.0, 23.2, 25.6, 76.7, 135.6, 162.2, 32.6, 20.0, 6.4,
              3.3,
            ],
          },
          {
            name: '应付金额',
            type: 'bar',
            yAxisIndex: 1,
            data: [
              2.0, 2.2, 3.3, 4.5, 6.3, 10.2, 20.3, 23.4, 23.0, 16.5, 12.0, 6.2,
            ],
          },
        ],
      }
      option10 && myChart10.setOption(option10)
      //负债排名Top10
      var chartDom11 = document.getElementById('chats-11')
      var myChart11 = echarts.init(chartDom11)
      var option11
      option11 = {
        title: {
          text: '负债排名Top10',
          left: 'left',
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'cross',
          },
        },
        grid: {
          left: '3%',
          right: '2%',
          bottom: '3%',
          containLabel: true,
        },
        toolbox: {
          feature: {
            dataView: { show: true, readOnly: false },
            restore: { show: true },
            saveAsImage: { show: true },
          },
        },
        legend: {
          data: ['负债合计'],
        },
        xAxis: [
          {
            type: 'category',

            // prettier-ignore
            data: ['公司1', '公司2', '公司3', '公司4', '公司5', '公司6', '公司7', '公司8', '公司9', '公司10'],
          },
        ],
        yAxis: [
          {
            type: 'value',
            name: '万元',
            position: 'left',
            alignTicks: true,
            axisLine: {
              show: true,
            },
            axisLabel: {
              formatter: '{value} ',
            },
          },
        ],
        series: [
          {
            name: '负债合计',
            type: 'bar',
            data: [2.0, 4.9, 7.0, 23.2, 25.6, 76.7, 135.6, 162.2, 32.6, 20.0],
          },
        ],
      }
      option11 && myChart11.setOption(option11)
      //负债月趋势对比
      var chartDom12 = document.getElementById('chats-12')
      var myChart12 = echarts.init(chartDom12)
      var option12
      option12 = {
        title: {
          text: '负债月趋势对比',
          left: 'left',
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'cross',
          },
        },
        grid: {
          left: '3%',
          right: '2%',
          bottom: '3%',
          containLabel: true,
        },
        toolbox: {
          feature: {
            dataView: { show: true, readOnly: false },
            restore: { show: true },
            saveAsImage: { show: true },
          },
        },
        legend: {
          data: ['负债合计', '流动负债', '非流动负债', '所有者权期末数'],
        },
        xAxis: [
          {
            type: 'category',

            // prettier-ignore
            data: ['公司1', '公司2', '公司3', '公司4', '公司5', '公司6', '公司7', '公司8', '公司9', '公司10'],
          },
        ],
        yAxis: [
          {
            type: 'value',
            name: '万元',
            position: 'left',
            alignTicks: true,
            axisLine: {
              show: true,
            },
            axisLabel: {
              formatter: '{value} ',
            },
          },
        ],
        series: [
          {
            name: '负债合计',
            type: 'bar',
            data: [12.0, 14.9, 27.0, 23.2, 25.6, 30.7, 51.6, 42.2, 32.6, 20.0],
          },
          {
            name: '流动负债',
            type: 'bar',
            data: [14.0, 16.2, 17.0, 33.2, 25.6, 31.7, 51.6, 41.2, 22.6, 10.0],
          },
          {
            name: '非流动负债',
            type: 'bar',
            data: [17.0, 19.9, 21.0, 22.2, 27.6, 31.7, 41.6, 43.2, 12.6, 22.0],
          },
          {
            name: '所有者权期末数',
            type: 'bar',
            data: [17.0, 19.9, 21.0, 22.2, 27.6, 31.7, 41.6, 43.2, 12.6, 22.0],
          },
        ],
      }
      option6 && myChart12.setOption(option12)
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
    display: flex;
    /* justify-content: space-between; */
    background: white;
    padding: 5px;
    margin-bottom: 10px;
    font-size: 16px;
  }

  .one-item p {
    font-size: 16px;
  }
  .chats > div > div {
    background: white;
    padding: 10px;
    margin-bottom: 20px;
  }
  .title {
    font-size: 16px;
    font-weight: 600;
  }
  .duibi {
    display: flex;
    font-size: 16px;
  }
  .border_none >>> .el-row {
    border: none;
  }

  style scoped > .el-col > div {
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
  .titleBar {
    font-size: 30px;
    font-weight: 600;
    background-color: #80b6f4;
    padding: 10px;
  }
  .margin-left-0 {
    margin-left: 0;
  }
  .margin-right-0 {
    margin-right: 0;
  }
</style>
