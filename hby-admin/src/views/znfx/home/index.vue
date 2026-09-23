<template>
  <!--  <div id="abc" class="system-log-container"></div>-->
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
        <el-form-item label="月">
          <el-select id="inputGroupSelectMonth" class="form-select">
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
          <el-button icon="el-icon-refresh-right" type="primary" />
        </el-form-item>
      </el-form>
    </div>
    <div class="content">
      <el-row :gutter="10">
        <el-col :lg="6" :md="6" :sm="12">
          <div class="one-item">
            <div>
              <h2>0</h2>
              <p>本年签订合同金额</p>
            </div>
            <div>
              <i class="el-icon-money"></i>
            </div>
          </div>
        </el-col>
        <el-col :lg="6" :md="6" :sm="12">
          <div class="one-item">
            <div>
              <h2>0</h2>
              <p>本年签订合同数量</p>
            </div>
            <div>
              <i class="el-icon-collection"></i>
            </div>
          </div>
        </el-col>
        <el-col :lg="6" :md="6" :sm="12">
          <div class="one-item">
            <div>
              <h2>0</h2>
              <p>执行中的高风险合同数量</p>
            </div>
            <div>
              <i class="el-icon-umbrella"></i>
            </div>
          </div>
        </el-col>
        <el-col :lg="6" :md="6" :sm="12">
          <div class="one-item">
            <div>
              <h2>0</h2>
              <p>合作方数量</p>
            </div>
            <div>
              <i class="el-icon-school"></i>
            </div>
          </div>
        </el-col>
        <el-col :lg="6" :md="6" :sm="12">
          <div class="one-item">
            <div>
              <h2>0</h2>
              <p>本年应收款金额</p>
            </div>
            <div>
              <i class="el-icon-house"></i>
            </div>
          </div>
        </el-col>
        <el-col :lg="6" :md="6" :sm="12">
          <div class="one-item">
            <div>
              <h2>0</h2>
              <p>本年实际收款金额</p>
            </div>
            <div>
              <i class="el-icon-film"></i>
            </div>
          </div>
        </el-col>
        <el-col :lg="6" :md="6" :sm="12">
          <div class="one-item">
            <div>
              <h2>0</h2>
              <p>本年应付款金额</p>
            </div>
            <div>
              <i class="el-icon-suitcase"></i>
            </div>
          </div>
        </el-col>
        <el-col :lg="6" :md="6" :sm="12">
          <div class="one-item">
            <div>
              <h2>0</h2>
              <p>本年实际付款金额</p>
            </div>
            <div>
              <i class="el-icon-takeaway-box"></i>
            </div>
          </div>
        </el-col>
      </el-row>

      <el-row :gutter="10">
        <el-col :lg="8" :md="8" :sm="24">
          <div id="chats-1" style="width: 100%; height: 300px"></div>
        </el-col>
        <el-col :lg="8" :md="8" :sm="24">
          <div id="chats-2" style="width: 100%; height: 300px"></div>
        </el-col>
        <el-col :lg="8" :md="8" :sm="24">
          <div id="chats-3" style="width: 100%; height: 300px"></div>
        </el-col>
      </el-row>

      <el-row :gutter="10">
        <el-col :lg="12" :md="12" :sm="24">
          <div id="chats-4" style="width: 100%; height: 300px"></div>
        </el-col>
        <el-col :lg="12" :md="12" :sm="24">
          <div id="chats-5" style="width: 100%; height: 300px"></div>
        </el-col>
      </el-row>

      <el-row :gutter="10">
        <el-col :lg="16" :md="16" :sm="24">
          <div id="chats-6" style="width: 100%; height: 300px"></div>
        </el-col>
        <el-col :lg="8" :md="8" :sm="24">
          <div id="chats-7" style="width: 100%; height: 300px"></div>
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
      const chats1 = echarts.init(document.getElementById('chats-1'))

      // 指定图表的配置项和数据
      chats1.setOption({
        title: {
          text: '各业务类别合同情况',
          left: 'left',
        },
        tooltip: {
          trigger: 'item',
        },
        legend: {
          top: '15%',
          left: 'center',
        },
        series: [
          {
            name: '合同数量',
            type: 'pie',
            top: '20%',
            radius: '50%',
            data: [
              { value: 0, name: '原材料供应/履行中' },
              { value: 0, name: '工程建设/履行中' },
              { value: 0, name: '服务与咨询/履行中' },
              { value: 0, name: '设备与资产/履行中' },
              { value: 0, name: '设备与资产/已订立' },
              { value: 0, name: '设计与技术/履行中' },
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
      })

      const chats2 = echarts.init(document.getElementById('chats-2'))

      // 指定图表的配置项和数据
      chats2.setOption({
        title: {
          text: '各种类的合同情况',
          left: 'left',
        },
        tooltip: {
          trigger: 'item',
        },
        legend: {
          top: '15%',
          left: 'center',
        },
        series: [
          {
            name: '合同数量',
            type: 'pie',
            top: '15%',
            radius: ['20%', '50%'],
            data: [
              { value: 0, name: '买卖合同' },
              { value: 0, name: '建设工程合同' },
              { value: 0, name: '技术合同' },
              { value: 0, name: '租赁合同' },
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
      })

      // 基于准备好的dom，初始化echarts实例
      const chats3 = echarts.init(document.getElementById('chats-3'))
      // 绘制图表
      chats3.setOption({
        title: {
          text: '历月合同签订数量',
          left: 'left',
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow',
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
            data: [
              '一月',
              '二月',
              '三月',
              '四月',
              '五月',
              '六月',
              '七月',
              '八月',
              '九月',
              '十月',
              '十一月',
              '十二月',
            ],
            axisTick: {
              alignWithLabel: true,
            },
          },
        ],
        yAxis: [
          {
            type: 'value',
          },
        ],
        series: [
          {
            name: '合同数量',
            type: 'bar',
            barWidth: '60%',
            data: [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
          },
        ],
      })

      const chats4 = echarts.init(document.getElementById('chats-4'))

      // 指定图表的配置项和数据
      chats4.setOption({
        title: {
          text: '最近12个月收款情况',
          subtext: '单位：万元',
        },
        tooltip: {
          trigger: 'axis',
        },
        legend: {
          data: ['本年应收款金额', '本年实际收款金额'],
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
          data: [
            '2020-12',
            '2021-01',
            '2021-02',
            '2021-03',
            '2021-04',
            '2021-05',
            '2021-06',
            '2021-07',
            '2021-08',
            '2021-09',
            '2021-10',
            '2021-11',
          ],
        },
        yAxis: {
          type: 'value',
        },
        series: [
          {
            name: '本年应收款金额',
            type: 'line',
            stack: 'Total',
            data: [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
          },
          {
            name: '本年实际收款金额',
            type: 'line',
            stack: 'Total',
            data: [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
          },
        ],
      })

      const chats5 = echarts.init(document.getElementById('chats-5'))

      // 指定图表的配置项和数据
      chats5.setOption({
        title: {
          text: '最近12个月付款情况',
          subtext: '单位：万元',
        },
        tooltip: {
          trigger: 'axis',
        },
        legend: {
          data: ['本年应付款金额', '本年实际付款金额'],
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
          data: [
            '2020-12',
            '2021-01',
            '2021-02',
            '2021-03',
            '2021-04',
            '2021-05',
            '2021-06',
            '2021-07',
            '2021-08',
            '2021-09',
            '2021-10',
            '2021-11',
          ],
        },
        yAxis: {
          type: 'value',
        },
        series: [
          {
            name: '本年应付款金额',
            type: 'line',
            stack: 'Total',
            data: [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
          },
          {
            name: '本年实际付款金额',
            type: 'line',
            stack: 'Total',
            data: [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
          },
        ],
      })

      const chats6 = echarts.init(document.getElementById('chats-6'))
      // 指定图表的配置项和数据
      chats6.setOption({
        title: {
          text: '收付款分析',
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
        // Declare several bar series, each will be mapped
        // to a column of dataset.source by default.
        series: [{ type: 'bar' }, { type: 'bar' }],
      })

      const chats7 = echarts.init(document.getElementById('chats-7'))

      // 指定图表的配置项和数据
      chats7.setOption({
        title: {
          text: '合同执行状态报表',
          left: 'left',
        },
        tooltip: {
          trigger: 'item',
        },
        legend: {
          top: '15%',
          left: 'center',
        },
        series: [
          {
            name: '合同数量',
            type: 'pie',
            top: '15%',
            radius: ['20%', '50%'],
            data: [
              { value: 0, name: '合同终止' },
              { value: 0, name: '合同分析' },
              { value: 0, name: '合同履行' },
              { value: 0, name: '合同暂停' },
              { value: 0, name: '合同解除' },
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
      })
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
    justify-content: space-between;
    background: white;
    padding: 10px;
    margin-bottom: 20px;
  }
  .one-item i {
    font-size: 24px;
  }
  .chats > div > div {
    background: white;
    padding: 10px;
    margin-bottom: 20px;
  }
</style>
