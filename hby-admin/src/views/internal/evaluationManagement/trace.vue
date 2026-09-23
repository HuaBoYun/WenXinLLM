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
                v-model="queryForm.assNumnber"
                clearable
                placeholder="评价项目编号"
                style="width: 140px; margin-right: 20px"
                v-if="item.name === '评价项目编号'"
              />

              <el-input
                v-model="queryForm.assName"
                clearable
                placeholder="评价项目名称"
                style="width: 140px; margin-right: 20px"
                v-if="item.name === '评价项目名称'"
              />

              <el-date-picker
                v-model="queryForm.date"
                clearable
                end-placeholder="结束时间"
                format="yyyy-MM-dd"
                range-separator="-"
                start-placeholder="开始时间"
                type="daterange"
                value-format="yyyy-MM-dd"
                style="width: 200px; margin-right: 10px"
                v-if="item.name === '时间'"
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
        <!--      <el-table-column type="selection" width="55" />-->
        <el-table-column
          align="center"
          label="评价项目编号"
          prop="assessid"
          #default="{ row }"
        >
          <el-button type="text" @click="handleRead(row, true)">
            {{ row.assessid }}
          </el-button>
        </el-table-column>
        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            align="center"
            label="评价项目名称"
            prop="assessname"
            v-if="item.name === '评价项目名称'"
          />
          <el-table-column
            align="center"
            label="评价模板"
            prop="templename"
            show-overflow-tooltip
            v-if="item.name === '评价模板'"
          >
            <template #default="{ row }">
              <el-button
                type="text"
                @click="$refs['EvaluateModel'].showEdit(row.asstemid)"
              >
                {{ row.templename }}
              </el-button>
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="评价期限"
            prop="startdate"
            v-if="item.name === '评价期限'"
          >
            <template #default="{ row }">
              {{ row.startdate }} - {{ row.enddate }}
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="发起日期"
            prop="assstartday"
            show-overflow-tooltip
            v-if="item.name === '发起日期'"
          />
          <el-table-column
            align="center"
            label="状态"
            prop="assstatus"
            show-overflow-tooltip
            v-if="item.name === '状态'"
          >
            <template #default="{ row }">
              {{ status[+row.assstatus] }}
            </template>
          </el-table-column>
        </div>

        <el-table-column
          align="center"
          label="操作"
          show-overflow-tooltip
          width="80"
        >
          <template #default="{ row }">
            <el-button type="text" @click="handleEdit(row)">跟踪</el-button>
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
    <TraceView ref="edit" @fetch-data="fetchData" />
    <EvaluateModel ref="EvaluateModel" />
    <ProjectView ref="read" @fetch-data="fetchData" />
  </div>
</template>

<script>
  import { getTraceList } from '@/api/internal/trace'
  import { doDelete } from '@/api/table'
  import filterSearch from '@/components/filterSearch.vue'
  import filterTable from '@/components/filterTable.vue'
  import EvaluateModel from '@/views/internal/evaluationManagement/components/EvaluateModel'
  import TraceView from '@/views/internal/evaluationManagement/components/TraceView'
  import ProjectView from '@/views/internal/evaluationManagement/components/ProjectView'

  export default {
    name: 'Trace',
    components: {
      TraceView,
      EvaluateModel,
      filterTable,
      filterSearch,
      ProjectView,
    },
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        status: ['', '已立项', '已启动', '已完成', '已完成'],
        total: 0,
        queryForm: {
          assName: '',
          assNumnber: '',
          pageNumber: 1,
          pageSize: 20,
        },
        filedAll: [
          { name: '评价项目名称' },
          { name: '评价模板' },
          { name: '评价期限' },
          { name: '发起日期' },
          { name: '状态' },
        ], //所有表格项
        filedNow: [],
        searchAll: this.getFiled(), //所有搜索项
        localKey: 'internal-evaluationManagement-trace-search',
        tableKey: 'internal-evaluationManagement-trace-list',
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
          { name: '评价项目编号', key: 'assNumnber' },
          { name: '评价项目名称', key: 'assName' },
          { name: '时间', key: 'Date' },
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
          assName: '',
          assNumnber: '',
          pageNumber: 1,
          pageSize: 20,
        }
        this.fetchData()
      },
      resetSearch() {
        this.resetQueryForm()
      },
      async fetchData() {
        this.listLoading = true
        const { date, ...other } = this.queryForm
        let startDate = ''
        let endDates = ''
        if (date) {
          startDate = date[0]
          endDates = date[1]
        }
        const {
          data: { pageBean },
        } = await getTraceList({ ...other, startDate, endDates })
        this.list = pageBean.records
        this.total = pageBean.total
        this.listLoading = false
      },
      handleAdd() {
        this.$refs['edit'].showEdit()
      },
      handleEdit(row) {
        this.$refs['edit'].showEdit(row)
      },
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg } = await doDelete({ ids: row.id })
          this.$baseMessage(msg, 'success', 'vab-hey-message-success')
          await this.fetchData()
        })
      },
      handleRead(row, type) {
        this.$refs['read'].showEdit(row, type)
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
