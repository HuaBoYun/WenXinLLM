<template>
  <el-dialog
    :close-on-click-modal="false"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
  >
    <el-table :data="tableData" v-loading="listLoading">
      <el-table-column align="center" label="计划编号" prop="PLANNUMBER" />
      <el-table-column align="center" label="计划名称" prop="PLANNAME" />
      <el-table-column align="center" label="测试人" prop="REALNAME" />
      <el-table-column align="center" label="状态" prop="ZT" />
      <el-table-column align="center" label="是否提交" prop="SFTJ" />
    </el-table>
    <el-pagination
      background
      :current-page="queryForm.pageNumber"
      :page-size="queryForm.pageSize"
      :total="total"
      @current-change="handleCurrentChange"
      @size-change="handleSizeChange"
    />
    <template #footer>
      <el-button @click="close">关 闭</el-button>
    </template>
  </el-dialog>
</template>

<script>
  import { csgzList } from '@/api/internal/new/track'
  export default {
    name: 'TrackView',
    data() {
      return {
        queryForm: {
          selectProjectid: '',
          pageNumber: 1,
          pageSize: 20,
        },
        listLoading: true,
        total: 0,
        tableData: [],
        title: '任务跟踪',
        dialogFormVisible: false,
      }
    },
    created() {},
    methods: {
      showEdit(row) {
        this.queryForm.selectProjectid = row.testplanid
        this.queryData()
        this.dialogFormVisible = true
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
        const { data, msg, code } = await csgzList(this.queryForm)
        if (code == 200) {
          this.tableData = data.pageBean.records
          this.total = data.pageBean.total
          this.listLoading = false
        } else {
          this.listLoading = false
          this.$baseMessage(msg, 'error')
        }
      },
      close() {
        this.tableData = []
        this.dialogFormVisible = false
      },
    },
  }
</script>
