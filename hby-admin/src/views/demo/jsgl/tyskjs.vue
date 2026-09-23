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
                placeholder="财务组织"
                v-if="item.name === '财务组织'"
              />
              <el-date-picker
                v-model="queryForm.day"
                type="daterange"
                align="right"
                unlink-panels
                range-separator="至"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                v-if="item.name === '时间'"
              ></el-date-picker>
              <el-input
                v-model="queryForm.supplier"
                clearable
                placeholder="交易类型"
                v-if="item.name === '交易类型'"
              />
              <el-input
                v-model="queryForm.receiptsNo"
                clearable
                placeholder="单据状态"
                v-if="item.name === '单据状态'"
              />
              <el-input
                v-model="queryForm.supplier"
                clearable
                placeholder="单据编号"
                v-if="item.name === '单据编号'"
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

    <div class="top_header">
      <div class="box_c">
        <div class="heji_box">
          <div class="heji_box_1">
            <img src="../../../assets/jsgl/6.png" alt="" />
            <div class="select_1">已选合计</div>
          </div>
          <div class="heji_box_2">
            <div class="number_box">
              <span class="number_box_1">0</span>
              <span class="number_box_2">笔</span>
            </div>
            <div class="number_box">
              <span class="number_box_3">0.00</span>
              <span class="number_box_4">元</span>
            </div>
          </div>
        </div>
      </div>
      <div class="box_c">
        <div class="heji_box">
          <div class="heji_box_no">
            <img src="../../../assets/jsgl/4.png" alt="" />
          </div>
          <div class="heji_box_2">
            <div class="number_box">
              <span class="number_box_1">0</span>
              <span class="number_box_2">笔</span>
              <span class="submitted">待提交</span>
            </div>
            <div class="number_box">
              <span class="number_box_3">0.00</span>
              <span class="number_box_4">元</span>
            </div>
          </div>
        </div>
      </div>
      <div class="box_c">
        <div class="heji_box">
          <div class="heji_box_no">
            <img src="../../../assets/jsgl/7.png" alt="" />
          </div>
          <div class="heji_box_2">
            <div class="number_box">
              <span class="number_box_1">0</span>
              <span class="number_box_2">笔</span>
              <span class="appvore">待审批</span>
            </div>
            <div class="number_box">
              <span class="number_box_3">0.00</span>
              <span class="number_box_4">元</span>
            </div>
          </div>
        </div>
      </div>
      <div class="box_c">
        <div class="heji_box">
          <div class="heji_box_no">
            <img src="../../../assets/jsgl/8.png" alt="" />
          </div>
          <div class="heji_box_2">
            <div class="number_box">
              <span class="number_box_1">0</span>
              <span class="number_box_2">笔</span>
              <span class="account">待结算</span>
            </div>
            <div class="number_box">
              <span class="number_box_3">0.00</span>
              <span class="number_box_4">元</span>
            </div>
          </div>
        </div>
      </div>
      <div class="box_c">
        <div class="heji_box">
          <div class="heji_box_no">
            <img src="../../../assets/jsgl/9.png" alt="" />
          </div>
          <div class="heji_box_2">
            <div class="number_box">
              <span class="number_box_1">0</span>
              <span class="number_box_2">笔</span>
              <span class="accomplish">今日完成</span>
            </div>
            <div class="number_box">
              <span class="number_box_3">0.00</span>
              <span class="number_box_4">元</span>
            </div>
          </div>
        </div>
      </div>
      <div class="box_c">
        <div class="dashboard_box">
          <div
            class="dashboard_1"
            ref="gauge1"
            style="width: 160px; height: 80px"
          ></div>
          <div class="day_1">今日</div>
          <div class="jindu">结算进度</div>
        </div>
      </div>
    </div>

    <el-card shadow="never" class="secondCard">
      <el-tabs v-model="activeName" @tab-click="handleClick">
        <el-tab-pane label="待提交" name="first"></el-tab-pane>
        <el-tab-pane label="待审批" name="second"></el-tab-pane>
        <el-tab-pane label="待结算" name="third"></el-tab-pane>
        <el-tab-pane label="全部" name="all"></el-tab-pane>
      </el-tabs>
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
        <!-- <el-button type="success" @click="handleEdit(false, false)">
          新建
        </el-button> -->
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
          label="财务组织"
          prop="qdcode"
          width="160"
        ></el-table-column>
        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            v-if="item.name === '交易类型'"
            align="center"
            label="交易类型"
            prop="projectOrderName"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '单据编号'"
            align="center"
            label="单据编号"
            prop="sjlxName"
            show-overflow-tooltip
          ></el-table-column>
          <el-table-column
            v-if="item.name === '单据日期'"
            align="center"
            label="单据日期"
            prop="costEstimation"
            show-overflow-tooltip
            width="160"
          />
          <el-table-column
            v-if="item.name === '总金额'"
            align="center"
            label="总金额"
            prop="costEstimation"
            show-overflow-tooltip
            width="180"
          />
          <el-table-column
            v-if="item.name === '来源单据类型'"
            align="center"
            label="来源单据类型"
            prop="costEstimation"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '来源单据制单人'"
            align="center"
            label="来源单据制单人"
            prop="costEstimation"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '单据状态'"
            align="center"
            label="单据状态"
            prop="costEstimation"
            show-overflow-tooltip
            width="140"
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
                  <el-button type="text">确认放行</el-button>
                  <el-button type="text">确认拦截</el-button>
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
  import Edit from './components/zdzfgzEdit.vue'
  import filterSearch from '@/components/filterSearch'
  import filterTable from '@/components/filterTable'
  import { searchTableMixis } from '@/mixis/index'

  export default {
    name: 'tyskjs',
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
          { name: '财务组织' },
          { name: '交易类型' },
          { name: '单据编号' },
          { name: '单据日期' },
          { name: '总金额' },
          { name: '来源单据类型' },
          { name: '来源单据制单人' },
          { name: '单据状态' },
        ], //所有表格项
        filedNow: [], //当前表格项
        searchAll: this.getFiled(), //所有搜索项
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        localKey: 'globalTreasurer-jsgl-tyskjs-search',
        tableKey: 'globalTreasurer-jsgl-tyskjs-list',
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
      this.initGauge('gauge1')
    },
    beforeDestroy() {
      // 销毁echarts实例，避免内存泄漏
      if (this.chart) {
        this.chart.dispose()
      }
    },
    methods: {
      initGauge(ref, title) {
        const gauge = echarts.init(this.$refs.gauge1)

        const option = {
          title: {
            text: title,
            left: '10%',
            top: 'middle',
            textStyle: {
              color: '#666',
            },
          },
          tooltip: {
            formatter: '{b} : {c}%',
          },
          series: [
            {
              // name: '结算进度',
              center: ['60%', '50%'],
              type: 'gauge',
              detail: {
                formatter: '{value}%',
                fontSize: 18,
                offsetCenter: [0, '70%'], // 调整百分比数字的位置
              },
              data: [{ value: 50 }],
              radius: '90%', // 设置仪表盘大小
              axisLine: {
                lineStyle: {
                  width: 10,
                  color: [
                    [
                      1,
                      new echarts.graphic.LinearGradient(0, 0, 1, 0, [
                        {
                          offset: 0,
                          color: '#ff4500',
                        },
                        {
                          offset: 1,
                          color: '#ffbf00',
                        },
                      ]),
                    ],
                  ], // 使用渐变色
                },
              },
              splitLine: {
                show: false, // 隐藏分割线
              },
              axisTick: {
                show: false, // 隐藏刻度
              },
              axisLabel: {
                show: false, // 隐藏刻度标签
              },
              pointer: {
                show: false, // 隐藏指针
              },
            },
          ],
        }

        gauge.setOption(option)
      },
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
          { name: '财务组织', key: 'jsFinance' },
          { name: '时间', key: 'supplier' },
          { name: '交易类型', key: 'receiptsNo' },
          { name: '单据状态', key: 'day' },
          { name: '单据编号', key: 'supplier' },
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

      handleEdit(row, disabled, type) {
        this.$refs['edit'].showEdit(row, disabled, this.planNum, type)
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
  .box_c {
    width: 16%;
    height: 90px;
  }
  .box_c:nth-child(1) {
    background: rgba(107, 187, 255, 0.1);
  }
  .box_c:nth-child(2) {
    background: rgba(236, 99, 62, 0.1);
  }
  .box_c:nth-child(3) {
    background: rgba(255, 178, 67, 0.08);
  }
  .box_c:nth-child(4) {
    background: rgba(28, 215, 151, 0.1);
  }
  .box_c:nth-child(5) {
    background: rgba(107, 187, 255, 0.1);
  }
  .heji_box {
    display: flex;
    align-items: center;
    height: 100%;
  }
  .heji_box_no {
    flex: 1;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    height: 100%;
  }
  .heji_box_1 {
    flex: 1;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    height: 100%;
    text-align: center;
    background: linear-gradient(316deg, #4c86f0 0%, #6bbbff 100%);
  }
  .select_1 {
    font-size: 12px;
    color: #fff;
    margin-top: 10px;
  }
  .heji_box_2 {
    flex: 2;
    height: 100%;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
  }
  .number_box {
    width: 100%;
    margin-left: 10px;
  }
  .number_box_1 {
    font-size: 20px;
    font-weight: bold;
  }
  .number_box_2 {
    font-size: 12px;
  }
  .number_box_3 {
    font-size: 18px;
    font-weight: bold;
    color: #6bbbff;
  }
  .number_box_4 {
    font-size: 12px;
    color: #6bbbff;
  }
  .submitted {
    font-size: 11px;
    border-radius: 10px;
    padding: 3px;
    background: #fff;
    color: rgb(236, 99, 62);
    border: 1px solid rgb(236, 99, 62);
    margin-left: 5px;
  }
  .appvore {
    font-size: 11px;
    border-radius: 10px;
    padding: 3px;
    background: #fff;
    color: rgb(255, 151, 0);
    border: 1px solid rgb(255, 151, 0);
    margin-left: 5px;
  }
  .account {
    font-size: 11px;
    border-radius: 10px;
    padding: 3px;
    background: #fff;
    color: rgb(0, 175, 115);
    border: 1px solid rgb(0, 175, 115);
    margin-left: 5px;
  }
  .accomplish {
    font-size: 11px;
    border-radius: 10px;
    padding: 3px;
    background: #fff;
    color: rgb(62, 166, 255);
    border: 1px solid rgb(62, 166, 255);
    margin-left: 5px;
  }
  .dashboard_box {
    position: relative;
  }
  .day_1 {
    position: absolute;
    top: 10px;
    left: 10px;
    font-size: 12px;
    border-radius: 10px;
    padding: 3px 5px;
    color: #4c86f0;
    background: rgba(76, 134, 240, 0.08);
    border: 1px solid rgba(76, 134, 240, 0.4);
  }
  .jindu {
    position: absolute;
    top: 42px;
  }
</style>
