<template>
  <el-dialog
    :visible.sync="dialogVisible"
    width="1000px"
    :close-on-click-modal="false"
  >
    <div class="system-log-container">
      <vab-query-form>
        <vab-query-form-right-panel :span="24">
          <el-button @click="dialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="save">确 定</el-button>
        </vab-query-form-right-panel>
      </vab-query-form>
      <el-table
        :data="list"
        ref="multipleTable"
        style="width: 100%"
        @select="handleSelection"
      >
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column label="底稿名称" prop="sheetCode"></el-table-column>
        <el-table-column prop="sheetName" label="底稿名称"></el-table-column>
        <el-table-column prop="realname" label="拟稿人"></el-table-column>
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
    </div>
  </el-dialog>
</template>
<script>
  import { sendManuscriptGzdg, sendManuscriptAtt } from '@/api/audit/implement'

  export default {
    data() {
      return {
        listLoading: false,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        dialogVisible: false,
        list: [],
        queryForm: {
          pageNumber: 1,
          pageSize: 20,
        },
        select: [],
        dpointid: '',
      }
    },
    methods: {
      showEdit(e) {
        this.dialogVisible = true
        this.getExecutorList(e)
        this.dpointid = e.dpointid
        this.select = []
      },
      async getExecutorList(row) {
        const data = await sendManuscriptGzdg({
          auditStaff: row.editor,
          selectIds: row.dpointid,
          type: 'nbsj',
        })
        this.list = data.data.pageInfo.tlist
        this.total = data.data.pageInfo.totalRecord
      },
      /**
       * @description: 改变每一页请求数量
       * @param {*} val
       * @return {*}
       */
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.getExecutorList()
      },
      /**
       * @description: 跳转页数
       * @param {*} val
       * @return {*}
       */
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.getExecutorList()
      },
      /**
       * @description: 保存表单
       * @return {*}
       */
      save() {
        if (this.select.length == 0) {
          this.$baseMessage('请选择底稿！', 'error', 'vab-hey-message-error')
          return
        }

        sendManuscriptAtt({
          workId: this.select[0].sheetId,
          type: 'nbsj',
          selectIds: this.dpointid,
          // modelTye: 'nbsj_yigl',
        }).then((res) => {
          if (res.code == 1) {
            this.$message.success('发送成功')
            this.dialogVisible = false
            this.$emit('fetch-data')
          }
        })
        // this.$emit('projectManage', this.select)
      },
      handleSelection(val) {
        this.current = val
        if (val.length > 1) {
          let del = val.shift()
          this.$refs.multipleTable.toggleRowSelection(del, false)
        }
        this.select = val
      },
    },
  }
</script>
<style scoped lang="scss">
  //隐藏表头全选框
  ::v-deep thead {
    .el-table-column--selection {
      .el-checkbox__inner {
        display: none !important;
      }
    }
  }
  .lr-layout {
    display: flex;
  }

  .lr-layout > .left {
    width: 350px;
    border-right: 1px solid ghostwhite;
    margin-right: 100px;
    padding-right: 10px;
  }

  .lr-layout > .right {
    flex: 1;
  }
</style>
