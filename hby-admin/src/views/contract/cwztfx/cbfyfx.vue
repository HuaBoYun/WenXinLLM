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
        <div class="titleBar">成本费用分析</div>
      </el-col>

      <el-col :lg="16" :md="6" :sm="12" class="border_none">
        <el-row :gutter="10">
          <el-col :lg="8" :md="6" :sm="12">
            <div
              class="one-item flex-col padding-10 margin-bottom-0"
              style="padding: 20px 10px"
            >
              <div class="flex align-baseline margin-bottom-10">
                <div class="price">298.11</div>
                <div class="unit">万元</div>
              </div>
              <div class="title margin-bottom-10">营业成本</div>
            </div>
          </el-col>
          <el-col :lg="8" :md="6" :sm="12">
            <div
              class="one-item flex-col padding-10 margin-bottom-0"
              style="padding: 20px 10px"
            >
              <div class="flex align-baseline margin-bottom-10">
                <div class="price">298.11</div>
                <div class="unit">万元</div>
              </div>
              <div class="title margin-bottom-10">本期金额</div>
            </div>
          </el-col>
          <el-col :lg="8" :md="6" :sm="12">
            <div
              class="one-item flex-col padding-10 margin-bottom-0"
              style="padding: 20px 10px"
            >
              <div class="flex align-baseline margin-bottom-10">
                <div class="price">298.11</div>
                <!-- <div class="unit">万元</div> -->
              </div>
              <div class="title margin-bottom-10">主营业务成本</div>
            </div>
          </el-col>
        </el-row>
        <el-row :gutter="10">
          <el-col :lg="8" :md="6" :sm="12">
            <div
              class="one-item flex-col padding-10 margin-bottom-0"
              style="padding: 20px 10px"
            >
              <div class="flex align-baseline margin-bottom-10">
                <div class="price">298.11</div>
                <div class="unit">万元</div>
              </div>
              <div class="title margin-bottom-10">财务费用</div>
            </div>
          </el-col>
          <el-col :lg="8" :md="6" :sm="12">
            <div
              class="one-item flex-col padding-10 margin-bottom-0"
              style="padding: 20px 10px"
            >
              <div class="flex align-baseline margin-bottom-10">
                <div class="price">298.11</div>
                <div class="unit">万元</div>
              </div>
              <div class="title margin-bottom-10">管理费用</div>
            </div>
          </el-col>
          <el-col :lg="8" :md="6" :sm="12">
            <div
              class="one-item flex-col padding-10 margin-bottom-0"
              style="padding: 20px 10px"
            >
              <div class="flex align-baseline margin-bottom-10">
                <div class="price">298.11</div>
                <!-- <div class="unit">万元</div> -->
              </div>
              <div class="title margin-bottom-10">销售费用</div>
            </div>
          </el-col>
        </el-row>
      </el-col>

      <el-row :gutter="10">
        <el-col :lg="8" :md="16" :sm="24">
          <div id="chats-1" style="width: 100%; height: 262px"></div>
        </el-col>
      </el-row>
      <el-row :gutter="10">
        <el-col :lg="12" :md="8" :sm="24">
          <div id="chats-2" style="width: 100%; height: 300px"></div>
        </el-col>
        <el-col :lg="12" :md="16" :sm="24">
          <div id="chats-3" style="width: 100%; height: 300px"></div>
        </el-col>
      </el-row>

      <el-row :gutter="10">
        <el-col :lg="12" :md="16" :sm="24">
          <div id="chats-4" style="width: 100%; height: 300px"></div>
        </el-col>
        <el-col :lg="12" :md="16" :sm="24">
          <div id="chats-5" style="width: 100%; height: 300px"></div>
        </el-col>
      </el-row>
      <el-row :gutter="10">
        <el-col :lg="12" :md="16" :sm="24">
          <div id="chats-6" style="width: 100%; height: 300px"></div>
        </el-col>
        <el-col :lg="12" :md="16" :sm="24">
          <div id="chats-7" style="width: 100%; height: 300px"></div>
        </el-col>
        <el-col :lg="8" :md="16" :sm="24">
          <div id="chats-8" style="width: 100%; height: 300px"></div>
        </el-col>
        <el-col :lg="8" :md="16" :sm="24">
          <div id="chats-9" style="width: 100%; height: 300px"></div>
        </el-col>
        <el-col :lg="8" :md="16" :sm="24">
          <div id="chats-10" style="width: 100%; height: 300px"></div>
        </el-col>
        <el-col :lg="12" :md="16" :sm="24">
          <div id="chats-11" style="width: 100%; height: 300px"></div>
        </el-col>
        <el-col :lg="12" :md="16" :sm="24">
          <div id="chats-12" style="width: 100%; height: 300px"></div>
        </el-col>
        <el-col :lg="24" :md="16" :sm="24">
          <div id="chats-13" style="width: 100%; height: 300px"></div>
        </el-col>
      </el-row>
    </div>
  </div>
</template>
<script>
  import * as echarts from 'echarts'
  export default {
    name: 'Download',
    components: {},
    mounted() {
      //各公司营业总成本
      var chartDom = document.getElementById('chats-1')
      var myChart = echarts.init(chartDom)
      var option
      option = {
        title: {
          text: '各公司营业总成本【联动】',
          left: 'left',
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow',
          },
        },
        legend: {},
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true,
        },
        xAxis: {
          type: 'value',
          boundaryGap: [0, 0.01],
        },
        yAxis: {
          type: 'category',
          data: ['公司1', '公司2', '公司3', '公司4', '公司5', '公司6'],
        },
        series: [
          {
            name: '本期金额',
            type: 'bar',
            data: [18203, 23489, 29034, 104970, 131744, 630230],
          },
        ],
      }
      option && myChart.setOption(option)
      //营业总成本月度分析
      var chartDom2 = document.getElementById('chats-2')
      var myChart2 = echarts.init(chartDom2)
      var option2
      const colors = ['#5470C6', '#91CC75', '#EE6666']
      option2 = {
        color: colors,
        title: {
          text: '营业总成本月度分析',
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
          right: '3%',
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
          data: ['营业成本', '同比', '环比'],
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
              lineStyle: {
                color: colors[0],
              },
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
              lineStyle: {
                color: colors[2],
              },
            },
            axisLabel: {
              formatter: '{value} ',
            },
          },
        ],
        series: [
          {
            name: '营业成本',
            type: 'bar',
            data: [
              2.0, 4.9, 7.0, 23.2, 25.6, 76.7, 135.6, 162.2, 32.6, 20.0, 6.4,
              3.3,
            ],
          },
          {
            name: '同比',
            type: 'line',
            data: [
              2.0, 4.9, 7.0, 23.2, 25.6, 76.7, 135.6, 162.2, 32.6, 20.0, 6.4,
              3.3,
            ],
          },
          {
            name: '环比',
            type: 'line',
            yAxisIndex: 1,
            data: [
              2.0, 2.2, 3.3, 4.5, 6.3, 10.2, 20.3, 23.4, 23.0, 16.5, 12.0, 6.2,
            ],
          },
        ],
      }

      option2 && myChart2.setOption(option2)

      //主营业务成本月度分析
      var chartDom4 = document.getElementById('chats-4')
      var myChart4 = echarts.init(chartDom4)
      var option4
      option4 = {
        color: colors,
        title: {
          text: '主营业务成本月度分析',
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
          right: '3%',
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
          data: ['本期金额', '同期金额', '同比增长率'],
        },
        xAxis: [
          {
            type: 'category',
            axisTick: {
              alignWithLabel: true,
            },
            // prettier-ignore
            data: ['1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11', '12'],
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
              lineStyle: {
                color: colors[0],
              },
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
              lineStyle: {
                color: colors[1],
              },
            },
            axisLabel: {
              formatter: '{value} ',
            },
          },
        ],
        series: [
          {
            name: '本期金额',
            type: 'bar',
            data: [
              2.0, 4.9, 7.0, 23.2, 25.6, 76.7, 135.6, 162.2, 32.6, 20.0, 6.4,
              3.3,
            ],
          },
          {
            name: '同期金额',
            type: 'bar',
            data: [
              2.0, 4.9, 7.0, 23.2, 25.6, 76.7, 135.6, 162.2, 32.6, 20.0, 6.4,
              3.3,
            ],
          },
          {
            name: '同比增长率',
            type: 'line',
            yAxisIndex: 1,
            data: [
              2.0, 2.2, 3.3, 4.5, 6.3, 10.2, 20.3, 23.4, 23.0, 16.5, 12.0, 6.2,
            ],
          },
        ],
      }

      option4 && myChart4.setOption(option4)
      //其他业务成本月度分析
      var chartDom5 = document.getElementById('chats-5')
      var myChart5 = echarts.init(chartDom5)
      var option5
      option5 = {
        color: colors,
        title: {
          text: '其他业务成本月度分析',
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
          right: '3%',
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
          data: ['营业成本', '同比', '环比'],
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
              lineStyle: {
                color: colors[0],
              },
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
              lineStyle: {
                color: colors[2],
              },
            },
            axisLabel: {
              formatter: '{value} ',
            },
          },
        ],
        series: [
          {
            name: '营业成本',
            type: 'bar',
            data: [
              2.0, 4.9, 7.0, 23.2, 25.6, 76.7, 135.6, 162.2, 32.6, 20.0, 6.4,
              3.3,
            ],
          },
          {
            name: '同比',
            type: 'line',
            yAxisIndex: 1,
            data: [
              2.0, 4.9, 7.0, 23.2, 25.6, 76.7, 135.6, 162.2, 32.6, 20.0, 6.4,
              3.3,
            ],
          },
          {
            name: '环比',
            type: 'line',
            yAxisIndex: 1,
            data: [
              2.0, 2.2, 3.3, 4.5, 6.3, 10.2, 20.3, 23.4, 23.0, 16.5, 12.0, 6.2,
            ],
          },
        ],
      }

      option5 && myChart5.setOption(option5)
      //主营业务成本年累计同比分析
      var chartDom6 = document.getElementById('chats-6')
      var myChart6 = echarts.init(chartDom6)
      var option6
      option6 = {
        color: colors,
        title: {
          text: '主营业务成本年累计同比分析',
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
          right: '3%',
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
          data: ['本年累计金额', '年同期累计金额', '年累计同比'],
        },
        xAxis: [
          {
            type: 'category',
            axisTick: {
              alignWithLabel: true,
            },
            // prettier-ignore
            data: ['1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11', '12'],
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
              lineStyle: {
                color: colors[0],
              },
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
              lineStyle: {
                color: colors[1],
              },
            },
            axisLabel: {
              formatter: '{value} ',
            },
          },
        ],
        series: [
          {
            name: '本年累计金额',
            type: 'bar',
            yAxisIndex: 0,
            data: [
              2.0, 4.9, 7.0, 23.2, 25.6, 76.7, 135.6, 162.2, 32.6, 20.0, 6.4,
              3.3,
            ],
          },
          {
            name: '年同期累计金额',
            type: 'bar',
            yAxisIndex: 0,
            data: [
              2.0, 4.9, 7.0, 23.2, 25.6, 76.7, 135.6, 162.2, 32.6, 20.0, 6.4,
              3.3,
            ],
          },
          {
            name: '年累计同比',
            type: 'line',
            yAxisIndex: 1,
            data: [
              2.0, 2.2, 3.3, 4.5, 6.3, 10.2, 20.3, 23.4, 23.0, 16.5, 12.0, 6.2,
            ],
          },
        ],
      }

      option6 && myChart6.setOption(option6)
      //公司期间费用排名TOP10【联动】
      var chartDom7 = document.getElementById('chats-7')
      var myChart7 = echarts.init(chartDom7)
      var option7
      option7 = {
        title: {
          text: '公司期间费用排名TOP10【联动】',
          left: 'left',
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow',
          },
        },
        legend: {},
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true,
        },
        xAxis: {
          type: 'value',
          boundaryGap: [0, 0.01],
        },
        yAxis: {
          type: 'category',
          data: ['公司1', '公司2', '公司3', '公司4', '公司5', '公司6'],
        },
        series: [
          {
            name: '期间费用',
            type: 'bar',
            data: [18203, 23489, 29034, 104970, 131744, 630230],
          },
        ],
      }
      option7 && myChart7.setOption(option7)
      //销售费用率
      var chartDom8 = document.getElementById('chats-8')
      var myChart8 = echarts.init(chartDom8)
      var option8
      option8 = {
        tooltip: {
          formatter: '{a} <br/>{b} : {c}%',
        },
        title: {
          text: '销售费用率',
          left: 'center',
        },
        series: [
          {
            name: 'Pressure',
            type: 'gauge',
            detail: {
              formatter: '{value}',
            },
            data: [
              {
                value: 50,
                name: 'SCORE',
              },
            ],
          },
        ],
      }

      option8 && myChart8.setOption(option8)
      //管理费用率
      var chartDom9 = document.getElementById('chats-9')
      var myChart9 = echarts.init(chartDom9)
      var option9
      option9 = {
        tooltip: {
          formatter: '{a} <br/>{b} : {c}%',
        },
        title: {
          text: '管理费用率',
          left: 'center',
        },
        series: [
          {
            name: 'Pressure',
            type: 'gauge',
            detail: {
              formatter: '{value}',
            },
            data: [
              {
                value: 50,
                name: 'SCORE',
              },
            ],
          },
        ],
      }

      option9 && myChart9.setOption(option9)
      //财务费用率
      var chartDom10 = document.getElementById('chats-10')
      var myChart10 = echarts.init(chartDom10)
      var option10
      option10 = {
        tooltip: {
          formatter: '{a} <br/>{b} : {c}%',
        },
        title: {
          text: '财务费用率',
          left: 'center',
        },
        series: [
          {
            name: 'Pressure',
            type: 'gauge',
            detail: {
              formatter: '{value}',
            },
            data: [
              {
                value: 50,
                name: 'SCORE',
              },
            ],
          },
        ],
      }

      option10 && myChart10.setOption(option10)
      //期间费用月趋势
      var chartDom11 = document.getElementById('chats-11')
      var myChart11 = echarts.init(chartDom11)
      var option11
      option11 = {
        title: {
          text: '期间费用月趋势',
          left: 'left',
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
          data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'],
        },
        yAxis: {
          type: 'value',
        },
        series: [
          {
            data: [820, 932, 901, 934, 1290, 1330, 1320],
            type: 'line',
            areaStyle: {},
          },
        ],
      }
      option11 && myChart11.setOption(option11)
      //费用月趋势对比
      var chartDom12 = document.getElementById('chats-12')
      var myChart12 = echarts.init(chartDom12)
      var option12
      option12 = {
        title: {
          text: '费用月趋势对比',
          left: 'left',
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true,
        },
        legend: {
          data: ['销售费用', '管理费用', '财务费用'],
        },
        xAxis: {
          type: 'category',
          boundaryGap: false,
          data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'],
        },
        yAxis: {
          type: 'value',
        },
        series: [
          {
            name: '销售费用',
            type: 'line',
            stack: 'Total',
            areaStyle: {},
            emphasis: {
              focus: 'series',
            },
            data: [120, 132, 101, 134, 90, 230, 210],
          },
          {
            name: '管理费用',
            type: 'line',
            stack: 'Total',
            areaStyle: {},
            emphasis: {
              focus: 'series',
            },
            data: [220, 182, 191, 234, 290, 330, 310],
          },
          {
            name: '财务费用',
            type: 'line',
            stack: 'Total',
            areaStyle: {},
            emphasis: {
              focus: 'series',
            },
            data: [150, 232, 201, 154, 190, 330, 410],
          },
        ],
      }
      option12 && myChart12.setOption(option12)
      //成本费用情况对比
      var chartDom13 = document.getElementById('chats-13')
      var myChart13 = echarts.init(chartDom13)
      var option13

      option13 = {
        title: {
          text: '成本费用情况对比',
          left: 'left',
        },
        legend: {},
        tooltip: {},
        data: [
          '主营业务成本',
          '其他业务成本',
          '营业外支出',
          '销售费用',
          '管理费用',
          '财务费用',
        ],
        xAxis: {
          type: 'category',
          data: ['1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11', '12'],
        },
        yAxis: { name: '单位：万元' },
        series: [
          {
            name: '主营业务成本',
            type: 'bar',
            data: [120, 132, 101, 134, 90, 230, 210, 120, 132, 101, 134, 90],
          },
          {
            name: '其他业务成本',
            type: 'bar',
            data: [123, 122, 111, 254, 210, 120, 110, 123, 122, 111, 254, 210],
          },
          {
            name: '营业外支出',
            type: 'bar',
            data: [152, 212, 241, 164, 123, 234, 152, 152, 212, 241, 164, 123],
          },
          {
            name: '销售费用',
            type: 'bar',
            data: [123, 231, 345, 123, 111, 222, 444, 123, 231, 345, 123, 111],
          },
          {
            name: '管理费用',
            type: 'bar',
            data: [222, 333, 111, 333, 222, 612, 212, 222, 333, 111, 333, 222],
          },
          {
            name: '财务费用',
            type: 'bar',
            data: [150, 232, 201, 154, 190, 330, 410, 150, 232, 201, 154, 190],
          },
        ],
      }

      option13 && myChart13.setOption(option13)
    },
    created() {},
    methods: {},
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
</style>
