<template>
  <el-dialog
    :visible.sync="dialogVisible"
    :close-on-click-modal="false"
    :append-to-body="true"
    title="版本选择"
    width="50%"
    @close="close"
  >
    <vab-query-form>
      <vab-query-form-right-panel :span="24">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </vab-query-form-right-panel>
    </vab-query-form>
    <el-table
      :data="tableData"
      style="width: 100%"
      @select="handleSelection"
      ref="multipleTable"
    >
      <el-table-column
        type="selection"
        width="55"
        :selectable="handleSelectable"
      ></el-table-column>
      <el-table-column
        prop="financedbtype"
        label="数据库类型"
      ></el-table-column>
      <el-table-column prop="financeconn" label="数据库连接"></el-table-column>
      <el-table-column prop="financeuser" label="数据库用户"></el-table-column>
      <el-table-column prop="fintext" label="数据源名称"></el-table-column>
    </el-table>
  </el-dialog>
</template>

<script>
  import { getDataSourceList } from '@/api/cwsc'
  export default {
    props: ['fid'],
    data() {
      return {
        dialogVisible: false,
        tableData: [],
        current: [],
        select: [],
      }
    },
    methods: {
      showEdit() {
        this.dialogVisible = true
        this.getVersionList()
      },
      async getVersionList() {
        const {
          data: { records, total },
        } = await getDataSourceList({ planid: this.fid })
        this.tableData = records
      },
      close() {
        this.dialogVisible = false
        this.tableData = []
      },
      handleSelectable(row) {
        // 通过是否存在 childrenList 判断是否为一级节点
        // 如果存在子节点（一级节点），则禁用勾选
        return !row.childrenList
      },
      handleSelection(val) {
        this.current = val
        if (val.length > 1) {
          let del = val.shift()
          this.$refs.multipleTable.toggleRowSelection(del, false)
        }
        this.select = val
      },
      save() {
        if (this.select.length == 0) {
          this.$baseMessage('请选择数据源！', 'error', 'vab-hey-message-error')
          return
        }
        this.$emit('dataSourceManage', this.select)
        this.dialogVisible = false
      },
    },
  }
</script>

<style scoped lang="scss">
  ::v-deep .is-disabled {
    display: none !important;
  }

  ::v-deep thead {
    .el-table-column--selection {
      .el-checkbox__inner {
        display: none !important;
      }
    }
  }
</style>
