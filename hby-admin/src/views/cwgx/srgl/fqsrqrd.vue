<!-- 分期收入确认单 -->
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
                v-model="queryForm.financeOrg"
                clearable
                placeholder="财务组织"
                v-if="item.name === '财务组织'"
              />
              <el-date-picker
                v-model="queryForm.day"
                type="daterange"
                align="right"
                unlink-panels
                range-separator="至"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                v-if="item.name === '时间'"
              ></el-date-picker>
              <el-input
                v-model="queryForm.documentNo"
                clearable
                placeholder="单据号"
                v-if="item.name === '单据号'"
              />
              <el-input
                v-model="queryForm.revenueType"
                clearable
                placeholder="收入类型"
                v-if="item.name === '收入类型'"
              />
              <el-input
                v-model="queryForm.customerName"
                clearable
                placeholder="客户"
                v-if="item.name === '客户'"
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
      <vab-query-form-right-panel style="width: 100%">
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
        <!-- <el-button type="success" @click="handleEdit(false, false)">
          新建
        </el-button> -->
        <!-- <el-button type="primary">导出</el-button> -->
      </vab-query-form-right-panel>

      <el-table v-loading="listLoading" :data="list">
        <el-table-column
          align="center"
          label="序号"
          prop="qdcode"
          width="100"
        ></el-table-column>
        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            v-if="item.name === '财务组织'"
            align="center"
            label="财务组织"
            prop="financeOrg"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '单据号'"
            align="center"
            label="单据号"
            prop="documentNo"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '单据日期'"
            align="center"
            label="单据日期"
            prop="documentDate"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '币种'"
            align="center"
            label="币种"
            prop="currency"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '原币无税金额'"
            align="center"
            label="原币无税金额"
            prop="originalAmount"
            show-overflow-tooltip
          ></el-table-column>
          <el-table-column
            v-if="item.name === '本币无税金额'"
            align="center"
            label="本币无税金额"
            prop="localAmount"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '客户'"
            align="center"
            label="客户"
            prop="customerName"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '部门'"
            align="center"
            label="部门"
            prop="department"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '单据状态'"
            align="center"
            label="单据状态"
            prop="documentStatus"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '审批状态'"
            align="center"
            label="审批状态"
            prop="approvalStatus"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '业务流程'"
            align="center"
            label="业务流程"
            prop="businessProcess"
            show-overflow-tooltip
          />
        </div>
        <el-table-column
          align="center"
          label="操作"
          show-overflow-tooltip
          width="120"
        >
          <template slot-scope="scope">
            <el-button
              type="text"
              @click="handleEdit(scope.row, false)"
              :disabled="!!scope.row.spzt"
            >
              修改
            </el-button>
            <el-dropdown style="margin-left: 10px">
              <el-button type="text">更多</el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item>
                  <el-button
                    @click="handleDelete(scope.row)"
                    type="text"
                    :disabled="!!scope.row.spzt"
                  >
                    删除
                  </el-button>
                </el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-pagination
      background
      :current-page="queryForm.pageNumber"
      :layout="layout"
      :page-size="queryForm.pageSize"
      :total="total"
      @current-change="handleCurrentChange"
      @size-change="handleSizeChange"
    />
    <Edit ref="edit" @fetch-data="fetchData"></Edit>
  </div>
</template>

<script>
  // 使用财务共享模块的收入确认API
  import {
    getDeferredRevenueList,
    deleteDeferredRevenue,
  } from '@/api/financialSharing/revenueManagement'
  import Edit from './components/cbcjEdit'
  import filterSearch from '@/components/filterSearch'
  import filterTable from '@/components/filterTable'
  import { searchTableMixis } from '@/mixis/index'

  export default {
    name: 'fqsrqrd',
    mixins: [searchTableMixis],
    components: {
      Edit,
      filterSearch,
      filterTable,
    },
    data() {
      return {
        list: [],
        planNum: '',
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          financeOrg: undefined,
          documentNo: undefined,
          revenueType: undefined,
          customerName: undefined,
          pageNumber: 1,
          pageSize: 10,
          day: null,
        },
        currProjectId: '',
        filedAll: [
          { name: '财务组织' },
          { name: '单据号' },
          { name: '单据日期' },
          { name: '币种' },
          { name: '原币无税金额' },
          { name: '本币无税金额' },
          { name: '客户' },
          { name: '部门' },
          { name: '单据状态' },
          { name: '审批状态' },
          { name: '业务流程' },
        ], //所有表格项
        filedNow: [], //当前表格项
        searchAll: this.getFiled(), //所有搜索项
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        localKey: 'cwgx-yfgl-fqsrqrd-search',
        tableKey: 'cwgx-yfgl-fqsrqrd-list',
        searchMore: false,
        select: [],
      }
    },
    created() {
      this.fetchData()
      this.initTable()
      this.searchNow = this.getFiled()
      this.searchItem = this.searchNow.slice(0, 4)
      this.initSearch()
    },
    mounted() {
      this.$bus.on('updateMsg', (value) => {
        if (value == 0) {
          this.fetchData()
        }
      })
    },
    methods: {
      getFiled() {
        return [
          { name: '财务组织', key: 'financeOrg' },
          { name: '时间', key: 'day' },
          { name: '单据号', key: 'documentNo' },
          { name: '收入类型', key: 'revenueType' },
          { name: '客户', key: 'customerName' },
        ]
      },
      selectTeamList(val, flagTitle) {
        console.log(val, flagTitle)
        if (flagTitle) {
          this.queryForm.projectOrderName = val[0].realname
          this.queryForm.projectOrderId = val[0].staffid
        } else {
          let arrStr = ''
          let arr = []
          val.forEach((item) => {
            arr.push(item.realname)
          })
          arrStr = arr.join(',')
          this.tableData[this.sIndex].zyNames = arrStr
          //拿到组员id字符串
          let arrStrZy = ''
          let arrZy = []
          val.forEach((item) => {
            arrZy.push(item.staffid)
          })
          arrStrZy = arrZy.join(',')
          this.zyStaffids = arrStrZy
        }
      },
      showGroupLeader() {
        this.$refs['select'].showEdit('leader')
      },
      resetQueryForm() {
        this.queryForm = {
          financeOrg: undefined,
          documentNo: undefined,
          revenueType: undefined,
          customerName: undefined,
          pageNumber: 1,
          pageSize: 10,
          day: null,
        }
      },
      resetSearch() {
        this.resetQueryForm()
        this.fetchData()
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
        try {
          // 构建查询参数
          const params = {
            pageNumber: this.queryForm.pageNumber,
            pageSize: this.queryForm.pageSize,
            param: {}
          }
          // 添加搜索条件
          if (this.queryForm.financeOrg) {
            params.param.financeOrg = this.queryForm.financeOrg
          }
          if (this.queryForm.documentNo) {
            params.param.deferredNo = this.queryForm.documentNo
          }
          if (this.queryForm.revenueType) {
            params.param.deferredType = this.queryForm.revenueType
          }
          if (this.queryForm.customerName) {
            params.param.customerName = this.queryForm.customerName
          }
          // 处理日期范围
          if (this.queryForm.day && this.queryForm.day.length === 2) {
            params.param.startDate = this.formatDate(this.queryForm.day[0])
            params.param.endDate = this.formatDate(this.queryForm.day[1])
          }

          const response = await getDeferredRevenueList(params)
          if (response.code === 1 && response.data) {
            // 转换数据格式以匹配表格字段
            const dataList = response.data.tlist || []
            this.list = dataList.map((item, index) => ({
              qdcode: (this.queryForm.pageNumber - 1) * this.queryForm.pageSize + index + 1,
              financeOrg: item.financeOrg || '默认财务组织',
              documentNo: item.deferredNo || item.DEFERRED_NO || '-',
              documentDate: item.createTime || item.CREATE_TIME || '-',
              currency: item.currency || 'CNY',
              originalAmount: item.originalAmount || item.ORIGINAL_AMOUNT || 0,
              localAmount: item.deferredAmount || item.DEFERRED_AMOUNT || 0,
              customerName: item.customerName || '-',
              department: item.department || '-',
              documentStatus: this.getStatusName(item.deferredStatus || item.DEFERRED_STATUS),
              approvalStatus: item.approvalStatus || '未提交',
              businessProcess: item.businessProcess || '-',
              id: item.deferredId || item.DEFERRED_ID,
              spzt: item.deferredStatus > 0 ? 1 : 0,
            }))
            this.total = response.data.totalRecord || 0
          } else {
            this.list = []
            this.total = 0
            if (response.msg) {
              this.$message.warning(response.msg)
            }
          }
        } catch (error) {
          console.error('获取分期收入确认单列表失败:', error)
          this.list = []
          this.total = 0
          this.$message.error('获取数据失败，请稍后重试')
        } finally {
          this.listLoading = false
        }
      },
      // 格式化日期
      formatDate(date) {
        if (!date) return ''
        const d = new Date(date)
        const year = d.getFullYear()
        const month = String(d.getMonth() + 1).padStart(2, '0')
        const day = String(d.getDate()).padStart(2, '0')
        return `${year}-${month}-${day}`
      },
      // 获取状态名称
      getStatusName(status) {
        const statusMap = {
          0: '待确认',
          1: '已确认',
          2: '已完成',
          3: '已取消',
        }
        return statusMap[status] || '未知'
      },
      days(start, end) {
        let s = new Date(start)
        let e = new Date(end)
        let hours = (e - s) / (1000 * 60 * 60 * 24)
        return hours + '天'
      },

      handleEdit(row, disabled, type) {
        this.$refs['edit'].showEdit(row, disabled, this.planNum, type)
      },
      async handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          try {
            const response = await deleteDeferredRevenue(row.id)
            if (response.code === 1) {
              this.$baseMessage('删除成功', 'success')
              await this.fetchData()
            } else {
              this.$baseMessage(response.msg || '删除失败', 'error')
            }
          } catch (error) {
            console.error('删除失败:', error)
            this.$baseMessage('删除失败，请稍后重试', 'error')
          }
        })
      },
      sendModel() {
        this.$refs['sendModel'].showEdit()
      },
      send() {
        this.$refs['send'].showEdit()
      },
      color(row) {
        if (row.id == this.currProjectId) {
          return { color: '#7fcf7c' }
        } else {
          return { color: '' }
        }
      },
    },
  }
</script>

<style scoped lang="scss">
  .system-log-container {
    padding: 0 !important;
    background: #f6f8f9 !important;
  }
  .margin-b0 {
    margin-bottom: 0;
  }
</style>
