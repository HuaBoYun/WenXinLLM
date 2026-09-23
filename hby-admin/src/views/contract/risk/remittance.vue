<template>
  <!--  <div id="abc" class="system-log-container"></div>-->
  <div id="app" class="page">
    <div class="table-responsive">
      <h3>年度合同交付</h3>
      <span>
        <label class="form-check-label" for="flexCheckDefault">全部</label>
        <input
          class="form-check-input margin-right-sm"
          type="checkbox"
          value=""
        />
      </span>
      <span v-for="(item, index) in years" :key="index">
        <label class="form-check-label" for="flexCheckDefault">
          {{ item }}
        </label>
        <input
          id="flexCheckDefault"
          class="form-check-input margin-right-sm"
          type="checkbox"
          value=""
        />
      </span>
    </div>
    <el-row :gutter="10">
      <el-col :lg="6" :md="6" :sm="12">
        <ul class="list-group zb-con">
          <li class="list-group-item"><h3>关 键 指 标</h3></li>
          <li class="list-group-item">
            <el-row :gutter="10">
              <el-col :lg="16" :md="16" :sm="12">
                <p>0 万元</p>
                <p>回款合同金额</p>
              </el-col>
              <el-col :lg="8" :md="8" :sm="12">
                <img src="../../../assets/img1.png" />
              </el-col>
            </el-row>
          </li>
          <li class="list-group-item">
            <el-row :gutter="10">
              <el-col :lg="16" :md="16" :sm="12">
                <p>0 万元</p>
                <p>回款合同金额</p>
              </el-col>
              <el-col :lg="8" :md="8" :sm="12">
                <img src="../../../assets/img2.png" />
              </el-col>
            </el-row>
          </li>
          <li class="list-group-item">
            <el-row :gutter="10">
              <el-col :lg="16" :md="16" :sm="12">
                <p>0 万元</p>
                <p>回款合同金额</p>
              </el-col>
              <el-col :lg="8" :md="8" :sm="12">
                <img src="../../../assets/img3.png" />
              </el-col>
            </el-row>
          </li>
          <li class="list-group-item">
            <el-row :gutter="10">
              <el-col :lg="16" :md="16" :sm="12">
                <p>0 万元</p>
                <p>回款合同金额</p>
              </el-col>
              <el-col :lg="8" :md="8" :sm="12">
                <img src="../../../assets/img4.png" />
              </el-col>
            </el-row>
          </li>
        </ul>
      </el-col>
      <el-col :lg="12" :md="12" :sm="12">
        <china-map />
      </el-col>
      <el-col :lg="6" :md="6" :sm="12">
        <div id="chats-1" style="width: 100%; height: 360px"></div>
      </el-col>
    </el-row>
    <div class="table-responsive">
      <h3>年度合同交付</h3>
      <span>
        <label class="form-check-label" for="flexCheckDefault">全部</label>
        <input
          id="flexCheckDefault"
          class="form-check-input margin-right-sm"
          type="checkbox"
          value=""
        />
      </span>
      <span v-for="(item, index) in years" :key="index">
        <label class="form-check-label" for="flexCheckDefault">
          {{ item }}
        </label>
        <input
          id="flexCheckDefault"
          class="form-check-input margin-right-sm"
          type="checkbox"
          value=""
        />
      </span>
    </div>
    <el-row :gutter="10">
      <el-col :lg="8" :md="8" :sm="12">
        <div id="chats-2" style="width: 100%; height: 300px"></div>
      </el-col>
      <el-col :lg="8" :md="8" :sm="12">
        <div id="chats-3" style="width: 100%; height: 300px"></div>
      </el-col>
      <el-col :lg="8" :md="8" :sm="12">
        <div id="chats-4" style="width: 100%; height: 300px"></div>
      </el-col>
      <el-col :lg="8" :md="8" :sm="12">
        <div id="chats-5" style="width: 100%; height: 300px"></div>
      </el-col>
      <el-col :lg="8" :md="8" :sm="12">
        <div id="chats-6" style="width: 100%; height: 300px"></div>
      </el-col>
      <el-col :lg="8" :md="8" :sm="12">
        <div id="chats-7" style="width: 100%; height: 300px"></div>
      </el-col>
    </el-row>
    <div class="table-responsive">
      <h3>季度合同交付</h3>
      <span>
        <label class="form-check-label" for="flexCheckDefault">全部</label>
        <input
          id="flexCheckDefault"
          class="form-check-input margin-right-sm"
          type="checkbox"
          value=""
        />
      </span>
      <span v-for="(item, index) in years" :key="index">
        <label class="form-check-label" for="flexCheckDefault">
          {{ item }}
        </label>
        <input
          id="flexCheckDefault"
          class="form-check-input margin-right-sm"
          type="checkbox"
          value=""
        />
      </span>
    </div>
    <el-row :gutter="10">
      <el-col :lg="8" :md="8" :sm="12">
        <div id="chats-2-1" style="width: 100%; height: 300px"></div>
      </el-col>
      <el-col :lg="8" :md="8" :sm="12">
        <div id="chats-3-1" style="width: 100%; height: 300px"></div>
      </el-col>
      <el-col :lg="8" :md="8" :sm="12">
        <div id="chats-4-1" style="width: 100%; height: 300px"></div>
      </el-col>
      <el-col :lg="8" :md="8" :sm="12">
        <div id="chats-5-1" style="width: 100%; height: 300px"></div>
      </el-col>
      <el-col :lg="8" :md="8" :sm="12">
        <div id="chats-6-1" style="width: 100%; height: 300px"></div>
      </el-col>
      <el-col :lg="8" :md="8" :sm="12">
        <div id="chats-7-1" style="width: 100%; height: 300px"></div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
  import * as echarts from 'echarts'
  import ChinaMap from './components/ChinaMap'

  export default {
    name: 'Download',
    components: {
      ChinaMap,
    },
    data() {
      return {
        activeYear: 2021,
        years: [2012, 2013, 2014, 2015, 2016, 2017, 2018, 2019, 2020, 2021],
      }
    },
    mounted() {
      // 基于准备好的dom，初始化echarts实例
      const chats1 = echarts.init(document.getElementById('chats-1'))
      // 绘制图表
      chats1.setOption({
        title: {
          text: '各类合同类型回款合同金额',
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
          data: [
            'Brazil',
            'Indonesia',
            'USA',
            'India',
            'China',
            'World',
            'Brazil',
            'Indonesia1',
            'USA1',
            'India1',
            'China1',
            'World1',
          ],
        },
        series: [
          {
            name: '2011',
            type: 'bar',
            barWidth: 10,
            data: [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
          },
        ],
      })

      const chats2 = echarts.init(document.getElementById('chats-2'))
      // 指定图表的配置项和数据
      chats2.setOption({
        title: {
          text: '各部门年度回款情况',
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
        grid: {
          left: '3%',
          right: '4%',
          bottom: '10%',
          containLabel: true,
        },
        legend: {
          left: 'center',
          bottom: 'bottom',
          data: ['Precipitation', 'Temperature'],
        },
        xAxis: [
          {
            type: 'category',
            data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'],
            axisPointer: {
              type: 'shadow',
            },
          },
        ],
        yAxis: [
          {
            type: 'value',
            name: '单位：万元/千',
            min: 0,
            max: 250,
            interval: 50,
            axisLabel: {
              formatter: '{value}',
            },
          },
          {
            type: 'value',
            name: '',
            min: 0,
            max: 25,
            interval: 5,
            axisLabel: {
              formatter: '{value}',
            },
          },
        ],
        series: [
          {
            name: 'Precipitation',
            type: 'bar',
            barWidth: 20,
            data: [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
          },
          {
            name: 'Temperature',
            type: 'line',
            yAxisIndex: 1,
            data: [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
          },
        ],
      })

      const chats3 = echarts.init(document.getElementById('chats-3'))
      // 指定图表的配置项和数据
      chats3.setOption({
        title: {
          text: '各业务类别合同情况',
          left: 'left',
        },
        tooltip: {
          trigger: 'item',
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '0%',
          containLabel: true,
        },
        series: [
          {
            name: '合同数量',
            type: 'pie',
            top: '0%',
            bottom: '0%',
            radius: '50%',
            data: [
              { value: 0, name: '原材料供应/履行中' },
              { value: 0, name: '工程建设/履行中' },
              { value: 0, name: '服务与咨询/履行中' },
              { value: 0, name: '设备与资产/履行中' },
              { value: 0, name: '设备与资产/已订立' },
              { value: 0, name: '设计与技术/履行中' },
              { value: 0, name: '原材料供应/履行中1' },
              { value: 0, name: '工程建设/履行中1' },
              { value: 0, name: '服务与咨询/履行中1' },
              { value: 0, name: '设备与资产/履行中1' },
              { value: 0, name: '设备与资产/已订立1' },
              { value: 0, name: '设计与技术/履行中1' },
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

      const chats4 = echarts.init(document.getElementById('chats-4'))
      // 指定图表的配置项和数据
      chats4.setOption({
        title: {
          text: '部门年度回款计划完成率',
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
        grid: {
          left: '3%',
          right: '4%',
          bottom: '10%',
          containLabel: true,
        },
        legend: {
          left: 'center',
          bottom: 'bottom',
          data: ['Evaporation', 'Precipitation', 'Temperature'],
        },
        xAxis: [
          {
            type: 'category',
            data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'],
            axisPointer: {
              type: 'shadow',
            },
          },
        ],
        yAxis: [
          {
            type: 'value',
            name: '单位：万元/%',
            min: 0,
            max: 250,
            interval: 50,
            axisLabel: {
              formatter: '{value}',
            },
          },
          {
            type: 'value',
            name: '',
            min: 0,
            max: 25,
            interval: 5,
            axisLabel: {
              formatter: '{value}%',
            },
          },
        ],
        series: [
          {
            name: 'Evaporation',
            type: 'bar',
            barWidth: 20,
            data: [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
          },
          {
            name: 'Precipitation',
            type: 'bar',
            barWidth: 20,
            data: [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
          },
          {
            name: 'Temperature',
            type: 'line',
            yAxisIndex: 1,
            data: [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
          },
        ],
      })

      const chats5 = echarts.init(document.getElementById('chats-5'))

      // 指定图表的配置项和数据
      chats5.setOption({
        title: {
          text: '各部门年累计回款金额及其同比',
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
        grid: {
          left: '3%',
          right: '4%',
          bottom: '10%',
          containLabel: true,
        },
        legend: {
          left: 'center',
          bottom: 'bottom',
          data: ['Precipitation', 'Temperature'],
        },
        xAxis: [
          {
            type: 'category',
            data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'],
            axisPointer: {
              type: 'shadow',
            },
          },
        ],
        yAxis: [
          {
            type: 'value',
            name: '单位：万元/千',
            min: 0,
            max: 250,
            interval: 50,
            axisLabel: {
              formatter: '{value}',
            },
          },
          {
            type: 'value',
            name: '',
            min: 0,
            max: 25,
            interval: 5,
            axisLabel: {
              formatter: '{value}',
            },
          },
        ],
        series: [
          {
            name: 'Precipitation',
            type: 'bar',
            barWidth: 20,
            data: [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
          },
          {
            name: 'Temperature',
            type: 'line',
            yAxisIndex: 1,
            data: [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
          },
        ],
      })

      const chats6 = echarts.init(document.getElementById('chats-6'))

      // 指定图表的配置项和数据
      chats6.setOption({
        title: {
          text: '各客户年累计未回款合同金额前十',
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
        grid: {
          left: '3%',
          right: '4%',
          bottom: '10%',
          containLabel: true,
        },
        legend: {
          left: 'center',
          bottom: 'bottom',
          data: ['Precipitation', 'Temperature'],
        },
        xAxis: [
          {
            type: 'category',
            data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'],
            axisPointer: {
              type: 'shadow',
            },
          },
        ],
        yAxis: [
          {
            type: 'value',
            name: '单位：万元/千',
            min: 0,
            max: 250,
            interval: 50,
            axisLabel: {
              formatter: '{value}',
            },
          },
          {
            type: 'value',
            name: '',
            min: 0,
            max: 25,
            interval: 5,
            axisLabel: {
              formatter: '{value}',
            },
          },
        ],
        series: [
          {
            name: 'Precipitation',
            type: 'bar',
            barWidth: 20,
            data: [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
          },
        ],
      })

      const chats7 = echarts.init(document.getElementById('chats-7'))

      // 指定图表的配置项和数据
      chats7.setOption({
        title: {
          text: '各合同年累计未回款金额前十',
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
        grid: {
          left: '3%',
          right: '4%',
          bottom: '10%',
          containLabel: true,
        },
        legend: {
          left: 'center',
          bottom: 'bottom',
          data: ['Precipitation', 'Temperature'],
        },
        xAxis: [
          {
            type: 'category',
            data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'],
            axisPointer: {
              type: 'shadow',
            },
          },
        ],
        yAxis: [
          {
            type: 'value',
            name: '单位：万元/千',
            min: 0,
            max: 250,
            interval: 50,
            axisLabel: {
              formatter: '{value}',
            },
          },
          {
            type: 'value',
            name: '',
            min: 0,
            max: 25,
            interval: 5,
            axisLabel: {
              formatter: '{value}',
            },
          },
        ],
        series: [
          {
            name: 'Precipitation',
            type: 'bar',
            barWidth: 20,
            data: [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
          },
        ],
      })

      const chatsJd2 = echarts.init(document.getElementById('chats-2-1'))

      // 指定图表的配置项和数据
      chatsJd2.setOption({
        title: {
          text: '各部门年度回款情况',
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
        grid: {
          left: '3%',
          right: '4%',
          bottom: '10%',
          containLabel: true,
        },
        legend: {
          left: 'center',
          bottom: 'bottom',
          data: ['Precipitation', 'Temperature'],
        },
        xAxis: [
          {
            type: 'category',
            data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'],
            axisPointer: {
              type: 'shadow',
            },
          },
        ],
        yAxis: [
          {
            type: 'value',
            name: '单位：万元/千',
            min: 0,
            max: 250,
            interval: 50,
            axisLabel: {
              formatter: '{value}',
            },
          },
          {
            type: 'value',
            name: '',
            min: 0,
            max: 25,
            interval: 5,
            axisLabel: {
              formatter: '{value}',
            },
          },
        ],
        series: [
          {
            name: 'Precipitation',
            type: 'bar',
            barWidth: 20,
            data: [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
          },
          {
            name: 'Temperature',
            type: 'line',
            yAxisIndex: 1,
            data: [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
          },
        ],
      })

      const chatsJd3 = echarts.init(document.getElementById('chats-3-1'))

      // 指定图表的配置项和数据
      chatsJd3.setOption({
        title: {
          text: '各业务类别合同情况',
          left: 'left',
        },
        tooltip: {
          trigger: 'item',
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '0%',
          containLabel: true,
        },
        series: [
          {
            name: '合同数量',
            type: 'pie',
            top: '0%',
            bottom: '0%',
            radius: '50%',
            data: [
              { value: 0, name: '原材料供应/履行中' },
              { value: 0, name: '工程建设/履行中' },
              { value: 0, name: '服务与咨询/履行中' },
              { value: 0, name: '设备与资产/履行中' },
              { value: 0, name: '设备与资产/已订立' },
              { value: 0, name: '设计与技术/履行中' },
              { value: 0, name: '原材料供应/履行中1' },
              { value: 0, name: '工程建设/履行中1' },
              { value: 0, name: '服务与咨询/履行中1' },
              { value: 0, name: '设备与资产/履行中1' },
              { value: 0, name: '设备与资产/已订立1' },
              { value: 0, name: '设计与技术/履行中1' },
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

      const chatsJd4 = echarts.init(document.getElementById('chats-4-1'))

      // 指定图表的配置项和数据
      chatsJd4.setOption({
        title: {
          text: '部门年度回款计划完成率',
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
        grid: {
          left: '3%',
          right: '4%',
          bottom: '10%',
          containLabel: true,
        },
        legend: {
          left: 'center',
          bottom: 'bottom',
          data: ['Evaporation', 'Precipitation', 'Temperature'],
        },
        xAxis: [
          {
            type: 'category',
            data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'],
            axisPointer: {
              type: 'shadow',
            },
          },
        ],
        yAxis: [
          {
            type: 'value',
            name: '单位：万元/%',
            min: 0,
            max: 250,
            interval: 50,
            axisLabel: {
              formatter: '{value}',
            },
          },
          {
            type: 'value',
            name: '',
            min: 0,
            max: 25,
            interval: 5,
            axisLabel: {
              formatter: '{value}%',
            },
          },
        ],
        series: [
          {
            name: 'Evaporation',
            type: 'bar',
            barWidth: 20,
            data: [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
          },
          {
            name: 'Precipitation',
            type: 'bar',
            barWidth: 20,
            data: [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
          },
          {
            name: 'Temperature',
            type: 'line',
            yAxisIndex: 1,
            data: [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
          },
        ],
      })

      const chatsJd5 = echarts.init(document.getElementById('chats-5-1'))

      // 指定图表的配置项和数据
      chatsJd5.setOption({
        title: {
          text: '各部门年累计回款金额及其同比',
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
        grid: {
          left: '3%',
          right: '4%',
          bottom: '10%',
          containLabel: true,
        },
        legend: {
          left: 'center',
          bottom: 'bottom',
          data: ['Precipitation', 'Temperature'],
        },
        xAxis: [
          {
            type: 'category',
            data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'],
            axisPointer: {
              type: 'shadow',
            },
          },
        ],
        yAxis: [
          {
            type: 'value',
            name: '单位：万元/千',
            min: 0,
            max: 250,
            interval: 50,
            axisLabel: {
              formatter: '{value}',
            },
          },
          {
            type: 'value',
            name: '',
            min: 0,
            max: 25,
            interval: 5,
            axisLabel: {
              formatter: '{value}',
            },
          },
        ],
        series: [
          {
            name: 'Precipitation',
            type: 'bar',
            barWidth: 20,
            data: [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
          },
          {
            name: 'Temperature',
            type: 'line',
            yAxisIndex: 1,
            data: [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
          },
        ],
      })

      const chatsJd6 = echarts.init(document.getElementById('chats-6-1'))

      // 指定图表的配置项和数据
      chatsJd6.setOption({
        title: {
          text: '各客户年累计未回款合同金额前十',
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
        grid: {
          left: '3%',
          right: '4%',
          bottom: '10%',
          containLabel: true,
        },
        legend: {
          left: 'center',
          bottom: 'bottom',
          data: ['Precipitation', 'Temperature'],
        },
        xAxis: [
          {
            type: 'category',
            data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'],
            axisPointer: {
              type: 'shadow',
            },
          },
        ],
        yAxis: [
          {
            type: 'value',
            name: '单位：万元/千',
            min: 0,
            max: 250,
            interval: 50,
            axisLabel: {
              formatter: '{value}',
            },
          },
          {
            type: 'value',
            name: '',
            min: 0,
            max: 25,
            interval: 5,
            axisLabel: {
              formatter: '{value}',
            },
          },
        ],
        series: [
          {
            name: 'Precipitation',
            type: 'bar',
            barWidth: 20,
            data: [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
          },
        ],
      })

      const chatsJd7 = echarts.init(document.getElementById('chats-7-1'))

      // 指定图表的配置项和数据
      chatsJd7.setOption({
        title: {
          text: '各合同年累计未回款金额前十',
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
        grid: {
          left: '3%',
          right: '4%',
          bottom: '10%',
          containLabel: true,
        },
        legend: {
          left: 'center',
          bottom: 'bottom',
          data: ['Precipitation', 'Temperature'],
        },
        xAxis: [
          {
            type: 'category',
            data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'],
            axisPointer: {
              type: 'shadow',
            },
          },
        ],
        yAxis: [
          {
            type: 'value',
            name: '单位：万元/千',
            min: 0,
            max: 250,
            interval: 50,
            axisLabel: {
              formatter: '{value}',
            },
          },
          {
            type: 'value',
            name: '',
            min: 0,
            max: 25,
            interval: 5,
            axisLabel: {
              formatter: '{value}',
            },
          },
        ],
        series: [
          {
            name: 'Precipitation',
            type: 'bar',
            barWidth: 20,
            data: [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
          },
        ],
      })
    },
    created() {},
    methods: {
      changeActiveYear: function (year) {
        this.activeYear = year
      },
    },
  }
</script>
<style scoped>
  .el-col > div {
    border: 1px solid #dcdfe5;
    margin-bottom: 10px;
  }

  .zb-con p {
    margin-bottom: 5px;
  }

  .zb-con img {
    width: 58px;
  }

  .page {
    padding: 20px;
  }

  .table-title {
    cursor: pointer;
  }

  .table th {
    position: relative;
  }

  .table-filter {
    position: absolute;
    border: 1px solid gainsboro;
    padding: 5px;
    left: 0;
    right: 0;
    top: 40px;
    background: white;
    min-width: 160px;
  }

  .table-filter input {
    padding: 5px;
    font-size: 14px;
    margin-right: 5px;
  }

  .table-filter .form-check {
    display: flex;
    flex-direction: column;
    text-align: left;
    font-size: 14px;
    font-weight: 400;
    padding: 5px;
  }

  .table-filter button {
    font-size: 12px;
    padding: 2px 10px;
  }

  .table-responsive {
    background: white;
    padding: 20px;
    margin-bottom: 20px;
  }
  h3 {
    font-size: 20px;
    margin: 3px 0;
  }

  .chats > div > div {
    background: white;
    padding: 10px;
    margin-bottom: 20px;
  }

  .select-year {
    display: flex;
    background: aliceblue;
    padding: 5px;
  }

  .select-year > div {
    margin-right: 10px;
    padding: 2px 5px;
    cursor: pointer;
  }

  .select-year .active {
    background: #ffaf0f;
    border-radius: 20px;
    color: white;
  }
  .margin-right-sm {
    margin-right: 15px;
  }
  .list-group {
    display: flex;
    flex-direction: column;
    padding-left: 0;
    margin-bottom: 0;
    border-radius: 0.25rem;
    margin-top: 0;
  }
  .list-group-item {
    position: relative;
    display: block;
    padding: 0.5rem 1rem;
    color: #212529;
    text-decoration: none;
    background-color: #fff;
    border: 1px solid #dcdfe5;
    border-bottom: 0px;
  }
  .list-group-item p {
    margin-top: 10px;
  }
  .list-group-item:first-child {
    border-top-left-radius: inherit;
    border-top-right-radius: inherit;
  }
  .list-group-item:last-child {
    border-bottom: 1px solid #dcdfe5;
  }
</style>
