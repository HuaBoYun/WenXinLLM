<template>
  <div class="system-log-container">
    <el-card shadow="never">
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
      <el-table
        v-loading="listLoading"
        :data="list"
        :default-sort="{ prop: 'plannumber', order: 'descending' }"
      >
        <el-table-column
          align="center"
          label="方案编号"
          prop="plannumber"
          #default="{ row }"
        >
          <el-button style="color: red" type="text" @click="handleDeatil(row)">
            {{ row.plannumber }}
          </el-button>
        </el-table-column>
        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            align="center"
            label="方案名称"
            prop="planname"
            v-if="item.name === '方案名称'"
          />
          <el-table-column
            align="center"
            label="方案指定部门"
            prop="planmadedep"
            v-if="item.name === '方案指定部门'"
          />
          <el-table-column
            align="center"
            label="被测机构"
            prop="testedorgs"
            v-if="item.name === '被测机构'"
            show-overflow-tooltip
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
            <el-button type="text" @click="handleEdit(row)">查看</el-button>
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
    <TaskView ref="edit" @fetch-data="fetchData" />
    <PlanView ref="planDetail" />
  </div>
</template>

<script>
  // import { getList } from '@/api/systemLog'
  import { controlTestImplList } from '@/api/internal/tack'
  import filterTable from '@/components/filterTable.vue'
  import { formatDay } from '@/utils/index'
  import TaskView from '@/views/internal/internalTest/components/TaskView'
  import PlanView from '@/views/internal/internalTest/components/PlanView'
  export default {
    name: 'Task',
    components: { TaskView, PlanView, filterTable },
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          code: '',
          name: '',
          pageNumber: 1,
          pageSize: 20,
        },
        filedAll: [
          { name: '方案名称' },
          { name: '方案指定部门' },
          { name: '被测机构' },
          { name: '计划开始时间' },
          { name: '计划结束时间' },
          { name: '状态' },
        ], //所有表格项
        filedNow: [],
        tableKey: 'internal-internalTest-task-list',
      }
    },
    created() {
      this.fetchData()
      this.initTable() //初始化表格
    },
    methods: {
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
      queryData() {
        this.queryForm.pageNumber = 1
        this.fetchData()
      },
      async fetchData() {
        this.listLoading = true
        const {
          data: {
            pageBean: { records, total },
          },
        } = await controlTestImplList(this.queryForm)
        this.list = records
        this.total = total
        this.listLoading = false
      },
      handleEdit(row) {
        this.$refs['edit'].showEdit(row)
      },
      handleDeatil(row) {
        this.$refs['planDetail'].showEdit(row, true)
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
