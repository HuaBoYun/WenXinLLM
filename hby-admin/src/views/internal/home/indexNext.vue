<template>
  <!--  <div id="abc" class="system-log-container"></div>-->
  <div id="app">
    <div class="content">
      <el-row :gutter="10">
        <el-col :lg="8" :md="8" :sm="24">
          <div id="chats-1" style="width: 100%; height: 360px"></div>
        </el-col>
        <el-col :lg="8" :md="16" :sm="24">
          <div id="chats-2" style="width: 100%; height: 360px"></div>
        </el-col>
        <el-col :lg="8" :md="16" :sm="24">
          <div id="chats-3" style="width: 100%; height: 360px"></div>
        </el-col>
      </el-row>
      <el-row :gutter="10">
        <el-col :lg="8" :md="8" :sm="24">
          <div id="chats-4" style="width: 100%; height: 360px"></div>
        </el-col> 
        <el-col :lg="16" :md="16" :sm="24">
          <div id="chats-5" style="width: 100%; height: 360px"></div>
        </el-col>
      </el-row>
      <!--
      <el-row :gutter="10">
        <el-col :lg="12" :md="12" :sm="24">
          <div id="chats-3" style="width: 100%; height: 200px"></div>
          <div id="chats-4" style="width: 100%; height: 200px"></div>
        </el-col>
        <el-col :lg="12" :md="12" :sm="24">
          <div class="table-responsive">
            <h5>审计项目数一览表</h5>
            <el-table border :data="tableData" style="width: 100%">
              <el-table-column label="序号" prop="date" />
              <el-table-column label="公司(风控) " prop="name" />
              <el-table-column label="项目数量" prop="address" />
            </el-table>
            <el-pagination
              background
              layout="prev, pager, next"
              :total="1000"
            />
          </div>
        </el-col>
      </el-row>

       <el-row :gutter="10">

        <el-col :lg="8" :md="8" :sm="24">
          <div id="chats-5" style="width: 100%; height: 360px"></div>
        </el-col>
        <el-col :lg="8" :md="8" :sm="24">
          <div id="chats-6" style="width: 100%; height: 360px"></div>
        </el-col>
        <el-col :lg="8" :md="8" :sm="24">
          <div id="chats-7" style="width: 100%; height: 360px"></div>
         </el-col>
      </el-row> -->
      <!-- <el-row :gutter="10">
        <el-col :lg="8" :md="8" :sm="24">
          <div id="chats-8" style="width: 100%; height: 360px"></div>
        </el-col>
        <el-col :span="16">
          <el-table :data="list" v-loading="listLoading">
            <el-table-column align="center" label="事件编号" prop="riskNumber">
              <template #default="{ row }">
                <el-button type="text" @click="handleRead(row)">
                  {{ row.riskNumber }}
                </el-button>
              </template>
            </el-table-column>
            <el-table-column
              align="center"
              label="事件名称"
              prop="riskName"
              show-overflow-tooltip
            />
            <el-table-column
              align="center"
              label="报送部门"
              prop="departmentName"
              show-overflow-tooltip
            />
            <el-table-column align="center" label="报送日期" prop="findTime" /> -->
            <!-- <el-table-column
                align="center"
                label="风险事件类别"
                prop="riskType"
                show-overflow-tooltip
              >
                <template slot-scope="{ row }">
                  <span>
                    {{ row.riskType == '1' ? '重大' : '非重大' }}
                  </span>
                </template>
            </el-table-column> -->
            <!-- <el-table-column align="center" label="审批状态" prop="state">
              <template #default="{ row }">
                {{
                  row.state == 1
                    ? '审批中'
                    : row.state == 2
                    ? '需调整'
                    : row.state == 3
                    ? '已撤销'
                    : row.state == 4
                    ? '已终止'
                    : row.state == 5
                    ? '已跟踪'
                    : row.state == 6
                    ? '已完成'
                    : '未审批'
                }}
              </template>
            </el-table-column>
          </el-table>
        </el-col> -->
        <!--   
        <el-col :lg="8" :md="8" :sm="24">
          <div id="chats-9" style="width: 100%; height: 360px"></div>
        </el-col>
        <el-col :lg="8" :md="8" :sm="24">
          <div id="chats-10" style="width: 100%; height: 360px"></div>
        </el-col>
        <el-col :lg="8" :md="8" :sm="24">
          <div id="chats-11" style="width: 100%; height: 360px"></div>
        </el-col>
        <el-col :lg="8" :md="8" :sm="24">
          <div id="chats-12" style="width: 100%; height: 360px"></div>
        </el-col> -->
      </el-row>
    </div>
    <EventEdit ref="edit" @fetch-data="fetchData" :riskcatid="riskcatid" />
  </div>
</template>

<script>
import * as echarts from 'echarts'
import first from './components/first'
import EventEdit from './components/EventEdit.vue'
import {
  getCompcnt,
  getCharts1,
  getCharts2,
  getCharts3,
  getCharts4,
  getCharts5,
} from '@/api/internal/home'
import { riskList } from '@/api/internal/new/plan'
export default {
  name: 'Download',
  components: { first, EventEdit },
  data() {
    return {
      defectGrade: [],
      list: [],
      listLoading: false,
      queryForm: {
        pageNumber: 1,
        pageSize: 5,
      },
    }
  },
  async mounted() {
    this.getChart1()
    this.getChart2()
    this.getChart3()
    this.getChart4()
    this.getChart5()
    // this.fetchData()

    // let res = await getCompcnt()
    // console.log(res)
    // this.defectGrade = [
    //   { name: '规章制度', value: res.data.institution_cnt },
    //   { name: '重要事项法审', value: res.data.matters_cnt },
    // ]
    // const chats8 = echarts.init(document.getElementById('chats-8'))
    // // 缺陷等级统计
    // chats8.setOption({
    //   title: {
    //     text: '合规审查数量',
    //     left: 'left',
    //   },
    //   tooltip: {
    //     trigger: 'item',
    //   },
    //   grid: {
    //     left: '3%',
    //     right: '4%',
    //     bottom: '0%',
    //     containLabel: true,
    //   },
    //   legend: {
    //     top: '10%',
    //     left: 'center',
    //   },
    //   series: [
    //     {
    //       name: '',
    //       type: 'pie',
    //       top: '45%',
    //       radius: '50%',
    //       data: this.defectGrade,
    //       emphasis: {
    //         itemStyle: {
    //           shadowBlur: 10,
    //           shadowOffsetX: 0,
    //           shadowColor: 'rgba(0, 0, 0, 0.5)',
    //         },
    //       },
    //     },
    //   ],
    // })
  },
  methods: {
    getChart1() {
      getCharts1().then((res) => {
        let defectGrade = []
        res.data.map((item) => {
          defectGrade.push({
            name: item.oneprocess,
            value: item.num,
          })
        })

        console.log('res', defectGrade)

        const chats1 = echarts.init(document.getElementById('chats-1'))
        // 缺陷等级统计
        chats1.setOption({
          title: {
            text: '内部控制缺陷分布',
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
              name: '',
              type: 'pie',
              top: '45%',
              radius: '50%',
              data: defectGrade,
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
    getChart2() {
      getCharts2().then((res) => {
        let defectGrade = []
        res.data.map((item) => {
          defectGrade.push({
            name: item.defecttype,
            value: item.num,
          })
        })

        const chats2 = echarts.init(document.getElementById('chats-2'))
        // 缺陷等级统计
        chats2.setOption({
          title: {
            text: '内部控制缺陷等级统计',
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
              name: '',
              type: 'pie',
              top: '45%',
              radius: '50%',
              data: defectGrade,
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
    getChart3() {
      getCharts3().then((res) => {
        let defectGrade = []
        res.data.map((item) => {
          defectGrade.push({
            name: item.status == 1 ? '创建' : item.status == 2 ? '启动' : '',
            value: item.num,
          })
        })

        const chats3 = echarts.init(document.getElementById('chats-3'))
        // 缺陷等级统计
        chats3.setOption({
          title: {
            text: '评价跟踪-状态统计',
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
              name: '',
              type: 'pie',
              top: '45%',
              radius: '50%',
              data: defectGrade,
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
    getChart4() {
      getCharts4().then((res) => {
        let defectGrade = []
        res.data.map((item) => {
          defectGrade.push({
            name: item.planstatus,
            value: item.num,
          })
        })

        const chats4 = echarts.init(document.getElementById('chats-4'))
        // 缺陷等级统计
        chats4.setOption({
          title: {
            text: '测试跟踪-状态统计',
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
              name: '',
              type: 'pie',
              top: '45%',
              radius: '50%',
              data: defectGrade,
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
    getChart5() {
      getCharts5().then((res) => {
        let XList = [],
          YList = []

        res.data.map((item) => {
          XList.push(item.testYear )
          YList.push(item.num )
        })

        // 基于准备好的dom，初始化echarts实例
        const chats5 = echarts.init(document.getElementById('chats-5'))
        // 绘制图表
        chats5.setOption({
          title: {
            text: '问题发现-年度统计',
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
              data: XList,
              axisTick: {
                alignWithLabel: true,
              },
              axisLabel: {
                color: 'rgba(0, 0, 0, 1)',
                fontSize: '0.6rem',
                interval: 0, // 设置斜切
                rotate: 1, // 设置斜切
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
              name: '数量',
              type: 'bar',
              barWidth: '60%',
              data: YList,
            },
          ],
        })
      })
    },
    async fetchData() {
      this.listLoading = true
      const {
        data: { tlist, totalPage },
      } = await riskList({
        ...this.queryForm,
      })
      this.list = tlist
      // this.total = totalPage
      this.listLoading = false
    },
    handleRead(row) {
      this.$refs['edit'].showEdit(row, 'detail')
    },
  },
}
</script>
<style scoped>
h5 {
  font-size: 18px;
  margin: 2px;
  color: #333;
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
