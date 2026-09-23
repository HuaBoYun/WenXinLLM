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
                v-model="queryForm.invoiceno"
                clearable
                placeholder="发票号"
                v-if="item.name === '发票号'"
              />
              <el-input
                v-model="queryForm.invoicecontent"
                clearable
                placeholder="发票内容"
                v-if="item.name === '发票内容'"
              />
              <el-input
                v-model="queryForm.invoiceheadtext"
                clearable
                placeholder="发票抬头"
                v-if="item.name === '发票抬头'"
              />
              <el-select
                v-model="queryForm.invoicetype"
                placeholder="发票类型"
                v-if="item.name === '发票类型'"
              >
                <el-option label="增值税专用发票" value="增值税专用发票" />
                <el-option label="增值税普通发票" value="增值税普通发票" />
              </el-select>
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
        <el-button type="success" @click="handleAdd">新建</el-button>
      </vab-query-form-right-panel>
      <el-table v-loading="listLoading" :data="list">
        <el-table-column align="center" label="发票号" prop="invoiceno">
          <template #default="{ row }">
            <el-button type="text" @click="handleDetail(row)">
              {{ row.invoiceno }}
            </el-button>
          </template>
        </el-table-column>
        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            align="center"
            label="发票内容"
            prop="invoicecontent"
            v-if="item.name === '发票内容'"
          />
          <el-table-column
            align="center"
            label="发票抬头"
            prop="invoiceheadtext"
            v-if="item.name === '发票抬头'"
          />
          <el-table-column
            align="center"
            label="票点(%)"
            prop="invoicepost"
            v-if="item.name === '票点(%)'"
          />
          <el-table-column
            align="center"
            label="收票日期"
            prop="invoicespdate"
            v-if="item.name === '收票日期'"
          />
          <el-table-column
            align="center"
            label="发票开具日期"
            prop="invoicedate"
            v-if="item.name === '发票开具日期'"
          />
          <el-table-column
            align="center"
            label="发票类型"
            prop="invoicetype"
            v-if="item.name === '发票类型'"
          />
          <el-table-column
            align="center"
            label="发票金额(元)"
            prop="invoicemoney"
            v-if="item.name === '发票金额(元)'"
          />
          <el-table-column
            align="center"
            label="发票状态"
            prop="invoicestatus"
            v-if="item.name === '发票状态'"
          >
            <!-- 1-未开票，2-已开票，3-未收款，4-已收款，5-已退票 -->
            <template #default="{ row }">
              <span v-if="row.invoicestatus == 1">未开票</span>
              <span v-if="row.invoicestatus == 2">已开票</span>
              <span v-if="row.invoicestatus == 3">未收款</span>
              <span v-if="row.invoicestatus == 4">已收款</span>
              <span v-if="row.invoicestatus == 5">已退票</span>
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
            <el-button type="text" @click="handleEdit(row)">修改</el-button>
            <el-dropdown style="margin-left: 10px" @command="handleCommand">
              <el-button type="text">更多</el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item @click.native="handleStatus(row, 1)">
                  未开票
                </el-dropdown-item>
                <el-dropdown-item @click.native="handleStatus(row, 2)">
                  已开票
                </el-dropdown-item>
                <el-dropdown-item @click.native="handleStatus(row, 3)">
                  未付款
                </el-dropdown-item>
                <el-dropdown-item @click.native="handleStatus(row, 4)">
                  已付款
                </el-dropdown-item>
                <el-dropdown-item @click.native="handleStatus(row, 5)">
                  已退款
                </el-dropdown-item>
                <el-dropdown-item @click.native="handleDelete(row)">
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
      :current-page="queryForm.pageNo"
      :layout="layout"
      :page-size="queryForm.pageSize"
      :total="total"
      @current-change="handleCurrentChange"
      @size-change="handleSizeChange"
    />
    <BillEdit ref="edit" @fetch-data="fetchData" />
  </div>
</template>

<script>
  import {
    getInvoicesManageMen,
    removeInvoice,
    InvoiceStatus,
  } from '@/api/contract/financing'
  import BillEdit from '@/views/contract/financing/components/BillEdit'
  import filterSearch from '@/components/filterSearch.vue'
  import filterTable from '@/components/filterTable.vue'

  export default {
    name: 'Bill',
    components: { BillEdit, filterTable, filterSearch },
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          invoiceno: '',
          invoicecontent: '',
          invoiceheadtext: '',
          invoicetype: '',
          pageNumber: 1,
          pageSize: 10,
          flowid: '765537',
        },

        localKey: 'contract-financing-bill-search',
        tableKey: 'contract-financing-bill-list',
        searchNow: [],
        searchMore: true,
        searchItem: [],
        searchAll: this.getFiled(), //所有搜索项
        filedNow: [],
        filedAll: [
          { name: '发票内容' },
          { name: '发票抬头' },
          { name: '票点(%)' },
          { name: '收票日期' },
          { name: '发票开具日期' },
          { name: '发票类型' },
          { name: '发票金额(元)' },
          { name: '发票状态' },
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
          { name: '发票号', key: 'invoiceno' },
          { name: '发票内容', key: 'invoicecontent' },
          { name: '发票抬头', key: 'invoiceheadtext' },
          { name: '发票类型', key: 'invoicetype' },
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
          invoiceno: '',
          invoicecontent: '',
          invoiceheadtext: '',
          invoicetype: '',
          pageNumber: 1,
          pageSize: 10,
          flowid: '765537',
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
          date: { tlist, totalRecord },
        } = await getInvoicesManageMen(this.queryForm)
        this.list = tlist.map((i) => {
          // const date =
          //   new Date(i.invoicespdate).getFullYear() +
          //   '-' +
          //   new Date(i.invoicespdate).getMonth() +
          //   '-' +
          //   new Date(i.invoicespdate).getDate()
          return {
            ...i,
            // invoicespdate: date,
          }
        })
        this.total = totalRecord
        this.listLoading = false
      },
      //过滤状态
      checkStatus(row) {
        if (row.invoicestatus > 1) {
          return false
        }
        return true
      },
      handleAdd() {
        this.$refs['edit'].showEdit()
      },
      handleEdit(row) {
        if (!this.checkStatus(row)) {
          this.$message.error('发票已开票，无法修改！')
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
          this.$message.error('发票已开票，无法删除！')
          return
        }
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg } = await removeInvoice({ invoiceId: row.invoiceid })
          this.$baseMessage(msg, 'success', 'vab-hey-message-success')
          await this.fetchData()
        })
      },
      //过滤状态
      handleStatus(row, status) {
        this.$baseConfirm('是否操作', null, async () => {
          const { msg, code } = await InvoiceStatus({
            invoiceId: row.invoiceid,
            status: status,
          })
          let msgText = ''
          if (status == 1) {
            msgText = '未开票'
          } else if (status == 2) {
            msgText = '已开票'
          } else if (status == 3) {
            msgText = '未付款'
          } else if (status == 4) {
            msgText = '已付款'
          } else if (status == 5) {
            msgText = '已退款'
          }
          if (code == 1) {
            this.$baseMessage(
              msgText + '成功',
              'success',
              'vab-hey-message-success'
            )
          } else {
            this.$baseMessage(msg, 'error', 'vab-hey-message-error')
          }

          await this.fetchData()
        })
      },
      sendModel() {
        this.$refs['sendModel'].showEdit()
      },
      send() {
        this.$refs['send'].showEdit()
      },
      handleCommand(command) {},
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
