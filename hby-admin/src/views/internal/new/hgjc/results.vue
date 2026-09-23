<template>
  <div class="system-log-container">
    <div class="lr-layout">
      <div class="right">
        <vab-query-form>
          <el-card shadow="never">
            <vab-query-form-top-panel>
              <el-form
                ref="form"
                checkable
                :inline="true"
                label-width="0"
                :model="queryForm"
                @submit.native.prevent
              >
                <el-form-item
                  v-for="(item, index) in searchItem"
                  :key="index"
                  :prop="item.key"
                >
                  <el-input
                    v-model="queryForm.riskNumber"
                    clearable
                    :placeholder="$translateTitle('事件编号')"
                    v-if="item.name === '事件编号'"
                  />
                  <el-input
                    v-model="queryForm.riskName"
                    clearable
                    :placeholder="$translateTitle('事件名称')"
                    v-if="item.name === '事件名称'"
                  />
                  <!-- <el-date-picker
                    align="right"
                    end-placeholder="结束日期"
                    range-separator="至"
                    format="yyyy-MM-dd"
                    start-placeholder="开始日期"
                    type="daterange"
                    unlink-panels
                    v-model="queryForm.Date"
                    value-format="yyyy-MM-dd"
                    v-if="item.name === '日期'"
                  /> -->
                </el-form-item>
                <el-form-item>
                  <el-button
                    icon="el-icon-search"
                    native-type="submit"
                    type="primary"
                    @click="fetchData"
                  >
                    {{ $translateTitle('查询') }}
                  </el-button>
                </el-form-item>
                <el-form-item>
                  <el-button native-type="submit" @click="resetSearch">
                    {{ $translateTitle('重置') }}
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
                    :class="
                      searchMore ? 'search-more is-opened' : 'search-more'
                    "
                    @click="showMore"
                  >
                    <span>
                      {{
                        searchMore
                          ? $translateTitle('收起')
                          : $translateTitle('展开')
                      }}
                    </span>
                    <i class="el-icon-arrow-down"></i>
                  </span>
                </el-form-item>
              </el-form>
            </vab-query-form-top-panel>
          </el-card>
        </vab-query-form>

        <el-card shadow="never" class="secondCard">
          <vab-query-form-right-panel class="option-row">
            <!-- <el-tooltip
              class="item"
              effect="dark"
              content="表格筛选"
              placement="top"
            > -->
            <el-button
              type="success"
              style="margin-right: 8px"
              @click="handleAdd()"
            >
              {{ $translateTitle('新建') }}
            </el-button>
            <!-- <el-button
              type="primary"
              style="margin-right: 8px"
              @click="$refs.industryCopy.showEdit()"
            >
              从行业复制
            </el-button> -->
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
            <!-- </el-tooltip> -->
          </vab-query-form-right-panel>

          <el-table v-loading="listLoading" :data="list">
            <el-table-column
              align="center"
              :label="$translateTitle('事件编号')"
              prop="riskNumber"
            >
              <template #default="{ row }">
                <el-button type="text" @click="handleRead(row)">
                  {{ row.riskNumber }}
                </el-button>
              </template>
            </el-table-column>
            <div v-for="(item, index) in filedNow" :key="index">
              <el-table-column
                align="center"
                :label="$translateTitle('事件名称')"
                prop="riskName"
                show-overflow-tooltip
                v-if="item.name === '事件名称'"
              />
              <el-table-column
                align="center"
                :label="$translateTitle('报送部门')"
                prop="departmentName"
                show-overflow-tooltip
                v-if="item.name === '报送部门'"
              />
              <el-table-column
                align="center"
                :label="$translateTitle('报送日期')"
                prop="findTime"
                v-if="item.name === '报送日期'"
              />
              <el-table-column
                align="center"
                :label="$translateTitle('风险事件类别')"
                prop="riskType"
                show-overflow-tooltip
                v-if="item.name === '风险事件类别'"
              >
                <template slot-scope="{ row }">
                  <span>
                    {{ row.riskType == '1' ? '重大' : '一般' }}
                  </span>
                </template>
              </el-table-column>
              <el-table-column
                align="center"
                :label="$translateTitle('审批状态')"
                prop="state"
                v-if="item.name === '审批状态'"
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
            </div>
            <el-table-column align="center" :label="$translateTitle('操作')">
              <template slot-scope="{ row }">
                <el-button
                  type="text"
                  @click="handleEdit(row)"
                  :disabled="!!row.state"
                >
                  {{ $translateTitle('修改') }}
                </el-button>

                <el-dropdown style="margin-left: 10px" @command="handleCommand">
                  <el-button type="text">
                    {{ $translateTitle('更多') }}
                  </el-button>
                  <el-dropdown-menu slot="dropdown">
                    <el-dropdown-item>
                      <el-button
                        type="text"
                        @click.native="handleManage(row)"
                        :disabled="!row.state"
                      >
                        {{ $translateTitle('办理') }}
                      </el-button>
                    </el-dropdown-item>
                    <el-dropdown-item>
                      <el-button
                        type="text"
                        @click="handleApproval(row)"
                        :disabled="!!row.state"
                      >
                        {{ $translateTitle('提交审批') }}
                      </el-button>
                    </el-dropdown-item>
                    <el-dropdown-item>
                      <el-button
                        type="text"
                        @click="handleDelete(row)"
                        :disabled="!!row.state"
                      >
                        {{ $translateTitle('删除') }}
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
          class="pager"
          :current-page="queryForm.pageNumber"
          :layout="layout"
          :page-size="queryForm.pageSize"
          :total="total"
          @current-change="handleCurrentChange"
          @size-change="handleSizeChange"
        />
      </div>
    </div>
    <EventEdit ref="edit" @fetch-data="fetchData" :riskcatid="riskcatid" />
    <EventRead ref="read" />
    <ProcessList ref="process" @fetchData="fetchData" />
    <WfqdDeal ref="wfqddeal" />
  </div>
</template>

<script>
  import { deleteRisk, riskList } from '@/api/internal/new/plan'
  import TypeTree from '@/views/risk/components/TypeTree.vue'
  import EventEdit from './components/EventEdit.vue'
  import EventRead from './components/EventRead.vue'
  import { UTCformat } from '@/utils'
  import filterSearch from '@/components/filterSearch.vue'
  import filterTable from '@/components/filterTable.vue'
  import ProcessList from '@/views/contract/contractManage/components/ProcessList'
  import WfqdDeal from '@/views/msg/components/options/WfqdDeal'
  import { getFlowPkInfo } from '@/api/contract/manage'
  export default {
    name: 'Fillin',
    components: {
      TypeTree,
      EventEdit,
      EventRead,
      filterSearch,
      filterTable,
      ProcessList,
      WfqdDeal,
    },
    data() {
      return {
        list: [],
        listLoading: false,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          riskName: '',
          riskNumber: '',
          pageNumber: 1,
          pageSize: 20,
        },
        riskcatid: undefined,
        searchAll: this.getFiled(), //所有搜索项
        localKey: 'risk-event-eventBase-search',
        tableKey: 'risk-event-eventBase-list',
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        searchMore: true,
        filedAll: [
          { name: '事件名称' },
          { name: '报送部门' },
          { name: '报送日期' },
          { name: '审批状态' },
        ], //所有表格项
        filedNow: [],
        selectList: [],
      }
    },
    created() {
      this.searchNow = this.getFiled()
      this.searchItem = this.searchNow.slice(0, 4)
      this.initSearch()
      this.initTable()
      this.fetchData()
    },
    mounted() {
      this.$bus.on('updateMsg', (value) => {
        console.log('qwe')
        if (value == 0) {
          this.fetchData()
        }
      })
    },
    methods: {
      handleApproval(row) {
        //提交审批
        this.$refs['process'].save(84, row.id)
      },
      async handleManage(row) {
        const res = await getFlowPkInfo({
          formId: row.id,
          tableId: 84,
        })

        this.$refs.wfqddeal.show(res.data, false)
      },
      // 定义表单所有项
      getFiled() {
        let fields = [
          { name: '事件编号', key: 'code' },
          { name: '事件名称', key: 'name' },
          { name: '日期', key: 'Date' },
        ]
        return fields
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
                this.queryForm[x.key] = ''
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
      // 动态表格开始
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
      showMore() {
        this.searchMore = !this.searchMore
        if (this.searchMore) {
          this.searchItem = this.searchNow
        } else {
          this.searchItem = this.searchNow.slice(0, 4)
        }
      },
      select(info) {
        console.log('select', info, '===')
        this.riskcatid = info.riskcatid
        this.fetchData()
      },
      handleSizeChange(val) {
        console.log('handleSizeChange')
        this.queryForm.pageSize = val
        this.fetchData()
      },
      handleCurrentChange(val) {
        console.log('handleCurrentChange')
        this.queryForm.pageNumber = val
        this.fetchData()
      },
      queryData() {
        console.log('queryData')
        this.queryForm.pageNumber = 1
        this.fetchData()
      },
      resetSearch() {
        ;(this.queryForm = {
          riskName: '',
          riskNumber: '',
          pageNumber: 1,
          pageSize: 20,
        }),
          this.fetchData()
      },
      async fetchData() {
        this.listLoading = true
        const {
          data: { tlist, totalRecord },
        } = await riskList({
          ...this.queryForm,
        })

        this.list = tlist
        this.total = totalRecord
        this.listLoading = false
      },
      handleSendTo() {},
      handleCopyFrom() {},
      async handleAdd() {
        this.$refs['edit'].showEdit(null, 'add')
      },

      handleEdit(row) {
        this.$refs['edit'].showEdit(row, 'edit')
      },

      handleRead(row) {
        this.$refs['edit'].showEdit(row, 'detail')
      },
      handleDelete(row) {
        console.log(row, '删除====')
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg, code } = await deleteRisk({ id: row.id })
          if (code !== 0) {
            this.$baseMessage(msg, 'success', 'vab-hey-message-success')
          }
          await this.fetchData()
        })
      },
    },
  }
</script>
<style scoped lang="scss">
  .system-log-container {
    padding: 0 !important;
  }
  .secondCard {
    margin-top: -5px !important;
  }
  .lr-layout {
    background: #f6f8f9;
    display: flex;
  }

  .lr-layout > .left {
    width: 230px;
    max-width: 230px;
    overflow: hidden;
    border-right: 1px solid ghostwhite;
    margin-right: 10px;
    padding: 20px 10px 20px 20px;
    background: #ffffff;
  }
  .option-row {
    width: 100%;
    margin-bottom: 20px;
  }
  .pager {
    margin-bottom: 20px !important;
  }
</style>
