<template>
  <div>
    <div style="padding: 10px 30px">
      <el-form ref="form" :inline="true" label-width="0">
        <el-form-item>
          <el-date-picker
            v-model="year"
            type="year"
            placeholder="选择年"
            value-format="yyyy"
          ></el-date-picker>
        </el-form-item>
        <el-form-item>
          <el-button icon="el-icon-search" type="primary" @click="getList">
            查询
          </el-button>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>
    </div>
    <el-row :gutter="10">
      <el-col :lg="12" :md="8" :sm="24">
        <div id="chats-1" style="width: 100%; height: 400px"></div>
      </el-col>
      <el-col :lg="12" :md="8" :sm="24">
        <div id="chats-2" style="width: 100%; height: 400px"></div>
      </el-col>
      <el-col :lg="12" :md="8" :sm="24">
        <div id="chats-3" style="width: 100%; height: 400px"></div>
      </el-col>
      <el-col :lg="12" :md="8" :sm="24">
        <div id="chats-4" style="width: 100%; height: 400px"></div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
  import * as echarts from 'echarts'
  import { getJFData } from '@/api/fwgl/home'
  export default {
    data() {
      return {
        jfslList: [],
        year: '',
      }
    },
    mounted() {
      this.initChart2()
      this.initChart3()
      this.initChart4()
      this.getList()
    },
    created() {
      const date = new Date()
      this.year = date.getFullYear().toString()
    },
    methods: {
      /**
       * @description: 重置并请求
       * @return {*}
       */      
      resetSearch() {
        this.year = ''
        this.getList()
      },
      getList() {
        getJFData({ year: this.year }).then((res) => {
          //图1
          let Chart1name = []
          let Chart1data1 = []
          let Chart1data2 = []
          for (let i = 1; i < res.jfslList.length; i++) {
            Chart1name.push(res.jfslList[i][0])
            Chart1data1.push(res.jfslList[i][1])
            Chart1data2.push(res.jfslList[i][2])
          }
          this.initChart1(Chart1name, Chart1data1, Chart1data2)

          //图2
          this.initChart2(res.moneyX, res.moneyY)
          //图3
          let Chart3name = []
          let Chart3data1 = []
          let Chart3data2 = []
          let Chart3data3 = []
          for (let i = 1; i < res.SlztList.length; i++) {
            Chart3name.push(res.SlztList[i][0])
            Chart3data1.push(res.SlztList[i][1])
            Chart3data2.push(res.SlztList[i][2])
            Chart3data3.push(res.SlztList[i][3])
          }
          this.initChart3(Chart3name, Chart3data1, Chart3data2, Chart3data3)

          //图4
          let Chart4name = []
          let Chart4data1 = []
          let Chart4data2 = []
          let Chart4data3 = []
          for (let i = 1; i < res.SsTypeList.length; i++) {
            Chart4name.push(res.SsTypeList[i][0])
            Chart4data1.push(res.SsTypeList[i][1])
            Chart4data2.push(res.SsTypeList[i][2])
            Chart4data3.push(res.SsTypeList[i][3])
          }
          this.initChart4(Chart4name, Chart4data1, Chart4data2, Chart4data3)
        })
      },
      initChart1(name, data1, data2) {
        var chartDom = document.getElementById('chats-1')
        var myChart = echarts.init(chartDom)
        var option

        option = {
          title: {
            text: '当年新增纠纷+存量未结',
            left: 'center',
          },
          tooltip: {
            trigger: 'axis',
            axisPointer: {
              type: 'shadow',
            },
          },
          legend: {
            bottom: 0,
          },
          grid: {
            left: '3%',
            right: '4%',
            bottom: '6%',
            containLabel: true,
          },
          yAxis: {
            type: 'value',
            boundaryGap: [0, 0.01],
          },
          xAxis: {
            type: 'category',
            data: name,
            axisLabel: { interval: 0, rotate: 70 },
          },
          series: [
            {
              name: '当年新增',
              type: 'bar',
              data: data1,
            },
            {
              name: '存量未结',
              type: 'bar',
              color: '#ed7e31',
              data: data2,
            },
          ],
        }

        option && myChart.setOption(option)
      },
      initChart2(a, b) {
        var chartDom = document.getElementById('chats-2')
        var myChart = echarts.init(chartDom)
        var option

        option = {
          title: {
            text: '涉诉金额',
            left: 'center',
          },
          xAxis: {
            type: 'category',
            data: a,
            axisLabel: { interval: 0, rotate: 70 },
          },
          yAxis: {
            type: 'value',
          },
          grid: {
            left: '3%',
            right: '4%',
            bottom: '6%',
            containLabel: true,
          },
          legend: {
            bottom: 0,
          },
          series: [
            {
              name: '涉诉金额',
              data: b,
              type: 'bar',
              showBackground: true,
              backgroundStyle: {
                color: 'rgba(180, 180, 180, 0.2)',
              },
            },
          ],
        }

        option && myChart.setOption(option)
      },
      initChart3(a, b, c, d) {
        var chartDom = document.getElementById('chats-3')
        var myChart = echarts.init(chartDom)
        var option

        option = {
          title: {
            text: '一审+二审+再审',
            left: 'center',
          },
          tooltip: {
            trigger: 'axis',
            axisPointer: {
              type: 'shadow',
            },
          },
          legend: {
            bottom: 0,
          },
          grid: {
            left: '3%',
            right: '4%',
            bottom: '6%',
            containLabel: true,
          },
          yAxis: {
            type: 'value',
            boundaryGap: [0, 0.01],
          },
          xAxis: {
            type: 'category',
            data: a,
            axisLabel: { interval: 0, rotate: 70 },
          },
          series: [
            {
              name: '一审',
              type: 'bar',
              data: b,
            },
            {
              name: '二审',
              type: 'bar',
              color: '#ed7e31',
              data: c,
            },
            {
              name: '再审',
              type: 'bar',
              color: '#a5a5a5',
              data: d,
            },
          ],
        }

        option && myChart.setOption(option)
      },
      initChart4(a, b, c, d) {
        var chartDom = document.getElementById('chats-4')
        var myChart = echarts.init(chartDom)
        var option

        option = {
          title: {
            text: '原告被告数量',
            left: 'center',
          },
          tooltip: {
            trigger: 'axis',
            axisPointer: {
              type: 'shadow',
            },
          },
          legend: {
            bottom: 0,
          },
          grid: {
            left: '3%',
            right: '4%',
            bottom: '6%',
            containLabel: true,
          },
          yAxis: {
            type: 'value',
            boundaryGap: [0, 0.01],
          },
          xAxis: {
            type: 'category',
            data: a,
            axisLabel: { interval: 0, rotate: 70 },
          },
          series: [
            {
              name: '原告',
              type: 'bar',
              data: b,
            },
            {
              name: '被告',
              type: 'bar',
              color: '#ed7e31',
              data: c,
            },
            {
              name: '第三方',
              type: 'bar',
              color: '#ed7e31',
              data: d,
            },
          ],
        }

        option && myChart.setOption(option)
      },
    },
  }
</script>

<style></style>
