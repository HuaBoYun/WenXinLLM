<template>
  <div class="system-log-container">
    <div class="content">
      <el-row :gutter="10">
        <el-col :span="6">
          <el-card>
            <div class="card-content">
              <div class="top">
                <div class="left">验收总额</div>
                <div class="right">同比 99%</div>
              </div>
              <div class="bottom">
                368,800
                <span class="unit">万元</span>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card>
            <div class="card-content">
              <div class="top">
                <div class="left">本月验收</div>
                <div class="right">同比 99%</div>
              </div>
              <div class="bottom">
                9809
                <span class="unit">万元</span>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card>
            <div class="card-content">
              <div class="top">
                <div class="left">收款总额</div>
                <div class="right">同比 99%</div>
              </div>
              <div class="bottom">
                298,800
                <span class="unit">万元</span>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card>
            <div class="card-content">
              <div class="top">
                <div class="left">本月收款</div>
                <div class="right">同比 99%</div>
              </div>
              <div class="bottom">
                8009
                <span class="unit">万元</span>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>

      <el-row :gutter="10">
        <el-col :lg="18" :md="18" :sm="18">
          <el-row>
            <el-col :span="24">
              <el-card>
                <div id="chats-1" style="width: 100%; height: 300px"></div>
              </el-card>
            </el-col>
            <el-col :span="24">
              <el-card>
                <h3 style="margin-top: 0">大区验收统计</h3>
                <el-table :data="tableData2">
                  <el-table-column
                    align="center"
                    label="大区"
                    prop="name"
                  ></el-table-column>
                  <el-table-column align="center" label="第一季度">
                    <el-table-column
                      align="center"
                      label="计划验收"
                      prop="field1"
                    ></el-table-column>
                    <el-table-column
                      align="center"
                      label="实际验收"
                      prop="field2"
                    ></el-table-column>
                  </el-table-column>
                  <el-table-column align="center" label="第二季度">
                    <el-table-column
                      align="center"
                      label="计划验收"
                      prop="field3"
                    ></el-table-column>
                    <el-table-column
                      align="center"
                      label="实际验收"
                      prop="field4"
                    ></el-table-column>
                  </el-table-column>
                  <el-table-column align="center" label="第三季度">
                    <el-table-column
                      align="center"
                      label="计划验收"
                      prop="field5"
                    ></el-table-column>
                    <el-table-column
                      align="center"
                      label="实际验收"
                      prop="field6"
                    ></el-table-column>
                  </el-table-column>
                  <el-table-column align="center" label="第四季度">
                    <el-table-column
                      align="center"
                      label="计划验收"
                      prop="field7"
                    ></el-table-column>
                    <el-table-column
                      align="center"
                      label="实际验收"
                      prop="field8"
                    ></el-table-column>
                  </el-table-column>
                </el-table>
              </el-card>
            </el-col>
          </el-row>
        </el-col>
        <el-col :lg="6" :md="6" :sm="6">
          <el-card>
            <h3 style="margin-top: 0">本年项目验收排名</h3>
            <el-table :data="tableData1">
              <el-table-column
                prop="avator"
                label="排名"
                #default="{ $index }"
                :width="60"
              >
                <div class="name-content">
                  <div class="rank-img">
                    <img v-if="rankImg[$index]" :src="rankImg[$index]" alt="" />
                    <div v-else class="rank-num">{{ $index + 1 }}</div>
                  </div>
                </div>
              </el-table-column>
              <el-table-column prop="name" label="区域"></el-table-column>
              <el-table-column prop="money" label="金额">
                <template #default="{ row, $index }">
                  <div class="name-content" :class="'money' + ($index + 1)">
                    {{ row.money }}万元
                  </div>
                </template>
              </el-table-column>
            </el-table>
          </el-card>
        </el-col>
      </el-row>

      <el-row :gutter="10">
        <el-col :lg="18" :md="18" :sm="18">
          <el-row>
            <el-col :span="24">
              <el-card>
                <div id="chats-2" style="width: 100%; height: 300px"></div>
              </el-card>
            </el-col>
            <el-col :span="24">
              <el-card>
                <h3 style="margin-top: 0">大区收款统计</h3>
                <el-table :data="tableData2">
                  <el-table-column
                    align="center"
                    label="大区"
                    prop="name"
                  ></el-table-column>
                  <el-table-column align="center" label="第一季度">
                    <el-table-column
                      align="center"
                      label="计划验收"
                      prop="field1"
                    ></el-table-column>
                    <el-table-column
                      align="center"
                      label="实际验收"
                      prop="field2"
                    ></el-table-column>
                  </el-table-column>
                  <el-table-column align="center" label="第二季度">
                    <el-table-column
                      align="center"
                      label="计划验收"
                      prop="field3"
                    ></el-table-column>
                    <el-table-column
                      align="center"
                      label="实际验收"
                      prop="field4"
                    ></el-table-column>
                  </el-table-column>
                  <el-table-column align="center" label="第三季度">
                    <el-table-column
                      align="center"
                      label="计划验收"
                      prop="field5"
                    ></el-table-column>
                    <el-table-column
                      align="center"
                      label="实际验收"
                      prop="field6"
                    ></el-table-column>
                  </el-table-column>
                  <el-table-column align="center" label="第四季度">
                    <el-table-column
                      align="center"
                      label="计划验收"
                      prop="field7"
                    ></el-table-column>
                    <el-table-column
                      align="center"
                      label="实际验收"
                      prop="field8"
                    ></el-table-column>
                  </el-table-column>
                </el-table>
              </el-card>
            </el-col>
          </el-row>
        </el-col>
        <el-col :lg="6" :md="6" :sm="6">
          <el-card>
            <h3 style="margin-top: 0">本年项目收款排名</h3>
            <el-table :data="tableData1">
              <el-table-column
                prop="avator"
                label="排名"
                #default="{ $index }"
                :width="60"
              >
                <div class="name-content">
                  <div class="rank-img">
                    <img v-if="rankImg[$index]" :src="rankImg[$index]" alt="" />
                    <div v-else class="rank-num">{{ $index + 1 }}</div>
                  </div>
                </div>
              </el-table-column>
              <el-table-column prop="name" label="区域"></el-table-column>
              <el-table-column prop="money" label="金额">
                <template #default="{ row, $index }">
                  <div class="name-content" :class="'money' + ($index + 1)">
                    {{ row.money }}万元
                  </div>
                </template>
              </el-table-column>
            </el-table>
          </el-card>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
  import * as echarts from 'echarts'
  import './utils/china'
  const img1 = require('@/assets/indexPage/report/yjtjbb/01.png')
  const img2 = require('@/assets/indexPage/report/yjtjbb/02.png')
  const img3 = require('@/assets/indexPage/report/yjtjbb/03.png')
  export default {
    name: 'Download',
    components: {},
    data() {
      return {
        rankImg: [img1, img2, img3],
        tableData1: [
          { name: '广东大区', bel: '华南大区_销售部', money: 1391 },
          { name: '华南大区', bel: '华南大区_销售部', money: 1391 },
          { name: '西南大区', bel: '华南大区_销售部', money: 1391 },
          { name: '华中大区', bel: '华南大区_销售部', money: 1391 },
          { name: '华北大区', bel: '华南大区_销售部', money: 1391 },
          { name: '上海大区', bel: '华南大区_销售部', money: 1391 },
          { name: '北京大区', bel: '华南大区_销售部', money: 1391 },
          { name: '华东大区', bel: '华南大区_销售部', money: 1391 },
          { name: '西北大区', bel: '华南大区_销售部', money: 1391 },
          { name: '海外国际', bel: '华南大区_销售部', money: 1391 },
        ],
        tableData2: [
          {
            name: '广东大区',
            field1: '305万元',
            field2: '358万元',
            field3: '310万元',
            field4: '360万元',
            field5: '320万元',
            field6: '290万元',
            field7: '470万元',
            field8: '480万元',
          },
          {
            name: '华南大区',
            field1: '305万元',
            field2: '358万元',
            field3: '310万元',
            field4: '360万元',
            field5: '320万元',
            field6: '290万元',
            field7: '470万元',
            field8: '480万元',
          },
          {
            name: '西南大区',
            field1: '305万元',
            field2: '358万元',
            field3: '310万元',
            field4: '360万元',
            field5: '320万元',
            field6: '290万元',
            field7: '470万元',
            field8: '480万元',
          },
          {
            name: '华中大区',
            field1: '305万元',
            field2: '358万元',
            field3: '310万元',
            field4: '360万元',
            field5: '320万元',
            field6: '290万元',
            field7: '470万元',
            field8: '480万元',
          },
          {
            name: '华北大区',
            field1: '305万元',
            field2: '358万元',
            field3: '310万元',
            field4: '360万元',
            field5: '320万元',
            field6: '290万元',
            field7: '470万元',
            field8: '480万元',
          },
          {
            name: '上海大区',
            field1: '305万元',
            field2: '358万元',
            field3: '310万元',
            field4: '360万元',
            field5: '320万元',
            field6: '290万元',
            field7: '470万元',
            field8: '480万元',
          },
          {
            name: '北京大区',
            field1: '305万元',
            field2: '358万元',
            field3: '310万元',
            field4: '360万元',
            field5: '320万元',
            field6: '290万元',
            field7: '470万元',
            field8: '480万元',
          },
          {
            name: '华东大区',
            field1: '305万元',
            field2: '358万元',
            field3: '310万元',
            field4: '360万元',
            field5: '320万元',
            field6: '290万元',
            field7: '470万元',
            field8: '480万元',
          },
          {
            name: '西北大区',
            field1: '305万元',
            field2: '358万元',
            field3: '310万元',
            field4: '360万元',
            field5: '320万元',
            field6: '290万元',
            field7: '470万元',
            field8: '480万元',
          },
          {
            name: '海外国际',
            field1: '305万元',
            field2: '358万元',
            field3: '310万元',
            field4: '360万元',
            field5: '320万元',
            field6: '290万元',
            field7: '470万元',
            field8: '480万元',
          },
        ],
      }
    },
    mounted() {
      this.setCharts1()
      this.setCharts2()
    },
    created() {},
    methods: {
      setCharts1() {
        const myChart = echarts.init(document.getElementById('chats-1'))
        const option = {
          title: {
            text: '项目验收统计',
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
        option && myChart.setOption(option)
      },
      setCharts2() {
        const myChart = echarts.init(document.getElementById('chats-2'))
        const option = {
          title: {
            text: '大区收款统计',
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
        option && myChart.setOption(option)
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

  .card-content .top {
    display: flex;
    align-items: center;
    justify-content: space-between;
    font-size: 14px;
    padding: 10px 0;
    border-bottom: 1px solid rgba(0, 0, 0, 0.1);
  }

  .card-content .bottom {
    font-size: 28px;
    padding-top: 10px;
  }

  .card-content .bottom .unit {
    font-size: 14px;
    color: #808080;
  }

  .money1 {
    color: #f15e47;
  }
  .money2 {
    color: #ffc200;
  }
  .money3 {
    color: #f5a623;
  }

  .rank-img {
    width: 22px;
    height: 22px;
    border-radius: 50%;
    overflow: hidden;
    margin-right: 20px;
  }

  .rank-num {
    color: #737c8b;
    background-color: #f6f6f6;
    line-height: 22px;
    text-align: center;
  }

  img {
    width: 100%;
    height: 100%;
  }

  .name-content {
    display: flex;
    align-items: center;
  }
</style>
