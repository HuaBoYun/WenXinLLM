<!-- 收入合同执行情况查询 -->
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
                v-model="queryForm.jsFinance"
                clearable
                placeholder="所属组织"
                v-if="item.name === '所属组织'"
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
                v-model="queryForm.supplier"
                clearable
                placeholder="合同名称"
                v-if="item.name === '合同名称'"
              />
              <el-input
                v-model="queryForm.supplier"
                clearable
                placeholder="客户"
                v-if="item.name === '客户'"
              />
              <el-input
                v-model="queryForm.supplier"
                clearable
                placeholder="合同类型"
                v-if="item.name === '合同类型'"
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
          type="index"
          width="100"
        ></el-table-column>
        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            v-if="item.name === '财务组织'"
            align="center"
            label="财务组织"
            prop="financialOrg"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '上级合同'"
            align="center"
            label="上级合同"
            prop="parentContractNo"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '合同编码'"
            align="center"
            label="合同编码"
            prop="contractNo"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '合同名称'"
            align="center"
            label="合同名称"
            prop="contractName"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '合同类型'"
            align="center"
            label="合同类型"
            prop="contractType"
            show-overflow-tooltip
          ></el-table-column>
          <el-table-column
            v-if="item.name === '合同状态'"
            align="center"
            label="合同状态"
            prop="statusDesc"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '合同日期'"
            align="center"
            label="合同日期"
            prop="signDate"
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
            v-if="item.name === '币种'"
            align="center"
            label="币种"
            prop="currency"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '合同总金额'"
            align="center"
            label="合同总金额"
            prop="contractAmount"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '累计出库金额'"
            align="center"
            label="累计出库金额"
            prop="outboundAmount"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '未出库金额'"
            align="center"
            label="未出库金额"
            prop="unoutboundAmount"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '累计签收金额'"
            align="center"
            label="累计签收金额"
            prop="signedAmount"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '累计途损金额'"
            align="center"
            label="累计途损金额"
            prop="transitLossAmount"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '未签收金额'"
            align="center"
            label="未签收金额"
            prop="unsignedAmount"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '累计收入确认金额'"
            align="center"
            label="累计收入确认金额"
            prop="performedAmount"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '未收入确认金额'"
            align="center"
            label="未收入确认金额"
            prop="unperformedAmount"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '累计开票金额'"
            align="center"
            label="累计开票金额"
            prop="invoicedAmount"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '未开票金额'"
            align="center"
            label="未开票金额"
            prop="uninvoicedAmount"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '累计收款金额'"
            align="center"
            label="累计收款金额"
            prop="receivedAmount"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '未收款金额'"
            align="center"
            label="未收款金额"
            prop="unreceiveAmount"
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
  import {
    getRevenueContractList,
    deleteRevenueContract,
  } from '@/oapi/cwgx/srgl'
  import Edit from './components/cbcjEdit'
  import filterSearch from '@/components/filterSearch'
  import filterTable from '@/components/filterTable'
  import { searchTableMixis } from '@/mixis/index'

  export default {
    name: 'srhtzxqkcx',
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
          contractNo: undefined,
          contractName: undefined,
          customerName: undefined,
          contractStatus: undefined,
          contractType: undefined,
          pageNumber: 1,
          pageSize: 10,
        },
        currProjectId: '',
        filedAll: [
          { name: '财务组织' },
          { name: '上级合同' },
          { name: '合同编码' },
          { name: '合同名称' },
          { name: '合同类型' },
          { name: '合同状态' },
          { name: '合同日期' },
          { name: '客户' },
          { name: '币种' },
          { name: '合同总金额' },
          { name: '累计出库金额' },
          { name: '未出库金额' },
          { name: '累计签收金额' },
          { name: '累计途损金额' },
          { name: '未签收金额' },
          { name: '累计收入确认金额' },
          { name: '未收入确认金额' },
          { name: '累计开票金额' },
          { name: '未开票金额' },
          { name: '累计收款金额' },
          { name: '未收款金额' },
        ], //所有表格项
        filedNow: [], //当前表格项
        searchAll: this.getFiled(), //所有搜索项
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        localKey: 'cwgl-srgl-srhtzxqkcx-search',
        tableKey: 'cwgl-srgl-srhtzxqkcx-list',
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
          { name: '所属组织', key: 'sxName' },
          { name: '时间', key: 'startStatus' },
          { name: '合同名称', key: 'startStatus1' },
          { name: '客户', key: 'startStatus2' },
          { name: '合同类型', key: 'startStatus2' },
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
          contractNo: undefined,
          contractName: undefined,
          customerName: undefined,
          contractStatus: undefined,
          contractType: undefined,
          pageNumber: 1,
          pageSize: 10,
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
        let { ...other } = this.queryForm
        try {
          const response = await getRevenueContractList({
            ...other,
          })
          // 从后端响应中获取数据
          const data = response.data || {}
          this.list = data.tlist || []
          this.total = data.totalRecord || 0
          this.currProjectId = data.currProjectId || ''
          if (this.list.length > 0 && this.list[0].contractNo) {
            this.planNum = this.list[0].contractNo
          }
        } catch (error) {
          console.error('获取收入合同执行情况列表失败:', error)
          this.list = []
          this.total = 0
        } finally {
          this.listLoading = false
        }
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
          const { msg, code } = await deleteRevenueContract(row.contractId || row.id)
          if (code == 1) {
            this.$baseMessage(msg || '删除成功', 'success')
            await this.fetchData()
          } else {
            this.$baseMessage(msg || '删除失败', 'error')
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
