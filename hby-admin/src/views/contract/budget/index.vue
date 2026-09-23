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
                v-model="queryForm.budgetName"
                clearable
                placeholder="预算名称"
                v-if="item.name === '预算名称'"
              />
              <el-select
                v-model="queryForm.budgetType"
                clearable
                placeholder="预算类型"
                v-if="item.name === '预算类型'"
              >
                <el-option label="初步预算" :value="1" />
                <el-option label="详细预算" :value="2" />
                <el-option label="执行预算" :value="3" />
              </el-select>
              <el-select
                v-model="queryForm.budgetStatus"
                clearable
                placeholder="预算状态"
                v-if="item.name === '预算状态'"
              >
                <el-option label="草稿" :value="1" />
                <el-option label="待审批" :value="2" />
                <el-option label="已审批" :value="3" />
                <el-option label="已批准" :value="4" />
                <el-option label="执行中" :value="5" />
              </el-select>
              <el-input
                v-model="queryForm.budgeterName"
                clearable
                placeholder="预算编制人"
                v-if="item.name === '预算编制人'"
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
        <el-button type="success" @click="handleAdd">新建预算</el-button>
      </vab-query-form-right-panel>
      <el-table v-loading="listLoading" :data="list" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" />
        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            align="center"
            label="预算名称"
            prop="budgetName"
            min-width="200"
            v-if="item.name === '预算名称'"
          >
            <template #default="{ row }">
              <el-button type="text" @click="handleDetail(row)">
                {{ row.budgetName }}
              </el-button>
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="预算类型"
            prop="budgetType"
            width="100"
            v-if="item.name === '预算类型'"
          >
            <template #default="{ row }">
              <el-tag :type="getBudgetTypeType(row.budgetType)">
                {{ getBudgetTypeName(row.budgetType) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="预算版本"
            prop="budgetVersion"
            width="100"
            v-if="item.name === '预算版本'"
          />
          <el-table-column
            align="center"
            label="总预算"
            prop="totalBudget"
            width="120"
            v-if="item.name === '总预算'"
          >
            <template #default="{ row }">
              {{ formatMoney(row.totalBudget) }}
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
            label="预算编制人"
            prop="budgeterName"
            width="120"
            v-if="item.name === '预算编制人'"
          />
          <el-table-column
            align="center"
            label="预算状态"
            prop="budgetStatus"
            width="100"
            v-if="item.name === '预算状态'"
          >
            <template #default="{ row }">
              <el-tag :type="getBudgetStatusType(row.budgetStatus)">
                {{ getBudgetStatusName(row.budgetStatus) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="预算日期"
            prop="budgetDate"
            width="120"
            :formatter="formatDate"
            v-if="item.name === '预算日期'"
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
              @click="handleApprove(row)"
              v-if="row.budgetStatus === 2"
            >
              审批
            </el-button>
            <el-button
              type="text"
              size="small"
              @click="handleBudgetDetail(row)"
            >
              预算明细
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
    <BudgetEdit ref="edit" @fetch-data="fetchData" />
    <BudgetApproval ref="approval" @fetch-data="fetchData" />
    <BudgetDetailManagement ref="budgetDetail" />
  </div>
</template>

<script>
  import {
    getProjectBudgetList,
    getProjectBudgetById,
    approveBudget,
    deleteProjectBudget,
    batchDeleteProjectBudget
  } from '@/api/contract/budget'
  import BudgetEdit from './components/BudgetEdit'
  import BudgetApproval from './components/BudgetApproval'
  import BudgetDetailManagement from './components/BudgetDetailManagement'
  import { formatDate, formatMoney } from '@/utils/index'
  import filterSearch from '@/components/filterSearch.vue'
  import filterTable from '@/components/filterTable.vue'
  import { successCode } from '@/config/net.config'

  export default {
    name: 'Budget',
    components: {
      BudgetEdit,
      BudgetApproval,
      BudgetDetailManagement,
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
          budgetName: '',
          budgetType: null,
          budgetStatus: null,
          budgeterName: '',
          projectId: '',
          pageNum: 1,
          pageSize: 20,
        },
        multipleSelection: [], // 多选数据

        localKey: 'contract-budget-search',
        tableKey: 'contract-budget-list',
        searchNow: [],
        searchMore: true,
        searchItem: [],
        searchAll: this.getFiled(), //所有搜索项
        filedNow: [],
        filedAll: [
          { name: '预算名称' },
          { name: '预算类型' },
          { name: '预算版本' },
          { name: '总预算' },
          { name: '项目ID' },
          { name: '预算编制人' },
          { name: '预算状态' },
          { name: '预算日期' },
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
          { name: '预算名称', key: 'budgetName' },
          { name: '预算类型', key: 'budgetType' },
          { name: '预算状态', key: 'budgetStatus' },
          { name: '预算编制人', key: 'budgeterName' },
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
      formatMoney(amount) {
        if (!amount) return '0'
        return (amount / 10000).toFixed(2) + '万元'
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
          budgetName: '',
          budgetType: null,
          budgetStatus: null,
          budgeterName: '',
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
          const response = await getProjectBudgetList(this.queryForm)
          console.log('项目预算列表响应:', response)
          if (response.code === 1) {  // 1表示成功
            this.list = response.data.list || []
            this.total = response.data.total || 0
          } else {
            this.$message.error(response.msg || '查询失败')  // 使用msg字段
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
          const response = await getProjectBudgetById(row.id)
          console.log('项目预算详情响应:', response)
          if (response.code === 1) {  // 1表示成功
            await this.$refs['edit'].showEdit('detail', response.data)
          } else {
            this.$message.error(response.msg || '获取详情失败')  // 使用msg字段
          }
        } catch (error) {
          this.$message.error('获取详情失败：' + error.message)
        }
      },
      async handleEdit(row) {
        try {
          const response = await getProjectBudgetById(row.id)
          console.log('项目预算编辑响应:', response)
          if (response.code === 1) {  // 1表示成功
            await this.$refs['edit'].showEdit('edit', response.data)
          } else {
            this.$message.error(response.msg || '获取详情失败')  // 使用msg字段
          }
        } catch (error) {
          this.$message.error('获取详情失败：' + error.message)
        }
      },
      // 审批处理
      async handleApprove(row) {
        try {
          const response = await getProjectBudgetById(row.id)
          console.log('项目预算审批响应:', response)
          if (response.code === 1) {  // 1表示成功
            await this.$refs['approval'].showEdit(response.data)
          } else {
            this.$message.error(response.msg || '获取详情失败')  // 使用msg字段
          }
        } catch (error) {
          this.$message.error('获取详情失败：' + error.message)
        }
      },
      // 预算明细
      handleBudgetDetail(row) {
        this.$refs['budgetDetail'].showEdit(row)
      },
      // 删除项目预算
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          try {
            console.log('删除项目预算，ID:', row.id)
            const response = await deleteProjectBudget(row.id)
            console.log('删除项目预算响应:', response)

            if (this.isResponseSuccess(response)) {
              this.$baseMessage(response.msg || '删除成功', 'success')
              this.fetchData() // 刷新列表
            } else {
              this.$baseMessage(response.message || response.msg || '删除失败', 'error')
            }
          } catch (error) {
            console.error('删除项目预算失败:', error)
            this.$baseMessage('删除失败：' + error.message, 'error')
          }
        })
      },
      // 多选处理
      handleSelectionChange(selection) {
        this.multipleSelection = selection
      },
      // 统一状态码判断方法
      isResponseSuccess(response) {
        return successCode.includes(response.code)
      },
      // 获取预算类型名称
      getBudgetTypeName(type) {
        const typeMap = {
          1: '初步预算',
          2: '详细预算',
          3: '执行预算'
        }
        return typeMap[type] || '未知'
      },
      // 获取预算类型样式
      getBudgetTypeType(type) {
        const typeMap = {
          1: 'info',
          2: 'warning',
          3: 'success'
        }
        return typeMap[type] || 'info'
      },
      // 获取预算状态名称
      getBudgetStatusName(status) {
        const statusMap = {
          1: '草稿',
          2: '待审批',
          3: '已审批',
          4: '已批准',
          5: '执行中'
        }
        return statusMap[status] || '未知'
      },
      // 获取预算状态样式
      getBudgetStatusType(status) {
        const typeMap = {
          1: 'info',
          2: 'warning',
          3: 'primary',
          4: 'success',
          5: 'success'
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
