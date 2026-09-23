<template>
  <div class="system-log-container">
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
                placeholder="计划编号"
                v-if="item.name === '计划编号'"
              />
              <el-input
                v-model="queryForm.name"
                clearable
                placeholder="计划名称"
                v-if="item.name === '计划名称'"
              />
              <el-date-picker
                v-model="queryForm.time"
                end-placeholder="自评完成时间"
                range-separator="至"
                start-placeholder="自评完成时间"
                type="daterange"
                value-format="yyyy-MM-dd"
                format="yyyy-MM-dd"
                v-if="item.name === '完成时间'"
              />
              <el-select
                v-model="queryForm.status"
                clearable
                placeholder="评估状态"
                v-if="item.name === '评估状态'"
              >
                <el-option
                  v-for="item in statusOptions"
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
                type="primary"
                native-type="submit"
                @click="fetchData('reset')"
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
        <el-table-column align="center" label="计划编号" prop="plancode">
          <template #default="{ row }">
            <el-button type="text" @click="handleRead(row)">
              {{ row.plancode }}
            </el-button>
          </template>
        </el-table-column>

        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            align="center"
            label="计划名称"
            prop="planName"
            show-overflow-tooltip
            v-if="item.name === '计划名称'"
          />
          <!-- <el-table-column align="center" label="制定机构" prop="data" /> -->
          <el-table-column
            align="center"
            label="开始时间"
            prop="startDate"
            show-overflow-tooltip
            v-if="item.name === '开始时间'"
          />
          <el-table-column
            align="center"
            label="预计完成时间"
            prop="endDate"
            show-overflow-tooltip
            v-if="item.name === '预计完成时间'"
          />
          <el-table-column
            align="center"
            label="已评估情况"
            prop="ypgStr"
            show-overflow-tooltip
            v-if="item.name === '已评估情况'"
          />
          <el-table-column
            align="center"
            label="未评估情况"
            prop="wpgStr"
            show-overflow-tooltip
            v-if="item.name === '未评估情况'"
          />

          <el-table-column
            align="center"
            label="状态"
            prop="status"
            show-overflow-tooltip
            v-if="item.name === '状态'"
          ></el-table-column>
        </div>
        <el-table-column align="center" label="操作" width="100">
          <template #default="{ row }">
            <el-button
              type="text"
              @click="handleEdit(row)"
              v-if="row.status !== '已评估'"
            >
              评估
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
    <TaskEdit ref="edit" @fetch-data="fetchData" />
    <PlanRead ref="read" />
  </div>
</template>

<script>
  import { getRiresultList } from '@/api/systemLog'
  import { doDelete } from '@/api/table'
  import filterSearch from '@/components/filterSearch.vue'
  import filterTable from '@/components/filterTable.vue'

  export default {
    name: 'TaskList',
    components: {
      TaskEdit: () => import('./components/TaskEdit.vue'),
      PlanRead: () => import('./components/PlanRead.vue'),
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
          pageNo: 1,
          pageSize: 20,
          code: undefined,
          name: undefined,
          time: undefined,
          status: undefined,
        },
        typeOptions: [
          {
            label: '年度计划',
            value: 1,
          },
          {
            label: '临时性计划',
            value: 2,
          },
        ],
        statusOptions: [
          {
            label: '未评估',
            value: 0,
          },
          {
            label: '评估中',
            value: 1,
          },
          {
            label: '已评估',
            value: 2,
          },
        ],
        searchAll: this.getFiled(), //所有搜索项
        localKey: 'risk-assessment-task-search',
        tableKey: 'risk-assessment-task-list',
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        searchMore: true,
        filedAll: [
          { name: '计划名称' },
          { name: '开始时间' },
          { name: '预计完成时间' },
          { name: '已评估情况' },
          { name: '未评估情况' },
          { name: '状态' },
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
          { name: '计划编号', key: 'code' },
          { name: '计划名称', key: 'name' },
          { name: '完成时间', key: 'time' },
          { name: '评估状态', key: 'status' },
        ]
        return fields
      },
      /**
       * @description: 初始化，搜索
       * @return {*}
       */
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
      /**
       * @description: 字典
       * @return {*}
       */
      taskStatus(v) {
        const arr = ['未开始', '评估中', '已完成']
        return arr[v - 1]
      },
      /**
       * @description: 分页
       * @return {*}
       */
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      /**
       * @description: 分页
       * @return {*}
       */
      handleCurrentChange(val) {
        this.queryForm.pageNo = val
        this.fetchData()
      },
      /**
       * @description: 分页，初始化
       * @return {*}
       */
      queryData() {
        this.queryForm.pageNo = 1
        this.fetchData()
      },
      /**
       * @description: 获取数据
       * @return {*}
       */
      async fetchData(type) {
        this.listLoading = true
        if (type && type == 'reset') this.$refs['form'].resetFields()
        if (this.queryForm.time) {
          this.queryForm.startDate = this.queryForm.time[0]
          this.queryForm.endDate = this.queryForm.time[1]
        } else {
          delete this.queryForm.startDate
          delete this.queryForm.endDate
        }
        const { time, ...data } = this.queryForm

        const {
          data: { pageBean },
        } = await getRiresultList(data)
        this.list = pageBean.list
        this.total = pageBean.total
        this.listLoading = false
      },
      /**
       * @description: 打开编辑
       * @return {*}
       */
      handleEdit(row) {
        console.log('r1r', this.$refs['edit'])

        this.$refs['edit'].showEdit(row)
      },
      /**
       * @description: 打开详情
       * @return {*}
       */
      handleRead(row) {
        this.$refs['read'].showRead(row)
      },
      /**
       * @description: 删除
       * @return {*}
       */
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg } = await doDelete({ ids: row.id })
          this.$baseMessage(msg, 'success', 'vab-hey-message-success')
          await this.fetchData()
        })
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
</style>
