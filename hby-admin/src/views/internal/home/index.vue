<template>
  <div class="internal-home-container">
    <!-- 切换模块默认进入驾驶舱，不挂载消息/流程组件；进入消息中心时再调用流程列表接口。 -->
    <!-- <div>
      <xiafa />
    </div> -->
    <div>
      <pubNode />
    </div>

    <!-- 页面头部 -->
    <div class="page-header" :style="{ backgroundColor: '#ffffff' }">
      <h2 class="header-title">内控管理驾驶舱</h2>
      <el-button
        type="primary"
        icon="el-icon-full-screen"
        @click="openFullScreen"
        class="screen-btn"
      >
        大屏
      </el-button>
    </div>

    <!-- 公司评价项目数图表 - 放在标头下方 -->
    <el-card class="chart-card" style="margin-bottom: 20px">
      <div id="chats-222" class="chart-container"></div>
    </el-card>

    <!-- 查询条件卡片 -->
    <el-card class="search-card">
      <el-form ref="form" :inline="true" label-width="0" :model="queryForm">
        <el-form-item>
          <el-select
            v-model="queryForm.orgId"
            style="width: 260px"
            placeholder="请选择公司"
            clearable
            @change="queryChange"
          >
            <el-option
              v-for="(item, index) in companyList"
              :key="index"
              :label="item.orgname"
              :value="item.orgid"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-date-picker
            v-model="queryForm.year"
            type="year"
            @change="queryChange"
            value-format="yyyy"
            placeholder="选择年"
          ></el-date-picker>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 主要内容区域 -->
    <div class="main-content">
      <!-- 第一行：项目成熟度 + 年度缺陷项目趋势分析 -->
      <el-row :gutter="20">
        <el-col :lg="8" :md="8" :sm="24">
          <el-card class="chart-card">
            <div id="chats-111" class="chart-container"></div>
          </el-card>
        </el-col>
        <el-col :lg="16" :md="16" :sm="24">
          <el-card class="chart-card">
            <div id="chats-888" class="chart-container"></div>
          </el-card>
        </el-col>
      </el-row>

      <!-- 第二行：各单位缺陷数量 + 评价结果表 -->
      <el-row :gutter="20">
        <el-col :lg="12" :md="12" :sm="24">
          <el-card class="chart-card chart-card-tall">
            <div
              id="chats-333"
              class="chart-container-tall"
              style="height: 650px"
            ></div>
          </el-card>
        </el-col>
        <el-col :lg="12" :md="12" :sm="24">
          <el-card class="table-card">
            <div class="table-wrapper">
              <el-table
                border
                :data="tableData"
                style="width: 100%"
                height="510"
                :max-height="510"
                :cell-style="cellStyle"
              >
                <!-- 固定表头 -->
                <el-table-column label="评价项目编号" prop="assessid" />
                <el-table-column label="评价项目名称" prop="assessname" />
                <el-table-column label="评价模板" prop="templename" />
                <el-table-column label="总分" prop="finalscorenew">
                  <template slot-scope="scope">
                    {{ parseFloat(scope.row.finalscorenew || 0).toFixed(2) }}
                  </template>
                </el-table-column>
              </el-table>
            </div>
            <div class="pagination-container">
              <el-pagination
                small
                background
                layout="prev, pager, next"
                :current-page="currentPage"
                :page-size="pageSize"
                :total="total"
                @current-change="handlePageChange"
              />
            </div>
          </el-card>
        </el-col>
      </el-row>

      <!-- 第三行：各部门缺陷 + 缺陷分布 + 缺陷等级 -->
      <el-row :gutter="20">
        <el-col :lg="8" :md="8" :sm="24">
          <el-card class="chart-card">
            <div id="chats-555" class="chart-container"></div>
          </el-card>
        </el-col>
        <el-col :lg="8" :md="8" :sm="24">
          <el-card class="chart-card">
            <div id="chats-666" class="chart-container"></div>
          </el-card>
        </el-col>
        <el-col :lg="8" :md="8" :sm="24">
          <el-card class="chart-card">
            <div id="chats-777" class="chart-container"></div>
          </el-card>
        </el-col>
      </el-row>

      <!-- 第四行：本年缺陷项目趋势分析 -->
      <el-row :gutter="20">
        <el-col :lg="24" :md="24" :sm="24">
          <el-card class="chart-card">
            <div id="chats-444" class="chart-container"></div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 全屏大屏容器 -->
    <div
      v-if="screenVisible"
      ref="fullscreenContainer"
      class="fullscreen-container"
    >
      <screen-display :screen-data="screenData" @close="closeFullScreen" />
    </div>
  </div>
</template>

<script>
  import * as echarts from 'echarts'
  import {
    getAssessCompanyList,
    getProjectMaturityAnalysis,
    getDefectQuantityIssues,
    getCompanyProjectEvaluations,
    getDefectQuantityIssuesByDep,
    getDefectProjectsYearAnalysis,
    getEvaluationResultsList,
    getDistributionDefectTypes,
    oneLevelProcess,
    defectGrade,
  } from '@/api/internal/home'
  // import xiafa from '@/views/homes/personal/xiafa-new.vue'
  import pubNode from './pubNode.vue'
  import ScreenDisplay from './components/ScreenDisplay.vue'

  export default {
    name: 'Download',
    components: {
      // xiafa,
      pubNode,
      ScreenDisplay,
    },
    data() {
      return {
        tableData: [], // 表格数据
        queryForm: {
          orgId: '',
          year: '',
        },
        companyList: [],

        currentPage: 1, // 当前页码
        pageSize: 10, // 每页显示的条数
        total: 100, // 总条数
        screenVisible: false, // 大屏显示状态
        screenData: {}, // 传递给大屏的数据
        themeColor: '#409eff', // 动态主题色
        themeColors: {
          'vab-theme-default': '#1890ff',
          'vab-theme-ocean': '#399efd',
          'vab-theme-green': '#41b584',
          'vab-theme-white': '#1890ff',
          'vab-theme-red': '#e50113',
        },
      }
    },
    computed: {
      // 获取当前主题配置
      theme() {
        return this.$store.getters['settings/theme']
      },
    },
    async mounted() {
      this.queryForm.year = String(new Date().getFullYear())
      this.initThemeColor()
      this.observeThemeChange()
      this.getData()
      this.queryChange()
    },
    created() {
      // this.loadTableData()
    },
    beforeDestroy() {
      // 组件销毁时断开主题监听
      if (this.themeObserver) {
        this.themeObserver.disconnect()
      }
    },
    methods: {
      // 初始化主题颜色
      initThemeColor() {
        const body = document.body

        // 根据body的class获取主题色
        for (const themeClass in this.themeColors) {
          if (body.classList.contains(themeClass)) {
            this.themeColor = this.themeColors[themeClass]
            break
          }
        }

        // 更新所有图表
        this.$nextTick(() => {
          this.queryChange()
        })
      },
      // 监听主题变化
      observeThemeChange() {
        const targetNode = document.body
        const config = { attributes: true, attributeFilter: ['class'] }

        const observer = new MutationObserver((mutations) => {
          mutations.forEach((mutation) => {
            if (
              mutation.type === 'attributes' &&
              mutation.attributeName === 'class'
            ) {
              this.initThemeColor()
            }
          })
        })

        observer.observe(targetNode, config)
        this.themeObserver = observer
      },
      queryChange() {
        this.getList1()
        this.getList2()
        this.getList3()
        this.getList4()
        this.getList5()
        this.getList6()
        this.getList7()
        this.getList8()
        this.getTable()
      },
      async getData() {
        const { data, code, msg } = await getAssessCompanyList()
        console.log(data, code, msg)
        this.companyList = data.data
      },
      // 加载表格数据
      getTable() {
        getEvaluationResultsList({
          ...this.queryForm,
          pageNo: this.currentPage,
          pageSize: this.pageSize,
        }).then((res) => {
          console.log('table', res)
          this.tableData = res.data.pageInfo.list
          this.total = res.data.pageInfo.total
        })
      },
      loadTableData() {
        // 模拟从后端获取数据
        const start = (this.currentPage - 1) * this.pageSize
        const end = start + this.pageSize
        this.tableData = this.generateMockData().slice(start, end)
      },
      // 生成模拟数据
      generateMockData() {
        const data = []
        for (let i = 1; i <= this.total; i++) {
          data.push({
            date: i,
            name: `公司${i}`,
            address: Math.floor(Math.random() * 1000), // 随机生成项目数量
            record: Math.floor(Math.random() * 1000), // 随机生成项目数量
          })
        }
        return data
      },
      // 处理分页变化
      handlePageChange(page) {
        console.log(page)
        this.currentPage = page
        this.getTable()
      },
      // 表头样式
      headerCellStyle() {
        return {
          fontSize: '14px', // 表头字体大小
          fontWeight: 'bold', // 表头字体加粗
          color: '#333', // 表头字体颜色
        }
      },
      // 单元格样式

      cellStyle() {
        return {
          fontSize: '14px', // 内容字体大小
          color: '#666', // 内容字体颜色
        }
      },
      getList1() {
        getProjectMaturityAnalysis({ ...this.queryForm }).then((res) => {
          console.log('res1', res)

          let defectGrade = []
          res.data.data.map((item) => {
            defectGrade.push({
              name: item.NAME,
              value: item.VALUE,
            })
          })

          const chats1 = echarts.init(document.getElementById('chats-111'))

          // 指定图表的配置项和数据
          chats1.setOption({
            title: {
              text: '项目成熟度占比分析',
              left: 'center',
              top: '5%',
              textStyle: {
                fontSize: 16,
                fontWeight: 'bold',
                color: this.themeColor,
              },
            },
            color: [
              this.themeColor,
              '#67c23a',
              '#e6a23c',
              '#f56c6c',
              '#909399',
              '#00d4ff',
              '#85ce61',
              '#f78989',
              '#a0cfff',
              '#b3d8ff',
            ],
            tooltip: {
              trigger: 'item',
              backgroundColor: 'rgba(255, 255, 255, 0.9)',
              borderColor: this.themeColor,
              borderWidth: 1,
              textStyle: {
                color: '#333',
              },
            },
            grid: {
              left: '3%',
              right: '4%',
              bottom: '0%',
              containLabel: true,
            },
            legend: {
              top: '15%',
              left: 'center',
              type: 'scroll', // 设置 legend 为滚动类型
              orient: 'horizontal', // 水平排列
              pageIconColor: this.themeColor, // 分页按钮颜色
              pageTextStyle: {
                color: '#606266',
              },
              textStyle: {
                color: '#606266',
              },
            },
            series: [
              {
                name: '类型',
                type: 'pie',
                top: '20%',
                radius: '50%',
                data: defectGrade,
                label: {
                  color: '#606266',
                },
                // data: [
                //   { value: 1048, name: '原材料供应/履行中' },
                //   { value: 735, name: '工程建设/履行中' },
                //   { value: 580, name: '服务与咨询/履行中' },
                //   { value: 484, name: '设备与资产/履行中' },
                //   { value: 300, name: '设备与资产/已订立' },
                //   { value: 600, name: '设计与技术/履行中' },
                //   { value: 1048, name: '原材料供应/履行中1' },
                //   { value: 735, name: '工程建设/履行中1' },
                //   { value: 580, name: '服务与咨询/履行中1' },
                //   { value: 484, name: '设备与资产/履行中1' },
                //   { value: 300, name: '设备与资产/已订立1' },
                //   { value: 600, name: '设计与技术/履行中1' },
                // ],
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
        })
      },
      getList2() {
        getCompanyProjectEvaluations().then((res) => {
          console.log('res3', res)
          const chats2 = echarts.init(document.getElementById('chats-222'))
          // 指定图表的配置项和数据
          chats2.setOption({
            title: {
              text: '公司评价项目数',
              left: 'left',
              top: '5%',
              textStyle: {
                fontSize: 16,
                fontWeight: 'bold',
                color: this.themeColor,
              },
            },
            color: [this.themeColor],
            tooltip: {
              trigger: 'axis',
              backgroundColor: 'rgba(255, 255, 255, 0.9)',
              borderColor: this.themeColor,
              borderWidth: 1,
              textStyle: {
                color: '#333',
              },
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
              textStyle: {
                color: '#606266',
              },
            },
            xAxis: [
              {
                type: 'category',
                data: res.data.nameList,
                // data: [
                //   '长江汽车股份',
                //   '长江石油化工股份有限公司',
                //   '长江保险有限公司',
                //   '长江集团有限公司',
                //   '长江经济贸易有限公司',
                //   '长江重工有限公司',
                //   '长脚制药有限公司',
                //   '长江科技股份有限公司', // 新增
                //   '长江物流有限公司', // 新增
                //   '长江能源有限公司', // 新增
                // ],
                axisPointer: {
                  type: 'shadow',
                },
                axisLabel: {
                  rotate: 30, // 旋转30度
                  interval: 0, // 强制显示所有标签
                },
              },
            ],
            yAxis: [
              {
                type: 'value',
                min: 0,
                axisLabel: {
                  formatter: '{value}',
                },
                axisLine: {
                  show: true,
                },
              },
            ],
            series: [
              {
                name: '项目数', // 将series的name设置为与title相同
                type: 'bar',
                barWidth: 20,
                data: res.data.valueList, // 扩展为10个值
                // data: [260, 590, 900, 2640, 2870, 7070, 17560, 3000, 4500, 6000], // 扩展为10个值
                itemStyle: {
                  color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                    { offset: 0, color: this.themeColor },
                    { offset: 1, color: this.themeColor + '80' },
                  ]),
                },
                label: {
                  show: true, // 显示柱状图上方的数量
                  position: 'top', // 数量显示在柱状图的顶部
                  formatter: '{c}', // 显示具体的数值
                },
              },
            ],
          })
        })
      },
      getList3() {
        getDefectQuantityIssues().then((res) => {
          console.log('res3', res)
          // 基于准备好的dom，初始化echarts实例
          const chats3 = echarts.init(document.getElementById('chats-333'))
          // 绘制图表
          chats3.setOption({
            title: {
              text: '各单位缺陷数量对比分析',
              left: 'left',
              top: '3%',
              textStyle: {
                fontSize: 16,
                fontWeight: 'bold',
                color: this.themeColor,
              },
            },
            color: [this.themeColor],
            tooltip: {
              trigger: 'axis',
              backgroundColor: 'rgba(255, 255, 255, 0.9)',
              borderColor: this.themeColor,
              borderWidth: 1,
              textStyle: {
                color: '#333',
              },
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
            },
            xAxis: [
              {
                type: 'category',
                data: res.data.nameList,
                // [
                //   '长江汽车股份',
                //   '长江石油化工股份有限公司',
                //   '长江保险有限公司',
                //   '长江集团有限公司',
                //   '长江经济贸易有限公司',
                //   '长江重工有限公司',
                //   '长脚制药有限公司',
                //   '长江科技股份有限公司', // 新增
                //   '长江物流有限公司', // 新增
                //   '长江能源有限公司', // 新增
                // ],
                axisPointer: {
                  type: 'shadow',
                },
                axisLabel: {
                  rotate: 30, // 旋转30度
                  interval: 0, // 强制显示所有标签
                },
              },
            ],
            yAxis: [
              {
                type: 'value',
                min: 0,
                axisLabel: {
                  formatter: '{value}',
                },
                axisLine: {
                  show: true,
                },
              },
            ],
            series: [
              {
                name: '项目数', // 将series的name设置为与title相同
                type: 'bar',
                barWidth: 20,
                data: res.data.valueList, // 扩展为10个值
                // data: [260, 590, 900, 2640, 2870, 7070, 17560, 3000, 4500, 6000], // 扩展为10个值
                itemStyle: {
                  color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                    { offset: 0, color: this.themeColor },
                    { offset: 1, color: this.themeColor + '80' },
                  ]),
                },
                label: {
                  show: true, // 显示柱状图上方的数量
                  position: 'top', // 数量显示在柱状图的顶部
                  formatter: '{c}', // 显示具体的数值
                },
              },
            ],
          })
        })
      },
      getList4() {
        getDefectProjectsYearAnalysis({ ...this.queryForm }).then((res) => {
          console.log('res4', res)
          const chats4 = echarts.init(document.getElementById('chats-444'))

          let maxValue = Math.max(...res.data.yList) + 3

          // 指定图表的配置项和数据
          chats4.setOption({
            title: {
              text: '本年缺陷项目趋势分析',
              left: 'left',
              top: '5%',
              textStyle: {
                fontSize: 16,
                fontWeight: 'bold',
                color: this.themeColor,
              },
            },
            color: [this.themeColor],
            tooltip: {
              trigger: 'axis',
              backgroundColor: 'rgba(255, 255, 255, 0.9)',
              borderColor: this.themeColor,
              borderWidth: 1,
              textStyle: {
                color: '#333',
              },
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
              bottom: '0%',
              containLabel: true,
            },
            legend: {
              top: '15%',
              left: 'center',
              data: ['缺陷项目数'],
              textStyle: {
                color: '#606266',
              },
            },
            xAxis: [
              {
                type: 'category',
                data: res.data.xList,
                // data: [
                //   '2023-01',
                //   '2023-02',
                //   '2023-03',
                //   '2023-04',
                //   '2023-05',
                //   '2023-06',
                //   '2023-07',
                //   '2023-08',
                //   '2023-09',
                //   '2023-10',
                //   '2023-11',
                //   '2023-12',
                // ], // 改为年月格式
                axisPointer: {
                  type: 'shadow',
                },
              },
            ],
            yAxis: [
              {
                type: 'value',
                name: '',
                min: 0,
                // max: 250,
                interval: 1,
                axisLabel: {
                  formatter: '{value}',
                },
              },
              {
                type: 'value',
                name: '',
                min: 0,
                max: maxValue,
                interval: 1,
                axisLabel: {
                  formatter: '{value}',
                },
              },
            ],
            series: [
              {
                name: '缺陷项目数',
                type: 'line',
                yAxisIndex: 1,
                smooth: true,
                data: res.data.yList,
                // data: [
                //   2.0, 2.2, 3.3, 4.5, 6.3, 10.2, 20.3, 23.4, 23.0, 16.5, 12.0, 6.2,
                // ],
                itemStyle: {
                  color: this.themeColor,
                },
                lineStyle: {
                  color: this.themeColor,
                },
                areaStyle: {
                  color: {
                    type: 'linear',
                    x: 0,
                    y: 0,
                    x2: 0,
                    y2: 1,
                    colorStops: [
                      { offset: 0, color: this.themeColor + '60' }, // 渐变起始颜色
                      { offset: 1, color: this.themeColor + '10' }, // 渐变结束颜色
                    ],
                  },
                },
              },
            ],
          })
        })
      },
      getList5() {
        getDefectQuantityIssuesByDep({ ...this.queryForm }).then((res) => {
          console.log('res5', res)
          const chats5 = echarts.init(document.getElementById('chats-555'))

          // 指定图表的配置项和数据
          chats5.setOption({
            title: {
              text: '各部门缺陷数量',
              left: 'left',
              top: '5%',
              textStyle: {
                fontSize: 16,
                fontWeight: 'bold',
                color: this.themeColor,
              },
            },
            color: [this.themeColor],
            tooltip: {
              trigger: 'axis',
              backgroundColor: 'rgba(255, 255, 255, 0.9)',
              borderColor: this.themeColor,
              borderWidth: 1,
              textStyle: {
                color: '#333',
              },
              axisPointer: {
                type: 'shadow',
              },
            },
            grid: {
              left: '10%',
              right: '4%',
              bottom: '3%',
              containLabel: true,
            },
            xAxis: [
              {
                type: 'category',
                data: res.data.nameList,
                axisTick: {
                  alignWithLabel: true,
                },
                axisPointer: {
                  type: 'shadow',
                },
                axisLabel: {
                  rotate: 45, // 旋转30度
                  interval: 0, // 强制显示所有标签
                },
              },
            ],
            yAxis: [
              {
                type: 'value',
                interval: 1,
              },
            ],
            series: [
              {
                name: '缺陷数量',
                type: 'bar',
                barWidth: '60%',
                data: res.data.valueList,
                itemStyle: {
                  color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                    { offset: 0, color: this.themeColor },
                    { offset: 1, color: this.themeColor + '80' },
                  ]),
                },
              },
            ],
          })
        })
      },
      getList6() {
        oneLevelProcess({ ...this.queryForm }).then((res) => {
          console.log('res6', res)

          let defectGrade = []
          res.data.map((item) => {
            defectGrade.push({
              name: item.oneprocess,
              value: item.num,
            })
          })

          const chats6 = echarts.init(document.getElementById('chats-666'))
          // 指定图表的配置项和数据
          chats6.setOption({
            title: {
              text: '内部控制缺陷分布',
              left: 'center',
              top: '5%',
              textStyle: {
                fontSize: 16,
                fontWeight: 'bold',
                color: this.themeColor,
              },
            },
            color: [
              this.themeColor,
              '#67c23a',
              '#e6a23c',
              '#f56c6c',
              '#909399',
              '#00d4ff',
              '#85ce61',
              '#f78989',
              '#a0cfff',
              '#b3d8ff',
            ],
            tooltip: {
              trigger: 'item',
              backgroundColor: 'rgba(255, 255, 255, 0.9)',
              borderColor: this.themeColor,
              borderWidth: 1,
              textStyle: {
                color: '#333',
              },
            },
            grid: {
              left: '3%',
              right: '4%',
              bottom: '0%',
              containLabel: true,
            },
            legend: {
              top: '15%',
              left: 'center',
              type: 'scroll', // 设置 legend 为滚动类型
              orient: 'horizontal', // 水平排列
              pageIconColor: this.themeColor, // 分页按钮颜色
              pageTextStyle: {
                color: '#606266',
              },
              textStyle: {
                color: '#606266',
              },
            },
            series: [
              {
                name: '缺陷数量',
                type: 'pie',
                top: '20%',
                radius: ['20%', '60%'],
                data: defectGrade,
                // data: [
                //   { value: 1048, name: '原材料供应/履行中' },
                //   { value: 735, name: '工程建设/履行中' },
                //   { value: 580, name: '服务与咨询/履行中' },
                //   { value: 484, name: '设备与资产/履行中' },
                //   { value: 300, name: '设备与资产/已订立' },
                //   { value: 600, name: '设计与技术/履行中' },
                //   { value: 1048, name: '原材料供应/履行中1' },
                //   { value: 735, name: '工程建设/履行中1' },
                //   { value: 580, name: '服务与咨询/履行中1' },
                //   { value: 484, name: '设备与资产/履行中1' },
                //   { value: 300, name: '设备与资产/已订立1' },
                //   { value: 600, name: '设计与技术/履行中1' },
                // ],
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
        })
      },
      getList7() {
        defectGrade({ ...this.queryForm }).then((res) => {
          console.log('res7', res)

          let defectGrade = []
          res.data.nameList.map((item, i) => {
            defectGrade.push({
              name: item,
              value: res.data.valueList[i],
            })
          })
          const chats7 = echarts.init(document.getElementById('chats-777'))

          // 指定图表的配置项和数据
          chats7.setOption({
            title: {
              text: '内部控制缺陷等级统计',
              left: 'center',
              top: '5%',
              textStyle: {
                fontSize: 16,
                fontWeight: 'bold',
                color: this.themeColor,
              },
            },
            color: [
              this.themeColor,
              '#67c23a',
              '#e6a23c',
              '#f56c6c',
              '#909399',
              '#00d4ff',
              '#85ce61',
              '#f78989',
              '#a0cfff',
              '#b3d8ff',
            ],
            tooltip: {
              trigger: 'item',
              backgroundColor: 'rgba(255, 255, 255, 0.9)',
              borderColor: this.themeColor,
              borderWidth: 1,
              textStyle: {
                color: '#333',
              },
            },
            grid: {
              left: '3%',
              right: '4%',
              bottom: '0%',
              containLabel: true,
            },
            legend: {
              top: '15%',
              left: 'center',
              type: 'scroll', // 设置 legend 为滚动类型
              orient: 'horizontal', // 水平排列
              pageIconColor: this.themeColor, // 分页按钮颜色
              pageTextStyle: {
                color: '#606266',
              },
              textStyle: {
                color: '#606266',
              },
            },
            series: [
              {
                name: '统计数量',
                type: 'pie',
                top: '20%',
                radius: '50%',
                data: defectGrade,
                // data: [
                //   { value: 1048, name: '原材料供应/履行中' },
                //   { value: 735, name: '工程建设/履行中' },
                //   { value: 580, name: '服务与咨询/履行中' },
                //   { value: 484, name: '设备与资产/履行中' },
                //   { value: 300, name: '设备与资产/已订立' },
                //   { value: 600, name: '设计与技术/履行中' },
                //   { value: 1048, name: '原材料供应/履行中1' },
                //   { value: 735, name: '工程建设/履行中1' },
                //   { value: 580, name: '服务与咨询/履行中1' },
                //   { value: 484, name: '设备与资产/履行中1' },
                //   { value: 300, name: '设备与资产/已订立1' },
                //   { value: 600, name: '设计与技术/履行中1' },
                // ],
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
        })
      },
      getList8() {
        getDistributionDefectTypes({ ...this.queryForm }).then((res) => {
          console.log('res8', res)

          let y3 = []
          res.data.yAxis.执行缺陷.map((item, i) => {
            y3.push(item * 1 + res.data.yAxis.设计缺陷[i] * 1)
          })
          const chats8 = echarts.init(document.getElementById('chats-888'))

          // 指定图表的配置项和数据
          chats8.setOption({
            title: {
              text: '年度缺陷项目趋势分析',
              left: 'left',
              top: '5%',
              textStyle: {
                fontSize: 16,
                fontWeight: 'bold',
                color: this.themeColor,
              },
            },
            color: [this.themeColor, '#67c23a', '#f56c6c'],
            tooltip: {
              trigger: 'axis',
              backgroundColor: 'rgba(255, 255, 255, 0.9)',
              borderColor: this.themeColor,
              borderWidth: 1,
              textStyle: {
                color: '#333',
              },
              axisPointer: {
                type: 'cross',
                crossStyle: {
                  color: '#999',
                },
              },
            },
            legend: {
              top: '15%',
              left: 'center',
              textStyle: {
                color: '#606266',
              },
            },
            xAxis: [
              {
                type: 'category',
                // name: '缺陷等级',
                data: res.data.xAxis,
                axisPointer: {
                  type: 'shadow',
                },
              },
            ],
            yAxis: [
              {
                type: 'value',
                name: '',
                min: 0,
                // max: 250,
                // interval: 50,
                axisLabel: {
                  formatter: '{value}',
                },
              },
              {
                type: 'value',
                name: '',
                min: 0,
                // max: 25,
                // interval: 5,
                axisLabel: {
                  // formatter: '{value} °C'
                  formatter: '{value}',
                },
              },
            ],
            series: [
              {
                name: '执行缺陷',
                type: 'bar',
                tooltip: {
                  valueFormatter: function (value) {
                    // return value + ' ml';
                    return value
                  },
                },
                data: res.data.yAxis.执行缺陷,
              },
              {
                name: '设计缺陷',
                type: 'bar',
                tooltip: {
                  valueFormatter: function (value) {
                    return value
                  },
                },
                data: res.data.yAxis.设计缺陷,
              },
              {
                name: '缺陷总和',
                type: 'line',
                yAxisIndex: 1,
                tooltip: {
                  valueFormatter: function (value) {
                    return value
                  },
                },
                data: y3,
              },
            ],
          })
        })
      },
      // 打开全屏大屏
      openFullScreen() {
        // 收集所有图表数据传递给大屏组件
        this.screenData = {
          queryForm: this.queryForm,
          companyList: this.companyList,
          tableData: this.tableData,
          total: this.total,
        }
        this.screenVisible = true

        // 使用原生全屏API
        this.$nextTick(() => {
          const container = this.$refs.fullscreenContainer
          if (container) {
            this.requestFullscreen(container)
          }
        })
      },
      // 请求全屏
      requestFullscreen(element) {
        if (element.requestFullscreen) {
          element.requestFullscreen()
        } else if (element.webkitRequestFullscreen) {
          // Safari
          element.webkitRequestFullscreen()
        } else if (element.mozRequestFullScreen) {
          // Firefox
          element.mozRequestFullScreen()
        } else if (element.msRequestFullscreen) {
          // IE11
          element.msRequestFullscreen()
        }
      },
      // 退出全屏
      exitFullscreen() {
        if (document.exitFullscreen) {
          document.exitFullscreen()
        } else if (document.webkitExitFullscreen) {
          // Safari
          document.webkitExitFullscreen()
        } else if (document.mozCancelFullScreen) {
          // Firefox
          document.mozCancelFullScreen()
        } else if (document.msExitFullscreen) {
          // IE11
          document.msExitFullscreen()
        }
      },
      // 关闭全屏大屏
      closeFullScreen() {
        this.exitFullscreen()
        this.screenVisible = false
      },
      // 处理大屏关闭事件
      handleScreenClose() {
        this.exitFullscreen()
        this.screenVisible = false
      },
    },
  }
</script>
<style lang="scss" scoped>
  .internal-home-container {
    padding: 20px;
    background: #f0f2f5;
    min-height: calc(100vh - 84px);

    .page-header {
      display: flex;
      justify-content: center;
      align-items: center;
      position: relative;
      margin-bottom: 20px;
      padding: 20px;
      border-radius: 4px;
      box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
      transition: background-color 0.3s;

      .header-title {
        margin: 0;
        font-size: 22px;
        font-weight: bold;
        color: #333;
        text-align: center;
      }

      .screen-btn {
        position: absolute;
        right: 20px;
        font-size: 14px;
      }
    }

    .search-card {
      margin-bottom: 20px;
    }

    .chart-card {
      margin-bottom: 20px;
      transition: all 0.3s;

      &:hover {
        box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
        transform: translateY(-2px);
      }

      .card-title {
        display: flex;
        align-items: center;
        gap: 8px;
        font-size: 16px;
        font-weight: bold;
        color: #303133;

        i {
          font-size: 18px;
          color: #409eff;
        }
      }

      .chart-container {
        height: 320px;
        width: 100%;
      }

      .chart-container-small {
        height: 260px;
        width: 100%;
      }

      .chart-container-tall {
        height: 590px;
        width: 100%;
      }
    }

    .chart-card-tall {
      height: 650px;
    }

    .main-content {
      .table-card {
        margin-bottom: 20px;
        height: 650px;

        .card-title {
          display: flex;
          align-items: center;
          gap: 8px;
          font-size: 16px;
          font-weight: bold;
          color: #303133;

          i {
            font-size: 18px;
            color: #409eff;
          }
        }

        .table-wrapper {
          height: 510px;
          overflow: auto;
        }

        .pagination-container {
          margin-top: 10px;
          display: flex;
          justify-content: flex-start;
        }
      }
    }
  }

  // 全屏容器样式
  .fullscreen-container {
    position: fixed;
    top: 0;
    left: 0;
    width: 100vw;
    height: 100vh;
    z-index: 9999;
    background: #0a0e27;
    overflow: hidden;
  }
</style>
