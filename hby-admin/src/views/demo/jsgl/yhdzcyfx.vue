<template>
  <div class="system-log-container">
    <vab-query-form>
      <el-tabs v-model="activeName" @tab-click="handleClick">
        <el-tab-pane label="已到账" name="blend"></el-tab-pane>
        <el-tab-pane label="未到账" name="audit"></el-tab-pane>
      </el-tabs>
      <el-card shadow="never">
        <vab-query-form-left-panel :span="24">
          <template v-if="activeName === 'blend'">
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
                  placeholder="对账账户"
                  v-if="item.name === '对账账户'"
                />
                <el-select
                  v-model="queryForm.supplier"
                  clearable
                  placeholder="对账日期"
                  v-if="item.name === '对账日期'"
                >
                  <el-option
                    v-for="item in reconciliationList"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  ></el-option>
                </el-select>
                <el-date-picker
                  v-model="queryForm.day"
                  type="daterange"
                  align="right"
                  unlink-panels
                  range-separator="至"
                  start-placeholder="开始日期"
                  end-placeholder="结束日期"
                  v-if="item.name === '日期'"
                ></el-date-picker>
                <el-input
                  v-model="queryForm.supplier"
                  clearable
                  placeholder="差异天数"
                  v-if="item.name === '差异天数'"
                />
                <el-select
                  v-model="queryForm.supplier"
                  clearable
                  placeholder="差异类型"
                  v-if="item.name === '差异类型'"
                >
                  <el-option
                    v-for="item in differenceList"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  ></el-option>
                </el-select>
                <el-input
                  v-model="queryForm.supplier"
                  clearable
                  placeholder="金额开始"
                  v-if="item.name === '金额开始'"
                />
                <el-input
                  v-model="queryForm.supplier"
                  clearable
                  placeholder="金额结束"
                  v-if="item.name === '金额结束'"
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
          </template>
          <template v-if="activeName === 'audit'">
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
                  placeholder="对账账户"
                  v-if="item.name === '对账账户'"
                />
                <el-date-picker
                  v-model="queryForm.day"
                  type="date"
                  align="right"
                  placeholder="截止日期"
                  v-if="item.name === '截止日期'"
                ></el-date-picker>
                <el-input
                  v-model="queryForm.supplier"
                  clearable
                  placeholder="未到达天数"
                  v-if="item.name === '未到达天数'"
                />
                <el-select
                  v-model="queryForm.supplier"
                  clearable
                  placeholder="未到达类型"
                  v-if="item.name === '未到达类型'"
                >
                  <el-option
                    v-for="item in notTypeList"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  ></el-option>
                </el-select>
                <el-input
                  v-model="queryForm.supplier"
                  clearable
                  placeholder="金额开始"
                  v-if="item.name === '金额开始'"
                />
                <el-input
                  v-model="queryForm.supplier"
                  clearable
                  placeholder="金额结束"
                  v-if="item.name === '金额结束'"
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
          </template>
        </vab-query-form-left-panel>
      </el-card>
    </vab-query-form>

    <template v-if="activeName === 'blend'">
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
        </el-button>
        <el-button type="primary">导出</el-button> -->
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
              v-if="item.name === '单据号/凭证号'"
              align="center"
              label="单据号/凭证号"
              prop="projectOrderName"
              show-overflow-tooltip
              width="140"
            />
            <el-table-column
              v-if="item.name === '币种'"
              align="center"
              label="币种"
              prop="sjlxName"
              show-overflow-tooltip
            ></el-table-column>
            <el-table-column
              v-if="item.name === '银行档案'"
              align="center"
              label="银行档案"
              prop="planYear"
              show-overflow-tooltip
            />
            <el-table-column
              v-if="item.name === '银行账户'"
              align="center"
              label="银行账户"
              prop="costEstimation"
              show-overflow-tooltip
            />
            <el-table-column
              v-if="item.name === '账簿'"
              align="center"
              label="账簿"
              prop="costEstimation"
              show-overflow-tooltip
            />
            <el-table-column
              v-if="item.name === '科目'"
              align="center"
              label="科目"
              prop="costEstimation"
              show-overflow-tooltip
            />
            <el-table-column
              v-if="item.name === '辅助核算'"
              align="center"
              label="辅助核算"
              prop="costEstimation"
              show-overflow-tooltip
            />
            <el-table-column
              v-if="item.name === '对账日期'"
              align="center"
              label="对账日期"
              prop="costEstimation"
              show-overflow-tooltip
              width="140"
            />
            <el-table-column
              v-if="item.name === '业务日期'"
              align="center"
              label="业务日期"
              prop="costEstimation"
              show-overflow-tooltip
            />
            <el-table-column
              v-if="item.name === '对账单日期'"
              align="center"
              label="对账单日期"
              prop="costEstimation"
              show-overflow-tooltip
              width="120"
            />
            <el-table-column
              v-if="item.name === '差异天数'"
              align="center"
              label="差异天数"
              prop="costEstimation"
              show-overflow-tooltip
            />
            <el-table-column
              v-if="item.name === '收款'"
              align="center"
              label="收款"
              prop="costEstimation"
              show-overflow-tooltip
            />
            <el-table-column
              v-if="item.name === '付款'"
              align="center"
              label="付款"
              prop="costEstimation"
              show-overflow-tooltip
            />
            <el-table-column
              v-if="item.name === '异常类型'"
              align="center"
              label="标识码"
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
    </template>

    <template v-if="activeName === 'audit'">
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
                :list="filedNotAll"
                :name="tableKey"
                @updateTableShow="notTable"
              />
              <el-button
                slot="reference"
                icon="el-icon-s-grid"
                class="biaoge"
                style="margin-bottom: 10px; margin-right: 10px"
              ></el-button>
            </el-popover>
          </el-tooltip>
        </vab-query-form-right-panel>
        <el-table v-loading="listLoading" :data="list">
          <el-table-column
            type="selection"
            width="55"
            align="center"
          ></el-table-column>
          <el-table-column
            align="center"
            label="对账账户"
            prop="qdcode"
            width="160"
          ></el-table-column>
          <div v-for="(item, index) in filedNot" :key="index">
            <el-table-column
              v-if="item.name === '财务组织'"
              align="center"
              label="财务组织"
              prop="projectOrderName"
              show-overflow-tooltip
              width="140"
            />
            <el-table-column
              v-if="item.name === '日期'"
              align="center"
              label="日期"
              prop="sjlxName"
              show-overflow-tooltip
            ></el-table-column>
            <el-table-column
              v-if="item.name === '单据号/凭证号'"
              align="center"
              label="单据号/凭证号"
              prop="planYear"
              show-overflow-tooltip
              width="140"
            />
            <el-table-column
              v-if="item.name === '币种'"
              align="center"
              label="币种"
              prop="costEstimation"
              show-overflow-tooltip
            />
            <el-table-column
              v-if="item.name === '银行档案'"
              align="center"
              label="银行档案"
              prop="costEstimation"
              show-overflow-tooltip
            />
            <el-table-column
              v-if="item.name === '银行账户'"
              align="center"
              label="银行账户"
              prop="costEstimation"
              show-overflow-tooltip
            />
            <el-table-column
              v-if="item.name === '账簿'"
              align="center"
              label="账簿"
              prop="costEstimation"
              show-overflow-tooltip
            />
            <el-table-column
              v-if="item.name === '科目'"
              align="center"
              label="科目"
              prop="costEstimation"
              show-overflow-tooltip
            />
            <el-table-column
              v-if="item.name === '辅助核算'"
              align="center"
              label="辅助核算"
              prop="costEstimation"
              show-overflow-tooltip
            />
            <el-table-column
              v-if="item.name === '收款'"
              align="center"
              label="收款"
              prop="costEstimation"
              show-overflow-tooltip
            />
            <el-table-column
              v-if="item.name === '付款'"
              align="center"
              label="付款"
              prop="costEstimation"
              show-overflow-tooltip
            />
            <el-table-column
              v-if="item.name === '未达天数'"
              align="center"
              label="未达天数"
              prop="costEstimation"
              show-overflow-tooltip
            />
            <el-table-column
              v-if="item.name === '未达类型'"
              align="center"
              label="未达类型"
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
    </template>

    <Edit ref="edit" @fetch-data="fetchData"></Edit>
  </div>
</template>

<script>
  import {
    implementPlanList,
    implementPlanDelete,
    fpzyksry,
  } from '@/oapi/audit/project'
  import Edit from './components/zjfkEdit.vue'
  import filterSearch from '@/components/filterSearch'
  import filterTable from '@/components/filterTable'
  import { searchTableMixis } from '@/mixis/index'

  export default {
    name: 'yhdzcyfx',
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
          { name: '单据号/凭证号' },
          { name: '币种' },
          { name: '银行档案' },
          { name: '银行账户' },
          { name: '辅助核算' },
          { name: '账簿' },
          { name: '科目' },
          { name: '辅助核算' },
          { name: '对账日期' },
          { name: '业务日期' },
          { name: '对账单日期' },
          { name: '差异天数' },
          { name: '收款' },
          { name: '付款' },
          { name: '异常类型' },
        ], //所有表格项
        filedNotAll: [
          { name: '对账账户' },
          { name: '财务组织' },
          { name: '日期' },
          { name: '单据号/凭证号' },
          { name: '币种' },
          { name: '银行档案' },
          { name: '银行账户' },
          { name: '账簿' },
          { name: '科目' },
          { name: '辅助核算' },
          { name: '收款' },
          { name: '付款' },
          { name: '未达天数' },
          { name: '未达类型' },
        ], //未到账所有表格项
        filedNow: [], //当前表格项
        filedNot: [], //未到账当前表格项
        searchAll: this.getFiled(), //所有搜索项
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        localKey: 'globalTreasurer-jsgl-yhdzcyfx-search',
        tableKey: 'globalTreasurer-jsgl-yhdzcyfx-list',
        searchMore: false,
        select: [],
        reconciliationList: [
          {
            value: '1',
            label: '业务日期',
          },
          {
            value: '2',
            label: '对账日期',
          },
        ],
        differenceList: [
          {
            value: '1',
            label: '收款',
          },
          {
            value: '2',
            label: '付款',
          },
        ],
        notTypeList: [
          {
            value: '1',
            label: '单位未达',
          },
          {
            value: '2',
            label: '银行未达',
          },
        ],
        activeName: 'blend',
        isCut: false,
      }
    },
    created() {
      this.fetchData()
      this.initTable()
      this.notTable()
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
      notTable() {
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
            this.filedNot = tempArr
          } else {
            this.filedNot = this.filedNotAll
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
          { name: '对账账户', key: 'supplier' },
          { name: '对账日期', key: 'supplier' },
          { name: '日期', key: 'day' },
          { name: '差异天数', key: 'receiptsNo' },
          { name: '差异类型', key: 'supplier' },
          { name: '金额开始', key: 'supplier' },
          { name: '金额结束', key: 'supplier' },
          { name: '截止日期', key: 'day' },
          { name: '未到达天数', key: 'supplier' },
          { name: '未到达类型', key: 'supplier' },
          { name: '结算方式', key: 'supplier' },
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
        this.activeName = tab.name
      },
      handleIsCut() {
        this.isCut = !this.isCut
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
  .cutTab {
    background: #fff;
    text-align: right;
    padding: 10px 15px 0 0;
  }
  .el-tabs {
    background: #fff;
    padding: 0 22px;
  }
  .el-tabs__header {
    margin: 0px;
  }
  .texture_box {
    display: flex;
    justify-content: space-between;
    text-align: center;
    padding: 10px 0;
    border-bottom: 2px solid #ccc;
  }
  .texture_item {
    width: 33.3%;
  }
  .table_box {
    display: flex;
    justify-content: space-between;
    width: 100%;
  }
  .child_table {
    width: 49%;
  }
  .child_title {
    text-align: center;
    margin: 10px 0;
  }
</style>
