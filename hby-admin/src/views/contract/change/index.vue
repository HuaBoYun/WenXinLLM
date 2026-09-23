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
                v-model="queryForm.projectName"
                clearable
                placeholder="项目名称"
                v-if="item.name === '项目名称'"
              />
              <el-select
                v-model="queryForm.changeType"
                clearable
                placeholder="变更类型"
                v-if="item.name === '变更类型'"
              >
                <el-option label="范围变更" :value="1" />
                <el-option label="时间变更" :value="2" />
                <el-option label="成本变更" :value="3" />
                <el-option label="质量变更" :value="4" />
                <el-option label="资源变更" :value="5" />
              </el-select>
              <el-select
                v-model="queryForm.changeStatus"
                clearable
                placeholder="变更状态"
                v-if="item.name === '变更状态'"
              >
                <el-option label="待审核" :value="1" />
                <el-option label="审核中" :value="2" />
                <el-option label="已批准" :value="3" />
                <el-option label="已拒绝" :value="4" />
                <el-option label="已实施" :value="5" />
                <el-option label="已关闭" :value="6" />
              </el-select>
              <el-input
                v-model="queryForm.applicantName"
                clearable
                placeholder="申请人"
                v-if="item.name === '申请人'"
              />
              <el-input
                v-model="queryForm.changeId"
                clearable
                placeholder="变更ID"
                v-if="item.name === '变更ID'"
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
        <el-button type="success" @click="handleAdd">新建变更申请</el-button>
      </vab-query-form-right-panel>
      <el-table v-loading="listLoading" :data="list" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" />
        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            align="center"
            label="项目名称"
            prop="projectName"
            min-width="200"
            v-if="item.name === '项目名称'"
          >
            <template #default="{ row }">
              <el-button type="text" @click="handleDetail(row)">
                {{ row.projectName }}
              </el-button>
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="变更类型"
            prop="changeType"
            width="100"
            v-if="item.name === '变更类型'"
          >
            <template #default="{ row }">
              <el-tag :type="getChangeTypeType(row.changeType)">
                {{ getChangeTypeName(row.changeType) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="变更ID"
            prop="changeId"
            width="120"
            v-if="item.name === '变更ID'"
          />
          <el-table-column
            align="center"
            label="申请人"
            prop="applicantName"
            width="120"
            v-if="item.name === '申请人'"
          />
          <el-table-column
            align="center"
            label="变更状态"
            prop="changeStatus"
            width="100"
            v-if="item.name === '变更状态'"
          >
            <template #default="{ row }">
              <el-tag :type="getChangeStatusType(row.changeStatus)">
                {{ getChangeStatusName(row.changeStatus) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="影响程度"
            prop="impactLevel"
            width="100"
            v-if="item.name === '影响程度'"
          >
            <template #default="{ row }">
              <el-tag :type="getImpactLevelType(row.impactLevel)">
                {{ getImpactLevelName(row.impactLevel) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="预估成本"
            prop="estimatedCost"
            width="120"
            v-if="item.name === '预估成本'"
          >
            <template #default="{ row }">
              {{ formatMoney(row.estimatedCost) }}
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="申请日期"
            prop="applicationDate"
            width="120"
            :formatter="formatDate"
            v-if="item.name === '申请日期'"
          />
          <el-table-column
            align="center"
            label="计划完成日期"
            prop="plannedCompletionDate"
            width="120"
            :formatter="formatDate"
            v-if="item.name === '计划完成日期'"
          />
        </div>
        <el-table-column align="center" label="操作" width="300" fixed="right">
          <template #default="{ row }">
            <el-button type="text" size="small" @click="handleDetail(row)">详情</el-button>
            <el-button type="text" size="small" @click="handleEdit(row)" v-if="row.changeStatus === 1">编辑</el-button>
            <el-button
              type="text"
              size="small"
              @click="handleReview(row)"
              v-if="row.changeStatus === 1 || row.changeStatus === 2"
            >
              审核
            </el-button>
            <el-button
              type="text"
              size="small"
              @click="handleImplement(row)"
              v-if="row.changeStatus === 3"
            >
              实施
            </el-button>
            <el-button type="text" size="small" style="color: #f56c6c;" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-pagination
      background
      class="pagination"
      :current-page="queryForm.pageNumber"
      :layout="layout"
      :page-size="queryForm.pageSize"
      :total="total"
      @current-change="handleCurrentChange"
      @size-change="handleSizeChange"
    />
    <ChangeEdit ref="edit" @fetch-data="fetchData" />
    <ChangeReview ref="review" @fetch-data="fetchData" />
    <ChangeImplement ref="implement" @fetch-data="fetchData" />
  </div>
</template>

<script>
  import {
    getProjectChangeList,
    getProjectChangeById,
    deleteProjectChange
  } from '@/api/contract/change'
  import ChangeEdit from './components/ChangeEdit'
  import ChangeReview from './components/ChangeReview'
  import ChangeImplement from './components/ChangeImplement'
  import { formatDate, formatMoney } from '@/utils/index'
  import filterSearch from '@/components/filterSearch.vue'
  import filterTable from '@/components/filterTable.vue'
  import { successCode } from '@/config/net.config'

  export default {
    name: 'Change',
    components: {
      ChangeEdit,
      ChangeReview,
      ChangeImplement,
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
          projectName: '',
          changeType: null,
          changeStatus: null,
          applicantName: '',
          changeId: '',
          pageNumber: 1,
          pageSize: 20,
        },
        multipleSelection: [], // 多选数据

        localKey: 'contract-change-search',
        tableKey: 'contract-change-list',
        searchNow: [],
        searchMore: true,
        searchItem: [],
        searchAll: this.getFiled(), //所有搜索项
        filedNow: [],
        filedAll: [
          { name: '项目名称' },
          { name: '变更类型' },
          { name: '变更ID' },
          { name: '申请人' },
          { name: '变更状态' },
          { name: '影响程度' },
          { name: '预估成本' },
          { name: '申请日期' },
          { name: '计划完成日期' },
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
      // 统一状态码判断方法
      isResponseSuccess(response) {
        return successCode.includes(response.code)
      },
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
          { name: '项目名称', key: 'projectName' },
          { name: '变更类型', key: 'changeType' },
          { name: '变更状态', key: 'changeStatus' },
          { name: '申请人', key: 'applicantName' },
          { name: '变更ID', key: 'changeId' },
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
        this.queryForm.pageNumber = val
        this.fetchData()
      },
      resetQueryForm() {
        this.queryForm = {
          projectName: '',
          changeType: null,
          changeStatus: null,
          applicantName: '',
          changeId: '',
          pageNumber: 1,
          pageSize: 20,
        }
      },
      resetSearch() {
        this.resetQueryForm()
        this.fetchData()
      },
      queryData() {
        this.queryForm.pageNumber = 1
        this.fetchData()
      },
      /**
       * @description: 数据请求
       * @return {*}
       */
      async fetchData() {
        this.listLoading = true
        try {
          const response = await getProjectChangeList(this.queryForm)
          console.log('项目变更列表响应:', response)
          if (this.isResponseSuccess(response)) {
            this.list = response.data.list || []
            this.total = response.data.total || 0
          } else {
            this.$message.error(response.msg || response.message || '查询失败')
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
          const response = await getProjectChangeById(row.id)
          console.log('项目变更详情响应:', response)
          if (this.isResponseSuccess(response)) {
            await this.$refs['edit'].showEdit('detail', response.data)
          } else {
            this.$message.error(response.msg || response.message || '获取详情失败')
          }
        } catch (error) {
          this.$message.error('获取详情失败：' + error.message)
        }
      },
      async handleEdit(row) {
        try {
          // 验证行数据和ID
          if (!row || !row.id || row.id === 'undefined') {
            this.$message.error('编辑失败：数据异常，请刷新页面重试')
            return
          }

          const response = await getProjectChangeById(row.id)
          console.log('项目变更编辑响应:', response)
          if (this.isResponseSuccess(response)) {
            await this.$refs['edit'].showEdit('edit', response.data)
          } else {
            this.$message.error(response.msg || response.message || '获取详情失败')
          }
        } catch (error) {
          this.$message.error('获取详情失败：' + error.message)
        }
      },
      // 变更审核
      async handleReview(row) {
        try {
          const response = await getProjectChangeById(row.id)
          console.log('项目变更审核响应:', response)
          if (this.isResponseSuccess(response)) {
            await this.$refs['review'].showEdit(response.data)
          } else {
            this.$message.error(response.msg || response.message || '获取详情失败')
          }
        } catch (error) {
          this.$message.error('获取详情失败：' + error.message)
        }
      },
      // 变更实施
      async handleImplement(row) {
        try {
          const response = await getProjectChangeById(row.id)
          console.log('项目变更实施响应:', response)
          if (this.isResponseSuccess(response)) {
            await this.$refs['implement'].showEdit(response.data)
          } else {
            this.$message.error(response.msg || response.message || '获取详情失败')
          }
        } catch (error) {
          this.$message.error('获取详情失败：' + error.message)
        }
      },
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          try {
            const response = await deleteProjectChange(row.id)
            console.log('项目变更删除响应:', response)
            if (this.isResponseSuccess(response)) {
              this.$baseMessage('删除成功', 'success')
              this.fetchData()
            } else {
              this.$baseMessage(response.msg || response.message || '删除失败', 'error')
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
      // 获取变更类型名称
      getChangeTypeName(type) {
        const typeMap = {
          1: '范围变更',
          2: '时间变更',
          3: '成本变更',
          4: '质量变更',
          5: '资源变更'
        }
        return typeMap[type] || '未知'
      },
      // 获取变更类型样式
      getChangeTypeType(type) {
        const typeMap = {
          1: 'primary',
          2: 'success',
          3: 'warning',
          4: 'danger',
          5: 'info'
        }
        return typeMap[type] || 'info'
      },
      // 获取变更状态名称
      getChangeStatusName(status) {
        const statusMap = {
          1: '待审核',
          2: '审核中',
          3: '已批准',
          4: '已拒绝',
          5: '已实施',
          6: '已关闭'
        }
        return statusMap[status] || '未知'
      },
      // 获取变更状态样式
      getChangeStatusType(status) {
        const typeMap = {
          1: 'info',
          2: 'primary',
          3: 'success',
          4: 'danger',
          5: 'warning',
          6: 'info'
        }
        return typeMap[status] || 'info'
      },
      // 获取影响程度名称
      getImpactLevelName(level) {
        const levelMap = {
          1: '低',
          2: '中',
          3: '高',
          4: '极高'
        }
        return levelMap[level] || '未知'
      },
      // 获取影响程度样式
      getImpactLevelType(level) {
        const typeMap = {
          1: 'success',
          2: 'primary',
          3: 'warning',
          4: 'danger'
        }
        return typeMap[level] || 'info'
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
