<template>
  <!--  <div id="abc" class="system-log-container"></div>-->
  <div id="app" class="page">
    <el-row :gutter="10">
      <el-col :lg="8" :md="8" :sm="24">
        <china-map />
      </el-col>
      <el-col :lg="8" :md="8" :sm="24">
        <div id="chats-3" style="width: 100%; height: 360px"></div>
      </el-col>
      <el-col :lg="8" :md="8" :sm="24">
        <div id="chats-6" style="width: 100%; height: 360px"></div>
      </el-col>
    </el-row>
    <el-row :gutter="10">
      <el-col :lg="8" :md="8" :sm="24">
        <div>
          <p>重大风险事件总项数: 100</p>
          <p>已整改重大风险事件: 52</p>
          <p>重大风险事件索赔金额 : 5,901.02 万</p>
          <div id="chats-7" style="width: 100%; height: 300px"></div>
        </div>
      </el-col>
      <el-col :lg="16" :md="16" :sm="24">
        <div class="table-responsive">
          <h5>审计执行情况</h5>
          <el-table border :data="tableData" style="width: 100%">
            <el-table-column label="日期" prop="date" width="180" />
            <el-table-column label="姓名" prop="name" width="180" />
            <el-table-column label="地址" prop="address" />
          </el-table>
          <el-pagination background layout="prev, pager, next" :total="1000" />
        </div>
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
        tableData: [
          {
            date: '2016-05-02',
            name: '王小虎',
            address: '上海市普陀区金沙江路 1518 弄',
          },
          {
            date: '2016-05-04',
            name: '王小虎',
            address: '上海市普陀区金沙江路 1517 弄',
          },
          {
            date: '2016-05-01',
            name: '王小虎',
            address: '上海市普陀区金沙江路 1519 弄',
          },
          {
            date: '2016-05-03',
            name: '王小虎',
            address: '上海市普陀区金沙江路 1516 弄',
          },
        ],
      }
    },
    mounted() {
      // 基于准备好的dom，初始化echarts实例
      const chats3 = echarts.init(document.getElementById('chats-3'))
      // 绘制图表
      chats3.setOption({
        title: {
          text: '成熟度等级项目数',
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
        legend: {
          top: '10%',
          left: 'center',
        },
        series: [
          {
            name: '合同数量',
            type: 'pie',
            top: '50%',
            radius: '50%',
            data: [
              { value: 1048, name: '原材料供应/履行中' },
              { value: 735, name: '工程建设/履行中' },
              { value: 580, name: '服务与咨询/履行中' },
              { value: 484, name: '设备与资产/履行中' },
              { value: 300, name: '设备与资产/已订立' },
              { value: 600, name: '设计与技术/履行中' },
              { value: 1048, name: '原材料供应/履行中1' },
              { value: 735, name: '工程建设/履行中1' },
              { value: 580, name: '服务与咨询/履行中1' },
              { value: 484, name: '设备与资产/履行中1' },
              { value: 300, name: '设备与资产/已订立1' },
              { value: 600, name: '设计与技术/履行中1' },
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

      const chats6 = echarts.init(document.getElementById('chats-6'))
      // 指定图表的配置项和数据
      chats6.setOption({
        title: {
          text: '成熟度等级评分',
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
        legend: {
          top: '10%',
          left: 'center',
        },
        series: [
          {
            name: '合同数量',
            type: 'pie',
            top: '50%',
            radius: '50%',
            data: [
              { value: 1048, name: '原材料供应/履行中' },
              { value: 735, name: '工程建设/履行中' },
              { value: 580, name: '服务与咨询/履行中' },
              { value: 484, name: '设备与资产/履行中' },
              { value: 300, name: '设备与资产/已订立' },
              { value: 600, name: '设计与技术/履行中' },
              { value: 1048, name: '原材料供应/履行中1' },
              { value: 735, name: '工程建设/履行中1' },
              { value: 580, name: '服务与咨询/履行中1' },
              { value: 484, name: '设备与资产/履行中1' },
              { value: 300, name: '设备与资产/已订立1' },
              { value: 600, name: '设计与技术/履行中1' },
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

      const chats7 = echarts.init(document.getElementById('chats-7'))

      // 指定图表的配置项和数据
      chats7.setOption({
        title: {
          text: '重大风险事件',
        },
        radar: {
          // shape: 'circle',
          indicator: [
            { name: 'Sales', max: 6500 },
            { name: 'Administration', max: 16000 },
            { name: 'Information Technology', max: 30000 },
            { name: 'Customer Support', max: 38000 },
            { name: 'Development', max: 52000 },
            { name: 'Marketing', max: 25000 },
          ],
        },
        series: [
          {
            name: 'Budget vs spending',
            type: 'radar',
            data: [
              {
                value: [4200, 3000, 20000, 35000, 50000, 18000],
                name: 'Allocated Budget',
              },
              {
                value: [5000, 14000, 28000, 26000, 42000, 21000],
                name: 'Actual Spending',
              },
            ],
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
</style>
