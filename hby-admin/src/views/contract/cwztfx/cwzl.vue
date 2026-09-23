<template>
  <!--  <div id="abc" class="system-log-container"></div>-->
  <div id="app">
    <div class="header fixed-top">
      <el-form :inline="true" label-width="80px">
        <el-form-item label="年">
          <el-select
            id="inputGroupSelectYear"
            class="form-select"
            v-model="queryForm.year"
          >
            <el-option value="2021">2021</el-option>
            <el-option value="2020">2020</el-option>
            <el-option value="2019">2019</el-option>
            <el-option value="2018">2018</el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="月">
          <el-select
            id="inputGroupSelectMonth"
            class="form-select"
            v-model="queryForm.month"
          >
            <el-option value="0">全部</el-option>
            <el-option value="1">1月</el-option>
            <el-option value="2">2月</el-option>
            <el-option value="3">3月</el-option>
            <el-option value="4">4月</el-option>
            <el-option value="5">5月</el-option>
            <el-option value="6">6月</el-option>
            <el-option value="7">7月</el-option>
            <el-option value="8">8月</el-option>
            <el-option value="9">9月</el-option>
            <el-option value="10">10月</el-option>
            <el-option value="11">11月</el-option>
            <el-option value="12">12月</el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button icon="el-icon-search" type="primary" @click="handleSearch" />
        </el-form-item>
      </el-form>
    </div>
    <div class="content">
      <el-row :gutter="10">
        <el-col :lg="24" :md="6" :sm="24">
          <div class="titleBar">利润分析</div>
        </el-col>

        <el-col :lg="12" :md="6" :sm="12" class="border_none">
          <el-row :gutter="10">
            <el-col :lg="12" :md="6" :sm="12">
              <div
                class="one-item flex-col padding-10 margin-bottom-0"
                style="padding: 15px 10px"
              >
                <div class="flex align-baseline margin-bottom-10">
                  <div class="price">298.11</div>
                  <div class="unit">万元</div>
                </div>
                <div class="title margin-bottom-10">利润总额</div>
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
                style="padding: 15px 10px"
              >
                <div class="flex align-baseline margin-bottom-10">
                  <div class="price">298.11</div>
                  <div class="unit">万元</div>
                </div>
                <div class="title margin-bottom-10">累计利润总额</div>
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
                style="padding: 15px 10px"
              >
                <div class="flex align-baseline margin-bottom-10">
                  <div class="price">298.11</div>
                  <div class="unit">万元</div>
                </div>
                <div class="title margin-bottom-10">净利润</div>
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
                style="padding: 15px 10px"
              >
                <div class="flex align-baseline margin-bottom-10">
                  <div class="price">298.11</div>
                  <div class="unit">万元</div>
                </div>
                <div class="title margin-bottom-10">累计净利润</div>
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

        <el-col :lg="6" :md="8" :sm="24">
          <div id="chats-1" style="width: 100%; height: 242px"></div>
        </el-col>
        <el-col :lg="6" :md="8" :sm="24">
          <div id="chats-2" style="width: 100%; height: 242px"></div>
        </el-col>
      </el-row>

      <el-row :gutter="10">
        <el-col :lg="24" :md="12" :sm="24">
          <div id="chats-6" style="width: 100%; height: 300px"></div>
        </el-col>
      </el-row>

      <el-row :gutter="10">
        <el-col :lg="8" :md="16" :sm="24">
          <div id="chats-3" style="width: 100%; height: 300px"></div>
        </el-col>
        <el-col :lg="16" :md="8" :sm="24">
          <div id="chats-4" style="width: 100%; height: 300px"></div>
        </el-col>
      </el-row>

      <el-col :lg="24" :md="6" :sm="24">
        <div class="titleBar">收入分析</div>
      </el-col>

      <el-col :lg="16" :md="6" :sm="12" class="border_none">
        <el-row :gutter="10">
          <el-col :lg="8" :md="6" :sm="12">
            <div
              class="one-item flex-col padding-10 margin-bottom-0"
              style="padding: 30px 10px"
            >
              <div class="flex align-baseline margin-bottom-10">
                <div class="price">298.11</div>
                <div class="unit">万元</div>
              </div>
              <div class="title margin-bottom-10">营业总收入</div>
              <div class="compare flex">
                <div class="margin-right-10">同比</div>
                <el-link type="danger">⬇-4.76%</el-link>
                <div class="margin-left-10 margin-right-10">环比</div>
                <el-link type="danger">⬇-4.76%</el-link>
              </div>
            </div>
          </el-col>
          <el-col :lg="8" :md="6" :sm="12">
            <div
              class="one-item flex-col padding-10 margin-bottom-0"
              style="padding: 30px 10px"
            >
              <div class="flex align-baseline margin-bottom-10">
                <div class="price">298.11</div>
                <div class="unit">万元</div>
              </div>
              <div class="title margin-bottom-10">累计营业总收入</div>
              <div class="compare flex">
                <div class="margin-right-10">同比</div>
                <el-link type="danger">⬇-4.76%</el-link>
                <div class="margin-left-10 margin-right-10">环比</div>
                <el-link type="danger">⬇-4.76%</el-link>
              </div>
            </div>
          </el-col>
          <el-col :lg="8" :md="6" :sm="12">
            <div
              class="one-item flex-col padding-10 margin-bottom-0"
              style="padding: 30px 10px"
            >
              <div class="flex align-baseline margin-bottom-10">
                <div class="price">298.11</div>
                <div class="unit">万元</div>
              </div>
              <div class="title margin-bottom-10">主营业务收入</div>
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
          <el-col :lg="8" :md="6" :sm="12">
            <div
              class="one-item flex-col padding-10 margin-bottom-0"
              style="padding: 30px 10px"
            >
              <div class="flex align-baseline margin-bottom-10">
                <div class="price">298.11</div>
                <div class="unit">万元</div>
              </div>
              <div class="title margin-bottom-10">累计主营业务收入</div>
              <div class="compare flex">
                <div class="margin-right-10">同比</div>
                <el-link type="danger">⬇-4.76%</el-link>
                <div class="margin-left-10 margin-right-10">环比</div>
                <el-link type="danger">⬇-4.76%</el-link>
              </div>
            </div>
          </el-col>
          <el-col :lg="8" :md="6" :sm="12">
            <div
              class="one-item flex-col padding-10 margin-bottom-0"
              style="padding: 30px 10px"
            >
              <div class="flex align-baseline margin-bottom-10">
                <div class="price">298.11</div>
                <div class="unit">万元</div>
              </div>
              <div class="title margin-bottom-10">其他业务收入</div>
              <div class="compare flex">
                <div class="margin-right-10">同比</div>
                <el-link type="danger">⬇-4.76%</el-link>
                <div class="margin-left-10 margin-right-10">环比</div>
                <el-link type="danger">⬇-4.76%</el-link>
              </div>
            </div>
          </el-col>
          <el-col :lg="8" :md="6" :sm="12">
            <div
              class="one-item flex-col padding-10 margin-bottom-0"
              style="padding: 30px 10px"
            >
              <div class="flex align-baseline margin-bottom-10">
                <div class="price">298.11</div>
                <div class="unit">万元</div>
              </div>
              <div class="title margin-bottom-10">累计其他业务收入</div>
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

      <el-row :gutter="10">
        <el-col :lg="8" :md="16" :sm="24">
          <div id="chats-5" style="width: 100%; height: 300px"></div>
        </el-col>
        <el-col :lg="24" :md="8" :sm="24">
          <div id="chats-7" style="width: 100%; height: 300px"></div>
        </el-col>
      </el-row>
      <el-row :gutter="10">
        <el-col :lg="24" :md="16" :sm="24">
          <div id="chats-8" style="width: 100%; height: 300px"></div>
        </el-col>
      </el-row>

      <el-col :lg="24" :md="6" :sm="24">
        <div class="titleBar">成本费用分析</div>
      </el-col>

      <el-row :gutter="10" style="margin-bottom: 10px">
        <el-col :lg="4" :md="6" :sm="12">
          <div class="one-item flex-col padding-10 margin-bottom-0">
            <div class="flex align-baseline margin-bottom-10">
              <div class="price">298.11</div>
              <div class="unit">万元</div>
            </div>
            <div class="title margin-bottom-10">营业成本</div>
          </div>
        </el-col>
        <el-col :lg="4" :md="6" :sm="12">
          <div class="one-item flex-col padding-10 margin-bottom-0">
            <div class="flex align-baseline margin-bottom-10">
              <div class="price">298.11</div>
              <div class="unit">万元</div>
            </div>
            <div class="title margin-bottom-10">其他业务成本</div>
          </div>
        </el-col>
        <el-col :lg="4" :md="6" :sm="12">
          <div class="one-item flex-col padding-10 margin-bottom-0">
            <div class="flex align-baseline margin-bottom-10">
              <div class="price">298.11</div>
              <div class="unit">万元</div>
            </div>
            <div class="title margin-bottom-10">主营业务成本</div>
          </div>
        </el-col>
        <el-col :lg="4" :md="6" :sm="12">
          <div class="one-item flex-col padding-10 margin-bottom-0">
            <div class="flex align-baseline margin-bottom-10">
              <div class="price">298.11</div>
              <div class="unit">万元</div>
            </div>
            <div class="title margin-bottom-10">财务费用</div>
          </div>
        </el-col>
        <el-col :lg="4" :md="6" :sm="12">
          <div class="one-item flex-col padding-10 margin-bottom-0">
            <div class="flex align-baseline margin-bottom-10">
              <div class="price">298.11</div>
              <div class="unit">万元</div>
            </div>
            <div class="title margin-bottom-10">管理费用</div>
          </div>
        </el-col>
        <el-col :lg="4" :md="6" :sm="12">
          <div class="one-item flex-col padding-10 margin-bottom-0">
            <div class="flex align-baseline margin-bottom-10">
              <div class="price">298.11</div>
              <div class="unit">万元</div>
            </div>
            <div class="title margin-bottom-10">销售费用</div>
          </div>
        </el-col>
      </el-row>

      <el-row :gutter="10">
        <el-col :lg="24" :md="8" :sm="24">
          <div id="chats-9" style="width: 100%; height: 300px"></div>
        </el-col>
      </el-row>
      <el-row :gutter="10">
        <el-col :lg="12" :md="8" :sm="24">
          <div id="chats-10" style="width: 100%; height: 300px"></div>
        </el-col>
        <el-col :lg="12" :md="8" :sm="24">
          <div id="chats-11" style="width: 100%; height: 300px"></div>
        </el-col>
      </el-row>
      <el-row :gutter="10">
        <el-col :lg="12" :md="8" :sm="24">
          <div id="chats-12" style="width: 100%; height: 300px"></div>
        </el-col>
        <el-col :lg="12" :md="8" :sm="24">
          <div id="chats-13" style="width: 100%; height: 300px"></div>
        </el-col>
      </el-row>

      <el-col :lg="24" :md="6" :sm="24">
        <div class="titleBar">应收应付分析</div>
      </el-col>

      <el-row :gutter="10" style="margin-bottom: 10px">
        <el-col :lg="6" :md="6" :sm="12">
          <div class="one-item flex-col padding-10 margin-bottom-0">
            <div class="flex align-baseline margin-bottom-10">
              <div class="price">298.11</div>
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
              <div class="price">298.11</div>
              <div class="unit">万元</div>
            </div>
            <div class="title margin-bottom-10">累计应收金额</div>
          </div>
        </el-col>
        <el-col :lg="6" :md="6" :sm="12">
          <div class="one-item flex-col padding-10 margin-bottom-0">
            <div class="flex align-baseline margin-bottom-10">
              <div class="price">298.11</div>
              <div class="unit">万元</div>
            </div>
            <div class="title margin-bottom-10">应付金额</div>
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
            <div class="title margin-bottom-10">累计应付金额</div>
          </div>
        </el-col>
      </el-row>

      <el-row :gutter="10">
        <el-col :lg="12" :md="16" :sm="24">
          <div id="chats-14" style="width: 100%; height: 300px"></div>
        </el-col>
        <el-col :lg="12" :md="16" :sm="24">
          <div id="chats-15" style="width: 100%; height: 300px"></div>
        </el-col>
      </el-row>
      <el-row :gutter="10">
        <el-col :lg="12" :md="16" :sm="24">
          <div id="chats-16" style="width: 100%; height: 300px"></div>
        </el-col>
        <el-col :lg="12" :md="16" :sm="24">
          <div id="chats-17" style="width: 100%; height: 300px"></div>
        </el-col>
      </el-row>

      <el-col :lg="24" :md="6" :sm="24">
        <div class="titleBar">资产负债分析</div>
      </el-col>

      <el-row :gutter="10" style="margin-bottom: 10px">
        <el-col :lg="6" :md="6" :sm="12">
          <div class="one-item flex-col padding-10 margin-bottom-0">
            <div class="flex align-baseline margin-bottom-10">
              <div class="price">298.11</div>
              <div class="unit">万元</div>
            </div>
            <div class="title margin-bottom-10">期末树</div>
          </div>
        </el-col>
        <el-col :lg="6" :md="6" :sm="12">
          <div class="one-item flex-col padding-10 margin-bottom-0">
            <div class="flex align-baseline margin-bottom-10">
              <div class="price">298.11</div>
              <div class="unit">万元</div>
            </div>
            <div class="title margin-bottom-10">其他流动负债</div>
          </div>
        </el-col>
        <el-col :lg="6" :md="6" :sm="12">
          <div class="one-item flex-col padding-10 margin-bottom-0">
            <div class="flex align-baseline margin-bottom-10">
              <div class="price">298.11</div>
              <div class="unit">万元</div>
            </div>
            <div class="title margin-bottom-10">非流动资产统计</div>
          </div>
        </el-col>
        <el-col :lg="6" :md="6" :sm="12">
          <div class="one-item flex-col padding-10 margin-bottom-0">
            <div class="flex align-baseline margin-bottom-10">
              <div class="price">298.11</div>
              <div class="unit">万元</div>
            </div>
            <div class="title margin-bottom-10">负债合计</div>
          </div>
        </el-col>
      </el-row>
      <el-row :gutter="10" style="margin-bottom: 10px">
        <el-col :lg="6" :md="6" :sm="12">
          <div class="one-item flex-col padding-10 margin-bottom-0">
            <div class="flex align-baseline margin-bottom-10">
              <div class="price">298.11</div>
              <div class="unit">万元</div>
            </div>
            <div class="title margin-bottom-10">流动资产合计</div>
          </div>
        </el-col>
        <el-col :lg="6" :md="6" :sm="12">
          <div class="one-item flex-col padding-10 margin-bottom-0">
            <div class="flex align-baseline margin-bottom-10">
              <div class="price">298.11</div>
              <div class="unit">万元</div>
            </div>
            <div class="title margin-bottom-10">非流动负债合计</div>
          </div>
        </el-col>
        <el-col :lg="6" :md="6" :sm="12">
          <div class="one-item flex-col padding-10 margin-bottom-0">
            <div class="flex align-baseline margin-bottom-10">
              <div class="price">298.11</div>
              <div class="unit">万元</div>
            </div>
            <div class="title margin-bottom-10">其他流动资产</div>
          </div>
        </el-col>
        <el-col :lg="6" :md="6" :sm="12">
          <div class="one-item flex-col padding-10 margin-bottom-0">
            <div class="flex align-baseline margin-bottom-10">
              <div class="price">298.11</div>
              <div class="unit">万元</div>
            </div>
            <div class="title margin-bottom-10">流动负债合计</div>
          </div>
        </el-col>
      </el-row>
      <el-col :lg="24" :md="16" :sm="24">
        <div id="chats-18" style="width: 100%; height: 300px"></div>
      </el-col>
      <el-row :gutter="10">
        <el-col :lg="12" :md="16" :sm="24">
          <div id="chats-19" style="width: 100%; height: 300px"></div>
        </el-col>
        <el-col :lg="12" :md="16" :sm="24">
          <div id="chats-20" style="width: 100%; height: 300px"></div>
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
    data() {
      return {
        queryForm: {
          year: '',
          month: '',
        },
      }
    },
    mounted() {
      var chartDom = document.getElementById('chats-1')
      var myChart = echarts.init(chartDom)
      var option
      option = {
        tooltip: {
          formatter: '{a} <br/>{b} : {c}%',
        },
        title: {
          text: '营业利润率',
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
      // myChart.resize({
      //   width: 500,
      //   height: 250,
      // })

      option && myChart.setOption(option)

      var chartDom2 = document.getElementById('chats-2')
      var myChart2 = echarts.init(chartDom2)
      var option2
      option2 = {
        tooltip: {
          formatter: '{a} <br/>{b} : {c}%',
        },
        title: {
          text: '净利润率',
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
      // myChart.resize({
      //   width: 500,
      //   height: 250,
      // })

      option2 && myChart2.setOption(option2)

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
            // ['product', '2015', '2016', '2017'],
            ['Matcha Latte', 43.3, 85.8, 93.7],
            ['Milk Tea', 83.1, 73.4, 55.1],
            ['Cheese Cocoa', 86.4, 65.2, 82.5],
            ['Walnut Brownie', 72.4, 53.9, 39.1],
          ],
        },
        xAxis: { type: 'category' },
        yAxis: {},
        series: [{ type: 'bar' }, { type: 'bar' }, { type: 'bar' }],
      }

      option3 && myChart3.setOption(option3)

      var chartDom4 = document.getElementById('chats-4')
      var myChart4 = echarts.init(chartDom4)
      var option4

      option4 = {
        title: {
          text: '月利润对比分析',
          left: 'left',
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

      option4 && myChart4.setOption(option4)

      var chartDom5 = document.getElementById('chats-5')
      var myChart5 = echarts.init(chartDom5)
      var option5

      option5 = {
        title: {
          text: '年收入情况分析【联动】',
          left: 'left',
        },
        xAxis: {
          type: 'category',
          data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'],
        },
        yAxis: {
          type: 'value',
        },
        series: [
          {
            data: [120, 200, 150, 80, 70, 110, 130],
            type: 'bar',
          },
        ],
      }

      option5 && myChart5.setOption(option5)

      const chats6 = echarts.init(document.getElementById('chats-6'))
      // 指定图表的配置项和数据
      chats6.setOption({
        title: {
          text: '营业利润趋势分析',
          subtext: '单位：万元',
          left: 'left',
        },
        legend: {
          left: 'center',
        },
        tooltip: {},
        dataset: {
          source: [
            ['product', '收款金额', '付款金额'],
            ['一月', 0, 0],
            ['二月', 0, 0],
            ['三月', 0, 0],
            ['四月', 0, 0],
            ['五月', 0, 0],
            ['六月', 0, 0],
            ['七月', 0, 0],
            ['八月', 0, 0],
            ['九月', 0, 0],
            ['十月', 0, 0],
            ['十一月', 0, 0],
            ['十二月', 0, 0],
          ],
        },
        xAxis: { type: 'category' },
        yAxis: {},

        series: [{ type: 'bar' }, { type: 'bar' }],
      })

      var chartDom7 = document.getElementById('chats-7')
      var myChart7 = echarts.init(chartDom7)
      var option7

      option7 = {
        title: {
          text: '月收入情况分析',
          left: 'left',
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

      option7 && myChart7.setOption(option7)

      var chartDom8 = document.getElementById('chats-8')
      var myChart8 = echarts.init(chartDom8)
      var option8

      option8 = {
        title: {
          text: '营业总收入排名Top10公司',
          subtext: '单位：万元',
          left: 'left',
        },
        xAxis: {
          type: 'category',
          data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'],
        },
        yAxis: {
          type: 'value',
        },
        series: [
          {
            data: [120, 200, 150, 80, 70, 110, 130],
            type: 'bar',
          },
        ],
      }

      option8 && myChart8.setOption(option8)

      var chartDom9 = document.getElementById('chats-9')
      var myChart9 = echarts.init(chartDom9)
      var option9

      option9 = {
        title: {
          text: '费用率对比情况',
          left: 'left',
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

      option9 && myChart9.setOption(option9)

      var chartDom10 = document.getElementById('chats-10')
      var myChart10 = echarts.init(chartDom10)
      var option10

      option10 = {
        title: {
          text: '年期间费用对比',
          left: 'left',
        },
        xAxis: {
          type: 'category',
          data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'],
        },
        yAxis: {
          type: 'value',
        },
        series: [
          {
            data: [120, 200, 150, 80, 70, 110, 130],
            type: 'bar',
          },
        ],
      }

      option10 && myChart10.setOption(option10)

      var chartDom11 = document.getElementById('chats-11')
      var myChart11 = echarts.init(chartDom11)
      var option11

      option11 = {
        title: {
          text: '月期间费用占比情况',
          left: 'left',
        },
        tooltip: {
          trigger: 'item',
        },
        legend: {
          orient: 'vertical',
          left: 'left',
        },
        series: [
          {
            name: 'Access From',
            type: 'pie',
            radius: '50%',
            data: [
              { value: 1048, name: 'Search Engine' },
              { value: 735, name: 'Direct' },
              { value: 580, name: 'Email' },
              { value: 484, name: 'Union Ads' },
              { value: 300, name: 'Video Ads' },
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

      option11 && myChart11.setOption(option11)

      var chartDom12 = document.getElementById('chats-12')
      var myChart12 = echarts.init(chartDom12)
      var option12

      option12 = {
        title: {
          text: '年成本对比分析',
          left: 'left',
        },
        xAxis: {
          type: 'category',
          data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'],
        },
        yAxis: {
          type: 'value',
        },
        series: [
          {
            data: [120, 200, 150, 80, 70, 110, 130],
            type: 'bar',
          },
        ],
      }

      option12 && myChart12.setOption(option12)

      var chartDom13 = document.getElementById('chats-13')
      var myChart13 = echarts.init(chartDom13)
      var option13

      option13 = {
        title: {
          text: '月成本对比分析',
          left: 'left',
        },
        xAxis: {
          type: 'category',
          data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'],
        },
        yAxis: {
          type: 'value',
        },
        series: [
          {
            data: [120, 200, 150, 80, 70, 110, 130],
            type: 'bar',
          },
        ],
      }

      option13 && myChart13.setOption(option13)

      var chartDom14 = document.getElementById('chats-14')
      var myChart14 = echarts.init(chartDom14)
      var option14

      option14 = {
        title: {
          text: '应收金额Top10',
          left: 'left',
        },
        xAxis: {
          type: 'category',
          data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'],
        },
        yAxis: {
          type: 'value',
        },
        series: [
          {
            data: [120, 200, 150, 80, 70, 110, 130],
            type: 'bar',
          },
        ],
      }

      option14 && myChart14.setOption(option14)

      var chartDom15 = document.getElementById('chats-15')
      var myChart15 = echarts.init(chartDom15)
      var option15

      option15 = {
        title: {
          text: '应付金额Top10',
          left: 'left',
        },
        xAxis: {
          type: 'category',
          data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'],
        },
        yAxis: {
          type: 'value',
        },
        series: [
          {
            data: [120, 200, 150, 80, 70, 110, 130],
            type: 'bar',
          },
        ],
      }

      option15 && myChart15.setOption(option15)

      var chartDom16 = document.getElementById('chats-16')
      var myChart16 = echarts.init(chartDom16)
      var option16

      option16 = {
        title: {
          text: '应收金额月度趋势分析',
          left: 'left',
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

      option16 && myChart16.setOption(option16)

      var chartDom17 = document.getElementById('chats-17')
      var myChart17 = echarts.init(chartDom17)
      var option17

      option17 = {
        title: {
          text: '应付金额月度趋势分析',
          left: 'left',
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

      option17 && myChart17.setOption(option17)

      var chartDom18 = document.getElementById('chats-18')
      var myChart18 = echarts.init(chartDom18)
      var option18

      option18 = {
        title: {
          text: '资产负债率月趋势',
          left: 'left',
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

      option18 && myChart18.setOption(option18)

      var chartDom19 = document.getElementById('chats-19')
      var myChart19 = echarts.init(chartDom19)
      var option19

      option19 = {
        title: {
          text: '资产负债率月度分析',
          left: 'left',
        },
        xAxis: {
          type: 'category',
          data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'],
        },
        yAxis: {
          type: 'value',
        },
        series: [
          {
            data: [120, 200, 150, 80, 70, 110, 130],
            type: 'bar',
          },
        ],
      }

      option19 && myChart19.setOption(option19)

      var chartDom20 = document.getElementById('chats-20')
      var myChart20 = echarts.init(chartDom20)
      var option20

      option20 = {
        title: {
          text: '对比分析',
          left: 'left',
        },
        xAxis: {
          type: 'category',
          data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'],
        },
        yAxis: {
          type: 'value',
        },
        series: [
          {
            data: [120, 200, 150, 80, 70, 110, 130],
            type: 'bar',
          },
        ],
      }

      option20 && myChart20.setOption(option20)
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

  <style scoped > .el-col > div {
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
