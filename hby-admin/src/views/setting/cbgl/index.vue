<template>
  <div class="system-log-container">
    <!-- <vab-query-form>
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
                v-model="queryForm.reminderStaffName"
                placeholder="催办人"
              />
            </el-form-item>
            <el-form-item>
              <el-button
                icon="el-icon-search"
                type="primary"
                @click="queryData"
              >
                查询
              </el-button>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="fetchData('reset')">
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
        </vab-query-form-top-panel>
      </el-card>
    </vab-query-form> -->

    <el-card shadow="never" class="secondCard">
      <!-- <vab-query-form-right-panel class="option-row">
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
      </vab-query-form-right-panel> -->
      <!-- <el-table v-loading="listLoading" :data="list">
        <el-table-column
          align="center"
          label="催办人"
          prop="creatorName"
          #default="{ row }"
        >
          <el-button type="text" @click="handleDetail(row)">
            {{ row.creatorName }}
          </el-button>
        </el-table-column>

        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            align="center"
            label="催办时间"
            prop="createdTime"
            show-overflow-tooltip
            v-if="item.name === '催办时间'"
          />
          <el-table-column
            align="center"
            label="通知内容"
            prop="reminderContent"
            show-overflow-tooltip
            v-if="item.name === '通知内容'"
          />
        </div>
        <el-table-column align="center" label="" prop="" width="1" />
      </el-table> -->
      <div style="font-size: 18px; margin-bottom: 10px">催办通知</div>
      <el-tabs
        type="border-card"
        v-model="activeTab"
        @tab-click="handleTabClick"
      >
        <el-tab-pane :label="tabLabel2" name="first" style="overflow-y: scroll">
          <el-table
            border
            :data="list"
            style="width: 100%"
            ref="multipleTable"
            v-loading="listLoading"
          >
            <el-table-column align="center" label="序号" width="60">
              <template #default="{ $index }">
                {{ $index + 1 }}
              </template>
            </el-table-column>
            <el-table-column
              align="center"
              label="通知内容"
              prop="reminderContent"
              show-overflow-tooltip
              :formatter="formatDate"
            />
            <el-table-column
              align="center"
              label="类型"
              prop="typeName"
              show-overflow-tooltip
            />
            <el-table-column
              align="center"
              label="下发人"
              prop="creatorName"
            ></el-table-column>

            <el-table-column
              align="center"
              label="下发时间"
              prop="createdTime"
            ></el-table-column>
            <el-table-column
              align="center"
              label="操作"
              show-overflow-tooltip
              width="120"
            >
              <template #default="{ row }">
                <el-button type="text" @click="handleDetail(row)">
                  详情
                </el-button>
              </template>
            </el-table-column>
          </el-table>
          <el-pagination
            background
            class="pager"
            :current-page="queryForm.pageNumber"
            :layout="layout"
            :page-size="queryForm.pageSize"
            :page-sizes="pageSizes"
            :total="total"
            @current-change="handleCurrentChange"
            @size-change="handleSizeChange"
          />
        </el-tab-pane>
        <el-tab-pane
          :label="tabLabel1"
          name="second"
          style="overflow-y: scroll"
        >
          <el-table
            border
            :data="list1"
            style="width: 100%"
            ref="multipleTable"
            v-loading="listLoading1"
          >
            <el-table-column align="center" label="序号" width="60">
              <template #default="{ $index }">
                {{ $index + 1 }}
              </template>
            </el-table-column>
            <el-table-column
              align="center"
              label="通知内容"
              prop="reminderContent"
              show-overflow-tooltip
              :formatter="formatDate"
            />
            <el-table-column
              align="center"
              label="类型"
              prop="typeName"
              show-overflow-tooltip
            />
            <el-table-column
              align="center"
              label="下发人"
              prop="creatorName"
            ></el-table-column>

            <el-table-column
              align="center"
              label="下发时间"
              prop="createdTime"
            ></el-table-column>

            <el-table-column
              align="center"
              label="操作"
              show-overflow-tooltip
              width="120"
            >
              <template #default="{ row }">
                <el-button type="text" @click="handleDetail(row)">
                  详情
                </el-button>
              </template>
            </el-table-column>
          </el-table>

          <el-pagination
            background
            class="pager"
            :current-page="queryForm1.pageNumber"
            :layout="layout"
            :page-size="queryForm1.pageSize"
            :page-sizes="pageSizes"
            :total="total1"
            @current-change="handleCurrentChange1"
            @size-change="handleSizeChange1"
          />
        </el-tab-pane>
      </el-tabs>
    </el-card>

    <CBDetail ref="detail" @close="close"></CBDetail>
  </div>
</template>

<script>
  import { getFWlist } from '@/api/setting/report'
  import { formatDay } from '@/utils/index'
  import filterSearch from '@/components/filterSearch.vue'
  import filterTable from '@/components/filterTable.vue'
  import CBDetail from './components/CBdetail.vue'
  import { getHomeCBList1 } from '@/api/setting/report'
  export default {
    name: 'NormalReportList',
    components: {
      filterSearch,
      filterTable,
      CBDetail,
    },
    data() {
      return {
        list: [],
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        list1: [],
        total1: 0,
        queryForm: {
          pageNumber: 1,
          pageSize: 5,
          reminderStaffName: '',
        },
        queryForm1: {
          pageNumber: 1,
          pageSize: 5,
          reminderStaffName: '',
        },
        searchAll: this.getFiled(), //所有搜索项
        localKey: 'risk-cbgl-index-search',
        tableKey: 'risk-cbgl-index-list',
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        searchMore: true,
        filedAll: [{ name: '下发时间' }, { name: '通知内容' }], //所有表格项
        filedNow: [],
        btnLoading: false,
        moduleRoute: 'zzfx', //智能分线
        activeTab: 'first',
        tabLabel1: '已阅事项',
        tabLabel2: '未阅事项',
        pageSizes: [5, 10, 50, 100],
        listLoading1: false,
        listLoading: false,
      }
    },
    created() {
      this.fetchData(0)
      this.fetchData(1)
      this.searchNow = this.getFiled()
      this.searchItem = this.searchNow.slice(0, 4)
      this.initSearch()
      this.initTable()
    },
    /**
     * @description: 流程提交回调
     * @return {*}
     */
    mounted() {},
    /**
     * @description: 提交审批
     * @return {*}
     */
    methods: {
      handleAdd() {
        this.$refs['cuifa'].showEdit(null, false)
      },
      handleCBDetail(row) {
        this.$refs['cuifa'].showEdit(row, 'detail')
      },
      // 定义表单所有项
      getFiled() {
        let fields = [{ name: '催办人', key: 'reminderStaffName' }]
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
        console.log(this.searchMore)
        if (this.searchMore) {
          this.searchItem = this.searchNow
        } else {
          this.searchItem = this.searchNow.slice(0, 4)
        }
      },
      /**
       * @description: 下载附件
       * @return {*}
       */

      /**
       * @description: 分页
       * @return {*}
       */
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData(0)
      },
      /**
       * @description: 分页
       * @return {*}
       */
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.fetchData(0)
      },
      handleSizeChange1(val) {
        this.queryForm1.pageSize = val
        this.fetchData(1)
      },
      /**
       * @description: 分页
       * @return {*}
       */
      handleCurrentChange1(val) {
        this.queryForm1.pageNumber = val
        this.fetchData(1)
      },
      /**
       * @description: 分页，初始化
       * @return {*}
       */
      queryData() {
        this.queryForm.pageNumber = 1
        this.fetchData()
      },
      /**
       * @description: 重置数据
       * @return {*}
       */
      resetQueryForm() {
        this.queryForm = {
          pageNumber: 1,
          pageSize: 20,
          reminderStaffName: '',
        }
      },
      /**
       * @description: 重置
       * @return {*}
       */
      resetSearch() {
        this.resetQueryForm()
        this.fetchData()
      },
      reloadTable() {
        this.fetchData()
      },
      /**
       * @description: 获取数据
       * @return {*}
       */
      async fetchData(type) {
        this.listLoading = true
        this.listLoading1 = true
        const {
          data: { tlist, totalRecord },
        } = await getHomeCBList1({
          ...(type == 1 ? this.queryForm1 : this.queryForm),
          isRead: type,
        })
        if (type == '1') {
          this.listLoading1 = false
          this.list1 = tlist
          this.total1 = totalRecord
          this.tabLabel1 = '已阅事项' + '(' + totalRecord + ')'
        } else {
          this.listLoading = false
          this.list = tlist
          this.total = totalRecord
          this.tabLabel2 = '未阅事项' + '(' + totalRecord + ')'
        }
      },

      handleDetail(row) {
        this.$refs['detail'].showEdit(row)
      },
      handleTabClick(tab) {
        console.log(tab)
        if (tab.name === 'first') {
          this.queryForm.pageNumber = 1
          this.fetchData(0)
        } else {
          this.queryForm1.pageNumber = 1
          this.fetchData(1)
        }
      },
      close() {
        this.fetchData(0)
        this.fetchData(1)
      },
    },
  }
</script>
<style scoped lang="scss">
  .system-log-container {
    padding: 0 !important;
    background: #f6f8f9 !important;
  }

  .secondCard {
    margin-top: -5px !important;
  }

  .option-row {
    width: 100%;
    margin-bottom: 20px;
  }

  .pager {
    margin-bottom: 20px !important;
  }
  h5 {
    font-size: 18px;
    margin: 0 0 10px 0;
    color: #333;
  }

  .el-col > div {
    border: 1px solid #dcdfe5;
    margin-bottom: 10px;
  }

  .page {
    padding: 20px;
  }

  .highlight {
    color: red; /* 设置文字颜色为红色 */
    font-weight: bold; /* 加粗文字 */
  }
</style>
