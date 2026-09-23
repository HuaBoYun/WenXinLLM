<template>
  <div class="system-log-container">
    <div class="content">
      <el-row :gutter="10">
        <el-col :span="8">
          <el-card>
            <div class="card-content">
              <div class="left">
                <img :src="img1" alt="" />
              </div>
              <div class="right">
                <div class="top">
                  368,800
                  <span class="unit">万元</span>
                </div>
                <div class="bottom">合同金额</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="8">
          <el-card>
            <div class="card-content">
              <div class="left">
                <img :src="img2" alt="" />
              </div>
              <div class="right">
                <div class="top">
                  9809
                  <span class="unit">份</span>
                </div>
                <div class="bottom">本年度合同数量</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="8">
          <el-card>
            <div class="card-content">
              <div class="left">
                <img :src="img3" alt="" />
              </div>
              <div class="right">
                <div class="top">
                  6,828
                  <span class="unit"></span>
                </div>
                <div class="bottom">客户数量</div>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>

      <el-row :gutter="10">
        <el-col :lg="8" :md="8" :sm="8">
          <el-row>
            <el-col :span="24">
              <el-card>
                <div id="chats-6" style="width: 100%; height: 300px"></div>
              </el-card>
            </el-col>
            <el-col :span="24">
              <el-card>
                <div id="chats-4" style="width: 100%; height: 300px"></div>
              </el-card>
            </el-col>
          </el-row>
        </el-col>
        <el-col :lg="16" :md="16" :sm="16">
          <el-card>
            <div id="chats-7" style="width: 100%; height: 660px"></div>
          </el-card>
        </el-col>
      </el-row>

      <el-row :gutter="10">
        <el-col :lg="24" :md="24" :sm="24">
          <el-card>
            <div id="chats-3" style="width: 100%; height: 300px"></div>
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
      var chartDom3 = document.getElementById('chats-3')
      var myChart3 = echarts.init(chartDom3)
      var option3

      option3 = {
        title: {
          text: '年利润对比分析【联动】',
          left: 'left',
        },
        legend: {},
        tooltip: {},
        dataset: {
          source: [
            ['一月', 24000, 8000],
            ['二月', 17000, 9000],
            ['三月', 15600, 4000],
            ['四月', 36600, 6000],
            ['五月', 39600, 3000],
            ['六月', 32600, 4000],
            ['七月', 19600, 4000],
            ['八月', 23600, 4000],
            ['九月', 17600, 4000],
            ['十月', 17600, 4000],
            ['十一月', 29600, 4000],
            ['十二月', 40600, 4000],
          ],
        },
        xAxis: { type: 'category' },
        yAxis: {},
        series: [{ type: 'bar' }, { type: 'bar' }],
      }

      option3 && myChart3.setOption(option3)

      var chartDom4 = document.getElementById('chats-4')
      var myChart4 = echarts.init(chartDom4)
      var option4

      option4 = {
        title: {
          text: '财务账款统计（万元）',
          left: 'left',
        },
        tooltip: {
          trigger: 'item',
          formatter: '{b} : {c} ({d}%)',
        },
        legend: {
          bottom: 10,
          left: 'center',
          data: ['正常', '完成', '暂停', '解除', '终止'],
        },
        series: [
          {
            type: 'pie',
            radius: '65%',
            center: ['50%', '50%'],
            selectedMode: 'single',
            data: [
              { value: 900, name: '正常' },
              { value: 735, name: '完成' },
              { value: 510, name: '暂停' },
              { value: 434, name: '解除' },
              { value: 335, name: '终止' },
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

      option4 && myChart4.setOption(option4)

      const chats6 = echarts.init(document.getElementById('chats-6'))
      // 指定图表的配置项和数据
      chats6.setOption({
        title: {
          text: '财务账款统计（万元）',
          subtext: '单位：万元',
          left: 'left',
        },
        legend: {
          left: 'center',
        },
        tooltip: {},
        dataset: {
          source: [
            ['一月', 24000, -8000],
            ['二月', 17000, -9000],
            ['三月', 15600, -4000],
            ['四月', 36600, -6000],
            ['五月', 39600, -3000],
            ['六月', 32600, -4000],
            ['七月', 19600, -4000],
            ['八月', 23600, -4000],
            ['九月', 17600, -4000],
            ['十月', 17600, -4000],
            ['十一月', 29600, -4000],
            ['十二月', 40600, -4000],
          ],
        },
        xAxis: { type: 'category' },
        yAxis: {},

        series: [{ type: 'bar' }],
      })

      const chats7 = echarts.init(document.getElementById('chats-7'))
      // 指定图表的配置项和数据
      const option7 = {
        title: {
          text: '全国客户分布',
          left: 'left',
        },
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
    created() {},
    methods: {},
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

  .card-content {
    display: flex;
    align-items: center;
  }

  .card-content .left {
    margin-right: 10px;
    width: 52px;
  }

  .card-content .left img {
    width: 100%;
  }

  .card-content .right .top {
    font-size: 28px;
    color: #000;
    margin-bottom: 6px;
  }

  .card-content .right .bottom {
    font-size: 16px;
    color: #808080;
  }

  .card-content .right .unit {
    font-size: 14px;
    color: #808080;
  }
</style>
