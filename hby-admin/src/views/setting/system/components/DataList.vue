<template>
  <el-dialog
    :title="title"
    :visible.sync="dialogVisible"
    width="1000px"
    @close="close"
    :close-on-click-modal="false"
  >
    <el-table v-loading="listLoading" :data="list">
      <el-table-column
        align="center"
        label="机构编号"
        prop="orgnumber"
        show-overflow-tooltip
        width="230px"
      />
      <el-table-column
        align="center"
        label="机构名称"
        prop="orgname"
        show-overflow-tooltip
      />
      <el-table-column
        align="center"
        label="机构简介"
        prop="orgmeno"
        show-overflow-tooltip
      />
      <el-table-column
        align="center"
        label="备注"
        prop="memo"
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
</template>
<script>
  import { getDataInitialList } from '@/api/setting/system'
  export default {
    name: 'DataList',
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
        dialogVisible: false,
        title: '行业架构',
      }
    },
    created() {
      this.fetchData()
    },
    methods: {
      showInit() {
        this.dialogVisible = true
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
          data: { tlist, totalRecord },
        } = await getDataInitialList(this.queryForm)
        this.list = tlist
        this.total = totalRecord
        this.listLoading = false
      },
      close() {
        this.dialogVisible = false
      },
    },
  }
</script>
