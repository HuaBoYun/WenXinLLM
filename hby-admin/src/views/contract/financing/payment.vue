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
                v-model="queryForm.paymenttitle"
                clearable
                placeholder="标题"
                v-if="item.name === '标题'"
              />
              <el-input
                v-model="queryForm.budgetname"
                clearable
                placeholder="收款单位"
                v-if="item.name === '收款单位'"
              />
              <el-input
                v-model="queryForm.contractname"
                clearable
                placeholder="合同名称"
                v-if="item.name === '合同名称'"
              />
              <el-input
                v-model="queryForm.contractno"
                clearable
                placeholder="合同编号"
                v-if="item.name === '合同编号'"
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
        <el-button type="success" @click="handleAdd">新增</el-button>
      </vab-query-form-right-panel>
      <el-table v-loading="listLoading" :data="list">
        <el-table-column align="center" label="标题" prop="paymenttitle">
          <template #default="{ row }">
            <el-button type="text" @click="handleDetail(row)">
              {{ row.paymenttitle }}
            </el-button>
          </template>
        </el-table-column>
        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            align="center"
            label="收款单位"
            prop="budgetname"
            v-if="item.name === '收款单位'"
          />
          <el-table-column
            align="center"
            label="合同编号"
            prop="contractno"
            v-if="item.name === '合同编号'"
          />
          <el-table-column
            align="center"
            label="合同名称"
            prop="contractname"
            v-if="item.name === '合同名称'"
          />
          <el-table-column
            align="center"
            label="付款时间"
            prop="paymentlatedate"
            v-if="item.name === '付款时间'"
          />
          <el-table-column
            align="center"
            label="付款金额"
            prop="paymenmoney"
            v-if="item.name === '付款金额'"
          />
          <!-- 1审批中，2需调整，3已通过，4已终止，5已跟踪，6已完成 -->
          <el-table-column
            align="center"
            label="审批状态"
            prop="paymentstatus"
            v-if="item.name === '审批状态'"
          >
            <template #default="{ row }">
              {{
                row.paymentstatus == 1
                  ? '审批中'
                  : row.paymentstatus == 2
                  ? '已退回'
                  : row.paymentstatus == 3
                  ? '已撤回'
                  : row.paymentstatus == 4
                  ? '已终止'
                  : row.paymentstatus == 5
                  ? '已跟踪'
                  : row.paymentstatus == 6
                  ? '已完成'
                  : '未审批'
              }}
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
              @click="handleEdit(row)"
              :disabled="!!row.paymentstatus"
            >
              修改
            </el-button>
            <el-dropdown style="margin-left: 10px" @command="handleCommand">
              <el-button type="text">更多</el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item @click.native="handleManage(row)">
                  <el-button type="text" :disabled="!row.paymentstatus">
                    办理
                  </el-button>
                </el-dropdown-item>
                <el-dropdown-item @click.native="handleApproval(row)">
                  <el-button type="text" :disabled="!!row.paymentstatus">
                    提交审批
                  </el-button>
                </el-dropdown-item>
                <el-dropdown-item @click.native="handleDelete(row)">
                  <el-button type="text" :disabled="!!row.paymentstatus">
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
      class="pagination"
      :current-page="queryForm.pageNo"
      :layout="layout"
      :page-size="queryForm.pageSize"
      :total="total"
      @current-change="handleCurrentChange"
      @size-change="handleSizeChange"
    />
    <PaymentEdit ref="edit" @fetch-data="fetchData" />
    <PaymentBanli ref="banli" @fetch-data="fetchData" />
    <ProcessList ref="process" @fetchData="fetchData" />
    <WfqdDeal ref="wfqdDeal" />
  </div>
</template>

<script>
  import {
    getPaymentManagemen,
    removePayment,
    fkglTjsp,
  } from '@/api/contract/financing'
  import PaymentEdit from '@/views/contract/financing/components/PaymentEdit'
  import PaymentBanli from '@/views/contract/financing/components/PaymentBanli'
  import filterSearch from '@/components/filterSearch.vue'
  import filterTable from '@/components/filterTable.vue'
  import { getFlowPkInfo } from '@/api/contract/manage.js'
  export default {
    name: 'Payment',
    components: {
      PaymentEdit,
      PaymentBanli,
      filterTable,
      filterSearch,
      ProcessList: () =>
        import('@/views/contract/contractManage/components/ProcessList'),
      WfqdDeal: () => import('@/views/msg/components/options/WfqdDeal'),
    },
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          paymenttitle: '',
          budgetname: '',
          contractname: '',
          contractno: '',
          pageNumber: 1,
          pageSize: 10,
          flowid: '759004',
        },

        localKey: 'contract-financing-payment-search',
        tableKey: 'contract-financing-payment-list',
        searchNow: [],
        searchMore: true,
        searchItem: [],
        searchAll: this.getFiled(), //所有搜索项
        filedNow: [],
        filedAll: [
          { name: '收款单位' },
          { name: '合同编号' },
          { name: '合同名称' },
          { name: '付款时间' },
          { name: '付款金额' },
          { name: '审批状态' },
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
          { name: '标题', key: 'paymenttitle' },
          { name: '收款单位', key: 'budgetname' },
          { name: '合同名称', key: 'contractname' },
          { name: '合同编号', key: 'contractno' },
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
      resetQueryForm() {
        this.queryForm = {
          paymenttitle: '',
          budgetname: '',
          contractname: '',
          contractno: '',
          pageNumber: 1,
          pageSize: 10,
          flowid: '759004',
        }
      },
      resetSearch() {
        this.resetQueryForm()
        this.fetchData()
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
        } = await getPaymentManagemen(this.queryForm)
        this.list = tlist
        this.total = totalRecord
        this.listLoading = false
      },
      checkStatus(row) {
        console.warn('checkStatus', row)
        if (row.paymentstatus >= 1) {
          return false
        }
        return true
      },
      handleAdd() {
        this.$refs['edit'].showEdit()
      },
      handleEdit(row) {
        if (!this.checkStatus(row) && row.paymentstatus != 2) {
          this.$message.error('已提交审批，无法修改！')
          return
        }
        this.$refs['edit'].showEdit(row)
      },
      /**
       * @description: 打开详情表单弹框
       * @param {*} row 选择的行数据
       * @return {*}
       */
      handleDetail(row) {
        this.$refs['edit'].showDetail(row)
      },
      handleDelete(row) {
        if (!this.checkStatus(row)) {
          this.$message.error('已提交审批，无法删除！')
          return
        }
        //
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg } = await removePayment({ parmentId: row.paymentid })
          this.$baseMessage(msg, 'success', 'vab-hey-message-success')
          await this.fetchData()
        })
      },
      //提交
      handleApprove(row) {
        this.$baseConfirm('是否提交', null, async () => {
          const { code, msg } = await fkglTjsp({
            parmentId: row.paymentid,
            flowid: this.queryForm.flowid,
          })
          if (code == 1) {
            this.$baseMessage(msg, 'success', 'vab-hey-message-success')
            await this.fetchData()
          }
        })
      },
      //流程提交
      handleBanli(row) {
        if (this.checkStatus(row)) {
          this.$message.error('请提交审批！')
          return
        }
        this.$refs['banli'].showEdit(row)
      },
      sendModel() {
        this.$refs['sendModel'].showEdit()
      },
      send() {
        this.$refs['send'].showEdit()
      },
      handleCommand() {
        //
      },
      handleApproval(row) {
        try {
          this.btnLoading = true
          this.$refs['process'].save(217, row.paymentid)
        } catch (error) {
          this.btnLoading = false
        }
      },
      async handleManage(row) {
        this.listLoading = true
        const res = await getFlowPkInfo({
          formId: row.paymentid,
          tableId: 217,
        })
        this.listLoading = false
        this.$refs['wfqdDeal'].show(res.data, false)
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
