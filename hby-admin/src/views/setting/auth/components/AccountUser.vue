<template>
  <el-drawer
    :before-close="close"
    size="1000px"
    :title="title"
    :visible.sync="dialogFormVisible"
  >
    <el-table :data="list">
      <el-table-column align="center" label="用户真实姓名" prop="realname" />
      <el-table-column align="center" label="用户名" prop="username" />
      <el-table-column align="center" label="所属公司" prop="orgName" />
      <el-table-column align="center" label="地址" prop="address" />
    </el-table>
    <el-pagination
      background
      :current-page="queryForm.pageNo"
      :layout="layout"
      :page-size="queryForm.pageSize"
      :total="total"
      @current-change="handleCurrentChange"
      @size-change="handleSizeChange"
    />
    <template #footer>
      <el-button @click="close">关 闭</el-button>
    </template>
    <ContractInfo ref="contractInfo" />
  </el-drawer>
</template>

<script>
  import ContractInfo from '@/views/contract/opposite/components/ContractInfo'
  import { bookdetail } from '@/api/setting/auth'
  export default {
    name: 'AccountUser',
    components: { ContractInfo },
    data() {
      return {
        queryForm: {
          code: '',
          name: '',
          pageNo: 1,
          pageSize: 20,
        },
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 3,
        title: '用户信息',
        dialogFormVisible: false,
        list: [],
      }
    },
    created() {},
    methods: {
      showEdit(row) {
        this.queryForm.pid = row.pid
        this.queryForm.bookid = row.bookid
        this.fetchData()
        this.dialogFormVisible = true
      },
      close() {
        this.dialogFormVisible = false
      },
      async fetchData() {
        this.listLoading = true
        const {
          pageInfo: { tlist, totalRecord },
        } = await bookdetail(this.queryForm)
        this.list = tlist
        this.total = totalRecord
        this.listLoading = false
      },
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.fetchData()
      },
    },
  }
</script>
<style scoped></style>
