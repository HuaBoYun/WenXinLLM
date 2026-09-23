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
                v-model="queryForm.projectType"
                clearable
                placeholder="项目类型"
                v-if="item.name === '项目类型'"
              >
                <el-option label="建筑工程" :value="1" />
                <el-option label="市政工程" :value="2" />
                <el-option label="水利工程" :value="3" />
                <el-option label="交通工程" :value="4" />
              </el-select>
              <el-select
                v-model="queryForm.biddingMethod"
                clearable
                placeholder="招标方式"
                v-if="item.name === '招标方式'"
              >
                <el-option label="公开招标" :value="1" />
                <el-option label="邀请招标" :value="2" />
                <el-option label="竞争性谈判" :value="3" />
                <el-option label="单一来源" :value="4" />
              </el-select>
              <el-select
                v-model="queryForm.projectStatus"
                clearable
                placeholder="项目状态"
                v-if="item.name === '项目状态'"
              >
                <el-option label="信息收集" :value="1" />
                <el-option label="投标决策" :value="2" />
                <el-option label="投标准备" :value="3" />
                <el-option label="投标提交" :value="4" />
                <el-option label="开标评标" :value="5" />
                <el-option label="结果公示" :value="6" />
                <el-option label="合同签订" :value="7" />
              </el-select>
              <el-input
                v-model="queryForm.ownerName"
                clearable
                placeholder="业主单位"
                v-if="item.name === '业主单位'"
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
        <el-button type="success" @click="handleAdd">新建项目</el-button>
        <el-button
          type="danger"
          @click="handleBatchDelete"
          :disabled="multipleSelection.length === 0"
        >
          批量删除
        </el-button>
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
            label="项目类型"
            prop="projectType"
            width="100"
            v-if="item.name === '项目类型'"
          >
            <template #default="{ row }">
              <el-tag :type="getProjectTypeType(row.projectType)">
                {{ getProjectTypeName(row.projectType) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="招标方式"
            prop="biddingMethod"
            width="120"
            v-if="item.name === '招标方式'"
          >
            <template #default="{ row }">
              {{ getBiddingMethodName(row.biddingMethod) }}
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="业主单位"
            prop="ownerName"
            min-width="180"
            v-if="item.name === '业主单位'"
          />
          <el-table-column
            align="center"
            label="合同金额"
            prop="contractAmount"
            width="120"
            v-if="item.name === '合同金额'"
          >
            <template #default="{ row }">
              {{ formatMoney(row.contractAmount) }}
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="项目状态"
            prop="projectStatus"
            width="100"
            v-if="item.name === '项目状态'"
          >
            <template #default="{ row }">
              <el-tag :type="getProjectStatusType(row.projectStatus)">
                {{ getProjectStatusName(row.projectStatus) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="开标时间"
            prop="bidOpeningDate"
            width="150"
            :formatter="formatDate"
            v-if="item.name === '开标时间'"
          />
          <el-table-column
            align="center"
            label="项目经理"
            prop="projectManagerName"
            width="100"
            v-if="item.name === '项目经理'"
          />
        </div>
        <el-table-column align="center" label="操作" width="250" fixed="right">
          <template #default="{ row }">
            <el-button type="text" size="small" @click="handleDetail(row)">详情</el-button>
            <el-button type="text" size="small" @click="handleEdit(row)">编辑</el-button>
            <el-button
              type="text"
              size="small"
              @click="handleDecision(row)"
              v-if="row.projectStatus === 2"
            >
              投标决策
            </el-button>
            <el-button
              type="text"
              size="small"
              @click="handleResult(row)"
              v-if="row.projectStatus === 6"
            >
              录入结果
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
    <BiddingEdit ref="edit" @fetch-data="fetchData" />
    <BiddingDecision ref="decision" @fetch-data="fetchData" />
    <BiddingResult ref="result" @fetch-data="fetchData" />
  </div>
</template>

<script>
  import {
    getBiddingProjectList,
    getBiddingProjectById,
    deleteBiddingProject,
    batchDeleteBiddingProjects
  } from '@/api/contract/bidding'
  import BiddingEdit from './components/BiddingEdit'
  import BiddingDecision from './components/BiddingDecision'
  import BiddingResult from './components/BiddingResult'
  import { formatDate, formatMoney } from '@/utils/index'
  import filterSearch from '@/components/filterSearch.vue'
  import filterTable from '@/components/filterTable.vue'

  export default {
    name: 'Bidding',
    components: { BiddingEdit, BiddingDecision, BiddingResult, filterTable, filterSearch },
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          projectName: '',
          projectType: null,
          biddingMethod: null,
          projectStatus: null,
          ownerName: '',
          pageNum: 1,
          pageSize: 20,
        },
        multipleSelection: [], // 多选数据

        localKey: 'contract-bidding-search',
        tableKey: 'contract-bidding-list',
        searchNow: [],
        searchMore: true,
        searchItem: [],
        searchAll: this.getFiled(), //所有搜索项
        filedNow: [],
        filedAll: [
          { name: '项目名称' },
          { name: '项目类型' },
          { name: '招标方式' },
          { name: '业主单位' },
          { name: '合同金额' },
          { name: '项目状态' },
          { name: '开标时间' },
          { name: '项目经理' },
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
          { name: '项目名称', key: 'projectName' },
          { name: '项目类型', key: 'projectType' },
          { name: '招标方式', key: 'biddingMethod' },
          { name: '项目状态', key: 'projectStatus' },
          { name: '业主单位', key: 'ownerName' },
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
          projectName: '',
          projectType: null,
          biddingMethod: null,
          projectStatus: null,
          ownerName: '',
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
          const response = await getBiddingProjectList(this.queryForm)
          if (response.code === 1) {
            this.list = response.data.list || []
            this.total = response.data.total || 0
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
          const response = await getBiddingProjectById(row.id)
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
          const response = await getBiddingProjectById(row.id)
          if (response.code === 1) {
            await this.$refs['edit'].showEdit('edit', response.data)
          } else {
            this.$message.error(response.msg || '获取详情失败')
          }
        } catch (error) {
          this.$message.error('获取详情失败：' + error.message)
        }
      },
      // 投标决策
      async handleDecision(row) {
        try {
          const response = await getBiddingProjectById(row.id)
          if (response.code === 1) {
            await this.$refs['decision'].showEdit(response.data)
          } else {
            this.$message.error(response.msg || '获取详情失败')
          }
        } catch (error) {
          this.$message.error('获取详情失败：' + error.message)
        }
      },
      // 录入结果
      async handleResult(row) {
        try {
          const response = await getBiddingProjectById(row.id)
          if (response.code === 1) {
            await this.$refs['result'].showEdit(response.data)
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
            console.log('删除招投标项目，ID:', row.id)
            const response = await deleteBiddingProject(row.id)
            console.log('删除招投标项目响应:', response)

            if (this.isResponseSuccess(response)) {
              this.$baseMessage(response.msg || '删除成功', 'success')
              this.fetchData() // 刷新列表
            } else {
              this.$baseMessage(response.message || response.msg || '删除失败', 'error')
            }
          } catch (error) {
            console.error('删除招投标项目失败:', error)
            this.$baseMessage('删除失败：' + error.message, 'error')
          }
        })
      },
      // 多选处理
      handleSelectionChange(selection) {
        this.multipleSelection = selection
      },
      // 批量删除
      handleBatchDelete() {
        if (this.multipleSelection.length === 0) {
          this.$baseMessage('请选择要删除的数据', 'warning')
          return
        }

        this.$baseConfirm('你确定要删除选中的项目吗', null, async () => {
          try {
            const ids = this.multipleSelection.map(row => row.id)
            console.log('批量删除招投标项目，IDs:', ids)
            const response = await batchDeleteBiddingProjects(ids)
            console.log('批量删除招投标项目响应:', response)

            if (this.isResponseSuccess(response)) {
              this.$baseMessage(response.msg || '批量删除成功', 'success')
              this.fetchData() // 刷新列表
            } else {
              this.$baseMessage(response.message || response.msg || '批量删除失败', 'error')
            }
          } catch (error) {
            console.error('批量删除招投标项目失败:', error)
            this.$baseMessage('批量删除失败：' + error.message, 'error')
          }
        })
      },
      // 获取项目类型名称
      getProjectTypeName(type) {
        const typeMap = {
          1: '建筑工程',
          2: '市政工程',
          3: '水利工程',
          4: '交通工程'
        }
        return typeMap[type] || '未知'
      },
      // 获取项目类型样式
      getProjectTypeType(type) {
        const typeMap = {
          1: 'primary',
          2: 'success',
          3: 'info',
          4: 'warning'
        }
        return typeMap[type] || 'info'
      },
      // 获取招标方式名称
      getBiddingMethodName(method) {
        const methodMap = {
          1: '公开招标',
          2: '邀请招标',
          3: '竞争性谈判',
          4: '单一来源'
        }
        return methodMap[method] || '未知'
      },
      // 获取项目状态名称
      getProjectStatusName(status) {
        const statusMap = {
          1: '信息收集',
          2: '投标决策',
          3: '投标准备',
          4: '投标提交',
          5: '开标评标',
          6: '结果公示',
          7: '合同签订'
        }
        return statusMap[status] || '未知'
      },
      // 获取项目状态样式
      getProjectStatusType(status) {
        const typeMap = {
          1: 'info',
          2: 'warning',
          3: 'primary',
          4: 'success',
          5: 'warning',
          6: 'info',
          7: 'success'
        }
        return typeMap[status] || 'info'
      },
      // 统一状态码判断方法
      isResponseSuccess(response) {
        // 示例云项目的成功状态码通常是 1 或 200
        const successCode = [1, 200]
        return successCode.includes(response.code)
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
