<template>
  <div>
    <el-dialog
      :title="title"
      :visible.sync="dialogFormVisible"
      width="800px"
      @close="close"
    >
      <el-table v-loading="listLoading" :data="list">
        <el-table-column align="center" label="导入人" prop="staffName" />
        <el-table-column
          align="center"
          label="导入时间"
          prop="createTime"
          show-overflow-tooltip
          :formatter="formatDate"
        ></el-table-column>
        <el-table-column align="center" label="总数量" prop="totalCount" />
        <el-table-column align="center" label="已导入数量" prop="importCount" />
        <el-table-column
          align="center"
          label="导入结果"
          prop="importContent"
          show-overflow-tooltip
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
    </el-dialog>
  </div>
</template>

<script>
  import { getSystemImportLogList } from '@/api/setting/org'
  import { formatDay } from '@/utils/index'

  export default {
    name: 'record',
    data() {
      return {
        title: '导入记录',
        dialogFormVisible: false,
        list: [],
        listLoading: false,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          importType: 1,
          pageNumber: 1,
          pageSize: 20,
        },
      }
    },
    props: {},
    methods: {
      formatDate(row, column) {
        // 获取单元格数据
        let data = row[column.property]
        return formatDay(data)
      },
      show(type) {
        this.queryForm.importType = type || 1
        this.dialogFormVisible = true
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
      async fetchData() {
        this.listLoading = true
        const {
          data: { records, total },
        } = await getSystemImportLogList(this.queryForm)
        this.list = records
        this.total = total
        this.listLoading = false
      },
      close() {
        this.dialogFormVisible = false
      },
    },
  }
</script>

<style></style>
