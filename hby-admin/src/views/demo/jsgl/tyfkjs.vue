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
      <div class="header_l">
        <div class="header_l_top">
          <div class="l_top_1">
            <div class="top_box">
              <div class="icon_img">
                <img src="../../../assets/jsgl/1.png" alt="" />
              </div>
              <div>
                <span class="title_1">直联账户余额</span>
                <div>
                  <span class="title_2">0.00</span>
                  <span class="title_3">元</span>
                </div>
              </div>
            </div>
          </div>
          <div class="l_top_1">
            <div class="top_box">
              <div class="icon_img">
                <img src="../../../assets/jsgl/2.png" alt="" />
              </div>
              <div>
                <span class="title_1">直联账户余额</span>
                <div>
                  <span class="title_2">0.00</span>
                  <span class="title_3">元</span>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div class="condition_box">
          <div>预算使用情况</div>
          <div class="easily_box">
            <div>
              <span class="easily_1"></span>
              <span class="easily_2">已用</span>
            </div>
            <div>
              <span class="easily_3"></span>
              <span class="easily_2">可用</span>
            </div>
          </div>
        </div>
        <div class="money_box">
          <div>0.00元</div>
          <div>0.00元</div>
        </div>
      </div>
      <div class="header_c">
        <div class="title_r">
          <span>结算方案推荐</span>
          <div>
            <el-popover
              placement="top-start"
              width="200"
              trigger="hover"
              content="选中同一财务组织结算单才能进行结算方案推荐"
            >
              <i class="el-icon-question"></i>
            </el-popover>
          </div>
        </div>
        <div class="box_1">
          <div
            ref="ringChart"
            style="width: 230px; height: 150px"
            class="echatr_box"
          ></div>
          <div class="container">
            <div class="card">
              <div class="icon">
                <img src="../../../assets/jsgl/3.png" alt="House Icon" />
              </div>
              <div class="content">
                <p class="title">已选合计</p>
                <div class="numbers">
                  <div>
                    <span class="number_1">0</span>
                    <span class="number_2">笔</span>
                  </div>
                  <div>
                    <span>0.00</span>
                    <span class="number_3">元</span>
                  </div>
                </div>
              </div>
            </div>
            <div class="card">
              <div class="icon">
                <img src="../../../assets/jsgl/4.png" alt="Public Icon" />
              </div>
              <div class="content">
                <p class="title">已选对公</p>
                <div class="numbers">
                  <div>
                    <span class="number_1">0</span>
                    <span class="number_2">笔</span>
                  </div>
                  <div>
                    <span>0.00</span>
                    <span class="number_3">元</span>
                  </div>
                </div>
              </div>
            </div>
            <div class="card">
              <div class="icon">
                <img src="../../../assets/jsgl/5.png" alt="Private Icon" />
              </div>
              <div class="content">
                <p class="title">已选对私</p>
                <div class="numbers">
                  <div>
                    <span class="number_1">0</span>
                    <span class="number_2">笔</span>
                  </div>
                  <div>
                    <span>0.00</span>
                    <span class="number_3">元</span>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="header_r">
        <div class="r_box_1">
          <div class="content">
            <div class="day">今日</div>
            <div class="title_3">结算进度</div>
            <div class="progcess">
              <div class="progcess_1">0.00</div>
              <div class="progcess_2">%</div>
            </div>
            <div class="el_box">
              <el-progress
                :text-inside="true"
                :stroke-width="12"
                :percentage="70"
              ></el-progress>
            </div>
          </div>
        </div>
        <div class="r_box_2">
          <div class="dashboard">
            <div
              class="dashboard_1"
              ref="gauge1"
              style="width: 160px; height: 80px"
            ></div>
            <div
              class="dashboard_2"
              ref="gauge2"
              style="width: 160px; height: 80px"
            ></div>
            <div class="gauge1_title">财司付款</div>
            <div class="gauge2_title">直联支付</div>
          </div>
        </div>
      </div>
    </div>

    <el-card shadow="never" class="secondCard">
      <el-tabs v-model="activeName" @tab-click="handleClick">
        <el-tab-pane label="待提交" name="first"></el-tab-pane>
        <el-tab-pane label="待审批" name="second"></el-tab-pane>
        <el-tab-pane label="待结算" name="third"></el-tab-pane>
        <el-tab-pane label="结算中" name="third2"></el-tab-pane>
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
            v-if="item.name === '期望付款日期'"
            align="center"
            label="期望付款日期"
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
    name: 'tyfkjs',
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
          { name: '期望付款日期' },
          { name: '来源单据制单人' },
          { name: '单据状态' },
        ], //所有表格项
        filedNow: [], //当前表格项
        searchAll: this.getFiled(), //所有搜索项
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        localKey: 'globalTreasurer-jsgl-tyfkjs-search',
        tableKey: 'globalTreasurer-jsgl-tyfkjs-list',
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
      this.initGauge('gauge2')

      // 绘制图表
      this.chart.setOption({
        tooltip: {
          trigger: 'item',
        },
        legend: {
          orient: 'vertical', // 图例列表的布局朝向，默认水平，这里设置为垂直
          right: '5%', // 设置图例到容器右侧的距离，以百分比表示
          top: 'middle', // 设置图例垂直居中显示
          data: ['应付票据', '应收票据', '银行存款', '资金缺口'], // 图例的数据项
        },
        series: [
          {
            left: '-90px',
            type: 'pie',
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
              { value: 335, name: '应付票据' },
              { value: 310, name: '应收票据' },
              { value: 234, name: '银行存款' },
              { value: 135, name: '资金缺口' },
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
      initGauge(ref, title) {
        const gauge = echarts.init(this.$refs[ref])

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
    justify-content: space-between;
    margin-bottom: 10px;
  }
  .header_l,
  .header_r {
    flex: 1;
    width: 29%;
    padding: 15px;
    background: #ffff;
  }
  .header_r {
    display: flex;
    justify-content: space-between;
  }
  .header_c {
    flex: 2;
    width: 39%;
    padding: 15px;
    background: #ffff;
  }
  .header_l_top {
    display: flex;
    justify-content: space-between;
  }
  .l_top_1 {
    width: 48%;
  }
  .l_top_1:nth-child(1) {
    background: rgba(255, 178, 67, 0.1);
  }
  .l_top_1:nth-child(2) {
    background: rgba(107, 187, 255, 0.1);
  }
  .top_box {
    padding: 5px 0;
    display: flex;
    justify-content: center;
    align-items: center;
  }
  .icon_img {
    margin: 5px 5px 0 0;
  }
  .icon {
    width: 50px;
    height: 50px;
  }
  .icon img {
    width: 100%;
  }
  .title_1 {
    font-size: 12px;
  }
  .title_2 {
    font-size: 24px;
    font-weight: bold;
  }
  .title_3 {
    font-size: 12px;
  }
  .condition_box {
    display: flex;
    justify-content: space-between;
    margin: 15px 0;
  }
  .easily_box {
    display: flex;
  }
  .easily_1 {
    display: inline-block;
    width: 5px;
    height: 5px;
    background: #1cd797;
  }
  .easily_3 {
    display: inline-block;
    width: 5px;
    height: 5px;
    background: orange;
  }
  .money_box {
    display: flex;
    justify-content: space-between;
    background: #ccc;
  }
  .box_1 {
    display: flex;
  }
  .echatr_box {
    flex: 1;
  }
  .container {
    flex: 1;
    display: flex;
    flex-wrap: wrap;
    gap: 10px;
  }

  .card {
    display: flex;
    flex-direction: column;
    align-items: center;
    background-color: white;
    border-radius: 10px;
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
    width: calc((100% - 20px) / 3); /* Adjust for spacing */
    padding: 8px;
    text-align: center;
  }

  .icon {
    width: 35px;
    height: 35px;
    border-radius: 10px;
    overflow: hidden;
    margin-bottom: 10px;
  }

  .icon img {
    width: 100%;
    height: 100%;
    object-fit: cover;
  }

  .content {
    text-align: center;
  }

  .title {
    font-size: 12px;
    color: #333;
    margin-bottom: 10px;
  }

  .numbers {
    font-size: 24px;
    color: #000;
  }

  .number_2 {
    font-size: 12px;
  }

  .number_3 {
    font-size: 12px;
  }

  .number {
    font-weight: bold;
  }
  .r_box_1 {
    width: 30%;
    padding: 0 5px;
    border-radius: 10px;
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  }
  .r_box_2 {
    width: 65%;
  }
  .day {
    width: 36px;
    border-radius: 10px;
    padding: 3px 5px;
    color: #4c86f0;
    border: 1px solid #a4bdeb;
    font-size: 12px;
  }
  .title_3 {
    text-align: left;
    font-size: 16px;
    margin: 10px 0;
  }
  .progcess {
    display: flex;
    align-items: center;
  }
  .progcess_1 {
    font-size: 24px;
    font-weight: bold;
  }
  .el_box {
    text-align: center;
    margin: 20px 0;
  }
  .dashboard {
    position: relative;
  }
  .dashboard canvas {
    left: 50px;
  }
  .gauge1_title {
    position: absolute;
    top: 10px;
  }
  .gauge2_title {
    position: absolute;
    bottom: 48px;
  }
  // .dashboard_1,
  // .dashboard_2 {
  //   // margin: 10px;
  //   box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  // }
</style>
