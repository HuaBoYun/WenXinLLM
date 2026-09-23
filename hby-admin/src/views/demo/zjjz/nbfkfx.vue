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
                placeholder="结算财务组织"
                v-if="item.name === '结算财务组织'"
              />
              <el-input
                v-model="queryForm.receiptsNo"
                clearable
                placeholder="单据编号"
                v-if="item.name === '单据编号'"
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
                placeholder="供应商"
                v-if="item.name === '供应商'"
              />
              <el-select
                v-model="queryForm.startStatus"
                placeholder="单据状态"
                clearable
                v-if="item.name === '单据状态'"
              >
                <el-option
                  v-for="item in optionsStatus"
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
      <el-tabs v-model="activeName" @tab-click="handleClick">
        <el-tab-pane label="待提交" name="first"></el-tab-pane>
        <el-tab-pane label="审批中" name="second"></el-tab-pane>
        <el-tab-pane label="全部" name="third"></el-tab-pane>
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
          label="结算财务组织"
          prop="qdcode"
          width="160"
        ></el-table-column>
        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            v-if="item.name === '单据编号'"
            align="center"
            label="单据编号"
            prop="projectOrderName"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '单据日期'"
            align="center"
            label="单据日期"
            prop="sjlxName"
            show-overflow-tooltip
          ></el-table-column>
          <el-table-column
            v-if="item.name === '币种'"
            align="center"
            label="币种"
            prop="planYear"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '付款原币金额'"
            align="center"
            label="付款原币金额"
            prop="costEstimation"
            show-overflow-tooltip
            width="160"
          />
          <el-table-column
            v-if="item.name === '付款组织本币金额'"
            align="center"
            label="付款组织本币金额"
            prop="costEstimation"
            show-overflow-tooltip
            width="180"
          />
          <el-table-column
            v-if="item.name === '业务流程'"
            align="center"
            label="业务流程"
            prop="costEstimation"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '部门'"
            align="center"
            label="部门"
            prop="costEstimation"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '业务员'"
            align="center"
            label="业务员"
            prop="costEstimation"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '制单人'"
            align="center"
            label="制单人"
            prop="costEstimation"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '是否已红冲'"
            align="center"
            label="是否已红冲"
            prop="costEstimation"
            show-overflow-tooltip
            width="140"
          />
          <el-table-column
            v-if="item.name === '付款集团本币金额'"
            align="center"
            label="付款集团本币金额"
            prop="costEstimation"
            show-overflow-tooltip
            width="180"
          />
          <el-table-column
            v-if="item.name === '交易对象类型'"
            align="center"
            label="交易对象类型"
            prop="costEstimation"
            show-overflow-tooltip
            width="180"
          />
          <el-table-column
            v-if="item.name === '供应商'"
            align="center"
            label="供应商"
            prop="costEstimation"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '客户'"
            align="center"
            label="客户"
            prop="costEstimation"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '单据状态'"
            align="center"
            label="单据状态"
            prop="costEstimation"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '生效状态'"
            align="center"
            label="生效状态"
            prop="costEstimation"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '付款交易类型'"
            align="center"
            label="付款交易类型"
            prop="costEstimation"
            show-overflow-tooltip
            width="160"
          />
          <el-table-column
            v-if="item.name === '付款全局本币金额'"
            align="center"
            label="付款全局本币金额"
            prop="costEstimation"
            show-overflow-tooltip
            width="180"
          />
          <el-table-column
            v-if="item.name === '集团本币汇率'"
            align="center"
            label="集团本币汇率"
            prop="costEstimation"
            show-overflow-tooltip
            width="160"
          />
          <el-table-column
            v-if="item.name === '全局本币汇率'"
            align="center"
            label="全局本币汇率"
            prop="costEstimation"
            show-overflow-tooltip
            width="160"
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
    getCashTransactionPage,
    getCashTransaction,
    createCashTransaction,
    updateCashTransaction,
    deleteCashTransaction,
    submitCashTransaction
  } from '@/api/globalTreasurer/xjgl'
  import Edit from './components/wtfkEdit.vue'
  import filterSearch from '@/components/filterSearch'
  import filterTable from '@/components/filterTable'
  import { searchTableMixis } from '@/mixis/index'

  export default {
    name: 'wtfk',
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
          { name: '结算财务组织' },
          { name: '单据编号' },
          { name: '单据日期' },
          { name: '币种' },
          { name: '付款原币金额' },
          { name: '付款组织本币金额' },
          { name: '业务流程' },
          { name: '部门' },
          { name: '业务员' },
          { name: '制单人' },
          { name: '是否已红冲' },
          { name: '付款集团本币金额' },
          { name: '交易对象类型' },
          { name: '供应商' },
          { name: '客户' },
          { name: '单据状态' },
          { name: '生效状态' },
          { name: '付款交易类型' },
          { name: '付款全局本币金额' },
          { name: '集团本币汇率' },
          { name: '全局本币汇率' },
        ], //所有表格项
        filedNow: [], //当前表格项
        searchAll: this.getFiled(), //所有搜索项
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        localKey: 'globalTreasurer-jsgl-wtfk-search',
        tableKey: 'globalTreasurer-jsgl-wtfk-list',
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
          { name: '结算财务组织', key: 'jsFinance' },
          { name: '单据编号', key: 'receiptsNo' },
          { name: '时间', key: 'day' },
          { name: '供应商', key: 'supplier' },
          { name: '单据状态', key: 'startStatus' },
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
        try {
          let { ...other } = this.queryForm
          const response = await getCashTransactionPage({
            ...other,
            orgId: this.$store.getters.orgId
          })

          // 使用与配置文件一致的成功状态码
          const successCodes = [200, 0, '200', '0', '1', 1, 2]
          if (successCodes.includes(response.code)) {
            // 支持多种数据结构格式
            if (response.data && response.data.records !== undefined) {
              // MyBatis Plus分页格式
              this.list = response.data.records || []
              this.total = response.data.total || 0
            } else if (response.data && response.data.tlist !== undefined) {
              // PageInfo格式
              this.list = response.data.tlist || []
              this.total = response.data.totalRecord || 0
            } else if (response.data && response.data.list !== undefined) {
              // 标准格式
              this.list = response.data.list || []
              this.total = response.data.total || 0
            } else if (response.data && Array.isArray(response.data)) {
              // 数组格式
              this.list = response.data || []
              this.total = response.data.length || 0
            } else {
              // 兜底处理
              this.list = []
              this.total = 0
            }
          } else {
            this.$message.error(response.message || '获取数据失败')
            this.list = []
            this.total = 0
          }
        } catch (error) {
          console.error('获取数据失败:', error)
          this.$message.error('获取数据失败，请检查网络连接')
          this.list = []
          this.total = 0
        } finally {
          this.listLoading = false
        }
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
</style>
