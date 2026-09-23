<template>
  <el-dialog
    title="项目"
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
        label="审计项目名称"
        width="120"
        prop="xmname"
      ></el-table-column>
      <el-table-column
        prop="xmtype"
        label="审计项目类型"
        width="120"
      ></el-table-column>
      <el-table-column
        prop="borgname"
        label="被审计单位"
        show-overflow-tooltip
      ></el-table-column>
      <el-table-column
            align="center"
            label="状态"
            prop="status"
          >
            <template #default="{ row }">
              {{
                row.status == 1
                  ? '启动'
                  : '未启动'
              }}
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
    <template #footer>
      <el-button @click="dialogVisible = false">取 消</el-button>
      <el-button type="primary" @click="save">确 定</el-button>
    </template>
  </el-dialog>
</template>
<script>
  import { UTCformat } from '@/utils'
 import { xmglqdNewList } from '@/oapi/audit/project'
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
          data: {pageInfo:{ tlist, totalRecord }},
        } = await xmglqdNewList(this.queryForm)
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
