<template>
  <el-dialog
    :title="title"
    :visible.sync="dialogTableVisible"
    width="500px"
    @close="close"
    :close-on-click-modal="false"
  >
    <el-table v-loading="listLoading" :data="list">
      <el-table-column
        align="center"
        label="报告名称"
        prop="reportName"
        show-overflow-tooltip
      />
      <el-table-column align="center" label="费用" prop="paymoney" />
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
  </el-dialog>
</template>
<script>
  import { getCostStatisticsDetail } from '@/api/setting/infoSearch'

  export default {
    name: 'Fymx',
    data() {
      return {
        dialogTableVisible: false,
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          recordId: undefined,
          pageNumber: 1,
          pageSize: 20,
        },
        title: '费用明细',
      }
    },
    methods: {
      showDetail(row) {
        this.queryForm.recordId = row.recordid
        this.fetchData()
        this.dialogTableVisible = true
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
        const { data } = await getCostStatisticsDetail(this.queryForm)
        this.list = [data]
        this.listLoading = false
      },
      close() {
        this.dialogTableVisible = false
      },
    },
  }
</script>
