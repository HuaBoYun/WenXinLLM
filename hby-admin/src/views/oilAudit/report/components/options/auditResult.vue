<template>
  <el-dialog
    title="审计结果确认单"
    :visible.sync="dialogVisible"
    width="50%"
    :close-on-click-modal="false"
    :append-to-body="true"
  >
    <el-table
      ref="multipleTable"
      :data="tableData"
      tooltip-effect="dark"
      style="width: 100%"
      @select="handleSelection"
    >
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column label="编号" prop="resultcode"></el-table-column>
      <el-table-column
        label="审计项目名称"
        width="120"
        prop="projectname"
      ></el-table-column>
      <el-table-column
        prop="orgidnames"
        label="被审计单位"
        show-overflow-tooltip
      ></el-table-column>
      <el-table-column
        prop="contractname"
        label="合同名称"
        width="120"
      ></el-table-column>
      <el-table-column
        prop="contractcode"
        label="合同编号"
        show-overflow-tooltip
      ></el-table-column>
      <el-table-column
        prop="orgidnames"
        label="审计对象人"
        show-overflow-tooltip
      >
        <!-- <template slot-scope="scope">
          {{ scope.row.externalassig == 0 ? '否' : '是' }}
        </template> -->
      </el-table-column>
    </el-table>
    <el-pagination
      class="pagination"
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
import { UTCformat } from '@/utils'
import { resultList } from '@/oapi/audit/implement'
export default {
  data() {
    return {
      dialogVisible: false,
      tableData: [],
      multipleSelection: [],
      layout: 'total, sizes, prev, pager, next, jumper',
      queryForm: {
        pageNumber: 1,
        pageSize: 20,
      },
      total: 0,
    }
  },
  methods: {
    async getData() {
      const { data, code, msg } = await resultList(this.queryForm)
      this.tableData = data.data.tlist
      this.total = data.data.totalRecord || 0
    },
    async showEdit() {
      this.dialogVisible = true
      this.getData()
    },
    handleSelection(val) {
      if (val.length > 1) {
        let del = val.shift()
        this.$refs.multipleTable.toggleRowSelection(del, false)
      }
      this.multipleSelection = val
    },
    save() {
      this.$emit('resultList', this.multipleSelection)
      this.dialogVisible = false
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
