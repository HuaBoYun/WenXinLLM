<template>
  <div class="back">
    <el-row>
      <el-col :span="4">
        <div class="leftView">
          <div class="viewCard back1">
            <div class="cardText">总资产（亿）</div>
            <div class="cardCount">476.5</div>
          </div>
          <div class="viewCard back2">
            <div class="cardText">总负债（亿）</div>
            <div class="cardCount">310.45</div>
          </div>
          <div class="viewCard back3">
            <div class="cardText">货币资金（亿）</div>
            <div class="cardCount">79.21</div>
          </div>
          <div class="viewCard back4">
            <div class="cardText">归母净资产（亿）</div>
            <div class="cardCount">78.93</div>
          </div>
          <div class="viewCard back5">
            <div class="cardText">存货（亿）</div>
            <div class="cardCount">33.25</div>
          </div>
          <div class="viewCard back6">
            <div class="cardText">利润营业收入（亿）</div>
            <div class="cardCount">2.16</div>
          </div>
          <div class="viewCard2 back7">
            <div class="cardText">当月现金流入（亿）</div>
            <div class="cardCount">0.6</div>
            <div class="cardCount2">现金流出 1.97 净流量 -1.37</div>
          </div>
        </div>
      </el-col>
      <el-col :span="20" style="margin-top: 20px; padding-left: 10px">
        <el-row :gutter="10">
          <el-col :span="12">
            <div class="myCard">
              <el-card>
                <div slot="header">
                  <div class="bodyTitle">资产负债分布</div>
                </div>
                <div id="chats-7" style="width: 100%; height: 700px"></div>
              </el-card>
            </div>
          </el-col>
          <el-col :span="12">
            <el-card>
              <div slot="header">
                <div class="bodyTitle">资产负债分布</div>
              </div>
              <Pie5 />
            </el-card>
            <div class="myCard">
            <el-card>
                <div slot="header">
                  <div class="bodyTitle">归母净利润-年度预算对标（亿）</div>
                </div>
                <Line7 />
              </el-card>
              </div>
          </el-col>
        </el-row>
      </el-col>
    </el-row>
  </div>
</template>
<script>
import './utils/china'
import * as echarts from 'echarts'
import Pie5 from './components/Pie5'
import Line7 from './components/Line7'
export default {
  components: {
    Pie5,
    Line7
  },
  data() {
    return {}
  },
  methods: {},
  mounted() {
    const chats7 = echarts.init(document.getElementById('chats-7'))
      // 指定图表的配置项和数据
      const option7 = {
        // title: {
        //   text: '全国客户分布',
        //   left: 'left',
        // },
        tooltip: {
          trigger: 'item',
          formatter: '{b}<br/>客户数量：{c}<br/>合同金额：9173.25万元',
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
          // orient: 'vertical',
          left: 'right',
          top: 'top',
          feature: {
            // dataView: { readOnly: false },
            saveAsImage: {},
            restore: {},
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
            // data: this.chinaList,
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
  
  }
}
</script>

<style scoped lang="scss">
.back {
  background: #f7f7f7;
}
.leftView {
  margin-left: 35px;
  margin-top: 20px;

  .viewCard {
    height: 98px;
    background-repeat: no-repeat;
    background-size: cover;
    background-position: left center;
    padding-left: 20px;
    border-radius: 4px;

    .cardText {
      padding-top: 20px;
      color: #333;
      font-size: 14px;
      text-align: left;
      line-height: 1.6;
      color: #fff;
    }

    .cardCount {
      padding-top: 3px;
      color: #fff;
      font-size: 24px;
      text-align: left;
      line-height: 1.6;
    }
  }

  .viewCard2 {
    height: 118px;
    background-repeat: no-repeat;
    background-size: cover;
    background-position: left center;
    padding-left: 20px;
    border-radius: 4px;

    .cardText {
      padding-top: 20px;
      font-size: 14px;
      text-align: left;
      line-height: 1.6;
      color: #fff;
    }

    .cardCount {
      padding-top: 3px;
      color: #fff;
      font-size: 24px;
      text-align: left;
      line-height: 1.6;
    }

    .cardCount2 {
      font-size: 14px;
      text-align: left;
      line-height: 1.6;
      color: #fff;
    }
  }

  .back1 {
    background-color: #92b6d6;
    background-image: url('../../../assets/personal/card1.png') no-repeat;
  }

  .back2 {
    background-color: #2f84c6;
    background-image: url('../../../assets/personal/card1.png') no-repeat;
  }
  .back3 {
    background-color: #65a4d4;
    background-image: url('../../../assets/personal/card1.png') no-repeat;
  }

  .back4 {
    background-color: #2abcbb;
    background-image: url('../../../assets/personal/card1.png') no-repeat;
  }

  .back5 {
    background-color: #92b6d6;
    background-image: url('../../../assets/personal/card1.png') no-repeat;
  }
  .back6 {
    background-color: #2abcbb;
    background-image: url('../../../assets/personal/card1.png') no-repeat;
  }
  .back7 {
    background-color: #2f84c6;
    background-image: url('../../../assets/personal/card1.png') no-repeat;
  }
}
.bodyTitle {
  color: #4285f4;
  font-size: 16px;
}

.myCard ::v-deep .el-card {
  .el-card__header {
    border-bottom: 1px solid #4285f4 !important;
  }
}
</style>
