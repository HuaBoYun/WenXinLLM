<template>
  <el-dialog
    :close-on-click-modal="false"
    :append-to-body="true"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
  >
    <el-table
      v-loading="loading"
      :data="list"
      highlight-current-row
      @current-change="handleSelected"
      ref="multipleTable"
      row-key="pkAccount"
      :tree-props="{ children: 'childrenList' }"
    >
      <el-table-column align="center" label="分录序号" prop="detailindex" />
      <el-table-column align="center" label="科目名称" prop="accasoaName" />
      <el-table-column align="center" label="摘要" prop="explanation" />
      <el-table-column align="center" label="会计期间" prop="periodv" />
      <el-table-column align="center" label="日期" prop="prepareddatev">
        <template slot-scope="scope">
          <span>{{ scope.row.prepareddatev.slice(0, 10) }}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="借方金额" prop="debitamount" />
      <el-table-column align="center" label="贷方金额" prop="creditamount" />
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
  import { getFinanceDataList } from '@/api/cwsc.js'
  export default {
    name: 'accTypeModel',
    components: {},
    inheritAttrs: false,
    props: ['pkOrg'],
    data() {
      return {
        dialogFormVisible: false,
        loading: false,
        list: [],
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        title: '明细',
        queryForm: {
          pageNo: 1,
          pageSize: 20,
          pkVoucher: '',
          pkOrg: '',
        },
        current: {},
      }
    },
    computed: {},
    watch: {},
    created() {},
    async mounted() {
      if (!localStorage.getItem('bookInfo')) await getUserSelectedBookInfo()
      const bookInfo = localStorage.getItem('bookInfo')
      this.bookInfo = JSON.parse(bookInfo)
    },
    methods: {
      handleSelected(val) {
        this.current = val
      },
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      handleCurrentChange(val) {
        this.queryForm.pageNo = val
        this.fetchData()
      },
      reset() {
        this.queryForm = {
          pageNo: 1,
          pageSize: 20,
          pkVoucher: '',
          pkOrg: this.pkOrg,
        }
        this.fetchData()
      },
      showEdit(data) {
        this.queryForm.pkVoucher = encodeURIComponent(data.pkVoucher)
        this.fetchData()
        this.dialogFormVisible = true
      },
      async fetchData() {
        let params = JSON.parse(JSON.stringify(this.queryForm))
        params.pkOrg = this.pkOrg
        this.loading = true
        const {
          data: { records, total },
        } = await getFinanceDataList(params)
        this.list = records
        this.total = total
        this.loading = false
      },
      close() {
        this.dialogFormVisible = false
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
<style scoped>
  .el-form-item__content span {
    font-size: 14px;
    font-weight: 500;
    color: darkgray;
  }
</style>
