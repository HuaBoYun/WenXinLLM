<template>
  <el-dialog
    title="立项建议表"
    :visible.sync="dialogVisible"
    width="50%"
    append-to-body
    :close-on-click-modal="false"
  >
    <el-table
      ref="multiTable"
      :data="tableList"
      tooltip-effect="dark"
      style="width: 100%"
      :max-height="600"
      @selection-change="handleSelection"
      v-loading="listLoading"
    >
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column
        align="center"
        label="序号"
        prop="id"
        width="100"
      ></el-table-column>
      <el-table-column align="center" label="审计事项" prop="projectName" />
      <el-table-column
        align="center"
        label="立项理由及审计目的"
        prop="projectPurpose"
        show-overflow-tooltip
      />
      <el-table-column
        align="center"
        label="重点关注内容"
        prop="concernsContent"
        show-overflow-tooltip
      />
      <el-table-column
        align="center"
        label="单位范围"
        prop="unitRange"
        show-overflow-tooltip
      />
      <el-table-column
        align="center"
        label="时间范围"
        prop="timeRange"
        show-overflow-tooltip
      />
      <el-table-column
        align="center"
        label="项目类型"
        prop="projectType"
        show-overflow-tooltip
      />
      <el-table-column
        align="center"
        label="备注"
        prop="remark"
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
    <template #footer>
      <el-button @click="handleClose">取 消</el-button>
      <el-button type="primary" @click="save">确 定</el-button>
    </template>
  </el-dialog>
</template>
<script>
  import { getList } from '@/api/oilAudit/jhgl/lxjyb'
  export default {
    data() {
      return {
        dialogVisible: false,
        tableList: [],
        curMulSelection: [], //当前所有已选择项
        multipleSelection: [],
        listLoading: true,
        queryForm: {
          pageNumber: 1,
          pageSize: 20,
        },
        total: 0,
        layout: 'total, sizes, prev, pager, next, jumper',
      }
    },
    methods: {
      showEdit(curMulSelection) {
        this.curMulSelection = curMulSelection
        this.dialogVisible = true
        this.getList()
      },
      async getList() {
        this.listLoading = true
        const {
          code,
          data: { tlist, totalRecord },
        } = await getList(this.queryForm)
        if (code === 1) {
          this.tableList = tlist
          this.total = totalRecord
          const arr = tlist.filter((row) =>
            this.curMulSelection.some((item) => item.id === row.id)
          )
          this.$nextTick(() => {
            arr.forEach((row) => {
              this.$refs.multiTable.toggleRowSelection(row)
            })
          })
        }
        this.listLoading = false
      },
      handleSelection(val) {
        console.log(val)
        // if (val.length > 1) {
        //   let del = val.shift()
        //   this.$refs.multiTable.toggleRowSelection(del, false)
        // }
        this.multipleSelection = val
      },
      save() {
        const arr = this.curMulSelection.concat(this.multipleSelection)
        const uniqueArray = arr.filter(
          (item, index, self) =>
            index === self.findIndex((t) => t.id === item.id)
        )
        this.$emit('changeTableData1', uniqueArray)
        this.dialogVisible = false
      },
      handleSizeChange(val) {
        this.queryForm.pageNumber = 1
        this.queryForm.pageSize = val
        this.getList()
      },
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.getList()
      },
      handleClose() {
        this.queryForm = {
          pageNumber: 1,
          pageSize: 20,
        }
        this.tableList = []
        this.curMulSelection = []
        this.multipleSelection = []
        this.total = 0
        this.dialogVisible = false
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
