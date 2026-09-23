<template>
  <div class="system-log-container">
    <vab-query-form>
      <el-card shadow="never">
        <vab-query-form-top-panel>
          <el-form
            ref="form"
            :inline="true"
            label-width="0"
            :model="queryForm"
            @submit.native.prevent
          >
            <el-form-item v-for="(item, index) in searchItem" :key="index">
              <el-input
                v-model="queryForm.taskName"
                clearable
                placeholder="任务书名称"
                v-if="item.name === '任务书名称'"
              />
              <el-select
                v-model="queryForm.taskType"
                clearable
                placeholder="任务书类型"
                v-if="item.name === '任务书类型'"
              >
                <el-option label="勘察任务书" :value="1" />
                <el-option label="设计任务书" :value="2" />
                <el-option label="施工任务书" :value="3" />
                <el-option label="监理任务书" :value="4" />
              </el-select>
              <el-select
                v-model="queryForm.taskStatus"
                clearable
                placeholder="任务书状态"
                v-if="item.name === '任务书状态'"
              >
                <el-option label="草稿" :value="1" />
                <el-option label="待审核" :value="2" />
                <el-option label="已审核" :value="3" />
                <el-option label="已批准" :value="4" />
                <el-option label="执行中" :value="5" />
                <el-option label="已完成" :value="6" />
              </el-select>
              <el-input
                v-model="queryForm.taskLeaderName"
                clearable
                placeholder="任务负责人"
                v-if="item.name === '任务负责人'"
              />
              <el-input
                v-model="queryForm.projectId"
                clearable
                placeholder="项目ID"
                v-if="item.name === '项目ID'"
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
        </vab-query-form-top-panel>
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
            <el-button
              slot="reference"
              icon="el-icon-s-grid"
              class="biaoge"
              style="margin-bottom: 10px; margin-right: 10px"
            ></el-button>
          </el-popover>
        </el-tooltip>
        <el-button type="success" @click="handleAdd">新建任务书</el-button>
      </vab-query-form-right-panel>
      <el-table v-loading="listLoading" :data="list" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" />
        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            align="center"
            label="任务书名称"
            prop="taskName"
            min-width="200"
            v-if="item.name === '任务书名称'"
          >
            <template #default="{ row }">
              <el-button type="text" @click="handleDetail(row)">
                {{ row.taskName }}
              </el-button>
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="任务书类型"
            prop="taskType"
            width="120"
            v-if="item.name === '任务书类型'"
          >
            <template #default="{ row }">
              <el-tag :type="getTaskTypeType(row.taskType)">
                {{ getTaskTypeName(row.taskType) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="项目ID"
            prop="projectId"
            width="100"
            v-if="item.name === '项目ID'"
          />
          <el-table-column
            align="center"
            label="任务负责人"
            prop="taskLeaderName"
            width="120"
            v-if="item.name === '任务负责人'"
          />
          <el-table-column
            align="center"
            label="任务书状态"
            prop="taskStatus"
            width="100"
            v-if="item.name === '任务书状态'"
          >
            <template #default="{ row }">
              <el-tag :type="getTaskStatusType(row.taskStatus)">
                {{ getTaskStatusName(row.taskStatus) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="计划开始日期"
            prop="plannedStartDate"
            width="120"
            :formatter="formatDate"
            v-if="item.name === '计划开始日期'"
          />
          <el-table-column
            align="center"
            label="计划结束日期"
            prop="plannedEndDate"
            width="120"
            :formatter="formatDate"
            v-if="item.name === '计划结束日期'"
          />
          <el-table-column
            align="center"
            label="创建时间"
            prop="createTime"
            width="150"
            :formatter="formatDate"
            v-if="item.name === '创建时间'"
          />
        </div>
        <el-table-column align="center" label="操作" width="250" fixed="right">
          <template #default="{ row }">
            <el-button type="text" size="small" @click="handleDetail(row)">详情</el-button>
            <el-button type="text" size="small" @click="handleEdit(row)">编辑</el-button>
            <el-button
              type="text"
              size="small"
              @click="handleReview(row)"
              v-if="row.taskStatus === 2"
            >
              审核
            </el-button>
            <el-button
              type="text"
              size="small"
              @click="handleAcceptance(row)"
              v-if="row.taskStatus === 5"
            >
              验收
            </el-button>
            <el-button type="text" size="small" style="color: #f56c6c;" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-pagination
      background
      class="pagination"
      :current-page="queryForm.pageNum"
      :layout="layout"
      :page-size="queryForm.pageSize"
      :total="total"
      @current-change="handleCurrentChange"
      @size-change="handleSizeChange"
    />
    <TaskEdit ref="edit" @fetch-data="fetchData" />
    <TaskReview ref="review" @fetch-data="fetchData" />
    <TaskAcceptance ref="acceptance" @fetch-data="fetchData" />
  </div>
</template>

<script>
  import {
    getTaskAssignmentList,
    getTaskAssignmentById,
    deleteTaskAssignment
  } from '@/api/contract/task'
  import TaskEdit from './components/TaskEdit'
  import TaskReview from './components/TaskReview'
  import TaskAcceptance from './components/TaskAcceptance'
  import { formatDate } from '@/utils/index'
  import filterSearch from '@/components/filterSearch.vue'
  import filterTable from '@/components/filterTable.vue'

  export default {
    name: 'Task',
    components: {
      TaskEdit,
      TaskReview,
      TaskAcceptance,
      filterTable,
      filterSearch
    },
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          taskName: '',
          taskType: null,
          taskStatus: null,
          taskLeaderName: '',
          projectId: '',
          pageNum: 1,
          pageSize: 20,
        },
        multipleSelection: [], // 多选数据

        localKey: 'contract-task-search',
        tableKey: 'contract-task-list',
        searchNow: [],
        searchMore: true,
        searchItem: [],
        searchAll: this.getFiled(), //所有搜索项
        filedNow: [],
        filedAll: [
          { name: '任务书名称' },
          { name: '任务书类型' },
          { name: '项目ID' },
          { name: '任务负责人' },
          { name: '任务书状态' },
          { name: '计划开始日期' },
          { name: '计划结束日期' },
          { name: '创建时间' },
        ],
      }
    },
    created() {
      this.fetchData()
      //初始化表格&筛选
      this.searchNow = this.getFiled()
      this.searchItem = this.searchNow.slice(0, 4)
      this.initTable()
      this.initSearch()
    },
    methods: {
      // 动态筛选 动态表格 初始化数据&相关方法
      showMore() {
        this.searchMore = !this.searchMore

        if (this.searchMore) {
          this.searchItem = this.searchNow
        } else {
          this.searchItem = this.searchNow.slice(0, 4)
        }
      },
      getFiled() {
        let fields = [
          { name: '任务书名称', key: 'taskName' },
          { name: '任务书类型', key: 'taskType' },
          { name: '任务书状态', key: 'taskStatus' },
          { name: '任务负责人', key: 'taskLeaderName' },
          { name: '项目ID', key: 'projectId' },
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
        return formatDate(data)
      },
      /**
       * @description: 改变每一页请求数量
       * @param {*} val
       * @return {*}
       */
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      /**
       * @description: 跳转页数
       * @param {*} val
       * @return {*}
       */
      handleCurrentChange(val) {
        this.queryForm.pageNum = val
        this.fetchData()
      },
      resetQueryForm() {
        this.queryForm = {
          taskName: '',
          taskType: null,
          taskStatus: null,
          taskLeaderName: '',
          projectId: '',
          pageNum: 1,
          pageSize: 20,
        }
      },
      resetSearch() {
        this.resetQueryForm()
        this.fetchData()
      },
      queryData() {
        this.queryForm.pageNum = 1
        this.fetchData()
      },
      /**
       * @description: 数据请求
       * @return {*}
       */
      async fetchData() {
        this.listLoading = true
        try {
          const response = await getTaskAssignmentList(this.queryForm)
          if (response.code === 1) {
            // 修复数据结构访问：response.data是分页对象，list在其中
            this.list = response.data?.list || []
            this.total = response.data?.total || 0
          } else {
            this.$message.error(response.msg || '查询失败')
            this.list = []
            this.total = 0
          }
        } catch (error) {
          this.$message.error('查询失败：' + error.message)
          this.list = []
          this.total = 0
        }
        this.listLoading = false
      },
      handleAdd() {
        this.$refs['edit'].showEdit('add', null)
      },
      /**
       * @description: 打开详情表单弹框
       * @param {*} row 选择的行数据
       * @return {*}
       */
      async handleDetail(row) {
        try {
          const response = await getTaskAssignmentById(row.id)
          if (response.code === 1) {
            await this.$refs['edit'].showEdit('detail', response.data)
          } else {
            this.$message.error(response.msg || '获取详情失败')
          }
        } catch (error) {
          this.$message.error('获取详情失败：' + error.message)
        }
      },
      async handleEdit(row) {
        try {
          const response = await getTaskAssignmentById(row.id)
          if (response.code === 1) {
            await this.$refs['edit'].showEdit('edit', response.data)
          } else {
            this.$message.error(response.msg || '获取详情失败')
          }
        } catch (error) {
          this.$message.error('获取详情失败：' + error.message)
        }
      },
      // 审核处理
      async handleReview(row) {
        try {
          const response = await getTaskAssignmentById(row.id)
          if (response.code === 1) {
            await this.$refs['review'].showEdit(response.data)
          } else {
            this.$message.error(response.msg || '获取详情失败')
          }
        } catch (error) {
          this.$message.error('获取详情失败：' + error.message)
        }
      },
      // 验收处理
      async handleAcceptance(row) {
        try {
          const response = await getTaskAssignmentById(row.id)
          if (response.code === 1) {
            await this.$refs['acceptance'].showEdit(response.data)
          } else {
            this.$message.error(response.msg || '获取详情失败')
          }
        } catch (error) {
          this.$message.error('获取详情失败：' + error.message)
        }
      },
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          try {
            const response = await deleteTaskAssignment(row.id)
            if (response.code === 1) {
              this.$baseMessage('删除成功', 'success')
              this.fetchData()
            } else {
              this.$baseMessage(response.msg || '删除失败', 'error')
            }
          } catch (error) {
            this.$baseMessage('删除失败：' + error.message, 'error')
          }
        })
      },
      // 多选处理
      handleSelectionChange(selection) {
        this.multipleSelection = selection
      },
      // 获取任务书类型名称
      getTaskTypeName(type) {
        const typeMap = {
          1: '勘察任务书',
          2: '设计任务书',
          3: '施工任务书',
          4: '监理任务书'
        }
        return typeMap[type] || '未知'
      },
      // 获取任务书类型样式
      getTaskTypeType(type) {
        const typeMap = {
          1: 'primary',
          2: 'success',
          3: 'warning',
          4: 'info'
        }
        return typeMap[type] || 'info'
      },
      // 获取任务书状态名称
      getTaskStatusName(status) {
        const statusMap = {
          1: '草稿',
          2: '待审核',
          3: '已审核',
          4: '已批准',
          5: '执行中',
          6: '已完成'
        }
        return statusMap[status] || '未知'
      },
      // 获取任务书状态样式
      getTaskStatusType(status) {
        const typeMap = {
          1: 'info',
          2: 'warning',
          3: 'primary',
          4: 'success',
          5: 'success',
          6: 'success'
        }
        return typeMap[status] || 'info'
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
