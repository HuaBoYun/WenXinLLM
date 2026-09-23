<!-- 应收核销结果查询 -->
<template>
  <div class="system-log-container">
    <!-- <vab-query-form>
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
                placeholder="所属组织"
                v-if="item.name === '所属组织'"
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
                placeholder="收款财务组织"
                v-if="item.name === '收款财务组织'"
              />
              <el-input
                v-model="queryForm.supplier"
                clearable
                placeholder="付款财务组织"
                v-if="item.name === '付款财务组织'"
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
    </vab-query-form> -->

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
        <el-button type="primary" @click="handleEdit(false, false)">
          查询
        </el-button>
        <!-- <el-button type="primary">导出</el-button> -->
      </vab-query-form-right-panel>

      <div class="tab-header">
        <div
          v-for="(tab, index) in tabs"
          :key="index"
          :class="['tab-title', { active: currentTab === index }]"
          @click="changeTab(index)"
        >
          {{ tab.title }}
        </div>
      </div>

      <template v-if="currentTab === 0">
        <el-table v-loading="listLoading" :data="list">
          <el-table-column
            align="center"
            label="序号"
            prop="qdcode"
            width="100"
          ></el-table-column>
          <div v-for="(item, index) in filedNow" :key="index">
            <el-table-column
              v-if="item.name === '主组织'"
              align="center"
              label="主组织"
              prop="projectOrderName"
              show-overflow-tooltip
            />
            <el-table-column
              v-if="item.name === '摘要'"
              align="center"
              label="摘要"
              prop="projectOrderName"
              show-overflow-tooltip
            />
            <el-table-column
              v-if="item.name === '处理日期'"
              align="center"
              label="处理日期"
              prop="projectOrderName"
              show-overflow-tooltip
            />
            <el-table-column
              v-if="item.name === '处理编号'"
              align="center"
              label="处理编号"
              prop="projectOrderName"
              show-overflow-tooltip
            />
            <el-table-column
              v-if="item.name === '借方币种'"
              align="center"
              label="借方币种"
              prop="sjlxName"
              show-overflow-tooltip
            ></el-table-column>
            <el-table-column
              v-if="item.name === '借方处理原币金额'"
              align="center"
              label="借方处理原币金额"
              prop="planYear"
              show-overflow-tooltip
            />
            <el-table-column
              v-if="item.name === '贷方处理原币金额'"
              align="center"
              label="贷方处理原币金额"
              prop="planYear"
              show-overflow-tooltip
            />
            <el-table-column
              v-if="item.name === '贷方处理本币金额'"
              align="center"
              label="贷方处理本币金额"
              prop="planYear"
              show-overflow-tooltip
            />
            <el-table-column
              v-if="item.name === '借方处理数量'"
              align="center"
              label="借方处理数量"
              prop="planYear"
              show-overflow-tooltip
            />
            <el-table-column
              v-if="item.name === '贷方处理数量'"
              align="center"
              label="贷方处理数量"
              prop="planYear"
              show-overflow-tooltip
            />
            <el-table-column
              v-if="item.name === '处理标志'"
              align="center"
              label="处理标志"
              prop="planYear"
              show-overflow-tooltip
            />
            <el-table-column
              v-if="item.name === '处理人'"
              align="center"
              label="处理人"
              prop="planYear"
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
      </template>

      <template v-if="currentTab === 1">
        <el-table v-loading="listLoading" :data="list">
          <el-table-column
            align="center"
            label="序号"
            prop="qdcode"
            width="100"
          ></el-table-column>
          <el-table-column
            align="center"
            label="主组织"
            prop="projectOrderName"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="摘要"
            prop="projectOrderName"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="处理日期"
            prop="projectOrderName"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="处理批次号"
            prop="projectOrderName"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="核销汇率"
            prop="sjlxName"
            show-overflow-tooltip
          ></el-table-column>
          <el-table-column
            align="center"
            label="单据大类"
            prop="sjlxName"
            show-overflow-tooltip
          ></el-table-column>
          <el-table-column
            align="center"
            label="单据编号"
            prop="sjlxName"
            show-overflow-tooltip
          ></el-table-column>
          <el-table-column
            align="center"
            label="对应单据大类"
            prop="sjlxName"
            show-overflow-tooltip
          ></el-table-column>
          <el-table-column
            align="center"
            label="对应单据编号"
            prop="sjlxName"
            show-overflow-tooltip
          ></el-table-column>
          <el-table-column
            align="center"
            label="借方处理原币金额"
            prop="planYear"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="借方处理本币金额"
            prop="planYear"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="借方处理数量"
            prop="planYear"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="贷方处理原币金额"
            prop="planYear"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="贷方处理本币金额"
            prop="planYear"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="贷方处理数量"
            prop="planYear"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="处理人"
            prop="planYear"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="红冲标记"
            prop="planYear"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="处理标志"
            prop="planYear"
            show-overflow-tooltip
          />
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
      </template>
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
  import Edit from './components/khzzbEdit'
  import filterSearch from '@/components/filterSearch'
  import filterTable from '@/components/filterTable'
  import { searchTableMixis } from '@/mixis/index'

  export default {
    name: 'yshxjgcx',
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
          startStatus: undefined,
          sxName: undefined,
          pageNumber: 1,
          pageSize: 10,
          xmnd: '',
        },
        currProjectId: '',
        filedAll: [
          { name: '主组织' },
          { name: '摘要' },
          { name: '处理日期' },
          { name: '处理编号' },
          { name: '借方币种' },
          { name: '借方处理原币金额' },
          { name: '贷方处理原币金额' },
          { name: '贷方处理本币金额' },
          { name: '借方处理数量' },
          { name: '贷方处理数量' },
          { name: '处理标志' },
          { name: '处理人' },
        ], //所有表格项
        filedNow: [], //当前表格项
        searchAll: this.getFiled(), //所有搜索项
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        localKey: 'cwgx-ysgl-yshxjgcx-search',
        tableKey: 'cwgx-ysgl-yshxjgcx-list',
        searchMore: false,
        select: [],
        currentTab: 0, // 当前选中的tab索引
        tabs: [{ title: '核销汇总' }, { title: '核销明细' }],
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
      changeTab(index) {
        this.currentTab = index
      },
      getFiled() {
        return [
          { name: '所属组织', key: 'sxName' },
          { name: '时间', key: 'startStatus' },
          { name: '收款财务组织', key: 'startStatus1' },
          { name: '付款财务组织', key: 'startStatus2' },
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
        this.listLoading = false
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
    },
  }
</script>

<style scoped lang="scss">
  .system-log-container {
    padding: 0 !important;
    background: #f6f8f9 !important;
  }
  .margin-b0 {
    margin-bottom: 0;
  }

  .tab-header {
    width: 150px;
    display: flex;
    margin-bottom: 10px;
  }

  .tab-title {
    margin-right: 10px;
    cursor: pointer;
    transition: all 0.3s;
  }

  .tab-title:hover {
    background-color: #f5f5f5;
  }

  .tab-title.active {
    color: #000;
    border-bottom: 2px solid red;
  }

  .tab-content {
    padding: 20px;
    min-height: 200px;
  }
</style>
