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
      <el-table-column align="center" label="借阅人" prop="realname" />
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
  import { getJyrz_countlist } from '@/api/audit/archives'
  import { doDelete } from '@/api/table'
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
          projectid: '',
          pageNumber: 1,
          pageSize: 20,
        },
      }
    },
    created() {},
    methods: {
      /**
       * @description: 打开表单
       * @param {*} row 传入数据
       * @return {*}
       */
      showEdit(row) {
        this.dialogFormVisible = true

        if (!row) {
          this.title = '添加'
        } else {
          this.title = '借阅详情'
          this.queryForm.projectid = row.projectId
          this.fetchData()
        }
      },
      /**
       * @description: 改变每一页请求数量
       * @param {*} val
       * @return {*}
       */
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      /**
       * @description: 跳转页数
       * @param {*} val
       * @return {*}
       */
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.fetchData()
      },
      queryData() {
        this.queryForm.pageNumber = 1
        this.fetchData()
      },
      /**
       * @description: 数据请求
       * @return {*}
       */
      async fetchData() {
        this.listLoading = true
        const {
          data: {
            pageInfo: { list, totalRecord },
          },
        } = await getJyrz_countlist(this.queryForm)

        this.list = list
        this.total = totalRecord
        this.listLoading = false
      },
      /**
       * @description: 打开新增表单弹框
       * @return {*}
       */
      handleAdd() {
        this.$refs['edit'].showEdit()
      },
      handleEdit(row) {
        this.$refs['edit'].showEdit(row)
      },
      /**
       * @description: 删除列表数据
       * @param {*} row 当前行数据
       * @return {*}
       */
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg } = await doDelete({ ids: row.id })
          this.$baseMessage(msg, 'success', 'vab-hey-message-success')
          await this.fetchData()
        })
      },
      /**
       * @description: 关闭弹窗并清理缓存数据
       * @return {*}
       */
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
