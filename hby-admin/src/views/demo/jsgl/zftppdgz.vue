<template>
  <div class="system-log-container">
    <vab-query-form>
      <el-card shadow="never">
        <vab-query-form-left-panel :span="24">
          <el-form
            ref="form"
            :inline="true"
            label-width="0"
            :model="queryForm"
            @submit.native.prevent
          >
            <el-form-item v-for="(item, index) in searchItem" :key="index">
              <el-input
                v-model="queryForm.jsFinance"
                clearable
                placeholder="摘要条件"
                v-if="item.name === '摘要条件'"
              />
              <el-input
                v-model="queryForm.supplier"
                clearable
                placeholder="关系"
                v-if="item.name === '关系'"
              />
              <el-input
                v-model="queryForm.supplier"
                clearable
                placeholder="日期范围"
                v-if="item.name === '日期范围'"
              />
              <el-input
                v-model="queryForm.receiptsNo"
                clearable
                placeholder="创建人"
                v-if="item.name === '创建人'"
              />
              <el-input
                v-model="queryForm.receiptsNo"
                clearable
                placeholder="最后修改人"
                v-if="item.name === '最后修改人'"
              />
            </el-form-item>
            <el-form-item>
              <el-button
                icon="el-icon-search"
                native-type="submit"
                type="primary"
                @click="fetchData"
              >
                查询
              </el-button>
            </el-form-item>
            <el-form-item>
              <el-button
                native-type="submit"
                type="primary"
                @click="resetSearch"
              >
                重置
              </el-button>
            </el-form-item>
            <el-form-item>
              <el-tooltip
                class="item"
                effect="dark"
                content="搜索筛选"
                placement="top"
              >
                <el-popover placement="left" trigger="click">
                  <filter-search
                    v-if="true"
                    :list="searchAll"
                    :name="localKey"
                    @updateSearchShow="initSearch"
                  />
                  <el-button slot="reference" style="height: 32px">
                    <vab-icon icon="filter" :is-custom-svg="true" />
                  </el-button>
                </el-popover>
              </el-tooltip>
            </el-form-item>
            <el-form-item>
              <span
                :class="searchMore ? 'search-more is-opened' : 'search-more'"
                @click="showMore"
              >
                <span>{{ searchMore ? '收起' : '展开' }}</span>
                <i class="el-icon-arrow-down"></i>
              </span>
            </el-form-item>
          </el-form>
        </vab-query-form-left-panel>
      </el-card>
    </vab-query-form>

    <el-card shadow="never" class="secondCard">
      <vab-query-form-right-panel style="width: 100%">
        <el-tooltip
          class="item"
          effect="dark"
          content="表格筛选"
          placement="top"
        >
          <el-popover placement="right" trigger="click">
            <filter-table
              :list="filedAll"
              :name="tableKey"
              @updateTableShow="initTable"
            />
            <!-- <i class="el-icon-delete" slot="reference"></i> -->
            <el-button
              slot="reference"
              icon="el-icon-s-grid"
              class="biaoge"
              style="margin-bottom: 10px; margin-right: 10px"
            ></el-button>
          </el-popover>
        </el-tooltip>
        <el-button type="success" @click="handleEdit(false, false)">
          新建
        </el-button>
        <!-- <el-button type="primary">导出</el-button> -->
      </vab-query-form-right-panel>

      <el-table v-loading="listLoading" :data="list">
        <el-table-column
          type="selection"
          width="55"
          align="center"
        ></el-table-column>
        <el-table-column
          align="center"
          label="摘要判断条件"
          prop="qdcode"
          width="160"
        ></el-table-column>
        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            v-if="item.name === '关系'"
            align="center"
            label="关系"
            prop="sjlxName"
            show-overflow-tooltip
          ></el-table-column>
          <el-table-column
            v-if="item.name === '日期范围'"
            align="center"
            label="日期范围"
            prop="costEstimation"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '创建人'"
            align="center"
            label="创建人"
            prop="costEstimation"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '最后修改人'"
            align="center"
            label="最后修改人"
            prop="costEstimation"
            show-overflow-tooltip
          />
        </div>

        <el-table-column
          align="center"
          label="操作"
          show-overflow-tooltip
          width="120"
        >
          <template slot-scope="scope">
            <el-button
              type="text"
              @click="handleEdit(scope.row, false)"
              :disabled="!!scope.row.spzt"
            >
              修改
            </el-button>
            <el-dropdown style="margin-left: 10px">
              <el-button type="text">更多</el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item>
                  <el-button
                    @click="handleDelete(scope.row)"
                    type="text"
                    :disabled="!!scope.row.spzt"
                  >
                    删除
                  </el-button>
                </el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-pagination
      background
      :current-page="queryForm.pageNumber"
      :layout="layout"
      :page-size="queryForm.pageSize"
      :total="total"
      @current-change="handleCurrentChange"
      @size-change="handleSizeChange"
    />
    <Edit ref="edit" @fetch-data="fetchData"></Edit>
  </div>
</template>

<script>
  import {
    implementPlanList,
    implementPlanDelete,
    fpzyksry,
  } from '@/oapi/audit/project'
  import * as echarts from 'echarts'
  import Edit from './components/zftppdgzEdit.vue'
  import filterSearch from '@/components/filterSearch'
  import filterTable from '@/components/filterTable'
  import { searchTableMixis } from '@/mixis/index'

  export default {
    name: 'zftppdgz',
    mixins: [searchTableMixis],
    components: {
      Edit,
      filterSearch,
      filterTable,
    },
    data() {
      return {
        list: [],
        planNum: '',
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          jsFinance: undefined,
          receiptsNo: undefined,
          day: undefined,
          supplier: undefined,
          startStatus: undefined,
          pageNumber: 1,
          pageSize: 10,
          xmnd: '',
        },
        currProjectId: '',
        filedAll: [
          { name: '摘要判断条件' },
          { name: '关系' },
          { name: '日期范围' },
          { name: '创建人' },
          { name: '最后修改人' },
        ], //所有表格项
        filedNow: [], //当前表格项
        searchAll: this.getFiled(), //所有搜索项
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        localKey: 'globalTreasurer-jsgl-zftppdgz-search',
        tableKey: 'globalTreasurer-jsgl-zftppdgz-list',
        searchMore: false,
        select: [],
        optionsStatus: [
          {
            value: '1',
            label: '开启',
          },
          {
            value: '2',
            label: '关闭',
          },
        ],
        activeName: 'second',
        gaugeOption: {
          tooltip: {
            formatter: '{a} <br/>{b} : {c}%',
          },
          series: [
            {
              name: '结算进度',
              type: 'gauge',
              radius: ['60%', '80%'],
              startAngle: -90,
              endAngle: 90,
              splitNumber: 5,
              axisLine: {
                lineStyle: {
                  width: 10,
                  color: [
                    [0.2, '#ff4500'],
                    [0.8, '#ffbf00'],
                    [1, '#ff7f50'],
                  ],
                },
              },
              axisTick: {
                show: false,
              },
              axisLabel: {
                show: false,
              },
              pointer: {
                show: false,
              },
              detail: {
                formatter: '{value}%',
                fontSize: 18,
                offsetCenter: [0, '-20%'],
              },
              data: [{ value: 0 }],
            },
          ],
        },
      }
    },
    created() {
      this.fetchData()
      this.initTable()
      this.searchNow = this.getFiled()
      this.searchItem = this.searchNow.slice(0, 4)
      this.initSearch()
    },
    mounted() {
      this.$bus.on('updateMsg', (value) => {
        if (value == 0) {
          this.fetchData()
        }
      })
      this.chart = echarts.init(this.$refs.ringChart)

      // 绘制图表
      this.chart.setOption({
        tooltip: {
          trigger: 'item',
        },
        legend: {
          orient: 'vertical', // 图例列表的布局朝向，默认水平，这里设置为垂直
          right: '5%', // 设置图例到容器右侧的距离，以百分比表示
          top: 'middle', // 设置图例垂直居中显示
          data: ['待提交', '待支付', '支付超时', '支付挂起', '已支付'], // 图例的数据项
        },
        series: [
          {
            left: '-90px',
            type: 'pie',
            center: ['50%', '50%'],
            radius: ['50%', '70%'], // 设置内半径和外半径，形成环形
            avoidLabelOverlap: false,
            itemStyle: {
              borderRadius: 10, // 设置圆角大小
              borderColor: '#fff',
              borderWidth: 2,
            },
            label: {
              show: false,
              position: 'center',
            },
            emphasis: {
              label: {
                show: true,
                fontSize: '12',
                fontWeight: 'bold',
              },
            },
            labelLine: {
              show: false,
            },
            data: [
              { value: 335, name: '待提交' },
              { value: 310, name: '待支付' },
              { value: 234, name: '支付超时' },
              { value: 135, name: '支付挂起' },
              { value: 135, name: '已支付' },
            ],
          },
        ],
      })
    },
    beforeDestroy() {
      // 销毁echarts实例，避免内存泄漏
      if (this.chart) {
        this.chart.dispose()
      }
    },
    methods: {
      yibiao() {},
      initTable() {
        this.loading = true
        let self = this
        this.$nextTick(function () {
          let data = localStorage.getItem(self.tableKey)
          if (data) {
            data = JSON.parse(data)
            let tempArr = []
            for (let i = 0; i < data.length; i++) {
              if (data[i].show) {
                tempArr.push(data[i])
              }
            }
            this.filedNow = tempArr
          } else {
            this.filedNow = this.filedAll
          }
          this.loading = false
        })
      },
      initSearch() {
        let self = this
        this.$nextTick(function () {
          let data = localStorage.getItem(self.localKey)
          if (data) {
            data = JSON.parse(data)
            let tempArr = []
            for (let i = 0; i < data.length; i++) {
              if (data[i].show) {
                tempArr.push(data[i])
              }
            }
            this.searchNow = tempArr
          } else {
            this.searchNow = this.searchAll
          }

          // 重置非展示搜索项
          this.searchAll.forEach((x) => {
            if (!this.searchNow.some((y) => y.key === x.key)) {
              if (Array.isArray(this.queryForm[x.key])) {
                this.queryForm[x.key] = []
              } else if (this.queryForm[x.key] instanceof Object) {
                this.queryForm[x.key] = {}
              } else {
                this.queryForm[x.key] = null
              }
            }
          })
          if (this.searchMore) {
            this.searchItem = this.searchNow
          } else {
            this.searchItem = this.searchNow.slice(0, 4)
          }
        })
      },
      getFiled() {
        return [
          { name: '摘要条件', key: 'jsFinance' },
          { name: '关系', key: 'day' },
          { name: '日期范围', key: 'supplier' },
          { name: '创建人', key: 'supplier' },
          { name: '最后修改人', key: 'receiptsNo' },
        ]
      },
      selectTeamList(val, flagTitle) {
        console.log(val, flagTitle)
        if (flagTitle) {
          this.queryForm.projectOrderName = val[0].realname
          this.queryForm.projectOrderId = val[0].staffid
        } else {
          let arrStr = ''
          let arr = []
          val.forEach((item) => {
            arr.push(item.realname)
          })
          arrStr = arr.join(',')
          this.tableData[this.sIndex].zyNames = arrStr
          //拿到组员id字符串
          let arrStrZy = ''
          let arrZy = []
          val.forEach((item) => {
            arrZy.push(item.staffid)
          })
          arrStrZy = arrZy.join(',')
          this.zyStaffids = arrStrZy
        }
      },
      showGroupLeader() {
        this.$refs['select'].showEdit('leader')
      },
      resetQueryForm() {
        this.queryForm = {
          projectOrderName: undefined,
          projectOrderId: undefined,
          pageNumber: 1,
          pageSize: 10,
          xmnd: '',
        }
      },
      resetSearch() {
        this.resetQueryForm()
        this.fetchData()
      },
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.fetchData()
      },
      queryData() {
        this.queryForm.pageNumber = 1
        this.fetchData()
      },
      async fetchData() {
        this.listLoading = true
        let { ...other } = this.queryForm
        const {
          data: { tlist, totalRecord, currProjectId },
        } = await implementPlanList({
          ...other,
        })
        this.listLoading = false
        return
        this.currProjectId = currProjectId

        this.list = tlist
        this.total = totalRecord
        this.planNum = tlist[0].projectCode
      },
      days(start, end) {
        let s = new Date(start)
        let e = new Date(end)
        let hours = (e - s) / (1000 * 60 * 60 * 24)
        return hours + '天'
      },

      handleEdit() {
        this.$refs['edit'].handleOpenSxlbDialog()
      },
      async handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg, code } = await implementPlanDelete({ ids: row.id })
          if (code == 1) {
            this.$baseMessage(msg, 'success')
            await this.fetchData()
          } else {
            this.$baseMessage(msg, 'error')
          }
        })
      },
      sendModel() {
        this.$refs['sendModel'].showEdit()
      },
      send() {
        this.$refs['send'].showEdit()
      },
      color(row) {
        if (row.id == this.currProjectId) {
          return { color: '#7fcf7c' }
        } else {
          return { color: '' }
        }
      },
      showMore() {
        this.searchMore = !this.searchMore

        if (this.searchMore) {
          this.searchItem = this.searchNow
        } else {
          this.searchItem = this.searchNow.slice(0, 4)
        }
      },
      handleClick(tab, event) {
        console.log(tab, event)
      },
    },
  }
</script>

<style scoped lang="scss">
  .system-log-container {
    background: #f6f8f9 !important;
    padding: 0 !important;
  }
  .secondCard {
    margin-top: -5px !important;
  }
  .pagination {
    margin-bottom: 20px !important;
  }
  .top_header {
    display: flex;
    background: #fff;
    padding: 10px;
    justify-content: space-between;
    margin-bottom: 10px;
  }
  .header_1 {
    width: 10%;
    padding: 10px;
  }
  .header_1_box {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    padding: 10px;
    background: #1682e624;
    border-radius: 10px;
  }
  .header_1_box_title {
    color: #626b7d70;
    margin: 5px 0;
  }
  .header_1_box_num {
    margin: 5px;
    color: #000;
  }
  .header_1_box_num_1 {
    font-weight: bold;
  }
  .header_1_box_num_2 {
    font-size: 12px;
  }
  .header_1_box_n {
    margin: 5px;
    color: #626b7d70;
  }
  .header_2 {
    width: 35%;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
  }
  .header_2_title {
    width: 100%;
    margin-left: 10px;
  }
  .header_3 {
    width: 10%;
    padding: 10px;
  }
  .header_3_title {
    font-size: 16px;
    font-weight: bold;
  }
  .header_3_box_1 {
    margin: 10px 0;
    color: #666;
  }
  .header_3_box_2 {
    margin: 10px 0;
    color: #666;
  }
  .header_3_box_num_2 {
    margin-left: 10px;
  }
  .header_3_box_num_1 {
    font-size: 16px;
    font-weight: bold;
  }
  .header_3_box_num {
    margin: 10px 0;
  }
  .header_4 {
    width: 45%;
  }
  .header_4_t {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }
  .header_4_t_r {
    display: flex;
    align-items: center;
  }
  .header_4_t_r_1 {
    margin-left: 5px;
    width: 20px;
    font-size: 24px;
    cursor: pointer;
  }
  .header_4_b {
    margin-top: 10px;
    display: flex;
    justify-content: space-between;
  }
  .b_week {
    width: 13.2%;
    padding: 0 5px;
    // text-align: center;
    background: #f6f8f9;
    line-height: 30px;
  }
  .b_week_title_top {
    background: aliceblue;
  }
  .title_top_num {
    margin-left: 8px;
  }
  .week {
    margin-right: 3px;
  }
  .week_number {
    font-weight: bold;
  }
</style>
