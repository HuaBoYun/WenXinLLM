<template>
  <div class="system-log-container">
    <div class="content">
      <el-row :gutter="10">
        <el-col :span="8">
          <el-card>
            <div id="chats-6" style="width: 100%; height: 220px"></div>
          </el-card>
        </el-col>
        <el-col :span="8">
          <el-card>
            <div id="chats-4" style="width: 100%; height: 220px"></div>
          </el-card>
        </el-col>
        <el-col :span="8">
          <el-card>
            <div id="chats-5" style="width: 100%; height: 220px"></div>
          </el-card>
        </el-col>
      </el-row>

      <el-row :gutter="10">
        <el-col :lg="16" :md="16" :sm="16">
          <el-row>
            <el-col :span="24">
              <el-card>
                <div id="chats-9" style="width: 100%; height: 300px"></div>
              </el-card>
            </el-col>
            <el-col :span="24">
              <el-card>
                <div id="chats-8" style="width: 100%; height: 300px"></div>
              </el-card>
            </el-col>
          </el-row>
        </el-col>
        <el-col :lg="8" :md="8" :sm="8">
          <el-card>
            <div id="chats-7" style="width: 100%; height: 660px"></div>
          </el-card>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
  import * as echarts from 'echarts'
  import './utils/china'
  const img1 = require('@/assets/indexPage/report/sjzl/01.png')
  const img2 = require('@/assets/indexPage/report/sjzl/02.png')
  const img3 = require('@/assets/indexPage/report/sjzl/03.png')
  export default {
    name: 'Download',
    components: {},
    data() {
      return {
        img1: img1,
        img2: img2,
        img3: img3,
      }
    },
    mounted() {
      this.setCharts4()
      this.setCharts5()
      this.setCharts6()
      this.setCharts7()
      this.setCharts8()
      this.setCharts9()
    },
    created() {},
    methods: {
      setCharts4() {
        const charts = echarts.init(document.getElementById('chats-4'))
        const option = {
          title: {
            text: '区域构成分析',
            left: 'left',
          },
          tooltip: {
            trigger: 'item',
            formatter: '{b} : {c} ({d}%)',
          },
          legend: {
            bottom: 0,
            left: 'center',
            data: ['西北', '东北', '华北', '华中', '华南'],
          },
          color: ['#2dc7c9', '#1d84c6', '#65a4d4', '#89b9df', '#bfd8ef'],
          series: [
            {
              type: 'pie',
              radius: '65%',
              center: ['50%', '50%'],
              selectedMode: 'single',
              data: [
                { value: 900, name: '西北' },
                { value: 735, name: '东北' },
                { value: 510, name: '华北' },
                { value: 434, name: '华中' },
                { value: 335, name: '华南' },
              ],
              emphasis: {
                itemStyle: {
                  shadowBlur: 10,
                  shadowOffsetX: 0,
                  shadowColor: 'rgba(0, 0, 0, 0.5)',
                },
              },
            },
          ],
        }
        charts.setOption(option)
      },
      setCharts5() {
        const charts = echarts.init(document.getElementById('chats-5'))
        const option = {
          title: {
            text: '产品类别分析',
            left: 'left',
          },
          tooltip: {
            trigger: 'item',
            formatter: '{b} : {c} ({d}%)',
          },
          legend: {
            bottom: 0,
            left: 'center',
            data: ['通信设备', '控件产品', '硬件设备', '云产品', '软件产品'],
          },
          color: ['#2dc7c9', '#1d84c6', '#65a4d4', '#89b9df', '#bfd8ef'],
          series: [
            {
              type: 'pie',
              center: ['50%', '50%'],
              radius: ['30%', '60%'],
              selectedMode: 'single',
              data: [
                { value: 900, name: '通信设备' },
                { value: 735, name: '控件产品' },
                { value: 510, name: '硬件设备' },
                { value: 434, name: '云产品' },
                { value: 335, name: '软件产品' },
              ],
              emphasis: {
                itemStyle: {
                  shadowBlur: 10,
                  shadowOffsetX: 0,
                  shadowColor: 'rgba(0, 0, 0, 0.5)',
                },
              },
            },
          ],
        }
        charts.setOption(option)
      },
      setCharts6() {
        const charts = echarts.init(document.getElementById('chats-6'))
        const option = {
          title: {
            text: '业绩汇总分析',
            subtext: '单位：万元',
            left: 'left',
          },
          legend: {
            bottom: 0,
            left: 'center',
            data: ['销售金额', '销售目标'],
          },
          tooltip: {},
          color: ['#1d84c6', '#2dc7c9'],
          xAxis: { type: 'category' },
          yAxis: {},

          series: [
            {
              name: 'bar1',
              type: 'bar',
              data: [24000, 17000, 15600, 36600, 39600],
            },
            { name: 'bar2', type: 'bar', data: [8000, 9000, 4000, 6000, 3000] },
          ],
        }
        charts.setOption(option)
      },
      setCharts7() {
        const chats7 = echarts.init(document.getElementById('chats-7'))
        // 指定图表的配置项和数据
        const option7 = {
          tooltip: {
            trigger: 'item',
            formatter: '{b}<br/>合同数量：{c}<br/>合同金额：9173.25万元',
          },
          dataRange: {
            show: true,
            min: 0,
            max: 1000,
            text: ['High', 'Low'],
            realtime: true,
            calculable: true,
            color: ['#bfd8ef', '#2f84c6'],
          },
          toolbox: {
            show: true,
            orient: 'vertical',
            left: 'right',
            top: 'center',
            feature: {
              dataView: { readOnly: false },
              restore: {},
              saveAsImage: {},
            },
          },
          geo: {
            map: name ? name : 'china', // 核心
            // roam: true,
            label: {
              // 页面初始化加载的文字
              normal: {
                show: true,
                textStyle: {
                  color: '#000', // 页面初始化的地图文字颜色
                  fontSize: 15, // // 页面初始化的地图文字大小
                },
              },
            },
            itemStyle: {
              //设置样式
              normal: {
                borderWidth: 0.5, //区域边框宽度
                borderColor: 'rgba(119, 156, 255, 1)',
                areaColor: {
                  x: 0,
                  y: 0,
                  x2: 0,
                  y2: 1,
                  colorStops: [
                    {
                      offset: 0,
                      color: '#bfd8ef', // 0% 处的颜色
                    },
                    {
                      offset: 1,
                      color: '#bfd8ef', // 100% 处的颜色
                    },
                  ],
                },
              },
              emphasis: {
                // 移入背景颜色
                areaColor: '#34c9aa',
                show: true,
                textStyle: {
                  color: '#000',
                  fontSize: '12px',
                },
              },
            },
          },
          series: [
            {
              type: 'map',
              geoIndex: 0,
              data: this.chinaList,
              data: [
                {
                  name: '青海',
                  value: 599,
                },
                {
                  name: '四川',
                  value: 142,
                },
                {
                  name: '湖北',
                  value: 599,
                },
                {
                  name: '云南',
                  value: 120,
                },
                {
                  name: '吉林',
                  value: 120,
                },
                {
                  name: '山西',
                  value: 120,
                },
                {
                  name: '河南',
                  value: 120,
                },
                {
                  name: '湖南',
                  value: 120,
                },
                {
                  name: '广东',
                  value: 810,
                },
                {
                  name: '广西',
                  value: 453,
                },
                {
                  name: '浙江',
                  value: 453,
                },
                {
                  name: '江苏',
                  value: 453,
                },
              ],
              itemStyle: {
                //地图区域的多边形 图形样式
                normal: {
                  //是图形在默认状态下的样式
                  label: {
                    show: true, //是否显示标签
                    textStyle: {
                      color: 'black',
                    },
                  },
                },
                zoom: 1, //地图缩放比例,默认为1
                emphasis: {
                  //是图形在高亮状态下的样式,比如在鼠标悬浮或者图例联动高亮时
                  label: { show: true },
                },
              },
              label: {
                normal: {
                  show: true, //显示省份标签
                  textStyle: { color: '#696969' }, //省份标签字体颜色
                },
                emphasis: {
                  show: true,
                  textStyle: {},
                },
              },
            },
            {
              type: 'scatter',
              coordinateSystem: 'geo',
              // rippleEffect: {
              //     brushType: 'fill',
              //     scale: 0
              // },
              // showEffectOn: 'render',
              // label: {
              //     lineHeight: 30,
              //     normal: {
              //         show: true,
              //         color: '#081727',
              //         position: 'inside',
              //         padding: [5, 0, 0, 0],
              //         verticalAlign: 'middle',
              //         formatter: function (para) {
              //             return '{cnNum|' + para.data.value[2] + '}'
              //             // return para.data.value[2]
              //         },
              //         rich: {
              //             cnNum: {
              //                 fontSize: 16,
              //                 color: '#081727',
              //                 lineHeight: 28,
              //             }
              //         }
              //     },
              // },
              // symbol: 'roundRect',
              // // symbolRotate: 20,
              // symbolSize: [40, 28],
              // data: [],
              // zlevel: 1,
            },
          ],
        }
        chats7.setOption(option7)
      },
      setCharts8() {
        const chats8 = echarts.init(document.getElementById('chats-8'))
        const option8 = {
          title: {
            text: '销售与金额分析',
            left: 'left',
          },
          xAxis: {},
          yAxis: {},
          tooltip: {
            formatter: function (val, index) {
              return `合同数量：${val.data[0]} <br/>销售金额：${val.data[1]}`
            },
          },
          series: [
            {
              name: 'scatter1',
              symbolSize: 40,
              data: [
                [48, 2196800],
                [86, 8792600],
                [112, 18622400],
                [169, 18799600],
                [246, 19723300],
                [295, 21928100],
                [304, 20965800],
                [397, 22639500],
                [476, 23380400],
              ],
              type: 'scatter',
            },
          ],
        }
        chats8.setOption(option8)
      },
      setCharts9() {
        const chats9 = echarts.init(document.getElementById('chats-9'))
        const option9 = {
          title: {
            text: '月度销售增长',
            left: 'left',
          },
          xAxis: {
            type: 'category',
            boundaryGap: false,
            data: [
              '1月',
              '2月',
              '3月',
              '4月',
              '5月',
              '6月',
              '7月',
              '8月',
              '9月',
              '10月',
              '11月',
              '12月',
            ],
          },
          yAxis: {
            type: 'value',
          },
          series: [
            {
              data: [
                100, 300, 200, 200, 500, 900, 1320, 932, 901, 600, 400, 200,
              ],
              type: 'line',
              areaStyle: {},
            },
          ],
        }
        chats9.setOption(option9)
      },
    },
  }
</script>
<style scoped>
  .system-log-container {
    padding: 0 !important;
  }
  /* .el-col > div {
    border: 1px solid #dcdfe5;
    margin-bottom: 10px;
  } */
  .content {
    padding: 20px;
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

  .padding-20 {
    padding: 20px;
  }

  .padding-10 {
    padding: 10px;
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
  .chats > div > div {
    background: white;
    padding: 10px;
    margin-bottom: 20px;
  }
</style>
