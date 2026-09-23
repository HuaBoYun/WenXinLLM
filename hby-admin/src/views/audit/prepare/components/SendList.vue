<template>
  <div>
    <el-dialog
      :visible.sync="dialogVisible"
      width="1000px"
      title="下发列表"
      :close-on-click-modal="false"
      v-if="dialogVisible"
      :modal="false"
    >
      <el-table
        v-loading="listLoading"
        ref="multipleTable"
        :data="list"
        :row-key="getRowKeys"
      >
        <el-table-column label="序号" type="index" width="60" />
        <el-table-column prop="createstaffName" label="下发人姓名"></el-table-column>
        <el-table-column prop="createtime" label="下发时间"></el-table-column>
        <el-table-column prop="reciver" label="接收人ID"></el-table-column>
        <el-table-column prop="reciverName" label="接收人姓名"></el-table-column>
        <el-table-column prop="recivedate" label="接收时间"></el-table-column>
        <el-table-column prop="isread" label="是否接收">
          <template slot-scope="scope">
            {{ scope.row.isread == 1 ? '是' : '否' }}
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
    </el-dialog>
  </div>
</template>
<script>
  import { getNoticeIssueList } from '@/api/audit/preparation'
  import { UTCformat } from '@/utils'
  export default {
    data() {
      return {
        listLoading: false,
        dialogVisible: false,
        list: [],
        queryForm: {
          pageNumber: 1,
          pageSize: 20,
          AdviceId: '',
        },
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        select: [],
      }
    },
    created() {},
    methods: {
      showEdit(row) {
        this.queryForm.AdviceId = row.adviceid
        this.fetchData()
        this.dialogVisible = true
      },
      fetchData() {
        this.listLoading = true
        getNoticeIssueList(this.queryForm).then((res) => {
          console.log(res)
          this.list = res.data.pageInfo.tlist
          this.total = res.data.pageInfo.totalRecord
          this.listLoading = false
        })
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
      // 2、设置row-key
      getRowKeys(row) {
        return row.staffid
      },
      // 3、勾选列表操作
      handleSelectionChange(selection) {
        // selection.shift()
        this.select = selection.map((item) => item.staffid)
      },
      // 4、回显已勾选的数据
      setCheckedRows() {
        let selectItem = []
        this.list.forEach((item) => {
          this.select.forEach((id) => {
            if (item.staffid === id) {
              selectItem.push(item)
            }
          })
        })
        this.$refs.multipleTable.toggleRowSelection(selectItem)
      },
      /**
       * @description: 关闭弹窗并清理缓存数据
       * @return {*}
       */      
      close() {
        this.dialogVisible = false
        this.select = []
      },
    },
  }
</script>
