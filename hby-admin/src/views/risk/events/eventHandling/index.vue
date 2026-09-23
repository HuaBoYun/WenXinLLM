<template>
  <div class="system-log-container">
    <div class="lr-layout">
      <div class="left">
        <type-tree @select="select" />
      </div>
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
                    v-model="queryForm.code"
                    clearable
                    placeholder="事件编号"
                    v-if="item.name === '事件编号'"
                  />
                  <el-input
                    v-model="queryForm.name"
                    clearable
                    placeholder="事件名称"
                    v-if="item.name === '事件名称'"
                  />
                  <el-date-picker
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
                  <el-button native-type="submit" @click="fetchData('reset')">
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
                    :class="
                      searchMore ? 'search-more is-opened' : 'search-more'
                    "
                    @click="showMore"
                  >
                    <span>{{ searchMore ? '收起' : '展开' }}</span>
                    <i class="el-icon-arrow-down"></i>
                  </span>
                </el-form-item>
              </el-form>
            </vab-query-form-top-panel>
          </el-card>
        </vab-query-form>

        <el-card shadow="never" class="secondCard">
          <vab-query-form-right-panel class="option-row">
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
          </vab-query-form-right-panel>
          <el-table v-loading="listLoading" :data="list">
            <el-table-column
              align="center"
              label="事件编号"
              prop="riskeventcode"
            >
              <template #default="{ row }">
                <el-button type="text" @click="handleRead(row)">
                  {{ row.riskeventcode }}
                </el-button>
              </template>
            </el-table-column>

            <div v-for="(item, index) in filedNow" :key="index">
              <el-table-column
                align="center"
                label="事件名称"
                prop="riskeventname"
                show-overflow-tooltip
                v-if="item.name === '事件名称'"
              />
              <el-table-column
                align="center"
                label="发生部门"
                prop="occureddepartment"
                show-overflow-tooltip
                v-if="item.name === '发生部门'"
              />
              <el-table-column
                align="center"
                label="发生日期"
                prop="occureddate"
                v-if="item.name === '发生日期'"
              />
              <el-table-column
                align="center"
                label="损失事件定性类别"
                prop="losseventcategory"
                show-overflow-tooltip
                v-if="item.name === '损失事件定性类别'"
              />
            </div>
            <el-table-column align="center" label="操作" width="180">
              <template #default="{ row }">
                <el-button type="text" @click="handleEdit(row)">
                  发起整改
                </el-button>
                <el-button type="text" @click="handleView(row)">
                  查看已有整改
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
        <el-pagination
          background
          class="pager"
          :current-page="queryForm.pageNo"
          :layout="layout"
          :page-size="queryForm.pageSize"
          :total="total"
          @current-change="handleCurrentChange"
          @size-change="handleSizeChange"
        />
      </div>
    </div>
    <RectifyEdit ref="edit" />
    <RectifyList ref="list" />
    <EventRead ref="read" />
  </div>
</template>

<script>
  import { getRiskListById } from '@/api/risk/riskEvents'
  import { doDelete } from '@/api/table'
  import TypeTree from '@/views/risk/components/TypeTree.vue'
  import EventRead from '../eventBase/components/EventRead.vue'
  import RectifyEdit from './components/RectifyEdit.vue'
  import RectifyList from './components/RectifyList.vue'
  import { UTCformat } from '@/utils'

  import filterSearch from '@/components/filterSearch.vue'
  import filterTable from '@/components/filterTable.vue'

  export default {
    name: 'EventHandling',
    components: {
      TypeTree,
      RectifyEdit,
      RectifyList,
      EventRead,
      filterSearch,
      filterTable,
    },
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          code: '',
          name: '',
          pageNo: 1,
          pageSize: 20,
        },
        searchAll: this.getFiled(), //所有搜索项
        localKey: 'risk-event-eventHanding-search',
        tableKey: 'risk-event-eventHanding-list',
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        searchMore: true,
        filedAll: [
          { name: '事件名称' },
          { name: '发生部门' },
          { name: '发生日期' },
          { name: '损失事件定性类别' },
        ], //所有表格项
        filedNow: [],
      }
    },
    created() {
      this.fetchData()
      this.searchNow = this.getFiled()
      this.searchItem = this.searchNow.slice(0, 4)
      this.initSearch()
      this.initTable()
    },
    methods: {
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
        this.riskcatid = info.riskcatid
        this.fetchData()
      },
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      handleCurrentChange(val) {
        this.queryForm.pageNo = val
        this.fetchData()
      },
      queryData() {
        this.queryForm.pageNo = 1
        this.fetchData()
      },
      async fetchData(type) {
        this.listLoading = true
        if (type && type == 'reset') this.$refs['form'].resetFields()
        const { Date } = this.queryForm
        let startDate = ''
        let endDate = ''
        if (Date) {
          startDate = Date[0]
          endDate = Date[1]
        }
        delete this.queryForm.Date
        const {
          data: { page },
        } = await getRiskListById({
          ...this.queryForm,
          riskcatid: this.riskcatid,
          startDate,
          endDate,
        })
        page.records = page.records.map((v) => {
          v.discovereddate = UTCformat(v.discovereddate)
          v.occureddate = UTCformat(v.occureddate)
          return v
        })
        this.list = page.records
        this.total = page.total
        this.listLoading = false
      },
      handleView(row) {
        this.$refs['list'].showList(row)
      },
      handleEdit(row) {
        this.$refs['edit'].showEdit(row)
      },
      handleRead(row) {
        this.$refs['read'].showRead(row)
      },
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg } = await doDelete({ ids: row.id })
          this.$baseMessage(msg, 'success', 'vab-hey-message-success')
          await this.fetchData()
        })
      },
      sendModel() {
        this.$refs['sendModel'].showEdit()
      },
      send() {
        this.$refs['send'].showEdit()
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
