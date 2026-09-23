<template>
  <el-dialog
    title="计划项目"
    :visible.sync="dialogVisible"
    width="50%"
    :modal="false"
    :close-on-click-modal="false"
  >
    <el-table
      ref="multipleTable"
      :data="tableData"
      tooltip-effect="dark"
      style="width: 100%"
      @select="handleSelection"
    >
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column
        align="center"
        label="项目编号"
        prop="qdcode"
        width="100"
      ></el-table-column>
      <el-table-column align="center" label="项目名称" prop="projectName" />
      <el-table-column
        align="center"
        label="项目负责人"
        prop="projectOrderName"
        show-overflow-tooltip
      />
      <el-table-column
        align="center"
        label="项目类别"
        prop="projectType"
        show-overflow-tooltip
      >
        <template #default="{ row }">
          {{ row.projectType == 1 ? '计划内' : '归档' }}
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        label="计划开始时间"
        prop="planStarttime"
        show-overflow-tooltip
      />
      <el-table-column
        align="center"
        label="计划结束时间"
        prop="planEndtime"
        show-overflow-tooltip
      />
      <el-table-column
        align="center"
        label="计划年度"
        prop="planYear"
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
      <el-button @click="dialogVisible = false">取 消</el-button>
      <el-button type="primary" @click="save">确 定</el-button>
    </template>
  </el-dialog>
</template>
<script>
  import { planingList } from '@/oapi/audit/project'
  export default {
    data() {
      return {
        dialogVisible: false,
        tableData: [],
        multipleSelection: [],
        queryForm: {
          pageNumber: 1,
          pageSize: 20,
        },
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
      }
    },
    methods: {
      /**
       * @description  初始化
       * @param {*}
       * @return {*}
       */
      /**
       * @description: 外部打开内部dialog
       * @param {*} title 表单类型
       * @param {*} row 编辑、详情带入的数据
       * @return {*}
       */
      async showEdit() {
        this.dialogVisible = true
        const {
          data: { tlist, totalRecord },
        } = await planingList(this.queryForm)
        this.tableData = tlist || []
        this.total = totalRecord || 0
      },
      /**
       * @description  选择列表数据，把数据存入multipleSelection
       * @param {*}
       * @return {*}
       */
      handleSelection(val) {
        if (val.length > 1) {
          let del = val.shift()
          this.$refs.multipleTable.toggleRowSelection(del, false)
        }
        this.multipleSelection = val
      },
      /**
       * @description   点击确定,把数据回传到父组件,关闭当前组件
       * @param {*}
       * @return {*}
       */
      save() {
        if (this.multipleSelection.length == 0) {
          this.$baseMessage('请选择项目！', 'error', 'vab-hey-message-error')
          return
        }
        this.$emit('selected', this.multipleSelection)
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
  // 隐藏表头全选框
  ::v-deep thead {
    .el-table-column--selection {
      .el-checkbox__inner {
        display: none !important;
      }
    }
  }
</style>
