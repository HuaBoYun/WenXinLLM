<template>
  <el-dialog
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
    :close-on-click-modal="false"
  >
    <el-table v-loading="listLoading" :data="list">
      <el-table-column align="center" label="序号" width="50" type="index">
        <template slot-scope="scope">
          {{ scope.$index + 1 }}
        </template>
      </el-table-column>
      <el-table-column align="center" label="借阅人" prop="tblstaff.realname" />
      <el-table-column
        align="center"
        label="借阅时间"
        prop="createDate"
        show-overflow-tooltip
      >
        <template slot-scope="{ row }">
          {{ dayjs(row.createDate).format('YYYY-MM-DD') }}
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        label="归还时间"
        prop="returnDate"
        show-overflow-tooltip
      >
        <template slot-scope="{ row }">
          {{ dayjs(row.returnDate).format('YYYY-MM-DD') }}
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        label="借阅事由"
        prop="memo"
        show-overflow-tooltip
      />
    </el-table>
    <!-- <el-pagination
      background
      :current-page="queryForm.pageNumber"
      :layout="layout"
      :page-size="queryForm.pageSize"
      :total="total"
      @current-change="handleCurrentChange"
      @size-change="handleSizeChange"
    /> -->
  </el-dialog>
</template>

<script>
  import { getJyrzxqList } from '@/oapi/audit/archives'
  import { doDelete } from '@/oapi/table'
  import * as dayjs from 'dayjs'
  export default {
    name: 'LogInfo',
    data() {
      return {
        dayjs: dayjs,
        title: '',
        dialogFormVisible: false,
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          id: '',
          pageNumber: 1,
          pageSize: 20,
        },
      }
    },
    created() {},
    methods: {
      showEdit(row) {
        this.dialogFormVisible = true

        if (!row) {
          this.title = '添加'
        } else {
          this.title = '借阅详情'
          this.queryForm.id = row.id
          this.fetchData()
        }
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
          data: { list },
        } = await getJyrzxqList(this.queryForm)

        this.list = list
        // this.total = totalRecord
        this.listLoading = false
      },
      handleAdd() {
        this.$refs['edit'].showEdit()
      },
      handleEdit(row) {
        this.$refs['edit'].showEdit(row)
      },
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg } = await doDelete({ ids: row.id })
          this.$baseMessage(msg, 'success', 'vab-hey-message-success')
          await this.fetchData()
        })
      },
      close() {
        this.$refs['form'].resetFields()
        this.form = this.$options.data().form
        this.dialogFormVisible = false
      },
      sendModel() {
        this.$refs['sendModel'].showEdit()
      },
      send() {
        this.$refs['send'].showEdit()
      },
    },
  }
</script>
