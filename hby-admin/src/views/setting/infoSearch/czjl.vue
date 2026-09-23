<template>
  <div class="system-log-container">
    <vab-query-form>
      <vab-query-form-left-panel>
        <el-form
          ref="form"
          :inline="true"
          label-width="100px"
          :model="queryForm"
          @submit.native.prevent
        >
          <el-form-item label="当前余额">
            <span>{{ money }}元</span>
          </el-form-item>
        </el-form>
      </vab-query-form-left-panel>
      <vab-query-form-right-panel>
        <el-button type="success" @click="handleEdit" v-if="hasAuth('CZJLcz')">
          充值
        </el-button>
      </vab-query-form-right-panel>
    </vab-query-form>

    <el-table v-loading="listLoading" :data="list" @sort-change="sortChange">
      <el-table-column
        align="center"
        label="订单编号"
        prop="orderno"
        show-overflow-tooltip
        sortable="custom"
      />
      <!-- <el-table-column
        align="center"
        label="创建人"
        prop="orgid"
        show-overflow-tooltip
      /> -->
      <el-table-column align="center" label="创建人">
        <template #default="{ row }">
          {{ row.tblStaff.realname }}
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        label="交易金额"
        prop="ordermoney"
        show-overflow-tooltip
      />
      <el-table-column
        align="center"
        label="创建时间"
        prop="createdate"
        show-overflow-tooltip
        sortable="custom"
      />
      <el-table-column
        align="center"
        label="订单状态"
        prop="status"
        show-overflow-tooltip
      >
        <template #default="{ row }">
          {{ ['未支付', '处理中', '已完成'][row.status - 1] }}
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      background
      :current-page="queryForm.pageNumber"
      :layout="layout"
      :page-size="queryForm.pageSize"
      :total="total"
      @current-change="handleCurrentChange"
      @size-change="handleSizeChange"
    />
    <czjl-edit ref="edit" @fetch-data="fetchData" />
  </div>
</template>

<script>
  import { getChargeRecord } from '@/api/setting/infoSearch'
  import CzjlEdit from './components/CzjlEdit'
  import * as dayjs from 'dayjs'
  import { hasAuth } from '@/utils'

  export default {
    name: 'SystemLog',
    components: { CzjlEdit },
    data() {
      return {
        money: 0,
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          pageNumber: 1,
          pageSize: 20,
        },
        sortFields: '',
        sortFlag: 'asc',
      }
    },
    created() {
      this.fetchData()
    },
    methods: {
      async sortChange(column) {
        let { order, prop } = column
        let p = prop
        this.sortFields = p || ''
        if (order === 'ascending') {
          this.sortFlag = 'asc'
        } else if (order === 'descending') {
          this.sortFlag = 'desc'
        } else {
          this.sortFlag = ''
        }
        await this.fetchData()
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
        const {
          data: {
            money,
            pageInfo: { tlist, totalRecord },
          },
        } = await getChargeRecord({...this.queryForm,
          sortFields: this.sortFields,
          sortFlag: this.sortFlag})
        this.money = money
        this.list = tlist.map((i) => {
          return {
            ...i,
            createdate: dayjs(i.createdate).format('YYYY-MM-DD HH:mm:ss'),
          }
        })
        this.total = totalRecord
        this.listLoading = false
      },
      handleEdit() {
        this.$refs['edit'].showEdit(this.money)
      },
    },
  }
</script>
