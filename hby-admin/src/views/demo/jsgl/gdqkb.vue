<template>
  <div class="system-log-container">
    <vab-query-form>
      <div class="cutTab">
        <el-button type="success" @click="handleIsCut">切换</el-button>
      </div>
      <el-tabs v-model="activeName" @tab-click="handleClick">
        <el-tab-pane label="勾兑情况" name="blend"></el-tab-pane>
        <el-tab-pane label="长期未达账审计" name="audit"></el-tab-pane>
        <el-tab-pane label="核销情况" name="condition"></el-tab-pane>
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
                  placeholder="勾对情况"
                  v-if="item.name === '勾对情况'"
                />
                <el-input
                  v-model="queryForm.supplier"
                  clearable
                  placeholder="单位日记账方向"
                  v-if="item.name === '单位日记账方向'"
                />
                <el-input
                  v-model="queryForm.supplier"
                  clearable
                  placeholder="结算方式"
                  v-if="item.name === '结算方式'"
                />
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
          <template v-if="activeName === 'condition'">
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
                  type="daterange"
                  align="right"
                  unlink-panels
                  range-separator="至"
                  start-placeholder="开始日期"
                  end-placeholder="结束日期"
                  v-if="item.name === '日期'"
                ></el-date-picker>
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
                  placeholder="日期"
                  v-if="item.name === '日期'"
                ></el-date-picker>
                <el-input
                  v-model="queryForm.supplier"
                  clearable
                  placeholder="至截止日期大于等未达天数"
                  v-if="item.name === '至截止日期大于等未达天数'"
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

    <el-card shadow="never" class="secondCard">
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
            <i class="el-icon-delete" slot="reference"></i>
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
        <el-button type="primary">导出</el-button>
      </vab-query-form-right-panel> -->
      <div class="texture_box">
        <div class="texture_item">
          <span>财务组织:</span>
        </div>
        <div class="texture_item">
          <span>对账账户:</span>
        </div>
        <div class="texture_item">
          <span>银行账户:</span>
        </div>
      </div>
      <div :class="{ table_box: !isCut, table_box_cut: isCut }">
        <div :class="{ child_table: !isCut }">
          <div class="child_title">单位日记账</div>
          <el-table v-loading="listLoading" :data="list">
            <el-table-column
              type="selection"
              width="55"
              align="center"
            ></el-table-column>
            <el-table-column
              align="center"
              label="日期"
              prop="qdcode"
              width="160"
            ></el-table-column>
            <div v-for="(item, index) in filedNow" :key="index">
              <el-table-column
                v-if="item.name === '财务组织'"
                align="center"
                label="财务组织"
                prop="projectOrderName"
                show-overflow-tooltip
              />
              <el-table-column
                v-if="item.name === '账簿'"
                align="center"
                label="账簿"
                prop="sjlxName"
                show-overflow-tooltip
              ></el-table-column>
              <el-table-column
                v-if="item.name === '科目'"
                align="center"
                label="科目"
                prop="planYear"
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
                v-if="item.name === '摘要'"
                align="center"
                label="摘要"
                prop="costEstimation"
                show-overflow-tooltip
              />
              <el-table-column
                v-if="item.name === '单据号/凭证号'"
                align="center"
                label="单据号/凭证号"
                prop="costEstimation"
                show-overflow-tooltip
                width="140"
              />
              <el-table-column
                v-if="item.name === '票据日期'"
                align="center"
                label="票据日期"
                prop="costEstimation"
                show-overflow-tooltip
                width="120"
              />
              <el-table-column
                v-if="item.name === '结算方式'"
                align="center"
                label="结算方式"
                prop="costEstimation"
                show-overflow-tooltip
                width="140"
              />
              <el-table-column
                v-if="item.name === '票据号'"
                align="center"
                label="票据号"
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
                v-if="item.name === '两清'"
                align="center"
                label="两清"
                prop="costEstimation"
                show-overflow-tooltip
              />
              <el-table-column
                v-if="item.name === '批次号'"
                align="center"
                label="批次号"
                prop="costEstimation"
                show-overflow-tooltip
              />
              <el-table-column
                v-if="item.name === '标识码'"
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
          <el-pagination
            background
            :current-page="queryForm.pageNumber"
            :layout="layout"
            :page-size="queryForm.pageSize"
            :total="total"
            @current-change="handleCurrentChange"
            @size-change="handleSizeChange"
          />
        </div>

        <div :class="{ child_table: !isCut }">
          <div class="child_title">银行对账单</div>
          <el-table v-loading="listLoading" :data="list">
            <el-table-column
              type="selection"
              width="55"
              align="center"
            ></el-table-column>
            <el-table-column
              align="center"
              label="日期"
              prop="qdcode"
              width="160"
            ></el-table-column>
            <div v-for="(item, index) in filedNow" :key="index">
              <el-table-column
                v-if="item.name === '摘要'"
                align="center"
                label="摘要"
                prop="projectOrderName"
                show-overflow-tooltip
              />
              <el-table-column
                v-if="item.name === '结算方式'"
                align="center"
                label="结算方式"
                prop="sjlxName"
                show-overflow-tooltip
              ></el-table-column>
              <el-table-column
                v-if="item.name === '票据号'"
                align="center"
                label="票据号"
                prop="planYear"
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
                v-if="item.name === '两清'"
                align="center"
                label="两清"
                prop="costEstimation"
                show-overflow-tooltip
                width="120"
              />
              <el-table-column
                v-if="item.name === '批次号'"
                align="center"
                label="批次号"
                prop="costEstimation"
                show-overflow-tooltip
                width="120"
              />
              <el-table-column
                v-if="item.name === '标识码'"
                align="center"
                label="标识码"
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
                    </el-dropdown-item>
                  </el-dropdown-menu>
                </el-dropdown>
              </template>
            </el-table-column>
          </el-table>
          <el-pagination
            background
            :current-page="queryForm.pageNumber"
            :layout="layout"
            :page-size="queryForm.pageSize"
            :total="total"
            @current-change="handleCurrentChange"
            @size-change="handleSizeChange"
          />
        </div>
      </div>
    </el-card>

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
    name: 'gdqkb',
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
          { name: '日期' },
          { name: '财务组织' },
          { name: '账簿' },
          { name: '科目' },
          { name: '辅助核算' },
          { name: '摘要' },
          { name: '单据号/凭证号' },
          { name: '票据日期' },
          { name: '结算方式' },
          { name: '票据号' },
          { name: '收款' },
          { name: '付款' },
          { name: '两清' },
          { name: '批次号' },
          { name: '标识码' },
        ], //所有表格项
        filedNow: [], //当前表格项
        searchAll: this.getFiled(), //所有搜索项
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        localKey: 'globalTreasurer-jsgl-gdqkb-search',
        tableKey: 'globalTreasurer-jsgl-gdqkb-list',
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
        activeName: 'blend',
        isCut: false,
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
          { name: '财务组织', key: 'jsFinance' },
          { name: '对账账户', key: 'supplier' },
          { name: '勾对情况', key: 'supplier' },
          { name: '日期', key: 'day' },
          { name: '至截止日期大于等未达天数', key: 'receiptsNo' },
          { name: '单位日记账方向', key: 'supplier' },
          { name: '结算方式', key: 'supplier' },
          { name: '金额开始', key: 'supplier' },
          { name: '金额结束', key: 'supplier' },
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
