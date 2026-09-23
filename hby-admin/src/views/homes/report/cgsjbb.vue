<template>
  <div>
    <first></first>

    <el-row :gutter="10" style="margin-top: 10px">
      <el-col :span="18">
        <el-card>
          <h3>招标进度情况</h3>
          <div class="stepList">
            <div class="header">
              <h4>招标项目</h4>
              <div class="right">
                <h4>寻源</h4>
                <h4>招标</h4>
                <h4>投标</h4>
                <h4>比价</h4>
                <h4>评审</h4>
                <h4>中标</h4>
              </div>
            </div>
            <div class="listBody">
              <div class="header" v-for="item in stepList">
                <div>{{ item.name }}</div>
                <div class="stepContent">
                  <el-steps :active="item.process" finish-status="success">
                    <el-step></el-step>
                    <el-step></el-step>
                    <el-step></el-step>
                    <el-step></el-step>
                    <el-step></el-step>
                    <el-step></el-step>
                  </el-steps>
                </div>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card>
          <h3>报价更新</h3>
          <div class="table1">
            <el-table :data="tableData1" :show-header="false">
              <el-table-column prop="name" :width="220">
                <template #default="{ row }">
                  <div class="name">{{ row.name }}</div>
                  <div class="date">更新：{{ row.date }}</div>
                </template>
              </el-table-column>
              <el-table-column prop="money"></el-table-column>
            </el-table>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="10">
      <el-col :span="6">
        <el-card>
          <div id="chats-1" style="width: 100%; height: 300px"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <div id="chats-2" style="width: 100%; height: 300px"></div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card>
          <el-card>
            <h3>风险预警</h3>
            <div class="table2">
              <el-table :data="tableData2" :show-header="false">
                <el-table-column prop="name"></el-table-column>
                <el-table-column prop="num"></el-table-column>
              </el-table>
            </div>
          </el-card>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
  import * as echarts from 'echarts'
  import first from './cgsjbb/first.vue'
  export default {
    components: { first },
    data() {
      return {
        stepList: [
          {
            name: '年度办公电脑更新采购（32台）',
            process: 6,
          },
          {
            name: '中秋节日员工礼物采购（5200份）',
            process: 5,
          },
          {
            name: '9月办公室办公用品采购',
            process: 4,
          },
          {
            name: '信创云服务器采购（6台）',
            process: 3,
          },
          {
            name: '人体工程学办公椅专项采购（528张）',
            process: 3,
          },
          {
            name: '网络设备采购项目',
            process: 2,
          },
        ],
        tableData1: [
          {
            name: '办公台式电脑报价更新/台',
            date: '2023-02-16',
            money: '3,200.00元',
          },
          {
            name: '办公笔记本电脑报价更新/台',
            date: '2023-02-15',
            money: '5,200.00元',
          },
          {
            name: '黑色墨粉盒LT201/个',
            date: '2023-02-14',
            money: '199.00元',
          },
          {
            name: 'A4打印用纸（5包、2500张）/箱',
            date: '2023-02-13',
            money: '85.00元',
          },
          {
            name: '激光打印机SDW2606/台',
            date: '2023-02-12',
            money: '2,798.00元',
          },
        ],
        tableData2: [
          {
            name: '采购风险',
            num: 3,
          },
          {
            name: '采购合同风险',
            num: 5,
          },
          {
            name: '供应商经营风险',
            num: 12,
          },
          {
            name: '供应商资质到期',
            num: 1,
          },
        ],
      }
    },
    mounted() {
      this.setCharts1()
      this.setCharts2()
    },
    methods: {
      setCharts1() {
        const charts = echarts.init(document.getElementById('chats-1'))
        const option = {
          title: {
            text: '采购类型统计',
            left: 'left',
          },
          tooltip: {
            trigger: 'item',
            formatter: '{b} : {c} ({d}%)',
          },
          legend: {
            bottom: 0,
            left: 'center',
            data: [
              '办公用品型采购',
              '生产型采购',
              '项目型采购',
              '服务型采购',
              '其他',
            ],
          },
          color: ['#2dc7c9', '#1d84c6', '#65a4d4', '#89b9df', '#bfd8ef'],
          series: [
            {
              type: 'pie',
              radius: '65%',
              center: ['50%', '50%'],
              selectedMode: 'single',
              data: [
                { value: 900, name: '办公用品型采购' },
                { value: 735, name: '生产型采购' },
                { value: 510, name: '项目型采购' },
                { value: 434, name: '服务型采购' },
                { value: 335, name: '其他' },
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
      setCharts2() {
        const charts = echarts.init(document.getElementById('chats-2'))
        const option = {
          title: {
            text: '采购金额统计',
            subtext: '单位：万元',
            left: 'left',
          },
          legend: {
            bottom: 0,
            left: 'center',
            data: ['计划采购', '实际采购'],
          },
          tooltip: {},
          color: ['#1d84c6', '#2dc7c9'],
          xAxis: { type: 'category' },
          yAxis: {},
          series: [
            {
              name: 'bar1',
              type: 'bar',
              data: [
                24000, 17000, 15600, 36600, 39600, 24000, 17000, 15600, 36600,
                24000, 17000, 15600, 36600,
              ],
            },
            {
              name: 'bar2',
              type: 'bar',
              data: [
                8000, 9000, 4000, 6000, 3000, 8000, 9000, 4000, 6000, 8000,
                9000, 4000, 6000,
              ],
            },
          ],
        }
        charts.setOption(option)
      },
    },
  }
</script>

<style scoped>
  .stepList .header,
  .stepList .header .right {
    display: flex;
    align-items: center;
    justify-content: space-between;
  }

  .stepList .header .right {
    width: 60%;
  }

  .stepList .header .stepContent {
    width: 60%;
  }

  .stepContent /deep/ .is-success .el-step__icon.is-text {
    background-color: #20b759;
    color: #fff;
    border: none;
  }
  .stepContent /deep/ .is-process .el-step__icon-inner {
    content: '';
  }

  .listBody .header {
    margin-bottom: 20px;
  }

  .table1 .date {
    color: #999;
  }
</style>
