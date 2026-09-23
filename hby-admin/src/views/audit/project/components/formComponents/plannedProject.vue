<template>
  <el-dialog
    title="计划项目"
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
      <el-table-column
        label="项目名称"
        width="120"
        prop="projectname"
      ></el-table-column>
      <el-table-column
        prop="targetname"
        label="工作目标"
        width="120"
      ></el-table-column>
      <el-table-column
        prop="finishtime"
        label="计划完成时间"
        show-overflow-tooltip
      ></el-table-column>
      <el-table-column
        prop="orgidnames"
        label="被审计单位"
        show-overflow-tooltip
      ></el-table-column>
      <el-table-column
        prop="externalassig"
        label="是否外委"
        show-overflow-tooltip
      >
        <template slot-scope="scope">
          {{ scope.row.externalassig == 0 ? '否' : '是' }}
        </template>
      </el-table-column>
    </el-table>
    <template #footer>
      <el-button @click="dialogVisible = false">取 消</el-button>
      <el-button type="primary" @click="save">确 定</el-button>
    </template>
  </el-dialog>
</template>
<script>
  import { UTCformat } from '@/utils'
  export default {
    data() {
      return {
        dialogVisible: false,
        tableData: [],
        multipleSelection: [],
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
      async showEdit(resPlanProjectArr) {
        resPlanProjectArr.map((v) => {
          v.finishtime = UTCformat(v.finishtime)
        })
        this.tableData = resPlanProjectArr
        this.dialogVisible = true
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
        this.$emit('plannedList', this.multipleSelection)
        this.dialogVisible = false
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
