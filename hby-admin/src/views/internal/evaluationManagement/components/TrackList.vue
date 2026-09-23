<template>
  <el-dialog
    :close-on-click-modal="false"
    :append-to-body="true"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
  >
    <el-table :data="list">
      <el-table-column align="center" label="序号" type="index" />
      <el-table-column align="center" label="评价编号" prop="ASSESSID" />
      <el-table-column align="center" label="项目名称" prop="ASSESSNAME" />
      <el-table-column align="center" label="评价人" prop="REALNAME" />
      <el-table-column align="center" label="状态" prop="STATUS">
        <template #default="{ row }">
          {{ row.STATUS == 0 ? '未处理' : '已处理' }}
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
    <template #footer>
      <el-button @click="close">关 闭</el-button>
    </template>
  </el-dialog>
</template>

<script>
  import { getProjPerson } from '@/api/internal/trace'
  export default {
    name: 'TrackList',
    data() {
      return {
        queryForm: {
          selectedPlans: '',
          orgid: '',
          pageNumber: 1,
          pageSize: 20,
        },
        total: 0,
        list: [],
        title: '跟踪详情',
        dialogFormVisible: false,
      }
    },
    created() {},
    methods: {
      showEdit(row) {
        this.queryForm.selectedPlans = row.assid
        this.queryForm.orgid = row.assorgid
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
        const {
          data: {
            pageBean: { records: list, total },
          },
        } = await getProjPerson(this.queryForm)
        this.list = list
        this.total = total
        this.listLoading = false
      },
      close() {
        this.dialogFormVisible = false
      },
    },
  }
</script>
