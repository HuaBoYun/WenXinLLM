<template>
  <el-dialog
    :close-on-click-modal="false"
    title="线索编号"
    :visible.sync="dialogVisible"
    :append-to-body="true"
    width="50%"
  >
    <el-table
      ref="multipleTable"
      v-loading="listLoading"
      :data="list"
      tooltip-effect="dark"
      style="width: 100%"
      @select="handleSelection"
    >
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column label="线索编号" prop="cluenaber"></el-table-column>
      <el-table-column label="核实内容" prop="verifycontent"></el-table-column>
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
      <el-button @click="dialogVisible = false">取 消</el-button>
      <el-button type="primary" @click="save">确 定</el-button>
    </template>
  </el-dialog>
</template>
<script>
  import { yshListBYNew } from '@/api/audit/wgzz'
  export default {
    data() {
      return {
        dialogVisible: false,
        multipleSelection: [],
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          pageNumber: 1,
          pageSize: 20,
        },
      }
    },
    methods: {
      async show() {
        this.dialogVisible = true
        this.listLoading = true
        const {
          data: {
            pageInfo: { tlist: list, totalRecord: total },
          },
        } = await yshListBYNew(this.queryForm)
        this.list = list
        this.total = total
        this.listLoading = false
        this.$forceUpdate()
      },
      handleSelection(val) {
        if (val.length > 1) {
          let del = val.shift()
          this.$refs.multipleTable.toggleRowSelection(del, false)
        }
        this.multipleSelection = val
      },
      /**
       * @description: 保存表单
       * @return {*}
       */      
      save() {
        this.$emit('selected', this.multipleSelection[0])
        this.dialogVisible = false
      },
      /**
       * @description: 改变每一页请求数量
       * @param {*} val
       * @return {*}
       */
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.show()
      },
      /**
       * @description: 跳转页数
       * @param {*} val
       * @return {*}
       */
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.show()
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
</style>
