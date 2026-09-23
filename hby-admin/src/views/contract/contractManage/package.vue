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
                v-model="queryForm.contractno"
                clearable
                placeholder="合同编号"
                v-if="item.name === '合同编号'"
              />
              <el-input
                v-model="queryForm.contractname"
                clearable
                placeholder="合同名称"
                v-if="item.name === '合同名称'"
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
            <!-- <i class="el-icon-delete" slot="reference"></i> -->
            <el-button
              slot="reference"
              icon="el-icon-s-grid"
              class="biaoge"
              style="margin-bottom: 10px; margin-right: 10px"
            ></el-button>
          </el-popover>
        </el-tooltip>
        <el-button native-type="submit" type="primary" @click="handleHistory">
          借阅记录
        </el-button>
        <el-button native-type="submit" type="primary" @click="handleBorrow">
          申请借阅
        </el-button>
      </vab-query-form-right-panel>
      <el-table v-loading="listLoading" ref="myTable" :data="list">
        <el-table-column align="center" label="合同编号" prop="contractno" />
        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            align="center"
            label="合同名称"
            prop="contractname"
            v-if="item.name === '合同名称'"
          >
            <template #default="{ row }">
              <el-button type="text" @click="handleDetail(row)">
                {{ row.contractname }}
              </el-button>
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="合同类型"
            prop="contracttype"
            v-if="item.name === '合同类型'"
          />
          <el-table-column
            align="center"
            label="收付款方向"
            prop="dctype"
            v-if="item.name === '收付款方向'"
          />
          <el-table-column
            align="center"
            label="合同金额"
            prop="contractmoney"
            v-if="item.name === '合同金额'"
          />
          <el-table-column
            align="center"
            label="归档来源"
            prop="hiscontractstatus"
            v-if="item.name === '归档来源'"
          >
            <template #default="{ row }">
              {{ mapHistoryStatus(row) }}
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="审批状态"
            prop="contractstatus"
            v-if="item.name === '审批状态'"
          >
            <template #default="{ row }">
              {{ mapContractStatus(row) }}
            </template>
          </el-table-column>
        </div>
        <el-table-column
          align="center"
          label="操作"
          show-overflow-tooltip
          width="120"
        >
          <template #default="{ row }">
            <!-- <el-button type="text" @click="handleHistory(row)">详情</el-button> -->
            <el-button
              :disabled="row.contractstatus == 8"
              type="text"
              @click="handleFil(row)"
            >
              归档
            </el-button>
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
    <BorrowEdit ref="edit" @fetch-data="fetchData" />
    <BorrowHistory ref="history" />
    <CreateDetail ref="common" @fetch-data="fetchData" />
  </div>
</template>

<script>
  import {
    archiveContract,
    checkLendStatus,
    getContractArchiveList,
  } from '@/api/contract/manage'
  import BorrowEdit from './components/BorrowEdit.vue'
  import BorrowHistory from './components/BorrowHistory.vue'
  // import ChangeDetail from './components/contractsEdit/ChangeDetail.vue'
  import CreateDetail from './components/contractsEdit/CreateDetail.vue'
  import filterSearch from '@/components/filterSearch.vue'
  import filterTable from '@/components/filterTable.vue'

  export default {
    name: 'Package',
    components: {
      BorrowEdit,
      BorrowHistory,
      CreateDetail,
      filterSearch,
      filterTable,
    },
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {},
        currentEdit: 'moren',
        curSelected: null,

        localKey: 'contract-contractManage-package-search',
        tableKey: 'contract-contractManage-package-list',
        searchNow: [],
        searchMore: true,
        searchItem: [],
        searchAll: this.getFiled(), //所有搜索项
        filedNow: [],
        filedAll: [
          { name: '合同名称' },
          { name: '合同类型' },
          { name: '收付款方向' },
          { name: '合同金额' },
          { name: '归档来源' },
          { name: '审批状态' },
        ],
      }
    },
    created() {
      this.resetQueryForm()
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
          { name: '合同编号', key: 'contractno' },
          { name: '合同名称', key: 'contractname' },
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
      selectAll() {
        this.curSelected = null
        this.$refs.myTable.clearSelection()
      },
      //多选
      handleSelectionChange(selection, row) {
        this.singleSelect(row)
      },
      handleRowChange(row) {
        this.singleSelect(row)
      },
      singleSelect(row) {
        this.curSelected = row
        this.$refs.myTable.clearSelection()
        this.$refs.myTable.toggleRowSelection(row)
      },
      //重置
      resetQueryForm() {
        this.queryForm = {
          contractno: undefined,
          contractname: undefined,
          flowId: 622327,
          pageNumber: 1,
          pageSize: 20,
        }
      },
      //重置
      resetSearch() {
        this.resetQueryForm()
        this.fetchData()
      },
      mapContractStatus(row) {
        // const res = contractStatusOptions.filter((item) => {
        //   return item.value === row.contractstatus
        // })
        // return res[0].label
        if (row.contractstatus !== 8) {
          return '待归档'
        } else {
          return '已归档'
        }
      },
      mapHistoryStatus(row) {
        if (row.contractstatus == 8) {
          switch (row.hiscontractstatus) {
            case 10:
              return '变更归档'
            case 11:
              return '终止归档'
            case 7:
            case 8:
            case 9:
            case 12:
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
              return '正常归档'
            default:
              return '正常归档'
          }
        } else {
          return ''
        }
      },
      /**
       * @description: 打开详情表单弹框
       * @param {*} row 选择的行数据
       * @return {*}
       */
      async handleDetail(row) {
        this.$refs['common'].showDetail(row, row.contracttype)
        // const { code, msg } = await checkLendStatus({
        //   contractId: row.contractid,
        // })

        // if (code === 2) {
        //   this.$refs['common'].showDetail(row, row.contracttype)
        // } else {
        //   this.$baseMessage(msg, 'info', 'vab-hey-message-error')
        // }
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
        const {
          data: {
            pageInfo: { tlist, totalRecord },
          },
        } = await getContractArchiveList(this.queryForm)
        this.list = tlist
        this.total = totalRecord
        this.listLoading = false
      },
      handleHistory() {
        this.$refs['history'].show()
      },
      //归档方法
      handleFil(row) {
        this.$baseConfirm('你确定要归档吗', null, async () => {
          await archiveContract({
            contractId: row.contractid,
          })
          await this.fetchData()
        })
      },
      handleBorrow() {
        this.$refs['edit'].showEdit()
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
