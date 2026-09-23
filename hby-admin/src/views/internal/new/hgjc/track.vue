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
                v-model="queryForm.plannumber"
                clearable
                placeholder="计划编号"
                style="width: 140px; margin-right: 20px"
                v-if="item.name === '计划编号'"
              />

              <el-input
                v-model="queryForm.planname"
                clearable
                placeholder="计划名称"
                style="width: 140px; margin-right: 20px"
                v-if="item.name === '计划名称'"
              />

              <el-date-picker
                v-model="queryForm.time"
                clearable
                end-placeholder="开始时间"
                format="yyyy-MM-dd"
                range-separator="-"
                start-placeholder="开始时间"
                style="width: 200px; margin-right: 20px"
                type="daterange"
                value-format="yyyy-MM-dd"
                v-if="item.name === '时间'"
              />

              <el-select
                v-model="queryForm.status"
                clearable
                placeholder="请选择计划状态"
                style="width: 140px; margin-right: 20px"
                v-if="item.name === '计划状态'"
              >
                <el-option
                  v-for="item in statusList"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
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
      <vab-query-form-right-panel :span="24">
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
        <el-table-column align="center" label="计划编号" prop="plannumber">
          <template #default="{ row }">
            <el-button
              style="color: red"
              type="text"
              @click="handleDeatil(row)"
            >
              {{ row.plannumber }}
            </el-button>
          </template>
        </el-table-column>
        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            align="center"
            label="计划名称"
            prop="planname"
            v-if="item.name === '计划名称'"
          />
          <el-table-column
            align="center"
            label="计划编制部门"
            prop="planmadedep"
            v-if="item.name === '计划编制部门'"
          />
          <el-table-column
            align="center"
            label="被测机构"
            prop="testedorgs"
            v-if="item.name === '被测机构'"
          />
          <el-table-column
            align="center"
            label="计划开始时间"
            prop="starttime"
            show-overflow-tooltip
            :formatter="formatDate"
            v-if="item.name === '计划开始时间'"
          />
          <el-table-column
            align="center"
            label="计划结束时间"
            prop="endtime"
            show-overflow-tooltip
            :formatter="formatDate"
            v-if="item.name === '计划结束时间'"
          />
          <el-table-column
            align="center"
            label="创建人"
            prop="creator"
            v-if="item.name === '创建人'"
          ></el-table-column>
          <el-table-column
            align="center"
            label="创建时间"
            prop="createdTime"
            v-if="item.name === '创建时间'"
          ></el-table-column>
          <el-table-column
            align="center"
            label="状态"
            prop="planstatus"
            show-overflow-tooltip
            v-if="item.name === '状态'"
          />
        </div>

        <el-table-column
          align="center"
          label="操作"
          show-overflow-tooltip
          width="80"
        >
          <template #default="{ row }">
            <el-button type="text" @click="handleEdit(row)">任务跟踪</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-pagination
      class="pagination"
      background
      :current-page="queryForm.pageNumber"
      :layout="layout"
      :page-size="queryForm.pageSize"
      :total="total"
      @current-change="handleCurrentChange"
      @size-change="handleSizeChange"
    />
    <TrackView ref="edit" @fetch-data="fetchData" />
    <TestListInfo ref="testListInfo" />
  </div>
</template>

<script>
  // import { getList } from '@/api/systemLog'
  import { getList } from '@/api/internal/new/track'
  import { formatDay } from '@/utils/index'
  // import Track from "@/views/contract/execute/track";
  import filterSearch from '@/components/filterSearch.vue'
  import filterTable from '@/components/filterTable.vue'
  import TestListInfo from '@/views/internal/new/hgjc/components/TestListInfo'
  import TrackView from '@/views/internal/new/hgjc/components/TrackView'
  export default {
    name: 'Track',
    // components: {Track},
    components: { TrackView, TestListInfo, filterTable, filterSearch },
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          planname: '',
          plannumber: '',
          time: [],
          status: '',
          pageNumber: 1,
          pageSize: 20,
        },
        statusList: [
          {
            label: '未启动',
            value: '未启动',
          },
          {
            label: '已启动',
            value: '已启动',
          },
          {
            label: '执行中',
            value: '执行中',
          },
          {
            label: '已完成',
            value: '已完成',
          },
        ],
        filedAll: [
          { name: '计划名称' },
          { name: '计划编制部门' },
          { name: '被测机构' },
          { name: '计划开始时间' },
          { name: '计划结束时间' },
          { name: '创建人' },
        { name: '创建时间' },
          { name: '状态' },
        ], //所有表格项
        filedNow: [],
        searchAll: this.getFiled(), //所有搜索项
        localKey: 'internal-internalTest-track-search',
        tableKey: 'internal-internalTest-track-list',
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        searchMore: true,
      }
    },
    created() {
      this.fetchData()
      this.initTable() //初始化表格
      this.searchNow = this.getFiled()
      this.searchItem = this.searchNow.slice(0, 4)
      this.initSearch()
    },
    methods: {
      // 定义表单所有项
      getFiled() {
        let fields = [
          { name: '计划编号', key: 'plannumber' },
          { name: '计划名称', key: 'planname' },
          { name: '时间', key: 'time' },
          { name: '计划状态', key: 'status' },
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
      showMore() {
        this.searchMore = !this.searchMore
        console.log(this.searchMore)
        if (this.searchMore) {
          this.searchItem = this.searchNow
        } else {
          this.searchItem = this.searchNow.slice(0, 4)
        }
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
      formatDate(row, column) {
        // 获取单元格数据
        let data = row[column.property]
        return formatDay(data)
      },
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.fetchData()
      },
      resetQueryForm() {
        this.queryForm = {
          planname: '',
          plannumber: '',
          time: [],
          status: '',
          pageNumber: 1,
          pageSize: 20,
        }
        this.fetchData()
      },
      resetSearch() {
        this.resetQueryForm()
      },
      queryData() {
        this.queryForm.pageNumber = 1
        this.fetchData()
      },
      async fetchData() {
        const { time, ...other } = this.queryForm
        let starttime_max = ''
        let starttime_min = ''
        if (time.length > 0) {
          starttime_min = time[0]
          starttime_max = time[1]
        }
        this.listLoading = true
        const {
          data: {
            pageBean: { records, total },
          },
        } = await getList({ starttime_max, starttime_min, ...other })
        this.list = records
        this.total = total
        this.listLoading = false
      },
      handleDeatil(row) {
        this.$refs['testListInfo'].showEdit(row)
      },
      handleEdit(row) {
        this.$refs['edit'].showEdit(row)
      },
    },
  }
</script>
<style scoped lang="scss">
  .system-log-container {
    background: #f6f8f9 !important;
    padding: 0 !important;
  }
  .pagination {
    margin-bottom: 20px !important;
  }
  .secondCard {
    margin-top: -5px !important;
  }
</style>
