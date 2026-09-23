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
                v-model="queryForm.organization"
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
              <el-select
                v-model="queryForm.sortType"
                placeholder="排序"
                clearable
                v-if="item.name === '排序'"
              >
                <el-option
                  v-for="item in sortType"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                ></el-option>
              </el-select>
              <el-input
                v-model="queryForm.curreny"
                clearable
                placeholder="现金账户"
                v-if="item.name === '现金账户'"
              />
              <el-input
                v-model="queryForm.curreny"
                clearable
                placeholder="币种"
                v-if="item.name === '币种'"
              />
              <el-select
                v-model="queryForm.businessType"
                placeholder="业务系统"
                clearable
                v-if="item.name === '业务系统'"
              >
                <el-option
                  v-for="item in businessType"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                ></el-option>
              </el-select>
              <el-input
                v-model="queryForm.curreny"
                clearable
                placeholder="交易类型"
                v-if="item.name === '交易类型'"
              />
              <el-select
                v-model="queryForm.sortType"
                placeholder="第一排序"
                clearable
                v-if="item.name === '第一排序'"
              >
                <el-option
                  v-for="item in sortType"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                ></el-option>
              </el-select>
              <el-select
                v-model="queryForm.sortType"
                placeholder="第二排序"
                clearable
                v-if="item.name === '第二排序'"
              >
                <el-option
                  v-for="item in sortType"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                ></el-option>
              </el-select>
              <el-select
                v-model="queryForm.sortType"
                placeholder="第三排序"
                clearable
                v-if="item.name === '第三排序'"
              >
                <el-option
                  v-for="item in sortType"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                ></el-option>
              </el-select>
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
      <div class="top_box">
        <!-- <vab-query-form-right-panel style="width: 100%">
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
              <el-button
                slot="reference"
                icon="el-icon-s-grid"
                class="biaoge"
                style="margin-bottom: 10px; margin-right: 10px"
              ></el-button>
            </el-popover>
          </el-tooltip>
          <el-button type="success">结账</el-button>
          <el-button type="success">取消结账</el-button>
        </vab-query-form-right-panel> -->
      </div>

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
        <el-table-column
          align="center"
          label="币种"
          prop="qdcode"
        ></el-table-column>
        <el-table-column
          align="center"
          label="银行档案"
          prop="qdcode"
        ></el-table-column>
        <el-table-column
          align="center"
          label="银行类别"
          prop="qdcode"
        ></el-table-column>
        <el-table-column
          align="center"
          label="账户名称"
          prop="qdcode"
        ></el-table-column>
        <el-table-column
          align="center"
          label="银行账户"
          prop="qdcode"
        ></el-table-column>
        <el-table-column
          align="center"
          label="账户属性"
          prop="qdcode"
        ></el-table-column>
        <el-table-column label="期初余额" align="center">
          <el-table-column
            align="center"
            label="原币"
            prop="qdcode"
          ></el-table-column>
          <el-table-column
            align="center"
            label="本币"
            prop="qdcode"
          ></el-table-column>
        </el-table-column>
        <el-table-column label="本期收入" align="center">
          <el-table-column
            align="center"
            label="原币"
            prop="qdcode"
          ></el-table-column>
          <el-table-column
            align="center"
            label="本币"
            prop="qdcode"
          ></el-table-column>
        </el-table-column>
        <el-table-column label="本期支出" align="center">
          <el-table-column
            align="center"
            label="原币"
            prop="qdcode"
          ></el-table-column>
          <el-table-column
            align="center"
            label="本币"
            prop="qdcode"
          ></el-table-column>
        </el-table-column>
        <el-table-column label="期末余额" align="center">
          <el-table-column
            align="center"
            label="原币"
            prop="qdcode"
          ></el-table-column>
          <el-table-column
            align="center"
            label="本币"
            prop="qdcode"
          ></el-table-column>
        </el-table-column>
        <el-table-column label="可用余额" align="center">
          <el-table-column
            align="center"
            label="原币"
            prop="qdcode"
          ></el-table-column>
          <el-table-column
            align="center"
            label="本币"
            prop="qdcode"
          ></el-table-column>
        </el-table-column>
        <el-table-column
          align="center"
          label="平均余额"
          prop="qdcode"
        ></el-table-column>

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
  import Edit from './components/wbdhEdit.vue'
  import filterSearch from '@/components/filterSearch'
  import filterTable from '@/components/filterTable'
  import { searchTableMixis } from '@/mixis/index'

  export default {
    name: 'yhckrcx',
    mixins: [searchTableMixis],
    components: {
      Edit,
      filterSearch,
      filterTable,
    },
    data() {
      return {
        tableData: [],
        list: [],
        planNum: '',
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          organization: undefined,
          day: undefined,
          curreny: undefined,
          pageNumber: 1,
          pageSize: 10,
          xmnd: '',
        },
        currProjectId: '',
        filedAll: [
          { name: '财务组织' },
          { name: '现金账户' },
          { name: '币种' },
        ], //所有表格项
        filedNow: [], //当前表格项
        searchAll: this.getFiled(), //所有搜索项
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        localKey: 'globalTreasurer-jsgl-yhckrcx-search',
        tableKey: 'globalTreasurer-jsgl-yhckrcx-list',
        searchMore: false,
        select: [],
        sortType: [
          {
            value: '1',
            label: '按组织排序',
          },
          {
            value: '2',
            label: '按币种排序',
          },
        ],
        businessType: [
          {
            value: '1',
            label: '融资租赁',
          },
          {
            value: '2',
            label: '应收管理',
          },
          {
            value: '3',
            label: '应付管理',
          },
          {
            value: '4',
            label: '现金管理',
          },
          {
            value: '5',
            label: '结算平台',
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
    methods: {
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
          { name: '财务组织', key: 'organization' },
          { name: '时间', key: 'day' },
          { name: '排序', key: 'sort' },
          { name: '现金账户', key: 'currentAccount' },
          { name: '币种', key: 'currency' },
          { name: '业务系统', key: 'operationSystem' },
          { name: '交易类型', key: 'dealType' },
          { name: '第一排序', key: 'dealType' },
          { name: '第二排序', key: 'dealType' },
          { name: '第三排序', key: 'dealType' },
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
  .secondCard_2 {
    margin-top: 15px !important;
  }
  .pagination {
    margin-bottom: 20px !important;
  }
  .top_r {
    width: 60px;
  }
  .r_line {
    width: 20px;
    height: 2px;
    background: red;
    margin: 5px auto;
  }
  .top_box {
    display: flex;
  }
  .account_pagination {
    margin-bottom: 15px !important;
  }
</style>
