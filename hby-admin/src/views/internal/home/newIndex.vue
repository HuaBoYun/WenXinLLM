<template>
  <!--  <div id="abc" class="system-log-container"></div>-->
  <div id="app">
    <div class="content">
      <el-row :gutter="10">
        <el-col :span="24">
          <notice />
        </el-col>
        <!-- <el-col :span="24">
          <first />
        </el-col>
        <el-col :lg="8" :md="8" :sm="24">
          <div id="chats-8" style="width: 100%; height: 360px"></div>
        </el-col>
        <el-col :span="16">
          <div class="table-responsive">
            <h5>风险事件台账</h5>
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
              <el-table-column
                  align="center"
                  label="报送日期"
                  prop="findTime"
                />
              <el-table-column
                  align="center"
                  label="审批状态"
                  prop="state"
                >
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
          </div>
        </el-col> -->
        <el-col :span="24">
          <div class="table-responsive">
            <h5>合规报告台账</h5>
            <el-table v-loading="listLoading" :data="list1">
              <el-table-column align="center" label="序号" type="index" />
              <el-table-column
                align="center"
                label="报告名称"
                prop="reportName"
              >
                <template #default="{ row }">
                  <el-button type="text" @click="handleDetail(row)">
                    {{ row.reportName }}
                  </el-button>
                </template>
              </el-table-column>
              <el-table-column
                align="center"
                label="报告类型"
                prop="reporttType"
              >
                <template #default="{ row }">
                  {{ type[+row.reporttType] }}
                </template>
              </el-table-column>
              <el-table-column
                align="center"
                label="层级"
                prop="hierarchy"
              ></el-table-column>
              <el-table-column
                align="center"
                label="拟稿人"
                prop="draftsmanName"
              ></el-table-column>
              <el-table-column
                align="center"
                label="部门负责人"
                prop="departmentHeadName"
              ></el-table-column>
              <el-table-column
                align="center"
                label="创建人"
                prop="creatorName"
              ></el-table-column>
              <el-table-column
                align="center"
                label="创建时间"
                prop="createdTime"
              ></el-table-column>
              <el-table-column align="center" label="审批状态" prop="state">
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
          </div>
        </el-col>
        <el-col :span="24">
          <div class="table-responsive">
            <h5>合规计划台账</h5>
            <el-table v-loading="listLoading" :data="list2">
              <el-table-column align="center" label="序号" type="index" />
              <el-table-column
                align="center"
                label="计划编号"
                prop="planSerialNumber"
              >
                <template #default="{ row }">
                  <el-button type="text" @click="handleDetails(row)">
                    {{ row.planSerialNumber }}
                  </el-button>
                </template>
              </el-table-column>
              <el-table-column
                align="center"
                label="计划名称"
                prop="planName"
              ></el-table-column>
              <el-table-column
                align="center"
                label="层级"
                prop="hierarchy"
              ></el-table-column>
              <el-table-column
                align="center"
                label="编制人"
                prop="compilerName"
              ></el-table-column>
              <el-table-column
                align="center"
                label="部门负责人"
                prop="departmentHeadName"
              ></el-table-column>
              <el-table-column
                align="center"
                label="创建人"
                prop="creator"
              ></el-table-column>
              <el-table-column
                align="center"
                label="创建时间"
                prop="createdTime"
              ></el-table-column>
              <el-table-column align="center" label="审批状态" prop="state">
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
          </div>
        </el-col>
      </el-row>
    </div>
    <EventEdit ref="edit" @fetch-data="fetchData" :riskcatid="riskcatid" />
    <Views ref="edit1" @fetchData="fetchData"></Views>
    <HgjhglView ref="edit2" @fetchData="fetchData"></HgjhglView>
  </div>
</template>

<script>
  import * as echarts from 'echarts'
  import first from './components/first'
  import EventEdit from './components/EventEdit.vue'
  import Views from '@/views/internal/new/hgbg/components/indexView.vue'
  import HgjhglView from '@/views/internal/new/hggl/components/hgjhglView.vue'
  import { getCompcnt } from '@/api/internal/home'
  import { riskList } from '@/api/internal/new/plan'
  import { getReportList, getJHGLList } from '@/api/hggl/hgjhgl'
  import notice from '@/views/contract/home/index.vue'
  export default {
    name: 'Download',
    components: { first, EventEdit, Views, HgjhglView, notice },
    data() {
      return {
        defectGrade: [],
        list: [],
        list1: [],
        list2: [],
        listLoading: false,
        queryForm: {
          pageNumber: 1,
          pageSize: 5,
        },
        riskcatid: undefined,
        type: ['', '类型1', '类型2', '类型3'],
      }
    },
    async mounted() {
      this.fetchData()

      let res = await getCompcnt()
      console.log(res)
      this.defectGrade = [
        { name: '规章制度', value: res.data.institution_cnt },
        { name: '重要事项法审', value: res.data.matters_cnt },
      ]

      const chats8 = echarts.init(document.getElementById('chats-8'))

      // 缺陷等级统计
      chats8.setOption({
        title: {
          text: '合规审查数量',
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
            data: this.defectGrade,
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
    methods: {
      async fetchData() {
        // 表格数据
        this.listLoading = true
        const {
          data: { tlist },
        } = await riskList({ ...this.queryForm })
        this.list = tlist

        getReportList(this.queryForm).then((res) => {
          this.list1 = res.data.tlist
        })
        getJHGLList(this.queryForm).then((res) => {
          this.list2 = res.data.tlist
        })

        this.listLoading = false
      },
      handleRead(row) {
        this.$refs['edit'].showEdit(row, 'detail')
      },
      handleDetail(row) {
        this.$refs['edit1'].showEdit('详情', row)
      },
      handleDetails(row) {
        this.$refs['edit2'].showEdit('详情', row)
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

  .table-responsive {
    background: white;
    padding: 20px;
    margin-bottom: 20px;
  }

  h5 {
    margin: 0 0 10px 0;
    font-size: 17px;
  }
</style>
