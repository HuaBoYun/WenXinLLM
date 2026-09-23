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
                v-model="queryForm.operationsType"
                clearable
                placeholder="经营类型"
                v-if="item.name === '经营类型'"
              >
                <el-option label="收入管理" :value="1" />
                <el-option label="成本管理" :value="2" />
                <el-option label="利润分析" :value="3" />
                <el-option label="现金流管理" :value="4" />
                <el-option label="风险控制" :value="5" />
                <el-option label="绩效评估" :value="6" />
              </el-select>
              <el-select
                v-model="queryForm.operationsStatus"
                clearable
                placeholder="经营状态"
                v-if="item.name === '经营状态'"
              >
                <el-option label="正常" :value="1" />
                <el-option label="预警" :value="2" />
                <el-option label="异常" :value="3" />
                <el-option label="停止" :value="4" />
              </el-select>
              <el-input
                v-model="queryForm.managerName"
                clearable
                placeholder="负责人"
                v-if="item.name === '负责人'"
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
        <el-button type="success" @click="handleAdd">新建经营记录</el-button>
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
            label="经营类型"
            prop="operationsType"
            width="120"
            v-if="item.name === '经营类型'"
          >
            <template #default="{ row }">
              <el-tag :type="getOperationsTypeType(row.operationsType)">
                {{ getOperationsTypeName(row.operationsType) }}
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
            label="负责人"
            prop="managerName"
            width="120"
            v-if="item.name === '负责人'"
          />
          <el-table-column
            align="center"
            label="经营状态"
            prop="operationsStatus"
            width="100"
            v-if="item.name === '经营状态'"
          >
            <template #default="{ row }">
              <el-tag :type="getOperationsStatusType(row.operationsStatus)">
                {{ getOperationsStatusName(row.operationsStatus) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="预算收入"
            prop="budgetRevenue"
            width="120"
            v-if="item.name === '预算收入'"
          >
            <template #default="{ row }">
              {{ formatMoney(row.budgetRevenue) }}
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="实际收入"
            prop="actualRevenue"
            width="120"
            v-if="item.name === '实际收入'"
          >
            <template #default="{ row }">
              {{ formatMoney(row.actualRevenue) }}
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="预算成本"
            prop="budgetCost"
            width="120"
            v-if="item.name === '预算成本'"
          >
            <template #default="{ row }">
              {{ formatMoney(row.budgetCost) }}
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="实际成本"
            prop="actualCost"
            width="120"
            v-if="item.name === '实际成本'"
          >
            <template #default="{ row }">
              {{ formatMoney(row.actualCost) }}
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="利润率(%)"
            prop="profitRate"
            width="100"
            v-if="item.name === '利润率'"
          >
            <template #default="{ row }">
              <span :class="getProfitRateClass(row.profitRate)">
                {{ row.profitRate || 0 }}%
              </span>
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="风险等级"
            prop="riskLevel"
            width="100"
            v-if="item.name === '风险等级'"
          >
            <template #default="{ row }">
              <el-tag :type="getRiskLevelType(row.riskLevel)">
                {{ getRiskLevelName(row.riskLevel) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="创建时间"
            prop="createTime"
            width="150"
            :formatter="formatDate"
            v-if="item.name === '创建时间'"
          />
        </div>
        <el-table-column align="center" label="操作" width="400" fixed="right">
          <template #default="{ row }">
            <el-button type="text" size="small" @click="handleDetail(row)">详情</el-button>
            <el-button type="text" size="small" @click="handleEdit(row)">编辑</el-button>

            <!-- 项目管理功能下拉菜单 -->
            <el-dropdown @command="handleManagementCommand" trigger="click">
              <el-button type="text" size="small">
                项目管理 <i class="el-icon-arrow-down el-icon--right"></i>
              </el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item :command="{type: 'finance', row: row}">
                  <i class="el-icon-money"></i> 财务管理
                </el-dropdown-item>
                <el-dropdown-item :command="{type: 'progress', row: row}">
                  <i class="el-icon-time"></i> 进度管理
                </el-dropdown-item>
                <el-dropdown-item :command="{type: 'quality', row: row}">
                  <i class="el-icon-star-on"></i> 质量管理
                </el-dropdown-item>
                <el-dropdown-item :command="{type: 'safety', row: row}">
                  <i class="el-icon-warning"></i> 安全管理
                </el-dropdown-item>
                <el-dropdown-item :command="{type: 'settlement', row: row}">
                  <i class="el-icon-document"></i> 结算管理
                </el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>

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
    <ManagementEdit ref="edit" @fetch-data="fetchData" />
    <FinanceManagement ref="finance" />
    <ProgressManagement ref="progress" />
    <QualityManagement ref="quality" />
    <SafetyManagement ref="safety" />
    <SettlementManagement ref="settlement" />
  </div>
</template>

<script>
  import {
    getProjectOperationsList,
    getProjectOperationsById,
    deleteProjectOperations
  } from '@/api/contract/operations'
  import { successCode } from '@/config'
  import ManagementEdit from './components/ManagementEdit'
  import FinanceManagement from './components/FinanceManagement'
  import ProgressManagement from './components/ProgressManagement'
  import QualityManagement from './components/QualityManagement'
  import SafetyManagement from './components/SafetyManagement'
  import SettlementManagement from './components/SettlementManagement'
  import { formatDate, formatMoney } from '@/utils/index'
  import filterSearch from '@/components/filterSearch.vue'
  import filterTable from '@/components/filterTable.vue'

  export default {
    name: 'Management',
    components: {
      ManagementEdit,
      FinanceManagement,
      ProgressManagement,
      QualityManagement,
      SafetyManagement,
      SettlementManagement,
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
          operationsType: null,
          operationsStatus: null,
          managerName: '',
          projectId: '',
          pageNumber: 1,
          pageSize: 20,
        },
        multipleSelection: [], // 多选数据

        localKey: 'contract-management-search',
        tableKey: 'contract-management-list',
        searchNow: [],
        searchMore: true,
        searchItem: [],
        searchAll: this.getFiled(), //所有搜索项
        filedNow: [],
        filedAll: [
          { name: '项目名称' },
          { name: '经营类型' },
          { name: '项目ID' },
          { name: '负责人' },
          { name: '经营状态' },
          { name: '预算收入' },
          { name: '实际收入' },
          { name: '预算成本' },
          { name: '实际成本' },
          { name: '利润率' },
          { name: '风险等级' },
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
      /**
       * 检查响应是否成功
       * @param {Object} response 响应对象
       * @returns {boolean} 是否成功
       */
      isResponseSuccess(response) {
        const codes = Array.isArray(successCode) ? successCode : [successCode]
        return codes.includes(response.code)
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
          { name: '经营类型', key: 'operationsType' },
          { name: '经营状态', key: 'operationsStatus' },
          { name: '负责人', key: 'managerName' },
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
        this.queryForm.pageNumber = val
        this.fetchData()
      },
      resetQueryForm() {
        this.queryForm = {
          projectName: '',
          operationsType: null,
          operationsStatus: null,
          managerName: '',
          projectId: '',
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
          const response = await getProjectOperationsList(this.queryForm)
          console.log('项目经营列表接口返回数据:', response)
          console.log('响应状态码:', response.code)
          console.log('响应数据结构:', response.data)

          // 使用统一的状态码判断方法
          if (this.isResponseSuccess(response)) {
            // 支持多种数据结构格式
            if (response.data && response.data.list !== undefined) {
              // 标准格式：{ code: 200, data: { list: [], total: 0 } }
              this.list = response.data.list || []
              this.total = response.data.total || 0
            } else if (response.data && Array.isArray(response.data)) {
              // 数组格式：{ code: 200, data: [] }
              this.list = response.data || []
              this.total = response.data.length || 0
            } else if (response.list !== undefined) {
              // 直接格式：{ code: 200, list: [], total: 0 }
              this.list = response.list || []
              this.total = response.total || 0
            } else {
              // 兜底处理
              this.list = []
              this.total = 0
            }
            console.log('项目经营列表数据:', this.list.length, '条记录')
          } else {
            this.$message.error(response.message || response.msg || '查询失败')
            this.list = []
            this.total = 0
          }
        } catch (error) {
          console.error('获取项目经营列表失败:', error)
          this.$message.error('获取数据失败：' + error.message)
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
          const response = await getProjectOperationsById(row.id)
          console.log('详情接口返回数据:', response)
          console.log('详情响应状态码:', response.code)

          // 使用统一的状态码判断方法
          if (this.isResponseSuccess(response)) {
            await this.$refs['edit'].showEdit('detail', response.data)
          } else {
            this.$message.error(response.message || response.msg || '获取详情失败')
          }
        } catch (error) {
          this.$message.error('获取详情失败：' + error.message)
        }
      },
      async handleEdit(row) {
        try {
          const response = await getProjectOperationsById(row.id)
          console.log('编辑接口返回数据:', response)
          console.log('编辑响应状态码:', response.code)

          // 使用统一的状态码判断方法
          if (this.isResponseSuccess(response)) {
            await this.$refs['edit'].showEdit('edit', response.data)
          } else {
            this.$message.error(response.message || response.msg || '获取详情失败')
          }
        } catch (error) {
          this.$message.error('获取详情失败：' + error.message)
        }
      },
      // 财务管理
      handleFinanceManagement(row) {
        this.$refs['finance'].showEdit(row)
      },
      // 进度管理
      handleProgressManagement(row) {
        this.$refs['progress'].showEdit(row)
      },
      // 质量管理
      handleQualityManagement(row) {
        this.$refs['quality'].showEdit(row)
      },
      // 安全管理
      handleSafetyManagement(row) {
        this.$refs['safety'].showEdit(row)
      },
      // 结算管理
      handleSettlementManagement(row) {
        this.$refs['settlement'].showEdit(row)
      },
      // 处理管理功能下拉菜单命令
      handleManagementCommand(command) {
        const { type, row } = command
        switch (type) {
          case 'finance':
            this.handleFinanceManagement(row)
            break
          case 'progress':
            this.handleProgressManagement(row)
            break
          case 'quality':
            this.handleQualityManagement(row)
            break
          case 'safety':
            this.handleSafetyManagement(row)
            break
          case 'settlement':
            this.handleSettlementManagement(row)
            break
          default:
            this.$message.warning('未知的管理类型')
        }
      },
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          try {
            const response = await deleteProjectOperations(row.id)
            console.log('删除接口返回数据:', response)
            console.log('删除响应状态码:', response.code)
            console.log('删除响应消息:', response.msg)

            // 使用统一的状态码判断方法
            if (this.isResponseSuccess(response)) {
              this.$baseMessage(response.msg || '删除成功', 'success')
              this.fetchData()
            } else {
              this.$baseMessage(response.message || response.msg || '删除失败', 'error')
            }
          } catch (error) {
            console.error('删除操作错误:', error)
            this.$baseMessage('删除失败：' + error.message, 'error')
          }
        })
      },
      // 多选处理
      handleSelectionChange(selection) {
        this.multipleSelection = selection
      },
      // 获取经营类型名称
      getOperationsTypeName(type) {
        const typeMap = {
          1: '收入管理',
          2: '成本管理',
          3: '利润分析',
          4: '现金流管理',
          5: '风险控制',
          6: '绩效评估'
        }
        return typeMap[type] || '未知'
      },
      // 获取经营类型样式
      getOperationsTypeType(type) {
        const typeMap = {
          1: 'success',
          2: 'primary',
          3: 'warning',
          4: 'danger',
          5: 'info',
          6: ''
        }
        return typeMap[type] || 'info'
      },
      // 获取经营状态名称
      getOperationsStatusName(status) {
        const statusMap = {
          1: '正常',
          2: '预警',
          3: '异常',
          4: '停止'
        }
        return statusMap[status] || '未知'
      },
      // 获取经营状态样式
      getOperationsStatusType(status) {
        const typeMap = {
          1: 'success',
          2: 'warning',
          3: 'danger',
          4: 'info'
        }
        return typeMap[status] || 'info'
      },
      // 获取风险等级名称
      getRiskLevelName(level) {
        const levelMap = {
          1: '低',
          2: '中',
          3: '高',
          4: '极高'
        }
        return levelMap[level] || '未知'
      },
      // 获取风险等级样式
      getRiskLevelType(level) {
        const typeMap = {
          1: 'success',
          2: '',
          3: 'warning',
          4: 'danger'
        }
        return typeMap[level] || 'info'
      },
      // 获取利润率样式
      getProfitRateClass(rate) {
        if (!rate) return ''
        if (rate > 20) return 'text-success'
        if (rate > 10) return 'text-warning'
        if (rate > 0) return 'text-info'
        return 'text-danger'
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

  .text-success {
    color: #67c23a;
    font-weight: bold;
  }

  .text-warning {
    color: #e6a23c;
    font-weight: bold;
  }

  .text-info {
    color: #909399;
  }

  .text-danger {
    color: #f56c6c;
    font-weight: bold;
  }
</style>
