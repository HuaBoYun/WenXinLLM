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
              <el-input
                v-model="queryForm.supplier"
                clearable
                placeholder="本方账户对应子户"
                v-if="item.name === '本方账户对应子户'"
              />
              <el-input
                v-model="queryForm.supplier"
                clearable
                placeholder="对方单位"
                v-if="item.name === '对方单位'"
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
                v-model="queryForm.receiptsNo"
                clearable
                placeholder="生成状态"
                v-if="item.name === '生成状态'"
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
      <!-- <el-tabs v-model="activeName" @tab-click="handleClick">
        <el-tab-pane label="未生成" name="first"></el-tab-pane>
        <el-tab-pane label="已生成" name="second"></el-tab-pane>
        <el-tab-pane label="已发布" name="second1"></el-tab-pane>
        <el-tab-pane label="已认领" name="second2"></el-tab-pane>
        <el-tab-pane label="不生成" name="second3"></el-tab-pane>
        <el-tab-pane label="全部" name="all"></el-tab-pane>
      </el-tabs> -->
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
          新增
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
            v-if="item.name === '单据编号'"
            align="center"
            label="单据编号"
            prop="sjlxName"
            show-overflow-tooltip
          ></el-table-column>
          <el-table-column
            v-if="item.name === '到账日期'"
            align="center"
            label="到账日期"
            prop="costEstimation"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '本方银行账户'"
            align="center"
            label="本方银行账户"
            prop="costEstimation"
            show-overflow-tooltip
            width="140"
          />
          <el-table-column
            v-if="item.name === '对方单位'"
            align="center"
            label="对方单位"
            prop="costEstimation"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '对方账户'"
            align="center"
            label="对方账户"
            prop="costEstimation"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '收付性质'"
            align="center"
            label="收付性质"
            prop="costEstimation"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '币种'"
            align="center"
            label="币种"
            prop="costEstimation"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '金额'"
            align="center"
            label="金额"
            prop="costEstimation"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '已认领金额'"
            align="center"
            label="已认领金额"
            prop="costEstimation"
            show-overflow-tooltip
            width="120"
          />
          <el-table-column
            v-if="item.name === '生成状态'"
            align="center"
            label="生成状态"
            prop="costEstimation"
            show-overflow-tooltip
            width="160"
          />
          <el-table-column
            v-if="item.name === '摘要'"
            align="center"
            label="摘要"
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
                  <el-button>启用</el-button>
                  <el-button>停用</el-button>
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
  import Edit from './components/dztzzdsdgzEdit.vue'
  import filterSearch from '@/components/filterSearch'
  import filterTable from '@/components/filterTable'
  import { searchTableMixis } from '@/mixis/index'

  export default {
    name: 'gxzxdztzfb',
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
          { name: '单据编号' },
          { name: '到账日期' },
          { name: '本方银行账户' },
          { name: '对方单位' },
          { name: '对方账户' },
          { name: '收付性质' },
          { name: '币种' },
          { name: '金额' },
          { name: '已认领金额' },
          { name: '生成状态' },
          { name: '摘要' },
        ], //所有表格项
        filedNow: [], //当前表格项
        searchAll: this.getFiled(), //所有搜索项
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        localKey: 'globalTreasurer-jsgl-gxzxdztzfb-search',
        tableKey: 'globalTreasurer-jsgl-gxzxdztzfb-list',
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
    },
    beforeDestroy() {},
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
          { name: '财务组织', key: 'jsFinance' },
          { name: '本方账户对应子户', key: 'supplier' },
          { name: '对方单位', key: 'supplier' },
          { name: '时间', key: 'receiptsNo' },
          { name: '生成状态', key: 'receiptsNo' },
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
