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
                v-model="queryForm.debtType"
                clearable
                placeholder="债权类型"
                v-if="item.name === '债权类型'"
              >
                <el-option label="应收账款" :value="1" />
                <el-option label="预付款项" :value="2" />
                <el-option label="其他应收款" :value="3" />
                <el-option label="保证金" :value="4" />
                <el-option label="违约金" :value="5" />
              </el-select>
              <el-select
                v-model="queryForm.debtStatus"
                clearable
                placeholder="债权状态"
                v-if="item.name === '债权状态'"
              >
                <el-option label="正常" :value="1" />
                <el-option label="逾期" :value="2" />
                <el-option label="催收中" :value="3" />
                <el-option label="已回收" :value="4" />
                <el-option label="坏账" :value="5" />
              </el-select>
              <el-input
                v-model="queryForm.debtorName"
                clearable
                placeholder="债务人"
                v-if="item.name === '债务人'"
              />
              <el-input
                v-model="queryForm.debtId"
                clearable
                placeholder="债权ID"
                v-if="item.name === '债权ID'"
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
        <el-button type="success" @click="handleAdd">新建债权记录</el-button>
        <el-button type="primary" @click="handleAgingAnalysis">账龄分析</el-button>
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
            <template slot-scope="{ row }">
              <el-button type="text" @click="handleDetail(row)">
                {{ row.projectName }}
              </el-button>
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="债权类型"
            prop="debtType"
            width="100"
            v-if="item.name === '债权类型'"
          >
            <template slot-scope="{ row }">
              <el-tag :type="getDebtTypeType(row.debtType)">
                {{ getDebtTypeName(row.debtType) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="债权ID"
            prop="debtId"
            width="120"
            v-if="item.name === '债权ID'"
          />
          <el-table-column
            align="center"
            label="债务人"
            prop="debtorName"
            width="150"
            v-if="item.name === '债务人'"
          />
          <el-table-column
            align="center"
            label="债权状态"
            prop="debtStatus"
            width="100"
            v-if="item.name === '债权状态'"
          >
            <template slot-scope="{ row }">
              <el-tag :type="getDebtStatusType(row.debtStatus)">
                {{ getDebtStatusName(row.debtStatus) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="债权金额"
            prop="debtAmount"
            width="120"
            v-if="item.name === '债权金额'"
          >
            <template slot-scope="{ row }">
              {{ formatMoney(row.debtAmount) }}
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="已回收金额"
            prop="recoveredAmount"
            width="120"
            v-if="item.name === '已回收金额'"
          >
            <template slot-scope="{ row }">
              {{ formatMoney(row.recoveredAmount) }}
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="剩余金额"
            prop="remainingAmount"
            width="120"
            v-if="item.name === '剩余金额'"
          >
            <template slot-scope="{ row }">
              {{ formatMoney(row.remainingAmount) }}
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="逾期天数"
            prop="overdueDays"
            width="100"
            v-if="item.name === '逾期天数'"
          >
            <template slot-scope="{ row }">
              <span :class="row.overdueDays > 0 ? 'overdue' : ''">
                {{ row.overdueDays || 0 }}天
              </span>
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="到期日期"
            prop="dueDate"
            width="120"
            :formatter="formatDate"
            v-if="item.name === '到期日期'"
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
        <el-table-column align="center" label="操作" width="300" fixed="right">
          <template slot-scope="{ row }">
            <el-button type="text" size="small" @click="handleDetail(row)">详情</el-button>
            <el-button type="text" size="small" @click="handleEdit(row)">编辑</el-button>
            <el-button
              type="text"
              size="small"
              @click="handleCollection(row)"
              v-if="row.debtStatus !== 4 && row.debtStatus !== 5"
            >
              催收记录
            </el-button>
            <el-button
              type="text"
              size="small"
              @click="handleRecovery(row)"
              v-if="row.debtStatus !== 4 && row.debtStatus !== 5"
            >
              回收登记
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
    <DebtEdit ref="edit" @fetch-data="fetchData" />
    <DebtCollection ref="collection" @fetch-data="fetchData" />
    <DebtRecovery ref="recovery" @fetch-data="fetchData" />
    <AgingAnalysis ref="aging" />
  </div>
</template>

<script>
  import {
    getDebtRecordList,
    getDebtRecordById,
    deleteDebtRecord
  } from '@/api/contract/debt'
  import DebtEdit from './components/DebtEdit'
  import DebtCollection from './components/DebtCollection'
  import DebtRecovery from './components/DebtRecovery'
  import AgingAnalysis from './components/AgingAnalysis'
  import { formatDate, formatMoney } from '@/utils/index'
  import filterSearch from '@/components/filterSearch.vue'
  import filterTable from '@/components/filterTable.vue'
  import { successCode } from '@/config/net.config'

  export default {
    name: 'Debt',
    components: {
      DebtEdit,
      DebtCollection,
      DebtRecovery,
      AgingAnalysis,
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
          debtType: null,
          debtStatus: null,
          debtorName: '',
          debtId: '',
          pageNum: 1,
          pageSize: 20,
        },
        multipleSelection: [], // 多选数据

        localKey: 'contract-debt-search',
        tableKey: 'contract-debt-list',
        searchNow: [],
        searchMore: true,
        searchItem: [],
        searchAll: this.getFiled(), //所有搜索项
        filedNow: [],
        filedAll: [
          { name: '项目名称' },
          { name: '债权类型' },
          { name: '债权ID' },
          { name: '债务人' },
          { name: '债权状态' },
          { name: '债权金额' },
          { name: '已回收金额' },
          { name: '剩余金额' },
          { name: '逾期天数' },
          { name: '到期日期' },
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
      // 统一的响应成功判断方法
      isResponseSuccess(response) {
        return response && successCode.includes(response.code)
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
          { name: '债权类型', key: 'debtType' },
          { name: '债权状态', key: 'debtStatus' },
          { name: '债务人', key: 'debtorName' },
          { name: '债权ID', key: 'debtId' },
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
          debtType: null,
          debtStatus: null,
          debtorName: '',
          debtId: '',
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
          const response = await getDebtRecordList(this.queryForm)
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
        console.log('handleAdd called, refs:', this.$refs)
        console.log('edit ref:', this.$refs['edit'])
        if (this.$refs['edit']) {
          this.$refs['edit'].showEdit('add', null)
        } else {
          console.error('DebtEdit component ref not found!')
        }
      },
      /**
       * @description: 打开详情表单弹框
       * @param {*} row 选择的行数据
       * @return {*}
       */
      async handleDetail(row) {
        try {
          const response = await getDebtRecordById(row.id)
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
          const response = await getDebtRecordById(row.id)
          if (response.code === 1) {
            await this.$refs['edit'].showEdit('edit', response.data)
          } else {
            this.$message.error(response.msg || '获取详情失败')
          }
        } catch (error) {
          this.$message.error('获取详情失败：' + error.message)
        }
      },
      // 催收记录
      async handleCollection(row) {
        try {
          const response = await getDebtRecordById(row.id)
          if (response.code === 1) {
            await this.$refs['collection'].showEdit(response.data)
          } else {
            this.$message.error(response.msg || '获取详情失败')
          }
        } catch (error) {
          this.$message.error('获取详情失败：' + error.message)
        }
      },
      // 回收登记
      async handleRecovery(row) {
        try {
          const response = await getDebtRecordById(row.id)
          if (response.code === 1) {
            await this.$refs['recovery'].showEdit(response.data)
          } else {
            this.$message.error(response.msg || '获取详情失败')
          }
        } catch (error) {
          this.$message.error('获取详情失败：' + error.message)
        }
      },
      // 账龄分析
      handleAgingAnalysis() {
        this.$refs['aging'].showEdit()
      },
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          try {
            const response = await deleteDebtRecord(row.id)
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
      // 获取债权类型名称
      getDebtTypeName(type) {
        const typeMap = {
          1: '应收账款',
          2: '预付款项',
          3: '其他应收款',
          4: '保证金',
          5: '违约金'
        }
        return typeMap[type] || '未知'
      },
      // 获取债权类型样式
      getDebtTypeType(type) {
        const typeMap = {
          1: 'primary',
          2: 'success',
          3: 'info',
          4: 'warning',
          5: 'danger'
        }
        return typeMap[type] || 'info'
      },
      // 获取债权状态名称
      getDebtStatusName(status) {
        const statusMap = {
          1: '正常',
          2: '逾期',
          3: '催收中',
          4: '已回收',
          5: '坏账'
        }
        return statusMap[status] || '未知'
      },
      // 获取债权状态样式
      getDebtStatusType(status) {
        const typeMap = {
          1: 'success',
          2: 'warning',
          3: 'primary',
          4: 'info',
          5: 'danger'
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
  .overdue {
    color: #f56c6c;
    font-weight: bold;
  }
</style>
