<template>
  <div>
    <el-row :gutter="20" class="topcard">
      <el-col :span="6">
        <el-card class="item">
          <div class="title">
            <h5>销售合同</h5>
            <el-tag size="mini" class="right">月度</el-tag>
          </div>
          <div class="content">
            <h1>40 886,200</h1>
            <div class="footer">
              <div class="small">金额</div>
              <div class="right">
                <div>98%</div>
                <i></i>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="item">
          <div class="title">
            <h5>采购合同</h5>
            <el-tag size="mini" class="right">月度</el-tag>
          </div>
          <div class="content">
            <h1>275,800</h1>
            <div class="footer">
              <div class="small">金额</div>
              <div class="right">
                <div>98%</div>
                <i></i>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="item">
          <div class="title">
            <h5>合同收款</h5>
            <el-tag size="mini" class="right">月度</el-tag>
          </div>
          <div class="content">
            <h1>106,120</h1>
            <div class="footer">
              <div class="small">金额</div>
              <div class="right">
                <div>98%</div>
                <i></i>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="item">
          <div class="title">
            <h5>合同付款</h5>
            <el-tag size="mini" class="right">月度</el-tag>
          </div>
          <div class="content">
            <h1>80,600</h1>
            <div class="footer">
              <div class="small">金额</div>
              <div class="right">
                <div>98%</div>
                <i></i>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="10">
      <el-col :span="18">
        <el-card>
          <div id="chats-1" style="width: 100%; height: 300px"></div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card>
          <div class="count-1" style="width: 100%; height: 300px">
            <div class="count-item" v-for="item in 3">
              <div class="count-num">
                {{ item === 1 ? '2,346' : item === 2 ? '4,422' : '9,180' }}
              </div>
              <div class="count-tip">
                <span>
                  {{ item === 1 ? '已开票' : item === 2 ? '未开票' : '已收款' }}
                </span>
                <span>
                  {{ item === 1 ? '48%' : item === 2 ? '60%' : '22%' }}
                </span>
              </div>
              <el-row class="count-progress">
                <el-col
                  :span="item === 1 ? 12 : item === 2 ? 13 : 6"
                  class="actived"
                ></el-col>
                <el-col
                  :span="item === 1 ? 12 : item === 2 ? 11 : 18"
                  class="unactived"
                ></el-col>
              </el-row>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="10">
      <el-col :span="8" v-for="(item, index) in allDatas" :key="item.title">
        <el-card>
          <h3 class="title">{{ item.title }}</h3>
          <el-table :data="item.tableData" :show-header="item.showHeader">
            <el-table-column
              type="selection"
              v-if="item.multi"
            ></el-table-column>

            <template v-if="index === 0">
              <el-table-column prop="name" label="项目名称"></el-table-column>
              <el-table-column prop="money" label="合同金额"></el-table-column>
              <el-table-column
                prop="belong"
                label="所属项目组"
              ></el-table-column>
              <el-table-column prop="rate" label="同比">
                <template #default="{ row }">
                  <div class="green">{{ row.rate }}</div>
                </template>
              </el-table-column>
            </template>

            <template v-if="index === 1">
              <el-table-column prop="name">
                <template #default="{ row }">
                  <span>{{ row.name }}</span>
                  <span>
                    <el-button size="mini" type="primary">
                      {{ row.status }}
                    </el-button>
                  </span>
                </template>
              </el-table-column>
            </template>

            <template v-if="index === 2">
              <el-table-column prop="name">
                <template #default="{ row }">
                  <div>{{ row.name }}</div>
                  <el-row :gutter="10">
                    <el-col :span="8">合同金额</el-col>
                    <el-col :span="8">合同覆约率%</el-col>
                    <el-col :span="8">已完成覆约</el-col>
                  </el-row>
                  <el-row :gutter="10">
                    <el-col :span="8">{{ row.field1 }}</el-col>
                    <el-col :span="8">{{ row.field2 }}</el-col>
                    <el-col :span="8">{{ row.field3 }}</el-col>
                  </el-row>
                </template>
              </el-table-column>
            </template>
          </el-table>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="10">
      <el-col :span="24"><h1>大区排名</h1></el-col>
      <el-col :span="6">
        <el-card>
          <div style="width: 100%; height: 300px">
            <h4>销售总额排名</h4>
            <el-table :data="rankTableData" :show-header="false">
              <el-table-column :width="40">
                <template #default="{ $index }">
                  <div :class="'box-' + ($index + 1)">
                    {{ $index + 1 }}
                  </div>
                </template>
              </el-table-column>
              <el-table-column prop="name"></el-table-column>
              <el-table-column prop="money" :width="90"></el-table-column>
            </el-table>
          </div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <div id="chats-2" style="width: 100%; height: 300px"></div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card>
          <h4>各大区占比</h4>
          <el-row>
            <el-col :span="12">
              <div id="chats-3" style="width: 100%; height: 240px"></div>
              <div style="width: 100%; text-align: center">销售</div>
            </el-col>
            <el-col :span="12">
              <div id="chats-4" style="width: 100%; height: 240px"></div>
              <div style="width: 100%; text-align: center">采购</div>
            </el-col>
          </el-row>
        </el-card>
      </el-col>
    </el-row>

    <el-row>
      <el-card>
        <h4>我起草的</h4>
        <el-table :data="drawTableData">
          <el-table-column type="index" label="序号"></el-table-column>
          <el-table-column prop="field1" label="合同标题"></el-table-column>
          <el-table-column prop="field2" label="合同编号"></el-table-column>
          <el-table-column prop="field3" label="我方签约主体"></el-table-column>
          <el-table-column prop="field4" label="合同生效日期"></el-table-column>
          <el-table-column prop="field5" label="合同终止日期"></el-table-column>
          <el-table-column prop="field6" label="合同金额"></el-table-column>
          <el-table-column prop="field7" label="合同状态"></el-table-column>
          <el-table-column prop="field8" label="当前处理人"></el-table-column>
        </el-table>
      </el-card>
    </el-row>
  </div>
</template>
<script>
  import * as echarts from 'echarts'
  export default {
    data() {
      return {
        allDatas: [
          {
            title: '月销售排名',
            showHeader: true,
            multi: false,
            tableData: [
              {
                name: '销售项目1',
                money: '4,562,013',
                belong: '项目组1',
                rate: '24%',
              },
              {
                name: '销售项目2',
                money: '4,034,578',
                belong: '项目组1',
                rate: '24%',
              },
              {
                name: '销售项目3',
                money: '4,000,000',
                belong: '项目组1',
                rate: '24%',
              },
              {
                name: '销售项目4',
                money: '4,000,000',
                belong: '项目组1',
                rate: '24%',
              },
              {
                name: '销售项目5',
                money: '4,000,000',
                belong: '项目组1',
                rate: '24%',
              },
              {
                name: '销售项目6',
                money: '4,000,000',
                belong: '项目组1',
                rate: '24%',
              },
              {
                name: '销售项目7',
                money: '4,000,000',
                belong: '项目组1',
                rate: '24%',
              },
            ],
          },
          {
            title: '合同覆约',
            showHeader: false,
            multi: true,
            tableData: [
              {
                name: '设备采购合同',
                status: '已完成',
              },
              {
                name: '2021年10月原材料采购合同',
                status: '已完成',
              },
              {
                name: '成品销售合同',
                status: '已完成',
              },
              {
                name: '特种设备采购合同',
                status: '已完成',
              },
              {
                name: '办公设备采购合同',
                status: '已完成',
              },
              {
                name: '2021年11月原材料采购合同',
                status: '已完成',
              },
              {
                name: '2021年12月原材料采购合同',
                status: '已完成',
              },
            ],
          },
          {
            title: '合同风险',
            showHeader: false,
            multi: true,
            tableData: [
              {
                name: '2021年9月原材料采购合同',
                field1: '8,632,180',
                field2: '56.11%',
                field3: '4,843,516',
              },
              {
                name: '2021年8月原材料采购合同',
                field1: '8,632,180',
                field2: '56.11%',
                field3: '4,843,516',
              },
              {
                name: '2021年7月原材料采购合同',
                field1: '8,632,180',
                field2: '56.11%',
                field3: '4,843,516',
              },
            ],
          },
        ],
        rankTableData: [
          {
            name: '华北地区',
            money: '30120,359',
          },
          {
            name: '华南地区',
            money: '28120,359',
          },
          {
            name: '东部地区',
            money: '27120,359',
          },
        ],
        drawTableData: [
          {
            field1: '合同标题1',
            field2: 'HT202102270001',
            field3: '签约公司1',
            field4: '2021-02-27',
            field5: '2021-03-27',
            field6: '2301,873',
            field7: '待签订',
            field8: '处理人1',
          },
          {
            field1: '合同标题1',
            field2: 'HT202102270001',
            field3: '签约公司1',
            field4: '2021-02-27',
            field5: '2021-03-27',
            field6: '2301,873',
            field7: '待签订',
            field8: '处理人1',
          },
          {
            field1: '合同标题2',
            field2: 'HT202102270001',
            field3: '签约公司1',
            field4: '2021-02-27',
            field5: '2021-03-27',
            field6: '2301,873',
            field7: '待签订',
            field8: '处理人1',
          },
          {
            field1: '合同标题1',
            field2: 'HT202102270001',
            field3: '签约公司1',
            field4: '2021-02-27',
            field5: '2021-03-27',
            field6: '2301,873',
            field7: '待签订',
            field8: '处理人1',
          },
          {
            field1: '合同标题1',
            field2: 'HT202102270001',
            field3: '签约公司1',
            field4: '2021-02-27',
            field5: '2021-03-27',
            field6: '2301,873',
            field7: '待签订',
            field8: '处理人1',
          },
          {
            field1: '合同标题1',
            field2: 'HT202102270001',
            field3: '签约公司1',
            field4: '2021-02-27',
            field5: '2021-03-27',
            field6: '2301,873',
            field7: '待签订',
            field8: '处理人1',
          },
          {
            field1: '合同标题1',
            field2: 'HT202102270001',
            field3: '签约公司1',
            field4: '2021-02-27',
            field5: '2021-03-27',
            field6: '2301,873',
            field7: '待签订',
            field8: '处理人1',
          },
          {
            field1: '合同标题1',
            field2: 'HT202102270001',
            field3: '签约公司1',
            field4: '2021-02-27',
            field5: '2021-03-27',
            field6: '2301,873',
            field7: '待签订',
            field8: '处理人1',
          },
          {
            field1: '合同标题1',
            field2: 'HT202102270001',
            field3: '签约公司1',
            field4: '2021-02-27',
            field5: '2021-03-27',
            field6: '2301,873',
            field7: '待签订',
            field8: '处理人1',
          },
          {
            field1: '合同标题1',
            field2: 'HT202102270001',
            field3: '签约公司1',
            field4: '2021-02-27',
            field5: '2021-03-27',
            field6: '2301,873',
            field7: '待签订',
            field8: '处理人1',
          },
          {
            field1: '合同标题1',
            field2: 'HT202102270001',
            field3: '签约公司1',
            field4: '2021-02-27',
            field5: '2021-03-27',
            field6: '2301,873',
            field7: '待签订',
            field8: '处理人1',
          },
          {
            field1: '合同标题1',
            field2: 'HT202102270001',
            field3: '签约公司1',
            field4: '2021-02-27',
            field5: '2021-03-27',
            field6: '2301,873',
            field7: '待签订',
            field8: '处理人1',
          },
          {
            field1: '合同标题1',
            field2: 'HT202102270001',
            field3: '签约公司1',
            field4: '2021-02-27',
            field5: '2021-03-27',
            field6: '2301,873',
            field7: '待签订',
            field8: '处理人1',
          },
          {
            field1: '合同标题1',
            field2: 'HT202102270001',
            field3: '签约公司1',
            field4: '2021-02-27',
            field5: '2021-03-27',
            field6: '2301,873',
            field7: '待签订',
            field8: '处理人1',
          },
          {
            field1: '合同标题1',
            field2: 'HT202102270001',
            field3: '签约公司1',
            field4: '2021-02-27',
            field5: '2021-03-27',
            field6: '2301,873',
            field7: '待签订',
            field8: '处理人1',
          },
        ],
      }
    },
    mounted() {
      this.setCharts1()
      this.setCharts2()
      this.setCharts3()
      this.setCharts4()
    },
    methods: {
      setCharts1() {
        const chats = echarts.init(document.getElementById('chats-1'))
        const option = {
          title: {
            text: '销售合同统计',
            left: 'left',
          },
          tooltip: {
            trigger: 'axis',
            axisPointer: {
              type: 'cross',
              crossStyle: {
                color: '#999',
              },
            },
          },
          toolbox: {
            feature: {
              dataView: { show: true, readOnly: false },
              magicType: { show: true, type: ['line', 'bar'] },
              restore: { show: true },
              saveAsImage: { show: true },
            },
          },
          legend: {
            data: ['Evaporation', 'Precipitation', 'Temperature'],
          },
          xAxis: [
            {
              type: 'category',
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
              axisPointer: {
                type: 'shadow',
              },
            },
          ],
          yAxis: [
            {
              type: 'value',
              name: '柱状',
              min: 0,
              max: 1800,
            },
            {
              type: 'value',
              name: '折线',
              min: 0,
              max: 1500,
            },
          ],
          series: [
            {
              name: '柱状',
              type: 'bar',
              tooltip: {
                valueFormatter: function (value) {
                  return value
                },
              },
              data: [
                300, 200, 799, 560, 780, 767, 1356, 1622, 326, 200, 64, 3.3,
              ],
            },
            {
              name: '折线',
              type: 'line',
              yAxisIndex: 1,
              tooltip: {
                valueFormatter: function (value) {
                  return value
                },
              },
              data: [
                200, 220, 330, 450, 630, 1020, 203, 234, 230, 160, 120, 620,
              ],
            },
          ],
        }
        chats.setOption(option)
      },
      setCharts2() {
        const chats = echarts.init(document.getElementById('chats-2'))
        const option = {
          color: ['#a3e1d4', '#6dbcce'],
          tooltip: {
            trigger: 'axis',
            axisPointer: {
              type: 'cross',
              label: {
                backgroundColor: '#6a7985',
              },
            },
          },
          toolbox: {
            feature: {
              saveAsImage: {},
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
              boundaryGap: false,
              data: ['2', '4', '6', '8', '10', '12', '14'],
            },
          ],
          yAxis: [
            {
              type: 'value',
            },
          ],
          series: [
            {
              name: 'Line 1',
              type: 'line',
              stack: 'Total',
              smooth: true,
              lineStyle: {
                width: 0,
              },
              showSymbol: false,
              areaStyle: {
                opacity: 0.8,
                color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                  {
                    offset: 0,
                    color: 'rgb(128, 255, 165)',
                  },
                  {
                    offset: 1,
                    color: 'rgb(1, 191, 236)',
                  },
                ]),
              },
              emphasis: {
                focus: 'series',
              },
              data: [140, 232, 101, 264, 90, 340, 250],
            },
            {
              name: 'Line 2',
              type: 'line',
              stack: 'Total',
              smooth: true,
              lineStyle: {
                width: 0,
              },
              showSymbol: false,
              areaStyle: {
                opacity: 0.8,
                color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                  {
                    offset: 0,
                    color: 'rgb(0, 221, 255)',
                  },
                  {
                    offset: 1,
                    color: 'rgb(77, 119, 255)',
                  },
                ]),
              },
              emphasis: {
                focus: 'series',
              },
              data: [120, 282, 111, 234, 220, 340, 310],
            },
          ],
        }
        chats.setOption(option)
      },
      setCharts3() {
        const charts = echarts.init(document.getElementById('chats-3'))
        const option = {
          color: ['#2dc7c9', '#1d84c6', '#65a4d4'],
          tooltip: {
            trigger: 'item',
          },
          series: [
            {
              name: 'Access From',
              type: 'pie',
              radius: '65%',
              avoidLabelOverlap: false,

              label: {
                show: false,
                position: 'center',
              },
              emphasis: {
                label: {
                  show: true,
                  fontSize: 40,
                  fontWeight: 'bold',
                },
              },
              labelLine: {
                show: false,
              },
              data: [
                { value: 1048, name: 'Search Engine' },
                { value: 735, name: 'Direct' },
                { value: 580, name: 'Email' },
              ],
            },
          ],
        }
        charts.setOption(option)
      },
      setCharts4() {
        const charts = echarts.init(document.getElementById('chats-4'))
        const option = {
          tooltip: {
            trigger: 'item',
          },
          series: [
            {
              name: 'Access From',
              type: 'pie',
              radius: ['40%', '70%'],
              avoidLabelOverlap: false,
              label: {
                show: false,
                position: 'center',
              },
              emphasis: {
                label: {
                  show: true,
                  fontSize: 40,
                  fontWeight: 'bold',
                },
              },
              labelLine: {
                show: false,
              },
              data: [
                { value: 1048, name: 'Search Engine' },
                { value: 735, name: 'Direct' },
                { value: 580, name: 'Email' },
                { value: 484, name: 'Union Ads' },
                { value: 300, name: 'Video Ads' },
              ],
            },
          ],
        }
        charts.setOption(option)
      },
    },
  }
</script>
<style scoped lang="scss">
  .topcard {
    display: flex;
    // padding: 0 5px;
  }
  .topcard .item {
    // margin: 0 5px;
    flex: 1;
  }
  .topcard .item :deep(.el-card__body) {
    padding: 0 !important;
  }
  .topcard .item .title {
    background-color: rgb(255, 255, 255);
    border-color: rgb(228, 230, 231);
    border-image: none 100% / 1 / 0 stretch;
    border-style: solid solid none;
    border-width: 3px 0px 0px;
    color: inherit;
    margin-bottom: 0px;
    padding: 14px 15px 7px;
    min-height: 48px;
    display: flex;
  }
  .topcard .item .title h5 {
    display: inline-block;
    font-size: 14px;
    margin: 0px 0px 7px;
    padding: 0px;
    text-overflow: ellipsis;
    flex: 17;
  }
  .topcard .item .title .right {
    flex: 1;
    // justify-content: right;
  }
  .topcard .item .content .right {
    flex: 1;
    // justify-content: right;
    // text-align: right;
  }
  .topcard .item .content {
    background-color: rgb(255, 255, 255);
    color: inherit;
    padding: 15px 20px 20px;
    border-color: rgb(231, 234, 236);
    border-image: none 100% / 1 / 0 stretch;
    border-style: solid solid none;
    border-width: 1px 0px;
  }
  .topcard .item .content h1 {
    display: block;
    // font-size: 2em;
    // margin-block-start: 0.67em;
    // margin-block-end: 0.67em;
    // margin-inline-start: 0px;
    // margin-inline-end: 0px;
    font-weight: 100;
    font-size: 30px;

    line-height: 1.1;
    color: rgb(51, 51, 51);
  }
  .topcard .item .content .small {
    flex: 17;
    font-size: 85%;
  }
  .topcard .item .content .footer {
    display: flex;
  }

  .count-item {
    // margin: 20px 0;
    margin-bottom: 25px;
  }

  .count-1 .count-item .count-num {
    font-size: 20px;
    color: #808080;
  }

  .count-1 .count-item .count-tip {
    font-size: 12px;
    color: #808080;
    margin: 4px 0;
    display: flex;
    justify-content: space-between;
  }

  .count-1 .count-item .count-progress .actived {
    background-color: #1ab394;
    height: 5px;
  }
  .count-1 .count-item .count-progress .unactived {
    background-color: #f5f5f5;
    height: 5px;
  }

  .green {
    color: #1ab394;
  }

  .box-1 {
    width: 22px;
    height: 22px;
    text-align: center;
    color: #fff;
    background-color: #1c84c6;
    border-radius: 4px;
  }

  .box-2 {
    width: 22px;
    height: 22px;
    text-align: center;
    color: #fff;
    background-color: #23c6c8;
    border-radius: 4px;
  }

  .box-3 {
    width: 22px;
    height: 22px;
    text-align: center;
    color: #fff;
    background-color: #1ab394;
    border-radius: 4px;
  }
</style>
