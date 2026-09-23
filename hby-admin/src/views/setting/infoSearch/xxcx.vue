<template>
  <div class="system-log-container">
    <el-table v-loading="listLoading" :data="list" @sort-change="sortChange">
      <el-table-column
        align="center"
        label="报告名称"
        prop="reportName"
        show-overflow-tooltip
      />
      <el-table-column
        align="center"
        label="收费规则"
        prop="paymoney"
        show-overflow-tooltip
      />
      <el-table-column
        align="center"
        label="查询时间"
        prop="querytime"
        show-overflow-tooltip
        sortable="custom"
      />
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
  </div>
</template>

<script>
  import { getPersonalList } from '@/api/setting/infoSearch'
  import * as dayjs from 'dayjs'
  export default {
    name: 'Xxcx',
    data() {
      return {
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
          data: { tlist, totalRecord },
        } = await getPersonalList({...this.queryForm,
          sortFields: this.sortFields,
          sortFlag: this.sortFlag,})
        this.list = tlist.map((i) => {
          return {
            ...i,
            querytime: dayjs(i.querytime).format('YYYY-MM-DD HH:mm:ss'),
          }
        })
        this.total = totalRecord
        this.listLoading = false
      },
    },
  }
</script>
