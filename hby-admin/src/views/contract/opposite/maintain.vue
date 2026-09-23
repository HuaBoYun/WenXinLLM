<template>
  <div class="system-log-container">
    <vab-query-form>
      <el-card shadow="never">
        <vab-query-form-left-panel :span="18">
          <el-form
            ref="form"
            :inline="true"
            label-width="0"
            :model="queryForm"
            @submit.native.prevent
          >
            <el-form-item v-for="(item, index) in searchItem" :key="index">
              <el-input
                v-model="queryForm.budgetname"
                clearable
                placeholder="相对方名称"
                v-if="item.name === '相对方名称'"
              />
              <el-input
                v-model="queryForm.counterpartno"
                clearable
                placeholder="相对方编号"
                v-if="item.name === '相对方编号'"
              />
              <el-input
                v-model="queryForm.projectstagegoal"
                clearable
                placeholder="法定代表人"
                v-if="item.name === '法定代表人'"
              />
            </el-form-item>
            <!-- <el-form-item>
              <el-input
                v-model="queryForm.counterpartaddress"
                clearable
                placeholder="相对方地址"
              />
            </el-form-item> -->
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
        <el-button type="success" @click="handleBank()">银行账户管理</el-button>
        <el-button type="success" @click="handleEdit()">新建</el-button>
      </vab-query-form-right-panel>
      <el-table
        v-loading="listLoading"
        :data="list"
        highlight-current-row
        @current-change="handleRowChange"
      >
        <el-table-column
          align="center"
          label="相对方编号"
          prop="counterpartno"
        />
        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            align="center"
            label="相对方名称"
            prop="budgetname"
            v-if="item.name === '相对方名称'"
          >
            <template #default="{ row }">
              <el-button type="text" @click.stop="handleDetail(row)">
                {{ row.budgetname }}
              </el-button>
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="法定代表人"
            prop="projectstagegoal"
            v-if="item.name === '法定代表人'"
          />
          <!-- <el-table-column align="center" label="相对方地址" prop="counterpartaddress" show-overflow-tooltip /> -->
          <el-table-column
            align="center"
            label="注册资本"
            prop="totaltmoney"
            v-if="item.name === '注册资本'"
          />
          <el-table-column
            align="center"
            label="企业状态"
            prop="resultdescription"
            v-if="item.name === '企业状态'"
          />
          <el-table-column
            align="center"
            label="审批状态"
            prop="inspectionstatus"
            v-if="item.name === '审批状态'"
          >
            <template #default="{ row }">
              {{ mapStatus(row) }}
            </template>
          </el-table-column>

          <el-table-column
            align="center"
            label="黑名单状态"
            prop="inspectionstatus"
            v-if="item.name === '黑名单状态'"
          >
            <template #default="{ row }">
              {{ mapStatus1(row) }}
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
            <el-button
              type="text"
              @click.stop="handleEdit(row)"
              :disabled="row.inspectionstatus"
            >
              修改
            </el-button>
            <el-dropdown style="margin-left: 10px" @command="handleCommand">
              <el-button type="text">更多</el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item
                  :disabled="!row.inspectionstatus"
                  @click.native="handleManage(row)"
                >
                  办理
                </el-dropdown-item>
                <el-dropdown-item
                  :disabled="
                    !(
                      row.hasOwnProperty('blackRecord') &&
                      row.blackRecord.aprstatus > 0
                    )
                  "
                  @click.native="handleHMDManage(row)"
                >
                  黑名单办理
                </el-dropdown-item>
                <el-dropdown-item
                  @click.native="handleApproval(row)"
                  :disabled="row.inspectionstatus"
                >
                  提交审批
                </el-dropdown-item>
                <el-dropdown-item
                  @click.native="addBlack(row)"
                  :disabled="
                    !(
                      row.inspectionstatus == 6 &&
                      !row.hasOwnProperty('blackRecord')
                    )
                  "
                >
                  加入黑名单
                </el-dropdown-item>
                <el-dropdown-item
                  v-if="FWNames()"
                  :disabled="row.inspectionstatus"
                  @click.native="handleDelete(row)"
                >
                  删除
                </el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
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
    <MaintainDetail ref="detail" />
    <MaintainEdit ref="edit" @fetch-data="fetchData" />
    <BlacklistForm ref="black" @fetch-data="fetchData" />
    <MaintainBlackEdit ref="blackEdit" @fetch-data="fetchData" />
    <BankDialog ref="bank-dialog" />
    <Deal ref="deal" />
    <ProcessList ref="process" />
    <WfqdDeal ref="wfqddeal" />
  </div>
</template>

<script>
  import { deleteOpposite, getOppositeList } from '@/api/contract/opposite'
  import filterSearch from '@/components/filterSearch.vue'
  import filterTable from '@/components/filterTable.vue'
  import Deal from '@/views/contract/contractManage/components/Deal.vue'
  import ProcessList from '@/views/contract/contractManage/components/ProcessList.vue'
  import MaintainDetail from '@/views/contract/opposite/components/MaintainDetail'
  import MaintainEdit from '@/views/contract/opposite/components/MaintainEdit'
  import MaintainBlackEdit from '@/views/contract/opposite/components/MaintainBlackEdit'

  import BankDialog from './components/BankDialog.vue'
  import BlacklistForm from './components/BlacklistForm.vue'
  import WfqdDeal from '@/views/msg/components/options/WfqdDeal'
  import { getFlowPkInfo } from '@/api/contract/manage'

  export default {
    name: 'Maintain',
    components: {
      filterTable,
      filterSearch,
      MaintainEdit,
      MaintainDetail,
      MaintainBlackEdit,
      BlacklistForm,
      BankDialog,
      Deal,
      ProcessList,
      WfqdDeal,
    },
    data() {
      return {
        list: [],
        listLoading: true,
        isBlackAdmin: 1,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {},
        current: undefined,

        localKey: 'contract-opposite-maintain-search',
        tableKey: 'contract-opposite-maintain-list',
        searchNow: [],
        searchMore: true,
        searchItem: [],
        searchAll: this.getFiled(), //所有搜索项
        filedNow: [],
        filedAll: [
          { name: '相对方名称' },
          { name: '法定代表人' },
          { name: '注册资本' },
          { name: '企业状态' },
          { name: '审批状态' },
          { name: '黑名单状态' },
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
    mounted() {
      this.$bus.on('updateMsg', (value) => {
        if (value == 0) {
          this.fetchData()
        }
      })
    },
    methods: {
      async handleManage(row) {
        const res = await getFlowPkInfo({
          formId: row.budgetid,
          tableId: 2,
        })

        this.$refs.wfqddeal.show(res.data, false)
      },
      //黑名单办理
      async handleHMDManage(row) {
        const res = await getFlowPkInfo({
          formId: row.blackRecord.brid,
          tableId: 3,
        })
        this.$refs.wfqddeal.show(res.data, false)
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
          { name: '相对方名称', key: 'budgetname' },
          { name: '相对方编号', key: 'counterpartno' },
          { name: '法定代表人', key: 'projectstagegoal' },
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
      //数据处理
      FWNames() {
        let use = window.localStorage.getItem('userInfo')
        return JSON.parse(use).linkDetp.orgname.indexOf('法务') > 0
      },
      //前置条件
      addBlack(row) {
        if (row.blackAprStatus == 6) {
          this.$baseMessage(
            '已加入黑名单的相对方,无法再次加入黑名单',
            'error',
            'vab-hey-message-error'
          )
        } else if (row.blackAprStatus == 0 || !row.blackAprStatus) {
          this.$refs.blackEdit.showDetail(row)
        } else {
          this.$baseMessage(
            '黑名单审批中,无法再次加入黑名单',
            'error',
            'vab-hey-message-error'
          )
        }
      },
      resetQueryForm() {
        this.queryForm = {
          budgetname: undefined,
          // counterpartno: undefined,
          projectstagegoal: undefined,
          // counterpartaddress: undefined,
          flowId: 622322,
          pageNumber: 1,
          pageSize: 20,
        }
      },
      resetSearch() {
        this.resetQueryForm()
        this.fetchData()
      },

      mapStatus(row) {
        const { inspectionstatus, blackAprStatus } = row
        if (blackAprStatus) {
          if (blackAprStatus == 0 || blackAprStatus == 2) {
            return inspectionstatus == 1
              ? '审批中'
              : inspectionstatus == 2
              ? '已退回'
              : inspectionstatus == 3
              ? '已撤销'
              : inspectionstatus == 4
              ? '已终止'
              : inspectionstatus == 5
              ? '已跟踪'
              : inspectionstatus == 6
              ? '已完成'
              : '未审批'
          } else if (blackAprStatus == 6) {
            return '已加入黑名单'
          } else {
            return '加入黑名单审批中'
          }
        } else {
          return inspectionstatus == 1
            ? '审批中'
            : inspectionstatus == 2
            ? '已退回'
            : inspectionstatus == 3
            ? '已撤销'
            : inspectionstatus == 4
            ? '已终止'
            : inspectionstatus == 5
            ? '已跟踪'
            : inspectionstatus == 6
            ? '已完成'
            : '未审批'
        }
      },
      mapStatus1(row) {
        if (row.hasOwnProperty('blackRecord')) {
          return row.blackRecord.aprstatus == 1
            ? '审批中'
            : row.blackRecord.aprstatus == 2
            ? '已退回'
            : row.blackRecord.aprstatus == 3
            ? '已撤销'
            : row.blackRecord.aprstatus == 4
            ? '已终止'
            : row.blackRecord.aprstatus == 5
            ? '已跟踪'
            : row.blackRecord.aprstatus == 6
            ? '已加入黑名单'
            : '未审批'
        } else {
          return '未加入黑名单'
        }
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
          data: { tlist, totalRecord },
          isBlackAdmin,
        } = await getOppositeList(this.queryForm)
        this.list = tlist
        this.isBlackAdmin = isBlackAdmin
        this.total = totalRecord
        this.listLoading = false
      },
      handleApproval(row) {
        const tableId = 2
        const fromId = row.budgetid
        this.$refs['process'].save(tableId, fromId)
      },
      handleDeal(row) {
        if (row.inspectionstatus == -1 || row.inspectionstatus == 0) {
          this.$baseMessage('请提交审批', 'error', 'vab-hey-message-error')
          return
        }
        this.$refs['deal'].show(row, 'opposite')
      },
      /**
       * @description: 打开详情表单弹框
       * @param {*} row 选择的行数据
       * @return {*}
       */
      handleDetail(row) {
        this.$refs['detail'].showDetail(row)
      },
      async handleEdit(row) {
        // if (row && this.mapStatus(row) != "需调整") {
        //   const { code } = await checkStatus({ budgetId: row.budgetid });
        //   if (code != 1) return;
        // }
        this.$refs['edit'].showEdit(row)
      },
      //相对方校验
      handleBank() {
        if (!this.current) {
          this.$baseMessage('请先选择相对方', 'error', 'vab-hey-message-error')
          return
        }
        this.$refs['bank-dialog'].show(this.current)
      },
      async handleDelete(row) {
        // const { code } = await checkStatus({ budgetId: row.budgetid });
        // if (code != 1) return;
        const userInfo = window.localStorage.getItem('userInfo')
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg, code } = await deleteOpposite({
            budgetId: row.budgetid,
            staffId: JSON.parse(userInfo).staffid,
            // flowId: this.queryForm.flowId,
          })
          if (code == '1') {
            this.$baseMessage(msg, 'success', 'vab-hey-message-success')
          }
          await this.fetchData()
        })
      },
      //保存
      handleCommand(command) {
        switch (command) {
          case 'copy':
            this.$refs.copyToIndustry.showEdit()
            break
        }
      },
      handleRowChange(val) {
        this.current = val
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
