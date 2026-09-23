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
        <div class="titleBar">应收账款分析</div>
      </el-col>

      <el-row :gutter="10">
        <el-col :lg="16" :md="6" :sm="12" class="border_none">
          <el-row :gutter="10">
            <el-col :lg="6" :md="6" :sm="12">
              <div
                class="one-item flex-col padding-10 margin-bottom-0"
                style="padding: 20px 10px"
              >
                <div class="flex align-baseline margin-bottom-10">
                  <div class="price">2152.15</div>
                  <div class="unit">万元</div>
                </div>
                <div class="title margin-bottom-10">应收金额</div>
                <div class="compare flex">
                  <div class="margin-right-10">同比</div>
                  <el-link type="danger">⬇-4.76%</el-link>
                  <div class="margin-left-10 margin-right-10">环比</div>
                  <el-link type="danger">⬇-4.76%</el-link>
                </div>
              </div>
            </el-col>
            <el-col :lg="6" :md="6" :sm="12">
              <div
                class="one-item flex-col padding-10 margin-bottom-0"
                style="padding: 20px 10px"
              >
                <div class="flex align-baseline margin-bottom-10">
                  <div class="price">163.62</div>
                  <div class="unit">万元</div>
                </div>
                <div class="title margin-bottom-10">收款金额</div>
                <div class="compare flex">
                  <div class="margin-right-10">同比</div>
                  <el-link type="success">↑-0.46%</el-link>
                  <div class="margin-left-10 margin-right-10">环比</div>
                  <el-link type="success">↑-2.95%</el-link>
                </div>
              </div>
            </el-col>
            <el-col :lg="6" :md="6" :sm="12">
              <div
                class="one-item flex-col padding-10 margin-bottom-0"
                style="padding: 20px 10px"
              >
                <div class="flex align-baseline margin-bottom-10">
                  <div class="price">285.04</div>
                  <div class="unit">万元</div>
                </div>
                <div class="title margin-bottom-10">应收余额</div>
                <div class="compare flex">
                  <div class="margin-right-10">同比</div>
                  <el-link type="danger">⬇-5.5%</el-link>
                  <div class="margin-left-10 margin-right-10">环比</div>
                  <el-link type="danger">⬇-3.59%</el-link>
                </div>
              </div>
            </el-col>
            <el-col :lg="6" :md="6" :sm="12">
              <div
                class="one-item flex-col padding-10 margin-bottom-0"
                style="padding: 20px 10px"
              >
                <div class="flex align-baseline margin-bottom-10">
                  <div class="price">153.66</div>
                  <div class="unit">万元</div>
                </div>
                <div class="title margin-bottom-10">应收账款发生额</div>
                <div class="compare flex">
                  <div class="margin-right-10">同比</div>
                  <el-link type="success">↑-4.76%</el-link>
                  <div class="margin-left-10 margin-right-10">环比</div>
                  <el-link type="success">↑-4.76%</el-link>
                </div>
              </div>
            </el-col>
          </el-row>
          <el-row :gutter="10">
            <el-col :lg="6" :md="6" :sm="12">
              <div
                class="one-item flex-col padding-10 margin-bottom-0"
                style="padding: 20px 10px"
              >
                <div class="flex align-baseline margin-bottom-10">
                  <div class="price">0</div>
                  <div class="unit">万元</div>
                </div>
                <div class="title margin-bottom-10">应付金额</div>
                <div class="compare flex">
                  <div class="margin-right-10">同比</div>
                  <el-link type="warning">-0%</el-link>
                  <div class="margin-left-10 margin-right-10">环比</div>
                  <el-link type="warning">-0%</el-link>
                </div>
              </div>
            </el-col>
            <el-col :lg="6" :md="6" :sm="12">
              <div
                class="one-item flex-col padding-10 margin-bottom-0"
                style="padding: 20px 10px"
              >
                <div class="flex align-baseline margin-bottom-10">
                  <div class="price">139.37</div>
                  <div class="unit">万元</div>
                </div>
                <div class="title margin-bottom-10">付款金额</div>
                <div class="compare flex">
                  <div class="margin-right-10">同比</div>
                  <el-link type="danger">⬇-4.76%</el-link>
                  <div class="margin-left-10 margin-right-10">环比</div>
                  <el-link type="danger">⬇-4.76%</el-link>
                </div>
              </div>
            </el-col>
            <el-col :lg="6" :md="6" :sm="12">
              <div
                class="one-item flex-col padding-10 margin-bottom-0"
                style="padding: 20px 10px"
              >
                <div class="flex align-baseline margin-bottom-10">
                  <div class="price">298.11</div>
                  <div class="unit">万元</div>
                </div>
                <div class="title margin-bottom-10">应付余额</div>
                <div class="compare flex">
                  <div class="margin-right-10">同比</div>
                  <el-link type="danger">⬇-4.76%</el-link>
                  <div class="margin-left-10 margin-right-10">环比</div>
                  <el-link type="danger">⬇-4.76%</el-link>
                </div>
              </div>
            </el-col>

            <el-col :lg="6" :md="6" :sm="12">
              <div
                class="one-item flex-col padding-10 margin-bottom-0"
                style="padding: 20px 10px"
              >
                <div class="flex align-baseline margin-bottom-10">
                  <div class="price">1'408'727</div>
                  <!-- <div class="unit">万元</div> -->
                </div>
                <div class="title margin-bottom-10">应付账款发生额</div>
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
        <el-col :lg="4" :md="6" :sm="24">
          <div id="chats-1" style="width: 100%; height: 263px"></div>
        </el-col>
        <el-col :lg="4" :md="10" :sm="24">
          <div id="chats-2" style="width: 100%; height: 263px"></div>
        </el-col>
      </el-row>
      <el-row :gutter="10">
        <el-col :lg="8" :md="6" :sm="24">
          <div id="chats-3" style="width: 100%; height: 263px"></div>
        </el-col>
        <el-col :lg="8" :md="6" :sm="24">
          <div id="chats-4" style="width: 100%; height: 263px"></div>
        </el-col>
        <el-col :lg="8" :md="6" :sm="24">
          <div id="chats-5" style="width: 100%; height: 263px"></div>
        </el-col>
      </el-row>
      <el-row :gutter="10">
        <el-col :lg="8" :md="6" :sm="24">
          <div id="chats-6" style="width: 100%; height: 263px"></div>
        </el-col>
        <el-col :lg="8" :md="6" :sm="24">
          <div id="chats-7" style="width: 100%; height: 263px"></div>
        </el-col>
        <el-col :lg="8" :md="6" :sm="24">
          <div id="chats-8" style="width: 100%; height: 263px"></div>
        </el-col>
      </el-row>
      <el-col :lg="24" :md="6" :sm="24">
        <div class="titleBar">应付账款分析</div>
      </el-col>
      <el-row :gutter="10">
        <el-col :lg="8" :md="6" :sm="24">
          <div id="chats-9" style="width: 100%; height: 263px"></div>
        </el-col>
        <el-col :lg="8" :md="6" :sm="24">
          <div id="chats-10" style="width: 100%; height: 263px"></div>
        </el-col>
        <el-col :lg="8" :md="6" :sm="24">
          <div id="chats-11" style="width: 100%; height: 263px"></div>
        </el-col>
      </el-row>
      <el-row :gutter="10">
        <el-col :lg="8" :md="6" :sm="24">
          <div id="chats-12" style="width: 100%; height: 263px"></div>
        </el-col>
        <el-col :lg="8" :md="6" :sm="24">
          <div id="chats-13" style="width: 100%; height: 263px"></div>
        </el-col>
        <el-col :lg="8" :md="6" :sm="24">
          <div id="chats-14" style="width: 100%; height: 263px"></div>
        </el-col>
      </el-row>
    </div>
  </div>
</template>
<script>
  import * as echarts from 'echarts'
  import { getYsyfzkfxData } from '@/api/stateAssets/cwztfx'
  export default {
    name: 'Ysyfzkfx',
    components: {},
    data() {
      return {
        queryYear: new Date().getFullYear().toString(),
        queryMonth: '0',
        overviewData: {
          receivable: '--', receivableYoy: null,
          payable: '--', payableYoy: null,
          receivableTurnover: '--',
          payableTurnover: '--'
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
        getYsyfzkfxData({ year: this.queryYear, month: this.queryMonth }).then(res => {
          const d = res.data || res
          if (d.overview) {
            const o = d.overview
            this.overviewData = {
              receivable: o.receivable || '--',
              receivableYoy: o.receivableYoy,
              payable: o.payable || '--',
              payableYoy: o.payableYoy,
              receivableTurnover: o.receivableTurnover !== undefined ? o.receivableTurnover.toFixed(2) : '--',
              payableTurnover: o.payableTurnover !== undefined ? o.payableTurnover.toFixed(2) : '--'
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
          refreshChart('chats-1', { xAxis: { data: months }, series: [{ data: d.monthTrend.map(r => r.receivable) }] })
          refreshChart('chats-2', { xAxis: { data: months }, series: [{ data: d.monthTrend.map(r => r.payable) }] })
          refreshChart('chats-5', { xAxis: [{ data: months }], series: [{ data: d.monthTrend.map(r => r.receivable) }, { data: d.monthTrend.map(r => r.yoy) }] })
          refreshChart('chats-6', { xAxis: [{ data: months }], series: [{ data: d.monthTrend.map(r => r.payable) }, { data: d.monthTrend.map(r => r.yoy) }] })
        }
        if (d.companyRankReceivable && d.companyRankReceivable.length) {
          refreshChart('chats-3', { xAxis: { data: d.companyRankReceivable.map(r => r.companyName) }, series: [{ data: d.companyRankReceivable.map(r => r.amount) }] })
        }
        if (d.companyRankPayable && d.companyRankPayable.length) {
          refreshChart('chats-4', { xAxis: { data: d.companyRankPayable.map(r => r.companyName) }, series: [{ data: d.companyRankPayable.map(r => r.amount) }] })
        }
      },
      initCharts() {
      //应收余额
      var chartDom = document.getElementById('chats-1')
      var myChart = echarts.init(chartDom)
      var option
      option = {
        tooltip: {
          formatter: '{a} <br/>{b} : {c}%',
        },
        title: {
          text: '应收余额',
          left: 'center',
        },
        grid: {
          left: '1%',
          right: '1%',
          bottom: '1%',
          containLabel: true,
        },
        series: [
          {
            name: 'Pressure',
            type: 'gauge',
            detail: {
              formatter: '{value}万元',
            },
            data: [
              {
                value: 150,
                name: '应收余额',
              },
            ],
          },
        ],
      }
      option && myChart.setOption(option)
      //应付余额
      var chartDom2 = document.getElementById('chats-2')
      var myChart2 = echarts.init(chartDom2)
      var option2
      option2 = {
        tooltip: {
          formatter: '{a} <br/>{b} : {c}%',
        },
        title: {
          text: '应付余额',
          left: 'center',
        },
        grid: {
          left: '1%',
          right: '1%',
          bottom: '1%',
          containLabel: true,
        },
        series: [
          {
            name: 'Pressure',
            type: 'gauge',
            detail: {
              formatter: '{value}万元',
            },
            data: [
              {
                value: 150,
                name: '应付余额',
              },
            ],
          },
        ],
      }
      option2 && myChart2.setOption(option2)
      //1.年度应收金额与收款金额对比分析【联动2】
      var chartDom3 = document.getElementById('chats-3')
      var myChart3 = echarts.init(chartDom3)
      var option3
      option3 = {
        title: {
          text: '1.年度应收金额与收款金额对比分析【联动2】',
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
          top: '20%',
          bottom: '15%',
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
          data: ['应收金额', '收款金额'],
          y: 'bottom',
        },
        xAxis: [
          {
            type: 'category',

            // prettier-ignore
            data: ['2012', '2013', '2014', '2015', '2016', '2017', '2018', '2019', '2020', '2021'],
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
            data: [12.0, 14.9, 27.0, 23.2, 25.6, 30.7, 51.6, 42.2, 32.6, 20.0],
          },
          {
            name: '收款金额',
            type: 'bar',
            data: [14.0, 16.2, 17.0, 33.2, 25.6, 31.7, 51.6, 41.2, 22.6, 10.0],
          },
        ],
      }
      option3 && myChart3.setOption(option3)
      //2.各月份应收金额与收款金额对比分析
      var chartDom4 = document.getElementById('chats-4')
      var myChart4 = echarts.init(chartDom4)
      var option4
      option4 = {
        title: {
          text: '2.各月份应收金额与收款金额对比分析',
          left: 'left',
        },
        grid: {
          left: '3%',
          right: '4%',
          top: '20%',
          bottom: '10%',
          containLabel: true,
        },
        legend: {
          data: ['应收金额', '收款金额'],
          y: 'bottom',
        },
        xAxis: {
          type: 'category',
          boundaryGap: false,
          data: ['1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11', '12'],
        },
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
            type: 'line',
            stack: 'Total',
            areaStyle: {},
            emphasis: {
              focus: 'series',
            },
            data: [120, 132, 101, 134, 90, 230, 210, 120, 132, 101, 134, 90],
          },
          {
            name: '收款金额',
            type: 'line',
            stack: 'Total',
            areaStyle: {},
            emphasis: {
              focus: 'series',
            },
            data: [220, 182, 191, 234, 290, 330, 310, 220, 182, 191, 234, 290],
          },
        ],
      }
      option4 && myChart4.setOption(option4)
      //3.各公司应收金额与收款金额对比分析
      var chartDom5 = document.getElementById('chats-5')
      var myChart5 = echarts.init(chartDom5)
      var option5
      option5 = {
        title: {
          text: '3.各公司应收金额与收款金额对比分析',
          left: 'left',
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'cross',
          },
        },
        grid: {
          left: '1%',
          right: '1%',
          top: '20%',
          bottom: '15%',
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
          data: ['应收金额', '收款金额'],
          y: 'bottom',
        },
        xAxis: [
          {
            type: 'category',

            // prettier-ignore
            data: ['公司12', '公司13', '公司14', '公司15', '公司16', '公司17', '公司18', '公司19', '公司20', '公司21'],
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
            data: [12.0, 14.9, 27.0, 23.2, 25.6, 30.7, 51.6, 42.2, 32.6, 20.0],
          },
          {
            name: '收款金额',
            type: 'bar',
            data: [14.0, 16.2, 17.0, 33.2, 25.6, 31.7, 51.6, 41.2, 22.6, 10.0],
          },
        ],
      }
      option5 && myChart5.setOption(option5)
      //4.应收账款余额分析
      var chartDom6 = document.getElementById('chats-6')
      var myChart6 = echarts.init(chartDom6)
      var option6
      option6 = {
        title: {
          text: '4.应收账款余额分析',
          left: 'left',
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'cross',
          },
        },
        grid: {
          left: '1%',
          right: '1%',
          top: '20%',
          bottom: '15%',
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
          data: ['应收余额'],
          y: 'bottom',
        },
        xAxis: [
          {
            // prettier-ignore
            data: ['公司12', '公司13', '公司14', '公司15', '公司16', '公司17', '公司18', '公司19', '公司20', '公司21'],
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
            name: '应收余额',
            type: 'bar',
            data: [12.0, 14.9, 27.0, 23.2, 25.6, 30.7, 51.6, 42.2, 32.6, 20.0],
          },
        ],
      }
      option6 && myChart6.setOption(option6)
      //5.应收账款发生额分析
      var chartDom7 = document.getElementById('chats-7')
      var myChart7 = echarts.init(chartDom7)
      var option7
      option7 = {
        title: {
          text: '5.应收账款发生额分析',
          left: 'left',
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'cross',
          },
        },
        grid: {
          left: '1%',
          right: '1%',
          top: '20%',
          bottom: '15%',
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
          data: ['应收账款发生额'],
          y: 'bottom',
        },
        xAxis: [
          {
            // prettier-ignore
            data: ['公司12', '公司13', '公司14', '公司15', '公司16', '公司17', '公司18', '公司19', '公司20', '公司21'],
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
            name: '应收账款发生额',
            type: 'bar',
            data: [12.0, 14.9, 27.0, 23.2, 25.6, 30.7, 51.6, 42.2, 32.6, 20.0],
          },
        ],
      }
      option7 && myChart7.setOption(option7)
      //6.应收账款年初期末余额对比分析
      var chartDom8 = document.getElementById('chats-8')
      var myChart8 = echarts.init(chartDom8)
      var option8
      option8 = {
        title: {
          text: '6.应收账款年初期末余额对比分析',
          left: 'left',
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'cross',
          },
        },
        grid: {
          left: '1%',
          right: '1%',
          top: '20%',
          bottom: '15%',
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
          data: ['年初余额', '期末余额'],
          y: 'bottom',
        },
        xAxis: [
          {
            type: 'category',

            // prettier-ignore
            data: ['公司12', '公司13', '公司14', '公司15', '公司16', '公司17', '公司18', '公司19', '公司20', '公司21'],
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
            name: '年初余额',
            type: 'bar',
            data: [12.0, 14.9, 27.0, 23.2, 25.6, 30.7, 51.6, 42.2, 32.6, 20.0],
          },
          {
            name: '期末余额',
            type: 'bar',
            data: [14.0, 16.2, 17.0, 33.2, 25.6, 31.7, 51.6, 41.2, 22.6, 10.0],
          },
        ],
      }
      option8 && myChart8.setOption(option8)

      //1.年度应付金额与付款金额对比分析【联动2】
      var chartDom9 = document.getElementById('chats-9')
      var myChart9 = echarts.init(chartDom9)
      var option9
      option9 = {
        title: {
          text: '1.年度应付金额与付款金额对比分析【联动2】',
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
          top: '20%',
          bottom: '15%',
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
          data: ['应付金额', '付款金额'],
          y: 'bottom',
        },
        xAxis: [
          {
            type: 'category',

            // prettier-ignore
            data: ['2012', '2013', '2014', '2015', '2016', '2017', '2018', '2019', '2020', '2021'],
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
            data: [12.0, 14.9, 27.0, 23.2, 25.6, 30.7, 51.6, 42.2, 32.6, 20.0],
          },
          {
            name: '付款金额',
            type: 'bar',
            data: [14.0, 16.2, 17.0, 33.2, 25.6, 31.7, 51.6, 41.2, 22.6, 10.0],
          },
        ],
      }
      option9 && myChart9.setOption(option9)
      //2.各月应付金额与付款金额对比分析
      var chartDom10 = document.getElementById('chats-10')
      var myChart10 = echarts.init(chartDom10)
      var option10
      option10 = {
        title: {
          text: '2.各月应付金额与付款金额对比分析',
          left: 'left',
        },
        grid: {
          left: '3%',
          right: '4%',
          top: '20%',
          bottom: '10%',
          containLabel: true,
        },
        legend: {
          data: ['应付金额', '付款金额'],
          y: 'bottom',
        },
        xAxis: {
          type: 'category',
          // boundaryGap: false,
          data: ['1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11', '12'],
        },
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
            data: [120, 132, 101, 134, 90, 230, 210, 120, 132, 101, 134, 90],
          },
          {
            name: '付款金额',
            type: 'bar',

            data: [220, 182, 191, 234, 290, 330, 310, 220, 182, 191, 234, 290],
          },
        ],
      }
      option10 && myChart10.setOption(option10)
      //3.各公司应付金额与付款金额对比分析
      var chartDom11 = document.getElementById('chats-11')
      var myChart11 = echarts.init(chartDom11)
      var option11
      option11 = {
        title: {
          text: '3.各公司应付金额与付款金额对比分析',
          left: 'left',
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'cross',
          },
        },
        grid: {
          left: '1%',
          right: '1%',
          top: '20%',
          bottom: '15%',
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
          data: ['应付金额', '付款金额'],
          y: 'bottom',
        },
        xAxis: [
          {
            type: 'category',

            // prettier-ignore
            data: ['公司12', '公司13', '公司14', '公司15', '公司16', '公司17', '公司18', '公司19', '公司20', '公司21'],
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
            data: [12.0, 14.9, 27.0, 23.2, 25.6, 30.7, 51.6, 42.2, 32.6, 20.0],
          },
          {
            name: '付款金额',
            type: 'bar',
            data: [14.0, 16.2, 17.0, 33.2, 25.6, 31.7, 51.6, 41.2, 22.6, 10.0],
          },
        ],
      }
      option11 && myChart11.setOption(option11)
      //4.应付账款余额分析
      var chartDom12 = document.getElementById('chats-12')
      var myChart12 = echarts.init(chartDom12)
      var option12
      option12 = {
        title: {
          text: '4.应付账款余额分析',
          left: 'left',
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'cross',
          },
        },
        grid: {
          left: '1%',
          right: '1%',
          top: '20%',
          bottom: '15%',
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
          data: ['应付余额'],
          y: 'bottom',
        },
        xAxis: [
          {
            // prettier-ignore
            data: ['公司12', '公司13', '公司14', '公司15', '公司16', '公司17', '公司18', '公司19', '公司20', '公司21'],
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
            name: '应付余额',
            type: 'bar',
            data: [12.0, 14.9, 27.0, 23.2, 25.6, 30.7, 51.6, 42.2, 32.6, 20.0],
          },
        ],
      }
      option12 && myChart12.setOption(option12)
      //5.应付账款发生额分析
      var chartDom13 = document.getElementById('chats-13')
      var myChart13 = echarts.init(chartDom13)
      var option13
      option13 = {
        title: {
          text: '5.应付账款发生额分析',
          left: 'left',
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'cross',
          },
        },
        grid: {
          left: '1%',
          right: '1%',
          top: '20%',
          bottom: '15%',
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
          data: ['应付账款发生额'],
          y: 'bottom',
        },
        xAxis: [
          {
            // prettier-ignore
            data: ['公司12', '公司13', '公司14', '公司15', '公司16', '公司17', '公司18', '公司19', '公司20', '公司21'],
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
            name: '应付账款发生额',
            type: 'bar',
            data: [12.0, 14.9, 27.0, 23.2, 25.6, 30.7, 51.6, 42.2, 32.6, 20.0],
          },
        ],
      }
      option13 && myChart13.setOption(option13)
      //6.应付账款年初期末余额对比分析
      var chartDom14 = document.getElementById('chats-14')
      var myChart14 = echarts.init(chartDom14)
      var option14
      option14 = {
        title: {
          text: '6.应付账款年初期末余额对比分析',
          left: 'left',
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'cross',
          },
        },
        grid: {
          left: '1%',
          right: '1%',
          top: '20%',
          bottom: '15%',
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
          data: ['年初余额', '期末余额'],
          y: 'bottom',
        },
        xAxis: [
          {
            type: 'category',

            // prettier-ignore
            data: ['公司12', '公司13', '公司14', '公司15', '公司16', '公司17', '公司18', '公司19', '公司20', '公司21'],
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
            name: '年初余额',
            type: 'bar',
            data: [12.0, 14.9, 27.0, 23.2, 25.6, 30.7, 51.6, 42.2, 32.6, 20.0],
          },
          {
            name: '期末余额',
            type: 'bar',
            data: [14.0, 16.2, 17.0, 33.2, 25.6, 31.7, 51.6, 41.2, 22.6, 10.0],
          },
        ],
      }
      option14 && myChart14.setOption(option14)
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
